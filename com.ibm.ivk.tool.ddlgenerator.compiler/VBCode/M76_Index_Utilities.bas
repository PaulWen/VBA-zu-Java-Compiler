Attribute VB_Name = "M76_Index_Utilities"
Option Explicit

Type IndexDescriptorRefs
  refs() As Integer
  numRefs As Integer
End Type

Type IndexDescriptor
  sectionName As String
  className As String
  cType As AcmAttrContainerType
  indexName As String
  shortName As String
  isUnique As Boolean
  forGen As Boolean
  specificToQueryTables As Boolean
  specificToPools As String

  ' derived attributes
  sectionIndex As Integer
  attrRefs As IndexAttrDescriptorRefs
End Type

Type IndexDescriptors
  descriptors() As IndexDescriptor
  numDescriptors As Integer
End Type
  
  
Sub initIndexDescriptors( _
  ByRef indexes As IndexDescriptors _
)
  indexes.numDescriptors = 0
End Sub


Function allocIndexDescriptorIndex( _
  ByRef indexes As IndexDescriptors _
) As Integer
  allocIndexDescriptorIndex = -1
  
  With indexes
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    .descriptors(.numDescriptors).attrRefs.numRefs = 0
    allocIndexDescriptorIndex = .numDescriptors
  End With
End Function


Function allocIndexDescriptorRefIndex( _
  ByRef indexRefs As IndexDescriptorRefs _
) As Integer
  allocIndexDescriptorRefIndex = -1
  
  With indexRefs
    If .numRefs = 0 Then
      ReDim .refs(1 To gc_allocBlockSize)
    ElseIf .numRefs >= UBound(.refs) Then
      ReDim Preserve .refs(1 To .numRefs + gc_allocBlockSize)
    End If
    .numRefs = .numRefs + 1
    allocIndexDescriptorRefIndex = .numRefs
  End With
End Function

