Attribute VB_Name = "M71_Org"
Option Explicit

Private Const colOrg = 2
Private Const colName = colOrg + 1
Private Const colIsPrimary = colName + 1
Private Const colIsTemplate = colIsPrimary + 1
Private Const colOid = colIsTemplate + 1
Private Const colSequenceCacheSize = colOid + 1

Private Const firstRow = 3

Private Const sheetName = "Org"

Private Const processingStep = 3

Global g_orgs As OrgDescriptors


Private Sub readSheet()
  Dim thisOrgId As Integer
  
  initOrgDescriptors g_orgs
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colOrg) & "" <> ""
    thisOrgId = CInt(thisSheet.Cells(thisRow, colOrg))
    
    With g_orgs.descriptors(allocOrgIndex(g_orgs))
      .id = thisOrgId
      .name = Trim(thisSheet.Cells(thisRow, colName))
      .isPrimary = getBoolean(thisSheet.Cells(thisRow, colIsPrimary))
      .isTemplate = getBoolean(thisSheet.Cells(thisRow, colIsTemplate))
      .oid = getInteger(thisSheet.Cells(thisRow, colOid))
      .sequenceCacheSize = getInteger(thisSheet.Cells(thisRow, colSequenceCacheSize), -1)
      
      If Not genTemplateDdl And .isTemplate Then
        g_orgs.numDescriptors = g_orgs.numDescriptors - 1
      End If
    End With
    thisRow = thisRow + 1
  Wend
End Sub


Sub getOrgs()
  If g_orgs.numDescriptors = 0 Then
    readSheet
  End If
End Sub


Sub resetOrgs()
  g_orgs.numDescriptors = 0
End Sub

Function getOrgIndexById( _
  ByRef thisOrgId As Integer _
) As Integer
  Dim i As Integer

  getOrgIndexById = -1
  getOrgs

  For i = 1 To g_orgs.numDescriptors Step 1
    If g_orgs.descriptors(i).id = thisOrgId Then
      getOrgIndexById = i
      Exit Function
    End If
  Next i
End Function


Function getOrgNameById( _
  thisOrgId As Integer _
) As String
  getOrgNameById = ""
  Dim orgIndex As Integer
  orgIndex = getOrgIndexById(thisOrgId)
  If (orgIndex > 0) Then getOrgNameById = g_orgs.descriptors(orgIndex).name
End Function


Function getOrgNameByIndex( _
  ByVal thisOrgIndex As Integer _
) As String
  getOrgNameByIndex = ""
  If (thisOrgIndex > 0) Then getOrgNameByIndex = g_orgs.descriptors(thisOrgIndex).name
End Function


Function getOrgIsTemplate( _
  ByVal thisOrgIndex As Integer _
) As Boolean
  getOrgIsTemplate = False
  If (thisOrgIndex > 0) Then getOrgIsTemplate = g_orgs.descriptors(thisOrgIndex).isTemplate
End Function


Sub genOrgDdl( _
  Optional ByVal thisOrgIndex As Integer = -1, _
  Optional ddlType As DdlTypeId = edtLdm _
)
  On Error GoTo ErrorExit
  
  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexDb, processingStep, ddlType, thisOrgIndex)

  If ddlType = edtLdm Then
    genSequence "Common Sequence for Object IDs", UCase(snMeta) & "." & UCase(gc_seqNameOid), 1, fileNo
  Else
    genOidSequenceForOrg thisOrgIndex, fileNo, ddlType
  End If

' ### IF IVK ###
  If supportGroupIdColumns Then
    Dim thisOrgId As Integer
    thisOrgId = -1
    Dim orgIsTemplate As Boolean
    If thisOrgIndex > 0 Then
      With g_orgs.descriptors(thisOrgIndex)
        thisOrgId = .id
        orgIsTemplate = .isTemplate
      End With
    End If
    
    Dim qualSeqNameGroupId As String
    Dim lastQualSeqNameGroupId As String
    lastQualSeqNameGroupId = ""
    Dim attrShortName As String
    Dim i As Integer
    For i = 1 To g_attributes.numDescriptors
    With g_attributes.descriptors(i)
      If .groupIdBasedOn <> "" Then
        qualSeqNameGroupId = ""
        attrShortName = g_attributes.descriptors(i).shortName
        If .cType = eactClass Then
          With g_classes.descriptors(g_classes.descriptors(.acmEntityIndex).orMappingSuperClassIndex)
            If .specificToOrgId > 0 And thisOrgId <> .specificToOrgId Then
              GoTo NextI
            End If
            qualSeqNameGroupId = _
              genQualObjName( _
                .sectionIndex, "SEQ_" & .shortName & attrShortName, "SEQ_" & .shortName & attrShortName, ddlType, thisOrgIndex _
              )
          End With
        Else
          ' we currently do not need this
        End If

        If qualSeqNameGroupId <> "" And lastQualSeqNameGroupId <> qualSeqNameGroupId Then
          genSequence _
            "Sequence for Group IDs for Column """ & .attributeName & "@" & .className & """", qualSeqNameGroupId, thisOrgIndex, _
            fileNo, gc_sequenceMinValue, , , , , "1", orgIsTemplate
          lastQualSeqNameGroupId = qualSeqNameGroupId
        End If
      End If
    End With
    GoTo NextI
  errMsgBox (Err.description)

NextI:
    Next i
  End If

' ### ENDIF IVK ###
NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub genOrgsDdl( _
  ddlType As DdlTypeId _
)
  Dim thisOrgIndex As Integer
  
  If ddlType = edtLdm Then
    genOrgDdl , edtLdm
  ElseIf ddlType = edtPdm Then
    For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
      genOrgDdl thisOrgIndex, edtPdm
    Next thisOrgIndex
  End If
End Sub


Function genQualOidSeqNameForOrg( _
  ByVal thisOrgIndex As Integer, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional forOrgIndex As Integer = -1, _
  Optional ByVal thisPoolIndex As Integer = -1, _
  Optional ByRef sectionIndex As Integer = -1, _
  Optional ByRef name As String = gc_seqNameOid _
) As String
  If sectionIndex < 0 Then
    sectionIndex = g_sectionIndexMeta
  End If
  
  genQualOidSeqNameForOrg = genQualObjName(sectionIndex, name, name, ddlType, _
    thisOrgIndex, thisPoolIndex, , , , , , IIf(forOrgIndex > 0, genOrgId(forOrgIndex, ddlType), ""))
End Function


Sub genOidSequenceForOrg( _
  ByVal thisOrgIndex As Integer, _
  fileNo As Integer, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional forOrgIndex As Integer = -1 _
)
  Dim thisPoolIndex As Integer
  Dim orgSeqCacheSize As Integer
  Dim poolSeqCacheSize As Integer
  Dim orgIsTemplate As Boolean
  Dim thisOrgId As Integer
  Dim forOrgId As Integer
  
  Dim isCtoSequence As Boolean
  If thisOrgIndex < 1 Then
    orgSeqCacheSize = g_orgs.descriptors(g_primaryOrgIndex).sequenceCacheSize
    isCtoSequence = True
    orgIsTemplate = False
    thisOrgId = -1
  Else
    With g_orgs.descriptors(thisOrgIndex)
      orgSeqCacheSize = .sequenceCacheSize
      isCtoSequence = .isPrimary And (thisOrgIndex < 1)
      orgIsTemplate = .isTemplate
      thisOrgId = .id
    End With
  End If
  
  If isCtoSequence Then
    genSequence "Sequence for Generating CTO-Object IDs", genQualOidSeqNameForOrg(thisOrgIndex, ddlType), 0, fileNo, _
                "9" & Right(gc_sequenceMinValue, Len(gc_sequenceMinValue) - 1), "9" & Right(gc_sequenceMinValue, Len(gc_sequenceMinValue) - 1)
    genSequence "Sequence for Synchronization of VDF/XML-Export Jobs", genQualObjName(g_sectionIndexMeta, "RunningNMB", "RunningNMB", ddlType, thisOrgIndex), 0, fileNo, _
                "0", "0", , , , 1
    Exit Sub
  End If
  
  Dim forOrgIsTemplate As Boolean
  forOrgIsTemplate = False
  If forOrgIndex > 0 Then
    With g_orgs.descriptors(forOrgIndex)
      forOrgIsTemplate = .isTemplate
      forOrgId = .id
    End With
  Else
    forOrgId = -1
  End If
  If Not forOrgIsTemplate Then
    For thisPoolIndex = 1 To g_pools.numDescriptors
      With g_pools.descriptors(thisPoolIndex)
        poolSeqCacheSize = .sequenceCacheSize
        If (((.specificToOrgId = -1) Or (.specificToOrgId = thisOrgId)) And .commonItemsLocal) Then
          genSequence _
            "Sequence for Generating Object IDs for Org """ & g_orgs.descriptors(thisOrgIndex).name & """ (MIG)", _
            genQualOidSeqNameForOrg(thisOrgIndex, ddlType, forOrgIndex, thisPoolIndex), _
            IIf(forOrgIndex >= 0, forOrgId, 0), fileNo, , gc_sequenceStartValue, _
            IIf(forOrgIndex >= 0, gc_sequenceEndValue, "8" & Right(gc_sequenceEndValue, Len(gc_sequenceEndValue) - 1)), _
            (orgSeqCacheSize > 1) Or (poolSeqCacheSize > 1), _
            IIf(orgSeqCacheSize > poolSeqCacheSize, orgSeqCacheSize, poolSeqCacheSize), , orgIsTemplate
        End If
      End With
    Next thisPoolIndex
  
    With g_orgs.descriptors(thisOrgIndex)
      genSequence _
        "Sequence for Generating Object IDs for Org """ & _
        IIf(.isTemplate, genTemplateParamWrapper(.name), .name) & """", _
        genQualOidSeqNameForOrg(thisOrgIndex, ddlType, forOrgIndex), _
        IIf(forOrgIndex >= 0, forOrgId, thisOrgId), fileNo, , , , (orgSeqCacheSize > 1), orgSeqCacheSize, , orgIsTemplate
    End With
  End If
End Sub


Sub genSequence( _
  ByRef comment As String, _
  ByRef qualSeqName As String, _
  seqNo As Integer, _
  fileNo As Integer, _
  Optional ByRef startValue As String = gc_sequenceStartValue, _
  Optional ByRef minValue As String = gc_sequenceMinValue, _
  Optional ByRef maxValue As String = gc_sequenceEndValue, _
  Optional useCaching As Boolean = True, _
  Optional cacheSize As Integer = 500, _
  Optional ByRef increment As String = CStr(gc_sequenceIncrementValue), _
  Optional forTemplate As Boolean = False _
)
  If Not generateDdlCreateSeq Then
    Exit Sub
  End If
  
  Dim seqNoStr As String
  If forTemplate Then
    seqNoStr = IIf(seqNo < 0, "", genTemplateParamWrapper(CStr(seqNo)) & "")
  Else
    seqNoStr = IIf(seqNo < 0, "", seqNo & "")
  End If
  
  printSectionHeader comment, fileNo
  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE SEQUENCE"
  Print #fileNo, addTab(1); qualSeqName; " AS "; g_dbtSequence
  Print #fileNo, addTab(0); "START WITH"
  Print #fileNo, addTab(1); seqNoStr; startValue
  Print #fileNo, addTab(0); "INCREMENT BY"
  Print #fileNo, addTab(1); increment
  Print #fileNo, addTab(0); "MINVALUE"
  Print #fileNo, addTab(1); seqNoStr; minValue
  Print #fileNo, addTab(0); "MAXVALUE"
  Print #fileNo, addTab(1); seqNoStr; maxValue
  Print #fileNo, addTab(0); "NO CYCLE"
  If useCaching Then
    Print #fileNo, addTab(0); "CACHE "; CStr(cacheSize)
  Else
    Print #fileNo, addTab(0); "NO CACHE"
  End If
  Print #fileNo, addTab(0); gc_sqlCmdDelim
End Sub


Sub dropOrgsDdl( _
  Optional onlyIfEmpty As Boolean = False _
)
  On Error Resume Next
  If generateLdm Then
    killFile genDdlFileName(g_targetDir, g_sectionIndexDb, processingStep, edtLdm), onlyIfEmpty
  End If
  
  If generatePdm Then
    Dim thisOrgIndex As Integer
    For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
      killFile genDdlFileName(g_targetDir, snDb, processingStep, edtPdm, thisOrgIndex), onlyIfEmpty
    Next thisOrgIndex
  End If

NormalExit:
End Sub


' ### IF IVK ###
Sub evalOrgs()
  Dim i As Integer
  For i = 1 To g_orgs.numDescriptors Step 1
    With g_orgs.descriptors(i)
      .setProductiveTargetPoolId = g_productiveDataPoolId
      .setProductiveTargetPoolIndex = getDataPoolIndexById(.setProductiveTargetPoolId)
    End With
  Next i
End Sub
' ### ENDIF IVK ###

