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
  
  With plds
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocProfLevelDescriptorIndex = .numDescriptors
  End With
End Function




