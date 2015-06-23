Attribute VB_Name = "M24_Attribute_Utilities"
Option Explicit

Enum AttrDescriptorRefType
  eadrtAttribute = 1
  eadrtEnum = 2
  eadrtType = 3
End Enum

Type AttrDescriptorRef
  refIndex As Integer
  refType As AttrDescriptorRefType
End Type

Type AttrDescriptorRefs
  descriptors() As AttrDescriptorRef
  numDescriptors As Integer
End Type
     
Type OidColDescriptor
  colName As String
  colCat As AttrCategory
End Type
   
Type OidColDescriptors
  descriptors() As OidColDescriptor
  numDescriptors As Integer
End Type
   
Enum AcmAttrContainerType
  eactClass = 1
  eactRelationship = 2
  eactEnum = 3
' ### IF IVK ###
  eactType = 4
  eactView = 5
' ### ELSE IVK ###
' eactView = 4
' ### ENDIF IVK ###
End Enum

Enum AttrValueType
  eavtDomain = 1
  eavtEnum = 2
  eavtDomainEnumId = 3
  eavtDomainEnumValue = 4
End Enum
  
' ### IF IVK ###
' two flavors of Attribute Mapping for ACM:
' 1) based on navigation along relationships
' 2) based on scalar SQL-expression
Type AttributeMappingForACM
  description As String
  isRelBasedMapping As Boolean
  isInstantiated As Boolean
  mapTo As String
  navDirection As RelNavigationDirection ' indicates which direction to follow to the 'target class'
  relIndex As Integer ' references the relationship which leads to the 'target class'
  targetClassIndex As Integer ' target class
End Type
  
' ### ENDIF IVK ###
Type AttributeDescriptor
  sectionName As String
  className As String
  cType As AcmAttrContainerType
  attributeName As String
  shortName As String
  i18nId As String
  mapsToChangeLogAttributes() As String
' ### IF IVK ###

  virtuallyMapsTo As AttributeMappingForACM
  virtuallyMapsToForRead As AttributeMappingForACM
  ftoConflictWith As String
  ftoConflictType As Integer
  ftoConflictMessageIdBase As Long
  groupIdBasedOn As String
  groupIdAttributes() As String
  groupIdAttributeIndexes() As Integer
  isNationalizable As Boolean
  isExpression As Boolean
  noXmlExport As Boolean
  isPersistent As Boolean
' ### ENDIF IVK ###
  
  domainSection As String
  domainName As String
  default As String
  isNl As Boolean
  isNullable As Boolean
  isNullableInOrgs As String
  isIdentifying As Boolean
  includeInPkIndex As Boolean
  isTimeVarying As Boolean
  comment As String

  ' derived attributes
' ### IF IVK ###
  isVirtual As Boolean
  ftoConflictWithAttrIndex As Integer
  ftoConflictWithSrcAttrIndex As Integer
  isGroupId As Boolean
  virtuallyReferredToBy() As Integer
' ### ENDIF IVK ###
  isPdmSpecific As Boolean
  isNotAcmRelated As Boolean
  isPrimaryKey As Boolean
  valueType As AttrValueType
  valueTypeIndex As Integer
  domainIndex As Integer
  reusedAttrIndex As Integer
  reusingAttrIndexes() As Integer
  compressDefault As Boolean
  acmEntityIndex As Integer
  attrIndex As Integer
  attrNlIndex As Integer
  dbColName(1 To 2) As String
End Type

Type AttributeMappingForCl
  prio As Integer
  mapFrom As String
  mapTo As String
  isTv As Boolean
  attrIndex As Integer
End Type

Type AttributeDescriptors
  descriptors() As AttributeDescriptor
  numDescriptors As Integer
End Type
  
Type AttributeTransformationContext
  orgIndex As Integer
  poolIndex As Integer
  tabQualifier As String
  forLrt As Boolean
  lrtOidRef As String
End Type
  
Type AttributeTransformation
  attributeName As String
  domainSection As String
  domainName As String
  value As String
  isConstant As Boolean
End Type
  
Type AttributeListTransformation
  attributePrefix As String
  attributePostfix As String
  attributeRepeatDelimiter As String ' if this is set, the attribute name is transformed twice with this delimiter in between (support for XML-Export)
  postProcessAfterMapping As Boolean
  numMappings As Integer
  mappings() As AttributeTransformation

  containsNlAttribute As Boolean
  nlAttrRefs As AttrDescriptorRefs ' optionally may be used to collect references to AttrDescriptors found during attribute transformation
  numNlAttrRefsTv As Integer ' optionally may be used to count the number of NL attribute references in GEN table
  numNlAttrRefsNonTv As Integer ' optionally may be used to count the number of NL attribute references in non-GEN table
  domainRefs As DomainDescriptorRefs ' optionally may be used to collect references to DomainDescriptors found during attribute transformation
' ### IF IVK ###
  virtualAttrRefs As AttrDescriptorRefs ' optionally may be used to collect references to AttrDescriptors found during attribute transformation
' ### ENDIF IVK ###
  oidDescriptors As OidColDescriptors ' optionally may be used to collect infos about OID attribtes found during attribute transformation
  distinguishNullabilityForDomainRefs As Boolean
  ignoreConstraint As Boolean
  trimRight As Boolean
  suppressAllComma As Boolean
  
  doCollectDomainDescriptors As Boolean
  doCollectAttrDescriptors As Boolean
' ### IF IVK ###
  doCollectVirtualDomainDescriptors As Boolean
  doCollectVirtualAttrDescriptors As Boolean
' ### ENDIF IVK ###
  doCollectOidColDescriptors As Boolean
  oidColFilter As AttrCategory
  conEnumLabelText As AttributeTransformationContext
End Type
    
Type EntityColumnDescriptor
  columnName As String
  isNullable As Boolean
  acmEntityType As AcmAttrContainerType
  acmEntityName As String
  acmAttributeName As String
  acmAttributeIndex As Integer
  acmFkRelIndex As Integer
  dbDomainIndex As Integer
  columnCategory As AttrCategory
  fkTargetAcmEntityName As String
  isInstantiated As Boolean
End Type
    
Type EntityColumnDescriptors
  descriptors() As EntityColumnDescriptor
  numDescriptors As Integer
End Type
    
Global nullAttributeTransformation As AttributeListTransformation
Global nullEntityColumnDescriptors As EntityColumnDescriptors


Sub initAttributeDescriptors( _
  ByRef des As AttributeDescriptors _
)
  des.numDescriptors = 0
  nullAttributeTransformation.numMappings = 0
  nullAttributeTransformation.attributePrefix = ""
  nullAttributeTransformation.attributePostfix = ""
  nullAttributeTransformation.doCollectDomainDescriptors = False
  nullAttributeTransformation.doCollectAttrDescriptors = False
  nullEntityColumnDescriptors.numDescriptors = 0
' ### IF IVK ###
  nullAttributeTransformation.doCollectVirtualDomainDescriptors = False
  nullAttributeTransformation.doCollectVirtualAttrDescriptors = False
' ### ENDIF IVK ###
  
  initDomainDescriptorRefs nullAttributeTransformation.domainRefs
End Sub


Sub initAttributeTransformation( _
  ByRef transformation As AttributeListTransformation, _
  numMappings As Integer, _
  Optional doCollectDomainDescriptors As Boolean = False, _
  Optional doCollectAttrDescriptors As Boolean = False, _
  Optional doCollectOidColDescriptors As Boolean = False, _
  Optional ByRef prefix As String = "", _
  Optional ByRef attr1 As String = "", _
  Optional ByRef val1 As String = "", _
  Optional ByRef attr2 As String = "", _
  Optional ByRef val2 As String = "", _
  Optional ByRef attr3 As String = "", _
  Optional ByRef val3 As String = "", _
  Optional ByRef postfix As String = "", _
  Optional ByRef delimiter As String = "", _
  Optional oidColFilter As AttrCategory = eacAnyOid, _
  Optional doCollectVirtualDomainDescriptors As Boolean = False, _
  Optional doCollectVirtualAttrDescriptors As Boolean = False _
)
  With transformation
    .attributePrefix = prefix
    .attributePostfix = postfix
    .attributeRepeatDelimiter = delimiter
    .postProcessAfterMapping = False
    .numMappings = numMappings
    .distinguishNullabilityForDomainRefs = False
    .doCollectDomainDescriptors = doCollectDomainDescriptors
    .doCollectAttrDescriptors = doCollectAttrDescriptors
' ### IF IVK ###
    .doCollectVirtualDomainDescriptors = doCollectVirtualDomainDescriptors
    .doCollectVirtualAttrDescriptors = doCollectVirtualAttrDescriptors
' ### ENDIF IVK ###
    .doCollectOidColDescriptors = doCollectOidColDescriptors
    .oidColFilter = oidColFilter
    .ignoreConstraint = False
    .trimRight = True
    .containsNlAttribute = False
    
    If numMappings > 0 Then
      ReDim .mappings(1 To numMappings)
      Dim i As Integer
      For i = 1 To numMappings
        .mappings(i).domainSection = ""
        .mappings(i).domainName = ""
      Next i
      
      .mappings(1).attributeName = attr1
      .mappings(1).value = val1
      If numMappings > 1 Then
        .mappings(2).attributeName = attr2
        .mappings(2).value = val2
        If numMappings > 2 Then
          .mappings(3).attributeName = attr3
          .mappings(3).value = val3
        End If
      End If
    End If
    .domainRefs.numRefs = 0
    .nlAttrRefs.numDescriptors = 0
    .oidDescriptors.numDescriptors = 0
    With .conEnumLabelText
      .orgIndex = -1
      .poolIndex = -1
      .tabQualifier = ""
      .forLrt = False
      .lrtOidRef = ""
    End With
  End With
End Sub


Sub setAttributeTransformationContext( _
  ByRef transformation As AttributeListTransformation, _
  Optional ByVal thisOrgIndex As Integer = -1, _
  Optional ByVal thisPoolIndex As Integer = -1, _
  Optional ByRef tabQualifier As String = "", _
  Optional ByRef lrtOidRef As String = "", _
  Optional forLrt = False _
)
  With transformation.conEnumLabelText
    .orgIndex = thisOrgIndex
    .poolIndex = thisPoolIndex
    .tabQualifier = tabQualifier
    .lrtOidRef = lrtOidRef
    .forLrt = forLrt Or lrtOidRef <> ""
  End With
End Sub


Sub setAttributeMapping( _
  ByRef transformation As AttributeListTransformation, _
  mappingIndex As Integer, _
  Optional ByRef attr As String = "", _
  Optional ByRef val As String = "", _
  Optional ByRef domainSection As String = "", _
  Optional ByRef domainName As String = "", _
  Optional ByRef isConstant As Boolean = False _
)
  With transformation.mappings(mappingIndex)
    .attributeName = attr
    .domainSection = domainSection
    .domainName = domainName
    .value = val
    .isConstant = isConstant
  End With
End Sub


Function allocAttributeDescriptorIndex( _
  ByRef attributes As AttributeDescriptors _
) As Integer
  allocAttributeDescriptorIndex = -1
  
  With attributes
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocAttributeDescriptorIndex = .numDescriptors
    With .descriptors(.numDescriptors)
      .valueTypeIndex = -1
      .domainIndex = -1
      .reusedAttrIndex = -1
    End With
  End With
End Function

Function allocEntityColumnDescriptorIndex( _
  ByRef des As EntityColumnDescriptors _
) As Integer
  allocEntityColumnDescriptorIndex = -1
  
  With des
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocEntityColumnDescriptorIndex = .numDescriptors
  End With
End Function
' ### IF IVK ###


Sub addVirtuallyReferingAttr( _
  attrIndex As Integer, _
  referringAttr As Integer _
)
  If attrIndex <= 0 Then
    Exit Sub
  End If

  With g_attributes.descriptors(attrIndex)
    Dim i As Integer
    For i = 1 To UBound(.virtuallyReferredToBy)
      If .virtuallyReferredToBy(i) = attrIndex Then
        Exit Sub
      End If
    Next i
    
    ReDim Preserve .virtuallyReferredToBy(0 To UBound(.virtuallyReferredToBy) + 1)
    .virtuallyReferredToBy(UBound(.virtuallyReferredToBy)) = referringAttr
  End With
End Sub


' ### ENDIF IVK ###
' ### IF IVK ###
Function findColumnToUse( _
  ByRef des As EntityColumnDescriptors, _
  ByRef columnName As String, _
  ByRef entityName As String, _
  ByRef acmEntityType As AcmAttrContainerType, _
  ByRef acmAttributeName As String, _
  valueType As AttrValueType, _
  valueTypeIndex As Integer, _
  ByRef isReused As Boolean, _
  ByRef columnCategory As AttrCategory, _
  Optional ByRef fkRelIndex As Integer, _
  Optional ByRef findOnly As Boolean = False, _
  Optional ByRef acmAttributeIndex As Integer = -1, _
  Optional isNullable As Boolean = False, _
  Optional isInstantiated As Boolean = True _
) As Integer
' ### ELSE IVK ###
'Function findColumnToUse( _
' ByRef des As EntityColumnDescriptors, _
' ByRef columnName As String, _
' ByRef entityName As String, _
' ByRef acmEntityType As AcmAttrContainerType, _
' ByRef acmAttributeName As String, _
' valueType As AttrValueType, _
' valueTypeIndex As Integer, _
' ByRef isReused As Boolean, _
' ByRef columnCategory As AttrCategory, _
' Optional ByRef fkRelIndex As Integer, _
' Optional ByRef findOnly As Boolean = False, _
' Optional ByRef acmAttributeIndex As Integer = -1, _
' Optional isNullable As Boolean = False _
') As Integer
' ### ENDIF IVK ###
  Dim i As Integer
  
  findColumnToUse = -1
  For i = 1 To des.numDescriptors Step 1
    With des.descriptors(i)
      ' FIXME: Use more precise criteria / include domain
      If UCase(.columnName) = UCase(columnName) Then
        findColumnToUse = i
        isReused = True
        .isNullable = .isNullable Or isNullable
' ### IF IVK ###
        .isInstantiated = .isInstantiated Or isInstantiated
' ### ENDIF IVK ###
        .columnCategory = .columnCategory Or columnCategory
        Exit Function
      End If
    End With
  Next i
  
  If Not findOnly Then
    ' did not find a column to reuse - record this as a new column
    i = allocEntityColumnDescriptorIndex(des)
    With des.descriptors(i)
      .acmEntityName = entityName
      .acmEntityType = acmEntityType
      .acmAttributeName = acmAttributeName
      .acmAttributeIndex = acmAttributeIndex
      .acmFkRelIndex = fkRelIndex
      .columnName = columnName
      If valueType = eavtDomain Then
        .dbDomainIndex = valueTypeIndex
      ElseIf valueType = eavtEnum Then
        .dbDomainIndex = g_enums.descriptors(valueTypeIndex).domainIndexId
        .acmAttributeName = .acmAttributeName & gc_enumAttrNameSuffix
      ElseIf valueType = eavtDomainEnumId Then
        .dbDomainIndex = g_enums.descriptors(valueTypeIndex).domainIndexId
      ElseIf valueType = eavtDomainEnumValue Then
        .dbDomainIndex = g_enums.descriptors(valueTypeIndex).domainIndexValue
      End If
      .columnCategory = columnCategory
      .isNullable = isNullable
' ### IF IVK ###
      .isInstantiated = isInstantiated
' ### ENDIF IVK ###
    End With
    findColumnToUse = i
  End If
  
  isReused = False
End Function


Function getAttrContainerType( _
  str As String _
) As AcmAttrContainerType
  str = UCase(Left(Trim(str & ""), 1))
  Select Case str
  Case gc_acmEntityTypeKeyEnum
    getAttrContainerType = eactEnum
  Case gc_acmEntityTypeKeyRel
    getAttrContainerType = eactRelationship
  Case gc_acmEntityTypeKeyClass
    getAttrContainerType = eactClass
' ### IF IVK ###
  Case gc_acmEntityTypeKeyType
    getAttrContainerType = eactType
' ### ENDIF IVK ###
  Case gc_acmEntityTypeKeyView
    getAttrContainerType = eactView
  End Select
End Function


Sub initAttrDescriptorRefs( _
  ByRef attrRefs As AttrDescriptorRefs _
)
  attrRefs.numDescriptors = 0
End Sub


Function allocAttrDescriptorRefIndex( _
  ByRef attrRefs As AttrDescriptorRefs _
) As Integer
  allocAttrDescriptorRefIndex = -1
  
  With attrRefs
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocAttrDescriptorRefIndex = .numDescriptors
  End With
End Function


Sub addAttrDescriptorRef( _
  ByRef refs As AttrDescriptorRefs, _
  ref As Integer, _
  Optional withRepeat As Boolean = False _
)
  Dim i As Integer
  
  With refs
    ' check if this attribute is already listed
    For i = 1 To .numDescriptors Step 1
      If reuseColumnsInTabsForOrMapping And Not withRepeat Then
        If g_attributes.descriptors(.descriptors(i).refIndex).attributeName = g_attributes.descriptors(ref).attributeName Then
          Exit Sub
        End If
      Else
        If reuseColumnsInTabsForOrMapping Then
          If g_attributes.descriptors(.descriptors(i).refIndex).attributeName = g_attributes.descriptors(ref).attributeName Then
            If g_attributes.descriptors(ref).reusedAttrIndex <= 0 Then
              g_attributes.descriptors(ref).reusedAttrIndex = .descriptors(i).refIndex
            End If
          End If
        End If
        If .descriptors(i).refIndex = ref Then
          Exit Sub
        End If
      End If
    Next i
    
    ' attribute is not listed -> add it
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    .descriptors(.numDescriptors).refIndex = ref
    .descriptors(.numDescriptors).refType = eadrtAttribute
  End With
End Sub





Sub addOidColDescriptor( _
  ByRef des As OidColDescriptors, _
  ByRef colName As String, _
  colCat As AttrCategory _
)
  With des
    Dim i As Integer
    ' check if this attribute is already listed
    For i = 1 To .numDescriptors Step 1
      If .descriptors(i).colName = colName Then
        Exit Sub
      End If
    Next i
    
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    .descriptors(.numDescriptors).colName = colName
    .descriptors(.numDescriptors).colCat = colCat
  End With
End Sub

