 Attribute VB_Name = "M22_Class"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colSection = 2
 Private Const colClass = colSection + 1
 Private Const colAggHeadSection = colClass + 1
 Private Const colAggHeadName = colAggHeadSection + 1
 Private Const colClassLdm = colAggHeadName + 1
 Private Const colShortName = colClassLdm + 1
 ' ### IF IVK ###
 Private Const colLrtClassification = colShortName + 1
 Private Const colLrtActivationStatusMode = colLrtClassification + 1
 Private Const colEntityFilterEnumCriteria = colLrtActivationStatusMode + 1
 Private Const colIgnoreForChangeLog = colEntityFilterEnumCriteria + 1
 ' ### ELSE IVK ###
 'Private Const colIgnoreForChangeLog = colShortName + 1
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 Private Const colMapToClAttribute = colIgnoreForChangeLog + 1
 Private Const colNavPathToDivision = colMapToClAttribute + 1
 Private Const colNavPathToOrg = colNavPathToDivision + 1
 Private Const colNavPathToCodeType = colNavPathToOrg + 1
 Private Const colCondenseData = colNavPathToCodeType + 1
 Private Const colIsDeletable = colCondenseData + 1
 Private Const colEnforceChangeComment = colIsDeletable + 1
 Private Const colIsCommonToOrgs = colEnforceChangeComment + 1
 ' ### ELSE IVK ###
 'Private Const colIsCommonToOrgs = colIgnoreForChangeLog + 1
 ' ### ENDIF IVK ###
 Private Const colSpecificToOrg = colIsCommonToOrgs + 1
 Private Const colIsCommonToPools = colSpecificToOrg + 1
 Private Const colSpecificToPool = colIsCommonToPools + 1
 Private Const colNoIndexesInPool = colSpecificToPool + 1
 Private Const colUseValueCompression = colNoIndexesInPool + 1
 ' ### IF IVK ###
 Private Const colIsCore = colUseValueCompression + 1
 Private Const colIsAbstract = colIsCore + 1
 ' ### ELSE IVK ###
 'Private Const colIsAbstract = colUseValueCompression + 1
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 Private Const colSupportAhStatusPropagation = colIsAbstract + 1
 Private Const colUpdateMode = colSupportAhStatusPropagation + 1
 Private Const colSuperClassSection = colUpdateMode + 1
 ' ### ELSE IVK ###
 'Private Const colSuperClassSection = colIsAbstract + 1
 ' ### ENDIF IVK ###
 Private Const colSuperClass = colSuperClassSection + 1
 Private Const colUseSurrogateKey = colSuperClass + 1
 Private Const colUseVersionTag = colUseSurrogateKey + 1
 ' ### IF IVK ###
 Private Const colClassMapping = colUseVersionTag + 1
 Private Const colClassId = colClassMapping + 1
 ' ### ELSE IVK ###
 'Private Const colClassId = colUseVersionTag + 1
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 Private Const colNoRangePartitioning = colClassId + 1
 Private Const colRangePartitioningAll = colNoRangePartitioning + 1
 Private Const colRangePartitionGroup = colRangePartitioningAll + 1
 Private Const colIsNationalizable = colRangePartitionGroup + 1
 Private Const colIsGenForming = colIsNationalizable + 1
 ' ### ELSE IVK ###
 'Private Const colIsGenForming = colClassId + 1
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 Private Const colHasNoIdentity = colIsGenForming + 1
 Private Const colIsPsTagged = colHasNoIdentity + 1
 Private Const colPsTagNotIdentifying = colIsPsTagged + 1
 Private Const colPsTagOptional = colPsTagNotIdentifying + 1
 Private Const colIgnPsRegVarOnInsDel = colPsTagOptional + 1
 Private Const colIsPsForming = colIgnPsRegVarOnInsDel + 1
 Private Const colSupportExtendedPsCopy = colIsPsForming + 1
 Private Const colLogLastChange = colSupportExtendedPsCopy + 1
 ' ### ELSE IVK ###
 'Private Const colLogLastChange = colIsGenForming + 1
 ' ### ENDIF IVK ###
 Private Const colLogLastChangeInView = colLogLastChange + 1
 Private Const colLogLastChangeAutoMaint = colLogLastChangeInView + 1
 ' ### IF IVK ###
 Private Const colExpandExpressionsInFtoView = colLogLastChangeAutoMaint + 1
 Private Const colIsUserTransactional = colExpandExpressionsInFtoView + 1
 ' ### ELSE IVK ###
 'Private Const colIsUserTransactional = colLogLastChangeAutoMaint + 1
 ' ### ENDIF IVK ###
 Private Const colUseMqtToImplementLrt = colIsUserTransactional + 1
 Private Const colNotAcmRelated = colUseMqtToImplementLrt + 1
 Private Const colNoAlias = colNotAcmRelated + 1
 Private Const colNoFks = colNoAlias + 1
 ' ### IF IVK ###
 Private Const colNoXmlExport = colNoFks + 1
 Private Const colUseXmlExport = colNoXmlExport + 1
 Private Const colIsLrtSpecific = colUseXmlExport + 1
 ' ### ELSE IVK ###
 'Private Const colIsLrtSpecific = colNoFks + 1
 ' ### ENDIF IVK ###
 Private Const colIsPdmSpecific = colIsLrtSpecific + 1
 ' ### IF IVK ###
 Private Const colIncludeInPdmExportSeqNo = colIsPdmSpecific + 1
 Private Const colIsVolatile = colIncludeInPdmExportSeqNo + 1
 ' ### ELSE IVK ###
 'Private Const colIsVolatile = colIsPdmSpecific + 1
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 Private Const colNotPersisted = colIsVolatile + 1
 Private Const colIsSubjectToArchiving = colNotPersisted + 1
 Private Const colNonStandardRefTimeStampForArchiving = colIsSubjectToArchiving + 1
 Private Const colNoTransferToProduction = colNonStandardRefTimeStampForArchiving + 1
 Private Const colNoFto = colNoTransferToProduction + 1
 Private Const colFtoSingleObjProcessing = colNoFto + 1
 Private Const colTabSpaceData = colFtoSingleObjProcessing + 1
 ' ### ELSE IVK ###
 'Private Const colTabSpaceData = colIsVolatile + 1
 ' ### ENDIF IVK ###
 Private Const colTabSpaceLong = colTabSpaceData + 1
 Private Const colTabSpaceNl = colTabSpaceLong + 1
 Private Const colTabSpaceIndex = colTabSpaceNl + 1
 Private Const colComment = colTabSpaceIndex + 1
 Private Const colI18nId = colComment + 1
 
 Global Const colClassI18nId = colI18nId
 
 Private Const firstRow = 4
 
 Private Const sheetName = "Class"
 
 Private Const processingStep = 2
 Private Const processingStepLrt = 2
 Private Const processingStepPsCopy = 1
 Private Const processingStepPsCopy2 = 2
 Private Const processingStepExpCopy = 6
 Private Const processingStepSetProd = 5
 Private Const processingStepFto = 3
 Private Const processingStepAlias = 3
 Private Const processingStepComment = 4
 Private Const processingStepMiscMeta = 1
 
 Private Const acmCsvProcessingStep = 1
 Global Const ldmCsvTableProcessingStep = 2
 Global Const ldmCsvFkProcessingStep = 3
 Private Const pdmCsvProcessingStep = 3
 
 Global g_classes As ClassDescriptors
 
 
 Private Sub readSheet()
   initClassDescriptors(g_classes)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer

   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   Dim lastSection As String
   While thisSheet.Cells(thisRow, colClass) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).sectionName = Trim(thisSheet.Cells(thisRow, colSection))
       If (g_classes.descriptors(allocClassDescriptorIndex(g_classes)).sectionName & "" = "") Then g_classes.descriptors(allocClassDescriptorIndex(g_classes)).sectionName = lastSection
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).className = Trim(thisSheet.Cells(thisRow, colClass))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).i18nId = Trim(thisSheet.Cells(thisRow, colI18nId))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).aggHeadSection = Trim(thisSheet.Cells(thisRow, colAggHeadSection))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).aggHeadName = Trim(thisSheet.Cells(thisRow, colAggHeadName))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).classNameLdm = Trim(thisSheet.Cells(thisRow, colClassLdm))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).shortName = Trim(thisSheet.Cells(thisRow, colShortName))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).lrtClassification = Trim(thisSheet.Cells(thisRow, colLrtClassification))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).lrtActivationStatusMode = Trim(thisSheet.Cells(thisRow, colLrtActivationStatusMode))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).entityFilterEnumCriteria = getInteger(thisSheet.Cells(thisRow, colEntityFilterEnumCriteria), 0)
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).navPathStrToDivision = Trim(thisSheet.Cells(thisRow, colNavPathToDivision))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).navPathStrToOrg = Trim(thisSheet.Cells(thisRow, colNavPathToOrg))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).navPathStrToCodeType = Trim(thisSheet.Cells(thisRow, colNavPathToCodeType))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).condenseData = getBoolean(thisSheet.Cells(thisRow, colCondenseData))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isDeletable = getBoolean(thisSheet.Cells(thisRow, colIsDeletable))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).enforceLrtChangeComment = getBoolean(thisSheet.Cells(thisRow, colEnforceChangeComment))
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).ignoreForChangelog = getBoolean(thisSheet.Cells(thisRow, colIgnoreForChangeLog))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).mapOidToClAttribute = Trim(thisSheet.Cells(thisRow, colMapToClAttribute))
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isCommonToOrgs = getBoolean(thisSheet.Cells(thisRow, colIsCommonToOrgs))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).specificToOrgId = getInteger(thisSheet.Cells(thisRow, colSpecificToOrg))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isCommonToPools = g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isCommonToOrgs Or getBoolean(thisSheet.Cells(thisRow, colIsCommonToPools))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).specificToPool = getInteger(thisSheet.Cells(thisRow, colSpecificToPool))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).noIndexesInPool = getInteger(thisSheet.Cells(thisRow, colNoIndexesInPool))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).useValueCompression = getBoolean(thisSheet.Cells(thisRow, colUseValueCompression))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isCore = getBoolean(thisSheet.Cells(thisRow, colIsCore))
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isAbstract = getBoolean(thisSheet.Cells(thisRow, colIsAbstract))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).supportAhStatusPropagation = getBoolean(thisSheet.Cells(thisRow, colSupportAhStatusPropagation))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).updateMode = getDbUpdateMode(thisSheet.Cells(thisRow, colUpdateMode))
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).superClassSection = Trim(thisSheet.Cells(thisRow, colSuperClassSection))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).superClass = Trim(thisSheet.Cells(thisRow, colSuperClass))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).useSurrogateKey = getBoolean(thisSheet.Cells(thisRow, colUseSurrogateKey))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).useVersiontag = getBoolean(thisSheet.Cells(thisRow, colUseVersionTag))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).mapping = getClassMapping(thisSheet.Cells(thisRow, colClassMapping))
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).classId = getInteger(thisSheet.Cells(thisRow, colClassId))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).noRangePartitioning = getBoolean(thisSheet.Cells(thisRow, colNoRangePartitioning))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).rangePartitioningAll = getBoolean(thisSheet.Cells(thisRow, colRangePartitioningAll))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).rangePartitionGroup = Trim(thisSheet.Cells(thisRow, colRangePartitionGroup))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isNationalizable = getBoolean(thisSheet.Cells(thisRow, colIsNationalizable))
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isGenForming = getBoolean(thisSheet.Cells(thisRow, colIsGenForming))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).hasNoIdentity = getBoolean(thisSheet.Cells(thisRow, colHasNoIdentity))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isPsTagged = getBoolean(thisSheet.Cells(thisRow, colIsPsTagged))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).psTagNotIdentifying = getBoolean(thisSheet.Cells(thisRow, colPsTagNotIdentifying))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).psTagOptional = getBoolean(thisSheet.Cells(thisRow, colPsTagOptional))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).ignPsRegVarOnInsDel = getBoolean(thisSheet.Cells(thisRow, colIgnPsRegVarOnInsDel))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isPsForming = getBoolean(thisSheet.Cells(thisRow, colIsPsForming))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).supportExtendedPsCopy = getBoolean(thisSheet.Cells(thisRow, colSupportExtendedPsCopy))
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).logLastChange = getBoolean(thisSheet.Cells(thisRow, colLogLastChange))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).logLastChangeInView = getBoolean(thisSheet.Cells(thisRow, colLogLastChangeInView))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).logLastChangeAutoMaint = getBoolean(thisSheet.Cells(thisRow, colLogLastChangeAutoMaint))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).expandExpressionsInFtoView = getBoolean(thisSheet.Cells(thisRow, colExpandExpressionsInFtoView))
 ' ### ENDIF IVK ###
       If UCase(Trim(thisSheet.Cells(thisRow, colIsUserTransactional))) = "M" Then
         g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isLrtMeta = True
         g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isUserTransactional = False
       Else
         g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isUserTransactional = getBoolean(thisSheet.Cells(thisRow, colIsUserTransactional))
       End If
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).useMqtToImplementLrt = getBoolean(thisSheet.Cells(thisRow, colUseMqtToImplementLrt))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).notAcmRelated = getBoolean(thisSheet.Cells(thisRow, colNotAcmRelated))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).noAlias = getBoolean(thisSheet.Cells(thisRow, colNoAlias))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).noFks = getBoolean(thisSheet.Cells(thisRow, colNoFks))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).noXmlExport = getBoolean(thisSheet.Cells(thisRow, colNoXmlExport))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).useXmlExport = getBoolean(thisSheet.Cells(thisRow, colUseXmlExport))
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isLrtSpecific = getBoolean(thisSheet.Cells(thisRow, colIsLrtSpecific))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isPdmSpecific = getBoolean(thisSheet.Cells(thisRow, colIsPdmSpecific))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).includeInPdmExportSeqNo = getInteger(thisSheet.Cells(thisRow, colIncludeInPdmExportSeqNo), -1)
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isVolatile = getBoolean(thisSheet.Cells(thisRow, colIsVolatile))
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).notPersisted = getBoolean(thisSheet.Cells(thisRow, colNotPersisted))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).isSubjectToArchiving = getBoolean(thisSheet.Cells(thisRow, colIsSubjectToArchiving))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).nonStandardRefTimeStampForArchiving = thisSheet.Cells(thisRow, colNonStandardRefTimeStampForArchiving)
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).noTransferToProduction = getBoolean(thisSheet.Cells(thisRow, colNoTransferToProduction))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).noFto = getBoolean(thisSheet.Cells(thisRow, colNoFto))
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).ftoSingleObjProcessing = getBoolean(thisSheet.Cells(thisRow, colFtoSingleObjProcessing))
 ' ### ENDIF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).tabSpaceData = thisSheet.Cells(thisRow, colTabSpaceData)
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).tabSpaceLong = thisSheet.Cells(thisRow, colTabSpaceLong)
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).tabSpaceNl = thisSheet.Cells(thisRow, colTabSpaceNl)
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).tabSpaceIndex = thisSheet.Cells(thisRow, colTabSpaceIndex)
 
 ' ### IF IVK ###
       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).defaultStatus = statusReadyForActivation

       ReDim g_classes.descriptors(allocClassDescriptorIndex(g_classes)).groupIdAttrIndexes(0 To 0)
 ' ### ENDIF IVK ###
       ReDim g_classes.descriptors(allocClassDescriptorIndex(g_classes)).aggChildClassIndexes(0 To 0)
       ReDim g_classes.descriptors(allocClassDescriptorIndex(g_classes)).aggChildRelIndexes(0 To 0)

       lastSection = g_classes.descriptors(allocClassDescriptorIndex(g_classes)).sectionName
 ' ### IF IVK ###

       g_classes.descriptors(allocClassDescriptorIndex(g_classes)).hasGroupIdAttrInNonGen = False
 ' ### ENDIF IVK ###

 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub resetClassesCsvExported()
   Dim i As Integer
 
     For i = 1 To g_classes.numDescriptors Step 1
         g_classes.descriptors(i).isLdmCsvExported = False
         g_classes.descriptors(i).isLdmLrtCsvExported = False
 ' ### IF IVK ###
         g_classes.descriptors(i).isXsdExported = False
 ' ### ENDIF IVK ###
         g_classes.descriptors(i).isCtoAliasCreated = False
     Next i
 End Sub
 
 
 Sub getClasses()
   If g_classes.numDescriptors = 0 Then
     readSheet()
   End If
 End Sub
 
 
 Sub resetClasses()
     g_classes.numDescriptors = 0
     ReDim g_classes.descriptors(1 To 1)
 End Sub
 
 
 Function getClassIndexByName( _
   ByRef sectionName As String, _
   ByRef className As String, _
   Optional silent As Boolean = False _
 ) As Integer
   Dim i As Integer
 
   getClassIndexByName = -1
   If sectionName = "" And className = "" Then
     Exit Function
   End If
 
   For i = 1 To g_classes.numDescriptors Step 1
     If UCase(g_classes.descriptors(i).sectionName) = UCase(sectionName) And _
        UCase(g_classes.descriptors(i).className) = UCase(className) Then
       getClassIndexByName = i
       Exit Function
     End If
   Next i
 
   If Not silent Then
     logMsg("unable to identify class '" & sectionName & "." & className & "'", ellError, edtLdm)
   End If
 End Function
 
 Function getClassIdStrByIndex( _
   classIndex As Integer _
 ) As String
   Dim i As Integer
 
   getClassIdStrByIndex = -1
 
   If classIndex > 0 And classIndex < g_classes.numDescriptors Then
       getClassIdStrByIndex = g_classes.descriptors(classIndex).classIdStr
   End If
 End Function
 
 
 Function getSubClassIdStrListByClassIndex( _
   classIndex As Integer _
 ) As String
 
   Dim subClassIdStrList As String
   subClassIdStrList = ""

     subClassIdStrList = IIf(g_classes.descriptors(classIndex).isAbstract, "", "'" & g_classes.descriptors(classIndex).classIdStr & "'")
     Dim i As Integer
     For i = 1 To UBound(g_classes.descriptors(classIndex).subclassIndexesRecursive)
         If Not g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexesRecursive(i)).isAbstract Then
           subClassIdStrList = subClassIdStrList & IIf(subClassIdStrList = "", "", ",") & "'" & g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexesRecursive(i)).classIdStr & "'"
         End If
     Next i

   getSubClassIdStrListByClassIndex = subClassIdStrList
 End Function
 
 
 Sub getSubClassIdStrListPartitionGroupMap( _
   classIndex As Integer _
 )
 
     Dim i As Integer
     For i = 1 To UBound(g_classes.descriptors(classIndex).subclassIndexesRecursive)
         Dim j As Integer
         For j = 1 To g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexesRecursive(i)).subClassIdStrSeparatePartition.numMaps
             addStrListMapEntry(g_classes.descriptors(classIndex).subClassIdStrSeparatePartition, g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexesRecursive(i)).subClassIdStrSeparatePartition.maps(j).name, g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexesRecursive(i)).subClassIdStrSeparatePartition.maps(j).list)
         Next j
     Next i
 End Sub
 
 
 Private Function getNonAbstractSubClassIdStrListHavingAttrByClassIndex( _
   classIndex As Integer, _
   ByRef attrName As String _
 ) As String

   getNonAbstractSubClassIdStrListHavingAttrByClassIndex = ""

     Dim i As Integer
     For i = 1 To g_classes.descriptors(classIndex).attrRefs.numDescriptors
       If UCase(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).attributeName) = UCase(attrName) Then
         getNonAbstractSubClassIdStrListHavingAttrByClassIndex = g_classes.descriptors(classIndex).subclassIdStrListNonAbstract
         Exit Function
       End If
     Next i
 End Function
 
 
 Function getNonAbstractSubClassIdStrListRecursiveHavingAttrByClassIndex( _
   classIndex As Integer, _
   ByRef attrName As String _
 ) As String
 
   Dim resClassIdStrList As String
   Dim subClassIdStrList As String
   resClassIdStrList = ""

     resClassIdStrList = getNonAbstractSubClassIdStrListHavingAttrByClassIndex(classIndex, attrName)

     If resClassIdStrList <> "" Then
       getNonAbstractSubClassIdStrListRecursiveHavingAttrByClassIndex = resClassIdStrList
       Exit Function
     End If

     Dim i As Integer
     For i = 1 To UBound(g_classes.descriptors(classIndex).subclassIndexes)
         subClassIdStrList = getNonAbstractSubClassIdStrListRecursiveHavingAttrByClassIndex(g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexes(i)).classIndex, attrName)
         If subClassIdStrList <> "" Then
           resClassIdStrList = resClassIdStrList & IIf(resClassIdStrList = "", "", ",") & subClassIdStrList
         End If
     Next i

   getNonAbstractSubClassIdStrListRecursiveHavingAttrByClassIndex = resClassIdStrList
 End Function
 
 
 Function getClassByIndex( _
   classIndex As Integer _
 ) As ClassDescriptor
   If (classIndex > 0) Then getClassByIndex = g_classes.descriptors(classIndex)
 End Function
 
 
 Function getClassIndexByI18nId( _
   ByRef i18nId As String _
 ) As Integer
   Dim i As Integer
 
   getClassIndexByI18nId = -1
 
   For i = 1 To g_classes.numDescriptors Step 1
     If UCase(g_classes.descriptors(i).i18nId) = UCase(i18nId) Then
       getClassIndexByI18nId = i
       Exit Function
     End If
   Next i
 End Function
 
 
 Function getClassShortNameByIndex( _
   ByRef classIndex As Integer _
 ) As String
   getClassShortNameByIndex = ""
   If (classIndex > 0) Then getClassShortNameByIndex = g_classes.descriptors(classIndex).shortName
 End Function
 
 Function getUseSurrogateKeyByClassName( _
   ByRef sectionName As String, _
   ByRef className As String _
 ) As String
   Dim classIndex As Integer
   classIndex = getClassIndexByName(sectionName, className)

   getUseSurrogateKeyByClassName = True
   If (classIndex > 0) Then getUseSurrogateKeyByClassName = g_classes.descriptors(classIndex).useSurrogateKey
 End Function
 
 
 Function getOrMappingSuperClassIndexByClassIndex( _
   ByVal classIndex As Integer _
 ) As Integer
   getOrMappingSuperClassIndexByClassIndex = classIndex

   While (classIndex > 0)
       If g_classes.descriptors(classIndex).superClass = "" Then
         getOrMappingSuperClassIndexByClassIndex = classIndex
         classIndex = -1
       Else
         classIndex = g_classes.descriptors(classIndex).superClassIndex
       End If
   Wend
 End Function
 
 
 Function getOrMappingSuperClass( _
   ByRef sectionName As String, _
   ByRef className As String _
 ) As ClassDescriptor
   Dim classIndex As Integer
   classIndex = getClassIndexByName(sectionName, className)

   getOrMappingSuperClass = g_classes.descriptors(classIndex)

   While (classIndex > 0)
       If g_classes.descriptors(classIndex).superClass = "" Then
         getOrMappingSuperClass = g_classes.descriptors(classIndex)
         classIndex = -1
       Else
         classIndex = g_classes.descriptors(classIndex).superClassIndex
       End If
   Wend
 End Function
 
 
 Function getAttributeIndexByClassIndexAndName( _
   classIndex As Integer, _
   ByRef attrName As String, _
   Optional silent As Boolean = False _
 ) As Integer
   Dim i As Integer
 
   getAttributeIndexByClassIndexAndName = -1
   If classIndex < 0 Or classIndex > g_classes.numDescriptors Then
     Exit Function
   End If
 
     For i = 1 To g_classes.descriptors(classIndex).attrRefsInclSubClasses.numDescriptors
         If g_classes.descriptors(classIndex).attrRefsInclSubClasses.descriptors(i).refIndex > 0 Then
           If UCase(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefsInclSubClasses.descriptors(i).refIndex).attributeName) = UCase(attrName) Then
             getAttributeIndexByClassIndexAndName = g_classes.descriptors(classIndex).attrRefsInclSubClasses.descriptors(i).refIndex
             Exit Function
           End If
         End If
     Next i

     If Not silent Then
       errMsgBox "unable to identify attribute '" & attrName & " in class '" & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & "'", vbCritical
     End If
 End Function
 
 
 Function getAttributeIndexByClassNameAndName( _
   sectionName As String, _
   className As String, _
   ByRef attrName As String, _
   Optional silent As Boolean = False _
 ) As Integer
   Dim classIndex As Integer
   classIndex = getClassIndexByName(sectionName, className)

   getAttributeIndexByClassNameAndName = getAttributeIndexByClassIndexAndName(classIndex, attrName)
 End Function
 
 
 Sub addAggChildClassIndex( _
   thisClassIndex As Integer, _
   aggChildClassIndex As Integer _
 )
   Dim i As Integer

   If (thisClassIndex <= 0) Or (aggChildClassIndex <= 0) Then
     Exit Sub
   End If

     If (g_classes.descriptors(thisClassIndex).orMappingSuperClassIndex = g_classes.descriptors(aggChildClassIndex).orMappingSuperClassIndex) And _
        (g_classes.descriptors(thisClassIndex).classIndex <> g_classes.descriptors(aggChildClassIndex).classIndex) Then
       Exit Sub
     End If

     Dim ub As Integer
     ub = UBound(g_classes.descriptors(thisClassIndex).aggChildClassIndexes)
     For i = 1 To ub
       If g_classes.descriptors(thisClassIndex).aggChildClassIndexes(i) = aggChildClassIndex Then
         Exit Sub
       End If
     Next i

     If ub = 0 Then
       ReDim g_classes.descriptors(thisClassIndex).aggChildClassIndexes(1 To 1)
     Else
       ReDim Preserve g_classes.descriptors(thisClassIndex).aggChildClassIndexes(1 To (ub + 1))
     End If
     g_classes.descriptors(thisClassIndex).aggChildClassIndexes(ub + 1) = aggChildClassIndex
 End Sub
 
 
 Sub addAggChildRelIndex( _
   thisClassIndex As Integer, _
   aggChildRelIndex As Integer _
 )
   Dim i As Integer

   If thisClassIndex <= 0 Or aggChildRelIndex <= 0 Then
     Exit Sub
   End If
 
     If (g_relationships.descriptors(aggChildRelIndex).maxLeftCardinality = 1 Or g_relationships.descriptors(aggChildRelIndex).maxRightCardinality = 1) And (Not g_relationships.descriptors(aggChildRelIndex).isNl) Then
       Exit Sub
     End If

     Dim ub As Integer
     ub = UBound(g_classes.descriptors(thisClassIndex).aggChildRelIndexes)

     For i = 1 To ub
       If g_classes.descriptors(thisClassIndex).aggChildRelIndexes(i) = aggChildRelIndex Then
         Exit Sub
       End If
     Next i

     If ub = 0 Then
       ReDim g_classes.descriptors(thisClassIndex).aggChildRelIndexes(1 To 1)
     Else
       ReDim Preserve g_classes.descriptors(thisClassIndex).aggChildRelIndexes(1 To (ub + 1))
     End If

     g_classes.descriptors(thisClassIndex).aggChildRelIndexes(ub + 1) = aggChildRelIndex
 End Sub
 ' ### IF IVK ###
 
 
 Sub addGroupIdAttrIndex( _
   thisClassIndex As Integer, _
   groupIdAttrIndex As Integer _
 )
   Dim i As Integer

   If (thisClassIndex <= 0) Or (groupIdAttrIndex <= 0) Then
     Exit Sub
   End If

     Dim ub As Integer
     ub = UBound(g_classes.descriptors(thisClassIndex).groupIdAttrIndexes)
     For i = 1 To ub
       If g_classes.descriptors(thisClassIndex).groupIdAttrIndexes(i) = groupIdAttrIndex Then
         Exit Sub
       End If
     Next i

     If ub = 0 Then
       ReDim g_classes.descriptors(thisClassIndex).groupIdAttrIndexes(1 To 1)
     Else
       ReDim Preserve g_classes.descriptors(thisClassIndex).groupIdAttrIndexes(1 To (ub + 1))
     End If
     g_classes.descriptors(thisClassIndex).groupIdAttrIndexes(ub + 1) = groupIdAttrIndex
 End Sub
 
 
 Sub addGroupIdAttrIndexInclSubClasses( _
   thisClassIndex As Integer, _
   groupIdAttrIndex As Integer _
 )
   Dim i As Integer

   If (thisClassIndex <= 0) Or (groupIdAttrIndex <= 0) Then
     Exit Sub
   End If

     Dim ub As Integer
     ub = UBound(g_classes.descriptors(thisClassIndex).groupIdAttrIndexesInclSubclasses)
     For i = 1 To ub
       If g_classes.descriptors(thisClassIndex).groupIdAttrIndexes(i) = groupIdAttrIndex Then
         Exit Sub
       End If
     Next i

     'TF: groupIdAttrIndexes added
     If ub = 0 Then
       ReDim g_classes.descriptors(thisClassIndex).groupIdAttrIndexesInclSubclasses(1 To 1)
       ReDim g_classes.descriptors(thisClassIndex).groupIdAttrIndexes(1 To 1)
     Else
       ReDim Preserve g_classes.descriptors(thisClassIndex).groupIdAttrIndexesInclSubclasses(1 To (ub + 1))
       ReDim Preserve g_classes.descriptors(thisClassIndex).groupIdAttrIndexes(1 To (ub + 1))
     End If
     g_classes.descriptors(thisClassIndex).groupIdAttrIndexesInclSubclasses(ub + 1) = groupIdAttrIndex
 End Sub
 ' ### ENDIF IVK ###
 
 
 Function getDirectSubclassIndexes( _
   thisClassIndex As Integer _
 ) As Integer()
   Dim thisSection As String
   Dim thisClassName As String
   Dim result() As Integer
   Dim resultPos As Integer

   ReDim result(1 To g_classes.numDescriptors)
   resultPos = 0
     thisSection = UCase(g_classes.descriptors(thisClassIndex).sectionName)
     thisClassName = UCase(g_classes.descriptors(thisClassIndex).className)
 
   Dim i As Integer
   For i = 1 To g_classes.numDescriptors Step 1
       If UCase(g_classes.descriptors(i).superClassSection) = thisSection And UCase(g_classes.descriptors(i).superClass) = thisClassName Then
         resultPos = resultPos + 1
         result(resultPos) = i
       End If
   Next i

   If resultPos > 0 Then
     ReDim Preserve result(1 To resultPos)
   Else
     ReDim result(0 To 0)
   End If

   getDirectSubclassIndexes = result
 End Function
 
 
 Private Sub addDirectSubclassIndexes( _
   ByRef indexes() As Integer, _
   ByRef pos As Integer, _
   thisClassIndex As Integer _
 )

   Dim thisI As Integer
     For thisI = 1 To UBound(g_classes.descriptors(thisClassIndex).subclassIndexes)
       If g_classes.descriptors(thisClassIndex).subclassIndexes(thisI) <> thisClassIndex Then
         pos = pos + 1
         indexes(pos) = g_classes.descriptors(thisClassIndex).subclassIndexes(thisI)
         addDirectSubclassIndexes(indexes, pos, g_classes.descriptors(thisClassIndex).subclassIndexes(thisI))
       End If
     Next thisI
 End Sub
 
 
 Function getSubclassIndexesRecursive( _
   thisClassIndex As Integer _
 ) As Integer()
   Dim thisSection As String
   Dim thisClassName As String
   Dim result() As Integer
   Dim resultPos As Integer

   ReDim result(1 To g_classes.numDescriptors)
   resultPos = 0
   addDirectSubclassIndexes(result, resultPos, thisClassIndex)

   If resultPos > 0 Then
     ReDim Preserve result(1 To resultPos)
   Else
     ReDim result(0 To 0)
   End If

   getSubclassIndexesRecursive = result
 End Function
 
 
 Sub addRelRef( _
   ByRef relRefs As RelationshipDescriptorRefs, _
   thisRelIndex As Integer, _
   refType As RelNavigationDirection _
 )
   Dim i As Integer
   For i = 1 To relRefs.numRefs
       If relRefs.refs(i).refIndex = thisRelIndex And relRefs.refs(i).refType = refType Then
         Exit Sub
       End If
   Next i

     relRefs.refs(allocRelDescriptorRefIndex(relRefs)).refIndex = thisRelIndex
     relRefs.refs(allocRelDescriptorRefIndex(relRefs)).refType = refType
 End Sub
 
 
 Private Sub addRelRefsRecursive( _
   ByRef relRefs As RelationshipDescriptorRefs, _
   thisClassIndex As Integer _
 )

   Dim thisI As Integer
   Dim thisR As Integer
     For thisR = 1 To g_classes.descriptors(thisClassIndex).relRefs.numRefs
         relRefs.refs(allocRelDescriptorRefIndex(relRefs)).refIndex = g_classes.descriptors(thisClassIndex).relRefs.refs(thisR).refIndex
         relRefs.refs(allocRelDescriptorRefIndex(relRefs)).refType = g_classes.descriptors(thisClassIndex).relRefs.refs(thisR).refType
     Next thisR

     For thisI = 1 To UBound(g_classes.descriptors(thisClassIndex).subclassIndexes)
       addRelRefsRecursive(relRefs, g_classes.descriptors(thisClassIndex).subclassIndexes(thisI))
     Next thisI
 End Sub
 
 Function getRelRefsRecursive( _
   thisClassIndex As Integer _
 ) As RelationshipDescriptorRefs
   Dim thisSection As String, thisClassName As String
   Dim result As RelationshipDescriptorRefs

   addRelRefsRecursive(result, thisClassIndex)

   getRelRefsRecursive = result
 End Function
 
 
 Sub genTransformedAttrDeclsForClassRecursiveWithColReUse( _
   ByRef classIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional level As Integer = 1, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forGen As Boolean = False, _
   Optional suppressMetaAttrs As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional direction As RecursionDirection = erdDown, _
   Optional ByRef attrIsReUsed As Boolean = False, _
   Optional ByRef genParentTabName As String = "", _
   Optional suppressColConstraints As Boolean = False, _
   Optional useAlternativeDefaults As Boolean = False, _
   Optional forceComma As Boolean = False _
 )
   Dim addComma As Boolean
   Dim hasMetaAttrs As Boolean
   Dim forSubClass As Boolean
   Dim useVersiontag As Boolean

   On Error GoTo ErrorExit

     useVersiontag = (level = 1) And (Not suppressMetaAttrs) And g_classes.descriptors(classIndex).useVersiontag
 ' ### IF IVK ###
     hasMetaAttrs = useVersiontag Or _
                    g_classes.descriptors(classIndex).isPsTagged Or _
                    (Not forGen And g_classes.descriptors(classIndex).isNationalizable) Or _
                    ((forGen Or g_classes.descriptors(classIndex).hasNoIdentity) And g_classes.descriptors(classIndex).isGenForming) Or _
                    (g_classes.descriptors(classIndex).logLastChange And (Not forGen Or g_cfgGenLogChangeForGenTabs))
 ' ### ELSE IVK ###
 '   hasMetaAttrs = useVersiontag Or _
 '                  (forGen And .isGenForming) Or _
 '                  (.logLastChange And (Not forGen Or g_cfgGenLogChangeForGenTabs))
 ' ### ENDIF IVK ###
     forSubClass = IIf(direction = erdDown, level > 1, g_classes.descriptors(classIndex).superClassIndex > 0)

     If direction = erdUp And g_classes.descriptors(classIndex).superClass <> "" Then
       ' recurse to parent class
       genTransformedAttrDeclsForClassRecursiveWithColReUse(g_classes.descriptors(classIndex).superClassIndex, _
         transformation, tabColumns, level + 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, _
         suppressMetaAttrs, forLrt, outputMode, direction, , , , , forceComma Or useVersiontag Or ((g_classes.descriptors(classIndex).numAttrsInNonGen - g_classes.descriptors(classIndex).numNlAttrsInNonGen) > 0))
     End If

     If (level > 1) Then
       printSectionHeader(_
         "private attributes for subclass """ & UCase(g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className) & _
         IIf(g_classes.descriptors(classIndex).classIdStr <> "", """ (ClassId='" & g_classes.descriptors(classIndex).classIdStr & "')", ""), fileNo, outputMode)
     End If

     ' Fixme: add 'derived columns in ClassDescriptor'
     ' ######################################################
     Dim i As Integer
     Dim numAttrsInSubclasses As Integer
     Dim numRelBasedFkAttrsInclSubclasses As Integer
     numAttrsInSubclasses = 0
     numRelBasedFkAttrsInclSubclasses = g_classes.descriptors(classIndex).numRelBasedFkAttrs
     If direction = erdDown Then
       For i = 1 To UBound(g_classes.descriptors(classIndex).subclassIndexes) Step 1
           numAttrsInSubclasses = numAttrsInSubclasses + IIf(forGen, g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexes(i)).numAttrsInGen, g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexes(i)).numAttrsInNonGen)
           If Not forGen Then numRelBasedFkAttrsInclSubclasses = numRelBasedFkAttrsInclSubclasses + g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexes(i)).numRelBasedFkAttrs
       Next i
     End If

     addComma = _
       forceComma Or _
       hasMetaAttrs Or _
       (numAttrsInSubclasses > 0) Or _
       (numRelBasedFkAttrsInclSubclasses > 0)

     genTransformedAttrDeclsForEntityWithColReUse(eactClass, g_classes.descriptors(classIndex).classIndex, _
       transformation, tabColumns, forSubClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, _
       g_classes.descriptors(classIndex).useSurrogateKey, g_classes.descriptors(classIndex).isGenForming, forGen, suppressMetaAttrs Or (direction = erdUp And g_classes.descriptors(classIndex).superClass <> ""), direction = erdUp, _
       g_classes.descriptors(classIndex).isUserTransactional, Not addComma, _
       forLrt, outputMode, indent, , genParentTabName, suppressColConstraints, useAlternativeDefaults)

     addComma = _
       forceComma Or _
       hasMetaAttrs Or _
       (numAttrsInSubclasses > 0)

     genTransformedAttrDeclForRelationshipsByClassWithColReuse(classIndex, transformation, tabColumns, (direction = erdDown) And (level > 1), _
       fileNo, ddlType, thisOrgIndex, thisPoolIndex, forGen, outputMode, indent, addComma, direction = erdUp)

     If direction = erdDown Then
       For i = 1 To UBound(g_classes.descriptors(classIndex).subclassIndexes) Step 1
           numAttrsInSubclasses = numAttrsInSubclasses - IIf(forGen, g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexes(i)).numAttrsInGen, g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexes(i)).numAttrsInNonGen)

         addComma = _
           forceComma Or _
           hasMetaAttrs Or _
           (numAttrsInSubclasses > 0)

         genTransformedAttrDeclsForClassRecursiveWithColReUse(_
            g_classes.descriptors(classIndex).subclassIndexes(i), _
            transformation, tabColumns, level + 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, _
            forLrt, outputMode, erdDown, , , , , addComma)
       Next i
     End If

     If level = 1 Then
       If Not suppressMetaAttrs Then
 ' ### IF IVK ###
         If g_classes.descriptors(classIndex).isPsTagged Then
           addComma = _
             forceComma Or _
             useVersiontag Or _
             (Not forGen And g_classes.descriptors(classIndex).isNationalizable) Or _
             ((forGen Or g_classes.descriptors(classIndex).hasNoIdentity) And g_classes.descriptors(classIndex).isGenForming) Or _
             (g_classes.descriptors(classIndex).logLastChange And (Not forGen Or g_cfgGenLogChangeForGenTabs))

           printSectionHeader("Product Structure Tag", fileNo, outputMode)
           printConditional(fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conPsOid, cosnPsOid, eavtDomain, g_domainIndexOid, transformation, tabColumns, _
               eactClass, classIndex, IIf(g_classes.descriptors(classIndex).psTagOptional, "", "NOT NULL"), addComma, ddlType, , outputMode, eacPsOid, , indent, , _
               "[LDM] Product Structure Tag" _
             ))
         End If
         If Not forGen And g_classes.descriptors(classIndex).isNationalizable Then
           addComma = _
             forceComma Or _
             useVersiontag Or _
             ((forGen Or g_classes.descriptors(classIndex).hasNoIdentity) And g_classes.descriptors(classIndex).isGenForming) Or _
             (g_classes.descriptors(classIndex).logLastChange And (Not forGen Or g_cfgGenLogChangeForGenTabs))

           printSectionHeader("Is this a 'nationalized' entity?", fileNo, outputMode)
           printConditional(fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conIsNational, cosnIsNational, eavtDomain, g_domainIndexBoolean, transformation, _
               tabColumns, eactClass, classIndex, "NOT NULL", addComma, ddlType, , outputMode, _
               eacNationalEntityMeta Or eacRegular, , indent, , _
               "[LDM] Is this a 'nationalized' entity?", "0" _
             ))
         End If
         If (forGen Or g_classes.descriptors(classIndex).hasNoIdentity) And g_classes.descriptors(classIndex).isGenForming Then
 ' ### ELSE IVK ###
 '       If forGen  And .isGenForming Then
 ' ### ENDIF IVK ###
           addComma = _
             forceComma Or _
             useVersiontag Or _
             (g_classes.descriptors(classIndex).logLastChange And (Not forGen Or g_cfgGenLogChangeForGenTabs))

           printSectionHeader("Validity Range", fileNo, outputMode)
           printConditional(fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conValidFrom, cosnValidFrom, eavtDomain, g_domainIndexValTimestamp, transformation, _
               tabColumns, eactClass, classIndex, "NOT NULL", , ddlType, , outputMode, , , indent, , _
               "[ACM] Begin timestamp of record's validity range" _
             ))
           printConditional(fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conValidTo, cosnValidTo, eavtDomain, g_domainIndexValTimestamp, transformation, _
               tabColumns, eactClass, classIndex, "NOT NULL", addComma, ddlType, , outputMode, , , indent, , _
               "[ACM] End timestamp of record's validity range" _
             ))
         End If

         If g_classes.descriptors(classIndex).logLastChange And (Not forGen Or g_cfgGenLogChangeForGenTabs) Then
           addComma = _
             forceComma Or _
             useVersiontag

           If Not forLrt Or g_cfgGenLogChangeForLrtTabs Then
             genTransformedLogChangeAttrDeclsWithColReUse(fileNo, transformation, tabColumns, eactClass, classIndex, ddlType, g_classes.descriptors(classIndex).className, outputMode, indent, addComma, useAlternativeDefaults)
           ElseIf forLrt And Not g_cfgGenLogChangeForLrtTabs And (outputMode And edomValueNonLrt) Then
             genTransformedLogChangeAttrDeclsWithColReUse(fileNo, transformation, tabColumns, eactClass, classIndex, ddlType, g_classes.descriptors(classIndex).className, edomValueNonLrt, indent, addComma, useAlternativeDefaults)
           End If
         End If
 
         If g_classes.descriptors(classIndex).useVersiontag Then
           printSectionHeader("Object Version ID", fileNo, outputMode)
           printConditional(fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conVersionId, cosnVersionId, eavtDomain, g_domainIndexVersion, transformation, tabColumns, _
               eactClass, classIndex, "NOT NULL DEFAULT 1" & IIf(ddlType = edtPdm And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), _
               forceComma, ddlType, , outputMode, eacVid, , indent, , _
               "[LDM] Record version tag", "1" _
             ))
         End If
       End If
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Sub genAttrDeclsForClassRecursiveWithColReUse( _
   ByRef classIndex As Integer, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional level As Integer = 1, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forGen As Boolean = False, _
   Optional suppressMetaAttrs As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional direction As RecursionDirection = erdDown, _
   Optional ByRef genParentTabName As String = "", _
   Optional suppressColConstraints As Boolean = False, _
   Optional useAlternativeDefaults As Boolean = False _
 )
   On Error GoTo ErrorExit

   genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, nullAttributeTransformation, tabColumns, level, _
     fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, _
     forLrt, outputMode, direction, , genParentTabName, suppressColConstraints, useAlternativeDefaults)
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genAttrDeclsForClassRecursive( _
   ByRef classIndex As Integer, _
   Optional level As Integer = 1, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forGen As Boolean = False, _
   Optional suppressMetaAttrs As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt _
 )
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   On Error GoTo ErrorExit

   genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, nullAttributeTransformation, tabColumns, level, _
     fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode)
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrDeclsForRelationship( _
   thisRelIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forGen As Boolean = False, _
   Optional suppressMetaAttrs As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt _
 )
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   On Error GoTo ErrorExit

   genTransformedAttrDeclsForRelationshipWithColReUse(thisRelIndex, transformation, tabColumns, fileNo, ddlType, _
                 thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode)
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Sub genAttrDeclsForRelationship( _
   thisRelIndex As Integer, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forGen As Boolean = False, _
   Optional suppressMetaAttrs As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt _
 )
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   On Error GoTo ErrorExit

   genTransformedAttrDeclsForRelationshipWithColReUse(thisRelIndex, nullAttributeTransformation, tabColumns, fileNo, ddlType, _
                 thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode)
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genAttrDeclsForEnum( _
   thisEnumIndex As Integer, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional outputMode As DdlOutputMode = edomDecl _
 )
   On Error GoTo ErrorExit

   genAttrDeclsForEntity(eactEnum, thisEnumIndex, False, fileNo, ddlType, thisOrgIndex, thisPoolIndex, False, , , , , , , outputMode)
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrDeclsForEnum( _
   thisEnumIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional outputMode As DdlOutputMode = edomListLrt, _
   Optional useVersiontag As Boolean = True _
 )
   On Error GoTo ErrorExit

     genTransformedAttrDeclsForEntity(eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, transformation, False, fileNo, ddlType, thisOrgIndex, thisPoolIndex, _
       False, False, False, False, , Not useVersiontag, False, outputMode, indent, , eactEnum)
 
     printSectionHeader("Object Version ID", fileNo, outputMode)
     printConditional(fileNo, _
       genTransformedAttrDeclByDomain( _
         conVersionId, cosnVersionId, eavtDomain, g_domainIndexVersion, transformation, _
         eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL DEFAULT 1" & IIf(ddlType = edtPdm And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), _
         False, ddlType, , outputMode, eacVid, , indent _
       ))
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrDeclsForEnumWithColReuse( _
   thisEnumIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional outputMode As DdlOutputMode = edomListLrt, _
   Optional useVersiontag As Boolean = True _
 )
     genTransformedAttrDeclsForEntityWithColReUse(eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, transformation, tabColumns, False, _
       fileNo, ddlType, thisOrgIndex, thisPoolIndex, False, False, False, False, , , Not useVersiontag, False, outputMode, indent)
 
     If useVersiontag Then
       printSectionHeader("Object Version ID", fileNo, outputMode)
       printConditional(fileNo, _
         genTransformedAttrDeclByDomain( _
           conVersionId, cosnVersionId, eavtDomain, g_domainIndexVersion, transformation, _
           eactEnum, g_enums.descriptors(thisEnumIndex).enumIndex, "NOT NULL DEFAULT 1" & IIf(ddlType = edtPdm And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), _
           False, ddlType, , outputMode, eacVid, , indent _
         ))
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genDbObjComment( _
   ByRef objType As String, _
   ByRef objName As String, _
   ByRef objComment As String, _
   fileNo As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If objComment <> "" Then
     Dim commentMeta As String

     commentMeta = ""
     If thisOrgIndex >= 0 Then
       commentMeta = "[MPC" & genOrgId(thisOrgIndex, ddlType)
     End If
     If thisPoolIndex >= 0 Then
       commentMeta = commentMeta & ",DP" & genPoolId(thisPoolIndex, ddlType) & "] "
     End If

     Print #fileNo, "COMMENT ON " & UCase(objType) & " " & objName & " IS " & "'" & commentMeta & Replace(objComment, "'", "''") & "'"; gc_sqlCmdDelim
   End If
 End Sub
 
 
 Private Sub genDbAlias( _
   ByRef qualAliasName As String, _
   ByRef qualRefObj As String, _
   ByRef qualRefObjLdm As String, _
   ByRef objName As String, _
   ByRef tabDescr As String, _
   ByRef sectionIndex As Integer, _
   Optional forLrt As Boolean = False, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef extraComment As String = "" _
 )
   Dim fileNoAl As Integer
   fileNoAl = _
     openDdlFileBySectionIndex( _
       g_targetDir, sectionIndex, processingStepAlias, edtPdm, thisOrgIndex, thisPoolIndex, , g_phaseIndexAliases, ldmIterationPostProc _
     )

   On Error GoTo ErrorExit
   printSectionHeader("Alias for " & tabDescr & IIf(Not forLrt, "", " (LRT)") & IIf(extraComment = "", "", " (" & extraComment & ")"), fileNoAl)
 
   Print #fileNoAl,
   Print #fileNoAl, "CREATE ALIAS " & qualAliasName & " FOR " & qualRefObj; gc_sqlCmdDelim

   If generateCommentOnAliases Then
     Print #fileNoAl,
     genDbObjComment("ALIAS", qualAliasName, tabDescr & IIf(forLrt, " (LRT)", ""), fileNoAl, thisOrgIndex, thisPoolIndex)
   End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 'added paramter withTempTable (Defect 19001 wf)
 Sub genTabSubQueryByEntityIndex( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   fileNo As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ddlType As DdlTypeId, _
   lrtAware As Boolean, _
   forGen As Boolean, _
   ByRef tabVar As String, _
   ByVal columnList As String, _
   Optional indent As Integer = 1, _
   Optional ByRef oidVar As String = "", _
   Optional ByRef lrtOidVar As String = "lrtOid_in", _
   Optional withTempTable As Boolean = True _
 )
   Dim thisOrgId As Integer
   Dim thisPoolId As Integer
   If thisOrgIndex > 0 Then thisOrgId = g_orgs.descriptors(thisOrgIndex).id Else thisOrgId = -1
   If thisPoolIndex > 0 Then thisPoolId = g_pools.descriptors(thisPoolIndex).id Else thisPoolId = -1

   If Left(columnList, Len(g_anOid)) <> g_anOid Then
     columnList = g_anOid & IIf(columnList = "", "", ",") & columnList
   End If

   If acmEntityType = eactClass Then
       Dim parFkAttrName As String
       parFkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(acmEntityIndex).shortName)

       If lrtAware And g_classes.descriptors(acmEntityIndex).isUserTransactional Then
         If g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt Then
           If oidVar = "" And lrtOidVar = "" And Not forGen Then
             Print #fileNo, addTab(indent + 0); genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True); " "; tabVar
           Else
             Print #fileNo, addTab(indent + 0); "("
             If withTempTable Then
               If forGen Then
                 Print #fileNo, addTab(indent + 1); "SELECT"
 ' ### IF IVK ###
                 Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",LRTSTATE,"; g_anIsLrtPrivate; ","; g_anIsDeleted; ","; g_anValidFrom; ","; g_anValidTo; ","; _
                                                  "ROWNUMBER() OVER (PARTITION BY " & parFkAttrName & ","; g_anInLrt; " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
                                                  "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
 ' ### ELSE IVK ###
 '             Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",LRTSTATE," ; g_anIsLrtPrivate; ","; g_anValidFrom; ","; g_anValidTo; ","; _
 '                                                "ROWNUMBER() OVER (PARTITION BY " & parFkAttrName & ","; g_anInLrt; " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
 '                                                "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
 ' ### ENDIF IVK ###
                 Print #fileNo, addTab(indent + 1); "FROM"
                 Print #fileNo, addTab(indent + 2); "("
                 indent = indent + 2
               End If
 ' ### IF IVK ###
               Print #fileNo, addTab(indent + 1); "SELECT "; columnList; ","; g_anInLrt; ",LRTSTATE,"; g_anIsLrtPrivate; ","; g_anIsDeleted; IIf(forGen, "," & g_anValidFrom & "," & g_anValidTo, ""); " FROM "; _
                                                genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True); _
                                                IIf(oidVar <> "" Or lrtOidVar <> "", " WHERE ", ""); _
                                                IIf(lrtOidVar = "", "", "((" & g_anIsLrtPrivate & " = 0 AND " & g_anIsDeleted & " = 0 AND ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))) OR " & _
                                                "(" & g_anIsLrtPrivate & " = 1 AND LRTSTATE <> " & CStr(lrtStatusDeleted) & " AND (" & g_anInLrt & " = " & lrtOidVar & ")))"); _
                                                IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
 ' ### ELSE IVK ###
 '           Print #fileNo, addTab(indent + 1); "SELECT "; columnList; ","; g_anInLrt; ",LRTSTATE," ; g_anIsLrtPrivate; ""; IIf(forGen, "," & g_anValidFrom;  & "," & g_anValidTo, ""); " FROM "; _
 '                                              genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True); _
 '                                              IIf(oidVar <> "" Or lrtOidVar <> "", " WHERE ", ""); _
 '                                              IIf(lrtOidVar = "", "", "((" & g_anIsLrtPrivate & " = 0 AND ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))) OR " & _
 '                                              "(" & g_anIsLrtPrivate & " = 1 AND LRTSTATE <> " & CStr(lrtStatusDeleted) & " AND (" & g_anInLrt & " = " & lrtOidVar & ")))"); _
 '                                              IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
 ' ### ENDIF IVK ###
               If forGen Then
                 indent = indent - 2
                 Print #fileNo, addTab(indent + 2); ") G_"; tabVar
               End If
             ' Branch for modification of View V_CL_GENERICASPECT (Defect 19001 wf)
             ' withTempTable = False
             Else
               If forGen Then
                 Print #fileNo, addTab(indent + 1); "SELECT"
 ' ### IF IVK ###
                 Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",LRTSTATE,"; g_anIsLrtPrivate; ","; g_anIsDeleted; ","; g_anValidFrom; ","; g_anValidTo; ","; _
                                                  "ROWNUMBER() OVER (PARTITION BY " & parFkAttrName & ","; g_anInLrt; " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
                                                  "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
                 Print #fileNo, addTab(indent + 1); "FROM"
                 indent = indent + 2
               End If
 ' ### IF IVK ###
               Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True);
               If forGen Then
                 indent = indent - 2
               End If
             End If
             Print #fileNo, addTab(indent + 0); ") "; tabVar
           End If
         Else
           Print #fileNo, addTab(indent + 0); "("
           If forGen Then
             Print #fileNo, addTab(indent + 1); "SELECT"
 ' ### IF IVK ###
             Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",CAST(0 AS "; g_dbtEnumId; ") AS LRTSTATE,CAST(0 AS "; g_dbtBoolean; ") AS "; g_anIsLrtPrivate; ","; g_anIsDeleted; ","; g_anValidFrom; ","; g_anValidTo; ","; _
                                                "ROWNUMBER() OVER (PARTITION BY " & parFkAttrName & ","; g_anInLrt; " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
                                                "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
 ' ### ELSE IVK ###
 '           Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",CAST(0 AS "; g_dbtEnumId; ") AS LRTSTATE,CAST(0 AS "; g_dbtBoolean; ") AS " ; g_anIsLrtPrivate; ","; g_anValidFrom; ","; g_anValidTo; ","; _
 '                                              "ROWNUMBER() OVER (PARTITION BY " & parFkAttrName & ","; g_anInLrt; " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
 '                                              "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
 ' ### ENDIF IVK ###
             Print #fileNo, addTab(indent + 1); "FROM"
             Print #fileNo, addTab(indent + 2); "("
             indent = indent + 2
           End If

 ' ### IF IVK ###
           Print #fileNo, addTab(indent + 1); "SELECT "; columnList; ","; g_anInLrt; ",CAST(0 AS "; g_dbtEnumId; ") AS LRTSTATE,CAST(0 AS "; g_dbtBoolean; ") AS "; g_anIsLrtPrivate; ","; g_anIsDeleted; ""; IIf(forGen, "," & g_anValidFrom & "," & g_anValidTo, ""); " FROM "; _
                                              genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, False); _
                                              " WHERE ("; g_anIsDeleted; " = 0)"; _
                                              IIf(lrtOidVar = "", "", " AND ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))"); _
                                              IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
 ' ### ELSE IVK ###
 '        Print #fileNo, addTab(indent + 1); "SELECT "; columnList; ","; g_anInLrt; ",CAST(0 AS "; g_dbtEnumId; ") AS LRTSTATE,CAST(0 AS "; g_dbtBoolean; ") AS " ; g_anIsLrtPrivate; ""; IIf(forGen, "," & g_anValidFrom & "," & g_anValidTo, ""); " FROM "; _
 '                                           genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, False); _
 '                                           IIf(lrtOidVar = "", "", " WHERE ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))"); _
 '                                           IIf(oidVar <> "", IIf(lrtOidVar = "", " WHERE", " AND") & " (OID = " & oidVar & ")", "")
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(indent + 2); "UNION ALL"
 ' ### IF IVK ###
           Print #fileNo, addTab(indent + 1); "SELECT "; columnList; ","; g_anInLrt; ",LRTSTATE,CAST(1 AS "; g_dbtBoolean; ") AS "; g_anIsLrtPrivate; ",CAST(0 AS "; g_dbtBoolean; ") AS "; g_anIsDeleted; ""; IIf(forGen, "," & g_anValidFrom & "," & g_anValidTo, ""); " FROM "; _
                                              genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True); _
                                              " WHERE (LRTSTATE <> " & CStr(lrtStatusDeleted) & ")"; _
                                              IIf(lrtOidVar = "", "", " AND (" & g_anInLrt & " = " & lrtOidVar & ")"); _
                                              IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
 ' ### ELSE IVK ###
 '         Print #fileNo, addTab(indent + 1); "SELECT "; columnList; ","; g_anInLrt; ",LRTSTATE,CAST(1 AS "; g_dbtBoolean; ") AS " ; g_anIsLrtPrivate; ""; IIf(forGen, "," & g_anValidFrom & "," & g_anValidTo, ""); " FROM "; _
 '                                            genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True); _
 '                                            " WHERE (LRTSTATE <> " & CStr(lrtStatusDeleted) & ")"; _
 '                                            IIf(lrtOidVar = "", "", " AND (" & g_anInLrt & " = " & lrtOidVar & ")"); _
 '                                            IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
 ' ### ENDIF IVK ###
           If forGen Then
             indent = indent - 2
             Print #fileNo, addTab(indent + 2); ") G_"; tabVar
           End If
           Print #fileNo, addTab(indent + 0); ") "; tabVar
         End If
       Else
         If forGen Then
           Print #fileNo, addTab(indent + 0); "("
           Print #fileNo, addTab(indent + 1); "SELECT"
 ' ### IF IVK ###
           Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",CAST(0 AS "; g_dbtEnumId; ") AS LRTSTATE,CAST(0 AS "; g_dbtBoolean; ") AS "; g_anIsLrtPrivate; ","; g_anIsDeleted; ","; g_anValidFrom; ","; g_anValidTo; ","; _
                                              "ROWNUMBER() OVER (PARTITION BY "; parFkAttrName; " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
                                              "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
 ' ### ELSE IVK ###
 '         Print #fileNo, addTab(indent + 2); columnList; ","; g_anInLrt; ",CAST(0 AS "; g_dbtEnumId; ") AS LRTSTATE,CAST(0 AS "; g_dbtBoolean; ") AS " ; g_anIsLrtPrivate; ","; g_anValidFrom; ","; g_anValidTo; ","; _
 '                                            "ROWNUMBER() OVER (PARTITION BY " & parFkAttrName & " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) " & _
 '                                            "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(indent + 1); "FROM"
           Print #fileNo, addTab(indent + 2); genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen)
           Print #fileNo, addTab(indent + 0); ") "; tabVar
         Else
           Print #fileNo, addTab(indent + 0); genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen); " "; tabVar
         End If
       End If
   ElseIf acmEntityType = eactRelationship Then
       If lrtAware And g_relationships.descriptors(acmEntityIndex).isUserTransactional Then
         If g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt Then
           If oidVar = "" And lrtOidVar = "" Then
             Print #fileNo, addTab(indent + 0); genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True); " "; tabVar
           Else
             Print #fileNo, addTab(indent + 0); "("
 ' ### IF IVK ###
             Print #fileNo, addTab(indent + 1); "SELECT "; columnList; " FROM "; _
                                                genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True); _
                                                IIf(oidVar <> "" Or lrtOidVar <> "", " WHERE ", ""); _
                                                IIf(lrtOidVar = "", "", "((" & g_anIsLrtPrivate & " = 0 AND " & g_anIsDeleted & " = 0 AND ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))) OR " & _
                                                "(" & g_anIsLrtPrivate & " = 1 AND LRTSTATE <> " & CStr(lrtStatusDeleted) & " AND (" & g_anInLrt & " = " & lrtOidVar & ")))"); _
                                                IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
 ' ### ELSE IVK ###
 '           Print #fileNo, addTab(indent + 1); "SELECT "; columnList; " FROM "; _
 '                                              genQualTabNameByRelIndex(.relIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True); _
 '                                              IIf(oidVar <> "" Or lrtOidVar <> "", " WHERE ", ""); _
 '                                              IIf(lrtOidVar = "", "", "((" & g_anIsLrtPrivate & " = 0 AND ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))) OR " & _
 '                                              "(" & g_anIsLrtPrivate & " = 1 AND LRTSTATE <> " & CStr(lrtStatusDeleted) & " AND (" & g_anInLrt & " = " & lrtOidVar & ")))"); _
 '                                              IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
 ' ### ENDIF IVK ###
             Print #fileNo, addTab(indent + 0); ") "; tabVar
           End If
         Else
           Print #fileNo, addTab(indent + 0); "("
 ' ### IF IVK ###
           Print #fileNo, addTab(indent + 1); "SELECT "; columnList; " FROM "; _
                                              genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, False); _
                                              " WHERE ("; g_anIsDeleted; " = 0)"; _
                                              IIf(lrtOidVar = "", "", " AND ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))"); _
                                              IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
 ' ### ELSE IVK ###
 '        Print #fileNo, addTab(indent + 1); "SELECT "; columnList; " FROM "; _
 '                                           genQualTabNameByRelIndex(.relIndex, ddlType, thisOrgIndex, thisPoolIndex, False); _
 '                                           IIf(lrtOidVar = "", "", " WHERE ((" & g_anInLrt & " IS NULL) OR (" & g_anInLrt & " <> " & lrtOidVar & "))"); _
 '                                           IIf(oidVar <> "", IIf(lrtOidVar = "", " WHERE", " AND") & " (OID = " & oidVar & ")", "")
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(indent + 2); "UNION ALL"
           Print #fileNo, addTab(indent + 1); "SELECT "; columnList; " FROM "; _
                                              genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, True); _
                                              " WHERE (LRTSTATE <> "; CStr(lrtStatusDeleted); ")"; _
                                              IIf(lrtOidVar = "", "", " AND (" & g_anInLrt & " = " & lrtOidVar & ")"); _
                                              IIf(oidVar <> "", " AND (OID = " & oidVar & ")", "")
           Print #fileNo, addTab(indent + 0); ") "; tabVar
         End If
       Else
         Print #fileNo, addTab(3); genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex); " "; tabVar
       End If
   End If
 End Sub
 
 
 ' ### IF IVK ###
 Private Sub genGetCodePropertyGroupByPriceAssignmentFunction( _
   fileNo As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If
 
   Dim qualObjNameCpGroupHasProperty As String

   Dim isWorkDataPool As Boolean
   Dim isProductiveDataPool As Boolean
   Dim isArchiveDataPool As Boolean
   Dim poolSupportLrt As Boolean

   If thisPoolIndex > 0 Then
       isWorkDataPool = g_pools.descriptors(thisPoolIndex).supportLrt
       isProductiveDataPool = g_pools.descriptors(thisPoolIndex).isProductive
       isArchiveDataPool = g_pools.descriptors(thisPoolIndex).isArchive
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt

       If isArchiveDataPool Then
         Exit Sub
       End If
   End If

   Dim qualFuncName As String
 
     Dim lrtAware As Boolean
     Dim k As Integer
     For k = 1 To IIf(poolSupportLrt, 2, 1)
       lrtAware = (k = 2)
       qualFuncName = genQualFuncName(g_classes.descriptors(g_classIndexGenericAspect).sectionIndex, udfnGetCpgByPriceAssignment, ddlType, thisOrgIndex, thisPoolIndex)

       printSectionHeader(_
         "Function retrieving the OID of the CODEPROPERTYGROUP corresponding to CODEPRICEASSIGNMENT" & _
         IIf(lrtAware, " (LRT-aware)", ""), fileNo)

       Dim relIndexCpGroupHasProperty As Integer
       relIndexCpGroupHasProperty = getRelIndexByName(rxnCpGroupHasProperty, rnCpGroupHasProperty)

       If lrtAware Then
         qualObjNameCpGroupHasProperty = genQualViewNameByRelIndex(relIndexCpGroupHasProperty, ddlType, thisOrgIndex, thisPoolIndex, lrtAware, useMqtToImplementLrt)
       Else
         qualObjNameCpGroupHasProperty = genQualViewNameByRelIndex(relIndexCpGroupHasProperty, ddlType, thisOrgIndex, thisPoolIndex)
       End If

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE FUNCTION"
       Print #fileNo, addTab(1); qualFuncName

       Print #fileNo, addTab(0); "("
       genProcParm(fileNo, "", "oid_in", g_dbtOid, lrtAware, "OID of '" & g_classes.descriptors(g_classIndexGenericAspect).sectionName & "." & g_classes.descriptors(g_classIndexGenericAspect).sectionShortName & "'-object")
       If lrtAware Then
         genProcParm(fileNo, "", "lrtOid_in", g_dbtOid, False, "OID of the LRT used for reference")
       End If
       Print #fileNo, addTab(0); ")"
 
       Print #fileNo, addTab(0); "RETURNS"
       Print #fileNo, addTab(1); g_dbtOid
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "DETERMINISTIC"
       Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
       Print #fileNo, addTab(0); "READS SQL DATA"
       Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
       genProcSectionHeader(fileNo, "declare variables", , True)
       genVarDecl(fileNo, "v_cpgOid", g_dbtOid, "NULL")
 
       genProcSectionHeader(fileNo, "retrieve OID of CODEPROPERTYGROUP")
       Print #fileNo, addTab(1); "SET v_cpgOid = ("
 
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "CPG."; g_anOid
       Print #fileNo, addTab(2); "FROM"

       genTabSubQueryByEntityIndex(g_classIndexGenericAspect, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "GAS", g_anOid & ", PRPAPR_OID, BESESL_OID", 3)

       Print #fileNo, addTab(2); "INNER JOIN"
       genProcSectionHeader(fileNo, "1st Navigation: PriceAssignment -> (Numeric)Property -> CodePropertyGroup", 3, True)

       genTabSubQueryByEntityIndex(g_classIndexProperty, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "PRP", g_anOid & ", CLASSID", 3)

       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "PRP."; g_anOid; " = GAS.PRPAPR_OID"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "PRP."; g_anCid; " = '"; g_classes.descriptors(g_classIndexNumericProperty).classIdStr; "'"
       Print #fileNo, addTab(2); "INNER JOIN"

       genTabSubQueryByEntityIndex(g_relIndexCpGroupHasProperty, eactRelationship, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "CHP", "CPG_OID, PRP_OID", 3)

       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "CHP.PRP_OID = PRP."; g_anOid
       Print #fileNo, addTab(2); "INNER JOIN"

       genTabSubQueryByEntityIndex(g_classIndexCodePropertyGroup, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "CPG", g_anOid & ", CGCHCA_OID", 3)

       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "CPG."; g_anOid; " = CHP.CPG_OID"
       Print #fileNo, addTab(2); "INNER JOIN"
       genProcSectionHeader(fileNo, "2nd Navigation: CodePriceAssignment -> EndSlot -> Category -> CodePropertyGroup", 3, True)

       genTabSubQueryByEntityIndex(g_classIndexEndSlot, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "ESL", g_anOid & ", CLASSID, ESCESC_OID", 3)

       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "ESL."; g_anOid; " = GAS.BESESL_OID"
       Print #fileNo, addTab(4); "AND"
       genProcSectionHeader(fileNo, "filter criterion on ENDSLOT", 3, True)
       Print #fileNo, addTab(3); "ESL."; g_anCid; " = '"; g_classes.descriptors(g_classIndexMasterEndSlot).classIdStr; "'"
       Print #fileNo, addTab(2); "INNER JOIN"

       genTabSubQueryByEntityIndex(g_classIndexCategory, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "CAT", g_anOid, 3)

       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "ESL.ESCESC_OID = CAT."; g_anOid
       Print #fileNo, addTab(2); "WHERE"
       genProcSectionHeader(fileNo, "filter criterion on GENERICASPECT", 3, True)
       Print #fileNo, addTab(3); "GAS."; g_anOid; " = oid_in"
       Print #fileNo, addTab(4); "AND"
       genProcSectionHeader(fileNo, "intersect both navigation paths", 3, True)
       Print #fileNo, addTab(3); "CPG.CGCHCA_OID = CAT."; g_anOid
       Print #fileNo, addTab(2); "ORDER BY"
       Print #fileNo, addTab(3); "CPG."; g_anOid; " DESC"
       Print #fileNo, addTab(2); "FETCH FIRST 1 ROW ONLY"
       Print #fileNo, addTab(1); ");"
 
       Print #fileNo,
       Print #fileNo, addTab(1); "RETURN v_cpgOid;"
       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     Next k
 End Sub
 
 
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 Sub genAliasDdl( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   isCommonToOrgs As Boolean, isCommonToPools As Boolean, isAcmRelated As Boolean, _
   ByRef qualRefObjNameLdm As String, ByRef qualRefObjNamePdm As String, _
   Optional ByVal isCtoAliasCreated As Boolean = False, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal objOrgIndex As Integer = -1, _
   Optional ByVal objPoolIndex As Integer = -1, _
   Optional aliasType As DbAliasEntityType, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forLrtDeletedObjects = False, _
   Optional forPsDpFilter As Boolean = False, _
   Optional forPsDpFilterExtended As Boolean = False, _
   Optional ByRef comment As String = "", _
   Optional ByRef suffix As String = "", _
   Optional ByVal objSupportsLrt As Boolean = False, _
   Optional ByVal objIsPsTagged As Boolean = False, _
   Optional ByVal objSupportsPsDpFilter As Boolean = False, _
   Optional ByVal objIsArchive As Boolean = False, _
   Optional ByVal objSupportsLogChange As Boolean = False, _
   Optional ByVal suppressGenSuffix As Boolean = False, _
   Optional ByVal forRegularSchemaOnly As Boolean = False _
 )
 ' ### ELSE IVK ###
 'Sub genAliasDdl( _
 '  ByRef sectionIndex As Integer, _
 '  ByRef objName As String, _
 '  isCommonToOrgs As Boolean, _
 '  isCommonToPools As Boolean, _
 '  isAcmRelated As Boolean, _
 '  ByRef qualRefObjNameLdm As String, _
 '  ByRef qualRefObjNamePdm As String, _
 '  Optional ByVal isCtoAliasCreated As Boolean = False, _
 '  Optional ddlType As DdlTypeId = edtLdm, _
 '  Optional ByVal objOrgIndex As Integer = -1, _
 '  Optional ByVal objPoolIndex As Integer = -1, _
 '  Optional aliasType As DbAliasEntityType, _
 '  Optional forGen As Boolean = False, _
 '  Optional forLrt As Boolean = False, _
 '  Optional ByRef comment As String = "", _
 '  Optional byref suffix As String = "", _
 '  Optional ByVal objSupportsLrt As Boolean = False, _
 '  Optional ByVal objSupportsLogChange As Boolean = False, _
 '  Optional ByVal suppressGenSuffix As Boolean = False, _
 '  Optional ByVal forRegularSchemaOnly As Boolean = False _
 ')
 ' ### ENDIF IVK ###
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim mapViewToTab As Boolean
   Dim objMapsViewToTab As Boolean
   Dim skipAliasInNonRegularSchemas As Boolean

   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer
 ' ### IF IVK ###
   If objPoolIndex > 0 Then
       objIsArchive = (objIsArchive And g_pools.descriptors(objPoolIndex).isArchive)
   End If
 
   objMapsViewToTab = objIsPsTagged Or objSupportsLrt Or objSupportsLogChange Or objIsArchive
 ' ### ELSE IVK ###
 
 ' objMapsViewToTab = objSupportsLogChange
 ' ### ENDIF IVK ###
   mapViewToTab = objMapsViewToTab And aliasType = edatView
   skipAliasInNonRegularSchemas = (objMapsViewToTab And aliasType = edatTable) Or forRegularSchemaOnly

   Dim qualAliasNamePdm As String
   Const lrtAliasComment = "LRT-Alias-Schema"

   If isCommonToOrgs Then
     If qualRefObjNamePdm <> "VL6CMET.V_GROUP_NL_TEXT" And qualRefObjNamePdm <> "VL6CMET.V_AGGREGATIONNODE_NL_TEXT" And qualRefObjNamePdm <> "VL6CMET.V_ENDNODE_NL_TEXT" Then
     For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
       For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
         If g_pools.descriptors(thisPoolIndex).supportAcm Then
             If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And _
                IIf(objPoolIndex > 0, g_pools.descriptors(thisPoolIndex).commonItemsLocal, Not g_pools.descriptors(thisPoolIndex).commonItemsLocal) Then
               ' if we explicitly specified a pool then this pool implements common items locally
 ' ### IF IVK ###
               If (objIsPsTagged Or objIsArchive) And aliasType = edatTable Then
                 ' generate Alias in 'regular schema'
                 qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, , , , , suppressGenSuffix)
                 genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, _
                   thisOrgIndex, thisPoolIndex, "Data Pool Alias Schema")

                 ' we do not generate Aliases for PS-Tagged-Tables in LRT-alias schemas
                 ' instead we generate aliases for PS-Tagging-Views which 'look like' the corresponding tables
               Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
                 ' generate Alias in LRT-Alias-Schema
                 ' we do not need to care about the question whether 'objName' supports LRT since we are in the 'common-to-org-branch
 ' ### IF IVK ###
                 If Not forPsDpFilter And Not forPsDpFilterExtended And Not skipAliasInNonRegularSchemas Then
                   qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, _
                     IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, False, False, False, suppressGenSuffix)
 ' ### ELSE IVK ###
 '               If Not skipAliasInNonRegularSchemas Then
 '                 qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, _
 '                   IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, suppressGenSuffix)
 ' ### ENDIF IVK ###
                   genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, _
                     thisOrgIndex, thisPoolIndex, "LRT-Alias-Schema")
                 End If

 ' ### IF IVK ###
                 If g_pools.descriptors(thisPoolIndex).supportLrt Or supportAliasDelForNonLrtPools Then
                   ' generate Alias in LRT-Alias-Schema for deleted objects
                   ' we do not need to care about the question whether 'objName' supports LRT since we are in the 'common-to-org-branch
                   If Not objIsArchive And Not forPsDpFilter And Not forPsDpFilterExtended And Not skipAliasInNonRegularSchemas Then
                     qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, _
                       IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, True, False, False, suppressGenSuffix)
                     genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, _
                       thisOrgIndex, thisPoolIndex, _
                       "LRT-Alias-Schema for deleted objects")
                   End If
                 End If

                 If supportFilteringByPsDpMapping Then
                   ' generate Alias in Alias-Schema for PS-DP-Filtering if this is not 'for deleted objects'
                   If (forPsDpFilter Or Not objSupportsPsDpFilter) And Not skipAliasInNonRegularSchemas Then
                     qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, _
                       IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, False, True, , suppressGenSuffix)
                     genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, _
                       thisOrgIndex, thisPoolIndex, "Alias-Schema for filtering by PSDPMAPPING")
                   End If

                   If (forPsDpFilterExtended Or Not objSupportsPsDpFilter) And Not skipAliasInNonRegularSchemas Then
                     qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, _
                       IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, False, , True, suppressGenSuffix)
                     genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, _
                       thisOrgIndex, thisPoolIndex, "Alias-Schema for filtering by PSDPMAPPING")
                   End If
                 End If

 ' ### ENDIF IVK ###
 ' ### IF IVK ###
                 If Not (objIsPsTagged Or objIsArchive) Then
                   ' generate Alias in 'regular schema'
                   qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, _
                     aliasType, forGen, forLrt, suffix, , , , , suppressGenSuffix)
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 '                 ' generate Alias in 'regular schema'
 '                 qualAliasNamePdm = genQualAliasName(objName, ddlType, thisOrgIndex, thisPoolIndex, _
 '                   aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
 ' ### ENDIF IVK ###
                   genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, _
                     thisOrgIndex, thisPoolIndex, "Data Pool Alias Schema")
 ' ### IF IVK ###
                 End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
                 If Not isCtoAliasCreated And objPoolIndex <= 0 And Not forPsDpFilter And Not forPsDpFilterExtended Then
                   qualAliasNamePdm = genQualAliasName(objName, ddlType, , , aliasType, forGen, forLrt, suffix, , , , , suppressGenSuffix)
 ' ### ELSE IVK ###
 '               If Not isCtoAliasCreated And objPoolIndex <= 0 Then
 '                 qualAliasNamePdm = genQualAliasName(objName, ddlType, , , aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
 ' ### ENDIF IVK ###
                   genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt)
                   isCtoAliasCreated = True
                 End If
 ' ### IF IVK ###
               End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
             End If
         End If
       Next thisPoolIndex
     Next thisOrgIndex
     Else
                   qualAliasNamePdm = genQualAliasName(objName, ddlType, , , aliasType, forGen, forLrt, suffix, , , , , suppressGenSuffix)
                   genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt)
     End If
   ElseIf isCommonToPools Then
     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(thisPoolIndex).supportAcm Then
           If poolIsValidForOrg(thisPoolIndex, objOrgIndex) And _
              IIf(objPoolIndex > 0, g_pools.descriptors(thisPoolIndex).commonItemsLocal, Not g_pools.descriptors(thisPoolIndex).commonItemsLocal) Then
             ' if we explicitly specified a pool then this pool implements items locally
 ' ### IF IVK ###
             If (objIsPsTagged Or objSupportsLogChange Or objIsArchive) And (aliasType = edatTable) Then
               ' generate Alias in 'regular schema'
               qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, , , , , suppressGenSuffix)
 ' ### ELSE IVK ###
 '           If objSupportsLogChange And (aliasType = edatTable) Then
 '             ' generate Alias in 'regular schema'
 '             qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
 ' ### ENDIF IVK ###
               genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, _
                 "Data Pool Alias Schema")

               ' we do not generate Aliases for PS-Tagged-Tables in LRT-alias schemas
               ' instead we generate aliases for PS-Tagging-Views which 'look like' the corresponding tables
             Else

               ' generate Alias in LRT-Alias-Schema
               ' we do not need to care about the question whether 'objName' supports LRT since we are in the 'common-to-pools-branch
 ' ### IF IVK ###
               If Not forPsDpFilter And Not forPsDpFilterExtended And Not skipAliasInNonRegularSchemas Then
                 qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, _
                   IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, False, False, False, suppressGenSuffix)
 ' ### ELSE IVK ###
 '             If Not skipAliasInNonRegularSchemas Then
 '               qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, _
 '                 IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, suppressGenSuffix)
 ' ### ENDIF IVK ###
                 genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, _
                   "LRT-Alias-Schema")
               End If

 ' ### IF IVK ###
               If g_pools.descriptors(thisPoolIndex).supportLrt Or supportAliasDelForNonLrtPools Then
                 ' generate Alias in LRT-Alias-Schema for deleted objects
                 ' we do not need to care about the question whether 'objName' supports LRT since we are in the 'common-to-pools-branch

                 If (Not objIsArchive Or forLrtDeletedObjects) And Not skipAliasInNonRegularSchemas And Not forPsDpFilter And Not forPsDpFilterExtended Then
                   qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, _
                     IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, True, False, False, suppressGenSuffix)
                   genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, _
                     "LRT-Alias-Schema for deleted objects")
                 End If
               End If

               If supportFilteringByPsDpMapping Then
                 ' generate Alias in Alias-Schema for PS-DP-Filtering
                 If (forPsDpFilter Or Not objSupportsPsDpFilter) And Not supportAliasDelForNonLrtPools Then
                   qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, _
                     IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, False, True, , suppressGenSuffix)
                   genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, _
                     "Alias-Schema for filtering by PSDPMAPPING")
                 End If

                 If (forPsDpFilterExtended Or Not objSupportsPsDpFilter) And Not supportAliasDelForNonLrtPools Then
                   qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, _
                     IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, False, , True, suppressGenSuffix)
                   genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, _
                     "Alias-Schema for filtering by PSDPMAPPING")
                 End If
               End If

 ' ### ENDIF IVK ###
 ' ### IF IVK ###
               If Not (objIsPsTagged Or objIsArchive) Then
                 ' generate Alias in 'regular schema'
                 qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, , , , suppressGenSuffix)
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 '               ' generate Alias in 'regular schema'
 '               qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, thisPoolIndex, aliasType, forGen, forLrt, suffix, suppressGenSuffix)
 ' ### ENDIF IVK ###
                 genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, thisPoolIndex, _
                   "Data Pool Alias Schema")
 ' ### IF IVK ###
               End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
             End If
           End If
       End If
     Next thisPoolIndex
   Else
     If g_pools.descriptors(objPoolIndex).supportAcm Then
       If poolIsValidForOrg(objPoolIndex, objOrgIndex) Then
         ' generate Alias in LRT-Alias-Schema
 ' ### IF IVK ###
         If (objIsPsTagged Or objSupportsLrt Or objIsArchive) And aliasType = edatTable Then
           ' generate Alias in 'regular schema'
           qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, aliasType, forGen, forLrt, suffix, , , , , suppressGenSuffix)
 ' ### ELSE IVK ###
 '       If objSupportsLrt And aliasType = edatTable Then
 '         ' generate Alias in 'regular schema'
 '         qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
 ' ### ENDIF IVK ###
           genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, _
             "Data Pool Alias Schema")
 
           ' we do not generate Aliases for PS-Tagged-Tables in LRT-alias schemas
           ' instead we generate aliases for LRT-Views which 'look like' the corresponding tables
         Else
 ' ### IF IVK ###
           ' generate Alias in LRT-Alias-Schema if this is not 'for deleted objects' and not 'for Ps-Dp Filter'
           If Not forLrtDeletedObjects And Not forPsDpFilter And Not forPsDpFilterExtended And Not skipAliasInNonRegularSchemas Then
             qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, _
               IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, False, , , suppressGenSuffix)
 ' ### ELSE IVK ###
 '         ' generate Alias in LRT-Alias-Schema
 '         If Not skipAliasInNonRegularSchemas Then
 '           qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, suppressGenSuffix)
 ' ### ENDIF IVK ###
             genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, _
               "LRT-Alias-Schema")
           End If
 
 ' ### IF IVK ###
           If supportFilteringByPsDpMapping Then
             ' generate Alias in Alias-Schema for PS-DP-Filtering if this is not 'for deleted objects'
             If Not forLrtDeletedObjects And (forPsDpFilter Or Not objSupportsPsDpFilter) And Not skipAliasInNonRegularSchemas Then
               qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, _
                 IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, False, True, , suppressGenSuffix)
               genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, _
                 "Alias-Schema for filtering by PSDPMAPPING")
             End If

             If Not forLrtDeletedObjects And (forPsDpFilterExtended Or Not objSupportsPsDpFilter) And Not skipAliasInNonRegularSchemas Then
               qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, _
                 IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, False, , True, suppressGenSuffix)
               genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, _
                 "Alias-Schema for filtering by PSDPMAPPING")
             End If
           End If

           If g_pools.descriptors(objPoolIndex).supportLrt Or supportAliasDelForNonLrtPools Then
             ' generate Alias in LRT-Alias-Schema for deleted objects
             If (Not (objIsPsTagged Or objSupportsLrt Or objIsArchive) Or forLrtDeletedObjects) And Not skipAliasInNonRegularSchemas And Not forPsDpFilter And Not forPsDpFilterExtended Then
               qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, _
                 IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, True, , , suppressGenSuffix)
               genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, _
                 "LRT-Alias-Schema for deleted objects")
             End If
           End If

          ' add aliases for tables not CTO, not CTP, ACM-related and not user transaction, in work data pools, not for NL_TEXT
           If (objPoolIndex = g_workDataPoolIndex And Not objSupportsLrt And isAcmRelated And Not forPsDpFilter And Not forPsDpFilterExtended And Not skipAliasInNonRegularSchemas And Not InStr(1, UCase(objName), "NL_TEXT") > 0) Then
             qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, _
                 IIf(mapViewToTab, edatTable, aliasType), forGen, forLrt, suffix, True, True, , , suppressGenSuffix)
             genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, _
                 "Alias-Schema for deleted objects")
           End If

 ' ### ENDIF IVK ###
         End If

         ' generate Alias in 'regular schema'
 ' ### IF IVK ###
         If Not (objIsPsTagged Or objSupportsLrt Or objIsArchive) Then
           qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, aliasType, forGen, forLrt, suffix, , , , , suppressGenSuffix)
 ' ### ELSE IVK ###
 '       If Not objSupportsLrt Then
 '         qualAliasNamePdm = genQualAliasName(objName, ddlType, objOrgIndex, objPoolIndex, aliasType, forGen, forLrt, suffix, , suppressGenSuffix)
 ' ### ENDIF IVK ###
           genDbAlias(qualAliasNamePdm, qualRefObjNamePdm, qualRefObjNameLdm, objName, comment, sectionIndex, forLrt, objOrgIndex, objPoolIndex, _
             "Data Pool Alias Schema")
         End If
       End If
     End If
   End If
 
 NormalExit:
   closeAllDdlFiles(, , sectionIndex, processingStepAlias, g_phaseIndexAliases, ddlType)
   Exit Sub

 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub dropClassIdList( _
   Optional onlyIfEmpty As Boolean = False _
 )
   If Not generateEntityIdList Then
     Exit Sub
   End If

   killFile(genMetaFileName(g_targetDir, "ClassId"), onlyIfEmpty)
 End Sub
 
 
 Sub genClassIdList()
   If Not generateEntityIdList Then
     Exit Sub
   End If
 
   Dim fileName As String
   fileName = genMetaFileName(g_targetDir, "ClassId")
   assertDir(fileName)
   Dim fileNo As Integer
   fileNo = FreeFile()

   On Error GoTo ErrorExit
   Open fileName For Output As #fileNo

   Dim thisClassIndex As Integer
   Dim maxQualClassNameLen As Integer
   maxQualClassNameLen = 0

     For thisClassIndex = 1 To g_classes.numDescriptors Step 1
         If Not g_classes.descriptors(thisClassIndex).notAcmRelated And g_classes.descriptors(thisClassIndex).classId > 0 Then
           If Len(g_classes.descriptors(thisClassIndex).sectionName & "." & g_classes.descriptors(thisClassIndex).className) > maxQualClassNameLen Then
             maxQualClassNameLen = Len(g_classes.descriptors(thisClassIndex).sectionName & "." & g_classes.descriptors(thisClassIndex).className)
           End If
         End If
     Next thisClassIndex

     For thisClassIndex = 1 To g_classes.numDescriptors Step 1
         If Not g_classes.descriptors(thisClassIndex).notAcmRelated And g_classes.descriptors(thisClassIndex).classId > 0 Then
           Print #fileNo, paddRight(g_classes.descriptors(thisClassIndex).sectionName & "." & g_classes.descriptors(thisClassIndex).className, maxQualClassNameLen) & " : " & g_classes.descriptors(thisClassIndex).classIdStr
         End If
     Next thisClassIndex
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genClassDdl( _
   ByRef classIndex As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim thisOrgId As Integer
   Dim thisPoolId As Integer
   If thisOrgIndex > 0 Then thisOrgId = g_orgs.descriptors(thisOrgIndex).id Else thisOrgId = -1
   If thisPoolIndex > 0 Then thisPoolId = g_pools.descriptors(thisPoolIndex).id Else thisPoolId = -1

   If ddlType = edtPdm And Not poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
     Exit Sub
   End If

   Dim thisOrgDescriptorStr As String
   Dim noIndexesInPool As Integer
   Dim fileNo As Integer
   Dim fileNoCl As Integer
   Dim fileNoLrt As Integer
   Dim fileNoLrtView As Integer
   Dim fileNoLrtSup As Integer
   Dim fileNoLc As Integer
   Dim fileNoFk As Integer
 ' ### IF IVK ###
   Dim fileNoXmlF As Integer
   Dim fileNoXmlV As Integer
   Dim fileNoSetProd As Integer
   Dim fileNoSetProdCl As Integer
   Dim fileNoFto As Integer
   Dim fileNoGaSup As Integer
   Dim fileNoPs As Integer
   Dim fileNoPsCopy As Integer, fileNoPsCopy2 As Integer
   Dim fileNoExpCopy As Integer
   Dim fileNoArc As Integer
   Dim isGenericAspect As Boolean
   Dim isDivTagged As Boolean
   Dim thisPartitionIndex As Integer
   Dim lbClassIdStr As String
   Dim ubClassIdStr As String
   Dim supportPartitionByClassId As Boolean
   Dim tabPartitionType As PartitionType
 ' ### ENDIF IVK ###

   Dim poolSuppressUniqueConstraints As Boolean
   Dim poolSupportLrt As Boolean
   Dim poolCommonItemsLocal As Boolean
   Dim poolSupportAcm As Boolean
   Dim poolSuppressRefIntegrity As Boolean
   Dim poolSupportUpdates As Boolean
   Dim poolSupportXmlExport As Boolean
   If thisPoolIndex > 0 Then
       poolSuppressUniqueConstraints = g_pools.descriptors(thisPoolIndex).suppressUniqueConstraints
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
       poolCommonItemsLocal = g_pools.descriptors(thisPoolIndex).commonItemsLocal
       poolSupportAcm = g_pools.descriptors(thisPoolIndex).supportAcm
       poolSuppressRefIntegrity = g_pools.descriptors(thisPoolIndex).suppressRefIntegrity
       poolSupportUpdates = g_pools.descriptors(thisPoolIndex).supportUpdates
       poolSupportXmlExport = g_pools.descriptors(thisPoolIndex).supportXmlExport
   Else
     poolSupportLrt = True
     poolSupportAcm = True
     poolSupportUpdates = True
   End If
 
 ' ### IF IVK ###
   Dim orgSetProductiveTargetPoolIndex As Integer
   Dim orgIsPrimary As Boolean

   If thisOrgIndex < 1 Then
     orgSetProductiveTargetPoolIndex = -1
     orgIsPrimary = False
   Else
       orgSetProductiveTargetPoolIndex = g_orgs.descriptors(thisOrgIndex).setProductiveTargetPoolIndex
       orgIsPrimary = g_orgs.descriptors(thisOrgIndex).isPrimary
   End If

 ' ### ENDIF IVK ###
   thisOrgDescriptorStr = genOrgId(thisOrgIndex, ddlType)

   Dim ldmIteration As Integer
 ' ### IF IVK ###
     If g_classes.descriptors(classIndex).notPersisted Then
       Exit Sub
     End If

 ' ### ENDIF IVK ###
     If g_classes.descriptors(classIndex).sectionName & "" = "" Then
       GoTo NormalExit
     End If
 
     If ignoreUnknownSections And (g_classes.descriptors(classIndex).sectionIndex < 0) Then
       GoTo NormalExit
     End If
 
     If ddlType = edtPdm Then
       If Not sectionValidForPoolAndOrg(g_classes.descriptors(classIndex).sectionIndex, thisOrgIndex, thisPoolIndex) Then
         GoTo NormalExit
       End If
     End If

     If g_classes.descriptors(classIndex).isLrtSpecific And Not g_genLrtSupport Then
       GoTo NormalExit
     End If

     If g_classes.descriptors(classIndex).isPdmSpecific And ddlType <> edtPdm Then
       GoTo NormalExit
     End If

     If ddlType = edtPdm And g_classes.descriptors(classIndex).specificToOrgId >= 0 And g_classes.descriptors(classIndex).specificToOrgId <> thisOrgId Then
       GoTo NormalExit
     End If

 ' ### IF IVK ###
     If ddlType = edtPdm And g_classes.descriptors(classIndex).specificToPool >= 0 And g_classes.descriptors(classIndex).specificToPool <> thisPoolId And thisPoolId <> g_migDataPoolId Then
 ' ### ELSE IVK ###
 '   If ddlType = edtPdm And .specificToPool >= 0 And .specificToPool <> thisPoolId Then
 ' ### ENDIF IVK ###
       GoTo NormalExit
     End If

     If ddlType = edtPdm And thisPoolId <> -1 Then
       If Not g_classes.descriptors(classIndex).notAcmRelated And Not poolSupportAcm Then
         GoTo NormalExit
       End If
     End If
 
 ' ### IF IVK ###
     If ddlType = edtPdm And thisPoolIndex = g_archiveDataPoolIndex And Not supportArchivePool Then
       GoTo NormalExit
     End If

     If ddlType = edtPdm And thisPoolIndex = g_archiveDataPoolIndex And Not g_classes.descriptors(classIndex).isSubjectToArchiving And Not g_classes.descriptors(classIndex).notAcmRelated Then
       GoTo NormalExit
     End If

     isDivTagged = (g_classes.descriptors(classIndex).navPathToDiv.relRefIndex > 0) And Not (g_classes.descriptors(classIndex).classIndex = g_classIndexProductStructure)

 ' ### ENDIF IVK ###
     ldmIteration = IIf(g_classes.descriptors(classIndex).isCommonToOrgs, ldmIterationGlobal, ldmIterationPoolSpecific)
 ' ### IF IVK ###
     isGenericAspect = (UCase(g_classes.descriptors(classIndex).className) = "GENERICASPECT")
     supportPartitionByClassId = supportRangePartitioningByClassId And g_classes.descriptors(classIndex).subClassIdStrSeparatePartition.numMaps > 0
 ' ### ENDIF IVK ###

     fileNo = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseRegularTables, ldmIteration)
     fileNoFk = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseFksRelTabs, ldmIterationPoolSpecific)

 ' ### IF IVK ###
     If isGenericAspect Then
       fileNoGaSup = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepMiscMeta, ddlType, thisOrgIndex, thisPoolIndex, , phaseGaSyncSupport, ldmIteration)
     End If
 ' ### ENDIF IVK ###

     If generateLrt Then
       fileNoLrt = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, , phaseLrt, ldmIteration)

       fileNoLrtView = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, , phaseLrtViews, ldmIteration)

       fileNoCl = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, , phaseChangeLogViews, ldmIteration)

       fileNoLrtSup = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, , phaseLrtSupport, ldmIteration)
 ' ### IF IVK ###

       If orgSetProductiveTargetPoolIndex > 0 Then
         ' we need to place this DDL into the file corresponding to the 'higher pool id'! otherwise this results in errors during deployment
         fileNoSetProd = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepSetProd, ddlType, thisOrgIndex, orgSetProductiveTargetPoolIndex, , phaseUseCases, ldmIteration)

         fileNoSetProdCl = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepSetProd, ddlType, thisOrgIndex, orgSetProductiveTargetPoolIndex, , phaseChangeLogViews, ldmIteration)
       End If

       If Not orgIsPrimary And Not g_classes.descriptors(classIndex).noFto Then
         fileNoFto = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepFto, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIteration)
       End If
 ' ### ENDIF IVK ###
     End If

 ' ### IF IVK ###
     If generateXmlExportSupport Then
       fileNoXmlV = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseXmlExport, ldmIteration)

       If generateXsdInCtoSchema And ddlType = edtPdm And thisOrgIndex > 0 Then
         fileNoXmlF = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStep, ddlType, , , , phaseXmlExport, ldmIteration)
       Else
         fileNoXmlF = fileNoXmlV
       End If
     End If

     If generatePsTaggingView And g_classes.descriptors(classIndex).isPsTagged Then
       fileNoPs = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phasePsTagging, ldmIteration)
     End If

     If g_classes.descriptors(classIndex).logLastChange Then
       If (g_classes.descriptors(classIndex).logLastChangeAutoMaint) Or _
          (generateLogChangeView And Not g_classes.descriptors(classIndex).isUserTransactional And Not g_classes.descriptors(classIndex).isPsTagged And g_classes.descriptors(classIndex).logLastChangeInView) Then
         If fileNoPs > 0 Then
           fileNoLc = fileNoPs
         Else
           fileNoLc = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseLogChange, ldmIteration)
         End If
       End If
     End If

     If g_genLrtSupport And generatePsCopySupport And (g_classes.descriptors(classIndex).isPsForming Or g_classes.descriptors(classIndex).supportExtendedPsCopy) And g_classes.descriptors(classIndex).isUserTransactional Then
       fileNoPsCopy = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepPsCopy, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIteration)
       fileNoPsCopy2 = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepPsCopy2, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIteration)
     End If

     If g_genLrtSupport And generateExpCopySupport And g_classes.descriptors(classIndex).isSubjectToExpCopy Then
       fileNoExpCopy = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStepExpCopy, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIteration)
     End If

     If ddlType = edtPdm And supportArchivePool Then
       If thisPoolIndex = g_productiveDataPoolIndex Then
         fileNoArc = openDdlFile(g_targetDir, g_classes.descriptors(classIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, g_archiveDataPoolIndex, , phaseArchive, ldmIteration)
       End If
     End If
 
 ' ### ENDIF IVK ###
     'On Error GoTo ErrorExit

     If g_classes.descriptors(classIndex).superClass & "" <> "" Then
 ' ### IF IVK ###
       GoTo GenXmlExport
 ' ### ELSE IVK ###
 '     GoTo NormalExit
 ' ### ENDIF IVK ###
     End If

     noIndexesInPool = g_classes.descriptors(classIndex).noIndexesInPool

     Dim genSupportForLrt As Boolean
     genSupportForLrt = False
     If g_genLrtSupport And g_classes.descriptors(classIndex).isUserTransactional Then
       If thisPoolId > 0 Then
         genSupportForLrt = poolSupportLrt
       Else
         genSupportForLrt = (ddlType = edtLdm) And Not g_classes.descriptors(classIndex).isCommonToOrgs And Not g_classes.descriptors(classIndex).isCommonToPools
       End If
     End If

     ' (optionally) loop twice over the table structure: first run: 'Main' table + GEN-table; second run: corresponding LRT-tables
     Dim loopCount As Integer, iteration As Integer, forLrt As Boolean
     loopCount = IIf(genSupportForLrt, 2, 1)

     Dim tabColumns As EntityColumnDescriptors
     Dim qualTabName As String
     Dim qualTabNameLdm  As String
     Dim isAggregateHead As Boolean
     Dim transformation As AttributeListTransformation

     isAggregateHead = (g_classes.descriptors(classIndex).aggHeadClassIndex = g_classes.descriptors(classIndex).classIndex)
     For iteration = 1 To loopCount Step 1
       forLrt = (iteration = 2)

       initAttributeTransformation(transformation, 0)
       setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex)
       tabColumns = nullEntityColumnDescriptors

       qualTabName = genQualTabNameByClassIndex(g_classes.descriptors(classIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt)
       qualTabNameLdm = genQualTabNameByClassIndex(g_classes.descriptors(classIndex).classIndex, edtLdm, thisOrgIndex, thisPoolIndex, False, forLrt)

       addTabToDdlSummary(qualTabName, ddlType, g_classes.descriptors(classIndex).notAcmRelated)
       registerQualTable(qualTabNameLdm, qualTabName, g_classes.descriptors(classIndex).classIndex, g_classes.descriptors(classIndex).classIndex, eactClass, thisOrgIndex, thisPoolIndex, ddlType, g_classes.descriptors(classIndex).notAcmRelated, False, forLrt, False)

       If generateDdlCreateTable Then
         If g_classes.descriptors(classIndex).classId >= 0 And Not g_classes.descriptors(classIndex).notAcmRelated Then
           printChapterHeader("ACM-Class """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & """" & IIf(Not forLrt, "", " (LRT)"), fileNo)
         Else
           printChapterHeader("LDM-Table """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & """" & IIf(Not forLrt, "", " (LRT)"), fileNo)
         End If

         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE TABLE"
         Print #fileNo, addTab(1); qualTabName
         Print #fileNo, addTab(0); "("

 ' ### IF IVK ###
         If g_classes.descriptors(classIndex).isGenForming And g_classes.descriptors(classIndex).hasNoIdentity Then
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , True, True, forLrt, IIf(forLrt, edomDeclLrt, edomDeclNonLrt), , , , poolCommonItemsLocal, poolCommonItemsLocal)
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , False, False, forLrt, IIf(forLrt, edomDeclLrt, edomDeclNonLrt), , , , poolCommonItemsLocal, poolCommonItemsLocal)
         Else
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , , forLrt, IIf(forLrt, edomDeclLrt, edomDeclNonLrt), , , , poolCommonItemsLocal, poolCommonItemsLocal)
         End If
 ' ### ELSE IVK ###
 '       genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , , forLrt, IIf(forLrt, edomDeclLrt, edomDeclNonLrt), , , , poolcommonItemsLocal, poolcommonItemsLocal
 ' ### ENDIF IVK ###
         Print #fileNo, ")"

 ' ### IF IVK ###
         Dim fkAttrToDiv As String
         fkAttrToDiv = ""
         If g_classes.descriptors(classIndex).navPathToDiv.relRefIndex > 0 Then
             If g_classes.descriptors(classIndex).navPathToDiv.navDirection = etLeft Then
               fkAttrToDiv = g_relationships.descriptors(g_classes.descriptors(classIndex).navPathToDiv.relRefIndex).leftFkColName(ddlType)
             Else
               fkAttrToDiv = g_relationships.descriptors(g_classes.descriptors(classIndex).navPathToDiv.relRefIndex).rightFkColName(ddlType)
             End If
         End If

         genTabDeclTrailer(fileNo, ddlType, isDivTagged, eactClass, g_classes.descriptors(classIndex).classIndex, thisOrgIndex, thisPoolIndex, False, forLrt, False, supportPartitionByClassId, fkAttrToDiv, tabPartitionType)
 ' ### ELSE IVK ###
 '       genTabDeclTrailer fileNo, ddlType, eactClass, .classIndex, thisOrgIndex, thisPoolIndex, False, forLrt, False
 ' ### ENDIF IVK ###

         Print #fileNo, gc_sqlCmdDelim
       Else
 ' ### IF IVK ###
         If g_classes.descriptors(classIndex).isGenForming And g_classes.descriptors(classIndex).hasNoIdentity Then
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , True, True, forLrt, edomNone, , , , poolCommonItemsLocal, poolCommonItemsLocal)
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , False, False, forLrt, edomNone, , , , poolCommonItemsLocal, poolCommonItemsLocal)
         Else
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , , forLrt, edomNone, , , , poolCommonItemsLocal, poolCommonItemsLocal)
         End If
 ' ### ELSE IVK ###
 '       genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , , forLrt, edomNone, , , , poolcommonItemsLocal, poolcommonItemsLocal
 ' ### ENDIF IVK ###
       End If

       If (forLrt And lrtTablesVolatile) Or g_classes.descriptors(classIndex).isVolatile Then
         Print #fileNo,
         Print #fileNo, addTab(0); "ALTER TABLE "; qualTabName; " VOLATILE CARDINALITY"; gc_sqlCmdDelim
       End If
 
 ' ### IF IVK ###
       genPKForClass(qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, , forLrt, , poolSuppressUniqueConstraints, tabPartitionType)
 ' ### ELSE IVK ###
 '     genPKForClass qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, , forLrt, , poolsuppressUniqueConstraints
 ' ### ENDIF IVK ###

       If Not ((ddlType = edtPdm) And (noIndexesInPool >= 0) And (noIndexesInPool = thisPoolId)) Then
 ' ### IF IVK ###
         genIndexesForEntity(qualTabName, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, False, forLrt, False, False, poolSuppressUniqueConstraints, tabPartitionType)
 ' ### ELSE IVK ###
 '       genIndexesForEntity qualTabName, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, False, forLrt, False, False, poolsuppressUniqueConstraints
 ' ### ENDIF IVK ###
       End If

 ' ### IF IVK ###
       If Not forLrt And Not poolSuppressRefIntegrity Then
         genEnumFKsForClassRecursive(qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, False, False, 1, tabPartitionType)
         If g_classes.descriptors(classIndex).isGenForming And g_classes.descriptors(classIndex).hasNoIdentity Then
           genEnumFKsForClassRecursive(qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, True, False, 1, tabPartitionType)
         End If
         genFKsForPsTagOnClass(qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, forLrt, , , , tabPartitionType)
       End If

       If Not forLrt Then
         genFKsForRelationshipsByClassRecursive(qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, , , , tabPartitionType)
         If (classIndex = g_classIndexGenericAspect) Then
           genFKCheckSPForRelationshipByClassAndName(qualTabName, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType)
         End If
       End If
       If genSupportForLrt And Not poolSuppressRefIntegrity Then
         genFksForLrtByEntity(qualTabName, qualTabNameLdm, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, False, forLrt, , tabPartitionType)
       End If

       If transformation.containsNlAttribute Or (forLrt And ((isAggregateHead And g_classes.descriptors(classIndex).implicitelyGenChangeComment) Or g_classes.descriptors(classIndex).enforceLrtChangeComment)) Then
         genNlsTabsForClassRecursive(classIndex, classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoFk, ddlType, False, forLrt, poolCommonItemsLocal)
       End If
 ' ### ELSE IVK ###
 '     If Not forLrt And Not pool.suppressRefIntegrity Then
 '       genEnumFKsForClassRecursive qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, False, 1
 '     End If
 '
 '     If Not forLrt Then
 '       genFKsForRelationshipsByClassRecursive qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType
 '     End If
 '     If genSupportForLrt And Not pool.suppressRefIntegrity Then
 '       genFksForLrtByEntity qualTabName, qualTabNameLdm, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, False, forLrt
 '     End If
 '
 '     If transformation.containsNlAttribute Or (forLrt And isAggregateHead) Then
 '       genNlsTabsForClassRecursive classIndex, classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoFk, ddlType, False, forLrt, poolcommonItemsLocal
 '     End If
 ' ### ENDIF IVK ###

       If generateCommentOnTables And Not g_classes.descriptors(classIndex).notAcmRelated Then
         Print #fileNo,
         genDbObjComment("TABLE", qualTabName, "ACM-Class """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & """" & IIf(forLrt, " (LRT)", ""), fileNo, thisOrgIndex, thisPoolIndex)
       End If

       If generateCommentOnColumns And Not g_classes.descriptors(classIndex).notAcmRelated Then
         Print #fileNo,
         Print #fileNo, addTab(0); "COMMENT ON "; qualTabName; " ("
 ' ### IF IVK ###
         If g_classes.descriptors(classIndex).isGenForming And g_classes.descriptors(classIndex).hasNoIdentity Then
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , True, True, forLrt, edomComment)
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , False, False, forLrt, edomComment)
         Else
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , , forLrt, edomComment)
         End If
 ' ### ELSE IVK ###
 '       genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , , forLrt, edomComment
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       End If
 ' ### IF IVK ###

       If Not forLrt Then
         If g_classes.descriptors(classIndex).hasExpBasedVirtualAttrInNonGenInclSubClasses And (Not poolSupportLrt Or Not g_classes.descriptors(classIndex).useMqtToImplementLrt) And poolSupportUpdates Then
           ' create INSERT-trigger to maintain derived attributes (for LRT-MQT-supported classes this is done in MQT-triggers)
           genVirtualAttrTrigger(fileNoLrtSup, classIndex, qualTabName, thisOrgIndex, thisPoolIndex, ddlType, False)
         End If
       End If

       If (g_classes.descriptors(classIndex).hasExpBasedVirtualAttrInNonGenInclSubClasses Or g_classes.descriptors(classIndex).hasRelBasedVirtualAttrInNonGenInclSubClasses) And poolSupportUpdates Then
         genVirtAttrSupportForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoLrtSup, ddlType, , forLrt)
       End If
       If g_classes.descriptors(classIndex).hasGroupIdAttrInNonGenInclSubClasses And poolSupportUpdates Then
         genGroupIdSupportForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoLrtSup, ddlType, , forLrt)
       End If
 ' ### ENDIF IVK ###

       ' GEN-Tabs if class is Generation-Forming
 ' ### IF IVK ###
       If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
 ' ### ELSE IVK ###
 '     If .isGenForming Then
 ' ### ENDIF IVK ###
         tabColumns = nullEntityColumnDescriptors

         Dim qualTabNameGen As String, qualTabNameGenLdm As String
         qualTabNameGen = genQualTabNameByClassIndex(g_classes.descriptors(classIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, True, forLrt)
         'Defect 19643 wf
         'Folgender Aufruf wird erreicht für Tabelle VL6CPST011.PROPERTY_GEN_LRT, aber nicht für VL6CPST011.PROPERTY_GEN_LRT_MQT
         qualTabNameGenLdm = genQualTabNameByClassIndex(g_classes.descriptors(classIndex).classIndex, edtLdm, thisOrgIndex, thisPoolIndex, True, forLrt)

         addTabToDdlSummary(qualTabNameGen, ddlType, g_classes.descriptors(classIndex).notAcmRelated)
         registerQualTable(qualTabNameGenLdm, qualTabNameGen, g_classes.descriptors(classIndex).classIndex, g_classes.descriptors(classIndex).classIndex, eactClass, thisOrgIndex, thisPoolIndex, ddlType, g_classes.descriptors(classIndex).notAcmRelated, True, forLrt, False)

         If generateDdlCreateTable Then
           printChapterHeader("""GEN""-Table for ACM-Class """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & """" & IIf(Not forLrt, "", " (LRT)"), fileNo)
           Print #fileNo,
           Print #fileNo, "CREATE TABLE"
           Print #fileNo, addTab(1); qualTabNameGen
           Print #fileNo, "("

           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, _
             thisOrgIndex, thisPoolIndex, , True, , forLrt, IIf(forLrt, edomDeclLrt, edomDeclNonLrt), , , qualTabName, , poolCommonItemsLocal)
 
           Print #fileNo, ")"

 ' ### IF IVK ###
           genTabDeclTrailer(fileNo, ddlType, False, eactClass, g_classes.descriptors(classIndex).classIndex, thisOrgIndex, thisPoolIndex, False, forLrt, False, supportPartitionByClassId, , tabPartitionType)
 ' ### ELSE IVK ###
 '         genTabDeclTrailer fileNo, ddlType, eactClass, .classIndex, thisOrgIndex, thisPoolIndex, False, forLrt, False
 ' ### ENDIF IVK ###

           Print #fileNo, gc_sqlCmdDelim
         Else
           genTransformedAttrDeclsForClassRecursiveWithColReUse(classIndex, transformation, tabColumns, 1, fileNo, ddlType, _
             thisOrgIndex, thisPoolIndex, , True, , forLrt, edomNone, , , qualTabName, , poolCommonItemsLocal)
         End If

         If forLrt And lrtTablesVolatile Then
           Print #fileNo,
           Print #fileNo, addTab(0); "ALTER TABLE "; qualTabNameGen; " VOLATILE CARDINALITY"; gc_sqlCmdDelim
         End If

         If Not poolSuppressRefIntegrity Then
           genPKForGenClass(qualTabNameGen, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt)
         End If

 ' ### IF IVK ###
         If Not ((ddlType = edtPdm) And (g_classes.descriptors(classIndex).noIndexesInPool >= 0) And (g_classes.descriptors(classIndex).noIndexesInPool = thisPoolId)) Then
           genIndexesForEntity(qualTabNameGen, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, True, forLrt, , , _
             poolSuppressUniqueConstraints, tabPartitionType)
         End If
 
         If Not forLrt And Not poolSuppressRefIntegrity Then
           genEnumFKsForClassRecursive(qualTabNameGen, qualTabNameGenLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, True, False, 1, tabPartitionType)
           genFKsForGenParent(qualTabNameGen, qualTabNameGenLdm, qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, tabPartitionType)
         End If
         If genSupportForLrt And Not poolSuppressRefIntegrity Then
           genFksForLrtByEntity(qualTabNameGen, qualTabNameGenLdm, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, True, forLrt, , tabPartitionType)
         End If
 ' ### ELSE IVK ###
 '       If Not ((ddlType = edtPdm) And (.noIndexesInPool >= 0) And (.noIndexesInPool = thisPoolId)) Then
 '         genIndexesForEntity qualTabNameGen, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, True, forLrt, , , poolSuppressUniqueConstraints
 '       End If
 '
 '       If Not forLrt And Not pool.suppressRefIntegrity Then
 '         genEnumFKsForClassRecursive qualTabNameGen, qualTabNameGenLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, True, 1
 '         genFKsForGenParent qualTabNameGen, qualTabNameGenLdm, qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType
 '       End If
 '       If genSupportForLrt And Not pool.suppressRefIntegrity Then
 '         genFksForLrtByEntity qualTabNameGen, qualTabNameGenLdm, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, True, forLrt
 '       End If
 ' ### ENDIF IVK ###
 
 ' ### IF IVK ###
         If g_classes.descriptors(classIndex).hasExpBasedVirtualAttrInGenInclSubClasses And Not forLrt And (Not poolSupportLrt Or Not g_classes.descriptors(classIndex).useMqtToImplementLrt) And poolSupportUpdates Then
           ' create INSERT-trigger to maintain derived attributes (for LRT-MQT-supported classes this is done in MQT-triggers)
           genVirtualAttrTrigger(fileNoLrtSup, classIndex, qualTabNameGen, thisOrgIndex, thisPoolIndex, ddlType, True)
         End If
         If (g_classes.descriptors(classIndex).hasExpBasedVirtualAttrInGenInclSubClasses Or g_classes.descriptors(classIndex).hasRelBasedVirtualAttrInGenInclSubClasses) And poolSupportUpdates Then
           genVirtAttrSupportForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoLrtSup, ddlType, True, forLrt)
         End If

         If ddlType = edtPdm And Not g_classes.descriptors(classIndex).noAlias Then
           genAliasDdl(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).isCommonToOrgs, g_classes.descriptors(classIndex).isCommonToPools, Not g_classes.descriptors(classIndex).notAcmRelated, _
             qualTabNameGenLdm, qualTabNameGen, g_classes.descriptors(classIndex).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, True, forLrt, False, False, False, _
             IIf(g_classes.descriptors(classIndex).classId >= 0 And Not g_classes.descriptors(classIndex).notAcmRelated, "ACM-Class", "LDM-Table") & " """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & """ (GEN)", , _
             g_classes.descriptors(classIndex).isUserTransactional, g_classes.descriptors(classIndex).isPsTagged, , , g_classes.descriptors(classIndex).logLastChangeInView)
         End If

         If transformation.containsNlAttribute Or (forLrt And g_classes.descriptors(classIndex).implicitelyGenChangeComment) Then
           genNlsTabsForClassRecursive(classIndex, classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoFk, ddlType, True, forLrt, poolCommonItemsLocal)
         End If
 ' ### ELSE IVK ###
 '       If ddlType = edtPdm And Not .noAlias Then
 '         genAliasDdl.sectionName, .sectionShortName, .className, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
 '           qualTabNameGenLdm, qualTabNameGen, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, True, forLrt, _
 '           IIf(.classId >= 0 And Not .notAcmRelated, "ACM-Class", "LDM-Table") & " """ & .sectionName & "." & .className & """ (GEN)", , _
 '           .isUserTransactional, .logLastChangeInView
 '       End If
 '
 '       If transformation.containsNlAttribute Or forLrt Then
 '         genNlsTabsForClassRecursive classIndex, classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoFk, ddlType, True, forLrt, poolcommonItemsLocal
 '       End If
 ' ### ENDIF IVK ###

         If generateCommentOnTables And Not g_classes.descriptors(classIndex).notAcmRelated Then
           Print #fileNo,
           genDbObjComment("TABLE", qualTabNameGen, "ACM-Class """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & """ (GEN)" & IIf(forLrt, " (LRT)", ""), fileNo, thisOrgIndex, thisPoolIndex)
         End If

         If generateCommentOnColumns And Not g_classes.descriptors(classIndex).notAcmRelated Then
           Print #fileNo,
          Print #fileNo, addTab(0); "COMMENT ON "; qualTabNameGen; " ("

           genAttrDeclsForClassRecursiveWithColReUse(classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , True, , forLrt, _
             IIf(forLrt, edomDeclLrt, edomDeclNonLrt) Or edomComment, , qualTabName, , poolCommonItemsLocal)

           Print #fileNo, addTab(0); ")"
           Print #fileNo, addTab(0); gc_sqlCmdDelim
         End If
       End If

       If ddlType = edtPdm And Not g_classes.descriptors(classIndex).noAlias Then
 ' ### IF IVK ###
         genAliasDdl(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).isCommonToOrgs, g_classes.descriptors(classIndex).isCommonToPools, Not g_classes.descriptors(classIndex).notAcmRelated, _
           qualTabNameLdm, qualTabName, g_classes.descriptors(classIndex).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, forLrt, False, False, False, _
           IIf(g_classes.descriptors(classIndex).classId >= 0 And Not g_classes.descriptors(classIndex).notAcmRelated, "ACM-Class", "LDM-Table") & " """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & """", , _
           g_classes.descriptors(classIndex).isUserTransactional, g_classes.descriptors(classIndex).isPsTagged, , , g_classes.descriptors(classIndex).logLastChangeInView)
 ' ### ELSE IVK ###
 '       genAliasDdl .sectionName, .sectionShortName, .className, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
 '         qualTabNameLdm, qualTabName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, forLrt, _
 '         IIf(.classId >= 0 And Not .notAcmRelated, "ACM-Class", "LDM-Table") & " """ & .sectionName & "." & .className & """", , _
 '         .isUserTransactional, .logLastChangeInView
 ' ### ENDIF IVK ###
       End If
     Next iteration
 
     If g_genLrtSupport And g_classes.descriptors(classIndex).isUserTransactional And Not poolCommonItemsLocal Then
       genLrtSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoLrtView, fileNoCl, fileNoFk, fileNoLrtSup, ddlType)
 ' ### IF IVK ###
       If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
 ' ### ELSE IVK ###
 '     If .isGenForming Then
 ' ### ENDIF IVK ###
         genLrtSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoLrtView, fileNoCl, fileNoFk, fileNoLrtSup, ddlType, True)
       End If
     End If

 ' ### IF IVK ###
     If genSupportForLrt Then
       If generatePsCopySupport Then
         genPsCopySupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoPsCopy, fileNoPsCopy2, ddlType)
         If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
           genPsCopySupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoPsCopy, fileNoPsCopy2, ddlType, True)
         End If
       End If

       If generateExpCopySupport Then
         genExpCopySupportDdlForClass(g_classes.descriptors(classIndex).classIndex, thisOrgIndex, thisPoolIndex, fileNoExpCopy, ddlType)
         If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
           genExpCopySupportDdlForClass(g_classes.descriptors(classIndex).classIndex, thisOrgIndex, thisPoolIndex, fileNoExpCopy, ddlType, True)
         End If
       End If

       If orgSetProductiveTargetPoolIndex > 0 Then
         genSetProdSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, orgSetProductiveTargetPoolIndex, fileNoSetProd, fileNoSetProdCl, ddlType)
         If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
           genSetProdSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, orgSetProductiveTargetPoolIndex, fileNoSetProd, fileNoSetProdCl, ddlType, True)
         End If
       End If

       If Not orgIsPrimary And Not g_classes.descriptors(classIndex).noFto Then
         genFtoSupportDdlForClass(g_classes.descriptors(classIndex).classIndex, g_primaryOrgIndex, g_productiveDataPoolIndex, thisOrgIndex, thisPoolIndex, fileNoFto, ddlType)
         If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
           genFtoSupportDdlForClass(g_classes.descriptors(classIndex).classIndex, g_primaryOrgIndex, g_productiveDataPoolIndex, thisOrgIndex, thisPoolIndex, fileNoFto, ddlType, True)
         End If
       End If
     End If

     If generatePsTaggingView And g_classes.descriptors(classIndex).isPsTagged Then
       genPsTagSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoPs, ddlType)
       If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
         genPsTagSupportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoPs, ddlType, True)
       End If
     End If
 
     If generateLogChangeView And Not g_classes.descriptors(classIndex).isUserTransactional And Not g_classes.descriptors(classIndex).isPsTagged And g_classes.descriptors(classIndex).logLastChange And g_classes.descriptors(classIndex).logLastChangeInView Then
       genLogChangeSupportDdlForClass(g_classes.descriptors(classIndex).classIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType)
       If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
         genLogChangeSupportDdlForClass(g_classes.descriptors(classIndex).classIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, True)
       End If
     End If
 
 ' ### ENDIF IVK ###
     If g_classes.descriptors(classIndex).logLastChange And g_classes.descriptors(classIndex).logLastChangeAutoMaint Then
       genLogChangeAutoMaintSupportDdlForClass(g_classes.descriptors(classIndex).classIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, , forLrt)
 ' ### IF IVK ###
       If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
 ' ### ELSE IVK ###
 '     If .isGenForming Then
 ' ### ENDIF IVK ###
         genLogChangeAutoMaintSupportDdlForClass(g_classes.descriptors(classIndex).classIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, True, forLrt)
       End If
     End If
 ' ### IF IVK ###

     If ddlType = edtPdm And supportArchivePool Then
       If thisPoolIndex = g_productiveDataPoolIndex Then
         genArchiveSupportDdlForClass(classIndex, thisOrgIndex, g_archiveDataPoolIndex, fileNoArc, ddlType)
         If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
           genArchiveSupportDdlForClass(classIndex, thisOrgIndex, g_archiveDataPoolIndex, fileNoArc, ddlType, True)
         End If
       End If
     End If
 ' ### ENDIF IVK ###

     ' class may be a copy taken from g_glasses! make sure we update the original source!
     g_classes.descriptors(g_classes.descriptors(classIndex).classIndex).isLdmCsvExported = True
     g_classes.descriptors(g_classes.descriptors(classIndex).classIndex).isCtoAliasCreated = True
     g_classes.descriptors(classIndex).isLdmCsvExported = True ' safe is safe ;-)
     g_classes.descriptors(classIndex).isCtoAliasCreated = True ' safe is safe ;-)
     If genSupportForLrt Then
       g_classes.descriptors(g_classes.descriptors(classIndex).classIndex).isLdmLrtCsvExported = True
       g_classes.descriptors(classIndex).isLdmLrtCsvExported = True ' safe is safe ;-)
     End If
 ' ### IF IVK ###
 
 GenXmlExport:
     If generateXmlExportSupport And g_classes.descriptors(classIndex).supportXmlExport And (ddlType = edtLdm Or thisPoolId = -1 Or poolSupportXmlExport) Then
       genXmlExportDdlForClass(classIndex, thisOrgIndex, thisPoolIndex, fileNoXmlF, fileNoXmlV, ddlType)
     End If

     If isGenericAspect Then
       genGetCodePropertyGroupByPriceAssignmentFunction(fileNoGaSup, thisOrgIndex, thisPoolIndex, ddlType)
     End If
 ' ### ENDIF IVK ###
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Close #fileNoLrt
   Close #fileNoLrtView
   Close #fileNoCl
   Close #fileNoLrtSup
   Close #fileNoLc
   Close #fileNoFk
 ' ### IF IVK ###
   Close #fileNoSetProd
   Close #fileNoSetProdCl
   Close #fileNoFto
   Close #fileNoXmlV
   Close #fileNoPs
   Close #fileNoGaSup
   Close #fileNoPsCopy
   Close #fileNoPsCopy2
   Close #fileNoExpCopy
   Close #fileNoArc
   Close #fileNoXmlF
 ' ### ENDIF IVK ###
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Sub genTabDeclTrailer( _
   fileNo As Integer, _
   ddlType As DdlTypeId, _
   isDivTagged As Boolean, _
   acmEntityType As AcmAttrContainerType, _
   acmEntityIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   Optional forNl As Boolean = False, _
   Optional forLrt As Boolean, _
   Optional forMqt As Boolean, _
   Optional supportPartitionByClassId As Boolean = False, _
   Optional ByRef fkAttrToDiv As String, _
   Optional ByRef tabPartitionType As PartitionType = ptNone _
 )
   Dim partitionByClassId As Boolean
   partitionByClassId = supportPartitionByClassId And supportRangePartitioningByClassIdFirstPsOid

 ' ### ELSE IVK ###
 'Sub genTabDeclTrailer( _
 ' fileNo As Integer, _
 ' ddlType As DdlTypeId, _
 ' acmEntityType As AcmAttrContainerType, _
 ' acmEntityIndex As Integer, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' Optional forNl As Boolean = False, _
 ' Optional forLrt As Boolean, _
 ' Optional forMqt As Boolean _
 ')
 ' ### ENDIF IVK ###
   If ddlType <> edtPdm Then
     Exit Sub
   End If
 
   Dim thisPartitionIndex As Integer
   Dim lbClassIdVirtStr As String
   Dim lbClassIdStr As String
   Dim ubClassIdStr As String
   Dim tabSpaceData As String
   Dim tabSpaceLong As String
   Dim tabSpaceIndex As String
   Dim tabSpaceIndexData As Integer
   Dim tabSpaceIndexLong As Integer
   Dim tabSpaceIndexIndex As Integer
   Dim useValueCompression As Boolean
 ' ### IF IVK ###
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
   Dim noRangePartitioning As Boolean
 ' ### ENDIF IVK ###

   Dim poolSupportLrt As Boolean
   If thisPoolIndex > 0 Then
     poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
   End If

   If acmEntityType = eactClass Then
       tabSpaceIndex = g_classes.descriptors(acmEntityIndex).tabSpaceIndex
       tabSpaceIndexIndex = g_classes.descriptors(acmEntityIndex).tabSpaceIndexIndex
       tabSpaceLong = g_classes.descriptors(acmEntityIndex).tabSpaceLong
       tabSpaceIndexLong = g_classes.descriptors(acmEntityIndex).tabSpaceIndexLong
       If forNl Then
         tabSpaceData = g_classes.descriptors(acmEntityIndex).tabSpaceNl
         tabSpaceIndexData = g_classes.descriptors(acmEntityIndex).tabSpaceIndexNl
       Else
         tabSpaceData = g_classes.descriptors(acmEntityIndex).tabSpaceData
         tabSpaceIndexData = g_classes.descriptors(acmEntityIndex).tabSpaceIndexData
       End If
       useValueCompression = g_classes.descriptors(acmEntityIndex).useValueCompression
 ' ### IF IVK ###
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged And (usePsTagInNlTextTables Or Not forNl)
       psTagOptional = g_classes.descriptors(acmEntityIndex).psTagOptional
       noRangePartitioning = g_classes.descriptors(acmEntityIndex).noRangePartitioning
       If Not noRangePartitioning And g_classes.descriptors(acmEntityIndex).isUserTransactional And poolSupportLrt And Not g_classes.descriptors(acmEntityIndex).rangePartitioningAll Then
         If g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt Then
           If forLrt Then
             noRangePartitioning = Not (forMqt Or partitionLrtPrivateWhenMqt)
           Else
             noRangePartitioning = Not (forMqt Or partitionLrtPublicWhenMqt)
           End If
         Else
           If forLrt Then
             noRangePartitioning = Not partitionLrtPrivateWhenNoMqt
           Else
             noRangePartitioning = Not partitionLrtPublicWhenNoMqt
           End If
         End If
       End If
       If Not noRangePartitioning And noPartitioningInDataPools <> "" And thisPoolIndex > 0 And Not g_classes.descriptors(acmEntityIndex).rangePartitioningAll Then
         noRangePartitioning = includedInList(noPartitioningInDataPools, g_pools.descriptors(thisPoolIndex).id)
       End If
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       tabSpaceIndexIndex = g_relationships.descriptors(acmEntityIndex).tabSpaceIndexIndex
       tabSpaceIndex = g_relationships.descriptors(acmEntityIndex).tabSpaceIndex
       tabSpaceLong = g_relationships.descriptors(acmEntityIndex).tabSpaceLong
       tabSpaceIndexLong = g_relationships.descriptors(acmEntityIndex).tabSpaceIndexLong
       If forNl Then
         tabSpaceData = g_relationships.descriptors(acmEntityIndex).tabSpaceNl
         tabSpaceIndexData = g_relationships.descriptors(acmEntityIndex).tabSpaceIndexNl
       Else
         tabSpaceData = g_relationships.descriptors(acmEntityIndex).tabSpaceData
         tabSpaceIndexData = g_relationships.descriptors(acmEntityIndex).tabSpaceIndexData
       End If
       useValueCompression = g_relationships.descriptors(acmEntityIndex).useValueCompression
 ' ### IF IVK ###
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged And (usePsTagInNlTextTables Or Not forNl)
       psTagOptional = False
       noRangePartitioning = g_relationships.descriptors(acmEntityIndex).noRangePartitioning
       If Not noRangePartitioning And g_relationships.descriptors(acmEntityIndex).isUserTransactional And poolSupportLrt Then
         If g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt Then
           If forLrt Then
             noRangePartitioning = Not (forMqt Or partitionLrtPrivateWhenMqt)
           Else
             noRangePartitioning = Not (forMqt Or partitionLrtPublicWhenMqt)
           End If
         Else
           If forLrt Then
             noRangePartitioning = Not partitionLrtPrivateWhenNoMqt
           Else
             noRangePartitioning = Not partitionLrtPublicWhenNoMqt
           End If
         End If
       End If
       If Not noRangePartitioning And noPartitioningInDataPools <> "" And thisPoolIndex > 0 Then
         noRangePartitioning = includedInList(noPartitioningInDataPools, g_pools.descriptors(thisPoolIndex).id)
       End If
 ' ### ENDIF IVK ###
   Else
     Exit Sub
   End If

   If tabSpaceIndexData > 0 Then
     Print #fileNo, "IN " & genTablespaceNameByIndex(tabSpaceIndexData, thisOrgIndex, thisPoolIndex)
   End If
 ' ### IF IVK ###
 ' wf If-Bedingung deaktivuert --> Alle Tabellen mit Definition LONG-TS (wird aber nicht implemetiert für Bestand)
 ' wf WI19388
 '  If tabSpaceIndexLong > 0 And _
 '    (noRangePartitioning Or _
 '      (Not isPsTagged Or Not supportRangePartitioningByPsOid) And _
 '      (Not isDivTagged Or Not supportRangePartitioningByDivOid) _
 '    ) Then
 ' ### ELSE IVK ###
 ' If tabSpaceIndexLong > 0 Then
 ' ### ENDIF IVK ###
     Print #fileNo, "LONG IN " & genTablespaceNameByIndex(tabSpaceIndexLong, thisOrgIndex, thisPoolIndex)
 '  End If
   If tabSpaceIndexIndex > 0 Then
     Print #fileNo, "INDEX IN " & genTablespaceNameByIndex(tabSpaceIndexIndex, thisOrgIndex, thisPoolIndex)
   End If

   If ddlType = edtPdm And useValueCompression Then
     Print #fileNo, "VALUE COMPRESSION"
   End If
   Print #fileNo, "COMPRESS YES"

 ' ### IF IVK ###
   If forNl And Not usePsTagInNlTextTables Then
     Exit Sub
   End If

   If noRangePartitioning Then
     Exit Sub
   End If

   If isPsTagged And supportRangePartitioningByPsOid Then
     Dim thisPsOidForPartitioning As Long
     Dim elemsRangePartitionTablesByPsOid() As String
     elemsRangePartitionTablesByPsOid = split(listRangePartitionTablesByPsOid, ",")

     If supportPartitionByClassId And acmEntityType = eactClass Then
       Print #fileNo, addTab(0); "PARTITION BY RANGE ("; g_anPsOid; " NULLS FIRST, "; UCase(g_anCid); " NULLS FIRST) ("
       tabPartitionType = ptPsOidCid

       If partitionByClassId Then
         If psTagOptional Then
           Print #fileNo, addTab(1); "PARTITION "; genPartitionName(0); " STARTING (MINVALUE,MINVALUE) INCLUSIVE ENDING (0,MAXVALUE) INCLUSIVE,"
         End If
           Dim i As Integer
           For i = LBound(elemsRangePartitionTablesByPsOid) To UBound(elemsRangePartitionTablesByPsOid)
             thisPartitionIndex = 1
             While g_classes.descriptors(acmEntityIndex).subClassPartitionBoundaries(1, thisPartitionIndex) <> "" Or g_classes.descriptors(acmEntityIndex).subClassPartitionBoundaries(2, thisPartitionIndex) <> ""
               lbClassIdVirtStr = IIf(g_classes.descriptors(acmEntityIndex).subClassPartitionBoundaries(1, thisPartitionIndex) = "", getClassId(0, 0), g_classes.descriptors(acmEntityIndex).subClassPartitionBoundaries(1, thisPartitionIndex))
               lbClassIdStr = IIf(g_classes.descriptors(acmEntityIndex).subClassPartitionBoundaries(1, thisPartitionIndex) = "", "MINVALUE", "'" & g_classes.descriptors(acmEntityIndex).subClassPartitionBoundaries(1, thisPartitionIndex) & "'")
               ubClassIdStr = IIf(g_classes.descriptors(acmEntityIndex).subClassPartitionBoundaries(2, thisPartitionIndex) = "", "MAXVALUE", "'" & g_classes.descriptors(acmEntityIndex).subClassPartitionBoundaries(2, thisPartitionIndex) & "'")

               If thisPartitionIndex > 1 Or i > LBound(elemsRangePartitionTablesByPsOid) Then
                 Print #fileNo, ","
               End If
               thisPsOidForPartitioning = getLong(elemsRangePartitionTablesByPsOid(i), -1)
               Print #fileNo, addTab(1); "PARTITION "; _
                                         genPartitionName(thisPsOidForPartitioning, , lbClassIdVirtStr); " "; _
                                         "STARTING ("; CStr(thisPsOidForPartitioning); ", "; lbClassIdStr; ") INCLUSIVE "; _
                                         "ENDING ("; CStr(thisPsOidForPartitioning); ", "; ubClassIdStr; ") INCLUSIVE";
               thisPartitionIndex = thisPartitionIndex + 1
             Wend
           Next i
         Print #fileNo,
       Else
         For i = LBound(elemsRangePartitionTablesByPsOid) To UBound(elemsRangePartitionTablesByPsOid)
           thisPsOidForPartitioning = getLong(elemsRangePartitionTablesByPsOid(i), -1)
           Print #fileNo, addTab(1); "PARTITION "; genPartitionName(thisPsOidForPartitioning); " "; _
                                     "STARTING ("; CStr(thisPsOidForPartitioning); ", MINVALUE) INCLUSIVE "; _
                                     "ENDING ("; CStr(thisPsOidForPartitioning); ", MAXVALUE) INCLUSIVE"; _
                                     IIf(i < UBound(elemsRangePartitionTablesByPsOid), ",", "")
         Next i
       End If
     Else
       Print #fileNo, addTab(0); "PARTITION BY RANGE ("; g_anPsOid; " NULLS FIRST) ("
       tabPartitionType = ptPsOid

       If psTagOptional Then
         Print #fileNo, addTab(1); "PARTITION "; genPartitionName(0); " STARTING MINVALUE INCLUSIVE ENDING 0 INCLUSIVE,"
       End If
       For i = LBound(elemsRangePartitionTablesByPsOid) To UBound(elemsRangePartitionTablesByPsOid)
         thisPsOidForPartitioning = getLong(elemsRangePartitionTablesByPsOid(i), -1)
         Print #fileNo, addTab(1); "PARTITION "; genPartitionName(thisPsOidForPartitioning); " STARTING "; CStr(thisPsOidForPartitioning); _
                                   " INCLUSIVE ENDING "; CStr(thisPsOidForPartitioning); " INCLUSIVE"; _
                                   IIf(i < UBound(elemsRangePartitionTablesByPsOid), ",", "")
       Next i
     End If

     Print #fileNo, addTab(0); ")"
   ElseIf isDivTagged And supportRangePartitioningByDivOid Then
     Dim thisDivOidForPartitioning As Long
     Dim elemsRangePartitionTablesByDivOid() As String
     elemsRangePartitionTablesByDivOid = split(listRangePartitionTablesByDivOid, ",")

     Print #fileNo, addTab(0); "PARTITION BY RANGE ("; fkAttrToDiv; " NULLS FIRST) ("
     tabPartitionType = ptDivOid

     For i = LBound(elemsRangePartitionTablesByDivOid) To UBound(elemsRangePartitionTablesByDivOid)
       thisDivOidForPartitioning = getLong(elemsRangePartitionTablesByDivOid(i), -1)
       Print #fileNo, addTab(1); "PARTITION "; genPartitionName(thisDivOidForPartitioning, False); " STARTING "; CStr(thisDivOidForPartitioning); _
                                 " INCLUSIVE ENDING "; CStr(thisDivOidForPartitioning); " INCLUSIVE"; _
                                 IIf(i < UBound(elemsRangePartitionTablesByDivOid), ",", "")
     Next i

     Print #fileNo, addTab(0); ")"
   End If
 ' ### ENDIF IVK ###
 End Sub
 ' ### IF IVK ###
 
 
 Private Sub genVirtualAttrTrigger( _
   fileNo As Integer, _
   ByRef classIndex As Integer, _
   ByRef qualTabName As String, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False _
 )
   Dim entityTypeDescr As String
   entityTypeDescr = "ACM-Class" & IIf(forNl, " (NL-Text)", "")

   ' we currently only support insert trigger
     Dim hasVirtualAttrs As Boolean
     hasVirtualAttrs = Not forNl And ((forGen And g_classes.descriptors(classIndex).hasExpBasedVirtualAttrInGenInclSubClasses) Or (Not forGen And g_classes.descriptors(classIndex).hasExpBasedVirtualAttrInNonGenInclSubClasses))

     If Not hasVirtualAttrs Then
       Exit Sub
     End If

     Dim qualTriggerName As String
     Dim tabColumns As EntityColumnDescriptors
     Dim transformation As AttributeListTransformation

     ' ####################################################################################################################
     ' #    INSERT Trigger
     ' ####################################################################################################################

     qualTriggerName = genQualTriggerNameByClassIndex(g_classes.descriptors(classIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, Not forNl And forGen, , , , , IIf(forNl, "NLTXT", "") & "_INS")

     printSectionHeader(_
       "Insert-Trigger for maintaining virtual columns in table """ & qualTabName & _
       """ (" & entityTypeDescr & " """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & """)", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "AFTER INSERT ON"
     Print #fileNo, addTab(1); qualTabName
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 0)
     transformation.doCollectVirtualAttrDescriptors = True
     transformation.doCollectAttrDescriptors = True
     setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName)

     genTransformedAttrListForEntityWithColReuse(g_classes.descriptors(classIndex).classIndex, eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomNone)

     genProcSectionHeader(fileNo, "update virtual columns in table", , True)

     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); qualTabName; " T"
     Print #fileNo, addTab(1); "SET"

     Dim firstAttr As Boolean
     firstAttr = True
     Dim k As Integer
     For k = 1 To tabColumns.numDescriptors
         If tabColumns.descriptors(k).columnCategory And eacVirtual Then
           If Not firstAttr Then
             Print #fileNo, ","
           End If
           firstAttr = False
           Print #fileNo, addTab(2); "T."; tabColumns.descriptors(k).columnName; " = "; transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomValueVirtual);
         End If
     Next k

     Print #fileNo,
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "T."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
     Print #fileNo, addTab(1); ";"

     Print #fileNo, "END"
     Print #fileNo, gc_sqlCmdDelim
 End Sub
 ' ### ENDIF IVK ###
 
 
 Sub genClassesDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisClassIndex As Integer
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   resetClassesCsvExported()

   If ddlType = edtLdm Then
    For thisClassIndex = 1 To g_classes.numDescriptors Step 1
      genClassDdl(thisClassIndex, , , edtLdm)
    Next thisClassIndex

    resetClassesCsvExported()
   ElseIf ddlType = edtPdm Then
       For thisClassIndex = 1 To g_classes.numDescriptors Step 1
         thisOrgIndex = -1
         thisPoolIndex = -1
           If g_classes.descriptors(thisClassIndex).isCommonToOrgs Then
             genClassDdl(thisClassIndex, , , edtPdm)

             ' if there is some data pool which locally implements this class, take care of that
             For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
               If g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
                 For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
                   If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                     genClassDdl(thisClassIndex, thisOrgIndex, thisPoolIndex, edtPdm)
                   End If
                 Next thisOrgIndex
               End If
             Next thisPoolIndex

           Else
             For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
               If g_classes.descriptors(thisClassIndex).isCommonToPools Then
                 genClassDdl(thisClassIndex, thisOrgIndex, , edtPdm)
                 ' if there is some data pool which locally implements this class, take care of that
                 For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                   If g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
                     If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                       genClassDdl(thisClassIndex, thisOrgIndex, thisPoolIndex, edtPdm)
                     End If
                   End If
                 Next thisPoolIndex

               Else
                 For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                   If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                     genClassDdl(thisClassIndex, thisOrgIndex, thisPoolIndex, edtPdm)
                   End If
                 Next thisPoolIndex
               End If
             Next thisOrgIndex
           End If
       Next thisClassIndex

    resetClassesCsvExported()
   End If
 End Sub
 
 
 ' ### IF IVK ###
 Private Sub genClassHibernateSupport( _
   ByRef classIndex As Integer, _
   ddlType As DdlTypeId _
 )
   Dim fileNameHCfg As String
   Dim fileNoHCfg As Integer

   If Not genSupportForHibernate Then
     Exit Sub
   End If

   fileNameHCfg = genHCfgFileName(g_targetDir, classIndex, ddlType)
   assertDir(fileNameHCfg)
   fileNoHCfg = FreeFile()
   Open fileNameHCfg For Append As #fileNoHCfg

   On Error GoTo ErrorExit

     ' (optionally) loop twice over the table structure: first run: 'Main' table + GEN-table; second run: corresponding LRT-tables
     Dim loopCount As Integer, iteration As Integer, forLrt As Boolean
     loopCount = IIf(generateLrt, 2, 1)

     Dim tabColumns As EntityColumnDescriptors
     For iteration = 1 To loopCount Step 1
       forLrt = (iteration = 2)
     Next iteration

     genHCfgForClass(classIndex, fileNoHCfg, ddlType)
     If g_classes.descriptors(classIndex).isGenForming And Not g_classes.descriptors(classIndex).hasNoIdentity Then
       genHCfgForClass(classIndex, fileNoHCfg, ddlType, True)
     End If
 
 NormalExit:
   On Error Resume Next
   Close #fileNoHCfg
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genClassesHibernateSupport( _
   ddlType As DdlTypeId _
 )
   If Not genSupportForHibernate Or ddlType <> edtLdm Then
     Exit Sub
   End If

   Dim thisClassIndex As Integer

   For thisClassIndex = 1 To g_classes.numDescriptors Step 1
     genClassHibernateSupport(thisClassIndex, ddlType)
   Next thisClassIndex
 End Sub
 
 
 Sub dropClassesHibernateSupport( _
   ddlType As DdlTypeId _
 )
   If Not genSupportForHibernate Then
     Exit Sub
   End If

   Dim thisClassIndex As Integer

   For thisClassIndex = 1 To g_classes.numDescriptors Step 1
     killFile(genHCfgFileName(g_targetDir, thisClassIndex, ddlType))
   Next thisClassIndex
 End Sub
 
 
 ' ### ENDIF IVK ###
 Sub dropClassesCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   killCsvFileWhereEver(g_sectionIndexDbMeta, clnAcmSection, g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM")
   killCsvFileWhereEver(g_sectionIndexDbMeta, clnAcmEntity, g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM")
   killCsvFileWhereEver(g_sectionIndexDbMeta, clnLdmTable, g_targetDir, ldmCsvTableProcessingStep, onlyIfEmpty, "LDM")
   killCsvFileWhereEver(g_sectionIndexDbMeta, clnFkDependency, g_targetDir, ldmCsvFkProcessingStep, onlyIfEmpty, "LDM")

   killCsvFileWhereEver(g_sectionIndexDbMeta, clnPdmTable, g_targetDir, pdmCsvProcessingStep, onlyIfEmpty, "PDM")
 End Sub
 
 
 Sub genClassAcmMetaCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnAcmEntity, acmCsvProcessingStep, "ACM", ddlType)
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim typeKey As String
   typeKey = gc_acmEntityTypeKeyClass

   Dim i As Integer
   For i = 1 To g_classes.numDescriptors
     If Not g_classes.descriptors(i).notAcmRelated Then
         Print #fileNo, """"; UCase(g_classes.descriptors(i).sectionName); """,";
         Print #fileNo, """"; UCase(g_classes.descriptors(i).className); """,";
         Print #fileNo, """"; UCase(g_classes.descriptors(i).shortName); """,";
         Print #fileNo, """"; typeKey; """,";
         Print #fileNo, """"; g_classes.descriptors(i).classIdStr; """,";
         Print #fileNo, """"; g_classes.descriptors(i).i18nId; """,";
         Print #fileNo, IIf(g_classes.descriptors(i).isCommonToOrgs, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).isCommonToPools, gc_dbTrue, gc_dbFalse); ",";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).supportXmlExport, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).useXmlExport, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).aggHeadClassIdStr <> "", """" & g_classes.descriptors(i).aggHeadClassIdStr & """", ""); ",";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).noFto, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).isUserTransactional, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).isLrtMeta, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).isUserTransactional And g_classes.descriptors(i).useMqtToImplementLrt, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).useLrtCommitPreprocess, gc_dbTrue, gc_dbFalse); ",";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).lrtActivationStatusMode <> "", """" & g_classes.descriptors(i).lrtActivationStatusMode & """", ""); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).lrtClassification <> "", """" & g_classes.descriptors(i).lrtClassification & """", ""); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).isSubjectToArchiving, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).isGenForming And Not g_classes.descriptors(i).hasNoIdentity, gc_dbTrue, gc_dbFalse); ",";
 ' ### ELSE IVK ###
 '       Print #fileNo, IIf(.isGenForming, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).isPsTagged, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).isPsForming, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).logLastChange, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).isAbstract, gc_dbTrue, gc_dbFalse); ",";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).isSubjectToPreisDurchschuss, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).isUserTransactional And g_classes.descriptors(i).hasOrganizationSpecificReference, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).ignoreForChangelog, gc_dbTrue, gc_dbFalse); ",";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_classes.descriptors(i).condenseData, gc_dbTrue, gc_dbFalse) & ",";
         Print #fileNo, g_classes.descriptors(i).entityFilterEnumCriteria; ",";
         Print #fileNo, IIf(g_classes.descriptors(i).supportAhStatusPropagation, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).rangePartitioningAll, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, """"; UCase(g_classes.descriptors(g_classes.descriptors(i).orMappingSuperClassIndex).sectionName); """,";
         Print #fileNo, """"; UCase(g_classes.descriptors(g_classes.descriptors(i).orMappingSuperClassIndex).className); """,";
         Print #fileNo, """"; typeKey; """,";
         Print #fileNo, IIf(g_classes.descriptors(i).superClassSection <> "", """" & UCase(g_classes.descriptors(i).superClassSection) & """", ""); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).superClass <> "", """" & UCase(g_classes.descriptors(i).superClass) & """", ""); ",";
         Print #fileNo, IIf(g_classes.descriptors(i).superClass <> "", """" & typeKey & """", ""); ",";
         Print #fileNo, ",,0,";
         Print #fileNo, getCsvTrailer(12)
     End If
   Next i
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 ' ### IF IVK ###
 
 
 Sub dropClassesXmlExport()
   If Not generateXmlExportSupport Then
     Exit Sub
   End If

   Dim thisClassIndex As Integer

   If generateLdm Then
     For thisClassIndex = 1 To g_classes.numDescriptors Step 1
       killFile(genXmlExportFileName(g_targetDir, thisClassIndex, edtLdm))
       killFile(genXmlExportFileName(g_targetDir, thisClassIndex, edtLdm, True))
     Next thisClassIndex
   End If
 End Sub
 ' ### ENDIF IVK ###
 
 
 Sub evalClasses()
   Dim i As Integer, j As Integer
     For i = 1 To g_classes.numDescriptors Step 1
         ' determine TableSpaces
         g_classes.descriptors(i).tabSpaceIndexData = IIf(g_classes.descriptors(i).tabSpaceData <> "", getTableSpaceIndexByName(g_classes.descriptors(i).tabSpaceData), -1)
         g_classes.descriptors(i).tabSpaceIndexIndex = IIf(g_classes.descriptors(i).tabSpaceIndex <> "", getTableSpaceIndexByName(g_classes.descriptors(i).tabSpaceIndex), -1)
         g_classes.descriptors(i).tabSpaceIndexLong = IIf(g_classes.descriptors(i).tabSpaceLong <> "", getTableSpaceIndexByName(g_classes.descriptors(i).tabSpaceLong), -1)
         g_classes.descriptors(i).tabSpaceIndexNl = IIf(g_classes.descriptors(i).tabSpaceNl <> "", getTableSpaceIndexByName(g_classes.descriptors(i).tabSpaceNl), -1)

         If g_classes.descriptors(i).tabSpaceIndexData > 0 Then
           If g_tableSpaces.descriptors(g_classes.descriptors(i).tabSpaceIndexData).category = tscSms Then
             If g_classes.descriptors(i).tabSpaceIndexIndex > 0 And g_classes.descriptors(i).tabSpaceIndexIndex <> g_classes.descriptors(i).tabSpaceIndexData Then
               g_classes.descriptors(i).tabSpaceIndexIndex = g_classes.descriptors(i).tabSpaceIndexData
               logMsg("index table space """ & g_classes.descriptors(i).tabSpaceIndex & """ for class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """" & _
                 " must be identical to data table space since data table space is ""SMS"" - fixed", ellFixableWarning)
             End If
             If g_classes.descriptors(i).tabSpaceIndexLong > 0 And g_classes.descriptors(i).tabSpaceIndexLong <> g_classes.descriptors(i).tabSpaceIndexData Then
               g_classes.descriptors(i).tabSpaceIndexLong = g_classes.descriptors(i).tabSpaceIndexData
               logMsg("long table space """ & g_classes.descriptors(i).tabSpaceLong & """ for class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """" & _
                 " must be identical to data table space since data table space is ""SMS"" - fixed", ellFixableWarning)
             End If
           End If
         End If

         ' confirm that class name is unique
         For j = 1 To i - 1 Step 1
           If UCase(g_classes.descriptors(i).sectionName) = UCase(g_classes.descriptors(j).sectionName) And _
              UCase(g_classes.descriptors(i).className) = UCase(g_classes.descriptors(j).className) Then
             logMsg("class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """ is not unque", ellFatal)
           End If
         Next j

         ' initialize variables
         g_classes.descriptors(i).useValueCompression = g_classes.descriptors(i).useValueCompression And dbCompressValues
         g_classes.descriptors(i).numRelBasedFkAttrs = 0
         g_classes.descriptors(i).isAggHead = False
         g_classes.descriptors(i).hasLabel = False
         g_classes.descriptors(i).hasLabelInGen = False
 ' ### IF IVK ###
         g_classes.descriptors(i).hasAttrHasConflict = False
         g_classes.descriptors(i).hasIsNationalInclSubClasses = g_classes.descriptors(i).isNationalizable
         ' determine whether class supports XML-export
         If g_classes.descriptors(i).noXmlExport Or g_classes.descriptors(i).isAbstract Then
           g_classes.descriptors(i).supportXmlExport = False
         ElseIf g_classes.descriptors(i).isCommonToPools Or g_classes.descriptors(i).isCommonToOrgs Then
           g_classes.descriptors(i).supportXmlExport = True
         Else
           If g_classes.descriptors(i).specificToPool >= 0 Then
             Dim dataPoolIndex As Integer
             dataPoolIndex = getDataPoolIndexById(g_classes.descriptors(i).specificToPool)
             If dataPoolIndex > 0 Then
               If g_pools.descriptors(dataPoolIndex).supportXmlExport Then
                 g_classes.descriptors(i).supportXmlExport = True
               End If
             End If
           Else
             g_classes.descriptors(i).supportXmlExport = True
           End If
         End If
 ' ### ENDIF IVK ###

         ' determine reference to section
         g_classes.descriptors(i).sectionIndex = getSectionIndexByName(g_classes.descriptors(i).sectionName)
         g_classes.descriptors(i).sectionShortName = ""
         If g_classes.descriptors(i).sectionIndex > 0 Then
           g_classes.descriptors(i).sectionShortName = g_sections.descriptors(g_classes.descriptors(i).sectionIndex).shortName
         End If

         ' determine 'hasSubClasses'
         For j = 1 To g_classes.numDescriptors Step 1
           If g_classes.descriptors(i).sectionName = g_classes.descriptors(j).superClassSection And _
              g_classes.descriptors(i).className = g_classes.descriptors(j).superClass Then
             g_classes.descriptors(i).hasSubClass = True
             j = g_classes.numDescriptors ' just to exit this loop
           End If
         Next j
 NextI:
     Next i

     For i = 1 To g_classes.numDescriptors Step 1
 ' ### IF IVK ###
         If g_classes.descriptors(i).mapOidToClAttribute <> "" Then
           addAttrMapping(g_classes.descriptors(i).clMapAttrs, genSurrogateKeyName(edtPdm), g_classes.descriptors(i).mapOidToClAttribute)
         End If

         ' determine whether this is a PriceAssignment
         g_classes.descriptors(i).isPriceAssignment = InStr(1, UCase(g_classes.descriptors(i).className), "PRICEASSIGNMENT")
         g_classes.descriptors(i).hasPriceAssignmentSubClass = g_classes.descriptors(i).isPriceAssignment
         g_classes.descriptors(i).isSubjectToPreisDurchschuss = g_classes.descriptors(i).isPriceAssignment
 ' ### ENDIF IVK ###

         ' determine class ID as string
         g_classes.descriptors(i).classIdStr = getClassIdByClassIndex(i)
         ' determine class index
         g_classes.descriptors(i).classIndex = getClassIndexByName(g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className)
         ' determine class index of aggregate head
         g_classes.descriptors(i).aggHeadClassIndex = -1
         g_classes.descriptors(i).aggHeadClassIndexExact = -1
         g_classes.descriptors(i).aggHeadClassIdStr = ""
         If Not g_classes.descriptors(i).notAcmRelated Then
           If g_classes.descriptors(i).aggHeadSection <> "" And g_classes.descriptors(i).aggHeadName <> "" Then
             g_classes.descriptors(i).aggHeadClassIndex = getClassIndexByName(g_classes.descriptors(i).aggHeadSection, g_classes.descriptors(i).aggHeadName)
             g_classes.descriptors(i).aggHeadClassIndexExact = g_classes.descriptors(i).aggHeadClassIndex
             If g_classes.descriptors(i).aggHeadClassIndex <= 0 Then
               logMsg("unable to identify aggregate head class '" & g_classes.descriptors(i).aggHeadSection & "." & g_classes.descriptors(i).aggHeadName & "'", ellError)
             Else
                 g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).isAggHead = (g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).superClassSection = "")
               g_classes.descriptors(i).aggHeadClassIdStr = getClassIdByClassIndex(g_classes.descriptors(i).aggHeadClassIndex)
             End If
           End If
         End If
         ' determine superclass index
         '  !! we need to do this separately because 'getOrMappingSuperClassIndex' relies on all super class indexes being set!
         g_classes.descriptors(i).superClassIndex = getClassIndexByName(g_classes.descriptors(i).superClassSection, g_classes.descriptors(i).superClass)
 ' ### IF IVK ###

         ' verify that enforceLrtChangeComment is only set for userTransActional classes
         If g_classes.descriptors(i).enforceLrtChangeComment And Not g_classes.descriptors(i).isUserTransactional Then
           logMsg("class '" & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & "' enforces LRT-ChangeComment but is not user-transactional - fixed", ellFixableWarning)
           g_classes.descriptors(i).enforceLrtChangeComment = False
         End If
 ' ### ENDIF IVK ###
     Next i
 
     Dim k As Integer
 ' ### IF IVK ###
     ' propagate 'isSubjectToPreisDurchschuss' to parent classes
     For i = 1 To g_classes.numDescriptors Step 1
         If g_classes.descriptors(i).isSubjectToPreisDurchschuss Then
           k = g_classes.descriptors(i).superClassIndex
           While k > 0
             g_classes.descriptors(k).isSubjectToPreisDurchschuss = True
             k = g_classes.descriptors(k).superClassIndex
           Wend
         End If
     Next i
 
 ' ### ENDIF IVK ###
     For i = 1 To g_classes.numDescriptors Step 1
         ' determine index of class 'owning' the table implementing this class
         g_classes.descriptors(i).orMappingSuperClassIndex = getOrMappingSuperClassIndexByClassIndex(i)
         '
         ' determine all 'direct' subclasses
         g_classes.descriptors(i).subclassIndexes = getDirectSubclassIndexes(i)
         ' is this class implemented with an 'own table'?

         g_classes.descriptors(i).hasOwnTable = Not g_classes.descriptors(i).hasSubClass And (UBound(g_classes.descriptors(i).subclassIndexes) = 0) And g_classes.descriptors(i).superClass = ""
         ' determine attribute references

 ' ### IF IVK ###
         If g_classes.descriptors(i).supportExtendedPsCopy And Not g_classes.descriptors(i).isPsTagged Then
           logMsg("class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & "is tagged to 'support PSCOPY' but is not PS-tagged - fixed", ellFixableWarning)
           g_classes.descriptors(i).supportExtendedPsCopy = False
         End If

         If g_classes.descriptors(i).supportExtendedPsCopy And (g_classes.descriptors(i).isCommonToPools Or g_classes.descriptors(i).isCommonToOrgs) Then
           logMsg("class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & "is tagged to 'support PSCOPY' is but common " & _
             IIf(g_classes.descriptors(i).isCommonToOrgs, "organizations (cto)", "pools (ctp)"), ellFixableWarning)
           g_classes.descriptors(i).supportExtendedPsCopy = False
         End If
 ' ### ENDIF IVK ###

         If g_classes.descriptors(i).isUserTransactional And (g_classes.descriptors(i).isCommonToPools Or g_classes.descriptors(i).isCommonToOrgs) Then
           logMsg("class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & "has stereotype <lrt> but is common to " & _
             IIf(g_classes.descriptors(i).isCommonToOrgs, "organizations (cto)", "pools (ctp)") & " - fixed", ellFixableWarning)
           g_classes.descriptors(i).isUserTransactional = False
         End If

         If g_classes.descriptors(i).isUserTransactional And Not g_classes.descriptors(i).logLastChange Then
           logMsg("potential inconsistency with class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & _
             "class has stereotype <lrt> but does not have stereotype <logChange>", ellWarning)
         End If

         If g_classes.descriptors(i).isUserTransactional And g_classes.descriptors(i).logLastChange And Not g_classes.descriptors(i).logLastChangeInView Then
           logMsg("inconsistency with class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & _
             "class has stereotypes <logChange> and <lrt> but does not support 'logChangeInView' - fixed", ellFixableWarning)
           g_classes.descriptors(i).logLastChangeInView = True
         End If
 ' ### IF IVK ###

         If g_classes.descriptors(i).isPsTagged And g_classes.descriptors(i).logLastChange And Not g_classes.descriptors(i).logLastChangeInView Then
           logMsg("inconsistency with class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & _
             "class has stereotypes <logChange> and <ps> but does not support 'logChangeInView' - fixed", ellFixableWarning)
           g_classes.descriptors(i).logLastChangeInView = True
         End If

         If g_classes.descriptors(i).isPsForming And Not g_classes.descriptors(i).isUserTransactional Then
           logMsg("potential inconsistency with class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & _
             "class is 'PS-forming' but does not have stereotype <lrt>", ellInfo)
         End If

         If g_classes.descriptors(i).isSubjectToArchiving And Not g_classes.descriptors(i).logLastChange Then
           logMsg("potential inconsistency with class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & _
             "class is marked as ""subject to archiving"" but does not have stereotype <logChange>", ellWarning)
         End If

         If g_classes.descriptors(i).specificToOrgId >= 0 And Not g_classes.descriptors(i).noFto Then
           logMsg("class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & _
             "is specific to MPC " & g_classes.descriptors(i).specificToOrgId & " but does not have stereotype <nt2m> (no transfer to MPC) - fixed", ellFixableWarning)
           g_classes.descriptors(i).noFto = True
         ElseIf g_classes.descriptors(i).specificToPool >= 0 And Not g_classes.descriptors(i).noTransferToProduction Then
           logMsg("class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & "is specific to pool " & g_classes.descriptors(i).specificToPool & _
             " but does not have stereotype <nt2p> (no transfer to production) - fixed", ellFixableWarning)
           g_classes.descriptors(i).noTransferToProduction = True
         End If

         If g_classes.descriptors(i).isCommonToOrgs And Not g_classes.descriptors(i).noFto Then
           logMsg("class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & _
                  "is common to organizations (cto) but does not have stereotype <nt2m> (no transfer to MPC) - fixed", ellFixableWarning)
           g_classes.descriptors(i).noFto = True
         ElseIf g_classes.descriptors(i).isCommonToPools And Not g_classes.descriptors(i).noTransferToProduction Then
           logMsg("class """ & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & """: " & _
                  "is common to pools (ctp) but does not have stereotype <nt2p> (no transfer to production) - fixed", ellFixableWarning)
           g_classes.descriptors(i).noTransferToProduction = True
         End If
 ' ### ENDIF IVK ###

         ' determine references to attributes
           g_classes.descriptors(i).attrRefs.numDescriptors = 0
           g_classes.descriptors(i).attrRefsInclSubClasses.numDescriptors = 0
           g_classes.descriptors(i).attrRefsInclSubClassesWithRepeat.numDescriptors = 0
           g_classes.descriptors(i).numAttrsInGen = 0
           g_classes.descriptors(i).numAttrsInNonGen = 0
           g_classes.descriptors(i).numNlAttrsInGen = 0
           g_classes.descriptors(i).numNlAttrsInNonGen = 0
           g_classes.descriptors(i).hasNlAttrsInGenInclSubClasses = False
           g_classes.descriptors(i).hasNlAttrsInNonGenInclSubClasses = False
 ' ### IF IVK ###
           g_classes.descriptors(i).hasGroupIdAttrInNonGenInclSubClasses = g_classes.descriptors(i).hasGroupIdAttrInNonGen
           g_classes.descriptors(i).hasExpBasedVirtualAttrInGenInclSubClasses = g_classes.descriptors(i).hasExpBasedVirtualAttrInGen
           g_classes.descriptors(i).hasExpBasedVirtualAttrInNonGenInclSubClasses = g_classes.descriptors(i).hasExpBasedVirtualAttrInNonGen
           g_classes.descriptors(i).hasRelBasedVirtualAttrInGenInclSubClasses = g_classes.descriptors(i).hasRelBasedVirtualAttrInGen
           g_classes.descriptors(i).hasRelBasedVirtualAttrInNonGenInclSubClasses = g_classes.descriptors(i).hasRelBasedVirtualAttrInNonGen
           g_classes.descriptors(i).containsIsNotPublishedInclSubClasses = g_classes.descriptors(i).containsIsNotPublished
 ' ### ENDIF IVK ###
     Next i
 
     For i = 1 To g_classes.numDescriptors Step 1
         For j = 1 To g_attributes.numDescriptors Step 1
             If UCase(g_classes.descriptors(i).sectionName) = UCase(g_attributes.descriptors(j).sectionName) And _
                UCase(g_classes.descriptors(i).className) = UCase(g_attributes.descriptors(j).className) And _
                g_attributes.descriptors(j).cType = eactClass _
             Then
               g_attributes.descriptors(j).acmEntityIndex = i
               g_attributes.descriptors(j).isPdmSpecific = g_attributes.descriptors(j).isPdmSpecific Or g_classes.descriptors(i).isPdmSpecific
               If Not g_classes.descriptors(i).notAcmRelated Then
                 g_attributes.descriptors(j).isNotAcmRelated = False
               End If

 ' ### IF IVK ###
               If UCase(g_attributes.descriptors(j).attributeName) = UCase(conHasConflict) Then
                 g_classes.descriptors(i).hasAttrHasConflict = True
               End If

 ' ### ENDIF IVK ###
               If Not g_classes.descriptors(i).isGenForming And g_attributes.descriptors(j).isTimeVarying Then
                 logMsg("Attribute """ & g_attributes.descriptors(j).className & "." & g_attributes.descriptors(j).attributeName & " is marked as 'timeVarying' but class is not 'genForming' - fixed", _
                   ellFixableWarning)
                 g_attributes.descriptors(j).isTimeVarying = False
               End If
                 If g_attributes.descriptors(j).valueType = eavtEnum Then
                   g_classes.descriptors(i).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_classes.descriptors(i).attrRefs)).refType = eadrtEnum
 ' ### IF IVK ###
                 ElseIf isType(g_attributes.descriptors(j).domainSection, g_attributes.descriptors(j).domainName) Then
                   g_classes.descriptors(i).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_classes.descriptors(i).attrRefs)).refType = eadrtType
 ' ### ENDIF IVK ###
                 Else
                   g_classes.descriptors(i).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_classes.descriptors(i).attrRefs)).refType = eadrtAttribute
                 End If
                 g_classes.descriptors(i).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_classes.descriptors(i).attrRefs)).refIndex = j

               If g_attributes.descriptors(j).isNl Then
                   g_classes.descriptors(i).nlAttrRefs.descriptors(allocAttrDescriptorRefIndex(g_classes.descriptors(i).nlAttrRefs)) = g_classes.descriptors(i).attrRefs.descriptors(g_classes.descriptors(i).attrRefs.numDescriptors)
 ' ### IF IVK ###
                   If g_attributes.descriptors(j).isTimeVarying And Not g_classes.descriptors(i).hasNoIdentity Then
 ' ### ELSE IVK ###
 '                 If g_attributes.descriptors(j).isTimeVarying Then
 ' ### ENDIF IVK ###
                     g_classes.descriptors(i).numNlAttrsInGen = g_classes.descriptors(i).numNlAttrsInGen + 1
                   Else
                     g_classes.descriptors(i).numNlAttrsInNonGen = g_classes.descriptors(i).numNlAttrsInNonGen + 1
                   End If
               Else
 ' ### IF IVK ###
                   If g_attributes.descriptors(j).isTimeVarying And Not g_classes.descriptors(i).hasNoIdentity Then
 ' ### ELSE IVK ###
 '                 If g_attributes.descriptors(j).isTimeVarying Then
 ' ### ENDIF IVK ###
                     g_classes.descriptors(i).numAttrsInGen = g_classes.descriptors(i).numAttrsInGen + 1
                   Else
                     g_classes.descriptors(i).numAttrsInNonGen = g_classes.descriptors(i).numAttrsInNonGen + 1
                   End If
               End If

               If Not strArrayIsNull(g_attributes.descriptors(j).mapsToChangeLogAttributes) Then
                 For k = LBound(g_attributes.descriptors(j).mapsToChangeLogAttributes) To UBound(g_attributes.descriptors(j).mapsToChangeLogAttributes)
 ' ### IF IVK ###
                   addAttrMapping(g_classes.descriptors(i).clMapAttrs, g_attributes.descriptors(j).attributeName & IIf(g_attributes.descriptors(j).valueType = eavtEnum, gc_enumAttrNameSuffix, ""), g_attributes.descriptors(j).mapsToChangeLogAttributes(k), _
                     g_attributes.descriptors(j).isTimeVarying And Not g_classes.descriptors(i).hasNoIdentity, j)
 ' ### ELSE IVK ###
 '                 addAttrMapping g_classes.descriptors(i).clMapAttrs, .attributeName & IIf(.valueType = eavtEnum, gc_enumAttrNameSuffix, ""), .mapsToChangeLogAttributes(k), _
 '                   .isTimeVarying, j
 ' ### ENDIF IVK ###
                 Next k
               End If
             End If
         Next j

         ' determine references to indexes
         g_classes.descriptors(i).indexRefs.numRefs = 0
         For j = 1 To g_indexes.numDescriptors Step 1
             If UCase(g_classes.descriptors(i).sectionName) = UCase(g_indexes.descriptors(j).sectionName) And _
                UCase(g_classes.descriptors(i).className) = UCase(g_indexes.descriptors(j).className) Then
               g_classes.descriptors(i).indexRefs.refs(allocIndexDescriptorRefIndex(g_classes.descriptors(i).indexRefs)) = j
             End If
         Next j

 ' ### IF IVK ###
         ' determine references to relationships
         g_classes.descriptors(i).allowedCountriesRelIndex = -1
         g_classes.descriptors(i).disAllowedCountriesRelIndex = -1
         g_classes.descriptors(i).allowedCountriesListRelIndex = -1
         g_classes.descriptors(i).disAllowedCountriesListRelIndex = -1

 ' ### ENDIF IVK ###
         Dim invertDirection As Boolean
         g_classes.descriptors(i).relRefs.numRefs = 0
         For j = 1 To g_relationships.numDescriptors Step 1
             If UCase(g_classes.descriptors(i).sectionName) = UCase(g_relationships.descriptors(j).leftClassSectionName) And _
                UCase(g_classes.descriptors(i).className) = UCase(g_relationships.descriptors(j).leftClassName) Then
 ' ### IF IVK ###
               ' prefer directed relationship if we have the choice between both directions
               If g_classes.descriptors(getClassIndexByName(g_relationships.descriptors(j).leftClassSectionName, g_relationships.descriptors(j).leftClassName)).orMappingSuperClassIndex = _
                 g_classes.descriptors(getClassIndexByName(g_relationships.descriptors(j).rightClassSectionName, g_relationships.descriptors(j).rightClassName)).orMappingSuperClassIndex And _
                 g_relationships.descriptors(j).maxLeftCardinality = 1 And g_relationships.descriptors(j).maxRightCardinality <> 1 Then
                 ' restrict this to individual relationship: hack to avoid re-ordering of columns in tables (MIG-team would complain)
                 invertDirection = g_relationships.descriptors(j).relName = "ExtendsSr0Validity"
               Else
                 invertDirection = False
               End If
               ' Fixme: get rid of hard coded relationship names
               If InStr(UCase(g_relationships.descriptors(j).relName), "DISALLOWEDCOUNTRIESLIST") Then
                 g_classes.descriptors(i).disAllowedCountriesListRelIndex = j
               ElseIf InStr(UCase(g_relationships.descriptors(j).relName), "DISALLOWEDCOUNTRIES") Then
                 g_classes.descriptors(i).disAllowedCountriesRelIndex = j
               ElseIf InStr(UCase(g_relationships.descriptors(j).relName), "ALLOWEDCOUNTRIESLIST") Then
                 g_classes.descriptors(i).allowedCountriesListRelIndex = j
               ElseIf InStr(UCase(g_relationships.descriptors(j).relName), "ALLOWEDCOUNTRIES") Then
                 g_classes.descriptors(i).allowedCountriesRelIndex = j
               End If

 ' ### ELSE IVK ###
 '             invertDirection = False
 ' ### ENDIF IVK ###

                 g_classes.descriptors(i).relRefs.refs(allocRelDescriptorRefIndex(g_classes.descriptors(i).relRefs)).refIndex = j
                 g_classes.descriptors(i).relRefs.refs(allocRelDescriptorRefIndex(g_classes.descriptors(i).relRefs)).refType = IIf(invertDirection, etRight, etLeft)
             ElseIf UCase(g_classes.descriptors(i).sectionName) = UCase(g_relationships.descriptors(j).rightClassSectionName) And _
               UCase(g_classes.descriptors(i).className) = UCase(g_relationships.descriptors(j).rightClassName) Then
               invertDirection = False

                 g_classes.descriptors(i).relRefs.refs(allocRelDescriptorRefIndex(g_classes.descriptors(i).relRefs)).refIndex = j
                 g_classes.descriptors(i).relRefs.refs(allocRelDescriptorRefIndex(g_classes.descriptors(i).relRefs)).refType = IIf(invertDirection, etLeft, etRight)
             End If
         Next j
     Next i

     For i = 1 To g_classes.numDescriptors Step 1
         ' verify consistency of aggregate heads with object relational mapping
         If g_classes.descriptors(i).aggHeadClassIndex > 0 Then
           If g_classes.descriptors(i).aggHeadClassIndex <> g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex Then
             logMsg("potential inconsistency: aggregate head of class '" & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & "' is not identical to its 'OR-mapping parent class' " & _
               "'" & g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).sectionName & "." & _
               g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).className & "'", ellInfo)
             g_classes.descriptors(i).aggHeadClassIndex = g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex
             g_classes.descriptors(i).aggHeadSection = g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).sectionName
             g_classes.descriptors(i).aggHeadName = g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).className
             g_classes.descriptors(i).aggHeadClassIdStr = g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).classIdStr
             g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).isAggHead = True
           End If
         End If
     Next i

     For i = 1 To g_classes.numDescriptors Step 1
         ' determine all subclasses (recurse down)
         ' Important: can only be done after direct subclasses of all classes are determined
         g_classes.descriptors(i).subclassIndexesRecursive = getSubclassIndexesRecursive(i)
         g_classes.descriptors(i).subclassIdStrListNonAbstract = getSubClassIdStrListByClassIndex(g_classes.descriptors(i).classIndex)
         g_classes.descriptors(i).attrRefsInclSubClasses = g_classes.descriptors(i).attrRefs
         g_classes.descriptors(i).attrRefsInclSubClassesWithRepeat = g_classes.descriptors(i).attrRefs
         g_classes.descriptors(i).nlAttrRefsInclSubclasses = g_classes.descriptors(i).nlAttrRefs
         g_classes.descriptors(i).hasNlAttrsInGenInclSubClasses = (g_classes.descriptors(i).numNlAttrsInGen > 0)
         g_classes.descriptors(i).hasNlAttrsInNonGenInclSubClasses = (g_classes.descriptors(i).numNlAttrsInNonGen > 0)
         g_classes.descriptors(i).implicitelyGenChangeComment = g_classes.descriptors(i).sectionShortName = "PST" And Not g_classes.descriptors(i).condenseData
         g_classes.descriptors(i).clMapAttrsInclSubclasses = g_classes.descriptors(i).clMapAttrs
 ' ### IF IVK ###
         g_classes.descriptors(i).groupIdAttrIndexesInclSubclasses = g_classes.descriptors(i).groupIdAttrIndexes
 ' ### ENDIF IVK ###

         addAggChildClassIndex(g_classes.descriptors(i).aggHeadClassIndex, g_classes.descriptors(i).classIndex)

         For j = 1 To UBound(g_classes.descriptors(i).subclassIndexesRecursive) Step 1
           g_classes.descriptors(i).hasNlAttrsInGenInclSubClasses = g_classes.descriptors(i).hasNlAttrsInGenInclSubClasses Or (g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).numNlAttrsInGen > 0)
           g_classes.descriptors(i).hasNlAttrsInNonGenInclSubClasses = g_classes.descriptors(i).hasNlAttrsInNonGenInclSubClasses Or (g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).numNlAttrsInNonGen > 0)
 ' ### IF IVK ###
           g_classes.descriptors(i).hasExpBasedVirtualAttrInGenInclSubClasses = g_classes.descriptors(i).hasExpBasedVirtualAttrInGenInclSubClasses Or g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).hasExpBasedVirtualAttrInGen
           g_classes.descriptors(i).hasExpBasedVirtualAttrInNonGenInclSubClasses = g_classes.descriptors(i).hasExpBasedVirtualAttrInNonGenInclSubClasses Or g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).hasExpBasedVirtualAttrInNonGen
           g_classes.descriptors(i).hasRelBasedVirtualAttrInGenInclSubClasses = g_classes.descriptors(i).hasRelBasedVirtualAttrInGenInclSubClasses Or g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).hasRelBasedVirtualAttrInGen
           g_classes.descriptors(i).hasRelBasedVirtualAttrInNonGenInclSubClasses = g_classes.descriptors(i).hasRelBasedVirtualAttrInNonGenInclSubClasses Or g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).hasRelBasedVirtualAttrInNonGen
           g_classes.descriptors(i).hasGroupIdAttrInNonGenInclSubClasses = g_classes.descriptors(i).hasGroupIdAttrInNonGenInclSubClasses Or g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).hasGroupIdAttrInNonGen
           g_classes.descriptors(i).containsIsNotPublishedInclSubClasses = g_classes.descriptors(i).containsIsNotPublishedInclSubClasses Or g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).containsIsNotPublished

           g_classes.descriptors(i).hasPriceAssignmentSubClass = g_classes.descriptors(i).hasPriceAssignmentSubClass Or g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).isPriceAssignment
           If g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).isPriceAssignment And Not g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).isAbstract Then
             g_classes.descriptors(i).subclassIdStrListNonAbstractPriceAssignment = IIf(g_classes.descriptors(i).subclassIdStrListNonAbstractPriceAssignment = "", "", g_classes.descriptors(i).subclassIdStrListNonAbstractPriceAssignment & ",") & _
               "'" & g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).classIdStr & "'"
           End If

           ' check if some subclass is PS-tagged while this class is not
           If g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).isPsTagged Then
             If Not g_classes.descriptors(i).isPsTagged Then
               g_classes.descriptors(i).isPsTagged = True
               g_classes.descriptors(i).psTagOptional = True
             End If
           End If
 ' ### ENDIF IVK ###

           For k = 1 To g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).attrRefs.numDescriptors
             addAttrDescriptorRef(g_classes.descriptors(i).attrRefsInclSubClasses, g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).attrRefs.descriptors(k).refIndex)
             addAttrDescriptorRef(g_classes.descriptors(i).attrRefsInclSubClassesWithRepeat, g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).attrRefs.descriptors(k).refIndex, True)
           Next k
           For k = 1 To g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).nlAttrRefs.numDescriptors
             addAttrDescriptorRef(g_classes.descriptors(i).nlAttrRefsInclSubclasses, g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).nlAttrRefs.descriptors(k).refIndex)
           Next k

 ' ### IF IVK ###
             For k = 1 To UBound(g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).groupIdAttrIndexes)
               addGroupIdAttrIndexInclSubClasses(i, g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).groupIdAttrIndexes(k))
             Next k

 ' ### ENDIF IVK ###
             If Not arrayIsNull(g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).clMapAttrs) Then
               For k = LBound(g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).clMapAttrs) To UBound(g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).clMapAttrs)
                 addAttrMapping(g_classes.descriptors(i).clMapAttrsInclSubclasses, g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).clMapAttrs(k).mapFrom, g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).clMapAttrs(k).mapTo, g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).clMapAttrs(k).isTv, g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).clMapAttrs(k).attrIndex)
               Next k
             End If
 ' ### IF IVK ###

           ' propagate '(dis)allowedCountries-relationships'
           If g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).allowedCountriesRelIndex > 0 Then
             g_classes.descriptors(i).allowedCountriesRelIndex = g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).allowedCountriesRelIndex
           End If
           If g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).disAllowedCountriesRelIndex > 0 Then
             g_classes.descriptors(i).disAllowedCountriesRelIndex = g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).disAllowedCountriesRelIndex
           End If
           If g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).allowedCountriesListRelIndex > 0 Then
             g_classes.descriptors(i).allowedCountriesListRelIndex = g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).allowedCountriesListRelIndex
           End If
           If g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).disAllowedCountriesListRelIndex > 0 Then
             g_classes.descriptors(i).disAllowedCountriesListRelIndex = g_classes.descriptors(g_classes.descriptors(i).subclassIndexesRecursive(j)).disAllowedCountriesListRelIndex
           End If
 ' ### ENDIF IVK ###
         Next j
 ' ### IF IVK ###

         ' determine whether aggregate head is price assignment
         If g_classes.descriptors(i).aggHeadClassIndexExact > 0 Then
           g_classes.descriptors(i).hasPriceAssignmentAggHead = g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndexExact).hasPriceAssignmentSubClass And Not g_classes.descriptors(i).isAggHead
         ElseIf g_classes.descriptors(i).aggHeadClassIndex > 0 Then
           g_classes.descriptors(i).hasPriceAssignmentAggHead = g_classes.descriptors(g_classes.descriptors(i).aggHeadClassIndex).hasPriceAssignmentSubClass
         End If
 ' ### ENDIF IVK ###
         '
         ' determine all references to relationships (recurse down)
         ' Important: can only be done after relrefs of all classes are determined
         g_classes.descriptors(i).relRefsRecursive = getRelRefsRecursive(i)
     Next i
 
 ' ### IF IVK ###
   For i = 1 To g_classes.numDescriptors
       If g_classes.descriptors(i).rangePartitionGroup <> "" Then
         addStrListMapEntry(g_classes.descriptors(i).subClassIdStrSeparatePartition, g_classes.descriptors(i).rangePartitionGroup, g_classes.descriptors(i).subclassIdStrListNonAbstract)
       End If
   Next i
 
   For i = 1 To g_classes.numDescriptors
       getSubClassIdStrListPartitionGroupMap(g_classes.descriptors(i).classIndex)
       g_classes.descriptors(i).useLrtCommitPreprocess = _
         (UCase(g_classes.descriptors(i).className) = UCase(clnGenericCode)) Or _
         (UCase(g_classes.descriptors(i).className) = UCase(clnTypeSpec)) Or _
         g_classes.descriptors(i).hasRelBasedVirtualAttrInGenInclSubClasses Or _
         g_classes.descriptors(i).hasRelBasedVirtualAttrInNonGenInclSubClasses
       g_classes.descriptors(i).isSubjectToExpCopy = g_classes.descriptors(i).isUserTransactional And UCase(g_classes.descriptors(i).aggHeadName) = UCase(clnExpression)
   Next i

   ' determine boundaries of partition-ranges
   If supportRangePartitioningByClassId Then
     Dim subClassIdStrings() As String
     Dim minClassIdStr As String
     Dim lastMinClassIdStr As String
     Dim lowerBoundClassIdStr As String
     Dim foundNewMinClassId As Boolean
     Dim matchingRangeIndexes As String
     Dim lastMatchingRangeIndexes As String
     Dim thisBoundaryIndex As Integer
     Dim thisClassIdStr As String

     lastMatchingRangeIndexes = ""
     For i = 1 To g_classes.numDescriptors
         If g_classes.descriptors(i).subClassIdStrSeparatePartition.numMaps > 0 And (g_classes.descriptors(i).orMappingSuperClassIndex = g_classes.descriptors(i).classIndex) Then
           ' loop over all sub-classes (ascending order)
           ' - if set of 'matching range definitions' differs to 'previous classid' this classid defines the lower bound of a new effective range

           matchingRangeIndexes = ""
           subClassIdStrings = split(g_classes.descriptors(i).subclassIdStrListNonAbstract, ",")
           lastMinClassIdStr = getClassId(0, 0)
           lowerBoundClassIdStr = getClassId(0, 0)
           minClassIdStr = getClassId(99, 999)
           foundNewMinClassId = True
           thisBoundaryIndex = 1
           While foundNewMinClassId
             foundNewMinClassId = False
             matchingRangeIndexes = ""

             ' lookup 'next smallest' classid
             For j = 0 To UBound(subClassIdStrings)
               thisClassIdStr = Replace(subClassIdStrings(j), "'", "")
               If StrComp(thisClassIdStr, minClassIdStr, vbTextCompare) = -1 And _
                  StrComp(thisClassIdStr, lowerBoundClassIdStr, vbTextCompare) = 1 Then
                 minClassIdStr = Replace(thisClassIdStr, "'", "")
                 foundNewMinClassId = True
               End If
             Next j
 
             If foundNewMinClassId Then
               ' determine set of range-definitions holding this classid
               For k = 1 To g_classes.descriptors(i).subClassIdStrSeparatePartition.numMaps
                 If InStr(1, g_classes.descriptors(i).subClassIdStrSeparatePartition.maps(k).list, "'" & minClassIdStr & "'") Then
                   matchingRangeIndexes = matchingRangeIndexes & "-" & k & "-"
                 End If
               Next k

               ' if set of matching range indexes differs from previous one, this classid defines the lower bound of a new range
               If matchingRangeIndexes <> lastMatchingRangeIndexes And lastMinClassIdStr <> getClassId(0, 0) Then
                 If thisBoundaryIndex = 1 Then
                   g_classes.descriptors(i).subClassPartitionBoundaries(1, thisBoundaryIndex) = ""
                 End If
                 g_classes.descriptors(i).subClassPartitionBoundaries(2, thisBoundaryIndex) = lastMinClassIdStr
                 thisBoundaryIndex = thisBoundaryIndex + 1
                 g_classes.descriptors(i).subClassPartitionBoundaries(1, thisBoundaryIndex) = minClassIdStr
                 g_classes.descriptors(i).subClassPartitionBoundaries(2, thisBoundaryIndex) = ""
               End If
               lastMinClassIdStr = minClassIdStr
               lowerBoundClassIdStr = minClassIdStr
               minClassIdStr = getClassId(99, 999)
               lastMatchingRangeIndexes = matchingRangeIndexes
             End If
           Wend
         End If
     Next i
   End If
 ' ### ENDIF IVK ###
 End Sub
 
 
 ' ### IF IVK ###
 Sub evalClasses2()
   Dim i As Integer, j As Integer
   For i = 1 To g_classes.numDescriptors
       ' determine navigation path to division
       g_classes.descriptors(i).navPathToDiv.relRefIndex = -1
       If g_classes.descriptors(i).navPathStrToDivision <> "" Then
         genNavPathForClass(g_classes.descriptors(i).navPathToDiv, g_classes.descriptors(i).navPathStrToDivision, g_classIndexDivision)
       End If

       ' determine navigation path to Organization
       g_classes.descriptors(i).navPathToOrg.relRefIndex = -1
       If g_classes.descriptors(i).navPathStrToOrg <> "" Then
         genNavPathForClass(g_classes.descriptors(i).navPathToOrg, g_classes.descriptors(i).navPathStrToOrg, g_classIndexOrganization)
       End If

       ' determine navigation path to Code
       g_classes.descriptors(i).navPathToCodeType.relRefIndex = -1
       If g_classes.descriptors(i).navPathStrToCodeType <> "" Then
         genNavPathForClass(g_classes.descriptors(i).navPathToCodeType, g_classes.descriptors(i).navPathStrToCodeType, g_classIndexCodeType)
       End If
   Next i
 End Sub
 
 
 ' ### ENDIF IVK ###
 Private Sub printsubClassHier( _
   thisClassIndex As Integer, _
   level As Integer _
 )
   Dim i As Integer
     For i = LBound(g_classes.descriptors(thisClassIndex).subclassIndexes) To UBound(g_classes.descriptors(thisClassIndex).subclassIndexes)
       If i > 0 Then
         Debug.Print addTab(level) & g_classes.descriptors(thisClassIndex).subclassIndexes(i) & " - " & g_classes.descriptors(g_classes.descriptors(thisClassIndex).subclassIndexes(i)).className
         printsubClassHier(g_classes.descriptors(thisClassIndex).subclassIndexes(i), level + 1)
       End If
     Next i
 End Sub
 
 
 Private Sub printRefs()
   Dim i As Integer, j As Integer
   Dim attr As AttributeDescriptor
   Dim refAttr As AttributeDescriptor

     For i = 1 To g_classes.numDescriptors Step 1
 '        Debug.Print .className & " : " & .attrRefs.numDescriptors
         For j = 1 To g_classes.descriptors(i).attrRefs.numDescriptors
 '          Debug.Print .className & " / " & .attrRefs.descriptors(j).refType & " / " & .attrRefs.descriptors(j).refIndex
         Next j
     Next i

     For i = 1 To g_classes.numDescriptors Step 1
         For j = 1 To g_classes.descriptors(i).attrRefs.numDescriptors
           attr = g_attributes.descriptors(g_classes.descriptors(i).attrRefs.descriptors(j).refIndex)
           If attr.reusedAttrIndex > 0 Then
             refAttr = g_attributes.descriptors(attr.reusedAttrIndex)
             Debug.Print attr.attributeName & "@" & attr.className & " [" & g_classes.descriptors(i).attrRefs.descriptors(j).refIndex & "] -> " & refAttr.attributeName & "@" & refAttr.className & " [" & attr.reusedAttrIndex & "]"
           End If
         Next j
     Next i
 End Sub
 
 
 
 
