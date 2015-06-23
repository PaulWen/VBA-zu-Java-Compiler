Attribute VB_Name = "M75_BufferPool"
Option Explicit

Private Const colBufPoolName = 2
Private Const colShortName = colBufPoolName + 1
Private Const colIsCommonToOrgs = colShortName + 1
Private Const colSpecificToOrg = colIsCommonToOrgs + 1
Private Const colIsCommonToPools = colSpecificToOrg + 1
Private Const colSpecificToPool = colIsCommonToPools + 1
Private Const colIsPdmSpecific = colSpecificToPool + 1
Private Const colNumBlockPages = colIsPdmSpecific + 1
Private Const colPageSize = colNumBlockPages + 1
Private Const colSize = colPageSize + 1

Private Const processingStep = 1

Private Const firstRow = 3

Private Const sheetName = "BP"

Global g_bufPools As BufferPoolDescriptors


Private Sub readSheet()
  Dim thisSection As BufferPoolDescriptor
  
  initBufferPoolDescriptors g_bufPools
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colBufPoolName) & "" <> ""
    With g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools))
      .bufPoolName = Trim(thisSheet.Cells(thisRow, colBufPoolName))
      .shortName = Trim(thisSheet.Cells(thisRow, colShortName))
      .isCommonToOrgs = getBoolean(thisSheet.Cells(thisRow, colIsCommonToOrgs))
      .specificToOrgId = getInteger(thisSheet.Cells(thisRow, colSpecificToOrg))
      .isCommonToPools = .isCommonToOrgs Or getBoolean(thisSheet.Cells(thisRow, colIsCommonToPools))
      .specificToPool = getInteger(thisSheet.Cells(thisRow, colSpecificToPool))
      .isPdmSpecific = getBoolean(thisSheet.Cells(thisRow, colIsPdmSpecific))
      .numBlockPages = getLong(thisSheet.Cells(thisRow, colNumBlockPages))
      .pageSize = thisSheet.Cells(thisRow, colPageSize)
      .numPages = getLong(thisSheet.Cells(thisRow, colSize))
    End With
      
    thisRow = thisRow + 1
  Wend
End Sub


Sub getBufferPools()
  If (g_bufPools.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetBufferPools()
  g_bufPools.numDescriptors = 0
End Sub


Function getBufferPoolIndexByName( _
  ByRef bufPoolName As String _
) As Integer
  Dim i As Integer

  getBufferPoolIndexByName = -1
  getBufferPools

  For i = 1 To g_bufPools.numDescriptors Step 1
    If UCase(g_bufPools.descriptors(i).bufPoolName) = UCase(bufPoolName) Then
      getBufferPoolIndexByName = i
      Exit Function
    End If
  Next i
End Function


Function getBufferPoolDdlBaseFileName( _
  ddlType As DdlTypeId _
) As String
  getBufferPoolDdlBaseFileName = baseName(genDdlFileName(g_targetDir, g_sectionIndexDb, processingStep, ddlType))
End Function


Private Sub genBufferPoolDdl( _
  thisBufPoolIndex As Integer, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional ByVal thisOrgIndex As Integer = -1, _
  Optional ByVal thisPoolIndex As Integer = -1 _
)
  If ddlType <> edtPdm Or thisBufPoolIndex < 0 Then
    Exit Sub
  End If
    
  With g_bufPools.descriptors(thisBufPoolIndex)
    If .isPdmSpecific And ddlType <> edtPdm Then
      Exit Sub
    End If
    
    Dim thisOrgDescriptorStr As String
    thisOrgDescriptorStr = genOrgId(thisOrgIndex, ddlType)
    
    Dim fileNoCr As Integer
    fileNoCr = openDdlFile(g_targetDir, g_sectionIndexDb, processingStep, ddlType, thisOrgIndex, thisPoolIndex)
    
    On Error GoTo ErrorExit
    
    printChapterHeader "Bufferpool """ & .bufPoolName & """", fileNoCr
    Print #fileNoCr, addTab(0); "CREATE BUFFERPOOL"
    Print #fileNoCr, addTab(1); genBufferPoolNameByIndex(.bufPoolIndex, thisOrgIndex, thisPoolIndex)
    Print #fileNoCr, addTab(1); "SIZE "; CStr(.numPages)
    Print #fileNoCr, addTab(1); "PAGESIZE "; CStr(.pageSize)
    If .numBlockPages >= 0 Then
      Print #fileNoCr, addTab(1); "NUMBLOCKPAGES "; CStr(.numBlockPages)
    End If
    Print #fileNoCr, addTab(0); gc_sqlCmdDelim
    Print #fileNoCr,
  End With

NormalExit:
  On Error Resume Next
  Close #fileNoCr
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub genBufferPoolsDdl( _
  ddlType As DdlTypeId _
)
  Dim i As Integer
  Dim thisOrgIndex As Integer
  Dim thisBufPoolIndex As Integer
  Dim thisPoolIndex As Integer
    
  With g_bufPools
    If ddlType = edtLdm Then
      For thisBufPoolIndex = 1 To .numDescriptors Step 1
        genBufferPoolDdl thisBufPoolIndex, edtLdm
      Next thisBufPoolIndex
    ElseIf ddlType = edtPdm Then
      For thisBufPoolIndex = 1 To .numDescriptors Step 1
        With .descriptors(thisBufPoolIndex)
          If .isCommonToOrgs Then
            genBufferPoolDdl thisBufPoolIndex, edtPdm
          Else
            For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
              If .specificToOrgId <= 0 Or .specificToOrgId = g_orgs.descriptors(thisOrgIndex).id Then
                If .isCommonToPools Then
                  genBufferPoolDdl thisBufPoolIndex, edtPdm, thisOrgIndex
                Else
                  For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                    If (.specificToPool <= 0 Or .specificToPool = g_pools.descriptors(thisPoolIndex).id) And _
                        poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                      genBufferPoolDdl thisBufPoolIndex, edtPdm, thisOrgIndex, thisPoolIndex
                    End If
                  Next thisPoolIndex
                End If
              End If
            Next thisOrgIndex
          End If
        End With
      Next thisBufPoolIndex
    End If
  End With
End Sub

