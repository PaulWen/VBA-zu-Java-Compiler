 Attribute VB_Name = "M77_IndexAttr"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colSection = 2
 Private Const colClassName = colSection + 1
 Private Const colEntityType = colClassName + 1
 Private Const colIndexName = colEntityType + 1
 Private Const colAttrName = colIndexName + 1
 Private Const colAttrIsIncluded = colAttrName + 1
 Private Const colRelSectionName = colAttrIsIncluded + 1
 Private Const colRelName = colRelSectionName + 1
 Private Const colIsAsc = colAttrName + 1
 
 Private Const firstRow = 3
 
 Private Const sheetName = "IdxAttr"
 
 Global g_indexAttrs As IndexAttrDescriptors
 
 
 Private Sub readSheet()
   initIndexAttrDescriptors(g_indexAttrs)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colSection) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

     allocIndexAttrDescriptorIndex(g_indexAttrs)
       g_indexAttrs.descriptors(g_indexAttrs.numDescriptors).sectionName = Trim(thisSheet.Cells(thisRow, colSection))
       g_indexAttrs.descriptors(g_indexAttrs.numDescriptors).className = Trim(thisSheet.Cells(thisRow, colClassName))
       g_indexAttrs.descriptors(g_indexAttrs.numDescriptors).cType = getAttrContainerType(thisSheet.Cells(thisRow, colEntityType))
       g_indexAttrs.descriptors(g_indexAttrs.numDescriptors).indexName = Trim(thisSheet.Cells(thisRow, colIndexName))
       g_indexAttrs.descriptors(g_indexAttrs.numDescriptors).attrName = Trim(thisSheet.Cells(thisRow, colAttrName))
       g_indexAttrs.descriptors(g_indexAttrs.numDescriptors).attrIsIncluded = getBoolean(thisSheet.Cells(thisRow, colAttrIsIncluded))
       g_indexAttrs.descriptors(g_indexAttrs.numDescriptors).relSectionName = Trim(thisSheet.Cells(thisRow, colRelSectionName))
       g_indexAttrs.descriptors(g_indexAttrs.numDescriptors).relName = Trim(thisSheet.Cells(thisRow, colRelName))
       g_indexAttrs.descriptors(g_indexAttrs.numDescriptors).isAsc = Not (UCase(thisSheet.Cells(thisRow, colIsAsc)) = "DESC")
 
 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getIndexAttrs()
   If (g_indexAttrs.numDescriptors = 0) Then
     readSheet()
   End If
 End Sub
 
 
 Sub resetIndexAttrs()
   g_indexAttrs.numDescriptors = 0
 End Sub
 
 
 Sub evalIndexAttrs()
   Dim i As Integer, j As Integer
     Dim enumDescr As EnumDescriptor

     For i = 1 To g_indexAttrs.numDescriptors Step 1
         ' determine references to attributes
         For j = 1 To g_attributes.numDescriptors Step 1
             If UCase(g_indexAttrs.descriptors(i).sectionName) = UCase(g_attributes.descriptors(j).sectionName) And _
                UCase(g_indexAttrs.descriptors(i).className) = UCase(g_attributes.descriptors(j).className) And _
                (UCase(g_indexAttrs.descriptors(i).attrName) = UCase(g_attributes.descriptors(j).attributeName) Or UCase(g_indexAttrs.descriptors(i).attrName) = (UCase(g_attributes.descriptors(j).attributeName) & gc_enumAttrNameSuffix)) And _
                g_indexAttrs.descriptors(i).cType = g_attributes.descriptors(j).cType Then
               g_indexAttrs.descriptors(i).attrRef = j
             End If
         Next j

         If g_indexAttrs.descriptors(i).attrRef <= 0 And g_indexAttrs.descriptors(i).relSectionName <> "" And g_indexAttrs.descriptors(i).relName <> "" Then
           If g_indexAttrs.descriptors(i).cType = eactClass Then
             ' check if this index-attribute corresponds to a relationship
             For j = 1 To g_relationships.numDescriptors Step 1
                 If UCase(g_indexAttrs.descriptors(i).relSectionName) = UCase(g_relationships.descriptors(j).sectionName) And _
                    UCase(g_indexAttrs.descriptors(i).relName) = UCase(g_relationships.descriptors(j).relName) Then

                     If UCase(g_relationships.descriptors(j).leftClassName) = UCase(g_indexAttrs.descriptors(i).className) Then
                       g_indexAttrs.descriptors(i).relRefDirection = etLeft
                     Else
                       g_indexAttrs.descriptors(i).relRefDirection = etRight
                     End If

                   g_indexAttrs.descriptors(i).relRef = j
                 End If
             Next j
           ElseIf g_indexAttrs.descriptors(i).cType = eactRelationship Then
             For j = 1 To g_relationships.numDescriptors Step 1
                 If UCase(g_indexAttrs.descriptors(i).sectionName) = UCase(g_relationships.descriptors(j).sectionName) And _
                    UCase(g_indexAttrs.descriptors(i).className) = UCase(g_relationships.descriptors(j).relName) Then

                     If UCase(g_relationships.descriptors(j).lrRelName) = UCase(g_indexAttrs.descriptors(i).relName) Then
                       g_indexAttrs.descriptors(i).relRefDirection = etLeft
                     Else
                       g_indexAttrs.descriptors(i).relRefDirection = etRight
                     End If

                   g_indexAttrs.descriptors(i).relRef = j
                 End If
             Next j
           End If
         End If

 ' ### IF IVK ###
         If g_indexAttrs.descriptors(i).attrName = UCase(conOid) Or g_indexAttrs.descriptors(i).attrName = UCase(conClassId) Or g_indexAttrs.descriptors(i).attrName = UCase(conVersionId) Or g_indexAttrs.descriptors(i).attrName = UCase(conValidFrom) Or g_indexAttrs.descriptors(i).attrName = UCase(conValidTo) Or g_indexAttrs.descriptors(i).attrName = UCase(conIsDeleted) Or Right(g_indexAttrs.descriptors(i).attrName, 4) = "_OID" Then
 ' ### ELSE IVK ###
 '       If .attrName = ucase(conOid) Or .attrName = ucase(conClassId) Or .attrName = UCase(conVersionId) Or .attrName = UCase(conValidFrom) Or .attrName = UCase(conValidTo) Or Right(.attrName, 4) = "_OID" Then
 ' ### ENDIF IVK ###
           g_indexAttrs.descriptors(i).attrRef = -1
           ' meta attribute
         ElseIf Not (g_indexAttrs.descriptors(i).attrRef > 0 Or g_indexAttrs.descriptors(i).relRef > 0) Then
           If g_indexAttrs.descriptors(i).attrName <> "" Then
             logMsg("unknown attribute """ & g_indexAttrs.descriptors(i).className & "." & g_indexAttrs.descriptors(i).attrName & """ used in index """ & g_indexAttrs.descriptors(i).sectionName & "." & g_indexAttrs.descriptors(i).indexName, ellError)
           ElseIf g_indexAttrs.descriptors(i).relName <> "" Then
             logMsg("unknown relationship """ & g_indexAttrs.descriptors(i).relSectionName & "." & g_indexAttrs.descriptors(i).relName & """ used in index """ & g_indexAttrs.descriptors(i).sectionName & "." & g_indexAttrs.descriptors(i).indexName, ellError)
           End If
         End If
     Next i
 End Sub
 
