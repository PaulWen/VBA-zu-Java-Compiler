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

     If indexes.numDescriptors = 0 Then
       ReDim indexes.descriptors(1 To gc_allocBlockSize)
     ElseIf indexes.numDescriptors >= UBound(indexes.descriptors) Then
       ReDim Preserve indexes.descriptors(1 To indexes.numDescriptors + gc_allocBlockSize)
     End If
     indexes.numDescriptors = indexes.numDescriptors + 1
     allocIndexAttrDescriptorIndex = indexes.numDescriptors
 End Function
 
 
 Function allocIndexAttrDescriptorRefIndex( _
   ByRef attrRefs As IndexAttrDescriptorRefs _
 ) As Integer
   allocIndexAttrDescriptorRefIndex = -1

     If attrRefs.numRefs = 0 Then
       ReDim attrRefs.refs(1 To gc_allocBlockSize)
     ElseIf attrRefs.numRefs >= UBound(attrRefs.refs) Then
       ReDim Preserve attrRefs.refs(1 To attrRefs.numRefs + gc_allocBlockSize)
     End If
     attrRefs.numRefs = attrRefs.numRefs + 1
     allocIndexAttrDescriptorRefIndex = attrRefs.numRefs
 End Function
 
