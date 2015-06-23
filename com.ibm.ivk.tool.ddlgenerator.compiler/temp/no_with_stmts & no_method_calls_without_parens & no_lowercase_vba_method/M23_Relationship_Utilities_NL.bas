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
       If relationshipNls.numDescriptors = 0 Then
         ReDim relationshipNls.descriptors(1 To gc_allocBlockSize)
       ElseIf relationshipNls.numDescriptors >= UBound(relationshipNls.descriptors) Then
         ReDim Preserve relationshipNls.descriptors(1 To relationshipNls.numDescriptors + gc_allocBlockSize)
       End If
       relationshipNls.numDescriptors = relationshipNls.numDescriptors + 1
         ReDim relationshipNls.descriptors(relationshipNls.numDescriptors).nl(1 To numLangsForRelationshipsNl)
       allocRelationshipNlDescriptorIndex = relationshipNls.numDescriptors
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
         If Not strArrayIsNull(g_relationshipsNl.descriptors(g_relationships.descriptors(relationshipIndex).relNlIndex).nl) Then
           For langId = LBound(g_relationshipsNl.descriptors(g_relationships.descriptors(relationshipIndex).relNlIndex).nl) To UBound(g_relationshipsNl.descriptors(g_relationships.descriptors(relationshipIndex).relNlIndex).nl)
             If g_relationshipsNl.descriptors(g_relationships.descriptors(relationshipIndex).relNlIndex).nl(langId) <> "" Then
               If langId = gc_langIdEnglish Then
                 getPrimaryRelationshipLabelByIndex = g_relationshipsNl.descriptors(g_relationships.descriptors(relationshipIndex).relNlIndex).nl(langId)
                 Exit Function
               End If
               If langId < minLangId Then
                 getPrimaryRelationshipLabelByIndex = g_relationshipsNl.descriptors(g_relationships.descriptors(relationshipIndex).relNlIndex).nl(langId)
                 minLangId = langId
               End If
             End If
           Next langId
         End If
     End If
   End If
 End Function
 
