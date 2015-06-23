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

     If errs.numDescriptors = 0 Then
       ReDim errs.descriptors(1 To gc_allocBlockSize)
     ElseIf errs.numDescriptors >= UBound(errs.descriptors) Then
       ReDim Preserve errs.descriptors(1 To errs.numDescriptors + gc_allocBlockSize)
     End If
     errs.numDescriptors = errs.numDescriptors + 1
     allocErrDescriptorIndex = errs.numDescriptors
 End Function
 
 
