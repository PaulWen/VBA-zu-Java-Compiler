 Attribute VB_Name = "M14_XMLExport"
 ' ### IF IVK ###
 Option Explicit
 
 
 Private Sub genXmlExportViewSupportDdlForNlTable( _
   ByRef attrRefs As AttrDescriptorRefs, _
   ByRef xmlElementName As String, _
   ByRef qualNlTabName As String, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional inclTvAttrs As Boolean = False, _
   Optional inclNonTvAttrs As Boolean = False, _
   Optional onlyThisAttribute As Integer = -1, _
   Optional ByRef tabVariable As String = "T", _
   Optional ByRef idVariable As String = conOid, _
   Optional ByRef idRefVariable As String = conOid, _
   Optional ByRef indent As Integer = 9, _
   Optional ByRef extraAttributeName As String = "" _
 )
   Print #fileNo, addTab(indent) & ",XMLELEMENT (NAME """; UCase(xmlElementName); "S"","
   Print #fileNo, addTab(indent + 1); "(SELECT"
   Print #fileNo, addTab(indent + 2); "XMLAGG ("
   Print #fileNo, addTab(indent + 3); "XMLELEMENT ("
   Print #fileNo, addTab(indent + 4); "NAME """; UCase(xmlElementName); ""","

     Dim i As Integer
     For i = 1 To attrRefs.numDescriptors
       If onlyThisAttribute = -1 Or (onlyThisAttribute = attrRefs.descriptors(i).refIndex) Then
           If IIf(inclTvAttrs, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isTimeVarying, False) Or IIf(inclNonTvAttrs, Not g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isTimeVarying, False) Then
             Print #fileNo, addTab(indent + 5); "XMLELEMENT (NAME """; UCase(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName); """, L."; UCase(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName); "),"
           End If
       End If
     Next i

   If extraAttributeName <> "" Then
     Print #fileNo, addTab(indent + 5); "XMLELEMENT (NAME """; UCase(extraAttributeName); """, L."; UCase(extraAttributeName); "),"
   End If

   Print #fileNo, addTab(indent + 5); "XMLELEMENT (NAME "; """"; g_anLanguageId; """, "; "L."; g_anLanguageId; ")"

   Print #fileNo, addTab(indent + 3); ")"
   Print #fileNo, addTab(indent + 2); ") FROM "; qualNlTabName; " AS L WHERE L."; UCase(idRefVariable); " = "; tabVariable; "."; UCase(idVariable)
   Print #fileNo, addTab(indent + 1); ")"
   Print #fileNo, addTab(indent + 0); ")"
 End Sub
 
 
 Private Sub genXmlExportViewDdlForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim sectionName As String
   Dim sectionShortName As String
   Dim sectionIndex As Integer
   Dim orMappingEntityIndex As Integer
   Dim entityTypeDescr As String
   Dim entityName As String
   Dim entityShortName As String
   Dim entityNameDb As String
   Dim orEntityShortName As String
   Dim classIdStr As String
   Dim hasOwnTable As Boolean
   Dim qualTabName As String
   Dim qualTabNameGen As String
   Dim qualViewName As String
   Dim qualViewNameLdm  As String
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim hasGenTab As Boolean
   Dim isCtoAliasCreated As Boolean
   Dim isPsTagged As Boolean
   Dim useSurrogateKey As Boolean
   Dim navToDivRelRefIndex As Integer ' follow this relationship when navigating to Division
   Dim navToDivDirection As RelNavigationDirection ' indicates wheter we need to follow left or right hand side to navigate to Division
   Dim navToFirstClassToDivDirection As RelNavigationDirection ' if we are dealing with a relationship, when navigating to 'Division' we need to first follow left or right hand side to get to a Class from where we step further
   Dim navRefClassIndex As Integer
   Dim navRefClassShortName As String
   Dim fkAttrToClass As String

   On Error GoTo ErrorExit

   orMappingEntityIndex = -1

   If acmEntityType = eactClass Then
       orMappingEntityIndex = g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex
       orEntityShortName = g_classes.descriptors(g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex).shortName
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_classes.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       entityTypeDescr = "ACM-Class"
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityNameDb = entityName
       classIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       hasGenTab = g_classes.descriptors(acmEntityIndex).isGenForming And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity
       isCtoAliasCreated = g_classes.descriptors(acmEntityIndex).isCtoAliasCreated
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       useSurrogateKey = g_classes.descriptors(acmEntityIndex).useSurrogateKey

       navToFirstClassToDivDirection = -1
       navToDivRelRefIndex = g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex
       navToDivDirection = g_classes.descriptors(acmEntityIndex).navPathToDiv.navDirection
       navRefClassIndex = -1

       qualTabName = genQualTabNameByClassIndex(orMappingEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, False)
       If g_classes.descriptors(acmEntityIndex).isGenForming And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity Then
         qualTabNameGen = genQualTabNameByClassIndex(orMappingEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, True)
       Else
         qualTabNameGen = ""
       End If

       qualViewName = genQualViewNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, False, , , , , gc_xmlObjNameSuffix)
       qualViewNameLdm = genQualViewNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, edtLdm, thisOrgIndex, thisPoolIndex, False, , , , , gc_xmlObjNameSuffix)
   ElseIf acmEntityType = eactRelationship Then
       orMappingEntityIndex = g_relationships.descriptors(acmEntityIndex).relIndex
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_relationships.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       entityTypeDescr = "ACM-Relationship"
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityNameDb = entityName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       orEntityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       classIdStr = ""
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       hasGenTab = False
       isCtoAliasCreated = g_relationships.descriptors(acmEntityIndex).isCtoAliasCreated
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       useSurrogateKey = useSurrogateKeysForNMRelationships

       navToFirstClassToDivDirection = g_relationships.descriptors(acmEntityIndex).navPathToDiv.navDirectionToClass
       navToDivRelRefIndex = -1
       navToDivDirection = -1
       If navToFirstClassToDivDirection = etLeft Then
         ' we need to follow relationship to left -> figure out what the complete path to Division is
         navRefClassIndex = g_relationships.descriptors(acmEntityIndex).leftEntityIndex
         navRefClassShortName = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).shortName
         fkAttrToClass = genSurrogateKeyName(ddlType, navRefClassShortName)
           navToDivRelRefIndex = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).navPathToDiv.relRefIndex
           navToDivDirection = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).navPathToDiv.navDirection
       ElseIf navToFirstClassToDivDirection = etRight Then
         ' we need to follow relationship to right -> figure out what the complete path to Division is
         navRefClassIndex = g_relationships.descriptors(acmEntityIndex).rightEntityIndex
         navRefClassShortName = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).shortName
         fkAttrToClass = genSurrogateKeyName(ddlType, g_relationships.descriptors(acmEntityIndex).lrShortRelName)
           navToDivRelRefIndex = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).navPathToDiv.relRefIndex
           navToDivDirection = g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).navPathToDiv.navDirection
       End If

       qualTabName = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex)
       qualViewName = genQualViewNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, False, , , , gc_xmlObjNameSuffix)
       qualViewNameLdm = genQualViewNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, edtLdm, thisOrgIndex, thisPoolIndex, False, , , , gc_xmlObjNameSuffix)
   ElseIf acmEntityType = eactEnum Then
       orMappingEntityIndex = g_enums.descriptors(acmEntityIndex).enumIndex
       sectionName = g_enums.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_enums.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_enums.descriptors(acmEntityIndex).sectionIndex
       entityTypeDescr = "ACM-Enumeration"
       entityName = g_enums.descriptors(acmEntityIndex).enumName
       entityShortName = g_enums.descriptors(acmEntityIndex).shortName
       orEntityShortName = g_enums.descriptors(acmEntityIndex).shortName
       entityNameDb = g_enums.descriptors(acmEntityIndex).enumNameDb
       classIdStr = ""
       hasOwnTable = True
       isCommonToOrgs = g_enums.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_enums.descriptors(acmEntityIndex).isCommonToPools
       hasGenTab = False
       isCtoAliasCreated = g_enums.descriptors(acmEntityIndex).isCtoAliasCreated
       isPsTagged = False
       useSurrogateKey = useSurrogateKeysForNMRelationships

       qualTabName = genQualTabNameByEnumIndex(g_enums.descriptors(acmEntityIndex).enumIndex, ddlType, thisOrgIndex, thisPoolIndex)
       qualViewName = genQualViewNameByEnumIndex(g_enums.descriptors(acmEntityIndex).enumIndex, ddlType, thisOrgIndex, thisPoolIndex, False, , gc_xmlObjNameSuffix)
       qualViewNameLdm = genQualViewNameByEnumIndex(g_enums.descriptors(acmEntityIndex).enumIndex, edtLdm, thisOrgIndex, thisPoolIndex, False, , gc_xmlObjNameSuffix)
   End If
 
   Dim parentOidFk As String
   parentOidFk = genSurrogateKeyName(ddlType, orEntityShortName)

   If generateXmlExportViews Then
     ' ####################################################################################################################
     ' #    View to generate XML-Export for entity
     ' ####################################################################################################################

     If entityName = "TaxParameter" Then
         Print #fileNo,
     End If


     printSectionHeader("View generating XML-Export of " & entityTypeDescr & " """ & sectionName & "." & entityName & """", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE VIEW"
     Print #fileNo, addTab(1); qualViewName

     Print #fileNo, addTab(0); "("
     If generateXmlPsOidColForPsTaggedEntities And isPsTagged Then
       Print #fileNo, genAttrDeclByDomain(conPsOid, cosnPsOid, dxnOid, dnOid, acmEntityType, acmEntityIndex, , True, ddlType, , edomListNonLrt Or edomXml, eacPsOid Or eacFkOid, , 1)
     End If
     Print #fileNo, genAttrDeclByDomain(conXmlRecord, cosnXmlRecord, eavtDomain, g_domainIndexXmlRecord, acmEntityType, acmEntityIndex, , False, ddlType, , edomListNonLrt Or edomXml, eacRegular, , 1)

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "AS"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "SELECT"
     If generateXmlPsOidColForPsTaggedEntities And isPsTagged Then
       Print #fileNo, addTab(2); "T."; g_anPsOid; ","
     End If
     Print #fileNo, addTab(2); "XMLSERIALIZE ("
     Print #fileNo, addTab(3); "CONTENT XMLELEMENT ("
     Print #fileNo, addTab(4); "NAME ""ROWS"","
     Print #fileNo, addTab(5); "XMLAGG ("
     Print #fileNo, addTab(6); "XMLELEMENT ("
     Print #fileNo, addTab(7); "NAME ""ROW"","
     Dim transformation As AttributeListTransformation

     initAttributeTransformation(transformation, 6, , True, , "XMLELEMENT (NAME """, , , , , , , ")", """, T.")
     setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T")
 
     transformation.domainRefs.numRefs = 0
       setAttributeMapping(transformation, 1, , , g_domains.descriptors(g_domainIndexTemplateFileData).sectionName, g_domains.descriptors(g_domainIndexTemplateFileData).domainName)
       setAttributeMapping(transformation, 2, conValue, "XMLELEMENT (NAME """ & g_anValue & """, CAST(NULL AS VARCHAR(1)))", g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
       setAttributeMapping(transformation, 3, , , g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
       setAttributeMapping(transformation, 4, , , g_domains.descriptors(g_domainIndexBIBRegistryValue).sectionName, g_domains.descriptors(g_domainIndexBIBRegistryValue).domainName)
       setAttributeMapping(transformation, 5, , , g_domains.descriptors(g_domainIndexLongText).sectionName, g_domains.descriptors(g_domainIndexLongText).domainName)
       setAttributeMapping(transformation, 6, , , g_domains.descriptors(g_domainIndexReportFileData).sectionName, g_domains.descriptors(g_domainIndexReportFileData).domainName)

     Dim nlObjName As String
     Dim nlObjShortName As String
     Dim qualNlTabName  As String
     If acmEntityType = eactClass Then
       genTransformedAttrListForClassRecursive(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, _
         False, False, edomValueNonLrt Or edomValueExpression Or edomXml, erdUp)

       ' generations
         If g_classes.descriptors(acmEntityIndex).isGenForming And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity Then
           Dim transformationGen As AttributeListTransformation
           transformationGen = transformation

           Print #fileNo, addTab(8); ",XMLELEMENT (NAME ""GENERATIONS"","
           Print #fileNo, addTab(9); "(SELECT"
           Print #fileNo, addTab(11); "XMLAGG ("
           Print #fileNo, addTab(12); "XMLELEMENT ("
           Print #fileNo, addTab(12); "NAME ""GENERATION"","

           transformationGen.attributeRepeatDelimiter = """, G."
           genTransformedAttrListForClassRecursive(acmEntityIndex, transformationGen, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 13, _
             False, True, edomValueNonLrt Or edomValueExpression Or edomXml, erdUp)

           transformation.domainRefs = transformationGen.domainRefs
           ' NL-attributes for GEN table
             If transformationGen.numNlAttrRefsTv > 0 Then
               qualNlTabName = genQualTabNameByClassIndex(orMappingEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , , True)

               genXmlExportViewSupportDdlForNlTable(transformationGen.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, True, False, , "G", g_anOid, parentOidFk, 14)
             End If

           Print #fileNo, addTab(12); ")"
           Print #fileNo, addTab(11); ") FROM "; qualTabNameGen; " AS G WHERE T."; g_anOid; " = G."; parentOidFk
           Print #fileNo, addTab(10); ")"
           Print #fileNo, addTab(9); ")"

         End If
     ElseIf acmEntityType = eactRelationship Then
       Dim tabColumns As EntityColumnDescriptors
       tabColumns = nullEntityColumnDescriptors
       genTransformedAttrDeclsForRelationshipWithColReUse(acmEntityIndex, transformation, tabColumns, fileNo, _
         ddlType, thisOrgIndex, thisPoolIndex, 8, , , False, edomListNonLrt Or edomXml)
     ElseIf acmEntityType = eactEnum Then
       Print #fileNo, addTab(8); _
                            "XMLELEMENT (NAME "; _
                            """"; g_anEnumId; """, "; _
                            "T."; g_anEnumId; ")"; _
                            IIf(g_enums.descriptors(acmEntityIndex).attrRefs.numDescriptors > 0 Or xmlExportColumnVersionId, ",", "")
 
       genTransformedAttrDeclsForEnum(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, edomList Or edomXml, xmlExportColumnVersionId)
     End If

     ' NL-attributes for non-GEN table
       If (acmEntityType = eactEnum) Or (IIf(Not hasGenTab, transformation.numNlAttrRefsTv, 0) + transformation.numNlAttrRefsNonTv > 0) Then
         qualNlTabName = genQualTabNameByEntityIndex(orMappingEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, , , , True)

         If acmEntityType = eactEnum Then
           genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, Not hasGenTab, True, , , g_anEnumId, g_anEnumRefId, 8, g_anEnumLabelText)
         Else
           genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, Not hasGenTab, True, , , g_anOid, parentOidFk, 8)
         End If
       End If

     Print #fileNo, addTab(6); ")"
     Print #fileNo, addTab(5); ")"
     Print #fileNo, addTab(3); ") AS CLOB("; CStr(maxXmlExportStringLength); ")"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabName; " AS T"

     If Not hasOwnTable And classIdStr <> "" Then
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "T."; g_anCid; " = '"; classIdStr; "'"
     End If

     If generateXmlPsOidColForPsTaggedEntities And isPsTagged Then
       Print #fileNo, addTab(1); "GROUP BY"
       Print #fileNo, addTab(2); "T."; conPsOid
     End If

     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); gc_sqlCmdDelim

     If ddlType = edtPdm Then
       genAliasDdl(sectionIndex, entityNameDb, isCommonToOrgs, isCommonToPools, True, _
                   qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, False, False, False, _
                   "XML-Export-View """ & sectionName & "." & entityName & """", gc_xmlObjNameSuffix)
     End If
   End If
 
   If generateXmlExportFuncs Then
     Dim qualFuncName As String

     ' ####################################################################################################################
     ' #    Function generating XML-Export for entity (all-in-one)
     ' ####################################################################################################################

     qualFuncName = genQualFuncName(sectionIndex, entityShortName, ddlType, thisOrgIndex, thisPoolIndex, , , gc_xmlObjNameSuffix)
     printSectionHeader("Function generating " & IIf(isPsTagged, "PS-specific ", "") & "XML-Export of " & entityTypeDescr & " """ & sectionName & "." & entityName & """ (all-in-one)", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); qualFuncName
     Print #fileNo, addTab(0); "("

     If isPsTagged Then
       genProcParm(fileNo, "", "psOid_in", g_dbtOid, False, "OID of the ProductStructure to export records for")
     End If
 
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RETURNS TABLE"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "record CLOB("; CStr(maxXmlExportStringLength); ")"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "READS SQL DATA"
     Print #fileNo, addTab(0); "RETURN"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "XMLSERIALIZE ("
     Print #fileNo, addTab(3); "CONTENT XMLELEMENT ("
     Print #fileNo, addTab(4); "NAME ""ROWS"","
     Print #fileNo, addTab(5); "XMLAGG ("
     Print #fileNo, addTab(6); "XMLELEMENT ("
     Print #fileNo, addTab(7); "NAME ""ROW"","

     initAttributeTransformation(transformation, 6, , True, , "XMLELEMENT (NAME """, , , , , , , ")", """, T.")
     setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T")
 
       setAttributeMapping(transformation, 1, , , g_domains.descriptors(g_domainIndexTemplateFileData).sectionName, g_domains.descriptors(g_domainIndexTemplateFileData).domainName)
       setAttributeMapping(transformation, 2, conValue, "XMLELEMENT (NAME """ & g_anValue & """, CAST(NULL AS VARCHAR(1)))", g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
       setAttributeMapping(transformation, 3, , , g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
       setAttributeMapping(transformation, 4, , , g_domains.descriptors(g_domainIndexBIBRegistryValue).sectionName, g_domains.descriptors(g_domainIndexBIBRegistryValue).domainName)
       setAttributeMapping(transformation, 5, , , g_domains.descriptors(g_domainIndexLongText).sectionName, g_domains.descriptors(g_domainIndexLongText).domainName)
       setAttributeMapping(transformation, 6, , , g_domains.descriptors(g_domainIndexReportFileData).sectionName, g_domains.descriptors(g_domainIndexReportFileData).domainName)

     transformation.domainRefs.numRefs = 0

     If acmEntityType = eactClass Then
       genTransformedAttrListForClassRecursive(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, _
         False, False, edomValueNonLrt Or edomValueExpression Or edomXml, erdUp)

       ' generations
         If g_classes.descriptors(acmEntityIndex).isGenForming And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity Then
           transformationGen = transformation

           Print #fileNo, addTab(8); ",XMLELEMENT (NAME ""GENERATIONS"","
           Print #fileNo, addTab(9); "(SELECT"
           Print #fileNo, addTab(10); "XMLAGG ("
           Print #fileNo, addTab(11); "XMLELEMENT ("
           Print #fileNo, addTab(12); "NAME ""GENERATION"","

           transformationGen.attributeRepeatDelimiter = """, G."
           genTransformedAttrListForClassRecursive(acmEntityIndex, transformationGen, fileNo, ddlType, _
             thisOrgIndex, thisPoolIndex, 13, False, True, edomValueNonLrt Or edomValueExpression Or edomXml, erdUp)

           transformation.domainRefs = transformationGen.domainRefs
           ' NL-attributes for GEN table
             If transformationGen.numNlAttrRefsTv > 0 Then
               qualNlTabName = genQualTabNameByClassIndex(orMappingEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , , True)

               genXmlExportViewSupportDdlForNlTable(transformationGen.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, True, False, , "G", g_anOid, parentOidFk, 13)
             End If

           Print #fileNo, addTab(11); ")"
           Print #fileNo, addTab(10); ") FROM "; qualTabNameGen; " AS G WHERE T."; g_anOid; " = G."; parentOidFk
           Print #fileNo, addTab(9); ")"
           Print #fileNo, addTab(8); ")"

         End If
     ElseIf acmEntityType = eactRelationship Then
       tabColumns = nullEntityColumnDescriptors
       genTransformedAttrDeclsForRelationshipWithColReUse(acmEntityIndex, transformation, tabColumns, fileNo, _
         ddlType, thisOrgIndex, thisPoolIndex, 8, , , False, edomValueNonLrt Or edomValueExpression Or edomXml)
     ElseIf acmEntityType = eactEnum Then
       Print #fileNo, addTab(8); _
                            "XMLELEMENT (NAME "; _
                            """"; g_anEnumId; """, "; _
                            "T."; g_anEnumId; ")"; _
                            IIf(g_enums.descriptors(acmEntityIndex).attrRefs.numDescriptors > 0 Or xmlExportColumnVersionId, ",", "")

       genTransformedAttrDeclsForEnum(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 8, edomValue Or edomXml, xmlExportColumnVersionId)
     End If

     ' NL-attributes for non-GEN table
       If (acmEntityType = eactEnum) Or (IIf(Not hasGenTab, transformation.numNlAttrRefsTv, 0) + transformation.numNlAttrRefsNonTv > 0) Then
         qualNlTabName = genQualTabNameByEntityIndex(orMappingEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, , , , True)

         If acmEntityType = eactEnum Then
           genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, Not hasGenTab, True, , , g_anEnumId, g_anEnumRefId, 8, g_anEnumLabelText)
         Else
           genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, Not hasGenTab, True, , , g_anOid, parentOidFk, 8)
         End If
       End If

     Print #fileNo, addTab(6); ")"
     Print #fileNo, addTab(5); ")"
     Print #fileNo, addTab(3); ") AS CLOB("; maxXmlExportStringLength; ")"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabName; " AS T"

     If Not hasOwnTable And classIdStr <> "" Then
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "T."; g_anCid; " = '"; classIdStr; "'"
       If isPsTagged Then
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "T."; conPsOid; " = psOid_in"
       End If
     ElseIf isPsTagged Then
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "T."; conPsOid; " = psOid_in"
     End If

     Print #fileNo, addTab(0); gc_sqlCmdDelim

     ' ####################################################################################################################
     ' #    Function generating XML-Export for entity (segmented)
     ' ####################################################################################################################

     qualFuncName = genQualFuncName(sectionIndex, entityShortName, ddlType, thisOrgIndex, thisPoolIndex, , , gc_xmlObjNameSuffix)
     printSectionHeader("Function generating " & IIf(isPsTagged, "PS-specific ", "") & "XML-Export of " & entityTypeDescr & " """ & sectionName & "." & entityName & """ (segmented)", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); qualFuncName
     Print #fileNo, addTab(0); "("

     genProcParm(fileNo, "", "startRecord_in", "BIGINT", True, "'first' record number to retrieve (starting with 1 and based on ascending OID-ordering)")
     genProcParm(fileNo, "", "maxRecords_in", "INTEGER", isPsTagged, "maximum number of records to retrieve in one segment")

     If isPsTagged Then
       genProcParm(fileNo, "", "psOid_in", g_dbtOid, False, "OID of the ProductStructure to export records for")
     End If
 
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RETURNS TABLE"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "totalRecords BIGINT, -- total number of records in a 'full export'"
     Print #fileNo, addTab(1); "record       CLOB("; CStr(maxXmlExportStringLength); ")"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "READS SQL DATA"
     Print #fileNo, addTab(0); "RETURN"

     Print #fileNo, addTab(1); "WITH"
     Print #fileNo, addTab(2); "V_T_Ordered"
     Print #fileNo, addTab(1); "AS"
     Print #fileNo, addTab(1); "("

     Print #fileNo, addTab(2); "SELECT"

     initAttributeTransformation(transformation, 6, , , , "T.")
     setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T")
 
       setAttributeMapping(transformation, 1, , , g_domains.descriptors(g_domainIndexTemplateFileData).sectionName, g_domains.descriptors(g_domainIndexTemplateFileData).domainName)
       setAttributeMapping(transformation, 2, conValue, "T." & g_anValue & "", g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
       setAttributeMapping(transformation, 3, , , g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
       setAttributeMapping(transformation, 4, , , g_domains.descriptors(g_domainIndexBIBRegistryValue).sectionName, g_domains.descriptors(g_domainIndexBIBRegistryValue).domainName)
       setAttributeMapping(transformation, 5, , , g_domains.descriptors(g_domainIndexLongText).sectionName, g_domains.descriptors(g_domainIndexLongText).domainName)
       setAttributeMapping(transformation, 6, , , g_domains.descriptors(g_domainIndexReportFileData).sectionName, g_domains.descriptors(g_domainIndexReportFileData).domainName)

     If acmEntityType = eactEnum Then
       Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY T.ID ASC) AS ROWNUM,"
       ' todo: shouldn't we include this in 'genAttrList...'-Subroutine?
       Print #fileNo, addTab(3); "T.ID"; IIf(g_enums.descriptors(acmEntityIndex).attrRefs.numDescriptors > 0 Or xmlExportColumnVersionId, ",", "")
     Else
       If useSurrogateKey Then
         Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY T."; g_anOid; " ASC) AS ROWNUM,"
       Else
         Dim pkAttrList As String
         If acmEntityType = eactClass Then
           pkAttrList = getPkAttrListByClass(acmEntityIndex, ddlType, "T.")
         End If
         If pkAttrList = "" Then
           Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY"

           genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 4, , , edomListNonLrt Or edomXml, erdUp)

           Print #fileNo, addTab(3); ") AS ROWNUM,"
         Else
           Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY "; pkAttrList; ") AS ROWNUM,"
         End If
       End If
     End If

     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, xmlExportColumnVersionId, , , (edomListNonLrt And Not edomExpression) Or edomXml Or edomExpressionRef, erdUp)

     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabName; " AS T"
     If Not hasOwnTable And classIdStr <> "" Then
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); g_anCid; " = '"; classIdStr; "'"
       If isPsTagged Then
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); conPsOid; " = psOid_in"
       End If
     ElseIf isPsTagged Then
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); conPsOid; " = psOid_in"
     End If
     Print #fileNo, addTab(1); ")"

     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "(SELECT COUNT(*) FROM V_T_Ordered),"
     Print #fileNo, addTab(2); "XMLSERIALIZE ("
     Print #fileNo, addTab(3); "CONTENT XMLAGG ("
     Print #fileNo, addTab(4); "XMLELEMENT ("
     Print #fileNo, addTab(5); "NAME ""ROW"","

     initAttributeTransformation(transformation, 6, , True, , "XMLELEMENT (NAME """, , , , , , , ")", """, T.")
     setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "T")
 
       setAttributeMapping(transformation, 1, , , g_domains.descriptors(g_domainIndexTemplateFileData).sectionName, g_domains.descriptors(g_domainIndexTemplateFileData).domainName)
       setAttributeMapping(transformation, 2, conValue, "XMLELEMENT (NAME """ & g_anValue & """, CAST(NULL AS VARCHAR(1)))", g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
       setAttributeMapping(transformation, 3, , , g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
       setAttributeMapping(transformation, 4, , , g_domains.descriptors(g_domainIndexBIBRegistryValue).sectionName, g_domains.descriptors(g_domainIndexBIBRegistryValue).domainName)
       setAttributeMapping(transformation, 5, , , g_domains.descriptors(g_domainIndexLongText).sectionName, g_domains.descriptors(g_domainIndexLongText).domainName)
       setAttributeMapping(transformation, 6, , , g_domains.descriptors(g_domainIndexReportFileData).sectionName, g_domains.descriptors(g_domainIndexReportFileData).domainName)

     transformation.domainRefs.numRefs = 0

     If acmEntityType = eactClass Then
       genTransformedAttrListForClassRecursive(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, _
         False, False, edomValueNonLrt Or edomValueExpression Or edomXml, erdUp)

       ' generations
         If g_classes.descriptors(acmEntityIndex).isGenForming And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity Then
           transformationGen = transformation

           Print #fileNo, addTab(6); ",XMLELEMENT (NAME ""GENERATIONS"","
           Print #fileNo, addTab(7); "(SELECT"
           Print #fileNo, addTab(8); "XMLAGG ("
           Print #fileNo, addTab(9); "XMLELEMENT ("
           Print #fileNo, addTab(10); "NAME ""GENERATION"","

           transformationGen.attributeRepeatDelimiter = """, G."
           genTransformedAttrListForClassRecursive(acmEntityIndex, transformationGen, fileNo, ddlType, _
             thisOrgIndex, thisPoolIndex, 11, False, True, edomValueNonLrt Or edomValueExpression Or edomXml, erdUp)

           transformation.domainRefs = transformationGen.domainRefs
           ' NL-attributes for GEN table
             If transformationGen.numNlAttrRefsTv > 0 Then
               qualNlTabName = genQualTabNameByEntityIndex(orMappingEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, True, , , True)

               genXmlExportViewSupportDdlForNlTable(transformationGen.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, True, False, , "G", g_anOid, parentOidFk, 11)
             End If

           Print #fileNo, addTab(9); ")"
           Print #fileNo, addTab(8); ") FROM "; qualTabNameGen; " AS G WHERE T."; g_anOid; " = G."; g_classes.descriptors(orMappingEntityIndex).shortName; "_OID"
           Print #fileNo, addTab(7); ")"
           Print #fileNo, addTab(6); ")"

         End If
     ElseIf acmEntityType = eactRelationship Then
       tabColumns = nullEntityColumnDescriptors
       genTransformedAttrDeclsForRelationshipWithColReUse(acmEntityIndex, transformation, tabColumns, _
         fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, , , False, edomValueNonLrt Or edomValueExpression Or edomXml)
     ElseIf acmEntityType = eactEnum Then
       Print #fileNo, addTab(6); _
                            "XMLELEMENT (NAME "; _
                            """"; g_anEnumId; """, "; _
                            "T."; g_anEnumId; ")"; _
                            IIf(g_enums.descriptors(acmEntityIndex).attrRefs.numDescriptors > 0 Or xmlExportColumnVersionId, ",", "")

       genTransformedAttrDeclsForEnum(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, edomValue Or edomXml, xmlExportColumnVersionId)
     End If
 
     ' NL-attributes for non-GEN table
       If IIf(Not hasGenTab, transformation.numNlAttrRefsTv, 0) + transformation.numNlAttrRefsNonTv > 0 Then
         qualNlTabName = genQualTabNameByEntityIndex(orMappingEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, , , , True)

         If acmEntityType = eactEnum Then
           genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, Not hasGenTab, True, , , g_anEnumId, g_anEnumRefId, 7, g_anEnumLabelText)
         Else
           genXmlExportViewSupportDdlForNlTable(transformation.nlAttrRefs, "NlText", qualNlTabName, fileNo, ddlType, Not hasGenTab, True, , , g_anOid, parentOidFk, 7)
         End If
       End If

     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); ") AS CLOB("; maxXmlExportStringLength; ")"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "V_T_Ordered T"
 
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "ROWNUM BETWEEN COALESCE(startRecord_in, 1) AND COALESCE(startRecord_in + maxRecords_in - 1, 9999999999999999999999999999999)"

     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genXmlExportXsdFuncSupportForNlTable( _
   ByRef attrRefs As AttrDescriptorRefs, _
   ByRef xmlElementName As String, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional onlyThisAttribute As Integer = -1, _
   Optional inclTvAttrs As Boolean = False, _
   Optional inclNonTvAttrs As Boolean = False, _
   Optional indent As Integer = 9, _
   Optional ByRef extraAttributeName As String = "", _
   Optional ByRef extraSectionName As String = "", _
   Optional ByRef extraDomainName As String = "", _
   Optional ByRef extraIsNullable As Boolean = False _
 )
   Print #fileNo, addTab(2); "'"; addTab(indent + 0); "<element name="""; UCase(xmlElementName); "S"" minOccurs=""0"" maxOccurs=""unbounded"">'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 1); "<complexType>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 2); "<sequence>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 3); "<element name="""; UCase(xmlElementName); """ minOccurs=""0"" maxOccurs=""unbounded"">'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 4); "<complexType>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 5); "<sequence>'"; " || cr ||"

     Dim i As Integer
     For i = 1 To attrRefs.numDescriptors
       If onlyThisAttribute = -1 Or (onlyThisAttribute = attrRefs.descriptors(i).refIndex) Then
           If IIf(inclTvAttrs, g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isTimeVarying, False) Or IIf(inclNonTvAttrs, Not g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isTimeVarying, False) Then
             Print #fileNo, addTab(2); "'"; addTab(indent + 6); "<element name="""; UCase(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName); _
                                       """ type=""standardxml:"; g_attributes.descriptors(attrRefs.descriptors(i).refIndex).sectionName; "_"; g_attributes.descriptors(attrRefs.descriptors(i).refIndex).domainName; IIf(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNullable, "_N", ""); """/>' || cr ||"
           End If
       End If
     Next i
 
   If extraAttributeName <> "" Then
     Print #fileNo, addTab(2); "'"; addTab(indent + 6); "<element name="""; UCase(extraAttributeName); _
                               """ type=""standardxml:"; extraSectionName; "_"; extraDomainName; IIf(extraIsNullable, "_N", ""); """/>' || cr ||"
   End If

   Print #fileNo, addTab(2); "'"; addTab(indent + 6); "<element name="""; _
                  g_anLanguageId; """ type=""standardxml:"; g_anLanguageId; """/>' || cr ||"

   Print #fileNo, addTab(2); "'"; addTab(indent + 5); "</sequence>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 4); "</complexType>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 3); "</element>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 2); "</sequence>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 1); "</complexType>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(indent + 0); "</element>'"; " || cr ||"
 End Sub
 
 
 Private Function xmlNormalizedEntityName( _
   ByRef entityName As String _
 ) As String
   xmlNormalizedEntityName = UCase(Left(entityName, 1)) & LCase(Right(entityName, Len(entityName) - 1))
 End Function
 
 
 Private Sub genXmlExportXsdFuncForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not generateXmlXsdFuncs Then
     Exit Sub
   End If
 
   Dim sectionName As String
   Dim sectionShortName As String
   Dim sectionIndex As Integer
   Dim entityTypeDescr As String
   Dim entityName As String
   Dim entityShortName As String
   Dim qualFuncName As String
   Dim isXsdExported As Boolean
   Dim hasGenTab As Boolean
   Dim maxStrLength As Integer

   On Error GoTo ErrorExit

   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_classes.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       entityTypeDescr = "ACM-Class"
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       hasGenTab = g_classes.descriptors(acmEntityIndex).isGenForming And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity
       isXsdExported = g_classes.descriptors(acmEntityIndex).isXsdExported
       maxStrLength = 0
       g_classes.descriptors(acmEntityIndex).isXsdExported = True
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_relationships.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       entityTypeDescr = "ACM-Relationship"
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       hasGenTab = False
       isXsdExported = g_relationships.descriptors(acmEntityIndex).isXsdExported
       maxStrLength = 0

       g_relationships.descriptors(acmEntityIndex).isXsdExported = True
   ElseIf acmEntityType = eactEnum Then
       sectionName = g_enums.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_enums.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_enums.descriptors(acmEntityIndex).sectionIndex
       entityTypeDescr = "ACM-Enumeration"
       entityName = g_enums.descriptors(acmEntityIndex).enumName
       entityShortName = g_enums.descriptors(acmEntityIndex).shortName
       hasGenTab = False
       isXsdExported = g_enums.descriptors(acmEntityIndex).isXsdExported
       maxStrLength = g_enums.descriptors(acmEntityIndex).maxLength

       g_enums.descriptors(acmEntityIndex).isXsdExported = True
   End If
 
   If isXsdExported Then
     Exit Sub
   End If
 
   If generateXsdInCtoSchema Then
     qualFuncName = genQualFuncName(sectionIndex, entityShortName, ddlType, , , , , gc_xsdObjNameSuffix)
   Else
     qualFuncName = genQualFuncName(sectionIndex, entityShortName, ddlType, thisOrgIndex, thisPoolIndex, , , gc_xsdObjNameSuffix)
   End If
 
   printSectionHeader("Function generating XSD for " & entityTypeDescr & " """ & sectionName & "." & entityName & """", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName; " ()"
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(32000)"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "DECLARE cr CHAR(1);"
   Print #fileNo, addTab(1); "SET cr = CHR(10);"
   Print #fileNo, addTab(1); "RETURN"
   Print #fileNo, addTab(2); "'"; "<?xml version=""1.0"" encoding=""UTF-8""?>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(0); "<schema xmlns:standardxml=""http://ivkmds.dcx.com/ASBO/StandardXML""' || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(0); "targetNamespace=""http://ivkmds.dcx.com/ASBO/StandardXML""' || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(0); "xmlns=""http://www.w3.org/2001/XMLSchema"">' || cr || cr ||"

   Print #fileNo, addTab(2); "'"; addTab(0); "<element name=""ACM-"; xmlNormalizedEntityName(entityName); Chr(34); ">'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(1); "<complexType>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(2); "<sequence>' || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(3); "<element name=""ROWS"" minOccurs=""0"" maxOccurs=""unbounded"">'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(4); "<complexType>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(5); "<sequence>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(6); "<element name=""ROW"" minOccurs=""0"" maxOccurs=""unbounded"">'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(7); "<complexType>'"; " || cr ||"
   Print #fileNo, addTab(2); "'"; addTab(8); "<sequence>'"; " || cr ||"

   Dim transformation As AttributeListTransformation
   initAttributeTransformation(transformation, 6, True, True, , , , , , , , , , , , xmlExportVirtualColumns, xmlExportVirtualColumns)
   transformation.distinguishNullabilityForDomainRefs = True
   Dim transformationGen As AttributeListTransformation
   transformationGen.distinguishNullabilityForDomainRefs = True
   initAttributeTransformation(transformationGen, 0, True, True, , , , , , , , , , , , xmlExportVirtualColumns, xmlExportVirtualColumns)
 
     setAttributeMapping(transformation, 1, , , g_domains.descriptors(g_domainIndexTemplateFileData).sectionName, g_domains.descriptors(g_domainIndexTemplateFileData).domainName)
     setAttributeMapping(transformation, 2, conValue, g_anValue, g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
     setAttributeMapping(transformation, 3, , , g_domains.descriptors(g_domainIndexBinaryPropertyValue).sectionName, g_domains.descriptors(g_domainIndexBinaryPropertyValue).domainName)
     setAttributeMapping(transformation, 4, , , g_domains.descriptors(g_domainIndexBIBRegistryValue).sectionName, g_domains.descriptors(g_domainIndexBIBRegistryValue).domainName)
     setAttributeMapping(transformation, 5, , , g_domains.descriptors(g_domainIndexLongText).sectionName, g_domains.descriptors(g_domainIndexLongText).domainName)
     setAttributeMapping(transformation, 6, , , g_domains.descriptors(g_domainIndexReportFileData).sectionName, g_domains.descriptors(g_domainIndexReportFileData).domainName)

   If acmEntityType = eactClass Then
     genTransformedAttrListForClassRecursive(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, False, False, _
       edomXsd Or IIf(xmlExportVirtualColumns, edomXmlVirtual, edomNone), erdUp)

     'generations
       If g_classes.descriptors(acmEntityIndex).isGenForming And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity Then
         transformationGen = transformation

         Print #fileNo, addTab(2); "'"; addTab(9); "<element name=""GENERATIONS"" minOccurs=""0"" maxOccurs=""unbounded"">'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(10); "<complexType>'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(11); "<sequence>'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(12); "<element name=""GENERATION"" minOccurs=""0"" maxOccurs=""unbounded"">'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(13); "<complexType>'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(14); "<sequence>'"; " || cr ||"

         'including generation elements
         genTransformedAttrListForClassRecursive(acmEntityIndex, transformationGen, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 12, False, True, _
           edomXsd Or IIf(xmlExportVirtualColumns, edomXmlVirtual, edomNone), erdUp)
         transformation.domainRefs = transformationGen.domainRefs

         ' NL-attributes for GEN table
           If transformationGen.numNlAttrRefsTv > 0 Then
             genXmlExportXsdFuncSupportForNlTable(transformationGen.nlAttrRefs, "NlText", fileNo, ddlType, , True, False, 15)
           End If

         Print #fileNo, addTab(2); "'"; addTab(14); "</sequence>'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(13); "</complexType>'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(12); "</element>'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(11); "</sequence>'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(10); "</complexType>'"; " || cr ||"
         Print #fileNo, addTab(2); "'"; addTab(9); "</element>'"; " || cr ||"
       End If
   ElseIf acmEntityType = eactRelationship Then
     Dim tabColumns As EntityColumnDescriptors
     tabColumns = nullEntityColumnDescriptors

     genTransformedAttrDeclsForRelationshipWithColReUse(acmEntityIndex, transformation, tabColumns, _
       fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, , , False, edomXsd Or IIf(xmlExportVirtualColumns, edomXmlVirtual, edomNone))
   ElseIf acmEntityType = eactEnum Then
     Print #fileNo, addTab(2); "'"; addTab(9); "<element name="""; g_anEnumId; """ type=""standardxml:"; sectionName; gc_enumAttrNameSuffix; """/>' || cr ||"

     genTransformedAttrDeclsForEnum(acmEntityIndex, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 6, _
       edomXsd Or IIf(xmlExportVirtualColumns, edomXmlVirtual, edomNone))
   End If

   ' NL-attributes for non-GEN table
     If (acmEntityType = eactEnum) Or (IIf(Not hasGenTab, transformation.numNlAttrRefsTv, 0) + transformation.numNlAttrRefsNonTv > 0) Then
       If acmEntityType = eactEnum Then
         genXmlExportXsdFuncSupportForNlTable(transformation.nlAttrRefs, "NlText", fileNo, ddlType, , False, True, , conEnumLabelText, snCommon, "EnumText", False)
       Else
         genXmlExportXsdFuncSupportForNlTable(transformation.nlAttrRefs, "NlText", fileNo, ddlType, , Not hasGenTab, True)
       End If
     End If

     Print #fileNo, addTab(2); "'"; addTab(8); "</sequence>'"; " || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(7); "</complexType>'"; " || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(6); "</element>'"; " || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(5); "</sequence>'"; " || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(4); "</complexType>'"; " || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(3); "</element>'"; " || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(2); "</sequence>'"; " || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(1); "</complexType>'"; " || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(0); "</element>'"; " || cr || cr ||"
     Print #fileNo,

     If acmEntityType = eactEnum Then
         genSimpleTypesForXML(fileNo, transformation, g_enums.descriptors(acmEntityIndex).sectionName, , g_enums.descriptors(acmEntityIndex).idDataType, , val(g_enums.descriptors(acmEntityIndex).maxLength), , , , True, g_enums.descriptors(acmEntityIndex).sectionName & gc_enumAttrNameSuffix)
 
       genSimpleTypesForXML(fileNo, transformation, "Common", "EnumText", etVarchar, , CStr(maxStrLength), , , , True)
       genSimpleTypesForXML(fileNo, transformation, "LANGUAGE", "ID", etInteger, , , , , , True)
     End If

     If transformation.nlAttrRefs.numDescriptors > 0 Then
       genSimpleTypesForXML(fileNo, transformation, sectionName, , etSmallint, , , , , , True, g_anLanguageId)
     ElseIf transformationGen.nlAttrRefs.numDescriptors > 0 Then
       genSimpleTypesForXML(fileNo, transformationGen, sectionName, , etSmallint, , , , , , True, g_anLanguageId)
     End If
 
   genSimpleTypesForXML(fileNo, transformation)

   Print #fileNo, addTab(2); "'"; addTab(0); "</schema>';"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genXmlExportDdlForClass( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNoF As Integer, _
   fileNoV As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not generateXmlExportSupport Or classIndex < 1 Then
     Exit Sub
   End If

     If g_classes.descriptors(classIndex).notAcmRelated Or g_classes.descriptors(classIndex).isAbstract Or g_classes.descriptors(classIndex).noXmlExport Then
       Exit Sub
     End If

   genXmlExportXsdFuncForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoF, ddlType)
   genXmlExportViewDdlForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoV, ddlType)
 End Sub
 
 
 Sub genXmlExportDdlForEnum( _
   thisEnumIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNoF As Integer, _
   fileNoV As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not generateXmlExportSupport Or thisEnumIndex < 1 Then
     Exit Sub
   End If
   If g_enums.descriptors(thisEnumIndex).notAcmRelated Then
     Exit Sub
   End If

   genXmlExportXsdFuncForEntity(thisEnumIndex, eactEnum, thisOrgIndex, thisPoolIndex, fileNoF, ddlType)
   genXmlExportViewDdlForEntity(thisEnumIndex, eactEnum, thisOrgIndex, thisPoolIndex, fileNoV, ddlType)
 End Sub
 
 
 Sub genXmlExportDdlForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNoF As Integer, _
   fileNoV As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not generateXmlExportSupport Or thisRelIndex < 1 Then
     Exit Sub
   End If

     If g_relationships.descriptors(thisRelIndex).notAcmRelated Or g_relationships.descriptors(thisRelIndex).noXmlExport Then
       Exit Sub
     End If
 
   genXmlExportXsdFuncForEntity(thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoF, ddlType)
   genXmlExportViewDdlForEntity(thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoV, ddlType)
 End Sub
 
 
 Private Sub genSimpleTypesForXML( _
   fileNo As Integer, _
   ByRef transformation As AttributeListTransformation, _
   Optional ByRef sectionName As String = "", _
   Optional ByRef domainName As String = "", _
   Optional dataType As typeId, _
   Optional ByRef minLength As String = "", _
   Optional ByRef maxLength As String = "", _
   Optional xscale As Integer = 0, _
   Optional ByRef minValue As String = "", _
   Optional ByRef maxValue As String = "", _
   Optional singleType As Boolean = False, _
   Optional ByRef attrName As String = "" _
 )
   If singleType = True Then
     printSimpleTypeForXML(dataType, fileNo, sectionName, domainName, minLength, maxLength, xscale, minValue, maxValue, attrName)
   Else
     Dim i As Integer
     For i = 1 To transformation.domainRefs.numRefs Step 1
         If supportUnicode And g_domains.descriptors(transformation.domainRefs.refs(i).ref).supportUnicode And g_domains.descriptors(transformation.domainRefs.refs(i).ref).maxLength <> "" Then
           printSimpleTypeForXML(g_domains.descriptors(transformation.domainRefs.refs(i).ref).dataType, fileNo, g_domains.descriptors(transformation.domainRefs.refs(i).ref).sectionName, g_domains.descriptors(transformation.domainRefs.refs(i).ref).domainName, g_domains.descriptors(transformation.domainRefs.refs(i).ref).minLength, _
             IIf(supportUnicode And g_domains.descriptors(transformation.domainRefs.refs(i).ref).supportUnicode, CInt(g_domains.descriptors(transformation.domainRefs.refs(i).ref).unicodeExpansionFactor * CInt(g_domains.descriptors(transformation.domainRefs.refs(i).ref).maxLength)) & "", g_domains.descriptors(transformation.domainRefs.refs(i).ref).maxLength), g_domains.descriptors(transformation.domainRefs.refs(i).ref).scale, g_domains.descriptors(transformation.domainRefs.refs(i).ref).minValue, g_domains.descriptors(transformation.domainRefs.refs(i).ref).maxValue, , _
             transformation.domainRefs.refs(i).isNullable)
         Else
           printSimpleTypeForXML(g_domains.descriptors(transformation.domainRefs.refs(i).ref).dataType, fileNo, g_domains.descriptors(transformation.domainRefs.refs(i).ref).sectionName, g_domains.descriptors(transformation.domainRefs.refs(i).ref).domainName, g_domains.descriptors(transformation.domainRefs.refs(i).ref).minLength, _
             g_domains.descriptors(transformation.domainRefs.refs(i).ref).maxLength, g_domains.descriptors(transformation.domainRefs.refs(i).ref).scale, g_domains.descriptors(transformation.domainRefs.refs(i).ref).minValue, g_domains.descriptors(transformation.domainRefs.refs(i).ref).maxValue, , transformation.domainRefs.refs(i).isNullable)
         End If
     Next i
   End If
 End Sub
 
 
 Private Sub printSimpleTypeForXMLCore( _
   fileNo As Integer, _
   typeStr As String, _
   isOptional As Boolean, _
   Optional indent As Integer = 1, _
   Optional ByRef minValue As String = "", _
   Optional ByRef maxValue As String = "" _
 )
   If isOptional Then
     Print #fileNo, addTab(2); "'"; addTab(indent + 0); "<union>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(indent + 1); "<simpleType>' || cr ||"

     If minValue = "" Or maxValue = "" Then
       Print #fileNo, addTab(2); "'"; addTab(indent + 2); "<restriction base="""; typeStr; """/>' || cr ||"
     Else
       Print #fileNo, addTab(2); "'"; addTab(indent + 2); "<restriction base="""; typeStr; """>' || cr ||"
       Print #fileNo, addTab(2); "'"; addTab(indent + 3); "<minInclusive value="""; minValue; """/>' || cr ||"
       Print #fileNo, addTab(2); "'"; addTab(indent + 3); "<maxInclusive value="""; maxValue; """/>' || cr ||"
       Print #fileNo, addTab(2); "'"; addTab(indent + 2); "</restriction>' || cr ||"
     End If

     Print #fileNo, addTab(2); "'"; addTab(indent + 1); "</simpleType>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(indent + 1); "<simpleType>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(indent + 2); "<restriction base=""string"">' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(indent + 3); "<maxLength value=""0""/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(indent + 2); "</restriction>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(indent + 1); "</simpleType>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(indent + 0); "</union>' || cr ||"
   Else
     If minValue = "" Or maxValue = "" Then
       Print #fileNo, addTab(2); "'"; addTab(indent + 0); "<restriction base="""; typeStr; """/>' || cr ||"
     Else
       Print #fileNo, addTab(2); "'"; addTab(indent + 0); "<restriction base="""; typeStr; """>' || cr ||"
       Print #fileNo, addTab(2); "'"; addTab(indent + 1); "<minInclusive value="""; minValue; """/>' || cr ||"
       Print #fileNo, addTab(2); "'"; addTab(indent + 1); "<maxInclusive value="""; maxValue; """/>' || cr ||"
       Print #fileNo, addTab(2); "'"; addTab(indent + 0); "</restriction>' || cr ||"
     End If
   End If
 End Sub
 
 Private Sub printSimpleTypeForXML( _
   dataType As typeId, _
   fileNo As Integer, _
   Optional ByRef sectionName As String, _
   Optional ByRef domainName As String, _
   Optional ByRef minLength As String, _
   Optional ByRef maxLength As String, _
   Optional xscale As Integer, _
   Optional ByRef minValue As String = "", _
   Optional ByRef maxValue As String = "", _
   Optional ByRef attrName As String = "", _
   Optional isOptional As Boolean = False _
 )
   If attrName <> "" Then
     Print #fileNo, addTab(2); "'"; addTab(0); "<simpleType name="""; attrName; IIf(isOptional, "_N", ""); """>' || cr ||"
   Else
     Print #fileNo, addTab(2); "'"; addTab(0); "<simpleType name="""; sectionName; "_"; domainName; IIf(isOptional, "_N", ""); """>' || cr ||"
   End If

   If dataType = etBigInt Then
     printSimpleTypeForXMLCore(fileNo, "long", isOptional)

   ElseIf dataType = etBinVarchar Then
     Print #fileNo, addTab(2); "'"; addTab(1); "<restriction base=""string"">' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(2); "<minLength value=""0""/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(1); "</restriction>' || cr ||"

   ElseIf dataType = etBlob Then
     Print #fileNo, addTab(2); "'"; addTab(1); "<restriction base=""string"">' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(2); "<minLength value=""0""/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(1); "</restriction>' || cr ||"

   ElseIf dataType = etChar Then
     Print #fileNo, addTab(2); "'"; addTab(1); "<restriction base=""string"">' || cr ||"
     If isOptional Or minLength <> "" Then
       Print #fileNo, addTab(2); "'"; addTab(2); "<minLength value="""; CStr(IIf(isOptional, 0, minLength)); """/>' || cr ||"
     End If
     Print #fileNo, addTab(2); "'"; addTab(2); "<maxLength value="""; CStr(maxLength); """/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(1); "</restriction>' || cr ||"

   ElseIf dataType = etBinChar Then
     Print #fileNo, addTab(2); "'"; addTab(1); "<restriction base=""string"">' || cr ||"
     If isOptional Or minLength <> "" Then
       Print #fileNo, addTab(2); "'"; addTab(2); "<minLength value="""; CStr(IIf(isOptional, 0, minLength)); """/>' || cr ||"
     End If
     Print #fileNo, addTab(2); "'"; addTab(2); "<maxLength value="""; CStr(maxLength); """/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(1); "</restriction>' || cr ||"

   ElseIf dataType = etClob Then
     Print #fileNo, addTab(2); "'"; addTab(1); "<restriction base=""string"">' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(2); "<minLength value=""0""/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(1); "</restriction>' || cr ||"

   ElseIf dataType = etDate Then
     printSimpleTypeForXMLCore(fileNo, "date", isOptional)

   ElseIf dataType = etDecimal Then
     printSimpleTypeForXMLCore(fileNo, "decimal", isOptional)

   ElseIf dataType = etDouble Then
     printSimpleTypeForXMLCore(fileNo, "double", isOptional)

   ElseIf dataType = etFloat Then
     printSimpleTypeForXMLCore(fileNo, "float", isOptional)

   ElseIf dataType = etInteger Then
     printSimpleTypeForXMLCore(fileNo, "integer", isOptional)

   ElseIf dataType = etLongVarchar Then
     Print #fileNo, addTab(2); "'"; addTab(1); "<restriction base=""string"">' || cr ||"
     If isOptional Or minLength <> "" Then
       Print #fileNo, addTab(2); "'"; addTab(2); "<minLength value="""; CStr(IIf(isOptional, 0, minLength)); """/>' || cr ||"
     End If
     Print #fileNo, addTab(2); "'"; addTab(2); "<maxLength value="""; CStr(maxLength); """/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(1); "</restriction>' || cr ||"

   ElseIf sectionName = dxnBoolean And domainName = dnBoolean Then
     Print #fileNo, addTab(2); "'"; addTab(1); "<restriction base=""boolean"">' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(2); "<pattern value=""0""/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(2); "<pattern value=""1""/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(1); "</restriction>' || cr ||"

   ElseIf dataType = etSmallint Then
     printSimpleTypeForXMLCore(fileNo, "int", isOptional, , minValue, maxValue)

   ElseIf dataType = etTime Then
     printSimpleTypeForXMLCore(fileNo, "time", isOptional)

   ElseIf dataType = etTimestamp Then
     printSimpleTypeForXMLCore(fileNo, "dateTime", isOptional)

   ElseIf dataType = etVarchar Then
     Print #fileNo, addTab(2); "'"; addTab(1); "<restriction base=""string"">' || cr ||"
     If isOptional Or minLength <> "" Then
       Print #fileNo, addTab(2); "'"; addTab(2); "<minLength value="""; CStr(IIf(isOptional, 0, minLength)); """/>' || cr ||"
     End If
     Print #fileNo, addTab(2); "'"; addTab(2); "<maxLength value="""; CStr(maxLength); """/>' || cr ||"
     Print #fileNo, addTab(2); "'"; addTab(1); "</restriction>' || cr ||"

   End If

   Print #fileNo, addTab(2); "'"; addTab(0); "</simpleType>' || cr ||"
 End Sub
 
 ' ### ENDIF IVK ###
