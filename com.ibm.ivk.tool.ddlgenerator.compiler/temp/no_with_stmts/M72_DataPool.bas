 Attribute VB_Name = "M72_DataPool"
 Option Explicit
 
 Private Const colDataPool = 2
 Private Const colName = colDataPool + 1
 Private Const colShortName = colName + 1
 Private Const colSpecificToOrg = colShortName + 1
 ' ### IF IVK ###
 Private Const colSupportLRT = colSpecificToOrg + 1
 ' ### ELSE IVK ###
 'Private Const colSupportLRT = colSpecificToOrg + 1
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 Private Const colSupportViewsForPsTag = colSupportLRT + 1
 Private Const colSupportTriggerForPsTag = colSupportViewsForPsTag + 1
 Private Const colSupportXmlExport = colSupportTriggerForPsTag + 1
 Private Const colSupportUpdates = colSupportXmlExport + 1
 ' ### ELSE IVK ###
 'Private Const colSupportUpdates = colSupportLRT + 1
 ' ### ENDIF IVK ###
 Private Const colSuppressRefIntegrity = colSupportUpdates + 1
 Private Const colSuppressUniqueConstraints = colSuppressRefIntegrity + 1
 ' ### IF IVK ###
 Private Const colInstantiateExpressions = colSuppressUniqueConstraints + 1
 Private Const colCommonItemsLocal = colInstantiateExpressions + 1
 ' ### ELSE IVK ###
 'Private Const colCommonItemsLocal = colSuppressUniqueConstraints + 1
 ' ### ENDIF IVK ###
 Private Const colSupportAcm = colCommonItemsLocal + 1
 Private Const colIsActive = colSupportAcm + 1
 ' ### IF IVK ###
 Private Const colIsProductive = colIsActive + 1
 Private Const colIsArchive = colIsProductive + 1
 Private Const colSupportNationalization = colIsArchive + 1
 Private Const colSequenceCacheSize = colSupportNationalization + 1
 ' ### ELSE IVK ###
 'Private Const colSequenceCacheSize = colIsActive + 1
 ' ### ENDIF IVK ###
 
 Private Const firstRow = 3
 
 Private Const sheetName = "DP"
 
 Private Const processingStepOidSeq = 3
 Private Const processingStepUdf = 5
 Private Const processingStepSp = 5
 
 Global g_pools As DataPoolDescriptors
 
 Private Sub readSheet()
   Dim thisPoolId As Integer

   initDataPoolDescriptors g_pools

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colDataPool) & "" <> ""
     thisPoolId = CInt(thisSheet.Cells(thisRow, colDataPool))

       g_pools.descriptors(allocDataPoolIndex(g_pools)).id = thisPoolId
       g_pools.descriptors(allocDataPoolIndex(g_pools)).name = Trim(thisSheet.Cells(thisRow, colName))
       g_pools.descriptors(allocDataPoolIndex(g_pools)).shortName = Trim(thisSheet.Cells(thisRow, colShortName))
       g_pools.descriptors(allocDataPoolIndex(g_pools)).specificToOrgId = getInteger(thisSheet.Cells(thisRow, colSpecificToOrg))
       g_pools.descriptors(allocDataPoolIndex(g_pools)).supportLrt = getBoolean(thisSheet.Cells(thisRow, colSupportLRT))

 ' ### IF IVK ###
       g_pools.descriptors(allocDataPoolIndex(g_pools)).supportViewsForPsTag = getBoolean(thisSheet.Cells(thisRow, colSupportViewsForPsTag))
       g_pools.descriptors(allocDataPoolIndex(g_pools)).supportTriggerForPsTag = getBoolean(thisSheet.Cells(thisRow, colSupportTriggerForPsTag))
       g_pools.descriptors(allocDataPoolIndex(g_pools)).supportXmlExport = getBoolean(thisSheet.Cells(thisRow, colSupportXmlExport))
 ' ### ENDIF IVK ###
       g_pools.descriptors(allocDataPoolIndex(g_pools)).supportUpdates = getBoolean(thisSheet.Cells(thisRow, colSupportUpdates))

       g_pools.descriptors(allocDataPoolIndex(g_pools)).suppressRefIntegrity = getBoolean(thisSheet.Cells(thisRow, colSuppressRefIntegrity))
       g_pools.descriptors(allocDataPoolIndex(g_pools)).suppressUniqueConstraints = getBoolean(thisSheet.Cells(thisRow, colSuppressUniqueConstraints))
 ' ### IF IVK ###
       g_pools.descriptors(allocDataPoolIndex(g_pools)).instantiateExpressions = getBoolean(thisSheet.Cells(thisRow, colInstantiateExpressions))
 ' ### ENDIF IVK ###
       g_pools.descriptors(allocDataPoolIndex(g_pools)).commonItemsLocal = getBoolean(thisSheet.Cells(thisRow, colCommonItemsLocal))
       g_pools.descriptors(allocDataPoolIndex(g_pools)).supportAcm = getBoolean(thisSheet.Cells(thisRow, colSupportAcm))

       g_pools.descriptors(allocDataPoolIndex(g_pools)).isActive = getBoolean(thisSheet.Cells(thisRow, colIsActive))
 ' ### IF IVK ###
       g_pools.descriptors(allocDataPoolIndex(g_pools)).isArchive = getBoolean(thisSheet.Cells(thisRow, colIsArchive))
       g_pools.descriptors(allocDataPoolIndex(g_pools)).isProductive = getBoolean(thisSheet.Cells(thisRow, colIsProductive))
       g_pools.descriptors(allocDataPoolIndex(g_pools)).supportNationalization = getBoolean(thisSheet.Cells(thisRow, colSupportNationalization))
 ' ### ENDIF IVK ###

       g_pools.descriptors(allocDataPoolIndex(g_pools)).sequenceCacheSize = getInteger(thisSheet.Cells(thisRow, colSequenceCacheSize), -1)
 ' ### IF IVK ###

       If g_pools.descriptors(allocDataPoolIndex(g_pools)).isArchive And Not supportArchivePool Then
         g_pools.descriptors(allocDataPoolIndex(g_pools)).isActive = False
       End If
 ' ### ENDIF IVK ###
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getDataPools()
   If g_pools.numDescriptors = 0 Then
     readSheet
   End If
 End Sub
 
 
 Sub resetDataPools()
   g_pools.numDescriptors = 0
 End Sub
 
 
 Sub cleanupPools()
   Dim srcIndex As Integer, dstIndex As Integer
   dstIndex = 1
   For srcIndex = 1 To g_pools.numDescriptors Step 1
     If g_pools.descriptors(srcIndex).isActive Then
       If srcIndex <> dstIndex Then
         g_pools.descriptors(dstIndex) = g_pools.descriptors(srcIndex)
       End If
       dstIndex = dstIndex + 1
     End If
   Next srcIndex
   g_pools.numDescriptors = dstIndex - 1
 End Sub
 
 
 Function getDataPoolIndexById( _
   ByRef poolId As Integer _
 ) As Integer
   Dim i As Integer
 
   getDataPoolIndexById = -1
   getDataPools
 
   For i = 1 To g_pools.numDescriptors Step 1
     If g_pools.descriptors(i).id = poolId Then
       getDataPoolIndexById = i
       Exit Function
     End If
   Next i
 End Function
 
 
 Function getDataPoolNameByIndex( _
   poolIndex As Integer _
 ) As String
   getDataPoolNameByIndex = ""
   If (poolIndex > 0) Then getDataPoolNameByIndex = g_pools.descriptors(poolIndex).name
 End Function
 
 
 Function poolIsValidForOrg( _
   ByVal thisPoolIndex As Integer, _
   ByVal thisOrgIndex As Integer _
 ) As Boolean
   If thisPoolIndex < 1 Or thisOrgIndex < 1 Then
     poolIsValidForOrg = True
   Else
       poolIsValidForOrg = (g_pools.descriptors(thisPoolIndex).specificToOrgId = -1 Or g_pools.descriptors(thisPoolIndex).specificToOrgId = g_orgs.descriptors(thisOrgIndex).id)
   End If
 End Function
 
 
 ' ### IF IVK ###
 Function poolSupportsArchiving( _
   poolId As Integer _
 ) As Boolean
   poolSupportsArchiving = False

   If poolId <> -1 Then
     Dim i As Integer

     For i = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(i).id = poolId Then
         poolSupportsArchiving = g_pools.descriptors(i).isArchive
         Exit Function
       End If
     Next i
   End If
 End Function
 
 
 ' ### ENDIF IVK ###
 Function poolSupportLrt( _
   poolId As Integer _
 ) As Boolean
   poolSupportLrt = False

   If poolId <> -1 Then
     Dim i As Integer

     For i = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(i).id = poolId Then
         poolSupportLrt = g_pools.descriptors(i).supportLrt
         Exit Function
       End If
     Next i
   End If
 End Function
 
 
 Sub genDataPoolDdl( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forOrgIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit

   If ddlType = edtPdm Then
     If thisPoolIndex > 0 Then
       If Not g_pools.descriptors(thisPoolIndex).supportAcm Then
         Exit Sub
       End If
     End If
   Else
     Exit Sub
   End If

   If thisOrgIndex > 0 And thisPoolIndex > 0 Then
     If Not g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
       Exit Sub
     End If
   End If

   Dim fileNo As Integer

   fileNo = openDdlFile(g_targetDir, g_sectionIndexDb, processingStepOidSeq, ddlType, thisOrgIndex, thisPoolIndex)
 
   genOidSequenceForOrg thisOrgIndex, fileNo, ddlType, forOrgIndex
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Sub genDataPoolDdl2( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   If ddlType = edtPdm Then
       If (Not g_pools.descriptors(thisPoolIndex).supportAcm) Or g_pools.descriptors(thisPoolIndex).isArchive Then
         Exit Sub
       End If
   End If

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexAspect, processingStepUdf, ddlType, thisOrgIndex, thisPoolIndex, , , ldmIterationPoolSpecific)
 
   genSrxUDFsByPool estSr0, thisOrgIndex, thisPoolIndex, fileNo, ddlType
   genSrxUDFsByPool estSr1, thisOrgIndex, thisPoolIndex, fileNo, ddlType
   genSrxUDFsByPool estNsr1, thisOrgIndex, thisPoolIndex, fileNo, ddlType
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### ENDIF IVK ###
 Sub genDataPoolDdl3( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   On Error GoTo ErrorExit

   If ddlType = edtLdm Then
     Exit Sub
   End If

   Dim poolSupportLrt As Boolean
   Dim thisPoolId As Integer

     If (Not g_pools.descriptors(thisPoolIndex).supportAcm) Or g_pools.descriptors(thisPoolIndex).specificToOrgId > 0 Then
       Exit Sub
     End If

     thisPoolId = g_pools.descriptors(thisPoolIndex).id
     poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
 
   Dim thisOrgId As Integer
   thisOrgId = g_pools.descriptors(thisOrgIndex).id

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDb, processingStepSp, ddlType, thisOrgIndex, thisPoolIndex, , phaseLrt)
 
   Dim qualProcName As String
   ' ####################################################################################################################
   ' #    SP for checking consistency of DB2 register
   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexAliasLrt, spnCheckDb2Register, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader "SP for checking consistency of DB2 register", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "regVarLrtOid_in", "VARCHAR(128)", True, "value of registry variable holding LRT-OID"
 ' ### IF IVK ###
   genProcParm fileNo, "IN", "regVarPsOid_in", "VARCHAR(128)", True, "value of registry variable holding PS-OID"
 ' ### ENDIF IVK ###
   genProcParm fileNo, "IN", "regVarSchema_in", "VARCHAR(128)", True, "value of registry variable holding current schema"
   genProcParm fileNo, "IN", "forLrt_in", "INTEGER", False, "'1' iff LRT-context is required, '0' if LRT-context is required to be empty, NULL if no restrictions on LRT-context apply"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_lrtOid", g_dbtOid, "NULL"
 ' ### IF IVK ###
   genVarDecl fileNo, "v_psOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_lrtPsOid", g_dbtOid, "NULL"
 ' ### ENDIF IVK ###
   genVarDecl fileNo, "v_lrtOrgId", g_dbtEnumId, "NULL"
   genVarDecl fileNo, "v_schemaOrgIdStr", "VARCHAR(2)"

   Print #fileNo,
   Dim indent As Integer
   indent = 1
   If poolSupportLrt Then
     Print #fileNo, addTab(1); "IF forLrt_in = 1 THEN"
     genProcSectionHeader fileNo, "verify that DB2 register for LRTOID is set", 2, True
     Print #fileNo, addTab(2); "IF COALESCE(regVarLrtOid_in, '') = '' THEN"
     genSignalDdl "lrtContextNotSet", fileNo, 3
     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo, addTab(1); "ELSEIF forLrt_in = "; gc_dbFalse; " THEN"
     indent = 2
   End If
   genProcSectionHeader fileNo, "verify that DB2 register for LRTOID is NOT set", indent, True
   Print #fileNo, addTab(indent); "IF COALESCE(regVarLrtOid_in, '') <> '' THEN"
   genSignalDdl "lrtContextSet", fileNo, indent + 1
   Print #fileNo, addTab(indent); "END IF;"

   If poolSupportLrt Then
     Print #fileNo, addTab(1); "END IF;"
   End If
 
   genProcSectionHeader fileNo, "this check is temporarily disabled"
   Print #fileNo, addTab(1); "RETURN 0;"
 
   genProcSectionHeader fileNo, "use default values for input parameter if no values are provided"
   Print #fileNo, addTab(1); "IF COALESCE(regVarLrtOid_in, '') = '' THEN"
   Print #fileNo, addTab(2); "SET regVarLrtOid_in = '0';"
   Print #fileNo, addTab(1); "END IF;"

 ' ### IF IVK ###
   Print #fileNo, addTab(1); "IF COALESCE(regVarPsOid_in, '') = '' THEN"
   Print #fileNo, addTab(2); "SET regVarPsOid_in = '0';"
   Print #fileNo, addTab(1); "END IF;"

 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "SET regVarSchema_in  = COALESCE(regVarSchema_in, CURRENT SCHEMA);"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_lrtOid = "; g_dbtOid; "(regVarLrtOid_in);"
 ' ### IF IVK ###
   Print #fileNo, addTab(1); "SET v_psOid  = "; g_dbtOid; "(regVarPsOid_in);"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "SET v_lrtOrgId  = v_lrtOid / 1"; Left("000000000000000000000000000000000000", Len(gc_sequenceEndValue)); ";"
   Print #fileNo, addTab(1); "SET v_schemaOrgIdStr  = LEFT(RIGHT(regVarSchema_in, 3),2);"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_lrtOid <> 0 THEN"
   genProcSectionHeader fileNo, "verify that DB2 register for LRT-OID is consistent with organization", 2, True
   Print #fileNo, addTab(2); "IF v_lrtOrgId <> "; genOrgId(thisOrgIndex, ddlType, True); " THEN"
 
   genSignalDdlWithParms "lrtContextInconsistentWithOrg", fileNo, 3, , , , , , , , , , "COALESCE(RTRIM(CHAR(v_lrtOid)), '')", "RTRIM(CHAR(" & genOrgId(thisOrgIndex, ddlType, True) & "))"

   Print #fileNo, addTab(2); "END IF;"

 ' ### IF IVK ###
   If poolSupportLrt Then
     Dim qualTabNameLrt As String
     qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

     Print #fileNo,
     Print #fileNo, addTab(2); "SELECT "; g_anPsOid; " INTO v_lrtPsOid FROM "; qualTabNameLrt; " WHERE OID = v_lrtOid WITH UR;"

     genProcSectionHeader fileNo, "verify that LRT OID is valid", 2
     Print #fileNo, addTab(2); "IF v_lrtPsOid IS NULL THEN"
     genSignalDdlWithParms "lrtContextInvalid", fileNo, 3, , , , , , , , , , "RTRIM(CHAR(v_lrtOid))"
     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo,
     genProcSectionHeader fileNo, "verify that DB2 register for PS-OID is consistent with LRT", 2
     Print #fileNo, addTab(2); "IF COALESCE(v_psOid, -1) <> COALESCE(v_lrtPsOid, -1) THEN"
     genSignalDdlWithParms "psInconsistentWithLrt", fileNo, 3, , , , , , , , , , "COALESCE(RTRIM(CHAR(v_psOid)),'')", "COALESCE(RTRIM(CHAR(v_lrtPsOid)),'')"
     Print #fileNo, addTab(2); "END IF;"
   End If

 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "verify that CURRENT SCHEMA is consistent with organization", 1
   Print #fileNo, addTab(1); "IF regVarSchema_in <> '' THEN"

   Print #fileNo, addTab(2); "IF v_schemaOrgIdStr <> RIGHT(DIGITS("; genOrgId(thisOrgIndex, ddlType, True); "),2) THEN"
   genSignalDdlWithParms "schemaInconsistentWithOrg", fileNo, 3, , , , , , , , , , "regVarSchema_in", "RTRIM(CHAR(" & genOrgId(thisOrgIndex, ddlType, True) & "))"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   printSectionHeader "SP for checking consistency of DB2 register", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "forLrt_in", "INTEGER", False, "'1' iff LRT-context is required, '0' otherwise"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
   Print #fileNo,
 
 ' ### IF IVK ###
   Print #fileNo, addTab(1); "CALL "; qualProcName; "("; gc_db2RegVarLrtOid; ", "; gc_db2RegVarPsOid; ", "; gc_db2RegVarSchema; ", forLrt_in);"
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(1); "CALL "; qualProcName; "("; gc_db2RegVarLrtOid; ", "; gc_db2RegVarSchema; ", forLrt_in);"
 ' ### ENDIF IVK ###

   Print #fileNo,
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
 
 
 Sub genDataPoolsDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisPoolIndex As Integer
   Dim thisOrgIndex As Integer
   Dim forOrgIndex As Integer

   If ddlType = edtLdm Then
 ' ### IF IVK ###
     genDataPoolDdl2 , , edtLdm
 ' ### ENDIF IVK ###
     genDataPoolDdl3 , , edtLdm
   ElseIf ddlType = edtPdm Then
     genDataPoolDdl , , , edtPdm

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       If g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
         For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
             For forOrgIndex = 1 To g_orgs.numDescriptors Step 1
               genDataPoolDdl thisOrgIndex, thisPoolIndex, forOrgIndex, edtPdm
             Next forOrgIndex
           End If
         Next thisOrgIndex
       End If

       For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
         If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
 ' ### IF IVK ###
           genDataPoolDdl2 thisOrgIndex, thisPoolIndex, edtPdm
 ' ### ENDIF IVK ###
           genDataPoolDdl3 thisOrgIndex, thisPoolIndex, edtPdm
         End If
       Next thisOrgIndex
     Next thisPoolIndex
   End If
 End Sub
 ' ### IF IVK ###
 
 
 Sub genSrxUDFsByPool( _
   srxType As SrxTypeId, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim srxTypeStr As String
   srxTypeStr = genSrxType2Str(srxType)
 
   Dim qualFuncName As String
   Dim qualTabNameGenericAspect As String
   Dim qualTabNameGenericCode As String
     qualFuncName = genQualFuncName(g_classes.descriptors(g_classIndexGenericAspect).sectionIndex, srxTypeStr & "Ctxt_OID", ddlType, thisOrgIndex, thisPoolIndex)
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim colPrefix As String
   If srxType = estSr0 Then
     colPrefix = "S0CS"
   ElseIf srxType = estSr1 Then
     colPrefix = "S1CT"
   ElseIf srxType = estNsr1 Then
     colPrefix = "N1CN"
   Else
     colPrefix = "XXXX" ' should not happen
   End If

   printSectionHeader "Function for concatenating " & srxTypeStr & "-Context-OIDs for ""Aspect""", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "oid_in", g_dbtOid, False, "OID of an 'Aspect'-object"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(220)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_oidIdList", "VARCHAR(220)", "''"
 
   genProcSectionHeader fileNo, "add each OID contributing to the SR0Context"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "oid"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "01_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "02_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "03_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "04_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "05_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "06_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "07_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "08_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "09_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT "; colPrefix; "10_OID FROM "; qualTabNameGenericAspect; " WHERE OID = oid_in"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "oid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "oid IS NOT NULL"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "oid"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_oidIdList = v_oidIdList || (CASE V_oidIdList WHEN '' THEN '' ELSE ',' END) || RTRIM(CAST(OID AS CHAR(20)));"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
 '  Print #fileNo, addTab(1); "RETURN (CASE WHEN v_oidIdList = '' THEN NULL ELSE v_oidIdList END);"
   Print #fileNo, addTab(1); "RETURN v_oidIdList;"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
     qualFuncName = genQualFuncName(g_classes.descriptors(g_classIndexGenericAspect).sectionIndex, srxTypeStr & "Ctxt_CDE", ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader "Function for concatenating " & srxTypeStr & "-Context-CodeNumbers for ""Aspect""", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "", "oid_in", g_dbtOid, False, "OID of an 'Aspect'-object"
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(159)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_codeNumList", "VARCHAR(159)", "''"
 
   genProcSectionHeader fileNo, "add each " & g_anCodeNumber & " contributing to the SR0Context"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); conCodeNumber
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "01_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "02_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "03_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "04_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "05_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "06_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "07_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "08_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "09_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "SELECT C."; g_anCodeNumber; " FROM "; qualTabNameGenericCode; " C,"; _
                             qualTabNameGenericAspect; " A WHERE A."; colPrefix; "10_OID = C."; g_anOid; " AND A."; g_anOid; " = oid_in"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); conCodeNumber; " AS c_codeNumber"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); conCodeNumber
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_codeNumList = v_codeNumList || (CASE v_codeNumList WHEN '' THEN '' ELSE ',' END) || c_codeNumber;"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_codeNumList;"
 '  Print #fileNo, addTab(1); "RETURN (CASE WHEN v_codeNumList = '' THEN NULL ELSE v_codeNumList END);"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   If Not (thisOrgIndex = g_primaryOrgIndex) And (thisPoolIndex = g_workDataPoolIndex) Then
     Const numSrxCodes = 10

     Dim qualTabNameGenericAspectFactory
     qualTabNameGenericAspectFactory = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
     Dim qualTabNameGenericAspectOrg
     qualTabNameGenericAspectOrg = qualTabNameGenericAspect

       qualFuncName = genQualFuncName(g_classes.descriptors(g_classIndexGenericAspect).sectionIndex, srxTypeStr & "IsAvail", ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader "Function determining whether a factory " & srxTypeStr & "-Context is subsumed by an MPC" & srxTypeStr & "-Context", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); qualFuncName
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "", "factoryOid_in", g_dbtOid, True, "OID of a factory 'Aspect'-object"
     genProcParm fileNo, "", "mpcOid_in", g_dbtOid, False, "OID of an MPC 'Aspect'-object"
     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); "RETURNS"
     Print #fileNo, addTab(1); g_dbtBoolean
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "READS SQL DATA"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader fileNo, "declare variables"
     genVarDecl fileNo, "v_isAvailable", g_dbtBoolean, gc_dbTrue
     Dim i As Integer
     For i = 1 To numSrxCodes
       genVarDecl fileNo, "v_facOid" & Right("0" & i, 2), g_dbtOid, "NULL"
       genVarDecl fileNo, "v_mpcOid" & Right("0" & i, 2), g_dbtOid, "NULL"
     Next i

     genProcSectionHeader fileNo, "determine " & srxTypeStr & "Context-OIDs"
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(1); "("
     For i = 1 To numSrxCodes
       Print #fileNo, addTab(2); "v_facOid"; Right("0" & i, 2); IIf(i < numSrxCodes, ",", "")
     Next i
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "="
     Print #fileNo, addTab(1); "("

     Print #fileNo, addTab(2); "SELECT"
     For i = 1 To numSrxCodes
       Print #fileNo, addTab(3); colPrefix; Right("0" & i, 2); "_OID"; IIf(i < numSrxCodes, ",", "")
     Next i
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameGenericAspectFactory
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); g_anOid; " = factoryOid_in"
     Print #fileNo, addTab(1); ");"
     Print #fileNo,

     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(1); "("
     For i = 1 To numSrxCodes
       Print #fileNo, addTab(2); "v_mpcOid"; Right("0" & i, 2); IIf(i < numSrxCodes, ",", "")
     Next i
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "="
     Print #fileNo, addTab(1); "("

     Print #fileNo, addTab(2); "SELECT"
     For i = 1 To numSrxCodes
       Print #fileNo, addTab(3); colPrefix; Right("0" & i, 2); "_OID"; IIf(i < numSrxCodes, ",", "")
     Next i
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameGenericAspectOrg
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); g_anOid; " = mpcOid_in"
     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "check each CODE contributing to the " & srxTypeStr & "Context"
     Print #fileNo, addTab(1); "FOR codeLoop AS"
     Print #fileNo, addTab(2); "WITH"
     Print #fileNo, addTab(3); "V_Fac"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "AS"
     Print #fileNo, addTab(2); "("

     For i = 1 To numSrxCodes
       Print #fileNo, addTab(3); "VALUES(v_facOid"; Right("0" & i, 2); ")"
       If i < numSrxCodes Then
         Print #fileNo, addTab(4); "UNION ALL"
       End If
     Next i
     Print #fileNo, addTab(2); "),"
     Print #fileNo, addTab(3); "V_Org"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "AS"
     Print #fileNo, addTab(2); "("
     For i = 1 To numSrxCodes
       Print #fileNo, addTab(3); "VALUES(v_mpcOid"; Right("0" & i, 2); ")"
       If i < numSrxCodes Then
         Print #fileNo, addTab(4); "UNION ALL"
       End If
     Next i

     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V_Fac"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "oid IS NOT NULL"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "oid NOT IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "oid"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); "V_Org"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "oid IS NOT NULL"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(2); "FETCH FIRST 1 ROWS ONLY"
     Print #fileNo, addTab(1); "DO"
     Print #fileNo, addTab(2); "SET v_isAvailable = "; gc_dbFalse; ";"
     Print #fileNo, addTab(1); "END FOR;"
     Print #fileNo,
     Print #fileNo, addTab(1); "RETURN v_isAvailable;"
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If
 
   If srxType = estSr0 Then
       qualFuncName = genQualFuncName(g_classes.descriptors(g_classIndexGenericAspect).sectionIndex, "IsValidForSr0", ddlType, thisOrgIndex, thisPoolIndex)

     printSectionHeader "Function determining whether a given Aspect is valid for a given SR0-Context", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); qualFuncName
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "", "aspectOid_in", g_dbtOid, True, "OID of the 'Aspect'-object to be checked"
     genProcParm fileNo, "", "sr0Oid_in", g_dbtOid, False, "OID of an 'SR0-Validity'-object"
     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); "RETURNS"
     Print #fileNo, addTab(1); g_dbtBoolean
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "READS SQL DATA"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader fileNo, "declare variables"
     genVarDecl fileNo, "v_isValid", g_dbtBoolean, gc_dbTrue

     genProcSectionHeader fileNo, "add each " & g_anCodeNumber & " contributing to the SR0Context"
     Print #fileNo, addTab(1); "FOR tabLoop AS"
     Print #fileNo, addTab(2); "WITH"
     Print #fileNo, addTab(3); "V_Asp"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "AS"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "01_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "02_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "03_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "04_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "05_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "06_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "07_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "08_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "09_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "10_OID FROM "; qualTabNameGenericAspect; " WHERE OID = aspectOid_in"
     Print #fileNo, addTab(2); "),"
     Print #fileNo, addTab(3); "V_SR0"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "AS"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "01_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "02_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "03_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "04_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "05_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "06_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "07_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "08_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "09_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(4); "UNION ALL"
     Print #fileNo, addTab(3); "SELECT "; colPrefix; "10_OID FROM "; qualTabNameGenericAspect; " WHERE OID = sr0Oid_in"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "oid"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V_Asp"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "oid IS NOT NULL"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "oid NOT IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "oid"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); "V_SR0"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "oid IS NOT NULL"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(2); "FETCH FIRST 1 ROWS ONLY"
     Print #fileNo, addTab(1); "DO"
     Print #fileNo, addTab(2); "SET v_isValid = "; gc_dbFalse; ";"
     Print #fileNo, addTab(1); "END FOR;"
     Print #fileNo,
     Print #fileNo, addTab(1); "RETURN v_isValid;"
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If
 End Sub
 ' ### ENDIF IVK ###
 
