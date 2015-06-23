Attribute VB_Name = "M17_FwkTest"
' ### IF IVK ###
Option Explicit

Private Const processingStep = 2


Sub genFwkTestDdlForOrg( _
  Optional ByVal thisOrgIndex As Integer = -1, _
  Optional ddlType As DdlTypeId = edtPdm _
)
  Dim thisOrgId As Integer
  If thisOrgIndex > 0 Then thisOrgId = g_orgs.descriptors(thisOrgIndex).id Else thisOrgId = -1
  
  If ddlType <> edtPdm Then
    Exit Sub
  End If
  
  On Error GoTo ErrorExit
  
  With g_sections.descriptors(g_sectionIndexFwkTest)
    If .specificToOrgs <> "" And Not includedInList(.specificToOrgs, thisOrgId) Then
      Exit Sub
    End If
  End With
  
  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexFwkTest, processingStep, ddlType, thisOrgIndex)

  genFwkTestOidSequenceForOrg thisOrgIndex, fileNo, ddlType

NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub genFwkTestDdlForPool( _
  Optional ByVal thisOrgIndex As Integer = -1, _
  Optional ByVal thisPoolIndex As Integer = -1, _
  Optional ddlType As DdlTypeId = edtPdm _
)
  If ddlType <> edtPdm Then
    Exit Sub
  End If
  
  On Error GoTo ErrorExit
  
  With g_sections.descriptors(g_sectionIndexFwkTest)
    If .specificToOrgs <> "" Then
      If thisOrgIndex < 1 Then
        Exit Sub
      ElseIf .specificToOrgs <> "" And Not includedInList(.specificToOrgs, g_orgs.descriptors(thisOrgIndex).id) Then
        Exit Sub
      End If
    End If
  End With
  
  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexFwkTest, processingStep, ddlType, thisOrgIndex, thisPoolIndex)

  Dim qualProcName As String
  qualProcName = genQualProcName(g_sectionIndexAliasLrt, "Exc_Test", ddlType, thisOrgIndex, thisPoolIndex)

  printSectionHeader "SP for Testing Business Exceptions", fileNo
  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE PROCEDURE"
  Print #fileNo, addTab(1); qualProcName
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being archived"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "RESULT SETS 0"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "BEGIN"

  Print #fileNo,
  Print #fileNo, addTab(1); "SET rowCount_out = 17;"
  Print #fileNo,
  Print #fileNo, addTab(1); "SIGNAL SQLSTATE '79133' SET MESSAGE_TEXT = '[MDS]: 1300005;"; genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, 2, 1); "';"

  Print #fileNo, addTab(0); "END"
  Print #fileNo, addTab(0); gc_sqlCmdDelim

NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Function genFwkTestOidSequenceNameForOrg( _
  ByVal thisOrgIndex As Integer, _
  Optional ddlType As DdlTypeId = edtPdm _
) As String
  genFwkTestOidSequenceNameForOrg = genQualObjName(g_sectionIndexFwkTest, gc_seqNameOid, gc_seqNameOid, ddlType, thisOrgIndex)
End Function


Sub genFwkTestOidSequenceForOrg( _
  ByVal thisOrgIndex As Integer, _
  fileNo As Integer, _
  Optional ddlType As DdlTypeId = edtLdm _
)
  With g_orgs.descriptors(thisOrgIndex)
    genSequence "Sequence for Generating Object IDs for FwkTest / MPC """ & .name & """", genFwkTestOidSequenceNameForOrg(.id, ddlType), 0, fileNo, "00000000000000000"
  End With
End Sub

Sub genFwkTestDdl( _
  ddlType As DdlTypeId _
)
  Dim thisOrgIndex As Integer
  Dim thisPoolIndex As Integer
  
  If Not generateFwkTest Then
    Exit Sub
  End If
  
  If ddlType = edtPdm Then
    For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
      genFwkTestDdlForOrg thisOrgIndex, edtPdm
      For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
        If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
          genFwkTestDdlForPool thisOrgIndex, thisPoolIndex, edtPdm
        End If
      Next thisPoolIndex
    Next thisOrgIndex
  End If
End Sub
' ### ENDIF IVK ###

