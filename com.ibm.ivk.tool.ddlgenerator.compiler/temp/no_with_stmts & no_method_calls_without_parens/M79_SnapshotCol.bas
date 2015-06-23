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
   initSnapshotColDescriptors(g_snapshotCols)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colTabName) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

       g_snapshotCols.descriptors(allocSnapshotColDescriptorIndex(g_snapshotCols)).tabName = Trim(thisSheet.Cells(thisRow, colTabName))
       g_snapshotCols.descriptors(allocSnapshotColDescriptorIndex(g_snapshotCols)).colName = Trim(thisSheet.Cells(thisRow, colColName))
       g_snapshotCols.descriptors(allocSnapshotColDescriptorIndex(g_snapshotCols)).colAlias = Trim(thisSheet.Cells(thisRow, colColAlias))
       g_snapshotCols.descriptors(allocSnapshotColDescriptorIndex(g_snapshotCols)).displayFunction = Trim(thisSheet.Cells(thisRow, colDisplayFunction))
       g_snapshotCols.descriptors(allocSnapshotColDescriptorIndex(g_snapshotCols)).columnExpression = Trim(thisSheet.Cells(thisRow, colColumnExpression))
       g_snapshotCols.descriptors(allocSnapshotColDescriptorIndex(g_snapshotCols)).sequenceNo = getInteger(thisSheet.Cells(thisRow, colSequenceNo))
       g_snapshotCols.descriptors(allocSnapshotColDescriptorIndex(g_snapshotCols)).category = Trim(thisSheet.Cells(thisRow, colCategory))
       g_snapshotCols.descriptors(allocSnapshotColDescriptorIndex(g_snapshotCols)).level = getInteger(thisSheet.Cells(thisRow, colLevel))

 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getSnapshotCols()
   If (g_snapshotCols.numDescriptors = 0) Then
     readSheet()
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
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim i As Integer
   For i = 1 To g_snapshotCols.numDescriptors
       Print #fileNo, """"; g_snapshotCols.descriptors(i).tabName; """,";
       Print #fileNo, """"; g_snapshotCols.descriptors(i).colName; """,";
       Print #fileNo, IIf(Trim(g_snapshotCols.descriptors(i).colAlias) = "", "", """" & g_snapshotCols.descriptors(i).colAlias & """"); ",";
       Print #fileNo, IIf(Trim(g_snapshotCols.descriptors(i).displayFunction) = "", "", """" & g_snapshotCols.descriptors(i).displayFunction & """"); ",";
       Print #fileNo, IIf(Trim(g_snapshotCols.descriptors(i).columnExpression) = "", "", """" & g_snapshotCols.descriptors(i).columnExpression & """"); ",";
       Print #fileNo, IIf(g_snapshotCols.descriptors(i).sequenceNo >= 0, CStr(g_snapshotCols.descriptors(i).sequenceNo), ""); ",";
       Print #fileNo, IIf(Trim(g_snapshotCols.descriptors(i).category) = "", "", """" & g_snapshotCols.descriptors(i).category & """"); ",";
       Print #fileNo, IIf(g_snapshotCols.descriptors(i).level >= 0, CStr(g_snapshotCols.descriptors(i).level), "")
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
   killCsvFileWhereEver(g_sectionIndexDbMonitor, clnSnapshotCol, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin")
 End Sub
 
