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
          logMsg "invalid language ID '" & thisSheet.Cells(thisRow - 1, i) & "' found in sheet '" & thisSheet.name & "' (column" & colFirstLang + i - 1 & ")", ellError
        End If
      Next i
    End If
  End If

  If numLangsForAttributesNl > 0 Then
    While thisSheet.Cells(thisRow, colI18nId) & "" <> ""
      If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
        GoTo NextRow
      End If
      
      With g_attributesNl.descriptors(allocAttributeNlDescriptorIndex(g_attributesNl))
        .i18nId = Trim(thisSheet.Cells(thisRow, colI18nId))
        For i = 1 To numLangsForAttributesNl
          .nl(i) = Trim(thisSheet.Cells(thisRow, colFirstLang + i - 1))
        Next i
      End With

NextRow:
      thisRow = thisRow + 1
    Wend
  End If
End Sub


Sub getAttributesNl()
  If g_attributesNl.numDescriptors = 0 Then
    readSheet
  End If
End Sub


Sub resetAttributesNl()
  g_attributesNl.numDescriptors = 0
  isInitialized = False
End Sub


Sub evalAttributesNl()
  Dim i As Integer, j As Integer
  With g_attributesNl
    For i = 1 To .numDescriptors Step 1
      With .descriptors(i)
        .attributeIndex = getAttributeIndexByI18nId(.i18nId)
        If .attributeIndex > 0 Then
          g_attributes.descriptors(.attributeIndex).attrNlIndex = i
        End If
      End With
    Next i
  End With
End Sub


Sub dropAttributeNlCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbMeta, genNlObjName(clnAcmAttribute), g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM"
End Sub


Sub genAttributeNlAcmMetaCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, genNlObjName(clnAcmAttribute), acmCsvProcessingStep, "ACM", ddlType)
  assertDir fileName
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
    With g_attributesNl.descriptors(i)
      For j = 1 To numLangsForAttributesNl
        If .nl(j) <> "" And .attributeIndex > 0 Then
          With g_attributes.descriptors(.attributeIndex)
' ### IF IVK ###
            If Not .isNotAcmRelated And (.cType <> eactType) Then
              Dim effectiveAttrName As String
              Dim k As Integer
              For k = 1 To IIf(.isExpression, 2, 1)
                If k = 1 Then
                  effectiveAttrName = genAttrName(.attributeName, ddlType, , , , , , False)
                Else
                  effectiveAttrName = genSurrogateKeyName(ddlType, .shortName & "EXP")
                End If
                
                Print #fileNo, """"; UCase(effectiveAttrName); """,";
' ### ELSE IVK ###
'           If Not .isNotAcmRelated Then
' ### INDENT IVK ### -2
'               Print #fileNo, """"; UCase(.attributeName); """,";
' ### ENDIF IVK ###
                Print #fileNo, """"; UCase(.sectionName); """,";
                Print #fileNo, """"; UCase(.className); """,";
                Print #fileNo, """"; getAcmEntityTypeKey(.cType); """,";
                Print #fileNo, CStr(j); ",";
                Print #fileNo, """"; g_attributesNl.descriptors(i).nl(j); """,";
                Print #fileNo, getCsvTrailer(0)
' ### IF IVK ###
              
                If .isNationalizable Then
                  Print #fileNo, """"; UCase(genAttrName(effectiveAttrName, ddlType, , , , , True, False)); """,";
                  Print #fileNo, """"; UCase(.sectionName); """,";
                  Print #fileNo, """"; UCase(.className); """,";
                  Print #fileNo, """"; getAcmEntityTypeKey(.cType); """,";
                  Print #fileNo, CStr(j); ",";
                  Print #fileNo, """"; g_attributesNl.descriptors(i).nl(j) & natNlSuffixes(j); """,";
                  Print #fileNo, getCsvTrailer(0)
                  Print #fileNo, """"; UCase(genAttrName(effectiveAttrName & gc_anSuffixNatActivated, ddlType, , , , , , False)); """,";
                  Print #fileNo, """"; UCase(.sectionName); """,";
                  Print #fileNo, """"; UCase(.className); """,";
                  Print #fileNo, """"; getAcmEntityTypeKey(.cType); """,";
                  Print #fileNo, CStr(j); ",";
                  Print #fileNo, """"; g_attributesNl.descriptors(i).nl(j) & isNatActiveNlSuffixes(j); """,";
                  Print #fileNo, getCsvTrailer(0)
                End If
              Next k
' ### ELSE IVK ###
' ### INDENT IVK ### 0
' ### ENDIF IVK ###
            End If
          End With
        End If
      Next j
    End With
  Next i

  For i = 1 To g_classes.numDescriptors
    With g_classes.descriptors(i)
      If Not .notAcmRelated And .superClassIndex <= 0 Then
        ' surrogate key
        If .useSurrogateKey Then
          Print #fileNo, """"; UCase(conOid); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Objekt ID"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conOid); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""Object ID"","; getCsvTrailer(0)
        End If
        ' validFrom / validTo
        If .isGenForming Then
          Print #fileNo, """"; UCase(conValidFrom); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Gültig von"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conValidFrom); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""Valid from"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conValidTo); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Gültig bis"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conValidTo); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""Valid to"","; getCsvTrailer(0)
        End If
        If .logLastChange Then
          Print #fileNo, """"; UCase(conCreateTimestamp); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Erstellungszeitpunkt"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conCreateTimestamp); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""Create Timestamp"","; getCsvTrailer(0)
          
          Print #fileNo, """"; UCase(conCreateUser); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Ersteller"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conCreateUser); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""Create user"","; getCsvTrailer(0)
          
          Print #fileNo, """"; UCase(conLastUpdateTimestamp); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Zeitpunkt"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conLastUpdateTimestamp); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""Timestamp"","; getCsvTrailer(0)
          
          Print #fileNo, """"; UCase(conUpdateUser); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Benutzer"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conUpdateUser); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""User"","; getCsvTrailer(0)
        End If
' ### IF IVK ###
        ' isNational
        If .isNationalizable Then
          Print #fileNo, """"; UCase(conIsNational); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Nationalisiert"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conIsNational); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""Nationalized"","; getCsvTrailer(0)
        End If
        ' hasBeenSetProductive-tag
        If .isUserTransactional Then
          Print #fileNo, """"; UCase(conHasBeenSetProductive); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Produktivgestellt"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conHasBeenSetProductive); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""Set productive"","; getCsvTrailer(0)
        End If
        ' PS-tag
        If .isPsTagged Then
          Print #fileNo, """"; UCase(conPsOid); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdGerman); ",""Produktstruktur OID"","; getCsvTrailer(0)
          Print #fileNo, """"; UCase(conPsOid); ""","; """"; UCase(.sectionName); ""","; _
                         """"; UCase(.className); ""","; """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(gc_langIdEnglish); ",""Productstructure OID"","; getCsvTrailer(0)
        End If
' ### ENDIF IVK ###
      End If
    End With
  Next i
  
  For i = 1 To g_relationships.numDescriptors
    With g_relationships.descriptors(i)
If .relName = "BinaryPropertyValue" Then
Dim rel As RelationshipDescriptor
rel = g_relationships.descriptors(i)
End If
      If Not .notAcmRelated And .reusedRelIndex <= 0 Then
        If .implementsInOwnTable Then
          If useSurrogateKeysForNMRelationships Then
            Print #fileNo, """"; UCase(conOid); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdGerman); ",""Beziehungs-ID"","; getCsvTrailer(0)
            Print #fileNo, """"; UCase(conOid); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdEnglish); ",""Relationship ID"","; getCsvTrailer(0)
          End If
        
          ' createTimestamp, LastUpdateTimestamp, etc
          If .logLastChange Then
            Print #fileNo, """"; UCase(conCreateTimestamp); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdGerman); ",""Erstellungszeitpunkt"","; getCsvTrailer(0)
            Print #fileNo, """"; UCase(conCreateTimestamp); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdEnglish); ",""Create Timestamp"","; getCsvTrailer(0)
            
            Print #fileNo, """"; UCase(conCreateUser); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdGerman); ",""Ersteller"","; getCsvTrailer(0)
            Print #fileNo, """"; UCase(conCreateUser); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdEnglish); ",""Create user"","; getCsvTrailer(0)
            
            Print #fileNo, """"; UCase(conLastUpdateTimestamp); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdGerman); ",""Zeitpunkt"","; getCsvTrailer(0)
            Print #fileNo, """"; UCase(conLastUpdateTimestamp); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdEnglish); ",""Timestamp"","; getCsvTrailer(0)
            
            Print #fileNo, """"; UCase(conUpdateUser); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdGerman); ",""Benutzer"","; getCsvTrailer(0)
            Print #fileNo, """"; UCase(conUpdateUser); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdEnglish); ",""User"","; getCsvTrailer(0)
          End If
' ### IF IVK ###
        
          ' PS-tag
          If .isPsTagged Then
            Print #fileNo, """"; UCase(conPsOid); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdGerman); ",""Produktstruktur OID"","; getCsvTrailer(0)
            Print #fileNo, """"; UCase(conPsOid); ""","; """"; UCase(.sectionName); ""","; _
                           """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                           CStr(gc_langIdEnglish); ",""Productstructure OID"","; getCsvTrailer(0)
          End If
            
          If .relNlIndex > 0 Then
            If g_relationshipsNl.descriptors(.relNlIndex).nl(gc_langIdGerman) <> "" Then
              Print #fileNo, """"; .leftFkColName(ddlType); ""","; """"; UCase(.sectionName); ""","; _
                             """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                             CStr(gc_langIdGerman); ","; _
                             """"; g_relationshipsNl.descriptors(.relNlIndex).nl(gc_langIdGerman); ""","; _
                             getCsvTrailer(0)
            End If
            If g_relationshipsNl.descriptors(.relNlIndex).nl(gc_langIdEnglish) <> "" Then
              Print #fileNo, """"; .leftFkColName(ddlType); ""","; """"; UCase(.sectionName); ""","; _
                             """"; UCase(.relName); ""","; """"; gc_acmEntityTypeKeyRel; ""","; _
                             CStr(gc_langIdEnglish); ","; _
                             """"; g_relationshipsNl.descriptors(.relNlIndex).nl(gc_langIdEnglish); ""","; _
                             getCsvTrailer(0)
            End If
          End If
' ### ENDIF IVK ###
                ' not .implementsInOwnTable
        ElseIf (.relNlIndex > 0) Then
          Dim entityIdImplementingFk
          entityIdImplementingFk = IIf(.implementsInEntity = ernmLeft, .leftEntityIndex, IIf(.implementsInEntity = ernmRight, .rightEntityIndex, -1))
          
          If entityIdImplementingFk > 0 Then
            Dim fkColName As String
            Dim relNlIndex As Integer
            Dim skip As Boolean
            skip = False
            
            If .reusedRelIndex > 0 Then
              If .implementsInEntity = ernmLeft Then
                If .leftEntityType = eactClass Then
                  If g_classes.descriptors(.leftEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(g_relationships.descriptors(.reusedRelIndex).leftEntityIndex).orMappingSuperClassIndex Then
                    skip = True
                  End If
                ElseIf .leftEntityIndex = g_relationships.descriptors(.reusedRelIndex).leftEntityIndex Then
                  skip = True
                End If
              ElseIf .implementsInEntity = ernmRight Then
                If .rightEntityType = eactClass Then
                  If g_classes.descriptors(.rightEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(g_relationships.descriptors(.reusedRelIndex).rightEntityIndex).orMappingSuperClassIndex Then
                    skip = True
                  End If
                ElseIf .rightEntityIndex = g_relationships.descriptors(.reusedRelIndex).rightEntityIndex Then
                  skip = True
                End If
              End If
            End If
            
            Dim entityTypeImplementingFk As AcmAttrContainerType
            
            If (supportColumnIsInstantiatedInAcmAttribute Or Not skip) And Not .isReusedInSameEntity Then
              With g_relationships.descriptors(i)
                If .implementsInEntity = ernmLeft Then
                  fkColName = .rightFkColName(ddlType)
                  entityTypeImplementingFk = .leftEntityType
                Else
                  fkColName = .leftFkColName(ddlType)
                  entityTypeImplementingFk = .rightEntityType
                End If
                
                relNlIndex = .relNlIndex
              End With
              
              If entityTypeImplementingFk = eactClass Then
                With g_classes.descriptors(entityIdImplementingFk)
                  For j = 1 To numLangsForAttributesNl
                    If g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(j) <> "" Then
                      Print #fileNo, """"; fkColName; """,";
                      Print #fileNo, """"; UCase(.sectionName); """,";
                      Print #fileNo, """"; UCase(.className); """,";
                      Print #fileNo, """"; gc_acmEntityTypeKeyClass; """,";
                      Print #fileNo, CStr(j); ",";
                      Print #fileNo, """"; g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(j); """,";
                      Print #fileNo, getCsvTrailer(0)
                    End If
                  Next j
               End With
              ElseIf entityTypeImplementingFk = eactRelationship Then
                With g_relationships.descriptors(entityIdImplementingFk)
                  For j = 1 To numLangsForAttributesNl
                    If g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(j) <> "" Then
                      Print #fileNo, """"; fkColName; """,";
                      Print #fileNo, """"; UCase(.sectionName); """,";
                      Print #fileNo, """"; UCase(.relName); """,";
                      Print #fileNo, """"; gc_acmEntityTypeKeyRel; """,";
                      Print #fileNo, CStr(j); ",";
                      Print #fileNo, """"; g_relationshipsNl.descriptors(g_relationships.descriptors(i).relNlIndex).nl(j); """,";
                      Print #fileNo, getCsvTrailer(0)
                    End If
                  Next j
               End With
              End If
            End If
          End If
        
        End If
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

