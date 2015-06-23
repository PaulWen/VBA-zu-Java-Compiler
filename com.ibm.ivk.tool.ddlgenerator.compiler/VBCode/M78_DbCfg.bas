Attribute VB_Name = "M78_DbCfg"
Option Explicit

Private Const colEntryFilter = 1
Private Const colParameter = 2
Private Const colValue = colParameter + 1
Private Const colIsDbmParam = colValue + 1
Private Const colIsDbProfileParam = colIsDbmParam + 1
Private Const colSequenceNo = colIsDbProfileParam + 1
Private Const colServerPlatform = colSequenceNo + 1
Private Const colMinDbRelease = colServerPlatform + 1

Private Const firstRow = 3

Private Const sheetName = "DbCfg"

Global g_dbCfgParams As DbCfgParamDescriptors


Private Sub readSheet()
  initDbCfgParamDescriptors g_dbCfgParams
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colParameter) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
      GoTo NextRow
    End If
    
    allocDbCfgParamDescriptorIndex g_dbCfgParams
    With g_dbCfgParams.descriptors(g_dbCfgParams.numDescriptors)
      .parameter = Trim(thisSheet.Cells(thisRow, colParameter))
      .value = Trim(thisSheet.Cells(thisRow, colValue))
      .isDbmCfgParam = getBoolean(thisSheet.Cells(thisRow, colIsDbmParam))
      .isDbProfileParam = getBoolean(thisSheet.Cells(thisRow, colIsDbProfileParam))
      .sequenceNo = getInteger(thisSheet.Cells(thisRow, colSequenceNo), -1)
      .serverPlatform = Trim(thisSheet.Cells(thisRow, colServerPlatform))
      .minDbRelease = Trim(thisSheet.Cells(thisRow, colMinDbRelease))
    End With
      
NextRow:
    thisRow = thisRow + 1
  Wend
End Sub


Sub getDbCfgParams()
  If (g_dbCfgParams.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetDbCfgParams()
  g_dbCfgParams.numDescriptors = 0
End Sub

