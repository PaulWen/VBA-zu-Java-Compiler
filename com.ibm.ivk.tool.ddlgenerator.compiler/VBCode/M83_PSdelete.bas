Attribute VB_Name = "M83_PSdelete"
' ### IF IVK ###
Option Explicit

Private Const processingStep = 3


Sub genPsDeleteSupportDdl( _
  ddlType As DdlTypeId _
)
  Dim thisOrgIndex As Integer
  Dim thisPoolIndex As Integer
  
  If Not g_genLrtSupport Then
    Exit Sub
  End If
  
  If ddlType = edtPdm Then
    For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
      If g_pools.descriptors(thisPoolIndex).supportLrt Then
        For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
          If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And g_orgs.descriptors(thisOrgIndex).isPrimary Then
            genPsDeleteSupportDdlByPool thisOrgIndex, thisPoolIndex, edtPdm
          End If
         Next thisOrgIndex
       End If
     Next thisPoolIndex
  End If
End Sub


Private Sub genPsDeleteSupportDdlByPool( _
  Optional ByVal thisOrgIndex As Integer = -1, _
  Optional ByVal thisPoolIndex As Integer = -1, _
  Optional ddlType As DdlTypeId = edtLdm _
)
  If generateFwkTest Then
    Exit Sub
  End If
  
  If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
    ' PS-delete is only supported at 'pool-level'
    Exit Sub
  End If

  On Error GoTo ErrorExit
  
  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)

  Dim qualTabNameDocuNews As String
  Dim qualTabNameDocuNewsType As String
  Dim qualTabNameMdsInbox As String
  Dim qualTabNameJob As String

  qualTabNameDocuNews = genQualTabNameByClassIndex(g_classIndexDocuNews, ddlType, thisOrgIndex, thisPoolIndex)
  qualTabNameDocuNewsType = genQualTabNameByClassIndex(g_classIndexDocuNewsType, ddlType, thisOrgIndex, thisPoolIndex)
  qualTabNameJob = genQualTabNameByClassIndex(g_classIndexJob, ddlType, thisOrgIndex, thisPoolIndex)

  Dim qualProcName As String
  qualProcName = genQualProcName(g_sectionIndexAliasLrt, "PsDelete", ddlType, thisOrgIndex, thisPoolIndex)
  
  printSectionHeader "SP for 'Deleting ProductStructure'", fileNo
  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE PROCEDURE"
  Print #fileNo, addTab(1); qualProcName
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProuctStructure to delete"
  genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being deleted (sum over all tables)"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "RESULT SETS 0"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  Print #fileNo, addTab(0); "BEGIN"

  genProcSectionHeader fileNo, "declare conditions", , True
  genCondDecl fileNo, "notFound", "02000"
  
  genProcSectionHeader fileNo, "declare variables"
  genSigMsgVarDecl fileNo
  genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
  genVarDecl fileNo, "v_isUnderConstruction", g_dbtBoolean, gc_dbFalse
  genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
  genSpLogDecl fileNo

  genProcSectionHeader fileNo, "declare statement"
  genVarDecl fileNo, "stmnt", "STATEMENT"
  
  genProcSectionHeader fileNo, "declare condition handler"
  Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
  Print #fileNo, addTab(1); "BEGIN"
  Print #fileNo, addTab(2); "-- just ignore"
  Print #fileNo, addTab(1); "END;"
  
  genSpLogProcEnter fileNo, qualProcName, ddlType, , "psOid_in", "rowCount_out"

  genDb2RegVarCheckDdl fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1

  Print #fileNo,
  Print #fileNo, addTab(1); "SET rowCount_out = 0;"
  
  genProcSectionHeader fileNo, "verify that ProductStructure exists"
  Print #fileNo, addTab(1); "IF NOT EXISTS (SELECT "; g_anOid; " FROM "; g_qualTabNameProductStructure; " WHERE "; g_anOid; " = psOid_in) THEN"
  
  genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "psOid_in", "rowCount_out"
  genSignalDdlWithParms "psNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(psOid_in))"
  
  Print #fileNo, addTab(1); "END IF;"

  genProcSectionHeader fileNo, "verify that ProductStructure is 'under construction'"
  Print #fileNo, addTab(1); "SET v_isUnderConstruction ="
  Print #fileNo, addTab(1); "("
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); g_anIsUnderConstruction
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameProductStructure
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); g_anOid; " = psOid_in"
  Print #fileNo, addTab(1); ");"
  Print #fileNo,
  Print #fileNo, addTab(1); "IF NOT (v_isUnderConstruction = 1) THEN"
  
  genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "psOid_in", "rowCount_out"
  genSignalDdlWithParms "psDelNotUndConstr", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(psOid_in))"
  
  Print #fileNo, addTab(1); "END IF;"

  genProcSectionHeader fileNo, "loop over all PS-tagged tables"

  Print #fileNo, addTab(1); "FOR tabLoop AS"
  genTabListView fileNo, thisOrgIndex, thisPoolIndex, thisPoolIndex, ddlType, 2, True
  
  Print #fileNo, addTab(2); "SELECT"
  Print #fileNo, addTab(3); "T.schemaName AS c_schemaName,"
  Print #fileNo, addTab(3); "T.tabName AS c_tableName,"
  Print #fileNo, addTab(3); "T.filter AS c_filter"
  Print #fileNo, addTab(2); "FROM"
  Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L,"
  Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P,"
  Print #fileNo, addTab(3); "V_TabList T,"
  Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
  Print #fileNo, addTab(2); "WHERE"
  Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "P."; g_anPdmTableName; " = T.TABNAME"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " = T.schemaName"
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "A."; g_anAcmIsPs; " = "; gc_dbTrue
  Print #fileNo, addTab(4); "AND"
  Print #fileNo, addTab(3); "COALESCE(P."; g_anOrganizationId; ","; genOrgId(thisOrgIndex, ddlType, True); ") = "; genOrgId(thisOrgIndex, ddlType, True)
  Print #fileNo, addTab(2); "ORDER BY"
  ' todo: foreign keys on LRT-tables do not appear to be reflected in FKSEQUENCENO; thus we need extra ordering by ISLRT here
  Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " DESC,"
  Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " DESC,"
  Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " DESC"
  Print #fileNo, addTab(2); "FOR READ ONLY"
  
  Print #fileNo, addTab(1); "DO"
  
  genProcSectionHeader fileNo, "delete records in table tagged with this PS-OID", 2, True
  Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_schemaName || '.' || c_tableName || ' WHERE ' || c_filter;"
  Print #fileNo,
  Print #fileNo, addTab(2); "SET v_stmntTxt = REPLACE(v_stmntTxt,'<PS>',RTRIM(CHAR(psOid_in)));"
  Print #fileNo, addTab(2); "SET v_stmntTxt = REPLACE(v_stmntTxt,'<REFSCHEMA>',c_schemaName);"
  Print #fileNo,
  Print #fileNo, addTab(2); "PREPARE stmnt FROM v_stmntTxt;"
  Print #fileNo,
  Print #fileNo, addTab(2); "EXECUTE"
  Print #fileNo, addTab(3); "stmnt"
  Print #fileNo, addTab(2); "USING"
  Print #fileNo, addTab(3); "psOid_in"
  Print #fileNo, addTab(2); ";"
  
  genProcSectionHeader fileNo, "count the number of affected rows", 2
  Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
  Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

  Print #fileNo, addTab(1); "END FOR;"
    
  genProcSectionHeader fileNo, "delete related DocuNews"
  Print #fileNo, addTab(1); "DELETE FROM"
  Print #fileNo, addTab(2); qualTabNameDocuNews; " N"
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "EXISTS ("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "1"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); qualTabNameDocuNewsType; " T"
  Print #fileNo, addTab(3); "WHERE"
  Print #fileNo, addTab(4); "N.DNATPE_OID = T."; g_anOid
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "T.DNPPST_OID = psOid_in"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(1); ";"

  genProcSectionHeader fileNo, "count the number of affected rows"
  Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
  Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
  
  genProcSectionHeader fileNo, "delete related DocuNewsType"
  Print #fileNo, addTab(1); "DELETE FROM"
  Print #fileNo, addTab(2); qualTabNameDocuNewsType
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "DNPPST_OID = psOid_in"
  Print #fileNo, addTab(1); ";"

  genProcSectionHeader fileNo, "count the number of affected rows"
  Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
  Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
  
  genProcSectionHeader fileNo, "delete related DataPools"
  Print #fileNo, addTab(1); "UPDATE"
  Print #fileNo, addTab(2); g_qualTabNameUser; " U"
  Print #fileNo, addTab(1); "SET"
  Print #fileNo, addTab(2); "U.LDPLDP_OID = NULL"
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "EXISTS ("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "1"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); g_qualTabNameDataPool; " P"
  Print #fileNo, addTab(3); "WHERE"
  Print #fileNo, addTab(4); "U.LDPLDP_OID = P."; g_anOid
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "P.DPSPST_OID = psOid_in"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(1); ";"
  
  Print #fileNo, addTab(1); "DELETE FROM"
  Print #fileNo, addTab(2); g_qualTabNameWriteLock
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "wdpdpo_oid IN ("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "oid"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); g_qualTabNameDataPool
  Print #fileNo, addTab(3); "WHERE"
  Print #fileNo, addTab(4); "dpspst_oid = psOid_in"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(0); ";"
  
  Print #fileNo, addTab(1); "DELETE FROM"
  Print #fileNo, addTab(2); g_qualTabNameReleaseLock
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "rdpdpo_oid IN ("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "oid"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); g_qualTabNameDataPool
  Print #fileNo, addTab(3); "WHERE"
  Print #fileNo, addTab(4); "dpspst_oid = psOid_in"
  Print #fileNo, addTab(2); ")"
  Print #fileNo, addTab(0); ";"
  
  Print #fileNo, addTab(1); "DELETE FROM"
  Print #fileNo, addTab(2); g_qualTabNameDataPool
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); "DPSPST_OID = psOid_in"
  Print #fileNo, addTab(1); ";"

  genProcSectionHeader fileNo, "count the number of affected rows"
  Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
  Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
  
  genProcSectionHeader fileNo, "delete PS-related data in table """ & g_qualTabNameRegistryStatic & """"
  Print #fileNo, addTab(1); "DELETE FROM"
  Print #fileNo, addTab(2); g_qualTabNameRegistryStatic
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); g_anSection; " IN ('STANDARDXML', 'VDFXML')"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); g_anKey; " = 'DESTINATION'"
  Print #fileNo, addTab(3); "AND"
  Print #fileNo, addTab(2); g_anSubKey; " LIKE '%,' || RTRIM(CHAR(psOid_in)) || ',%'"
  Print #fileNo, addTab(1); ";"
    
  genProcSectionHeader fileNo, "count the number of affected rows"
  Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
  Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
  
  genProcSectionHeader fileNo, "delete ProductStructure"
  Print #fileNo, addTab(1); "DELETE FROM"
  Print #fileNo, addTab(2); g_qualTabNameProductStructureNl
  Print #fileNo, addTab(1); "WHERE"
  With g_classes.descriptors(g_classIndexProductStructure)
    Print #fileNo, addTab(2); genSurrogateKeyName(ddlType, .shortName); " = psOid_in"
  End With
  Print #fileNo, addTab(1); ";"
  
  genProcSectionHeader fileNo, "count the number of affected rows"
  Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
  Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
  
  Print #fileNo, addTab(1); "DELETE FROM"
  Print #fileNo, addTab(2); g_qualTabNameProductStructure
  Print #fileNo, addTab(1); "WHERE"
  Print #fileNo, addTab(2); g_anOid; " = psOid_in"
  Print #fileNo, addTab(1); ";"
  
  genProcSectionHeader fileNo, "count the number of affected rows"
  Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
  Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
  
  genSpLogProcExit fileNo, qualProcName, ddlType, , "psOid_in", "rowCount_out"
  
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
' ### ENDIF IVK ###




Private Sub genTabListViewElement( _
  ByRef acmEntityIndex As Integer, _
  ByRef acmEntityType As AcmAttrContainerType, _
  forGen As Boolean, _
  forNl As Boolean, _
  fileNo As Integer, _
  ByRef qualSrcTabName As String, _
  ByRef qualDstTabName As String, _
  ByRef firstCall As Boolean, _
  Optional indent As Integer = 2, _
  Optional useFilter As Boolean = True, _
  Optional ByRef filter As String = "", _
  Optional forPsDelete As Boolean = False _
)
  
  Dim listSrc() As String
  Dim listDst() As String
  listSrc = split(qualSrcTabName, ".")
  listDst = split(qualDstTabName, ".")
  
  Dim colList As String
  Dim columnsComparable As Boolean
  colList = ""
  columnsComparable = True
  
  If Not forPsDelete Then
    Dim transformation As AttributeListTransformation
    Dim tabColumns As EntityColumnDescriptors
    Dim isGenericAspect As Boolean
    isGenericAspect = False
    
    initAttributeTransformation transformation, 0
    tabColumns = nullEntityColumnDescriptors
    
    If Not forNl Then
      If acmEntityType = eactClass Then
        With g_classes.descriptors(acmEntityIndex)
          isGenericAspect = (UCase(.className) = UCase(clnGenericAspect))
        End With
      End If
    End If
    
    If forNl Then
      genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, , , edtPdm, , , 0, forGen, False, , edomNone
    Else
      genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, edtPdm, , , 0, False, forGen, edomNone
    End If

    Dim i As Integer
    For i = 1 To tabColumns.numDescriptors
      With tabColumns.descriptors(i)
        If ((.columnCategory And eacOid) Or (.columnCategory And eacCid) Or _
            (.columnCategory And eacPsOid) Or (.columnCategory And eacLrtMeta) Or _
            (.columnCategory And eacFkOidParent)) And _
           ((.columnCategory And eacRegular) = 0) Then
          ' ignore this column
        Else
          With g_domains.descriptors(.dbDomainIndex)
            If (.dataType = etBlob) Or (.dataType = etClob) Then
              columnsComparable = False
            End If
          End With
          If (.columnCategory And eacExpression) Then
            ' we do not instantiate these columns
          ElseIf isGenericAspect And (.columnCategory = eacFkOid) And _
             (Len(.columnName) = 10) And _
             ((Left(.columnName, 4) = "S0CS") Or (Left(.columnName, 4) = "N1CN") Or (Left(.columnName, 4) = "S1CT")) And _
             (Right(.columnName, 4) = "_OID") Then
            ' special treatment of GenericAspect: ignore all FK-columns corresponding to SRX-Context
            ' this is covered by the string attribute already
            ' otherwise column list is tooooo long
            ' -> ignore this column
          Else
            colList = colList & IIf(colList = "", "", ",") & .columnName
          End If
        End If
      End With
    Next i
  End If
  
  If Not firstCall Then
    Print #fileNo, addTab(indent + 1); "UNION"
  End If
  If listSrc(0) = listDst(0) Then
    Print #fileNo, addTab(indent + 0); "VALUES('"; listSrc(0); "','"; listSrc(1); "',"; _
                                        IIf(useFilter And (filter <> ""), "'" & filter & "'", "CAST(NULL AS VARCHAR(200))"); _
                                        IIf(forPsDelete, "", IIf(columnsComparable, ",1", ",0")); _
                                        IIf(forPsDelete, "", ",'" & colList & "'"); _
                                        ")"
  Else
    Print #fileNo, addTab(indent + 0); "VALUES('"; listSrc(0); "','"; listDst(0); "','"; listSrc(1); "',"; _
                                        IIf(useFilter And (filter <> ""), "'" & filter & "'", "CAST(NULL AS VARCHAR(200))"); _
                                        IIf(forPsDelete, "", IIf(columnsComparable, ",1", ",0")); _
                                        IIf(forPsDelete, "", ",'" & colList & "'"); _
                                        ")"
  End If
  firstCall = False
End Sub


Sub genTabListView( _
  fileNo As Integer, _
  ByVal thisOrgIndex As Integer, _
  srcPoolIndex As Integer, _
  dstPoolIndex As Integer, _
  Optional ddlType As DdlTypeId = edtPdm, _
  Optional indent As Integer = 1, _
  Optional forPsDelete As Boolean = False _
)
  Dim firstCall As Boolean
  firstCall = True
  
  Print #fileNo, addTab(indent + 0); "WITH V_TabList"
  Print #fileNo, addTab(indent + 0); "("
  
  If srcPoolIndex = dstPoolIndex Then
    Print #fileNo, addTab(indent + 1); "schemaName,"
  Else
    Print #fileNo, addTab(indent + 1); "srcSchemaName,"
    Print #fileNo, addTab(indent + 1); "dstSchemaName,"
  End If
  
  Print #fileNo, addTab(indent + 1); "tabName,"
  If forPsDelete Then
    Print #fileNo, addTab(indent + 1); "filter"
  Else
    Print #fileNo, addTab(indent + 1); "filter,"
    Print #fileNo, addTab(indent + 1); "colListComparable,"
    Print #fileNo, addTab(indent + 1); "colList"
  End If
  
  Print #fileNo, addTab(indent + 0); ")"
  Print #fileNo, addTab(indent + 0); "AS"
  Print #fileNo, addTab(indent + 0); "("
  
  Dim thisEntityIndex As Integer
  Dim qualSrcTabName As String
  Dim qualDstTabName As String
  Dim qualSrcTabNameGen As String
  Dim qualDstTabNameGen As String
  Dim qualSrcTabNameNl As String
  Dim qualDstTabNameNl As String
  Dim parentFkAttr As String
  
  Dim psOidFilterStr As String
  psOidFilterStr = g_anPsOid & " = <PS>"
  Dim filter As String
  
  Dim processEntity As Boolean
  Dim forLrt As Boolean
  Dim fkAttrToDiv As String
  Dim navPathFromClassToDiv As NavPathFromClassToClass
  Dim navPathFromRelToDiv As NavPathFromRelationshipToClass
  
  For thisEntityIndex = 1 To g_classes.numDescriptors
    fkAttrToDiv = ""
    With g_classes.descriptors(thisEntityIndex)
      If forPsDelete Then
        processEntity = Not (.superClassIndex > 0) And _
        ((.specificToPool <= 0) Or (.specificToPool = g_pools.descriptors(srcPoolIndex).id)) And .isPsTagged
      Else
        processEntity = Not .isCommonToOrgs And Not .isCommonToPools And Not .isLrtSpecific And _
                        Not .notAcmRelated And Not (.superClassIndex > 0) And (.specificToPool <= 0)
      End If
      If processEntity Then
        If .isPsTagged Then
          filter = psOidFilterStr
        Else
          navPathFromClassToDiv = .navPathToDiv
          If navPathFromClassToDiv.relRefIndex > 0 Then
            With g_relationships.descriptors(navPathFromClassToDiv.relRefIndex)
              fkAttrToDiv = IIf(navPathFromClassToDiv.navDirection = etLeft, .leftFkColName(ddlType), .rightFkColName(ddlType))
            End With
          End If
        
          If fkAttrToDiv <> "" Then
            filter = fkAttrToDiv & " = <DIV>"
          End If
        End If
        
        Dim i As Integer
        For i = 1 To IIf(.isUserTransactional And forPsDelete, 2, 1)
          forLrt = (i = 2)
          qualSrcTabName = genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, , forLrt)
          qualDstTabName = genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, dstPoolIndex, , forLrt)
          
          genTabListViewElement thisEntityIndex, eactClass, False, False, fileNo, qualSrcTabName, qualDstTabName, firstCall, indent + 1, _
            filter <> "", filter, forPsDelete

          If .hasNlAttrsInNonGenInclSubClasses Then
            parentFkAttr = genAttrName(conOid, ddlType, .shortName)
            qualSrcTabNameNl = genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, srcPoolIndex, , forLrt, , True)
            qualDstTabNameNl = genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, dstPoolIndex, , forLrt, , True)
            
            If forLrt Then
              Dim qualSrcTabNamePub As String
              qualSrcTabNamePub = genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, , False)
              
              genTabListViewElement thisEntityIndex, eactClass, False, True, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, _
                filter <> "", parentFkAttr & " IN (SELECT " & g_anOid & " FROM " & _
                IIf(forPsDelete, qualSrcTabName, "<REFSCHEMA>." & getUnqualObjName(qualSrcTabName)) & _
                " WHERE " & filter & ") OR " & parentFkAttr & " IN (SELECT " & g_anOid & " FROM " & _
                IIf(forPsDelete, qualSrcTabNamePub, "<REFSCHEMA>." & getUnqualObjName(qualSrcTabNamePub)) & _
                " WHERE " & filter & ")", forPsDelete
            Else
              genTabListViewElement thisEntityIndex, eactClass, False, True, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, _
                filter <> "", parentFkAttr & " IN (SELECT " & g_anOid & " FROM " & _
                IIf(forPsDelete, qualSrcTabName, "<REFSCHEMA>." & getUnqualObjName(qualSrcTabName)) & _
                " WHERE " & filter & ")", forPsDelete
            End If
          End If
          
          If .isGenForming And Not .hasNoIdentity Then
            qualSrcTabNameGen = genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, True, forLrt)
            qualDstTabNameGen = genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, dstPoolIndex, True, forLrt)
          
            parentFkAttr = genAttrName(conOid, ddlType, .shortName)
            
            genTabListViewElement thisEntityIndex, eactClass, True, False, fileNo, qualSrcTabNameGen, qualDstTabNameGen, firstCall, indent + 1, _
              filter <> "", filter, forPsDelete
          
            If .hasNlAttrsInGenInclSubClasses Then
                qualSrcTabNameNl = genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, srcPoolIndex, True, forLrt, , True)
                qualDstTabNameNl = genQualTabNameByClassIndex(.classIndex, ddlType, thisOrgIndex, dstPoolIndex, True, forLrt, , True)
              
              If forLrt Then
                Dim qualSrcTabNameGenPub As String
                qualSrcTabNameGenPub = genQualTabNameByClassIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, True, False)
            
                genTabListViewElement thisEntityIndex, eactClass, True, True, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, _
                  filter <> "", parentFkAttr & " IN (SELECT " & g_anOid & " FROM " & _
                  IIf(forPsDelete, qualSrcTabNameGen, "<REFSCHEMA>." & getUnqualObjName(qualSrcTabNameGen)) & _
                  " WHERE " & filter & ") OR " & parentFkAttr & " IN (SELECT " & g_anOid & " FROM " & _
                  IIf(forPsDelete, qualSrcTabNameGenPub, "<REFSCHEMA>." & getUnqualObjName(qualSrcTabNameGenPub)) & _
                  " WHERE " & filter & ")", forPsDelete
              Else
                genTabListViewElement thisEntityIndex, eactClass, True, True, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, _
                  filter <> "", parentFkAttr & " IN (SELECT " & g_anOid & " FROM " & _
                  IIf(forPsDelete, qualSrcTabNameGen, "<REFSCHEMA>." & getUnqualObjName(qualSrcTabNameGen)) & _
                  " WHERE " & filter & ")", forPsDelete
              End If
            End If
          End If
        Next i
      End If
    End With
  Next thisEntityIndex
  
  For thisEntityIndex = 1 To g_relationships.numDescriptors
    fkAttrToDiv = ""
    With g_relationships.descriptors(thisEntityIndex)
      If forPsDelete Then
        processEntity = Not .notAcmRelated And (.specificToPool <= 0) And .isPsTagged And _
                        ((.maxLeftCardinality < 0 And .maxRightCardinality < 0) Or .isNl)
      Else
        processEntity = Not .isCommonToOrgs And Not .isCommonToPools And Not .isLrtSpecific And _
                        Not .notAcmRelated And (.specificToPool <= 0) And _
                        ((.maxLeftCardinality < 0 And .maxRightCardinality < 0) Or .isNl)
      End If

      If processEntity Then
        If .isPsTagged Then
          filter = psOidFilterStr
        Else
          navPathFromRelToDiv = .navPathToDiv
          
          Dim navToDivRelRefIndex As Integer ' follow this relationship when navigating to Division
          Dim navToDivDirection As RelNavigationDirection ' indicates wheter we need to follow left or right hand side to navigate to Division
          Dim navToFirstClassToDivDirection As RelNavigationDirection ' if we are dealing with a relationship, when navigating to 'Division' we need to first follow left or right hand side to get to a Class from where we step further
          Dim navRefClassShortName As String
          Dim fkAttrToClass As String
          Dim navRefClassIndex As Integer
          
          navToFirstClassToDivDirection = .navPathToDiv.navDirectionToClass
          navToDivRelRefIndex = -1
          navToDivDirection = -1
          If navToFirstClassToDivDirection = etLeft Then
            ' we need to follow relationship to left -> figure out what the complete path to Division is
            navRefClassIndex = .leftEntityIndex
            navRefClassShortName = g_classes.descriptors(.leftEntityIndex).shortName
            fkAttrToClass = genSurrogateKeyName(ddlType, navRefClassShortName)
            With g_classes.descriptors(.leftEntityIndex)
              navToDivRelRefIndex = .navPathToDiv.relRefIndex
              navToDivDirection = .navPathToDiv.navDirection
            End With
          ElseIf navToFirstClassToDivDirection = etRight Then
            ' we need to follow relationship to right -> figure out what the complete path to Division is
            navRefClassIndex = .rightEntityIndex
            navRefClassShortName = g_classes.descriptors(.rightEntityIndex).shortName
            fkAttrToClass = genSurrogateKeyName(ddlType, navRefClassShortName)
            With g_classes.descriptors(.rightEntityIndex)
              navToDivRelRefIndex = .navPathToDiv.relRefIndex
              navToDivDirection = .navPathToDiv.navDirection
            End With
          End If
          If navToDivRelRefIndex > 0 Then
            With g_relationships.descriptors(navToDivRelRefIndex)
              If navToDivDirection = etLeft Then
                fkAttrToDiv = .leftFkColName(ddlType)
              Else
                fkAttrToDiv = .rightFkColName(ddlType)
              End If
            End With
          End If

          If fkAttrToDiv <> "" Then
            Dim qualRefTabName As String
            qualRefTabName = genQualTabNameByClassIndex(navRefClassIndex, ddlType, thisOrgIndex, dstPoolIndex)
            filter = fkAttrToClass & " IN (SELECT OID FROM " & _
                     IIf(forPsDelete, qualRefTabName, "<REFSCHEMA>." & getUnqualObjName(qualRefTabName)) & _
                     " WHERE " & fkAttrToDiv & " = <DIV>)"
          End If
        End If
        
        For i = 1 To IIf(.isUserTransactional And forPsDelete, 2, 1)
          forLrt = (i = 2)
          qualSrcTabName = genQualTabNameByRelIndex(thisEntityIndex, ddlType, thisOrgIndex, srcPoolIndex, forLrt)
          qualDstTabName = genQualTabNameByRelIndex(thisEntityIndex, ddlType, thisOrgIndex, dstPoolIndex, forLrt)

          genTabListViewElement thisEntityIndex, eactRelationship, False, False, fileNo, qualSrcTabName, qualDstTabName, firstCall, indent + 1, _
            filter <> "", filter, forPsDelete
          
          If .nlAttrRefs.numDescriptors > 0 Then
            parentFkAttr = genAttrName(conOid, ddlType, .shortName)
            qualSrcTabNameNl = genQualTabNameByRelIndex(.relIndex, ddlType, thisOrgIndex, srcPoolIndex, forLrt, , True)
            qualDstTabNameNl = genQualTabNameByRelIndex(.relIndex, ddlType, thisOrgIndex, dstPoolIndex, forLrt, , True)
            
            genTabListViewElement thisEntityIndex, eactRelationship, False, True, fileNo, qualSrcTabNameNl, qualDstTabNameNl, firstCall, indent + 1, _
              filter <> "", parentFkAttr & " IN (SELECT " & g_anOid & " FROM " & _
              qualSrcTabName & " WHERE " & filter & ")", forPsDelete
          End If
        Next i
      End If
    End With
  Next thisEntityIndex
  Print #fileNo, addTab(indent + 0); ")"
End Sub
