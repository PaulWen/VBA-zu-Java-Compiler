 Attribute VB_Name = "M99_IndexException"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colSection = 2
 Private Const colSectionShortName = colSection + 1
 Private Const colIndexName = colSectionShortName + 1
 Private Const colNoIndexInPool = colIndexName + 1
 
 Private Const firstRow = 3
 
 Private Const sheetName = "IdxExcp"
 
 Global g_indexExcp As IndexExcpDescriptors
 
 
 Private Sub readSheet()
   initIndexExcpDescriptors(g_indexExcp)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colSection) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

     allocIndexExcpDescriptorIndex(g_indexExcp)
       g_indexExcp.descriptors(g_indexExcp.numDescriptors).sectionName = Trim(thisSheet.Cells(thisRow, colSection))
       g_indexExcp.descriptors(g_indexExcp.numDescriptors).sectionShortName = Trim(thisSheet.Cells(thisRow, colSectionShortName))
       g_indexExcp.descriptors(g_indexExcp.numDescriptors).indexName = Trim(thisSheet.Cells(thisRow, colIndexName))
       g_indexExcp.descriptors(g_indexExcp.numDescriptors).noIndexInPool = Trim(thisSheet.Cells(thisRow, colNoIndexInPool))
 
 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getIndexExcp()
   If (g_indexExcp.numDescriptors = 0) Then
     readSheet()
   End If
 End Sub
 
