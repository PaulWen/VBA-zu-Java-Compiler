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
  initIndexAttrDescriptors g_indexAttrs
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colSection) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
      GoTo NextRow
    End If
    
    allocIndexAttrDescriptorIndex g_indexAttrs
    With g_indexAttrs.descriptors(g_indexAttrs.numDescriptors)
      .sectionName = Trim(thisSheet.Cells(thisRow, colSection))
      .className = Trim(thisSheet.Cells(thisRow, colClassName))
      .cType = getAttrContainerType(thisSheet.Cells(thisRow, colEntityType))
      .indexName = Trim(thisSheet.Cells(thisRow, colIndexName))
      .attrName = Trim(thisSheet.Cells(thisRow, colAttrName))
      .attrIsIncluded = getBoolean(thisSheet.Cells(thisRow, colAttrIsIncluded))
      .relSectionName = Trim(thisSheet.Cells(thisRow, colRelSectionName))
      .relName = Trim(thisSheet.Cells(thisRow, colRelName))
      .isAsc = Not (UCase(thisSheet.Cells(thisRow, colIsAsc)) = "DESC")
    End With

NextRow:
    thisRow = thisRow + 1
  Wend
End Sub


Sub getIndexAttrs()
  If (g_indexAttrs.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetIndexAttrs()
  g_indexAttrs.numDescriptors = 0
End Sub


Sub evalIndexAttrs()
  Dim i As Integer, j As Integer
  With g_indexAttrs
    Dim enumDescr As EnumDescriptor
    
    For i = 1 To .numDescriptors Step 1
      With .descriptors(i)
        ' determine references to attributes
        For j = 1 To g_attributes.numDescriptors Step 1
          With g_attributes.descriptors(j)
            If UCase(g_indexAttrs.descriptors(i).sectionName) = UCase(.sectionName) And _
               UCase(g_indexAttrs.descriptors(i).className) = UCase(.className) And _
               (UCase(g_indexAttrs.descriptors(i).attrName) = UCase(.attributeName) Or UCase(g_indexAttrs.descriptors(i).attrName) = (UCase(.attributeName) & gc_enumAttrNameSuffix)) And _
               g_indexAttrs.descriptors(i).cType = .cType Then
              g_indexAttrs.descriptors(i).attrRef = j
            End If
          End With
        Next j
        
        If .attrRef <= 0 And .relSectionName <> "" And .relName <> "" Then
          If .cType = eactClass Then
            ' check if this index-attribute corresponds to a relationship
            For j = 1 To g_relationships.numDescriptors Step 1
              With g_relationships.descriptors(j)
                If UCase(g_indexAttrs.descriptors(i).relSectionName) = UCase(.sectionName) And _
                   UCase(g_indexAttrs.descriptors(i).relName) = UCase(.relName) Then
                  
                  With g_indexAttrs.descriptors(i)
                    If UCase(g_relationships.descriptors(j).leftClassName) = UCase(.className) Then
                      .relRefDirection = etLeft
                    Else
                      .relRefDirection = etRight
                    End If
                  End With
                
                  g_indexAttrs.descriptors(i).relRef = j
                End If
              End With
            Next j
          ElseIf .cType = eactRelationship Then
            For j = 1 To g_relationships.numDescriptors Step 1
              With g_relationships.descriptors(j)
                If UCase(g_indexAttrs.descriptors(i).sectionName) = UCase(.sectionName) And _
                   UCase(g_indexAttrs.descriptors(i).className) = UCase(.relName) Then
                  
                  With g_indexAttrs.descriptors(i)
                    If UCase(g_relationships.descriptors(j).lrRelName) = UCase(.relName) Then
                      .relRefDirection = etLeft
                    Else
                      .relRefDirection = etRight
                    End If
                  End With
                
                  g_indexAttrs.descriptors(i).relRef = j
                End If
              End With
            Next j
          End If
        End If
        
' ### IF IVK ###
        If .attrName = UCase(conOid) Or .attrName = UCase(conClassId) Or .attrName = UCase(conVersionId) Or .attrName = UCase(conValidFrom) Or .attrName = UCase(conValidTo) Or .attrName = UCase(conIsDeleted) Or Right(.attrName, 4) = "_OID" Then
' ### ELSE IVK ###
'       If .attrName = ucase(conOid) Or .attrName = ucase(conClassId) Or .attrName = UCase(conVersionId) Or .attrName = UCase(conValidFrom) Or .attrName = UCase(conValidTo) Or Right(.attrName, 4) = "_OID" Then
' ### ENDIF IVK ###
          .attrRef = -1
          ' meta attribute
        ElseIf Not (.attrRef > 0 Or .relRef > 0) Then
          If .attrName <> "" Then
            logMsg "unknown attribute """ & .className & "." & .attrName & """ used in index """ & .sectionName & "." & .indexName, ellError
          ElseIf .relName <> "" Then
            logMsg "unknown relationship """ & .relSectionName & "." & .relName & """ used in index """ & .sectionName & "." & .indexName, ellError
          End If
        End If
      End With
    Next i
  End With
End Sub

