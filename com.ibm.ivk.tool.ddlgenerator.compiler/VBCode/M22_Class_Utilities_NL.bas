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
    With classNls
      If .numDescriptors = 0 Then
        ReDim .descriptors(1 To gc_allocBlockSize)
      ElseIf .numDescriptors >= UBound(.descriptors) Then
        ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
      End If
      .numDescriptors = .numDescriptors + 1
      With .descriptors(.numDescriptors)
        ReDim .nl(1 To numLangsForClassesNl)
      End With
      allocClassNlDescriptorIndex = .numDescriptors
    End With
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
      With g_classesNl.descriptors(g_classes.descriptors(classIndex).classNlIndex)
        If Not strArrayIsNull(.nl) Then
          For langId = LBound(.nl) To UBound(.nl)
            If .nl(langId) <> "" Then
              If langId = gc_langIdEnglish Then
                getPrimaryClassLabelByIndex = .nl(langId)
                Exit Function
              End If
              If langId < minLangId Then
                getPrimaryClassLabelByIndex = .nl(langId)
                minLangId = langId
              End If
            End If
          Next langId
        End If
      End With
    End If
  End If
End Function

