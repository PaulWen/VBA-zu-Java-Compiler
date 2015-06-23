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
    
  With perms
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocPrivilegeDescriptorIndex = .numDescriptors
  End With
End Function


Sub evalPrivileges()
  Dim i As Integer, j As Integer
  With g_privileges
    For i = 1 To .numDescriptors Step 1
      With .descriptors(i)
        If .withGrantOption And UCase(.objectType) <> "SCHEMA" Then
          logMsg "privileges on object """ & .objectName & """ (" & .objectType & ") WITH GRANT OPTION not supported - fixed", ellFixableWarning
          .withGrantOption = False
        End If
      End With
    Next i
  End With
End Sub
