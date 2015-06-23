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
  
  With kwMap
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocKwMapDescriptorIndex = .numDescriptors
  End With
End Function

