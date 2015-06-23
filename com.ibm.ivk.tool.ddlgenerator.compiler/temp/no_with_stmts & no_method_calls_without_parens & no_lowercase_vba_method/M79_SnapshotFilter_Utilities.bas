 Attribute VB_Name = "M79_SnapshotFilter_Utilities"
 Option Explicit
 
 Type SnapshotFilterDescriptor
   tabName As String
   level As Integer
   collectFilter As String
   selectFilter As String
 End Type
 
 Type SnapshotFilterDescriptors
   descriptors() As SnapshotFilterDescriptor
   numDescriptors As Integer
 End Type

 
 Sub initSnapshotFilterDescriptors( _
   ByRef cols As SnapshotFilterDescriptors _
 )
   cols.numDescriptors = 0
 End Sub
 
 
 Function allocSnapshotFilterDescriptorIndex( _
   ByRef cols As SnapshotFilterDescriptors _
 ) As Integer
   allocSnapshotFilterDescriptorIndex = -1

     If cols.numDescriptors = 0 Then
       ReDim cols.descriptors(1 To gc_allocBlockSize)
     ElseIf cols.numDescriptors >= UBound(cols.descriptors) Then
       ReDim Preserve cols.descriptors(1 To cols.numDescriptors + gc_allocBlockSize)
     End If
     cols.numDescriptors = cols.numDescriptors + 1
     allocSnapshotFilterDescriptorIndex = cols.numDescriptors
 End Function
 
 
 
