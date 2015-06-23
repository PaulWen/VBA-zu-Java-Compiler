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

     If indexes.numDescriptors = 0 Then
       ReDim indexes.descriptors(1 To gc_allocBlockSize)
     ElseIf indexes.numDescriptors >= UBound(indexes.descriptors) Then
       ReDim Preserve indexes.descriptors(1 To indexes.numDescriptors + gc_allocBlockSize)
     End If
     indexes.numDescriptors = indexes.numDescriptors + 1
     allocDbCfgProfileDescriptorIndex = indexes.numDescriptors
 End Function
 
