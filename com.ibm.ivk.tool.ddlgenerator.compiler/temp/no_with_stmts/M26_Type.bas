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
       g_types.descriptors(allocTypeDescriptorIndex(g_types)).sectionName = thisSheet.Cells(thisRow, colSection)
       If (g_types.descriptors(allocTypeDescriptorIndex(g_types)).sectionName & "" = "") Then g_types.descriptors(allocTypeDescriptorIndex(g_types)).sectionName = lastSection
       g_types.descriptors(allocTypeDescriptorIndex(g_types)).typeName = thisSheet.Cells(thisRow, colTypeName)
       If (g_types.descriptors(allocTypeDescriptorIndex(g_types)).typeName & "" = "") Then g_types.descriptors(allocTypeDescriptorIndex(g_types)).typeName = lastTypeName
       g_types.descriptors(allocTypeDescriptorIndex(g_types)).shortName = thisSheet.Cells(thisRow, colShortName)
       g_types.descriptors(allocTypeDescriptorIndex(g_types)).comment = thisSheet.Cells(thisRow, colComment)

       lastSection = g_types.descriptors(allocTypeDescriptorIndex(g_types)).sectionName
       lastTypeName = g_types.descriptors(allocTypeDescriptorIndex(g_types)).typeName

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
       ' determine class index
       g_types.descriptors(thisTypeIndex).typeIndex = getTypeIndexByName(g_types.descriptors(thisTypeIndex).sectionName, g_types.descriptors(thisTypeIndex).typeName)
       ' determine reference to section
       g_types.descriptors(thisTypeIndex).sectionIndex = getSectionIndexByName(g_types.descriptors(thisTypeIndex).sectionName)
       ' determine index of class 'owning' the table implementing this class

       g_types.descriptors(thisTypeIndex).attrRefs.numDescriptors = 0
       For thisAttrIndex = 1 To g_attributes.numDescriptors Step 1
           If UCase(g_types.descriptors(thisTypeIndex).sectionName) = UCase(g_attributes.descriptors(thisAttrIndex).sectionName) And _
              UCase(g_types.descriptors(thisTypeIndex).typeName) = UCase(g_attributes.descriptors(thisAttrIndex).className) And _
              g_attributes.descriptors(thisAttrIndex).cType = eactType Then
               If g_attributes.descriptors(thisAttrIndex).valueType = eavtEnum Then
                 g_types.descriptors(thisTypeIndex).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_types.descriptors(thisTypeIndex).attrRefs)).refType = eadrtEnum
               ElseIf isType(g_attributes.descriptors(thisAttrIndex).domainSection, g_attributes.descriptors(thisAttrIndex).domainName) Then
                 g_types.descriptors(thisTypeIndex).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_types.descriptors(thisTypeIndex).attrRefs)).refType = eadrtType
               Else
                 g_types.descriptors(thisTypeIndex).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_types.descriptors(thisTypeIndex).attrRefs)).refType = eadrtAttribute
               End If
               g_types.descriptors(thisTypeIndex).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_types.descriptors(thisTypeIndex).attrRefs)).refIndex = thisAttrIndex
           End If
       Next thisAttrIndex
   Next thisTypeIndex
 End Sub
 
 
 Private Sub printRefs()
   Dim i As Integer, j As Integer
     For i = 1 To g_types.numDescriptors Step 1
         Debug.Print g_types.descriptors(i).typeName; " : "; g_types.descriptors(i).attrRefs.numDescriptors
         For j = 1 To g_types.descriptors(i).attrRefs.numDescriptors
           Debug.Print g_types.descriptors(i).typeName; " / "; g_types.descriptors(i).attrRefs.descriptors(j).refType; " / "; g_types.descriptors(i).attrRefs.descriptors(j).refIndex
         Next j
     Next i
 End Sub
 ' ### ENDIF IVK ###
 
