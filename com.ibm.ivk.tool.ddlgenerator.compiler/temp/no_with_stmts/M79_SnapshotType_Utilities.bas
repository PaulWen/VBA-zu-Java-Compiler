 Attribute VB_Name = "M79_SnapshotType_Utilities"
 Option Explicit
 
 Type SnapshotTypeDescriptor
   procName As String
   className As String
   viewName As String
   sequenceNo As Integer
   sequenceNoCollect As Integer
   category As String
   level As Integer
   isApplSpecific As Boolean
   supportAnalysis As Boolean

   ' derived attributes
   classIndex As Integer
 End Type
 
 Type SnapshotTypeDescriptors
   descriptors() As SnapshotTypeDescriptor
   numDescriptors As Integer
 End Type


 Sub initSnapshotTypeDescriptors( _
   ByRef types As SnapshotTypeDescriptors _
 )
   types.numDescriptors = 0
 End Sub
 
 
 Function allocSnapshotTypeDescriptorIndex( _
   ByRef types As SnapshotTypeDescriptors _
 ) As Integer
   allocSnapshotTypeDescriptorIndex = -1

     If types.numDescriptors = 0 Then
       ReDim types.descriptors(1 To gc_allocBlockSize)
     ElseIf types.numDescriptors >= UBound(types.descriptors) Then
       ReDim Preserve types.descriptors(1 To types.numDescriptors + gc_allocBlockSize)
     End If
     types.numDescriptors = types.numDescriptors + 1
     allocSnapshotTypeDescriptorIndex = types.numDescriptors
 End Function
