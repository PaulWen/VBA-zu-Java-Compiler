Attribute VB_Name = "M23_Relationship_Utilities_NL"
Option Explicit

Type RelationshipNlDescriptor
  i18nId As String
  
  nl() As String

  ' derived attributes
  relationshipIndex As String
End Type

Type RelationshipNlDescriptors
  descriptors() As RelationshipNlDescriptor
  numDescriptors As Integer
End Type
  
Function allocRelationshipNlDescriptorIndex( _
  ByRef relationshipNls As RelationshipNlDescriptors _
) As Integer
  allocRelationshipNlDescriptorIndex = -1
  
  If numLangsForRelationshipsNl > 0 Then
    With relationshipNls
      If .numDescriptors = 0 Then
        ReDim .descriptors(1 To gc_allocBlockSize)
      ElseIf .numDescriptors >= UBound(.descriptors) Then
        ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
      End If
      .numDescriptors = .numDescriptors + 1
      With .descriptors(.numDescriptors)
        ReDim .nl(1 To numLangsForRelationshipsNl)
      End With
      allocRelationshipNlDescriptorIndex = .numDescriptors
    End With
  End If
End Function


Function getPrimaryRelationshipLabelByIndex( _
  relationshipIndex As Integer _
) As String
  getPrimaryRelationshipLabelByIndex = "<unknown relationship>"
  Dim i As Integer, langId As Integer, minLangId As Integer
  minLangId = 9999
  
  If relationshipIndex > 0 Then
    If g_relationships.descriptors(relationshipIndex).relNlIndex > 0 Then
      With g_relationshipsNl.descriptors(g_relationships.descriptors(relationshipIndex).relNlIndex)
        If Not strArrayIsNull(.nl) Then
          For langId = LBound(.nl) To UBound(.nl)
            If .nl(langId) <> "" Then
              If langId = gc_langIdEnglish Then
                getPrimaryRelationshipLabelByIndex = .nl(langId)
                Exit Function
              End If
              If langId < minLangId Then
                getPrimaryRelationshipLabelByIndex = .nl(langId)
                minLangId = langId
              End If
            End If
          Next langId
        End If
      End With
    End If
  End If
End Function

