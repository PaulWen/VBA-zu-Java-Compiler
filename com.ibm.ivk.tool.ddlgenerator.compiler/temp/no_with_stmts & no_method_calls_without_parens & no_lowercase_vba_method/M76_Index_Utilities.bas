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

     If indexes.numDescriptors = 0 Then
       ReDim indexes.descriptors(1 To gc_allocBlockSize)
     ElseIf indexes.numDescriptors >= UBound(indexes.descriptors) Then
       ReDim Preserve indexes.descriptors(1 To indexes.numDescriptors + gc_allocBlockSize)
     End If
     indexes.numDescriptors = indexes.numDescriptors + 1
     indexes.descriptors(indexes.numDescriptors).attrRefs.numRefs = 0
     allocIndexDescriptorIndex = indexes.numDescriptors
 End Function
 
 
 Function allocIndexDescriptorRefIndex( _
   ByRef indexRefs As IndexDescriptorRefs _
 ) As Integer
   allocIndexDescriptorRefIndex = -1

     If indexRefs.numRefs = 0 Then
       ReDim indexRefs.refs(1 To gc_allocBlockSize)
     ElseIf indexRefs.numRefs >= UBound(indexRefs.refs) Then
       ReDim Preserve indexRefs.refs(1 To indexRefs.numRefs + gc_allocBlockSize)
     End If
     indexRefs.numRefs = indexRefs.numRefs + 1
     allocIndexDescriptorRefIndex = indexRefs.numRefs
 End Function
 
