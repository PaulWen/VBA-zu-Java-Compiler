Attribute VB_Name = "M24_Attribute_Utilities_NL"
Option Explicit

Type AttributeNlDescriptor
  i18nId As String
  
  nl() As String

  ' derived attributes
  attributeIndex As String
End Type

Type AttributeNlDescriptors
  descriptors() As AttributeNlDescriptor
  numDescriptors As Integer
End Type
  
  


Function allocAttributeNlDescriptorIndex( _
  ByRef attributeNls As AttributeNlDescriptors _
) As Integer
  allocAttributeNlDescriptorIndex = -1
  
  If numLangsForAttributesNl > 0 Then
    With attributeNls
      If .numDescriptors = 0 Then
        ReDim .descriptors(1 To gc_allocBlockSize)
      ElseIf .numDescriptors >= UBound(.descriptors) Then
        ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
      End If
      .numDescriptors = .numDescriptors + 1
      With .descriptors(.numDescriptors)
        ReDim .nl(1 To numLangsForAttributesNl)
      End With
      allocAttributeNlDescriptorIndex = .numDescriptors
    End With
  End If
End Function


