Attribute VB_Name = "M07_SpLogging"
Option Explicit

Private Const processingStepSpLog = 5

Private Const implementSpLogByWrapper = True

Private Const maxSpLogArgLength = 40

Private Const logEventTypeEntry = "B"
Private Const logEventTypeEscape = "X"
Private Const logEventTypeExit = "E"
Private Const logEventTypeLog = "L"

Private Const logEventContextTypeProcedure = "P"
Private Const logEventContextTypeFunction = "F"
Private Const logEventContextTypeTrigger = "T"

Private Const switchPosLogByConfig = 2
Private Const switchPosLogProcedure = 3
Private Const switchPosLogFunction = 4
Private Const switchPosLogTrigger = 5


Sub genSpDdl()
  If Not generatePdm Or Not supportSpLogging Then
    Exit Sub
  End If
  
  If spLogMode <> esplFile Then
    Exit Sub
  End If

  Dim ddlType As DdlTypeId
  ddlType = edtPdm
  
  Dim spLogHandleLength As Integer
  spLogHandleLength = IIf(spLogMode = esplFile, 160, 13)

  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexSpLog, processingStepSpLog, ddlType, , , "Deploy", phaseCoreSupport)

  On Error GoTo ErrorExit
  
  Dim qualProcName As String
  Dim unqualProcName As String
  Dim externalProcName As String
  
  If spLogMode = esplFile Then
    ' ####################################################################################################################
    ' #    UPDATE_SP_CONFIG
    ' ####################################################################################################################
    unqualProcName = "UPDATE_SP_CONFIG"
    externalProcName = "UpdateSPConfig"

    printSectionHeader "Stored Procedure " & unqualProcName, fileNo

    qualProcName = genQualProcName(g_sectionIndexSpLog, unqualProcName, ddlType)
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcName

    Print #fileNo, addTab(0); "("
    
    genProcParm fileNo, "IN", "SP_NAME", "VARCHAR(128)", True, "name of the stored procedure"
    genProcParm fileNo, "IN", "MODE", "CHAR(1)", False, "update mode ('Y' or 'N')"
  
    Print #fileNo, addTab(0); ")"
    Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 0"
    Print #fileNo, addTab(0); "DETERMINISTIC"
    
    If generateSpLogMessages Then
      Print #fileNo, addTab(0); "LANGUAGE C"
      Print #fileNo, addTab(0); "PARAMETER STYLE SQL"
      Print #fileNo, addTab(0); "DBINFO"
      Print #fileNo, addTab(0); "FENCED"
      Print #fileNo, addTab(0); "NOT THREADSAFE"
      Print #fileNo, addTab(0); "NO SQL"
      Print #fileNo, addTab(0); "PROGRAM TYPE SUB"
      Print #fileNo, addTab(0); "EXTERNAL NAME '<spPathPrefix>splogger!"; externalProcName; "'"
      If spLogAutonomousTransaction Then
        Print #fileNo, addTab(0); "AUTONOMOUS"
      End If
    Else
      Print #fileNo, addTab(0); "LANGUAGE SQL"
      Print #fileNo, addTab(0); "BEGIN"
      Print #fileNo, addTab(0); "END"
    End If
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  
    ' ####################################################################################################################
    ' #    GET_SP_CONFIG
    ' ####################################################################################################################
    unqualProcName = "GET_SP_CONFIG"
    externalProcName = "GetSPConfig"

    printSectionHeader "Stored Procedure " & unqualProcName, fileNo

    qualProcName = genQualProcName(g_sectionIndexSpLog, unqualProcName, ddlType)
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcName

    Print #fileNo, addTab(0); "("
    Print #fileNo, addTab(0); ")"
    
    Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 1"
    Print #fileNo, addTab(0); "DETERMINISTIC"
    
    If generateSpLogMessages Then
      Print #fileNo, addTab(0); "LANGUAGE C"
      Print #fileNo, addTab(0); "PARAMETER STYLE SQL"
      Print #fileNo, addTab(0); "DBINFO"
      Print #fileNo, addTab(0); "FENCED"
      Print #fileNo, addTab(0); "NOT THREADSAFE"
      Print #fileNo, addTab(0); "MODIFIES SQL DATA"
      Print #fileNo, addTab(0); "PROGRAM TYPE SUB"
      Print #fileNo, addTab(0); "EXTERNAL NAME '<spPathPrefix>splogger!"; externalProcName; "'"
      If spLogAutonomousTransaction Then
        Print #fileNo, addTab(0); "AUTONOMOUS"
      End If
    Else
      Print #fileNo, addTab(0); "LANGUAGE SQL"
      Print #fileNo, addTab(0); "BEGIN"
      Print #fileNo, addTab(0); "END"
    End If
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  
    ' ####################################################################################################################
    ' #    GET_SP_LOG
    ' ####################################################################################################################
    unqualProcName = "GET_SP_LOG"
    externalProcName = "GetSPLog"

    printSectionHeader "Stored Procedure " & unqualProcName, fileNo

    qualProcName = genQualProcName(g_sectionIndexSpLog, unqualProcName, ddlType)
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcName

    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "IN", "LOG_FILE_NAME", "VARCHAR(30)", False
    Print #fileNo, addTab(0); ")"
    
    Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 1"
    Print #fileNo, addTab(0); "DETERMINISTIC"
    
    If generateSpLogMessages Then
      Print #fileNo, addTab(0); "LANGUAGE C"
      Print #fileNo, addTab(0); "PARAMETER STYLE SQL"
      Print #fileNo, addTab(0); "DBINFO"
      Print #fileNo, addTab(0); "FENCED"
      Print #fileNo, addTab(0); "NOT THREADSAFE"
      Print #fileNo, addTab(0); "MODIFIES SQL DATA"
      Print #fileNo, addTab(0); "PROGRAM TYPE SUB"
      Print #fileNo, addTab(0); "EXTERNAL NAME '<spPathPrefix>splogger!"; externalProcName; "'"
      If spLogAutonomousTransaction Then
        Print #fileNo, addTab(0); "AUTONOMOUS"
      End If
    Else
      Print #fileNo, addTab(0); "LANGUAGE SQL"
      Print #fileNo, addTab(0); "BEGIN"
      Print #fileNo, addTab(0); "END"
    End If
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  
    ' ####################################################################################################################
    ' #    OPEN_LOG
    ' ####################################################################################################################
    unqualProcName = "OPEN_LOG"
    externalProcName = "OpenLog"

    printSectionHeader "Stored Procedure " & unqualProcName, fileNo

    qualProcName = genQualProcName(g_sectionIndexSpLog, unqualProcName, ddlType)
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcName

    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "IN", "SP_NAME", "VARCHAR(128)", True
    genProcParm fileNo, "OUT", "HANDLE", "CHAR(160) FOR BIT DATA", False
    Print #fileNo, addTab(0); ")"
    
    Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 0"
    Print #fileNo, addTab(0); "DETERMINISTIC"
    
    If generateSpLogMessages Then
      Print #fileNo, addTab(0); "LANGUAGE C"
      Print #fileNo, addTab(0); "PARAMETER STYLE SQL"
      Print #fileNo, addTab(0); "DBINFO"
      Print #fileNo, addTab(0); "FENCED"
      Print #fileNo, addTab(0); "NOT THREADSAFE"
      Print #fileNo, addTab(0); "NO SQL"
      Print #fileNo, addTab(0); "PROGRAM TYPE SUB"
      Print #fileNo, addTab(0); "EXTERNAL NAME '<spPathPrefix>splogger!"; externalProcName; "'"
      If spLogAutonomousTransaction Then
        Print #fileNo, addTab(0); "AUTONOMOUS"
      End If
    Else
      Print #fileNo, addTab(0); "LANGUAGE SQL"
      Print #fileNo, addTab(0); "BEGIN"
      Print #fileNo, addTab(0); "END"
    End If
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  
    ' ####################################################################################################################
    ' #    CLOSE_LOG
    ' ####################################################################################################################
    unqualProcName = "CLOSE_LOG"
    externalProcName = "CloseLog"

    printSectionHeader "Stored Procedure " & unqualProcName, fileNo

    qualProcName = genQualProcName(g_sectionIndexSpLog, unqualProcName, ddlType)
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcName

    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "IN", "HANDLE", "CHAR(160) FOR BIT DATA", False
    Print #fileNo, addTab(0); ")"
    
    Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 0"
    Print #fileNo, addTab(0); "DETERMINISTIC"
    
    If generateSpLogMessages Then
      Print #fileNo, addTab(0); "LANGUAGE C"
      Print #fileNo, addTab(0); "PARAMETER STYLE SQL"
      Print #fileNo, addTab(0); "DBINFO"
      Print #fileNo, addTab(0); "FENCED"
      Print #fileNo, addTab(0); "NOT THREADSAFE"
      Print #fileNo, addTab(0); "NO SQL"
      Print #fileNo, addTab(0); "PROGRAM TYPE SUB"
      Print #fileNo, addTab(0); "EXTERNAL NAME '<spPathPrefix>splogger!"; externalProcName; "'"
      If spLogAutonomousTransaction Then
        Print #fileNo, addTab(0); "AUTONOMOUS"
      End If
    Else
      Print #fileNo, addTab(0); "LANGUAGE SQL"
      Print #fileNo, addTab(0); "BEGIN"
      Print #fileNo, addTab(0); "END"
    End If
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  
    ' ####################################################################################################################
    ' #    LOGGER
    ' ####################################################################################################################
    unqualProcName = "LOGGER"
    externalProcName = "Logger"

    printSectionHeader "Stored Procedure " & unqualProcName, fileNo

    qualProcName = genQualProcName(g_sectionIndexSpLog, unqualProcName, ddlType)
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcName

    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "IN", "HANDLE", "CHAR(160) FOR BIT DATA", True
    genProcParm fileNo, "IN", "MSG", "VARCHAR(4000)", False
    Print #fileNo, addTab(0); ")"
    
    Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 0"
    Print #fileNo, addTab(0); "DETERMINISTIC"
    
    If generateSpLogMessages Then
      Print #fileNo, addTab(0); "LANGUAGE C"
      Print #fileNo, addTab(0); "PARAMETER STYLE SQL"
      Print #fileNo, addTab(0); "DBINFO"
      Print #fileNo, addTab(0); "FENCED"
      Print #fileNo, addTab(0); "NOT THREADSAFE"
      Print #fileNo, addTab(0); "NO SQL"
      Print #fileNo, addTab(0); "PROGRAM TYPE SUB"
      Print #fileNo, addTab(0); "EXTERNAL NAME '<spPathPrefix>splogger!"; externalProcName; "'"
      If spLogAutonomousTransaction Then
        Print #fileNo, addTab(0); "AUTONOMOUS"
      End If
    Else
      Print #fileNo, addTab(0); "LANGUAGE SQL"
      Print #fileNo, addTab(0); "BEGIN"
      Print #fileNo, addTab(0); "END"
    End If
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  
    ' ####################################################################################################################
    ' #    LOGINFO
    ' ####################################################################################################################
    unqualProcName = "LOGINFO"
    externalProcName = "Loginfo"

    printSectionHeader "Stored Procedure " & unqualProcName, fileNo

    qualProcName = genQualProcName(g_sectionIndexSpLog, unqualProcName, ddlType)
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcName

    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "IN", "HANDLE", "CHAR(160) FOR BIT DATA", True
    genProcParm fileNo, "IN", "MSG", "VARCHAR(4000)", False
    Print #fileNo, addTab(0); ")"
    
    Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 0"
    Print #fileNo, addTab(0); "DETERMINISTIC"
    
    If generateSpLogMessages Then
      Print #fileNo, addTab(0); "LANGUAGE C"
      Print #fileNo, addTab(0); "PARAMETER STYLE SQL"
      Print #fileNo, addTab(0); "DBINFO"
      Print #fileNo, addTab(0); "FENCED"
      Print #fileNo, addTab(0); "NOT THREADSAFE"
      Print #fileNo, addTab(0); "NO SQL"
      Print #fileNo, addTab(0); "PROGRAM TYPE SUB"
      Print #fileNo, addTab(0); "EXTERNAL NAME '<spPathPrefix>splogger!"; externalProcName; "'"
      If spLogAutonomousTransaction Then
        Print #fileNo, addTab(0); "AUTONOMOUS"
      End If
    Else
      Print #fileNo, addTab(0); "LANGUAGE SQL"
      Print #fileNo, addTab(0); "BEGIN"
      Print #fileNo, addTab(0); "END"
    End If
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  
    ' ####################################################################################################################
    ' #    SNOW
    ' ####################################################################################################################
    unqualProcName = "SNOW"
    externalProcName = "Snow"

    printSectionHeader "Stored Procedure " & unqualProcName, fileNo

    qualProcName = genQualProcName(g_sectionIndexSpLog, unqualProcName, ddlType)
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcName

    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "OUT", "msg", "VARCHAR(100)", False
    Print #fileNo, addTab(0); ")"
    
    Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 0"
    Print #fileNo, addTab(0); "DETERMINISTIC"
    
    If generateSpLogMessages Then
      Print #fileNo, addTab(0); "LANGUAGE C"
      Print #fileNo, addTab(0); "PARAMETER STYLE SQL"
      Print #fileNo, addTab(0); "DBINFO"
      Print #fileNo, addTab(0); "FENCED"
      Print #fileNo, addTab(0); "NOT THREADSAFE"
      Print #fileNo, addTab(0); "NO SQL"
      Print #fileNo, addTab(0); "PROGRAM TYPE SUB"
      Print #fileNo, addTab(0); "EXTERNAL NAME '<spPathPrefix>splogger!"; externalProcName; "'"
      If spLogAutonomousTransaction Then
        Print #fileNo, addTab(0); "AUTONOMOUS"
      End If
    Else
      Print #fileNo, addTab(0); "LANGUAGE SQL"
      Print #fileNo, addTab(0); "BEGIN"
      Print #fileNo, addTab(0); "END"
    End If
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  
    ' ####################################################################################################################
    ' #    TRUNCATE
    ' ####################################################################################################################
    unqualProcName = "TRUNCATE"
    externalProcName = "truncate_table"

    printSectionHeader "Stored Procedure " & unqualProcName, fileNo

    qualProcName = genQualProcName(g_sectionIndexSpLog, unqualProcName, ddlType)
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcName

    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "IN", "schemaName", "VARCHAR(130)", True
    genProcParm fileNo, "IN", "tableName", "VARCHAR(130)", False
    Print #fileNo, addTab(0); ")"
    
    Print #fileNo, addTab(0); "DYNAMIC RESULT SETS 0"
    Print #fileNo, addTab(0); "MODIFIES SQL DATA"
    Print #fileNo, addTab(0); "NOT DETERMINISTIC"
    Print #fileNo, addTab(0); "CALLED ON NULL INPUT"
    
    If generateSpLogMessages Then
      Print #fileNo, addTab(0); "LANGUAGE C"
      Print #fileNo, addTab(0); "PARAMETER STYLE SQL"
      Print #fileNo, addTab(0); "DBINFO"
      Print #fileNo, addTab(0); "FENCED"
      Print #fileNo, addTab(0); "NOT THREADSAFE"
      Print #fileNo, addTab(0); "INHERIT SPECIAL REGISTERS"
      Print #fileNo, addTab(0); "PROGRAM TYPE SUB"
      Print #fileNo, addTab(0); "EXTERNAL NAME '<spPathPrefix>splogger!"; externalProcName; "'"
      If spLogAutonomousTransaction Then
        Print #fileNo, addTab(0); "AUTONOMOUS"
      End If
    Else
      Print #fileNo, addTab(0); "LANGUAGE SQL"
      Print #fileNo, addTab(0); "BEGIN"
      Print #fileNo, addTab(0); "END"
    End If
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  End If

NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Private Sub genSpTabLogRecordDdl( _
    fileNo As Integer, _
    ddlType As DdlTypeId, _
    spLogHdlVar As String, _
    spEntryTsVar As String, _
    eventType As String, _
    contextSchemaVar As String, _
    contextNameVar As String, _
    contextType As String, _
    messageVar As String _
)
  If eventType = logEventTypeEntry Then
    genProcSectionHeader fileNo, "create new log handle"
    Print #fileNo, addTab(1); "SET "; spLogHdlVar; " = GENERATE_UNIQUE();"
    Print #fileNo, addTab(1); "SET "; spEntryTsVar; " = CURRENT TIMESTAMP;"
  End If
  
  genProcSectionHeader fileNo, "check whether log message is to be ignored"
  Print #fileNo, addTab(1); "IF (COALESCE("; gc_db2RegVarCtrl; ", '') = '') OR (LEFT(RIGHT('0000000' || "; gc_db2RegVarCtrl; ", "; CStr(switchPosLogByConfig); "), 1) = '1') THEN"
  genProcSectionHeader fileNo, "check by config table", 2, True
  Print #fileNo, addTab(2); "IF NOT EXISTS("
  Print #fileNo, addTab(3); "SELECT"
  Print #fileNo, addTab(4); "1"
  Print #fileNo, addTab(3); "FROM"
  Print #fileNo, addTab(4); g_qualTabNameSqlLogCfg; " C"
  Print #fileNo, addTab(3); "WHERE"
  Print #fileNo, addTab(4); "COALESCE(C."; g_anEventType; ", '"; eventType; "') = '"; eventType; "'"
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "COALESCE(C."; g_anSpLogContextSchema; ", "; contextSchemaVar; ") = "; contextSchemaVar
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "COALESCE(C."; g_anSpLogContextName; ", "; contextNameVar; ") = "; contextNameVar
  Print #fileNo, addTab(5); "AND"
  Print #fileNo, addTab(4); "COALESCE(C."; g_anSpLogContextType; ", '"; contextType; "') = '"; contextType; "'"
  Print #fileNo, addTab(2); ") THEN"
  
  Print #fileNo, addTab(3); "RETURN 0;"
  
  Print #fileNo, addTab(2); "END IF;"
    
  If contextType = logEventContextTypeProcedure Then
    Print #fileNo, addTab(1); "ELSEIF LEFT(RIGHT('0000000' || "; gc_db2RegVarCtrl; ", "; CStr(switchPosLogProcedure); "), 1) <> '1' THEN"
  ElseIf contextType = logEventContextTypeFunction Then
    Print #fileNo, addTab(1); "ELSEIF LEFT(RIGHT('0000000' || "; gc_db2RegVarCtrl; ", "; CStr(switchPosLogFunction); "), 1) <> '1' THEN"
  ElseIf contextType = logEventContextTypeTrigger Then
    Print #fileNo, addTab(1); "ELSEIF LEFT('0000000' || RIGHT("; gc_db2RegVarCtrl; ", "; CStr(switchPosLogTrigger); "), 1) <> '1' THEN"
  End If
  genProcSectionHeader fileNo, "check by special register", 2, True
  Print #fileNo, addTab(2); "RETURN 0;"
  Print #fileNo, addTab(1); "END IF;"
  
  genProcSectionHeader fileNo, "place record in Log-Table"
  Print #fileNo, addTab(1); "INSERT INTO"
  Print #fileNo, addTab(2); g_qualTabNameSqlLog
  Print #fileNo, addTab(1); "("
  
  genAttrListForEntity g_classIndexSqlLog, eactClass, fileNo, ddlType, , , 2, , edomListNonLrt
  
  Print #fileNo, addTab(1); ")"
  Print #fileNo, addTab(1); "VALUES"
  Print #fileNo, addTab(1); "("
  
  Dim transformation As AttributeListTransformation
  initAttributeTransformation transformation, 16
  
  setAttributeMapping transformation, 1, conId, spLogHdlVar
  setAttributeMapping transformation, 2, conSpLogEventTime, "CURRENT TIMESTAMP"
  If eventType = logEventTypeEntry Then
    setAttributeMapping transformation, 3, conSpLogEventTimeRelative, "0"
  Else
    setAttributeMapping transformation, 3, conSpLogEventTimeRelative, "TIMESTAMPDIFF(2, CHAR(CURRENT TIMESTAMP - " & spEntryTsVar & ")) + " & _
      "(DECIMAL(TIMESTAMPDIFF(1, CHAR('00000000000000.' || RIGHT(CHAR(CURRENT TIMESTAMP - " & spEntryTsVar & "),6)))) / 1000000)"
  End If
  setAttributeMapping transformation, 4, conEventType, "'" & eventType & "'"
  setAttributeMapping transformation, 5, conSpLogContextSchema, contextSchemaVar
  setAttributeMapping transformation, 6, conSpLogContextName, contextNameVar
  setAttributeMapping transformation, 7, conSpLogContextType, "'" & contextType & "'"
  setAttributeMapping transformation, 8, conMessage, "REPLACE(" & messageVar & ", CHR(10), ' ')"
  setAttributeMapping transformation, 9, conSchema, "CURRENT SCHEMA"
  setAttributeMapping transformation, 10, conPath, "CURRENT PATH"
  setAttributeMapping transformation, 11, conClientApplName, "CURRENT CLIENT_APPLNAME"
  setAttributeMapping transformation, 12, conClientWrkstnName, "CURRENT CLIENT_WRKSTNNAME"
  setAttributeMapping transformation, 13, conClientAcctng, "CURRENT CLIENT_ACCTNG"
  setAttributeMapping transformation, 14, conClientUserId, "CURRENT CLIENT_USERID"
  setAttributeMapping transformation, 15, conIsolation, "CURRENT ISOLATION"
  setAttributeMapping transformation, 16, conUser, "CURRENT USER"
  
  genTransformedAttrListForEntity g_classIndexSqlLog, eactClass, transformation, fileNo, ddlType, , , 2, , , edomListNonLrt
  
  Print #fileNo, addTab(1); ");"
End Sub


Sub genSpLogWrapperDdl( _
  ddlType As DdlTypeId _
)
  genSpDdl
  
  If ddlType <> edtPdm Or Not supportSpLogging Then
    Exit Sub
  End If
  
  Dim spLogHandleLength As Integer
  spLogHandleLength = IIf(spLogMode = esplFile, 160, 13)

  Dim fileNo As Integer
  fileNo = openDdlFile(g_targetDir, g_sectionIndexSpLog, processingStepSpLog, ddlType, , , , phaseCoreSupport)
    
  On Error GoTo ErrorExit
  
  Dim transformation As AttributeListTransformation
  Dim tabColumns As EntityColumnDescriptors
  
  ' ####################################################################################################################
  ' #    Wrapper-SP for placing a 'procedure entry log message'
  ' ####################################################################################################################

  Dim qualProcName As String
  qualProcName = genQualProcName(g_sectionIndexSpLog, "SPLOG_ENTER", ddlType)

  printSectionHeader "Wrapper-SP for placing a 'procedure entry log message'", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE PROCEDURE"
  Print #fileNo, addTab(1); qualProcName
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "INOUT", "spLogHdl_inout", "CHAR(" & spLogHandleLength & ") FOR BIT DATA", True, "log handle"
  genProcParm fileNo, "INOUT", "spEntryTimestamp_inout", "TIMESTAMP", True, "procedure's entry timestamp"
  genProcParm fileNo, "IN", "procSchema_in", g_dbtDbSchemaName, True, "procedure schema name"
  genProcParm fileNo, "IN", "procName_in", "VARCHAR(128)", True, "procedure name"
  genProcParm fileNo, "IN", "argList_in", "VARCHAR(800)", False, "list of argument values"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "RESULT SETS 0"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  If spLogAutonomousTransaction Then
    Print #fileNo, addTab(0); "AUTONOMOUS"
  End If
  Print #fileNo, addTab(0); "BEGIN"
  
  genProcSectionHeader fileNo, "declare conditions", , True
  
  If spLogMode = esplFile Then
    genCondDecl fileNo, "implNotFound", "42724", 1
    genCondDecl fileNo, "procTerminated", "38503", 1
  
    genProcSectionHeader fileNo, "declare continue handler"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;"
    
    genProcSectionHeader fileNo, "call SP-Logging Procedure"
    Print #fileNo, addTab(1); "CALL "; genQualProcName(g_sectionIndexSpLog, "OPEN_LOG", ddlType); "(procName_in, spLogHdl_inout);"
    Print #fileNo, addTab(1); "CALL "; genQualProcName(g_sectionIndexSpLog, "LOGINFO", ddlType); "(spLogHdl_inout, '--> entering Procedure ' || procName_in || '(' || argList_in || ')');"
  ElseIf spLogMode = esplTable Then
    genCondDecl fileNo, "tabDoesNotExist", "42704", 1
    
    genProcSectionHeader fileNo, "declare continue handler"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR tabDoesNotExist BEGIN END;"
    
    genSpTabLogRecordDdl fileNo, ddlType, "spLogHdl_inout", "spEntryTimestamp_inout", logEventTypeEntry, "procSchema_in", "procName_in", logEventContextTypeProcedure, "argList_in"
  End If
  
  Print #fileNo, addTab(0); "END"
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  ' ####################################################################################################################
  ' #    Wrapper-SP for placing a 'procedure exit log message'
  ' ####################################################################################################################

  qualProcName = genQualProcName(g_sectionIndexSpLog, "SPLOG_EXIT", ddlType)

  printSectionHeader "Wrapper-SP for placing a 'procedure exit log message'", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE PROCEDURE"
  Print #fileNo, addTab(1); qualProcName
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "IN", "spLogHdl_in", "CHAR(" & spLogHandleLength & ") FOR BIT DATA", True, "log handle"
  genProcParm fileNo, "IN", "spEntryTimestamp_in", "TIMESTAMP", True, "procedure's entry timestamp"
  genProcParm fileNo, "IN", "procSchema_in", g_dbtDbSchemaName, True, "procedure schema name"
  genProcParm fileNo, "IN", "procName_in", "VARCHAR(128)", True, "procedure name"
  genProcParm fileNo, "IN", "argList_in", "VARCHAR(800)", False, "list of argument values"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "RESULT SETS 0"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  If spLogAutonomousTransaction Then
    Print #fileNo, addTab(0); "AUTONOMOUS"
  End If
  Print #fileNo, addTab(0); "BEGIN"

  genProcSectionHeader fileNo, "declare conditions", , True
  
  If spLogMode = esplFile Then
    genCondDecl fileNo, "implNotFound", "42724", 1
    genCondDecl fileNo, "procTerminated", "38503", 1
  
    genProcSectionHeader fileNo, "declare continue handler"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;"
  
    genProcSectionHeader fileNo, "call SP-Logging Procedure"
    Print #fileNo, addTab(1); "CALL "; genQualProcName(g_sectionIndexSpLog, "LOGINFO", ddlType); _
                              "(spLogHdl_in, '<-- leaving Procedure ' || procName_in || '(' || argList_in || ')');"
    Print #fileNo, addTab(1); "CALL "; genQualProcName(g_sectionIndexSpLog, "CLOSE_LOG", ddlType); _
                              "(spLogHdl_in);"
  ElseIf spLogMode = esplTable Then
    genCondDecl fileNo, "tabDoesNotExist", "42704", 1
    
    genProcSectionHeader fileNo, "declare continue handler"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR tabDoesNotExist BEGIN END;"
    
    genSpTabLogRecordDdl fileNo, ddlType, "spLogHdl_in", "spEntryTimestamp_in", logEventTypeExit, "procSchema_in", "procName_in", logEventContextTypeProcedure, "argList_in"
  End If
  
  Print #fileNo, addTab(0); "END"
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  ' ####################################################################################################################
  ' #    Wrapper-SP for placing a 'procedure escape log message'
  ' ####################################################################################################################

  qualProcName = genQualProcName(g_sectionIndexSpLog, "SPLOG_ESC", ddlType)

  printSectionHeader "Wrapper-SP for placing a 'procedure escape log message'", fileNo

  Print #fileNo,
  Print #fileNo, addTab(0); "CREATE PROCEDURE"
  Print #fileNo, addTab(1); qualProcName
  Print #fileNo, addTab(0); "("
  genProcParm fileNo, "IN", "spLogHdl_in", "CHAR(" & spLogHandleLength & ") FOR BIT DATA", True, "log handle"
  genProcParm fileNo, "IN", "spEntryTimestamp_in", "TIMESTAMP", True, "procedure's entry timestamp"
  genProcParm fileNo, "IN", "procSchema_in", g_dbtDbSchemaName, True, "procedure schema name"
  genProcParm fileNo, "IN", "procName_in", "VARCHAR(128)", True, "procedure name"
  genProcParm fileNo, "IN", "argList_in", "VARCHAR(800)", False, "list of argument values"
  Print #fileNo, addTab(0); ")"
  Print #fileNo, addTab(0); "RESULT SETS 0"
  Print #fileNo, addTab(0); "LANGUAGE SQL"
  If spLogAutonomousTransaction Then
    Print #fileNo, addTab(0); "AUTONOMOUS"
  End If
  Print #fileNo, addTab(0); "BEGIN"

  genProcSectionHeader fileNo, "declare conditions", , True
  
  If spLogMode = esplFile Then
    genCondDecl fileNo, "implNotFound", "42724", 1
    genCondDecl fileNo, "procTerminated", "38503", 1
  
    genProcSectionHeader fileNo, "declare continue handler"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;"
  
    genProcSectionHeader fileNo, "call SP-Logging Procedure"
    Print #fileNo, addTab(1); "CALL "; genQualProcName(g_sectionIndexSpLog, "LOGINFO", ddlType); _
                              "(spLogHdl_in, '<-- escaping Procedure ' || procName_in || '(' || argList_in || ')');"
    Print #fileNo, addTab(1); "CALL "; genQualProcName(g_sectionIndexSpLog, "CLOSE_LOG", ddlType); _
                              "(spLogHdl_in);"
  ElseIf spLogMode = esplTable Then
    genCondDecl fileNo, "tabDoesNotExist", "42704", 1
    
    genProcSectionHeader fileNo, "declare continue handler"
    Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR tabDoesNotExist BEGIN END;"
    
    genSpTabLogRecordDdl fileNo, ddlType, "spLogHdl_in", "spEntryTimestamp_in", logEventTypeEscape, "procSchema_in", "procName_in", logEventContextTypeProcedure, "argList_in"
  End If
  
  Print #fileNo, addTab(0); "END"
  Print #fileNo, addTab(0); gc_sqlCmdDelim

  ' ####################################################################################################################
  ' #    SP for activating / deactivating Stored Procedure Logging
  ' ####################################################################################################################
      
  Dim qualProcedureNameActivate As String
  Dim deactivateMode As Boolean
  
  Dim mode As Integer
  For mode = 1 To 2
    deactivateMode = (mode = 2)
    If deactivateMode Then
      qualProcedureNameActivate = genQualProcName(g_sectionIndexSpLog, "DEACTIVATE", ddlType)
    
      printSectionHeader "SP for deactivating Stored Procedure Logging", fileNo
    Else
      qualProcedureNameActivate = genQualProcName(g_sectionIndexSpLog, "ACTIVATE", ddlType)
    
      printSectionHeader "SP for activating Stored Procedure Logging", fileNo
    End If

    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcedureNameActivate
    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
    genProcParm fileNo, "IN", "eventType_in", "CHAR(1)", True, "event types to activate"
    genProcParm fileNo, "IN", "contextType_in", "CHAR(1)", True, "distinguishes context types 'P' (procedure), 'F' (function) and 'T' (trigger)"
    genProcParm fileNo, "IN", "contextSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) schema-filter for contexts to activate"
    genProcParm fileNo, "IN", "contextNamePattern_in", "VARCHAR(80)", True, "(optional) filter for contexts to activate"
    genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of activation-statements executed"
    Print #fileNo, addTab(0); ")"
    Print #fileNo, addTab(0); "RESULT SETS 1"
    Print #fileNo, addTab(0); "LANGUAGE SQL"
    If spLogAutonomousTransaction Then
      Print #fileNo, addTab(0); "AUTONOMOUS"
    End If
    Print #fileNo, addTab(0); "BEGIN"
    
    genProcSectionHeader fileNo, "declare variables", , True
    genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
    If spLogMode = esplTable Then
      genVarDecl fileNo, "v_rowCount", "INTEGER", "NULL"
    End If
    genSpLogDecl fileNo
    
    genDdlForTempStatement fileNo, 1, True, 400, , , , , , , , True
    
    genProcSectionHeader fileNo, "initialize output parameter"
    Print #fileNo, addTab(1); "SET rowCount_out = 0;"
    
    genSpLogProcEnter fileNo, qualProcedureNameActivate, ddlType, , "mode_in", "'eventType_in", "'contextType_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out"
    
    If spLogMode = esplFile Then
      Print #fileNo,
      Print #fileNo, addTab(1); "FOR procLoop AS"
      
      Print #fileNo, addTab(2); "SELECT DISTINCT"
      Print #fileNo, addTab(3); "PROCSCHEMA,"
      Print #fileNo, addTab(3); "PROCNAME"
      Print #fileNo, addTab(2); "FROM"
      Print #fileNo, addTab(3); "SYSCAT.PROCEDURES"
      Print #fileNo, addTab(2); "WHERE"
      Print #fileNo, addTab(3); "("
      Print #fileNo, addTab(4); "contextSchemaPattern_in IS NULL"
      Print #fileNo, addTab(5); "OR"
      Print #fileNo, addTab(4); "RTRIM(PROCSCHEMA) LIKE contextSchemaPattern_in ESCAPE '\'"
      Print #fileNo, addTab(3); ")"
      Print #fileNo, addTab(4); "AND"
      Print #fileNo, addTab(3); "RTRIM(PROCSCHEMA) LIKE '"; g_allSchemaNamePattern; "' ESCAPE '\'"
      Print #fileNo, addTab(4); "AND"
      Print #fileNo, addTab(3); "("
      Print #fileNo, addTab(4); "contextNamePattern_in IS NULL"
      Print #fileNo, addTab(5); "OR"
      Print #fileNo, addTab(4); "RTRIM(PROCNAME) LIKE contextNamePattern_in ESCAPE '\'"
      Print #fileNo, addTab(3); ")"
      Print #fileNo, addTab(2); "FOR READ ONLY"
      
      Print #fileNo, addTab(1); "DO"
      Print #fileNo, addTab(2); "SET v_stmntTxt ="
      Print #fileNo, addTab(3); "'CALL "; genQualProcName(g_sectionIndexSpLog, "UPDATE_SP_CONFIG", ddlType); "(''' || PROCSCHEMA || '.' || PROCNAME || ''',''' || "; IIf(deactivateMode, "N", "Y"); " || ''')'"
      Print #fileNo, addTab(2); ";"
      Print #fileNo,
      
      Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + 1;"
      
      genProcSectionHeader fileNo, "store statement in temporary table", 2
      Print #fileNo, addTab(2); "INSERT INTO"
      Print #fileNo, addTab(3); tempTabNameStatement
      Print #fileNo, addTab(2); "("
      Print #fileNo, addTab(3); "SEQNO,"
      Print #fileNo, addTab(3); "STATEMENT"
      Print #fileNo, addTab(2); ")"
      Print #fileNo, addTab(2); "VALUES"
      Print #fileNo, addTab(2); "("
      Print #fileNo, addTab(3); "rowCount_out,"
      Print #fileNo, addTab(3); "v_stmntTxt"
      Print #fileNo, addTab(2); ");"
      
      Print #fileNo,
      Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
      Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
      
      Print #fileNo, addTab(2); "END IF;"
      
      Print #fileNo, addTab(1); "END FOR;"
      
    ElseIf spLogMode = esplTable Then
      Print #fileNo,
      Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
      Print #fileNo, addTab(2); "IF COALESCE(contextType_in, '"; logEventContextTypeProcedure; "') = '"; logEventContextTypeProcedure; "' THEN"
      
      genProcSectionHeader fileNo, "store statement in temporary table", 3, True
      Print #fileNo, addTab(3); "INSERT INTO"
      Print #fileNo, addTab(4); tempTabNameStatement
      Print #fileNo, addTab(3); "("
      Print #fileNo, addTab(4); "seqNo,"
      Print #fileNo, addTab(4); "statement,"
      Print #fileNo, addTab(4); "flag"
      Print #fileNo, addTab(3); ")"
      
      Print #fileNo, addTab(3); "WITH"
      Print #fileNo, addTab(4); "V"
      Print #fileNo, addTab(3); "("
      genAttrListForEntity g_classIndexSqlLogCfg, eactClass, fileNo, ddlType, , , 4, , edomListNonLrt
      Print #fileNo, addTab(3); ")"
      Print #fileNo, addTab(3); "AS"
      Print #fileNo, addTab(3); "("
      Print #fileNo, addTab(4); "SELECT DISTINCT"
      
      initAttributeTransformation transformation, 4
      
      setAttributeMapping transformation, 1, conEventType, "eventType_in"
      setAttributeMapping transformation, 2, conSpLogContextSchema, "(CASE WHEN contextSchemaPattern_in IS NULL THEN contextSchemaPattern_in ELSE P.PROCSCHEMA END)"
      setAttributeMapping transformation, 3, conSpLogContextName, "(CASE WHEN contextNamePattern_in IS NULL THEN contextNamePattern_in ELSE P.PROCNAME END)"
      setAttributeMapping transformation, 4, conSpLogContextType, "(CASE WHEN (contextNamePattern_in IS NULL OR P.PROCNAME IS NULL) THEN contextType_in ELSE '" & logEventContextTypeProcedure & "' END)"
    
      genTransformedAttrListForEntity g_classIndexSqlLogCfg, eactClass, transformation, fileNo, ddlType, , , 5, , , edomListNonLrt
      
      Print #fileNo, addTab(4); "FROM"
      Print #fileNo, addTab(5); "SYSCAT.PROCEDURES P"
      Print #fileNo, addTab(4); "WHERE"
      Print #fileNo, addTab(5); "P.PROCSCHEMA LIKE COALESCE(contextSchemaPattern_in, '"; g_allSchemaNamePattern; "') ESCAPE '\'"
      Print #fileNo, addTab(6); "AND"
      Print #fileNo, addTab(5); "P.PROCNAME LIKE COALESCE(contextNamePattern_in, '%') ESCAPE '\'"
      
      Print #fileNo, addTab(3); "),"
      
      Print #fileNo, addTab(4); "V_Flagged"
      Print #fileNo, addTab(3); "("
      Print #fileNo, addTab(4); "flag,"
      
      genAttrListForEntity g_classIndexSqlLogCfg, eactClass, fileNo, ddlType, , , 4, , edomListNonLrt
      
      Print #fileNo, addTab(3); ")"
      Print #fileNo, addTab(3); "AS"
      Print #fileNo, addTab(3); "("
      Print #fileNo, addTab(4); "SELECT"
      
      Print #fileNo, addTab(5); "("
      Print #fileNo, addTab(6); "CASE WHEN EXISTS ("
      Print #fileNo, addTab(7); "SELECT"
      Print #fileNo, addTab(8); "1"
      Print #fileNo, addTab(7); "FROM"
      Print #fileNo, addTab(8); g_qualTabNameSqlLogCfg; " C"
      Print #fileNo, addTab(7); "WHERE"
      If deactivateMode Then
        Print #fileNo, addTab(8); "( C."; g_anEventType; " = eventType_in OR eventType_in is NULL )"
        Print #fileNo, addTab(9); "AND"
        Print #fileNo, addTab(8); "( C."; g_anSpLogContextType; " = contextType_in OR contextType_in is NULL)"
        Print #fileNo, addTab(9); "AND"
        Print #fileNo, addTab(8); "( C."; g_anSpLogContextSchema; " like contextSchemaPattern_in||'%' OR contextSchemaPattern_in is NULL)"
        Print #fileNo, addTab(9); "AND"
        Print #fileNo, addTab(8); "( C."; g_anSpLogContextName; " like contextNamePattern_in||'%' OR contextNamePattern_in is NULL)"
        Print #fileNo, addTab(6); ") THEN '+' ELSE ' ' END"
      Else
        Print #fileNo, addTab(8); "COALESCE(C."; g_anSpLogContextSchema; ", '#') = COALESCE(V."; g_anSpLogContextSchema; ", '#')"
        Print #fileNo, addTab(9); "AND"
        Print #fileNo, addTab(8); "COALESCE(C."; g_anSpLogContextName; ", '#') = COALESCE(V."; g_anSpLogContextName; ", '#')"
        Print #fileNo, addTab(9); "AND"
        Print #fileNo, addTab(8); "COALESCE(C."; g_anSpLogContextType; ", '#') = COALESCE(V."; g_anSpLogContextType; ", '#')"
        Print #fileNo, addTab(9); "AND"
        Print #fileNo, addTab(8); "COALESCE(C."; g_anEventType; ", '#') = COALESCE(V."; g_anEventType; ", '#')"
        Print #fileNo, addTab(6); ") THEN ' ' ELSE '+' END"
      End If
      Print #fileNo, addTab(5); "),"
      genAttrListForEntity g_classIndexSqlLogCfg, eactClass, fileNo, ddlType, , , 5, , edomListNonLrt
      
      Print #fileNo, addTab(4); "FROM"
      Print #fileNo, addTab(5); "V"
      Print #fileNo, addTab(3); ")"
      
      Print #fileNo, addTab(3); "SELECT"
      Print #fileNo, addTab(4); "ROWNUMBER() OVER (ORDER BY "; g_anSpLogContextSchema; ", "; g_anSpLogContextName; "),"
    
      If deactivateMode Then
        Print #fileNo, addTab(4); "'DELETE FROM "; g_qualTabNameSqlLogCfg; " C ' ||"
        Print #fileNo, addTab(4); "' WHERE ' ||"
        Print #fileNo, addTab(5); " CASE WHEN "; g_anEventType; " IS NULL     THEN '1=1' ELSE 'C."; g_anEventType; "'||' = '||''''||"; g_anEventType; "||''''                  END || ' AND ' ||"
        Print #fileNo, addTab(5); " CASE WHEN "; g_anSpLogContextType; " IS NULL   THEN '1=1' ELSE 'C."; g_anSpLogContextType; "'||' = '||''''||"; g_anSpLogContextType; "||''''              END || ' AND ' ||"
        Print #fileNo, addTab(5); " CASE WHEN "; g_anSpLogContextSchema; " IS NULL THEN '1=1' ELSE 'C."; g_anSpLogContextSchema; "'||' LIKE '||''''||"; g_anSpLogContextSchema; "||'%'||''''  END || ' AND ' ||"
        Print #fileNo, addTab(5); " CASE WHEN "; g_anSpLogContextName; " IS NULL   THEN '1=1' ELSE 'C."; g_anSpLogContextName; "'||' LIKE '||''''||"; g_anSpLogContextName; "||'%'||''''      END,"
      Else
        Print #fileNo, addTab(4); "'INSERT INTO "; g_qualTabNameSqlLogCfg; " (' ||"
        tabColumns = nullEntityColumnDescriptors
        genTransformedAttrListForEntityWithColReuse g_classIndexSqlLogCfg, eactClass, transformation, tabColumns, fileNo, ddlType, , , 4, , , edomNone
        Dim i As Integer
        For i = 1 To tabColumns.numDescriptors
          With tabColumns.descriptors(i)
            Print #fileNo, addTab(5); "'"; .columnName; IIf(i < tabColumns.numDescriptors, ",", ""); "' ||"
          End With
        Next i
        Print #fileNo, addTab(4); "') VALUES (' ||"
        For i = 1 To tabColumns.numDescriptors
          With tabColumns.descriptors(i)
            If .columnName = conEventType Then
              Print #fileNo, addTab(5); "COALESCE('''' || "; g_anEventType; " || '''', 'NULL')"; IIf(i < tabColumns.numDescriptors, " || ','", ""); " ||"
            ElseIf .columnName = conSpLogContextSchema Then
              Print #fileNo, addTab(5); "COALESCE('''' || "; g_anSpLogContextSchema; " || '''', 'NULL')"; IIf(i < tabColumns.numDescriptors, " || ','", ""); " ||"
            ElseIf .columnName = conSpLogContextName Then
              Print #fileNo, addTab(5); "COALESCE('''' || "; g_anSpLogContextName; " || '''', 'NULL')"; IIf(i < tabColumns.numDescriptors, " || ','", ""); " ||"
            ElseIf .columnName = conSpLogContextType Then
              Print #fileNo, addTab(5); "(CASE WHEN "; g_anSpLogContextName; " IS NULL THEN 'NULL' ELSE '''"; logEventContextTypeProcedure; "''' END)"; IIf(i < tabColumns.numDescriptors, " || ','", ""); " ||"
            End If
          End With
        Next i
        Print #fileNo, addTab(4); "')',"
      End If
        
      Print #fileNo, addTab(4); "flag"
      Print #fileNo, addTab(3); "FROM"
      Print #fileNo, addTab(4); "V_flagged"
      Print #fileNo, addTab(3); "WITH UR;"
    
      Print #fileNo,
      Print #fileNo, addTab(3); "SET rowCount_out = (SELECT COUNT(*) FROM "; tempTabNameStatement; " WHERE flag = '+');"
    
      Print #fileNo, addTab(2); "END IF;"
      Print #fileNo, addTab(1); "END IF;"
      
      genProcSectionHeader fileNo, "update configuration"
      Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
      Print #fileNo, addTab(2); "IF COALESCE(contextType_in, '"; logEventContextTypeProcedure; "') = '"; logEventContextTypeProcedure; "' THEN"
      
      If deactivateMode Then
        Print #fileNo, addTab(3); "DELETE FROM"
        Print #fileNo, addTab(4); g_qualTabNameSqlLogCfg; " C"
        Print #fileNo, addTab(3); "WHERE"
        Print #fileNo, addTab(4); "( C."; g_anEventType; " = eventType_in OR eventType_in is NULL )"
        Print #fileNo, addTab(5); "AND"
        Print #fileNo, addTab(4); "( C."; g_anSpLogContextType; " = contextType_in OR contextType_in is NULL)"
        Print #fileNo, addTab(5); "AND"
        Print #fileNo, addTab(4); "( C."; g_anSpLogContextSchema; " like contextSchemaPattern_in||'%' OR contextSchemaPattern_in is NULL)"
        Print #fileNo, addTab(5); "AND"
        Print #fileNo, addTab(4); "( C."; g_anSpLogContextName; " like contextNamePattern_in||'%' OR contextNamePattern_in is NULL)"
        Print #fileNo, addTab(5); "AND"
        Print #fileNo, addTab(4); "( C."; g_anSpLogContextName; " like contextNamePattern_in||'%' OR contextNamePattern_in is NULL)"
        Print #fileNo, addTab(3); "WITH UR;"
      Else
        Print #fileNo, addTab(3); "INSERT INTO"
        Print #fileNo, addTab(4); g_qualTabNameSqlLogCfg
        Print #fileNo, addTab(3); "("
      
        genAttrListForEntity g_classIndexSqlLogCfg, eactClass, fileNo, ddlType, , , 4, , edomListNonLrt
      
        Print #fileNo, addTab(3); ")"
        Print #fileNo, addTab(3); "WITH"
        Print #fileNo, addTab(4); "V"
        Print #fileNo, addTab(3); "("
        genAttrListForEntity g_classIndexSqlLogCfg, eactClass, fileNo, ddlType, , , 4, , edomListNonLrt
        Print #fileNo, addTab(3); ")"
        Print #fileNo, addTab(3); "AS"
        Print #fileNo, addTab(3); "("
        Print #fileNo, addTab(4); "SELECT DISTINCT"
        
        initAttributeTransformation transformation, 4
        
        setAttributeMapping transformation, 1, conEventType, "eventType_in"
        setAttributeMapping transformation, 2, conSpLogContextSchema, "(CASE WHEN contextSchemaPattern_in IS NULL THEN contextSchemaPattern_in ELSE P.PROCSCHEMA END)"
        setAttributeMapping transformation, 3, conSpLogContextName, "(CASE WHEN contextNamePattern_in IS NULL THEN contextNamePattern_in ELSE P.PROCNAME END)"
        setAttributeMapping transformation, 4, conSpLogContextType, "(CASE WHEN (contextNamePattern_in IS NULL OR P.PROCNAME IS NULL) THEN contextType_in ELSE '" & logEventContextTypeProcedure & "' END)"
      
        genTransformedAttrListForEntity g_classIndexSqlLogCfg, eactClass, transformation, fileNo, ddlType, , , 5, , , edomListNonLrt
        
        Print #fileNo, addTab(4); "FROM"
        Print #fileNo, addTab(5); "SYSCAT.PROCEDURES P"
        Print #fileNo, addTab(4); "WHERE"
        Print #fileNo, addTab(5); "P.PROCSCHEMA LIKE COALESCE(contextSchemaPattern_in, '"; g_allSchemaNamePattern; "') ESCAPE '\'"
        Print #fileNo, addTab(6); "AND"
        Print #fileNo, addTab(5); "P.PROCNAME LIKE COALESCE(contextNamePattern_in, '%') ESCAPE '\'"
        
        Print #fileNo, addTab(3); ")"
        Print #fileNo, addTab(3); "SELECT"
        genAttrListForEntity g_classIndexSqlLogCfg, eactClass, fileNo, ddlType, , , 4, , edomListNonLrt
      
        Print #fileNo, addTab(3); "FROM"
        Print #fileNo, addTab(4); "V"
        Print #fileNo, addTab(3); "WHERE"
        
        Print #fileNo, addTab(4); "NOT EXISTS ("
        Print #fileNo, addTab(5); "SELECT"
        Print #fileNo, addTab(6); "1"
        Print #fileNo, addTab(5); "FROM"
        Print #fileNo, addTab(6); g_qualTabNameSqlLogCfg; " C"
        Print #fileNo, addTab(5); "WHERE"
        Print #fileNo, addTab(6); "COALESCE(C."; g_anSpLogContextSchema; ", '#') = COALESCE(V."; g_anSpLogContextSchema; ", '#')"
        Print #fileNo, addTab(7); "AND"
        Print #fileNo, addTab(6); "COALESCE(C."; g_anSpLogContextName; ", '#') = COALESCE(V."; g_anSpLogContextName; ", '#')"
        Print #fileNo, addTab(7); "AND"
        Print #fileNo, addTab(6); "COALESCE(C."; g_anSpLogContextType; ", '#') = COALESCE(V."; g_anSpLogContextType; ", '#')"
        Print #fileNo, addTab(7); "AND"
        Print #fileNo, addTab(6); "COALESCE(C."; g_anEventType; ", '#') = COALESCE(V."; g_anEventType; ", '#')"
        Print #fileNo, addTab(4); ")"
        Print #fileNo, addTab(3); "WITH UR;"
      End If
      
      Print #fileNo,
      Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
      Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
      
      Print #fileNo, addTab(2); "END IF;"
      Print #fileNo, addTab(1); "END IF;"
    End If
    
    genProcSectionHeader fileNo, "return result to application"
    Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
    Print #fileNo, addTab(2); "BEGIN"
    Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
    Print #fileNo, addTab(4); "SELECT"
    Print #fileNo, addTab(5); "flag AS F,"
    Print #fileNo, addTab(5); "statement"
    Print #fileNo, addTab(4); "FROM"
    Print #fileNo, addTab(5); tempTabNameStatement
    Print #fileNo, addTab(4); "ORDER BY"
    Print #fileNo, addTab(5); "seqno ASC"
    Print #fileNo, addTab(4); "FOR READ ONLY"
    Print #fileNo, addTab(3); ";"
    
    genProcSectionHeader fileNo, "leave cursor open for application", 3
    Print #fileNo, addTab(3); "OPEN stmntCursor;"
      
    Print #fileNo, addTab(2); "END;"
    Print #fileNo, addTab(1); "END IF;"
      
    genSpLogProcExit fileNo, qualProcedureNameActivate, ddlType, , "mode_in", "'eventType_in", "'contextType_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out"
    
    Print #fileNo, addTab(0); "END"
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  Next mode
        
  If spLogMode = esplFile Then
    ' ####################################################################################################################
    ' #    SP for activating Stored Procedure Logging
    ' ####################################################################################################################
    
    printSectionHeader "SP for activating Stored Procedure Logging", fileNo
    
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcedureNameActivate
    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
    genProcParm fileNo, "IN", "contextSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) schema-filter for contexts to activate"
    genProcParm fileNo, "IN", "contextNamePattern_in", "VARCHAR(80)", True, "(optional) filter for contexts to activate"
    genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of activation-statements executed"
    Print #fileNo, addTab(0); ")"
    Print #fileNo, addTab(0); "RESULT SETS 1"
    Print #fileNo, addTab(0); "LANGUAGE SQL"
    If spLogAutonomousTransaction Then
      Print #fileNo, addTab(0); "AUTONOMOUS"
    End If
    Print #fileNo, addTab(0); "BEGIN"
  
    If spLogMode = esplFile Then
      genSpLogDecl fileNo, -1, True
  
      genSpLogProcEnter fileNo, qualProcedureNameActivate, ddlType, , "mode_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out", , , , , , , , False
    
      Print #fileNo,
      Print #fileNo, addTab(1); "CALL "; qualProcedureNameActivate; "(mode_in, contextSchemaPattern_in, contextNamePattern_in, 'Y', rowCount_out);"
    
      genSpLogProcExit fileNo, qualProcedureNameActivate, ddlType, , "mode_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out", , , , , , , , False
    ElseIf spLogMode = esplTable Then
    End If
    
    Print #fileNo, addTab(0); "END"
    Print #fileNo, addTab(0); gc_sqlCmdDelim
    
    ' ####################################################################################################################
    ' #    SP for deactivating Stored Procedure Logging
    ' ####################################################################################################################
    
    Dim qualProcedureNameDeactivate As String
    qualProcedureNameDeactivate = genQualProcName(g_sectionIndexSpLog, "DEACTIVATE", ddlType)
    
    printSectionHeader "SP for deactivating Stored Procedure Logging", fileNo
    
    Print #fileNo,
    Print #fileNo, addTab(0); "CREATE PROCEDURE"
    Print #fileNo, addTab(1); qualProcedureNameDeactivate
    Print #fileNo, addTab(0); "("
    genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
    genProcParm fileNo, "IN", "contextSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) schema-filter for contexts to activate"
    genProcParm fileNo, "IN", "contextNamePattern_in", "VARCHAR(80)", True, "(optional) filter for contexts to activate"
    genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of activation-statements executed"
    Print #fileNo, addTab(0); ")"
    Print #fileNo, addTab(0); "RESULT SETS 1"
    Print #fileNo, addTab(0); "LANGUAGE SQL"
    If spLogAutonomousTransaction Then
      Print #fileNo, addTab(0); "AUTONOMOUS"
    End If
    Print #fileNo, addTab(0); "BEGIN"
  
    If spLogMode = esplFile Then
      genSpLogDecl fileNo, -1, True
  
      genSpLogProcEnter fileNo, qualProcedureNameDeactivate, ddlType, , "mode_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out"
    
      Print #fileNo,
      Print #fileNo, addTab(1); "CALL "; qualProcedureNameActivate; "(mode_in, contextSchemaPattern_in, contextNamePattern_in, 'N', rowCount_out);"
    
      genSpLogProcExit fileNo, qualProcedureNameDeactivate, ddlType, , "mode_in", "'contextSchemaPattern_in", "'contextNamePattern_in", "rowCount_out"
    ElseIf spLogMode = esplTable Then
    End If
    
    Print #fileNo, addTab(0); "END"
    Print #fileNo, addTab(0); gc_sqlCmdDelim
  End If

NormalExit:
  On Error Resume Next
  Close #fileNo
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub
  

Sub genSpLogDecl( _
  fileNo As Integer, _
  Optional indent As Integer = 1, _
  Optional genHeader As Boolean = False _
)
  If Not supportSpLogging Or Not generateSpLogMessages Then
    Exit Sub
  End If

  Dim spLogHandleLength As Integer
  spLogHandleLength = IIf(spLogMode = esplFile, 160, 13)

  If genHeader Then
    Dim skipNl As Boolean
    skipNl = False
    If indent < 0 Then
      skipNl = True
      indent = -indent
    End If
  
    genProcSectionHeader fileNo, "declare variables", indent, skipNl
  End If
  
  If spLogMode = esplTable Then
    genVarDecl fileNo, "v_spEntryTimestamp", "TIMESTAMP", "NULL", indent
  End If
  genVarDecl fileNo, "v_spLogHdl", "CHAR(" & spLogHandleLength & ") FOR BIT DATA", "NULL", indent
End Sub


Function pEnterArg( _
  ByRef arg As String, _
  Optional ByRef prefix As String = "',' || ", _
  Optional ByRef postfix As String = " || " _
) As String
  pEnterArg = ""
  
  Dim isStringArg As Boolean
  Dim isDateTimeArg As Boolean
  If Left(arg, 1) = "'" And Left(arg, 2) <> "'." Then
    isStringArg = True
    arg = Right(arg, Len(arg) - 1)
  ElseIf Left(arg, 1) = "#" Then
    isDateTimeArg = True
    arg = Right(arg, Len(arg) - 1)
  End If

  If arg = "" Then
    pEnterArg = ""
  ElseIf arg = "?" Then
    pEnterArg = prefix & "'?'" & postfix
  ElseIf UCase(Right(arg, 4)) = "_OUT" Then
    pEnterArg = prefix & "'[?]'" & postfix
  ElseIf UCase(Right(arg, 6)) = "_INOUT" Then
    If isStringArg Then
      pEnterArg = prefix & "'[' || COALESCE(''''||RTRIM(LEFT(" & arg & ", " & maxSpLogArgLength & "))||'''','-') || ']'" & postfix
    ElseIf isDateTimeArg Then
      pEnterArg = prefix & "'[' || COALESCE(''''||RTRIM(CAST(" & arg & " AS CHAR(" & maxSpLogArgLength & ")))||'''','-') || ']'" & postfix
    Else
      pEnterArg = prefix & "'[' || COALESCE(RTRIM(CAST(" & arg & " AS CHAR(" & maxSpLogArgLength & "))),'-') || ']'" & postfix
    End If
  Else
    If isStringArg Then
      pEnterArg = prefix & "COALESCE(''''||RTRIM(LEFT(" & arg & ", " & maxSpLogArgLength & "))||'''','-')" & postfix
    ElseIf isDateTimeArg Then
      pEnterArg = prefix & "COALESCE(''''||RTRIM(CAST(" & arg & " AS CHAR(" & maxSpLogArgLength & ")))||'''','-')" & postfix
    Else
      pEnterArg = prefix & "COALESCE(RTRIM(CAST(" & arg & " AS CHAR(" & maxSpLogArgLength & "))),'-')" & postfix
    End If
  End If
End Function


Function pExitArg( _
  ByRef arg As String, _
  Optional ByRef prefix As String = "',' || ", _
  Optional ByRef postfix As String = " || " _
) As String
  pExitArg = ""
  
  Dim isStringArg As Boolean
  Dim isDateTimeArg As Boolean
  If Left(arg, 1) = "'" And Left(arg, 2) <> "'." Then
    isStringArg = True
    arg = Right(arg, Len(arg) - 1)
  ElseIf Left(arg, 1) = "#" Then
    isDateTimeArg = True
    arg = Right(arg, Len(arg) - 1)
  End If
  
  If arg = "" Then
    pExitArg = ""
  ElseIf arg = "?" Then
    pExitArg = prefix & "'?'" & postfix
  ElseIf UCase(Right(arg, 4)) = "_OUT" Then
    If isStringArg Then
      pExitArg = prefix & "'[' || COALESCE(''''||RTRIM(LEFT(" & arg & ", " & maxSpLogArgLength & "))||'''','-') || ']'" & postfix
    ElseIf isDateTimeArg Then
      pExitArg = prefix & "'[' || COALESCE(''''||RTRIM(CAST(" & arg & " AS CHAR(" & maxSpLogArgLength & ")))||'''','-') || ']'" & postfix
    Else
      pExitArg = prefix & "'[' || COALESCE(RTRIM(CAST(" & arg & " AS CHAR(" & maxSpLogArgLength & "))),'-') || ']'" & postfix
    End If
  Else
    If isStringArg Then
      pExitArg = prefix & "COALESCE(''''||RTRIM(LEFT(" & arg & ", " & maxSpLogArgLength & "))||'''','-')" & postfix
    ElseIf isDateTimeArg Then
      pExitArg = prefix & "COALESCE(''''||RTRIM(CAST(" & arg & " AS CHAR(" & maxSpLogArgLength & ")))||'''','-')" & postfix
    Else
      pExitArg = prefix & "COALESCE(RTRIM(CAST(" & arg & " AS CHAR(" & maxSpLogArgLength & "))),'-')" & postfix
    End If
  End If
End Function


Sub genSpLogProcEnter( _
  fileNo As Integer, _
  ByRef qualProcName As String, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional indent As Integer = 1, _
  Optional ByRef arg1 As String = "", _
  Optional ByRef arg2 As String = "", _
  Optional ByRef arg3 As String = "", _
  Optional ByRef arg4 As String = "", _
  Optional ByRef arg5 As String = "", _
  Optional ByRef arg6 As String = "", _
  Optional ByRef arg7 As String = "", _
  Optional ByRef arg8 As String = "", _
  Optional ByRef arg9 As String = "", _
  Optional ByRef arg10 As String = "", _
  Optional ByRef arg11 As String = "", _
  Optional ByRef arg12 As String = "" _
)
  If Not supportSpLogging Or Not generateSpLogMessages Then
    Exit Sub
  End If

  Dim procSchemaName As String
  Dim procName As String
  procSchemaName = getSchemaName(qualProcName)
  procName = getUnqualObjName(qualProcName)
  
  If implementSpLogByWrapper Then
    Dim skipNl As Boolean
    skipNl = False
    If indent < 0 Then
      skipNl = True
      indent = -indent
    End If
  
    genProcSectionHeader fileNo, "log procedure entry", indent, skipNl
    Print #fileNo, addTab(indent); "CALL "; genQualProcName(g_sectionIndexSpLog, "SPLOG_ENTER", ddlType); _
                                   "(v_spLogHdl, v_spEntryTimestamp, "; _
                                   "'" & procSchemaName & "', "; _
                                   "'" & procName & "', "; _
                                   pEnterArg(arg1, ""); _
                                   pEnterArg(arg2); _
                                   pEnterArg(arg3); _
                                   pEnterArg(arg4); _
                                   pEnterArg(arg5); _
                                   pEnterArg(arg6); _
                                   pEnterArg(arg7); _
                                   pEnterArg(arg8); _
                                   pEnterArg(arg9); _
                                   pEnterArg(arg10); _
                                   pEnterArg(arg11); _
                                   pEnterArg(arg12); _
                                   "''"; _
                                   ");"
  Else
    Print #fileNo, addTab(indent + 0); "BEGIN"
    genCondDecl fileNo, "implNotFound", "42724", indent + 1
    genCondDecl fileNo, "procTerminated", "38503", indent + 1
    Print #fileNo, addTab(indent + 1); "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;"
    Print #fileNo, addTab(indent + 1); "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;"
  
    Print #fileNo, addTab(indent + 1); "CALL "; genQualProcName(g_sectionIndexDbAdmin, "OPEN_LOG", ddlType); _
                                       "('"; qualProcName; "',v_spLogHdl);"
    Print #fileNo, addTab(indent + 1); "CALL "; genQualProcName(g_sectionIndexDbAdmin, "LOGINFO", ddlType); _
                                       "(v_spLogHdl, '--> entering Procedure "; _
                                       qualProcName; _
                                       "(' || "; _
                                       pEnterArg(arg1, ""); _
                                       pEnterArg(arg2); _
                                       pEnterArg(arg3); _
                                       pEnterArg(arg4); _
                                       pEnterArg(arg5); _
                                       pEnterArg(arg6); _
                                       pEnterArg(arg7); _
                                       pEnterArg(arg8); _
                                       pEnterArg(arg9); _
                                       pEnterArg(arg10); _
                                       pEnterArg(arg11); _
                                       pEnterArg(arg12); _
                                       "')');"
    Print #fileNo, addTab(indent + 0); "END;"
  End If
End Sub


Sub genSpLogProcExit( _
  fileNo As Integer, _
  ByRef qualProcName As String, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional indent As Integer = 1, _
  Optional ByRef arg1 As String = "", _
  Optional ByRef arg2 As String = "", _
  Optional ByRef arg3 As String = "", _
  Optional ByRef arg4 As String = "", _
  Optional ByRef arg5 As String = "", _
  Optional ByRef arg6 As String = "", _
  Optional ByRef arg7 As String = "", _
  Optional ByRef arg8 As String = "", _
  Optional ByRef arg9 As String = "", _
  Optional ByRef arg10 As String = "", _
  Optional ByRef arg11 As String = "", _
  Optional ByRef arg12 As String = "" _
)
  If Not supportSpLogging Or Not generateSpLogMessages Then
    Exit Sub
  End If
  
  Dim procSchemaName As String
  Dim procName As String
  procSchemaName = getSchemaName(qualProcName)
  procName = getUnqualObjName(qualProcName)
  
  If implementSpLogByWrapper Then
    Dim skipNl As Boolean
    skipNl = False
    If indent < 0 Then
      skipNl = True
      indent = -indent
    End If
    
    genProcSectionHeader fileNo, "log procedure exit", indent, skipNl
    Print #fileNo, addTab(indent); "CALL "; genQualProcName(g_sectionIndexSpLog, "SPLOG_EXIT", ddlType); _
                                   "(v_spLogHdl, v_spEntryTimestamp, "; _
                                   "'" & procSchemaName & "', "; _
                                   "'" & procName & "', "; _
                                   pExitArg(arg1, ""); _
                                   pExitArg(arg2); _
                                   pExitArg(arg3); _
                                   pExitArg(arg4); _
                                   pExitArg(arg5); _
                                   pExitArg(arg6); _
                                   pExitArg(arg7); _
                                   pExitArg(arg8); _
                                   pExitArg(arg9); _
                                   pExitArg(arg10); _
                                   pExitArg(arg11); _
                                   pExitArg(arg12); _
                                   "''"; _
                                   ");"
  Else
    genSpLogProcExitByMode fileNo, qualProcName, , ddlType, indent, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12
  End If
End Sub


Sub genSpLogProcEscape( _
  fileNo As Integer, _
  ByRef qualProcName As String, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional indent As Integer = 1, _
  Optional ByRef arg1 As String = "", _
  Optional ByRef arg2 As String = "", _
  Optional ByRef arg3 As String = "", _
  Optional ByRef arg4 As String = "", _
  Optional ByRef arg5 As String = "", _
  Optional ByRef arg6 As String = "", _
  Optional ByRef arg7 As String = "", _
  Optional ByRef arg8 As String = "", _
  Optional ByRef arg9 As String = "", _
  Optional ByRef arg10 As String = "", _
  Optional ByRef arg11 As String = "", _
  Optional ByRef arg12 As String = "" _
)
  If Not supportSpLogging Or Not generateSpLogMessages Then
    Exit Sub
  End If

  Dim procSchemaName As String
  Dim procName As String
  procSchemaName = getSchemaName(qualProcName)
  procName = getUnqualObjName(qualProcName)
  
  If implementSpLogByWrapper Then
    Dim skipNl As Boolean
    skipNl = False
    If indent < 0 Then
      skipNl = True
      indent = -indent
    End If
    
    genProcSectionHeader fileNo, "log procedure escape", indent, skipNl
    Print #fileNo, addTab(indent); "CALL "; genQualProcName(g_sectionIndexSpLog, "SPLOG_ESC", ddlType); _
                                   "(v_spLogHdl, v_spEntryTimestamp, "; _
                                   "'" & procSchemaName & "', "; _
                                   "'" & procName & "', "; _
                                   pExitArg(arg1, ""); _
                                   pExitArg(arg2); _
                                   pExitArg(arg3); _
                                   pExitArg(arg4); _
                                   pExitArg(arg5); _
                                   pExitArg(arg6); _
                                   pExitArg(arg7); _
                                   pExitArg(arg8); _
                                   pExitArg(arg9); _
                                   pExitArg(arg10); _
                                   pExitArg(arg11); _
                                   pExitArg(arg12); _
                                   "''"; _
                                   ");"
  Else
    genSpLogProcExitByMode fileNo, qualProcName, "escaping", ddlType, indent, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12
  End If
End Sub


Private Sub genSpLogProcExitByMode( _
  fileNo As Integer, _
  ByRef procName As String, _
  Optional ByRef mode As String = "leaving", _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional indent As Integer = 1, _
  Optional ByRef arg1 As String = "", _
  Optional ByRef arg2 As String = "", _
  Optional ByRef arg3 As String = "", _
  Optional ByRef arg4 As String = "", _
  Optional ByRef arg5 As String = "", _
  Optional ByRef arg6 As String = "", _
  Optional ByRef arg7 As String = "", _
  Optional ByRef arg8 As String = "", _
  Optional ByRef arg9 As String = "", _
  Optional ByRef arg10 As String = "", _
  Optional ByRef arg11 As String = "", _
  Optional ByRef arg12 As String = "" _
)
  Print #fileNo, addTab(indent + 0); "BEGIN"
  genCondDecl fileNo, "implNotFound", "42724", indent + 1
  genCondDecl fileNo, "procTerminated", "38503", indent + 1
  Print #fileNo, addTab(indent + 1); "DECLARE CONTINUE HANDLER FOR implNotFound   BEGIN END;"
  Print #fileNo, addTab(indent + 1); "DECLARE CONTINUE HANDLER FOR procTerminated BEGIN END;"
  
  Print #fileNo, addTab(indent + 1); "CALL "; genQualProcName(g_sectionIndexDbAdmin, "LOGINFO", ddlType); _
                                     "(v_spLogHdl, '<-- "; mode; " leaving Procedure "; _
                                     procName; _
                                     "(' || "; _
                                     pExitArg(arg1, ""); _
                                     pExitArg(arg2); _
                                     pExitArg(arg3); _
                                     pExitArg(arg4); _
                                     pExitArg(arg5); _
                                     pExitArg(arg6); _
                                     pExitArg(arg7); _
                                     pExitArg(arg8); _
                                     pExitArg(arg9); _
                                     pExitArg(arg10); _
                                     pExitArg(arg11); _
                                     pExitArg(arg12); _
                                     "')');"
  Print #fileNo, addTab(indent + 1); "CALL "; genQualProcName(g_sectionIndexDbAdmin, "CLOSE_LOG", ddlType); _
                                     "(v_spLogHdl);"
  Print #fileNo, addTab(indent + 0); "END;"
End Sub
