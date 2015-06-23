 Attribute VB_Name = "M11_LRT_MQT"
 Option Explicit
 
 Private Const pc_tempTabNamePrivOid = "SESSION.PrivOid"
 Private Const pc_tempTabNamePubOid = "SESSION.PubOid"
 
 Private Const processingStep = 2
 
 
 Sub genLrtMqtSupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If Not g_genLrtSupport Then
     Exit Sub
   End If

   If ddlType = edtLdm Then
     genLrtMqtSupportDdlByType(edtLdm)

     genLrtMqtSupportDdlByPool()
   ElseIf ddlType = edtPdm Then
     genLrtMqtSupportDdlByType(edtPdm)

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(thisPoolIndex).supportLrt Then
         For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
             genLrtMqtSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
           End If
          Next thisOrgIndex
        End If
      Next thisPoolIndex
   End If
 End Sub
 
 
 Private Sub genLrtMqtSupportDdlByType( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit

   Dim thisOrgId As Integer
   Dim thisPoolId As Integer

   Dim fileNo As Integer
 ' ### IF IVK ###
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, , , , phaseLrtMqt, ldmIterationPoolSpecific)
 ' ### ELSE IVK ###
 ' fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, , , , phaseLrtMqt, ldmIterationPoolSpecific)
 ' ### ENDIF IVK ###
 
   ' ####################################################################################################################
   ' #    SP for Synchronizing LRT-MQTs
   ' ####################################################################################################################

   Dim qualProcNameMqtSync As String
   qualProcNameMqtSync = genQualProcName(g_sectionIndexDbAdmin, spnLrtMqtSync, ddlType)
   Dim unqualProcNameMqtSync As String
   unqualProcNameMqtSync = getUnqualObjName(qualProcNameMqtSync)

   printSectionHeader("SP for Synchronizing LRT-MQTs", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameMqtSync
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to synchronize MQTs for")
   genProcParm(fileNo, "OUT", "orgCount_out", "INTEGER", True, "number of organizations processed")
   genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables synchronized")
   genProcParm(fileNo, "OUT", "rowCount_out", "BIGINT", False, "number of rows affected")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_tabCount", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_rowCount", "BIGINT", "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "initialize output variables")
   Print #fileNo, addTab(1); "SET orgCount_out = 0;"
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader(fileNo, "loop over all 'matching' organizations")
   Print #fileNo, addTab(1); "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "O.ID"
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

   Dim i As Integer
   For i = 1 To g_pools.numDescriptors
       If g_pools.descriptors(i).supportLrt Then
         Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; g_schemaNameCtoDbAdmin; "' || "; _
                               "CAST(RIGHT('00' || RTRIM(CAST(ID AS CHAR(2))),2) || '"; CStr(g_pools.descriptors(i).id); "' AS CHAR(3)) || '."; unqualProcNameMqtSync; _
                               "(?,?)';"
         Print #fileNo,
         Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
         Print #fileNo,
         Print #fileNo, addTab(2); "EXECUTE"
         Print #fileNo, addTab(3); "v_stmnt"
         Print #fileNo, addTab(2); "INTO"
         Print #fileNo, addTab(3); "v_tabCount,"
         Print #fileNo, addTab(3); "v_rowCount"
         Print #fileNo, addTab(2); ";"

         genProcSectionHeader(fileNo, "accumulate counter values", 2)
         Print #fileNo, addTab(2); "SET orgCount_out = orgCount_out + 1;"
         Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + v_tabCount;"
         Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
       End If
   Next i

   Print #fileNo, addTab(1); "END FOR;"

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
 
 
 Private Sub genLrtMqtSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseLrtMqt, ldmIterationPoolSpecific)
 
   ' ####################################################################################################################
   ' #    SP for Synchronizing LRT-MQTs
   ' ####################################################################################################################

   Dim qualProcNameMqtSync As String
   qualProcNameMqtSync = genQualProcName(g_sectionIndexDbAdmin, spnLrtMqtSync, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("SP for Synchronizing LRT-MQTs", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameMqtSync
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables synchronized")
   genProcParm(fileNo, "OUT", "rowCount_out", "BIGINT", False, "number of rows affected")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_rowCount", "BIGINT", "NULL")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "initialize output variables")
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   If ddlType = edtLdm Then
     genProcSectionHeader(fileNo, "loop over all 'LRT-MQT-tables'")
   Else
     genProcSectionHeader(fileNo, "loop over all 'LRT-MQT-tables' (organization " & genOrgId(thisOrgIndex, ddlType, True) & " / data pool " & g_pools.descriptors(thisPoolIndex).id & ")")
   End If

   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"

   Print #fileNo, addTab(2); "SELECT "
   If ddlType = edtPdm Then
     Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
     Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName"
   Else
     Print #fileNo, addTab(3); "L."; g_anLdmSchemaName; " AS c_schemaName,"
     Print #fileNo, addTab(3); "L."; g_anLdmTableName; " AS c_tableName"
   End If
   Print #fileNo, addTab(2); "FROM"

   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LM"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LM."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = LM."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = LM."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LM."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LM."; g_anLdmIsMqt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LM."; g_anLdmIsNl; " = L."; g_anLdmIsNl
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LM."; g_anLdmIsGen; " = L."; g_anLdmIsGen

   If ddlType = edtPdm Then
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   End If

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "A."; g_anAcmUseLrtMqt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
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

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '."; UCase("MqtSync_"); "' || c_tableName || '(?)';"

   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader(fileNo, "accumulate counter values", 2)
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit(fileNo, qualProcNameMqtSync, ddlType, , "tabCount_out", "rowCount_out")

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
 
 Sub genLrtMqtSupportForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNoTab As Integer, _
   fileNoView As Integer, _
   fileNoFk As Integer, _
   fileNoMqt As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional isPurelyPrivate As Boolean = False _
 )
   If Not useMqtToImplementLrt Then
     Exit Sub
   End If

   Dim poolSuppressUniqueConstraints As Boolean
   Dim poolSupportLrt As Boolean
   If thisPoolIndex > 0 Then
       poolSuppressUniqueConstraints = g_pools.descriptors(thisPoolIndex).suppressUniqueConstraints
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
   End If

   Dim orMappingRootEntityIndex As Integer
   Dim sectionIndex As Integer
   Dim sectionName As String
   Dim entityName As String
   Dim entityShortName As String
   Dim entityTypeDescr As String
   Dim tabSpaceData As String
   Dim tabSpaceIndexData As Integer
   Dim tabSpaceLong As String
   Dim tabSpaceIndexLong As Integer
   Dim tabSpaceIndex As String
   Dim tabSpaceIndexIndex As Integer
   Dim useValueCompression As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
 ' ### IF IVK ###
   Dim tableIsPsTagged As Boolean
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
 ' ### ENDIF IVK ###
   Dim isVolatile As Boolean
   Dim isAggHead As Boolean
 ' ### IF IVK ###
   Dim objSupportsPsDpFilter As Boolean
 ' ### ENDIF IVK ###
   Dim useMqtToImplementLrtForEntity As Boolean
   Dim hasVirtualAttrs As Boolean
 ' ### IF IVK ###
   Dim isAllowedCountriesRel As RelNavigationMode
   Dim isDisallowedCountriesRel As RelNavigationMode
 ' ### ENDIF IVK ###
   Dim acFkColName As String
   Dim acClassIndex As Integer
   Dim acOoParClassIndex As Integer
 ' ### IF IVK ###
   Dim qualCountryListFuncName As String
 ' ### ENDIF IVK ###
   Dim acColName As String
   Dim acColLength As Integer
 ' ### IF IVK ###
   Dim condenseData As Boolean
   Dim expandExpressionsInFtoView As Boolean
   Dim fkAttrToDiv As String
   Dim isDivTagged As Boolean
   Dim supportPartitionByClassId As Boolean

   Dim useDivOidWhereClause As Boolean
   Dim useDivRelKey As Boolean
 
   isAllowedCountriesRel = ernmNone
   isDisallowedCountriesRel = ernmNone
 ' ### ENDIF IVK ###
   acClassIndex = -1
   acOoParClassIndex = -1
 ' ### IF IVK ###
   qualCountryListFuncName = ""
 ' ### ENDIF IVK ###

   On Error GoTo ErrorExit

   If acmEntityType = eactClass Then

       useDivOidWhereClause = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex = g_classIndexGenericCode) And Not isPsTagged
       useDivRelKey = (acmEntityIndex = g_classIndexGenericCode) And Not forNl
 
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Class"
       orMappingRootEntityIndex = g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex
       useValueCompression = g_classes.descriptors(acmEntityIndex).useValueCompression
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
 ' ### IF IVK ###
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = g_classes.descriptors(acmEntityIndex).psTagOptional
 ' ### ENDIF IVK ###
       isVolatile = g_classes.descriptors(acmEntityIndex).isVolatile
       isAggHead = g_classes.descriptors(acmEntityIndex).isAggHead And Not forGen And Not forNl
 ' ### IF IVK ###
       tableIsPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged And (usePsTagInNlTextTables Or Not forNl)
       objSupportsPsDpFilter = g_classes.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
 ' ### IF IVK ###
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       expandExpressionsInFtoView = g_classes.descriptors(acmEntityIndex).expandExpressionsInFtoView

       supportPartitionByClassId = supportRangePartitioningByClassId And Not forNl And g_classes.descriptors(acmEntityIndex).subClassIdStrSeparatePartition.numMaps > 0

       hasVirtualAttrs = Not forNl And ((forGen And g_classes.descriptors(acmEntityIndex).hasExpBasedVirtualAttrInGenInclSubClasses) Or (Not forGen And g_classes.descriptors(acmEntityIndex).hasExpBasedVirtualAttrInNonGenInclSubClasses))

       isDivTagged = (g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex > 0) And Not (g_classes.descriptors(acmEntityIndex).classIndex = g_classIndexProductStructure)
       If isDivTagged Then
           If g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex).navPathToDiv.navDirectionToClass = etLeft Then
             fkAttrToDiv = g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex).leftFkColName(ddlType)
           ElseIf forNl Then
             fkAttrToDiv = conDivOid
           Else
             fkAttrToDiv = g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex).rightFkColName(ddlType)
           End If
       End If
 ' ### ENDIF IVK ###

       tabSpaceData = g_classes.descriptors(acmEntityIndex).tabSpaceData
       tabSpaceIndexData = g_classes.descriptors(acmEntityIndex).tabSpaceIndexData
       tabSpaceLong = g_classes.descriptors(acmEntityIndex).tabSpaceLong
       tabSpaceIndexLong = g_classes.descriptors(acmEntityIndex).tabSpaceIndexLong
       tabSpaceIndex = g_classes.descriptors(acmEntityIndex).tabSpaceIndex
       tabSpaceIndexIndex = g_classes.descriptors(acmEntityIndex).tabSpaceIndexIndex
   ElseIf acmEntityType = eactRelationship Then
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Relationship"
       orMappingRootEntityIndex = g_relationships.descriptors(acmEntityIndex).relIndex
       useValueCompression = g_relationships.descriptors(acmEntityIndex).useValueCompression
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
 ' ### IF IVK ###
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = False
 ' ### ENDIF IVK ###
       isVolatile = g_relationships.descriptors(acmEntityIndex).isVolatile
       isAggHead = False
 ' ### IF IVK ###
       tableIsPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged And (usePsTagInNlTextTables Or Not forNl)
       objSupportsPsDpFilter = g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
 ' ### IF IVK ###
       condenseData = False
       expandExpressionsInFtoView = False
       supportPartitionByClassId = False
       hasVirtualAttrs = False

       isAllowedCountriesRel = g_relationships.descriptors(acmEntityIndex).isAllowedCountries
       isDisallowedCountriesRel = g_relationships.descriptors(acmEntityIndex).isDisallowedCountries

       If g_relationships.descriptors(acmEntityIndex).isDisallowedCountries <> ernmNone Or g_relationships.descriptors(acmEntityIndex).isAllowedCountries <> ernmNone Then
         acClassIndex = IIf(g_relationships.descriptors(acmEntityIndex).isDisallowedCountries = ernmLeft Or g_relationships.descriptors(acmEntityIndex).isAllowedCountries = ernmLeft, g_relationships.descriptors(acmEntityIndex).rightEntityIndex, g_relationships.descriptors(acmEntityIndex).leftEntityIndex)
           acOoParClassIndex = g_classes.descriptors(acClassIndex).orMappingSuperClassIndex
           acFkColName = genSurrogateKeyName(ddlType, g_classes.descriptors(acClassIndex).shortName)
       End If

       If g_relationships.descriptors(acmEntityIndex).isAllowedCountries <> ernmNone Then
         qualCountryListFuncName = genQualFuncName(g_relationships.descriptors(acmEntityIndex).sectionIndex, udfnAllowedCountry2Str0, ddlType, thisOrgIndex, thisPoolIndex)
         acColName = g_anAllowedCountries
         acColLength = gc_allowedCountriesMaxLength
       ElseIf g_relationships.descriptors(acmEntityIndex).isDisallowedCountries <> ernmNone Then
         qualCountryListFuncName = genQualFuncName(g_relationships.descriptors(acmEntityIndex).sectionIndex, udfnDisallowedCountry2Str0, ddlType, thisOrgIndex, thisPoolIndex)
         acColName = g_anDisAllowedCountries
         acColLength = gc_disallowedCountriesMaxLength
       End If

       isDivTagged = False
       If Not forNl Then
         If g_relationships.descriptors(acmEntityIndex).leftIsDivision Then
           isDivTagged = True
           fkAttrToDiv = genSurrogateKeyName(ddlType, g_relationships.descriptors(acmEntityIndex).rlShortRelName)
         ElseIf g_relationships.descriptors(acmEntityIndex).rightIsDivision Then
           isDivTagged = True
           fkAttrToDiv = genSurrogateKeyName(ddlType, g_relationships.descriptors(acmEntityIndex).lrShortRelName)
         End If
       End If

 ' ### ENDIF IVK ###
       tabSpaceData = g_relationships.descriptors(acmEntityIndex).tabSpaceData
       tabSpaceIndexData = g_relationships.descriptors(acmEntityIndex).tabSpaceIndexData
       tabSpaceLong = g_relationships.descriptors(acmEntityIndex).tabSpaceLong
       tabSpaceIndexLong = g_relationships.descriptors(acmEntityIndex).tabSpaceIndexLong
       tabSpaceIndex = g_relationships.descriptors(acmEntityIndex).tabSpaceIndex
       tabSpaceIndexIndex = g_relationships.descriptors(acmEntityIndex).tabSpaceIndexIndex
   Else
     Exit Sub
   End If

   If Not useMqtToImplementLrtForEntity Then
     Exit Sub
   End If

   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors

   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameMqt As String
   Dim qualTabNamePriv As String
   Dim qualTabNamePub As String
   Dim qualTabNameMqtLdm As String
   qualTabNameMqt = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True, forNl)
   qualTabNamePriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, False, forNl)
   qualTabNamePub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, forNl)
   qualTabNameMqtLdm = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, edtLdm, thisOrgIndex, thisPoolIndex, forGen, True, True, forNl)

   Dim i As Integer
 ' ### IF IVK ###
   Dim showDeletedObjectsInView As Boolean
   Dim filterForPsDpMapping As Boolean
   Dim filterForPsDpMappingExtended As Boolean
 ' ### ENDIF IVK ###
   Dim qualViewName As String
   Dim qualViewNameLdm As String
   Dim qualAcTableName As String
   Dim propToPriv As Boolean
 ' ### IF IVK ###
   Dim tabPartitionType As PartitionType
 ' ### ENDIF IVK ###

   If ddlType = edtPdm And Not poolSupportLrt Then
     Exit Sub
     ' this is handled with non-MQT-LRT
   End If

   ' ####################################################################################################################
   ' #    MQT for LRT-Views
   ' ####################################################################################################################
 
   addTabToDdlSummary(qualTabNameMqt, ddlType, False)

   registerQualTable(qualTabNameMqtLdm, qualTabNameMqt, orMappingRootEntityIndex, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, ddlType, False, forGen, True, forNl, True)

   If generateDdlCreateTable Then
     printChapterHeader("LRT-MQT-Table for " & entityTypeDescr & " """ & sectionName & "." & entityName & """" & IIf(forGen, " (GEN)", "") & IIf(forNl, " (NL)", ""), fileNoTab)

     Print #fileNoTab,
     Print #fileNoTab, addTab(0); "CREATE TABLE"
     Print #fileNoTab, addTab(1); qualTabNameMqt
     Print #fileNoTab, addTab(0); "("

 ' ### IF IVK ###
     If forNl Then
       genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTab, , , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, True, edomDecl Or edomMqtLrt Or edomDeclVirtual)
     Else
       genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTab, ddlType, thisOrgIndex, thisPoolIndex, 1, True, forGen, edomDecl Or edomMqtLrt Or edomDeclVirtual)
     End If
 ' ### ELSE IVK ###
 '   If forNl Then
 '     genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTab, , , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, True, edomDecl Or edomMqtLrt
 '   Else
 '     genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTab, ddlType, thisOrgIndex, thisPoolIndex, 1, True, forGen, edomDecl Or edomMqtLrt
 '   End If
 ' ### ENDIF IVK ###

     Print #fileNoTab, ")"

 ' ### IF IVK ###
     genTabDeclTrailer(fileNoTab, ddlType, isDivTagged, acmEntityType, acmEntityIndex, thisOrgIndex, thisPoolIndex, forNl, True, True, supportPartitionByClassId, fkAttrToDiv, tabPartitionType)
 ' ### ELSE IVK ###
 '   genTabDeclTrailer fileNoTab, ddlType, acmEntityType, acmEntityIndex, thisOrgIndex, thisPoolIndex, forNl, True, True
 ' ### ENDIF IVK ###

     Print #fileNoTab, gc_sqlCmdDelim
   End If

 ' ### IF IVK ###
   If (forNl And Not isPsTagged) Or isVolatile Then
 ' ### ELSE IVK ###
 ' If forNl Or isVolatile Then
 ' ### ENDIF IVK ###
     Print #fileNoTab,
     Print #fileNoTab, addTab(0); "ALTER TABLE "; qualTabNameMqt; " VOLATILE CARDINALITY"; gc_sqlCmdDelim
   End If

 ' ### IF IVK ###
   'Defect 19643 wf
   'Einmaliger Aufruf: Indexe fuer VL6CPST011.PROPERTY_GEN_LRT_MQT
   genIndexesForEntity(qualTabNameMqt, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNoTab, ddlType, forGen, True, True, forNl, poolSuppressUniqueConstraints, tabPartitionType)

   If acmEntityType = eactClass And Not forNl Then
     genFKsForRelationshipsByClassRecursive(qualTabNameMqt, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, forGen, True, True, tabPartitionType)
   End If
 ' ### ELSE IVK ###
 ' genIndexesForEntity qualTabNameMqt, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNoTab, ddlType, forGen, True, True, forNl, poolSuppressUniqueConstraints
 '
 ' If acmEntityType = eactClass And Not forNl Then
 '  genFKsForRelationshipsByClassRecursive qualTabNameMqt, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, forGen, True, True
 ' End If
 ' ### ENDIF IVK ###

   Dim qualTriggerName As String

   ' ####################################################################################################################
   ' #    INSERT Trigger
   ' ####################################################################################################################

 ' ### IF IVK ###
   qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen And Not forNl, , , , , IIf(forNl, "NLTXT", "") & "_INS")
 ' ### ELSE IVK ###
 ' qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, , "INS")
 ' ### ENDIF IVK ###

   printSectionHeader("Insert-Trigger for maintaining LRT-MQT-table for table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoMqt)
   Print #fileNoMqt,
   Print #fileNoMqt, addTab(0); "CREATE TRIGGER"
   Print #fileNoMqt, addTab(1); qualTriggerName
   Print #fileNoMqt, addTab(0); "AFTER INSERT ON"
   Print #fileNoMqt, addTab(1); qualTabNamePub
   Print #fileNoMqt, addTab(0); "REFERENCING"
   Print #fileNoMqt, addTab(1); "NEW AS "; gc_newRecordName
   Print #fileNoMqt, addTab(0); "FOR EACH ROW"
   Print #fileNoMqt, addTab(0); "BEGIN ATOMIC"

   Dim printedHeader As Boolean
 ' ### IF IVK ###
   Dim numVirtualAttrs As Integer
   Dim numVirtualAttrsInstantiated As Integer
   printedHeader = False
   numVirtualAttrs = 0
   numVirtualAttrsInstantiated = 0

   If hasVirtualAttrs Then
     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 0)
     transformation.doCollectVirtualAttrDescriptors = True
     transformation.doCollectAttrDescriptors = True
     setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName)

     If forNl Then
       genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomXref)
     Else
       genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomXref)
     End If

     Dim k As Integer
     For k = 1 To tabColumns.numDescriptors
         If tabColumns.descriptors(k).columnCategory And eacVirtual Then
           numVirtualAttrs = numVirtualAttrs + 1
           If tabColumns.descriptors(k).isInstantiated Then numVirtualAttrsInstantiated = numVirtualAttrsInstantiated + 1
         End If
     Next k

     For k = 1 To tabColumns.numDescriptors
         If tabColumns.descriptors(k).columnCategory And eacVirtual Then
           If Not printedHeader Then
             genProcSectionHeader(fileNoMqt, "declare variables")
             printedHeader = True
           End If
           genVarDecl(fileNoMqt, "v_" & tabColumns.descriptors(k).acmAttributeName, getDbDatatypeByDomainIndex(tabColumns.descriptors(k).dbDomainIndex), "NULL")
         End If
     Next k

     printedHeader = False
     For k = 1 To tabColumns.numDescriptors
         If tabColumns.descriptors(k).columnCategory And eacVirtual Then
           If Not printedHeader Then
             genProcSectionHeader(fileNoMqt, "initialize variables")
             printedHeader = True
           End If
           Print #fileNoMqt, addTab(1); "SET "; "v_"; tabColumns.descriptors(k).acmAttributeName; " = "; transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomValueVirtual, , , True); ";"
         End If
     Next k

     genProcSectionHeader(fileNoMqt, "update virtual columns in public table")

     Print #fileNoMqt, addTab(1); "UPDATE"
     Print #fileNoMqt, addTab(2); qualTabNamePub; " PUB"
     Print #fileNoMqt, addTab(1); "SET"

     Dim firstAttr As Boolean
     firstAttr = True
     For k = 1 To tabColumns.numDescriptors
         If (tabColumns.descriptors(k).columnCategory And eacVirtual) And tabColumns.descriptors(k).isInstantiated Then
           If Not firstAttr Then
             Print #fileNoMqt, ","
           End If
           firstAttr = False
           Print #fileNoMqt, addTab(2); "PUB."; tabColumns.descriptors(k).columnName; " = v_"; tabColumns.descriptors(k).acmAttributeName;
         End If
     Next k

     Print #fileNoMqt,
     Print #fileNoMqt, addTab(1); "WHERE"
     Print #fileNoMqt, addTab(2); "PUB."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
     Print #fileNoMqt, addTab(1); ";"
   End If

 ' ### ENDIF IVK ###
   genProcSectionHeader(fileNoMqt, "propagate INSERT to MQT-table")
   Print #fileNoMqt, addTab(1); "INSERT INTO"
   Print #fileNoMqt, addTab(2); qualTabNameMqt
   Print #fileNoMqt, addTab(1); "("
 
 ' ### IF IVK ###
   If forNl Then
     genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomListNonLrt Or edomMqtLrt Or edomListVirtual Or edomListExpression)
   Else
     genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomListNonLrt Or edomMqtLrt Or edomListVirtual Or edomListExpression)
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomListNonLrt Or edomMqtLrt
 ' Else
 '   genAttrListForEntity acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomListNonLrt Or edomMqtLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(1); ")"
   Print #fileNoMqt, addTab(1); "VALUES"
   Print #fileNoMqt, addTab(1); "("

 ' ### IF IVK ###
   initAttributeTransformation(transformation, 2 + numVirtualAttrs, , , , gc_newRecordName & ".")
 ' ### ELSE IVK ###
 ' initAttributeTransformation transformation, 2, , , , gc_newRecordName & "."
 ' ### ENDIF IVK ###
   setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName)
   setAttributeMapping(transformation, 1, conIsLrtPrivate, gc_dbFalse)
   setAttributeMapping(transformation, 2, conInUseBy, "(SELECT LRT.UTROWN_OID FROM " & qualTabNameLrt & " LRT WHERE LRT." & g_anOid & " = " & gc_newRecordName & "." & g_anInLrt & ")")

 ' ### IF IVK ###
   If hasVirtualAttrs Then
     numVirtualAttrs = 0
     For k = 1 To tabColumns.numDescriptors
         If tabColumns.descriptors(k).columnCategory And eacVirtual Then
           numVirtualAttrs = numVirtualAttrs + 1

           setAttributeMapping(transformation, 2 + numVirtualAttrs, tabColumns.descriptors(k).columnName, "v_" & tabColumns.descriptors(k).acmAttributeName)
         End If
     Next k
   End If
 
   If forNl Then
     genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomMqtLrt Or edomValueVirtual Or edomValueExpression)
   Else
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt Or edomMqtLrt Or edomValueVirtual Or edomValueExpression)
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomMqtLrt
 ' Else
 '   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt Or edomMqtLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(1); ");"

 ' ### IF IVK ###
   If (isAllowedCountriesRel Or isDisallowedCountriesRel) And maintainVirtAttrInTriggerPubOnRelTabs Then
     ' update in public -> propagate to private and public table
     For i = 1 To 2
       propToPriv = (i = 2)
       qualAcTableName = genQualTabNameByClassIndex(acOoParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, propToPriv)

       genProcSectionHeader(fileNoMqt, "propagate INSERT to table """ & qualAcTableName & """", 1)
       Print #fileNoMqt, addTab(1); "UPDATE"
       Print #fileNoMqt, addTab(2); qualAcTableName; " E"
       Print #fileNoMqt, addTab(1); "SET"
       Print #fileNoMqt, addTab(2); "E."; acColName; " = "; qualCountryListFuncName; "("; gc_newRecordName; "."; acFkColName; IIf(propToPriv, ", E." & g_anInLrt & "", ""); ", "; CStr(acColLength); ")"
       Print #fileNoMqt, addTab(1); "WHERE"
       Print #fileNoMqt, addTab(2); "E."; g_anOid; " = "; gc_newRecordName; "."; acFkColName
       Print #fileNoMqt, addTab(1); ";"
     Next i
   End If

 ' ### ENDIF IVK ###
   Print #fileNoMqt, "END"
   Print #fileNoMqt, gc_sqlCmdDelim

   ' ####################################################################################################################
 
 ' ### IF IVK ###
   qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen And Not forNl, , , , , IIf(forNl, "NLTXT", "") & "L_INS")
 ' ### ELSE IVK ###
 ' qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , True, , "INS")
 ' ### ENDIF IVK ###

   printSectionHeader("Insert-Trigger for maintaining LRT-MQT-table for table """ & qualTabNamePriv & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoMqt)
   Print #fileNoMqt,
   Print #fileNoMqt, addTab(0); "CREATE TRIGGER"
   Print #fileNoMqt, addTab(1); qualTriggerName
   Print #fileNoMqt, addTab(0); "AFTER INSERT ON"
   Print #fileNoMqt, addTab(1); qualTabNamePriv
   Print #fileNoMqt, addTab(0); "REFERENCING"
   Print #fileNoMqt, addTab(1); "NEW AS "; gc_newRecordName
   Print #fileNoMqt, addTab(0); "FOR EACH ROW"
   Print #fileNoMqt, addTab(0); "BEGIN ATOMIC"

 ' ### IF IVK ###
   numVirtualAttrs = 0
   numVirtualAttrsInstantiated = 0

   If hasVirtualAttrs Then
     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 0)
     transformation.doCollectVirtualAttrDescriptors = True
     transformation.doCollectAttrDescriptors = True
     setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName, gc_newRecordName & "." & g_anInLrt)

     If forNl Then
       genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomXref)
     Else
       genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomXref)
     End If

     For k = 1 To tabColumns.numDescriptors
         If tabColumns.descriptors(k).columnCategory And eacVirtual Then
           numVirtualAttrs = numVirtualAttrs + 1
           If tabColumns.descriptors(k).isInstantiated Then numVirtualAttrsInstantiated = numVirtualAttrsInstantiated + 1
         End If
     Next k
   End If

 ' ### ENDIF IVK ###
   genProcSectionHeader(fileNoMqt, "propagate INSERT to MQT-table")
   Print #fileNoMqt, addTab(1); "INSERT INTO"
   Print #fileNoMqt, addTab(2); qualTabNameMqt
   Print #fileNoMqt, addTab(1); "("

 ' ### IF IVK ###
   If forNl Then
     genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, edomListLrt Or edomMqtLrt Or edomListVirtual Or edomListExpression)
   Else
     genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, True, forGen, edomListLrt Or edomMqtLrt Or edomListVirtual Or edomListExpression)
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, edomListLrt Or edomMqtLrt
 ' Else
 '   genAttrListForEntity acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, True, forGen, edomListLrt Or edomMqtLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(1); ")"
   Print #fileNoMqt, addTab(1); "VALUES"
   Print #fileNoMqt, addTab(1); "("

 ' ### IF IVK ###
   initAttributeTransformation(transformation, 2 + numVirtualAttrsInstantiated, , , , gc_newRecordName & ".")
 ' ### ELSE IVK ###
 ' initAttributeTransformation transformation, 2, , , , gc_newRecordName & "."
 ' ### ENDIF IVK ###
   setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName, , True)
   setAttributeMapping(transformation, 1, conIsLrtPrivate, gc_dbTrue)
   setAttributeMapping(transformation, 2, conInUseBy, "(SELECT LRT.UTROWN_OID FROM " & qualTabNameLrt & " LRT WHERE LRT." & g_anOid & " = " & gc_newRecordName & "." & g_anInLrt & ")")

 ' ### IF IVK ###
   If hasVirtualAttrs Then
     numVirtualAttrsInstantiated = 0
     For k = 1 To tabColumns.numDescriptors
         If (tabColumns.descriptors(k).columnCategory And eacVirtual) <> 0 And tabColumns.descriptors(k).isInstantiated Then
           numVirtualAttrsInstantiated = numVirtualAttrsInstantiated + 1
           setAttributeMapping(transformation, 2 + numVirtualAttrsInstantiated, tabColumns.descriptors(k).columnName, gc_newRecordName & "." & UCase(tabColumns.descriptors(k).columnName))
         End If
     Next k
   End If
 
   If forNl Then
     genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, , edomListLrt Or edomMqtLrt Or edomValueVirtual Or edomValueVirtualNonPersisted Or edomValueVirtual Or edomValueExpression)
   Else
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt Or edomMqtLrt Or edomValueVirtual Or edomValueVirtualNonPersisted Or edomValueVirtual Or edomValueExpression)
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, , edomListLrt Or edomMqtLrt
 ' Else
 '   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt Or edomMqtLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(1); ");"

   Print #fileNoMqt, "END"
   Print #fileNoMqt, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UPDATE Trigger
   ' ####################################################################################################################

 ' ### IF IVK ###
   qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen And Not forNl, , , , , IIf(forNl, "NLTXT", "") & "_UPD")
 ' ### ELSE IVK ###
 ' qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, , "UPD")
 ' ### ENDIF IVK ###

   printSectionHeader("Update-Trigger for maintaining LRT-MQT-table for table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoMqt)

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(0); "CREATE TRIGGER"
   Print #fileNoMqt, addTab(1); qualTriggerName
   Print #fileNoMqt, addTab(0); "AFTER UPDATE ON"
   Print #fileNoMqt, addTab(1); qualTabNamePub
   Print #fileNoMqt, addTab(0); "REFERENCING"
   Print #fileNoMqt, addTab(1); "NEW AS "; gc_newRecordName
   Print #fileNoMqt, addTab(1); "OLD AS "; gc_oldRecordName
   Print #fileNoMqt, addTab(0); "FOR EACH ROW"
   Print #fileNoMqt, addTab(0); "BEGIN ATOMIC"

   genProcSectionHeader(fileNoMqt, "propagate UPDATE to MQT-table", , True)
   Print #fileNoMqt, addTab(1); "UPDATE"
   Print #fileNoMqt, addTab(2); qualTabNameMqt; " MQT"
   Print #fileNoMqt, addTab(1); "SET"
   Print #fileNoMqt, addTab(2); "("

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation(transformation, 2)
   setAttributeMapping(transformation, 1, conIsLrtPrivate, gc_dbFalse)
   setAttributeMapping(transformation, 2, conOid, "")

 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   Else
     genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt
 ' Else
 '   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(1); "="
   Print #fileNoMqt, addTab(2); "("

   initAttributeTransformation(transformation, 2, , , , gc_newRecordName & ".")
   setAttributeMapping(transformation, 1, conIsLrtPrivate, gc_dbFalse)
   setAttributeMapping(transformation, 2, conOid, "")

   transformation.attributePrefix = gc_newRecordName & "."
   setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName)
 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt Or edomListVirtual Or edomValueVirtualNonPersisted Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   Else
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt Or edomListVirtual Or edomValueVirtualNonPersisted Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt
 ' Else
 '   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt
 ' End If
 ' ### ENDIF IVK ###
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(1); "WHERE"
   Print #fileNoMqt, addTab(2); "MQT."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
   Print #fileNoMqt, addTab(3); "AND"
   Print #fileNoMqt, addTab(2); "MQT."; g_anIsLrtPrivate; " = "; gc_dbFalse
   genDdlPsDivClause(fileNoMqt, 2, "MQT", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
   Print #fileNoMqt, addTab(1); ";"
 ' ### IF IVK ###

   If (isAllowedCountriesRel Or isDisallowedCountriesRel) And maintainVirtAttrInTriggerPubOnRelTabs Then
     ' update in public -> propagate to private and public table
     For i = 1 To 2
       propToPriv = (i = 2)
       qualAcTableName = genQualTabNameByClassIndex(acOoParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, propToPriv)

       genProcSectionHeader(fileNoMqt, "propagate UPDATE to table """ & qualAcTableName & """", 1)
       Print #fileNoMqt, addTab(1); "UPDATE"
       Print #fileNoMqt, addTab(2); qualAcTableName; " E"
       Print #fileNoMqt, addTab(1); "SET"
       Print #fileNoMqt, addTab(2); "E."; acColName; " = "; qualCountryListFuncName; "("; gc_newRecordName; "."; acFkColName; IIf(propToPriv, ", E." & g_anInLrt & "", ""); ", "; CStr(acColLength); ")"
       Print #fileNoMqt, addTab(1); "WHERE"
       Print #fileNoMqt, addTab(2); "E."; g_anOid; " = "; gc_newRecordName; "."; acFkColName
       genDdlPsDivClause(fileNoMqt, 2, "E", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
       Print #fileNoMqt, addTab(1); ";"

       Print #fileNoMqt, addTab(1); "IF "; gc_newRecordName; "."; acFkColName; " <> "; gc_oldRecordName; "."; acFkColName; " THEN"
       Print #fileNoMqt, addTab(2); "UPDATE"
       Print #fileNoMqt, addTab(3); qualAcTableName; " E"
       Print #fileNoMqt, addTab(2); "SET"
       Print #fileNoMqt, addTab(3); "E."; acColName; " = "; qualCountryListFuncName; "("; gc_oldRecordName; "."; acFkColName; IIf(propToPriv, ", E." & g_anInLrt & "", ""); ", "; CStr(acColLength); ")"
       Print #fileNoMqt, addTab(2); "WHERE"
       Print #fileNoMqt, addTab(3); "E."; g_anOid; " = "; gc_oldRecordName; "."; acFkColName
       genDdlPsDivClause(fileNoMqt, 2, "E", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
       Print #fileNoMqt, addTab(2); ";"
       Print #fileNoMqt, addTab(1); "END IF;"
     Next i
   End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, "END"
   Print #fileNoMqt, gc_sqlCmdDelim

   ' ####################################################################################################################

 ' ### IF IVK ###
   qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen And Not forNl, , , , , IIf(forNl, "NLTXT", "") & "L_UPD")
 ' ### ELSE IVK ###
 ' qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl, , "UPD")
 ' ### ENDIF IVK ###

   printSectionHeader("Update-Trigger for maintaining LRT-MQT-table for table """ & qualTabNamePriv & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoMqt)

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(0); "CREATE TRIGGER"
   Print #fileNoMqt, addTab(1); qualTriggerName
   Print #fileNoMqt, addTab(0); "AFTER UPDATE ON"
   Print #fileNoMqt, addTab(1); qualTabNamePriv
   Print #fileNoMqt, addTab(0); "REFERENCING"
   Print #fileNoMqt, addTab(1); "NEW AS "; gc_newRecordName
   Print #fileNoMqt, addTab(1); "OLD AS "; gc_oldRecordName
   Print #fileNoMqt, addTab(0); "FOR EACH ROW"
   Print #fileNoMqt, addTab(0); "BEGIN ATOMIC"

   genProcSectionHeader(fileNoMqt, "propagate UPDATE to MQT-table", , True)
   Print #fileNoMqt, addTab(1); "UPDATE"
   Print #fileNoMqt, addTab(2); qualTabNameMqt; " MQT"
   Print #fileNoMqt, addTab(1); "SET"
   Print #fileNoMqt, addTab(2); "("

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation(transformation, 1)
   setAttributeMapping(transformation, 1, conIsLrtPrivate, gc_dbTrue)

 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   Else
     genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt
 ' Else
 '   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(1); "="
   Print #fileNoMqt, addTab(2); "("

   transformation.attributePrefix = gc_newRecordName & "."
   setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName, gc_newRecordName & "." & g_anInLrt, True)
 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt Or edomListVirtual Or edomValueVirtualNonPersisted Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   Else
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, forGen, edomListLrt Or edomListVirtual Or edomValueVirtualNonPersisted Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt
 ' Else
 '   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, forGen, edomListLrt
 ' End If
 ' ### ENDIF IVK ###
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(1); "WHERE"
   Print #fileNoMqt, addTab(2); "MQT."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
   Print #fileNoMqt, addTab(3); "AND"
   Print #fileNoMqt, addTab(2); "MQT."; g_anIsLrtPrivate; " = "; gc_dbTrue
   Print #fileNoMqt, addTab(3); "AND"
   Print #fileNoMqt, addTab(2); "MQT."; g_anInLrt; " = "; gc_oldRecordName; "."; g_anInLrt
   genDdlPsDivClause(fileNoMqt, 2, "MQT", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
   Print #fileNoMqt, addTab(1); ";"
   Print #fileNoMqt, "END"
   Print #fileNoMqt, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    DELETE Trigger
   ' ####################################################################################################################

 ' ### IF IVK ###
   qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen And Not forNl, , , , , IIf(forNl, "NLTXT", "") & "_DEL")
 ' ### ELSE IVK ###
 ' qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "DEL")
 ' ### ENDIF IVK ###

   printSectionHeader("Delete-Trigger for maintaining LRT-MQT-table for table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoMqt)
   Print #fileNoMqt,
   Print #fileNoMqt, addTab(0); "CREATE TRIGGER"
   Print #fileNoMqt, addTab(1); qualTriggerName
   Print #fileNoMqt, addTab(0); "AFTER DELETE ON"
   Print #fileNoMqt, addTab(1); qualTabNamePub
   Print #fileNoMqt, addTab(0); "REFERENCING"
   Print #fileNoMqt, addTab(1); "OLD AS "; gc_oldRecordName
   Print #fileNoMqt, addTab(0); "FOR EACH ROW"
   Print #fileNoMqt, addTab(0); "BEGIN ATOMIC"

   genProcSectionHeader(fileNoMqt, "propagate DELETE to MQT-table")
   Print #fileNoMqt, addTab(1); "DELETE FROM"
   Print #fileNoMqt, addTab(2); qualTabNameMqt; " MQT"
   Print #fileNoMqt, addTab(1); "WHERE"
   Print #fileNoMqt, addTab(2); "MQT."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
   Print #fileNoMqt, addTab(3); "AND"
   Print #fileNoMqt, addTab(2); "MQT."; g_anIsLrtPrivate; " = "; gc_dbFalse
   genDdlPsDivClause(fileNoMqt, 2, "MQT", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
   Print #fileNoMqt, addTab(1); ";"

 ' ### IF IVK ###
   If (isAllowedCountriesRel Or isDisallowedCountriesRel) And maintainVirtAttrInTriggerPubOnRelTabs Then
     ' delete in public -> propagate to private and public table
     For i = 1 To 2
       propToPriv = (i = 2)
       qualAcTableName = genQualTabNameByClassIndex(acOoParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, propToPriv)

       genProcSectionHeader(fileNoMqt, "propagate DELETE to table """ & qualAcTableName & """", 1)
       Print #fileNoMqt, addTab(1); "UPDATE"
       Print #fileNoMqt, addTab(2); qualAcTableName; " E"
       Print #fileNoMqt, addTab(1); "SET"
       Print #fileNoMqt, addTab(2); "E."; acColName; " = "; qualCountryListFuncName; "("; gc_oldRecordName; "."; acFkColName; IIf(propToPriv, ", E." & g_anInLrt, ""); ", "; CStr(acColLength); ")"
       Print #fileNoMqt, addTab(1); "WHERE"
       Print #fileNoMqt, addTab(2); "E."; g_anOid; " = "; gc_oldRecordName; "."; acFkColName
       genDdlPsDivClause(fileNoMqt, 2, "E", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
       Print #fileNoMqt, addTab(1); ";"
     Next i
   End If

 ' ### ENDIF IVK ###
   Print #fileNoMqt, "END"
   Print #fileNoMqt, gc_sqlCmdDelim

   ' ####################################################################################################################

 ' ### IF IVK ###
   qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen And Not forNl, , , , , IIf(forNl, "NLTXT", "") & "L_DEL")
 ' ### ENDIF IVK ###
 ' qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl, , "DEL")
 ' ### ENDIF IVK ###

   printSectionHeader("Delete-Trigger for maintaining LRT-MQT-table for table """ & qualTabNamePriv & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoMqt)
   Print #fileNoMqt,
   Print #fileNoMqt, addTab(0); "CREATE TRIGGER"
   Print #fileNoMqt, addTab(1); qualTriggerName
   Print #fileNoMqt, addTab(0); "AFTER DELETE ON"
   Print #fileNoMqt, addTab(1); qualTabNamePriv
   Print #fileNoMqt, addTab(0); "REFERENCING"
   Print #fileNoMqt, addTab(1); "OLD AS "; gc_oldRecordName
   Print #fileNoMqt, addTab(0); "FOR EACH ROW"
   Print #fileNoMqt, addTab(0); "BEGIN ATOMIC"

   genProcSectionHeader(fileNoMqt, "propagate DELETE to MQT-table")
   Print #fileNoMqt, addTab(1); "DELETE FROM"
   Print #fileNoMqt, addTab(2); qualTabNameMqt; " MQT"
   Print #fileNoMqt, addTab(1); "WHERE"
   Print #fileNoMqt, addTab(2); "MQT."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
   Print #fileNoMqt, addTab(3); "AND"
   Print #fileNoMqt, addTab(2); "MQT."; g_anInLrt; " = "; gc_oldRecordName; "."; g_anInLrt
   Print #fileNoMqt, addTab(3); "AND"
   Print #fileNoMqt, addTab(2); "MQT."; g_anIsLrtPrivate; " = "; gc_dbTrue
   genDdlPsDivClause(fileNoMqt, 2, "MQT", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
   Print #fileNoMqt, addTab(1); ";"
   Print #fileNoMqt, "END"
   Print #fileNoMqt, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    View for providing LRT-specific set of public + private records
   ' ####################################################################################################################
 
 ' ### IF IVK ###
   ' we need to generate four views
   '   - one not filtering out deleted objects (first loop)
   '   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING (special feature for interfaces / second loop)
   '   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING / current division (special feature for interfaces / third loop)
   '   - one filtering out deleted objects and not filtering for Product Structures in PSDPMAPPING (fourth loop)
   ' filtering deleted objects / not filtering by PSDPMAPPING is done in fourth loop since this view is the one used in subsequent trigger definitions
   For i = 1 To 4
     showDeletedObjectsInView = (i = 1)
     filterForPsDpMapping = (i = 2)
     filterForPsDpMappingExtended = (i = 3)
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
     If filterForPsDpMapping And (Not supportFilteringByPsDpMapping Or Not objSupportsPsDpFilter) Then
       GoTo NextII
     End If
     If filterForPsDpMappingExtended And (Not supportFilteringByPsDpMapping Or Not objSupportsPsDpFilter) Then
       GoTo NextII
     End If

     qualViewName = _
       genQualViewNameByEntityIndex( _
         acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True, forNl, , _
         IIf(showDeletedObjectsInView, "D", "") & IIf(filterForPsDpMapping, "I", IIf(filterForPsDpMappingExtended, "J", "")) _
       )

     printSectionHeader("View to 'filter' private and public LRT rows of MQT-table """ & qualTabNameMqt & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoView, , _
                            "(" & IIf(showDeletedObjectsInView, "", "do not ") & "retrieve deleted objects" & _
                            IIf(supportFilteringByPsDpMapping, " / " & IIf(filterForPsDpMapping Or filterForPsDpMappingExtended, "", "do not ") & "filter by PSDPMAPPING", "") & ")")
 ' ### ELSE IVK ###
 '   qualViewName = _
 '     genQualViewNameByEntityIndex( _
 '       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True, forNl _
 '     )
 '
 '   printSectionHeader "View to 'filter' private and public LRT rows of MQT-table """ & qualTabNameMqt & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoView
 ' ### ENDIF IVK ###
     Print #fileNoView,
     Print #fileNoView, addTab(0); "CREATE VIEW"
     Print #fileNoView, addTab(1); qualViewName
     Print #fileNoView, addTab(0); "("

     If Not forGen And Not forNl Then
       printConditional(fileNoView, _
         genAttrDeclByDomain( _
           conWorkingState, conWorkingState, eavtEnum, getEnumIndexByName(dxnWorkingState, dnWorkingState), _
           acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacRegular, , 1, True _
         ))
     End If
 
 ' ### IF IVK ###
     If condenseData Then
       ' virtually merge-in columns 'INLRT', 'STATUS_ID' AND 'INUSEBY'
       printConditional(fileNoView, _
         genAttrDeclByDomain( _
           conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, _
           acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacLrtMeta, , 1, True _
         ))
       printConditional(fileNoView, _
         genAttrDeclByDomain( _
           enStatus, esnStatus, eavtEnum, g_enumIndexStatus, _
           acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacLrtMeta Or eacSetProdMeta, , 1, True _
         ))
       printConditional(fileNoView, _
         genAttrDeclByDomain( _
           conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, _
           acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacLrtMeta Or eacSetProdMeta, , 1, True _
         ))
     End If

     If forNl Then
       genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoView, "", , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, False, _
         edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomListVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
     Else
       genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoView, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, _
         edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomListVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
     End If
 ' ### ELSE IVK ###
 '   If forNl Then
 '     genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoView, "", , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, False, _
 '       edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomLrtPriv
 '   Else
 '     genAttrListForEntity acmEntityIndex, acmEntityType, fileNoView, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, _
 '       edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomLrtPriv
 '   End If
 ' ### ENDIF IVK ###

     Print #fileNoView, addTab(0); ")"
     Print #fileNoView, addTab(0); "AS"

     Print #fileNoView, addTab(0); "("
     Print #fileNoView, addTab(1); "SELECT"

     If Not forGen And Not forNl Then
 ' ### IF IVK ###
       If condenseData Then
         Print #fileNoView, addTab(2); g_dbtEnumId; "("; CStr(workingStateUnlocked); "),"
       Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
         Print #fileNoView, addTab(2); g_dbtEnumId; "("
         Print #fileNoView, addTab(3); "CASE MQT."; g_anIsLrtPrivate
         Print #fileNoView, addTab(3); "WHEN 0 THEN"
         Print #fileNoView, addTab(4); "("
         Print #fileNoView, addTab(5); "CASE"
         Print #fileNoView, addTab(6); "WHEN MQT."; g_anInLrt; " IS NULL THEN "; CStr(workingStateUnlocked)
 ' ### IF IVK ###
         If Not (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
           Print #fileNoView, addTab(6); "WHEN MQT."; g_anInLrt; " = "; g_activeLrtOidDdl; " THEN "; CStr(workingLockedInActiveTransaction)
           Print #fileNoView, addTab(6); "WHEN LRT.UTROWN_OID = (SELECT UTROWN_OID FROM "; qualTabNameLrt; _
                                     " WHERE OID = "; g_activeLrtOidDdl; ") THEN "; CStr(workingLockedInInactiveTransaction)
           If Not showDeletedObjectsInView And isAggHead Then
             Print #fileNoView, addTab(6); "WHEN ("; getActiveLrtOidStrDdl(ddlType, thisOrgIndex); " = '') AND (RTRIM(CURRENT CLIENT_USERID) = (SELECT USR."; g_anUserId; _
                                       " FROM "; g_qualTabNameUser; " USR WHERE USR."; g_anOid; " = LRT.UTROWN_OID)) THEN "; CStr(workingLockedInInactiveTransaction)
           End If
         End If
 ' ### ENDIF IVK ###
         Print #fileNoView, addTab(6); "ELSE "; CStr(workingLockedByOtherUser)
         Print #fileNoView, addTab(5); "END"
         Print #fileNoView, addTab(4); ")"

         Print #fileNoView, addTab(3); "ELSE"
         Print #fileNoView, addTab(4); CStr(workingLockedInActiveTransaction)
         Print #fileNoView, addTab(3); "END"
         Print #fileNoView, addTab(2); "),"
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
     End If

 ' ### IF IVK ###
     If condenseData Then
       ' virtually merge-in columns 'INLRT', 'STATUS_ID' and 'INUSEBY'
       Print #fileNoView, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
       Print #fileNoView, addTab(2); g_dbtEnumId; "(CASE MQT."; g_anIsLrtPrivate; " WHEN 1 THEN "; CStr(statusWorkInProgress); " ELSE "; CStr(statusProductive); " END),"
       Print #fileNoView, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
     End If

 ' ### ENDIF IVK ###
     initAttributeTransformation(transformation, 1, , , , "MQT.")

 ' ### IF IVK ###
     If filterForPsDpMapping Or filterForPsDpMappingExtended Then
       setAttributeMapping(transformation, 1, conInUseBy, "CAST(NULL AS " & g_dbtOid & ")")
     Else
       setAttributeMapping(transformation, 1, conInUseBy, "LRT.UTROWN_OID")
     End If

     If forNl Then
       genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoView, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , _
         edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomListVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
     Else
       genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoView, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, _
         edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomListVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
     End If
 ' ### ELSE IVK ###
 '   setAttributeMapping transformation, 1, conInUseBy, "LRT.UTROWN_OID"
 '
 '   If forNl Then
 '     genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoView, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , _
 '       edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomLrtPriv
 '   Else
 '     genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoView, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, _
 '       edomListLrt Or edomListNonLrt Or edomMqtLrt Or edomLrtPriv
 '   End If
 ' ### ENDIF IVK ###

     Print #fileNoView, addTab(1); "FROM"
     Print #fileNoView, addTab(2); qualTabNameMqt; " MQT"

 ' ### IF IVK ###
     If tableIsPsTagged And (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
       Print #fileNoView, addTab(1); "INNER JOIN"
       Print #fileNoView, addTab(2); g_qualTabNamePsDpMapping; " PSDPM"
       Print #fileNoView, addTab(1); "ON"
       Print #fileNoView, addTab(2); "MQT."; g_anPsOid; " = PSDPM.PSOID"

       If filterForPsDpMappingExtended Then
         Print #fileNoView, addTab(1); "INNER JOIN"
         Print #fileNoView, addTab(2); g_qualTabNamePsDpMapping; " PSDPM_SP"
         Print #fileNoView, addTab(1); "ON"
         Print #fileNoView, addTab(2); "PSDPM.DPSPARTE = PSDPM_SP.DPSPARTE"
         Print #fileNoView, addTab(3); "AND"
         Print #fileNoView, addTab(2); "("
         Print #fileNoView, addTab(3); "("; gc_db2RegVarPsOid; " = '')"
         Print #fileNoView, addTab(4); "OR"
         Print #fileNoView, addTab(3); "(PSDPM_SP.PSOID = "; g_activePsOidDdl; ")"
         Print #fileNoView, addTab(2); ")"
       End If
     End If

 ' ### ENDIF IVK ###
 ' ### IF IVK ###
     If Not (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
       Print #fileNoView, addTab(1); "LEFT OUTER JOIN"
       Print #fileNoView, addTab(2); qualTabNameLrt; " LRT"
       Print #fileNoView, addTab(1); "ON"
       Print #fileNoView, addTab(2); "MQT."; g_anInLrt; " = LRT."; g_anOid
 ' ### IF IVK ###
     End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###

     Print #fileNoView, addTab(1); "WHERE"

     Print #fileNoView, addTab(2); "("

     Print #fileNoView, addTab(3); "("
     Print #fileNoView, addTab(4); "MQT."; g_anIsLrtPrivate; " = "; gc_dbFalse
     If (Not condenseData And (filterForPsDpMapping Or filterForPsDpMappingExtended)) Then
         Print #fileNoView, addTab(5); "AND"
         Print #fileNoView, addTab(4); "MQT."; g_anIsDeleted; " = "; gc_dbFalse
     End If

 ' ### IF IVK ###
     If Not (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
       Print #fileNoView, addTab(5); "AND"
       Print #fileNoView, addTab(4); "("
       Print #fileNoView, addTab(5); "MQT."; g_anInLrt; " <> "; g_activeLrtOidDdl
       Print #fileNoView, addTab(6); "OR"
       Print #fileNoView, addTab(5); "MQT."; g_anInLrt; " IS NULL"
       Print #fileNoView, addTab(4); ")"

 ' ### IF IVK ###
       If Not showDeletedObjectsInView And Not condenseData Then
         Print #fileNoView, addTab(5); "AND"
         Print #fileNoView, addTab(4); "MQT."; g_anIsDeleted; " = "; gc_dbFalse
       End If
 ' ### ENDIF IVK ###
       Print #fileNoView, addTab(3); ")"

       Print #fileNoView, addTab(4); "OR"

       Print #fileNoView, addTab(3); "("
       Print #fileNoView, addTab(4); "MQT."; g_anIsLrtPrivate; " = "; gc_dbTrue
       If Not showDeletedObjectsInView Then
         Print #fileNoView, addTab(5); "AND"
         Print #fileNoView, addTab(4); "MQT."; g_anLrtState; " <> "; CStr(lrtStatusDeleted)
       End If
       Print #fileNoView, addTab(5); "AND"

       Print #fileNoView, addTab(4); "MQT."; g_anInLrt; " = "; g_activeLrtOidDdl
 ' ### IF IVK ###
     End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###

     Print #fileNoView, addTab(3); ")"

     Print #fileNoView, addTab(2); ")"

 ' ### IF IVK ###
     If tableIsPsTagged And Not (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
       Print #fileNoView, addTab(3); "AND"
       Print #fileNoView, addTab(2); "("

       Print #fileNoView, addTab(3); "("; gc_db2RegVarPsOid; " = '')"
       If usePsFltrByDpMappingForRegularViews Then
         Print #fileNoView, addTab(4); "OR"
         Print #fileNoView, addTab(3); "("
         Print #fileNoView, addTab(4); "("; gc_db2RegVarPsOid; " = '0')"
         Print #fileNoView, addTab(5); "AND"
         Print #fileNoView, addTab(4); "(MQT."; g_anPsOid; " IN (SELECT PSOID FROM "; g_qualTabNamePsDpMapping; "))"
         Print #fileNoView, addTab(3); ")"
       End If

       If psTagOptional Then
         Print #fileNoView, addTab(4); "OR"
         Print #fileNoView, addTab(3); "(PUB."; g_anPsOid; " IS NULL)"
       End If

       Print #fileNoView, addTab(4); "OR"
       Print #fileNoView, addTab(3); "(MQT."; g_anPsOid; " = "; g_activePsOidDdl; ")"
       Print #fileNoView, addTab(2); ")"
     End If

 ' ### ENDIF IVK ###
     Print #fileNoView, addTab(0); ")"
     Print #fileNoView, addTab(0); gc_sqlCmdDelim

     If ddlType = edtPdm Then
       qualViewNameLdm = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, edtLdm, thisOrgIndex, thisPoolIndex, forGen, True, , forNl)
 ' ### IF IVK ###
       genAliasDdl(sectionIndex, IIf(forNl, genNlObjName(entityName), entityName), _
                   isCommonToOrgs, isCommonToPools, True, _
                   qualViewNameLdm, qualViewName, False, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, True, _
                   showDeletedObjectsInView, filterForPsDpMapping, filterForPsDpMappingExtended, _
                   "LRT-View" & IIf(showDeletedObjectsInView, " (include deleted objects)", "") & _
                   IIf(supportFilteringByPsDpMapping, " (" & IIf(filterForPsDpMapping, "", "do not ") & "filter by PSDPMAPPING)", "") & _
                   " """ & sectionName & "." & entityName & """", , True, tableIsPsTagged, objSupportsPsDpFilter, , forNl)
 ' ### ELSE IVK ###
 '     genAliasDdl sectionIndex, IIf(forNl, genNlObjName(entityName), entityName), _
 '                 isCommonToOrgs, isCommonToPools, True, _
 '                 qualViewNameLdm, qualViewName, False, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, True, _
 '                 "LRT-View" & " """ & sectionName & "." & entityName & """", , True , forNl
 ' ### ENDIF IVK ###
     End If
 ' ### IF IVK ###
 NextII:
   Next i
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
 
   ' ####################################################################################################################
   ' #    SP for syncing MQT with base tables
   ' ####################################################################################################################

   Dim unqualTabName As String
   unqualTabName = getUnqualObjName(qualTabNamePriv)

   Dim qualProcNameMqtSync As String
   qualProcNameMqtSync = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl, "MqtSync")

   printSectionHeader("SP for synchronizing LRT-MQT for " & entityTypeDescr & " """ & sectionName & "." & entityName & """" & IIf(forGen, " (GEN)", "") & IIf(forNl, " (NL)", "") & " with underlying base tables", fileNoMqt)

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(0); "CREATE PROCEDURE"
   Print #fileNoMqt, addTab(1); qualProcNameMqtSync
   Print #fileNoMqt, addTab(0); "("

   genProcParm(fileNoMqt, "OUT", "rowCount_out", "BIGINT", False, "number of rows affected")

   Print #fileNoMqt, addTab(0); ")"
   Print #fileNoMqt, addTab(0); "RESULT SETS 0"
   Print #fileNoMqt, addTab(0); "LANGUAGE SQL"
   Print #fileNoMqt, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNoMqt, "declare variables", , True)
   genVarDecl(fileNoMqt, "v_rowCount", "INTEGER", "NULL")
   genVarDecl(fileNoMqt, "v_OidCount", "INTEGER", "NULL")
   genVarDecl(fileNoMqt, "v_lBound", "BIGINT", "NULL")
   genVarDecl(fileNoMqt, "v_uBound", "BIGINT", "NULL")
   genVarDecl(fileNoMqt, "v_numRowsPerUow", "BIGINT", "1000000")
   genSpLogDecl(fileNoMqt)

   genProcSectionHeader(fileNoMqt, "temporary table for private OIDs to INSERT")
   Print #fileNoMqt, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNoMqt, addTab(2); pc_tempTabNamePrivOid
   Print #fileNoMqt, addTab(1); "("
   Print #fileNoMqt, addTab(2); "seqNo INTEGER,"
   Print #fileNoMqt, addTab(2); "oid   "; g_dbtOid
   Print #fileNoMqt, addTab(1); ")"
   genDdlForTempTableDeclTrailer(fileNoMqt, 1, True, True)

   genProcSectionHeader(fileNoMqt, "temporary table for public OIDs to INSERT")
   Print #fileNoMqt, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNoMqt, addTab(2); pc_tempTabNamePubOid
   Print #fileNoMqt, addTab(1); "("
   Print #fileNoMqt, addTab(2); "seqNo INTEGER,"
   Print #fileNoMqt, addTab(2); "oid   "; g_dbtOid
   Print #fileNoMqt, addTab(1); ")"
   genDdlForTempTableDeclTrailer(fileNoMqt, 1, True, True)
 
   genSpLogProcEnter(fileNoMqt, qualProcNameMqtSync, ddlType, , "v_useLogging_in", "rowCount_out")

   genProcSectionHeader(fileNoMqt, "initialize output variables")
   Print #fileNoMqt, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader(fileNoMqt, "delete records from MQT not found in base tables")
   Print #fileNoMqt, addTab(1); "DELETE FROM"
   Print #fileNoMqt, addTab(2); qualTabNameMqt; " MQT"
   Print #fileNoMqt, addTab(1); "WHERE"
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "MQT."; g_anOid; ","
   Print #fileNoMqt, addTab(3); "MQT."; g_anIsLrtPrivate
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(2); "NOT IN"
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "SELECT"
   Print #fileNoMqt, addTab(4); "PRIV."; g_anOid; ","
   Print #fileNoMqt, addTab(4); gc_dbTrue
   Print #fileNoMqt, addTab(3); "FROM"
   Print #fileNoMqt, addTab(4); qualTabNamePriv; " PRIV"
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(3); "AND"
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "MQT."; g_anOid; ","
   Print #fileNoMqt, addTab(3); "MQT."; g_anIsLrtPrivate
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(2); "NOT IN"
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "SELECT"
   Print #fileNoMqt, addTab(4); "PUB."; g_anOid; ","
   Print #fileNoMqt, addTab(4); "0"
   Print #fileNoMqt, addTab(3); "FROM"
   Print #fileNoMqt, addTab(4); qualTabNamePub; " PUB"
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(1); ";"

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNoMqt, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(1); "COMMIT;"

   genProcSectionHeader(fileNoMqt, "update public records in MQT differing in base table")
 
   Print #fileNoMqt, addTab(1); "UPDATE"
   Print #fileNoMqt, addTab(2); qualTabNameMqt; " MQT"
   Print #fileNoMqt, addTab(1); "SET"
   Print #fileNoMqt, addTab(2); "("

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation(transformation, 2)
   setAttributeMapping(transformation, 1, conOid, "")
   setAttributeMapping(transformation, 2, conIsLrtPrivate, "")

 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   Else
     genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt
 ' Else
 '   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(1); "="
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "SELECT"

   transformation.attributePrefix = "PUB."
   setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PUB")
 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, False, , edomListNonLrt Or edomValueVirtual Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   Else
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 4, , False, forGen, edomListNonLrt Or edomValueVirtual Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, False, , edomListNonLrt
 ' Else
 '   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 4, , False, forGen, edomListNonLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(3); "FROM"
   Print #fileNoMqt, addTab(4); qualTabNamePub; " PUB"
   Print #fileNoMqt, addTab(3); "WHERE"

   Print #fileNoMqt, addTab(4); "( MQT."; conIsLrtPrivate; " = 0 )"
   Print #fileNoMqt, addTab(5); "AND"
   Print #fileNoMqt, addTab(4); "( MQT."; g_anOid; " = PUB."; g_anOid; " )"

   Print #fileNoMqt, addTab(2); ")"

   Print #fileNoMqt, addTab(1); "WHERE"
   Print #fileNoMqt, addTab(2); "EXISTS"
   Print #fileNoMqt, addTab(3); "("
   Print #fileNoMqt, addTab(4); "SELECT"
   Print #fileNoMqt, addTab(5); "1"
   Print #fileNoMqt, addTab(4); "FROM"
   Print #fileNoMqt, addTab(5); qualTabNamePub; " PUB"
   Print #fileNoMqt, addTab(4); "WHERE"

   Print #fileNoMqt, addTab(5); "( MQT."; conIsLrtPrivate; " = 0 )"
   Print #fileNoMqt, addTab(6); "AND"
   Print #fileNoMqt, addTab(5); "( MQT."; g_anOid; " = PUB."; g_anOid; " )"

   Print #fileNoMqt, addTab(6); "AND"
   Print #fileNoMqt, addTab(5); "("
   Dim firstCol As Boolean
   firstCol = True
   For i = 1 To tabColumns.numDescriptors
 ' ### IF IVK ###
       If ((tabColumns.descriptors(i).columnCategory And eacOid) <> eacOid) And ((tabColumns.descriptors(i).columnCategory And eacExpression) <> eacExpression) Then
 ' ### ELSE IVK ###
 '     If (.columnCategory And eacOid) <> eacOid Then
 ' ### ENDIF IVK ###
         If Not firstCol Then
           Print #fileNoMqt, addTab(7); "OR"
         End If
         If False Then
           ' todo: consider null-values here
           Print #fileNoMqt, addTab(6); "(COALESCE(MQT."; tabColumns.descriptors(i).columnName; ", PUB."; tabColumns.descriptors(i).columnName; ") IS NOT NULL AND (MQT."; tabColumns.descriptors(i).columnName; " IS NULL OR PUB."; tabColumns.descriptors(i).columnName; " IS NULL OR MQT."; tabColumns.descriptors(i).columnName; " <> PUB."; tabColumns.descriptors(i).columnName; "))"
         Else
           Print #fileNoMqt, addTab(6); "MQT."; tabColumns.descriptors(i).columnName; " <> PUB."; tabColumns.descriptors(i).columnName
         End If
         firstCol = False
       End If
   Next i
   Print #fileNoMqt, addTab(5); ")"

   Print #fileNoMqt, addTab(3); ")"
   Print #fileNoMqt, addTab(1); ";"

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNoMqt, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(1); "COMMIT;"

   genProcSectionHeader(fileNoMqt, "update private records in MQT differing in base table")

   Print #fileNoMqt, addTab(1); "UPDATE"
   Print #fileNoMqt, addTab(2); qualTabNameMqt; " MQT"
   Print #fileNoMqt, addTab(1); "SET"
   Print #fileNoMqt, addTab(2); "("

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation(transformation, 2)
   setAttributeMapping(transformation, 1, conOid, "")
   setAttributeMapping(transformation, 2, conIsLrtPrivate, "")

 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntityWithColReUse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   Else
     genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt
 ' Else
 '   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(1); "="
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "SELECT"

   transformation.attributePrefix = "PRIV."
   setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PRIV", "PRIV." & g_anInLrt)
 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt Or edomValueVirtual Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   Else
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt Or edomValueVirtual Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt
 ' Else
 '   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(3); "FROM"
   Print #fileNoMqt, addTab(4); qualTabNamePriv; " PRIV"
   Print #fileNoMqt, addTab(3); "WHERE"

   Print #fileNoMqt, addTab(4); "( MQT."; conIsLrtPrivate; " = 1 )"
   Print #fileNoMqt, addTab(5); "AND"
   Print #fileNoMqt, addTab(4); "( MQT."; g_anOid; " = PRIV."; g_anOid; " )"

   Print #fileNoMqt, addTab(2); ")"

   Print #fileNoMqt, addTab(1); "WHERE"
   Print #fileNoMqt, addTab(2); "EXISTS"
   Print #fileNoMqt, addTab(3); "("
   Print #fileNoMqt, addTab(4); "SELECT"
   Print #fileNoMqt, addTab(5); "1"
   Print #fileNoMqt, addTab(4); "FROM"
   Print #fileNoMqt, addTab(5); qualTabNamePriv; " PRIV"
   Print #fileNoMqt, addTab(4); "WHERE"

   Print #fileNoMqt, addTab(5); "( MQT."; conIsLrtPrivate; " = 1 )"
   Print #fileNoMqt, addTab(6); "AND"
   Print #fileNoMqt, addTab(5); "( MQT."; g_anOid; " = PRIV."; g_anOid; " )"

   Print #fileNoMqt, addTab(6); "AND"
   Print #fileNoMqt, addTab(5); "("
   firstCol = True
   For i = 1 To tabColumns.numDescriptors
 ' ### IF IVK ###
       If ((tabColumns.descriptors(i).columnCategory And eacOid) <> eacOid) And ((tabColumns.descriptors(i).columnCategory And eacExpression) <> eacExpression) Then
 ' ### ELSE IVK ###
 '     If (.columnCategory And eacOid) <> eacOid Then
 ' ### ENDIF IVK ###
         If Not firstCol Then
           Print #fileNoMqt, addTab(7); "OR"
         End If
         If False Then
           ' todo: consider null-values here
           Print #fileNoMqt, addTab(6); "(COALESCE(MQT."; tabColumns.descriptors(i).columnName; ", PRIV."; tabColumns.descriptors(i).columnName; ") IS NOT NULL AND (MQT."; tabColumns.descriptors(i).columnName; " IS NULL OR PRIV."; tabColumns.descriptors(i).columnName; " IS NULL OR MQT."; tabColumns.descriptors(i).columnName; " <> PRIV."; tabColumns.descriptors(i).columnName; "))"
         Else
           Print #fileNoMqt, addTab(6); "MQT."; tabColumns.descriptors(i).columnName; " <> PRIV."; tabColumns.descriptors(i).columnName
         End If
         firstCol = False
       End If
   Next i
   Print #fileNoMqt, addTab(5); ")"

   Print #fileNoMqt, addTab(3); ")"
   Print #fileNoMqt, addTab(1); ";"

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNoMqt, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(1); "COMMIT;"

   genProcSectionHeader(fileNoMqt, "determine private OIDs to INSERT")
   Print #fileNoMqt, addTab(1); "INSERT INTO"
   Print #fileNoMqt, addTab(2); pc_tempTabNamePrivOid
   Print #fileNoMqt, addTab(1); "("
   Print #fileNoMqt, addTab(2); "seqNo,"
   Print #fileNoMqt, addTab(2); "oid"
   Print #fileNoMqt, addTab(1); ")"
   Print #fileNoMqt, addTab(1); "SELECT"
   Print #fileNoMqt, addTab(2); "ROWNUMBER() OVER (ORDER BY PRIV."; g_anOid; "),"
   Print #fileNoMqt, addTab(2); "PRIV."; g_anOid
   Print #fileNoMqt, addTab(1); "FROM"
   Print #fileNoMqt, addTab(2); qualTabNamePriv; " PRIV"
   Print #fileNoMqt, addTab(1); "WHERE"
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "PRIV."; g_anOid; ","
   Print #fileNoMqt, addTab(3); gc_dbTrue
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(2); "NOT IN"
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "SELECT"
   Print #fileNoMqt, addTab(4); "MQT."; g_anOid; ","
   Print #fileNoMqt, addTab(4); "MQT."; g_anIsLrtPrivate
   Print #fileNoMqt, addTab(3); "FROM"
   Print #fileNoMqt, addTab(4); qualTabNameMqt; " MQT"
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(1); ";"

   genProcSectionHeader(fileNoMqt, "determine number of OIDs to INSERT")
   Print #fileNoMqt, addTab(1); "GET DIAGNOSTICS v_OidCount = ROW_COUNT;"

   genProcSectionHeader(fileNoMqt, "loop over 'sliding window on OIDs' to restrict to a maximum number of records processed in a single UOW")
   Print #fileNoMqt, addTab(1); "SET v_lBound = 1;"
   Print #fileNoMqt, addTab(1); "WHILE v_lBound <= v_OidCount DO"

   genProcSectionHeader(fileNoMqt, "determine upper bound of 'sliding OID-window'", 2, True)
   Print #fileNoMqt, addTab(2); "SET v_uBound = v_lBound + v_numRowsPerUow - 1;"

   genProcSectionHeader(fileNoMqt, "insert records in MQT found in 'sliding OID-window' of LRT-private table but not in MQT", 2)
   Print #fileNoMqt, addTab(2); "INSERT INTO"
   Print #fileNoMqt, addTab(3); qualTabNameMqt
   Print #fileNoMqt, addTab(2); "("

 ' ### IF IVK ###
   If forNl Then
     genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, edomListLrt Or edomMqtLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   Else
     genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt Or edomMqtLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, edomListLrt Or edomMqtLrt
 ' Else
 '   genAttrListForEntity acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt Or edomMqtLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(2); "SELECT"

   initAttributeTransformation(transformation, 2, , , , "PRIV.")
   setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PRIV", "PRIV." & g_anInLrt)
   setAttributeMapping(transformation, 1, conIsLrtPrivate, gc_dbTrue)
   setAttributeMapping(transformation, 2, conInUseBy, "(SELECT LRT.UTROWN_OID FROM " & qualTabNameLrt & " LRT WHERE PRIV." & g_anInLrt & " = LRT." & g_anOid & ")")
 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt Or edomMqtLrt Or edomValueVirtual Or edomValueVirtualNonPersisted Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   Else
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, forGen, edomListLrt Or edomMqtLrt Or edomValueVirtual Or edomValueVirtualNonPersisted Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, True, , edomListLrt Or edomMqtLrt
 ' Else
 '   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, forGen, edomListLrt Or edomMqtLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(2); "FROM"
   Print #fileNoMqt, addTab(3); qualTabNamePriv; " PRIV,"
   Print #fileNoMqt, addTab(3); pc_tempTabNamePrivOid; " W"
   Print #fileNoMqt, addTab(2); "WHERE"
   Print #fileNoMqt, addTab(3); "W."; g_anOid; " = PRIV."; g_anOid
   Print #fileNoMqt, addTab(4); "AND"
   Print #fileNoMqt, addTab(3); "W.SEQNO BETWEEN v_lBound AND v_uBound"
   Print #fileNoMqt, addTab(2); ";"

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNoMqt, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

   genProcSectionHeader(fileNoMqt, "commit UOW", 2)
   Print #fileNoMqt, addTab(2); "COMMIT;"

   genProcSectionHeader(fileNoMqt, "determine next upper bound of sliding window", 2)
   Print #fileNoMqt, addTab(2); "SET v_lBound = v_uBound + 1;"
   Print #fileNoMqt, addTab(1); "END WHILE;"
 
   genProcSectionHeader(fileNoMqt, "determine public OIDs to INSERT")
   Print #fileNoMqt, addTab(1); "INSERT INTO"
   Print #fileNoMqt, addTab(2); pc_tempTabNamePubOid
   Print #fileNoMqt, addTab(1); "("
   Print #fileNoMqt, addTab(2); "seqNo,"
   Print #fileNoMqt, addTab(2); "oid"
   Print #fileNoMqt, addTab(1); ")"
   Print #fileNoMqt, addTab(1); "SELECT"
   Print #fileNoMqt, addTab(2); "ROWNUMBER() OVER (ORDER BY PUB."; g_anOid; "),"
   Print #fileNoMqt, addTab(2); "PUB."; g_anOid
   Print #fileNoMqt, addTab(1); "FROM"
   Print #fileNoMqt, addTab(3); qualTabNamePub; " PUB"
   Print #fileNoMqt, addTab(1); "WHERE"
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "PUB."; g_anOid; ","
   Print #fileNoMqt, addTab(3); "0"
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(2); "NOT IN"
   Print #fileNoMqt, addTab(2); "("
   Print #fileNoMqt, addTab(3); "SELECT"
   Print #fileNoMqt, addTab(4); "MQT."; g_anOid; ","
   Print #fileNoMqt, addTab(4); "MQT."; g_anIsLrtPrivate
   Print #fileNoMqt, addTab(3); "FROM"
   Print #fileNoMqt, addTab(4); qualTabNameMqt; " MQT"
   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(1); ";"

   genProcSectionHeader(fileNoMqt, "determine number of OIDs to INSERT")
   Print #fileNoMqt, addTab(1); "GET DIAGNOSTICS v_OidCount = ROW_COUNT;"

   genProcSectionHeader(fileNoMqt, "loop over 'sliding window on OIDs' to restrict to a maximum number of records processed in a single UOW")
   Print #fileNoMqt, addTab(1); "SET v_lBound = 1;"
   Print #fileNoMqt, addTab(1); "WHILE v_lBound <= v_OidCount DO"

   genProcSectionHeader(fileNoMqt, "determine upper bound of 'sliding OID-window'", 2, True)
   Print #fileNoMqt, addTab(2); "SET v_uBound = v_lBound + v_numRowsPerUow - 1;"

   genProcSectionHeader(fileNoMqt, "insert records in MQT found in 'sliding OID-window' of LRT-public table but not in MQT", 2)
   Print #fileNoMqt, addTab(2); "INSERT INTO"
   Print #fileNoMqt, addTab(3); qualTabNameMqt
   Print #fileNoMqt, addTab(2); "("

 ' ### IF IVK ###
   If forNl Then
     genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt Or edomMqtLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   Else
     genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt Or edomMqtLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoMqt, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt Or edomMqtLrt
 ' Else
 '   genAttrListForEntity acmEntityIndex, acmEntityType, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt Or edomMqtLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(2); ")"
   Print #fileNoMqt, addTab(2); "SELECT"

   initAttributeTransformation(transformation, 2, , , , "PUB.")
   setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PUB")
   setAttributeMapping(transformation, 1, conIsLrtPrivate, gc_dbFalse)
   setAttributeMapping(transformation, 2, conInUseBy, "(SELECT LRT.UTROWN_OID FROM " & qualTabNameLrt & " LRT WHERE PUB." & g_anInLrt & " = LRT." & g_anOid & ")")
 ' ### IF IVK ###
   If forNl Then
     genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt Or edomMqtLrt Or edomValueVirtual Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   Else
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt Or edomMqtLrt Or edomValueVirtual Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone))
   End If
 ' ### ELSE IVK ###
 ' If forNl Then
 '   genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt Or edomMqtLrt
 ' Else
 '   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoMqt, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt Or edomMqtLrt
 ' End If
 ' ### ENDIF IVK ###

   Print #fileNoMqt, addTab(2); "FROM"
   Print #fileNoMqt, addTab(3); qualTabNamePub; " PUB,"
   Print #fileNoMqt, addTab(3); pc_tempTabNamePubOid; " W"
   Print #fileNoMqt, addTab(2); "WHERE"
   Print #fileNoMqt, addTab(3); "W."; g_anOid; " = PUB."; g_anOid
   Print #fileNoMqt, addTab(4); "AND"
   Print #fileNoMqt, addTab(3); "W.SEQNO BETWEEN v_lBound AND v_uBound"
   Print #fileNoMqt, addTab(2); ";"

   Print #fileNoMqt,
   Print #fileNoMqt, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNoMqt, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

   genProcSectionHeader(fileNoMqt, "commit UOW", 2)
   Print #fileNoMqt, addTab(2); "COMMIT;"

   genProcSectionHeader(fileNoMqt, "determine next upper bound of sliding window", 2)
   Print #fileNoMqt, addTab(2); "SET v_lBound = v_uBound + 1;"
   Print #fileNoMqt, addTab(1); "END WHILE;"

   genSpLogProcExit(fileNoMqt, qualProcNameMqtSync, ddlType, , "rowCount_out")

   Print #fileNoMqt, addTab(0); "END"
   Print #fileNoMqt, gc_sqlCmdDelim
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 
