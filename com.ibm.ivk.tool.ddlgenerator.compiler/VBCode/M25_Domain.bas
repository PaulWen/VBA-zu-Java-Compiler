Attribute VB_Name = "M25_Domain"
Option Explicit

Private Const colEntryFilter = 1
Private Const colSection = 2
Private Const colDomain = colSection + 1
Private Const colDataType = colDomain + 1
Private Const colMinLength = colDataType + 1
Private Const colMaxLength = colMinLength + 1
Private Const colScale = colMaxLength + 1
Private Const colMinValue = colScale + 1
Private Const colMaxValue = colMinValue + 1
Private Const colValueList = colMaxValue + 1
Private Const colCheckConstraint = colValueList + 1
Private Const colNotLogged = colCheckConstraint + 1
Private Const colNotCompact = colNotLogged + 1
Private Const colIsGenerated = colNotCompact + 1
Private Const colUnicodeExpansionFactor = colIsGenerated + 1

Private Const firstRow = 3

Private Const sheetName = "Dom"

Private Const acmCsvProcessingStep = 3
Private Const acmCsvProcessingStepEnum = 4

Global g_domains As DomainDescriptors

Private Function readSheet() As DomainDescriptors
  initDomainDescriptors g_domains
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  Dim lastSection As String
  While thisSheet.Cells(thisRow, colDomain) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
      GoTo NextRow
    End If
    
    With g_domains.descriptors(allocDomainDescriptorIndex(g_domains))
      .sectionName = Trim(thisSheet.Cells(thisRow, colSection))
      If (.sectionName & "" = "") Then .sectionName = lastSection
      .domainName = Trim(thisSheet.Cells(thisRow, colDomain))
      .dataType = getDataTypeId(thisSheet.Cells(thisRow, colDataType))
      .minLength = Trim(thisSheet.Cells(thisRow, colMinLength))
      .maxLength = Trim(thisSheet.Cells(thisRow, colMaxLength))
      .scale = thisSheet.Cells(thisRow, colScale)
      .minValue = Trim(thisSheet.Cells(thisRow, colMinValue))
      .maxValue = Trim(thisSheet.Cells(thisRow, colMaxValue))
      .valueList = Trim(thisSheet.Cells(thisRow, colValueList))
      .constraint = Trim(thisSheet.Cells(thisRow, colCheckConstraint))
      .notLogged = getBoolean(thisSheet.Cells(thisRow, colNotLogged))
      .notCompact = getBoolean(thisSheet.Cells(thisRow, colNotCompact))
      .supportUnicode = (.dataType = etChar Or .dataType = etClob Or .dataType = etLongVarchar Or .dataType = etVarchar) And _
                        (Trim(thisSheet.Cells(thisRow, colUnicodeExpansionFactor)) <> "")
      .isGenerated = (.dataType = etBigInt Or .dataType = etInteger Or .dataType = etSmallint) And getBoolean(thisSheet.Cells(thisRow, colIsGenerated))
      
      .unicodeExpansionFactor = IIf(.supportUnicode, getSingle(thisSheet.Cells(thisRow, colUnicodeExpansionFactor), unicodeExpansionFactor), 1)
      
      lastSection = .sectionName
NextRow:
    End With
      
    thisRow = thisRow + 1
  Wend
End Function


Sub getDomains()
  If g_domains.numDescriptors = 0 Then
    readSheet
  End If
End Sub


Sub resetDomains()
  g_domains.numDescriptors = 0
End Sub


Function getDomainIndexByName( _
  ByRef sectionName As String, _
  ByRef domainName As String, _
  Optional silent As Boolean = False _
) As Integer
  Dim i As Integer

  getDomainIndexByName = -1
  getDomains

  For i = 1 To g_domains.numDescriptors Step 1
    If UCase(g_domains.descriptors(i).sectionName) = UCase(sectionName) And _
       UCase(g_domains.descriptors(i).domainName) = UCase(domainName) Then
      getDomainIndexByName = i
      Exit Function
    End If
  Next i

  If Not silent Then
    errMsgBox "unable to identify domain '" & sectionName & "." & domainName & "'", vbCritical
  End If
End Function


Function getDbDatatypeByDomainIndex( _
  domainIndex As Integer _
) As String
  getDbDatatypeByDomainIndex = ""
  
  If (domainIndex > 0) Then
    With g_domains.descriptors(domainIndex)
      getDbDatatypeByDomainIndex = getDataType(.dataType, .maxLength, .scale, .supportUnicode, .unicodeExpansionFactor)
    End With
  End If
End Function


Function getDbDataTypeByDomainName( _
  ByRef sectionName As String, _
  ByRef domainName As String _
) As String
  Dim domainIndex As Integer
  getDbDataTypeByDomainName = ""
  domainIndex = getDomainIndexByName(sectionName, domainName)
  If (domainIndex > 0) Then getDbDataTypeByDomainName = getDbDatatypeByDomainIndex(domainIndex)
End Function


Function getDbMaxDataTypeLengthByDomainName( _
  ByRef sectionName As String, _
  ByRef domainName As String _
) As Integer
  Dim domainIndex As Integer
  getDbMaxDataTypeLengthByDomainName = -1
  domainIndex = getDomainIndexByName(sectionName, domainName)
  If (domainIndex > 0) Then
    With g_domains.descriptors(domainIndex)
      getDbMaxDataTypeLengthByDomainName = .maxLength * IIf(.supportUnicode, IIf(.unicodeExpansionFactor >= 1, .unicodeExpansionFactor, unicodeExpansionFactor), 1)
    End With
  End If
End Function


Sub evalDomains()
  Dim i As Integer, j As Integer
  With g_domains
    For i = 1 To .numDescriptors Step 1
      .descriptors(i).domainIndex = i
    Next i
  End With
End Sub


Sub dropDomainCsv( _
  Optional onlyIfEmpty As Boolean = False _
)
  killCsvFileWhereEver g_sectionIndexDbMeta, clnAcmDomain, g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM"
  killCsvFileWhereEver g_sectionIndexDbMeta, clnAcmDomain, g_targetDir, acmCsvProcessingStepEnum, onlyIfEmpty, "ACM"
End Sub


Sub genDomainAcmMetaCsv( _
  ddlType As DdlTypeId _
)
  Dim fileName As String
  Dim fileNo As Integer
  
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnAcmDomain, acmCsvProcessingStep, "ACM", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo
  On Error GoTo ErrorExit

  Dim i As Integer
  For i = 1 To g_domains.numDescriptors
    With g_domains.descriptors(i)
      If Not .isGenerated Then
        Print #fileNo, """"; UCase(.sectionName); """,";
        Print #fileNo, """"; UCase(.domainName); """,";
        Print #fileNo, "0,";
        Print #fileNo, """"; UCase(getDataType(.dataType, .maxLength, .scale, .supportUnicode, .unicodeExpansionFactor)); """,";
        Print #fileNo, IIf(.minLength = "", "", """" & .minLength & """"); ",";
        Print #fileNo, IIf(.maxLength = "", "", """" & .maxLength & """"); ",";
        Print #fileNo, IIf(.minValue = "", "", .minValue); ",";
        Print #fileNo, IIf(.minValue = "", "", .maxValue); ",";
        Print #fileNo, IIf(.scale < 0, "", CStr(.scale)); ",";
        Print #fileNo, IIf(.supportUnicode, gc_dbTrue, gc_dbFalse); ",";
        Print #fileNo, getCsvTrailer(0)
      End If
    End With
  Next i

  Close #fileNo
  fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnAcmDomain, acmCsvProcessingStepEnum, "ACM", ddlType)
  assertDir fileName
  fileNo = FreeFile()
  Open fileName For Append As #fileNo

  For i = 1 To g_enums.numDescriptors
    With g_enums.descriptors(i)
      Print #fileNo, """"; UCase(.sectionName); """,";
      Print #fileNo, """"; UCase(.enumName); """,";
      Print #fileNo, "1,";
      Print #fileNo, """"; UCase(getDataType(.idDataType)); """,";
      Print #fileNo, ",,,,,";
      Print #fileNo, "0,";
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

