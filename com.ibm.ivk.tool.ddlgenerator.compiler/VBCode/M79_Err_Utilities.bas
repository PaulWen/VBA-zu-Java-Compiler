Attribute VB_Name = "M79_Err_Utilities"
Option Explicit

Type ErrDescriptor
  id As String
  isTechnical As Boolean
  sqlStateOffset As Integer
  busErrorMessageNo As String
  messagePattern As String
  messageExplanation As String
  conEnumLabelText As String
End Type

Type ErrDescriptors
  descriptors() As ErrDescriptor
  numDescriptors As Integer
End Type


Sub initErrDescriptors( _
  ByRef errs As ErrDescriptors _
)
  errs.numDescriptors = 0
End Sub


Function allocErrDescriptorIndex( _
  ByRef errs As ErrDescriptors _
) As Integer
  allocErrDescriptorIndex = -1
  
  With errs
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocErrDescriptorIndex = .numDescriptors
  End With
End Function


