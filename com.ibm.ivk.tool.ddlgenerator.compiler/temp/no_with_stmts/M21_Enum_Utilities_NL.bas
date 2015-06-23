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
       If enumNls.numDescriptors = 0 Then
         ReDim enumNls.descriptors(1 To gc_allocBlockSize)
       ElseIf enumNls.numDescriptors >= UBound(enumNls.descriptors) Then
         ReDim Preserve enumNls.descriptors(1 To enumNls.numDescriptors + gc_allocBlockSize)
       End If
       enumNls.numDescriptors = enumNls.numDescriptors + 1
         ReDim enumNls.descriptors(enumNls.numDescriptors).nl(1 To numLangsForEnumsNl)
       allocEnumNlDescriptorIndex = enumNls.numDescriptors
   End If
 End Function
 
