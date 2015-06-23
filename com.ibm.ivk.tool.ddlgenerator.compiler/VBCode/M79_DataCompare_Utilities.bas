Attribute VB_Name = "M79_DataCompare_Utilities"
' ### IF IVK ###
Option Explicit

Enum DataCompareMode
  dcmKey = 1
  dcmCompare = 2
  dcmNone = 0
End Enum

Type DCompDescriptorRefs
  refs() As Integer
  numRefs As Integer
End Type

Type DCompDescriptor
  checkName As String
  sectionName As String
  entityName As String
  cType As AcmAttrContainerType
  dataPoolId As Integer
  refDataPoolId As Integer
  
  attrName As String
  compareMode As String
  sequenceNo As Integer
  
  ' derived attributes
  attrRef As Integer
End Type

Type DCompDescriptors
  descriptors() As DCompDescriptor
  numDescriptors As Integer
End Type
  

Sub initDCompDescriptors( _
  ByRef dComps As DCompDescriptors _
)
  dComps.numDescriptors = 0
End Sub


Function allocDCompDescriptorIndex( _
  ByRef dComps As DCompDescriptors _
) As Integer
  allocDCompDescriptorIndex = -1
  
  With dComps
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocDCompDescriptorIndex = .numDescriptors
  End With
End Function


Function getDataCompareMode( _
  ByRef str As String _
) As String
  str = UCase(Left(Trim(str & ""), 1))
  getDataCompareMode = IIf((str = "K"), dcmKey, IIf((str = "C"), dcmCompare, dcmNone))
End Function
' ### ENDIF IVK ###

