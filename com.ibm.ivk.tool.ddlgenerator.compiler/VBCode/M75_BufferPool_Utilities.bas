Attribute VB_Name = "M75_BufferPool_Utilities"
Option Explicit

Type BufferPoolDescriptor
  bufPoolName As String
  shortName As String
  isCommonToOrgs As Boolean
  specificToOrgId As Integer
  isCommonToPools As Boolean
  specificToPool As Integer
  isPdmSpecific As Boolean
  numBlockPages As Long
  pageSize As String
  numPages As Long

  ' derived attributes
  bufPoolIndex As Integer
End Type

Type BufferPoolDescriptors
  descriptors() As BufferPoolDescriptor
  numDescriptors As Integer
End Type


Sub initBufferPoolDescriptors( _
  ByRef bufPools As BufferPoolDescriptors _
)
  bufPools.numDescriptors = 0
End Sub


Function allocBufferPoolDescriptorIndex( _
  ByRef bufPools As BufferPoolDescriptors _
) As Integer
  allocBufferPoolDescriptorIndex = -1
  
  With bufPools
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocBufferPoolDescriptorIndex = .numDescriptors
  End With
End Function


Sub evalBufferPools()
  Dim thisBufPoolIndex As Integer
  With g_bufPools
    For thisBufPoolIndex = 1 To .numDescriptors Step 1
      With .descriptors(thisBufPoolIndex)
        .bufPoolIndex = thisBufPoolIndex
      End With
    Next thisBufPoolIndex
  End With
End Sub


