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

     If domains.numDescriptors = 0 Then
       ReDim domains.descriptors(1 To gc_allocBlockSize)
     ElseIf domains.numDescriptors >= UBound(domains.descriptors) Then
       ReDim Preserve domains.descriptors(1 To domains.numDescriptors + gc_allocBlockSize)
     End If
     domains.numDescriptors = domains.numDescriptors + 1
     allocDomainDescriptorIndex = domains.numDescriptors
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

     ' check if this domain is already listed
     For i = 1 To refs.numRefs Step 1
       If refs.refs(i).ref = ref And (Not distinguishNullability Or refs.refs(i).isNullable = isNullable) Then
         Exit Sub
       End If
     Next i

     ' domain is not listed -> add it
     If refs.numRefs = 0 Then
       ReDim refs.refs(1 To gc_allocBlockSize)
     ElseIf refs.numRefs >= UBound(refs.refs) Then
       ReDim Preserve refs.refs(1 To refs.numRefs + gc_allocBlockSize)
     End If
     refs.numRefs = refs.numRefs + 1
     refs.refs(refs.numRefs).ref = ref
     refs.refs(refs.numRefs).isNullable = isNullable
 End Sub
