 Attribute VB_Name = "M22_Class_Utilities"
 Option Explicit
 
 Enum ClassMapping
   cmSuper = 1
   cmSub = 2
   cmOwn = 3
 End Enum
 
 Type NavPathFromClassToClass
   relRefIndex As Integer ' references the relationship which leads to the 'target class'
   navDirection As RelNavigationDirection ' indicates which direction to follow to the 'target class'
 End Type
 
 Type StrListMap
   name As String
   list As String
 End Type
 
 Type StrListMaps
   numMaps As Integer
   maps() As StrListMap
 End Type
 
 Type ClassDescriptor
   sectionName As String
   className As String
   i18nId As String
   aggHeadSection As String
   aggHeadName As String
   classNameLdm As String
   shortName As String
 ' ### IF IVK ###
   lrtClassification As String
   lrtActivationStatusMode As String
 ' ### ENDIF IVK ###
   ignoreForChangelog As Boolean
 ' ### IF IVK ###
   mapOidToClAttribute As String
   navPathStrToDivision As String
   navPathStrToOrg As String
   navPathStrToCodeType As String
   condenseData As Boolean
   isDeletable As Boolean
   enforceLrtChangeComment As Boolean
   entityFilterEnumCriteria As Integer
 ' ### ENDIF IVK ###
   isCommonToOrgs As Boolean
   specificToOrgId As Integer
   isCommonToPools As Boolean
   specificToPool As Integer
   noIndexesInPool As Integer
   useValueCompression As Boolean
   superClassSection As String
   superClass As String
   useSurrogateKey As Boolean
   useVersiontag As Boolean
 ' ### IF IVK ###
   mapping As ClassMapping
 ' ### ENDIF IVK ###
   classId As Integer
 ' ### IF IVK ###
   noRangePartitioning As Boolean
   rangePartitioningAll As Boolean
   rangePartitionGroup As String
   isNationalizable As Boolean
 ' ### ENDIF IVK ###
   isGenForming As Boolean
 ' ### IF IVK ###
   hasNoIdentity As Boolean
   isCore As Boolean
 ' ### ENDIF IVK ###
   isAbstract As Boolean
 ' ### IF IVK ###
   supportAhStatusPropagation As Boolean
   updateMode As DbUpdateMode
   isPsTagged As Boolean
   psTagNotIdentifying As Boolean
   psTagOptional As Boolean
   ignPsRegVarOnInsDel As Boolean
   isPsForming As Boolean
   supportExtendedPsCopy As Boolean
 ' ### ENDIF IVK ###
   logLastChange As Boolean
   logLastChangeAutoMaint As Boolean
   logLastChangeInView As Boolean
 ' ### IF IVK ###
   expandExpressionsInFtoView As Boolean
 ' ### ENDIF IVK ###
   isUserTransactional As Boolean
   isLrtMeta As Boolean
   useMqtToImplementLrt As Boolean
   notAcmRelated As Boolean
   noAlias As Boolean
   noFks As Boolean
 ' ### IF IVK ###
   noXmlExport As Boolean
   useXmlExport As Boolean
 ' ### ENDIF IVK ###
   isLrtSpecific As Boolean
   isPdmSpecific As Boolean
 ' ### IF IVK ###
   includeInPdmExportSeqNo As Integer
 ' ### ENDIF IVK ###
   isVolatile As Boolean
 ' ### IF IVK ###
   notPersisted As Boolean
   isSubjectToArchiving As Boolean
   nonStandardRefTimeStampForArchiving As String
   noTransferToProduction As Boolean
   noFto As Boolean
   ftoSingleObjProcessing As Boolean
 ' ### ENDIF IVK ###

   tabSpaceData As String
   tabSpaceLong As String
   tabSpaceNl As String
   tabSpaceIndex As String
   defaultStatus As Integer

   ' derived attributes
   useLrtCommitPreprocess As Boolean
   hasBusinessKey As Boolean
   classIdStr As String
   classNlIndex As Integer
   aggHeadClassIndex As Integer
   aggHeadClassIndexExact As Integer
   aggHeadClassIdStr As String
   isAggHead As Boolean
   hasSubClass As Boolean
   classIndex As Integer
   superClassIndex As Integer
   subclassIndexes() As Integer
   subclassIndexesRecursive() As Integer
   subclassIdStrListNonAbstract As String
 '  subClassIdStrSeparatePartition As String
   subClassIdStrSeparatePartition As StrListMaps
 'Compiler: 2D-Array
   'subClassPartitionBoundaries(1 To 2, 1 To 10) As String
   aggChildClassIndexes() As Integer
   aggChildRelIndexes() As Integer
   sectionIndex As Integer
   sectionShortName As String
   orMappingSuperClassIndex As Integer
   hasOwnTable As Boolean
   attrRefs As AttrDescriptorRefs
   attrRefsInclSubClasses As AttrDescriptorRefs
   attrRefsInclSubClassesWithRepeat As AttrDescriptorRefs
   clMapAttrs() As AttributeMappingForCl
   clMapAttrsInclSubclasses() As AttributeMappingForCl
   nlAttrRefs As AttrDescriptorRefs
   nlAttrRefsInclSubclasses As AttrDescriptorRefs
   numAttrsInNonGen As Integer
   numAttrsInGen As Integer
   numNlAttrsInNonGen As Integer
   numNlAttrsInGen As Integer
   numRelBasedFkAttrs As Integer
   hasNlAttrsInNonGenInclSubClasses As Boolean
   hasNlAttrsInGenInclSubClasses As Boolean
   hasLabel As Boolean
   hasLabelInGen As Boolean
   indexRefs As IndexDescriptorRefs
   relRefs As RelationshipDescriptorRefs
   relRefsRecursive As RelationshipDescriptorRefs
   implicitelyGenChangeComment As Boolean

   tabSpaceIndexData As Integer
   tabSpaceIndexIndex As Integer
   tabSpaceIndexLong As Integer
   tabSpaceIndexNl As Integer
 ' ### IF IVK ###

   containsIsNotPublished As Boolean
   containsIsNotPublishedInclSubClasses As Boolean
   isPriceAssignment As Boolean
   hasPriceAssignmentSubClass As Boolean
   hasPriceAssignmentAggHead As Boolean
   isSubjectToPreisDurchschuss As Boolean
   subclassIdStrListNonAbstractPriceAssignment As String
   isSubjectToExpCopy As Boolean
   supportXmlExport As Boolean
   hasExpressionInNonGen As Boolean
   hasExpressionInGen As Boolean
   allowedCountriesRelIndex As Integer
   disAllowedCountriesRelIndex As Integer
   allowedCountriesListRelIndex As Integer
   disAllowedCountriesListRelIndex As Integer
   isValidForOrganization As Boolean
   hasOrganizationSpecificReference As Boolean
   relRefsToOrganizationSpecificClasses As RelationshipDescriptorRefs
   hasGroupIdAttrInNonGen As Boolean
   hasGroupIdAttrInNonGenInclSubClasses As Boolean
   hasExpBasedVirtualAttrInNonGen As Boolean
   hasExpBasedVirtualAttrInGen As Boolean
   hasExpBasedVirtualAttrInNonGenInclSubClasses As Boolean
   hasExpBasedVirtualAttrInGenInclSubClasses As Boolean
   hasRelBasedVirtualAttrInNonGen As Boolean
   hasRelBasedVirtualAttrInGen As Boolean
   hasRelBasedVirtualAttrInNonGenInclSubClasses As Boolean
   hasRelBasedVirtualAttrInGenInclSubClasses As Boolean
   hasAttrHasConflict As Boolean
   hasIsNationalInclSubClasses As Boolean
 ' ### ENDIF IVK ###

   ' temporary variables supporting processing
   isLdmCsvExported As Boolean
   isLdmLrtCsvExported As Boolean
   isCtoAliasCreated As Boolean
 ' ### IF IVK ###
   isXsdExported As Boolean
   navPathToDiv As NavPathFromClassToClass
   navPathToOrg As NavPathFromClassToClass
   navPathToCodeType As NavPathFromClassToClass
 
   groupIdAttrIndexes() As Integer
   groupIdAttrIndexesInclSubclasses() As Integer
 ' ### ENDIF IVK ###
 End Type
 
 Type ClassDescriptors
   descriptors() As ClassDescriptor
   numDescriptors As Integer
 End Type

 
 Sub initStrListMaps( _
   ByRef mapping As StrListMaps _
 )
   mapping.numMaps = 0
 End Sub
 
 
 Sub addStrListMapEntry( _
   mapping As StrListMaps, _
   ByVal name As String, _
   list As String _
 )
   name = UCase(name)

     Dim i As Integer
     For i = 1 To mapping.numMaps
       If mapping.maps(i).name = name Then
         Dim elems() As String
         elems = split(list, ",")
         Dim j As Integer
         For j = LBound(elems) To UBound(elems)
           If Not InStr(1, mapping.maps(i).list, elems(j)) Then
             mapping.maps(i).list = mapping.maps(i).list & IIf(mapping.maps(i).list = "", "", ",") & elems(j)
           End If
         Next j
         Exit Sub
       End If
     Next i

     If mapping.numMaps = 0 Then
       ReDim mapping.maps(1 To gc_allocBlockSize)
     ElseIf mapping.numMaps >= UBound(mapping.maps) Then
       ReDim Preserve mapping.maps(1 To mapping.numMaps + gc_allocBlockSize)
     End If

     mapping.numMaps = mapping.numMaps + 1

       mapping.maps(mapping.numMaps).name = name
       mapping.maps(mapping.numMaps).list = list

 End Sub
 
 
 Sub initClassDescriptors( _
   ByRef classes As ClassDescriptors _
 )
   classes.numDescriptors = 0
 End Sub
 
 
 Function allocClassDescriptorIndex( _
   ByRef classes As ClassDescriptors _
 ) As Integer
   allocClassDescriptorIndex = -1

     If classes.numDescriptors = 0 Then
       ReDim classes.descriptors(1 To gc_allocBlockSize)
     ElseIf classes.numDescriptors >= UBound(classes.descriptors) Then
       ReDim Preserve classes.descriptors(1 To classes.numDescriptors + gc_allocBlockSize)
     End If
     classes.numDescriptors = classes.numDescriptors + 1
       initAttrDescriptorRefs classes.descriptors(classes.numDescriptors).attrRefs
       initAttrDescriptorRefs classes.descriptors(classes.numDescriptors).nlAttrRefs
       initAttrDescriptorRefs classes.descriptors(classes.numDescriptors).nlAttrRefsInclSubclasses
       initStrListMaps classes.descriptors(classes.numDescriptors).subClassIdStrSeparatePartition
 
       classes.descriptors(classes.numDescriptors).relRefs.numRefs = 0
     allocClassDescriptorIndex = classes.numDescriptors
 End Function
 
 
 Function getClassMapping( _
   ByRef str As String _
 ) As ClassMapping
   str = UCase(Trim(str & ""))
   If (str = "SUPER") Then
     getClassMapping = cmSuper
   ElseIf (str = "SUB") Then
     getClassMapping = cmSub
   Else
     getClassMapping = cmOwn
   End If
 End Function
 
 
 Sub printChapterHeader( _
   ByRef header As String, _
   fileNo As Integer _
 )
   Print #fileNo,
   Print #fileNo, "-- "; gc_sqlDelimLine1
   Print #fileNo, "-- #"
   Print #fileNo, "-- #    "; header
   Print #fileNo, "-- #"
   Print #fileNo, "-- "; gc_sqlDelimLine1
   Print #fileNo,
 End Sub
 
 
 Function printComment( _
   ByRef comment As String, _
   fileNo As Integer, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 0 _
 ) As String
   printComment = ""

 ' ### IF IVK ###
   If ((outputMode And edomMapHibernate) = 0) And _
      ((outputMode And edomDecl) <> 0) And _
      ((outputMode And edomComment) = 0) And _
      ((outputMode And edomNoDdlComment) = 0) And _
      Not ((outputMode And edomNoSpecifics) = edomNoSpecifics) Then
 ' ### ELSE IVK ###
 ' If ((outputMode And edomDecl) <> 0) And _
 '    ((outputMode And edomComment) = 0) And _
 '    ((outputMode And edomNoDdlComment) = 0) And _
 '    Not ((outputMode And edomNoSpecifics) = edomNoSpecifics) Then
 ' ### ENDIF IVK ###
     If fileNo > 0 Then
       Print #fileNo, addTab(indent); "-- "; comment
     Else
       printComment = addTab(indent) & "-- " & comment
     End If
   End If
 End Function
 
 
 Sub printSectionHeader( _
   ByRef header As String, _
   fileNo As Integer, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional ByRef header2 As String = "" _
 )
 ' ### IF IVK ###
   If ((outputMode And edomMapHibernate) = 0) And _
      ((outputMode And edomDecl) <> 0) And _
      ((outputMode And edomComment) = 0) And _
      ((outputMode And edomNoDdlComment) = 0) And _
      Not ((outputMode And edomNoSpecifics) = edomNoSpecifics) Then
 ' ### ELSE IVK ###
 ' If ((outputMode And edomDecl) <> 0) And _
 '    ((outputMode And edomComment) = 0) And _
 '    ((outputMode And edomNoDdlComment) = 0) And _
 '    Not ((outputMode And edomNoSpecifics) = edomNoSpecifics) Then
 ' ### ENDIF IVK ###
     Print #fileNo,
     Print #fileNo, "-- "; gc_sqlDelimLine2
     Print #fileNo, "--      "; header
     If header2 <> "" Then
       Print #fileNo, "--      "; header2
     End If
     Print #fileNo, "-- "; gc_sqlDelimLine2
   End If
 End Sub
 
 
 Function getClassId( _
   ByRef sectionNo As Integer, _
   ByRef classId As Integer _
 ) As String
   getClassId = Right("00" & sectionNo, 2) & Right("000" & classId, 3)
 End Function
 
 
 Function getClassIdByClassIndex( _
   thisClassIndex As Integer _
 ) As String
   getClassIdByClassIndex = ""
 
   If thisClassIndex > 0 Then
       If g_classes.descriptors(thisClassIndex).classId > 0 Then
         getClassIdByClassIndex = getClassId(getSectionSeqNoByIndex(g_classes.descriptors(thisClassIndex).sectionIndex), g_classes.descriptors(thisClassIndex).classId)
       End If
   End If
 End Function
 
 
 ' ### IF IVK ###
 Sub genNavPathForClass( _
    ByRef navPath As NavPathFromClassToClass, _
    ByRef str As String, _
    ByVal targetClassIndex As Integer _
 )
   Dim list() As String
   list = split("", ".")
   list = split(str, ".")

   If UBound(list) = 1 Then
     Dim relSectionName As String
     Dim relName As String

     relSectionName = list(LBound(list))
     relName = list(LBound(list) + 1)
     'determine the relationship which leads us to Division
     Dim relIndex As Integer
     relIndex = getRelIndexByName(relSectionName, relName)

       If g_relationships.descriptors(relIndex).relName = "" Then
         logMsg "unable to determine relationship '" & str & "' supposed to lead to '" & g_classes.descriptors(targetClassIndex).className & "'", ellError
       Else
         navPath.relRefIndex = relIndex
         If g_relationships.descriptors(relIndex).leftEntityType = eactClass And g_relationships.descriptors(relIndex).leftEntityIndex = targetClassIndex Then
           navPath.navDirection = etLeft
         ElseIf g_relationships.descriptors(relIndex).rightEntityType = eactClass And g_relationships.descriptors(relIndex).rightEntityIndex = targetClassIndex Then
           navPath.navDirection = etRight
         Else
           logMsg "relationship '" & str & "' does not to lead to '" & g_classes.descriptors(targetClassIndex).className & "'", ellError
           navPath.relRefIndex = -1
         End If
       End If
   End If
 End Sub

 
 ' ### ENDIF IVK ###
 Sub addClassIdToList( _
   ByRef classIdList As String, _
   thisClassIndex As Integer, _
   Optional includeSubClasses As Boolean = True _
 )
     If (InStr(1, classIdList, g_classes.descriptors(thisClassIndex).classIdStr) = 0) And Not g_classes.descriptors(thisClassIndex).isAbstract Then
       classIdList = classIdList & IIf(classIdList = "", "", ",") & "'" & g_classes.descriptors(thisClassIndex).classIdStr & "'"
     End If
     If includeSubClasses Then
       Dim i As Integer
       For i = 1 To UBound(g_classes.descriptors(thisClassIndex).subclassIndexesRecursive)
           If (InStr(1, classIdList, g_classes.descriptors(g_classes.descriptors(thisClassIndex).subclassIndexesRecursive(i)).classIdStr) = 0) And Not g_classes.descriptors(g_classes.descriptors(thisClassIndex).subclassIndexesRecursive(i)).isAbstract Then
             classIdList = classIdList & IIf(classIdList = "", "", ",") & "'" & g_classes.descriptors(g_classes.descriptors(thisClassIndex).subclassIndexesRecursive(i)).classIdStr & "'"
           End If
       Next i
     End If
 End Sub
 
