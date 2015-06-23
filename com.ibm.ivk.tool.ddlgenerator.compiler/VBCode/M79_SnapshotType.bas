Attribute VB_Name = "M79_SnapshotType"
Option Explicit

Private Const colEntryFilter = 1
Private Const colProcName = 2
Private Const colTabName = colProcName + 1
Private Const colViewName = colTabName + 1
Private Const colSequenceNo = colViewName + 1
Private Const colSequenceNoCollect = colSequenceNo + 1
Private Const colCategory = colSequenceNoCollect + 1
Private Const colLevel = colCategory + 1
Private Const colIsApplSpecific = colLevel + 1
Private Const colSupportAnalysis = colIsApplSpecific + 1

Private Const firstRow = 3
Private Const sheetName = "SnTp"
Private Const processingStep = 2

Global g_snapshotTypes As SnapshotTypeDescriptors


Private Sub readSheet()
  initSnapshotTypeDescriptors g_snapshotTypes
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colProcName) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
      GoTo NextRow
    End If
    
    With g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes))
      .procName = Trim(thisSheet.Cells(thisRow, colProcName))
      .className = Trim(thisSheet.Cells(thisRow, colTabName))
      .viewName = Trim(thisSheet.Cells(thisRow, colViewName))
      .sequenceNo = getInteger(thisSheet.Cells(thisRow, colSequenceNo))
      .sequenceNoCollect = getInteger(thisSheet.Cells(thisRow, colSequenceNoCollect))
      .category = Trim(thisSheet.Cells(thisRow, colCategory))
      .level = getInteger(thisSheet.Cells(thisRow, colLevel))
      .isApplSpecific = getBoolean(thisSheet.Cells(thisRow, colIsApplSpecific))
      .supportAnalysis = getBoolean(thisSheet.Cells(thisRow, colSupportAnalysis))
    End With
      
NextRow:
    thisRow = thisRow + 1
  Wend
End Sub


Sub getSnapshotTypes()
  If (g_snapshotTypes.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetSnapshotTypes()
  g_snapshotTypes.numDescriptors = 0
End Sub


Sub genSnapshotTypesCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMonitor, clnSnapshotType, processingStep, "DbAdmin", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  On Error GoTo ErrorExit
  
  Dim i As Integer
  For i = 1 To g_snapshotTypes.numDescriptors
    With g_snapshotTypes.descriptors(i)
      Print #fileNo, """"; .procName; """,";
      Print #fileNo, """"; .className; """,";
      Print #fileNo, """"; .viewName; """,";
      Print #fileNo, IIf(.sequenceNo >= 0, CStr(.sequenceNo), ""); ",";
      Print #fileNo, IIf(.category = "", "", """" & .category & """"); ",";
      Print #fileNo, IIf(.level > 0, CStr(.level), ""); ",";
      Print #fileNo, IIf(.isApplSpecific, gc_dbTrue, gc_dbFalse); ",";
      Print #fileNo, IIf(.supportAnalysis, gc_dbTrue, gc_dbFalse)
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


Sub dropSnapshotTypesCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbMonitor, clnSnapshotType, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin"
End Sub


Sub evalSnapshotTypes()
  Dim i As Integer
  With g_snapshotTypes
    For i = 1 To .numDescriptors
      With .descriptors(i)
        .classIndex = getClassIndexByName(snDbMonitor, .className)
      End With
    Next i
  End With
End Sub

