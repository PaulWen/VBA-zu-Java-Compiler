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
  
  With container
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocContainerDescriptorIndex = .numDescriptors
  End With
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
  
  With containerRefs
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocContainerDescriptorRefIndex = .numDescriptors
  End With
End Function

