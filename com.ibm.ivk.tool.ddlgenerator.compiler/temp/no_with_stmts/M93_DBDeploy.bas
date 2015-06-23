 Attribute VB_Name = "M93_DBDeploy"
 Option Explicit
 
 Private Const processingStepDeploy = 4
 Private Const maxSubDirs = 30
 
 
 Sub genDbDeployPostprocess( _
   ddlType As DdlTypeId _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   Dim fileNo As Integer
   fileNo = openDmlFile(g_targetDir, g_sectionIndexDbMeta, processingStepDeploy, edtPdm, , , "Deploy", phaseAliases)

   On Error GoTo ErrorExit

   genDbDeployPostprocessMeta fileNo
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genDbDeployPostprocessMeta( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   Dim qualViewName As String

     qualViewName = genQualViewName(g_classes.descriptors(g_classIndexLdmTable).sectionIndex, vnLdmTabDepOrder, vnsLdmTabDepOrder, ddlType)

   printSectionHeader "order LDM-tables according to their involvement in foreign key chains", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "UPDATE"
   Print #fileNo, addTab(1); g_qualTabNameLdmTable; " T"
   Print #fileNo, addTab(0); "SET"
   Print #fileNo, addTab(1); g_anLdmFkSequenceNo; " ="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "seqNo"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualViewName; " V"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "T."; g_anLdmTableName; " = V.SrcTable"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "T."; g_anLdmSchemaName; " = V.SrcSchema"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, gc_sqlCmdDelim

   Dim qualProcName As String

   qualProcName = genQualProcName(g_sectionIndexDbAdmin, spnCreateLrtAliases, ddlType)

   printSectionHeader "create Aliases for 'private-only' and 'public-only' tables", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CALL "; qualProcName; "(2, NULL, NULL, ?, ?)"
   Print #fileNo, gc_sqlCmdDelim

   qualProcName = genQualProcName(g_sectionIndexDbMonitor, spnGenViewSnapshot, ddlType)

   printSectionHeader "create snapshot-views", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CALL "; qualProcName; "(0)"
   Print #fileNo, gc_sqlCmdDelim

   If setDefaultCfgDuringDeployment Then
     qualProcName = genQualProcName(g_sectionIndexDbAdmin, spnSetCfg, ddlType)
 
     printSectionHeader "apply default DB configuration", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CALL "; qualProcName; "(1, ?, ?)"
     Print #fileNo, gc_sqlCmdDelim
   End If

   If Not generateFwkTest Then
     qualProcName = genQualProcName(g_sectionIndexDbAdmin, spnSetTableCfg, ddlType)

     printSectionHeader "configure table parameters", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CALL "; qualProcName; "(2, NULL, NULL, ?)"
     Print #fileNo, gc_sqlCmdDelim
 ' ### IF IVK ###

     qualProcName = genQualProcName(g_sectionIndexPaiLog, spnRssGetStatus, ddlType)

     printSectionHeader "initialize RSS-Status-Tables", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CALL "; qualProcName; "('DEPLOY', ?)"
     Print #fileNo, gc_sqlCmdDelim
 ' ### ENDIF IVK ###
   End If
 ' ### IF IVK ###

   printSectionHeader "update deployment history", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "INSERT INTO"
   Print #fileNo, addTab(1); g_qualTabNameApplVersion
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "VERSION,"
   Print #fileNo, addTab(1); "DEPLOYDATE,"
   Print #fileNo, addTab(1); "DESCRIPTION"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "VALUES"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "'"; versionString; "',"
   Print #fileNo, addTab(1); "CURRENT DATE,"
   Print #fileNo, addTab(1); "'DDL-Deployment'"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, gc_sqlCmdDelim
 ' ### ENDIF IVK ###
 End Sub
 
 
 Sub genCsvInventoryList( _
   ByRef dirPath As String, _
   Optional attributes As Integer = vbNormal _
 )
   Dim match As String
   Dim matchElems() As String
   Dim schemaName As String
   Dim tabName As String
   Dim fileNameList As String
   Dim fileNo As Integer

   fileNo = -1
   fileNameList = dirPath & "\db2csv.lst"

   match = dir(dirPath & "\*CSV", attributes)
   If match = "" Then
     Exit Sub
   End If

   On Error GoTo ErrorExit
   fileNo = FreeFile()
   Open fileNameList For Output As #fileNo

   Do While match <> ""
     matchElems = split(match, ".", 3)
     tabName = matchElems(1)
     matchElems = split(matchElems(0), "-", 4)
     schemaName = matchElems(2)

     If schemaName <> "" And tabName <> "" Then
       Print #fileNo, "!"""; schemaName; """."""; tabName; """!"; match; "!"
     End If

     match = dir ' next entry.
   Loop
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genCsvInventoryListsRecursive( _
   ByRef dirPath As String, _
   Optional attributes As Integer = vbNormal _
 )
   Dim match As String
   Dim matchElems() As String
   Dim subDirs(1 To maxSubDirs) As String
   Dim numSubDirs As Integer
   numSubDirs = 0

   match = dir(dirPath & "\*", vbDirectory)
   Do While match <> ""
     If numSubDirs < maxSubDirs And match <> "." And match <> ".." Then
       If (GetAttr(dirPath & "\" & match) And vbDirectory) = vbDirectory Then
         numSubDirs = numSubDirs + 1
         subDirs(numSubDirs) = match
       End If
     End If
     match = dir ' next entry.
   Loop
 
   Dim i As Integer
   For i = 1 To numSubDirs
     genCsvInventoryList dirPath & "\" & subDirs(i), attributes
     genCsvInventoryListsRecursive dirPath & "\" & subDirs(i), attributes
   Next i
 End Sub
 
 
 Sub genCsvInventoryLists()
   If generateLdm Then
     genCsvInventoryListsRecursive g_targetDir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & "\LDM" & IIf(g_genLrtSupport, "-LRT", "")
   End If

   If generatePdm Then
     genCsvInventoryListsRecursive g_targetDir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & "\PDM" & IIf(g_genLrtSupport, "-LRT", "")
   End If
 End Sub
 
 
 Sub dropCsvInventoryList( _
   ByRef dirPath As String _
 )
   Dim match As String

   match = dir(dirPath & "\db2csv.lst", vbNormal)
   If match <> "" Then
     killFile dirPath & "\" & match
   End If

 End Sub
 
 
 Sub dropCsvInventoryListsRecursive( _
   ByRef dirPath As String, _
   Optional attributes As Integer = vbNormal _
 )
   Dim match As String
   Dim matchElems() As String
   Dim subDirs(1 To maxSubDirs) As String
   Dim numSubDirs As Integer
   numSubDirs = 0

   match = dir(dirPath & "\*", vbDirectory)
   Do While match <> ""
     If numSubDirs < maxSubDirs And match <> "." And match <> ".." Then
       If (GetAttr(dirPath & "\" & match) And vbDirectory) = vbDirectory Then
         numSubDirs = numSubDirs + 1
         subDirs(numSubDirs) = match
       End If
     End If
     match = dir ' next entry.
   Loop
 
   Dim i As Integer
   For i = 1 To numSubDirs
     dropCsvInventoryList dirPath & "\" & subDirs(i)
     dropCsvInventoryListsRecursive dirPath & "\" & subDirs(i)
   Next i
 End Sub
 
 Sub dropCsvInventoryLists()
   If generateLdm Then
     dropCsvInventoryListsRecursive g_targetDir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & "\LDM" & IIf(g_genLrtSupport, "-LRT", "")
   End If

   If generatePdm Then
     dropCsvInventoryListsRecursive g_targetDir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & "\PDM" & IIf(g_genLrtSupport, "-LRT", "")
   End If
 End Sub
 
