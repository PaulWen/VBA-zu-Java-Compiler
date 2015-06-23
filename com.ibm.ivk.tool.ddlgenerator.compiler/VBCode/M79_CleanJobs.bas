Attribute VB_Name = "M79_CleanJobs"
Option Explicit

Private Const colEntryFilter = 1
Private Const colJobCategory = 2
Private Const colJobName = colJobCategory + 1
Private Const colLevel = colJobName + 1
Private Const colSequenceNo = colLevel + 1
Private Const colTableSchema = colSequenceNo + 1
Private Const colTableName = colTableSchema + 1
Private Const colTableRef = colTableName + 1
Private Const colCondition = colTableRef + 1
Private Const colCommitCount = colCondition + 1

Private Const firstRow = 3

Private Const sheetName = "CleanJobs"

Private Const processingStep = 2

Global g_cleanjobs As CleanJobDescriptors


Private Sub readSheet()
  initCleanJobDescriptors g_cleanjobs
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colJobCategory) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
      GoTo NextRow
    End If
    
    With g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs))
      .jobCategory = Trim(thisSheet.Cells(thisRow, colJobCategory))
      .jobName = Trim(thisSheet.Cells(thisRow, colJobName))
      .level = Trim(thisSheet.Cells(thisRow, colLevel))
      .sequenceNo = Trim(thisSheet.Cells(thisRow, colSequenceNo))
      .tableSchema = Trim(thisSheet.Cells(thisRow, colTableSchema))
      .tableName = Trim(thisSheet.Cells(thisRow, colTableName))
      .tableRef = Trim(thisSheet.Cells(thisRow, colTableRef))
      .condition = Trim(thisSheet.Cells(thisRow, colCondition))
      .commitCount = getLong(thisSheet.Cells(thisRow, colCommitCount))
    End With
      
NextRow:
    thisRow = thisRow + 1
  Wend
End Sub


Sub getCleanJobs()
  If (g_cleanjobs.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetCleanJobs()
  g_cleanjobs.numDescriptors = 0
End Sub


Sub genCleanJobsCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbAdmin, clnCleanJobs, processingStep, "DbAdmin", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  On Error GoTo ErrorExit
  
  Dim i As Integer
  For i = 1 To g_cleanjobs.numDescriptors
    With g_cleanjobs.descriptors(i)
      Print #fileNo, """"; .jobCategory; """,";
      Print #fileNo, IIf(.jobName <> "", """" & .jobName & """", "") & ",";
      Print #fileNo, IIf(.level <> "", .level, "") & ",";
      Print #fileNo, IIf(.sequenceNo <> "", .sequenceNo, "") & ",";
      Print #fileNo, IIf(.tableSchema <> "", """" & .tableSchema & """", "") & ",";
      Print #fileNo, """"; .tableName; """,";
      Print #fileNo, IIf(.tableRef <> "", """" & .tableRef & """", "") & ",";
      Print #fileNo, IIf(.condition <> "", """" & .condition & """", "") & ",";
      Print #fileNo, IIf(.commitCount > 0, CStr(.commitCount), "") & ","
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


Sub dropCleanJobsCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbAdmin, clnCleanJobs, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin"
End Sub


