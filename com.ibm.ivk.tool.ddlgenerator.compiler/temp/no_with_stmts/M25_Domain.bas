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

       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).sectionName = Trim(thisSheet.Cells(thisRow, colSection))
       If (g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).sectionName & "" = "") Then g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).sectionName = lastSection
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).domainName = Trim(thisSheet.Cells(thisRow, colDomain))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).dataType = getDataTypeId(thisSheet.Cells(thisRow, colDataType))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).minLength = Trim(thisSheet.Cells(thisRow, colMinLength))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).maxLength = Trim(thisSheet.Cells(thisRow, colMaxLength))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).scale = thisSheet.Cells(thisRow, colScale)
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).minValue = Trim(thisSheet.Cells(thisRow, colMinValue))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).maxValue = Trim(thisSheet.Cells(thisRow, colMaxValue))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).valueList = Trim(thisSheet.Cells(thisRow, colValueList))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).constraint = Trim(thisSheet.Cells(thisRow, colCheckConstraint))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).notLogged = getBoolean(thisSheet.Cells(thisRow, colNotLogged))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).notCompact = getBoolean(thisSheet.Cells(thisRow, colNotCompact))
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).supportUnicode = (g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).dataType = etChar Or g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).dataType = etClob Or g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).dataType = etLongVarchar Or g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).dataType = etVarchar) And _
                         (Trim(thisSheet.Cells(thisRow, colUnicodeExpansionFactor)) <> "")
       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).isGenerated = (g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).dataType = etBigInt Or g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).dataType = etInteger Or g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).dataType = etSmallint) And getBoolean(thisSheet.Cells(thisRow, colIsGenerated))

       g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).unicodeExpansionFactor = IIf(g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).supportUnicode, getSingle(thisSheet.Cells(thisRow, colUnicodeExpansionFactor), unicodeExpansionFactor), 1)

       lastSection = g_domains.descriptors(allocDomainDescriptorIndex(g_domains)).sectionName
 NextRow:

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
       getDbDatatypeByDomainIndex = getDataType(g_domains.descriptors(domainIndex).dataType, g_domains.descriptors(domainIndex).maxLength, g_domains.descriptors(domainIndex).scale, g_domains.descriptors(domainIndex).supportUnicode, g_domains.descriptors(domainIndex).unicodeExpansionFactor)
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
       getDbMaxDataTypeLengthByDomainName = g_domains.descriptors(domainIndex).maxLength * IIf(g_domains.descriptors(domainIndex).supportUnicode, IIf(g_domains.descriptors(domainIndex).unicodeExpansionFactor >= 1, g_domains.descriptors(domainIndex).unicodeExpansionFactor, unicodeExpansionFactor), 1)
   End If
 End Function
 
 
 Sub evalDomains()
   Dim i As Integer, j As Integer
     For i = 1 To g_domains.numDescriptors Step 1
       g_domains.descriptors(i).domainIndex = i
     Next i
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
       If Not g_domains.descriptors(i).isGenerated Then
         Print #fileNo, """"; UCase(g_domains.descriptors(i).sectionName); """,";
         Print #fileNo, """"; UCase(g_domains.descriptors(i).domainName); """,";
         Print #fileNo, "0,";
         Print #fileNo, """"; UCase(getDataType(g_domains.descriptors(i).dataType, g_domains.descriptors(i).maxLength, g_domains.descriptors(i).scale, g_domains.descriptors(i).supportUnicode, g_domains.descriptors(i).unicodeExpansionFactor)); """,";
         Print #fileNo, IIf(g_domains.descriptors(i).minLength = "", "", """" & g_domains.descriptors(i).minLength & """"); ",";
         Print #fileNo, IIf(g_domains.descriptors(i).maxLength = "", "", """" & g_domains.descriptors(i).maxLength & """"); ",";
         Print #fileNo, IIf(g_domains.descriptors(i).minValue = "", "", g_domains.descriptors(i).minValue); ",";
         Print #fileNo, IIf(g_domains.descriptors(i).minValue = "", "", g_domains.descriptors(i).maxValue); ",";
         Print #fileNo, IIf(g_domains.descriptors(i).scale < 0, "", CStr(g_domains.descriptors(i).scale)); ",";
         Print #fileNo, IIf(g_domains.descriptors(i).supportUnicode, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, getCsvTrailer(0)
       End If
   Next i
 
   Close #fileNo
   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnAcmDomain, acmCsvProcessingStepEnum, "ACM", ddlType)
   assertDir fileName
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
 
   For i = 1 To g_enums.numDescriptors
       Print #fileNo, """"; UCase(g_enums.descriptors(i).sectionName); """,";
       Print #fileNo, """"; UCase(g_enums.descriptors(i).enumName); """,";
       Print #fileNo, "1,";
       Print #fileNo, """"; UCase(getDataType(g_enums.descriptors(i).idDataType)); """,";
       Print #fileNo, ",,,,,";
       Print #fileNo, "0,";
       Print #fileNo, getCsvTrailer(0)
   Next i
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
