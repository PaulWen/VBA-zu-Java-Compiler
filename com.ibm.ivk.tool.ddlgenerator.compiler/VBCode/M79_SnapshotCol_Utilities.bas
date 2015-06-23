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
  
  With cols
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocSnapshotColDescriptorIndex = .numDescriptors
  End With
End Function


