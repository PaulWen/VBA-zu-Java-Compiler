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
   initSnapshotTypeDescriptors(g_snapshotTypes)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colProcName) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

       g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes)).procName = Trim(thisSheet.Cells(thisRow, colProcName))
       g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes)).className = Trim(thisSheet.Cells(thisRow, colTabName))
       g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes)).viewName = Trim(thisSheet.Cells(thisRow, colViewName))
       g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes)).sequenceNo = getInteger(thisSheet.Cells(thisRow, colSequenceNo))
       g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes)).sequenceNoCollect = getInteger(thisSheet.Cells(thisRow, colSequenceNoCollect))
       g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes)).category = Trim(thisSheet.Cells(thisRow, colCategory))
       g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes)).level = getInteger(thisSheet.Cells(thisRow, colLevel))
       g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes)).isApplSpecific = getBoolean(thisSheet.Cells(thisRow, colIsApplSpecific))
       g_snapshotTypes.descriptors(allocSnapshotTypeDescriptorIndex(g_snapshotTypes)).supportAnalysis = getBoolean(thisSheet.Cells(thisRow, colSupportAnalysis))

 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getSnapshotTypes()
   If (g_snapshotTypes.numDescriptors = 0) Then
     readSheet()
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
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim i As Integer
   For i = 1 To g_snapshotTypes.numDescriptors
       Print #fileNo, """"; g_snapshotTypes.descriptors(i).procName; """,";
       Print #fileNo, """"; g_snapshotTypes.descriptors(i).className; """,";
       Print #fileNo, """"; g_snapshotTypes.descriptors(i).viewName; """,";
       Print #fileNo, IIf(g_snapshotTypes.descriptors(i).sequenceNo >= 0, CStr(g_snapshotTypes.descriptors(i).sequenceNo), ""); ",";
       Print #fileNo, IIf(g_snapshotTypes.descriptors(i).category = "", "", """" & g_snapshotTypes.descriptors(i).category & """"); ",";
       Print #fileNo, IIf(g_snapshotTypes.descriptors(i).level > 0, CStr(g_snapshotTypes.descriptors(i).level), ""); ",";
       Print #fileNo, IIf(g_snapshotTypes.descriptors(i).isApplSpecific, gc_dbTrue, gc_dbFalse); ",";
       Print #fileNo, IIf(g_snapshotTypes.descriptors(i).supportAnalysis, gc_dbTrue, gc_dbFalse)
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
   killCsvFileWhereEver(g_sectionIndexDbMonitor, clnSnapshotType, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin")
 End Sub
 
 
 Sub evalSnapshotTypes()
   Dim i As Integer
     For i = 1 To g_snapshotTypes.numDescriptors
         g_snapshotTypes.descriptors(i).classIndex = getClassIndexByName(snDbMonitor, g_snapshotTypes.descriptors(i).className)
     Next i
 End Sub
 
