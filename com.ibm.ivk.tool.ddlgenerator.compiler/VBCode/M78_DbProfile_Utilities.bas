Attribute VB_Name = "M78_DbProfile_Utilities"
Option Explicit

Type DbCfgProfileDescriptor
  profileName As String
  objectType As String
  schemaName As String
  objectName As String
  sequenceNo As Integer
  configParameter As String
  configValue As String
  serverPlatform As String
  minDbRelease As String
End Type

Type DbCfgProfileDescriptors
  descriptors() As DbCfgProfileDescriptor
  numDescriptors As Integer
End Type
  
  
Sub initDbCfgProfileDescriptors( _
  ByRef indexes As DbCfgProfileDescriptors _
)
  indexes.numDescriptors = 0
End Sub


Function allocDbCfgProfileDescriptorIndex( _
  ByRef indexes As DbCfgProfileDescriptors _
) As Integer
  allocDbCfgProfileDescriptorIndex = -1
  
  With indexes
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocDbCfgProfileDescriptorIndex = .numDescriptors
  End With
End Function

