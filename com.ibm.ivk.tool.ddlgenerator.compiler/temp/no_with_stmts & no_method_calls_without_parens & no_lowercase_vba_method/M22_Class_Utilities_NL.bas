 Attribute VB_Name = "M22_Class_Utilities_NL"
 Option Explicit
 
 Type ClassNlDescriptor
   i18nId As String

   nl() As String
 
   ' derived attributes
   classIndex As String
 End Type
 
 Type ClassNlDescriptors
   descriptors() As ClassNlDescriptor
   numDescriptors As Integer
 End Type


 
 
 Function allocClassNlDescriptorIndex( _
   ByRef classNls As ClassNlDescriptors _
 ) As Integer
   allocClassNlDescriptorIndex = -1

   If numLangsForClassesNl > 0 Then
       If classNls.numDescriptors = 0 Then
         ReDim classNls.descriptors(1 To gc_allocBlockSize)
       ElseIf classNls.numDescriptors >= UBound(classNls.descriptors) Then
         ReDim Preserve classNls.descriptors(1 To classNls.numDescriptors + gc_allocBlockSize)
       End If
       classNls.numDescriptors = classNls.numDescriptors + 1
         ReDim classNls.descriptors(classNls.numDescriptors).nl(1 To numLangsForClassesNl)
       allocClassNlDescriptorIndex = classNls.numDescriptors
   End If
 End Function
 
 
 Function getPrimaryClassLabelByIndex( _
   classIndex As Integer _
 ) As String
   getPrimaryClassLabelByIndex = "<unknown class>"
   Dim i As Integer, langId As Integer, minLangId As Integer
   minLangId = 9999

   If classIndex > 0 Then
     If g_classes.descriptors(classIndex).classNlIndex > 0 Then
         If Not strArrayIsNull(g_classesNl.descriptors(g_classes.descriptors(classIndex).classNlIndex).nl) Then
           For langId = LBound(g_classesNl.descriptors(g_classes.descriptors(classIndex).classNlIndex).nl) To UBound(g_classesNl.descriptors(g_classes.descriptors(classIndex).classNlIndex).nl)
             If g_classesNl.descriptors(g_classes.descriptors(classIndex).classNlIndex).nl(langId) <> "" Then
               If langId = gc_langIdEnglish Then
                 getPrimaryClassLabelByIndex = g_classesNl.descriptors(g_classes.descriptors(classIndex).classNlIndex).nl(langId)
                 Exit Function
               End If
               If langId < minLangId Then
                 getPrimaryClassLabelByIndex = g_classesNl.descriptors(g_classes.descriptors(classIndex).classNlIndex).nl(langId)
                 minLangId = langId
               End If
             End If
           Next langId
         End If
     End If
   End If
 End Function
 
