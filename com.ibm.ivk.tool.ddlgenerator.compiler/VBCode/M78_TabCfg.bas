Attribute VB_Name = "M78_TabCfg"
Option Explicit

Private Const colSequenceNo = 2
Private Const colSchemaPattern = colSequenceNo + 1
Private Const colNamePattern = colSchemaPattern + 1
Private Const colSchemaPatternExcluded = colNamePattern + 1
Private Const colNamePatternExcluded = colSchemaPatternExcluded + 1
Private Const colPctFree = colNamePatternExcluded + 1
Private Const colIsVolatile = colPctFree + 1
Private Const colUseRowCompression = colIsVolatile + 1
Private Const colUseIndexCompression = colUseRowCompression + 1

Private Const firstRow = 3

Private Const sheetName = "TabCfg"

Private Const processingStep = 2

Global g_TabCfgParams As TabCfgParamDescriptors


Private Sub readSheet()
  initTabCfgParamDescriptors g_TabCfgParams
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colSequenceNo) & "" <> ""
    allocTabCfgParamDescriptorIndex g_TabCfgParams
    With g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors)
      .sequenceNumber = getInteger(thisSheet.Cells(thisRow, colSequenceNo))
      .schemaPattern = Trim(thisSheet.Cells(thisRow, colSchemaPattern))
      .namePattern = Trim(thisSheet.Cells(thisRow, colNamePattern))
      .schemaPatternExcluded = Trim(thisSheet.Cells(thisRow, colSchemaPatternExcluded))
      .namePatternExcluded = Trim(thisSheet.Cells(thisRow, colNamePatternExcluded))
      .pctFree = getInteger(thisSheet.Cells(thisRow, colPctFree))
      .isVolatile = getTvBoolean(thisSheet.Cells(thisRow, colIsVolatile))
      .useRowCompression = getTvBoolean(thisSheet.Cells(thisRow, colUseRowCompression))
      .useIndexCompression = getTvBoolean(thisSheet.Cells(thisRow, colUseIndexCompression))
    End With
      
    thisRow = thisRow + 1
  Wend
End Sub


Sub getTabCfgParams()
  If (g_TabCfgParams.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetTabCfgParams()
  g_TabCfgParams.numDescriptors = 0
End Sub


Sub genTabCfgCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  On Error GoTo ErrorExit
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbAdmin, clnTableCfg, processingStep, "DbAdmin", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  
  Dim i As Integer
  For i = 1 To g_TabCfgParams.numDescriptors
    With g_TabCfgParams.descriptors(i)
      Print #fileNo, CStr(.sequenceNumber); ",";
      Print #fileNo, """"; UCase(.schemaPattern); """,";
      Print #fileNo, """"; UCase(.namePattern); """,";
      Print #fileNo, IIf(.schemaPatternExcluded = "", "", """" & UCase(.schemaPatternExcluded) & """"); ",";
      Print #fileNo, IIf(.namePatternExcluded = "", "", """" & UCase(.namePatternExcluded) & """"); ",";
      Print #fileNo, IIf(.pctFree < 0, "", CStr(.pctFree)); ",";
      Print #fileNo, IIf(.isVolatile = tvTrue, gc_dbTrue, IIf(.isVolatile = tvFalse, gc_dbFalse, "")); ",";
      Print #fileNo, IIf(.useRowCompression = tvTrue, gc_dbTrue, IIf(.useRowCompression = tvFalse, gc_dbFalse, "")); ",";
      Print #fileNo, IIf(.useIndexCompression = tvTrue, gc_dbTrue, IIf(.useIndexCompression = tvFalse, gc_dbFalse, "")); ",";
      Print #fileNo,
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


Sub dropTabCfgsCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbAdmin, clnTableCfg, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin"
End Sub
