Attribute VB_Name = "M26_Type_Utilities"
' ### IF IVK ###
Option Explicit

Type TypeDescriptor
  sectionName As String
  typeName As String
  shortName As String
  comment As String

  ' derived attributes
  typeIndex As Integer
  sectionIndex As Integer
  attrRefs As AttrDescriptorRefs
End Type

Type TypeDescriptors
  descriptors() As TypeDescriptor
  numDescriptors As Integer
End Type


Sub initTypeDescriptors( _
  ByRef types As TypeDescriptors _
)
  types.numDescriptors = 0
End Sub


Function allocTypeDescriptorIndex( _
  ByRef types As TypeDescriptors _
) As Integer
  allocTypeDescriptorIndex = -1
  
  With types
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocTypeDescriptorIndex = .numDescriptors
  End With
End Function
' ### ENDIF IVK ###

