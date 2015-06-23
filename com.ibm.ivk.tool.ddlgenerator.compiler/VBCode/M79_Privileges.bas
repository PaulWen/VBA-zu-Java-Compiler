Attribute VB_Name = "M79_Privileges"
Option Explicit

Private Const colSequenceNumber = 2
Private Const colEnvironment = colSequenceNumber + 1
Private Const colOperation = colEnvironment + 1
Private Const colObjectType = colOperation + 1
Private Const colSchemaName = colObjectType + 1
Private Const colObjectName = colSchemaName + 1
Private Const colFilter = colObjectName + 1
Private Const colGranteeType = colFilter + 1
Private Const colGrantee = colGranteeType + 1
Private Const colPrivilege = colGrantee + 1
Private Const colWithGrantOption = colPrivilege + 1

Private Const firstRow = 3
Private Const sheetName = "Privileges"
Private Const processingStep = 2
Private Const keyWordProductKey = "<prodKey>"

Global g_privileges As PrivilegeDescriptors


Function getGranteeType( _
  ByVal str As String _
) As String
  getGranteeType = ""
  
  str = UCase(Left(Trim(str & ""), 1))
  Select Case str
  Case "U"
    getGranteeType = "USER"
  Case "G"
    getGranteeType = "GROUP"
  Case "P"
    getGranteeType = "PUBLIC"
  Case Else
    logMsg "Unknown Grantee-Type '" & str & "'", ellWarning
  End Select
End Function


Private Sub readSheet()
  initPrivilegeDescriptors g_privileges
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colOperation) & "" <> ""
    With g_privileges.descriptors(allocPrivilegeDescriptorIndex(g_privileges))
      .sequenceNumber = getInteger(thisSheet.Cells(thisRow, colSequenceNumber))
      .environment = Trim(thisSheet.Cells(thisRow, colEnvironment))
      .operation = Trim(thisSheet.Cells(thisRow, colOperation))
      .objectType = Trim(thisSheet.Cells(thisRow, colObjectType))
      .schemaName = Replace(Trim(thisSheet.Cells(thisRow, colSchemaName)), keyWordProductKey, productKey)
      .objectName = Trim(thisSheet.Cells(thisRow, colObjectName))
      .filter = Replace(Trim(thisSheet.Cells(thisRow, colFilter)), keyWordProductKey, productKey)
      .granteeType = getGranteeType(thisSheet.Cells(thisRow, colGranteeType))
      .grantee = Trim(thisSheet.Cells(thisRow, colGrantee))
      .privilege = Trim(thisSheet.Cells(thisRow, colPrivilege))
      .withGrantOption = getBoolean(thisSheet.Cells(thisRow, colWithGrantOption))
    End With
      
    thisRow = thisRow + 1
  Wend
End Sub


Sub getPrivileges()
  If (g_privileges.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetPrivileges()
  g_privileges.numDescriptors = 0
End Sub


Sub genPrivilegesCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbAdmin, clnDbPrivileges, processingStep, "DbAdmin", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  On Error GoTo ErrorExit
  
  Dim i As Integer
  For i = 1 To g_privileges.numDescriptors
    With g_privileges.descriptors(i)
      Print #fileNo, IIf(.sequenceNumber > 0, "" & .sequenceNumber, ""); ",";
      Print #fileNo, IIf(.environment <> "", """" & .environment & """", ""); ",";
      Print #fileNo, IIf(.operation <> "", """" & .operation & """", ""); ",";
      Print #fileNo, IIf(.objectType <> "", """" & .objectType & """", ""); ",";
      Print #fileNo, IIf(.schemaName <> "", """" & .schemaName & """", ""); ",";
      Print #fileNo, IIf(.objectName <> "", """" & .objectName & """", ""); ",";
      Print #fileNo, IIf(.filter <> "", """" & .filter & """", ""); ",";
      Print #fileNo, IIf(.granteeType <> "", """" & .granteeType & """", ""); ",";
      Print #fileNo, IIf(.grantee <> "", """" & .grantee & """", ""); ",";
      Print #fileNo, IIf(.privilege <> "", """" & .privilege & """", ""); ",";
      Print #fileNo, IIf(.withGrantOption, gc_dbTrue, gc_dbFalse)
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


Sub dropPrivilegesCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbAdmin, clnDbPrivileges, g_targetDir, processingStep, onlyIfEmpty, "DbAdmin"
End Sub

