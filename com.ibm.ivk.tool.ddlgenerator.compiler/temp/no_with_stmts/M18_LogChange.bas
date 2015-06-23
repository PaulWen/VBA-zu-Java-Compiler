 Attribute VB_Name = "M18_LogChange"
 Option Explicit
 
 Private Const processingStep = 1
 
 Private Const generateLogChangeTrigger = True
 
 Private Sub genInsertChangeLogBroadcastCall( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim entityName As String
   Dim classId As String
   Dim ahClassId As String
 
     entityName = UCase(g_classes.descriptors(acmEntityIndex).className)
     classId = g_classes.descriptors(acmEntityIndex).classIdStr
     ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     If g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr <> "" Then
       ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     Else
       ahClassId = g_classes.descriptors(acmEntityIndex).classIdStr
     End If
 
   Dim qualProcNameClBroadCast As String
   qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)
 
   printComment "determine User id", fileNo, , 1
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
   Print #fileNo,
   printComment "create Changelog Records", fileNo, , 1
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL"
   Print #fileNo, addTab(2); qualProcNameClBroadCast
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "'"; classId; "',"
   Print #fileNo, addTab(2); "'C',"
   Print #fileNo, addTab(2); "'"; ahClassId; "',"
   Print #fileNo, addTab(2); "NEWRECORD.OID,"
   Print #fileNo, addTab(2); "'"; entityName; "',"
   Print #fileNo, addTab(2); "NEWRECORD.OID,"
   Print #fileNo, addTab(2); "v_cdUserId,"
   If entityName = "AGGREGATIONNODE" Then
     Print #fileNo, addTab(2); "NEWRECORD.AVDDIV_OID,"
   ElseIf entityName = "ENDNODE" Then
     Print #fileNo, addTab(2); "NEWRECORD.EVDDIV_OID,"
   Else
     Print #fileNo, addTab(2); "NULL,"
   End If
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(2); "v_numClRecords"
   Print #fileNo, addTab(1); ");"
 
 End Sub
 
 Private Sub genInsertRelChangeLogBroadcastCall( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim entityName As String
   Dim classId As String
   Dim leftEntityIndex As Integer
   Dim leftFkColName As String
   Dim ahClassId As String
 
     entityName = UCase(g_relationships.descriptors(acmEntityIndex).relName)
     classId = g_relationships.descriptors(acmEntityIndex).relIdStr
     leftEntityIndex = g_relationships.descriptors(acmEntityIndex).leftEntityIndex
     leftFkColName = g_relationships.descriptors(acmEntityIndex).leftFkColName(1)
 
     ahClassId = g_classes.descriptors(leftEntityIndex).classIdStr
 
   Dim qualProcNameClBroadCast As String
   qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)
 
   printComment "determine User id", fileNo, , 1
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
   Print #fileNo,
   printComment "create Changelog Records", fileNo, , 1
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL"
   Print #fileNo, addTab(2); qualProcNameClBroadCast
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "'"; classId; "',"
   Print #fileNo, addTab(2); "'R',"
   Print #fileNo, addTab(2); "'"; ahClassId; "',"
   Print #fileNo, addTab(2); "NEWRECORD."; leftFkColName; ","
   Print #fileNo, addTab(2); "'"; entityName; "',"
   Print #fileNo, addTab(2); "NEWRECORD.OID,"
   Print #fileNo, addTab(2); "v_cdUserId,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(2); "v_numClRecords"
   Print #fileNo, addTab(1); ");"
 
 End Sub
 
 Private Sub genUpdateChangeLogBroadcastCall( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   ByRef colDesc As EntityColumnDescriptor, _
   ByVal thisOrgIndex As Integer, _
   nlText As Boolean, _
   distNlText As Boolean, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim entityName As String
   Dim classId As String
   Dim ahClassId As String
   Dim shortName As String
   Dim columnName As String
   Dim dataType As Integer
   Dim tabColumns As EntityColumnDescriptors
   Dim isNullable As Boolean
 
     If nlText And Not distNlText Then
       entityName = UCase(g_classes.descriptors(acmEntityIndex).className) & "_NL_TEXT"
     Else
       entityName = UCase(g_classes.descriptors(acmEntityIndex).className)
     End If
     shortName = g_classes.descriptors(acmEntityIndex).shortName
     classId = g_classes.descriptors(acmEntityIndex).classIdStr
     ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     If g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr <> "" Then
       ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     Else
       ahClassId = g_classes.descriptors(acmEntityIndex).classIdStr
     End If

     columnName = colDesc.columnName
     dataType = g_domains.descriptors(colDesc.dbDomainIndex).dataType
     isNullable = colDesc.isNullable

   If distNlText Then
     ' map classId / ahClassId to parent
     ' if approach becomes generic concept this hardcoding should be replaced
     If classId = "11025" Then
       classId = "11022"
       ahClassId = "11022"
     ElseIf classId = "11026" Then
       classId = "11023"
       ahClassId = "11023"
     ElseIf classId = "11027" Then
       classId = "11024"
       ahClassId = "11024"
     End If
   End If
 
   Dim qualProcNameClBroadCast As String
   qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)
 
   Print #fileNo,
   If isNullable Or (nlText And Not distNlText) Then
     If dataType = 1 Or dataType = 2 Or dataType = 3 Or dataType = 17 Then
       Print #fileNo, addTab(1); "IF COALESCE(NEWRECORD."; columnName; ", -1) <> COALESCE(OLDRECORD."; columnName; ", -1) THEN"
     Else
       Print #fileNo, addTab(1); "IF COALESCE(NEWRECORD."; columnName; ", '') <> COALESCE(OLDRECORD."; columnName; ", '') THEN"
     End If
   Else
     Print #fileNo, addTab(1); "IF NEWRECORD."; columnName; " <> OLDRECORD."; columnName; " THEN"
   End If
   printComment "create Changelog Records", fileNo, , 2
   Print #fileNo, addTab(2); "CALL"
   Print #fileNo, addTab(3); qualProcNameClBroadCast
   Print #fileNo, addTab(2); "("
   If distNlText Then
     Print #fileNo, addTab(3); genOrgIdByIndex(thisOrgIndex, ddlType, True); ","
   Else
     Print #fileNo, addTab(3); "NULL,"
   End If
   Print #fileNo, addTab(3); "NULL,"
   Print #fileNo, addTab(3); "'"; classId; "',"
   Print #fileNo, addTab(3); "'C',"
   Print #fileNo, addTab(3); "'"; ahClassId; "',"
   If nlText Then
     If distNlText Then
       If entityName = "GROUP_DIST_NL_TEXT" Then
         Print #fileNo, addTab(3); "OLDRECORD.GNLGRP_OID,"
       ElseIf entityName = "AGGREGATIONNODE_DIST_NL_TEXT" Then
         Print #fileNo, addTab(3); "OLDRECORD.ANLANO_OID,"
       ElseIf entityName = "ENDNODE_DIST_NL_TEXT" Then
         Print #fileNo, addTab(3); "OLDRECORD.ENLENO_OID,"
       Else
         Print #fileNo, addTab(3); "OLDRECORD.OID,"
       End If
     Else
       Print #fileNo, addTab(3); "OLDRECORD."; shortName; "_OID,"
     End If
     Print #fileNo, addTab(3); "1,"
   Else
     Print #fileNo, addTab(3); "OLDRECORD.OID,"
     Print #fileNo, addTab(3); "0,"
   End If
   Print #fileNo, addTab(3); "'"; entityName; "',"
   Print #fileNo, addTab(3); "'"; columnName; "',"
   Print #fileNo, addTab(3); "OLDRECORD.OID,"
   If dataType = 6 Then
     'dataType String
     Print #fileNo, addTab(3); "4,"
     Print #fileNo, addTab(3); "NULL,"
     Print #fileNo, addTab(3); "NULL,"
     Print #fileNo, addTab(3); "OLDRECORD."; columnName; ","
     Print #fileNo, addTab(3); "NEWRECORD."; columnName; ","
     Print #fileNo, addTab(3); "NULL,"
     Print #fileNo, addTab(3); "NULL,"
   ElseIf dataType = 1 Or dataType = 2 Or dataType = 17 Then
     'dataType Enum or Integer or Boolean
     Print #fileNo, addTab(3); "1,"
     Print #fileNo, addTab(3); "NULL,"
     Print #fileNo, addTab(3); "NULL,"
     Print #fileNo, addTab(3); "NULL,"
     Print #fileNo, addTab(3); "NULL,"
     Print #fileNo, addTab(3); "OLDRECORD."; columnName; ","
     Print #fileNo, addTab(3); "NEWRECORD."; columnName; ","
   ElseIf dataType = 3 Then
     'dataType BigInteger
     Print #fileNo, addTab(3); "6,"
     Print #fileNo, addTab(3); "OLDRECORD."; columnName; ","
     Print #fileNo, addTab(3); "NEWRECORD."; columnName; ","
   End If
   Print #fileNo, addTab(3); "v_cdUserId,"
   If entityName = "AGGREGATIONNODE" Then
     Print #fileNo, addTab(3); "NEWRECORD.AVDDIV_OID,"
   ElseIf entityName = "ENDNODE" Then
     Print #fileNo, addTab(3); "NEWRECORD.EVDDIV_OID,"
   ElseIf entityName = "AGGREGATIONNODE_NL_TEXT" Then
     Print #fileNo, addTab(3); "(SELECT AVDDIV_OID FROM VL6CMET.V_AGGREGATIONNODE_LC WHERE OID = NEWRECORD."; shortName; "_OID),"
   ElseIf entityName = "ENDNODE_NL_TEXT" Then
     Print #fileNo, addTab(3); "(SELECT EVDDIV_OID FROM VL6CMET.V_ENDNODE_LC WHERE OID = NEWRECORD."; shortName; "_OID),"
   Else
     Print #fileNo, addTab(3); "NULL,"
   End If
   Print #fileNo, addTab(3); "NULL,"
   Print #fileNo, addTab(3); "2,"
   Print #fileNo, addTab(3); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(3); "v_numClRecords"
   Print #fileNo, addTab(2); ");"
   Print #fileNo, addTab(1); "END IF;"
 
 End Sub
 
 Private Sub genInsertDistNlChangeLogBroadcastCall( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim entityName As String
   Dim classId As String
   Dim ahClassId As String
 
     entityName = UCase(g_classes.descriptors(acmEntityIndex).className)
     classId = g_classes.descriptors(acmEntityIndex).classIdStr
     ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr

   ' map classId / ahClassId to parent
   ' if approach becomes generic concept this hardcoding should be replaced
   If classId = "11025" Then
     classId = "11022"
     ahClassId = "11022"
   ElseIf classId = "11026" Then
     classId = "11023"
     ahClassId = "11023"
   ElseIf classId = "11027" Then
     classId = "11024"
     ahClassId = "11024"
   End If
 
   Dim qualProcNameClBroadCast As String
   qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)
 
   printComment "determine User id", fileNo, , 1
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
   Print #fileNo,
   printComment "create Changelog Records", fileNo, , 1
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL"
   Print #fileNo, addTab(2); qualProcNameClBroadCast
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); genOrgIdByIndex(thisOrgIndex, ddlType, True); ","
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "'"; classId; "',"
   Print #fileNo, addTab(2); "'C',"
   Print #fileNo, addTab(2); "'"; ahClassId; "',"
   If entityName = "GROUP_DIST_NL_TEXT" Then
     Print #fileNo, addTab(2); "NEWRECORD.GNLGRP_OID,"
   ElseIf entityName = "AGGREGATIONNODE_DIST_NL_TEXT" Then
     Print #fileNo, addTab(2); "NEWRECORD.ANLANO_OID,"
   ElseIf entityName = "ENDNODE_DIST_NL_TEXT" Then
     Print #fileNo, addTab(2); "NEWRECORD.ENLENO_OID,"
   Else
     Print #fileNo, addTab(2); "NEWRECORD.OID,"
   End If
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); "'"; entityName; "',"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NEWRECORD.OID,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "v_cdUserId,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(2); "v_numClRecords"
   Print #fileNo, addTab(1); ");"
 
 End Sub
 
 Private Sub genInsertNlChangeLogBroadcastCall( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim entityName As String
   Dim classId As String
   Dim ahClassId As String
   Dim shortName As String
 
     entityName = UCase(g_classes.descriptors(acmEntityIndex).className) & "_NL_TEXT"
     shortName = g_classes.descriptors(acmEntityIndex).shortName
     classId = g_classes.descriptors(acmEntityIndex).classIdStr
     ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     If g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr <> "" Then
       ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     Else
       ahClassId = g_classes.descriptors(acmEntityIndex).classIdStr
     End If
 
   Dim qualProcNameClBroadCast As String
   qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)
 
   printComment "determine User id", fileNo, , 1
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
   Print #fileNo,
   printComment "create Changelog Records", fileNo, , 1
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL"
   Print #fileNo, addTab(2); qualProcNameClBroadCast
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "'"; classId; "',"
   Print #fileNo, addTab(2); "'C',"
   Print #fileNo, addTab(2); "'"; ahClassId; "',"
   Print #fileNo, addTab(2); "NEWRECORD."; shortName; "_OID,"
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); "'"; entityName; "',"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NEWRECORD.OID,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "v_cdUserId,"
   'Special handling: EndNode/AggregationNode includes parent reference in insert changeLog
   If entityName = "AGGREGATIONNODE_NL_TEXT" Then
     Print #fileNo, addTab(2); "(SELECT AVDDIV_OID FROM VL6CMET.V_AGGREGATIONNODE_LC WHERE OID = NEWRECORD."; shortName; "_OID),"
   ElseIf entityName = "ENDNODE_NL_TEXT" Then
     Print #fileNo, addTab(2); "(SELECT EVDDIV_OID FROM VL6CMET.V_ENDNODE_LC WHERE OID = NEWRECORD."; shortName; "_OID),"
   Else
     Print #fileNo, addTab(2); "NULL,"
   End If
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(2); "v_numClRecords"
   Print #fileNo, addTab(1); ");"
 
 End Sub
 
 Private Sub genDeleteChangeLogBroadcastCall( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim entityName As String
   Dim classId As String
   Dim ahClassId As String
 
     entityName = UCase(g_classes.descriptors(acmEntityIndex).className)
     classId = g_classes.descriptors(acmEntityIndex).classIdStr
     ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     If g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr <> "" Then
       ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     Else
       ahClassId = g_classes.descriptors(acmEntityIndex).classIdStr
     End If
 
   Dim qualProcNameClBroadCast As String
   qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)
 
   printComment "determine User id", fileNo, , 1
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
   Print #fileNo,
   printComment "create Changelog Records", fileNo, , 1
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL"
   Print #fileNo, addTab(2); qualProcNameClBroadCast
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "'"; classId; "',"
   Print #fileNo, addTab(2); "'C',"
   Print #fileNo, addTab(2); "'"; ahClassId; "',"
   Print #fileNo, addTab(2); "OLDRECORD.OID,"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "'"; entityName; "',"
   'Special handling: EndNode/AggregationNode includes parent reference in delete changeLog
   If entityName = "AGGREGATIONNODE" Then
     Print #fileNo, addTab(2); "'ANPANO_OID',"
     Print #fileNo, addTab(2); "OLDRECORD.OID,"
     Print #fileNo, addTab(2); "(CASE WHEN OLDRECORD.ANPANO_OID IS NULL THEN NULL ELSE 6 END),"
     Print #fileNo, addTab(2); "OLDRECORD.ANPANO_OID,"
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "v_cdUserId,"
     Print #fileNo, addTab(2); "OLDRECORD.AVDDIV_OID,"
   ElseIf entityName = "ENDNODE" Then
     Print #fileNo, addTab(2); "'ENPANO_OID',"
     Print #fileNo, addTab(2); "OLDRECORD.OID,"
     Print #fileNo, addTab(2); "6,"
     Print #fileNo, addTab(2); "OLDRECORD.ENPANO_OID,"
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "v_cdUserId,"
     Print #fileNo, addTab(2); "OLDRECORD.EVDDIV_OID,"
   Else
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "OLDRECORD.OID,"
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "v_cdUserId,"
     Print #fileNo, addTab(2); "NULL,"
   End If
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "3,"
   Print #fileNo, addTab(2); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(2); "v_numClRecords"
   Print #fileNo, addTab(1); ");"

 End Sub
 
 Private Sub genDeleteRelChangeLogBroadcastCall( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim entityName As String
   Dim classId As String
   Dim leftEntityIndex As Integer
   Dim leftFkColName As String
   Dim ahClassId As String
 
     entityName = UCase(g_relationships.descriptors(acmEntityIndex).relName)
     classId = g_relationships.descriptors(acmEntityIndex).relIdStr
     leftEntityIndex = g_relationships.descriptors(acmEntityIndex).leftEntityIndex
     leftFkColName = g_relationships.descriptors(acmEntityIndex).leftFkColName(1)
 
     ahClassId = g_classes.descriptors(leftEntityIndex).classIdStr
 
   Dim qualProcNameClBroadCast As String
   qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)
 
   printComment "determine User id", fileNo, , 1
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
   Print #fileNo,
   printComment "create Changelog Records", fileNo, , 1
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL"
   Print #fileNo, addTab(2); qualProcNameClBroadCast
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "'"; classId; "',"
   Print #fileNo, addTab(2); "'R',"
   Print #fileNo, addTab(2); "'"; ahClassId; "',"
   Print #fileNo, addTab(2); "OLDRECORD."; leftFkColName; ","
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "'"; entityName; "',"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "OLDRECORD.OID,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "v_cdUserId,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "3,"
   Print #fileNo, addTab(2); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(2); "v_numClRecords"
   Print #fileNo, addTab(1); ");"

 End Sub
 Private Sub genDeleteDistNlChangeLogBroadcastCall( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim entityName As String
   Dim classId As String
   Dim ahClassId As String
 
     entityName = UCase(g_classes.descriptors(acmEntityIndex).className)
     classId = g_classes.descriptors(acmEntityIndex).classIdStr
     ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr

   ' map classId / ahClassId to parent
   ' if approach becomes generic concept this hardcoding should be replaced
   If classId = "11025" Then
     classId = "11022"
     ahClassId = "11022"
   ElseIf classId = "11026" Then
     classId = "11023"
     ahClassId = "11023"
   ElseIf classId = "11027" Then
     classId = "11024"
     ahClassId = "11024"
   End If
 
   Dim qualProcNameClBroadCast As String
   qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)
 
   printComment "determine User id", fileNo, , 1
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
   Print #fileNo,
   printComment "create Changelog Records", fileNo, , 1
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL"
   Print #fileNo, addTab(2); qualProcNameClBroadCast
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); genOrgIdByIndex(thisOrgIndex, ddlType, True); ","
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "'"; classId; "',"
   Print #fileNo, addTab(2); "'C',"
   Print #fileNo, addTab(2); "'"; ahClassId; "',"
   If entityName = "GROUP_DIST_NL_TEXT" Then
     Print #fileNo, addTab(2); "OLDRECORD.GNLGRP_OID,"
   ElseIf entityName = "AGGREGATIONNODE_DIST_NL_TEXT" Then
     Print #fileNo, addTab(2); "OLDRECORD.ANLANO_OID,"
   ElseIf entityName = "ENDNODE_DIST_NL_TEXT" Then
     Print #fileNo, addTab(2); "OLDRECORD.ENLENO_OID,"
   Else
     Print #fileNo, addTab(2); "OLDRECORD.OID,"
   End If
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); "'"; entityName; "',"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "OLDRECORD.OID,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "v_cdUserId,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "3,"
   Print #fileNo, addTab(2); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(2); "v_numClRecords"
   Print #fileNo, addTab(1); ");"

 End Sub
 
 Private Sub genDeleteNlChangeLogBroadcastCall( _
   fileNo As Integer, _
   ByRef acmEntityIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim entityName As String
   Dim classId As String
   Dim ahClassId As String
   Dim shortName As String
 
     entityName = UCase(g_classes.descriptors(acmEntityIndex).className) & "_NL_TEXT"
     shortName = g_classes.descriptors(acmEntityIndex).shortName
     classId = g_classes.descriptors(acmEntityIndex).classIdStr
     ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     If g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr <> "" Then
       ahClassId = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
     Else
       ahClassId = g_classes.descriptors(acmEntityIndex).classIdStr
     End If
 
   Dim qualProcNameClBroadCast As String
   qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)
 
   printComment "determine User id", fileNo, , 1
   Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
   Print #fileNo,
   printComment "create Changelog Records", fileNo, , 1
   Print #fileNo,
   Print #fileNo, addTab(1); "CALL"
   Print #fileNo, addTab(2); qualProcNameClBroadCast
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "'"; classId; "',"
   Print #fileNo, addTab(2); "'C',"
   Print #fileNo, addTab(2); "'"; ahClassId; "',"
   Print #fileNo, addTab(2); "OLDRECORD."; shortName; "_OID,"
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); "'"; entityName; "',"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "OLDRECORD.OID,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "v_cdUserId,"
   'Special handling: EndNode/AggregationNode includes parent reference in delete changeLog
   If entityName = "AGGREGATIONNODE_NL_TEXT" Then
     Print #fileNo, addTab(2); "(SELECT AVDDIV_OID FROM VL6CMET.V_AGGREGATIONNODE_LC WHERE OID = OLDRECORD."; shortName; "_OID),"
   ElseIf entityName = "ENDNODE_NL_TEXT" Then
     Print #fileNo, addTab(2); "(SELECT EVDDIV_OID FROM VL6CMET.V_ENDNODE_LC WHERE OID = OLDRECORD."; shortName; "_OID),"
   Else
     Print #fileNo, addTab(2); "NULL,"
   End If
   Print #fileNo, addTab(2); "NULL,"
   Print #fileNo, addTab(2); "3,"
   Print #fileNo, addTab(2); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(2); "v_numClRecords"
   Print #fileNo, addTab(1); ");"
 
 End Sub
 
 
 
 Private Sub genLogChangeSupportDdlForEntity( _
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
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim entityInsertable As Boolean
   Dim entityUpdatable As Boolean
   Dim entityDeletable As Boolean
   Dim isCtoAliasCreated As Boolean
   Dim sectionIndex As Integer
   Dim sectionName As String
   Dim noAlias As Boolean
   Dim useSurrogateKey As Boolean
 ' ### IF IVK ###
   Dim isPsTagged As Boolean
   Dim psTagOptional As Boolean
   Dim ignorePsRegVarOnInsertDelete As Boolean
 ' ### ELSE ###
 '
 ' entityInsertable = True
 ' entityUpdatable = True
 ' entityDeletable = True
 ' ### ENDIF IVK ###
 
 If acmEntityType = eactClass Then
       sectionIndex = g_classes.descriptors(acmEntityIndex).sectionIndex
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Class"
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isCtoAliasCreated = g_classes.descriptors(acmEntityIndex).isCtoAliasCreated
       noAlias = g_classes.descriptors(acmEntityIndex).noAlias
       useSurrogateKey = g_classes.descriptors(acmEntityIndex).useSurrogateKey
 ' ### IF IVK ###
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = g_classes.descriptors(acmEntityIndex).psTagOptional
       entityInsertable = (g_classes.descriptors(acmEntityIndex).updateMode And eupmInsert)
       entityUpdatable = (g_classes.descriptors(acmEntityIndex).updateMode And eupmUpdate)
       entityDeletable = (g_classes.descriptors(acmEntityIndex).updateMode And eupmDelete)
       ignorePsRegVarOnInsertDelete = g_classes.descriptors(acmEntityIndex).ignPsRegVarOnInsDel
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactRelationship Then
       sectionIndex = g_relationships.descriptors(acmEntityIndex).sectionIndex
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Relationship"
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isCtoAliasCreated = g_relationships.descriptors(acmEntityIndex).isCtoAliasCreated
       noAlias = g_relationships.descriptors(acmEntityIndex).noAlias
       useSurrogateKey = True ' ???? FIXME
 ' ### IF IVK ###
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       psTagOptional = False
       entityInsertable = True
       entityUpdatable = True
       entityDeletable = True
       ignorePsRegVarOnInsertDelete = False
 ' ### ENDIF IVK ###
   End If

 ' ### IF IVK ###
   Dim supportTriggerForPsTag As Boolean
   supportTriggerForPsTag = True

   Dim poolSupportPsTaggingTrigger As Boolean
   poolSupportPsTaggingTrigger = True
 ' ### ENDIF IVK ###

   Dim poolSupportLrt As Boolean
   poolSupportLrt = False

   If ddlType = edtPdm And thisPoolIndex > 0 Then
       poolSupportLrt = g_pools.descriptors(thisPoolIndex).supportLrt
 ' ### IF IVK ###
       supportTriggerForPsTag = g_pools.descriptors(thisPoolIndex).supportViewsForPsTag
       poolSupportPsTaggingTrigger = g_pools.descriptors(thisPoolIndex).supportTriggerForPsTag
 ' ### ENDIF IVK ###
   End If

 ' ### IF IVK ###
   If isPsTagged Then
     ' included in PS-tagging-views
     Exit Sub
   End If

 ' ### ENDIF IVK ###
   If isUserTransactional And g_genLrtSupport Then
     ' included in LRT-views
     Exit Sub
   End If

   Dim transformation As AttributeListTransformation
   Dim qualViewName As String
   Dim qualNlViewName As String
   Dim qualViewNameLdm  As String
   Dim qualNlViewNameLdm  As String

   Dim qualTabName As String
   Dim qualNlTabName As String
   qualTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen)

   Dim tabQualifier As String
   tabQualifier = UCase(entityShortName)

   If generateLogChangeView Then
     qualViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, , , , "LC")

     printSectionHeader "View supporting <logChange>-columns for table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo

     Print #fileNo,
     Print #fileNo, "CREATE VIEW"
     Print #fileNo, addTab(1); qualViewName
     Print #fileNo, "("
 
 ' ### IF IVK ###
     genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, edomListNonLrt Or edomListVirtual
 ' ### ELSE IVK ###
 '   genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 1, False, forGen, edomListNonLrt
 ' ### ENDIF IVK ###
 
     Print #fileNo, ")"
     Print #fileNo, "AS"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "SELECT"
 
     initAttributeTransformation transformation, 0, , , , tabQualifier & "."
 ' ### IF IVK ###
     genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , _
       False, forGen, edomListNonLrt Or edomValueVirtual
 ' ### ELSE IVK ###
 '   genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt
 ' ### ENDIF IVK ###
 
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabName; " "; tabQualifier
 
 ' ### IF IVK ###
     If isPsTagged Then
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "("; gc_db2RegVarPsOid; " = '')"
       If psTagOptional Then
         Print #fileNo, addTab(4); "OR"
         Print #fileNo, addTab(3); "("; tabQualifier; "."; g_anPsOid; " IS NULL)"
       End If

       Print #fileNo, addTab(4); "OR"
       Print #fileNo, addTab(3); "("; tabQualifier; "."; g_anPsOid; " = "; g_activePsOidDdl; ")"
       Print #fileNo, addTab(2); ")"
     End If

 ' ### ENDIF IVK ###
     Print #fileNo, addTab(0); ")"

     Print #fileNo, gc_sqlCmdDelim

     If ddlType = edtPdm And Not noAlias Then
       qualViewNameLdm = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, edtLdm, thisOrgIndex, thisPoolIndex, forGen, , , , , "LC")
 ' ### IF IVK ###
       genAliasDdl sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
                   qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, False, False, False, False, _
                   "<logChange>-View """ & sectionName & "." & entityName & """", , , _
                   False, , , True
 ' ### ELSE IVK ###
 '     genAliasDdl sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
 '                 qualViewNameLdm, qualViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, False, _
 '                 "<logChange>-View """ & sectionName & "." & entityName & """", , , True
 ' ### ENDIF IVK ###
     End If

     If qualTabName = "VL6CMET.GROUP" Or qualTabName = "VL6CMET.AGGREGATIONNODE" Or qualTabName = "VL6CMET.ENDNODE" _
     Then
       Print #fileNo, addTab(1); "-- VIEW for NL Entity here - "; qualTabName
       qualNlTabName = qualTabName & "_NL_TEXT"

       qualNlViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, , True)

       printSectionHeader "View as base for triggers for table """ & qualNlTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo

       Print #fileNo,
       Print #fileNo, "CREATE VIEW"
       Print #fileNo, addTab(1); qualNlViewName
       Print #fileNo, "("
 
       genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomListNonLrt Or edomMqtLrt Or edomListVirtual Or edomListExpression
 
       Print #fileNo, ")"
       Print #fileNo, "AS"
       Print #fileNo, addTab(0); "("
       Print #fileNo, addTab(1); "SELECT"
 
       initAttributeTransformation transformation, 0, , , , tabQualifier & "."
       ' genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , _
       ' False, forGen, edomAll
       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt Or edomValueVirtual
       ' genNlsTransformedAttrListForEntity g_classIndexLrt, eactClass, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , _
       ' edomListNonLrt

       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualNlTabName; " "; tabQualifier
 
       Print #fileNo, addTab(0); ")"

       Print #fileNo, gc_sqlCmdDelim

       'initAttributeTransformation transformation, 0
       'transformation.doCollectVirtualAttrDescriptors = True
       'transformation.doCollectAttrDescriptors = True
       'setAttributeTransformationContext transformation, thisOrgIndex, thisPoolIndex, gc_newRecordName

       If ddlType = edtPdm And Not noAlias Then
         qualNlViewNameLdm = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, edtLdm, thisOrgIndex, thisPoolIndex, forGen, , , True, "", "NL_TEXT")
         genAliasDdl sectionIndex, entityName, isCommonToOrgs, isCommonToPools, True, _
                     qualViewNameLdm, qualNlViewName, isCtoAliasCreated, ddlType, thisOrgIndex, thisPoolIndex, edatView, forGen, False, False, False, False, _
                     " NL-View """ & sectionName & "." & entityName & "_NL_TEXT""", "NL_TEXT", , _
                     False, , , True
       End If

     End If
 

   End If

   If generateLogChangeTrigger Then
     Dim qualTriggerName As String
     Dim qualNlTriggerName As String
     Dim broadcastChanges As Boolean
     Dim broadcastForDist As Boolean
     Dim broadcastForNl As Boolean
     Dim broadcastForRel As Boolean
     Dim hasDistTable As Boolean

     ' ####################################################################################################################
     ' #    INSERT Trigger
     ' ####################################################################################################################

     qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "LC_INS")

     printSectionHeader "Insert-Trigger supporting <logChange>-columns in table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo
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

       If _
         qualTabName = "VL6CMET.GROUP" Or qualTabName = "VL6CMET.GROUPVALIDFORORGANIZATION" Or qualTabName = "VL6CMET.GROUP_NL_TEXT" Or InStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 Or _
         qualTabName = "VL6CMET.AGGREGATIONNODE" Or qualTabName = "VL6CMET.AGGREGATIONNODE_NL_TEXT" Or InStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 Or _
         qualTabName = "VL6CMET.ENDNODE" Or qualTabName = "VL6CMET.ENDNODE_NL_TEXT" Or InStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0 _
       Then
         broadcastChanges = True
         broadcastForDist = False
         broadcastForNl = False
         broadcastForRel = False
         If InStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 Or InStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 Or InStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0 Then
           broadcastForDist = True
         ElseIf qualTabName = "VL6CMET.GROUPVALIDFORORGANIZATION" Then
           broadcastForRel = True
         Else
           broadcastForNl = True
         End If
       Else
         broadcastChanges = False
         broadcastForDist = False
       End If

       If broadcastChanges Then
         Print #fileNo,
         Print #fileNo, addTab(1); "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;"
         Print #fileNo, addTab(1); "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;"
       End If
 
       Print #fileNo,
       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); qualTabName
       Print #fileNo, addTab(1); "("

       genAttrListForEntity acmEntityIndex, acmEntityType, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, False, forGen, edomListNonLrt

       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "VALUES"
       Print #fileNo, addTab(1); "("

       initAttributeTransformation transformation, 0, , , , gc_newRecordName & "."

       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt

       Print #fileNo, addTab(1); ");"
     End If

     If broadcastChanges Then
       If broadcastForDist Then
         Print #fileNo, addTab(1); "-- CHANGELOGBROADCAST for Distributed Entity Insert"
          genInsertDistNlChangeLogBroadcastCall fileNo, acmEntityIndex, thisOrgIndex, ddlType
      ElseIf broadcastForRel Then
         Print #fileNo, addTab(1); "-- CHANGELOGBROADCAST for Rel Entity Insert"
         genInsertRelChangeLogBroadcastCall fileNo, acmEntityIndex, ddlType
      Else
         Print #fileNo, addTab(1); "-- CHANGELOGBROADCAST for Base Entity Insert"
         genInsertChangeLogBroadcastCall fileNo, acmEntityIndex, ddlType
       End If
     End If

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     If broadcastChanges And broadcastForNl Then
       qualNlTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , True, , "_INS")
       qualNlViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, , True, , "")
       'qualNlViewName = genQualNlTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, True)
       qualNlTabName = qualTabName & "_NL_TEXT"

       Print #fileNo,
       Print #fileNo, addTab(1); "-- TIGGER and CHANGELOGBROADCAST for NlText Entity Insert"
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE TRIGGER"
       Print #fileNo, addTab(1); qualNlTriggerName
       Print #fileNo, addTab(0); "INSTEAD OF INSERT ON"
       Print #fileNo, addTab(1); qualNlViewName
       Print #fileNo, addTab(0); "REFERENCING"
       Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
       Print #fileNo, addTab(0); "FOR EACH ROW"
       Print #fileNo, addTab(0); "BEGIN ATOMIC"
       Print #fileNo,
       Print #fileNo, addTab(1); "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;"
       Print #fileNo, addTab(1); "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;"
 
       Print #fileNo,
       Print #fileNo, addTab(1); "INSERT INTO"
       Print #fileNo, addTab(2); qualNlTabName
       Print #fileNo, addTab(1); "("

       genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomListNonLrt Or edomMqtLrt Or edomListVirtual Or edomListExpression

       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "VALUES"
       Print #fileNo, addTab(1); "("

       initAttributeTransformation transformation, 0, , , , gc_newRecordName & "."

       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt

       Print #fileNo, addTab(1); ");"
       Print #fileNo,
       genInsertNlChangeLogBroadcastCall fileNo, acmEntityIndex, ddlType
       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim

    End If

     ' ####################################################################################################################
     ' #    UPDATE Trigger
     ' ####################################################################################################################

     qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "LC_UPD")

     printSectionHeader "Update-Trigger supporting <logChange>-columns in table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo

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
       If _
         qualTabName = "VL6CMET.GROUP" Or qualTabName = "VL6CMET.GROUP_NL_TEXT" Or InStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 Or _
         qualTabName = "VL6CMET.AGGREGATIONNODE" Or qualTabName = "VL6CMET.AGGREGATIONNODE_NL_TEXT" Or InStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 Or _
         qualTabName = "VL6CMET.ENDNODE" Or qualTabName = "VL6CMET.ENDNODE_NL_TEXT" Or InStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0 _
       Then
         broadcastChanges = True
         broadcastForDist = False
         broadcastForNl = False
         broadcastForRel = False
         If InStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 Or InStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 Or InStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0 Then
           broadcastForDist = True
         ElseIf qualTabName = "VL6CMET.GROUPVALIDFORORGANIZATION" Then
           broadcastForRel = True
         Else
           broadcastForNl = True
         End If
       Else
         broadcastChanges = False
         broadcastForDist = False
       End If

       If broadcastChanges Then
         Print #fileNo,
         Print #fileNo, addTab(1); "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;"
         Print #fileNo, addTab(1); "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;"
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

       initAttributeTransformation transformation, 0, , , , gc_newRecordName & "."

       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , False, forGen, edomListNonLrt

       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); g_anOid; " = "; gc_oldRecordName; "."; g_anOid
       Print #fileNo, addTab(1); ";"

       If broadcastChanges Then
         Dim tabColumns As EntityColumnDescriptors
         Dim ignoreLastUpdateTimestamp As Boolean

         Dim filterByClassId As Boolean
         Dim ignoreForChangelog As Boolean
         Dim thisAttributeIndex As Integer
         Dim orSuperClassIndex As Integer
         Dim attrAppearsInClassIdStr As String
         Dim lastAttrAppearsInClassIdStr As String
         Dim closingEndIfOutStanding As Boolean
         Dim hasColumnToFilter As Boolean

         If broadcastForDist Then
           Print #fileNo, addTab(1); "-- CHANGELOGBROADCAST for Distributed Entity Update here"
           Print #fileNo,
           printComment "determine User id", fileNo, , 1
           Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
           Print #fileNo,
           hasColumnToFilter = False
           closingEndIfOutStanding = False

           ' generate change log records for changed regular attributes
           ignoreLastUpdateTimestamp = True
           initAttributeTransformation transformation, IIf(ignoreLastUpdateTimestamp, 4, 3)
           setAttributeMapping transformation, 1, conCreateUser, ""
           setAttributeMapping transformation, 2, conUpdateUser, ""
           setAttributeMapping transformation, 3, conCreateTimestamp, ""
           If ignoreLastUpdateTimestamp Then
             setAttributeMapping transformation, 4, conLastUpdateTimestamp, ""
           End If

           tabColumns = nullEntityColumnDescriptors
           genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, _
             fileNo, ddlType, thisOrgIndex, thisPoolIndex, 0, False, forGen, edomNone
           thisAttributeIndex = 1
           attrAppearsInClassIdStr = ""
           lastAttrAppearsInClassIdStr = ""

           While thisAttributeIndex <= tabColumns.numDescriptors
              'Print #fileNo, addTab(1); "-- Column:"; .columnName
              If isClAttrCat(tabColumns.descriptors(thisAttributeIndex).columnCategory, False) Then
                 filterByClassId = False
                 ignoreForChangelog = False

                 If tabColumns.descriptors(thisAttributeIndex).acmFkRelIndex > 0 Then
                   If g_relationships.descriptors(tabColumns.descriptors(thisAttributeIndex).acmFkRelIndex).ignoreForChangelog Then
                     ignoreForChangelog = True
                   End If
                 End If

                 If Not ignoreForChangelog Then
                   genUpdateChangeLogBroadcastCall fileNo, acmEntityIndex, tabColumns.descriptors(thisAttributeIndex), thisOrgIndex, True, True, ddlType
                End If
               End If

             thisAttributeIndex = thisAttributeIndex + 1
           Wend
         Else
           Print #fileNo, addTab(1); "-- CHANGELOGBROADCAST for Base Entity Update"
           Print #fileNo,
           printComment "determine User id", fileNo, , 1
           Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
           Print #fileNo,
 
           hasColumnToFilter = False
           closingEndIfOutStanding = False

           ' generate change log records for changed regular attributes
           ignoreLastUpdateTimestamp = True
           initAttributeTransformation transformation, IIf(ignoreLastUpdateTimestamp, 4, 3)
           setAttributeMapping transformation, 1, conCreateUser, ""
           setAttributeMapping transformation, 2, conUpdateUser, ""
           setAttributeMapping transformation, 3, conCreateTimestamp, ""
           If ignoreLastUpdateTimestamp Then
             setAttributeMapping transformation, 4, conLastUpdateTimestamp, ""
           End If

           tabColumns = nullEntityColumnDescriptors
           genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, _
             fileNo, ddlType, thisOrgIndex, thisPoolIndex, 0, False, forGen, edomNone
           thisAttributeIndex = 1
           attrAppearsInClassIdStr = ""
           lastAttrAppearsInClassIdStr = ""

           While thisAttributeIndex <= tabColumns.numDescriptors
              'Print #fileNo, addTab(1); "-- Column:"; .columnName
              If isClAttrCat(tabColumns.descriptors(thisAttributeIndex).columnCategory, False) Then
                 filterByClassId = False
                 ignoreForChangelog = False

                 If tabColumns.descriptors(thisAttributeIndex).acmFkRelIndex > 0 Then
                   If g_relationships.descriptors(tabColumns.descriptors(thisAttributeIndex).acmFkRelIndex).ignoreForChangelog Then
                     ignoreForChangelog = True
                   End If
                 End If

                 If Not ignoreForChangelog Then
                   genUpdateChangeLogBroadcastCall fileNo, acmEntityIndex, tabColumns.descriptors(thisAttributeIndex), thisOrgIndex, False, False, ddlType
                End If
               End If

             thisAttributeIndex = thisAttributeIndex + 1
           Wend
         End If

       End If
     End If

     Print #fileNo, "END"
     Print #fileNo, gc_sqlCmdDelim

     If broadcastChanges And broadcastForNl Then
       qualNlTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , True, , "_UPD")
       qualNlViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, , True, , "")
       qualNlTabName = qualTabName & "_NL_TEXT"

       Print #fileNo,
       Print #fileNo, addTab(1); "-- TIGGER and CHANGELOGBROADCAST for NlText Entity Update"
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE TRIGGER"
       Print #fileNo, addTab(1); qualNlTriggerName
       Print #fileNo, addTab(0); "INSTEAD OF UPDATE ON"
       Print #fileNo, addTab(1); qualNlViewName
       Print #fileNo, addTab(0); "REFERENCING"
       Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
       Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
       Print #fileNo, addTab(0); "FOR EACH ROW"
       Print #fileNo, addTab(0); "BEGIN ATOMIC"
       Print #fileNo,
       Print #fileNo, addTab(1); "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;"
       Print #fileNo, addTab(1); "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;"
 
       Print #fileNo,
       Print #fileNo, addTab(1); "UPDATE"
       Print #fileNo, addTab(2); qualNlTabName
       Print #fileNo, addTab(1); "SET"
       Print #fileNo, addTab(1); "("

       genNlsAttrDeclsForEntity acmEntityIndex, acmEntityType, fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 2, forGen, False, edomListNonLrt Or edomMqtLrt Or edomListVirtual Or edomListExpression

       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "="
       Print #fileNo, addTab(1); "("

       initAttributeTransformation transformation, 0, , , , gc_newRecordName & "."

       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , False, ddlType, thisOrgIndex, thisPoolIndex, 2, , , , edomListNonLrt

       Print #fileNo, addTab(1); ")"
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "OID = OLDRECORD.OID"
       Print #fileNo, addTab(1); ";"
 
       printComment "determine User id", fileNo, , 1
       Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
       Print #fileNo,
       hasColumnToFilter = False
       closingEndIfOutStanding = False
       ' generate change log records for changed regular attributes
       ignoreLastUpdateTimestamp = True
       initAttributeTransformation transformation, IIf(ignoreLastUpdateTimestamp, 4, 3)
       setAttributeMapping transformation, 1, conCreateUser, ""
       setAttributeMapping transformation, 2, conUpdateUser, ""
       setAttributeMapping transformation, 3, conCreateTimestamp, ""
       If ignoreLastUpdateTimestamp Then
         setAttributeMapping transformation, 4, conLastUpdateTimestamp, ""
       End If

       tabColumns = nullEntityColumnDescriptors
       genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, _
         fileNo, , , ddlType, thisOrgIndex, thisPoolIndex, 0, False, False, , edomNone
       thisAttributeIndex = 1
       attrAppearsInClassIdStr = ""
       lastAttrAppearsInClassIdStr = ""

       While thisAttributeIndex <= tabColumns.numDescriptors
          If isClAttrCat(tabColumns.descriptors(thisAttributeIndex).columnCategory, False) Then
             filterByClassId = False
             ignoreForChangelog = False

             If tabColumns.descriptors(thisAttributeIndex).acmFkRelIndex > 0 Then
               If g_relationships.descriptors(tabColumns.descriptors(thisAttributeIndex).acmFkRelIndex).ignoreForChangelog Then
                 ignoreForChangelog = True
               End If
             End If

             If Not ignoreForChangelog Then
               genUpdateChangeLogBroadcastCall fileNo, acmEntityIndex, tabColumns.descriptors(thisAttributeIndex), thisOrgIndex, True, False, ddlType
              End If
           End If

         thisAttributeIndex = thisAttributeIndex + 1
       Wend
       Print #fileNo,
       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim
       Print #fileNo,
     End If

     ' ####################################################################################################################
     ' #    DELETE Trigger
     ' ####################################################################################################################

     qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "LC_DEL")

     printSectionHeader "Delete-Trigger supporting <logChange>-columns in table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo

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
         'When approach becomes a generic concept, replace hardcoded name checks by flags
       If _
         qualTabName = "VL6CMET.GROUP" Or qualTabName = "VL6CMET.GROUPVALIDFORORGANIZATION" Or qualTabName = "VL6CMET.GROUP_NL_TEXT" Or InStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 Or _
         qualTabName = "VL6CMET.AGGREGATIONNODE" Or qualTabName = "VL6CMET.AGGREGATIONNODE_NL_TEXT" Or InStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 Or _
         qualTabName = "VL6CMET.ENDNODE" Or qualTabName = "VL6CMET.ENDNODE_NL_TEXT" Or InStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0 _
       Then
         broadcastChanges = True
         broadcastForDist = False
         broadcastForNl = False
         broadcastForRel = False
         If InStr(1, qualTabName, "GROUP_DIST_NL_TEXT") > 0 Or InStr(1, qualTabName, "AGGREGATIONNODE_DIST_NL_TEXT") > 0 Or InStr(1, qualTabName, "ENDNODE_DIST_NL_TEXT") > 0 Then
           broadcastForDist = True
         ElseIf qualTabName = "VL6CMET.GROUPVALIDFORORGANIZATION" Then
           broadcastForRel = True
         Else
           broadcastForNl = True
         End If
       Else
         broadcastChanges = False
         broadcastForDist = False
       End If

       If broadcastChanges Then
         Print #fileNo,
         Print #fileNo, addTab(1); "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;"
         Print #fileNo, addTab(1); "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;"
         If qualTabName = "VL6CMET.GROUP" Or qualTabName = "VL6CMET.ENDNODE" Then
           Print #fileNo, addTab(1); "CALL VL6CMET.DEL"; UCase(entityName); "DISTNLTEXT(OLDRECORD.OID);"
         ElseIf qualTabName = "VL6CMET.AGGREGATIONNODE" Then
           Print #fileNo, addTab(1); "CALL VL6CMET.DELAGGNODEDISTNLTEXT(OLDRECORD.OID);"
         End If
       End If

       Print #fileNo,
       Print #fileNo, addTab(1); "DELETE FROM"
       Print #fileNo, addTab(2); qualTabName
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); g_anOid; " = "; gc_oldRecordName; "."; g_anOid
       Print #fileNo, addTab(1); ";"

       If broadcastChanges Then
         If broadcastForDist Then
           Print #fileNo, addTab(1); "-- CHANGELOGBROADCAST for Distributed Entity Delete"
           genDeleteDistNlChangeLogBroadcastCall fileNo, acmEntityIndex, thisOrgIndex, ddlType
         ElseIf broadcastForRel Then
           Print #fileNo, addTab(1); "-- CHANGELOGBROADCAST for Rel Entity Delete"
           genDeleteRelChangeLogBroadcastCall fileNo, acmEntityIndex, ddlType
         Else
           Print #fileNo, addTab(1); "-- CHANGELOGBROADCAST for Base Entity Delete"
           genDeleteChangeLogBroadcastCall fileNo, acmEntityIndex, ddlType
         End If
       End If

     End If

     Print #fileNo, "END"
     Print #fileNo, gc_sqlCmdDelim

     If broadcastChanges And broadcastForNl Then
       qualNlTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , True, , "_DEL")
       qualNlViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, , True, , "")
       'qualNlViewName = genQualNlTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, False, False, True)
       qualNlTabName = qualTabName & "_NL_TEXT"

       Print #fileNo,
       Print #fileNo, addTab(1); "-- TIGGER and CHANGELOGBROADCAST for NlText Entity Delete"
       Print #fileNo,
       Print #fileNo, addTab(0); "CREATE TRIGGER"
       Print #fileNo, addTab(1); qualNlTriggerName
       Print #fileNo, addTab(0); "INSTEAD OF DELETE ON"
       Print #fileNo, addTab(1); qualNlViewName
       Print #fileNo, addTab(0); "REFERENCING"
       Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
       Print #fileNo, addTab(0); "FOR EACH ROW"
       Print #fileNo, addTab(0); "BEGIN ATOMIC"
       Print #fileNo,
       Print #fileNo, addTab(1); "DECLARE v_cdUserId                VARCHAR(16)     DEFAULT NULL;"
       Print #fileNo, addTab(1); "DECLARE v_numClRecords            INTEGER         DEFAULT NULL;"
 
       Print #fileNo,
       Print #fileNo, addTab(1); "DELETE FROM"
       Print #fileNo, addTab(2); qualNlTabName
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); "OID = "; gc_oldRecordName; ".OID"
       Print #fileNo, addTab(1); ";"

       Print #fileNo,
       genDeleteNlChangeLogBroadcastCall fileNo, acmEntityIndex, ddlType
       Print #fileNo, addTab(0); "END"
       Print #fileNo, addTab(0); gc_sqlCmdDelim

    End If

   End If
 End Sub
 
 
 Sub genLogChangeSupportDdlForClass( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   genLogChangeSupportDdlForEntity classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen
 End Sub
 
 
 Sub genLogChangeSupportDdlForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
   genLogChangeSupportDdlForEntity thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen
 End Sub
 
 
 Private Sub genLogChangeAutoMaintSupportDdlForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False _
 )
   Dim entityName As String
   Dim entityTypeDescr As String
   Dim entityShortName As String
   Dim sectionName As String
   Dim logLastChange As Boolean
   Dim isCtp As Boolean
   Dim isUserTransactional As Boolean
   Dim qualTabName As String

   If acmEntityType = eactClass Then
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       entityName = g_classes.descriptors(acmEntityIndex).className
       entityShortName = g_classes.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Class"
       logLastChange = g_classes.descriptors(acmEntityIndex).logLastChange
       isCtp = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional

       qualTabName = genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       entityName = g_relationships.descriptors(acmEntityIndex).relName
       entityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       entityTypeDescr = "ACM-Relationship"
       logLastChange = g_relationships.descriptors(acmEntityIndex).logLastChange
       isCtp = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional

       qualTabName = genQualTabNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt)
   End If

   If thisPoolIndex > 0 And isCtp Then
     Exit Sub
   End If

   If Not logLastChange Then
     Exit Sub
   End If

   If forLrt And Not isUserTransactional Then
     Exit Sub
   End If

   Dim qualTriggerName As String

   ' ####################################################################################################################
   ' #    INSERT Trigger
   ' ####################################################################################################################

 ' ### IF IVK ###
   qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "_LCINS")
 ' ### ELSE IVK ###
 ' qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "LC_INS")
 ' ### ENDIF IVK ###

   printSectionHeader "Insert-Trigger for maintaining log-change-columns in table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE TRIGGER"
   Print #fileNo, addTab(1); qualTriggerName
   Print #fileNo, addTab(0); "NO CASCADE BEFORE INSERT ON"
   Print #fileNo, addTab(1); qualTabName
   Print #fileNo, addTab(0); "REFERENCING"
   Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
   Print #fileNo, addTab(0); "FOR EACH ROW"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "SET "; gc_newRecordName; "."; g_anCreateUser; " = COALESCE("; gc_newRecordName; "."; g_anCreateUser; ", RTRIM(LEFT(CURRENT USER, 16))),"
   Print #fileNo, addTab(3); gc_newRecordName; "."; g_anCreateTimestamp; " = COALESCE("; gc_newRecordName; "."; g_anCreateTimestamp; ", CURRENT TIMESTAMP),"
   Print #fileNo, addTab(3); gc_newRecordName; "."; g_anUpdateUser; " = COALESCE("; gc_newRecordName; "."; g_anUpdateUser; ", RTRIM(LEFT(CURRENT USER, 16))),"
   Print #fileNo, addTab(3); gc_newRecordName; "."; g_anLastUpdateTimestamp; " = COALESCE("; gc_newRecordName; "."; g_anLastUpdateTimestamp; ", CURRENT TIMESTAMP)"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    UPDATE Trigger
   ' ####################################################################################################################

 ' ### IF IVK ###
   qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "_LCUPD")
 ' ### ELSE IVK ###
 ' qualTriggerName = genQualTriggerNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, , , , , "LC_UPD")
 ' ### ENDIF IVK ###

   printSectionHeader "Update-Trigger for maintaining log-change-columns in table """ & qualTabName & """ (" & entityTypeDescr & " """ & sectionName & "." & entityName & """)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE TRIGGER"
   Print #fileNo, addTab(1); qualTriggerName
   Print #fileNo, addTab(0); "NO CASCADE BEFORE UPDATE ON"
   Print #fileNo, addTab(1); qualTabName
   Print #fileNo, addTab(0); "REFERENCING"
   Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
   Print #fileNo, addTab(0); "FOR EACH ROW"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
   Print #fileNo, addTab(1); "SET "; gc_newRecordName; "."; g_anCreateUser; " = COALESCE("; gc_newRecordName; "."; g_anCreateUser; ", RTRIM(LEFT(CURRENT USER, 16))),"
   Print #fileNo, addTab(3); gc_newRecordName; "."; g_anCreateTimestamp; " = COALESCE("; gc_newRecordName; "."; g_anCreateTimestamp; ", CURRENT TIMESTAMP),"
   Print #fileNo, addTab(3); gc_newRecordName; "."; g_anUpdateUser; " = COALESCE("; gc_newRecordName; "."; g_anUpdateUser; ", RTRIM(LEFT(CURRENT USER, 16))),"
   Print #fileNo, addTab(3); gc_newRecordName; "."; g_anLastUpdateTimestamp; " = COALESCE("; gc_newRecordName; "."; g_anLastUpdateTimestamp; ", CURRENT TIMESTAMP)"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, gc_sqlCmdDelim
 End Sub

 
 Sub genLogChangeAutoMaintSupportDdlForClass( _
   ByRef classIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False _
 )
   genLogChangeAutoMaintSupportDdlForEntity classIndex, eactClass, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt
 End Sub
 
 
 Sub genLogChangeAutoMaintSupportDdlForRelationship( _
   thisRelIndex As Integer, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False _
 )
   genLogChangeAutoMaintSupportDdlForEntity thisRelIndex, eactRelationship, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt
 End Sub
 
 
 
