Attribute VB_Name = "M22_Class_NL"
Option Explicit

Private Const colEntryFilter = 1
Private Const colI18nId = colClassI18nId
Private Const colFirstLang = colI18nId + 1

Private langIds() As Integer

Private Const firstRow = 4

Private Const sheetName = "Class"

Global numLangsForClassesNl As Integer
Private isIntialized As Boolean

Private Const acmCsvProcessingStep = 1

Global g_classesNl As ClassNlDescriptors


Private Sub readSheet()
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

  If Not isInitialized Then
    numLangsForClassesNl = 0
        
    While thisSheet.Cells(thisRow - 1, colFirstLang + numLangsForClassesNl) & "" <> ""
      numLangsForClassesNl = numLangsForClassesNl + 1
    Wend
    ReDim langIds(1 To numLangsForClassesNl)
    Dim i As Integer
    For i = 1 To numLangsForClassesNl
      langIds(i) = getInteger(thisSheet.Cells(thisRow - 1, colFirstLang + i - 1))
      
      If langIds(i) < 0 Then
        logMsg "invalid language ID '" & thisSheet.Cells(thisRow - 1, i) & "' found in sheet '" & thisSheet.name & "' (column" & colFirstLang + i - 1 & "", ellError
      End If
    Next i
  End If

  If numLangsForClassesNl > 0 Then
    While thisSheet.Cells(thisRow, colI18nId) & "" <> ""
      If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
        GoTo NextRow
      End If
    
      With g_classesNl.descriptors(allocClassNlDescriptorIndex(g_classesNl))
        .i18nId = Trim(thisSheet.Cells(thisRow, colI18nId))
        For i = 1 To numLangsForClassesNl
          .nl(i) = Trim(thisSheet.Cells(thisRow, colFirstLang + i - 1))
        Next i
      End With
  
NextRow:
      thisRow = thisRow + 1
    Wend
  End If
End Sub


Sub getClassesNl()
  If g_classesNl.numDescriptors = 0 Then
    readSheet
  End If
End Sub


Sub resetClassesNl()
  g_classesNl.numDescriptors = 0
  isInitialized = False
End Sub


Sub evalClassesNl()
  Dim i As Integer, j As Integer
  With g_classesNl
    For i = 1 To .numDescriptors Step 1
      With .descriptors(i)
        .classIndex = getClassIndexByI18nId(.i18nId)
        If .classIndex > 0 Then
          g_classes.descriptors(.classIndex).classNlIndex = i
        End If
      End With
    Next i
  End With
End Sub


Sub dropClassesNlCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbMeta, genNlObjName(clnAcmEntity), g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM"
End Sub


Sub genClassNlAcmMetaCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, genNlObjName(clnAcmEntity), acmCsvProcessingStep, "ACM", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  On Error GoTo ErrorExit
  
  Dim thisClassNlIndex As Integer
  Dim thisLangId As Integer
  For thisClassNlIndex = 1 To g_classesNl.numDescriptors
    With g_classesNl.descriptors(thisClassNlIndex)
      For thisLangId = 1 To numLangsForClassesNl
        If .nl(thisLangId) <> "" And .classIndex > 0 Then
          Print #fileNo, """"; UCase(g_classes.descriptors(.classIndex).sectionName); ""","; _
                         """"; UCase(g_classes.descriptors(.classIndex).className); ""","; _
                         """"; gc_acmEntityTypeKeyClass; ""","; _
                         CStr(thisLangId); ","; _
                         """"; .nl(thisLangId); ""","; _
                         getCsvTrailer(0)
        End If
      Next thisLangId
    End With
  Next thisClassNlIndex

NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub

