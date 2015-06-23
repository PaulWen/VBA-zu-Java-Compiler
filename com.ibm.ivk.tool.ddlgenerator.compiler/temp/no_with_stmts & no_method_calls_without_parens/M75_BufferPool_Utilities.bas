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

     If bufPools.numDescriptors = 0 Then
       ReDim bufPools.descriptors(1 To gc_allocBlockSize)
     ElseIf bufPools.numDescriptors >= UBound(bufPools.descriptors) Then
       ReDim Preserve bufPools.descriptors(1 To bufPools.numDescriptors + gc_allocBlockSize)
     End If
     bufPools.numDescriptors = bufPools.numDescriptors + 1
     allocBufferPoolDescriptorIndex = bufPools.numDescriptors
 End Function
 
 
 Sub evalBufferPools()
   Dim thisBufPoolIndex As Integer
     For thisBufPoolIndex = 1 To g_bufPools.numDescriptors Step 1
         g_bufPools.descriptors(thisBufPoolIndex).bufPoolIndex = thisBufPoolIndex
     Next thisBufPoolIndex
 End Sub
 
 
