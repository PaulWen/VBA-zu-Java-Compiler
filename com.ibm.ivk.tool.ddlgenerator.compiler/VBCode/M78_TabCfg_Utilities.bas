Attribute VB_Name = "M78_TabCfg_Utilities"
Option Explicit

Type TabCfgParamDescriptor
  sequenceNumber As Integer
  schemaPattern As String
  namePattern As String
  schemaPatternExcluded As String
  namePatternExcluded As String
  pctFree As Integer
  isVolatile As TvBoolean
  useRowCompression As TvBoolean
  useIndexCompression As TvBoolean
End Type

Type TabCfgParamDescriptors
  descriptors() As TabCfgParamDescriptor
  numDescriptors As Integer
End Type
  
  
Sub initTabCfgParamDescriptors( _
  ByRef tabCfgs As TabCfgParamDescriptors _
)
  tabCfgs.numDescriptors = 0
End Sub


Function allocTabCfgParamDescriptorIndex( _
  ByRef tabCfgs As TabCfgParamDescriptors _
) As Integer
  allocTabCfgParamDescriptorIndex = -1
  
  With tabCfgs
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocTabCfgParamDescriptorIndex = .numDescriptors
  End With
End Function

