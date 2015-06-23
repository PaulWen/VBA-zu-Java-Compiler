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

   initBufferPoolDescriptors(g_bufPools)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colBufPoolName) & "" <> ""
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).bufPoolName = Trim(thisSheet.Cells(thisRow, colBufPoolName))
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).shortName = Trim(thisSheet.Cells(thisRow, colShortName))
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).isCommonToOrgs = getBoolean(thisSheet.Cells(thisRow, colIsCommonToOrgs))
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).specificToOrgId = getInteger(thisSheet.Cells(thisRow, colSpecificToOrg))
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).isCommonToPools = g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).isCommonToOrgs Or getBoolean(thisSheet.Cells(thisRow, colIsCommonToPools))
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).specificToPool = getInteger(thisSheet.Cells(thisRow, colSpecificToPool))
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).isPdmSpecific = getBoolean(thisSheet.Cells(thisRow, colIsPdmSpecific))
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).numBlockPages = getLong(thisSheet.Cells(thisRow, colNumBlockPages))
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).pageSize = thisSheet.Cells(thisRow, colPageSize)
       g_bufPools.descriptors(allocBufferPoolDescriptorIndex(g_bufPools)).numPages = getLong(thisSheet.Cells(thisRow, colSize))

     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getBufferPools()
   If (g_bufPools.numDescriptors = 0) Then
     readSheet()
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
   getBufferPools()
 
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

     If g_bufPools.descriptors(thisBufPoolIndex).isPdmSpecific And ddlType <> edtPdm Then
       Exit Sub
     End If

     Dim thisOrgDescriptorStr As String
     thisOrgDescriptorStr = genOrgId(thisOrgIndex, ddlType)

     Dim fileNoCr As Integer
     fileNoCr = openDdlFile(g_targetDir, g_sectionIndexDb, processingStep, ddlType, thisOrgIndex, thisPoolIndex)

     On Error GoTo ErrorExit

     printChapterHeader("Bufferpool """ & g_bufPools.descriptors(thisBufPoolIndex).bufPoolName & """", fileNoCr)
     Print #fileNoCr, addTab(0); "CREATE BUFFERPOOL"
     Print #fileNoCr, addTab(1); genBufferPoolNameByIndex(g_bufPools.descriptors(thisBufPoolIndex).bufPoolIndex, thisOrgIndex, thisPoolIndex)
     Print #fileNoCr, addTab(1); "SIZE "; CStr(g_bufPools.descriptors(thisBufPoolIndex).numPages)
     Print #fileNoCr, addTab(1); "PAGESIZE "; CStr(g_bufPools.descriptors(thisBufPoolIndex).pageSize)
     If g_bufPools.descriptors(thisBufPoolIndex).numBlockPages >= 0 Then
       Print #fileNoCr, addTab(1); "NUMBLOCKPAGES "; CStr(g_bufPools.descriptors(thisBufPoolIndex).numBlockPages)
     End If
     Print #fileNoCr, addTab(0); gc_sqlCmdDelim
     Print #fileNoCr,
 
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

     If ddlType = edtLdm Then
       For thisBufPoolIndex = 1 To g_bufPools.numDescriptors Step 1
         genBufferPoolDdl(thisBufPoolIndex, edtLdm)
       Next thisBufPoolIndex
     ElseIf ddlType = edtPdm Then
       For thisBufPoolIndex = 1 To g_bufPools.numDescriptors Step 1
           If g_bufPools.descriptors(thisBufPoolIndex).isCommonToOrgs Then
             genBufferPoolDdl(thisBufPoolIndex, edtPdm)
           Else
             For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
               If g_bufPools.descriptors(thisBufPoolIndex).specificToOrgId <= 0 Or g_bufPools.descriptors(thisBufPoolIndex).specificToOrgId = g_orgs.descriptors(thisOrgIndex).id Then
                 If g_bufPools.descriptors(thisBufPoolIndex).isCommonToPools Then
                   genBufferPoolDdl(thisBufPoolIndex, edtPdm, thisOrgIndex)
                 Else
                   For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                     If (g_bufPools.descriptors(thisBufPoolIndex).specificToPool <= 0 Or g_bufPools.descriptors(thisBufPoolIndex).specificToPool = g_pools.descriptors(thisPoolIndex).id) And _
                         poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                       genBufferPoolDdl(thisBufPoolIndex, edtPdm, thisOrgIndex, thisPoolIndex)
                     End If
                   Next thisPoolIndex
                 End If
               End If
             Next thisOrgIndex
           End If
       Next thisBufPoolIndex
     End If
 End Sub
 
