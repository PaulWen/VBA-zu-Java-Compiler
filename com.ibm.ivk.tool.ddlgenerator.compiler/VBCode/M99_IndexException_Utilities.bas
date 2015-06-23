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
  
  With indexes
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocIndexExcpDescriptorIndex = .numDescriptors
  End With
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
    With g_indexExcp.descriptors(i)
'    test = "VL6C" & .sectionShortName & genOrgIdByIndex(thisOrgIndex, ddlType) & .noIndexInPool & "." & .indexName
     If ("VL6C" & .sectionShortName & genOrgIdByIndex(thisOrgIndex, ddlType) & .noIndexInPool & "." & .indexName) = qualIndexName Then
        indexExcp = True
        Exit Function
      End If
    End With
  Next i
End Function



