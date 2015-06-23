 Attribute VB_Name = "M73_TableSpace"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colTableSpaceName = 2
 Private Const colShortName = colTableSpaceName + 1
 Private Const colIsCommonToOrgs = colShortName + 1
 Private Const colSpecificToOrg = colIsCommonToOrgs + 1
 Private Const colIsCommonToPools = colSpecificToOrg + 1
 Private Const colSpecificToPool = colIsCommonToPools + 1
 Private Const colIsPdmSpecific = colSpecificToPool + 1
 Private Const colIsMonitor = colIsPdmSpecific + 1
 Private Const colType = colIsMonitor + 1
 Private Const colManagedBy = colType + 1
 Private Const colPageSize = colManagedBy + 1
 Private Const colAutoResize = colPageSize + 1
 Private Const colIncreasePercent = colAutoResize + 1
 Private Const colIncreaseAbsolute = colIncreasePercent + 1
 Private Const colMaxSize = colIncreaseAbsolute + 1
 Private Const colExtentSize = colMaxSize + 1
 Private Const colPrefetchSize = colExtentSize + 1
 Private Const colBufferPool = colPrefetchSize + 1
 Private Const colOverhead = colBufferPool + 1
 Private Const colTransferRate = colOverhead + 1
 Private Const colUseFileSystemCaching = colTransferRate + 1
 Private Const colSupportDroppedTableReovery = colUseFileSystemCaching + 1
 
 Private Const processingStep = 2
 
 Private Const firstRow = 3
 
 Private Const sheetName = "TS"
 
 Global g_tableSpaces As TableSpaceDescriptors
 
 Private Sub readSheet()
   initTableSpaceDescriptors g_tableSpaces

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colTableSpaceName) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).tableSpaceName = Trim(thisSheet.Cells(thisRow, colTableSpaceName))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).shortName = Trim(thisSheet.Cells(thisRow, colShortName))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).isCommonToOrgs = getBoolean(thisSheet.Cells(thisRow, colIsCommonToOrgs))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).specificToOrgId = getInteger(thisSheet.Cells(thisRow, colSpecificToOrg))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).isCommonToPools = g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).isCommonToOrgs Or getBoolean(thisSheet.Cells(thisRow, colIsCommonToPools))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).specificToPool = getInteger(thisSheet.Cells(thisRow, colSpecificToPool))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).isPdmSpecific = getBoolean(thisSheet.Cells(thisRow, colIsPdmSpecific))
        g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).isMonitor = getBoolean(thisSheet.Cells(thisRow, colIsMonitor))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).type = thisSheet.Cells(thisRow, colType)
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).category = getTabSpaceCategory(thisSheet.Cells(thisRow, colManagedBy))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).pageSize = Trim(thisSheet.Cells(thisRow, colPageSize))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).autoResize = getBoolean(thisSheet.Cells(thisRow, colAutoResize))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).increasePercent = getInteger(thisSheet.Cells(thisRow, colIncreasePercent))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).increaseAbsolute = Trim(thisSheet.Cells(thisRow, colIncreaseAbsolute))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).maxSize = Trim(thisSheet.Cells(thisRow, colMaxSize))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).extentSize = Trim(thisSheet.Cells(thisRow, colExtentSize))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).prefetchSize = Trim(thisSheet.Cells(thisRow, colPrefetchSize))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).bufferPoolName = Trim(thisSheet.Cells(thisRow, colBufferPool))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).overhead = Trim(thisSheet.Cells(thisRow, colOverhead))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).transferrate = Trim(thisSheet.Cells(thisRow, colTransferRate))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).useFileSystemCaching = getBoolean(thisSheet.Cells(thisRow, colUseFileSystemCaching))
       g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces)).supportDroppedTableRecovery = getBoolean(thisSheet.Cells(thisRow, colSupportDroppedTableReovery))

 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getTableSpaces()
   If (g_tableSpaces.numDescriptors = 0) Then
     readSheet
   End If
 End Sub
 
 
 Sub resetTableSpaces()
   g_tableSpaces.numDescriptors = 0
 End Sub
 
 
 Function getTableSpaceIndexByName( _
   tableSpaceName As String _
 ) As Integer
   Dim i As Integer
 
   getTableSpaceIndexByName = -1
   getTableSpaces
 
   For i = 1 To g_tableSpaces.numDescriptors Step 1
     If UCase(g_tableSpaces.descriptors(i).tableSpaceName) = UCase(tableSpaceName) Then
       getTableSpaceIndexByName = i
       Exit Function
     End If
   Next i
 End Function
 
 Function getTableSpaceDdlBaseFileName( _
     ddlType As DdlTypeId _
 ) As String
   getTableSpaceDdlBaseFileName = baseName(genDdlFileName(g_targetDir, g_sectionIndexDb, processingStep, ddlType))
 End Function
 
 
 Private Sub genTableSpaceDdl( _
   ByRef tablespace As TableSpaceDescriptor, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 )
   If ddlType <> edtPdm And tablespace.isPdmSpecific Then
     Exit Sub
   End If

   Dim fileNo As Integer
   Dim thisOrgDescriptorStr As String

   thisOrgDescriptorStr = genOrgId(thisOrgIndex, ddlType)

     fileNo = openDdlFile(g_targetDir, g_sectionIndexDb, processingStep, ddlType, thisOrgIndex, thisPoolIndex)

     On Error GoTo ErrorExit

     printChapterHeader "TableSpace """ & tablespace.tableSpaceName & """", fileNo
     Print #fileNo, addTab(0); "CREATE "; IIf(tablespace.type <> "", UCase(tablespace.type) & " ", ""); "TABLESPACE "
     Print #fileNo, addTab(1); UCase(tablespace.tableSpaceName)
     Print #fileNo, addTab(1); paddRight("PAGESIZE "); IIf(tablespace.pageSize <> "", tablespace.pageSize, "4096")

     Print #fileNo, addTab(1); paddRight("MANAGED BY "); IIf(tablespace.category = tscDms, "DATABASE", "SYSTEM")

     Dim j As Integer
     Dim numContainerRefs As Integer
     numContainerRefs = tablespace.containerRefs.numDescriptors
     If tablespace.category = tscSms Then
       Print #fileNo, addTab(1); "USING ("
         For j = 1 To numContainerRefs
           Print #fileNo, addTab(2); "'"; genContainerNameByIndex(tablespace.containerRefs.descriptors(j), thisOrgIndex, thisPoolIndex); "'"; IIf(j = numContainerRefs, "", ",")
         Next j
         Print #fileNo, addTab(1); ")"
     Else
       Print #fileNo, addTab(1); "USING ("
         For j = 1 To numContainerRefs
           Dim thisContainerIndex As Integer
           thisContainerIndex = tablespace.containerRefs.descriptors(j)
             Print #fileNo, addTab(2); IIf(g_containers.descriptors(tablespace.containerRefs.descriptors(j)).type = cntFile, "FILE", "DEVICE"); " "; _
                   "'"; genContainerNameByIndex(thisContainerIndex, thisOrgIndex, thisPoolIndex); "'"; " "; _
                   CStr(g_containers.descriptors(tablespace.containerRefs.descriptors(j)).size); IIf(j = numContainerRefs, "", ",")
         Next j
         Print #fileNo, addTab(1); ")"

       If tablespace.autoResize Then
         Print #fileNo, addTab(1); paddRight("AUTORESIZE "); "YES"

         If tablespace.increasePercent > 0 Then
           Print #fileNo, addTab(1); paddRight("INCREASESIZE "); CStr(tablespace.increasePercent); " PERCENT"
         ElseIf tablespace.increaseAbsolute <> "" Then
           Print #fileNo, addTab(1); paddRight("INCREASESIZE "); tablespace.increaseAbsolute
         End If

         If tablespace.maxSize <> "" Then
           Print #fileNo, addTab(1); paddRight("MAXSIZE "); tablespace.maxSize
         End If
       End If
     End If

     If ddlType = edtPdm Then
       If tablespace.extentSize <> "" Then
         Print #fileNo, addTab(1); paddRight("EXTENTSIZE "); tablespace.extentSize
       End If
       If tablespace.prefetchSize <> "" Then
         Print #fileNo, addTab(1); paddRight("PREFETCHSIZE "); tablespace.prefetchSize
       End If
     End If

     Print #fileNo, addTab(1); paddRight("BUFFERPOOL "); genBufferPoolNameByIndex(tablespace.bufferPoolIndex, thisOrgIndex, thisPoolIndex)

     If ddlType = edtPdm Then
       Print #fileNo, addTab(1); IIf(Not tablespace.useFileSystemCaching, "NO ", ""); "FILE SYSTEM CACHING"
       If tablespace.overhead <> "" Then
         Print #fileNo, addTab(1); paddRight("OVERHEAD "); tablespace.overhead
       End If
       If tablespace.transferrate <> "" Then
         Print #fileNo, addTab(1); paddRight("TRANSFERRATE "); tablespace.transferrate
       End If
       Print #fileNo, addTab(1); paddRight("DROPPED TABLE RECOVERY "); IIf(tablespace.supportDroppedTableRecovery, "ON", "OFF")
     End If
     Print #fileNo, gc_sqlCmdDelim
     Print #fileNo, ""
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTableSpacesDdl( _
   ddlType As DdlTypeId _
 )
   Dim i As Integer
   Dim thisOrgIndex As Integer
   Dim tabSpaceIndex As Integer
   Dim thisPoolIndex As Integer

     If ddlType = edtLdm Then
       For tabSpaceIndex = 1 To g_tableSpaces.numDescriptors Step 1
         genTableSpaceDdl g_tableSpaces.descriptors(tabSpaceIndex), edtLdm
       Next tabSpaceIndex
     ElseIf ddlType = edtPdm Then
       For tabSpaceIndex = 1 To g_tableSpaces.numDescriptors Step 1
           If g_tableSpaces.descriptors(tabSpaceIndex).isCommonToOrgs Then
             genTableSpaceDdl g_tableSpaces.descriptors(tabSpaceIndex), edtPdm
           Else
             For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
               If g_tableSpaces.descriptors(tabSpaceIndex).specificToOrgId <= 0 Or g_tableSpaces.descriptors(tabSpaceIndex).specificToOrgId = g_orgs.descriptors(thisOrgIndex).id Then
                 If g_tableSpaces.descriptors(tabSpaceIndex).isCommonToPools Then
                   genTableSpaceDdl g_tableSpaces.descriptors(tabSpaceIndex), edtPdm, thisOrgIndex
                 Else
                   For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                     If (g_tableSpaces.descriptors(tabSpaceIndex).specificToPool <= 0 Or g_tableSpaces.descriptors(tabSpaceIndex).specificToPool = g_pools.descriptors(thisPoolIndex).id) And _
                         poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                       genTableSpaceDdl g_tableSpaces.descriptors(tabSpaceIndex), edtPdm, thisOrgIndex, thisPoolIndex
                     End If
                   Next thisPoolIndex
                 End If
               End If
             Next thisOrgIndex
           End If
       Next tabSpaceIndex
     End If
 End Sub
 
 
 Sub evalTablespaces()
   Dim thisTabSpaceIndex As Integer
   Dim thisContainerIndex As Integer
     For thisTabSpaceIndex = 1 To g_tableSpaces.numDescriptors Step 1
         g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs.numDescriptors = 0
         For thisContainerIndex = 1 To g_containers.numDescriptors Step 1
             If UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName) = UCase(g_containers.descriptors(thisContainerIndex).tableSpaceName) Then
               g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs.descriptors(allocContainerDescriptorRefIndex(g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs)) = thisContainerIndex
             End If
           g_tableSpaces.descriptors(thisTabSpaceIndex).bufferPoolIndex = getBufferPoolIndexByName(g_tableSpaces.descriptors(thisTabSpaceIndex).bufferPoolName)
           g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceIndex = thisTabSpaceIndex
         Next thisContainerIndex
     Next thisTabSpaceIndex
 End Sub
 
