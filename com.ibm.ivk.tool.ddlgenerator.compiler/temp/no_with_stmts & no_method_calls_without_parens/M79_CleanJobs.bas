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
   initCleanJobDescriptors(g_cleanjobs)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colJobCategory) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

       g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs)).jobCategory = Trim(thisSheet.Cells(thisRow, colJobCategory))
       g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs)).jobName = Trim(thisSheet.Cells(thisRow, colJobName))
       g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs)).level = Trim(thisSheet.Cells(thisRow, colLevel))
       g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs)).sequenceNo = Trim(thisSheet.Cells(thisRow, colSequenceNo))
       g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs)).tableSchema = Trim(thisSheet.Cells(thisRow, colTableSchema))
       g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs)).tableName = Trim(thisSheet.Cells(thisRow, colTableName))
       g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs)).tableRef = Trim(thisSheet.Cells(thisRow, colTableRef))
       g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs)).condition = Trim(thisSheet.Cells(thisRow, colCondition))
       g_cleanjobs.descriptors(allocCleanJobDescriptorIndex(g_cleanjobs)).commitCount = getLong(thisSheet.Cells(thisRow, colCommitCount))

 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getCleanJobs()
   If (g_cleanjobs.numDescriptors = 0) Then
     readSheet()
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
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim i As Integer
   For i = 1 To g_cleanjobs.numDescriptors
       Print #fileNo, """"; g_cleanjobs.descriptors(i).jobCategory; """,";
       Print #fileNo, IIf(g_cleanjobs.descriptors(i).jobName <> "", """" & g_cleanjobs.descriptors(i).jobName & """", "") & ",";
       Print #fileNo, IIf(g_cleanjobs.descriptors(i).level <> "", g_cleanjobs.descriptors(i).level, "") & ",";
       Print #fileNo, IIf(g_cleanjobs.descriptors(i).sequenceNo <> "", g_cleanjobs.descriptors(i).sequenceNo, "") & ",";
       Print #fileNo, IIf(g_cleanjobs.descriptors(i).tableSchema <> "", """" & g_cleanjobs.descriptors(i).tableSchema & """", "") & ",";
       Print #fileNo, """"; g_cleanjobs.descriptors(i).tableName; """,";
       Print #fileNo, IIf(g_cleanjobs.descriptors(i).tableRef <> "", """" & g_cleanjobs.descriptors(i).tableRef & """", "") & ",";
       Print #fileNo, IIf(g_cleanjobs.descriptors(i).condition <> "", """" & g_cleanjobs.descriptors(i).condition & """", "") & ",";
       Print #fileNo, IIf(g_cleanjobs.descriptors(i).commitCount > 0, CStr(g_cleanjobs.descriptors(i).commitCount), "") & ","
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
   killCsvFileWhereEver(g_sectionIndexDbAdmin, clnCleanJobs, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin")
 End Sub
 
 
