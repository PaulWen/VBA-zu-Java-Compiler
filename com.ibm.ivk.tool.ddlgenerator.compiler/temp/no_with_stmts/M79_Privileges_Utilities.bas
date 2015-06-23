 Attribute VB_Name = "M79_Privileges_Utilities"
 Option Explicit
 
 Type PrivilegeDescriptor
   sequenceNumber As Integer
   environment As String
   operation As String
   objectType As String
   schemaName As String
   objectName As String
   filter As String
   granteeType As String
   grantee As String
   privilege As String
   withGrantOption As Boolean
 End Type
 
 Type PrivilegeDescriptors
   descriptors() As PrivilegeDescriptor
   numDescriptors As Integer
 End Type


 Sub initPrivilegeDescriptors( _
   ByRef perms As PrivilegeDescriptors _
 )
   perms.numDescriptors = 0
 End Sub
 
 
 Function allocPrivilegeDescriptorIndex( _
   ByRef perms As PrivilegeDescriptors _
 ) As Integer
   allocPrivilegeDescriptorIndex = -1

     If perms.numDescriptors = 0 Then
       ReDim perms.descriptors(1 To gc_allocBlockSize)
     ElseIf perms.numDescriptors >= UBound(perms.descriptors) Then
       ReDim Preserve perms.descriptors(1 To perms.numDescriptors + gc_allocBlockSize)
     End If
     perms.numDescriptors = perms.numDescriptors + 1
     allocPrivilegeDescriptorIndex = perms.numDescriptors
 End Function
 
 
 Sub evalPrivileges()
   Dim i As Integer, j As Integer
     For i = 1 To g_privileges.numDescriptors Step 1
         If g_privileges.descriptors(i).withGrantOption And UCase(g_privileges.descriptors(i).objectType) <> "SCHEMA" Then
           logMsg "privileges on object """ & g_privileges.descriptors(i).objectName & """ (" & g_privileges.descriptors(i).objectType & ") WITH GRANT OPTION not supported - fixed", ellFixableWarning
           g_privileges.descriptors(i).withGrantOption = False
         End If
     Next i
 End Sub
