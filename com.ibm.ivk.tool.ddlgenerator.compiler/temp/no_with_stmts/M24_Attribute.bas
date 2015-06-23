 Attribute VB_Name = "M24_Attribute"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colSection = 2
 Private Const colClass = colSection + 1
 Private Const colEntityType = colClass + 1
 Private Const colAttribute = colEntityType + 1
 Private Const colShortName = colAttribute + 1
 Private Const colMapsToClAttributes = colShortName + 1
 ' ### IF IVK ###
 Private Const colMapsToACMAttribute = colMapsToClAttributes + 1
 Private Const colMapsToACMAttributeForRead = colMapsToACMAttribute + 1
 Private Const colAcmMappingIsInstantiated = colMapsToACMAttributeForRead + 1
 Private Const colFtoConflictWith = colAcmMappingIsInstantiated + 1
 Private Const colGroupIdBasedOn = colFtoConflictWith + 1
 Private Const colDomainSection = colGroupIdBasedOn + 1
 ' ### ELSE IVK ###
 'Private Const colDomainSection = colMapsToClAttributes + 1
 ' ### ENDIF IVK ###
 Private Const colDomain = colDomainSection + 1
 Private Const colDefault = colDomain + 1
 Private Const colIsNl = colDefault + 1
 ' ### IF IVK ###
 Private Const colIsNationalizable = colIsNl + 1
 Private Const colIsNullable = colIsNationalizable + 1
 ' ### ELSE IVK ###
 'Private Const colIsNullable = colIsNl + 1
 ' ### ENDIF IVK ###
 Private Const colIsNullableInOrgs = colIsNullable + 1
 Private Const colIsIdentifying = colIsNullableInOrgs + 1
 Private Const colIncludeInPkIndex = colIsIdentifying + 1
 ' ### IF IVK ###
 Private Const colIsExpression = colIncludeInPkIndex + 1
 Private Const colNoXmlExport = colIsExpression + 1
 Private Const colIsPersistent = colNoXmlExport + 1
 Private Const colIsTimeVarying = colIsPersistent + 1
 ' ### ELSE IVK ###
 'Private Const colIsTimeVarying = colIncludeInPkIndex + 1
 ' ### ENDIF IVK ###
 Private Const colComment = colIsTimeVarying + 1
 Private Const colI18nId = colComment + 1
 
 Global Const colAttrI18nId = colI18nId
 
 Private Const firstRow = 4
 
 Private Const sheetName = "Attr"
 
 Private Const acmCsvProcessingStep = 5
 
 Global g_attributes As AttributeDescriptors
 
 
 
 Sub genAttrList( _
    ByRef list() As String, _
    ByRef str As String _
 )
   Dim i As Integer
   list = split(str, ",")
   For i = LBound(list) To UBound(list)
     list(i) = Trim(list(i))
   Next i
 End Sub

 
 Private Sub readSheet()
   initAttributeDescriptors g_attributes

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   Dim lastSection As String, lastClassName As String
   Dim clAttributes As String
   While thisSheet.Cells(thisRow, colAttribute) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).sectionName = Trim(thisSheet.Cells(thisRow, colSection))
       If (g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).sectionName & "" = "") Then g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).sectionName = lastSection
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).className = Trim(thisSheet.Cells(thisRow, colClass))
       If (g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).className & "" = "") Then g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).className = lastClassName
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).attributeName = Trim(thisSheet.Cells(thisRow, colAttribute))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).cType = getAttrContainerType(Trim(thisSheet.Cells(thisRow, colEntityType)))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).i18nId = Trim(thisSheet.Cells(thisRow, colI18nId))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).shortName = Trim(thisSheet.Cells(thisRow, colShortName))
       clAttributes = Trim(thisSheet.Cells(thisRow, colMapsToClAttributes))
       If clAttributes <> "" Then
         genAttrList g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).mapsToChangeLogAttributes, clAttributes
       End If
 ' ### IF IVK ###
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).ftoConflictWith = Trim(thisSheet.Cells(thisRow, colFtoConflictWith))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).groupIdBasedOn = Trim(thisSheet.Cells(thisRow, colGroupIdBasedOn))
       If g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).groupIdBasedOn <> "" Then
         genAttrList g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).groupIdAttributes, g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).groupIdBasedOn
         g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isGroupId = True
       End If

       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).virtuallyMapsTo.description = Trim(thisSheet.Cells(thisRow, colMapsToACMAttribute))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isVirtual = (g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).virtuallyMapsTo.description <> "")
       If g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isVirtual Then
         g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).virtuallyMapsToForRead.description = Trim(thisSheet.Cells(thisRow, colMapsToACMAttributeForRead))
         g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).virtuallyMapsTo.isInstantiated = getBoolean(thisSheet.Cells(thisRow, colAcmMappingIsInstantiated))
       End If
 
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isNationalizable = getBoolean(thisSheet.Cells(thisRow, colIsNationalizable))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isExpression = getBoolean(thisSheet.Cells(thisRow, colIsExpression))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).noXmlExport = getBoolean(thisSheet.Cells(thisRow, colNoXmlExport))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isPersistent = getBoolean(thisSheet.Cells(thisRow, colIsPersistent))
 ' ### ENDIF IVK ###

       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).domainSection = Trim(thisSheet.Cells(thisRow, colDomainSection))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).domainName = Trim(thisSheet.Cells(thisRow, colDomain))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).default = Trim(thisSheet.Cells(thisRow, colDefault))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isNl = getBoolean(thisSheet.Cells(thisRow, colIsNl))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isNullable = getBoolean(thisSheet.Cells(thisRow, colIsNullable))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isNullableInOrgs = Trim(thisSheet.Cells(thisRow, colIsNullableInOrgs))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isIdentifying = getBoolean(thisSheet.Cells(thisRow, colIsIdentifying))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).includeInPkIndex = getBoolean(thisSheet.Cells(thisRow, colIncludeInPkIndex))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isTimeVarying = getBoolean(thisSheet.Cells(thisRow, colIsTimeVarying))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).comment = Trim(thisSheet.Cells(thisRow, colComment))
       g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isNotAcmRelated = True
 ' ### IF IVK ###

       ReDim g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).virtuallyReferredToBy(0 To 0)
 ' ### ENDIF IVK ###

       lastSection = g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).sectionName
       lastClassName = g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).className
 
 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 ' ### IF IVK ###
 Sub addAttribute( _
   ByRef sectionName As String, _
   ByRef entityName As String, _
   ByRef containerType As AcmAttrContainerType, _
   ByRef attributeName As String, _
   ByRef shortName As String, _
   ByRef domainSection As String, _
   ByRef domainName As String, _
   Optional ByRef default As String = "", _
   Optional isNl As Boolean = False, _
   Optional isNationalizable As Boolean = False, _
   Optional isNullable As Boolean = False, _
   Optional isIdentifying As Boolean = False, _
   Optional isExpression As Boolean, _
   Optional isTimeVarying As Boolean, _
   Optional ByRef comment As String = "", _
   Optional isVirtual As Boolean = False _
 )
 ' ### ELSE IVK ###
 'Sub addAttribute( _
 ' ByRef sectionName As String, _
 ' ByRef entityName As String, _
 ' ByRef containerType As AcmAttrContainerType, _
 ' ByRef attributeName As String, _
 ' ByRef shortName As String, _
 ' ByRef domainSection As String, _
 ' ByRef domainName As String, _
 ' Optional ByRef default As String = "", _
 ' Optional isNl As Boolean = False, _
 ' Optional isNullable As Boolean = False, _
 ' Optional isIdentifying As Boolean = False, _
 ' Optional isTimeVarying As Boolean, _
 ' Optional ByRef comment As String = "" _
 ')
 ' ### ENDIF IVK ###
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).sectionName = sectionName
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).className = entityName
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).cType = containerType
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).attributeName = attributeName
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).shortName = shortName
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).domainSection = domainSection
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).domainName = domainName
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).default = default
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isNl = isNl
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isNullable = isNullable
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isIdentifying = isIdentifying
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isTimeVarying = isTimeVarying
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).comment = comment
 ' ### IF IVK ###
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isNationalizable = isNationalizable
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isExpression = isExpression
     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isPersistent = True

     g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).isVirtual = isVirtual
     ReDim g_attributes.descriptors(allocAttributeDescriptorIndex(g_attributes)).virtuallyReferredToBy(0 To 0)
 ' ### ENDIF IVK ###
 End Sub
 
 
 Sub getAttributes()
   If g_attributes.numDescriptors = 0 Then
     readSheet
   End If
 End Sub
 
 
 Sub resetAttributes()
   g_attributes.numDescriptors = 0
 End Sub
 
 
 Function getAttributeIndexByName( _
   ByRef sectionName As String, _
   ByRef attributeName As String _
 ) As Integer
   Dim i As Integer
 
   getAttributeIndexByName = -1
 
   For i = 1 To g_attributes.numDescriptors Step 1
     If UCase(g_attributes.descriptors(i).sectionName) = UCase(sectionName) And _
        UCase(g_attributes.descriptors(i).attributeName) = UCase(attributeName) Then
       getAttributeIndexByName = i
       Exit Function
     End If
   Next i
 End Function
 
 
 ' ### IF IVK ###
 Function getAttributeIndexByNameAndEntityIndex( _
   ByRef attributeName As String, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef acmEntityIndex As Integer, _
   Optional ByVal includeVirtualAttr As Boolean = False _
 ) As Integer
 ' ### ELSE IVK ###
 'Function getAttributeIndexByNameAndEntityIndex( _
 ' ByRef attributeName As String, _
 ' ByRef acmEntityType As AcmAttrContainerType, _
 ' ByRef acmEntityIndex As Integer _
 ') As Integer
 ' ### ENDIF IVK ###
   Dim i As Integer
 
   getAttributeIndexByNameAndEntityIndex = -1
 
   For i = 1 To g_attributes.numDescriptors Step 1
     If (UCase(g_attributes.descriptors(i).attributeName) = UCase(attributeName) Or UCase(g_attributes.descriptors(i).attributeName) & gc_enumAttrNameSuffix = UCase(attributeName)) And _
        g_attributes.descriptors(i).cType = acmEntityType And _
        g_attributes.descriptors(i).acmEntityIndex = acmEntityIndex Then
 ' ### IF IVK ###
       If (includeVirtualAttr Or Not g_attributes.descriptors(i).isVirtual) Then
 ' ### ENDIF IVK ###
         getAttributeIndexByNameAndEntityIndex = i
         Exit Function
 ' ### IF IVK ###
       End If
 ' ### ENDIF IVK ###
     End If
   Next i
 End Function
 
 
 ' ### IF IVK ###
 Function getAttributeIndexByNameAndEntityIndexRaw( _
   ByRef attributeName As String, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef acmEntityIndex As Integer, _
   Optional ByVal includeVirtualAttr As Boolean = False _
 ) As Integer
 ' ### ELSE IVK ###
 'Function getAttributeIndexByNameAndEntityIndexRaw( _
 '  ByRef attributeName As String, _
 '  ByRef acmEntityType As AcmAttrContainerType, _
 '  ByRef acmEntityIndex As Integer _
 ') As Integer
 ' ### ENDIF IVK ###
   Dim i As Integer
 
   getAttributeIndexByNameAndEntityIndexRaw = -1
 
   For i = 1 To g_attributes.numDescriptors Step 1
     If (UCase(g_attributes.descriptors(i).attributeName) = UCase(attributeName) Or UCase(g_attributes.descriptors(i).attributeName) & gc_enumAttrNameSuffix = UCase(attributeName)) And _
        g_attributes.descriptors(i).cType = acmEntityType And _
        g_attributes.descriptors(i).acmEntityIndex = acmEntityIndex Then
 ' ### IF IVK ###
       If (includeVirtualAttr Or Not g_attributes.descriptors(i).isVirtual) Then
 ' ### ENDIF IVK ###
         getAttributeIndexByNameAndEntityIndexRaw = i
         Exit Function
 ' ### IF IVK ###
       End If
 ' ### ENDIF IVK ###
     End If
   Next i
 End Function
 
 
 ' ### IF IVK ###
 Function getAttributeIndexByNameAndEntityIndexRecursive( _
   ByRef attributeName As String, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef acmEntityIndex As Integer, _
   Optional ByVal includeVirtualAttr As Boolean = False _
 ) As Integer
 ' ### ELSE IVK ###
 'Function getAttributeIndexByNameAndEntityIndexRecursive( _
 '  ByRef attributeName As String, _
 '  ByRef acmEntityType As AcmAttrContainerType, _
 '  ByRef acmEntityIndex As Integer _
 ') As Integer
 ' ### ENDIF IVK ###
   Dim thisAttrIndex As Integer
   Dim i As Integer
 
   getAttributeIndexByNameAndEntityIndexRecursive = -1

   If acmEntityType = eactClass Then
 ' ### IF IVK ###
       thisAttrIndex = getAttributeIndexByNameAndEntityIndex(attributeName, eactClass, acmEntityIndex, includeVirtualAttr)
 ' ### ELSE IVK ###
 '     thisAttrIndex = getAttributeIndexByNameAndEntityIndex(attributeName, eactClass, acmEntityIndex)
 ' ### ENDIF IVK ###
       If thisAttrIndex > 0 Then
         getAttributeIndexByNameAndEntityIndexRecursive = thisAttrIndex
         Exit Function
       End If
       For i = LBound(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive) To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive)
 ' ### IF IVK ###
         thisAttrIndex = getAttributeIndexByNameAndEntityIndex(attributeName, eactClass, g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i), includeVirtualAttr)
 ' ### ELSE IVK ###
 '       thisAttrIndex = getAttributeIndexByNameAndEntityIndex(attributeName, eactClass, .subclassIndexesRecursive(i))
 ' ### ENDIF IVK ###
         If thisAttrIndex > 0 Then
           getAttributeIndexByNameAndEntityIndexRecursive = thisAttrIndex
           Exit Function
         End If
       Next i
   Else
 ' ### IF IVK ###
     getAttributeIndexByNameAndEntityIndexRecursive = getAttributeIndexByNameAndEntityIndex(attributeName, acmEntityType, acmEntityIndex, includeVirtualAttr)
 ' ### ELSE IVK ###
 '   getAttributeIndexByNameAndEntityIndexRecursive = getAttributeIndexByNameAndEntityIndex(attributeName, acmEntityType, acmEntityIndex)
 ' ### ENDIF IVK ###
   End If
 End Function
 
 Function getAttributeIndexByI18nId( _
   ByRef i18nId As String _
 ) As Integer
   Dim i As Integer
 
   getAttributeIndexByI18nId = -1
 
   For i = 1 To g_attributes.numDescriptors Step 1
     If UCase(g_attributes.descriptors(i).i18nId) = UCase(i18nId) Then
       getAttributeIndexByI18nId = i
       Exit Function
     End If
   Next i
 End Function
 
 Function getMaxDbAttributeLengthByNameAndEntityIndex( _
   ByRef attributeName As String, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef acmEntityIndex As Integer, _
   Optional ByVal includeVirtualAttr As Boolean = False _
 ) As Integer
   getMaxDbAttributeLengthByNameAndEntityIndex = -1
 
   Dim attrIndex As Integer
   attrIndex = getAttributeIndexByNameAndEntityIndex(attributeName, acmEntityType, acmEntityIndex)

   If attrIndex > 0 Then
       If g_attributes.descriptors(attrIndex).domainIndex > 0 Then
           getMaxDbAttributeLengthByNameAndEntityIndex = g_domains.descriptors(g_attributes.descriptors(attrIndex).domainIndex).maxLength * IIf(g_domains.descriptors(g_attributes.descriptors(attrIndex).domainIndex).supportUnicode, g_domains.descriptors(g_attributes.descriptors(attrIndex).domainIndex).unicodeExpansionFactor, 1)
       End If
   End If
 
 End Function
 
 
 Function getPkAttrListByClass( _
   ByRef classIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef prefix As String = "", _
   Optional forLrt As Boolean = False, _
   Optional includedExtraAttrs As Boolean = False, _
   Optional excludeFkAttrs As Boolean = False _
 ) As String

   On Error GoTo ErrorExit

   Dim pkAttrList As String
   getPkAttrListByClass = ""
   pkAttrList = ""

   Dim relNameInfix As String

     Dim i As Integer
     For i = 1 To g_classes.descriptors(classIndex).attrRefs.numDescriptors Step 1
         If g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).cType = eactClass Then
           If (g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).isIdentifying And Not includedExtraAttrs) Or (includedExtraAttrs And Not g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).isIdentifying And g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).includeInPkIndex) Then
             pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ",") & UCase(prefix) & genAttrNameByIndex(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).attrIndex, ddlType)
           End If
         End If
     Next i

     If includeFksInPks And Not excludeFkAttrs Then
       Dim relShortName As String
       Dim relDirShortName As String
       Dim srcClassIndex As Integer, dstClassIndex As Integer
       Dim j As Integer
       For i = 1 To g_classes.descriptors(classIndex).relRefs.numRefs Step 1
         If g_classes.descriptors(classIndex).relRefs.refs(i).refType = etLeft Then
             If g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).maxRightCardinality = 1 Then
               If Not includedExtraAttrs And g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).isIdentifyingRight Then
                 If g_classes.descriptors(g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).rightEntityIndex).useSurrogateKey Then
                   pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ",") & UCase(prefix) & _
                     genAttrDeclByDomain(conOid, cosnOid, eavtDomain, _
                          g_domainIndexOid, eactClass, classIndex, , False, ddlType, _
                          g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).shortName & g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).lrShortRelName, edomList, , , 0)
                 Else
                   relShortName = g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).shortName
                   relDirShortName = g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).lrShortRelName
                   relNameInfix = IIf(g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).useLrLdmRelName, g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).lrLdmRelName, relShortName & relDirShortName)
                   pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ",") & _
                                getPkAttrListByClass(g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).rightEntityIndex, ddlType, _
                                                     prefix & relNameInfix & IIf(Right("_" & prefix & relNameInfix, 1) = "_", "", "_"), forLrt)
                 End If
               ElseIf includedExtraAttrs And g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).includeInPkIndex Then
                 pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ",") & UCase(prefix) & _
                   genAttrDeclByDomain(conOid, cosnOid, eavtDomain, _
                        g_domainIndexOid, eactClass, classIndex, , False, ddlType, _
                        g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).shortName & g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).lrShortRelName, edomList, , , 0)
               End If
             End If
         ElseIf g_classes.descriptors(classIndex).relRefs.refs(i).refType = etRight Then
             If g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).maxLeftCardinality = 1 Then
               If Not includedExtraAttrs And g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).isIdentifyingLeft Then
                 If g_classes.descriptors(g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).leftEntityIndex).useSurrogateKey Then
                   pkAttrList = _
                     pkAttrList & IIf(pkAttrList = "", "", ",") & UCase(prefix) & _
                     genAttrDeclByDomain( _
                       conOid, cosnOid, eavtDomain, _
                       g_domainIndexOid, eactClass, classIndex, , False, ddlType, _
                       g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).shortName & g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).rlShortRelName, edomList, , , 0 _
                     )
                 Else
                   relShortName = g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).shortName
                   relDirShortName = g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).rlShortRelName
                   relNameInfix = IIf(g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).useRlLdmRelName, g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).rlLdmRelName, relShortName & relDirShortName)
                   pkAttrList = _
                     pkAttrList & IIf(pkAttrList = "", "", ",") & _
                     getPkAttrListByClass(g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).leftEntityIndex, ddlType, _
                       prefix & relNameInfix & IIf(Right("_" & prefix & relNameInfix, 1) = "_", "", "_"), forLrt _
                     )
                 End If
               ElseIf includedExtraAttrs And g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).includeInPkIndex Then
                 pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ",") & UCase(prefix) & _
                   genAttrDeclByDomain(conOid, cosnOid, eavtDomain, _
                        g_domainIndexOid, eactClass, classIndex, , False, ddlType, _
                        g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).shortName & g_relationships.descriptors(g_classes.descriptors(classIndex).relRefs.refs(i).refIndex).rlShortRelName, edomList, , , 0)
               End If
             End If
         End If
       Next i
     End If
 ' ### IF IVK ###

     If pkAttrList <> "" Then
       If g_classes.descriptors(classIndex).isNationalizable And nationalFlagPartOfPK Then
         pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ",") & UCase(prefix) & g_anIsNational
       End If
     End If
     If pkAttrList <> "" Or Not g_classes.descriptors(classIndex).useSurrogateKey Then
       If g_classes.descriptors(classIndex).isPsTagged And (Not g_classes.descriptors(classIndex).psTagNotIdentifying Or includedExtraAttrs) Then
         pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ",") & UCase(prefix) & g_anPsOid
       End If
     End If
 ' ### ENDIF IVK ###

   getPkAttrListByClass = pkAttrList
 
 NormalExit:
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 
 
 Function getPkAttrListByClassIndex( _
   classIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef prefix As String = "", _
   Optional forLrt As Boolean = False, _
   Optional includeExtraAttrs As Boolean = False, _
   Optional excludeFkAttrs As Boolean = False _
 ) As String
   getPkAttrListByClassIndex = getPkAttrListByClass(classIndex, ddlType, prefix, forLrt, includeExtraAttrs, excludeFkAttrs)
 End Function
 
 Function getPkAttrListByRel( _
   thisRelIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef prefix As String = "" _
 ) As String
   getPkAttrListByRel = getPkAttrListByRelIndex(thisRelIndex, ddlType, prefix)
 End Function
 
 
 Function getPkAttrListByRelIndex( _
   relIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef prefix As String = "" _
 ) As String
   getPkAttrListByRelIndex = ""

   Dim pkAttrList As String
   pkAttrList = ""

     Dim i As Integer
     For i = 1 To g_relationships.descriptors(relIndex).attrRefs.numDescriptors Step 1
         If g_attributes.descriptors(g_relationships.descriptors(relIndex).attrRefs.descriptors(i).refIndex).isIdentifying Then
           If g_attributes.descriptors(g_relationships.descriptors(relIndex).attrRefs.descriptors(i).refIndex).valueType = eavtEnum Then
             pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ", ") & UCase(prefix) & genAttrName(g_attributes.descriptors(g_relationships.descriptors(relIndex).attrRefs.descriptors(i).refIndex).attributeName & gc_enumAttrNameSuffix, ddlType)
           Else
             pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ", ") & UCase(prefix) & genAttrName(g_attributes.descriptors(g_relationships.descriptors(relIndex).attrRefs.descriptors(i).refIndex).attributeName, ddlType)
           End If
         End If
     Next i

   getPkAttrListByRelIndex = pkAttrList
 End Function
 
 
 Sub genAttrListForClassRecursive( _
   ByRef classIndex As Integer, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forLrt As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomListLrt, _
   Optional direction As RecursionDirection = erdDown _
 )
   On Error GoTo ErrorExit

     Dim tabColumns As EntityColumnDescriptors
     tabColumns = nullEntityColumnDescriptors

     If g_classes.descriptors(classIndex).isGenForming Then
 ' ### IF IVK ###
       If g_classes.descriptors(classIndex).hasNoIdentity Then
         genAttrDeclsForClassRecursiveWithColReUse classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, True, True, forLrt, outputMode, direction
         genAttrDeclsForClassRecursiveWithColReUse classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, False, False, forLrt, outputMode, direction
       Else
         genAttrDeclsForClassRecursiveWithColReUse classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, False, forLrt, outputMode, direction
       End If
 ' ### ELSE IVK ###
 '     genAttrDeclsForClassRecursiveWithColReUse classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, False, forLrt, outputMode, direction
 ' ### ENDIF IVK ###
     Else
       genAttrDeclsForClassRecursiveWithColReUse classIndex, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, , , forLrt, outputMode, direction
     End If

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrListForClassRecursive( _
   ByRef classIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forLrt As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomListLrt, _
   Optional direction As RecursionDirection = erdDown _
 )
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   On Error GoTo ErrorExit

   genTransformedAttrListForClassRecursiveWithColReuse classIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, _
     indent, forLrt, forGen, outputMode, direction
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrListForClassRecursiveWithColReuse( _
   ByRef classIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forLrt As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomListLrt, _
   Optional direction As RecursionDirection = erdDown _
 )
   On Error GoTo ErrorExit

     If g_classes.descriptors(classIndex).isGenForming Then
 ' ### IF IVK ###
       If g_classes.descriptors(classIndex).hasNoIdentity Then
         genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, True, True, forLrt, outputMode, direction
         genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, False, False, forLrt, outputMode, direction
       Else
         genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, False, forLrt, outputMode, direction
       End If
 ' ### ELSE IVK ###
 '     genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, False, forLrt, outputMode, direction
 ' ### ENDIF IVK ###
     Else
       genTransformedAttrDeclsForClassRecursiveWithColReUse classIndex, transformation, tabColumns, 1, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, , , forLrt, outputMode, direction
     End If
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genAttrListForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forLrt As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomListLrt, _
   Optional direction As RecursionDirection = erdDown _
 )
   On Error GoTo ErrorExit

   If acmEntityType = eactClass Then
     genAttrListForClassRecursive acmEntityIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forLrt, forGen, outputMode, erdDown
   ElseIf acmEntityType = eactRelationship Then
     genAttrDeclsForRelationship acmEntityIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, , False, forLrt, outputMode
   ElseIf acmEntityType = eactEnum Then
     genAttrDeclsForEnum acmEntityIndex, fileNo, ddlType, thisOrgIndex, thisPoolIndex
   End If
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrListForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef transformation As AttributeListTransformation, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional useVersiontag As Boolean = True, _
   Optional forLrt As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomListLrt, _
   Optional direction As RecursionDirection = erdDown _
 )
   On Error GoTo ErrorExit

   If acmEntityType = eactClass Then
     genTransformedAttrListForClassRecursive acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forLrt, forGen, outputMode, direction
   ElseIf acmEntityType = eactRelationship Then
     genTransformedAttrDeclsForRelationship acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, , False, forLrt, outputMode
   ElseIf acmEntityType = eactEnum Then
     genTransformedAttrDeclsForEnum acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, outputMode, useVersiontag
   End If
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrListForEntityWithColReuse( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forLrt As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomListLrt, _
   Optional direction As RecursionDirection = erdDown _
 )
   On Error GoTo ErrorExit
 
   If acmEntityType = eactClass Then
     genTransformedAttrListForClassRecursiveWithColReuse acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, forLrt, forGen, outputMode, direction
   ElseIf acmEntityType = eactRelationship Then
     genTransformedAttrDeclsForRelationshipWithColReUse acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, , False, forLrt, outputMode
   ElseIf acmEntityType = eactEnum Then
     genTransformedAttrDeclsForEnumWithColReuse acmEntityIndex, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent, outputMode
   End If
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrDeclsForEntityWithColReUse( _
   acmEntityType As AcmAttrContainerType, _
   acmEntityIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional forSubClass As Boolean = False, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional useSurrogateKey As Boolean = True, _
   Optional classIsGenForming As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional suppressOid As Boolean = False, _
   Optional enforceClassId As Boolean = False, _
   Optional isUserTransactional As Boolean = False, _
   Optional suppressTrailingComma As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 1, _
   Optional suppressLrtStatus As Boolean = False, _
   Optional ByRef genParentTabName As String = "", _
   Optional suppressColConstraints As Boolean = False, _
   Optional useAlternativeDefaults As Boolean = False, Optional suppressMetaAttrs As Boolean = False _
 )
   Dim i As Integer
   Dim attrSpecifics As String
   Dim attrRefs As AttrDescriptorRefs
   Dim isAggregate As Boolean
   Dim forLrtMqt As Boolean
   Dim entitySectionName As String
   Dim entityName As String
   Dim entityIdStr As String
   Dim poolSupportLrt As Boolean
 ' ### IF IVK ###
   Dim defaultStatus As Integer
   Dim isPsForming As Boolean
   Dim supportPsCopy As Boolean
   Dim ahSupportPsCopy As Boolean
   Dim condenseData As Boolean
   Dim instantiateExpressions As Boolean

   condenseData = False
 ' ### ENDIF IVK ###

   If thisPoolIndex > 0 Then
 ' ### IF IVK ###
       instantiateExpressions = g_pools.descriptors(thisPoolIndex).instantiateExpressions
 ' ### ENDIF IVK ###
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
 ' ### IF IVK ###
   Else
     instantiateExpressions = False
 ' ### ENDIF IVK ###
   End If

   On Error GoTo ErrorExit

   forLrtMqt = forLrt And ((outputMode And edomMqtLrt) = edomMqtLrt)

   ' FIXME: in general we need to set this depending on the class resp. relationship

   If acmEntityType = eactClass Then
       entitySectionName = g_classes.descriptors(acmEntityIndex).sectionName
       entityName = g_classes.descriptors(acmEntityIndex).className
       attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
 ' ### IF IVK ###
       defaultStatus = g_classes.descriptors(acmEntityIndex).defaultStatus
       isPsForming = g_classes.descriptors(acmEntityIndex).isPsForming
       supportPsCopy = g_classes.descriptors(acmEntityIndex).supportExtendedPsCopy
       If g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0 Then
         ahSupportPsCopy = g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).supportExtendedPsCopy
       End If
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
 ' ### ENDIF IVK ###
       isAggregate = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0)
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
   ElseIf acmEntityType = eactRelationship Then
       entitySectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       entityName = g_relationships.descriptors(acmEntityIndex).relName

       attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
 ' ### IF IVK ###
       defaultStatus = g_relationships.descriptors(acmEntityIndex).defaultStatus
       isPsForming = g_relationships.descriptors(acmEntityIndex).isPsForming
       supportPsCopy = g_relationships.descriptors(acmEntityIndex).supportExtendedPsCopy
       If g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex > 0 Then
         ahSupportPsCopy = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).supportExtendedPsCopy
       End If
 ' ### ENDIF IVK ###
       isAggregate = (g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex > 0)
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
   ElseIf acmEntityType = eactEnum Then
       entitySectionName = g_enums.descriptors(acmEntityIndex).sectionName
       entityName = g_enums.descriptors(acmEntityIndex).enumName

       attrRefs = g_enums.descriptors(acmEntityIndex).attrRefs
 ' ### IF IVK ###
       defaultStatus = statusReadyForActivation
       isPsForming = False
 ' ### ENDIF IVK ###
       isAggregate = False
       entityIdStr = g_enums.descriptors(acmEntityIndex).enumIdStr
   End If

   If Not forSubClass And Not suppressMetaAttrs Then
     If Not suppressOid Then
       If useSurrogateKey Then
         If forGen And acmEntityType = eactClass Then
             If genParentTabName <> "" Then
               printSectionHeader "Foreign Key to 'Parent Table' (" & genParentTabName & ")", fileNo, outputMode
             End If

             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 g_classes.descriptors(acmEntityIndex).shortName & "_" & cosnOid, g_classes.descriptors(acmEntityIndex).shortName & "_" & cosnOid, _
                 eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, _
                 , outputMode, eacFkOid Or eacFkOidParent, , indent, , "[LDM] Foreign Key / Object Identifier" _
               )
         End If
         printSectionHeader "Surrogate Key", fileNo, outputMode
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conOid, cosnOid, eavtDomain, _
             g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, _
             , outputMode, eacOid, , indent, , "[LDM] Object Identifier" _
           )
       End If

       If isUserTransactional And g_genLrtSupport And (outputMode And edomMqtLrt) Then
         printSectionHeader "Flag '" & conIsLrtPrivate & "'", fileNo, outputMode
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conIsLrtPrivate, cosnIsLrtPrivate, eavtDomain, g_domainIndexIsLrtPrivate, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, eacMqtLrtMeta, , indent, , _
             "[LRT-MQT] identifies 'LRT-private' records", "0" _
           )
 ' ### IF IVK ###
         If Not condenseData Then
           printSectionHeader "Column '" & conInUseBy & "'", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, transformation, _
               tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, eacMqtLrtMeta, , indent, , _
               "[LRT-MQT] identifies the user holding the lock on the record", , , True _
             )
         End If
 ' ### ELSE IVK ###
 '       printSectionHeader "Column '" & conInUseBy & "'", fileNo, outputMode
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( _
 '           conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, transformation, _
 '           tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, eacMqtLrtMeta, , indent, , _
 '           "[LRT-MQT] identifies the user holding the lock on the record", , True _
 '         )
 ' ### ENDIF IVK ###
       End If
 
       If isUserTransactional And g_genLrtSupport And ((outputMode And edomListNoLrt) <> edomListNoLrt) Then
 ' ### IF IVK ###
         If (forLrt Or Not condenseData) Then
           printSectionHeader "LRT - Id", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, transformation, _
               tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt And Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
               outputMode And IIf(((outputMode And edomValue) <> 0) And ((outputMode And edomLrtPriv) <> 0), Not edomList, Not 0), _
               eacLrtMeta, , indent, , "[LRT] Identifier of LRT the record is involved in", , , Not forLrt Or forLrtMqt _
             )
         End If
 ' ### ELSE IVK ###
 '       If forLrt Then
 '         printSectionHeader "LRT - Id", fileNo, outputMode
 '         printConditional fileNo, _
 '           genTransformedAttrDeclByDomainWithColReUse( _
 '             conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, transformation, _
 '             tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt And Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
 '             outputMode And IIf(((outputMode And edomValue) <> 0) And ((outputMode And edomLrtPriv) <> 0), Not edomList, Not 0), _
 '             eacLrtMeta, , indent, , "[LRT] Identifier of LRT the record is involved in", , Not forLrt Or forLrtMqt _
 '           )
 '       End If
 ' ### ENDIF IVK ###
 
 ' ### IF IVK ###
         If Not condenseData Then
           printSectionHeader "Flag 'status'", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               enStatus, esnStatus, eavtEnum, g_enumIndexStatus, transformation, _
               tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt Or forLrtMqt, "", "NOT NULL DEFAULT " & IIf(useAlternativeDefaults, statusProductive, statusWorkInProgress)), , _
               ddlType, , outputMode, eacLrtMeta Or eacSetProdMeta, , indent, , _
               "[ACM] Specifies the state of the record with respect to 'release to production'", CStr(defaultStatus) _
             )
         End If
 ' ### ENDIF IVK ###
       End If

       If isAggregate Then
         ' LRT-specific columns wich exist in public and in private tables
         printSectionHeader "ClassId of 'aggregate head'", fileNo, outputMode
 ' ### IF IVK ###
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conAhClassId, cosnAggHeadClassId, eavtDomain, g_domainIndexCid, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), , _
             ddlType, , outputMode, eacCid Or eacLrtMeta, , indent, , _
             "[MET] ID of the ACM-class of the 'Aggregate Head'", , , Not generateAhIdsNotNull Or useAlternativeDefaults _
           )
 ' ### ELSE IVK ###
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( _
 '           conAhClassId, cosnAggHeadClassId, eavtDomain, g_domainIndexCid, transformation, _
 '           tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), , _
 '           ddlType, , outputMode, eacCid Or eacLrtMeta, , indent, , _
 '           "[MET] ID of the ACM-class of the 'Aggregate Head'", , Not generateAhIdsNotNull Or useAlternativeDefaults _
 '         )
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
         printSectionHeader "ObjectId of 'aggregate head'", fileNo, outputMode
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conAhOId, cosnAggHeadOId, eavtDomain, g_domainIndexOid, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), _
             , ddlType, , outputMode, _
             eacFkOid Or eacLrtMeta Or eacAhOid Or IIf(isPsForming, eacPsFormingOid, 0) Or IIf(ahSupportPsCopy, eacFkExtPsCopyOid, 0), , indent, , _
             "[MET] Object ID of the 'Aggregate Head'", , , Not generateAhIdsNotNull Or useAlternativeDefaults _
           )
       End If

       If isUserTransactional And g_genLrtSupport And ((outputMode And edomListNoLrt) <> edomListNoLrt) Then
         If hasBeenSetProductiveInPrivLrt Then
           If isUserTransactional And g_genLrtSupport Then
             printSectionHeader "Flag 'hasBeenSetProductive'", fileNo, outputMode
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 conHasBeenSetProductive, cosnHasBeenSetProductive, eavtDomain, g_domainIndexBoolean, transformation, _
                 tabColumns, acmEntityType, acmEntityIndex, IIf(Not hasBeenSetProductiveInPrivLrt And (forLrt Or forLrtMqt), "", "NOT NULL DEFAULT " & IIf(useAlternativeDefaults, 1, 0)), , _
                 ddlType, , outputMode, eacLrtMeta, , indent, , _
                 "[LRT] Specifies whether record has been set productive", "0" _
               )
           End If
         End If
 ' ### ELSE IVK ###
 '       printSectionHeader "ObjectId of 'aggregate head'", fileNo, outputMode
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( _
 '           conAhOId, cosnAggHeadOId, eavtDomain, g_domainIndexOid, transformation, _
 '           tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), _
 '           , ddlType, , outputMode, eacFkOid Or eacLrtMeta Or eacAhOid , , indent, , _
 '           "[MET] Object ID of the 'Aggregate Head'", , Not generateAhIdsNotNull Or useAlternativeDefaults _
 '         )
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
         If forLrt And (outputMode And edomValueNonLrt) Then
           If Not hasBeenSetProductiveInPrivLrt Then
             printSectionHeader "Flag 'hasBeenSetProductive'", fileNo, outputMode
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 conHasBeenSetProductive, cosnHasBeenSetProductive, eavtDomain, g_domainIndexBoolean, transformation, _
                 tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , edomValueLrt Or (outputMode And edomDefaultValue), eacLrtMeta, , indent, , , gc_dbFalse, , True _
               )
           End If
           If Not condenseData Then
             printSectionHeader "Flag 'isDeleted'", fileNo, outputMode
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 conIsDeleted, conIsDeleted, eavtDomain, g_domainIndexBoolean, transformation, _
                 tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , edomValueLrt Or (outputMode And edomDefaultValue), eacLrtMeta, , indent, , , gc_dbFalse, , True _
               )
           End If
         ElseIf (Not forLrt Or (outputMode And (edomListNonLrt Or edomDeclNonLrt))) Then
           If Not hasBeenSetProductiveInPrivLrt Then
             printSectionHeader "Flag 'hasBeenSetProductive'", fileNo, outputMode
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 conHasBeenSetProductive, cosnHasBeenSetProductive, eavtDomain, g_domainIndexBoolean, transformation, _
                 tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt And Not forLrtMqt, "", "NOT NULL DEFAULT " & IIf(useAlternativeDefaults, 1, 0)), , ddlType, , _
                 outputMode, eacLrtMeta, , indent, , _
                 "[LRT] Specifies whether record has been set productive", "0" _
               )
           End If
           If Not condenseData Then
             printSectionHeader "Flag 'IsDeleted'", fileNo, outputMode
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 conIsDeleted, cosnIsDeleted, eavtDomain, g_domainIndexBoolean, transformation, _
                 tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt And Not forLrtMqt, "", "NOT NULL DEFAULT 0"), , ddlType, , _
                 outputMode, eacLrtMeta, , indent, , _
                 "[LRT] Specifies whether record logically has been deleted", "0" _
               )
           End If
         End If

 ' ### ENDIF IVK ###
         ' columns which exist in private and not in public tables
         If Not forLrt And (outputMode And edomValueLrt) Then
           printSectionHeader "LRT - Status (locked[" & lrtStatusLocked & "], created[" & lrtStatusCreated & "], updated[" & lrtStatusUpdated & "], deleted[" & lrtStatusDeleted & "])", fileNo, edomValueLrt
 ' ### IF IVK ###
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, _
               transformation, tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
               edomValueLrt, eacLrtMeta, , indent, , , , , forLrtMqt _
             )
 ' ### ELSE IVK ###
 '         printConditional fileNo, _
 '           genTransformedAttrDeclByDomainWithColReUse( _
 '             conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, _
 '             transformation, tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
 '             edomValueLrt, eacLrtMeta, , indent, , , , forLrtMqt _
 '           )
 ' ### ENDIF IVK ###
         ElseIf forLrt Or (outputMode And (edomListLrt Or edomDeclLrt)) Then
           printSectionHeader "LRT - Status (locked[" & lrtStatusLocked & "], created[" & lrtStatusCreated & "], updated[" & lrtStatusUpdated & "], deleted[" & lrtStatusDeleted & "])", fileNo, outputMode
 ' ### IF IVK ###
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, _
               transformation, tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , outputMode, eacLrtMeta, , indent, , _
               "[LRT] Record status with respect to its involvement in an LRT (locked[" & lrtStatusLocked & "], created[" & lrtStatusCreated & "], updated[" & lrtStatusUpdated & "], deleted[" & lrtStatusDeleted & "])", , , forLrtMqt _
             )
 ' ### ELSE IVK ###
 '         printConditional fileNo, _
 '           genTransformedAttrDeclByDomainWithColReUse( _
 '             conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, _
 '             transformation, tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , outputMode, eacLrtMeta, , indent, , _
 '             "[LRT] Record status with respect to its involvement in an LRT (locked[" & lrtStatusLocked & "], created[" & lrtStatusCreated & "], updated[" & lrtStatusUpdated & "], deleted[" & lrtStatusDeleted & "])", , forLrtMqt _
 '           )
 ' ### ENDIF IVK ###
         End If
       End If

       If acmEntityType = eactClass Then
           If Not g_classes.descriptors(acmEntityIndex).notAcmRelated And ((enforceClassId And entityIdStr <> "" And Not g_classes.descriptors(acmEntityIndex).hasOwnTable) Or g_classes.descriptors(acmEntityIndex).hasSubClass) Then
             printSectionHeader "Class ID", fileNo, outputMode
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 conClassId, cosnClassId, eavtDomain, g_domainIndexCid, _
                 transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, eacCid, , indent, , _
                 "[LDM] Class Identifier" _
               )
           End If
       End If
     End If
   End If

   Dim printedHeader As Boolean
   printedHeader = False

   Dim attrIsReUsed As Boolean
   For i = 1 To attrRefs.numDescriptors
     attrIsReUsed = False
 ' ### IF IVK ###
       If ((outputMode And edomXsd) Or (outputMode And edomXml)) And g_attributes.descriptors(attrRefs.descriptors(i).refIndex).noXmlExport Then
         GoTo NextI
       End If
       If g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isExpression Then
         If outputMode And edomXsd Then
         ElseIf outputMode And edomXml Then
         ElseIf Not instantiateExpressions And outputMode <> edomNone Then
           If (outputMode And (edomMqtLrt Or edomExpression Or edomExpressionDummy)) = 0 Then
             GoTo NextI
           ElseIf (outputMode And edomMqtLrt) And Not includeTermStringsInMqt Then
             GoTo NextI
           End If
         End If
       End If
 
 ' ### ENDIF IVK ###
       If UCase(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).sectionName) = UCase(entitySectionName) And UCase(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).className) = UCase(entityName) And _
              (g_attributes.descriptors(attrRefs.descriptors(i).refIndex).cType = acmEntityType) And IIf(classIsGenForming, forGen = g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isTimeVarying, True) Then
         Dim isNullable As Boolean
         Dim default As String
         isNullable = g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNullable
         default = g_attributes.descriptors(attrRefs.descriptors(i).refIndex).default

         If g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNullableInOrgs <> "" And thisOrgIndex > 0 Then
           If includedInList(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNullableInOrgs, g_orgs.descriptors(thisOrgIndex).id) Then
             isNullable = True
             default = ""
           End If
         End If

         attrSpecifics = IIf((g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNl Or isNullable Or forSubClass) And _
                              Not (g_attributes.descriptors(attrRefs.descriptors(i).refIndex).domainName = dnBoolean And g_attributes.descriptors(attrRefs.descriptors(i).refIndex).domainSection = dxnBoolean), _
                             "", IIf(suppressColConstraints And Not g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isIdentifying, "", "NOT NULL"))
         attrSpecifics = attrSpecifics & IIf(Trim(default) = "", "", IIf(attrSpecifics = "", "", " ") & "DEFAULT " & default & IIf(ddlType = edtPdm And g_attributes.descriptors(attrRefs.descriptors(i).refIndex).compressDefault, " COMPRESS SYSTEM DEFAULT", ""))
         Dim attrNameSuffix As String
         attrNameSuffix = ""
         If g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNl Then
           transformation.containsNlAttribute = True
           attrNameSuffix = langDfltSuffix
           If transformation.doCollectAttrDescriptors Then
             addAttrDescriptorRef transformation.nlAttrRefs, attrRefs.descriptors(i).refIndex
             If g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isTimeVarying Then
               transformation.numNlAttrRefsTv = transformation.numNlAttrRefsTv + 1
             Else
               transformation.numNlAttrRefsNonTv = transformation.numNlAttrRefsNonTv + 1
             End If
           End If
           If transformation.doCollectDomainDescriptors Then
             addDomainDescriptorRef transformation.domainRefs, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).domainIndex, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNullable, transformation.distinguishNullabilityForDomainRefs
           End If
         End If
         If acmEntityType = eactClass And Not printedHeader And Not forSubClass Then
           printSectionHeader "attributes for """ & UCase(entitySectionName & "." & entityName) & """" & _
                              IIf(entityIdStr <> "", " (ClassId='" & entityIdStr & "')", ""), fileNo, outputMode
           printedHeader = True
         End If

         If Not g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNl Then
           Dim attrComment As String
           If (outputMode And edomComment <> 0) And g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attrNlIndex > 0 Then
               If g_attributesNl.descriptors(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attrNlIndex).nl(gc_langIdEnglish) <> "" Then
                 attrComment = " (" & g_attributesNl.descriptors(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attrNlIndex).nl(gc_langIdEnglish) & ")"
               End If
           End If

           printComment """" & g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName & """ (" & g_attributes.descriptors(attrRefs.descriptors(i).refIndex).domainSection & "." & g_attributes.descriptors(attrRefs.descriptors(i).refIndex).domainName & ")", fileNo, outputMode
           ' pass default value to 'genTransformedAttrDeclByDomainWithColReUse' to support outputmode 'edomDefaultValue'
 ' ### IF IVK ###
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName & attrNameSuffix, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).shortName & attrNameSuffix, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).valueType, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).valueTypeIndex, _
               transformation, tabColumns, acmEntityType, acmEntityIndex, attrSpecifics, (Not suppressTrailingComma) Or (i <> attrRefs.numDescriptors), _
               ddlType, , outputMode, IIf(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isExpression, eacExpression, eacRegular), , indent, attrIsReUsed, _
               "[ACM] Attribute '" & g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName & "'" & attrComment, default, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isVirtual, isNullable Or g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNationalizable, attrRefs.descriptors(i).refIndex _
             )
 ' ### ELSE IVK ###
 '         printConditional fileNo, '
 '           genTransformedAttrDeclByDomainWithColReUse( _
 '             .attributeName & attrNameSuffix, .shortName & attrNameSuffix, .valueType, .valueTypeIndex, _
 '             transformation, tabColumns, acmEntityType, acmEntityIndex, attrSpecifics, (Not suppressTrailingComma) Or (i <> attrRefs.numDescriptors), _
 '             ddlType, , outputMode, eacRegular, , indent, attrIsReUsed, _
 '             "[ACM] Attribute '" & .attributeName & "'" & attrComment, default, isNullable, attrRefs.descriptors(i).refIndex _
 '           )
 ' ### ENDIF IVK ###
         End If
 ' ### IF IVK ###

         If Not g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNl And g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNationalizable And Not attrIsReUsed Then
           printComment "nationalized attribute """ & g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName & """ (" & g_attributes.descriptors(attrRefs.descriptors(i).refIndex).domainSection & "." & g_attributes.descriptors(attrRefs.descriptors(i).refIndex).domainName & ")", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName & attrNameSuffix & gc_anSuffixNat, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).shortName & attrNameSuffix & gc_asnSuffixNat, _
                g_attributes.descriptors(attrRefs.descriptors(i).refIndex).valueType, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, attrSpecifics, , _
               ddlType, , outputMode, IIf(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isExpression, eacExpression, eacRegular) Or eacNational, , indent, attrIsReUsed, _
               "[ACM] Attribute '" & g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName & "' (nationalized)", g_attributes.descriptors(attrRefs.descriptors(i).refIndex).default, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isVirtual, True, attrRefs.descriptors(i).refIndex _
             )
           printSectionHeader "Is the nationalized attribute active?", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName & attrNameSuffix & gc_anSuffixNatActivated, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).shortName & attrNameSuffix & gc_asnSuffixNatActivated, _
               eavtDomain, g_domainIndexBoolean, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT 0" & _
               IIf(ddlType = edtPdm And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), , _
               ddlType, , outputMode, IIf(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isExpression, eacExpression, eacRegular) Or eacNationalBool, , indent, attrIsReUsed, _
               "[ACM] Indicates whether nationalized attribute '" & g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName & "' is active", gc_dbFalse, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isVirtual, False, attrRefs.descriptors(i).refIndex _
             )
         End If
 ' ### ENDIF IVK ###
       End If
 NextI:
   Next i
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedAttrDeclsForEntity( _
   acmEntityType As AcmAttrContainerType, _
   acmEntityIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   Optional forSubClass As Boolean = False, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional useSurrogateKey As Boolean = True, _
   Optional classIsGenForming As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional suppressOid As Boolean = False, _
   Optional isUserTransactional As Boolean = False, _
   Optional suppressTrailingComma As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 1, _
   Optional suppressLrtStatus As Boolean = False, _
   Optional ByRef genParentTabName As String = "", _
   Optional suppressColConstraints As Boolean = False _
 )
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   On Error GoTo ErrorExit

   genTransformedAttrDeclsForEntityWithColReUse _
     acmEntityType, acmEntityIndex, transformation, _
     tabColumns, forSubClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, useSurrogateKey, classIsGenForming, forGen, suppressOid, , _
     isUserTransactional, suppressTrailingComma, forLrt, outputMode, indent, suppressLrtStatus, genParentTabName, suppressColConstraints
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Sub genAttrDeclsForEntity( _
   acmEntityType As AcmAttrContainerType, _
   acmEntityIndex As Integer, _
   Optional forSubClass As Boolean = False, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional useSurrogateKey As Boolean = True, _
   Optional classIsGenForming As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional suppressOid As Boolean = False, _
   Optional classIsTransactional As Boolean = False, _
   Optional suppressTrailingComma As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 1, _
   Optional suppressLrtStatus As Boolean = False, _
   Optional ByRef genParentTabName As String = "", _
   Optional suppressColConstraints As Boolean = False _
 )
   On Error GoTo ErrorExit

   genTransformedAttrDeclsForEntity _
     acmEntityType, acmEntityIndex, _
     nullAttributeTransformation, forSubClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, useSurrogateKey, classIsGenForming, forGen, _
     suppressOid, classIsTransactional, suppressTrailingComma, forLrt, outputMode, indent, suppressLrtStatus, genParentTabName, suppressColConstraints
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Function genFkTransformedAttrDeclsWithColReuse( _
   ByVal acmClassIndex As Integer, _
   ByRef attrSpecifics As String, _
   ByRef isPsForming As Boolean, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional ByRef refClassName As String = "", _
   Optional ByRef refClassShortName As String = "", _
   Optional fileNo As Integer = 1, Optional ddlType As DdlTypeId = edtLdm, _
   Optional addComma As Boolean = True, _
   Optional nationalized As Boolean = False, _
   Optional returnDecls As Boolean = False, _
   Optional ByRef attrDecls As String = "", _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 1, _
   Optional isOptional As Boolean = False _
 ) As String
   genFkTransformedAttrDeclsWithColReuse = ""

   On Error GoTo ErrorExit

   Dim attrList As String, decl As String
   attrList = ""
   Dim sectionName As String
   Dim clasName As String
     If g_classes.descriptors(acmClassIndex).useSurrogateKey Then
 ' ### IF IVK ###
       attrList = genSurrogateKeyName(ddlType, IIf(reuseColumnsInTabsForOrMapping, refClassShortName, ""), , , , nationalized)

       printComment """" & g_classes.descriptors(acmClassIndex).className & IIf(g_classes.descriptors(acmClassIndex).className = "", "", ":") & conOid & """ (" & dxnOid & "." & dnOid & ")", fileNo, outputMode
       decl = genTransformedAttrDeclByDomainWithColReUse(genSurrogateKeyName(ddlType, IIf(reuseColumnsInTabsForOrMapping, refClassShortName, ""), , , , nationalized), _
              genSurrogateKeyShortName(ddlType, IIf(reuseColumnsInTabsForOrMapping, refClassShortName, "") & IIf(nationalized, "_" & gc_asnSuffixNat, "")), _
              eavtDomain, g_domainIndexOid, transformation, tabColumns, eactClass, acmClassIndex, attrSpecifics, addComma, ddlType, , outputMode, _
              eacFkOid Or IIf(isPsForming, eacPsFormingOid, 0) Or IIf(g_classes.descriptors(acmClassIndex).supportExtendedPsCopy, eacFkExtPsCopyOid, 0) Or IIf(nationalized, eacNational, 0), , _
              indent, , "[LDM] Foreign Key to ACM-class '" & g_classes.descriptors(acmClassIndex).className & "'", , , isOptional)
 ' ### ELSE IVK ###
 '      attrList = genSurrogateKeyName(ddlType, IIf(reuseColumnsInTabsForOrMapping, refClassShortName, ""))
 '
 '     printComment """" & .className & IIf(.className = "", "", ":") & conOid & """ (" & dxnOID & "." & dnOID & ")", fileNo, outputMode
 '      decl = genTransformedAttrDeclByDomainWithColReUse(genSurrogateKeyName(ddlType, IIf(reuseColumnsInTabsForOrMapping, refClassShortName, "")), _
 '             genSurrogateKeyShortName(ddlType, IIf(reuseColumnsInTabsForOrMapping, refClassShortName, "")), _
 '             eavtDomain, g_domainIndexOid, transformation, tabColumns, eactClass, acmClassIndex, attrSpecifics, addComma, ddlType, , outputMode, _
 '             eacFkOid, , indent, , "[LDM] Foreign Key to ACM-class '" & .className & "'", , isOptional)
 ' ### ENDIF IVK ###
       If returnDecls Then
         attrDecls = attrDecls & IIf(attrDecls = "", "", vbCrLf) & decl
       End If
       printConditional fileNo, decl
     Else
       Dim i As Integer, numAttrs As Integer
       numAttrs = 0
       For i = 1 To g_attributes.numDescriptors Step 1
           If UCase(g_attributes.descriptors(i).sectionName) = UCase(g_classes.descriptors(acmClassIndex).sectionName) And UCase(g_attributes.descriptors(i).className) = UCase(g_classes.descriptors(acmClassIndex).className) And g_attributes.descriptors(i).isIdentifying Then
             numAttrs = numAttrs + 1
           End If
       Next i

       For i = 1 To g_attributes.numDescriptors Step 1
           If UCase(g_attributes.descriptors(i).sectionName) = UCase(g_classes.descriptors(acmClassIndex).sectionName) And UCase(g_attributes.descriptors(i).className) = UCase(g_classes.descriptors(acmClassIndex).className) And g_attributes.descriptors(i).isIdentifying Then
             attrList = IIf(attrList = "", "", ",") & g_attributes.descriptors(i).attributeName

             printComment """" & g_attributes.descriptors(i).className & IIf(g_attributes.descriptors(i).className <> "", ":", "") & g_attributes.descriptors(i).attributeName & """ (" & g_attributes.descriptors(i).domainSection & "." & g_attributes.descriptors(i).domainName & ")", fileNo, outputMode
 ' ### IF IVK ###
             decl = genTransformedAttrDeclByDomainWithColReUse(g_attributes.descriptors(i).attributeName, g_attributes.descriptors(i).shortName, g_attributes.descriptors(i).valueType, g_attributes.descriptors(i).valueTypeIndex, transformation, tabColumns, _
                    eactClass, acmClassIndex, attrSpecifics, addComma Or (i < numAttrs), ddlType, , outputMode, , , indent, , , , , isOptional)
 ' ### ELSE IVK ###
 '           decl = genTransformedAttrDeclByDomainWithColReUse(.attributeName, .shortName, .valueType, .valueTypeIndex, transformation, tabColumns, _
 '                  eactClass, acmClassIndex, attrSpecifics, addComma Or (i < numAttrs), ddlType, , outputMode, , , indent, , , , isOptional)
 ' ### ENDIF IVK ###
             printConditional fileNo, decl
             If returnDecls Then
               attrDecls = attrDecls & IIf(attrDecls = "", "", vbCrLf) & decl
             End If
           End If
       Next i
     End If

   genFkTransformedAttrDeclsWithColReuse = attrList
 
 NormalExit:
   On Error Resume Next
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 
 
 Function genFkTransformedAttrDecls( _
   ByVal acmClassIndex As Integer, _
   ByRef attrSpecifics As String, _
   ByRef isPsForming As Boolean, _
   ByRef transformation As AttributeListTransformation, _
   Optional ByRef refClassName As String = "", _
   Optional ByRef refClassShortName As String = "", _
   Optional fileNo As Integer = 1, Optional ddlType As DdlTypeId = edtLdm, _
   Optional addComma As Boolean = True, _
   Optional nationalized As Boolean = False, _
   Optional returnDecls As Boolean = False, _
   Optional ByRef attrDecls As String = "", _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 1, _
   Optional isOptional As Boolean = False _
 ) As String
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   On Error GoTo ErrorExit
 
 ' ### IF IVK ###
   genFkTransformedAttrDecls = genFkTransformedAttrDeclsWithColReuse(acmClassIndex, attrSpecifics, isPsForming, transformation, tabColumns, refClassName, refClassShortName, _
                                   fileNo, ddlType, addComma, nationalized, returnDecls, attrDecls, outputMode, indent, isOptional)
 ' ### ELSE IVK ###
 '  genFkTransformedAttrDecls = genFkTransformedAttrDeclsWithColReuse(acmClassIndex, attrSpecifics, False, transformation, tabColumns, refClassName, refClassShortName, _
 '                                  fileNo, ddlType, addComma, nationalized, returnDecls, attrDecls, outputMode, indent, isOptional)
 ' ### ENDIF IVK ###
 
 NormalExit:
   On Error Resume Next
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 
 
 ' ### IF IVK ###
 Sub genFkTransformedAttrDeclsForRelationshipWithColReUse( _
   targetClassIndex As Integer, _
   ByRef acmRelIndex As Integer, _
   ByRef relationshipNameShort As String, _
   ByRef concatRelNameShort As Boolean, _
   ByRef relationshipIsNationalizable As Boolean, _
   ByRef attrSpecifics As String, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional fileNo As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 1, _
   Optional addComma As Boolean = True _
 )
 ' ### ELSE IVK ###
 'Sub genFkTransformedAttrDeclsForRelationshipWithColReUse( _
 ' targetClassIndex As Integer, _
 ' ByRef acmRelIndex As Integer, _
 ' ByRef relationshipNameShort As String, _
 ' ByRef concatRelNameShort As Boolean, _
 ' ByRef attrSpecifics As String, _
 ' ByRef transformation As AttributeListTransformation, _
 ' ByRef tabColumns As EntityColumnDescriptors, _
 '  Optional fileNo As Integer = 1, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
 ' Optional indent As Integer = 1, _
 ' Optional addComma As Boolean = True _
 ')
 ' ### ENDIF IVK ###
   On Error GoTo ErrorExit

   Dim isOptional As Boolean
   isOptional = Not InStr(1, UCase(attrSpecifics), "NOT NULL")

   Dim attrCat As AttrCategory
   attrCat = eacFkOid

   Dim relName As String
   If acmRelIndex > 0 Then
 ' ### IF IVK ###
       If g_relationships.descriptors(acmRelIndex).isMdsExpressionRel Then attrCat = attrCat Or eacFkOidExpression
 ' ### ENDIF IVK ###
       relName = g_relationships.descriptors(acmRelIndex).relName
   End If

 ' ### IF IVK ###
     If g_classes.descriptors(targetClassIndex).classIndex = g_classIndexCountryIdList Then
       attrCat = attrCat Or eacFkCountryIdList
     End If

 ' ### ENDIF IVK ###
     If g_classes.descriptors(targetClassIndex).useSurrogateKey Then
 ' ### IF IVK ###
       attrCat = attrCat Or IIf(g_classes.descriptors(targetClassIndex).supportExtendedPsCopy, eacFkExtPsCopyOid, 0) Or IIf(g_classes.descriptors(targetClassIndex).isSubjectToExpCopy, eacFkOidExpElement, 0)

 ' ### ENDIF IVK ###
       printComment """" & relationshipNameShort & IIf(relationshipNameShort = "", "", ":") & conOid & """ (" & dxnOid & "." & dnOid & ")", fileNo, outputMode
 ' ### IF IVK ###
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conOid, cosnOid, eavtDomain, g_domainIndexOid, _
           transformation, tabColumns, eactClass, g_classes.descriptors(targetClassIndex).classIndex, attrSpecifics, addComma Or relationshipIsNationalizable, ddlType, _
           relationshipNameShort, outputMode, attrCat Or IIf(g_classes.descriptors(targetClassIndex).isPsForming, eacPsFormingOid, 0), acmRelIndex, indent, , _
           "[LDM] Foreign Key corresponding to ACM-relationship '" & relName & "' :-> '" & g_classes.descriptors(targetClassIndex).sectionName & "." & g_classes.descriptors(targetClassIndex).className & "'", _
           , , isOptional _
         )
       If relationshipIsNationalizable Then
         printSectionHeader "nationalized Relationship", fileNo, outputMode
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conOid & gc_anSuffixNat, cosnOid & gc_anSuffixNat, _
             eavtDomain, g_domainIndexOid, transformation, tabColumns, eactClass, g_classes.descriptors(targetClassIndex).classIndex, attrSpecifics, , ddlType, _
             relationshipNameShort, outputMode, attrCat Or IIf(g_classes.descriptors(targetClassIndex).isPsForming, eacPsFormingOid, 0) Or eacNational, acmRelIndex, indent, , _
             "[LDM] Foreign Key corresponding to ACM-relationship (national) '" & relName & "' :-> '" & g_classes.descriptors(targetClassIndex).sectionName & "." & g_classes.descriptors(targetClassIndex).className & "'", , , isOptional _
           )
         printSectionHeader "Is nationalized Relationship active?", fileNo, outputMode
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conOid & gc_anSuffixNatActivated, cosnOid & "_" & gc_asnSuffixNatActivated, _
             eavtDomain, g_domainIndexBoolean, transformation, tabColumns, eactClass, g_classes.descriptors(targetClassIndex).classIndex, "NOT NULL DEFAULT 0" & _
             IIf(ddlType = edtPdm And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), addComma, _
             ddlType, relationshipNameShort, outputMode, (attrCat Or eacRegular Or eacNationalBool) And Not (eacFkOid Or eacFkExtPsCopyOid), acmRelIndex, indent, _
             , "[LDM] Is nationalized Relationship active?", gc_dbFalse _
           )
       End If
 ' ### ELSE IVK ###
 '     printConditional fileNo, _
 '       genTransformedAttrDeclByDomainWithColReUse( _
 '         conOid, cosnOid, _
 '         eavtDomain, g_domainIndexOid, transformation, tabColumns, eactClass, .classIndex, attrSpecifics, addComma , ddlType, _
 '         relationshipNameShort, outputMode, attrCat, acmRelIndex, indent, , _
 '         "[LDM] Foreign Key corresponding to ACM-relationship '" & relName & "' :-> '" & .sectionName & "." & .className & "'", _
 '         , isOptional _
 '       )
 ' ### ENDIF IVK ###
     Else
       Dim i As Integer
       Dim thisClassIndex As Integer
       thisClassIndex = g_classes.descriptors(targetClassIndex).classIndex
       While thisClassIndex > 0
           For i = 1 To g_classes.descriptors(thisClassIndex).attrRefs.numDescriptors Step 1
               If g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).isIdentifying Then
                 printComment """" & relationshipNameShort & IIf(relationshipNameShort = "", "", ":") & g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).attributeName & """ (" & g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).domainSection & "." & g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).domainName & ")", fileNo, outputMode
 ' ### IF IVK ###
                 printConditional fileNo, _
                   genTransformedAttrDeclByDomainWithColReUse( _
                     g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).attributeName, g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).shortName, g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).valueType, g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).valueTypeIndex, _
                     transformation, tabColumns, eactClass, thisClassIndex, attrSpecifics, , ddlType, _
                     relationshipNameShort, outputMode, attrCat, acmRelIndex, indent, , , , , isOptional _
                   )
                 If relationshipIsNationalizable Then
                   printSectionHeader "nationalized Relationship", fileNo
                   printConditional fileNo, _
                     genTransformedAttrDeclByDomainWithColReUse( _
                       g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).attributeName & gc_anSuffixNat, g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).shortName & gc_anSuffixNat, g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).valueType, g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).valueTypeIndex, _
                       transformation, tabColumns, eactClass, thisClassIndex, Replace(attrSpecifics, eactRelationship, acmRelIndex, "NOT NULL", ""), , ddlType, _
                       relationshipNameShort, outputMode, eacFkOid Or eacNational, acmRelIndex, indent, , , , , True _
                     )
                 End If
 ' ### ELSE IVK ###
 '               printConditional fileNo, _
 '                 genTransformedAttrDeclByDomainWithColReUse( _
 '                   .attributeName, .shortName, .valueType, .valueTypeIndex, _
 '                   transformation, tabColumns, eactClass, thisClassIndex, attrSpecifics, , ddlType, _
 '                   relationshipNameShort, outputMode, attrCat, acmRelIndex, indent, , , , isOptional _
 '                 )
 ' ### ENDIF IVK ###
               End If
           Next i

           If includeFksInPks Then
             Dim j As Integer
             For j = 1 To g_classes.descriptors(thisClassIndex).relRefs.numRefs
               If g_classes.descriptors(thisClassIndex).relRefs.refs(j).refType = etRight Then
                   If g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).isIdentifyingLeft And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).maxLeftCardinality = 1 Then
 ' ### IF IVK ###
                     genFkTransformedAttrDeclsForRelationshipWithColReUse g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).leftEntityIndex, _
                         g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).relIndex, IIf(concatRelNameShort, relationshipNameShort & "_" & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).shortName & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).rlShortRelName, relationshipNameShort), _
                         concatRelNameShort, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent
 ' ### ELSE IVK ###
 '                   genFkTransformedAttrDeclsForRelationshipWithColReUse .leftEntityIndex, _
 '                       .relIndex, IIf(concatRelNameShort, relationshipNameShort & "_" & .shortName & .rlShortRelName, relationshipNameShort), _
 '                       concatRelNameShort, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent
 ' ### ENDIF IVK ###
                   End If
               ElseIf g_classes.descriptors(thisClassIndex).relRefs.refs(j).refType = etLeft Then
                   If g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).isIdentifyingRight And g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).maxRightCardinality = 1 Then
 ' ### IF IVK ###
                     genFkTransformedAttrDeclsForRelationshipWithColReUse g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).rightEntityIndex, _
                         g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).relIndex, IIf(concatRelNameShort, relationshipNameShort & "_" & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).shortName & g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).lrShortRelName, relationshipNameShort), _
                         concatRelNameShort, g_relationships.descriptors(g_classes.descriptors(thisClassIndex).relRefs.refs(j).refIndex).isNationalizable, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent
 ' ### ELSE IVK ###
 '                   genFkTransformedAttrDeclsForRelationshipWithColReUse .rightEntityIndex, _
 '                       .relIndex, IIf(concatRelNameShort, relationshipNameShort & "_" & .shortName & .lrShortRelName, relationshipNameShort), _
 '                       concatRelNameShort, attrSpecifics, transformation, tabColumns, fileNo, ddlType, outputMode, indent
 ' ### ENDIF IVK ###
                   End If
               End If
             Next j
           End If
           thisClassIndex = g_classes.descriptors(thisClassIndex).superClassIndex
       Wend
 ' ### IF IVK ###
       If relationshipIsNationalizable Then
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conOid & gc_anSuffixNatActivated, cosnOid & "_" & gc_asnSuffixNatActivated, _
             eavtDomain, g_domainIndexBoolean, transformation, tabColumns, eactClass, g_classes.descriptors(targetClassIndex).classIndex, "NOT NULL", , _
             ddlType, relationshipNameShort, outputMode, eacRegular Or eacNationalBool, acmRelIndex, indent, , , gc_dbFalse _
           )
       End If
 ' ### ENDIF IVK ###
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genTransformedLogChangeAttrDeclsWithColReUse( _
   fileNo As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional acmEntityType As AcmAttrContainerType = eactClass, _
   Optional acmEntityIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef className As String = "", _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional indent As Integer = 1, _
   Optional addComma As Boolean = True, _
   Optional useAlternativeDefaults As Boolean = False _
 )
   On Error GoTo ErrorExit

   printSectionHeader "Last Change Log", fileNo, outputMode
 ' ### IF IVK ###
   If outputMode And edomMapHibernate Then
     Print #fileNo, addTab(indent); "<component name=""changeLog"" class=""com.dcx.ivkmds.common.bo.persistent.ChangeLog"">"
     Print #fileNo,
     Print #fileNo, addTab(indent + 1); "<component name=""creator"" class=""com.dcx.ivkmds.common.bo.persistent.Creator"">"
     Print #fileNo,
     indent = indent + 2
   End If

   If useAlternativeDefaults Then
     printConditional fileNo, _
       genTransformedAttrDeclByDomainWithColReUse( _
         conCreateUser, cosnCreateUser, eavtDomain, g_domainIndexUserIdAlt, _
         transformation, tabColumns, acmEntityType, acmEntityIndex, "DEFAULT CURRENT USER", , ddlType, , outputMode, , , indent, , _
         "[ACM] CD Id of user who created the record", , , True _
       )
   Else
     printConditional fileNo, _
       genTransformedAttrDeclByDomainWithColReUse( _
         conCreateUser, cosnCreateUser, eavtDomain, g_domainIndexUserId, _
         transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, , , indent, , _
         "[ACM] CD Id of user who created the record", , , True _
       )
   End If
 ' ### ELSE IVK ###
 ' If useAlternativeDefaults Then
 '   printConditional fileNo, _
 '     genTransformedAttrDeclByDomainWithColReUse( _
 '       conCreateUser, cosnCreateUser, eavtDomain, g_domainIndexUserIdAlt, _
 '       transformation, tabColumns, acmEntityType, acmEntityIndex, "DEFAULT CURRENT USER", , ddlType, , outputMode, , , indent, , _
 '       "[ACM] CD Id of user who created the record", , True _
 '     )
 ' Else
 '   printConditional fileNo, _
 '     genTransformedAttrDeclByDomainWithColReUse( _
 '       conCreateUser, cosnCreateUser, eavtDomain, g_domainIndexUserId, _
 '       transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, , , indent, , _
 '       "[ACM] CD Id of user who created the record", , True _
 '     )
 ' End If
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 
   If ((outputMode And edomListVirtual) <> 0 And (outputMode And edomValueVirtual) = 0 And (outputMode And edomValueVirtualNonPersisted) = 0) Or (outputMode And edomDeclVirtual) Then
     printConditional fileNo, _
       genTransformedAttrDeclByDomainWithColReUse( _
         conCreateUserName, cosnCreateUserName, eavtDomain, g_domainIndexUserName, _
         transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, , , indent, , _
         "[ACM] User Name of user who created the record", , True, True, , False _
       )
   ElseIf (outputMode And edomValueVirtual) <> 0 Or (outputMode And edomValueVirtualNonPersisted) <> 0 Then
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conCreateUserName, cosnCreateUserName, eavtDomain, g_domainIndexUserName, _
           transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , edomValue Or edomDefaultValue, , , indent, , _
           "[ACM] User Name of user who created the record", _
           genGetUserNameByIdDdl(transformation.attributePrefix & g_anCreateUser, ddlType), True, True, , False _
         )
   End If
 ' ### ENDIF IVK ###

   printConditional fileNo, _
     genTransformedAttrDeclByDomainWithColReUse( _
       g_anCreateTimestamp, cosnCreateTimestamp, eavtDomain, g_domainIndexModTimestamp, _
       transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT CURRENT TIMESTAMP", , _
       ddlType, , outputMode, , , indent, , "[ACM] Timestamp when the record was created", "CURRENT TIMESTAMP" _
     )

 ' ### IF IVK ###
   If outputMode And edomMapHibernate Then
     Print #fileNo, addTab(indent - 1); "</component>"
     Print #fileNo,
     Print #fileNo, addTab(indent - 1); "<component name=""lastModifier"" class=""com.dcx.ivkmds.common.bo.persistent.LastModifier"">"
   End If

   If useAlternativeDefaults Then
     printConditional fileNo, _
       genTransformedAttrDeclByDomainWithColReUse( _
         conUpdateUser, cosnUpdateUser, eavtDomain, g_domainIndexUserIdAlt, _
         transformation, tabColumns, acmEntityType, acmEntityIndex, "DEFAULT CURRENT USER", , ddlType, , outputMode, , , indent, , _
         "[ACM] CD Id of user who last modified the record", , , True _
       )
   Else
     printConditional fileNo, _
       genTransformedAttrDeclByDomainWithColReUse( _
         conUpdateUser, cosnUpdateUser, eavtDomain, g_domainIndexUserId, _
         transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, , , indent, , _
         "[ACM] CD Id of user who last modified the record", , , True _
       )
   End If
 ' ### ELSE IVK ###
 ' If useAlternativeDefaults Then
 '   printConditional fileNo, _
 '     genTransformedAttrDeclByDomainWithColReUse( _
 '       conUpdateUser, cosnUpdateUser, eavtDomain, g_domainIndexUserIdAlt, _
 '       transformation, tabColumns, acmEntityType, acmEntityIndex, "DEFAULT CURRENT USER", , ddlType, , outputMode, , , indent, , _
 '       "[ACM] CD Id of user who last modified the record", , True _
 '     )
 ' Else
 '   printConditional fileNo, _
 '     genTransformedAttrDeclByDomainWithColReUse( _
 '       conUpdateUser, cosnUpdateUser, eavtDomain, g_domainIndexUserId, _
 '       transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, , , indent, , _
 '       "[ACM] CD Id of user who last modified the record", , True _
 '     )
 ' End If
 ' ### ENDIF IVK ###
 ' ### IF IVK ###

   If ((outputMode And edomListVirtual) <> 0 And (outputMode And edomValueVirtual) = 0 And (outputMode And edomValueVirtualNonPersisted) = 0) Or (outputMode And edomDeclVirtual) Then
     printConditional fileNo, _
       genTransformedAttrDeclByDomainWithColReUse( _
         conUpdateUserName, cosnUpdateUserName, eavtDomain, g_domainIndexUserName, _
         transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, , , indent, , _
         "[ACM] User Name of user who last modified the record", , True, True, , False _
       )
   ElseIf (outputMode And edomValueVirtual) <> 0 Or (outputMode And edomValueVirtualNonPersisted) <> 0 Then
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conUpdateUserName, cosnUpdateUserName, eavtDomain, g_domainIndexUserName, _
           transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , edomValue Or edomDefaultValue, , , indent, , _
           "[ACM] User Name of user who last modified the record", _
           genGetUserNameByIdDdl(transformation.attributePrefix & g_anUpdateUser, ddlType), True, True, , False _
         )
   End If
 ' ### ENDIF IVK ###

   printConditional fileNo, _
     genTransformedAttrDeclByDomainWithColReUse( _
       conLastUpdateTimestamp, cosnLastUpdateTimestamp, eavtDomain, g_domainIndexModTimestamp, _
       transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT CURRENT TIMESTAMP", _
       addComma, ddlType, , outputMode, , , indent, , _
       "[ACM] Timestamp when the record was last modified", "CURRENT TIMESTAMP" _
     )
 ' ### IF IVK ###

   If outputMode And edomMapHibernate Then
     Print #fileNo, addTab(indent - 1); "</component>"
     Print #fileNo,
     Print #fileNo, addTab(indent - 2); "</component>"
     Print #fileNo,
     indent = indent + 2
   End If
 ' ### ENDIF IVK ###
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 
 Function getFkSrcAttrSeq( _
   thisClassIndex As Integer, _
   ByRef relShortName As String, _
   Optional ddlType As DdlTypeId = edtLdm _
 ) As String
   getFkSrcAttrSeq = ""

   On Error GoTo ErrorExit

     If g_classes.descriptors(thisClassIndex).useSurrogateKey Then
       getFkSrcAttrSeq = genSurrogateKeyName(ddlType, g_classes.descriptors(thisClassIndex).shortName, IIf(reuseColumnsInTabsForOrMapping, relShortName, ""))
     Else
       Dim attrSeq As String
       attrSeq = ""
       Dim i As Integer
       For i = 1 To g_classes.descriptors(thisClassIndex).attrRefs.numDescriptors Step 1
           attrSeq = attrSeq & IIf(attrSeq & "" = "", "", ",") & _
                     genAttrName(g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).attributeName, ddlType, g_classes.descriptors(thisClassIndex).shortName, IIf(reuseColumnsInTabsForOrMapping, relShortName, ""))
       Next i

       getFkSrcAttrSeq = attrSeq
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 
 Function getFkSrcAttrSeqExt( _
   thisClassIndex As Integer, _
   ByRef relShortName As String, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef strKey As String = "", _
   Optional refIsPsTagged As Boolean = True, _
   Optional relUseNumMaps As Boolean = True, _
   Optional relUseMqtToImplementLrt As Boolean = True, _
   Optional dstRefToNl As Boolean = False _
 ) As String
   getFkSrcAttrSeqExt = ""

   On Error GoTo ErrorExit

   Dim poolSupportLrt As Boolean
   Dim noRangePartitioning As Boolean
 
   If thisPoolIndex > 0 Then
     poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
   End If

     If g_classes.descriptors(thisClassIndex).useSurrogateKey Then
       getFkSrcAttrSeqExt = strKey
       noRangePartitioning = g_classes.descriptors(thisClassIndex).noRangePartitioning
       If g_classes.descriptors(thisClassIndex).isPsTagged And refIsPsTagged Then
         If Not noRangePartitioning And g_classes.descriptors(thisClassIndex).isUserTransactional And poolSupportLrt And Not g_classes.descriptors(thisClassIndex).rangePartitioningAll Then
           If g_classes.descriptors(thisClassIndex).useMqtToImplementLrt And relUseNumMaps And relUseMqtToImplementLrt Then
             noRangePartitioning = Not partitionLrtPublicWhenMqt
           Else
             noRangePartitioning = Not partitionLrtPublicWhenNoMqt
           End If
         End If
         If Not noRangePartitioning And Not g_classes.descriptors(thisClassIndex).psTagOptional Then
           getFkSrcAttrSeqExt = getFkSrcAttrSeqExt & ", PS_OID"
           If g_classes.descriptors(thisClassIndex).subClassIdStrSeparatePartition.numMaps > 0 And Not dstRefToNl Then
             getFkSrcAttrSeqExt = getFkSrcAttrSeqExt & ", " & UCase(g_anAhCid)
           End If
         End If
       End If
     Else
       Dim attrSeq As String
       attrSeq = ""
       Dim i As Integer
       For i = 1 To g_classes.descriptors(thisClassIndex).attrRefs.numDescriptors Step 1
           attrSeq = attrSeq & IIf(attrSeq & "" = "", "", ",") & _
                     genAttrName(g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).attributeName, ddlType, g_classes.descriptors(thisClassIndex).shortName, IIf(reuseColumnsInTabsForOrMapping, relShortName, ""))
       Next i

       getFkSrcAttrSeqExt = attrSeq
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 
 Function getFkTargetAttrSeq( _
   thisClassIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 ) As String
   getFkTargetAttrSeq = ""

     If g_classes.descriptors(thisClassIndex).useSurrogateKey Then
       getFkTargetAttrSeq = g_anOid
     Else
       Dim attrSeq As String
       attrSeq = ""
       Dim i As Integer
       For i = 1 To g_classes.descriptors(thisClassIndex).attrRefs.numDescriptors Step 1
           attrSeq = attrSeq & IIf(attrSeq & "" = "", "", ",") & genAttrName(g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).attributeName, ddlType)
       Next i

       getFkTargetAttrSeq = attrSeq
     End If
 End Function
 
 Function getFkTargetAttrSeqExt( _
   thisClassIndex As Integer, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal strKey As String = "OID", _
   Optional ByVal dstAggHeadClassIdStr As String = "", _
   Optional relUseMqtToImplementLrt As Boolean = True, _
   Optional dstRefToNl As Boolean = False _
 ) As String
   getFkTargetAttrSeqExt = ""

   Dim poolSupportLrt As Boolean
   Dim noRangePartitioning As Boolean
 
   If thisPoolIndex > 0 Then
     poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
   End If

     If g_classes.descriptors(thisClassIndex).useSurrogateKey Then
       getFkTargetAttrSeqExt = strKey
       noRangePartitioning = g_classes.descriptors(thisClassIndex).noRangePartitioning
       If g_classes.descriptors(thisClassIndex).isPsTagged Then
         If Not noRangePartitioning And g_classes.descriptors(thisClassIndex).isUserTransactional And poolSupportLrt And Not g_classes.descriptors(thisClassIndex).rangePartitioningAll Then
           If g_classes.descriptors(thisClassIndex).useMqtToImplementLrt And relUseMqtToImplementLrt Then
             noRangePartitioning = Not partitionLrtPublicWhenMqt
           Else
             noRangePartitioning = Not partitionLrtPublicWhenNoMqt
           End If
         End If
 
         If Not noRangePartitioning And Not g_classes.descriptors(thisClassIndex).psTagOptional And g_classes.descriptors(thisClassIndex).subClassIdStrSeparatePartition.numMaps <= 0 Then
           getFkTargetAttrSeqExt = getFkTargetAttrSeqExt & ", PS_OID"
         End If
         If Not noRangePartitioning And Not g_classes.descriptors(thisClassIndex).psTagOptional And g_classes.descriptors(thisClassIndex).subClassIdStrSeparatePartition.numMaps > 0 And dstAggHeadClassIdStr <> "" And dstRefToNl Then
           getFkTargetAttrSeqExt = getFkTargetAttrSeqExt & ", PS_OID"
         End If
         If Not noRangePartitioning And Not g_classes.descriptors(thisClassIndex).psTagOptional And g_classes.descriptors(thisClassIndex).subClassIdStrSeparatePartition.numMaps > 0 And dstAggHeadClassIdStr <> "" And Not dstRefToNl Then
           getFkTargetAttrSeqExt = getFkTargetAttrSeqExt & ", PS_OID" & ", " & UCase(g_anCid)
         End If
       End If
     Else
       Dim attrSeq As String
       attrSeq = ""
       Dim i As Integer
       For i = 1 To g_classes.descriptors(thisClassIndex).attrRefs.numDescriptors Step 1
           attrSeq = attrSeq & IIf(attrSeq & "" = "", "", ",") & genAttrName(g_attributes.descriptors(g_classes.descriptors(thisClassIndex).attrRefs.descriptors(i).refIndex).attributeName, ddlType)
       Next i

       getFkTargetAttrSeqExt = attrSeq
     End If
 End Function
 
 ' ### IF IVK ###
 Private Sub genFKForRelationshipByClassAndName( _
   ByRef qualTabName As String, _
   ByRef classIndex As Integer, _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional tabPartitionType As PartitionType = ptNone _
 )
 ' ### ELSE IVK ###
 'Private Sub genFKForRelationshipByClassAndName( _
 ' ByRef qualTabName As String, _
 ' ByRef classIndex As Integer, _
 ' thisRelIndex As Integer, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' ByRef fileNo As Integer, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional forGen As Boolean = False, _
 ' Optional forLrt As Boolean = False, _
 ' Optional forMqt As Boolean = False _
 ')
 ' ### ENDIF IVK ###
   Dim leftClass As ClassDescriptor, rightclass As ClassDescriptor
   Dim dstClass As ClassDescriptor, srcClass As ClassDescriptor
   Dim srcQualTabName As String, dstQualTabName As String
   Dim srcQualTabNameLdm As String, dstQualTabNameLdm As String
   Dim srcAttrSeq As String, dstAttrSeq As String
 ' ### IF IVK ###
   Dim srcAttrSeqNat As String
 ' ### ENDIF IVK ###
   Dim relSrc2DstShortName As String
   Dim relSrc2DstLdmName As String
   Dim relSrc2DstUseLdmName As Boolean
   Dim dstUseSurrogateKey As Boolean
   Dim srcIsIdentifying As Boolean
   Dim dstIsIdentifying As Boolean
   Dim dstRefToGen As Boolean
   Dim dstRefToNl As Boolean
   Dim switchedDirection As Boolean
   Dim useIndexOnFk As Boolean
   Dim qualIndexName As String
   Dim i As Integer
   Dim relFkMaintenanceMode As FkMaintenanceMode
   Dim extendFK As Boolean

   On Error GoTo ErrorExit

   Dim class As ClassDescriptor
   class = g_classes.descriptors(classIndex)

   Dim suppressRefIntegrity As Boolean
   suppressRefIntegrity = False
   If thisPoolIndex > 0 Then
     suppressRefIntegrity = g_pools.descriptors(thisPoolIndex).suppressRefIntegrity
   End If

   ' check if relationship is implemented as FK in table 'qualTabName'
     If reuseRelationships And g_relationships.descriptors(thisRelIndex).reusedRelIndex > 0 Then
       ' we re-use an existing foreign key to implement this relationship
       Exit Sub
     End If

     If g_relationships.descriptors(thisRelIndex).isNl And supportNlForRelationships Then
       ' any relationship marked as 'NL' definitely requires a relationship table and cannot solely be mapped to a foreign key
       Exit Sub
     End If

     If forGen Then
       ' we do not support 'timevarying relationships'
       Exit Sub
     End If

     ' two cases: FK from 'left to right' or vice versa
     ' switch classses, if relationship is from 'right to left' (normalize direction of relationship)
     If UCase(g_relationships.descriptors(thisRelIndex).leftClassSectionName) = UCase(class.sectionName) And _
        UCase(g_relationships.descriptors(thisRelIndex).leftClassName) = UCase(class.className) And g_relationships.descriptors(thisRelIndex).maxRightCardinality = 1 Then
        ' 'dstClass' is linked via FK
       switchedDirection = False
       srcClass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).leftClassSectionName, g_relationships.descriptors(thisRelIndex).leftClassName)
       dstClass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).rightClassSectionName, g_relationships.descriptors(thisRelIndex).rightClassName)
       relSrc2DstShortName = g_relationships.descriptors(thisRelIndex).lrShortRelName
       relSrc2DstUseLdmName = g_relationships.descriptors(thisRelIndex).useLrLdmRelName
       relSrc2DstLdmName = g_relationships.descriptors(thisRelIndex).lrLdmRelName
       srcIsIdentifying = g_relationships.descriptors(thisRelIndex).isIdentifyingLeft
       dstIsIdentifying = g_relationships.descriptors(thisRelIndex).isIdentifyingRight
       useIndexOnFk = g_relationships.descriptors(thisRelIndex).useIndexOnRightFk
       relFkMaintenanceMode = g_relationships.descriptors(thisRelIndex).lrFkMaintenanceMode

       dstRefToGen = (g_relationships.descriptors(thisRelIndex).rightTargetType And erttGen) <> 0 And dstClass.isGenForming And Not dstClass.hasNoIdentity
       dstRefToNl = (g_relationships.descriptors(thisRelIndex).rightTargetType And erttNL) <> 0 And ((dstRefToGen And dstClass.hasNlAttrsInGenInclSubClasses) Or (Not dstRefToGen And dstClass.hasNlAttrsInNonGenInclSubClasses))
     ElseIf UCase(g_relationships.descriptors(thisRelIndex).rightClassSectionName) = UCase(class.sectionName) And _
            UCase(g_relationships.descriptors(thisRelIndex).rightClassName) = UCase(class.className) And g_relationships.descriptors(thisRelIndex).maxLeftCardinality = 1 Then
        ' 'srcClass' is linked via FK
       switchedDirection = True
       dstClass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).leftClassSectionName, g_relationships.descriptors(thisRelIndex).leftClassName)
       srcClass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).rightClassSectionName, g_relationships.descriptors(thisRelIndex).rightClassName)
       relSrc2DstShortName = g_relationships.descriptors(thisRelIndex).rlShortRelName
       relSrc2DstUseLdmName = g_relationships.descriptors(thisRelIndex).useRlLdmRelName
       relSrc2DstLdmName = g_relationships.descriptors(thisRelIndex).rlLdmRelName
       srcIsIdentifying = g_relationships.descriptors(thisRelIndex).isIdentifyingRight
       dstIsIdentifying = g_relationships.descriptors(thisRelIndex).isIdentifyingLeft
       useIndexOnFk = g_relationships.descriptors(thisRelIndex).useIndexOnRightFk
       relFkMaintenanceMode = g_relationships.descriptors(thisRelIndex).rlFkMaintenanceMode

       dstRefToGen = (g_relationships.descriptors(thisRelIndex).leftTargetType And erttGen) <> 0 And dstClass.isGenForming And Not dstClass.hasNoIdentity
       dstRefToNl = (g_relationships.descriptors(thisRelIndex).leftTargetType And erttNL) <> 0 And ((dstRefToGen And dstClass.hasNlAttrsInGenInclSubClasses) Or (Not dstRefToGen And dstClass.hasNlAttrsInNonGenInclSubClasses))
     Else
       Exit Sub
     End If
     leftClass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).leftClassSectionName, g_relationships.descriptors(thisRelIndex).leftClassName)
     rightclass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).rightClassSectionName, g_relationships.descriptors(thisRelIndex).rightClassName)

     srcQualTabName = genQualTabNameByClassIndex(srcClass.classIndex, ddlType, thisOrgIndex, thisPoolIndex)
     srcQualTabNameLdm = genQualTabNameByClassIndex(srcClass.classIndex, edtLdm, thisOrgIndex, thisPoolIndex)
     dstQualTabName = genQualTabNameByClassIndex(dstClass.classIndex, ddlType, thisOrgIndex, thisPoolIndex, dstRefToGen, , , dstRefToNl)
     dstQualTabNameLdm = genQualTabNameByClassIndex(dstClass.classIndex, edtLdm, thisOrgIndex, thisPoolIndex, dstRefToGen, , , dstRefToNl)
     dstUseSurrogateKey = dstClass.useSurrogateKey

     If dstUseSurrogateKey Then
       dstAttrSeq = g_anOid
       Dim relShortName As String
       relShortName = g_relationships.descriptors(thisRelIndex).effectiveShortName

       If switchedDirection Then
         srcAttrSeq = _
           genSurrogateKeyName( _
             ddlType, IIf(reuseColumnsInTabsForOrMapping, "", getClassShortNameByIndex(g_relationships.descriptors(thisRelIndex).leftEntityIndex)), _
             relShortName & g_relationships.descriptors(thisRelIndex).rlShortRelName _
           )
 ' ### IF IVK ###
         If g_relationships.descriptors(thisRelIndex).isNationalizable Then
           srcAttrSeqNat = _
             genSurrogateKeyName( _
               ddlType, IIf(reuseColumnsInTabsForOrMapping, "", getClassShortNameByIndex(g_relationships.descriptors(thisRelIndex).leftEntityIndex)), _
               relShortName & g_relationships.descriptors(thisRelIndex).rlShortRelName, , , True _
             )
         End If
 ' ### ENDIF IVK ###
       Else
         srcAttrSeq = _
           genSurrogateKeyName( _
             ddlType, IIf(reuseColumnsInTabsForOrMapping, "", getClassShortNameByIndex(g_relationships.descriptors(thisRelIndex).rightEntityIndex)), _
             relShortName & g_relationships.descriptors(thisRelIndex).lrShortRelName _
           )
 ' ### IF IVK ###
         If g_relationships.descriptors(thisRelIndex).isNationalizable Then
           srcAttrSeqNat = _
             genSurrogateKeyName( _
               ddlType, IIf(reuseColumnsInTabsForOrMapping, "", getClassShortNameByIndex(g_relationships.descriptors(thisRelIndex).rightEntityIndex)), _
               relShortName & g_relationships.descriptors(thisRelIndex).lrShortRelName, , , True _
             )
         End If
 ' ### ENDIF IVK ###
       End If
     Else
       srcAttrSeq = _
         getPkAttrListByClass( _
           dstClass.classIndex, ddlType, IIf(relSrc2DstUseLdmName, relSrc2DstLdmName & IIf(relSrc2DstLdmName = "", "", "_"), _
           g_relationships.descriptors(thisRelIndex).shortName & relSrc2DstShortName & "_"), forLrt _
         )
       dstAttrSeq = getPkAttrListByClass(dstClass.classIndex, ddlType)
     End If

     Dim fkEnforced As Boolean
     fkEnforced = Not (g_relationships.descriptors(thisRelIndex).isNotEnforced Or forLrt)
     If (UCase(g_relationships.descriptors(thisRelIndex).leftClassSectionName) = UCase(class.sectionName) And _
         UCase(g_relationships.descriptors(thisRelIndex).leftClassName) = UCase(class.className) And g_relationships.descriptors(thisRelIndex).maxRightCardinality = 1) Then
       ' need to deal with a relationship where 'class' is located at the lhs and the FK points to the right

 ' ### IF IVK ###
       If reusePsTagForRelationships And switchedDirection And dstClass.isPsTagged And srcClass.classIndex = g_classIndexProductStructure Then
         ' we merge this foreign key with the PS-tag
         printComment "reusing PS-tag for relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """(""" & g_relationships.descriptors(thisRelIndex).lrRelName & """) : """ & _
                      g_relationships.descriptors(thisRelIndex).rightClassSectionName & "." & g_relationships.descriptors(thisRelIndex).rightClassName & """ -> """ & _
                      g_relationships.descriptors(thisRelIndex).leftClassSectionName & "." & g_relationships.descriptors(thisRelIndex).leftClassName & """", fileNo
       ElseIf reusePsTagForRelationships And Not switchedDirection And srcClass.isPsTagged And dstClass.classIndex = g_classIndexProductStructure Then
         printComment "reusing PS-tag for relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """(""" & g_relationships.descriptors(thisRelIndex).lrRelName & """) : """ & _
                      g_relationships.descriptors(thisRelIndex).rightClassSectionName & "." & g_relationships.descriptors(thisRelIndex).rightClassName & """ -> """ & _
                      g_relationships.descriptors(thisRelIndex).leftClassSectionName & "." & g_relationships.descriptors(thisRelIndex).leftClassName & """", fileNo
       Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
 
         If srcClass.isCommonToOrgs And ddlType = edtPdm And Not dstClass.isCommonToOrgs And Not suppressRefIntegrity And g_relationships.descriptors(thisRelIndex).fkReferenceOrgId <= 0 Then
           If generateDdlCreateFK Then
             printSectionHeader "Foreign Key corresponding to Relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """", fileNo
             logMsg "unable to implement foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since (source) class """ & _
                    srcClass.sectionName & "." & srcClass.className & """ is common to MPCs and """ & _
                    dstClass.sectionName & "." & dstClass.className & """ is not", _
                    ellWarning, ddlType, thisOrgIndex, thisPoolIndex
             Print #fileNo,
             Print #fileNo, "-- unable to implement foreign key since """; srcQualTabName; """ is common to MPCs and """; dstQualTabName; """ is not"
           End If
         ElseIf srcClass.isCommonToPools And ddlType = edtPdm And (Not (dstClass.isCommonToPools Or dstClass.isCommonToOrgs)) And Not suppressRefIntegrity And g_relationships.descriptors(thisRelIndex).fkReferencePoolId <= 0 Then
           If generateDdlCreateFK Then
             printSectionHeader "Foreign Key corresponding to Relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """", fileNo
             logMsg "unable to implement foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since class """ & _
                    srcClass.sectionName & "." & srcClass.className & """ is common to Pools and """ & _
                    dstClass.sectionName & "." & dstClass.className & """ is not", _
                    ellWarning, ddlType, thisOrgIndex, thisPoolIndex
             Print #fileNo,
             Print #fileNo, "-- unable to implement foreign key since """; srcQualTabName; """ is common to pools and """; dstQualTabName; """ is not"
           End If
         Else
           If Not suppressRefIntegrity And generateDdlCreateFK Then
             printSectionHeader "Foreign Key corresponding to Relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """", fileNo
           End If

           If ddlType = edtPdm And Not srcClass.isUserTransactional And dstClass.isUserTransactional And Not suppressRefIntegrity And Not g_relationships.descriptors(thisRelIndex).isNotEnforced Then
             If generateDdlCreateFK Then
               logMsg "unable to enforce foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since class """ & _
                      srcClass.sectionName & "." & srcClass.className & """ is not transactional and """ & _
                      dstClass.sectionName & "." & dstClass.className & """ is transactional", _
                      ellWarning, ddlType, thisOrgIndex, thisPoolIndex
             End If
             fkEnforced = False
           End If

 ' ### IF IVK ###
           For i = 1 To IIf(g_relationships.descriptors(thisRelIndex).isNationalizable, 2, 1)
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
             If Not suppressRefIntegrity And generateDdlCreateFK Then
 
               extendFK = False
               If Not (g_relationships.descriptors(thisRelIndex).sectionName = snDbMeta Or g_relationships.descriptors(thisRelIndex).sectionName = snDbAdmin) Then
                 If Not (srcClass.classIndex = dstClass.classIndex And srcClass.subClassIdStrSeparatePartition.numMaps > 0 And dstClass.subClassIdStrSeparatePartition.numMaps > 0) Then
                   If Not (dstClass.aggHeadClassIdStr = "09001" And srcClass.aggHeadClassIdStr <> "09001" And dstRefToNl = False) Then
 '                    If fkEnforced Then
                     extendFK = True
 '                    End If
                   End If
                 End If
               End If

               Print #fileNo,
               Print #fileNo, addTab(0); "ALTER TABLE"
               If g_relationships.descriptors(thisRelIndex).isMdsExpressionRel And g_relationships.descriptors(thisRelIndex).isTimeVarying And Not class.hasNoIdentity Then
                 'Special handling for Expression Relations in Gen Class
                 Print #fileNo, addTab(1); qualTabName; "_"; gc_dbObjSuffixGen
               Else
                 Print #fileNo, addTab(1); qualTabName
               End If
               Print #fileNo, addTab(0); "ADD CONSTRAINT"
 ' ### IF IVK ###
               Dim foreignKeyName As String

               foreignKeyName = genFkName(g_relationships.descriptors(thisRelIndex).relName, g_relationships.descriptors(thisRelIndex).shortName, IIf(switchedDirection, g_relationships.descriptors(thisRelIndex).rlShortRelName, g_relationships.descriptors(thisRelIndex).lrShortRelName) & IIf(i = 1, "", gc_asnSuffixNat), _
                                         ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
               Print #fileNo, addTab(1); foreignKeyName
 ' ### ELSE IVK ###
 '             Print #fileNo, addTab(1); genFkName(.relName, .shortName, IIf(switchedDirection, .rlShortRelName, .lrShortRelName), ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
 ' ### ENDIF IVK ###
               Print #fileNo, addTab(0); "FOREIGN KEY"
 ' ### IF IVK ###
               If Not extendFK Then
                 Print #fileNo, addTab(1); "("; IIf(i = 1, srcAttrSeq, srcAttrSeqNat); ")"
               Else
                 Print #fileNo, addTab(1); "("; getFkSrcAttrSeqExt(dstClass.classIndex, "", thisPoolIndex, ddlType, IIf(i = 1, srcAttrSeq, srcAttrSeqNat), , , , dstRefToNl); ")"
               End If
 ' ### ELSE IVK ###
 '             Print #fileNo, addTab(1); "("; srcAttrSeq; ")"
 ' ### ENDIF IVK ###
               Print #fileNo, addTab(0); "REFERENCES"
               If Not extendFK Then
                 Print #fileNo, addTab(1); dstQualTabName; " ("; dstAttrSeq; ")"
               Else
                 Print #fileNo, addTab(1); dstQualTabName; " ("; getFkTargetAttrSeqExt(dstClass.classIndex, thisPoolIndex, ddlType, dstAttrSeq, dstClass.aggHeadClassIdStr, , dstRefToNl); ")"
               End If
               If relFkMaintenanceMode Then
                 Print #fileNo, addTab(0); "ON DELETE CASCADE"
               End If
               If Not fkEnforced Or InStr(foreignKeyName, "3TSTTPA") > 0 Then
                 Print #fileNo, addTab(0); "NOT ENFORCED"
               End If
               Print #fileNo, gc_sqlCmdDelim
             End If

             If (ddlType = edtPdm) And generateIndexOnFk And useIndexOnFk And generateDdlCreateIndex Then
 ' ### IF IVK ###
               qualIndexName = _
                 genQualIndexName( _
                   g_relationships.descriptors(thisRelIndex).sectionIndex, class.shortName & "_" & g_relationships.descriptors(thisRelIndex).relName & IIf(switchedDirection, g_relationships.descriptors(thisRelIndex).rlShortRelName, _
                   g_relationships.descriptors(thisRelIndex).lrShortRelName) & IIf(i = 1, "", gc_asnSuffixNat), _
                   class.shortName & g_relationships.descriptors(thisRelIndex).shortName & IIf(switchedDirection, g_relationships.descriptors(thisRelIndex).rlShortRelName, g_relationships.descriptors(thisRelIndex).lrShortRelName) & IIf(i = 1, "", gc_asnSuffixNat), _
                   ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, , forMqt _
                 )
 ' ### ELSE IVK ###
 '             qualIndexName = _
 '               genQualIndexName( _
 '                 .sectionIndex, class.shortName & "_" & .relName & IIf(switchedDirection, .rlShortRelName, .lrShortRelName), _
 '                 class.shortName & .shortName & IIf(switchedDirection, .rlShortRelName, .lrShortRelName), ddlType, thisOrgIndex, _
 '                 thisPoolIndex, forGen, forLrt, , forMqt _
 '               )
 ' ### ENDIF IVK ###
               If indexExcp(qualIndexName, thisOrgIndex) = False Then
                 Print #fileNo,
                 Print #fileNo, addTab(0); "CREATE INDEX"
                 Print #fileNo, addTab(1); qualIndexName
                 Print #fileNo, addTab(0); "ON"
                 Print #fileNo, addTab(1); qualTabName
                 Print #fileNo, addTab(0); "("
 ' ### IF IVK ###
                 Print #fileNo, addTab(1); IIf(i = 1, srcAttrSeq, srcAttrSeqNat)
 ' ### ELSE IVK ###
 '               Print #fileNo, addTab(1); srcAttrSeq
 ' ### ENDIF IVK ###
                 Print #fileNo, addTab(0); ")"
                 Print #fileNo, gc_sqlCmdDelim
               End If ' indexExcp
             End If
 ' ### IF IVK ###
           Next i
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###

           registerQualLdmFk srcQualTabNameLdm, dstQualTabNameLdm, srcClass.classIndex, eactClass, , , fkEnforced
         End If
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
     End If

     fkEnforced = Not (g_relationships.descriptors(thisRelIndex).isNotEnforced Or forLrt)
     If (UCase(g_relationships.descriptors(thisRelIndex).rightClassSectionName) = UCase(class.sectionName) And _
         UCase(g_relationships.descriptors(thisRelIndex).rightClassName) = UCase(class.className) And g_relationships.descriptors(thisRelIndex).maxLeftCardinality = 1 And g_relationships.descriptors(thisRelIndex).maxRightCardinality <> 1) Then
       ' need to deal with a relationship where 'class' is located at the rhs and the FK points to the left
 ' ### IF IVK ###
       If reusePsTagForRelationships And Not switchedDirection And dstClass.isPsTagged And srcClass.classIndex = g_classIndexProductStructure And Not suppressRefIntegrity Then
         ' we merge this foreign key with the PS-tag
         printComment "reusing PS-tag for relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """(""" & g_relationships.descriptors(thisRelIndex).lrRelName & """) : """ & _
                      g_relationships.descriptors(thisRelIndex).leftClassSectionName & "." & g_relationships.descriptors(thisRelIndex).leftClassName & """ -> """ & _
                      g_relationships.descriptors(thisRelIndex).rightClassSectionName & "." & g_relationships.descriptors(thisRelIndex).rightClassName & """", fileNo
       ElseIf reusePsTagForRelationships And switchedDirection And srcClass.isPsTagged And dstClass.classIndex = g_classIndexProductStructure And Not suppressRefIntegrity Then
         printComment "reusing PS-tag for relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """(""" & g_relationships.descriptors(thisRelIndex).lrRelName & """) : """ & _
                      g_relationships.descriptors(thisRelIndex).rightClassSectionName & "." & g_relationships.descriptors(thisRelIndex).rightClassName & """ -> """ & _
                      g_relationships.descriptors(thisRelIndex).leftClassSectionName & "." & g_relationships.descriptors(thisRelIndex).leftClassName & """", fileNo
       Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         If srcClass.isCommonToOrgs And ddlType = edtPdm And Not dstClass.isCommonToOrgs And Not suppressRefIntegrity And g_relationships.descriptors(thisRelIndex).fkReferenceOrgId <= 0 Then
           If generateDdlCreateFK Then
             printSectionHeader "Foreign Key corresponding to Relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """", fileNo
             logMsg "unable to implement foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since (source) class """ & _
                    srcClass.sectionName & "." & srcClass.className & """ is common to MPCs and """ & _
                    dstClass.sectionName & "." & dstClass.className & """ is not", _
                    ellWarning, ddlType, thisOrgIndex, thisPoolIndex
            Print #fileNo,
            Print #fileNo, "-- unable to implement foreign key since table """; srcQualTabName; """ is common to MPCs and """; dstQualTabName; """ is not"
           End If
         ElseIf srcClass.isCommonToPools And ddlType = edtPdm And (Not (dstClass.isCommonToPools Or dstClass.isCommonToOrgs)) And Not suppressRefIntegrity And g_relationships.descriptors(thisRelIndex).fkReferencePoolId <= 0 Then
           If generateDdlCreateFK Then
             printSectionHeader "Foreign Key corresponding to Relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """", fileNo
             logMsg "unable to implement foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since (source) class """ & _
                    srcClass.sectionName & "." & srcClass.className & """ is common to Pools and """ & _
                    dstClass.sectionName & "." & dstClass.className & """ is not", _
                    ellWarning, ddlType, thisOrgIndex, thisPoolIndex
             Print #fileNo,
             Print #fileNo, "-- unable to implement foreign key since """; srcQualTabName; """ is common to pools and """; dstQualTabName; """ is not"
           End If
         Else
           If Not suppressRefIntegrity And generateDdlCreateFK Then
             printSectionHeader "Foreign Key corresponding to Relationship """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """", fileNo
           End If

           If ddlType = edtPdm And Not srcClass.isUserTransactional And dstClass.isUserTransactional And Not suppressRefIntegrity And Not g_relationships.descriptors(thisRelIndex).isNotEnforced Then
             If generateDdlCreateFK Then
               logMsg "unable to enforce foreign key for """ & g_relationships.descriptors(thisRelIndex).sectionName & "." & g_relationships.descriptors(thisRelIndex).relName & """ since class """ & _
                      srcClass.sectionName & "." & srcClass.className & """ is not transactional and """ & _
                      dstClass.sectionName & "." & dstClass.className & """ is transactional", _
                      ellWarning, ddlType, thisOrgIndex, thisPoolIndex
             End If
             fkEnforced = False
           End If

 ' ### IF IVK ###
           For i = 1 To IIf(g_relationships.descriptors(thisRelIndex).isNationalizable, 2, 1)
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
             If Not suppressRefIntegrity And generateDdlCreateFK Then

               extendFK = False
               If Not (g_relationships.descriptors(thisRelIndex).sectionName = snDbMeta Or g_relationships.descriptors(thisRelIndex).sectionName = snDbAdmin) Then
                 If Not (srcClass.classIndex = dstClass.classIndex And srcClass.subClassIdStrSeparatePartition.numMaps > 0 And dstClass.subClassIdStrSeparatePartition.numMaps > 0) Then
 '                  If fkEnforced Then
                     extendFK = True
 '                  End If
                 End If
               End If
 
               Print #fileNo,
               Print #fileNo, addTab(0); "ALTER TABLE"
               Print #fileNo, addTab(1); qualTabName
               Print #fileNo, addTab(0); "ADD CONSTRAINT"
 ' ### IF IVK ###
               Print #fileNo, addTab(1); genFkName(g_relationships.descriptors(thisRelIndex).relName, g_relationships.descriptors(thisRelIndex).shortName, IIf(switchedDirection, g_relationships.descriptors(thisRelIndex).rlShortRelName, g_relationships.descriptors(thisRelIndex).lrShortRelName) & IIf(i = 1, "", gc_asnSuffixNat), _
                                        ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
 ' ### ELSE IVK ###
 '             Print #fileNo, addTab(1); genFkName(.relName, .shortName, IIf(switchedDirection, .rlShortRelName, .lrShortRelName), ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
 ' ### ENDIF IVK ###
               Print #fileNo, addTab(0); "FOREIGN KEY"
 ' ### IF IVK ###
               If Not extendFK Then
                 Print #fileNo, addTab(1); "("; IIf(i = 1, srcAttrSeq, srcAttrSeqNat); ")"
               Else
                 Print #fileNo, addTab(1); "("; getFkSrcAttrSeqExt(dstClass.classIndex, "", thisPoolIndex, ddlType, IIf(i = 1, srcAttrSeq, srcAttrSeqNat)); ")"
               End If
 ' ### ELSE IVK ###
 '             Print #fileNo, addTab(1); "(" ; srcAttrSeq ; ")"
 ' ### ENDIF IVK ###
               Print #fileNo, addTab(0); "REFERENCES"
               If Not extendFK Then
                 Print #fileNo, addTab(1); dstQualTabName; " ("; dstAttrSeq; ")"
               Else
                 Print #fileNo, addTab(1); dstQualTabName; " ("; getFkTargetAttrSeqExt(dstClass.classIndex, thisPoolIndex, ddlType, dstAttrSeq, dstClass.aggHeadClassIdStr); ")"
               End If
               If relFkMaintenanceMode = efkmCascade Then
                 Print #fileNo, addTab(0); "ON DELETE CASCADE"
               End If
               If Not fkEnforced Then
                 Print #fileNo, addTab(0); "NOT ENFORCED"
               End If
               Print #fileNo, gc_sqlCmdDelim
             End If

             If (ddlType = edtPdm) And generateIndexOnFk And useIndexOnFk And generateDdlCreateIndex Then
 ' ### IF IVK ###
               qualIndexName = _
                 genQualIndexName( _
                   g_relationships.descriptors(thisRelIndex).sectionIndex, class.shortName & "_" & g_relationships.descriptors(thisRelIndex).relName & IIf(switchedDirection, g_relationships.descriptors(thisRelIndex).rlShortRelName, g_relationships.descriptors(thisRelIndex).lrShortRelName) & IIf(i = 1, "", gc_asnSuffixNat), _
                   class.shortName & g_relationships.descriptors(thisRelIndex).shortName & IIf(switchedDirection, g_relationships.descriptors(thisRelIndex).rlShortRelName, g_relationships.descriptors(thisRelIndex).lrShortRelName) & IIf(i = 1, "", gc_asnSuffixNat), _
                   ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, , forMqt _
                 )
 ' ### ELSE IVK ###
 '             qualIndexName = _
 '               genQualIndexName( _
 '                 .sectionIndex, class.shortName & "_" & .relName & IIf(switchedDirection, .rlShortRelName, .lrShortRelName), _
 '                 class.shortName & .shortName & IIf(switchedDirection, .rlShortRelName, .lrShortRelName), ddlType, thisOrgIndex, _
 '                 thisPoolIndex, forGen, forLrt, , forMqt _
 '               )
 ' ### ENDIF IVK ###

               If indexExcp(qualIndexName, thisOrgIndex) = False Then
                 Print #fileNo,
                 Print #fileNo, addTab(0); "CREATE INDEX"
                 Print #fileNo, addTab(1); qualIndexName
                 Print #fileNo, addTab(0); "ON"
                 Print #fileNo, addTab(1); qualTabName
                 Print #fileNo, addTab(0); "("
 ' ### IF IVK ###
                 Print #fileNo, addTab(1); IIf(i = 1, srcAttrSeq, srcAttrSeqNat)
 ' ### ELSE IVK ###
 '             Print #fileNo, addTab(1); srcAttrSeq
 ' ### ENDIF IVK ###
                 Print #fileNo, addTab(0); ")"
                 Print #fileNo, gc_sqlCmdDelim
               End If ' indexExcp
             End If
 ' ### IF IVK ###
           Next i
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###

           registerQualLdmFk srcQualTabNameLdm, dstQualTabNameLdm, srcClass.classIndex, eactClass, , , fkEnforced
         End If
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Private Sub genFKCheckInfoForRelationshipByClassAndName( _
     ByRef qualTabName As String, _
     ByRef classIndex As Integer, _
     ByVal thisOrgIndex As Integer, _
     ByVal thisPoolIndex As Integer, _
     ByRef fileNo As Integer, _
     Optional ddlType As DdlTypeId = edtLdm, _
     Optional forGen As Boolean = False, _
     Optional forLrt As Boolean = False, _
     Optional forMqt As Boolean = False _
     )

     Dim leftClass As ClassDescriptor, rightclass As ClassDescriptor
     Dim dstClass As ClassDescriptor, srcClass As ClassDescriptor
     Dim srcQualTabName As String, dstQualTabName As String
     Dim srcAttrSeq As String, dstAttrSeq As String

     Dim qualIndexName As String
     Dim i As Integer

     On Error GoTo ErrorExit

     Dim class As ClassDescriptor
     class = g_classes.descriptors(classIndex)

     Dim suppressRefIntegrity As Boolean
     suppressRefIntegrity = False

     If thisPoolIndex > 0 Then
        suppressRefIntegrity = g_pools.descriptors(thisPoolIndex).suppressRefIntegrity
     End If

     If suppressRefIntegrity Then
         Exit Sub
     End If

     If forGen Or forLrt Or forMqt Then
         Exit Sub
     End If

     Dim thisRelIndex As Integer

         For i = 1 To class.relRefs.numRefs
             If class.orMappingSuperClassIndex = g_classIndexGenericAspect Then

                 thisRelIndex = class.relRefs.refs(i).refIndex


                     If g_relationships.descriptors(thisRelIndex).isNl And supportNlForRelationships Then
                         ' any relationship marked as 'NL' definitely requires a relationship table and cannot solely be mapped to a foreign key
                         GoTo NextI
                     End If

                     If (g_relationships.descriptors(thisRelIndex).leftClassSectionName <> "Aspect") Or (g_relationships.descriptors(thisRelIndex).rightClassSectionName <> "Code") Then
                         GoTo NextI
                     End If

                     ' 'dstClass' is linked via FK
                     srcClass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).leftClassSectionName, g_relationships.descriptors(thisRelIndex).leftClassName)
                     dstClass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).rightClassSectionName, g_relationships.descriptors(thisRelIndex).rightClassName)

                     leftClass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).leftClassSectionName, g_relationships.descriptors(thisRelIndex).leftClassName)
                     rightclass = getOrMappingSuperClass(g_relationships.descriptors(thisRelIndex).rightClassSectionName, g_relationships.descriptors(thisRelIndex).rightClassName)

                     srcQualTabName = genQualTabNameByClassIndex(srcClass.classIndex, ddlType, thisOrgIndex, thisPoolIndex)
                     dstQualTabName = genQualTabNameByClassIndex(dstClass.classIndex, ddlType, thisOrgIndex, thisPoolIndex, False, , , False)

                     dstAttrSeq = g_anOid
                     Dim relShortName As String
                     relShortName = g_relationships.descriptors(thisRelIndex).effectiveShortName

                     srcAttrSeq = _
                         genSurrogateKeyName( _
                         ddlType, IIf(reuseColumnsInTabsForOrMapping, "", getClassShortNameByIndex(g_relationships.descriptors(thisRelIndex).rightEntityIndex)), _
                         relShortName & g_relationships.descriptors(thisRelIndex).lrShortRelName _
                         )

                     Dim fkEnforced As Boolean
                     fkEnforced = Not (g_relationships.descriptors(thisRelIndex).isNotEnforced Or forLrt)
                     If (UCase(g_relationships.descriptors(thisRelIndex).leftClassSectionName) = UCase(class.sectionName) And _
                         UCase(g_relationships.descriptors(thisRelIndex).leftClassName) = UCase(class.className) And g_relationships.descriptors(thisRelIndex).maxRightCardinality = 1) Then

                         registerCheckFk srcQualTabName, dstQualTabName, srcAttrSeq, fkEnforced
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
 
 
 Sub genFKCheckSPForRelationshipByClassAndName( _
   ByRef qualTabName As String, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
 
   On Error GoTo ErrorExit

   Dim suppressRefIntegrity As Boolean
   suppressRefIntegrity = False


   If thisPoolIndex > 0 Then
     suppressRefIntegrity = g_pools.descriptors(thisPoolIndex).suppressRefIntegrity
   End If

   If suppressRefIntegrity Then
     Exit Sub
   End If

   If g_checkFks.numFks < 1 Then
      Exit Sub
   End If
 

   printSectionHeader "SP for checking foreign keys not enforced", fileNo
   Dim qualProcedureNameCheckFk As String
 
   qualProcedureNameCheckFk = _
     genQualProcName(g_sectionIndexAspect, spnFkCheckAspectCode, ddlType, thisOrgIndex, thisPoolIndex)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCheckFk
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure"
   genProcParm fileNo, "IN", "timestamp_in", "TIMESTAMP", True, "marks the execution timestamp of the LRT "
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of invalid code references"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo

   Print #fileNo,
   Print #fileNo, addTab(1); "IF timestamp_in IS NULL THEN"

   Print #fileNo, addTab(2); "SET rowCount_out = ("
   Print #fileNo, addTab(3); "SELECT COUNT(OID) FROM ("

   Dim srcQualTabName As String, dstQualTabName As String
   srcQualTabName = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)
   dstQualTabName = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)

   Dim stmtForFk As String
   stmtForFk = ""

   Dim i As Integer
     For i = 1 To g_checkFks.numFks
         If g_checkFks.fks(i).srcQualTableName = srcQualTabName And g_checkFks.fks(i).dstQualTableName = dstQualTabName And Not g_checkFks.fks(i).isEnforced Then
             If stmtForFk <> "" Then
                 Print #fileNo, addTab(3); "UNION ALL "
             End If

             stmtForFk = "SELECT DISTINCT " & g_checkFks.fks(i).srcAttrSeq & " AS OID FROM " & qualTabName & " WHERE PS_OID = psOid_in "
             Print #fileNo, addTab(4); stmtForFk
         End If
     Next i


   Print #fileNo, addTab(3); ") REF"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "NOT EXISTS (SELECT OID FROM "; dstQualTabName; " GC WHERE GC.OID = REF.OID)"
   Print #fileNo, addTab(1); ");"

   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "SET rowCount_out = ("
   Print #fileNo, addTab(3); "SELECT COUNT(OID) FROM ("

   stmtForFk = ""
     For i = 1 To g_checkFks.numFks
         If g_checkFks.fks(i).srcQualTableName = srcQualTabName And g_checkFks.fks(i).dstQualTableName = dstQualTabName And Not g_checkFks.fks(i).isEnforced Then
             If stmtForFk <> "" Then
                 Print #fileNo, addTab(3); "UNION ALL "
             End If

             stmtForFk = "SELECT DISTINCT " & g_checkFks.fks(i).srcAttrSeq & " AS OID FROM " & qualTabName & " WHERE PS_OID = psOid_in AND LASTUPDATETIMESTAMP = timestamp_in"
             Print #fileNo, addTab(4); stmtForFk
         End If
     Next i

   Print #fileNo, addTab(3); ") REF"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "NOT EXISTS (SELECT OID FROM "; dstQualTabName; " GC WHERE GC.OID = REF.OID)"
   Print #fileNo, addTab(1); ");"

   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,

   Print #fileNo,
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   'create index for this SP on lastupdatetimestamp of GenericAspect: only in Work and Productive, not necessary in LRT-Tables
   Dim schemaName As String
   Dim tabName As String
   splitQualifiedName srcQualTabName, schemaName, tabName

   Dim qualIndexName As String
   qualIndexName = schemaName & ".IDX_GAS_LASTUPDTS"

   printSectionHeader "Index on LASTUPDATETIMESTAMP for SP " & qualProcedureNameCheckFk, fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE INDEX"
   Print #fileNo, addTab(1); qualIndexName
   Print #fileNo, addTab(0); "ON"
   Print #fileNo, addTab(1); qualTabName; " (LASTUPDATETIMESTAMP ASC)"
   Print #fileNo, gc_sqlCmdDelim
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 ' ### IF IVK ###
 Sub genFKsForRelationshipsByClass( _
   ByRef qualTabName As String, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional tabPartitionType As PartitionType = ptNone _
 )
 ' ### ELSE IVK ###
 'Sub genFKsForRelationshipsByClass( _
 ' ByRef qualTabName As String, _
 ' ByRef classIndex As Integer, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' ByRef fileNo As Integer, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional forGen As Boolean = False, _
 ' Optional forLrt As Boolean = False, _
 ' Optional forMqt As Boolean = False _
 ')
 ' ### ENDIF IVK ###
   ' qualTabName: fully qualified name of table to generate FKs for
   ' class: Class to analyse for relationships implemented as FK
   '        if this class appears on the left hand side of a relationship with a 'maxRightCardinality' of '1'

   On Error GoTo ErrorExit

   Dim i As Integer
     For i = 1 To g_classes.descriptors(classIndex).relRefs.numRefs
 ' ### IF IVK ###
       genFKForRelationshipByClassAndName qualTabName, g_classes.descriptors(classIndex).classIndex, g_classes.descriptors(classIndex).relRefs.refs(i).refIndex, _
         thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt, tabPartitionType
 ' ### ELSE IVK ###
 '     genFKForRelationshipByClassAndName qualTabName, .classIndex, .relRefs.refs(i).refIndex, _
 '       thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt
 ' ### ENDIF IVK ###
     Next i
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Sub genFKsForRelationshipsByClassRecursive( _
   ByRef qualTabName As String, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional tabPartitionType As PartitionType = ptNone _
 )
 ' ### ELSE IVK ###
 'Sub genFKsForRelationshipsByClassRecursive( _
 ' ByRef qualTabName As String, _
 ' ByRef classIndex As Integer, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' ByRef fileNo As Integer, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional forGen As Boolean = False, _
 ' Optional forLrt As Boolean = False, _
 ' Optional forMqt As Boolean = False _
 ')
 ' ### ENDIF IVK ###
   ' qualTabName: fully qualified name of table to generate FKs for
   ' class: Class to analyse for relationships implemented as FK; this is done recursively over inheritance relationship
 
   On Error GoTo ErrorExit

    If g_classes.descriptors(classIndex).noFks Then
       Exit Sub
     End If
 
 ' ### IF IVK ###
     genFKsForRelationshipsByClass qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt, tabPartitionType
 ' ### ELSE IVK ###
 '   genFKsForRelationshipsByClass qualTabName, classIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt
 ' ### ENDIF IVK ###

     If g_classes.descriptors(classIndex).orMappingSuperClassIndex = g_classIndexGenericAspect Then
         genFKCheckInfoForRelationshipByClassAndName qualTabName, g_classes.descriptors(classIndex).classIndex, _
         thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt
     End If


     Dim i As Integer
     For i = 1 To UBound(g_classes.descriptors(classIndex).subclassIndexes) Step 1
 ' ### IF IVK ###
       genFKsForRelationshipsByClassRecursive qualTabName, g_classes.descriptors(classIndex).subclassIndexes(i), thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt, tabPartitionType
 ' ### ELSE IVK ###
 '     genFKsForRelationshipsByClassRecursive qualTabName, .subclassIndexes(i), thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forMqt
 ' ### ENDIF IVK ###
     Next i
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Sub genFKsForGenParent( _
   ByRef qualTabNameGen As String, _
   ByRef qualTabNameGenLdm As String, _
   ByRef qualTabName As String, _
   ByRef qualTabNameLdm As String, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional tabPartitionType As PartitionType = ptNone _
 )
 ' ### ELSE IVK ###
 'Sub genFKsForGenParent( _
 ' ByRef qualTabNameGen As String, _
 ' ByRef qualTabNameGenLdm As String, _
 ' ByRef qualTabName As String, _
 ' ByRef qualTabNameLdm As String, _
 ' ByRef classIndex As Integer, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' ByRef fileNo As Integer, _
 ' Optional ddlType As DdlTypeId = edtLdm _
 ')
 ' ### ENDIF IVK ###
   Dim pkAttrList As String
   Dim refPkAttrList As String

     If g_classes.descriptors(classIndex).noFks Then
       Exit Sub
     End If

     If g_classes.descriptors(classIndex).useSurrogateKey Then
       pkAttrList = g_classes.descriptors(classIndex).shortName & "_" & g_anOid
       refPkAttrList = g_anOid
     Else
       pkAttrList = getPkAttrListByClass(g_classes.descriptors(classIndex).classIndex)
       refPkAttrList = pkAttrList
     End If
 
     If generateDdlCreateFK Then
       printSectionHeader "Foreign Key to ""Parent"" of ""GEN-Table"" """ & qualTabNameGen & """", fileNo
       Print #fileNo,
       Print #fileNo, addTab(0); "ALTER TABLE"
       Print #fileNo, addTab(1); qualTabNameGen
       Print #fileNo, addTab(0); "ADD CONSTRAINT"

       Print #fileNo, addTab(1); genFkName(g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, "PAR", ddlType, thisOrgIndex, thisPoolIndex)

       Print #fileNo, addTab(0); "FOREIGN KEY"
       Print #fileNo, addTab(1); "("; getFkSrcAttrSeqExt(g_classes.descriptors(classIndex).classIndex, "", thisPoolIndex, ddlType, pkAttrList); ")"
       Print #fileNo, addTab(0); "REFERENCES"
       Print #fileNo, addTab(1); qualTabName; " ("; getFkTargetAttrSeqExt(g_classes.descriptors(classIndex).classIndex, thisPoolIndex, ddlType, refPkAttrList); ")"
       Print #fileNo, gc_sqlCmdDelim
     End If

     registerQualLdmFk qualTabNameGenLdm, qualTabNameLdm, g_classes.descriptors(classIndex).classIndex, eactClass, , True

     If (ddlType = edtPdm) And generateIndexOnFk And generateDdlCreateIndex Then
       Dim qualIndexName As String
       qualIndexName = genQualIndexName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className & "GPA", g_classes.descriptors(classIndex).shortName & "GPA", ddlType, thisOrgIndex, thisPoolIndex)

       If indexExcp(qualIndexName, thisOrgIndex) = False Then
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE INDEX"
         Print #fileNo, addTab(1); qualIndexName
         Print #fileNo, addTab(0); "ON"
         Print #fileNo, addTab(1); qualTabNameGen
         Print #fileNo, addTab(0); "("
         Print #fileNo, addTab(1); UCase(pkAttrList)
         Print #fileNo, addTab(0); ")"
         Print #fileNo, gc_sqlCmdDelim
       End If ' indexExcp
     End If
 End Sub
 
 
 ' ### IF IVK ###
 Sub genPKForClass( _
   ByRef qualTabName As String, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional includeValidFrom As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional noConstraints As Boolean = False, _
   Optional tabPartitionType As PartitionType = ptNone _
 )
 ' ### ELSE IVK ###
 'Sub genPKForClass( _
 ' ByRef qualTabName As String, _
 ' ByRef classIndex As Integer, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' fileNo As Integer, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional includeValidFrom As Boolean = False, _
 ' Optional forLrt As Boolean = False, _
 ' Optional forMqt As Boolean = False, _
 ' Optional noConstraints As Boolean = False _
 ')
 ' ### ENDIF IVK ###
   Dim pkName As String
   Dim ukName As String
   Dim qualIndexName As String
   Dim ukAttrList As String
   Dim pkAttrList As String
   Dim attrListIncludedTech As String
   Dim attrListIncluded As String

   On Error GoTo ErrorExit

   Dim poolCommonItemsLocal As Boolean
   Dim poolIsArchive As Boolean
   Dim poolSupportLrt As Boolean
   Dim noRangePartitioning As Boolean

   If thisPoolIndex > 0 Then
       poolCommonItemsLocal = g_pools.descriptors(thisPoolIndex).commonItemsLocal
       poolIsArchive = g_pools.descriptors(thisPoolIndex).isArchive
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
   End If

   attrListIncluded = ""
   attrListIncludedTech = ""

     pkName = genPkName(g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt)
     ukName = "UK_" & Mid(pkName, 4)
     qualIndexName = genUkName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt)

     ukAttrList = getPkAttrListByClass(g_classes.descriptors(classIndex).classIndex, ddlType, , forLrt)
     attrListIncluded = getPkAttrListByClass(g_classes.descriptors(classIndex).classIndex, ddlType, , forLrt, True)

     attrListIncludedTech = ""

     If g_classes.descriptors(classIndex).useSurrogateKey Then
       noRangePartitioning = g_classes.descriptors(classIndex).noRangePartitioning
       If g_classes.descriptors(classIndex).isPsTagged Then
         If Not noRangePartitioning And g_classes.descriptors(classIndex).isUserTransactional And poolSupportLrt And Not g_classes.descriptors(classIndex).rangePartitioningAll Then
           If g_classes.descriptors(classIndex).useMqtToImplementLrt Then
             noRangePartitioning = Not partitionLrtPublicWhenMqt
           Else
             noRangePartitioning = Not partitionLrtPublicWhenNoMqt
           End If
         End If
       End If
     End If
 
 ' ### IF IVK ###
     ' todo: we currently do not support this - could not prove to help (check this again)
     If (Not (thisPoolIndex > 0 And (poolCommonItemsLocal Or poolIsArchive))) And _
        Not g_classes.descriptors(classIndex).condenseData And _
        (g_classes.descriptors(classIndex).isAggHead Or g_classes.descriptors(classIndex).isCommonToPools) And _
        (forMqt Or Not forLrt) Then
       If Not g_classes.descriptors(classIndex).isPsTagged And g_classes.descriptors(classIndex).navPathToDiv.relRefIndex > 0 And Not g_classes.descriptors(classIndex).isPsTagged Then
           If poolSupportLrt And g_relationships.descriptors(g_classes.descriptors(classIndex).navPathToDiv.relRefIndex).isUserTransactional Then
             attrListIncludedTech = IIf(g_classes.descriptors(classIndex).navPathToDiv.navDirection = etLeft, g_relationships.descriptors(g_classes.descriptors(classIndex).navPathToDiv.relRefIndex).leftFkColName(ddlType), g_relationships.descriptors(g_classes.descriptors(classIndex).navPathToDiv.relRefIndex).rightFkColName(ddlType)) & "," & g_anInLrt
           End If
       End If
     End If
 
 ' ### ENDIF IVK ###
     If g_classes.descriptors(classIndex).useSurrogateKey Then
       pkAttrList = g_anOid
     Else
       pkAttrList = ukAttrList
     End If
 
     If includeValidFrom Then
       pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ",") & g_anValidFrom
       ukAttrList = ukAttrList & IIf(ukAttrList = "", "", ",") & g_anValidFrom
     End If

 ' ### IF IVK ###
     If g_genLrtSupport And g_classes.descriptors(classIndex).isUserTransactional And (Not forLrt Or forMqt) And Not g_classes.descriptors(classIndex).condenseData Then
       If ukAttrList <> "" Then
         ukAttrList = ukAttrList & "," & g_anIsDeleted
       End If
     End If

 ' ### ENDIF IVK ###
     If g_genLrtSupport And g_classes.descriptors(classIndex).isUserTransactional And forLrt Then
       Dim extraAttrs As String
       extraAttrs = ""
       If forMqt Then
         extraAttrs = extraAttrs & IIf(extraAttrs = "", "", ",") & g_anIsLrtPrivate
       End If
       extraAttrs = _
         extraAttrs & _
         IIf(extraAttrs = "", "", ",") & g_anInLrt & _
         "," & g_anLrtState
 
       If ukAttrList <> "" Then
         ukAttrList = ukAttrList & "," & extraAttrs
       End If
       If pkAttrList <> "" Then
         pkAttrList = pkAttrList & "," & extraAttrs
       End If
     End If
     If pkAttrList <> "" Then
       If noConstraints Then
         If thisPoolIndex <> g_archiveDataPoolIndex Then
           If generateDdlCreateIndex Then
             printSectionHeader "Primary Key", fileNo
             If indexExcp(genQualIndexName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, , forMqt, "PKA"), thisOrgIndex) = False Then
               Print #fileNo,
               Print #fileNo, addTab(0); "CREATE INDEX"
               Print #fileNo, addTab(1); genQualIndexName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, , forMqt, "PKA")
               Print #fileNo, addTab(0); "ON"
               Print #fileNo, addTab(1); qualTabName; "("; UCase(pkAttrList); _
                                        IIf(attrListIncluded <> "", "," & attrListIncluded, ""); _
                                         IIf(attrListIncludedTech <> "", "," & attrListIncludedTech, ""); _
                                         ")"
               Print #fileNo, gc_sqlCmdDelim
             End If ' indexExcp
           End If
         Else
           If generateDdlCreatePK Then
             ' ArchivePool does not have constraints in general but must have a primary key anyway
             printSectionHeader "Primary Key", fileNo
             If indexExcp(genQualIndexName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, , forMqt, "PKA"), thisOrgIndex) = False Then
               Print #fileNo,
               Print #fileNo, addTab(0); "ALTER TABLE"
               Print #fileNo, addTab(1); qualTabName
               Print #fileNo, addTab(0); "ADD CONSTRAINT"
               Print #fileNo, addTab(1); pkName
               Print #fileNo, addTab(0); "PRIMARY KEY("; UCase(pkAttrList); ")"
               Print #fileNo, gc_sqlCmdDelim

             End If ' indexExcp
           End If
         End If 'archiveDataPool
       Else
         If generateDdlCreatePK Then
           printSectionHeader "Primary Key", fileNo
           Print #fileNo,
           Print #fileNo, addTab(0); "ALTER TABLE"
           Print #fileNo, addTab(1); qualTabName
           Print #fileNo, addTab(0); "ADD CONSTRAINT"
           Print #fileNo, addTab(1); pkName
           Print #fileNo, addTab(0); "PRIMARY KEY("; UCase(pkAttrList); ")"
           Print #fileNo, gc_sqlCmdDelim

           If g_classes.descriptors(classIndex).isPsTagged And Not noRangePartitioning And Not g_classes.descriptors(classIndex).psTagOptional And Not forLrt And thisPoolIndex <> 1 Then
 
             Dim additionalUK As Boolean
             Dim i As Integer
             For i = 1 To g_relationships.numDescriptors Step 1
               If g_relationships.descriptors(i).leftClassSectionName = g_classes.descriptors(classIndex).sectionName And g_relationships.descriptors(i).leftClassName = g_classes.descriptors(classIndex).className And _
                  g_relationships.descriptors(i).maxRightCardinality = -1 And _
                  (g_relationships.descriptors(i).isCommonToPools = False Or _
                  (g_relationships.descriptors(i).isCommonToPools = True And g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isCommonToPools = True)) And _
                  g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged = True Then
                 additionalUK = True
                 Exit For
               End If
               If g_relationships.descriptors(i).rightClassSectionName = g_classes.descriptors(classIndex).sectionName And g_relationships.descriptors(i).rightClassName = g_classes.descriptors(classIndex).className And _
                  g_relationships.descriptors(i).maxLeftCardinality = -1 And _
                  (g_relationships.descriptors(i).isCommonToPools = False Or _
                  (g_relationships.descriptors(i).isCommonToPools = True And g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isCommonToPools = True)) And _
                  g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged = True Then
                 additionalUK = True
                 Exit For
               End If
               If g_relationships.descriptors(i).leftClassSectionName = g_classes.descriptors(classIndex).sectionName And g_relationships.descriptors(i).leftClassName = g_classes.descriptors(classIndex).className And _
                  g_relationships.descriptors(i).minLeftCardinality = 1 And _
                  g_relationships.descriptors(i).maxLeftCardinality = 1 And _
                  g_relationships.descriptors(i).minRightCardinality = 1 And _
                  g_relationships.descriptors(i).maxRightCardinality = 1 And _
                  (g_relationships.descriptors(i).isCommonToPools = False Or _
                  (g_relationships.descriptors(i).isCommonToPools = True And g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isCommonToPools = True)) And _
                  g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged = True Then
                 additionalUK = True
                 Exit For
               End If
               If g_relationships.descriptors(i).rightClassSectionName = g_classes.descriptors(classIndex).sectionName And g_relationships.descriptors(i).rightClassName = g_classes.descriptors(classIndex).className And _
                  g_relationships.descriptors(i).minLeftCardinality = 1 And _
                  g_relationships.descriptors(i).maxLeftCardinality = 1 And _
                  g_relationships.descriptors(i).minRightCardinality = 1 And _
                  g_relationships.descriptors(i).maxRightCardinality = 1 And _
                  (g_relationships.descriptors(i).isCommonToPools = False Or _
                  (g_relationships.descriptors(i).isCommonToPools = True And g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isCommonToPools = True)) And _
                  g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged = True Then
                 additionalUK = True
                 Exit For
               End If
               If g_classes.descriptors(classIndex).sectionName = "Lrt" And g_classes.descriptors(classIndex).className = "LRT" Then
                 additionalUK = True
                 Exit For
               End If
             Next i
 
             If additionalUK Then
               printSectionHeader "Unique Constraint for """ & qualTabName & """", fileNo
 
               Print #fileNo,
               Print #fileNo, addTab(0); "CREATE UNIQUE INDEX"
               Print #fileNo, addTab(1); genQualUkName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, ukName, ddlType, thisOrgIndex, thisPoolIndex)
               Print #fileNo, addTab(0); "ON"
               Print #fileNo, addTab(1); qualTabName; "("; UCase(pkAttrList); ", PS_OID"; IIf(g_classes.descriptors(classIndex).subClassIdStrSeparatePartition.numMaps > 0, ", " & UCase(g_anCid), ""); ")"
               Print #fileNo, gc_sqlCmdDelim
 
               Print #fileNo, addTab(0); "ALTER TABLE"
               Print #fileNo, addTab(1); qualTabName
               Print #fileNo, addTab(0); "ADD CONSTRAINT "
               Print #fileNo, addTab(1); ukName
               Print #fileNo, addTab(1); "UNIQUE ("; UCase(pkAttrList); ", PS_OID"; IIf(g_classes.descriptors(classIndex).subClassIdStrSeparatePartition.numMaps > 0, ", " & UCase(g_anCid), ""); ")"
               Print #fileNo, gc_sqlCmdDelim
             End If
 
           End If
 
           If (attrListIncludedTech <> "" And InStr(1, pkAttrList, attrListIncludedTech) = 0) Or _
             (attrListIncluded <> "" And InStr(1, pkAttrList, attrListIncluded) = 0) Then
             If indexExcp(genQualPkName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className & "I", g_classes.descriptors(classIndex).shortName & "I", ddlType, thisOrgIndex, thisPoolIndex, False, forLrt), thisOrgIndex) = False Then
               Print #fileNo,
               Print #fileNo, addTab(0); "CREATE UNIQUE INDEX"
               Print #fileNo, addTab(1); genQualPkName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className & "I", g_classes.descriptors(classIndex).shortName & "I", ddlType, thisOrgIndex, thisPoolIndex, False, forLrt)
               Print #fileNo, addTab(0); "ON"
               Print #fileNo, addTab(1); qualTabName; "("; UCase(pkAttrList); ")"
               Print #fileNo, addTab(0); "INCLUDE"
               Print #fileNo, addTab(1); "("; attrListIncluded; _
                                         IIf(attrListIncluded = "" Or attrListIncludedTech = "" Or InStr(1, pkAttrList, attrListIncludedTech) > 0, "", ","); _
                                         IIf(attrListIncludedTech = "" Or InStr(1, pkAttrList, attrListIncludedTech) > 0, "", UCase(attrListIncludedTech)); _
                                         ")"
               Print #fileNo, gc_sqlCmdDelim
             End If ' indexExcp
           End If
         End If
       End If
     End If
 
     If g_classes.descriptors(classIndex).useSurrogateKey And ukAttrList <> "" And Not includeValidFrom And generateDdlCreateIndex Then
         printSectionHeader IIf(forLrt Or forMqt Or noConstraints, "", "Unique ") & "Index on Business Key Attributes", fileNo
       If indexExcp(qualIndexName, thisOrgIndex) = False Then
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE "; IIf(forLrt Or forMqt Or noConstraints, "", "UNIQUE "); "INDEX"
         Print #fileNo, addTab(1); qualIndexName
         Print #fileNo, addTab(0); "ON"
         Print #fileNo, addTab(1); qualTabName; " ("; UCase(ukAttrList); IIf(forLrt Or forMqt Or noConstraints, IIf(attrListIncluded = "", "", "," & attrListIncluded), ""); ")"
         If attrListIncluded <> "" And Not (forLrt Or forMqt Or noConstraints) Then
           Print #fileNo, addTab(0); "INCLUDE"
           Print #fileNo, addTab(1); "("; UCase(attrListIncluded); ")"
         End If
         Print #fileNo, gc_sqlCmdDelim
       End If ' indexExcp
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genPKForGenClass( _
   ByVal qualTabName As String, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional noConstraints As Boolean = False _
 )
   Dim pkName As String
   Dim ukName As String
   Dim qualIndexName As String
 
   Dim ukAttrList As String, ukAttrListBus As String
   Dim pkAttrList As String, pkAttrListBus As String

   On Error GoTo ErrorExit

     pkName = genPkName(g_classes.descriptors(classIndex).shortName, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, True, forLrt)
     ukName = "UK_" & Mid(pkName, 4)
     qualIndexName = genUkName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, True, forLrt)

     ukAttrListBus = getPkAttrListByClass(g_classes.descriptors(classIndex).classIndex, ddlType, , forLrt)

     If g_classes.descriptors(classIndex).useSurrogateKey Then
       pkAttrListBus = g_classes.descriptors(classIndex).shortName & "_" & g_anOid
       pkAttrList = g_anOid
     Else
       pkAttrListBus = ukAttrListBus
       pkAttrList = pkAttrListBus
     End If

     pkAttrListBus = pkAttrListBus & IIf(pkAttrListBus = "", "", ",") & g_anValidFrom
     ukAttrListBus = ukAttrListBus & IIf(ukAttrListBus = "", "", ",") & g_anValidFrom

     If g_genLrtSupport And g_classes.descriptors(classIndex).isUserTransactional And forLrt Then
       Dim extraAttrs As String
       extraAttrs = ""
       If forMqt Then
         extraAttrs = extraAttrs & IIf(extraAttrs = "", "", ",") & g_anIsLrtPrivate
       End If
       extraAttrs = extraAttrs & _
                    IIf(extraAttrs = "", "", ",") & g_anInLrt & _
                    "," & g_anLrtState

       If ukAttrList <> "" Then
         ukAttrList = ukAttrList & "," & extraAttrs
       End If
       If pkAttrList <> "" Then
         pkAttrList = pkAttrList & "," & extraAttrs
       End If
     End If

 ' ### IF IVK ###
     If g_genLrtSupport And g_classes.descriptors(classIndex).isUserTransactional And (Not forLrt Or forMqt) Then
       If pkAttrListBus <> "" Then
         pkAttrListBus = pkAttrListBus & "," & g_anIsDeleted
       End If
       If ukAttrListBus <> "" Then
         ukAttrListBus = ukAttrListBus & "," & g_anIsDeleted
       End If
     End If

 ' ### ENDIF IVK ###
     If pkAttrList <> "" Then
       If generateDdlCreatePK Then
         printSectionHeader "Primary Key", fileNo
         Print #fileNo, addTab(0); "ALTER TABLE"
         Print #fileNo, addTab(1); qualTabName
         Print #fileNo, addTab(0); "ADD CONSTRAINT"
         Print #fileNo, addTab(1); pkName
         Print #fileNo, addTab(0); "PRIMARY KEY("; UCase(pkAttrList); ")"
         Print #fileNo, gc_sqlCmdDelim

         If thisPoolIndex = 3 And Not forLrt Then
 '        If g_pools.descriptors(thisPoolIndex).id = 3 And Not forLrt Then
 
           Dim additionalUK As Boolean
           Dim i As Integer
           For i = 1 To g_relationships.numDescriptors Step 1
             If g_relationships.descriptors(i).leftClassSectionName = g_classes.descriptors(classIndex).sectionName And g_relationships.descriptors(i).leftClassName = g_classes.descriptors(classIndex).className And _
                g_relationships.descriptors(i).maxRightCardinality = -1 And _
                g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged = True Then
               additionalUK = True
               Exit For
             End If
             If g_relationships.descriptors(i).rightClassSectionName = g_classes.descriptors(classIndex).sectionName And g_relationships.descriptors(i).rightClassName = g_classes.descriptors(classIndex).className And _
                g_relationships.descriptors(i).maxLeftCardinality = -1 And _
                g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged = True Then
               additionalUK = True
               Exit For
             End If
             If g_relationships.descriptors(i).leftClassSectionName = g_classes.descriptors(classIndex).sectionName And g_relationships.descriptors(i).leftClassName = g_classes.descriptors(classIndex).className And _
                g_relationships.descriptors(i).minLeftCardinality = 1 And _
                g_relationships.descriptors(i).maxLeftCardinality = 1 And _
                g_relationships.descriptors(i).minRightCardinality = 1 And _
                g_relationships.descriptors(i).maxRightCardinality = 1 And _
                g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged = True Then
               additionalUK = True
               Exit For
             End If
             If g_relationships.descriptors(i).rightClassSectionName = g_classes.descriptors(classIndex).sectionName And g_relationships.descriptors(i).rightClassName = g_classes.descriptors(classIndex).className And _
                g_relationships.descriptors(i).minLeftCardinality = 1 And _
                g_relationships.descriptors(i).maxLeftCardinality = 1 And _
                g_relationships.descriptors(i).minRightCardinality = 1 And _
                g_relationships.descriptors(i).maxRightCardinality = 1 And _
                g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged = True Then
               additionalUK = True
               Exit For
             End If
           Next i
 
           If additionalUK Then
             If indexExcp(genQualUkName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, ukName, ddlType, thisOrgIndex, thisPoolIndex), thisOrgIndex) = False Then
               printSectionHeader "Unique Constraint for """ & qualTabName & """", fileNo

               Print #fileNo,
               Print #fileNo, addTab(0); "CREATE UNIQUE INDEX"
               Print #fileNo, addTab(1); genQualUkName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, ukName, ddlType, thisOrgIndex, thisPoolIndex)
               Print #fileNo, addTab(0); "ON"
               Print #fileNo, addTab(1); qualTabName; "("; UCase(pkAttrList); ", PS_OID"; ")"
               Print #fileNo, gc_sqlCmdDelim

               Print #fileNo, addTab(0); "ALTER TABLE"
               Print #fileNo, addTab(1); qualTabName
               Print #fileNo, addTab(0); "ADD CONSTRAINT"
               Print #fileNo, addTab(1); ukName
               Print #fileNo, addTab(1); "UNIQUE (" & UCase(pkAttrList) & ", PS_OID)"
               Print #fileNo, gc_sqlCmdDelim
             End If
           End If

         End If
 
       End If

       If generateDdlCreateIndex Then
         printSectionHeader IIf(forLrt Or noConstraints, "", "Unique ") & "Index on """ & pkAttrListBus & """", fileNo
         If indexExcp(qualIndexName, thisOrgIndex) = False Then
           Print #fileNo, addTab(0); "CREATE "; IIf(forLrt Or noConstraints, "", "UNIQUE "); "INDEX"
           Print #fileNo, addTab(1); qualIndexName
           Print #fileNo, addTab(0); "ON"
           Print #fileNo, addTab(1); qualTabName; " ("; UCase(pkAttrListBus); ")"
           Print #fileNo, gc_sqlCmdDelim
         End If ' indexExcp
       End If
     End If

     If g_classes.descriptors(classIndex).useSurrogateKey And pkAttrListBus <> "" And Not g_classes.descriptors(classIndex).useSurrogateKey And generateDdlCreateIndex Then
       printSectionHeader IIf(forMqt Or noConstraints, "", "Unique ") & "Index on Business Key Attributes", fileNo
       If indexExcp(qualIndexName, thisOrgIndex) = False Then
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE "; IIf(forMqt Or noConstraints, "", "UNIQUE "); "INDEX"
         Print #fileNo, addTab(1); qualIndexName; "B"
         Print #fileNo, addTab(0); "ON"
         Print #fileNo, addTab(1); qualTabName; " ("; UCase(ukAttrListBus); ")"
         Print #fileNo, gc_sqlCmdDelim
       End If ' indexExcp
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Function genOidSequenceNameForClass( _
   thisClassIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 ) As String
   genOidSequenceNameForClass = genOidSequenceNameForClassIndex(thisClassIndex, thisOrgIndex, thisPoolIndex, ddlType)
 End Function
 
 
 Function genOidSequenceNameForClassIndex( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 ) As String
   Dim sectionName As String
   Dim name As String

   genOidSequenceNameForClassIndex = ""

     If Not g_classes.descriptors(classIndex).useSurrogateKey Then
       Exit Function
     End If
     genOidSequenceNameForClassIndex = genQualObjName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex)
 End Function
 
 
 Sub genNlsAttrDeclsForEntity( _
   acmEntityIndex As Integer, _
   acmEntityType As AcmAttrContainerType, _
   fileNo As Integer, _
   Optional ByRef qualTabName As String = "", _
   Optional onlyThisAttribute As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional includeMetaAttrs As Boolean = True, _
   Optional ByRef parentTabPkAttrList As String = "", _
   Optional ByRef parentTabPkAttrDecl As String = "", _
   Optional ByRef pkAttrList As String = "", _
   Optional ByRef tabAttrList As String = "", _
   Optional useAlternativeDefaults As Boolean = False _
 )
   Dim transformation As AttributeListTransformation
   transformation = nullAttributeTransformation
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   On Error GoTo ErrorExit

   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, _
     onlyThisAttribute, False, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, forLrt, includeMetaAttrs, outputMode, qualTabName, parentTabPkAttrList, parentTabPkAttrDecl, _
     pkAttrList, tabAttrList, useAlternativeDefaults
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genNlsTransformedAttrListForEntity( _
   acmEntityIndex As Integer, _
   acmEntityType As AcmAttrContainerType, _
   ByRef transformation As AttributeListTransformation, _
   fileNo As Integer, _
   Optional onlyThisAttribute As Integer = -1, _
   Optional forceNotNull As Boolean = False, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional includeMetaAttrs As Boolean = True, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional ByRef qualTabName As String = "", _
   Optional ByRef parentTabPkAttrList As String = "", _
   Optional ByRef parentTabPkAttrDecl As String = "", _
   Optional ByRef pkAttrList As String = "", _
   Optional ByRef tabAttrList As String = "", _
   Optional useAlternativeDefaults As Boolean = False _
 )
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
 
   On Error GoTo ErrorExit

   genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, _
     onlyThisAttribute, False, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, forLrt, includeMetaAttrs, outputMode, qualTabName, parentTabPkAttrList, parentTabPkAttrDecl, _
     pkAttrList, tabAttrList, useAlternativeDefaults
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genNlsTransformedAttrListForEntityWithColReUse( _
   acmEntityIndex As Integer, _
   acmEntityType As AcmAttrContainerType, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   fileNo As Integer, _
   Optional onlyThisAttribute As Integer = -1, _
   Optional forceNotNull As Boolean = False, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional includeMetaAttrs As Boolean = True, _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional ByRef qualTabName As String = "", _
   Optional ByRef parentTabPkAttrList As String = "", _
   Optional ByRef parentTabPkAttrDecl As String = "", _
   Optional ByRef pkAttrList As String = "", _
   Optional ByRef tabAttrList As String = "", _
   Optional useAlternativeDefaults As Boolean = False _
 )
   Dim attrRefs As AttrDescriptorRefs
   Dim nlAttrRefs As AttrDescriptorRefs
   Dim attrRefsLeft As AttrDescriptorRefs
   Dim attrRefsRight As AttrDescriptorRefs
   Dim relRefs As RelationshipDescriptorRefs
   Dim className As String
   Dim classShortName As String
   Dim useVersiontag As Boolean
   Dim useSurrogateKey As Boolean
   Dim isUserTransactional As Boolean
   Dim isAggregateMember As Boolean
   Dim isAggregateHead As Boolean
   Dim numNlAttrs As Integer
   Dim defaultStatus As Integer
 ' ### IF IVK ###
   Dim hasNoIdentity As Boolean
   Dim enforceChangeComment As Boolean
   Dim noRangePartitioning As Boolean
   Dim isPsForming As Boolean
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
   Dim supportPsCopy As Boolean
   Dim ahSupportPsCopy As Boolean
   Dim condenseData As Boolean
   condenseData = False
 ' ### ENDIF IVK ###

    On Error GoTo ErrorExit

   ' todo: we should use a parameter to pass this info
   Dim forLrtMqt As Boolean
   forLrtMqt = forLrt And ((outputMode And edomMqtLrt) = edomMqtLrt)

   initAttrDescriptorRefs attrRefsLeft
   initAttrDescriptorRefs attrRefsRight

   Dim poolSupportLrt As Boolean

   If thisPoolIndex > 0 Then
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
   End If

   If acmEntityType = eactClass Then
       attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
       nlAttrRefs = g_classes.descriptors(acmEntityIndex).nlAttrRefs
       relRefs = g_classes.descriptors(acmEntityIndex).relRefs
       className = g_classes.descriptors(acmEntityIndex).className
       classShortName = g_classes.descriptors(acmEntityIndex).shortName
       useVersiontag = g_classes.descriptors(acmEntityIndex).useVersiontag
       useSurrogateKey = g_classes.descriptors(acmEntityIndex).useSurrogateKey
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       isAggregateMember = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0)
       isAggregateHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).classIndex)
       defaultStatus = g_classes.descriptors(acmEntityIndex).defaultStatus
       If onlyThisAttribute > 0 Then
         numNlAttrs = 1
       Else
         numNlAttrs = g_classes.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors

         Dim i As Integer
         For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexes) Step 1
           numNlAttrs = numNlAttrs + g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexes(i)).nlAttrRefs.numDescriptors
         Next i
       End If
 ' ### IF IVK ###
       isPsForming = g_classes.descriptors(acmEntityIndex).isPsForming
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = g_classes.descriptors(acmEntityIndex).psTagOptional
       supportPsCopy = g_classes.descriptors(acmEntityIndex).supportExtendedPsCopy
       If g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0 Then
         ahSupportPsCopy = g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).supportExtendedPsCopy
       End If
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       noRangePartitioning = g_classes.descriptors(acmEntityIndex).noRangePartitioning
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).isGenForming And g_classes.descriptors(acmEntityIndex).hasNoIdentity
       enforceChangeComment = g_classes.descriptors(acmEntityIndex).enforceLrtChangeComment
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
       nlAttrRefs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
       If g_relationships.descriptors(acmEntityIndex).leftEntityIndex > -1 Then
         attrRefsLeft = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).attrRefs
       End If
       If g_relationships.descriptors(acmEntityIndex).rightEntityIndex > -1 Then
         attrRefsRight = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).attrRefs
       End If
       initRelDescriptorRefs relRefs
       className = g_relationships.descriptors(acmEntityIndex).relName
       classShortName = g_relationships.descriptors(acmEntityIndex).shortName
       useVersiontag = g_relationships.descriptors(acmEntityIndex).useVersiontag
       useSurrogateKey = useSurrogateKeysForNMRelationships And (g_relationships.descriptors(acmEntityIndex).attrRefs.numDescriptors > 0 Or g_relationships.descriptors(acmEntityIndex).logLastChange)
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       isAggregateMember = (g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex > 0)
       isAggregateHead = False
       defaultStatus = g_relationships.descriptors(acmEntityIndex).defaultStatus
 ' ### IF IVK ###
       isPsForming = g_relationships.descriptors(acmEntityIndex).isPsForming
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = False
       supportPsCopy = g_relationships.descriptors(acmEntityIndex).supportExtendedPsCopy
       If g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex > 0 Then
         ahSupportPsCopy = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).supportExtendedPsCopy
       End If
       noRangePartitioning = g_relationships.descriptors(acmEntityIndex).noRangePartitioning
       hasNoIdentity = True
       enforceChangeComment = False
 ' ### ENDIF IVK ###
       If onlyThisAttribute > 0 Then
         numNlAttrs = 1
       Else
         numNlAttrs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors

         For i = 1 To g_relationships.descriptors(acmEntityIndex).reusingRelIndexes.numIndexes Step 1
           numNlAttrs = numNlAttrs + g_relationships.descriptors(g_relationships.descriptors(acmEntityIndex).reusingRelIndexes.indexes(i)).nlAttrRefs.numDescriptors
         Next i
       End If
   ElseIf acmEntityType = eactEnum Then
       attrRefs = g_enums.descriptors(acmEntityIndex).attrRefs
       className = g_enums.descriptors(acmEntityIndex).enumName
       classShortName = g_enums.descriptors(acmEntityIndex).shortName
       useVersiontag = True
       useSurrogateKey = True
       isUserTransactional = False
       isAggregateMember = False
       isAggregateHead = False
       numNlAttrs = 1
 ' ### IF IVK ###
       isPsForming = False
       isPsTagged = False
       psTagOptional = False
       noRangePartitioning = True
       hasNoIdentity = False
 ' ### ENDIF IVK ###
   End If

   If includeMetaAttrs Then
     If useSurrogateKey Then
       printSectionHeader "Surrogate Key", fileNo, outputMode
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conOid, cosnOid, eavtDomain, g_domainIndexOid, _
           transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, eacOid, , indent _
         )
     End If

     If g_genLrtSupport And isUserTransactional Then
       If outputMode And edomMqtLrt Then
         printSectionHeader "Flag '" & conIsLrtPrivate & "'", fileNo, outputMode
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conIsLrtPrivate, cosnIsLrtPrivate, eavtDomain, g_domainIndexIsLrtPrivate, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, eacMqtLrtMeta, , indent, , "[LRT-MQT] identifies 'LRT-private' records", gc_dbFalse _
           )
         printSectionHeader "Column '" & conInUseBy & "'", fileNo, outputMode
 ' ### IF IVK ###
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, eacMqtLrtMeta, , indent, , _
             "[LRT-MQT] identifies the user holding the lock on the record", , , True _
           )
 ' ### ELSE IVK ###
 '        printConditional fileNo, _
 '          genTransformedAttrDeclByDomainWithColReUse( _
 '            conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, transformation, _
 '            tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, eacMqtLrtMeta, , indent, , _
 '            "[LRT-MQT] identifies the user holding the lock on the record", , True _
 '          )
 ' ### ENDIF IVK ###
       End If
 
 ' ### IF IVK ###
       If (forLrt Or Not condenseData) Then
 ' ### ELSE IVK ###
 '     If forLrt Then
 ' ### ENDIF IVK ###
         printSectionHeader "LRT - Id", fileNo, outputMode
 ' ### IF IVK ###
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, _
             transformation, tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt And Not forLrtMqt, "NOT NULL", ""), , _
             ddlType, , outputMode And IIf(((outputMode And edomValue) <> 0) And ((outputMode And edomLrtPriv) <> 0), Not edomList, Not 0), _
             eacLrtMeta, , indent, , , , , Not forLrt Or forLrtMqt _
           )
 ' ### ELSE IVK ###
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( _
 '           conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, _
 '           transformation, tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt And Not forLrtMqt, "NOT NULL", ""), , _
 '           ddlType, , outputMode And IIf(((outputMode And edomValue) <> 0) And ((outputMode And edomLrtPriv) <> 0), Not edomList, Not 0), _
 '           eacLrtMeta, , indent, , , , Not forLrt Or forLrtMqt _
 '         )
 ' ### ENDIF IVK ###
       End If

 ' ### IF IVK ###
       If (isAggregateHead Or enforceChangeComment) And Not forGen Then
 ' ### ELSE IVK ###
 '     If isAggregateHead And Not forGen Then
 ' ### ENDIF IVK ###
         If Not forLrt And (outputMode And edomValueLrt) Then
           printSectionHeader "Change Comment", fileNo, edomValueLrt
 ' ### IF IVK ###
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conChangeComment, cosnChangeComment, eavtDomain, g_domainIndexChangeComment, _
               transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , edomValueLrt, eacLrtMeta, , indent, , , , , True _
             )
 ' ### ELSE IVK ###
 '         printConditional fileNo, _
 '           genTransformedAttrDeclByDomainWithColReUse( _
 '             conChangeComment, cosnChangeComment, eavtDomain, g_domainIndexChangeComment, _
 '             transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , edomValueLrt, eacLrtMeta, , indent, , , , True _
 '           )
 ' ### ENDIF IVK ###
         ElseIf forLrt Or (outputMode And (edomListLrt Or edomDeclLrt)) Then
           printSectionHeader "Change Comment", fileNo, outputMode
 ' ### IF IVK ###
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conChangeComment, cosnChangeComment, eavtDomain, g_domainIndexChangeComment, _
               transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, eacLrtMeta, , indent, , , , , True _
             )
 ' ### ELSE IVK ###
 '         printConditional fileNo, _
 '           genTransformedAttrDeclByDomainWithColReUse( _
 '             conChangeComment, cosnChangeComment, eavtDomain, g_domainIndexChangeComment, _
 '             transformation, tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, eacLrtMeta, , indent, , , , True _
 '           )
 ' ### ENDIF IVK ###
         End If
       End If

 ' ### IF IVK ###
       printSectionHeader "Flag 'status'", fileNo, outputMode
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           enStatus, esnStatus, eavtEnum, g_enumIndexStatus, transformation, _
           tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt, "", "NOT NULL DEFAULT " & IIf(useAlternativeDefaults, statusProductive, statusWorkInProgress)), , _
           ddlType, , outputMode, eacLrtMeta Or eacSetProdMeta, , indent, , _
           "Specifies the state of the record with respect to 'release to production", CStr(defaultStatus), , Not forLrt _
         )
 ' ### ENDIF IVK ###
     End If

     If (isAggregateMember Or (g_genLrtSupport And isUserTransactional)) Then
         printSectionHeader "ClassId of 'aggregate head'", fileNo, outputMode
 ' ### IF IVK ###
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conAhClassId, cosnAggHeadClassId, eavtDomain, g_domainIndexCid, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), , ddlType, , outputMode, eacCid Or eacLrtMeta, , indent, , _
             "ID of the ACM-class of the 'Aggregate Head'", , , True _
           )
 ' ### ELSE IVK ###
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( _
 '           conAhClassId, cosnAggHeadClassId, eavtDomain, g_domainIndexCid, transformation, _
 '           tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), , ddlType, , outputMode, eacCid Or eacLrtMeta, , indent, , _
 '           "ID of the ACM-class of the 'Aggregate Head'", , True _
 '         )
 ' ### ENDIF IVK ###

         printSectionHeader "ObjectId of 'aggregate head'", fileNo, outputMode
 ' ### IF IVK ###
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conAhOId, cosnAggHeadOId, eavtDomain, g_domainIndexOid, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), , ddlType, , outputMode, _
             eacFkOid Or eacLrtMeta Or eacAhOid Or IIf(isPsForming, eacPsFormingOid, 0) Or IIf(ahSupportPsCopy, eacFkExtPsCopyOid, 0), , indent, , _
             "Object ID of the 'Aggregate Head'", , , True _
           )
 ' ### ELSE IVK ###
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( _
 '           conAhOId, cosnAggHeadOId, eavtDomain, g_domainIndexOid, transformation, _
 '           tabColumns, acmEntityType, acmEntityIndex, IIf(generateAhIdsNotNull And Not useAlternativeDefaults, "NOT NULL", ""), , ddlType, , outputMode, _
 '           eacFkOid Or eacLrtMeta Or eacAhOid, , indent, , _
 '           "Object ID of the 'Aggregate Head'", , True _
 '         )
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
       If hasBeenSetProductiveInPrivLrt Then
         If isUserTransactional And g_genLrtSupport Then
           printSectionHeader "Flag 'hasBeenSetProductive'", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conHasBeenSetProductive, cosnHasBeenSetProductive, eavtDomain, g_domainIndexBoolean, transformation, _
               tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT " & IIf(useAlternativeDefaults, 1, 0), , _
               ddlType, , outputMode, eacLrtMeta, , indent, , _
               "[LRT] Specifies whether record has been set productive", gc_dbFalse _
             )
         End If
       End If
 ' ### ENDIF IVK ###
     End If

     If g_genLrtSupport And isUserTransactional Then
       ' columns which exist in public and not in private tables
       If forLrt And (outputMode And edomValueNonLrt) Then
 ' ### IF IVK ###
         If Not hasBeenSetProductiveInPrivLrt Then
           printSectionHeader "Flag 'hasBeenSetProductive'", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conHasBeenSetProductive, cosnHasBeenSetProductive, eavtDomain, g_domainIndexBoolean, transformation, _
               tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , edomValueLrt, eacLrtMeta, , indent, , , gc_dbFalse, , True _
             )
         End If
         If Not condenseData Then
           printSectionHeader "Flag 'isDeleted'", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse(conIsDeleted, conIsDeleted, eavtDomain, g_domainIndexBoolean, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , edomValueLrt, eacLrtMeta, , indent, , , gc_dbFalse, , True _
           )
         End If
 ' ### ENDIF IVK ###
       ElseIf Not forLrt Or (outputMode And (edomListNonLrt Or edomDeclNonLrt)) Then
 ' ### IF IVK ###
         If Not hasBeenSetProductiveInPrivLrt Then
           printSectionHeader "Flag 'hasBeenSetProductive'", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conHasBeenSetProductive, cosnHasBeenSetProductive, eavtDomain, g_domainIndexBoolean, transformation, _
               tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt, "", "NOT NULL DEFAULT " & IIf(useAlternativeDefaults, 1, 0)), , ddlType, , outputMode, eacLrtMeta, , indent, , _
               "[LRT] Specifies whether record has been set productive", gc_dbFalse _
             )
         End If
         If Not condenseData Then
           printSectionHeader "Flag 'isDeleted'", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               conIsDeleted, cosnIsDeleted, eavtDomain, g_domainIndexBoolean, transformation, _
               tabColumns, acmEntityType, acmEntityIndex, IIf(forLrt, "", "NOT NULL DEFAULT 0"), , ddlType, , outputMode, eacLrtMeta, , indent, , _
               "[LRT] Specifies whether record logically has been deleted", gc_dbFalse _
             )
         End If
 ' ### ENDIF IVK ###
       End If
 
       ' columns which exist in private and not in public tables
       If Not forLrt And (outputMode And edomValueLrt) Then
         printSectionHeader "LRT - Status (locked[" & lrtStatusLocked & "], created[" & lrtStatusCreated & "], updated[" & lrtStatusUpdated & "], deleted[" & lrtStatusDeleted & "])", fileNo, edomValueLrt
 ' ### IF IVK ###
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
             edomValueLrt, eacLrtMeta, , indent, , , , , forLrtMqt _
           )
 ' ### ELSE IVK ###
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( _
 '           conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, transformation, _
 '           tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
 '           edomValueLrt, eacLrtMeta, , indent, , , , forLrtMqt _
 '         )
 ' ### ENDIF IVK ###
       ElseIf forLrt Or (outputMode And (edomListLrt Or edomDeclLrt)) Then
         printSectionHeader "LRT - Status (locked[" & lrtStatusLocked & "], created[" & lrtStatusCreated & "], updated[" & lrtStatusUpdated & "], deleted[" & lrtStatusDeleted & "])", fileNo, outputMode
 ' ### IF IVK ###
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, transformation, _
             tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
             outputMode, eacLrtMeta, , indent, , , , , forLrtMqt _
           )
 ' ### ELSE IVK ###
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( '
 '           conLrtState, cosnLrtState, eavtDomain, g_domainIndexLrtStatus, transformation, _
 '           tabColumns, acmEntityType, acmEntityIndex, IIf(Not forLrtMqt, "NOT NULL", ""), , ddlType, , _
 '           outputMode, eacLrtMeta, , indent, , , , forLrtMqt _
 '         )
 ' ### ENDIF IVK ###
       End If
     End If

     ' determine primary key attribute(s) of this table
     If qualTabName <> "" Then
       printSectionHeader "Foreign Key to 'Parent Table' (" & qualTabName & ")", fileNo, outputMode
     End If
 
     If Not useSurrogateKey And parentTabPkAttrDecl <> "" Then
       Print #fileNo, parentTabPkAttrDecl
     End If

     tabAttrList = parentTabPkAttrList
     pkAttrList = parentTabPkAttrList
     If useSurrogateKey And acmEntityType <> eactEnum Then
       tabAttrList = genSurrogateKeyName(ddlType, classShortName)
       pkAttrList = g_anOid
       If reuseColumnsInTabsForOrMapping Then
 ' ### IF IVK ###
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             genSurrogateKeyName(ddlType, classShortName), genSurrogateKeyShortName(ddlType, classShortName), _
             eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
             eacFkOid Or IIf(isPsForming, eacPsFormingOid, 0) Or IIf(supportPsCopy, eacFkExtPsCopyOid, 0) Or eacFkOidParent, , indent _
           )
 ' ### ELSE IVK ###
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( _
 '           genSurrogateKeyName(ddlType, classShortName), genSurrogateKeyShortName(ddlType, classShortName), _
 '           eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
 '           eacFkOid Or eacFkOidParent, , indent _
 '         )
 ' ### ENDIF IVK ###
       Else
 ' ### IF IVK ###
         printConditional fileNo, _
           genTransformedAttrDeclByDomainWithColReUse( _
             conOid, cosnOid, eavtDomain, g_domainIndexOid, _
             transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
             eacFkOid Or IIf(isPsForming, eacPsFormingOid, 0) Or IIf(supportPsCopy, eacFkExtPsCopyOid, 0) Or eacFkOidParent, , indent _
           )
 ' ### ELSE IVK ###
 '       printConditional fileNo, _
 '         genTransformedAttrDeclByDomainWithColReUse( _
 '           conOid, cosnOid, eavtDomain, g_domainIndexOid, _
 '           transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
 '           eacFkOid Or eacFkOidParent, , indent _
 '         )
 ' ### ENDIF IVK ###
       End If
     Else
       Dim j As Integer
       For j = 1 To attrRefs.numDescriptors Step 1
           If g_attributes.descriptors(attrRefs.descriptors(j).refIndex).isIdentifying Then
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 g_attributes.descriptors(attrRefs.descriptors(j).refIndex).attributeName, g_attributes.descriptors(attrRefs.descriptors(j).refIndex).shortName, g_attributes.descriptors(attrRefs.descriptors(j).refIndex).valueType, g_attributes.descriptors(attrRefs.descriptors(j).refIndex).valueTypeIndex, _
                 transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, , , indent _
               )
           End If
       Next j
       If acmEntityType = eactRelationship Then
           If g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).useSurrogateKey Then
 ' ### IF IVK ###
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).shortName), genSurrogateKeyShortName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).shortName), _
                 eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
                 eacFkOid Or IIf(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).isPsForming, eacPsFormingOid, 0) Or IIf(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).supportExtendedPsCopy, eacFkExtPsCopyOid, 0), , indent _
               )
 ' ### ELSE IVK ###
 '           printConditional fileNo, _
 '             genTransformedAttrDeclByDomainWithColReUse( _
 '               genSurrogateKeyName(ddlType, .shortName), genSurrogateKeyShortName(ddlType, .shortName), _
 '               eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
 '               eacFkOid, , indent _
 '             )
 ' ### ENDIF IVK ###
           Else
             MsgBox "FIXME: implement NL-Text-support for relationships not using a surrogate key"
           End If
           If g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).useSurrogateKey Then
 ' ### IF IVK ###
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).shortName), genSurrogateKeyShortName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).shortName), _
                 eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
                 eacFkOid Or IIf(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).isPsForming, eacPsFormingOid, 0) Or IIf(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).supportExtendedPsCopy, eacFkExtPsCopyOid, 0), , indent _
               )
 ' ### ELSE IVK ###
 '           printConditional fileNo, _
 '             genTransformedAttrDeclByDomainWithColReUse( _
 '               genSurrogateKeyName(ddlType, .shortName), genSurrogateKeyShortName(ddlType, .shortName), _
 '               eavtDomain, g_domainIndexOid, transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, _
 '               eacFkOid, , indent _
 '             )
 ' ### ENDIF IVK ###
           Else
             MsgBox "FIXME: implement NL-Text-support for relationships not using a surrogate key"
           End If
       End If

       If includeFksInPks Then
         For j = 1 To relRefs.numRefs
           If relRefs.refs(j).refType = etRight Then
               If g_relationships.descriptors(relRefs.refs(j).refIndex).isIdentifyingLeft And g_relationships.descriptors(relRefs.refs(j).refIndex).maxLeftCardinality = 1 Then
 ' ### IF IVK ###
                 genFkTransformedAttrDeclsForRelationshipWithColReUse g_relationships.descriptors(relRefs.refs(j).refIndex).leftEntityIndex, _
                   g_relationships.descriptors(relRefs.refs(j).refIndex).relIndex, IIf(g_relationships.descriptors(relRefs.refs(j).refIndex).useRlLdmRelName, g_relationships.descriptors(relRefs.refs(j).refIndex).rlLdmRelName, g_relationships.descriptors(relRefs.refs(j).refIndex).shortName & g_relationships.descriptors(relRefs.refs(j).refIndex).rlShortRelName), _
                   Not g_relationships.descriptors(relRefs.refs(j).refIndex).useRlLdmRelName, g_relationships.descriptors(relRefs.refs(j).refIndex).isNationalizable, "NOT NULL", transformation, tabColumns, fileNo, ddlType, outputMode, indent
 ' ### ELSE IVK ###
 '               genFkTransformedAttrDeclsForRelationshipWithColReUse .leftEntityIndex, _
 '                 .relIndex, IIf(.useRlLdmRelName, .rlLdmRelName, .shortName & .rlShortRelName), _
 '                 Not .useRlLdmRelName, "NOT NULL", transformation, tabColumns, fileNo, ddlType, outputMode, indent
 ' ### ENDIF IVK ###
               End If
           ElseIf relRefs.refs(j).refType = etLeft Then
               If g_relationships.descriptors(relRefs.refs(j).refIndex).isIdentifyingRight And g_relationships.descriptors(relRefs.refs(j).refIndex).maxRightCardinality = 1 Then
 ' ### IF IVK ###
                 genFkTransformedAttrDeclsForRelationshipWithColReUse g_relationships.descriptors(relRefs.refs(j).refIndex).rightEntityIndex, _
                   g_relationships.descriptors(relRefs.refs(j).refIndex).relIndex, IIf(g_relationships.descriptors(relRefs.refs(j).refIndex).useLrLdmRelName, g_relationships.descriptors(relRefs.refs(j).refIndex).lrLdmRelName, g_relationships.descriptors(relRefs.refs(j).refIndex).shortName & g_relationships.descriptors(relRefs.refs(j).refIndex).lrShortRelName), _
                   Not g_relationships.descriptors(relRefs.refs(j).refIndex).useLrLdmRelName, g_relationships.descriptors(relRefs.refs(j).refIndex).isNationalizable, "NOT NULL", transformation, tabColumns, fileNo, ddlType, outputMode, indent
 ' ### ELSE IVK ###
 '               genFkTransformedAttrDeclsForRelationshipWithColReUse .rightEntityIndex, _
 '                 .relIndex, IIf(.useLrLdmRelName, .lrLdmRelName, .shortName & .lrShortRelName), _
 '                 Not .useLrLdmRelName, "NOT NULL", transformation, tabColumns, fileNo, ddlType, outputMode, indent
 ' ### ENDIF IVK ###
               End If
           End If
         Next j
       End If
 
       tabAttrList = tabAttrList & IIf(tabAttrList = "", "", ",") & getPkAttrListByClass(acmEntityIndex, ddlType, , forLrt)
       pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ",") & getPkAttrListByClass(acmEntityIndex, ddlType, , forLrt)
     End If

     printSectionHeader "Language Id", fileNo, outputMode
     printConditional fileNo, _
       genTransformedAttrDeclByDomainWithColReUse( _
         conLanguageId, cosnLanguageId, eavtDomainEnumId, g_enumIndexLanguage, _
         transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, eacLangId, , indent _
       )
   End If

   If acmEntityType = eactEnum Then
       printSectionHeader "REF Id", fileNo, outputMode
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conEnumRefId, cosnEnumRefId, eavtDomainEnumId, g_enums.descriptors(acmEntityIndex).enumIndex, _
           transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, , , indent _
         )
       printSectionHeader "LABEL", fileNo, outputMode
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conEnumLabelText, cosnEnumLabelText, eavtDomainEnumValue, g_enums.descriptors(acmEntityIndex).enumIndex, _
           transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL", , ddlType, , outputMode, , , indent _
         )
   End If

   For i = 1 To nlAttrRefs.numDescriptors Step 1
     If onlyThisAttribute = -1 Or (onlyThisAttribute = nlAttrRefs.descriptors(i).refIndex) Then
 ' ### IF IVK ###
         If hasNoIdentity Or (g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).isTimeVarying = forGen) Then
 ' ### ELSE IVK ###
 '       If .isTimeVarying = forGen Then
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
           printSectionHeader "NL-Text Attribute (" & g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).attributeName & "@" & g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).className & ")", fileNo, outputMode
           printConditional fileNo, _
             genTransformedAttrDeclByDomainWithColReUse( _
               g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).attributeName, g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).shortName, g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).valueType, g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).valueTypeIndex, transformation, tabColumns, _
               acmEntityType, acmEntityIndex, IIf(forceNotNull Or (numNlAttrs > 1), "", "NOT NULL"), , _
               ddlType, , outputMode, , , indent, , , , , , nlAttrRefs.descriptors(i).refIndex _
             )
 ' ### ELSE IVK ###
 '         printConditional fileNo, _
 '           genTransformedAttrDeclByDomainWithColReUse( _
 '             .attributeName, .shortName, .valueType, .valueTypeIndex, transformation, tabColumns, _
 '             acmEntityType, acmEntityIndex, IIf(forceNotNull Or (numNlAttrs > 1), "", "NOT NULL"), , _
 '             ddlType, , outputMode, , , indent, , , , , nlAttrRefs.descriptors(i).refIndex)
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
           If g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).isNationalizable Then
             printSectionHeader "nationalized NL-Text Attribute (" & g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).attributeName & ")", fileNo, outputMode
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).attributeName & gc_anSuffixNat, g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).shortName & gc_asnSuffixNat, g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).valueType, g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).valueTypeIndex, transformation, _
                 tabColumns, acmEntityType, acmEntityIndex, , , ddlType, , outputMode, , , indent, , , , , , nlAttrRefs.descriptors(i).refIndex _
               )

             printSectionHeader "Is nationalized Text active?", fileNo, outputMode
             printConditional fileNo, _
               genTransformedAttrDeclByDomainWithColReUse( _
                 g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).attributeName & gc_anSuffixNatActivated, g_attributes.descriptors(nlAttrRefs.descriptors(i).refIndex).shortName & gc_asnSuffixNatActivated, _
                 eavtDomain, g_domainIndexBoolean, transformation, _
                 tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT 0" & IIf(ddlType = edtPdm And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), , _
                 ddlType, , outputMode, eacNationalBool, , indent, , , gc_dbFalse, , , nlAttrRefs.descriptors(i).refIndex _
               )
           End If
 ' ### ENDIF IVK ###
         End If
     End If
   Next i
 
   If acmEntityType = eactClass Then
       For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexes) Step 1
         genNlsTransformedAttrListForEntityWithColReUse g_classes.descriptors(acmEntityIndex).subclassIndexes(i), eactClass, transformation, tabColumns, _
           fileNo, onlyThisAttribute, forceNotNull Or numNlAttrs > 1, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, forLrt, False, outputMode, qualTabName, , , , , useAlternativeDefaults
       Next i
   ElseIf acmEntityType = eactRelationship Then
       For i = 1 To g_relationships.descriptors(acmEntityIndex).reusingRelIndexes.numIndexes Step 1
         genNlsTransformedAttrListForEntityWithColReUse g_relationships.descriptors(acmEntityIndex).reusingRelIndexes.indexes(i), eactRelationship, _
           transformation, tabColumns, fileNo, onlyThisAttribute, forceNotNull Or numNlAttrs > 1, ddlType, thisOrgIndex, thisPoolIndex, indent, forGen, forLrt, False, outputMode, qualTabName, , , , , useAlternativeDefaults
       Next i
   End If
 
   If includeMetaAttrs Then
 ' ### IF IVK ###
     If isPsTagged And usePsTagInNlTextTables And Not noRangePartitioning Then
       printSectionHeader "Product Structure Tag", fileNo, outputMode
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conPsOid, cosnPsOid, eavtDomain, g_domainIndexOid, transformation, tabColumns, _
           acmEntityType, acmEntityIndex, IIf(psTagOptional, "", "NOT NULL"), True, ddlType, , outputMode, eacPsOid, , indent, , _
           "[LDM] Product Structure Tag" _
         )
     Else
        If className = "GenericCode" Then
          printSectionHeader "Division column", fileNo, outputMode
          printConditional fileNo, _
            genTransformedAttrDeclByDomainWithColReUse( _
            conDivOid, cosnDivOid, eavtDomain, g_domainIndexOid, _
            transformation, tabColumns, acmEntityType, acmEntityIndex, "NOT NULL DEFAULT 0", useVersiontag, ddlType, , _
            outputMode, eacDivOid, , indent, , "[LDM] Division Tag" _
          )
        End If
     End If

 ' ### ENDIF IVK ###
     If g_cfgGenLogChangeForNlTabs Then
       genTransformedLogChangeAttrDeclsWithColReUse fileNo, transformation, tabColumns, acmEntityType, acmEntityIndex, ddlType, className, outputMode, , , useAlternativeDefaults
     End If
 
     If useVersiontag Then
       printSectionHeader "Object Version ID", fileNo, outputMode
       printConditional fileNo, _
         genTransformedAttrDeclByDomainWithColReUse( _
           conVersionId, cosnVersionId, eavtDomain, g_domainIndexVersion, transformation, tabColumns, _
           acmEntityType, acmEntityIndex, "NOT NULL DEFAULT 1" & IIf(ddlType = edtPdm And dbCompressSystemDefaults, " COMPRESS SYSTEM DEFAULT", ""), _
           False, ddlType, , outputMode, eacVid, , indent, , , "1" _
         )
     End If
   End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genNlsSingleTabForEntity( _
   rootAcmEntityIndex As Integer, _
   acmEntityIndex As Integer, _
   acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoFk As Integer, _
   fileNoLrtFk As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional ByRef parentTabPkAttrList As String = "", _
   Optional ByRef parentTabPkAttrDecl As String = "", _
   Optional useAlternativeDefaults As Boolean = False _
 )
   Dim sectionName As String
   Dim sectionShortName As String
   Dim sectionIndex As Integer
   Dim className As String
   Dim classShortName As String
   Dim isUserTransactional As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim useMqtToImplementLrt As Boolean
   Dim attrRefs As AttrDescriptorRefs
   Dim notAcmRelated As Boolean
   Dim noAlias As Boolean
   Dim useSurrogateKey As Boolean
   Dim useVersiontag As Boolean
   Dim tabSpaceIndexData As Integer
   Dim tabSpaceIndexIndex As Integer
   Dim isCtoAliasCreated As Boolean
   Dim nlObjName As String
   Dim nlObjShortName As String
 ' ### IF IVK ###
   Dim isSubjectToArchiving As Boolean
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
   Dim tabPartitionType As PartitionType
   Dim noRangePartitioning As Boolean
   Dim rangePartitioningAll As Boolean
 ' ### ENDIF IVK ###
   Dim poolSupportLrt As Boolean
   Dim poolCommonItemsLocal As Boolean
   Dim poolSuppressRefIntegrity As Boolean

   On Error GoTo ErrorExit

   If thisPoolIndex > 0 Then
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
       poolCommonItemsLocal = g_pools.descriptors(thisPoolIndex).commonItemsLocal
       poolSuppressRefIntegrity = g_pools.descriptors(thisPoolIndex).suppressRefIntegrity
   End If

   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_classes.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       className = g_classes.descriptors(acmEntityIndex).className
       classShortName = g_classes.descriptors(acmEntityIndex).shortName
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       useMqtToImplementLrt = g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
       attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
       notAcmRelated = g_classes.descriptors(acmEntityIndex).notAcmRelated
       noAlias = g_classes.descriptors(acmEntityIndex).noAlias
       useSurrogateKey = g_classes.descriptors(acmEntityIndex).useSurrogateKey
       useVersiontag = g_classes.descriptors(acmEntityIndex).useVersiontag
       tabSpaceIndexData = g_classes.descriptors(acmEntityIndex).tabSpaceIndexNl
       tabSpaceIndexIndex = g_classes.descriptors(acmEntityIndex).tabSpaceIndexIndex
       isCtoAliasCreated = g_classes.descriptors(acmEntityIndex).isCtoAliasCreated
 ' ### IF IVK ###
       isSubjectToArchiving = g_classes.descriptors(acmEntityIndex).isSubjectToArchiving
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = g_classes.descriptors(acmEntityIndex).psTagOptional
       noRangePartitioning = g_classes.descriptors(acmEntityIndex).noRangePartitioning
       rangePartitioningAll = g_classes.descriptors(acmEntityIndex).rangePartitioningAll
 ' ### ENDIF IVK ###

       nlObjName = genNlObjName(className, , forGen)
       nlObjShortName = genNlObjShortName(classShortName, , forGen)
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_relationships.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       className = g_relationships.descriptors(acmEntityIndex).relName
       classShortName = g_relationships.descriptors(acmEntityIndex).shortName
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       useMqtToImplementLrt = g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
       attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
       notAcmRelated = g_relationships.descriptors(acmEntityIndex).notAcmRelated
       noAlias = g_relationships.descriptors(acmEntityIndex).noAlias
       useSurrogateKey = useSurrogateKeysForNMRelationships And (g_relationships.descriptors(acmEntityIndex).attrRefs.numDescriptors > 0 Or g_relationships.descriptors(acmEntityIndex).logLastChange)
       useVersiontag = g_relationships.descriptors(acmEntityIndex).useVersiontag
       tabSpaceIndexData = g_relationships.descriptors(acmEntityIndex).tabSpaceIndexNl
       tabSpaceIndexIndex = g_relationships.descriptors(acmEntityIndex).tabSpaceIndexIndex
       isCtoAliasCreated = g_relationships.descriptors(acmEntityIndex).isCtoAliasCreated
 ' ### IF IVK ###
       isSubjectToArchiving = g_relationships.descriptors(acmEntityIndex).isSubjectToArchiving
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = False
       noRangePartitioning = g_relationships.descriptors(acmEntityIndex).noRangePartitioning
       rangePartitioningAll = False
 ' ### ENDIF IVK ###
 
       nlObjName = genNlObjName(className, , forGen)
       nlObjShortName = genNlObjShortName(classShortName, , forGen)
   End If

   Dim genSupportForLrt As Boolean
   genSupportForLrt = False
   If g_genLrtSupport And isUserTransactional Then
     If thisPoolIndex > 0 Then
       genSupportForLrt = poolSupportLrt
     Else
       genSupportForLrt = ddlType = edtLdm
     End If
   End If

   Dim qualNlTabName As String, qualNlTabNameLdm As String
   Dim qualTabName As String, qualTabNameLdm As String
   Dim qualLangTabName As String
   Dim qualIndexName As String
   Dim pkAttrList As String
   Dim tabAttrList As String

   qualNlTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, , True)
   qualNlTabNameLdm = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, edtLdm, thisOrgIndex, thisPoolIndex, forGen, forLrt, , True)

   addTabToDdlSummary qualNlTabName, ddlType, notAcmRelated
   registerQualTable qualNlTabNameLdm, qualNlTabName, rootAcmEntityIndex, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, ddlType, notAcmRelated, forGen, forLrt, True

   If acmEntityType = eactClass Then
       qualTabName = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
       qualTabNameLdm = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, edtLdm, thisOrgIndex, thisPoolIndex, forGen, forLrt)
   ElseIf acmEntityType = eactRelationship Then
     qualTabName = genQualTabNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt)
     qualTabNameLdm = genQualTabNameByRelIndex(acmEntityIndex, edtLdm, thisOrgIndex, thisPoolIndex, forLrt)
   End If

   If generateDdlCreateTable Then
     printChapterHeader "NL-Table for ACM-" & IIf(acmEntityType = eactClass, "Class", "Relationship") & _
                        " """ & sectionName & "." & className & """" & IIf(forLrt, " (LRT)", ""), fileNo
     Print #fileNo,
     Print #fileNo, "CREATE TABLE"
     Print #fileNo, addTab; qualNlTabName
     Print #fileNo, "("

     genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, qualTabName, , ddlType, thisOrgIndex, thisPoolIndex, , forGen, forLrt, IIf(forLrt, edomDeclLrt, edomDeclNonLrt), , parentTabPkAttrList, parentTabPkAttrDecl, pkAttrList, tabAttrList, useAlternativeDefaults

     Print #fileNo, ")"

     Dim isDivTagged As Boolean
     isDivTagged = (acmEntityIndex = g_classIndexGenericCode)

 ' ### IF IVK ###
     genTabDeclTrailer fileNo, ddlType, isDivTagged, acmEntityType, acmEntityIndex, thisOrgIndex, thisPoolIndex, True, forLrt, False, False, IIf(isDivTagged, conDivOid, ""), tabPartitionType
 ' ### ELSE IVK ###
 '   genTabDeclTrailer fileNo, ddlType, acmEntityType, acmEntityIndex, thisOrgIndex, thisPoolIndex, True, forLrt, False
 ' ### ENDIF IVK ###
 
     Print #fileNo, gc_sqlCmdDelim
   End If

   If forLrt And lrtTablesVolatile Then
     Print #fileNo,
     Print #fileNo, addTab(0); "ALTER TABLE "; qualNlTabName; " VOLATILE CARDINALITY"; gc_sqlCmdDelim
 ' ### IF IVK ###
   ElseIf Not isCommonToPools And Not poolCommonItemsLocal And Not notAcmRelated And (Not poolSupportLrt Or Not useMqtToImplementLrt) And Not isPsTagged Then
 ' ### ELSE IVK ###
 ' ElseIf Not isCommonToPools And Not poolCommonItemsLocal And Not notAcmRelated And (Not poolSupportLrt Or Not useMqtToImplementLrt) Then
 ' ### ENDIF IVK ###
     Print #fileNo,
     Print #fileNo, addTab(0); "ALTER TABLE "; qualNlTabName; " VOLATILE CARDINALITY"; gc_sqlCmdDelim
   End If

   If ddlType = edtPdm And Not noAlias Then
 ' ### IF IVK ###
       genAliasDdl sectionIndex, nlObjName, isCommonToOrgs, isCommonToPools, Not notAcmRelated, _
                   qualNlTabNameLdm, qualNlTabName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, forLrt, False, False, False, _
                   "NL-Table """ & sectionName & "." & nlObjName & """", , isUserTransactional, False, , isSubjectToArchiving
 ' ### ELSE IVK ###
 '   genAliasDdl sectionIndex, nlObjName, isCommonToOrgs, isCommonToPools, Not notAcmRelated, _
 '               qualNlTabNameLdm, qualNlTabName, isCtoAliasCreated, ddlType, thisOthisOrgIndexrgId, thisPoolIndex, edatTable, False, forLrt, _
 '               "NL-Table """ & sectionName & "." & nlObjName & """", , isUserTransactional
 ' ### ENDIF IVK ###
   End If

   ' DDL for Primary Key
   Dim pkName As String, uiName As String, ukName As String
   pkName = genPkName(tabPrefixNl & UCase(classShortName & "NLT"), tabPrefixNl & UCase(classShortName & "NLT"), _
                      ddlType, thisOrgIndex, thisPoolIndex, False, forLrt)
   ukName = "UK_" & Mid(pkName, 4)
 
   uiName = genUkName(sectionIndex, tabPrefixNl & classShortName & IIf(forGen, "G", "") & "NLT", _
                      tabPrefixNl & classShortName & IIf(forGen, "G", "") & "NLT", ddlType, thisOrgIndex, thisPoolIndex, False, forLrt)

   Dim useSurrogateKeysForNlTabs As Boolean
   useSurrogateKeysForNlTabs = True

   If useSurrogateKeysForNlTabs And useSurrogateKey Then
     If generateDdlCreatePK Then
       printSectionHeader "Primary Key for """ & qualNlTabName & """", fileNo
       Print #fileNo, addTab(0); "ALTER TABLE"
       Print #fileNo, addTab(1); qualNlTabName
       Print #fileNo, addTab(0); "ADD CONSTRAINT"
       Print #fileNo, addTab(1); pkName
       Print #fileNo, addTab(0); "PRIMARY KEY"
       Print #fileNo, addTab(1); "(" & g_anOid & IIf(g_genLrtSupport And forLrt, "," & g_anInLrt & "," & g_anLrtState, "") & ")"
       Print #fileNo, gc_sqlCmdDelim
 
       If thisPoolIndex = 2 And Not isCommonToPools And Not isCommonToOrgs And Not noRangePartitioning And rangePartitioningAll And Not forLrt Then
       'If thisPoolIndex = 2 And isPsTagged And Not noRangePartitioning And rangePartitioningAll And Not forLrt Then
       'If g_pools.descriptors(thisPoolIndex).id = 1 And isPsTagged And Not noRangePartitioning And rangePartitioningAll And Not forLrt Then
 
         Dim isLeftPs As Boolean
         Dim isRightPs As Boolean
         Dim additionalUK As Boolean
         Dim i As Integer
         For i = 1 To g_relationships.numDescriptors Step 1
           isLeftPs = g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged
           isRightPs = g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged
           If g_relationships.descriptors(i).leftClassSectionName = sectionName And g_relationships.descriptors(i).leftClassName = className And _
              g_relationships.descriptors(i).maxRightCardinality = -1 Then
              'g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged = True Then
             additionalUK = True
             Exit For
           End If
           If g_relationships.descriptors(i).rightClassSectionName = sectionName And g_relationships.descriptors(i).rightClassName = className And _
              g_relationships.descriptors(i).maxLeftCardinality = -1 Then
              'g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged = True Then
             additionalUK = True
             Exit For
           End If
           If g_relationships.descriptors(i).leftClassSectionName = sectionName And g_relationships.descriptors(i).leftClassName = className And _
              g_relationships.descriptors(i).minLeftCardinality = 1 And _
              g_relationships.descriptors(i).maxLeftCardinality = 1 And _
              g_relationships.descriptors(i).minRightCardinality = 1 And _
              g_relationships.descriptors(i).maxRightCardinality = 1 Then
              'g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).isPsTagged = True Then
             additionalUK = True
             Exit For
           End If
           If g_relationships.descriptors(i).rightClassSectionName = sectionName And g_relationships.descriptors(i).rightClassName = className And _
              g_relationships.descriptors(i).minLeftCardinality = 1 And _
              g_relationships.descriptors(i).maxLeftCardinality = 1 And _
              g_relationships.descriptors(i).minRightCardinality = 1 And _
              g_relationships.descriptors(i).maxRightCardinality = 1 Then
              'g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).isPsTagged = True Then
             additionalUK = True
             Exit For
           End If
         Next i

         If additionalUK Then
           printSectionHeader "Unique Constraint for """ & qualNlTabName & """", fileNo

           Dim columnName As String
           columnName = IIf(isLeftPs Or isRightPs, conPsOid, conDivOid)

           Print #fileNo,
           Print #fileNo, addTab(0); "CREATE UNIQUE INDEX"
           Print #fileNo, addTab(1); genQualUkName(sectionIndex, className, ukName, ddlType, thisOrgIndex, thisPoolIndex)
           Print #fileNo, addTab(0); "ON"
           Print #fileNo, addTab(1); qualNlTabName; "("; g_anOid & IIf(g_genLrtSupport And forLrt, "," & g_anInLrt & "," & g_anLrtState, ""); ", "; columnName; ")"
           Print #fileNo, gc_sqlCmdDelim

           'Print #fileNo, addTab(0); "ALTER TABLE"
           'Print #fileNo, addTab(1); qualNlTabName
           'Print #fileNo, addTab(0); "ADD CONSTRAINT"
           'Print #fileNo, addTab(1); ukName
           'Print #fileNo, addTab(1); "UNIQUE (" & g_anOid & IIf(g_genLrtSupport And forLrt, "," & g_anInLrt & "," & g_anLrtState, "") & ", "; columnName; ")"
           'Print #fileNo, gc_sqlCmdDelim
         End If
       End If
 
     End If

     If generateDdlCreateIndex Then
       If indexExcp(uiName, thisOrgIndex) = False Then
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE INDEX"
         Print #fileNo, addTab(1); uiName
         Print #fileNo, addTab(0); "ON"
 ' ### IF IVK ###
 
         Dim additionalColumnName As String
         If isPsTagged And Not noRangePartitioning And usePsTagInNlTextTables Then
             additionalColumnName = g_anPsOid
         ElseIf className = clnGenericCode Then
             additionalColumnName = g_anDivOid
         Else
             additionalColumnName = ""
         End If


         Print #fileNo, addTab(1); qualNlTabName; "("; _
                                   IIf(additionalColumnName <> "", additionalColumnName & ",", ""); _
                                    UCase(tabAttrList); IIf(tabAttrList = "", "", ","); _
                                   g_anLanguageId; _
                                   IIf(g_genLrtSupport And forLrt, "," & g_anInLrt, ""); _
                                   ")"
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(1); qualNlTabName; "("; _
 '                               UCase(tabAttrList); IIf(tabAttrList = "", "", ","); _
 '                               g_anLanguageId; _
 '                               IIf(g_genLrtSupport And forLrt, "," & g_anInLrt, ""); _
 '                               ")"
 ' ### ENDIF IVK ###
         Print #fileNo, gc_sqlCmdDelim
       End If ' indexExcp
     End If
   Else
     If generateDdlCreatePK Then
       Print #fileNo, addTab(0); "ALTER TABLE"
       Print #fileNo, addTab(1); qualNlTabName
       Print #fileNo, addTab(0); "ADD CONSTRAINT"
       Print #fileNo, addTab(1); pkName
       Print #fileNo, addTab(0); "PRIMARY KEY"
       Print #fileNo, addTab(1); "("; UCase(tabAttrList); IIf(tabAttrList = "", "", ", "); g_anLanguageId; ")"
       Print #fileNo, gc_sqlCmdDelim
     End If
   End If

   If Not forLrt Then
     ' DDL for Foreign Key to 'Parent Table'
     If Not poolSuppressRefIntegrity Then
       If generateDdlCreateFK Then
         printSectionHeader "Foreign Key to ""NL-Parent Table"" (" & qualNlTabName & " -> " & qualTabName & ")", fileNoFk

         Print #fileNoFk,
         Print #fileNoFk, addTab(0); "ALTER TABLE"
         Print #fileNoFk, addTab(1); qualNlTabName
         Print #fileNoFk, addTab(0); "ADD CONSTRAINT"
         Print #fileNoFk, addTab(1); genFkName(classShortName & "NLPAR", _
                                               classShortName & "NLPAR", "", ddlType, thisOrgIndex, thisPoolIndex, False, forLrt)
         Print #fileNoFk, addTab(0); "FOREIGN KEY"
         If sectionName = snDbMeta Then
           Print #fileNoFk, addTab(1); "("; UCase(tabAttrList); ")"
           Print #fileNoFk, addTab(0); "REFERENCES"
           Print #fileNoFk, addTab(1); qualTabName; " ("; UCase(pkAttrList); ")"
         Else
           If acmEntityType = eactRelationship Then
             Print #fileNoFk, addTab(1); "("; getFkSrcAttrSeqExt(g_relationships.descriptors(acmEntityIndex).leftEntityIndex, "", thisPoolIndex, ddlType, tabAttrList, , , g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt); ")"
             Print #fileNoFk, addTab(0); "REFERENCES"
            Print #fileNoFk, addTab(1); qualTabName; " ("; getFkTargetAttrSeqExt(g_relationships.descriptors(acmEntityIndex).leftEntityIndex, thisPoolIndex, ddlType, pkAttrList, g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr, g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt); ")"
           Else
             Print #fileNoFk, addTab(1); "("; getFkSrcAttrSeqExt(acmEntityIndex, "", thisPoolIndex, ddlType, tabAttrList); ")"
             Print #fileNoFk, addTab(0); "REFERENCES"
             Print #fileNoFk, addTab(1); qualTabName; " ("; getFkTargetAttrSeqExt(acmEntityIndex, thisPoolIndex, ddlType, pkAttrList, g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr); ")"
           End If
         End If
         Print #fileNoFk, gc_sqlCmdDelim
       End If

       registerQualLdmFk qualNlTabNameLdm, qualTabNameLdm, acmEntityIndex, acmEntityType
     End If

     If (ddlType = edtPdm) And generateIndexOnFk And generateDdlCreateIndex Then
       qualIndexName = genQualIndexName(sectionIndex, className & IIf(forGen, "G", "") & "PAR", classShortName & IIf(forGen, "G", "") & "PAR", ddlType, thisOrgIndex, thisPoolIndex)

       If indexExcp(qualIndexName, thisOrgIndex) = False Then
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE INDEX"
         Print #fileNo, addTab(1); qualIndexName
         Print #fileNo, addTab(0); "ON"
         Print #fileNo, addTab(1); qualNlTabName
         Print #fileNo, addTab(0); "("
         Print #fileNo, addTab(1); UCase(tabAttrList)
         Print #fileNo, addTab(0); ")"
         Print #fileNo, gc_sqlCmdDelim
       End If ' indexExcp
     End If
   End If
 ' ### IF IVK ###
 
   If Not forLrt And Not poolSuppressRefIntegrity Then
     If acmEntityType = eactClass Then
       genFKsForPsTagOnClass qualNlTabName, qualNlTabNameLdm, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt, , forGen, True, tabPartitionType
       genFKsForDivTagOnClass qualNlTabName, qualNlTabNameLdm, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt, , forGen, True, tabPartitionType
     End If
   End If
 ' ### ENDIF IVK ###
 
   If Not poolSuppressRefIntegrity Then
     ' DDL for Foreign Key to 'Language Table'
     If generateDdlCreateFK Then
       printSectionHeader "Foreign Key to ""Language Table""", fileNo
       Print #fileNo,
       Print #fileNo, addTab(0); "ALTER TABLE"
       Print #fileNo, addTab(1); qualNlTabName
       Print #fileNo, addTab(0); "ADD CONSTRAINT"
       Print #fileNo, addTab(1); genFkName(classShortName & "NLLAN", _
                                           classShortName & "NLLAN", "", ddlType, thisOrgIndex, thisPoolIndex, False, forLrt)
       Print #fileNo, addTab(0); "FOREIGN KEY"
       Print #fileNo, addTab(1); "("; g_anLanguageId; ")"
       Print #fileNo, addTab(0); "REFERENCES"
       Print #fileNo, addTab(1); g_qualTabNameLanguage; "("; g_anEnumId; ")"
       Print #fileNo, gc_sqlCmdDelim
     End If

     registerQualLdmFk qualNlTabNameLdm, genQualTabNameByEnumIndex(g_enumIndexLanguage, edtLdm), acmEntityIndex, acmEntityType
   End If

   If (ddlType = edtPdm) And generateIndexOnFkForNLang And generateDdlCreateIndex Then
     qualIndexName = genQualIndexName(sectionIndex, className & "LAN", classShortName & "LAN", ddlType, thisOrgIndex, thisPoolIndex)

     If indexExcp(qualIndexName, thisOrgIndex) = False Then
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE INDEX"
       Print #fileNo, addTab(1); qualIndexName
       Print #fileNo, addTab(0); "ON"
       Print #fileNo, addTab(1); qualNlTabName
       Print #fileNo, addTab(0); "("
       Print #fileNo, addTab(1); g_anLanguageId
       Print #fileNo, addTab(0); ")"
       Print #fileNo, gc_sqlCmdDelim
     End If ' indexExcp
   End If

   If genSupportForLrt And Not poolSuppressRefIntegrity Then
 ' ### IF IVK ###
     genFksForLrtByEntity qualNlTabName, qualNlTabNameLdm, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNoLrtFk, ddlType, forGen, forLrt, "NLT", tabPartitionType
 ' ### ELSE IVK ###
 '   genFksForLrtByEntity qualNlTabName, qualNlTabNameLdm, acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNoLrtFk, ddlType, forGen, forLrt, "NLT"
 ' ### ENDIF IVK ###
   End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genNlsTabsForClassRecursive( _
   ByRef rootClassIndex As Integer, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoFk As Integer, _
   fileNoLrtFk As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional useAlternativeDefaults As Boolean = False _
 )
   On Error GoTo ErrorExit

   genNlsSingleTabForEntity rootClassIndex, classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoLrtFk, ddlType, forGen, forLrt, , , useAlternativeDefaults
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genNlsTabsForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoFk As Integer, _
   fileNoLrtFk As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional ByRef parentTabPkAttrList As String = "", _
   Optional ByRef parentTabPkAttrDecl As String = "", _
   Optional useAlternativeDefaults As Boolean = False _
 )
   On Error GoTo ErrorExit

   genNlsSingleTabForEntity thisRelIndex, thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, fileNoFk, fileNoLrtFk, ddlType, forGen, forLrt, , , useAlternativeDefaults
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Sub genEnumFKsForClassRecursiveWithColReUse( _
   ByRef qualTabName As String, _
   ByRef qualTabNameLdm As String, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   ddlType As DdlTypeId, _
   ByRef tabColumns As EntityColumnDescriptors, _
   forGen As Boolean, _
   parentHasNoIdentity As Boolean, _
   level As Integer, _
   Optional tabPartitionType As PartitionType = ptNone _
 )
 ' ### ELSE IVK ###
 'Sub genEnumFKsForClassRecursiveWithColReUse( _
 ' ByRef qualTabName As String, _
 ' ByRef qualTabNameLdm As String, _
 ' ByRef classIndex As Integer, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' ByRef fileNo As Integer, _
 ' ddlType As DdlTypeId, _
 ' ByRef tabColumns As EntityColumnDescriptors, _
 ' forGen As Boolean, _
 ' level As Integer _
 ')
 ' ### ENDIF IVK ###
   Dim sectionName As String
   Dim sectionIndex As Integer
   Dim className As String
   Dim classShortName As String
   Dim classIsUserTransactional As Boolean
 ' ### IF IVK ###
   Dim classHasNoIdentity As Boolean
 ' ### ENDIF IVK ###

   On Error GoTo ErrorExit

     If g_classes.descriptors(classIndex).noFks Then
       Exit Sub
     End If

     sectionName = g_classes.descriptors(classIndex).sectionName
     sectionIndex = g_classes.descriptors(classIndex).sectionIndex
     className = g_classes.descriptors(classIndex).className
     classShortName = g_classes.descriptors(classIndex).shortName
 ' ### IF IVK ###
     classHasNoIdentity = parentHasNoIdentity Or g_classes.descriptors(classIndex).hasNoIdentity
 ' ### ENDIF IVK ###
     classIsUserTransactional = g_classes.descriptors(classIndex).isUserTransactional

     Dim qualEnumTabName As String, qualEnumTabNameLdm As String
     Dim db2AttrName As String
     Dim colIndex As Integer
     Dim isReused As Boolean
 
     Dim i As Integer
     For i = 1 To g_classes.descriptors(classIndex).attrRefs.numDescriptors Step 1
       If g_classes.descriptors(classIndex).attrRefs.descriptors(i).refType = eadrtEnum Then
          If Not g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).reusedAttrIndex > 0 Then
 ' ### IF IVK ###
            If UCase(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).sectionName) = UCase(sectionName) And UCase(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).className) = UCase(className) And _
                IIf(classHasNoIdentity, Not forGen, g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).isTimeVarying = forGen) And (g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueType = eavtEnum) Then
 ' ### ELSE IVK ###
 '          If UCase(.sectionName) = UCase(sectionName) And UCase(.className) = UCase(className) And _
 '              (.isTimeVarying = forGen) And (.valueType = eavtEnum) Then
 ' ### ENDIF IVK ###
               Dim effectiveDomainIndex As Integer
               If g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueType = eavtDomain Then
                 effectiveDomainIndex = g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).domainIndex
               ElseIf g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueType = eavtEnum Then
                 effectiveDomainIndex = g_enums.descriptors(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueTypeIndex).domainIndexId
               End If

                 qualEnumTabName = genQualTabNameByEnumIndex(g_enums.descriptors(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueTypeIndex).enumIndex, ddlType, thisOrgIndex, thisPoolIndex)
                 qualEnumTabNameLdm = genQualTabNameByEnumIndex(g_enums.descriptors(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueTypeIndex).enumIndex, edtLdm, thisOrgIndex, thisPoolIndex)

               ' DDL for Foreign Key to 'Enum Table'
               If level <= 1 Or reuseColumnsInTabsForOrMapping Then
                 db2AttrName = genAttrName(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).attributeName & gc_enumAttrNameSuffix, ddlType)
               Else
                 db2AttrName = genAttrName(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).attributeName & gc_enumAttrNameSuffix, ddlType, className, classShortName)
               End If
 
               colIndex = findColumnToUse(tabColumns, db2AttrName, className, eactClass, g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).attributeName, g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueType, g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueTypeIndex, isReused, eacFkOid)
 
 ' ### IF IVK ###
               If Not isReused And (ddlType = edtLdm Or (Not (UCase(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).attributeName) = UCase(enStatus) And classIsUserTransactional))) Then
                 Dim fkName As String
                 fkName = genFkName(className, classShortName, g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex)

                 If generateDdlCreateFK Then
                   printSectionHeader _
                     "Foreign Key to ""Enum Table"" on """ & g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).attributeName & "@" & g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).className & _
                     """ (" & g_enums.descriptors(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueTypeIndex).sectionName & "." & g_enums.descriptors(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueTypeIndex).enumName & ")", fileNo
                   Print #fileNo,
                   Print #fileNo, addTab(0); "ALTER TABLE"
                   Print #fileNo, addTab(1); qualTabName
                   Print #fileNo, addTab(0); "ADD CONSTRAINT"
                   Print #fileNo, addTab(1); fkName
                   Print #fileNo, addTab(0); "FOREIGN KEY"
                   Print #fileNo, addTab(1); "("; db2AttrName; ")"
                   Print #fileNo, addTab(0); "REFERENCES"
                   Print #fileNo, addTab(1); qualEnumTabName; " ("; g_anEnumId; ")"
                   Print #fileNo, gc_sqlCmdDelim
                 End If

                 registerQualLdmFk qualTabNameLdm, qualEnumTabNameLdm, classIndex, eactClass, g_enums.descriptors(g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).valueTypeIndex).notAcmRelated

                 If (ddlType = edtPdm) And generateIndexOnFk And Not g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).isIdentifying And generateDdlCreateIndex Then
                   Dim qualIndexName As String
                   qualIndexName = genQualIndexName(sectionIndex, className & g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).attributeName, classShortName & g_attributes.descriptors(g_classes.descriptors(classIndex).attrRefs.descriptors(i).refIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex)

                   If indexExcp(qualIndexName, thisOrgIndex) = False Then
                     Print #fileNo,
                     Print #fileNo, addTab(0); "CREATE INDEX"
                     Print #fileNo, addTab(1); qualIndexName
                     Print #fileNo, addTab(0); "ON"
                     Print #fileNo, addTab(1); qualTabName
                     Print #fileNo, addTab(0); "("
                     Print #fileNo, addTab(1); db2AttrName; " ASC"
                     Print #fileNo, addTab(0); ")"
                     Print #fileNo, gc_sqlCmdDelim
                   End If ' indexExcp
                 End If
               End If
 ' ### ENDIF IVK ###
             End If
           End If
       End If
     Next i

     For i = 1 To UBound(g_classes.descriptors(classIndex).subclassIndexes) Step 1
 ' ### IF IVK ###
       genEnumFKsForClassRecursiveWithColReUse qualTabName, qualTabNameLdm, g_classes.descriptors(classIndex).subclassIndexes(i), thisOrgIndex, thisPoolIndex, fileNo, _
           ddlType, tabColumns, forGen, classHasNoIdentity, level + 1, tabPartitionType
 ' ### ELSE IVK ###
 '    genEnumFKsForClassRecursiveWithColReUse qualTabName, qualTabNameLdm, .subclassIndexes(i), thisOrgIndex, thisPoolIndex, fileNo, _
 '        ddlType, tabColumns, forGen, level + 1
 ' ### ENDIF IVK ###
     Next i
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Sub genEnumFKsForClassRecursive( _
   ByRef qualTabName As String, _
   ByRef qualTabNameLdm As String, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   ddlType As DdlTypeId, _
   forGen As Boolean, _
   parentHasNoIdentity As Boolean, _
   level As Integer, _
   Optional tabPartitionType As PartitionType = ptNone _
 )
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
 
   On Error GoTo ErrorExit

   genEnumFKsForClassRecursiveWithColReUse qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNo, _
       ddlType, tabColumns, forGen, parentHasNoIdentity, level, tabPartitionType
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 ' ### ELSE IVK ###
 'Sub genEnumFKsForClassRecursive( _
 ' ByRef qualTabName As String, _
 ' ByRef qualTabNameLdm As String, _
 ' ByRef classIndex As Integer, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' ByRef fileNo As Integer, _
 ' ddlType As DdlTypeId, _
 ' forGen As Boolean, _
 ' level As Integer _
 ')
 ' Dim tabColumns As EntityColumnDescriptors
 ' tabColumns = nullEntityColumnDescriptors
 '
 ' On Error GoTo ErrorExit
 '
 ' genEnumFKsForClassRecursiveWithColReUse qualTabName, qualTabNameLdm, classIndex, thisOrgIndex, thisPoolIndex, fileNo, _
 '     ddlType, tabColumns, forGen, level
 '
 'NormalExit:
 ' On Error Resume Next
 ' Exit Sub
 '
 'ErrorExit:
 ' errMsgBox Err.description
 ' Resume NormalExit
 'End Sub
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 
 
 Sub genFKsForPsTagOnClass( _
   ByRef qualTabName As String, _
   ByRef qualTabNameLdm As String, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional tabPartitionType As PartitionType = ptNone _
 )
   ' DDL for Foreign Key to 'ProductStructure Table'
   Dim fkName As String
   Dim qualTabNameProductStructureLdm As String

   On Error GoTo ErrorExit

     If Not g_classes.descriptors(classIndex).isPsTagged Or g_classes.descriptors(classIndex).noFks Or (forNl And g_classes.descriptors(classIndex).noRangePartitioning) Then
       Exit Sub
     End If

     fkName = genFkName(g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, "PS", ddlType, thisOrgIndex, thisPoolIndex)

     qualTabNameProductStructureLdm = genQualTabNameByClassIndex(g_classIndexProductStructure, edtLdm, thisOrgIndex, thisPoolIndex)

     If generateDdlCreateFK Then
       printSectionHeader "Foreign Key to ""Product Structure"" Table", fileNo
     End If
     If g_classes.descriptors(classIndex).isCommonToOrgs And ddlType = edtPdm And Not g_classes.descriptors(g_classIndexProductStructure).isCommonToOrgs Then
       If generateDdlCreateFK Then
         logMsg "unable to implement foreign key corresponding to PS-tag for class """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & _
                """ since this class is common to MPCs", ellWarning, ddlType, thisOrgIndex, thisPoolIndex
         Print #fileNo,
         Print #fileNo, "-- unable to implement foreign key since """ & qualTabName & """ is common to MPCs"
       End If
     ElseIf g_classes.descriptors(classIndex).isCommonToPools And ddlType = edtPdm And Not g_classes.descriptors(g_classIndexProductStructure).isCommonToPools Then
       If generateDdlCreateFK Then
         logMsg "unable to implement foreign key corresponding to PS-tag for class """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & _
                """ since this class is common to Pools", ellWarning, ddlType, thisOrgIndex, thisPoolIndex
         Print #fileNo,
         Print #fileNo, "-- unable to implement foreign key since """ & qualTabName & """ is common to pools"
       End If
     Else
       If generateDdlCreateFK Then
         Print #fileNo,
         Print #fileNo, "ALTER TABLE"
         Print #fileNo, addTab; qualTabName
         Print #fileNo, "ADD CONSTRAINT"
         Print #fileNo, addTab; fkName
         Print #fileNo, "FOREIGN KEY"

         Print #fileNo, addTab; "("; g_anPsOid; ")"
         Print #fileNo, "REFERENCES"
         Print #fileNo, addTab; g_qualTabNameProductStructure; " ("; g_anOid; ")"
         Print #fileNo, gc_sqlCmdDelim
       End If

       registerQualLdmFk qualTabNameLdm, qualTabNameProductStructureLdm, classIndex, eactClass
 
       If (ddlType = edtPdm) And generateIndexOnFkForPsTag And generateDdlCreateIndex Then
         Dim qualIndexName As String
         qualIndexName = genQualIndexName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className & "PSO", g_classes.descriptors(classIndex).shortName & "PSO", ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt)

         If indexExcp(qualIndexName, thisOrgIndex) = False Then
           Print #fileNo,
           Print #fileNo, addTab(0); "CREATE INDEX"
           Print #fileNo, addTab(1); qualIndexName
           Print #fileNo, addTab(0); "ON"
           Print #fileNo, addTab(1); qualTabName
           Print #fileNo, addTab(0); "("
           Print #fileNo, addTab(1); g_anPsOid & " ASC"
           Print #fileNo, addTab(0); ")"
           Print #fileNo, gc_sqlCmdDelim
         End If ' indexExcp
       End If
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 ' ### ENDIF IVK ###
 
 Sub genFKsForDivTagOnClass( _
   ByRef qualTabName As String, _
   ByRef qualTabNameLdm As String, _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional tabPartitionType As PartitionType = ptNone _
 )
   ' DDL for Foreign Key to 'Division Table'
   Dim fkName As String
   Dim qualTabNameDivisionLdm As String
   Dim aggHeadClassIndex As Integer

   On Error GoTo ErrorExit

   aggHeadClassIndex = g_classes.descriptors(classIndex).aggHeadClassIndex

     If g_classes.descriptors(classIndex).isPsTagged Or g_classes.descriptors(classIndex).noFks Or aggHeadClassIndex <> g_classIndexGenericCode Or Not forNl Then
       Exit Sub
     End If

     fkName = genFkName(g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, "DIV", ddlType, thisOrgIndex, thisPoolIndex)
     qualTabNameDivisionLdm = genQualTabNameByClassIndex(g_classIndexDivision, edtLdm, thisOrgIndex, thisPoolIndex)

     If generateDdlCreateFK Then
       printSectionHeader "Foreign Key to ""Division"" Table", fileNo
     End If
     If g_classes.descriptors(classIndex).isCommonToOrgs And ddlType = edtPdm And Not g_classes.descriptors(g_classIndexDivision).isCommonToOrgs Then
       If generateDdlCreateFK Then
         logMsg "unable to implement foreign key corresponding to DIV-tag for class """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & _
                """ since this class is common to MPCs", ellWarning, ddlType, thisOrgIndex, thisPoolIndex
         Print #fileNo,
         Print #fileNo, "-- unable to implement foreign key since """ & qualTabName & """ is common to MPCs"
       End If
     ElseIf g_classes.descriptors(classIndex).isCommonToPools And ddlType = edtPdm And Not g_classes.descriptors(g_classIndexDivision).isCommonToPools Then
       If generateDdlCreateFK Then
         logMsg "unable to implement foreign key corresponding to DIV-tag for class """ & g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className & _
                """ since this class is common to Pools", ellWarning, ddlType, thisOrgIndex, thisPoolIndex
         Print #fileNo,
         Print #fileNo, "-- unable to implement foreign key since """ & qualTabName & """ is common to pools"
       End If
     Else
       If generateDdlCreateFK Then
         Print #fileNo,
         Print #fileNo, "ALTER TABLE"
         Print #fileNo, addTab; qualTabName
         Print #fileNo, "ADD CONSTRAINT"
         Print #fileNo, addTab; fkName
         Print #fileNo, "FOREIGN KEY"

         Print #fileNo, addTab; "("; g_anDivOid; ")"
         Print #fileNo, "REFERENCES"
         Print #fileNo, addTab; g_qualTabNameDivision; " ("; g_anOid; ")"
         Print #fileNo, gc_sqlCmdDelim
       End If

       registerQualLdmFk qualTabNameLdm, qualTabNameDivisionLdm, classIndex, eactClass
 
       If (ddlType = edtPdm) And generateDdlCreateIndex Then
         Dim qualIndexName As String
         qualIndexName = genQualIndexName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className & "DVO", g_classes.descriptors(classIndex).shortName & "DVO", ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt)

         If indexExcp(qualIndexName, thisOrgIndex) = False Then
           Print #fileNo,
           Print #fileNo, addTab(0); "CREATE INDEX"
           Print #fileNo, addTab(1); qualIndexName
           Print #fileNo, addTab(0); "ON"
           Print #fileNo, addTab(1); qualTabName
           Print #fileNo, addTab(0); "("
           Print #fileNo, addTab(1); g_anDivOid & " ASC"
           Print #fileNo, addTab(0); ")"
           Print #fileNo, gc_sqlCmdDelim
         End If ' indexExcp
       End If
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 ' ### ENDIF IVK ###
 
 
 ' ### IF IVK ###
 Sub genFksForLrtByEntity( _
   ByRef qualTabName As String, _
   ByRef qualTabNameLdm As String, _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional ByRef suffix As String = "", _
   Optional tabPartitionType As PartitionType = ptNone _
 )
 ' ### ELSE IVK ###
 'Sub genFksForLrtByEntity( _
 ' ByRef qualTabName As String, _
 ' ByRef qualTabNameLdm As String, _
 ' ByRef acmEntityIndex As Integer, _
 ' ByRef acmEntityType As AcmAttrContainerType, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' ByRef fileNo As Integer, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional forGen As Boolean = False, _
 ' Optional forLrt As Boolean = False, _
 ' Optional ByRef suffix As String = "" _
 ')
 ' ### ENDIF IVK ###
   Dim sectionName As String
   Dim sectionIndex As Integer
   Dim className As String
   Dim classShortName As String
   Dim isUserTransactional As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim specificToOrgId As Integer
   Dim specificToPool As Integer
   Dim isPsTagged As Boolean
 ' ### IF IVK ###
   Dim condenseData As Boolean
   condenseData = False
 ' ### ENDIF IVK ###

   On Error GoTo ErrorExit

   If acmEntityType = eactClass Then
       If g_classes.descriptors(acmEntityIndex).noFks Then
         Exit Sub
       End If

       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       className = g_classes.descriptors(acmEntityIndex).className
       classShortName = g_classes.descriptors(acmEntityIndex).shortName
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       specificToOrgId = g_classes.descriptors(acmEntityIndex).specificToOrgId
       specificToPool = g_classes.descriptors(acmEntityIndex).specificToPool
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
 ' ### IF IVK ###
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       className = g_relationships.descriptors(acmEntityIndex).relName
       classShortName = g_relationships.descriptors(acmEntityIndex).shortName
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       specificToOrgId = g_relationships.descriptors(acmEntityIndex).specificToOrgId
       specificToPool = g_relationships.descriptors(acmEntityIndex).specificToPool
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
   End If

   Dim thisOrgId As Integer
   Dim thisPoolId As Integer
   If thisOrgIndex > 0 Then thisOrgId = g_orgs.descriptors(thisOrgIndex).id Else thisOrgId = -1
   If thisPoolIndex > 0 Then thisPoolId = g_pools.descriptors(thisPoolIndex).id Else thisPoolId = -1

   Dim lrtUseSurogateKey As Boolean

     If Not (g_classes.descriptors(g_classIndexLrt).isCommonToOrgs Or Not isCommonToOrgs Or thisOrgId = specificToOrgId) Or _
        Not (g_classes.descriptors(g_classIndexLrt).isCommonToPools Or Not isCommonToPools Or thisPoolId = specificToPool) Then
       ' we cannot have a foreign key pointing from common to specific pool
       Exit Sub
     End If
     lrtUseSurogateKey = g_classes.descriptors(g_classIndexLrt).useSurrogateKey

   Dim fkName As String
   Dim lrtTabName As String
   Dim lrtTabNameLdm As String

   lrtTabName = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)
   lrtTabNameLdm = genQualTabNameByClassIndex(g_classIndexLrt, edtLdm)

 ' ### IF IVK ###
   Dim qualTabNameLdmStatus As String
   qualTabNameLdmStatus = genQualTabNameByEnumIndex(g_enumIndexStatus, edtLdm, thisOrgIndex, thisPoolIndex)
 
 ' ### ENDIF IVK ###
   ' Foreign Key on 'InLrt'
   fkName = genFkName(className, classShortName, "LRT", ddlType, thisOrgIndex, thisPoolIndex, forGen)
 ' ### IF IVK ###
   If generateDdlCreateFK And (forLrt Or Not condenseData) Then
 ' ### ELSE IVK ###
 ' If generateDdlCreateFK And forLrt Then
 ' ### ENDIF IVK ###
     printSectionHeader "Foreign Key to ""LRT"" Table", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "ALTER TABLE"
     Print #fileNo, addTab(1); qualTabName
     Print #fileNo, addTab(0); "ADD CONSTRAINT"
     Print #fileNo, addTab(1); fkName
     Print #fileNo, addTab(0); "FOREIGN KEY"
     Print #fileNo, addTab(1); "("; getFkSrcAttrSeqExt(g_classIndexLrt, "", thisPoolIndex, ddlType, g_anInLrt, isPsTagged); ")"
     Print #fileNo, addTab(0); "REFERENCES"

     If lrtUseSurogateKey Then
       If isPsTagged Then
         Print #fileNo, addTab(1); lrtTabName & " (" & getFkTargetAttrSeqExt(g_classIndexLrt, thisPoolIndex, ddlType, g_anOid) & ")"
       Else
         Print #fileNo, addTab(1); lrtTabName & " (" & g_anOid & ")"
       End If
     Else
       If isPsTagged Then
         Print #fileNo, addTab(1); lrtTabName & " (" & getFkTargetAttrSeqExt(g_classIndexLrt, thisPoolIndex, ddlType, g_anLrtOid) & ")"
       Else
         Print #fileNo, addTab(1); lrtTabName & " (" & g_anLrtOid & ")"
       End If
     End If

     Print #fileNo, gc_sqlCmdDelim
   End If

   registerQualLdmFk qualTabNameLdm, lrtTabNameLdm, acmEntityIndex, acmEntityType, , forGen

   ' Foreign Key on 'Status'
   fkName = genFkName(className, classShortName, "STA", ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)

 ' ### IF IVK ###
   If (ddlType = edtPdm) And generateDdlCreateFK And Not condenseData Then
     printSectionHeader "Foreign Key to ""Status""-Enumeration Table", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "ALTER TABLE"
     Print #fileNo, addTab(1); qualTabName
     Print #fileNo, addTab(0); "ADD CONSTRAINT"
     Print #fileNo, addTab(1); fkName
     Print #fileNo, addTab(0); "FOREIGN KEY"
     Print #fileNo, addTab(1); "("; genAttrName(enStatus, ddlType); "_ID)"
     Print #fileNo, addTab(0); "REFERENCES"
     Print #fileNo, addTab(1); g_qualTabNameStatus; " (ID)"

     Print #fileNo, gc_sqlCmdDelim
   End If

   registerQualLdmFk qualTabNameLdm, qualTabNameLdmStatus, acmEntityIndex, acmEntityType, , forGen
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
   If (ddlType = edtPdm) And generateIndexOnFkForLrtId And generateDdlCreateIndex And Not condenseData Then
 ' ### ELSE IVK ###
 ' If (ddlType = edtPdm) And generateIndexOnFkForLrtId And generateDdlCreateIndex Then
 ' ### ENDIF IVK ###
     Dim qualIndexName As String
     qualIndexName = genQualIndexName(sectionIndex, className & suffix, classShortName & suffix, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)

     If indexExcp(qualIndexName, thisOrgIndex) = False Then
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE INDEX"
       Print #fileNo, addTab(1); qualIndexName
       Print #fileNo, addTab(0); "ON"
       Print #fileNo, addTab(1); qualTabName
       Print #fileNo, addTab(0); "("
       Print #fileNo, addTab(1); g_anInLrt; " ASC"
       Print #fileNo, addTab(0); ")"
       Print #fileNo, gc_sqlCmdDelim
     End If ' indexExcp
   End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub evalAttributes()
   Dim i As Integer, j As Integer

   Dim relId As Integer
   relId = 100

     For i = 1 To g_attributes.numDescriptors Step 1
         ' determine Domains
         g_attributes.descriptors(i).domainIndex = -1
         g_attributes.descriptors(i).valueTypeIndex = -1

 ' ### IF IVK ###
         If g_attributes.descriptors(i).ftoConflictWith <> "" Then
           Dim elems() As String
           elems = split("", "/")
           elems = split(g_attributes.descriptors(i).ftoConflictWith, "/")

           If UBound(elems) = 0 Then
             g_attributes.descriptors(i).ftoConflictWithAttrIndex = getAttributeIndexByName(g_attributes.descriptors(i).sectionName, g_attributes.descriptors(i).ftoConflictWith)
             g_attributes.descriptors(i).ftoConflictType = -1
             g_attributes.descriptors(i).ftoConflictMessageIdBase = -1
           Else
             g_attributes.descriptors(i).ftoConflictWithAttrIndex = getAttributeIndexByName(g_attributes.descriptors(i).sectionName, elems(0))
             g_attributes.descriptors(i).ftoConflictType = getInteger(elems(1))
             If UBound(elems) < 2 Then
               g_attributes.descriptors(i).ftoConflictMessageIdBase = -1
             Else
               g_attributes.descriptors(i).ftoConflictMessageIdBase = getLong(elems(2))
             End If
           End If
           g_attributes.descriptors(g_attributes.descriptors(i).ftoConflictWithAttrIndex).ftoConflictWithSrcAttrIndex = i
         End If
 ' ### ENDIF IVK ###
         For j = 1 To g_enums.numDescriptors
           If UCase(g_attributes.descriptors(i).domainSection) = UCase(g_enums.descriptors(j).sectionName) And _
              UCase(g_attributes.descriptors(i).domainName) = UCase(g_enums.descriptors(j).enumName) Then
             If g_attributes.descriptors(i).domainSection <> g_enums.descriptors(j).sectionName Then
               logMsg "Inconsistent 'casing' for section name """ & g_attributes.descriptors(i).domainSection & """ used to define attribute """ & g_attributes.descriptors(i).attributeName & "@" & g_attributes.descriptors(i).sectionName & "." & g_attributes.descriptors(i).className & """", ellFixableWarning
             End If
             If g_attributes.descriptors(i).domainName <> g_enums.descriptors(j).enumName Then
               logMsg "Inconsistent 'casing' for enum name """ & g_attributes.descriptors(i).domainName & """ used to define attribute """ & g_attributes.descriptors(i).attributeName & "@" & g_attributes.descriptors(i).sectionName & "." & g_attributes.descriptors(i).className & """", ellFixableWarning
             End If
             g_attributes.descriptors(i).valueType = eavtEnum
             g_attributes.descriptors(i).valueTypeIndex = j
             Exit For
           End If
         Next j
         If g_attributes.descriptors(i).valueTypeIndex = -1 Then
           For j = 1 To g_domains.numDescriptors
             If UCase(g_attributes.descriptors(i).domainSection) = UCase(g_domains.descriptors(j).sectionName) And _
                UCase(g_attributes.descriptors(i).domainName) = UCase(g_domains.descriptors(j).domainName) Then
               If g_attributes.descriptors(i).domainSection <> g_domains.descriptors(j).sectionName Then
                 logMsg "Inconsistent 'casing' for section name """ & g_attributes.descriptors(i).domainSection & """ used to define attribute """ & g_attributes.descriptors(i).attributeName & "@" & g_attributes.descriptors(i).sectionName & "." & g_attributes.descriptors(i).className & """", ellFixableWarning
               End If
               If g_attributes.descriptors(i).domainName <> g_domains.descriptors(j).domainName Then
                 logMsg "Inconsistent 'casing' for domain name """ & g_attributes.descriptors(i).domainName & """ used to define attribute """ & g_attributes.descriptors(i).attributeName & "@" & g_attributes.descriptors(i).sectionName & "." & g_attributes.descriptors(i).className & """", ellFixableWarning
               End If
               g_attributes.descriptors(i).domainIndex = j
               g_attributes.descriptors(i).valueType = eavtDomain
               g_attributes.descriptors(i).valueTypeIndex = j

 ' ### IF IVK ###
               If g_attributes.descriptors(i).isExpression Then
                 g_attributes.descriptors(i).domainIndex = g_domainIndexOid
               End If
 ' ### ENDIF IVK ###

               Exit For
             End If
           Next j
           If g_attributes.descriptors(i).valueTypeIndex = -1 Then
             logMsg "Unknown domain """ & g_attributes.descriptors(i).domainSection & "." & g_attributes.descriptors(i).domainName & """ used to define attribute """ & g_attributes.descriptors(i).attributeName & "@" & g_attributes.descriptors(i).sectionName & "." & g_attributes.descriptors(i).className & """", ellError
           End If
         End If

         'was not supported in the past - now allowed for certain cases (one nullable column per unique index)
         'If .isNullable And .isIdentifying Then
         '  logMsg "Attribute """ & .attributeName & "@" & .sectionName & "." & .className & """ is marked as ""identifying"" and ""nullable""", ellError
         'End If

 ' ### IF IVK ###
         Dim classIndex As Integer
         classIndex = -1
         If g_attributes.descriptors(i).cType = eactClass Then
           classIndex = getClassIndexByName(g_attributes.descriptors(i).sectionName, g_attributes.descriptors(i).className, True)

           If g_attributes.descriptors(i).attributeName = conIsNotPublished Then
             g_classes.descriptors(classIndex).containsIsNotPublished = True
           End If
         End If

         If g_attributes.descriptors(i).groupIdBasedOn <> "" And classIndex > 0 Then
           addGroupIdAttrIndex classIndex, i
           g_classes.descriptors(classIndex).hasGroupIdAttrInNonGen = True
         End If

         ' analyze virtual attributes
         If g_attributes.descriptors(i).isVirtual Then
         ' we currently do not fully support expression-based mapping
           If Left(g_attributes.descriptors(i).virtuallyMapsTo.description, 1) = "#" Then
             If g_attributes.descriptors(i).cType = eactClass Then
               If g_attributes.descriptors(i).isTimeVarying And (Not g_classes.descriptors(classIndex).hasNoIdentity) Then
                 g_classes.descriptors(classIndex).hasExpBasedVirtualAttrInGen = True
               Else
                 g_classes.descriptors(classIndex).hasExpBasedVirtualAttrInNonGen = True
               End If
             End If
           Else
             If g_attributes.descriptors(i).cType = eactClass Then
               If g_attributes.descriptors(i).isTimeVarying And (Not g_classes.descriptors(classIndex).hasNoIdentity) Then
                 g_classes.descriptors(classIndex).hasRelBasedVirtualAttrInGen = True
               Else
                 g_classes.descriptors(classIndex).hasRelBasedVirtualAttrInNonGen = True
               End If
             End If
           End If
         End If

         ' handle attributes marked as 'MDS expression'
         If g_attributes.descriptors(i).isExpression Then
           If classIndex <= 0 Then
             classIndex = getClassIndexByName(g_attributes.descriptors(i).sectionName, g_attributes.descriptors(i).className)
           End If

           If classIndex > 0 Then
             Dim class As ClassDescriptor
             class = g_classes.descriptors(classIndex) ' just to shorten the following code
             relId = getMaxRelIdBySection(class.sectionName) + 1

             Dim relIndex As Integer
             relIndex = allocRelationshipDescriptorIndex(g_relationships)

               g_relationships.descriptors(relIndex).i18nId = "R-" & class.sectionName & "-EXP-" & g_attributes.descriptors(i).attributeName

               g_relationships.descriptors(relIndex).sectionName = class.sectionName
               g_relationships.descriptors(relIndex).relName = class.className & g_attributes.descriptors(i).attributeName
               g_relationships.descriptors(relIndex).relId = relId
               g_relationships.descriptors(relIndex).shortName = g_attributes.descriptors(i).shortName
               g_relationships.descriptors(relIndex).reuseName = g_attributes.descriptors(i).attributeName
               g_relationships.descriptors(relIndex).reuseShortName = g_attributes.descriptors(i).shortName
               g_relationships.descriptors(relIndex).isCommonToOrgs = class.isCommonToOrgs
               g_relationships.descriptors(relIndex).specificToOrgId = class.specificToOrgId
               g_relationships.descriptors(relIndex).isCommonToPools = class.isCommonToPools
               g_relationships.descriptors(relIndex).specificToPool = class.specificToPool
               g_relationships.descriptors(relIndex).useValueCompression = class.useValueCompression
               g_relationships.descriptors(relIndex).useVersiontag = class.useVersiontag
               g_relationships.descriptors(relIndex).notAcmRelated = class.notAcmRelated
               g_relationships.descriptors(relIndex).isLrtSpecific = class.isLrtSpecific
               g_relationships.descriptors(relIndex).isPdmSpecific = class.isPdmSpecific
               g_relationships.descriptors(relIndex).isNotEnforced = False
               g_relationships.descriptors(relIndex).isNl = False
               g_relationships.descriptors(relIndex).leftClassSectionName = class.sectionName
               g_relationships.descriptors(relIndex).leftClassName = class.className
               g_relationships.descriptors(relIndex).leftTargetType = erttRegular
               g_relationships.descriptors(relIndex).lrRelName = g_attributes.descriptors(i).attributeName & "Expression"
               g_relationships.descriptors(relIndex).lrShortRelName = "EXP"
               g_relationships.descriptors(relIndex).lrLdmRelName = g_relationships.descriptors(relIndex).lrRelName
               g_relationships.descriptors(relIndex).minLeftCardinality = 0
               g_relationships.descriptors(relIndex).maxLeftCardinality = -1
               g_relationships.descriptors(relIndex).isIdentifyingLeft = False
               g_relationships.descriptors(relIndex).useIndexOnLeftFk = generateIndexOnExpressionFks
               g_relationships.descriptors(relIndex).ignoreForChangelog = True
               g_relationships.descriptors(relIndex).rightClassSectionName = g_classes.descriptors(g_classIndexExpression).sectionName
               g_relationships.descriptors(relIndex).rightClassName = g_classes.descriptors(g_classIndexExpression).className
               g_relationships.descriptors(relIndex).rightTargetType = erttRegular
               g_relationships.descriptors(relIndex).isMdsExpressionRel = True
               g_relationships.descriptors(relIndex).rlRelName = class.className
               g_relationships.descriptors(relIndex).rlShortRelName = class.shortName
               g_relationships.descriptors(relIndex).rlLdmRelName = g_relationships.descriptors(relIndex).rlRelName
               g_relationships.descriptors(relIndex).minRightCardinality = 0
               g_relationships.descriptors(relIndex).maxRightCardinality = 1
               g_relationships.descriptors(relIndex).isIdentifyingRight = False
               g_relationships.descriptors(relIndex).useIndexOnRightFk = generateIndexOnExpressionFks
               g_relationships.descriptors(relIndex).isNationalizable = g_attributes.descriptors(i).isNationalizable
               g_relationships.descriptors(relIndex).isPsForming = class.isPsForming
               g_relationships.descriptors(relIndex).logLastChange = class.logLastChange
               g_relationships.descriptors(relIndex).isUserTransactional = class.isUserTransactional
               g_relationships.descriptors(relIndex).logLastChangeInView = class.logLastChangeInView
               g_relationships.descriptors(relIndex).isSubjectToArchiving = class.isSubjectToArchiving
               g_relationships.descriptors(relIndex).noTransferToProduction = class.noTransferToProduction
               g_relationships.descriptors(relIndex).noFto = class.noFto
               g_relationships.descriptors(relIndex).tabSpaceData = class.tabSpaceData
               g_relationships.descriptors(relIndex).tabSpaceLong = class.tabSpaceLong
               g_relationships.descriptors(relIndex).tabSpaceNl = class.tabSpaceNl
               g_relationships.descriptors(relIndex).tabSpaceIndex = class.tabSpaceIndex
               g_relationships.descriptors(relIndex).isTimeVarying = g_attributes.descriptors(i).isTimeVarying

 
               If g_attributes.descriptors(i).attrNlIndex > 0 Then
                 Dim relNlIndex As Integer
                 relNlIndex = allocRelationshipNlDescriptorIndex(g_relationshipsNl)

                   g_relationshipsNl.descriptors(relNlIndex).i18nId = g_relationships.descriptors(relIndex).i18nId
                   g_relationshipsNl.descriptors(relNlIndex).relationshipIndex = relIndex

                   ReDim nl(1 To numLangsForRelationshipsNl)
                   For j = 1 To numLangsForRelationshipsNl
                     g_relationshipsNl.descriptors(relNlIndex).nl(j) = g_attributesNl.descriptors(g_attributes.descriptors(i).attrNlIndex).nl(j)
                   Next j
               End If
           End If
         End If
 ' ### ENDIF IVK ###

         ' verify that 'attribute container' is defined
         If g_attributes.descriptors(i).cType = eactClass Then
           If getClassIndexByName(g_attributes.descriptors(i).sectionName, g_attributes.descriptors(i).className, True) <= 0 Then
             logMsg "Class """ & g_attributes.descriptors(i).sectionName & "." & g_attributes.descriptors(i).className & """ holding attribute """ & g_attributes.descriptors(i).attributeName & """ not known", ellError
           End If
         ElseIf g_attributes.descriptors(i).cType = eactRelationship Then
           If getRelIndexByName(g_attributes.descriptors(i).sectionName, g_attributes.descriptors(i).className, True) <= 0 Then
             logMsg "Relationship """ & g_attributes.descriptors(i).sectionName & "." & g_attributes.descriptors(i).className & """ holding attribute """ & g_attributes.descriptors(i).attributeName & """ not known", ellError
           End If
         ElseIf g_attributes.descriptors(i).cType = eactEnum Then
           If getEnumIndexByName(g_attributes.descriptors(i).sectionName, g_attributes.descriptors(i).className, True) <= 0 Then
             logMsg "Enumeration """ & g_attributes.descriptors(i).sectionName & "." & g_attributes.descriptors(i).className & """ holding attribute """ & g_attributes.descriptors(i).attributeName & """ not known", ellError
           End If
 ' ### IF IVK ###
         ElseIf g_attributes.descriptors(i).cType = eactType Then
           If getTypeIndexByName(g_attributes.descriptors(i).sectionName, g_attributes.descriptors(i).className, True) <= 0 Then
             logMsg "type """ & g_attributes.descriptors(i).sectionName & "." & g_attributes.descriptors(i).className & """ holding attribute """ & g_attributes.descriptors(i).attributeName & """ not known", ellError
           End If
 ' ### ENDIF IVK ###
         End If

 ' ### IF IVK ###
         ' analyze group-ID columns
         If g_attributes.descriptors(i).groupIdBasedOn <> "" Then
           For j = LBound(g_attributes.descriptors(i).groupIdAttributes) To UBound(g_attributes.descriptors(i).groupIdAttributes)
             If getAttributeIndexByNameAndEntityIndexRaw(g_attributes.descriptors(i).groupIdAttributes(j), g_attributes.descriptors(i).cType, g_attributes.descriptors(i).acmEntityIndex, True) Then
 ' todo
             End If
           Next j
         End If
 ' ### ENDIF IVK ###

         ' determine DB-column names
         Dim thisDdlType As DdlTypeId
         For thisDdlType = edtPdm To edtLdm
           g_attributes.descriptors(i).dbColName(thisDdlType) = genAttrName(g_attributes.descriptors(i).attributeName & IIf(g_attributes.descriptors(i).valueType = eavtEnum, gc_enumAttrNameSuffix, ""), thisDdlType)
         Next thisDdlType
     Next i
 End Sub
 
 
 Sub evalAttributes2()
   Dim i As Integer
   Dim j As Integer
   Dim relIndex As Integer
   Dim relNavDirection As RelNavigationDirection
   Dim classIndex As Integer
   Dim elems() As String
   Dim mapRelName As String
   Dim mapAttrName As String
   Dim referToAttrIndex As Integer
   Dim referToClassIndex As Integer
   Dim thisClassIndex As Integer

   For i = 1 To g_attributes.numDescriptors
       If g_attributes.descriptors(i).acmEntityIndex <= 0 Then
         GoTo NextI
       End If

       If g_attributes.descriptors(i).valueType = eavtEnum Then
         g_attributes.descriptors(i).domainIndex = g_enums.descriptors(g_attributes.descriptors(i).valueTypeIndex).domainIndexId
       End If

       g_attributes.descriptors(i).compressDefault = False
       If dbCompressSystemDefaults And g_attributes.descriptors(i).domainIndex > 0 Then
         g_attributes.descriptors(i).compressDefault = g_domains.descriptors(g_attributes.descriptors(i).domainIndex).dataType <> etTimestamp And _
                            g_domains.descriptors(g_attributes.descriptors(i).domainIndex).dataType <> etTime And _
                            g_domains.descriptors(g_attributes.descriptors(i).domainIndex).dataType <> etDate
       End If

       If g_attributes.descriptors(i).isIdentifying Then
         If g_attributes.descriptors(i).cType = eactClass Then
           g_attributes.descriptors(i).isPrimaryKey = g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).useSurrogateKey
           g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).hasBusinessKey = True
         ElseIf g_attributes.descriptors(i).cType = eactRelationship Then
           g_attributes.descriptors(i).isPrimaryKey = Not useSurrogateKeysForNMRelationships
           g_relationships.descriptors(g_attributes.descriptors(i).acmEntityIndex).hasBusinessKey = True
         End If
       End If

       If g_attributes.descriptors(i).cType <> eactClass Then
         GoTo NextI
       End If
 ' ### IF IVK ###

       If g_attributes.descriptors(i).isExpression Then
         thisClassIndex = g_attributes.descriptors(i).acmEntityIndex
         If g_attributes.descriptors(i).isTimeVarying And Not g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).hasNoIdentity Then
           While thisClassIndex > 0
             g_classes.descriptors(thisClassIndex).hasExpressionInGen = True
             thisClassIndex = g_classes.descriptors(thisClassIndex).superClassIndex
           Wend
         Else
           While thisClassIndex > 0
             g_classes.descriptors(thisClassIndex).hasExpressionInNonGen = True
             thisClassIndex = g_classes.descriptors(thisClassIndex).superClassIndex
           Wend
         End If
       End If

       If Not g_attributes.descriptors(i).isVirtual Then
         GoTo NextI
       End If

         If Left(g_attributes.descriptors(i).virtuallyMapsTo.description, 1) = "#" Then
           g_attributes.descriptors(i).virtuallyMapsTo.isRelBasedMapping = False
           g_attributes.descriptors(i).virtuallyMapsTo.mapTo = Right(g_attributes.descriptors(i).virtuallyMapsTo.description, Len(g_attributes.descriptors(i).virtuallyMapsTo.description) - 1)
             If Left(g_attributes.descriptors(i).virtuallyMapsToForRead.description, 1) = "#" Then
               g_attributes.descriptors(i).virtuallyMapsToForRead.isRelBasedMapping = False
               g_attributes.descriptors(i).virtuallyMapsToForRead.mapTo = Right(g_attributes.descriptors(i).virtuallyMapsToForRead.description, Len(g_attributes.descriptors(i).virtuallyMapsToForRead.description) - 1)
             End If
           GoTo NextI
         End If

       classIndex = g_attributes.descriptors(i).acmEntityIndex

       elems = split("", "/")
       elems = split(g_attributes.descriptors(i).virtuallyMapsTo.description, "/")

       If UBound(elems) <> 1 Then
         MsgBox "provide some error message here"
         GoTo NextI
       End If
       g_attributes.descriptors(i).virtuallyMapsTo.isRelBasedMapping = True

         mapRelName = Trim(elems(0))
         mapAttrName = Trim(elems(1))

         referToClassIndex = 0
         For j = 1 To g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).relRefs.numRefs
           relIndex = g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).relRefs.refs(j).refIndex
           relNavDirection = g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).relRefs.refs(j).refType

             If relNavDirection = etLeft And g_relationships.descriptors(relIndex).maxLeftCardinality < 0 And g_relationships.descriptors(relIndex).maxRightCardinality = 1 And _
                g_relationships.descriptors(relIndex).lrRelName = mapRelName Then
               g_attributes.descriptors(i).virtuallyMapsTo.mapTo = mapAttrName
               g_attributes.descriptors(i).virtuallyMapsTo.navDirection = etLeft
               g_attributes.descriptors(i).virtuallyMapsTo.relIndex = relIndex
               g_attributes.descriptors(i).virtuallyMapsTo.targetClassIndex = g_relationships.descriptors(relIndex).rightEntityIndex
               referToClassIndex = g_relationships.descriptors(relIndex).rightEntityIndex
               Exit For
             ElseIf relNavDirection = etRight And g_relationships.descriptors(relIndex).maxRightCardinality < 0 And g_relationships.descriptors(relIndex).maxLeftCardinality = 1 And _
                g_relationships.descriptors(relIndex).rlRelName = mapRelName Then
               g_attributes.descriptors(i).virtuallyMapsTo.mapTo = mapAttrName
               g_attributes.descriptors(i).virtuallyMapsTo.navDirection = etRight
               g_attributes.descriptors(i).virtuallyMapsTo.relIndex = relIndex
               g_attributes.descriptors(i).virtuallyMapsTo.targetClassIndex = g_relationships.descriptors(relIndex).leftEntityIndex
               referToClassIndex = g_relationships.descriptors(relIndex).leftEntityIndex
               Exit For
             End If
         Next j
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 
     If referToClassIndex > 0 Then
       referToAttrIndex = getAttributeIndexByNameAndEntityIndexRaw(mapAttrName, eactClass, referToClassIndex)

       addVirtuallyReferingAttr referToAttrIndex, i
     End If
 ' ### ENDIF IVK ###
 NextI:
   Next i
 
   For i = 1 To g_attributes.numDescriptors
 ' ### IF IVK ###
       If UCase(g_attributes.descriptors(i).attributeName) = UCase(conIsNational) Then
         If g_attributes.descriptors(i).cType = eactClass Then
           thisClassIndex = g_attributes.descriptors(i).acmEntityIndex
           While thisClassIndex > 0
             g_classes.descriptors(thisClassIndex).hasIsNationalInclSubClasses = True
             thisClassIndex = g_classes.descriptors(thisClassIndex).superClassIndex
           Wend
         ElseIf g_attributes.descriptors(i).cType = eactRelationship Then
           g_relationships.descriptors(g_attributes.descriptors(i).acmEntityIndex).hasIsNationalInclSubClasses = True
         End If
       End If

 ' ### ENDIF IVK ###
       If UCase(g_attributes.descriptors(i).attributeName) = "LABEL" And g_attributes.descriptors(i).isNl Then
         If g_attributes.descriptors(i).cType = eactClass Then
 ' ### IF IVK ###
           If g_attributes.descriptors(i).isTimeVarying And Not g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).hasNoIdentity Then
 ' ### ELSE IVK ###
 '         If .isTimeVarying Then
 ' ### ENDIF IVK ###
             g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).hasLabelInGen = True
           Else
             g_classes.descriptors(g_attributes.descriptors(i).acmEntityIndex).hasLabel = True
           End If
         ElseIf g_attributes.descriptors(i).cType = eactRelationship Then
           g_relationships.descriptors(g_attributes.descriptors(i).acmEntityIndex).hasLabel = True
         End If
       End If
   Next i
 End Sub

 
 Sub dropAttributeCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   killCsvFileWhereEver g_sectionIndexDbMeta, clnAcmAttribute, g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM"
 End Sub
 
 
 Private Sub printAttrCsvLine( _
   fileNo As Integer, _
   ByRef attributeName As String, _
   ByRef dbColName As String, _
   ByRef i18nId As String, _
   ByRef domainSection As String, _
   ByRef domain As String, _
   ByRef attrSeqNo As Integer, _
   ByRef sectionName As String, _
   ByRef className As String, _
   cType As AcmAttrContainerType, _
   Optional isNl As Boolean = False, _
   Optional isTimeVarying As Boolean = False, _
   Optional isBusinessKey As Boolean = False, _
   Optional isPrimaryKey As Boolean = False, _
   Optional isTechnical As Boolean = False, _
   Optional isNullable As Boolean = True, _
   Optional isVirtual As Boolean = False, _
   Optional isVInstantiated As Boolean = False, _
   Optional isGroupId As Boolean = False, _
   Optional isExpression As Boolean = False, _
   Optional isInstantiated As Boolean = True _
 )
   Print #fileNo, """"; UCase(attributeName); """,";
   Print #fileNo, """"; UCase(dbColName); """,";
   Print #fileNo, IIf(i18nId = "", "", """" & UCase(i18nId) & """"); ",";
   Print #fileNo, IIf(isNl, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNo, IIf(isTimeVarying, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNo, IIf(isBusinessKey, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNo, IIf(isPrimaryKey, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNo, IIf(isTechnical, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNo, IIf(isNullable, gc_dbTrue, gc_dbFalse); ",";
 ' ### IF IVK ###
   Print #fileNo, IIf(isVirtual, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNo, IIf(isVInstantiated, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNo, IIf(isGroupId, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNo, IIf(isExpression, gc_dbTrue, gc_dbFalse); ",";
   If supportColumnIsInstantiatedInAcmAttribute Then
     Print #fileNo, IIf(isInstantiated, gc_dbTrue, gc_dbFalse); ",";
   End If
 ' ### ELSE IVK ###
 ' Print #fileNo, IIf(isInstantiated, gc_dbTrue, gc_dbFalse); ",";
 ' ### ENDIF IVK ###
   Print #fileNo, """"; UCase(domainSection); """,";
   Print #fileNo, """"; UCase(domain); """,";
   Print #fileNo, CStr(attrSeqNo); ",";
   Print #fileNo, """"; UCase(sectionName); """,";
   Print #fileNo, """"; UCase(className); """,";
   Print #fileNo, """"; getAcmEntityTypeKey(cType); """,";
   Print #fileNo, getCsvTrailer(0)
 End Sub
 
 
 Sub genAttributeAcmMetaCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnAcmAttribute, acmCsvProcessingStep, "ACM", ddlType)
   assertDir fileName
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim lastEntityName As String
   Dim lastEntitySection As String
   Dim lastEntityType As AcmAttrContainerType
   Dim attrSeqNo As Integer
   Dim i As Integer, j As Integer
   Dim skip As Boolean
   Dim isReused As Boolean

   For i = 1 To g_attributes.numDescriptors
       g_attributes.descriptors(i).attrIndex = i

       If Not g_attributes.descriptors(i).isNotAcmRelated Then
         If lastEntityName <> g_attributes.descriptors(i).className Or _
            lastEntitySection <> g_attributes.descriptors(i).sectionName Or _
            lastEntityType <> g_attributes.descriptors(i).cType Then
           lastEntityName = g_attributes.descriptors(i).className
           lastEntitySection = g_attributes.descriptors(i).sectionName
           lastEntityType = g_attributes.descriptors(i).cType
           attrSeqNo = 1
         End If

         isReused = reuseColumnsInTabsForOrMapping And g_attributes.descriptors(i).reusedAttrIndex > 0
 ' ### IF IVK ###
         skip = (g_attributes.descriptors(i).cType = eactType) Or g_attributes.descriptors(i).isNotAcmRelated
 ' ### ELSE IVK ###
 '       skip = .isNotAcmRelated
 ' ### ENDIF IVK ###
         If g_attributes.descriptors(i).isPdmSpecific And (ddlType <> edtPdm) Then
           skip = True
         End If

         If Not skip Then
 
 ' ### IF IVK ###
           printAttrCsvLine fileNo, g_attributes.descriptors(i).attributeName, g_attributes.descriptors(i).dbColName(ddlType), g_attributes.descriptors(i).i18nId, g_attributes.descriptors(i).domainSection, g_attributes.descriptors(i).domainName, attrSeqNo, g_attributes.descriptors(i).sectionName, _
                                    g_attributes.descriptors(i).className, g_attributes.descriptors(i).cType, g_attributes.descriptors(i).isNl, g_attributes.descriptors(i).isTimeVarying, g_attributes.descriptors(i).isIdentifying, g_attributes.descriptors(i).isPrimaryKey, , g_attributes.descriptors(i).isNullable, _
                                    g_attributes.descriptors(i).isVirtual, g_attributes.descriptors(i).virtuallyMapsTo.isInstantiated, g_attributes.descriptors(i).isGroupId, , Not isReused
 ' ### ELSE IVK ###
 '         printAttrCsvLine fileNo, .attributeName, .dbColName(ddlType), .i18nId, .domainSection, .domain, attrSeqNo, .sectionName, _
 '                                  .className, .cType, .isNl, .isTimeVarying, .isIdentifying, .isPrimaryKey, , .isNullable, Not isReused
 ' ### ENDIF IVK ###
           attrSeqNo = attrSeqNo + 1

 ' ### IF IVK ###
           If g_attributes.descriptors(i).isNationalizable Then
             printAttrCsvLine fileNo, genAttrName(g_attributes.descriptors(i).attributeName, ddlType, , , , , True, False), genAttrName(g_attributes.descriptors(i).attributeName, ddlType, , , , g_attributes.descriptors(i).valueType, True), g_attributes.descriptors(i).i18nId & "-" & gc_asnSuffixNat, g_attributes.descriptors(i).domainSection, g_attributes.descriptors(i).domainName, attrSeqNo, g_attributes.descriptors(i).sectionName, _
                                      g_attributes.descriptors(i).className, g_attributes.descriptors(i).cType, g_attributes.descriptors(i).isNl, g_attributes.descriptors(i).isTimeVarying, False, False, , True, _
                                      g_attributes.descriptors(i).isVirtual, g_attributes.descriptors(i).virtuallyMapsTo.isInstantiated, g_attributes.descriptors(i).isGroupId, , Not isReused
             attrSeqNo = attrSeqNo + 1
             printAttrCsvLine fileNo, genAttrName(g_attributes.descriptors(i).attributeName & gc_anSuffixNatActivated, ddlType, , , , , , False), _
                                      genAttrName(g_attributes.descriptors(i).attributeName & gc_anSuffixNatActivated, ddlType), g_attributes.descriptors(i).i18nId & "-" & gc_asnSuffixNatActivated, dxnBoolean, dnBoolean, attrSeqNo, g_attributes.descriptors(i).sectionName, _
                                      g_attributes.descriptors(i).className, g_attributes.descriptors(i).cType, , , , , , , , , , , Not isReused
             attrSeqNo = attrSeqNo + 1
           End If
 ' ### ENDIF IVK ###
         End If
       End If
   Next i
 
   For i = 1 To g_classes.numDescriptors
       If Not g_classes.descriptors(i).notAcmRelated And g_classes.descriptors(i).superClassIndex <= 0 Then
         ' surrogate key
         If g_classes.descriptors(i).useSurrogateKey Then
           printAttrCsvLine fileNo, conOid, g_anOid, "", dxnOid, dnOid, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, True, True, False
         End If
         ' classId
         If Not g_classes.descriptors(i).hasOwnTable Then
           printAttrCsvLine fileNo, conClassId, g_anCid, "", dxnClassId, dnClassId, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
         End If
         ' aggregate head: classId and objectId
         If g_classes.descriptors(i).aggHeadClassIndex > 0 Then
           printAttrCsvLine fileNo, conAhClassId, g_anAhCid, "", dxnClassId, dnClassId, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
           printAttrCsvLine fileNo, conAhOId, g_anAhOid, "", dxnOid, dnOid, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
         End If
         ' validFrom / validTo
         If g_classes.descriptors(i).isGenForming Then
           printAttrCsvLine fileNo, conValidFrom, g_anValidFrom, "", dxnValTimestamp, dnValTimestamp, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
           printAttrCsvLine fileNo, conValidTo, g_anValidTo, "", dxnValTimestamp, dnValTimestamp, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
         End If
         If g_classes.descriptors(i).logLastChange Then
           printAttrCsvLine fileNo, conCreateTimestamp, g_anCreateTimestamp, "", dxnModTimestamp, dnModTimestamp, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
           printAttrCsvLine fileNo, conCreateUser, g_anCreateUser, "", dxnUserId, dnUserId, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
           printAttrCsvLine fileNo, conLastUpdateTimestamp, g_anLastUpdateTimestamp, "", dxnModTimestamp, dnModTimestamp, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
           printAttrCsvLine fileNo, conUpdateUser, g_anUpdateUser, "", dxnUserId, dnUserId, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
         End If
 ' ### IF IVK ###
         ' isNational
         If g_classes.descriptors(i).isNationalizable Then
           printAttrCsvLine fileNo, conIsNational, g_anIsNational, "", dxnBoolean, dnBoolean, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
         End If
         ' hasBeenSetProductive-tag
         If g_classes.descriptors(i).isUserTransactional Then
           printAttrCsvLine fileNo, conHasBeenSetProductive, g_anHasBeenSetProductive, "", dxnBoolean, dnBoolean, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
         End If
         ' PS-tag
         If g_classes.descriptors(i).isPsTagged Then
           printAttrCsvLine fileNo, conPsOid, g_anPsOid, "", dxnOid, dnOid, 1000, g_classes.descriptors(i).sectionName, g_classes.descriptors(i).className, eactClass, _
                                    False, False, False, False, True, False
         End If
 ' ### ENDIF IVK ###
       End If
   Next i

   Dim isExpressionRel As Boolean
   Dim isTimeVaryingRel As Boolean
   For i = 1 To g_relationships.numDescriptors
       isExpressionRel = g_relationships.descriptors(i).isMdsExpressionRel
       isTimeVaryingRel = g_relationships.descriptors(i).isTimeVarying

       skip = g_relationships.descriptors(i).notAcmRelated Or (g_relationships.descriptors(i).isPdmSpecific And (ddlType <> edtPdm))

       If Not skip Then
         If g_relationships.descriptors(i).implementsInOwnTable Then
           If useSurrogateKeysForNMRelationships Then
             printAttrCsvLine fileNo, conOid, g_anOid, "", dxnOid, dnOid, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
                                      False, False, False, True, True, False
           End If

           ' aggregate head: classId and objectId
           If g_relationships.descriptors(i).aggHeadClassIndex > 0 Then
             printAttrCsvLine fileNo, conAhClassId, g_anAhCid, "", dxnClassId, dnClassId, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
                                      False, False, False, False, True, False
             printAttrCsvLine fileNo, conAhOId, g_anAhOid, "", dxnOid, dnOid, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
                                      False, False, False, False, True, False
           End If
           ' createTimestamp, LastUpdateTimestamp, etc
           If g_relationships.descriptors(i).logLastChange Then
             printAttrCsvLine fileNo, conCreateTimestamp, g_anCreateTimestamp, "", dxnModTimestamp, dnModTimestamp, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
                                      False, False, False, False, True, False
             printAttrCsvLine fileNo, conCreateUser, g_anCreateUser, "", dxnUserId, dnUserId, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
                                      False, False, False, False, True, False
             printAttrCsvLine fileNo, conLastUpdateTimestamp, g_anLastUpdateTimestamp, "", dxnModTimestamp, dnModTimestamp, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
                                      False, False, False, False, True, False
             printAttrCsvLine fileNo, conUpdateUser, g_anUpdateUser, "", dxnUserId, dnUserId, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
                                      False, False, False, False, True, False
           End If
 ' ### IF IVK ###
           ' hasBeenSetProductive-tag
           If g_relationships.descriptors(i).isUserTransactional Then
             printAttrCsvLine fileNo, conHasBeenSetProductive, g_anHasBeenSetProductive, "", dxnBoolean, dnBoolean, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
                                      False, False, False, False, True, False
           End If
           ' PS-tag
           If g_relationships.descriptors(i).isPsTagged Then
             printAttrCsvLine fileNo, conPsOid, g_anPsOid, "", dxnOid, dnOid, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
                                      False, False, False, False, True, False
           End If
 ' ### ENDIF IVK ###

           printAttrCsvLine fileNo, _
             g_relationships.descriptors(i).leftFkColName(ddlType), g_relationships.descriptors(i).leftFkColName(ddlType), "", dxnOid, dnOid, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
             False, False, False, False, True, False

           printAttrCsvLine fileNo, _
             g_relationships.descriptors(i).rightFkColName(ddlType), g_relationships.descriptors(i).rightFkColName(ddlType), "", dxnOid, dnOid, 1000, g_relationships.descriptors(i).sectionName, g_relationships.descriptors(i).relName, eactRelationship, _
             False, False, False, False, True, False
         Else ' not g_relationships.descriptors(i).implementsInOwnTable
           Dim entityIdImplementingFk As Integer
           Dim entityTypeImplementingFk As AcmAttrContainerType

           If g_relationships.descriptors(i).implementsInEntity = ernmLeft Then
             entityIdImplementingFk = g_relationships.descriptors(i).leftEntityIndex
             entityTypeImplementingFk = g_relationships.descriptors(i).leftEntityType
           Else
             entityIdImplementingFk = g_relationships.descriptors(i).rightEntityIndex
             entityTypeImplementingFk = g_relationships.descriptors(i).rightEntityType
           End If
           isReused = False

           If entityIdImplementingFk > 0 Then
             If g_relationships.descriptors(i).reusedRelIndex > 0 Then
               If g_relationships.descriptors(i).implementsInEntity = ernmLeft Then
                 If g_relationships.descriptors(i).leftEntityType = eactClass Then
                   If g_classes.descriptors(g_relationships.descriptors(i).leftEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(g_relationships.descriptors(g_relationships.descriptors(i).reusedRelIndex).leftEntityIndex).orMappingSuperClassIndex Then
                     isReused = True
                   End If
                 ElseIf g_relationships.descriptors(i).leftEntityIndex = g_relationships.descriptors(g_relationships.descriptors(i).reusedRelIndex).leftEntityIndex Then
                   isReused = True
                 End If
               ElseIf g_relationships.descriptors(i).implementsInEntity = ernmRight Then
                 If g_relationships.descriptors(i).rightEntityType = eactClass Then
                   If g_classes.descriptors(g_relationships.descriptors(i).rightEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(g_relationships.descriptors(g_relationships.descriptors(i).reusedRelIndex).rightEntityIndex).orMappingSuperClassIndex Then
                     isReused = True
                   End If
                 ElseIf g_relationships.descriptors(i).rightEntityIndex = g_relationships.descriptors(g_relationships.descriptors(i).reusedRelIndex).rightEntityIndex Then
                   isReused = True
                 End If
               End If
             End If

             If (supportColumnIsInstantiatedInAcmAttribute Or Not isReused) And Not g_relationships.descriptors(i).isReusedInSameEntity Then
               Dim fkColName As String
               fkColName = IIf(g_relationships.descriptors(i).implementsInEntity = ernmLeft, g_relationships.descriptors(i).rightFkColName(ddlType), g_relationships.descriptors(i).leftFkColName(ddlType))

               If entityTypeImplementingFk = eactClass Then
 ' ### IF IVK ###
                   printAttrCsvLine fileNo, _
                     fkColName, fkColName, "", dxnOid, dnOid, 1000, g_classes.descriptors(entityIdImplementingFk).sectionName, g_classes.descriptors(entityIdImplementingFk).className, eactClass, _
                     False, isTimeVaryingRel, False, False, True, False, , , , isExpressionRel, Not isReused
 ' ### ELSE IVK ###
 '                 printAttrCsvLine fileNo, _
 '                   fkColName, fkColName, "", dxnOid, dnOid, 1000, .sectionName, .className, eactClass, _
 '                   False, False, False, False, True, False, , Not isReused
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
                   If g_relationships.descriptors(i).isNationalizable Then
                     printAttrCsvLine fileNo, _
                       genAttrName(fkColName & gc_anSuffixNat, ddlType, , , , , , False), genAttrName(fkColName, ddlType, , , , , True), "", dxnOid, dnOid, 1000, g_classes.descriptors(entityIdImplementingFk).sectionName, g_classes.descriptors(entityIdImplementingFk).className, eactClass, _
                       False, isTimeVaryingRel, False, False, True, False, , , , isExpressionRel, Not isReused
                     printAttrCsvLine fileNo, _
                       genAttrName(fkColName & "_ISNATACTIVE", ddlType, , , , , , False), genAttrName(fkColName & "_ISNATACTIVE", ddlType), "", dxnOid, dnOid, 1000, g_classes.descriptors(entityIdImplementingFk).sectionName, g_classes.descriptors(entityIdImplementingFk).className, eactClass, _
                       False, isTimeVaryingRel, False, False, True, False, , , , isExpressionRel, Not isReused
                   End If
 ' ### ENDIF IVK ###
               ElseIf entityTypeImplementingFk = eactRelationship Then
 ' ### IF IVK ###
                   printAttrCsvLine fileNo, _
                     fkColName, fkColName, "", dxnOid, dnOid, 1000, g_relationships.descriptors(entityIdImplementingFk).sectionName, g_relationships.descriptors(entityIdImplementingFk).relName, eactRelationship, _
                     False, False, False, False, True, False, , , , isExpressionRel, Not isReused
 ' ### ELSE IVK ###
 '                 printAttrCsvLine fileNo, _
 '                   fkColName, fkColName, "", dxnOid, dnOid, 1000, .sectionName, .relName, eactRelationship, _
 '                   False, False, False, False, True, False, , Not isReused
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
                   If g_relationships.descriptors(i).isNationalizable Then
                     printAttrCsvLine fileNo, _
                       genAttrName(fkColName & gc_anSuffixNat, ddlType, , , , , , False), genAttrName(fkColName, ddlType, , , , , True), "", dxnOid, dnOid, 1000, g_relationships.descriptors(entityIdImplementingFk).sectionName, g_relationships.descriptors(entityIdImplementingFk).relName, eactRelationship, _
                       False, False, False, False, True, False, , , , isExpressionRel, Not isReused
                     printAttrCsvLine fileNo, _
                       genAttrName(fkColName & "_ISNATACTIVE", ddlType, , , , , , False), genAttrName(fkColName & "_ISNATACTIVE", ddlType), "", dxnOid, dnOid, 1000, g_relationships.descriptors(entityIdImplementingFk).sectionName, g_relationships.descriptors(entityIdImplementingFk).relName, eactRelationship, _
                       False, False, False, False, True, False, , , , isExpressionRel, Not isReused
                   End If
 ' ### ENDIF IVK ###
               End If
             End If
           End If
         End If
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
 
 
 
 
 
