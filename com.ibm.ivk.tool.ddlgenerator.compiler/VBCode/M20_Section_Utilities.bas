Attribute VB_Name = "M20_Section_Utilities"
Option Explicit

Type SectionDescriptor
  sectionName As String
  shortName As String
  seqNo As Integer
  specificToOrgs As String
  specificToPools As String
  
  isTechnical As Boolean
  
  ' derived attributes
  sectionIndex As Integer
  maxRelId As Integer
  
  ' file handles
  fileNoDdl() As Integer
End Type

Type SectionDescriptors
  descriptors() As SectionDescriptor
  numDescriptors As Integer
  maxSeqNo As Integer
End Type
  
  
Sub initSectionDescriptors( _
  ByRef sects As SectionDescriptors _
)
  sects.numDescriptors = 0
  sects.maxSeqNo = 0
End Sub


Function allocSectionDescriptorIndex( _
  ByRef sects As SectionDescriptors _
) As Integer
  allocSectionDescriptorIndex = -1
  
  With sects
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocSectionDescriptorIndex = .numDescriptors
  End With
End Function


Function sectionValidForPoolAndOrg( _
  ByRef sectionIndex As Integer, _
  Optional ByVal thisOrgIndex As Integer = -1, _
  Optional ByVal thisPoolIndex As Integer = -1 _
) As Boolean
  sectionValidForPoolAndOrg = False
  
  With g_sections.descriptors(sectionIndex)
    If .specificToOrgs <> "" Then
      If thisOrgIndex < 1 Then
        sectionValidForPoolAndOrg = Not listHasPostiveElement(.specificToOrgs)
        Exit Function
      ElseIf Not includedInList(.specificToOrgs, g_orgs.descriptors(thisOrgIndex).id) Then
        Exit Function
      End If
    End If
    If .specificToPools <> "" Then
      If thisPoolIndex < 1 Then
        sectionValidForPoolAndOrg = Not listHasPostiveElement(.specificToPools)
        Exit Function
      ElseIf Not includedInList(.specificToPools, g_pools.descriptors(thisPoolIndex).id) Then
        Exit Function
      End If
    End If
  End With
  
  sectionValidForPoolAndOrg = True
End Function

