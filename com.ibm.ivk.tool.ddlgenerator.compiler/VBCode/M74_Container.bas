Attribute VB_Name = "M74_Container"
Option Explicit

Private Const colTableSpaceName = 2
Private Const colContainerName = colTableSpaceName + 1
Private Const colType = colContainerName + 1
Private Const colIsCommonToOrgs = colType + 1
Private Const colSpecificToOrg = colIsCommonToOrgs + 1
Private Const colIsCommonToPools = colSpecificToOrg + 1
Private Const colSpecificToPool = colIsCommonToPools + 1
Private Const colIsPdmSpecific = colSpecificToPool + 1
Private Const colSize = colIsPdmSpecific + 1

Private Const processingStep = 2

Private Const firstRow = 3

Private Const sheetName = "Cont"

Global g_containers As ContainerDescriptors


Private Sub readSheet()
  initContainerDescriptors g_containers
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colContainerName) & "" <> ""
    With g_containers.descriptors(allocContainerDescriptorIndex(g_containers))
      .tableSpaceName = Trim(thisSheet.Cells(thisRow, colTableSpaceName))
      .containerName = Trim(thisSheet.Cells(thisRow, colContainerName))
      .type = getContainerType(thisSheet.Cells(thisRow, colType))
      .isCommonToOrgs = getBoolean(thisSheet.Cells(thisRow, colIsCommonToOrgs))
      .specificToOrgId = getInteger(thisSheet.Cells(thisRow, colSpecificToOrg))
      .isCommonToPools = .isCommonToOrgs Or getBoolean(thisSheet.Cells(thisRow, colIsCommonToPools))
      .specificToPool = getInteger(thisSheet.Cells(thisRow, colSpecificToPool))
      .isPdmSpecific = getBoolean(thisSheet.Cells(thisRow, colIsPdmSpecific))
      .size = getLong(thisSheet.Cells(thisRow, colSize))
    End With
      
    thisRow = thisRow + 1
  Wend
End Sub


Sub getContainers()
  If (g_containers.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetContainers()
  g_containers.numDescriptors = 0
End Sub


Sub evalContainers()
  Dim i As Integer
  For i = 1 To g_containers.numDescriptors
    With g_containers.descriptors(i)
      .containerName = kwTranslate(.containerName)
      .containerIndex = i
    End With
  Next i
End Sub

