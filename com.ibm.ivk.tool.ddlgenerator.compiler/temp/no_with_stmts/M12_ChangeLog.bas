 Attribute VB_Name = "M12_ChangeLog"
 Option Explicit
 
 Private Const pc_tempTabNameChangeLogCte = "SESSION.cte_bas"
 
 Private Const pc_tempTabNameChangeLogAc = "SESSION.ChangeLog_AC"
 
 Global Const clNlPrioAggHead = 0
 Global Const clNlPrioNonAggHead = 100
 Global Const clNlPrioOther = 200
 
 Enum ChangeLogColumnType
   clValueTypeInteger = 1
   clValueTypeBoolean = 2
   clValueTypeTimeStamp = 3
   clValueTypeString = 4
   clValueTypeDecimal = 5
   clValueTypeBigInteger = 6
   clValueTypeDate = 7
 End Enum
 
 Enum ChangeLogMode
   eclLrt = 0
 ' ### IF IVK ###
   eclSetProd = 1
 ' ### ENDIF IVK ###
   eclPubUpdate = 2
 ' ### IF IVK ###
   eclPubMassUpdate = 3
 ' ### ENDIF IVK ###
 End Enum
 
 Function isClAttrCat( _
   columnCategory As AttrCategory, _
   includeSetProdMeta As Boolean _
 ) As Boolean
   If columnCategory And eacCid Then
     isClAttrCat = False
   ElseIf columnCategory And eacOid Then
     isClAttrCat = False
 ' ### IF IVK ###
   ElseIf columnCategory And eacPsOid Then
     isClAttrCat = False
   ElseIf columnCategory And eacGroupId Then
     isClAttrCat = False
 ' ### ENDIF IVK ###
   ElseIf columnCategory And eacFkOidParent Then
     isClAttrCat = False
 ' ### IF IVK ###
   ElseIf includeSetProdMeta And (columnCategory And eacSetProdMeta) Then
     isClAttrCat = True
   ElseIf columnCategory And eacNationalBool Then
     isClAttrCat = True
 ' ### ENDIF IVK ###
   ElseIf columnCategory And eacLrtMeta Then
     isClAttrCat = False
   ElseIf columnCategory And eacRegular Then
     isClAttrCat = True
   ElseIf columnCategory And eacFkOid Then
     isClAttrCat = True
 ' ### IF IVK ###
   ElseIf columnCategory And eacExpression Then
     isClAttrCat = True
   ElseIf columnCategory And eacFkOidExpression Then
     isClAttrCat = True
 ' ### ENDIF IVK ###
   Else
     isClAttrCat = False
   End If
 End Function
 
 
 Function genClModeDescription( _
   ByRef clMode As ChangeLogMode _
 ) As String
   If clMode = eclLrt Then
     genClModeDescription = "LRT"
 ' ### IF IVK ###
   ElseIf clMode = eclSetProd Then
     genClModeDescription = "SetProductive"
 ' ### ENDIF IVK ###
   ElseIf clMode = eclPubUpdate Then
     genClModeDescription = "public update"
 ' ### IF IVK ###
   ElseIf clMode = eclPubMassUpdate Then
     genClModeDescription = "public mass update"
 ' ### ENDIF IVK ###
   Else
     genClModeDescription = "-unsupported-"
   End If
 End Function
 
 
 Function attrTypeMapsToClColType( _
   ByRef attrTypeId As typeId, _
   ByRef clColTypeId As ChangeLogColumnType _
 ) As Boolean
   If clColTypeId = clValueTypeInteger Then
     attrTypeMapsToClColType = (attrTypeId = etInteger Or attrTypeId = etSmallint)
   ElseIf clColTypeId = clValueTypeBoolean Then
     attrTypeMapsToClColType = (attrTypeId = etBoolean)
   ElseIf clColTypeId = clValueTypeTimeStamp Then
     attrTypeMapsToClColType = (attrTypeId = etTimestamp)
   ElseIf clColTypeId = clValueTypeDate Then
     attrTypeMapsToClColType = (attrTypeId = etDate)
   ElseIf clColTypeId = clValueTypeBigInteger Then
     attrTypeMapsToClColType = (attrTypeId = etBigInt)
   ElseIf clColTypeId = clValueTypeDecimal Then
     attrTypeMapsToClColType = (attrTypeId = etDecimal)
   ElseIf clColTypeId = clValueTypeString Then
     attrTypeMapsToClColType = True
   End If
 End Function
 
 Function getClColTypeByAttrType( _
   ByRef attrTypeId As typeId _
 ) As ChangeLogColumnType
   If attrTypeId = etBoolean Then
     getClColTypeByAttrType = clValueTypeBoolean
   ElseIf attrTypeId = etTimestamp Then
     getClColTypeByAttrType = clValueTypeTimeStamp
   ElseIf attrTypeId = etDate Then
     getClColTypeByAttrType = clValueTypeDate
   ElseIf attrTypeId = etBigInt Then
     getClColTypeByAttrType = clValueTypeBigInteger
   ElseIf attrTypeId = etInteger Or attrTypeId = etSmallint Then
     getClColTypeByAttrType = clValueTypeInteger
   ElseIf attrTypeId = etDecimal Or attrTypeId = etDouble Or attrTypeId = etFloat Then
     getClColTypeByAttrType = clValueTypeDecimal
   Else
     getClColTypeByAttrType = clValueTypeString
   End If
 End Function
 
 
 ' ### IF IVK ###
 Sub genDdlForTempTablesChangeLog( _
   fileNo As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ddlType As DdlTypeId, _
   Optional indent As Integer = 1, _
   Optional includeNlTab As Boolean = True, _
   Optional includeAeTab As Boolean = False, _
   Optional includeStTab As Boolean = False, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False, _
   Optional prioColumnInNlTab As Boolean = True _
 )
 ' ### ELSE IVK ###
 'Sub genDdlForTempTablesChangeLog( _
 ' fileNo As Integer, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' ddlType As DdlTypeId, _
 ' Optional indent As Integer = 1, _
 ' Optional includeNlTab As Boolean = True, _
 ' Optional includeAeTab As Boolean = False, _
 ' Optional withReplace As Boolean = False, _
 ' Optional onCommitPreserve As Boolean = False, _
 ' Optional onRollbackPreserve As Boolean = False, _
 ' Optional prioColumnInNlTab As Boolean = True _
 ')
 ' ### ENDIF IVK ###
   Dim qualTabNameChangeLog As String
   qualTabNameChangeLog = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex)

   genProcSectionHeader fileNo, "temporary table for ChangeLog", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog
   Print #fileNo, addTab(indent + 0); "LIKE"
   Print #fileNo, addTab(indent + 1); qualTabNameChangeLog
   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve

   If includeNlTab Then
     Dim qualTabNameChangeLogNl As String
     qualTabNameChangeLogNl = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, , , , True)

     genProcSectionHeader fileNo, "temporary NL-Text table for ChangeLog", indent
     Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogNl
     If prioColumnInNlTab Then
       Print #fileNo, addTab(indent + 0); "("
       printComment "the same NL-text column may be filled from different sources - column """ & conTmpPrio & """ defines priorities", fileNo, edomDeclNonLrt, indent + 1
       Print #fileNo, addTab(indent + 1); genAttrDeclByDomain(conTmpPrio, cosnTmpPrio, eavtDomain, _
                                          g_domainIndexTmpPrio, eactClass, g_classIndexChangeLog, "DEFAULT " & CStr(clNlPrioOther), , ddlType, _
                                          , edomDeclNonLrt Or edomNoDdlComment, eacRegular, , 0, False)

       genNlsAttrDeclsForEntity g_classIndexChangeLog, eactClass, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, indent + 1, , , edomDeclNonLrt Or edomNoDdlComment Or edomNoSpecifics
       Print #fileNo, addTab(indent + 0); ")"
     Else
       Print #fileNo, addTab(indent + 0); "LIKE"
       Print #fileNo, addTab(indent + 1); qualTabNameChangeLogNl
     End If

     genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
   End If
 ' ### IF IVK ###

   If includeStTab Then
     genProcSectionHeader fileNo, "temporary table for ChangeLog - status", indent
     Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogStatus
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "psOid            "; g_dbtOid; ","
     Print #fileNo, addTab(indent + 1); "divisionOid      "; g_dbtOid
     Print #fileNo, addTab(indent + 0); ")"

     genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
   End If

   If includeAeTab Then
     genProcSectionHeader fileNo, "temporary table for optimized ChangeLog", indent
     Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(indent + 1); pc_tempTabNameChangeLogCte
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "objectid          "; g_dbtOid; ","
     Print #fileNo, addTab(indent + 1); "dbcolumnname     "; g_dbtDbColumnName; ","
     Print #fileNo, addTab(indent + 1); "switch          "; g_dbtBoolean;
     Print #fileNo, addTab(indent + 0); ")"
     genDdlForTempTableDeclTrailer fileNo, indent, True, onCommitPreserve, onRollbackPreserve
   End If
 ' ### ENDIF IVK ###
 End Sub
 
 
 ' ### IF IVK ###
 Sub genDdlForTempChangeLogSummary( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional forOrg As Boolean = False, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader fileNo, "temporary table for ChangeLog-Summary", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); IIf(forOrg, gc_tempTabNameChangeLogOrgSummary, gc_tempTabNameChangeLogSummary)
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "objectId        "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "entityType      "; g_dbtEntityType; ","
   Print #fileNo, addTab(indent + 1); "entityId        "; g_dbtEntityId; ","
   Print #fileNo, addTab(indent + 1); "ahClassId       "; g_dbtEntityId; ","
   Print #fileNo, addTab(indent + 1); "ahObjectId      "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "aggregateType   "; g_dbtEntityId; ","

   If forOrg Then
     Print #fileNo, addTab(indent + 1); "ahIsCreated     "; g_dbtBoolean; ","
     Print #fileNo, addTab(indent + 1); "ahIsUpdated     "; g_dbtBoolean; ","
     Print #fileNo, addTab(indent + 1); "ahIsDeleted     "; g_dbtBoolean; ","
   End If

   Print #fileNo, addTab(indent + 1); "isCreated       "; g_dbtBoolean; ","
   Print #fileNo, addTab(indent + 1); "isUpdated       "; g_dbtBoolean; ","
   Print #fileNo, addTab(indent + 1); "isDeleted       "; g_dbtBoolean
   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 End Sub
 
 
 Sub genDdlForTempFtoClgGenericAspect( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False, _
   Optional includeSr0ContextTabOrg As Boolean = True, _
   Optional includeSr0ContextTabCmp As Boolean = True _
 )
   genProcSectionHeader fileNo, "temporary table for Countries managed by 'this Organization'", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameManagedCountry
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "countryOid     "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 
   genProcSectionHeader fileNo, "temporary table for Countries relevant for 'this Organization'", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameRelevantCountry
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "countryOid     "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve

   genProcSectionHeader fileNo, "temporary table for CountryId-Lists involving Countries relevant for 'this Organization'", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameRelevantCountryIdList
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "idListOid      "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve

   genProcSectionHeader fileNo, "temporary table for mapping of CountryId-Lists to Countries managed by 'this Organization'", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameRelevantCountryIdXRef
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "idListOid      "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "countryOid     "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve

   If includeSr0ContextTabCmp Then
     genProcSectionHeader fileNo, "temporary table for SR0-Contexts (factory) revelant for 'this Organization'", indent
     Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameFtoClSr0ContextFac
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "sr0Context     VARCHAR(50)"
     Print #fileNo, addTab(indent + 0); ")"

     genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
   End If

   If includeSr0ContextTabOrg Then
     genProcSectionHeader fileNo, "temporary table for SR0-Contexts (MPC) revelant for 'this Organization", indent
     Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameFtoClSr0ContextOrg
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "sr0Context     VARCHAR(50)"
     Print #fileNo, addTab(indent + 0); ")"

     genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
   End If
 End Sub
 
 
 Sub genDdlForTempImplicitChangeLogSummary( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional forOrg As Boolean = False, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   If forOrg Then
     genProcSectionHeader fileNo, "temporary table for implicit changes", indent
     Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogImplicitChanges
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "aggregateType   "; g_dbtEntityId; ","
     Print #fileNo, addTab(indent + 1); "ahClassId       "; g_dbtEntityId; ","
     Print #fileNo, addTab(indent + 1); "ahObjectId      "; g_dbtOid; ","
     Print #fileNo, addTab(indent + 1); "isToBeCreated   "; g_dbtBoolean; ","
     Print #fileNo, addTab(indent + 1); "isToBeDeleted   "; g_dbtBoolean
     Print #fileNo, addTab(indent + 0); ")"

     genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
   End If
 End Sub
 
 
 Function genAddNlTextChangeLogDdlForIndividualAttrs( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef dbAcmEntityType As String, _
   ByRef entityIdStrList As String, _
   ByRef gc_tempTabNameChangeLog As String, _
   ByRef gc_tempTabNameChangeLogNl As String, _
   ByRef qualRefNlTabName As String, _
   ByRef oidRefAttrName As String, _
   ByRef qualAggHeadRefNlTabName As String, _
   ByRef aggHeadOidRefAttrName As String, _
   ByRef attrRefs As AttrDescriptorRefs, _
   ByRef relRefs As RelationshipDescriptorRefs, _
   ByRef forGen As Boolean, _
   ByRef lrtOidFilterVar As String, _
   ByRef psOidFilterVar As String, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef includeChangeComment As Boolean = False, _
   Optional ByRef includeRegularAttrs As Boolean = True, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByRef indent As Integer = 1, _
   Optional skipNl As Boolean = False _
 ) As Integer
 ' ### ELSE IVK ###
 'Function genAddNlTextChangeLogDdlForIndividualAttrs( _
 ' fileNo As Integer, _
 ' ByRef acmEntityIndex As Integer, _
 ' ByRef acmEntityType As AcmAttrContainerType, _
 ' ByRef dbAcmEntityType As String, _
 ' ByRef entityIdStrList As String, _
 ' ByRef gc_tempTabNameChangeLog As String, _
 ' ByRef gc_tempTabNameChangeLogNl As String, _
 ' ByRef qualRefNlTabName As String, _
 ' ByRef oidRefAttrName As String, _
 ' ByRef qualAggHeadRefNlTabName As String, _
 ' ByRef aggHeadOidRefAttrName As String, _
 ' ByRef attrRefs As AttrDescriptorRefs, _
 ' ByRef relRefs As RelationshipDescriptorRefs, _
 ' ByRef forGen As Boolean, _
 ' ByRef lrtOidFilterVar As String, _
 ' Optional ByVal thisOrgIndex As Integer = -1, _
 ' Optional ByVal thisPoolIndex As Integer = -1, _
 ' Optional ByRef includeChangeComment As Boolean = False, _
 ' Optional ByRef includeRegularAttrs As Boolean = True, _
 ' Optional ByRef ddlType As DdlTypeId = edtLdm, _
 ' Optional ByRef indent As Integer = 1, _
 ' Optional skipNl As Boolean = False _
 ') As Integer
 ' ### ENDIF IVK ###
   genAddNlTextChangeLogDdlForIndividualAttrs = 0

   Dim relRefsAh As RelationshipDescriptorRefs
   Dim aggHeadClassIndex As Integer
   Dim useMqtToImplementLrt As Boolean
   Dim clMode As ChangeLogMode
   Dim isAggHead As Boolean
   Dim implicitelyGenChangeComment As Boolean
   Dim hasNl As Boolean
   Dim useClassIdFilter As Boolean
 ' ### IF IVK ###
   Dim hasNoIdentity As Boolean
   Dim enforceLrtChangeComment As Boolean
   Dim isPsTagged As Boolean
 ' ### ENDIF IVK ###

   useClassIdFilter = True
 ' ### IF IVK ###
   clMode = IIf(lrtOidFilterVar = "", eclSetProd, eclLrt)
 ' ### ELSE IVK ###
 '  clMode = eclLrt
 ' ### ENDIF IVK ###
 
   If acmEntityType = eactClass Then
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       useMqtToImplementLrt = g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
       isAggHead = g_classes.descriptors(acmEntityIndex).isAggHead
       implicitelyGenChangeComment = g_classes.descriptors(acmEntityIndex).implicitelyGenChangeComment
       hasNl = g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses

 ' ### IF IVK ###
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       enforceLrtChangeComment = g_classes.descriptors(acmEntityIndex).enforceLrtChangeComment
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       useClassIdFilter = useClassIdFilter And (UCase(g_classes.descriptors(acmEntityIndex).className) <> UCase(clnGenericAspect)) ' list of class-IDs too long
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       useMqtToImplementLrt = g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
       isAggHead = False
 ' ### IF IVK ###
       hasNoIdentity = False
       enforceLrtChangeComment = False
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
   Else
     Exit Function
   End If
 
   Dim i As Integer
   Dim j As Integer
   Dim columnName As String
   Dim columnMaxLengthSrc As Integer
   Dim columnMaxLengthDst As Integer
   Dim addNewLine As Boolean
   addNewLine = False

   Dim isolationLevelSuffix As String
   isolationLevelSuffix = "WITH UR" ' with DB2 V8 it is advised to use uncommitted read for NL-Texts to avoid lock-conflicts

   Dim numAttrsFound As Integer
   numAttrsFound = 0
 
   Dim tabVarNl As String
   Dim tabVarGen As String
 
 ' ### IF IVK ###
   If includeChangeComment And Not forGen And ((isAggHead And (implicitelyGenChangeComment Or hasNl)) Or (enforceLrtChangeComment And clMode = eclLrt)) Then
 ' ### ELSE IVK ###
 ' If includeChangeComment And Not forGen And isAggHead Then
 ' ### ENDIF IVK ###
     columnName = g_anChangeComment

     columnMaxLengthSrc = getDbMaxDataTypeLengthByDomainName(dxnChangeComment, dnChangeComment)
     columnMaxLengthDst = getMaxDbAttributeLengthByNameAndEntityIndex(columnName, eactClass, g_classIndexChangeLog)

     genProcSectionHeader fileNo, "add NL-Text-Column """ & columnName & """ to changelog entries", indent, skipNl

     tabVarNl = IIf(isAggHead, "AHNL", "NL")

     Print #fileNo, addTab(indent + 0); "INSERT INTO"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogNl
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "oid,"
     Print #fileNo, addTab(indent + 1); "CLG_OID,"
     Print #fileNo, addTab(indent + 1); "language_Id,"
     Print #fileNo, addTab(indent + 1); columnName; ","
     Print #fileNo, addTab(indent + 1); "PRIO,"
 ' ### IF IVK ###
     If usePsTagInNlTextTables Then
       Print #fileNo, addTab(indent + 1); g_anPsOid; ","
     End If
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(indent + 1); g_anVersionId
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "SELECT"
     Print #fileNo, addTab(indent + 1); "-1,"
     Print #fileNo, addTab(indent + 1); "L."; g_anOid; ","
     Print #fileNo, addTab(indent + 1); tabVarNl; "."; g_anLanguageId; ","
     If columnMaxLengthSrc <= columnMaxLengthDst Then
       Print #fileNo, addTab(indent + 1); tabVarNl; "."; columnName; ","
     Else
       Print #fileNo, addTab(indent + 1); "RTRIM(LEFT("; tabVarNl; "."; columnName; ","; CStr(columnMaxLengthDst); ")),"
     End If
     Print #fileNo, addTab(indent + 1); CStr(IIf(isAggHead, clNlPrioAggHead, clNlPrioNonAggHead)); ","
 ' ### IF IVK ###
     If usePsTagInNlTextTables Then
       Print #fileNo, addTab(indent + 1); "L."; g_anPsOid; ","
     End If
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(indent + 1); "1"
     Print #fileNo, addTab(indent + 0); "FROM"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog; " L"
     Print #fileNo, addTab(indent + 0); "INNER JOIN"
     If isAggHead Then
       Print #fileNo, addTab(indent + 1); qualAggHeadRefNlTabName; " "; tabVarNl
       Print #fileNo, addTab(indent + 0); "ON"
       Print #fileNo, addTab(indent + 1); tabVarNl; "."; aggHeadOidRefAttrName; " = L.ahObjectId"
     Else
       Print #fileNo, addTab(indent + 1); qualRefNlTabName; " "; tabVarNl
       Print #fileNo, addTab(indent + 0); "ON"
       Print #fileNo, addTab(indent + 1); tabVarNl; "."; oidRefAttrName; " = L.objectId"
     End If
     Print #fileNo, addTab(indent + 0); "WHERE"
     If clMode = eclLrt Then
       Print #fileNo, addTab(indent + 1); tabVarNl; "."; g_anInLrt; " = "; lrtOidFilterVar
       Print #fileNo, addTab(indent + 2); "AND"
     End If
     Print #fileNo, addTab(indent + 1); tabVarNl; "."; columnName; " IS NOT NULL"
     If useClassIdFilter Then
       Print #fileNo, addTab(indent + 2); "AND"
       If isAggHead Then
         Print #fileNo, addTab(indent + 1); "L.ahClassid IN ("; entityIdStrList; ")"
       Else
         Print #fileNo, addTab(indent + 1); "L.entityType = '"; getAcmEntityTypeKey(acmEntityType); "'"
         Print #fileNo, addTab(indent + 2); "AND"
         Print #fileNo, addTab(indent + 1); "L.entityId = "; entityIdStrList
       End If
     End If
     Print #fileNo, addTab(indent + 0); isolationLevelSuffix; ";"

     addNewLine = True
     numAttrsFound = numAttrsFound + 1
   End If

   tabVarNl = "ONL"
   tabVarGen = "OGEN"

   If includeRegularAttrs Then
     For i = 1 To attrRefs.numDescriptors
       If attrRefs.descriptors(i).refType = eadrtAttribute Then
 ' ### IF IVK ###
           If (Not strArrayIsNull(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).mapsToChangeLogAttributes)) And g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isNl And (forGen Or hasNoIdentity) = g_attributes.descriptors(attrRefs.descriptors(i).refIndex).isTimeVarying Then
 ' ### ELSE IVK ###
 '         If (Not strArrayIsNull(.mapsToChangeLogAttributes)) And .isNl And forGen = .isTimeVarying Then
 ' ### ENDIF IVK ###
             For j = LBound(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).mapsToChangeLogAttributes) To UBound(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).mapsToChangeLogAttributes)
               columnName = genAttrName(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).attributeName, ddlType)

               columnMaxLengthSrc = getMaxDbAttributeLengthByNameAndEntityIndex(columnName, acmEntityType, acmEntityIndex)
               columnMaxLengthDst = getMaxDbAttributeLengthByNameAndEntityIndex(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).mapsToChangeLogAttributes(j), eactClass, g_classIndexChangeLog)

               genProcSectionHeader fileNo, "add NL-Text-Column """ & columnName & """ / """ & UCase(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).mapsToChangeLogAttributes(j)) & """ to changelog entries", indent, Not addNewLine

               ' propagate NL-text values to all aggregate elements
               Print #fileNo, addTab(indent + 0); "INSERT INTO"
               Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogNl
               Print #fileNo, addTab(indent + 0); "("
               Print #fileNo, addTab(indent + 1); "oid,"
               Print #fileNo, addTab(indent + 1); "CLG_OID,"
               Print #fileNo, addTab(indent + 1); g_anLanguageId; ","
               Print #fileNo, addTab(indent + 1); UCase(g_attributes.descriptors(attrRefs.descriptors(i).refIndex).mapsToChangeLogAttributes(j)); ","
               Print #fileNo, addTab(indent + 1); "PRIO,"
 ' ### IF IVK ###
               If usePsTagInNlTextTables Then
                 Print #fileNo, addTab(indent + 1); g_anPsOid; ","
               End If
 ' ### ENDIF IVK ###
               Print #fileNo, addTab(indent + 1); g_anVersionId
               Print #fileNo, addTab(indent + 0); ")"
               Print #fileNo, addTab(indent + 0); "SELECT"
               Print #fileNo, addTab(indent + 1); "-1,"
               Print #fileNo, addTab(indent + 1); "L."; g_anOid; ","
               Print #fileNo, addTab(indent + 1); tabVarNl; "."; g_anLanguageId; ","
               If columnMaxLengthSrc <= columnMaxLengthDst Then
                 Print #fileNo, addTab(indent + 1); tabVarNl; "."; columnName; ","
               Else
                 Print #fileNo, addTab(indent + 1); "RTRIM(LEFT("; tabVarNl; "."; columnName; ","; CStr(columnMaxLengthDst); ")),"
               End If
               Print #fileNo, addTab(indent + 1); IIf(isAggHead, "(CASE WHEN " & tabVarNl & "." & oidRefAttrName & " = " & tabVarNl & "." & g_anAhOid & " THEN " & clNlPrioNonAggHead & " ELSE " & clNlPrioAggHead & " END)", CStr(clNlPrioNonAggHead)); ","
 ' ### IF IVK ###
               If usePsTagInNlTextTables Then
                 Print #fileNo, addTab(indent + 1); "L."; g_anPsOid; ","
               End If
 ' ### ENDIF IVK ###
               Print #fileNo, addTab(indent + 1); "1"
               Print #fileNo, addTab(indent + 0); "FROM"
               Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog; " L,"

               If Not isAggHead And forGen Then
                 If clMode = eclLrt Then
                   If useMqtToImplementLrt Then
                     Print #fileNo, addTab(indent + 1); genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True, False); " "; tabVarNl
                   Else
                     Print #fileNo, addTab(indent + 1); "("
 ' ### IF IVK ###
                     Print #fileNo, addTab(indent + 2); "SELECT OID,"; oidRefAttrName; " FROM "; _
                                                        genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, False); " "; _
                                                        "WHERE (("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")) AND ("; g_anIsDeleted; " = 0)"
 ' ### ELSE IVK ###
 '                   Print #fileNo, addTab(indent + 2); "SELECT OID,"; oidRefAttrName; " FROM "; _
 '                                                      genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, False); " "; _
 '                                                      "WHERE ("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")"
 ' ### ENDIF IVK ###
                     Print #fileNo, addTab(indent + 3); "UNION ALL"
                     Print #fileNo, addTab(indent + 2); "SELECT OID,"; oidRefAttrName; " FROM "; _
                                                        genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, False, False); " "; _
                                                        "WHERE ("; g_anInLrt; " = "; lrtOidFilterVar; ")"
                     Print #fileNo, addTab(indent + 1); ") "; tabVarGen; ","
                   End If
                 Else
                   Print #fileNo, addTab(indent + 1); genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , False); " "; tabVarGen; ","
                 End If
               End If

               If clMode = eclLrt Then
                 If useMqtToImplementLrt Then
                   Print #fileNo, addTab(indent + 1); genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, True, True); " "; tabVarNl
                 Else
                   Print #fileNo, addTab(indent + 1); "("
 ' ### IF IVK ###
                   Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; oidRefAttrName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                      genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, True); " "; _
                                                      "WHERE (("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")) AND ("; g_anIsDeleted; " = 0)"
 ' ### ELSE IVK ###
 '                 Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; oidRefAttrName; ","; columnName; ","; g_anLanguageId; " FROM "; _
 '                                                    genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, True); " "; _
 '                                                    "WHERE ("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")"
 ' ### ENDIF IVK ###
                   Print #fileNo, addTab(indent + 3); "UNION ALL"
                   Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; oidRefAttrName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                      genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, False, True); " "; _
                                                      "WHERE ("; g_anInLrt; " = "; lrtOidFilterVar; ")"
                   Print #fileNo, addTab(indent + 1); ") "; tabVarNl
                 End If
               Else
                 Print #fileNo, addTab(indent + 1); genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , True); " "; tabVarNl
               End If
               Print #fileNo, addTab(indent + 0); "WHERE"

               If Not isAggHead And forGen Then
                 Print #fileNo, addTab(indent + 1); tabVarNl; "."; oidRefAttrName; " = "; tabVarGen; "."; g_anOid
                 Print #fileNo, addTab(indent + 2); "AND"
               End If

               If isAggHead Then
                 Print #fileNo, addTab(indent + 1); "(L.ahObjectId = "; tabVarNl; "."; g_anAhOid; ")"
                 If useClassIdFilter Then
                   Print #fileNo, addTab(indent + 2); "AND"
                   Print #fileNo, addTab(indent + 1); "L.ahClassId IN ("; entityIdStrList; ")"
                 End If
               Else
                 Print #fileNo, addTab(indent + 1); "("
                 Print #fileNo, addTab(indent + 2); "(L.objectId = "; tabVarNl; "."; oidRefAttrName; ")"
                 Print #fileNo, addTab(indent + 3); "OR"
                 Print #fileNo, addTab(indent + 2); "(L.objectId = "; tabVarNl; "."; g_anOid; ")"
                 If forGen Then
                   Print #fileNo, addTab(indent + 3); "OR"
                   Print #fileNo, addTab(indent + 2); "(L.objectId = "; tabVarGen; "."; oidRefAttrName; ")"
                 End If
                 Print #fileNo, addTab(indent + 1); ")"
                 If useClassIdFilter Then
                   Print #fileNo, addTab(indent + 2); "AND"
                   Print #fileNo, addTab(indent + 1); "L.entityType = '"; getAcmEntityTypeKey(acmEntityType); "'"
                   Print #fileNo, addTab(indent + 2); "AND"
                   Print #fileNo, addTab(indent + 1); "L.entityId = "; entityIdStrList
                 End If
               End If

               Print #fileNo, addTab(indent + 2); "AND"

               If clMode = eclLrt Then
                 If useMqtToImplementLrt Then
                   Print #fileNo, addTab(indent + 1); "("
 ' ### IF IVK ###
                   Print #fileNo, addTab(indent + 2); "(("; tabVarNl; "."; g_anIsLrtPrivate; " = 0) AND ("; tabVarNl; "."; g_anIsDeleted; " = 0) AND ("; tabVarNl; "."; g_anInLrt; " IS NULL OR "; tabVarNl; "."; g_anInLrt; " <> "; lrtOidFilterVar; "))"
 ' ### ELSE IVK ###
 '                 Print #fileNo, addTab(indent + 2); "(("; tabVarNl; "." ; g_anIsLrtPrivate; " = 0) AND ("; tabVarNl; "."; g_anInLrt; " IS NULL OR "; tabVarNl; "."; g_anInLrt; " <> "; lrtOidFilterVar; "))"
 ' ### ENDIF IVK ###
                   Print #fileNo, addTab(indent + 3); "OR"
                   Print #fileNo, addTab(indent + 2); "(("; tabVarNl; "."; g_anIsLrtPrivate; " = 1) AND ("; tabVarNl; "."; g_anInLrt; " = "; lrtOidFilterVar; "))"
                   Print #fileNo, addTab(indent + 1); ")"
                   Print #fileNo, addTab(indent + 2); "AND"
                 End If
               End If

               Print #fileNo, addTab(indent + 1); tabVarNl; "."; columnName; " IS NOT NULL"
               Print #fileNo, addTab(indent + 0); isolationLevelSuffix; ";"

               addNewLine = True
               numAttrsFound = numAttrsFound + 1
             Next j
           End If
       End If
     Next i

 ' ### IF IVK ###
     Dim referredClassIndex As Integer
     Dim referringClassIndex As Integer
     Dim entityIdStrListForRel As String
     Dim attrIndex As Integer
     entityIdStrListForRel = ""
     Dim relFkAttrName As String
     Dim attrBasePrio As Integer

     If Not forGen Then
       For i = 1 To relRefs.numRefs
           If Not arrayIsNull(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute) And g_relationships.descriptors(relRefs.refs(i).refIndex).reusedRelIndex <= 0 Then
             If relRefs.refs(i).refType = etRight Then
               referredClassIndex = g_relationships.descriptors(relRefs.refs(i).refIndex).leftEntityIndex
               referringClassIndex = g_relationships.descriptors(relRefs.refs(i).refIndex).rightEntityIndex
               relFkAttrName = g_relationships.descriptors(relRefs.refs(i).refIndex).leftFkColName(ddlType)
             Else
               referredClassIndex = g_relationships.descriptors(relRefs.refs(i).refIndex).rightEntityIndex
               referringClassIndex = g_relationships.descriptors(relRefs.refs(i).refIndex).leftEntityIndex
               relFkAttrName = g_relationships.descriptors(relRefs.refs(i).refIndex).rightFkColName(ddlType)
             End If

             Dim refColName As String
             refColName = genSurrogateKeyName(ddlType, g_classes.descriptors(referredClassIndex).shortName)

             If referringClassIndex > 0 Then
               ' determine list of subClassIDs for which this relationship applies
               entityIdStrListForRel = g_classes.descriptors(referringClassIndex).subclassIdStrListNonAbstract
               ' examine reusing relationships
               Dim l As Integer
               For l = 1 To g_relationships.descriptors(relRefs.refs(i).refIndex).reusingRelIndexes.numIndexes
                   addClassIdToList entityIdStrListForRel, IIf(relRefs.refs(i).refType = etRight, g_relationships.descriptors(g_relationships.descriptors(relRefs.refs(i).refIndex).reusingRelIndexes.indexes(l)).rightEntityIndex, g_relationships.descriptors(g_relationships.descriptors(relRefs.refs(i).refIndex).reusingRelIndexes.indexes(l)).leftEntityIndex), True
               Next l
             End If

             For j = LBound(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute) To UBound(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute)
               attrBasePrio = g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute(j).prio
               attrIndex = getAttributeIndexByName(g_classes.descriptors(referredClassIndex).sectionName, g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute(j).mapTo)

               If attrIndex > 0 Then
                   If g_attributes.descriptors(attrIndex).isNl Then
                     columnName = genAttrName(g_attributes.descriptors(attrIndex).attributeName, ddlType)
                     columnMaxLengthSrc = getMaxDbAttributeLengthByNameAndEntityIndex(columnName, g_attributes.descriptors(attrIndex).cType, g_attributes.descriptors(attrIndex).acmEntityIndex)
                     columnMaxLengthDst = getMaxDbAttributeLengthByNameAndEntityIndex(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute(j).mapFrom, eactClass, g_classIndexChangeLog)

                     Dim tabVarRef As String
                       refColName = genSurrogateKeyName(ddlType, g_classes.descriptors(g_classes.descriptors(referredClassIndex).orMappingSuperClassIndex).shortName)
                       tabVarRef = UCase(g_classes.descriptors(g_classes.descriptors(referringClassIndex).orMappingSuperClassIndex).shortName)

                     If g_attributes.descriptors(attrIndex).isTimeVarying Then
                         tabVarNl = UCase(g_classes.descriptors(g_classes.descriptors(referredClassIndex).orMappingSuperClassIndex).shortName) & "GNL"
                         tabVarGen = UCase(g_classes.descriptors(g_classes.descriptors(referredClassIndex).orMappingSuperClassIndex).shortName) & "G"

                       genProcSectionHeader fileNo, "add NL-Text-Column """ & _
                           UCase(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute(j).mapFrom) & _
                           """ (" & UCase(g_attributes.descriptors(attrIndex).attributeName) & " @ " & g_classes.descriptors(referredClassIndex).className & " (TV)) to changelog entries", indent
 
                       Print #fileNo, addTab(indent + 0); "INSERT INTO"
                       Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogNl
                       Print #fileNo, addTab(indent + 0); "("
                       Print #fileNo, addTab(indent + 1); g_anOid; ","
                       Print #fileNo, addTab(indent + 1); "CLG_OID,"
                       Print #fileNo, addTab(indent + 1); g_anLanguageId; ","
                       Print #fileNo, addTab(indent + 1); UCase(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute(j).mapFrom); ","
                       Print #fileNo, addTab(indent + 1); "PRIO,"
                       If usePsTagInNlTextTables Then
                         Print #fileNo, addTab(indent + 1); g_anPsOid; ","
                       End If
                       Print #fileNo, addTab(indent + 1); g_anVersionId
                       Print #fileNo, addTab(indent + 0); ")"
                       Print #fileNo, addTab(indent + 0); "SELECT"
                       Print #fileNo, addTab(indent + 1); "-1,"
                       Print #fileNo, addTab(indent + 1); "L."; g_anOid; ","
                       Print #fileNo, addTab(indent + 1); tabVarNl; "."; g_anLanguageId; ","

                       If columnMaxLengthSrc <= columnMaxLengthDst Then
                         Print #fileNo, addTab(indent + 1); tabVarNl; "."; columnName; ","
                       Else
                         Print #fileNo, addTab(indent + 1); "RTRIM(LEFT("; tabVarNl; "."; columnName; ","; CStr(columnMaxLengthDst); ")),"
                       End If

                       Print #fileNo, addTab(indent + 1); IIf(isAggHead, "(CASE WHEN " & tabVarNl & "." & refColName & " = " & tabVarNl & "." & g_anAhOid & " THEN " & (attrBasePrio + clNlPrioNonAggHead) & " ELSE " & (attrBasePrio + clNlPrioAggHead) & " END)", CStr(attrBasePrio + clNlPrioNonAggHead)); ","
                       If usePsTagInNlTextTables Then
                         Print #fileNo, addTab(indent + 1); "L."; g_anPsOid; ","
                       End If
                       Print #fileNo, addTab(indent + 1); "1"
                       Print #fileNo, addTab(indent + 0); "FROM"
                       Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog; " L"
                       Print #fileNo, addTab(indent + 0); "INNER JOIN" ' Join with 'referring table'

                       If clMode = eclLrt Then
                         If g_classes.descriptors(referringClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False, True); " "; tabVarRef
                         Else
                           Print #fileNo, addTab(indent + 1); "("
                           Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; refColName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                              genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False, False, False, True); _
                                                              " WHERE (("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")) AND ("; g_anIsDeleted; " = 0)"; _
                                                              IIf(g_classes.descriptors(referringClassIndex).isPsTagged, " AND (" & g_anPsOid & " = " & psOidFilterVar & ")", "")
                           Print #fileNo, addTab(indent + 3); "UNION ALL"
                           Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; refColName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                              genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True, False, False, True); _
                                                              " WHERE ("; g_anInLrt; " = "; lrtOidFilterVar; ")"
                           Print #fileNo, addTab(indent + 1); ") "; tabVarRef
                         End If
                       Else
                         Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, , , False, True); " "; tabVarRef
                       End If

                       Print #fileNo, addTab(indent + 0); "ON"
                       If isAggHead Then
                         Print #fileNo, addTab(indent + 1); "L.ahObjectId = "; tabVarRef; "."; g_anOid
                       Else
                         Print #fileNo, addTab(indent + 1); "L.objectId = "; tabVarRef; "."; g_anOid
                       End If

                       If clMode = eclLrt Then
                         If g_classes.descriptors(referringClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 2); "AND"
                           Print #fileNo, addTab(indent + 1); "("
                           Print #fileNo, addTab(indent + 2); "(("; tabVarRef; "."; g_anIsLrtPrivate; " = 0) AND ("; tabVarRef; "."; g_anIsDeleted; " = 0) AND ("; tabVarRef; "."; g_anInLrt; " IS NULL OR "; tabVarRef; "."; g_anInLrt; " <> "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 3); "OR"
                           Print #fileNo, addTab(indent + 2); "(("; tabVarRef; "."; g_anIsLrtPrivate; " = 1) AND ("; tabVarRef; "."; g_anInLrt; " = "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 1); ")"
                         End If
                       End If

                       Print #fileNo, addTab(indent + 0); "INNER JOIN" ' Join with GEN of 'referred table'

                       Print #fileNo, addTab(indent + 1); "("
                       Print #fileNo, addTab(indent + 2); "SELECT"
                       Print #fileNo, addTab(indent + 3); g_anOid; ","
                       Print #fileNo, addTab(indent + 3); refColName; ","

                       If clMode = eclLrt And g_classes.descriptors(referredClassIndex).useMqtToImplementLrt Then
                         Print #fileNo, addTab(indent + 3); g_anIsLrtPrivate; ","
                         Print #fileNo, addTab(indent + 3); g_anIsDeleted; ","
                         Print #fileNo, addTab(indent + 3); g_anInLrt; ","
                       End If

                       Print #fileNo, addTab(indent + 3); "ROWNUMBER() OVER (PARTITION BY "; refColName; " ORDER BY (CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)) AS ROWNUM"
                       Print #fileNo, addTab(indent + 2); "FROM"
                       Print #fileNo, addTab(indent + 3); "("

                       If clMode = eclLrt Then
                         If g_classes.descriptors(referredClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 4); "SELECT OID,"; g_anValidTo; ","; refColName; _
                                                              IIf(clMode = eclLrt And g_classes.descriptors(referredClassIndex).useMqtToImplementLrt, "," & g_anIsLrtPrivate & "," & g_anIsDeleted & "," & g_anInLrt & "", ""); _
                                                              " FROM "; _
                                                              genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True, True, False, True); " "; _
                                                              "WHERE (("; g_anIsLrtPrivate; " = 0) AND ("; g_anIsDeleted; " = 0) AND ("; g_anInLrt; " IS NULL OR "; g_anInLrt; " <> "; lrtOidFilterVar; ")) "; _
                                                              "OR "; _
                                                               "(("; g_anIsLrtPrivate; " = 1) AND ("; g_anInLrt; " = "; lrtOidFilterVar; "))"; _
                                                              IIf(g_classes.descriptors(referredClassIndex).isPsTagged, " AND (" & g_anPsOid & " = " & psOidFilterVar & ")", "")
                         Else
                           Print #fileNo, addTab(indent + 4); "SELECT OID,"; g_anValidTo; ","; refColName; " FROM "; _
                                                              genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, True, False, False, False, True); _
                                                              " WHERE (("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")) AND ("; g_anIsDeleted; " = 0)"; _
                                                              IIf(g_classes.descriptors(referredClassIndex).isPsTagged, " AND (" & g_anPsOid & " = " & psOidFilterVar & ")", "")
                           Print #fileNo, addTab(indent + 5); "UNION ALL"
                           Print #fileNo, addTab(indent + 4); "SELECT OID,"; g_anValidTo; ","; refColName; " FROM "; _
                                                              genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True, False, False, True); _
                                                              " WHERE ("; g_anInLrt; " = "; lrtOidFilterVar; ")"
                         End If
                       Else
                         Print #fileNo, addTab(indent + 4); "SELECT OID,"; g_anValidTo; ","; refColName; " FROM "; _
                                                            genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , , False, True)
                       End If
                       Print #fileNo, addTab(indent + 3); ") "; tabVarGen; "_ALL"
                       Print #fileNo, addTab(indent + 1); ") "; tabVarGen

                       Print #fileNo, addTab(indent + 0); "ON"
                       Print #fileNo, addTab(indent + 1); tabVarRef; "."; relFkAttrName; " = "; tabVarGen; "."; refColName
                       Print #fileNo, addTab(indent + 2); "AND"
                       Print #fileNo, addTab(indent + 1); tabVarGen; ".ROWNUM = 1"

                       If clMode = eclLrt Then
                         If g_classes.descriptors(referredClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 2); "AND"
                           Print #fileNo, addTab(indent + 1); "("
                           Print #fileNo, addTab(indent + 2); "(("; tabVarGen; "."; g_anIsLrtPrivate; " = 0) AND ("; tabVarGen; "."; g_anIsDeleted; " = 0) AND ("; tabVarGen; "."; g_anInLrt; " IS NULL OR "; tabVarGen; "."; g_anInLrt; " <> "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 3); "OR"
                           Print #fileNo, addTab(indent + 2); "(("; tabVarGen; "."; g_anIsLrtPrivate; " = 1) AND ("; tabVarGen; "."; g_anInLrt; " = "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 1); ")"
                         End If
                       End If

                       Print #fileNo, addTab(indent + 0); "INNER JOIN" ' Join with NL-TEXT of GEN of 'referred table'
                       If clMode = eclLrt Then
                         If g_classes.descriptors(referredClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True, True, True, True); " "; tabVarNl
                         Else
                           Print #fileNo, addTab(indent + 1); "("
                           Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; refColName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                              genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, True, False, False, True, True); _
                                                              " WHERE (("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")) AND ("; g_anIsDeleted; " = 0)"
                           Print #fileNo, addTab(indent + 3); "UNION ALL"
                           Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; refColName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                              genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True, False, True, True); _
                                                              " WHERE ("; g_anInLrt; " = "; lrtOidFilterVar; ")"
                           Print #fileNo, addTab(indent + 1); ") "; tabVarNl
                         End If
                       Else
                         Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , , True, True); " "; tabVarNl
                       End If

                       Print #fileNo, addTab(indent + 0); "ON"
                       Print #fileNo, addTab(indent + 1); tabVarNl; "."; refColName; " = "; tabVarGen; "."; g_anOid

                       If clMode = eclLrt Then
                         If g_classes.descriptors(referredClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 2); "AND"
                           Print #fileNo, addTab(indent + 1); "("
                           Print #fileNo, addTab(indent + 2); "(("; tabVarNl; "."; g_anIsLrtPrivate; " = 0) AND ("; tabVarNl; "."; g_anIsDeleted; " = 0) AND ("; tabVarNl; "."; g_anInLrt; " IS NULL OR "; tabVarNl; "."; g_anInLrt; " <> "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 3); "OR"
                           Print #fileNo, addTab(indent + 2); "(("; tabVarNl; "."; g_anIsLrtPrivate; " = 1) AND ("; tabVarNl; "."; g_anInLrt; " = "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 1); ")"
                         End If
                       End If

                       Print #fileNo, addTab(indent + 0); "WHERE"
                       'include only reference object labels in german and english
                       Print #fileNo, addTab(indent + 1); tabVarNl; "."; g_anLanguageId; " IN ("; gc_langIdGerman; ","; gc_langIdEnglish; ")"
                       Print #fileNo, addTab(indent + 2); "AND"
                       Print #fileNo, addTab(indent + 1); tabVarNl; "."; columnName; " IS NOT NULL"

                       If useClassIdFilter Then
                         Print #fileNo, addTab(indent + 2); "AND"
                         If isAggHead Then
                           Print #fileNo, addTab(indent + 1); "L.ahClassid IN ("; entityIdStrList; ")"
                         Else
                           Print #fileNo, addTab(indent + 1); "L.entityType = '"; getAcmEntityTypeKey(acmEntityType); "'"
                           Print #fileNo, addTab(indent + 2); "AND"
                           Print #fileNo, addTab(indent + 1); "L.entityId = "; entityIdStrList
                         End If
                       End If

                       If isPsTagged Then
                         If Not (clMode = eclLrt) Or useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 2); "AND"
                           Print #fileNo, addTab(indent + 1); tabVarRef; "."; g_anPsOid; " = "; psOidFilterVar
                         End If
                       End If

                       Print #fileNo, addTab(indent + 0); isolationLevelSuffix; ";"
                     Else
                       tabVarNl = UCase(g_classes.descriptors(referredClassIndex).shortName) & "NL"

                       genProcSectionHeader fileNo, "add NL-Text-Column """ & _
                           UCase(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute(j).mapFrom) & _
                           """ (" & UCase(g_attributes.descriptors(attrIndex).attributeName) & " @ " & g_classes.descriptors(referredClassIndex).className & ") to changelog entries", indent

                       Print #fileNo, addTab(indent + 0); "INSERT INTO"
                       Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogNl
                       Print #fileNo, addTab(indent + 0); "("
                       Print #fileNo, addTab(indent + 1); g_anOid; ","
                       Print #fileNo, addTab(indent + 1); "CLG_OID,"
                       Print #fileNo, addTab(indent + 1); g_anLanguageId; ","
                       Print #fileNo, addTab(indent + 1); UCase(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute(j).mapFrom); ","
                       Print #fileNo, addTab(indent + 1); "PRIO,"
                       If usePsTagInNlTextTables Then
                         Print #fileNo, addTab(indent + 1); g_anPsOid; ","
                       End If
                       Print #fileNo, addTab(indent + 1); g_anVersionId
                       Print #fileNo, addTab(indent + 0); ")"
                       Print #fileNo, addTab(indent + 0); "SELECT"
                       Print #fileNo, addTab(indent + 1); "-1,"
                       Print #fileNo, addTab(indent + 1); "L."; g_anOid; ","
                       Print #fileNo, addTab(indent + 1); tabVarNl; "."; g_anLanguageId; ","

                       If columnMaxLengthSrc <= columnMaxLengthDst Then
                         Print #fileNo, addTab(indent + 1); tabVarNl; "."; columnName; ","
                       Else
                         Print #fileNo, addTab(indent + 1); "RTRIM(LEFT("; tabVarNl; "."; columnName; ","; CStr(columnMaxLengthDst); ")),"
                       End If

                       Print #fileNo, addTab(indent + 1); IIf(isAggHead, "(CASE WHEN " & tabVarNl & "." & refColName & " = " & tabVarNl & "." & g_anAhOid & " THEN " & (attrBasePrio + clNlPrioNonAggHead) & " ELSE " & (attrBasePrio + clNlPrioAggHead) & " END)", CStr(attrBasePrio + clNlPrioNonAggHead)); ","
                       If usePsTagInNlTextTables Then
                         Print #fileNo, addTab(indent + 1); "L."; g_anPsOid; ","
                       End If
                       Print #fileNo, addTab(indent + 1); "1"
                       Print #fileNo, addTab(indent + 0); "FROM"
                       Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog; " L"
                       Print #fileNo, addTab(indent + 0); "INNER JOIN" ' Join with 'referring table'

                       If clMode = eclLrt Then
                         If g_classes.descriptors(referringClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False, True); " "; tabVarRef
                         Else
                           Print #fileNo, addTab(indent + 1); "("
                           Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; refColName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                              genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False, False, False, True); _
                                                              " WHERE (("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")) AND ("; g_anIsDeleted; " = 0)"
                           Print #fileNo, addTab(indent + 3); "UNION ALL"
                           Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; refColName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                              genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True, False, False, True); _
                                                              " WHERE ("; g_anInLrt; " = "; lrtOidFilterVar; ")"
                           Print #fileNo, addTab(indent + 1); ") "; tabVarRef
                         End If
                       Else
                         Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(referringClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, , , False, True); " "; tabVarRef
                       End If

                       Print #fileNo, addTab(indent + 0); "ON"
                       If isAggHead Then
                         Print #fileNo, addTab(indent + 1); "L.ahObjectId = "; tabVarRef; "."; g_anOid
                       Else
                         Print #fileNo, addTab(indent + 1); "L.objectId = "; tabVarRef; "."; g_anOid
                       End If

                       If clMode = eclLrt Then
                         If g_classes.descriptors(referringClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 2); "AND"
                           Print #fileNo, addTab(indent + 1); "("
                           Print #fileNo, addTab(indent + 2); "(("; tabVarRef; "."; g_anIsLrtPrivate; " = 0) AND ("; tabVarRef; "."; g_anIsDeleted; " = 0) AND ("; tabVarRef; "."; g_anInLrt; " IS NULL OR "; tabVarRef; "."; g_anInLrt; " <> "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 3); "OR"
                           Print #fileNo, addTab(indent + 2); "(("; tabVarRef; "."; g_anIsLrtPrivate; " = 1) AND ("; tabVarRef; "."; g_anInLrt; " = "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 1); ")"
                         End If
                       End If

                       Print #fileNo, addTab(indent + 0); "INNER JOIN" ' Join with NL-TEXT of 'referred table'
                       If clMode = eclLrt Then
                         If g_classes.descriptors(referredClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, True, True); " "; tabVarNl
                         Else
                           Print #fileNo, addTab(indent + 1); "("
                           Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; refColName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                              genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False, False, True, True); _
                                                              " WHERE (("; g_anInLrt; " IS NULL) OR ("; g_anInLrt; " <> "; lrtOidFilterVar; ")) AND ("; g_anIsDeleted; " = 0)"
                           Print #fileNo, addTab(indent + 3); "UNION ALL"
                           Print #fileNo, addTab(indent + 2); "SELECT OID,"; g_anAhOid; ","; refColName; ","; columnName; ","; g_anLanguageId; " FROM "; _
                                                              genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True, False, True, True); _
                                                              " WHERE ("; g_anInLrt; " = "; lrtOidFilterVar; ")"
                           Print #fileNo, addTab(indent + 1); ") "; tabVarNl
                         End If
                       Else
                         Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(referredClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, , , True, True); " "; tabVarNl
                       End If

                       Print #fileNo, addTab(indent + 0); "ON"
                       Print #fileNo, addTab(indent + 1); tabVarRef; "."; relFkAttrName; " = "; tabVarNl; "."; refColName

                       If clMode = eclLrt Then
                         If g_classes.descriptors(referredClassIndex).useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 2); "AND"
                           Print #fileNo, addTab(indent + 1); "("
                           Print #fileNo, addTab(indent + 2); "(("; tabVarNl; "."; g_anIsLrtPrivate; " = 0) AND ("; tabVarNl; "."; g_anIsDeleted; " = 0) AND ("; tabVarNl; "."; g_anInLrt; " IS NULL OR "; tabVarNl; "."; g_anInLrt; " <> "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 3); "OR"
                           Print #fileNo, addTab(indent + 2); "(("; tabVarNl; "."; g_anIsLrtPrivate; " = 1) AND ("; tabVarNl; "."; g_anInLrt; " = "; lrtOidFilterVar; "))"
                           Print #fileNo, addTab(indent + 1); ")"
                         End If
                       End If

                       Print #fileNo, addTab(indent + 0); "WHERE"
                       'include only reference object labels in german and english
                       Print #fileNo, addTab(indent + 1); tabVarNl; "."; g_anLanguageId; " IN ("; gc_langIdGerman; ","; gc_langIdEnglish; ")"
                       Print #fileNo, addTab(indent + 2); "AND"
                       Print #fileNo, addTab(indent + 1); tabVarNl; "."; columnName; " IS NOT NULL"

                       If useClassIdFilter Then
                         Print #fileNo, addTab(indent + 2); "AND"
                         If isAggHead Then
                           Print #fileNo, addTab(indent + 1); "L.ahClassid IN ("; entityIdStrList; ")"
                         Else
                           Print #fileNo, addTab(indent + 1); "L.entityType = '"; getAcmEntityTypeKey(acmEntityType); "'"
                           Print #fileNo, addTab(indent + 2); "AND"
                           Print #fileNo, addTab(indent + 1); "L.entityId = "; entityIdStrList
                         End If
                       End If

                       If isPsTagged Then
                         If Not (clMode = eclLrt) Or useMqtToImplementLrt Then
                           Print #fileNo, addTab(indent + 2); "AND"
                           Print #fileNo, addTab(indent + 1); tabVarRef; "."; g_anPsOid; " = "; psOidFilterVar
                         End If
                       End If

                       Print #fileNo, addTab(indent + 0); isolationLevelSuffix; ";"
                     End If
                   End If
                   numAttrsFound = numAttrsFound + 1
               End If
             Next j
           End If
       Next i
     End If
 ' ### ENDIF IVK ###
   End If
 
   genAddNlTextChangeLogDdlForIndividualAttrs = numAttrsFound
 End Function
 
 
 Sub genAddNlTextChangeLogDdl( _
   fileNo As Integer, _
   ByRef gc_tempTabNameChangeLog As String, _
   ByRef gc_tempTabNameChangeLogNl As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional indent As Integer = 1, _
   Optional ByRef lrtOidStr As String = "lrtOid_in", _
   Optional clMode As ChangeLogMode = eclLrt, _
   Optional ByRef qualTabNameLrtNl As String = "" _
 )

   If clMode = eclLrt Then
     Dim qualTabNameLrt As String

     genProcSectionHeader fileNo, "add NL-texts for """ & g_anLrtComment & """", indent

     Print #fileNo, addTab(indent + 0); "INSERT INTO"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogNl
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); g_anOid; ","
     Print #fileNo, addTab(indent + 1); "CLG_OID,"
     Print #fileNo, addTab(indent + 1); g_anLanguageId; ","
     Print #fileNo, addTab(indent + 1); g_anLrtComment; ","
     Print #fileNo, addTab(indent + 1); g_anVersionId
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "SELECT"
     Print #fileNo, addTab(indent + 1); "-1,"
     Print #fileNo, addTab(indent + 1); "L."; g_anOid; ","
     Print #fileNo, addTab(indent + 1); "LRTNL."; g_anLanguageId; ","
     Print #fileNo, addTab(indent + 1); "LRTNL.TRANSACTIONCOMMENT,"
     Print #fileNo, addTab(indent + 1); "1"
     Print #fileNo, addTab(indent + 0); "FROM"
     Print #fileNo, addTab(indent + 1); qualTabNameLrtNl; " LRTNL,"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog; " L"
     Print #fileNo, addTab(indent + 0); "WHERE"
     Print #fileNo, addTab(indent + 1); "LRTNL.LRT_OID = "; lrtOidStr
     Print #fileNo, addTab(indent + 2); "AND"
     Print #fileNo, addTab(indent + 1); "LRTNL.TRANSACTIONCOMMENT IS NOT NULL"
     Print #fileNo, addTab(indent + 0); ";"
   End If

   genProcSectionHeader fileNo, "add NL-texts for entity-names", indent
   Print #fileNo, addTab(indent + 0); "INSERT INTO"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogNl
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); g_anOid; ","
   Print #fileNo, addTab(indent + 1); "CLG_OID,"
   Print #fileNo, addTab(indent + 1); g_anLanguageId; ","
   Print #fileNo, addTab(indent + 1); g_anAcmEntityName; ","
   Print #fileNo, addTab(indent + 1); g_anVersionId
   Print #fileNo, addTab(indent + 0); ")"
   Print #fileNo, addTab(indent + 0); "SELECT"
   Print #fileNo, addTab(indent + 1); "-1,"
   Print #fileNo, addTab(indent + 1); "L."; g_anOid; ","
   Print #fileNo, addTab(indent + 1); "ENL."; g_anLanguageId; ","
   Print #fileNo, addTab(indent + 1); "ENL."; g_anAcmEntityLabel; ","
   Print #fileNo, addTab(indent + 1); "1"
   Print #fileNo, addTab(indent + 0); "FROM"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog; " L"
   Print #fileNo, addTab(indent + 0); "INNER JOIN"
   Print #fileNo, addTab(indent + 1); g_qualTabNameAcmEntity; " E"
   Print #fileNo, addTab(indent + 0); "ON"
   Print #fileNo, addTab(indent + 1); "L."; g_anAcmEntityId; " = E."; g_anAcmEntityId
   Print #fileNo, addTab(indent + 2); "AND"
   Print #fileNo, addTab(indent + 1); "L."; g_anAcmEntityType; " = E."; g_anAcmEntityType
   Print #fileNo, addTab(indent + 0); "INNER JOIN"
   Print #fileNo, addTab(indent + 1); g_qualTabNameAcmEntityNl; " ENL"
   Print #fileNo, addTab(indent + 0); "ON"
   Print #fileNo, addTab(indent + 1); "E."; g_anAcmEntitySection; " = ENL."; g_anAcmEntitySection
   Print #fileNo, addTab(indent + 2); "And"
   Print #fileNo, addTab(indent + 1); "E."; g_anAcmEntityName; " = ENL."; g_anAcmEntityName
   Print #fileNo, addTab(indent + 2); "And"
   Print #fileNo, addTab(indent + 1); "E."; g_anAcmEntityType; " = ENL."; g_anAcmEntityType
   Print #fileNo, addTab(indent + 0); ";"
 
   ' FIXME: assuming that within a single class hierarchy a given attribute name is not mapped
   ' differently for different classes we use 'DISTINCT' here. We should navigate up in the
   ' class hierarchy and pick exactly the attribute that is referred to!
   ' E.g. 'SR0CONTEXT' exists multiple times in the GENERICASPECT-tree. Each changelog-entry refers to a unique
   ' occurence.

   genProcSectionHeader fileNo, "add NL-texts for attribute-names", indent
   Print #fileNo, addTab(indent + 0); "INSERT INTO"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogNl
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); g_anOid; ","
   Print #fileNo, addTab(indent + 1); "CLG_OID,"
   Print #fileNo, addTab(indent + 1); g_anLanguageId; ","
   Print #fileNo, addTab(indent + 1); g_anAcmAttributeLabel; ","
   Print #fileNo, addTab(indent + 1); g_anVersionId
   Print #fileNo, addTab(indent + 0); ")"
   Print #fileNo, addTab(indent + 0); "SELECT DISTINCT"
   Print #fileNo, addTab(indent + 1); "-1,"
   Print #fileNo, addTab(indent + 1); "L."; g_anOid; ","
   Print #fileNo, addTab(indent + 1); "ANL."; g_anLanguageId; ","
   Print #fileNo, addTab(indent + 1); "ANL."; g_anAcmAttributeLabel; ","
   Print #fileNo, addTab(indent + 1); "1"
   Print #fileNo, addTab(indent + 0); "FROM"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog; " L"
   Print #fileNo, addTab(indent + 0); "INNER JOIN"
   Print #fileNo, addTab(indent + 1); g_qualTabNameAcmEntity; " EC"
   Print #fileNo, addTab(indent + 0); "ON"
   Print #fileNo, addTab(indent + 1); "L."; g_anAcmEntityId; " = EC."; g_anAcmEntityId
   Print #fileNo, addTab(indent + 2); "AND"
   Print #fileNo, addTab(indent + 1); "L."; g_anAcmEntityType; " = EC."; g_anAcmEntityType
   Print #fileNo, addTab(indent + 0); "INNER JOIN"
   Print #fileNo, addTab(indent + 1); g_qualTabNameAcmEntity; " E"
   Print #fileNo, addTab(indent + 0); "ON"
 ' ### IF IVK ###
   Print #fileNo, addTab(indent + 1); "E."; g_anAcmOrParEntitySection; " = EC."; g_anAcmOrParEntitySection
   Print #fileNo, addTab(indent + 2); "AND"
   Print #fileNo, addTab(indent + 1); "E."; g_anAcmOrParEntityName; " = EC."; g_anAcmOrParEntityName
   Print #fileNo, addTab(indent + 2); "AND"
   Print #fileNo, addTab(indent + 1); "E."; g_anAcmOrParEntityType; " = EC."; g_anAcmOrParEntityType
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(indent + 1); "COALESCE(E."; g_anAcmOrParEntitySection; ",E."; g_anAcmEntitySection; ") = COALESCE(EC."; g_anAcmOrParEntitySection; "EC."; g_anAcmEntitySection; ")"
 ' Print #fileNo, addTab(indent + 2); "AND"
 ' Print #fileNo, addTab(indent + 1); "COALESCE(E."; g_anAcmOrParEntityName; ",E."; g_anAcmEntityName; ") = COALESCE(EC."; g_anAcmOrParEntityName; "EC."; g_anAcmEntityName; ")"
 ' Print #fileNo, addTab(indent + 2); "AND"
 ' Print #fileNo, addTab(indent + 1); "E."; g_anAcmEntityType; " = EC."; g_anAcmEntityType
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(indent + 0); "INNER JOIN"
   Print #fileNo, addTab(indent + 1); g_qualTabNameAcmAttributeNl; " ANL"
   Print #fileNo, addTab(indent + 0); "ON"
   Print #fileNo, addTab(indent + 1); "E."; g_anAcmEntitySection; " = ANL."; g_anAcmEntitySection
   Print #fileNo, addTab(indent + 2); "AND"
   Print #fileNo, addTab(indent + 1); "E."; g_anAcmEntityName; " = ANL."; g_anAcmEntityName
   Print #fileNo, addTab(indent + 2); "AND"
   Print #fileNo, addTab(indent + 1); "E."; g_anAcmEntityType; " = ANL."; g_anAcmEntityType
   Print #fileNo, addTab(indent + 0); "WHERE"
   Print #fileNo, addTab(indent + 1); "L."; g_anLdmDbColumnName; " = ANL."; g_anAcmAttributeName
   Print #fileNo, addTab(indent + 2); "OR"
   Print #fileNo, addTab(indent + 1); "L."; g_anLdmDbColumnName; " = ANL."; g_anAcmAttributeName; " || '_ID'"
   Print #fileNo, addTab(indent + 0); ";"
 End Sub
 
 
 Sub genCondenseChangeLogNlDdl( _
   fileNo As Integer, _
   ByRef changeLogClassIndex As Integer, _
   ByRef qualTabNameChangeLogNl As String, _
   ByRef gc_tempTabNameChangeLogNl As String, _
   ByRef qualSeqNameOid As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1 _
 )
   Dim transformation As AttributeListTransformation
   initAttributeTransformation transformation, 0
 
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
 
   Dim tIndex As Integer
   Dim i As Integer

   genProcSectionHeader fileNo, "condense and move all ChangeLog NL-records into persistent table", indent
 
   Print #fileNo, addTab(indent + 0); "BEGIN ATOMIC"
   genNlsTransformedAttrListForEntityWithColReUse changeLogClassIndex, eactClass, transformation, tabColumns, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, , , , , edomNone
 
   genProcSectionHeader fileNo, "declare variables", indent + 1, True
   genVarDecl fileNo, "v_last_clgOid", g_dbtOid, "-1", indent + 1
   genVarDecl fileNo, "v_last_languageId", g_dbtEnumId, "-1", indent + 1
 ' ### IF IVK ###
   If usePsTagInNlTextTables Then
     genVarDecl fileNo, "v_last_psOid", g_dbtOid, "-1", indent + 1
   End If
 ' ### ENDIF IVK ###

   Print #fileNo,
   For i = 1 To tabColumns.numDescriptors
       If (tabColumns.descriptors(i).columnCategory And eacRegular) Then
         genVarDecl fileNo, "v_last_" & tabColumns.descriptors(i).acmAttributeName, getDbDatatypeByDomainIndex(tabColumns.descriptors(i).dbDomainIndex), "NULL", indent + 1
       End If
   Next i

   genProcSectionHeader fileNo, "loop over individual records in temporary table", indent + 1
   Print #fileNo, addTab(indent + 1); "FOR logLoop AS"
   Print #fileNo, addTab(indent + 2); "SELECT"
 
   For i = 1 To tabColumns.numDescriptors
       If (tabColumns.descriptors(i).columnCategory And eacRegular) Then
         Print #fileNo, addTab(indent + 3); tabColumns.descriptors(i).columnName; " AS c_"; tabColumns.descriptors(i).acmAttributeName; ","
       End If
   Next i

 ' ### IF IVK ###
   If usePsTagInNlTextTables Then
     Print #fileNo, addTab(indent + 3); g_anPsOid; " AS c_psOid,"
   End If
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(indent + 3); "CLG_OID AS c_clgOid,"
   Print #fileNo, addTab(indent + 3); g_anLanguageId; " AS c_languageId"
   Print #fileNo, addTab(indent + 2); "FROM"
   Print #fileNo, addTab(indent + 3); gc_tempTabNameChangeLogNl
   Print #fileNo, addTab(indent + 2); "ORDER BY"
   Print #fileNo, addTab(indent + 3); "CLG_OID,"
   Print #fileNo, addTab(indent + 3); g_anLanguageId; ","
   Print #fileNo, addTab(indent + 3); "COALESCE(PRIO, "; CStr(clNlPrioOther); ") ASC"
   Print #fileNo, addTab(indent + 1); "DO"

   genProcSectionHeader fileNo, "determine whether this record needs to be merged with the previous record", indent + 2, True
   Print #fileNo, addTab(indent + 2); "IF (v_last_clgOid > 0) AND ((c_clgOid <> v_last_clgOid) OR (c_languageId <> v_last_languageId)) THEN"
   genProcSectionHeader fileNo, "this maps to a new record - persist previous record", indent + 3, True

   Print #fileNo, addTab(indent + 3); "INSERT INTO"
   Print #fileNo, addTab(indent + 4); qualTabNameChangeLogNl
   Print #fileNo, addTab(indent + 3); "("
 
   initAttributeTransformation transformation, 0
   tabColumns = nullEntityColumnDescriptors
   genNlsTransformedAttrListForEntityWithColReUse changeLogClassIndex, eactClass, transformation, tabColumns, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, indent + 4, , , , edomListNonLrt
 
   Print #fileNo, addTab(indent + 3); ")"
   Print #fileNo, addTab(indent + 3); "VALUES"
   Print #fileNo, addTab(indent + 3); "("

   tIndex = 1
 ' ### IF IVK ###
   initAttributeTransformation transformation, tabColumns.numDescriptors + 4 + IIf(usePsTagInNlTextTables, 1, 0)
 ' ### ELSE IVK ###
 ' initAttributeTransformation transformation, tabColumns.numDescriptors + 4
 ' ### ENDIF IVK ###
   For i = 1 To tabColumns.numDescriptors
       If (tabColumns.descriptors(i).columnCategory And eacRegular) Then
         setAttributeMapping transformation, tIndex, tabColumns.descriptors(i).columnName, "v_last_" & tabColumns.descriptors(i).acmAttributeName
         tIndex = tIndex + 1
       End If
   Next i
   setAttributeMapping transformation, tIndex + 0, conOid, "NEXTVAL FOR " & qualSeqNameOid
   setAttributeMapping transformation, tIndex + 1, "CLG_OID", "v_last_clgOid"
   setAttributeMapping transformation, tIndex + 2, conLanguageId, "v_last_languageId"
   setAttributeMapping transformation, tIndex + 3, conVersionId, "1"
 ' ### IF IVK ###
   If usePsTagInNlTextTables Then
     setAttributeMapping transformation, tIndex + 4, conPsOid, "v_last_psOid"
   End If
 ' ### ENDIF IVK ###

   tabColumns = nullEntityColumnDescriptors
   genNlsTransformedAttrListForEntityWithColReUse changeLogClassIndex, eactClass, transformation, tabColumns, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, indent + 4, , , , edomListNonLrt

   Print #fileNo, addTab(indent + 3); ");"

   genProcSectionHeader fileNo, "keep track of values read in this record", indent + 3
   For i = 1 To tabColumns.numDescriptors
       If (tabColumns.descriptors(i).columnCategory And eacRegular) Then
         Print #fileNo, addTab(indent + 3); "SET v_last_"; tabColumns.descriptors(i).acmAttributeName; " = c_"; tabColumns.descriptors(i).acmAttributeName; ";"
       End If
   Next i

   Print #fileNo, addTab(indent + 3); "SET v_last_clgOid = c_clgOid;"
   Print #fileNo, addTab(indent + 3); "SET v_last_languageId = c_languageId;"
 ' ### IF IVK ###
   If usePsTagInNlTextTables Then
     Print #fileNo, addTab(indent + 3); "SET v_last_psOid = c_psOid;"
   End If
 ' ### ENDIF IVK ###
 
   Print #fileNo, addTab(indent + 2); "ELSE"
   genProcSectionHeader fileNo, "this record merges with previous record", indent + 3, True
 
   For i = 1 To tabColumns.numDescriptors
       If (tabColumns.descriptors(i).columnCategory And eacRegular) Then
         Print #fileNo, addTab(indent + 3); "IF c_"; tabColumns.descriptors(i).acmAttributeName; " IS NOT NULL THEN"
         Print #fileNo, addTab(indent + 4); "SET v_last_"; tabColumns.descriptors(i).acmAttributeName; " = c_"; tabColumns.descriptors(i).acmAttributeName; ";"
         Print #fileNo, addTab(indent + 3); "END IF;"
       End If
   Next i
 
   Print #fileNo, addTab(indent + 3); "IF v_last_clgOid < 0 THEN"
   Print #fileNo, addTab(indent + 4); "SET v_last_clgOid = c_clgOid;"
   Print #fileNo, addTab(indent + 4); "SET v_last_languageId = c_languageId;"
 ' ### IF IVK ###
   If usePsTagInNlTextTables Then
     Print #fileNo, addTab(indent + 4); "SET v_last_psOid = c_psOid;"
   End If
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(indent + 3); "END IF;"
 
   Print #fileNo, addTab(indent + 2); "END IF;"
   Print #fileNo, addTab(indent + 1); "END FOR;"

   genProcSectionHeader fileNo, "persist final record", indent + 1
   Print #fileNo, addTab(indent + 1); "IF v_last_clgOid > 0 THEN"
   Print #fileNo, addTab(indent + 2); "INSERT INTO"
   Print #fileNo, addTab(indent + 3); qualTabNameChangeLogNl
   Print #fileNo, addTab(indent + 2); "("
 
   initAttributeTransformation transformation, 0
   tabColumns = nullEntityColumnDescriptors
   genNlsTransformedAttrListForEntityWithColReUse changeLogClassIndex, eactClass, transformation, tabColumns, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, indent + 3, , , , edomListNonLrt
 
   Print #fileNo, addTab(indent + 2); ")"
   Print #fileNo, addTab(indent + 2); "VALUES"
   Print #fileNo, addTab(indent + 2); "("

   tIndex = 1
 ' ### IF IVK ###
   initAttributeTransformation transformation, tabColumns.numDescriptors + 4 + IIf(usePsTagInNlTextTables, 1, 0)
 ' ### ELSE IVK ###
 ' initAttributeTransformation transformation, tabColumns.numDescriptors + 4
 ' ### ENDIF IVK ###
   For i = 1 To tabColumns.numDescriptors
       If (tabColumns.descriptors(i).columnCategory And eacRegular) Then
         setAttributeMapping transformation, tIndex, tabColumns.descriptors(i).columnName, "v_last_" & tabColumns.descriptors(i).acmAttributeName
         tIndex = tIndex + 1
       End If
   Next i
   setAttributeMapping transformation, tIndex + 0, conOid, "NEXTVAL FOR " & qualSeqNameOid
   setAttributeMapping transformation, tIndex + 1, "CLG_OID", "v_last_clgOid"
   setAttributeMapping transformation, tIndex + 2, conLanguageId, "v_last_languageId"
   setAttributeMapping transformation, tIndex + 3, conVersionId, "1"
 ' ### IF IVK ###
   If usePsTagInNlTextTables Then
     setAttributeMapping transformation, tIndex + 4, conPsOid, "v_last_psOid"
   End If
 ' ### ENDIF IVK ###

   tabColumns = nullEntityColumnDescriptors
   genNlsTransformedAttrListForEntityWithColReUse changeLogClassIndex, eactClass, transformation, tabColumns, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, indent + 3, , , , edomListNonLrt

   Print #fileNo, addTab(indent + 2); ");"

   Print #fileNo, addTab(indent + 1); "END IF;"

   Print #fileNo, addTab(indent + 0); "END;"
 End Sub
 
 
 Sub genPersistChangeLogDdl( _
   fileNo As Integer, _
   ByRef changeLogClassIndex As Integer, _
   ByRef qualTabNameChangeLog As String, _
   ByRef gc_tempTabNameChangeLog As String, _
   ByRef qualTabNameChangeLogNl As String, _
   ByRef gc_tempTabNameChangeLogNl As String, _
   ByRef qualSeqNameOid As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional clMode As ChangeLogMode = eclLrt, _
   Optional ByRef qualTabNameLrtNl As String = "", _
   Optional ByRef lrtOidStr As String = "lrtOid_in", _
   Optional skipNl As Boolean = False, _
   Optional ByRef varNameRefTs As String = "" _
 )
   Dim qualTabNameChangeLogWork As String
   qualTabNameChangeLogWork = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, g_workDataPoolIndex)
   Dim qualTabNameChangeLogNlWork As String
   qualTabNameChangeLogNlWork = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, g_workDataPoolIndex, , , , True)

 ' ### IF IVK ###
   genProcSectionHeader fileNo, "determine OID of division for log records - if not already known", indent, skipNl
   Print #fileNo, addTab(indent + 0); "UPDATE"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog; " TCL"
   Print #fileNo, addTab(indent + 0); "SET"
   Print #fileNo, addTab(indent + 1); "TCL.divisionOid = (SELECT PS.PDIDIV_OID FROM "; g_qualTabNameProductStructure; " PS WHERE PS."; g_anOid; " = TCL."; g_anPsOid; ")"
   Print #fileNo, addTab(indent + 0); "WHERE"
   Print #fileNo, addTab(indent + 1); "(TCL.divisionOid IS NULL)"
   Print #fileNo, addTab(indent + 2); "AND"
   Print #fileNo, addTab(indent + 1); "(TCL."; g_anPsOid; " IS NOT NULL)"
   Print #fileNo, addTab(indent + 0); ";"

 ' ### ENDIF IVK ###
   genProcSectionHeader fileNo, "move all ChangeLog records into persistent table", indent

   Print #fileNo, addTab(indent + 0); "INSERT INTO"
   Print #fileNo, addTab(indent + 1); qualTabNameChangeLog
   Print #fileNo, addTab(indent + 0); "("

   genAttrDeclsForClassRecursive changeLogClassIndex, , fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 1, , , , edomListNonLrt

   Print #fileNo, addTab(indent + 0); ")"
   Print #fileNo, addTab(indent + 0); "SELECT"

   genAttrDeclsForClassRecursive changeLogClassIndex, , fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 1, , , , edomListNonLrt

   Print #fileNo, addTab(indent + 0); "FROM"
   Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog
   Print #fileNo, addTab(indent + 0); ";"

   genAddNlTextChangeLogDdl fileNo, gc_tempTabNameChangeLog, gc_tempTabNameChangeLogNl, ddlType, indent, lrtOidStr, clMode, qualTabNameLrtNl

 ' ### IF IVK ###
   If clMode = eclSetProd Then
     genProcSectionHeader fileNo, "retrieve LRTCOMMENT from ChangeLog in Work Data Pool", indent

     Print #fileNo, addTab(indent + 0); "INSERT INTO"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLogNl
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); g_anOid; ","
     Print #fileNo, addTab(indent + 1); "CLG_OID,"
     Print #fileNo, addTab(indent + 1); g_anLanguageId; ","
     Print #fileNo, addTab(indent + 1); "LRTCOMMENT,"
     Print #fileNo, addTab(indent + 1); g_anVersionId
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "WITH"
     Print #fileNo, addTab(indent + 1); "V_NlCl"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "clg_oid,"
     Print #fileNo, addTab(indent + 1); "language_id,"
     Print #fileNo, addTab(indent + 1); "lrtcomment,"
     Print #fileNo, addTab(indent + 1); "rownum"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "AS"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "SELECT"
     Print #fileNo, addTab(indent + 2); "L."; g_anOid; ","
     Print #fileNo, addTab(indent + 2); "NLWork."; g_anLanguageId; ","
     Print #fileNo, addTab(indent + 2); "NLWork.LRTCOMMENT,"
     Print #fileNo, addTab(indent + 2); "ROWNUMBER() OVER (PARTITION BY NLWork.CLG_OID, NLWork."; g_anLanguageId; ", Work."; g_anLdmDbColumnName; ", Work.OPERATION_ID ORDER BY Work.OPTIMESTAMP DESC)"
     Print #fileNo, addTab(indent + 1); "FROM"
     Print #fileNo, addTab(indent + 2); gc_tempTabNameChangeLog; " L"
     Print #fileNo, addTab(indent + 1); "INNER JOIN"
     Print #fileNo, addTab(indent + 2); qualTabNameChangeLogWork; " Work"
     Print #fileNo, addTab(indent + 1); "ON"
     Print #fileNo, addTab(indent + 2); "L.objectId = Work.OBJECTID"
     Print #fileNo, addTab(indent + 3); "AND"
     Print #fileNo, addTab(indent + 2); "L.operation_id = Work.OPERATION_ID"
     Print #fileNo, addTab(indent + 3); "AND"
     Print #fileNo, addTab(indent + 2); "COALESCE(L.dbColumnName, '') = COALESCE(Work."; g_anLdmDbColumnName; ", '')"
     Print #fileNo, addTab(indent + 1); "INNER JOIN"
     Print #fileNo, addTab(indent + 2); qualTabNameChangeLogNlWork; " NLWork"
     Print #fileNo, addTab(indent + 1); "ON"
     Print #fileNo, addTab(indent + 2); "Work."; g_anOid; " = NLWork.CLG_OID"
     Print #fileNo, addTab(indent + 1); "WHERE"
     Print #fileNo, addTab(indent + 2); "Work.OPTIMESTAMP < v_setProductiveTs"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "SELECT"
     Print #fileNo, addTab(indent + 1); "-1,"
     Print #fileNo, addTab(indent + 1); "clg_oid,"
     Print #fileNo, addTab(indent + 1); "language_id,"
     Print #fileNo, addTab(indent + 1); "lrtcomment,"
     Print #fileNo, addTab(indent + 1); "1"
     Print #fileNo, addTab(indent + 0); "FROM"
     Print #fileNo, addTab(indent + 1); "V_NlCl"
     Print #fileNo, addTab(indent + 0); "WHERE"
     Print #fileNo, addTab(indent + 1); "rownum = 1"
     Print #fileNo, addTab(indent + 0); "WITH UR;"
   End If
 ' ### ENDIF IVK ###

   genCondenseChangeLogNlDdl fileNo, changeLogClassIndex, qualTabNameChangeLogNl, gc_tempTabNameChangeLogNl, qualSeqNameOid, ddlType, thisOrgIndex, thisPoolIndex, indent
 End Sub
 
 'Parameter withTempTable added due to change on View V_CL_GENERICASPECT (defect 19001 wf)
 Private Sub genCondOuterJoin( _
   fileNo As Integer, _
   classIndex As Integer, _
   classIndexAh As Integer, _
   clMode As ChangeLogMode, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef tupVar1 As String, _
   ByRef tupVar1Ah As String, _
   ByRef tupVar2 As String, _
   ByRef fkAttrName As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional indent As Integer = 1, _
   Optional ByRef referredColumns As String = "", _
   Optional ByVal forGen As Boolean = False, _
   Optional ByRef lrtOidRef As String = "PRIV.INLRT", _
   Optional withTempTable As Boolean = True _
 )
   Dim thisClassShortName As String
   Dim thisClassIndex As Integer
   Dim thisTupVar As String

   If classIndex > 0 Then
     thisClassIndex = classIndex
     thisTupVar = tupVar1
   ElseIf classIndexAh > 0 Then
     thisClassIndex = classIndexAh
     thisTupVar = tupVar1Ah
   Else
     Exit Sub
   End If

   thisClassShortName = g_classes.descriptors(thisClassIndex).shortName

   Dim thisReferredColumns As String
   Dim parFkAttrName As String
   If forGen Then
     parFkAttrName = genSurrogateKeyName(ddlType, thisClassShortName)
     thisReferredColumns = referredColumns & IIf(referredColumns <> "", ",", "") & parFkAttrName
   Else
     parFkAttrName = g_anOid
     thisReferredColumns = referredColumns
   End If

   If clMode = eclLrt Then
     Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
 
     'added parameter withTempTable (defect 19001 wf)
     genTabSubQueryByEntityIndex thisClassIndex, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, (clMode = eclLrt), forGen, tupVar2, thisReferredColumns, indent + 1, , "", withTempTable

     Print #fileNo, addTab(indent + 0); "ON"
     Print #fileNo, addTab(indent + 1); thisTupVar; "."; fkAttrName; " = "; tupVar2; "."; parFkAttrName

     If g_classes.descriptors(thisClassIndex).isUserTransactional Then
       Print #fileNo, addTab(indent + 2); "AND"
       Print #fileNo, addTab(indent + 1); "("
 ' ### IF IVK ###
       Print #fileNo, addTab(indent + 2); "(("; tupVar2; "."; g_anIsLrtPrivate; " = 0) AND ("; tupVar2; "."; g_anIsDeleted; " = 0) AND ("; tupVar2; "."; g_anInLrt; " IS NULL OR "; tupVar2; "."; g_anInLrt; " <> "; lrtOidRef; "))"
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(indent + 2); "(("; tupVar2; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVar2; "."; g_anInLrt; " IS NULL OR "; tupVar2; "."; g_anInLrt; " <> "; lrtOidRef; "))"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(indent + 3); "OR"
       Print #fileNo, addTab(indent + 2); "(("; tupVar2; "."; g_anIsLrtPrivate; " = 1) AND ("; tupVar2; ".LRTSTATE <> "; CStr(lrtStatusDeleted); ") AND ("; tupVar2; "."; g_anInLrt; " = "; lrtOidRef; "))"
       Print #fileNo, addTab(indent + 1); ")"
       If forGen Then
         Print #fileNo, addTab(indent + 2); "AND"
         Print #fileNo, addTab(indent + 1); "("
         Print #fileNo, addTab(indent + 2); tupVar2; ".ROWNUM = 1"
         Print #fileNo, addTab(indent + 1); ")"
       End If
     End If
   Else
     Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
     If forGen Then
       Print #fileNo, addTab(indent + 1); "(SELECT T.*,ROWNUMBER() OVER (PARTITION BY "; parFkAttrName; " ORDER BY "; _
                                          "(CASE WHEN "; g_anValidTo; " > CURRENT DATE THEN TIMESTAMPDIFF(16, CHAR("; g_anValidTo; " - CURRENT DATE)) "; _
                                          "ELSE TIMESTAMPDIFF(16, CHAR(CURRENT DATE - "; g_anValidTo; ")) + 10000000 END)"; _
                                          ") AS ROWNUM FROM "; genQualTabNameByClassIndex(thisClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen); " T) "; tupVar2; ""
     Else
       Print #fileNo, addTab(indent + 1); genQualTabNameByClassIndex(thisClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen); " "; tupVar2
     End If
     Print #fileNo, addTab(indent + 0); "ON"
     Print #fileNo, addTab(indent + 1); thisTupVar; "."; fkAttrName; " = "; tupVar2; "."; parFkAttrName
     If forGen Then
       Print #fileNo, addTab(indent + 2); "AND"
       Print #fileNo, addTab(indent + 1); "("
       Print #fileNo, addTab(indent + 2); tupVar2; ".ROWNUM = 1"
       Print #fileNo, addTab(indent + 1); ")"
     End If
   End If
 End Sub
 
 
 ' ### IF IVK ###
 Sub genRetrieveSr0ContextForSr1Validity( _
   fileNo As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ddlType As DdlTypeId, _
   lrtAware As Boolean, _
   Optional indent As Integer = 1, _
   Optional ByRef lrtOidVar As String = "lrtOid_in", _
   Optional skipNl As Boolean = False _
 )
   ' Mapping of SR0Validity-columns to Sr1Validity-Records could also be done via 'CL-Attribute Mapping', but this is far more efficient

   Dim forNsr1Validity As Boolean
   Dim i As Integer
   For i = 1 To 2
     forNsr1Validity = (i = 2)

     If forNsr1Validity Then
       genProcSectionHeader fileNo, "special treatment of NSR1Validity: retrieve " & g_anSr0Context & " / SR1CONTEXT", indent
     Else
       genProcSectionHeader fileNo, "special treatment of SR1Validity: retrieve " & g_anSr0Context & "", indent, skipNl
     End If

     Print #fileNo, addTab(indent + 0); "UPDATE"
     Print #fileNo, addTab(indent + 1); gc_tempTabNameChangeLog; " TCL"
     Print #fileNo, addTab(indent + 0); "SET"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "TCL."; g_anSr0Context; ","
     Print #fileNo, addTab(indent + 1); "TCL.CSBAUMUSTER,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE1,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE2,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE3,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE4,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE5,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE6,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE7,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE8,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE9,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODE10,"

     If forNsr1Validity Then
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE1,"
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE2,"
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE3,"
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE4,"
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE5,"
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE6,"
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE7,"
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE8,"
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE9,"
       Print #fileNo, addTab(indent + 1); "TCL.SR1CODE10,"
     End If

     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID1,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID2,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID3,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID4,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID5,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID6,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID7,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID8,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID9,"
     Print #fileNo, addTab(indent + 1); "TCL.SR0CODEOID10"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "="
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "SELECT"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CONTEXT,   SR0.SR0CONTEXT),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.CSBAUMUSTER,  SR0.CSBAUMUSTER),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE1,     S01."; g_anCodeNumber; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE2,     S02."; g_anCodeNumber; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE3,     S03."; g_anCodeNumber; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE4,     S04."; g_anCodeNumber; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE5,     S05."; g_anCodeNumber; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE6,     S06."; g_anCodeNumber; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE7,     S07."; g_anCodeNumber; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE8,     S08."; g_anCodeNumber; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE9,     S09."; g_anCodeNumber; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODE10,    S10."; g_anCodeNumber; "),"

     If forNsr1Validity Then
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE1,     S101."; g_anCodeNumber; "),"
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE2,     S102."; g_anCodeNumber; "),"
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE3,     S103."; g_anCodeNumber; "),"
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE4,     S104."; g_anCodeNumber; "),"
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE5,     S105."; g_anCodeNumber; "),"
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE6,     S106."; g_anCodeNumber; "),"
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE7,     S107."; g_anCodeNumber; "),"
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE8,     S108."; g_anCodeNumber; "),"
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE9,     S109."; g_anCodeNumber; "),"
       Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR1CODE10,    S110."; g_anCodeNumber; "),"
     End If

     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID1,  S01."; g_anOid; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID2,  S02."; g_anOid; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID3,  S03."; g_anOid; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID4,  S04."; g_anOid; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID5,  S05."; g_anOid; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID6,  S06."; g_anOid; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID7,  S07."; g_anOid; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID8,  S08."; g_anOid; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID9,  S09."; g_anOid; "),"
     Print #fileNo, addTab(indent + 2); "COALESCE(TCL.SR0CODEOID10, S10."; g_anOid; ")"
     Print #fileNo, addTab(indent + 1); "FROM"

     genTabSubQueryByEntityIndex g_classIndexGenericAspect, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "SR0", _
       "SR0CONTEXT,CSBAUMUSTER,S0CS01_OID,S0CS02_OID,S0CS03_OID,S0CS04_OID,S0CS05_OID,S0CS06_OID,S0CS07_OID,S0CS08_OID,S0CS09_OID,S0CS10_OID", _
       indent + 2, , lrtOidVar
     Print #fileNo, addTab(indent + 1); "INNER JOIN"
     If forNsr1Validity Then
       genTabSubQueryByEntityIndex g_classIndexGenericAspect, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "SR1", _
         "E0VEX0_OID,S1CT01_OID,S1CT02_OID,S1CT03_OID,S1CT04_OID,S1CT05_OID,S1CT06_OID,S1CT07_OID,S1CT08_OID,S1CT09_OID,S1CT10_OID", _
         indent + 2, , lrtOidVar
     Else
       genTabSubQueryByEntityIndex g_classIndexGenericAspect, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "SR1", _
         "E0VEX0_OID", _
         indent + 2, , lrtOidVar
     End If

     Print #fileNo, addTab(indent + 1); "ON"
     Print #fileNo, addTab(indent + 2); "SR0."; g_anOid; " = SR1.E0VEX0_OID"

     If forNsr1Validity Then
       Print #fileNo, addTab(indent + 1); "INNER JOIN"
       genTabSubQueryByEntityIndex g_classIndexGenericAspect, eactClass, fileNo, thisOrgIndex, thisPoolIndex, ddlType, lrtAware, False, "NSR1", _
         "E1VEX1_OID", _
         indent + 2, , lrtOidVar

       Print #fileNo, addTab(indent + 1); "ON"
       Print #fileNo, addTab(indent + 2); "SR1."; g_anOid; " = NSR1.E1VEX1_OID"
     End If

     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S01", "S0CS01_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS01_OID", , lrtOidVar
     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S02", "S0CS02_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS02_OID", , lrtOidVar
     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S03", "S0CS03_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS03_OID", , lrtOidVar
     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S04", "S0CS04_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS04_OID", , lrtOidVar
     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S05", "S0CS05_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS05_OID", , lrtOidVar
     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S06", "S0CS06_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS06_OID", , lrtOidVar
     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S07", "S0CS07_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS07_OID", , lrtOidVar
     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S08", "S0CS08_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS08_OID", , lrtOidVar
     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S09", "S0CS09_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS09_OID", , lrtOidVar
     genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR0", "", "S10", "S0CS10_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S0CS10_OID", , lrtOidVar

     If forNsr1Validity Then
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S101", "S1CT01_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT01_OID", , lrtOidVar
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S102", "S1CT02_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT02_OID", , lrtOidVar
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S103", "S1CT03_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT03_OID", , lrtOidVar
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S104", "S1CT04_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT04_OID", , lrtOidVar
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S105", "S1CT05_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT05_OID", , lrtOidVar
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S106", "S1CT06_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT06_OID", , lrtOidVar
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S107", "S1CT07_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT07_OID", , lrtOidVar
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S108", "S1CT08_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT08_OID", , lrtOidVar
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S109", "S1CT09_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT09_OID", , lrtOidVar
       genCondOuterJoin fileNo, g_classIndexGenericCode, -1, IIf(lrtAware, eclLrt, eclSetProd), thisOrgIndex, thisPoolIndex, "SR1", "", "S110", "S1CT10_OID", ddlType, indent + 1, g_anOid & "," & g_anCodeNumber & ",S1CT10_OID", , lrtOidVar
     End If

     Print #fileNo, addTab(indent + 1); "WHERE"
     If forNsr1Validity Then
       Print #fileNo, addTab(indent + 2); "(TCL.ahObjectId = NSR1."; g_anOid; ")"
     Else
       Print #fileNo, addTab(indent + 2); "(TCL.ahObjectId = SR1."; g_anOid; ")"
     End If
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "WHERE"
     If forNsr1Validity Then
       Print #fileNo, addTab(indent + 1); "(TCL.ahClassId = '"; getClassIdStrByIndex(g_classIndexNSr1Validity); "')"
     Else
       Print #fileNo, addTab(indent + 1); "(TCL.ahClassId = '"; getClassIdStrByIndex(g_classIndexSr1Validity); "')"
     End If
     Print #fileNo, addTab(indent + 0); ";"
   Next i
 End Sub
 ' ### ENDIF IVK ###
 
 
 Sub genChangeLogSupportForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef relRefs As RelationshipDescriptorRefs, _
   ByRef qualSourceTabName As String, _
   ByRef qualSourceNlTabName As String, _
   ByRef qualTargetTabName As String, _
   ByRef qualTargetNlTabName As String, _
   ByRef qualRefGenTabName As String, _
   ByRef qualAggHeadRefNlTabName As String, _
   ByRef qualAggHeadTabName As String, _
   ByVal thisOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoClView As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional clMode As ChangeLogMode = eclLrt _
 )
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim sectionIndex As String
   Dim dbAcmEntityType As String
   Dim isLogChange As Boolean
   Dim isGenForming As Boolean
   Dim hasNlAttrs As Boolean
   Dim entityIsTransactional As Boolean
   Dim entityIdStrList As String
   Dim aggHeadShortClassName As String
   Dim aggHeadClassIndex As Integer
   Dim hasSubClass As Boolean
   Dim entityIdStr As String
   Dim nlAttrRefs As AttrDescriptorRefs
   Dim attrRefs As AttrDescriptorRefs
   Dim hasOwnTable As Boolean
   Dim isAggHead As Boolean
 ' ### IF IVK ###
   Dim hasNoIdentity As Boolean
   Dim isPsTagged As Boolean
   Dim isGenericAspect As Boolean
   Dim hasPriceAssignmentSubClass As Boolean
   Dim priceAssignmentSubClassIdList As String
   Dim priceAssignmentHasNlAttrs As Boolean
   Dim isSubjectToPreisDurchschuss As Boolean
   Dim condenseData As Boolean
   Dim enforceLrtChangeComment As Boolean
   Dim isNationalizable As Boolean
   Dim hasIsNationalInclSubClasses As Boolean

   hasPriceAssignmentSubClass = False
   priceAssignmentSubClassIdList = ""
   priceAssignmentHasNlAttrs = False
   isGenericAspect = False
   enforceLrtChangeComment = False
 ' ### ELSE IVK ###
 '
 ' ### ENDIF IVK ###
   isAggHead = False

   If acmEntityType = eactClass Then
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
 ' ### IF IVK ###
       isGenericAspect = UCase(g_classes.descriptors(acmEntityIndex).className) = UCase(clnGenericAspect) And Not forGen And Not forNl
 ' ### ENDIF IVK ###

       If forNl Then
         entityName = genNlObjName(g_classes.descriptors(acmEntityIndex).className, , forGen)
         entityShortName = genNlObjShortName(g_classes.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Class (NL-Text)"
 ' ### IF IVK ###
         hasNoIdentity = False
 ' ### ENDIF IVK ###
         isLogChange = False
         isGenForming = False
         attrRefs = g_classes.descriptors(acmEntityIndex).nlAttrRefsInclSubclasses
         hasOwnTable = True
       Else
         entityName = g_classes.descriptors(acmEntityIndex).className
         entityShortName = g_classes.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Class"
 ' ### IF IVK ###
         hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
 ' ### ENDIF IVK ###
         isLogChange = g_classes.descriptors(acmEntityIndex).logLastChange
         isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
         If forGen Then
           hasNlAttrs = g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses
         Else
           hasNlAttrs = g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses
         End If
         attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs

 ' ### IF IVK ###
         hasPriceAssignmentSubClass = g_classes.descriptors(acmEntityIndex).hasPriceAssignmentSubClass
         isSubjectToPreisDurchschuss = g_classes.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss
         enforceLrtChangeComment = g_classes.descriptors(acmEntityIndex).enforceLrtChangeComment

         If Not hasPriceAssignmentSubClass Then
           priceAssignmentHasNlAttrs = hasNlAttrs
         End If
 ' ### ENDIF IVK ###
         isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex)
       End If
       entityIsTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       entityIdStrList = getSubClassIdStrListByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex)
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       hasSubClass = g_classes.descriptors(acmEntityIndex).hasSubClass
       nlAttrRefs = g_classes.descriptors(acmEntityIndex).nlAttrRefsInclSubclasses
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
 ' ### IF IVK ###
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       isNationalizable = g_classes.descriptors(acmEntityIndex).isNationalizable And Not forNl
       hasIsNationalInclSubClasses = g_classes.descriptors(acmEntityIndex).hasIsNationalInclSubClasses And Not forNl
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       If forNl Then
         entityName = genNlObjName(g_relationships.descriptors(acmEntityIndex).relName, , forGen)
         entityShortName = genNlObjShortName(g_relationships.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Relationship (NL-Text)"
         isLogChange = False
         attrRefs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
       Else
         entityName = g_relationships.descriptors(acmEntityIndex).relName
         entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Relationship"
         isLogChange = g_relationships.descriptors(acmEntityIndex).logLastChange
         hasNlAttrs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0
         attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
 ' ### IF IVK ###
         priceAssignmentHasNlAttrs = hasNlAttrs
 ' ### ENDIF IVK ###
       End If
       isGenForming = False
       entityIsTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       entityIdStrList = "'" & g_relationships.descriptors(acmEntityIndex).relIdStr & "'"
       dbAcmEntityType = "R"
       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       hasSubClass = False
       entityIdStr = ""
       nlAttrRefs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
       hasOwnTable = True
 ' ### IF IVK ###
       hasNoIdentity = False
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       condenseData = False
       isNationalizable = g_relationships.descriptors(acmEntityIndex).isNationalizable
       hasIsNationalInclSubClasses = g_relationships.descriptors(acmEntityIndex).hasIsNationalInclSubClasses
       isSubjectToPreisDurchschuss = g_relationships.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss
 ' ### ENDIF IVK ###
   Else
     Exit Sub
   End If

   Dim isPrimaryOrg As Boolean
   isPrimaryOrg = (thisOrgIndex = g_primaryOrgIndex)

   If aggHeadClassIndex > 0 Then
     aggHeadShortClassName = g_classes.descriptors(aggHeadClassIndex).shortName
   End If

 ' ### IF IVK ###
   If isSubjectToPreisDurchschuss Then
       Dim i As Integer
       For i = 1 To UBound(g_classes.descriptors(g_classIndexGenericAspect).subclassIndexesRecursive)
           If Not g_classes.descriptors(g_classes.descriptors(g_classIndexGenericAspect).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(g_classIndexGenericAspect).subclassIndexesRecursive(i)).isPriceAssignment Then
             priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(g_classIndexGenericAspect).subclassIndexesRecursive(i)).classIdStr & "'"
             priceAssignmentHasNlAttrs = priceAssignmentHasNlAttrs Or g_classes.descriptors(g_classes.descriptors(g_classIndexGenericAspect).subclassIndexesRecursive(i)).hasNlAttrsInNonGenInclSubClasses
           End If
       Next i
   End If

 ' ### ENDIF IVK ###
   Dim qualTabNameLrt As String
   Dim qualTabNameLrtNl As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, srcPoolIndex)
   qualTabNameLrtNl = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, srcPoolIndex, , , , True)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)
 
   ' ####################################################################################################################
   ' #    View for ChangeLog
   ' ####################################################################################################################

   genChangeLogViewDdl acmEntityIndex, acmEntityType, qualSourceTabName, qualRefGenTabName, qualSourceNlTabName, _
                       qualTargetTabName, qualTargetNlTabName, qualAggHeadTabName, _
                       thisOrgIndex, srcPoolIndex, dstPoolIndex, fileNoClView, ddlType, forGen, clMode

   genChangeLogViewDdl2 acmEntityIndex, acmEntityType, qualSourceTabName, qualRefGenTabName, qualSourceNlTabName, _
                       qualTargetTabName, qualTargetNlTabName, qualAggHeadTabName, _
                       thisOrgIndex, srcPoolIndex, dstPoolIndex, fileNoClView, ddlType, forGen, clMode

   ' ####################################################################################################################
   ' #    SP for creating ChangeLog
   ' ####################################################################################################################
   Dim qualProcName As String
   Dim seqNo As Integer
   Dim allRegAttrsProcessed As Boolean
   Dim allNlAttrsProcessed As Boolean
   Dim lastRegAttrsProcessed As Integer
   Dim lastNlAttrsProcessed As Integer
   Dim numAttrsProcessedThisLoop As Integer
   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors
   Dim changeLogAcHasBeenFilled As Boolean

   Dim procNamePrefix As String
 ' ### IF IVK ###
   If clMode = eclLrt Then
     procNamePrefix = spnLrtGenChangelog
   Else
     procNamePrefix = spnSpGenChangelog
   End If
 ' ### ELSE IVK ###
 ' procNamePrefix = spnLrtGenChangelog
 ' ### ENDIF IVK ###
 
   Dim qualViewName As String
 
   allRegAttrsProcessed = False
   allNlAttrsProcessed = False
   lastRegAttrsProcessed = 0
   lastNlAttrsProcessed = 0
   seqNo = 0
   changeLogAcHasBeenFilled = False
   While Not allRegAttrsProcessed Or Not allNlAttrsProcessed
     numAttrsProcessedThisLoop = 0

     qualProcName = _
       genQualProcNameByEntityIndex( _
         acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , forNl, procNamePrefix, , , , IIf(seqNo = 0, "", CStr(seqNo)) _
       )
 
       printSectionHeader "SP for creating ChangeLog (" & genClModeDescription(clMode) & ") on """ & qualTargetTabName & _
                          """ (" & entityTypeDescr & " """ & g_sections.descriptors(sectionIndex).sectionName & "." & entityName & """)", fileNo

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcName
     Print #fileNo, addTab(0); "("

     If clMode = eclLrt Then
       genProcParm fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to create the Log for"
 ' ### IF IVK ###
       genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to create the Log for"
 ' ### ENDIF IVK ###
       genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser"
       genProcParm fileNo, "IN", "opId_in", g_dbtEnumId, True, "identifies the operation (insert, update, delete) to create the Log for"
       genProcParm fileNo, "IN", "commitTs_in", "TIMESTAMP", True, "marks the execution timestamp of the LRT"
 ' ### IF IVK ###
       genProcParm fileNo, "IN", "autoPriceSetProductive_in", g_dbtBoolean, True, "specifies whether prices are set productive"
       genProcParm fileNo, "IN", "settingManActCP_in", g_dbtBoolean, True, "setting 'manuallyActivateCodePrice'"
       genProcParm fileNo, "IN", "settingManActTP_in", g_dbtBoolean, True, "setting 'manuallyActivateTypePrice'"
       genProcParm fileNo, "IN", "settingManActSE_in", g_dbtBoolean, True, "setting 'manuallyActivateStandardEquipmentPrice'"
       genProcParm fileNo, "IN", "settingSelRelease_in", g_dbtBoolean, True, "setting 'useSelectiveReleaseProcess'"

       If Not isPrimaryOrg Then
         genProcParm fileNo, "IN", "isFtoLrt_in", g_dbtBoolean, True, "'1' if and only if this LRT 'is central data transfer'"
       End If
 ' ### ENDIF IVK ###
     Else
 ' ### IF IVK ###
       genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to create the Log for"
 ' ### ENDIF IVK ###
       genProcParm fileNo, "IN", "opId_in", g_dbtEnumId, True, "identifies the operation (insert, update, delete) to create the Log for"
       genProcParm fileNo, "IN", "commitTs_in", "TIMESTAMP", True, "marks the timestamp of 'Setting Productive'"
     End If
     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows returned in the log"

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader fileNo, "declare conditions", , True
     genCondDecl fileNo, "alreadyExist", "42710"
     genCondDecl fileNo, "notFound", "02000"

     genProcSectionHeader fileNo, "declare variables"
     genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
 ' ### IF IVK ###
     If clMode = eclSetProd And Not isPsTagged Then
       genVarDecl fileNo, "v_divisionOid", g_dbtOid, "NULL"
     End If
 ' ### ENDIF IVK ###
     genSpLogDecl fileNo
 
     genProcSectionHeader fileNo, "declare continue handler"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"

     Dim hasColumnToFilter As Boolean
 ' ### IF IVK ###
     Dim ignoreLastUpdateTimestamp As Boolean
     ignoreLastUpdateTimestamp = Not isAggHead Or (forGen And Not hasNoIdentity) Or forNl Or (clMode <> eclSetProd)
 
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
     initAttributeTransformation transformation, IIf(ignoreLastUpdateTimestamp, 4, 3)
 ' ### ELSE IVK ###
 '   initAttributeTransformation transformation, 4
 ' ### ENDIF IVK ###
     setAttributeMapping transformation, 1, conCreateUser, ""
     setAttributeMapping transformation, 2, conUpdateUser, ""
     setAttributeMapping transformation, 3, conCreateTimestamp, ""
 ' ### IF IVK ###
     If ignoreLastUpdateTimestamp Then
       setAttributeMapping transformation, 4, conLastUpdateTimestamp, ""
     End If
 ' ### ELSE IVK ###
 '   setAttributeMapping transformation, 4, conLastUpdateTimestamp, ""
 ' ### ENDIF IVK ###

     tabColumns = nullEntityColumnDescriptors
     genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, _
       fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, False, forGen, edomNone

     Dim attrIndex As Integer
     Dim numAttrs As Integer
     ' determine whether this table has some column eligible for selective filtering
     attrIndex = lastRegAttrsProcessed + 1
 ' ### IF IVK ###
     numAttrs = IIf(isGenForming And (forGen Or hasNoIdentity) And (seqNo = 0), 2, IIf(seqNo = 0, 1, 0))
 ' ### ELSE IVK ###
 '   numAttrs = IIf(isGenForming And forGen And (seqNo = 0), 2, IIf(seqNo = 0, 1, 0))
 ' ### ENDIF IVK ###
     hasColumnToFilter = False
 ' ### IF IVK ###
     If ((clMode = eclLrt) Or (clMode = eclSetProd)) And hasSubClass Then
 ' ### ELSE IVK ###
 '   If clMode = eclLrt And hasSubClass Then
 ' ### ENDIF IVK ###
       While Not hasColumnToFilter And (attrIndex <= tabColumns.numDescriptors)
 ' ### IF IVK ###
           If (tabColumns.descriptors(attrIndex).columnCategory And eacRegular) Or (tabColumns.descriptors(attrIndex).columnCategory And eacFkOid) Or ((clMode = eclLrt) And (tabColumns.descriptors(attrIndex).columnCategory And eacSetProdMeta)) Then
 ' ### ELSE IVK ###
 '         If (.columnCategory And eacRegular) Or (.columnCategory And eacFkOid) Then
 ' ### ENDIF IVK ###
             If tabColumns.descriptors(attrIndex).acmAttributeIndex > 0 Then
                 hasColumnToFilter = hasColumnToFilter Or (g_classes.descriptors(g_attributes.descriptors(tabColumns.descriptors(attrIndex).acmAttributeIndex).acmEntityIndex).superClassIndex > 0)
             End If
             numAttrs = numAttrs + 1
           End If
         attrIndex = attrIndex + 1
       Wend

       attrIndex = lastNlAttrsProcessed + 1
       While Not hasColumnToFilter And (attrIndex <= nlAttrRefs.numDescriptors)
         If nlAttrRefs.descriptors(attrIndex).refType = eadrtAttribute Then
 ' ### IF IVK ###
             If g_attributes.descriptors(nlAttrRefs.descriptors(attrIndex).refIndex).isNl And (forGen Or hasNoIdentity) = g_attributes.descriptors(nlAttrRefs.descriptors(attrIndex).refIndex).isTimeVarying Then
 ' ### ELSE IVK ###
 '           If .isNl And forGen = .isTimeVarying Then
 ' ### ENDIF IVK ###
               hasColumnToFilter = hasColumnToFilter Or (g_classes.descriptors(g_attributes.descriptors(nlAttrRefs.descriptors(attrIndex).refIndex).acmEntityIndex).superClassIndex > 0)
               numAttrs = numAttrs + 1
             End If
         End If
         attrIndex = attrIndex + 1
       Wend
     End If
 
 ' ### IF IVK ###
     genDdlForTempTablesChangeLog fileNo, thisOrgIndex, dstPoolIndex, ddlType, 1, _
       (isAggHead Or (enforceLrtChangeComment And clMode = eclLrt) Or hasNlAttrs) And (seqNo = 0), True


   '  genDdlForTempTablesChangeLog fileNo, thisOrgIndex, dstPoolIndex, ddlType, 1, _
    '   (isAggHead Or (enforceLrtChangeComment And clMode = eclLrt) Or hasNlAttrs) And (seqNo = 0), hasColumnToFilter
 
 ' ### ELSE IVK ###
 '   genDdlForTempTablesChangeLog fileNo, thisOrgIndex, dstPoolIndex, ddlType, 1, (isAggHead Or hasNlAttrs) And (seqNo = 0), hasColumnToFilter
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
     If clMode = eclSetProd Then
       If condenseData Then
         genDdlForTempOids fileNo, 1
       Else
         genDdlForTempTablesSp fileNo, 1
       End If
     End If
 ' ### ENDIF IVK ###
 
 ' ### IF IVK ###
     If clMode = eclLrt Then
       If isPrimaryOrg Then
         genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "autoPriceSetProductive_in", _
                                   "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out"
       Else
         genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "autoPriceSetProductive_in", _
                                   "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out"
       End If
     Else
       genSpLogProcEnter fileNo, qualProcName, ddlType, , "opId_in", "#commitTs_in", "rowCount_out"
     End If

 ' ### ELSE IVK ###
 '   If clMode = eclLrt Then
 '     genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "rowCount_out"
 '   Else
 '     genSpLogProcEnter fileNo, qualProcName, ddlType, , "opId_in", "#commitTs_in", "rowCount_out"
 '   End If
 ' ### ENDIF IVK ###
 
     Print #fileNo,
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"

 ' ### IF IVK ###
     If clMode = eclSetProd And Not isPsTagged Then
       genProcSectionHeader fileNo, "determine division OID"
       Print #fileNo, addTab(1); "SET v_divisionOid ="
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "PDIDIV_OID"
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); g_qualTabNameProductStructure
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); g_anOid; " = psOid_in"
       Print #fileNo, addTab(1); ");"
     End If

 ' ### ENDIF IVK ###
     Print #fileNo,
     If seqNo = 0 Then
 ' ### IF IVK ###
       If clMode = eclLrt And Not condenseData Then
 ' ### ELSE IVK ###
 '     If clMode = eclLrt Then
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); "IF opId_in = "; CStr(lrtStatusLocked); " THEN"
 ' ### IF IVK ###
         If isGenericAspect Then
           genRetrieveSr0ContextForSr1Validity fileNo, thisOrgIndex, srcPoolIndex, ddlType, True, 2, "lrtOid_in", True
         End If
 
         ' if we found some (>2) NL-Text Column to process we need to increase the number of processed attributes
         ' each NL-Text Column eats up about 50% of the source code volume compared to a 'regular attribute'
         numAttrsProcessedThisLoop = _
             genAddNlTextChangeLogDdlForIndividualAttrs( _
               fileNo, acmEntityIndex, acmEntityType, dbAcmEntityType, entityIdStrList, gc_tempTabNameChangeLog, _
               gc_tempTabNameChangeLogNl, qualSourceNlTabName, genSurrogateKeyName(ddlType, entityShortName), _
               qualAggHeadRefNlTabName, genSurrogateKeyName(ddlType, aggHeadShortClassName), nlAttrRefs, _
               relRefs, forGen, "lrtOid_in", "psOid_in", thisOrgIndex, srcPoolIndex, True, True, ddlType, 2, Not isGenericAspect _
           )

         numAttrsProcessedThisLoop = IIf(numAttrsProcessedThisLoop > 2, 1, 0) + IIf(isGenericAspect, 5, 0)
 ' ### ELSE IVK ###
 '       ' if we found some (>2) NL-Text Column to process we need to increase the number of processed attributes
 '       ' each NL-Text Column eats up about 50% of the source code volume compared to a 'regular attribute'
 '       numAttrsProcessedThisLoop = _
 '         ( _
 '           genAddNlTextChangeLogDdlForIndividualAttrs( _
 '             fileNo, acmEntityIndex, acmEntityType, dbAcmEntityType, entityIdStrList, gc_tempTabNameChangeLog, _
 '             gc_tempTabNameChangeLogNl, qualSourceNlTabName, genSurrogateKeyName(ddlType, entityShortName), _
 '             qualAggHeadRefNlTabName, genSurrogateKeyName(ddlType, aggHeadShortClassName), nlAttrRefs, _
 '             relRefs, forGen, "lrtOid_in", thisOrgIndex, srcPoolIndex, True, True, ddlType, 2, True _
 '         ) _
 '       )
 '       numAttrsProcessedThisLoop = IIf(numAttrsProcessedThisLoop > 2, 1, 0)
 ' ### ENDIF IVK ###

         Print #fileNo, addTab(1); "ELSEIF opId_in = "; CStr(lrtStatusCreated); " OR opId_in = "; CStr(lrtStatusDeleted); " THEN"
 ' ### IF IVK ###
       ElseIf clMode = eclSetProd And Not condenseData And hasNlAttrs Then
         Print #fileNo, addTab(1); "IF opId_in = "; CStr(lrtStatusLocked); " THEN"

         If isGenericAspect Then
           genRetrieveSr0ContextForSr1Validity fileNo, thisOrgIndex, srcPoolIndex, ddlType, False, 2, "", True
         End If

         numAttrsProcessedThisLoop = _
             genAddNlTextChangeLogDdlForIndividualAttrs( _
               fileNo, acmEntityIndex, acmEntityType, dbAcmEntityType, entityIdStrList, gc_tempTabNameChangeLog, gc_tempTabNameChangeLogNl, _
               qualSourceNlTabName, genSurrogateKeyName(ddlType, entityShortName), qualAggHeadRefNlTabName, _
               genSurrogateKeyName(ddlType, aggHeadShortClassName), nlAttrRefs, relRefs, forGen, "", "psOid_in", _
               thisOrgIndex, srcPoolIndex, False, True, ddlType, 2, True _
           )

         numAttrsProcessedThisLoop = IIf(numAttrsProcessedThisLoop > 2, 1, 0) + IIf(isGenericAspect, 5, 0)

         Print #fileNo, addTab(1); "ELSEIF opId_in = "; CStr(lrtStatusCreated); " OR opId_in = "; CStr(lrtStatusDeleted); " THEN"
       Else
         If isGenericAspect Then
           Print #fileNo, addTab(1); "IF opId_in = "; CStr(lrtStatusLocked); " THEN"
           genRetrieveSr0ContextForSr1Validity fileNo, thisOrgIndex, srcPoolIndex, ddlType, False, 2, "lrtOid_in", True
           numAttrsProcessedThisLoop = 5
           Print #fileNo, addTab(1); "ELSEIF opId_in = "; CStr(lrtStatusCreated); " OR opId_in = "; CStr(lrtStatusDeleted); " THEN"
         Else
           If condenseData Then
             Print #fileNo, addTab(1); "IF opId_in = "; CStr(lrtStatusCreated); " THEN"
           Else
             Print #fileNo, addTab(1); "IF opId_in = "; CStr(lrtStatusCreated); " OR opId_in = "; CStr(lrtStatusDeleted); " THEN"
           End If
 ' ### ELSE IVK ###
 '         Print #fileNo, addTab(1); "IF opId_in = " ; CStr(lrtStatusCreated) ; " OR opId_in = " ; CStr(lrtStatusDeleted) ; " THEN"
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
         End If
 ' ### ENDIF IVK ###
       End If

 ' ### IF IVK ###
       genGenChangeLogRecordDdl acmEntityIndex, acmEntityType, qualSourceTabName, qualTargetTabName, qualSeqNameOid, _
                                gc_tempTabNameChangeLog, IIf(condenseData, "inserts", "inserts and deletes"), _
                                "opId_in", thisOrgIndex, _
                                dstPoolIndex, fileNo, ddlType, forGen, , , , , clMode, , , , , , , _
                                IIf(clMode = eclLrt, "cdUserId_in", ""), IIf(condenseData And clMode = eclSetProd, CStr(lrtStatusCreated), ""), False
 ' ### ELSE IVK ###
 '     genGenChangeLogRecordDdl acmEntityIndex, acmEntityType, qualSourceTabName, qualTargetTabName, qualSeqNameOid, _
 '                              gc_tempTabNameChangeLog, "inserts and deletes", _
 '                              "opId_in", thisOrgIndex, _
 '                              dstPoolIndex, fileNo, ddlType, forGen, , , , , clMode, , , , , , , _
 '                              IIf(clMode = eclLrt, "cdUserId_in", ""), , "", False
 ' ### ENDIF IVK ###
       If hasNlAttrs Then
         Print #fileNo,
 ' ### IF IVK ###
         genGenChangeLogRecordDdl acmEntityIndex, acmEntityType, qualSourceTabName, qualTargetTabName, qualSeqNameOid, _
                                  gc_tempTabNameChangeLog, IIf(condenseData, "inserts", "inserts and deletes"), _
                                  "opId_in", thisOrgIndex, _
                                  dstPoolIndex, fileNo, ddlType, forGen, True, , , , clMode, , , , , , , IIf(clMode = eclLrt, "cdUserId_in", "")
 ' ### ELSE IVK ###
 '       genGenChangeLogRecordDdl acmEntityIndex, acmEntityType, qualSourceTabName, qualTargetTabName, qualSeqNameOid, _
 '                                gc_tempTabNameChangeLog, "inserts and deletes", _
 '                                "opId_in", thisOrgIndex, _
 '                                dstPoolIndex, fileNo, ddlType, forGen, True, , , , clMode, , , , , , , IIf(clMode = eclLrt, "cdUserId_in", "")
 ' ### ENDIF IVK ###
       End If

 ' ### IF IVK ###
       If Not condenseData Then
         Print #fileNo, addTab(1); "ELSEIF opId_in = "; CStr(lrtStatusUpdated); " THEN"
       End If
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(1); "ELSEIF opId_in = " ; CStr(lrtStatusUpdated) ; " THEN"
 ' ### ENDIF IVK ###
     Else
 ' ### IF IVK ###
       If Not condenseData Then
         Print #fileNo, addTab(1); "IF opId_in = "; CStr(lrtStatusUpdated); " THEN"
       End If
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(1); "IF opId_in = "; CStr(lrtStatusUpdated); " THEN"
 ' ### ENDIF IVK ###
     End If
 
 ' ### IF IVK ###
     If condenseData Then
       allRegAttrsProcessed = True
       allNlAttrsProcessed = True
     Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###


       If seqNo = 0 Then
         ' first procedure takes care of INSERT & DELETE - we thus handle one attribute less
         numAttrsProcessedThisLoop = numAttrsProcessedThisLoop + 1
       End If

       Dim ignoreForChangelog As Boolean
       Dim thisAttributeIndex As Integer

       Dim valuesStringForCTE As String
       Dim caseUpdateStringForCTE As String
       valuesStringForCTE = ""
       caseUpdateStringForCTE = ""

       Dim qualTabNameExpr As String
       qualTabNameExpr = genQualTabNameByEntityIndex(g_classIndexExpression, eactClass, ddlType, thisOrgIndex, srcPoolIndex, False)

       Dim qualTabName As String
       qualTabName = genQualTabNameByEntityIndex(g_classIndexGenericAspect, eactClass, ddlType, thisOrgIndex, srcPoolIndex, False)

       Dim splitVar As Boolean
       splitVar = isGenericAspect And (clMode = eclSetProd)

       Dim cdUserId_in As String
       cdUserId_in = IIf(clMode = eclLrt, "cdUserId_in", "V.cdUserId")


       Dim stringsPerType(15) As String
       'stringsPerType(1) :  _t
       'stringsPerType(2) :  _o
       'stringsPerType(3) :  _n
       'stringsPerType(4) :  _Dto
       'stringsPerType(5) :  _Dtn
       'stringsPerType(6) :  _Io
       'stringsPerType(7) :  _In
       'stringsPerType(8) :  _BIo
       'stringsPerType(9) :  _BIn
       'stringsPerType(10) :  _Do
       'stringsPerType(11) :  _Dn
       'stringsPerType(12) :  _Bo
       'stringsPerType(13) :  _Bn
       'stringsPerType(14) :  _To
       'stringsPerType(15) :  _Tn
       For i = 1 To 15
         stringsPerType(i) = "CASE bas.dbColumnName" & vbCrLf & "        WHEN 'DUMMYDUMMY' THEN NULL" & vbCrLf & "    "
       Next i

       ' generate change log records for changed regular attributes
       thisAttributeIndex = lastRegAttrsProcessed + 1

       While thisAttributeIndex <= tabColumns.numDescriptors
           If isClAttrCat(tabColumns.descriptors(thisAttributeIndex).columnCategory, clMode = eclLrt) Then
             ignoreForChangelog = False

             If tabColumns.descriptors(thisAttributeIndex).acmFkRelIndex > 0 Then
               If g_relationships.descriptors(tabColumns.descriptors(thisAttributeIndex).acmFkRelIndex).ignoreForChangelog Then
                 ignoreForChangelog = True
               End If
             End If

 ' ### IF IVK ###
             If clMode = eclLrt And tabColumns.descriptors(thisAttributeIndex).columnName = g_anIsBlockedPrice Then
               ignoreForChangelog = True
             End If

 ' ### ENDIF IVK ###
             If Not ignoreForChangelog Then
 ' ### IF IVK ###
               Dim columnTargetValue As String
               columnTargetValue = ""

               If clMode = eclLrt And tabColumns.descriptors(thisAttributeIndex).columnName = g_anStatus Then
                 Dim setManActConditional As Boolean
 '                setManActConditional = Not isPrimaryOrg And hasIsNationalInclSubClasses And isSubjectToPreisDurchschuss
                 setManActConditional = Not isPrimaryOrg And hasIsNationalInclSubClasses
 
                 columnTargetValue = _
                   IIf(isSubjectToPreisDurchschuss, "CASE WHEN (autoPriceSetProductive_in = 1) AND (" & g_anAhCid & " IN (" & priceAssignmentSubClassIdList & ")) THEN " & statusReadyToBeSetProductive & " ELSE ", "") & _
                   g_qualFuncNameGetLrtTargetStatus & "(" & _
                   g_anAhCid & "," & _
                   "CAST('" & gc_acmEntityTypeKeyClass & "' AS CHAR(1))," & _
                   IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (V." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActCP_in END)", "settingManActCP_in") & "," & _
                   IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (V." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActTP_in END)", "settingManActTP_in") & "," & _
                   IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (V." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActSE_in END)", "settingManActSE_in") & "," & _
                   "settingSelRelease_in" & _
                   ")" & _
                   IIf(isSubjectToPreisDurchschuss, " END", "")
               End If
               Dim alternativeColumnName As String
               alternativeColumnName = ""
               If tabColumns.descriptors(thisAttributeIndex).acmAttributeIndex > 0 Then
                 If g_attributes.descriptors(tabColumns.descriptors(thisAttributeIndex).acmAttributeIndex).isExpression Then
                   alternativeColumnName = genSurrogateKeyName(ddlType, g_attributes.descriptors(tabColumns.descriptors(thisAttributeIndex).acmAttributeIndex).shortName & "EXP")
                 End If
               End If

               genGenChangeLogRecordForCTEDdl "updates on '" & tabColumns.descriptors(thisAttributeIndex).columnName & "'" & " (" & thisAttributeIndex & ")", _
                                        fileNo, stringsPerType, valuesStringForCTE, caseUpdateStringForCTE, splitVar, qualTabNameExpr, _
                                        ddlType, tabColumns.descriptors(thisAttributeIndex).columnName, alternativeColumnName, g_domains.descriptors(tabColumns.descriptors(thisAttributeIndex).dbDomainIndex).dataType, clMode, tabColumns.descriptors(thisAttributeIndex).columnCategory, _
                                        columnTargetValue, tabColumns.descriptors(thisAttributeIndex).isNullable

             End If
           End If

         lastRegAttrsProcessed = thisAttributeIndex
         thisAttributeIndex = thisAttributeIndex + 1
       Wend
       allRegAttrsProcessed = allRegAttrsProcessed Or (thisAttributeIndex > tabColumns.numDescriptors)

     Dim viewNameSuffix As String
     viewNameSuffix = IIf(clMode = eclPubUpdate Or clMode = eclPubMassUpdate, "CORE", "")


     qualViewName = _
       genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , forNl, "CL", viewNameSuffix)

     If Len(valuesStringForCTE) > 3 Then
         valuesStringForCTE = Left(valuesStringForCTE, Len(valuesStringForCTE) - 3)

         printCteChangeLogStatements fileNo, acmEntityType, qualViewName, qualSourceTabName, qualSeqNameOid, isGenForming, forGen, False, _
             hasNoIdentity, clMode, cdUserId_in, isPsTagged, splitVar, stringsPerType(), valuesStringForCTE, caseUpdateStringForCTE
 
     End If


    If (nlAttrRefs.numDescriptors > 0) Then
      'reset for nl attributes
       valuesStringForCTE = ""
       caseUpdateStringForCTE = ""
       For i = 1 To 15
         stringsPerType(i) = "CASE bas.dbColumnName" & vbCrLf & "        WHEN 'DUMMYDUMMY' THEN NULL" & vbCrLf
       Next i
    End If

       ' generate change log records for changed NL-Text attributes
       thisAttributeIndex = lastNlAttrsProcessed + 1
       While thisAttributeIndex <= nlAttrRefs.numDescriptors
         If nlAttrRefs.descriptors(thisAttributeIndex).refType = eadrtAttribute Then
 ' ### IF IVK ###
             If g_attributes.descriptors(nlAttrRefs.descriptors(thisAttributeIndex).refIndex).isNl And (forGen Or hasNoIdentity) = g_attributes.descriptors(nlAttrRefs.descriptors(thisAttributeIndex).refIndex).isTimeVarying Then

 ' ### ELSE IVK ###
 '           If .isNl And forGen = .isTimeVarying Then
 ' ### ENDIF IVK ###
               Dim columnName As String
               columnName = genAttrName(g_attributes.descriptors(nlAttrRefs.descriptors(thisAttributeIndex).refIndex).attributeName, ddlType)

               genGenChangeLogRecordForCTEDdl "updates on NL-column '" & columnName & "'" & " (" & thisAttributeIndex & ")", _
                                        fileNo, stringsPerType, valuesStringForCTE, caseUpdateStringForCTE, False, qualTabNameExpr, ddlType, columnName, , _
                                        g_domains.descriptors(g_attributes.descriptors(nlAttrRefs.descriptors(thisAttributeIndex).refIndex).domainIndex).dataType, clMode, , , True

 ' ### IF IVK ###

               If g_attributes.descriptors(nlAttrRefs.descriptors(thisAttributeIndex).refIndex).isNationalizable Then
                 Dim natColumnName As String
                 natColumnName = genAttrName(g_attributes.descriptors(nlAttrRefs.descriptors(thisAttributeIndex).refIndex).attributeName, ddlType, , , , , True)

                 genGenChangeLogRecordForCTEDdl _
                    "updates on NL-column '" & natColumnName & "'" & " (" & thisAttributeIndex & ")", _
                   fileNo, stringsPerType, valuesStringForCTE, caseUpdateStringForCTE, False, qualTabNameExpr, ddlType, natColumnName, , _
                   g_domains.descriptors(g_attributes.descriptors(nlAttrRefs.descriptors(thisAttributeIndex).refIndex).domainIndex).dataType, clMode, , , True


                 natColumnName = genAttrName(g_attributes.descriptors(nlAttrRefs.descriptors(thisAttributeIndex).refIndex).attributeName & gc_anSuffixNatActivated, ddlType)

                 genGenChangeLogRecordForCTEDdl _
                    "updates on NL-column '" & natColumnName & "'" & " (" & thisAttributeIndex & ")", _
                   fileNo, stringsPerType, valuesStringForCTE, caseUpdateStringForCTE, False, qualTabNameExpr, ddlType, natColumnName, , _
                   g_domains.descriptors(g_attributes.descriptors(nlAttrRefs.descriptors(thisAttributeIndex).refIndex).domainIndex).dataType, clMode, , , False

               End If
 ' ### ENDIF IVK ###
             End If
         End If

         lastNlAttrsProcessed = thisAttributeIndex
         thisAttributeIndex = thisAttributeIndex + 1
       Wend
 ExitLoop:

       allNlAttrsProcessed = allNlAttrsProcessed Or (thisAttributeIndex > nlAttrRefs.numDescriptors)

 
     If (nlAttrRefs.numDescriptors > 0) Then
       qualViewName = _
             genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , True, "CL", viewNameSuffix)

        If Len(valuesStringForCTE) > 3 Then
             valuesStringForCTE = Left(valuesStringForCTE, Len(valuesStringForCTE) - 3)

             Print #fileNo,
             Print #fileNo, addTab(1); "DELETE FROM "; pc_tempTabNameChangeLogCte; ";"

             printCteChangeLogStatements fileNo, acmEntityType, qualViewName, qualSourceTabName, qualSeqNameOid, isGenForming, forGen, True, _
                 hasNoIdentity, clMode, cdUserId_in, isPsTagged, False, stringsPerType(), valuesStringForCTE, caseUpdateStringForCTE
        End If
     End If


 ' ### IF IVK ###
     End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(1); "END IF;"

 ' ### IF IVK ###
     If clMode = eclLrt Then
       If isPrimaryOrg Then
         genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "autoPriceSetProductive_in", _
                                  "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out"
       Else
         genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "autoPriceSetProductive_in", _
                                  "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out"
       End If
     Else
       genSpLogProcExit fileNo, qualProcName, ddlType, , IIf(clMode = eclLrt, "lrtOid_in", ""), "opId_in", "#commitTs_in", "rowCount_out"
     End If
 ' ### ELSE IVK ###
 '   If clMode = eclLrt Then
 '     genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "'cdUserId_in", "opId_in", "#commitTs_in", "rowCount_out"
 '   Else
 '     genSpLogProcExit fileNo, qualProcName, ddlType, , "opId_in", "#commitTs_in", "rowCount_out"
 '   End If
 ' ### ENDIF IVK ###

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     seqNo = seqNo + 1
   Wend
 End Sub
 
 
 
 Sub genGenChangeLogRecordDdl( _
   ByRef acmEntityIndex As Integer, ByRef acmEntityType As AcmAttrContainerType, _
   ByRef qualTabName As String, _
   ByRef qualLrtTabName As String, _
   ByRef qualSeqNameOid As String, _
   ByRef qualTabNameChangeLog As String, _
   ByRef opDescription As String, _
   ByRef lrtStateFilter As String, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef dbColumnName As String = "", _
   Optional ByRef dbColumnNameAlternative As String = "", _
   Optional dbColumnType As typeId = etNone, _
   Optional clMode As ChangeLogMode = eclLrt, _
   Optional columnCategory As AttrCategory = eacRegular, Optional indent As Integer = 2, _
   Optional ByRef valueOld As String = "", Optional ByRef valueNew As String = "", _
   Optional ByRef refOid As String = "", Optional ByRef logRecordOid As String = "", _
   Optional ByRef cdUserId As String = "", Optional ByRef opStatus As String = "", Optional addNewLine As Boolean = True, _
   Optional columnIsNullable As Boolean = False, _
   Optional ByRef divisionOid As String = "", Optional ByRef priceClassIndex As Integer _
 )
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
 ' ### IF IVK ###
   Dim isPsTagged As Boolean
 ' ### ENDIF IVK ###
   Dim hasOwnTable As Boolean
   Dim entityIdStr As String
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
 ' ### IF IVK ###
   Dim hasNoIdentity As Boolean
 ' ### ENDIF IVK ###
   Dim attrMapping() As AttributeMappingForCl
   Dim relLeftClassIdStr As String
   Dim relLeftFk As String
   Dim relRightClassIdStr As String
   Dim relRightFk As String
   Dim dbAcmEntityType As String
 ' ### IF IVK ###
   Dim navPathToDiv As NavPathFromClassToClass
   Dim condenseData As Boolean
 ' ### ENDIF IVK ###
   Dim isAggHead As Boolean

 ' ### IF IVK ###
   navPathToDiv.relRefIndex = -1
 ' ### ENDIF IVK ###
   isAggHead = False

   If acmEntityType = eactClass Then
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityTypeDescr = "ACM-Class"
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
       isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
       attrMapping = g_classes.descriptors(acmEntityIndex).clMapAttrsInclSubclasses
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex) And Not forGen And Not forNl
 ' ### IF IVK ###
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       navPathToDiv = g_classes.descriptors(acmEntityIndex).navPathToDiv
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityTypeDescr = "ACM-Relationship"
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       hasOwnTable = True
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       relRefs.numRefs = 0
       isGenForming = False
       dbAcmEntityType = gc_acmEntityTypeKeyRel
 ' ### IF IVK ###
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       hasNoIdentity = False
       condenseData = False
 ' ### ENDIF IVK ###

       Dim reuseRelIndex As Integer
       reuseRelIndex = IIf(reuseRelationships And g_relationships.descriptors(acmEntityIndex).reusedRelIndex > 0, g_relationships.descriptors(acmEntityIndex).reusedRelIndex, acmEntityIndex)
           relLeftClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).classIdStr
           relLeftFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).shortName)
           relRightClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).classIdStr
           relRightFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).shortName)
   Else
     Exit Sub
   End If

   Dim viewNameSuffix As String
 ' ### IF IVK ###
   viewNameSuffix = IIf(clMode = eclPubUpdate Or clMode = eclPubMassUpdate, "CORE", "")
 ' ### ELSE IVK ###
 ' viewNameSuffix = IIf(clMode = eclPubUpdate, "CORE", "")
 ' ### ENDIF IVK ###
   Dim qualViewName As String
   qualViewName = _
     genQualViewNameByEntityIndex( _
       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, "CL", viewNameSuffix _
     )

   If addNewLine Then
     Print #fileNo,
   End If
 
   genProcSectionHeader fileNo, opDescription & _
                                IIf(forGen Or forNl, " (", "") & _
                                IIf(forGen, "GEN", "") & _
                                IIf(forNl, IIf(forGen, "/", "") & "NL", "") & _
                                IIf(forGen Or forNl, ")", ""), indent + 0, True
   Print #fileNo, addTab(indent + 0); "INSERT INTO"
   Print #fileNo, addTab(indent + 1); qualTabNameChangeLog
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid,"
   Print #fileNo, addTab(indent + 1); "entityId,"
   Print #fileNo, addTab(indent + 1); "entityType,"
   Print #fileNo, addTab(indent + 1); "ahClassId,"
   Print #fileNo, addTab(indent + 1); "ahObjectId,"
   Print #fileNo, addTab(indent + 1); "gen,"
   Print #fileNo, addTab(indent + 1); "nl,"
   Print #fileNo, addTab(indent + 1); "dbTableName,"
   If dbColumnName <> "" Then
     Print #fileNo, addTab(indent + 1); "dbColumnName,"
   End If
   Print #fileNo, addTab(indent + 1); "objectId,"
   If acmEntityType = eactRelationship Then
     Print #fileNo, addTab(indent + 1); "refClassId1,"
     Print #fileNo, addTab(indent + 1); "refObjectId1,"
     Print #fileNo, addTab(indent + 1); "refClassId2,"
     Print #fileNo, addTab(indent + 1); "refObjectId2,"
   End If
 ' ### IF IVK ###
   Print #fileNo, addTab(indent + 1); "price,"
   Print #fileNo, addTab(indent + 1); "propertyOid,"
   Print #fileNo, addTab(indent + 1); "propertyType_Id,"
   Print #fileNo, addTab(indent + 1); "isNational,"
   Print #fileNo, addTab(indent + 1); "csBaumuster,"
   If acmEntityType = eactClass Then
     Print #fileNo, addTab(indent + 1); "sr0Context,"
     Print #fileNo, addTab(indent + 1); "sr0Code1,"
     Print #fileNo, addTab(indent + 1); "sr0Code2,"
     Print #fileNo, addTab(indent + 1); "sr0Code3,"
     Print #fileNo, addTab(indent + 1); "sr0Code4,"
     Print #fileNo, addTab(indent + 1); "sr0Code5,"
     Print #fileNo, addTab(indent + 1); "sr0Code6,"
     Print #fileNo, addTab(indent + 1); "sr0Code7,"
     Print #fileNo, addTab(indent + 1); "sr0Code8,"
     Print #fileNo, addTab(indent + 1); "sr0Code9,"
     Print #fileNo, addTab(indent + 1); "sr0Code10,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid1,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid2,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid3,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid4,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid5,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid6,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid7,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid8,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid9,"
     Print #fileNo, addTab(indent + 1); "sr0CodeOid10,"
     Print #fileNo, addTab(indent + 1); "sr1Context,"
     Print #fileNo, addTab(indent + 1); "sr1Code1,"
     Print #fileNo, addTab(indent + 1); "sr1Code2,"
     Print #fileNo, addTab(indent + 1); "sr1Code3,"
     Print #fileNo, addTab(indent + 1); "sr1Code4,"
     Print #fileNo, addTab(indent + 1); "sr1Code5,"
     Print #fileNo, addTab(indent + 1); "sr1Code6,"
     Print #fileNo, addTab(indent + 1); "sr1Code7,"
     Print #fileNo, addTab(indent + 1); "sr1Code8,"
     Print #fileNo, addTab(indent + 1); "sr1Code9,"
     Print #fileNo, addTab(indent + 1); "sr1Code10,"
     Print #fileNo, addTab(indent + 1); "nsr1Context,"
     Print #fileNo, addTab(indent + 1); "nsr1Code1,"
     Print #fileNo, addTab(indent + 1); "nsr1Code2,"
     Print #fileNo, addTab(indent + 1); "nsr1Code3,"
     Print #fileNo, addTab(indent + 1); "nsr1Code4,"
     Print #fileNo, addTab(indent + 1); "nsr1Code5,"
     Print #fileNo, addTab(indent + 1); "nsr1Code6,"
     Print #fileNo, addTab(indent + 1); "nsr1Code7,"
     Print #fileNo, addTab(indent + 1); "nsr1Code8,"
     Print #fileNo, addTab(indent + 1); "nsr1Code9,"
     Print #fileNo, addTab(indent + 1); "nsr1Code10,"
     Print #fileNo, addTab(indent + 1); "slotPlausibilityRuleType_ID,"
     Print #fileNo, addTab(indent + 1); "witexp_oid,"
     Print #fileNo, addTab(indent + 1); "winexp_oid,"
     Print #fileNo, addTab(indent + 1); "expexp_oid,"
   End If
   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(indent + 1); "validFrom,"
     Print #fileNo, addTab(indent + 1); "validTo,"
   End If
 ' ### IF IVK ###
   Print #fileNo, addTab(indent + 1); "baseCodeNumber,"
   Print #fileNo, addTab(indent + 1); "baseCodeType,"
   Print #fileNo, addTab(indent + 1); "codeKind_Id,"
   If cr132 Then
     Print #fileNo, addTab(indent + 1); "baseEndSlotOid,"
   End If

   Print #fileNo, addTab(indent + 1); "slotType_Id,"
   Print #fileNo, addTab(indent + 1); "aclacl_oid,"
   Print #fileNo, addTab(indent + 1); "dcldcl_oid,"
   Print #fileNo, addTab(indent + 1); "assignedPaintZoneKey,"
   Print #fileNo, addTab(indent + 1); "divisionOid,"
   Print #fileNo, addTab(indent + 1); "dpClassNumber,"
 ' ### ENDIF IVK ###

   If dbColumnName <> "" Then
     Print #fileNo, addTab(indent + 1); "valueType_Id,"
 ' ### IF IVK ###
     If isClAttrCat(columnCategory, (clMode = eclLrt) Or (clMode = eclPubUpdate) Or (clMode = eclPubMassUpdate)) Then
 ' ### ELSE IVK ###
 '   If isClAttrCat(columnCategory, (clMode = eclLrt) Or (clMode = eclPubUpdate)) Then
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
       If resolveCountryIdListInChangeLog And ((columnCategory And eacFkCountryIdList) <> 0) Then
         Print #fileNo, addTab(indent + 1); "oldValueString,"
         Print #fileNo, addTab(indent + 1); "newValueString,"
       ElseIf (columnCategory And eacFkOid) = 0 And attrTypeMapsToClColType(dbColumnType, clValueTypeString) Then
 ' ### ELSE IVK ###
 '     If (columnCategory And eacFkOid) = 0 And attrTypeMapsToClColType(dbColumnType, clValueTypeString) Then
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(indent + 1); "oldValueString,"
         Print #fileNo, addTab(indent + 1); "newValueString,"
       End If
       If attrTypeMapsToClColType(dbColumnType, clValueTypeTimeStamp) Then
         Print #fileNo, addTab(indent + 1); "oldValueTimestamp,"
         Print #fileNo, addTab(indent + 1); "newValueTimestamp,"
       End If
       If attrTypeMapsToClColType(dbColumnType, clValueTypeDate) Then
         Print #fileNo, addTab(indent + 1); "oldValueDate,"
         Print #fileNo, addTab(indent + 1); "newValueDate,"
       End If
       If attrTypeMapsToClColType(dbColumnType, clValueTypeInteger) Then
         Print #fileNo, addTab(indent + 1); "oldValueInteger,"
         Print #fileNo, addTab(indent + 1); "newValueInteger,"
       End If
 ' ### IF IVK ###
       If ((columnCategory And eacExpression) <> 0 And (columnCategory And eacNationalBool) = 0) Or attrTypeMapsToClColType(dbColumnType, clValueTypeBigInteger) Then
         Print #fileNo, addTab(indent + 1); "oldValueBigInt,"
         Print #fileNo, addTab(indent + 1); "newValueBigInt,"
       End If
 ' ### ENDIF IVK ###
       If attrTypeMapsToClColType(dbColumnType, clValueTypeDecimal) Then
         Print #fileNo, addTab(indent + 1); "oldValueDecimal,"
         Print #fileNo, addTab(indent + 1); "newValueDecimal,"
       End If
       If (columnCategory And eacRegular) Then
         If attrTypeMapsToClColType(dbColumnType, clValueTypeBoolean) Then
           Print #fileNo, addTab(indent + 1); "oldValueBoolean,"
           Print #fileNo, addTab(indent + 1); "newValueBoolean,"
         End If
       End If
     End If
   End If
   If clMode = eclLrt Then
     Print #fileNo, addTab(indent + 1); "lrtOid,"
   End If
   Print #fileNo, addTab(indent + 1); "isPerformedInMassupdate,"
   Print #fileNo, addTab(indent + 1); "operation_Id,"
   Print #fileNo, addTab(indent + 1); "opTimestamp,"
   Print #fileNo, addTab(indent + 1); "cdUserId,"
   Print #fileNo, addTab(indent + 1); "ps_Oid,"
   Print #fileNo, addTab(indent + 1); "versionId"
   Print #fileNo, addTab(indent + 0); ")"
   Print #fileNo, addTab(indent + 0); "SELECT"
   ' OID
   If logRecordOid <> "" Then
     Print #fileNo, addTab(indent + 1); logRecordOid; ","
   Else
     Print #fileNo, addTab(indent + 1); "NEXTVAL FOR "; qualSeqNameOid; ","
   End If
   Print #fileNo, addTab(indent + 1); "V.entityId,"
   Print #fileNo, addTab(indent + 1); "V.entityType,"
   Print #fileNo, addTab(indent + 1); "V.ahClassId,"
   Print #fileNo, addTab(indent + 1); "V.ahObjectId,"
   Print #fileNo, addTab(indent + 1); "V.gen,"
   Print #fileNo, addTab(indent + 1); "V.nl,"
   Print #fileNo, addTab(indent + 1); "V.dbTableName,"

   If dbColumnName <> "" Then
     'dbColumnName
     If dbColumnNameAlternative = "" Then
       Print #fileNo, addTab(indent + 1); "'"; UCase(dbColumnName); "',"
     Else
 ' ### IF IVK ###
       If columnCategory And eacNational Then
         Print #fileNo, addTab(indent + 1); "'"; genAttrName(dbColumnNameAlternative, ddlType, , , , , True); "',"
       ElseIf columnCategory And eacNationalBool Then
         Print #fileNo, addTab(indent + 1); "'"; UCase(dbColumnNameAlternative & gc_anSuffixNatActivated); "',"
       Else
         Print #fileNo, addTab(indent + 1); "'"; genAttrName(dbColumnNameAlternative, ddlType); "',"
       End If
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(indent + 1); "'"; genAttrName(dbColumnNameAlternative, ddlType); "',"
 ' ### ENDIF IVK ###
     End If
   End If
   Print #fileNo, addTab(indent + 1); "V.objectId,"

   If acmEntityType = eactRelationship Then
     Print #fileNo, addTab(indent + 1); "V.refClassId1,"
     Print #fileNo, addTab(indent + 1); "V.refObjectId1,"
     Print #fileNo, addTab(indent + 1); "V.refClassId2,"
     Print #fileNo, addTab(indent + 1); "V.refObjectId2,"
   End If

 ' ### IF IVK ###
   Print #fileNo, addTab(indent + 1); "V.price,"
   Print #fileNo, addTab(indent + 1); "V.propertyOid,"
   Print #fileNo, addTab(indent + 1); "V.propertyType_ID,"
   Print #fileNo, addTab(indent + 1); "COALESCE(V.isNational, "; gc_dbFalse; "),"
   Print #fileNo, addTab(indent + 1); "V.csBaumuster,"

   If acmEntityType = eactClass Then
     Print #fileNo, addTab(indent + 1); "V.sr0Context,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code1,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code2,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code3,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code4,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code5,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code6,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code7,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code8,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code9,"
     Print #fileNo, addTab(indent + 1); "V.sr0Code10,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid1,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid2,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid3,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid4,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid5,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid6,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid7,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid8,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid9,"
     Print #fileNo, addTab(indent + 1); "V.sr0CodeOid10,"
     Print #fileNo, addTab(indent + 1); "V.sr1Context,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code1,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code2,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code3,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code4,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code5,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code6,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code7,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code8,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code9,"
     Print #fileNo, addTab(indent + 1); "V.sr1Code10,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Context,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code1,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code2,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code3,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code4,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code5,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code6,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code7,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code8,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code9,"
     Print #fileNo, addTab(indent + 1); "V.nsr1Code10,"

     Print #fileNo, addTab(indent + 1); "V.slotPlausibilityRuleType_ID,"
     Print #fileNo, addTab(indent + 1); "V.witexp_oid,"
     Print #fileNo, addTab(indent + 1); "V.winexp_oid,"
     Print #fileNo, addTab(indent + 1); "V.expexp_oid,"
   End If

   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(indent + 1); "V."; g_anValidFrom; ","
     Print #fileNo, addTab(indent + 1); "V."; g_anValidTo; ","
   End If

 ' ### IF IVK ###
   Print #fileNo, addTab(indent + 1); "V.baseCodeNumber,"
   Print #fileNo, addTab(indent + 1); "V.baseCodeType,"
   Print #fileNo, addTab(indent + 1); "V.codeKind_id,"
   If cr132 Then
     Print #fileNo, addTab(indent + 1); "V.baseEndSlotOid,"
   End If
   Print #fileNo, addTab(indent + 1); "V.slotType_Id,"
   Print #fileNo, addTab(indent + 1); "V.aclacl_oid,"
   Print #fileNo, addTab(indent + 1); "V.dcldcl_oid,"
   Print #fileNo, addTab(indent + 1); "V.assignedPaintZoneKey,"
   If divisionOid <> "" Then
     Print #fileNo, addTab(indent + 1); "COALESCE(V.divisionOid,"; divisionOid; "),"
   Else
     Print #fileNo, addTab(indent + 1); "V.divisionOid,"
   End If

   Print #fileNo, addTab(indent + 1); "V.dpClassNumber,"
 ' ### ENDIF IVK ###

   If dbColumnName <> "" Then
 ' ### IF IVK ###
     If clMode = eclPubUpdate Or clMode = eclPubMassUpdate Then
 ' ### ELSE IVK ###
 '   If clMode = eclPubUpdate Then
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(indent + 1); CStr(getClColTypeByAttrType(dbColumnType)); ","
 
       If isClAttrCat(columnCategory, True) Then
         If (columnCategory And eacFkOid) = 0 And attrTypeMapsToClColType(dbColumnType, clValueTypeString) Then
           If dbColumnType = etBigInt Or _
              dbColumnType = etDecimal Or _
              dbColumnType = etDouble Or _
              dbColumnType = etFloat Or _
              dbColumnType = etInteger Or _
              dbColumnType = etSmallint Or _
              dbColumnType = etTime Or _
              dbColumnType = etTimestamp Or _
              dbColumnType = etDate Or _
              dbColumnType = etBoolean Then
             Print #fileNo, addTab(indent + 1); "RTRIM(CAST("; valueOld; " AS CHAR(30))),"
             Print #fileNo, addTab(indent + 1); "RTRIM(CAST("; valueNew; " AS CHAR(30))),"
           Else
             Print #fileNo, addTab(indent + 1); "CAST("; valueOld; " AS VARCHAR(4000)),"
             Print #fileNo, addTab(indent + 1); "CAST("; valueNew; " AS VARCHAR(4000)),"
           End If
         End If
         If attrTypeMapsToClColType(dbColumnType, clValueTypeTimeStamp) Then
           If dbColumnType = etTimestamp Then
             Print #fileNo, addTab(indent + 1); valueOld; ","
             Print #fileNo, addTab(indent + 1); valueNew; ","
           Else
             Print #fileNo, addTab(indent + 1); "CAST("; valueOld; " AS TIMESTAMP),"
             Print #fileNo, addTab(indent + 1); "CAST("; valueNew; " AS TIMESTAMP),"
           End If
         End If
         If attrTypeMapsToClColType(dbColumnType, clValueTypeDate) Then
           If dbColumnType = etDate Then
             Print #fileNo, addTab(indent + 1); valueOld; ","
             Print #fileNo, addTab(indent + 1); valueNew; ","
           Else
             Print #fileNo, addTab(indent + 1); "CAST("; valueOld; " AS DATE),"
             Print #fileNo, addTab(indent + 1); "CAST("; valueNew; " AS DATE),"
           End If
         End If
         If attrTypeMapsToClColType(dbColumnType, clValueTypeInteger) Then
           If dbColumnType = etInteger Or dbColumnType = etSmallint Then
             Print #fileNo, addTab(indent + 1); valueOld; ","
             Print #fileNo, addTab(indent + 1); valueNew; ","
           Else
             Print #fileNo, addTab(indent + 1); "CAST("; valueOld; " AS INTEGER),"
             Print #fileNo, addTab(indent + 1); "CAST("; valueNew; " AS INTEGER),"
           End If
         End If
 ' ### IF IVK ###
         If ((columnCategory And eacExpression) <> 0 And (columnCategory And eacNationalBool) = 0) Or attrTypeMapsToClColType(dbColumnType, clValueTypeBigInteger) Then
           If dbColumnType = etBigInt Then
             Print #fileNo, addTab(indent + 1); valueOld; ","
             Print #fileNo, addTab(indent + 1); valueNew; ","
           Else
             Print #fileNo, addTab(indent + 1); "CAST("; valueOld; " AS "; g_dbtOid; "),"
             Print #fileNo, addTab(indent + 1); "CAST("; valueNew; " AS "; g_dbtOid; "),"
           End If
         End If
 ' ### ENDIF IVK ###
         If attrTypeMapsToClColType(dbColumnType, clValueTypeDecimal) Then
           Print #fileNo, addTab(indent + 1); "CAST("; valueOld; " AS DECIMAL(31,10)),"
           Print #fileNo, addTab(indent + 1); "CAST("; valueNew; " AS DECIMAL(31,10)),"
         End If
         If (columnCategory And eacRegular) Then
           If attrTypeMapsToClColType(dbColumnType, clValueTypeBoolean) Then
             If dbColumnType = etBoolean Or dbColumnType = etSmallint Then
               Print #fileNo, addTab(indent + 1); valueOld; ","
               Print #fileNo, addTab(indent + 1); valueNew; ","
             Else
               Print #fileNo, addTab(indent + 1); "CAST("; valueOld; " AS "; g_dbtBoolean; "),"
               Print #fileNo, addTab(indent + 1); "CAST("; valueNew; " AS "; g_dbtBoolean; "),"
             End If
           End If
         End If
       End If
     Else
       If dbColumnNameAlternative = "" Then
         Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); "_t,"
       Else
         Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 2); "_t,"
       End If

       If isClAttrCat(columnCategory, clMode = eclLrt) Then
 ' ### IF IVK ###
         If resolveCountryIdListInChangeLog And ((columnCategory And eacFkCountryIdList) <> 0) Then
           Print #fileNo, addTab(indent + 1); "(SELECT IDLIST FROM "; g_qualTabNameCountryIdList; " WHERE OID = V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); "_o),"
           Print #fileNo, addTab(indent + 1); "(SELECT IDLIST FROM "; g_qualTabNameCountryIdList; " WHERE OID = "; _
                                              IIf(clMode = eclLrt And valueNew <> "", "CAST(RTRIM(CAST(" & valueNew & " AS CHAR(254))) AS VARCHAR(4000))", "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 2) & "_n"); _
                                              "),"
         ElseIf (columnCategory And eacFkOid) = 0 And attrTypeMapsToClColType(dbColumnType, clValueTypeString) Then
 ' ### ELSE IVK ###
 '       If (columnCategory And eacFkOid) = 0 And attrTypeMapsToClColType(dbColumnType, clValueTypeString) Then
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); "_o,"
           Print #fileNo, addTab(indent + 1); IIf(clMode = eclLrt And valueNew <> "", "CAST(RTRIM(CAST(" & valueNew & " AS CHAR(254))) AS VARCHAR(4000))", "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 2) & "_n"); ","
         End If
         If attrTypeMapsToClColType(dbColumnType, clValueTypeTimeStamp) Then
           Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 3); "_To,"
           Print #fileNo, addTab(indent + 1); IIf(clMode = eclLrt And valueNew <> "", "TIMESTAMP(" & valueNew & ")", "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 3) & "_Tn"); ","
         End If
         If attrTypeMapsToClColType(dbColumnType, clValueTypeDate) Then
           Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 3); "_Dto,"
           Print #fileNo, addTab(indent + 1); IIf(clMode = eclLrt And valueNew <> "", "DATE(" & valueNew & ")", "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 3) & "_Dtn"); ","
         End If
         If attrTypeMapsToClColType(dbColumnType, clValueTypeInteger) Then
           Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 3); "_Io,"
           Print #fileNo, addTab(indent + 1); IIf(clMode = eclLrt And valueNew <> "", "INTEGER(" & valueNew & ")", "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 3) & "_In"); ","
         End If
 ' ### IF IVK ###
         If ((columnCategory And eacExpression) <> 0 And (columnCategory And eacNationalBool) = 0) Or attrTypeMapsToClColType(dbColumnType, clValueTypeBigInteger) Then
           If dbColumnNameAlternative = "" Then
             Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 3); "_BIo,"
             Print #fileNo, addTab(indent + 1); IIf(clMode = eclLrt And valueNew <> "", g_dbtOid & "(" & valueNew & ")", "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 3) & "_BIn"); ","
           Else
             Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 3); "_BIo,"
             Print #fileNo, addTab(indent + 1); IIf(clMode = eclLrt And valueNew <> "", g_dbtOid & "(" & valueNew & ")", "V." & Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 3) & "_BIn"); ","
           End If
         End If
 ' ### ENDIF IVK ###
         If attrTypeMapsToClColType(dbColumnType, clValueTypeDecimal) Then
           Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 3); "_Do,"
           Print #fileNo, addTab(indent + 1); IIf(clMode = eclLrt And valueNew <> "", "DECIMAL(" & valueNew & ")", "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 3) & "_Dn"); ","
         End If
         If (columnCategory And eacRegular) Then
           If attrTypeMapsToClColType(dbColumnType, clValueTypeBoolean) Then
             Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 3); "_Bo,"
             Print #fileNo, addTab(indent + 1); IIf(clMode = eclLrt And valueNew <> "", g_dbtBoolean & "(" & valueNew & ")", "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 3) & "_Bn"); ","
           End If
         End If
       End If
     End If
   End If

   ' lrtOid
   If clMode = eclLrt Then
     Print #fileNo, addTab(indent + 1); "lrtOid_in,"
   End If

   Print #fileNo, addTab(indent + 1); "V.isPerformedInMassupdate,"

   If opStatus = "" Then
     Print #fileNo, addTab(indent + 1); "V.operation_Id,"
   Else
     Print #fileNo, addTab(indent + 1); opStatus; ","
   End If

   ' opTimestamp
   If clMode = eclPubUpdate Then
     Print #fileNo, addTab(indent + 1); "CURRENT TIMESTAMP,"
 ' ### IF IVK ###
   ElseIf clMode = eclPubMassUpdate Then
     Print #fileNo, addTab(indent + 1); "v_currentTimestamp,"
 ' ### ENDIF IVK ###
   Else
     Print #fileNo, addTab(indent + 1); "commitTs_in,"
   End If
   ' cdUserId
   If cdUserId <> "" Then
     Print #fileNo, addTab(indent + 1); cdUserId; ","
   ElseIf clMode = eclLrt Then
     Print #fileNo, addTab(indent + 1); "v_cdUserId,"
   Else
     Print #fileNo, addTab(indent + 1); "V.cdUserId,"
   End If
   Print #fileNo, addTab(indent + 1); "V.ps_Oid,"

   ' versionId"
   Print #fileNo, addTab(indent + 1); "1"
   Print #fileNo, addTab(indent + 0); "FROM"
   Print #fileNo, addTab(indent + 1); qualViewName; " V"

 ' ### IF IVK ###
   If clMode = eclSetProd Then
     Print #fileNo, addTab(indent + 0); "INNER JOIN"
     If condenseData Then
       Print #fileNo, addTab(indent + 1); tempTabNameOids; " E"
       Print #fileNo, addTab(indent + 0); "ON"
     Else
       Print #fileNo, addTab(indent + 1); gc_tempTabNameSpAffectedEntities; " E"
       Print #fileNo, addTab(indent + 0); "ON"
       Print #fileNo, addTab(indent + 1); "E.orParEntityType = '"; dbAcmEntityType; "'"
       Print #fileNo, addTab(indent + 2); "AND"
       Print #fileNo, addTab(indent + 1); "E.orParEntityId = '"; entityIdStr; "'"
       Print #fileNo, addTab(indent + 2); "AND"
       Print #fileNo, addTab(indent + 1); "E.opId = opId_in"
       Print #fileNo, addTab(indent + 2); "AND"
       Print #fileNo, addTab(indent + 1); "E.isNl = "; IIf(forNl, gc_dbTrue, gc_dbFalse)
       Print #fileNo, addTab(indent + 2); "AND"
       Print #fileNo, addTab(indent + 1); "E.isGen = "; IIf(forGen, gc_dbTrue, gc_dbFalse)
       Print #fileNo, addTab(indent + 2); "AND"
     End If
     Print #fileNo, addTab(indent + 1); "E.oid = V.objectId"
   ElseIf clMode = eclPubMassUpdate Then
     Print #fileNo, addTab(indent + 0); "INNER JOIN"
     Print #fileNo, addTab(indent + 1); qualTabName; " T"
     Print #fileNo, addTab(indent + 0); "ON"
     Print #fileNo, addTab(indent + 1); "V.objectId = T."; g_anOid
   End If
 ' ### ENDIF IVK ###

   Print #fileNo, addTab(indent + 0); "WHERE"

   If clMode = eclPubUpdate Then
     Print #fileNo, addTab(indent + 1); "V.objectId = "; refOid
 ' ### IF IVK ###
   ElseIf clMode = eclPubMassUpdate Then
     Print #fileNo, addTab(indent + 1); "T."; g_anInLrt; " IS NULL"
     Print #fileNo, addTab(indent + 2); "AND"
     Print #fileNo, addTab(indent + 1); "T."; g_anStatus; " < v_targetState"

     If isAggHead Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "T."; g_anIsNational; " = forNational_in"
     End If
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "T."; IIf(isAggHead, g_anCid, g_anAhCid); " = classId_in"
     If isPsTagged Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "T."; g_anPsOid; " = v_psOid"
     End If

     If Not isAggHead Then
       Dim qualTabNameGenericAspect As String
       qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)

       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "EXISTS ("
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "1"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); qualTabNameGenericAspect; " A"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "A."; g_anOid; " = T."; g_anAhOid
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "A."; g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "A."; g_anInLrt; " IS NULL"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "A."; g_anIsNational; " = forNational_in"
       Print #fileNo, addTab(2); ")"
      End If
   Else
     If clMode = eclSetProd And condenseData Then
       Print #fileNo, addTab(indent + 1); "(1=1) -- nothing to filter"
     Else
       Print #fileNo, addTab(indent + 1); "V.operation_Id IN ("; lrtStateFilter; ")"
     End If

     If clMode = eclLrt Then
       Print #fileNo, addTab(indent + 2); "AND"
       Print #fileNo, addTab(indent + 1); "V."; g_anLrtOid; " = lrtOid_in"
     ElseIf Not condenseData Then
       Print #fileNo, addTab(indent + 2); "AND"
       Print #fileNo, addTab(indent + 1); "V.status_Id = "; CStr(statusReadyToBeSetProductive)
     End If
 
     If clMode = eclSetProd And Not condenseData Then
       Print #fileNo, addTab(indent + 2); "AND"
       If isPsTagged Then
         Print #fileNo, addTab(indent + 1); "V.ps_Oid = psOid_in"
       Else
         Print #fileNo, addTab(indent + 1); "V.divisionOid = v_divisionOid"
       End If
     End If

     If dbColumnName <> "" Then
       Print #fileNo, addTab(indent + 2); "AND"
       Dim colNewSuffix As String
       Dim colOldSuffix As String
       Dim colOldSuffixAlternative As String
       Dim colNewSuffixAlternative As String
       If (columnCategory And eacFkOid) Then
         colOldSuffix = "_BIo"
         colNewSuffix = "_BIn"
       Else
         colOldSuffix = "_o"
         colNewSuffix = "_n"
         colOldSuffixAlternative = "_BIo"
         colNewSuffixAlternative = "_BIn"
       End If

       If columnIsNullable Then
         If (columnCategory And eacExpression) = 0 Then
           Print #fileNo, addTab(indent + 1); "NOT (V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colOldSuffix; " IS NULL AND V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colNewSuffix; " IS NULL)"

           Print #fileNo, addTab(indent + 2); "AND"
           Print #fileNo, addTab(indent + 1); "("
           Print #fileNo, addTab(indent + 2); "(V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colOldSuffix; " IS NULL)"
           Print #fileNo, addTab(indent + 3); "OR"
           Print #fileNo, addTab(indent + 2); "(V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colNewSuffix; " IS NULL)"
           Print #fileNo, addTab(indent + 3); "OR"
           Print #fileNo, addTab(indent + 2); "(V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colOldSuffix; " <> V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colNewSuffix; ")"
           Print #fileNo, addTab(indent + 1); ")"
         Else
           'Special case: column is expression
           Print #fileNo, addTab(indent + 1); "("
           Print #fileNo, addTab(indent + 2); "NOT (V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colOldSuffix; " IS NULL AND V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colNewSuffix; " IS NULL)"
           Print #fileNo, addTab(indent + 3); "AND"
           Print #fileNo, addTab(indent + 2); "("
           Print #fileNo, addTab(indent + 3); "(V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colOldSuffix; " IS NULL)"
           Print #fileNo, addTab(indent + 4); "OR"
           Print #fileNo, addTab(indent + 3); "(V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colNewSuffix; " IS NULL)"
           Print #fileNo, addTab(indent + 4); "OR"
           Print #fileNo, addTab(indent + 3); "(V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colOldSuffix; " <> V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colNewSuffix; ")"
           Print #fileNo, addTab(indent + 2); ")"

           Print #fileNo, addTab(indent + 3); "OR"

           Print #fileNo, addTab(indent + 2); "NOT (V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 2); colOldSuffixAlternative; " IS NULL AND V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 2); colNewSuffixAlternative; " IS NULL)"
           Print #fileNo, addTab(indent + 3); "AND"
           Print #fileNo, addTab(indent + 2); "("
           Print #fileNo, addTab(indent + 3); "(V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 2); colOldSuffixAlternative; " IS NULL)"
           Print #fileNo, addTab(indent + 4); "OR"
           Print #fileNo, addTab(indent + 3); "(V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 2); colNewSuffixAlternative; " IS NULL)"
           Print #fileNo, addTab(indent + 4); "OR"
           Print #fileNo, addTab(indent + 3); "(V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 2); colOldSuffixAlternative; " <> V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 2); colNewSuffixAlternative; ")"
           Print #fileNo, addTab(indent + 2); ")"

           Print #fileNo, addTab(indent + 1); ")"
         End If
       Else
         If (columnCategory And eacExpression) = 0 Then
           If dbColumnName = g_anStatus Then
             Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); "_Io"; " <> "; valueNew
           Else
             Print #fileNo, addTab(indent + 1); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colOldSuffix; " <> V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colNewSuffix
           End If
         Else
          'Special case: column is expression
           Print #fileNo, addTab(indent + 1); "("
           Print #fileNo, addTab(indent + 2); "V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colOldSuffix; " <> V."; Left(dbColumnName, gc_dbMaxAttributeNameLength - 2); colNewSuffix
           Print #fileNo, addTab(indent + 3); "OR"
           Print #fileNo, addTab(indent + 2); "V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 2); colOldSuffixAlternative; " <> V."; Left(dbColumnNameAlternative, gc_dbMaxAttributeNameLength - 2); colNewSuffixAlternative
           Print #fileNo, addTab(indent + 1); ")"
         End If
       End If
     End If
 ' ### ENDIF IVK ###
   End If

 ' ### IF IVK ###
   Print #fileNo, addTab(indent + 0); IIf((clMode = eclLrt Or clMode = eclSetProd), "WITH UR", ""); ";"
 
   If clMode <> eclPubUpdate And clMode <> eclPubMassUpdate Then
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(indent + 0); IIf(clMode = eclLrt, "WITH UR", ""); ";"
 '
 ' If clMode <> eclPubUpdate Then
 ' ### ENDIF IVK ###
     Print #fileNo,
     Print #fileNo, addTab(indent + 0); "-- count affected rows"
     Print #fileNo, addTab(indent + 0); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(indent + 0); "SET rowCount_out = rowCount_out + v_rowCount;"
   End If
 End Sub
 
 
 
 
 Sub genChangeLogViewDdlHeader( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef qualTargetTabName As String, _
   ByVal thisOrgIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional clMode As ChangeLogMode = eclLrt _
 )
   Dim sectionIndex As Integer
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim entityIdStr As String
   Dim isGenForming As Boolean
   Dim attrMapping() As AttributeMappingForCl
   Dim isLogChange As Boolean
   Dim useMqtToImplementLrt As Boolean
 ' ### IF IVK ###
   Dim hasNoIdentity As Boolean
   Dim condenseData As Boolean
 ' ### ENDIF IVK ###

   If acmEntityType = eactClass Then
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Class"
       isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
       isLogChange = g_classes.descriptors(acmEntityIndex).logLastChange
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       attrMapping = g_classes.descriptors(acmEntityIndex).clMapAttrsInclSubclasses
       useMqtToImplementLrt = g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
 ' ### IF IVK ###
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Relationship"
       isLogChange = g_relationships.descriptors(acmEntityIndex).logLastChange
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       isGenForming = False
       useMqtToImplementLrt = g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
 ' ### IF IVK ###
       hasNoIdentity = False
       condenseData = False
 ' ### ENDIF IVK ###
   Else
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    ChangeLog-View for entity
   ' ####################################################################################################################

   Dim transformation As AttributeListTransformation
   initAttributeTransformation transformation, 3
   setAttributeMapping transformation, 1, conCreateUser, ""
   setAttributeMapping transformation, 2, conUpdateUser, ""
   setAttributeMapping transformation, 3, conCreateTimestamp, ""

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, _
     fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, False, forGen, edomNone

   Dim viewNameSuffix As String
 ' ### IF IVK ###
   viewNameSuffix = IIf(clMode = eclPubUpdate Or clMode = eclPubMassUpdate, "CORE", "")
 ' ### ELSE IVK ###
 ' viewNameSuffix = IIf(clMode = eclPubUpdate, "CORE", "")
 ' ### ENDIF IVK ###
 
   Dim qualViewName As String
   qualViewName = _
     genQualViewNameByEntityIndex( _
       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , , "CL", viewNameSuffix _
     )
   printSectionHeader _
     "ChangeLog-View (" & genClModeDescription(clMode) & ") for table """ & qualTargetTabName & """ (ACM-" & _
     IIf(acmEntityType = eactClass, "Class", "Relationship") & """" & g_sections.descriptors(sectionIndex).sectionName & "." & entityName & """)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewName
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "entityId,"
   Print #fileNo, addTab(1); "entityType,"
   Print #fileNo, addTab(1); "ahClassId,"
   Print #fileNo, addTab(1); "ahObjectId,"
   Print #fileNo, addTab(1); "gen,"
   Print #fileNo, addTab(1); "nl,"
   Print #fileNo, addTab(1); "dbTableName,"
   Print #fileNo, addTab(1); "objectId,"
   If acmEntityType = eactRelationship Then
     Print #fileNo, addTab(1); "refClassId1,"
     Print #fileNo, addTab(1); "refObjectId1,"
     Print #fileNo, addTab(1); "refClassId2,"
     Print #fileNo, addTab(1); "refObjectId2,"
   End If
 ' ### IF IVK ###
   Print #fileNo, addTab(1); "price,"
   Print #fileNo, addTab(1); "propertyOid,"
   Print #fileNo, addTab(1); "propertyType_Id,"
   Print #fileNo, addTab(1); "isNational,"
   Print #fileNo, addTab(1); "csBaumuster,"
   Print #fileNo, addTab(1); "sr0Context,"
   Print #fileNo, addTab(1); "sr0Code1,"
   Print #fileNo, addTab(1); "sr0Code2,"
   Print #fileNo, addTab(1); "sr0Code3,"
   Print #fileNo, addTab(1); "sr0Code4,"
   Print #fileNo, addTab(1); "sr0Code5,"
   Print #fileNo, addTab(1); "sr0Code6,"
   Print #fileNo, addTab(1); "sr0Code7,"
   Print #fileNo, addTab(1); "sr0Code8,"
   Print #fileNo, addTab(1); "sr0Code9,"
   Print #fileNo, addTab(1); "sr0Code10,"

   Print #fileNo, addTab(1); "sr0CodeOid1,"
   Print #fileNo, addTab(1); "sr0CodeOid2,"
   Print #fileNo, addTab(1); "sr0CodeOid3,"
   Print #fileNo, addTab(1); "sr0CodeOid4,"
   Print #fileNo, addTab(1); "sr0CodeOid5,"
   Print #fileNo, addTab(1); "sr0CodeOid6,"
   Print #fileNo, addTab(1); "sr0CodeOid7,"
   Print #fileNo, addTab(1); "sr0CodeOid8,"
   Print #fileNo, addTab(1); "sr0CodeOid9,"
   Print #fileNo, addTab(1); "sr0CodeOid10,"

   Print #fileNo, addTab(1); "sr1Context,"
   Print #fileNo, addTab(1); "sr1Code1,"
   Print #fileNo, addTab(1); "sr1Code2,"
   Print #fileNo, addTab(1); "sr1Code3,"
   Print #fileNo, addTab(1); "sr1Code4,"
   Print #fileNo, addTab(1); "sr1Code5,"
   Print #fileNo, addTab(1); "sr1Code6,"
   Print #fileNo, addTab(1); "sr1Code7,"
   Print #fileNo, addTab(1); "sr1Code8,"
   Print #fileNo, addTab(1); "sr1Code9,"
   Print #fileNo, addTab(1); "sr1Code10,"

   Print #fileNo, addTab(1); "nsr1Context,"
   Print #fileNo, addTab(1); "nsr1Code1,"
   Print #fileNo, addTab(1); "nsr1Code2,"
   Print #fileNo, addTab(1); "nsr1Code3,"
   Print #fileNo, addTab(1); "nsr1Code4,"
   Print #fileNo, addTab(1); "nsr1Code5,"
   Print #fileNo, addTab(1); "nsr1Code6,"
   Print #fileNo, addTab(1); "nsr1Code7,"
   Print #fileNo, addTab(1); "nsr1Code8,"
   Print #fileNo, addTab(1); "nsr1Code9,"
   Print #fileNo, addTab(1); "nsr1Code10,"

   Print #fileNo, addTab(1); "slotPlausibilityRuleType_ID,"
   Print #fileNo, addTab(1); "witexp_oid,"
   Print #fileNo, addTab(1); "winexp_oid,"
   Print #fileNo, addTab(1); "expexp_oid,"
   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(1); "validFrom,"
     Print #fileNo, addTab(1); "validTo,"
   End If
 ' ### IF IVK ###
   Print #fileNo, addTab(1); "baseCodeNumber,"
   Print #fileNo, addTab(1); "baseCodeType,"
   Print #fileNo, addTab(1); "codeKind_id,"
   'Print #fileNo, addTab(1); "codeGroup???Key3,"
   'Print #fileNo, addTab(1); "codeGroup???Key2,"
   'Print #fileNo, addTab(1); "codeGroup???Key,"
   If cr132 Then
     Print #fileNo, addTab(1); "baseEndSlotOid,"
   End If
   Print #fileNo, addTab(1); "slotType_Id,"
   Print #fileNo, addTab(1); "aclacl_oid,"
   Print #fileNo, addTab(1); "dcldcl_oid,"
   Print #fileNo, addTab(1); "assignedPaintZoneKey,"
   Print #fileNo, addTab(1); "divisionOid,"
   Print #fileNo, addTab(1); "dpClassNumber,"
   Print #fileNo, addTab(1); "isPerformedInMassupdate,"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "lrtOid,"
 ' ### IF IVK ###
   If clMode <> eclPubUpdate And clMode <> eclPubMassUpdate And Not (clMode = eclSetProd And condenseData) Then
 ' ### ELSE IVK ###
 ' If clMode <> eclPubUpdate Then
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(1); "operation_Id,"
   End If
   Print #fileNo, addTab(1); "status_Id,"
   Print #fileNo, addTab(1); "operationTimestamp,"

   ' make sure that 'LastUpdateTimeStamp' is handled as attribute
   ' guess we do not need this any more
   Dim domainIndexModTs As Integer
   If isLogChange Then
     domainIndexModTs = g_domainIndexModTimestamp
     findColumnToUse tabColumns, g_anLastUpdateTimestamp, clnAcmEntity, acmEntityType, conLastUpdateTimestamp, eavtDomain, domainIndexModTs, False, eacRegular
   End If

   ' make sure that 'validFrom' and 'validTo' are handled as attribute
   Dim domainIndexValidTs As Integer
 ' ### IF IVK ###
   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     domainIndexValidTs = g_domainIndexValTimestamp
     findColumnToUse tabColumns, g_anValidFrom, clnAcmEntity, acmEntityType, conValidFrom, eavtDomain, domainIndexValidTs, False, eacRegular
     findColumnToUse tabColumns, g_anValidTo, clnAcmEntity, acmEntityType, conValidTo, eavtDomain, domainIndexValidTs, False, eacRegular
   End If

 ' ### IF IVK ###
   If clMode <> eclPubUpdate And clMode <> eclPubMassUpdate And Not condenseData Then
 ' ### ELSE IVK ###
 ' If clMode <> eclPubUpdate Then
 ' ### ENDIF IVK ###
     Dim i As Integer
     For i = 1 To tabColumns.numDescriptors
         If isClAttrCat(tabColumns.descriptors(i).columnCategory, clMode = eclLrt) Then
           Dim attrTypeId As typeId
           attrTypeId = g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).dataType
           Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 2); "_t,"

           Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 2); "_o,"
           Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 2); "_n,"

           If attrTypeMapsToClColType(attrTypeId, clValueTypeTimeStamp) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_To,"
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Tn,"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeDate) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Dto,"
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Dtn,"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeInteger) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Io,"
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_In,"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeBigInteger) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_BIo,"
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_BIn,"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeDecimal) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Do,"
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Dn,"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeBoolean) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Bo,"
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Bn,"
           End If
         End If
     Next i
   End If

 ' ### IF IVK ###
   If clMode = eclSetProd Then
     Print #fileNo, addTab(1); "cdUserId,"
   End If

   Print #fileNo, addTab(1); "ps_Oid"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
 End Sub
 
 
 Sub genChangeLogViewDdl( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef qualSourceTabName As String, _
   ByRef qualSourceGenTabName As String, _
   ByRef qualSourceNlTabName As String, _
   ByRef qualTargetTabName As String, _
   ByRef qualTargetNlTabName As String, _
   ByRef qualAggHeadTabName As String, _
   ByVal thisOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional clMode As ChangeLogMode = eclLrt _
 )
   Dim sectionIndex As Integer
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim hasOwnTable As Boolean
   Dim entityIdStr As String
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
   Dim attrMapping() As AttributeMappingForCl
   Dim relLeftClassIdStr As String
   Dim relLeftFk As String
   Dim relRightClassIdStr As String
   Dim relRightFk As String
   Dim hasNlAttributes As Boolean
   Dim isLogChange As Boolean
   Dim checkAggHeadForAttrs As Boolean
   Dim aggHeadClassIndex As Integer
   Dim isAggHead As Boolean
   Dim isAbstract As Boolean
   Dim attrMappingAh() As AttributeMappingForCl
   Dim relRefsAh As RelationshipDescriptorRefs
   Dim includeAggHeadInJoinPath As Boolean
   Dim includeGenInJoinPath As Boolean
   Dim includeAggHeadGenInJoinPath As Boolean
   Dim aggHeadReferredColumns As String
   Dim aggHeadGenReferredColumns As String
   Dim genReferredColumns As String
   Dim aggHeadSupportMqt As Boolean
   Dim useMqtToImplementLrtForEntity As Boolean
   Dim ignoreForChangelog As Boolean
 ' ### IF IVK ###
   Dim isPsTagged As Boolean
   Dim hasNoIdentity As Boolean
   Dim allowedCountriesRelIndex As Integer
   Dim disAllowedCountriesRelIndex As Integer
   Dim allowedCountriesListRelIndex As Integer
   Dim disAllowedCountriesListRelIndex As Integer
   Dim condenseData As Boolean
   Dim isNationalizable As Boolean
 ' ### ENDIF IVK ###

   includeAggHeadInJoinPath = False
   includeGenInJoinPath = False
   includeAggHeadGenInJoinPath = False
   isAggHead = False
   isAbstract = False

   If acmEntityType = eactClass Then
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Class"
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
       relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
       isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
       isLogChange = g_classes.descriptors(acmEntityIndex).logLastChange
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       hasNlAttributes = IIf(forGen, g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses)
       attrMapping = g_classes.descriptors(acmEntityIndex).clMapAttrsInclSubclasses
 ' ### IF IVK ###
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       isNationalizable = g_classes.descriptors(acmEntityIndex).isNationalizable
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       ignoreForChangelog = g_classes.descriptors(acmEntityIndex).ignoreForChangelog
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       allowedCountriesRelIndex = g_classes.descriptors(acmEntityIndex).allowedCountriesRelIndex
       disAllowedCountriesRelIndex = g_classes.descriptors(acmEntityIndex).disAllowedCountriesRelIndex
       allowedCountriesListRelIndex = g_classes.descriptors(acmEntityIndex).allowedCountriesListRelIndex
       disAllowedCountriesListRelIndex = g_classes.descriptors(acmEntityIndex).disAllowedCountriesListRelIndex
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt

       checkAggHeadForAttrs = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And ((g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex <> g_classes.descriptors(acmEntityIndex).aggHeadClassIndex) Or forGen)
       isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex)
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Relationship"
       isLogChange = g_relationships.descriptors(acmEntityIndex).logLastChange

       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       hasOwnTable = True
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       relRefs.numRefs = 0
       isGenForming = False
       hasNlAttributes = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0

       Dim reuseRelIndex As Integer
       reuseRelIndex = IIf(reuseRelationships And g_relationships.descriptors(acmEntityIndex).reusedRelIndex > 0, g_relationships.descriptors(acmEntityIndex).reusedRelIndex, acmEntityIndex)
           relLeftClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).classIdStr
           relLeftFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).shortName)
           relRightClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).classIdStr
           relRightFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).shortName)

       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       checkAggHeadForAttrs = (g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex > 0)
       ignoreForChangelog = g_relationships.descriptors(acmEntityIndex).ignoreForChangelog
 ' ### IF IVK ###
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       hasNoIdentity = False
       isNationalizable = False
       allowedCountriesRelIndex = -1
       disAllowedCountriesRelIndex = -1
       allowedCountriesListRelIndex = -1
       disAllowedCountriesListRelIndex = -1
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
       condenseData = False
 ' ### ENDIF IVK ###
   Else
     Exit Sub
   End If
 
   aggHeadSupportMqt = False
   If checkAggHeadForAttrs Then
       attrMappingAh = g_classes.descriptors(aggHeadClassIndex).clMapAttrsInclSubclasses
       relRefsAh = g_classes.descriptors(aggHeadClassIndex).relRefsRecursive
       aggHeadSupportMqt = useMqtToImplementLrt And g_classes.descriptors(aggHeadClassIndex).useMqtToImplementLrt
   End If

   If ignoreForChangelog Then
     Exit Sub
   End If

   Dim tupVarSrc As String
   Dim tupVarSrcGen As String
   Dim tupVarSrcPar As String
   Dim tupVarSrcParGen As String
   Dim tupVarTgt As String
   Dim tupVarAh As String

   If clMode = eclLrt Then
     tupVarSrc = "PRIV"
     tupVarSrcGen = "GEN"
     tupVarSrcPar = "PAR"
     tupVarSrcParGen = "PARGEN"
     tupVarTgt = "PUB"
     tupVarAh = "AH"
   ElseIf clMode = eclPubUpdate Then
     tupVarSrc = "OBJ"
     tupVarSrcGen = "OBJGEN"
     tupVarSrcPar = "PAR"
     tupVarSrcParGen = "PARGEN"
     tupVarTgt = " - no used -"
     tupVarAh = "AH"
 ' ### IF IVK ###
   ElseIf clMode = eclPubMassUpdate Then
     tupVarSrc = "OBJ"
     tupVarSrcGen = "OBJGEN"
     tupVarSrcPar = "PAR"
     tupVarSrcParGen = "PARGEN"
     tupVarTgt = " - no used -"
     tupVarAh = "AH"
 ' ### ENDIF IVK ###
   Else
     tupVarSrc = "SRC"
     tupVarSrcGen = "SRCGEN"
     tupVarSrcPar = "SRCPAR"
     tupVarSrcParGen = "SRCPARGEN"
     tupVarTgt = "TGT"
     tupVarAh = "AH"
   End If

   Dim parTabIsAhTab As Boolean
   parTabIsAhTab = (aggHeadClassIndex = acmEntityIndex) And (acmEntityType = eactClass)

   ' ####################################################################################################################
   ' #    ChangeLog-View for entity
   ' ####################################################################################################################

   'separate some code to avoid 'Procedure too large' - errors
   genChangeLogViewDdlHeader acmEntityIndex, acmEntityType, qualTargetTabName, thisOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, clMode

   Dim transformation As AttributeListTransformation
   initAttributeTransformation transformation, 3
   setAttributeMapping transformation, 1, conCreateUser, ""
   setAttributeMapping transformation, 2, conUpdateUser, ""
   setAttributeMapping transformation, 3, conCreateTimestamp, ""

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, _
     fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, False, forGen, edomNone

   Dim viewNameSuffix As String
 ' ### IF IVK ###
   viewNameSuffix = IIf(clMode = eclPubUpdate Or clMode = eclPubMassUpdate, "CORE", "")
 ' ### ELSE IVK ###
 ' viewNameSuffix = IIf(clMode = eclPubUpdate , "CORE", "")
 ' ### ENDIF IVK ###
   Dim qualViewName As String
   qualViewName = _
     genQualViewNameByEntityIndex( _
       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , , "CL", viewNameSuffix _
     )

   ' make sure that 'LastUpdateTimeStamp' is handled as attribute
   ' guess we do not need this any more
   Dim domainIndexModTs As Integer
   If isLogChange Then
     domainIndexModTs = g_domainIndexModTimestamp
     findColumnToUse tabColumns, g_anLastUpdateTimestamp, clnAcmEntity, acmEntityType, conLastUpdateTimestamp, eavtDomain, domainIndexModTs, False, eacRegular
   End If

   ' make sure that 'validFrom' and 'validTo' are handled as attribute
   Dim domainIndexValidTs As Integer
 ' ### IF IVK ###
   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     domainIndexValidTs = g_domainIndexValTimestamp
     findColumnToUse tabColumns, g_anValidFrom, clnAcmEntity, acmEntityType, conValidFrom, eavtDomain, domainIndexValidTs, False, eacRegular
     findColumnToUse tabColumns, g_anValidTo, clnAcmEntity, acmEntityType, conValidTo, eavtDomain, domainIndexValidTs, False, eacRegular
   End If

   ' entityId / entityType
   If acmEntityType = eactClass Then
     Print #fileNo, addTab(2); "-- entityId"
     If hasOwnTable Then
       Print #fileNo, addTab(2); "'"; entityIdStr; "',"
     Else
       If forGen Then
         Print #fileNo, addTab(2); tupVarSrcPar; "."; g_anCid; ","
       Else
         Print #fileNo, addTab(2); tupVarSrc; "."; g_anCid; ","
       End If
     End If
     Print #fileNo, addTab(2); "-- entityType"
     Print #fileNo, addTab(2); "'"; gc_acmEntityTypeKeyClass; "',"
   Else
     Print #fileNo, addTab(2); "-- entityId"
     Print #fileNo, addTab(2); "'"; entityIdStr; "',"
     Print #fileNo, addTab(2); "-- entityType"
     Print #fileNo, addTab(2); "'"; gc_acmEntityTypeKeyRel; "',"
   End If
   ' ahClassId
   Print #fileNo, addTab(2); "-- ahClassId"
   If aggHeadClassIndex > 0 Then
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhCid; ","
   Else
     Print #fileNo, addTab(2); "'"; entityIdStr; "',"
   End If
   ' ahObjectId
   Print #fileNo, addTab(2); "-- ahObjectId"
   If aggHeadClassIndex > 0 Then
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhOid; ","
   Else
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anOid; ","
   End If
   ' gen
   Print #fileNo, addTab(2); "-- gen"
   Print #fileNo, addTab(2); IIf(forGen, "1,", "0,")
   ' nl
   Print #fileNo, addTab(2); "-- nl"
   Print #fileNo, addTab(2); "0,"
   'dbTableName
   Print #fileNo, addTab(2); "-- dbTableName"
   Print #fileNo, addTab(2); "'"; getObjBaseName(qualTargetTabName); "',"
   ' objectId
   Print #fileNo, addTab(2); "-- objectId"
   Print #fileNo, addTab(2); tupVarSrc; "."; g_anOid; ","

   If acmEntityType = eactRelationship Then
     ' refClassId1
     Print #fileNo, addTab(2); "-- refClassId1"
     Print #fileNo, addTab(2); "'"; relLeftClassIdStr; "',"
     ' refObjectId1
     Print #fileNo, addTab(2); "-- refObjectId1"
     Print #fileNo, addTab(2); tupVarSrc; "."; relLeftFk; ","
     ' refClassId2
     Print #fileNo, addTab(2); "-- refClassId2"
     Print #fileNo, addTab(2); "'"; relRightClassIdStr; "',"
     ' refObjectId2
     Print #fileNo, addTab(2); "-- refObjectId2"
     Print #fileNo, addTab(2); tupVarSrc; "."; relRightFk; ","
   End If

 ' ### IF IVK ###
   Dim priceTargetClassIndex As Integer
   Dim priceTargetClassIndexAh As Integer
   Dim priceFkAttrName As String
   Dim priceQualObjName As String
   Dim includeAggHeadInJoinPathForPrice As Boolean
   ' price
 
   Dim foundPrice As Boolean
   Dim foundPriceInAggHead As Boolean
 
   foundPrice = _
   genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "price", "PRI", priceTargetClassIndex, _
     priceTargetClassIndexAh, "CAST(NULL AS DECIMAL(15,3))", priceQualObjName, priceFkAttrName, _
     includeAggHeadInJoinPathForPrice, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, _
     IIf(isAggHead And forGen, tupVarSrcPar, tupVarAh), tupVarSrcGen, , foundPriceInAggHead, , , , aggHeadReferredColumns)
 
   If includeAggHeadInJoinPathForPrice And Not isAggHead Then
     includeAggHeadInJoinPath = includeAggHeadInJoinPath Or includeAggHeadInJoinPathForPrice
   End If
 
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
   Dim propertyTargetClassIndex As Integer
   Dim propertyTargetClassIndexAh As Integer
   Dim propertyFkAttrName As String
   Dim propertyQualObjName As String
   Dim includeAggHeadInJoinPathForPropertyOid As Boolean
   ' propertyOid

   Dim foundPropertyOid As Boolean
   Dim foundPropertyOidInAggHead As Boolean

   foundPropertyOid = _
   genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyOid", "PRP", propertyTargetClassIndex, _
     propertyTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", propertyQualObjName, propertyFkAttrName, _
     includeAggHeadInJoinPathForPropertyOid, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, _
     IIf(isAggHead And forGen, tupVarSrcPar, tupVarAh), tupVarSrcGen, , foundPropertyOidInAggHead, , , , aggHeadReferredColumns)

   If includeAggHeadInJoinPathForPropertyOid And Not isAggHead Then
     includeAggHeadInJoinPath = includeAggHeadInJoinPath Or includeAggHeadInJoinPathForPropertyOid
   End If

   Dim propertyTypeTargetClassIndex As Integer
   Dim propertyTypeTargetClassIndexAh As Integer
   Dim propertyTypeFkAttrName As String
   Dim propertyTypeQualObjName As String
   ' propertyType_Id
   If foundPropertyOid Then
     Dim qualObjNamePropertyGen As String
 
       If clMode = eclLrt Then
         qualObjNamePropertyGen = genQualViewNameByClassIndex(g_classes.descriptors(IIf(propertyTargetClassIndex > 0, propertyTargetClassIndex, IIf(propertyTargetClassIndexAh > 0, propertyTargetClassIndexAh, IIf(foundPropertyOidInAggHead, aggHeadClassIndex, acmEntityIndex)))).classIndex, ddlType, thisOrgIndex, srcPoolIndex, True, True, g_classes.descriptors(IIf(propertyTargetClassIndex > 0, propertyTargetClassIndex, IIf(propertyTargetClassIndexAh > 0, propertyTargetClassIndexAh, IIf(foundPropertyOidInAggHead, aggHeadClassIndex, acmEntityIndex)))).useMqtToImplementLrt)
       Else
         qualObjNamePropertyGen = genQualTabNameByClassIndex(g_classes.descriptors(IIf(propertyTargetClassIndex > 0, propertyTargetClassIndex, IIf(propertyTargetClassIndexAh > 0, propertyTargetClassIndexAh, IIf(foundPropertyOidInAggHead, aggHeadClassIndex, acmEntityIndex)))).classIndex, ddlType, thisOrgIndex, srcPoolIndex, True)
       End If

     ' Fixme: get rid of hard-coding here!!
     If propertyTargetClassIndex > 0 Or propertyTargetClassIndexAh > 0 Then
       Print #fileNo, addTab(2); "-- propertyType_ID"
       Print #fileNo, addTab(2); "(SELECT PRPG.TYPE_ID FROM "; qualObjNamePropertyGen; " PRPG WHERE PRPG.PRP_OID = PRP."; g_anOid; " ORDER BY "; g_anValidFrom; " DESC FETCH FIRST 1 ROW ONLY),"
     ElseIf foundPropertyOidInAggHead Then
       If isAggHead Then
         genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyType_ID", "PT", propertyTypeTargetClassIndex, _
           propertyTypeTargetClassIndexAh, "CAST(NULL AS " & g_dbtEnumId & ")", propertyTypeQualObjName, propertyTypeFkAttrName, _
           True, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, _
           IIf(forGen, tupVarSrcPar, tupVarAh), tupVarSrcGen, , , , , , aggHeadReferredColumns
       Else
         Print #fileNo, addTab(2); "-- propertyType_ID"
         Print #fileNo, addTab(2); "(SELECT PRPG.TYPE_ID FROM "; qualObjNamePropertyGen; " PRPG WHERE PRPG.PRP_OID = "; tupVarAh; "."; g_anOid; " ORDER BY "; g_anValidFrom; " DESC FETCH FIRST 1 ROW ONLY),"
       End If
     Else
       Print #fileNo, addTab(2); "-- propertyType_ID"
       Print #fileNo, addTab(2); "(SELECT PRPG.TYPE_ID FROM "; qualObjNamePropertyGen; " PRPG WHERE PRPG.PRP_OID = "; tupVarSrc; "."; g_anOid; " ORDER BY "; g_anValidFrom; " DESC FETCH FIRST 1 ROW ONLY),"
     End If
   Else
     genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyType_ID", "PT", propertyTypeTargetClassIndex, _
       propertyTypeTargetClassIndexAh, "CAST(NULL AS " & g_dbtEnumId & ")", propertyTypeQualObjName, propertyTypeFkAttrName, _
       includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, tupVarAh, _
       tupVarSrcGen, , , , , , aggHeadReferredColumns
   End If

   ' isNational
   If isNationalizable Then
     Print #fileNo, addTab(2); "-- isNational"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anIsNational; ","
   Else
     genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "isNational", gc_dbFalse, forGen, _
                       includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtBoolean, 2, True, tupVarSrc, _
                       tupVarAh, , , aggHeadReferredColumns
   End If

   ' csBaumuster
   Dim csBaumusterTargetClassIndex As Integer
   Dim csBaumusterTargetClassIndexAh As Integer
   Dim csBaumusterFkAttrName As String
   Dim csBaumusterQualObjName As String
   Dim csBaumusterFoundInAggHead As Boolean

   Print #fileNo, addTab(2); "-- csBaumuster"

   If genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "csBaumuster", "CSB", csBaumusterTargetClassIndex, _
     csBaumusterTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", csBaumusterQualObjName, csBaumusterFkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, tupVarAh, _
     tupVarSrcGen, , csBaumusterFoundInAggHead, , , , aggHeadReferredColumns, , , True, True) Then

     Print #fileNo, addTab(2); "COALESCE(";

     ' FixMe: for csBaumuster we only navigate along 'direct' relationships (i.e. not related to aggregate head)
     ' A more generic criterion would be to check with which relationship this class effectively can be related to an Aggregate Head having a 'csBaumuster'-relationship
     ' E.g.: A DecisionTable may never have an NSR1Validity as Aggregate Head which then is related to some SR0Validity carying 'baumuster'
     genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "csBaumuster", "CSB", forGen, _
                       includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "CAST(NULL AS VARCHAR(1))", 2, True, tupVarSrc, _
                       tupVarAh, , , aggHeadReferredColumns, , True
     If csBaumusterTargetClassIndex > 0 And csBaumusterTargetClassIndexAh <= 0 Then
         genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "csBaumuster", "CSB", csBaumusterTargetClassIndex, _
           csBaumusterTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", csBaumusterQualObjName, csBaumusterFkAttrName, _
           includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, False, True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
           , aggHeadReferredColumns, , , , True
     End If
     csBaumusterTargetClassIndexAh = 0
     Print #fileNo, "CAST(NULL AS VARCHAR(8))),"
   Else
     Print #fileNo, addTab(2); "CAST(NULL AS VARCHAR(8)),"
   End If

   ' sr0Context
   genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "sr0Context", "NULL", forGen, _
                       includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "VARCHAR(159)", 2, True, tupVarSrc, tupVarAh, , , aggHeadReferredColumns

   Dim s0_01TargetClassIndex As Integer
   Dim s0_01TargetClassIndexAh As Integer
   Dim s0_01FkAttrName As String
   Dim s0_01QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code01", "S01", s0_01TargetClassIndex, _
     s0_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_01QualObjName, s0_01FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s0_02TargetClassIndex As Integer
   Dim s0_02TargetClassIndexAh As Integer
   Dim s0_02FkAttrName As String
   Dim s0_02QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code02", "S02", s0_02TargetClassIndex, _
     s0_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_02QualObjName, s0_02FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s0_03TargetClassIndex As Integer
   Dim s0_03TargetClassIndexAh As Integer
   Dim s0_03FkAttrName As String
   Dim s0_03QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code03", "S03", s0_03TargetClassIndex, _
     s0_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_03QualObjName, s0_03FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s0_04TargetClassIndex As Integer
   Dim s0_04TargetClassIndexAh As Integer
   Dim s0_04FkAttrName As String
   Dim s0_04QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code04", "S04", s0_04TargetClassIndex, _
     s0_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_04QualObjName, s0_04FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s0_05TargetClassIndex As Integer
   Dim s0_05TargetClassIndexAh As Integer
   Dim s0_05FkAttrName As String
   Dim s0_05QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code05", "S05", s0_05TargetClassIndex, _
     s0_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_05QualObjName, s0_05FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s0_06TargetClassIndex As Integer
   Dim s0_06TargetClassIndexAh As Integer
   Dim s0_06FkAttrName As String
   Dim s0_06QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code06", "S06", s0_06TargetClassIndex, _
     s0_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_06QualObjName, s0_06FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s0_07TargetClassIndex As Integer
   Dim s0_07TargetClassIndexAh As Integer
   Dim s0_07FkAttrName As String
   Dim s0_07QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code07", "S07", s0_07TargetClassIndex, _
     s0_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_07QualObjName, s0_07FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s0_08TargetClassIndex As Integer
   Dim s0_08TargetClassIndexAh As Integer
   Dim s0_08FkAttrName As String
   Dim s0_08QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code08", "S08", s0_08TargetClassIndex, _
     s0_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_08QualObjName, s0_08FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s0_09TargetClassIndex As Integer
   Dim s0_09TargetClassIndexAh As Integer
   Dim s0_09FkAttrName As String
   Dim s0_09QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code09", "S09", s0_09TargetClassIndex, _
     s0_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_09QualObjName, s0_09FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s0_10TargetClassIndex As Integer
   Dim s0_10TargetClassIndexAh As Integer
   Dim s0_10FkAttrName As String
   Dim s0_10QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code10", "S10", s0_10TargetClassIndex, _
     s0_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_10QualObjName, s0_10FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   ' sr0CodeOids
   Print #fileNo, addTab(2); IIf(s0_01TargetClassIndex > 0 Or s0_01TargetClassIndexAh > 0, "S01." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
   Print #fileNo, addTab(2); IIf(s0_02TargetClassIndex > 0 Or s0_02TargetClassIndexAh > 0, "S02." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
   Print #fileNo, addTab(2); IIf(s0_03TargetClassIndex > 0 Or s0_03TargetClassIndexAh > 0, "S03." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
   Print #fileNo, addTab(2); IIf(s0_04TargetClassIndex > 0 Or s0_04TargetClassIndexAh > 0, "S04." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
   Print #fileNo, addTab(2); IIf(s0_05TargetClassIndex > 0 Or s0_05TargetClassIndexAh > 0, "S05." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
   Print #fileNo, addTab(2); IIf(s0_06TargetClassIndex > 0 Or s0_06TargetClassIndexAh > 0, "S06." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
   Print #fileNo, addTab(2); IIf(s0_07TargetClassIndex > 0 Or s0_07TargetClassIndexAh > 0, "S07." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
   Print #fileNo, addTab(2); IIf(s0_08TargetClassIndex > 0 Or s0_08TargetClassIndexAh > 0, "S08." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
   Print #fileNo, addTab(2); IIf(s0_09TargetClassIndex > 0 Or s0_09TargetClassIndexAh > 0, "S09." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
   Print #fileNo, addTab(2); IIf(s0_10TargetClassIndex > 0 Or s0_10TargetClassIndexAh > 0, "S10." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","

   ' sr1Context
   genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "sr1Context", "NULL", forGen, _
                     includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "VARCHAR(159)", 2, True, tupVarSrc, tupVarAh, , , , aggHeadReferredColumns

   Dim s1_01TargetClassIndex As Integer
   Dim s1_01TargetClassIndexAh As Integer
   Dim s1_01FkAttrName As String
   Dim s1_01QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code01", "S101", s1_01TargetClassIndex, _
     s1_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_01QualObjName, s1_01FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s1_02TargetClassIndex As Integer
   Dim s1_02TargetClassIndexAh As Integer
   Dim s1_02FkAttrName As String
   Dim s1_02QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code02", "S102", s1_02TargetClassIndex, _
     s1_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_02QualObjName, s1_02FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s1_03TargetClassIndex As Integer
   Dim s1_03TargetClassIndexAh As Integer
   Dim s1_03FkAttrName As String
   Dim s1_03QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code03", "S103", s1_03TargetClassIndex, _
     s1_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_03QualObjName, s1_03FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s1_04TargetClassIndex As Integer
   Dim s1_04TargetClassIndexAh As Integer
   Dim s1_04FkAttrName As String
   Dim s1_04QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code04", "S104", s1_04TargetClassIndex, _
     s1_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_04QualObjName, s1_04FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s1_05TargetClassIndex As Integer
   Dim s1_05TargetClassIndexAh As Integer
   Dim s1_05FkAttrName As String
   Dim s1_05QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code05", "S105", s1_05TargetClassIndex, _
     s1_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_05QualObjName, s1_05FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s1_06TargetClassIndex As Integer
   Dim s1_06TargetClassIndexAh As Integer
   Dim s1_06FkAttrName As String
   Dim s1_06QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code06", "S106", s1_06TargetClassIndex, _
     s1_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_06QualObjName, s1_06FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s1_07TargetClassIndex As Integer
   Dim s1_07TargetClassIndexAh As Integer
   Dim s1_07FkAttrName As String
   Dim s1_07QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code07", "S107", s1_07TargetClassIndex, _
     s1_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_07QualObjName, s1_07FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s1_08TargetClassIndex As Integer
   Dim s1_08TargetClassIndexAh As Integer
   Dim s1_08FkAttrName As String
   Dim s1_08QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code08", "S108", s1_08TargetClassIndex, _
     s1_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_08QualObjName, s1_08FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s1_09TargetClassIndex As Integer
   Dim s1_09TargetClassIndexAh As Integer
   Dim s1_09FkAttrName As String
   Dim s1_09QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code09", "S109", s1_09TargetClassIndex, _
     s1_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_09QualObjName, s1_09FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim s1_10TargetClassIndex As Integer
   Dim s1_10TargetClassIndexAh As Integer
   Dim s1_10FkAttrName As String
   Dim s1_10QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code10", "S110", s1_10TargetClassIndex, _
     s1_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_10QualObjName, s1_10FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   ' nsr1Context
   genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "nsr1Context", "NULL", forGen, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "VARCHAR(159)", 2, True, tupVarSrc, tupVarAh, , , , aggHeadReferredColumns

   Dim ns1_01TargetClassIndex As Integer
   Dim ns1_01TargetClassIndexAh As Integer
   Dim ns1_01FkAttrName As String
   Dim ns1_01QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code01", "N101", ns1_01TargetClassIndex, _
     ns1_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_01QualObjName, ns1_01FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim ns1_02TargetClassIndex As Integer
   Dim ns1_02TargetClassIndexAh As Integer
   Dim ns1_02FkAttrName As String
   Dim ns1_02QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code02", "N102", ns1_02TargetClassIndex, _
     ns1_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_02QualObjName, ns1_02FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim ns1_03TargetClassIndex As Integer
   Dim ns1_03TargetClassIndexAh As Integer
   Dim ns1_03FkAttrName As String
   Dim ns1_03QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code03", "N103", ns1_03TargetClassIndex, _
     ns1_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_03QualObjName, ns1_03FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim ns1_04TargetClassIndex As Integer
   Dim ns1_04TargetClassIndexAh As Integer
   Dim ns1_04FkAttrName As String
   Dim ns1_04QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code04", "N104", ns1_04TargetClassIndex, _
     ns1_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_04QualObjName, ns1_04FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim ns1_05TargetClassIndex As Integer
   Dim ns1_05TargetClassIndexAh As Integer
   Dim ns1_05FkAttrName As String
   Dim ns1_05QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code05", "N105", ns1_05TargetClassIndex, _
     ns1_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_05QualObjName, ns1_05FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim ns1_06TargetClassIndex As Integer
   Dim ns1_06TargetClassIndexAh As Integer
   Dim ns1_06FkAttrName As String
   Dim ns1_06QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code06", "N106", ns1_06TargetClassIndex, _
     ns1_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_06QualObjName, ns1_06FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim ns1_07TargetClassIndex As Integer
   Dim ns1_07TargetClassIndexAh As Integer
   Dim ns1_07FkAttrName As String
   Dim ns1_07QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code07", "N107", ns1_07TargetClassIndex, _
     ns1_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_07QualObjName, ns1_07FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim ns1_08TargetClassIndex As Integer
   Dim ns1_08TargetClassIndexAh As Integer
   Dim ns1_08FkAttrName As String
   Dim ns1_08QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code08", "N108", ns1_08TargetClassIndex, _
     ns1_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_08QualObjName, ns1_08FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim ns1_09TargetClassIndex As Integer
   Dim ns1_09TargetClassIndexAh As Integer
   Dim ns1_09FkAttrName As String
   Dim ns1_09QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code09", "N109", ns1_09TargetClassIndex, _
     ns1_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_09QualObjName, ns1_09FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   Dim ns1_10TargetClassIndex As Integer
   Dim ns1_10TargetClassIndexAh As Integer
   Dim ns1_10FkAttrName As String
   Dim ns1_10QualObjName As String

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code10", "N110", ns1_10TargetClassIndex, _
     ns1_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_10QualObjName, ns1_10FkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , _
     , aggHeadReferredColumns
 
   ' slotPlausibilityRuleType_ID
   genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "slotPlausibilityRuleType_ID", "NULL", forGen, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtEnumId, 2, True, tupVarSrc, tupVarAh, , , , aggHeadReferredColumns
 
   ' with
   genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "with", "NULL", forGen, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtOid, 2, True, tupVarSrc, tupVarAh, , , , aggHeadReferredColumns
   ' withNot
   genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "withNot", "NULL", forGen, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtOid, 2, True, tupVarSrc, tupVarAh, , , , aggHeadReferredColumns
   ' expression
   genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "expression", "NULL", forGen, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtOid, 2, True, tupVarSrc, tupVarAh, , , , aggHeadReferredColumns

 ' ### ENDIF IVK ###
   ' validFrom / validTo
 ' ### IF IVK ###
   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(2); "-- validFrom"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anValidFrom; ","
     Print #fileNo, addTab(2); "-- validTo"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anValidTo; ","
   End If

 ' ### IF IVK ###
   Dim bcTargetClassIndex As Integer
   Dim bcTargetClassIndexAh As Integer
   Dim bcFkAttrName As String
   Dim bcQualObjName As String
   Dim bcReferredColumnList As String
 
   ' baseCodeNumber, baseCodeType and codeKind
   Dim baseCodeNumberFoundInAh As Boolean
   If genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "baseCodeNumber", "BC", bcTargetClassIndex, _
        bcTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", bcQualObjName, bcFkAttrName, _
        includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, _
        tupVarSrc, tupVarAh, tupVarSrcGen, , baseCodeNumberFoundInAh, , , bcReferredColumnList, aggHeadReferredColumns) Then

     Dim tupVarRef As String
     If bcTargetClassIndex > 0 Or bcTargetClassIndexAh > 0 Then
       tupVarRef = "BC"
       addStrListElem bcReferredColumnList, g_anCodeNumber
       addStrListElem bcReferredColumnList, "CTLTLV_OID"
       addStrListElem bcReferredColumnList, "CTYTYP_OID"
       addStrListElem bcReferredColumnList, "CDIDIV_OID"
       addStrListElem bcReferredColumnList, g_anIsNational
     Else
       If baseCodeNumberFoundInAh Then
         addStrListElem aggHeadReferredColumns, g_anCodeNumber
         addStrListElem aggHeadReferredColumns, "CTLTLV_OID"
         addStrListElem aggHeadReferredColumns, "CTYTYP_OID"
         addStrListElem aggHeadReferredColumns, "CDIDIV_OID"
         addStrListElem aggHeadReferredColumns, g_anIsNational
         tupVarRef = tupVarAh
       Else
         tupVarRef = tupVarSrc
       End If
     End If

     ' FIXME: get rid of this hard-coded column name
     Print #fileNo, addTab(2); "-- baseCodeType"
     Print #fileNo, addTab(2); "(SELECT T.CODETYPENUMBER FROM "; g_qualTabNameCodeType; " T WHERE T."; g_anOid; " = "; tupVarRef; ".CTYTYP_OID),"
     Print #fileNo, addTab(2); "-- codeKind"
     Print #fileNo, addTab(2); "(CASE "; tupVarRef; "."; g_anIsNational; " WHEN 0 THEN 1 WHEN 1 THEN 2 ELSE NULL END),"
   Else
     Print #fileNo, addTab(2); "-- baseCodeType"
     Print #fileNo, addTab(2); "CAST(NULL AS CHAR(1)),"
     Print #fileNo, addTab(2); "-- codeKind"
     Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtEnumId; "),"
   End If

   Dim ctTargetClassIndex As Integer
   Dim ctTargetClassIndexAh As Integer
   Dim ctFkAttrName As String
   Dim ctQualObjName As String

   Dim endSlotTargetClassIndex As Integer
   Dim endSlotTargetClassIndexAh As Integer
   Dim endSlotFkAttrName As String
   Dim endSlotQualObjName As String

   If cr132 Then
     ' baseEndSlotOid
     genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "baseEndSlotOid", "BES", endSlotTargetClassIndex, _
       endSlotTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", endSlotQualObjName, endSlotFkAttrName, _
       includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
       tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns
   End If

   Dim slotTypeTargetClassIndex As Integer
   Dim slotTypeTargetClassIndexAh As Integer
   Dim slotTypeFkAttrName As String
   Dim slotTypeQualObjName As String
   Dim slotTypeIdIsGen As Boolean
   Dim slotTypeIdIsAggHead As Boolean
   Dim slotTypeIdIsAggHeadGen As Boolean
   ' slotType_Id
   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "slotType_Id", "BEG", slotTypeTargetClassIndex, _
     slotTypeTargetClassIndexAh, "CAST(NULL AS " & g_dbtEnumId & ")", slotTypeQualObjName, slotTypeFkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
     tupVarSrc, tupVarAh, tupVarSrcGen, , slotTypeIdIsAggHead, slotTypeIdIsGen, slotTypeIdIsAggHeadGen, , _
     aggHeadReferredColumns, genReferredColumns, aggHeadGenReferredColumns
   includeGenInJoinPath = includeGenInJoinPath Or (slotTypeIdIsGen And Not slotTypeIdIsAggHeadGen And slotTypeTargetClassIndex <= 0 And slotTypeTargetClassIndexAh <= 0)
   includeAggHeadGenInJoinPath = includeAggHeadGenInJoinPath Or (slotTypeIdIsAggHeadGen And Not slotTypeIdIsGen And slotTypeTargetClassIndex <= 0 And slotTypeTargetClassIndexAh <= 0)
   includeAggHeadInJoinPath = includeAggHeadInJoinPath Or slotTypeIdIsAggHead

   ' allowedCountries
   Dim qualCountryListFuncName As String
   Dim qualCountryListFuncNameSuffix As String
   ' for LRT- and SETPRODUCTIVE changelog use 'country-list-function' aware of 'deleted records'
   qualCountryListFuncNameSuffix = IIf((clMode = eclLrt) Or (clMode = eclSetProd), "_D", "")

   Dim acTargetClassIndex As Integer
   Dim acTargetClassIndexAh As Integer
   Dim acFkAttrName As String
   Dim acQualObjName As String
   Dim acReferredColumnList As String
 
   If allowedCountriesRelIndex > 0 Then
       qualCountryListFuncName = _
         genQualFuncName( _
           g_relationships.descriptors(allowedCountriesRelIndex).sectionIndex, udfnAllowedCountry2Str0 & qualCountryListFuncNameSuffix, ddlType, thisOrgIndex, srcPoolIndex _
         )
     Print #fileNo, addTab(2); "-- aclacl_oid"
     If clMode = eclLrt Then
       Print #fileNo, addTab(2); "(SELECT OID FROM "; g_qualTabNameCountryIdList; " WHERE IDLIST = "; qualCountryListFuncName; "("; _
                                 tupVarSrc; "."; g_anOid; ","; tupVarSrc; "."; g_anInLrt; ",1024)),"
     Else
       Print #fileNo, addTab(2); "(SELECT OID FROM "; g_qualTabNameCountryIdList; " WHERE IDLIST = "; qualCountryListFuncName; "("; _
                                 tupVarSrc; "."; g_anOid; ",1024)),"
     End If
   Else
     genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "aclacl_oid", "AC", acTargetClassIndex, _
       acTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", acQualObjName, acFkAttrName, _
       includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
       tupVarSrc, tupVarAh, tupVarSrcGen, , , , , acReferredColumnList, aggHeadReferredColumns
   End If

   ' disallowedCountries
   Dim dcTargetClassIndex As Integer
   Dim dcTargetClassIndexAh As Integer
   Dim dcFkAttrName As String
   Dim dcQualObjName As String
   Dim dcReferredColumnList As String

   If disAllowedCountriesRelIndex > 0 Then
       qualCountryListFuncName = _
         genQualFuncName( _
           g_relationships.descriptors(disAllowedCountriesRelIndex).sectionIndex, udfnDisallowedCountry2Str0 & qualCountryListFuncNameSuffix, ddlType, thisOrgIndex, srcPoolIndex _
         )
     Print #fileNo, addTab(2); "-- dcldcl_oid"
     If clMode = eclLrt Then
       Print #fileNo, addTab(2); "(SELECT OID FROM "; g_qualTabNameCountryIdList; " WHERE IDLIST = "; qualCountryListFuncName; "("; _
                                 tupVarSrc; "."; g_anOid; ","; tupVarSrc; "."; g_anInLrt; ",1024)),"
     Else
       Print #fileNo, addTab(2); "(SELECT OID FROM "; g_qualTabNameCountryIdList; " WHERE IDLIST = "; qualCountryListFuncName; "("; _
                                 tupVarSrc; "."; g_anOid; ",1024)),"
     End If
   Else
     genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "dcldcl_oid", "DC", dcTargetClassIndex, _
       dcTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", dcQualObjName, dcFkAttrName, _
       includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
       tupVarSrc, tupVarAh, tupVarSrcGen, , , , , dcReferredColumnList, aggHeadReferredColumns
   End If
 
   ' assignedPaintZoneKey
   Dim assignedPzkTargetClassIndex As Integer
   Dim assignedPzkTargetClassIndexAh As Integer
   Dim assignedPzkFkAttrName As String
   Dim assignedPzkQualObjName As String
   Dim assignedPzkIsGen As Boolean
   Dim assignedPzkIsAggHead As Boolean
   Dim assignedPzkIsAggHeadGen As Boolean

   genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, conAssignedPaintZoneKey, "BEG", assignedPzkTargetClassIndex, _
     assignedPzkTargetClassIndexAh, "CAST(NULL AS VARCHAR(15))", assignedPzkQualObjName, assignedPzkFkAttrName, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
     tupVarSrc, tupVarAh, tupVarSrcGen, , assignedPzkIsAggHead, assignedPzkIsGen, assignedPzkIsAggHeadGen, , _
     aggHeadReferredColumns, genReferredColumns, aggHeadGenReferredColumns
   includeGenInJoinPath = includeGenInJoinPath Or (assignedPzkIsGen And Not assignedPzkIsAggHeadGen And assignedPzkTargetClassIndex <= 0 And assignedPzkIsAggHeadGen <= 0)
   includeAggHeadGenInJoinPath = includeAggHeadGenInJoinPath Or (assignedPzkIsAggHeadGen And assignedPzkIsGen And assignedPzkTargetClassIndex <= 0 And assignedPzkIsAggHeadGen <= 0)
   includeAggHeadInJoinPath = includeAggHeadInJoinPath Or assignedPzkIsAggHead

   'includeAggHeadInJoinPath = includeAggHeadInJoinPath Or (clMode = eclSetProd)


   Dim divisionTargetClassIndex As Integer
   Dim divisionTargetClassIndexAh As Integer
   Dim divisionFkAttrName As String
   Dim divisionQualObjName As String
   Dim divisionReferredColumnList As String
   Dim noJoinForDivisionOid As Boolean
   noJoinForDivisionOid = (aggHeadClassIndex = g_classIndexGenericCode) And (acmEntityType = eactRelationship) And Not isPsTagged

   ' divisionOid
   If bcTargetClassIndex > 0 Then
     Print #fileNo, addTab(2); "-- divisionOid"
     Print #fileNo, addTab(2); "BC.CDIDIV_OID,"
   ElseIf noJoinForDivisionOid Then
     Print #fileNo, addTab(2); "-- divisionOid"
     Print #fileNo, addTab(2); tupVarSrc; "."; "DIV_OID,"
   Else
     genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "divisionOid", "DIV", divisionTargetClassIndex, _
       divisionTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", divisionQualObjName, divisionFkAttrName, _
       includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, False, True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , divisionReferredColumnList, aggHeadReferredColumns
   End If

   ' dpClassNumber
   genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, conDpClassNumber, "NULL", forGen, _
     includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "SMALLINT", 2, True, tupVarSrc, tupVarSrcGen, IIf(parTabIsAhTab, tupVarSrcPar, tupVarAh), _
     tupVarSrcPar, , aggHeadReferredColumns

   ' isPerformedInMassupdate
   Print #fileNo, addTab(2); "-- isPerformedInMassupdate"
   Print #fileNo, addTab(2); "0,"
 ' ### ENDIF IVK ###
   ' lrtOid
   Print #fileNo, addTab(2); "-- lrtOid"
   If clMode = eclLrt Then
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anInLrt; ","
   Else
     Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
   End If

 ' ### IF IVK ###
   If clMode <> eclPubUpdate And clMode <> eclPubMassUpdate And Not (clMode = eclSetProd And condenseData) Then
 ' ### ELSE IVK ###
 ' If clMode <> eclPubUpdate Then
 ' ### ENDIF IVK ###
     ' operation_Id
     Print #fileNo, addTab(2); "-- operation_Id"

     If clMode = eclLrt Then
       Print #fileNo, addTab(2); tupVarSrc; ".LRTSTATE,"
 ' ### IF IVK ###
     Else
       If condenseData Then
         Print #fileNo, addTab(2); "CAST((CASE WHEN "; tupVarTgt; "."; g_anOid; _
                                   " IS NULL THEN "; CStr(lrtStatusCreated); " ELSE "; CStr(lrtStatusUpdated); " END) AS "; g_dbtEnumId; "),"
       Else
         Print #fileNo, addTab(2); "CAST((CASE WHEN "; tupVarSrc; "."; conIsDeleted; _
                                   " = 1 THEN "; CStr(lrtStatusDeleted); " WHEN "; tupVarTgt; "."; g_anOid; _
                                   " IS NULL THEN "; CStr(lrtStatusCreated); " ELSE "; CStr(lrtStatusUpdated); " END) AS "; g_dbtEnumId; "),"
       End If
 ' ### ENDIF IVK ###
     End If
   End If

 ' ### IF IVK ###
   ' status_Id
   Print #fileNo, addTab(2); "-- status_Id"
   If condenseData Then
     Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtEnumId; "),"
   Else
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anStatus; ","
   End If

 ' ### ENDIF IVK ###
   ' operationTimestamp
   Print #fileNo, addTab(2); "-- operationTimestamp"
   Print #fileNo, addTab(2); tupVarSrc; "."; g_anLastUpdateTimestamp; ","

 ' ### IF IVK ###
   If clMode <> eclPubUpdate And clMode <> eclPubMassUpdate And Not condenseData Then
 ' ### ELSE IVK ###
 ' If clMode <> eclPubUpdate Then
 ' ### ENDIF IVK ###
     Dim i As Integer
     For i = 1 To tabColumns.numDescriptors
         If isClAttrCat(tabColumns.descriptors(i).columnCategory, clMode = eclLrt) Then
           Dim attrTypeId As typeId
           attrTypeId = g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).dataType

           ' valueType
           Print #fileNo, addTab(2); CStr(getClColTypeByAttrType(attrTypeId)); ","

 ' ### IF IVK ###
           If (tabColumns.descriptors(i).columnCategory And eacExpression) Then
             Dim oldVal As String, newVal As String
             Dim transformationExpr As AttributeListTransformation
             initAttributeTransformation transformationExpr, 0, , , , tupVarTgt & "."
             setAttributeTransformationContext transformationExpr, thisOrgIndex, dstPoolIndex, tupVarTgt
             newVal = transformAttrName(tabColumns.descriptors(i).columnName, eavtDomain, tabColumns.descriptors(i).dbDomainIndex, transformationExpr, ddlType, , , , , tabColumns.descriptors(i).acmAttributeIndex, edomValueExpression, , , , tabColumns.descriptors(i).columnCategory)
             transformationExpr.attributePrefix = tupVarSrc & "."
             setAttributeTransformationContext transformationExpr, thisOrgIndex, srcPoolIndex, tupVarSrc, , clMode = eclLrt
             oldVal = transformAttrName(tabColumns.descriptors(i).columnName, eavtDomain, tabColumns.descriptors(i).dbDomainIndex, transformationExpr, ddlType, , , , , tabColumns.descriptors(i).acmAttributeIndex, edomValueExpression, , , , tabColumns.descriptors(i).columnCategory)

             If attrTypeId = etBoolean Then
               Print #fileNo, addTab(2); "RTRIM(CAST("; newVal; " AS CHAR(30))),"
               Print #fileNo, addTab(2); "RTRIM(CAST("; oldVal; " AS CHAR(30))),"
             End If

             Print #fileNo, addTab(2); newVal; ","
             Print #fileNo, addTab(2); oldVal; ","
           Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
             ' oldValueString / newValueString
             If attrTypeId = etBigInt Or _
                attrTypeId = etDecimal Or _
                attrTypeId = etDouble Or _
                attrTypeId = etFloat Or _
                attrTypeId = etInteger Or _
                attrTypeId = etSmallint Or _
                attrTypeId = etBoolean Then
               Print #fileNo, addTab(2); "RTRIM(CAST("; tupVarTgt; "."; tabColumns.descriptors(i).columnName; " AS CHAR(30))),"
               Print #fileNo, addTab(2); "RTRIM(CAST("; tupVarSrc; "."; tabColumns.descriptors(i).columnName; " AS CHAR(30))),"
             Else
               Print #fileNo, addTab(2); "CAST("; tupVarTgt; "."; tabColumns.descriptors(i).columnName; " AS VARCHAR(4000)),"
               Print #fileNo, addTab(2); "CAST("; tupVarSrc; "."; tabColumns.descriptors(i).columnName; " AS VARCHAR(4000)),"
             End If

             ' oldValueTimestamp / newValueTimestamp
             If attrTypeMapsToClColType(attrTypeId, clValueTypeTimeStamp) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               Print #fileNo, addTab(2); tupVarSrc; "."; tabColumns.descriptors(i).columnName; ","
             End If

             ' oldValueDate / newValueDate
             If attrTypeMapsToClColType(attrTypeId, clValueTypeDate) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               Print #fileNo, addTab(2); tupVarSrc; "."; tabColumns.descriptors(i).columnName; ","
             End If

             ' oldValueInteger / newValueInteger
             If attrTypeMapsToClColType(attrTypeId, clValueTypeInteger) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               Print #fileNo, addTab(2); tupVarSrc; "."; tabColumns.descriptors(i).columnName; ","
             End If

             ' oldValueBigInt / newValueBigInt
             If attrTypeMapsToClColType(attrTypeId, clValueTypeBigInteger) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               Print #fileNo, addTab(2); tupVarSrc; "."; tabColumns.descriptors(i).columnName; ","
             End If

             ' oldValueDecimal / newValueDecimal
             If attrTypeMapsToClColType(attrTypeId, clValueTypeDecimal) Then
               Print #fileNo, addTab(2); "CAST("; tupVarTgt; "."; tabColumns.descriptors(i).columnName; " AS DECIMAL(31,10)),"
               Print #fileNo, addTab(2); "CAST("; tupVarSrc; "."; tabColumns.descriptors(i).columnName; " AS DECIMAL(31,10)),"
             End If

             ' oldValueBoolean / newValueBoolean
             If attrTypeMapsToClColType(attrTypeId, clValueTypeBoolean) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               Print #fileNo, addTab(2); tupVarSrc; "."; tabColumns.descriptors(i).columnName; ","
             End If
 ' ### IF IVK ###
           End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
         End If
     Next i
   End If

 ' ### IF IVK ###
   If clMode = eclSetProd Then
     ' cdUserId
     Print #fileNo, addTab(2); "COALESCE("; tupVarSrc; "."; g_anUpdateUser; ","; tupVarSrc; "."; g_anCreateUser; ",'-unk-'),"
   End If

   ' ps_Oid
   Print #fileNo, addTab(2); "-- ps_Oid"
   If isPsTagged Then
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anPsOid
   Else
     Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; ")"
   End If
 ' ### ENDIF IVK ###

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualSourceTabName; " "; tupVarSrc

   If referToAggHeadInChangeLog And checkAggHeadForAttrs And includeAggHeadInJoinPath Then
     Print #fileNo, addTab(1); "LEFT OUTER JOIN"

     genTabSubQueryByEntityIndex aggHeadClassIndex, eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, (clMode = eclLrt), forGen, "AH", aggHeadReferredColumns, 2, , ""

     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhOid; " = "; tupVarAh; "."; g_anOid

     If clMode = eclLrt Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "("
 ' ### IF IVK ###
       Print #fileNo, addTab(3); "(("; tupVarAh; "."; g_anIsLrtPrivate; " = 0) AND ("; tupVarAh; "."; g_anIsDeleted; " = 0) AND ("; tupVarAh; "."; g_anInLrt; " IS NULL OR "; tupVarAh; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(3); "(("; tupVarAh; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarAh; "."; g_anInLrt; " IS NULL OR "; tupVarAh; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(4); "OR"
       If isPsTagged Then
         Print #fileNo, addTab(3); "(("; tupVarAh; "."; g_anIsLrtPrivate; " = 1) AND ("; tupVarAh; ".LRTSTATE <> "; CStr(lrtStatusDeleted); ") AND ("; tupVarAh; "."; g_anInLrt; " = PRIV."; g_anInLrt; "))"
       Else
         Print #fileNo, addTab(3); "(("; tupVarAh; "."; g_anIsLrtPrivate; " = 1) AND ("; tupVarAh; "."; g_anInLrt; " = PRIV."; g_anInLrt; "))"
       End If
       Print #fileNo, addTab(2); ")"
     End If
   End If

   If includeAggHeadGenInJoinPath Then
     Dim aggHeadFkAttrName As String
     aggHeadFkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(aggHeadClassIndex).shortName)
     aggHeadGenReferredColumns = aggHeadGenReferredColumns & IIf(aggHeadGenReferredColumns = "", "", ",") & aggHeadFkAttrName

     Print #fileNo, addTab(1); "LEFT OUTER JOIN"
 
     genTabSubQueryByEntityIndex aggHeadClassIndex, eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, (clMode = eclLrt), True, tupVarSrcGen, aggHeadGenReferredColumns, 2, , ""

     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhOid; ""; " = "; tupVarSrcGen; "."; aggHeadFkAttrName

     If clMode = eclLrt Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "("
 ' ### IF IVK ###
       Print #fileNo, addTab(3); "(("; tupVarSrcGen; "."; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anIsDeleted; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(3); "(("; tupVarSrcGen; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(4); "OR"
       Print #fileNo, addTab(3); "(("; tupVarSrcGen; "."; g_anIsLrtPrivate; " = 1) AND ("; tupVarSrcGen; ".LRTSTATE <> "; CStr(lrtStatusDeleted); ") AND ("; tupVarSrcGen; "."; g_anInLrt; " = PRIV."; g_anInLrt; "))"
       Print #fileNo, addTab(2); ")"
     End If

     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); tupVarSrcGen; ".ROWNUM = 1"
   ElseIf includeGenInJoinPath Then
     genReferredColumns = genReferredColumns & IIf(genReferredColumns = "", "", ",") & genSurrogateKeyName(ddlType, entityShortName)

     Print #fileNo, addTab(1); "LEFT OUTER JOIN"
     genTabSubQueryByEntityIndex acmEntityIndex, eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, clMode = eclLrt, True, tupVarSrcGen, genReferredColumns, 2, , ""

     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anOid; " = "; tupVarSrcGen; "."; genSurrogateKeyName(ddlType, entityShortName)

     If clMode = eclLrt Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "("
 ' ### IF IVK ###
       Print #fileNo, addTab(3); "(("; tupVarSrcGen; "."; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anIsDeleted; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(3); "(("; tupVarSrcGen; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(4); "OR"
       Print #fileNo, addTab(3); "(("; tupVarSrcGen; "."; g_anIsLrtPrivate; " = 1) AND ("; tupVarSrcGen; ".LRTSTATE <> "; CStr(lrtStatusDeleted); ") AND ("; tupVarSrcGen; "."; g_anInLrt; " = PRIV."; g_anInLrt; "))"
       Print #fileNo, addTab(2); ")"
     End If

     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); tupVarSrcGen; ".ROWNUM = 1"
   End If

   If forGen Then
     Dim qualViewNameNonGen As String
     qualViewNameNonGen = _
       genQualViewNameByEntityIndex( _
         acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, False, True, useMqtToImplementLrtForEntity _
       )

     Print #fileNo, addTab(1); "INNER JOIN"
     Print #fileNo, addTab(2); qualViewNameNonGen; " "; tupVarSrcPar
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; genAttrName(conOid, ddlType, entityShortName); " = "; tupVarSrcPar; "."; g_anOid
   End If

 ' ### IF IVK ###
   If clMode <> eclPubUpdate And clMode <> eclPubMassUpdate And Not condenseData Then
 ' ### ELSE IVK ###
 ' If clMode <> eclPubUpdate Then
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(1); "LEFT OUTER JOIN"

     Print #fileNo, addTab(2); qualTargetTabName; " "; tupVarTgt
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anOid; " = "; tupVarTgt; "."; g_anOid
   End If

 ' ### IF IVK ###
   genCondOuterJoin fileNo, propertyTargetClassIndex, propertyTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "PRP", propertyFkAttrName, ddlType
   If Not noJoinForDivisionOid Then
     genCondOuterJoin fileNo, divisionTargetClassIndex, divisionTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "DIV", divisionFkAttrName, ddlType
   End If
   genCondOuterJoin fileNo, propertyTypeTargetClassIndex, propertyTypeTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "PT", propertyTypeFkAttrName, ddlType
   genCondOuterJoin fileNo, bcTargetClassIndex, bcTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "BC", bcFkAttrName, ddlType, , bcReferredColumnList
   If cr132 Then
     genCondOuterJoin fileNo, endSlotTargetClassIndex, endSlotTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "BES", endSlotFkAttrName, ddlType, , "ASSIGNEDPAINTZONEKEY," & g_anSlotType
   End If

   'Change only on View V_CL_GENERICASPECT (Defect 19001 wf)
   If (acmEntityIndex = g_classIndexGenericAspect) Then
     genCondOuterJoin fileNo, assignedPzkTargetClassIndex, assignedPzkTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "BEG", assignedPzkFkAttrName, ddlType, , "ASSIGNEDPAINTZONEKEY," & g_anSlotType, assignedPzkIsGen Or assignedPzkIsAggHeadGen, , False
   Else
     ' we know that ASSIGNEDPAINTZONEKEY and SLOTTYPE_ID always go hand-in-hand. we thus use some hard-coding here
     genCondOuterJoin fileNo, assignedPzkTargetClassIndex, assignedPzkTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "BEG", assignedPzkFkAttrName, ddlType, , "ASSIGNEDPAINTZONEKEY," & g_anSlotType, assignedPzkIsGen Or assignedPzkIsAggHeadGen
   End If

   Dim refColumnsNSrX As String
   refColumnsNSrX = g_anOid & "," & g_anCodeNumber

   genCondOuterJoin fileNo, s0_01TargetClassIndex, s0_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S01", s0_01FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s0_02TargetClassIndex, s0_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S02", s0_02FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s0_03TargetClassIndex, s0_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S03", s0_03FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s0_04TargetClassIndex, s0_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S04", s0_04FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s0_05TargetClassIndex, s0_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S05", s0_05FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s0_06TargetClassIndex, s0_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S06", s0_06FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s0_07TargetClassIndex, s0_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S07", s0_07FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s0_08TargetClassIndex, s0_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S08", s0_08FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s0_09TargetClassIndex, s0_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S09", s0_09FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s0_10TargetClassIndex, s0_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S10", s0_10FkAttrName, ddlType, , refColumnsNSrX
 
   genCondOuterJoin fileNo, s1_01TargetClassIndex, s1_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S101", s1_01FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s1_02TargetClassIndex, s1_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S102", s1_02FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s1_03TargetClassIndex, s1_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S103", s1_03FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s1_04TargetClassIndex, s1_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S104", s1_04FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s1_05TargetClassIndex, s1_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S105", s1_05FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s1_06TargetClassIndex, s1_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S106", s1_06FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s1_07TargetClassIndex, s1_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S107", s1_07FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s1_08TargetClassIndex, s1_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S108", s1_08FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s1_09TargetClassIndex, s1_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S109", s1_09FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, s1_10TargetClassIndex, s1_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "S110", s1_10FkAttrName, ddlType, , refColumnsNSrX

   genCondOuterJoin fileNo, ns1_01TargetClassIndex, ns1_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N101", ns1_01FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, ns1_02TargetClassIndex, ns1_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N102", ns1_02FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, ns1_03TargetClassIndex, ns1_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N103", ns1_03FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, ns1_04TargetClassIndex, ns1_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N104", ns1_04FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, ns1_05TargetClassIndex, ns1_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N105", ns1_05FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, ns1_06TargetClassIndex, ns1_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N106", ns1_06FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, ns1_07TargetClassIndex, ns1_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N107", ns1_07FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, ns1_08TargetClassIndex, ns1_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N108", ns1_08FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, ns1_09TargetClassIndex, ns1_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N109", ns1_09FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, ns1_10TargetClassIndex, ns1_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "N110", ns1_10FkAttrName, ddlType, , refColumnsNSrX
   genCondOuterJoin fileNo, acTargetClassIndex, acTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "AC", acFkAttrName, ddlType
   genCondOuterJoin fileNo, dcTargetClassIndex, dcTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "DC", dcFkAttrName, ddlType
   genCondOuterJoin fileNo, csBaumusterTargetClassIndex, csBaumusterTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrc, tupVarAh, "CSB", csBaumusterFkAttrName, ddlType

 ' ### ENDIF IVK ###
   If clMode = eclLrt Then
     Print #fileNo, addTab(1); "WHERE"
 ' ### IF IVK ###
     If condenseData Then
       Print #fileNo, addTab(2); tupVarSrc; ".LRTSTATE = "; CStr(lrtStatusCreated)
     Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(2); tupVarSrc; ".LRTSTATE <> "; CStr(lrtStatusLocked)
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); tupVarSrc; ".LRTSTATE <> "; CStr(lrtStatusDeleted)
       Print #fileNo, addTab(4); "OR"
       Print #fileNo, addTab(3); tupVarTgt; "."; g_anOid; " IS NOT NULL"
       Print #fileNo, addTab(2); ")"
 ' ### IF IVK ###
     End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
   End If

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 ' ### IF IVK ###
   If clMode = eclPubUpdate Or clMode = eclPubMassUpdate Then
 ' ### ELSE IVK ###
 ' If clMode = eclPubUpdate Then
 ' ### ENDIF IVK ###
     Exit Sub
   End If

   If hasNlAttributes Then
     ' ####################################################################################################################
     ' #    ChangeLog-View for NL-Tab
     ' ####################################################################################################################

     includeAggHeadInJoinPath = (clMode = eclSetProd)

     initAttributeTransformation transformation, 0
     tabColumns = nullEntityColumnDescriptors
     genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, _
       fileNo, , False, ddlType, thisOrgIndex, dstPoolIndex, 0, forGen, False, , edomNone

     qualViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , True, "CL")

     printSectionHeader "ChangeLog-View (" & genClModeDescription(clMode) & ") for table """ & qualTargetNlTabName & """ (ACM-" & _
                        IIf(acmEntityType = eactClass, "Class", "Relationship") & """" & _
                        g_sections.descriptors(sectionIndex).sectionName & "." & entityName & """)", fileNo

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE VIEW"
     Print #fileNo, addTab(1); qualViewName
     Print #fileNo, addTab(0); "("

     Print #fileNo, addTab(1); "entityId,"
     Print #fileNo, addTab(1); "entityType,"
     Print #fileNo, addTab(1); "ahClassId,"
     Print #fileNo, addTab(1); "ahObjectId,"
     Print #fileNo, addTab(1); "gen,"
     Print #fileNo, addTab(1); "nl,"
     Print #fileNo, addTab(1); "dbTableName,"
     Print #fileNo, addTab(1); "objectId,"
     If acmEntityType = eactRelationship Then
       Print #fileNo, addTab(1); "refClassId1,"
       Print #fileNo, addTab(1); "refObjectId1,"
       Print #fileNo, addTab(1); "refClassId2,"
       Print #fileNo, addTab(1); "refObjectId2,"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(1); "price,"
     Print #fileNo, addTab(1); "propertyOid,"
     Print #fileNo, addTab(1); "propertyType_Id,"
     Print #fileNo, addTab(1); "isNational,"
     Print #fileNo, addTab(1); "csBaumuster,"
     If acmEntityType = eactClass Then
       Print #fileNo, addTab(1); "sr0Context,"
       Print #fileNo, addTab(1); "sr0Code1,"
       Print #fileNo, addTab(1); "sr0Code2,"
       Print #fileNo, addTab(1); "sr0Code3,"
       Print #fileNo, addTab(1); "sr0Code4,"
       Print #fileNo, addTab(1); "sr0Code5,"
       Print #fileNo, addTab(1); "sr0Code6,"
       Print #fileNo, addTab(1); "sr0Code7,"
       Print #fileNo, addTab(1); "sr0Code8,"
       Print #fileNo, addTab(1); "sr0Code9,"
       Print #fileNo, addTab(1); "sr0Code10,"

       Print #fileNo, addTab(1); "sr0CodeOid1,"
       Print #fileNo, addTab(1); "sr0CodeOid2,"
       Print #fileNo, addTab(1); "sr0CodeOid3,"
       Print #fileNo, addTab(1); "sr0CodeOid4,"
       Print #fileNo, addTab(1); "sr0CodeOid5,"
       Print #fileNo, addTab(1); "sr0CodeOid6,"
       Print #fileNo, addTab(1); "sr0CodeOid7,"
       Print #fileNo, addTab(1); "sr0CodeOid8,"
       Print #fileNo, addTab(1); "sr0CodeOid9,"
       Print #fileNo, addTab(1); "sr0CodeOid10,"

       Print #fileNo, addTab(1); "sr1Context,"
       Print #fileNo, addTab(1); "sr1Code1,"
       Print #fileNo, addTab(1); "sr1Code2,"
       Print #fileNo, addTab(1); "sr1Code3,"
       Print #fileNo, addTab(1); "sr1Code4,"
       Print #fileNo, addTab(1); "sr1Code5,"
       Print #fileNo, addTab(1); "sr1Code6,"
       Print #fileNo, addTab(1); "sr1Code7,"
       Print #fileNo, addTab(1); "sr1Code8,"
       Print #fileNo, addTab(1); "sr1Code9,"
       Print #fileNo, addTab(1); "sr1Code10,"
 
       Print #fileNo, addTab(1); "nsr1Context,"
       Print #fileNo, addTab(1); "nsr1Code1,"
       Print #fileNo, addTab(1); "nsr1Code2,"
       Print #fileNo, addTab(1); "nsr1Code3,"
       Print #fileNo, addTab(1); "nsr1Code4,"
       Print #fileNo, addTab(1); "nsr1Code5,"
       Print #fileNo, addTab(1); "nsr1Code6,"
       Print #fileNo, addTab(1); "nsr1Code7,"
       Print #fileNo, addTab(1); "nsr1Code8,"
       Print #fileNo, addTab(1); "nsr1Code9,"
       Print #fileNo, addTab(1); "nsr1Code10,"

       Print #fileNo, addTab(1); "slotPlausibilityRuleType_ID,"
       Print #fileNo, addTab(1); "witexp_oid,"
       Print #fileNo, addTab(1); "winexp_oid,"
       Print #fileNo, addTab(1); "expexp_oid,"
     End If
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
     If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 '   If isGenForming And forGen Then
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(1); "validFrom,"
       Print #fileNo, addTab(1); "validTo,"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(1); "baseCodeNumber,"
     Print #fileNo, addTab(1); "baseCodeType,"
     Print #fileNo, addTab(1); "codeKind_id,"

     If cr132 Then
       Print #fileNo, addTab(1); "baseEndSlotOid,"
     End If
     Print #fileNo, addTab(1); "slotType_Id,"
     Print #fileNo, addTab(1); "aclacl_oid,"
     Print #fileNo, addTab(1); "dcldcl_oid,"
     Print #fileNo, addTab(1); "assignedPaintZoneKey,"
     Print #fileNo, addTab(1); "divisionOid,"
     Print #fileNo, addTab(1); "dpClassNumber,"
     Print #fileNo, addTab(1); "isPerformedInMassupdate,"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(1); "lrtOid,"
 ' ### IF IVK ###
     If Not (clMode = eclSetProd And condenseData) Then
       Print #fileNo, addTab(1); "operation_Id,"
     End If
     Print #fileNo, addTab(1); "status_Id,"
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(1); "operation_Id,"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(1); "operationTimestamp,"

     Print #fileNo, addTab(1); g_anLanguageId; ","

     For i = 1 To tabColumns.numDescriptors
 ' ### IF IVK ###
         If isClAttrCat(tabColumns.descriptors(i).columnCategory, clMode = eclLrt) And ((tabColumns.descriptors(i).columnCategory And eacNationalEntityMeta) = 0) Then
 ' ### ELSE IVK ###
 '       If isClAttrCat(.columnCategory, clMode = eclLrt) Then
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 2); "_t,"

           Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 2); "_o,"
           Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 2); "_n,"

           If attrTypeMapsToClColType(g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).dataType, clValueTypeInteger) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Io,"
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_In,"
           End If
         End If
     Next i

 ' ### IF IVK ###
     If clMode = eclSetProd Then
       Print #fileNo, addTab(1); "cdUserId,"
     End If
 ' ### ENDIF IVK ###

     Dim useParTab As Boolean
     useParTab = Not forGen
 ' ### IF IVK ###
     Print #fileNo, addTab(1); "ps_Oid"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "AS"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "SELECT"
     ' entityId / entityType
 
     If acmEntityType = eactClass Then
       Print #fileNo, addTab(2); "-- entityId"
       If hasOwnTable Then
         Print #fileNo, addTab(2); "'"; entityIdStr; "',"
       Else
       Print #fileNo, addTab(2); IIf(forGen, tupVarSrcParGen, tupVarSrcPar); "."; g_anCid; ","
       End If
       Print #fileNo, addTab(2); "-- entityType"
       Print #fileNo, addTab(2); "'"; gc_acmEntityTypeKeyClass; "',"
     Else
       Print #fileNo, addTab(2); "-- entityId"
       Print #fileNo, addTab(2); "'"; entityIdStr; "',"
       Print #fileNo, addTab(2); "-- entityType"
       Print #fileNo, addTab(2); "'"; gc_acmEntityTypeKeyRel; "',"
     End If
     ' ahClassId
     Print #fileNo, addTab(2); "-- ahClassId"
 
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhCid; ","
     ' ahObjectId
     Print #fileNo, addTab(2); "-- ahObjectId"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhOid; ","
     ' gen
     Print #fileNo, addTab(2); "-- gen"
     Print #fileNo, addTab(2); IIf(forGen, "1,", "0,")
     ' nl
     Print #fileNo, addTab(2); "-- nl"
     Print #fileNo, addTab(2); "1,"
     'dbTableName
     Print #fileNo, addTab(2); "-- dbTableName"
     Print #fileNo, addTab(2); "'"; getObjBaseName(qualTargetNlTabName); "',"
     ' objectId
     Print #fileNo, addTab(2); "-- objectId"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anOid; ","

     If acmEntityType = eactRelationship Then
       ' refClassId1
       Print #fileNo, addTab(2); "-- refClassId1"
       Print #fileNo, addTab(2); "'"; relLeftClassIdStr; "',"
       ' refObjectId1
       Print #fileNo, addTab(2); "-- refObjectId1"
       Print #fileNo, addTab(2); tupVarSrcPar; "."; relLeftFk; ","
       ' refClassId2
       Print #fileNo, addTab(2); "-- refClassId2"
       Print #fileNo, addTab(2); "'"; relRightClassIdStr; "',"
       ' refObjectId2
       Print #fileNo, addTab(2); "-- refObjectId2"
       Print #fileNo, addTab(2); tupVarSrcPar; "."; relRightFk; ","
       useParTab = True
     End If
 ' ### IF IVK ###

     ' price
     foundPrice = _
       genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "price", "PRI", priceTargetClassIndex, _
         priceTargetClassIndexAh, "CAST(NULL AS DECIMAL(15,3))", priceQualObjName, priceFkAttrName, _
         includeAggHeadInJoinPathForPrice, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, _
         tupVarAh, tupVarSrcGen, tupVarSrcPar, foundPriceInAggHead, , , , aggHeadReferredColumns, , , True)

     If foundPriceInAggHead And includeAggHeadInJoinPathForPrice Then
       If isAggHead Then
         Print #fileNo, addTab(2); "-- price"
         Print #fileNo, addTab(2); tupVarSrc; ".,"; priceFkAttrName; ","
       Else
         includeAggHeadInJoinPath = includeAggHeadInJoinPath Or includeAggHeadInJoinPathForPrice
         genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "price", "PRI", priceTargetClassIndex, _
           priceTargetClassIndexAh, "CAST(NULL AS DECIMAL(15, 3))", priceQualObjName, priceFkAttrName, _
           includeAggHeadInJoinPathForPrice, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, _
           tupVarAh, tupVarSrcGen, tupVarSrcPar, foundPriceInAggHead, , , , aggHeadReferredColumns
       End If
     Else
       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "price", "PRI", priceTargetClassIndex, _
         priceTargetClassIndexAh, "CAST(NULL AS DECIMAL(15,3))", priceQualObjName, priceFkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, _
         tupVarAh, tupVarSrcGen, tupVarSrcPar, foundPriceInAggHead, , , , aggHeadReferredColumns
     End If
 ' ### ENDIF IVK ###
 ' ### IF IVK ###

     ' propertyOId
     foundPropertyOid = _
       genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyOid", "PRP", propertyTargetClassIndex, _
         propertyTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", propertyQualObjName, propertyFkAttrName, _
         includeAggHeadInJoinPathForPropertyOid, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, _
         tupVarAh, tupVarSrcGen, , foundPropertyOidInAggHead, , , , aggHeadReferredColumns, , , True)

     If foundPropertyOidInAggHead And includeAggHeadInJoinPathForPropertyOid Then
       If isAggHead Then
         Print #fileNo, addTab(2); "-- propertyOid"
         Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhOid; ","
       Else
         includeAggHeadInJoinPath = includeAggHeadInJoinPath Or includeAggHeadInJoinPathForPropertyOid
         genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyOid", "PRP", propertyTargetClassIndex, _
           propertyTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", propertyQualObjName, propertyFkAttrName, _
           includeAggHeadInJoinPathForPropertyOid, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, _
           tupVarAh, tupVarSrcGen, , foundPropertyOidInAggHead, , , , aggHeadReferredColumns
       End If
     Else
       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyOid", "PRP", propertyTargetClassIndex, _
         propertyTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", propertyQualObjName, propertyFkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrc, _
         tupVarAh, tupVarSrcGen, , foundPropertyOidInAggHead, , , , aggHeadReferredColumns
     End If

     ' Fixme: get rid of hard-coding here!!
     If foundPropertyOid Then
         If clMode = eclLrt Then
           qualObjNamePropertyGen = genQualViewNameByClassIndex(g_classes.descriptors(IIf(propertyTargetClassIndex > 0, propertyTargetClassIndex, IIf(propertyTargetClassIndexAh > 0, propertyTargetClassIndexAh, IIf(foundPropertyOidInAggHead, aggHeadClassIndex, acmEntityIndex)))).classIndex, ddlType, thisOrgIndex, srcPoolIndex, True, True, g_classes.descriptors(IIf(propertyTargetClassIndex > 0, propertyTargetClassIndex, IIf(propertyTargetClassIndexAh > 0, propertyTargetClassIndexAh, IIf(foundPropertyOidInAggHead, aggHeadClassIndex, acmEntityIndex)))).useMqtToImplementLrt)
         Else
           qualObjNamePropertyGen = genQualTabNameByClassIndex(g_classes.descriptors(IIf(propertyTargetClassIndex > 0, propertyTargetClassIndex, IIf(propertyTargetClassIndexAh > 0, propertyTargetClassIndexAh, IIf(foundPropertyOidInAggHead, aggHeadClassIndex, acmEntityIndex)))).classIndex, ddlType, thisOrgIndex, srcPoolIndex, True)
         End If

       If propertyTargetClassIndex > 0 Or propertyTargetClassIndexAh > 0 Then
         Print #fileNo, addTab(2); "-- propertyType_ID"
         Print #fileNo, addTab(2); "(SELECT PRPG.TYPE_ID FROM "; qualObjNamePropertyGen; " PRPG WHERE PRPG.PRP_OID = PRP."; g_anOid; " ORDER BY "; g_anValidFrom; " DESC FETCH FIRST 1 ROW ONLY),"
       ElseIf foundPropertyOidInAggHead Then
         If Not genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyType_ID", "PT", propertyTypeTargetClassIndex, _
                  propertyTypeTargetClassIndexAh, "CAST(NULL AS " & g_dbtEnumId & ")", propertyTypeQualObjName, propertyTypeFkAttrName, _
                  includeAggHeadInJoinPath, False, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, _
                  IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns) Then
           Print #fileNo, addTab(2); "-- propertyType_ID"
           Print #fileNo, addTab(2); "(SELECT PRPG.TYPE_ID FROM "; qualObjNamePropertyGen; " PRPG WHERE PRPG.PRP_OID = "; tupVarAh; "."; g_anOid; " ORDER BY "; g_anValidFrom; " DESC FETCH FIRST 1 ROW ONLY),"
         End If
       Else
         Print #fileNo, addTab(2); "-- propertyType_ID"
         Print #fileNo, addTab(2); "(SELECT PRPG.TYPE_ID FROM "; qualObjNamePropertyGen; " PRPG WHERE PRPG.PRP_OID = "; tupVarSrc; "."; g_anOid; " ORDER BY "; g_anValidFrom; " DESC FETCH FIRST 1 ROW ONLY),"
       End If
     Else
       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "propertyType_ID", "PT", propertyTypeTargetClassIndex, _
         propertyTypeTargetClassIndexAh, "CAST(NULL AS " & g_dbtEnumId & ")", propertyTypeQualObjName, propertyTypeFkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, _
         IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns
     End If

     ' isNational
     If isNationalizable Then
       Print #fileNo, addTab(2); "-- isNational"
       Print #fileNo, addTab(2); tupVarSrcPar; "."; g_anIsNational; ","
       useParTab = True
     Else
       genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "isNational", gc_dbFalse, forGen, _
                         includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtBoolean, 2, True, tupVarSrcPar, tupVarAh, , , aggHeadReferredColumns
     End If
 
     ' csBaumuster
     Print #fileNo, addTab(2); "-- csBaumuster"
     If genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "csBaumuster", "CSB", csBaumusterTargetClassIndex, _
       csBaumusterTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", csBaumusterQualObjName, csBaumusterFkAttrName, _
       includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, tupVarSrcPar, tupVarAh, _
       tupVarSrcParGen, , csBaumusterFoundInAggHead, , , , aggHeadReferredColumns, , , True, True) Then

       Print #fileNo, addTab(2); "COALESCE(";

       ' FixMe: for csBaumuster we only navigate along 'direct' relationships (i.e. not related to aggregate head)
       ' A more generic criterion would be to check with which relationship this class effectively can be related to an Aggregate Head having a 'csBaumuster'-relationship
       ' E.g.: A DecisionTable may never have an NSR1Validity as Aggregate Head which then is related to some SR0Validity carying 'baumuster'
       genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "csBaumuster", "CSB", forGen, _
                         includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "CAST(NULL AS VARCHAR(1))", 2, True, tupVarSrcPar, _
                         tupVarAh, , , aggHeadReferredColumns, , True
       If csBaumusterTargetClassIndex > 0 And csBaumusterTargetClassIndexAh <= 0 Then
           genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "csBaumuster", "CSB", csBaumusterTargetClassIndex, _
             csBaumusterTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", csBaumusterQualObjName, csBaumusterFkAttrName, _
             includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, False, True, True, tupVarSrcPar, tupVarAh, tupVarSrcParGen, , , , , _
             , aggHeadReferredColumns, , , , True
       End If
       csBaumusterTargetClassIndexAh = 0
       Print #fileNo, "CAST(NULL AS VARCHAR(8))),"
     Else
       Print #fileNo, addTab(2); "CAST(NULL AS VARCHAR(8)),"
     End If

     If acmEntityType = eactClass Then
       ' sr0Context
       genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "sr0Context", "NULL", forGen, _
                         includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "VARCHAR(159)", 2, True, tupVarSrcPar, tupVarAh, , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code01", "S01", s0_01TargetClassIndex, _
         s0_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_01QualObjName, s0_01FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code02", "S02", s0_02TargetClassIndex, _
         s0_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_02QualObjName, s0_02FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code03", "S03", s0_03TargetClassIndex, _
         s0_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_03QualObjName, s0_03FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code04", "S04", s0_04TargetClassIndex, _
         s0_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_04QualObjName, s0_04FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code05", "S05", s0_05TargetClassIndex, _
         s0_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_05QualObjName, s0_05FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code06", "S06", s0_06TargetClassIndex, _
         s0_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_06QualObjName, s0_06FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code07", "S07", s0_07TargetClassIndex, _
         s0_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_07QualObjName, s0_07FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code08", "S08", s0_08TargetClassIndex, _
         s0_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_08QualObjName, s0_08FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code09", "S09", s0_09TargetClassIndex, _
         s0_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_09QualObjName, s0_09FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr0Code10", "S10", s0_10TargetClassIndex, _
         s0_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s0_10QualObjName, s0_10FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, , , , , , , , , aggHeadReferredColumns

       ' sr1CodeOids
       Print #fileNo, addTab(2); IIf(s0_01TargetClassIndex > 0 Or s0_01TargetClassIndexAh > 0, "S01." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"); ","
       Print #fileNo, addTab(2); IIf(s0_02TargetClassIndex > 0 Or s0_02TargetClassIndexAh > 0, "S02." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"), ","
       Print #fileNo, addTab(2); IIf(s0_03TargetClassIndex > 0 Or s0_03TargetClassIndexAh > 0, "S03." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"), ","
       Print #fileNo, addTab(2); IIf(s0_04TargetClassIndex > 0 Or s0_04TargetClassIndexAh > 0, "S04." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"), ","
       Print #fileNo, addTab(2); IIf(s0_05TargetClassIndex > 0 Or s0_05TargetClassIndexAh > 0, "S05." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"), ","
       Print #fileNo, addTab(2); IIf(s0_06TargetClassIndex > 0 Or s0_06TargetClassIndexAh > 0, "S06." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"), ","
       Print #fileNo, addTab(2); IIf(s0_07TargetClassIndex > 0 Or s0_07TargetClassIndexAh > 0, "S07." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"), ","
       Print #fileNo, addTab(2); IIf(s0_08TargetClassIndex > 0 Or s0_08TargetClassIndexAh > 0, "S08." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"), ","
       Print #fileNo, addTab(2); IIf(s0_09TargetClassIndex > 0 Or s0_09TargetClassIndexAh > 0, "S09." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"), ","
       Print #fileNo, addTab(2); IIf(s0_10TargetClassIndex > 0 Or s0_10TargetClassIndexAh > 0, "S10." & g_anOid, "CAST(NULL AS " & g_dbtOid & ")"), ","

       ' sr1Context
       genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "sr1Context", "NULL", forGen, _
                         includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "VARCHAR(159)", 2, True, tupVarSrcPar, tupVarAh, , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code01", "S101", s1_01TargetClassIndex, _
         s1_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_01QualObjName, s1_01FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code02", "S102", s1_02TargetClassIndex, _
         s1_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_02QualObjName, s1_02FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code03", "S103", s1_03TargetClassIndex, _
         s1_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_03QualObjName, s1_03FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code04", "S104", s1_04TargetClassIndex, _
         s1_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_04QualObjName, s1_04FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code05", "S105", s1_05TargetClassIndex, _
         s1_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_05QualObjName, s1_05FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code06", "S106", s1_06TargetClassIndex, _
         s1_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_06QualObjName, s1_06FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code07", "S107", s1_07TargetClassIndex, _
         s1_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_07QualObjName, s1_07FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code08", "S108", s1_08TargetClassIndex, _
         s1_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_08QualObjName, s1_08FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code09", "S109", s1_09TargetClassIndex, _
         s1_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_09QualObjName, s1_09FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "sr1Code10", "S110", s1_10TargetClassIndex, _
         s1_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", s1_10QualObjName, s1_10FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       ' nsr1Context
       genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "nsr1Context", "NULL", forGen, _
                         includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "VARCHAR(159)", 2, True, tupVarSrcPar, tupVarAh, , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code01", "N101", ns1_01TargetClassIndex, _
         ns1_01TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_01QualObjName, ns1_01FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code02", "N102", ns1_02TargetClassIndex, _
         ns1_02TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_02QualObjName, ns1_02FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code03", "N103", ns1_03TargetClassIndex, _
         ns1_03TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_03QualObjName, ns1_03FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code04", "N104", ns1_04TargetClassIndex, _
         ns1_04TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_04QualObjName, ns1_04FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code05", "N105", ns1_05TargetClassIndex, _
         ns1_05TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_05QualObjName, ns1_05FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code06", "N106", ns1_06TargetClassIndex, _
         ns1_06TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_06QualObjName, ns1_06FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code07", "N107", ns1_07TargetClassIndex, _
         ns1_07TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_07QualObjName, ns1_07FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code08", "N108", ns1_08TargetClassIndex, _
         ns1_08TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_08QualObjName, ns1_08FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code09", "N109", ns1_09TargetClassIndex, _
         ns1_09TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_09QualObjName, ns1_09FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "nsr1Code10", "N110", ns1_10TargetClassIndex, _
         ns1_10TargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", ns1_10QualObjName, ns1_10FkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, , True, True, tupVarSrc, tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns

       ' slotPlausibilityRuleType_ID
       genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "slotPlausibilityRuleType_ID", "NULL", forGen, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtEnumId, 2, True, IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , , aggHeadReferredColumns
       ' with
       genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "with", "NULL", forGen, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtOid, 2, True, IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , , aggHeadReferredColumns
       ' withNot
       genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "withNot", "NULL", forGen, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtOid, 2, True, IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , , aggHeadReferredColumns
       ' expression
       genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, "expression", "NULL", forGen, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , g_dbtOid, 2, True, IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , , aggHeadReferredColumns
     End If
 ' ### ENDIF IVK ###

     ' validFrom / validTo
 ' ### IF IVK ###
     If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 '   If isGenForming And forGen Then
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(2); "-- validFrom"
       Print #fileNo, addTab(2); IIf(forGen, tupVarSrcParGen, tupVarSrcPar); "."; g_anValidFrom; ","
       Print #fileNo, addTab(2); "-- validTo"
       Print #fileNo, addTab(2); IIf(forGen, tupVarSrcParGen, tupVarSrcPar); "."; g_anValidTo; ","
     End If
 ' ### IF IVK ###

     ' baseCodeNumber, baseCodeType and codeKind
     If genLrtLogRelColDdlAh(fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "baseCodeNumber", "BC", bcTargetClassIndex, _
          bcTargetClassIndexAh, "CAST(NULL AS VARCHAR(1))", bcQualObjName, bcFkAttrName, _
          includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, True, _
          tupVarSrc, tupVarAh, tupVarSrcGen, tupVarSrcPar, baseCodeNumberFoundInAh, , , bcReferredColumnList, aggHeadReferredColumns) Then

       If bcTargetClassIndex > 0 Or bcTargetClassIndexAh > 0 Then
         tupVarRef = "BC"
         addStrListElem bcReferredColumnList, g_anCodeNumber
         addStrListElem bcReferredColumnList, "CTLTLV_OID"
         addStrListElem bcReferredColumnList, "CTYTYP_OID"
         addStrListElem bcReferredColumnList, "CDIDIV_OID"
         addStrListElem bcReferredColumnList, g_anIsNational
       Else
         If baseCodeNumberFoundInAh Then
           addStrListElem aggHeadReferredColumns, g_anCodeNumber
           addStrListElem aggHeadReferredColumns, "CTLTLV_OID"
           addStrListElem aggHeadReferredColumns, "CTYTYP_OID"
           addStrListElem aggHeadReferredColumns, "CDIDIV_OID"
           addStrListElem aggHeadReferredColumns, g_anIsNational
           tupVarRef = tupVarAh
         Else
           tupVarRef = tupVarSrcPar
         End If
       End If

       Print #fileNo, addTab(2); "-- baseCodeType"
       Print #fileNo, addTab(2); "(SELECT T.CODETYPENUMBER FROM "; g_qualTabNameCodeType; " T WHERE T."; g_anOid; " = "; tupVarRef; ".CTYTYP_OID),"
       Print #fileNo, addTab(2); "-- codeKind"
       Print #fileNo, addTab(2); "(CASE "; tupVarRef; "."; g_anIsNational; " WHEN 0 THEN 1 WHEN 1 THEN 2 ELSE NULL END),"
     Else
       Print #fileNo, addTab(2); "-- baseCodeType"
       Print #fileNo, addTab(2); "CAST(NULL AS CHAR(1)),"
       Print #fileNo, addTab(2); "-- codeKind"
       Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtEnumId; "),"
     End If

     If cr132 Then
       ' baseEndSlotOid
       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "baseEndSlotOid", "BES", endSlotTargetClassIndex, _
         endSlotTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", endSlotQualObjName, endSlotFkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
         tupVarSrc, tupVarAh, tupVarSrcGen, tupVarSrcPar, , , , , aggHeadReferredColumns
     End If

     ' slotType_Id
     genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "slotType_Id", "BEG", slotTypeTargetClassIndex, _
       slotTypeTargetClassIndexAh, "CAST(NULL AS " & g_dbtEnumId & ")", slotTypeQualObjName, slotTypeFkAttrName, _
       includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
       IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , slotTypeIdIsAggHead, slotTypeIdIsGen, slotTypeIdIsAggHeadGen, , _
       aggHeadReferredColumns, genReferredColumns, aggHeadGenReferredColumns
     includeGenInJoinPath = includeGenInJoinPath Or (slotTypeIdIsGen And Not slotTypeIdIsAggHeadGen And slotTypeTargetClassIndex <= 0 And slotTypeTargetClassIndexAh <= 0)
     includeAggHeadGenInJoinPath = includeAggHeadGenInJoinPath Or (slotTypeIdIsAggHeadGen And Not slotTypeIdIsGen And slotTypeTargetClassIndex <= 0 And slotTypeTargetClassIndexAh <= 0)
     includeAggHeadInJoinPath = includeAggHeadInJoinPath Or slotTypeIdIsAggHead

     ' allowedCountries
     If allowedCountriesRelIndex > 0 Then
         qualCountryListFuncName = _
           genQualFuncName( _
             g_relationships.descriptors(allowedCountriesRelIndex).sectionIndex, udfnAllowedCountry2Str0 & qualCountryListFuncNameSuffix, ddlType, thisOrgIndex, srcPoolIndex _
           )
       Print #fileNo, addTab(2); "-- aclacl_oid"
       If clMode = eclLrt Then
         Print #fileNo, addTab(2); "(SELECT OID FROM "; g_qualTabNameCountryIdList; " WHERE IDLIST = "; qualCountryListFuncName; "("; _
                                   tupVarSrc; "."; g_anOid; ","; tupVarSrc & "." & g_anInLrt; ",1024)),"
       Else
         Print #fileNo, addTab(2); "(SELECT OID FROM "; g_qualTabNameCountryIdList; " WHERE IDLIST = "; qualCountryListFuncName; "("; _
                                   tupVarSrc; "."; g_anOid; ",1024)),"
       End If
     Else
       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "aclacl_oid", "AC", acTargetClassIndex, _
         acTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", acQualObjName, acFkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
         IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , , , , acReferredColumnList, aggHeadReferredColumns
     End If
 
     ' disallowedCountries
     If disAllowedCountriesRelIndex > 0 Then
         qualCountryListFuncName = _
           genQualFuncName( _
             g_relationships.descriptors(disAllowedCountriesRelIndex).sectionIndex, udfnDisallowedCountry2Str0 & qualCountryListFuncNameSuffix, ddlType, thisOrgIndex, srcPoolIndex _
           )
       Print #fileNo, addTab(2); "-- dcldcl_oid"
       If clMode = eclLrt Then
         Print #fileNo, addTab(2); "(SELECT OID FROM "; g_qualTabNameCountryIdList; " WHERE IDLIST = "; qualCountryListFuncName; "("; _
                                   tupVarSrc; "."; g_anOid; ","; tupVarSrc; "."; g_anInLrt; ",1024)),"
       Else
         Print #fileNo, addTab(2); "(SELECT OID FROM "; g_qualTabNameCountryIdList; " WHERE IDLIST = "; qualCountryListFuncName; "("; _
                                   tupVarSrc; "."; g_anOid; ",1024)),"
       End If
     Else
       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "dcldcl_oid", "DC", dcTargetClassIndex, _
         dcTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", dcQualObjName, dcFkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
         IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , , , , dcReferredColumnList, aggHeadReferredColumns
     End If

     ' assignedPaintZoneKey
     genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, conAssignedPaintZoneKey, "BEG", assignedPzkTargetClassIndex, _
       assignedPzkTargetClassIndexAh, "CAST(NULL AS VARCHAR(15))", assignedPzkQualObjName, assignedPzkFkAttrName, _
       includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, True, True, False, _
       IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , assignedPzkIsAggHead, assignedPzkIsGen, assignedPzkIsAggHeadGen, , _
       aggHeadReferredColumns, genReferredColumns, aggHeadGenReferredColumns
     includeGenInJoinPath = includeGenInJoinPath Or (assignedPzkIsGen And Not assignedPzkIsAggHeadGen And assignedPzkTargetClassIndex <= 0 And assignedPzkIsAggHeadGen <= 0)
     includeAggHeadGenInJoinPath = includeAggHeadGenInJoinPath Or (assignedPzkIsAggHeadGen And assignedPzkIsGen And assignedPzkTargetClassIndex <= 0 And assignedPzkIsAggHeadGen <= 0)
     includeAggHeadInJoinPath = includeAggHeadInJoinPath Or assignedPzkIsAggHead

     ' divisionOid
     If bcTargetClassIndex > 0 Then
       Print #fileNo, addTab(2); "-- divisionOid"
       Print #fileNo, addTab(2); "BC.CDIDIV_OID,"
     Else
       genLrtLogRelColDdlAh fileNo, relRefs, relRefsAh, attrMapping, attrMappingAh, "divisionOid", "DIV", divisionTargetClassIndex, _
         divisionTargetClassIndexAh, "CAST(NULL AS " & g_dbtOid & ")", divisionQualObjName, divisionFkAttrName, _
         includeAggHeadInJoinPath, checkAggHeadForAttrs, clMode, ddlType, thisOrgIndex, srcPoolIndex, forGen, 2, False, True, False, _
         IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, tupVarSrcGen, , , , , , aggHeadReferredColumns
     End If

     ' dpClassNumber
     genLrtLogColDdlAh fileNo, attrMapping, attrMappingAh, conDpClassNumber, "NULL", forGen, _
       includeAggHeadInJoinPath, checkAggHeadForAttrs, ddlType, , "SMALLINT", 2, _
       True, IIf(forGen, tupVarSrcParGen, tupVarSrcPar), tupVarAh, , , , aggHeadReferredColumns

     ' isPerformedInMassupdate
     Print #fileNo, addTab(2); "-- isPerformedInMassupdate"
     Print #fileNo, addTab(2); "0,"
 ' ### ENDIF IVK ###

     ' lrtOid
     Print #fileNo, addTab(2); "-- lrtOid"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anInLrt; ","
     ' operation_Id
 ' ### IF IVK ###
     If Not (clMode = eclSetProd And condenseData) Then
       Print #fileNo, addTab(2); "-- operation_Id"
     End If
     If clMode = eclLrt Then
       Print #fileNo, addTab(2); tupVarSrc; ".LRTSTATE,"
     Else
       Print #fileNo, addTab(2); "CAST((CASE WHEN "; tupVarSrc; "."; conIsDeleted; _
                                 " = 1 THEN "; CStr(lrtStatusDeleted); " WHEN "; tupVarTgt; "."; g_anOid; _
                                 " IS NULL THEN "; CStr(lrtStatusCreated); " ELSE "; CStr(lrtStatusUpdated); " END) AS "; g_dbtEnumId; "),"
     End If
     ' status_Id
     Print #fileNo, addTab(2); "-- status_Id"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anStatus; ","
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(2); "-- operation_Id"
 '   If clMode = eclLrt Then
 '     Print #fileNo, addTab(2); tupVarSrc ; ".LRTSTATE,"
 '   Else
 '     Print #fileNo, addTab(2); "CAST((CASE WHEN " ; tupVarTgt ; "." ; g_anOid ; _
 '                               " IS NULL THEN " ; CStr(lrtStatusCreated) ; " ELSE " ; CStr(lrtStatusUpdated) ; " END) AS "; g_dbtEnumId; "),"
 '   End If
 ' ### ENDIF IVK ###

     ' operationTimestamp
     Print #fileNo, addTab(2); "-- operationTimestamp"
     Print #fileNo, addTab(2); IIf(forGen, tupVarSrcParGen, tupVarSrcPar); "."; g_anLastUpdateTimestamp; ","

     ' language_Id
     Print #fileNo, addTab(2); "-- "; g_anLanguageId
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anLanguageId; ","

     For i = 1 To tabColumns.numDescriptors
         If isClAttrCat(tabColumns.descriptors(i).columnCategory, clMode = eclLrt) Then
           attrTypeId = g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).dataType

           ' valueType
           Print #fileNo, addTab(2); CStr(getClColTypeByAttrType(attrTypeId)); ","

           If attrTypeId = etBigInt Or _
              attrTypeId = etDecimal Or _
              attrTypeId = etDouble Or _
              attrTypeId = etFloat Or _
              attrTypeId = etInteger Or _
              attrTypeId = etSmallint Or _
              attrTypeId = etTime Or _
              attrTypeId = etTimestamp Or _
              attrTypeId = etDate Or _
              attrTypeId = etBoolean Then
             Print #fileNo, addTab(2); "RTRIM(CAST("; tupVarTgt; "."; tabColumns.descriptors(i).columnName; " AS CHAR(30))),"
             Print #fileNo, addTab(2); "RTRIM(CAST("; tupVarSrc; "."; tabColumns.descriptors(i).columnName; " AS CHAR(30))),"
           Else
             Print #fileNo, addTab(2); "CAST("; tupVarTgt; "."; tabColumns.descriptors(i).columnName; " AS VARCHAR(4000)),"
             Print #fileNo, addTab(2); "CAST("; tupVarSrc; "."; tabColumns.descriptors(i).columnName; " AS VARCHAR(4000)),"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeInteger) Then
             Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
             Print #fileNo, addTab(2); tupVarSrc; "."; tabColumns.descriptors(i).columnName; ","
           End If
         End If
     Next i
 ' ### IF IVK ###

     If clMode = eclSetProd Then
       ' cdUserId
       Print #fileNo, addTab(2); "COALESCE("; IIf(forGen, tupVarAh, tupVarSrcPar); "."; g_anUpdateUser; ","; IIf(forGen, tupVarAh, tupVarSrcPar); "."; g_anCreateUser; ",'-unk-'),"
     End If

     ' ps_Oid
     Print #fileNo, addTab(2); "-- ps_Oid"
     If isPsTagged Then
       Print #fileNo, addTab(2); IIf(forGen, tupVarSrcParGen, tupVarSrcPar); "."; g_anPsOid
     Else
       Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; ")"
     End If
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(1); "FROM"

     Dim qualViewOrTabNamePar As String
     Dim qualViewOrTabNameParGen As String
     If clMode = eclLrt Then
       qualViewOrTabNamePar = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, False, True, useMqtToImplementLrtForEntity)
       If forGen Then
         qualViewOrTabNameParGen = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, True, True, useMqtToImplementLrtForEntity)
       End If
     Else
       qualViewOrTabNamePar = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, False)
       If forGen Then
         qualViewOrTabNameParGen = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, True)
       End If
     End If

     Print #fileNo, addTab(2); qualSourceNlTabName; " "; tupVarSrc

     If forGen Then
       Print #fileNo, addTab(1); "INNER JOIN"
       Print #fileNo, addTab(2); qualViewOrTabNameParGen; " "; tupVarSrcParGen
       Print #fileNo, addTab(1); "ON"
       Print #fileNo, addTab(2); tupVarSrc; "."; genAttrName(conOid, ddlType, entityShortName); " = "; tupVarSrcParGen; "."; g_anOid

       If useParTab Then
         Print #fileNo, addTab(1); "INNER JOIN"
         Print #fileNo, addTab(2); qualViewOrTabNamePar; " "; tupVarSrcPar
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); tupVarSrcParGen; "."; genAttrName(conOid, ddlType, entityShortName); " = "; tupVarSrcPar; "."; g_anOid
       End If
     Else
       Print #fileNo, addTab(1); "INNER JOIN"
       Print #fileNo, addTab(2); qualViewOrTabNamePar; " "; tupVarSrcPar
       Print #fileNo, addTab(1); "ON"
       Print #fileNo, addTab(2); tupVarSrc; "."; genAttrName(conOid, ddlType, entityShortName); " = "; tupVarSrcPar; "."; g_anOid
     End If

     If referToAggHeadInChangeLog And checkAggHeadForAttrs And includeAggHeadInJoinPath Then
       Print #fileNo, addTab(1); "LEFT OUTER JOIN"
       Print #fileNo, addTab(2); qualAggHeadTabName; " "; tupVarAh
       Print #fileNo, addTab(1); "ON"
       Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhOid; " = "; tupVarAh; "."; g_anOid
     End If

     Print #fileNo, addTab(1); "LEFT OUTER JOIN"
     Print #fileNo, addTab(2); qualTargetNlTabName; " "; tupVarTgt
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anOid; " = "; tupVarTgt; "."; g_anOid

 ' ### IF IVK ###
     genCondOuterJoin fileNo, priceTargetClassIndex, priceTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, IIf(useParTab, tupVarSrcPar, tupVarSrc), tupVarAh, "PRI", priceFkAttrName, ddlType
     genCondOuterJoin fileNo, propertyTargetClassIndex, propertyTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, IIf(useParTab, tupVarSrcPar, tupVarSrc), tupVarAh, "PRP", propertyFkAttrName, ddlType
     genCondOuterJoin fileNo, propertyTypeTargetClassIndex, propertyTypeTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "PT", propertyTypeFkAttrName, ddlType
     genCondOuterJoin fileNo, divisionTargetClassIndex, divisionTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "DIV", divisionFkAttrName, ddlType
     genCondOuterJoin fileNo, bcTargetClassIndex, bcTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "BC", bcFkAttrName, ddlType, , bcReferredColumnList
     If cr132 Then
       genCondOuterJoin fileNo, endSlotTargetClassIndex, endSlotTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "BES", endSlotFkAttrName, ddlType, , "ASSIGNEDPAINTZONEKEY," & g_anSlotType
     End If

     genCondOuterJoin fileNo, assignedPzkTargetClassIndex, assignedPzkTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "BEG", assignedPzkFkAttrName, ddlType, , "ASSIGNEDPAINTZONEKEY," & g_anSlotType, assignedPzkIsGen
 
     genCondOuterJoin fileNo, s0_01TargetClassIndex, s0_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S01", s0_01FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s0_02TargetClassIndex, s0_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S02", s0_02FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s0_03TargetClassIndex, s0_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S03", s0_03FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s0_04TargetClassIndex, s0_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S04", s0_04FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s0_05TargetClassIndex, s0_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S05", s0_05FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s0_06TargetClassIndex, s0_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S06", s0_06FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s0_07TargetClassIndex, s0_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S07", s0_07FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s0_08TargetClassIndex, s0_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S08", s0_08FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s0_09TargetClassIndex, s0_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S09", s0_09FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s0_10TargetClassIndex, s0_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S10", s0_10FkAttrName, ddlType, , refColumnsNSrX

     genCondOuterJoin fileNo, s1_01TargetClassIndex, s1_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S101", s1_01FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s1_02TargetClassIndex, s1_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S102", s1_02FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s1_03TargetClassIndex, s1_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S103", s1_03FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s1_04TargetClassIndex, s1_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S104", s1_04FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s1_05TargetClassIndex, s1_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S105", s1_05FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s1_06TargetClassIndex, s1_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S106", s1_06FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s1_07TargetClassIndex, s1_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S107", s1_07FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s1_08TargetClassIndex, s1_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S108", s1_08FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s1_09TargetClassIndex, s1_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S109", s1_09FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, s1_10TargetClassIndex, s1_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "S110", s1_10FkAttrName, ddlType, , refColumnsNSrX

     genCondOuterJoin fileNo, ns1_01TargetClassIndex, ns1_01TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N101", ns1_01FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, ns1_02TargetClassIndex, ns1_02TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N102", ns1_02FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, ns1_03TargetClassIndex, ns1_03TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N103", ns1_03FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, ns1_04TargetClassIndex, ns1_04TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N104", ns1_04FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, ns1_05TargetClassIndex, ns1_05TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N105", ns1_05FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, ns1_06TargetClassIndex, ns1_06TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N106", ns1_06FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, ns1_07TargetClassIndex, ns1_07TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N107", ns1_07FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, ns1_08TargetClassIndex, ns1_08TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N108", ns1_08FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, ns1_09TargetClassIndex, ns1_09TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N109", ns1_09FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, ns1_10TargetClassIndex, ns1_10TargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "N110", ns1_10FkAttrName, ddlType, , refColumnsNSrX
     genCondOuterJoin fileNo, acTargetClassIndex, acTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "AC", acFkAttrName, ddlType
     genCondOuterJoin fileNo, dcTargetClassIndex, dcTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "DC", dcFkAttrName, ddlType
     genCondOuterJoin fileNo, csBaumusterTargetClassIndex, csBaumusterTargetClassIndexAh, clMode, thisOrgIndex, srcPoolIndex, tupVarSrcPar, tupVarAh, "CSB", csBaumusterFkAttrName, ddlType
 ' ### ENDIF IVK ###

     If clMode = eclLrt Then
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); tupVarSrc; ".LRTSTATE <> "; CStr(lrtStatusLocked)
 ' ### IF IVK ###
       If Not condenseData Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "("
         Print #fileNo, addTab(3); tupVarSrc; ".LRTSTATE <> "; CStr(lrtStatusDeleted)
         Print #fileNo, addTab(4); "OR"
         Print #fileNo, addTab(3); tupVarTgt; "."; g_anOid; " IS NOT NULL"
         Print #fileNo, addTab(2); ")"
       End If
 ' ### IF IVK ###
     End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If
 End Sub
 
 
 ' ### IF IVK ###
 Sub genMaintainChangeLogStatusDdl( _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   ByRef timeStamp As String, _
   Optional offset As Integer = 1, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional withLrtContext As Boolean = True _
 )
   If generateFwkTest Then
     Exit Sub
   End If
 
   Dim qualTabNameChangelogStatus As String
   qualTabNameChangelogStatus = genQualTabNameByClassIndex(g_classIndexChangeLogStatus, ddlType, thisOrgIndex, thisPoolIndex)
 
   genProcSectionHeader fileNo, "keep track of last update timestamp of changelog", offset
   Print #fileNo, addTab(offset + 0); "UPDATE"
   Print #fileNo, addTab(offset + 1); qualTabNameChangelogStatus; " CLS"
   Print #fileNo, addTab(offset + 0); "SET"
   Print #fileNo, addTab(offset + 1); "CLS.LASTCOMMITTIME = (CASE WHEN CLS.LASTCOMMITTIME > "; timeStamp; " THEN CLS.LASTCOMMITTIME ELSE "; timeStamp; " END)"
   Print #fileNo, addTab(offset + 0); "WHERE"
   Print #fileNo, addTab(offset + 1); "CLS.WITHLRTCONTEXT = "; IIf(withLrtContext, gc_dbTrue, gc_dbFalse)
   Print #fileNo, addTab(offset + 2); "AND"
   Print #fileNo, addTab(offset + 1); "EXISTS ("
   Print #fileNo, addTab(offset + 2); "SELECT"
   Print #fileNo, addTab(offset + 3); "1"
   Print #fileNo, addTab(offset + 2); "FROM"
   Print #fileNo, addTab(offset + 3); gc_tempTabNameChangeLogStatus; " S"
   Print #fileNo, addTab(offset + 2); "WHERE"
   Print #fileNo, addTab(offset + 3); "COALESCE(CLS."; g_anPsOid; ", -1) = COALESCE(S.psOid, -1)"
   Print #fileNo, addTab(offset + 4); "AND"
   Print #fileNo, addTab(offset + 3); "CLS.DIVISIONOID = S.divisionOid"
   Print #fileNo, addTab(offset + 1); ")"
   Print #fileNo, addTab(offset + 0); ";"
   Print #fileNo,
   Print #fileNo, addTab(offset + 0); "INSERT INTO"
   Print #fileNo, addTab(offset + 1); qualTabNameChangelogStatus
   Print #fileNo, addTab(offset + 0); "("
   Print #fileNo, addTab(offset + 1); g_anPsOid; ","
   Print #fileNo, addTab(offset + 1); "DIVISIONOID,"
   Print #fileNo, addTab(offset + 1); "LASTCOMMITTIME,"
   Print #fileNo, addTab(offset + 1); "WITHLRTCONTEXT"
   Print #fileNo, addTab(offset + 0); ")"
   Print #fileNo, addTab(offset + 0); "SELECT DISTINCT"
   Print #fileNo, addTab(offset + 1); "psOid,"
   Print #fileNo, addTab(offset + 1); "divisionOid,"
   Print #fileNo, addTab(offset + 1); timeStamp; ","
   Print #fileNo, addTab(offset + 1); IIf(withLrtContext, gc_dbTrue, gc_dbFalse)
   Print #fileNo, addTab(offset + 0); "FROM"
   Print #fileNo, addTab(offset + 1); gc_tempTabNameChangeLogStatus; " S"
   Print #fileNo, addTab(offset + 0); "WHERE"
   Print #fileNo, addTab(offset + 1); "NOT EXISTS ("
   Print #fileNo, addTab(offset + 2); "SELECT"
   Print #fileNo, addTab(offset + 3); "1"
   Print #fileNo, addTab(offset + 2); "FROM"
   Print #fileNo, addTab(offset + 3); qualTabNameChangelogStatus; " CLS"
   Print #fileNo, addTab(offset + 2); "WHERE"
   Print #fileNo, addTab(offset + 3); "COALESCE(S.psOid, -1) = COALESCE(CLS."; g_anPsOid; ", -1)"
   Print #fileNo, addTab(offset + 4); "AND"
   Print #fileNo, addTab(offset + 3); "S.divisionOid = CLS.DIVISIONOID"
   Print #fileNo, addTab(offset + 4); "AND"
   Print #fileNo, addTab(offset + 3); "CLS.WITHLRTCONTEXT = "; IIf(withLrtContext, gc_dbTrue, gc_dbFalse)
   Print #fileNo, addTab(offset + 1); ")"
   Print #fileNo, addTab(offset + 0); ";"
 End Sub
 ' ### ENDIF IVK ###
 
 Sub genChangeLogViewDdlHeader2( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef qualTargetTabName As String, _
   ByVal thisOrgIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional clMode As ChangeLogMode = eclLrt _
 )
   Dim sectionIndex As Integer
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim entityIdStr As String
   Dim isGenForming As Boolean
   Dim attrMapping() As AttributeMappingForCl
   Dim isLogChange As Boolean
   Dim useMqtToImplementLrt As Boolean
 ' ### IF IVK ###
   Dim hasNoIdentity As Boolean
   Dim condenseData As Boolean
 ' ### ENDIF IVK ###

   If acmEntityType = eactClass Then
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Class"
       isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
       isLogChange = g_classes.descriptors(acmEntityIndex).logLastChange
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       attrMapping = g_classes.descriptors(acmEntityIndex).clMapAttrsInclSubclasses
       useMqtToImplementLrt = g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
 ' ### IF IVK ###
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Relationship"
       isLogChange = g_relationships.descriptors(acmEntityIndex).logLastChange
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       isGenForming = False
       useMqtToImplementLrt = g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
 ' ### IF IVK ###
       hasNoIdentity = False
       condenseData = False
 ' ### ENDIF IVK ###
   Else
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    ChangeLog-View for entity
   ' ####################################################################################################################

   Dim transformation As AttributeListTransformation
   initAttributeTransformation transformation, 3
   setAttributeMapping transformation, 1, conCreateUser, ""
   setAttributeMapping transformation, 2, conUpdateUser, ""
   setAttributeMapping transformation, 3, conCreateTimestamp, ""

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, _
     fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, False, forGen, edomNone

   Dim viewNameSuffix As String
 ' ### IF IVK ###
   viewNameSuffix = "REDUCED"
 ' ### ELSE IVK ###
 ' viewNameSuffix = IIf(clMode = eclPubUpdate, "CORE", "")
 ' ### ENDIF IVK ###
 
   Dim qualViewName As String
   qualViewName = _
     genQualViewNameByEntityIndex( _
       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, dstPoolIndex, forGen, , , , "CL", viewNameSuffix _
     )
   printSectionHeader _
     "Reduced ChangeLog-View - non string columns - (" & genClModeDescription(clMode) & ") for table """ & qualTargetTabName & """ (ACM-" & _
     IIf(acmEntityType = eactClass, "Class", "Relationship") & """" & g_sections.descriptors(sectionIndex).sectionName & "." & entityName & """)", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewName
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "objectId,"



   ' make sure that 'LastUpdateTimeStamp' is handled as attribute
   ' guess we do not need this any more
   Dim domainIndexModTs As Integer
   If isLogChange Then
     domainIndexModTs = g_domainIndexModTimestamp
     findColumnToUse tabColumns, g_anLastUpdateTimestamp, clnAcmEntity, acmEntityType, conLastUpdateTimestamp, eavtDomain, domainIndexModTs, False, eacRegular
   End If

   ' make sure that 'validFrom' and 'validTo' are handled as attribute
   Dim domainIndexValidTs As Integer
 ' ### IF IVK ###
   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     domainIndexValidTs = g_domainIndexValTimestamp
     findColumnToUse tabColumns, g_anValidFrom, clnAcmEntity, acmEntityType, conValidFrom, eavtDomain, domainIndexValidTs, False, eacRegular
     findColumnToUse tabColumns, g_anValidTo, clnAcmEntity, acmEntityType, conValidTo, eavtDomain, domainIndexValidTs, False, eacRegular
   End If

 ' ### IF IVK ###
   If clMode <> eclPubUpdate And clMode <> eclPubMassUpdate And Not condenseData Then
 ' ### ELSE IVK ###
 ' If clMode <> eclPubUpdate Then
 ' ### ENDIF IVK ###
     Dim i As Integer
     For i = 1 To tabColumns.numDescriptors
         If isClAttrCat(tabColumns.descriptors(i).columnCategory, clMode = eclLrt) Then
           Dim attrTypeId As typeId
           Dim newValueString As String
           newValueString = ""
           attrTypeId = g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).dataType
           If attrTypeId <> etVarchar Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 2); "_t,"

             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 2); "_o,"
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 2); "_n,"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeTimeStamp) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_To,"
             newValueString = Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3) & "_Tn"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeDate) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Dto,"
             newValueString = Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3) & "_Dtn"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeInteger) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Io,"
             newValueString = Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3) & "_In"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeBigInteger) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_BIo,"
             newValueString = Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3) & "_BIn"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeDecimal) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Do,"
             newValueString = Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3) & "_Dn"
           End If

           If attrTypeMapsToClColType(attrTypeId, clValueTypeBoolean) Then
             Print #fileNo, addTab(1); Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3); "_Bo,"
             newValueString = Left(tabColumns.descriptors(i).columnName, gc_dbMaxAttributeNameLength - 3) & "_Bn"
           End If
           If tabColumns.descriptors(i).columnName <> "LASTUPDATETIMESTAMP" And newValueString <> "" Then
             newValueString = newValueString & ","
           End If
           If newValueString <> "" Then
             Print #fileNo, addTab(1); newValueString
           End If
         End If
     Next i
   End If

 ' ### IF IVK ###
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
 End Sub
 
 
 Sub genChangeLogViewDdl2( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByRef qualSourceTabName As String, _
   ByRef qualSourceGenTabName As String, _
   ByRef qualSourceNlTabName As String, _
   ByRef qualTargetTabName As String, _
   ByRef qualTargetNlTabName As String, _
   ByRef qualAggHeadTabName As String, _
   ByVal thisOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional clMode As ChangeLogMode = eclLrt _
 )
 
 If clMode <> eclSetProd Then
     Exit Sub
 End If
 
 If acmEntityIndex <> g_classIndexGenericAspect Then
     Exit Sub
 End If
 
 
 
 '0x3.V_CL_GENERICASPECT_REDUCED erzeugen: ohne jegliche String-Attribute
 
   Dim sectionIndex As Integer
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim hasOwnTable As Boolean
   Dim entityIdStr As String
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
   Dim attrMapping() As AttributeMappingForCl
   Dim relLeftClassIdStr As String
   Dim relLeftFk As String
   Dim relRightClassIdStr As String
   Dim relRightFk As String
   Dim hasNlAttributes As Boolean
   Dim isLogChange As Boolean
   Dim checkAggHeadForAttrs As Boolean
   Dim aggHeadClassIndex As Integer
   Dim isAggHead As Boolean
   Dim isAbstract As Boolean
   Dim attrMappingAh() As AttributeMappingForCl
   Dim relRefsAh As RelationshipDescriptorRefs
   Dim includeAggHeadInJoinPath As Boolean
   Dim includeGenInJoinPath As Boolean
   Dim includeAggHeadGenInJoinPath As Boolean
   Dim aggHeadReferredColumns As String
   Dim aggHeadGenReferredColumns As String
   Dim genReferredColumns As String
   Dim aggHeadSupportMqt As Boolean
   Dim useMqtToImplementLrtForEntity As Boolean
   Dim ignoreForChangelog As Boolean
 ' ### IF IVK ###
   Dim isPsTagged As Boolean
   Dim hasNoIdentity As Boolean
   Dim allowedCountriesRelIndex As Integer
   Dim disAllowedCountriesRelIndex As Integer
   Dim allowedCountriesListRelIndex As Integer
   Dim disAllowedCountriesListRelIndex As Integer
   Dim condenseData As Boolean
   Dim isNationalizable As Boolean
 ' ### ENDIF IVK ###

   includeAggHeadInJoinPath = False
   includeGenInJoinPath = False
   includeAggHeadGenInJoinPath = False
   isAggHead = False
   isAbstract = False

   If acmEntityType = eactClass Then
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Class"
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
       relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
       isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
       isLogChange = g_classes.descriptors(acmEntityIndex).logLastChange
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       hasNlAttributes = IIf(forGen, g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses)
       attrMapping = g_classes.descriptors(acmEntityIndex).clMapAttrsInclSubclasses
 ' ### IF IVK ###
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       isNationalizable = g_classes.descriptors(acmEntityIndex).isNationalizable
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       ignoreForChangelog = g_classes.descriptors(acmEntityIndex).ignoreForChangelog
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       allowedCountriesRelIndex = g_classes.descriptors(acmEntityIndex).allowedCountriesRelIndex
       disAllowedCountriesRelIndex = g_classes.descriptors(acmEntityIndex).disAllowedCountriesRelIndex
       allowedCountriesListRelIndex = g_classes.descriptors(acmEntityIndex).allowedCountriesListRelIndex
       disAllowedCountriesListRelIndex = g_classes.descriptors(acmEntityIndex).disAllowedCountriesListRelIndex
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt

       checkAggHeadForAttrs = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And ((g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex <> g_classes.descriptors(acmEntityIndex).aggHeadClassIndex) Or forGen)
       isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex)
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Relationship"
       isLogChange = g_relationships.descriptors(acmEntityIndex).logLastChange

       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       hasOwnTable = True
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       relRefs.numRefs = 0
       isGenForming = False
       hasNlAttributes = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0

       Dim reuseRelIndex As Integer
       reuseRelIndex = IIf(reuseRelationships And g_relationships.descriptors(acmEntityIndex).reusedRelIndex > 0, g_relationships.descriptors(acmEntityIndex).reusedRelIndex, acmEntityIndex)
           relLeftClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).classIdStr
           relLeftFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).shortName)
           relRightClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).classIdStr
           relRightFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).shortName)

       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       checkAggHeadForAttrs = (g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex > 0)
       ignoreForChangelog = g_relationships.descriptors(acmEntityIndex).ignoreForChangelog
 ' ### IF IVK ###
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       hasNoIdentity = False
       isNationalizable = False
       allowedCountriesRelIndex = -1
       disAllowedCountriesRelIndex = -1
       allowedCountriesListRelIndex = -1
       disAllowedCountriesListRelIndex = -1
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
       condenseData = False
 ' ### ENDIF IVK ###
   Else
     Exit Sub
   End If
 
   aggHeadSupportMqt = False
   If checkAggHeadForAttrs Then
       attrMappingAh = g_classes.descriptors(aggHeadClassIndex).clMapAttrsInclSubclasses
       relRefsAh = g_classes.descriptors(aggHeadClassIndex).relRefsRecursive
       aggHeadSupportMqt = useMqtToImplementLrt And g_classes.descriptors(aggHeadClassIndex).useMqtToImplementLrt
   End If

   If ignoreForChangelog Then
     Exit Sub
   End If

   Dim tupVarSrc As String
   Dim tupVarSrcGen As String
   Dim tupVarSrcPar As String
   Dim tupVarSrcParGen As String
   Dim tupVarTgt As String
   Dim tupVarAh As String

   If clMode = eclLrt Then
     tupVarSrc = "PRIV"
     tupVarSrcGen = "GEN"
     tupVarSrcPar = "PAR"
     tupVarSrcParGen = "PARGEN"
     tupVarTgt = "PUB"
     tupVarAh = "AH"
   ElseIf clMode = eclPubUpdate Then
     tupVarSrc = "OBJ"
     tupVarSrcGen = "OBJGEN"
     tupVarSrcPar = "PAR"
     tupVarSrcParGen = "PARGEN"
     tupVarTgt = " - no used -"
     tupVarAh = "AH"
 ' ### IF IVK ###
   ElseIf clMode = eclPubMassUpdate Then
     tupVarSrc = "OBJ"
     tupVarSrcGen = "OBJGEN"
     tupVarSrcPar = "PAR"
     tupVarSrcParGen = "PARGEN"
     tupVarTgt = " - no used -"
     tupVarAh = "AH"
 ' ### ENDIF IVK ###
   Else
     tupVarSrc = "SRC"
     tupVarSrcGen = "SRCGEN"
     tupVarSrcPar = "SRCPAR"
     tupVarSrcParGen = "SRCPARGEN"
     tupVarTgt = "TGT"
     tupVarAh = "AH"
   End If

   Dim parTabIsAhTab As Boolean
   parTabIsAhTab = (aggHeadClassIndex = acmEntityIndex) And (acmEntityType = eactClass)

   ' ####################################################################################################################
   ' #    Reduced ChangeLog-View only for GenericAspect
   ' ####################################################################################################################

   'separate some code to avoid 'Procedure too large' - errors
   genChangeLogViewDdlHeader2 acmEntityIndex, acmEntityType, qualTargetTabName, thisOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, clMode

   Dim transformation As AttributeListTransformation
   initAttributeTransformation transformation, 3
   setAttributeMapping transformation, 1, conCreateUser, ""
   setAttributeMapping transformation, 2, conUpdateUser, ""
   setAttributeMapping transformation, 3, conCreateTimestamp, ""

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, _
     fileNo, ddlType, thisOrgIndex, dstPoolIndex, 0, False, forGen, edomNone

   ' make sure that 'LastUpdateTimeStamp' is handled as attribute
   ' guess we do not need this any more
   Dim domainIndexModTs As Integer
   If isLogChange Then
     domainIndexModTs = g_domainIndexModTimestamp
     findColumnToUse tabColumns, g_anLastUpdateTimestamp, clnAcmEntity, acmEntityType, conLastUpdateTimestamp, eavtDomain, domainIndexModTs, False, eacRegular
   End If

   ' make sure that 'validFrom' and 'validTo' are handled as attribute
   Dim domainIndexValidTs As Integer
 ' ### IF IVK ###
   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     domainIndexValidTs = g_domainIndexValTimestamp
     findColumnToUse tabColumns, g_anValidFrom, clnAcmEntity, acmEntityType, conValidFrom, eavtDomain, domainIndexValidTs, False, eacRegular
     findColumnToUse tabColumns, g_anValidTo, clnAcmEntity, acmEntityType, conValidTo, eavtDomain, domainIndexValidTs, False, eacRegular
   End If


   ' objectId
   Print #fileNo, addTab(2); "-- objectId"
   Print #fileNo, addTab(2); tupVarSrc; "."; g_anOid; ","

 ' ### IF IVK ###
   If clMode <> eclPubUpdate And clMode <> eclPubMassUpdate And Not condenseData Then
 ' ### ELSE IVK ###
 ' If clMode <> eclPubUpdate Then
 ' ### ENDIF IVK ###
     Dim i As Integer
     For i = 1 To tabColumns.numDescriptors
         If isClAttrCat(tabColumns.descriptors(i).columnCategory, clMode = eclLrt) Then
           Dim attrTypeId As typeId
           attrTypeId = g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).dataType

           ' valueType
           If attrTypeId = etBigInt Or _
                attrTypeId = etDecimal Or _
                attrTypeId = etDouble Or _
                attrTypeId = etFloat Or _
                attrTypeId = etInteger Or _
                attrTypeId = etSmallint Or _
                attrTypeId = etDate Or _
                attrTypeId = etTimestamp Or _
                attrTypeId = etBoolean Then
                 Print #fileNo, addTab(2); CStr(getClColTypeByAttrType(attrTypeId)); ","
           End If

 ' ### IF IVK ###
           If (tabColumns.descriptors(i).columnCategory And eacExpression) Then
             Dim oldVal As String, newVal As String
             Dim transformationExpr As AttributeListTransformation
             initAttributeTransformation transformationExpr, 0, , , , tupVarTgt & "."
             setAttributeTransformationContext transformationExpr, thisOrgIndex, dstPoolIndex, tupVarTgt
             newVal = transformAttrName(tabColumns.descriptors(i).columnName, eavtDomain, tabColumns.descriptors(i).dbDomainIndex, transformationExpr, ddlType, , , , , tabColumns.descriptors(i).acmAttributeIndex, edomValueExpression, , , , tabColumns.descriptors(i).columnCategory)
             transformationExpr.attributePrefix = tupVarSrc & "."
             setAttributeTransformationContext transformationExpr, thisOrgIndex, srcPoolIndex, tupVarSrc, , clMode = eclLrt
             oldVal = transformAttrName(tabColumns.descriptors(i).columnName, eavtDomain, tabColumns.descriptors(i).dbDomainIndex, transformationExpr, ddlType, , , , , tabColumns.descriptors(i).acmAttributeIndex, edomValueExpression, , , , tabColumns.descriptors(i).columnCategory)

             If attrTypeId = etBoolean Then
               Print #fileNo, addTab(2); "RTRIM(CAST("; newVal; " AS VARCHAR(50))),"
               Print #fileNo, addTab(2); "RTRIM(CAST("; oldVal; " AS VARCHAR(50))),"
               Print #fileNo, addTab(2); newVal; ","
               Print #fileNo, addTab(2); oldVal; ","
             End If
           Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
             ' oldValueString / newValueString
             If attrTypeId = etBigInt Or _
                attrTypeId = etDecimal Or _
                attrTypeId = etDouble Or _
                attrTypeId = etFloat Or _
                attrTypeId = etInteger Or _
                attrTypeId = etSmallint Or _
                attrTypeId = etDate Or _
                attrTypeId = etTimestamp Or _
                attrTypeId = etBoolean Then
               Print #fileNo, addTab(2); "RTRIM(CAST("; tupVarTgt; "."; tabColumns.descriptors(i).columnName; " AS VARCHAR(50))),"
               Print #fileNo, addTab(2); "RTRIM(CAST("; tupVarSrc; "."; tabColumns.descriptors(i).columnName; " AS VARCHAR(50))),"
             End If
             Dim newValueColumn As String
             newValueColumn = ""

             ' oldValueTimestamp / newValueTimestamp
             If attrTypeMapsToClColType(attrTypeId, clValueTypeTimeStamp) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               newValueColumn = tupVarSrc & "." & tabColumns.descriptors(i).columnName
             End If

             ' oldValueDate / newValueDate
             If attrTypeMapsToClColType(attrTypeId, clValueTypeDate) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               newValueColumn = tupVarSrc & "." & tabColumns.descriptors(i).columnName
             End If

             ' oldValueInteger / newValueInteger
             If attrTypeMapsToClColType(attrTypeId, clValueTypeInteger) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               newValueColumn = tupVarSrc & "." & tabColumns.descriptors(i).columnName
             End If

             ' oldValueBigInt / newValueBigInt
             If attrTypeMapsToClColType(attrTypeId, clValueTypeBigInteger) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               newValueColumn = tupVarSrc & "." & tabColumns.descriptors(i).columnName
             End If

             ' oldValueDecimal / newValueDecimal
             If attrTypeMapsToClColType(attrTypeId, clValueTypeDecimal) Then
               Print #fileNo, addTab(2); "CAST("; tupVarTgt; "."; tabColumns.descriptors(i).columnName; " AS DECIMAL(31,10)),"
               newValueColumn = tupVarSrc & "." & tabColumns.descriptors(i).columnName
               newValueColumn = "CAST(" & newValueColumn & " AS DECIMAL(31,10))"
             End If

             ' oldValueBoolean / newValueBoolean
             If attrTypeMapsToClColType(attrTypeId, clValueTypeBoolean) Then
               Print #fileNo, addTab(2); tupVarTgt; "."; tabColumns.descriptors(i).columnName; ","
               newValueColumn = tupVarSrc & "." & tabColumns.descriptors(i).columnName
             End If

             If tabColumns.descriptors(i).columnName <> "LASTUPDATETIMESTAMP" And newValueColumn <> "" Then
                 newValueColumn = newValueColumn & ","
             End If
             If newValueColumn <> "" Then
               Print #fileNo, addTab(2); newValueColumn
             End If

 ' ### IF IVK ###
           End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
         End If
     Next i
   End If

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualSourceTabName; " "; tupVarSrc

   If referToAggHeadInChangeLog And checkAggHeadForAttrs And includeAggHeadInJoinPath Then
     Print #fileNo, addTab(1); "LEFT OUTER JOIN"

     genTabSubQueryByEntityIndex aggHeadClassIndex, eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, (clMode = eclLrt), forGen, "AH", aggHeadReferredColumns, 2, , ""

     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhOid; " = "; tupVarAh; "."; g_anOid

     If clMode = eclLrt Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "("
 ' ### IF IVK ###
       Print #fileNo, addTab(3); "(("; tupVarAh; "."; g_anIsLrtPrivate; " = 0) AND ("; tupVarAh; "."; g_anIsDeleted; " = 0) AND ("; tupVarAh; "."; g_anInLrt; " IS NULL OR "; tupVarAh; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(3); "(("; tupVarAh; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarAh; "."; g_anInLrt; " IS NULL OR "; tupVarAh; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(4); "OR"
       Print #fileNo, addTab(3); "(("; tupVarAh; "."; g_anIsLrtPrivate; " = 1) AND ("; tupVarAh; ".LRTSTATE <> "; CStr(lrtStatusDeleted); ") AND ("; tupVarAh; "."; g_anInLrt; " = PRIV."; g_anInLrt; "))"
       Print #fileNo, addTab(2); ")"
     End If
   End If

   If includeAggHeadGenInJoinPath Then
     Dim aggHeadFkAttrName As String
     aggHeadFkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(aggHeadClassIndex).shortName)
     aggHeadGenReferredColumns = aggHeadGenReferredColumns & IIf(aggHeadGenReferredColumns = "", "", ",") & aggHeadFkAttrName

     Print #fileNo, addTab(1); "LEFT OUTER JOIN"
 
     genTabSubQueryByEntityIndex aggHeadClassIndex, eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, (clMode = eclLrt), True, tupVarSrcGen, aggHeadGenReferredColumns, 2, , ""

     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anAhOid; ""; " = "; tupVarSrcGen; "."; aggHeadFkAttrName

     If clMode = eclLrt Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "("
 ' ### IF IVK ###
       Print #fileNo, addTab(3); "(("; tupVarSrcGen; "."; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anIsDeleted; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(3); "(("; tupVarSrcGen; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(4); "OR"
       Print #fileNo, addTab(3); "(("; tupVarSrcGen; "."; g_anIsLrtPrivate; " = 1) AND ("; tupVarSrcGen; ".LRTSTATE <> "; CStr(lrtStatusDeleted); ") AND ("; tupVarSrcGen; "."; g_anInLrt; " = PRIV."; g_anInLrt; "))"
       Print #fileNo, addTab(2); ")"
     End If

     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); tupVarSrcGen; ".ROWNUM = 1"
   ElseIf includeGenInJoinPath Then
     genReferredColumns = genReferredColumns & IIf(genReferredColumns = "", "", ",") & genSurrogateKeyName(ddlType, entityShortName)

     Print #fileNo, addTab(1); "LEFT OUTER JOIN"
     genTabSubQueryByEntityIndex acmEntityIndex, eactClass, fileNo, thisOrgIndex, srcPoolIndex, ddlType, clMode = eclLrt, True, tupVarSrcGen, genReferredColumns, 2, , ""

     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anOid; " = "; tupVarSrcGen; "."; genSurrogateKeyName(ddlType, entityShortName)

     If clMode = eclLrt Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "("
 ' ### IF IVK ###
       Print #fileNo, addTab(3); "(("; tupVarSrcGen; "."; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anIsDeleted; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(3); "(("; tupVarSrcGen; "." ; g_anIsLrtPrivate; " = 0) AND ("; tupVarSrcGen; "."; g_anInLrt; " IS NULL OR "; tupVarSrcGen; "."; g_anInLrt; " <> PRIV."; g_anInLrt; "))"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(4); "OR"
       Print #fileNo, addTab(3); "(("; tupVarSrcGen; "."; g_anIsLrtPrivate; " = 1) AND ("; tupVarSrcGen; ".LRTSTATE <> "; CStr(lrtStatusDeleted); ") AND ("; tupVarSrcGen; "."; g_anInLrt; " = PRIV."; g_anInLrt; "))"
       Print #fileNo, addTab(2); ")"
     End If

     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); tupVarSrcGen; ".ROWNUM = 1"
   End If

   If forGen Then
     Dim qualViewNameNonGen As String
     qualViewNameNonGen = _
       genQualViewNameByEntityIndex( _
         acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, srcPoolIndex, False, True, useMqtToImplementLrtForEntity _
       )

     Print #fileNo, addTab(1); "INNER JOIN"
     Print #fileNo, addTab(2); qualViewNameNonGen; " "; tupVarSrcPar
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; genAttrName(conOid, ddlType, entityShortName); " = "; tupVarSrcPar; "."; g_anOid
   End If

 ' ### IF IVK ###
   If clMode <> eclPubUpdate And clMode <> eclPubMassUpdate And Not condenseData Then
 ' ### ELSE IVK ###
 ' If clMode <> eclPubUpdate Then
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(1); "JOIN"

     Print #fileNo, addTab(2); qualTargetTabName; " "; tupVarTgt
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); tupVarSrc; "."; g_anOid; " = "; tupVarTgt; "."; g_anOid
   End If

 ' ### IF IVK ###


 ' ### ENDIF IVK ##

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 ' ### IF IVK ###
   If clMode = eclPubUpdate Or clMode = eclPubMassUpdate Then
 ' ### ELSE IVK ###
 ' If clMode = eclPubUpdate Then
 ' ### ENDIF IVK ###
     Exit Sub
   End If


 End Sub
 
 Private Sub printCteChangeLogStatements( _
   fileNo As Integer, _
   acmEntityType As AcmAttrContainerType, _
   qualViewName As String, _
   tabName As String, _
   qualSeqNameOid As String, _
   isGenForming As Boolean, _
   forGen As Boolean, _
   forNl As Boolean, _
   hasNoIdentity As Boolean, _
   clMode As ChangeLogMode, _
   cdUserId_in As String, _
   isPsTagged As Boolean, _
   splitCaseColumns As Boolean, _
   ByRef stringsPerType() As String, _
   ByRef valuesStringForCTE As String, _
   ByRef caseUpdateStringForCTE As String _
 )
 
   Print #fileNo,
   Print #fileNo, addTab(1); "INSERT INTO "; pc_tempTabNameChangeLogCte
   Print #fileNo, addTab(1); "WITH cte_bas"
   Print #fileNo, addTab(1); "AS ("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(2); caseUpdateStringForCTE

   Print #fileNo, addTab(3); "V.objectId"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualViewName; " V"
   If clMode = eclPubUpdate Or clMode = eclSetProd Then
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); gc_tempTabNameSpAffectedEntities; " E"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "E.oid = V.objectId"
     Print #fileNo, addTab(2); "AND"
     Print #fileNo, addTab(3); "E.opId = opId_in"
   End If
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "V.operation_Id = opId_in"
   Print #fileNo, addTab(2); "AND"
   If clMode = eclLrt Then
      Print #fileNo, addTab(3); "V.LRTOID = lrtOid_in" & vbCrLf
   ElseIf clMode = eclPubUpdate Or clMode = eclSetProd Then
      Print #fileNo, addTab(3); "V."; g_anStatus; " = "; CStr(statusReadyToBeSetProductive)
      Print #fileNo, addTab(2); "AND"
      If isPsTagged Then
         Print #fileNo, addTab(3); "V."; g_anPsOid; " = psOid_in"
      Else
         Print #fileNo, addTab(3); "V.divisionOid = v_divisionOid"
      End If
   End If
 
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "bas.objectId,"
   Print #fileNo, addTab(3); "lat.dbColumnName,"
   Print #fileNo, addTab(3); "lat.switch"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "cte_bas AS bas,"
   Print #fileNo, addTab(3); "LATERAL(VALUES"
   Print #fileNo, addTab(2); valuesStringForCTE
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "AS lat (dbColumnName, switch)"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "lat.switch = 1"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "now use cte for inserting into change log"

   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); gc_tempTabNameChangeLog
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "oid,"
   Print #fileNo, addTab(3); "entityId,"
   Print #fileNo, addTab(3); "entityType,"
   Print #fileNo, addTab(3); "ahClassId,"
   Print #fileNo, addTab(3); "ahObjectId,"
   Print #fileNo, addTab(3); "gen,"
   Print #fileNo, addTab(3); "nl,"
   Print #fileNo, addTab(3); "dbTableName,"
   Print #fileNo, addTab(3); "dbColumnName,"
   Print #fileNo, addTab(3); "objectId,"
   If acmEntityType = eactRelationship Then
     Print #fileNo, addTab(3); "refClassId1,"
     Print #fileNo, addTab(3); "refObjectId1,"
     Print #fileNo, addTab(3); "refClassId2,"
     Print #fileNo, addTab(3); "refObjectId2,"
   End If
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "price,"
   Print #fileNo, addTab(3); "propertyOid,"
   Print #fileNo, addTab(3); "propertyType_Id,"
   Print #fileNo, addTab(3); "isNational,"
   Print #fileNo, addTab(3); "csBaumuster,"
   If acmEntityType = eactClass Then
     Print #fileNo, addTab(3); "sr0Context,"
     Print #fileNo, addTab(3); "sr0Code1,"
     Print #fileNo, addTab(3); "sr0Code2,"
     Print #fileNo, addTab(3); "sr0Code3,"
     Print #fileNo, addTab(3); "sr0Code4,"
     Print #fileNo, addTab(3); "sr0Code5,"
     Print #fileNo, addTab(3); "sr0Code6,"
     Print #fileNo, addTab(3); "sr0Code7,"
     Print #fileNo, addTab(3); "sr0Code8,"
     Print #fileNo, addTab(3); "sr0Code9,"
     Print #fileNo, addTab(3); "sr0Code10,"
     Print #fileNo, addTab(3); "sr0CodeOid1,"
     Print #fileNo, addTab(3); "sr0CodeOid2,"
     Print #fileNo, addTab(3); "sr0CodeOid3,"
     Print #fileNo, addTab(3); "sr0CodeOid4,"
     Print #fileNo, addTab(3); "sr0CodeOid5,"
     Print #fileNo, addTab(3); "sr0CodeOid6,"
     Print #fileNo, addTab(3); "sr0CodeOid7,"
     Print #fileNo, addTab(3); "sr0CodeOid8,"
     Print #fileNo, addTab(3); "sr0CodeOid9,"
     Print #fileNo, addTab(3); "sr0CodeOid10,"
     Print #fileNo, addTab(3); "sr1Context,"
     Print #fileNo, addTab(3); "sr1Code1,"
     Print #fileNo, addTab(3); "sr1Code2,"
     Print #fileNo, addTab(3); "sr1Code3,"
     Print #fileNo, addTab(3); "sr1Code4,"
     Print #fileNo, addTab(3); "sr1Code5,"
     Print #fileNo, addTab(3); "sr1Code6,"
     Print #fileNo, addTab(3); "sr1Code7,"
     Print #fileNo, addTab(3); "sr1Code8,"
     Print #fileNo, addTab(3); "sr1Code9,"
     Print #fileNo, addTab(3); "sr1Code10,"
     Print #fileNo, addTab(3); "nsr1Context,"
     Print #fileNo, addTab(3); "nsr1Code1,"
     Print #fileNo, addTab(3); "nsr1Code2,"
     Print #fileNo, addTab(3); "nsr1Code3,"
     Print #fileNo, addTab(3); "nsr1Code4,"
     Print #fileNo, addTab(3); "nsr1Code5,"
     Print #fileNo, addTab(3); "nsr1Code6,"
     Print #fileNo, addTab(3); "nsr1Code7,"
     Print #fileNo, addTab(3); "nsr1Code8,"
     Print #fileNo, addTab(3); "nsr1Code9,"
     Print #fileNo, addTab(3); "nsr1Code10,"
     Print #fileNo, addTab(3); "slotPlausibilityRuleType_ID,"
     Print #fileNo, addTab(3); "witexp_oid,"
     Print #fileNo, addTab(3); "winexp_oid,"
     Print #fileNo, addTab(3); "expexp_oid,"
   End If
   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(3); "validFrom,"
     Print #fileNo, addTab(3); "validTo,"
   End If
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "baseCodeNumber,"
   Print #fileNo, addTab(3); "baseCodeType,"
   Print #fileNo, addTab(3); "codeKind_Id,"
   If cr132 Then
     Print #fileNo, addTab(3); "baseEndSlotOid,"
   End If

   Print #fileNo, addTab(3); "slotType_Id,"
   Print #fileNo, addTab(3); "aclacl_oid,"
   Print #fileNo, addTab(3); "dcldcl_oid,"
   Print #fileNo, addTab(3); "assignedPaintZoneKey,"
   Print #fileNo, addTab(3); "divisionOid,"
   Print #fileNo, addTab(3); "dpClassNumber,"
 ' ### ENDIF IVK ###

   Print #fileNo, addTab(3); "valueType_Id,"
   Print #fileNo, addTab(3); "oldValueString,"
   If Not splitCaseColumns Then
     Print #fileNo, addTab(3); "newValueString,"

     Print #fileNo, addTab(3); "oldValueDate,"
     Print #fileNo, addTab(3); "newValueDate,"

     Print #fileNo, addTab(3); "oldValueInteger,"
     Print #fileNo, addTab(3); "newValueInteger,"

     Print #fileNo, addTab(3); "oldValueBigInt,"
     Print #fileNo, addTab(3); "newValueBigInt,"

     Print #fileNo, addTab(3); "oldValueDecimal,"
     Print #fileNo, addTab(3); "newValueDecimal,"

     Print #fileNo, addTab(3); "oldValueBoolean,"
     Print #fileNo, addTab(3); "newValueBoolean,"

     Print #fileNo, addTab(3); "oldValueTimestamp,"
     Print #fileNo, addTab(3); "newValueTimestamp,"
   End If

   If clMode = eclLrt Then
     Print #fileNo, addTab(3); "lrtOid,"
   End If
   If forNl Then
     Print #fileNo, addTab(3); "languageId,"
   End If
   Print #fileNo, addTab(3); "isPerformedInMassupdate,"
   Print #fileNo, addTab(3); "operation_Id,"
   Print #fileNo, addTab(3); "opTimestamp,"
   Print #fileNo, addTab(3); "cdUserId,"
   Print #fileNo, addTab(3); "ps_Oid,"
   Print #fileNo, addTab(3); "versionId"
   Print #fileNo, addTab(2); ")"

 
   Print #fileNo, addTab(2); "SELECT"
   ' OID
   'If logRecordOid <> "" Then
    ' Print #fileNo, addTab(1); logRecordOid; ","
   'Else
     Print #fileNo, addTab(3); "NEXTVAL FOR "; qualSeqNameOid; ","
   'End If
   Print #fileNo, addTab(3); "V.entityId,"
   Print #fileNo, addTab(3); "V.entityType,"
   Print #fileNo, addTab(3); "V.ahClassId,"
   Print #fileNo, addTab(3); "V.ahObjectId,"
   Print #fileNo, addTab(3); "V.gen,"
   Print #fileNo, addTab(3); "V.nl,"
   Print #fileNo, addTab(3); "V.dbTableName,"
   Print #fileNo, addTab(3); "bas.dbColumnName,"
   Print #fileNo, addTab(3); "V.objectId,"

   If acmEntityType = eactRelationship Then
     Print #fileNo, addTab(3); "V.refClassId1,"
     Print #fileNo, addTab(3); "V.refObjectId1,"
     Print #fileNo, addTab(3); "V.refClassId2,"
     Print #fileNo, addTab(3); "V.refObjectId2,"
   End If

 ' ### IF IVK ###
   Print #fileNo, addTab(3); "V.price,"
   Print #fileNo, addTab(3); "V.propertyOid,"
   Print #fileNo, addTab(3); "V.propertyType_ID,"
   Print #fileNo, addTab(3); "COALESCE(V.isNational, "; gc_dbFalse; "),"
   Print #fileNo, addTab(3); "V.csBaumuster,"

   If acmEntityType = eactClass Then
     Print #fileNo, addTab(3); "V.sr0Context,"
     Print #fileNo, addTab(3); "V.sr0Code1,"
     Print #fileNo, addTab(3); "V.sr0Code2,"
     Print #fileNo, addTab(3); "V.sr0Code3,"
     Print #fileNo, addTab(3); "V.sr0Code4,"
     Print #fileNo, addTab(3); "V.sr0Code5,"
     Print #fileNo, addTab(3); "V.sr0Code6,"
     Print #fileNo, addTab(3); "V.sr0Code7,"
     Print #fileNo, addTab(3); "V.sr0Code8,"
     Print #fileNo, addTab(3); "V.sr0Code9,"
     Print #fileNo, addTab(3); "V.sr0Code10,"
     Print #fileNo, addTab(3); "V.sr0CodeOid1,"
     Print #fileNo, addTab(3); "V.sr0CodeOid2,"
     Print #fileNo, addTab(3); "V.sr0CodeOid3,"
     Print #fileNo, addTab(3); "V.sr0CodeOid4,"
     Print #fileNo, addTab(3); "V.sr0CodeOid5,"
     Print #fileNo, addTab(3); "V.sr0CodeOid6,"
     Print #fileNo, addTab(3); "V.sr0CodeOid7,"
     Print #fileNo, addTab(3); "V.sr0CodeOid8,"
     Print #fileNo, addTab(3); "V.sr0CodeOid9,"
     Print #fileNo, addTab(3); "V.sr0CodeOid10,"
     Print #fileNo, addTab(3); "V.sr1Context,"
     Print #fileNo, addTab(3); "V.sr1Code1,"
     Print #fileNo, addTab(3); "V.sr1Code2,"
     Print #fileNo, addTab(3); "V.sr1Code3,"
     Print #fileNo, addTab(3); "V.sr1Code4,"
     Print #fileNo, addTab(3); "V.sr1Code5,"
     Print #fileNo, addTab(3); "V.sr1Code6,"
     Print #fileNo, addTab(3); "V.sr1Code7,"
     Print #fileNo, addTab(3); "V.sr1Code8,"
     Print #fileNo, addTab(3); "V.sr1Code9,"
     Print #fileNo, addTab(3); "V.sr1Code10,"
     Print #fileNo, addTab(3); "V.nsr1Context,"
     Print #fileNo, addTab(3); "V.nsr1Code1,"
     Print #fileNo, addTab(3); "V.nsr1Code2,"
     Print #fileNo, addTab(3); "V.nsr1Code3,"
     Print #fileNo, addTab(3); "V.nsr1Code4,"
     Print #fileNo, addTab(3); "V.nsr1Code5,"
     Print #fileNo, addTab(3); "V.nsr1Code6,"
     Print #fileNo, addTab(3); "V.nsr1Code7,"
     Print #fileNo, addTab(3); "V.nsr1Code8,"
     Print #fileNo, addTab(3); "V.nsr1Code9,"
     Print #fileNo, addTab(3); "V.nsr1Code10,"

     Print #fileNo, addTab(3); "V.slotPlausibilityRuleType_ID,"
     Print #fileNo, addTab(3); "V.witexp_oid,"
     Print #fileNo, addTab(3); "V.winexp_oid,"
     Print #fileNo, addTab(3); "V.expexp_oid,"
   End If

   If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 ' If isGenForming And forGen Then
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(3); "V."; g_anValidFrom; ","
     Print #fileNo, addTab(3); "V."; g_anValidTo; ","
   End If

 ' ### IF IVK ###
   Print #fileNo, addTab(3); "V.baseCodeNumber,"
   Print #fileNo, addTab(3); "V.baseCodeType,"
   Print #fileNo, addTab(3); "V.codeKind_id,"
   If cr132 Then
     Print #fileNo, addTab(3); "V.baseEndSlotOid,"
   End If
   Print #fileNo, addTab(3); "V.slotType_Id,"
   Print #fileNo, addTab(3); "V.aclacl_oid,"
   Print #fileNo, addTab(3); "V.dcldcl_oid,"
   Print #fileNo, addTab(3); "V.assignedPaintZoneKey,"
   Print #fileNo, addTab(3); "V.divisionOid,"
   Print #fileNo, addTab(3); "V.dpClassNumber,"
 ' ### ENDIF IVK ###

   ' finalize the built strings
   Dim endSuffix As String
   endSuffix = "      END AS wert"
   stringsPerType(1) = stringsPerType(1) & "    ELSE NULL" & vbCrLf & endSuffix & "_t,"
   stringsPerType(2) = stringsPerType(2) & "    ELSE CAST(NULL AS VARCHAR(4000))" & vbCrLf & endSuffix & "_o,"
   stringsPerType(3) = stringsPerType(3) & "    ELSE CAST(NULL AS VARCHAR(4000))" & vbCrLf & endSuffix & "_n"
   stringsPerType(4) = stringsPerType(4) & "    ELSE CAST(NULL AS DATE)" & vbCrLf & endSuffix & "_Dto,"
   stringsPerType(5) = stringsPerType(5) & "    ELSE CAST(NULL AS DATE)" & vbCrLf & endSuffix & "_Dtn,"
   stringsPerType(6) = stringsPerType(6) & "    ELSE CAST(NULL AS INTEGER)" & vbCrLf & endSuffix & "_Io,"
   stringsPerType(7) = stringsPerType(7) & "    ELSE CAST(NULL AS INTEGER)" & vbCrLf & endSuffix & "_In,"
   stringsPerType(8) = stringsPerType(8) & "    ELSE CAST(NULL AS BIGINT)" & vbCrLf & endSuffix & "_BIo,"
   stringsPerType(9) = stringsPerType(9) & "    ELSE CAST(NULL AS BIGINT)" & vbCrLf & endSuffix & "_BIn,"
   stringsPerType(10) = stringsPerType(10) & "    ELSE CAST(NULL AS DECIMAL)" & vbCrLf & endSuffix & "_Do,"
   stringsPerType(11) = stringsPerType(11) & "    ELSE CAST(NULL AS DECIMAL)" & vbCrLf & endSuffix & "_Dn,"
   stringsPerType(12) = stringsPerType(12) & "    ELSE CAST(NULL AS SMALLINT)" & vbCrLf & endSuffix & "_Bo,"
   stringsPerType(13) = stringsPerType(13) & "    ELSE CAST(NULL AS SMALLINT)" & vbCrLf & endSuffix & "_Bn,"
   stringsPerType(14) = stringsPerType(14) & "    ELSE CAST(NULL AS TIMESTAMP)" & vbCrLf & endSuffix & "_To,"
   stringsPerType(15) = stringsPerType(15) & "    ELSE CAST(NULL AS TIMESTAMP)" & vbCrLf & endSuffix & "_Tn"
   If Not splitCaseColumns Then
     stringsPerType(3) = stringsPerType(3) & ","
     stringsPerType(15) = stringsPerType(15) & ","
   End If
   Dim i As Integer
   Dim limit As Integer
   If splitCaseColumns Then
     limit = 2
   Else
     limit = 15
   End If

   For i = 1 To limit
     Print #fileNo, addTab(3); stringsPerType(i)
   Next i

   If clMode = eclLrt Then
     Print #fileNo, addTab(3); "lrtOid_in,"
   End If
   If forNl Then
     Print #fileNo, addTab(3); "V.LANGUAGE_ID,"
   End If
   Print #fileNo, addTab(3); "V.isPerformedInMassupdate,"
   Print #fileNo, addTab(3); "V.operation_Id,"

   ' opTimestamp
   If clMode = eclPubUpdate Then
     Print #fileNo, addTab(3); "CURRENT TIMESTAMP,"
 ' ### IF IVK ###
   ElseIf clMode = eclPubMassUpdate Then
     Print #fileNo, addTab(3); "v_currentTimestamp,"
 ' ### ENDIF IVK ###
   Else
     Print #fileNo, addTab(3); "commitTs_in,"
   End If
   ' cdUserId
   Print #fileNo, addTab(3); cdUserId_in; ","
   Print #fileNo, addTab(3); "V.ps_Oid,"

   ' versionId"
   Print #fileNo, addTab(3); "1"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualViewName; " AS V, "
   Print #fileNo, addTab(3); pc_tempTabNameChangeLogCte; " AS bas"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "bas.switch = 1"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "bas.objectId = V.objectId"
   If isPsTagged Then
     Print #fileNo, addTab(2); "AND"
     Print #fileNo, addTab(3); "V."; g_anPsOid; " = psOid_in"
   End If
   Print #fileNo, addTab(2); ";"
 

   If splitCaseColumns Then
   'additional update statements

   ' update for newValueString
     Print #fileNo,
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); gc_tempTabNameChangeLog; " bas"
     Print #fileNo, addTab(2); "SET"
     Print #fileNo, addTab(3); "newValueString"
     Print #fileNo, addTab(2); "="
     Print #fileNo, addTab(2); "(SELECT"
     Print #fileNo, addTab(3); stringsPerType(3)
 
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); tabName; " AS V "
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "bas.objectId = V.oid"
     If isPsTagged Then
         Print #fileNo, addTab(2); "AND"
         Print #fileNo, addTab(3); "V."; g_anPsOid; " = psOid_in"
     End If
     Print #fileNo, addTab(2); ");"

   'update the rest
     qualViewName = qualViewName & "_REDUCED"

     Print #fileNo,
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); gc_tempTabNameChangeLog; " bas"
     Print #fileNo, addTab(2); "SET("

     Print #fileNo, addTab(3); "oldValueDate,"
     Print #fileNo, addTab(3); "newValueDate,"

     Print #fileNo, addTab(3); "oldValueInteger,"
     Print #fileNo, addTab(3); "newValueInteger,"

     Print #fileNo, addTab(3); "oldValueBigInt,"
     Print #fileNo, addTab(3); "newValueBigInt,"

     Print #fileNo, addTab(3); "oldValueDecimal,"
     Print #fileNo, addTab(3); "newValueDecimal,"

     Print #fileNo, addTab(3); "oldValueBoolean,"
     Print #fileNo, addTab(3); "newValueBoolean,"

     Print #fileNo, addTab(3); "oldValueTimestamp,"
     Print #fileNo, addTab(3); "newValueTimestamp"

     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "="
     Print #fileNo, addTab(2); "(SELECT"

     For i = 4 To 15
         Print #fileNo, addTab(3); stringsPerType(i)
     Next i
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualViewName; " AS V "
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "bas.objectId = V.objectId"
     Print #fileNo, addTab(2); ");"
   End If


 End Sub
 
 
 Sub genGenChangeLogRecordForCTEDdl( _
   ByRef opDescription As String, _
   fileNo As Integer, _
   ByRef stringsPerType() As String, _
   ByRef valuesStringForCTE As String, _
   ByRef caseStringForCTE As String, _
   splitVar As Boolean, _
   exprTabName As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef dbColumnName As String = "", _
   Optional ByRef dbColumnNameAlternative As String = "", _
   Optional dbColumnType As typeId = etNone, _
   Optional clMode As ChangeLogMode = eclLrt, _
   Optional columnCategory As AttrCategory = eacRegular, _
   Optional ByRef valueNew As String = "", _
   Optional columnIsNullable As Boolean = False _
 )

   Dim oldValue As String
   Dim newValue As String
   Dim usedColumnName As String
   Dim countryString As String
   Dim originalDbColumnAlternative As String
   Dim exprString As String

   countryString = ""
   exprString = ""

   originalDbColumnAlternative = dbColumnNameAlternative
   'special case for STATUS_ID
   If dbColumnName = g_anStatus Then
     oldValue = "V." & dbColumnName & "_Io"
   Else
     oldValue = "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 2) & "_o"
   End If

   If valueNew <> "" Then
     newValue = valueNew
   Else
     newValue = "V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 2) & "_n"
   End If
   usedColumnName = dbColumnName
   If dbColumnName <> "" Then

     If dbColumnNameAlternative <> "" Then
       If columnCategory And eacNational Then
         dbColumnNameAlternative = genAttrName(dbColumnNameAlternative, ddlType, , , , , True)
       ElseIf columnCategory And eacNationalBool Then
         dbColumnNameAlternative = UCase(dbColumnNameAlternative & gc_anSuffixNatActivated)
         usedColumnName = dbColumnNameAlternative
       End If
     End If

     If isClAttrCat(columnCategory, (clMode = eclLrt) Or (clMode = eclPubUpdate) Or (clMode = eclPubMassUpdate)) Then
 
       caseStringForCTE = caseStringForCTE & vbTab & "-- " & opDescription & vbCrLf
       caseStringForCTE = caseStringForCTE & vbTab & vbTab & "CASE" & vbCrLf
       caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "WHEN" & vbCrLf & vbTab & vbTab & vbTab & vbTab
       If ((columnCategory And eacExpression) <> 0 And ((columnCategory And eacNationalBool) = 0)) Then
         caseStringForCTE = caseStringForCTE & " NOT (" & oldValue & " IS NULL AND " & newValue & " IS NULL)" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "AND" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & oldValue & " IS NULL)" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "OR" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & newValue & " IS NULL)" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "OR" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & oldValue & " <> " & newValue & ")" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & ")" & vbCrLf

         oldValue = "V." & Left(originalDbColumnAlternative, gc_dbMaxAttributeNameLength - 3) & "_BIo"
         Dim newValueExp As String
         newValueExp = "V." & Left(originalDbColumnAlternative, gc_dbMaxAttributeNameLength - 3) & "_BIn"
         usedColumnName = dbColumnNameAlternative

         If splitVar Then
             exprString = "(SELECT CAST(RTRIM(LEFT(X.TERMSTRING,750)) AS VARCHAR(750)) FROM " & exprTabName & " X WHERE X.OID = V." & usedColumnName & ")"
         End If

         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "OR" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "NOT (" & oldValue & " IS NULL AND " & newValueExp & " IS NULL)" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "AND" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & oldValue & " IS NULL)" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "OR" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & newValueExp & " IS NULL)" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "OR" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & oldValue & " <> " & newValueExp & ")" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & ")" & vbCrLf
       ElseIf (columnCategory And eacFkOid) Then
         oldValue = "V." & dbColumnName & "_BIo"
         newValue = "V." & dbColumnName & "_BIn"
         caseStringForCTE = caseStringForCTE & " NOT (" & oldValue & " IS NULL AND " & newValue & " IS NULL)" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "AND" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & oldValue & " IS NULL)" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "OR" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & newValue & " IS NULL)" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "OR" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & oldValue & " <> " & newValue & ")" & vbCrLf
         caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & ")" & vbCrLf
         If (isClAttrCat(columnCategory, clMode = eclLrt) And resolveCountryIdListInChangeLog And ((columnCategory And eacFkCountryIdList) <> 0)) Then
             countryString = "(SELECT IDLIST FROM " & g_qualTabNameCountryIdList & " WHERE OID = V." & dbColumnName
         End If
       Else
         If columnIsNullable Then
             caseStringForCTE = caseStringForCTE & " NOT (" & oldValue & " IS NULL AND " & newValue & " IS NULL)" & vbCrLf
             caseStringForCTE = caseStringForCTE & vbTab & vbTab & "AND" & vbCrLf
             caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & vbCrLf
             caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & oldValue & " IS NULL)" & vbCrLf
             caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "OR" & vbCrLf
             caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & newValue & " IS NULL)" & vbCrLf
             caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "OR" & vbCrLf
             caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "(" & oldValue & " <> " & newValue & ")" & vbCrLf
             caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & ")" & vbCrLf
           Else
             caseStringForCTE = caseStringForCTE & oldValue & " <> " & newValue & vbCrLf
           End If
       End If

       valuesStringForCTE = valuesStringForCTE & vbTab & vbTab & "('" & usedColumnName & "', bas." & usedColumnName & ")," & vbCrLf

       Dim prefix As String
       Dim suffix As String

       prefix = "    WHEN '" & usedColumnName & "' THEN V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 2)
       suffix = vbCrLf & "    "
       'special case for VAL_EXP_OID_t
       If columnCategory And eacNational And originalDbColumnAlternative <> "" Then
         stringsPerType(1) = stringsPerType(1) & "    WHEN '" & usedColumnName & "' THEN V." & Left(originalDbColumnAlternative, gc_dbMaxAttributeNameLength - 2) & "_t" & suffix
       Else
         stringsPerType(1) = stringsPerType(1) & "    WHEN '" & usedColumnName & "' THEN V." & Left(usedColumnName, gc_dbMaxAttributeNameLength - 2) & "_t" & suffix
       End If

       Dim isFk As Boolean
       Dim isFkCountry As Boolean

       isFk = (columnCategory And eacFkOid) <> 0
       isFkCountry = (columnCategory And eacFkCountryIdList) <> 0
       If (isFk And Not isFkCountry) Then
         ' omit these entries
       Else
         'special case for ACLACL_OID, DCLDCL_OID
         If countryString <> "" Then
             stringsPerType(2) = stringsPerType(2) & "    WHEN '" & usedColumnName & "' THEN " & countryString & "_o)" & suffix
         Else
             stringsPerType(2) = stringsPerType(2) & prefix & "_o" & suffix
         End If
         'special case for STATUS_ID
         If (usedColumnName = g_anStatus) Then
             stringsPerType(3) = stringsPerType(3) & "    WHEN '" & usedColumnName & "' THEN CAST(RTRIM(CAST(" & newValue & " AS CHAR(254))) AS VARCHAR(4000))" & suffix
         Else
                 'special case for ACLACL_OID, DCLDCL_OID
             If countryString <> "" Then
                 stringsPerType(3) = stringsPerType(3) & "    WHEN '" & usedColumnName & "' THEN " & countryString
                 'no _n
                 If splitVar Then
                   stringsPerType(3) = stringsPerType(3) & ")" & suffix
                 Else
                   stringsPerType(3) = stringsPerType(3) & "_n)" & suffix
                 End If
             Else
                 If splitVar Then
                         ' special case for expressions
                     If exprString <> "" Then
                       stringsPerType(3) = stringsPerType(3) & "    WHEN '" & usedColumnName & "' THEN " & exprString & suffix
                     Else
                         stringsPerType(3) = stringsPerType(3) & "    WHEN '" & usedColumnName & "' THEN CAST(V." & usedColumnName & " AS VARCHAR(4000))" & suffix
                     End If
                 Else
                     stringsPerType(3) = stringsPerType(3) & "    WHEN '" & usedColumnName & "' THEN " & newValue & suffix
                 End If
             End If
         End If
       End If
       prefix = "    WHEN '" & usedColumnName & "' THEN V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 3)
       If attrTypeMapsToClColType(dbColumnType, clValueTypeDate) Then
         'Views have column length 31
         'prefix = "    WHEN '" & usedColumnName & "' THEN V." & Left(dbColumnName, gc_dbMaxAttributeNameLength - 4)
         stringsPerType(4) = stringsPerType(4) & prefix & "_Dto" & suffix
         stringsPerType(5) = stringsPerType(5) & prefix & "_Dtn" & suffix
       ElseIf attrTypeMapsToClColType(dbColumnType, clValueTypeInteger) Then
         stringsPerType(6) = stringsPerType(6) & prefix & "_Io" & suffix
         'special case for STATUS_ID
         If (usedColumnName = g_anStatus) Then
             stringsPerType(7) = stringsPerType(7) & "    WHEN '" & usedColumnName & "' THEN INTEGER(" & newValue & ")" & suffix
         Else
             stringsPerType(7) = stringsPerType(7) & prefix & "_In" & suffix
         End If
       ElseIf attrTypeMapsToClColType(dbColumnType, clValueTypeBigInteger) Or (dbColumnNameAlternative = usedColumnName And Not (Len(usedColumnName) > 12 And Right(usedColumnName, 12) = "_ISNATACTIVE")) Then
         Dim prefixForBI As String
         ' special case special case for VAL_EXP_OID
         If columnCategory And eacNational And originalDbColumnAlternative <> "" Then
           prefixForBI = "    WHEN '" & usedColumnName & "' THEN V." & Left(originalDbColumnAlternative, gc_dbMaxAttributeNameLength - 4)
          'special case expressions
         ElseIf dbColumnNameAlternative = usedColumnName Then
           prefixForBI = "    WHEN '" & usedColumnName & "' THEN V." & Left(usedColumnName, gc_dbMaxAttributeNameLength - 4)
         Else
           prefixForBI = prefix
         End If
         stringsPerType(8) = stringsPerType(8) & prefixForBI & "_BIo" & suffix
         stringsPerType(9) = stringsPerType(9) & prefixForBI & "_BIn" & suffix
       ElseIf attrTypeMapsToClColType(dbColumnType, clValueTypeDecimal) Then
         stringsPerType(10) = stringsPerType(10) & prefix & "_Do" & suffix
         stringsPerType(11) = stringsPerType(11) & prefix & "_Dn" & suffix
       ElseIf attrTypeMapsToClColType(dbColumnType, clValueTypeBoolean) Then
         stringsPerType(12) = stringsPerType(12) & prefix & "_Bo" & suffix
         stringsPerType(13) = stringsPerType(13) & prefix & "_Bn" & suffix
       ElseIf attrTypeMapsToClColType(dbColumnType, clValueTypeTimeStamp) Then
         stringsPerType(14) = stringsPerType(14) & prefix & "_To" & suffix
         stringsPerType(15) = stringsPerType(15) & prefix & "_Tn" & suffix
       End If
     End If

     caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "THEN" & vbCrLf
     caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & vbTab & "1" & vbCrLf
     caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & "ELSE" & vbCrLf
     caseStringForCTE = caseStringForCTE & vbTab & vbTab & vbTab & vbTab & "0" & vbCrLf
     caseStringForCTE = caseStringForCTE & vbTab & vbTab & "END AS " & usedColumnName & "," & vbCrLf
   End If

 End Sub
 
 
