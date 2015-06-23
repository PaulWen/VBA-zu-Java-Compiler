 Attribute VB_Name = "M99_IndexException_Utilities"
 Option Explicit
 
 Type IndexExcpDescriptor
   sectionName As String
   sectionShortName As String
   indexName As String
   noIndexInPool As String
 End Type
 
 Type IndexExcpDescriptors
   descriptors() As IndexExcpDescriptor
   numDescriptors As Integer
 End Type


 Sub initIndexExcpDescriptors( _
   ByRef indexes As IndexExcpDescriptors _
 )
   indexes.numDescriptors = 0
 End Sub
 
 
 
 Function allocIndexExcpDescriptorIndex( _
   ByRef indexes As IndexExcpDescriptors _
 ) As Integer
   allocIndexExcpDescriptorIndex = -1

     If indexes.numDescriptors = 0 Then
       ReDim indexes.descriptors(1 To gc_allocBlockSize)
     ElseIf indexes.numDescriptors >= UBound(indexes.descriptors) Then
       ReDim Preserve indexes.descriptors(1 To indexes.numDescriptors + gc_allocBlockSize)
     End If
     indexes.numDescriptors = indexes.numDescriptors + 1
     allocIndexExcpDescriptorIndex = indexes.numDescriptors
 End Function
 
 
 
 Function indexExcp( _
   ByVal qualIndexName As String, _
   ByVal thisOrgIndex As Integer, _
   Optional ddlType As DdlTypeId _
 ) As Boolean
   indexExcp = False
 
   Dim i As Integer
 '  Dim test As String
   For i = 1 To g_indexExcp.numDescriptors Step 1
 '    test = "VL6C" & .sectionShortName & genOrgIdByIndex(thisOrgIndex, ddlType) & .noIndexInPool & "." & .indexName
      If ("VL6C" & g_indexExcp.descriptors(i).sectionShortName & genOrgIdByIndex(thisOrgIndex, ddlType) & g_indexExcp.descriptors(i).noIndexInPool & "." & g_indexExcp.descriptors(i).indexName) = qualIndexName Then
         indexExcp = True
         Exit Function
       End If
   Next i
 End Function
 
 
 
