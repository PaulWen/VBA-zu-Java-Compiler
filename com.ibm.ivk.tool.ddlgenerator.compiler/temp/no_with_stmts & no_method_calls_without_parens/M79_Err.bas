 Attribute VB_Name = "M79_Err"
 Option Explicit
 
 Private Const colEntryFilter = 1
 Private Const colId = 2
 Private Const colIsActive = colId + 1
 Private Const colIsTechnical = colIsActive + 1
 Private Const colSqlState = colIsTechnical + 1
 Private Const colBusErrorMessageNo = colSqlState + 1
 Private Const colMessagePattern = colBusErrorMessageNo + 1
 Private Const colLength = colMessagePattern + 1
 Private Const colMessageExplanation = colLength + 1
 Private Const colBusErrorMessageText = colMessageExplanation + 1
 Private Const colComment = colBusErrorMessageText + 1
 Private Const colContext = colComment + 1
 
 Private Const processingStep = 2
 Private Const acmCsvProcessingStep = 1
 Private Const firstRow = 3
 Private Const sheetName = "Err"
 
 Global g_errs As ErrDescriptors
 
 
 Private Sub readSheet()
   initErrDescriptors(g_errs)

   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   While thisSheet.Cells(thisRow, colId) & "" <> ""
     If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
       GoTo NextRow
     End If

     If getBoolean(thisSheet.Cells(thisRow, colIsActive)) Then
         g_errs.descriptors(allocErrDescriptorIndex(g_errs)).id = Trim(thisSheet.Cells(thisRow, colId))
         g_errs.descriptors(allocErrDescriptorIndex(g_errs)).isTechnical = getBoolean(thisSheet.Cells(thisRow, colIsTechnical))
         g_errs.descriptors(allocErrDescriptorIndex(g_errs)).sqlStateOffset = getInteger(thisSheet.Cells(thisRow, colSqlState))
         g_errs.descriptors(allocErrDescriptorIndex(g_errs)).busErrorMessageNo = Trim(thisSheet.Cells(thisRow, colBusErrorMessageNo))
         g_errs.descriptors(allocErrDescriptorIndex(g_errs)).messagePattern = Trim(thisSheet.Cells(thisRow, colMessagePattern))
 
         g_errs.descriptors(allocErrDescriptorIndex(g_errs)).messageExplanation = Trim(thisSheet.Cells(thisRow, colMessageExplanation))
         g_errs.descriptors(allocErrDescriptorIndex(g_errs)).conEnumLabelText = Trim(thisSheet.Cells(thisRow, colContext))
     End If

 NextRow:
     thisRow = thisRow + 1
   Wend
 End Sub
 
 
 Sub getErrs()
   If (g_errs.numDescriptors = 0) Then
     readSheet()
   End If
 End Sub
 
 
 Sub resetErrs()
   g_errs.numDescriptors = 0
 End Sub
 
 
 Private Function transformErrMsg( _
   msgIndex As Integer, _
   ByRef arg1 As Variant, _
   ByRef arg2 As Variant, _
   ByRef arg3 As Variant, _
   ByRef arg4 As Variant, _
   ByRef arg5 As Variant, _
   ByRef arg6 As Variant, _
   ByRef arg7 As Variant, _
   ByRef arg8 As Variant, _
   ByRef arg9 As Variant, _
   Optional ByRef parm1 As String = "", _
   Optional ByRef parm2 As String = "", _
   Optional ByRef parm3 As String = "", _
   Optional ByRef parm4 As String = "" _
 ) As String
   Dim result As String
     result = Replace(g_errs.descriptors(msgIndex).messagePattern, "%1", arg1 & "")
     If g_errs.descriptors(msgIndex).busErrorMessageNo <> "" Then
       result = Replace(result, "%b", g_errs.descriptors(msgIndex).busErrorMessageNo & "")
     End If
   result = Replace(result, "%2", arg2 & "")
   result = Replace(result, "%3", arg3 & "")
   result = Replace(result, "%4", arg4 & "")
   result = Replace(result, "%5", arg5 & "")
   result = Replace(result, "%6", arg6 & "")
   result = Replace(result, "%7", arg7 & "")
   result = Replace(result, "%8", arg8 & "")
   result = Replace(result, "%9", arg9 & "")

   result = Replace(result, "'", "''")

   result = Replace(result, "$1", "' || " & parm1 & " || '")
   result = Replace(result, "$2", "' || " & parm2 & " || '")
   result = Replace(result, "$3", "' || " & parm3 & " || '")
   result = Replace(result, "$4", "' || " & parm4 & " || '")

   transformErrMsg = "'" & g_cfgSqlMsgPrefix & result & "'"
 End Function
 
 
 Function getSqlStateByOffset( _
   offset As Integer _
 ) As Long
   getSqlStateByOffset = g_cfgSqlStateStart + offset
 End Function
 
 Sub genSignalDdl( _
   id As String, _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional arg1 As Variant = "", _
   Optional arg2 As Variant = "", _
   Optional arg3 As Variant = "", _
   Optional arg4 As Variant = "", _
   Optional arg5 As Variant = "", _
   Optional arg6 As Variant = "", _
   Optional arg7 As Variant = "", _
   Optional arg8 As Variant = "", _
   Optional arg9 As Variant = "" _
 )
   Dim i As Integer
   For i = 1 To g_errs.numDescriptors
       If UCase(g_errs.descriptors(i).id) = UCase(id) Then
         Print #fileNo, addTab(indent); "SIGNAL SQLSTATE '"; CStr(getSqlStateByOffset(g_errs.descriptors(i).sqlStateOffset)); _
                        "' SET MESSAGE_TEXT = "; transformErrMsg(i, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9); ";"
         Exit Sub
       End If
   Next i

   logMsg("unknown SIGNAL id '" & id & "'", ellError, edtNone)
 End Sub
 
 
 Sub genSigMsgVarDecl( _
   fileNo As Integer, _
   Optional indent As Integer = 1 _
 )
   genVarDecl(fileNo, "v_msg", "VARCHAR(" & gc_dbMaxSignalMessageLength & ")", "NULL", indent)
 End Sub
 
 Sub genSignalDdlWithParms( _
   id As String, _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional ByRef arg1 As Variant = "", _
   Optional ByRef arg2 As Variant = "", _
   Optional ByRef arg3 As Variant = "", _
   Optional ByRef arg4 As Variant = "", _
   Optional ByRef arg5 As Variant = "", _
   Optional ByRef arg6 As Variant = "", _
   Optional ByRef arg7 As Variant = "", _
   Optional ByRef arg8 As Variant = "", _
   Optional ByRef arg9 As Variant = "", _
   Optional ByRef parm1 As String = "", _
   Optional ByRef parm2 As String = "", _
   Optional ByRef parm3 As String = "", _
   Optional ByRef parm4 As String = "" _
 )
   Dim i As Integer
   For i = 1 To g_errs.numDescriptors
       If UCase(g_errs.descriptors(i).id) = UCase(id) Then
         Print #fileNo, addTab(indent); "SET v_msg = RTRIM(LEFT("; _
                                        transformErrMsg(i, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, parm1, parm2, parm3, parm4); _
                                        ","; CStr(gc_dbMaxSignalMessageLength); "));"

         Print #fileNo, addTab(indent); "SIGNAL SQLSTATE '"; CStr(getSqlStateByOffset(g_errs.descriptors(i).sqlStateOffset)); "' "; _
                                        "SET MESSAGE_TEXT = v_msg;"
         Exit Sub
       End If
   Next i
 
   logMsg("unknown SIGNAL id '" & id & "'", ellError, edtNone)
 End Sub
 
 
 Sub genSignalDdlWithParmsForCompoundSql( _
   id As String, _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional ByRef arg1 As Variant = "", _
   Optional ByRef arg2 As Variant = "", _
   Optional ByRef arg3 As Variant = "", _
   Optional ByRef arg4 As Variant = "", _
   Optional ByRef arg5 As Variant = "", _
   Optional ByRef arg6 As Variant = "", _
   Optional ByRef arg7 As Variant = "", _
   Optional ByRef arg8 As Variant = "", _
   Optional ByRef arg9 As Variant = "", _
   Optional ByRef parm1 As String = "", _
   Optional ByRef parm2 As String = "", _
   Optional ByRef parm3 As String = "", _
   Optional ByRef parm4 As String = "" _
 )
   Dim i As Integer
   For i = 1 To g_errs.numDescriptors
       If UCase(g_errs.descriptors(i).id) = UCase(id) Then
         Print #fileNo, addTab(indent); "SET v_msg = LEFT("; _
                                        transformErrMsg(i, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, parm1, parm2, parm3, parm4); _
                                        ","; CStr(gc_dbMaxSignalMessageLength); ");"

         Print #fileNo, addTab(indent); "SIGNAL SQLSTATE '"; CStr(getSqlStateByOffset(g_errs.descriptors(i).sqlStateOffset)); "' "; _
                                        "SET MESSAGE_TEXT = v_msg;"
         Exit Sub
       End If
   Next i

   logMsg("unknown SIGNAL id '" & id & "'", ellError, edtNone)
 End Sub
 
 
 Sub dropErrorCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   killCsvFileWhereEver(g_sectionIndexDbMeta, clnErrMsg, g_targetDir, acmCsvProcessingStep, onlyIfEmpty, "ACM")
 End Sub
 
 
 Sub genErrorCsv( _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnErrMsg, acmCsvProcessingStep, "ACM", ddlType)
   assertDir(fileName)
   fileNo = FreeFile()
   Open fileName For Append As #fileNo
   On Error GoTo ErrorExit

   Dim i As Integer
   Dim newline As String
   newline = "" & vbLf
   For i = 1 To g_errs.numDescriptors
       Print #fileNo, CStr(g_errs.descriptors(i).sqlStateOffset + g_cfgSqlStateStart); ",";
       Print #fileNo, IIf(g_errs.descriptors(i).busErrorMessageNo <> "", g_errs.descriptors(i).busErrorMessageNo, ""); ",";
       Print #fileNo, """"; Replace(g_errs.descriptors(i).messageExplanation, """", """"""); """,";
       Print #fileNo, """"; Replace(Replace(g_errs.descriptors(i).conEnumLabelText, newline, "\n", , , vbBinaryCompare), """", """"""); """,";
       Print #fileNo, getCsvTrailer(0)
   Next i

   Close #fileNo
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
