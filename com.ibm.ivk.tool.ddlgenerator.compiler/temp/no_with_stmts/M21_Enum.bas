 Attribute VB_Name = "M21_Enum"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colSection = 2
 Private Const colEnumName = colSection + 1
 Private Const colI18nId = colEnumName + 1
 Private Const colIsEnumlang = colI18nId + 1
 Private Const colShortName = colIsEnumlang + 1
 Private Const colIdDomainSection = colShortName + 1
 Private Const colIdDomainName = colIdDomainSection + 1
 Private Const colValueMaxLength = colIdDomainName + 1
 Private Const colIsCommonToOrgs = colValueMaxLength + 1
 Private Const colIsCommonToPools = colIsCommonToOrgs + 1
 Private Const colEnumId = colIsCommonToPools + 1
 Private Const colNotAcmRelated = colEnumId + 1
 Private Const colNoAlias = colNotAcmRelated + 1
 ' ### IF IVK ###
 Private Const colNoXmlExport = colNoAlias + 1
 Private Const colUseXmlExport = colNoXmlExport + 1
 Private Const colIsLrtSpecific = colUseXmlExport + 1
 ' ### ELSE IVK ###
 'Private Const colIsLrtSpecific = colNoAlias + 1
 ' ### ENDIF IVK ###
 Private Const colIsPdmSpecific = colIsLrtSpecific + 1
 Private Const colTabSpaceData = colIsPdmSpecific + 1
 Private Const colTabSpaceLong = colTabSpaceData + 1
 Private Const colTabSpaceNl = colTabSpaceLong + 1
 Private Const colTabSpaceIndex = colTabSpaceNl + 1
 Private Const colValueId = colTabSpaceIndex + 1
 Private Const colValueLang1 = colValueId + 1
 Private Const colValueLang2 = colValueLang1 + 1
 Private Const colValueLang3 = colValueLang2 + 1
 Private Const colValueLang4 = colValueLang3 + 1
 
 Private Const colFirstValueLang = colValueLang1
 Private Const colLastValueLang = colValueLang4
 Private Const colFirstAttr = colLastValueLang + 1
 
 Private Const firstRow = 3
 
 Private Const sheetName = "Enum"
 
 Private Const processingStep = 1
 Private Const acmCsvProcessingStep = 0
 
 Private Const suffixLabel = "_LABEL"
 Private Const suffixLabelShort = "LBL"
 Private Const suffixText = "_TEXT"
 Private Const suffixTextShort = "TXT"
 
 Global g_enums As EnumDescriptors
 
 
 Private Sub checkRow( _
   ByRef thisSheet As Worksheet, _
   thisRow As Integer, _
   ByRef secName As String, _
   ByRef enumName As String, _
   ByRef shortName As String _
 )
   If thisSheet.Cells(thisRow, colSection) & "" <> "" Then secName = thisSheet.Cells(thisRow, colSection)
   If thisSheet.Cells(thisRow, colEnumName) & "" <> "" Then enumName = thisSheet.Cells(thisRow, colEnumName)
   If thisSheet.Cells(thisRow, colShortName) & "" <> "" Then shortName = thisSheet.Cells(thisRow, colShortName)
 End Sub
 
 Private Sub readSheet()
   initEnumDescriptors g_enums

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   Dim lastEnumName As String
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   Dim currSection As String, currEnumName As String, currShortName As String
   checkRow thisSheet, thisRow, currSection, currEnumName, currShortName
   lastEnumName = ""

   Dim i As Integer
   While thisSheet.Cells(thisRow, colEnumName) & thisSheet.Cells(thisRow, colValueId) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

     If currEnumName <> "" And currEnumName <> lastEnumName Then
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).sectionName = currSection
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).enumName = currEnumName
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).i18nId = Trim(thisSheet.Cells(thisRow, colI18nId))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isEnumLang = getBoolean(thisSheet.Cells(thisRow, colIsEnumlang))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).shortName = currShortName
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).idDomainSection = Trim(thisSheet.Cells(thisRow, colIdDomainSection))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).idDomainName = Trim(thisSheet.Cells(thisRow, colIdDomainName))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).maxLength = IIf(thisSheet.Cells(thisRow, colValueMaxLength) & "" = "", -1, _
                          CInt(thisSheet.Cells(thisRow, colValueMaxLength)))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isCommonToOrgs = getBoolean(thisSheet.Cells(thisRow, colIsCommonToOrgs))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isCommonToPools = g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isCommonToOrgs Or getBoolean(thisSheet.Cells(thisRow, colIsCommonToPools))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).enumId = getInteger(thisSheet.Cells(thisRow, colEnumId))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).notAcmRelated = getBoolean(thisSheet.Cells(thisRow, colNotAcmRelated))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).noAlias = getBoolean(thisSheet.Cells(thisRow, colNoAlias))
 ' ### IF IVK ###
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).noXmlExport = getBoolean(thisSheet.Cells(thisRow, colNoXmlExport))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).useXmlExport = getBoolean(thisSheet.Cells(thisRow, colUseXmlExport))
 ' ### ENDIF IVK ###
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isLrtSpecific = getBoolean(thisSheet.Cells(thisRow, colIsLrtSpecific))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isPdmSpecific = getBoolean(thisSheet.Cells(thisRow, colIsPdmSpecific))
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceData = thisSheet.Cells(thisRow, colTabSpaceData)
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceLong = thisSheet.Cells(thisRow, colTabSpaceLong)
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceNl = thisSheet.Cells(thisRow, colTabSpaceNl)
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceIndex = thisSheet.Cells(thisRow, colTabSpaceIndex)

         initEnumVals g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values

         Dim thisEnumName As String
         thisEnumName = currEnumName
         While thisSheet.Cells(thisRow, colValueId) & "" <> "" And thisEnumName = currEnumName
           Dim colLang As Integer
           For colLang = colFirstValueLang To colLastValueLang Step 1
             If thisSheet.Cells(thisRow, colLang) & "" <> "" Then
                 g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).id = CInt(thisSheet.Cells(thisRow, colValueId))
                 g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).oid = pullOid
                 g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).languageId = colLang - colFirstValueLang + 1
                 g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).valueString = Trim(thisSheet.Cells(thisRow, colLang))
                 For i = 1 To maxAttrsPerEnum Step 1
                   g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).attrStrings(i) = thisSheet.Cells(thisRow, colFirstAttr + i - 1)
                 Next i
             End If
           Next colLang
           thisRow = thisRow + 1
           If thisSheet.Cells(thisRow, colEnumName) & "" <> "" Then thisEnumName = thisSheet.Cells(thisRow, colEnumName)
         Wend
         lastEnumName = currEnumName
         checkRow thisSheet, thisRow, currSection, currEnumName, currShortName
     End If
     If currEnumName = lastEnumName Then
 NextRow:
       thisRow = thisRow + 1
       checkRow thisSheet, thisRow, currSection, currEnumName, currShortName
     End If
   Wend
 End Sub
 
 Private Sub genEnumDdl( _
   thisEnumIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 )
   If ddlType = edtPdm And Not poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
     Exit Sub
   End If

   Dim thisOrgId As Integer
   Dim thisPoolId As Integer
   If thisOrgIndex > 0 Then thisOrgId = g_orgs.descriptors(thisOrgIndex).id Else thisOrgId = -1
   If thisPoolIndex > 0 Then thisPoolId = g_pools.descriptors(thisPoolIndex).id Else thisPoolId = -1

 ' ### IF IVK ###
   Dim fileNoXmlF As Integer
   Dim fileNoXmlV As Integer
 ' ### ENDIF IVK ###
   Dim poolSupportXmlExport As Boolean
   Dim poolSupportAcm As Boolean

   On Error GoTo ErrorExit

   If thisPoolIndex > 0 Then
       poolSupportXmlExport = g_pools.descriptors(thisPoolIndex).supportXmlExport
       poolSupportAcm = g_pools.descriptors(thisPoolIndex).supportAcm
   End If

   Dim ldmIteration As Integer
     If g_enums.descriptors(thisEnumIndex).sectionName & "" = "" Then
       GoTo NormalExit
     End If

     If g_enums.descriptors(thisEnumIndex).isLrtSpecific And Not g_genLrtSupport Then
       GoTo NormalExit
     End If

     If g_enums.descriptors(thisEnumIndex).isPdmSpecific And ddlType <> edtPdm Then
       GoTo NormalExit
     End If

     If ignoreUnknownSections And (g_enums.descriptors(thisEnumIndex).sectionIndex < 0) Then
       GoTo NormalExit
     End If

     If ddlType = edtPdm Then
         If Not g_enums.descriptors(thisEnumIndex).isCommonToOrgs And (g_sections.descriptors(g_enums.descriptors(thisEnumIndex).sectionIndex).specificToOrgs <> "" And Not includedInList(g_sections.descriptors(g_enums.descriptors(thisEnumIndex).sectionIndex).specificToOrgs, thisOrgId)) Then
           GoTo NormalExit
         End If
         If Not g_enums.descriptors(thisEnumIndex).isCommonToPools And (g_sections.descriptors(g_enums.descriptors(thisEnumIndex).sectionIndex).specificToPools <> "" And Not includedInList(g_sections.descriptors(g_enums.descriptors(thisEnumIndex).sectionIndex).specificToPools, thisPoolId)) Then
           GoTo NormalExit
         End If
     End If

     If ddlType = edtPdm And thisPoolId <> -1 Then
       If Not g_enums.descriptors(thisEnumIndex).notAcmRelated And Not poolSupportAcm Then
         GoTo NormalExit
       End If
     End If

     ldmIteration = IIf(g_enums.descriptors(thisEnumIndex).isCommonToOrgs, ldmIterationGlobal, ldmIterationPoolSpecific)

     Dim fileNo As Integer
     fileNo = openDdlFile(g_targetDir, g_enums.descriptors(thisEnumIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex)

 ' ### IF IVK ###
     If generateXmlExportSupport Then
       fileNoXmlV = openDdlFile(g_targetDir, g_enums.descriptors(thisEnumIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseXmlExport, ldmIteration)

       If generateXsdInCtoSchema And ddlType = edtPdm And thisOrgIndex > 0 Then
         fileNoXmlF = openDdlFile(g_targetDir, g_enums.descriptors(thisEnumIndex).sectionIndex, processingStep, ddlType, , , , phaseXmlExport, ldmIteration)
       Else
         fileNoXmlF = fileNoXmlV
       End If
     End If

 ' ### ENDIF IVK ###

     Dim qualTabName As String, qualTabNameLdm As String
     Dim qualIndexName As String
     Dim qualLangTabName As String, qualLangTabNameLdm As String
 
     qualTabName = genQualTabNameByEnumIndex(g_enums.descriptors(thisEnumIndex).enumIndex, ddlType, thisOrgIndex, thisPoolIndex)
     qualTabNameLdm = IIf(ddlType = edtLdm, qualTabName, genQualTabNameByEnumIndex(g_enums.descriptors(thisEnumIndex).enumIndex, edtLdm, thisOrgIndex, thisPoolIndex))
     qualLangTabName = getQualTabNameLanguageEnum(thisOrgIndex, thisPoolIndex, ddlType)
     qualLangTabNameLdm = getQualTabNameLanguageEnum(thisOrgIndex, thisPoolIndex, edtLdm)

     addTabToDdlSummary qualTabName, ddlType, g_enums.descriptors(thisEnumIndex).notAcmRelated
     registerQualTable qualTabNameLdm, qualTabName, g_enums.descriptors(thisEnumIndex).enumIndex, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum, thisOrgIndex, thisPoolIndex, ddlType, g_enums.descriptors(thisEnumIndex).notAcmRelated

     Dim enumNameLbl As String, enumNameLblShort As String
     Dim enumNameDbLbl As String, enumNameDbLblShort As String
     enumNameLbl = genNlObjName(g_enums.descriptors(thisEnumIndex).enumName)
     enumNameLblShort = genNlObjShortName(g_enums.descriptors(thisEnumIndex).shortName)
     enumNameDbLbl = genNlObjName(g_enums.descriptors(thisEnumIndex).enumNameDb)
     enumNameDbLblShort = genNlObjShortName(g_enums.descriptors(thisEnumIndex).shortName)

     If generateDdlCreateTable Then
       printChapterHeader "Enumeration """ & g_enums.descriptors(thisEnumIndex).sectionName & "." & g_enums.descriptors(thisEnumIndex).enumName & """", fileNo
       Print #fileNo, "CREATE TABLE"
       Print #fileNo, addTab(1); qualTabName
       Print #fileNo, addTab(0); "("

       If g_enums.descriptors(thisEnumIndex).domainIndexId > 0 Then
         Print #fileNo, genAttrDecl(conEnumId, cosnEnumId, eavtDomainEnumId, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType)
       End If

       genAttrDeclsForEnum thisEnumIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex

       printSectionHeader "Object Version ID", fileNo
       Print #fileNo, genAttrDeclByDomain(conVersionId, cosnVersionId, _
                      eavtDomain, g_domainIndexVersion, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL DEFAULT 1" & _
                      IIf(ddlType = edtPdm And dbCompressValuesInEnumTabs And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), _
                      False, ddlType, , , eacVid)

       Print #fileNo, ")"
       If ddlType = edtPdm Then
         If g_enums.descriptors(thisEnumIndex).tabSpaceData <> "" Then
           Print #fileNo, "IN " & genTablespaceNameByIndex(g_enums.descriptors(thisEnumIndex).tabSpaceIndexData, thisOrgIndex, thisPoolIndex)
         End If
         If g_enums.descriptors(thisEnumIndex).tabSpaceLong <> "" Then
           Print #fileNo, "LONG IN " & genTablespaceNameByIndex(g_enums.descriptors(thisEnumIndex).tabSpaceIndexLong, thisOrgIndex, thisPoolIndex)
         End If
         If g_enums.descriptors(thisEnumIndex).tabSpaceIndex <> "" Then
           Print #fileNo, "INDEX IN " & genTablespaceNameByIndex(g_enums.descriptors(thisEnumIndex).tabSpaceIndexIndex, thisOrgIndex, thisPoolIndex)
         End If
       End If

       If ddlType = edtPdm And dbCompressValuesInEnumTabs Then
         Print #fileNo, "VALUE COMPRESSION"
       End If
       Print #fileNo, "COMPRESS YES"
       Print #fileNo, gc_sqlCmdDelim
     End If

     If g_enums.descriptors(thisEnumIndex).idDataType <> etNone Then
       If generateDdlCreatePK Then
         Print #fileNo,
         Print #fileNo, "ALTER TABLE"
         Print #fileNo, addTab(1); qualTabName
         Print #fileNo, "ADD CONSTRAINT"
         Print #fileNo, addTab(1); genPkName(g_enums.descriptors(thisEnumIndex).enumName, g_enums.descriptors(thisEnumIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex)
         Print #fileNo, "PRIMARY KEY"
         Print #fileNo, addTab(1); "(" & g_anEnumId & ")"
         Print #fileNo, gc_sqlCmdDelim
       End If
     End If

     If generateCommentOnTables And Not g_enums.descriptors(thisEnumIndex).notAcmRelated Then
       Print #fileNo,
       genDbObjComment "TABLE", qualTabName, "ACM-Enumeration """ & g_enums.descriptors(thisEnumIndex).sectionName & "." & g_enums.descriptors(thisEnumIndex).enumName & """", fileNo, thisOrgIndex, thisPoolIndex
     End If

     If generateCommentOnColumns And Not g_enums.descriptors(thisEnumIndex).notAcmRelated Then
       Print #fileNo,
       Print #fileNo, addTab(0); "COMMENT ON "; qualTabName; " ("

 ' ### IF IVK ###
       Print #fileNo, genAttrDecl(conEnumId, cosnEnumId, eavtDomainEnumId, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , , "[LDM] Enumeration Value")
 ' ### ELSE IVK ###
 '     Print #fileNo, genAttrDecl(conEnumId, cosnEnumId, eavtDomainEnumId, .enumIndex, eactEnum, .enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , "[LDM] Enumeration Value")
 ' ### ENDIF IVK ###

       genAttrDeclsForEnum thisEnumIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex, edomComment
 
       Print #fileNo, genAttrDeclByDomain(conVersionId, cosnVersionId, eavtDomain, _
                      g_domainIndexVersion, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL DEFAULT 1" & _
                      IIf(ddlType = edtPdm And dbCompressValuesInEnumTabs And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), _
                      False, ddlType, , edomComment, eacVid, , , , "[LDM] Record version tag")

       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     End If
 
     If g_enums.descriptors(thisEnumIndex).idDataType <> etNone Then
       Dim qualTabNameNl As String
       Dim qualLdmTabNameNl As String
       qualTabNameNl = genQualTabNameByEnumIndex(g_enums.descriptors(thisEnumIndex).enumIndex, ddlType, thisOrgIndex, thisPoolIndex, True)
       qualLdmTabNameNl = genQualTabNameByEnumIndex(g_enums.descriptors(thisEnumIndex).enumIndex, edtLdm, thisOrgIndex, thisPoolIndex, True)

       addTabToDdlSummary qualTabNameNl, ddlType, g_enums.descriptors(thisEnumIndex).notAcmRelated
       registerQualTable qualLdmTabNameNl, qualTabNameNl, g_enums.descriptors(thisEnumIndex).enumIndex, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum, thisOrgIndex, thisPoolIndex, ddlType, g_enums.descriptors(thisEnumIndex).notAcmRelated, , , True

       If generateDdlCreateTable Then
         Print #fileNo, ""
         Print #fileNo, "CREATE TABLE"
         Print #fileNo, addTab(1); qualTabNameNl
         Print #fileNo, "("
         printSectionHeader "Surrogate Key", fileNo
         Print #fileNo, genAttrDeclByDomain(conOid, cosnOid, eavtDomain, g_domainIndexOid, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType)
         printSectionHeader "Reference to ENUM table", fileNo
         Print #fileNo, genAttrDecl(conEnumRefId, cosnEnumRefId, eavtDomainEnumId, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType)
         printSectionHeader "Language of this LABEL", fileNo
         Print #fileNo, genAttrDecl(conLanguageId, cosnLanguageId, eavtDomainEnumId, g_enumIndexLanguage, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType)
         printSectionHeader "LABEL", fileNo
         Print #fileNo, genAttrDecl(conEnumLabelText, cosnEnumLabelText, eavtDomainEnumValue, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType)

         printSectionHeader "Object Version ID", fileNo
         Print #fileNo, genAttrDeclByDomain(conVersionId, cosnVersionId, eavtDomain, _
                        g_domainIndexVersion, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL DEFAULT 1", False, ddlType, , , eacVid)
         Print #fileNo, ")"

         If ddlType = edtPdm Then
           If g_enums.descriptors(thisEnumIndex).tabSpaceData <> "" Then
             Print #fileNo, "IN " & genTablespaceNameByIndex(g_enums.descriptors(thisEnumIndex).tabSpaceIndexNl, thisOrgIndex, thisPoolIndex)
           End If
           If g_enums.descriptors(thisEnumIndex).tabSpaceLong <> "" Then
             Print #fileNo, "LONG IN " & genTablespaceNameByIndex(g_enums.descriptors(thisEnumIndex).tabSpaceIndexLong, thisOrgIndex, thisPoolIndex)
           End If
           If g_enums.descriptors(thisEnumIndex).tabSpaceIndex <> "" Then
             Print #fileNo, "INDEX IN " & genTablespaceNameByIndex(g_enums.descriptors(thisEnumIndex).tabSpaceIndexIndex, thisOrgIndex, thisPoolIndex)
           End If
         End If

         If ddlType = edtPdm And dbCompressValuesInEnumTabs And dbCompressValuesInNlsTabs Then
           Print #fileNo, "VALUE COMPRESSION"
         End If
         Print #fileNo, "COMPRESS YES"
         Print #fileNo, gc_sqlCmdDelim
       End If

       If generateCommentOnTables And Not g_enums.descriptors(thisEnumIndex).notAcmRelated Then
         Print #fileNo,
         genDbObjComment "TABLE", qualTabNameNl, "ACM-Enumeration """ & g_enums.descriptors(thisEnumIndex).sectionName & "." & g_enums.descriptors(thisEnumIndex).enumName & """ (NL)", fileNo, thisOrgIndex, thisPoolIndex
       End If

       If generateCommentOnColumns And Not g_enums.descriptors(thisEnumIndex).notAcmRelated Then
         Print #fileNo,
         Print #fileNo, addTab(0); "COMMENT ON "; qualTabNameNl; " ("
         Print #fileNo, genAttrDeclByDomain(conOid, cosnOid, eavtDomain, _
                        g_domainIndexOid, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , "[LDM] Record (/Object) Identifier")
 ' ### IF IVK ###
         Print #fileNo, genAttrDecl(conEnumRefId, cosnEnumRefId, eavtDomainEnumId, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , , "[LDM] Reference to parent-Enumeration-table")
         Print #fileNo, genAttrDecl(conLanguageId, cosnLanguageId, eavtDomainEnumId, g_enumIndexLanguage, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , , "[LDM] Language identifier")
         Print #fileNo, genAttrDecl(conEnumLabelText, cosnEnumLabelText, eavtDomainEnumValue, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , , "[LDM] NL-value of Enumeration literal")
 ' ### ELSE IVK ###
 '       Print #fileNo, genAttrDecl(conEnumRefId, cosnEnumRefId, eavtDomainEnumId, .enumIndex, .enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , "[LDM] Reference to parent-Enumeration-table")
 '       Print #fileNo, genAttrDecl(conLanguageId, cosnLanguageId, eavtDomainEnumId, g_enumIndexLanguage, eactEnum, .enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , "[LDM] Language identifier")
 '       Print #fileNo, genAttrDecl(conEnumLabelText, cosnEnumLabelText, eavtDomainEnumValue, .enumIndex, eactEnum, .enumIndex, "NOT NULL", , ddlType, , edomComment, , , , , , "[LDM] NL-value of Enumeration literal")
 ' ### ENDIF IVK ###
         Print #fileNo, genAttrDeclByDomain(conVersionId, cosnVersionId, eavtDomain, _
                        g_domainIndexVersion, eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL DEFAULT 1", False, ddlType, , edomComment, eacVid, , , , "[LDM] Record version tag")
         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       End If

       If generateDdlCreatePK Then
         Print #fileNo,
         Print #fileNo, addTab(0); "ALTER TABLE"
         Print #fileNo, addTab(1); qualTabNameNl
         Print #fileNo, addTab(0); "ADD CONSTRAINT"
         Print #fileNo, addTab(1); genPkName(enumNameLbl, enumNameLblShort, ddlType, thisOrgIndex, thisPoolIndex)
         Print #fileNo, addTab(0); "PRIMARY KEY"
         Print #fileNo, addTab(1); "(" & g_anOid & ")"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       End If

       If generateDdlCreateFK Then
         Print #fileNo,
         Print #fileNo, addTab(0); "ALTER TABLE"
         Print #fileNo, addTab(1); qualTabNameNl
         Print #fileNo, addTab(0); "ADD CONSTRAINT"
         Print #fileNo, addTab(1); genFkName(enumNameLbl, enumNameLblShort, "RID", ddlType, thisOrgIndex, thisPoolIndex)
         Print #fileNo, addTab(0); "FOREIGN KEY"
         Print #fileNo, addTab(1); "("; g_anEnumRefId; ")"
         Print #fileNo, addTab(0); "REFERENCES"
         Print #fileNo, addTab(1); qualTabName; " ("; g_anEnumId; ")"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       End If

       registerQualLdmFk qualLdmTabNameNl, qualTabNameLdm, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum

       If (ddlType = edtPdm) And generateIndexOnFkForEnums And generateDdlCreateIndex Then
         qualIndexName = genQualIndexName(g_enums.descriptors(thisEnumIndex).sectionIndex, enumNameLbl & "PAR", enumNameLblShort & "PAR", ddlType, thisOrgIndex, thisPoolIndex)

         If indexExcp(qualIndexName, thisOrgIndex) = False Then
           Print #fileNo,
           Print #fileNo, addTab(0); "CREATE INDEX"
           Print #fileNo, addTab(1); qualIndexName
           Print #fileNo, addTab(0); "ON"
           Print #fileNo, addTab(1); qualTabNameNl
           Print #fileNo, addTab(0); "("
           Print #fileNo, addTab(1); g_anEnumRefId; " ASC"
           Print #fileNo, addTab(0); ")"
           Print #fileNo, gc_sqlCmdDelim
         End If 'indexExcp
       End If

       If generateDdlCreateFK Then
         Print #fileNo,
         Print #fileNo, addTab(0); "ALTER TABLE"
         Print #fileNo, addTab(1); qualTabNameNl
         Print #fileNo, addTab(0); "ADD CONSTRAINT"
         Print #fileNo, addTab(1); genFkName(enumNameLbl, enumNameLblShort, "LID", ddlType, thisOrgIndex, thisPoolIndex)
         Print #fileNo, addTab(0); "FOREIGN KEY"
         Print #fileNo, addTab(1); "("; g_anLanguageId; ")"
         Print #fileNo, addTab(0); "REFERENCES"
         Print #fileNo, addTab(1); qualLangTabName; " ("; g_anEnumId; ")"
         Print #fileNo, gc_sqlCmdDelim
       End If

       registerQualLdmFk qualLdmTabNameNl, qualLangTabNameLdm, g_enums.descriptors(thisEnumIndex).enumIndex, eactEnum

       If generateDdlCreateIndex Then
         If (ddlType = edtPdm) And generateIndexOnFkForEnums And generateIndexOnFkForNLang Then
           qualIndexName = genQualIndexName(g_enums.descriptors(thisEnumIndex).sectionIndex, enumNameLbl & "LAN", enumNameLblShort & "LAN", ddlType, thisOrgIndex, thisPoolIndex)

           If indexExcp(qualIndexName, thisOrgIndex) = False Then
             Print #fileNo,
             Print #fileNo, addTab(0); "CREATE INDEX"
             Print #fileNo, addTab(1); qualIndexName
             Print #fileNo, addTab(0); "ON"
             Print #fileNo, addTab(1); qualTabNameNl
             Print #fileNo, addTab(0); "("
             Print #fileNo, addTab(1); g_anLanguageId; " ASC"
             Print #fileNo, addTab(0); ")"
             Print #fileNo, gc_sqlCmdDelim
           End If 'indexExcp
         End If

         qualIndexName = genQualObjName(g_enums.descriptors(thisEnumIndex).sectionIndex, g_enums.descriptors(thisEnumIndex).shortName & "LBL_UK", g_enums.descriptors(thisEnumIndex).shortName & "LBL_UK", ddlType, thisOrgIndex, thisPoolIndex)

         If indexExcp(qualIndexName, thisOrgIndex) = False Then
           Print #fileNo,
           Print #fileNo, addTab(0); "CREATE UNIQUE INDEX"
           Print #fileNo, addTab(1); qualIndexName
           Print #fileNo, addTab(0); "ON"
           Print #fileNo, addTab(1); qualTabNameNl
           Print #fileNo, addTab(0); "("
           Print #fileNo, addTab(1); g_anEnumRefId & ","
           Print #fileNo, addTab(1); g_anLanguageId
           Print #fileNo, addTab(0); ")"
           Print #fileNo, gc_sqlCmdDelim
         End If 'indexExcp
       End If
     End If

     If ddlType = edtPdm And Not g_enums.descriptors(thisEnumIndex).noAlias Then
       Dim qualEnumTabNameLdm  As String
       qualEnumTabNameLdm = genQualTabNameByEnumIndex(g_enums.descriptors(thisEnumIndex).enumIndex, edtLdm, thisOrgIndex, thisPoolIndex)
 ' ### IF IVK ###
       genAliasDdl g_enums.descriptors(thisEnumIndex).sectionIndex, g_enums.descriptors(thisEnumIndex).enumNameDb, g_enums.descriptors(thisEnumIndex).isCommonToOrgs, g_enums.descriptors(thisEnumIndex).isCommonToPools, Not g_enums.descriptors(thisEnumIndex).notAcmRelated, _
         qualEnumTabNameLdm, qualTabName, g_enums.descriptors(thisEnumIndex).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, False, False, False, _
         "Enumeration """ & g_enums.descriptors(thisEnumIndex).sectionName & "." & g_enums.descriptors(thisEnumIndex).enumName & """"
 ' ### ELSE IVK ###
 '     genAliasDdl .sectionIndex, .enumNameDb, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
 '       qualEnumTabNameLdm, qualTabName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, _
 '       "Enumeration """ & .sectionName & "." & .enumName & """"
 ' ### ENDIF IVK ###

       qualEnumTabNameLdm = genQualTabNameByEnumIndex(g_enums.descriptors(thisEnumIndex).enumIndex, edtLdm, thisOrgIndex, thisPoolIndex)
 ' ### IF IVK ###
       genAliasDdl g_enums.descriptors(thisEnumIndex).sectionIndex, enumNameDbLbl, g_enums.descriptors(thisEnumIndex).isCommonToOrgs, g_enums.descriptors(thisEnumIndex).isCommonToPools, Not g_enums.descriptors(thisEnumIndex).notAcmRelated, _
         qualEnumTabNameLdm, qualTabNameNl, g_enums.descriptors(thisEnumIndex).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, False, False, False, _
         "Enumeration-Label """ & g_enums.descriptors(thisEnumIndex).sectionName & "." & enumNameDbLbl & """"
 ' ### ELSE IVK ###
 '     genAliasDdl .sectionIndex, enumNameDbLbl, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
 '       qualEnumTabNameLdm, qualTabNameNl, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, _
 '       "Enumeration-Label """ & .sectionName & "." & enumNameDbLbl & """"
 ' ### ENDIF IVK ###
     End If

     genEnumCsv thisEnumIndex, ddlType, thisOrgIndex, thisPoolIndex

     ' enums may be a copy taken from g_enumss! make sure we update the original source!
     g_enums.descriptors(g_enums.descriptors(thisEnumIndex).enumIndex).isLdmCsvExported = True
     g_enums.descriptors(g_enums.descriptors(thisEnumIndex).enumIndex).isCtoAliasCreated = True

     g_enums.descriptors(thisEnumIndex).isLdmCsvExported = True ' safe is safe ;-)
     g_enums.descriptors(thisEnumIndex).isCtoAliasCreated = True ' safe is safe ;-)

 ' ### IF IVK ###
 GenXmlExport:
     If generateXmlExportSupport And (ddlType = edtLdm Or thisPoolIndex < 1 Or poolSupportXmlExport) Then
       genXmlExportDdlForEnum thisEnumIndex, thisOrgIndex, thisPoolIndex, fileNoXmlF, fileNoXmlV, ddlType
     End If

 ' ### ENDIF IVK ###
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
 ' ### IF IVK ###
   Close #fileNoXmlV
   Close #fileNoXmlF
 ' ### ENDIF IVK ###

   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genEnumsDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisEnumIndex As Integer
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   resetEnumsCsvExported

     If ddlType = edtLdm Then
       For thisEnumIndex = 1 To g_enums.numDescriptors Step 1
         genEnumDdl thisEnumIndex, edtLdm
       Next thisEnumIndex

       resetEnumsCsvExported
     ElseIf ddlType = edtPdm Then
       For thisEnumIndex = 1 To g_enums.numDescriptors Step 1
           If g_enums.descriptors(thisEnumIndex).isCommonToOrgs Then
             genEnumDdl g_enums.descriptors(thisEnumIndex).enumIndex, edtPdm

             ' if there is some data pool which locally implements this enumeration, take care of that
             For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
               If g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
                 For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
                   If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                     genEnumDdl g_enums.descriptors(thisEnumIndex).enumIndex, edtPdm, thisOrgIndex, thisPoolIndex
                   End If
                 Next thisOrgIndex
               End If
             Next thisPoolIndex

           Else
             For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
               If g_enums.descriptors(thisEnumIndex).isCommonToPools Then
                 genEnumDdl g_enums.descriptors(thisEnumIndex).enumIndex, edtPdm, thisOrgIndex

                 ' if there is some data pool which locally implements this enumeration, take care of that
                 For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                   If g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
                     If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                       genEnumDdl g_enums.descriptors(thisEnumIndex).enumIndex, edtPdm, thisOrgIndex, thisPoolIndex
                     End If
                   End If
                 Next thisPoolIndex

               Else
                 For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                   If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                     genEnumDdl g_enums.descriptors(thisEnumIndex).enumIndex, edtPdm, thisOrgIndex, thisPoolIndex
                   End If
                 Next thisPoolIndex
               End If
             Next thisOrgIndex
           End If
       Next thisEnumIndex

       resetEnumsCsvExported
     End If
 End Sub
 
 
 Sub genEnumAcmMetaCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   On Error GoTo ErrorExit

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnAcmEntity, acmCsvProcessingStep, "ACM", ddlType)
   assertDir fileName
   fileNo = FreeFile()
   Open fileName For Append As #fileNo

   Dim i As Integer
   For i = 1 To g_enums.numDescriptors
       If (Not g_enums.descriptors(i).isPdmSpecific Or ddlType = edtPdm) Then
         Print #fileNo, """"; UCase(g_enums.descriptors(i).sectionName); """,";
         Print #fileNo, """"; UCase(g_enums.descriptors(i).enumName); """,";
         Print #fileNo, """"; UCase(g_enums.descriptors(i).shortName); """,";
         Print #fileNo, """"; gc_acmEntityTypeKeyEnum; """,";
         Print #fileNo, """"; g_enums.descriptors(i).enumIdStr; """,";
         Print #fileNo, """"; g_enums.descriptors(i).i18nId; """,";
         Print #fileNo, IIf(g_enums.descriptors(i).isCommonToOrgs, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_enums.descriptors(i).isCommonToPools, gc_dbTrue, gc_dbFalse); ",";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_enums.descriptors(i).supportXmlExport, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_enums.descriptors(i).useXmlExport, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, ",0,0,0,0,0,,,0,0,0,0,0,0,0,0,0,0,0,,,,,";
 ' ### ELSE IVK ###
 '       Print #fileNo, ",0,0,0,0,0,0,0,0,";
 ' ### ENDIF IVK ###
         Print #fileNo, ",,,,,0,";
         Print #fileNo, getCsvTrailer(12)
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
 
 
 Sub dropEnumsCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   Dim i As Integer, j As Integer
   Dim orgIndex As Integer
   Dim poolIndex As Integer

   ' FIXME: why do we use '3' here?
   Const maxSteps = 3
   Dim enumName As String
   killCsvFileWhereEver g_sectionIndexDbMeta, clnAcmEntity, g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM"
   For i = 1 To g_enums.numDescriptors Step 1
       For j = 0 To maxSteps
         enumName = g_enums.descriptors(i).enumName
         Dim k As Integer
         For k = 1 To 2
           killCsvFileWhereEver g_enums.descriptors(i).sectionIndex, enumName, g_targetDir, j, onlyIfEmpty
           killCsvFileWhereEver g_enums.descriptors(i).sectionIndex, enumName & "_" & tabPrefixNl & suffixText, g_targetDir, j, onlyIfEmpty

           killCsvFileWhereEver g_enums.descriptors(i).sectionIndex, enumName, g_targetDir, j, onlyIfEmpty, "PDM"
           killCsvFileWhereEver g_enums.descriptors(i).sectionIndex, enumName & "_" & tabPrefixNl & suffixText, g_targetDir, j, onlyIfEmpty, "PDM"
           enumName = g_enums.descriptors(i).enumNameDb
         Next k
       Next j
   Next i
 End Sub
 
 
 Sub genEnumCsv( _
   thisEnumIndex As Integer, _
   ddlType As DdlTypeId, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer _
 )
   On Error GoTo ErrorExit

   Dim fileName As String
   Dim fileNameLabel As String
     If g_enums.descriptors(thisEnumIndex).sectionName = "" Or g_enums.descriptors(thisEnumIndex).enumName = "" Then
       GoTo NormalExit
     End If

     fileName = _
       genCsvFileName( _
         g_targetDir, g_enums.descriptors(thisEnumIndex).sectionIndex, g_enums.descriptors(thisEnumIndex).enumNameDb, IIf(g_enums.descriptors(thisEnumIndex).isEnumLang, 0, 1), _
         IIf(g_enums.descriptors(thisEnumIndex).refersToPdm, "PDM", ""), ddlType, _
         thisOrgIndex, thisPoolIndex, g_enums.descriptors(thisEnumIndex).isCommonToOrgs, g_enums.descriptors(thisEnumIndex).isCommonToPools _
       )

     assertDir fileName

     If g_enums.descriptors(thisEnumIndex).idDataType <> etNone Then
       fileNameLabel = _
         genCsvFileName( _
           g_targetDir, g_enums.descriptors(thisEnumIndex).sectionIndex, genNlObjName(g_enums.descriptors(thisEnumIndex).enumNameDb), _
           2, IIf(g_enums.descriptors(thisEnumIndex).refersToPdm, "PDM", ""), ddlType, thisOrgIndex, _
           thisPoolIndex, g_enums.descriptors(thisEnumIndex).isCommonToOrgs, g_enums.descriptors(thisEnumIndex).isCommonToPools _
         )
     End If

     Dim fileNoEnumCsv As Integer
     Dim fileNoEnumCsvOrg As Integer
     Dim thisFileNoCsv As Integer
     fileNoEnumCsv = FreeFile()
     Open fileName For Output As #fileNoEnumCsv
     Dim fileNoEnumLabelCsv As Integer
     Dim fileNoEnumLabelCsvOrg As Integer
     Dim thisFileNoLabelCsv As Integer
     If g_enums.descriptors(thisEnumIndex).idDataType <> etNone Then
       fileNoEnumLabelCsv = FreeFile()
       Open fileNameLabel For Output As #fileNoEnumLabelCsv
     End If
 
     Dim oidStr As String
     Dim valIdStr As String
     Dim extraCommas As String
     Dim thisComma As String
     Dim maxAttrs As Integer
     maxAttrs = IIf(maxAttrsPerEnum < g_enums.descriptors(thisEnumIndex).attrRefs.numDescriptors, maxAttrsPerEnum, g_enums.descriptors(thisEnumIndex).attrRefs.numDescriptors)
     Dim i As Integer, j As Integer, k As Integer
     For i = LBound(g_enums.descriptors(thisEnumIndex).values.vals) To g_enums.descriptors(thisEnumIndex).values.numVals Step 1
       For k = 0 To IIf(thisOrgIndex <= 0 And g_enums.descriptors(thisEnumIndex).values.vals(i).isOrgSpecific And ddlType = edtPdm, g_orgs.numDescriptors, 0)
         If k = 0 And g_enums.descriptors(thisEnumIndex).values.vals(i).isOrgSpecific Then
           GoTo NextK
         End If

         If k > 0 And thisOrgIndex <= 0 And g_enums.descriptors(thisEnumIndex).values.vals(i).isOrgSpecific And ddlType = edtPdm Then
           GoTo NextK
         Else
           oidStr = CStr(g_enums.descriptors(thisEnumIndex).values.vals(i).oid)
           valIdStr = CStr(g_enums.descriptors(thisEnumIndex).values.vals(i).id)

           thisFileNoCsv = fileNoEnumCsv
           thisFileNoLabelCsv = fileNoEnumLabelCsv
         End If

         Dim thisValue As String
         extraCommas = ""
         thisComma = IIf(i = LBound(g_enums.descriptors(thisEnumIndex).values.vals) And g_enums.descriptors(thisEnumIndex).idDataType = etNone, "", ",")

         For j = 1 To maxAttrs
           thisValue = g_enums.descriptors(thisEnumIndex).values.vals(i).attrStrings(j)
           If thisValue = "" Then
             thisValue = g_attributes.descriptors(g_enums.descriptors(thisEnumIndex).attrRefs.descriptors(j).refIndex).default
             If Left(thisValue, 1) = "'" Then
               thisValue = Right(thisValue, Len(thisValue) - 1)
             End If
             If Right(thisValue, 1) = "'" Then
               thisValue = Left(thisValue, Len(thisValue) - 1)
             End If
           End If

           If thisValue <> "" Then
             Select Case g_domains.descriptors(g_attributes.descriptors(g_enums.descriptors(thisEnumIndex).attrRefs.descriptors(j).refIndex).domainIndex).dataType
             Case etChar, etVarchar
               extraCommas = extraCommas & thisComma & """" & thisValue & """"
             Case etDate, etTime, etTimestamp
               If InStr(1, thisValue, "'") > 0 Then
                 thisValue = Right(thisValue, Len(thisValue) - InStr(1, thisValue, "'"))
               End If
               If InStr(1, thisValue, "'") > 0 Then
                 thisValue = "'" & Left(thisValue, InStr(1, thisValue, "'"))
               End If
               If UCase(thisValue) = "CURRENT TIMESTAMP" Then
                 thisValue = Format(Now, "yyyy-MM-DD-00.00.00.000000")
               End If
               extraCommas = extraCommas & thisComma & thisValue
             Case etDecimal, etDouble, etFloat
               extraCommas = extraCommas & thisComma & Replace(thisValue, ",", ".")
             Case Else
               extraCommas = extraCommas & thisComma & thisValue
             End Select
           Else
             extraCommas = extraCommas & thisComma
           End If
         Next j

         If g_enums.descriptors(thisEnumIndex).idDataType <> etNone Then
           Print #thisFileNoLabelCsv, oidStr & "," & valIdStr & "," & g_enums.descriptors(thisEnumIndex).values.vals(i).languageId & ",""" & _
                     Replace(g_enums.descriptors(thisEnumIndex).values.vals(i).valueString, """", """""") & """,1"
         End If

         If g_enums.descriptors(thisEnumIndex).values.vals(i).languageId = gc_langIdGerman Then
           Print #thisFileNoCsv, IIf(g_enums.descriptors(thisEnumIndex).idDataType = etNone, "", valIdStr); extraCommas; ",1"
         End If

         If g_enums.descriptors(thisEnumIndex).values.vals(i).isOrgSpecific Then
           Close #fileNoEnumCsvOrg
           If g_enums.descriptors(thisEnumIndex).idDataType <> etNone Then
             Close #fileNoEnumLabelCsvOrg
           End If
         End If
 NextK:
       Next k
     Next i
 
 NormalExit:
   On Error Resume Next
   Close #fileNoEnumCsv
   Close #fileNoEnumCsvOrg
   Close #fileNoEnumLabelCsv
   Close #fileNoEnumLabelCsvOrg
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub resetEnumsCsvExported()
   Dim i As Integer
     For i = 1 To g_enums.numDescriptors Step 1
       g_enums.descriptors(i).isLdmCsvExported = False
 ' ### IF IVK ###
       g_enums.descriptors(i).isXsdExported = False
 ' ### ENDIF IVK ###
       g_enums.descriptors(i).isCtoAliasCreated = False
     Next i
 End Sub
 
 
 Sub getEnums()
   If (g_enums.numDescriptors = 0) Then
     readSheet

     ' make sure we've read the MPCs and Attributes
     getOrgs
     getAttributes

     Dim enumLangIndex As Integer

     enumLangIndex = getEnumLangIndex()

     Dim i As Integer
     Dim j As Integer
     ' create enum PdmOrganization
     addAttribute exnPdmOrganization, enPdmOrganization, eactEnum, conOrgOid, cosnOrgOid, dxnOid, dnOid
 ' ### IF IVK ###
     addAttribute exnPdmOrganization, enPdmOrganization, eactEnum, conPdmSequenceSchemaName, cosnPdmSequenceSchemaName, snDbMeta, dnDbSchemaName
 ' ### ENDIF IVK ###

       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).sectionName = exnPdmOrganization
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).enumName = enPdmOrganization
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isEnumLang = False
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).shortName = esnPdmOrganization
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).enumId = 999
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).idDomainSection = snMeta
 ' FIXME: get rid of hard-coding
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).idDomainName = dnEnumId
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).maxLength = 20
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isCommonToOrgs = True
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isCommonToPools = True
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).notAcmRelated = True
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).noAlias = False
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isLrtSpecific = False
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isPdmSpecific = False
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).refersToPdm = True
 
       If enumLangIndex > 0 Then
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceData = g_enums.descriptors(enumLangIndex).tabSpaceData
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceIndex = g_enums.descriptors(enumLangIndex).tabSpaceIndex
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceLong = g_enums.descriptors(enumLangIndex).tabSpaceLong
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceNl = g_enums.descriptors(enumLangIndex).tabSpaceNl
       End If

       initEnumVals g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values
       Dim orgOidStr As String
       For i = 1 To g_orgs.numDescriptors
           g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).id = g_orgs.descriptors(i).id
           If g_orgs.descriptors(i).isTemplate Then
             orgOidStr = "" & genTemplateParamWrapper(pullOrgOidByIndex(i), True)

             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).isOrgSpecific = True
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).oid = pullOrgOidByIndex(i)
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).attrStrings(1) = "" & orgOidStr
           Else
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).isOrgSpecific = False
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).oid = pullOid
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).attrStrings(1) = "" & g_orgs.descriptors(i).oid
           End If
           g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).languageId = gc_langIdEnglish
           g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).valueString = g_orgs.descriptors(i).name
 ' ### IF IVK ###
           g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).attrStrings(2) = genSchemaName(snMeta, ssnMeta, edtPdm, i)
 
           For j = 3 To maxAttrsPerEnum Step 1
 ' ### ELSE IVK ###
 '         For j = 2 To maxAttrsPerEnum Step 1
 ' ### ENDIF IVK ###
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).attrStrings(j) = ""
           Next j

         ' We need to have german values - otherwise the enum values do not show up in the CSV file
           g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).id = g_orgs.descriptors(i).id
           If g_orgs.descriptors(i).isTemplate Then
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).isOrgSpecific = True
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).oid = pullOrgOidByIndex(i)
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).attrStrings(1) = orgOidStr
           Else
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).isOrgSpecific = False
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).oid = pullOid
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).attrStrings(1) = "" & g_orgs.descriptors(i).oid
           End If
           g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).languageId = gc_langIdGerman
           g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).valueString = g_orgs.descriptors(i).name
 ' ### IF IVK ###
           g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).attrStrings(2) = genSchemaName(snMeta, ssnMeta, edtPdm, i)
           For j = 3 To maxAttrsPerEnum Step 1
 ' ### ELSE IVK ###
 '         For j = 2 To maxAttrsPerEnum Step 1
 ' ### ENDIF IVK ###
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).attrStrings(j) = ""
           Next j
       Next i

     ' make sure we've read the DataPools
     getDataPools

     ' create enum PdmDataPoolType
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).sectionName = exnPdmDataPoolType
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).enumName = enPdmDataPoolType
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isEnumLang = False
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).shortName = esnPdmDataPoolType
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).enumId = 998
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).idDomainSection = snMeta
 ' FIXME: get rid of hard-coding
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).idDomainName = dnEnumId
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).maxLength = 30
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isCommonToOrgs = True
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isCommonToPools = True
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).notAcmRelated = True
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).noAlias = True
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isLrtSpecific = False
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).isPdmSpecific = False
       g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).refersToPdm = True

       If enumLangIndex > 0 Then
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceData = g_enums.descriptors(enumLangIndex).tabSpaceData
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceIndex = g_enums.descriptors(enumLangIndex).tabSpaceIndex
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceLong = g_enums.descriptors(enumLangIndex).tabSpaceLong
         g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).tabSpaceNl = g_enums.descriptors(enumLangIndex).tabSpaceNl
       End If

       initEnumVals g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values

       For i = 1 To g_pools.numDescriptors
         If g_pools.descriptors(i).supportAcm Then
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).id = g_pools.descriptors(i).id
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).oid = pullOid
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).languageId = gc_langIdEnglish
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).valueString = g_pools.descriptors(i).name
           ' We need to have german values - otherwise the enum values do not show up in the CSV file
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).id = g_pools.descriptors(i).id
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).oid = pullOid
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).languageId = gc_langIdGerman
             g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values.vals(allocEnumValIndex(g_enums.descriptors(allocEnumDescriptorIndex(g_enums)).values)).valueString = g_pools.descriptors(i).name
         End If
       Next i
   End If
 End Sub
 
 
 Sub resetEnums()
   g_enums.numDescriptors = 0
 End Sub
 
 
 Function getQualTabNameLanguageEnum( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 ) As String
   getQualTabNameLanguageEnum = genQualTabNameByEnumIndex(g_enumIndexLanguage, ddlType, thisOrgIndex, thisPoolIndex)
 End Function
 
 
 Function getEnumIndexByName( _
   ByRef sectionName As String, _
   ByRef enumName As String, _
   Optional silent As Boolean = False _
 ) As Integer
   Dim i As Integer
 
   getEnumIndexByName = -1
   getEnums
 
   For i = 1 To g_enums.numDescriptors Step 1
     If UCase(g_enums.descriptors(i).sectionName) = UCase(sectionName) And _
        UCase(g_enums.descriptors(i).enumName) = UCase(enumName) Then
       getEnumIndexByName = i
       Exit Function
     End If
   Next i
 
   If Not silent Then
     logMsg "unable to identify enumeration '" & sectionName & "." & enumName & "'", ellError, edtLdm
   End If
 End Function
 
 
 Function getEnumIndexByI18nId( _
   ByRef i18nId As String _
 ) As Integer
   Dim i As Integer
 
   getEnumIndexByI18nId = -1
 
   For i = 1 To g_enums.numDescriptors Step 1
     If UCase(g_enums.descriptors(i).i18nId) = UCase(i18nId) Then
       getEnumIndexByI18nId = i
       Exit Function
     End If
   Next i
 End Function
 
 
 Function isEnum( _
   ByRef sectionName As String, _
   ByRef enumName As String, _
   Optional ByRef enumIndex As Integer = -1 _
 ) As Boolean
   isEnum = False

   enumIndex = getEnumIndexByName(sectionName, enumName, True)
   If (enumIndex > 0) Then
     isEnum = True
   End If
 End Function
 
 
 Sub evalEnums()
   Dim thisEnumIndex As Integer
   Dim thisAttrIndex As Integer
 
     For thisEnumIndex = 1 To g_enums.numDescriptors Step 1
         g_enums.descriptors(thisEnumIndex).sectionIndex = getSectionIndexByName(g_enums.descriptors(thisEnumIndex).sectionName)
         g_enums.descriptors(thisEnumIndex).sectionShortName = ""
         If g_enums.descriptors(thisEnumIndex).sectionIndex > 0 Then
           g_enums.descriptors(thisEnumIndex).sectionShortName = g_sections.descriptors(g_enums.descriptors(thisEnumIndex).sectionIndex).shortName
         End If
     Next thisEnumIndex

     For thisEnumIndex = 1 To g_enums.numDescriptors Step 1
         g_enums.descriptors(thisEnumIndex).enumIdStr = getEnumIdByIndex(thisEnumIndex)

         g_enums.descriptors(thisEnumIndex).enumIndex = thisEnumIndex
         g_enums.descriptors(thisEnumIndex).enumNameDb = genEnumObjName(g_enums.descriptors(thisEnumIndex).enumName)
         g_enums.descriptors(thisEnumIndex).domainIndexId = getDomainIndexByName(g_enums.descriptors(thisEnumIndex).idDomainSection, g_enums.descriptors(thisEnumIndex).idDomainName)
         g_enums.descriptors(thisEnumIndex).idDataType = g_domains.descriptors(g_enums.descriptors(thisEnumIndex).domainIndexId).dataType
         g_enums.descriptors(thisEnumIndex).attrRefs.numDescriptors = 0
         g_enums.descriptors(thisEnumIndex).refersToPdm = g_enums.descriptors(thisEnumIndex).refersToPdm Or g_enums.descriptors(thisEnumIndex).isPdmSpecific

         For thisAttrIndex = 1 To g_attributes.numDescriptors Step 1
             If UCase(g_enums.descriptors(thisEnumIndex).sectionName) = UCase(g_attributes.descriptors(thisAttrIndex).sectionName) And _
                UCase(g_enums.descriptors(thisEnumIndex).enumName) = UCase(g_attributes.descriptors(thisAttrIndex).className) And _
                g_attributes.descriptors(thisAttrIndex).cType = eactEnum Then

               g_attributes.descriptors(thisAttrIndex).acmEntityIndex = thisEnumIndex
               g_attributes.descriptors(thisAttrIndex).isPdmSpecific = g_attributes.descriptors(thisAttrIndex).isPdmSpecific Or g_enums.descriptors(thisEnumIndex).isPdmSpecific
               If Not g_enums.descriptors(thisEnumIndex).notAcmRelated Then
                 g_attributes.descriptors(thisAttrIndex).isNotAcmRelated = False
               End If

                 If isEnum(g_attributes.descriptors(thisAttrIndex).domainSection, g_attributes.descriptors(thisAttrIndex).domainName) Then
                   g_enums.descriptors(thisEnumIndex).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_enums.descriptors(thisEnumIndex).attrRefs)).refType = eadrtEnum
 ' ### IF IVK ###
                 ElseIf isType(g_attributes.descriptors(thisAttrIndex).domainSection, g_attributes.descriptors(thisAttrIndex).domainName) Then
                   g_enums.descriptors(thisEnumIndex).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_enums.descriptors(thisEnumIndex).attrRefs)).refType = eadrtType
 ' ### ENDIF IVK ###
                 Else
                   g_enums.descriptors(thisEnumIndex).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_enums.descriptors(thisEnumIndex).attrRefs)).refType = eadrtAttribute
                 End If
                 g_enums.descriptors(thisEnumIndex).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_enums.descriptors(thisEnumIndex).attrRefs)).refIndex = thisAttrIndex
             End If
         Next thisAttrIndex
         For thisAttrIndex = 1 To g_enums.descriptors(thisEnumIndex).values.numVals
           If Len(g_enums.descriptors(thisEnumIndex).values.vals(thisAttrIndex).valueString) > g_enums.descriptors(thisEnumIndex).maxLength Then
             logMsg "Enumeration""" & g_enums.descriptors(thisEnumIndex).sectionName & "." & g_enums.descriptors(thisEnumIndex).enumName & " has maximum literal length " & g_enums.descriptors(thisEnumIndex).maxLength & " but literal """ & g_enums.descriptors(thisEnumIndex).values.vals(thisAttrIndex).valueString & """ has length " & Len(g_enums.descriptors(thisEnumIndex).values.vals(thisAttrIndex).valueString), ellError
           End If
         Next thisAttrIndex

 ' ### IF IVK ###
         ' determine whether class supports XML-export
         g_enums.descriptors(thisEnumIndex).supportXmlExport = Not g_enums.descriptors(thisEnumIndex).noXmlExport

 ' ### ENDIF IVK ###
         ' determine TableSpaces
         g_enums.descriptors(thisEnumIndex).tabSpaceIndexData = IIf(g_enums.descriptors(thisEnumIndex).tabSpaceData <> "", getTableSpaceIndexByName(g_enums.descriptors(thisEnumIndex).tabSpaceData), -1)
         g_enums.descriptors(thisEnumIndex).tabSpaceIndexIndex = IIf(g_enums.descriptors(thisEnumIndex).tabSpaceIndex <> "", getTableSpaceIndexByName(g_enums.descriptors(thisEnumIndex).tabSpaceIndex), -1)
         g_enums.descriptors(thisEnumIndex).tabSpaceIndexLong = IIf(g_enums.descriptors(thisEnumIndex).tabSpaceLong <> "", getTableSpaceIndexByName(g_enums.descriptors(thisEnumIndex).tabSpaceLong), -1)
         g_enums.descriptors(thisEnumIndex).tabSpaceIndexNl = IIf(g_enums.descriptors(thisEnumIndex).tabSpaceNl <> "", getTableSpaceIndexByName(g_enums.descriptors(thisEnumIndex).tabSpaceNl), -1)

         If g_enums.descriptors(thisEnumIndex).tabSpaceIndexData > 0 Then
           If g_tableSpaces.descriptors(g_enums.descriptors(thisEnumIndex).tabSpaceIndexData).category = tscSms Then
             If g_enums.descriptors(thisEnumIndex).tabSpaceIndexIndex > 0 And g_enums.descriptors(thisEnumIndex).tabSpaceIndexIndex <> g_enums.descriptors(thisEnumIndex).tabSpaceIndexData Then
               g_enums.descriptors(thisEnumIndex).tabSpaceIndexIndex = g_enums.descriptors(thisEnumIndex).tabSpaceIndexData
               logMsg "index table space """ & g_enums.descriptors(thisEnumIndex).tabSpaceIndex & """ for enum """ & g_enums.descriptors(thisEnumIndex).sectionName & "." & g_enums.descriptors(thisEnumIndex).enumName & """" & _
                      " must be identical to data table space since data table space is ""SMS"" - fixed", ellFixableWarning
             End If
             If g_enums.descriptors(thisEnumIndex).tabSpaceIndexLong > 0 And g_enums.descriptors(thisEnumIndex).tabSpaceIndexLong <> g_enums.descriptors(thisEnumIndex).tabSpaceIndexData Then
               g_enums.descriptors(thisEnumIndex).tabSpaceIndexLong = g_enums.descriptors(thisEnumIndex).tabSpaceIndexData
               logMsg "long table space """ & g_enums.descriptors(thisEnumIndex).tabSpaceLong & """ for enum """ & g_enums.descriptors(thisEnumIndex).sectionName & "." & g_enums.descriptors(thisEnumIndex).enumName & """" & _
                      " must be identical to data table space since data table space is ""SMS"" - fixed", ellFixableWarning
             End If
           End If
         End If

         Dim domainIndexThisEnum As Integer
         domainIndexThisEnum = allocDomainDescriptorIndex(g_domains)
           g_domains.descriptors(domainIndexThisEnum).sectionName = g_enums.descriptors(thisEnumIndex).sectionName
           g_domains.descriptors(domainIndexThisEnum).domainName = "EnumVal" & g_enums.descriptors(thisEnumIndex).enumName
           g_domains.descriptors(domainIndexThisEnum).dataType = etVarchar
           g_domains.descriptors(domainIndexThisEnum).minLength = ""
           g_domains.descriptors(domainIndexThisEnum).maxLength = g_enums.descriptors(thisEnumIndex).maxLength
           g_domains.descriptors(domainIndexThisEnum).scale = 0
           g_domains.descriptors(domainIndexThisEnum).minValue = ""
           g_domains.descriptors(domainIndexThisEnum).maxValue = ""
           g_domains.descriptors(domainIndexThisEnum).valueList = ""
           g_domains.descriptors(domainIndexThisEnum).constraint = ""
           g_domains.descriptors(domainIndexThisEnum).notLogged = False
           g_domains.descriptors(domainIndexThisEnum).notCompact = False
           g_domains.descriptors(domainIndexThisEnum).supportUnicode = False
           g_domains.descriptors(domainIndexThisEnum).unicodeExpansionFactor = 1
           g_domains.descriptors(domainIndexThisEnum).isGenerated = True

           g_domains.descriptors(domainIndexThisEnum).domainIndex = domainIndexThisEnum

         g_enums.descriptors(thisEnumIndex).domainIndexValue = domainIndexThisEnum
     Next thisEnumIndex
 End Sub
 
