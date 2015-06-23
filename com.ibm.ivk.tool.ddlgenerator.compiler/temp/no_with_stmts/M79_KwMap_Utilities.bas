 Attribute VB_Name = "M79_KwMap_Utilities"
 Option Explicit
 
 Type KwMapDescriptor
   keyword As String
   value As String
 End Type
 
 Type KwMapDescriptors
   descriptors() As KwMapDescriptor
   numDescriptors As Integer
 End Type
 
 
 Sub initKwMapDescriptors( _
   ByRef kwMap As KwMapDescriptors _
 )
   kwMap.numDescriptors = 0
 End Sub
 
 
 Function allocKwMapDescriptorIndex( _
   ByRef kwMap As KwMapDescriptors _
 ) As Integer
   allocKwMapDescriptorIndex = -1

     If kwMap.numDescriptors = 0 Then
       ReDim kwMap.descriptors(1 To gc_allocBlockSize)
     ElseIf kwMap.numDescriptors >= UBound(kwMap.descriptors) Then
       ReDim Preserve kwMap.descriptors(1 To kwMap.numDescriptors + gc_allocBlockSize)
     End If
     kwMap.numDescriptors = kwMap.numDescriptors + 1
     allocKwMapDescriptorIndex = kwMap.numDescriptors
 End Function
 
