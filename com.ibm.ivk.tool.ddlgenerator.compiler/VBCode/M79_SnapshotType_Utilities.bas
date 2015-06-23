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
  
  With types
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocSnapshotTypeDescriptorIndex = .numDescriptors
  End With
End Function
