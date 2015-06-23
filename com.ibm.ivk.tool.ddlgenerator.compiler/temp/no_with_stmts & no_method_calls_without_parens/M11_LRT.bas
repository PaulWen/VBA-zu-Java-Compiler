 Attribute VB_Name = "M11_LRT"
 
 Option Explicit
 
 Private Const pc_tempTabNamePubOidsAffected = "SESSION.PubOidsAffected"
 Private Const pc_tempTabNamePubOidsAffectedNl = "SESSION.PubOidsAffectedNl"
 
 Global Const lrtStatusLocked = 0
 Global Const lrtStatusCreated = 1
 Global Const lrtStatusUpdated = 2
 Global Const lrtStatusDeleted = 3
 Global Const lrtStatusMassDeleted = 4
 Global Const lrtStatusNonLrtCreated = 5
 
 Global Const workingStateUnlocked = 1
 Global Const workingLockedByOtherUser = 2
 Global Const workingLockedInActiveTransaction = 3
 Global Const workingLockedInInactiveTransaction = 4
 
 Private Const processingStep = 1
 Private Const attrListAlign = 40
 
 Global Const tempTabNameLrtLog = "SESSION.LrtLog"
 
 Sub genLrtSupportDdl( _
   ddlType As DdlTypeId _
 )
   Dim i As Integer
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If ddlType = edtLdm Then
 ' ### IF IVK ###
     genLrtSupportDdlByType(edtLdm)
 
 ' ### ENDIF IVK ###
     genLrtSupportDdlByPool()
 ' ### IF IVK ###
     genLrtSpSupportDdlByPool()
 ' ### ENDIF IVK ###
   ElseIf ddlType = edtPdm Then
 ' ### IF IVK ###
     genLrtSupportDdlByType(edtPdm)
 ' ### ENDIF IVK ###

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(thisPoolIndex).supportLrt Then
         For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
             genLrtSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
 ' ### IF IVK ###
             genLrtSpSupportDdlByPool(thisOrgIndex, thisPoolIndex, edtPdm)
 ' ### ENDIF IVK ###
           End If
          Next thisOrgIndex
        End If
      Next thisPoolIndex
   End If
 End Sub
 ' ### IF IVK ###
 
 
 Private Sub genLrtSupportDdlByType( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not g_genLrtSupport Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, , , , phaseLrt, ldmIterationPoolSpecific)
 
   ' ####################################################################################################################
   ' #    create user defined function determining target status of records after LRT-Commit
   ' ####################################################################################################################

   printSectionHeader("UDF for determining target status of records after LRT-Commit", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); g_qualFuncNameGetLrtTargetStatus
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "entityId_in", g_dbtEntityId, True, "ACM entity ID")
   genProcParm(fileNo, "", "entityType_in", g_dbtEntityType, True, "ACM entity type")
   genProcParm(fileNo, "", "settingManActCP_in", g_dbtBoolean, True, "setting 'manuallyActivateCodePrice'")
   genProcParm(fileNo, "", "settingManActTP_in", g_dbtBoolean, True, "setting 'manuallyActivateTypePrice'")
   genProcParm(fileNo, "", "settingManActSE_in", g_dbtBoolean, True, "setting 'manuallyActivateStandardEquipmentPrice'")
   genProcParm(fileNo, "", "settingSelRelease_in", g_dbtBoolean, False, "setting 'useSelectiveReleaseProcess'")
   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtEnumId
   Print #fileNo, addTab(0); "BEGIN ATOMIC"

   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_lrtActivationType", "VARCHAR(2)", "NULL")

   genProcSectionHeader(fileNo, "determine LRT activation type (CP,TP,SE,GA,DT,NP,NULL)")
   Print #fileNo, addTab(1); "SET v_lrtActivationType ="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); g_anAcmLrtActivationType
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); g_anAcmEntityId; " = entityId_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); g_anAcmEntityType; " = entityType_in"
   Print #fileNo, addTab(2); ");"

   genProcSectionHeader(fileNo, "return result")
   Print #fileNo, addTab(1); "RETURN"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE v_lrtActivationType"

   Print #fileNo, addTab(4); "WHEN 'CP' THEN"
   genProcSectionHeader(fileNo, "target status for 'Code Price'", 5, True)

   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "CASE"
   Print #fileNo, addTab(7); "WHEN settingManActCP_in = 1 THEN "; CStr(statusReadyForActivation)
   Print #fileNo, addTab(7); "WHEN settingSelRelease_in = 1 THEN "; CStr(statusReadyForRelease)
   Print #fileNo, addTab(7); "ELSE "; CStr(statusReadyToBeSetProductive)
   Print #fileNo, addTab(6); "END"
   Print #fileNo, addTab(5); ")"

   Print #fileNo, addTab(4); "WHEN 'TP' THEN"
   genProcSectionHeader(fileNo, "target status for 'Type Price'", 5, True)

   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "CASE"
   Print #fileNo, addTab(7); "WHEN settingManActTP_in = 1 THEN "; CStr(statusReadyForActivation)
   Print #fileNo, addTab(7); "WHEN settingSelRelease_in = 1 THEN "; CStr(statusReadyForRelease)
   Print #fileNo, addTab(7); "ELSE "; CStr(statusReadyToBeSetProductive)
   Print #fileNo, addTab(6); "END"
   Print #fileNo, addTab(5); ")"

   Print #fileNo, addTab(4); "WHEN 'SE' THEN"
   genProcSectionHeader(fileNo, "target status for 'Standard Equipment'", 5, True)

   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "CASE"
   Print #fileNo, addTab(7); "WHEN settingManActSE_in = 1 THEN "; CStr(statusReadyForActivation)
   Print #fileNo, addTab(7); "WHEN settingSelRelease_in = 1 THEN "; CStr(statusReadyForRelease)
   Print #fileNo, addTab(7); "ELSE "; CStr(statusReadyToBeSetProductive)
   Print #fileNo, addTab(6); "END"
   Print #fileNo, addTab(5); ")"

   Print #fileNo, addTab(4); "WHEN 'GA' THEN"
   genProcSectionHeader(fileNo, "target status for 'GenericAspect'", 5, True)

   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "CASE"
   Print #fileNo, addTab(7); "WHEN settingSelRelease_in = 1 THEN "; CStr(statusReadyForRelease)
   Print #fileNo, addTab(7); "ELSE "; CStr(statusReadyToBeSetProductive)
   Print #fileNo, addTab(6); "END"
   Print #fileNo, addTab(5); ")"

   Print #fileNo, addTab(4); "WHEN 'DT' THEN"
   genProcSectionHeader(fileNo, "target status for 'DecisionTable'", 5, True)

   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "CASE"
   Print #fileNo, addTab(7); "WHEN settingSelRelease_in = 1 THEN "; CStr(statusReadyForRelease)
   Print #fileNo, addTab(7); "ELSE "; CStr(statusReadyToBeSetProductive)
   Print #fileNo, addTab(6); "END"
   Print #fileNo, addTab(5); ")"
 
   Print #fileNo, addTab(4); "WHEN 'NP' THEN"
   genProcSectionHeader(fileNo, "target status for 'no transfer to production'", 5, True)

   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); CStr(statusWorkInProgress)
   Print #fileNo, addTab(5); ")"

   Print #fileNo, addTab(4); "ELSE "; CStr(statusReadyToBeSetProductive)

   Print #fileNo, addTab(3); "END"
   Print #fileNo, addTab(2); ");"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Sub genDdlForTempPrivClassIdOid( _
   fileNo As Integer _
 )
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.PRIVCLASSIDOID"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "CLASSID CHAR(5),"
   Print #fileNo, addTab(2); "OID BIGINT NOT NULL"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "ON COMMIT PRESERVE ROWS"
   Print #fileNo, addTab(1); "NOT LOGGED;"
 
 End Sub
 
 
 Sub genDdlPdmEntityCheck( _
   fileNo As Integer, _
   indent As Integer, _
   prefix As String _
 )
     Print #fileNo, addTab(indent); prefix; "ENTITY_TYPE = '"; gc_acmEntityTypeKeyClass; "'"
     Print #fileNo, addTab(indent + 1); "AND"
     Print #fileNo, addTab(indent); prefix; "ENTITY_ISLRT = "; gc_dbTrue
     Print #fileNo, addTab(indent + 1); "AND"
     Print #fileNo, addTab(indent); prefix; "LDM_ISGEN = "; gc_dbFalse
     Print #fileNo, addTab(indent + 1); "AND"
     Print #fileNo, addTab(indent); prefix; "LDM_ISLRT = "; gc_dbFalse
     Print #fileNo, addTab(indent + 1); "AND"
     Print #fileNo, addTab(indent); prefix; "LDM_ISNL = "; gc_dbFalse

 End Sub
 
 Sub genDdlPsDivClause( _
   fileNo As Integer, _
   indent As Integer, _
   prefixLeft As String, _
   ByVal prefixRightPs As String, _
   ByVal prefixRightDiv As String, _
   isPsTagged As Boolean, _
   usePsTagInNlTextTables As Boolean, _
   forNl As Boolean, _
   useDivOidWhereClause As Boolean, _
   useDivRelKey As Boolean, _
   Optional useForAggHeadJoin As Boolean _
 )
         If prefixLeft <> "" Then
           prefixLeft = prefixLeft + "."
         End If
         If prefixRightPs <> "" Then
           prefixRightPs = prefixRightPs + "."
         End If
         If prefixRightDiv <> "" Then
           prefixRightDiv = prefixRightDiv + "."
         End If
         If isPsTagged And (usePsTagInNlTextTables Or Not forNl) Then
           Print #fileNo, addTab(indent + 1); "AND"
           If prefixRightPs = "" Then
             Print #fileNo, addTab(indent); prefixLeft; conPsOid; " = v_psOid"
           Else
             Print #fileNo, addTab(indent); prefixLeft; conPsOid; " = "; prefixRightPs; conPsOid
           End If
         ElseIf useDivOidWhereClause Then
           Print #fileNo, addTab(indent + 1); "AND"
           If useDivRelKey Then
             Print #fileNo, addTab(indent); prefixLeft; "CDIDIV_OID = "; prefixRightDiv; "CDIDIV_OID"
           ElseIf useForAggHeadJoin Then
             Print #fileNo, addTab(indent); prefixLeft; "CDIDIV_OID = "; prefixRightDiv; conDivOid
           Else
             Print #fileNo, addTab(indent); prefixLeft; conDivOid; " = "; prefixRightDiv; conDivOid
           End If
         End If
 End Sub
 
 Sub genStatusCheckDdl( _
   fileNo As Integer, _
   ByRef recordVar As String, _
   Optional ByRef statusAttr As String = "STATUS_ID", _
   Optional indent As Integer = 1 _
 )
   If Not generateStatusCheckDdl Then
     Exit Sub
   End If

   genProcSectionHeader(fileNo, "verify that new status is supported by the MDS status concept", indent)
   Print #fileNo, addTab(indent); "IF (("; recordVar; "."; statusAttr; " IS NOT NULL) AND ("; recordVar; "."; statusAttr; " NOT IN ("; _
                                   CStr(statusWorkInProgress); ", "; CStr(statusReadyForActivation); ", "; _
                                   CStr(statusReadyForRelease); ", "; CStr(statusReadyToBeSetProductive); "))) THEN"
   genSignalDdl("attrVal4", fileNo, indent + 1, statusAttr, statusWorkInProgress, statusReadyForActivation, statusReadyForRelease, statusReadyToBeSetProductive)

   Print #fileNo, addTab(indent); "END IF;"
   Print #fileNo,
 End Sub
 ' ### ENDIF IVK ###
 
 
 Sub genDb2RegVarCheckDdl( _
   fileNo As Integer, _
   ddlType As DdlTypeId, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   Optional forLrt As TvBoolean = False, _
   Optional indent As Integer = 1 _
 )
   If Not generateDb2RegistryCheckInSps Then
     Exit Sub
   End If

   If ddlType = edtLdm Then
     Exit Sub
   End If

   If thisOrgIndex < 1 Or thisPoolIndex < 1 Then
     ' we do not check DB2 register 'outside of DataPools'
     Exit Sub
   End If

   If thisPoolIndex > 0 Then
 ' ### IF IVK ###
       If g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
         Exit Sub
       End If
 ' ### ENDIF IVK ###
       If Not g_pools.descriptors(thisPoolIndex).supportLrt Then
         forLrt = tvFalse
       End If
   End If
 
   Dim qualProcName As String
   qualProcName = genQualProcName(g_sectionIndexAliasLrt, spnCheckDb2Register, ddlType, thisOrgIndex, thisPoolIndex)
 
   genProcSectionHeader(fileNo, "verify that DB2-Register are used consistently", indent)

   If supportSpLogging Then
     Print #fileNo, addTab(indent + 0); "IF COALESCE(RIGHT("; gc_db2RegVarCtrl; ",1), '') = '1' THEN"
   Else
     Print #fileNo, addTab(indent + 0); "IF COALESCE("; gc_db2RegVarCtrl; ", '') = '1' THEN"
   End If

 ' ### IF IVK ###
   Print #fileNo, addTab(indent + 1); "CALL "; qualProcName; "("; gc_db2RegVarLrtOid; ", "; gc_db2RegVarPsOid; ", "; gc_db2RegVarSchema; ", "; IIf(forLrt = tvTrue, gc_dbTrue, IIf(forLrt = tvFalse, gc_dbFalse, "NULL")); ");"
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(indent + 1); "CALL "; qualProcName; "("; gc_db2RegVarLrtOid; ", "; gc_db2RegVarSchema; ", "; IIf(forLrt = tvTrue, gc_dbTrue, IIf(forLrt = tvFalse, gc_dbFalse, "NULL")); ");"
 ' ### ENDIF IVK ###
 
   Print #fileNo, addTab(indent + 0); "END IF;"
 End Sub
 
 
 ' ### IF IVK ###
 Sub genPsCheckDdlForInsertDelete( _
   fileNo As Integer, _
   ByRef qualAttrName As String, _
   ddlType As DdlTypeId, _
   ByVal thisOrgIndex As Integer, _
   Optional ignorePsRegVar As Boolean = False, _
   Optional psTagOptional As Boolean = False, _
   Optional indent As Integer = 1, _
   Optional genHeader As Boolean = True, _
   Optional ByRef psOidRecord As String = "v_psOidRecord", _
   Optional ByRef psOidRegVar As String = "v_psOidRegVar", _
   Optional ByRef psOidEffective As String = "v_psOid", _
   Optional forInsert As Boolean = False, _
   Optional ByRef refTab As String = "", _
   Optional ByRef refOid As String = "" _
 )
   If genHeader Then
     genProcSectionHeader(fileNo, "declare variables", indent)
   End If
   genVarDecl(fileNo, psOidRecord, g_dbtOid, "NULL", indent)
   If forInsert Or (Not ignorePsRegVar) Then
     genVarDecl(fileNo, psOidRegVar, g_dbtOid, "NULL", indent)
   End If
   If psOidEffective & "" <> "" Then
     genVarDecl(fileNo, psOidEffective, g_dbtOid, "NULL", indent)
   End If

   Print #fileNo,
   Print #fileNo, addTab(indent + 0); "SET "; psOidRecord; " = "; qualAttrName; ";"

   If forInsert Or (Not ignorePsRegVar) Then
     Print #fileNo, addTab(indent + 0); "SET "; psOidRegVar; " = (CASE WHEN "; gc_db2RegVarPsOid; _
                                        " IN ('','0') THEN CAST(NULL AS "; g_dbtOid; ") ELSE "; g_activePsOidDdl; " END);"
   End If

   If psOidEffective & "" <> "" Then
     If ignorePsRegVar Then
       Print #fileNo, addTab(indent + 0); "SET "; psOidEffective; " = COALESCE("; psOidRecord; ", "; psOidRegVar; ");"
     Else
       Print #fileNo, addTab(indent + 0); "SET "; psOidEffective; " = COALESCE("; psOidRegVar; ", "; psOidRecord; ");"
     End If
   End If

   genProcSectionHeader(fileNo, "verify that the PS-tag is used consistently", indent)
   If ignorePsRegVar Then
     If Not psTagOptional Then
       Print #fileNo, addTab(indent + 0); "IF "; IIf(psOidEffective & "" <> "", psOidEffective, psOidRecord); " IS NULL THEN"
       genProcSectionHeader(fileNo, "if PS-tag is not specified in record return with error", indent + 1, True)
       genSignalDdl("noPs", fileNo, indent + 1)
       Print #fileNo, addTab(indent + 0); "END IF;"
     End If
   Else
     Dim indent2 As Integer
     indent2 = 0
     If psTagOptional Then
       indent2 = -1
     End If

     If Not psTagOptional Then
       Print #fileNo, addTab(indent + 0); "IF ("; psOidRecord; " IS NULL AND "; psOidRegVar; " IS NULL) THEN"
       genProcSectionHeader(fileNo, "if PS-tag is specified neither in registry variable nor in record return with error", indent + 1, True)
       genSignalDdl("noPs", fileNo, indent + 1)
       Print #fileNo, addTab(indent + 0); "ELSE"
     End If
     Print #fileNo, addTab(indent + indent2 + 1); "IF (("; psOidRecord; " IS NOT NULL) AND ("; psOidRegVar; " IS NOT NULL) AND ("; psOidRecord; " <> "; psOidRegVar; ")) THEN"
     genProcSectionHeader(fileNo, "if PS-tag is specified neither in registry variable nor in record return with error", indent + indent2 + 2, True)

     genSignalDdlWithParms("incorrPsTagExtended", fileNo, indent + indent2 + 2, IIf(refTab = "", "", refTab), , , , , , , , , _
                           "RTRIM(CHAR(" & psOidRecord & "))", "RTRIM(CHAR(" & psOidRegVar & "))", IIf(refOid = "", "", "RTRIM(CHAR(" & refOid & "))"))

     Print #fileNo, addTab(indent + indent2 + 1); "END IF;"
     If Not psTagOptional Then
       Print #fileNo, addTab(indent + 0); "END IF;"
     End If
   End If
 End Sub
 
 
 Sub genPsCheckDdlForUpdate( _
   fileNo As Integer, _
   ByRef qualAttrNameOld As String, _
   ByRef qualAttrNameNew As String, _
   ddlType As DdlTypeId, _
   ByVal thisOrgIndex As Integer, _
   Optional psTagOptional As Boolean = False, _
   Optional indent As Integer = 1, _
   Optional genHeader As Boolean = True, _
   Optional ByRef psOidRecordNew As String = "v_psOidRecord", _
   Optional ByRef psOidRegVar As String = "v_psOidRegVar", _
   Optional ByRef psOidEffective As String = "v_psOid", _
   Optional ByRef refTab As String = "", _
   Optional ByRef refOid As String = "" _
 )
   If genHeader Then
     genProcSectionHeader(fileNo, "declare variables", indent)
   End If
   genVarDecl(fileNo, psOidRecordNew, g_dbtOid, "NULL", indent)
   genVarDecl(fileNo, psOidRegVar, g_dbtOid, "NULL", indent)
   If psOidEffective & "" <> "" Then
     genVarDecl(fileNo, psOidEffective, g_dbtOid, "NULL", indent)
   End If
 
   Print #fileNo,
   Print #fileNo, addTab(indent + 0); "SET "; psOidRecordNew; " = COALESCE("; qualAttrNameNew; ", "; qualAttrNameOld; ");"
   Print #fileNo, addTab(indent + 0); "SET "; psOidRegVar; " = (CASE WHEN "; gc_db2RegVarPsOid; _
                                      " IN ('','0') THEN CAST(NULL AS "; g_dbtOid; ") ELSE "; g_activePsOidDdl; " END);"
 
   If psOidEffective & "" <> "" Then
     Print #fileNo, addTab(indent + 0); "SET "; psOidEffective; " = COALESCE("; psOidRegVar; ", "; psOidRecordNew; ");"
   End If
 
   genProcSectionHeader(fileNo, "verify that the PS-tag is used consistently", indent)
   Dim indent2 As Integer
   indent2 = 0
   If psTagOptional Then
     indent2 = -1
   End If

   If Not psTagOptional Then
     Print #fileNo, addTab(indent + 0); "IF ("; psOidRecordNew; " IS NULL AND "; psOidRegVar; " IS NULL) THEN"
     genProcSectionHeader(fileNo, "if PS-tag is specified neither in registry variable nor in record return with error", indent + 1, True)
     genSignalDdl("noPs", fileNo, indent + 1)
     Print #fileNo, addTab(indent + 0); "ELSE"
   End If

   Print #fileNo, addTab(indent + indent2 + 1); "IF (("; psOidRecordNew; " IS NOT NULL) AND ("; psOidRegVar; " IS NOT NULL) AND ("; psOidRecordNew; " <> "; psOidRegVar; ")) THEN"
   genProcSectionHeader(fileNo, "if PS-tag is specified inconsistently return with error", indent + indent2 + 2, True)

   genSignalDdlWithParms("incorrPsTagExtended", fileNo, indent + indent2 + 2, IIf(refTab = "", "", refTab), , , , , , , , , _
                         "RTRIM(CHAR(" & psOidRecordNew & "))", "RTRIM(CHAR(" & psOidRegVar & "))", IIf(refOid = "", "", "RTRIM(CHAR(" & refOid & "))"))

   Print #fileNo, addTab(indent + indent2 + 1); "END IF;"

   If Not psTagOptional Then
     Print #fileNo, addTab(indent + 0); "END IF;"
   End If
 End Sub
 
 
 Sub genPsCheckDdlForNonPsTaggedInLrt( _
   fileNo As Integer, _
   ddlType As DdlTypeId, _
   ByVal thisOrgIndex As Integer, _
   Optional indent As Integer = 1, _
   Optional genHeader As Boolean = True, _
   Optional ByRef psOidRegVar As String = "v_psOidRegVar", _
   Optional ByRef psOidEffective As String = "v_psOid" _
 )
   If genHeader Then
     genProcSectionHeader(fileNo, "declare variables", indent)
   End If
   genVarDecl(fileNo, psOidRegVar, g_dbtOid, "NULL", indent)
   If psOidEffective & "" <> "" Then
     genVarDecl(fileNo, psOidEffective, g_dbtOid, "NULL", indent)
   End If

   Print #fileNo,
   Print #fileNo, addTab(indent + 0); "SET "; psOidRegVar; " = (CASE WHEN "; gc_db2RegVarPsOid; " IN ('','0') THEN CAST(NULL AS "; g_dbtOid; ") ELSE "; g_activePsOidDdl; " END);"

   If psOidEffective & "" <> "" Then
     Print #fileNo, addTab(indent + 0); "SET "; psOidEffective; " = "; psOidRegVar; ";"
   End If

   Print #fileNo, addTab(indent + 0); "IF "; psOidRegVar; " IS NULL THEN"
   genProcSectionHeader(fileNo, "if PS-tag is not specified in record return with error", indent + 1, True)
   genSignalDdl("noPs", fileNo, indent + 1)
   Print #fileNo, addTab(indent + 0); "END IF;"
 End Sub
 
 
 ' ### ENDIF IVK ###
 Sub genVerifyActiveLrtDdl( _
   fileNo As Integer, _
   ddlType As DdlTypeId, _
   ByRef qualTabNameLrt As String, _
   ByRef lrtOidStr As String, _
   Optional indent As Integer = 1, _
   Optional skipNl As Boolean = False _
 )
   genProcSectionHeader(fileNo, "verify that current LRT is (still) active", indent + 0, skipNl)
   Print #fileNo, addTab(indent + 0); "SET v_lrtClosed = (SELECT (CASE WHEN "; g_anEndTime; " IS NULL THEN "; gc_dbFalse; " ELSE "; gc_dbTrue; " END) FROM "; qualTabNameLrt; " WHERE "; g_anOid; " = "; lrtOidStr; ");"
   Print #fileNo, addTab(indent + 0); "IF v_lrtClosed = "; gc_dbTrue; " THEN"
   genProcSectionHeader(fileNo, "LRT is already closed", indent + 1, True)
   genSignalDdl("lrtClosed", fileNo, indent + 1)
   Print #fileNo, addTab(indent + 0); "ELSEIF v_lrtClosed IS NULL THEN"
   genProcSectionHeader(fileNo, "LRT does not exist", indent + 1, True)
   genSignalDdlWithParms("lrtNotExist", fileNo, indent + 1, , , , , , , , , , "RTRIM(CHAR(" & lrtOidStr & "))")
   Print #fileNo, addTab(indent + 0); "END IF;"
 End Sub
 
 
 Sub genAggHeadLockPropDdl( _
   fileNo As Integer, _
   ByRef recordName As String, _
   ahClassIndex As Integer, _
   ByRef qualAggHeadTabName As String, _
   ByRef qualAggHeadLrtTabName As String, _
   ByRef qualTabNameLrtAffectedEntity As String, _
   ByRef varNameCdUserId As String, _
   ddlType As DdlTypeId, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   Optional indent As Integer = 1, _
   Optional usePsOidWhereClause As Boolean = False, _
   Optional useDivOidWhereClause As Boolean = False, _
   Optional useDivRelKey As Boolean = False _
 )
   Dim transformation As AttributeListTransformation

 ' ### IF IVK ###
     If g_classes.descriptors(ahClassIndex).condenseData Then
       Exit Sub
     End If

 ' ### ENDIF IVK ###
     Dim qualTabNameLrt As String
     qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

     genProcSectionHeader(fileNo, "lock the 'public aggregate head record' with this LRT-OID", indent + 0)
     Print #fileNo, addTab(indent + 0); "IF "; recordName; "."; g_anAhOid; " IS NOT NULL THEN"

     genProcSectionHeader(fileNo, "since DB2 applies some restrictions on 'table access contexts'", indent + 1, True)
     genProcSectionHeader(fileNo, "it is NOT possible to call LRTLOCK here", indent + 1, True)

     genProcSectionHeader(fileNo, "determine OID of LRT owning a lock on aggregate head", indent + 1)
     Print #fileNo, addTab(indent + 1); "SET v_inLrt ="
     Print #fileNo, addTab(indent + 2); "("
     Print #fileNo, addTab(indent + 3); "SELECT"
     Print #fileNo, addTab(indent + 4); "PUB."; g_anInLrt
     Print #fileNo, addTab(indent + 3); "FROM"
     Print #fileNo, addTab(indent + 4); qualAggHeadTabName; " PUB"
     Print #fileNo, addTab(indent + 3); "WHERE"
     Print #fileNo, addTab(indent + 4); "PUB."; g_anOid; " = "; recordName; "."; g_anAhOid
     'add where clause on partition key ps_oid or div_oid except where aggregate child is ps_oid tagged and head div_oid tagged
     If Not ((ahClassIndex = g_classIndexGenericCode) And usePsOidWhereClause) Then
      genDdlPsDivClause(fileNo, indent + 4, "PUB", recordName, recordName, usePsOidWhereClause, False, False, useDivOidWhereClause, useDivRelKey, True)
     End If

     Print #fileNo, addTab(indent + 2); ")"
     Print #fileNo, addTab(indent + 1); ";"

     genProcSectionHeader(fileNo, "check if aggregate head is locked by some LRT other than this one", indent + 1)
     Print #fileNo, addTab(indent + 1); "IF (v_inLrt IS NOT NULL) AND (v_inLrt <> v_lrtOid) THEN"
     genProcSectionHeader(fileNo, "determine ID of user holding the lock", indent + 2, True)
     Print #fileNo, addTab(indent + 2); "SET "; varNameCdUserId; " = (SELECT USR."; g_anUserId; " FROM "; g_qualTabNameUser; " USR INNER JOIN "; qualTabNameLrt; " LRT ON LRT.UTROWN_OID = USR."; g_anOid; " WHERE LRT."; g_anOid; " = v_inLrt);"
     Print #fileNo, addTab(indent + 2); "SET "; varNameCdUserId; " = COALESCE("; varNameCdUserId; ", '<unknown>');"
     Print #fileNo,
     genSignalDdlWithParms("lrtLockAlreadyLocked", fileNo, indent + 2, , , , , , , , , , varNameCdUserId)
     Print #fileNo, addTab(indent + 1); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(indent + 1); "IF v_inLrt IS NOT NULL THEN"
     genProcSectionHeader(fileNo, "aggregate head is already locked by this transaction", indent + 2, True)
     Print #fileNo, addTab(indent + 1); "ELSE"

     genProcSectionHeader(fileNo, "copy the 'public aggregate head' into 'private table'", indent + 2, True)
     Print #fileNo, addTab(indent + 2); "INSERT INTO"
     Print #fileNo, addTab(indent + 3); qualAggHeadLrtTabName
     Print #fileNo, addTab(indent + 2); "("

 ' ### IF IVK ###
     genAttrListForEntity(ahClassIndex, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, True, False, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
 ' ### ELSE IVK ###
 '   genAttrListForEntity ahClassIndex, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, True, False, edomListLrt
 ' ### ENDIF IVK ###

     Print #fileNo, addTab(indent + 2); ")"
     Print #fileNo, addTab(indent + 2); "SELECT"

 ' ### IF IVK ###
     initAttributeTransformation(transformation, 3, , True, True)
 ' ### ELSE IVK ###
 '   initAttributeTransformation transformation, 2, , True, True
 ' ### ENDIF IVK ###

     setAttributeMapping(transformation, 1, conLrtState, "" & lrtStatusLocked)
     setAttributeMapping(transformation, 2, conInLrt, "v_lrtOid")
 ' ### IF IVK ###
     setAttributeMapping(transformation, 3, conPsOid, "v_psOid")

     genTransformedAttrListForEntity(ahClassIndex, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, , True, False, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
 ' ### ELSE IVK ###
 '   genTransformedAttrListForEntity ahClassIndex, eactClass, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, indent + 3, , True, False, edomListLrt
 ' ### ENDIF IVK ###

     Print #fileNo, addTab(indent + 2); "FROM"
     Print #fileNo, addTab(indent + 3); qualAggHeadTabName
     Print #fileNo, addTab(indent + 2); "WHERE"
     Print #fileNo, addTab(indent + 3); g_anOid; " = "; recordName; "."; g_anAhOid
     'add where clause on partition key ps_oid or div_oid except where aggregate child is ps_oid tagged and head div_oid tagged
     If Not ((ahClassIndex = g_classIndexGenericCode) And usePsOidWhereClause) Then
       genDdlPsDivClause(fileNo, indent + 3, "", recordName, recordName, usePsOidWhereClause, False, False, useDivOidWhereClause, useDivRelKey, True)
     End If
     Print #fileNo, addTab(indent + 2); ";"

     genProcSectionHeader(fileNo, "lock the 'public aggregate head' with this LRT-OID", indent + 2)
     Print #fileNo, addTab(indent + 2); "UPDATE"
     Print #fileNo, addTab(indent + 3); qualAggHeadTabName; " PUB"
     Print #fileNo, addTab(indent + 2); "SET"
     Print #fileNo, addTab(indent + 3); "PUB."; g_anInLrt; " = v_lrtOid"
     Print #fileNo, addTab(indent + 2); "WHERE"
     Print #fileNo, addTab(indent + 3); "PUB."; g_anOid; " = "; recordName; "."; g_anAhOid
     'add where clause on partition key ps_oid or div_oid except where aggregate child is ps_oid tagged and head div_oid tagged
     If Not ((ahClassIndex = g_classIndexGenericCode) And usePsOidWhereClause) Then
       genDdlPsDivClause(fileNo, indent + 3, "PUB", recordName, recordName, usePsOidWhereClause, False, False, useDivOidWhereClause, useDivRelKey, True)
     End If
     Print #fileNo, addTab(indent + 2); ";"

     genDdlForUpdateAffectedEntities(fileNo, "aggregate head", eactClass, gc_acmEntityTypeKeyClass, False, False, qualTabNameLrtAffectedEntity, getClassIdStrByIndex(ahClassIndex), _
       getClassIdStrByIndex(ahClassIndex), "v_lrtOid", indent + 2, CStr(lrtStatusLocked), False)

     Print #fileNo, addTab(indent + 1); "END IF;"
     Print #fileNo, addTab(indent + 0); "END IF;"
 End Sub
 
 
 Sub genProcParm( _
   fileNo As Integer, _
   ByRef mode As String, _
   ByRef name As String, _
   ByRef dbType As String, _
   Optional addComma As Boolean = True, _
   Optional ByRef comment As String = "" _
 )
   Dim comma As String
   comma = IIf(addComma, ",", "")

   Print #fileNo, addTab(1); IIf(mode <> "", Left(mode & "   ", gc_sqlMaxParmNameLength) & " ", "");
   Print #fileNo, Left(name & "                           ", gc_sqlMaxVarNameLength); " ";
   Print #fileNo, IIf(comment = "", dbType & comma, _
                      IIf(Len(dbType) >= gc_sqlMaxVarTypeLength, dbType & comma, Left(dbType & comma & "                        ", gc_sqlMaxVarTypeLength)) & " -- " & comment)
 End Sub


 Sub genVarDecl( _
   fileNo As Integer, _
   ByRef varName As String, _
   ByRef dbType As String, _
   Optional ByRef default As String = "", _
   Optional indent As Integer = 1, _
   Optional ByRef comment As String = "" _
 )
   Print #fileNo, addTab(indent); "DECLARE "; IIf(Len(varName) > gc_sqlMaxVarNameLength, varName, Left(varName & "                           ", gc_sqlMaxVarNameLength)); " "; _
                                  IIf(default = "", dbType & ";", _
                                                    IIf(Len(dbType) >= gc_sqlMaxVarTypeLength, dbType, Left(dbType & "                        ", gc_sqlMaxVarTypeLength)) & " " & _
                                                    "DEFAULT " & default & ";"); _
                                  IIf(comment = "", "", " -- " & comment)
 End Sub


 Sub genCondDecl( _
   fileNo As Integer, _
   ByRef condName As String, _
   ByRef sqlState As String, _
   Optional indent As Integer = 1 _
 )
   Print #fileNo, addTab(indent); "DECLARE "; Left(condName & "                           ", gc_sqlMaxVarNameLength); " "; _
                                   "CONDITION FOR SQLSTATE '"; sqlState; "';"
 End Sub

 Sub genProcSectionHeader( _
   fileNo As Integer, _
   ByRef header As String, _
   Optional indent As Integer = 1, _
   Optional skipNl As Boolean = False _
 )
   If Not skipNl Then
     Print #fileNo,
   End If
   Print #fileNo, addTab(indent); "-- "; header
 End Sub
 
 Sub genDdlForUpdateAffectedEntities( _
   fileNo As Integer, _
   ByRef entityTypeDescr As String, _
   acmEntityType As AcmAttrContainerType, _
   ByRef dbAcmEntityType As String, _
   ByRef isGen As Boolean, _
   ByVal isNl As Boolean, _
   ByRef qualTabNameLrtAffectedEntity As String, _
   ByRef entityIdStr As String, _
   ByRef ahClassIdStr As String, _
   ByRef lrtOidStr As String, _
   Optional indent As Integer = 2, _
   Optional ByRef op As String = "v_lrtExecutedOperation", _
   Optional propagateToAh As Boolean = True _
 )

   genProcSectionHeader(fileNo, "register that this " & entityTypeDescr & " is affected by this LRT", indent)
   Print #fileNo, addTab(indent + 0); "SET v_lrtEntityIdCount ="
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); "SELECT"
   Print #fileNo, addTab(indent + 3); "COUNT(*)"
   Print #fileNo, addTab(indent + 2); "FROM"
   Print #fileNo, addTab(indent + 3); qualTabNameLrtAffectedEntity
   Print #fileNo, addTab(indent + 2); "WHERE"
   Print #fileNo, addTab(indent + 3); g_anLrtOid; " = "; lrtOidStr
   Print #fileNo, addTab(indent + 4); "AND"
   Print #fileNo, addTab(indent + 3); g_anAcmOrParEntityId; " = '"; entityIdStr; "'"
   Print #fileNo, addTab(indent + 4); "AND"
   Print #fileNo, addTab(indent + 3); g_anAcmEntityType; " = '"; dbAcmEntityType; "'"
   If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     Print #fileNo, addTab(indent + 4); "AND"
     Print #fileNo, addTab(indent + 3); g_anLdmIsGen; " = "; CStr(IIf(isGen, 1, 0))
     Print #fileNo, addTab(indent + 4); "AND"
     Print #fileNo, addTab(indent + 3); g_anLdmIsNl; " = "; CStr(IIf(isNl, 1, 0))
   End If
   Print #fileNo, addTab(indent + 4); "AND"
   Print #fileNo, addTab(indent + 3); g_anLrtOpId; " = "; op
   Print #fileNo, addTab(indent + 1); ");"
   Print #fileNo,
   Print #fileNo, addTab(indent + 0); "IF v_lrtEntityIdCount = 0 THEN"
   Print #fileNo, addTab(indent + 1); "INSERT INTO"
   Print #fileNo, addTab(indent + 2); qualTabNameLrtAffectedEntity
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); g_anLrtOid; ","
   Print #fileNo, addTab(indent + 2); g_anAcmOrParEntityId; ","
   Print #fileNo, addTab(indent + 2); g_anAcmEntityType; ","
   If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     Print #fileNo, addTab(indent + 2); g_anLdmIsGen; ","
     Print #fileNo, addTab(indent + 2); g_anLdmIsNl; ","
   End If
   Print #fileNo, addTab(indent + 2); g_anLrtOpId
   Print #fileNo, addTab(indent + 1); ")"
   Print #fileNo, addTab(indent + 1); "VALUES"
   Print #fileNo, addTab(indent + 1); "("
   Print #fileNo, addTab(indent + 2); lrtOidStr; ","
   Print #fileNo, addTab(indent + 2); "'"; entityIdStr; "',"
   Print #fileNo, addTab(indent + 2); "'"; dbAcmEntityType; "',"
   If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     Print #fileNo, addTab(indent + 2); CStr(IIf(isGen, 1, 0)); ","
     Print #fileNo, addTab(indent + 2); CStr(IIf(isNl, 1, 0)); ","
   End If
   Print #fileNo, addTab(indent + 2); op
   Print #fileNo, addTab(indent + 1); ");"
   Print #fileNo, addTab(indent + 0); "END IF;"

   If propagateToAh And ((acmEntityType = eactRelationship) Or (entityIdStr <> ahClassIdStr)) Then
     genProcSectionHeader(fileNo, "register that aggregate head is affected (locked) by this LRT", indent)
     Print #fileNo,
     Print #fileNo, addTab(indent + 0); "SET v_lrtEntityIdCount ="
     Print #fileNo, addTab(indent + 1); "("
     Print #fileNo, addTab(indent + 2); "SELECT"
     Print #fileNo, addTab(indent + 3); "COUNT(*)"
     Print #fileNo, addTab(indent + 2); "FROM"
     Print #fileNo, addTab(indent + 3); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(indent + 2); "WHERE"
     Print #fileNo, addTab(indent + 3); g_anLrtOid; " = "; lrtOidStr
     Print #fileNo, addTab(indent + 4); "AND"
     Print #fileNo, addTab(indent + 3); g_anAcmOrParEntityId; " = '"; ahClassIdStr; "'"
     Print #fileNo, addTab(indent + 4); "AND"
     Print #fileNo, addTab(indent + 3); g_anAcmEntityType; " = '"; getAcmEntityTypeKey(eactClass); "'"
     If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); g_anLdmIsGen; " = "; gc_dbFalse
       Print #fileNo, addTab(indent + 4); "AND"
       Print #fileNo, addTab(indent + 3); g_anLdmIsNl; " = "; gc_dbFalse
     End If
     Print #fileNo, addTab(indent + 4); "AND"
     Print #fileNo, addTab(indent + 3); g_anLrtOpId; " = "; CStr(lrtStatusLocked)
     Print #fileNo, addTab(indent + 1); ");"

     Print #fileNo,
     Print #fileNo, addTab(indent + 0); "IF v_lrtEntityIdCount = 0 THEN"
     Print #fileNo, addTab(indent + 1); "INSERT INTO"
     Print #fileNo, addTab(indent + 2); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(indent + 1); "("
     Print #fileNo, addTab(indent + 2); g_anLrtOid; ","
     Print #fileNo, addTab(indent + 2); g_anAcmOrParEntityId; ","
     Print #fileNo, addTab(indent + 2); g_anAcmEntityType; ","
     If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
       Print #fileNo, addTab(indent + 2); g_anLdmIsGen; ","
       Print #fileNo, addTab(indent + 2); g_anLdmIsNl; ","
     End If
     Print #fileNo, addTab(indent + 2); g_anLrtOpId
     Print #fileNo, addTab(indent + 1); ")"
     Print #fileNo, addTab(indent + 1); "VALUES"
     Print #fileNo, addTab(indent + 1); "("
     Print #fileNo, addTab(indent + 2); lrtOidStr; ","
     Print #fileNo, addTab(indent + 2); "'"; ahClassIdStr; "',"
     Print #fileNo, addTab(indent + 2); "'"; gc_acmEntityTypeKeyClass; "',"
     If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
       Print #fileNo, addTab(indent + 2); "0,"
       Print #fileNo, addTab(indent + 2); "0,"
     End If
     Print #fileNo, addTab(indent + 2); CStr(lrtStatusLocked)
     Print #fileNo, addTab(indent + 1); ");"
     Print #fileNo, addTab(indent + 0); "END IF;"
   End If
 End Sub
 
 
 Sub genDdlForUpdateLrtLastOpTs( _
   fileNo As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ByRef lrtOidStr As String, _
   Optional ByRef timestampStr As String = "CURRENT TIMESTAMP", _
   Optional ByRef ddlType As DdlTypeId = edtPdm, _
   Optional indent As Integer = 1 _
 )
   Dim qualTabNameLrtExecStatus As String
   qualTabNameLrtExecStatus = genQualTabNameByClassIndex(g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex)

   genProcSectionHeader(fileNo, "record LRT's last update timestamp", indent + 0)
   Print #fileNo, addTab(indent + 0); "UPDATE"
   Print #fileNo, addTab(indent + 1); qualTabNameLrtExecStatus
   Print #fileNo, addTab(indent + 0); "SET"
   Print #fileNo, addTab(indent + 1); g_anLastOpTime; " = "; timestampStr
   Print #fileNo, addTab(indent + 0); "WHERE"
   Print #fileNo, addTab(indent + 1); g_anLrtOid; " = "; lrtOidStr
   Print #fileNo, addTab(indent + 0); ";"
 End Sub

 
 Sub genDdlForTempLrtLog( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional restrictColSet As Boolean = False, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary table for LRT-Log", indent)
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempTabNameLrtLog
   Print #fileNo, addTab(indent + 0); "("
   If Not restrictColSet Then
     Print #fileNo, addTab(indent + 1); "displayMe           "; g_dbtBoolean; ","
     Print #fileNo, addTab(indent + 1); "orParEntityId       "; g_dbtEntityId; ","
   End If
   Print #fileNo, addTab(indent + 1); "entityId            "; g_dbtEntityId; ","
   Print #fileNo, addTab(indent + 1); "entityType          "; g_dbtEntityType; ","
   If Not restrictColSet Then
     Print #fileNo, addTab(indent + 1); "entityName          VARCHAR(60),"
 ' ### IF IVK ###
     Print #fileNo, addTab(indent + 1); "displayCategory     CHAR(1),"
 ' ### ENDIF IVK ###
   End If
   Print #fileNo, addTab(indent + 1); "gen                 "; g_dbtBoolean; ","
   Print #fileNo, addTab(indent + 1); "isNl                "; g_dbtBoolean; ","
   Print #fileNo, addTab(indent + 1); "oid                 "; g_dbtOid; ","
   If Not restrictColSet Then
     Print #fileNo, addTab(indent + 1); "refClassId1         "; g_dbtEntityId; ","
   End If
   Print #fileNo, addTab(indent + 1); "refObjectId1        "; g_dbtOid; ","
   If Not restrictColSet Then
     Print #fileNo, addTab(indent + 1); "refClassId2         "; g_dbtEntityId; ","
   End If
   Print #fileNo, addTab(indent + 1); "refObjectId2        "; g_dbtOid; ","
   If Not restrictColSet Then
     Print #fileNo, addTab(indent + 1); "label               "; g_dbtLrtLabel; ","
     Print #fileNo, addTab(indent + 1); "comment             "; g_dbtChangeComment; ","
   End If
   Print #fileNo, addTab(indent + 1); "code                "; g_dbtCodeNumber; ","
   If Not restrictColSet Then
 ' ### IF IVK ###
     Print #fileNo, addTab(indent + 1); "sr0Context          VARCHAR(159),"
     Print #fileNo, addTab(indent + 1); "sr0Code1            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "sr0Code2            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "sr0Code3            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "sr0Code4            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "sr0Code5            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "sr0Code6            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "sr0Code7            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "sr0Code8            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "sr0Code9            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "sr0Code10           "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "baseCode            "; g_dbtCodeNumber; ","
     Print #fileNo, addTab(indent + 1); "baseEndSlot         "; getDbDataTypeByDomainName(dxnEndSlotLabel, dnEndSlotLabel); ","
     Print #fileNo, addTab(indent + 1); "t_baseEndSlotGenOid "; g_dbtOid; ","
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(indent + 1); "validFrom           DATE,"
     Print #fileNo, addTab(indent + 1); "validTo             DATE,"
   End If
   Print #fileNo, addTab(indent + 1); "operation           "; g_dbtEnumId; ","
   Print #fileNo, addTab(indent + 1); "ts                  TIMESTAMP"
   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace)
 End Sub
 
 
 Sub genDdlForTempTableDeclTrailer( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   If onCommitPreserve Then
     Print #fileNo, addTab(indent + 0); "ON COMMIT PRESERVE ROWS"
   End If

   Print #fileNo, addTab(indent + 0); "NOT LOGGED";

   If onRollbackPreserve Then
     Print #fileNo,
     Print #fileNo, addTab(indent + 0); "ON ROLLBACK PRESERVE ROWS";
   End If

   If withReplace Then
     Print #fileNo,
     Print #fileNo, addTab(indent + 0); "WITH REPLACE";
   End If
 
   Print #fileNo, ";"
 End Sub
 
 
 Private Sub genLrtSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not g_genLrtSupport Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseLrt, ldmIterationPoolSpecific)

   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLrtNlText As String
   qualTabNameLrtNlText = genQualNlTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLdmLrt As String
   qualTabNameLdmLrt = genQualTabNameByClassIndex(g_classIndexLrt, edtLdm, thisOrgIndex, thisPoolIndex)

   Dim lrtClassUseSurrogateKey As Boolean
   lrtClassUseSurrogateKey = g_classes.descriptors(g_classIndexLrt).useSurrogateKey

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLrtExecStatus As String
   qualTabNameLrtExecStatus = genQualTabNameByClassIndex(g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex)

 ' ### IF IVK ###
   If Not generateFwkTest Then
     Dim qualTabNameGeneralSettings As String
     qualTabNameGeneralSettings = genQualTabNameByClassIndex(g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex)

     Dim qualTabNameConflict As String
     qualTabNameConflict = genQualTabNameByClassIndex(g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex)
   End If

   ' ### ENDIF IVK ###

   Dim qualViewName As String
   Dim qualViewNameLdm  As String
   qualViewName = genQualViewName(g_sectionIndexLrt, vnLrtAffectedLdmTab, vsnLrtAffectedLdmTab, ddlType, thisOrgIndex, thisPoolIndex)
   qualViewNameLdm = genQualViewName(g_sectionIndexLrt, vnLrtAffectedLdmTab, vsnLrtAffectedLdmTab, edtLdm)

   genLrtSupportDdlByPool0(fileNo, thisOrgIndex, thisPoolIndex, ddlType)

   ' just to initialize the variables
 
     Dim qualNlTabName As String
     Dim qualNlTabNameLdm As String
     Dim nlObjName As String
     Dim nlObjNameShort As String
     nlObjName = genNlObjName(g_classes.descriptors(g_classIndexLrt).className)
     nlObjNameShort = genNlObjShortName(g_classes.descriptors(g_classIndexLrt).shortName)
     qualNlTabName = genQualTabNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
     qualNlTabNameLdm = genQualTabNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, edtLdm, thisOrgIndex, thisPoolIndex, , , , True)
 


   ' ####################################################################################################################
   ' #    create view to determine PDM tables involved in an LRT
   ' ####################################################################################################################

   Dim qualViewNamePdmTabs As String
   Dim qualViewNamePdmTabsLdm  As String

   qualViewNamePdmTabs = genQualViewName(g_sectionIndexLrt, vnLrtAffectedPdmTab, vsnLrtAffectedPdmTab, ddlType, thisOrgIndex, thisPoolIndex)
   qualViewNamePdmTabsLdm = genQualViewName(g_sectionIndexLrt, vnLrtAffectedPdmTab, vsnLrtAffectedPdmTab, edtLdm)

   printSectionHeader("View for all PDM-tables related to a specific LRT", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNamePdmTabs
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anInLrt; ","
   Print #fileNo, addTab(1); g_anAcmOrParEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); "OPID,"
   Print #fileNo, addTab(1); "SCHEMANAME,"
   Print #fileNo, addTab(1); g_anPdmTableName; ","
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); g_anAcmIgnoreForChangelog; ","
   Print #fileNo, addTab(1); g_anAcmUseLrtCommitPreprocess; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(1); g_anAcmDisplayCategory; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "SEQNO"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "AL."; g_anInLrt; ","
   Print #fileNo, addTab(2); "AL."; g_anAcmOrParEntityId; ","
   Print #fileNo, addTab(2); "AL."; g_anAcmEntityType; ","
   Print #fileNo, addTab(2); "AL.OPID,"
   Print #fileNo, addTab(2); "PT."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(2); "PT."; g_anPdmTableName; ","
   Print #fileNo, addTab(2); "AL."; g_anLdmIsNl; ","
   Print #fileNo, addTab(2); "AL."; g_anLdmIsGen; ","
   Print #fileNo, addTab(2); "AL."; g_anAcmIgnoreForChangelog; ","
   Print #fileNo, addTab(2); "AL."; g_anAcmUseLrtCommitPreprocess; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "AL."; g_anAcmDisplayCategory; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "AL.SEQNO"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewName; " AL,"
   Print #fileNo, addTab(2); g_qualTabNamePdmTable; " PT"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "PT."; g_anPdmLdmFkSchemaName; " = AL."; g_anLdmSchemaName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PT."; g_anPdmLdmFkTableName; " = AL."; g_anLdmTableName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PT."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PT."; g_anPoolTypeId; " = "; genPoolId(thisPoolIndex, ddlType)
 
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 ' ### IF IVK ###
     genAliasDdl(g_sectionIndexLrt, vnLrtAffectedPdmTab, g_classes.descriptors(g_classIndexLrtAffectedEntity).isCommonToOrgs, g_classes.descriptors(g_classIndexLrtAffectedEntity).isCommonToPools, True, _
       qualViewNamePdmTabsLdm, qualViewNamePdmTabs, g_classes.descriptors(g_classIndexLrtAffectedEntity).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, False, False, False, _
       "LRT-AFFECTED-PDM-TABLES View """ & snDbMeta & "." & vnLrtAffectedPdmTab & """", , True)
 ' ### ELSE IVK ###
 '   genAliasDdlX g_sectionIndexLrt, vnLrtAffectedPdmTab, .isCommonToOrgs, .isCommonToPools, True, _
 '     qualViewNamePdmTabsLdm, qualViewNamePdmTabs, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, _
 '     "LRT-AFFECTED-PDM-TABLES View """ & snDbMeta & "." & vnLrtAffectedPdmTab & """", , True
 ' ### ENDIF IVK ###

   ' ####################################################################################################################
   ' #    SP for BEGIN of LRT
   ' ####################################################################################################################

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim internalProcVersion As Boolean
   Dim qualProcName As String
 ' ### IFNOT IVK ###
  Dim i As Integer
 ' ### ENDIF IVK ###
   For i = 1 To 2
     internalProcVersion = (i = 2)
 
     qualProcName = _
       genQualProcName(IIf(internalProcVersion, g_sectionIndexLrt, g_sectionIndexAliasLrt), spnLrtBegin, ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader("Stored Procedure for BEGIN of LRT", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcName
     Print #fileNo, addTab(0); "("

     genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser")
 ' ### IF IVK ###
     genProcParm(fileNo, "IN", "trNumber_in", "INTEGER", True, "logical transaction number")
     If internalProcVersion Then
       genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure we are working with")
     End If
     genProcParm(fileNo, "IN", "isCentralDataTransfer_in", g_dbtBoolean, True, "logically 'boolean' (0 = false, 1 = true)")
 ' ### ENDIF IVK ###
     genProcParm(fileNo, "OUT", "lrtOid_out", g_dbtLrtId, False, "return value: OID of the created LRT")

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare variables", , True)
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_now", "TIMESTAMP", "NULL")
     genVarDecl(fileNo, "v_userOid", g_dbtOid, "0")
     genVarDecl(fileNo, "v_lrtCount", "INTEGER", "0")
     genSpLogDecl(fileNo)

 ' ### IF IVK ###
     If Not internalProcVersion Then
       genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
     End If

     genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'cdUserId_in", "trNumber_in", IIf(internalProcVersion, "psOid_in", ""), "isCentralDataTransfer_in", "lrtOid_out")
 ' ### ELSE IVK ###
 '   genSpLogProcEnter fileNo, qualProcName, ddlType, , "'cdUserId_in", "lrtOid_out"
 ' ### ENDIF IVK ###

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

     genProcSectionHeader(fileNo, "determine timestamp of LRT begin")
     Print #fileNo, addTab(1); "SET v_now = CURRENT TIMESTAMP;"

     genProcSectionHeader(fileNo, "initialize output parameter")
     Print #fileNo, addTab(1); "SET lrtOid_out = NULL;"

 ' ### IF IVK ###
     If Not internalProcVersion Then
       genProcSectionHeader(fileNo, "initialize variables")
       Print #fileNo, addTab(1); "SET v_psOid = "; g_activePsOidDdl; ";"
     End If

 ' ### ENDIF IVK ###
     genProcSectionHeader(fileNo, "determine user's OID")
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); g_anOid
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_userOid"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); g_qualTabNameUser; " U"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "U."; g_anUserId; " = cdUserId_in"
     Print #fileNo, addTab(1); "WITH UR;"

     genProcSectionHeader(fileNo, "make sure that cdUserId_in identifies a valid user")
     Print #fileNo, addTab(1); "IF (v_userOid IS NULL) THEN"
 ' ### IF IVK ###
     genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "cdUserId_in", "trNumber_in", IIf(internalProcVersion, "psOid_in", ""), "isCentralDataTransfer_in", "lrtOid_out")
 ' ### ELSE IVK ###
 '   genSpLogProcEscape fileNo, qualProcName, ddlType, , "'cdUserId_in", "lrtOid_out"
 ' ### ENDIF IVK ###
     genSignalDdlWithParms("userUnknown", fileNo, 2, , , , , , , , , , "cdUserId_in")
     Print #fileNo, addTab(1); "END IF;"
 ' ### IF IVK ###

     genProcSectionHeader(fileNo, "verify that this transaction has not ended")
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "COUNT(*)"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_lrtCount"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameLrt; " L"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "L.UTROWN_OID = v_userOid"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "L.TRNUMBER = trNumber_in"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "L."; g_anPsOid; " = "; IIf(internalProcVersion, "psOid_in", "v_psOid")
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "L."; g_anEndTime; " IS NULL"
     Print #fileNo, addTab(1); "WITH UR;"

     genProcSectionHeader(fileNo, "if there is already an active transaction for this user with the same logical trNumber, we need to quit")
     Print #fileNo, addTab(1); "IF (v_lrtCount > 0) THEN"
     genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "cdUserId_in", "trNumber_in", IIf(internalProcVersion, "psOid_in", ""), "isCentralDataTransfer_in", "lrtOid_out")
     genSignalDdlWithParms("ltrAlreadyActive", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(trNumber_in))", "cdUserId_in")
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader(fileNo, "create and register new LRT-OID")
     Print #fileNo, addTab(1); "SET lrtOid_out = NEXTVAL FOR "; qualSeqNameOid; ";"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameLrt
     Print #fileNo, addTab(1); "("
     If lrtClassUseSurrogateKey Then
       Print #fileNo, addTab(2); g_anOid; ","
     End If
     Print #fileNo, addTab(2); g_anIsCentralDataTransfer; ","
     Print #fileNo, addTab(2); "STARTTIME,"
     Print #fileNo, addTab(2); "TRNUMBER,"
     Print #fileNo, addTab(2); "UTROWN_OID,"
     Print #fileNo, addTab(2); g_anPsOid; ","
     Print #fileNo, addTab(2); g_anVersionId
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "("
     If lrtClassUseSurrogateKey Then
       Print #fileNo, addTab(2); "lrtOid_out,"
     End If
     Print #fileNo, addTab(2); "isCentralDataTransfer_in,"
     Print #fileNo, addTab(2); "v_now,"
     Print #fileNo, addTab(2); "trNumber_in,"
     Print #fileNo, addTab(2); "v_userOid,"
     Print #fileNo, addTab(2); IIf(internalProcVersion, "psOid_in", "v_psOid"); ","
     Print #fileNo, addTab(2); "1"
     Print #fileNo, addTab(1); ");"
 ' ### ENDIF IVK ###

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameLrtExecStatus
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); g_anLrtOid; ","
     Print #fileNo, addTab(2); conLastOpTime
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "VALUES"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "lrtOid_out,"
     Print #fileNo, addTab(2); "v_now"
     Print #fileNo, addTab(1); ");"

 ' ### IF IVK ###
     genSpLogProcExit(fileNo, qualProcName, ddlType, , "'cdUserId_in", "trNumber_in", IIf(internalProcVersion, "psOid_in", ""), "isCentralDataTransfer_in", "lrtOid_out")
 ' ### ELSE IVK ###
 '   genSpLogProcExit fileNo, qualProcName, ddlType, , "'cdUserId_in", "trNumber_in", "lrtOid_out"
 ' ### ENDIF IVK ###

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i

   ' ####################################################################################################################
   ' #    SP for LRT-(UN)LOCK
   ' ####################################################################################################################

   Dim qualPdmTableViewName As String
   qualPdmTableViewName = genQualViewName(g_sectionIndexDbMeta, vnPdmTable, vnsPdmTable, ddlType)
 
   Dim qualFuncNameParseClassIdOidList As String
   qualFuncNameParseClassIdOidList = genQualFuncName(g_sectionIndexMeta, udfnParseClassIdOidList, ddlType, , , , , , True)
 
   Dim unQualProcedureName As String
   Dim unQualProcedureShortName As String
   Dim un As String
   unQualProcedureName = "LRTLOCK"
   unQualProcedureShortName = "LCK"
   un = ""

   Dim forLock As Boolean
   For i = 1 To 2
     forLock = (i = 1)

     qualProcName = genQualProcName(g_sectionIndexAliasLrt, unQualProcedureName, ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader("SP for LRT-" & UCase(un) & "LOCK", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcName
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "classId_in", g_dbtEntityId, True, "CLASSID of the row being " & un & "locked")
     genProcParm(fileNo, "IN", "oid_in", g_dbtOid, True, "OID of the row being " & un & "locked")
     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being " & un & "locked (0 or 1)")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare conditions", , True)
     genCondDecl(fileNo, "notFound", "02000")
     genSpLogDecl(fileNo)
 
     genProcSectionHeader(fileNo, "declare variables")
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
     genVarDecl(fileNo, "v_tabFound", g_dbtBoolean, gc_dbFalse)
 ' ### IF IVK ###
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "0")
 ' ### ENDIF IVK ###
     If forLock Then
       genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "0")
     End If

     genProcSectionHeader(fileNo, "declare statement")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
     genProcSectionHeader(fileNo, "declare continue handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"
 
     genSpLogProcEnter(fileNo, qualProcName, ddlType, , "'classId_in", "oid_in", "rowCount_out")

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, IIf(forLock, tvTrue, tvNull), 1)
 
     genProcSectionHeader(fileNo, "initialize variables")
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"
     If forLock Then
       Print #fileNo, addTab(1); "SET v_lrtOid = "; g_activeLrtOidDdl; ";"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(1); "SET v_psOid  = "; g_activePsOidDdl; ";"
 ' ### ENDIF IVK ###

     If forLock Then
       genProcSectionHeader(fileNo, "verify that we have an active transaction")
       Print #fileNo, addTab(1); "IF "; gc_db2RegVarLrtOid; " = '' THEN"

       genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "classId_in", "oid_in", "rowCount_out")
       genSignalDdl("noLrt", fileNo, 2)

       Print #fileNo, addTab(1); "END IF;"
     End If

     genProcSectionHeader(fileNo, "process involved table(s) - with current MDS concepts there is exactly one table")
     Print #fileNo, addTab(1); "FOR tabLoop AS"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); g_anPdmFkSchemaName; " AS c_schemaName,"
     Print #fileNo, addTab(3); g_anPdmTypedTableName; " AS c_tableName"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualPdmTableViewName
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "ENTITY_ID = classId_in"
     Print #fileNo, addTab(4); "AND"
     genDdlPdmEntityCheck(fileNo, 3, "")
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "PDM_"; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "PDM_POOLTYPE_ID = "; genPoolId(thisPoolIndex, ddlType)
     Print #fileNo, addTab(2); "WITH UR"
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"
 ' ### IF IVK ###
     If forLock Then
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?,?,?)';"
     Else
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?,?)';"
     End If
 ' ### ELSE IVK ###
 '   If forLock Then
 '     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?,?)';"
 '   Else
 '     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?)';"
 '   End If
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE"
     Print #fileNo, addTab(3); "v_stmnt"
     Print #fileNo, addTab(2); "INTO"
     Print #fileNo, addTab(3); "v_rowCount"
     Print #fileNo, addTab(2); "USING"
     If forLock Then
       Print #fileNo, addTab(3); "v_lrtOid,"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(3); "v_psOid,"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(3); "oid_in"
     Print #fileNo, addTab(2); ";"
     Print #fileNo,
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);"
     Print #fileNo, addTab(2); "SET v_tabFound = "; gc_dbTrue; ";"
     Print #fileNo, addTab(1); "END FOR;"

     genProcSectionHeader(fileNo, "make sure that we found a table")
     Print #fileNo, addTab(1); "IF v_tabFound = "; gc_dbFalse; " THEN"
     genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "classId_in", "oid_in", "rowCount_out")
     If un = "" Then
       genSignalDdlWithParms("noTableToLock", fileNo, 2, , , , , , , , , , "classId_in")
     Else
       genSignalDdlWithParms("noTableToUnLock", fileNo, 2, , , , , , , , , , "classId_in")
     End If

     Print #fileNo, addTab(1); "END IF;"

     genSpLogProcExit(fileNo, qualProcName, ddlType, , "'classId_in", "oid_in", "rowCount_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

     qualProcName = genQualProcName(g_sectionIndexAliasLrt, unQualProcedureName & "List", ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader("SP for LRT-" & UCase(un) & "LOCK by list of OIDs", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcName
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "classIdOidList_in", "CLOB(1M)", True, "'|'-separated list of pairs of 'classId,OID' to " & un & "lock")
     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being " & un & "locked")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
 
     genProcSectionHeader(fileNo, "declare conditions", , True)
     genCondDecl(fileNo, "notFound", "02000")
     genSpLogDecl(fileNo)
 
     genProcSectionHeader(fileNo, "declare variables")
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
 ' ### IF IVK ###
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "0")
 ' ### ENDIF IVK ###
     If forLock Then
       genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "0")
     End If

     genProcSectionHeader(fileNo, "declare statement")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
     genProcSectionHeader(fileNo, "declare continue handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"
 
     genSpLogProcEnter(fileNo, qualProcName, ddlType, , "classIdOidList_in", "rowCount_out")

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, IIf(forLock, tvTrue, tvNull), 1)
 
     genProcSectionHeader(fileNo, "initialize variables")
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"
     If forLock Then
       Print #fileNo, addTab(1); "SET v_lrtOid = "; g_activeLrtOidDdl; ";"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(1); "SET v_psOid  = "; g_activePsOidDdl; ";"
 ' ### ENDIF IVK ###

     If forLock Then
       genProcSectionHeader(fileNo, "verify that we have an active transaction")
       Print #fileNo, addTab(1); "IF "; gc_db2RegVarLrtOid; " = '' THEN"

       genSpLogProcEscape(fileNo, qualProcName, ddlType, , "classIdOidList_in", "rowCount_out")
       genSignalDdl("noLrt", fileNo, 2)

       Print #fileNo, addTab(1); "END IF;"
     End If

     genProcSectionHeader(fileNo, "for each OID process involved table(s) - with current MDS concepts there is exactly one table per OID")
     Print #fileNo, addTab(1); "FOR tabLoop AS"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "T."; g_anPdmFkSchemaName; " AS c_schemaName,"
     Print #fileNo, addTab(3); "T."; g_anPdmTypedTableName; "  AS c_tableName,"
     Print #fileNo, addTab(3); "O."; g_anCid; " AS c_classId,"
     Print #fileNo, addTab(3); "O."; g_anOid; " AS c_oid"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "TABLE ("; qualFuncNameParseClassIdOidList; "(classIdOidList_in)) AS O"
     Print #fileNo, addTab(2); "LEFT OUTER JOIN"
     Print #fileNo, addTab(3); qualPdmTableViewName; " T"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "T.ENTITY_ID = O."; g_anCid
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "T."; g_anPdmTypedTableName; " IS NULL"
     Print #fileNo, addTab(4); "OR"
     Print #fileNo, addTab(3); "("
     genDdlPdmEntityCheck(fileNo, 4, "T.")
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "T.PDM_"; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "T.PDM_POOLTYPE_ID = "; genPoolId(thisPoolIndex, ddlType)
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(2); "WITH UR"
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"

     genProcSectionHeader(fileNo, "make sure that we found a table", 2, True)
     Print #fileNo, addTab(2); "IF c_tableName IS NULL THEN"
     genSpLogProcEscape(fileNo, qualProcName, ddlType, 3, "classIdOidList_in", "rowCount_out")
     If un = "" Then
       genSignalDdlWithParms("noTableToLock", fileNo, 3, , , , , , , , , , "c_classId")
     Else
       genSignalDdlWithParms("noTableToUnLock", fileNo, 3, , , , , , , , , , "c_classId")
     End If
     Print #fileNo, addTab(2); "END IF;"

     Print #fileNo,
 ' ### IF IVK ###
     If forLock Then
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?,?,?)';"
     Else
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?,?)';"
     End If
 ' ### ELSE IVK ###
 '   If forLock Then
 '     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?,?)';"
 '   Else
 '     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '(?,?)';"
 '   End If
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE"
     Print #fileNo, addTab(3); "v_stmnt"
     Print #fileNo, addTab(2); "INTO"
     Print #fileNo, addTab(3); "v_rowCount"
     Print #fileNo, addTab(2); "USING"
     If forLock Then
       Print #fileNo, addTab(3); "v_lrtOid,"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(3); "v_psOid,"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(3); "c_oid"
     Print #fileNo, addTab(2); ";"
     Print #fileNo,
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);"

     Print #fileNo, addTab(1); "END FOR;"

     genSpLogProcExit(fileNo, qualProcName, ddlType, , "classIdOidList_in", "rowCount_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
 

     If forLock Then
  ' ####################################################################################################################
   ' rs15

     qualProcName = genQualProcName(g_sectionIndexAliasLrt, "FILLTEMPTABLE", ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader("SP for to store Oid / ClassId tuples in a temporary table", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcName
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "classIdOidList_in", "CLOB(1M)", True, "'|'-separated list of pairs of 'classId,OID' to " & un & "lock")
     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being inserted")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
 
     genProcSectionHeader(fileNo, "declare conditions", , True)
     genCondDecl(fileNo, "notFound", "02000")
     genCondDecl(fileNo, "alreadyExist", "42710")
     genSpLogDecl(fileNo)
 
     genProcSectionHeader(fileNo, "declare variables")
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
 ' ### IF IVK ###
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "0")
 ' ### ENDIF IVK ###
     If forLock Then
       genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "0")
     End If

     genProcSectionHeader(fileNo, "declare statement")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
     genProcSectionHeader(fileNo, "declare continue handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"
 
     genSpLogProcEnter(fileNo, qualProcName, ddlType, , "classIdOidList_in", "rowCount_out")

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, IIf(forLock, tvTrue, tvNull), 1)
 
     genProcSectionHeader(fileNo, "initialize variables")
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"
     If forLock Then
       Print #fileNo, addTab(1); "SET v_lrtOid = "; g_activeLrtOidDdl; ";"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(1); "SET v_psOid  = "; g_activePsOidDdl; ";"
 ' ### ENDIF IVK ###

     If forLock Then
       genProcSectionHeader(fileNo, "verify that we have an active transaction")
       Print #fileNo, addTab(1); "IF "; gc_db2RegVarLrtOid; " = '' THEN"

       genSpLogProcEscape(fileNo, qualProcName, ddlType, , "classIdOidList_in", "rowCount_out")
       genSignalDdl("noLrt", fileNo, 2)

       Print #fileNo, addTab(1); "END IF;"
     End If

     genDdlForTempPrivClassIdOid(fileNo)

     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); "SESSION.PRIVCLASSIDOID"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "O.CLASSID AS classId,"
     Print #fileNo, addTab(2); "O.OID AS oid"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "TABLE (VL6CMET.PARSECLASSOIDLIST(classIdOidList_in)) AS O;"
     Print #fileNo,
 
     Print #fileNo, addTab(2); "SET v_rowCount = ( SELECT COUNT(*) FROM SESSION.PRIVCLASSIDOID );"
     Print #fileNo,
 
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);"
     Print #fileNo,

     genSpLogProcExit(fileNo, qualProcName, ddlType, , "classIdOidList_in", "rowCount_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim


     qualProcName = genQualProcName(g_sectionIndexAliasLrt, unQualProcedureName & "ListTempTable", ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader("SP for LRT-" & UCase(un) & "LOCK by list of OIDs in temporary table", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcName
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being " & un & "locked")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
 
     genProcSectionHeader(fileNo, "declare conditions", , True)
     genCondDecl(fileNo, "notFound", "02000")
     genCondDecl(fileNo, "alreadyExist", "42710")
     genSpLogDecl(fileNo)
 
     genProcSectionHeader(fileNo, "declare variables")
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
 ' ### IF IVK ###
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "0")
 ' ### ENDIF IVK ###
     If forLock Then
       genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "0")
     End If
     genVarDecl(fileNo, "v_classId", "CHAR(5)", "NULL")

     genProcSectionHeader(fileNo, "declare statement")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
     genProcSectionHeader(fileNo, "declare continue handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"
 
     genSpLogProcEnter(fileNo, qualProcName, ddlType, , "classIdOidList_in", "rowCount_out")

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, IIf(forLock, tvTrue, tvNull), 1)
 
     genProcSectionHeader(fileNo, "initialize variables")
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"
     If forLock Then
       Print #fileNo, addTab(1); "SET v_lrtOid = "; g_activeLrtOidDdl; ";"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(1); "SET v_psOid  = "; g_activePsOidDdl; ";"
 ' ### ENDIF IVK ###

     If forLock Then
       genProcSectionHeader(fileNo, "verify that we have an active transaction")
       Print #fileNo, addTab(1); "IF "; gc_db2RegVarLrtOid; " = '' THEN"

       genSpLogProcEscape(fileNo, qualProcName, ddlType, , "classIdOidList_in", "rowCount_out")
       genSignalDdl("noLrt", fileNo, 2)

       Print #fileNo, addTab(1); "END IF;"
     End If

     Print #fileNo,
     genDdlForTempPrivClassIdOid(fileNo)
     genProcSectionHeader(fileNo, "make sure that there is a table for each classId")
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "O."; g_anCid
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_classId"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "SESSION.PRIVCLASSIDOID AS O"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "O."; g_anCid; " NOT IN ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "T.ENTITY_ID"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualPdmTableViewName; " T"
     Print #fileNo, addTab(3); "WHERE"
     genDdlPdmEntityCheck(fileNo, 4, "T.")
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "T.PDM_"; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "T.PDM_POOLTYPE_ID = "; genPoolId(thisPoolIndex, ddlType)
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); "FETCH FIRST 1 ROWS ONLY"
     Print #fileNo, addTab(1); "WITH UR"
     Print #fileNo, addTab(1); "FOR READ ONLY;"
 
     Print #fileNo, addTab(1); "IF v_classId IS NOT NULL THEN"
     genSpLogProcEscape(fileNo, qualProcName, ddlType, 3, "classIdOidList_in", "rowCount_out")
     genSignalDdlWithParms("noTableToLock", fileNo, 3, , , , , , , , , , "v_classId")
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader(fileNo, "for each classId in the temp table process involved table(s)")
     Print #fileNo, addTab(1); "FOR tabLoop AS"
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); "T."; g_anPdmFkSchemaName; " AS c_schemaName,"
     Print #fileNo, addTab(3); "T."; g_anPdmTypedTableName; "  AS c_tableName"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "SESSION.PRIVCLASSIDOID AS O"
     Print #fileNo, addTab(2); "LEFT OUTER JOIN"
     Print #fileNo, addTab(3); qualPdmTableViewName; " T"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "T.ENTITY_ID = O."; g_anCid
     Print #fileNo, addTab(2); "WHERE"
     genDdlPdmEntityCheck(fileNo, 3, "T.")
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "T.PDM_"; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "T.PDM_POOLTYPE_ID = "; genPoolId(thisPoolIndex, ddlType)
     Print #fileNo, addTab(2); "WITH UR"
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"

     Print #fileNo,
 ' ### IF IVK ###
     If forLock Then
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '_TEMPTABLE(?,?,?)';"
     Else
       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '_TEMPTABLE(?,?)';"
     End If
 ' ### ELSE IVK ###
 '   If forLock Then
 '     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '_TEMPTABLE(?,?)';"
 '   Else
 '     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; unQualProcedureName; "_' || c_tableName || '_TEMPTABLE(?)';"
 '   End If
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE"
     Print #fileNo, addTab(3); "v_stmnt"
     Print #fileNo, addTab(2); "INTO"
     Print #fileNo, addTab(3); "v_rowCount"
     Print #fileNo, addTab(2); "USING"
     If forLock Then
       Print #fileNo, addTab(3); "v_lrtOid,"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(3); "v_psOid"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(2); ";"
     Print #fileNo,
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);"
     Print #fileNo,

     Print #fileNo, addTab(1); "END FOR;"

     genSpLogProcExit(fileNo, qualProcName, ddlType, , "classIdOidList_in", "rowCount_out")
     Print #fileNo,

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
 
     End If

     unQualProcedureName = "LRTUNLOCK"
     unQualProcedureShortName = "ULK"
     un = "un"

   Next i

   Dim qualTabNameChangeLog As String
   Dim qualTabNameChangeLogNl As String

   qualTabNameChangeLog = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex)
   qualTabNameChangeLogNl = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex, , , , True)

 ' ### IF IVK ###
   Dim qualTabNameJob As String
   qualTabNameJob = genQualTabNameByClassIndex(g_classIndexJob, ddlType, thisOrgIndex, thisPoolIndex)

 ' ### ENDIF IVK ###
   Dim qualProcNameLrtCommitIntern As String
   Dim qualProcNameLrtCommitExtern As String
   Dim isPrimaryOrg As Boolean
   isPrimaryOrg = (thisOrgIndex = g_primaryOrgIndex)

   Dim useLrtOidListParam As Boolean
   Dim lrtOidRefVar As String
   For i = 1 To 2
     useLrtOidListParam = (i = 2)
     If useLrtOidListParam Then
       lrtOidRefVar = "v_lrtOid"
       qualProcNameLrtCommitExtern = genQualProcName(g_sectionIndexAliasLrt, spnLrtCommitList, ddlType, thisOrgIndex, thisPoolIndex)
       qualProcNameLrtCommitIntern = genQualProcName(g_sectionIndexLrt, spnLrtCommitList, ddlType, thisOrgIndex, thisPoolIndex)
 ' todo: move the internal procedure to 'internal schema' / remove the following statement
       qualProcNameLrtCommitIntern = qualProcNameLrtCommitExtern
     Else
       lrtOidRefVar = "lrtOid_in"
       qualProcNameLrtCommitExtern = genQualProcName(g_sectionIndexAliasLrt, spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex)
       qualProcNameLrtCommitIntern = genQualProcName(g_sectionIndexLrt, spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex)
 ' todo: move the internal procedure to 'internal schema' / remove the following statement
       qualProcNameLrtCommitIntern = qualProcNameLrtCommitExtern
     End If

     ' ####################################################################################################################
     ' #    SP for COMMIT on LRT
     ' ####################################################################################################################

     printSectionHeader("SP for COMMIT an LRT", fileNo)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameLrtCommitIntern
     Print #fileNo, addTab(0); "("
     If useLrtOidListParam Then
       genProcParm(fileNo, "IN", "lrtOids_in", "VARCHAR(1000)", True, "','-separated list of OIDs of LRTs to commit")
     Else
       genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to commit")
     End If
 ' ### IF IVK ###
     genProcParm(fileNo, "IN", "autoPriceSetProductive_in", g_dbtBoolean, True, "specifies whether prices are set productive automatically")
 ' ### ENDIF IVK ###
     genProcParm(fileNo, "IN", "genChangelog_in", g_dbtBoolean, True, "generate ChangeLog-records if and only if this parameter is '1'")
 ' ### IF IVK ###
     genProcParm(fileNo, "IN", "forceGenWorkSpace_in", g_dbtBoolean, True, "force call to GEN_WORKSPACE if and only if this parameter is '1'")
     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", True, "number of rows affected by the commit")
     genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", True, "in case of error of GEN_WORKSPACE: provides information about the error context")
     genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", True, "in case of error of GEN_WORKSPACE: JAVA stack trace")
     genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE")
 ' ### ELSE IVK ###
 '   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by the commit"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare variables")
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
     genVarDecl(fileNo, "v_lrtCount", "INTEGER", "0")
 ' ### IF IVK ###
     If Not isPrimaryOrg Then
       genVarDecl(fileNo, "v_isCentralDataTransfer", g_dbtBoolean, gc_dbFalse)
     End If
 ' ### ENDIF IVK ###
     genVarDecl(fileNo, "v_jobCount", "INTEGER", "0")
     genVarDecl(fileNo, "v_cdUserId", g_dbtUserId, "NULL")
     genVarDecl(fileNo, "v_opType", "INTEGER", CStr(lrtStatusCreated))
     genVarDecl(fileNo, "v_commitTs", "TIMESTAMP", "NULL")
     genVarDecl(fileNo, "v_commitEndTs", "TIMESTAMP", "NULL")
 ' ### IF IVK ###
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_settingManActCP", g_dbtBoolean, gc_dbFalse)
     genVarDecl(fileNo, "v_settingManActTP", g_dbtBoolean, gc_dbFalse)
     genVarDecl(fileNo, "v_settingManActSE", g_dbtBoolean, gc_dbFalse)
     genVarDecl(fileNo, "v_settingSelRelease", g_dbtBoolean, gc_dbFalse)
 ' ### ENDIF IVK ###
     genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
     genSpLogDecl(fileNo)

     genProcSectionHeader(fileNo, "declare statement")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")

     genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, thisPoolIndex, ddlType, 1, True, , True, True)
 
 ' ### IF IVK ###
     If useLrtOidListParam Then
       genSpLogProcEnter(fileNo, qualProcNameLrtCommitIntern, ddlType, , "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     Else
       genSpLogProcEnter(fileNo, qualProcNameLrtCommitIntern, ddlType, , "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     End If
 ' ### ELSE IVK ###
 '   If useLrtOidListParam Then
 '     genSpLogProcEnter fileNo, qualProcNameLrtCommitIntern, ddlType, , "'lrtOids_in", "genChangelog_in", "rowCount_out"
 '   Else
 '     genSpLogProcEnter fileNo, qualProcNameLrtCommitIntern, ddlType, , "lrtOid_in", "genChangelog_in", "rowCount_out"
 '   End If
 ' ### ENDIF IVK ###

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

     genProcSectionHeader(fileNo, "initialize output parameter")
     Print #fileNo, addTab(1); "SET rowCount_out    = 0;"
 ' ### IF IVK ###
     Print #fileNo, addTab(1); "SET gwspError_out   = NULL;"
     Print #fileNo, addTab(1); "SET gwspInfo_out    = NULL;"
     Print #fileNo, addTab(1); "SET gwspWarning_out = NULL;"
 ' ### ENDIF IVK ###

     genProcSectionHeader(fileNo, "determine COMMIT timestamp", 1)
     Print #fileNo, addTab(1); "SET v_commitTs = CURRENT TIMESTAMP;"

     Dim offset As Integer
     offset = 0
     If useLrtOidListParam Then
       genProcSectionHeader(fileNo, "loop over all OIDs of LRTs in lrtOids_in")
       Print #fileNo, addTab(1); "FOR lrtLoop AS"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "CAST(ELEM AS "; g_dbtOid; ") AS "; lrtOidRefVar
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); "TABLE ( "; g_qualFuncNameStrElems; "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(2); "ORDER BY"
       Print #fileNo, addTab(3); "POSINDEX ASC"
       Print #fileNo, addTab(1); "DO"
       offset = 1
     End If
 
     genProcSectionHeader(fileNo, "verify that this is an existing transaction", offset + 1, useLrtOidListParam)
     Print #fileNo, addTab(offset + 1); "SELECT"
     Print #fileNo, addTab(offset + 2); "COUNT(*)"
     Print #fileNo, addTab(offset + 1); "INTO"
     Print #fileNo, addTab(offset + 2); "v_lrtCount"
     Print #fileNo, addTab(offset + 1); "FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameLrt; " L"
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); "L."; g_anOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); "WITH UR;"

     genProcSectionHeader(fileNo, "if this transaction does not exist, we need to quit", offset + 1)
     Print #fileNo, addTab(offset + 1); "IF (v_lrtCount = 0) THEN"
 
 ' ### IF IVK ###
     If useLrtOidListParam Then
       genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     Else
       genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     End If
 ' ### ELSE IVK ###
 '   If useLrtOidListParam Then
 '     genSpLogProcEscape fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "'lrtOids_in", "genChangelog_in", "rowCount_out"
 '   Else
 '     genSpLogProcEscape fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "lrtOid_in", "genChangelog_in", "rowCount_out"
 '   End If
 ' ### ENDIF IVK ###
 
     genSignalDdlWithParms("lrtNotExist", fileNo, offset + 2, , , , , , , , , , "RTRIM(CHAR(" & lrtOidRefVar & "))")
     Print #fileNo, addTab(offset + 1); "END IF;"

 ' ### IF IVK ###
     genProcSectionHeader(fileNo, "determine PS-OID" & IIf(isPrimaryOrg, "", " / isCentralDataTransfer"), offset + 1)
     Print #fileNo, addTab(offset + 1); "SELECT"
     Print #fileNo, addTab(offset + 2); "L."; g_anPsOid; ","
     If Not isPrimaryOrg Then
       Print #fileNo, addTab(offset + 2); "L."; g_anIsCentralDataTransfer; ","
     End If
     Print #fileNo, addTab(offset + 2); "U."; g_anUserId
     Print #fileNo, addTab(offset + 1); "INTO"
     Print #fileNo, addTab(offset + 2); "v_psOid,"
     If Not isPrimaryOrg Then
       Print #fileNo, addTab(offset + 2); "v_isCentralDataTransfer,"
     End If
     Print #fileNo, addTab(offset + 2); "v_cdUserId"
     Print #fileNo, addTab(offset + 1); "FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameLrt; " L"
     Print #fileNo, addTab(offset + 1); "LEFT OUTER JOIN"
     Print #fileNo, addTab(offset + 2); g_qualTabNameUser; " U"
     Print #fileNo, addTab(offset + 1); "ON"
     Print #fileNo, addTab(offset + 2); "L.UTROWN_OID = U."; g_anOid
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); "L."; g_anOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); "WITH UR;"

 ' ### ENDIF IVK ###
     genProcSectionHeader(fileNo, "verify that this transaction has not ended", offset + 1)
     Print #fileNo, addTab(offset + 1); "SELECT"
     Print #fileNo, addTab(offset + 2); "COUNT(*)"
     Print #fileNo, addTab(offset + 1); "INTO"
     Print #fileNo, addTab(offset + 2); "v_lrtCount"
     Print #fileNo, addTab(offset + 1); "FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameLrt; " L"
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); "L."; g_anOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 3); "AND"
     Print #fileNo, addTab(offset + 2); "NOT (L."; g_anEndTime; " IS NULL)"
     Print #fileNo, addTab(offset + 1); "WITH UR;"

     genProcSectionHeader(fileNo, "if this transaction has already ended, we need to quit", offset + 1)
     Print #fileNo, addTab(offset + 1); "IF (v_lrtCount > 0) THEN"
 
 ' ### IF IVK ###
     If useLrtOidListParam Then
       genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, offset + 2, "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     Else
       genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, offset + 2, "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     End If
 ' ### ELSE IVK ###
 '   If useLrtOidListParam Then
 '     genSpLogProcEscape fileNo, qualProcNameLrtCommitIntern, ddlType, offset + 2, "'lrtOids_in", "genChangelog_in", "rowCount_out"
 '   Else
 '     genSpLogProcEscape fileNo, qualProcNameLrtCommitIntern, ddlType, offset + 2, "lrtOid_in", "genChangelog_in", "rowCount_out"
 '   End If
 ' ### ENDIF IVK ###

     genSignalDdlWithParms("lrtAlreadyCompleted", fileNo, offset + 2, , , , , , , , , , "RTRIM(CHAR(" & lrtOidRefVar & "))")
     Print #fileNo, addTab(offset + 1); "END IF;"

 ' ### IF IVK ###
     If Not generateFwkTest Then
       Dim qualTabNamePricePreferences As String
       qualTabNamePricePreferences = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType, thisOrgIndex)

       genProcSectionHeader(fileNo, "determine configuration settings", offset + 1)
       Print #fileNo, addTab(offset + 1); "SELECT"
       Print #fileNo, addTab(offset + 2); "ISMANUALLYACTIVATEDCODEPRICE,"
       Print #fileNo, addTab(offset + 2); "ISMANUALLYACTIVATEDTYPEPRICE,"
       Print #fileNo, addTab(offset + 2); "ISMANUALLYACTIVATEDSTANDARDEQU"
       Print #fileNo, addTab(offset + 1); "INTO"
       Print #fileNo, addTab(offset + 2); "v_settingManActCP,"
       Print #fileNo, addTab(offset + 2); "v_settingManActTP,"
       Print #fileNo, addTab(offset + 2); "v_settingManActSE"
       Print #fileNo, addTab(offset + 1); "FROM"
       Print #fileNo, addTab(offset + 2); qualTabNamePricePreferences
       Print #fileNo, addTab(offset + 1); "WHERE"
       Print #fileNo, addTab(offset + 2); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(offset + 1); ";"

       Print #fileNo,
       Print #fileNo, addTab(offset + 1); "SELECT"
       Print #fileNo, addTab(offset + 2); "USESELECTIVERELEASEPROCESS"
       Print #fileNo, addTab(offset + 1); "INTO"
       Print #fileNo, addTab(offset + 2); "v_settingSelRelease"
       Print #fileNo, addTab(offset + 1); "FROM"
       Print #fileNo, addTab(offset + 2); qualTabNameGeneralSettings
       Print #fileNo, addTab(offset + 1); "WHERE"
       Print #fileNo, addTab(offset + 2); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(offset + 1); ";"

       genProcSectionHeader(fileNo, "make sure that no job is running for this LRT", offset + 1)
       Print #fileNo, addTab(offset + 1); "SELECT"
       Print #fileNo, addTab(offset + 2); "COUNT(*)"
       Print #fileNo, addTab(offset + 1); "INTO"
       Print #fileNo, addTab(offset + 2); "v_jobCount"
       Print #fileNo, addTab(offset + 1); "FROM"
       Print #fileNo, addTab(offset + 2); qualTabNameJob
       Print #fileNo, addTab(offset + 1); "WHERE"
       Print #fileNo, addTab(offset + 2); g_anLrtOid; " = "; lrtOidRefVar
       Print #fileNo, addTab(offset + 1); ";"
       Print #fileNo,
       Print #fileNo, addTab(offset + 1); "IF v_jobCount > 0 THEN"

       If useLrtOidListParam Then
         genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
       Else
         genSpLogProcEscape(fileNo, qualProcNameLrtCommitIntern, ddlType, -(offset + 2), "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
       End If
       genSignalDdlWithParms("lrtComHasActiveJobs", fileNo, offset + 2, , , , , , , , , , "RTRIM(CHAR(" & lrtOidRefVar & "))")

       Print #fileNo, addTab(offset + 1); "END IF;"
     End If

 ' ### ENDIF IVK ###
     If useLrtOidListParam Then
       genProcSectionHeader(fileNo, "empty ChangeLog", offset + 1)
       Print #fileNo, addTab(offset + 1); "DELETE FROM "; gc_tempTabNameChangeLog; ";"
       Print #fileNo, addTab(offset + 1); "DELETE FROM "; gc_tempTabNameChangeLogNl; ";"
     End If

 ' ### IF IVK ###
     genProcSectionHeader(fileNo, "for operation INSERT and DELETE loop over tables supporting pre-processing of LRT-Commit", offset + 1)
     Print #fileNo, addTab(offset + 1); "SET v_opType = "; CStr(lrtStatusDeleted); ";"
 
     Print #fileNo, addTab(offset + 1); "WHILE v_opType IS NOT NULL DO"

     Print #fileNo, addTab(offset + 2); "FOR tabLoop AS"
     Print #fileNo, addTab(offset + 3); "WITH"
     Print #fileNo, addTab(offset + 4); "V_Tab"
     Print #fileNo, addTab(offset + 3); "("
     Print #fileNo, addTab(offset + 4); "schemaName,"
     Print #fileNo, addTab(offset + 4); "tableName,"
     Print #fileNo, addTab(offset + 4); "seqNo"
     Print #fileNo, addTab(offset + 3); ")"
     Print #fileNo, addTab(offset + 3); "AS"
     Print #fileNo, addTab(offset + 3); "("
     Print #fileNo, addTab(offset + 4); "SELECT DISTINCT"
     Print #fileNo, addTab(offset + 5); "SCHEMANAME,"
     Print #fileNo, addTab(offset + 5); g_anPdmTableName; ","
     Print #fileNo, addTab(offset + 5); "SEQNO"
     Print #fileNo, addTab(offset + 4); "FROM"
     Print #fileNo, addTab(offset + 5); qualViewNamePdmTabs
     Print #fileNo, addTab(offset + 4); "WHERE"
     Print #fileNo, addTab(offset + 5); g_anInLrt; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 4); "AND"
     Print #fileNo, addTab(offset + 5); "OPID = v_opType"
     Print #fileNo, addTab(offset + 4); "AND"
     Print #fileNo, addTab(offset + 5); g_anLdmIsGen; " = "; gc_dbFalse
     Print #fileNo, addTab(offset + 4); "AND"
     Print #fileNo, addTab(offset + 5); g_anLdmIsNl; " = "; gc_dbFalse
     Print #fileNo, addTab(offset + 4); "AND"
     Print #fileNo, addTab(offset + 5); g_anAcmUseLrtCommitPreprocess; " = "; gc_dbTrue
     Print #fileNo, addTab(offset + 3); ")"
     Print #fileNo, addTab(offset + 3); "SELECT"
     Print #fileNo, addTab(offset + 4); "schemaName AS c_schemaName,"
     Print #fileNo, addTab(offset + 4); "tableName  AS c_tableName"
     Print #fileNo, addTab(offset + 3); "FROM"
     Print #fileNo, addTab(offset + 4); "V_Tab"
     Print #fileNo, addTab(offset + 3); "ORDER BY"
     genProcSectionHeader(fileNo, "invert sequence of tables processed for 'DELETE'", offset + 4, True)
     Print #fileNo, addTab(offset + 4); "(CASE WHEN v_opType = "; CStr(lrtStatusDeleted); " THEN -1 ELSE 1 END) * seqNo ASC"
     Print #fileNo, addTab(offset + 3); "WITH UR"
     Print #fileNo, addTab(offset + 3); "FOR READ ONLY"
     Print #fileNo, addTab(offset + 2); "DO"

     Print #fileNo, addTab(offset + 3); "SET v_stmntTxt = 'CALL ' || c_schemaName || '."; UCase(spnLrtCommitPreProc); "_' || c_tableName || '(?,?,?,?,?,?)';"

     Print #fileNo,

     Print #fileNo, addTab(offset + 3); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,

     Print #fileNo, addTab(offset + 3); "EXECUTE"
     Print #fileNo, addTab(offset + 4); "v_stmnt"
     Print #fileNo, addTab(offset + 3); "INTO"
     Print #fileNo, addTab(offset + 4); "v_rowCount"
     Print #fileNo, addTab(offset + 3); "USING"
     Print #fileNo, addTab(offset + 4); lrtOidRefVar; ","
     Print #fileNo, addTab(offset + 4); "v_cdUserId,"
     Print #fileNo, addTab(offset + 4); "v_psOid,"
     Print #fileNo, addTab(offset + 4); "v_opType,"
     Print #fileNo, addTab(offset + 4); "v_commitTs"
     Print #fileNo, addTab(offset + 3); ";"

     Print #fileNo, addTab(offset + 2); "END FOR;"
     Print #fileNo,
     Print #fileNo, addTab(offset + 2); "SET v_opType = (CASE v_opType WHEN "; CStr(lrtStatusDeleted); " THEN "; CStr(lrtStatusCreated); " WHEN "; CStr(lrtStatusCreated); " THEN "; CStr(lrtStatusUpdated); " ELSE NULL END);"
     Print #fileNo, addTab(offset + 1); "END WHILE;"

 ' ### ENDIF IVK ###
     genProcSectionHeader(fileNo, "for each operation loop over tables to generate Change Log", offset + 1)

     Print #fileNo, addTab(offset + 1); "IF genChangelog_in = 1 THEN"

     Print #fileNo, addTab(offset + 2); "SET v_opType = "; CStr(lrtStatusDeleted); ";"
 
     Print #fileNo, addTab(offset + 2); "WHILE v_opType IS NOT NULL DO"

     Print #fileNo, addTab(offset + 3); "FOR tabLoop AS"
     Print #fileNo, addTab(offset + 4); "WITH"
     Print #fileNo, addTab(offset + 5); "V_Tab"
     Print #fileNo, addTab(offset + 4); "("
     Print #fileNo, addTab(offset + 5); "schemaName,"
     Print #fileNo, addTab(offset + 5); "tableName,"
     Print #fileNo, addTab(offset + 5); "isNl,"
     Print #fileNo, addTab(offset + 5); "seqNo"
     Print #fileNo, addTab(offset + 4); ")"
     Print #fileNo, addTab(offset + 4); "AS"
     Print #fileNo, addTab(offset + 4); "("
     Print #fileNo, addTab(offset + 5); "SELECT"
     Print #fileNo, addTab(offset + 6); "SCHEMANAME,"
     Print #fileNo, addTab(offset + 5); "(CASE WHEN LENGTH("; g_anPdmTableName; ") > 8 and SUBSTR("; g_anPdmTableName; ", LENGTH("; g_anPdmTableName; ")- 8 + 1, 8) = '_NL_TEXT' THEN SUBSTR("; g_anPdmTableName; ", 1, LENGTH("; g_anPdmTableName; ")- 8) ELSE "; g_anPdmTableName; " END) AS c_tableName,"
     Print #fileNo, addTab(offset + 6); g_anLdmIsNl; ","
     Print #fileNo, addTab(offset + 6); "SEQNO"
     Print #fileNo, addTab(offset + 5); "FROM"
     Print #fileNo, addTab(offset + 6); qualViewNamePdmTabs
     Print #fileNo, addTab(offset + 5); "WHERE"
     Print #fileNo, addTab(offset + 6); g_anInLrt; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 5); "AND"
     Print #fileNo, addTab(offset + 6); "((OPID = v_opType) OR (v_opType = "; CStr(lrtStatusLocked); "))"
     Print #fileNo, addTab(offset + 5); "AND"
     Print #fileNo, addTab(offset + 6); g_anAcmIgnoreForChangelog; " = "; gc_dbFalse
     Print #fileNo, addTab(offset + 5); "ORDER BY"
     genProcSectionHeader(fileNo, "invert sequence of tables processed for 'DELETE'", offset + 5, True)
     Print #fileNo, addTab(offset + 5); "(CASE WHEN v_opType = "; CStr(lrtStatusDeleted); " THEN -1 ELSE 1 END) * seqNo ASC"
     Print #fileNo, addTab(offset + 4); ")"
     Print #fileNo, addTab(offset + 4); "SELECT DISTINCT"
     Print #fileNo, addTab(offset + 5); "schemaName AS c_schemaName,"
     Print #fileNo, addTab(offset + 5); g_anPdmTableName; " AS c_tableName"
     Print #fileNo, addTab(offset + 4); "FROM"
     Print #fileNo, addTab(offset + 5); "V_Tab"
     Print #fileNo, addTab(offset + 4); "WITH UR"
     Print #fileNo, addTab(offset + 4); "FOR READ ONLY"
     Print #fileNo, addTab(offset + 3); "DO"

 ' ### IF IVK ###
     Print #fileNo, addTab(offset + 4); "SET v_stmntTxt = 'CALL ' || c_schemaName || '."; UCase(spnLrtGenChangelog); "_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?,"; IIf(isPrimaryOrg, "", "?,"); "?)';"
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(offset + 4); "SET v_stmntTxt = 'CALL ' || c_schemaName || '."; UCase(spnLrtGenChangelog); "_' || c_tableName || '(?,?,?,?)';"
 ' ### ENDIF IVK ###
     Print #fileNo,

     Print #fileNo, addTab(offset + 4); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,

     Print #fileNo, addTab(offset + 4); "EXECUTE"
     Print #fileNo, addTab(offset + 5); "v_stmnt"
     Print #fileNo, addTab(offset + 4); "INTO"
     Print #fileNo, addTab(offset + 5); "v_rowCount"
     Print #fileNo, addTab(offset + 4); "USING"
     Print #fileNo, addTab(offset + 5); lrtOidRefVar; ","
 ' ### IF IVK ###
     Print #fileNo, addTab(offset + 5); "v_psOid,"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(offset + 5); "v_cdUserId,"
     Print #fileNo, addTab(offset + 5); "v_opType,"
 ' ### IF IVK ###
     Print #fileNo, addTab(offset + 5); "v_commitTs,"
     Print #fileNo, addTab(offset + 5); "autoPriceSetProductive_in,"
     Print #fileNo, addTab(offset + 5); "v_settingManActCP,"
     Print #fileNo, addTab(offset + 5); "v_settingManActTP,"
     Print #fileNo, addTab(offset + 5); "v_settingManActSE,"
     If isPrimaryOrg Then
       Print #fileNo, addTab(offset + 5); "v_settingSelRelease"
     Else
       Print #fileNo, addTab(offset + 5); "v_settingSelRelease,"
       Print #fileNo, addTab(offset + 5); "v_isCentralDataTransfer"
     End If
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(offset + 5); "v_commitTs"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(offset + 4); ";"

     Print #fileNo, addTab(offset + 3); "END FOR;"
     Print #fileNo,
     Print #fileNo, addTab(offset + 3); "SET v_opType = (CASE v_opType WHEN "; CStr(lrtStatusDeleted); " THEN "; CStr(lrtStatusCreated); " WHEN "; CStr(lrtStatusCreated); " THEN "; CStr(lrtStatusUpdated); " WHEN "; CStr(lrtStatusUpdated); " THEN "; CStr(lrtStatusLocked); " ELSE NULL END);"
     Print #fileNo, addTab(offset + 2); "END WHILE;"

     genPersistChangeLogDdl(_
       fileNo, g_classIndexChangeLog, qualTabNameChangeLog, gc_tempTabNameChangeLog, qualTabNameChangeLogNl, gc_tempTabNameChangeLogNl, _
       qualSeqNameOid, ddlType, thisOrgIndex, thisPoolIndex, offset + 2, eclLrt, qualNlTabName, lrtOidRefVar)

     Print #fileNo, addTab(offset + 1); "END IF;"

     genProcSectionHeader(fileNo, "for each operation <> 'locked' loop over tables to LRT-commit (sequence: DELETE -> CREATE -> UPDATE)", offset + 1)
     Print #fileNo, addTab(offset + 1); "SET v_opType = "; CStr(lrtStatusDeleted); ";"
     Print #fileNo, addTab(offset + 1); "WHILE v_opType IS NOT NULL DO"

     Print #fileNo, addTab(offset + 2); "FOR tabLoop AS"
     Print #fileNo, addTab(offset + 3); "WITH"
     Print #fileNo, addTab(offset + 4); "V_Tab"
     Print #fileNo, addTab(offset + 3); "("
     Print #fileNo, addTab(offset + 4); "schemaName,"
     Print #fileNo, addTab(offset + 4); "tableName,"
     Print #fileNo, addTab(offset + 4); "seqNo"
     Print #fileNo, addTab(offset + 3); ")"
     Print #fileNo, addTab(offset + 3); "AS"
     Print #fileNo, addTab(offset + 4); "("
     Print #fileNo, addTab(offset + 3); "SELECT"
     Print #fileNo, addTab(offset + 4); "SCHEMANAME,"
     Print #fileNo, addTab(offset + 4); "(CASE WHEN LENGTH("; g_anPdmTableName; ") > 8 and SUBSTR("; g_anPdmTableName; ", LENGTH("; g_anPdmTableName; ")- 8 + 1, 8) = '_NL_TEXT' THEN SUBSTR("; g_anPdmTableName; ", 1, LENGTH("; g_anPdmTableName; ")- 8) ELSE "; g_anPdmTableName; " END),"
     Print #fileNo, addTab(offset + 4); "SEQNO"
     Print #fileNo, addTab(offset + 3); "FROM"
     Print #fileNo, addTab(offset + 4); qualViewNamePdmTabs
     Print #fileNo, addTab(offset + 3); "WHERE"
     Print #fileNo, addTab(offset + 4); g_anInLrt; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 3); "AND"
     Print #fileNo, addTab(offset + 4); "OPID = v_opType"
     Print #fileNo, addTab(offset + 3); ")"
     Print #fileNo, addTab(offset + 3); "SELECT"
     Print #fileNo, addTab(offset + 4); "schemaName AS c_schemaName,"
     Print #fileNo, addTab(offset + 4); g_anPdmTableName; " AS c_tableName,"
     Print #fileNo, addTab(offset + 4); "MIN(SEQNO) AS SEQNO"
     Print #fileNo, addTab(offset + 3); "FROM"
     Print #fileNo, addTab(offset + 4); "V_Tab"
     Print #fileNo, addTab(offset + 3); "GROUP BY"
     Print #fileNo, addTab(offset + 4); "schemaName,"; g_anPdmTableName
     Print #fileNo, addTab(offset + 3); "ORDER BY"
     genProcSectionHeader(fileNo, "sequence of tables processed must be inverted for 'DELETE'", offset + 4, True)
     Print #fileNo, addTab(offset + 4); "(CASE WHEN v_opType = "; CStr(lrtStatusDeleted); " THEN -1 ELSE 1 END) * SEQNO ASC"
     Print #fileNo, addTab(offset + 3); "WITH UR"
     Print #fileNo, addTab(offset + 3); "FOR READ ONLY"
     Print #fileNo, addTab(offset + 2); "DO"

 ' ### IF IVK ###
     If isPrimaryOrg Then
       Print #fileNo, addTab(offset + 3); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?,?)' ;"
     Else
       Print #fileNo, addTab(offset + 3); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?,?,?)' ;"
     End If
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(offset + 3); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?)' ;"
 ' ### ENDIF IVK ###

     Print #fileNo,
     Print #fileNo, addTab(offset + 3); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(offset + 3); "EXECUTE"
     Print #fileNo, addTab(offset + 4); "v_stmnt"
     Print #fileNo, addTab(offset + 3); "INTO"
     Print #fileNo, addTab(offset + 4); "v_rowCount"
     Print #fileNo, addTab(offset + 3); "USING"
     Print #fileNo, addTab(offset + 4); lrtOidRefVar; ","
     Print #fileNo, addTab(offset + 4); "v_cdUserId,"
 ' ### IF IVK ###
     Print #fileNo, addTab(offset + 4); "v_psOid,"
     Print #fileNo, addTab(offset + 4); "v_opType,"
     Print #fileNo, addTab(offset + 4); "v_commitTs,"
     Print #fileNo, addTab(offset + 4); "autoPriceSetProductive_in,"
     Print #fileNo, addTab(offset + 4); "v_settingManActCP,"
     Print #fileNo, addTab(offset + 4); "v_settingManActTP,"
     Print #fileNo, addTab(offset + 4); "v_settingManActSE,"

     If isPrimaryOrg Then
       Print #fileNo, addTab(offset + 4); "v_settingSelRelease"
     Else
       Print #fileNo, addTab(offset + 4); "v_settingSelRelease,"
       Print #fileNo, addTab(offset + 4); "v_isCentralDataTransfer"
     End If
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(offset + 4); "v_opType,"
 '   Print #fileNo, addTab(offset + 4); "v_commitTs"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(offset + 3); ";"

     genProcSectionHeader(fileNo, "count number of committed rows", offset + 3)
     Print #fileNo, addTab(offset + 3); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);"

     Print #fileNo, addTab(offset + 2); "END FOR;"

     Print #fileNo,
     Print #fileNo, addTab(offset + 2); "SET v_opType = (CASE v_opType WHEN "; CStr(lrtStatusDeleted); " THEN "; CStr(lrtStatusCreated); " WHEN "; CStr(lrtStatusCreated); " THEN "; CStr(lrtStatusUpdated); " ELSE NULL END);"
     Print #fileNo, addTab(offset + 1); "END WHILE;"

     genProcSectionHeader(fileNo, "loop over tables to finish LRT-commit (unlock)", offset + 1)
     Print #fileNo, addTab(offset + 1); "SET v_opType = "; CStr(lrtStatusLocked); ";"
     Print #fileNo, addTab(offset + 1); "FOR tabLoop AS"
     Print #fileNo, addTab(offset + 2); "SELECT DISTINCT"
     Print #fileNo, addTab(offset + 3); "SCHEMANAME AS c_schemaName,"
     Print #fileNo, addTab(offset + 3); g_anPdmTableName; " AS c_tableName,"
     Print #fileNo, addTab(offset + 3); "SEQNO"
     Print #fileNo, addTab(offset + 2); "FROM"
     Print #fileNo, addTab(offset + 3); qualViewNamePdmTabs
     Print #fileNo, addTab(offset + 2); "WHERE"
     Print #fileNo, addTab(offset + 3); g_anInLrt; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 2); "AND"
     Print #fileNo, addTab(offset + 3); g_anLdmIsNl; " = "; gc_dbFalse
     Print #fileNo, addTab(offset + 2); "ORDER BY"
     genProcSectionHeader(fileNo, "sequence of tables processed must be inverted since we execute 'DELETE' during postprocessing", offset + 3, True)
     Print #fileNo, addTab(offset + 3); "SEQNO DESC"
     Print #fileNo, addTab(offset + 2); "WITH UR"
     Print #fileNo, addTab(offset + 2); "FOR READ ONLY"
     Print #fileNo, addTab(offset + 1); "DO"

 ' ### IF IVK ###
     If isPrimaryOrg Then
       Print #fileNo, addTab(offset + 2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?,?)' ;"
     Else
       Print #fileNo, addTab(offset + 2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?,?,?,?,?,?,?,?,?)' ;"
     End If
 ' ### ELSE IVK ###
 '     Print #fileNo, addTab(offset + 2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.LRTCOMMIT_' || c_tableName || '(?,?,?,?)' ;"
 ' ### ENDIF IVK ###

     Print #fileNo,
     Print #fileNo, addTab(offset + 2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(offset + 2); "EXECUTE"
     Print #fileNo, addTab(offset + 3); "v_stmnt"
     Print #fileNo, addTab(offset + 2); "INTO"
     Print #fileNo, addTab(offset + 3); "v_rowCount"
     Print #fileNo, addTab(offset + 2); "USING"
     Print #fileNo, addTab(offset + 3); lrtOidRefVar; ","
     Print #fileNo, addTab(offset + 3); "v_cdUserId,"
 ' ### IF IVK ###
     Print #fileNo, addTab(offset + 3); "v_psOid,"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(offset + 3); "v_opType,"
 ' ### IF IVK ###
     Print #fileNo, addTab(offset + 3); "v_commitTs,"
     Print #fileNo, addTab(offset + 3); "autoPriceSetProductive_in,"
     Print #fileNo, addTab(offset + 3); "v_settingManActCP,"
     Print #fileNo, addTab(offset + 3); "v_settingManActTP,"
     Print #fileNo, addTab(offset + 3); "v_settingManActSE,"
     If isPrimaryOrg Then
       Print #fileNo, addTab(offset + 3); "v_settingSelRelease"
     Else
       Print #fileNo, addTab(offset + 3); "v_settingSelRelease,"
       Print #fileNo, addTab(offset + 3); "v_isCentralDataTransfer"
     End If
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(offset + 3); "v_commitTs"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(offset + 2); ";"

     Print #fileNo, addTab(offset + 1); "END FOR;"

 ' ### IF IVK ###
     genProcSectionHeader(fileNo, "call GEN_WORKSPACE if required", offset + 1)

     If Not isPrimaryOrg Then
       If useLrtOidListParam Then
         Print #fileNo, addTab(offset + 1); "SELECT"
         Print #fileNo, addTab(offset + 2); "MAX(L."; g_anIsCentralDataTransfer; ")"
         Print #fileNo, addTab(offset + 1); "INTO"
         Print #fileNo, addTab(offset + 2); "v_isCentralDataTransfer"
         Print #fileNo, addTab(offset + 1); "FROM"
         Print #fileNo, addTab(offset + 2); qualTabNameLrt; " L"
         Print #fileNo, addTab(offset + 1); "INNER JOIN"
         Print #fileNo, addTab(offset + 2); "TABLE ( "; g_qualFuncNameStrElems; "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X"
         Print #fileNo, addTab(offset + 1); "ON"
         Print #fileNo, addTab(offset + 2); "L."; g_anOid; " = CAST(X.ELEM AS "; g_dbtOid; ")"
         Print #fileNo, addTab(offset + 1); "WHERE"
         Print #fileNo, addTab(offset + 2); "L."; g_anIsCentralDataTransfer; " = "; gc_dbTrue
         Print #fileNo, addTab(offset + 1); "WITH UR;"
         Print #fileNo,
       End If
     End If

     Print #fileNo, addTab(offset + 1); "IF "; IIf(isPrimaryOrg, "", "(v_isCentralDataTransfer = 1) OR "); "(forceGenWorkSpace_in = 1) THEN"

     genProcSectionHeader(fileNo, "determine OID of Organization", offset + 2, True)
     Print #fileNo, addTab(offset + 2); "SELECT ORGOID INTO v_orgOid FROM "; g_qualTabNamePdmOrganization; " WHERE ID = "; genOrgId(thisOrgIndex, ddlType, True); " WITH UR;"

     genCallGenWorkspaceDdl(fileNo, thisOrgIndex, thisPoolIndex, "v_orgOid", "v_psOid", g_pools.descriptors(thisPoolIndex).id, "gwspError_out", "gwspInfo_out", "gwspWarning_out", offset + 2, ddlType)
     Print #fileNo, addTab(offset + 1); "END IF;"

     genProcSectionHeader(fileNo, "keep track of Product Structures and Divisions involved in this LRT", offset + 1)
     Print #fileNo, addTab(offset + 1); "INSERT INTO"
     Print #fileNo, addTab(offset + 2); gc_tempTabNameChangeLogStatus
     Print #fileNo, addTab(offset + 1); "("
     Print #fileNo, addTab(offset + 2); "psOid,"
     Print #fileNo, addTab(offset + 2); "divisionOid"
     Print #fileNo, addTab(offset + 1); ")"
     Print #fileNo, addTab(offset + 1); "WITH"
     Print #fileNo, addTab(offset + 2); "V_PsDiv"
     Print #fileNo, addTab(offset + 1); "("
     Print #fileNo, addTab(offset + 2); "psOid,"
     Print #fileNo, addTab(offset + 2); "divisionOid"
     Print #fileNo, addTab(offset + 1); ")"
     Print #fileNo, addTab(offset + 1); "AS"
     Print #fileNo, addTab(offset + 1); "("
     Print #fileNo, addTab(offset + 2); "SELECT DISTINCT"
     Print #fileNo, addTab(offset + 3); g_anPsOid; ","
     Print #fileNo, addTab(offset + 3); "DIVISIONOID"
     Print #fileNo, addTab(offset + 2); "FROM"
     Print #fileNo, addTab(offset + 3); gc_tempTabNameChangeLog
     Print #fileNo, addTab(offset + 1); ")"
     Print #fileNo, addTab(offset + 1); "SELECT"
     Print #fileNo, addTab(offset + 2); "psOid,"
     Print #fileNo, addTab(offset + 2); "divisionOid"
     Print #fileNo, addTab(offset + 1); "FROM"
     Print #fileNo, addTab(offset + 2); "V_PsDiv"
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); "NOT EXISTS ("
     Print #fileNo, addTab(offset + 3); "SELECT"
     Print #fileNo, addTab(offset + 4); "1"
     Print #fileNo, addTab(offset + 3); "FROM"
     Print #fileNo, addTab(offset + 4); gc_tempTabNameChangeLogStatus; " CS"
     Print #fileNo, addTab(offset + 3); "WHERE"
     Print #fileNo, addTab(offset + 4); "COALESCE(V_PsDiv.psOid, -1) = COALESCE(CS.psOid, -1)"
     Print #fileNo, addTab(offset + 5); "AND"
     Print #fileNo, addTab(offset + 4); "COALESCE(V_PsDiv.divisionOid, -1) = COALESCE(CS.divisionOid, -1)"
     Print #fileNo, addTab(offset + 2); ")"
     Print #fileNo, addTab(offset + 1); ";"

 ' ### ENDIF IVK ###
     If useLrtOidListParam Then
       Print #fileNo, addTab(1); "END FOR;"
     End If

     If useLrtOidListParam Then
       genProcSectionHeader(fileNo, "loop again over all OIDs of LRTs in lrtOids_in to update meta information")
       Print #fileNo, addTab(1); "FOR lrtLoop AS"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "CAST(ELEM AS "; g_dbtOid; ") AS "; lrtOidRefVar
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); "TABLE ( "; g_qualFuncNameStrElems; "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(2); "ORDER BY"
       Print #fileNo, addTab(3); "POSINDEX ASC"
       Print #fileNo, addTab(1); "DO"

 ' ### IF IVK ###
       genProcSectionHeader(fileNo, "determine PS-OID" & IIf(isPrimaryOrg, "", " / isCentralDataTransfer"), offset + 1, True)
       Print #fileNo, addTab(offset + 1); "SELECT"
       Print #fileNo, addTab(offset + 2); "L."; g_anPsOid; ","
       If Not isPrimaryOrg Then
         Print #fileNo, addTab(offset + 2); "L."; g_anIsCentralDataTransfer; ","
       End If
       Print #fileNo, addTab(offset + 2); "U."; g_anUserId
       Print #fileNo, addTab(offset + 1); "INTO"
       Print #fileNo, addTab(offset + 2); "v_psOid,"
       If Not isPrimaryOrg Then
         Print #fileNo, addTab(offset + 2); "v_isCentralDataTransfer,"
       End If
       Print #fileNo, addTab(offset + 2); "v_cdUserId"
       Print #fileNo, addTab(offset + 1); "FROM"
       Print #fileNo, addTab(offset + 2); qualTabNameLrt; " L"
       Print #fileNo, addTab(offset + 1); "LEFT OUTER JOIN"
       Print #fileNo, addTab(offset + 2); g_qualTabNameUser; " U"
       Print #fileNo, addTab(offset + 1); "ON"
       Print #fileNo, addTab(offset + 2); "L.UTROWN_OID = U."; g_anOid
       Print #fileNo, addTab(offset + 1); "WHERE"
       Print #fileNo, addTab(offset + 2); "L."; g_anOid; " = "; lrtOidRefVar
       Print #fileNo, addTab(offset + 1); "WITH UR;"
 ' ### ENDIF IVK ###
     End If

 ' ### IF IVK ###
     If Not isPrimaryOrg And Not generateFwkTest Then
       genProcSectionHeader(fileNo, "set 'commit timestamp' for last 'central data transfer'", offset + 1)
       Print #fileNo, addTab(offset + 1); "IF (v_isCentralDataTransfer = 1) THEN"
       Print #fileNo, addTab(offset + 2); "UPDATE"
       Print #fileNo, addTab(offset + 3); qualTabNameGeneralSettings
       Print #fileNo, addTab(offset + 2); "SET"
       Print #fileNo, addTab(offset + 3); "LASTCENTRALDATATRANSFERCOMMIT = LASTCENTRALDATATRANSFERBEGIN"
       Print #fileNo, addTab(offset + 2); "WHERE"
       Print #fileNo, addTab(offset + 3); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(offset + 2); ";"

       genProcSectionHeader(fileNo, "cleanup FTO-CONFLICT-table", offset + 2)
       Print #fileNo, addTab(offset + 2); "DELETE FROM"
       Print #fileNo, addTab(offset + 3); qualTabNameConflict
       Print #fileNo, addTab(offset + 2); "WHERE"
       Print #fileNo, addTab(offset + 3); "CLRLRT_OID = "; lrtOidRefVar
       Print #fileNo, addTab(offset + 2); ";"
       Print #fileNo, addTab(offset + 1); "END IF;"
     End If

 ' ### ENDIF IVK ###
     genProcSectionHeader(fileNo, "cleanup table """ & qualTabNameLrtAffectedEntity & """", offset + 1)
     Print #fileNo, addTab(offset + 1); "DELETE FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); g_anLrtOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); ";"

     Print #fileNo,
     Print #fileNo, addTab(offset + 1); "SET v_commitEndTs = CURRENT TIMESTAMP;"

     genProcSectionHeader(fileNo, "mark this LRT as 'committed'", offset + 1)
     Print #fileNo, addTab(offset + 1); "UPDATE"
     Print #fileNo, addTab(offset + 2); qualTabNameLrt
     Print #fileNo, addTab(offset + 1); "SET"
     Print #fileNo, addTab(offset + 2); g_anEndTime; " = v_commitTs,"
 ' ### IF IVK ###
     Print #fileNo, addTab(offset + 2); "COMMITTIME = v_commitEndTs,"
     Print #fileNo, addTab(offset + 2); g_anIsActive; " = "; gc_dbFalse
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(offset + 2); "COMMITTIME = CURRENT TIMESTAMP"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); g_anOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); ";"

     genProcSectionHeader(fileNo, "cleanup info associated to LRT", offset + 1)
     Print #fileNo, addTab(offset + 1); "DELETE FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameLrtExecStatus
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); g_anLrtOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); ";"

     If useLrtOidListParam Then
       Print #fileNo, addTab(1); "END FOR;"
     End If

 ' ### IF IVK ###
     genMaintainChangeLogStatusDdl(thisOrgIndex, thisPoolIndex, fileNo, "v_commitEndTs", 1, ddlType, True)

     If useLrtOidListParam Then
       genSpLogProcExit(fileNo, qualProcNameLrtCommitIntern, ddlType, , "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     Else
       genSpLogProcExit(fileNo, qualProcNameLrtCommitIntern, ddlType, , "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "forceGenWorkSpace_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
     End If
 ' ### ELSE IVK ###
 '   If useLrtOidListParam Then
 '     genSpLogProcExit fileNo, qualProcNameLrtCommitIntern, ddlType, , "'lrtOids_in", "genChangelog_in", "rowCount_out"
 '   Else
 '     genSpLogProcExit fileNo, qualProcNameLrtCommitIntern, ddlType, , "lrtOid_in", "genChangelog_in", "rowCount_out"
 '   End If
 ' ### ENDIF IVK ###

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
     ' #    SP for COMMIT on LRT
     ' ####################################################################################################################

 ' ### IF IVK ###
     Dim useGenChangeLogParam As Boolean
     Dim j As Integer
     For j = 1 To 2
       useGenChangeLogParam = (j = 2)
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
       printSectionHeader("SP for COMMIT an LRT", fileNo)

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcNameLrtCommitExtern
       Print #fileNo, addTab(0); "("

       If useLrtOidListParam Then
         genProcParm(fileNo, "IN", "lrtOids_in", "VARCHAR(1000)", True, "','-separated list of OIDs of LRTs to commit")
       Else
         genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to commit")
       End If

 ' ### IF IVK ###
       genProcParm(fileNo, "IN", "autoPriceSetProductive_in", g_dbtBoolean, True, "specifies whether prices are set productive automatically")
       If useGenChangeLogParam Then
         genProcParm(fileNo, "IN", "genChangelog_in", g_dbtBoolean, True, "generate ChangeLog-records if and only if this parameter is '1'")
       End If
       genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", True, "number of rows affected by the commit")
       genProcParm(fileNo, "OUT", "gwspError_out", "VARCHAR(256)", True, "in case of error of GEN_WORKSPACE: provides information about the error context")
       genProcParm(fileNo, "OUT", "gwspInfo_out", "VARCHAR(1024)", True, "in case of error of GEN_WORKSPACE: JAVA stack trace")
       genProcParm(fileNo, "OUT", "gwspWarning_out", "VARCHAR(512)", False, "(optionally) provides information helpful for interpreting the result of GEN_WORKSPACE")
 ' ### ELSE IVK ###
 '     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by the commit"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 0"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

       genSpLogDecl(fileNo, -1, True)

       If useLrtOidListParam Then
 ' ### IF IVK ###
         If useGenChangeLogParam Then
           genSpLogProcEnter(fileNo, qualProcNameLrtCommitExtern, ddlType, , "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")

           Print #fileNo, addTab(1); "CALL "; qualProcNameLrtCommitIntern; "(lrtOids_in, autoPriceSetProductive_in, genChangelog_in, 0, rowCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);"

           genSpLogProcExit(fileNo, qualProcNameLrtCommitExtern, ddlType, , "'lrtOids_in", "autoPriceSetProductive_in", "genChangelog_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         Else
           genSpLogProcEnter(fileNo, qualProcNameLrtCommitExtern, ddlType, , "'lrtOids_in", "autoPriceSetProductive_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")

           Print #fileNo, addTab(1); "CALL "; qualProcNameLrtCommitIntern; "(lrtOids_in, autoPriceSetProductive_in, 1, 0, rowCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);"

           genSpLogProcExit(fileNo, qualProcNameLrtCommitExtern, ddlType, , "'lrtOids_in", "autoPriceSetProductive_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         End If
 ' ### ELSE IVK ###
 '       genSpLogProcEnter fileNo, qualProcNameLrtCommitExtern, ddlType, , "'lrtOids_in", "rowCount_out"
 '
 '       Print #fileNo, addTab(1); "CALL "; qualProcNameLrtCommitIntern; "(lrtOids_in, 1, rowCount_out);"
 '
 '       genSpLogProcExit fileNo, qualProcNameLrtCommitExtern, ddlType, , "'lrtOids_in", "rowCount_out"
 ' ### ENDIF IVK ###
       Else
 ' ### IF IVK ###
         If useGenChangeLogParam Then
           genSpLogProcEnter(fileNo, qualProcNameLrtCommitExtern, ddlType, , "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")

           Print #fileNo, addTab(1); "CALL "; qualProcNameLrtCommitIntern; "(lrtOid_in, autoPriceSetProductive_in, genChangelog_in, 0, rowCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);"

           genSpLogProcExit(fileNo, qualProcNameLrtCommitExtern, ddlType, , "lrtOid_in", "autoPriceSetProductive_in", "genChangelog_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         Else
           genSpLogProcEnter(fileNo, qualProcNameLrtCommitExtern, ddlType, , "lrtOid_in", "autoPriceSetProductive_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")

           Print #fileNo, addTab(1); "CALL "; qualProcNameLrtCommitIntern; "(lrtOid_in, autoPriceSetProductive_in, 1, 0, rowCount_out, gwspError_out, gwspInfo_out, gwspWarning_out);"

           genSpLogProcExit(fileNo, qualProcNameLrtCommitExtern, ddlType, , "lrtOid_in", "autoPriceSetProductive_in", "rowCount_out", "'gwspError_out", "'gwspInfo_out", "'gwspWarning_out")
         End If
 ' ### ELSE IVK ###
 '       genSpLogProcEnter fileNo, qualProcNameLrtCommitExtern, ddlType, , "lrtOid_in", "rowCount_out"
 '
 '       Print #fileNo, addTab(1); "CALL "; qualProcNameLrtCommitIntern; "(lrtOid_in, 1, rowCount_out);"
 '
 '       genSpLogProcExit fileNo, qualProcNameLrtCommitExtern, ddlType, , "lrtOid_in", "rowCount_out"
 ' ### ENDIF IVK ###
       End If

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
 ' ### IF IVK ###
     Next j
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
   Next i

   ' ####################################################################################################################

   genLrtSupportDdlByPool2(fileNo, thisOrgIndex, thisPoolIndex, ddlType)
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub


 ' ### IF IVK ###
 Private Sub genActivateCodeForEntity( _
   acmEntityType As AcmAttrContainerType, _
   acmEntityIndex As Integer, _
   fileNo As Integer, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit
 
   Dim isSubjectToActivation As Boolean
   Dim isAggHead As Boolean
   Dim isPsTagged As Boolean
   Dim qualTabName As String
   Dim ignoreForChangelog As Boolean

   If acmEntityType = eactClass Then
       isSubjectToActivation = (g_classes.descriptors(acmEntityIndex).hasPriceAssignmentAggHead Or g_classes.descriptors(acmEntityIndex).hasPriceAssignmentSubClass) And g_classes.descriptors(acmEntityIndex).superClassIndex <= 0
       isAggHead = g_classes.descriptors(acmEntityIndex).isAggHead And Not forNl
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged And (usePsTagInNlTextTables Or Not forNl)
       ignoreForChangelog = g_classes.descriptors(acmEntityIndex).ignoreForChangelog
       qualTabName = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl)
   ElseIf acmEntityType = eactRelationship Then
       isSubjectToActivation = g_relationships.descriptors(acmEntityIndex).hasPriceAssignmentAggHead And g_relationships.descriptors(acmEntityIndex).maxLeftCardinality < 0 And g_relationships.descriptors(acmEntityIndex).maxRightCardinality < 0 And g_relationships.descriptors(acmEntityIndex).reusedRelIndex <= 0
       isAggHead = False
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged And (usePsTagInNlTextTables Or Not forNl)
       ignoreForChangelog = g_relationships.descriptors(acmEntityIndex).ignoreForChangelog
       qualTabName = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, , , forNl)
   Else
     Exit Sub
   End If

   If Not isSubjectToActivation Then
     Exit Sub
   End If

   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)

   If Not forNl And Not ignoreForChangelog Then
     Dim qualSeqNameOid As String
     qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

     genGenChangeLogRecordDdl(_
       acmEntityIndex, acmEntityType, qualTabName, "", qualSeqNameOid, gc_tempTabNameChangeLog, _
       "create changelog-records for status-update on table '" & qualTabName & "'", "", thisOrgIndex, thisPoolIndex, _
       fileNo, ddlType, , , g_anStatus, , etSmallint, eclPubMassUpdate, eacSetProdMeta, 1, _
       "T." & g_anStatus, "v_targetState", "", "NEXTVAL FOR " & qualSeqNameOid, "v_cdUserId", CStr(lrtStatusUpdated), , , , g_classIndexCodePriceAssignment)
   End If

   genProcSectionHeader(fileNo, "update status on table '" & qualTabName & "'")
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); qualTabName; " T"
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); "T."; g_anStatus; " = v_targetState,"
   If Not forNl Then
     Print #fileNo, addTab(2); "T."; g_anLastUpdateTimestamp; " = v_currentTimestamp,"
     Print #fileNo, addTab(2); "T."; g_anUpdateUser; " = v_cdUserId,"
   End If
   Print #fileNo, addTab(2); "T."; g_anVersionId; " = T."; g_anVersionId; " + 1"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "T."; g_anInLrt; " IS NULL"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "T."; g_anStatus; " < v_targetState"
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

    Print #fileNo, addTab(1); ";"

    genProcSectionHeader(fileNo, "count the number of affected rows", 1, True)
    Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
    Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

    If isAggHead And Not forGen And Not forNl Then
      Print #fileNo, addTab(1); "SET priceCount_out = priceCount_out + v_rowCount;"
    End If
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
 End Sub
 ' ### ENDIF IVK ###
 
 
 Private Sub genLrtSupportDdlByPool0( _
   fileNo As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
 
 Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLrtNlText As String
   qualTabNameLrtNlText = genQualNlTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLdmLrt As String
   qualTabNameLdmLrt = genQualTabNameByClassIndex(g_classIndexLrt, edtLdm, thisOrgIndex, thisPoolIndex)

   Dim lrtClassUseSurrogateKey As Boolean
   lrtClassUseSurrogateKey = g_classes.descriptors(g_classIndexLrt).useSurrogateKey

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLrtExecStatus As String
   qualTabNameLrtExecStatus = genQualTabNameByClassIndex(g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex)

 ' ### IF IVK ###
   If Not generateFwkTest Then
     Dim qualTabNameGeneralSettings As String
     qualTabNameGeneralSettings = genQualTabNameByClassIndex(g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex)

     Dim qualTabNameConflict As String
     qualTabNameConflict = genQualTabNameByClassIndex(g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex)
   End If

   Dim qualViewName As String
   Dim qualViewNameLdm  As String
 
 ' ####################################################################################################################
   ' #    create view for LRTExecStatus
   ' ####################################################################################################################

   qualViewName = genQualViewNameByClassIndex(g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("View for LRT-ExecStatus", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewName
   Print #fileNo, addTab(0); "("

   genAttrDeclsForClassRecursive(g_classIndexLrtExecStatus, , fileNo, ddlType, , , 1, , , , edomListNonLrt)
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(1); "SELECT"
 
   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation(transformation, 1)
   setAttributeMapping(transformation, 1, conLastOpTime, "MAX(" & g_anLastOpTime & ")")

   genTransformedAttrListForEntityWithColReuse(g_classIndexLrtExecStatus, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomListNonLrt)
 
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameLrtExecStatus
   Print #fileNo, addTab(1); "GROUP BY"
   Print #fileNo, addTab(2); g_anLrtOid; ""
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   If ddlType = edtPdm Then
     qualViewNameLdm = genQualViewNameByClassIndex(g_classIndexLrtExecStatus, edtLdm, , , , , , , , "ACTIVE")
 ' ### IF IVK ###
       genAliasDdl(g_classes.descriptors(g_classIndexLrtExecStatus).sectionIndex, g_classes.descriptors(g_classIndexLrtExecStatus).className, g_classes.descriptors(g_classIndexLrtExecStatus).isCommonToOrgs, g_classes.descriptors(g_classIndexLrtExecStatus).isCommonToPools, True, _
         qualViewNameLdm, qualViewName, g_classes.descriptors(g_classIndexLrtExecStatus).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, False, False, False, _
         "Active LRT View """ & g_classes.descriptors(g_classIndexLrtExecStatus).sectionName & "." & g_classes.descriptors(g_classIndexLrtExecStatus).className & """", , False, True)
 ' ### ELSE IVK ###
 '     genAliasDdl .sectionIndex, .className, .isCommonToOrgs, .isCommonToPools, True, _
 '       qualViewNameLdm, qualViewName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, _
 '       "Active LRT View """ & .sectionName & "." & .className & """", , False
 ' ### ENDIF IVK ###
   End If

   ' ####################################################################################################################
   ' #    create view to filter active LRTs
   ' ####################################################################################################################

   qualViewName = genQualViewNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "ACTIVE")

   printSectionHeader("View for filtering active LRTs", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewName
   Print #fileNo, addTab(0); "("

   genAttrDeclsForClassRecursive(g_classIndexLrt, , fileNo, ddlType, , , 1, , , , edomListNonLrt)
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(1); "SELECT"
 
   genAttrDeclsForClassRecursive(g_classIndexLrt, , fileNo, ddlType, , , 2, , , , edomListNonLrt)
 
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameLrt
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anEndTime; " IS NULL"
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '')"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("; g_anPsOid; " = "; g_activePsOidDdl; ")"
   Print #fileNo, addTab(2); ")"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
     qualViewNameLdm = genQualViewNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, edtLdm, , , , , , , , "ACTIVE")
 ' ### IF IVK ###
     genAliasDdl(g_classes.descriptors(g_classIndexLrt).sectionIndex, g_classes.descriptors(g_classIndexLrt).className, g_classes.descriptors(g_classIndexLrt).isCommonToOrgs, g_classes.descriptors(g_classIndexLrt).isCommonToPools, True, _
       qualViewNameLdm, qualViewName, g_classes.descriptors(g_classIndexLrt).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, False, False, False, _
       "Active LRT View """ & g_classes.descriptors(g_classIndexLrt).sectionName & "." & g_classes.descriptors(g_classIndexLrt).className & """", , False, True)
 ' ### ELSE IVK ###
 '   genAliasDdl .sectionIndex, .className, .isCommonToOrgs, .isCommonToPools, True, _
 '     qualViewNameLdm, qualViewName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, _
 '     "Active LRT View """ & .sectionName & "." & .className & """", , False
 ' ### ENDIF IVK ###
     ' gen Aliases for NL-Text table
 
     Dim qualNlTabName As String
     Dim qualNlTabNameLdm As String
     Dim nlObjName As String
     Dim nlObjNameShort As String
     nlObjName = genNlObjName(g_classes.descriptors(g_classIndexLrt).className)
     nlObjNameShort = genNlObjShortName(g_classes.descriptors(g_classIndexLrt).shortName)
     qualNlTabName = genQualTabNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
     qualNlTabNameLdm = genQualTabNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, edtLdm, thisOrgIndex, thisPoolIndex, , , , True)
 
 ' ### IF IVK ###
     genAliasDdl(g_classes.descriptors(g_classIndexLrt).sectionIndex, nlObjName, g_classes.descriptors(g_classIndexLrt).isCommonToOrgs, g_classes.descriptors(g_classIndexLrt).isCommonToPools, True, _
                 qualNlTabNameLdm, qualNlTabName, g_classes.descriptors(g_classIndexLrt).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, False, False, False, _
                 "NL-Table """ & g_classes.descriptors(g_classIndexLrt).sectionName & "." & nlObjName & """")

     genAliasDdl(g_classes.descriptors(g_classIndexLrt).sectionIndex, g_classes.descriptors(g_classIndexLrt).className, g_classes.descriptors(g_classIndexLrt).isCommonToOrgs, g_classes.descriptors(g_classIndexLrt).isCommonToPools, True, _
                 qualTabNameLdmLrt, qualTabNameLrt, g_classes.descriptors(g_classIndexLrt).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, False, False, False, _
                 "LDM-Table """ & g_classes.descriptors(g_classIndexLrt).sectionName & "." & g_classes.descriptors(g_classIndexLrt).className & """", , , , , , , , True)
 ' ### ELSE IVK ###
 '   genAliasDdl .sectionIndex, nlObjName, .isCommonToOrgs, .isCommonToPools, True, _
 '               qualNlTabNameLdm, qualNlTabName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, _
 '               "NL-Table """ & .sectionName & "." & nlObjName & """"
 '
 '   genAliasDdl .sectionIndex, .className, .isCommonToOrgs, .isCommonToPools, True, _
 '               qualTabNameLdmLrt, qualTabNameLrt, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatTable, False, False, _
 '               "LDM-Table """ & .sectionName & "." & .className & """", , , , , True
 ' ### ENDIF IVK ###


 ' ### ENDIF IVK ###

   Dim qualTriggerName As String

   ' ####################################################################################################################
   ' #    INSERT Trigger
   ' ####################################################################################################################
     nlObjName = genNlObjName(g_classes.descriptors(g_classIndexLrt).className)
     nlObjNameShort = genNlObjShortName(g_classes.descriptors(g_classIndexLrt).shortName)
     qualNlTabName = genQualTabNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , True)
     qualNlTabNameLdm = genQualTabNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, edtLdm, thisOrgIndex, thisPoolIndex, , , , True)
 ' ### IF IVK ###
     qualTriggerName = genQualTriggerNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "ACTIVE_INS")
 ' ### ELSE IVK ###
 '   qualTriggerName = genQualTriggerNameByClassIndex(.classIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "ACTIVE_INS")
 ' ### ENDIF IVK ###

     printSectionHeader("Insert-Trigger on table """ & qualTabNameLrt & """ (ACM-class """ & g_classes.descriptors(g_classIndexLrt).sectionName & "." & g_classes.descriptors(g_classIndexLrt).className & """)", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE TRIGGER"
   Print #fileNo, addTab(1); qualTriggerName
   Print #fileNo, addTab(0); "INSTEAD OF INSERT ON"
   Print #fileNo, addTab(1); qualViewName
   Print #fileNo, addTab(0); "REFERENCING"
   Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
   Print #fileNo, addTab(0); "FOR EACH ROW"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genSignalDdl("insertNotAllowed", fileNo, 1, g_classes.descriptors(g_classIndexLrt).className)
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, gc_sqlCmdDelim
 
   ' ####################################################################################################################

 ' ### IF IVK ###
   If usePsTagInNlTextTables Then
       qualTriggerName = _
         genQualTriggerNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , True, , , , "NL_INS")

       printSectionHeader("Insert-Trigger on table """ & qualTabNameLrtNlText & """ (ACM-class """ & g_classes.descriptors(g_classIndexLrt).sectionName & "." & g_classes.descriptors(g_classIndexLrt).className & """)", fileNo)
 
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "NO CASCADE BEFORE INSERT ON"
     Print #fileNo, addTab(1); qualTabNameLrtNlText
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader(fileNo, "retrieve PS-Tag from registry-variable (if not explicitly set)", , True)
     Print #fileNo, addTab(1); "IF "; gc_newRecordName; "."; g_anPsOid; " IS NULL THEN"
     Print #fileNo, addTab(2); "SET "; gc_newRecordName; "."; g_anPsOid; " = "; g_activePsOidDdl; ";"
     Print #fileNo, addTab(1); "END IF;"
 
     Print #fileNo, addTab(0); "END"
     Print #fileNo, gc_sqlCmdDelim
   End If
 ' ### ENDIF IVK ###
 
   ' ####################################################################################################################
   ' #    UPDATE Trigger
   ' ####################################################################################################################
     qualTriggerName = genQualTriggerNameByClassIndex(g_classes.descriptors(g_classIndexLrt).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "ACTIVE_UPD")

     printSectionHeader("Update-Trigger on table """ & qualTabNameLrt & """ (ACM-class """ & g_classes.descriptors(g_classIndexLrt).sectionName & "." & g_classes.descriptors(g_classIndexLrt).className & """)", fileNo)
 
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

 ' ### IF IVK ###
   genProcSectionHeader(fileNo, "if PS-tag does not 'fit' return with error")
   Print #fileNo, addTab(1); "IF ("; gc_db2RegVarPsOid; " <> '') THEN"
   Print #fileNo, addTab(2); "IF ("; gc_newRecordName; "."; g_anPsOid; " <> "; g_activePsOidDdl; ") OR"
   Print #fileNo, addTab(2); "   ("; gc_oldRecordName; "."; g_anPsOid; " <> "; g_activePsOidDdl; ") THEN"
   genSignalDdl("incorrPsTag", fileNo, 3)

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation(transformation, 3, , , , , g_anIsActive, "", g_anIsCentralDataTransfer, "", g_anIsInUseByFto, "")
 
   genTransformedAttrListForEntityWithColReuse(g_classIndexLrt, eactClass, transformation, tabColumns, fileNo, ddlType, , , , , , edomNone)

   genProcSectionHeader(fileNo, "make sure that update does not involve any of the 'non-updatable' columns")
   Dim firstCol As Boolean
   firstCol = True
   Print #fileNo, addTab(1); "IF"
   Dim i As Integer
   For i = 1 To tabColumns.numDescriptors
       If tabColumns.descriptors(i).columnCategory <> eacVid Then
         If Not firstCol Then
           Print #fileNo, addTab(3); "OR"
         End If

         If g_domains.descriptors(tabColumns.descriptors(i).dbDomainIndex).dataType = etTimestamp Then
           If tabColumns.descriptors(i).isNullable Then
             Print #fileNo, addTab(2); "COALESCE(VARCHAR_FORMAT("; gc_oldRecordName; "."; tabColumns.descriptors(i).columnName; ", 'YYYY-MM-DD HH24:MI:SS'),'') <> "; _
                                       "COALESCE(VARCHAR_FORMAT("; gc_newRecordName; "."; tabColumns.descriptors(i).columnName; ", 'YYYY-MM-DD HH24:MI:SS'),'')"
           Else
             Print #fileNo, addTab(2); "VARCHAR_FORMAT("; gc_oldRecordName; "."; tabColumns.descriptors(i).columnName; ", 'YYYY-MM-DD HH24:MI:SS') <> "; _
                                       "VARCHAR_FORMAT("; gc_newRecordName; "."; tabColumns.descriptors(i).columnName; ", 'YYYY-MM-DD HH24:MI:SS')"
           End If
         Else
           Print #fileNo, addTab(2); gc_oldRecordName; "."; tabColumns.descriptors(i).columnName; " <> "; gc_newRecordName; "."; tabColumns.descriptors(i).columnName
         End If

         firstCol = False
       End If
   Next i
   Print #fileNo, addTab(1); "THEN"

   genSignalDdl("updateNotAllowed", fileNo, 2, clnLrt)

   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "do the update")
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); qualTabNameLrt
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); g_anIsActive; " = COALESCE("; gc_newRecordName; "."; g_anIsActive; ", 0),"
   Print #fileNo, addTab(2); g_anIsCentralDataTransfer; " = (CASE "; g_anIsCentralDataTransfer; " WHEN 1 THEN 1 ELSE COALESCE("; gc_newRecordName; "."; g_anIsCentralDataTransfer; ", "; g_anIsCentralDataTransfer; ") END),"
   Print #fileNo, addTab(2); g_anIsInUseByFto; " = COALESCE("; gc_newRecordName; "."; g_anIsInUseByFto; ", "; g_anIsInUseByFto; "),"
   Print #fileNo, addTab(2); g_anVersionId; " = COALESCE("; gc_newRecordName; "."; g_anVersionId; ", "; g_anVersionId; ")"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anOid; " = "; gc_newRecordName; "."; g_anOid
   Print #fileNo, addTab(1); ";"
 ' ### ELSE IVK ###
 ' genSignalDdl "updateNotAllowed", fileNo, 1, clnLrt
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(0); "END"
   Print #fileNo, gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    DELETE Trigger
   ' ####################################################################################################################

   qualTriggerName = genQualTriggerNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "ACTIVE_DEL")

   printSectionHeader("Delete-Trigger on table """ & qualTabNameLrt & """ (ACM-class """ & snLrt & "." & clnLrt & """)", fileNo)
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE TRIGGER"
   Print #fileNo, addTab(1); qualTriggerName
   Print #fileNo, addTab(0); "INSTEAD OF DELETE ON"
   Print #fileNo, addTab(1); qualViewName
   Print #fileNo, addTab(0); "REFERENCING"
   Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
   Print #fileNo, addTab(0); "FOR EACH ROW"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genSignalDdl("deleteNotAllowed", fileNo, 1, clnLrt)
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    create view to determine LDM tables involved in an LRT
   ' ####################################################################################################################

   qualViewName = genQualViewName(g_sectionIndexLrt, vnLrtAffectedLdmTab, vsnLrtAffectedLdmTab, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("View for all LDM-tables related to a specific LRT", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewName
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anInLrt; ","
   Print #fileNo, addTab(1); g_anAcmOrParEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); "OPID,"
   Print #fileNo, addTab(1); g_anLdmSchemaName; ","
   Print #fileNo, addTab(1); g_anLdmTableName; ","
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); g_anAcmIgnoreForChangelog; ","
   Print #fileNo, addTab(1); g_anAcmUseLrtCommitPreprocess; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(1); g_anAcmDisplayCategory; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "SEQNO"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "AT."; g_anLrtOid; ","
   Print #fileNo, addTab(2); "AT."; g_anAcmOrParEntityId; ","
   Print #fileNo, addTab(2); "AT."; g_anAcmEntityType; ","
   Print #fileNo, addTab(2); "AT.OPID,"
   Print #fileNo, addTab(2); "LT."; g_anLdmSchemaName; ","
   Print #fileNo, addTab(2); "LT."; g_anLdmTableName; ","
   Print #fileNo, addTab(2); "LT."; g_anLdmIsNl; ","
   Print #fileNo, addTab(2); "LT."; g_anLdmIsGen; ","
   Print #fileNo, addTab(2); "AE."; g_anAcmIgnoreForChangelog; ","
   Print #fileNo, addTab(2); "AE."; g_anAcmUseLrtCommitPreprocess; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "AE."; g_anAcmDisplayCategory; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "LT."; g_anLdmFkSequenceNo
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameLrtAffectedEntity; " AT"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " AE"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "AT."; g_anAcmOrParEntityId; " = AE."; g_anAcmEntityId
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "AT."; g_anAcmEntityType; " = AE."; g_anAcmEntityType
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameLdmTable; " LT"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "LT."; g_anAcmEntitySection; " = AE."; g_anAcmEntitySection
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "LT."; g_anAcmEntityName; " = AE."; g_anAcmEntityName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "LT."; g_anAcmEntityType; " = AE."; g_anAcmEntityType
   If lrtDistinguishGenAndNlTextTabsInAffectedEntities Then
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "LT."; g_anLdmIsGen; " = AT."; g_anAcmIsGen
     Print #fileNo, addTab(4); "OR"
     Print #fileNo, addTab(3); "AT.OPID = "; CStr(lrtStatusLocked)
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "LT."; g_anLdmIsNl; " = AT."; g_anLdmIsNl
   End If
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "LT."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "AE."; g_anAcmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   If ddlType = edtPdm Then
     qualViewNameLdm = genQualViewName(g_sectionIndexLrt, vnLrtAffectedLdmTab, vsnLrtAffectedLdmTab, edtLdm)
 ' ### IF IVK ###
       genAliasDdl(g_sectionIndexLrt, vnLrtAffectedLdmTab, g_classes.descriptors(g_classIndexLrtAffectedEntity).isCommonToOrgs, g_classes.descriptors(g_classIndexLrtAffectedEntity).isCommonToPools, True, _
         qualViewNameLdm, qualViewName, g_classes.descriptors(g_classIndexLrtAffectedEntity).isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, False, False, False, _
         "LRT-AFFECTED-LDM-TABLES View """ & g_classes.descriptors(g_classIndexLrtAffectedEntity).sectionName & "." & g_classes.descriptors(g_classIndexLrtAffectedEntity).className & """", , True)
 ' ### ELSE IVK ###
 '     genAliasDdl g_sectionIndexLrt, vnLrtAffectedLdmTab, .isCommonToOrgs, .isCommonToPools, True, _
 '       qualViewNameLdm, qualViewName, .isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, False, False, _
 '       "LRT-AFFECTED-LDM-TABLES View """ & .sectionName & "." & .className & """", , True
 ' ### ENDIF IVK ###
   End If
 
 End Sub
 
 
 Private Sub genLrtSupportDdlByPool2( _
   fileNo As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualViewNamePdmTabs As String
   If ddlType = edtLdm Then
     qualViewNamePdmTabs = genQualViewName(g_sectionIndexLrt, vnLrtAffectedLdmTab, vsnLrtAffectedLdmTab, ddlType, thisOrgIndex, thisPoolIndex)
   Else
     qualViewNamePdmTabs = genQualViewName(g_sectionIndexLrt, vnLrtAffectedPdmTab, vsnLrtAffectedPdmTab, ddlType, thisOrgIndex, thisPoolIndex)
   End If

   Dim qualPdmTableViewName As String
   qualPdmTableViewName = genQualViewName(g_sectionIndexDbMeta, vnPdmTable, vnsPdmTable, ddlType)

   Dim isPrimaryOrg As Boolean
   isPrimaryOrg = (thisOrgIndex = g_primaryOrgIndex)

 ' ### IF IVK ###
   If Not generateFwkTest Then
     Dim qualTabNameGeneralSettings As String
     qualTabNameGeneralSettings = genQualTabNameByClassIndex(g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex)

     Dim qualTabNameConflict As String
     qualTabNameConflict = genQualTabNameByClassIndex(g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex)
   End If

   Dim qualTabNameJob As String
   qualTabNameJob = genQualTabNameByClassIndex(g_classIndexJob, ddlType, thisOrgIndex, thisPoolIndex)

 ' ### ENDIF IVK ###
   Dim qualProcNameLrtCommitIntern As String
   Dim qualProcNameLrtCommitExtern As String
   qualProcNameLrtCommitExtern = genQualProcName(g_sectionIndexAliasLrt, spnLrtCommit, ddlType, thisOrgIndex, thisPoolIndex)
   qualProcNameLrtCommitIntern = qualProcNameLrtCommitExtern

   Dim qualTabNameLrtExecStatus As String
   qualTabNameLrtExecStatus = genQualTabNameByClassIndex(g_classIndexLrtExecStatus, ddlType, thisOrgIndex, thisPoolIndex)

   ' ####################################################################################################################

   Dim qualProcNameLrtRollback As String
   Dim useLrtOidListParam As Boolean
   Dim lrtOidRefVar As String
   Dim i As Integer
   For i = 1 To 2
     useLrtOidListParam = (i = 2)
     If useLrtOidListParam Then
       lrtOidRefVar = "v_lrtOid"
       qualProcNameLrtRollback = genQualProcName(g_sectionIndexAliasLrt, spnLrtRollbackList, ddlType, thisOrgIndex, thisPoolIndex)
     Else
       lrtOidRefVar = "lrtOid_in"
       qualProcNameLrtRollback = genQualProcName(g_sectionIndexAliasLrt, spnLrtRollback, ddlType, thisOrgIndex, thisPoolIndex)
     End If

     ' ####################################################################################################################
     ' #    SP for ROLLBACK on LRT
     ' ####################################################################################################################
 
     printSectionHeader("SP for ROLLBACK on an LRT", fileNo)
 
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameLrtRollback
     Print #fileNo, addTab(0); "("
     If useLrtOidListParam Then
       genProcParm(fileNo, "IN", "lrtOids_in", "VARCHAR(1000)", True, "','-separated list of OIDs of LRTs to commit")
     Else
       genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to rollback")
     End If
     genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by the rollback")
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
 
     genProcSectionHeader(fileNo, "declare variables", , True)
     genSigMsgVarDecl(fileNo)
     genVarDecl(fileNo, "v_rowCount", "INTEGER", 0)
 ' ### IF IVK ###
     genVarDecl(fileNo, "v_jobCount", "INTEGER", "0")
 ' ### ENDIF IVK ###
     genVarDecl(fileNo, "v_lrtCount", "INTEGER", "0")
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_rollBackTs", "TIMESTAMP", "NULL")
 ' ### IF IVK ###
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
     If Not isPrimaryOrg Then
       genVarDecl(fileNo, "v_isCentralDataTransfer", g_dbtBoolean, 0)
     End If
 ' ### ENDIF IVK ###
     genSpLogDecl(fileNo)
 
     genProcSectionHeader(fileNo, "declare statement")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
     genSpLogProcEnter(fileNo, qualProcNameLrtRollback, ddlType, , IIf(useLrtOidListParam, "'lrtOids_in", "lrtOid_in"), "rowCount_out")
 
     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)
 
     Dim offset As Integer
     offset = 0
     If useLrtOidListParam Then
       genProcSectionHeader(fileNo, "loop over all OIDs of LRTs in lrtOids_in")
       Print #fileNo, addTab(1); "FOR lrtLoop AS"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "CAST(ELEM AS "; g_dbtOid; ") AS "; lrtOidRefVar
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); "TABLE ( "; g_qualFuncNameStrElems; "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(2); "ORDER BY"
       Print #fileNo, addTab(3); "POSINDEX ASC"
       Print #fileNo, addTab(1); "DO"
       offset = 1
     End If

 ' ### IF IVK ###
     genProcSectionHeader(fileNo, "determine PS-OID", offset + 1, useLrtOidListParam)
     If useLrtOidListParam Then
       Print #fileNo, addTab(offset + 1); "SELECT "; g_anPsOid; " INTO v_psOid FROM "; qualTabNameLrt; " WHERE OID = "; lrtOidRefVar; " WITH UR;"
     Else
       Print #fileNo, addTab(offset + 1); "SET v_psOid = "; g_activePsOidDdl; ";"
     End If
 
     genProcSectionHeader(fileNo, "make sure that no job is running for this LRT", offset + 1)
     Print #fileNo, addTab(offset + 1); "SELECT"
     Print #fileNo, addTab(offset + 2); "COUNT(*)"
     Print #fileNo, addTab(offset + 1); "INTO"
     Print #fileNo, addTab(offset + 1); "v_jobCount"
     Print #fileNo, addTab(offset + 1); "FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameJob
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); g_anLrtOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); ";"
     Print #fileNo,
     Print #fileNo, addTab(offset + 1); "IF v_jobCount > 0 THEN"
     genSpLogProcEscape(fileNo, qualProcNameLrtRollback, ddlType, offset + 1, IIf(useLrtOidListParam, "'lrtOids_in", "lrtOid_in"), "rowCount_out")
     genSignalDdlWithParms("lrtRbHasActiveJobs", fileNo, offset + 2, , , , , , , , , , "RTRIM(CHAR(" & lrtOidRefVar & "))")
     Print #fileNo, addTab(offset + 1); "END IF;"
 
 ' ### ENDIF IVK ###
     Print #fileNo,
     Print #fileNo, addTab(offset + 1); "SET rowCount_out = 0;"
 
     genProcSectionHeader(fileNo, "verify that this transaction has not ended", offset + 1)
     Print #fileNo, addTab(offset + 1); "SELECT"
     Print #fileNo, addTab(offset + 2); "COUNT(*)"
     Print #fileNo, addTab(offset + 1); "INTO"
     Print #fileNo, addTab(offset + 2); "v_lrtCount"
     Print #fileNo, addTab(offset + 1); "FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameLrt; " L"
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); "L."; g_anOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 3); "AND"
     Print #fileNo, addTab(offset + 2); "NOT (L."; g_anEndTime; " IS NULL)"
     Print #fileNo, addTab(offset + 1); "WITH UR;"
 
     genProcSectionHeader(fileNo, "if this transaction has already ended, we need to quit", offset + 1)
     Print #fileNo, addTab(offset + 1); "IF (v_lrtCount > 0) THEN"
     genSpLogProcEscape(fileNo, qualProcNameLrtRollback, ddlType, offset + 1, IIf(useLrtOidListParam, "'lrtOids_in", "lrtOid_in"), "rowCount_out")
     genSignalDdlWithParms("lrtAlreadyCompleted", fileNo, offset + 2, , , , , , , , , , "RTRIM(CHAR(" & lrtOidRefVar & "))")
     Print #fileNo, addTab(offset + 1); "END IF;"
 
     genProcSectionHeader(fileNo, "verify that this is an existing transaction", offset + 1)
     Print #fileNo, addTab(offset + 1); "SELECT"
     Print #fileNo, addTab(offset + 2); "COUNT(*)"
     Print #fileNo, addTab(offset + 1); "INTO"
     Print #fileNo, addTab(offset + 1); "v_lrtCount"
     Print #fileNo, addTab(offset + 1); "FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameLrt; " L"
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); "L."; g_anOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); "WITH UR;"
 
     genProcSectionHeader(fileNo, "if this transaction does not exist, we need to quit", offset + 1)
     Print #fileNo, addTab(offset + 1); "IF (v_lrtCount = 0) THEN"
     genSpLogProcEscape(fileNo, qualProcNameLrtRollback, ddlType, -(offset + 2), IIf(useLrtOidListParam, "'lrtOids_in", "lrtOid_in"), "rowCount_out")
     genSignalDdlWithParms("lrtNotExist", fileNo, offset + 2, , , , , , , , , , "RTRIM(CHAR(" & lrtOidRefVar & "))")
     Print #fileNo, addTab(offset + 1); "END IF;"
 
     genProcSectionHeader(fileNo, "determine ROLLBACK timestamp", offset + 1)
     Print #fileNo, addTab(offset + 1); "SET v_rollBackTs = CURRENT TIMESTAMP;"
 
     genProcSectionHeader(fileNo, "rollback all tables", offset + 1)
     Print #fileNo, addTab(offset + 1); "FOR tabLoop AS"
     Print #fileNo, addTab(offset + 2); "SELECT DISTINCT"
     Print #fileNo, addTab(offset + 3); "SCHEMANAME AS c_schemaName,"
     Print #fileNo, addTab(offset + 3); g_anPdmTableName; " AS c_tableName"
     Print #fileNo, addTab(offset + 2); "FROM"
     Print #fileNo, addTab(offset + 3); qualViewNamePdmTabs
     Print #fileNo, addTab(offset + 2); "WHERE"
     Print #fileNo, addTab(offset + 3); g_anInLrt; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 4); "AND"
     Print #fileNo, addTab(offset + 3); g_anLdmIsNl; " = "; gc_dbFalse
     Print #fileNo, addTab(offset + 2); "WITH UR"
     Print #fileNo, addTab(offset + 2); "FOR READ ONLY"
     Print #fileNo, addTab(offset + 1); "DO"

     Print #fileNo, addTab(offset + 2); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '.LRTROLLBACK_' || c_tableName || '(?,?)' ;"
     Print #fileNo,
     Print #fileNo, addTab(offset + 2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(offset + 2); "EXECUTE"
     Print #fileNo, addTab(offset + 3); "v_stmnt"
     Print #fileNo, addTab(offset + 2); "INTO"
     Print #fileNo, addTab(offset + 3); "v_rowCount"
     Print #fileNo, addTab(offset + 2); "USING"
     Print #fileNo, addTab(offset + 3); lrtOidRefVar
     Print #fileNo, addTab(offset + 2); ";"
     Print #fileNo,
     Print #fileNo, addTab(offset + 2); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount, 0);"
     Print #fileNo, addTab(offset + 1); "END FOR;"
 
     If useLrtOidListParam Then
       Print #fileNo, addTab(1); "END FOR;"

       genProcSectionHeader(fileNo, "loop again over all OIDs of LRTs in lrtOids_in to update meta information")
       Print #fileNo, addTab(1); "FOR lrtLoop AS"
       Print #fileNo, addTab(2); "SELECT"
       Print #fileNo, addTab(3); "CAST(ELEM AS "; g_dbtOid; ") AS "; lrtOidRefVar
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); "TABLE ( "; g_qualFuncNameStrElems; "(lrtOids_in, CAST(',' AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(2); "ORDER BY"
       Print #fileNo, addTab(3); "POSINDEX ASC"
       Print #fileNo, addTab(1); "DO"
     End If

 ' ### IF IVK ###
     If Not isPrimaryOrg And Not generateFwkTest Then
       genProcSectionHeader(fileNo, "cleanup FTO-CONFLICT-table", offset + 1)
       Print #fileNo, addTab(offset + 1); "DELETE FROM"
       Print #fileNo, addTab(offset + 2); qualTabNameConflict
       Print #fileNo, addTab(offset + 1); "WHERE"
       Print #fileNo, addTab(offset + 2); "CLRLRT_OID = "; lrtOidRefVar
       Print #fileNo, addTab(offset + 1); ";"
     End If

 ' ### ENDIF IVK ###
 
     genProcSectionHeader(fileNo, "cleanup table """ & qualTabNameLrtAffectedEntity & """", offset + 1, useLrtOidListParam)
     Print #fileNo, addTab(offset + 1); "DELETE FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameLrtAffectedEntity
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); g_anLrtOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); ";"
 
     genProcSectionHeader(fileNo, "mark this LRT as 'rolled back'", offset + 1)
     Print #fileNo, addTab(offset + 1); "UPDATE"
     Print #fileNo, addTab(offset + 2); qualTabNameLrt
     Print #fileNo, addTab(offset + 1); "SET"
 ' ### IF IVK ###
     Print #fileNo, addTab(offset + 2); g_anEndTime; " = v_rollBackTs,"
     Print #fileNo, addTab(offset + 2); g_anIsActive; " = "; gc_dbFalse
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(offset + 2); g_anEndTime; " = v_rollBackTs"
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); g_anOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); ";"
 
     genProcSectionHeader(fileNo, "cleanup info associated to LRT", offset + 1)
     Print #fileNo, addTab(offset + 1); "DELETE FROM"
     Print #fileNo, addTab(offset + 2); qualTabNameLrtExecStatus
     Print #fileNo, addTab(offset + 1); "WHERE"
     Print #fileNo, addTab(offset + 2); g_anLrtOid; " = "; lrtOidRefVar
     Print #fileNo, addTab(offset + 1); ";"

     If Not isPrimaryOrg Then
       Print #fileNo, addTab(offset + 1); "SELECT"
       Print #fileNo, addTab(offset + 2); "MAX(L."; g_anIsCentralDataTransfer; ")"
       Print #fileNo, addTab(offset + 1); "INTO"
       Print #fileNo, addTab(offset + 2); "v_isCentralDataTransfer"
       Print #fileNo, addTab(offset + 1); "FROM"
       Print #fileNo, addTab(offset + 2); qualTabNameLrt; " L"
       Print #fileNo, addTab(offset + 1); "WHERE"
       Print #fileNo, addTab(offset + 2); "L."; g_anOid; " = "; lrtOidRefVar
       Print #fileNo, addTab(offset + 1); "WITH UR;"
       Print #fileNo,

       Print #fileNo, addTab(offset + 1); "IF (v_isCentralDataTransfer = 1) THEN"
       genProcSectionHeader(fileNo, "cleanup generalsettings info if LRT was used for FTO", offset + 1)
       Print #fileNo, addTab(offset + 2); "UPDATE"
       Print #fileNo, addTab(offset + 3); qualTabNameGeneralSettings
       Print #fileNo, addTab(offset + 2); "SET"
       Print #fileNo, addTab(offset + 3); "LASTCENTRALDATATRANSFERBEGIN = LASTCENTRALDATATRANSFERCOMMIT"
       Print #fileNo, addTab(offset + 2); "WHERE"
       Print #fileNo, addTab(offset + 3); g_anPsOid; " = v_psOid"
       Print #fileNo, addTab(offset + 2); ";"
       Print #fileNo, addTab(offset + 1); "END IF;"
     End If
 
     If useLrtOidListParam Then
       Print #fileNo, addTab(1); "END FOR;"
     End If
 
     genSpLogProcExit(fileNo, qualProcNameLrtRollback, ddlType, offset + 1, IIf(useLrtOidListParam, "'lrtOids_in", "lrtOid_in"), "rowCount_out")
 
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i

 ' ### IF IVK ###
   ' ####################################################################################################################
   ' #    SP for propagating status update from aggregate head to aggregate children
   ' ####################################################################################################################

   Dim qualProcNamePropStatus As String

   qualProcNamePropStatus = genQualProcName(g_sectionIndexAliasLrt, spnAHPropagateStatus, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("SP for propagating status update from aggregate head to aggregate children", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNamePropStatus
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "classId_in", g_dbtEntityId, True, "CLASSID of the row to propagate the status for")
   genProcParm(fileNo, "IN", "oid_in", g_dbtOid, True, "OID of the row to propagate the status for")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records updated")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "notFound", "02000")
 
   genProcSectionHeader(fileNo, "declare variables")
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_tabFound", g_dbtBoolean, gc_dbFalse)
   genVarDecl(fileNo, "v_psOid", g_dbtOid, "0")
   genSpLogDecl(fileNo)

   genProcSectionHeader(fileNo, "declare statement")
   genVarDecl(fileNo, "v_stmnt", "STATEMENT")
 
   genProcSectionHeader(fileNo, "declare continue handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcEnter(fileNo, qualProcNamePropStatus, ddlType, , "'classId_in", "oid_in", "rowCount_out")

   genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

   genProcSectionHeader(fileNo, "initialize variables")
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
   Print #fileNo, addTab(1); "SET v_psOid  = "; g_activePsOidDdl; ";"

   genProcSectionHeader(fileNo, "process involved table(s) - with current MDS concepts there is exactly one table")
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); g_anPdmFkSchemaName; " AS tabSchema,"
   Print #fileNo, addTab(3); g_anPdmTypedTableName; " AS tabName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualPdmTableViewName
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "ENTITY_ID = classId_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "ENTITY_TYPE = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "ENTITY_ISLRT = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LDM_ISGEN = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LDM_ISLRT = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LDM_ISNL = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PDM_"; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PDM_POOLTYPE_ID = "; genPoolId(thisPoolIndex, ddlType)

   ' FIXME: extend meta model to cover this
   Dim tabNameList As String
   tabNameList = ""
   For i = 1 To g_classes.numDescriptors
       If g_classes.descriptors(i).supportAhStatusPropagation And g_classes.descriptors(i).isAggHead Then
         tabNameList = tabNameList & IIf(tabNameList = "", "", ",") & "'" & getUnqualObjName(genQualTabNameByClassIndex(g_classes.descriptors(i).classIndex, ddlType, thisOrgIndex, thisPoolIndex)) & "'"
       End If
   Next i

   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); g_anPdmTypedTableName; " IN ("; tabNameList; ")"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || tabSchema || '."; UCase(spnAHPropagateStatus); "_' || tabName || '(?,?,?)';"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "v_psOid,"
   Print #fileNo, addTab(3); "oid_in"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + COALESCE(v_rowCount,0);"
   Print #fileNo, addTab(2); "SET v_tabFound = "; gc_dbTrue; ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader(fileNo, "make sure that we found a table")
   Print #fileNo, addTab(1); "IF v_tabFound = "; gc_dbFalse; " THEN"
   genSpLogProcEscape(fileNo, qualProcNamePropStatus, ddlType, 2, "classId_in", "oid_in", "rowCount_out")
   genSignalDdlWithParms("noTablePropStatus", fileNo, 2, , , , , , , , , , "classId_in")
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit(fileNo, qualProcNamePropStatus, ddlType, , "'classId_in", "oid_in", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for propagating status update from aggregate head to aggregate children
   ' ####################################################################################################################

   Dim qualProcNameDelObjPropStatus As String

   qualProcNameDelObjPropStatus = genQualProcName(g_sectionindexAliasDelObj, spnAHPropagateStatus, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("SP for propagating status update from aggregate head to aggregate children", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDelObjPropStatus
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "classId_in", g_dbtEntityId, True, "CLASSID of the row to propagate the status for")
   genProcParm(fileNo, "IN", "oid_in", g_dbtOid, True, "OID of the row to propagate the status for")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records updated")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameDelObjPropStatus, ddlType, , "'classId_in", "oid_in", "rowCount_out")
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcNamePropStatus; "(classId_in, oid_in, rowCount_out);"
 
   genSpLogProcExit(fileNo, qualProcNameDelObjPropStatus, ddlType, , "'classId_in", "oid_in", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 ' ### ENDIF IVK ###
   ' ####################################################################################################################
   ' #    SP for retrieving LRT-Log
   ' ####################################################################################################################

   ' we define multiple versions of this stored procedure with different sets of API-parameters
   Dim fillRestrictedColSetOnly As Boolean
   Dim useLangParameter As Boolean
   Dim useMaxRecordCount As Boolean
 ' ### IF IVK ###
   Dim useDisplayCategory As Boolean
 ' ### ENDIF IVK ###
   Dim spInfix As String
   Dim qualProcNameLrtGetLog As String
   ' to enable API with just two parameters start loop with 'i = 1'
   For i = 2 To 4
     fillRestrictedColSetOnly = (i = 3)
     useLangParameter = (i = 2 Or i = 4)
 ' ### IF IVK ###
     useDisplayCategory = (i = 4)
 ' ### ENDIF IVK ###
     useMaxRecordCount = (i = 4)
     spInfix = IIf(fillRestrictedColSetOnly, "_RED", "")

     qualProcNameLrtGetLog = genQualProcName(g_sectionIndexAliasLrt, spnLrtGetLog & spInfix, ddlType, thisOrgIndex, thisPoolIndex)

     If fillRestrictedColSetOnly Then
       printSectionHeader("SP for retrieving LRT-Log / retrieve 'restricted set of columns only' (e.g. no ChangeComment)", fileNo)
     Else
       printSectionHeader("SP for retrieving LRT-Log", fileNo)
     End If

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameLrtGetLog
     Print #fileNo, addTab(0); "("

     genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to retrieve the Log for")
     genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", useLangParameter, "(optional) retrieve only records for updates past this timestamp")
 ' ### IF IVK ###
     If useDisplayCategory Then
       genProcParm(fileNo, "IN", "displayCategory_in", "CHAR(1)", useLangParameter Or useMaxRecordCount, "(optional) retrieve only records related to this category")
     End If
 ' ### ENDIF IVK ###
     If useLangParameter Then
       genProcParm(fileNo, "IN", "languageId_in", g_dbtEnumId, useMaxRecordCount, "use this language to retrieve NL-Texts")
     End If
     If useMaxRecordCount Then
       genProcParm(fileNo, "IN", "maxRowCount_in", "INTEGER", False, "maximum number of rows to retrieve")
     End If

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 1"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare variables", , True)
 ' ### IF IVK ###
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
 ' ### ENDIF IVK ###
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
     If Not fillRestrictedColSetOnly And Not useLangParameter Then
       genVarDecl(fileNo, "v_languageId", g_dbtEnumId, "NULL")
     End If
     genVarDecl(fileNo, "v_maxRowCount", "INTEGER", "NULL")

     genSpLogDecl(fileNo)

     genProcSectionHeader(fileNo, "declare statement")
     genVarDecl(fileNo, "v_stmnt", "STATEMENT")

     genDdlForTempLrtLog(fileNo, , fillRestrictedColSetOnly, True)

     genSpLogProcEnter(fileNo, qualProcNameLrtGetLog, ddlType, , "lrtOid_in", "startTime_in", IIf(useLangParameter, "languageId_in", ""), "rowCount_out")

     genDb2RegVarCheckDdl(fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

     If useMaxRecordCount Then
       genProcSectionHeader(fileNo, "initialize variable(s)")
       Print #fileNo, addTab(1); "SET v_maxRowCount = maxRowCount_in;"
     End If

     If Not fillRestrictedColSetOnly And Not useLangParameter Then
       genProcSectionHeader(fileNo, "determine user's language")
       Print #fileNo, addTab(1); "SELECT"
       Print #fileNo, addTab(2); "U.DATALANGUAGE_ID"
       Print #fileNo, addTab(1); "INTO"
       Print #fileNo, addTab(2); "v_languageId"
       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualTabNameLrt; " L,"
       Print #fileNo, addTab(2); g_qualTabNameUser; " U"
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "L.UTROWN_OID = U."; g_anOid
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "L."; g_anOid; " = lrtOid_in"
       Print #fileNo, addTab(1); "WITH UR;"

       genProcSectionHeader(fileNo, "use 'English' if user does not have data language", , True)
       Print #fileNo, addTab(1); "SET v_languageId = COALESCE(v_languageId, "; CStr(gc_langIdEnglish); ");"
     ElseIf useLangParameter Then
       genProcSectionHeader(fileNo, "use 'English' if no language is specified")
       Print #fileNo, addTab(1); "SET languageId_in = COALESCE(languageId_in, "; CStr(gc_langIdEnglish); ");"
     End If

 ' ### IF IVK ###
     genProcSectionHeader(fileNo, "determine PS-OID corresponding to LRT")
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); g_anPsOid
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_psOid"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameLrt
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anOid; " = lrtOid_in"
     Print #fileNo, addTab(1); "WITH UR;"

 ' ### ENDIF IVK ###
     genProcSectionHeader(fileNo, "loop over affected PDM tables and collect log records")
     Print #fileNo, addTab(1); "FOR tabLoop AS"

     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); "SCHEMANAME AS c_schemaName,"
     Print #fileNo, addTab(3); g_anPdmTableName; " AS c_tableName,"
     Print #fileNo, addTab(3); "SEQNO"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualViewNamePdmTabs
     Print #fileNo, addTab(2); "WHERE"
 ' ### IF IVK ###
     If useDisplayCategory Then
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "displayCategory_in IS NULL"
       Print #fileNo, addTab(5); "OR"
       Print #fileNo, addTab(4); g_anAcmDisplayCategory; " = displayCategory_in"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(4); "AND"
     End If
 ' ### ENDIF IVK ###
     Print #fileNo, addTab(3); g_anInLrt; " = lrtOid_in"
     If fillRestrictedColSetOnly Then
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "OPID <> "; CStr(lrtStatusLocked)
     End If
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anIgnoreForChangelog; " = "; gc_dbFalse
     Print #fileNo, addTab(2); "ORDER BY"
     Print #fileNo, addTab(3); "SEQNO ASC"
     Print #fileNo, addTab(2); "WITH UR"
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"

 ' ### IF IVK ###
     Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; UCase(spnLrtGetLog); spInfix; "_' || c_tableName || '("; _
                               IIf(Not fillRestrictedColSetOnly, "?,?,", ""); _
                               "?,?,?,?)' ;"
 ' ### ELSE IVK ###
 '   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL ' || c_schemaName || '.' || '"; UCase(spnLrtGetLog); spInfix; "_' || c_tableName || '("; _
 '                             IIf(Not fillRestrictedColSetOnly, "?,?,", ""); _
 '                             "?,?,?)' ;"
 ' ### ENDIF IVK ###

     Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE"
     Print #fileNo, addTab(3); "v_stmnt"
     Print #fileNo, addTab(2); "INTO"
     Print #fileNo, addTab(3); "v_rowCount"
     Print #fileNo, addTab(2); "USING"
     Print #fileNo, addTab(3); "lrtOid_in,"
 ' ### IF IVK ###
     Print #fileNo, addTab(3); "v_psOid,"
 ' ### ENDIF IVK ###
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(3); IIf(useLangParameter, "languageId_in", "v_languageId"); ","
     End If
     Print #fileNo, addTab(3); "startTime_in"; IIf(fillRestrictedColSetOnly, "", ",")
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(3); "v_maxRowCount"
     End If
     Print #fileNo, addTab(2); ";"

     If Not fillRestrictedColSetOnly Then
       Print #fileNo,
       Print #fileNo, addTab(2); "SET v_maxRowCount = (CASE WHEN v_maxRowCount > v_rowCount THEN v_maxRowCount - v_rowCount ELSE 0 END);"
     End If
     Print #fileNo, addTab(1); "END FOR;"

     If Not fillRestrictedColSetOnly Then
       genProcSectionHeader(fileNo, "set language specific column 'entityName' in log")
       Print #fileNo, addTab(1); "UPDATE"
       Print #fileNo, addTab(2); tempTabNameLrtLog; " L"
       Print #fileNo, addTab(1); "SET"
       Print #fileNo, addTab(2); "entityName = ("
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ENL."; g_anAcmEntityLabel
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); g_qualTabNameAcmEntityNl; " ENL"
       Print #fileNo, addTab(3); "INNER JOIN"
       Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " E"
       Print #fileNo, addTab(3); "ON"
       Print #fileNo, addTab(4); "E."; g_anAcmEntitySection; " = ENL."; g_anAcmEntitySection
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "E."; g_anAcmEntityName; " = ENL."; g_anAcmEntityName
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "E."; g_anAcmEntityType; " = ENL."; g_anAcmEntityType
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "E."; g_anAcmEntityType; " = L.entityType"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "E."; g_anAcmEntityId; " = L.entityId"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "ENL."; g_anLanguageId; " = "; IIf(useLangParameter, "languageId_in", "v_languageId")
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(1); ";"
     End If

     genProcSectionHeader(fileNo, "return result to application")
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "DECLARE logCursor CURSOR WITH RETURN TO CLIENT FOR"
     Print #fileNo, addTab(3); "WITH"
     Print #fileNo, addTab(4); "V_LrtLog"
     Print #fileNo, addTab(3); "AS"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "SELECT"

     If useMaxRecordCount Then
       Print #fileNo, addTab(5); "ROWNUMBER() OVER (ORDER BY displayMe DESC, OID ASC) AS seqNo,"
     End If

     Print #fileNo, addTab(5); "entityId,"
     Print #fileNo, addTab(5); "entityType,"
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(5); "entityName,"
 ' ### IF IVK ###
       Print #fileNo, addTab(5); "displayCategory,"
 ' ### ENDIF IVK ###
     End If
     Print #fileNo, addTab(5); "gen,"
     Print #fileNo, addTab(5); "isNl,"
     Print #fileNo, addTab(5); "oid,"
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(5); "refClassId1,"
     End If
     Print #fileNo, addTab(5); "refObjectId1,"
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(5); "refClassId2,"
     End If
     Print #fileNo, addTab(5); "refObjectId2,"
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(5); "label,"
       Print #fileNo, addTab(5); "comment,"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(5); "code,"
 ' ### ENDIF IVK ###
     If Not fillRestrictedColSetOnly Then
 ' ### IF IVK ###
       Print #fileNo, addTab(5); "sr0Context,"
       Print #fileNo, addTab(5); "sr0Code1,"
       Print #fileNo, addTab(5); "sr0Code2,"
       Print #fileNo, addTab(5); "sr0Code3,"
       Print #fileNo, addTab(5); "sr0Code4,"
       Print #fileNo, addTab(5); "sr0Code5,"
       Print #fileNo, addTab(5); "sr0Code6,"
       Print #fileNo, addTab(5); "sr0Code7,"
       Print #fileNo, addTab(5); "sr0Code8,"
       Print #fileNo, addTab(5); "sr0Code9,"
       Print #fileNo, addTab(5); "sr0Code10,"
       Print #fileNo, addTab(5); "baseCode,"
       Print #fileNo, addTab(5); "baseEndSlot,"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(5); "validFrom,"
       Print #fileNo, addTab(5); "validTo,"
     End If
     Print #fileNo, addTab(5); "operation,"
     Print #fileNo, addTab(5); "ts"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); tempTabNameLrtLog
     Print #fileNo, addTab(3); ")"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "entityId,"
     Print #fileNo, addTab(4); "entityType,"
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(4); "entityName,"
 ' ### IF IVK ###
       Print #fileNo, addTab(4); "displayCategory,"
 ' ### ENDIF IVK ###
     End If
     Print #fileNo, addTab(4); "gen,"
     Print #fileNo, addTab(4); "isNl,"
     Print #fileNo, addTab(4); "oid,"
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(4); "refClassId1,"
     End If
     Print #fileNo, addTab(4); "refObjectId1,"
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(4); "refClassId2,"
     End If
     Print #fileNo, addTab(4); "refObjectId2,"
     If Not fillRestrictedColSetOnly Then
       Print #fileNo, addTab(4); "label,"
       Print #fileNo, addTab(4); "comment,"
     End If
 ' ### IF IVK ###
     Print #fileNo, addTab(4); "code,"
 ' ### ENDIF IVK ###
     If Not fillRestrictedColSetOnly Then
 ' ### IF IVK ###
       Print #fileNo, addTab(4); "sr0Context,"
       Print #fileNo, addTab(4); "sr0Code1,"
       Print #fileNo, addTab(4); "sr0Code2,"
       Print #fileNo, addTab(4); "sr0Code3,"
       Print #fileNo, addTab(4); "sr0Code4,"
       Print #fileNo, addTab(4); "sr0Code5,"
       Print #fileNo, addTab(4); "sr0Code6,"
       Print #fileNo, addTab(4); "sr0Code7,"
       Print #fileNo, addTab(4); "sr0Code8,"
       Print #fileNo, addTab(4); "sr0Code9,"
       Print #fileNo, addTab(4); "sr0Code10,"
       Print #fileNo, addTab(4); "baseCode,"
       Print #fileNo, addTab(4); "baseEndSlot,"
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(4); "validFrom,"
       Print #fileNo, addTab(4); "validTo,"
     End If
     Print #fileNo, addTab(4); "operation,"
     Print #fileNo, addTab(4); "ts"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "V_LrtLog"
     If useMaxRecordCount Then
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "maxRowCount_in IS NULL"
       Print #fileNo, addTab(5); "OR"
       Print #fileNo, addTab(4); "seqNo <= maxRowCount_in"
     End If
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "oid"
     Print #fileNo, addTab(2); ";"

     genProcSectionHeader(fileNo, "leave cursor open for application", 2)
     Print #fileNo, addTab(2); "OPEN logCursor;"
     Print #fileNo, addTab(1); "END;"

     genSpLogProcExit(fileNo, qualProcNameLrtGetLog, ddlType, , "lrtOid_in", "startTime_in", IIf(useLangParameter, "languageId_in", ""), "rowCount_out")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     If fillRestrictedColSetOnly Then
       Dim qualProcNameLrtGetLogWrapper As String
       qualProcNameLrtGetLogWrapper = genQualProcName(g_sectionindexAliasPrivateOnly, spnLrtGetLog & spInfix, ddlType, thisOrgIndex, thisPoolIndex)

       printSectionHeader("Wrapper SP for retrieving LRT-Log in Work Data Pool / retrieve 'restricted set of columns only'", fileNo)

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcNameLrtGetLogWrapper
       Print #fileNo, addTab(0); "("

       genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to retrieve the Log for")
       genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", False, "(optional) retrieve only records for updates past this timestamp")

       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 1"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

       genSpLogDecl(fileNo, -1, True)

       genSpLogProcEnter(fileNo, qualProcNameLrtGetLogWrapper, ddlType, , "lrtOid_in", "startTime_in", "rowCount_out")

       Print #fileNo,
       Print #fileNo, addTab(1); "CALL "; qualProcNameLrtGetLog; "(lrtOid_in, startTime_in);"

       genSpLogProcExit(fileNo, qualProcNameLrtGetLogWrapper, ddlType, , "lrtOid_in", "startTime_in", "rowCount_out")

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     End If

 ' ### IF IVK ###
     If useDisplayCategory Then
       If fillRestrictedColSetOnly Then
         printSectionHeader("SP for retrieving LRT-Log / retrieve 'restricted set of columns only' (e.g. no ChangeComment)", fileNo)
       Else
         printSectionHeader("SP for retrieving LRT-Log", fileNo)
       End If

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcNameLrtGetLog
       Print #fileNo, addTab(0); "("

       genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to retrieve the Log for")
       genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", useLangParameter Or useMaxRecordCount, "(optional) retrieve only records for updates past this timestamp")
       If useLangParameter Then
         genProcParm(fileNo, "IN", "languageId_in", g_dbtEnumId, useMaxRecordCount, "use this language to retrieve NL-Texts")
       End If
       If useMaxRecordCount Then
         genProcParm(fileNo, "IN", "maxRowCount_in", "INTEGER", False, "maximum number of rows to retrieve")
       End If

       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 1"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

       genSpLogDecl(fileNo, -1, True)

       genSpLogProcEnter(fileNo, qualProcNameLrtGetLog, ddlType, , "lrtOid_in", "startTime_in", IIf(useLangParameter, "languageId_in", ""), IIf(useMaxRecordCount, "maxRowCount_in", ""))
 
       Print #fileNo, addTab(1); "CALL "; qualProcNameLrtGetLog; "(lrtOid_in, startTime_in"; _
                                 ", NULL"; _
                                 IIf(useLangParameter, ", languageId_in", ""); _
                                 IIf(useMaxRecordCount, ", maxRowCount_in", ""); _
                                 ");"

       genSpLogProcExit(fileNo, qualProcNameLrtGetLog, ddlType, , "lrtOid_in", "startTime_in", IIf(useLangParameter, "languageId_in", ""), IIf(useMaxRecordCount, "maxRowCount_in", ""))

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     End If
 ' ### ENDIF IVK ###
   Next i

   ' ####################################################################################################################
   ' #    SP for retrieving cardinality of LRT-Log
   ' ####################################################################################################################

   Dim qualProcNameLrtGetLogCard As String
   qualProcNameLrtGetLogCard = genQualProcName(g_sectionIndexAliasLrt, spnLrtGetLogCard, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("SP for retrieving cardinality of LRT-Log", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameLrtGetLogCard
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows in the log")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare conditions", , True)
   genCondDecl(fileNo, "alreadyExist", "42710")

   genSpLogDecl(fileNo, , True)

   genProcSectionHeader(fileNo, "declare condition handler")
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist BEGIN END;"
 
   genDdlForTempLrtLog(fileNo)

   genSpLogProcEnter(fileNo, qualProcNameLrtGetLogCard, ddlType, , "rowCount_out")

   genProcSectionHeader(fileNo, "count rows in LRT-Log")
   Print #fileNo, addTab(1); "SET rowCount_out = (SELECT COUNT(DISTINCT oid) FROM "; tempTabNameLrtLog; ");"

   genSpLogProcExit(fileNo, qualProcNameLrtGetLogCard, ddlType, , "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


   ' ####################################################################################################################
   ' #    SP for checking if Lrt contains division data
   ' ####################################################################################################################

   Dim procName As String
   procName = genQualProcName(g_sectionIndexAliasLrt, spnLrtIncludesDivisionData, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("SP for checking if Lrt contains division data", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); procName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to check")
   genProcParm(fileNo, "OUT", "result_out", g_dbtBoolean, False, "0 = false, 1 = true")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogProcEnter(fileNo, procName, ddlType, , "lrtOid_in")

   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); "result_out ="
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE WHEN"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "SELECT"
   Print #fileNo, addTab(7); "COUNT(*)"
   Print #fileNo, addTab(6); "FROM"
   Print #fileNo, addTab(7); g_qualTabNameAcmEntity; " E, "; qualViewNamePdmTabs; " L"
   Print #fileNo, addTab(6); "WHERE"
   Print #fileNo, addTab(7); "E.ENTITYID = L.ORPARENTENTITYID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(7); "E.ENTITYTYPE = L.ENTITYTYPE"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(7); "E.ISPS = 0"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(7); "E.ISPSFORMING = 0"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(7); "E.ISLRT = 1"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(7); "L.INLRT = lrtOid_in"
   Print #fileNo, addTab(5); ") > 0"
   Print #fileNo, addTab(4); "THEN"
   Print #fileNo, addTab(5); "1"
   Print #fileNo, addTab(4); "ELSE"
   Print #fileNo, addTab(5); "0"
   Print #fileNo, addTab(4); "END"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(1); ";"

   genSpLogProcExit(fileNo, procName, ddlType, , "result_out")
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim


 End Sub
 

 ' ### IF IVK ###
 Private Sub genLrtSpSupportDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If Not g_genLrtSupport Or generateFwkTest Then
     Exit Sub
   End If

   On Error GoTo ErrorExit
 
   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexLrt, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)

   ' ####################################################################################################################
   ' #    SP for activion of all Prices
   ' ####################################################################################################################

   Dim qualTabNameGeneralSettings As String
   qualTabNameGeneralSettings = genQualTabNameByClassIndex(g_classIndexGeneralSettings, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameTypeSpec As String
   qualTabNameTypeSpec = genQualTabNameByClassIndex(g_classIndexTypeSpec, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameTypeStandardEquipment As String
   qualTabNameTypeStandardEquipment = genQualTabNameByClassIndex(g_classIndexTypeStandardEquipment, ddlType, thisOrgIndex, thisPoolIndex)

   Dim changeLogClassIndex As Integer
   changeLogClassIndex = g_classIndexChangeLog
   Dim qualTabNameChangeLog As String
   qualTabNameChangeLog = genQualTabNameByClassIndex(changeLogClassIndex, ddlType, thisOrgIndex, thisPoolIndex)
   Dim qualTabNameChangeLogNl As String
   qualTabNameChangeLogNl = genQualNlTabNameByClassIndex(changeLogClassIndex, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim qualProcName As String
   Dim qualProcNameCP As String
   Dim qualProcNameDelObCP As String
   Dim qualProcNameTP As String
   Dim qualProcNameDelObTP As String

   qualProcName = genQualProcName(g_sectionIndexAliasLrt, spnActivateAllPrices, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader("SP for activation of all Prices", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "forNational_in", g_dbtBoolean, True, "if 'TRUE' activate all national Code Prices, if 'FALSE' activate non-national Code Prices")
   genProcParm(fileNo, "IN", "classId_in", g_dbtEntityId, True, "classId of Price to activate (supported: Code Prices and Type Price)")
   genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", True, "number of Code Prices activated")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records activated (including aggregate children)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genSigMsgVarDecl(fileNo)
   genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_targetState", g_dbtEnumId, "0")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(600)", "NULL")
   genVarDecl(fileNo, "v_cdUserId", g_dbtUserId, "NULL")
   genVarDecl(fileNo, "v_currentTimestamp", "TIMESTAMP", "NULL")
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genSpLogDecl(fileNo)
 
   genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, thisPoolIndex, ddlType, 1, False, , , True)
 
   genSpLogProcEnter(fileNo, qualProcName, ddlType, , "forNational_in", "classId_in", "priceCount_out", "rowCount_out")
 
   genProcSectionHeader(fileNo, "determine ProductStructure")
   Print #fileNo, addTab(1); "SET v_psOid  = "; g_activePsOidDdl; ";"

   genProcSectionHeader(fileNo, "make sure that ProductStructure exists")
   Print #fileNo, addTab(1); "IF (v_psOid IS NULL) THEN"
   genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "forNational_in", "classId_in", "priceCount_out", "rowCount_out")
   genSignalDdl("noPs", fileNo, 2)
   Print #fileNo, addTab(1); "ELSEIF NOT EXISTS (SELECT 1 FROM "; g_qualTabNameProductStructure; " WHERE OID = v_psOid) THEN"
   genSpLogProcEscape(fileNo, qualProcName, ddlType, 2, "forNational_in", "classId_in", "priceCount_out", "rowCount_out")
   genSignalDdlWithParms("psNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_psOid))")
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "make parameter classId_in is set correctly")
   Print #fileNo, addTab(1); "IF (classId_in <> '" + g_classes.descriptors(g_classIndexCodePriceAssignment).classIdStr + "'  AND classId_in <> '" + g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr + "') THEN"
   genSignalDdlWithParms("illegParam", fileNo, 2, "classId_in", , , , , , , , , "RTRIM(CHAR(classId_in))")
   Print #fileNo, addTab(1); "END IF;"
 
 
   genProcSectionHeader(fileNo, "determine target state according to configuration settings for 'selective release process'")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); g_dbtEnumId; "((CASE USESELECTIVERELEASEPROCESS WHEN 0 THEN "; CStr(statusReadyToBeSetProductive) & " ELSE " & CStr(statusReadyForRelease) & " END))"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_targetState"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGeneralSettings
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); "SET v_targetState = COALESCE(v_targetState, "; CStr(statusReadyForRelease); ");"
 
   genProcSectionHeader(fileNo, "determine current user id (for changelog)")
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'MIG_NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
 
   genProcSectionHeader(fileNo, "determine current timestamp")
   Print #fileNo, addTab(1); "SET v_currentTimestamp = CURRENT TIMESTAMP;"
 
   Dim i As Integer
   For i = 1 To g_relationships.numDescriptors
       If g_relationships.descriptors(i).hasPriceAssignmentAggHead And g_relationships.descriptors(i).maxLeftCardinality < 0 And g_relationships.descriptors(i).maxRightCardinality < 0 And g_relationships.descriptors(i).reusedRelIndex <= 0 Then
         genActivateCodeForEntity(eactRelationship, g_relationships.descriptors(i).relIndex, fileNo, , , thisOrgIndex, thisPoolIndex, ddlType)
       End If
   Next i
   For i = 1 To g_classes.numDescriptors
       If (g_classes.descriptors(i).hasPriceAssignmentAggHead Or g_classes.descriptors(i).hasPriceAssignmentSubClass) And g_classes.descriptors(i).superClassIndex <= 0 Then
         genActivateCodeForEntity(eactClass, g_classes.descriptors(i).classIndex, fileNo, , , thisOrgIndex, thisPoolIndex, ddlType)

         If g_classes.descriptors(i).hasNlAttrsInNonGenInclSubClasses Then
           genActivateCodeForEntity(eactClass, g_classes.descriptors(i).classIndex, fileNo, , True, thisOrgIndex, thisPoolIndex, ddlType)
         End If
       End If
   Next i


   Print #fileNo, addTab(1); "IF classId_in = '09032' THEN"
   genProcSectionHeader(fileNo, "update status on table table '" & qualTabNameTypeSpec & "'")
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); qualTabNameTypeSpec; " T"
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "T."; g_anStatus; " = v_targetState,"
   Print #fileNo, addTab(3); "T."; g_anLastUpdateTimestamp; " = v_currentTimestamp,"
   Print #fileNo, addTab(3); "T."; g_anUpdateUser; " = v_cdUserId,"
   Print #fileNo, addTab(3); "T."; g_anVersionId; " = T."; g_anVersionId; " + 1"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "T."; g_anInLrt; " IS NULL"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "T."; g_anStatus; " < v_targetState"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "T."; g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "T.TSTTPA_OID IN (SELECT OID FROM "; qualTabNameGenericAspect; " GA WHERE GA.STATUS_ID = v_targetState)"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "count the number of affected rows", 1, True)
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   genProcSectionHeader(fileNo, "update status on table '" & qualTabNameTypeStandardEquipment & "'")
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); qualTabNameTypeStandardEquipment; " T"
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "T."; g_anStatus; " = v_targetState,"
   Print #fileNo, addTab(3); "T."; g_anVersionId; " = T."; g_anVersionId; " + 1"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "T."; g_anInLrt; " IS NULL"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "T."; g_anStatus; " < v_targetState"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "T."; g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "T.TSETYS_OID IN (SELECT OID FROM "; qualTabNameTypeSpec; " TS WHERE TS.STATUS_ID = v_targetState)"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "count the number of affected rows", 1, True)
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "move all ChangeLog records into persistent table")

   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNameChangeLog
   Print #fileNo, addTab(1); "("

   genAttrListForEntity(changeLogClassIndex, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , edomListLrt Or edomListVirtual Or edomVirtualPersisted)

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"

   genAttrListForEntity(changeLogClassIndex, eactClass, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , edomListLrt Or edomListVirtual Or edomVirtualPersisted)

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); gc_tempTabNameChangeLog
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "add NL-texts to changelog")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNameChangeLogNl
   Print #fileNo, addTab(1); "("

   Print #fileNo, addTab(2); g_anOid; ","
   Print #fileNo, addTab(2); "CLG_OID,"
   Print #fileNo, addTab(2); g_anLanguageId; ","
   Print #fileNo, addTab(2); g_anAcmAttributeLabel; ","
   Print #fileNo, addTab(2); g_anAcmEntityName; ","
   Print #fileNo, addTab(2); g_anVersionId

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "V_Nl"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "clg_oid,"
   Print #fileNo, addTab(2); "languageId,"
   Print #fileNo, addTab(2); "attributeLabel,"
   Print #fileNo, addTab(2); "entityName"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("

   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "L."; g_anOid; ","
   Print #fileNo, addTab(3); "ENL."; g_anLanguageId; ","
   Print #fileNo, addTab(3); "ANL."; g_anAcmAttributeLabel; ","
   Print #fileNo, addTab(3); "ENL."; g_anAcmEntityLabel
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); gc_tempTabNameChangeLog; " L"

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " E"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "L."; g_anAcmEntityId; " = E."; g_anAcmEntityId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anAcmEntityType; " = E."; g_anAcmEntityType

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntityNl; " ENL"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "E."; g_anAcmEntitySection; " = ENL."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "And"
   Print #fileNo, addTab(3); "E."; g_anAcmEntityName; " = ENL."; g_anAcmEntityName
   Print #fileNo, addTab(4); "And"
   Print #fileNo, addTab(3); "E."; g_anAcmEntityType; " = ENL."; g_anAcmEntityType

   ' FIXME: assuming that within a single class hierarchy a given attribute name is not mapped
   ' differently for different classes we use 'DISTINCT' here. We should navigate up in the
   ' class hierarchy and pick exactly the attribute that is referred to!
   ' E.g. 'SR0CONTEXT' exists multiple times in the GENERICASPECT-tree. Each changelog-entry refers to a unique
   ' occurence.
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " EA"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "EA."; g_anAcmOrParEntitySection; " = E."; g_anAcmOrParEntitySection
   Print #fileNo, addTab(4); "And"
   Print #fileNo, addTab(3); "EA."; g_anAcmOrParEntityName; " = E."; g_anAcmOrParEntityName
   Print #fileNo, addTab(4); "And"
   Print #fileNo, addTab(3); "EA."; g_anAcmOrParEntityType; " = E."; g_anAcmOrParEntityType

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmAttributeNl; " ANL"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "EA."; g_anAcmEntitySection; " = ANL."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "EA."; g_anAcmEntityName; " = ANL."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "EA."; g_anAcmEntityType; " = ANL."; g_anAcmEntityType
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "L."; g_anLdmDbColumnName; " = ANL."; g_anAcmAttributeName
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "L."; g_anLdmDbColumnName; " = ANL."; g_anAcmAttributeName; " || '_ID'"
   Print #fileNo, addTab(1); "),"

   Print #fileNo, addTab(2); "V_NlD"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "clg_oid,"
   Print #fileNo, addTab(2); "languageId,"
   Print #fileNo, addTab(2); "attributeLabel,"
   Print #fileNo, addTab(2); "entityName"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "clg_oid,"
   Print #fileNo, addTab(3); "languageId,"
   Print #fileNo, addTab(3); "attributeLabel,"
   Print #fileNo, addTab(3); "entityName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_Nl"
   Print #fileNo, addTab(1); ")"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "NEXTVAL FOR "; qualSeqNameOid; ","
   Print #fileNo, addTab(2); "clg_oid,"
   Print #fileNo, addTab(2); "languageId,"
   Print #fileNo, addTab(2); "attributeLabel,"
   Print #fileNo, addTab(2); "entityName,"
   Print #fileNo, addTab(2); "1"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_NlD"

   Print #fileNo, addTab(1); ";"
 
   genSpLogProcExit(fileNo, qualProcName, ddlType, , "forNational_in", "classId_in", "priceCount_out", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for activion of all Code Prices
   ' ####################################################################################################################

   qualProcNameDelObCP = genQualProcName(g_sectionindexAliasDelObj, spnActivateAllCodePrices, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("SP for activion of all Code Prices", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDelObCP
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "forNational_in", g_dbtBoolean, True, "if 'TRUE' activate all national Code Prices, if 'FALSE' activate non-national Code Prices")
   genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", True, "number of Code Prices activated")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records activated (including aggregate children)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"


   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameDelObCP, ddlType, , "forNational_in", "'" + g_classes.descriptors(g_classIndexCodePriceAssignment).classIdStr + "'", "priceCount_out", "rowCount_out")
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(forNational_in, '" + g_classes.descriptors(g_classIndexCodePriceAssignment).classIdStr + "', priceCount_out, rowCount_out);"
 
   genSpLogProcExit(fileNo, qualProcName, ddlType, , "forNational_in", "'" + g_classes.descriptors(g_classIndexCodePriceAssignment).classIdStr + "'", "priceCount_out", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for activion of all Code Prices
   ' ####################################################################################################################

   qualProcNameCP = genQualProcName(g_sectionIndexAliasLrt, spnActivateAllCodePrices, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("SP for activion of all Code Prices", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameCP
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "forNational_in", g_dbtBoolean, True, "if 'TRUE' activate all national Code Prices, if 'FALSE' activate non-national Code Prices")
   genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", True, "number of Code Prices activated")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records activated (including aggregate children)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"


   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameCP, ddlType, , "forNational_in", "'" + g_classes.descriptors(g_classIndexCodePriceAssignment).classIdStr + "'", "priceCount_out", "rowCount_out")
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(forNational_in, '" + g_classes.descriptors(g_classIndexCodePriceAssignment).classIdStr + "', priceCount_out, rowCount_out);"
 
   genSpLogProcExit(fileNo, qualProcName, ddlType, , "forNational_in", "'" + g_classes.descriptors(g_classIndexCodePriceAssignment).classIdStr + "'", "priceCount_out", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for activion of all Type Prices
   ' ####################################################################################################################

   qualProcNameDelObTP = genQualProcName(g_sectionindexAliasDelObj, spnActivateAllTypePrices, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("SP for activion of all Type Prices", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameDelObTP
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "forNational_in", g_dbtBoolean, True, "if 'TRUE' activate all national Type Prices, if 'FALSE' activate non-national Type Prices")
   genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", True, "number of Type Prices activated")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records activated (including aggregate children)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"


   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameDelObTP, ddlType, , "forNational_in", "'" + g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr + "'", "priceCount_out", "rowCount_out")
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(forNational_in, '" + g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr + "', priceCount_out, rowCount_out);"
 
   genSpLogProcExit(fileNo, qualProcName, ddlType, , "forNational_in", "'" + g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr + "'", "priceCount_out", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for activion of all Type Prices
   ' ####################################################################################################################

   qualProcNameTP = genQualProcName(g_sectionIndexAliasLrt, spnActivateAllTypePrices, ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("SP for activion of all Type Prices", fileNo)

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameTP
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "forNational_in", g_dbtBoolean, True, "if 'TRUE' activate all national Type Prices, if 'FALSE' activate non-national Type Prices")
   genProcParm(fileNo, "OUT", "priceCount_out", "INTEGER", True, "number of Type Prices activated")
   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records activated (including aggregate children)")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"


   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameTP, ddlType, , "forNational_in", "'" + g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr + "'", "priceCount_out", "rowCount_out")
 
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(forNational_in, '" + g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr + "', priceCount_out, rowCount_out);"
 
   genSpLogProcExit(fileNo, qualProcName, ddlType, , "forNational_in", "'" + g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr + "'", "priceCount_out", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDdlForAggStatusProp( _
   ByRef qualChildTabName As String, _
   fileNo As Integer, _
   Optional indent As Integer = 3, _
   Optional ByRef oidReference As String = "NEWRECORD.OID", _
   Optional ByRef statusReference As String = "NEWRECORD.STATUS_ID", _
   Optional ByRef classIdReference As String = "NEWRECORD.CLASSID", _
   Optional ByRef recordCountVar As String = "", _
   Optional ByRef recordCountVarSum As String = "" _
 )
   Print #fileNo, addTab(indent + 0); "UPDATE"
   Print #fileNo, addTab(indent + 1); qualChildTabName; " D"
   Print #fileNo, addTab(indent + 0); "SET"
   Print #fileNo, addTab(indent + 1); "D."; g_anStatus; " = "; statusReference
   Print #fileNo, addTab(indent + 0); "WHERE"
   Print #fileNo, addTab(indent + 1); "D."; g_anAhCid; " = "; classIdReference
   Print #fileNo, addTab(indent + 2); "AND"
   Print #fileNo, addTab(indent + 1); "D."; g_anAhOid; " = "; oidReference
   Print #fileNo, addTab(indent + 2); "AND"
   Print #fileNo, addTab(indent + 1); "D."; g_anStatus; " NOT IN ("; statusReference; ","; CStr(statusProductive) & ")"
   Print #fileNo, addTab(indent + 0); ";"
 
   If recordCountVar <> "" And recordCountVarSum <> "" Then
     genProcSectionHeader(fileNo, "count the number of affected rows", indent)
     Print #fileNo, addTab(indent + 0); "GET DIAGNOSTICS "; recordCountVar; " = ROW_COUNT;"
     Print #fileNo, addTab(indent + 0); "SET "; recordCountVarSum; " = "; recordCountVarSum; " + "; recordCountVar; ";"
   End If
 End Sub
 
 
 Private Sub genDdlForAggStatusPropLrtCommit( _
   ByRef qualChildTabName As String, _
   ByRef qualAhPrivTabName As String, _
   ByRef priceAssignmentSubClassIdList As String, _
   fileNo As Integer, _
   Optional indent As Integer = 3, _
   Optional isPsTagged As Boolean = False, _
   Optional ByRef psTagRefVal As String = "" _
 )
   Dim hasPriceAssignmentSubClass As Boolean
   hasPriceAssignmentSubClass = (priceAssignmentSubClassIdList <> "")

   Print #fileNo, addTab(indent + 0); "UPDATE"
   Print #fileNo, addTab(indent + 1); qualChildTabName; " D"
   Print #fileNo, addTab(indent + 0); "SET"
   Print #fileNo, addTab(indent + 1); "D."; g_anStatus; " = ";

   Print #fileNo, IIf(hasPriceAssignmentSubClass, "CASE WHEN (autoPriceSetProductive_in = 1) AND (D." & g_anAhCid & " IN (" & priceAssignmentSubClassIdList & ")) THEN " & statusReadyToBeSetProductive & " ELSE ", "");
   Print #fileNo, g_qualFuncNameGetLrtTargetStatus; "(";
   Print #fileNo, "D."; g_anAhCid; ",";
   Print #fileNo, "CAST('"; gc_acmEntityTypeKeyClass; "' AS "; g_dbtEntityType; "),";
   Print #fileNo, "settingManActCP_in,";
   Print #fileNo, "settingManActTP_in,";
   Print #fileNo, "settingManActSE_in,";
   Print #fileNo, "settingSelRelease_in";
   Print #fileNo, ")";
   Print #fileNo, IIf(hasPriceAssignmentSubClass, " END", "")

   Print #fileNo, addTab(indent + 0); "WHERE"

   If isPsTagged And psTagRefVal <> "" Then
     Print #fileNo, addTab(indent + 1); "D."; g_anPsOid; " = "; psTagRefVal
     Print #fileNo, addTab(indent + 2); "AND"
   End If

   Print #fileNo, addTab(indent + 1); "EXISTS ("
   Print #fileNo, addTab(indent + 2); "SELECT"
   Print #fileNo, addTab(indent + 3); "1"
   Print #fileNo, addTab(indent + 2); "FROM"
   Print #fileNo, addTab(indent + 3); pc_tempTabNamePubOidsAffected; " O"
   Print #fileNo, addTab(indent + 2); "WHERE"
   Print #fileNo, addTab(indent + 3); "D."; g_anAhOid; " = O.oid"
   Print #fileNo, addTab(indent + 4); "AND"
   Print #fileNo, addTab(indent + 3); "D."; g_anAhCid; " = O.classId"
   Print #fileNo, addTab(indent + 4); "AND"
   Print #fileNo, addTab(indent + 3); "D."; g_anStatus; " NOT IN (O.privStatusId, "; CStr(statusProductive); ")"
   Print #fileNo, addTab(indent + 1); ")"
   Print #fileNo, addTab(indent + 0); "WITH UR"; ";"
 End Sub
 
 
 ' ### ENDIF IVK ###
 Sub genLrtSupportViewForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional isPurelyPrivate As Boolean = False _
 )
   Dim sectionName As String
   Dim sectionIndex As Integer
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim isUserTransactional As Boolean
 ' ### IF IVK ###
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
 ' ### ENDIF IVK ###
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim dbAcmEntityType As String
   Dim attrRefs As AttrDescriptorRefs
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
 ' ### IF IVK ###
   Dim hasNoIdentity As Boolean
   Dim isNational As Boolean
 ' ### ENDIF IVK ###
   Dim isAggHead As Boolean
   Dim ahClassIndex As Integer
   Dim ahClassIdStr As String
   Dim aggChildClassIndexes() As Integer
   Dim aggChildRelIndexes() As Integer
   Dim useMqtToImplementLrtForEntity As Boolean
 ' ### IF IVK ###
   Dim objSupportsPsDpFilter As Boolean
   Dim condenseData As Boolean
   Dim expandExpressionsInFtoView As Boolean
 ' ### ENDIF IVK ###

   On Error GoTo ErrorExit

   If acmEntityType = eactClass Then
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       ahClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       ahClassIdStr = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
       aggChildClassIndexes = g_classes.descriptors(acmEntityIndex).aggChildClassIndexes
       aggChildRelIndexes = g_classes.descriptors(acmEntityIndex).aggChildRelIndexes
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
 ' ### IF IVK ###
       objSupportsPsDpFilter = g_classes.descriptors(acmEntityIndex).isPsTagged
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       expandExpressionsInFtoView = g_classes.descriptors(acmEntityIndex).expandExpressionsInFtoView
 ' ### ENDIF IVK ###
       isAggHead = g_classes.descriptors(acmEntityIndex).isAggHead And Not forGen And Not forNl

       If forNl Then
         entityName = genNlObjName(g_classes.descriptors(acmEntityIndex).className, , forGen)
         entityShortName = genNlObjShortName(g_classes.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Class (NL-Text)"
         hasOwnTable = True
 ' ### IF IVK ###
         isPsTagged = usePsTagInNlTextTables And g_classes.descriptors(acmEntityIndex).isPsTagged
         psTagOptional = usePsTagInNlTextTables And g_classes.descriptors(acmEntityIndex).psTagOptional
 ' ### ENDIF IVK ###
         isAbstract = False
         attrRefs = g_classes.descriptors(acmEntityIndex).nlAttrRefsInclSubclasses
         relRefs.numRefs = 0
         isGenForming = False
 ' ### IF IVK ###
         hasNoIdentity = False
         isNational = False
 ' ### ENDIF IVK ###
       Else
         entityName = g_classes.descriptors(acmEntityIndex).className
         entityShortName = g_classes.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Class"
         hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
 ' ### IF IVK ###
         isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
         psTagOptional = g_classes.descriptors(acmEntityIndex).psTagOptional
 ' ### ENDIF IVK ###
         isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
         attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
         relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
         isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
 ' ### IF IVK ###
         hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
         isNational = g_classes.descriptors(acmEntityIndex).isNationalizable
 ' ### ENDIF IVK ###
       End If
   ElseIf acmEntityType = eactRelationship Then
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       dbAcmEntityType = "R"
       ahClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       ahClassIdStr = g_relationships.descriptors(acmEntityIndex).aggHeadClassIdStr
       relRefs.numRefs = 0
       isGenForming = False
 ' ### IF IVK ###
       hasNoIdentity = False
       isNational = False
 ' ### ENDIF IVK ###
       isAggHead = False
 ' ### IF IVK ###
       psTagOptional = False
 ' ### ENDIF IVK ###
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
 ' ### IF IVK ###
       objSupportsPsDpFilter = g_relationships.descriptors(acmEntityIndex).isPsTagged
       condenseData = False
       expandExpressionsInFtoView = False
 ' ### ENDIF IVK ###

       ReDim aggChildClassIndexes(0 To 0)
       ReDim aggChildRelIndexes(0 To 0)

       If forNl Then
         entityName = genNlObjName(g_relationships.descriptors(acmEntityIndex).relName, , forGen)
         entityShortName = genNlObjShortName(g_relationships.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Relationship (NL-Text)"
 ' ### IF IVK ###
         isPsTagged = usePsTagInNlTextTables And g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
         attrRefs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
       Else
         entityName = g_relationships.descriptors(acmEntityIndex).relName
         entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Relationship"
 ' ### IF IVK ###
         isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
         attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
       End If
   Else
     Exit Sub
   End If

   If Not generateLrt Or (ddlType = edtLdm And Not isUserTransactional) Then
     Exit Sub
   End If
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' LRT is only supported at 'pool-level'
     Exit Sub
   End If

   Dim poolSupportLrt As Boolean
   If thisPoolIndex > 0 Then
     poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
   End If

 ' ### IF IVK ###
   If ddlType = edtPdm And thisPoolIndex = g_archiveDataPoolIndex Then
     ' LRT-emulating view is implemented in Archive-module
     Exit Sub
   End If

 ' ### ENDIF IVK ###
 '  If poolsupportLrt And useMqtToImplementLrtForEntity And Not isPurelyPrivate And Not implementLrtNonMqtViewsForEntitiesSupportingMqts Then
   If poolSupportLrt And useMqtToImplementLrtForEntity And Not isPurelyPrivate Then
     Exit Sub
   End If

   Dim transformation As AttributeListTransformation
   Dim qualTabNamePub As String
   Dim qualTabNamePriv As String
   If acmEntityType = eactClass Then
       qualTabNamePub = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl)
       qualTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl)
   Else
       qualTabNamePub = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, , , forNl)
       qualTabNamePriv = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , forNl)
   End If

   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualAggHeadTabName As String
   Dim qualAggHeadLockProcName As String
   If ahClassIndex > 0 Then
     qualAggHeadTabName = genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, thisPoolIndex)
       qualAggHeadLockProcName = genQualProcName(g_classes.descriptors(ahClassIndex).sectionIndex, "LRTLOCK_" & g_classes.descriptors(ahClassIndex).className, ddlType, thisOrgIndex, thisPoolIndex)
   End If

   Dim qualViewName As String
   Dim qualViewNameLdm  As String
 ' ### IF IVK ###
   Dim showDeletedObjectsInView As Boolean
   Dim filterForPsDpMapping As Boolean
   Dim filterForPsDpMappingExtended As Boolean
 ' ### ENDIF IVK ###
   Dim tabQualifier As String
 
   If g_cfgLrtGenDB2View Then
     ' if pool does not support LRT generate View which provides the same interface as LRT-Views
     If ddlType = edtPdm And Not poolSupportLrt Then
 ' ### IF IVK ###
       ' we need to generate three views
       '   - one filtering for Product Structures in PSDPMAPPING (special feature for interfaces / first loop)
       '   - one filtering for Product Structures in PSDPMAPPING / current division (special feature for interfaces / second loop)
       '   - one not filtering for Product Structures in PSDPMAPPING (third loop)
       ' not filtering for Product Structures in PSDPMAPPING is done in second loop since this view is the one used in subsequent trigger definitions
       Dim i As Integer
       For i = 1 To 3
         filterForPsDpMapping = (i = 1)
         filterForPsDpMappingExtended = (i = 2)

         If filterForPsDpMapping And (Not supportFilteringByPsDpMapping Or Not objSupportsPsDpFilter) Then
           GoTo NextI
         End If
         If filterForPsDpMappingExtended And (Not supportFilteringByPsDpMapping Or Not objSupportsPsDpFilter) Then
           GoTo NextI
         End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
         qualViewName = _
           genQualViewNameByEntityIndex( _
             acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl, , _
             IIf(filterForPsDpMapping, "I", IIf(filterForPsDpMappingExtended, "J", "")) _
           )
 ' ### ELSE IVK ###
 '       qualViewName = _
 '         genQualViewNameByEntityIndex( _
 '           acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl _
 '         )
 ' ### ENDIF IVK ###
         printSectionHeader("LRT-emulating View for table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo)
         Print #fileNo,
         Print #fileNo, "CREATE VIEW"
         Print #fileNo, addTab(1); qualViewName
         Print #fileNo, "("
         If Not forGen And Not forNl Then
           printConditional(fileNo, _
             genAttrDeclByDomain(conWorkingState, conWorkingState, eavtEnum, getEnumIndexByName(dxnWorkingState, dnWorkingState), _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacRegular, , 1, True))
         End If

 ' ### IF IVK ###
         If condenseData Then
           ' virtually merge-in columns 'INLRT' AND 'STATUS_ID'
           printConditional(fileNo, _
             genAttrDeclByDomain(conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacLrtMeta, , 1, True))
           printConditional(fileNo, _
             genAttrDeclByDomain(enStatus, esnStatus, eavtEnum, g_enumIndexStatus, _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacLrtMeta Or eacSetProdMeta, , 1, True))
         End If

 ' ### ENDIF IVK ###
         printConditional(fileNo, _
           genAttrDeclByDomain(conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, _
             acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacRegular, , 1, True))
 ' ### IF IVK ###
         If forNl Then
           genNlsAttrDeclsForEntity(_
             acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, , _
             edomListLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone) Or _
             IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
         Else
           genAttrListForEntity(_
             acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, _
             edomListLrt Or edomListVirtual Or IIf(includeTermStringsInMqt, edomListExpression, edomNone) Or _
             IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
         End If
 ' ### ELSE IVK ###
 '       If forNl Then
 '         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, , edomListLrt Or edomLrtPriv
 '       Else
 '         genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, edomListLrt Or edomLrtPriv
 '       End If
 ' ### ENDIF IVK ###

         Print #fileNo, ")"
         Print #fileNo, "AS"
         Print #fileNo, addTab(0); "("

         If isPurelyPrivate Then
           Print #fileNo, addTab(1); "SELECT"

           If Not forGen And Not forNl Then
             Print #fileNo, addTab(2); "CAST("; CStr(workingStateUnlocked); " AS "; g_dbtEnumId; "),"
           End If

 ' ### IF IVK ###
           If condenseData Then
             ' virtually merge-in columns 'INLRT' and 'STATUS_ID'
             Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
             Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(statusProductive); "),"
           End If

 ' ### ENDIF IVK ###
           Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
 
 ' ### IF IVK ###
           If forNl Then
             genNlsAttrDeclsForEntity(_
               acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, _
               edomValue Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone) Or _
               IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
           Else
             genAttrListForEntity(_
               acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, _
               edomValue Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone) Or _
               IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
           End If
 ' ### ELSE IVK ###
 '         If forNl Then
 '           genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomValue Or edomLrtPriv
 '         Else
 '           genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomValue Or edomLrtPriv
 '         End If
 ' ### ENDIF IVK ###
 
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); "SYSIBM.SYSDUMMY1"
           Print #fileNo, addTab(1); "WHERE"
           Print #fileNo, addTab(2); "0 = 1"
         Else
           Print #fileNo, addTab(1); "SELECT"

           If Not forGen And Not forNl Then
             Print #fileNo, addTab(2); "CAST("; CStr(workingStateUnlocked); " AS "; g_dbtEnumId; "),"
           End If

 ' ### IF IVK ###
           If condenseData Then
             ' virtually merge-in columns 'INLRT' and 'STATUS_ID'
             Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
             Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(statusProductive); "),"
           End If

 ' ### ENDIF IVK ###
           Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
 
           tabQualifier = UCase(entityShortName)
           initAttributeTransformation(transformation, 0, , , , tabQualifier & ".")
           setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, tabQualifier)

 ' ### IF IVK ###
           If forNl Then
             genNlsTransformedAttrListForEntity(_
               acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , _
               edomListNonLrt Or edomValueLrt Or edomValueVirtual Or edomVirtualPersisted Or _
               IIf(includeTermStringsInMqt, edomValueExpression, edomNone) Or _
               IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
           Else
             genTransformedAttrListForEntity(_
               acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, _
               edomListNonLrt Or edomValueLrt Or edomValueVirtual Or _
               edomVirtualPersisted Or IIf(includeTermStringsInMqt, edomValueExpression, edomNone) Or _
               IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
           End If
 ' ### ELSE IVK ###
 '         If forNl Then
 '           genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomValueLrt Or edomLrtPriv
 '         Else
 '           genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt Or edomValueLrt Or edomLrtPriv
 '         End If
 ' ### ENDIF IVK ###

           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); qualTabNamePub; " "; tabQualifier

 ' ### IF IVK ###
           If isPsTagged Then
             If filterForPsDpMapping Or filterForPsDpMappingExtended Then
               Print #fileNo, addTab(1); "INNER JOIN"
               Print #fileNo, addTab(2); g_qualTabNamePsDpMapping; " PSDPM"
               Print #fileNo, addTab(1); "ON"
               Print #fileNo, addTab(2); tabQualifier; "."; g_anPsOid; " = PSDPM.PSOID"

               If thisPoolIndex = g_workDataPoolIndex Then
                 Print #fileNo, addTab(3); "AND"
                 Print #fileNo, addTab(2); tabQualifier; "."; g_anIsDeleted; " = "; gc_dbFalse
               End If

               If filterForPsDpMappingExtended Then
                 Print #fileNo, addTab(1); "INNER JOIN"
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

             If Not (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
               Print #fileNo, addTab(1); "WHERE"
               Print #fileNo, addTab(2); "("
               Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '')"
               If usePsFltrByDpMappingForRegularViews Then
                 Print #fileNo, addTab(4); "OR"
                 Print #fileNo, addTab(3); "("
                 Print #fileNo, addTab(4); "("; gc_db2RegVarPsOid; " = '0')"
                 Print #fileNo, addTab(5); "AND"
                 Print #fileNo, addTab(4); "("; tabQualifier; "."; g_anPsOid; " IN (SELECT PSOID FROM "; g_qualTabNamePsDpMapping; "))"
                 Print #fileNo, addTab(3); ")"
               End If

               If psTagOptional Then
                 Print #fileNo, addTab(4); "OR"
                 Print #fileNo, addTab(3); "("; tabQualifier; "."; g_anPsOid; " IS NULL)"
               End If

               Print #fileNo, addTab(4); "OR"
               Print #fileNo, addTab(3); "("; tabQualifier; "."; g_anPsOid; " = "; g_activePsOidDdl; ")"
               Print #fileNo, addTab(2); ")"
             End If
           End If
 ' ### ENDIF IVK ###
         End If

         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); gc_sqlCmdDelim

         If ddlType = edtPdm Then
           qualViewNameLdm = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, edtLdm, , , forGen, True, , forNl)
 ' ### IF IVK ###
           genAliasDdl(sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
              qualViewNameLdm, qualViewName, False, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, True, , filterForPsDpMapping, filterForPsDpMappingExtended, _
              "LRT-emulating View " & """" & sectionName & "." & entityName & """", , True, isPsTagged, objSupportsPsDpFilter, , , forNl)
 ' ### ELSE IVK ###
 '         genAliasDdl sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
 '           qualViewNameLdm, qualViewName, False, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, True, _
 '           "LRT-emulating View " & """" & sectionName & "." & entityName & """", , True, , forNl
 ' ### ENDIF IVK ###
         End If
 ' ### IF IVK ###

 NextI:
       Next i
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
     Else
 ' ### IF IVK ###
       ' we need to generate four views
       '   - one not filtering out deleted objects (first loop)
       '   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING (special feature for interfaces / second loop)
       '   - one filtering out deleted objects and filtering for Product Structures in PSDPMAPPING / current division (special feature for interfaces / third loop)
       '   - one filtering out deleted objects and not filtering for Product Structures in PSDPMAPPING (fourth loop)
       ' filtering deleted objects / not filtering by PSDPMAPPING is done in third loop since this view is the one used in subsequent trigger definitions
       For i = 1 To 4
         showDeletedObjectsInView = (i = 1)
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
         filterForPsDpMapping = (i = 2)
         filterForPsDpMappingExtended = (i = 3)

         If filterForPsDpMapping And (Not supportFilteringByPsDpMapping Or Not objSupportsPsDpFilter) Then
           GoTo NextII
         End If
         If filterForPsDpMappingExtended And (Not supportFilteringByPsDpMapping Or Not objSupportsPsDpFilter) Then
           GoTo NextII
         End If

         qualViewName = _
           genQualViewNameByEntityIndex( _
             acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl, , _
             IIf(showDeletedObjectsInView, "D", "") & IIf(filterForPsDpMapping, "I", IIf(filterForPsDpMappingExtended, "J", "")) _
           )

         printSectionHeader(_
           "View for 'merging' private and public LRT rows of table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo, , _
           "(" & IIf(showDeletedObjectsInView, "", "do not ") & "retrieve deleted objects" & _
           IIf(supportFilteringByPsDpMapping, " / " & IIf(filterForPsDpMapping Or filterForPsDpMappingExtended, "", "do not ") & "filter by PSDPMAPPING", "") & ")")
 ' ### ELSE IVK ###
 '       qualViewName = genQualViewNameByEntityIndex(sacmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl)
 '
 '       printSectionHeader "View for 'merging' private and public LRT rows of table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo
 ' ### ENDIF IVK ###
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE VIEW"
         Print #fileNo, addTab(1); qualViewName
         Print #fileNo, addTab(0); "("

         If Not forGen And Not forNl Then
           printConditional(fileNo, _
             genAttrDeclByDomain(conWorkingState, conWorkingState, eavtEnum, getEnumIndexByName(dxnWorkingState, dnWorkingState), _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacRegular, , 1, True))
         End If

 ' ### IF IVK ###
         If condenseData Then
           ' virtually merge-in columns 'INLRT' and 'STATUS_ID'
           printConditional(fileNo, _
             genAttrDeclByDomain(conInLrt, cosnInLrt, eavtDomain, g_domainIndexLrtId, _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacLrtMeta, , 1, True))
           printConditional(fileNo, _
             genAttrDeclByDomain(enStatus, esnStatus, eavtEnum, g_enumIndexStatus, _
               acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacLrtMeta Or eacSetProdMeta, , 1, True))
         End If

 ' ### ENDIF IVK ###
         printConditional(fileNo, _
           genAttrDeclByDomain(conInUseBy, cosnInUseBy, eavtDomain, g_domainIndexInUseBy, _
             acmEntityType, acmEntityIndex, , , ddlType, , edomListLrt, eacRegular, , 1, True))
 ' ### IF IVK ###
         If forNl Then
           genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, False, edomListLrt Or edomListVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone))
         Else
           genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, edomListLrt Or edomListVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone))
         End If
 ' ### ELSE IVK ###
 '       If forNl Then
 '         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 1, forGen, False, edomListLrt
 '       Else
 '         genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, edomListLrt
 '       End If
 ' ### ENDIF IVK ###

         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); "AS"

 ' ### IF IVK ###
         If isPurelyPrivate And (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
           Print #fileNo, addTab(0); "("
           Print #fileNo, addTab(1); "SELECT"

           If Not forGen And Not forNl Then
             Print #fileNo, addTab(2); "CAST("; CStr(workingStateUnlocked); " AS "; g_dbtEnumId; "),"
           End If

           If condenseData Then
             ' virtually merge-in columns 'INLRT' and 'STATUS_ID'
             Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
             Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(statusProductive); "),"
           End If

           Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
 
           If forNl Then
             genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, edomValue Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone))
           Else
             genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, forGen, edomValue Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone))
           End If
 
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); "SYSIBM.SYSDUMMY1"
           Print #fileNo, addTab(1); "WHERE"
           Print #fileNo, addTab(2); "0 = 1"
           Print #fileNo, addTab(0); ")"
         End If

 ' ### ENDIF IVK ###
         If Not isPurelyPrivate Then
           Print #fileNo, addTab(0); "("
           Print #fileNo, addTab(1); "SELECT"

 ' ### IF IVK ###
           If condenseData Then
             Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(workingStateUnlocked); "),"
             ' virtually merge-in columns 'INLRT' and 'STATUS_ID'
             Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
             Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(statusProductive); "),"
           ElseIf Not forGen And Not forNl Then
 ' ### ELSE IVK ###
 '         If Not forGen And Not forNl Then
 ' ### ENDIF IVK ###
             Print #fileNo, addTab(2); g_dbtEnumId; "("
             Print #fileNo, addTab(3); "CASE"
             Print #fileNo, addTab(4); "WHEN PUB."; g_anInLrt; " IS NULL THEN "; CStr(workingStateUnlocked)
 ' ### IF IVK ###
             If Not (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
               Print #fileNo, addTab(4); "WHEN PUB."; g_anInLrt; " = "; g_activeLrtOidDdl; " THEN "; CStr(workingLockedInActiveTransaction)
               Print #fileNo, addTab(4); "WHEN PUBLRT.UTROWN_OID = (SELECT UTROWN_OID FROM "; qualTabNameLrt; _
                                         " WHERE OID = "; g_activeLrtOidDdl; ") THEN "; CStr(workingLockedInInactiveTransaction)
 ' ### IF IVK ###
               If Not showDeletedObjectsInView And isAggHead Then
                 Print #fileNo, addTab(4); "WHEN ("; getActiveLrtOidStrDdl(ddlType, thisOrgIndex); " = '') AND (RTRIM(CURRENT CLIENT_USERID) = (SELECT USR."; g_anUserId; _
                                           " FROM "; g_qualTabNameUser; " USR WHERE USR."; g_anOid; " = PUBLRT.UTROWN_OID)) THEN "; CStr(workingLockedInInactiveTransaction)
               End If
             End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
             Print #fileNo, addTab(4); "ELSE "; CStr(workingLockedByOtherUser)
             Print #fileNo, addTab(3); "END"
             Print #fileNo, addTab(2); "),"
           End If

 ' ### IF IVK ###
           If filterForPsDpMapping Or filterForPsDpMappingExtended Or condenseData Then
             Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtOid; "),"
           Else
             Print #fileNo, addTab(2); "PUBLRT.UTROWN_OID,"
           End If

 ' ### ENDIF IVK ###
           initAttributeTransformation(transformation, 0, , , , "PUB.")
           setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PUB.", g_activeLrtOidDdl)
 ' ### IF IVK ###
           If forNl Then
             genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomValueLrt Or edomValueVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
           Else
             genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, _
               fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, _
               edomListNonLrt Or edomValueLrt Or edomValueVirtual Or IIf(expandExpressionsInFtoView, edomExpressionDummy, edomNone) Or edomLrtPriv)
           End If
 ' ### ELSE IVK ###
 '         If forNl Then
 '           genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomValueLrt Or edomLrtPriv
 '         Else
 '           genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt Or edomValueLrt Or edomLrtPriv
 '         End If
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); qualTabNamePub; " PUB"

 ' ### IF IVK ###
           If isPsTagged And (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
             Print #fileNo, addTab(1); "INNER JOIN"
             Print #fileNo, addTab(2); g_qualTabNamePsDpMapping; " PSDPM"
             Print #fileNo, addTab(1); "ON"
             Print #fileNo, addTab(2); "PUB."; g_anPsOid; " = PSDPM.PSOID"
             If thisPoolIndex = g_workDataPoolIndex Then
                 Print #fileNo, addTab(3); "AND"
                 Print #fileNo, addTab(2); "PUB."; g_anIsDeleted; " = "; gc_dbFalse
             End If
           End If

           If Not filterForPsDpMapping And Not filterForPsDpMappingExtended And Not condenseData Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
             Print #fileNo, addTab(1); "LEFT OUTER JOIN"
             Print #fileNo, addTab(2); qualTabNameLrt; " PUBLRT"
             Print #fileNo, addTab(1); "ON"
             Print #fileNo, addTab(2); "PUB."; g_anInLrt; " = PUBLRT."; g_anOid

             Print #fileNo, addTab(1); "WHERE"
 ' ### IF IVK ###
             If Not showDeletedObjectsInView Then
               Print #fileNo, addTab(2); "(PUB."; g_anIsDeleted; " = 0)"
               Print #fileNo, addTab(3); "AND"
             End If

             If condenseData Then
               Print #fileNo, addTab(2); "(1=1)"
             Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -6
 ' ### ENDIF IVK ###
               Print #fileNo, addTab(2); "("
               Print #fileNo, addTab(3); "(PUB."; g_anInLrt; " IS NULL)"
               Print #fileNo, addTab(4); "OR"
               Print #fileNo, addTab(3); "(PUB."; g_anInLrt; " <> "; g_activeLrtOidDdl; ")"
               Print #fileNo, addTab(2); ")"
 ' ### IF IVK ###
             End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
             If isPsTagged Then
               Print #fileNo, addTab(3); "AND"
               Print #fileNo, addTab(2); "("
               Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '')"

               If usePsFltrByDpMappingForRegularViews Then
                 Print #fileNo, addTab(4); "OR"
                 Print #fileNo, addTab(3); "("
                 Print #fileNo, addTab(4); "("; gc_db2RegVarPsOid; " = '0')"
                 Print #fileNo, addTab(5); "AND"
                 Print #fileNo, addTab(4); "(PUB."; g_anPsOid; " IN (SELECT PSOID FROM "; g_qualTabNamePsDpMapping; "))"
                 Print #fileNo, addTab(3); ")"
               End If

               If psTagOptional Then
                 Print #fileNo, addTab(4); "OR"
                 Print #fileNo, addTab(3); "(PUB."; g_anPsOid; " IS NULL)"
               End If

               Print #fileNo, addTab(4); "OR"
               Print #fileNo, addTab(3); "(PUB."; g_anPsOid; " = "; g_activePsOidDdl; ")"
               Print #fileNo, addTab(2); ")"
             End If
           End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###

           Print #fileNo, addTab(0); ")"

 ' ### IF IVK ###
           If Not filterForPsDpMapping And Not filterForPsDpMappingExtended Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
             Print #fileNo, addTab(0); "UNION ALL"
 ' ### IF IVK ###
           End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         End If

 ' ### IF IVK ###
         If Not filterForPsDpMapping And Not filterForPsDpMappingExtended Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(0); "("
           Print #fileNo, addTab(1); "SELECT"

           If Not forGen And Not forNl Then
             Print #fileNo, addTab(2); CStr(workingLockedInActiveTransaction); ","
           End If

 ' ### IF IVK ###
           If condenseData Then
             ' virtually merge-in columns 'INLRT' and 'STATUS_ID'
             Print #fileNo, addTab(2); "PRIV."; g_anInLrt; ","
             Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(statusProductive); "),"
           End If

 ' ### ENDIF IVK ###
           Print #fileNo, addTab(2); "PRIVLRT.UTROWN_OID,"

 ' ### IF IVK ###
           initAttributeTransformation(transformation, IIf(hasBeenSetProductiveInPrivLrt, 1, 2) + IIf(condenseData, 1, 0), , , , "PRIV.")
           setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, "PRIV.", g_activeLrtOidDdl)
           setAttributeMapping(transformation, 1, conIsDeleted, gc_dbFalse)
           If condenseData Then
             setAttributeMapping(transformation, 2, conInLrt, "")
           End If
           If Not hasBeenSetProductiveInPrivLrt Then
             setAttributeMapping(transformation, IIf(condenseData, 3, 2), conHasBeenSetProductive, gc_dbFalse)
           End If

           If forNl Then
             genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, True, edomListLrt Or edomValueNonLrt Or edomValueVirtual)
           Else
             genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt Or edomValueNonLrt Or edomValueVirtual)
           End If
 ' ### ELSE IVK ###
 '         initAttributeTransformation transformation, 0, , , , "PRIV."
 '         setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, "PRIV.", g_activeLrtOidDdl
 '
 '         If forNl Then
 '           genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, True, edomListLrt Or edomValueNonLrt
 '         Else
 '           genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt Or edomValueNonLrt
 '         End If
 ' ### ENDIF IVK ###

           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); qualTabNamePriv; " PRIV"

 ' ### IF IVK ###
           If isPsTagged And (filterForPsDpMapping Or filterForPsDpMappingExtended) Then
             Print #fileNo, addTab(1); "INNER JOIN"
             Print #fileNo, addTab(2); g_qualTabNamePsDpMapping; " PSDPM"
             Print #fileNo, addTab(1); "ON"
             Print #fileNo, addTab(2); "PRIV."; g_anPsOid; " = PSDPM.PSOID"

             If filterForPsDpMappingExtended Then
               Print #fileNo, addTab(1); "INNER JOIN"
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



 ' ### ENDIF IVK ###
           Print #fileNo, addTab(1); "LEFT OUTER JOIN"
           Print #fileNo, addTab(2); qualTabNameLrt; " PRIVLRT"
           Print #fileNo, addTab(1); "ON"
           Print #fileNo, addTab(2); "PRIV."; g_anInLrt; " = PRIVLRT."; g_anOid
           Print #fileNo, addTab(1); "WHERE"
           If Not showDeletedObjectsInView Then
             Print #fileNo, addTab(2); "(PRIV."; g_anLrtState; " <> "; CStr(lrtStatusDeleted); ")"
             Print #fileNo, addTab(3); "AND"
           End If
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "(PRIV."; g_anInLrt; " = "; g_activeLrtOidDdl; ")"
           Print #fileNo, addTab(2); ")"

 ' ### IF IVK ###
           If isPsTagged Then
             Print #fileNo, addTab(3); "AND"
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '')"
             If Not filterForPsDpMapping And Not filterForPsDpMappingExtended And usePsFltrByDpMappingForRegularViews Then
               Print #fileNo, addTab(4); "OR"
               Print #fileNo, addTab(3); "("
               Print #fileNo, addTab(4); "("; gc_db2RegVarPsOid; " = '0')"
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "(PRIV."; g_anPsOid; " IN (SELECT PSOID FROM "; g_qualTabNamePsDpMapping; "))"
               Print #fileNo, addTab(3); ")"
             End If

             If psTagOptional Then
               Print #fileNo, addTab(4); "OR"
               Print #fileNo, addTab(3); "(PRIV."; g_anPsOid; " IS NULL)"
             End If

             Print #fileNo, addTab(4); "OR"
             Print #fileNo, addTab(3); "(PRIV."; g_anPsOid; " = "; g_activePsOidDdl; ")"
             Print #fileNo, addTab(2); ")"
           End If
 ' ### ENDIF IVK ###

           Print #fileNo, addTab(0); ")"
 ' ### IF IVK ###
         End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###

         Print #fileNo, addTab(0); gc_sqlCmdDelim

         If (ddlType = edtPdm) And (Not useMqtToImplementLrtForEntity Or Not activateLrtMqtViews Or isPurelyPrivate) Then
           qualViewNameLdm = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, edtLdm, , , forGen, True, , forNl)
 ' ### IF IVK ###
           genAliasDdl(sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
             qualViewNameLdm, qualViewName, False, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, True, showDeletedObjectsInView, filterForPsDpMapping, filterForPsDpMappingExtended, _
             "LRT-View" & IIf(showDeletedObjectsInView, " (include deleted objects)", "") & _
             IIf(supportFilteringByPsDpMapping, " (" & IIf(filterForPsDpMapping, "", "do not ") & "filter by PSDPMAPPING)", "") & _
             " """ & sectionName & "." & entityName & """", , True, isPsTagged, objSupportsPsDpFilter, , , forNl)
 ' ### ELSE IVK ###
 '         genAliasDdl sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
 '           qualViewNameLdm, qualViewName, False, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, True, _
 '           "LRT-View """ & sectionName & "." & entityName & """", , True, , forNl
 ' ### ENDIF IVK ###
         End If
 ' ### IF IVK ###
 
 NextII:
       Next i
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
     End If
   End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genLrtSupportTriggerForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNoClView As Integer, _
   fileNoTrigger As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional isPurelyPrivate As Boolean = False _
 )
   Dim sectionName As String
   Dim sectionShortName As String
   Dim sectionIndex As Integer
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim isUserTransactional As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim entityInsertable As Boolean
   Dim entityUpdatable As Boolean
   Dim entityDeletable As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim dbAcmEntityType As String
   Dim attrRefs As AttrDescriptorRefs
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
   Dim ahClassIndex As Integer
   Dim ahClassIdStr As String
   Dim aggChildClassIndexes() As Integer
   Dim aggChildRelIndexes() As Integer
   Dim useMqtToImplementLrtForEntity As Boolean
   Dim isSubjectToActivation As Boolean
   Dim busKeyAttrListNoFks As String
   Dim busKeyAttrArrayNoFks() As String
   Dim logLastChange As Boolean
 ' ### IF IVK ###
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
   Dim hasNoIdentity As Boolean
   Dim isNational As Boolean
   Dim ignorePsRegVarOnInsertDelete As Boolean
   Dim numGroupIdAttrs As Integer
   Dim groupIdAttrIndexes() As Integer
   Dim hasExpBasedVirtualAttrs As Boolean
   Dim condenseData As Boolean
   Dim isGenericAspectHead As Boolean ' GenericAspects always need special treatment ;-)
 ' ### ENDIF IVK ###

   On Error GoTo ErrorExit

 ' ### IF IVK ###
   isGenericAspectHead = False
 ' ### ENDIF IVK ###
   busKeyAttrListNoFks = ""

   ReDim groupIdAttrIndexes(0 To 0)

   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_classes.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       logLastChange = g_classes.descriptors(acmEntityIndex).logLastChange
 ' ### IF IVK ###
       entityInsertable = (g_classes.descriptors(acmEntityIndex).updateMode And eupmInsert)
       entityUpdatable = (g_classes.descriptors(acmEntityIndex).updateMode And eupmUpdate)
       entityDeletable = (g_classes.descriptors(acmEntityIndex).updateMode And eupmDelete)
 ' ### ELSE IVK ###
 '     entityInsertable = True
 '     entityUpdatable = True
 '     entityDeletable = True
 ' ### ENDIF IVK ###
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       ahClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       ahClassIdStr = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
       aggChildClassIndexes = g_classes.descriptors(acmEntityIndex).aggChildClassIndexes
       aggChildRelIndexes = g_classes.descriptors(acmEntityIndex).aggChildRelIndexes
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
       If g_classes.descriptors(acmEntityIndex).hasBusinessKey Then
         busKeyAttrListNoFks = getPkAttrListByClassIndex(acmEntityIndex, ddlType, , , , True)
         genAttrList(busKeyAttrArrayNoFks, busKeyAttrListNoFks)
       End If

 ' ### IF IVK ###
       isGenericAspectHead = g_classes.descriptors(acmEntityIndex).classIndex = g_classIndexGenericAspect And Not forGen And Not forNl
 
       isSubjectToActivation = (g_classes.descriptors(acmEntityIndex).hasPriceAssignmentAggHead Or g_classes.descriptors(acmEntityIndex).hasPriceAssignmentSubClass) And g_classes.descriptors(acmEntityIndex).superClassIndex <= 0
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData

       If g_classes.descriptors(acmEntityIndex).hasGroupIdAttrInNonGenInclSubClasses And Not forNl And Not forGen Then
         groupIdAttrIndexes = g_classes.descriptors(acmEntityIndex).groupIdAttrIndexesInclSubclasses
         numGroupIdAttrs = UBound(groupIdAttrIndexes) - LBound(groupIdAttrIndexes) + 1
       End If

       ignorePsRegVarOnInsertDelete = g_classes.descriptors(acmEntityIndex).ignPsRegVarOnInsDel

 ' ### ENDIF IVK ###
       If forNl Then
         entityName = genNlObjName(g_classes.descriptors(acmEntityIndex).className, , forGen)
         entityShortName = genNlObjShortName(g_classes.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Class (NL-Text)"
         hasOwnTable = True
         isAbstract = False
         attrRefs = g_classes.descriptors(acmEntityIndex).nlAttrRefsInclSubclasses
         relRefs.numRefs = 0
         isGenForming = False
 ' ### IF IVK ###
         isPsTagged = usePsTagInNlTextTables And g_classes.descriptors(acmEntityIndex).isPsTagged
         psTagOptional = usePsTagInNlTextTables And g_classes.descriptors(acmEntityIndex).psTagOptional
         hasNoIdentity = False
         isNational = False
         hasExpBasedVirtualAttrs = False
 ' ### ENDIF IVK ###
       Else
         entityName = g_classes.descriptors(acmEntityIndex).className
         entityShortName = g_classes.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Class"
         hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
         isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
         attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
         relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
         isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
 ' ### IF IVK ###
         isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
         psTagOptional = g_classes.descriptors(acmEntityIndex).psTagOptional
         hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
         isNational = g_classes.descriptors(acmEntityIndex).isNationalizable

         hasExpBasedVirtualAttrs = Not forNl And ((forGen And g_classes.descriptors(acmEntityIndex).hasExpBasedVirtualAttrInGenInclSubClasses) Or (Not forGen And g_classes.descriptors(acmEntityIndex).hasExpBasedVirtualAttrInNonGenInclSubClasses))
 ' ### ENDIF IVK ###
       End If
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_relationships.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       entityInsertable = True
       entityUpdatable = True
       entityDeletable = True
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       dbAcmEntityType = "R"
       ahClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       ahClassIdStr = g_relationships.descriptors(acmEntityIndex).aggHeadClassIdStr
       relRefs.numRefs = 0
       isGenForming = False
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
 ' ### IF IVK ###
       hasNoIdentity = False
       isNational = False
       psTagOptional = False
       hasExpBasedVirtualAttrs = False
       isSubjectToActivation = g_relationships.descriptors(acmEntityIndex).hasPriceAssignmentAggHead And g_relationships.descriptors(acmEntityIndex).maxLeftCardinality < 0 And g_relationships.descriptors(acmEntityIndex).maxRightCardinality < 0 And g_relationships.descriptors(acmEntityIndex).reusedRelIndex <= 0
       condenseData = False
 
       numGroupIdAttrs = 0
       ignorePsRegVarOnInsertDelete = False
 ' ### ENDIF IVK ###
 
       ReDim aggChildClassIndexes(0 To 0)
       ReDim aggChildRelIndexes(0 To 0)

       If forNl Then
         entityName = genNlObjName(g_relationships.descriptors(acmEntityIndex).relName, , forGen)
         entityShortName = genNlObjShortName(g_relationships.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Relationship (NL-Text)"
 ' ### IF IVK ###
         isPsTagged = usePsTagInNlTextTables And g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
         attrRefs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
       Else
         entityName = g_relationships.descriptors(acmEntityIndex).relName
         entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Relationship"
 ' ### IF IVK ###
         isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
         attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
       End If
   Else
     Exit Sub
   End If

   If Not generateLrt Or (ddlType = edtLdm And Not isUserTransactional) Then
     Exit Sub
   End If
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' LRT is only supported at 'pool-level'
     Exit Sub
   End If

   Dim poolSupportLrt As Boolean
   If thisPoolIndex > 0 Then
     poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
   Else
     poolSupportLrt = (ddlType = edtLdm)
   End If
 ' ### IF IVK ###
   If ddlType = edtPdm And thisPoolIndex = g_archiveDataPoolIndex Then
     ' LRT-emulating view is implemented in Archive-module
     Exit Sub
   End If

 ' ### ENDIF IVK ###
   If ddlType = edtPdm And forMqt And Not poolSupportLrt Then
     ' LRT-emulating view is only supported in non-MQT-mode
     Exit Sub
   End If

   If poolSupportLrt And useMqtToImplementLrtForEntity And Not forMqt And Not isPurelyPrivate And Not implementLrtNonMqtViewsForEntitiesSupportingMqts Then
     Exit Sub
   End If

   If Not g_cfgLrtGenDB2Trigger Then
     Exit Sub
   End If

   Dim transformation As AttributeListTransformation

   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNamePub As String
   Dim qualTabNamePriv As String
   If acmEntityType = eactClass Then
       qualTabNamePub = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl)
       qualTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl)
   Else
       qualTabNamePub = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, , , forNl)
       qualTabNamePriv = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , forNl)
   End If

   Dim unQualTabNamePub As String
   Dim unQualTabNamePriv As String
   unQualTabNamePub = getUnqualObjName(qualTabNamePub)
   unQualTabNamePriv = getUnqualObjName(qualTabNamePriv)

   Dim qualTabNameAggHeadPub As String
   Dim qualViewNameAggHead As String
   Dim qualTabNameAggHeadPriv As String
   Dim qualProcNameAggHeadLock As String
   If ahClassIndex > 0 Then
     qualTabNameAggHeadPub = genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, thisPoolIndex)
     qualTabNameAggHeadPriv = genQualTabNameByClassIndex(ahClassIndex, ddlType, thisOrgIndex, thisPoolIndex, , True)

       qualViewNameAggHead = genQualViewNameByClassIndex(g_classes.descriptors(ahClassIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , True, g_classes.descriptors(ahClassIndex).useMqtToImplementLrt)
       qualProcNameAggHeadLock = genQualProcNameByEntityIndex(g_classes.descriptors(ahClassIndex).classIndex, eactClass, ddlType, thisOrgIndex, thisPoolIndex, , , , , "LRTLOCK")
   End If

   Dim qualSeqNameGroupId As String

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim qualTabNameChangeLog As String
   qualTabNameChangeLog = genQualTabNameByClassIndex(g_classIndexChangeLog, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualViewName As String
 
   Dim nextTrIndex As Integer
   Dim thisEntityTabColumns As EntityColumnDescriptors
 ' ### IF IVK ###
   Dim genericAspectTabColumns As EntityColumnDescriptors
   Dim numVirtualAttrs As Integer
 ' ### ENDIF IVK ###

   Dim forDeletedObjects As Boolean
   Dim nameSuffix As String
   Dim l As Integer
 ' ### IF IVK ###
   For l = 1 To IIf((isGenericAspectHead Or (acmEntityType = eactClass And (acmEntityIndex = g_classIndexTypeSpec Or acmEntityIndex = g_classIndexTypeStandardEquipment))) And poolSupportLrt, 2, 1)
 ' ### ELSE IVK ###
 ' For l = 1 To 2
 ' ### ENDIF IVK ###
     forDeletedObjects = (l = 2)
     nameSuffix = IIf(forDeletedObjects, "D", "")

     qualViewName = _
       genQualViewNameByEntityIndex( _
         acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, forMqt, forNl, , nameSuffix _
       )

 ' ### IF IVK ###
     If isSubjectToActivation And ((ddlType = edtLdm And Not forMqt) Or poolSupportLrt) And Not forNl And _
        (Not forMqt Or Not implementLrtNonMqtViewsForEntitiesSupportingMqts) And _
        Not forDeletedObjects Then
       ' ####################################################################################################################
       ' #    ChangeLog View for Public Update
       ' ####################################################################################################################

       genChangeLogViewDdl(_
         acmEntityIndex, acmEntityType, qualTabNamePub, "", "", qualTabNamePub, "", qualTabNameAggHeadPub, _
         thisOrgIndex, thisPoolIndex, thisPoolIndex, fileNoClView, ddlType, forGen, eclPubUpdate)
     End If

 ' ### ENDIF IVK ###
     Dim qualTriggerName As String

     ' ####################################################################################################################
     ' #    INSERT Trigger
     ' ####################################################################################################################

 ' ### IF IVK ###
     qualTriggerName = _
       genQualTriggerNameByEntityIndex( _
         acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , IIf(forNl, "NL", "") & IIf(forMqt, "M", "") & nameSuffix & "LRT_INS", eondmNone _
       )
 ' ### ELSE IVK ###
 '   qualTriggerName = _
 '     genQualTriggerNameByEntityIndex( _
 '       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, forMqt, forNl, , nameSuffix & IIf(nameSuffix = "", "", "_") & "INS" _
 '     )
 ' ### ENDIF IVK ###

     If ddlType = edtPdm And Not poolSupportLrt Then
       printSectionHeader("Insert-Trigger for LRT-emulating view on table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoTrigger)
       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(0); "CREATE TRIGGER"
       Print #fileNoTrigger, addTab(1); qualTriggerName
       Print #fileNoTrigger, addTab(0); "INSTEAD OF INSERT ON"
       Print #fileNoTrigger, addTab(1); qualViewName
       Print #fileNoTrigger, addTab(0); "REFERENCING"
       Print #fileNoTrigger, addTab(1); "NEW AS "; gc_newRecordName
       Print #fileNoTrigger, addTab(0); "FOR EACH ROW"
       Print #fileNoTrigger, addTab(0); "BEGIN ATOMIC"

       If Not entityInsertable And generateUpdatableCheckInUpdateTrigger Then
         genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName)
       ElseIf isPurelyPrivate Then
         genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName)
       Else
 ' ### IF IVK ###
         If isPsTagged And (usePsTagInNlTextTables Or Not forNl) Then
           genProcSectionHeader(fileNoTrigger, "declare variables")
           genSigMsgVarDecl(fileNoTrigger)

           ' note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar' and 'v_PsOid'
           genPsCheckDdlForInsertDelete(_
             fileNoTrigger, gc_newRecordName & "." & g_anPsOid, ddlType, thisOrgIndex, ignorePsRegVarOnInsertDelete, _
             psTagOptional, , False, "v_psOidRecord", "v_psOidRegVar", "v_psOid", , qualViewName, gc_newRecordName & "." & g_anOid)
         End If

 ' ### ENDIF IVK ###
         genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, tvFalse, 1)

         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(1); "INSERT INTO"
         Print #fileNoTrigger, addTab(2); qualTabNamePub
         Print #fileNoTrigger, addTab(1); "("

         If forNl Then
           genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomListNonLrt)
         Else
           genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomListNonLrt)
         End If

         Print #fileNoTrigger, addTab(1); ")"
         Print #fileNoTrigger, addTab(1); "VALUES"
         Print #fileNoTrigger, addTab(1); "("

 ' ### IF IVK ###
         initAttributeTransformation(transformation, IIf(ignorePsRegVarOnInsertDelete, 0, 1), , , , gc_newRecordName & ".")
         If Not ignorePsRegVarOnInsertDelete Then
           setAttributeMapping(transformation, 1, conPsOid, "v_psOid")
         End If
 ' ### ELSE IVK ###
 '       initAttributeTransformation transformation, 0, , , , gc_newRecordName & "."
 ' ### ENDIF IVK ###

         If forNl Then
           genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomDefaultValue)
         Else
           genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt Or edomDefaultValue)
         End If

         Print #fileNoTrigger, addTab(1); ");"
       End If
       Print #fileNoTrigger, "END"
       Print #fileNoTrigger, gc_sqlCmdDelim
     ElseIf Not (isPurelyPrivate And forMqt) Then
       printSectionHeader("Insert-Trigger for 'public-private' LRT-view """ & qualViewName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoTrigger)
       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(0); "CREATE TRIGGER"
       Print #fileNoTrigger, addTab(1); qualTriggerName
       Print #fileNoTrigger, addTab(0); "INSTEAD OF INSERT ON"
       Print #fileNoTrigger, addTab(1); qualViewName
       Print #fileNoTrigger, addTab(0); "REFERENCING"
       Print #fileNoTrigger, addTab(1); "NEW AS "; gc_newRecordName
       Print #fileNoTrigger, addTab(0); "FOR EACH ROW"
       Print #fileNoTrigger, addTab(0); "BEGIN ATOMIC"

       genProcSectionHeader(fileNoTrigger, "declare variables")
       genSigMsgVarDecl(fileNoTrigger)
       genVarDecl(fileNoTrigger, "v_lrtOid", g_dbtOid, "0")
       genVarDecl(fileNoTrigger, "v_lrtClosed", g_dbtBoolean, "NULL")
       genVarDecl(fileNoTrigger, "v_now", "TIMESTAMP", "CURRENT TIMESTAMP")
       genVarDecl(fileNoTrigger, "v_privRecordExists", g_dbtBoolean, gc_dbFalse)

       If Not isPurelyPrivate Then
         genVarDecl(fileNoTrigger, "v_privOwnerId", g_dbtLrtId, "NULL")
         genVarDecl(fileNoTrigger, "v_pubRecordExists", g_dbtBoolean, gc_dbFalse)
         genVarDecl(fileNoTrigger, "v_pubOwnerId", g_dbtLrtId, "NULL")

         If (qualTabNamePub <> qualTabNameAggHeadPub) And (ahClassIndex > 0) Then
           genVarDecl(fileNoTrigger, "v_pubOwnerUserId", g_dbtUserId, "NULL")
           genVarDecl(fileNoTrigger, "v_inLrt", g_dbtOid, "0")
         End If
 ' ### IF IVK ###

         If maintainGroupIdColumnsInLrtTrigger And (numGroupIdAttrs > 0) Then
           Dim i As Integer
           For i = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
               genVarDecl(fileNoTrigger, "v_" & g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName, getDbDatatypeByDomainIndex(g_attributes.descriptors(groupIdAttrIndexes(i)).domainIndex), "NULL")
           Next i
         End If
 ' ### ENDIF IVK ###
       End If

       genVarDecl(fileNoTrigger, "v_lrtExecutedOperation", "INTEGER", CStr(lrtStatusCreated))
       genVarDecl(fileNoTrigger, "v_lrtEntityIdCount", "INTEGER", "0")

 ' ### IF IVK ###
       If hasExpBasedVirtualAttrs And maintainVirtAttrInTriggerOnEntityTabs Then
         thisEntityTabColumns = nullEntityColumnDescriptors
         initAttributeTransformation(transformation, 0)
         transformation.doCollectVirtualAttrDescriptors = True
         transformation.doCollectAttrDescriptors = True
         setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName)

         genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, thisEntityTabColumns, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomXref)

         numVirtualAttrs = 0
         Dim k As Integer
         For k = 1 To thisEntityTabColumns.numDescriptors
             If thisEntityTabColumns.descriptors(k).columnCategory And eacVirtual Then
               genVarDecl(fileNoTrigger, "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName, getDbDatatypeByDomainIndex(thisEntityTabColumns.descriptors(k).dbDomainIndex), "NULL")
               numVirtualAttrs = numVirtualAttrs + 1
             End If
         Next k
       End If

       Dim useDivOidHandling As Boolean
       useDivOidHandling = (ahClassIndex = g_classIndexGenericCode) And (qualTabNamePub <> qualTabNameAggHeadPub) And Not isPsTagged

       Dim useDivOidWhereClause As Boolean
       useDivOidWhereClause = (ahClassIndex = g_classIndexGenericCode) And Not isPsTagged

       Dim useDivRelKey As Boolean
       useDivRelKey = (acmEntityIndex = g_classIndexGenericCode) And Not forNl


       If useDivOidHandling Then
         genVarDecl(fileNoTrigger, "v_DivOid", "BIGINT", "NULL")
       End If

       If isPsTagged And (usePsTagInNlTextTables Or Not forNl) Then
         ' note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar' and 'v_psOid'
         genPsCheckDdlForInsertDelete(_
           fileNoTrigger, gc_newRecordName & "." & g_anPsOid, ddlType, thisOrgIndex, , _
           psTagOptional, , False, "v_psOidRecord", "v_psOidRegVar", , , qualViewName, gc_newRecordName & "." & g_anOid)
       ElseIf qualTabNamePub <> qualTabNameAggHeadPub Then
         genPsCheckDdlForNonPsTaggedInLrt(fileNoTrigger, ddlType, thisOrgIndex, , False)
       End If

 ' ### ENDIF IVK ###
       genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

       genProcSectionHeader(fileNoTrigger, "determine LRT OID")
       Print #fileNoTrigger, addTab(1); "SET v_lrtOid = (CASE "; gc_db2RegVarLrtOid; " WHEN '' THEN CAST(NULL AS "; g_dbtOid; ") ELSE "; _
                                        g_activeLrtOidDdl; " END);"



 ' ### IF IVK ###
       If hasExpBasedVirtualAttrs And maintainVirtAttrInTriggerOnEntityTabs Then
         Dim printedHeader As Boolean
         printedHeader = False
         ' to minimze number of calls to UDFs always call the LRT-version - use LRTOID = NULL if no LRT-context is set
         transformation.conEnumLabelText.lrtOidRef = IIf(maintainVirtAttrInTriggerPrivOnEntityTabs, "v_lrtOid", "")

         For k = 1 To thisEntityTabColumns.numDescriptors
             If thisEntityTabColumns.descriptors(k).columnCategory And eacVirtual Then
               If Not printedHeader Then
                 genProcSectionHeader(fileNoTrigger, "initialize variables for virtual attributes", 1)
                 printedHeader = True
               End If
               Dim virtAttrStr As String
               virtAttrStr = transformAttrName(thisEntityTabColumns.descriptors(k).columnName, eavtDomain, thisEntityTabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, thisEntityTabColumns.descriptors(k).acmAttributeIndex, edomValueVirtual)
               If maintainVirtAttrInTriggerPubOnEntityTabs And maintainVirtAttrInTriggerPrivOnEntityTabs Then
                 Print #fileNoTrigger, addTab(1); "SET "; "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName; " = "; virtAttrStr; ";"
               ElseIf maintainVirtAttrInTriggerPubOnEntityTabs Then
                 Print #fileNoTrigger, addTab(1); "SET "; "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName; " = (CASE WHEN v_lrtOid IS NULL THEN "; virtAttrStr; " ELSE "; gc_newRecordName; "."; UCase(thisEntityTabColumns.descriptors(k).acmAttributeName); " END);"
               ElseIf maintainVirtAttrInTriggerPrivOnEntityTabs Then
                 Print #fileNoTrigger, addTab(1); "SET "; "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName; " = (CASE WHEN v_lrtOid IS NOT NULL THEN "; virtAttrStr; " ELSE "; gc_newRecordName; "."; UCase(thisEntityTabColumns.descriptors(k).acmAttributeName); " END);"
               End If
             End If
         Next k
       End If

 ' ### ENDIF IVK ###
       genProcSectionHeader(fileNoTrigger, "if no LRT-ID is given, insert in public table")
       Print #fileNoTrigger, addTab(1); "IF v_lrtOid IS NULL THEN"

       If useDivOidHandling Then
         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(2); "SET v_DivOid = (SELECT CDIDIV_OID FROM "; qualTabNameAggHeadPub; " WHERE OID = NEWRECORD.AHOID);"
       End If

       If isPurelyPrivate Then
         genProcSectionHeader(fileNoTrigger, "not supported - table is purely private", 2, True)
       Else
 ' ### IF IVK ###
         If maintainGroupIdColumnsInLrtTrigger And (numGroupIdAttrs > 0) Then
           For i = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
               Print #fileNoTrigger, addTab(2); "IF "; gc_newRecordName; "."; g_anCid; " IN ('"; getClassIdStrByIndex(g_attributes.descriptors(groupIdAttrIndexes(i)).acmEntityIndex); "') THEN"
               genProcSectionHeader(fileNoTrigger, "determine value of group-ID column """ & g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName & """", 3, True)
               Print #fileNoTrigger, addTab(3); "SET v_"; g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName; " = ("
               Print #fileNoTrigger, addTab(4); "SELECT"
               Print #fileNoTrigger, addTab(5); UCase(g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName)
               Print #fileNoTrigger, addTab(4); "FROM"
               Print #fileNoTrigger, addTab(5); qualTabNamePub; " PUB"
               Print #fileNoTrigger, addTab(4); "WHERE"
               Print #fileNoTrigger, addTab(5); "PUB."; g_anCid; " IN ('"; getClassIdStrByIndex(g_attributes.descriptors(groupIdAttrIndexes(i)).acmEntityIndex); "')"
               Print #fileNoTrigger, addTab(6); "AND"
               Dim j As Integer
               For j = LBound(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes) To UBound(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes)
                 Dim v1 As String
                 Dim v2 As String
                 Dim maxVarNameLength As Integer
                 ' Fixme: get rid of this hard-coding
                 maxVarNameLength = 29
 
                 If Left(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j), 1) = "#" Then
                   v1 = paddRight(mapExpression(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j), thisOrgIndex, thisPoolIndex, ddlType, "PUB"), maxVarNameLength)
                   v2 = paddRight(mapExpression(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j), thisOrgIndex, thisPoolIndex, ddlType, gc_newRecordName), maxVarNameLength)
                 Else
                   v1 = paddRight("PUB." & UCase(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j)), maxVarNameLength)
                   v2 = paddRight(gc_newRecordName & "." & UCase(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j)), maxVarNameLength)
                 End If
 
                 Print #fileNoTrigger, addTab(5); "((("; v1; " IS NULL) AND ("; v2; " IS NULL)) OR ("; v1; " =  "; v2; "))"; IIf(j < UBound(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes), " AND", "")
               Next j
               Print #fileNoTrigger, addTab(4); "FETCH FIRST 1 ROW ONLY"
               Print #fileNoTrigger, addTab(3); ");"

               Print #fileNoTrigger,
               Print #fileNoTrigger, addTab(3); "IF v_"; g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName; " IS NULL THEN"
               qualSeqNameGroupId = genQualObjName(sectionIndex, "SEQ_" & entityShortName & g_attributes.descriptors(groupIdAttrIndexes(i)).shortName, "SEQ_" & entityShortName & g_attributes.descriptors(groupIdAttrIndexes(i)).shortName, ddlType, thisOrgIndex)

               Print #fileNoTrigger, addTab(4); "SET v_"; g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName; " = NEXTVAL FOR "; qualSeqNameGroupId; ";"
               Print #fileNoTrigger, addTab(3); "END IF;"
               Print #fileNoTrigger, addTab(2); "END IF;"
               Print #fileNoTrigger,
           Next i
         End If

 ' ### ENDIF IVK ###
         Print #fileNoTrigger, addTab(2); "INSERT INTO"
         Print #fileNoTrigger, addTab(3); qualTabNamePub
         Print #fileNoTrigger, addTab(2); "("

 ' ### IF IVK ###
         If forNl Then
           genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
         Else
           genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
         End If
 ' ### ELSE IVK ###
 '       If forNl Then
 '         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt
 '       Else
 '         genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt
 '       End If
 ' ### ENDIF IVK ###

         Print #fileNoTrigger, addTab(2); ")"
         Print #fileNoTrigger, addTab(2); "VALUES"
         Print #fileNoTrigger, addTab(2); "("

 ' ### IF IVK ###
         initAttributeTransformation(transformation, 6 + numVirtualAttrs + IIf(maintainGroupIdColumnsInLrtTrigger, numGroupIdAttrs, 0), , , , gc_newRecordName & ".")
 ' ### ELSE IVK ###
 '       initAttributeTransformation transformation, 5, , , , gc_newRecordName & "."
 ' ### ENDIF IVK ###

         setAttributeMapping(transformation, 1, conLrtState, CStr(lrtStatusCreated))
         setAttributeMapping(transformation, 2, conInLrt, "CAST(NULL AS " & g_dbtLrtId & ")")
         setAttributeMapping(transformation, 3, conCreateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anCreateTimestamp & ", v_now)", , , True)
         setAttributeMapping(transformation, 4, conLastUpdateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anLastUpdateTimestamp & ", v_now)", , , True)
 ' ### IF IVK ###
         If (useDivOidHandling) Then
             setAttributeMapping(transformation, 5, conDivOid, "v_DivOid")
         Else
             setAttributeMapping(transformation, 5, conPsOid, "v_psOid")
         End If
         setAttributeMapping(transformation, 6, conHasBeenSetProductive, gc_dbFalse)
         nextTrIndex = 7
 
         If hasExpBasedVirtualAttrs And maintainVirtAttrInTriggerOnEntityTabs Then
           For k = 1 To thisEntityTabColumns.numDescriptors
               If thisEntityTabColumns.descriptors(k).columnCategory And eacVirtual Then
                 setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors(k).columnName, "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName)
                 nextTrIndex = nextTrIndex + 1
               End If
           Next k
         End If
 
         If maintainGroupIdColumnsInLrtTrigger And (numGroupIdAttrs > 0) Then
           For i = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
               setAttributeMapping(transformation, nextTrIndex, g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName, "v_" & g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName)
               nextTrIndex = nextTrIndex + 1
           Next i
         End If
 ' ### ENDIF IVK ###
 
 ' ### IF IVK ###
         If forNl Then
           genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, _
             forGen, False, , edomListNonLrt Or edomDefaultValue Or edomListVirtual Or edomVirtualPersisted)
         Else
           genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, , _
             False, forGen, edomListNonLrt Or edomDefaultValue Or edomListVirtual Or edomVirtualPersisted)
         End If
 ' ### ELSE IVK ###
 '       If forNl Then
 '         genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, _
 '           forGen, False, , edomListNonLrt Or edomDefaultValue
 '       Else
 '         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, , _
 '           False, forGen, edomListNonLrt Or edomDefaultValue
 '       End If
 ' ### ENDIF IVK ###

         Print #fileNoTrigger, addTab(2); ");"

         Print #fileNoTrigger,
       End If

       Print #fileNoTrigger, addTab(1); "ELSE"

       genVerifyActiveLrtDdl(fileNoTrigger, ddlType, qualTabNameLrt, "v_lrtOid", 2, True)
 ' ### IF IVK ###
       genStatusCheckDdl(fileNoTrigger, gc_newRecordName, , 2)
 ' ### ENDIF IVK ###

       If Not isPurelyPrivate Then
         genProcSectionHeader(fileNoTrigger, "check if " & gc_newRecordName & " already exists as 'public record' (v_pubRecordExists = 1)", 2)

 ' ### IF IVK ###
         If condenseData Then
           If isPsTagged And (usePsTagInNlTextTables Or Not forNl) Then
             Print #fileNoTrigger, addTab(2); "IF EXISTS(SELECT 1 FROM "; qualTabNamePub; " PUB WHERE PUB."; g_anOid; " = "; gc_newRecordName; "."; g_anOid; " AND PUB."; conPsOid; " = "; gc_newRecordName; "."; conPsOid; " ) THEN"
           Else
             Print #fileNoTrigger, addTab(2); "IF EXISTS(SELECT 1 FROM "; qualTabNamePub; " PUB WHERE PUB."; g_anOid; " = "; gc_newRecordName; "."; g_anOid; " ) THEN"
           End If
           Print #fileNoTrigger, addTab(3); "SET v_pubRecordExists = "; gc_dbTrue; ";"
           Print #fileNoTrigger, addTab(2); "ELSE"
           Print #fileNoTrigger, addTab(3); "SET v_pubRecordExists = "; gc_dbFalse; ";"
           Print #fileNoTrigger, addTab(2); "END IF;"
         Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
           Print #fileNoTrigger, addTab(2); "SET v_pubOwnerId = NULL;"
           Print #fileNoTrigger, addTab(2); "SET v_pubOwnerId ="
           Print #fileNoTrigger, addTab(3); "("
           Print #fileNoTrigger, addTab(4); "SELECT"
           Print #fileNoTrigger, addTab(5); "COALESCE(PUB."; g_anInLrt; ",-1)"
           Print #fileNoTrigger, addTab(4); "FROM"
           Print #fileNoTrigger, addTab(5); qualTabNamePub; " PUB"
           Print #fileNoTrigger, addTab(4); "WHERE"
           Print #fileNoTrigger, addTab(5); "PUB."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
           genDdlPsDivClause(fileNoTrigger, 5, "PUB", gc_newRecordName, gc_newRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
           Print #fileNoTrigger, addTab(4); "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)"
           Print #fileNoTrigger, addTab(3); ");"
           Print #fileNoTrigger, addTab(2); "SET v_pubRecordExists = (CASE WHEN v_pubOwnerId IS NULL THEN 0 ELSE 1 END);"
           Print #fileNoTrigger, addTab(2); "SET v_pubOwnerId = (CASE WHEN v_pubOwnerId = -1 THEN NULL ELSE v_pubOwnerId END);"
 ' ### IF IVK ###
         End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
       End If

       genProcSectionHeader(fileNoTrigger, "check if " & gc_newRecordName & " already exists as 'private record' (v_privRecordExists > 0)", 2)
       If isPurelyPrivate Then
         If isPsTagged And (usePsTagInNlTextTables Or Not forNl) Then
           Print #fileNoTrigger, addTab(2); "IF EXISTS(SELECT 1 FROM "; qualTabNamePriv; " PRIV WHERE PRIV."; g_anOid; " = "; gc_newRecordName; "."; g_anOid; " AND PRIV."; conPsOid; " = "; gc_newRecordName; "."; conPsOid; " ) THEN"
         Else
           Print #fileNoTrigger, addTab(2); "IF EXISTS(SELECT 1 FROM "; qualTabNamePriv; " PRIV WHERE PRIV."; g_anOid; " = "; gc_newRecordName; "."; g_anOid; " ) THEN"
         End If
         Print #fileNoTrigger, addTab(3); "SET v_privRecordExists = "; gc_dbTrue; ";"
         Print #fileNoTrigger, addTab(2); "ELSE"
         Print #fileNoTrigger, addTab(3); "SET v_privRecordExists = "; gc_dbFalse; ";"
         Print #fileNoTrigger, addTab(2); "END IF;"
       Else
         Print #fileNoTrigger, addTab(2); "SET v_privOwnerId = NULL;"
         Print #fileNoTrigger, addTab(2); "SET v_privOwnerId ="
         Print #fileNoTrigger, addTab(3); "("
         Print #fileNoTrigger, addTab(4); "SELECT"
         Print #fileNoTrigger, addTab(5); "COALESCE(PRIV."; g_anInLrt; ",-1)"
         Print #fileNoTrigger, addTab(4); "FROM"
         Print #fileNoTrigger, addTab(5); qualTabNamePriv; " PRIV"
         Print #fileNoTrigger, addTab(4); "WHERE"
         Print #fileNoTrigger, addTab(5); "PRIV."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
         genDdlPsDivClause(fileNoTrigger, 5, "PRIV", gc_newRecordName, gc_newRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
         Print #fileNoTrigger, addTab(4); "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)"
         Print #fileNoTrigger, addTab(3); ");"
         Print #fileNoTrigger, addTab(2); "SET v_privRecordExists = (CASE WHEN v_privOwnerId IS NULL THEN 0 ELSE 1 END);"
         Print #fileNoTrigger, addTab(2); "SET v_privOwnerId = (CASE WHEN v_privOwnerId = -1 THEN NULL ELSE v_privOwnerId END);"
      End If

       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(2); "IF v_privRecordExists = 1 THEN"

       genSignalDdlWithParmsForCompoundSql("recordPrivatelyExists", fileNoTrigger, 3, , , , , , , , , , "RTRIM(CHAR(" & gc_newRecordName & ".OID))")

       Print #fileNoTrigger, addTab(2); "ELSE"

       If isPurelyPrivate Then
         genProcSectionHeader(fileNoTrigger, gc_newRecordName & " is a new 'private record'", 3)
       Else
         genProcSectionHeader(fileNoTrigger, "record does not exist 'in private' - make sure it does not exist 'in public'", 3, True)
         Print #fileNoTrigger, addTab(3); "IF v_pubRecordExists = 1 THEN"
         genSignalDdlWithParmsForCompoundSql("lrtInsAlready", fileNoTrigger, 4, unQualTabNamePriv, , , , , , , , , "RTRIM(CHAR(" & gc_newRecordName & "." & g_anOid & "))")
         Print #fileNoTrigger, addTab(3); "END IF;"

         If qualTabNamePub <> qualTabNameAggHeadPub And ahClassIndex > 0 Then
           ' lock the 'public aggregate head record' with this LRT-OID
           genAggHeadLockPropDdl(fileNoTrigger, gc_newRecordName, ahClassIndex, qualTabNameAggHeadPub, qualTabNameAggHeadPriv, qualTabNameLrtAffectedEntity, "v_pubOwnerUserId", ddlType, thisOrgIndex, thisPoolIndex, 3, (isPsTagged And (usePsTagInNlTextTables Or Not forNl)), useDivOidWhereClause, useDivRelKey)
         End If

 ' ### IF IVK ###
         If maintainGroupIdColumnsInLrtTrigger And (numGroupIdAttrs > 0) Then
           For i = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
               Print #fileNoTrigger, addTab(3); "IF "; gc_newRecordName; "."; g_anCid; " IN ('"; getClassIdStrByIndex(g_attributes.descriptors(groupIdAttrIndexes(i)).acmEntityIndex); "') THEN"
               genProcSectionHeader(fileNoTrigger, "determine value of group-ID column """ & g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName & """", 4, True)
               Print #fileNoTrigger, addTab(4); "SET v_"; g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName; " = ("
               Print #fileNoTrigger, addTab(5); "SELECT"
               Print #fileNoTrigger, addTab(6); UCase(g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName)
               Print #fileNoTrigger, addTab(5); "FROM"
               Print #fileNoTrigger, addTab(6); qualTabNamePriv; " PRIV"
               Print #fileNoTrigger, addTab(5); "WHERE"
               Print #fileNoTrigger, addTab(6); "PRIV."; g_anInLrt; " = v_lrtOid"
               Print #fileNoTrigger, addTab(7); "AND"
               Print #fileNoTrigger, addTab(6); "PRIV."; g_anCid; " IN ('"; getClassIdStrByIndex(g_attributes.descriptors(groupIdAttrIndexes(i)).acmEntityIndex); "')"

               Print #fileNoTrigger, addTab(7); "AND"
               For j = LBound(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes) To UBound(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes)
                 ' Fixme: get rid of this hard-coding
                 maxVarNameLength = 29
 
                 If Left(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j), 1) = "#" Then
                   v1 = paddRight(mapExpression(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j), thisOrgIndex, thisPoolIndex, ddlType, "PRIV", , "v_lrtOid"), maxVarNameLength)
                   v2 = paddRight(mapExpression(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j), thisOrgIndex, thisPoolIndex, ddlType, gc_newRecordName, , "v_lrtOid"), maxVarNameLength)
                 Else
                   v1 = paddRight("PRIV." & UCase(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j)), maxVarNameLength)
                   v2 = paddRight(gc_newRecordName & "." & UCase(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes(j)), maxVarNameLength)
                 End If
 
                 Print #fileNoTrigger, addTab(6); "((("; v1; " IS NULL) AND ("; v2; " IS NULL)) OR ("; v1; " =  "; v2; "))"; IIf(j < UBound(g_attributes.descriptors(groupIdAttrIndexes(i)).groupIdAttributes), " AND", "")
               Next j

               Print #fileNoTrigger, addTab(5); "FETCH FIRST 1 ROW ONLY"
               Print #fileNoTrigger, addTab(4); ");"

               Print #fileNoTrigger,
               Print #fileNoTrigger, addTab(4); "IF v_"; g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName; " IS NULL THEN"
               qualSeqNameGroupId = genQualObjName(sectionIndex, "SEQ_" & entityShortName & g_attributes.descriptors(groupIdAttrIndexes(i)).shortName, "SEQ_" & entityShortName & g_attributes.descriptors(groupIdAttrIndexes(i)).shortName, ddlType, thisOrgIndex)

               Print #fileNoTrigger, addTab(5); "SET v_"; g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName; " = NEXTVAL FOR "; qualSeqNameGroupId; ";"
               Print #fileNoTrigger, addTab(4); "END IF;"
               Print #fileNoTrigger, addTab(3); "END IF;"
           Next i
         End If

 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNoTrigger, "record neither exists 'in private' nor 'in public' - consider " & gc_newRecordName & " as new 'private record'", 3)
       End If

       If useDivOidHandling Then
         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(3); "SET v_DivOid = (CASE WHEN NEWRECORD.DIV_OID IS NULL THEN (SELECT CDIDIV_OID FROM "; qualTabNameAggHeadPriv; " WHERE OID = NEWRECORD.AHOID) ELSE NEWRECORD.DIV_OID END);"
       End If
 
       Print #fileNoTrigger, addTab(3); "INSERT INTO"
       Print #fileNoTrigger, addTab(4); qualTabNamePriv
       Print #fileNoTrigger, addTab(3); "("

 ' ### IF IVK ###
       If forNl Then
         genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
       Else
         genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
       End If
 ' ### ELSE IVK ###
 '     If forNl Then
 '       genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt
 '     Else
 '       genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt
 '     End If
 ' ### ENDIF IVK ###

       Print #fileNoTrigger, addTab(3); ")"
       Print #fileNoTrigger, addTab(3); "VALUES"
       Print #fileNoTrigger, addTab(3); "("

 ' ### IF IVK ###
       initAttributeTransformation(transformation, 7 + numVirtualAttrs + IIf(maintainGroupIdColumnsInLrtTrigger, numGroupIdAttrs, 0), , , , gc_newRecordName & ".")
 ' ### ELSE IVK ###
 '     initAttributeTransformation transformation, 4, , , , gc_newRecordName & "."
 ' ### ENDIF IVK ###

       setAttributeMapping(transformation, 1, conLrtState, CStr(lrtStatusCreated))
       setAttributeMapping(transformation, 2, conInLrt, "v_lrtOid")
       setAttributeMapping(transformation, 3, conCreateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anCreateTimestamp & ", v_now)", , , True)
       setAttributeMapping(transformation, 4, conLastUpdateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anLastUpdateTimestamp & ", v_now)", , , True)
 ' ### IF IVK ###
       If (useDivOidHandling) Then
         setAttributeMapping(transformation, 5, conDivOid, "v_DivOid")
       Else
         setAttributeMapping(transformation, 5, conPsOid, "v_psOid")
       End If
       setAttributeMapping(transformation, 6, conHasBeenSetProductive, gc_dbFalse)
       setAttributeMapping(transformation, 7, conStatusId, "COALESCE(" & gc_newRecordName & "." & g_anStatus & ", " & statusWorkInProgress & ")", , , True)
       nextTrIndex = 8

       If hasExpBasedVirtualAttrs And maintainVirtAttrInTriggerOnEntityTabs Then
         For k = 1 To thisEntityTabColumns.numDescriptors
             If thisEntityTabColumns.descriptors(k).columnCategory And eacVirtual Then
               setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors(k).columnName, "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName)
               nextTrIndex = nextTrIndex + 1
             End If
         Next k
       End If

       If maintainGroupIdColumnsInLrtTrigger And (numGroupIdAttrs > 0) Then
         For i = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
             setAttributeMapping(transformation, nextTrIndex, g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName, "v_" & g_attributes.descriptors(groupIdAttrIndexes(i)).attributeName)
             nextTrIndex = nextTrIndex + 1
         Next i
       End If

       If forNl Then
         genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , _
           edomListLrt Or edomDefaultValue Or edomListVirtual Or edomVirtualPersisted)
       Else
         genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , _
           True, forGen, edomListLrt Or edomDefaultValue Or edomListVirtual Or edomVirtualPersisted)
       End If
 ' ### ELSE IVK ###
 '     If forNl Then
 '       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , _
 '         edomListLrt Or edomDefaultValue
 '     Else
 '       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , _
 '         True, forGen, edomListLrt Or edomDefaultValue
 '     End If
 ' ### ENDIF IVK ###

       Print #fileNoTrigger, addTab(3); ");"
       Print #fileNoTrigger, addTab(2); "END IF;"

 ' ### IF IVK ###
       genDdlForUpdateAffectedEntities(fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, _
         entityIdStr, ahClassIdStr, "v_lrtOid", 2, , Not condenseData)
 ' ### ELSE IVK ###
 '     genDdlForUpdateAffectedEntities fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, _
 '       entityIdStr, ahClassIdStr, "v_lrtOid", 2
 ' ### ENDIF IVK ###

       Print #fileNoTrigger, addTab(1); "END IF;"

       genDdlForUpdateLrtLastOpTs(fileNoTrigger, thisOrgIndex, thisPoolIndex, "v_lrtOid", "v_now", ddlType)

       Print #fileNoTrigger, addTab(0); "END"
       Print #fileNoTrigger, addTab(0); gc_sqlCmdDelim
     End If

 ' ### IF IVK ###
     If ddlType = edtPdm And poolSupportLrt And Not forNl And Not forGen And _
        (Not forMqt Or Not implementLrtNonMqtViewsForEntitiesSupportingMqts) And Not forDeletedObjects And (acmEntityType = eactClass) Then
         If IIf(useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt, Not forMqt Or Not implementLrtNonMqtViewsForEntitiesSupportingMqts, True) And g_classes.descriptors(acmEntityIndex).supportAhStatusPropagation And g_classes.descriptors(acmEntityIndex).isAggHead Then
           ' ####################################################################################################################
           ' #    Procedure for propagating status update from aggregate head to aggregate children
           ' ####################################################################################################################

           Dim qualProcName As String
           qualProcName = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, spnAHPropagateStatus)

           printSectionHeader("Procedure for propagating status update from aggregate head to aggregate children (ACM-Class """ & sectionName & "." & entityName & """)", fileNoTrigger)

           Print #fileNoTrigger,
           Print #fileNoTrigger, addTab(0); "CREATE PROCEDURE"
           Print #fileNoTrigger, addTab(1); qualProcName
           Print #fileNoTrigger, addTab(0); "("
           genProcParm(fileNoTrigger, "IN", "psOid_in", g_dbtOid, True, "PS-OID of the row to propagate the status for")
           genProcParm(fileNoTrigger, "IN", "oid_in", g_dbtOid, True, "OID of the row to propagate the status for")
           genProcParm(fileNoTrigger, "OUT", "rowCount_out", "INTEGER", False, "number of records updated")
           Print #fileNoTrigger, addTab(0); ")"
           Print #fileNoTrigger, addTab(0); "RESULT SETS 0"
           Print #fileNoTrigger, addTab(0); "LANGUAGE SQL"
           Print #fileNoTrigger, addTab(0); "BEGIN"

           genProcSectionHeader(fileNoTrigger, "declare variables")
           genSigMsgVarDecl(fileNoTrigger)
           genVarDecl(fileNoTrigger, "v_statusId", g_dbtEnumId, "NULL")
           genVarDecl(fileNoTrigger, "v_classId", g_dbtEntityId, "NULL")
           genVarDecl(fileNoTrigger, "v_rowCount", "INTEGER", "0")
           genSpLogDecl(fileNoTrigger)

           genSpLogProcEnter(fileNoTrigger, qualProcName, ddlType, , "psOid_in", "oid_in", "rowCount_out")
 
           genProcSectionHeader(fileNoTrigger, "determine aggregate's status", 1)
           Print #fileNoTrigger, addTab(1); "SELECT"
           Print #fileNoTrigger, addTab(2); g_anStatus; ","

           If g_classes.descriptors(acmEntityIndex).hasOwnTable Then
             Print #fileNoTrigger, addTab(2); "'"; g_classes.descriptors(acmEntityIndex).classIdStr; "'"
           Else
             Print #fileNoTrigger, addTab(2); g_anCid
           End If

           Print #fileNoTrigger, addTab(1); "INTO"
           Print #fileNoTrigger, addTab(2); "v_statusId,"
           Print #fileNoTrigger, addTab(2); "v_classId"
           Print #fileNoTrigger, addTab(1); "FROM"
           Print #fileNoTrigger, addTab(2); qualTabNamePub
           Print #fileNoTrigger, addTab(1); "WHERE"
           Print #fileNoTrigger, addTab(2); g_anOid; " = oid_in"
           If isPsTagged Then
             Print #fileNoTrigger, addTab(3); "AND"
             Print #fileNoTrigger, addTab(2); g_anPsOid; " = psOid_in"
           End If
           Print #fileNoTrigger, addTab(1); ";"

           genProcSectionHeader(fileNoTrigger, "if record does not exist there is nothing to propagate", 1)
           Print #fileNoTrigger, addTab(1); "IF v_statusId IS NULL THEN"
           genSpLogProcEscape(fileNoTrigger, qualProcName, ddlType, 2, "psOid_in", "oid_in", "rowCount_out")
           genSignalDdlWithParms("ahStatusPropNotFound", fileNoTrigger, 2, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(oid_in))")
           Print #fileNoTrigger, addTab(1); "END IF;"
 
           genProcSectionHeader(fileNoTrigger, "initialize output parameter")
           Print #fileNoTrigger, addTab(1); "SET rowCount_out = 0;"
 
           Print #fileNoTrigger,
           For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes)
               If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isUserTransactional And Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isCommonToOrgs And Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isCommonToPools And g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).superClassIndex <= 0 Then
                 If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex <> acmEntityIndex Then
                   ' set status for base table
                   genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child class '" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).sectionName & "." & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).className & "'", 1, True)
                   genDdlForAggStatusProp(genQualTabNameByClassIndex(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, thisPoolIndex), fileNoTrigger, 1, _
                     "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out")
                 End If

                 If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).hasNlAttrsInNonGenInclSubClasses Then
                   ' set status for NL-Text table
                   genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child class '" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).sectionName & "." & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).className & "' (NL_TEXT)", 1, True)
                   genDdlForAggStatusProp(genQualTabNameByClassIndex(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , True), fileNoTrigger, 1, _
                     "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out")
                 End If

                 If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isGenForming And Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).hasNoIdentity Then
                   ' set status for GENtable
                   genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child class '" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).sectionName & "." & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).className & "' (GEN)", 1, True)
                   genDdlForAggStatusProp(genQualTabNameByClassIndex(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, thisPoolIndex, True), fileNoTrigger, 1, _
                     "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out")

                   If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).hasNlAttrsInGenInclSubClasses Then
                     ' set status for NL-Text GEN-table
                     genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child class '" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).sectionName & "." & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).className & "' (GEN/NL_TEXT)", 1, True)
                     genDdlForAggStatusProp(genQualTabNameByClassIndex(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , , True), fileNoTrigger, 1, _
                       "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out")
                   End If
                 End If
               End If
           Next i

           For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes)
               If g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).isUserTransactional And Not g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).isCommonToOrgs And Not g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).isCommonToPools Then
                 ' set status for relationship table
                 genProcSectionHeader(fileNoTrigger, "propagate status to aggregate child relationship '" & g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).sectionName & "." & g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).relName & "'", 1, True)
                 genDdlForAggStatusProp(genQualTabNameByRelIndex(g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).relIndex, ddlType, thisOrgIndex, thisPoolIndex), fileNoTrigger, 1, _
                   "oid_in", "v_statusId", "v_classId", "v_rowCount", "rowCount_out")
               End If
           Next i

           genSpLogProcExit(fileNoTrigger, qualProcName, ddlType, , "psOid_in", "oid_in", "rowCount_out")

           Print #fileNoTrigger, addTab(0); "END"
           Print #fileNoTrigger, addTab(0); gc_sqlCmdDelim
         End If
     End If

 ' ### ENDIF IVK ###
     ' ####################################################################################################################
     ' #    UPDATE Trigger
     ' ####################################################################################################################

 ' ### ENDIF IVK ###
     qualTriggerName = _
       genQualTriggerNameByEntityIndex( _
         acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , IIf(forNl, "NL", "") & IIf(forMqt, "M", "") & nameSuffix & "LRT_UPD" _
       )
 ' ### ENDIF IVK ###
 '   qualTriggerName = _
 '     genQualTriggerNameByEntityIndex( _
 '       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, forMqt, forNl, , nameSuffix & IIf(nameSuffix = "", "", "_") & "UPD" _
 '     )
 ' ### ENDIF IVK ###

     If ddlType = edtPdm And Not poolSupportLrt Then
       printSectionHeader("Update-Trigger for LRT-emulating view on table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoTrigger)
       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(0); "CREATE TRIGGER"
       Print #fileNoTrigger, addTab(1); qualTriggerName
       Print #fileNoTrigger, addTab(0); "INSTEAD OF UPDATE ON"
       Print #fileNoTrigger, addTab(1); qualViewName
       Print #fileNoTrigger, addTab(0); "REFERENCING"
       Print #fileNoTrigger, addTab(1); "OLD AS "; gc_oldRecordName
       Print #fileNoTrigger, addTab(1); "NEW AS "; gc_newRecordName
       Print #fileNoTrigger, addTab(0); "FOR EACH ROW"
       Print #fileNoTrigger, addTab(0); "BEGIN ATOMIC"

       useDivOidWhereClause = (ahClassIndex = g_classIndexGenericCode) And Not isPsTagged
       useDivRelKey = (acmEntityIndex = g_classIndexGenericCode) And Not forNl

       If Not entityUpdatable And generateUpdatableCheckInUpdateTrigger Then
         genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName)
       ElseIf isPurelyPrivate Then
         genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName)
 ' ### IF IVK ###
       ElseIf condenseData Then
         genSignalDdl("updateNotAllowed", fileNoTrigger, 1, entityName)
 ' ### ENDIF IVK ###
       Else
 ' ### IF IVK ###
         If isPsTagged And (usePsTagInNlTextTables Or Not forNl) Then
           genProcSectionHeader(fileNoTrigger, "declare variables")
           genSigMsgVarDecl(fileNoTrigger)

           ' note: this procedure declares variables 'v_psOidRecord', 'v_psOidRegVar' and 'v_psOid'
           genPsCheckDdlForUpdate(fileNoTrigger, gc_oldRecordName & "." & g_anPsOid, gc_newRecordName & "." & g_anPsOid, ddlType, thisOrgIndex, psTagOptional, 1, False, , , , qualViewName, gc_oldRecordName & "." & g_anOid)
         End If

         genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, tvFalse, 1)

 ' ### ENDIF IVK ###
         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(1); "UPDATE"
         Print #fileNoTrigger, addTab(2); qualTabNamePub
         Print #fileNoTrigger, addTab(1); "SET"
         Print #fileNoTrigger, addTab(1); "("

         If forNl Then
           genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomListNonLrt)
         Else
           genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomListNonLrt)
         End If

         Print #fileNoTrigger, addTab(1); ")"
         Print #fileNoTrigger, addTab(1); "="
         Print #fileNoTrigger, addTab(1); "("

 ' ### IF IVK ###
         initAttributeTransformation(transformation, 1, , , , gc_newRecordName & ".")
         setAttributeMapping(transformation, 1, conPsOid, "v_psOid")
 ' ### ELSE IVK ###
 '       initAttributeTransformation transformation, 0, , , , gc_newRecordName & "."
 ' ### ENDIF IVK ###

         If forNl Then
           genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, , edomListNonLrt Or edomDefaultValue)
         Else
           genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt Or edomDefaultValue)
         End If

         Print #fileNoTrigger, addTab(1); ")"
         Print #fileNoTrigger, addTab(1); "WHERE"
         Print #fileNoTrigger, addTab(2); g_anOid; " = "; gc_oldRecordName; "."; g_anOid
         genDdlPsDivClause(fileNoTrigger, 2, "", "", gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
         Print #fileNoTrigger, addTab(1); ";"
       End If
       Print #fileNoTrigger, "END"
       Print #fileNoTrigger, gc_sqlCmdDelim
     ElseIf Not (isPurelyPrivate And forMqt) Then
       printSectionHeader("Update-Trigger for 'public-private' LRT-view """ & qualViewName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoTrigger)

       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(0); "CREATE TRIGGER"
       Print #fileNoTrigger, addTab(1); qualTriggerName
       Print #fileNoTrigger, addTab(0); "INSTEAD OF UPDATE ON"
       Print #fileNoTrigger, addTab(1); qualViewName
       Print #fileNoTrigger, addTab(0); "REFERENCING"
       Print #fileNoTrigger, addTab(1); "OLD AS "; gc_oldRecordName
       Print #fileNoTrigger, addTab(1); "NEW AS "; gc_newRecordName
       Print #fileNoTrigger, addTab(0); "FOR EACH ROW"
       Print #fileNoTrigger, addTab(0); "BEGIN ATOMIC"

       genProcSectionHeader(fileNoTrigger, "declare variables")
       genSigMsgVarDecl(fileNoTrigger)

       genVarDecl(fileNoTrigger, "v_lrtOid", g_dbtOid, "0")
       genVarDecl(fileNoTrigger, "v_lrtClosed", g_dbtBoolean, "NULL")
       genVarDecl(fileNoTrigger, "v_now", "TIMESTAMP", "CURRENT TIMESTAMP")
       genVarDecl(fileNoTrigger, "v_privRecordExists", g_dbtBoolean, gc_dbFalse)
       genVarDecl(fileNoTrigger, "v_privRecordCountDeleted", "INTEGER", "0")
       genVarDecl(fileNoTrigger, "v_privOwnerId", g_dbtLrtId, "NULL")

       If Not isPurelyPrivate Then
         genVarDecl(fileNoTrigger, "v_pubRecordExists", g_dbtBoolean, gc_dbFalse)
 ' ### IF IVK ###
       End If
       If Not isPurelyPrivate And Not condenseData Then
 ' ### ENDIF IVK ###
         genVarDecl(fileNoTrigger, "v_pubOwnerId", g_dbtLrtId, "NULL")

         If (qualTabNamePub <> qualTabNameAggHeadPub) And (ahClassIndex > 0) Then
           genVarDecl(fileNoTrigger, "v_pubOwnerUserId", g_dbtUserId, "NULL")
           genVarDecl(fileNoTrigger, "v_inLrt", g_dbtOid, "0")
         End If

         genVarDecl(fileNoTrigger, "v_oidCount", "INTEGER", "0")

         If thisOrgIndex <> g_primaryOrgIndex And poolSupportLrt Then
           genVarDecl(fileNoTrigger, "v_isFtoLrt", g_dbtBoolean, gc_dbFalse)
           genVarDecl(fileNoTrigger, "v_entityLabel", "VARCHAR(90)", "'" & getPrimaryEntityLabelByIndex(acmEntityType, acmEntityIndex) & "'")
           If busKeyAttrListNoFks <> "" And acmEntityType = eactClass And Not forNl Then
             genVarDecl(fileNoTrigger, "v_busKeyValues", "VARCHAR(200)", "NULL")
           End If
         End If
       End If

       genVarDecl(fileNoTrigger, "v_lrtExecutedOperation", "INTEGER", CStr(lrtStatusUpdated))
       genVarDecl(fileNoTrigger, "v_lrtEntityIdCount", "INTEGER", "0")

 ' ### IF IVK ###
       If isGenericAspectHead Then
         genVarDecl(fileNoTrigger, "v_logRecordOid", g_dbtOid, "0")
         genVarDecl(fileNoTrigger, "v_cdUserId", g_dbtUserId, "NULL")
         genVarDecl(fileNoTrigger, "v_divisionOid", g_dbtOid, "NULL")
       End If

       If hasExpBasedVirtualAttrs And maintainVirtAttrInTriggerOnEntityTabs Then
         thisEntityTabColumns = nullEntityColumnDescriptors
         initAttributeTransformation(transformation, 0)
         transformation.doCollectVirtualAttrDescriptors = True
         transformation.doCollectAttrDescriptors = True
         setAttributeTransformationContext(transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName)

         genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, thisEntityTabColumns, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomXref)

         numVirtualAttrs = 0
         For k = 1 To thisEntityTabColumns.numDescriptors
             If thisEntityTabColumns.descriptors(k).columnCategory And eacVirtual Then
               genVarDecl(fileNoTrigger, "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName, getDbDatatypeByDomainIndex(thisEntityTabColumns.descriptors(k).dbDomainIndex), "NULL")
               numVirtualAttrs = numVirtualAttrs + 1
             End If
         Next k
       End If

       If isPsTagged And (usePsTagInNlTextTables Or Not forNl) Then
         ' note: this procedure declares variables 'v_psOidRecord' and 'v_psOidRegVar'
         genPsCheckDdlForUpdate(_
           fileNoTrigger, gc_oldRecordName & "." & g_anPsOid, gc_newRecordName & "." & g_anPsOid, ddlType, thisOrgIndex, _
           psTagOptional, 1, False, , , , qualViewName, gc_oldRecordName & "." & g_anOid)
       ElseIf qualTabNamePub <> qualTabNameAggHeadPub Then
         genPsCheckDdlForNonPsTaggedInLrt(fileNoTrigger, ddlType, thisOrgIndex, , False)
       End If

 ' ### ENDIF IVK ###
       genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

       genProcSectionHeader(fileNoTrigger, "determine LRT OID")
       Print #fileNoTrigger, addTab(1); "SET v_lrtOid = (CASE "; gc_db2RegVarLrtOid; " WHEN '' THEN CAST(NULL AS "; g_dbtOid; ") ELSE "; _
                                        g_activeLrtOidDdl; " END);"

 ' ### IF IVK ###
       If hasExpBasedVirtualAttrs And maintainVirtAttrInTriggerOnEntityTabs Then
         printedHeader = False
         For k = 1 To thisEntityTabColumns.numDescriptors
             If thisEntityTabColumns.descriptors(k).columnCategory And eacVirtual Then
               If Not printedHeader Then
                 genProcSectionHeader(fileNoTrigger, "initialize variables for virtual attributes", 1)
                 printedHeader = True
               End If
               If maintainVirtAttrInTriggerPubOnEntityTabs And maintainVirtAttrInTriggerPrivOnEntityTabs Then
                 Print #fileNoTrigger, addTab(1); "SET "; "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName; " = "; gc_oldRecordName; "."; UCase(thisEntityTabColumns.descriptors(k).acmAttributeName); ";"
               ElseIf maintainVirtAttrInTriggerPubOnEntityTabs Then
                 Print #fileNoTrigger, addTab(1); "SET "; "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName; " = (CASE WHEN v_lrtOid IS NULL THEN "; gc_oldRecordName; "."; UCase(thisEntityTabColumns.descriptors(k).acmAttributeName); " ELSE "; gc_newRecordName; "."; UCase(thisEntityTabColumns.descriptors(k).acmAttributeName); " END);"
               ElseIf maintainVirtAttrInTriggerPrivOnEntityTabs Then
                 Print #fileNoTrigger, addTab(1); "SET "; "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName; " = (CASE WHEN v_lrtOid IS NOT NULL THEN "; gc_oldRecordName; "."; UCase(thisEntityTabColumns.descriptors(k).acmAttributeName); " ELSE "; gc_newRecordName; "."; UCase(thisEntityTabColumns.descriptors(k).acmAttributeName); " END);"
               End If
             End If
         Next k
       End If

 ' ### ENDIF IVK ###
       genProcSectionHeader(fileNoTrigger, "if no LRT-ID is given, update in public table")
       Print #fileNoTrigger, addTab(1); "IF (v_lrtOid IS NULL) THEN"

       If isPurelyPrivate Then
         genProcSectionHeader(fileNoTrigger, "not supported - table is purely private", 2, True)
 ' ### IF IVK ###
       ElseIf condenseData Then
         genProcSectionHeader(fileNoTrigger, "not supported - table does not support 'update in public'", 2, True)
 ' ### ENDIF IVK ###
       Else
         Dim indentOffset As Integer
         indentOffset = 0

 ' ### IF IVK ###
         ' GenericAspects always need special treatment ;-)
         If isGenericAspectHead And Not generateFwkTest Then

           Print #fileNoTrigger, addTab(2); "IF"
 
           genericAspectTabColumns = nullEntityColumnDescriptors

           initAttributeTransformation(transformation, 5, , True)
           setAttributeMapping(transformation, 1, conStatusId, "", , , True)
           setAttributeMapping(transformation, 2, conIsBlockedPrice, "", , , False)
           setAttributeMapping(transformation, 3, conCreateTimestamp, "", , , False)
           setAttributeMapping(transformation, 4, conLastUpdateTimestamp, "", , , False)
           setAttributeMapping(transformation, 5, conVersionId, "", , , False)
 
           genTransformedAttrListForEntityWithColReuse(acmEntityIndex, acmEntityType, transformation, genericAspectTabColumns, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, , , edomNone)

           Dim invalidValue As String
           Dim colValueDescr As String
           Dim colValueDescrLen As Integer
           Dim spaces As String
           spaces = Space(41)
           For k = 1 To genericAspectTabColumns.numDescriptors
               If (genericAspectTabColumns.descriptors(k).columnCategory And eacExpression) = 0 Then
                 invalidValue = "XX"
                   If (g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etChar Or g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etClob Or g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etLongVarchar Or g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etVarchar) Then
                     invalidValue = "''"
                   ElseIf (g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etBigInt Or g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etSmallint Or g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etInteger Or g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etBoolean) Then
                     invalidValue = "-1"
                   ElseIf (g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etDate) Then
                     invalidValue = "DATE('0001-01-01')"
                   ElseIf (g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType = etDecimal) Then
                     invalidValue = "DECIMAL(-0.000000001)"
                   Else
                     logMsg("data type """ & g_domains.descriptors(genericAspectTabColumns.descriptors(k).dbDomainIndex).dataType & """ not (yet) supported in LRT-update-trigger", ellError, edtNone)
                   End If
                 colValueDescr = genericAspectTabColumns.descriptors(k).columnName & "," & invalidValue
                 colValueDescrLen = Len(colValueDescr)
                 If colValueDescrLen < 40 Then colValueDescrLen = 40

                 Print #fileNoTrigger, addTab(3); "(COALESCE("; gc_newRecordName; "."; Left(colValueDescr & ")" & spaces, colValueDescrLen + 1); " <>"; _
                                                  " COALESCE("; gc_oldRecordName; "."; Left(colValueDescr & ")" & spaces, colValueDescrLen + 1); ")"; _
                                                  IIf(k = genericAspectTabColumns.numDescriptors, "", " OR")
               End If
           Next k

           Print #fileNoTrigger, addTab(2); "THEN"
           indentOffset = 1
         End If

 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNoTrigger, "verify that record is not locked", 2 + indentOffset, True)
         Print #fileNoTrigger, addTab(2 + indentOffset); "SET v_oidCount ="
         Print #fileNoTrigger, addTab(3 + indentOffset); "("
         Print #fileNoTrigger, addTab(4 + indentOffset); "SELECT"
         Print #fileNoTrigger, addTab(5 + indentOffset); "COUNT(*)"
         Print #fileNoTrigger, addTab(4 + indentOffset); "FROM"
         Print #fileNoTrigger, addTab(5 + indentOffset); qualTabNamePub; " PUB"
         Print #fileNoTrigger, addTab(4 + indentOffset); "WHERE"
         Print #fileNoTrigger, addTab(5 + indentOffset); "PUB."; g_anInLrt; " IS NOT NULL"
         Print #fileNoTrigger, addTab(6 + indentOffset); "AND"
         Print #fileNoTrigger, addTab(5 + indentOffset); "PUB."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
         genDdlPsDivClause(fileNoTrigger, 5 + indentOffset, "PUB", "", gc_newRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
         Print #fileNoTrigger, addTab(3 + indentOffset); ");"
         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(2 + indentOffset); "IF v_oidCount > 0 THEN"
         genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 3 + indentOffset, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(" & gc_newRecordName & "." & g_anOid & "))")
         Print #fileNoTrigger, addTab(2 + indentOffset); "END IF;"

 ' ### IF IVK ###
         If isGenericAspectHead And Not generateFwkTest Then
           Print #fileNoTrigger, addTab(2); "END IF;"
         End If

         If isGenericAspectHead And Not generateFwkTest Then
             genProcSectionHeader(fileNoTrigger, "determine division OID", 2)
             Print #fileNoTrigger, addTab(2); "SET v_divisionOid ="
             Print #fileNoTrigger, addTab(3); "("
             Print #fileNoTrigger, addTab(4); "SELECT"
             Print #fileNoTrigger, addTab(5); "PDIDIV_OID"
             Print #fileNoTrigger, addTab(4); "FROM"
             Print #fileNoTrigger, addTab(5); g_qualTabNameProductStructure
             Print #fileNoTrigger, addTab(4); "WHERE"
             Print #fileNoTrigger, addTab(5); g_anOid & " = v_psOid"
             Print #fileNoTrigger, addTab(3); ");"

             genProcSectionHeader(fileNoTrigger, "create changelog entry if " & g_anStatus & " or " & g_anIsBlockedPrice & " has changed", 2)
             Print #fileNoTrigger, addTab(2); "IF ("; gc_newRecordName; "."; g_anStatus; ""; " <> "; gc_oldRecordName; "."; g_anStatus; ") OR"
             Print #fileNoTrigger, addTab(2); "   ("; gc_newRecordName; "."; g_anIsBlockedPrice; " <> "; gc_oldRecordName; "."; g_anIsBlockedPrice; ") THEN"

             genProcSectionHeader(fileNoTrigger, "determine current user id (for changelog)", 3, True)
             Print #fileNoTrigger, addTab(3); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'MIG_NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
             Print #fileNoTrigger, addTab(2); "END IF;"

             Print #fileNoTrigger,
             Print #fileNoTrigger, addTab(2); "IF "; gc_newRecordName; "."; g_anStatus; ""; " <> "; gc_oldRecordName; "."; g_anStatus; " THEN"

             Print #fileNoTrigger, addTab(3); "SET v_logRecordOid = NEXTVAL FOR "; qualSeqNameOid; ";"

             genGenChangeLogRecordDdl(_
               acmEntityIndex, acmEntityType, qualTabNamePub, qualTabNamePriv, qualSeqNameOid, _
               qualTabNameChangeLog, "update of '" & g_anStatus & "'", "", thisOrgIndex, thisPoolIndex, _
               fileNoTrigger, ddlType, , , g_anStatus, , etSmallint, eclPubUpdate, eacSetProdMeta, 3, _
               gc_oldRecordName & "." & g_anStatus, gc_newRecordName & "." & g_anStatus, _
               gc_oldRecordName & "." & g_anOid, "v_logRecordOid", "v_cdUserId", CStr(lrtStatusUpdated), , , "v_divisionOid", g_classIndexCodePriceAssignment)
             Print #fileNoTrigger, addTab(2); "END IF;"

             Print #fileNoTrigger,
             Print #fileNoTrigger, addTab(2); "IF "; gc_newRecordName; "."; g_anIsBlockedPrice; " <> "; gc_oldRecordName; "."; g_anIsBlockedPrice; " THEN"

             Print #fileNoTrigger, addTab(3); "SET v_logRecordOid = NEXTVAL FOR "; qualSeqNameOid; ";"

             genGenChangeLogRecordDdl(_
               acmEntityIndex, acmEntityType, qualTabNamePub, qualTabNamePriv, qualSeqNameOid, _
               qualTabNameChangeLog, "update of '" & g_anIsBlockedPrice & "'", "", thisOrgIndex, thisPoolIndex, _
               fileNoTrigger, ddlType, , , g_anIsBlockedPrice, , etBoolean, eclPubUpdate, eacRegular, 3, _
               gc_oldRecordName & "." & g_anIsBlockedPrice, gc_newRecordName & "." & g_anIsBlockedPrice, _
               gc_oldRecordName & "." & g_anOid, "v_logRecordOid", "v_cdUserId", CStr(lrtStatusUpdated), , , "v_divisionOid", g_classIndexCodePriceAssignment)

             Print #fileNoTrigger, addTab(2); "END IF;"
         End If

 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNoTrigger, "update record in public table", 2)
         Print #fileNoTrigger, addTab(2); "UPDATE"
         Print #fileNoTrigger, addTab(3); qualTabNamePub; " PUB"
         Print #fileNoTrigger, addTab(2); "SET"
         Print #fileNoTrigger, addTab(2); "("

 ' ### IF IVK ###
         If forNl Then
           genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
         Else
           genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
         End If
 ' ### ELSE IVK ###
 '       If forNl Then
 '         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt
 '       Else
 '         genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt
 '       End If
 ' ### ENDIF IVK ###

         Print #fileNoTrigger, addTab(2); ")"
         Print #fileNoTrigger, addTab(2); "="
         Print #fileNoTrigger, addTab(2); "("

 ' ### IF IVK ###
         initAttributeTransformation(transformation, 5 + numVirtualAttrs, , , , gc_newRecordName & ".")
 ' ### ELSE IVK ###
 '       initAttributeTransformation transformation, 3, , , , gc_newRecordName & "."
 ' ### ENDIF IVK ###

         setAttributeMapping(transformation, 1, conInLrt, gc_oldRecordName & "." & g_anInLrt)
         setAttributeMapping(transformation, 2, conCreateTimestamp, gc_oldRecordName & "." & g_anCreateTimestamp, , , True)
         setAttributeMapping(transformation, 3, conLastUpdateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anLastUpdateTimestamp & ", v_now)", , , True)
 ' ### IF IVK ###
         setAttributeMapping(transformation, 4, conHasBeenSetProductive, gc_oldRecordName & "." & g_anHasBeenSetProductive)
         setAttributeMapping(transformation, 5, conPsOid, "v_psOid")
         nextTrIndex = 6
 
         If hasExpBasedVirtualAttrs And maintainVirtAttrInTriggerOnEntityTabs Then
           For k = 1 To thisEntityTabColumns.numDescriptors
               If thisEntityTabColumns.descriptors(k).columnCategory And eacVirtual Then
                 setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors(k).columnName, "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName)
                 nextTrIndex = nextTrIndex + 1
               End If
           Next k
         End If
 
         If forNl Then
           genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt Or edomListVirtual Or edomVirtualPersisted)
         Else
           genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt Or edomDefaultValue Or edomListVirtual Or edomVirtualPersisted)
         End If
 ' ### ELSE IVK ###
 '
 '       If forNl Then
 '         genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt
 '       Else
 '         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt Or edomDefaultValue
 '       End If
 ' ### ENDIF IVK ###

         Print #fileNoTrigger, addTab(2); ")"
         Print #fileNoTrigger, addTab(2); "WHERE"
         Print #fileNoTrigger, addTab(3); "PUB."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
         genDdlPsDivClause(fileNoTrigger, 3, "PUB", "", gc_newRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
         Print #fileNoTrigger, addTab(2); ";"
       End If

       Print #fileNoTrigger, addTab(1); "ELSE"

       genVerifyActiveLrtDdl(fileNoTrigger, ddlType, qualTabNameLrt, "v_lrtOid", 2, True)
 ' ### IF IVK ###
       genStatusCheckDdl(fileNoTrigger, gc_newRecordName, , 2)
 ' ### ENDIF IVK ###

       If Not isPurelyPrivate Then
         genProcSectionHeader(fileNoTrigger, "check if " & gc_newRecordName & " is an update of a 'public record' (v_pubRecordExists = 1)", 2)
 ' ### IF IVK ###
         If condenseData Then
           Print #fileNoTrigger, addTab(2); "IF EXISTS(SELECT 1 FROM "; qualTabNamePub; " PUB WHERE PUB."; g_anOid; " = "; gc_newRecordName; "."; g_anOid; ") THEN"
           Print #fileNoTrigger, addTab(3); "SET v_pubRecordExists = "; gc_dbTrue; ";"
           Print #fileNoTrigger, addTab(2); "ELSE"
           Print #fileNoTrigger, addTab(3); "SET v_pubRecordExists = "; gc_dbFalse; ";"
           Print #fileNoTrigger, addTab(2); "END IF;"
         Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
           Print #fileNoTrigger, addTab(2); "SET v_pubOwnerId = NULL;"
           Print #fileNoTrigger, addTab(2); "SET v_pubOwnerId ="
           Print #fileNoTrigger, addTab(3); "("
           Print #fileNoTrigger, addTab(4); "SELECT"
           Print #fileNoTrigger, addTab(5); "COALESCE(PUB."; g_anInLrt; ",-1)"
           Print #fileNoTrigger, addTab(4); "FROM"
           Print #fileNoTrigger, addTab(5); qualTabNamePub; " PUB"
           Print #fileNoTrigger, addTab(4); "WHERE"
           Print #fileNoTrigger, addTab(5); "PUB."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
           genDdlPsDivClause(fileNoTrigger, 5, "PUB", "", gc_newRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
           Print #fileNoTrigger, addTab(4); "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)"
           Print #fileNoTrigger, addTab(3); ");"
           Print #fileNoTrigger, addTab(2); "SET v_pubRecordExists = (CASE WHEN v_pubOwnerId IS NULL THEN 0 ELSE 1 END);"
           Print #fileNoTrigger, addTab(2); "SET v_pubOwnerId = (CASE WHEN v_pubOwnerId = -1 THEN NULL ELSE v_pubOwnerId END);"
         End If
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###

       genProcSectionHeader(fileNoTrigger, "check if " & gc_newRecordName & " corresponds to a 'private record' (v_privRecordExists = 1)", 2, True)
       Print #fileNoTrigger, addTab(2); "SET v_privOwnerId = NULL;"
       Print #fileNoTrigger, addTab(2); "SET v_privOwnerId ="
       Print #fileNoTrigger, addTab(3); "("
       Print #fileNoTrigger, addTab(4); "SELECT"
       Print #fileNoTrigger, addTab(5); "COALESCE(PRIV."; g_anInLrt; ",-1)"
       Print #fileNoTrigger, addTab(4); "FROM"
       Print #fileNoTrigger, addTab(5); qualTabNamePriv; " PRIV"
       Print #fileNoTrigger, addTab(4); "WHERE"
       Print #fileNoTrigger, addTab(5); "PRIV."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
       genDdlPsDivClause(fileNoTrigger, 5, "PRIV", "", gc_newRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
       Print #fileNoTrigger, addTab(4); "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)"
       Print #fileNoTrigger, addTab(3); ");"
       Print #fileNoTrigger, addTab(2); "SET v_privRecordExists = (CASE WHEN v_privOwnerId IS NULL THEN 0 ELSE 1 END);"
       Print #fileNoTrigger, addTab(2); "SET v_privOwnerId = (CASE WHEN v_privOwnerId = -1 THEN NULL ELSE v_privOwnerId END);"

       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(2); "IF v_privRecordExists = 1 THEN"
       genProcSectionHeader(fileNoTrigger, "check if the 'private record' is marked 'deleted[" & CStr(lrtStatusDeleted) & "]'", 3, True)
       Print #fileNoTrigger, addTab(3); "SET (v_privRecordCountDeleted) ="
       Print #fileNoTrigger, addTab(4); "("
       Print #fileNoTrigger, addTab(5); "SELECT"
       Print #fileNoTrigger, addTab(6); "COUNT(*)"
       Print #fileNoTrigger, addTab(5); "FROM"
       Print #fileNoTrigger, addTab(6); qualTabNamePriv; " PRIV"
       Print #fileNoTrigger, addTab(5); "WHERE"
       Print #fileNoTrigger, addTab(6); "(PRIV."; g_anOid; " = "; gc_newRecordName; "."; g_anOid; ")"
       Print #fileNoTrigger, addTab(7); "AND"
       Print #fileNoTrigger, addTab(6); "(PRIV."; g_anLrtState; " = "; CStr(lrtStatusDeleted); ")"
       genDdlPsDivClause(fileNoTrigger, 6, "PRIV", "", gc_newRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
       Print #fileNoTrigger, addTab(4); ");"
       Print #fileNoTrigger, addTab(2); "END IF;"
       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(2); "IF v_privRecordCountDeleted > 0 THEN"
       genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 3, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(" & gc_newRecordName & "." & g_anOid & "))")
       Print #fileNoTrigger, addTab(2); "END IF;"

 ' ### IF IVK ###
       If condenseData Then
         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(2); "IF v_pubRecordExists = 1 THEN"
         genSignalDdl("pubUpdateNotAllowed", fileNoTrigger, 3, entityName)
         Print #fileNoTrigger, addTab(2); "END IF;"
       End If

       If Not isPurelyPrivate And Not condenseData Then
 ' ### ELSE IVK ###
 '     If Not isPurelyPrivate Then
 ' ### ENDIF IVK ###
         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(2); "IF v_pubRecordExists = 1 THEN"
         Print #fileNoTrigger, addTab(3); "-- check if this record is locked by some LRT other than this one"
         Print #fileNoTrigger, addTab(3); "IF NOT ((v_pubOwnerId IS NULL) OR (v_pubOwnerId = v_lrtOid)) THEN"

 ' ### IF IVK ###
         If thisOrgIndex <> g_primaryOrgIndex And poolSupportLrt Then
           genProcSectionHeader(fileNoTrigger, "determine whether this LRT is a FACTORYTAKEOVER-LRT", 4, True)
           Print #fileNoTrigger, addTab(4); "SET v_isFtoLrt = COALESCE((SELECT "; g_anIsCentralDataTransfer; " FROM "; qualTabNameLrt; " WHERE "; g_anOid; " = v_lrtOid), 0);"

           genProcSectionHeader(fileNoTrigger, "create a 'business error message' if this LRT is FACTORYTAKEOVER", 4)
           Print #fileNoTrigger, addTab(4); "IF v_isFtoLrt = 1 THEN"

           genProcSectionHeader(fileNoTrigger, "determine entityLabel", 5, True)
           Print #fileNoTrigger, addTab(5); "SET v_entityLabel = RTRIM(LEFT(COALESCE(("
           Print #fileNoTrigger, addTab(6); "SELECT"
           Print #fileNoTrigger, addTab(7); g_anAcmEntityLabel
           Print #fileNoTrigger, addTab(6); "FROM"
           Print #fileNoTrigger, addTab(7); g_qualTabNameAcmEntity; " E"
           Print #fileNoTrigger, addTab(6); "INNER JOIN"
           Print #fileNoTrigger, addTab(7); g_qualTabNameAcmEntityNl; " ENL"
           Print #fileNoTrigger, addTab(6); "ON"
           Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntitySection; " = ENL."; g_anAcmEntitySection
           Print #fileNoTrigger, addTab(8); "AND"
           Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntityName; " = ENL."; g_anAcmEntityName
           Print #fileNoTrigger, addTab(8); "AND"
           Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntityType; " = ENL."; g_anAcmEntityType
           Print #fileNoTrigger, addTab(6); "WHERE"
           Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntityType; " = '"; getAcmEntityTypeKey(acmEntityType); "'"
           Print #fileNoTrigger, addTab(8); "AND"
           Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntityId; " = "; IIf(hasOwnTable, "'" & entityIdStr & "'", gc_oldRecordName & "." & conClassId)
           Print #fileNoTrigger, addTab(6); "ORDER BY"
           Print #fileNoTrigger, addTab(7); "(CASE ENL."; g_anLanguageId; " WHEN "; gc_langIdEnglish; " THEN 0 ELSE ENL."; g_anLanguageId; " END) ASC"
           Print #fileNoTrigger, addTab(6); "FETCH FIRST 1 ROW ONLY), '"; getPrimaryEntityLabelByIndex(acmEntityType, acmEntityIndex); "'), "; _
                                            CStr(33 - IIf(busKeyAttrListNoFks = "", 3, Len(busKeyAttrListNoFks)) - IIf(forNl Or forGen, 3, 0) - IIf(forGen, 1, 0) - IIf(forNl, 1, 0)); ")"; _
                                            ; IIf(forGen Or forNl, " || ' (" & IIf(forGen, "G", "") & IIf(forNl, "N", "") & ")'", ""); ");"

           If busKeyAttrListNoFks <> "" And acmEntityType = eactClass And Not forNl Then
             genProcSectionHeader(fileNoTrigger, "concatenate business key values for error message", 5)
             Print #fileNoTrigger, addTab(5); "SET v_busKeyValues ="
             For i = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
               Print #fileNoTrigger, addTab(6); gc_oldRecordName; "."; busKeyAttrArrayNoFks(i); IIf(i < UBound(busKeyAttrArrayNoFks), " || ',' ||", "")
             Next i
             Print #fileNoTrigger, addTab(5); ";"
 
             genProcSectionHeader(fileNoTrigger, "signal eror message", 5)
             genSignalDdlWithParms("ftoLockDetail", fileNoTrigger, 5, busKeyAttrListNoFks, , , , , , , , , "v_entityLabel", "v_busKeyValues")
           Else
             genProcSectionHeader(fileNoTrigger, "signal eror message", 5)
             genSignalDdlWithParms("ftoLockDetail", fileNoTrigger, 5, g_anOid, , , , , , , , , "v_entityLabel", "RTRIM(CHAR(" & gc_oldRecordName & "." & g_anOid & "))")
           End If

           Print #fileNoTrigger, addTab(4); "ELSE"
           genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 5, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(" & gc_newRecordName & "." & g_anOid & "))")
           Print #fileNoTrigger, addTab(4); "END IF;"
         Else
           genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 4, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(" & gc_newRecordName & "." & g_anOid & "))")
         End If

 ' ### ENDIF IVK ###
         Print #fileNoTrigger, addTab(3); "END IF;"

         genProcSectionHeader(fileNoTrigger, "lock the 'public record' with this LRT-OID", 3)
         Print #fileNoTrigger, addTab(3); "IF (v_pubOwnerId IS NULL) OR (v_pubOwnerId <> v_lrtOid) THEN"
         Print #fileNoTrigger, addTab(4); "UPDATE"
         Print #fileNoTrigger, addTab(5); qualTabNamePub; " PUB"
         Print #fileNoTrigger, addTab(4); "SET"
         Print #fileNoTrigger, addTab(5); "PUB."; g_anInLrt; " = v_lrtOid"
         Print #fileNoTrigger, addTab(4); "WHERE"
         Print #fileNoTrigger, addTab(5); "PUB."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
         genDdlPsDivClause(fileNoTrigger, 5, "PUB", "", gc_newRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
         Print #fileNoTrigger, addTab(4); ";"
         Print #fileNoTrigger, addTab(3); "END IF;"

         If qualTabNamePub <> qualTabNameAggHeadPub And ahClassIndex > 0 Then
           ' lock the 'public aggregate head record' with this LRT-OID
           genAggHeadLockPropDdl(fileNoTrigger, gc_newRecordName, ahClassIndex, qualTabNameAggHeadPub, qualTabNameAggHeadPriv, qualTabNameLrtAffectedEntity, "v_pubOwnerUserId", ddlType, thisOrgIndex, thisPoolIndex, 3, (isPsTagged And (usePsTagInNlTextTables Or Not forNl)), useDivOidWhereClause, useDivRelKey)
         End If

         Print #fileNoTrigger, addTab(2); "END IF;"
       End If

       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(2); "IF v_privRecordExists = "; gc_dbFalse; " THEN"
       Print #fileNoTrigger, addTab(3); "-- private record does not exist; thus consider " & gc_newRecordName; " as new 'private record'"
       Print #fileNoTrigger, addTab(3); "INSERT INTO"
       Print #fileNoTrigger, addTab(4); qualTabNamePriv
       Print #fileNoTrigger, addTab(3); "("

 ' ### IF IVK ###
       If forNl Then
         genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
       Else
         genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
       End If
 ' ### ELSE IVK ###
 '     If forNl Then
 '       genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt
 '     Else
 '       genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt
 '     End If
 ' ### ENDIF IVK ###

       Print #fileNoTrigger, addTab(3); ")"
       Print #fileNoTrigger, addTab(3); "VALUES"
       Print #fileNoTrigger, addTab(3); "("

 ' ### IF IVK ###
       initAttributeTransformation(transformation, 7 + numVirtualAttrs, , , , gc_newRecordName & ".")
 ' ### ELSE IVK ###
 '     initAttributeTransformation transformation, 4, , , , gc_newRecordName & "."
 ' ### ENDIF IVK ###

       setAttributeMapping(transformation, 1, conLrtState, "" & lrtStatusUpdated)
       setAttributeMapping(transformation, 2, conInLrt, "v_lrtOid")
       setAttributeMapping(transformation, 3, conCreateTimestamp, gc_oldRecordName & "." & g_anCreateTimestamp, , , True)
       setAttributeMapping(transformation, 4, conLastUpdateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anLastUpdateTimestamp & ", v_now)", , , True)
 ' ### IF IVK ###
       setAttributeMapping(transformation, 5, conHasBeenSetProductive, gc_oldRecordName & "." & g_anHasBeenSetProductive)
       setAttributeMapping(transformation, 6, conStatusId, CStr(statusWorkInProgress), , , True)
       setAttributeMapping(transformation, 7, conPsOid, "v_psOid")
       nextTrIndex = 8

       If hasExpBasedVirtualAttrs And maintainVirtAttrInTriggerOnEntityTabs Then
         For k = 1 To thisEntityTabColumns.numDescriptors
             If thisEntityTabColumns.descriptors(k).columnCategory And eacVirtual Then
               setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors(k).columnName, "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName)
               nextTrIndex = nextTrIndex + 1
             End If
         Next k
       End If

       If forNl Then
         genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt Or edomDefaultValue Or edomListVirtual Or edomVirtualPersisted)
       Else
         genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt Or edomDefaultValue Or edomListVirtual Or edomVirtualPersisted)
       End If
 ' ### ELSE IVK ###
 '
 '     If forNl Then
 '       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt Or edomDefaultValue
 '     Else
 '       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt Or edomDefaultValue
 '     End If
 ' ### ENDIF IVK ###

       Print #fileNoTrigger, addTab(3); ");"

       Print #fileNoTrigger, addTab(2); "ELSE"
       Print #fileNoTrigger, addTab(3); "-- private record exists - check if it is locked by some LRT other than this one"
       Print #fileNoTrigger, addTab(3); "IF NOT (v_privOwnerId = "; gc_newRecordName; "."; g_anInLrt; ") THEN"
       genSignalDdlWithParmsForCompoundSql("lrtUpdLocked", fileNoTrigger, 4, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(" & gc_newRecordName & "." & g_anOid & "))")
       Print #fileNoTrigger, addTab(3); "END IF;"
       Print #fileNoTrigger,

       Print #fileNoTrigger, addTab(3); "-- now update private record with values in "; gc_newRecordName; ""
       Print #fileNoTrigger, addTab(3); "UPDATE"
       Print #fileNoTrigger, addTab(4); qualTabNamePriv; " PRIV"
       Print #fileNoTrigger, addTab(3); "SET"
       Print #fileNoTrigger, addTab(3); "("

 ' ### IF IVK ###
       If forNl Then
         genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
       Else
         genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
       End If
 ' ### ELSE IVK ###
 '     If forNl Then
 '       genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt
 '     Else
 '       genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt
 '     End If
 ' ### ENDIF IVK ###

       Print #fileNoTrigger, addTab(3); ")"
       Print #fileNoTrigger, addTab(3); "="
       Print #fileNoTrigger, addTab(3); "("

 ' ### IF IVK ###
       initAttributeTransformation(transformation, 7 + numVirtualAttrs, , , , gc_newRecordName & ".")
 ' ### ELSE IVK ###
 '     initAttributeTransformation transformation, 4, , , , gc_newRecordName & "."
 ' ### ENDIF IVK ###

       setAttributeMapping(transformation, 1, conLrtState, "(CASE WHEN " & g_anLrtState & " = " & lrtStatusLocked & " THEN " & lrtStatusUpdated & " ELSE " & g_anLrtState & " END)")
       setAttributeMapping(transformation, 2, conInLrt, "v_lrtOid")
       setAttributeMapping(transformation, 3, conCreateTimestamp, gc_oldRecordName & "." & g_anCreateTimestamp, , , True)
       setAttributeMapping(transformation, 4, conLastUpdateTimestamp, "COALESCE(" & gc_newRecordName & "." & g_anLastUpdateTimestamp & ", v_now)", , , True)
 ' ### IF IVK ###
       setAttributeMapping(transformation, 5, conHasBeenSetProductive, gc_oldRecordName & "." & g_anHasBeenSetProductive)
       setAttributeMapping(transformation, 6, conStatusId, "COALESCE(" & gc_newRecordName & "." & g_anStatus & ", " & statusWorkInProgress & ")", , , True)
       setAttributeMapping(transformation, 7, conPsOid, "v_psOid")
       nextTrIndex = 8

       If hasExpBasedVirtualAttrs And maintainVirtAttrInTriggerOnEntityTabs Then
         For k = 1 To thisEntityTabColumns.numDescriptors
             If thisEntityTabColumns.descriptors(k).columnCategory And eacVirtual Then
               setAttributeMapping(transformation, nextTrIndex, thisEntityTabColumns.descriptors(k).columnName, "v_" & thisEntityTabColumns.descriptors(k).acmAttributeName)
               nextTrIndex = nextTrIndex + 1
             End If
         Next k
       End If

       If forNl Then
         genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt Or edomDefaultValue Or edomListVirtual Or edomVirtualPersisted)
       Else
         genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt Or edomDefaultValue Or edomListVirtual Or edomVirtualPersisted)
       End If
 ' ### ELSE IVK ###
 '
 '     If forNl Then
 '       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt Or edomDefaultValue
 '     Else
 '       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt Or edomDefaultValue
 '     End If
 ' ### ENDIF IVK ###

       Print #fileNoTrigger, addTab(3); ")"
       Print #fileNoTrigger, addTab(3); "WHERE"
       Print #fileNoTrigger, addTab(4); "PRIV."; g_anOid; " = "; gc_newRecordName; "."; g_anOid
       genDdlPsDivClause(fileNoTrigger, 4, "PRIV", "", gc_newRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
       Print #fileNoTrigger, addTab(3); ";"
       Print #fileNoTrigger, addTab(2); "END IF;"

 ' ### IF IVK ###
       genDdlForUpdateAffectedEntities(fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, _
         entityIdStr, ahClassIdStr, "v_lrtOid", 2, , Not condenseData)
 ' ### ELSEIF IVK ###
 '     genDdlForUpdateAffectedEntities fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, _
 '       entityIdStr, ahClassIdStr, "v_lrtOid", 2
 ' ### ENDIF IVK ###

       Print #fileNoTrigger, addTab(1); "END IF;"

       genDdlForUpdateLrtLastOpTs(fileNoTrigger, thisOrgIndex, thisPoolIndex, "v_lrtOid", "v_now", ddlType)

       Print #fileNoTrigger, addTab(0); "END"
       Print #fileNoTrigger, addTab(0); gc_sqlCmdDelim
     End If

     ' ####################################################################################################################
     ' #    DELETE Trigger
     ' ####################################################################################################################

 ' ### IF IVK ###
     qualTriggerName = _
       genQualTriggerNameByEntityIndex( _
         acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , IIf(forNl, "NL", "") & IIf(forMqt, "M", "") & nameSuffix & "LRT_DEL" _
       )
 ' ### ELSEIF IVK ###
 '   qualTriggerName = _
 '     genQualTriggerNameByEntityIndex( _
 '       acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, forMqt, forNl, , nameSuffix & IIf(nameSuffix = "", "", "_") & "DEL" _
 '     )
 ' ### ENDIF IVK ###

     If ddlType = edtPdm And Not poolSupportLrt Then
       printSectionHeader("Delete-Trigger for LRT-emulating view on table """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoTrigger)
       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(0); "CREATE TRIGGER"
       Print #fileNoTrigger, addTab(1); qualTriggerName
       Print #fileNoTrigger, addTab(0); "INSTEAD OF DELETE ON"
       Print #fileNoTrigger, addTab(1); qualViewName
       Print #fileNoTrigger, addTab(0); "REFERENCING"
       Print #fileNoTrigger, addTab(1); "OLD AS "; gc_oldRecordName
       Print #fileNoTrigger, addTab(0); "FOR EACH ROW"
       Print #fileNoTrigger, addTab(0); "BEGIN ATOMIC"

       If Not entityDeletable And generateUpdatableCheckInUpdateTrigger Then
         genSignalDdl("deleteNotAllowed", fileNoTrigger, 1, entityName)
       ElseIf isPurelyPrivate Then
         genSignalDdl("deleteNotAllowed", fileNoTrigger, 1, entityName)
 ' ### IF IVK ###
       ElseIf condenseData Then
         genSignalDdl("deleteNotAllowed", fileNoTrigger, 1, entityName)
 ' ### ENDIF IVK ###
       Else
 ' ### IF IVK ###
         If isPsTagged And (usePsTagInNlTextTables Or Not forNl) Then
           genProcSectionHeader(fileNoTrigger, "declare variables")
           genSigMsgVarDecl(fileNoTrigger)

           ' note: this procedure declares variables 'v_psOidRecord' and 'v_psOidRegVar'
           genPsCheckDdlForInsertDelete(_
             fileNoTrigger, gc_oldRecordName & "." & g_anPsOid, ddlType, thisOrgIndex, ignorePsRegVarOnInsertDelete, _
             psTagOptional, , False, , , "", , qualViewName, gc_oldRecordName & "." & g_anOid)
         End If

 ' ### ENDIF IVK ###
         genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, tvFalse, 1)

         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(1); "DELETE FROM"
         Print #fileNoTrigger, addTab(2); qualTabNamePub
         Print #fileNoTrigger, addTab(1); "WHERE"
         Print #fileNoTrigger, addTab(2); g_anOid; " = "; gc_oldRecordName; "."; g_anOid
         genDdlPsDivClause(fileNoTrigger, 2, "", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
        Print #fileNoTrigger, addTab(1); ";"
       End If
       Print #fileNoTrigger, "END"
       Print #fileNoTrigger, gc_sqlCmdDelim
     ElseIf Not (isPurelyPrivate And forMqt) Then
       printSectionHeader("Delete-Trigger for 'public-private' LRT-view """ & qualViewName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNoTrigger)

       Print #fileNoTrigger,
       Print #fileNoTrigger, addTab(0); "CREATE TRIGGER"
       Print #fileNoTrigger, addTab(1); qualTriggerName
       Print #fileNoTrigger, addTab(0); "INSTEAD OF DELETE ON"
       Print #fileNoTrigger, addTab(1); qualViewName
       Print #fileNoTrigger, addTab(0); "REFERENCING"
       Print #fileNoTrigger, addTab(1); "OLD AS "; gc_oldRecordName
       Print #fileNoTrigger, addTab(0); "FOR EACH ROW"
       Print #fileNoTrigger, addTab(0); "BEGIN ATOMIC"

 ' ### IF IVK ###
       If condenseData Then
         genSignalDdl("deleteNotAllowed", fileNoTrigger, 1, entityName)
       Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNoTrigger, "declare variables")
         genSigMsgVarDecl(fileNoTrigger)
         genVarDecl(fileNoTrigger, "v_lrtOid", g_dbtOid, "0")
         genVarDecl(fileNoTrigger, "v_lrtClosed", g_dbtBoolean, "NULL")
         genVarDecl(fileNoTrigger, "v_now", "TIMESTAMP", "CURRENT TIMESTAMP")
         genVarDecl(fileNoTrigger, "v_privRecordExists", g_dbtBoolean, gc_dbFalse)
         genVarDecl(fileNoTrigger, "v_privRecordCountDeleted", g_dbtBoolean, gc_dbFalse)
         genVarDecl(fileNoTrigger, "v_privOwnerId", g_dbtLrtId, "NULL")

         If Not isPurelyPrivate Then
           genVarDecl(fileNoTrigger, "v_pubRecordExists", g_dbtBoolean, gc_dbFalse)
           genVarDecl(fileNoTrigger, "v_pubOwnerId", g_dbtLrtId, "NULL")

           If (qualTabNamePub <> qualTabNameAggHeadPub) And (ahClassIndex > 0) Then
             genVarDecl(fileNoTrigger, "v_pubOwnerUserId", g_dbtUserId, "NULL")
             genVarDecl(fileNoTrigger, "v_inLrt", g_dbtOid, "0")
           End If

           genVarDecl(fileNoTrigger, "v_oidCount", "INTEGER", "0")

           If thisOrgIndex <> g_primaryOrgIndex And poolSupportLrt Then
             genVarDecl(fileNoTrigger, "v_isFtoLrt", g_dbtBoolean, gc_dbFalse)
             genVarDecl(fileNoTrigger, "v_entityLabel", "VARCHAR(90)", "'" & getPrimaryEntityLabelByIndex(acmEntityType, acmEntityIndex) & "'")
             If busKeyAttrListNoFks <> "" And acmEntityType = eactClass And Not forNl Then
               genVarDecl(fileNoTrigger, "v_busKeyValues", "VARCHAR(200)", "NULL")
             End If
           End If
         End If

         genVarDecl(fileNoTrigger, "v_lrtExecutedOperation", "INTEGER", CStr(lrtStatusDeleted))
         genVarDecl(fileNoTrigger, "v_lrtEntityIdCount", "INTEGER", "0")

 ' ### IF IVK ###
         If isPsTagged And (usePsTagInNlTextTables Or Not forNl) Then
           ' note: this procedure declares variables 'v_psOidRecord' and 'v_psOidRegVar'
           genPsCheckDdlForInsertDelete(_
             fileNoTrigger, gc_oldRecordName & "." & g_anPsOid, ddlType, thisOrgIndex, ignorePsRegVarOnInsertDelete, _
             psTagOptional, , False, , , IIf(qualTabNamePub = qualTabNameAggHeadPub, "", "v_psOid"), , qualViewName, gc_oldRecordName & "." & g_anOid)
         ElseIf qualTabNamePub <> qualTabNameAggHeadPub Then
           genPsCheckDdlForNonPsTaggedInLrt(fileNoTrigger, ddlType, thisOrgIndex, , False)
         End If

 ' ### ENDIF IVK ###
         genDb2RegVarCheckDdl(fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1)

         Print #fileNoTrigger, addTab(1); "-- determine LRT OID"
         Print #fileNoTrigger, addTab(1); "SET v_lrtOid = (CASE "; gc_db2RegVarLrtOid; " WHEN '' THEN CAST(NULL AS "; g_dbtOid; ") ELSE "; _
                                          g_activeLrtOidDdl; " END);"

         genProcSectionHeader(fileNoTrigger, "if no LRT-ID is given, delete in public table")
         Print #fileNoTrigger, addTab(1); "IF v_lrtOid IS NULL THEN"

         If isPurelyPrivate Then
           genProcSectionHeader(fileNoTrigger, "not supported - table is purely private", 2, True)
         Else
           genProcSectionHeader(fileNoTrigger, "verify that record is not locked", 2, True)
           Print #fileNoTrigger, addTab(2); "SET v_oidCount ="
           Print #fileNoTrigger, addTab(3); "("
           Print #fileNoTrigger, addTab(4); "SELECT"
           Print #fileNoTrigger, addTab(5); "COUNT(*)"
           Print #fileNoTrigger, addTab(4); "FROM"
           Print #fileNoTrigger, addTab(5); qualTabNamePub; " PUB"
           Print #fileNoTrigger, addTab(4); "WHERE"
           Print #fileNoTrigger, addTab(5); "PUB."; g_anInLrt; " IS NOT NULL"
           Print #fileNoTrigger, addTab(6); "AND"
           Print #fileNoTrigger, addTab(5); "PUB."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
           genDdlPsDivClause(fileNoTrigger, 5, "PUB", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
           Print #fileNoTrigger, addTab(3); ");"
           Print #fileNoTrigger,
           Print #fileNoTrigger, addTab(2); "IF v_oidCount > 0 THEN"
           genSignalDdlWithParmsForCompoundSql("lrtDelLocked", fileNoTrigger, 3, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(" & gc_oldRecordName & "." & g_anOid & "))")
           Print #fileNoTrigger, addTab(2); "END IF;"

           Print #fileNoTrigger,
           Print #fileNoTrigger, addTab(2); "DELETE FROM"
           Print #fileNoTrigger, addTab(3); qualTabNamePub
           Print #fileNoTrigger, addTab(2); "WHERE"
           Print #fileNoTrigger, addTab(3); g_anOid; " = "; gc_oldRecordName; "."; g_anOid
           genDdlPsDivClause(fileNoTrigger, 3, "", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
           Print #fileNoTrigger, addTab(2); ";"
         End If

         Print #fileNoTrigger, addTab(1); "ELSE"

         genVerifyActiveLrtDdl(fileNoTrigger, ddlType, qualTabNameLrt, "v_lrtOid", 2, True)

         If Not isPurelyPrivate Then
           genProcSectionHeader(fileNoTrigger, "check if " & gc_oldRecordName & " refers to a 'public record' (v_pubRecordExists = 1)", 2)
           Print #fileNoTrigger, addTab(2); "SET v_pubOwnerId = NULL;"
           Print #fileNoTrigger, addTab(2); "SET v_pubOwnerId ="
           Print #fileNoTrigger, addTab(3); "("
           Print #fileNoTrigger, addTab(4); "SELECT"
           Print #fileNoTrigger, addTab(5); "COALESCE(PUB."; g_anInLrt; ",-1)"
           Print #fileNoTrigger, addTab(4); "FROM"
           Print #fileNoTrigger, addTab(5); qualTabNamePub; " PUB"
           Print #fileNoTrigger, addTab(4); "WHERE"
           Print #fileNoTrigger, addTab(5); "PUB."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
           genDdlPsDivClause(fileNoTrigger, 5, "PUB", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
           Print #fileNoTrigger, addTab(4); "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)"
           Print #fileNoTrigger, addTab(3); ");"
           Print #fileNoTrigger, addTab(2); "SET v_pubRecordExists = (CASE WHEN v_pubOwnerId IS NULL THEN 0 ELSE 1 END);"
           Print #fileNoTrigger, addTab(2); "SET v_pubOwnerId = (CASE WHEN v_pubOwnerId = -1 THEN NULL ELSE v_pubOwnerId END);"
         End If

         genProcSectionHeader(fileNoTrigger, "check if " & gc_oldRecordName & " corresponds to a 'private record' (v_privRecordExists = 1)", 2)
         Print #fileNoTrigger, addTab(2); "SET v_privOwnerId = NULL;"
         Print #fileNoTrigger, addTab(2); "SET v_privOwnerId ="
         Print #fileNoTrigger, addTab(3); "("
         Print #fileNoTrigger, addTab(4); "SELECT"
         Print #fileNoTrigger, addTab(5); "COALESCE(PRIV."; g_anInLrt; ",-1)"
         Print #fileNoTrigger, addTab(4); "FROM"
         Print #fileNoTrigger, addTab(5); qualTabNamePriv; " PRIV"
         Print #fileNoTrigger, addTab(4); "WHERE"
         Print #fileNoTrigger, addTab(5); "PRIV."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
         genDdlPsDivClause(fileNoTrigger, 5, "PRIV", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
         Print #fileNoTrigger, addTab(4); "FETCH FIRST 1 ROW ONLY -- there is only 1 row (maximum)"
         Print #fileNoTrigger, addTab(3); ");"
         Print #fileNoTrigger, addTab(2); "SET v_privRecordExists = (CASE WHEN v_privOwnerId IS NULL THEN 0 ELSE 1 END);"
         Print #fileNoTrigger, addTab(2); "SET v_privOwnerId = (CASE WHEN v_privOwnerId = -1 THEN NULL ELSE v_privOwnerId END);"

         Print #fileNoTrigger, addTab(2); "IF v_privRecordExists = 1 THEN"
         Print #fileNoTrigger, addTab(3); "-- check if the 'private record' is marked 'deleted["; CStr(lrtStatusDeleted); "]'"
         Print #fileNoTrigger, addTab(3); "SET (v_privRecordCountDeleted) ="
         Print #fileNoTrigger, addTab(4); "("
         Print #fileNoTrigger, addTab(5); "SELECT"
         Print #fileNoTrigger, addTab(6); "COUNT(*)"
         Print #fileNoTrigger, addTab(5); "FROM"
         Print #fileNoTrigger, addTab(6); qualTabNamePriv; " PRIV"
         Print #fileNoTrigger, addTab(5); "WHERE"
         Print #fileNoTrigger, addTab(6); "(PRIV."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid; ")"
         Print #fileNoTrigger, addTab(7); "AND"
         Print #fileNoTrigger, addTab(6); "(PRIV."; g_anLrtState; " = "; CStr(lrtStatusDeleted); ")"
         genDdlPsDivClause(fileNoTrigger, 6, "PRIV", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
         Print #fileNoTrigger, addTab(4); ");"
         Print #fileNoTrigger, addTab(2); "END IF;"
         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(2); "IF v_privRecordCountDeleted > 0 THEN"
         Print #fileNoTrigger, addTab(3); "-- should we allow to delete an already deleted record?"
         genSignalDdlWithParmsForCompoundSql("lrtDelAlreadyDel", fileNoTrigger, 3, , , , , , , , , , "RTRIM(CHAR(" & gc_oldRecordName & "." & g_anOid & "))")
         Print #fileNoTrigger, addTab(2); "END IF;"

         If Not isPurelyPrivate Then
           Print #fileNoTrigger,
           Print #fileNoTrigger, addTab(2); "IF v_pubRecordExists = 1 THEN"
           genProcSectionHeader(fileNoTrigger, "check if this record is locked by some LRT other than this one", 3, True)
           Print #fileNoTrigger, addTab(3); "IF NOT ((v_pubOwnerId IS NULL) OR (v_pubOwnerId = v_lrtOid)) THEN"
 ' ### IF IVK ###
           If thisOrgIndex <> g_primaryOrgIndex And poolSupportLrt Then
             genProcSectionHeader(fileNoTrigger, "determine whether this LRT is a FACTORYTAKEOVER-LRT", 4, True)
             Print #fileNoTrigger, addTab(4); "SET v_isFtoLrt = COALESCE((SELECT "; g_anIsCentralDataTransfer; " FROM "; qualTabNameLrt; " WHERE "; g_anOid; " = v_lrtOid), 0);"

             genProcSectionHeader(fileNoTrigger, "create a 'business error message' if this LRT is FACTORYTAKEOVER", 4)
             Print #fileNoTrigger, addTab(4); "IF v_isFtoLrt = 1 THEN"

             genProcSectionHeader(fileNoTrigger, "determine entityLabel", 5, True)
             Print #fileNoTrigger, addTab(5); "SET v_entityLabel = RTRIM(LEFT(COALESCE(("
             Print #fileNoTrigger, addTab(6); "SELECT"
             Print #fileNoTrigger, addTab(7); g_anAcmEntityLabel
             Print #fileNoTrigger, addTab(6); "FROM"
             Print #fileNoTrigger, addTab(7); g_qualTabNameAcmEntity; " E"
             Print #fileNoTrigger, addTab(6); "INNER JOIN"
             Print #fileNoTrigger, addTab(7); g_qualTabNameAcmEntityNl; " ENL"
             Print #fileNoTrigger, addTab(6); "ON"
             Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntitySection; " = ENL."; g_anAcmEntitySection
             Print #fileNoTrigger, addTab(8); "AND"
             Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntityName; " = ENL."; g_anAcmEntityName
             Print #fileNoTrigger, addTab(8); "AND"
             Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntityType; " = ENL."; g_anAcmEntityType
             Print #fileNoTrigger, addTab(6); "WHERE"
             Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntityType; " = '"; getAcmEntityTypeKey(acmEntityType); "'"
             Print #fileNoTrigger, addTab(8); "AND"
             Print #fileNoTrigger, addTab(7); "E."; g_anAcmEntityId; " = "; IIf(hasOwnTable, "'" & entityIdStr & "'", gc_oldRecordName & "." & conClassId)
             Print #fileNoTrigger, addTab(6); "ORDER BY"
             Print #fileNoTrigger, addTab(7); "(CASE ENL."; g_anLanguageId; " WHEN "; CStr(gc_langIdEnglish); " THEN 0 ELSE ENL."; g_anLanguageId; " END) ASC"
             Print #fileNoTrigger, addTab(6); "FETCH FIRST 1 ROW ONLY), '"; getPrimaryEntityLabelByIndex(acmEntityType, acmEntityIndex); "'), "; _
                                              CStr(33 - IIf(busKeyAttrListNoFks = "", 3, Len(busKeyAttrListNoFks)) - IIf(forNl Or forGen, 3, 0) - IIf(forGen, 1, 0) - IIf(forNl, 1, 0)); ")"; _
                                              ; IIf(forGen Or forNl, " || ' (" & IIf(forGen, "G", "") & IIf(forNl, "N", "") & ")'", ""); ");"

             If busKeyAttrListNoFks <> "" And acmEntityType = eactClass And Not forNl Then
               genProcSectionHeader(fileNoTrigger, "concatenate business key values for error message", 5)
               Print #fileNoTrigger, addTab(5); "SET v_busKeyValues ="
               For i = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
                 Print #fileNoTrigger, addTab(6); gc_oldRecordName; "."; busKeyAttrArrayNoFks(i); IIf(i < UBound(busKeyAttrArrayNoFks), " || ',' ||", "")
               Next i
               Print #fileNoTrigger, addTab(5); ";"
 
               genProcSectionHeader(fileNoTrigger, "signal eror message", 5)
               genSignalDdlWithParms("ftoLockDetail", fileNoTrigger, 5, busKeyAttrListNoFks, , , , , , , , , "v_entityLabel", "v_busKeyValues")
             Else
               genProcSectionHeader(fileNoTrigger, "signal eror message", 5)
               genSignalDdlWithParms("ftoLockDetail", fileNoTrigger, 5, g_anOid, , , , , , , , , "v_entityLabel", "RTRIM(CHAR(" & gc_oldRecordName & "." & g_anOid & "))")
             End If

             Print #fileNoTrigger, addTab(4); "ELSE"
             genSignalDdlWithParmsForCompoundSql("lrtDelNotOwner", fileNoTrigger, 5, , , , , , , , , , "RTRIM(CHAR(" & gc_oldRecordName & "." & g_anOid & "))")
             Print #fileNoTrigger, addTab(4); "END IF;"
           Else
             genSignalDdlWithParmsForCompoundSql("lrtDelNotOwner", fileNoTrigger, 4, , , , , , , , , , "RTRIM(CHAR(" & gc_oldRecordName & "." & g_anOid & "))")
           End If
 ' ### ELSE IVK ###
 '         genSignalDdlWithParmsForCompoundSql "lrtDelNotOwner", fileNoTrigger, 4, , , , , , , , , , "RTRIM(CHAR(" & gc_oldRecordName & "." & g_anOid & "))"
 ' ### ENDIF IVK ###
           Print #fileNoTrigger, addTab(3); "END IF;"

           genProcSectionHeader(fileNoTrigger, "lock the 'public record' with this LRT-OID", 3)
           Print #fileNoTrigger, addTab(3); "IF (v_pubOwnerId IS NULL) OR (v_pubOwnerId <> v_lrtOid) THEN"
           Print #fileNoTrigger, addTab(4); "UPDATE"
           Print #fileNoTrigger, addTab(5); qualTabNamePub; " PUB"
           Print #fileNoTrigger, addTab(4); "SET"
           Print #fileNoTrigger, addTab(5); "PUB."; g_anInLrt; " = v_lrtOid"
           Print #fileNoTrigger, addTab(4); "WHERE"
           Print #fileNoTrigger, addTab(5); "PUB."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
           genDdlPsDivClause(fileNoTrigger, 5, "PUB", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
           Print #fileNoTrigger, addTab(4); ";"
           Print #fileNoTrigger, addTab(3); "END IF;"

           If qualTabNamePub <> qualTabNameAggHeadPub And ahClassIndex > 0 Then
             ' lock the 'public aggregate head record' with this LRT-OID
             genAggHeadLockPropDdl(fileNoTrigger, gc_oldRecordName, ahClassIndex, qualTabNameAggHeadPub, qualTabNameAggHeadPriv, qualTabNameLrtAffectedEntity, "v_pubOwnerUserId", ddlType, thisOrgIndex, thisPoolIndex, 3, (isPsTagged And (usePsTagInNlTextTables Or Not forNl)), useDivOidWhereClause, useDivRelKey)
           End If

           Print #fileNoTrigger, addTab(2); "END IF;"
         End If

         Print #fileNoTrigger,
         Print #fileNoTrigger, addTab(2); "IF v_privRecordExists = "; gc_dbFalse; " THEN"
         genProcSectionHeader(fileNoTrigger, "private record does not exist; thus copy " & gc_oldRecordName & " as new 'private record' and mark it as 'deleted[" & CStr(lrtStatusDeleted) & "]'", 3, True)
         Print #fileNoTrigger, addTab(3); "INSERT INTO"
         Print #fileNoTrigger, addTab(4); qualTabNamePriv
         Print #fileNoTrigger, addTab(3); "("

 ' ### IF IVK ###
         If forNl Then
           genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
         Else
           genAttrListForEntity(acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
         End If
 ' ### ELSE IVK ###
 '       If forNl Then
 '         genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNoTrigger, , , ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, edomListLrt
 '       Else
 '         genAttrListForEntity acmEntityIndex, acmEntityType, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, True, forGen, edomListLrt
 '       End If
 ' ### ENDIF IVK ###

         Print #fileNoTrigger, addTab(3); ")"
         Print #fileNoTrigger, addTab(3); "VALUES"
         Print #fileNoTrigger, addTab(3); "("

 ' ### IF IVK ###
         initAttributeTransformation(transformation, 4, , , , gc_oldRecordName & ".")
 ' ### ELSE IVK ###
 '       initAttributeTransformation transformation, 3, , , , gc_oldRecordName & "."
 ' ### ENDIF IVK ###

         setAttributeMapping(transformation, 1, conLrtState, CStr(lrtStatusDeleted))
         setAttributeMapping(transformation, 2, conInLrt, "v_lrtOid")
         setAttributeMapping(transformation, 3, conLastUpdateTimestamp, "v_now", , , True)
 ' ### IF IVK ###
         setAttributeMapping(transformation, 4, conStatusId, CStr(statusWorkInProgress), , , True)

         If forNl Then
           genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt Or edomListVirtual Or edomVirtualPersisted)
         Else
           genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt Or edomListVirtual Or edomVirtualPersisted)
         End If
 ' ### ELSE IVK ###
 '
 '       If forNl Then
 '         genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, True, , edomListLrt
 '       Else
 '         genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNoTrigger, ddlType, thisOrgIndex, thisPoolIndex, 4, , True, forGen, edomListLrt
 '       End If
 ' ### ENDIF IVK ###

         Print #fileNoTrigger, addTab(3); ");"
         Print #fileNoTrigger, addTab(2); "ELSE"
         Print #fileNoTrigger, addTab(3); "-- private record exists - check if it is locked by some LRT other than this one"
         Print #fileNoTrigger, addTab(3); "IF v_privOwnerId <> v_lrtOid THEN"
         genSignalDdlWithParmsForCompoundSql("lrtDelNotOwner", fileNoTrigger, 4, , , , , , , , , , "RTRIM(CHAR(" & gc_oldRecordName & "." & g_anOid & "))")
         Print #fileNoTrigger, addTab(3); "END IF;"

         If isPurelyPrivate Then
           genProcSectionHeader(fileNoTrigger, "delete the private record", 3)
           Print #fileNoTrigger, addTab(3); "DELETE FROM"
           Print #fileNoTrigger, addTab(4); qualTabNamePriv; " PRIV"
           Print #fileNoTrigger, addTab(3); "WHERE"
           Print #fileNoTrigger, addTab(4); "PRIV."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
           genDdlPsDivClause(fileNoTrigger, 4, "PRIV", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
           Print #fileNoTrigger, addTab(3); ";"
         Else
           genProcSectionHeader(fileNoTrigger, "mark 'private record' as 'deleted[" & CStr(lrtStatusDeleted) & "]'", 3)
           Print #fileNoTrigger, addTab(3); "UPDATE"
           Print #fileNoTrigger, addTab(4); qualTabNamePriv; " PRIV"
           Print #fileNoTrigger, addTab(3); "SET"

           If Not forNl And (logLastChange Or acmEntityType = eactRelationship) Then
             Print #fileNoTrigger, addTab(4); g_anLastUpdateTimestamp; " = v_now,"
           End If
           Print #fileNoTrigger, addTab(4); g_anLrtState; " = "; CStr(lrtStatusDeleted)

           Print #fileNoTrigger, addTab(3); "WHERE"
           Print #fileNoTrigger, addTab(4); "PRIV."; g_anOid; " = "; gc_oldRecordName; "."; g_anOid
           genDdlPsDivClause(fileNoTrigger, 4, "PRIV", gc_oldRecordName, gc_oldRecordName, isPsTagged, usePsTagInNlTextTables, forNl, useDivOidWhereClause, useDivRelKey)
           Print #fileNoTrigger, addTab(3); ";"
         End If

         Print #fileNoTrigger, addTab(2); "END IF;"

         genDdlForUpdateAffectedEntities(_
           fileNoTrigger, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, _
           entityIdStr, ahClassIdStr, "v_lrtOid", 2)

         Print #fileNoTrigger, addTab(1); "END IF;"

         genDdlForUpdateLrtLastOpTs(fileNoTrigger, thisOrgIndex, thisPoolIndex, "v_lrtOid", "v_now", ddlType)
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###

       Print #fileNoTrigger, addTab(0); "END"
       Print #fileNoTrigger, addTab(0); gc_sqlCmdDelim
     End If
   Next l
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Private Sub genLrtSupportSpsForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoClView As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False _
 )
   Dim sectionName As String
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim ahClassIdStr As String
   Dim isUserTransactional As Boolean
   Dim isPsTagged As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim entityIdStrList As String
   Dim dbAcmEntityType As String
   Dim attrRefs As AttrDescriptorRefs
   Dim attrRefsInclSubClasses As AttrDescriptorRefs
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
   Dim hasNoIdentity As Boolean
   Dim hasNlAttributes As Boolean
   Dim hasNlTable As Boolean
   Dim attrMapping() As AttributeMappingForCl
   Dim relLeftClassIdStr As String
   Dim relLeftFk As String
   Dim relRightClassIdStr As String
   Dim relRightFk As String
   Dim ignoreForChangelog As Boolean
   Dim hasPriceAssignmentSubClass As Boolean
   Dim hasPriceAssignmentAggHead As Boolean
   Dim isSubjectToPreisDurchschuss As Boolean
   Dim priceAssignmentSubClassIdList As String
   Dim aggHeadClassIndex As Integer
   Dim aggHeadShortClassName As String
   Dim isAggregateHead As Boolean
   Dim busKeyAttrList As String
   Dim busKeyAttrListNoFks As String
   Dim busKeyAttrArray() As String
   Dim busKeyAttrArrayNoFks() As String
   Dim hasGroupIdAttrs As Boolean
   Dim groupIdAttrIndexes() As Integer
   Dim isGenericCode As Boolean
   Dim isEndSlot As Boolean
   Dim isTypeSpec As Boolean
   Dim condenseData As Boolean
   Dim useLrtCommitPreprocess As Boolean
   Dim hasRelBasedVirtualAttrInGenInclSubClasses As Boolean
   Dim hasRelBasedVirtualAttrInNonGenInclSubClasses As Boolean

   On Error GoTo ErrorExit

   If ddlType = edtPdm Then
     If thisPoolIndex < 1 Then
       Exit Sub
     ElseIf Not g_pools.descriptors(thisPoolIndex).supportLrt Then
       Exit Sub
     End If
   End If

   Dim transformation As AttributeListTransformation
   transformation = nullAttributeTransformation

   hasPriceAssignmentSubClass = False
   hasPriceAssignmentAggHead = False
   priceAssignmentSubClassIdList = ""

   busKeyAttrList = ""
   busKeyAttrListNoFks = ""
   useLrtCommitPreprocess = False
   hasRelBasedVirtualAttrInGenInclSubClasses = False
   hasRelBasedVirtualAttrInNonGenInclSubClasses = False

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
   Dim ukAttrDecls As String
   Dim pkAttrList As String
   Dim leftFkAttrs As String
   Dim rightFkAttrs As String
   Dim isPrimaryOrg As Boolean

   isPrimaryOrg = (thisOrgIndex = g_primaryOrgIndex)

   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       entityIdStrList = getSubClassIdStrListByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex)
       ahClassIdStr = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       hasNlAttributes = IIf(forGen, g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses)
       attrMapping = g_classes.descriptors(acmEntityIndex).clMapAttrsInclSubclasses
       ignoreForChangelog = g_classes.descriptors(acmEntityIndex).ignoreForChangelog
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       isAggregateHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).classIndex)
       If g_classes.descriptors(acmEntityIndex).hasBusinessKey Then
         busKeyAttrList = getPkAttrListByClassIndex(acmEntityIndex, ddlType)
         busKeyAttrListNoFks = getPkAttrListByClassIndex(acmEntityIndex, ddlType, , , , True)

         genAttrList(busKeyAttrArrayNoFks, busKeyAttrListNoFks)
       End If
       hasGroupIdAttrs = Not forNl And Not forGen And g_classes.descriptors(acmEntityIndex).hasGroupIdAttrInNonGenInclSubClasses
       If hasGroupIdAttrs Then
         groupIdAttrIndexes = g_classes.descriptors(acmEntityIndex).groupIdAttrIndexesInclSubclasses
       End If
       hasRelBasedVirtualAttrInGenInclSubClasses = g_classes.descriptors(acmEntityIndex).hasRelBasedVirtualAttrInGenInclSubClasses
       hasRelBasedVirtualAttrInNonGenInclSubClasses = g_classes.descriptors(acmEntityIndex).hasRelBasedVirtualAttrInNonGenInclSubClasses
       useLrtCommitPreprocess = g_classes.descriptors(acmEntityIndex).useLrtCommitPreprocess And Not forGen And Not forNl
       isGenericCode = g_classes.descriptors(acmEntityIndex).classIndex = g_classIndexGenericCode And Not forGen And Not forNl
       isEndSlot = g_classes.descriptors(acmEntityIndex).classIndex = g_classIndexEndSlot
       isTypeSpec = g_classes.descriptors(acmEntityIndex).classIndex = g_classIndexTypeSpec
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData

       If forNl Then
         entityName = genNlObjName(g_classes.descriptors(acmEntityIndex).className, , forGen)
         entityShortName = genNlObjShortName(g_classes.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Class (NL-Text)"
         hasOwnTable = True
         isPsTagged = usePsTagInNlTextTables And g_classes.descriptors(acmEntityIndex).isPsTagged
         isAbstract = False
         attrRefs = g_classes.descriptors(acmEntityIndex).nlAttrRefsInclSubclasses
         attrRefsInclSubClasses = g_classes.descriptors(acmEntityIndex).nlAttrRefsInclSubclasses
         relRefs.numRefs = 0
         isGenForming = False
         hasNoIdentity = False
       Else
         entityName = g_classes.descriptors(acmEntityIndex).className
         entityShortName = g_classes.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Class"
         hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
         isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
         isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
         attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
         attrRefsInclSubClasses = g_classes.descriptors(acmEntityIndex).attrRefsInclSubClassesWithRepeat
         relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
         isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
         hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
         hasPriceAssignmentSubClass = g_classes.descriptors(acmEntityIndex).hasPriceAssignmentSubClass
         hasPriceAssignmentAggHead = g_classes.descriptors(acmEntityIndex).hasPriceAssignmentAggHead
         isSubjectToPreisDurchschuss = g_classes.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss

         If hasPriceAssignmentSubClass Then
           Dim i As Integer
           For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive)
               If Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isPriceAssignment Then
                 priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).classIdStr & "'"
               End If
           Next i
         ElseIf hasPriceAssignmentAggHead Then
             For i = 1 To UBound(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive)
                 If Not g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isPriceAssignment Then
                   priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).classIdStr & "'"
                 End If
             Next i
         End If
       End If
   ElseIf acmEntityType = eactRelationship Then
       If forNl Then
         entityName = genNlObjName(g_relationships.descriptors(acmEntityIndex).relName, , forGen)
         entityShortName = genNlObjShortName(g_relationships.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Relationship (NL-Text)"
         isPsTagged = usePsTagInNlTextTables And g_relationships.descriptors(acmEntityIndex).isPsTagged
         attrRefs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
         attrRefsInclSubClasses = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
       Else
         entityName = g_relationships.descriptors(acmEntityIndex).relName
         entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Relationship"
         isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
         attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
         attrRefsInclSubClasses = g_relationships.descriptors(acmEntityIndex).attrRefs
       End If
 
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       entityIdStrList = "'" & g_relationships.descriptors(acmEntityIndex).relIdStr & "'"
       ahClassIdStr = g_relationships.descriptors(acmEntityIndex).aggHeadClassIdStr
       dbAcmEntityType = "R"
       relRefs.numRefs = 0
       isGenForming = False
       hasNoIdentity = False
       hasNlAttributes = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0
       ignoreForChangelog = g_relationships.descriptors(acmEntityIndex).ignoreForChangelog
       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       isAggregateHead = False
       isGenericCode = False
       hasPriceAssignmentAggHead = g_relationships.descriptors(acmEntityIndex).hasPriceAssignmentAggHead
       isSubjectToPreisDurchschuss = g_relationships.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss
       If hasPriceAssignmentAggHead Then
           For i = 1 To UBound(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive)
               If Not g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isPriceAssignment Then
                 priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).classIdStr & "'"
               End If
           Next i
       End If

       hasGroupIdAttrs = False
       condenseData = False

       genTransformedAttrDeclsForRelationshipWithColReUse_Int(acmEntityIndex, transformation, tabColumns, _
           ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, , False, False, edomNone)
       busKeyAttrList = leftFkAttrs & "," & rightFkAttrs

       Dim reuseRelIndex As Integer
       reuseRelIndex = IIf(reuseRelationships And g_relationships.descriptors(acmEntityIndex).reusedRelIndex > 0, g_relationships.descriptors(acmEntityIndex).reusedRelIndex, acmEntityIndex)
           relLeftClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).classIdStr
           relLeftFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).shortName)
           relRightClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).classIdStr
           relRightFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).shortName)
   Else
     Exit Sub
   End If

   hasNlTable = hasNlAttributes Or (isAggregateHead And Not forGen And Not forNl And Not condenseData)

   If Not generateLrt Or Not isUserTransactional Then
     Exit Sub
   End If
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' LRT is only supported at 'pool-level'
     Exit Sub
   End If
 
   If aggHeadClassIndex > 0 Then
     aggHeadShortClassName = g_classes.descriptors(aggHeadClassIndex).shortName
   End If
 
   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNamePub As String
   Dim qualTabNamePriv As String
   If acmEntityType = eactClass Then
       qualTabNamePub = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl)
       qualTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl)
   Else
       qualTabNamePub = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, , , forNl)
       qualTabNamePriv = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , forNl)
   End If
 
   Dim qualAggHeadTabName As String
   Dim qualAggHeadNlTabNamePriv As String
   Dim aggHeadFkAttrName As String
   qualAggHeadTabName = ""
   qualAggHeadNlTabNamePriv = ""
   If aggHeadClassIndex > 0 Then
     qualAggHeadTabName = genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, thisPoolIndex)

     qualAggHeadNlTabNamePriv = _
       genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, thisPoolIndex, , True, , True)

       aggHeadFkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(aggHeadClassIndex).shortName)
   End If

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   If generateLrtSps Then
     If Not forNl Then
       If useLrtCommitPreprocess And Not generateFwkTest Then
         ' ####################################################################################################################
         ' #    SP for prepocessing LRTCOMMIT
         ' ####################################################################################################################

         Dim qualProcNameLrtCommitPreProc As String
         qualProcNameLrtCommitPreProc = _
           genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, spnLrtCommitPreProc)
 
         printSectionHeader("SP for preprocessing LRT for LRT-COMMIT on """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo)
 
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE PROCEDURE"
         Print #fileNo, addTab(1); qualProcNameLrtCommitPreProc
         Print #fileNo, addTab(0); "("
         genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to preprocess")
         genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "CD User Id of the mdsUser")
         genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the LRT's product structure")
         genProcParm(fileNo, "IN", "opId_in", g_dbtEnumId, True, "identifies the operation (insert, update, delete) to create the Log for")
         genProcParm(fileNo, "IN", "commitTs_in", "TIMESTAMP", True, "marks the commit timestamp of the LRT")
         genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by this commit")
         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); "RESULT SETS 0"
         Print #fileNo, addTab(0); "LANGUAGE SQL"
         Print #fileNo, addTab(0); "BEGIN"
 
         If hasRelBasedVirtualAttrInNonGenInclSubClasses Or hasRelBasedVirtualAttrInGenInclSubClasses Then
           genSpLogDecl(fileNo, -1, True)
 
           genSpLogProcEnter(fileNo, qualProcNameLrtCommitPreProc, ddlType, , "lrtOid_in", "'cdUserId_in", "psOid_in", "opId_in", "#commitTs_in", "rowCount_out")
 
           Print #fileNo, addTab(1); "IF opId_in = " & CStr(lrtStatusCreated) & " THEN"

           Dim colonMissing As Boolean
           Dim relIndex As Integer
           Dim relNavDirection As RelNavigationDirection
           Dim sourceClassIndex As Integer
           Dim sourceOrParClassIndex As Integer
           Dim targetOrParClassIndex As Integer
           Dim virtAttrlist As String
           Dim forTvColumns As Boolean
           Dim updateFromPriv As Boolean
           Dim offset As Integer
           Dim j As Integer
           For j = IIf(hasRelBasedVirtualAttrInNonGenInclSubClasses, 1, 2) To IIf(hasRelBasedVirtualAttrInGenInclSubClasses, 2, 1)
             forTvColumns = (j = 2)
             genProcSectionHeader(fileNo, "instantiate virtual attributes" & IIf(forTvColumns, " (GEN)", ""), 2, Not forTvColumns)

             virtAttrlist = ""
             For i = 1 To attrRefsInclSubClasses.numDescriptors
               If attrRefsInclSubClasses.descriptors(i).refType = eadrtAttribute And attrRefsInclSubClasses.descriptors(i).refIndex > 0 Then
                   If (g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isTimeVarying = forTvColumns) And g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isVirtual Then
                     virtAttrlist = virtAttrlist & ", " & g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).dbColName(ddlType)

                     ' todo: this only works as long as all virtual columns in a table refer to the same reference-table
                     relIndex = g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).virtuallyMapsTo.relIndex
                     relNavDirection = g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).virtuallyMapsTo.navDirection
                     sourceClassIndex = g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).acmEntityIndex
                     sourceOrParClassIndex = g_classes.descriptors(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).acmEntityIndex).orMappingSuperClassIndex
                     targetOrParClassIndex = g_classes.descriptors(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).virtuallyMapsTo.targetClassIndex).orMappingSuperClassIndex
                   End If
               End If
             Next i

             Dim fkAttrName As String
               fkAttrName = IIf(relNavDirection = etLeft, g_relationships.descriptors(relIndex).rightFkColName(ddlType), g_relationships.descriptors(relIndex).leftFkColName(ddlType))

               If g_classes.descriptors(sourceClassIndex).hasOwnTable Then
                 offset = 0
               Else
                 Print #fileNo, addTab(2); "IF EXISTS (SELECT 1 FROM ";
                 If forTvColumns Then
                   Print #fileNo, genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, True);
                 Else
                   Print #fileNo, qualTabNamePriv;
                 End If
                 Print #fileNo, " WHERE "; g_anCid; " IN ("; g_classes.descriptors(sourceClassIndex).subclassIdStrListNonAbstract; ")) THEN"
                 offset = 1
               End If

             Print #fileNo, addTab(offset + 2); "UPDATE"
             If forTvColumns Then
               Print #fileNo, addTab(offset + 3); genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, True); " T"
             Else
               Print #fileNo, addTab(offset + 3); qualTabNamePriv; " T"
             End If
 
             Print #fileNo, addTab(offset + 2); "SET"
             Print #fileNo, addTab(offset + 2); "("

             colonMissing = False
             For i = 1 To attrRefsInclSubClasses.numDescriptors
               If attrRefsInclSubClasses.descriptors(i).refType = eadrtAttribute And attrRefsInclSubClasses.descriptors(i).refIndex > 0 Then
                   If (g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isTimeVarying = forTvColumns) And g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isVirtual Then
                     If colonMissing Then
                       Print #fileNo, ","
                     End If
                     Print #fileNo, addTab(offset + 3); g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).dbColName(ddlType);
                     colonMissing = True
                   End If
               End If
             Next i
             Print #fileNo, ""

             Print #fileNo, addTab(offset + 2); ")"
             Print #fileNo, addTab(offset + 2); "="
             Print #fileNo, addTab(offset + 2); "("
             Print #fileNo, addTab(offset + 3); "SELECT"

             colonMissing = False
             For i = 1 To attrRefsInclSubClasses.numDescriptors
               If attrRefsInclSubClasses.descriptors(i).refType = eadrtAttribute And attrRefsInclSubClasses.descriptors(i).refIndex > 0 Then
                   If (g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isTimeVarying = forTvColumns) And g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).isVirtual Then
                     If colonMissing Then
                       Print #fileNo, ","
                     End If
                     Print #fileNo, addTab(offset + 4); "COALESCE(T."; g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).dbColName(ddlType); ", S."; genAttrName(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).virtuallyMapsTo.mapTo & IIf(g_attributes.descriptors(attrRefsInclSubClasses.descriptors(i).refIndex).valueType = eavtEnum, gc_enumAttrNameSuffix, ""), ddlType); ")";
                     colonMissing = True
                   End If
               End If
             Next i
             Print #fileNo, ""

             Print #fileNo, addTab(offset + 3); "FROM"
             If forTvColumns Then
               Print #fileNo, addTab(offset + 4); "("
               Print #fileNo, addTab(offset + 5); "SELECT "; g_anOid; ", "; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName); _
                                                  virtAttrlist; " FROM "; genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, False); " "; _
                                                  "WHERE "; g_anInLrt; " <> lrtOid_in AND "; g_anIsDeleted; " = "; gc_dbFalse; _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 6); "UNION ALL"
               Print #fileNo, addTab(offset + 5); "SELECT "; g_anOid; ", "; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName); _
                                                  virtAttrlist; " FROM "; genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, True); " "; _
                                                  "WHERE "; g_anInLrt; " = lrtOid_in AND "; g_anLrtState; " <> "; CStr(lrtStatusDeleted); _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 4); ") S"

               Print #fileNo, addTab(offset + 3); "INNER JOIN"

               Print #fileNo, addTab(offset + 4); "("
               Print #fileNo, addTab(offset + 5); "SELECT "; g_anOid; ", "; fkAttrName; " FROM "; genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " "; _
                                                  "WHERE "; g_anInLrt; " <> lrtOid_in AND "; g_anIsDeleted; " = "; gc_dbFalse; _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 6); "UNION ALL"
               Print #fileNo, addTab(offset + 5); "SELECT "; g_anOid; ", "; fkAttrName; _
                                                  " FROM "; genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True); " "; _
                                                  "WHERE "; g_anInLrt; " = lrtOid_in AND "; g_anLrtState; " <> "; CStr(lrtStatusDeleted); _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 4); ") TPar"

               Print #fileNo, addTab(offset + 3); "ON"
               Print #fileNo, addTab(offset + 4); "TPar."; fkAttrName; " = S."; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName)
             Else
               Print #fileNo, addTab(offset + 4); "("
               Print #fileNo, addTab(offset + 5); "SELECT "; g_anOid; virtAttrlist; " FROM "; genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, False); " "; _
                                                  "WHERE "; g_anInLrt; " <> lrtOid_in AND "; g_anIsDeleted; " = "; gc_dbFalse; _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 6); "UNION ALL"
               Print #fileNo, addTab(offset + 5); "SELECT "; g_anOid; virtAttrlist; " FROM "; genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, True); " "; _
                                                  "WHERE "; g_anInLrt; " = lrtOid_in AND "; g_anLrtState; " <> "; CStr(lrtStatusDeleted); _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 4); ") S"
             End If

             Print #fileNo, addTab(offset + 3); "WHERE"

             If forTvColumns Then
               Print #fileNo, addTab(offset + 4); "T."; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName); " = TPar."; g_anOid
             Else
                 Print #fileNo, addTab(offset + 4); "T."; fkAttrName; " = S."; g_anOid
             End If

             Print #fileNo, addTab(offset + 3); "FETCH FIRST 1 ROW ONLY"
             Print #fileNo, addTab(offset + 2); ")"

             Print #fileNo, addTab(offset + 2); "WHERE"
             Print #fileNo, addTab(offset + 3); "T."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(offset + 4); "AND"
             Print #fileNo, addTab(offset + 3); "T."; g_anLrtState; " = opId_in"

               If Not g_classes.descriptors(sourceClassIndex).hasOwnTable Then
                 Print #fileNo, addTab(offset + 4); "AND"
                 Print #fileNo, addTab(offset + 3); "T."; g_anCid; " IN ("; g_classes.descriptors(sourceClassIndex).subclassIdStrListNonAbstract; ")"
               End If
               If g_classes.descriptors(sourceClassIndex).isPsTagged Then
                 Print #fileNo, addTab(offset + 4); "AND"
                 Print #fileNo, addTab(offset + 3); "T."; g_anPsOid; " = psOid_in"
               End If

             Print #fileNo, addTab(offset + 4); "AND"
             Print #fileNo, addTab(offset + 3); "EXISTS ("
             Print #fileNo, addTab(offset + 4); "SELECT"
             Print #fileNo, addTab(offset + 5); "1"
             Print #fileNo, addTab(offset + 4); "FROM"

             If forTvColumns Then
               Print #fileNo, addTab(offset + 4); "("
               Print #fileNo, addTab(offset + 5); "SELECT "; g_anOid; ", "; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName); _
                                                  ", "; g_anValidFrom; ", "; g_anValidTo; _
                                                  " FROM "; genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, False); " "; _
                                                  "WHERE "; g_anInLrt; " <> lrtOid_in AND "; g_anIsDeleted; " = "; gc_dbFalse; _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 6); "UNION ALL"
               Print #fileNo, addTab(offset + 5); "SELECT "; g_anOid; ", "; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName); _
                                                  ", "; g_anValidFrom; ", "; g_anValidTo; _
                                                  " FROM "; genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forTvColumns, True); " "; _
                                                  "WHERE "; g_anInLrt; " = lrtOid_in AND "; g_anLrtState; " <> "; CStr(lrtStatusDeleted); _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 4); ") S"

               Print #fileNo, addTab(offset + 4); "INNER JOIN"

               Print #fileNo, addTab(offset + 5); "("
               Print #fileNo, addTab(offset + 6); "SELECT "; g_anOid; ", "; fkAttrName; _
                                                  " FROM "; genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " "; _
                                                  "WHERE "; g_anInLrt; " <> lrtOid_in AND "; g_anIsDeleted; " = "; gc_dbFalse; _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 7); "UNION ALL"
               Print #fileNo, addTab(offset + 6); "SELECT "; g_anOid; ", "; fkAttrName; _
                                                  " FROM "; genQualTabNameByClassIndex(sourceOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True); " "; _
                                                  "WHERE "; g_anInLrt; " = lrtOid_in AND "; g_anLrtState; " <> "; CStr(lrtStatusDeleted); _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 5); ") TPar"

               Print #fileNo, addTab(offset + 4); "ON"
               Print #fileNo, addTab(offset + 5); "TPar."; fkAttrName; " = S."; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName)
             Else
               Print #fileNo, addTab(offset + 5); "("
               Print #fileNo, addTab(offset + 6); "SELECT "; g_anOid; " FROM "; genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " "; _
                                                  "WHERE "; g_anInLrt; " <> lrtOid_in AND "; g_anIsDeleted; " = "; gc_dbFalse; _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 7); "UNION ALL"
               Print #fileNo, addTab(offset + 6); "SELECT "; g_anOid; " FROM "; genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True); " "; _
                                                  "WHERE "; g_anInLrt; " = lrtOid_in AND "; g_anLrtState; " <> "; CStr(lrtStatusDeleted); _
                                                  IIf(g_classes.descriptors(sourceOrParClassIndex).isPsTagged, " AND " & g_anPsOid & " = psOid_in", "")
               Print #fileNo, addTab(offset + 5); ") S"
             End If

             Print #fileNo, addTab(offset + 4); "WHERE"

             If forTvColumns Then
               Print #fileNo, addTab(offset + 5); "T."; genSurrogateKeyName(ddlType, g_classes.descriptors(targetOrParClassIndex).shortName); " = TPar."; g_anOid
               Print #fileNo, addTab(offset + 6); "AND"
               Print #fileNo, addTab(offset + 5); "S."; g_anValidFrom; " <= T."; g_anValidFrom
               Print #fileNo, addTab(offset + 6); "AND"
               Print #fileNo, addTab(offset + 5); "S."; g_anValidTo; " >= T."; g_anValidFrom
             Else
                 Print #fileNo, addTab(offset + 5); "T."; fkAttrName; " = S."; g_anOid
             End If

             Print #fileNo, addTab(offset + 3); ")"
             Print #fileNo, addTab(offset + 2); ";"

             If Not g_classes.descriptors(sourceClassIndex).hasOwnTable Then
               Print #fileNo, addTab(2); "END IF;"
             End If
           Next j

           Print #fileNo, addTab(1); "END IF;"

           If isEndSlot Then
             Dim qualTabNameAggregationSlotPriv As String
             qualTabNameAggregationSlotPriv = genQualTabNameByClassIndex(g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, False, True)
 
             Print #fileNo, addTab(1); "IF opId_in = " & CStr(lrtStatusUpdated) & " THEN"
             genProcSectionHeader(fileNo, "if a EndSlot exists for this LRT with a changed ASL reference", 2, True)
             genProcSectionHeader(fileNo, "then set the FK in public temporarily to null to avoid constraint violations if the referenced ASL is deleted", 2, True)
             Print #fileNo, addTab(2); "UPDATE"
             Print #fileNo, addTab(3); genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " AS u_esl"
             Print #fileNo, addTab(2); "SET"
             Print #fileNo, addTab(3); "u_esl.esrasl_oid"
             Print #fileNo, addTab(2); "="
             Print #fileNo, addTab(3); "NULL"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "u_esl."; g_anOid; " IN ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "esl_l."; g_anOid
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, True); " AS esl_l"
             Print #fileNo, addTab(4); "INNER JOIN"
             Print #fileNo, addTab(5); genQualTabNameByClassIndex(targetOrParClassIndex, ddlType, thisOrgIndex, thisPoolIndex, False, False); " AS esl"
             Print #fileNo, addTab(4); "ON"
             Print #fileNo, addTab(5); "esl_l."; g_anOid; " = esl."; g_anOid
             Print #fileNo, addTab(4); "INNER JOIN"
             Print #fileNo, addTab(5); qualTabNameAggregationSlotPriv; " AS asl_l"
             Print #fileNo, addTab(4); "ON"
             Print #fileNo, addTab(5); "esl.esrasl_oid = asl_l."; g_anOid
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "esl_l."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(5); "esl_l.lrtstate = "; CStr(lrtStatusUpdated)
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(5); "esl_l."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(5); "esl."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(5); "asl_l.lrtstate = "; CStr(lrtStatusDeleted)
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(2); ";"
             Print #fileNo, addTab(1); "END IF;"
           End If

         End If

         If isTypeSpec Then
           Dim qualTabNameTypeSpecPriv As String
           qualTabNameTypeSpecPriv = genQualTabNameByClassIndex(g_classIndexTypeSpec, ddlType, thisOrgIndex, thisPoolIndex, , True)
           Dim qualTabNameTypeSpecPub As String
           qualTabNameTypeSpecPub = genQualTabNameByClassIndex(g_classIndexTypeSpec, ddlType, thisOrgIndex, thisPoolIndex, , False)

           Print #fileNo, addTab(1); "IF opId_in = " & CStr(lrtStatusUpdated) & " THEN"
           genProcSectionHeader(fileNo, "if a TypeSpec exists for this LRT with a changed TPA reference", 2, True)
           genProcSectionHeader(fileNo, "then set the FK in public temporarily to null to avoid constraint violations if the referenced TPA is deleted", 2, True)
           Print #fileNo, addTab(2); "UPDATE"
           Print #fileNo, addTab(3); qualTabNameTypeSpecPub; " TS"
           Print #fileNo, addTab(2); "SET"
           Print #fileNo, addTab(3); "TS.TSTTPA_OID"
           Print #fileNo, addTab(2); "="
           Print #fileNo, addTab(3); "NULL"
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "TS."; g_anOid; " IN ("
           Print #fileNo, addTab(4); "SELECT"
           Print #fileNo, addTab(5); "TSL."; g_anOid
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); qualTabNameTypeSpecPriv; " TSL"
           Print #fileNo, addTab(4); "INNER JOIN"
           Print #fileNo, addTab(5); qualTabNameTypeSpecPub; " TSP"
           Print #fileNo, addTab(4); "ON"
           Print #fileNo, addTab(5); "TSL."; g_anOid; " = TSP."; g_anOid
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "TSL."; g_anInLrt; " = lrtOid_in"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "TSL.LRTSTATE = "; CStr(lrtStatusUpdated)
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "TSL."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "TSP."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "TSP.TSTTPA_OID IS NOT NULL"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "("
           Print #fileNo, addTab(6); "TSL.TSTTPA_OID <> TSP.TSTTPA_OID"
           Print #fileNo, addTab(5); "OR"
           Print #fileNo, addTab(6); "TSL.TSTTPA_OID IS NULL"
           Print #fileNo, addTab(5); ")"
           Print #fileNo, addTab(3); ")"
           Print #fileNo, addTab(2); ";"

           genProcSectionHeader(fileNo, "if a TypeSpec exists for this LRT with a changed previousTypeSpec reference", 2, True)
           genProcSectionHeader(fileNo, "then set the FK in public temporarily to null to avoid constraint violations if the referenced TypeSpec is deleted", 2, True)
           Print #fileNo, addTab(2); "UPDATE"
           Print #fileNo, addTab(3); qualTabNameTypeSpecPub; " TS"
           Print #fileNo, addTab(2); "SET"
           Print #fileNo, addTab(3); "TS.PTYPTY_OID"
           Print #fileNo, addTab(2); "="
           Print #fileNo, addTab(3); "NULL"
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "TS."; g_anOid; " IN ("
           Print #fileNo, addTab(4); "SELECT"
           Print #fileNo, addTab(5); "TSL."; g_anOid
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); qualTabNameTypeSpecPriv; " TSL"
           Print #fileNo, addTab(4); "INNER JOIN"
           Print #fileNo, addTab(5); qualTabNameTypeSpecPub; " TSP"
           Print #fileNo, addTab(4); "ON"
           Print #fileNo, addTab(5); "TSL."; g_anOid; " = TSP."; g_anOid
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "TSL."; g_anInLrt; " = lrtOid_in"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "TSL.LRTSTATE = "; CStr(lrtStatusUpdated)
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "TSL."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "TSP."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "TSP.PTYPTY_OID IS NOT NULL"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(5); "("
           Print #fileNo, addTab(6); "TSL.PTYPTY_OID <> TSP.PTYPTY_OID"
           Print #fileNo, addTab(5); "OR"
           Print #fileNo, addTab(6); "TSL.PTYPTY_OID IS NULL"
           Print #fileNo, addTab(5); ")"
           Print #fileNo, addTab(3); ")"
           Print #fileNo, addTab(2); ";"
           Print #fileNo, addTab(1); "END IF;"

         End If

         If isGenericCode Then
           If isPrimaryOrg Then
             ' special treetment for GENERICCODE: for a newly inserted / deleted GENERICCODE its association(s) to CATEGORY must replicated to each PRODUCTSTRUCTURE
             Dim qualTabNameCodeCategoryPriv As String
             qualTabNameCodeCategoryPriv = genQualTabNameByRelIndex(g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, True)
             Dim qualTabNameCodeCategoryPub As String
             qualTabNameCodeCategoryPub = genQualTabNameByRelIndex(g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex, False)
             Dim qualTabNameGenericCodePriv As String
             qualTabNameGenericCodePriv = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, , True)
             Dim qualTabNameCategory As String
             qualTabNameCategory = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, thisOrgIndex, thisPoolIndex)

             genProcSectionHeader(fileNo, "declare variables", , True)
             genVarDecl(fileNo, "v_lrtEntityIdCount", "BIGINT", "0")
             genSpLogDecl(fileNo)
 
             genSpLogProcEnter(fileNo, qualProcNameLrtCommitPreProc, ddlType, , "lrtOid_in", "'cdUserId_in", "psOid_in", "opId_in", "#commitTs_in", "rowCount_out")

             Print #fileNo,
             Print #fileNo, addTab(1); "IF opId_in = " & CStr(lrtStatusCreated) & " THEN"

             genProcSectionHeader(fileNo, "create CODE <-> temporary CATEGORY relationships for newly created GENERICCODE and all ProductStructures", 2, True)
             Print #fileNo, addTab(2); "INSERT INTO"
             Print #fileNo, addTab(3); qualTabNameCodeCategoryPriv
             Print #fileNo, addTab(2); "("

             genAttrListForEntity(g_relIndexCodeCategory, eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt)

             Print #fileNo, addTab(2); ")"

             Print #fileNo, addTab(2); "SELECT"

             initAttributeTransformation(transformation, 12, , , , "GC.")

             setAttributeMapping(transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid)
             setAttributeMapping(transformation, 2, conHasBeenSetProductive, gc_dbFalse)
             setAttributeMapping(transformation, 3, conLrtState, CStr(lrtStatusCreated))
             setAttributeMapping(transformation, 4, conDpClassNumber, "CAST(NULL AS SMALLINT)")
             setAttributeMapping(transformation, 5, "GCO_OID", "GC." & g_anOid)
             setAttributeMapping(transformation, 6, "CAT_OID", "CA." & g_anOid)
             setAttributeMapping(transformation, 7, conPsOid, "PS." & g_anOid)
             setAttributeMapping(transformation, 8, conInLrt, "lrtOid_in")
             setAttributeMapping(transformation, 9, conCreateUser, "cdUserId_in")
             setAttributeMapping(transformation, 10, conCreateTimestamp, "commitTs_in")
             setAttributeMapping(transformation, 11, conUpdateUser, "cdUserId_in")
             setAttributeMapping(transformation, 12, conLastUpdateTimestamp, "commitTs_in")

             genTransformedAttrListForEntity(g_relIndexCodeCategory, eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, False, edomListLrt)

             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); qualTabNameGenericCodePriv; " GC"

             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); g_qualTabNameDivision; " DV"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "GC.CDIDIV_OID = DV."; g_anOid

             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); g_qualTabNameProductStructure; " PS"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "PS.PDIDIV_OID = DV."; g_anOid

             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); qualTabNameCategory; " CA"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "CA."; g_anPsOid; " = PS."; g_anOid
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CA."; g_anIsDefault; " = "; gc_dbTrue

             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "GC.LRTSTATE = "; CStr(lrtStatusCreated)
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "GC."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "NOT EXISTS"
             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); "SELECT"
             Print #fileNo, addTab(6); "1"
             Print #fileNo, addTab(5); "FROM"
             Print #fileNo, addTab(6); qualTabNameCodeCategoryPriv; " CC"
             Print #fileNo, addTab(5); "WHERE"
             Print #fileNo, addTab(6); "GC."; g_anOid; " = CC.GCO_OID"
             Print #fileNo, addTab(7); "AND"
             Print #fileNo, addTab(6); "PS."; g_anOid; " = CC."; g_anPsOid
             Print #fileNo, addTab(7); "AND"
             Print #fileNo, addTab(6); "GC."; g_anInLrt; " = CC."; g_anInLrt
             Print #fileNo, addTab(4); ")"

             Print #fileNo, addTab(2); ";"

               genDdlForUpdateAffectedEntities(fileNo, "ACM-Relationship", eactRelationship, "R", False, False, qualTabNameLrtAffectedEntity, _
                 g_relationships.descriptors(g_relIndexCodeCategory).relIdStr, g_relationships.descriptors(g_relIndexCodeCategory).aggHeadClassIdStr, "lrtOid_in", 2, CStr(lrtStatusCreated), False)

             Print #fileNo, addTab(1); "ELSEIF opId_in = " & CStr(lrtStatusDeleted) & " THEN"
             genProcSectionHeader(fileNo, "delete CODE <-> temporary CATEGORY relationships for deleted GENERICCODE and all ProductStructures", 2, True)

             Print #fileNo, addTab(2); "INSERT INTO"
             Print #fileNo, addTab(3); qualTabNameCodeCategoryPriv
             Print #fileNo, addTab(2); "("

             genAttrListForEntity(g_relIndexCodeCategory, eactRelationship, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, True, forGen, edomListLrt)

             Print #fileNo, addTab(2); ")"

             Print #fileNo, addTab(2); "SELECT"

             initAttributeTransformation(transformation, 3, , , , "CC.")

             setAttributeMapping(transformation, 1, conLrtState, CStr(lrtStatusDeleted))
             setAttributeMapping(transformation, 2, conInLrt, "lrtOid_in")
             setAttributeMapping(transformation, 3, conStatusId, CStr(statusWorkInProgress))

             genTransformedAttrListForEntity(g_relIndexCodeCategory, eactRelationship, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, , True, False, edomListLrt)

             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); qualTabNameGenericCodePriv; " GC"

             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); qualTabNameCodeCategoryPub; " CC"

             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "GC."; g_anOid; " = CC.GCO_OID"

             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "GC.LRTSTATE = "; CStr(lrtStatusDeleted)
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "GC."; g_anInLrt; " = lrtOid_in"

             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "NOT EXISTS"
             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); "SELECT"
             Print #fileNo, addTab(6); "1"
             Print #fileNo, addTab(5); "FROM"
             Print #fileNo, addTab(6); qualTabNameCodeCategoryPriv; " CCPRIV"
             Print #fileNo, addTab(5); "WHERE"
             Print #fileNo, addTab(6); "CCPRIV."; g_anOid; " = CC."; g_anOid
             Print #fileNo, addTab(7); "AND"
             Print #fileNo, addTab(6); "CCPRIV."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(4); ")"

             Print #fileNo, addTab(2); ";"

             genProcSectionHeader(fileNo, "lock public records", 2)
             Print #fileNo, addTab(2); "UPDATE"
             Print #fileNo, addTab(3); qualTabNameCodeCategoryPub; " PUB"
             Print #fileNo, addTab(2); "SET"
             Print #fileNo, addTab(3); "PUB."; g_anInLrt & " = lrtOid_in"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "EXISTS"
             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); "SELECT"
             Print #fileNo, addTab(6); "1"
             Print #fileNo, addTab(5); "FROM"
             Print #fileNo, addTab(6); qualTabNameCodeCategoryPriv; " PRIV"
             Print #fileNo, addTab(5); "WHERE"
             Print #fileNo, addTab(6); "PUB."; g_anOid; " = PRIV."; g_anOid
             Print #fileNo, addTab(5); "AND"
             Print #fileNo, addTab(6); "PRIV."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(4); ")"

             Print #fileNo, addTab(2); ";"

               genDdlForUpdateAffectedEntities(fileNo, "ACM-Relationship", eactRelationship, "R", False, False, qualTabNameLrtAffectedEntity, _
                 g_relationships.descriptors(g_relIndexCodeCategory).relIdStr, g_relationships.descriptors(g_relIndexCodeCategory).aggHeadClassIdStr, "lrtOid_in", 2, CStr(lrtStatusDeleted), False)

             Print #fileNo, addTab(1); "END IF;"

           Else
             genSpLogDecl(fileNo)
 
             genSpLogProcEnter(fileNo, qualProcNameLrtCommitPreProc, ddlType, , "lrtOid_in", "'cdUserId_in", "psOid_in", "opId_in", "#commitTs_in", "rowCount_out")

             genProcSectionHeader(fileNo, "pre-processing is only done at factory side", 1, True)
           End If
         End If

         genSpLogProcExit(fileNo, qualProcNameLrtCommitPreProc, ddlType, , "lrtOid_in", "'cdUserId_in", "psOid_in", "opId_in", "#commitTs_in", "rowCount_out")

         Print #fileNo, addTab(0); "END"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       End If
     End If
   End If
 
   genLrtSupportSpsForEntity2(acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNo, fileNoClView, ddlType, forGen, forNl)
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub

 
 Private Sub genLrtSupportSpsForEntity2( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoClView As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False _
 )
 ' ### ELSE IVK ###
 'Private Sub genLrtSupportSpsForEntity( _
 ' ByRef acmEntityIndex As Integer, _
 ' ByRef acmEntityType As AcmAttrContainerType, _
 ' thisOrgIndex As Integer, _
 ' thisPoolIndex As Integer, _
 ' fileNo As Integer, _
 ' fileNoClView As Integer, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional forGen As Boolean = False, _
 ' Optional forNl As Boolean = False _
 ')
 ' ### ENDIF IVK ###
   Dim sectionName As String
   Dim sectionShortName As String
   Dim sectionIndex As Integer
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim ahClassIdStr As String
   Dim isUserTransactional As Boolean
   Dim isPsTagged As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim entityIdStrList As String
   Dim dbAcmEntityType As String
   Dim attrRefs As AttrDescriptorRefs
   Dim attrRefsInclSubClasses As AttrDescriptorRefs
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
   Dim hasNlAttributes As Boolean
   Dim hasNlTable As Boolean
   Dim attrMapping() As AttributeMappingForCl
   Dim relLeftClassIdStr As String
   Dim relLeftFk As String
   Dim relRightClassIdStr As String
   Dim relRightFk As String
   Dim ignoreForChangelog As Boolean
   Dim aggHeadClassIndex As Integer
   Dim aggHeadShortClassName As String
   Dim isAggregateHead As Boolean
   Dim implicitelyGenChangeComment As Boolean
   Dim busKeyAttrList As String
   Dim busKeyAttrListNoFks As String
   Dim busKeyAttrArray() As String
   Dim busKeyAttrArrayNoFks() As String
   Dim useLrtCommitPreprocess As Boolean
   Dim tmpClassId As String
 
 ' ### IF IVK ###
   Dim hasNoIdentity As Boolean
   Dim hasPriceAssignmentSubClass As Boolean
   Dim hasPriceAssignmentAggHead As Boolean
   Dim isSubjectToPreisDurchschuss As Boolean
   Dim priceAssignmentSubClassIdList As String
   Dim hasGroupIdAttrs As Boolean
   Dim groupIdAttrIndexes() As Integer
   Dim isGenericAspectHead As Boolean ' GenericAspects always need special treatment ;-)
   Dim isGenericCode As Boolean
   Dim condenseData As Boolean

   Dim hasRelBasedVirtualAttrInGenInclSubClasses As Boolean
   Dim hasRelBasedVirtualAttrInNonGenInclSubClasses As Boolean
   Dim isNationalizable As Boolean
   Dim hasIsNationalInclSubClasses As Boolean
 ' ### ENDIF IVK ###

   On Error GoTo ErrorExit

   If ddlType = edtPdm Then
     If thisPoolIndex < 1 Then
       Exit Sub
     ElseIf Not g_pools.descriptors(thisPoolIndex).supportLrt Then
       Exit Sub
     End If
   End If

   Dim transformation As AttributeListTransformation
   transformation = nullAttributeTransformation

   busKeyAttrList = ""
   busKeyAttrListNoFks = ""
   useLrtCommitPreprocess = False
 ' ### IF IVK ###
   hasPriceAssignmentSubClass = False
   hasPriceAssignmentAggHead = False
   priceAssignmentSubClassIdList = ""
   hasRelBasedVirtualAttrInGenInclSubClasses = False
   hasRelBasedVirtualAttrInNonGenInclSubClasses = False
 ' ### ENDIF IVK ###

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
   Dim ukAttrDecls As String
   Dim pkAttrList As String
   Dim leftFkAttrs As String
   Dim rightFkAttrs As String
   Dim isPrimaryOrg As Boolean
 
   isPrimaryOrg = (thisOrgIndex = g_primaryOrgIndex)

   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_classes.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       entityIdStrList = getSubClassIdStrListByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex)
       ahClassIdStr = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       hasNlAttributes = IIf(forGen, g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses)
       attrMapping = g_classes.descriptors(acmEntityIndex).clMapAttrsInclSubclasses
       ignoreForChangelog = g_classes.descriptors(acmEntityIndex).ignoreForChangelog
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       isAggregateHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).classIndex)
       implicitelyGenChangeComment = g_classes.descriptors(acmEntityIndex).implicitelyGenChangeComment
       If g_classes.descriptors(acmEntityIndex).hasBusinessKey Then
         busKeyAttrList = getPkAttrListByClassIndex(acmEntityIndex, ddlType)
         busKeyAttrListNoFks = getPkAttrListByClassIndex(acmEntityIndex, ddlType, , , , True)

         genAttrList(busKeyAttrArrayNoFks, busKeyAttrListNoFks)
       End If
       useLrtCommitPreprocess = g_classes.descriptors(acmEntityIndex).useLrtCommitPreprocess And Not forGen And Not forNl
 ' ### IF IVK ###
       hasGroupIdAttrs = Not forNl And Not forGen And g_classes.descriptors(acmEntityIndex).hasGroupIdAttrInNonGenInclSubClasses
       If hasGroupIdAttrs Then
         groupIdAttrIndexes = g_classes.descriptors(acmEntityIndex).groupIdAttrIndexesInclSubclasses
       End If
       hasRelBasedVirtualAttrInGenInclSubClasses = g_classes.descriptors(acmEntityIndex).hasRelBasedVirtualAttrInGenInclSubClasses
       hasRelBasedVirtualAttrInNonGenInclSubClasses = g_classes.descriptors(acmEntityIndex).hasRelBasedVirtualAttrInNonGenInclSubClasses
       isGenericAspectHead = UCase(g_classes.descriptors(acmEntityIndex).className) = UCase(clnGenericAspect) And Not forGen And Not forNl
       isGenericCode = UCase(g_classes.descriptors(acmEntityIndex).className) = UCase(clnGenericCode) And Not forGen And Not forNl
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       isNationalizable = g_classes.descriptors(acmEntityIndex).isNationalizable
       hasIsNationalInclSubClasses = g_classes.descriptors(acmEntityIndex).hasIsNationalInclSubClasses And Not forNl
 ' ### ENDIF IVK ###

       If forNl Then
         entityName = genNlObjName(g_classes.descriptors(acmEntityIndex).className, , forGen)
         entityShortName = genNlObjShortName(g_classes.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Class (NL-Text)"
         hasOwnTable = True
         isAbstract = False
         attrRefs = g_classes.descriptors(acmEntityIndex).nlAttrRefsInclSubclasses
         attrRefsInclSubClasses = g_classes.descriptors(acmEntityIndex).nlAttrRefsInclSubclasses
         relRefs.numRefs = 0
         isGenForming = False
 ' ### IF IVK ###
         isPsTagged = usePsTagInNlTextTables And g_classes.descriptors(acmEntityIndex).isPsTagged
         hasNoIdentity = False
 ' ### ENDIF IVK ###
       Else
         entityName = g_classes.descriptors(acmEntityIndex).className
         entityShortName = g_classes.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Class"
         hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
         isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
         attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
         attrRefsInclSubClasses = g_classes.descriptors(acmEntityIndex).attrRefsInclSubClassesWithRepeat
         relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
         isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
 ' ### IF IVK ###
         isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
         hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
         hasPriceAssignmentSubClass = g_classes.descriptors(acmEntityIndex).hasPriceAssignmentSubClass
         hasPriceAssignmentAggHead = g_classes.descriptors(acmEntityIndex).hasPriceAssignmentAggHead
         isSubjectToPreisDurchschuss = g_classes.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss

         If hasPriceAssignmentSubClass Then
           Dim i As Integer
           For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive)
               If Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isPriceAssignment Then
                 priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).classIdStr & "'"
               End If
           Next i
         ElseIf hasPriceAssignmentAggHead Then
             For i = 1 To UBound(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive)
                 If Not g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isPriceAssignment Then
                   priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).classIdStr & "'"
                 End If
             Next i
         End If
 ' ### ENDIF IVK ###
       End If
   ElseIf acmEntityType = eactRelationship Then
       If forNl Then
         entityName = genNlObjName(g_relationships.descriptors(acmEntityIndex).relName, , forGen)
         entityShortName = genNlObjShortName(g_relationships.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Relationship (NL-Text)"
 ' ### IF IVK ###
         isPsTagged = usePsTagInNlTextTables And g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
         attrRefs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
         attrRefsInclSubClasses = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
       Else
         entityName = g_relationships.descriptors(acmEntityIndex).relName
         entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Relationship"
 ' ### IF IVK ###
         isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
         attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
         attrRefsInclSubClasses = g_relationships.descriptors(acmEntityIndex).attrRefs
       End If
 
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       sectionShortName = g_relationships.descriptors(acmEntityIndex).sectionShortName
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       entityIdStrList = "'" & g_relationships.descriptors(acmEntityIndex).relIdStr & "'"
       ahClassIdStr = g_relationships.descriptors(acmEntityIndex).aggHeadClassIdStr
       dbAcmEntityType = "R"
       relRefs.numRefs = 0
       isGenForming = False
 ' ### IF IVK ###
       hasNoIdentity = False
 ' ### ENDIF IVK ###
       hasNlAttributes = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0
       ignoreForChangelog = g_relationships.descriptors(acmEntityIndex).ignoreForChangelog
       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       isAggregateHead = False
 ' ### IF IVK ###
       isGenericAspectHead = False
       isGenericCode = False
       hasPriceAssignmentAggHead = g_relationships.descriptors(acmEntityIndex).hasPriceAssignmentAggHead
       isSubjectToPreisDurchschuss = g_relationships.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss
       If hasPriceAssignmentAggHead Then
           For i = 1 To UBound(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive)
               If Not g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isPriceAssignment Then
                 priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).classIdStr & "'"
               End If
           Next i
       End If

       hasGroupIdAttrs = False
       condenseData = False
       isNationalizable = g_relationships.descriptors(acmEntityIndex).isNationalizable And Not g_relationships.descriptors(acmEntityIndex).isNl
       hasIsNationalInclSubClasses = g_relationships.descriptors(acmEntityIndex).hasIsNationalInclSubClasses And Not g_relationships.descriptors(acmEntityIndex).isNl
 ' ### ENDIF IVK ###

       genTransformedAttrDeclsForRelationshipWithColReUse_Int(acmEntityIndex, transformation, tabColumns, _
           ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, , False, False, edomNone)
       busKeyAttrList = leftFkAttrs & "," & rightFkAttrs

       Dim reuseRelIndex As Integer
       reuseRelIndex = IIf(reuseRelationships And g_relationships.descriptors(acmEntityIndex).reusedRelIndex > 0, g_relationships.descriptors(acmEntityIndex).reusedRelIndex, acmEntityIndex)
           relLeftClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).classIdStr
           relLeftFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).shortName)
           relRightClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).classIdStr
           relRightFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).shortName)
   Else
     Exit Sub
   End If
 
 ' ### IF IVK ###
   hasNlTable = hasNlAttributes Or (isAggregateHead And implicitelyGenChangeComment And Not forGen And Not forNl And Not condenseData)
 ' ### ELSE IVK ###
 ' hasNlTable = hasNlAttributes Or (isAggregateHead And implicitelyGenChangeComment And Not forGen And Not forNl)
 ' ### ENDIF IVK ###

   If Not generateLrt Or Not isUserTransactional Then
     Exit Sub
   End If
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' LRT is only supported at 'pool-level'
     Exit Sub
   End If
 
   If aggHeadClassIndex > 0 Then
     aggHeadShortClassName = g_classes.descriptors(aggHeadClassIndex).shortName
   End If
 
   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNamePub As String, qualTabNamePriv As String
   Dim qualTabNamePubNl As String, qualTabNamePrivNl As String

   If acmEntityType = eactClass Then
       qualTabNamePub = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen)
       qualTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True)

       If hasNlTable Then
         qualTabNamePubNl = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , True)
         qualTabNamePrivNl = genQualTabNameByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , True)
       End If
   Else
       qualTabNamePub = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex)
       qualTabNamePriv = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, True)

       If hasNlTable Then
         qualTabNamePubNl = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, , , True)
         qualTabNamePrivNl = genQualTabNameByRelIndex(g_relationships.descriptors(acmEntityIndex).relIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , True)
       End If
   End If

   Dim qualAggHeadTabName As String
   Dim qualAggHeadNlTabNamePriv As String
   Dim aggHeadFkAttrName As String
   qualAggHeadTabName = ""
   qualAggHeadNlTabNamePriv = ""
   If aggHeadClassIndex > 0 Then
       qualAggHeadTabName = genQualTabNameByClassIndex(g_classes.descriptors(aggHeadClassIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex)
       qualAggHeadNlTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(aggHeadClassIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , True, , True)
       aggHeadFkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(aggHeadClassIndex).shortName)
   End If

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   Dim hasNlLabelAttr As Boolean
   hasNlLabelAttr = False
 ' ### IF IVK ###
   Dim labelIsNationalizable As Boolean
   labelIsNationalizable = False
 ' ### ENDIF IVK ###

   If Not forNl Then
     initAttributeTransformation(transformation, 0, , True)
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , True, forGen, edomNone)

       Dim j As Integer
       For j = 1 To transformation.nlAttrRefs.numDescriptors
         If transformation.nlAttrRefs.descriptors(j).refType = eadrtAttribute Then
             If UCase(g_attributes.descriptors(transformation.nlAttrRefs.descriptors(j).refIndex).attributeName) = "LABEL" Then
               hasNlLabelAttr = True
 ' ### IF IVK ###
               labelIsNationalizable = g_attributes.descriptors(transformation.nlAttrRefs.descriptors(j).refIndex).isNationalizable
 ' ### ENDIF IVK ###
             End If
         End If
       Next j
   End If

 ' ### IF IVK ###
   Dim setManActConditional As Boolean
   setManActConditional = Not isPrimaryOrg And hasIsNationalInclSubClasses

 ' ### ENDIF IVK ###
   If generateLrtSps Then
     If Not forNl Then

       ' ####################################################################################################################
       ' #    SP for COMMIT on given class
       ' ####################################################################################################################

       Dim qualProcNameLrtCommit As String
 
       qualProcNameLrtCommit = _
         genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, spnLrtCommit)

       printSectionHeader("SP for LRT-COMMIT on """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo)
 
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcNameLrtCommit
       Print #fileNo, addTab(0); "("
       genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to commit")
       genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "ID of the user owning the LRT")
 ' ### IF IVK ###
       genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure corresponding to the LRT")
 ' ### ENDIF IVK ###
       genProcParm(fileNo, "IN", "lrtStatus_in", g_dbtEnumId, True, "commit only records in this status (locked[" & lrtStatusLocked & "], created[" & lrtStatusCreated & "], updated[" & lrtStatusUpdated & "], deleted[" & lrtStatusDeleted & "])")
       genProcParm(fileNo, "IN", "commitTs_in", "TIMESTAMP", True, "marks the execution timestamp of the LRT")
 ' ### IF IVK ###
       genProcParm(fileNo, "IN", "autoPriceSetProductive_in", g_dbtBoolean, True, "specifies whether prices are set productive")
       genProcParm(fileNo, "IN", "settingManActCP_in", g_dbtBoolean, True, "setting 'manuallyActivateCodePrice'")
       genProcParm(fileNo, "IN", "settingManActTP_in", g_dbtBoolean, True, "setting 'manuallyActivateTypePrice'")
       genProcParm(fileNo, "IN", "settingManActSE_in", g_dbtBoolean, True, "setting 'manuallyActivateStandardEquipmentPrice'")
       genProcParm(fileNo, "IN", "settingSelRelease_in", g_dbtBoolean, True, "setting 'useSelectiveReleaseProcess'")

       If Not isPrimaryOrg Then
         genProcParm(fileNo, "IN", "isFtoLrt_in", g_dbtBoolean, True, "'1' if and only if this LRT 'is central data transfer'")
       End If
 ' ### ENDIF IVK ###

       genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by this commit")
       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 0"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

 ' ### IF IVK ###
       If Not condenseData Then
         genProcSectionHeader(fileNo, "declare conditions", , True)
         genCondDecl(fileNo, "notFound", "02000")
         genCondDecl(fileNo, "alreadyExist", "42710")
       End If

 ' ### ENDIF IVK ###
       genProcSectionHeader(fileNo, "declare variables")

       If busKeyAttrList <> "" Then
         genSigMsgVarDecl(fileNo)
       End If
       If busKeyAttrListNoFks <> "" Then
         genVarDecl(fileNo, "v_busKeyValues", "VARCHAR(200)", "NULL")
 ' ### IFNOT IVK ###
 '       Dim i As Integer
 ' ### ENDIF IVK ###
         For i = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
           genVarDecl(fileNo, "v_" & busKeyAttrArrayNoFks(i), "VARCHAR(40)", "NULL")
         Next i
       End If
 ' ### IF IVK ###
       If Not condenseData Then
         If maintainGroupIdColumnsInLrtCommit And hasGroupIdAttrs Then
           genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL")
         Else
           genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL")
         End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 '       genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
 ' ### ENDIF IVK ###
        genVarDecl(fileNo, "v_rowCountCLog", "BIGINT", "0")
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
       genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
 
 ' ### IF IVK ###
       If maintainGroupIdColumnsInLrtCommit And hasGroupIdAttrs Then
         Dim gidColShortName  As String
         Dim k As Integer
         For k = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
             gidColShortName = g_attributes.descriptors(groupIdAttrIndexes(k)).shortName
             genVarDecl(fileNo, "v_" & UCase(gidColShortName), "BIGINT", "NULL")
         Next k
       End If

 ' ### ENDIF IVK ###
       genSpLogDecl(fileNo)

       genProcSectionHeader(fileNo, "declare statement")
       genVarDecl(fileNo, "v_stmnt", "STATEMENT")

 ' ### IF IVK ###
       If Not condenseData Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNo, "declare condition handler")
         Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
         Print #fileNo, addTab(1); "BEGIN"
         Print #fileNo, addTab(2); "-- just ignore"
         Print #fileNo, addTab(1); "END;"
         Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
         Print #fileNo, addTab(1); "BEGIN"
         Print #fileNo, addTab(2); "-- just ignore"
         Print #fileNo, addTab(1); "END;"

         genDdlForTempTablesChangeLog(fileNo, thisOrgIndex, thisPoolIndex, ddlType, 1, True)

         genProcSectionHeader(fileNo, "temporary tables for Public OIDs affected")
         Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
         Print #fileNo, addTab(2); pc_tempTabNamePubOidsAffected
         Print #fileNo, addTab(1); "("
         Print #fileNo, addTab(2); "oid                  "; g_dbtOid; ","
         If Not hasOwnTable Then
           Print #fileNo, addTab(2); "classId              "; g_dbtEntityId; ","
         End If
 ' ### IF IVK ###
         Print #fileNo, addTab(2); "statusId             "; g_dbtEnumId; ","
         If isGenericAspectHead Then
           Print #fileNo, addTab(2); "privStatusId         "; g_dbtEnumId; ","
         End If
         Print #fileNo, addTab(2); "isDeleted            "; g_dbtBoolean; ","
         Print #fileNo, addTab(2); "hasBeenSetProductive "; g_dbtBoolean
 ' ### ELSE IVK ###
 '       Print #fileNo, addTab(2); "isDeleted            "; g_dbtBoolean
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ")"
         genDdlForTempTableDeclTrailer(fileNo, 1, True)
 
         If hasNlAttributes Then
           Print #fileNo,
           Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
           Print #fileNo, addTab(2); pc_tempTabNamePubOidsAffectedNl
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "oid                  "; g_dbtOid; ","
           Print #fileNo, addTab(2); "parOid               "; g_dbtOid; ","
 ' ### IF IVK ###
           Print #fileNo, addTab(2); "isDeleted            "; g_dbtBoolean; ","
           Print #fileNo, addTab(2); "hasBeenSetProductive "; g_dbtBoolean
 ' ### ELSE IVK ###
 '         Print #fileNo, addTab(2); "isDeleted            "; g_dbtBoolean
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(1); ")"
           genDdlForTempTableDeclTrailer(fileNo, 1, True)
         End If
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###

 ' ### IF IVK ###
       If Not isPrimaryOrg Then
         genSpLogProcEnter(fileNo, _
           qualProcNameLrtCommit, ddlType, , "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", _
           "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out")
       Else
         genSpLogProcEnter(fileNo, _
           qualProcNameLrtCommit, ddlType, , "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", _
           "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out")
       End If
 ' ### ELSE IVK ###
 '     genSpLogProcEnter fileNo, qualProcNameLrtCommit, ddlType, , "lrtOid_in", "'cdUserId_in", "lrtStatus_in", "#commitTs_in", "rowCount_out"
 ' ### ENDIF IVK ###

       genProcSectionHeader(fileNo, "initialize output parameter 'rowCount_out'", 1)
       Print #fileNo, addTab(1); "SET rowCount_out = 0;"

 ' ### IF IVK ###
       If hasPriceAssignmentSubClass Then
         genProcSectionHeader(fileNo, "take care of prices being set productive automatically (Preis-Durchschuss)")
         Print #fileNo, addTab(1); "IF ( lrtStatus_in <> "; CStr(lrtStatusLocked); " ) AND ( autoPriceSetProductive_in = 1 ) THEN"

         Print #fileNo, addTab(2); "INSERT INTO"
         Print #fileNo, addTab(3); g_qualTabNameRegistryDynamic
         Print #fileNo, addTab(2); "("
         Print #fileNo, addTab(3); g_anOid; ","
         Print #fileNo, addTab(3); g_anSection; ","
         Print #fileNo, addTab(3); g_anKey; ","
         Print #fileNo, addTab(3); g_anSubKey; ","
         Print #fileNo, addTab(3); g_anValue
         Print #fileNo, addTab(2); ")"
         Print #fileNo, addTab(2); "SELECT"
         Print #fileNo, addTab(3); "NEXTVAL FOR "; qualSeqNameOid; ","
         Print #fileNo, addTab(3); "'"; gc_regDynamicSectionAutoSetProd; "',"
         Print #fileNo, addTab(3); "'"; gc_regDynamicKeyAutoSetProd; "',"
         Print #fileNo, addTab(3); "'"; Right("00" & genOrgId(thisOrgIndex, ddlType, True), 2); "-' || RTRIM(CAST(lrtOid_in AS CHAR(40))),"

         Print #fileNo, addTab(3); "RTRIM(CAST(PRIV."; g_anOid; " AS CHAR(40)))"

         Print #fileNo, addTab(2); "FROM"
         Print #fileNo, addTab(3); qualTabNamePriv; " PRIV"
         Print #fileNo, addTab(2); "WHERE"
         Print #fileNo, addTab(3); "PRIV."; g_anCid; " IN ("; priceAssignmentSubClassIdList; ")"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "PRIV."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "PRIV."; g_anLrtState; " = lrtStatus_in"
         Print #fileNo, addTab(2); "WITH UR;"

         Print #fileNo, addTab(1); "END IF;"
       End If

       If Not condenseData Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNo, "determine Public OIDs of affected entities")
         Print #fileNo, addTab(1); "IF ( lrtStatus_in IN ("; CStr(lrtStatusLocked); ", "; CStr(lrtStatusUpdated); ", "; CStr(lrtStatusDeleted); ")) THEN"

         Print #fileNo, addTab(2); "INSERT INTO"
         Print #fileNo, addTab(3); pc_tempTabNamePubOidsAffected
         Print #fileNo, addTab(2); "("
 ' ### IF IVK ###
         Print #fileNo, addTab(3); "oid,"
         If Not hasOwnTable Then
           Print #fileNo, addTab(3); "classId,"
         End If
         Print #fileNo, addTab(3); "statusId,"
         If isGenericAspectHead Then
           Print #fileNo, addTab(3); "privStatusId,"
         End If
         Print #fileNo, addTab(3); "isDeleted,"
         Print #fileNo, addTab(3); "hasBeenSetProductive"
 ' ### ELSE IVK ###
 '       Print #fileNo, addTab(3); "oid"; IIf(hasOwnTable, "", ",")
 '       If Not hasOwnTable Then
 '         Print #fileNo, addTab(3); "classId"
 '       End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(2); ")"
         Print #fileNo, addTab(2); "SELECT"
 ' ### IF IVK ###
         Print #fileNo, addTab(3); "PUB."; g_anOid; ","
         If Not hasOwnTable Then
           Print #fileNo, addTab(3); "PUB."; g_anCid; ","
         End If
         Print #fileNo, addTab(3); "PUB."; g_anStatus; ","
         If isGenericAspectHead Then
           Print #fileNo, addTab(3); "PRIV."; g_anStatus; ","
         End If
         Print #fileNo, addTab(3); "PUB."; g_anIsDeleted; ","
         Print #fileNo, addTab(3); "PUB."; g_anHasBeenSetProductive
 ' ### ELSE IVK ###
 '       Print #fileNo, addTab(3); "PUB."; g_anOid; IIf(hasOwnTable, "", ",")
 '       If Not hasOwnTable Then
 '         Print #fileNo, addTab(3); "PUB."; g_anCid
 '       End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(2); "FROM"
         Print #fileNo, addTab(3); qualTabNamePub; " PUB"
         Print #fileNo, addTab(2); "INNER JOIN"
         Print #fileNo, addTab(3); qualTabNamePriv; " PRIV"
         Print #fileNo, addTab(2); "ON"
         Print #fileNo, addTab(3); "PUB."; g_anOid; " = PRIV."; g_anOid
         Print #fileNo, addTab(2); "WHERE"
         Print #fileNo, addTab(3); "PRIV."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "((lrtStatus_in = "; CStr(lrtStatusLocked); ") OR (PRIV."; g_anLrtState; " = lrtStatus_in))"

 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "PRIV."; g_anPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(2); "WITH UR;"

         If hasNlAttributes Then
           Print #fileNo,
           Print #fileNo, addTab(2); "INSERT INTO"
           Print #fileNo, addTab(3); pc_tempTabNamePubOidsAffectedNl
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "oid,"
 ' ### IF IVK ###
           Print #fileNo, addTab(3); "parOid,"
           Print #fileNo, addTab(3); "isDeleted,"
           Print #fileNo, addTab(3); "hasBeenSetProductive"
 ' ### ELSE IVK ###
 '         Print #fileNo, addTab(3); "parOid"
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(2); ")"
           Print #fileNo, addTab(2); "SELECT"
           Print #fileNo, addTab(3); "PUB."; g_anOid; ","
 ' ### IF IVK ###
           Print #fileNo, addTab(3); "PUB."; genSurrogateKeyName(ddlType, entityShortName); ","
           Print #fileNo, addTab(3); "PUB."; g_anIsDeleted; ","
           Print #fileNo, addTab(3); "PUB."; g_anHasBeenSetProductive
 ' ### ELSE IVK ###
 '         Print #fileNo, addTab(3); "PUB."; genSurrogateKeyName(ddlType, entityShortName)
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); qualTabNamePubNl; " PUB"
           Print #fileNo, addTab(2); "INNER JOIN"
           Print #fileNo, addTab(3); qualTabNamePrivNl; " PRIV"
           Print #fileNo, addTab(2); "ON"
           Print #fileNo, addTab(3); "PUB."; g_anOid; " = PRIV."; g_anOid
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "PRIV."; g_anInLrt; " = lrtOid_in"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "((lrtStatus_in = "; CStr(lrtStatusLocked); ") OR (PRIV."; g_anLrtState; " = lrtStatus_in))"

 ' ### IF IVK ###
           If isPsTagged And usePsTagInNlTextTables Then
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "PRIV."; g_anPsOid; " = psOid_in"
           End If

 ' ### ENDIF IVK ###
           Print #fileNo, addTab(2); "WITH UR;"
         End If

         Print #fileNo, addTab(1); "END IF;"
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###

       Dim multipleTableSuffix As String
       multipleTableSuffix = IIf(hasNlTable, "s", "")

       genProcSectionHeader(fileNo, "propagate changes to public tables")
       Print #fileNo, addTab(1); "IF ( lrtStatus_in = "; CStr(lrtStatusLocked); " ) THEN"

 ' ### IF IVK ###
       If Not condenseData Then
         genProcSectionHeader(fileNo, "delete all rows in public table" & multipleTableSuffix & " related to this LRT marked as deleted and not being set productive", 2, True)
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         If hasNlAttributes Then
           Print #fileNo, addTab(2); "DELETE FROM "; qualTabNamePubNl; " AS PUBNL"
           Print #fileNo, addTab(2); "WHERE"
           If isPsTagged Then
             Print #fileNo, addTab(3); "PUBNL."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
           End If
           Print #fileNo, addTab(3); "EXISTS ("
           Print #fileNo, addTab(4); "SELECT"
           Print #fileNo, addTab(5); "1"
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffectedNl; " AS ses"
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "PUBNL."; g_anOid; " = ses.oid"
           Print #fileNo, addTab(6); "AND"
           Print #fileNo, addTab(5); "ses.isDeleted = "; gc_dbTrue
           Print #fileNo, addTab(6); "AND"
           Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbFalse
           Print #fileNo, addTab(3); ")"
           Print #fileNo, addTab(2); ";"
           Print #fileNo,
         End If

         Print #fileNo, addTab(2); "DELETE FROM "; qualTabNamePub; " AS PUB"
         Print #fileNo, addTab(2); "WHERE"
         If isPsTagged Then
           Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(4); "AND"
         End If
         Print #fileNo, addTab(3); "EXISTS ("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "1"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffected; " AS ses"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PUB."; g_anOid; " = ses.oid"
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "ses.isDeleted = "; gc_dbTrue
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbFalse
         Print #fileNo, addTab(3); ")"
         Print #fileNo, addTab(2); ";"

         genProcSectionHeader(fileNo, "unlock ALL rows in public table" & multipleTableSuffix & " related to this LRT", 2)
         If hasNlAttributes Then
           Print #fileNo, addTab(2); "UPDATE "; qualTabNamePubNl; " AS PUBNL"
           Print #fileNo, addTab(2); "SET PUBNL.INLRT = CAST(NULL AS BIGINT)"
           Print #fileNo, addTab(2); "WHERE"
           If isPsTagged Then
             Print #fileNo, addTab(3); "PUBNL."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
           End If
           Print #fileNo, addTab(3); "EXISTS ("
           Print #fileNo, addTab(4); "SELECT"
           Print #fileNo, addTab(5); "1"
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffectedNl; " AS ses"
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "PUBNL."; g_anOid; " = ses.oid"
           Print #fileNo, addTab(3); ")"
           Print #fileNo, addTab(2); ";"
           Print #fileNo,
         End If
         Print #fileNo, addTab(2); "UPDATE "; qualTabNamePub; " AS PUB"
         Print #fileNo, addTab(2); "SET PUB.INLRT = CAST(NULL AS BIGINT)"
         Print #fileNo, addTab(2); "WHERE"
         If isPsTagged Then
           Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(4); "AND"
         End If
         Print #fileNo, addTab(3); "EXISTS ("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "1"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffected; " AS ses"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PUB."; g_anOid; " = ses.oid"
         Print #fileNo, addTab(3); ")"
         Print #fileNo, addTab(2); ";"
 ' ### IF IVK ###
       End If

       genProcSectionHeader(fileNo, "cleanup private table" & multipleTableSuffix, 2, condenseData)
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 '
 '     genProcSectionHeader fileNo, "cleanup private table" & multipleTableSuffix, 2
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(2); "DELETE FROM"
       Print #fileNo, addTab(3); qualTabNamePriv; " PRIV"
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "PRIV."; g_anInLrt; " = lrtOid_in"
       'CQDAT00027607: additional CodeCategory entries for foreign product structures must be removed
       If isPsTagged And Not (isPrimaryOrg And acmEntityIndex = g_relIndexCodeCategory And acmEntityType = eactRelationship) Then
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "PRIV."; g_anPsOid; " = psOid_in"
       End If
       Print #fileNo, addTab(2); "WITH UR;"

       If hasNlTable Then
         Print #fileNo,
         Print #fileNo, addTab(2); "DELETE FROM"
         Print #fileNo, addTab(3); qualTabNamePrivNl; " PRIV"
         Print #fileNo, addTab(2); "WHERE"
         Print #fileNo, addTab(3); "PRIV."; g_anInLrt; " = lrtOid_in"
         If isPsTagged Then
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "PRIV."; g_anPsOid; " = psOid_in"
         End If
         Print #fileNo, addTab(2); "WITH UR;"
       End If

       Print #fileNo, addTab(1); "ELSEIF ( lrtStatus_in = "; CStr(lrtStatusCreated); " ) THEN"

 ' ### IF IVK ###
       Dim gidTabVar As String
 ' ### ENDIF IVK ###
       Dim crTabVar As String
       Dim sourceTabVar As String
       Dim mgidColName As String
       Dim subClassIdStrList As String
 ' ### IF IVK ###
       Dim qualSeqNameGroupId As String
       Dim expGroupIdColNo As Integer
       If maintainGroupIdColumnsInLrtCommit And hasGroupIdAttrs Then
         Dim maxVarNameLength As Integer
         ' Fixme: get rid of this hard-coding
         maxVarNameLength = 22

         For k = LBound(groupIdAttrIndexes) To UBound(groupIdAttrIndexes)
             genProcSectionHeader(fileNo, "update group-ID column """ & UCase(g_attributes.descriptors(groupIdAttrIndexes(k)).attributeName) & """ in table """ & qualTabNamePriv & """", 2, True)

             gidTabVar = UCase(g_attributes.descriptors(groupIdAttrIndexes(k)).shortName)
             crTabVar = "CR"
             sourceTabVar = "PRIV" ' UCase(entityShortName)
             Dim gidColName As String
             gidColName = genAttrName(g_attributes.descriptors(groupIdAttrIndexes(k)).attributeName, ddlType)
             gidColShortName = g_attributes.descriptors(groupIdAttrIndexes(k)).shortName
 
             subClassIdStrList = g_classes.descriptors(g_attributes.descriptors(groupIdAttrIndexes(k)).acmEntityIndex).subclassIdStrListNonAbstract
             qualSeqNameGroupId = _
               genQualObjName( _
                 sectionIndex, "SEQ_" & entityShortName & g_attributes.descriptors(groupIdAttrIndexes(k)).shortName, "SEQ_" & entityShortName & g_attributes.descriptors(groupIdAttrIndexes(k)).shortName, ddlType, thisOrgIndex _
               )
             Print #fileNo, addTab(2); "FOR gidLoop AS gidCursor CURSOR WITH HOLD FOR"

             Print #fileNo, addTab(3); "SELECT"

             If isPsTagged Then
               Print #fileNo, addTab(4); paddRight(crTabVar & "." & g_anPsOid, maxVarNameLength); " AS v_"; g_anPsOid; ", "
             End If

             expGroupIdColNo = 0
             Dim l As Integer
             For l = LBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes) To UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes)
               If Left(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l), 1) = "#" Then
                 expGroupIdColNo = expGroupIdColNo + 1
                 Print #fileNo, addTab(4); paddRight(crTabVar & ".EXP_" & CStr(expGroupIdColNo), maxVarNameLength); " AS v_EXP_"; CStr(expGroupIdColNo); IIf(l < UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes), ",", "")
               Else
                 Print #fileNo, addTab(4); paddRight(crTabVar & "." & UCase(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l)), maxVarNameLength); " AS v_"; UCase(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l)); IIf(l < UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes), ",", "")
               End If
             Next l

             Print #fileNo, addTab(3); "FROM"
             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); "SELECT DISTINCT"

             If isPsTagged Then
               Print #fileNo, addTab(6); sourceTabVar; "."; g_anPsOid; ","
             End If

             expGroupIdColNo = 0
             For l = LBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes) To UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes)
               If Left(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l), 1) = "#" Then
                 expGroupIdColNo = expGroupIdColNo + 1
                 Print #fileNo, addTab(6); mapExpression(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l), thisOrgIndex, thisPoolIndex, ddlType, sourceTabVar, , sourceTabVar & "." & g_anInLrt); " AS EXP_"; CStr(expGroupIdColNo); IIf(l < UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes), ",", "")
               Else
                 Print #fileNo, addTab(6); sourceTabVar; "."; UCase(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l)); IIf(l < UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes), ",", "")
               End If
             Next l

             Print #fileNo, addTab(5); "FROM"
             Print #fileNo, addTab(6); qualTabNamePriv; " "; sourceTabVar
             Print #fileNo, addTab(5); "WHERE"
             Print #fileNo, addTab(6); "("; sourceTabVar; "."; g_anCid; " IN ("; subClassIdStrList; "))"
             Print #fileNo, addTab(7); "AND"
             Print #fileNo, addTab(6); "("; sourceTabVar; "."; g_anInLrt; " = lrtOid_in)"
             Print #fileNo, addTab(7); "AND"
             Print #fileNo, addTab(6); "("; sourceTabVar; "."; g_anLrtState; " = "; CStr(lrtStatusCreated); ")"

             Print #fileNo, addTab(4); ") AS "; crTabVar
             Print #fileNo, addTab(3); "FOR READ ONLY"
             Print #fileNo, addTab(2); "WITH UR"

             Print #fileNo, addTab(2); "DO"

             Print #fileNo, addTab(3); "SET v_"; UCase(gidColShortName); " = ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); sourceTabVar; "."; gidColName
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); qualTabNamePriv; " "; sourceTabVar
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "("; sourceTabVar; "."; gidColName; " IS NOT NULL) AND"
             Print #fileNo, addTab(5); "("; sourceTabVar; "."; g_anInLrt; " = lrtOid_in) AND"
             Print #fileNo, addTab(5); "("; sourceTabVar; "."; g_anCid; " IN ("; subClassIdStrList; ")) AND"

             ' Fixme: get rid of this hard-coding
             maxVarNameLength = 24

             If isPsTagged Then
               Print #fileNo, addTab(5); "("; sourceTabVar; "."; g_anPsOid; " = v_"; g_anPsOid; ") AND"
             End If

             expGroupIdColNo = 0
             For l = LBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes) To UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes)
               Dim v1 As String
               Dim v2 As String
               If Left(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l), 1) = "#" Then
                 expGroupIdColNo = expGroupIdColNo + 1
                 v1 = mapExpression(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l), thisOrgIndex, thisPoolIndex, ddlType, sourceTabVar, , sourceTabVar & "." & g_anInLrt)
                 v2 = "v_EXP" & "_" & CStr(expGroupIdColNo)
               Else
                 v1 = paddRight(sourceTabVar & "." & UCase(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l)), maxVarNameLength)
                 v2 = paddRight("v_" & UCase(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l)), maxVarNameLength)
               End If

               Print #fileNo, addTab(5); "((("; v1; " IS NULL) AND ("; v2; " IS NULL)) OR ("; v1; " = "; v2; "))"; IIf(l < UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes), " AND", "")
             Next l

             Print #fileNo, addTab(4); "FETCH FIRST 1 ROW ONLY"
             Print #fileNo, addTab(3); ");"
 
             Print #fileNo,
             Print #fileNo, addTab(3); "IF v_"; UCase(gidColShortName); " IS NULL THEN"
             Print #fileNo, addTab(4); "SET v_"; UCase(gidColShortName); " = NEXTVAL FOR "; qualSeqNameGroupId; ";"
             Print #fileNo, addTab(3); "END IF;"
             Print #fileNo,
             Print #fileNo, addTab(3); "SET v_stmntTxt = 'UPDATE "; qualTabNamePriv; " "; UCase(entityShortName); " SET "; UCase(entityShortName); "."; gidColName; " = ' ||"
             Print #fileNo, addTab(3); "                 RTRIM(CHAR(v_"; UCase(gidColShortName); ")) || ' WHERE ' ||"

             Print #fileNo, addTab(3); "                 '("; UCase(entityShortName); "."; gidColName; " IS NULL) AND ' ||"

             Print #fileNo, addTab(3); "                 '("; UCase(entityShortName); "."; g_anCid; " IN ("; Replace(subClassIdStrList, "'", "''"); ")) AND ' ||"
 
             Print #fileNo, addTab(3); "                 '("; entityShortName; "."; g_anInLrt; " = "; "' || RTRIM(CHAR(lrtOid_in)) || ') AND ' ||"

             If isPsTagged Then
               Print #fileNo, addTab(3); "                 '("; entityShortName; "."; g_anPsOid; " = "; "' || RTRIM(CHAR(v_"; g_anPsOid; ")) || ') AND ' ||"
             End If

             tabColumns = nullEntityColumnDescriptors
             initAttributeTransformation(transformation, 0, , True, , , , , , , , , , , , True, True)
             genTransformedAttrListForEntityWithColReuse(acmEntityIndex, eactClass, transformation, tabColumns, fileNo, ddlType, , , , , , edomNone)
 
             expGroupIdColNo = 0
             For l = LBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes) To UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes)
               Dim thisColumn As String
               thisColumn = UCase(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l))
               Dim colCastPrefix As String
               Dim colCastPostfix As String
               colCastPrefix = ""
               colCastPostfix = ""
               Dim thisDomainIndex As Integer
               Dim thisDbType As typeId

               thisDbType = etNone
               If thisColumn = g_anValidFrom Or thisColumn = g_anValidTo Then
                 thisDbType = etDate
               Else
                 Dim m As Integer
                 For m = 1 To tabColumns.numDescriptors
                     If tabColumns.descriptors(m).columnName = thisColumn Then
                       thisDbType = g_domains.descriptors(tabColumns.descriptors(m).dbDomainIndex).dataType
                       Exit For
                     End If
                 Next m
               End If

               If (thisDbType = etChar Or thisDbType = etClob Or thisDbType = etLongVarchar Or thisDbType = etVarchar) Then
                 colCastPrefix = "''"
                 colCastPostfix = "''"
               ElseIf thisDbType = etDate Then
                 colCastPrefix = "DATE(''"
                 colCastPostfix = "'')"
               ElseIf thisDbType = etTime Then
                 colCastPrefix = "TIME(''"
                 colCastPostfix = "'')"
               ElseIf thisDbType = etTimestamp Then
                 colCastPrefix = "TIMESTAMP(''"
                 colCastPostfix = "'')"
               End If

               If Left(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes(l), 1) = "#" Then
                 Dim thisColumnExpr As String
                 Dim refColumnExpr As String
                 expGroupIdColNo = expGroupIdColNo + 1

                 thisColumnExpr = "v_EXP_" & CStr(expGroupIdColNo)
                 refColumnExpr = mapExpression(thisColumn, thisOrgIndex, thisPoolIndex, ddlType, entityShortName, , entityShortName & "." & g_anInLrt)
                 Print #fileNo, addTab(11); " (CASE WHEN "; thisColumnExpr; " IS NULL THEN '("; refColumnExpr; " IS NULL)' "; _
                                            "ELSE '("; refColumnExpr; " = "; colCastPrefix; "' || RTRIM(REPLACE(CHAR("; thisColumnExpr; "),'''',''''''))"; IIf(colCastPostfix = "", "", " || '" & colCastPostfix & "'"); " || ')'"; _
                                            " END)"; IIf(l < UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes), " || ' AND ' ||", ";")
               Else
                 Print #fileNo, addTab(11); " (CASE WHEN v_"; thisColumn; " IS NULL THEN '("; entityShortName; "."; thisColumn; " IS NULL)' "; _
                                            "ELSE '("; entityShortName; "."; thisColumn; " = "; colCastPrefix; "' || RTRIM(REPLACE(CHAR(v_"; thisColumn; "),'''',''''''))"; IIf(colCastPostfix = "", "", " || '" & colCastPostfix & "'"); " || ')'"; _
                                            " END)"; IIf(l < UBound(g_attributes.descriptors(groupIdAttrIndexes(k)).groupIdAttributes), " || ' AND ' ||", ";")
               End If
             Next l

             Print #fileNo,
             Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
             Print #fileNo, addTab(2); "END FOR;"
         Next k
         Print #fileNo,
       End If

 ' ### ENDIF IVK ###
       genProcSectionHeader(fileNo, "CREATE: move all LRT-private 'new' records into public tables (INSERT)", 2, True)

       If busKeyAttrList <> "" Then
         genProcSectionHeader(fileNo, "verify that there is no conflict with some public record with respect to business key", 2)
         genAttrList(busKeyAttrArray, busKeyAttrList)

         Print #fileNo, addTab(2); "IF EXISTS"
         Print #fileNo, addTab(3); "("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "1"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); qualTabNamePub; " PUB,"
         Print #fileNo, addTab(5); qualTabNamePriv; " PRIV"
         Print #fileNo, addTab(4); "WHERE"
 ' ### IF IVK ###
         If Not condenseData Then
           Print #fileNo, addTab(5); "PUB."; g_anIsDeleted; " = "; gc_dbFalse
           Print #fileNo, addTab(6); "AND"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(5); "PRIV."; UCase(g_anLrtState); " = "; CStr(lrtStatusCreated)
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "PRIV."; g_anInLrt; " = lrtOid_in"

         For i = LBound(busKeyAttrArray) To UBound(busKeyAttrArray)
           Print #fileNo, addTab(6); "AND"
           Print #fileNo, addTab(5); "PUB."; UCase(busKeyAttrArray(i)); " = PRIV."; UCase(busKeyAttrArray(i))
         Next i
 
         Print #fileNo, addTab(3); ") THEN"

         If acmEntityType = eactClass And busKeyAttrListNoFks <> "" Then
           genProcSectionHeader(fileNo, "determine non-FK values violating business key", 4, True)
           Print #fileNo, addTab(4); "SELECT"
           For i = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
             Print #fileNo, addTab(5); "CAST(RTRIM(CAST("; UCase(busKeyAttrArrayNoFks(i)); " AS CHAR(40))) AS VARCHAR(40))"; IIf(i < UBound(busKeyAttrArrayNoFks), ",", "")
           Next i
           Print #fileNo, addTab(4); "INTO"
           For i = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
             Print #fileNo, addTab(5); "v_"; busKeyAttrArrayNoFks(i); IIf(i < UBound(busKeyAttrArrayNoFks), ",", "")
           Next i
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); qualTabNamePriv; " PRIV"
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "EXISTS ("
           Print #fileNo, addTab(6); "SELECT"
           Print #fileNo, addTab(7); "1"
           Print #fileNo, addTab(6); "FROM"
           Print #fileNo, addTab(7); qualTabNamePub; " PUB"
           Print #fileNo, addTab(6); "WHERE"
 ' ### IF IVK ###
           If Not condenseData Then
             Print #fileNo, addTab(7); "PUB."; g_anIsDeleted; " = "; gc_dbFalse
             Print #fileNo, addTab(8); "AND"
           End If
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(7); "PRIV."; UCase(g_anLrtState); " = "; CStr(lrtStatusCreated)
           Print #fileNo, addTab(8); "AND"
           Print #fileNo, addTab(7); "PRIV."; g_anInLrt; " = lrtOid_in"

           For i = LBound(busKeyAttrArray) To UBound(busKeyAttrArray)
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PUB."; UCase(busKeyAttrArray(i)); " = PRIV."; UCase(busKeyAttrArray(i))
           Next i
           Print #fileNo, addTab(5); ")"
           Print #fileNo, addTab(4); "FETCH FIRST 1 ROW ONLY;"
 
           genProcSectionHeader(fileNo, "concatenate business key values for error message", 4)
           Print #fileNo, addTab(4); "SET v_busKeyValues ="
           For i = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
             Print #fileNo, addTab(6); "'"; busKeyAttrArrayNoFks(i); "=' || v_"; busKeyAttrArrayNoFks(i); IIf(i < UBound(busKeyAttrArrayNoFks), " || ',' ||", "")
           Next i
           Print #fileNo, addTab(4); ";"
 
 ' ### IF IVK ###
           If Not isPrimaryOrg Then
             genSpLogProcEscape(fileNo, _
               qualProcNameLrtCommit, ddlType, 4, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", _
               "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out")
           Else
             genSpLogProcEscape(fileNo, _
               qualProcNameLrtCommit, ddlType, 4, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", _
               "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out")
           End If
 ' ### ELSE IVK ###
 '         genSpLogProcEscape fileNo, qualProcNameLrtCommit, ddlType, 4, "lrtOid_in", "'cdUserId_in", "lrtStatus_in", "#commitTs_in", "rowCount_out"
 ' ### ENDIF IVK ###
           genProcSectionHeader(fileNo, "signal eror message", 4)
           genSignalDdlWithParms("lrtCommitBusKeyViolation", fileNo, 4, getPrimaryClassLabelByIndex(acmEntityIndex), , , , , , , , , "COALESCE(v_busKeyValues,'<->')")
         Else
 ' ### IF IVK ###
           If Not isPrimaryOrg Then
             genSpLogProcEscape(fileNo, _
               qualProcNameLrtCommit, ddlType, -4, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", _
               "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out")
           Else
             genSpLogProcEscape(fileNo, _
               qualProcNameLrtCommit, ddlType, -4, "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", _
               "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out")
           End If
 ' ### ELSE IVK ###
 '         genSpLogProcEscape fileNo, qualProcNameLrtCommit, ddlType, 4, "lrtOid_in", "'cdUserId_in", "lrtStatus_in", "#commitTs_in", "rowCount_out"
 ' ### ENDIF IVK ###
           genProcSectionHeader(fileNo, "signal eror message", 4)
           genSignalDdlWithParms("lrtCommitBusKeyViolation", fileNo, 4, getUnqualObjName(qualTabNamePub), busKeyAttrList, , , , , , , , "'" & busKeyAttrList & "'")
         End If

         Print #fileNo, addTab(2); "END IF;"
         Print #fileNo,
       End If

       Print #fileNo, addTab(2); "INSERT INTO"
       Print #fileNo, addTab(3); qualTabNamePub
       Print #fileNo, addTab(2); "("

       genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt)
 
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "SELECT"

 ' ### IF IVK ###
       initAttributeTransformation(transformation, 8, , , , "PRIV.")
 ' ### ELSE IVK ###
 '     initAttributeTransformation transformation, 5, , , , "PRIV."
 ' ### ENDIF IVK ###

       setAttributeMapping(transformation, 1, conInLrt, "CAST(NULL AS " & g_dbtOid & ")")
       setAttributeMapping(transformation, 2, conCreateTimestamp, "commitTs_in")
       setAttributeMapping(transformation, 3, conLastUpdateTimestamp, "commitTs_in")

 ' ### IF IVK ###
       setAttributeMapping(transformation, 4, conIsDeleted, gc_dbFalse)
       If isPrimaryOrg Then
         setAttributeMapping(transformation, 5, conCreateUser, "cdUserId_in")
         setAttributeMapping(transformation, 6, conUpdateUser, "cdUserId_in")
       Else
         setAttributeMapping(transformation, 5, conCreateUser, "(CASE isFtoLrt_in WHEN 1 THEN PRIV." & g_anCreateUser & " ELSE cdUserId_in END)")
         setAttributeMapping(transformation, 6, conUpdateUser, "(CASE isFtoLrt_in WHEN 1 THEN PRIV." & g_anUpdateUser & " ELSE cdUserId_in END)")
       End If

       setAttributeMapping(transformation, 7, conHasBeenSetProductive, gc_dbFalse)

       If aggHeadClassIndex > 0 Then
         tmpClassId = "PRIV." & g_anAhCid
       Else
         tmpClassId = "'" & g_classes.descriptors(acmEntityIndex).classIdStr & "'"
       End If

       setAttributeMapping(transformation, 8, conStatusId, _
         IIf(hasPriceAssignmentSubClass, "CASE WHEN (autoPriceSetProductive_in = 1) AND (PRIV." & g_anCid & " IN (" & priceAssignmentSubClassIdList & ")) THEN " & statusReadyToBeSetProductive & " ELSE ", "") & _
         g_qualFuncNameGetLrtTargetStatus & "(" & _
         tmpClassId & "," & _
         "CAST('" & gc_acmEntityTypeKeyClass & "' AS " & g_dbtEntityType & ")," & _
         IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActCP_in END)", "settingManActCP_in") & "," & _
         IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActTP_in END)", "settingManActTP_in") & "," & _
         IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActSE_in END)", "settingManActSE_in") & "," & _
         "settingSelRelease_in" & _
         ")" & _
         IIf(hasPriceAssignmentSubClass, " END", ""))
 ' ### ELSE IVK ###
 '     setAttributeMapping transformation, 4, conCreateUser, "cdUserId_in"
 '     setAttributeMapping transformation, 5, conUpdateUser, "cdUserId_in"
 ' ### ENDIF IVK ###

       genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, , False, forGen, edomListNonLrt)

       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); qualTabNamePriv; " PRIV"
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); "PRIV."; g_anInLrt; " = lrtOid_in"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "PRIV."; g_anLrtState; " = "; CStr(lrtStatusCreated)
       Print #fileNo, addTab(2); "WITH UR;"

       genProcSectionHeader(fileNo, "count the number of affected rows", 2)
       Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
       Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

       If hasNlAttributes Then
         genProcSectionHeader(fileNo, "insert records into NL-table", 2)
         Print #fileNo, addTab(2); "INSERT INTO"
         Print #fileNo, addTab(3); qualTabNamePubNl
         Print #fileNo, addTab(2); "("

         genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt)

         Print #fileNo, addTab(2); ")"
         Print #fileNo, addTab(2); "SELECT"

 ' ### IF IVK ###
         initAttributeTransformation(transformation, 4, , , , "PRIV.")
 ' ### ELSE IVK ###
 '       initAttributeTransformation transformation, 1, , , , "PRIV."
 ' ### ENDIF IVK ###

         setAttributeMapping(transformation, 1, conInLrt, "CAST(NULL AS " & g_dbtOid & ")")
 ' ### IF IVK ###
         setAttributeMapping(transformation, 2, conIsDeleted, gc_dbFalse)
         setAttributeMapping(transformation, 3, conHasBeenSetProductive, gc_dbFalse)

         If aggHeadClassIndex > 0 Then
           tmpClassId = "PRIV." & g_anAhCid
         Else
           tmpClassId = "'" & g_classes.descriptors(acmEntityIndex).classIdStr & "'"
         End If

         setAttributeMapping(transformation, 4, conStatusId, _
           IIf(hasPriceAssignmentSubClass, "CASE WHEN (autoPriceSetProductive_in = 1) AND (PAR." & g_anCid & " IN (" & priceAssignmentSubClassIdList & ")) THEN " & statusReadyToBeSetProductive & " ELSE ", "") & _
           g_qualFuncNameGetLrtTargetStatus & "(" & _
           tmpClassId & "," & _
           "CAST('" & gc_acmEntityTypeKeyClass & "' AS " & g_dbtEntityType & ")," & _
           IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActCP_in END)", "settingManActCP_in") & "," & _
           IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActTP_in END)", "settingManActTP_in") & "," & _
           IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActSE_in END)", "settingManActSE_in") & "," & _
           "settingSelRelease_in" & _
           ")" & _
           IIf(hasPriceAssignmentSubClass, " END", ""))
 ' ### ENDIF IVK ###

         genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, , edomListNonLrt)

         Print #fileNo, addTab(2); "FROM"
         If hasOwnTable Then
           Print #fileNo, addTab(3); qualTabNamePrivNl; " PRIV"
         Else
           Print #fileNo, addTab(3); qualTabNamePrivNl; " PRIV,"
           Print #fileNo, addTab(3); qualTabNamePub; " PAR"
         End If
         Print #fileNo, addTab(2); "WHERE"
         Print #fileNo, addTab(3); "PRIV."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "PRIV."; g_anLrtState; " = "; CStr(lrtStatusCreated)
         If Not hasOwnTable Then
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "PRIV."; genSurrogateKeyName(ddlType, entityShortName); " = PAR."; g_anOid
         End If
         Print #fileNo, addTab(2); ";"

         genProcSectionHeader(fileNo, "count the number of affected rows", 2)
         Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
         Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
       End If

       Print #fileNo, addTab(1); "ELSEIF ( lrtStatus_in = "; CStr(lrtStatusUpdated); " ) THEN"
 ' ### IF IVK ###
       If condenseData Then
         genProcSectionHeader(fileNo, "UPDATE not supported for this table", 2, True)
       Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNo, "UPDATE: propagate all LRT-private modified records into public tables", 2, True)
         Print #fileNo, addTab(2); "UPDATE"
         Print #fileNo, addTab(3); qualTabNamePub; " PUB"
         Print #fileNo, addTab(2); "SET"
         Print #fileNo, addTab(2); "("

         genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 3, False, forGen, edomListNonLrt)

         Print #fileNo, addTab(2); ")"
         Print #fileNo, addTab(2); "="
         Print #fileNo, addTab(2); "("
         Print #fileNo, addTab(3); "SELECT"

 ' ### IF IVK ###
         initAttributeTransformation(transformation, 8, , , , "PRIV.")
 ' ### ELSE IVK ###
 '       initAttributeTransformation transformation, 4, , , , "PRIV."
 ' ### ENDIF IVK ###

         setAttributeMapping(transformation, 1, conInLrt, "CAST(NULL AS " & g_dbtOid & ")")
         setAttributeMapping(transformation, 2, conLastUpdateTimestamp, "commitTs_in")

 ' ### IF IVK ###
         If isPrimaryOrg Then
           setAttributeMapping(transformation, 3, conUpdateUser, "cdUserId_in")
         Else
           setAttributeMapping(transformation, 3, conUpdateUser, "(CASE isFtoLrt_in WHEN 1 THEN PRIV." & g_anUpdateUser & " ELSE cdUserId_in END)")
         End If
 ' ### ELSE IVK ###
 '       setAttributeMapping transformation, 3, conUpdateUser, "cdUserId_in"
 ' ### ENDIF IVK ###

         setAttributeMapping(transformation, 4, conInLrt, "CAST(NULL AS " & g_dbtOid & ")")
 ' ### IF IVK ###
         setAttributeMapping(transformation, 5, conIsDeleted, "PUB." & g_anIsDeleted)
         setAttributeMapping(transformation, 6, conHasBeenSetProductive, "PUB." & g_anHasBeenSetProductive)

         If isPrimaryOrg Then
           setAttributeMapping(transformation, 7, conIsBlockedPrice, "PUB." & g_anIsBlockedPrice)
         Else
           setAttributeMapping(transformation, 7, conIsBlockedPrice, "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV.ISNATIONAL = 0) THEN PRIV." & g_anIsBlockedPrice & " ELSE PUB." & g_anIsBlockedPrice & " END)")
         End If
 
 
         If aggHeadClassIndex > 0 Then
           tmpClassId = "PRIV." & g_anAhCid
         Else
           tmpClassId = "'" & g_classes.descriptors(acmEntityIndex).classIdStr & "'"
         End If
 
         setAttributeMapping(transformation, 8, conStatusId, _
           IIf(hasPriceAssignmentSubClass, "CASE WHEN (autoPriceSetProductive_in = 1) AND (PRIV." & g_anCid & " IN (" & priceAssignmentSubClassIdList & ")) THEN " & statusReadyToBeSetProductive & " ELSE ", "") & _
           g_qualFuncNameGetLrtTargetStatus & "(" & _
           tmpClassId & "," & _
           "CAST('" & gc_acmEntityTypeKeyClass & "' AS " & g_dbtEntityType & ")," & _
           IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActCP_in END)", "settingManActCP_in") & "," & _
           IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActTP_in END)", "settingManActTP_in") & "," & _
           IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PRIV." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActSE_in END)", "settingManActSE_in") & "," & _
           "settingSelRelease_in" & _
           ")" & _
           IIf(hasPriceAssignmentSubClass, " END", ""))
 ' ### ENDIF IVK ###

         genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 4, , False, forGen, edomListNonLrt)

         Print #fileNo, addTab(3); "FROM"
         Print #fileNo, addTab(4); qualTabNamePriv; " PRIV"
         Print #fileNo, addTab(3); "WHERE"
         Print #fileNo, addTab(4); "PUB."; g_anOid; " = PRIV."; g_anOid
         If isPsTagged Then
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "PRIV."; g_anPsOid; " = psOid_in"
         End If
         Print #fileNo, addTab(5); "AND"
         Print #fileNo, addTab(4); "PRIV."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(5); "AND"
         Print #fileNo, addTab(4); "PRIV."; g_anLrtState; " = "; CStr(lrtStatusUpdated)
         Print #fileNo, addTab(2); ")"
         Print #fileNo, addTab(2); "WHERE"
         If isPsTagged Then
           Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(4); "AND"
         End If
         Print #fileNo, addTab(3); "EXISTS ("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "1"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffected; " AS ses"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PUB."; g_anOid; " = ses.oid"
         Print #fileNo, addTab(3); ")"
         Print #fileNo, addTab(2); "WITH UR;"

         genProcSectionHeader(fileNo, "count the number of affected rows", 2)
         Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
         Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

         If hasNlAttributes Then
           genProcSectionHeader(fileNo, "update records in NL-table", 2)
           Print #fileNo, addTab(2); "UPDATE"
           Print #fileNo, addTab(3); qualTabNamePubNl; " PUB"
           Print #fileNo, addTab(2); "SET"
           Print #fileNo, addTab(2); "("

           genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 3, forGen, False, edomListNonLrt)

           Print #fileNo, addTab(2); ")"
           Print #fileNo, addTab(2); "="
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "SELECT"

 ' ### IF IVK ###
           initAttributeTransformation(transformation, 4, , , , "PRIV.")
 ' ### ELSE IVK ###
 '         initAttributeTransformation transformation, 1, , , , "PRIV."
 ' ### ENDIF IVK ###

           setAttributeMapping(transformation, 1, conInLrt, "CAST(NULL AS " & g_dbtOid & ")")
 ' ### IF IVK ###
           setAttributeMapping(transformation, 2, conIsDeleted, "PUB." & g_anIsDeleted)
           setAttributeMapping(transformation, 3, conHasBeenSetProductive, "PUB." & g_anHasBeenSetProductive)

           If aggHeadClassIndex > 0 Then
             tmpClassId = "PRIV." & g_anAhCid
           Else
             tmpClassId = "'" & g_classes.descriptors(acmEntityIndex).classIdStr & "'"
           End If

           setAttributeMapping(transformation, 4, conStatusId, _
             IIf(hasPriceAssignmentSubClass, "CASE WHEN (autoPriceSetProductive_in = 1) AND (PAR." & g_anCid & " IN (" & priceAssignmentSubClassIdList & ")) THEN " & statusReadyToBeSetProductive & " ELSE ", "") & _
             g_qualFuncNameGetLrtTargetStatus & "(" & _
             tmpClassId & "," & _
             "CAST('" & gc_acmEntityTypeKeyClass & "' AS " & g_dbtEntityType & ")," & _
             IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActCP_in END)", "settingManActCP_in") & "," & _
             IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActTP_in END)", "settingManActTP_in") & "," & _
             IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActSE_in END)", "settingManActSE_in") & "," & _
             "settingSelRelease_in" & _
             ")" & _
             IIf(hasPriceAssignmentSubClass, " END", ""))
 ' ### ENDIF IVK ###

           genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 4, forGen, False, , edomListNonLrt)

           Print #fileNo, addTab(3); "FROM"

           Print #fileNo, addTab(4); qualTabNamePrivNl; " PRIV"
           If Not hasOwnTable Then
             Print #fileNo, addTab(3); "INNER JOIN"
             Print #fileNo, addTab(4); qualTabNamePub; " PAR"
             Print #fileNo, addTab(3); "ON"
             Print #fileNo, addTab(4); "PRIV."; genSurrogateKeyName(ddlType, entityShortName); " = PAR."; g_anOid
           End If
           Print #fileNo, addTab(3); "WHERE"
           Print #fileNo, addTab(4); "PUB."; g_anOid; " = PRIV."; g_anOid
           If isPsTagged Then
             Print #fileNo, addTab(5); "AND"
             Print #fileNo, addTab(4); "PRIV."; g_anPsOid; " = psOid_in"
           End If
           Print #fileNo, addTab(2); ")"
           Print #fileNo, addTab(2); "WHERE"
           If isPsTagged Then
             Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
           End If
           Print #fileNo, addTab(3); "EXISTS ("
           Print #fileNo, addTab(4); "SELECT"
           Print #fileNo, addTab(5); "1"
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffectedNl; " AS ses"
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "PUB."; g_anOid; " = ses.oid"
           Print #fileNo, addTab(3); ")"
           Print #fileNo, addTab(2); "WITH UR;"

           genProcSectionHeader(fileNo, "count the number of affected rows", 2)
           Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
           Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
 
         End If
 ' ### IF IVK ###

         If entityName = clnTypeSpec Then
             Dim genericAspectTabName As String
             genericAspectTabName = genQualTabNameByClassIndex(getClassIndexByName(clxnGenericAspect, clnGenericAspect), ddlType, thisOrgIndex, thisPoolIndex, False)
             Print #fileNo,
             genProcSectionHeader(fileNo, "if TPA is deleted, then the typespec must have the same status as the deleted TPA in order to avoid constraint violations during setproductive", 2)
             Print #fileNo, addTab(2); "UPDATE"
             Print #fileNo, addTab(3); qualTabNamePub; " PUB"
             Print #fileNo, addTab(2); "SET"
             Print #fileNo, addTab(4); conStatusId; " = (SELECT TPA."; conStatusId; " FROM "; genericAspectTabName; " TPA WHERE PUB.TSTTPA_OID = TPA."; g_anOid; ")"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "1"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffected; " AS ses"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "PUB."; g_anOid; " = ses.oid"
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "1"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); genericAspectTabName; " AS TPA"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "PUB.TSTTPA_OID = TPA."; g_anOid
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(5); "TPA."; conIsDeleted; " = 1"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(5); "TPA."; conHasBeenSetProductive; " = 1"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(5); "PUB."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(3); ";"
         End If
 

         If isGenericAspectHead Then
           ' GenericAspect always requires some special treatment...
             genProcSectionHeader(fileNo, "propagate status to all aggregate children", 2)

             For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes)
                 If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isUserTransactional And Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isCommonToOrgs And Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isCommonToPools And g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).superClassIndex <= 0 Then
                   If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex <> acmEntityIndex Then
                     ' set status for base table
                     genProcSectionHeader(fileNo, "propagate status to aggregate child class '" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).sectionName & "." & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).className & "'", 2, True)
                     genDdlForAggStatusPropLrtCommit(_
                       genQualTabNameByClassIndex(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, thisPoolIndex), qualTabNamePriv, _
                         priceAssignmentSubClassIdList, fileNo, 2, g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isPsTagged, "psOid_in")
                   End If
                   If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).hasNlAttrsInNonGenInclSubClasses Then
                     ' set status for NL-Text table
                     genProcSectionHeader(fileNo, "propagate status to aggregate child class '" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).sectionName & "." & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).className & "' (NL_TEXT)", 2, True)
                     genDdlForAggStatusPropLrtCommit(_
                       genQualTabNameByClassIndex(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , , , True), qualTabNamePriv, _
                         priceAssignmentSubClassIdList, fileNo, 2, g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isPsTagged, "psOid_in")
                   End If

                   If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isGenForming And Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).hasNoIdentity Then
                     ' set status for GENtable
                     genProcSectionHeader(fileNo, "propagate status to aggregate child class '" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).sectionName & "." & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).className & "' (GEN)", 2, True)
                     genDdlForAggStatusPropLrtCommit(_
                       genQualTabNameByClassIndex(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, thisPoolIndex, True), qualTabNamePriv, _
                         priceAssignmentSubClassIdList, fileNo, 2, g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isPsTagged, "psOid_in")

                     If g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).hasNlAttrsInGenInclSubClasses Then
                       ' set status for NL-Text GEN-table
                       genProcSectionHeader(fileNo, "propagate status to aggregate child class '" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).sectionName & "." & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).className & "' (GEN/NL_TEXT)", 2, True)
                       genDdlForAggStatusPropLrtCommit(_
                         genQualTabNameByClassIndex(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).classIndex, ddlType, thisOrgIndex, thisPoolIndex, True, , , True), qualTabNamePriv, _
                           priceAssignmentSubClassIdList, fileNo, 2, g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggChildClassIndexes(i)).isPsTagged, "psOid_in")
                     End If
                   End If
                 End If
             Next i
 
             For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes)
                 If g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).isUserTransactional And Not g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).isCommonToOrgs And Not g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).isCommonToPools Then
                     ' set status for relationship table
                     genProcSectionHeader(fileNo, "propagate status to aggregate child relationship '" & g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).sectionName & "." & g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).relName & "'", 2, True)
                     genDdlForAggStatusPropLrtCommit(_
                       genQualTabNameByRelIndex(g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).relIndex, ddlType, thisOrgIndex, thisPoolIndex), qualTabNamePriv, _
                         priceAssignmentSubClassIdList, fileNo, 2, g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).aggChildRelIndexes(i)).isPsTagged, "psOid_in")
                 End If
             Next i
         End If
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###

       Print #fileNo, addTab(1); "ELSEIF ( lrtStatus_in = "; CStr(lrtStatusDeleted); " ) THEN"

 ' ### IF IVK ###
       If condenseData Then
         genProcSectionHeader(fileNo, "DELETE not supported for this table", 2, True)
       Else
         If lrtCommitDeleteDeletedNonProductiveRecords Then
           genProcSectionHeader(fileNo, "DELETE: delete records in public tables which are not 'set productive' and marked 'deleted[" & CStr(lrtStatusDeleted) & "]' in LRT", 2, True)

 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -4
 ' ### ENDIF IVK ###
           If hasNlAttributes Then
             genProcSectionHeader(fileNo, "delete records in NL-table", 2, True)
             Print #fileNo, addTab(2); "DELETE FROM"
             Print #fileNo, addTab(3); qualTabNamePubNl; " AS PUBNL"
             Print #fileNo, addTab(2); "WHERE"
             If isPsTagged Then
               Print #fileNo, addTab(3); "PUBNL."; g_anPsOid; " = psOid_in"
               Print #fileNo, addTab(4); "AND"
             End If
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "1"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffectedNl; " AS ses"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "PUBNL."; g_anOid; " = ses.oid"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbFalse
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(2); ";"

             genProcSectionHeader(fileNo, "count the number of affected rows", 2)
             Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
             Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
           End If

           If acmEntityIndex = getClassIndexByName(clxnGenericCode, clnGenericCode) And acmEntityType = eactClass Then
             genProcSectionHeader(fileNo, "delete records in 'codecategory'", 2)
             Print #fileNo, addTab(2); "DELETE FROM"
             Print #fileNo, addTab(3); genQualTabNameByRelIndex(g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex); " AS PUB"
             Print #fileNo, addTab(2); "WHERE"
             If isPsTagged Then
               Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
               Print #fileNo, addTab(4); "AND"
             End If
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "1"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffected; " AS ses"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "PUB."; g_anAhOid; " = ses.oid"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbFalse
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(2); ";"

             genProcSectionHeader(fileNo, "count the number of affected rows", 2)
             Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
             Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
           End If

           genProcSectionHeader(fileNo, "delete records in 'base table'", 2, Not hasNlAttributes)
           Print #fileNo, addTab(2); "DELETE FROM"
           Print #fileNo, addTab(3); qualTabNamePub; " AS PUB"
           Print #fileNo, addTab(2); "WHERE"
           If isPsTagged Then
             Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
           End If
           Print #fileNo, addTab(3); "EXISTS ("
           Print #fileNo, addTab(4); "SELECT"
           Print #fileNo, addTab(5); "1"
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffected; " AS ses"
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "PUB."; g_anOid; " = ses.oid"
           Print #fileNo, addTab(6); "AND"
           Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbFalse
           Print #fileNo, addTab(3); ")"
           Print #fileNo, addTab(2); ";"

           genProcSectionHeader(fileNo, "count the number of affected rows", 2)
           Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
           Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
 ' ### IF IVK ###
         Else
           genProcSectionHeader(fileNo, "DELETE: mark records in public tables as 'deleted' which are not 'set productive' and marked 'deleted[" & CStr(lrtStatusDeleted) & "]' in LRT", 2, True)

           If hasNlAttributes Then
             genProcSectionHeader(fileNo, "mark record in NL-table as being deleted", 2, True)
             Print #fileNo, addTab(2); "UPDATE"
             Print #fileNo, addTab(3); qualTabNamePubNl; " PUBNL"
             Print #fileNo, addTab(2); "SET"
             Print #fileNo, addTab(3); "PUBNL."; g_anIsDeleted; " = "; gc_dbTrue
             Print #fileNo, addTab(2); "WHERE"
             If isPsTagged Then
               Print #fileNo, addTab(3); "PUBNL."; g_anPsOid; " = psOid_in"
               Print #fileNo, addTab(4); "AND"
             End If
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "1"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffectedNl; " AS ses"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "PUBNL."; g_anOid; " = ses.oid"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbFalse
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(2); "WITH UR;"

             genProcSectionHeader(fileNo, "count the number of affected rows", 2)
             Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
             Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
           End If

           genProcSectionHeader(fileNo, "mark records in 'base table' as being deleted", 2, Not hasNlAttributes)
           Print #fileNo, addTab(2); "UPDATE"
           Print #fileNo, addTab(3); qualTabNamePub; " PUB"
           Print #fileNo, addTab(2); "SET"
           Print #fileNo, addTab(3); "PUB."; g_anIsDeleted; " = "; gc_dbTrue
           Print #fileNo, addTab(2); "WHERE"
           If isPsTagged Then
             Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
           End If
           Print #fileNo, addTab(3); "EXISTS ("
           Print #fileNo, addTab(4); "SELECT"
           Print #fileNo, addTab(5); "1"
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffected; " AS ses"
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "PUB."; g_anOid; " = ses.oid"
           Print #fileNo, addTab(6); "AND"
           Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbFalse
           Print #fileNo, addTab(3); ")"
           Print #fileNo, addTab(2); "WITH UR;"

           genProcSectionHeader(fileNo, "count the number of affected rows", 2)
           Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
           Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
 '          Print #fileNo, addTab(2); "END FOR;"
         End If

         genProcSectionHeader(fileNo, "DELETE: mark records in public tables as 'deleted' which are 'set productive' and marked 'deleted[" & CStr(lrtStatusDeleted) & "]' in LRT", 2)

         If hasNlAttributes Then
           genProcSectionHeader(fileNo, "mark records in NL-table", 2, True)

           If hasPriceAssignmentSubClass Then
             Print #fileNo, addTab(2); "UPDATE"
             Print #fileNo, addTab(3); qualTabNamePubNl; " PUBNL"
             Print #fileNo, addTab(2); "SET"
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); "PUBNL."; g_anIsDeleted; ","
             Print #fileNo, addTab(3); "PUBNL."; g_anInLrt; ","
             Print #fileNo, addTab(3); "PUBNL."; g_anStatus; ""
             Print #fileNo, addTab(2); ")"
             Print #fileNo, addTab(2); "="
             Print #fileNo, addTab(2); "("
             Print #fileNo, addTab(3); "SELECT"
             Print #fileNo, addTab(4); "1,"
             Print #fileNo, addTab(4); "CAST(NULL AS "; g_dbtOid; "),"
             Print #fileNo, addTab(4); "CASE WHEN (autoPriceSetProductive_in = 1) AND (PAR."; g_anCid; " IN ("; priceAssignmentSubClassIdList; ")) THEN"; statusReadyToBeSetProductive; "ELSE "; _
               g_qualFuncNameGetLrtTargetStatus; "("; _
               "PUBNL."; g_anAhCid; ","; _
               "CAST('"; gc_acmEntityTypeKeyClass; "' AS " & g_dbtEntityType & "),"; _
               IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActCP_in END)", "settingManActCP_in") & "," & _
               IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActTP_in END)", "settingManActTP_in") & "," & _
               IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PAR." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActSE_in END)", "settingManActSE_in") & "," & _
               "settingSelRelease_in"; _
               ")"; _
               " END"
 
             Print #fileNo, addTab(3); "FROM"

             If hasOwnTable Then
               Print #fileNo, addTab(4); "SYSIBM.SYDUMMY1"
             Else
               Print #fileNo, addTab(4); qualTabNamePub; " PAR"
               Print #fileNo, addTab(3); "WHERE"
               Print #fileNo, addTab(4); "PUBNL."; entityShortName; "_"; g_anOid; " = PAR."; g_anOid
               Print #fileNo, addTab(5); "AND"
               Print #fileNo, addTab(4); "PAR."; g_anPsOid; " = psOid_in"
             End If
             Print #fileNo, addTab(2); ")"

             Print #fileNo, addTab(2); "WHERE"
 
             If isPsTagged Then
               Print #fileNo, addTab(3); "PUBNL."; g_anPsOid; " = psOid_in"
               Print #fileNo, addTab(4); "AND"
             End If
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "1"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffectedNl; " AS ses"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "PUBNL."; g_anOid; " = ses.oid"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbTrue
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(2); "WITH UR;"

           Else
             Print #fileNo, addTab(2); "UPDATE"
             Print #fileNo, addTab(3); qualTabNamePubNl; " PUBNL"
             Print #fileNo, addTab(2); "SET"
             Print #fileNo, addTab(3); "PUBNL."; g_anIsDeleted; " = 1,"
             Print #fileNo, addTab(3); "PUBNL."; g_anStatus; " = "; _
               g_qualFuncNameGetLrtTargetStatus; "("; _
               "PUBNL." & g_anAhCid & ","; _
               "CAST('" & gc_acmEntityTypeKeyClass & "' AS " & g_dbtEntityType & "),"; _
               "settingManActCP_in,"; _
               "settingManActTP_in,"; _
               "settingManActSE_in,"; _
               "settingSelRelease_in"; _
              "),"
             Print #fileNo, addTab(3); "PUBNL."; g_anInLrt; " = CAST(NULL AS "; g_dbtOid; ")"
             Print #fileNo, addTab(2); "WHERE"
             If isPsTagged Then
               Print #fileNo, addTab(3); "PUBNL."; g_anPsOid; " = psOid_in"
               Print #fileNo, addTab(4); "AND"
             End If
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "1"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffectedNl; " AS ses"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "PUBNL."; g_anOid; " = ses.oid"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbTrue
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(2); "WITH UR;"
           End If

           genProcSectionHeader(fileNo, "count the number of affected rows", 2)
           Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
           Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
         End If

         If acmEntityIndex = getClassIndexByName(clxnGenericCode, clnGenericCode) And acmEntityType = eactClass Then
           genProcSectionHeader(fileNo, "mark records in 'codecategory'", 2)
           Print #fileNo, addTab(2); "UPDATE"
           Print #fileNo, addTab(3); genQualTabNameByRelIndex(g_relIndexCodeCategory, ddlType, thisOrgIndex, thisPoolIndex); " PUB"
           Print #fileNo, addTab(2); "SET"
           Print #fileNo, addTab(3); "PUB.ISDELETED = 1,"
           Print #fileNo, addTab(3); "PUB.INLRT = CAST(NULL AS BIGINT),"
           Print #fileNo, addTab(3); "PUB.STATUS_ID = VL6CLRT.F_GETLRTTGS(PUB.AHCLASSID,CAST('C' AS CHAR(1)),settingManActCP_in,settingManActTP_in,settingManActSE_in,settingSelRelease_in)"
           Print #fileNo, addTab(2); "WHERE"

           If isPsTagged Then
             Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
           End If
           Print #fileNo, addTab(3); "EXISTS ("
           Print #fileNo, addTab(4); "SELECT"
           Print #fileNo, addTab(5); "1"
           Print #fileNo, addTab(4); "FROM"
           Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffected; " AS ses"
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "PUB."; g_anAhOid; " = ses.oid"
           Print #fileNo, addTab(6); "AND"
           Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbTrue
           Print #fileNo, addTab(3); ")"
           Print #fileNo, addTab(2); "WITH UR;"
 
           genProcSectionHeader(fileNo, "count the number of affected rows", 2)
           Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
           Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

         End If

         genProcSectionHeader(fileNo, "mark records in 'base table'", 2, Not hasNlAttributes)
         Print #fileNo, addTab(2); "UPDATE"
         Print #fileNo, addTab(3); qualTabNamePub; " PUB"
         Print #fileNo, addTab(2); "SET"
         Print #fileNo, addTab(3); "PUB."; g_anIsDeleted; " = 1,"
         Print #fileNo, addTab(3); "PUB."; g_anInLrt; " = CAST(NULL AS "; g_dbtOid; "),"

         If aggHeadClassIndex > 0 Then
           tmpClassId = "PUB." + g_anAhCid
         Else
           tmpClassId = "'" + g_classes.descriptors(acmEntityIndex).classIdStr + "'"
         End If

         Print #fileNo, addTab(3); "PUB."; g_anStatus; " = "; _
           IIf(hasPriceAssignmentSubClass, "CASE WHEN (autoPriceSetProductive_in = 1) AND (PUB." & g_anCid & " IN (" & priceAssignmentSubClassIdList & ")) THEN " & statusReadyToBeSetProductive & " ELSE ", ""); _
           g_qualFuncNameGetLrtTargetStatus; "("; _
           tmpClassId; ","; _
           "CAST('"; gc_acmEntityTypeKeyClass; "' AS " & g_dbtEntityType & "),"; _
           IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PUB." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActCP_in END)", "settingManActCP_in") & "," & _
           IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PUB." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActTP_in END)", "settingManActTP_in") & "," & _
           IIf(setManActConditional, "(CASE WHEN (isFtoLrt_in = 1) AND (PUB." & g_anIsNational & " = 0) THEN " & g_dbtBoolean & "(0) ELSE settingManActSE_in END)", "settingManActSE_in") & "," & _
           "settingSelRelease_in"; _
           ")"; _
           IIf(hasPriceAssignmentSubClass, " END", "")

         Print #fileNo, addTab(2); "WHERE"
         If isPsTagged Then
           Print #fileNo, addTab(3); "PUB."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(4); "AND"
         End If
         Print #fileNo, addTab(3); "EXISTS ("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "1"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); pc_tempTabNamePubOidsAffected; " AS ses"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PUB."; g_anOid; " = ses.oid"
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "ses.hasBeenSetProductive = "; gc_dbTrue
         Print #fileNo, addTab(3); ")"
         Print #fileNo, addTab(2); "WITH UR;"

         genProcSectionHeader(fileNo, "count the number of affected rows", 2)
         Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
         Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

 ' ### ENDIF IVK ###

 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
       Print #fileNo, addTab(1); "END IF;"

 ' ### IF IVK ###
       If Not isPrimaryOrg Then
         genSpLogProcExit(fileNo, _
           qualProcNameLrtCommit, ddlType, , "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", _
           "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "isFtoLrt_in", "rowCount_out")
       Else
         genSpLogProcExit(fileNo, _
           qualProcNameLrtCommit, ddlType, , "lrtOid_in", "'cdUserId_in", "psOid_in", "lrtStatus_in", "#commitTs_in", "autoPriceSetProductive_in", _
           "settingManActCP_in", "settingManActTP_in", "settingManActSE_in", "settingSelRelease_in", "rowCount_out")
       End If
 ' ### ELSE IVK ###
 '     genSpLogProcExit fileNo, qualProcNameLrtCommit, ddlType, , "lrtOid_in", "'cdUserId_in", "lrtStatus_in", "#commitTs_in", "rowCount_out"
 ' ### ENDIF IVK ###

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     End If
   End If

   genLrtSupportSpsForEntity3(acmEntityIndex, acmEntityType, thisOrgIndex, thisPoolIndex, fileNo, fileNoClView, ddlType, forGen, forNl)
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genLrtSupportSpsForEntity3( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   fileNoClView As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False _
 )
   Dim sectionName As String
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim ahClassIdStr As String
   Dim isUserTransactional As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim entityIdStrList As String
   Dim dbAcmEntityType As String
   Dim attrRefs As AttrDescriptorRefs
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
   Dim hasNlAttributes As Boolean
   Dim hasNlTable As Boolean
   Dim attrMapping() As AttributeMappingForCl
   Dim relLeftClassIdStr As String
   Dim relLeftFk As String
   Dim relRightClassIdStr As String
   Dim relRightFk As String
   Dim ignoreForChangelog As Boolean
   Dim aggHeadClassIndex As Integer
   Dim aggHeadShortClassName As String
   Dim isAggregateHead As Boolean
   Dim implicitelyGenChangeComment As Boolean
   Dim ahHasChangeComment As Boolean
   Dim busKeyAttrList As String
 ' ### IF IVK ###
   Dim isPsTagged As Boolean
   Dim hasNoIdentity As Boolean
   Dim lrtClassification As String
   Dim lrtActivationStatusMode As String
   Dim hasPriceAssignmentSubClass As Boolean
   Dim hasPriceAssignmentAggHead As Boolean
   Dim priceAssignmentSubClassIdList As String
   Dim isSubjectToPreisDurchschuss As String

   Dim condenseData As Boolean
   Dim enforceLrtChangeComment As Boolean
 ' ### ENDIF IVK ###
 
   On Error GoTo ErrorExit

   If ddlType = edtPdm Then
     If thisPoolIndex < 1 Then
       Exit Sub
     ElseIf Not g_pools.descriptors(thisPoolIndex).supportLrt Then
       Exit Sub
     End If
   End If

   Dim transformation As AttributeListTransformation
   transformation = nullAttributeTransformation

 ' ### IF IVK ###
   hasPriceAssignmentSubClass = False
   hasPriceAssignmentAggHead = False
   priceAssignmentSubClassIdList = ""
   enforceLrtChangeComment = False
 ' ### ENDIF IVK ###
   busKeyAttrList = ""

   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       entityIdStrList = getSubClassIdStrListByClassIndex(g_classes.descriptors(acmEntityIndex).classIndex)
       ahClassIdStr = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       hasNlAttributes = IIf(forGen, g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses)
       attrMapping = g_classes.descriptors(acmEntityIndex).clMapAttrsInclSubclasses
       ignoreForChangelog = g_classes.descriptors(acmEntityIndex).ignoreForChangelog
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       isAggregateHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).classIndex)
       implicitelyGenChangeComment = g_classes.descriptors(acmEntityIndex).implicitelyGenChangeComment
       If g_classes.descriptors(acmEntityIndex).hasBusinessKey Then
         busKeyAttrList = getPkAttrListByClassIndex(acmEntityIndex, ddlType)
       End If
 ' ### IF IVK ###
       lrtClassification = g_classes.descriptors(acmEntityIndex).lrtClassification
       lrtActivationStatusMode = g_classes.descriptors(acmEntityIndex).lrtActivationStatusMode
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
 ' ### ENDIF IVK ###

       Dim i As Integer
       If forNl Then
         entityName = genNlObjName(g_classes.descriptors(acmEntityIndex).className, , forGen)
         entityShortName = genNlObjShortName(g_classes.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Class (NL-Text)"
         hasOwnTable = True
         isAbstract = False
         attrRefs = g_classes.descriptors(acmEntityIndex).nlAttrRefsInclSubclasses
         relRefs.numRefs = 0
         isGenForming = False
 ' ### IF IVK ###
         isPsTagged = usePsTagInNlTextTables And g_classes.descriptors(acmEntityIndex).isPsTagged
         hasNoIdentity = False
 ' ### ENDIF IVK ###
       Else
         entityName = g_classes.descriptors(acmEntityIndex).className
         entityShortName = g_classes.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Class"
         hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
         isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
         attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
         relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
         isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
 ' ### IF IVK ###
         isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
         hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
         hasPriceAssignmentSubClass = g_classes.descriptors(acmEntityIndex).hasPriceAssignmentSubClass
         hasPriceAssignmentAggHead = g_classes.descriptors(acmEntityIndex).hasPriceAssignmentAggHead
         isSubjectToPreisDurchschuss = g_classes.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss
         enforceLrtChangeComment = g_classes.descriptors(acmEntityIndex).enforceLrtChangeComment

         If hasPriceAssignmentSubClass Then
           For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive)
               If Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isPriceAssignment Then
                 priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).classIdStr & "'"
               End If
           Next i
         ElseIf hasPriceAssignmentAggHead Then
             For i = 1 To UBound(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive)
                 If Not g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isPriceAssignment Then
                   priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).classIdStr & "'"
                 End If
             Next i
         End If
 ' ### ENDIF IVK ###
       End If
   ElseIf acmEntityType = eactRelationship Then
       If forNl Then
         entityName = genNlObjName(g_relationships.descriptors(acmEntityIndex).relName, , forGen)
         entityShortName = genNlObjShortName(g_relationships.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Relationship (NL-Text)"
 ' ### IF IVK ###
         isPsTagged = usePsTagInNlTextTables And g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
         attrRefs = g_relationships.descriptors(acmEntityIndex).nlAttrRefs
       Else
         entityName = g_relationships.descriptors(acmEntityIndex).relName
         entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Relationship"
 ' ### IF IVK ###
         isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
 ' ### ENDIF IVK ###
         attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
       End If
 
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       entityIdStrList = "'" & g_relationships.descriptors(acmEntityIndex).relIdStr & "'"
       ahClassIdStr = g_relationships.descriptors(acmEntityIndex).aggHeadClassIdStr
       dbAcmEntityType = "R"
       relRefs.numRefs = 0
       isGenForming = False
       hasNlAttributes = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0
       ignoreForChangelog = g_relationships.descriptors(acmEntityIndex).ignoreForChangelog
       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       isAggregateHead = False
 ' ### IF IVK ###
       condenseData = False
       hasNoIdentity = False
       lrtClassification = g_relationships.descriptors(acmEntityIndex).lrtClassification
       lrtActivationStatusMode = g_relationships.descriptors(acmEntityIndex).lrtActivationStatusMode
       hasPriceAssignmentAggHead = g_relationships.descriptors(acmEntityIndex).hasPriceAssignmentAggHead
       isSubjectToPreisDurchschuss = g_relationships.descriptors(acmEntityIndex).isSubjectToPreisDurchschuss
 
       If hasPriceAssignmentAggHead Then
           For i = 1 To UBound(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive)
               If Not g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isAbstract And g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).isPriceAssignment Then
                 priceAssignmentSubClassIdList = priceAssignmentSubClassIdList & IIf(priceAssignmentSubClassIdList <> "", ",", "") & "'" & g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex).subclassIndexesRecursive(i)).classIdStr & "'"
               End If
           Next i
       End If
 ' ### ENDIF IVK ###

       Dim tabColumns As EntityColumnDescriptors
       tabColumns = nullEntityColumnDescriptors
 
       Dim ukAttrDecls As String
       Dim pkAttrList As String
       Dim leftFkAttrs As String
       Dim rightFkAttrs As String
       genTransformedAttrDeclsForRelationshipWithColReUse_Int(acmEntityIndex, transformation, tabColumns, _
           ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, ddlType, , , 1, , False, False, edomNone)
       busKeyAttrList = leftFkAttrs & "," & rightFkAttrs

       Dim reuseRelIndex As Integer
       reuseRelIndex = IIf(reuseRelationships And g_relationships.descriptors(acmEntityIndex).reusedRelIndex > 0, g_relationships.descriptors(acmEntityIndex).reusedRelIndex, acmEntityIndex)
           relLeftClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).classIdStr
           relLeftFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).leftEntityIndex).shortName)
           relRightClassIdStr = g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).classIdStr
           relRightFk = genAttrName(conOid, ddlType, g_classes.descriptors(g_relationships.descriptors(reuseRelIndex).rightEntityIndex).shortName)
   Else
     Exit Sub
   End If

 ' ### IF IVK ###
   hasNlTable = hasNlAttributes Or (isAggregateHead And implicitelyGenChangeComment And Not forGen And Not forNl And Not condenseData)
 ' ### ELSE IVK ###
 ' hasNlTable = hasNlAttributes Or (isAggregateHead And implicitelyGenChangeComment And Not forGen And Not forNl)
 ' ### ENDIF IVK ###

   If Not generateLrt Or Not isUserTransactional Then
     Exit Sub
   End If
   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 0) Then
     ' LRT is only supported at 'pool-level'
     Exit Sub
   End If
 
   If aggHeadClassIndex > 0 Then
     aggHeadShortClassName = g_classes.descriptors(aggHeadClassIndex).shortName
   End If
 
   Dim qualTabNamePub As String, qualTabNamePriv As String, unQualTabNamePub As String
   qualTabNamePub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl)
   qualTabNamePriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , forNl)
   unQualTabNamePub = getUnqualObjName(qualTabNamePub)
 
   Dim qualTabNameNlPub As String, qualTabNameNlPriv As String
 ' ### IF IVK ###
   If hasNlAttributes Or ((isAggregateHead Or enforceLrtChangeComment) And Not forGen And Not forNl) Then
 ' ### ELSE IVK ###
 ' If hasNlAttributes Or (isAggregateHead And Not forGen And Not forNl) Then
 ' ### ENDIF IVK ###
     qualTabNameNlPub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , True)
     qualTabNameNlPriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, True, , True)
   End If
 
   Dim qualTabNameAggHeadPub As String
   Dim qualViewNameAggHead As String
   Dim qualTabNameAggHeadNlPriv As String
   Dim aggHeadFkAttrName As String
   qualTabNameAggHeadPub = ""
   qualTabNameAggHeadNlPriv = ""
   If aggHeadClassIndex > 0 Then
     qualTabNameAggHeadPub = genQualTabNameByClassIndex(aggHeadClassIndex, ddlType, thisOrgIndex, thisPoolIndex)

       qualViewNameAggHead = genQualViewNameByClassIndex(g_classes.descriptors(aggHeadClassIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , True, g_classes.descriptors(aggHeadClassIndex).useMqtToImplementLrt)
       qualTabNameAggHeadNlPriv = genQualTabNameByClassIndex(g_classes.descriptors(aggHeadClassIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, , True, , True)
       ahHasChangeComment = g_classes.descriptors(aggHeadClassIndex).implicitelyGenChangeComment Or g_classes.descriptors(aggHeadClassIndex).hasNlAttrsInNonGenInclSubClasses
       aggHeadFkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(aggHeadClassIndex).shortName)
   End If
 
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualPdmTableViewName As String
   qualPdmTableViewName = genQualViewName(g_sectionIndexDbMeta, vnPdmTable, vnsPdmTable, ddlType)

   Dim hasNlLabelAttr As Boolean
   hasNlLabelAttr = False
 ' ### IF IVK ###
   Dim labelIsNationalizable As Boolean
   labelIsNationalizable = False
 ' ### ENDIF IVK ###

   If Not forNl Then
     initAttributeTransformation(transformation, 0, , True)
     genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , , True, forGen, edomNone)

       Dim j As Integer
       For j = 1 To transformation.nlAttrRefs.numDescriptors
         If transformation.nlAttrRefs.descriptors(j).refType = eadrtAttribute Then
             If UCase(g_attributes.descriptors(transformation.nlAttrRefs.descriptors(j).refIndex).attributeName) = "LABEL" Then
               hasNlLabelAttr = True
 ' ### IF IVK ###
               labelIsNationalizable = g_attributes.descriptors(transformation.nlAttrRefs.descriptors(j).refIndex).isNationalizable
 ' ### ENDIF IVK ###
             End If
         End If
       Next j
   End If
 
   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualProcName As String

   If generateLrtSps Then
     If Not forNl Then
       ' ####################################################################################################################
       ' #    SP for ROLLBACK on given class
       ' ####################################################################################################################

       qualProcName = _
         genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, "LRTROLLBACK")

       printSectionHeader("SP for LRT-ROLLBACK on """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo)

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcName
       Print #fileNo, addTab(0); "("
       genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to rollback")
       genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by this rollback")
       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 0"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

       genProcSectionHeader(fileNo, "declare conditions", , True)
       genCondDecl(fileNo, "notFound", "02000")

       genProcSectionHeader(fileNo, "declare variables")
       genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
       If isPsTagged Then
         genVarDecl(fileNo, "v_psOid", "BIGINT", "0")
       End If
       genSpLogDecl(fileNo)

       genProcSectionHeader(fileNo, "declare condition handler")
       Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
       Print #fileNo, addTab(1); "BEGIN"
       Print #fileNo, addTab(2); "-- just ignore"
       Print #fileNo, addTab(1); "END;"

       genSpLogProcEnter(fileNo, qualProcName, ddlType, , "lrtOid_in", "rowCount_out")

       genProcSectionHeader(fileNo, "initialize output parameter 'rowCount_out'", 1)
       Print #fileNo, addTab(1); "SET rowCount_out = 0;"
       If isPsTagged Then
         Print #fileNo, addTab(1); "SET v_psOid = ( SELECT lrt.ps_oid FROM "; qualTabNameLrt; " AS lrt WHERE lrt.oid = lrtOid_in );"
       End If

 ' ### IF IVK ###
       If Not condenseData Then
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNo, "unlock rows in public table" & IIf(hasNlAttributes, "s", ""), 1)
         Print #fileNo, addTab(1); "UPDATE"
         Print #fileNo, addTab(2); qualTabNamePub; " PUB"
         Print #fileNo, addTab(1); "SET"
         Print #fileNo, addTab(2); "PUB."; g_anInLrt; " = CAST(NULL AS "; g_dbtOid; ")"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PUB."; g_anInLrt; " = lrtOid_in"
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PUB."; g_anPsOid; " = v_psOid"
         End If
         Print #fileNo, addTab(1); ";"

         If hasNlAttributes Then
           Print #fileNo,

           Print #fileNo, addTab(1); "UPDATE"
           Print #fileNo, addTab(2); qualTabNameNlPub; " PUB"
           Print #fileNo, addTab(1); "SET"
           Print #fileNo, addTab(2); "PUB."; g_anInLrt; " = CAST(NULL AS "; g_dbtOid; ")"
           Print #fileNo, addTab(1); "WHERE"
           Print #fileNo, addTab(2); "PUB."; g_anInLrt; " = lrtOid_in"
           If isPsTagged Then
             Print #fileNo, addTab(3); "AND"
             Print #fileNo, addTab(2); "PUB."; g_anPsOid; " = v_psOid"
           End If
           Print #fileNo, addTab(1); ";"
         End If
 
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###
       genProcSectionHeader(fileNo, "cleanup private table" & IIf(hasNlTable, "s", ""), 1)
       Print #fileNo, addTab(1); "DELETE FROM"
       Print #fileNo, addTab(2); qualTabNamePriv; " PRIV"
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "PRIV."; g_anInLrt; " = lrtOid_in"
       If isPsTagged Then
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "PRIV."; g_anPsOid; " = v_psOid"
       End If
       Print #fileNo, addTab(1); "WITH UR;"

       genProcSectionHeader(fileNo, "count the number of affected rows", 1)
       Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
       Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

       If hasNlTable Then
         genProcSectionHeader(fileNo, "cleanup private NL-table", 1)
         Print #fileNo, addTab(1); "DELETE FROM"
         Print #fileNo, addTab(2); qualTabNameNlPriv; " PRIV"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PRIV."; g_anInLrt; " = lrtOid_in"
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PRIV."; g_anPsOid; " = v_psOid"
         End If
         Print #fileNo, addTab(1); "WITH UR;"

         genProcSectionHeader(fileNo, "count the number of affected rows", 1)
         Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
         Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
       End If

       genSpLogProcExit(fileNo, qualProcName, ddlType, , "lrtOid_in", "rowCount_out")

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     End If

     If Not forNl And Not forGen And (acmEntityType <> eactRelationship) Then
       ' ####################################################################################################################
       ' #    SP for LRT-LOCK on record on a given class
       ' ####################################################################################################################

       qualProcName = _
         genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, "LRTLOCK")

       printSectionHeader("SP for LRT-LOCK on """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo)

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcName
       Print #fileNo, addTab(0); "("
       genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "LRT-OID used to lock the record")
 ' ### IF IVK ###
       genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure this row is supposed to correspond to")
 ' ### ENDIF IVK ###
       genProcParm(fileNo, "IN", "oid_in", g_dbtOid, True, "OID of the row being locked")
       genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being locked (0 or 1)")
       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 0"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

 ' ### IF IVK ###
       If condenseData Then
         genSpLogDecl(fileNo, -1, True)
         genSpLogProcEnter(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
         genSpLogProcEscape(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
         genSignalDdl("lrtLockNotSup", fileNo, 1, unQualTabNamePub)
       Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNo, "declare variables", , True)
         genSigMsgVarDecl(fileNo)
         genVarDecl(fileNo, "v_oid", g_dbtOid, "NULL")
         genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "NULL")
         genVarDecl(fileNo, "v_lrtEntityIdCount", "BIGINT", "0")
         genVarDecl(fileNo, "v_pubOwnerUserId", g_dbtUserId, "NULL")
         genSpLogDecl(fileNo)

 ' ### IF IVK ###
         genSpLogProcEnter(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###

         Print #fileNo,
         Print #fileNo, addTab(1); "SET rowCount_out = 0;"

         genProcSectionHeader(fileNo, "determine existance and 'current owner' of record to lock")
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "PUB."; g_anOid; ","
         Print #fileNo, addTab(2); "PUB."; g_anInLrt
         Print #fileNo, addTab(1); "INTO"
         Print #fileNo, addTab(2); "v_oid,"
         Print #fileNo, addTab(2); "v_lrtOid"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualTabNamePub; " PUB"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PUB."; g_anOid; " = oid_in"
 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PUB."; conPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ";"

         genProcSectionHeader(fileNo, "verify that record exists")
         Print #fileNo, addTab(1); "IF v_oid IS NULL THEN"
 ' ### IF IVK ###
         genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
         genSignalDdlWithParms("lrtLockNotFound", fileNo, 2, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(oid_in))")
         Print #fileNo, addTab(1); "END IF;"

         genProcSectionHeader(fileNo, "verify that record is not already locked by some other transaction")
         Print #fileNo, addTab(1); "IF (v_lrtOid IS NOT NULL) AND (v_lrtOid <> lrtOid_in) THEN"
         genProcSectionHeader(fileNo, "determine ID of user holding the lock", 2, True)
         Print #fileNo, addTab(2); "SET v_pubOwnerUserId = (SELECT USR."; g_anUserId; " FROM "; g_qualTabNameUser; " USR INNER JOIN "; qualTabNameLrt; " LRT ON LRT.UTROWN_OID = USR."; g_anOid; " WHERE LRT."; g_anOid; " = v_lrtOid);"
         Print #fileNo, addTab(2); "SET v_pubOwnerUserId = COALESCE(v_pubOwnerUserId, '<unknown>');"
         Print #fileNo,
 ' ### IF IVK ###
         genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
         genSignalDdlWithParms("lrtLockAlreadyLocked", fileNo, 2, , , , , , , , , , "v_pubOwnerUserId")
         Print #fileNo, addTab(1); "END IF;"

         genProcSectionHeader(fileNo, "if record is already locked by current transaction there is nothing to do")
         Print #fileNo, addTab(1); "IF v_lrtOid = lrtOid_in THEN"
 ' ### IF IVK ###
         genSpLogProcExit(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcExit fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(2); "RETURN 0;"
         Print #fileNo, addTab(1); "END IF;"

         genProcSectionHeader(fileNo, "copy the 'public record' into 'private table'")
         Print #fileNo, addTab(1); "INSERT INTO"
         Print #fileNo, addTab(2); qualTabNamePriv
         Print #fileNo, addTab(1); "("

         If forNl Then
           genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, edomListLrt)
         Else
           genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, forGen, edomListLrt)
         End If

         Print #fileNo, addTab(1); ")"
         Print #fileNo, addTab(1); "SELECT"

 ' ### IF IVK ###
         initAttributeTransformation(transformation, 3, , True, True)
 ' ### ELSE IVK ###
 '       initAttributeTransformation transformation, 2, , True, True
 ' ### ENDIF IVK ###
         setAttributeMapping(transformation, 1, conLrtState, "" & lrtStatusLocked)
         setAttributeMapping(transformation, 2, conInLrt, "lrtOid_in")
 ' ### IF IVK ###
         setAttributeMapping(transformation, 3, conPsOid, "psOid_in")
 ' ### ENDIF IVK ###

         If forNl Then
           genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, , edomListLrt)
         Else
           genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt)
         End If

         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualTabNamePub
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); g_anOid; " = oid_in"
 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); conPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ";"

         genProcSectionHeader(fileNo, "lock the 'public record' with this LRT-OID")
         Print #fileNo, addTab(1); "UPDATE"
         Print #fileNo, addTab(2); qualTabNamePub; " PUB"
         Print #fileNo, addTab(1); "SET"
         Print #fileNo, addTab(2); "PUB."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PUB."; g_anOid; " = oid_in"
 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PUB."; conPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ";"

         genProcSectionHeader(fileNo, "count the number of affected rows")
         Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

         genDdlForUpdateAffectedEntities(fileNo, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, _
           entityIdStr, ahClassIdStr, "lrtOid_in", 1, CStr(lrtStatusLocked), False)

 ' ### IF IVK ###
         genSpLogProcExit(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim


       ' ####################################################################################################################
       ' #    SP for LRT-LOCK via TempTable on records on a given table
       ' ####################################################################################################################

       qualProcName = _
         genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, "LRTLOCK", "TEMPTABLE")

       printSectionHeader("SP for TempTable-based LRT-LOCK on records in """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo)

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcName
       Print #fileNo, addTab(0); "("
       genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "LRT-OID used to lock the record")
 ' ### IF IVK ###
       genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure this row is supposed to correspond to")
 ' ### ENDIF IVK ###
       genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being locked (0 or 1)")
       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 0"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

 ' ### IF IVK ###
       If condenseData Then
         genSpLogDecl(fileNo, -1, True)
         genSpLogProcEnter(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
         genSpLogProcEscape(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
         genSignalDdl("lrtLockNotSup", fileNo, 1, unQualTabNamePub)
       Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNo, "declare conditions", , True)
         genCondDecl(fileNo, "alreadyExist", "42710")
         genSpLogDecl(fileNo)

         genProcSectionHeader(fileNo, "declare variables", , True)
         genSigMsgVarDecl(fileNo)
         genVarDecl(fileNo, "v_oid", g_dbtOid, "NULL")
         genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "NULL")
         genVarDecl(fileNo, "v_lrtEntityIdCount", "BIGINT", "0")
         genVarDecl(fileNo, "v_pubOwnerUserId", g_dbtUserId, "NULL")
         genSpLogDecl(fileNo)

 ' ### IF IVK ###
         genSpLogProcEnter(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNo, "declare continue handler")
         Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
         Print #fileNo, addTab(1); "BEGIN"
         Print #fileNo, addTab(2); "-- just ignore"
         Print #fileNo, addTab(1); "END;"

         genDdlForTempPrivClassIdOid(fileNo)

         Print #fileNo,
         Print #fileNo, addTab(1); "SET rowCount_out = 0;"

         genProcSectionHeader(fileNo, "determine if at least one record does not exist at all")
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "T."; g_anOid
         Print #fileNo, addTab(1); "INTO"
         Print #fileNo, addTab(2); "v_oid"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); "SESSION.PRIVCLASSIDOID T"
         Print #fileNo, addTab(1); "LEFT OUTER JOIN"
         Print #fileNo, addTab(2); qualTabNamePub; " PUB"
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); "PUB."; g_anOid; "= T."; g_anOid
         Print #fileNo, addTab(1); "JOIN"
         Print #fileNo, addTab(3); qualPdmTableViewName; " TBL"
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); "TBL.ENTITY_ID= T."; g_anCid
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "TBL.ENTITY_TYPE = '"; gc_acmEntityTypeKeyClass; "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "TBL."; g_anPdmTypedTableName; " = '"; getUnqualObjName(qualTabNamePub); "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "TBL."; g_anPdmFkSchemaName; " = '"; getSchemaName(qualTabNamePub); "'"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PUB."; g_anOid; " IS NULL"
         Print #fileNo, addTab(1); "FETCH FIRST ROW ONLY;"

         genProcSectionHeader(fileNo, "verify that records exist and are of given PS")
         Print #fileNo, addTab(1); "IF v_oid IS NOT NULL THEN"
 ' ### IF IVK ###
         genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
         genSignalDdlWithParms("objNotFound", fileNo, 2, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(v_oid))")
         Print #fileNo, addTab(1); "END IF;"

         If isPsTagged Then
           genProcSectionHeader(fileNo, "determine if at least one record does not exist in current PS")
           Print #fileNo, addTab(1); "SELECT"
           Print #fileNo, addTab(2); "T."; g_anOid
           Print #fileNo, addTab(1); "INTO"
           Print #fileNo, addTab(2); "v_oid"
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); "SESSION.PRIVCLASSIDOID T"
           Print #fileNo, addTab(1); "LEFT OUTER JOIN"
           Print #fileNo, addTab(2); qualTabNamePub; " PUB"
           Print #fileNo, addTab(1); "ON"
           Print #fileNo, addTab(2); "PUB."; g_anOid; "= T."; g_anOid
           Print #fileNo, addTab(1); "JOIN"
           Print #fileNo, addTab(3); qualPdmTableViewName; " TBL"
           Print #fileNo, addTab(1); "ON"
           Print #fileNo, addTab(2); "TBL.ENTITY_ID= T."; g_anCid
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "TBL.ENTITY_TYPE = '"; gc_acmEntityTypeKeyClass; "'"
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "TBL."; g_anPdmTypedTableName; " = '"; getUnqualObjName(qualTabNamePub); "'"
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "TBL."; g_anPdmFkSchemaName; " = '"; getSchemaName(qualTabNamePub); "'"
           Print #fileNo, addTab(1); "WHERE"
           Print #fileNo, addTab(2); "PUB."; conPsOid; " <> psOid_in"

 ' ### ENDIF IVK ###
           Print #fileNo, addTab(1); "FETCH FIRST ROW ONLY;"

           genProcSectionHeader(fileNo, "verify that records exist and are of given PS")
           Print #fileNo, addTab(1); "IF v_oid IS NOT NULL THEN"
 ' ### IF IVK ###
           genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
           genSignalDdlWithParms("objNotFoundInPs", fileNo, 2, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(v_oid))", "RTRIM(CHAR(psOid_in))")
           Print #fileNo, addTab(1); "END IF;"
         End If

         genProcSectionHeader(fileNo, "determine if at least one record is locked")
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "T."; g_anOid; ","
         Print #fileNo, addTab(2); "PUB."; g_anInLrt
         Print #fileNo, addTab(1); "INTO"
         Print #fileNo, addTab(2); "v_oid,"
         Print #fileNo, addTab(2); "v_lrtOid"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); "SESSION.PRIVCLASSIDOID T"
         Print #fileNo, addTab(1); "JOIN"
         Print #fileNo, addTab(2); qualTabNamePub; " PUB"
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); "PUB."; g_anOid; "= T."; g_anOid
         Print #fileNo, addTab(1); "JOIN"
         Print #fileNo, addTab(3); qualPdmTableViewName; " TBL"
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); "TBL.ENTITY_ID= T."; g_anCid
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "TBL.ENTITY_TYPE = '"; gc_acmEntityTypeKeyClass; "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "TBL."; g_anPdmTypedTableName; " = '"; getUnqualObjName(qualTabNamePub); "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "TBL."; g_anPdmFkSchemaName; " = '"; getSchemaName(qualTabNamePub); "'"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PUB."; g_anInLrt; " IS NOT NULL AND PUB."; g_anInLrt; " <> lrtOid_in"
         Print #fileNo, addTab(1); "FETCH FIRST ROW ONLY;"

         genProcSectionHeader(fileNo, "verify that record is not already locked by some other transaction")
         Print #fileNo, addTab(1); "IF v_lrtOid IS NOT NULL THEN"
         genProcSectionHeader(fileNo, "determine ID of user holding the lock", 2, True)
         Print #fileNo, addTab(2); "SET v_pubOwnerUserId = (SELECT USR."; g_anUserId; " FROM "; g_qualTabNameUser; " USR INNER JOIN "; qualTabNameLrt; " LRT ON LRT.UTROWN_OID = USR."; g_anOid; " WHERE LRT."; g_anOid; " = v_lrtOid);"
         Print #fileNo, addTab(2); "SET v_pubOwnerUserId = COALESCE(v_pubOwnerUserId, '<unknown>');"
         Print #fileNo,
 ' ### IF IVK ###
         genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
         genSignalDdlWithParms("lrtLockAlreadyLocked", fileNo, 2, , , , , , , , , , "v_pubOwnerUserId")
         Print #fileNo, addTab(1); "END IF;"

         genProcSectionHeader(fileNo, "copy the 'public records' into 'private table'")
         Print #fileNo, addTab(1); "INSERT INTO"
         Print #fileNo, addTab(2); qualTabNamePriv
         Print #fileNo, addTab(1); "("

         If forNl Then
           genNlsAttrDeclsForEntity(acmEntityIndex, acmEntityType, fileNo, "", , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, edomListLrt)
         Else
           genAttrListForEntity(acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, True, forGen, edomListLrt)
         End If

         Print #fileNo, addTab(1); ")"
         Print #fileNo, addTab(1); "SELECT"

 ' ### IF IVK ###
         initAttributeTransformation(transformation, 3, , True, True)
 ' ### ELSE IVK ###
 '       initAttributeTransformation transformation, 2, , True, True
 ' ### ENDIF IVK ###
         setAttributeMapping(transformation, 1, conLrtState, "" & lrtStatusLocked)
         setAttributeMapping(transformation, 2, conInLrt, "lrtOid_in")
 ' ### IF IVK ###
         setAttributeMapping(transformation, 3, conPsOid, "psOid_in")
 ' ### ENDIF IVK ###

         If forNl Then
           genNlsTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, True, , edomListLrt)
         Else
           genTransformedAttrListForEntity(acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , True, forGen, edomListLrt)
         End If

         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualTabNamePub
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); g_anOid; " IN (SELECT OID FROM SESSION.PRIVCLASSIDOID)"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "(INLRT IS NULL OR INLRT <> lrtOid_in)"
 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); conPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ";"

         genProcSectionHeader(fileNo, "lock the 'public records' with this LRT-OID")
         Print #fileNo, addTab(1); "UPDATE"
         Print #fileNo, addTab(2); qualTabNamePub; " PUB"
         Print #fileNo, addTab(1); "SET"
         Print #fileNo, addTab(2); "PUB."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PUB."; g_anOid; " IN (SELECT OID FROM SESSION.PRIVCLASSIDOID)"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "(PUB.INLRT IS NULL OR PUB.INLRT <> lrtOid_in)"
 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PUB."; conPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ";"

         genProcSectionHeader(fileNo, "count the number of affected rows")
         Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

         genDdlForUpdateAffectedEntities(fileNo, entityTypeDescr, acmEntityType, dbAcmEntityType, forGen, forNl, qualTabNameLrtAffectedEntity, _
           entityIdStr, ahClassIdStr, "lrtOid_in", 1, CStr(lrtStatusLocked), False)

 ' ### IF IVK ###
         genSpLogProcExit(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
 


       ' ####################################################################################################################
       ' #    SP for UNLOCK on record of given class
       ' ####################################################################################################################

       qualProcName = _
         genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, "LRTUNLOCK")

       printSectionHeader("SP for LRT-UNLOCK on """ & qualTabNamePub & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo)

       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE PROCEDURE"
       Print #fileNo, addTab(1); qualProcName
       Print #fileNo, addTab(0); "("
 ' ### IF IVK ###
       genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure this row is supposed to correspond to")
 ' ### ENDIF IVK ###
       genProcParm(fileNo, "IN", "oid_in", g_dbtOid, True, "OID of the row being unlocked")
       genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being unlocked (0 or 1)")
       Print #fileNo, addTab(0); ")"
       Print #fileNo, addTab(0); "RESULT SETS 0"
       Print #fileNo, addTab(0); "LANGUAGE SQL"
       Print #fileNo, addTab(0); "BEGIN"

 ' ### IF IVK ###
       If condenseData Then
         genSpLogDecl(fileNo, -1, True)
         genSpLogProcEnter(fileNo, qualProcName, ddlType, , "psOid_in", "oid_in", "rowCount_out")
         genSpLogProcEscape(fileNo, qualProcName, ddlType, , "psOid_in", "oid_in", "rowCount_out")
         genSignalDdl("lrtUnLockNotSup", fileNo, 1, unQualTabNamePub)
       Else
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### -2
 ' ### ENDIF IVK ###
         genProcSectionHeader(fileNo, "declare variables", , True)
         genSigMsgVarDecl(fileNo)
         genVarDecl(fileNo, "v_oid", g_dbtOid, "NULL")
         genVarDecl(fileNo, "v_lrtOid", g_dbtOid, "NULL")
         genVarDecl(fileNo, "v_lrtState", g_dbtEnumId, "NULL")
         genSpLogDecl(fileNo)

 ' ### IF IVK ###
         genSpLogProcEnter(fileNo, qualProcName, ddlType, , "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEnter fileNo, qualProcName, ddlType, , "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###

         Print #fileNo,
         Print #fileNo, addTab(1); "SET rowCount_out = 0;"

         genProcSectionHeader(fileNo, "determine existance and 'current owner' of record to unlock")
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "PUB."; g_anOid; ","
         Print #fileNo, addTab(2); "PUB."; g_anInLrt
         Print #fileNo, addTab(1); "INTO"
         Print #fileNo, addTab(2); "v_oid,"
         Print #fileNo, addTab(2); "v_lrtOid"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualTabNamePub; " PUB"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PUB."; g_anOid; " = oid_in"
 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PUB."; conPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ";"

         genProcSectionHeader(fileNo, "verify that record exists")
         Print #fileNo, addTab(1); "IF v_oid IS NULL THEN"
 ' ### IF IVK ###
         genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
         genSignalDdlWithParms("lrtUnlockNotFound", fileNo, 2, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(oid_in))")
         Print #fileNo, addTab(1); "END IF;"

         genProcSectionHeader(fileNo, "if record is not locked by any transaction there is nothing to do")
         Print #fileNo, addTab(1); "IF v_lrtOid IS NULL THEN"
         Print #fileNo, addTab(2); "RETURN 0;"
         Print #fileNo, addTab(1); "END IF;"

         genProcSectionHeader(fileNo, "determine LRTSTATE of record to unlock")
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "PRIV."; g_anLrtState
         Print #fileNo, addTab(1); "INTO"
         Print #fileNo, addTab(2); "v_lrtState"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualTabNamePriv; " PRIV"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PRIV."; g_anOid; " = oid_in"
 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PRIV."; conPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ";"

         Print #fileNo, addTab(1); "IF (v_lrtState IS NOT NULL) AND (v_lrtState  <> "; CStr(lrtStatusLocked); ") THEN"
 ' ### IF IVK ###
         genSpLogProcEscape(fileNo, qualProcName, ddlType, -2, "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
         genSignalDdlWithParms("lrtUnlockChPending", fileNo, 2, unQualTabNamePub, , , , , , , , , "RTRIM(CHAR(oid_in))")
         Print #fileNo, addTab(1); "END IF;"

         genProcSectionHeader(fileNo, "unlock the 'public record'")
         Print #fileNo, addTab(1); "UPDATE"
         Print #fileNo, addTab(2); qualTabNamePub; " PUB"
         Print #fileNo, addTab(1); "SET"
         Print #fileNo, addTab(2); "PUB."; g_anInLrt; " = CAST(NULL AS "; g_dbtOid; ")"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PUB."; g_anOid; " = oid_in"
 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PUB."; conPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ";"

         genProcSectionHeader(fileNo, "remove 'private record'")
         Print #fileNo, addTab(1); "DELETE FROM"
         Print #fileNo, addTab(2); qualTabNamePriv; " PRIV"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "PRIV."; g_anOid; " = oid_in"
 ' ### IF IVK ###
         If isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "PRIV."; conPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###
         Print #fileNo, addTab(1); ";"

         genProcSectionHeader(fileNo, "count the number of affected rows")
         Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

 ' ### IF IVK ###
         genSpLogProcExit(fileNo, qualProcName, ddlType, , "psOid_in", "oid_in", "rowCount_out")
 ' ### ELSE IVK ###
 '       genSpLogProcExit fileNo, qualProcName, ddlType, , "oid_in", "rowCount_out"
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
       End If
 ' ### ELSE IVK ###
 ' ### INDENT IVK ### 0
 ' ### ENDIF IVK ###

       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
     End If

     If Not ignoreForChangelog Then
       Dim parFkAttrName As String
       parFkAttrName = genAttrName(conOid, ddlType, entityShortName)

       Dim fillRestrictedColSetOnly As Boolean
       Dim spInfix As String
       For i = 1 To 2
         fillRestrictedColSetOnly = (i = 2)
         spInfix = IIf(fillRestrictedColSetOnly, "_RED", "")

         ' ####################################################################################################################
         ' #    SP for retrieving LRT-Log
         ' ####################################################################################################################

         qualProcName = _
           genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , forNl, spnLrtGetLog & spInfix)

         printSectionHeader("SP for retrieving LRT-Log on """ & qualTabNamePub & IIf(fillRestrictedColSetOnly, " (restricted column set)", "") & _
                            """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo)

         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE PROCEDURE"
         Print #fileNo, addTab(1); qualProcName
         Print #fileNo, addTab(0); "("
         genProcParm(fileNo, "IN", "lrtOid_in", g_dbtLrtId, True, "OID of the LRT to retrieve the Log-Records for")
 ' ### IF IVK ###
         genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure the LRT corresponds to")
 ' ### ENDIF IVK ###
         If Not fillRestrictedColSetOnly Then
           genProcParm(fileNo, "IN", "languageId_in", g_dbtEnumId, True, "ID of language to use for language-specific columns")
         End If
         genProcParm(fileNo, "IN", "startTime_in", "TIMESTAMP", True, "(optional) retrieve only records for updates past this timestamp")
         If Not fillRestrictedColSetOnly Then
           genProcParm(fileNo, "IN", "maxRowCount_in", "INTEGER", True, "maximum number of rows to add to the change log")
         End If
         genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows placed in the log")
         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); "RESULT SETS 0"
         Print #fileNo, addTab(0); "LANGUAGE SQL"
         Print #fileNo, addTab(0); "BEGIN"

         genProcSectionHeader(fileNo, "declare conditions", , True)
         genCondDecl(fileNo, "notFound", "02000")
         genCondDecl(fileNo, "alreadyExist", "42710")

         genSpLogDecl(fileNo, , True)

         genProcSectionHeader(fileNo, "declare continue handler")
         Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
         Print #fileNo, addTab(1); "BEGIN"
         Print #fileNo, addTab(2); "-- the temporary table holding the LRT-Log already exists in this session - ignore this"
         Print #fileNo, addTab(1); "END;"
         Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
         Print #fileNo, addTab(1); "BEGIN"
         Print #fileNo, addTab(2); "-- just ignore"
         Print #fileNo, addTab(1); "END;"

         genDdlForTempLrtLog(fileNo, , fillRestrictedColSetOnly, False)

 ' ### IF IVK ###
         If fillRestrictedColSetOnly Then
           genSpLogProcEnter(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "#startTime_in", "rowCount_out")
         Else
           genSpLogProcEnter(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "languageId_in", "#startTime_in", "maxRowCount_in", "rowCount_out")
         End If
 ' ### ELSE IVK ###
 '       If fillRestrictedColSetOnly Then
 '         genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "#startTime_in", "rowCount_out"
 '       Else
 '         genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "languageId_in", "#startTime_in", "maxRowCount_in", "rowCount_out"
 '       End If
 ' ### ENDIF IVK ###

         genProcSectionHeader(fileNo, "process inserts, updates deletes related to '" & sectionName & "." & entityName & "'")

         Dim indent As Integer
         indent = 1

         If Not fillRestrictedColSetOnly Then
           Print #fileNo, addTab(1); "IF COALESCE(maxRowCount_in,1) > 0 THEN"
           genProcSectionHeader(fileNo, "retrieve records to be returned to application", 2, True)
           indent = 2
         End If

         Print #fileNo, addTab(indent + 0); "INSERT INTO"
         Print #fileNo, addTab(indent + 1); tempTabNameLrtLog
         Print #fileNo, addTab(indent + 0); "("
         If Not fillRestrictedColSetOnly Then
           Print #fileNo, addTab(indent + 1); "displayMe,"
           Print #fileNo, addTab(indent + 1); "orParEntityId,"
         End If
         Print #fileNo, addTab(indent + 1); "entityId,"
         Print #fileNo, addTab(indent + 1); "entityType,"

         If Not fillRestrictedColSetOnly Then
 ' ### IF IVK ###
           Print #fileNo, addTab(indent + 1); "displayCategory,"
 ' ### ENDIF IVK ###
           If hasNlLabelAttr Then
             Print #fileNo, addTab(indent + 1); "label,"
           End If
         End If

         Print #fileNo, addTab(indent + 1); "gen,"
         Print #fileNo, addTab(indent + 1); "isNl,"
         Print #fileNo, addTab(indent + 1); "oid,"
         If acmEntityType = eactRelationship And Not forNl Then
           If Not fillRestrictedColSetOnly Then
             Print #fileNo, addTab(indent + 1); "refClassId1,"
           End If
           Print #fileNo, addTab(indent + 1); "refObjectId1,"
           If Not fillRestrictedColSetOnly Then
             Print #fileNo, addTab(indent + 1); "refClassId2,"
           End If
           Print #fileNo, addTab(indent + 1); "refObjectId2,"
         End If
 ' ### IF IVK ###
         If Not forNl Then
           Print #fileNo, addTab(indent + 1); "code,"
         End If
 ' ### ENDIF IVK ###

         If Not fillRestrictedColSetOnly Then
 ' ### IF IVK ###
           If Not forNl Then
             Print #fileNo, addTab(indent + 1); "sr0Context,"
           End If
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
           Print #fileNo, addTab(indent + 1); "baseCode,"
           Print #fileNo, addTab(indent + 1); "baseEndSlot,"
 ' ### ENDIF IVK ###
           Print #fileNo, addTab(indent + 1); "validFrom,"
           Print #fileNo, addTab(indent + 1); "validTo,"
         End If

         Print #fileNo, addTab(indent + 1); "operation,"
         Print #fileNo, addTab(indent + 1); "ts"
         Print #fileNo, addTab(indent + 0); ")"
         Print #fileNo, addTab(indent + 0); "SELECT"

         If Not fillRestrictedColSetOnly Then
           ' displayMe
           Print #fileNo, addTab(indent + 1); "(CASE WHEN COALESCE(maxRowCount_in,1) > 0 THEN 1 ELSE 0 END),"
           ' orParEntityId
           Print #fileNo, addTab(indent + 1); "'"; entityIdStr; "',"
         End If

         ' entityId
         If acmEntityType = eactClass Then
           If hasOwnTable Then
             Print #fileNo, addTab(indent + 1); "'"; entityIdStr; "',"
           Else
             If forGen Then
               Print #fileNo, addTab(indent + 1); "COALESCE(PAR_PRIV."; g_anCid; ",PAR_PUB."; g_anCid; "),"
             Else
               Print #fileNo, addTab(indent + 1); "PRIV."; g_anCid; ","
             End If
           End If
         Else
           Print #fileNo, addTab(indent + 1); "'"; entityIdStr; "',"
         End If

         ' entityType
         Print #fileNo, addTab(indent + 1); "'"; dbAcmEntityType; "',"

         If Not fillRestrictedColSetOnly Then
 ' ### IF IVK ###
           ' displayCategory
           Print #fileNo, addTab(indent + 1); IIf(lrtClassification = "", "CAST(NULL AS VARCHAR(1)),", "'" & lrtClassification & "',")

 ' ### ENDIF IVK ###
           If hasNlLabelAttr Then
             ' label
 ' ### IF IVK ###
             If labelIsNationalizable Then
               Print #fileNo, addTab(indent + 1); "COALESCE((CASE NL_PRIV.LABEL_ISNATACTIVE WHEN 1 THEN NL_PRIV.LABEL_NATIONAL ELSE NL_PRIV.LABEL END),"
               Print #fileNo, addTab(indent + 1); "         (CASE NL_PUB. LABEL_ISNATACTIVE WHEN 1 THEN NL_PUB. LABEL_NATIONAL ELSE NL_PUB. LABEL END)),"
             Else
               Print #fileNo, addTab(indent + 1); "COALESCE(NL_PRIV.LABEL, NL_PUB.LABEL),"
             End If
 ' ### ELSE IVK ###
 '           Print #fileNo, addTab(indent + 1); "COALESCE(NL_PRIV.LABEL, NL_PUB.LABEL),"
 ' ### ENDIF IVK ###
           End If
         End If

         ' gen
         Print #fileNo, addTab(indent + 1); IIf(forGen, "1,", "0,")

         ' isNl
         Print #fileNo, addTab(indent + 1); IIf(forNl, "1,", "0,")

         ' OID
         Print #fileNo, addTab(indent + 1); "PRIV."; g_anOid; ","

         If acmEntityType = eactRelationship And Not forNl Then
           If Not fillRestrictedColSetOnly Then
             ' refClassId1
             Print #fileNo, addTab(indent + 1); "'"; relLeftClassIdStr; "',"
           End If
           ' refObjectId1
           Print #fileNo, addTab(indent + 1); "PRIV."; relLeftFk; ","
           If Not fillRestrictedColSetOnly Then
             ' refClassId2
             Print #fileNo, addTab(indent + 1); "'"; relRightClassIdStr; "',"
           End If
           ' refObjectId2
           Print #fileNo, addTab(indent + 1); "PRIV."; relRightFk; ","
         End If
 ' ### IF IVK ###

         ' code
         If Not forNl Then
           genLrtLogColDdl(fileNo, attrMapping, "code", "NULL", forGen And Not hasNoIdentity, ddlType, , "VARCHAR(1)", indent + 1, , "PRIV")
         End If

         Dim foundSr0Context As Boolean
         Dim s0_01TargetClassIndex As Integer
         Dim s0_01FkAttrName As String
         Dim s0_01QualObjName As String
         Dim s0_02TargetClassIndex As Integer
         Dim s0_02FkAttrName As String
         Dim s0_02QualObjName As String
         Dim s0_03TargetClassIndex As Integer
         Dim s0_03FkAttrName As String
         Dim s0_03QualObjName As String
         Dim s0_04TargetClassIndex As Integer
         Dim s0_04FkAttrName As String
         Dim s0_04QualObjName As String
         Dim s0_05TargetClassIndex As Integer
         Dim s0_05FkAttrName As String
         Dim s0_05QualObjName As String
         Dim s0_06TargetClassIndex As Integer
         Dim s0_06FkAttrName As String
         Dim s0_06QualObjName As String
         Dim s0_07TargetClassIndex As Integer
         Dim s0_07FkAttrName As String
         Dim s0_07QualObjName As String
         Dim s0_08TargetClassIndex As Integer
         Dim s0_08FkAttrName As String
         Dim s0_08QualObjName As String
         Dim s0_09TargetClassIndex As Integer
         Dim s0_09FkAttrName As String
         Dim s0_09QualObjName As String
         Dim s0_10TargetClassIndex As Integer
         Dim s0_10FkAttrName As String
         Dim s0_10QualObjName As String
         Dim bcTargetClassIndex As Integer
         Dim bcFkAttrName As String
         Dim bcQualObjName As String
         Dim beTargetClassIndex As Integer
         Dim beFkAttrName As String
         Dim beQualObjName As String

         s0_01TargetClassIndex = 0
         s0_02TargetClassIndex = 0
         s0_03TargetClassIndex = 0
         s0_04TargetClassIndex = 0
         s0_05TargetClassIndex = 0
         s0_06TargetClassIndex = 0
         s0_07TargetClassIndex = 0
         s0_08TargetClassIndex = 0
         s0_09TargetClassIndex = 0
         s0_10TargetClassIndex = 0
         bcTargetClassIndex = 0
         beTargetClassIndex = 0
 ' ### ENDIF IVK ###

         If Not fillRestrictedColSetOnly Then
 ' ### IF IVK ###
           If forNl Then
                 foundSr0Context = False
           Else
                 foundSr0Context = genLrtLogColDdl(fileNo, attrMapping, "sr0Context", "NULL", forGen And Not hasNoIdentity, ddlType, , "VARCHAR(1)", indent + 1, , "PRIV")
           End If

           If lrtLogRetrieveSr0CodesFromSr0Context Then
             If foundSr0Context Then
               Print #fileNo, addTab(indent + 1); "T.codeNumber01,                          -- sr0Code01"
               Print #fileNo, addTab(indent + 1); "T.codeNumber02,                          -- sr0Code02"
               Print #fileNo, addTab(indent + 1); "T.codeNumber03,                          -- sr0Code03"
               Print #fileNo, addTab(indent + 1); "T.codeNumber04,                          -- sr0Code04"
               Print #fileNo, addTab(indent + 1); "T.codeNumber05,                          -- sr0Code05"
               Print #fileNo, addTab(indent + 1); "T.codeNumber06,                          -- sr0Code06"
               Print #fileNo, addTab(indent + 1); "T.codeNumber07,                          -- sr0Code07"
               Print #fileNo, addTab(indent + 1); "T.codeNumber08,                          -- sr0Code08"
               Print #fileNo, addTab(indent + 1); "T.codeNumber08,                          -- sr0Code09"
               Print #fileNo, addTab(indent + 1); "T.codeNumber10,                          -- sr0Code10"
             Else
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code01"
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code02"
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code03"
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code04"
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code05"
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code06"
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code07"
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code08"
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code09"
               Print #fileNo, addTab(indent + 1); "CAST(NULL AS VARCHAR(1)),                -- sr0Code10"
             End If
           Else
             ' sr0Code01
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code01", "S01", s0_01TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_01QualObjName, s0_01FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)

             ' sr0Code02
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code02", "S02", s0_02TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_02QualObjName, s0_02FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)

             ' sr0Code03
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code03", "S03", s0_03TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_03QualObjName, s0_03FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)

             ' sr0Code04
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code04", "S04", s0_04TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_04QualObjName, s0_04FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)

             ' sr0Code05
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code05", "S05", s0_05TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_05QualObjName, s0_05FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)

             ' sr0Code06
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code06", "S06", s0_06TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_06QualObjName, s0_06FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)

             ' sr0Code07
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code07", "S07", s0_07TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_07QualObjName, s0_07FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)

             ' sr0Code08
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code08", "S08", s0_08TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_08QualObjName, s0_08FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)

             ' sr0Code09
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code09", "S09", s0_09TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_09QualObjName, s0_09FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)

             ' sr0Code10
             genLrtLogRelColDdl(fileNo, relRefs, "sr0Code10", "S10", s0_10TargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
               s0_10QualObjName, s0_10FkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)
           End If

           ' baseCodeNumber
           genLrtLogRelColDdl(fileNo, relRefs, "baseCodeNumber", "BC", bcTargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
             bcQualObjName, bcFkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent + 1)
 
           ' baseEndSlot
           genLrtLogRelColDdl(fileNo, relRefs, "baseEndSlot", "BE", beTargetClassIndex, "CAST(NULL AS VARCHAR(1))", _
             beQualObjName, beFkAttrName, eclLrt, ddlType, thisOrgIndex, thisPoolIndex, forGen, "CHAR(60)", indent + 1, , , , , True)

           If beTargetClassIndex > 0 Then
             ' actually not needed here since this will be overwritten below
             Print #fileNo, addTab(indent + 1); "CAST(PRIV."; beFkAttrName; " AS CHAR(22)),"
           End If

 ' ### ENDIF IVK ###
 ' ### IF IVK ###
           If isGenForming And (forGen Or hasNoIdentity) Then
 ' ### ELSE IVK ###
 '         If isGenForming And forGen Then
 ' ### ENDIF IVK ###
             ' validFrom
             Print #fileNo, addTab(indent + 1); "PRIV."; g_anValidFrom; ","
             ' validTo
             Print #fileNo, addTab(indent + 1); "PRIV."; g_anValidTo; ","
           Else
             Print #fileNo, addTab(indent + 1); "CAST(NULL AS DATE),"
             Print #fileNo, addTab(indent + 1); "CAST(NULL AS DATE),"
           End If
         End If

         Print #fileNo, addTab(indent + 1); "PRIV.LRTSTATE,"
         If forNl Then
             Print #fileNo, addTab(indent + 1); "COALESCE(PAR_PRIV."; g_anLastUpdateTimestamp; ", PAR_PRIV."; g_anCreateTimestamp; ")"
         Else
             Print #fileNo, addTab(indent + 1); "COALESCE(PRIV."; g_anLastUpdateTimestamp; ", PRIV."; g_anCreateTimestamp; ")"
         End If
         Print #fileNo, addTab(indent + 0); "FROM"
         Print #fileNo, addTab(indent + 1); qualTabNamePriv; " PRIV"
 ' ### IF IVK ###

         If lrtLogRetrieveSr0CodesFromSr0Context And foundSr0Context And Not fillRestrictedColSetOnly Then
           Dim qualFuncNameParseSr0Context As String
           qualFuncNameParseSr0Context = genQualFuncName(g_sectionIndexMeta, udfnParseSr0Context, ddlType, , , , , , True)

           Print #fileNo, addTab(indent + 0); "INNER JOIN"
           Print #fileNo, addTab(indent + 1); "TABLE("; qualFuncNameParseSr0Context; "(PRIV.sr0Context)) T"
           Print #fileNo, addTab(indent + 0); "ON"
           Print #fileNo, addTab(indent + 1); "(1=1)"
         End If
 ' ### ENDIF IVK ###

         If hasNlLabelAttr And Not fillRestrictedColSetOnly Then
           Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
           Print #fileNo, addTab(indent + 1); qualTabNameNlPriv; " NL_PRIV"
           Print #fileNo, addTab(indent + 0); "ON"
           Print #fileNo, addTab(indent + 1); "NL_PRIV."; parFkAttrName; " = PRIV."; g_anOid
           Print #fileNo, addTab(indent + 2); "AND"
           Print #fileNo, addTab(indent + 1); "NL_PRIV."; g_anLanguageId; " = languageId_in"

           Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
           Print #fileNo, addTab(indent + 1); qualTabNameNlPub; " NL_PUB"
           Print #fileNo, addTab(indent + 0); "ON"
           Print #fileNo, addTab(indent + 1); "NL_PUB."; parFkAttrName; " = PRIV."; g_anOid
           Print #fileNo, addTab(indent + 2); "AND"
           Print #fileNo, addTab(indent + 1); "NL_PUB."; g_anLanguageId; " = languageId_in"
         End If

         If forGen Or forNl Then
           Dim qualTabNameAggHeadPriv As String
           qualTabNameAggHeadPriv = genQualTabNameByEntityIndex(aggHeadClassIndex, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True)

           Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
           Print #fileNo, addTab(indent + 1); qualTabNameAggHeadPriv; " PAR_PRIV"
           Print #fileNo, addTab(indent + 0); "ON"
           Print #fileNo, addTab(indent + 1); "PRIV."; "AHOID"; " = PAR_PRIV."; g_anOid
           Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
           Print #fileNo, addTab(indent + 1); qualTabNameAggHeadPub; " PAR_PUB"
           Print #fileNo, addTab(indent + 0); "ON"
           Print #fileNo, addTab(indent + 1); "PRIV."; "AHOID"; " = PAR_PUB."; g_anOid
         End If
 ' ### IF IVK ###

         If bcTargetClassIndex > 0 Then
           Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
           Print #fileNo, addTab(indent + 1); bcQualObjName; " BC"
           Print #fileNo, addTab(indent + 0); "ON"
           Print #fileNo, addTab(indent + 1); "PRIV."; bcFkAttrName; " = BC."; g_anOid
         End If

         If Not lrtLogRetrieveSr0CodesFromSr0Context Then
           If s0_01TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_01QualObjName; " S01"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_01FkAttrName; " = S01."; g_anOid
           End If

           If s0_02TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_02QualObjName; " S02"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_02FkAttrName; " = S02."; g_anOid
           End If

           If s0_03TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_03QualObjName; " S03"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_03FkAttrName; " = S03."; g_anOid
           End If

           If s0_04TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_04QualObjName; " S04"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_04FkAttrName; " = S04."; g_anOid
           End If

           If s0_05TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_05QualObjName; " S05"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_05FkAttrName; " = S05."; g_anOid
           End If

           If s0_06TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_06QualObjName; " S06"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_06FkAttrName; " = S06."; g_anOid
           End If

           If s0_07TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_07QualObjName; " S07"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_07FkAttrName; " = S07."; g_anOid
           End If

           If s0_08TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_08QualObjName; " S08"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_08FkAttrName; " = S08."; g_anOid
           End If

           If s0_09TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_09QualObjName; " S09"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_09FkAttrName; " = S09."; g_anOid
           End If

           If s0_10TargetClassIndex > 0 Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); s0_10QualObjName; " S10"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; s0_10FkAttrName; " = S10."; g_anOid
           End If
         End If
 ' ### ENDIF IVK ###

         Print #fileNo, addTab(indent + 0); "WHERE"
         Print #fileNo, addTab(indent + 1); "PRIV."; g_anInLrt; " = lrtOid_in"

         If fillRestrictedColSetOnly Then
           Print #fileNo, addTab(indent + 2); "AND"
           Print #fileNo, addTab(indent + 1); "PRIV.LRTSTATE <> "; CStr(lrtStatusLocked)
         End If
 ' ### IF IVK ###

         If isPsTagged Then
           Print #fileNo, addTab(indent + 2); "AND"
           Print #fileNo, addTab(indent + 1); "PRIV."; g_anPsOid; " = psOid_in"
         End If
 ' ### ENDIF IVK ###

         Print #fileNo, addTab(indent + 2); "AND"
         Print #fileNo, addTab(indent + 1); "("
         Print #fileNo, addTab(indent + 2); "(startTime_in IS NULL)"
         Print #fileNo, addTab(indent + 3); "OR"

         Dim alias As String
         alias = "PRIV"
         If forNl Then
             alias = "PAR_PRIV"
         End If

         If fillRestrictedColSetOnly Then
           Print #fileNo, addTab(indent + 2); "("
           Print #fileNo, addTab(indent + 3); "(COALESCE("; alias; "."; g_anLastUpdateTimestamp; ", "; alias; "."; g_anCreateTimestamp; ") >= (startTime_in - 500000 MICROSECONDS))"
           Print #fileNo, addTab(indent + 4); "AND"
           Print #fileNo, addTab(indent + 3); "((PRIV.LRTSTATE = "; CStr(lrtStatusDeleted); ") OR (COALESCE("; alias; "."; g_anLastUpdateTimestamp; ", "; alias; "."; g_anCreateTimestamp; ") >= startTime_in))"
           Print #fileNo, addTab(indent + 2); ")"
         Else
           Print #fileNo, addTab(indent + 2); "(COALESCE("; alias; "."; g_anLastUpdateTimestamp; ", "; alias; "."; g_anCreateTimestamp; ") >= startTime_in)"
         End If

         Print #fileNo, addTab(indent + 1); ")"

         If fillRestrictedColSetOnly Then
           Print #fileNo, addTab(indent + 0); ";"
         Else
           Print #fileNo, addTab(indent + 0); "WITH UR;"
         End If

         genProcSectionHeader(fileNo, "count the number of affected rows", indent)
         Print #fileNo, addTab(indent + 0); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

         If Not fillRestrictedColSetOnly Then
           Print #fileNo, addTab(1); "ELSE"
           genProcSectionHeader(fileNo, "retrieve records NOT to be returned to application", 2, True)

           Print #fileNo, addTab(indent + 0); "INSERT INTO"
           Print #fileNo, addTab(indent + 1); tempTabNameLrtLog
           Print #fileNo, addTab(indent + 0); "("
           Print #fileNo, addTab(indent + 1); "displayMe,"
           Print #fileNo, addTab(indent + 1); "orParEntityId,"
           Print #fileNo, addTab(indent + 1); "entityType,"
           Print #fileNo, addTab(indent + 1); "gen,"
           Print #fileNo, addTab(indent + 1); "isNl,"
           Print #fileNo, addTab(indent + 1); "oid"
           Print #fileNo, addTab(indent + 0); ")"
           Print #fileNo, addTab(indent + 0); "SELECT"

           ' displayMe
           Print #fileNo, addTab(indent + 1); "0,"
           ' orParEntityId
           Print #fileNo, addTab(indent + 1); "'"; entityIdStr; "',"
           ' entityType
           Print #fileNo, addTab(indent + 1); "'"; dbAcmEntityType; "',"
           ' gen
           Print #fileNo, addTab(indent + 1); IIf(forGen, "1,", "0,")
           ' isNl
           Print #fileNo, addTab(indent + 1); IIf(forNl, "1,", "0,")
           ' OID
           Print #fileNo, addTab(indent + 1); "PRIV."; g_anOid

           Print #fileNo, addTab(indent + 0); "FROM"
           Print #fileNo, addTab(indent + 1); qualTabNamePriv; " PRIV"

           If forNl Then
             Print #fileNo, addTab(indent + 0); "LEFT OUTER JOIN"
             Print #fileNo, addTab(indent + 1); qualTabNameAggHeadPriv; " PAR_PRIV"
             Print #fileNo, addTab(indent + 0); "ON"
             Print #fileNo, addTab(indent + 1); "PRIV."; "AHOID"; " = PAR_PRIV."; g_anOid
           End If

           Print #fileNo, addTab(indent + 0); "WHERE"
           Print #fileNo, addTab(indent + 1); "PRIV."; g_anInLrt; " = lrtOid_in"
 ' ### IF IVK ###

           If isPsTagged Then
             Print #fileNo, addTab(indent + 2); "AND"
             Print #fileNo, addTab(indent + 1); "PRIV."; g_anPsOid; " = psOid_in"
           End If
 ' ### ENDIF IVK ###

           Print #fileNo, addTab(indent + 2); "AND"
           Print #fileNo, addTab(indent + 1); "("
           Print #fileNo, addTab(indent + 2); "(startTime_in IS NULL)"
           Print #fileNo, addTab(indent + 3); "OR"
           If forNl Then
             Print #fileNo, addTab(indent + 2); "(COALESCE(PAR_PRIV."; g_anLastUpdateTimestamp; ", PAR_PRIV."; g_anCreateTimestamp; ") >= startTime_in)"
           Else
             Print #fileNo, addTab(indent + 2); "(COALESCE(PRIV."; g_anLastUpdateTimestamp; ", PRIV."; g_anCreateTimestamp; ") >= startTime_in)"
           End If
           Print #fileNo, addTab(indent + 1); ")"

           Print #fileNo, addTab(indent + 0); "WITH UR;"

           genProcSectionHeader(fileNo, "count the number of affected rows", indent)
           Print #fileNo, addTab(indent + 0); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

           Print #fileNo, addTab(1); "END IF;"
         End If

 ' ### IF IVK ###
         If Not condenseData And (((aggHeadClassIndex > 0) And ((aggHeadClassIndex <> acmEntityIndex) Or forGen Or forNl) And ahHasChangeComment) Or (implicitelyGenChangeComment Or hasNlAttributes)) Then
 ' ### ELSE IVK ###
 '       If (((aggHeadClassIndex > 0) And ((aggHeadClassIndex <> acmEntityIndex) Or forGen Or forNl)) Or (implicitelyGenChangeComment Or hasNlAttributes)) Then
 ' ### ENDIF IVK ###
           indent = 1
           If Not fillRestrictedColSetOnly Then
             genProcSectionHeader(fileNo, "retrieve details for records to be returned to application", 1)
             Print #fileNo, addTab(1); "IF COALESCE(maxRowCount_in,1) > 0 THEN"
             indent = 2
           End If

           If Not fillRestrictedColSetOnly And qualTabNameAggHeadNlPriv <> "" Then
             If (aggHeadClassIndex > 0) And ((aggHeadClassIndex <> acmEntityIndex) Or forGen Or forNl) Then
               genProcSectionHeader(fileNo, "retrieve CHANGECOMMENT from Aggregate Head", indent, True)
             Else
               genProcSectionHeader(fileNo, "determine CHANGECOMMENT", indent, True)
             End If
             Print #fileNo, addTab(indent + 0); "UPDATE"
             Print #fileNo, addTab(indent + 1); tempTabNameLrtLog; " L"
             Print #fileNo, addTab(indent + 0); "SET"
             Print #fileNo, addTab(indent + 1); "L.comment = ("
             Print #fileNo, addTab(indent + 2); "SELECT"
             Print #fileNo, addTab(indent + 3); "CNL_PRIV."; conChangeComment
             Print #fileNo, addTab(indent + 2); "FROM"

             If (aggHeadClassIndex > 0) And ((aggHeadClassIndex <> acmEntityIndex) Or forGen Or forNl) Then
               Print #fileNo, addTab(indent + 3); qualTabNameAggHeadNlPriv; " CNL_PRIV"
               Print #fileNo, addTab(indent + 2); "INNER JOIN"
               Print #fileNo, addTab(indent + 3); qualTabNamePriv; " PRIV"
               Print #fileNo, addTab(indent + 2); "ON"
               Print #fileNo, addTab(indent + 3); "PRIV."; g_anAhOid; " = CNL_PRIV."; aggHeadFkAttrName
               Print #fileNo, addTab(indent + 2); "WHERE"
               Print #fileNo, addTab(indent + 3); "PRIV."; g_anOid; " = L.oid"
             Else
               Print #fileNo, addTab(indent + 3); qualTabNameNlPriv; " CNL_PRIV"
               Print #fileNo, addTab(indent + 2); "WHERE"
               Print #fileNo, addTab(indent + 3); "CNL_PRIV."; aggHeadFkAttrName; " = L.oid"
             End If

             Print #fileNo, addTab(indent + 4); "AND"
             Print #fileNo, addTab(indent + 3); "CNL_PRIV."; g_anLanguageId; " = languageId_in"
             Print #fileNo, addTab(indent + 2); "ORDER BY"
             Print #fileNo, addTab(indent + 3); "CNL_PRIV.LRTSTATE ASC"
             Print #fileNo, addTab(indent + 2); "FETCH FIRST 1 ROW ONLY"
             Print #fileNo, addTab(indent + 1); ")"
             Print #fileNo, addTab(indent + 0); "WHERE"
             Print #fileNo, addTab(indent + 1); "L.comment IS NULL"
             Print #fileNo, addTab(indent + 2); "AND"
             Print #fileNo, addTab(indent + 1); "L.orParEntityId = '"; entityIdStr; "'"
             Print #fileNo, addTab(indent + 2); "AND"
             Print #fileNo, addTab(indent + 1); "L.entityType = '"; dbAcmEntityType; "'"
             If fillRestrictedColSetOnly Then
               Print #fileNo, addTab(indent + 0); ";"
             Else
               Print #fileNo, addTab(indent + 0); "WITH UR;"
             End If
           End If

 ' ### IF IVK ###
           If beTargetClassIndex > 0 Then
             Dim beQualGenObjName As String
             Dim beQualGenNlObjName As String

               beQualGenObjName = genQualViewNameByClassIndex(g_classes.descriptors(beTargetClassIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True, g_classes.descriptors(beTargetClassIndex).useMqtToImplementLrt)
               beQualGenNlObjName = genQualViewNameByClassIndex(g_classes.descriptors(beTargetClassIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, True, True, g_classes.descriptors(beTargetClassIndex).useMqtToImplementLrt, True)

             genProcSectionHeader(fileNo, "retrieve LABEL from BaseEndSlot", indent)
             Print #fileNo, addTab(indent + 0); "UPDATE"
             Print #fileNo, addTab(indent + 1); tempTabNameLrtLog; " L"
             Print #fileNo, addTab(indent + 0); "SET"
             Print #fileNo, addTab(indent + 1); "L.t_baseEndSlotGenOID = ("

             Print #fileNo, addTab(indent + 2); "SELECT"
             Print #fileNo, addTab(indent + 3); "BEG."; g_anOid
             Print #fileNo, addTab(indent + 2); "FROM"
             Print #fileNo, addTab(indent + 3); qualTabNamePriv; " PRIV"
             Print #fileNo, addTab(indent + 2); "INNER JOIN"
             Print #fileNo, addTab(indent + 3); beQualGenObjName; " BEG"
             Print #fileNo, addTab(indent + 2); "ON"
             Print #fileNo, addTab(indent + 3); "PRIV."; beFkAttrName; " = BEG."; genSurrogateKeyName(ddlType, "ESL")
             Print #fileNo, addTab(indent + 2); "WHERE"
             Print #fileNo, addTab(indent + 3); "PRIV."; g_anValidTo; " >= BEG."; g_anValidFrom
             Print #fileNo, addTab(indent + 4); "AND"
             Print #fileNo, addTab(indent + 3); "PRIV."; g_anValidFrom; " <= BEG."; g_anValidTo
             Print #fileNo, addTab(indent + 4); "AND"
             Print #fileNo, addTab(indent + 3); "PRIV."; g_anOid; " = L."; g_anOid
             Print #fileNo, addTab(indent + 2); "ORDER BY"
             Print #fileNo, addTab(indent + 3); "BEG."; g_anValidFrom
             Print #fileNo, addTab(indent + 2); "FETCH FIRST 1 ROW ONLY"
             Print #fileNo, addTab(indent + 1); ")"
             Print #fileNo, addTab(indent + 0); "WHERE"
             Print #fileNo, addTab(indent + 1); "L.orParEntityId = '"; entityIdStr; "'"
             Print #fileNo, addTab(indent + 2); "AND"
             Print #fileNo, addTab(indent + 1); "L.entityType = '"; dbAcmEntityType; "'"
             If fillRestrictedColSetOnly Then
               Print #fileNo, addTab(indent + 0); ";"
             Else
               Print #fileNo, addTab(indent + 0); "WITH UR;"
             End If

             Print #fileNo,
             Print #fileNo, addTab(indent + 0); "UPDATE"
             Print #fileNo, addTab(indent + 1); tempTabNameLrtLog; " L"
             Print #fileNo, addTab(indent + 0); "SET"
             Print #fileNo, addTab(indent + 1); "L.baseEndSlot = ("
             Print #fileNo, addTab(indent + 2); "SELECT"
             Print #fileNo, addTab(indent + 3); "BEGNL.LABEL"
             Print #fileNo, addTab(indent + 2); "FROM"
             Print #fileNo, addTab(indent + 3); beQualGenNlObjName; " BEGNL"
             Print #fileNo, addTab(indent + 2); "WHERE"
             Print #fileNo, addTab(indent + 3); "BEGNL."; genSurrogateKeyName(ddlType, "ESL"); " = L.t_baseEndSlotGenOID"
             Print #fileNo, addTab(indent + 4); "AND"
             Print #fileNo, addTab(indent + 3); "BEGNL."; g_anLanguageId; " = languageId_in"
             Print #fileNo, addTab(indent + 2); "FETCH FIRST 1 ROW ONLY"
             Print #fileNo, addTab(indent + 1); ")"
             Print #fileNo, addTab(indent + 0); "WHERE"
             Print #fileNo, addTab(indent + 1); "L.orParEntityId = '"; entityIdStr; "'"
             Print #fileNo, addTab(indent + 2); "AND"
             Print #fileNo, addTab(indent + 1); "L.entityType = '"; dbAcmEntityType; "'"
             If fillRestrictedColSetOnly Then
               Print #fileNo, addTab(indent + 0); ";"
             Else
               Print #fileNo, addTab(indent + 0); "WITH UR;"
             End If
           End If
 ' ### ENDIF IVK ###

           If Not fillRestrictedColSetOnly Then
             Print #fileNo, addTab(1); "END IF;"
           End If
         End If

 ' ### IF IVK ###
         If fillRestrictedColSetOnly Then
           genSpLogProcExit(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "#startTime_in", "rowCount_out")
         Else
           genSpLogProcExit(fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "languageId_in", "#startTime_in", "maxRowCount_in", "rowCount_out")
         End If
 ' ### ELSE IVK ###
 '       If fillRestrictedColSetOnly Then
 '         genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "#startTime_in", "rowCount_out"
 '       Else
 '         genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "languageId_in", "#startTime_in", "maxRowCount_in", "rowCount_out"
 '       End If
 ' ### ENDIF IVK ###

         Print #fileNo, addTab(0); "END"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       Next i

       If Not forNl Then
         Dim qualGenLrtViewName As String
         If Not forGen Then
             qualGenLrtViewName = genQualViewName(g_sectionIndexAliasLrt, entityName, entityShortName, ddlType, thisOrgIndex, thisPoolIndex, True, True)
         Else
             qualGenLrtViewName = ""
         End If

         genChangeLogSupportForEntity(acmEntityIndex, acmEntityType, relRefs, qualTabNamePriv, qualTabNameNlPriv, _
             qualTabNamePub, qualTabNameNlPub, qualGenLrtViewName, qualTabNameAggHeadNlPriv, qualViewNameAggHead, thisOrgIndex, thisPoolIndex, _
             thisPoolIndex, fileNo, fileNoClView, ddlType, forGen, forNl)
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
 
 
 Function genLrtLogColDdl( _
   fileNo As Integer, _
   ByRef clMapAttrs() As AttributeMappingForCl, _
   ByRef clMapAttrName As String, _
   ByRef default As String, _
   forGen As Boolean, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef castToType As String = "", _
   Optional ByRef castToTypeDefault As String = "", _
   Optional indent As Integer = 2, _
   Optional commentOnSeparateLine As Boolean = False, _
   Optional ByRef tabVariableName As String = "PRIV", _
   Optional ByRef tabVariableNameGen As String = "GEN", _
   Optional ByRef referredColumns As String = "", _
   Optional ByRef referredColumnsGen As String = "", _
   Optional silent As Boolean = False, _
   Optional ByRef colFoundInGen As Boolean = False, _
   Optional ByVal searchAllColumns As Boolean = False _
 ) As Boolean
   On Error GoTo ErrorExit

   genLrtLogColDdl = False

   Dim tabVariableNameToUse As String
   tabVariableNameToUse = tabVariableName

   If Not arrayIsNull(clMapAttrs) Then
     Dim i As Integer
     For i = LBound(clMapAttrs) To UBound(clMapAttrs)
       If UCase(clMapAttrs(i).mapTo) = UCase(clMapAttrName) And (forGen = clMapAttrs(i).isTv Or (Not forGen)) Then
         If Not forGen And clMapAttrs(i).isTv Then
           colFoundInGen = True
           referredColumnsGen = referredColumnsGen & IIf(referredColumnsGen = "", "", ",") & UCase(clMapAttrs(i).mapFrom)
           tabVariableNameToUse = tabVariableNameGen
         Else
           referredColumns = referredColumns & IIf(referredColumns = "", "", ",") & UCase(clMapAttrs(i).mapFrom)
         End If

         genLrtLogColDdl = True

 '        If silent And Not searchAllColumns Then
 '          Exit Function
 '        End If
 
         Dim mapFromToUse As String
         Dim castToTypeToUse As String
         mapFromToUse = UCase(clMapAttrs(i).mapFrom)
         castToTypeToUse = castToType

 ' ### IF IVK ###
         If clMapAttrs(i).attrIndex > 0 Then
             If g_attributes.descriptors(clMapAttrs(i).attrIndex).isExpression Then
               mapFromToUse = genSurrogateKeyName(ddlType, g_attributes.descriptors(clMapAttrs(i).attrIndex).shortName & "EXP")
               castToTypeToUse = ""
               castToTypeDefault = g_dbtOid
             End If
         End If

 ' ### ENDIF IVK ###
         If Not silent Then
           If castToTypeToUse <> "" Then
             If commentOnSeparateLine Then
               If searchAllColumns Then
                 Print #fileNo, "CAST("; tabVariableNameToUse; "."; mapFromToUse; " AS "; castToTypeToUse; "), ";
               Else
                 Print #fileNo, addTab(indent); "-- "; clMapAttrName
                 Print #fileNo, addTab(indent); "CAST("; tabVariableNameToUse; "."; mapFromToUse; " AS "; castToTypeToUse; "),"
               End If
             Else
               Print #fileNo, addTab(indent); paddRight("CAST(" & tabVariableNameToUse & "." & mapFromToUse & " AS " & castToType & "),", attrListAlign); " -- "; clMapAttrName
             End If
           Else
             If commentOnSeparateLine Then
               If searchAllColumns Then
                 Print #fileNo, tabVariableNameToUse; "."; mapFromToUse; ", ";
               Else
                 Print #fileNo, addTab(indent); "-- "; clMapAttrName
                 Print #fileNo, addTab(indent); tabVariableNameToUse; "."; mapFromToUse; ","
               End If
             Else
               Print #fileNo, addTab(indent); paddRight(tabVariableNameToUse & "." & mapFromToUse & ",", attrListAlign); " -- "; clMapAttrName
             End If
           End If
         End If

         If searchAllColumns Then
           GoTo NextI
         Else
           Exit Function
         End If

         If castToTypeToUse <> "" Then
           If commentOnSeparateLine Then
             If searchAllColumns Then
               Print #fileNo, "CAST("; tabVariableNameToUse; "."; mapFromToUse; " AS "; castToType; "), ";
             Else
               Print #fileNo, addTab(indent); "-- "; clMapAttrName
               Print #fileNo, addTab(indent); "CAST("; tabVariableNameToUse; "."; mapFromToUse; " AS "; castToType; "),"
             End If
           Else
             Print #fileNo, addTab(indent); paddRight("CAST(" & tabVariableNameToUse & "." & mapFromToUse & " AS " & castToTypeToUse & "),", attrListAlign); " -- "; clMapAttrName
           End If
         Else
           If commentOnSeparateLine Then
             If searchAllColumns Then
               Print #fileNo, tabVariableNameToUse; "."; mapFromToUse; ", ";
             Else
               Print #fileNo, addTab(indent); "-- "; clMapAttrName
               Print #fileNo, addTab(indent); tabVariableNameToUse; "."; mapFromToUse; ","
             End If
           Else
             Print #fileNo, addTab(indent); paddRight(tabVariableNameToUse & "." & mapFromToUse & ",", attrListAlign); " -- "; clMapAttrName
           End If
         End If
         Exit Function
       End If
 NextI:
     Next i
   End If

   If default <> "" And Not silent And Not searchAllColumns Then
     If castToTypeDefault <> "" Then
       If commentOnSeparateLine Then
         Print #fileNo, addTab(indent); "-- "; clMapAttrName
         Print #fileNo, addTab(indent); "CAST("; default; " AS "; castToTypeDefault; "),"
       Else
         Print #fileNo, addTab(indent); paddRight("CAST(" & default & " AS " & castToTypeDefault & "),", attrListAlign); " -- "; clMapAttrName
       End If
     Else
       If commentOnSeparateLine Then
         Print #fileNo, addTab(indent); "-- "; clMapAttrName
         Print #fileNo, addTab(indent); default; ","
       Else
         Print #fileNo, addTab(indent); paddRight(default & ",", attrListAlign); " -- "; clMapAttrName
       End If
     End If
   End If
 
 NormalExit:
   On Error Resume Next
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 
 
 Function genLrtLogColDdlAh( _
   fileNo As Integer, _
   ByRef clMapAttrs() As AttributeMappingForCl, _
   ByRef clMapAttrsAh() As AttributeMappingForCl, _
   ByRef clMapAttrName As String, _
   ByRef default As String, _
   forGen As Boolean, _
   ByRef includeAggHeadInJoinPath As Boolean, _
   ByVal checkAggHeadForAttrs As Boolean, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef castToType As String = "", _
   Optional ByRef castToTypeDefault As String = "", _
   Optional indent As Integer = 2, _
   Optional commentOnSeparateLine As Boolean = False, _
   Optional ByRef tabVariableName As String = "PRIV", _
   Optional ByRef tabVariableNameGen As String = "GEN", _
   Optional ByRef tabVariableNameAh As String = "AH", _
   Optional ByRef tabVariableNamePar As String = "", _
   Optional ByRef referredColumns As String = "", _
   Optional ByRef referredAggHeadColumns As String = "", _
   Optional ByVal searchAllColumns As Boolean = False _
 ) As Boolean
   genLrtLogColDdlAh = False

   If genLrtLogColDdl( _
     fileNo, clMapAttrs, clMapAttrName, IIf(checkAggHeadForAttrs, "", default), forGen, ddlType, castToType, castToTypeDefault, _
     indent, commentOnSeparateLine, tabVariableName, tabVariableNameGen, referredColumns, , , , searchAllColumns) _
   Then
     genLrtLogColDdlAh = True
   Else
     If checkAggHeadForAttrs Then
       If genLrtLogColDdl( _
         fileNo, clMapAttrsAh, clMapAttrName, default, False, ddlType, castToType, castToTypeDefault, _
         indent, commentOnSeparateLine, tabVariableNameAh, tabVariableNameGen, referredAggHeadColumns, , , , searchAllColumns) _
       Then
         genLrtLogColDdlAh = True
         includeAggHeadInJoinPath = (tabVariableNamePar <> tabVariableNameAh)
       End If
     End If
   End If
 End Function
 
 
 ' ### IF IVK ###
 Sub genLrtLogRelColDdl( _
   fileNo As Integer, _
   ByRef relRefs As RelationshipDescriptorRefs, _
   ByRef clMapAttrName As String, _
   ByRef refTabVariableName As String, _
   ByRef targetClassIndex As Integer, _
   ByRef default As String, _
   ByRef qualObjName As String, _
   ByRef fkAttrName As String, _
   ByRef clMode As ChangeLogMode, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional ByRef castToType As String = "", _
   Optional indent As Integer = 2, _
   Optional suppressDefault As Boolean = False, _
   Optional commentOnSeparateLine As Boolean = False, _
   Optional forceFollowOidReferences As Boolean = True, _
   Optional ByRef srcTabVariableName As String = "", _
   Optional silent As Boolean = False, _
   Optional ByRef referredColumns As String = "", _
   Optional ByRef colFound As Boolean = False, _
   Optional ByRef colIsGen As Boolean = False, _
   Optional ByVal searchAllColumns As Boolean = False _
 )
   targetClassIndex = -1
   colFound = False
   colIsGen = False

   Dim directedRelShortName As String
   Dim relName As String
   Dim relShortName As String
   Dim colName As String
   Dim i As Integer

   For i = 1 To relRefs.numRefs
       If Not arrayIsNull(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute) Then
         Dim j As Integer
         For j = LBound(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute) To UBound(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute)
           If UCase(g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute(j).mapFrom) = UCase(clMapAttrName) Then
             ' this relationship points to the class where the attribute 'clMapAttrName' can be found
             If relRefs.refs(i).refType = etLeft Then
               ' the targetClass is found at the right hand side
               ' make sure that we do not navigate along the relationship in the reverse direction
               If g_relationships.descriptors(relRefs.refs(i).refIndex).maxRightCardinality <> 1 Then
                 GoTo ExitFor
               End If
               colFound = True
               targetClassIndex = g_relationships.descriptors(relRefs.refs(i).refIndex).rightEntityIndex
               directedRelShortName = g_relationships.descriptors(relRefs.refs(i).refIndex).lrShortRelName
             Else
               ' the targetClass is found at the right hand side
               ' make sure that we do not navigate along the relationship in the reverse direction
               If g_relationships.descriptors(relRefs.refs(i).refIndex).maxLeftCardinality <> 1 Then
                 GoTo ExitFor
               End If
               colFound = True
               targetClassIndex = g_relationships.descriptors(relRefs.refs(i).refIndex).leftEntityIndex
               directedRelShortName = g_relationships.descriptors(relRefs.refs(i).refIndex).rlShortRelName
             End If
             ' we need to refer to the 'OR-Mapping class' if target class does not have an own table
             targetClassIndex = g_classes.descriptors(targetClassIndex).orMappingSuperClassIndex
             colName = g_relationships.descriptors(relRefs.refs(i).refIndex).refersToClAttribute(j).mapTo
             relName = IIf(g_relationships.descriptors(relRefs.refs(i).refIndex).reuseName <> "", g_relationships.descriptors(relRefs.refs(i).refIndex).reuseName, g_relationships.descriptors(relRefs.refs(i).refIndex).relName)
             relShortName = g_relationships.descriptors(relRefs.refs(i).refIndex).effectiveShortName
               If g_classes.descriptors(targetClassIndex).isGenForming And Not g_classes.descriptors(targetClassIndex).hasNoIdentity Then
                 Dim thisAttrIndex As Integer
                 thisAttrIndex = getAttributeIndexByNameAndEntityIndexRecursive(colName, eactClass, g_classes.descriptors(targetClassIndex).classIndex)
                 If thisAttrIndex > 0 Then
                   colIsGen = g_attributes.descriptors(thisAttrIndex).isTimeVarying
                 End If
               End If
             GoTo ExitFor
           End If
         Next j
       End If
   Next i
 
 ExitFor:
   If colFound Then
     fkAttrName = _
       genAttrName( _
         conOid, ddlType, relShortName & directedRelShortName _
       )

     If (UCase(colName) = g_anOid) And Not forceFollowOidReferences Then
       targetClassIndex = -1
       If Not silent Then
         If searchAllColumns Then
           Print #fileNo, srcTabVariableName; "."; UCase(fkAttrName); ", ";
         Else
           If commentOnSeparateLine Then
             Print #fileNo, addTab(indent); "-- "; clMapAttrName
             Print #fileNo, addTab(indent); srcTabVariableName; "."; UCase(fkAttrName); ","
           Else
             Print #fileNo, addTab(indent); paddRight(srcTabVariableName & "." & UCase(fkAttrName) & ",", attrListAlign); " -- "; clMapAttrName
           End If
         End If
       End If
     Else
       If Not silent Then
         If castToType <> "" Then
           If searchAllColumns Then
             Print #fileNo, "CAST("; refTabVariableName; "."; UCase(colName); " AS "; castToType; "), ";
           Else
             If commentOnSeparateLine Then
               Print #fileNo, addTab(indent); "-- "; clMapAttrName
               Print #fileNo, addTab(indent); "CAST("; refTabVariableName; "."; UCase(colName); " AS "; castToType; "),"
             Else
               Print #fileNo, addTab(indent); paddRight("CAST(" & refTabVariableName & "." & UCase(colName) & " AS " & castToType & "),", attrListAlign); " -- "; clMapAttrName
             End If
           End If
         Else
           If searchAllColumns Then
             Print #fileNo, refTabVariableName; "."; UCase(colName); ", ";
           Else
             If commentOnSeparateLine Then
               Print #fileNo, addTab(indent); "-- "; clMapAttrName
               Print #fileNo, addTab(indent); refTabVariableName; "."; UCase(colName); ","
             Else
               Print #fileNo, addTab(indent); paddRight(refTabVariableName & "." & UCase(colName) & ",", attrListAlign); " -- "; clMapAttrName
             End If
           End If
         End If
       End If

       addStrListElem(referredColumns, fkAttrName)

         If g_classes.descriptors(targetClassIndex).isUserTransactional And (clMode = eclLrt) Then
           qualObjName = _
             genQualViewNameByClassIndex( _
               g_classes.descriptors(targetClassIndex).classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen Or colIsGen, True, useMqtToImplementLrt And g_classes.descriptors(targetClassIndex).useMqtToImplementLrt _
             )
         Else
           qualObjName = genQualTabNameByClassIndex(targetClassIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen Or colIsGen)
         End If
     End If
   Else
     If Not suppressDefault And Not searchAllColumns Then
       If commentOnSeparateLine Then
         Print #fileNo, addTab(indent); "-- "; clMapAttrName
         Print #fileNo, addTab(indent); default; ","
       Else
         Print #fileNo, addTab(indent); paddRight(default & ",", attrListAlign); " -- "; clMapAttrName
       End If
     End If
   End If
 End Sub
 
 
 Function genLrtLogRelColDdlAh( _
   fileNo As Integer, _
   ByRef relRefs As RelationshipDescriptorRefs, _
   ByRef relRefsAh As RelationshipDescriptorRefs, _
   ByRef attrMapping() As AttributeMappingForCl, ByRef attrMappingAh() As AttributeMappingForCl, _
   ByRef clMapAttrName As String, ByRef refTupVar As String, _
   ByRef targetClassIndex As Integer, ByRef targetClassIndexAh As Integer, _
   ByRef defaultVal As String, ByRef qualObjName As String, _
   ByRef fkAttrName As String, _
   ByRef includeAggHeadInJoinPath As Boolean, _
   ByVal checkAggHeadForAttrs As Boolean, _
   ByRef clMode As ChangeLogMode, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional indent As Integer = 2, Optional checkColumnsAlso As Boolean = False, _
   Optional commentOnSeparateLine As Boolean = False, _
   Optional forceFollowOidReferences As Boolean = True, _
   Optional ByRef colTupVarSrc As String = "", Optional ByRef colTupVarAh As String = "", _
   Optional ByRef colTupVarSrcGen As String = "", Optional ByRef colTupVarSrcPar As String = "", _
   Optional ByRef colFoundInAggHead As Boolean = False, Optional ByRef colFoundInGen As Boolean = False, Optional ByRef colFoundInAggHeadGen As Boolean = False, _
   Optional ByRef referredColumns As String = "", Optional ByRef referredAggHeadColumns As String = "", Optional ByRef referredGenColumns As String = "", Optional ByRef referredAggHeadGenColumns As String = "", _
   Optional silent As Boolean = False, _
   Optional ByVal searchAllColumns As Boolean = False _
 ) As Boolean
 
   Dim colFound As Boolean

   genLrtLogRelColDdlAh = False
   colFoundInAggHead = False

   ' try to find a column following relationships at 'targetClass'
   genLrtLogRelColDdl(fileNo, relRefs, clMapAttrName, refTupVar, targetClassIndex, defaultVal, _
     qualObjName, fkAttrName, clMode, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent, checkAggHeadForAttrs Or checkColumnsAlso, _
     commentOnSeparateLine, forceFollowOidReferences, IIf(colTupVarSrcPar & "" = "", colTupVarSrc, colTupVarSrcPar), silent, , colFound, colFoundInGen, searchAllColumns)

   If checkAggHeadForAttrs And Not colFound Then
     ' did not find a column following relationships at 'targetClass' -> try aggregate head
     genLrtLogRelColDdl(fileNo, relRefsAh, clMapAttrName, refTupVar, targetClassIndexAh, defaultVal, _
       qualObjName, fkAttrName, clMode, ddlType, thisOrgIndex, thisPoolIndex, forGen, , indent, checkColumnsAlso, _
       commentOnSeparateLine, forceFollowOidReferences, colTupVarAh, silent, referredAggHeadColumns, colFound, colFoundInAggHeadGen, searchAllColumns)

     If colFound Then
       If Not colFoundInAggHeadGen Then
         includeAggHeadInJoinPath = True
       End If
       colFoundInAggHead = Not colFoundInAggHeadGen
     End If
   End If
 
   genLrtLogRelColDdlAh = colFound

   If checkColumnsAlso Then
     If searchAllColumns Or Not colFound Then
       If _
         genLrtLogColDdl( _
           fileNo, attrMapping, clMapAttrName, IIf(checkAggHeadForAttrs, "", defaultVal), _
           forGen, ddlType, , , indent, commentOnSeparateLine, IIf(colTupVarSrcPar = "", colTupVarSrc, colTupVarSrcPar), _
           colTupVarSrcGen, referredColumns, referredGenColumns, silent, colFoundInGen, searchAllColumns _
         ) _
       Then
         genLrtLogRelColDdlAh = True
       Else
         If checkAggHeadForAttrs Then
           If genLrtLogColDdl(fileNo, attrMappingAh, clMapAttrName, defaultVal, False, ddlType, , , indent, True, colTupVarAh, colTupVarSrcGen, referredAggHeadColumns, referredAggHeadGenColumns, silent, colFoundInAggHeadGen, searchAllColumns) Then
             If Not colFoundInAggHeadGen Then
               includeAggHeadInJoinPath = Not colFoundInAggHeadGen
             End If
             colFoundInAggHead = Not colFoundInAggHeadGen
             genLrtLogRelColDdlAh = True
           End If
          End If
       End If
     End If
   End If
 End Function
 
 
 ' ### ENDIF IVK ###
 Sub genLrtSupportDdlForClass( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNoTab As Integer, _
   fileNoView As Integer, _
   fileNoClView As Integer, _
   fileNoFk As Integer, _
   fileNoSup As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   Dim hasNlTab As Boolean
   Dim nlTabIsPurelyPrivate As Boolean

   On Error GoTo ErrorExit

 ' ### IF IVK ###
     hasNlTab = (forGen And g_classes.descriptors(classIndex).hasNlAttrsInGenInclSubClasses) Or _
                (Not forGen And (g_classes.descriptors(classIndex).hasNlAttrsInNonGenInclSubClasses Or g_classes.descriptors(classIndex).enforceLrtChangeComment Or (g_classes.descriptors(classIndex).aggHeadClassIndex = g_classes.descriptors(classIndex).classIndex And g_classes.descriptors(classIndex).implicitelyGenChangeComment And Not g_classes.descriptors(classIndex).condenseData)))
 ' ### ELSE IVK ###
 '   hasNlTab = (forGen And .hasNlAttrsInGenInclSubClasses) Or _
 '              (Not forGen And (.hasNlAttrsInNonGenInclSubClasses Or (.aggHeadClassIndex = .classIndex)))
 ' ### ENDIF IVK ###
     nlTabIsPurelyPrivate = hasNlTab And _
                Not (forGen And g_classes.descriptors(classIndex).hasNlAttrsInGenInclSubClasses) And _
                Not (Not forGen And (g_classes.descriptors(classIndex).hasNlAttrsInNonGenInclSubClasses))
     genLrtSupportViewForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoView, ddlType, forGen, False)
     If hasNlTab Then
       genLrtSupportViewForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoView, ddlType, forGen, True, nlTabIsPurelyPrivate)
     End If

     genLrtSupportTriggerForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, forGen, False)
     If hasNlTab Then
       genLrtSupportTriggerForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, forGen, True, False, nlTabIsPurelyPrivate)
     End If

     genLrtSupportSpsForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoSup, fileNoClView, ddlType, forGen, False)
     If hasNlTab And Not nlTabIsPurelyPrivate Then
       genLrtSupportSpsForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoSup, fileNoClView, ddlType, forGen, True)
     End If

     If useMqtToImplementLrt And g_classes.descriptors(classIndex).useMqtToImplementLrt Then
       genLrtMqtSupportForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoTab, fileNoView, fileNoFk, fileNoSup, ddlType, forGen, False)
       If hasNlTab And Not nlTabIsPurelyPrivate Then
         genLrtMqtSupportForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoTab, fileNoView, fileNoFk, fileNoSup, ddlType, forGen, True, nlTabIsPurelyPrivate)
       End If

       genLrtSupportTriggerForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, forGen, False, True)
       If hasNlTab Then
         genLrtSupportTriggerForEntity(classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, forGen, True, True, nlTabIsPurelyPrivate)
       End If
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' todo: hoarminize parameter list with genLrtSupportDdlForClass
 Sub genLrtSupportDdlForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNoTab As Integer, _
   fileNoView As Integer, _
   fileNoClView As Integer, _
   fileNoFk As Integer, _
   fileNoSup As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit

     genLrtSupportViewForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoView, ddlType, , False)
     If g_relationships.descriptors(thisRelIndex).nlAttrRefs.numDescriptors > 0 Then
       genLrtSupportViewForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoView, ddlType, , True)
     End If

     genLrtSupportTriggerForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, , False)
     If g_relationships.descriptors(thisRelIndex).nlAttrRefs.numDescriptors > 0 Then
       genLrtSupportTriggerForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, , True)
     End If

     genLrtSupportSpsForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoSup, fileNoClView, ddlType, , False)
     If g_relationships.descriptors(thisRelIndex).nlAttrRefs.numDescriptors > 0 Then
       genLrtSupportSpsForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoSup, fileNoClView, ddlType, , True)
     End If

     If useMqtToImplementLrt And g_relationships.descriptors(thisRelIndex).useMqtToImplementLrt Then
       genLrtMqtSupportForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoTab, fileNoView, fileNoFk, fileNoSup, ddlType, , False)
       If g_relationships.descriptors(thisRelIndex).nlAttrRefs.numDescriptors > 0 Then
         genLrtMqtSupportForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoTab, fileNoView, fileNoFk, fileNoSup, ddlType, , True)
       End If

       genLrtSupportTriggerForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, , False, True)
       If g_relationships.descriptors(thisRelIndex).nlAttrRefs.numDescriptors > 0 Then
         genLrtSupportTriggerForEntity(g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNoClView, fileNoSup, ddlType, , True, True)
       End If
     End If
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 
