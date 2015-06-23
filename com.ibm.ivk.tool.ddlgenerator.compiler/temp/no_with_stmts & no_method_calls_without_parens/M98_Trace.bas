 Attribute VB_Name = "M98_Trace"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const processingStepTrace = 4
 
 Private Type tempTabMapping
   classIndex As Integer
   qualTabName As String
   tempTabName As String
   idAttrName As String
 End Type
 
 
 Private Sub setTabMapping( _
   ByRef mapping As tempTabMapping, _
   ByVal classIndex As Integer, _
   ByRef tempTabName As String, _
   ddlType As DdlTypeId, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer _
 )
     mapping.classIndex = classIndex
       If g_classes.descriptors(classIndex).attrRefs.numDescriptors > 0 Then
         ' implicit assumption: ID-column is the first column in table
         mapping.idAttrName = g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(1).refIndex).attributeName
       Else
         mapping.idAttrName = ""
       End If
     mapping.qualTabName = genQualTabNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex)
     mapping.tempTabName = tempTabName
 End Sub
 
 
 Sub genTraceDdl( _
   ddlType As DdlTypeId _
 )
   If ddlType = edtPdm Then
     Dim thisOrgIndex As Integer
     Dim thisPoolIndex As Integer

     For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
       For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
             genTraceDdlByPool(edtPdm, thisOrgIndex, thisPoolIndex)
           End If
       Next thisPoolIndex
     Next thisOrgIndex
   End If
 End Sub
 
 
 Sub genTraceDdlByPool( _
   ddlType As DdlTypeId, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   Dim thisOrgId As Integer
   Dim thisPoolId As Integer
   If thisOrgIndex > 0 Then thisOrgId = g_orgs.descriptors(thisOrgIndex).id Else thisOrgId = -1
   If thisPoolIndex > 0 Then thisPoolId = g_pools.descriptors(thisPoolIndex).id Else thisPoolId = -1

   On Error GoTo ErrorExit

   Dim nothingToDo As Boolean
   nothingToDo = True
   Const numClasses = 5
   Dim tabMapping(1 To numClasses) As tempTabMapping
   setTabMapping(tabMapping(1), g_classIndexFtoChangelogSummary, gc_tempTabNameChangeLogSummary, ddlType, thisOrgIndex, thisPoolIndex)
   setTabMapping(tabMapping(2), g_classIndexFtoOrgChangelogSummary, gc_tempTabNameChangeLogOrgSummary, ddlType, thisOrgIndex, thisPoolIndex)
   setTabMapping(tabMapping(3), g_classIndexFtoOrgImplicitChangesSummary, gc_tempTabNameChangeLogImplicitChanges, ddlType, thisOrgIndex, thisPoolIndex)
   setTabMapping(tabMapping(4), g_classIndexSpAffectedEntity, gc_tempTabNameSpAffectedEntities, ddlType, thisOrgIndex, thisPoolIndex)
   setTabMapping(tabMapping(5), g_classIndexSpFilteredEntity, gc_tempTabNameSpFilteredEntities, ddlType, thisOrgIndex, thisPoolIndex)

   Dim i As Integer
   For i = 1 To numClasses
       If g_classes.descriptors(tabMapping(i).classIndex).specificToOrgId <= 0 Or g_classes.descriptors(tabMapping(i).classIndex).specificToOrgId = thisOrgId Then
         If g_classes.descriptors(tabMapping(i).classIndex).specificToPool < 0 Or g_classes.descriptors(tabMapping(i).classIndex).specificToPool = thisPoolId Then
           nothingToDo = False
         End If
       End If
   Next i

   If nothingToDo Then
     Exit Sub
   End If

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexTrace, processingStepTrace, ddlType, thisOrgIndex, thisPoolIndex, , phaseDbSupport)

   ' ####################################################################################################################
   ' #    SP for Persisting Trace Tables
   ' ####################################################################################################################

   Dim qualProcNameTracePersist As String
   qualProcNameTracePersist = genQualProcName(g_sectionIndexTrace, spnTracePersist, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("SP for Persisting Trace Tables", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameTracePersist
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "OUT", "traceId_out", "BIGINT", True, "ID used to identify persisted records related to this procedure call")
   genProcParm(fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of non-empty temporary tables persisted")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows persisted")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "alreadyExist", "42710")

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_traceId", "BIGINT", "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist BEGIN END;"

   genProcSectionHeader(fileNo, "declare temporary temporary tables")
   For i = 1 To numClasses
       If i > 1 Then
         Print #fileNo,
       End If

       Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
       Print #fileNo, addTab(2); tabMapping(i).tempTabName
       Print #fileNo, addTab(1); "LIKE"
       Print #fileNo, addTab(2); tabMapping(i).qualTabName

       genDdlForTempTableDeclTrailer(fileNo, 1, False)
   Next i

   genSpLogProcEnter(fileNo, qualProcNameTracePersist, ddlType, , "tabCount_out", "rowCount_out")

   genProcSectionHeader(fileNo, "initialize output variables")
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
   Print #fileNo, addTab(1); "SET traceId_out = NULL;"

   genProcSectionHeader(fileNo, "determine trace ID")
   Print #fileNo, addTab(1); "SET v_traceId = NEXTVAL FOR "; genQualOidSeqNameForOrg(thisOrgIndex, ddlType); ";"
 
   genProcSectionHeader(fileNo, "persist records in temporary tables")

   Dim transformation As AttributeListTransformation
   For i = 1 To numClasses
       If i > 1 Then
         Print #fileNo,
       End If
       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); tabMapping(i).qualTabName
       Print #fileNo, addTab(1); "("
 
       genAttrListForEntity(tabMapping(i).classIndex, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , edomListNonLrt)

       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "SELECT"

       initAttributeTransformation(transformation, 1)
       setAttributeMapping(transformation, 1, tabMapping(i).idAttrName, "v_traceId")

       genTransformedAttrListForEntity(tabMapping(i).classIndex, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt)

       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); tabMapping(i).tempTabName
       Print #fileNo, addTab(1); ";"

       genProcSectionHeader(fileNo, "count the number of affected rows and tables", 1, True)
       Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
       Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
       Print #fileNo, addTab(1); "IF rowCount_out > 0 THEN"
       Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
       Print #fileNo, addTab(1); "END IF;"
 
   Next i

   genProcSectionHeader(fileNo, "set output trace ID")
   Print #fileNo, addTab(1); "SET traceId_out = v_traceId;"

   genSpLogProcExit(fileNo, qualProcNameTracePersist, ddlType, , "tabCount_out", "rowCount_out")
 
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
 ' ### ENDIF IVK ###
 
