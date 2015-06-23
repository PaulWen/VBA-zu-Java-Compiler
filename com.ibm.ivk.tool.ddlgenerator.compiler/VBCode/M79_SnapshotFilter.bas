Attribute VB_Name = "M79_SnapshotFilter"
Option Explicit

Private Const colEntryFilter = 1
Private Const colTabName = 2
Private Const colLevel = colTabName + 1
Private Const colCollectFilter = colLevel + 1
Private Const colSelectFilter = colCollectFilter + 1

Private Const firstRow = 3
Private Const sheetName = "SnFl"
Private Const processingStep = 2

Global g_snapshotFilter As SnapshotFilterDescriptors


Private Sub readSheet()
  initSnapshotFilterDescriptors g_snapshotFilter
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colTabName) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
      GoTo NextRow
    End If

    With g_snapshotFilter.descriptors(allocSnapshotFilterDescriptorIndex(g_snapshotFilter))
      .tabName = Trim(thisSheet.Cells(thisRow, colTabName))
      .level = getInteger(thisSheet.Cells(thisRow, colLevel))
      .collectFilter = Trim(thisSheet.Cells(thisRow, colCollectFilter))
      .selectFilter = Trim(thisSheet.Cells(thisRow, colSelectFilter))
      
      If .selectFilter = "=" Then
        .selectFilter = .collectFilter
      End If
    End With
NextRow:
    thisRow = thisRow + 1
  Wend
End Sub


Sub getSnapshotFilter()
  If (g_snapshotFilter.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetSnapshotFilter()
  g_snapshotFilter.numDescriptors = 0
End Sub


Sub genSnapshotFilterCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMonitor, clnSnapshotFilter, processingStep, "DbAdmin", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  On Error GoTo ErrorExit
  
  Dim i As Integer
  For i = 1 To g_snapshotFilter.numDescriptors
    With g_snapshotFilter.descriptors(i)
      If .selectFilter <> "" Or .collectFilter <> "" Then
        Print #fileNo, """"; .tabName; """,";
        Print #fileNo, IIf(.level >= 0, CStr(.level), ""); ",";
        Print #fileNo, IIf(.collectFilter = "", "", """" & .collectFilter & """"); ",";
        Print #fileNo, IIf(.selectFilter = "", "", """" & .selectFilter & """")
      End If
    End With
  Next i

NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub dropSnapshotFilterCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbMonitor, clnSnapshotFilter, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin"
End Sub

