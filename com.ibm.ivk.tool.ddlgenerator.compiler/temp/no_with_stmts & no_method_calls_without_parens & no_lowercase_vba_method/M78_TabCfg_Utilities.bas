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

     If tabCfgs.numDescriptors = 0 Then
       ReDim tabCfgs.descriptors(1 To gc_allocBlockSize)
     ElseIf tabCfgs.numDescriptors >= UBound(tabCfgs.descriptors) Then
       ReDim Preserve tabCfgs.descriptors(1 To tabCfgs.numDescriptors + gc_allocBlockSize)
     End If
     tabCfgs.numDescriptors = tabCfgs.numDescriptors + 1
     allocTabCfgParamDescriptorIndex = tabCfgs.numDescriptors
 End Function
 
