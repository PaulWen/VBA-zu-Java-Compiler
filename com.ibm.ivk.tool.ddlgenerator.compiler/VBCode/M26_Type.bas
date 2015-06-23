Attribute VB_Name = "M26_Type"
' ### IF IVK ###
Option Explicit

Private Const colSection = 2
Private Const colTypeName = colSection + 1
Private Const colShortName = colTypeName + 1
Private Const colComment = colShortName + 1

Private Const firstRow = 3

Private Const sheetName = "Type"

Global g_types As TypeDescriptors


Private Sub readSheet()
  initTypeDescriptors g_types
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow
  
  Dim lastSection As String, lastTypeName As String
  While thisSheet.Cells(thisRow, colTypeName) & "" <> ""
    With g_types.descriptors(allocTypeDescriptorIndex(g_types))
      .sectionName = thisSheet.Cells(thisRow, colSection)
      If (.sectionName & "" = "") Then .sectionName = lastSection
      .typeName = thisSheet.Cells(thisRow, colTypeName)
      If (.typeName & "" = "") Then .typeName = lastTypeName
      .shortName = thisSheet.Cells(thisRow, colShortName)
      .comment = thisSheet.Cells(thisRow, colComment)
    
      lastSection = .sectionName
      lastTypeName = .typeName
    End With
      
    thisRow = thisRow + 1
  Wend
End Sub


Sub getTypes()
  If g_types.numDescriptors = 0 Then
    readSheet
  End If
End Sub


Sub resetTypes()
  g_types.numDescriptors = 0
End Sub


Function getTypeIndexByName( _
  ByRef sectionName As String, _
  ByRef typeName As String, _
  Optional silent As Boolean = False _
) As Integer
  Dim i As Integer

  getTypeIndexByName = -1

  For i = 1 To g_types.numDescriptors Step 1
    If UCase(g_types.descriptors(i).sectionName) = UCase(sectionName) And _
       UCase(g_types.descriptors(i).typeName) = UCase(typeName) Then
      getTypeIndexByName = i
      Exit Function
    End If
  Next i

  If Not silent Then
    errMsgBox "unable to identify type '" & sectionName & "." & typeName & "'", vbCritical
  End If
End Function


Function isType( _
  ByRef sectionName As String, _
  ByRef typeName As String, _
  Optional ByRef typeIndex As Integer = -1 _
) As Boolean
  isType = False
  
  typeIndex = getTypeIndexByName(sectionName, typeName, True)
  If (typeIndex > 0) Then
    isType = True
  End If
End Function


Sub evalTypes()
  Dim thisTypeIndex As Integer
  Dim thisAttrIndex As Integer
    
  For thisTypeIndex = 1 To g_types.numDescriptors Step 1
    With g_types.descriptors(thisTypeIndex)
      ' determine class index
      .typeIndex = getTypeIndexByName(.sectionName, .typeName)
      ' determine reference to section
      .sectionIndex = getSectionIndexByName(.sectionName)
      ' determine index of class 'owning' the table implementing this class
        
      g_types.descriptors(thisTypeIndex).attrRefs.numDescriptors = 0
      For thisAttrIndex = 1 To g_attributes.numDescriptors Step 1
        With g_attributes.descriptors(thisAttrIndex)
          If UCase(g_types.descriptors(thisTypeIndex).sectionName) = UCase(.sectionName) And _
             UCase(g_types.descriptors(thisTypeIndex).typeName) = UCase(.className) And _
             .cType = eactType Then
            With g_types.descriptors(thisTypeIndex).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_types.descriptors(thisTypeIndex).attrRefs))
              If g_attributes.descriptors(thisAttrIndex).valueType = eavtEnum Then
                .refType = eadrtEnum
              ElseIf isType(g_attributes.descriptors(thisAttrIndex).domainSection, g_attributes.descriptors(thisAttrIndex).domainName) Then
                .refType = eadrtType
              Else
                .refType = eadrtAttribute
              End If
              .refIndex = thisAttrIndex
            End With
          End If
        End With
      Next thisAttrIndex
    End With
  Next thisTypeIndex
End Sub


Private Sub printRefs()
  Dim i As Integer, j As Integer
  With g_types
    For i = 1 To .numDescriptors Step 1
      With .descriptors(i)
        Debug.Print .typeName; " : "; .attrRefs.numDescriptors
        For j = 1 To .attrRefs.numDescriptors
          Debug.Print .typeName; " / "; .attrRefs.descriptors(j).refType; " / "; .attrRefs.descriptors(j).refIndex
        Next j
      End With
    Next i
  End With
End Sub
' ### ENDIF IVK ###

