 Attribute VB_Name = "M78_DbProfile"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colProfileName = 2
 Private Const colObjectType = colProfileName + 1
 Private Const colSchemaName = colObjectType + 1
 Private Const colObjectName = colSchemaName + 1
 Private Const colSequenceNo = colObjectName + 1
 Private Const colConfigParameter = colSequenceNo + 1
 Private Const colConfigValue = colConfigParameter + 1
 Private Const colServerPlatform = colConfigValue + 1
 Private Const colMinDbRelease = colServerPlatform + 1
 
 Private Const firstRow = 3
 
 Private Const sheetName = "DbProf"
 
 Private Const processingStep = 2
 
 Global g_dbCfgProfiles As DbCfgProfileDescriptors
 
 
 Private Sub readSheet()
   initDbCfgProfileDescriptors g_dbCfgProfiles

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colObjectType) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

     allocDbCfgProfileDescriptorIndex g_dbCfgProfiles
       g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors).profileName = Trim(thisSheet.Cells(thisRow, colProfileName))
       g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors).objectType = Trim(thisSheet.Cells(thisRow, colObjectType))
       g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors).schemaName = Trim(thisSheet.Cells(thisRow, colSchemaName))
       g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors).objectName = Trim(thisSheet.Cells(thisRow, colObjectName))
       g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors).sequenceNo = getInteger(thisSheet.Cells(thisRow, colSequenceNo))
       g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors).configParameter = Trim(thisSheet.Cells(thisRow, colConfigParameter))
       g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors).configValue = Trim(thisSheet.Cells(thisRow, colConfigValue))
       g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors).serverPlatform = Trim(thisSheet.Cells(thisRow, colServerPlatform))
       g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors).minDbRelease = Trim(thisSheet.Cells(thisRow, colMinDbRelease))

 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getDbCfgProfiles()
   If (g_dbCfgProfiles.numDescriptors = 0) Then
     readSheet
   End If
 End Sub
 
 
 Sub resetDbCfgProfiles()
   g_dbCfgProfiles.numDescriptors = 0
 End Sub
 
 
 Sub genDbCfgProfileCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbAdmin, clnDbCfgProfile, processingStep, "DbAdmin", ddlType)
   assertDir fileName
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim i As Integer
   For i = 1 To g_dbCfgProfiles.numDescriptors
       Print #fileNo, """"; g_dbCfgProfiles.descriptors(i).profileName; """,";
       Print #fileNo, """"; UCase(g_dbCfgProfiles.descriptors(i).objectType); """,";
       Print #fileNo, IIf(g_dbCfgProfiles.descriptors(i).schemaName <> "", """" & UCase(g_dbCfgProfiles.descriptors(i).schemaName) & """", "") & ",";
       Print #fileNo, """"; UCase(g_dbCfgProfiles.descriptors(i).objectName); """,";
       Print #fileNo, IIf(g_dbCfgProfiles.descriptors(i).sequenceNo > 0, g_dbCfgProfiles.descriptors(i).sequenceNo, "") & ",";
       Print #fileNo, """"; UCase(g_dbCfgProfiles.descriptors(i).configParameter); """,";
       Print #fileNo, """"; g_dbCfgProfiles.descriptors(i).configValue; """,";
       Print #fileNo, IIf(g_dbCfgProfiles.descriptors(i).serverPlatform <> "", """" & UCase(g_dbCfgProfiles.descriptors(i).serverPlatform) & """", "") & ",";
       Print #fileNo, IIf(g_dbCfgProfiles.descriptors(i).minDbRelease <> "", UCase(Replace(g_dbCfgProfiles.descriptors(i).minDbRelease, ",", ".")), "") & ",";
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
 
 
 Sub dropDbCfgProfilesCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   killCsvFileWhereEver g_sectionIndexDbAdmin, clnDbCfgProfile, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin"
 End Sub
 
 
 
