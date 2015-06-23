 Attribute VB_Name = "M79_SnapshotCol_Utilities"
 Option Explicit
 
 Type SnapshotColDescriptor
   tabName As String
   colName As String
   colAlias As String
   displayFunction As String
   columnExpression As String
   sequenceNo As Integer
   category As String
   level As Integer
 End Type
 
 Type SnapshotColDescriptors
   descriptors() As SnapshotColDescriptor
   numDescriptors As Integer
 End Type


 Sub initSnapshotColDescriptors( _
   ByRef cols As SnapshotColDescriptors _
 )
   cols.numDescriptors = 0
 End Sub
 
 
 Function allocSnapshotColDescriptorIndex( _
   ByRef cols As SnapshotColDescriptors _
 ) As Integer
   allocSnapshotColDescriptorIndex = -1

     If cols.numDescriptors = 0 Then
       ReDim cols.descriptors(1 To gc_allocBlockSize)
     ElseIf cols.numDescriptors >= UBound(cols.descriptors) Then
       ReDim Preserve cols.descriptors(1 To cols.numDescriptors + gc_allocBlockSize)
     End If
     cols.numDescriptors = cols.numDescriptors + 1
     allocSnapshotColDescriptorIndex = cols.numDescriptors
 End Function
 
 
