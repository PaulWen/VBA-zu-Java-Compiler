Attribute VB_Name = "M79_SnapshotCol"
Option Explicit

Private Const colEntryFilter = 1
Private Const colTabName = 2
Private Const colColName = colTabName + 1
Private Const colColAlias = colColName + 1
Private Const colDisplayFunction = colColAlias + 1
Private Const colColumnExpression = colDisplayFunction + 1
Private Const colSequenceNo = colColumnExpression + 1
Private Const colCategory = colSequenceNo + 1
Private Const colLevel = colCategory + 1

Private Const firstRow = 3
Private Const sheetName = "SnCol"
Private Const processingStep = 2

Global g_snapshotCols As SnapshotColDescriptors


Private Sub readSheet()
  initSnapshotColDescriptors g_snapshotCols
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colTabName) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
      GoTo NextRow
    End If
    
    With g_snapshotCols.descriptors(allocSnapshotColDescriptorIndex(g_snapshotCols))
      .tabName = Trim(thisSheet.Cells(thisRow, colTabName))
      .colName = Trim(thisSheet.Cells(thisRow, colColName))
      .colAlias = Trim(thisSheet.Cells(thisRow, colColAlias))
      .displayFunction = Trim(thisSheet.Cells(thisRow, colDisplayFunction))
      .columnExpression = Trim(thisSheet.Cells(thisRow, colColumnExpression))
      .sequenceNo = getInteger(thisSheet.Cells(thisRow, colSequenceNo))
      .category = Trim(thisSheet.Cells(thisRow, colCategory))
      .level = getInteger(thisSheet.Cells(thisRow, colLevel))
    End With
      
NextRow:
    thisRow = thisRow + 1
  Wend
End Sub


Sub getSnapshotCols()
  If (g_snapshotCols.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetSnapshotCols()
  g_snapshotCols.numDescriptors = 0
End Sub


Sub genSnapshotColsCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMonitor, clnSnapshotCol, processingStep, "DbAdmin", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  On Error GoTo ErrorExit
  
  Dim i As Integer
  For i = 1 To g_snapshotCols.numDescriptors
    With g_snapshotCols.descriptors(i)
      Print #fileNo, """"; .tabName; """,";
      Print #fileNo, """"; .colName; """,";
      Print #fileNo, IIf(Trim(.colAlias) = "", "", """" & .colAlias & """"); ",";
      Print #fileNo, IIf(Trim(.displayFunction) = "", "", """" & .displayFunction & """"); ",";
      Print #fileNo, IIf(Trim(.columnExpression) = "", "", """" & .columnExpression & """"); ",";
      Print #fileNo, IIf(.sequenceNo >= 0, CStr(.sequenceNo), ""); ",";
      Print #fileNo, IIf(Trim(.category) = "", "", """" & .category & """"); ",";
      Print #fileNo, IIf(.level >= 0, CStr(.level), "")
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


Sub dropSnapshotColsCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbMonitor, clnSnapshotCol, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin"
End Sub

