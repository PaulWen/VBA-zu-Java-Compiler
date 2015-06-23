 Attribute VB_Name = "M79_KwMap"
 Option Explicit
 
 Private Const colKeywordName = 2
 Private Const colValue = colKeywordName + 1
 
 Private Const processingStep = 2
 Private Const firstRow = 3
 Private Const sheetName = "KwMap"
 
 Global g_kwMaps As KwMapDescriptors
 
 
 Private Sub readSheet()
   initKwMapDescriptors g_kwMaps

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colKeywordName) & "" <> ""
       g_kwMaps.descriptors(allocKwMapDescriptorIndex(g_kwMaps)).keyword = Trim(thisSheet.Cells(thisRow, colKeywordName))
       g_kwMaps.descriptors(allocKwMapDescriptorIndex(g_kwMaps)).value = Trim(thisSheet.Cells(thisRow, colValue))

     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getKwMaps()
   If (g_kwMaps.numDescriptors = 0) Then
     readSheet
   End If
 End Sub
 
 
 Sub resetKwMaps()
   g_kwMaps.numDescriptors = 0
 End Sub
 
 
 Function kwTranslate( _
   ByVal text As String _
 ) As String
   ' predefined keywords
   text = Replace(text, "<productKey>", UCase(productKey))

   Dim i As Integer
   For i = 1 To g_kwMaps.numDescriptors
       text = Replace(text, g_kwMaps.descriptors(i).keyword, g_kwMaps.descriptors(i).value)
   Next i

   kwTranslate = text
 End Function
