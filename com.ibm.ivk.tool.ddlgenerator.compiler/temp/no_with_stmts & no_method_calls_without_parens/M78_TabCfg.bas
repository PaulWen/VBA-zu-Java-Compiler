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
   initTabCfgParamDescriptors(g_TabCfgParams)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colSequenceNo) & "" <> ""
     allocTabCfgParamDescriptorIndex(g_TabCfgParams)
       g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors).sequenceNumber = getInteger(thisSheet.Cells(thisRow, colSequenceNo))
       g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors).schemaPattern = Trim(thisSheet.Cells(thisRow, colSchemaPattern))
       g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors).namePattern = Trim(thisSheet.Cells(thisRow, colNamePattern))
       g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors).schemaPatternExcluded = Trim(thisSheet.Cells(thisRow, colSchemaPatternExcluded))
       g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors).namePatternExcluded = Trim(thisSheet.Cells(thisRow, colNamePatternExcluded))
       g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors).pctFree = getInteger(thisSheet.Cells(thisRow, colPctFree))
       g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors).isVolatile = getTvBoolean(thisSheet.Cells(thisRow, colIsVolatile))
       g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors).useRowCompression = getTvBoolean(thisSheet.Cells(thisRow, colUseRowCompression))
       g_TabCfgParams.descriptors(g_TabCfgParams.numDescriptors).useIndexCompression = getTvBoolean(thisSheet.Cells(thisRow, colUseIndexCompression))

     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getTabCfgParams()
   If (g_TabCfgParams.numDescriptors = 0) Then
     readSheet()
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
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo

   Dim i As Integer
   For i = 1 To g_TabCfgParams.numDescriptors
       Print #fileNo, CStr(g_TabCfgParams.descriptors(i).sequenceNumber); ",";
       Print #fileNo, """"; UCase(g_TabCfgParams.descriptors(i).schemaPattern); """,";
       Print #fileNo, """"; UCase(g_TabCfgParams.descriptors(i).namePattern); """,";
       Print #fileNo, IIf(g_TabCfgParams.descriptors(i).schemaPatternExcluded = "", "", """" & UCase(g_TabCfgParams.descriptors(i).schemaPatternExcluded) & """"); ",";
       Print #fileNo, IIf(g_TabCfgParams.descriptors(i).namePatternExcluded = "", "", """" & UCase(g_TabCfgParams.descriptors(i).namePatternExcluded) & """"); ",";
       Print #fileNo, IIf(g_TabCfgParams.descriptors(i).pctFree < 0, "", CStr(g_TabCfgParams.descriptors(i).pctFree)); ",";
       Print #fileNo, IIf(g_TabCfgParams.descriptors(i).isVolatile = tvTrue, gc_dbTrue, IIf(g_TabCfgParams.descriptors(i).isVolatile = tvFalse, gc_dbFalse, "")); ",";
       Print #fileNo, IIf(g_TabCfgParams.descriptors(i).useRowCompression = tvTrue, gc_dbTrue, IIf(g_TabCfgParams.descriptors(i).useRowCompression = tvFalse, gc_dbFalse, "")); ",";
       Print #fileNo, IIf(g_TabCfgParams.descriptors(i).useIndexCompression = tvTrue, gc_dbTrue, IIf(g_TabCfgParams.descriptors(i).useIndexCompression = tvFalse, gc_dbFalse, "")); ",";
       Print #fileNo,
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
   killCsvFileWhereEver(g_sectionIndexDbAdmin, clnTableCfg, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin")
 End Sub
