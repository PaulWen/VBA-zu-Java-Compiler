Attribute VB_Name = "M78_DbCfg_Utilities"
Option Explicit

Type DbCfgParamDescriptor
  parameter As String
  value As String
  isDbmCfgParam As Boolean
  isDbProfileParam As Boolean
  serverPlatform As String
  sequenceNo As Integer
  minDbRelease As String
End Type

Type DbCfgParamDescriptors
  descriptors() As DbCfgParamDescriptor
  numDescriptors As Integer
End Type
  
  
Sub initDbCfgParamDescriptors( _
  ByRef dbCfgs As DbCfgParamDescriptors _
)
  dbCfgs.numDescriptors = 0
End Sub


Function allocDbCfgParamDescriptorIndex( _
  ByRef dbCfgs As DbCfgParamDescriptors _
) As Integer
  allocDbCfgParamDescriptorIndex = -1
  
  With dbCfgs
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocDbCfgParamDescriptorIndex = .numDescriptors
  End With
End Function

