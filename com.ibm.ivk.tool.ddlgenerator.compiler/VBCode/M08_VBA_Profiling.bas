Attribute VB_Name = "M08_VBA_Profiling"
' ### IF IVK ###
Option Explicit

Private Const colEntryFilter = 1
Private Const colModuleName = 2
Private Const colProcName = 3
Private Const colLevel = 4

Private Const processingStep = 2
Private Const firstRow = 3
Private Const sheetName = "Prof"

Global g_profLevels As ProfLevelDescriptors

Private profFileNo As Integer
Private profPaused As Boolean
Private profCallCount As Long
Private profCallLevel As Integer

Public Type SystemTime
  wYear As Integer
  wMonth As Integer
  wDayOfWeek As Integer
  wDay As Integer
  wHour As Integer
  wMinute As Integer
  wSecond As Integer
  wMilliseconds As Integer
End Type

Private Sub readSheet()
  initErrDescriptors g_errs

  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

  While thisSheet.Cells(thisRow, colModuleName) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Or _
       getInteger(thisSheet.Cells(thisRow, colLevel), 1) <= 0 Then
      GoTo NextRow
    End If

    With g_profLevels.descriptors(allocProfLevelDescriptorIndex(g_profLevels))
      .level = getInteger(thisSheet.Cells(thisRow, colLevel), 1)
      .moduleName = baseName(Trim(thisSheet.Cells(thisRow, colModuleName)), ".bas")
      .procName = Trim(thisSheet.Cells(thisRow, colProcName))
    End With

NextRow:
    thisRow = thisRow + 1
  Wend
End Sub

Sub profLogClose()
  On Error Resume Next

  Close #profFileNo
  profFileNo = 0
  profCallCount = 0
  profCallLevel = 0
  profPaused = True
End Sub
' ### ENDIF IVK ###


