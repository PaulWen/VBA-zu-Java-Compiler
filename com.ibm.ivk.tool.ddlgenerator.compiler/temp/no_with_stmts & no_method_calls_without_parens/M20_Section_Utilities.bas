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

     If sects.numDescriptors = 0 Then
       ReDim sects.descriptors(1 To gc_allocBlockSize)
     ElseIf sects.numDescriptors >= UBound(sects.descriptors) Then
       ReDim Preserve sects.descriptors(1 To sects.numDescriptors + gc_allocBlockSize)
     End If
     sects.numDescriptors = sects.numDescriptors + 1
     allocSectionDescriptorIndex = sects.numDescriptors
 End Function
 
 
 Function sectionValidForPoolAndOrg( _
   ByRef sectionIndex As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 ) As Boolean
   sectionValidForPoolAndOrg = False

     If g_sections.descriptors(sectionIndex).specificToOrgs <> "" Then
       If thisOrgIndex < 1 Then
         sectionValidForPoolAndOrg = Not listHasPostiveElement(g_sections.descriptors(sectionIndex).specificToOrgs)
         Exit Function
       ElseIf Not includedInList(g_sections.descriptors(sectionIndex).specificToOrgs, g_orgs.descriptors(thisOrgIndex).id) Then
         Exit Function
       End If
     End If
     If g_sections.descriptors(sectionIndex).specificToPools <> "" Then
       If thisPoolIndex < 1 Then
         sectionValidForPoolAndOrg = Not listHasPostiveElement(g_sections.descriptors(sectionIndex).specificToPools)
         Exit Function
       ElseIf Not includedInList(g_sections.descriptors(sectionIndex).specificToPools, g_pools.descriptors(thisPoolIndex).id) Then
         Exit Function
       End If
     End If

   sectionValidForPoolAndOrg = True
 End Function
 
