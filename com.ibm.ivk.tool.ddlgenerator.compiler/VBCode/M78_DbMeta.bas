Attribute VB_Name = "M78_DbMeta"
Option Explicit

Type LdmFk
  srcSchema As String
  srcTable As String
  dstSchema As String
  dstTable As String
  isEnforced As Boolean
End Type

Type CheckFk
  srcQualTableName As String
  dstQualTableName As String
  srcAttrSeq As String
  isEnforced As Boolean
End Type

Type LdmFks
  numFks As Integer
  fks() As LdmFk
End Type

Type CheckFks
  numFks As Integer
  fks() As CheckFk
End Type

Global g_ldmFks As LdmFks

Global g_checkFks As CheckFks

Private Const pdmCsvProcessingStep = 3

Private fileNoCsvLdmTable As Integer
Private fileNoCsvPdmTable As Integer


Sub closeCsvFilesLPdmTable()
  On Error Resume Next
  Close fileNoCsvLdmTable
  Close fileNoCsvPdmTable

  fileNoCsvLdmTable = -1
  fileNoCsvPdmTable = -1
End Sub


Sub initGLdmFks()
  initLdmFks g_ldmFks
  initCheckFks g_checkFks
End Sub


Private Sub initLdmFks( _
  ByRef fks As LdmFks _
)
  fks.numFks = 0
End Sub

Private Sub initCheckFks( _
  ByRef fks As CheckFks _
)
  fks.numFks = 0
End Sub


Function allocLdmFkIndex( _
  ByRef fks As LdmFks _
) As Integer
  allocLdmFkIndex = -1
  
  With fks
    If .numFks = 0 Then
      ReDim .fks(1 To gc_allocBlockSize)
    ElseIf .numFks >= UBound(.fks) Then
      ReDim Preserve .fks(1 To .numFks + gc_allocBlockSize)
    End If
    .numFks = .numFks + 1
    allocLdmFkIndex = .numFks
  End With
End Function

Function allocCheckFkIndex( _
  ByRef fks As CheckFks _
) As Integer
  allocCheckFkIndex = -1
  
  With fks
    If .numFks = 0 Then
      ReDim .fks(1 To gc_allocBlockSize)
    ElseIf .numFks >= UBound(.fks) Then
      ReDim Preserve .fks(1 To .numFks + gc_allocBlockSize)
    End If
    .numFks = .numFks + 1
    allocCheckFkIndex = .numFks
  End With
End Function


Private Sub addLdmFk( _
  ByRef srcSchema As String, _
  ByRef srcTable As String, _
  ByRef dstSchema As String, _
  ByRef dstTable As String, _
  Optional ByRef isEnforced As Boolean = True _
)
  Dim i As Integer
  With g_ldmFks
    For i = 1 To .numFks
      With .fks(i)
        If .srcSchema = srcSchema And .srcTable = srcTable And .dstSchema = dstSchema And .dstTable = dstTable Then
          Exit Sub
        End If
      End With
    Next i
    With .fks(allocLdmFkIndex(g_ldmFks))
      .srcSchema = srcSchema
      .srcTable = srcTable
      .dstSchema = dstSchema
      .dstTable = dstTable
      .isEnforced = isEnforced
    End With
  End With
End Sub

Private Sub addCheckFk( _
  ByRef srcQualTableName As String, _
  ByRef dstQualTableName As String, _
  ByRef srcAttrSeq As String, _
  Optional ByRef isEnforced As Boolean = True _
)
  Dim i As Integer
  With g_checkFks
    For i = 1 To .numFks
      With .fks(i)
        If .srcQualTableName = srcQualTableName And .dstQualTableName = dstQualTableName And .srcAttrSeq = srcAttrSeq Then
          Exit Sub
        End If
      End With
    Next i
    With .fks(allocCheckFkIndex(g_checkFks))
      .srcQualTableName = srcQualTableName
      .dstQualTableName = dstQualTableName
      .srcAttrSeq = srcAttrSeq
      .isEnforced = isEnforced
    End With
  End With
End Sub
Sub registerCheckFk( _
  ByRef srcQualTableName As String, _
  ByRef dstQualTableName As String, _
  ByRef srcAttrSeq As String, _
  Optional ByRef isEnforced As Boolean = True _
)
  addCheckFk srcQualTableName, dstQualTableName, srcAttrSeq, isEnforced
End Sub

Private Sub registerLdmFk( _
  ByRef srcSchemaName As String, _
  ByRef srcTabName As String, _
  ByRef dstSchemaName As String, _
  ByRef dstTabName As String, _
  ByRef srcAcmEntityIndex As Integer, _
  ByRef srcAcmEntityType As AcmAttrContainerType, _
  Optional dstNotAcmRelated As Boolean = False, _
  Optional forGen As Boolean = False, _
  Optional ByRef isEnforced As Boolean = True _
)
  addLdmFk srcSchemaName, srcTabName, dstSchemaName, dstTabName, isEnforced
End Sub


Private Sub genLdmFksCsv( _
  Optional ddlType As DdlTypeId = edtLdm _
)
  Dim fileNo As Integer
  Dim fileName As String
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnFkDependency, ldmCsvFkProcessingStep, "LDM", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  
  On Error GoTo ErrorExit
  Open fileName For Append As #fileNo
  
  Dim i As Integer
  For i = 1 To g_ldmFks.numFks
    With g_ldmFks.fks(i)
      Print #fileNo, IIf(.isEnforced, gc_dbTrue, gc_dbFalse); ",";
      Print #fileNo, """"; UCase(Trim(.srcTable)); """,";
      Print #fileNo, """"; UCase(Trim(.srcSchema)); """,";
      Print #fileNo, """"; UCase(Trim(.dstTable)); """,";
      Print #fileNo, """"; UCase(Trim(.dstSchema)); """,";
      Print #fileNo, getCsvTrailer(0)
    End With
  Next i
  
NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub genLdmFksCsvs()
  If generateLdm Then
    genLdmFksCsv edtLdm
  End If

  If generatePdm Then
    genLdmFksCsv edtPdm
  End If
End Sub


Sub registerQualLdmFk( _
  ByRef qualLdmSrcTableName As String, _
  ByRef qualLdmDstTableName As String, _
  ByRef srcAcmEntityIndex As Integer, _
  ByRef srcAcmEntityType As AcmAttrContainerType, _
  Optional dstNotAcmRelated As Boolean = False, _
  Optional forGen As Boolean = False, _
  Optional isEnforced As Boolean = True _
)
  If dstNotAcmRelated Then
    Exit Sub
  End If
  
  If srcAcmEntityType = eactClass Then
    With g_classes.descriptors(srcAcmEntityIndex)
      If .notAcmRelated Then
        Exit Sub
      End If
    End With
  ElseIf srcAcmEntityType = eactRelationship Then
    With g_relationships.descriptors(srcAcmEntityIndex)
      If .notAcmRelated Or .isNotEnforced Then
        Exit Sub
      End If
    End With
  ElseIf srcAcmEntityType = eactEnum Then
    With g_enums.descriptors(srcAcmEntityIndex)
      If .notAcmRelated Then
        Exit Sub
      End If
    End With
  End If
  
  Dim srcSchemaName As String
  Dim srcTabName As String
  Dim dstSchemaName As String
  Dim dstTabName As String

  splitQualifiedName qualLdmSrcTableName, srcSchemaName, srcTabName
  splitQualifiedName qualLdmDstTableName, dstSchemaName, dstTabName
  registerLdmFk srcSchemaName, srcTabName, dstSchemaName, dstTabName, srcAcmEntityIndex, srcAcmEntityType, dstNotAcmRelated, forGen, isEnforced
End Sub


Private Sub registerLdmTable( _
  ByRef clnLdmSchema As String, _
  ByRef ldmTabName As String, _
  ByRef rootAcmEntityIndex As Integer, _
  ByRef acmEntityIndex As Integer, _
  ByRef acmEntityType As AcmAttrContainerType, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional isGen As Boolean = False, _
  Optional isLrt As Boolean = False, _
  Optional isNl As Boolean = False, _
  Optional isMqt As Boolean = False _
)
  Dim acmSectionName As String
  Dim acmEntityName As String
    
  If acmEntityType = eactClass Then
    With g_classes.descriptors(rootAcmEntityIndex)
      If (Not isLrt And .isLdmCsvExported) Or (isLrt And .isLdmLrtCsvExported) Or .notAcmRelated Then
        Exit Sub
      End If
    End With
    With g_classes.descriptors(acmEntityIndex)
      acmSectionName = .sectionName
      acmEntityName = .className
    End With
  ElseIf acmEntityType = eactEnum Then
    With g_enums.descriptors(rootAcmEntityIndex)
      If .isLdmCsvExported Or .notAcmRelated Then
        Exit Sub
      End If
    End With
    With g_enums.descriptors(acmEntityIndex)
      acmSectionName = .sectionName
      acmEntityName = .enumName
    End With
  ElseIf acmEntityType = eactRelationship Then
    With g_relationships.descriptors(rootAcmEntityIndex)
      If (Not isLrt And .isLdmCsvExported) Or (isLrt And .isLdmLrtCsvExported) Or .notAcmRelated Then
        Exit Sub
      End If
    End With
    With g_relationships.descriptors(acmEntityIndex)
      acmSectionName = .sectionName
      acmEntityName = .relName
    End With
  End If

  On Error GoTo ErrorExit
  
  If fileNoCsvLdmTable < 1 Then
    Dim fileName As String
    fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnLdmTable, ldmCsvTableProcessingStep, "LDM", ddlType)
    assertDir fileName
    fileNoCsvLdmTable = FreeFile()
  
    Open fileName For Append As #fileNoCsvLdmTable
  End If
  
  Print #fileNoCsvLdmTable, """"; UCase(Trim(ldmTabName)); """,";
  Print #fileNoCsvLdmTable, ",";
  Print #fileNoCsvLdmTable, IIf(isNl, gc_dbTrue, gc_dbFalse); ",";
  Print #fileNoCsvLdmTable, IIf(isGen, gc_dbTrue, gc_dbFalse); ",";
  Print #fileNoCsvLdmTable, IIf(isLrt, gc_dbTrue, gc_dbFalse); ",";
  Print #fileNoCsvLdmTable, IIf(isMqt, gc_dbTrue, gc_dbFalse); ",";
  Print #fileNoCsvLdmTable, """"; UCase(Trim(acmSectionName)); """,";
  Print #fileNoCsvLdmTable, """"; UCase(Trim(acmEntityName)); """,";
  Print #fileNoCsvLdmTable, """"; getAcmEntityTypeKey(acmEntityType); """,";
  Print #fileNoCsvLdmTable, """"; UCase(Trim(clnLdmSchema)); """,";
  Print #fileNoCsvLdmTable, getCsvTrailer(0)
  
NormalExit:
  On Error Resume Next
  ' leave file open
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub splitQualifiedName( _
  ByRef qualifiedName As String, _
  ByRef qualifier As String, _
  ByRef unqualifiedName As String _
)
  Dim elems() As String
  elems = split(qualifiedName, ".", 2)
  
  qualifier = ""
  unqualifiedName = ""
  If UBound(elems) = 1 Then
    qualifier = elems(0)
    unqualifiedName = elems(1)
  End If
End Sub


Private Sub registerPdmTable( _
  ByRef qualRefObjNamePdm As String, _
  ByRef qualRefObjNameLdm As String, _
  ByVal thisOrgIndex As Integer, _
  ByVal thisPoolIndex As Integer _
)
  Dim qualifierLdm As String
  Dim nameLdm As String
  Dim qualifierPdm As String
  Dim namePdm As String
  
  splitQualifiedName qualRefObjNameLdm, qualifierLdm, nameLdm
  splitQualifiedName qualRefObjNamePdm, qualifierPdm, namePdm
  
  If getOrgIsTemplate(thisOrgIndex) Then
    ' we do not create CSV for template Orgs
    Exit Sub
  End If
  
  On Error GoTo ErrorExit
  
  If fileNoCsvPdmTable < 1 Then
    Dim fileNameCsv As String
    fileNameCsv = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnPdmTable, pdmCsvProcessingStep, "PDM", edtPdm, , , , , thisOrgIndex)
    assertDir fileNameCsv
  
    fileNoCsvPdmTable = FreeFile()
    Open fileNameCsv For Append As #fileNoCsvPdmTable
  End If
  
  Dim orgIdStr As String
  If thisOrgIndex <= 0 Then
    orgIdStr = ""
  Else
    With g_orgs.descriptors(thisOrgIndex)
      If .isTemplate Then
        orgIdStr = genTemplateParamWrapper(CStr(.id))
      Else
        orgIdStr = CStr(.id)
      End If
    End With
  End If
  
  Print #fileNoCsvPdmTable, """"; namePdm; """,";
  Print #fileNoCsvPdmTable, orgIdStr; ",";
  If thisPoolIndex > 0 Then
    Print #fileNoCsvPdmTable, CStr(g_pools.descriptors(thisPoolIndex).id); ",";
  Else
    Print #fileNoCsvPdmTable, ",";
  End If
  Print #fileNoCsvPdmTable, """"; nameLdm; """,";
  Print #fileNoCsvPdmTable, """"; qualifierLdm; """,";
  Print #fileNoCsvPdmTable, """"; qualifierPdm; """,";
  Print #fileNoCsvPdmTable, getCsvTrailer(0)

NormalExit:
  On Error Resume Next
  ' leave file open
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub registerQualTable( _
  ByRef qualLdmTableName As String, _
  ByRef qualPdmTableName As String, _
  ByRef rootAcmEntityIndex As Integer, _
  ByRef acmEntityIndex As Integer, _
  ByRef acmEntityType As AcmAttrContainerType, _
  ByVal thisOrgIndex As Integer, _
  ByVal thisPoolIndex As Integer, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional ByRef notAcmRelated As Boolean, _
  Optional isGen As Boolean = False, _
  Optional isLrt As Boolean = False, _
  Optional isNl As Boolean = False, _
  Optional isMqt As Boolean = False _
)
  Dim ldmSchemaName As String
  Dim ldmTableName As String

  splitQualifiedName qualLdmTableName, ldmSchemaName, ldmTableName
  registerLdmTable ldmSchemaName, ldmTableName, rootAcmEntityIndex, acmEntityIndex, acmEntityType, ddlType, isGen, isLrt, isNl, isMqt
  
  If ddlType = edtPdm And Not notAcmRelated Then
    registerPdmTable qualPdmTableName, qualLdmTableName, thisOrgIndex, thisPoolIndex
  End If
End Sub



