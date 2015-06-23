 Attribute VB_Name = "M74_Container_Utilities"
 Option Explicit
 
 Type ContainerDescriptorRefs
   descriptors() As Integer
   numDescriptors As Integer
 End Type

 Enum containerType
   cntFile = 0
   cntDevice = 1
 End Enum
 
 Type ContainerDescriptor
   tableSpaceName As String
   containerName As String
   type As containerType
   isCommonToOrgs As Boolean
   specificToOrgId As Integer
   isCommonToPools As Boolean
   specificToPool As Integer
   isPdmSpecific As Boolean
   size As Long

   ' derived attributes
   containerIndex As Integer
 End Type
 
 Type ContainerDescriptors
   descriptors() As ContainerDescriptor
   numDescriptors As Integer
 End Type
 
 
 Sub initContainerDescriptors( _
   ByRef container As ContainerDescriptors _
 )
   container.numDescriptors = 0
 End Sub
 
 
 Function allocContainerDescriptorIndex( _
   ByRef container As ContainerDescriptors _
 ) As Integer
   allocContainerDescriptorIndex = -1

     If container.numDescriptors = 0 Then
       ReDim container.descriptors(1 To gc_allocBlockSize)
     ElseIf container.numDescriptors >= UBound(container.descriptors) Then
       ReDim Preserve container.descriptors(1 To container.numDescriptors + gc_allocBlockSize)
     End If
     container.numDescriptors = container.numDescriptors + 1
     allocContainerDescriptorIndex = container.numDescriptors
 End Function
 
 
 Function getContainerType( _
   str As String _
 ) As containerType
   str = UCase(Left(Trim(str & ""), 1))
   getContainerType = IIf((str = "D"), cntDevice, cntFile)
 End Function
 
 
 
 Function allocContainerDescriptorRefIndex( _
   ByRef containerRefs As ContainerDescriptorRefs _
 ) As Integer
   allocContainerDescriptorRefIndex = -1

     If containerRefs.numDescriptors = 0 Then
       ReDim containerRefs.descriptors(1 To gc_allocBlockSize)
     ElseIf containerRefs.numDescriptors >= UBound(containerRefs.descriptors) Then
       ReDim Preserve containerRefs.descriptors(1 To containerRefs.numDescriptors + gc_allocBlockSize)
     End If
     containerRefs.numDescriptors = containerRefs.numDescriptors + 1
     allocContainerDescriptorRefIndex = containerRefs.numDescriptors
 End Function
 
