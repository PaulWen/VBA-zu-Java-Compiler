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
   initSectionDescriptors(g_sections)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   g_sections.maxSeqNo = -1

   While thisSheet.Cells(thisRow, colSection) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

       g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).sectionName = Trim(thisSheet.Cells(thisRow, colSection))
       g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).shortName = Trim(thisSheet.Cells(thisRow, colShortName))
       g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo = getInteger(thisSheet.Cells(thisRow, colSeqNo))
       g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).specificToOrgs = Trim(thisSheet.Cells(thisRow, colSpecificToOrgs))
       g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).specificToPools = Trim(thisSheet.Cells(thisRow, colSpecificToPool))

       If UCase(g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).sectionName) = "FWKTEST" And Not generateFwkTest Then
         g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).specificToOrgs = CStr(100) 'this will never be a valid MPC
         g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).specificToPools = CStr(100)
       End If
       g_sections.maxSeqNo = IIf(g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo > g_sections.maxSeqNo, g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo, g_sections.maxSeqNo)
 
 NextRow:
     thisRow = thisRow + 1
   Wend
 
   ' add some technical sections
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).sectionName = snAlias
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).shortName = ssnAlias
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo = g_sections.maxSeqNo + 1
     g_sections.maxSeqNo = g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).isTechnical = True
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).sectionName = snAliasDelObj
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).shortName = ssnAliasDelObj
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo = g_sections.maxSeqNo + 1
     g_sections.maxSeqNo = g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).isTechnical = True
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).sectionName = snAliasLrt
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).shortName = ssnAliasLrt
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo = g_sections.maxSeqNo + 1
     g_sections.maxSeqNo = g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).isTechnical = True
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).sectionName = snAliasPsDpFiltered
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).shortName = ssnAliasPsDpFiltered
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo = g_sections.maxSeqNo + 1
     g_sections.maxSeqNo = g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).isTechnical = True
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).sectionName = snAliasPsDpFilteredExtended
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).shortName = ssnAliasPsDpFilteredExtended
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo = g_sections.maxSeqNo + 1
     g_sections.maxSeqNo = g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).isTechnical = True
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).sectionName = snAliasPrivateOnly
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).shortName = ssnAliasPrivateOnly
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo = g_sections.maxSeqNo + 1
     g_sections.maxSeqNo = g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).isTechnical = True
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).sectionName = snHelp
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).shortName = ssnHelp
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo = g_sections.maxSeqNo + 1
     g_sections.maxSeqNo = g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).seqNo
     g_sections.descriptors(allocSectionDescriptorIndex(g_sections)).isTechnical = True
 End Sub
 
 
 Function getSections() As SectionDescriptors
   If (g_sections.numDescriptors = 0) Then
     readSheet()
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
   getSections()
 
   For i = 1 To g_sections.numDescriptors Step 1
     If UCase(g_sections.descriptors(i).sectionName) = UCase(sectName) Then
       getSectionIndexByName = i
       Exit Function
     End If
   Next i
 
   If Not silent Then
     logMsg("unable to identify section '" & sectName & "'", ellError, edtLdm)
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
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim i As Integer
   For i = 1 To g_sections.numDescriptors
       If InStr(1, g_sections.descriptors(i).specificToOrgs, "999") <= 0 And Not g_sections.descriptors(i).isTechnical Then
         Print #fileNo, """"; UCase(g_sections.descriptors(i).sectionName); ""","""; UCase(g_sections.descriptors(i).shortName); ""","; _
                        getCsvTrailer(0)
       End If
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
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim i As Integer
   For i = 1 To g_sections.numDescriptors
       If InStr(1, g_sections.descriptors(i).specificToOrgs, "999") <= 0 And Not g_sections.descriptors(i).isTechnical Then
         Print #fileNo, """"; genSchemaName(g_sections.descriptors(i).sectionName, g_sections.descriptors(i).shortName); ""","; getCsvTrailer(0)
       End If
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
   assertDir(fileName)
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
       ' HACK - we have some dummy MPC 999
       If InStr(1, g_sections.descriptors(thisSection).specificToOrgs, "999") <= 0 And Not g_sections.descriptors(thisSection).isTechnical Then
         schemaNameLdm = genSchemaName(g_sections.descriptors(thisSection).sectionName, g_sections.descriptors(thisSection).shortName, edtLdm)
         schemaNamePdm = genSchemaName(g_sections.descriptors(thisSection).sectionName, g_sections.descriptors(thisSection).shortName, edtPdm)
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
               assertDir(fileName)
               Open fileName For Append As #fileNoTemplate
               thisFileNo = fileNoTemplate
               orgIdStr = genTemplateParamWrapper(CStr(g_orgs.descriptors(thisOrgIndex).id))
             Else
               thisFileNo = fileNo
               orgIdStr = CStr(g_orgs.descriptors(thisOrgIndex).id)
             End If

             If (g_sections.descriptors(thisSection).specificToOrgs = "" Or includedInList(g_sections.descriptors(thisSection).specificToOrgs, thisOrgId)) Then
               If g_sections.descriptors(thisSection).specificToPools = "" Or includedInList(g_sections.descriptors(thisSection).specificToPools, 0) Then
                 schemaNamePdm = genSchemaName(g_sections.descriptors(thisSection).sectionName, g_sections.descriptors(thisSection).shortName, edtPdm, thisOrgIndex)
                 Print #thisFileNo, """"; schemaNamePdm; ""","; _
                                orgIdStr; ","; _
                                ","; _
                                """"; schemaNameLdm; ""","; _
                                getCsvTrailer(0)
               End If

               For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                 thisPoolId = g_pools.descriptors(thisPoolIndex).id
 
                 If (g_sections.descriptors(thisSection).specificToPools = "" Or includedInList(g_sections.descriptors(thisSection).specificToPools, thisPoolId)) And _
                    poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And g_pools.descriptors(thisPoolIndex).supportAcm Then
                   schemaNamePdm = genSchemaName(g_sections.descriptors(thisSection).sectionName, g_sections.descriptors(thisSection).shortName, edtPdm, thisOrgIndex, thisPoolIndex)
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
   killCsvFileWhereEver(g_sectionIndexDbMeta, clnLdmSchema, g_targetDir, ldmSchemaCsvProcessingStep, onlyIfEmpty, "LDM")
   killCsvFileWhereEver(g_sectionIndexDbMeta, clnPdmSchema, g_targetDir, pdmCsvProcessingStep, onlyIfEmpty, "PDM")
   killCsvFileWhereEver(g_sectionIndexDbMeta, clnPdmPrimarySchema, g_targetDir, pdmCsvProcessingStep, onlyIfEmpty, "PDM")
 End Sub
 
 
 Sub evalSections()
   Dim thisSectionIndex As Integer
     For thisSectionIndex = 1 To g_sections.numDescriptors Step 1
         g_sections.descriptors(thisSectionIndex).sectionIndex = thisSectionIndex
                 'Compiler: array mit vier dimensionen
         'ReDim .fileNoDdl(-1 To g_orgs.numDescriptors, -1 To g_pools.numDescriptors, 1 To gc_maxProcessingStep, UBound(g_fileNameIncrements))
     Next thisSectionIndex
 End Sub
 
