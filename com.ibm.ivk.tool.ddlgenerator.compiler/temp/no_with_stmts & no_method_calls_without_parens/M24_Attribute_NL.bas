 Attribute VB_Name = "M24_Attribute_NL"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colI18nId = colAttrI18nId
 Private Const colFirstLang = colI18nId + 1
 
 Private langIds() As Integer
 
 Private Const firstRow = 4
 
 Private Const sheetName = "Attr"
 
 Global numLangsForAttributesNl As Integer
 Private isIntialized As Boolean
 
 Private Const acmCsvProcessingStep = 5
 
 Global g_attributesNl As AttributeNlDescriptors
 
 
 Private Sub readSheet()
   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))

   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   If Not isInitialized Then
     numLangsForAttributesNl = 0

     While thisSheet.Cells(thisRow - 1, colFirstLang + numLangsForAttributesNl) & "" <> ""
       numLangsForAttributesNl = numLangsForAttributesNl + 1
     Wend
     If numLangsForAttributesNl > 0 Then
       ReDim langIds(1 To numLangsForAttributesNl)
       Dim i As Integer
       For i = 1 To numLangsForAttributesNl
         langIds(i) = getInteger(thisSheet.Cells(thisRow - 1, colFirstLang + i - 1))

         If langIds(i) < 0 Then
           logMsg("invalid language ID '" & thisSheet.Cells(thisRow - 1, i) & "' found in sheet '" & thisSheet.name & "' (column" & colFirstLang + i - 1 & ")", ellError)
         End If
       Next i
     End If
   End If
 
   If numLangsForAttributesNl > 0 Then
     While thisSheet.Cells(thisRow, colI18nId) & "" <> ""
       If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
         GoTo NextRow
       End If

         g_attributesNl.descriptors(allocAttributeNlDescriptorIndex(g_attributesNl)).i18nId = Trim(thisSheet.Cells(thisRow, colI18nId))
         For i = 1 To numLangsForAttributesNl
           g_attributesNl.descriptors(allocAttributeNlDescriptorIndex(g_attributesNl)).nl(i) = Trim(thisSheet.Cells(thisRow, colFirstLang + i - 1))
         Next i
 
 NextRow:
       thisRow = thisRow + 1
     Wend
   End If
 End Sub
 
 
 Sub getAttributesNl()
   If g_attributesNl.numDescriptors = 0 Then
     readSheet()
   End If
 End Sub
 
 
 Sub resetAttributesNl()
   g_attributesNl.numDescriptors = 0
   isInitialized = False
 End Sub
 
 
 Sub evalAttributesNl()
   Dim i As Integer, j As Integer
     For i = 1 To g_attributesNl.numDescriptors Step 1
         g_attributesNl.descriptors(i).attributeIndex = getAttributeIndexByI18nId(g_attributesNl.descriptors(i).i18nId)
         If g_attributesNl.descriptors(i).attributeIndex > 0 Then
           g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).attrNlIndex = i
         End If
     Next i
 End Sub
 
 
 Sub dropAttributeNlCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   killCsvFileWhereEver(g_sectionIndexDbMeta, genNlObjName(clnAcmAttribute), g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM")
 End Sub
 
 
 Sub genAttributeNlAcmMetaCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, genNlObjName(clnAcmAttribute), acmCsvProcessingStep, "ACM", ddlType)
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

 ' ### IF IVK ###
   ' FIXME: some hard-coding for NL-Text-Suffixes
   Dim natNlSuffixes() As String
   ReDim natNlSuffixes(1 To numLangsForAttributesNl)
   Dim isNatActiveNlSuffixes() As String
   ReDim isNatActiveNlSuffixes(1 To numLangsForAttributesNl)

   natNlSuffixes(1) = " (national)"
   isNatActiveNlSuffixes(1) = " (national aktiv)"
   If (numLangsForAttributesNl > 1) Then
     natNlSuffixes(2) = " (national)"
     isNatActiveNlSuffixes(2) = " (national active)"
   End If

 ' ### ENDIF IVK ###
   Dim i As Integer, j As Integer
   For i = 1 To g_attributesNl.numDescriptors
       For j = 1 To numLangsForAttributesNl
         If g_attributesNl.descriptors(i).nl(j) <> "" And g_attributesNl.descriptors(i).attributeIndex > 0 Then
 ' ### IF IVK ###
             If Not g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).isNotAcmRelated And (g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).cType <> eactType) Then
               Dim effectiveAttrName As String
               Dim k As Integer
               For k = 1 To IIf(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).isExpression, 2, 1)
                 If k = 1 Then
                   effectiveAttrName = genAttrName(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).attributeName, ddlType, , , , , , False)
                 Else
                   effectiveAttrName = genSurrogateKeyName(ddlType, g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).shortName & "EXP")
                 End If

                 Print #fileNo, """"; UCase(effectiveAttrName); """,";
 ' ### ELSE IVK ###
 '           If Not .isNotAcmRelated Then
 ' ### INDENT IVK ### -2
 '               Print #fileNo, """"; UCase(.attributeName); """,";
 ' ### ENDIF IVK ###
                 Print #fileNo, """"; UCase(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).sectionName); """,";
                 Print #fileNo, """"; UCase(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).className); """,";
                 Print #fileNo, """"; getAcmEntityTypeKey(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).cType); """,";
                 Print #fileNo, CStr(j); ",";
                 Print #fileNo, """"; g_attributesNl.descriptors(i).nl(j); """,";
                 Print #fileNo, getCsvTrailer(0)
 ' ### IF IVK ###

                 If g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).isNationalizable Then
                   Print #fileNo, """"; UCase(genAttrName(effectiveAttrName, ddlType, , , , , True, False)); """,";
                   Print #fileNo, """"; UCase(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).sectionName); """,";
                   Print #fileNo, """"; UCase(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).className); """,";
                   Print #fileNo, """"; getAcmEntityTypeKey(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).cType); """,";
                   Print #fileNo, CStr(j); ",";
                   Print #fileNo, """"; g_attributesNl.descriptors(i).nl(j) & natNlSuffixes(j); """,";
                   Print #fileNo, getCsvTrailer(0)
                   Print #fileNo, """"; UCase(genAttrName(effectiveAttrName & gc_anSuffixNatActivated, ddlType, , , , , , False)); """,";
                   Print #fileNo, """"; UCase(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).sectionName); """,";
                   Print #fileNo, """"; UCase(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).className); """,";
                   Print #fileNo, """"; getAcmEntityTypeKey(g_attributes.descriptors(g_attributesNl.descriptors(i).attributeIndex).cType); """,";
                   Print #fileNo, CStr(j); ",";
                   Print #fileNo, """"; g_attributesNl.descriptors(i).nl(j) & isNatActiveNlSuffixes(j); """,";
                   Print #fileNo, getCsvTrailer(0)
                 End If
               Next k
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
             End If
         End If
       Next j
   Next i
 
   For i = 1 To g_classes.numDescriptors
       If Not g_classes.descriptors(i).notAcmRelated And g_classes.descriptors(i).superClassIndex <= 0 Then
         ' surrogate key
         If g_classes.descriptors(i).useSurrogateKey Then
           Print #fileNo, """"; UCase(conOid); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Objekt ID"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conOid); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""Object ID"","; getCsvTrailer(0)
         End If
         ' validFrom / validTo
         If g_classes.descriptors(i).isGenForming Then
           Print #fileNo, """"; UCase(conValidFrom); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Gültig von"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conValidFrom); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""Valid from"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conValidTo); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Gültig bis"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conValidTo); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""Valid to"","; getCsvTrailer(0)
         End If
         If g_classes.descriptors(i).logLastChange Then
           Print #fileNo, """"; UCase(conCreateTimestamp); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Erstellungszeitpunkt"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conCreateTimestamp); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""Create Timestamp"","; getCsvTrailer(0)

           Print #fileNo, """"; UCase(conCreateUser); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Ersteller"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conCreateUser); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""Create user"","; getCsvTrailer(0)

           Print #fileNo, """"; UCase(conLastUpdateTimestamp); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Zeitpunkt"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conLastUpdateTimestamp); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""Timestamp"","; getCsvTrailer(0)

           Print #fileNo, """"; UCase(conUpdateUser); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Benutzer"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conUpdateUser); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""User"","; getCsvTrailer(0)
         End If
 ' ### IF IVK ###
         ' isNational
         If g_classes.descriptors(i).isNationalizable Then
           Print #fileNo, """"; UCase(conIsNational); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Nationalisiert"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conIsNational); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""Nationalized"","; getCsvTrailer(0)
         End If
         ' hasBeenSetProductive-tag
         If g_classes.descriptors(i).isUserTransactional Then
           Print #fileNo, """"; UCase(conHasBeenSetProductive); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Produktivgestellt"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conHasBeenSetProductive); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""Set productive"","; getCsvTrailer(0)
         End If
         ' PS-tag
         If g_classes.descriptors(i).isPsTagged Then
           Print #fileNo, """"; UCase(conPsOid); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdGerman); ",""Produktstruktur OID"","; getCsvTrailer(0)
           Print #fileNo, """"; UCase(conPsOid); ""","; """"; UCase(g_classes.descriptors(i).sectionName); ""","; _
                          """"; UCase(g_classes.descriptors(i).className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                          CStr(gc_langIdEnglish); ",""Productstructure OID"","; getCsvTrailer(0)
         End If
 ' ### ENDIF IVK ###
       End If
   Next i

   For i = 1 To g_relationships.numDescriptors
 If g_relationships.descriptors(i).relName = "BinaryPropertyValue" Then
 Dim rel As RelationshipDescriptor
 rel = g_relationships.descriptors(i)
 End If
       If Not g_relationships.descriptors(i).notAcmRelated And g_relationships.descriptors(i).reusedRelIndex <= 0 Then
         If g_relationships.descriptors(i).implementsInOwnTable Then
           If useSurrogateKeysForNMRelationships Then
             Print #fileNo, """"; UCase(conOid); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdGerman); ",""Beziehungs-ID"","; getCsvTrailer(0)
             Print #fileNo, """"; UCase(conOid); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdEnglish); ",""Relationship ID"","; getCsvTrailer(0)
           End If

           ' createTimestamp, LastUpdateTimestamp, etc
           If g_relationships.descriptors(i).logLastChange Then
             Print #fileNo, """"; UCase(conCreateTimestamp); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdGerman); ",""Erstellungszeitpunkt"","; getCsvTrailer(0)
             Print #fileNo, """"; UCase(conCreateTimestamp); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdEnglish); ",""Create Timestamp"","; getCsvTrailer(0)

             Print #fileNo, """"; UCase(conCreateUser); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdGerman); ",""Ersteller"","; getCsvTrailer(0)
             Print #fileNo, """"; UCase(conCreateUser); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdEnglish); ",""Create user"","; getCsvTrailer(0)

             Print #fileNo, """"; UCase(conLastUpdateTimestamp); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdGerman); ",""Zeitpunkt"","; getCsvTrailer(0)
             Print #fileNo, """"; UCase(conLastUpdateTimestamp); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdEnglish); ",""Timestamp"","; getCsvTrailer(0)

             Print #fileNo, """"; UCase(conUpdateUser); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdGerman); ",""Benutzer"","; getCsvTrailer(0)
             Print #fileNo, """"; UCase(conUpdateUser); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdEnglish); ",""User"","; getCsvTrailer(0)
           End If
 ' ### IF IVK ###

           ' PS-tag
           If g_relationships.descriptors(i).isPsTagged Then
             Print #fileNo, """"; UCase(conPsOid); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdGerman); ",""Produktstruktur OID"","; getCsvTrailer(0)
             Print #fileNo, """"; UCase(conPsOid); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                            """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                            CStr(gc_langIdEnglish); ",""Productstructure OID"","; getCsvTrailer(0)
           End If

           If g_relationships.descriptors(i).relNlIndex > 0 Then
             If g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(gc_langIdGerman) <> "" Then
               Print #fileNo, """"; g_relationships.descriptors(i).leftFkColName(ddlType); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                              """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                              CStr(gc_langIdGerman); ","; _
                              """"; g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(gc_langIdGerman); ""","; _
                              getCsvTrailer(0)
             End If
             If g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(gc_langIdEnglish) <> "" Then
               Print #fileNo, """"; g_relationships.descriptors(i).leftFkColName(ddlType); ""","; """"; UCase(g_relationships.descriptors(i).sectionName); ""","; _
                              """"; UCase(g_relationships.descriptors(i).relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                              CStr(gc_langIdEnglish); ","; _
                              """"; g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(gc_langIdEnglish); ""","; _
                              getCsvTrailer(0)
             End If
           End If
 ' ### ENDIF IVK ###
                 ' not .implementsInOwnTable
         ElseIf (g_relationships.descriptors(i).relNlIndex > 0) Then
           Dim entityIdImplementingFk
           entityIdImplementingFk = IIf(g_relationships.descriptors(i).implementsInEntity = ernmLeft, g_relationships.descriptors(i).leftEntityIndex, IIf(g_relationships.descriptors(i).implementsInEntity = ernmRight, g_relationships.descriptors(i).rightEntityIndex, -1))

           If entityIdImplementingFk > 0 Then
             Dim fkColName As String
             Dim relNlIndex As Integer
             Dim skip As Boolean
             skip = False

             If g_relationships.descriptors(i).reusedRelIndex > 0 Then
               If g_relationships.descriptors(i).implementsInEntity = ernmLeft Then
                 If g_relationships.descriptors(i).leftEntityType = eactClass Then
                   If g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(g_relationships.descriptors(g_relationships.descriptors(i).reusedRelIndex).leftEntityIndex).orMappingSuperClassIndex Then
                     skip = True
                   End If
                 ElseIf g_relationships.descriptors(i).leftEntityIndex = g_relationships.descriptors(g_relationships.descriptors(i).reusedRelIndex).leftEntityIndex Then
                   skip = True
                 End If
               ElseIf g_relationships.descriptors(i).implementsInEntity = ernmRight Then
                 If g_relationships.descriptors(i).rightEntityType = eactClass Then
                   If g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(g_relationships.descriptors(g_relationships.descriptors(i).reusedRelIndex).rightEntityIndex).orMappingSuperClassIndex Then
                     skip = True
                   End If
                 ElseIf g_relationships.descriptors(i).rightEntityIndex = g_relationships.descriptors(g_relationships.descriptors(i).reusedRelIndex).rightEntityIndex Then
                   skip = True
                 End If
               End If
             End If

             Dim entityTypeImplementingFk As AcmAttrContainerType

             If (supportColumnIsInstantiatedInAcmAttribute Or Not skip) And Not g_relationships.descriptors(i).isReusedInSameEntity Then
                 If g_relationships.descriptors(i).implementsInEntity = ernmLeft Then
                   fkColName = g_relationships.descriptors(i).rightFkColName(ddlType)
                   entityTypeImplementingFk = g_relationships.descriptors(i).leftEntityType
                 Else
                   fkColName = g_relationships.descriptors(i).leftFkColName(ddlType)
                   entityTypeImplementingFk = g_relationships.descriptors(i).rightEntityType
                 End If

                 relNlIndex = g_relationships.descriptors(i).relNlIndex

               If entityTypeImplementingFk = eactClass Then
                   For j = 1 To numLangsForAttributesNl
                     If g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(j) <> "" Then
                       Print #fileNo, """"; fkColName; """,";
                       Print #fileNo, """"; UCase(g_classes.descriptors(entityIdImplementingFk).sectionName); """,";
                       Print #fileNo, """"; UCase(g_classes.descriptors(entityIdImplementingFk).className); """,";
                       Print #fileNo, """"; gc_acmEntityTypeKeyClass; """,";
                       Print #fileNo, CStr(j); ",";
                       Print #fileNo, """"; g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(j); """,";
                       Print #fileNo, getCsvTrailer(0)
                     End If
                   Next j
               ElseIf entityTypeImplementingFk = eactRelationship Then
                   For j = 1 To numLangsForAttributesNl
                     If g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(j) <> "" Then
                       Print #fileNo, """"; fkColName; """,";
                       Print #fileNo, """"; UCase(g_relationships.descriptors(entityIdImplementingFk).sectionName); """,";
                       Print #fileNo, """"; UCase(g_relationships.descriptors(entityIdImplementingFk).relName); """,";
                       Print #fileNo, """"; gc_acmEntityTypeKeyRel; """,";
                       Print #fileNo, CStr(j); ",";
                       Print #fileNo, """"; g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(j); """,";
                       Print #fileNo, getCsvTrailer(0)
                     End If
                   Next j
               End If
             End If
           End If

         End If
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
 
