Attribute VB_Name = "M21_Enum_Utilities_NL"
Option Explicit

Type EnumNlDescriptor
  i18nId As String
  
  nl() As String

  ' derived attributes
  enumIndex As String
End Type

Type EnumNlDescriptors
  descriptors() As EnumNlDescriptor
  numDescriptors As Integer
End Type
  
Function allocEnumNlDescriptorIndex( _
  ByRef enumNls As EnumNlDescriptors _
) As Integer
  allocEnumNlDescriptorIndex = -1
  
  If numLangsForEnumsNl > 0 Then
    With enumNls
      If .numDescriptors = 0 Then
        ReDim .descriptors(1 To gc_allocBlockSize)
      ElseIf .numDescriptors >= UBound(.descriptors) Then
        ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
      End If
      .numDescriptors = .numDescriptors + 1
      With .descriptors(.numDescriptors)
        ReDim .nl(1 To numLangsForEnumsNl)
      End With
      allocEnumNlDescriptorIndex = .numDescriptors
    End With
  End If
End Function

