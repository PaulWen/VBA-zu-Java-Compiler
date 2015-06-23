Attribute VB_Name = "M20_Section"
Option Explicit

Private Const colEntryFilter = 1
Private Const colSection = 2
Private Const colShortName = colSection + 1
Private Const colSeqNo = colShortName + 1
Private Const colSpecificToOrgs = colSeqNo + 1
Private Const colSpecificToPool = colSpecificToOrgs + 1
Private Const colJavaPackage = colSpecificToPool + 1
Private Const colJavaParentPackage = colJavaPackage + 1

Private Const firstRow = 3

Private Const sheetName = "Sect"

Private Const ldmSchemaCsvProcessingStep = 1
Private Const acmSchemaCsvProcessingStep = 1
Private Const pdmCsvProcessingStep = 2

Global g_sections As SectionDescriptors


Private Sub readSheet()
  initSectionDescriptors g_sections
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  g_sections.maxSeqNo = -1
  
  While thisSheet.Cells(thisRow, colSection) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
      GoTo NextRow
    End If
    
    With g_sections.descriptors(allocSectionDescriptorIndex(g_sections))
      .sectionName = Trim(thisSheet.Cells(thisRow, colSection))
      .shortName = Trim(thisSheet.Cells(thisRow, colShortName))
      .seqNo = getInteger(thisSheet.Cells(thisRow, colSeqNo))
      .specificToOrgs = Trim(thisSheet.Cells(thisRow, colSpecificToOrgs))
      .specificToPools = Trim(thisSheet.Cells(thisRow, colSpecificToPool))
      
      If UCase(.sectionName) = "FWKTEST" And Not generateFwkTest Then
        .specificToOrgs = CStr(100) 'this will never be a valid MPC
        .specificToPools = CStr(100)
      End If
      g_sections.maxSeqNo = IIf(.seqNo > g_sections.maxSeqNo, .seqNo, g_sections.maxSeqNo)
    End With

NextRow:
    thisRow = thisRow + 1
  Wend

  ' add some technical sections
  With g_sections.descriptors(allocSectionDescriptorIndex(g_sections))
    .sectionName = snAlias
    .shortName = ssnAlias
    .seqNo = g_sections.maxSeqNo + 1
    g_sections.maxSeqNo = .seqNo
    .isTechnical = True
  End With
  With g_sections.descriptors(allocSectionDescriptorIndex(g_sections))
    .sectionName = snAliasDelObj
    .shortName = ssnAliasDelObj
    .seqNo = g_sections.maxSeqNo + 1
    g_sections.maxSeqNo = .seqNo
    .isTechnical = True
  End With
  With g_sections.descriptors(allocSectionDescriptorIndex(g_sections))
    .sectionName = snAliasLrt
    .shortName = ssnAliasLrt
    .seqNo = g_sections.maxSeqNo + 1
    g_sections.maxSeqNo = .seqNo
    .isTechnical = True
  End With
  With g_sections.descriptors(allocSectionDescriptorIndex(g_sections))
    .sectionName = snAliasPsDpFiltered
    .shortName = ssnAliasPsDpFiltered
    .seqNo = g_sections.maxSeqNo + 1
    g_sections.maxSeqNo = .seqNo
    .isTechnical = True
  End With
  With g_sections.descriptors(allocSectionDescriptorIndex(g_sections))
    .sectionName = snAliasPsDpFilteredExtended
    .shortName = ssnAliasPsDpFilteredExtended
    .seqNo = g_sections.maxSeqNo + 1
    g_sections.maxSeqNo = .seqNo
    .isTechnical = True
  End With
  With g_sections.descriptors(allocSectionDescriptorIndex(g_sections))
    .sectionName = snAliasPrivateOnly
    .shortName = ssnAliasPrivateOnly
    .seqNo = g_sections.maxSeqNo + 1
    g_sections.maxSeqNo = .seqNo
    .isTechnical = True
  End With
  With g_sections.descriptors(allocSectionDescriptorIndex(g_sections))
    .sectionName = snHelp
    .shortName = ssnHelp
    .seqNo = g_sections.maxSeqNo + 1
    g_sections.maxSeqNo = .seqNo
    .isTechnical = True
  End With
End Sub


Function getSections() As SectionDescriptors
  If (g_sections.numDescriptors = 0) Then
    readSheet
  End If
End Function

Sub resetSections()
  g_sections.numDescriptors = 0
End Sub


Function getSectionIndexByName( _
  sectName As String, _
  Optional ByVal silent As Boolean _
) As Integer
  Dim i As Integer

  getSectionIndexByName = -1
  getSections

  For i = 1 To g_sections.numDescriptors Step 1
    If UCase(g_sections.descriptors(i).sectionName) = UCase(sectName) Then
      getSectionIndexByName = i
      Exit Function
    End If
  Next i

  If Not silent Then
    logMsg "unable to identify section '" & sectName & "'", ellError, edtLdm
  End If
End Function


Function getSectionShortNameByName( _
  sectName As String _
) As String
  Dim sectIndex As Integer
  getSectionShortNameByName = sectName
  sectIndex = getSectionIndexByName(sectName)
  If (sectIndex > 0) Then getSectionShortNameByName = g_sections.descriptors(sectIndex).shortName
End Function


Function getSectionSeqNoByName( _
  sectName As String _
) As String
  Dim sectIndex As Integer
  getSectionSeqNoByName = 0
  sectIndex = getSectionIndexByName(sectName)
  If (sectIndex > 0) Then getSectionSeqNoByName = g_sections.descriptors(sectIndex).seqNo
End Function


Function getSectionSeqNoByIndex( _
  sectionIndex As Integer _
) As String
  getSectionSeqNoByIndex = 0
  If (sectionIndex > 0) Then getSectionSeqNoByIndex = g_sections.descriptors(sectionIndex).seqNo
End Function


Sub genSectionAcmMetaCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnAcmSection, acmSchemaCsvProcessingStep, "ACM", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  On Error GoTo ErrorExit
  
  Dim i As Integer
  For i = 1 To g_sections.numDescriptors
    With g_sections.descriptors(i)
      If InStr(1, .specificToOrgs, "999") <= 0 And Not .isTechnical Then
        Print #fileNo, """"; UCase(.sectionName); ""","""; UCase(.shortName); ""","; _
                       getCsvTrailer(0)
      End If
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


Sub genSectionLdmMetaCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnLdmSchema, ldmSchemaCsvProcessingStep, "LDM", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  On Error GoTo ErrorExit
  
  Dim i As Integer
  For i = 1 To g_sections.numDescriptors
    With g_sections.descriptors(i)
      If InStr(1, .specificToOrgs, "999") <= 0 And Not .isTechnical Then
        Print #fileNo, """"; genSchemaName(.sectionName, .shortName); ""","; getCsvTrailer(0)
      End If
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


Sub genSectionPdmMetaCsv()
  Dim fileName As String, fileNamePri As String
  Dim fileNo As Integer, fileNoPri As Integer
  Dim fileNoTemplate As Integer, fileNoPriTemplate As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnPdmSchema, pdmCsvProcessingStep, "PDM", edtPdm)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  fileNamePri = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnPdmPrimarySchema, pdmCsvProcessingStep, "PDM", edtPdm)
  fileNoPri = FreeFile()
  Open fileNamePri For Append As #fileNoPri
  On Error GoTo ErrorExit
  
  Dim thisSection As Integer
  Dim thisPoolIndex As Integer, thisPoolId As Integer
  Dim thisOrgIndex As Integer, thisOrgId As Integer
  Dim schemaNameLdm As String
  Dim schemaNamePdm As String
  Dim schemaNameAliasPdm As String
  Dim schemaNameNativePdm As String
  Dim schemaNamePrivateOnlyPdm As String
  Dim schemaNamePublicOnlyPdm As String
  Dim thisFileNo As Integer
  Dim orgIdStr As String
' ### IF IVK ###
  Dim schemaNamePsDpFilteredPdm As String
  Dim schemaNamePsDpFilteredPdmExtended As String
  Dim schemaNameDeletedObjectPdm As String
' ### ENDIF IVK ###
  
  For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
    thisPoolId = g_pools.descriptors(thisPoolIndex).id
    If g_pools.descriptors(thisPoolIndex).supportAcm Then
      For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
        thisOrgId = g_orgs.descriptors(thisOrgIndex).id
        If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And Not g_orgs.descriptors(thisOrgIndex).isTemplate Then
          If g_pools.descriptors(thisPoolIndex).supportLrt Then
            schemaNameAliasPdm = genSchemaName(snAliasLrt, ssnAliasLrt, edtPdm, thisOrgIndex, thisPoolIndex)
' ### IF IVK ###
            schemaNamePsDpFilteredPdm = genSchemaName(snAliasPsDpFiltered, ssnAliasPsDpFiltered, edtPdm, thisOrgIndex, thisPoolIndex)
            schemaNamePsDpFilteredPdmExtended = genSchemaName(snAliasPsDpFilteredExtended, ssnAliasPsDpFilteredExtended, edtPdm, thisOrgIndex, thisPoolIndex)
            schemaNameDeletedObjectPdm = genSchemaName(snAliasDelObj, ssnAliasDelObj, edtPdm, thisOrgIndex, thisPoolIndex)
' ### ENDIF IVK ###
            schemaNamePrivateOnlyPdm = genSchemaName(snAliasPrivateOnly, ssnAliasPrivateOnly, edtPdm, thisOrgIndex, thisPoolIndex)
          Else
' ### IF IVK ###
            If supportAliasDelForNonLrtPools Then
              schemaNameAliasPdm = genSchemaName(snAlias, ssnAlias, edtPdm, thisOrgIndex, thisPoolIndex)
              schemaNameDeletedObjectPdm = genSchemaName(snAliasDelObj, ssnAliasDelObj, edtPdm, thisOrgIndex, thisPoolIndex)
            Else
              schemaNameAliasPdm = genSchemaName(snAliasLrt, ssnAliasLrt, edtPdm, thisOrgIndex, thisPoolIndex)
              schemaNameDeletedObjectPdm = ""
            End If
            schemaNamePsDpFilteredPdm = genSchemaName(snAliasPsDpFiltered, ssnAliasPsDpFiltered, edtPdm, thisOrgIndex, thisPoolIndex)
            schemaNamePsDpFilteredPdmExtended = genSchemaName(snAliasPsDpFilteredExtended, ssnAliasPsDpFilteredExtended, edtPdm, thisOrgIndex, thisPoolIndex)
' ### ELSE IVK ###
'           schemaNameAliasPdm = genSchemaName(snAliasLrt, ssnAliasLrt, edtPdm, thisOrgIndex, thisPoolIndex)
' ### ENDIF IVK ###
            schemaNamePrivateOnlyPdm = ""
          End If
          
          schemaNameNativePdm = genSchemaName(snAlias, ssnAlias, edtPdm, thisOrgIndex, thisPoolIndex)
          schemaNamePublicOnlyPdm = genSchemaName(snAliasPublicOnly, ssnAliasPublicOnly, edtPdm, thisOrgIndex, thisPoolIndex)
      
          thisFileNo = fileNoPri
          orgIdStr = CStr(g_orgs.descriptors(thisOrgIndex).id)
          
          Print #thisFileNo, """"; schemaNameAliasPdm; """,";
' ### IF IVK ###
          Print #thisFileNo, """"; schemaNamePsDpFilteredPdm; """,";
          Print #thisFileNo, """"; schemaNamePsDpFilteredPdmExtended; """,";
          Print #thisFileNo, IIf(schemaNameDeletedObjectPdm = "", "", """" & schemaNameDeletedObjectPdm & """"); ",";
' ### ENDIF IVK ###
          Print #thisFileNo, IIf(schemaNameNativePdm = "", "", """" & schemaNameNativePdm & """"); ",";
          Print #thisFileNo, IIf(schemaNamePrivateOnlyPdm = "", "", """" & schemaNamePrivateOnlyPdm & """"); ",";
          Print #thisFileNo, IIf(schemaNamePublicOnlyPdm = "", "", """" & schemaNamePublicOnlyPdm & """"); ",";
          Print #thisFileNo, orgIdStr; ",";
          Print #thisFileNo, CStr(thisPoolId); ",";
' ### IF IVK ###
          Print #thisFileNo, "0,";
' ### ENDIF IVK ###
          Print #thisFileNo, getCsvTrailer(0)
          
          If g_orgs.descriptors(thisOrgIndex).isTemplate Then
            Close #thisFileNo
          End If
        End If
      Next thisOrgIndex
    End If
  Next thisPoolIndex
  
  For thisSection = 1 To g_sections.numDescriptors
    With g_sections.descriptors(thisSection)
      ' HACK - we have some dummy MPC 999
      If InStr(1, .specificToOrgs, "999") <= 0 And Not .isTechnical Then
        schemaNameLdm = genSchemaName(.sectionName, .shortName, edtLdm)
        schemaNamePdm = genSchemaName(.sectionName, .shortName, edtPdm)
        Print #fileNo, """"; schemaNamePdm; ""","; _
                       ","; _
                       ","; _
                       """"; schemaNameLdm; ""","; _
                       getCsvTrailer(0)
        For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
          If Not g_orgs.descriptors(thisOrgIndex).isTemplate Then
            thisOrgId = g_orgs.descriptors(thisOrgIndex).id
            
            If g_orgs.descriptors(thisOrgIndex).isTemplate Then
              fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnPdmSchema, pdmCsvProcessingStep, "PDM", edtPdm, , , , , thisOrgIndex)
              fileNoTemplate = FreeFile()
              assertDir fileName
              Open fileName For Append As #fileNoTemplate
              thisFileNo = fileNoTemplate
              orgIdStr = genTemplateParamWrapper(CStr(g_orgs.descriptors(thisOrgIndex).id))
            Else
              thisFileNo = fileNo
              orgIdStr = CStr(g_orgs.descriptors(thisOrgIndex).id)
            End If
            
            If (.specificToOrgs = "" Or includedInList(.specificToOrgs, thisOrgId)) Then
              If .specificToPools = "" Or includedInList(.specificToPools, 0) Then
                schemaNamePdm = genSchemaName(.sectionName, .shortName, edtPdm, thisOrgIndex)
                Print #thisFileNo, """"; schemaNamePdm; ""","; _
                               orgIdStr; ","; _
                               ","; _
                               """"; schemaNameLdm; ""","; _
                               getCsvTrailer(0)
              End If
              
              For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                thisPoolId = g_pools.descriptors(thisPoolIndex).id

                If (.specificToPools = "" Or includedInList(.specificToPools, thisPoolId)) And _
                   poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And g_pools.descriptors(thisPoolIndex).supportAcm Then
                  schemaNamePdm = genSchemaName(.sectionName, .shortName, edtPdm, thisOrgIndex, thisPoolIndex)
                  Print #thisFileNo, """"; schemaNamePdm; ""","; _
                                 orgIdStr; ","; _
                                 CStr(thisPoolId); ","; _
                                  """"; schemaNameLdm; ""","; _
                                 getCsvTrailer(0)
                End If
              Next thisPoolIndex
            End If
          
            If g_orgs.descriptors(thisOrgIndex).isTemplate Then
              Close #thisFileNo
            End If
          End If
        Next thisOrgIndex
      End If
    End With
  Next thisSection

NormalExit:
  On Error Resume Next
  Close #fileNo
  Close #fileNoPri
  Close #fileNoTemplate
  Close #fileNoPriTemplate
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub dropSectionsCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbMeta, clnLdmSchema, g_targetDir, ldmSchemaCsvProcessingStep, onlyIfEmpty, "LDM"
  killCsvFileWhereEver g_sectionIndexDbMeta, clnPdmSchema, g_targetDir, pdmCsvProcessingStep, onlyIfEmpty, "PDM"
  killCsvFileWhereEver g_sectionIndexDbMeta, clnPdmPrimarySchema, g_targetDir, pdmCsvProcessingStep, onlyIfEmpty, "PDM"
End Sub


Sub evalSections()
  Dim thisSectionIndex As Integer
  With g_sections
    For thisSectionIndex = 1 To .numDescriptors Step 1
      With .descriptors(thisSectionIndex)
        .sectionIndex = thisSectionIndex
                'Compiler: array mit vier dimensionen
        'ReDim .fileNoDdl(-1 To g_orgs.numDescriptors, -1 To g_pools.numDescriptors, 1 To gc_maxProcessingStep, UBound(g_fileNameIncrements))
      End With
    Next thisSectionIndex
  End With
End Sub

