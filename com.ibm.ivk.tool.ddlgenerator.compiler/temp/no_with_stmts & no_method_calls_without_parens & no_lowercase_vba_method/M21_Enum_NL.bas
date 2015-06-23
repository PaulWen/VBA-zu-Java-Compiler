 Attribute VB_Name = "M21_Enum_NL"
 Option Explicit
 
 Private Const colEntryFilter = 1
 
 Private Const colI18nId = 4
 Private Const colFirstLang = colI18nId + 1
 
 Private langIds() As Integer
 
 Private Const firstRow = 4
 
 Private Const sheetName = "Enum-NL"
 
 Global numLangsForEnumsNl As Integer
 Private isIntialized As Boolean
 
 Private Const acmCsvProcessingStep = 0
 
 Global g_enumsNl As EnumNlDescriptors
 
 
 Private Sub readSheet()
   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))

   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   If Not isInitialized Then
     numLangsForEnumsNl = 0

     While thisSheet.Cells(thisRow - 1, colFirstLang + numLangsForEnumsNl) & "" <> ""
       numLangsForEnumsNl = numLangsForEnumsNl + 1
     Wend
     ReDim langIds(1 To numLangsForEnumsNl)
     Dim i As Integer
     For i = 1 To numLangsForEnumsNl
       langIds(i) = getInteger(thisSheet.Cells(thisRow - 1, colFirstLang + i - 1))

       If langIds(i) < 0 Then
         logMsg("invalid language ID '" & thisSheet.Cells(thisRow - 1, i) & "' found in sheet '" & thisSheet.Name & "' (column" & colFirstLang + i - 1 & "", ellError)
       End If
     Next i
   End If
 
   If numLangsForEnumsNl > 0 Then
     While thisSheet.Cells(thisRow, colI18nId) & "" <> ""
       If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
         GoTo NextRow
       End If

         g_enumsNl.descriptors(allocEnumNlDescriptorIndex(g_enumsNl)).i18nId = Trim(thisSheet.Cells(thisRow, colI18nId))
         For i = 1 To numLangsForEnumsNl
           g_enumsNl.descriptors(allocEnumNlDescriptorIndex(g_enumsNl)).nl(i) = Trim(thisSheet.Cells(thisRow, colFirstLang + i - 1))
         Next i
 NextRow:
       thisRow = thisRow + 1
     Wend
   End If
 End Sub
 
 
 Sub getEnumsNl()
   If g_enumsNl.numDescriptors = 0 Then
     readSheet()
   End If
 End Sub
 
 
 Sub resetEnumsNl()
   g_enumsNl.numDescriptors = 0
   isInitialized = False
 End Sub
 
 
 Sub evalEnumsNl()
   Dim i As Integer, j As Integer
     For i = 1 To g_enumsNl.numDescriptors Step 1
         g_enumsNl.descriptors(i).enumIndex = getEnumIndexByI18nId(g_enumsNl.descriptors(i).i18nId)
     Next i
 End Sub
 
 
 Sub dropEnumsNlCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   killCsvFileWhereEver(g_sectionIndexDbMeta, genNlObjName(clnAcmEntity), g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM")
 End Sub
 
 
 Sub genEnumNlAcmMetaCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, genNlObjName(clnAcmEntity), acmCsvProcessingStep, "ACM", ddlType)
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim i As Integer, j As Integer
   For i = 1 To g_enumsNl.numDescriptors
       For j = 1 To numLangsForEnumsNl
         If g_enumsNl.descriptors(i).nl(j) <> "" And g_enumsNl.descriptors(i).enumIndex > 0 Then
           Print #fileNo, """"; UCase(g_enums.descriptors(g_enumsNl.descriptors(i).enumIndex).sectionName); ""","; _
                          """"; UCase(g_enums.descriptors(g_enumsNl.descriptors(i).enumIndex).enumName); ""","; _
                          """"; gc_acmEntityTypeKeyEnum; ""","; _
                          CStr(j); ","; _
                          """"; g_enumsNl.descriptors(i).nl(j); ""","; _
                          getCsvTrailer(0)
         End If
       Next j
   Next i
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
