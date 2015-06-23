 Attribute VB_Name = "M05_Main"
 Option Explicit
 
 Private Sub copyMigFilesInDir( _
   ByRef sourceDirPath As String, _
   ByRef targetDirPath As String, _
   Optional ByRef skipFileSuffix1 As String = "", _
   Optional ByRef skipFileSuffix2 As String = "", _
   Optional alsoForFwkTest As Boolean = True _
 )
   If Not alsoForFwkTest And generateFwkTest Then
     Exit Sub
   End If

   Dim match As String

   On Error Resume Next
   Err.Number = 0
   match = dir(sourceDirPath & "\*", vbNormal)
   If Err.Number <> 0 And Err.Number <> 52 Then
     GoTo ErrorExit
   End If
   If match = "" Then
     Exit Sub
   End If

   On Error GoTo ErrorExit
   assertDir(targetDirPath & "\X")

   Do While match <> ""
     If ((skipFileSuffix1 = "") Or ((skipFileSuffix1 <> "") And (UCase(Right(match, Len(skipFileSuffix1))) <> UCase(skipFileSuffix1)))) And _
        ((skipFileSuffix2 = "") Or ((skipFileSuffix2 <> "") And (UCase(Right(match, Len(skipFileSuffix2))) <> UCase(skipFileSuffix2)))) Then
       FileCopy sourceDirPath & "\" & match, targetDirPath & "\" & match
     End If
     match = dir ' next entry.
   Loop
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub copyMigFiles()
   If (workSheetSuffix = "") Or (Not generatePdm) Then
     Exit Sub
   End If

   Dim srcDir As String, srcRootDir As String
   Dim dstDir As String, dstRootDir As String

   srcRootDir = g_targetDir & "\PDM" & IIf(g_genLrtSupport, "-LRT", "")
   dstRootDir = g_targetDir & "\" & workSheetSuffix & "\PDM" & IIf(g_genLrtSupport, "-LRT", "")

   copyMigFilesInDir(srcRootDir & "\Deploy", dstRootDir & "\Deploy", ".dml", IIf(supportSpLogging And targetPlatform = "AIX" And spLogMode = esplFile, "", "SPLOGGER.DDL"), True)
   copyMigFilesInDir(srcRootDir & "\Deploy\jar", dstRootDir & "\Deploy\jar", , , True)

   copyMigFilesInDir(srcRootDir & "\Deploy\obj\AIX", dstRootDir & "\Deploy\obj\AIX", IIf(supportSpLogging And targetPlatform = "AIX" And spLogMode = esplFile, "", "splogger"), , True)
   copyMigFilesInDir(srcRootDir & "\Deploy\obj\Windows", dstRootDir & "\Deploy\obj\Windows", IIf(supportSpLogging And targetPlatform <> "AIX" And spLogMode = esplFile, "", "splogger"), , True)

   Dim thisOrgIndex As Integer
   For thisOrgIndex = 1 To g_orgs.numDescriptors
       If Not g_orgs.descriptors(thisOrgIndex).isTemplate Then

         Dim j As Integer
         For j = 1 To 2
           Dim infix As String
           infix = IIf(j = 1, "", "\drop")
           srcDir = srcRootDir & "\" & gc_dirPrefixOrg & genOrgId(thisOrgIndex, edtPdm) & "-" & g_orgs.descriptors(thisOrgIndex).name & "\Migration" & infix
           dstDir = dstRootDir & "\" & gc_dirPrefixOrg & genOrgId(thisOrgIndex, edtPdm) & "-" & g_orgs.descriptors(thisOrgIndex).name & "\Migration" & infix
           copyMigFilesInDir(srcDir, dstDir, , , False)
         Next j
       End If
   Next thisOrgIndex
 End Sub
 
 
 Private Sub copyVdokfFiles()
   If (workSheetSuffix = "") Or (Not generatePdm) Or generateFwkTest Then
     Exit Sub
   End If

   Dim srcDir As String, srcRootDir As String
   Dim dstDir As String, dstRootDir As String
   Dim thisOrgId As Integer
   Dim thisPoolId As Integer
   Dim orgName As String
   Dim poolName As String

   srcRootDir = g_targetDir & "\PDM" & IIf(g_genLrtSupport, "-LRT", "")
   dstRootDir = g_targetDir & "\" & workSheetSuffix & "\PDM" & IIf(g_genLrtSupport, "-LRT", "")

   Dim thisOrgIndex As Integer
   For thisOrgIndex = 1 To g_orgs.numDescriptors
       If Not g_orgs.descriptors(thisOrgIndex).isTemplate Then
         thisOrgId = g_orgs.descriptors(thisOrgIndex).id
         orgName = Replace(getOrgNameById(thisOrgId), " ", "_", , , vbTextCompare)

         Dim thisPoolIndex As Integer
         For thisPoolIndex = 1 To g_pools.numDescriptors
           thisPoolId = g_pools.descriptors(thisPoolIndex).id

           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
             poolName = Replace(getDataPoolNameByIndex(thisPoolIndex), " ", "_", , , vbTextCompare)

             Dim k As Integer
             For k = 1 To 2
               Dim infix As String
               infix = IIf(k = 1, "", "\drop")
               srcDir = srcRootDir & "\" & gc_dirPrefixOrg & genOrgId(thisOrgIndex, edtPdm) & "-" & orgName & "\DPool-" & genPoolId(thisPoolIndex, edtPdm) & "-" & poolName & "\VDOKF" & infix
               dstDir = dstRootDir & "\" & gc_dirPrefixOrg & genOrgId(thisOrgIndex, edtPdm) & "-" & orgName & "\DPool-" & genPoolId(thisPoolIndex, edtPdm) & "-" & poolName & "\VDOKF" & infix
               copyMigFilesInDir(srcDir, dstDir, , , False)
             Next k
           End If
         Next thisPoolIndex
       End If
   Next thisOrgIndex
 End Sub
 
 
 Private Sub copyExplainFiles()
   If (workSheetSuffix = "") Or (Not generatePdm) Or generateFwkTest Then
     Exit Sub
   End If

   Dim srcDir As String, srcRootDir As String
   Dim dstDir As String, dstRootDir As String

   srcRootDir = g_targetDir & "\PDM" & IIf(g_genLrtSupport, "-LRT", "")
   dstRootDir = g_targetDir & "\" & workSheetSuffix & "\PDM" & IIf(g_genLrtSupport, "-LRT", "")

   Dim k As Integer
   For k = 1 To 2
     Dim infix As String
     infix = IIf(k = 1, "", "\drop")
     srcDir = srcRootDir & "\Explain" & infix
     dstDir = dstRootDir & "\Explain" & infix
     copyMigFilesInDir(srcDir, dstDir, , , False)
   Next k
 End Sub
 
 
 Private Sub loadSheets()
   getOrgs()
   getDataPools()
   getBufferPools()
   getKwMaps()
   getErrs()
   getDbCfgParams()
   getTabCfgParams()
   getDbCfgProfiles()
   getContainers()
   getTableSpaces()
   getAttributes()
   getAttributesNl()
   getEnums()
   getEnumsNl()
 ' ### IF IVK ###
   getTypes()
 ' ### ENDIF IVK ###
   getDomains()
   getClasses()
   getClassesNl()
   getRelationships()
   getRelationshipsNl()
   getIndexes()
   getIndexAttrs()
   getIndexExcp()
   getSections()
   getPrivileges()
   getSnapshotTypes()
   getSnapshotCols()
   getSnapshotFilter()
   getCleanJobs()
 ' ### IF IVK ###
   getDComps()
 ' ### ENDIF IVK ###

   initGlobals()

   evalObjects()
 End Sub
 
 
 Sub dropSheet( _
   sheetName As String _
 )
   Dim sheet As Worksheet
   On Error Resume Next

   sheet = ActiveSheet
   On Error GoTo ErrorExit
   ActiveWorkbook.Sheets(sheetName).Select
   ActiveWindow.SelectedSheets.Delete
   sheet.Select
 
 ErrorExit:
 End Sub
 
 
 Sub doRunTest()
 Attribute doRunTest.VB_ProcData.VB_Invoke_Func = "r\n14"
   doRun(ecfgTest)
 End Sub
 
 
 Sub doRunProductiveEw()
 Attribute doRunProductiveEw.VB_ProcData.VB_Invoke_Func = "p\n14"
   doRun(ecfgProductionEw)
 End Sub
 
 
 Sub doRunForDelivery()
 Attribute doRunForDelivery.VB_ProcData.VB_Invoke_Func = "d\n14"
   doRun(ecfgDelivery)
 End Sub
 
 
 Sub doRun( _
   Optional cfgMode As ConfigMode = ecfgTest _
 )
   If irregularSetting(cfgMode) Then
     If MsgBox("Generator Config includes irregular setting. Do you want to continue?", vbYesNo Or vbCritical) <> vbYes Then
       Exit Sub
     End If
   End If

   killFile(genLogFileName())
   setLogLevesl (Not CInt(0))

   closeAllDdlFiles()
   closeAllCsvFiles()

   readConfig(cfgMode)

   Dim tsBegin As Date
   tsBegin = Now

   logMsg("Start DDL Generator", ellInfo)

   setLogLevesl()

   resetOid()
   loadSheets()
 
 'genPackages
 'Exit Sub
   Dim loopStart As Integer, loopStop As Integer
   loopStart = IIf(generateNonLrt, 1, 2)
   loopStop = IIf(generateLrt, 2, 1)

   Dim i As Integer
   For i = loopStart To loopStop Step 1
     g_genLrtSupport = (i = 2)
     setEnv (g_genLrtSupport)

 ' ### IF IVK ###
     dropClassesHibernateSupport(edtLdm)
 ' ### ENDIF IVK ###
     dropClassIdList()
 ' ### IF IVK ###
     dropClassesXmlExport()
 ' ### ENDIF IVK ###
     dropSheet(g_sheetNameDdlSummary)

     dropDdl()
     dropCsv()
     dropCsvInventoryLists()
   Next i

   initGLdmFks()
   cleanupPools()

 ' ### IF IVK ###
   If genSupportForHibernate Then
     For i = loopStart To loopStop Step 1
       g_genLrtSupport = (i = 2)
       setEnv (g_genLrtSupport)
       genClassesHibernateSupport(edtLdm)
     Next i
   End If

 ' ### ENDIF IVK ###
   If generateEntityIdList Then
     genClassIdList()
     genRelIdList()
   End If

   For i = loopStart To loopStop Step 1
     g_genLrtSupport = (i = 2)
     setEnv (g_genLrtSupport)
 
 ' ### IF IVK ###
     copyMigFiles()
     copyVdokfFiles()
     copyExplainFiles()
 
 '    profLogOpen
 
 ' ### ENDIF IVK ###

     If generateLdm Then
       verifyWorksheet (g_sheetNameDdlSummary & "-tmp")
       If setSheetName(ActiveWorkbook, ActiveSheet, g_sheetNameDdlSummary, False) = vbNo Then
         Exit Sub
       End If
     End If
 
     Dim ddlType As DdlTypeId
     For ddlType = IIf(generatePdm, edtPdm, edtLdm) To IIf(generateLdm, edtLdm, edtPdm)
       initGlobalsByDdl(ddlType)

       genAcmMetaCsv(ddlType)
       genLdmMetaCsv(ddlType)
       genPdmMetaCsv(ddlType)
       genPrivilegesCsv(ddlType)
       genCleanJobsCsv(ddlType)
       genDbCfgProfileCsv(ddlType)
       genTabCfgCsv(ddlType)
       genSnapshotTypesCsv(ddlType)
       genSnapshotColsCsv(ddlType)
       genSnapshotFilterCsv(ddlType)

 ' ### IF IVK ###
       genDCompCsv(ddlType)
 ' ### ENDIF IVK ###

       genDbAdminDdl(ddlType)
 ' ### IF IVK ###
       genDbAdminPartitioningDdl(ddlType)
 ' ### ENDIF IVK ###
       genDbSnapshotDdl(ddlType)
       genDbIndexMetricsDdl(ddlType)
       genSpLogWrapperDdl(ddlType)
       genDbMetaDdl(ddlType)
       genDbUtilitiesDdl(ddlType)
 ' ### IF IVK ###
       genTraceDdl(ddlType)
       genDataCheckUtilitiesDdl(ddlType)
       genClassesHibernateSupport(ddlType)
 ' ### ENDIF IVK ###
       genLrtSupportDdl(ddlType)
       genLrtMqtSupportDdl(ddlType)
 ' ### IF IVK ###
       genVirtAttrSupportDdl(ddlType)
       genGroupIdSupportDdl(ddlType)
       genSetProdSupportDdl(ddlType)
       genArchiveSupportDdl(ddlType)
       genPsCopySupportDdl(ddlType)
       genDataFixSupportDdl(ddlType)
       genAcmMetaSupportDdl(ddlType)
       genPsCreateSupportDdl(ddlType)
       genPsDeleteSupportDdl(ddlType)
       genFactoryTakeOverDdl(ddlType)
       genCodesWithoutDepDdl(ddlType)
       genTechDataSupDdl(ddlType)
       genRssSupDdl(ddlType)
       genFwkTestDdl(ddlType)
 ' ### ENDIF IVK ###

       genOrgsDdl(ddlType)
       genDataPoolsDdl(ddlType)
       genBufferPoolsDdl(ddlType)
       genTableSpacesDdl(ddlType)

 ' ### IF IVK ###
       genDCompSupportDdl(ddlType)
 ' ### ENDIF IVK ###

       genEnumsDdl(ddlType)
       genClassesDdl(ddlType)
       genRelationshipsDdl(ddlType)

       genDbDeployPostprocess(ddlType)

       closeAllDdlFiles(, , , , , ddlType)
       closeAllCsvFiles(ddlType)
     Next ddlType

     genLdmFksCsvs()
     genCsvInventoryLists()

     dropDdl(True)

     resetDdl()

     initGLdmFks()
   Next i

   If generateDeployPackage Then
     genPackages()
   ElseIf generateUpdatePackage Then
     genPackages()
   End If


   If exportVBCode Then
     exportCode
   End If

   If exportXlsSheets Then
     exportSheets()
   End If

   reset()

   Dim tsEnd As Date
   tsEnd = Now

   Dim runTimeSec As Long
   runTimeSec = DateDiff("s", tsBegin, tsEnd)
   setLogLevesl (Not CInt(0))
   logMsg("End DDL Generator (" & CLng(runTimeSec / 60) & ":" & Right("0" & (runTimeSec Mod 60), 2) & ")", ellInfo)
 ' ### IF IVK ###
   profLogClose()
 ' ### ENDIF IVK ###
 End Sub
 
 
 Sub doPack( _
   Optional cfgMode As ConfigMode = ecfgTest _
 )
   If irregularSetting(cfgMode) Then
     If MsgBox("Generator Config includes irregular setting. Do you want to continue?", vbYesNo Or vbCritical) <> vbYes Then
       Exit Sub
     End If
   End If

   readConfig(cfgMode)
 
   resetOid()
   loadSheets()
 
   If generateDeployPackage Then
     genPackages()
   ElseIf generateUpdatePackage Then
     genPackages()
   End If

   reset()
 End Sub
 
 
 Sub doPackTest()
 Attribute doPackTest.VB_ProcData.VB_Invoke_Func = "R\n14"
   doPack(ecfgTest)
 End Sub


 Sub doPackProductive()
 Attribute doPackProductive.VB_ProcData.VB_Invoke_Func = "P\n14"
   doPack(ecfgProductionEw)
 End Sub
 
 Private Sub reset()
   resetDataPools()
   resetOrgs()
   resetBufferPools()
   resetContainers()
   resetKwMaps()
   resetErrs()
   resetDbCfgParams()
   resetTabCfgParams()
   resetDbCfgProfiles()
   resetTableSpaces()
   resetSections()
   resetDomains()
   resetAttributes()
   resetAttributesNl()
   resetClasses()
   resetClassesNl()
   resetIndexes()
   resetIndexAttrs()
   resetDomains()
   resetEnums()
   resetEnumsNl()
 ' ### IF IVK ###
   resetTypes()
 ' ### ENDIF IVK ###
   resetRelationships()
   resetRelationshipsNl()
   resetPrivileges()
   resetCleanJobs()
   resetSnapshotTypes()
   resetSnapshotCols()
   resetSnapshotFilter()
 ' ### IF IVK ###
   resetDComps()
 ' ### ENDIF IVK ###
 End Sub
