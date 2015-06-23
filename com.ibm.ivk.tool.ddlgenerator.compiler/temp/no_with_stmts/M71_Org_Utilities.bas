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

     If orgs.numDescriptors = 0 Then
       ReDim orgs.descriptors(1 To gc_allocBlockSize)
     ElseIf orgs.numDescriptors >= UBound(orgs.descriptors) Then
       ReDim Preserve orgs.descriptors(1 To orgs.numDescriptors + gc_allocBlockSize)
     End If
     orgs.numDescriptors = orgs.numDescriptors + 1

       orgs.descriptors(orgs.numDescriptors).oidSequenceCount = 1
     allocOrgIndex = orgs.numDescriptors
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
       If g_orgs.descriptors(i).isPrimary Then
         getPrimaryOrgId = g_orgs.descriptors(i).id
         Exit Function
       End If
   Next i
 End Function
 
 
 Function getPrimaryOrgIndex() As Integer
   getPrimaryOrgIndex = -1

   Dim i As Integer
   For i = 1 To g_orgs.numDescriptors
       If g_orgs.descriptors(i).isPrimary Then
         getPrimaryOrgIndex = i
         Exit Function
       End If
   Next i
 End Function
 
 
 Function getMinOrgId() As Integer
   Dim result As Integer

   result = -1
   getMinOrgId = -1

   Dim i As Integer
   For i = 1 To g_orgs.numDescriptors
       If Not g_orgs.descriptors(i).isTemplate And (result < 0 Or result > g_orgs.descriptors(i).id) Then
         result = g_orgs.descriptors(i).id
         getMinOrgId = g_orgs.descriptors(i).id
       End If
   Next i
 End Function
 
 
 Function getMaxOrgId() As Integer
   Dim result As Integer

   result = -1
   getMaxOrgId = -1

   Dim i As Integer
   For i = 1 To g_orgs.numDescriptors
       If Not g_orgs.descriptors(i).isTemplate And (result < 0 Or result < g_orgs.descriptors(i).id) Then
         result = g_orgs.descriptors(i).id
         getMaxOrgId = g_orgs.descriptors(i).id
       End If
   Next i
 End Function
 
 
 Function pullOrgOidByIndex( _
   ByVal thisOrgIndex As Integer _
 ) As Integer
   pullOrgOidByIndex = -1

   If thisOrgIndex >= 1 And thisOrgIndex <= g_orgs.numDescriptors Then
       pullOrgOidByIndex = g_orgs.descriptors(thisOrgIndex).oidSequenceCount
       g_orgs.descriptors(thisOrgIndex).oidSequenceCount = g_orgs.descriptors(thisOrgIndex).oidSequenceCount + 1
   End If
 End Function
 
