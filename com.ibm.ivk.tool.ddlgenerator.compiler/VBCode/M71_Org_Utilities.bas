Attribute VB_Name = "M71_Org_Utilities"
Option Explicit

Type OrgDescriptor
  id As Integer
  name As String
  isPrimary As Boolean
  oid As Integer
  sequenceCacheSize As Integer
  isTemplate As Boolean
  
  oidSequenceCount As Integer

  ' derived attributes
  setProductiveTargetPoolId As Integer
  setProductiveTargetPoolIndex As Integer
End Type

Type OrgDescriptors
  descriptors() As OrgDescriptor
  numDescriptors As Integer
End Type


Sub initOrgDescriptors( _
  ByRef orgs As OrgDescriptors _
)
  orgs.numDescriptors = 0
End Sub


Function allocOrgIndex( _
  ByRef orgs As OrgDescriptors _
) As Integer
  allocOrgIndex = -1
  
  With orgs
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    
    With .descriptors(.numDescriptors)
      .oidSequenceCount = 1
    End With
    allocOrgIndex = .numDescriptors
  End With
End Function


Function getEffectiveOrgId( _
  thisOrgId As Integer, _
  isCommon As Boolean _
) As Integer
  getEffectiveOrgId = IIf(isCommon, -1, thisOrgId)
End Function


Function getEffectiveOrgIndex( _
  ByVal thisOrgIndex As Integer, _
  isCommon As Boolean _
) As Integer
  getEffectiveOrgIndex = IIf(isCommon, -1, thisOrgIndex)
End Function

Function getPrimaryOrgId() As Integer
  getPrimaryOrgId = -1
  
  Dim i As Integer
  For i = 1 To g_orgs.numDescriptors
    With g_orgs.descriptors(i)
      If .isPrimary Then
        getPrimaryOrgId = .id
        Exit Function
      End If
    End With
  Next i
End Function


Function getPrimaryOrgIndex() As Integer
  getPrimaryOrgIndex = -1
  
  Dim i As Integer
  For i = 1 To g_orgs.numDescriptors
    With g_orgs.descriptors(i)
      If .isPrimary Then
        getPrimaryOrgIndex = i
        Exit Function
      End If
    End With
  Next i
End Function


Function getMinOrgId() As Integer
  Dim result As Integer
  
  result = -1
  getMinOrgId = -1
  
  Dim i As Integer
  For i = 1 To g_orgs.numDescriptors
    With g_orgs.descriptors(i)
      If Not .isTemplate And (result < 0 Or result > .id) Then
        result = .id
        getMinOrgId = .id
      End If
    End With
  Next i
End Function


Function getMaxOrgId() As Integer
  Dim result As Integer
  
  result = -1
  getMaxOrgId = -1
  
  Dim i As Integer
  For i = 1 To g_orgs.numDescriptors
    With g_orgs.descriptors(i)
      If Not .isTemplate And (result < 0 Or result < .id) Then
        result = .id
        getMaxOrgId = .id
      End If
    End With
  Next i
End Function


Function pullOrgOidByIndex( _
  ByVal thisOrgIndex As Integer _
) As Integer
  pullOrgOidByIndex = -1
  
  If thisOrgIndex >= 1 And thisOrgIndex <= g_orgs.numDescriptors Then
    With g_orgs.descriptors(thisOrgIndex)
      pullOrgOidByIndex = .oidSequenceCount
      .oidSequenceCount = .oidSequenceCount + 1
    End With
  End If
End Function

