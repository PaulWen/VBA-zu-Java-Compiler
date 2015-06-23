Attribute VB_Name = "M25_Domain_Utilities"
Option Explicit

Type DomainDescriptor
  sectionName As String
  domainName As String
  dataType As typeId
  minLength As String
  maxLength As String
  scale As Integer
  minValue As String
  maxValue As String
  valueList As String
  constraint As String
  notLogged As Boolean
  notCompact As Boolean
  supportUnicode As Boolean
  unicodeExpansionFactor As Single
  isGenerated As Boolean
  
  ' derived attributes
  domainIndex As Integer ' my index position in 'g_domains'
End Type

Type DomainDescriptors
  descriptors() As DomainDescriptor
  numDescriptors As Integer
End Type
  
Type DomainDescriptorRefHandle
  ref As Integer
  isNullable As Boolean
End Type

Type DomainDescriptorRefs
  refs() As DomainDescriptorRefHandle
  numRefs As Integer
End Type
  
  
Sub initDomainDescriptors( _
  ByRef domains As DomainDescriptors _
)
  domains.numDescriptors = 0
End Sub


Function allocDomainDescriptorIndex( _
  ByRef domains As DomainDescriptors _
) As Integer
  allocDomainDescriptorIndex = -1
  
  With domains
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocDomainDescriptorIndex = .numDescriptors
  End With
End Function


Sub initDomainDescriptorRefs( _
  ByRef refs As DomainDescriptorRefs _
)
  refs.numRefs = 0
End Sub

Sub addDomainDescriptorRef( _
  ByRef refs As DomainDescriptorRefs, _
  ref As Integer, _
  Optional isNullable As Boolean = False, _
  Optional distinguishNullability As Boolean = False _
)
  Dim i As Integer
  
  With refs
    ' check if this domain is already listed
    For i = 1 To .numRefs Step 1
      If .refs(i).ref = ref And (Not distinguishNullability Or .refs(i).isNullable = isNullable) Then
        Exit Sub
      End If
    Next i
    
    ' domain is not listed -> add it
    If .numRefs = 0 Then
      ReDim .refs(1 To gc_allocBlockSize)
    ElseIf .numRefs >= UBound(.refs) Then
      ReDim Preserve .refs(1 To .numRefs + gc_allocBlockSize)
    End If
    .numRefs = .numRefs + 1
    .refs(.numRefs).ref = ref
    .refs(.numRefs).isNullable = isNullable
  End With
End Sub
