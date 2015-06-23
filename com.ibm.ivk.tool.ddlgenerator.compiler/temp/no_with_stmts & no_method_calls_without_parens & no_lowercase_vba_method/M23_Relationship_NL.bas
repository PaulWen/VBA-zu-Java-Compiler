 Attribute VB_Name = "M23_Relationship_NL"
 Option Explicit
 
 Private Const colI18nId = colRelI18nId
 Private Const colFirstLang = colI18nId + 1
 
 Private langIds() As Integer
 
 Private Const firstRow = 4
 
 Private Const sheetName = "Rel"
 
 Global numLangsForRelationshipsNl As Integer
 Private isIntialized As Boolean
 
 Private Const acmCsvProcessingStep = 2
 
 Global g_relationshipsNl As RelationshipNlDescriptors
 
 
 Private Sub readSheet()
   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))

   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   If Not isInitialized Then
     numLangsForRelationshipsNl = 0

     While thisSheet.Cells(thisRow - 1, colFirstLang + numLangsForRelationshipsNl) & "" <> ""
       numLangsForRelationshipsNl = numLangsForRelationshipsNl + 1
     Wend
     If numLangsForRelationshipsNl > 0 Then
       ReDim langIds(1 To numLangsForRelationshipsNl)
     End If

     Dim i As Integer
     For i = 1 To numLangsForRelationshipsNl
       langIds(i) = getInteger(thisSheet.Cells(thisRow - 1, colFirstLang + i - 1))

       If langIds(i) < 0 Then
         logMsg("invalid language ID '" & thisSheet.Cells(thisRow - 1, i) & "' found in sheet '" & thisSheet.Name & "' (column" & colFirstLang + i - 1 & "", ellError)
       End If
     Next i
   End If
 
   If numLangsForRelationshipsNl > 0 Then
     While thisSheet.Cells(thisRow, colI18nId) & "" <> ""
         g_relationshipsNl.descriptors(allocRelationshipNlDescriptorIndex(g_relationshipsNl)).i18nId = Trim(thisSheet.Cells(thisRow, colI18nId))
         For i = 1 To numLangsForRelationshipsNl
           g_relationshipsNl.descriptors(allocRelationshipNlDescriptorIndex(g_relationshipsNl)).nl(i) = Trim(thisSheet.Cells(thisRow, colFirstLang + i - 1))
         Next i
       thisRow = thisRow + 1
     Wend
   End If
 End Sub
 
 
 Sub getRelationshipsNl()
   If g_relationshipsNl.numDescriptors = 0 Then
     readSheet()
   End If
 End Sub
 
 
 Sub resetRelationshipsNl()
   g_relationshipsNl.numDescriptors = 0
   isInitialized = False
 End Sub
 
 
 Sub evalRelationshipsNl()
   Dim i As Integer, j As Integer
     For i = 1 To g_relationshipsNl.numDescriptors Step 1
         g_relationshipsNl.descriptors(i).relationshipIndex = getRelIndexByI18nId(g_relationshipsNl.descriptors(i).i18nId)
         If g_relationshipsNl.descriptors(i).relationshipIndex > 0 Then
           g_relationships.descriptors(g_relationshipsNl.descriptors(i).relationshipIndex).relNlIndex = i
         End If
     Next i
 End Sub
 
 
 Sub dropRelationshipsNlCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   killCsvFileWhereEver(g_sectionIndexDbMeta, genNlObjName(clnAcmEntity), g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM")
 End Sub
 
 
 Sub genRelationshipNlAcmMetaCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, genNlObjName(clnAcmEntity), acmCsvProcessingStep, "ACM", ddlType)
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim thisRelNlIndex As Integer
   Dim thisLangId As Integer
   For thisRelNlIndex = 1 To g_relationshipsNl.numDescriptors
       For thisLangId = 1 To numLangsForRelationshipsNl
         If g_relationshipsNl.descriptors(thisRelNlIndex).nl(thisLangId) <> "" And g_relationshipsNl.descriptors(thisRelNlIndex).relationshipIndex > 0 Then
           Print #fileNo, """"; UCase(g_relationships.descriptors(g_relationshipsNl.descriptors(thisRelNlIndex).relationshipIndex).sectionName); ""","; _
                          """"; UCase(g_relationships.descriptors(g_relationshipsNl.descriptors(thisRelNlIndex).relationshipIndex).relName); ""","; _
                          """R"","; _
                          CStr(thisLangId); ","; _
                          """"; g_relationshipsNl.descriptors(thisRelNlIndex).nl(thisLangId); ""","; _
                          getCsvTrailer(0)
         End If
       Next thisLangId
   Next thisRelNlIndex
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
