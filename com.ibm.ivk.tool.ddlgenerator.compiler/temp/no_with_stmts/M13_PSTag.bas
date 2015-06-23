 Attribute VB_Name = "M13_PSTag"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const processingStep = 1
 
 
 Private Sub genPsTagSupportDdlForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim isUserTransactional As Boolean
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
   Dim subclassIdListMandatoryPsTag As String
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim entityInsertable As Boolean
   Dim entityUpdatable As Boolean
   Dim entityDeletable As Boolean
   Dim isCtoAliasCreated As Boolean
   Dim sectionName As String
   Dim sectionIndex As Integer
   Dim noAlias As Boolean
   Dim ignorePsRegVarOnInsertDelete As Boolean
   Dim useSurrogateKey As Boolean
   Dim isChangeLog As Boolean
   Dim expandExpressionsInFtoView As Boolean
   Dim isNotAcmRelated As Boolean

   subclassIdListMandatoryPsTag = ""
   isChangeLog = False

   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Class"
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = g_classes.descriptors(acmEntityIndex).psTagOptional
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       entityInsertable = (g_classes.descriptors(acmEntityIndex).updateMode And eupmInsert)
       entityUpdatable = (g_classes.descriptors(acmEntityIndex).updateMode And eupmUpdate)
       entityDeletable = (g_classes.descriptors(acmEntityIndex).updateMode And eupmDelete)
       isCtoAliasCreated = g_classes.descriptors(acmEntityIndex).isCtoAliasCreated
       noAlias = g_classes.descriptors(acmEntityIndex).noAlias
       ignorePsRegVarOnInsertDelete = g_classes.descriptors(acmEntityIndex).ignPsRegVarOnInsDel
       useSurrogateKey = g_classes.descriptors(acmEntityIndex).useSurrogateKey
       isChangeLog = (UCase(g_classes.descriptors(acmEntityIndex).className) = UCase(clnChangeLog))
       expandExpressionsInFtoView = g_classes.descriptors(acmEntityIndex).expandExpressionsInFtoView
       isNotAcmRelated = g_classes.descriptors(acmEntityIndex).notAcmRelated

       If psTagOptional And g_classes.descriptors(acmEntityIndex).hasSubClass Then
         Dim i As Integer
         For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive)
             If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isPsTagged And Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).psTagOptional Then
               subclassIdListMandatoryPsTag = subclassIdListMandatoryPsTag & IIf(subclassIdListMandatoryPsTag <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).classIdStr & "'"
             End If
         Next i
       End If
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Relationship"
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = False
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       entityInsertable = True
       entityUpdatable = True
       entityDeletable = True
       isCtoAliasCreated = g_relationships.descriptors(acmEntityIndex).isCtoAliasCreated
       noAlias = g_relationships.descriptors(acmEntityIndex).noAlias
       ignorePsRegVarOnInsertDelete = False
       useSurrogateKey = True ' ???? FIXME
       expandExpressionsInFtoView = False
       isNotAcmRelated = g_relationships.descriptors(acmEntityIndex).notAcmRelated
   End If

   Dim poolSupportPsTaggingView As Boolean
   Dim poolSupportPsTaggingTrigger As Boolean
   Dim poolSupportLrt As Boolean
   poolSupportPsTaggingView = True
   poolSupportPsTaggingTrigger = True
   poolSupportLrt = False

   If ddlType = edtPdm And thisPoolIndex > 0 Then
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
       poolSupportPsTaggingView = g_pools.descriptors(thisPoolIndex).supportViewsForPsTag
       poolSupportPsTaggingTrigger = g_pools.descriptors(thisPoolIndex).supportTriggerForPsTag
   End If

   If Not isPsTagged Or isNotAcmRelated Then
     Exit Sub
   End If

   If isUserTransactional And g_genLrtSupport Then
     ' filtering by PS is included in LRT-views
     Exit Sub
   End If

   Dim transformation As AttributeListTransformation

   Dim qualTabName As String
   qualTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen)

   Dim qualViewName As String
   Dim qualViewNameLdm  As String
   Dim filterForPsDpMapping As Boolean
   Dim filterForPsDpMappingExtended As Boolean

   Dim tabQualifier As String
   tabQualifier = UCase(entityShortName)

   If generatePsTaggingView And poolSupportPsTaggingView Then
     ' we need to generate three views
     '   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING (special feature for interfaces / first loop)
     '   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING / current division (special feature for interfaces / second loop)
     '   - one filtering out deleted objects and not filtering for Product Structures in PSDPMAPPING (third loop)

     For i = 1 To 3
       filterForPsDpMapping = (i = 1)
       filterForPsDpMappingExtended = (i = 2)

       If filterForPsDpMapping And Not supportFilteringByPsDpMapping Then
         GoTo NextI
       End If
       If filterForPsDpMappingExtended And Not supportFilteringByPsDpMapping Then
         GoTo NextI
       End If

       qualViewName = _
         genQualViewNameByEntityIndex( _
           acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, , , , _
           "PS" & IIf(filterForPsDpMapping, "_I", IIf(filterForPsDpMappingExtended, "_J", "")) _
         )

       printSectionHeader "View for filtering by Product Structure (PS-tag) on table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", _
                          fileNo, , _
                          IIf(supportFilteringByPsDpMapping, IIf(filterForPsDpMapping Or filterForPsDpMappingExtended, "", "do not ") & "filter by PSDPMAPPING", "")
       Print #fileNo,
       Print #fileNo, "CREATE VIEW"
       Print #fileNo, addTab(1); qualViewName
       Print #fileNo, "("

       genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, _
         edomListNonLrt Or edomListVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone)
 
       Print #fileNo, ")"
       Print #fileNo, "AS"
       Print #fileNo, addTab(0); "("
       Print #fileNo, addTab(1); "SELECT"
 
       initAttributeTransformation transformation, 0, , , , tabQualifier & "."
       ' for MPC's work data pool we resolve Expressions to Factory Work Datra Pool - they may not (yet) exist in MPC
       setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, tabQualifier
       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , _
         False, forGen, edomListNonLrt Or edomValueVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone)
 
       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualTabName; " "; tabQualifier

       If filterForPsDpMapping Or filterForPsDpMappingExtended Then
         If psTagOptional Then
           Print #fileNo, addTab(1); "LEFT OUTER JOIN"
         Else
           Print #fileNo, addTab(1); "INNER JOIN"
         End If
         Print #fileNo, addTab(2); g_qualTabNamePsDpMapping; " PSDPM"
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); tabQualifier; "."; g_anPsOid; " = PSDPM.PSOID"

         If filterForPsDpMappingExtended Then
           If psTagOptional Then
             Print #fileNo, addTab(1); "LEFT OUTER JOIN"
           Else
             Print #fileNo, addTab(1); "INNER JOIN"
           End If
           Print #fileNo, addTab(2); g_qualTabNamePsDpMapping; " PSDPM_SP"
           Print #fileNo, addTab(1); "ON"
           Print #fileNo, addTab(2); "PSDPM.DPSPARTE = PSDPM_SP.DPSPARTE"
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '')"
           Print #fileNo, addTab(4); "OR"
           Print #fileNo, addTab(3); "(PSDPM_SP.PSOID = "; g_activePsOidDdl; ")"
           Print #fileNo, addTab(2); ")"
         End If
       End If

       If filterForPsDpMapping Or filterForPsDpMappingExtended Then
         If psTagOptional Then
           Print #fileNo, addTab(1); "WHERE"
           If isChangeLog Then
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); "("; tabQualifier; "."; g_anPsOid; " IS NULL)"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "1"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); g_qualTabNameProductStructure; " PS,"
             If filterForPsDpMappingExtended Then
               Print #fileNo, addTab(5); g_qualTabNamePsDpMapping; " PSDPM,"
               Print #fileNo, addTab(5); g_qualTabNamePsDpMapping; " PSDPM_SP"
             Else
               Print #fileNo, addTab(5); g_qualTabNamePsDpMapping; " PSDPM"
             End If
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "PS."; g_anOid; " = PSDPM.PSOID"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "PS.PDIDIV_OID = "; tabQualifier; ".DIVISIONOID"
             If filterForPsDpMappingExtended Then
               Print #fileNo, addTab(6); "AND"
               Print #fileNo, addTab(5); "PSDPM.DPSPARTE = PSDPM_SP.DPSPARTE"
               Print #fileNo, addTab(6); "AND"
               Print #fileNo, addTab(5); "("
               Print #fileNo, addTab(6); "("; gc_db2RegVarPsOid; " = '')"
               Print #fileNo, addTab(7); "OR"
               Print #fileNo, addTab(6); "(PSDPM_SP.PSOID = "; g_activePsOidDdl; ")"
               Print #fileNo, addTab(5); ")"
             End If
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(2); ")"
           Else
             Print #fileNo, addTab(2); "("; tabQualifier; "."; g_anPsOid; " IS NULL)"
           End If
           Print #fileNo, addTab(3); "OR"
           If filterForPsDpMappingExtended Then
             Print #fileNo, addTab(2); "(PSDPM_SP.PSOID IS NOT NULL)"
           End If
           If filterForPsDpMapping Then
             Print #fileNo, addTab(2); "(PSDPM.PSOID IS NOT NULL)"
           End If
         End If
       Else
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "("; gc_db2RegVarPsOid; " = '')"

         If usePsFltrByDpMappingForRegularViews Then
           Print #fileNo, addTab(3); "OR"
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '0')"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "("; tabQualifier; "."; g_anPsOid; " IN (SELECT PSOID FROM "; g_qualTabNamePsDpMapping; "))"
           Print #fileNo, addTab(2); ")"
         End If

         If psTagOptional Then
           Print #fileNo, addTab(3); "OR"
           If isChangeLog Then
             Print #fileNo, addTab(2); "(("; tabQualifier; "."; g_anPsOid; " IS NULL) AND EXISTS (SELECT 1 FROM "; g_qualTabNameProductStructure; " PS WHERE PS."; g_anOid; " = "; g_activePsOidDdl; " AND PS.PDIDIV_OID = "; tabQualifier; ".DIVISIONOID))"
             Print #fileNo, addTab(3); "OR"
             Print #fileNo, addTab(2); "( "; tabQualifier; ".DIVISIONOID IS NULL )"
           Else
             Print #fileNo, addTab(2); "("; tabQualifier; "."; g_anPsOid; " IS NULL)"
           End If
         End If

         Print #fileNo, addTab(3); "OR"
         Print #fileNo, addTab(2); "("; tabQualifier; "."; g_anPsOid; " = "; g_activePsOidDdl; ")"
       End If

       Print #fileNo, addTab(0); ")"

       Print #fileNo, gc_sqlCmdDelim

       If ddlType = edtPdm And Not noAlias Then
         qualViewNameLdm = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityIndex, edtLdm, , , forGen, , , , , "PS")
         genAliasDdl sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
           qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, False, False, _
           filterForPsDpMapping, filterForPsDpMappingExtended, "PS-Tag-View """ & sectionName & "." & entityName & """" & _
           IIf(supportFilteringByPsDpMapping, " (" & IIf(filterForPsDpMapping Or filterForPsDpMappingExtended, "", "do not ") & "filter by PSDPMAPPING)", ""), , _
           isUserTransactional, True, True
       End If
 NextI:
     Next i
   End If

   If generatePsTaggingTrigger And poolSupportPsTaggingTrigger And useSurrogateKey Then
     Dim qualTriggerName As String

     Dim objSuffix As String
     For i = 1 To 3
       filterForPsDpMapping = (i = 1)
       filterForPsDpMappingExtended = (i = 2)

       If filterForPsDpMapping And Not supportFilteringByPsDpMapping Then
         GoTo NextII
       End If
       If filterForPsDpMappingExtended And Not supportFilteringByPsDpMapping Then
         GoTo NextII
       End If

       objSuffix = IIf(filterForPsDpMapping, "_I", IIf(filterForPsDpMappingExtended, "_J", ""))

       qualViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, , , , "PS" & objSuffix)
 
       ' ####################################################################################################################
       ' #    INSERT Trigger
       ' ####################################################################################################################

       qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "PS_INS" & objSuffix)

       printSectionHeader "Insert-Trigger supporting tagging by Product Structure (PS-tag) on table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE TRIGGER"
       Print #fileNo, addTab(1); qualTriggerName
       Print #fileNo, addTab(0); "INSTEAD OF INSERT ON"
       Print #fileNo, addTab(1); qualViewName
       Print #fileNo, addTab(0); "REFERENCING"
       Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
       Print #fileNo, addTab(0); "FOR EACH ROW"
       Print #fileNo, addTab(0); "BEGIN ATOMIC"

       If Not entityInsertable And generateUpdatableCheckInUpdateTrigger Then
         genSignalDdl "insertNotAllowed", fileNo, 1, entityName
       Else
         genProcSectionHeader fileNo, "declare variables"
         If isChangeLog Then
           genVarDecl fileNo, "v_rowCount", "INTEGER", "NULL"
           genVarDecl fileNo, "v_now", "TIMESTAMP", "NULL"
         End If
         genSigMsgVarDecl fileNo

         ' note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar' and 'v_psOid'
         genPsCheckDdlForInsertDelete fileNo, gc_newRecordName & "." & g_anPsOid, ddlType, thisOrgIndex, _
           ignorePsRegVarOnInsertDelete, psTagOptional, , False, "v_psOidRecord", "v_psOidRegVar", "v_psOid", True, qualViewName, gc_newRecordName & "." & g_anOid

         genDb2RegVarCheckDdl fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1

         If isChangeLog Then
           genProcSectionHeader fileNo, "determine current timestamp"
           Print #fileNo, addTab(1); "SET v_now = CURRENT TIMESTAMP;"
         End If

         If subclassIdListMandatoryPsTag <> "" Then
           Print #fileNo,
           Print #fileNo, addTab(1); "-- for all subclasses with optional PS-tag do not set '"; g_anPsOid; "'"
           Print #fileNo, addTab(1); "IF "; gc_newRecordName; "."; g_anCid; " NOT IN ("; subclassIdListMandatoryPsTag; ") THEN"
           Print #fileNo, addTab(2); "SET v_psOid = v_psOidRecord;"
           Print #fileNo, addTab(1); "END IF;"
         End If

         Print #fileNo,
         Print #fileNo, addTab(1); "INSERT INTO"
         Print #fileNo, addTab(2); qualTabName
         Print #fileNo, addTab(1); "("

         genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomListNonLrt

         Print #fileNo, addTab(1); ")"
         Print #fileNo, addTab(1); "VALUES"
         Print #fileNo, addTab(1); "("

         initAttributeTransformation transformation, 3, , , , gc_newRecordName & "."
         setAttributeMapping transformation, 1, conPsOid, "v_psOid"
         If isChangeLog Then
           setAttributeMapping transformation, 2, conCreateTimestamp, "v_now"
           setAttributeMapping transformation, 3, conLastUpdateTimestamp, "v_now"
         Else
           setAttributeMapping transformation, 2, conCreateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anCreateTimestamp & ", CURRENT TIMESTAMP)"
           setAttributeMapping transformation, 3, conLastUpdateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anLastUpdateTimestamp & ", CURRENT TIMESTAMP)"
         End If

         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt

         Print #fileNo, addTab(1); ");"

         If isChangeLog Then
             If Not ((g_classes.descriptors(g_classIndexChangeLogStatus).specificToPool > 0 And g_classes.descriptors(g_classIndexChangeLogStatus).specificToPool <> g_pools.descriptors(thisPoolIndex).id) Or (g_classes.descriptors(g_classIndexChangeLogStatus).specificToOrgId > 0 And g_classes.descriptors(g_classIndexChangeLogStatus).specificToOrgId <> g_orgs.descriptors(thisOrgIndex).id)) Then
               Dim qualTabNameChangelogStatus As String
               qualTabNameChangelogStatus = genQualTabNameByClassIndex(g_classIndexChangeLogStatus, ddlType, thisOrgIndex, thisPoolIndex)

               genProcSectionHeader fileNo, "keep track of last update timestamp of changelog"

               Print #fileNo, addTab(1); "UPDATE"
               Print #fileNo, addTab(2); qualTabNameChangelogStatus
               Print #fileNo, addTab(1); "SET"
               Print #fileNo, addTab(2); "LASTCOMMITTIME = v_now"
               Print #fileNo, addTab(1); "WHERE"
               Print #fileNo, addTab(2); "COALESCE("; g_anPsOid; ", -1) = COALESCE(v_psOid, -1)"
               Print #fileNo, addTab(3); "AND"
               Print #fileNo, addTab(2); "COALESCE(DIVISIONOID, -1) = COALESCE("; gc_newRecordName; ".DIVISIONOID, -1)"
               Print #fileNo, addTab(3); "AND"
               Print #fileNo, addTab(2); "WITHLRTCONTEXT = ( CASE WHEN "; gc_newRecordName; ".LRTOID IS NULL THEN "; gc_dbFalse; " ELSE "; gc_dbTrue; " END )"
               Print #fileNo, addTab(1); ";"
               Print #fileNo,
               Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
               Print #fileNo,
               Print #fileNo, addTab(1); "IF v_rowCount = 0 THEN"
               Print #fileNo, addTab(2); "INSERT INTO"
               Print #fileNo, addTab(3); qualTabNameChangelogStatus
               Print #fileNo, addTab(2); "("
               Print #fileNo, addTab(3); g_anPsOid; ", DIVISIONOID, LASTCOMMITTIME, WITHLRTCONTEXT"
               Print #fileNo, addTab(2); ")"
               Print #fileNo, addTab(2); "VALUES"
               Print #fileNo, addTab(2); "("
               Print #fileNo, addTab(3); "v_psOid, "; gc_newRecordName; ".DIVISIONOID, v_now, ( CASE WHEN "; gc_newRecordName; ".LRTOID IS NULL THEN "; gc_dbFalse; " ELSE "; gc_dbTrue; " END )"
               Print #fileNo, addTab(2); ");"
               Print #fileNo, addTab(1); "END IF;"
             End If
         End If
       End If
       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim

       ' ####################################################################################################################
       ' #    UPDATE Trigger
       ' ####################################################################################################################

       qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "PS_UPD" & objSuffix)

       printSectionHeader "Update-Trigger supporting tagging by Product Structure (PS-tag) on table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE TRIGGER"
       Print #fileNo, addTab(1); qualTriggerName
       Print #fileNo, addTab(0); "INSTEAD OF UPDATE ON"
       Print #fileNo, addTab(1); qualViewName
       Print #fileNo, addTab(0); "REFERENCING"
       Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
       Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
       Print #fileNo, addTab(0); "FOR EACH ROW"
       Print #fileNo, addTab(0); "BEGIN ATOMIC"

       If Not entityUpdatable And generateUpdatableCheckInUpdateTrigger Then
         genSignalDdl "updateNotAllowed", fileNo, 1, entityName
       Else
         genProcSectionHeader fileNo, "declare variables"
         genSigMsgVarDecl fileNo

         ' note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar', 'v_psOid'
         genPsCheckDdlForUpdate fileNo, gc_oldRecordName & "." & g_anPsOid, gc_newRecordName & "." & g_anPsOid, ddlType, thisOrgIndex, _
            psTagOptional, , False, , , , qualViewName, gc_oldRecordName & "." & g_anOid

         genDb2RegVarCheckDdl fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1

         If subclassIdListMandatoryPsTag <> "" Then
           Print #fileNo,
           Print #fileNo, addTab(1); "-- for all subclasses with optional PS-tag do not set '"; g_anPsOid; "'"
           Print #fileNo, addTab(1); "IF "; gc_newRecordName; "."; g_anCid; " NOT IN ("; subclassIdListMandatoryPsTag; ") THEN"
           Print #fileNo, addTab(2); "SET v_psOid = v_psOidRecord;"
           Print #fileNo, addTab(1); "END IF;"
         End If

         Print #fileNo,
         Print #fileNo, addTab(1); "UPDATE"
         Print #fileNo, addTab(2); qualTabName
         Print #fileNo, addTab(1); "SET"
         Print #fileNo, addTab(1); "("

         genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomListNonLrt

         Print #fileNo, addTab(1); ")"
         Print #fileNo, addTab(1); "="
         Print #fileNo, addTab(1); "("

         initAttributeTransformation transformation, 2, , , , gc_newRecordName & "."
         setAttributeMapping transformation, 1, conPsOid, "v_psOid"
         setAttributeMapping transformation, 2, conLastUpdateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anLastUpdateTimestamp & ", CURRENT TIMESTAMP)"

         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt

         Print #fileNo, addTab(1); ")"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); g_anOid; " = "; gc_oldRecordName; "."; g_anOid
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); conPsOid; " = v_psOid"
         Print #fileNo, addTab(1); ";"
       End If
       Print #fileNo, "END"
       Print #fileNo, gc_sqlCmdDelim

       ' ####################################################################################################################
       ' #    DELETE Trigger
       ' ####################################################################################################################

       qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "PS_DEL" & objSuffix)

       printSectionHeader "Delete-Trigger supporting tagging by Product Structure (PS-tag) on table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE TRIGGER"
       Print #fileNo, addTab(1); qualTriggerName
       Print #fileNo, addTab(0); "INSTEAD OF DELETE ON"
       Print #fileNo, addTab(1); qualViewName
       Print #fileNo, addTab(0); "REFERENCING"
       Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
       Print #fileNo, addTab(0); "FOR EACH ROW"
       Print #fileNo, addTab(0); "BEGIN ATOMIC"

       If Not entityDeletable And generateUpdatableCheckInUpdateTrigger Then
         genSignalDdl "deleteNotAllowed", fileNo, 1, entityName
       Else
         genProcSectionHeader fileNo, "declare variables"
         genSigMsgVarDecl fileNo

         ' note: this procedure declares variables 'v_psOidRecord' and 'v_psOidRegVar'
         genPsCheckDdlForInsertDelete fileNo, gc_oldRecordName & "." & g_anPsOid, ddlType, thisOrgIndex, ignorePsRegVarOnInsertDelete, _
           psTagOptional, , False, , , "", , qualViewName, gc_oldRecordName & "." & g_anOid

         genDb2RegVarCheckDdl fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1

         Print #fileNo,
         Print #fileNo, addTab(1); "DELETE FROM"
         Print #fileNo, addTab(2); qualTabName
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); g_anOid; " = "; gc_oldRecordName; "."; g_anOid
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); conPsOid; " = "; gc_oldRecordName; "."; conPsOid
         Print #fileNo, addTab(1); ";"
       End If
       Print #fileNo, "END"
       Print #fileNo, gc_sqlCmdDelim
 NextII:
     Next i
   End If
 End Sub
 
 
 Sub genPsTagSupportDdlForClass( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   genPsTagSupportDdlForEntity classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen
 End Sub
 
 
 Sub genPsTagSupportDdlForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   genPsTagSupportDdlForEntity thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen
 End Sub
 
 ' ### ENDIF IVK ###
 
 
