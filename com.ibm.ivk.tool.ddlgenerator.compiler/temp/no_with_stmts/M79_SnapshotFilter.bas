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
 
       g_snapshotFilter.descriptors(allocSnapshotFilterDescriptorIndex(g_snapshotFilter)).tabName = Trim(thisSheet.Cells(thisRow, colTabName))
       g_snapshotFilter.descriptors(allocSnapshotFilterDescriptorIndex(g_snapshotFilter)).level = getInteger(thisSheet.Cells(thisRow, colLevel))
       g_snapshotFilter.descriptors(allocSnapshotFilterDescriptorIndex(g_snapshotFilter)).collectFilter = Trim(thisSheet.Cells(thisRow, colCollectFilter))
       g_snapshotFilter.descriptors(allocSnapshotFilterDescriptorIndex(g_snapshotFilter)).selectFilter = Trim(thisSheet.Cells(thisRow, colSelectFilter))

       If g_snapshotFilter.descriptors(allocSnapshotFilterDescriptorIndex(g_snapshotFilter)).selectFilter = "=" Then
         g_snapshotFilter.descriptors(allocSnapshotFilterDescriptorIndex(g_snapshotFilter)).selectFilter = g_snapshotFilter.descriptors(allocSnapshotFilterDescriptorIndex(g_snapshotFilter)).collectFilter
       End If
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
       If g_snapshotFilter.descriptors(i).selectFilter <> "" Or g_snapshotFilter.descriptors(i).collectFilter <> "" Then
         Print #fileNo, """"; g_snapshotFilter.descriptors(i).tabName; """,";
         Print #fileNo, IIf(g_snapshotFilter.descriptors(i).level >= 0, CStr(g_snapshotFilter.descriptors(i).level), ""); ",";
         Print #fileNo, IIf(g_snapshotFilter.descriptors(i).collectFilter = "", "", """" & g_snapshotFilter.descriptors(i).collectFilter & """"); ",";
         Print #fileNo, IIf(g_snapshotFilter.descriptors(i).selectFilter = "", "", """" & g_snapshotFilter.descriptors(i).selectFilter & """")
       End If
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
 
