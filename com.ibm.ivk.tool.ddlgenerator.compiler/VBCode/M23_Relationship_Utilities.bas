Attribute VB_Name = "M23_Relationship_Utilities"
Option Explicit

Enum RelRefTargetType
  erttRegular = 0
  erttGen = 1
  erttNL = 2
  erttGenNl = (erttGen Or erttNL)
End Enum

Type NavPathFromRelationshipToClass
  navDirectionToClass As RelNavigationDirection ' indicates whether the 'first class' on the path to 'target class' is reached following this relationship in left or right direction
End Type

Type RelationshipIndexes
  indexes() As Integer
  numIndexes As Integer
End Type

Enum FkMaintenanceMode
  efkmRestrict = 0
  efkmCascade = 1
End Enum

Type RelationshipDescriptorRef
  refIndex As Integer
  refType As RelNavigationDirection
End Type
   
Type RelationshipDescriptorRefs
  refs() As RelationshipDescriptorRef
  numRefs As Integer
End Type

Type RelationshipDescriptor
  sectionName As String
  relName As String
  i18nId As String
  aggHeadSection As String
  aggHeadName As String
  shortName As String
  ignoreForChangelog As Boolean
  reuseName As String
  reuseShortName As String
' ### IF IVK ###
  lrtClassification As String
  lrtActivationStatusMode As String
  refersToClAttribute() As AttributeMappingForCl
  virtuallyMapsTo As AttributeMappingForACM
  navPathStrToDivision As String
  noRangePartitioning As Boolean
  noXmlExport As Boolean
  useXmlExport As Boolean
  isNationalizable As Boolean
  isPsForming As Boolean
  supportExtendedPsCopy As Boolean
  noTransferToProduction As Boolean
  noFto As Boolean
  ftoSingleObjProcessing As Boolean
' ### ENDIF IVK ###
  isCommonToOrgs As Boolean
  specificToOrgId As Integer
  fkReferenceOrgId As Integer
  isCommonToPools As Boolean
  specificToPool As Integer
  fkReferencePoolId As Integer
  noIndexesInPool As Integer
  useValueCompression As Boolean
  useSurrogateKey As Boolean
  useVersiontag As Boolean
  relId As Integer
  notAcmRelated As Boolean
  noAlias As Boolean
  isLrtSpecific As Boolean
  isPdmSpecific As Boolean
  includeInPdmExportSeqNo As Integer
  isVolatile As Boolean
  isNotEnforced As Boolean
  isNl As Boolean
  includeInPkIndex As Boolean
  leftClassSectionName As String
  leftClassName As String
  leftTargetType As RelRefTargetType
  lrRelName As String
  lrShortRelName As String
  lrLdmRelName As String
  lrFkMaintenanceMode As FkMaintenanceMode
  minLeftCardinality As Integer
  maxLeftCardinality As Integer
  isIdentifyingLeft As Boolean
  useIndexOnLeftFk As Boolean
' ### IF IVK ###
  leftDependentAttribute As String
' ### ENDIF IVK ###
  rightClassSectionName As String
  rightClassName As String
  rightTargetType As RelRefTargetType
  rlRelName As String
  rlShortRelName As String
  rlLdmRelName As String
  rlFkMaintenanceMode As FkMaintenanceMode
  minRightCardinality As Integer
  maxRightCardinality As Integer
  isIdentifyingRight As Boolean
  useIndexOnRightFk As Boolean
' ### IF IVK ###
  isRightRefToTimeVarying As Boolean
  rightDependentAttribute As String
' ### ENDIF IVK ###
  logLastChange As Boolean
  logLastChangeAutoMaint As Boolean
  logLastChangeInView As Boolean
  isUserTransactional As Boolean
  isLrtMeta As Boolean
  useMqtToImplementLrt As Boolean
  tabSpaceData As String
  tabSpaceLong As String
  tabSpaceNl As String
  tabSpaceIndex As String
  defaultStatus As Integer
  isTimeVarying As Boolean
  
  isMdsExpressionRel As Boolean
  
  ' derived attributes
  fkReferenceOrgIndex As Integer
  fkReferencePoolIndex As Integer
  effectiveShortName As String
  hasBusinessKey As Boolean
  implementsInOwnTable As Boolean
  implementsInEntity As RelNavigationMode
  relIdStr As String
  relNlIndex As Integer
  isVirtual As Boolean
  aggHeadClassIndex As Integer
  aggHeadClassIndexExact As Integer
  aggHeadClassIdStr As String
  hasPriceAssignmentAggHead As Boolean
  isSubjectToPreisDurchschuss As Boolean
  attrRefs As AttrDescriptorRefs
  nlAttrRefs As AttrDescriptorRefs
  relRefs As RelationshipDescriptorRefs
  indexRefs As IndexDescriptorRefs
  numAttrs As Integer
  leftEntityIndex As Integer
  leftEntityType As AcmAttrContainerType
  leftEntityShortName As String
  leftFkColName(1 To 2) As String
  rightEntityIndex As Integer
  rightEntityType As AcmAttrContainerType
  rightEntityShortName As String
  rightFkColName(1 To 2) As String
  useLrLdmRelName As Boolean
  useRlLdmRelName As Boolean
  isSubjectToArchiving As Boolean
  leftIsSubjectToArchiving As Boolean
  rightIsSubjectToArchiving As Boolean
  rightIsDivision As Boolean
  leftIsDivision As Boolean
  isPsTagged As Boolean
  relIndex As Integer
  sectionIndex As Integer
  sectionShortName As String
  tabSpaceIndexData As Integer
  tabSpaceIndexIndex As Integer
  tabSpaceIndexLong As Integer
  tabSpaceIndexNl As Integer
  
  hasLabel As Boolean
' ### IF IVK ###
  hasIsNationalInclSubClasses As Boolean
  isAllowedCountries As RelNavigationMode
  isDisallowedCountries As RelNavigationMode
  isAllowedCountriesList As RelNavigationMode
  isDisallowedCountriesList As RelNavigationMode
  isValidForOrganization As Boolean
  hasOrganizationSpecificReference As Boolean
  leftClassIsOrganizationSpecific As Boolean
  rightClassIsOrganizationSpecific As Boolean
  supportXmlExport As Boolean
  isSubjectToExpCopy As Boolean
' ### ENDIF IVK ###
  
  isReusedInSameEntity As Boolean
  reusedRelIndex As Integer
  reusingRelIndexes As RelationshipIndexes
  
  ' temporary variables supporting processing
  isLdmCsvExported As Boolean
  isLdmLrtCsvExported As Boolean
  isCtoAliasCreated As Boolean
' ### IF IVK ###
  isXsdExported As Boolean
  navPathToDiv As NavPathFromRelationshipToClass
' ### ENDIF IVK ###
End Type

Type RelationshipDescriptors
  descriptors() As RelationshipDescriptor
  numDescriptors As Integer
End Type
  

Function allocRelationshipDescriptorIndex( _
  ByRef relationships As RelationshipDescriptors _
) As Integer
  allocRelationshipDescriptorIndex = -1
  
  With relationships
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    With relationships.descriptors(.numDescriptors)
      initAttrDescriptorRefs .attrRefs
      initAttrDescriptorRefs .nlAttrRefs
    End With
    allocRelationshipDescriptorIndex = .numDescriptors
  End With
End Function

Function allocRelationshipIndex( _
  ByRef relIndexes As RelationshipIndexes _
) As Integer
  allocRelationshipIndex = -1
  
  With relIndexes
    If .numIndexes = 0 Then
      ReDim .indexes(1 To gc_allocBlockSize)
    ElseIf .numIndexes >= UBound(.indexes) Then
      ReDim Preserve .indexes(1 To .numIndexes + gc_allocBlockSize)
    End If
    .numIndexes = .numIndexes + 1
    allocRelationshipIndex = .numIndexes
  End With
End Function


Sub addRelIndex( _
  ByRef relIndexes As RelationshipIndexes, _
  relIndex As Integer _
)
  relIndexes.indexes(allocRelationshipIndex(relIndexes)) = relIndex
End Sub


Sub initRelDescriptorRefs( _
  ByRef relRefs As RelationshipDescriptorRefs _
)
  relRefs.numRefs = 0
End Sub


Function allocRelDescriptorRefIndex( _
  ByRef relRefs As RelationshipDescriptorRefs _
) As Integer
  allocRelDescriptorRefIndex = -1
  
  With relRefs
    If .numRefs = 0 Then
      ReDim .refs(1 To gc_allocBlockSize)
    ElseIf .numRefs >= UBound(.refs) Then
      ReDim Preserve .refs(1 To .numRefs + gc_allocBlockSize)
    End If
    .numRefs = .numRefs + 1
    allocRelDescriptorRefIndex = .numRefs
  End With
End Function


Function getRelIdByIndex( _
  thisRelIndex As Integer _
) As String
  If thisRelIndex < 1 Then
    getRelIdByIndex = ""
  Else
    With g_relationships.descriptors(thisRelIndex)
      getRelIdByIndex = Right("00" & getSectionSeqNoByIndex(.sectionIndex), 2) & Right("000" & .relId, 3)
    End With
  End If
End Function


Function getRelRefTargetType( _
  ByVal str As String _
) As RelRefTargetType
  str = UCase(Trim(str & ""))
  If str = "GEN" Then
    getRelRefTargetType = erttGen
  ElseIf str = "NL" Then
    getRelRefTargetType = erttNL
  ElseIf str = "GEN-NL" Then
    getRelRefTargetType = erttGenNl
  Else
    getRelRefTargetType = erttRegular
  End If
End Function


Sub genNavPathForRelationship( _
  ByRef relIndex As Integer, _
  ByRef navPath As NavPathFromRelationshipToClass, _
  ByRef str As String _
)
  Dim list() As String
  list = split("", ".")
  list = split(str, ".")
    
  If UBound(list) = 1 Then
    Dim classSectionName As String
    Dim className As String
    
    classSectionName = list(LBound(list))
    className = list(LBound(list) + 1)
    'determine the class which leads us to Division
    Dim classIndex As Integer
    Dim leftClassIndex As Integer
    Dim rightClassIndex As Integer
    classIndex = getClassIndexByName(classSectionName, className)
    If classIndex < 0 Then
      logMsg "unable to determine class '" & str & "' supposed to lead to 'Division'", ellError
      Exit Sub
    End If
    
    With g_relationships.descriptors(relIndex)
      leftClassIndex = .leftEntityIndex
      rightClassIndex = .rightEntityIndex
    End With
 
    If classIndex = leftClassIndex Then
      navPath.navDirectionToClass = etLeft
    ElseIf classIndex = rightClassIndex Then
      navPath.navDirectionToClass = etRight
    Else
      logMsg "incosistent specification of path '" & str & "' supposed to lead to 'Division'", ellError
      Exit Sub
    End If
  End If
End Sub
   
