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
    
    With g_tableSpaces.descriptors(allocTableSpaceDescriptorIndex(g_tableSpaces))
      .tableSpaceName = Trim(thisSheet.Cells(thisRow, colTableSpaceName))
      .shortName = Trim(thisSheet.Cells(thisRow, colShortName))
      .isCommonToOrgs = getBoolean(thisSheet.Cells(thisRow, colIsCommonToOrgs))
      .specificToOrgId = getInteger(thisSheet.Cells(thisRow, colSpecificToOrg))
      .isCommonToPools = .isCommonToOrgs Or getBoolean(thisSheet.Cells(thisRow, colIsCommonToPools))
      .specificToPool = getInteger(thisSheet.Cells(thisRow, colSpecificToPool))
      .isPdmSpecific = getBoolean(thisSheet.Cells(thisRow, colIsPdmSpecific))
       .isMonitor = getBoolean(thisSheet.Cells(thisRow, colIsMonitor))
      .type = thisSheet.Cells(thisRow, colType)
      .category = getTabSpaceCategory(thisSheet.Cells(thisRow, colManagedBy))
      .pageSize = Trim(thisSheet.Cells(thisRow, colPageSize))
      .autoResize = getBoolean(thisSheet.Cells(thisRow, colAutoResize))
      .increasePercent = getInteger(thisSheet.Cells(thisRow, colIncreasePercent))
      .increaseAbsolute = Trim(thisSheet.Cells(thisRow, colIncreaseAbsolute))
      .maxSize = Trim(thisSheet.Cells(thisRow, colMaxSize))
      .extentSize = Trim(thisSheet.Cells(thisRow, colExtentSize))
      .prefetchSize = Trim(thisSheet.Cells(thisRow, colPrefetchSize))
      .bufferPoolName = Trim(thisSheet.Cells(thisRow, colBufferPool))
      .overhead = Trim(thisSheet.Cells(thisRow, colOverhead))
      .transferrate = Trim(thisSheet.Cells(thisRow, colTransferRate))
      .useFileSystemCaching = getBoolean(thisSheet.Cells(thisRow, colUseFileSystemCaching))
      .supportDroppedTableRecovery = getBoolean(thisSheet.Cells(thisRow, colSupportDroppedTableReovery))
    End With
      
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
  
  With tablespace
    fileNo = openDdlFile(g_targetDir, g_sectionIndexDb, processingStep, ddlType, thisOrgIndex, thisPoolIndex)
    
    On Error GoTo ErrorExit
    
    printChapterHeader "TableSpace """ & .tableSpaceName & """", fileNo
    Print #fileNo, addTab(0); "CREATE "; IIf(.type <> "", UCase(.type) & " ", ""); "TABLESPACE "
    Print #fileNo, addTab(1); UCase(.tableSpaceName)
    Print #fileNo, addTab(1); paddRight("PAGESIZE "); IIf(.pageSize <> "", .pageSize, "4096")
    
    Print #fileNo, addTab(1); paddRight("MANAGED BY "); IIf(.category = tscDms, "DATABASE", "SYSTEM")
    
    Dim j As Integer
    Dim numContainerRefs As Integer
    numContainerRefs = tablespace.containerRefs.numDescriptors
    If .category = tscSms Then
      Print #fileNo, addTab(1); "USING ("
      With .containerRefs
        For j = 1 To numContainerRefs
          Print #fileNo, addTab(2); "'"; genContainerNameByIndex(.descriptors(j), thisOrgIndex, thisPoolIndex); "'"; IIf(j = numContainerRefs, "", ",")
        Next j
        Print #fileNo, addTab(1); ")"
      End With
    Else
      Print #fileNo, addTab(1); "USING ("
      With .containerRefs
        For j = 1 To numContainerRefs
          Dim thisContainerIndex As Integer
          thisContainerIndex = .descriptors(j)
          With g_containers.descriptors(.descriptors(j))
            Print #fileNo, addTab(2); IIf(.type = cntFile, "FILE", "DEVICE"); " "; _
                  "'"; genContainerNameByIndex(thisContainerIndex, thisOrgIndex, thisPoolIndex); "'"; " "; _
                  CStr(.size); IIf(j = numContainerRefs, "", ",")
          End With
        Next j
        Print #fileNo, addTab(1); ")"
      End With
      
      If .autoResize Then
        Print #fileNo, addTab(1); paddRight("AUTORESIZE "); "YES"
        
        If .increasePercent > 0 Then
          Print #fileNo, addTab(1); paddRight("INCREASESIZE "); CStr(.increasePercent); " PERCENT"
        ElseIf .increaseAbsolute <> "" Then
          Print #fileNo, addTab(1); paddRight("INCREASESIZE "); .increaseAbsolute
        End If
        
        If .maxSize <> "" Then
          Print #fileNo, addTab(1); paddRight("MAXSIZE "); .maxSize
        End If
      End If
    End If
    
    If ddlType = edtPdm Then
      If .extentSize <> "" Then
        Print #fileNo, addTab(1); paddRight("EXTENTSIZE "); .extentSize
      End If
      If .prefetchSize <> "" Then
        Print #fileNo, addTab(1); paddRight("PREFETCHSIZE "); .prefetchSize
      End If
    End If
    
    Print #fileNo, addTab(1); paddRight("BUFFERPOOL "); genBufferPoolNameByIndex(.bufferPoolIndex, thisOrgIndex, thisPoolIndex)
    
    If ddlType = edtPdm Then
      Print #fileNo, addTab(1); IIf(Not .useFileSystemCaching, "NO ", ""); "FILE SYSTEM CACHING"
      If .overhead <> "" Then
        Print #fileNo, addTab(1); paddRight("OVERHEAD "); .overhead
      End If
      If .transferrate <> "" Then
        Print #fileNo, addTab(1); paddRight("TRANSFERRATE "); .transferrate
      End If
      Print #fileNo, addTab(1); paddRight("DROPPED TABLE RECOVERY "); IIf(.supportDroppedTableRecovery, "ON", "OFF")
    End If
    Print #fileNo, gc_sqlCmdDelim
    Print #fileNo, ""
  End With

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
  
  With g_tableSpaces
    If ddlType = edtLdm Then
      For tabSpaceIndex = 1 To .numDescriptors Step 1
        genTableSpaceDdl g_tableSpaces.descriptors(tabSpaceIndex), edtLdm
      Next tabSpaceIndex
    ElseIf ddlType = edtPdm Then
      For tabSpaceIndex = 1 To .numDescriptors Step 1
        With .descriptors(tabSpaceIndex)
          If .isCommonToOrgs Then
            genTableSpaceDdl g_tableSpaces.descriptors(tabSpaceIndex), edtPdm
          Else
            For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
              If .specificToOrgId <= 0 Or .specificToOrgId = g_orgs.descriptors(thisOrgIndex).id Then
                If .isCommonToPools Then
                  genTableSpaceDdl g_tableSpaces.descriptors(tabSpaceIndex), edtPdm, thisOrgIndex
                Else
                  For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                    If (.specificToPool <= 0 Or .specificToPool = g_pools.descriptors(thisPoolIndex).id) And _
                        poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                      genTableSpaceDdl g_tableSpaces.descriptors(tabSpaceIndex), edtPdm, thisOrgIndex, thisPoolIndex
                    End If
                  Next thisPoolIndex
                End If
              End If
            Next thisOrgIndex
          End If
        End With
      Next tabSpaceIndex
    End If
  End With
End Sub


Sub evalTablespaces()
  Dim thisTabSpaceIndex As Integer
  Dim thisContainerIndex As Integer
  With g_tableSpaces
    For thisTabSpaceIndex = 1 To .numDescriptors Step 1
      With .descriptors(thisTabSpaceIndex)
        g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs.numDescriptors = 0
        For thisContainerIndex = 1 To g_containers.numDescriptors Step 1
          With g_containers.descriptors(thisContainerIndex)
            If UCase(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName) = UCase(.tableSpaceName) Then
              g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs.descriptors(allocContainerDescriptorRefIndex(g_tableSpaces.descriptors(thisTabSpaceIndex).containerRefs)) = thisContainerIndex
            End If
          End With
          .bufferPoolIndex = getBufferPoolIndexByName(.bufferPoolName)
          .tableSpaceIndex = thisTabSpaceIndex
        Next thisContainerIndex
      End With
    Next thisTabSpaceIndex
  End With
End Sub

