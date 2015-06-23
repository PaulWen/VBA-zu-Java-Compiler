 Attribute VB_Name = "M08_VBA_Profiling_Utilities"
 Option Explicit
 
 Type ProfLevelDescriptor
   moduleName As String
   procName As String
   level As Integer
 End Type
 
 Type ProfLevelDescriptors
   descriptors() As ProfLevelDescriptor
   numDescriptors As Integer
 End Type
 
 
 Function allocProfLevelDescriptorIndex( _
   ByRef plds As ProfLevelDescriptors _
 ) As Integer
   allocProfLevelDescriptorIndex = -1

     If plds.numDescriptors = 0 Then
       ReDim plds.descriptors(1 To gc_allocBlockSize)
     ElseIf plds.numDescriptors >= UBound(plds.descriptors) Then
       ReDim Preserve plds.descriptors(1 To plds.numDescriptors + gc_allocBlockSize)
     End If
     plds.numDescriptors = plds.numDescriptors + 1
     allocProfLevelDescriptorIndex = plds.numDescriptors
 End Function
 
 
 
 
