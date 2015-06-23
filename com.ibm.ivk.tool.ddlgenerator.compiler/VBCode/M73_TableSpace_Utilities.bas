Attribute VB_Name = "M73_TableSpace_Utilities"
Option Explicit

Enum TabSpaceCategory
  tscSms = 0
  tscDms = 1
End Enum

Type TableSpaceDescriptor
  tableSpaceName As String
  shortName As String
  isCommonToOrgs As Boolean
  specificToOrgId As Integer
  isCommonToPools As Boolean
  specificToPool As Integer
  isPdmSpecific As Boolean
  isMonitor As Boolean
  type As String
  category As TabSpaceCategory
  pageSize As String
  autoResize As Boolean
  increasePercent As Integer
  increaseAbsolute As String
  maxSize As String
  extentSize As String
  prefetchSize As String
  bufferPoolName As String
  overhead As String
  transferrate As String
  useFileSystemCaching As Boolean
  supportDroppedTableRecovery As Boolean

  ' derived attributes
  tableSpaceIndex As Integer
  containerRefs As ContainerDescriptorRefs
  bufferPoolIndex As Integer
End Type

Type TableSpaceDescriptors
  descriptors() As TableSpaceDescriptor
  numDescriptors As Integer
End Type


Sub initTableSpaceDescriptors( _
  ByRef tablespace As TableSpaceDescriptors _
)
  tablespace.numDescriptors = 0
End Sub


Function allocTableSpaceDescriptorIndex( _
  ByRef tablespace As TableSpaceDescriptors _
) As Integer
  allocTableSpaceDescriptorIndex = -1
  
  With tablespace
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    .descriptors(.numDescriptors).containerRefs.numDescriptors = 0
    allocTableSpaceDescriptorIndex = .numDescriptors
  End With
End Function


Function getTabSpaceCategory( _
  str As String _
) As TabSpaceCategory
  str = UCase(Left(Trim(str & ""), 1))
  getTabSpaceCategory = IIf((str = "D"), tscDms, tscSms)
End Function
