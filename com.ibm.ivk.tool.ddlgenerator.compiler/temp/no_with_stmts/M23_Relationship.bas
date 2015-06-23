 Attribute VB_Name = "M23_Relationship"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colSectionName = 2
 Private Const colRelName = colSectionName + 1
 Private Const colAggHeadSection = colRelName + 1
 Private Const colAggHeadName = colAggHeadSection + 1
 Private Const colNameShort = colAggHeadName + 1
 ' ### IF IVK ###
 Private Const colLrtClassification = colNameShort + 1
 Private Const colLrtActivationStatusMode = colLrtClassification + 1
 Private Const colIgnoreForChangeLog = colLrtActivationStatusMode + 1
 ' ### ELSE IVK ###
 'Private Const colIgnoreForChangeLog = colNameShort + 1
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 Private Const colMapsToACMAttribute = colIgnoreForChangeLog + 1
 Private Const colAcmMappingIsInstantiated = colMapsToACMAttribute + 1
 Private Const colNavPathToDivision = colAcmMappingIsInstantiated + 1
 Private Const colReuseName = colNavPathToDivision + 1
 ' ### ELSE IVK ###
 'Private Const colReuseName = colIgnoreForChangeLog + 1
 ' ### ENDIF IVK ###
 Private Const colReuseShortName = colReuseName + 1
 ' ### IF IVK ###
 Private Const colRefersToClAttributes = colReuseShortName + 1
 Private Const colIsCommonToOrgs = colRefersToClAttributes + 1
 ' ### ELSE IVK ###
 'Private Const colIsCommonToOrgs = colReuseShortName + 1
 ' ### ENDIF IVK ###
 Private Const colSpecificToOrg = colIsCommonToOrgs + 1
 Private Const colFkReferenceOrg = colSpecificToOrg + 1
 Private Const colIsCommonToPools = colFkReferenceOrg + 1
 Private Const colSpecificToPool = colIsCommonToPools + 1
 Private Const colFkReferencePool = colSpecificToPool + 1
 Private Const colNoIndexesInPool = colFkReferencePool + 1
 Private Const colUseValueCompression = colNoIndexesInPool + 1
 Private Const colUseSurrogateKey = colUseValueCompression + 1
 Private Const colUseVersionTag = colUseSurrogateKey + 1
 Private Const colRelId = colUseVersionTag + 1
 ' ### IF IVK ###
 Private Const colNoRangePartitioning = colRelId + 1
 Private Const colNotAcmRelated = colNoRangePartitioning + 1
 ' ### ELSE IVK ###
 'Private Const colNotAcmRelated = colRelId + 1
 ' ### ENDIF IVK ###
 Private Const colNoAlias = colNotAcmRelated + 1
 ' ### IF IVK ###
 Private Const colNoXmlExport = colNoAlias + 1
 Private Const colUseXmlExport = colNoXmlExport + 1
 Private Const colIsLrtSpecific = colUseXmlExport + 1
 ' ### ELSE IVK ###
 'Private Const colIsLrtSpecific = colNoAlias + 1
 ' ### ENDIF IVK ###
 Private Const colIsPdmSpecific = colIsLrtSpecific + 1
 ' ### IF IVK ###
 Private Const colIncludeInPdmExportSeqNo = colIsPdmSpecific + 1
 Private Const colIsVolatile = colIncludeInPdmExportSeqNo + 1
 ' ### ELSE IVK ###
 'Private Const colIsVolatile = colIsPdmSpecific + 1
 ' ### ENDIF IVK ###
 Private Const colIsNotEnforced = colIsVolatile + 1
 Private Const colIsNl = colIsNotEnforced + 1
 Private Const colIncludeInPkIndex = colIsNl + 1
 Private Const colLeftSection = colIncludeInPkIndex + 1
 Private Const colLeftClass = colLeftSection + 1
 Private Const colLeftTargetType = colLeftClass + 1
 Private Const colLRName = colLeftTargetType + 1
 Private Const colLRNameShort = colLRName + 1
 Private Const colLRLdmName = colLRNameShort + 1
 Private Const colMinLeftCardinality = colLRLdmName + 1
 Private Const colMaxLeftCardinality = colMinLeftCardinality + 1
 Private Const colIsIdentifyingLeft = colMaxLeftCardinality + 1
 Private Const colLRFkMaintenanceMode = colIsIdentifyingLeft + 1
 Private Const colUseIndexOnLeftFk = colLRFkMaintenanceMode + 1
 ' ### IF IVK ###
 Private Const colLeftDependentAttribute = colUseIndexOnLeftFk + 1
 Private Const colRightSection = colLeftDependentAttribute + 1
 ' ### ELSE IVK ###
 'Private Const colRightSection = colUseIndexOnLeftFk + 1
 ' ### ENDIF IVK ###
 Private Const colRightClass = colRightSection + 1
 Private Const colRightTargetType = colRightClass + 1
 Private Const colRLName = colRightTargetType + 1
 Private Const colRLNameShort = colRLName + 1
 Private Const colRLLdmName = colRLNameShort + 1
 Private Const colMinRightCardinality = colRLLdmName + 1
 Private Const colMaxRightCardinality = colMinRightCardinality + 1
 Private Const colIsIdentifyingRight = colMaxRightCardinality + 1
 Private Const colRLFkMaintenanceMode = colIsIdentifyingRight + 1
 Private Const colUseIndexOnRightFk = colRLFkMaintenanceMode + 1
 ' ### IF IVK ###
 'Private Const colIsRightRefToTimeVarying = colUseIndexOnRightFk + 1
 Private Const colRightDependentAttribute = colUseIndexOnRightFk + 1
 Private Const colIsNationalizable = colRightDependentAttribute + 1
 Private Const colIsPsForming = colIsNationalizable + 1
 Private Const colSupportExtendedPsCopy = colIsPsForming + 1
 Private Const colLogLastChange = colSupportExtendedPsCopy + 1
 ' ### ELSE IVK ###
 'Private Const colLogLastChange = colUseIndexOnRightFk + 1
 ' ### ENDIF IVK ###
 Private Const colLogLastChangeInView = colLogLastChange + 1
 Private Const colLogLastChangeAutoMaint = colLogLastChangeInView + 1
 Private Const colIsUserTransactional = colLogLastChangeAutoMaint + 1
 Private Const colUseMqtToImplementLrt = colIsUserTransactional + 1
 ' ### IF IVK ###
 Private Const colNoTransferToProduction = colUseMqtToImplementLrt + 1
 Private Const colNoFto = colNoTransferToProduction + 1
 Private Const colFtoSingleObjProcessing = colNoFto + 1
 Private Const colTabSpaceData = colFtoSingleObjProcessing + 1
 ' ### ELSE IVK ###
 'Private Const colTabSpaceData = colUseMqtToImplementLrt + 1
 ' ### ENDIF IVK ###
 Private Const colTabSpaceLong = colTabSpaceData + 1
 Private Const colTabSpaceNl = colTabSpaceLong + 1
 Private Const colTabSpaceIndex = colTabSpaceNl + 1
 Private Const colIsTv = colTabSpaceIndex + 1
 Private Const colI18nId = colIsTv + 1
 
 Global Const colRelI18nId = colI18nId
 
 Private Const firstRow = 4
 
 Private Const sheetName = "Rel"
 
 ' ### IF IVK ###
 Private Const processingStepPsCopy = 1
 Private Const processingStepPsCopy2 = 2
 Private Const processingStepExpCopy = 6
 Private Const processingStepSetProd = 5
 Private Const processingStepFto = 3
 Private Const processingStepAllowedCountries = 4
 ' ### ENDIF IVK ###
 Private Const processingStep = 3
 Private Const processingStepLrt = 4
 Private Const processingStepAcmCsv = 2
 
 Global g_relationships As RelationshipDescriptors
 ' ### IF IVK ###
 
 Private Const maxAlCountryListLen = 1024
 ' ### ENDIF IVK ###
 
 
 Sub genAttrMapping( _
    ByRef mapping() As AttributeMappingForCl, _
    ByRef str As String, _
    Optional isTv As Boolean = False, _
    Optional attrIndex As Integer = -1 _
 )
   Dim list() As String
   Dim elems() As String
   Dim prio As Integer
   prio = 0
   list = split("", ",")
   list = split(str, ",")

   If UBound(list) >= 0 Then
     ReDim mapping(0 To UBound(list))
   End If

   Dim i As Integer
   For i = LBound(list) To UBound(list)
     list(i) = Trim(list(i))

     If InStr(list(i), ":") Then
       elems = split(list(i), ":")
       prio = CInt(elems(0))
       list(i) = elems(1)
     End If

     elems = split(list(i), "/")
     If UBound(elems) = 1 Then
         mapping(i).prio = prio
         mapping(i).mapFrom = elems(0)
         mapping(i).mapTo = elems(1)
         mapping(i).isTv = isTv
         mapping(i).attrIndex = attrIndex
     End If
   Next i
 End Sub


 Sub addAttrMapping( _
    ByRef mapping() As AttributeMappingForCl, _
    ByRef mapFrom As String, _
    ByRef mapTo As String, _
    Optional isTv As Boolean = False, _
    Optional attrIndex As Integer = -1 _
 )
   If arrayIsNull(mapping) Then
     ReDim mapping(0 To 0)
   Else
     ReDim Preserve mapping(0 To UBound(mapping) + 1)
   End If

     mapping(UBound(mapping)).mapFrom = mapFrom
     mapping(UBound(mapping)).mapTo = mapTo
     mapping(UBound(mapping)).isTv = isTv
     mapping(UBound(mapping)).attrIndex = attrIndex
 End Sub

 
 Private Sub readSheet()
   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   Dim lastSection As String
   Dim clMapping As String
   While thisSheet.Cells(thisRow, colRelName) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).sectionName = thisSheet.Cells(thisRow, colSectionName)
       If (g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).sectionName & "" = "") Then g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).sectionName = lastSection
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).relName = Trim(thisSheet.Cells(thisRow, colRelName))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).i18nId = Trim(thisSheet.Cells(thisRow, colI18nId))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).aggHeadSection = Trim(thisSheet.Cells(thisRow, colAggHeadSection))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).aggHeadName = Trim(thisSheet.Cells(thisRow, colAggHeadName))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).shortName = Trim(thisSheet.Cells(thisRow, colNameShort))
 ' ### IF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).lrtClassification = Trim(thisSheet.Cells(thisRow, colLrtClassification))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).lrtActivationStatusMode = Trim(thisSheet.Cells(thisRow, colLrtActivationStatusMode))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).navPathStrToDivision = Trim(thisSheet.Cells(thisRow, colNavPathToDivision))
 ' ### ENDIF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).ignoreForChangelog = getBoolean(thisSheet.Cells(thisRow, colIgnoreForChangeLog))

 ' ### IF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).virtuallyMapsTo.description = Trim(thisSheet.Cells(thisRow, colMapsToACMAttribute))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isVirtual = (g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).virtuallyMapsTo.description <> "")
       If g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isVirtual Then
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).virtuallyMapsTo.isInstantiated = getBoolean(thisSheet.Cells(thisRow, colAcmMappingIsInstantiated))
       End If

 ' ### ENDIF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).reuseName = Trim(thisSheet.Cells(thisRow, colReuseName))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).reuseShortName = Trim(thisSheet.Cells(thisRow, colReuseShortName))
 ' ### IF IVK ###
       clMapping = Trim(thisSheet.Cells(thisRow, colRefersToClAttributes))
       If clMapping <> "" Then
         genAttrMapping g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).refersToClAttribute, clMapping
       End If
 ' ### ENDIF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).leftClassSectionName = Trim(thisSheet.Cells(thisRow, colLeftSection))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).leftClassName = Trim(thisSheet.Cells(thisRow, colLeftClass))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).leftTargetType = getRelRefTargetType(thisSheet.Cells(thisRow, colLeftTargetType))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).lrRelName = Trim(thisSheet.Cells(thisRow, colLRName))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).lrShortRelName = Trim(thisSheet.Cells(thisRow, colLRNameShort))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).lrLdmRelName = Trim(thisSheet.Cells(thisRow, colLRLdmName))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).useLrLdmRelName = g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).lrLdmRelName <> ""
       If g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).lrLdmRelName = "-" Then
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).lrLdmRelName = ""
       End If
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isCommonToOrgs = getBoolean(thisSheet.Cells(thisRow, colIsCommonToOrgs))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).specificToOrgId = getInteger(thisSheet.Cells(thisRow, colSpecificToOrg))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).fkReferenceOrgId = getInteger(thisSheet.Cells(thisRow, colFkReferenceOrg))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isCommonToPools = g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isCommonToOrgs Or getBoolean(thisSheet.Cells(thisRow, colIsCommonToPools))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).specificToPool = getInteger(thisSheet.Cells(thisRow, colSpecificToPool))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).fkReferencePoolId = getInteger(thisSheet.Cells(thisRow, colFkReferencePool))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).noIndexesInPool = getInteger(thisSheet.Cells(thisRow, colNoIndexesInPool))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).useValueCompression = getBoolean(thisSheet.Cells(thisRow, colUseValueCompression))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).useSurrogateKey = getBoolean(thisSheet.Cells(thisRow, colUseSurrogateKey))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).useVersiontag = getBoolean(thisSheet.Cells(thisRow, colUseVersionTag))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).relId = getInteger(thisSheet.Cells(thisRow, colRelId))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).notAcmRelated = getBoolean(thisSheet.Cells(thisRow, colNotAcmRelated))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).noAlias = getBoolean(thisSheet.Cells(thisRow, colNoAlias))
 ' ### IF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).noRangePartitioning = getBoolean(thisSheet.Cells(thisRow, colNoRangePartitioning))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).noXmlExport = getBoolean(thisSheet.Cells(thisRow, colNoXmlExport))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).useXmlExport = getBoolean(thisSheet.Cells(thisRow, colUseXmlExport))
 ' ### ENDIF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isLrtSpecific = getBoolean(thisSheet.Cells(thisRow, colIsLrtSpecific))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isPdmSpecific = getBoolean(thisSheet.Cells(thisRow, colIsPdmSpecific))
 ' ### IF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).includeInPdmExportSeqNo = getInteger(thisSheet.Cells(thisRow, colIncludeInPdmExportSeqNo), -1)
 ' ### ENDIF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isVolatile = getBoolean(thisSheet.Cells(thisRow, colIsVolatile))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isNotEnforced = getBoolean(thisSheet.Cells(thisRow, colIsNotEnforced))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isNl = getBoolean(thisSheet.Cells(thisRow, colIsNl))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).includeInPkIndex = getBoolean(thisSheet.Cells(thisRow, colIncludeInPkIndex))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).minLeftCardinality = getInteger(thisSheet.Cells(thisRow, colMinLeftCardinality))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).maxLeftCardinality = getInteger(thisSheet.Cells(thisRow, colMaxLeftCardinality))
       If g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).minLeftCardinality = -1 Then
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).minLeftCardinality = IIf(g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).maxLeftCardinality = 1, 1, 0)
       End If
 
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingLeft = getBoolean(thisSheet.Cells(thisRow, colIsIdentifyingLeft))

       If g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingLeft And g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).maxLeftCardinality <> 1 Then
         logMsg "unable to implement ACM-related relationship """ & g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).sectionName & "." & g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).relName & " as 'left-identifying' since 'max left cardinality <> 1' - fixed", _
                ellFixableWarning
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingLeft = False
       ElseIf g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingRight And g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).minRightCardinality <> 1 Then
         logMsg "unable to implement ACM-related relationship """ & g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).sectionName & "." & g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).relName & " as 'left-identifying' since 'min left cardinality <> 1' - fixed", _
                ellFixableWarning
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingLeft = False
       End If

       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).lrFkMaintenanceMode = getFkMaintenanceMode(thisSheet.Cells(thisRow, colLRFkMaintenanceMode))

       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).useIndexOnLeftFk = getBoolean(thisSheet.Cells(thisRow, colUseIndexOnLeftFk))
 ' ### IF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).leftDependentAttribute = Trim(thisSheet.Cells(thisRow, colLeftDependentAttribute))
 ' ### ENDIF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rightClassSectionName = Trim(thisSheet.Cells(thisRow, colRightSection))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rightClassName = Trim(thisSheet.Cells(thisRow, colRightClass))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rightTargetType = getRelRefTargetType(thisSheet.Cells(thisRow, colRightTargetType))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rlRelName = Trim(thisSheet.Cells(thisRow, colRLName))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rlShortRelName = Trim(thisSheet.Cells(thisRow, colRLNameShort))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rlLdmRelName = Trim(thisSheet.Cells(thisRow, colRLLdmName))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).useRlLdmRelName = g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rlLdmRelName <> ""
       If g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rlLdmRelName = "-" Then
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rlLdmRelName = ""
       End If
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).minRightCardinality = getInteger(thisSheet.Cells(thisRow, colMinRightCardinality))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).maxRightCardinality = getInteger(thisSheet.Cells(thisRow, colMaxRightCardinality))
       If g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).minRightCardinality = -1 Then
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).minRightCardinality = IIf(g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).maxRightCardinality = 1, 1, 0)
       End If
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingRight = getBoolean(thisSheet.Cells(thisRow, colIsIdentifyingRight))

       If g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingRight And g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).maxRightCardinality <> 1 Then
         logMsg "unable to implement ACM-related relationship """ & g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).sectionName & "." & g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).relName & " as 'right-identifying' since 'max right cardinality <> 1' - fixed", _
                ellFixableWarning
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingRight = False
       ElseIf g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingRight And g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).minRightCardinality <> 1 Then
         logMsg "unable to implement ACM-related relationship """ & g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).sectionName & "." & g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).relName & " as 'right-identifying' since 'min right cardinality <> 1' - fixed", _
                ellFixableWarning
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isIdentifyingRight = False
       End If

       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rlFkMaintenanceMode = getFkMaintenanceMode(thisSheet.Cells(thisRow, colRLFkMaintenanceMode))

       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).useIndexOnRightFk = getBoolean(thisSheet.Cells(thisRow, colUseIndexOnRightFk))
 ' ### IF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).rightDependentAttribute = Trim(thisSheet.Cells(thisRow, colRightDependentAttribute))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isNationalizable = getBoolean(thisSheet.Cells(thisRow, colIsNationalizable))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isPsForming = getBoolean(thisSheet.Cells(thisRow, colIsPsForming))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).supportExtendedPsCopy = getBoolean(thisSheet.Cells(thisRow, colSupportExtendedPsCopy))
 ' ### ENDIF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).logLastChange = getBoolean(thisSheet.Cells(thisRow, colLogLastChange))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).logLastChangeInView = getBoolean(thisSheet.Cells(thisRow, colLogLastChangeInView))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).logLastChangeAutoMaint = getBoolean(thisSheet.Cells(thisRow, colLogLastChangeAutoMaint))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isUserTransactional = getBoolean(thisSheet.Cells(thisRow, colIsUserTransactional))
       If UCase(Trim(thisSheet.Cells(thisRow, colIsUserTransactional))) = "M" Then
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isLrtMeta = True
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isUserTransactional = False
       Else
         g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isUserTransactional = getBoolean(thisSheet.Cells(thisRow, colIsUserTransactional))
       End If
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).useMqtToImplementLrt = getBoolean(thisSheet.Cells(thisRow, colUseMqtToImplementLrt))
 ' ### IF IVK ###
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).noTransferToProduction = getBoolean(thisSheet.Cells(thisRow, colNoTransferToProduction))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).noFto = getBoolean(thisSheet.Cells(thisRow, colNoFto))
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).ftoSingleObjProcessing = getBoolean(thisSheet.Cells(thisRow, colFtoSingleObjProcessing))
 ' ### ENDIF IVK ###

       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).tabSpaceData = thisSheet.Cells(thisRow, colTabSpaceData)
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).tabSpaceLong = thisSheet.Cells(thisRow, colTabSpaceLong)
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).tabSpaceNl = thisSheet.Cells(thisRow, colTabSpaceNl)
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).tabSpaceIndex = thisSheet.Cells(thisRow, colTabSpaceIndex)
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isTimeVarying = getBoolean(thisSheet.Cells(thisRow, colIsTv))
 
 ' ### IF IVK ###

       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).defaultStatus = statusReadyForActivation
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isSubjectToArchiving = False
       g_relationships.descriptors(allocRelationshipDescriptorIndex(g_relationships)).isMdsExpressionRel = False
 ' ### ENDIF IVK ###
 
 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub resetRelationshipsCsvExported()
   Dim i As Integer
     For i = 1 To g_relationships.numDescriptors Step 1
       g_relationships.descriptors(i).isLdmCsvExported = False
       g_relationships.descriptors(i).isLdmLrtCsvExported = False
 ' ### IF IVK ###
       g_relationships.descriptors(i).isXsdExported = False
 ' ### ENDIF IVK ###
       g_relationships.descriptors(i).isCtoAliasCreated = False
     Next i
 End Sub
 
 
 Sub getRelationships()
   If g_relationships.numDescriptors = 0 Then
     readSheet
   End If
 End Sub
 
 
 Sub resetRelationships()
   g_relationships.numDescriptors = 0
 End Sub
 
 
 Function getMaxRelIdBySection( _
   ByRef sectionName As String _
 ) As Integer
   Dim maxRelId As Integer
   maxRelId = 0

   Dim i As Integer
   For i = 1 To g_relationships.numDescriptors
       If g_relationships.descriptors(i).sectionName = sectionName And g_relationships.descriptors(i).relId > maxRelId Then
         maxRelId = g_relationships.descriptors(i).relId
       End If
   Next i

   getMaxRelIdBySection = maxRelId
 End Function
 
 
 Sub setRelationshipReusedRelIndex( _
   relIndex As Integer, _
   reusedRelIndex As Integer _
 )
   If relIndex > 0 Then
     While g_relationships.descriptors(reusedRelIndex).reusedRelIndex > 0
       reusedRelIndex = g_relationships.descriptors(reusedRelIndex).reusedRelIndex
     Wend
     g_relationships.descriptors(relIndex).reusedRelIndex = reusedRelIndex
 
     Dim msg As String
       msg = "reusing relationship """ & g_relationships.descriptors(reusedRelIndex).sectionName & "." & g_relationships.descriptors(reusedRelIndex).relName & """ [" & g_relationships.descriptors(reusedRelIndex).leftClassSectionName & "." & g_relationships.descriptors(reusedRelIndex).leftClassName & "<->" & g_relationships.descriptors(reusedRelIndex).rightClassSectionName & "." & g_relationships.descriptors(reusedRelIndex).rightClassName & "] (" & reusedRelIndex & ")"
       msg = msg & " for """ & g_relationships.descriptors(relIndex).sectionName & "." & g_relationships.descriptors(relIndex).relName & """ [" & g_relationships.descriptors(relIndex).leftClassSectionName & "." & g_relationships.descriptors(relIndex).leftClassName & "<->" & g_relationships.descriptors(relIndex).rightClassSectionName & "." & g_relationships.descriptors(relIndex).rightClassName & "] (" & relIndex & ")"

     logMsg msg, ellInfo
 
     'Debug.Print msg
     If reusedRelIndex > 0 Then
       ' add 'relIndex' to the list of relIndexes 'reusing' the relationship
       addRelIndex g_relationships.descriptors(reusedRelIndex).reusingRelIndexes, relIndex
     End If
   End If
 End Sub


 Function getRelIndexByName( _
   ByRef sectionName As String, _
   ByRef relName As String, _
   Optional silent As Boolean = False _
 ) As Integer
   Dim i As Integer
 
   getRelationships

   getRelIndexByName = -1
 
   For i = 1 To g_relationships.numDescriptors Step 1
     If UCase(g_relationships.descriptors(i).relName) = UCase(relName) And _
        UCase(g_relationships.descriptors(i).sectionName) = UCase(sectionName) Then
       getRelIndexByName = i
       Exit Function
     End If
   Next i
 
   If Not silent Then
     logMsg "unable to identify relationship '" & sectionName & "." & relName & "'", ellError, edtLdm
   End If
 End Function
 
 
 Function getRelIdStrByIndex( _
   relIndex As Integer _
 ) As String
   Dim i As Integer
 
   getRelIdStrByIndex = -1
 
   If relIndex > 0 And relIndex < g_relationships.numDescriptors Then
       getRelIdStrByIndex = g_relationships.descriptors(relIndex).relIdStr
   End If
 End Function
 
 
 Function getRelIndexByI18nId( _
   ByRef i18nId As String _
 ) As Integer
   Dim i As Integer
 
   getRelIndexByI18nId = -1
 
   For i = 1 To g_relationships.numDescriptors Step 1
     If UCase(g_relationships.descriptors(i).i18nId) = UCase(i18nId) Then
       getRelIndexByI18nId = i
       Exit Function
     End If
   Next i
 End Function
 
 
 Sub genTransformedAttrDeclsForRelationshipWithColReUse( _
   thisRelIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
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
   Dim ukAttrDecls As String
   Dim pkAttrList As String
   Dim leftFkAttrs As String
   Dim rightFkAttrs As String

   On Error GoTo ErrorExit

   genTransformedAttrDeclsForRelationshipWithColReUse_Int thisRelIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, _
     fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, suppressMetaAttrs, forLrt, outputMode
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrDeclsForRelationshipWithColReUse_Int( _
   thisRelIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   ByRef ukAttrDecls As String, _
   ByRef pkAttrList As String, _
   ByRef leftFkAttrs As String, _
   ByRef rightFkAttrs As String, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forGen As Boolean = False, _
   Optional suppressMetaAttrs As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional useAlternativeDefaults As Boolean = False _
 )
   Dim numAttrs As Integer

   On Error GoTo ErrorExit

     numAttrs = g_relationships.descriptors(thisRelIndex).attrRefs.numDescriptors

     If Not suppressMetaAttrs And useSurrogateKeysForNMRelationships And g_relationships.descriptors(thisRelIndex).useSurrogateKey Then
       printSectionHeader "Surrogate Key", fileNo, outputMode
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conOid, cosnOid, eavtDomain, g_domainIndexOid, _
           transformation, tabColumns, eactRelationship, g_relationships.descriptors(thisRelIndex).relIndex, "NOT NULL", , ddlType, , outputMode, _
           eacOid, , indent, , "[LDM] Relationship identifier" _
         )
       pkAttrList = genAttrName(conOid, ddlType)
     End If

     genTransformedAttrDeclsForEntityWithColReUse eactRelationship, g_relationships.descriptors(thisRelIndex).relIndex, _
       transformation, tabColumns, False, fileNo, ddlType, thisOrgIndex, thisPoolIndex, False, False, False, suppressMetaAttrs, False, g_relationships.descriptors(thisRelIndex).isUserTransactional, , forLrt, _
       outputMode, indent, , , , useAlternativeDefaults

     If g_relationships.descriptors(thisRelIndex).logLastChange And (Not forLrt Or g_cfgGenLogChangeForLrtTabs) And Not suppressMetaAttrs Then
       genTransformedLogChangeAttrDeclsWithColReUse fileNo, transformation, tabColumns, eactRelationship, g_relationships.descriptors(thisRelIndex).relIndex, ddlType, g_relationships.descriptors(thisRelIndex).relName, outputMode, indent, , useAlternativeDefaults
     End If
 
     If reuseRelationships And g_relationships.descriptors(thisRelIndex).reusingRelIndexes.numIndexes > 0 Then
       Dim i As Integer
       For i = 1 To g_relationships.descriptors(thisRelIndex).reusingRelIndexes.numIndexes Step 1
         genTransformedAttrDeclsForRelationshipWithColReUse g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i), _
           transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, , True, forLrt, outputMode

         genTransformedAttrDeclForRelationshipsByRelWithColReuse g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i), _
           transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, outputMode, indent
       Next i
     End If

     If suppressMetaAttrs Then
       Exit Sub
     End If

     Dim leftClass As ClassDescriptor, rightclass As ClassDescriptor
     leftClass = g_classes.descriptors(g_relationships.descriptors(thisRelIndex).leftEntityIndex)
     rightclass = g_classes.descriptors(g_relationships.descriptors(thisRelIndex).rightEntityIndex)
     Dim leftOrClass As ClassDescriptor, rightOrClass As ClassDescriptor
     leftOrClass = getOrMappingSuperClass(leftClass.sectionName, leftClass.className)
     rightOrClass = getOrMappingSuperClass(rightclass.sectionName, rightclass.className)

     ukAttrDecls = ""
     printSectionHeader "Foreign Key corresponding to Class """ & leftClass.sectionName & "." & leftClass.className & """", fileNo, outputMode
 ' ### IF IVK ###
     leftFkAttrs = _
       genFkTransformedAttrDeclsWithColReuse(leftClass.classIndex, _
         IIf((g_relationships.descriptors(thisRelIndex).minLeftCardinality = 0) And (g_relationships.descriptors(thisRelIndex).maxLeftCardinality = 1) And g_relationships.descriptors(thisRelIndex).isNationalizable, "", "NOT NULL"), _
         leftOrClass.isPsForming, transformation, tabColumns, leftClass.className, _
         leftClass.shortName, fileNo, ddlType, , , _
         True, ukAttrDecls, outputMode, indent)
     If g_relationships.descriptors(thisRelIndex).isNationalizable And g_relationships.descriptors(thisRelIndex).maxLeftCardinality = 1 Then
       printSectionHeader "Foreign Key (nationalized) corresponding to Class """ & leftClass.sectionName & "." & leftClass.className & """", fileNo
       genFkTransformedAttrDeclsWithColReuse leftOrClass.classIndex, "", leftOrClass.isPsForming, transformation, tabColumns, leftClass.className, _
         leftClass.shortName, fileNo, ddlType, , True, , , outputMode, indent, True
     End If
 ' ### ELSE IVK ###
 '   leftFkAttrs = _
 '     genFkTransformedAttrDeclsWithColReuse(leftClass.classIndex, "NOT NULL", False, transformation, tabColumns, leftClass.className, _
 '       leftClass.shortName, fileNo, ddlType, , , True, ukAttrDecls, outputMode, indent)
 ' ### ENDIF IVK ###
 '    End With

     Dim addComma As Boolean
     addComma = _
       g_relationships.descriptors(thisRelIndex).useVersiontag Or _
       (supportNlForRelationships And g_relationships.descriptors(thisRelIndex).isNl) Or _
       g_relationships.descriptors(thisRelIndex).isPsTagged

     printSectionHeader "Foreign Key corresponding to Class """ & rightclass.sectionName & "." & rightclass.className & """", fileNo, outputMode
 ' ### IF IVK ###
     rightFkAttrs = _
       genFkTransformedAttrDeclsWithColReuse(rightclass.classIndex, _
         IIf((g_relationships.descriptors(thisRelIndex).minRightCardinality = 0) And (g_relationships.descriptors(thisRelIndex).maxRightCardinality = 1) And g_relationships.descriptors(thisRelIndex).isNationalizable, "", "NOT NULL"), _
         rightOrClass.isPsForming, transformation, tabColumns, rightclass.className, _
         rightclass.shortName, fileNo, ddlType, _
         addComma Or (g_relationships.descriptors(thisRelIndex).isNationalizable And g_relationships.descriptors(thisRelIndex).maxRightCardinality = 1), , _
         True, ukAttrDecls, outputMode, indent)
     If g_relationships.descriptors(thisRelIndex).isNationalizable And g_relationships.descriptors(thisRelIndex).maxRightCardinality = 1 Then
       printSectionHeader "Foreign Key (nationalized) corresponding to Class """ & rightclass.sectionName & "." & rightclass.className & """", fileNo, outputMode
       genFkTransformedAttrDeclsWithColReuse rightOrClass.classIndex, "", rightOrClass.isPsForming, transformation, tabColumns, rightclass.className, _
         rightclass.shortName, fileNo, ddlType, addComma, True, , , outputMode, indent, True
     End If
 ' ### ELSE IVK ###
 '   rightFkAttrs = _
 '     genFkTransformedAttrDeclsWithColReuse(rightclass.classIndex, "NOT NULL", False, transformation, tabColumns, rightclass.className, _
 '       rightClass.shortName, fileNo, ddlType, addComma, , True, ukAttrDecls, outputMode, indent)
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
     If supportNlForRelationships And g_relationships.descriptors(thisRelIndex).isNl Then
       addComma = _
         g_relationships.descriptors(thisRelIndex).useVersiontag Or _
         g_relationships.descriptors(thisRelIndex).isNationalizable Or _
         g_relationships.descriptors(thisRelIndex).isPsTagged

       printSectionHeader "Language Id (Relationship has stereotype <nlText>)", fileNo, outputMode

       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conLanguageId, cosnLanguageId, eavtDomainEnumId, g_enumIndexLanguage, transformation, tabColumns, _
           eactRelationship, g_relationships.descriptors(thisRelIndex).relIndex, "NOT NULL", addComma, ddlType, , outputMode, eacLangId, , indent, , "[LDM] Language identifier" _
         )

       If g_relationships.descriptors(thisRelIndex).isNationalizable Then
         addComma = _
           g_relationships.descriptors(thisRelIndex).useVersiontag Or _
           g_relationships.descriptors(thisRelIndex).isPsTagged

         printSectionHeader "Is the nationalized reference active?", fileNo, outputMode
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conIsNationalActive, cosnIsNationalActive, eavtDomain, g_domainIndexBoolean, transformation, tabColumns, _
             eactRelationship, g_relationships.descriptors(thisRelIndex).relIndex, "NOT NULL DEFAULT 0" & _
             IIf(ddlType = edtPdm And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), addComma, _
             ddlType, , outputMode, eacRegular Or eacNationalBool, , indent, , "[LDM] Is the nationalized reference active?", gc_dbFalse _
           )
        End If
      End If
 
     If g_relationships.descriptors(thisRelIndex).isPsTagged Then
       ' this relationship also needs to be considered PS-tagged
       printSectionHeader "Product Structure Tag", fileNo, outputMode
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conPsOid, cosnPsOid, eavtDomain, g_domainIndexOid, _
           transformation, tabColumns, eactRelationship, g_relationships.descriptors(thisRelIndex).relIndex, "NOT NULL", g_relationships.descriptors(thisRelIndex).useVersiontag, ddlType, , _
           outputMode, eacPsOid, , indent, , "[LDM] Product Structure Tag" _
         )
     Else
        If g_relationships.descriptors(thisRelIndex).aggHeadName = "GenericCode" Then
          printSectionHeader "Division-Column", fileNo, outputMode
          printConditional fileNo, _
            genTransformedAttrDeclByDomainWithColReUse( _
            conDivOid, cosnDivOid, eavtDomain, g_domainIndexOid, _
            transformation, tabColumns, eactRelationship, g_relationships.descriptors(thisRelIndex).relIndex, "NOT NULL DEFAULT 0", g_relationships.descriptors(thisRelIndex).useVersiontag, ddlType, , _
            outputMode, eacDivOid, , indent, , "[LDM] Division Tag" _
          )
        End If
     End If

 ' ### ENDIF IVK ###
     If g_relationships.descriptors(thisRelIndex).useVersiontag Then
       printSectionHeader "Relationship Version Id", fileNo, outputMode
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conVersionId, cosnVersionId, eavtDomain, g_domainIndexVersion, transformation, tabColumns, _
           eactRelationship, g_relationships.descriptors(thisRelIndex).relIndex, "NOT NULL DEFAULT 1" & IIf(ddlType = edtPdm And dbCompressSystemDefaults, _
           " COMPRESS SYSTEM DEFAULT", ""), False, ddlType, , outputMode, eacVid, , indent, , "[LDM] Record version tag", "1" _
         )
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 
 Sub genRelIdList()
   If Not generateEntityIdList Then
     Exit Sub
   End If
 
   Dim fileName As String
   fileName = genMetaFileName(g_targetDir, "RelId")
   assertDir fileName
   Dim fileNo As Integer
   fileNo = FreeFile()

   On Error GoTo ErrorExit
   Open fileName For Output As #fileNo

   Dim thisRelIndex As Integer
   Dim maxQualRelNameLen As Integer
   maxQualRelNameLen = 0

     For thisRelIndex = 1 To g_relationships.numDescriptors Step 1
         If Not g_relationships.descriptors(thisRelIndex).notAcmRelated And g_relationships.descriptors(thisRelIndex).relId > 0 Then
           If Len(g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName) > maxQualRelNameLen Then
             maxQualRelNameLen = Len(g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName)
           End If
         End If
     Next thisRelIndex

     For thisRelIndex = 1 To g_relationships.numDescriptors Step 1
         If Not g_relationships.descriptors(thisRelIndex).notAcmRelated And g_relationships.descriptors(thisRelIndex).relId > 0 Then
           Print #fileNo, paddRight(g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName, maxQualRelNameLen) & " : " & g_relationships.descriptors(thisRelIndex).relIdStr
         End If
     Next thisRelIndex
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Private Sub genAllowedCountriesListFunction( _
   thisRelIndex As Integer, _
   fileNo As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
     Dim isDisallowedCountries As Boolean
     Dim targetClassIndex As Integer
     Dim targetClassName As String
     Dim targetSectionName As String
     Dim qualTabName As String
     Dim qualTabNameLrt As String
     Dim targetIsGenericAspect As Boolean

     Dim qualTabNameCountryIdXRef As String
     qualTabNameCountryIdXRef = genQualTabNameByRelIndex(g_relIndexCountryIdXRef, ddlType, thisOrgIndex, thisPoolIndex)
     Dim qualTabNameOrgManagesCountry As String
     qualTabNameOrgManagesCountry = genQualTabNameByRelIndex(g_relIndexOrgManagesCountry, ddlType, thisOrgIndex, thisPoolIndex)
     Dim qualTabNameCountrySpec As String
     qualTabNameCountrySpec = genQualTabNameByClassIndex(g_classIndexCountrySpec, ddlType, thisOrgIndex, thisPoolIndex)

     If g_relationships.descriptors(thisRelIndex).isDisallowedCountriesList <> ernmNone Or g_relationships.descriptors(thisRelIndex).isAllowedCountriesList <> ernmNone Then
         targetClassName = g_classes.descriptors(g_classes.descriptors(IIf(g_relationships.descriptors(thisRelIndex).isDisallowedCountriesList = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountriesList = ernmLeft, g_relationships.descriptors(thisRelIndex).rightEntityIndex, g_relationships.descriptors(thisRelIndex).leftEntityIndex)).orMappingSuperClassIndex).className
         targetSectionName = g_classes.descriptors(g_classes.descriptors(IIf(g_relationships.descriptors(thisRelIndex).isDisallowedCountriesList = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountriesList = ernmLeft, g_relationships.descriptors(thisRelIndex).rightEntityIndex, g_relationships.descriptors(thisRelIndex).leftEntityIndex)).orMappingSuperClassIndex).sectionName
         qualTabName = genQualTabNameByClassIndex(g_classes.descriptors(IIf(g_relationships.descriptors(thisRelIndex).isDisallowedCountriesList = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountriesList = ernmLeft, g_relationships.descriptors(thisRelIndex).rightEntityIndex, g_relationships.descriptors(thisRelIndex).leftEntityIndex)).orMappingSuperClassIndex, ddlType, thisOrgIndex, thisPoolIndex)
         qualTabNameLrt = genQualTabNameByClassIndex(g_classes.descriptors(IIf(g_relationships.descriptors(thisRelIndex).isDisallowedCountriesList = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountriesList = ernmLeft, g_relationships.descriptors(thisRelIndex).rightEntityIndex, g_relationships.descriptors(thisRelIndex).leftEntityIndex)).orMappingSuperClassIndex, ddlType, thisOrgIndex, thisPoolIndex, , True)

         targetIsGenericAspect = (g_classes.descriptors(g_classes.descriptors(IIf(g_relationships.descriptors(thisRelIndex).isDisallowedCountriesList = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountriesList = ernmLeft, g_relationships.descriptors(thisRelIndex).rightEntityIndex, g_relationships.descriptors(thisRelIndex).leftEntityIndex)).orMappingSuperClassIndex).classIndex = g_classIndexGenericAspect)
     End If
     isDisallowedCountries = (g_relationships.descriptors(thisRelIndex).isDisallowedCountriesList <> ernmNone)

     If targetClassName = clnGenericAspect Then
       ' we currently only support utility functions for this class
     Else
       Exit Sub
     End If

     Dim poolSupportLrt As Boolean
     poolSupportLrt = False
     If thisPoolIndex > 0 Then
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
     End If

     Dim isWorkDataPool As Boolean
     isWorkDataPool = (thisPoolIndex = g_workDataPoolIndex)

     Dim qualFuncName As String
     Dim use3DigitIds As Boolean
     Dim funcName As String
     Dim maxResLen As Integer

     Dim i As Integer
     For i = 1 To 2
       use3DigitIds = (i = 2)
       If isDisallowedCountries Then
         funcName = IIf(use3DigitIds, udfnDisallowedCountry2Str0, udfnDisallowedCountry2Str)
         maxResLen = gc_disallowedCountriesMaxLength
       Else
         funcName = IIf(use3DigitIds, udfnAllowedCountry2Str0, udfnAllowedCountry2Str)
         maxResLen = gc_allowedCountriesMaxLength
       End If

       Dim lrtAware As Boolean
       Dim includeDeletedPrivRecords As Boolean
       Dim includeDeletedPubRecords As Boolean
       Dim udfSuffixName As String
       Dim k As Integer
       For k = 1 To IIf((ddlType = edtLdm Or poolSupportLrt) And use3DigitIds, 4, 1)
         lrtAware = ((k = 2) Or (k = 3))

         includeDeletedPrivRecords = False
         includeDeletedPubRecords = False
         udfSuffixName = ""
         If k = 3 Then
           includeDeletedPrivRecords = True
           udfSuffixName = "_D"
         ElseIf k = 4 Then
           includeDeletedPubRecords = True
           udfSuffixName = "_D"
         End If

         Dim targetSectionIndex As Integer
         Dim l As Integer
         For l = 1 To IIf(use3DigitIds And targetIsGenericAspect And k = 1, 2, 1)
           targetSectionIndex = IIf(l = 1, g_relationships.descriptors(thisRelIndex).sectionIndex, g_sectionIndexAliasLrt)

           qualFuncName = genQualFuncName(targetSectionIndex, funcName & udfSuffixName, ddlType, thisOrgIndex, thisPoolIndex)

           printSectionHeader "Function for concatenating CountrySpec-IDs for ACM-Relationship """ & _
                               g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """" & IIf(use3DigitIds, " (use 3-digit IDs" & _
                               IIf(lrtAware, " / LRT-aware", "") & _
                               IIf(includeDeletedPrivRecords Or includeDeletedPubRecords, " / for deleted records", "") & ")", ""), fileNo

           Print #fileNo,
           Print #fileNo, addTab(0); "CREATE FUNCTION"
           Print #fileNo, addTab(1); qualFuncName
           Print #fileNo, addTab(0); "("
           genProcParm fileNo, "", "oid_in", g_dbtOid, True, "OID of '" & targetSectionName & "." & targetClassName & "'-object"
           If lrtAware Then
             genProcParm fileNo, "", "lrtOid_in", g_dbtOid, True, "OID of the LRT used for reference"
           End If
           genProcParm fileNo, "", "maxLength_in", "INTEGER", False, "maximum length of string returned"
           Print #fileNo, addTab(0); ")"

           Print #fileNo, addTab(0); "RETURNS"
           Print #fileNo, addTab(1); "VARCHAR("; CStr(maxAlCountryListLen); ")"
           Print #fileNo, addTab(0); "LANGUAGE SQL"
           Print #fileNo, addTab(0); "DETERMINISTIC"
           Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
           Print #fileNo, addTab(0); "READS SQL DATA"
           Print #fileNo, addTab(0); "BEGIN ATOMIC"

           genProcSectionHeader fileNo, "declare variables", , True
           genVarDecl fileNo, "v_cspIdList", "VARCHAR(" & CStr(CInt(1 * maxAlCountryListLen)) & ")", "''"
           genVarDecl fileNo, "v_trailer", "CHAR(3)", "'...'"
           If lrtAware Then
             genVarDecl fileNo, "v_lrtOid", g_dbtOid, "0"
           End If

           If lrtAware Then
             genProcSectionHeader fileNo, "allow for NULL being passed for 'lrtOid_in'"
             Print #fileNo, addTab(1); "SET v_lrtOid = COALESCE(lrtOid_in, 0);"
           End If

           genProcSectionHeader fileNo, "loop over relationship records related to the given OID"
           Print #fileNo, addTab(1); "FOR cspLoop AS"

           If isWorkDataPool Then
             Print #fileNo, addTab(2); "WITH"
             Print #fileNo, addTab(3); "V_Pub"
             Print #fileNo, addTab(2); "("
             If includeDeletedPubRecords Then
               Print #fileNo, addTab(3); "csp_oid,"
               Print #fileNo, addTab(3); "seqNo"
             Else
               Print #fileNo, addTab(3); "csp_oid"
             End If
             Print #fileNo, addTab(2); ")"
             Print #fileNo, addTab(2); "AS"
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); "SELECT"
             If includeDeletedPubRecords Then
               Print #fileNo, addTab(4); "CXR.CSP_OID,"
               Print #fileNo, addTab(4); "ROWNUMBER() OVER (PARTITION BY CXR.CSP_OID ORDER BY PUB."; g_anIsDeleted; " DESC)"
             Else
               Print #fileNo, addTab(4); "CXR.CSP_OID"
             End If
             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); qualTabName; " PUB"
             Print #fileNo, addTab(3); "INNER JOIN"
             Print #fileNo, addTab(4); g_qualTabNameCountryIdXRef; " CXR"
             Print #fileNo, addTab(3); "ON"
             Print #fileNo, addTab(4); "PUB.ACLACL_OID = CXR.CIL_OID"
             Print #fileNo, addTab(3); "WHERE"
             Print #fileNo, addTab(4); "(PUB."; g_anOid; " = oid_in)"

             If Not includeDeletedPubRecords Then
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "(PUB."; g_anIsDeleted; " = 0)"
             End If

             If lrtAware Then
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "("
               Print #fileNo, addTab(5); "(PUB."; g_anInLrt; " IS NULL)"
               Print #fileNo, addTab(6); "OR"
               Print #fileNo, addTab(5); "(PUB."; g_anInLrt; " <> v_lrtOid)"
               Print #fileNo, addTab(4); ")"
               Print #fileNo, addTab(2); "),"
               Print #fileNo, addTab(3); "V_Priv"
               Print #fileNo, addTab(2); "("
               If includeDeletedPrivRecords Then
                 Print #fileNo, addTab(3); "csp_oid,"
                 Print #fileNo, addTab(3); "seqNo"
               Else
                 Print #fileNo, addTab(3); "csp_oid"
               End If
               Print #fileNo, addTab(2); ")"
               Print #fileNo, addTab(2); "AS"
               Print #fileNo, addTab(2); "("
               Print #fileNo, addTab(3); "SELECT"
               If includeDeletedPrivRecords Then
                 Print #fileNo, addTab(4); "CXR.CSP_OID,"
                 Print #fileNo, addTab(4); "ROWNUMBER() OVER (PARTITION BY CXR.CSP_OID ORDER BY PRIV."; g_anLrtState; " DESC)"
               Else
                 Print #fileNo, addTab(4); "CXR.CSP_OID"
               End If
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameLrt; " PRIV"
               Print #fileNo, addTab(3); "INNER JOIN"
               Print #fileNo, addTab(4); g_qualTabNameCountryIdXRef; " CXR"
               Print #fileNo, addTab(3); "ON"
               Print #fileNo, addTab(4); "PRIV.ACLACL_OID = CXR.CIL_OID"
               Print #fileNo, addTab(3); "WHERE"
               Print #fileNo, addTab(4); "(PRIV."; g_anOid; " = oid_in)"
               If Not includeDeletedPrivRecords Then
                 Print #fileNo, addTab(5); "AND"
                 Print #fileNo, addTab(4); "(PRIV."; g_anLrtState; " <> "; CStr(lrtStatusDeleted); ")"
               End If
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "(PRIV."; g_anInLrt; " = v_lrtOid)"
             End If

             Print #fileNo, addTab(2); "),"
             Print #fileNo, addTab(3); "V_Csp_Oid"
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); "csp_oid"
             Print #fileNo, addTab(2); ")"
             Print #fileNo, addTab(2); "AS"
             Print #fileNo, addTab(2); "("

             If includeDeletedPubRecords Then
               Print #fileNo, addTab(3); "SELECT csp_oid FROM V_Pub WHERE seqNo = 1"
             Else
               Print #fileNo, addTab(3); "SELECT csp_oid FROM V_Pub"
             End If

             If lrtAware Then
               Print #fileNo, addTab(4); "UNION"
               If includeDeletedPrivRecords Then
                 Print #fileNo, addTab(3); "SELECT csp_oid FROM V_Priv WHERE seqNo = 1"
               Else
                 Print #fileNo, addTab(3); "SELECT csp_oid FROM V_Priv"
               End If
             End If

             Print #fileNo, addTab(2); ")"
             Print #fileNo, addTab(2); "SELECT"
             Print #fileNo, addTab(3); "C.ID AS c_id"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); "V_Csp_Oid A"
             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); g_qualTabNameCountrySpec; " C"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "A.csp_oid = C."; g_anOid
           Else
             Print #fileNo, addTab(2); "SELECT"
             Print #fileNo, addTab(3); "C.ID AS c_id"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); qualTabName; " A"
             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); qualTabNameCountryIdXRef; " CXR"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "A.ACLACL_OID = CXR.CIL_OID"
             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); qualTabNameCountrySpec; " C"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "C."; g_anOid; " = CXR.CSP_OID"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "A."; g_anOid; " = oid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CXR.CSP_OID = C."; g_anOid
           End If

           Print #fileNo, addTab(2); "ORDER BY"
           Print #fileNo, addTab(3); "C.ID"
           If use3DigitIds Then
             Print #fileNo, addTab(2); "FETCH FIRST 256 ROWS ONLY"
           End If
           Print #fileNo, addTab(1); "DO"
           If use3DigitIds Then
             Print #fileNo, addTab(2); "SET v_cspIdList = v_cspIdList || (CASE v_cspIdList WHEN '' THEN '' ELSE ',' END) || RIGHT('000' || RTRIM(CAST(c_id AS CHAR(5))), 3);"
           Else
             Print #fileNo, addTab(2); "SET v_cspIdList = v_cspIdList || (CASE v_cspIdList WHEN '' THEN '' ELSE ',' END) || RTRIM(CAST(c_id AS CHAR(5)));"
           End If
           Print #fileNo, addTab(1); "END FOR;"

           genProcSectionHeader fileNo, "post-process result string"
           Print #fileNo, addTab(1); "IF v_cspIdList = '' THEN"
           Print #fileNo, addTab(2); "SET v_cspIdList = NULL;"
           Print #fileNo, addTab(1); "ELSE"
           Print #fileNo, addTab(2); "IF LENGTH(v_cspIdList) > maxLength_in THEN"
           Print #fileNo, addTab(3); "RETURN LEFT(v_cspIdList, (maxLength_in - LENGTH(v_trailer))) || v_trailer;"
           Print #fileNo, addTab(2); "ELSE"
           Print #fileNo, addTab(3); "RETURN v_cspIdList;"
           Print #fileNo, addTab(2); "END IF;"
           Print #fileNo, addTab(1); "END IF;"
           Print #fileNo,
           Print #fileNo, addTab(1); "RETURN v_cspIdList;"
           Print #fileNo, addTab(0); "END"
           Print #fileNo, addTab(0); gc_sqlCmdDelim
         Next l

         qualFuncName = genQualFuncName(g_relationships.descriptors(thisRelIndex).sectionIndex, funcName & udfSuffixName, ddlType, thisOrgIndex, thisPoolIndex)

         printSectionHeader "Function for concatenating CountrySpec-IDs for ACM-Relationship """ & _
                             g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """" & IIf(use3DigitIds, " (use 3-digit IDs" & IIf(lrtAware, " / LRT-aware", "") & ")", ""), fileNo

         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE FUNCTION"
         Print #fileNo, addTab(1); qualFuncName
         Print #fileNo, addTab(0); "("
         genProcParm fileNo, "", "oid_in", g_dbtOid, lrtAware, "OID of '" & targetSectionName & "." & targetClassName & "'-object"
         If lrtAware Then
           genProcParm fileNo, "", "lrtOid_in", g_dbtOid, False, "OID of the LRT used for reference"
         End If
         Print #fileNo, addTab(0); ")"
 
         Print #fileNo, addTab(0); "RETURNS"
         Print #fileNo, addTab(1); "VARCHAR("; CStr(maxAlCountryListLen); ")"
         Print #fileNo, addTab(0); "LANGUAGE SQL"
         Print #fileNo, addTab(0); "DETERMINISTIC"
         Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
         Print #fileNo, addTab(0); "READS SQL DATA"
         Print #fileNo, addTab(0); "RETURN"
         Print #fileNo, addTab(1); qualFuncName; "(oid_in"; IIf(lrtAware, ", lrtOid_in", ""); ", "; CStr(maxResLen); ")"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       Next k
     Next i

     ' ####################################################################################################################
     ' #    UDF to decide whether a record is valid for a given Organization
     ' ####################################################################################################################
 
     If Not isDisallowedCountries Then
       Dim qualTabNameCountryGroupElem As String
       qualTabNameCountryGroupElem = genQualTabNameByRelIndex(g_relIndexCountryGroupElement, ddlType, thisOrgIndex, thisPoolIndex)
 
       Dim qualTabNameDisAllowed As String
       qualTabNameDisAllowed = genQualTabNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "Dis")
 
       qualFuncName = genQualFuncName(g_relationships.descriptors(thisRelIndex).sectionIndex, "HASALCNTRY", ddlType, thisOrgIndex, thisPoolIndex)

       printSectionHeader "Function deciding whether a """ & targetSectionName & "." & targetClassName & """ is valid for a given Organization", fileNo

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE FUNCTION"
       Print #fileNo, addTab(1); qualFuncName
       Print #fileNo, addTab(0); "("
       If targetIsGenericAspect Then
           genProcParm fileNo, "", "oid_in", g_dbtOid, True, "OID of an '" & g_classes.descriptors(g_classIndexGenericAspect).sectionName & "." & g_classes.descriptors(g_classIndexGenericAspect).className & "'-object"
           genProcParm fileNo, "", "classId_in", g_dbtEntityId, True, "CLASSID of the '" & g_classes.descriptors(g_classIndexGenericAspect).sectionName & "." & g_classes.descriptors(g_classIndexGenericAspect).className & "'-object"
       Else
         genProcParm fileNo, "", "oid_in", g_dbtOid, True, "OID of '" & targetSectionName & "." & targetClassName & "'-object"
       End If
         genProcParm fileNo, "", "orgOid_in", g_dbtOid, False, "OID of '" & g_classes.descriptors(g_classIndexOrganization).sectionName & "." & g_classes.descriptors(g_classIndexOrganization).className & "'-object"
 
       Print #fileNo, addTab(0); ")"

       Print #fileNo, addTab(0); "RETURNS"
       Print #fileNo, addTab(1); g_dbtBoolean
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "DETERMINISTIC"
       Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
       Print #fileNo, addTab(0); "READS SQL DATA"
       Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
       If targetIsGenericAspect Then
         genProcSectionHeader fileNo, "special consideration for SR0Validity"
         Print #fileNo, addTab(1); "IF classId_in = '"; g_classes.descriptors(g_classIndexSr0Validity).classIdStr; "' THEN"
         Print #fileNo, addTab(2); "RETURN 1;"
         Print #fileNo, addTab(1); "END IF;"
       End If

       genProcSectionHeader fileNo, "check ALLOWEDCOUNTRIES-association"""
       Print #fileNo, addTab(1); "FOR tabLoop AS"
       Print #fileNo, addTab(2); "WITH"
       Print #fileNo, addTab(3); "V_AllowedCountrySpec"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "countrySpecOid,"
       Print #fileNo, addTab(3); "classId"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "AS"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "CY."; g_anOid; ","
       Print #fileNo, addTab(4); "CY."; g_anCid; ""
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); qualTabName; " A"
       Print #fileNo, addTab(3); "INNER JOIN"
       Print #fileNo, addTab(4); qualTabNameCountryIdXRef; " CXR"
       Print #fileNo, addTab(3); "ON"
       Print #fileNo, addTab(4); "A.ACLACL_OID = CXR.CIL_OID"
       Print #fileNo, addTab(3); "INNER JOIN"
       Print #fileNo, addTab(4); qualTabNameCountrySpec; " CY"
       Print #fileNo, addTab(3); "ON"
       Print #fileNo, addTab(4); "CY."; g_anOid; " = CXR.CSP_OID"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "A."; g_anOid; " = oid_in"
       Print #fileNo, addTab(2); "),"
       Print #fileNo, addTab(3); "V_AllowedCountrySpec_Expanded"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "countrySpecOid,"
       Print #fileNo, addTab(3); "classId,"
       Print #fileNo, addTab(3); "level"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "AS"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "AC.countrySpecOid,"
       Print #fileNo, addTab(5); "AC.classId,"
       Print #fileNo, addTab(5); "1"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); "V_AllowedCountrySpec AC"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "UNION ALL"
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "GE.CSP_OID,"
       Print #fileNo, addTab(5); "CY."; g_anCid; ","
       Print #fileNo, addTab(5); "AC.level + 1"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); "V_AllowedCountrySpec_Expanded AC,"
       Print #fileNo, addTab(5); qualTabNameCountrySpec; " CY,"
       Print #fileNo, addTab(5); qualTabNameCountryGroupElem; " GE"
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "AC.countrySpecOid = GE.CNG_OID"
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "GE.CSP_OID = CY."; g_anOid
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "AC.level < 100"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(2); "),"
       Print #fileNo, addTab(3); "V_AllowedCountrySpec_Expanded_ByOrg"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "countrySpecOid,"
       Print #fileNo, addTab(3); "classId,"
       Print #fileNo, addTab(3); "orgOid"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "AS"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "AC.countrySpecOid,"
       Print #fileNo, addTab(4); "AC.classId,"
       Print #fileNo, addTab(4); "OC.ORG_OID"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "V_AllowedCountrySpec_Expanded AC,"
       Print #fileNo, addTab(4); qualTabNameOrgManagesCountry; " OC"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "AC.classId = '02002'"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "AC.countrySpecOid = OC.CNT_OID"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "OC.ORG_OID = orgOid_in"
       Print #fileNo, addTab(2); "),"
       Print #fileNo, addTab(3); "V_DisallowedCountrySpec_Expanded"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "countrySpecOid,"
       Print #fileNo, addTab(3); "level"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "AS"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "CXR.CSP_OID,"
       Print #fileNo, addTab(5); "1"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); qualTabName; " A"
       Print #fileNo, addTab(4); "INNER JOIN"
       Print #fileNo, addTab(5); qualTabNameCountryIdXRef; " CXR"
       Print #fileNo, addTab(4); "ON"
       Print #fileNo, addTab(5); "A.DCLDCL_OID = CXR.CIL_OID"
       Print #fileNo, addTab(4); "INNER JOIN"
       Print #fileNo, addTab(5); qualTabNameCountrySpec; " CY"
       Print #fileNo, addTab(4); "ON"
       Print #fileNo, addTab(5); "CY."; g_anOid; " = CXR.CSP_OID"
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "A."; g_anOid; " = oid_in"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "UNION ALL"
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "GE.CSP_OID,"
       Print #fileNo, addTab(5); "AC.level + 1"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); "V_DisallowedCountrySpec_Expanded AC,"
       Print #fileNo, addTab(5); qualTabNameCountrySpec; " CY,"
       Print #fileNo, addTab(5); qualTabNameCountryGroupElem; " GE"
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "AC.countrySpecOid = GE.CNG_OID"
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "GE.CSP_OID = CY."; g_anOid
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "AC.level < 50"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "V.countrySpecOid"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); "V_AllowedCountrySpec_Expanded_ByOrg V"
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "classId = '02002'"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "V.countrySpecOid NOT IN ("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "VD.countrySpecOid"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); "V_DisallowedCountrySpec_Expanded VD"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(2); "FETCH FIRST 1 ROWS ONLY"
       Print #fileNo, addTab(1); "DO"
       Print #fileNo, addTab(2); "RETURN 1;"
       Print #fileNo, addTab(1); "END FOR;"
       Print #fileNo,
       Print #fileNo, addTab(1); "RETURN 0;"
       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     End If
 End Sub
 
 
 Private Sub genAllowedCountriesFunction( _
   thisRelIndex As Integer, _
   fileNo As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
     Dim isDisallowedCountries As Boolean
     Dim oidAttrName As String
     Dim targetClassName As String
     Dim targetSectionName As String
     Dim targetIsGenericAspect As Boolean

     Dim qualTabNameOrgManagesCountry As String
     qualTabNameOrgManagesCountry = genQualTabNameByRelIndex(g_relIndexOrgManagesCountry, ddlType, thisOrgIndex, thisPoolIndex)
     Dim qualTabNameCountrySpec As String
     qualTabNameCountrySpec = genQualTabNameByClassIndex(g_classIndexCountrySpec, ddlType, thisOrgIndex, thisPoolIndex)

     If g_relationships.descriptors(thisRelIndex).isDisallowedCountries <> ernmNone Or g_relationships.descriptors(thisRelIndex).isAllowedCountries <> ernmNone Then
       If g_relationships.descriptors(thisRelIndex).isDisallowedCountries = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountries = ernmLeft Then
         oidAttrName = g_relationships.descriptors(thisRelIndex).rightFkColName(ddlType)
       Else
         oidAttrName = g_relationships.descriptors(thisRelIndex).leftFkColName(ddlType)
       End If
         targetClassName = g_classes.descriptors(IIf(g_relationships.descriptors(thisRelIndex).isDisallowedCountries = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountries = ernmLeft, g_relationships.descriptors(thisRelIndex).rightEntityIndex, g_relationships.descriptors(thisRelIndex).leftEntityIndex)).className
         targetSectionName = g_classes.descriptors(IIf(g_relationships.descriptors(thisRelIndex).isDisallowedCountries = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountries = ernmLeft, g_relationships.descriptors(thisRelIndex).rightEntityIndex, g_relationships.descriptors(thisRelIndex).leftEntityIndex)).sectionName
         targetIsGenericAspect = (g_classes.descriptors(g_classes.descriptors(IIf(g_relationships.descriptors(thisRelIndex).isDisallowedCountries = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountries = ernmLeft, g_relationships.descriptors(thisRelIndex).rightEntityIndex, g_relationships.descriptors(thisRelIndex).leftEntityIndex)).orMappingSuperClassIndex).classIndex = g_classIndexGenericAspect)
       isDisallowedCountries = True
     End If
     If g_relationships.descriptors(thisRelIndex).isAllowedCountries <> ernmNone Then
       If g_relationships.descriptors(thisRelIndex).isAllowedCountries = ernmLeft Then
         oidAttrName = g_relationships.descriptors(thisRelIndex).rightFkColName(ddlType)
       Else
         oidAttrName = g_relationships.descriptors(thisRelIndex).leftFkColName(ddlType)
       End If
       isDisallowedCountries = False
     End If

     Dim poolSupportLrt As Boolean
     poolSupportLrt = False
     If thisPoolIndex > 0 Then
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
     End If

     Dim isWorkDataPool As Boolean
     isWorkDataPool = (thisPoolIndex = g_workDataPoolIndex)

     Dim qualTabName As String
     qualTabName = genQualTabNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex)
     Dim qualTabNameLrt As String
     qualTabNameLrt = genQualTabNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, True)

     Dim qualFuncName As String
     Dim use3DigitIds As Boolean
     Dim funcName As String
     Dim maxResLen As Integer

     Dim i As Integer
     For i = 1 To 2
       use3DigitIds = (i = 2)
       If isDisallowedCountries Then
         funcName = IIf(use3DigitIds, udfnDisallowedCountry2Str0, udfnDisallowedCountry2Str)
         maxResLen = gc_disallowedCountriesMaxLength
       Else
         funcName = IIf(use3DigitIds, udfnAllowedCountry2Str0, udfnAllowedCountry2Str)
         maxResLen = gc_allowedCountriesMaxLength
       End If

       Dim lrtAware As Boolean
       Dim includeDeletedPrivRecords As Boolean
       Dim includeDeletedPubRecords As Boolean
       Dim udfSuffixName As String
       Dim k As Integer
       For k = 1 To IIf((ddlType = edtLdm Or poolSupportLrt) And use3DigitIds, 4, 1)
         lrtAware = ((k = 2) Or (k = 3))

         includeDeletedPrivRecords = False
         includeDeletedPubRecords = False
         udfSuffixName = ""
         If k = 3 Then
           includeDeletedPrivRecords = True
           udfSuffixName = "_D"
         ElseIf k = 4 Then
           includeDeletedPubRecords = True
           udfSuffixName = "_D"
         End If

         Dim targetSectionIndex As Integer
         Dim l As Integer
         For l = 1 To IIf(use3DigitIds And targetIsGenericAspect And k = 1, 2, 1)
           targetSectionIndex = IIf(l = 1, g_relationships.descriptors(thisRelIndex).sectionIndex, g_sectionIndexAliasLrt)

           qualFuncName = genQualFuncName(targetSectionIndex, funcName & udfSuffixName, ddlType, thisOrgIndex, thisPoolIndex)

           printSectionHeader "Function for concatenating CountrySpec-IDs for ACM-Relationship """ & _
                               g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """" & IIf(use3DigitIds, " (use 3-digit IDs" & _
                               IIf(lrtAware, " / LRT-aware", "") & _
                               IIf(includeDeletedPrivRecords Or includeDeletedPubRecords, " / for deleted records", "") & ")", ""), fileNo

           Print #fileNo,
           Print #fileNo, addTab(0); "CREATE FUNCTION"
           Print #fileNo, addTab(1); qualFuncName
           Print #fileNo, addTab(0); "("
           genProcParm fileNo, "", "oid_in", g_dbtOid, True, "OID of '" & targetSectionName & "." & targetClassName & "'-object"
           If lrtAware Then
             genProcParm fileNo, "", "lrtOid_in", g_dbtOid, True, "OID of the LRT used for reference"
           End If
           genProcParm fileNo, "", "maxLength_in", "INTEGER", False, "maximum length of string returned"
           Print #fileNo, addTab(0); ")"

           Print #fileNo, addTab(0); "RETURNS"
           Print #fileNo, addTab(1); "VARCHAR("; CStr(maxAlCountryListLen); ")"
           Print #fileNo, addTab(0); "LANGUAGE SQL"
           Print #fileNo, addTab(0); "DETERMINISTIC"
           Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
           Print #fileNo, addTab(0); "READS SQL DATA"
           Print #fileNo, addTab(0); "BEGIN ATOMIC"

           genProcSectionHeader fileNo, "declare variables", , True
           genVarDecl fileNo, "v_cspIdList", "VARCHAR(" & CStr(CInt(1 * maxAlCountryListLen)) & ")", "''"
           genVarDecl fileNo, "v_trailer", "CHAR(3)", "'...'"
           If lrtAware Then
             genVarDecl fileNo, "v_lrtOid", g_dbtOid, "0"
           End If

           If lrtAware Then
             genProcSectionHeader fileNo, "allow for NULL being passed for 'lrtOid_in'"
             Print #fileNo, addTab(1); "SET v_lrtOid = COALESCE(lrtOid_in, 0);"
           End If

           genProcSectionHeader fileNo, "loop over relationship records related to the given OID"
           Print #fileNo, addTab(1); "FOR cspLoop AS"

           If isWorkDataPool Then
             Print #fileNo, addTab(2); "WITH"
             Print #fileNo, addTab(3); "V_Pub"
             Print #fileNo, addTab(2); "("
             If includeDeletedPubRecords Then
               Print #fileNo, addTab(3); "csp_oid,"
               Print #fileNo, addTab(3); "seqNo"
             Else
               Print #fileNo, addTab(3); "csp_oid"
             End If
             Print #fileNo, addTab(2); ")"
             Print #fileNo, addTab(2); "AS"
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); "SELECT"
             If includeDeletedPubRecords Then
               Print #fileNo, addTab(4); "PUB.CSP_OID,"
               Print #fileNo, addTab(4); "ROWNUMBER() OVER (PARTITION BY PUB.CSP_OID ORDER BY PUB."; g_anIsDeleted; " DESC)"
             Else
               Print #fileNo, addTab(4); "PUB.CSP_OID"
             End If
             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); qualTabName; " PUB"
             Print #fileNo, addTab(3); "WHERE"
             Print #fileNo, addTab(4); "(PUB."; oidAttrName; " = oid_in)"

             If Not includeDeletedPubRecords Then
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "(PUB."; g_anIsDeleted; " = 0)"
             End If

             If lrtAware Then
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "("
               Print #fileNo, addTab(5); "(PUB."; g_anInLrt; " IS NULL)"
               Print #fileNo, addTab(6); "OR"
               Print #fileNo, addTab(5); "(PUB."; g_anInLrt; " <> v_lrtOid)"
               Print #fileNo, addTab(4); ")"
               Print #fileNo, addTab(2); "),"
               Print #fileNo, addTab(3); "V_Priv"
               Print #fileNo, addTab(2); "("
               If includeDeletedPrivRecords Then
                 Print #fileNo, addTab(3); "csp_oid,"
                 Print #fileNo, addTab(3); "seqNo"
               Else
                 Print #fileNo, addTab(3); "csp_oid"
               End If
               Print #fileNo, addTab(2); ")"
               Print #fileNo, addTab(2); "AS"
               Print #fileNo, addTab(2); "("
               Print #fileNo, addTab(3); "SELECT"
               If includeDeletedPrivRecords Then
                 Print #fileNo, addTab(4); "PRIV.CSP_OID,"
                 Print #fileNo, addTab(4); "ROWNUMBER() OVER (PARTITION BY PRIV.CSP_OID ORDER BY PRIV."; g_anLrtState; " DESC)"
               Else
                 Print #fileNo, addTab(4); "PRIV.CSP_OID"
               End If
               Print #fileNo, addTab(3); "FROM"
               Print #fileNo, addTab(4); qualTabNameLrt; " PRIV"
               Print #fileNo, addTab(3); "WHERE"
               Print #fileNo, addTab(4); "(PRIV."; oidAttrName; " = oid_in)"
               If Not includeDeletedPrivRecords Then
                 Print #fileNo, addTab(5); "AND"
                 Print #fileNo, addTab(4); "(PRIV."; g_anLrtState; " <> "; CStr(lrtStatusDeleted); ")"
               End If
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "(PRIV."; g_anInLrt; " = v_lrtOid)"
             End If

             Print #fileNo, addTab(2); "),"
             Print #fileNo, addTab(3); "V_Csp_Oid"
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); "csp_oid"
             Print #fileNo, addTab(2); ")"
             Print #fileNo, addTab(2); "AS"
             Print #fileNo, addTab(2); "("

             If includeDeletedPubRecords Then
               Print #fileNo, addTab(3); "SELECT csp_oid FROM V_Pub WHERE seqNo = 1"
             Else
               Print #fileNo, addTab(3); "SELECT csp_oid FROM V_Pub"
             End If

             If lrtAware Then
               Print #fileNo, addTab(4); "UNION"
               If includeDeletedPrivRecords Then
                 Print #fileNo, addTab(3); "SELECT csp_oid FROM V_Priv WHERE seqNo = 1"
               Else
                 Print #fileNo, addTab(3); "SELECT csp_oid FROM V_Priv"
               End If
             End If

             Print #fileNo, addTab(2); ")"
             Print #fileNo, addTab(2); "SELECT"
             Print #fileNo, addTab(3); "C.ID AS c_id"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); "V_Csp_Oid A,"
             Print #fileNo, addTab(3); qualTabNameCountrySpec; " C"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "A.csp_oid = C."; g_anOid
           Else
             Print #fileNo, addTab(2); "SELECT"
             Print #fileNo, addTab(3); "C.ID AS c_id"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); qualTabName; " A,"
             Print #fileNo, addTab(3); qualTabNameCountrySpec; " C"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "A."; oidAttrName; " = oid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "A.CSP_OID = C."; g_anOid
           End If

           Print #fileNo, addTab(2); "ORDER BY"
           Print #fileNo, addTab(3); "C.ID"
           If use3DigitIds Then
             Print #fileNo, addTab(2); "FETCH FIRST 256 ROWS ONLY"
           End If
           Print #fileNo, addTab(1); "DO"
           If use3DigitIds Then
             Print #fileNo, addTab(2); "SET v_cspIdList = v_cspIdList || (CASE v_cspIdList WHEN '' THEN '' ELSE ',' END) || RIGHT('000' || RTRIM(CAST(c_id AS CHAR(5))), 3);"
           Else
             Print #fileNo, addTab(2); "SET v_cspIdList = v_cspIdList || (CASE v_cspIdList WHEN '' THEN '' ELSE ',' END) || RTRIM(CAST(c_id AS CHAR(5)));"
           End If
           Print #fileNo, addTab(1); "END FOR;"

           genProcSectionHeader fileNo, "post-process result string"
           Print #fileNo, addTab(1); "IF v_cspIdList = '' THEN"
           Print #fileNo, addTab(2); "SET v_cspIdList = NULL;"
           Print #fileNo, addTab(1); "ELSE"
           Print #fileNo, addTab(2); "IF LENGTH(v_cspIdList) > maxLength_in THEN"
           Print #fileNo, addTab(3); "RETURN LEFT(v_cspIdList, (maxLength_in - LENGTH(v_trailer))) || v_trailer;"
           Print #fileNo, addTab(2); "ELSE"
           Print #fileNo, addTab(3); "RETURN v_cspIdList;"
           Print #fileNo, addTab(2); "END IF;"
           Print #fileNo, addTab(1); "END IF;"
           Print #fileNo,
           Print #fileNo, addTab(1); "RETURN v_cspIdList;"
           Print #fileNo, addTab(0); "END"
           Print #fileNo, addTab(0); gc_sqlCmdDelim
         Next l

         qualFuncName = genQualFuncName(g_relationships.descriptors(thisRelIndex).sectionIndex, funcName & udfSuffixName, ddlType, thisOrgIndex, thisPoolIndex)

         printSectionHeader "Function for concatenating CountrySpec-IDs for ACM-Relationship """ & _
                             g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """" & IIf(use3DigitIds, " (use 3-digit IDs" & IIf(lrtAware, " / LRT-aware", "") & ")", ""), fileNo

         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE FUNCTION"
         Print #fileNo, addTab(1); qualFuncName
         Print #fileNo, addTab(0); "("
         genProcParm fileNo, "", "oid_in", g_dbtOid, lrtAware, "OID of '" & targetSectionName & "." & targetClassName & "'-object"
         If lrtAware Then
           genProcParm fileNo, "", "lrtOid_in", g_dbtOid, False, "OID of the LRT used for reference"
         End If
         Print #fileNo, addTab(0); ")"
 
         Print #fileNo, addTab(0); "RETURNS"
         Print #fileNo, addTab(1); "VARCHAR("; CStr(maxAlCountryListLen); ")"
         Print #fileNo, addTab(0); "LANGUAGE SQL"
         Print #fileNo, addTab(0); "DETERMINISTIC"
         Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
         Print #fileNo, addTab(0); "READS SQL DATA"
         Print #fileNo, addTab(0); "RETURN"
         Print #fileNo, addTab(1); qualFuncName; "(oid_in"; IIf(lrtAware, ", lrtOid_in", ""); ", "; CStr(maxResLen); ")"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       Next k
     Next i

     ' ####################################################################################################################
     ' #    UDF to decide whether a record is valid for a given Organization
     ' ####################################################################################################################
 
     If Not isDisallowedCountries Then
       Dim qualTabNameCountryGroupElem As String
       qualTabNameCountryGroupElem = genQualTabNameByRelIndex(g_relIndexCountryGroupElement, ddlType, thisOrgIndex, thisPoolIndex)
 
       Dim qualTabNameDisAllowed As String
       qualTabNameDisAllowed = genQualTabNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "Dis")
 
       qualFuncName = genQualFuncName(g_relationships.descriptors(thisRelIndex).sectionIndex, "HASALCNTRY", ddlType, thisOrgIndex, thisPoolIndex)

       printSectionHeader "Function deciding whether a """ & targetSectionName & "." & targetClassName & """ is valid for a given Organization", fileNo

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE FUNCTION"
       Print #fileNo, addTab(1); qualFuncName
       Print #fileNo, addTab(0); "("
       If targetIsGenericAspect Then
           genProcParm fileNo, "", "oid_in", g_dbtOid, True, "OID of an '" & g_classes.descriptors(g_classIndexGenericAspect).sectionName & "." & g_classes.descriptors(g_classIndexGenericAspect).className & "'-object"
           genProcParm fileNo, "", "classId_in", g_dbtEntityId, True, "CLASSID of the '" & g_classes.descriptors(g_classIndexGenericAspect).sectionName & "." & g_classes.descriptors(g_classIndexGenericAspect).className & "'-object"
       Else
         genProcParm fileNo, "", "oid_in", g_dbtOid, True, "OID of '" & targetSectionName & "." & targetClassName & "'-object"
       End If
         genProcParm fileNo, "", "orgOid_in", g_dbtOid, False, "OID of '" & g_classes.descriptors(g_classIndexOrganization).sectionName & "." & g_classes.descriptors(g_classIndexOrganization).className & "'-object"
 
       Print #fileNo, addTab(0); ")"

       Print #fileNo, addTab(0); "RETURNS"
       Print #fileNo, addTab(1); g_dbtBoolean
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "DETERMINISTIC"
       Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
       Print #fileNo, addTab(0); "READS SQL DATA"
       Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
       If targetIsGenericAspect Then
         genProcSectionHeader fileNo, "special consideration for SR0Validity"
         Print #fileNo, addTab(1); "IF classId_in = '"; g_classes.descriptors(g_classIndexSr0Validity).classIdStr; "' THEN"
         Print #fileNo, addTab(2); "RETURN 1;"
         Print #fileNo, addTab(1); "END IF;"
       End If

       genProcSectionHeader fileNo, "check ALLOWEDCOUNTRIES-association"""
       Print #fileNo, addTab(1); "FOR tabLoop AS"
       Print #fileNo, addTab(2); "WITH"
       Print #fileNo, addTab(3); "V_AllowedCountrySpec"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "countrySpecOid,"
       Print #fileNo, addTab(3); "classId"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "AS"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "CY."; g_anOid; ","
       Print #fileNo, addTab(4); "CY."; g_anCid; ""
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); qualTabNameCountrySpec; " CY,"
       Print #fileNo, addTab(4); qualTabName; " AC"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "AC.CSP_OID = CY."; g_anOid
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "AC."; oidAttrName; " = oid_in"
       Print #fileNo, addTab(2); "),"
       Print #fileNo, addTab(3); "V_AllowedCountrySpec_Expanded"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "countrySpecOid,"
       Print #fileNo, addTab(3); "classId,"
       Print #fileNo, addTab(3); "level"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "AS"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "AC.countrySpecOid,"
       Print #fileNo, addTab(5); "AC.classId,"
       Print #fileNo, addTab(5); "1"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); "V_AllowedCountrySpec AC"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "UNION ALL"
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "GE.CSP_OID,"
       Print #fileNo, addTab(5); "CY."; g_anCid; ","
       Print #fileNo, addTab(5); "AC.level + 1"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); "V_AllowedCountrySpec_Expanded  AC,"
       Print #fileNo, addTab(5); qualTabNameCountrySpec; " CY,"
       Print #fileNo, addTab(5); qualTabNameCountryGroupElem; " GE"
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "AC.countrySpecOid = GE.CNG_OID"
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "GE.CSP_OID = CY."; g_anOid
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "AC.level < 100"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(2); "),"
       Print #fileNo, addTab(3); "V_AllowedCountrySpec_Expanded_ByOrg"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "countrySpecOid,"
       Print #fileNo, addTab(3); "classId,"
       Print #fileNo, addTab(3); "orgOid"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "AS"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "AC.countrySpecOid,"
       Print #fileNo, addTab(4); "AC.classId,"
       Print #fileNo, addTab(4); "OC.ORG_OID"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "V_AllowedCountrySpec_Expanded AC,"
       Print #fileNo, addTab(4); qualTabNameOrgManagesCountry; " OC"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "AC.classId = '02002'"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "AC.countrySpecOid = OC.CNT_OID"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "OC.ORG_OID = orgOid_in"
       Print #fileNo, addTab(2); "),"
       Print #fileNo, addTab(3); "V_DisallowedCountrySpec_Expanded"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "countrySpecOid,"
       Print #fileNo, addTab(3); "level"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "AS"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "AC.CSP_OID,"
       Print #fileNo, addTab(5); "1"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); qualTabNameDisAllowed; " AC"
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "AC."; oidAttrName; " = oid_in"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "UNION ALL"
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "GE.CSP_OID,"
       Print #fileNo, addTab(5); "AC.level + 1"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); "V_DisallowedCountrySpec_Expanded AC,"
       Print #fileNo, addTab(5); qualTabNameCountrySpec; " CY,"
       Print #fileNo, addTab(5); qualTabNameCountryGroupElem; " GE"
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "AC.countrySpecOid = GE.CNG_OID"
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "GE.CSP_OID = CY."; g_anOid
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "AC.level < 50"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "V.countrySpecOid"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); "V_AllowedCountrySpec_Expanded_ByOrg V"
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "classId = '02002'"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "V.countrySpecOid NOT IN ("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "VD.countrySpecOid"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); "V_DisallowedCountrySpec_Expanded VD"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(2); "FETCH FIRST 1 ROWS ONLY"
       Print #fileNo, addTab(1); "DO"
       Print #fileNo, addTab(2); "RETURN 1;"
       Print #fileNo, addTab(1); "END FOR;"
       Print #fileNo,
       Print #fileNo, addTab(1); "RETURN 0;"
       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     End If
 End Sub
 
 
 Private Sub genAllowedCountriesView( _
   thisRelIndex As Integer, _
   fileNo As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
     Dim isDisallowedCountries As Boolean
     Dim oidAttrName As String

     If g_relationships.descriptors(thisRelIndex).isDisallowedCountries = ernmLeft Or g_relationships.descriptors(thisRelIndex).isAllowedCountries = ernmLeft Then
       oidAttrName = g_relationships.descriptors(thisRelIndex).rightFkColName(ddlType)
     ElseIf g_relationships.descriptors(thisRelIndex).isDisallowedCountries = ernmRight Or g_relationships.descriptors(thisRelIndex).isAllowedCountries = ernmRight Then
       oidAttrName = g_relationships.descriptors(thisRelIndex).leftFkColName(ddlType)
     End If

     isDisallowedCountries = g_relationships.descriptors(thisRelIndex).isDisallowedCountries <> ernmNone

     Dim qualViewName As String
     qualViewName = genQualViewNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , , IIf(isDisallowedCountries, "DAC", "AC"))
 
     Dim qualTabName As String
     qualTabName = genQualTabNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex)

     Dim qualCountrySpecTabName As String
     qualCountrySpecTabName = genQualTabNameByClassIndex(g_classIndexCountrySpec, ddlType, thisOrgIndex, thisPoolIndex)

     Dim relNameInfix As String
     relNameInfix = UCase(Left(g_relationships.descriptors(thisRelIndex).relName, 1)) & Right(g_relationships.descriptors(thisRelIndex).relName, Len(g_relationships.descriptors(thisRelIndex).relName) - 1)

     printSectionHeader "View for concatenating CountrySpec-IDs for ACM-Relationship """ & _
                         g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE VIEW"
     Print #fileNo, addTab(1); qualViewName
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); oidAttrName; ","
     Print #fileNo, addTab(1); "COUNTRYSPECS"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "AS"
     Print #fileNo, addTab(0); "WITH"
     Print #fileNo, addTab(1); "V_"; relNameInfix
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); LCase(oidAttrName); ","
     Print #fileNo, addTab(1); "countrySpecId,"
     Print #fileNo, addTab(1); "seqNo"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "AS"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "AC."; oidAttrName; ","
     Print #fileNo, addTab(2); "CS.ID,"
     Print #fileNo, addTab(2); "ROWNUMBER() OVER (PARTITION BY AC."; oidAttrName; " ORDER BY CS.ID)"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabName; " AC,"
     Print #fileNo, addTab(2); qualCountrySpecTabName; " CS"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "AC.CSP_OID = CS."; g_anOid
     Print #fileNo, addTab(0); "),"
     Print #fileNo, addTab(1); "V_"; relNameInfix; "Str"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); LCase(oidAttrName); ","
     Print #fileNo, addTab(1); "countrySpecs,"
     Print #fileNo, addTab(1); "level"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "AS"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); LCase(oidAttrName); ","
     Print #fileNo, addTab(3); "CAST(RTRIM(CAST(countrySpecId AS CHAR(5))) AS VARCHAR(500)),"
     Print #fileNo, addTab(3); "1"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V_"; relNameInfix
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "seqNo = 1"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "UNION ALL"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "AC."; LCase(oidAttrName); ","
     Print #fileNo, addTab(3); "ACS.CountrySpecs || ',' || CAST(RTRIM(CAST(AC.CountrySpecId AS CHAR(5))) AS VARCHAR(500)),"
     Print #fileNo, addTab(3); "ACS.Level + 1"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V_"; relNameInfix; "Str ACS,"
     Print #fileNo, addTab(3); "V_"; relNameInfix; "    AC"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "ACS."; oidAttrName; " = AC."; oidAttrName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "AC.seqNo = ACS.level + 1"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "ACS.level < 5000"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(0); "),"
     Print #fileNo, addTab(1); "V_"; relNameInfix; "StrMax"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); LCase(oidAttrName); ","
     Print #fileNo, addTab(1); "countrySpecs"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "AS"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); LCase(oidAttrName); ","
     Print #fileNo, addTab(2); "countrySpecs"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "V_"; relNameInfix; "Str ACS"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "NOT EXISTS"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "SELECT "; oidAttrName; " FROM V_"; g_relationships.descriptors(thisRelIndex).relName; "Str ACS2 WHERE ACS."; oidAttrName; " = ACS2."; oidAttrName; " AND ACS2.Level > ACS.Level"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "SELECT"
     Print #fileNo, addTab(1); LCase(oidAttrName); ","
     Print #fileNo, addTab(1); "countrySpecs"
     Print #fileNo, addTab(0); "FROM"
     Print #fileNo, addTab(1); "V_"; relNameInfix; "StrMax"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 ' ### ENDIF IVK ###
 Private Sub genRelationshipDdl( _
   thisRelIndex As Integer, _
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
 ' ### IF IVK ###
   Dim isDivTagged As Boolean
   Dim tabPartitionType As PartitionType
 ' ### ENDIF IVK ###

   Dim fileNo As Integer
   Dim fileNoFk As Integer
 ' ### IF IVK ###
   Dim fileNoAc As Integer
   Dim fileNoXmlF As Integer
   Dim fileNoXmlV As Integer
   Dim fileNoPs As Integer
 ' ### ENDIF IVK ###
   Dim fileNoLc As Integer
   Dim fileNoLrt As Integer
   Dim fileNoLrtView As Integer
   Dim fileNoClView As Integer
   Dim fileNoLrtSup As Integer
 ' ### IF IVK ###
   Dim fileNoSetProd As Integer
   Dim fileNoSetProdCl As Integer
   Dim fileNoFto As Integer
   Dim fileNoPsCopy As Integer, fileNoPsCopy2 As Integer
   Dim fileNoExpCopy As Integer
   Dim fileNoArc As Integer
 ' ### ENDIF IVK ###

   thisOrgDescriptorStr = genOrgId(thisOrgIndex, ddlType)

   Dim orgSetProductiveTargetPoolIndex As Integer
   orgSetProductiveTargetPoolIndex = -1
   If thisOrgIndex > 0 Then
       orgSetProductiveTargetPoolIndex = g_orgs.descriptors(thisOrgIndex).setProductiveTargetPoolIndex
   End If

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

   Dim ldmIteration As Integer

     If ddlType <> edtPdm And g_relationships.descriptors(thisRelIndex).isPdmSpecific Then
       GoTo NormalExit
     End If

 ' ### IF IVK ###
     If (g_relationships.descriptors(thisRelIndex).sectionName & "" = "" Or (IIf(supportNlForRelationships, Not g_relationships.descriptors(thisRelIndex).isNl, True) And (g_relationships.descriptors(thisRelIndex).maxLeftCardinality = 1 Or g_relationships.descriptors(thisRelIndex).maxRightCardinality = 1))) _
       And (g_relationships.descriptors(thisRelIndex).isAllowedCountriesList = ernmNone) And (g_relationships.descriptors(thisRelIndex).isDisallowedCountriesList = ernmNone) Then
       GoTo NormalExit
     End If
 ' ### ELSE IVK ###
 '   If (.sectionName & "" = "" Or (IIf(supportNlForRelationships, Not .isNl, True) And (.maxLeftCardinality = 1 Or .maxRightCardinality = 1))) Then
 '     GoTo NormalExit
 '   End If
 ' ### ENDIF IVK ###

     If g_relationships.descriptors(thisRelIndex).leftEntityType = eactRelationship Or g_relationships.descriptors(thisRelIndex).rightEntityType = eactRelationship Then
       GoTo NormalExit
     End If

     If reuseRelationships And g_relationships.descriptors(thisRelIndex).reusedRelIndex > 0 Then
       GoTo NormalExit
     End If

     If ignoreUnknownSections And (g_relationships.descriptors(thisRelIndex).sectionIndex < 0) Then
       GoTo NormalExit
     End If

     If ddlType = edtPdm Then
       If Not sectionValidForPoolAndOrg(g_relationships.descriptors(thisRelIndex).sectionIndex, thisOrgIndex, thisPoolIndex) Then
         GoTo NormalExit
       End If
     End If

     If g_relationships.descriptors(thisRelIndex).isLrtSpecific And Not g_genLrtSupport Then
       GoTo NormalExit
     End If

     If g_relationships.descriptors(thisRelIndex).specificToOrgId > 0 And ddlType = edtPdm And g_relationships.descriptors(thisRelIndex).specificToOrgId <> thisOrgId Then
       GoTo NormalExit
     End If

     If g_relationships.descriptors(thisRelIndex).specificToPool > 0 And ddlType = edtPdm And g_relationships.descriptors(thisRelIndex).specificToPool <> thisPoolId Then
       GoTo NormalExit
     End If

     If ddlType = edtPdm And thisPoolId <> -1 Then
       If Not g_relationships.descriptors(thisRelIndex).notAcmRelated And Not poolSupportAcm Then
         GoTo NormalExit
       End If
     End If

 
     ldmIteration = IIf(g_relationships.descriptors(thisRelIndex).isCommonToOrgs, ldmIterationGlobal, ldmIterationPoolSpecific)

 ' ### IF IVK ###
     If g_relationships.descriptors(thisRelIndex).isDisallowedCountries Or g_relationships.descriptors(thisRelIndex).isAllowedCountries Then
       fileNoAc = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepAllowedCountries, ddlType, thisOrgIndex, thisPoolIndex, , phaseModuleMeta, ldmIteration)
     ElseIf g_relationships.descriptors(thisRelIndex).isDisallowedCountriesList Or g_relationships.descriptors(thisRelIndex).isAllowedCountriesList Then
       fileNoAc = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepAllowedCountries, ddlType, thisOrgIndex, thisPoolIndex, , phaseModuleMeta, ldmIteration)

       genAllowedCountriesListFunction thisRelIndex, fileNoAc, thisOrgIndex, thisPoolIndex, ddlType

       GoTo NormalExit
     End If
 
 ' ### ENDIF IVK ###
     fileNo = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseFksRelTabs, ldmIteration)
 
     If ddlType = edtPdm And (g_relationships.descriptors(thisRelIndex).fkReferenceOrgId > 0 Or g_relationships.descriptors(thisRelIndex).fkReferencePoolId > 0) Then
       fileNoFk = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStep, ddlType, IIf(g_relationships.descriptors(thisRelIndex).fkReferenceOrgIndex > 0, g_relationships.descriptors(thisRelIndex).fkReferenceOrgIndex, thisOrgIndex), IIf(g_relationships.descriptors(thisRelIndex).fkReferencePoolIndex > 0, g_relationships.descriptors(thisRelIndex).fkReferencePoolIndex, thisPoolIndex), , phaseFksRelTabs, ldmIterationPoolSpecific)
     ElseIf ddlType = edtLdm And ldmIteration <> ldmIterationPoolSpecific Then
       fileNoFk = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStep, ddlType, , , , phaseFksRelTabs, ldmIterationPoolSpecific)
     Else
       fileNoFk = fileNo
     End If

     If generateLrt Then
       fileNoLrt = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, , phaseLrt, ldmIteration)

       fileNoLrtView = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, , phaseLrtViews, ldmIteration)

       fileNoClView = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, , phaseChangeLogViews, ldmIteration)

       fileNoLrtSup = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepLrt, ddlType, thisOrgIndex, thisPoolIndex, , phaseLrtSupport, ldmIteration)
     End If

 ' ### IF IVK ###
     If generatePsTaggingView And g_relationships.descriptors(thisRelIndex).isPsTagged Then
       fileNoPs = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phasePsTagging, ldmIteration)
     End If

     If g_relationships.descriptors(thisRelIndex).logLastChange Then
       If (g_relationships.descriptors(thisRelIndex).logLastChangeAutoMaint) Or _
          (generateLogChangeView And Not g_relationships.descriptors(thisRelIndex).isUserTransactional And Not g_relationships.descriptors(thisRelIndex).isPsTagged And g_relationships.descriptors(thisRelIndex).logLastChangeInView) Then
         If fileNoPs > 0 Then
           fileNoLc = fileNoPs
         Else
           fileNoLc = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseLogChange, ldmIteration)
         End If
       End If
     End If
 ' ### ELSE IVK ###
 '   If .logLastChange Then
 '     If (.logLastChangeAutoMaint) Or (generateLogChangeView And Not .isUserTransactional And .logLastChangeInView) Then
 '       fileNoLc = openDdlFile(g_targetDir, .sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseLogChange, ldmIteration)
 '     End If
 '   End If
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
     If g_genLrtSupport And generatePsCopySupport And (g_relationships.descriptors(thisRelIndex).isPsForming Or g_relationships.descriptors(thisRelIndex).supportExtendedPsCopy) And g_relationships.descriptors(thisRelIndex).isUserTransactional Then
       fileNoPsCopy = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepPsCopy, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIteration)
       fileNoPsCopy2 = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepPsCopy2, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIteration)
     End If

     If g_genLrtSupport And generateExpCopySupport And g_relationships.descriptors(thisRelIndex).isSubjectToExpCopy Then
       fileNoExpCopy = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepExpCopy, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIteration)
     End If

     If ddlType = edtPdm And supportArchivePool And poolSupportsArchiving(thisPoolId) Then
       fileNoArc = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseArchive, ldmIteration)
     End If

     If generateLrt Then
       If orgSetProductiveTargetPoolIndex > 0 Then
         fileNoSetProd = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepSetProd, ddlType, thisOrgIndex, orgSetProductiveTargetPoolIndex, , phaseUseCases, ldmIteration)

         fileNoSetProdCl = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepSetProd, ddlType, thisOrgIndex, orgSetProductiveTargetPoolIndex, , phaseChangeLogViews, ldmIteration)
       End If

       If thisOrgIndex <> g_primaryOrgIndex And Not g_relationships.descriptors(thisRelIndex).noFto Then
         fileNoFto = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStepFto, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIteration)
       End If
     End If

     If generateXmlExportSupport Then
       fileNoXmlV = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseXmlExport, ldmIteration)

       If generateXsdInCtoSchema And ddlType = edtPdm And thisOrgId <> -1 Then
         fileNoXmlF = openDdlFile(g_targetDir, g_relationships.descriptors(thisRelIndex).sectionIndex, processingStep, ddlType, , , , phaseXmlExport, ldmIteration)
       Else
         fileNoXmlF = fileNoXmlV
       End If
     End If

 ' ### ENDIF IVK ###
     On Error GoTo ErrorExit

     Dim genSupportForLrt As Boolean
     genSupportForLrt = False
     If g_genLrtSupport And g_relationships.descriptors(thisRelIndex).isUserTransactional Then
       If thisPoolIndex > 1 Then
         genSupportForLrt = poolSupportLrt
       Else
         genSupportForLrt = (ddlType = edtLdm) And Not g_relationships.descriptors(thisRelIndex).isCommonToOrgs And Not g_relationships.descriptors(thisRelIndex).isCommonToPools
       End If
     End If

     ' (optionally) loop twice over the table structure: first run: 'Main' (public) table; second run: corresponding (private) LRT-tables
     Dim loopCount As Integer, iteration As Integer, forLrt As Boolean
     loopCount = IIf(genSupportForLrt, 2, 1)

     Dim qualTabName As String, qualTabNameLdm As String
     Dim relSectionName As String, relShortName As String, relName As String
     Dim numAttrs As Integer
     Dim leftFkAttrs As String, rightFkAttrs As String
     Dim ukAttrDecls As String
     Dim pkAttrList As String

     Dim leftClass As ClassDescriptor, rightclass As ClassDescriptor
     leftClass = g_classes.descriptors(g_relationships.descriptors(thisRelIndex).leftEntityIndex)
     rightclass = g_classes.descriptors(g_relationships.descriptors(thisRelIndex).rightEntityIndex)

     Dim leftOrClass As ClassDescriptor, rightOrClass As ClassDescriptor
     leftOrClass = getOrMappingSuperClass(leftClass.sectionName, leftClass.className)
     rightOrClass = getOrMappingSuperClass(rightclass.sectionName, rightclass.className)

     relSectionName = g_relationships.descriptors(thisRelIndex).sectionName
     relName = g_relationships.descriptors(thisRelIndex).relName
     relShortName = g_relationships.descriptors(thisRelIndex).shortName
     numAttrs = g_relationships.descriptors(thisRelIndex).attrRefs.numDescriptors

 ' ### IF IVK ###
     isDivTagged = g_relationships.descriptors(thisRelIndex).leftIsDivision Or g_relationships.descriptors(thisRelIndex).rightIsDivision Or rightOrClass.aggHeadClassIndex = g_classIndexGenericCode Or leftOrClass.aggHeadClassIndex = g_classIndexGenericCode

 ' ### ENDIF IVK ###

     For iteration = 1 To loopCount Step 1
       forLrt = (iteration = 2)

       qualTabName = genQualTabNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt)
       qualTabNameLdm = IIf(ddlType = edtLdm, qualTabName, genQualTabNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, edtLdm, thisOrgIndex, thisPoolIndex, forLrt))

       addTabToDdlSummary qualTabName, ddlType, False
       registerQualTable qualTabNameLdm, qualTabName, g_relationships.descriptors(thisRelIndex).relIndex, g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, ddlType, g_relationships.descriptors(thisRelIndex).notAcmRelated, False, forLrt

       Dim leftQualTabName As String, rightQualTabName As String
       Dim leftQualTabNameLdm As String, rightQualTabNameLdm As String
       Dim leftUseSurrogateKey As Boolean, rightUseSurrogateKey As Boolean
       Dim leftUseFileNoFk As Boolean, rightUseFileNoFk As Boolean

       Dim isLeftRefToGen As Boolean
       Dim isLeftRefToNl As Boolean
       isLeftRefToGen = (g_relationships.descriptors(thisRelIndex).leftTargetType And erttGen) <> 0 And leftOrClass.isGenForming And Not leftOrClass.hasNoIdentity
       isLeftRefToNl = (g_relationships.descriptors(thisRelIndex).leftTargetType And erttNL) <> 0 And ((isLeftRefToGen And leftOrClass.hasNlAttrsInGenInclSubClasses) Or (Not isLeftRefToGen And leftOrClass.hasNlAttrsInNonGenInclSubClasses))

       Dim isRightRefToGen As Boolean
       Dim isRightRefToNl As Boolean
       isRightRefToGen = (g_relationships.descriptors(thisRelIndex).rightTargetType And erttGen) <> 0 And rightOrClass.isGenForming And Not rightOrClass.hasNoIdentity
       isRightRefToNl = (g_relationships.descriptors(thisRelIndex).rightTargetType And erttNL) <> 0 And ((isRightRefToGen And rightOrClass.hasNlAttrsInGenInclSubClasses) Or (Not isRightRefToGen And rightOrClass.hasNlAttrsInNonGenInclSubClasses))

         leftUseFileNoFk = (g_relationships.descriptors(thisRelIndex).isCommonToOrgs And Not leftOrClass.isCommonToOrgs) Or (g_relationships.descriptors(thisRelIndex).isCommonToPools And Not leftOrClass.isCommonToPools)
         leftUseSurrogateKey = getUseSurrogateKeyByClassName(leftOrClass.sectionName, leftOrClass.className)
         leftQualTabName = _
           genQualTabNameByClassIndex( _
             leftOrClass.classIndex, ddlType, _
             IIf(g_relationships.descriptors(thisRelIndex).fkReferenceOrgIndex > 0, g_relationships.descriptors(thisRelIndex).fkReferenceOrgIndex, thisOrgIndex), _
             IIf(g_relationships.descriptors(thisRelIndex).fkReferencePoolIndex > 0, g_relationships.descriptors(thisRelIndex).fkReferencePoolIndex, thisPoolIndex), _
             isLeftRefToGen _
           )
         leftQualTabNameLdm = genQualTabNameByClassIndex(leftOrClass.classIndex, edtLdm, thisOrgIndex, thisPoolIndex, isLeftRefToGen, , , isLeftRefToNl)
         rightUseFileNoFk = (g_relationships.descriptors(thisRelIndex).isCommonToOrgs And Not rightOrClass.isCommonToOrgs) Or (g_relationships.descriptors(thisRelIndex).isCommonToPools And Not rightOrClass.isCommonToPools)
         rightUseSurrogateKey = getUseSurrogateKeyByClassName(rightOrClass.sectionName, rightOrClass.className)
         rightQualTabName = _
           genQualTabNameByClassIndex( _
             rightOrClass.classIndex, ddlType, _
             IIf(g_relationships.descriptors(thisRelIndex).fkReferenceOrgIndex > 0, g_relationships.descriptors(thisRelIndex).fkReferenceOrgIndex, thisOrgIndex), _
             IIf(g_relationships.descriptors(thisRelIndex).fkReferencePoolIndex > 0, g_relationships.descriptors(thisRelIndex).fkReferencePoolIndex, thisPoolIndex), _
             isRightRefToGen _
           )
         rightQualTabNameLdm = genQualTabNameByClassIndex(rightOrClass.classIndex, edtLdm, thisOrgIndex, thisPoolIndex, isRightRefToGen, , , isRightRefToNl)

       If generateDdlCreateTable Then
         printChapterHeader IIf(g_relationships.descriptors(thisRelIndex).notAcmRelated, "LDM", "ACM") & _
                                "-Relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """" & IIf(Not forLrt, "", " (LRT)") & " (" & _
                                g_relationships.descriptors(thisRelIndex).leftClassSectionName & "." & g_relationships.descriptors(thisRelIndex).leftClassName & "[" & _
                                IIf(g_relationships.descriptors(thisRelIndex).minLeftCardinality = 0 Or (g_relationships.descriptors(thisRelIndex).minLeftCardinality = 1 And g_relationships.descriptors(thisRelIndex).maxLeftCardinality <> 1), g_relationships.descriptors(thisRelIndex).minLeftCardinality & "..", "") & _
                                IIf(g_relationships.descriptors(thisRelIndex).maxLeftCardinality = 1, "1", "n") & _
                                "] <-> " & _
                                rightclass.sectionName & "." & rightclass.className & "[" & _
                                IIf(g_relationships.descriptors(thisRelIndex).minRightCardinality = 0 Or (g_relationships.descriptors(thisRelIndex).minRightCardinality = 1 And g_relationships.descriptors(thisRelIndex).maxRightCardinality <> 1), g_relationships.descriptors(thisRelIndex).minRightCardinality & "..", "") & _
                                IIf(g_relationships.descriptors(thisRelIndex).maxRightCardinality = 1, "1", "m") & _
                                "])", fileNo

         If reuseRelationships And g_relationships.descriptors(thisRelIndex).reusingRelIndexes.numIndexes > 0 Then
           Dim i As Integer
           For i = 1 To g_relationships.descriptors(thisRelIndex).reusingRelIndexes.numIndexes Step 1
               printComment "subsuming relationship:  """ & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).sectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).relName & """ (" & _
                                  g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).leftClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).leftClassName & "[" & _
                                  IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).minLeftCardinality = 0 Or (g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).minLeftCardinality = 1 And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).maxLeftCardinality <> 1), g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).minLeftCardinality & "..", "") & _
                                  IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).maxLeftCardinality = 1, "1", "n") & _
                                  "] <-> " & _
                                  g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).rightClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).rightClassName & "[" & _
                                  IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).minRightCardinality = 0 Or (g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).minRightCardinality = 1 And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).maxRightCardinality <> 1), g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).minRightCardinality & "..", "") & _
                                  IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).reusingRelIndexes.indexes(i)).maxRightCardinality = 1, "1", "m") & _
                                  "])", fileNo
           Next i
         End If

         Print #fileNo, "CREATE TABLE"
         Print #fileNo, addTab(1); qualTabName
         Print #fileNo, "("

         pkAttrList = ""

         Dim tabColumns As EntityColumnDescriptors
         tabColumns = nullEntityColumnDescriptors

         Dim transformation As AttributeListTransformation
         transformation = nullAttributeTransformation
         genTransformedAttrDeclsForRelationshipWithColReUse_Int thisRelIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, , False, forLrt, _
             IIf(forLrt, edomDeclLrt, edomDeclNonLrt), poolCommonItemsLocal

         Print #fileNo, ")"

 ' ### IF IVK ###
         Dim fkAttrToDiv As String
         If isDivTagged And supportRangePartitioningByDivOid Then
           If g_relationships.descriptors(thisRelIndex).leftIsDivision Then
             fkAttrToDiv = genSurrogateKeyName(ddlType, g_relationships.descriptors(thisRelIndex).rlShortRelName)
           ElseIf g_relationships.descriptors(thisRelIndex).rightIsDivision Then
             fkAttrToDiv = genSurrogateKeyName(ddlType, g_relationships.descriptors(thisRelIndex).lrShortRelName)
           Else
             fkAttrToDiv = conDivOid
           End If
         End If

         genTabDeclTrailer fileNo, ddlType, isDivTagged, eactRelationship, g_relationships.descriptors(thisRelIndex).relIndex, thisOrgIndex, thisPoolIndex, False, forLrt, False, False, fkAttrToDiv, tabPartitionType
 ' ### ELSE IVK ###
 '       genTabDeclTrailer fileNo, ddlType, eactRelationship, .relIndex, thisOrgIndex, thisPoolIndex, False, forLrt, False
 ' ### ENDIF IVK ###

         Print #fileNo, gc_sqlCmdDelim
       End If

       If (forLrt And lrtTablesVolatile) Or g_relationships.descriptors(thisRelIndex).isVolatile Then
         Print #fileNo,
         Print #fileNo, addTab(0); "ALTER TABLE "; qualTabName; " VOLATILE CARDINALITY"; gc_sqlCmdDelim
       End If

       If generateCommentOnTables And Not g_relationships.descriptors(thisRelIndex).notAcmRelated Then
         Print #fileNo,
         genDbObjComment "TABLE", qualTabName, "ACM-Relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """" & IIf(forLrt, " (LRT)", ""), fileNo, thisOrgIndex, thisPoolIndex
       End If

       If generateCommentOnColumns And Not g_relationships.descriptors(thisRelIndex).notAcmRelated Then
         Print #fileNo,
         Print #fileNo, addTab(0); "COMMENT ON "; qualTabName; " ("
         genTransformedAttrDeclsForRelationshipWithColReUse_Int thisRelIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, False, forLrt, _
             IIf(forLrt, edomListLrt, edomListNonLrt) Or edomComment, poolCommonItemsLocal
         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       End If

       If ddlType = edtPdm And Not g_relationships.descriptors(thisRelIndex).noAlias Then
 ' ### IF IVK ###
         genAliasDdl g_relationships.descriptors(thisRelIndex).sectionIndex, g_relationships.descriptors(thisRelIndex).relName, g_relationships.descriptors(thisRelIndex).isCommonToOrgs, g_relationships.descriptors(thisRelIndex).isCommonToPools, Not g_relationships.descriptors(thisRelIndex).notAcmRelated, _
                     qualTabNameLdm, qualTabName, g_relationships.descriptors(thisRelIndex).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, forLrt, False, False, False, _
                     "ACM-Relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """", , g_relationships.descriptors(thisRelIndex).isUserTransactional, g_relationships.descriptors(thisRelIndex).isPsTagged, , g_relationships.descriptors(thisRelIndex).isSubjectToArchiving, g_relationships.descriptors(thisRelIndex).logLastChangeInView
 ' ### ELSE IVK ###
 '       genAliasDdl(.sectionIndex, .relName, .isCommonToOrgs, .isCommonToPools, Not .notAcmRelated, _
 '                   qualTabNameLdm, qualTabName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, forLrt, _
 '                   "ACM-Relationship """ & .sectionName & "." & .relName & """", , .isUserTransactional, .logLastChangeInView)
 ' ### ENDIF IVK ###
       End If

       If Not ((ddlType = edtPdm) And (g_relationships.descriptors(thisRelIndex).noIndexesInPool >= 0) And (g_relationships.descriptors(thisRelIndex).noIndexesInPool = thisPoolId)) Then
 ' ### IF IVK ###
         genIndexesForEntity qualTabName, thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, False, forLrt, False, _
           False, poolSuppressUniqueConstraints, tabPartitionType
 ' ### ELSE IVK ###
 '       genIndexesForEntity qualTabName, thisRelIndex.relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, False, forLrt, False, _
 '         False, poolSuppressUniqueConstraints
 ' ### ENDIF IVK ###
       End If

       Dim fkName As String
       Dim qualIndexName As String
 ' ### IF IVK ###
       If (rightOrClass.isPsTagged Or leftOrClass.isPsTagged) Then
         If Not poolSuppressRefIntegrity Then

           fkName = genFkName(g_relationships.descriptors(thisRelIndex).relName, g_relationships.descriptors(thisRelIndex).shortName, "PS", ddlType, thisOrgIndex, thisPoolIndex)

           Dim qualTabNameProductStructureLdm As String
           qualTabNameProductStructureLdm = genQualTabNameByClassIndex(g_classIndexProductStructure, edtLdm)

           If generateDdlCreateFK Then
             printSectionHeader "Foreign Key to ""Product Structure"" Table", fileNo
             Print #fileNo,
             Print #fileNo, addTab(0); "ALTER TABLE"
             Print #fileNo, addTab(1); qualTabName
             Print #fileNo, addTab(0); "ADD CONSTRAINT"
             Print #fileNo, addTab(1); fkName
             Print #fileNo, addTab(0); "FOREIGN KEY"

             Print #fileNo, addTab(1); "("; g_anPsOid; ")"
             Print #fileNo, addTab(0); "REFERENCES"
             Print #fileNo, addTab(1); g_qualTabNameProductStructure; " ("; g_anOid; ")"
             Print #fileNo, gc_sqlCmdDelim
           End If

           registerQualLdmFk qualTabNameLdm, qualTabNameProductStructureLdm, g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship
         End If

       ElseIf (rightOrClass.aggHeadClassIndex = g_classIndexGenericCode Or leftOrClass.aggHeadClassIndex = g_classIndexGenericCode) Then
         If Not poolSuppressRefIntegrity Then

           fkName = genFkName(g_relationships.descriptors(thisRelIndex).relName, g_relationships.descriptors(thisRelIndex).shortName, "DIV", ddlType, thisOrgIndex, thisPoolIndex)

           Dim qualTabNameDivisionLdm As String
           qualTabNameDivisionLdm = genQualTabNameByClassIndex(g_classIndexDivision, edtLdm)

           If generateDdlCreateFK Then
             printSectionHeader "Foreign Key to ""Division"" Table", fileNo
             Print #fileNo,
             Print #fileNo, addTab(0); "ALTER TABLE"
             Print #fileNo, addTab(1); qualTabName
             Print #fileNo, addTab(0); "ADD CONSTRAINT"
             Print #fileNo, addTab(1); fkName
             Print #fileNo, addTab(0); "FOREIGN KEY"

             Print #fileNo, addTab(1); "("; g_anDivOid; ")"
             Print #fileNo, addTab(0); "REFERENCES"
             Print #fileNo, addTab(1); g_qualTabNameDivision; " ("; g_anOid; ")"
             Print #fileNo, gc_sqlCmdDelim
           End If

           registerQualLdmFk qualTabNameLdm, qualTabNameDivisionLdm, g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship
         End If

       End If
 
 ' ### ENDIF IVK ###
       If supportNlForRelationships And g_relationships.descriptors(thisRelIndex).isNl Then
         ' DDL for Foreign Key to 'Language Table'
         If Not poolSuppressRefIntegrity Then
           If generateDdlCreateFK Then
             printSectionHeader "Foreign Key to ""Language Table""", fileNo
             Print #fileNo,
             Print #fileNo, "ALTER TABLE"
             Print #fileNo, addTab; qualTabName
             Print #fileNo, "ADD CONSTRAINT"
             Print #fileNo, addTab; genFkName(g_relationships.descriptors(thisRelIndex).shortName & "LAN", g_relationships.descriptors(thisRelIndex).shortName & "LAN", "", ddlType, thisOrgIndex, thisPoolIndex)
             Print #fileNo, "FOREIGN KEY"
             Print #fileNo, addTab; "("; g_anLanguageId; ")"
             Print #fileNo, "REFERENCES"
             Print #fileNo, addTab; g_qualTabNameLanguage; "("; g_anEnumId; ")"
             Print #fileNo, gc_sqlCmdDelim
           End If

           registerQualLdmFk qualTabNameLdm, genQualTabNameByEnumIndex(g_enumIndexLanguage, edtLdm), g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship
         End If
       End If
 
       If Not forLrt Then
         Dim fileNoToUse As Integer
         If g_relationships.descriptors(thisRelIndex).isCommonToOrgs And ddlType = edtPdm And Not rightOrClass.isCommonToOrgs And Not poolSuppressRefIntegrity And g_relationships.descriptors(thisRelIndex).fkReferenceOrgId <= 0 Then
           If generateDdlCreateFK Then
             logMsg "unable to implement foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since this relationship is common to MPCs and (right) class """ & _
                    rightOrClass.sectionName & "." & rightOrClass.className & """ is not", _
                    ellWarning, ddlType, thisOrgIndex, thisPoolIndex
             Print #fileNo,
             Print #fileNo, "-- unable to implement foreign key since """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ is common to MPCs"
           End If
         ElseIf g_relationships.descriptors(thisRelIndex).isCommonToPools And ddlType = edtPdm And (Not (rightOrClass.isCommonToPools Or rightOrClass.isCommonToOrgs)) And Not poolSuppressRefIntegrity And g_relationships.descriptors(thisRelIndex).fkReferencePoolId <= 0 Then
           If generateDdlCreateFK Then
             logMsg "unable to implement foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since this relationship is common to Pools and (right) class """ & _
                    rightOrClass.sectionName & "." & rightOrClass.className & """ is not", _
                    ellWarning, ddlType, thisOrgIndex, thisPoolIndex
             Print #fileNo,
             Print #fileNo, "-- unable to implement foreign key since """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ is common to pools"
           End If
         Else
             If Not poolSuppressRefIntegrity Then
               If generateDdlCreateFK Then
                 fileNoToUse = IIf(rightUseFileNoFk, fileNoFk, fileNo)
                 If (g_relationships.descriptors(thisRelIndex).fkReferenceOrgId > 0 And Not rightOrClass.isCommonToOrgs) Or (g_relationships.descriptors(thisRelIndex).fkReferencePoolId > 0 And Not rightOrClass.isCommonToPools) Then
                   Print #fileNoToUse, "-- foreign key for "; _
                                       IIf(rightOrClass.isCommonToOrgs, "cto-", IIf(rightOrClass.isCommonToPools, "ctp-", "")); _
                                       "class """ & rightOrClass.sectionName & "." & rightOrClass.className & """ is implemented in"; _
                                       IIf(g_relationships.descriptors(thisRelIndex).fkReferenceOrgId > 0, " MPC " & g_relationships.descriptors(thisRelIndex).fkReferenceOrgId, ""); _
                                       IIf(g_relationships.descriptors(thisRelIndex).fkReferencePoolId > 0, " Pool " & g_relationships.descriptors(thisRelIndex).fkReferencePoolId, "")
                 End If

                 printSectionHeader "Foreign Key corresponding to Class """ & rightclass.sectionName & "." & rightclass.className & """", fileNoToUse
                 Print #fileNoToUse, addTab(0); "ALTER TABLE"
                 Print #fileNoToUse, addTab(1); qualTabName
                 Print #fileNoToUse, addTab(0); "ADD CONSTRAINT"

                   Print #fileNoToUse, addTab(1); genFkName(rightclass.className, rightclass.shortName, relShortName, ddlType, thisOrgIndex, thisPoolIndex)

                 Print #fileNoToUse, addTab(0); "FOREIGN KEY"
                 Print #fileNoToUse, addTab(1); "("; getFkSrcAttrSeqExt(rightclass.classIndex, "", thisPoolIndex, ddlType, genSurrogateKeyName(ddlType, rightclass.shortName), , IIf(rightclass.subClassIdStrSeparatePartition.numMaps > 0, False, True)); ")"
                 Print #fileNoToUse, addTab(0); "REFERENCES"
                 Print #fileNoToUse, addTab(1); rightQualTabName; " ("; getFkTargetAttrSeqExt(rightOrClass.classIndex, thisPoolIndex, ddlType, g_anOid, rightOrClass.aggHeadClassIdStr); ")"
                 If g_relationships.descriptors(thisRelIndex).rlFkMaintenanceMode = efkmCascade Then
                   Print #fileNoToUse, addTab(0); "ON DELETE CASCADE"
                 End If
                 If g_relationships.descriptors(thisRelIndex).isNotEnforced Then
                   Print #fileNoToUse, addTab(0); "NOT ENFORCED"
                 End If
                 Print #fileNoToUse, gc_sqlCmdDelim
               End If

               registerQualLdmFk qualTabNameLdm, rightQualTabNameLdm, g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, , , Not g_relationships.descriptors(thisRelIndex).isNotEnforced
             End If
         End If

         If g_relationships.descriptors(thisRelIndex).isCommonToOrgs And ddlType = edtPdm And Not leftOrClass.isCommonToOrgs And Not poolSuppressRefIntegrity And g_relationships.descriptors(thisRelIndex).fkReferenceOrgId <= 0 Then
           If generateDdlCreateFK Then
             logMsg "unable to implement foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since this relationship is common to MPCs and (left) class """ & _
                    leftOrClass.sectionName & "." & leftOrClass.className & """ is not", _
                    ellWarning, ddlType, thisOrgIndex, thisPoolIndex
             Print #fileNo,
             Print #fileNo, "-- unable to implement foreign key since """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ is common to MPCs"
           End If
         ElseIf g_relationships.descriptors(thisRelIndex).isCommonToPools And ddlType = edtPdm And (Not (leftOrClass.isCommonToPools Or leftOrClass.isCommonToOrgs)) And Not poolSuppressRefIntegrity And g_relationships.descriptors(thisRelIndex).fkReferencePoolId <= 0 Then
           If generateDdlCreateFK Then
             logMsg "unable to implement foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since this relationship is common to Pools and (right) class """ & _
                    leftOrClass.sectionName & "." & leftOrClass.className & """ is not", _
                    ellWarning, ddlType, thisOrgIndex, thisPoolIndex
             Print #fileNo,
             Print #fileNo, "-- unable to implement foreign key since """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ is common to pools"
           End If
         Else
             If Not poolSuppressRefIntegrity Then
               If generateDdlCreateFK Then
                 fileNoToUse = IIf(leftUseFileNoFk, fileNoFk, fileNo)
                 If (g_relationships.descriptors(thisRelIndex).fkReferenceOrgId > 0 And Not leftOrClass.isCommonToOrgs) Or (g_relationships.descriptors(thisRelIndex).fkReferencePoolId > 0 And Not leftOrClass.isCommonToPools) Then
                   Print #fileNoToUse, "-- foreign key for "; _
                                       IIf(leftOrClass.isCommonToOrgs, "cto-", IIf(leftOrClass.isCommonToPools, "ctp-", "")); _
                                       "class """ & leftOrClass.sectionName & "."; leftOrClass.className; """ is implemented in"; _
                                       IIf(g_relationships.descriptors(thisRelIndex).fkReferenceOrgId > 0, " MPC " & g_relationships.descriptors(thisRelIndex).fkReferenceOrgId, ""); _
                                       IIf(g_relationships.descriptors(thisRelIndex).fkReferencePoolId > 0, " Pool " & g_relationships.descriptors(thisRelIndex).fkReferencePoolId, "")
                 End If

                 printSectionHeader "Foreign Key corresponding to Class """ & leftClass.sectionName & "." & leftClass.className & """", fileNoToUse
                 Print #fileNoToUse, addTab(0); "ALTER TABLE"
                 Print #fileNoToUse, addTab(1); qualTabName
                 Print #fileNoToUse, addTab(0); "ADD CONSTRAINT"

                   Print #fileNoToUse, addTab(1); genFkName(leftClass.className, leftClass.shortName, relShortName, ddlType, thisOrgIndex, thisPoolIndex)

                 Print #fileNoToUse, addTab(0); "FOREIGN KEY"
                 Print #fileNoToUse, addTab(1); "("; getFkSrcAttrSeqExt(leftClass.classIndex, "", thisPoolIndex, ddlType, genSurrogateKeyName(ddlType, leftClass.shortName), , IIf(leftClass.subClassIdStrSeparatePartition.numMaps > 0, False, True)); ")"
                 Print #fileNoToUse, addTab(0); "REFERENCES"
                 Print #fileNoToUse, addTab(1); leftQualTabName; " ("; getFkTargetAttrSeqExt(leftOrClass.classIndex, thisPoolIndex, ddlType, g_anOid, leftOrClass.aggHeadClassIdStr); ")"
                 If g_relationships.descriptors(thisRelIndex).lrFkMaintenanceMode = efkmCascade Then
                   Print #fileNoToUse, addTab(0); "ON DELETE CASCADE"
                 End If
                 If g_relationships.descriptors(thisRelIndex).isNotEnforced Then
                   Print #fileNoToUse, addTab(0); "NOT ENFORCED"
                 End If
                 Print #fileNoToUse, gc_sqlCmdDelim
               End If

               registerQualLdmFk qualTabNameLdm, leftQualTabNameLdm, g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, , , Not g_relationships.descriptors(thisRelIndex).isNotEnforced
             End If
         End If
       End If

       If g_relationships.descriptors(thisRelIndex).nlAttrRefs.numDescriptors > 0 Then
         genNlsTabsForRelationship _
           thisRelIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNo, fileNo, ddlType, , forLrt, _
           leftFkAttrs & ", " & rightFkAttrs, ukAttrDecls, poolCommonItemsLocal
       End If
 ' ### IF IVK ###
 
       If (g_relationships.descriptors(thisRelIndex).leftDependentAttribute <> "" Or g_relationships.descriptors(thisRelIndex).rightDependentAttribute <> "") And (Not forLrt Or Not poolSupportLrt Or Not g_relationships.descriptors(thisRelIndex).useMqtToImplementLrt) And poolSupportUpdates Then
         ' triggers to maintain derived attributes (for LRT-MQT-supported relationships this is done in MQT-triggers)
         genVirtualAttrTriggerForRel fileNoLrtSup, thisRelIndex, qualTabName, thisOrgIndex, thisPoolIndex, forLrt, poolSupportLrt, ddlType
       End If
 ' ### ENDIF IVK ###
     Next iteration

     If g_genLrtSupport And g_relationships.descriptors(thisRelIndex).isUserTransactional And Not poolCommonItemsLocal Then
       genLrtSupportDdlForRelationship thisRelIndex, thisOrgIndex, thisPoolIndex, fileNo, fileNoLrtView, fileNoClView, fileNo, fileNoLrtSup, ddlType
     End If

     If genFksForLrtOnRelationships Then
       If genSupportForLrt And Not poolSuppressRefIntegrity Then
 ' ### IF IVK ###
         genFksForLrtByEntity qualTabName, qualTabNameLdm, thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, False, forLrt, , tabPartitionType
 ' ### ELSE IVK ###
 '       genFksForLrtByEntity qualTabName, qualTabNameLdm, thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoFk, ddlType, False, forLrt
 ' ### ENDIF IVK ###
       End If
     End If
 
 ' ### IF IVK ###
     If genSupportForLrt Then
       If generatePsCopySupport Then
         genPsCopySupportDdlForRelationship thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoPsCopy, fileNoPsCopy2, ddlType
       End If
 
       If generateExpCopySupport Then
         genExpCopySupportDdlForRelationship thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoExpCopy, ddlType
       End If

       If orgSetProductiveTargetPoolIndex > 0 Then
         genSetProdSupportDdlForRelationship thisRelIndex, thisOrgIndex, thisPoolIndex, orgSetProductiveTargetPoolIndex, fileNoSetProd, fileNoSetProdCl, ddlType
       End If

       If thisOrgIndex <> g_primaryOrgIndex And Not g_relationships.descriptors(thisRelIndex).noFto Then
         genFtoSupportDdlForRelationship thisRelIndex, g_primaryOrgIndex, g_productiveDataPoolIndex, thisOrgIndex, thisPoolIndex, fileNoFto, ddlType
       End If
     End If

     If generatePsTaggingView And g_relationships.descriptors(thisRelIndex).isPsTagged Then
       genPsTagSupportDdlForRelationship thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoPs, ddlType
     End If
 
 ' ### ENDIF IVK ###
     If generateLogChangeView And Not g_relationships.descriptors(thisRelIndex).isUserTransactional And Not g_relationships.descriptors(thisRelIndex).isPsTagged And g_relationships.descriptors(thisRelIndex).logLastChange And g_relationships.descriptors(thisRelIndex).logLastChangeInView Then
       genLogChangeSupportDdlForRelationship thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType
     End If
 
     If g_relationships.descriptors(thisRelIndex).logLastChange And g_relationships.descriptors(thisRelIndex).logLastChangeAutoMaint Then
       genLogChangeAutoMaintSupportDdlForRelationship thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoLc, ddlType, , forLrt
     End If
 
 ' ### IF IVK ###
     If ddlType = edtPdm And supportArchivePool And poolSupportsArchiving(thisPoolId) Then
       genArchiveSupportDdlForRelationship thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoArc, ddlType
     End If

 GenXmlExport:
     If generateXmlExportSupport And g_relationships.descriptors(thisRelIndex).supportXmlExport And (ddlType = edtLdm Or thisPoolId = -1 Or poolSupportXmlExport) Then
       genXmlExportDdlForRelationship thisRelIndex, thisOrgIndex, thisPoolIndex, fileNoXmlF, fileNoXmlV, ddlType
     End If

 ' ### ENDIF IVK ###
     ' relationship may be a copy taken from g_relationships! make sure we update the original source!
     g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relIndex).isLdmCsvExported = True
     g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relIndex).isCtoAliasCreated = True
 
     g_relationships.descriptors(thisRelIndex).isLdmCsvExported = True ' safe is safe ;-)
     g_relationships.descriptors(thisRelIndex).isCtoAliasCreated = True
     If genSupportForLrt Then
       g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relIndex).isLdmLrtCsvExported = True
       g_relationships.descriptors(thisRelIndex).isLdmLrtCsvExported = True ' safe is safe ;-)
     End If
 ' ### IF IVK ###

     If g_relationships.descriptors(thisRelIndex).isDisallowedCountries Or g_relationships.descriptors(thisRelIndex).isAllowedCountries Then
       genAllowedCountriesFunction thisRelIndex, fileNoAc, thisOrgIndex, thisPoolIndex, ddlType
       genAllowedCountriesView thisRelIndex, fileNoAc, thisOrgIndex, thisPoolIndex, ddlType
     End If
 ' ### ENDIF IVK ###
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Close #fileNoFk
 ' ### IF IVK ###
   Close #fileNoAc
 ' ### ENDIF IVK ###
   Close #fileNoLrt
   Close #fileNoLrtView
   Close #fileNoClView
   Close #fileNoLrtSup
 ' ### IF IVK ###
   Close #fileNoSetProd
   Close #fileNoSetProdCl
   Close #fileNoFto
   Close #fileNoPsCopy
   Close #fileNoPsCopy2
   Close #fileNoExpCopy
   Close #fileNoPs
 ' ### ENDIF IVK ###
   Close #fileNoLc
 ' ### IF IVK ###
   Close #fileNoArc
   Close #fileNoXmlV
   Close #fileNoXmlF
 ' ### ENDIF IVK ###
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Private Sub genVirtualAttrTriggerForRelAndClass( _
   fileNo As Integer, _
   thisRelIndex As Integer, _
   ByRef qualTabName As String, _
   ByRef attrIndex As Integer, _
   ByRef refClassIndex As Integer, _
   ByRef refClassOrParentIndex As Integer, _
   ByRef refColumnName As String, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forLrt As Boolean = False, _
   Optional poolSupportLrt As Boolean = True, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim qualTriggerName As String
   Dim qualRefTabName As String
   Dim qualRefTabNameLrt As String
   Dim attrName As String
   Dim transformation As AttributeListTransformation
   Dim refClassName As String
   Dim refSectionName As String

   attrName = genAttrNameByIndex(attrIndex, ddlType)

     refClassName = g_classes.descriptors(refClassIndex).className
     refSectionName = g_classes.descriptors(refClassIndex).sectionName

   qualRefTabName = genQualTabNameByClassIndex(refClassOrParentIndex, ddlType, thisOrgIndex, thisPoolIndex, , forLrt)
   qualRefTabNameLrt = genQualTabNameByClassIndex(refClassOrParentIndex, ddlType, thisOrgIndex, thisPoolIndex, , True)

     ' ####################################################################################################################
     ' #    INSERT Trigger
     ' ####################################################################################################################

     qualTriggerName = genQualTriggerNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, , , , "VA_INS", eondmSuffix)

     printSectionHeader "Insert-Trigger for maintaining virtual column """ & attrName & """ in table """ & qualRefTabName & """ (ACM-Class """ & refSectionName & "." & refClassName & """)", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "AFTER INSERT ON"
     Print #fileNo, addTab(1); qualTabName
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader fileNo, "update virtual column in " & IIf(forLrt, "private ", IIf(poolSupportLrt, "public ", "")) & "table """ & qualRefTabName & """", , True
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); qualRefTabName; " T"
     Print #fileNo, addTab(1); "SET"

     initAttributeTransformation transformation, 0
     setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, "T", IIf(forLrt, "T." & g_anInLrt, "")
 
       Print #fileNo, addTab(2); "T."; attrName; " = "; transformAttrName(attrName, g_attributes.descriptors(attrIndex).valueType, g_attributes.descriptors(attrIndex).valueTypeIndex, transformation, ddlType, , , , True, attrIndex, edomValueVirtual)

     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "T."; g_anOid; " = "; gc_newRecordName; "."; refColumnName

     If forLrt Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "T."; g_anInLrt; " = "; gc_newRecordName; "."; g_anInLrt
     End If
     Print #fileNo, addTab(1); ";"

     Print #fileNo, "END"
     Print #fileNo, gc_sqlCmdDelim

     ' ####################################################################################################################
     ' #    UPDATE Trigger
     ' ####################################################################################################################

     qualTriggerName = genQualTriggerNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, , , , "VA_UPD", eondmSuffix)

     printSectionHeader "Update-Trigger for maintaining virtual column """ & g_relationships.descriptors(thisRelIndex).leftDependentAttribute & """ in table """ & qualRefTabName & """ (ACM-Class """ & refSectionName & "." & refClassName & """)", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "AFTER UPDATE ON"
     Print #fileNo, addTab(1); qualTabName
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
     Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader fileNo, "update virtual column in " & IIf(forLrt, "private ", IIf(poolSupportLrt, "public ", "")) & "table """ & qualRefTabName & """", , True
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); qualRefTabName; " T"
     Print #fileNo, addTab(1); "SET"

     initAttributeTransformation transformation, 0
     setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, "T", IIf(forLrt, "T." & g_anInLrt, "")

       Print #fileNo, addTab(2); "T."; attrName; " = "; transformAttrName(attrName, g_attributes.descriptors(attrIndex).valueType, g_attributes.descriptors(attrIndex).valueTypeIndex, transformation, ddlType, , , , True, attrIndex, edomValueVirtual)

     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "T."; g_anOid; " = "; gc_newRecordName; "."; refColumnName

     If forLrt Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "T."; g_anInLrt; " = "; gc_newRecordName; "."; g_anInLrt
     End If

     Print #fileNo, addTab(1); ";"

     If Not forLrt And poolSupportLrt Then
       genProcSectionHeader fileNo, "update virtual column in private table """ & qualRefTabNameLrt & """", , True
       Print #fileNo, addTab(1); "UPDATE"
       Print #fileNo, addTab(2); qualRefTabNameLrt; " T"
       Print #fileNo, addTab(1); "SET"

       initAttributeTransformation transformation, 0
       setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, "T", "T." & g_anInLrt

         Print #fileNo, addTab(2); "T."; attrName; " = "; transformAttrName(attrName, g_attributes.descriptors(attrIndex).valueType, g_attributes.descriptors(attrIndex).valueTypeIndex, transformation, ddlType, , , , True, attrIndex, edomValueVirtual)

       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "T."; g_anOid; " = "; gc_newRecordName; "."; refColumnName
       Print #fileNo, addTab(1); ";"
     End If

     Print #fileNo,
     Print #fileNo, addTab(1); "IF "; gc_newRecordName; "."; refColumnName; " <> "; gc_oldRecordName; "."; refColumnName; " THEN"

     genProcSectionHeader fileNo, "update virtual column in " & IIf(forLrt, "private ", IIf(poolSupportLrt, "public ", "")) & "table """ & qualRefTabName & """", 2, True
     Print #fileNo, addTab(2); "UPDATE"
     Print #fileNo, addTab(3); qualRefTabName; " T"
     Print #fileNo, addTab(2); "SET"

     initAttributeTransformation transformation, 0
     setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, "T", IIf(forLrt, "T." & g_anInLrt, "")

       Print #fileNo, addTab(3); "T."; attrName; " = "; transformAttrName(attrName, g_attributes.descriptors(attrIndex).valueType, g_attributes.descriptors(attrIndex).valueTypeIndex, transformation, ddlType, , , , True, attrIndex, edomValueVirtual)

     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "T."; g_anOid; " = "; gc_oldRecordName; "."; refColumnName

     If forLrt Then
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "T."; g_anInLrt; " = "; gc_newRecordName; "."; g_anInLrt
     End If

     Print #fileNo, addTab(2); ";"

     If Not forLrt And poolSupportLrt Then
       genProcSectionHeader fileNo, "update virtual column in private table """ & qualRefTabNameLrt & """", 2, True
       Print #fileNo, addTab(2); "UPDATE"
       Print #fileNo, addTab(3); qualRefTabNameLrt; " T"
       Print #fileNo, addTab(2); "SET"

       initAttributeTransformation transformation, 0
       setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, "T", "T." & g_anInLrt

         Print #fileNo, addTab(3); "T."; attrName; " = "; transformAttrName(attrName, g_attributes.descriptors(attrIndex).valueType, g_attributes.descriptors(attrIndex).valueTypeIndex, transformation, ddlType, , , , True, attrIndex, edomValueVirtual)

       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "T."; g_anOid; " = "; gc_newRecordName; "."; refColumnName
       Print #fileNo, addTab(2); ";"
     End If
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo, "END"
     Print #fileNo, gc_sqlCmdDelim

     ' ####################################################################################################################
     ' #    DELETE Trigger
     ' ####################################################################################################################

     qualTriggerName = genQualTriggerNameByRelIndex(g_relationships.descriptors(thisRelIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, , , , "VA_DEL", eondmSuffix)

     printSectionHeader "Delete-Trigger for maintaining virtual column """ & g_relationships.descriptors(thisRelIndex).leftDependentAttribute & """ in table """ & qualRefTabName & """ (ACM-Class """ & refSectionName & "." & refClassName & """)", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "AFTER DELETE ON"
     Print #fileNo, addTab(1); qualTabName
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader fileNo, "update virtual column in " & IIf(forLrt, "private ", IIf(poolSupportLrt, "public ", "")) & "table """ & qualRefTabName & """", , True
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); qualRefTabName; " T"
     Print #fileNo, addTab(1); "SET"

     initAttributeTransformation transformation, 0
     setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, "T", IIf(forLrt, "T." & g_anInLrt, "")

       Print #fileNo, addTab(2); "T."; attrName; " = "; transformAttrName(attrName, g_attributes.descriptors(attrIndex).valueType, g_attributes.descriptors(attrIndex).valueTypeIndex, transformation, ddlType, , , , True, attrIndex, edomValueVirtual)

     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "T."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid

     If forLrt Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "T."; g_anInLrt; " = "; gc_oldRecordName; "."; g_anInLrt
     End If

     Print #fileNo, addTab(1); ";"

     Print #fileNo, "END"
     Print #fileNo, gc_sqlCmdDelim
 End Sub
 
 
 Private Sub genVirtualAttrTriggerForRel( _
   fileNo As Integer, _
   thisRelIndex As Integer, _
   ByRef qualTabName As String, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forLrt As Boolean = False, _
   Optional poolSupportLrt As Boolean = True, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   ' relationships are never updated; thus there is no need for an update trigger
     If g_relationships.descriptors(thisRelIndex).leftDependentAttribute <> "" Then
       genVirtualAttrTriggerForRelAndClass fileNo, g_relationships.descriptors(thisRelIndex).relIndex, qualTabName, getAttributeIndexByName(g_relationships.descriptors(thisRelIndex).leftClassSectionName, g_relationships.descriptors(thisRelIndex).leftDependentAttribute), _
         g_relationships.descriptors(thisRelIndex).leftEntityIndex, g_classes.descriptors(g_relationships.descriptors(thisRelIndex).leftEntityIndex).orMappingSuperClassIndex, genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(thisRelIndex).leftEntityIndex).shortName), _
         thisOrgIndex, thisPoolIndex, forLrt, poolSupportLrt, ddlType
     End If
 End Sub
 
 
 ' ### ENDIF IVK ###
 Sub genTransformedAttrDeclForRelationshipsByClassWithColReuse( _
   thisClassIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional forSubClass As Boolean = False, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional timeVaryingRels As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 1, _
   Optional addComma As Boolean = True, _
   Optional includeReusedRels As Boolean = False _
 )
   Dim i As Integer
   Dim attrSpecifics As String

   On Error GoTo ErrorExit

   Dim relShortName As String
   Dim relLdmShortName As String
   Dim relDirectedShortName As String
   Dim effectiveRelIndex As Integer
   Dim classHasNoIdentity As Boolean
   Dim supportedExpRel As Boolean
   Dim supportedNonGenRel As Boolean

   ' determine number of foreign key columns in this class
   Dim numFkAttrs As Integer
   numFkAttrs = 0

     classHasNoIdentity = g_classes.descriptors(thisClassIndex).hasNoIdentity
     For i = 1 To g_classes.descriptors(thisClassIndex).relRefs.numRefs Step 1
         'determine supported Expression Gen Relation
         If timeVaryingRels And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isTimeVarying And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isMdsExpressionRel And Not classHasNoIdentity Then
           supportedExpRel = True
         Else
           supportedExpRel = False
         End If
         'determine all non-Gen Relation (if generating nonGen, then only process non-TimeVarying attributes, or those for a noIdentity class)
         If Not timeVaryingRels And (Not g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isTimeVarying Or classHasNoIdentity) Then
           supportedNonGenRel = True
         Else
           supportedNonGenRel = False
         End If
         If IIf(supportNlForRelationships, Not g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isNl, True) And g_classes.descriptors(thisClassIndex).relRefs.refs(i).refType = etLeft And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxRightCardinality = 1 And (supportedNonGenRel Or supportedExpRel) Then
           If Not reuseRelationships Or g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex <= 0 Or includeReusedRels Then
             numFkAttrs = numFkAttrs + 1
           End If
         ElseIf IIf(supportNlForRelationships, Not g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isNl, True) And g_classes.descriptors(thisClassIndex).relRefs.refs(i).refType = etRight And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxRightCardinality <> 1 And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxLeftCardinality = 1 And (supportedNonGenRel Or supportedExpRel) Then
           If Not reuseRelationships Or g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex <= 0 Or includeReusedRels Then
             numFkAttrs = numFkAttrs + 1
           End If
         End If
     Next i

     For i = 1 To g_classes.descriptors(thisClassIndex).relRefs.numRefs Step 1
         'determine supported Expression Gen Relation
         If timeVaryingRels And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isTimeVarying And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isMdsExpressionRel And Not classHasNoIdentity Then
           supportedExpRel = True
         Else
           supportedExpRel = False
         End If
         'determine all non-Gen Relation (if generating nonGen, then only process non-TimeVarying attributes, or those for a noIdentity class)
         If Not timeVaryingRels And (Not g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isTimeVarying Or classHasNoIdentity) Then
           supportedNonGenRel = True
         Else
           supportedNonGenRel = False
         End If
         If IIf(supportNlForRelationships, Not g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isNl, True) And g_classes.descriptors(thisClassIndex).relRefs.refs(i).refType = etLeft And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxRightCardinality = 1 And (supportedNonGenRel Or supportedExpRel) Then
           attrSpecifics = IIf(forSubClass Or (g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minRightCardinality = 0), "", "NOT NULL")
           printSectionHeader """" & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).sectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relName & """ (" & _
                              g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).leftClassSectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).leftClassName & "[" & _
                              IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minLeftCardinality = 0 Or (g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minLeftCardinality = 1 And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxLeftCardinality <> 1), g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minLeftCardinality & "..", "") & _
                              IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxLeftCardinality = 1, "1", "n") & _
                              "] <-> " & _
                              g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rightClassSectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rightClassName & "[" & _
                              IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minRightCardinality = 0 Or (g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minRightCardinality = 1 And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxRightCardinality <> 1), g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minRightCardinality & "..", "") & _
                              IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxRightCardinality = 1, "1", "m") & _
                              "])", fileNo, outputMode
           printComment "Relationship """ & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).sectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relName & """(""" & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).lrRelName & """) : """ & _
                        g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).leftClassSectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).leftClassName & """ -> """ & _
                        g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rightClassSectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rightClassName & """", fileNo, outputMode
           If reuseRelationships And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex > 0 And Not includeReusedRels Then
               printComment "reusing foreign key for relationship """ & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).sectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).relName & """(""" & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).lrRelName & """) : """ & _
                            g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).leftClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).leftClassName & """ -> """ & _
                            g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).rightClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).rightClassName & """", fileNo, outputMode
           Else
             effectiveRelIndex = IIf(reuseRelationships And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex > 0, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relIndex)
               relShortName = g_relationships.descriptors(effectiveRelIndex).effectiveShortName
               relDirectedShortName = g_relationships.descriptors(effectiveRelIndex).lrShortRelName
               relLdmShortName = g_relationships.descriptors(effectiveRelIndex).lrLdmRelName
             ' FIXME: Parameter forLRTtab needs to be set ??
             genTransformedAttrDeclsForEntityWithColReUse _
               eactRelationship, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relIndex, transformation, tabColumns, forSubClass, fileNo, ddlType, thisOrgIndex, _
               thisPoolIndex, , False, False, True, , g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isUserTransactional, , , outputMode, indent
 ' ### IF IVK ###
             genFkTransformedAttrDeclsForRelationshipWithColReUse g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rightEntityIndex, _
               g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relIndex, IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).useLrLdmRelName, relLdmShortName, relShortName & relDirectedShortName), _
               Not g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).useLrLdmRelName, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
 ' ### ELSE IVK ###
 '           genFkTransformedAttrDeclsForRelationshipWithColReUse .rightEntityIndex, _
 '             .relIndex, IIf(.useLrLdmRelName, relLdmShortName, relShortName & relDirectedShortName), Not .useLrLdmRelName, _
 '             attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
 ' ### ENDIF IVK ###
             numFkAttrs = numFkAttrs - 1
           End If
         End If
         If IIf(supportNlForRelationships, Not g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isNl, True) And g_classes.descriptors(thisClassIndex).relRefs.refs(i).refType = etRight And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxRightCardinality <> 1 And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxLeftCardinality = 1 And (supportedNonGenRel Or supportedExpRel) Then
           attrSpecifics = IIf(forSubClass Or (g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minLeftCardinality = 0), "", "NOT NULL")
           printSectionHeader """" & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).sectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relName & """ (" & _
                              g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).leftClassSectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).leftClassName & "[" & _
                              IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minLeftCardinality = 0 Or (g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minLeftCardinality = 1 And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxLeftCardinality <> 1), g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minLeftCardinality & "..", "") & _
                              IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxLeftCardinality = 1, "1", "n") & _
                              "] <-> " & _
                              g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rightClassSectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rightClassName & "[" & _
                              IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minRightCardinality = 0 Or (g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minRightCardinality = 1 And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxRightCardinality <> 1), g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).minRightCardinality & "..", "") & _
                              IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).maxRightCardinality = 1, "1", "m") & _
                              "])", fileNo, outputMode
           printComment "Relationship """ & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).sectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relName & """(""" & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rlRelName & """) : """ & _
                        g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rightClassSectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).rightClassName & """ -> """ & _
                        g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).leftClassSectionName & "." & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).leftClassName & """", fileNo, outputMode
           If reuseRelationships And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex > 0 And Not includeReusedRels Then
               printComment "reusing foreign key for relationship """ & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).sectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).relName & """(""" & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).lrRelName & """) : """ & _
                            g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).leftClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).leftClassName & """ -> """ & _
                            g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).rightClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex).rightClassName & """", fileNo, outputMode
           Else
             effectiveRelIndex = IIf(reuseRelationships And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex > 0, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).reusedRelIndex, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relIndex)
               relShortName = g_relationships.descriptors(effectiveRelIndex).effectiveShortName
               relDirectedShortName = g_relationships.descriptors(effectiveRelIndex).rlShortRelName
               relLdmShortName = g_relationships.descriptors(effectiveRelIndex).rlLdmRelName

             ' FIXME: Parameter forLRTtab needs to be set ??
             genTransformedAttrDeclsForEntityWithColReUse _
               eactRelationship, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relIndex, transformation, tabColumns, forSubClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, _
               False, False, True, , , g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isUserTransactional, , , outputMode, indent, , , , , True
 ' ### IF IVK ###
             genFkTransformedAttrDeclsForRelationshipWithColReUse g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).leftEntityIndex, _
               g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).relIndex, IIf(g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).useRlLdmRelName, relLdmShortName, relShortName & relDirectedShortName), _
               Not g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).useRlLdmRelName, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(i).refIndex).isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, _
               indent, addComma Or (numFkAttrs > 1)
 ' ### ELSE IVK ###
 '           genFkTransformedAttrDeclsForRelationshipWithColReUse .leftEntityIndex, _
 '             .relIndex, IIf(.useRlLdmRelName, relLdmShortName, relShortName & relDirectedShortName), Not .useRlLdmRelName, _
 '             attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
 ' ### ENDIF IVK ###
             numFkAttrs = numFkAttrs - 1
           End If
         End If
 NextI:
     Next i
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrDeclForRelationshipsByRelWithColReuse( _
   thisRelIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 1, _
   Optional addComma As Boolean = True, _
   Optional includeReusedRels As Boolean = False _
 )
   Dim i As Integer
   Dim attrSpecifics As String

   Dim relShortName As String
   Dim relLdmShortName As String
   Dim relDirectedShortName As String
   Dim effectiveRelIndex As Integer

   On Error GoTo ErrorExit

   ' determine number of foreign key columns in this relationship
   Dim numFkAttrs As Integer
   numFkAttrs = 0
     For i = 1 To g_relationships.descriptors(thisRelIndex).relRefs.numRefs Step 1
         If IIf(supportNlForRelationships, Not g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).isNl, True) And g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refType = etLeft And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxRightCardinality = 1 Then
           If Not reuseRelationships Or g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex <= 0 Or includeReusedRels Then
             numFkAttrs = numFkAttrs + 1
           End If
         ElseIf IIf(supportNlForRelationships, Not g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).isNl, True) And g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refType = etRight And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxRightCardinality <> 1 And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxLeftCardinality = 1 Then
           If Not reuseRelationships Or g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex <= 0 Or includeReusedRels Then
             numFkAttrs = numFkAttrs + 1
           End If
         End If
     Next i

     For i = 1 To g_relationships.descriptors(thisRelIndex).relRefs.numRefs Step 1
         If IIf(supportNlForRelationships, Not g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).isNl, True) And g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refType = etLeft And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxRightCardinality = 1 Then
           attrSpecifics = IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minRightCardinality = 0, "", "NOT NULL")
           printSectionHeader """" & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).sectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relName & """ (" & _
                              g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).leftClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).leftClassName & "[" & _
                              IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minLeftCardinality = 0 Or (g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minLeftCardinality = 1 And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxLeftCardinality <> 1), g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minLeftCardinality & "..", "") & _
                              IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxLeftCardinality = 1, "1", "n") & _
                              "] <-> " & _
                              g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rightClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rightClassName & "[" & _
                              IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minRightCardinality = 0 Or (g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minRightCardinality = 1 And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxRightCardinality <> 1), g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minRightCardinality & "..", "") & _
                              IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxRightCardinality = 1, "1", "m") & _
                              "])", fileNo, outputMode
           printComment "Relationship """ & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).sectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relName & """(""" & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).lrRelName & """) : """ & _
                        g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).leftClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).leftClassName & """ -> """ & _
                        g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rightClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rightClassName & """", fileNo, outputMode
           If reuseRelationships And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex > 0 And Not includeReusedRels Then
               printComment "reusing foreign key for relationship """ & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).sectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).relName & """(""" & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).lrRelName & """) : """ & _
                            g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).leftClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).leftClassName & """ -> """ & _
                            g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).rightClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).rightClassName & """", fileNo, outputMode
           Else
             effectiveRelIndex = IIf(reuseRelationships And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex > 0, g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex, g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relIndex)
               relShortName = g_relationships.descriptors(effectiveRelIndex).effectiveShortName
               relDirectedShortName = g_relationships.descriptors(effectiveRelIndex).lrShortRelName
               relLdmShortName = g_relationships.descriptors(effectiveRelIndex).lrLdmRelName
             ' FIXME: Parameter forLRTtab needs to be set ??
             genTransformedAttrDeclsForEntityWithColReUse _
               eactRelationship, g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relIndex, transformation, tabColumns, False, fileNo, ddlType, thisOrgIndex, thisPoolIndex, _
               , False, False, True, , g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).isUserTransactional, , , outputMode, indent
 ' ### IF IVK ###
             genFkTransformedAttrDeclsForRelationshipWithColReUse g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rightEntityIndex, _
               g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relIndex, IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).useLrLdmRelName, relLdmShortName, relShortName & relDirectedShortName), _
               Not g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).useLrLdmRelName, g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, _
               indent, addComma Or (numFkAttrs > 1)
 ' ### ELSE IVK ###
 '           genFkTransformedAttrDeclsForRelationshipWithColReUse .rightEntityIndex, _
 '             .relIndex, IIf(.useLrLdmRelName, relLdmShortName, relShortName & relDirectedShortName), _
 '             Not .useLrLdmRelName, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
 ' ### ENDIF IVK ###
             numFkAttrs = numFkAttrs - 1
           End If
         End If
         If IIf(supportNlForRelationships, Not g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).isNl, True) And g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refType = etRight And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxRightCardinality <> 1 And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxLeftCardinality = 1 Then
           attrSpecifics = IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minLeftCardinality = 0, "", "NOT NULL")
           printSectionHeader """" & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).sectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relName & """ (" & _
                              g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).leftClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).leftClassName & "[" & _
                              IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minLeftCardinality = 0 Or (g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minLeftCardinality = 1 And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxLeftCardinality <> 1), g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minLeftCardinality & "..", "") & _
                              IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxLeftCardinality = 1, "1", "n") & _
                              "] <-> " & _
                              g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rightClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rightClassName & "[" & _
                              IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minRightCardinality = 0 Or (g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minRightCardinality = 1 And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxRightCardinality <> 1), g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).minRightCardinality & "..", "") & _
                              IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).maxRightCardinality = 1, "1", "m") & _
                              "])", fileNo, outputMode
           printComment "Relationship """ & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).sectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relName & """(""" & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rlRelName & """) : """ & _
                        g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rightClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).rightClassName & """ -> """ & _
                        g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).leftClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).leftClassName & """", fileNo, outputMode
           If reuseRelationships And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex > 0 And Not includeReusedRels Then
               printComment "reusing foreign key for relationship """ & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).sectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).relName & """(""" & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).lrRelName & """) : """ & _
                            g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).leftClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).leftClassName & """ -> """ & _
                            g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).rightClassSectionName & "." & g_relationships.descriptors(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex).rightClassName & """", fileNo, outputMode
           Else
             effectiveRelIndex = IIf(reuseRelationships And g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex > 0, g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).reusedRelIndex, g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relIndex)
               relShortName = g_relationships.descriptors(effectiveRelIndex).effectiveShortName
               relDirectedShortName = g_relationships.descriptors(effectiveRelIndex).rlShortRelName
               relLdmShortName = g_relationships.descriptors(effectiveRelIndex).rlLdmRelName

             ' FIXME: Parameter forLRTtab needs to be set ??
             genTransformedAttrDeclsForEntityWithColReUse _
               eactRelationship, g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relIndex, transformation, tabColumns, False, fileNo, ddlType, thisOrgIndex, thisPoolIndex, _
               False, False, True, , , g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).isUserTransactional, , , outputMode, indent, , , , , True
 ' ### IF IVK ###
             genFkTransformedAttrDeclsForRelationshipWithColReUse g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).leftEntityIndex, _
               g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).relIndex, IIf(g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).useRlLdmRelName, relLdmShortName, relShortName & relDirectedShortName), _
               Not g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).useRlLdmRelName, g_relationships.descriptors(g_relationships.descriptors(thisRelIndex).relRefs.refs(i).refIndex).isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
 ' ### ELSE IVK ###
 '           genFkTransformedAttrDeclsForRelationshipWithColReUse .leftEntityIndex, _
 '             .relIndex, IIf(.useRlLdmRelName, relLdmShortName, relShortName & relDirectedShortName), _
 '             Not .useRlLdmRelName, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent, addComma Or (numFkAttrs > 1)
 ' ### ENDIF IVK ###
             numFkAttrs = numFkAttrs - 1
           End If
         End If
 NextI:
     Next i
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 
 
 
 Sub genRelationshipsDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisRelIndex As Integer
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

     resetRelationshipsCsvExported

     If ddlType = edtLdm Then
       For thisRelIndex = 1 To g_relationships.numDescriptors Step 1
         genRelationshipDdl thisRelIndex, , , edtLdm
       Next thisRelIndex

       resetRelationshipsCsvExported
     ElseIf ddlType = edtPdm Then
       For thisRelIndex = 1 To g_relationships.numDescriptors Step 1
           If g_relationships.descriptors(thisRelIndex).isCommonToOrgs Then
             genRelationshipDdl thisRelIndex, , , edtPdm

             ' if there is some data pool which locally implements this relationship, take care of that
             For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
               If g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
                 For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
                   If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                     genRelationshipDdl thisRelIndex, thisOrgIndex, thisPoolIndex, edtPdm
                   End If
                 Next thisOrgIndex
               End If
             Next thisPoolIndex

           Else
             For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
               If g_relationships.descriptors(thisRelIndex).isCommonToPools Then
                 genRelationshipDdl thisRelIndex, thisOrgIndex, , edtPdm

                 ' if there is some data pool which locally implements this class, take care of that
                 For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                   If g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
                     If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                       genRelationshipDdl thisRelIndex, thisOrgIndex, thisPoolIndex, edtPdm
                     End If
                   End If
                 Next thisPoolIndex

               Else
                 For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
                   If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
                     genRelationshipDdl thisRelIndex, thisOrgIndex, thisPoolIndex, edtPdm
                   End If
                 Next thisPoolIndex
               End If
             Next thisOrgIndex
           End If
       Next thisRelIndex

       resetRelationshipsCsvExported
     End If
 End Sub
 
 
 Sub dropRelationshipsCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   killCsvFileWhereEver g_sectionIndexDbMeta, clnAcmEntity, g_targetDir, processingStepAcmCsv, onlyIfEmpty, "ACM"
 End Sub
 
 
 Sub genRelationshipAcmMetaCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnAcmEntity, processingStepAcmCsv, "ACM", ddlType)
   assertDir fileName
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim i As Integer
   For i = 1 To g_relationships.numDescriptors
       If Not g_relationships.descriptors(i).notAcmRelated Then
         Print #fileNo, """"; UCase(g_relationships.descriptors(i).sectionName); """,";
         Print #fileNo, """"; UCase(g_relationships.descriptors(i).relName); """,";
         Print #fileNo, """"; UCase(g_relationships.descriptors(i).shortName);
         Print #fileNo, """,""R"",";
         Print #fileNo, """"; g_relationships.descriptors(i).relIdStr; """,";
         Print #fileNo, """"; g_relationships.descriptors(i).i18nId; """,";
         Print #fileNo, IIf(g_relationships.descriptors(i).isCommonToOrgs, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).isCommonToPools, gc_dbTrue, gc_dbFalse); ",";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_relationships.descriptors(i).supportXmlExport, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).useXmlExport, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, IIf(g_relationships.descriptors(i).aggHeadClassIdStr <> "", """" & g_relationships.descriptors(i).aggHeadClassIdStr & """", ""); ",";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_relationships.descriptors(i).noFto, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, IIf(g_relationships.descriptors(i).isUserTransactional, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).isLrtMeta, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).isUserTransactional And g_relationships.descriptors(i).useMqtToImplementLrt, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(False, gc_dbTrue, gc_dbFalse); ",";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_relationships.descriptors(i).lrtActivationStatusMode <> "", """" & g_relationships.descriptors(i).lrtActivationStatusMode & """", ""); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).lrtClassification <> "", """" & g_relationships.descriptors(i).lrtClassification & """", ""); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).isSubjectToArchiving, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, "0,";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_relationships.descriptors(i).isPsTagged, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).isPsForming, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, IIf(g_relationships.descriptors(i).logLastChange, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, "0,";
 ' ### IF IVK ###
         Print #fileNo, IIf(g_relationships.descriptors(i).isSubjectToPreisDurchschuss, gc_dbTrue, gc_dbFalse); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).isUserTransactional And g_relationships.descriptors(i).hasOrganizationSpecificReference, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
         Print #fileNo, IIf(g_relationships.descriptors(i).ignoreForChangelog, gc_dbTrue, gc_dbFalse); ",";
 ' ### IF IVK ###
         Print #fileNo, "0,0,,";
 ' ### ENDIF IVK ###
         Print #fileNo, ",,,,,,";
         Print #fileNo, IIf(g_relationships.descriptors(i).reuseName = "", "", """" & UCase(g_relationships.descriptors(i).reuseName) & """"); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).reuseShortName = "", "", """" & UCase(g_relationships.descriptors(i).reuseShortName) & """"); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).isNotEnforced, gc_dbFalse, gc_dbTrue); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).rlShortRelName = "", "", """" & UCase(g_relationships.descriptors(i).rlShortRelName) & """"); ",";
         Print #fileNo, CStr(g_relationships.descriptors(i).minLeftCardinality); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).maxLeftCardinality > 0, g_relationships.descriptors(i).maxLeftCardinality & "", ""); ",";
         Print #fileNo, """"; UCase(g_relationships.descriptors(i).leftClassSectionName); """,";
         Print #fileNo, """"; UCase(g_relationships.descriptors(i).leftClassName); """,";
         Print #fileNo, """"; getAcmEntityTypeKey(g_relationships.descriptors(i).leftEntityType); """,";
         Print #fileNo, IIf(g_relationships.descriptors(i).lrShortRelName = "", "", """" & UCase(g_relationships.descriptors(i).lrShortRelName) & """"); ",";
         Print #fileNo, CStr(g_relationships.descriptors(i).minRightCardinality); ",";
         Print #fileNo, IIf(g_relationships.descriptors(i).maxRightCardinality > 0, g_relationships.descriptors(i).maxRightCardinality & "", ""); ",";
         Print #fileNo, """"; UCase(g_relationships.descriptors(i).rightClassSectionName); """,";
         Print #fileNo, """"; UCase(g_relationships.descriptors(i).rightClassName); """,";
         Print #fileNo, """"; getAcmEntityTypeKey(g_relationships.descriptors(i).rightEntityType); ""","; getCsvTrailer(0)
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
 
 
 Sub evalRelationships()
   Dim i As Integer, j As Integer

     Dim leftClass As ClassDescriptor
     Dim rightclass As ClassDescriptor

     For i = 1 To g_relationships.numDescriptors Step 1
         ' determine TableSpaces
         g_relationships.descriptors(i).tabSpaceIndexData = IIf(g_relationships.descriptors(i).tabSpaceData <> "", getTableSpaceIndexByName(g_relationships.descriptors(i).tabSpaceData), -1)
         g_relationships.descriptors(i).tabSpaceIndexIndex = IIf(g_relationships.descriptors(i).tabSpaceIndex <> "", getTableSpaceIndexByName(g_relationships.descriptors(i).tabSpaceIndex), -1)
         g_relationships.descriptors(i).tabSpaceIndexLong = IIf(g_relationships.descriptors(i).tabSpaceLong <> "", getTableSpaceIndexByName(g_relationships.descriptors(i).tabSpaceLong), -1)
         g_relationships.descriptors(i).tabSpaceIndexNl = IIf(g_relationships.descriptors(i).tabSpaceNl <> "", getTableSpaceIndexByName(g_relationships.descriptors(i).tabSpaceNl), -1)
         g_relationships.descriptors(i).useValueCompression = g_relationships.descriptors(i).useValueCompression And dbCompressValues

         ' initialize variables
         g_relationships.descriptors(i).hasLabel = False
 ' ### IF IVK ###
         g_relationships.descriptors(i).hasIsNationalInclSubClasses = g_relationships.descriptors(i).isNationalizable
 ' ### ENDIF IVK ###

         g_relationships.descriptors(i).aggHeadClassIndex = -1
         g_relationships.descriptors(i).aggHeadClassIndexExact = -1
         g_relationships.descriptors(i).aggHeadClassIdStr = ""
         If Not g_relationships.descriptors(i).notAcmRelated Then
           If g_relationships.descriptors(i).aggHeadSection <> "" And g_relationships.descriptors(i).aggHeadName <> "" Then
             g_relationships.descriptors(i).aggHeadClassIndex = getClassIndexByName(g_relationships.descriptors(i).aggHeadSection, g_relationships.descriptors(i).aggHeadName)
             If g_relationships.descriptors(i).aggHeadClassIndex <= 0 Then
               logMsg "unable to identify aggregate head class '" & g_relationships.descriptors(i).aggHeadSection & "." & g_relationships.descriptors(i).aggHeadName & "'", ellError
             Else
               g_relationships.descriptors(i).aggHeadClassIdStr = getClassIdByClassIndex(g_relationships.descriptors(i).aggHeadClassIndex)
             End If
             g_relationships.descriptors(i).aggHeadClassIndexExact = g_relationships.descriptors(i).aggHeadClassIndex
           End If
         End If

         ' determine references to indexes
         g_relationships.descriptors(i).indexRefs.numRefs = 0
         For j = 1 To g_indexes.numDescriptors Step 1
             If UCase(g_relationships.descriptors(i).sectionName) = UCase(g_indexes.descriptors(j).sectionName) And _
                UCase(g_relationships.descriptors(i).relName) = UCase(g_indexes.descriptors(j).className) Then
               g_relationships.descriptors(i).indexRefs.refs(allocIndexDescriptorRefIndex(g_relationships.descriptors(i).indexRefs)) = j
             End If
         Next j

         ' determine reference to section
         g_relationships.descriptors(i).sectionIndex = getSectionIndexByName(g_relationships.descriptors(i).sectionName)
         If g_relationships.descriptors(i).sectionIndex > 0 Then
           g_relationships.descriptors(i).sectionShortName = g_sections.descriptors(g_relationships.descriptors(i).sectionIndex).shortName
         End If

         If g_relationships.descriptors(i).tabSpaceIndexData > 0 Then
           If g_tableSpaces.descriptors(g_relationships.descriptors(i).tabSpaceIndexData).category = tscSms Then
             If g_relationships.descriptors(i).tabSpaceIndexIndex > 0 And g_relationships.descriptors(i).tabSpaceIndexIndex <> g_relationships.descriptors(i).tabSpaceIndexData Then
               g_relationships.descriptors(i).tabSpaceIndexIndex = g_relationships.descriptors(i).tabSpaceIndexData
               logMsg "index table space """ & g_relationships.descriptors(i).tabSpaceIndex & """ for relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """" & _
                      " must be identical to data table space since data table space is ""SMS"" - fixed", ellFixableWarning
             End If
             If g_relationships.descriptors(i).tabSpaceIndexLong > 0 And g_relationships.descriptors(i).tabSpaceIndexLong <> g_relationships.descriptors(i).tabSpaceIndexData Then
               g_relationships.descriptors(i).tabSpaceIndexLong = g_relationships.descriptors(i).tabSpaceIndexData
               logMsg "long table space """ & g_relationships.descriptors(i).tabSpaceLong & """ for relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """" & _
                      " must be identical to data table space since data table space is ""SMS"" - fixed", ellFixableWarning
             End If
           End If
         End If

         ' confirm that relationship name is unique
         For j = 1 To i - 1 Step 1
           If UCase(g_relationships.descriptors(i).sectionName) = UCase(g_relationships.descriptors(j).sectionName) And _
              UCase(g_relationships.descriptors(i).relName) = UCase(g_relationships.descriptors(j).relName) Then
             logMsg "relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """ is not unque", ellFatal
           End If
         Next j
 ' ### IF IVK ###

         ' determine whether class supports XML-export
         If g_relationships.descriptors(i).noXmlExport Then
           g_relationships.descriptors(i).supportXmlExport = False
         ElseIf g_relationships.descriptors(i).isCommonToPools Or g_relationships.descriptors(i).isCommonToOrgs Then
           g_relationships.descriptors(i).supportXmlExport = True
         Else
           If g_relationships.descriptors(i).specificToPool >= 0 Then
             If g_pools.descriptors(g_relationships.descriptors(i).specificToPool).supportXmlExport Then
               g_relationships.descriptors(i).supportXmlExport = True
             End If
           Else
             g_relationships.descriptors(i).supportXmlExport = True
           End If
         End If
 ' ### ENDIF IVK ###
     Next i

     For i = 1 To g_relationships.numDescriptors Step 1
         ' verify consistency of aggregate heads with object relational mapping
         If g_relationships.descriptors(i).aggHeadClassIndex > 0 Then
           If g_relationships.descriptors(i).aggHeadClassIndex <> g_classes.descriptors(g_relationships.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex Then
             logMsg "potential inconsistency: aggregate head of relationship '" & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & "' is not identical to its 'OR-mapping parent class' " & _
                    "'" & g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).sectionName & "." & _
                    g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).className & "'", ellInfo
             g_relationships.descriptors(i).aggHeadClassIndex = g_classes.descriptors(g_relationships.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex
             g_relationships.descriptors(i).aggHeadSection = g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).sectionName
             g_relationships.descriptors(i).aggHeadName = g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).className
             g_relationships.descriptors(i).aggHeadClassIdStr = g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(i).aggHeadClassIndex).orMappingSuperClassIndex).classIdStr
           End If
         End If
 ' ### IF IVK ###

         ' determine whether aggregate head is price assignment
         If g_relationships.descriptors(i).aggHeadClassIndexExact > 0 Then
           g_relationships.descriptors(i).hasPriceAssignmentAggHead = g_classes.descriptors(g_relationships.descriptors(i).aggHeadClassIndexExact).hasPriceAssignmentSubClass
         ElseIf g_relationships.descriptors(i).aggHeadClassIndex > 0 Then
           g_relationships.descriptors(i).hasPriceAssignmentAggHead = g_classes.descriptors(g_relationships.descriptors(i).aggHeadClassIndex).hasPriceAssignmentSubClass
         End If
 ' ### ENDIF IVK ###
     Next i

     For i = 1 To g_relationships.numDescriptors Step 1
         g_relationships.descriptors(i).relIndex = i

         If g_relationships.descriptors(i).fkReferenceOrgId > 0 Then
           g_relationships.descriptors(i).fkReferenceOrgIndex = getOrgIndexById(g_relationships.descriptors(i).fkReferenceOrgId)
         End If
         If g_relationships.descriptors(i).fkReferencePoolId > 0 Then
           g_relationships.descriptors(i).fkReferencePoolIndex = getOrgIndexById(g_relationships.descriptors(i).fkReferencePoolId)
         End If
         ' determine relationship ID as string
         g_relationships.descriptors(i).relIdStr = getRelIdByIndex(i)

         If g_relationships.descriptors(i).isUserTransactional And (g_relationships.descriptors(i).isCommonToPools Or g_relationships.descriptors(i).isCommonToOrgs) Then
           logMsg "relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "has stereotype <lrt> but is common to " & IIf(g_relationships.descriptors(i).isCommonToOrgs, "organizations (cto)", "pools (ctp)") & " - fixed", _
                  ellFixableWarning
           g_relationships.descriptors(i).isUserTransactional = False
         End If

 ' ### IF IVK ###
         If g_relationships.descriptors(i).isPsForming And Not g_relationships.descriptors(i).isUserTransactional And Not g_relationships.descriptors(i).isCommonToPools Then
           logMsg "potential inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "relationship is 'PS-forming' but does not have stereotype <lrt>", _
                  ellInfo
         End If

 ' ### ENDIF IVK ###
         If g_relationships.descriptors(i).isUserTransactional And g_relationships.descriptors(i).logLastChange And Not g_relationships.descriptors(i).logLastChangeInView Then
           logMsg "inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "relationship has stereotypes <logChange> and <lrt> but does not support 'logChangeInView' - fixed", _
                  ellFixableWarning
           g_relationships.descriptors(i).logLastChangeInView = True
         End If

 ' ### IF IVK ###
         If g_relationships.descriptors(i).isPsTagged And g_relationships.descriptors(i).logLastChange And Not g_relationships.descriptors(i).logLastChangeInView Then
           logMsg "inconsistency with class """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "relationship has stereotypes <logChange> and <ps> but does not support 'logChangeInView' - fixed", _
                  ellFixableWarning
           g_relationships.descriptors(i).logLastChangeInView = True
         End If

 ' ### ENDIF IVK ###
         g_relationships.descriptors(i).leftEntityIndex = getClassIndexByName(g_relationships.descriptors(i).leftClassSectionName, g_relationships.descriptors(i).leftClassName, True)
         If g_relationships.descriptors(i).leftEntityIndex > 0 Then
 ' ### IF IVK ###
           g_relationships.descriptors(i).leftIsDivision = (g_relationships.descriptors(i).leftEntityIndex = g_classIndexDivision)
 ' ### ENDIF IVK ###
           leftClass = getClassByIndex(g_relationships.descriptors(i).leftEntityIndex)
 ' ### IF IVK ###
           g_relationships.descriptors(i).leftIsSubjectToArchiving = leftClass.isSubjectToArchiving
 ' ### ENDIF IVK ###
           g_relationships.descriptors(i).leftEntityType = eactClass
           g_relationships.descriptors(i).leftEntityShortName = leftClass.shortName
         Else
           g_relationships.descriptors(i).leftEntityIndex = getRelIndexByName(g_relationships.descriptors(i).leftClassSectionName, g_relationships.descriptors(i).leftClassName)
 ' ### IF IVK ###
           g_relationships.descriptors(i).leftIsSubjectToArchiving = g_relationships.descriptors(g_relationships.descriptors(i).leftEntityIndex).isSubjectToArchiving
 ' ### ENDIF IVK ###
           If g_relationships.descriptors(i).leftEntityIndex > 0 Then
             g_relationships.descriptors(i).leftEntityType = eactRelationship
             g_relationships.descriptors(i).leftEntityShortName = g_relationships.descriptors(g_relationships.descriptors(i).leftEntityIndex).shortName
           End If
         End If

         g_relationships.descriptors(i).rightEntityIndex = getClassIndexByName(g_relationships.descriptors(i).rightClassSectionName, g_relationships.descriptors(i).rightClassName, True)
         If g_relationships.descriptors(i).rightEntityIndex > 0 Then
 ' ### IF IVK ###
           g_relationships.descriptors(i).rightIsDivision = (g_relationships.descriptors(i).rightEntityIndex = g_classIndexDivision)
 ' ### ENDIF IVK ###
           rightclass = getClassByIndex(g_relationships.descriptors(i).rightEntityIndex)
 ' ### IF IVK ###
           g_relationships.descriptors(i).rightIsSubjectToArchiving = rightclass.isSubjectToArchiving
 ' ### ENDIF IVK ###
           g_relationships.descriptors(i).rightEntityIndex = getClassIndexByName(g_relationships.descriptors(i).rightClassSectionName, g_relationships.descriptors(i).rightClassName, True)
           g_relationships.descriptors(i).rightEntityType = eactClass
           g_relationships.descriptors(i).rightEntityShortName = rightclass.shortName
         Else
           g_relationships.descriptors(i).rightEntityIndex = getRelIndexByName(g_relationships.descriptors(i).rightClassSectionName, g_relationships.descriptors(i).rightClassName)
 ' ### IF IVK ###
           g_relationships.descriptors(i).rightIsSubjectToArchiving = g_relationships.descriptors(g_relationships.descriptors(i).rightEntityIndex).isSubjectToArchiving
 ' ### ENDIF IVK ###
           If g_relationships.descriptors(i).rightEntityIndex > 0 Then
             g_relationships.descriptors(i).rightEntityType = eactRelationship
             g_relationships.descriptors(i).rightEntityShortName = g_relationships.descriptors(g_relationships.descriptors(i).rightEntityIndex).shortName
           End If
         End If

         g_relationships.descriptors(i).attrRefs.numDescriptors = 0
 ' ### IF IVK ###
         g_relationships.descriptors(i).isSubjectToArchiving = g_relationships.descriptors(i).leftIsSubjectToArchiving Or g_relationships.descriptors(i).rightIsSubjectToArchiving
 ' ### ENDIF IVK ###

         If g_relationships.descriptors(i).rightEntityIndex <= 0 Then
           logMsg "Unable to identify 'right' class """ & g_relationships.descriptors(i).rightClassSectionName & "." & g_relationships.descriptors(i).rightClassName & _
                  """ of relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """", IIf(g_relationships.descriptors(i).maxRightCardinality = 1, ellWarning, ellError)
           GoTo NextI
         End If
         If g_relationships.descriptors(i).leftEntityIndex <= 0 Then
           logMsg "Unable to identify 'left' class """ & g_relationships.descriptors(i).leftClassSectionName & "." & g_relationships.descriptors(i).leftClassName & _
                  """ of relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """", IIf(g_relationships.descriptors(i).maxRightCardinality = 1, ellWarning, ellError)
           GoTo NextI
         End If

 ' ### IF IVK ###
         If g_relationships.descriptors(i).specificToOrgId >= 0 And Not g_relationships.descriptors(i).noFto Then
           logMsg "relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "is specific to MPC " & g_relationships.descriptors(i).specificToOrgId & " but does not have stereotype <nt2m> (no transfer to MPC) - fixed", _
                  ellFixableWarning
           g_relationships.descriptors(i).noFto = True
         ElseIf g_relationships.descriptors(i).specificToPool >= 0 And Not g_relationships.descriptors(i).noTransferToProduction Then
           logMsg "relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "is specific to pool " & g_relationships.descriptors(i).specificToPool & " but does not have stereotype <nt2p> (no transfer to production) - fixed", _
                  ellFixableWarning
           g_relationships.descriptors(i).noTransferToProduction = True
         End If

         If g_relationships.descriptors(i).isCommonToOrgs And Not g_relationships.descriptors(i).noFto Then
           logMsg "relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "is common to organizations (cto) but does not have stereotype <nt2m> (no transfer to MPC) - fixed", _
                  ellFixableWarning
           g_relationships.descriptors(i).noFto = True
         ElseIf g_relationships.descriptors(i).isCommonToPools And Not g_relationships.descriptors(i).noTransferToProduction Then
           logMsg "relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "is common to pools (ctp) but does not have stereotype <nt2p> (no transfer to production) - fixed", _
                  ellFixableWarning
           g_relationships.descriptors(i).noTransferToProduction = True
         End If

         If leftClass.noFto And rightclass.noFto And Not g_relationships.descriptors(i).noFto Then
           logMsg "inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "referred classes """ & leftClass.sectionName & "." & leftClass.className & """ and " & _
                  """" & rightclass.sectionName & "." & rightclass.className & """ have stereotype <nt2m> but relationship has not", _
                  ellWarning
         End If

         If leftClass.noTransferToProduction And rightclass.noTransferToProduction And Not g_relationships.descriptors(i).noTransferToProduction Then
           logMsg "inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "referred classes """ & leftClass.sectionName & "." & leftClass.className & """ and " & _
                  """" & rightclass.sectionName & "." & rightclass.className & """ have stereotype <nt2p> but relationship has not", _
                  ellWarning
         End If

         ' check if relationship needs to be considered PS-tagged
         g_relationships.descriptors(i).isPsTagged = g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged Or g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged

 ' ### ENDIF IVK ###
         If g_relationships.descriptors(i).maxLeftCardinality = 1 And leftClass.isUserTransactional And Not rightclass.isUserTransactional Then
           logMsg "potential inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "referred class """ & leftClass.sectionName & "." & leftClass.className & """ is user-transactional " & _
                  "but referred class """ & rightclass.sectionName & "." & rightclass.className & """ is not", _
                  ellWarning
         ElseIf g_relationships.descriptors(i).maxRightCardinality = 1 And rightclass.isUserTransactional And Not leftClass.isUserTransactional Then
           logMsg "potential inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "referred class """ & rightclass.sectionName & "." & rightclass.className & """ is user-transactional " & _
                  "but referred class """ & leftClass.sectionName & "." & leftClass.className & """ is not", _
                  ellWarning
         ElseIf g_relationships.descriptors(i).maxLeftCardinality = 1 And leftClass.isUserTransactional And Not g_relationships.descriptors(i).isUserTransactional Then
           logMsg "potential inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "referred class """ & leftClass.sectionName & "." & leftClass.className & """ is user-transactional " & _
                  "but relationship is not", _
                  ellWarning
         ElseIf g_relationships.descriptors(i).maxRightCardinality = 1 And rightclass.isUserTransactional And Not g_relationships.descriptors(i).isUserTransactional Then
           logMsg "potential inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "referred class """ & rightclass.sectionName & "." & rightclass.className & """ is user-transactional " & _
                  "but relationship is not", _
                  ellWarning
         End If
         If leftClass.isCommonToOrgs = rightclass.isCommonToOrgs And leftClass.isCommonToOrgs <> g_relationships.descriptors(i).isCommonToOrgs Then
           logMsg "potential inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "referred classes """ & leftClass.sectionName & "." & leftClass.className & """ and """ & _
                  rightclass.sectionName & "." & rightclass.className & """ are " & IIf(g_relationships.descriptors(i).isCommonToOrgs, "not ", "") & "common to MPCs " & _
                  "but relationship is" & IIf(g_relationships.descriptors(i).isCommonToOrgs, "", " not"), _
                  ellWarning
         End If
         If leftClass.isCommonToPools = rightclass.isCommonToPools And leftClass.isCommonToPools <> g_relationships.descriptors(i).isCommonToPools Then
           logMsg "potential inconsistency with relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "referred classes """ & leftClass.sectionName & "." & leftClass.className & """ and """ & _
                  rightclass.sectionName & "." & rightclass.className & """ are " & IIf(g_relationships.descriptors(i).isCommonToPools, "not ", "") & "common to Pools " & _
                  "but relationship is" & IIf(g_relationships.descriptors(i).isCommonToPools, "", " not"), _
                  ellWarning
         End If

         If (Not supportNlForRelationships) Or (Not g_relationships.descriptors(i).isNl) Then
           If (g_relationships.descriptors(i).maxRightCardinality = 1) Then
             g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).numRelBasedFkAttrs = g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).numRelBasedFkAttrs + 1
           ElseIf (g_relationships.descriptors(i).maxLeftCardinality = 1) Then
             g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).numRelBasedFkAttrs = g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).numRelBasedFkAttrs + 1
           End If
         End If
 
 ' ### IF IVK ###
         ' Fixme: get rid of hard coded relatioship names
         If InStr(UCase(g_relationships.descriptors(i).relName), "DISALLOWEDCOUNTRIESLIST") Then
           g_relationships.descriptors(i).isDisallowedCountriesList = IIf(InStr(UCase(leftClass.className), "COUNTRYSPEC"), ernmLeft, ernmRight)
         ElseIf InStr(UCase(g_relationships.descriptors(i).relName), "ALLOWEDCOUNTRIESLIST") Then
           g_relationships.descriptors(i).isAllowedCountriesList = IIf(InStr(UCase(leftClass.className), "COUNTRYSPEC"), ernmLeft, ernmRight)
         ElseIf InStr(UCase(g_relationships.descriptors(i).relName), "DISALLOWEDCOUNTRIES") Then
           g_relationships.descriptors(i).isDisallowedCountries = IIf(InStr(UCase(leftClass.className), "COUNTRYSPEC"), ernmLeft, ernmRight)
         ElseIf InStr(UCase(g_relationships.descriptors(i).relName), "ALLOWEDCOUNTRIES") Then
           g_relationships.descriptors(i).isAllowedCountries = IIf(InStr(UCase(leftClass.className), "COUNTRYSPEC"), ernmLeft, ernmRight)
         End If

         If g_relationships.descriptors(i).navPathStrToDivision <> "" Then
           genNavPathForRelationship i, g_relationships.descriptors(i).navPathToDiv, g_relationships.descriptors(i).navPathStrToDivision
         End If

 ' ### ENDIF IVK ###
         addAggChildRelIndex g_relationships.descriptors(i).aggHeadClassIndex, g_relationships.descriptors(i).relIndex

 ' ### IF IVK ###
         If g_relationships.descriptors(i).supportExtendedPsCopy And Not g_relationships.descriptors(i).isPsTagged Then
           logMsg "relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "is tagged to 'support PSCOPY' but is not PS-tagged - fixed", _
                  ellFixableWarning
           g_relationships.descriptors(i).supportExtendedPsCopy = False
         End If

         If g_relationships.descriptors(i).supportExtendedPsCopy And (g_relationships.descriptors(i).isCommonToPools Or g_relationships.descriptors(i).isCommonToOrgs) Then
           logMsg "relationship """ & g_relationships.descriptors(i).sectionName & "." & g_relationships.descriptors(i).relName & """: " & _
                  "is tagged to 'support PSCOPY' is but common " & IIf(g_relationships.descriptors(i).isCommonToOrgs, "organizations (cto)", "pools (ctp)"), _
                  ellFixableWarning
           g_relationships.descriptors(i).supportExtendedPsCopy = False
         End If

 ' ### ENDIF IVK ###

 NextI:
     Next i

     For i = 1 To g_relationships.numDescriptors Step 1
         g_relationships.descriptors(i).relRefs.numRefs = 0
         For j = 1 To g_relationships.numDescriptors Step 1
             If UCase(g_relationships.descriptors(i).sectionName) = UCase(g_relationships.descriptors(j).leftClassSectionName) And _
                UCase(g_relationships.descriptors(i).relName) = UCase(g_relationships.descriptors(j).leftClassName) Then

                 g_relationships.descriptors(i).relRefs.refs(allocRelDescriptorRefIndex(g_relationships.descriptors(i).relRefs)).refIndex = j
                 g_relationships.descriptors(i).relRefs.refs(allocRelDescriptorRefIndex(g_relationships.descriptors(i).relRefs)).refType = etLeft
             ElseIf UCase(g_relationships.descriptors(i).sectionName) = UCase(g_relationships.descriptors(j).rightClassSectionName) And _
                UCase(g_relationships.descriptors(i).relName) = UCase(g_relationships.descriptors(j).rightClassName) And _
                g_relationships.descriptors(i).rightEntityType = eactRelationship Then

                 g_relationships.descriptors(i).relRefs.refs(allocRelDescriptorRefIndex(g_relationships.descriptors(i).relRefs)).refIndex = j
                 g_relationships.descriptors(i).relRefs.refs(allocRelDescriptorRefIndex(g_relationships.descriptors(i).relRefs)).refType = etRight
             End If
         Next j
     Next i

     For i = 1 To g_relationships.numDescriptors Step 1
         For j = 1 To g_attributes.numDescriptors Step 1
             If UCase(g_relationships.descriptors(i).sectionName) = UCase(g_attributes.descriptors(j).sectionName) And _
                UCase(g_relationships.descriptors(i).relName) = UCase(g_attributes.descriptors(j).className) And _
                g_attributes.descriptors(j).cType = eactRelationship Then

               g_attributes.descriptors(j).acmEntityIndex = i
               g_attributes.descriptors(j).isPdmSpecific = g_attributes.descriptors(j).isPdmSpecific Or g_relationships.descriptors(i).isPdmSpecific
               If Not g_relationships.descriptors(i).notAcmRelated Then
                 g_attributes.descriptors(j).isNotAcmRelated = False
               End If

                   If g_attributes.descriptors(j).isTimeVarying Then
                     logMsg "stereotype <tv> for attribute """ & g_attributes.descriptors(j).attributeName & """ at relationship """ & g_attributes.descriptors(j).className & """ is not supported - fixed", ellFixableWarning
                     g_attributes.descriptors(j).isTimeVarying = False
                   End If
                 If g_attributes.descriptors(j).valueType = eavtEnum Then
                   g_relationships.descriptors(i).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_relationships.descriptors(i).attrRefs)).refType = eadrtEnum
                 Else
                   g_relationships.descriptors(i).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_relationships.descriptors(i).attrRefs)).refType = eadrtAttribute
                 End If
                 g_relationships.descriptors(i).attrRefs.descriptors(allocAttrDescriptorRefIndex(g_relationships.descriptors(i).attrRefs)).refIndex = j
               If g_attributes.descriptors(j).isNl Then
                   g_relationships.descriptors(i).nlAttrRefs.descriptors(allocAttrDescriptorRefIndex(g_relationships.descriptors(i).nlAttrRefs)) = g_relationships.descriptors(i).attrRefs.descriptors(g_relationships.descriptors(i).attrRefs.numDescriptors)
               End If
             End If
         Next j
     Next i
 
   ' identify attributes which may be 'reused' (mapped to the same column) based on the OR-mapping rules
   Dim relIndex As Integer
   Dim matchRelIndex As Integer

   Dim relationship As RelationshipDescriptor
   Dim matchRelationship As RelationshipDescriptor

   Dim matchLeftClass As ClassDescriptor
   Dim matchRightClass As ClassDescriptor
   ' loop over all relationships being mapped to a foreign key; try to match against any other relationship being mapped to a foreign key to the same table
   For relIndex = 1 To g_relationships.numDescriptors Step 1
     relationship = g_relationships.descriptors(relIndex)

     leftClass = getClassByIndex(relationship.leftEntityIndex)
     rightclass = getClassByIndex(relationship.rightEntityIndex)

     If leftClass.notAcmRelated Or rightclass.notAcmRelated Then
       GoTo NextRel
     End If
 
     If relationship.maxLeftCardinality = 1 Or relationship.maxRightCardinality = 1 Then
       ' loop over all relationships potentially mapping to the same foreign key
       For matchRelIndex = 1 To relIndex - 1 Step 1
         matchRelationship = g_relationships.descriptors(matchRelIndex)

         If matchRelationship.maxLeftCardinality = 1 Or matchRelationship.maxRightCardinality = 1 Then
           matchLeftClass = getClassByIndex(matchRelationship.leftEntityIndex)
           matchRightClass = getClassByIndex(matchRelationship.rightEntityIndex)

           If relationship.maxLeftCardinality = 1 Then
             If matchRelationship.maxLeftCardinality = 1 And _
                leftClass.orMappingSuperClassIndex = matchLeftClass.orMappingSuperClassIndex And _
                rightclass.orMappingSuperClassIndex = matchRightClass.orMappingSuperClassIndex Then
               If (relationship.reuseName <> "") And (relationship.reuseName = matchRelationship.reuseName) Then
                 setRelationshipReusedRelIndex relIndex, matchRelIndex
                 GoTo NextRel
               Else
                 If relationship.reuseName = "" Then
                   logMsg "potential candidates for reuse of foreign key attribute: relationships """ & relationship.relName & """/""" & matchRelationship.relName & """", ellInfo
                 End If
               End If
             ElseIf matchRelationship.maxRightCardinality = 1 And _
                leftClass.orMappingSuperClassIndex = matchRightClass.orMappingSuperClassIndex And _
                rightclass.orMappingSuperClassIndex = matchLeftClass.orMappingSuperClassIndex Then
               If (relationship.reuseName <> "") And (relationship.reuseName = matchRelationship.reuseName) Then
                 setRelationshipReusedRelIndex relIndex, matchRelIndex
                 GoTo NextRel
               Else
                 If relationship.reuseName = "" Then
                   logMsg "potential candidates for reuse of foreign key attribute: relationships """ & relationship.relName & """/""" & matchRelationship.relName & """", ellInfo
                 End If
               End If
             End If
           ElseIf relationship.maxRightCardinality = 1 Then
             If matchRelationship.maxLeftCardinality = 1 And _
                leftClass.orMappingSuperClassIndex = matchRightClass.orMappingSuperClassIndex And _
                rightclass.orMappingSuperClassIndex = matchLeftClass.orMappingSuperClassIndex Then
               If (relationship.reuseName <> "") And (relationship.reuseName = matchRelationship.reuseName) Then
                 setRelationshipReusedRelIndex relIndex, matchRelIndex
                 GoTo NextRel
               Else
                 If relationship.reuseName = "" Then
                   logMsg "potential candidates for reuse of foreign key attribute: relationships """ & relationship.relName & """/""" & matchRelationship.relName & """", ellInfo
                 End If
               End If
             ElseIf matchRelationship.maxRightCardinality = 1 And _
                leftClass.orMappingSuperClassIndex = matchLeftClass.orMappingSuperClassIndex And _
                rightclass.orMappingSuperClassIndex = matchRightClass.orMappingSuperClassIndex Then
               If (relationship.reuseName <> "") And (relationship.reuseName = matchRelationship.reuseName) Then
                 setRelationshipReusedRelIndex relIndex, matchRelIndex
                 GoTo NextRel
               Else
                 If relationship.reuseName = "" Then
                   logMsg "potential candidates for reuse of foreign key attribute: relationships """ & relationship.relName & """/""" & matchRelationship.relName & """", ellInfo
                 End If
               End If
             End If
           End If
         End If
       Next matchRelIndex
     Else
       ' relationship.maxLeftCardinality = -1 And relationship.maxRightCardinality = -1
       ' loop over all relationships potentially mapping to the same relationship table
       For matchRelIndex = 1 To relIndex - 1 Step 1
         matchRelationship = g_relationships.descriptors(matchRelIndex)
         If matchRelationship.maxLeftCardinality = -1 And matchRelationship.maxRightCardinality = -1 Then
           matchLeftClass = getClassByIndex(matchRelationship.leftEntityIndex)
           matchRightClass = getClassByIndex(matchRelationship.rightEntityIndex)
           If (leftClass.orMappingSuperClassIndex = matchLeftClass.orMappingSuperClassIndex And _
                rightclass.orMappingSuperClassIndex = matchRightClass.orMappingSuperClassIndex) Or _
              (leftClass.orMappingSuperClassIndex = matchRightClass.orMappingSuperClassIndex And _
                rightclass.orMappingSuperClassIndex = matchLeftClass.orMappingSuperClassIndex) Then
             If (relationship.reuseName <> "") And (relationship.reuseName = matchRelationship.reuseName) Then
               setRelationshipReusedRelIndex relIndex, matchRelIndex
               GoTo NextRel
             Else
               logMsg "potential candidates for reuse of relationship table: relationships """ & relationship.relName & """/""" & matchRelationship.relName & """" _
                       & " " & leftClass.orMappingSuperClassIndex & "/" & matchLeftClass.orMappingSuperClassIndex & "/" & rightclass.orMappingSuperClassIndex & "/" & matchRightClass.orMappingSuperClassIndex, ellInfo
             End If
           End If
         End If
       Next matchRelIndex
     End If
 NextRel:
   Next relIndex
 
   For relIndex = 1 To g_relationships.numDescriptors Step 1
       ' determine effective short name
       g_relationships.descriptors(relIndex).effectiveShortName = IIf(reuseRelationships And g_relationships.descriptors(relIndex).reuseShortName <> "", g_relationships.descriptors(relIndex).reuseShortName, g_relationships.descriptors(relIndex).shortName)

       ' determine whether this relationship is implemented in an 'own table'
       If (reuseRelationships And g_relationships.descriptors(relIndex).reusedRelIndex > 0) Then
         g_relationships.descriptors(relIndex).implementsInOwnTable = False
       ElseIf supportNlForRelationships And g_relationships.descriptors(relIndex).isNl Then
         g_relationships.descriptors(relIndex).implementsInOwnTable = True
       ElseIf g_relationships.descriptors(relIndex).maxLeftCardinality = -1 And g_relationships.descriptors(relIndex).maxRightCardinality = -1 Then
         g_relationships.descriptors(relIndex).implementsInOwnTable = True
       Else
         g_relationships.descriptors(relIndex).implementsInOwnTable = False
       End If
   Next relIndex
 
   For relIndex = 1 To g_relationships.numDescriptors Step 1
       If g_relationships.descriptors(relIndex).reuseName <> "" Then
         If g_relationships.descriptors(relIndex).leftEntityType = eactClass And g_relationships.descriptors(relIndex).rightEntityType = eactClass Then
           If g_relationships.descriptors(relIndex).maxLeftCardinality = -1 And g_relationships.descriptors(relIndex).maxRightCardinality = 1 Then
             For j = relIndex - 1 To 1 Step -1
               If _
                 g_relationships.descriptors(relIndex).leftEntityIndex = g_relationships.descriptors(j).leftEntityIndex And _
                 g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(g_relationships.descriptors(j).rightEntityIndex).orMappingSuperClassIndex And _
                 g_relationships.descriptors(relIndex).maxLeftCardinality = g_relationships.descriptors(j).maxLeftCardinality And _
                 g_relationships.descriptors(relIndex).maxRightCardinality = g_relationships.descriptors(j).maxRightCardinality And _
                 g_relationships.descriptors(relIndex).reuseName = g_relationships.descriptors(j).reuseName _
               Then
                 g_relationships.descriptors(relIndex).isReusedInSameEntity = (g_relationships.descriptors(relIndex).rightEntityIndex <> g_relationships.descriptors(j).rightEntityIndex)
                 GoTo NextRelIndex
               End If
             Next j
           ElseIf g_relationships.descriptors(relIndex).maxRightCardinality = -1 And g_relationships.descriptors(relIndex).maxLeftCardinality = 1 Then
             For j = relIndex - 1 To 1 Step -1
               If _
                 g_relationships.descriptors(relIndex).rightEntityIndex = g_relationships.descriptors(j).rightEntityIndex And _
                 g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(g_relationships.descriptors(j).leftEntityIndex).orMappingSuperClassIndex And _
                 g_relationships.descriptors(relIndex).maxLeftCardinality = g_relationships.descriptors(j).maxLeftCardinality And _
                 g_relationships.descriptors(relIndex).maxRightCardinality = g_relationships.descriptors(j).maxRightCardinality And _
                 g_relationships.descriptors(relIndex).reuseName = g_relationships.descriptors(j).reuseName _
               Then
                 g_relationships.descriptors(relIndex).isReusedInSameEntity = (g_relationships.descriptors(relIndex).leftEntityIndex <> g_relationships.descriptors(j).leftEntityIndex)
                 GoTo NextRelIndex
               End If
             Next j
           End If
         End If
       End If
 NextRelIndex:
   Next relIndex

   For relIndex = 1 To g_relationships.numDescriptors Step 1
       ' if relationship is not implemented in own table, determine table implementing it
       g_relationships.descriptors(relIndex).implementsInEntity = ernmNone
       If Not g_relationships.descriptors(relIndex).implementsInOwnTable Then
         If g_relationships.descriptors(relIndex).maxRightCardinality = 1 Then
           g_relationships.descriptors(relIndex).implementsInEntity = ernmLeft
         Else
           g_relationships.descriptors(relIndex).implementsInEntity = ernmRight
         End If
       End If
   Next relIndex
 
 ' ### IF IVK ###
   ' determine classes / relationships being subject to 'PreisDurchschuss'
 '  For relIndex = 1 To g_relationships.numDescriptors Step 1
 '    With g_relationships.descriptors(relIndex)
 '      If .leftEntityIndex > 0 Then
 '        If g_classes.descriptors(.leftEntityIndex).hasPriceAssignmentSubClass Then
 '          If .maxLeftCardinality < 0 And .maxRightCardinality < 0 Then
 '            .isSubjectToPreisDurchschuss = True
 '          ElseIf .maxRightCardinality = 1 And g_classes.descriptors(.rightEntityIndex).isPsTagged And Not g_classes.descriptors(.rightEntityIndex).isPsForming Then
 '            With g_classes.descriptors(.rightEntityIndex)
 '              If .aggHeadClassIndexExact <= 0 Then
 '                .isSubjectToPreisDurchschuss = True
 '              ElseIf g_classes.descriptors(.aggHeadClassIndexExact).isSubjectToPreisDurchschuss Then
 '                .isSubjectToPreisDurchschuss = True
 '              End If
 '            End With
 '          End If
 '        End If
 '      End If
 '      If .rightEntityIndex > 0 Then
 '        If g_classes.descriptors(.rightEntityIndex).hasPriceAssignmentSubClass Then
 '          If .maxLeftCardinality < 0 And .maxRightCardinality < 0 Then
 '            .isSubjectToPreisDurchschuss = True
 '          ElseIf .maxLeftCardinality = 1 And g_classes.descriptors(.leftEntityIndex).isPsTagged And Not g_classes.descriptors(.leftEntityIndex).isPsForming Then
 '            With g_classes.descriptors(.leftEntityIndex)
 '              If .aggHeadClassIndexExact <= 0 Then
 '                .isSubjectToPreisDurchschuss = True
 '              ElseIf g_classes.descriptors(.aggHeadClassIndexExact).isSubjectToPreisDurchschuss Then
 '                .isSubjectToPreisDurchschuss = True
 '              End If
 '            End With
 '          End If
 '        End If
 '      End If
 '    End With
 '  Next relIndex
 
   ' determine whether this relationship defines validity per organization
   For relIndex = 1 To g_relationships.numDescriptors Step 1
       If InStr(1, UCase(g_relationships.descriptors(relIndex).relName), "VALID") Then
         If (g_relationships.descriptors(relIndex).leftEntityType = eactClass And g_relationships.descriptors(relIndex).leftEntityIndex = g_classIndexOrganization) Then
           g_relationships.descriptors(relIndex).isValidForOrganization = True
             g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).isValidForOrganization = True
             For i = 1 To UBound(g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).subclassIndexesRecursive)
               g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).subclassIndexesRecursive(i)).isValidForOrganization = True
             Next i
         ElseIf (g_relationships.descriptors(relIndex).rightEntityType = eactClass And g_relationships.descriptors(relIndex).rightEntityIndex = g_classIndexOrganization) Then
           g_relationships.descriptors(relIndex).isValidForOrganization = True
             g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).isValidForOrganization = True
             For i = 1 To UBound(g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).subclassIndexesRecursive)
               g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).subclassIndexesRecursive(i)).isValidForOrganization = True
             Next i
         End If
       End If
   Next relIndex
 
   ' determine whether this relationship corresponds to an organization-specific reference in some class
   Dim someClassUpdated As Boolean
   someClassUpdated = True
   Dim thisClassIndex As Integer
   While someClassUpdated
     someClassUpdated = False
     For relIndex = 1 To g_relationships.numDescriptors Step 1
         If g_relationships.descriptors(relIndex).leftEntityType = eactClass And g_relationships.descriptors(relIndex).rightEntityType = eactClass And g_relationships.descriptors(relIndex).leftEntityIndex > 0 And g_relationships.descriptors(relIndex).rightEntityIndex > 0 Then
           If (g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).isValidForOrganization Or g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).hasOrganizationSpecificReference) Or _
              (g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).isValidForOrganization Or g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).hasOrganizationSpecificReference) Then
             If g_relationships.descriptors(relIndex).maxLeftCardinality < 0 And g_relationships.descriptors(relIndex).maxRightCardinality < 0 Then
               If Not (g_relationships.descriptors(relIndex).leftEntityType = eactClass And g_relationships.descriptors(relIndex).leftEntityIndex = g_classIndexOrganization) And _
                  Not (g_relationships.descriptors(relIndex).rightEntityType = eactClass And g_relationships.descriptors(relIndex).rightEntityIndex = g_classIndexOrganization) Then
                 ' direct references to 'organization' are not included here
                 someClassUpdated = someClassUpdated Or Not g_relationships.descriptors(relIndex).hasOrganizationSpecificReference
                 g_relationships.descriptors(relIndex).hasOrganizationSpecificReference = True
                 If g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).isValidForOrganization Or g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).hasOrganizationSpecificReference Then
                   someClassUpdated = someClassUpdated Or Not g_relationships.descriptors(relIndex).rightClassIsOrganizationSpecific
                   g_relationships.descriptors(relIndex).rightClassIsOrganizationSpecific = True
                 End If
                 If g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).isValidForOrganization Or g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).hasOrganizationSpecificReference Then
                   someClassUpdated = someClassUpdated Or Not g_relationships.descriptors(relIndex).leftClassIsOrganizationSpecific
                   g_relationships.descriptors(relIndex).leftClassIsOrganizationSpecific = True
                 End If
               End If
             Else
               If g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).isValidForOrganization And g_relationships.descriptors(relIndex).maxRightCardinality < 0 Then
                 thisClassIndex = g_relationships.descriptors(relIndex).rightEntityIndex
                 While thisClassIndex > 0
                     someClassUpdated = someClassUpdated Or Not g_classes.descriptors(thisClassIndex).hasOrganizationSpecificReference
                     g_classes.descriptors(thisClassIndex).hasOrganizationSpecificReference = True
                     addRelRef g_classes.descriptors(thisClassIndex).relRefsToOrganizationSpecificClasses, relIndex, etRight
                     thisClassIndex = g_classes.descriptors(thisClassIndex).superClassIndex
                 Wend
               End If
               If g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).isValidForOrganization And g_relationships.descriptors(relIndex).maxLeftCardinality < 0 Then
                 thisClassIndex = g_relationships.descriptors(relIndex).leftEntityIndex
                 While thisClassIndex > 0
                     someClassUpdated = someClassUpdated Or Not g_classes.descriptors(thisClassIndex).hasOrganizationSpecificReference
                     g_classes.descriptors(thisClassIndex).hasOrganizationSpecificReference = True
                     addRelRef g_classes.descriptors(thisClassIndex).relRefsToOrganizationSpecificClasses, relIndex, etLeft
                     thisClassIndex = g_classes.descriptors(thisClassIndex).superClassIndex
                 Wend
               End If
             End If
           End If
         End If
     Next relIndex
   Wend
 
   Dim leftOrParentClassIndex As Integer
   Dim rightOrParentClassIndex As Integer
   For relIndex = 1 To g_relationships.numDescriptors Step 1
       g_relationships.descriptors(relIndex).isSubjectToExpCopy = g_relationships.descriptors(relIndex).isUserTransactional And UCase(g_relationships.descriptors(relIndex).aggHeadName) = UCase(clnExpression)

       If g_relationships.descriptors(relIndex).leftEntityIndex > 0 And g_relationships.descriptors(relIndex).rightEntityIndex > 0 And Not g_relationships.descriptors(relIndex).isMdsExpressionRel Then
         If g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).condenseData Or g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).condenseData Then
           leftOrParentClassIndex = g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).orMappingSuperClassIndex
           rightOrParentClassIndex = g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).orMappingSuperClassIndex

           If Not (g_relationships.descriptors(relIndex).maxLeftCardinality < 0 And g_relationships.descriptors(relIndex).maxRightCardinality < 0) And (leftOrParentClassIndex <> rightOrParentClassIndex) Then
             If g_classes.descriptors(g_relationships.descriptors(relIndex).rightEntityIndex).condenseData And ((g_relationships.descriptors(relIndex).maxLeftCardinality < 0) Or (g_relationships.descriptors(relIndex).maxLeftCardinality = 1 And g_relationships.descriptors(relIndex).maxRightCardinality = 1)) Then
               thisClassIndex = g_relationships.descriptors(relIndex).leftEntityIndex
               While thisClassIndex > 0
                   g_classes.descriptors(thisClassIndex).hasOrganizationSpecificReference = True
                   addRelRef g_classes.descriptors(thisClassIndex).relRefsToOrganizationSpecificClasses, relIndex, etLeft
                   thisClassIndex = g_classes.descriptors(thisClassIndex).superClassIndex
               Wend
             ElseIf g_classes.descriptors(g_relationships.descriptors(relIndex).leftEntityIndex).condenseData And ((g_relationships.descriptors(relIndex).maxRightCardinality < 0) Or (g_relationships.descriptors(relIndex).maxLeftCardinality = 1 And g_relationships.descriptors(relIndex).maxRightCardinality = 1)) Then
               thisClassIndex = g_relationships.descriptors(relIndex).rightEntityIndex
               While thisClassIndex > 0
                   g_classes.descriptors(thisClassIndex).hasOrganizationSpecificReference = True
                   addRelRef g_classes.descriptors(thisClassIndex).relRefsToOrganizationSpecificClasses, relIndex, etRight
                   thisClassIndex = g_classes.descriptors(thisClassIndex).superClassIndex
               Wend
             End If
           End If
         End If
       End If

       ' determine foreign key column names
       Dim thisDdlType As DdlTypeId
       For thisDdlType = edtPdm To edtLdm
         If g_relationships.descriptors(relIndex).implementsInOwnTable Then
           g_relationships.descriptors(relIndex).leftFkColName(thisDdlType) = genSurrogateKeyName(thisDdlType, g_relationships.descriptors(relIndex).leftEntityShortName)
           g_relationships.descriptors(relIndex).rightFkColName(thisDdlType) = genSurrogateKeyName(thisDdlType, g_relationships.descriptors(relIndex).rightEntityShortName)
         Else
           g_relationships.descriptors(relIndex).leftFkColName(thisDdlType) = genSurrogateKeyName(thisDdlType, g_relationships.descriptors(relIndex).effectiveShortName & g_relationships.descriptors(relIndex).rlShortRelName)
           g_relationships.descriptors(relIndex).rightFkColName(thisDdlType) = genSurrogateKeyName(thisDdlType, g_relationships.descriptors(relIndex).effectiveShortName & g_relationships.descriptors(relIndex).lrShortRelName)
         End If
       Next thisDdlType
   Next relIndex
 ' ### ENDIF IVK ###
 End Sub
 
