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
       If attributeNls.numDescriptors = 0 Then
         ReDim attributeNls.descriptors(1 To gc_allocBlockSize)
       ElseIf attributeNls.numDescriptors >= UBound(attributeNls.descriptors) Then
         ReDim Preserve attributeNls.descriptors(1 To attributeNls.numDescriptors + gc_allocBlockSize)
       End If
       attributeNls.numDescriptors = attributeNls.numDescriptors + 1
         ReDim attributeNls.descriptors(attributeNls.numDescriptors).nl(1 To numLangsForAttributesNl)
       allocAttributeNlDescriptorIndex = attributeNls.numDescriptors
   End If
 End Function
 
 
