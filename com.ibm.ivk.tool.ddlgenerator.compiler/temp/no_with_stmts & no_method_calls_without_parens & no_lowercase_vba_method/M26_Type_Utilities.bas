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

     If types.numDescriptors = 0 Then
       ReDim types.descriptors(1 To gc_allocBlockSize)
     ElseIf types.numDescriptors >= UBound(types.descriptors) Then
       ReDim Preserve types.descriptors(1 To types.numDescriptors + gc_allocBlockSize)
     End If
     types.numDescriptors = types.numDescriptors + 1
     allocTypeDescriptorIndex = types.numDescriptors
 End Function
 ' ### ENDIF IVK ###
 
