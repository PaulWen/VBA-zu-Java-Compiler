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
    With g_dbCfgProfiles.descriptors(g_dbCfgProfiles.numDescriptors)
      .profileName = Trim(thisSheet.Cells(thisRow, colProfileName))
      .objectType = Trim(thisSheet.Cells(thisRow, colObjectType))
      .schemaName = Trim(thisSheet.Cells(thisRow, colSchemaName))
      .objectName = Trim(thisSheet.Cells(thisRow, colObjectName))
      .sequenceNo = getInteger(thisSheet.Cells(thisRow, colSequenceNo))
      .configParameter = Trim(thisSheet.Cells(thisRow, colConfigParameter))
      .configValue = Trim(thisSheet.Cells(thisRow, colConfigValue))
      .serverPlatform = Trim(thisSheet.Cells(thisRow, colServerPlatform))
      .minDbRelease = Trim(thisSheet.Cells(thisRow, colMinDbRelease))
    End With
      
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
    With g_dbCfgProfiles.descriptors(i)
      Print #fileNo, """"; .profileName; """,";
      Print #fileNo, """"; UCase(.objectType); """,";
      Print #fileNo, IIf(.schemaName <> "", """" & UCase(.schemaName) & """", "") & ",";
      Print #fileNo, """"; UCase(.objectName); """,";
      Print #fileNo, IIf(.sequenceNo > 0, .sequenceNo, "") & ",";
      Print #fileNo, """"; UCase(.configParameter); """,";
      Print #fileNo, """"; .configValue; """,";
      Print #fileNo, IIf(.serverPlatform <> "", """" & UCase(.serverPlatform) & """", "") & ",";
      Print #fileNo, IIf(.minDbRelease <> "", UCase(Replace(.minDbRelease, ",", ".")), "") & ",";
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


Sub dropDbCfgProfilesCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbAdmin, clnDbCfgProfile, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin"
End Sub



