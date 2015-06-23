Attribute VB_Name = "M77_IndexAttr_Utilities"
Option Explicit

Type IndexAttrDescriptorRefs
  refs() As Integer
  numRefs As Integer
End Type

Type IndexAttrDescriptor
  sectionName As String
  className As String
  cType As AcmAttrContainerType
  indexName As String
  attrName As String
  attrIsIncluded As Boolean
  relSectionName As String
  relName As String
  isAsc As Boolean
  
  ' derived attributes
  attrRef As Integer
  relRef As Integer
  relRefDirection As RelNavigationDirection
End Type

Type IndexAttrDescriptors
  descriptors() As IndexAttrDescriptor
  numDescriptors As Integer
End Type
  
  
Sub initIndexAttrDescriptors( _
  ByRef indexes As IndexAttrDescriptors _
)
  indexes.numDescriptors = 0
End Sub


Function allocIndexAttrDescriptorIndex( _
  ByRef indexes As IndexAttrDescriptors _
) As Integer
  allocIndexAttrDescriptorIndex = -1
  
  With indexes
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocIndexAttrDescriptorIndex = .numDescriptors
  End With
End Function


Function allocIndexAttrDescriptorRefIndex( _
  ByRef attrRefs As IndexAttrDescriptorRefs _
) As Integer
  allocIndexAttrDescriptorRefIndex = -1
  
  With attrRefs
    If .numRefs = 0 Then
      ReDim .refs(1 To gc_allocBlockSize)
    ElseIf .numRefs >= UBound(.refs) Then
      ReDim Preserve .refs(1 To .numRefs + gc_allocBlockSize)
    End If
    .numRefs = .numRefs + 1
    allocIndexAttrDescriptorRefIndex = .numRefs
  End With
End Function

