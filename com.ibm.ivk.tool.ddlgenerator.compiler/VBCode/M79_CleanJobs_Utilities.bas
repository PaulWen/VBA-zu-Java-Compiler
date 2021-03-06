Attribute VB_Name = "M79_CleanJobs_Utilities"
Option Explicit

Type CleanJobDescriptor
  jobCategory As String
  jobName As String
  level As String
  sequenceNo As String
  tableSchema As String
  tableName As String
  tableRef As String
  condition As String
  commitCount As Long
End Type

Type CleanJobDescriptors
  descriptors() As CleanJobDescriptor
  numDescriptors As Integer
End Type
  

Sub initCleanJobDescriptors( _
  ByRef jobs As CleanJobDescriptors _
)
  jobs.numDescriptors = 0
End Sub


Function allocCleanJobDescriptorIndex( _
  ByRef jobs As CleanJobDescriptors _
) As Integer
  allocCleanJobDescriptorIndex = -1
  
  With jobs
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocCleanJobDescriptorIndex = .numDescriptors
  End With
End Function

