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
  
  With cols
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocSnapshotFilterDescriptorIndex = .numDescriptors
  End With
End Function



