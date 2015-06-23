 Attribute VB_Name = "M94_DBAdmin"
 Option Explicit
 
 Private Const processingStepAdmin = 4
 
 Private Const pc_tempTabNameExcludeSchema = "SESSION.ExcludeSchema"
 Private Const pc_tempTabNameExcludeName = "SESSION.ExcludeName"
 
 Global Const tempTabNameGrant = "SESSION.Grant"
 Global Const tempTabNameStatement = "SESSION.Statements"
 Global Const tempTabNameTableCfg = "SESSION.TableCfg"
 Global Const tempTabNameReorgChk = "SESSION.ReorgChk"
 
 
 Sub genDdlForTempStatement( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional ByRef maxStatementLength As Long = 400, _
   Optional autoSeq As Boolean = False, _
   Optional onCommitPreserve As Boolean = True, _
   Optional onRollbackPreserve As Boolean = True, _
   Optional includeCommentCol As Boolean = False, _
   Optional ByRef tabNameSuffix As String = "", _
   Optional includeSeqCol As Boolean = True, _
   Optional includeExecTimeCol As Boolean = False, _
   Optional includeFlagCol As Boolean = False, _
   Optional skipNl As Boolean = False, _
   Optional ByRef extraColName As String = "", _
   Optional ByRef extraColType As String = "", _
   Optional ByRef extraColName2 As String = "", _
   Optional ByRef extraColType2 As String = "" _
 )
   Dim colNameLength As Integer
   colNameLength = 9

   If Len(extraColName & "") > colNameLength Then
     colNameLength = Len(extraColName)
   End If
   If Len(extraColName2 & "") > colNameLength Then
     colNameLength = Len(extraColName2)
   End If

   genProcSectionHeader fileNo, "temporary table for statements", indent, skipNl
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempTabNameStatement; tabNameSuffix
   Print #fileNo, addTab(indent + 0); "("

   If includeSeqCol Then
     Print #fileNo, addTab(indent + 1); paddRight("seqNo", colNameLength); " INTEGER"; IIf(autoSeq, " GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1)", ""); ","
   End If
   If maxStatementLength > 32672 Then
     logMsg "CLOB larger than 32672 bytres may not be used in temporary Statement-table", ellError
     maxStatementLength = 32672
   End If

   If includeCommentCol Then
     Print #fileNo, addTab(indent + 1); paddRight("comment", colNameLength); " VARCHAR(100),"
   End If

   If includeExecTimeCol Then
     Print #fileNo, addTab(indent + 1); paddRight("execTime", colNameLength); " TIME,"
   End If

   If includeFlagCol Then
     Print #fileNo, addTab(indent + 1); paddRight("flag", colNameLength); " CHAR(1),"
   End If

   If extraColName & "" <> "" Then
     Print #fileNo, addTab(indent + 1); paddRight(extraColName, colNameLength); " "; extraColType; ","
   End If

   If extraColName2 & "" <> "" Then
     Print #fileNo, addTab(indent + 1); paddRight(extraColName2, colNameLength); " "; extraColType2; ","
   End If

   If maxStatementLength > 32672 Then
     logMsg "CLOB larger than 32000 bytes may not be used in temporary table", ellError
     Print #fileNo, addTab(indent + 1); paddRight("statement", colNameLength); " CLOB("; CStr(maxStatementLength); ")"
   Else
     Print #fileNo, addTab(indent + 1); paddRight("statement", colNameLength); " VARCHAR("; CStr(maxStatementLength); ")"
   End If

   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 End Sub
 
 
 Sub genDdlForTempGrants( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = True, _
   Optional onRollbackPreserve As Boolean = True, _
   Optional includeParmSignatureCol As Boolean = False _
 )
   genProcSectionHeader fileNo, "temporary table for GRANTs", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"

   Print #fileNo, addTab(indent + 1); tempTabNameGrant
   Print #fileNo, addTab(indent + 0); "("

   Print #fileNo, addTab(indent + 1); "schemaName    "; g_dbtDbSchemaName; "  NOT NULL,"
   Print #fileNo, addTab(indent + 1); "objectName    VARCHAR(100) NOT NULL,"
   If includeParmSignatureCol Then
     Print #fileNo, addTab(indent + 1); "parmSignature VARCHAR(500) NOT NULL,"
   End If
   Print #fileNo, addTab(indent + 1); "grantee       VARCHAR(100) NOT NULL,"
   Print #fileNo, addTab(indent + 1); "granteeType   CHAR(1)      NOT NULL,"
   Print #fileNo, addTab(indent + 1); "privilege     VARCHAR(10)  NOT NULL"

   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 End Sub
 
 
 Sub genDdlForTempTableCfg( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = True, _
   Optional onRollbackPreserve As Boolean = True _
 )
   genProcSectionHeader fileNo, "temporary table for table configuration", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempTabNameTableCfg
   Print #fileNo, addTab(indent + 0); "("

   Print #fileNo, addTab(indent + 1); "tabSchema           "; g_dbtDbSchemaName; ","
   Print #fileNo, addTab(indent + 1); "tabName             VARCHAR(50),"
   Print #fileNo, addTab(indent + 1); "pctFree             SMALLINT,"
   Print #fileNo, addTab(indent + 1); "isVolatile          "; g_dbtBoolean; ","
   Print #fileNo, addTab(indent + 1); "useCompression      "; g_dbtBoolean; ","
   Print #fileNo, addTab(indent + 1); "useIndexCompression "; g_dbtBoolean

   Print #fileNo, addTab(indent + 0); ")"

   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 End Sub
 
 
 Sub genDbAdminDdl( _
   ddlType As DdlTypeId _
 )
   If ddlType = edtPdm Then
     Dim thisPoolIndex As Integer
     Dim thisOrgIndex As Integer

     For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
       For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
         If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) Then
           genDbAdminDdlByPool thisOrgIndex, thisPoolIndex, edtPdm
         End If
       Next thisOrgIndex
     Next thisPoolIndex
   End If

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbAdmin, processingStepAdmin, ddlType, , , , phaseDbSupport)

   On Error GoTo ErrorExit

   genDbAdminDdl0 fileNo, ddlType
   genDbAdminDdl1 fileNo, ddlType
   genDdlSetgrants1 fileNo, ddlType
   genDdlSetgrants2 fileNo, ddlType
 ' ### IF IVK ###
   genDdlAutoDeploy fileNo, ddlType
 ' ### ENDIF IVK ###
   genDdlRedirRestore fileNo, ddlType
   genDbAdminDdl2 fileNo, ddlType
   genDbAdminDdl3 fileNo, ddlType
   genDbAdminDdl4 fileNo, ddlType
   genDdlSetTabCfg fileNo, ddlType
   genDbAdminDdl5 fileNo, ddlType
   genDbAdminDdlCompressionEstimation fileNo, ddlType
   genDbAdminDdl6 fileNo, ddlType
   genDbAdminDdl7 fileNo, ddlType
   genDbAdminDdl9 fileNo, ddlType
   genDbAdminDdl10 fileNo, ddlType
 ' ### IF IVK ###
   genDbAdminDdl11 fileNo, ddlType
   genDbAdminDdlOrgInitMetaBus fileNo, ddlType
   genDbAdminDdlOrgInitDupCode fileNo, ddlType
   genDbAdminDdlOrgInitEnp fileNo, ddlType
   genDbAdminDdlOrgInit fileNo, ddlType
   genDbAdminDdlOrgInit2 fileNo, ddlType
   genDbAdminDdlDivCreate fileNo, ddlType
   genDbAdminDdlMessageUpdate fileNo, ddlType
   genDdlAddTestUser fileNo, ddlType
 ' ### ENDIF IVK ###
   genDdlDbCompact fileNo, ddlType
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genDbMoveScript( _
   fileNo As Integer, _
   sectionNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   Dim qualViewNameMoveExp As String
   qualViewNameMoveExp = genQualViewName(g_sectionIndexDbAdmin, vnPdmExportStmntMove, vnsPdmExportStmntMove, ddlType)

   Dim qualViewNameMoveImp As String
   qualViewNameMoveImp = genQualViewName(g_sectionIndexDbAdmin, vnPdmImportStmntMove, vnsPdmImportStmntMove, ddlType)

   Dim qualViewNameMoveLoad As String
   qualViewNameMoveLoad = genQualViewName(g_sectionIndexDbAdmin, vnPdmLoadStmntMove, vnsPdmLoadStmntMove, ddlType)

   Dim qualViewNameMoveStmnts As String
   qualViewNameMoveStmnts = genQualViewName(g_sectionIndexDbAdmin, vnPdmMoveScript, vnsPdmMoveScript, ddlType)

   If sectionNo = 1 Then
     GoTo Sect1
   ElseIf sectionNo = 2 Then
     GoTo Sect2
   ElseIf sectionNo = 3 Then
     GoTo Sect3
   Else
     Exit Sub
   End If
 
 Sect1:
   Print #fileNo, addTab(2); "'#!/usr/bin/ksh' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'##########################################################################################' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# Script parameter:' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'dbName=' || CHR(10) ||"
   Print #fileNo, addTab(2); "'dbUser=' || CHR(10) ||"
   Print #fileNo, addTab(2); "'dbPw=' || CHR(10) ||"
   Print #fileNo, addTab(2); "'srcDbName=' || CHR(10) ||"
   Print #fileNo, addTab(2); "'force=0' || CHR(10) ||"
   Print #fileNo, addTab(2); "'silent=0' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'##########################################################################################' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# Utility procedures:' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'dbConnect() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local dbName=${1}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local dbUser=${2}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local dbPw=${3}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  db2 ""TERMINATE"" > /dev/null' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  if [ ""${dbUser}"" ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    db2 ""CONNECT TO $dbName USER $dbUser USING ${dbPw}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  else' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    db2 ""CONNECT TO ${dbName}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  rc=$?' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ ${rc} -gt 2 ] && exit ${rc}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'dbTerminate() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  db2 ""TERMINATE""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'ts() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  date +''[ %Y.%m.%d-%H:%M:%S ] ''' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'setCodePage1208() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local dbName=${1}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local dbUser=${2}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local dbPw=${3}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  dbTerminate > /dev/null' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  db2set DB2CODEPAGE=1208' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  sleep 5' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  dbConnect ${dbName} ${dbUser} ${dbPw} > /dev/null' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'resetCodePage() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  db2set DB2CODEPAGE=' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'tb() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  tr -d ''\r'' | sed ''s/[ ]*$//''' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'busy() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local interval=${1}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  i=1' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  while true; do' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    sleep $interval' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    printf "".""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    i=`expr ${i} + 1`' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    if [ ${i} = 60 ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      i=0' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      printf ""\n""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  done' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'startBusy() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local msg=${1}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local interval=${2}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  if [ ""${interval}"" = 0 ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    if [ ""${msg}"" ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      printf ""%s%s"" ""`ts`"" ""${msg}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    busyPid=' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    return 0' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  if [ ""${silent}"" = 0 ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    [ ""${msg}"" ] && printf ""%s%s [''.'' ~ %s sec]\n."" ""`ts`"" ""${msg}"" ""${interval}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'    busy ${interval} &' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    busyPid=$!' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  else' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    [ ""${msg}"" ] && printf ""%s%s"" ""`ts`"" ""${msg}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'stopBusy() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local msg=${1}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ ""${busyPid}"" ] && kill -9 ""${busyPid}"" 2>&1 > /dev/null' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  printf ""\n""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ ""${msg}"" ] && printf ""%s%s\n"" ""`ts`"" ""${msg}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'busyPid=' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'assertDb2Rc() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local rc=${1:-0}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local logFile=${2}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local rcThreshold=${3}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ -z ""${rcThreshold}"" ] && rcThreshold=2' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  if [ ""${rc}"" -gt ""${rcThreshold}""  ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    stopBusy ""failed. [rc=${rc}]""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    printf ""\n""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    if [ ""${logFile}"" ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      printf ""##########################################\n\n""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      tail -30 ""${logFile}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      printf ""\n##########################################\n""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    if [ ""${force}"" -ne 1 ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      resetCodePage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      exit ${rc}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'execDb2Script() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local msg=${1}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local scriptBaseName=${2}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local interval=${3}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local refDir=${4}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local rcThreshold=${5}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local cmdTermChar=${6}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ -z ""${rcThreshold}"" ] && rcThreshold=2' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  local rc' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ -z ""${interval}"" ] && interval=1' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  local db2CmdArgs' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  db2CmdArgs=""-v -s""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ -z ""${cmdTermChar}"" ] || db2CmdArgs=""${db2CmdArgs} -td${cmdTermChar}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  local infix=""""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  case ""${scriptBaseName}"" in' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    export*) infix="".${dbName}"";;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    import*) infix="".${dbName}"";;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    load*)   infix="".${dbName}"";;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  esac' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ ""${msg}"" ] && startBusy ""${msg} (output logged in ${scriptBaseName}${infix}.log)"" ""${interval}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  db2 ${db2CmdArgs} -f ""${refDir}${scriptBaseName}.db2"" > ""${refDir}${scriptBaseName}${infix}.log""' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  rc=$?' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  assertDb2Rc ""${rc}"" ""${refDir}${scriptBaseName}${infix}.log"" ""${rcThreshold}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ ""${msg}"" ] && stopBusy ""done. [rc=${rc}]""' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  return ${rc}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'execDb2Cmd() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local msg=${1}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local cmd=${2}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local scriptName=${3}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local interval=${4}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local rcThreshold=${5}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ -z ""${rcThreshold}"" ] && rcThreshold=2' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  local rc' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ -z ""${interval}"" ] && interval=1' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ ""${msg}"" ] && startBusy ""${msg}"" ""${interval}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  db2 -x ""${cmd}"" | tb > ${scriptName}.db2'  || CHR(10) ||"
   Print #fileNo, addTab(2); "'  rc=$?' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  assertDb2Rc ""${rc}"" ""${scriptName}.db2"" ""${rcThreshold}"" ' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ ""${msg}"" ] && stopBusy ""done. [rc=${rc}]""' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  return ${rc}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'checkForReject() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local logFile=${1}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local operation=${2}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local refDir=${3}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  if grep ''rejected  '' ""${refDir}${logFile}"" | grep -q -v ''= 0''; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    printf ""%s%s:\n\n"" ""`ts`"" ""${operation}-error reported. Check file ${logFile}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    printf ""##########################################\n\n""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    tail -30 ""${logFile}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    printf ""\n##########################################\n""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    resetCodePage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    exit 1' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  fi;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'resolveBackupPending() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local dbName=${1}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local dbUser=${2}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local dbPw=${3}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local msg=${4}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local scriptBaseName=${5}-backup' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local interval=${6}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local refDir=${7}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local rcThreshold=${8}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local cmdTermChar=${9}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local doQuiesce=${10}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  local rc=0' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local restarted=0' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  [ -z ""${doQuiesce}"" ] && doQuiesce=1' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  if db2 -x ""SELECT 1 FROM TABLE(SYSPROC.SNAPSHOT_TBS_CFG(CURRENT SERVER,-1)) AS SN WHERE (MOD(TABLESPACE_STATE, 64) / 32) > 0 FETCH FIRST 1 ROW ONLY"" | grep -q 1; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    echo BACKUP DB ""${dbName}"" TO /dev/null > ""${refDir}/${scriptBaseName}"".db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    chmod 644 ""${refDir}/${scriptBaseName}"".db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    dbTerminate > /dev/null' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    sleep 5' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    execDb2Script ""${msg}"" ""${scriptBaseName}"" ""${interval}"" ""${refDir}"" ""4"" ""${cmdTermChar}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    rc=$?' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    if [ ""${rc}"" = 4 ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      sleep 5' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      execDb2Script ""${msg}"" ""${scriptBaseName}"" ""${interval}"" ""${refDir}"" ""4"" ""${cmdTermChar}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      rc=$?' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      if [ ""${rc}"" = 4 ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        db2stop FORCE >> ${refDir}${scriptBaseName}.${dbName}.log' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        db2start >> ${refDir}${scriptBaseName}.${dbName}.log' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        execDb2Script ""${msg}"" ""${scriptBaseName}"" ""${interval}"" ""${refDir}"" ""${rcThreshold}"" ""${cmdTermChar}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        restarted=1' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'    dbConnect ${dbName} ${dbUser} ${dbPw} >> ${refDir}${scriptBaseName}.${dbName}.log' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    if [ ""${restarted}"" = 1 -a ""${doQuiesce}"" = 1 ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      db2 ""QUIESCE DB IMMEDIATE FORCE CONNECTIONS"" >> ${refDir}${scriptBaseName}.${dbName}.log' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'##########################################################################################' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# Verify usage:' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'cmd=`basename ${0}`' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'case ""${cmd}"" in' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  import*) scriptUsage=""-d <dbName> [ -u <dbUser> -p <dbPw> ] -s <srcDbName>""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'           ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  load*)   scriptUsage=""-d <dbName> [ -u <dbUser> -p <dbPw> ] -s <srcDbName>""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'           ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  *)       scriptUsage=""-d <dbName> [ -u <dbUser> -p <dbPw> ]""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'           ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'esac' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'usage() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  local ec=${1:-250}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  printf ""Usage: $scriptUsage\n""' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  exit ${ec}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'while getopts d:u:p:s:fSx opt 2> /dev/null' || CHR(10) ||"
   Print #fileNo, addTab(2); "'do' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  case ${opt} in' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    f)  force=1            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    d)  dbName=${OPTARG}   ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    u)  dbUser=${OPTARG}   ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    p)  dbPw=${OPTARG}     ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    s)  srcDbName=${OPTARG};;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    S)  silent=1           ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    *)  usage              ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  esac' || CHR(10) ||"
   Print #fileNo, addTab(2); "'done' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'shift `expr ${OPTIND} - 1`' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'case ""${cmd}"" in' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  how*) ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  *)    [ -z ""${dbName}""              ] && usage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        [ -z ""${dbUser}"" -a ""${dbPw}"" ] && usage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        [ -z ""${dbPw}"" -a ""${dbUser}"" ] && usage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'esac' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'dbName=`echo ${dbName} | tr ''[:upper:]'' ''[:lower:]''`' || CHR(10) ||"
   Print #fileNo, addTab(2); "'srcDbName=`echo ${srcDbName} | tr ''[:upper:]'' ''[:lower:]''`' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'case ""${cmd}"" in' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  import*) [ -z ""${srcDbName}"" ] && usage;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  load*)   [ -z ""${srcDbName}"" ] && usage;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'esac' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10)"

   Exit Sub
 
 Sect2:
   Print #fileNo, addTab(2); "'##########################################################################################' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# main routines' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'setup() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  dbConnect ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  startBusy ""Retrieve shell-script setup.sh"" 0' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  db2 -x ""SELECT stmnt FROM "; qualViewNameMoveStmnts; " ORDER BY seqno"" | tb > setup.sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  rc=$?' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  assertDb2Rc ""${rc}"" ""setup.sh""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  stopBusy ""done. [rc=${rc}]""' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  chmod 755 setup.sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  ln -sf ./setup.sh genStatements.sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  ln -sf ./setup.sh exportDb.sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  ln -sf ./setup.sh importDb.sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  ln -sf ./setup.sh loadDb.sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  ln -sf ./setup.sh howto.sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  genStatements ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'###########' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'genStatements() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  dbConnect ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"

   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve export script export.1-prep.db2""   ""SELECT STMNT FROM "; qualViewNameMoveExp; " WHERE PHASE = 1 ORDER BY SEQUENCENO"" export.1-prep'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve export script export.3-seq.db2""    ""SELECT STMNT FROM "; qualViewNameMoveExp; " WHERE PHASE = 2 ORDER BY SEQUENCENO"" export.3-seq'    || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve export script export.4-org.db2""    ""SELECT STMNT FROM "; qualViewNameMoveExp; " WHERE PHASE = 3 ORDER BY SEQUENCENO"" export.4-org'    || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve export script export.5-data.db2""   ""SELECT STMNT FROM "; qualViewNameMoveExp; " WHERE PHASE = 4 ORDER BY SEQUENCENO"" export.5-data'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve export script export.6-finish.db2"" ""SELECT STMNT FROM "; qualViewNameMoveExp; " WHERE PHASE = 5 ORDER BY SEQUENCENO"" export.6-finish' || CHR(10) ||"

   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve import script import.1-prep.db2""   ""SELECT STMNT FROM "; qualViewNameMoveImp; " WHERE PHASE = 1 ORDER BY SEQUENCENO"" import.1-prep' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve import script import.2-base.db2""   ""SELECT STMNT FROM "; qualViewNameMoveImp; " WHERE PHASE = 2 ORDER BY SEQUENCENO"" import.2-base' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve import script import.3-part.db2""   ""SELECT STMNT FROM "; qualViewNameMoveImp; " WHERE PHASE = 3 ORDER BY SEQUENCENO"" import.3-part' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve import script import.4-data.db2""   ""SELECT STMNT FROM "; qualViewNameMoveImp; " WHERE PHASE = 4 ORDER BY SEQUENCENO"" import.4-data' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve import script import.7-finish.db2"" ""SELECT STMNT FROM "; qualViewNameMoveImp; " WHERE PHASE = 5 ORDER BY SEQUENCENO"" import.7-finish' || CHR(10) ||"

   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve load script load.1-prep.db2""       ""SELECT STMNT FROM "; qualViewNameMoveLoad; " WHERE PHASE = 1 ORDER BY SEQUENCENO"" load.1-prep'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve load script load.2-base.db2""       ""SELECT STMNT FROM "; qualViewNameMoveLoad; " WHERE PHASE = 2 ORDER BY SEQUENCENO"" load.2-base'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve load script load.3-part.db2""       ""SELECT STMNT FROM "; qualViewNameMoveLoad; " WHERE PHASE = 3 ORDER BY SEQUENCENO"" load.3-part'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve load script load.4-data.db2""       ""SELECT STMNT FROM "; qualViewNameMoveLoad; " WHERE PHASE = 4 ORDER BY SEQUENCENO"" load.4-data'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Cmd ""Retrieve load script load.7-finish.db2""     ""SELECT STMNT FROM "; qualViewNameMoveLoad; " WHERE PHASE = 5 ORDER BY SEQUENCENO"" load.7-finish' || CHR(10) ||"

   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  chmod 644 export.1-prep.db2 export.3-seq.db2 export.4-org.db2 export.5-data.db2 export.6-finish.db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  ls ./MDSDB-*Rel*-Prep.db2 2> /dev/null | xargs chmod 644' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  chmod 644 import.1-prep.db2 import.2-base.db2 import.3-part.db2 import.4-data.db2 import.7-finish.db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  chmod 644 load.1-prep.db2 load.2-base.db2 load.3-part.db2 load.4-data.db2 load.7-finish.db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  chmod 777 .' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'###########' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'exportDb() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  resetCodePage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  dbConnect ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Quiesce source database"" ""export.1-prep"" 0' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  prepFile=`ls ./MDSDB-*Rel*-Prep.db2 2> /dev/null | sort -r | head -1`' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  if [ -f ""$prepFile"" ]; then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    ln -sf ""$prepFile"" ""export.2-prep.db2""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    execDb2Script ""Update DB structure"" ""export.2-prep"" 1 """" """" ""@""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Export sequences"" ""export.3-seq"" 1' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  grep ALTER export.3-seq.${dbName}.log > import.5-seq.${dbName}.db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  grep ALTER export.3-seq.${dbName}.log > load.5-seq.${dbName}.db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  chmod 644 import.5-seq.${dbName}.db2 load.5-seq.${dbName}.db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Export Organization OIDs"" ""export.4-org"" 1' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  grep UPDATE export.4-org.${dbName}.log | grep -v SELECT > import.6-org.${dbName}.db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  grep UPDATE export.4-org.${dbName}.log | grep -v SELECT > load.6-org.${dbName}.db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  chmod 644 import.6-org.${dbName}.db2 load.6-org.${dbName}.db2' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  mkdir -p ""${dbName}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  cd ""${dbName}""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  chmod 755 .' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  setCodePage1208 ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Export data"" ""export.5-data"" 10 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  resetCodePage' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  ls | xargs chmod 644' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  cd ..' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Unquiesce source database"" ""export.6-finish"" 0' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'###########' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'importDb() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  dbConnect ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  cd ${srcDbName}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Prepare database for import"" ""import.1-prep""              1 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  setCodePage1208 ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Import base data""            ""import.2-base""             10 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  resetCodePage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Add table partitions""        ""import.3-part""             10 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  setCodePage1208 ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Import data""                 ""import.4-data""             10 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  resetCodePage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  checkForReject ""import.4-data.${dbName}.log"" ""Import"" ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Import sequence values""      ""import.5-seq.${srcDbName}""  1 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Import Organization OIDs""    ""import.6-org.${srcDbName}""  1 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Finalizing database""         ""import.7-finish""            1 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'###########' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'loadDb() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  dbConnect ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  cd ${srcDbName}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Prepare database for load"" ""load.1-prep""              1 ""../""'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  setCodePage1208 ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Load base data""            ""load.2-base""             10 ""../"" 2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  resetCodePage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  resolveBackupPending ""${dbName}"" ""${dbUser}"" ""${dbPw}"" ""Resolving ''Backup Pending'' state"" ""load.2-base"" 10 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Add table partitions""      ""load.3-part""             10 ""../""'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  setCodePage1208 ${dbName} ${dbUser} ${dbPw}' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Load data""                 ""load.4-data""             10 ""../"" 2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  resetCodePage' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  checkForReject ""load.4-data.${dbName}.log"" ""Load"" ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  resolveBackupPending ""${dbName}"" ""${dbUser}"" ""${dbPw}"" ""Resolving ''Backup Pending'' state"" ""load.4-data"" 10 ""../""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Import sequence values""    ""load.5-seq.${srcDbName}""  1 ""../""'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Import Organization OIDs""  ""load.6-org.${srcDbName}""  1 ""../""'   || CHR(10) ||"
   Print #fileNo, addTab(2); "'  execDb2Script ""Finalizing database""       ""load.7-finish""            1 ""../"" 3' || CHR(10) ||"
   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'###########' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10)"

   Exit Sub
 
 Sect3:

   Print #fileNo, addTab(2); "'howTo() {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  cat << __EOF' || CHR(10) ||"

   Print #fileNo, addTab(2); "'Steps to move data between MDS-databases:' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
 
   Print #fileNo, addTab(2); "'* Prerequisites:' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - Take a copy of a R1.1.1 database (BACKUP & RESTORE) - called ''source database'' below. This backup is' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    then being used as source for the R1.2 load.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    Note: During step 2 below this database copy will structurally be modified so that it is equivalent' || CHR(10) ||"
   Print #fileNo, addTab(2); "'          to release 1.2. Thus, this copy afterwards should not be used for any MDS-environment.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - The source database is at least release 1.1.1.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - The new database - called ''target database'' below - is a release 1.2 database. (This supports Range Partitioning.)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - Setup of a shared directory (e.g. /mig12/expImpDir) called <expImpDir> which is accessible from both database' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    servers, source and target.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - If data for ''extra'' MPCs is supposed to be imported from source to target database, the DDL for the extra MPCs must be' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    deployed in the target database before and MPCs ''Meta Data'' must initialized before step 1. This corresponds to chapters' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    ''DDL-Deployment for New MPC'' and ''Initialize MPC-Meta Data'' in document ''DR09 - Databases and Files''.' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'    In case that directory <expImpDir> is NOT a shared directory accessible from source as well as target database,' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    directory <expImpDir> needs to be re-copied to the other environment after each Step such that it can be' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    accessed from target database. In this case <expImpDir> in the description of of step 3 refers to the copy' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    of this directory in the target environment.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    Notes:' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      - When copying directory <expImpDir> make sure that file system permissions are NOT modified.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      - Make sure directory <expImpDir> provides sufficient disk space for export-data.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        (~ 80GB assuming 3 MPCs and standard product structures)' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
 
   Print #fileNo, addTab(2); "'* Step1: Create Shell- and DB2-scripts from target database:' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  As instance owner of the target database (or user with sufficient GRANTs) execute the following commands' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  which in a first step create a shell-script. In a second step this script is used to create further' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  scripts for actually exporting data from source database and loading into target database.' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'  - mkdir -p <expImpDir> # where <expImpDir> is an appropriate empty directory, user should own this directory' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - cd <expImpDir>' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - cp <...>/MDSDB-Rel1.2-Prep.db2 .' || CHR(10) ||"
   Print #fileNo, addTab(2); "'    # where <...>/MDSDB-Rel1.2-Prep.db2 is the ''src DB update script'' from the ddlfix/releaseupdate/rel1.2 folder' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - db2 ""CONNECT TO <targetDbName>""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - db2 -x ""SELECT stmnt FROM "; qualViewNameMoveStmnts; " ORDER BY seqno"" | sed ''s/[ ]*$//'' > setupTmp.sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - chmod 755 setupTmp.sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - ./setupTmp.sh -d <targetDbName>' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
 
   Print #fileNo, addTab(2); "'* Step 2: Export data from source database:' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  As instance owner of the source database (or user with sufficient GRANTs) execute the following commands' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  which create a subdirectory <sourceDbName> and export data from source database into this directory.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - cd <expImpDir>' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - nohup ./exportDb.sh -d <sourceDbName> > exportDb.log&' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - tail -f exportDb.log' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
 
   Print #fileNo, addTab(2); "'* Step 3:' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  As instance owner of the target database (or user with sufficient GRANTs) execute the following commands' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  which load the exported data into the target database.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - cd <expImpDir>' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - nohup ./loadDb.sh -d <targetDbName> -s <sourceDbName> > loadDb.log&' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  - tail -f loadDb.log' || CHR(10) ||"

   Print #fileNo, addTab(2); "'__EOF' || CHR(10) ||"

   Print #fileNo, addTab(2); "'}' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'##########################################################################################' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# select routine to call' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'silent=1' || CHR(10) ||"
 '  Print #fileNo, addTab(2); "'[ -t 1 ] || silent=1' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'case $cmd in' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  genStat*) silent=1' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            genStatements' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  export*)  exportDb' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  import*)  importDb' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  load*)    loadDb' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  how*)     howTo' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  *)        silent=1' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            setup' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'esac'"
 End Sub
 
 
 Sub genDbAdminDdl0( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     ' we do not support this for LDM
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    SP for querying 'DB2 Level'
   ' ####################################################################################################################

   Dim qualProcedureNameGetDb2Level As String
   qualProcedureNameGetDb2Level = genQualProcName(g_sectionIndexDbAdmin, spnGetDb2Level, ddlType)
   printSectionHeader "SP for querying 'DB2 Level'", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameGetDb2Level
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "OUT", "major_out", "INTEGER", True, "major DB2 release level"
   genProcParm fileNo, "OUT", "minor_out", "INTEGER", True, "minor DB2 release level"
   genProcParm fileNo, "OUT", "level_out", "INTEGER", True, "minor DB2 version level"
   genProcParm fileNo, "OUT", "fp_out", "INTEGER", False, "DB2 fixpack number"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_levelStr", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_elem", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_strPos", "INTEGER", "NULL"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET major_out = NULL;"
   Print #fileNo, addTab(1); "SET minor_out = NULL;"
   Print #fileNo, addTab(1); "SET level_out = NULL;"
   Print #fileNo, addTab(1); "SET fp_out    = NULL;"

   genProcSectionHeader fileNo, "determine DB2-level as string"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "UPPER(SERVICE_LEVEL),"
   Print #fileNo, addTab(2); "FIXPACK_NUM"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_levelStr,"
   Print #fileNo, addTab(2); "fp_out"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "TABLE(SYSPROC.ENV_GET_INST_INFO()) AS INSTANCEINFO"
   Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"

   genProcSectionHeader fileNo, "parse DB2-level"
   Print #fileNo, addTab(1); "FOR elemLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "POSINDEX           AS c_posIndex,"
   Print #fileNo, addTab(3); "RTRIM(LTRIM(ELEM)) AS c_elem"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "TABLE ( "; g_qualFuncNameStrElems; "(v_levelStr, CAST('.' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "POSINDEX ASC"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "strip off non-numeric characters", 2, True
   Print #fileNo, addTab(2); "SET v_elem = c_elem;"
   Print #fileNo, addTab(2); "SET v_strPos = POSSTR(c_elem, ' ');"
   Print #fileNo, addTab(2); "IF v_strPos > 0 THEN"
   Print #fileNo, addTab(3); "SET v_elem = REPLACE(TRANSLATE(RIGHT(v_elem, LENGTH(v_elem) - v_strPos), '                          ', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), ' ', '');"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "retrieve result values", 2
   Print #fileNo, addTab(2); "    IF (c_posIndex = 0) THEN SET major_out = INTEGER(v_elem);"
   Print #fileNo, addTab(2); "ELSEIF (c_posIndex = 1) THEN SET minor_out = INTEGER(v_elem);"
   Print #fileNo, addTab(2); "ELSEIF (c_posIndex = 2) THEN SET level_out = INTEGER(v_elem);"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF retrieving 'DB2 Release'
   ' ####################################################################################################################

   printSectionHeader "Function retrieving 'DB2 (minor) Release'", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); g_qualFuncNameDb2Release
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtDbRelease
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_major", "INTEGER", "NULL"
   genVarDecl fileNo, "v_minor", "INTEGER", "NULL"
   genVarDecl fileNo, "v_level", "INTEGER", "NULL"
   genVarDecl fileNo, "v_fp", "INTEGER", "NULL"

   genProcSectionHeader fileNo, "determine DB2-level"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameGetDb2Level; "(v_major, v_minor, v_level, v_fp);"
 
   genProcSectionHeader fileNo, "return result"
   Print #fileNo, addTab(1); "RETURN v_major + (DECIMAL(v_minor)/100) + (DECIMAL(v_level)/100000);"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for creating Aliases for LRT-tables in 'private-only Alias Schema'
   ' ####################################################################################################################

   Dim qualProcedureNameCreateLrtAliases As String
   qualProcedureNameCreateLrtAliases = genQualProcName(g_sectionIndexDbAdmin, spnCreateLrtAliases, ddlType)
   printSectionHeader "SP for creating Aliases for LRT-tables in 'private-only Alias Schema'", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCreateLrtAliases
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "organizationId_in", g_dbtEnumId, True, "(optional) identifies the organization to create Aliases for"
   genProcParm fileNo, "IN", "accessMode_in", g_dbtEnumId, True, "(optional) identifies the access mode of the data pool to create Aliases for"
   genProcParm fileNo, "OUT", "aliasCount_out", "INTEGER", True, "number of Aliases created"
   genProcParm fileNo, "OUT", "viewCount_out", "INTEGER", False, "number of Views created"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "objectNotExists", "42704"
   genCondDecl fileNo, "aliasIsView", "42809"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(1000)", "NULL"
   genVarDecl fileNo, "v_colList", "VARCHAR(500)", "''"
   genVarDecl fileNo, "v_colValList", "VARCHAR(500)", "''"
   genVarDecl fileNo, "v_seqNo", "INTEGER", "1"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR objectNotExists"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR aliasIsView"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempStatement fileNo, 1, True, 1000

   genSpLogProcEnter fileNo, qualProcedureNameCreateLrtAliases, ddlType, , "mode_in", "organizationId_in", "accessMode_in", "aliasCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET aliasCount_out = 0;"
   Print #fileNo, addTab(1); "SET viewCount_out = 0;"
 
   genProcSectionHeader fileNo, "loop over matching LRT-tables and create Alias for 'private only'"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
 
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "PPRIV."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "S.PRIVATESCHEMANAME AS c_privSchemaName,"
   Print #fileNo, addTab(3); "PPRIV."; g_anPdmTableName; " AS c_privTableName,"
   Print #fileNo, addTab(3); "PPUB."; g_anPdmTableName; " AS c_pubTableName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LPRIV"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LPRIV."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = LPRIV."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = LPRIV."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(LPRIV."; g_anLdmIsLrt; " = 1 OR A."; g_anAcmIsLrtMeta; " = 1)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPRIV."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LPUB"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LPUB."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = LPUB."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = LPUB."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPUB."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPUB."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PPRIV"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PPRIV."; g_anPdmLdmFkSchemaName; " = LPRIV."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPRIV."; g_anPdmLdmFkTableName; " = LPRIV."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PPUB"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PPUB."; g_anPdmLdmFkSchemaName; " = LPUB."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPUB."; g_anPdmLdmFkTableName; " = LPUB."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmPrimarySchema; " S"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "S."; g_anOrganizationId; " = PPUB."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "S."; g_anPoolTypeId; " = PPUB."; g_anPoolTypeId

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "S.PRIVATESCHEMANAME IS NOT NULL"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPRIV."; g_anLdmIsNl; " = LPUB."; g_anLdmIsNl
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPRIV."; g_anLdmIsGen; " = LPUB."; g_anLdmIsGen
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPRIV."; g_anOrganizationId; " = PPUB."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPRIV."; g_anPoolTypeId; " = PPUB."; g_anPoolTypeId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(organizationId_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(PPUB."; g_anOrganizationId; ", organizationId_in) = organizationId_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(accessMode_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(PPUB."; g_anPoolTypeId; ", accessMode_in) = accessMode_in)"
   Print #fileNo, addTab(3); ")"
 
   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader fileNo, "drop Alias / View - if exists", 3, True
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP ALIAS ' || c_privSchemaName || '.' || c_pubTableName;"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP VIEW ' || c_privSchemaName || '.' || c_pubTableName;"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "create Alias", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CREATE ALIAS ' || c_privSchemaName || '.' || c_pubTableName || ' FOR ' || c_schemaName || '.' || c_privTableName;"
   Print #fileNo, addTab(2); "SET aliasCount_out = aliasCount_out + 1;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatement
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "aliasCount_out,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "loop over matching LRT-tables and create Alias / View for 'public only'"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
 
   Print #fileNo, addTab(2); "SELECT"
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "A."; g_anAcmCondenseData; " AS c_condenseData,"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(3); "(CASE WHEN A."; g_anAcmEntityType; " = 'C' AND A."; g_anAcmEntityId; " = A."; g_anAhCid; " THEN 1 ELSE 0 END) AS c_isAggHead,"
   Print #fileNo, addTab(3); "PPUB."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "S.PUBLICSCHEMANAME AS c_pubSchemaName,"
   Print #fileNo, addTab(3); "PPUB."; g_anPdmTableName; " AS c_pubTableName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LPUB"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = LPUB."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = LPUB."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = LPUB."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPUB."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPUB."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PPUB"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PPUB."; g_anPdmLdmFkSchemaName; " = LPUB."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPUB."; g_anPdmLdmFkTableName; " = LPUB."; g_anLdmTableName
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmPrimarySchema; " S"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "PPUB."; g_anOrganizationId; " IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S."; g_anOrganizationId; " = PPUB."; g_anOrganizationId; ")"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "PPUB."; g_anPoolTypeId; " IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(S."; g_anPoolTypeId; " = PPUB."; g_anPoolTypeId; ")"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(organizationId_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(S."; g_anOrganizationId; ", organizationId_in) = organizationId_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(accessMode_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(COALESCE(S."; g_anPoolTypeId; ", accessMode_in) = accessMode_in)"
   Print #fileNo, addTab(3); ")"
 
   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader fileNo, "drop Alias / View - if exists", 3, True
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP ALIAS ' || c_pubSchemaName || '.' || c_pubTableName;"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP VIEW ' || c_pubSchemaName || '.' || c_pubTableName;"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"

 ' ### IF IVK ###
   Print #fileNo,
   Print #fileNo, addTab(2); "IF (c_condenseData = 1) AND (c_isAggHead = 1) THEN"

   Print #fileNo, addTab(3); "SET v_colList    = '';"
   Print #fileNo, addTab(3); "SET v_colValList = '';"

   genProcSectionHeader fileNo, "loop over columns to build column lists", 3
   Print #fileNo, addTab(3); "FOR colLoop AS"
 
   Print #fileNo, addTab(4); "WITH"
   Print #fileNo, addTab(5); "V_MandatoryColumns"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "colName,"
   Print #fileNo, addTab(5); "defaultValue"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "AS"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "VALUES('"; g_anInLrt; "', 'CAST(NULL AS "; g_dbtOid; ")')"
   Print #fileNo, addTab(5); "UNION ALL"
   Print #fileNo, addTab(5); "VALUES('"; g_anAhOid; "', 'CAST(NULL AS "; g_dbtOid; ")')"
   Print #fileNo, addTab(4); "),"
   Print #fileNo, addTab(5); "V_ExistingColumns"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "colName,"
   Print #fileNo, addTab(5); "colNo"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "AS"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "SELECT"
   Print #fileNo, addTab(6); "COLNAME,"
   Print #fileNo, addTab(6); "COLNO"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); "SYSCAT.COLUMNS"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "TABSCHEMA = c_schemaName"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "TABNAME = c_pubTableName"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "COALESCE(E.colNo, -1) AS c_colNo,"
   Print #fileNo, addTab(5); "COALESCE(E.colName, M.colName) AS c_colName,"
   Print #fileNo, addTab(5); "M.defaultValue"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "V_ExistingColumns E"
   Print #fileNo, addTab(4); "FULL OUTER JOIN"
   Print #fileNo, addTab(5); "V_MandatoryColumns M"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "E.colName = M.colName"
   Print #fileNo, addTab(4); "ORDER BY COALESCE(E.colNo, -1)"
 
   Print #fileNo, addTab(3); "DO"
 
   Print #fileNo, addTab(4); "SET v_colList = v_colList || (CASE WHEN v_colList = '' THEN '' ELSE ',' END) || c_colName;"
   Print #fileNo, addTab(4); "SET v_colValList = v_colValList || (CASE WHEN v_colValList = '' THEN '' ELSE ',' END) || (CASE WHEN c_colNo < 0 THEN defaultValue ELSE c_colName END);"
 
   Print #fileNo, addTab(3); "END FOR;"

   genProcSectionHeader fileNo, "create View (to include 'missing column(s))", 3
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CREATE VIEW ' || c_pubSchemaName || '.' || c_pubTableName || ' (' || v_colList || ') AS SELECT ' || v_colValList || ' FROM ' || c_schemaName || '.' || c_pubTableName;"
   Print #fileNo, addTab(3); "SET viewCount_out = viewCount_out + 1;"
 
   Print #fileNo, addTab(2); "ELSE"

   genProcSectionHeader fileNo, "create Alias", 3, True
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CREATE ALIAS ' || c_pubSchemaName || '.' || c_pubTableName || ' FOR ' || c_schemaName || '.' || c_pubTableName;"
   Print #fileNo, addTab(3); "SET aliasCount_out = aliasCount_out + 1;"

   Print #fileNo, addTab(2); "END IF;"
 ' ### ELSE IVK ###
 ' genProcSectionHeader fileNo, "create Alias", 3, True
 ' Print #fileNo, addTab(2); "SET v_stmntTxt = 'CREATE ALIAS ' || c_pubSchemaName || '.' || c_pubTableName || ' FOR ' || c_schemaName || '.' || c_pubTableName;"
 ' Print #fileNo, addTab(2); "SET aliasCount_out = aliasCount_out + 1;"
 ' ### ENDIF IVK ###

   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatement
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "aliasCount_out,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit fileNo, qualProcedureNameCreateLrtAliases, ddlType, , "mode_in", "organizationId_in", "accessMode_in", "aliasCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Sub genDbAdminDdl1( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
 ' ### IF IVK ###
     ' we do not support most of this for LDM
     GoTo LdmEntryPoint
 ' ### ELSE IVK ###
 '   ' we do not support this for LDM
 '   Exit Sub
 ' ### ENDIF IVK ###
   End If

 ' ### IF IVK ###
   Dim unqualTabNameApplVersion As String
   unqualTabNameApplVersion = getUnqualObjName(g_qualTabNameApplVersion)
 
   Dim unqualTabNameApplHistory As String
   unqualTabNameApplHistory = getUnqualObjName(g_qualTabNameApplHistory)

 ' ### ENDIF IVK ###
   Dim qualProcedureNameTrigDisable As String
   qualProcedureNameTrigDisable = genQualProcName(g_sectionIndexDbAdmin, spnTriggerDisable, ddlType)
   Dim qualProcedureNameTrigEnable As String
   qualProcedureNameTrigEnable = genQualProcName(g_sectionIndexDbAdmin, spnTriggerEnable, ddlType)

 ' ### IF IVK ###
   ' ####################################################################################################################
   ' #    View defining the ACM 'core class-IDs'
   ' ####################################################################################################################

   Dim qualViewNameCore As String
   qualViewNameCore = genQualViewName(g_sectionIndexDbAdmin, vnAcmCoreEntityId, vsnAcmCoreEntityId, ddlType)
 
   printSectionHeader "View defining the ACM 'core class-IDs'", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameCore
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anAcmEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("

   Dim firstClass As Boolean
   firstClass = True
   Dim typeKey As String
   typeKey = gc_acmEntityTypeKeyClass

   Dim i As Integer
   For i = 1 To g_classes.numDescriptors
       If g_classes.descriptors(i).isCore Then
         If Not firstClass Then
           Print #fileNo, addTab(2); "UNION"
         End If
         firstClass = False
         Print #fileNo, addTab(1); "VALUES('"; g_classes.descriptors(i).classIdStr; "','"; typeKey; "')"
       End If
   Next i

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 ' ### ENDIF IVK ###
   Dim qualTabName As String
   Dim unqualTabName As String
   Dim schemaName As String

   ' ####################################################################################################################
   ' #    View for 'PDM tables involved in IMPORT / EXPORT'
   ' ####################################################################################################################

   Dim qualViewNameExpImpTab As String
   qualViewNameExpImpTab = genQualViewName(g_sectionIndexDbAdmin, vnPdmExportTabList, vnsPdmExportTabList, ddlType)

   printSectionHeader "View for 'PDM tables involved in IMPORT / EXPORT'", fileNo
   Print #fileNo,

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameExpImpTab
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(1); g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(1); g_anPdmTypedTableName; ","
   Print #fileNo, addTab(1); g_anAcmEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); g_anOrganizationId; ","
   Print #fileNo, addTab(1); g_anPoolTypeId; ","
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); g_anLdmIsLrt; ","
   Print #fileNo, addTab(1); g_anLdmIsMqt; ","
   Print #fileNo, addTab(1); g_anAcmIsCto; ","
   Print #fileNo, addTab(1); g_anAcmIsCtp; ","
   Print #fileNo, addTab(1); g_anAcmIsRangePartAll; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(1); g_anAcmIsPs; ","
   Print #fileNo, addTab(1); "ISORGIDTAGGED,"
   Print #fileNo, addTab(1); g_anAcmIsPsForming
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(1); "ISORGIDTAGGED"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "WITH"
   Print #fileNo, addTab(1); "V_SysTables"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "RTRIM(TABSCHEMA) AS tabSchema,"
   Print #fileNo, addTab(2); "RTRIM(TABNAME) AS tabName,"
   Print #fileNo, addTab(2); "type"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.TABLES"
   Print #fileNo, addTab(0); "),"

   Print #fileNo, addTab(1); "V_ExtraTables"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(2); "tabSchema,"
   Print #fileNo, addTab(2); "tabName,"
   Print #fileNo, addTab(2); "isOrgIdTagged,"
   Print #fileNo, addTab(2); "seqNo"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("

   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors
   Dim firstRow As Boolean
   firstRow = True
   Dim isOrgIdTagged As Boolean
 ' ### IF IVK ###
   For i = 1 To g_classes.numDescriptors
       If g_classes.descriptors(i).includeInPdmExportSeqNo >= 0 Then
         isOrgIdTagged = False
         qualTabName = genQualTabNameByClassIndex(g_classes.descriptors(i).classIndex, ddlType)
         unqualTabName = getUnqualObjName(qualTabName)
         schemaName = getSchemaName(qualTabName)

         If g_classes.descriptors(i).isCommonToOrgs Then
           tabColumns = nullEntityColumnDescriptors
           initAttributeTransformation transformation, 0, , True
           genTransformedAttrListForEntityWithColReuse g_classes.descriptors(i).classIndex, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

           Dim k As Integer
           For k = 1 To tabColumns.numDescriptors
               If tabColumns.descriptors(k).columnName = g_anOrganizationId Then
                 isOrgIdTagged = True
               End If
           Next k
         End If

         If firstRow Then
           firstRow = False
         Else
           Print #fileNo, addTab(1); "UNION ALL"
         End If

         Print #fileNo, addTab(1); "SELECT T.TABSCHEMA, T.TABNAME, "; g_dbtBoolean; "("; IIf(isOrgIdTagged, gc_dbTrue, gc_dbFalse); "), "; CStr(g_classes.descriptors(i).includeInPdmExportSeqNo); " FROM V_SysTables T WHERE";

         Print #fileNo, " T.TABSCHEMA LIKE '"; UCase(schemaName); "%'";
         Print #fileNo, " AND";
         Print #fileNo, " T.TABNAME = '"; UCase(unqualTabName); "'";
         Print #fileNo, " AND";
         Print #fileNo, " T.TYPE = 'T'"
       End If
   Next i

   For i = 1 To g_relationships.numDescriptors
       If g_relationships.descriptors(i).includeInPdmExportSeqNo >= 0 And Not (g_relationships.descriptors(i).isMdsExpressionRel) Then
         If firstRow Then
           firstRow = False
         Else
           Print #fileNo, addTab(1); "UNION ALL"
         End If

         Print #fileNo, addTab(1); "SELECT T.TABSCHEMA, T.TABNAME, "; g_dbtBoolean; "(0), "; CStr(g_relationships.descriptors(i).includeInPdmExportSeqNo); " FROM V_SysTables T WHERE";

         qualTabName = genQualTabNameByRelIndex(g_relationships.descriptors(i).relIndex, ddlType)
         unqualTabName = getUnqualObjName(qualTabName)
         schemaName = getSchemaName(qualTabName)

         Print #fileNo, " T.TABSCHEMA LIKE '"; UCase(schemaName); "%'";
         Print #fileNo, " AND";
         Print #fileNo, " T.TABNAME = '"; UCase(unqualTabName); "'";
         Print #fileNo, " AND";
         Print #fileNo, " T.TYPE = 'T'"
       End If
   Next i

 ' ### ENDIF IVK ###
   If firstRow Then
     ' no extra tables defined
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "CAST(NULL AS VARCHAR(1)),"
     Print #fileNo, addTab(2); "CAST(NULL AS VARCHAR(1)),"
     Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtBoolean; "),"
     Print #fileNo, addTab(2); "CAST(NULL AS INTEGER)"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "SYSIBM.SYSDUMMY1"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "(0 = 1)"
   End If

   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "L."; g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(2); "P."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(2); "P."; g_anPdmTableName; ","
   Print #fileNo, addTab(2); "A."; g_anAcmEntityId; ","
   Print #fileNo, addTab(2); "A."; g_anAcmEntityType; ","
   Print #fileNo, addTab(2); "P."; g_anOrganizationId; ","
   Print #fileNo, addTab(2); "P."; g_anPoolTypeId; ","
   Print #fileNo, addTab(2); "L."; g_anLdmIsNl; ","
   Print #fileNo, addTab(2); "L."; g_anLdmIsGen; ","
   Print #fileNo, addTab(2); "L."; g_anLdmIsLrt; ","
   Print #fileNo, addTab(2); "L."; g_anLdmIsMqt; ","
   Print #fileNo, addTab(2); "A."; g_anAcmIsCto; ","
   Print #fileNo, addTab(2); "A."; g_anAcmIsCtp; ","
   Print #fileNo, addTab(2); "A."; g_anAcmIsRangePartAll; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "A."; g_anAcmIsPs; ","
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "A."; g_anAcmIsPsForming
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; ")"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameLdmTable; " L,"
   Print #fileNo, addTab(2); g_qualTabNamePdmTable; " P,"
   Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType

   Print #fileNo, addTab(1); "UNION ALL"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "T.tabSchema,"
   Print #fileNo, addTab(2); "T.tabName,"
   Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtEntityId; "),"
   Print #fileNo, addTab(2); "'M',"
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; ")"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(3); "V_SysTables T"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "T.tabSchema LIKE '"; genSchemaName("%", "%", ddlType, g_primaryOrgIndex, g_migDataPoolIndex); "'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "T.type = 'T'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NOT EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " = T.TABNAME"
   Print #fileNo, addTab(2); ")"

   Print #fileNo, addTab(1); "UNION ALL"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "T.seqNo,"
   Print #fileNo, addTab(2); "T.tabSchema,"
   Print #fileNo, addTab(2); "T.tabName,"
   Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtEntityId; "),"
   Print #fileNo, addTab(2); "'M',"
   Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtEnumId; "),"
   Print #fileNo, addTab(2); "CAST(NULL AS "; g_dbtEnumId; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; "),"
   Print #fileNo, addTab(2); "T.isOrgIdTagged,"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "CAST(0 AS "; g_dbtBoolean; ")"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ExtraTables T"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "T.tabSchema NOT LIKE '"; genSchemaName("%", "%", ddlType, g_primaryOrgIndex, g_migDataPoolIndex); "'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NOT EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " = T.tabName"
   Print #fileNo, addTab(2); ")"

   Print #fileNo, addTab(0); ")"
   Print #fileNo, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    View for 'EXPORT statements for PDM tables'
   ' ####################################################################################################################

   Dim qualViewNameExp As String
   qualViewNameExp = genQualViewName(g_sectionIndexDbAdmin, vnPdmExportStmnt, vnsPdmExportStmnt, ddlType)

   printSectionHeader "View for 'EXPORT statements for PDM tables'", fileNo
   Print #fileNo,

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameExp
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(1); g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(1); g_anPdmTypedTableName; ","
   Print #fileNo, addTab(1); g_anAcmEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); g_anOrganizationId; ","
   Print #fileNo, addTab(1); g_anPoolTypeId; ","
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); g_anLdmIsLrt; ","
   Print #fileNo, addTab(1); g_anLdmIsMqt; ","
   Print #fileNo, addTab(1); g_anAcmIsCto; ","
   Print #fileNo, addTab(1); g_anAcmIsCtp; ","
   Print #fileNo, addTab(1); g_anAcmIsRangePartAll; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(1); g_anAcmIsPs; ","
   Print #fileNo, addTab(1); g_anAcmIsPsForming; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "STMNT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "WITH"
   Print #fileNo, addTab(1); "V_ColSeq"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "level,"
   Print #fileNo, addTab(1); "colSeq,"
   Print #fileNo, addTab(1); "tabschema,"
   Print #fileNo, addTab(1); "tabname"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "CAST(C.COLNAME AS VARCHAR(8000)),"
   Print #fileNo, addTab(2); "C.TABSCHEMA,"
   Print #fileNo, addTab(2); "C.TABNAME"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.COLUMNS C,"
   Print #fileNo, addTab(2); "SYSCAT.TABLES T"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "C.COLNO = 0"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "C.TABSCHEMA = T.TABSCHEMA"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "C.TABNAME = T.TABNAME"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "T.TYPE = 'T'"
   Print #fileNo, addTab(1); "UNION ALL"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "S.LEVEL + 1,"
   Print #fileNo, addTab(2); "CAST(S.colSeq || ',' || C.COLNAME AS VARCHAR(8000)),"
   Print #fileNo, addTab(2); "C.TABSCHEMA,"
   Print #fileNo, addTab(2); "C.TABNAME"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ColSeq S,"
   Print #fileNo, addTab(2); "SYSCAT.COLUMNS C"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "S.tabSchema = C.TABSCHEMA"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.tabName = C.TABNAME"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "C.COLNO = S.level + 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.level < 5000"
   Print #fileNo, addTab(0); "),"
   Print #fileNo, addTab(1); " V_ColSeqMaxLevel"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "level,"
   Print #fileNo, addTab(1); "tabschema,"
   Print #fileNo, addTab(1); "tabname"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "MAX(LEVEL),"
   Print #fileNo, addTab(2); "TABSCHEMA,"
   Print #fileNo, addTab(2); "TABNAME"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ColSeq"
   Print #fileNo, addTab(1); "GROUP BY"
   Print #fileNo, addTab(2); "TABSCHEMA,"
   Print #fileNo, addTab(2); "TABNAME"
   Print #fileNo, addTab(0); "),"
   Print #fileNo, addTab(1); "V_ColSeqMax"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "colSeq,"
   Print #fileNo, addTab(1); "tabschema,"
   Print #fileNo, addTab(1); "tabname"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "colSeq,"
   Print #fileNo, addTab(2); "V_ColSeq.tabSchema,"
   Print #fileNo, addTab(2); "V_ColSeq.tabName"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ColSeq,"
   Print #fileNo, addTab(2); "V_ColSeqMaxLevel"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "V_ColSeq.level = V_ColSeqMaxLevel.level"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "V_ColSeq.tabSchema = V_ColSeqMaxLevel.tabSchema"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "V_ColSeq.tabName = V_ColSeqMaxLevel.tabName"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "("

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "T."; g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(2); "T."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(2); "T."; g_anPdmTypedTableName; ","
   Print #fileNo, addTab(2); "T."; g_anAcmEntityId; ","
   Print #fileNo, addTab(2); "T."; g_anAcmEntityType; ","
   Print #fileNo, addTab(2); "T."; g_anOrganizationId; ","
   Print #fileNo, addTab(2); "T."; g_anPoolTypeId; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsNl; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsGen; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsLrt; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsMqt; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsCto; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsCtp; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsRangePartAll; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "T."; g_anAcmIsPs; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsPsForming; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "CAST("
   Print #fileNo, addTab(3); "'EXPORT TO ' ||"
   Print #fileNo, addTab(3); "T."; g_anPdmFkSchemaName; " || '.' || T."; g_anPdmTypedTableName; " ||"
   Print #fileNo, addTab(3); "'.ixf OF IXF LOBFILE ' || T."; g_anPdmFkSchemaName; " || '.' || T."; g_anPdmTypedTableName; " ||"
   Print #fileNo, addTab(3); "' MODIFIED BY LOBSINFILE SELECT ' || COALESCE(C.colSeq, '*') || ' FROM ' ||"
   Print #fileNo, addTab(3); "T."; g_anPdmFkSchemaName; " || '.' || T."; g_anPdmTypedTableName; " ||"
   Print #fileNo, addTab(3); "(CASE T.ISORGIDTAGGED WHEN 1 THEN ' WHERE ("; g_anOrganizationId; " < "; CStr(getMinOrgId()); " OR "; g_anOrganizationId; " > "; CStr(getMaxOrgId()); ")' ELSE '' END)"
   Print #fileNo, addTab(3); "AS VARCHAR(4000)"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameExpImpTab; " T"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); "V_ColSeqMax C"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "T."; g_anPdmFkSchemaName; " = C.tabSchema"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "T."; g_anPdmTypedTableName; " = C.tabName"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    View for 'EXPORT statements for PDM tables for DB-Move'
   ' ####################################################################################################################
 
   Dim qualViewNameMoveExp As String
   qualViewNameMoveExp = genQualViewName(g_sectionIndexDbAdmin, vnPdmExportStmntMove, vnsPdmExportStmntMove, ddlType)

   Dim qualViewNameMoveImp As String
   qualViewNameMoveImp = genQualViewName(g_sectionIndexDbAdmin, vnPdmImportStmntMove, vnsPdmImportStmntMove, ddlType)

   Dim qualViewNameMoveLoad As String
   qualViewNameMoveLoad = genQualViewName(g_sectionIndexDbAdmin, vnPdmLoadStmntMove, vnsPdmLoadStmntMove, ddlType)

   printSectionHeader "View for 'EXPORT statements for PDM tables for DB-Move'", fileNo
   Print #fileNo,

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameMoveExp
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SEQUENCENO,"
   Print #fileNo, addTab(1); "PHASE,"
   Print #fileNo, addTab(1); "STMNT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "WITH"
   Print #fileNo, addTab(1); "V_ExportBase"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY "; g_anLdmFkSequenceNo; " ASC, "; g_anOrganizationId; " ASC, "; g_anPoolTypeId; " ASC),"
   Print #fileNo, addTab(2); "STMNT"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameExp
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anAcmEntityType; " IN ('M', '"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE("; g_anPoolTypeId; ", "; CStr(g_workDataPoolId); ") IN ("; CStr(g_workDataPoolId); ", "; CStr(g_productiveDataPoolId); ", "; CStr(g_archiveDataPoolId); ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anPdmTypedTableName; " NOT IN ('"; unqualTabNameApplVersion; "', '"; unqualTabNameApplHistory; "')"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(0); "),"

   Print #fileNo, addTab(1); "V_ExportMisc"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "phase,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("

 ' ### IF IVK ###
   Const PhaseExportQuiesce = 1
   Const PhaseExportSequence = 2
   Const PhaseExportOrgOid = 3
   Const PhaseExportData = 4
   Const PhaseExportUnQuiesce = 5

   genProcSectionHeader fileNo, "export OIDs of Organization", 1, True
   Print #fileNo, addTab(1); "VALUES"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "1,"

   Print #fileNo, addTab(2); CStr(PhaseExportOrgOid); ","
   Print #fileNo, addTab(2); "'SELECT ''UPDATE "; g_qualTabNamePdmOrganization; " SET ORGOID = '' || RTRIM(CHAR(ORGOID)) || '' WHERE ID = '' || RTRIM(CHAR(ID)) ' ||"
   Print #fileNo, addTab(2); "'FROM ' ||"
   Print #fileNo, addTab(3); "'"; g_qualTabNamePdmOrganization; "'"
   Print #fileNo, addTab(1); ")"

   Print #fileNo, addTab(1); "UNION ALL"
 ' ### ELSE IVK ###
 ' Const PhaseExportQuiesce = 1
 ' Const PhaseExportSequence = 2
 ' Const PhaseExportData = 3
 ' Const PhaseExportUnQuiesce = 4
 ' ### ENDIF IVK ###

   genProcSectionHeader fileNo, "export values of sequences", 1, True
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "1,"
   Print #fileNo, addTab(2); CStr(PhaseExportSequence); ","
   Print #fileNo, addTab(2); "'VALUES(''ALTER SEQUENCE ' || RTRIM(SEQSCHEMA) || '.' || SEQNAME || ' RESTART WITH '' || RTRIM(CHAR(NEXTVAL FOR ' || RTRIM(SEQSCHEMA) || '.' || SEQNAME || ')))'"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.SEQUENCES"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "SEQSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SEQSCHEMA NOT LIKE '"; genSchemaName("%", "%", ddlType, g_primaryOrgIndex, g_migDataPoolIndex); "'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SEQSCHEMA NOT LIKE '"; g_schemaNameCtoDbMonitor; "%'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SEQNAME NOT LIKE 'RUNNINGNMB%'"

   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "quiesce DB", 1, True
   Print #fileNo, addTab(1); "VALUES (1, "; CStr(PhaseExportQuiesce); ", 'QUIESCE DB IMMEDIATE FORCE CONNECTIONS')"
 
   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "make sure that Snapshot-Tables are available", 1, True
   Dim qualProcNameReCreateSnapshots As String
   qualProcNameReCreateSnapshots = genQualProcName(g_sectionIndexDbMonitor, spnReCreateSnapshotTables, ddlType)
   Print #fileNo, addTab(1); "VALUES (2, "; CStr(PhaseExportQuiesce); ", 'CALL "; qualProcNameReCreateSnapshots; "(2, ?)')"
 
   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "unquiesce DB", 1, True
   Print #fileNo, addTab(1); "VALUES (1, "; CStr(PhaseExportUnQuiesce); ", 'UNQUIESCE DB')"
 
   Print #fileNo, addTab(0); "),"

   Print #fileNo, addTab(1); "V_StmntAll"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "phase,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "sequenceNo,"
   Print #fileNo, addTab(2); CStr(PhaseExportData); ","
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ExportBase"

   Print #fileNo, addTab(1); "UNION ALL"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "sequenceNo,"
   Print #fileNo, addTab(2); "phase,"
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ExportMisc"
   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY phase ASC, sequenceNo ASC),"
   Print #fileNo, addTab(2); "phase,"
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_StmntAll"
   Print #fileNo, addTab(0); ")"

   Print #fileNo, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    View for 'IMPORT statements for PDM tables'
   ' ####################################################################################################################

   Dim qualViewNameImp As String
   qualViewNameImp = genQualViewName(g_sectionIndexDbAdmin, vnPdmImportStmnt, vnsPdmImportStmnt, ddlType)

   printSectionHeader "View for 'IMPORT statements for PDM tables'", fileNo
   Print #fileNo,

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameImp
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(1); g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(1); g_anPdmTypedTableName; ","
   Print #fileNo, addTab(1); g_anAcmEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); g_anOrganizationId; ","
   Print #fileNo, addTab(1); g_anPoolTypeId; ","
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); g_anLdmIsLrt; ","
   Print #fileNo, addTab(1); g_anLdmIsMqt; ","
   Print #fileNo, addTab(1); g_anAcmIsCto; ","
   Print #fileNo, addTab(1); g_anAcmIsCtp; ","
   Print #fileNo, addTab(1); g_anAcmIsRangePartAll; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(1); g_anAcmIsPs; ","
   Print #fileNo, addTab(1); g_anAcmIsPsForming; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "STMNT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "T."; g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(2); "T."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(2); "T."; g_anPdmTypedTableName; ","
   Print #fileNo, addTab(2); "T."; g_anAcmEntityId; ","
   Print #fileNo, addTab(2); "T."; g_anAcmEntityType; ","
   Print #fileNo, addTab(2); "T."; g_anOrganizationId; ","
   Print #fileNo, addTab(2); "T."; g_anPoolTypeId; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsNl; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsGen; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsLrt; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsMqt; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsCto; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsCtp; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsRangePartAll; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "T."; g_anAcmIsPs; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsPsForming; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "CAST("
   Print #fileNo, addTab(3); "'IMPORT FROM ' ||"
   Print #fileNo, addTab(3); "T."; g_anPdmFkSchemaName; " || '.' || T."; g_anPdmTypedTableName; " ||"
   Print #fileNo, addTab(3); "'.ixf OF IXF COMMITCOUNT 10000 INSERT INTO ' ||"
   Print #fileNo, addTab(3); "T."; g_anPdmFkSchemaName; " || '.' || T."; g_anPdmTypedTableName
   Print #fileNo, addTab(3); "AS VARCHAR(400)"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameExpImpTab; " T"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    View for 'IMPORT statements for PDM tables for DB-Move'
   ' ####################################################################################################################
 
 ' ### IF IVK ###
   Dim qualProcedureNameAddTablePartitionByPs As String
   qualProcedureNameAddTablePartitionByPs = genQualProcName(g_sectionIndexDbAdmin, spnAddTablePartitionByPs, ddlType)
   Dim qualProcedureNameAddTablePartitionByDiv As String
   qualProcedureNameAddTablePartitionByDiv = genQualProcName(g_sectionIndexDbAdmin, spnAddTablePartitionByDiv, ddlType)
 
 ' ### ENDIF IVK ###
   printSectionHeader "View for 'IMPORT statements for PDM tables for DB-Move'", fileNo
   Print #fileNo,

 ' ### IF IVK ###
   Const PhaseImportPrologue = 1
   Const PhaseImportDataNonPs = 2
   Const PhaseImportPartition = 3
   Const PhaseImportDataPs = 4
   Const PhaseImportEpilogue = 5
 ' ### ELSE IVK ###
 ' Const PhaseImportPrologue = 1
 ' Const PhaseImportData = 2
 ' Const PhaseImportEpilogue = 3
 ' ### ENDIF IVK ###

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameMoveImp
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SEQUENCENO,"
   Print #fileNo, addTab(1); "PHASE,"
   Print #fileNo, addTab(1); "STMNT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "WITH"
   Print #fileNo, addTab(1); "V_ImportBase"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY "; g_anAcmIsCto; " DESC, "; g_anAcmIsCtp; " DESC, "; g_anLdmIsGen; " ASC, "; g_anLdmIsLrt; " ASC, "; g_anLdmIsMqt; " DESC, "; g_anLdmIsNl; " ASC, "; g_anLdmFkSequenceNo; " ASC),"
   Print #fileNo, addTab(2); "STMNT"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameImp
   Print #fileNo, addTab(1); "WHERE"
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "("; g_anOrganizationId; " IS NULL AND "; g_anAcmIsPs; " = 0)"
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(2); g_anOrganizationId; " IS NULL"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anAcmEntityType; " IN ('M', '"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE("; g_anPoolTypeId; ", "; g_workDataPoolId; ") IN ("; g_workDataPoolId; ", "; g_productiveDataPoolId; ", "; g_archiveDataPoolId; ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anPdmTypedTableName; " NOT IN ('"; unqualTabNameApplVersion; "', '"; unqualTabNameApplHistory; "')"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(0); "),"

   Print #fileNo, addTab(1); "V_ImportData"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY "; g_anAcmIsCto; " DESC, "; g_anAcmIsCtp; " DESC, "; g_anLdmFkSequenceNo; " ASC, "; g_anOrganizationId; " ASC, "; g_anPoolTypeId; " ASC, "; g_anLdmIsGen; " ASC, "; g_anLdmIsLrt; " ASC, "; g_anLdmIsMqt; " ASC, "; g_anLdmIsNl; " ASC),"
   Print #fileNo, addTab(2); "STMNT"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameImp
   Print #fileNo, addTab(1); "WHERE"
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "("; g_anOrganizationId; " IS NOT NULL OR "; g_anAcmIsPs; " <> 0)"
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(2); g_anOrganizationId; " IS NOT NULL"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anAcmEntityType; " IN ('M', '"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE("; g_anPoolTypeId; ", "; g_workDataPoolId; ") IN ("; g_workDataPoolId; ", "; g_productiveDataPoolId; ", "; g_archiveDataPoolId; ")"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(0); "),"

   Print #fileNo, addTab(1); "V_ImportMisc"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "phase,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
 
   genProcSectionHeader fileNo, "quiesce DB", 1, True
   Print #fileNo, addTab(1); "VALUES (1, "; CStr(PhaseImportPrologue); ", 'QUIESCE DB IMMEDIATE FORCE CONNECTIONS')"

   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "disable trigger before IMPORT", 1, True
   Print #fileNo, addTab(1); "VALUES (2, "; CStr(PhaseImportPrologue); ", 'CALL "; qualProcedureNameTrigDisable; "(NULL, NULL, ?, ?)')"

 ' ### IF IVK ###
   If supportRangePartitioningByPsOid Then
     Print #fileNo, addTab(1); "UNION ALL"

     genProcSectionHeader fileNo, "setup table partitions by PS", 1, True
     Print #fileNo, addTab(1); "VALUES (1, "; CStr(PhaseImportPartition); ", 'CALL "; qualProcedureNameAddTablePartitionByPs; "(1, NULL, NULL, ?)')"
   End If

   If supportRangePartitioningByDivOid Then
     Print #fileNo, addTab(1); "UNION ALL"

     genProcSectionHeader fileNo, "setup table partitions by DIV_OID", 1, True
     Print #fileNo, addTab(1); "VALUES (1, "; CStr(PhaseImportPartition); ", 'CALL "; qualProcedureNameAddTablePartitionByDiv; "(1, NULL, NULL, ?)')"
   End If

 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "re-enable trigger after IMPORT", 1, True
   Print #fileNo, addTab(1); "VALUES (1, "; CStr(PhaseImportEpilogue); ", 'CALL "; qualProcedureNameTrigEnable; "(NULL, NULL, ?, ?)')"

   Print #fileNo, addTab(1); "UNION ALL"

   Dim qualProcedureNameRunstats As String
   qualProcedureNameRunstats = genQualProcName(g_sectionIndexDbAdmin, spnRunstats, ddlType)

   genProcSectionHeader fileNo, "update statistics", 1, True
   Print #fileNo, addTab(1); "VALUES (2, "; CStr(PhaseImportEpilogue); ", 'CALL "; qualProcedureNameRunstats; "(2, ?)')"

   Print #fileNo, addTab(1); "UNION ALL"

   Dim qualProcedureNameRevalidate As String
   qualProcedureNameRevalidate = genQualProcName(g_sectionIndexDbAdmin, spnRevalidate, ddlType)

   genProcSectionHeader fileNo, "revalidate invalid objects", 1, True
   Print #fileNo, addTab(1); "VALUES (2, "; CStr(PhaseImportEpilogue); ", 'CALL "; qualProcedureNameRevalidate; "(2, ?)')"

   Print #fileNo, addTab(1); "UNION ALL"

   Dim qualProcedureNameRebindProcs As String
   qualProcedureNameRebindProcs = genQualProcName(g_sectionIndexDbAdmin, spnRebind, ddlType)

   genProcSectionHeader fileNo, "rebind packages", 1, True
   Print #fileNo, addTab(1); "VALUES (3, "; CStr(PhaseImportEpilogue); ", 'CALL "; qualProcedureNameRebindProcs; "(2, 1, ?)')"

 ' ### IF IVK ###
   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "keep track of data load (table """ & g_qualTabNameDataHistory & """", 1, True
   Print #fileNo, addTab(1); "VALUES (4, "; CStr(PhaseImportEpilogue); ", 'DELETE FROM "; g_qualTabNameDataHistory; " WHERE EVENT = ''RELEASE-UPDATE 1.2''')"
   Print #fileNo, addTab(1); "UNION ALL"
   Print #fileNo, addTab(1); "VALUES (5, "; CStr(PhaseImportEpilogue); ", 'INSERT INTO "; g_qualTabNameDataHistory; "(EVENTDATE, EVENTTIME, EVENT, DBNAME) VALUES (CURRENT DATE, CURRENT TIME, ''RELEASE-UPDATE 1.2'', CURRENT SERVER)')"

 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "unquiesce DB", 1, True
   Print #fileNo, addTab(1); "VALUES (6, "; CStr(PhaseImportEpilogue); ", 'UNQUIESCE DB')"

   Print #fileNo, addTab(0); "),"

   Print #fileNo, addTab(1); "V_StmntAll"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "phase,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "sequenceNo,"

 ' ### IF IVK ###
   Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(PhaseImportDataNonPs); "),"
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(PhaseImportData); "),"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ImportBase"

 ' ### IF IVK ###
   Print #fileNo, addTab(1); "UNION ALL"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "sequenceNo,"
   Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(PhaseImportDataPs); "),"
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ImportData"

 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "UNION ALL"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "sequenceNo,"
   Print #fileNo, addTab(2); "phase,"
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ImportMisc"

   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY phase ASC, sequenceNo ASC),"
   Print #fileNo, addTab(2); "phase,"
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_StmntAll"
   Print #fileNo, addTab(0); ")"

   Print #fileNo, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    View for 'LOAD statements for PDM tables'
   ' ####################################################################################################################

   Dim qualViewNameLoad As String
   qualViewNameLoad = genQualViewName(g_sectionIndexDbAdmin, vnPdmLoadStmnt, vnsPdmLoadStmnt, ddlType)

   printSectionHeader "View for 'LOAD statements for PDM tables'", fileNo
   Print #fileNo,

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameLoad
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(1); g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(1); g_anPdmTypedTableName; ","
   Print #fileNo, addTab(1); g_anAcmEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); g_anOrganizationId; ","
   Print #fileNo, addTab(1); g_anPoolTypeId; ","
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); g_anLdmIsLrt; ","
   Print #fileNo, addTab(1); g_anLdmIsMqt; ","
   Print #fileNo, addTab(1); g_anAcmIsCto; ","
   Print #fileNo, addTab(1); g_anAcmIsCtp; ","
   Print #fileNo, addTab(1); g_anAcmIsRangePartAll; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(1); g_anAcmIsPs; ","
   Print #fileNo, addTab(1); g_anAcmIsPsForming; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "STMNT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "T."; g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(2); "T."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(2); "T."; g_anPdmTypedTableName; ","
   Print #fileNo, addTab(2); "T."; g_anAcmEntityId; ","
   Print #fileNo, addTab(2); "T."; g_anAcmEntityType; ","
   Print #fileNo, addTab(2); "T."; g_anOrganizationId; ","
   Print #fileNo, addTab(2); "T."; g_anPoolTypeId; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsNl; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsGen; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsLrt; ","
   Print #fileNo, addTab(2); "T."; g_anLdmIsMqt; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsCto; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsCtp; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsRangePartAll; ","
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "T."; g_anAcmIsPs; ","
   Print #fileNo, addTab(2); "T."; g_anAcmIsPsForming; ","
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "CAST("
   Print #fileNo, addTab(3); "'LOAD FROM ' ||"
   Print #fileNo, addTab(3); "T."; g_anPdmFkSchemaName; " || '.' || T."; g_anPdmTypedTableName; " ||"
   Print #fileNo, addTab(3); "'.ixf OF IXF REPLACE INTO ' ||"
   Print #fileNo, addTab(3); "T."; g_anPdmFkSchemaName; " || '.' || T."; g_anPdmTypedTableName
   Print #fileNo, addTab(3); "AS VARCHAR(400)"
   Print #fileNo, addTab(2); ")"

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameExpImpTab; " T"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    View for 'LOAD statements for PDM tables for DB-Move'
   ' ####################################################################################################################

   printSectionHeader "View for 'LOAD statements for PDM tables for DB-Move'", fileNo
   Print #fileNo,

 ' ### IF IVK ###
   Const PhaseLoadPrologue = 1
   Const PhaseLoadDataNonPs = 2
   Const PhaseLoadPartition = 3
   Const PhaseLoadDataPs = 4
   Const PhaseLoadEpilogue = 5
 ' ### ELSE IVK ###
 ' Const PhaseLoadPrologue = 1
 ' Const PhaseLoadData = 2
 ' Const PhaseLoadEpilogue = 3
 ' ### ENDIF IVK ###

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameMoveLoad
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SEQUENCENO,"
   Print #fileNo, addTab(1); "PHASE,"
   Print #fileNo, addTab(1); "STMNT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "WITH"
   Print #fileNo, addTab(1); "V_ImportBase"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY "; g_anLdmFkSequenceNo; " ASC, "; g_anOrganizationId; " ASC, "; g_anPoolTypeId; " ASC),"
   Print #fileNo, addTab(2); "STMNT"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameLoad
   Print #fileNo, addTab(1); "WHERE"
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "("; g_anOrganizationId; " IS NULL AND "; g_anAcmIsPs; " = 0)"
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(2); g_anOrganizationId; " IS NULL"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anAcmEntityType; " IN ('M', '"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE("; g_anPoolTypeId; ", "; g_workDataPoolId; ") IN ("; g_workDataPoolId; ", "; g_productiveDataPoolId; ", "; g_archiveDataPoolId; ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anPdmTypedTableName; " NOT IN ('"; unqualTabNameApplVersion; "', '"; unqualTabNameApplHistory; "')"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(0); "),"

   Print #fileNo, addTab(1); "V_ImportData"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY "; g_anAcmIsCto; " DESC, "; g_anAcmIsCtp; " DESC, "; g_anLdmIsGen; " ASC, "; g_anLdmIsLrt; " ASC, "; g_anLdmIsMqt; " ASC, "; g_anLdmIsNl; " ASC, "; g_anLdmFkSequenceNo; " ASC),"
   Print #fileNo, addTab(2); "STMNT"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameLoad
   Print #fileNo, addTab(1); "WHERE"
 ' ### IF IVK ###
   Print #fileNo, addTab(2); "("; g_anOrganizationId; " IS NOT NULL OR "; g_anAcmIsPs; " <> 0)"
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(2); g_anOrganizationId; " IS NOT NULL"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anAcmEntityType; " IN ('M', '"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE("; g_anPoolTypeId; ", "; g_workDataPoolId; ") IN ("; g_workDataPoolId; ", "; g_productiveDataPoolId; ", "; g_archiveDataPoolId; ")"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(0); "),"

   Print #fileNo, addTab(1); "V_ImportMisc"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "phase,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
 
   genProcSectionHeader fileNo, "quiesce DB", 1, True
   Print #fileNo, addTab(1); "VALUES (1, "; CStr(PhaseLoadPrologue); ", 'QUIESCE DB IMMEDIATE FORCE CONNECTIONS')"

   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "disable trigger before IMPORT", 1, True
   Print #fileNo, addTab(1); "VALUES (2, "; CStr(PhaseLoadPrologue); ", 'CALL "; qualProcedureNameTrigDisable; "(NULL, NULL, ?, ?)')"

   Print #fileNo, addTab(1); "UNION ALL"
 
   Dim qualProcNameSetIntegrity As String
   qualProcNameSetIntegrity = genQualProcName(g_sectionIndexDbAdmin, spnIntegrity, ddlType)
 
 ' ### IF IVK ###
   genProcSectionHeader fileNo, "set integrity", 1, True
   Print #fileNo, addTab(1); "VALUES (1, "; CStr(PhaseLoadPartition); ", 'CALL "; qualProcNameSetIntegrity; "(2, ?)')"

   If supportRangePartitioningByPsOid Then
     Print #fileNo, addTab(1); "UNION ALL"

     genProcSectionHeader fileNo, "setup table partitions by " & g_anPsOid, 1, True
     Print #fileNo, addTab(1); "VALUES (3, "; CStr(PhaseLoadPartition); ", 'CALL "; qualProcedureNameAddTablePartitionByPs; "(1, NULL, NULL, ?)')"
   End If

   If supportRangePartitioningByDivOid Then
     Print #fileNo, addTab(1); "UNION ALL"

     genProcSectionHeader fileNo, "setup table partitions by DIV_OID", 1, True
     Print #fileNo, addTab(1); "VALUES (2, "; CStr(PhaseLoadPartition); ", 'CALL "; qualProcedureNameAddTablePartitionByDiv; "(1, NULL, NULL, ?)')"
   End If

   Print #fileNo, addTab(1); "UNION ALL"

 ' ### ENDIF IVK ###
   genProcSectionHeader fileNo, "set integrity", 1, True
   Print #fileNo, addTab(1); "VALUES (1, "; CStr(PhaseLoadEpilogue); ", 'CALL "; qualProcNameSetIntegrity; "(2, ?)')"

   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "re-enable trigger after LOAD", 1, True
   Print #fileNo, addTab(1); "VALUES (2, "; CStr(PhaseLoadEpilogue); ", 'CALL "; qualProcedureNameTrigEnable; "(NULL, NULL, ?, ?)')"

   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "revalidate invalid objects", 1, True
   Print #fileNo, addTab(1); "VALUES (3, "; CStr(PhaseLoadEpilogue); ", 'CALL "; qualProcedureNameRevalidate; "(2, ?)')"

   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "update statistics", 1, True
   Print #fileNo, addTab(1); "VALUES (4, "; CStr(PhaseLoadEpilogue); ", 'CALL "; qualProcedureNameRunstats; "(2, ?)')"

   Print #fileNo, addTab(1); "UNION ALL"

   genProcSectionHeader fileNo, "rebind packages", 1, True
   Print #fileNo, addTab(1); "VALUES (5, "; CStr(PhaseLoadEpilogue); ", 'CALL "; qualProcedureNameRebindProcs; "(2, 1, ?)')"

   Print #fileNo, addTab(1); "UNION ALL"

 ' ### IF IVK ###
   genProcSectionHeader fileNo, "keep track of data load (table """ & g_qualTabNameDataHistory & """", 1, True
   Print #fileNo, addTab(1); "VALUES (6, "; CStr(PhaseLoadEpilogue); ", 'DELETE FROM "; g_qualTabNameDataHistory; " WHERE EVENT = ''RELEASE-UPDATE 1.2''')"
   Print #fileNo, addTab(1); "UNION ALL"
   Print #fileNo, addTab(1); "VALUES (7, "; CStr(PhaseLoadEpilogue); ", 'INSERT INTO "; g_qualTabNameDataHistory; "(EVENTDATE, EVENTTIME, EVENT, DBNAME) VALUES (CURRENT DATE, CURRENT TIME, ''RELEASE-UPDATE 1.2'', CURRENT SERVER)')"

   Print #fileNo, addTab(1); "UNION ALL"

 ' ### ENDIF IVK ###
   genProcSectionHeader fileNo, "unquiesce DB", 1, True
   Print #fileNo, addTab(1); "VALUES (8, "; CStr(PhaseLoadEpilogue); ", 'UNQUIESCE DB')"

   Print #fileNo, addTab(0); "),"

   Print #fileNo, addTab(1); "V_StmntAll"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "phase,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "sequenceNo,"

 ' ### IF IVK ###
   Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(PhaseLoadDataNonPs); "),"
 ' ### ELSE IVK ###
 ' Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(PhaseLoadData); "),"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ImportBase"

 ' ### IF IVK ###
   Print #fileNo, addTab(1); "UNION ALL"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "sequenceNo,"
   Print #fileNo, addTab(2); g_dbtEnumId; "("; CStr(PhaseLoadDataPs); "),"
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ImportData"

 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "UNION ALL"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "sequenceNo,"
   Print #fileNo, addTab(2); "phase,"
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_ImportMisc"

   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY phase ASC, sequenceNo ASC),"
   Print #fileNo, addTab(2); "phase,"
   Print #fileNo, addTab(2); "stmnt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_StmntAll"
   Print #fileNo, addTab(0); ")"

   Print #fileNo, gc_sqlCmdDelim

 ' ### IF IVK ###
   ' ####################################################################################################################
   ' #    View for 'EXPORT statements for tables corresponding to ACM core classes'
   ' ####################################################################################################################

   Dim qualViewNameExpCore As String
   qualViewNameExpCore = genQualViewName(g_sectionIndexDbAdmin, vnPdmCoreExportStmnt, vnsPdmCoreExportStmnt, ddlType)

   printSectionHeader "View for 'EXPORT statements for tables corresponding to ACM core classes'", fileNo
   Print #fileNo,

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameExpCore
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(1); g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(1); g_anPdmTypedTableName; ","
   Print #fileNo, addTab(1); g_anAcmEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); g_anOrganizationId; ","
   Print #fileNo, addTab(1); g_anPoolTypeId; ","
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); g_anLdmIsLrt; ","
   Print #fileNo, addTab(1); g_anLdmIsMqt; ","
   Print #fileNo, addTab(1); g_anAcmIsCto; ","
   Print #fileNo, addTab(1); g_anAcmIsCtp; ","
   Print #fileNo, addTab(1); g_anAcmIsRangePartAll; ","
   Print #fileNo, addTab(1); g_anAcmIsPs; ","
   Print #fileNo, addTab(1); g_anAcmIsPsForming; ","
   Print #fileNo, addTab(1); "STMNT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(2); g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(2); g_anPdmTypedTableName; ","
   Print #fileNo, addTab(2); g_anAcmEntityId; ","
   Print #fileNo, addTab(2); g_anAcmEntityType; ","
   Print #fileNo, addTab(2); g_anOrganizationId; ","
   Print #fileNo, addTab(2); g_anPoolTypeId; ","
   Print #fileNo, addTab(2); g_anLdmIsNl; ","
   Print #fileNo, addTab(2); g_anLdmIsGen; ","
   Print #fileNo, addTab(2); g_anLdmIsLrt; ","
   Print #fileNo, addTab(2); g_anLdmIsMqt; ","
   Print #fileNo, addTab(2); g_anAcmIsCto; ","
   Print #fileNo, addTab(2); g_anAcmIsCtp; ","
   Print #fileNo, addTab(2); g_anAcmIsRangePartAll; ","
   Print #fileNo, addTab(2); g_anAcmIsPs; ","
   Print #fileNo, addTab(2); g_anAcmIsPsForming; ","
   Print #fileNo, addTab(2); "STMNT"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameExp
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "("; g_anAcmEntityId; ","; g_anAcmEntityType; ")"; _
                                    "IN (SELECT "; g_anAcmEntityId; ","; g_anAcmEntityType; _
                                    " FROM "; qualViewNameCore; ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE("; g_anOrganizationId; ",1) = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE("; g_anPoolTypeId; ",1) = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(0); ")"
   Print #fileNo, gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    View for 'IMPORT statements for tables corresponding to ACM core classes'
   ' ####################################################################################################################

   Dim qualViewNameImpCore As String
   qualViewNameImpCore = genQualViewName(g_sectionIndexDbAdmin, vnPdmCoreImportStmnt, vnsPdmCoreImportStmnt, ddlType)

   printSectionHeader "View for 'IMPORT statements for tables corresponding to ACM core classes'", fileNo
   Print #fileNo,

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameImpCore
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(1); g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(1); g_anPdmTypedTableName; ","
   Print #fileNo, addTab(1); g_anAcmEntityId; ","
   Print #fileNo, addTab(1); g_anAcmEntityType; ","
   Print #fileNo, addTab(1); g_anOrganizationId; ","
   Print #fileNo, addTab(1); g_anPoolTypeId; ","
   Print #fileNo, addTab(1); g_anLdmIsNl; ","
   Print #fileNo, addTab(1); g_anLdmIsGen; ","
   Print #fileNo, addTab(1); g_anLdmIsLrt; ","
   Print #fileNo, addTab(1); g_anAcmIsCto; ","
   Print #fileNo, addTab(1); g_anAcmIsCtp; ","
   Print #fileNo, addTab(1); g_anAcmIsRangePartAll; ","
   Print #fileNo, addTab(1); g_anAcmIsPs; ","
   Print #fileNo, addTab(1); g_anAcmIsPsForming; ","
   Print #fileNo, addTab(1); "STMNT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); g_anLdmFkSequenceNo; ","
   Print #fileNo, addTab(2); g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(2); g_anPdmTypedTableName; ","
   Print #fileNo, addTab(2); g_anAcmEntityId; ","
   Print #fileNo, addTab(2); g_anAcmEntityType; ","
   Print #fileNo, addTab(2); g_anOrganizationId; ","
   Print #fileNo, addTab(2); g_anPoolTypeId; ","
   Print #fileNo, addTab(2); g_anLdmIsNl; ","
   Print #fileNo, addTab(2); g_anLdmIsGen; ","
   Print #fileNo, addTab(2); g_anLdmIsLrt; ","
   Print #fileNo, addTab(2); g_anAcmIsCto; ","
   Print #fileNo, addTab(2); g_anAcmIsCtp; ","
   Print #fileNo, addTab(2); g_anAcmIsRangePartAll; ","
   Print #fileNo, addTab(2); g_anAcmIsPs; ","
   Print #fileNo, addTab(2); g_anAcmIsPsForming; ","
   Print #fileNo, addTab(2); "STMNT"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualViewNameImp
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "("; g_anAcmEntityId; ","; g_anAcmEntityType; ")"; _
                                    "IN (SELECT "; g_anAcmEntityId; ","; g_anAcmEntityType; _
                                    " FROM "; qualViewNameCore; ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE("; g_anOrganizationId; ",1) = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE("; g_anPoolTypeId; ",1) = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(0); ")"
   Print #fileNo, gc_sqlCmdDelim

 ' ### ENDIF IVK ###
   ' ####################################################################################################################
   ' #    View retrieving statements generating statement for moving data between databases
   ' ####################################################################################################################
 
   Dim qualViewNameMoveStmnts As String
   qualViewNameMoveStmnts = genQualViewName(g_sectionIndexDbAdmin, vnPdmMoveScript, vnsPdmMoveScript, ddlType)

   printSectionHeader "View retrieving statements generating statement for moving data between databases", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameMoveStmnts
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "seqNo,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "VALUES("

   Print #fileNo, addTab(2); "1,"
   genDbMoveScript fileNo, 1, ddlType

   Print #fileNo, addTab(1); ")"
   Print #fileNo,
   Print #fileNo, addTab(1); "UNION ALL"
   Print #fileNo,
   Print #fileNo, addTab(1); "VALUES("

   Print #fileNo, addTab(2); "2,"
   genDbMoveScript fileNo, 2, ddlType

   Print #fileNo, addTab(1); ")"
   Print #fileNo,
   Print #fileNo, addTab(1); "UNION ALL"
   Print #fileNo,
   Print #fileNo, addTab(1); "VALUES("

   Print #fileNo, addTab(2); "3,"
   genDbMoveScript fileNo, 3, ddlType

   Print #fileNo, addTab(1); ")"

   Print #fileNo, addTab(0); ")"

   Print #fileNo, gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    View for DML-Statements to export and drop triggers'
   ' ####################################################################################################################

   Dim qualViewNameDropTrigger As String
   qualViewNameDropTrigger = genQualViewName(g_sectionIndexDbAdmin, vnDropTrigger, vsnDropTrigger, ddlType)
 
   printSectionHeader "View for DML-Statements to export and drop triggers", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameDropTrigger
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "sequenceNo,"
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "VALUES ("
   Print #fileNo, addTab(3); "1,"
   Print #fileNo, addTab(3); "CAST('EXPORT TO MDSDB-Trigger.ddl OF DEL LOBFILE ''MDSDB-Trigger.ddl'' MODIFIED BY LOBSINFILE SELECT TEXT || CHR(10) || ''@'' || CHR(10) FROM SYSCAT.TRIGGERS WHERE TRIGSCHEMA LIKE ''"; g_allSchemaNamePattern; "''' AS VARCHAR(200))"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(2); "UNION ALL"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "2,"
   Print #fileNo, addTab(3); "'DROP TRIGGER ' ||"
   Print #fileNo, addTab(3); "TRIGSCHEMA || '.' || TRIGNAME"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TRIGGERS"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, gc_sqlCmdDelim

 ' ### IF IVK ###
 LdmEntryPoint:
   ' ####################################################################################################################
   ' #    SP for maintaining records in table APPLVERSION, APPLHISTORY and DATAFIXHISTORY
   ' ####################################################################################################################

   Dim qualProcedureNameSetApplVersion As String
   qualProcedureNameSetApplVersion = genQualProcName(g_sectionIndexDbAdmin, spnSetApplVersion, ddlType)

   printSectionHeader "SP for maintaining records in table APPLVERSION, APPLHISTORY and DATAFIXHISTORY", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetApplVersion
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "version_in", "VARCHAR(20)", True, "version-info"
   genProcParm fileNo, "IN", "revision_in", "VARCHAR(20)", True, "revision-info"
   genProcParm fileNo, "IN", "lrtOid_in", g_dbtOid, True, "(optional) for data fixes: OID of LRT if fix is implemented via LRT"
   genProcParm fileNo, "IN", "onlyOnce_in", g_dbtBoolean, True, "(optional) for data fixes: if set to '1' register the fix as 'once-only-fix'"
   genProcParm fileNo, "IN", "description_in", "VARCHAR(100)", False, "description text to store"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genProcSectionHeader fileNo, "declare statement", , Not supportSpLogging Or Not generateSpLogMessages
   genVarDecl fileNo, "v_stmnt", "STATEMENT"

   genSpLogProcEnter fileNo, qualProcedureNameSetApplVersion, ddlType, , "'version_in", "'revision_in", "lrtOid_in", "onlyOnce_in", "'description_in"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF POSSTR(UPPER(version_in), '-HF') = 0 THEN"
   genProcSectionHeader fileNo, "record version in table """ & getUnqualObjName(g_qualTabNameDataFixHistory) & """", 2, True

   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); g_qualTabNameDataFixHistory
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "VERSION, REVISION, "; g_anLrtOid; ", DEPLOYDATE, DEPLOYTIME, DESCRIPTION"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "version_in, revision_in, lrtOid_in, CURRENT DATE, CURRENT TIME, description_in"
   Print #fileNo, addTab(2); ");"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF onlyOnce_in = 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; g_qualTabNameDataFixIgnored; " (VERSION, REVISION, REASON) VALUES (version_in, revision_in, 'IBM: use only once');"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "ELSE"

   genProcSectionHeader fileNo, "record version in table """ & getUnqualObjName(g_qualTabNameApplVersion) & """", 2, True
   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); g_qualTabNameApplVersion
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "VERSION = version_in"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); g_qualTabNameApplVersion
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "VERSION, REVISION, DEPLOYDATE, DEPLOYTIME, DESCRIPTION"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "version_in, revision_in, CURRENT DATE, CURRENT TIME, description_in"
   Print #fileNo, addTab(2); ");"
   Print #fileNo,
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); g_qualTabNameApplHistory
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "VERSION, REVISION, DEPLOYDATE, DEPLOYTIME, DESCRIPTION"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "version_in, revision_in, CURRENT DATE, CURRENT TIME, description_in"
   Print #fileNo, addTab(2); ");"
 
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameSetApplVersion, ddlType, , "'version_in", "'revision_in", "lrtOid_in", "onlyOnce_in", , "'description_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for maintaining records in table APPLVERSION, APPLHISTORY and DATAFIXHISTORY", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetApplVersion
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "version_in", "VARCHAR(20)", True, "version-info"
   genProcParm fileNo, "IN", "revision_in", "VARCHAR(20)", True, "revision-info"
   genProcParm fileNo, "IN", "lrtOid_in", g_dbtOid, True, "(optional) for data fixes: OID of LRT if fix is implemented via LRT"
   genProcParm fileNo, "IN", "description_in", "VARCHAR(100)", False, "description text to store"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, qualProcedureNameSetApplVersion, ddlType, , "'version_in", "'revision_in", "'description_in"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetApplVersion; "(version_in, revision_in, lrtOid_in, 0, description_in);"

   genSpLogProcExit fileNo, qualProcedureNameSetApplVersion, ddlType, , "'version_in", "'revision_in", "'description_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for maintaining records in table APPLVERSION, APPLHISTORY and DATAFIXHISTORY", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetApplVersion
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "version_in", "VARCHAR(20)", True, "version-info"
   genProcParm fileNo, "IN", "revision_in", "VARCHAR(20)", True, "revision-info"
   genProcParm fileNo, "IN", "description_in", "VARCHAR(100)", False, "description text to store"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, qualProcedureNameSetApplVersion, ddlType, , "'version_in", "'revision_in", "'description_in"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetApplVersion; "(version_in, revision_in, CAST(NULL AS "; g_dbtBoolean; "), 0, description_in);"

   genSpLogProcExit fileNo, qualProcedureNameSetApplVersion, ddlType, , "'version_in", "'revision_in", "'description_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for maintaining records in table APPLVERSION, APPLHISTORY and DATAFIXHISTORY", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetApplVersion
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "version_in", "VARCHAR(20)", True, "version-info"
   genProcParm fileNo, "IN", "description_in", "VARCHAR(100)", False, "description text to store"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, qualProcedureNameSetApplVersion, ddlType, , "'version_in", "'description_in"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetApplVersion; "(version_in, CAST(NULL AS VARCHAR(20)), CAST(NULL AS "; g_dbtBoolean; "), 0, description_in);"

   genSpLogProcExit fileNo, qualProcedureNameSetApplVersion, ddlType, , "'version_in", "'description_in"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 ' ### ENDIF IVK ###
 End Sub
 
 
 Sub genDdlSetgrants1( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   Const commitCount = 10000

   Dim tempTabNameStatementGrant As String
   tempTabNameStatementGrant = tempTabNameStatement & "Grants"

   On Error GoTo ErrorExit

   ' ####################################################################################################################
   ' #    SP for granting access rights on schemas
   ' ####################################################################################################################

   Dim qualProcedureNameSchema As String
   qualProcedureNameSchema = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "SCH")

   printSectionHeader "SP for granting access rights on schemas", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSchema
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas that apply"
   genProcParm fileNo, "IN", "schemaNamePatternOpt_in", g_dbtDbSchemaName, True, "optional user-supplied additional filter for schemas"
   genProcParm fileNo, "IN", "filterTemplate_in", "VARCHAR(100)", True, "optional filter-pattern to apply (may use placeholder '<%S>')"
   genProcParm fileNo, "IN", "opType_in", "VARCHAR(6)", True, "either 'GRANT' or 'REVOKE'"
   genProcParm fileNo, "IN", "privilege_in", "VARCHAR(100)", True, "privilege to grant resp. revoke"
   genProcParm fileNo, "IN", "granteeType_in", "VARCHAR(6)", True, "either 'USER', 'GROUP' or 'PUBLIC'"
   genProcParm fileNo, "IN", "grantee_in", "VARCHAR(100)", True, "determines the user / group"
   genProcParm fileNo, "IN", "withGrant_in", g_dbtBoolean, True, "apply 'WITH GRANT OPTION' if and only if 'withGrant_in = 1'"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "INOUT", "rowCount_inout", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "privCannotBeRevoked", "42504"
   genCondDecl fileNo, "doesNotHavePrivOnObject", "42501"
   genCondDecl fileNo, "failedToExecAsSpecified", "42502"
   genCondDecl fileNo, "unexpectedToken", "42601"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_schemaName", g_dbtDbSchemaName, "NULL"
   genVarDecl fileNo, "v_csrStmntTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_objCsrStmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE objCursor CURSOR WITH HOLD FOR v_objCsrStmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR privCannotBeRevoked"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotHavePrivOnObject"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR failedToExecAsSpecified"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR unexpectedToken"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , , , , , , "Grants"

   genSpLogProcEnter fileNo, qualProcedureNameSchema, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_csrStmntTxt ="
   Print #fileNo, addTab(2); "'SELECT ' ||"
   Print #fileNo, addTab(3); "'SCHEMANAME ' ||"
   Print #fileNo, addTab(2); "'FROM ' ||"
   Print #fileNo, addTab(3); "'SYSCAT.SCHEMATA ' ||"
   Print #fileNo, addTab(2); "'WHERE ' ||"
   Print #fileNo, addTab(3); "'RTRIM(SCHEMANAME) LIKE ''' || schemaNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(SCHEMANAME) LIKE ''' || COALESCE(schemaNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(3); "(CASE WHEN COALESCE(filterTemplate_in, '') = '' THEN '' ELSE 'AND (' || REPLACE(filterTemplate_in, '<%S>', 'SCHEMANAME') || ') ' END) ||"
   Print #fileNo, addTab(2); "'FOR READ ONLY'"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_objCsrStmnt FROM v_csrStmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "OPEN objCursor;"
   Print #fileNo, addTab(1); "FETCH objCursor INTO v_schemaName;"
   Print #fileNo,
   Print #fileNo, addTab(1); "WHILE (SQLCODE = 0) DO"

   Print #fileNo, addTab(2); "IF (opType_in = 'GRANT') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'GRANT ' || privilege_in || ' ON SCHEMA ' || RTRIM(v_schemaName ) || ' TO ' || granteeType_in || COALESCE(' ' || grantee_in,'') || (CASE WHEN withGrant_in = 1 THEN ' WITH GRANT OPTION' ELSE '' END)"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'REVOKE ' || privilege_in || ' ON SCHEMA ' || RTRIM(v_schemaName) || ' FROM ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(2); "SET rowCount_inout = rowCount_inout + 1;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   If commitCount > 0 Then
     Print #fileNo,
     Print #fileNo, addTab(3); "IF MOD(rowCount_inout, "; CStr(commitCount); ") = 0 THEN"
     Print #fileNo, addTab(4); "COMMIT;"
     Print #fileNo, addTab(3); "END IF;"
   End If

   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementGrant
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "rowCount_inout,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "FETCH objCursor INTO v_schemaName;"
   Print #fileNo, addTab(1); "END WHILE;"

   Print #fileNo,
   Print #fileNo, addTab(1); "CLOSE objCursor;"

   genSpLogProcExit fileNo, qualProcedureNameSchema, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for granting access rights on tables, views and aliases
   ' ####################################################################################################################

   Dim qualProcedureNameTab As String
   qualProcedureNameTab = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "TAB")

   printSectionHeader "SP for granting access rights on tables, views and aliases", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTab
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "type_in", "CHAR(1)", True, "distinguishes between table ('T'), alias ('A') and view ('V')"
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas that apply"
   genProcParm fileNo, "IN", "schemaNamePatternOpt_in", g_dbtDbSchemaName, True, "optional user-supplied additional filter for schemas"
   genProcParm fileNo, "IN", "tableNamePattern_in", "VARCHAR(80)", True, "determines the tables that apply"
   genProcParm fileNo, "IN", "tableNamePatternOpt_in", "VARCHAR(80)", True, "optional user-supplied additional filter for tables"
   genProcParm fileNo, "IN", "filterTemplate_in", "VARCHAR(100)", True, "optional filter-pattern to apply (may use placeholder '<%S>', '<%N>')"
   genProcParm fileNo, "IN", "opType_in", "VARCHAR(6)", True, "either 'GRANT' or 'REVOKE'"
   genProcParm fileNo, "IN", "privilege_in", "VARCHAR(100)", True, "privilege to grant resp. revoke"
   genProcParm fileNo, "IN", "granteeType_in", "VARCHAR(6)", True, "either 'USER', 'GROUP' or 'PUBLIC'"
   genProcParm fileNo, "IN", "grantee_in", "VARCHAR(100)", True, "determines the user / group"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "INOUT", "rowCount_inout", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "privCannotBeRevoked", "42504"
   genCondDecl fileNo, "doesNotHavePrivOnObject", "42501"
   genCondDecl fileNo, "failedToExecAsSpecified", "42502"
   genCondDecl fileNo, "unexpectedToken", "42601"
   genCondDecl fileNo, "inoperative", "51024"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_tabSchema", g_dbtDbSchemaName, "NULL"
   genVarDecl fileNo, "v_tabName", g_dbtDbTableName, "NULL"
   genVarDecl fileNo, "v_csrStmntTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_objCsrStmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE objCursor CURSOR WITH HOLD FOR v_objCsrStmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR privCannotBeRevoked"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotHavePrivOnObject"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR failedToExecAsSpecified"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR unexpectedToken"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR inoperative"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempStatement fileNo, 1, , , , , , , "Grants"

   genSpLogProcEnter fileNo, qualProcedureNameTab, ddlType, , "'type_in", "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'tableNamePattern_in", "'tableNamePatternOpt_in", "'filterTemplate_in", "opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_csrStmntTxt ="
   Print #fileNo, addTab(2); "'SELECT ' ||"
   Print #fileNo, addTab(3); "'TABSCHEMA,' ||"
   Print #fileNo, addTab(3); "'TABNAME ' ||"
   Print #fileNo, addTab(2); "'FROM ' ||"
   Print #fileNo, addTab(3); "'SYSCAT.TABLES ' ||"
   Print #fileNo, addTab(2); "'WHERE ' ||"
   Print #fileNo, addTab(3); "'RTRIM(TABSCHEMA) LIKE ''' || schemaNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(TABSCHEMA) LIKE ''' || COALESCE(schemaNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(TABNAME) LIKE ''' || tableNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(TABNAME) LIKE ''' || COALESCE(tableNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'TYPE = ''' || type_in || ''' ' ||"
   Print #fileNo, addTab(3); "(CASE WHEN COALESCE(filterTemplate_in, '') = '' THEN '' ELSE 'AND (' || REPLACE(REPLACE(filterTemplate_in, '<%S>', 'TABSCHEMA'), '<%N>', 'TABNAME') || ') ' END) ||"
   Print #fileNo, addTab(2); "'FOR READ ONLY'"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_objCsrStmnt FROM v_csrStmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "OPEN objCursor;"
   Print #fileNo, addTab(1); "FETCH objCursor INTO v_tabSchema, v_tabName;"
   Print #fileNo,
   Print #fileNo, addTab(1); "WHILE (SQLCODE = 0) DO"
 
   Print #fileNo, addTab(2); "IF (opType_in = 'GRANT') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'GRANT ' || privilege_in || ' ON ' || RTRIM(v_tabSchema) || '.' || RTRIM(v_tabName) || ' TO ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'REVOKE ' || privilege_in || ' ON ' || RTRIM(v_tabSchema) || '.' || RTRIM(v_tabName) || ' FROM ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(2); "SET rowCount_inout = rowCount_inout + 1;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   If commitCount > 0 Then
     Print #fileNo,
     Print #fileNo, addTab(3); "IF MOD(rowCount_inout, "; CStr(commitCount); ") = 0 THEN"
     Print #fileNo, addTab(4); "COMMIT;"
     Print #fileNo, addTab(3); "END IF;"
   End If

   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementGrant
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "rowCount_inout,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(2); "FETCH objCursor INTO v_tabSchema, v_tabName;"
   Print #fileNo, addTab(1); "END WHILE;"

   Print #fileNo,
   Print #fileNo, addTab(1); "CLOSE objCursor;"

   genSpLogProcExit fileNo, qualProcedureNameTab, ddlType, , "'type_in", "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'tableNamePattern_in", "'tableNamePatternOpt_in", "'filterTemplate_in", "opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for granting access rights on sequences
   ' ####################################################################################################################

   Dim qualProcedureNameSeq As String
   qualProcedureNameSeq = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "SEQ")

   printSectionHeader "SP for granting access rights on sequences", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSeq
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas that apply"
   genProcParm fileNo, "IN", "schemaNamePatternOpt_in", g_dbtDbSchemaName, True, "optional user-supplied additional filter for schemas"
   genProcParm fileNo, "IN", "seqNamePattern_in", "VARCHAR(80)", True, "determines the sequences that apply"
   genProcParm fileNo, "IN", "seqNamePatternOpt_in", "VARCHAR(80)", True, "optional user-supplied additional filter for sequences"
   genProcParm fileNo, "IN", "filterTemplate_in", "VARCHAR(100)", True, "optional filter-pattern to apply (may use placeholder '<%S>', '<%N>')"
   genProcParm fileNo, "IN", "opType_in", "VARCHAR(6)", True, "either 'GRANT' or 'REVOKE'"
   genProcParm fileNo, "IN", "privilege_in", "VARCHAR(100)", True, "privilege to grant resp. revoke"
   genProcParm fileNo, "IN", "granteeType_in", "VARCHAR(6)", True, "either 'USER', 'GROUP' or 'PUBLIC'"
   genProcParm fileNo, "IN", "grantee_in", "VARCHAR(100)", True, "determines the user / group"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "INOUT", "rowCount_inout", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "privCannotBeRevoked", "42504"
   genCondDecl fileNo, "doesNotHavePrivOnObject", "42501"
   genCondDecl fileNo, "failedToExecAsSpecified", "42502"
   genCondDecl fileNo, "unexpectedToken", "42601"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_seqSchema", g_dbtDbSchemaName, "NULL"
   genVarDecl fileNo, "v_seqName", "VARCHAR(50)", "NULL"
   genVarDecl fileNo, "v_csrStmntTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_objCsrStmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE objCursor CURSOR WITH HOLD FOR v_objCsrStmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR privCannotBeRevoked"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotHavePrivOnObject"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR failedToExecAsSpecified"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR unexpectedToken"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , , , , , , "Grants"

   genSpLogProcEnter fileNo, qualProcedureNameSeq, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'seqNamePattern_in", "'seqNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_csrStmntTxt ="
   Print #fileNo, addTab(2); "'SELECT ' ||"
   Print #fileNo, addTab(3); "'SEQSCHEMA,' ||"
   Print #fileNo, addTab(3); "'SEQNAME ' ||"
   Print #fileNo, addTab(2); "'FROM ' ||"
   Print #fileNo, addTab(3); "'SYSCAT.SEQUENCES ' ||"
   Print #fileNo, addTab(2); "'WHERE ' ||"
   Print #fileNo, addTab(3); "'RTRIM(SEQSCHEMA) LIKE ''' || schemaNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(SEQSCHEMA) LIKE ''' || COALESCE(schemaNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(SEQNAME) LIKE ''' || seqNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(SEQNAME) LIKE ''' || COALESCE(seqNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(3); "(CASE WHEN COALESCE(filterTemplate_in, '') = '' THEN '' ELSE 'AND (' || REPLACE(REPLACE(filterTemplate_in, '<%S>', 'SEQSCHEMA'), '<%N>', 'SEQNAME') || ') ' END) ||"
   Print #fileNo, addTab(2); "'FOR READ ONLY'"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_objCsrStmnt FROM v_csrStmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "OPEN objCursor;"
   Print #fileNo, addTab(1); "FETCH objCursor INTO v_seqSchema, v_seqName;"
   Print #fileNo,
   Print #fileNo, addTab(1); "WHILE (SQLCODE = 0) DO"

   Print #fileNo, addTab(2); "IF (opType_in = 'GRANT') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'GRANT ' || privilege_in || ' ON SEQUENCE ' || RTRIM(v_seqSchema )|| '.' || RTRIM(v_seqName) || ' TO ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'REVOKE ' || privilege_in || ' ON SEQUENCE ' || RTRIM(v_seqSchema) || '.' || RTRIM(v_seqName) || ' FROM ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(2); "SET rowCount_inout = rowCount_inout + 1;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   If commitCount > 0 Then
     Print #fileNo,
     Print #fileNo, addTab(3); "IF MOD(rowCount_inout, "; CStr(commitCount); ") = 0 THEN"
     Print #fileNo, addTab(4); "COMMIT;"
     Print #fileNo, addTab(3); "END IF;"
   End If

   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementGrant
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "rowCount_inout,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "FETCH objCursor INTO v_seqSchema, v_seqName;"
   Print #fileNo, addTab(1); "END WHILE;"
   Print #fileNo,
   Print #fileNo, addTab(1); "CLOSE objCursor;"

   genSpLogProcExit fileNo, qualProcedureNameSeq, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'seqNamePattern_in", "'seqNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for granting access rights on packages
   ' ####################################################################################################################

   Dim qualProcedureNamePkg As String
   qualProcedureNamePkg = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "PKG")

   printSectionHeader "SP for granting access rights on packages", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNamePkg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas that apply"
   genProcParm fileNo, "IN", "schemaNamePatternOpt_in", g_dbtDbSchemaName, True, "optional user-supplied additional filter for schemas"
   genProcParm fileNo, "IN", "pkgNamePattern_in", "VARCHAR(80)", True, "determines the packages that apply"
   genProcParm fileNo, "IN", "pkgNamePatternOpt_in", "VARCHAR(80)", True, "optional user-supplied additional filter for packages"
   genProcParm fileNo, "IN", "filterTemplate_in", "VARCHAR(100)", True, "optional filter-pattern to apply (may use placeholder '<%S>', '<%N>')"
   genProcParm fileNo, "IN", "opType_in", "VARCHAR(6)", True, "either 'GRANT' or 'REVOKE'"
   genProcParm fileNo, "IN", "privilege_in", "VARCHAR(100)", True, "privilege to grant resp. revoke"
   genProcParm fileNo, "IN", "granteeType_in", "VARCHAR(6)", True, "either 'USER', 'GROUP' or 'PUBLIC'"
   genProcParm fileNo, "IN", "grantee_in", "VARCHAR(100)", True, "determines the user / group"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "INOUT", "rowCount_inout", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "privCannotBeRevoked", "42504"
   genCondDecl fileNo, "doesNotHavePrivOnObject", "42501"
   genCondDecl fileNo, "failedToExecAsSpecified", "42502"
   genCondDecl fileNo, "unexpectedToken", "42601"
   genCondDecl fileNo, "inoperative", "51024"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_pkgSchema", g_dbtDbSchemaName, "NULL"
   genVarDecl fileNo, "v_pkgName", "VARCHAR(50)", "NULL"
   genVarDecl fileNo, "v_csrStmntTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_objCsrStmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE objCursor CURSOR WITH HOLD FOR v_objCsrStmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR privCannotBeRevoked"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotHavePrivOnObject"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR failedToExecAsSpecified"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR unexpectedToken"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR inoperative"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , , , , , , "Grants"

   genSpLogProcEnter fileNo, qualProcedureNamePkg, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'pkgNamePattern_in", "'pkgNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_csrStmntTxt ="
   Print #fileNo, addTab(2); "'SELECT ' ||"
   Print #fileNo, addTab(3); "'PA.PKGSCHEMA,' ||"
   Print #fileNo, addTab(3); "'PA.PKGNAME ' ||"
   Print #fileNo, addTab(2); "'FROM ' ||"
   Print #fileNo, addTab(3); "'SYSCAT.PACKAGES PA ' ||"
   Print #fileNo, addTab(2); "'LEFT OUTER JOIN ' ||"
   Print #fileNo, addTab(3); "'SYSCAT.ROUTINEDEP RD ' ||"
   Print #fileNo, addTab(2); "'ON ' ||"
   Print #fileNo, addTab(3); "'RD.BNAME = PA.PKGNAME ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RD.ROUTINESCHEMA = PA.PKGSCHEMA ' ||"
   Print #fileNo, addTab(2); "'LEFT OUTER JOIN ' ||"
   Print #fileNo, addTab(3); "'SYSCAT.ROUTINES RT ' ||"
   Print #fileNo, addTab(2); "'ON ' ||"
   Print #fileNo, addTab(3); "'RD.ROUTINESCHEMA = RT.ROUTINESCHEMA ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RD.ROUTINENAME = RT.SPECIFICNAME ' ||"

   Print #fileNo, addTab(2); "'WHERE ' ||"
   Print #fileNo, addTab(3); "'RTRIM(PA.PKGSCHEMA) LIKE ''' || schemaNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(PA.PKGSCHEMA) LIKE ''' || COALESCE(schemaNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(PA.PKGNAME) LIKE ''' || pkgNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(PA.PKGNAME) LIKE ''' || COALESCE(pkgNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(3); "(CASE WHEN COALESCE(filterTemplate_in, '') = '' THEN '' ELSE 'AND (' || REPLACE(REPLACE(filterTemplate_in, '<%S>', 'PA.PKGSCHEMA'), '<%N>', 'PA.PKGNAME') || ') ' END) ||"
   Print #fileNo, addTab(2); "'FOR READ ONLY'"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_objCsrStmnt FROM v_csrStmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "OPEN objCursor;"
   Print #fileNo, addTab(1); "FETCH objCursor INTO v_pkgSchema, v_pkgName;"
   Print #fileNo,
   Print #fileNo, addTab(1); "WHILE (SQLCODE = 0) DO"

   Print #fileNo, addTab(2); "IF (opType_in = 'GRANT') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'GRANT ' || privilege_in || ' ON PACKAGE ' || RTRIM(v_pkgSchema )|| '.' || RTRIM(v_pkgName) || ' TO ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'REVOKE ' || privilege_in || ' ON PACKAGE ' || RTRIM(v_pkgSchema) || '.' || RTRIM(v_pkgName) || ' FROM ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(2); "SET rowCount_inout = rowCount_inout + 1;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   If commitCount > 0 Then
     Print #fileNo,
     Print #fileNo, addTab(3); "IF MOD(rowCount_inout, "; CStr(commitCount); ") = 0 THEN"
     Print #fileNo, addTab(4); "COMMIT;"
     Print #fileNo, addTab(3); "END IF;"
   End If

   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementGrant
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "rowCount_inout,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "FETCH objCursor INTO v_pkgSchema, v_pkgName;"
   Print #fileNo, addTab(1); "END WHILE;"
   Print #fileNo,
   Print #fileNo, addTab(1); "CLOSE objCursor;"

   genSpLogProcExit fileNo, qualProcedureNamePkg, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'pkgNamePattern_in", "'pkgNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for granting access rights on stored procedures
   ' ####################################################################################################################

   Dim qualProcedureNamePro As String
   qualProcedureNamePro = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "PRO")

   printSectionHeader "SP for granting access rights on stored procedures", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNamePro
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas that apply"
   genProcParm fileNo, "IN", "schemaNamePatternOpt_in", g_dbtDbSchemaName, True, "optional user-supplied additional filter for schemas"
   genProcParm fileNo, "IN", "procNamePattern_in", "VARCHAR(80)", True, "determines the procedures that apply"
   genProcParm fileNo, "IN", "procNamePatternOpt_in", "VARCHAR(80)", True, "optional user-supplied additional filter for procedures"
   genProcParm fileNo, "IN", "filterTemplate_in", "VARCHAR(100)", True, "optional filter-pattern to apply (may use placeholder '<%S>', '<%N>')"
   genProcParm fileNo, "IN", "opType_in", "VARCHAR(6)", True, "either 'GRANT' or 'REVOKE'"
   genProcParm fileNo, "IN", "privilege_in", "VARCHAR(100)", True, "privilege to grant resp. revoke"
   genProcParm fileNo, "IN", "granteeType_in", "VARCHAR(6)", True, "either 'USER', 'GROUP' or 'PUBLIC'"
   genProcParm fileNo, "IN", "grantee_in", "VARCHAR(100)", True, "determines the user / group"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "INOUT", "rowCount_inout", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "privCannotBeRevoked", "42504"
   genCondDecl fileNo, "doesNotHavePrivOnObject", "42501"
   genCondDecl fileNo, "failedToExecAsSpecified", "42502"
   genCondDecl fileNo, "unexpectedToken", "42601"
   genCondDecl fileNo, "inoperative", "51024"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_procSchema", g_dbtDbSchemaName, "NULL"
   genVarDecl fileNo, "v_procName", g_dbtDbProcName, "NULL"
   genVarDecl fileNo, "v_csrStmntTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_objCsrStmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE objCursor CURSOR WITH HOLD FOR v_objCsrStmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR privCannotBeRevoked"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotHavePrivOnObject"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR failedToExecAsSpecified"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR unexpectedToken"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR inoperative"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , , , , , , "Grants"

   genSpLogProcEnter fileNo, qualProcedureNamePro, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'procNamePattern_in", "'procNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_csrStmntTxt ="
   Print #fileNo, addTab(2); "'SELECT ' ||"
   Print #fileNo, addTab(3); "'PROCSCHEMA,' ||"
   Print #fileNo, addTab(3); "'SPECIFICNAME ' ||"
   Print #fileNo, addTab(2); "'FROM ' ||"
   Print #fileNo, addTab(3); "'SYSCAT.PROCEDURES ' ||"
   Print #fileNo, addTab(2); "'WHERE ' ||"
   Print #fileNo, addTab(3); "'RTRIM(PROCSCHEMA) LIKE ''' || schemaNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(PROCSCHEMA) LIKE ''' || COALESCE(schemaNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(PROCNAME) LIKE ''' || procNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(PROCNAME) LIKE ''' || COALESCE(procNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(3); "(CASE WHEN COALESCE(filterTemplate_in, '') = '' THEN '' ELSE 'AND (' || REPLACE(REPLACE(filterTemplate_in, '<%S>', 'PROCSCHEMA'), '<%N>', 'PROCNAME') || ') ' END) ||"
   Print #fileNo, addTab(2); "'FOR READ ONLY'"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_objCsrStmnt FROM v_csrStmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "OPEN objCursor;"
   Print #fileNo, addTab(1); "FETCH objCursor INTO v_procSchema, v_procName;"
   Print #fileNo,
   Print #fileNo, addTab(1); "WHILE (SQLCODE = 0) DO"

   Print #fileNo, addTab(2); "IF (opType_in = 'GRANT') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'GRANT ' || privilege_in || ' ON SPECIFIC PROCEDURE ' || RTRIM(v_procSchema )|| '.' || RTRIM(v_procName) || ' TO ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'REVOKE ' || privilege_in || ' ON SPECIFIC PROCEDURE ' || RTRIM(v_procSchema) || '.' || RTRIM(v_procName) || ' FROM ' || granteeType_in || COALESCE(' ' || grantee_in,'') || ' RESTRICT'"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(2); "SET rowCount_inout = rowCount_inout + 1;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   If commitCount > 0 Then
     Print #fileNo,
     Print #fileNo, addTab(3); "IF MOD(rowCount_inout, "; CStr(commitCount); ") = 0 THEN"
     Print #fileNo, addTab(4); "COMMIT;"
     Print #fileNo, addTab(3); "END IF;"
   End If

   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementGrant
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "rowCount_inout,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "FETCH objCursor INTO v_procSchema, v_procName;"
   Print #fileNo, addTab(1); "END WHILE;"
   Print #fileNo,
   Print #fileNo, addTab(1); "CLOSE objCursor;"

   genSpLogProcExit fileNo, qualProcedureNamePro, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'procNamePattern_in", "'procNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for granting access rights on User Defined Functions
   ' ####################################################################################################################

   Dim qualProcedureNameUdf As String
   qualProcedureNameUdf = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "UDF")

   printSectionHeader "SP for granting access rights on user defined functions", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameUdf
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas that apply"
   genProcParm fileNo, "IN", "schemaNamePatternOpt_in", g_dbtDbSchemaName, True, "optional user-supplied additional filter for schemas"
   genProcParm fileNo, "IN", "funcNamePattern_in", "VARCHAR(80)", True, "determines the functions that apply"
   genProcParm fileNo, "IN", "funcNamePatternOpt_in", "VARCHAR(80)", True, "optional user-supplied additional filter for functions"
   genProcParm fileNo, "IN", "filterTemplate_in", "VARCHAR(100)", True, "optional filter-pattern to apply (may use placeholder '<%S>', '<%N>')"
   genProcParm fileNo, "IN", "opType_in", "VARCHAR(6)", True, "either 'GRANT' or 'REVOKE'"
   genProcParm fileNo, "IN", "privilege_in", "VARCHAR(100)", True, "privilege to grant resp. revoke"
   genProcParm fileNo, "IN", "granteeType_in", "VARCHAR(6)", True, "either 'USER', 'GROUP' or 'PUBLIC'"
   genProcParm fileNo, "IN", "grantee_in", "VARCHAR(100)", True, "determines the user / group"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "INOUT", "rowCount_inout", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "privCannotBeRevoked", "42504"
   genCondDecl fileNo, "doesNotHavePrivOnObject", "42501"
   genCondDecl fileNo, "failedToExecAsSpecified", "42502"
   genCondDecl fileNo, "unexpectedToken", "42601"
   genCondDecl fileNo, "inoperative", "51024"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_funcSchema", g_dbtDbSchemaName, "NULL"
   genVarDecl fileNo, "v_funcName", g_dbtDbProcName, "NULL"
   genVarDecl fileNo, "v_csrStmntTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_objCsrStmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE objCursor CURSOR WITH HOLD FOR v_objCsrStmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR privCannotBeRevoked"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotHavePrivOnObject"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR failedToExecAsSpecified"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR unexpectedToken"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR inoperative"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , , , , , , "Grants"

   genSpLogProcEnter fileNo, qualProcedureNameUdf, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'funcNamePattern_in", "'funcNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_csrStmntTxt ="
   Print #fileNo, addTab(2); "'SELECT ' ||"
   Print #fileNo, addTab(3); "'FUNCSCHEMA,' ||"
   Print #fileNo, addTab(3); "'SPECIFICNAME ' ||"
   Print #fileNo, addTab(2); "'FROM ' ||"
   Print #fileNo, addTab(3); "'SYSCAT.FUNCTIONS ' ||"
   Print #fileNo, addTab(2); "'WHERE ' ||"
   Print #fileNo, addTab(3); "'RTRIM(FUNCSCHEMA) LIKE ''' || schemaNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(FUNCSCHEMA) LIKE ''' || COALESCE(schemaNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(FUNCNAME) LIKE ''' || funcNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(FUNCNAME) LIKE ''' || COALESCE(funcNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(3); "(CASE WHEN COALESCE(filterTemplate_in, '') = '' THEN '' ELSE 'AND (' || REPLACE(REPLACE(filterTemplate_in, '<%S>', 'FUNCSCHEMA'), '<%N>', 'FUNCNAME') || ') ' END) ||"
   Print #fileNo, addTab(2); "'FOR READ ONLY'"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_objCsrStmnt FROM v_csrStmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "OPEN objCursor;"
   Print #fileNo, addTab(1); "FETCH objCursor INTO v_funcSchema, v_funcName;"
   Print #fileNo,
   Print #fileNo, addTab(1); "WHILE (SQLCODE = 0) DO"

   Print #fileNo, addTab(2); "IF (opType_in = 'GRANT') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'GRANT ' || privilege_in || ' ON SPECIFIC FUNCTION ' || RTRIM(v_funcSchema )|| '.' || RTRIM(v_funcName) || ' TO ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'REVOKE ' || privilege_in || ' ON SPECIFIC FUNCTION ' || RTRIM(v_funcSchema) || '.' || RTRIM(v_funcName) || ' FROM ' || granteeType_in || COALESCE(' ' || grantee_in,'') || ' RESTRICT'"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(2); "SET rowCount_inout = rowCount_inout + 1;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   If commitCount > 0 Then
     Print #fileNo,
     Print #fileNo, addTab(3); "IF MOD(rowCount_inout, "; CStr(commitCount); ") = 0 THEN"
     Print #fileNo, addTab(4); "COMMIT;"
     Print #fileNo, addTab(3); "END IF;"
   End If

   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementGrant
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "rowCount_inout,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "FETCH objCursor INTO v_funcSchema, v_funcName;"
   Print #fileNo, addTab(1); "END WHILE;"
   Print #fileNo,
   Print #fileNo, addTab(1); "CLOSE objCursor;"

   genSpLogProcExit fileNo, qualProcedureNameUdf, ddlType, , "'schemaNamePattern_in", "'schemaNamePatternOpt_in", _
                             "'funcNamePattern_in", "'funcNamePatternOpt_in", "'filterTemplate_in", "'opType_in", _
                             "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for granting access rights on tablespaces
   ' ####################################################################################################################

   Dim qualProcedureNameTSp As String
   qualProcedureNameTSp = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "TSP")

   printSectionHeader "SP for granting access rights on stored procedures", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTSp
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "tabSpaceNamePattern_in", "VARCHAR(50)", True, "determines the tablespaces that apply"
   genProcParm fileNo, "IN", "tabSpaceNamePatternOpt_in", "VARCHAR(50)", True, "optional user-supplied additional filter for tablespaces"
   genProcParm fileNo, "IN", "filterTemplate_in", "VARCHAR(100)", True, "optional filter-pattern to apply (may use placeholder '<%S>')"
   genProcParm fileNo, "IN", "opType_in", "VARCHAR(6)", True, "either 'GRANT' or 'REVOKE'"
   genProcParm fileNo, "IN", "privilege_in", "VARCHAR(100)", True, "privilege to grant resp. revoke"
   genProcParm fileNo, "IN", "granteeType_in", "VARCHAR(6)", True, "either 'USER', 'GROUP' or 'PUBLIC'"
   genProcParm fileNo, "IN", "grantee_in", "VARCHAR(100)", True, "determines the user / group"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "INOUT", "rowCount_inout", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "privCannotBeRevoked", "42504"
   genCondDecl fileNo, "doesNotHavePrivOnObject", "42501"
   genCondDecl fileNo, "failedToExecAsSpecified", "42502"
   genCondDecl fileNo, "unexpectedToken", "42601"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_tbSpName", "VARCHAR(30)", "NULL"
   genVarDecl fileNo, "v_csrStmntTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_objCsrStmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE objCursor CURSOR WITH HOLD FOR v_objCsrStmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR privCannotBeRevoked"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotHavePrivOnObject"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR failedToExecAsSpecified"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR unexpectedToken"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , , , , , , "Grants"

   genSpLogProcEnter fileNo, qualProcedureNameTSp, ddlType, , "'tabSpaceNamePattern_in", "'tabSpaceNamePatternOpt_in", "'filterTemplate_in", _
                             "'opType_in", "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_csrStmntTxt ="
   Print #fileNo, addTab(2); "'SELECT ' ||"
   Print #fileNo, addTab(3); "'TBSPACE ' ||"
   Print #fileNo, addTab(2); "'FROM ' ||"
   Print #fileNo, addTab(3); "'SYSCAT.TABLESPACES ' ||"
   Print #fileNo, addTab(2); "'WHERE ' ||"
   Print #fileNo, addTab(3); "'RTRIM(TBSPACE) LIKE ''' || tabSpaceNamePattern_in || ''' ' ||"
   Print #fileNo, addTab(4); "'AND ' ||"
   Print #fileNo, addTab(3); "'RTRIM(TBSPACE) LIKE ''' || COALESCE(tabSpaceNamePatternOpt_in, '%') || ''' ' ||"
   Print #fileNo, addTab(3); "(CASE WHEN COALESCE(filterTemplate_in, '') = '' THEN '' ELSE 'AND (' || REPLACE(filterTemplate_in, '<%N>', 'TBSPACE') || ') ' END) ||"
   Print #fileNo, addTab(2); "'FOR READ ONLY'"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "PREPARE v_objCsrStmnt FROM v_csrStmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(1); "OPEN objCursor;"
   Print #fileNo, addTab(1); "FETCH objCursor INTO v_tbSpName;"
   Print #fileNo,
   Print #fileNo, addTab(1); "WHILE (SQLCODE = 0) DO"

   Print #fileNo, addTab(2); "IF (opType_in = 'GRANT') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'GRANT ' || privilege_in || ' OF TABLESPACE ' || RTRIM(v_tbSpName) || ' TO ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'REVOKE ' || privilege_in || ' OF TABLESPACE ' || RTRIM(v_tbSpName) || ' FROM ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(2); "SET rowCount_inout = rowCount_inout + 1;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   If commitCount > 0 Then
     Print #fileNo,
     Print #fileNo, addTab(3); "IF MOD(rowCount_inout, "; CStr(commitCount); ") = 0 THEN"
     Print #fileNo, addTab(4); "COMMIT;"
     Print #fileNo, addTab(3); "END IF;"
   End If

   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementGrant
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "rowCount_inout,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "FETCH objCursor INTO v_tbSpName;"
   Print #fileNo, addTab(1); "END WHILE;"
   Print #fileNo,
   Print #fileNo, addTab(1); "CLOSE objCursor;"

   genSpLogProcExit fileNo, qualProcedureNameTSp, ddlType, , "'tabSpaceNamePattern_in", "'tabSpaceNamePatternOpt_in", "'filterTemplate_in", _
                             "'opType_in", "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for granting access rights on database
   ' ####################################################################################################################

   Dim qualProcedureNameDb As String
   qualProcedureNameDb = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "DB")

   printSectionHeader "SP for granting access rights on database", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameDb
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "opType_in", "VARCHAR(6)", True, "either 'GRANT' or 'REVOKE'"
   genProcParm fileNo, "IN", "privilege_in", "VARCHAR(100)", True, "privilege to grant resp. revoke"
   genProcParm fileNo, "IN", "granteeType_in", "VARCHAR(6)", True, "either 'USER', 'GROUP' or 'PUBLIC'"
   genProcParm fileNo, "IN", "grantee_in", "VARCHAR(100)", True, "determines the user / group"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "INOUT", "rowCount_inout", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "privCannotBeRevoked", "42504"
   genCondDecl fileNo, "doesNotHavePrivOnObject", "42501"
   genCondDecl fileNo, "failedToExecAsSpecified", "42502"
   genCondDecl fileNo, "unexpectedToken", "42601"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR privCannotBeRevoked"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR doesNotHavePrivOnObject"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR failedToExecAsSpecified"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR unexpectedToken"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , , , , , , "Grants"

   genSpLogProcEnter fileNo, qualProcedureNameDb, ddlType, , "'opType_in", "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "IF (opType_in = 'GRANT') THEN"
   Print #fileNo, addTab(2); "SET v_stmntTxt ="
   Print #fileNo, addTab(3); "'GRANT ' || privilege_in || ' ON DATABASE TO ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "SET v_stmntTxt ="
   Print #fileNo, addTab(3); "'REVOKE ' || privilege_in || ' ON DATABASE FROM ' || granteeType_in || COALESCE(' ' || grantee_in,'')"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(1); "SET rowCount_inout = rowCount_inout + 1;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   If commitCount > 0 Then
     Print #fileNo,
     Print #fileNo, addTab(2); "IF MOD(rowCount_inout, "; CStr(commitCount); ") = 0 THEN"
     Print #fileNo, addTab(3); "COMMIT;"
     Print #fileNo, addTab(2); "END IF;"
   End If

   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "store statement in temporary table", 1
   Print #fileNo, addTab(1); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); tempTabNameStatementGrant
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SEQNO,"
   Print #fileNo, addTab(3); "STATEMENT"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "rowCount_inout,"
   Print #fileNo, addTab(3); "v_stmntTxt"
   Print #fileNo, addTab(2); ");"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameDb, ddlType, , "'opType_in", "'privilege_in", "'granteeType_in", "'grantee_in", "mode_in", "rowCount_inout"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genDdlSetgrants2( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   Dim tempTabNameStatementGrant As String
   tempTabNameStatementGrant = tempTabNameStatement & "Grants"

   Dim qualProcedureNameSchema As String
   qualProcedureNameSchema = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "SCH")
   Dim qualProcedureNameTab As String
   qualProcedureNameTab = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "TAB")
   Dim qualProcedureNameSeq As String
   qualProcedureNameSeq = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "SEQ")
   Dim qualProcedureNamePkg As String
   qualProcedureNamePkg = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "PKG")
   Dim qualProcedureNamePro As String
   qualProcedureNamePro = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "PRO")
   Dim qualProcedureNameUdf As String
   qualProcedureNameUdf = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "UDF")
   Dim qualProcedureNameTSp As String
   qualProcedureNameTSp = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "TSP")
   Dim qualProcedureNameDb As String
   qualProcedureNameDb = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "DB")

   On Error GoTo ErrorExit

   ' ####################################################################################################################
   ' #    SP for granting access rights (by environment / filtered)
   ' ####################################################################################################################

   Dim qualProcedureNameGrantByEnvFltr As String
   qualProcedureNameGrantByEnvFltr = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "ByEnvFltr", eondmNone)

   printSectionHeader "SP for granting access rights (by environment / filtered)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameGrantByEnvFltr
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "environment_in", "CHAR(1)", True, "refers to column 'environment' in table 'DbPrivileges'"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "schemaFilter_in", g_dbtDbSchemaName, True, "optional user-supplied additional filter for schemas"
   genProcParm fileNo, "IN", "objFilter_in", "VARCHAR(80)", True, "optional user-supplied additional filter for objects"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_typeSelector", "CHAR(1)", "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_grantee", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_enforcedOpType", "VARCHAR(6)", "NULL"
   genVarDecl fileNo, "v_filterCond", "VARCHAR(100)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE cfgParmCursor CURSOR FOR v_stmnt;"

   genDdlForTempStatement fileNo, 1, True, , , , , , "Grants"

   genSpLogProcEnter fileNo, qualProcedureNameGrantByEnvFltr, ddlType, , "'environment_in", "mode_in", "'schemaFilter_in", "'objFilter_in", "rowCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "check if we want to enforce 'REVOKE'"
   Print #fileNo, addTab(1); "IF mode_in >= 10 THEN"
   Print #fileNo, addTab(2); "SET mode_in = mode_in - 10;"
   Print #fileNo, addTab(2); "SET v_enforcedOpType = 'REVOKE';"
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF schemaFilter_in IS NULL AND objFilter_in IS NULL THEN"
   genProcSectionHeader fileNo, "process privileges on DB", 2, True
   Print #fileNo, addTab(2); "FOR dbLoop AS dbCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "COALESCE(v_enforcedOpType, UPPER(OPTYPE)) AS c_opType,"
   Print #fileNo, addTab(4); "UPPER(GRANTEETYPE)                        AS c_granteeType,"
   Print #fileNo, addTab(4); "UPPER(LTRIM(RTRIM(GRANTEE)))              AS c_grantee,"
   Print #fileNo, addTab(4); "UPPER(PRIVILEGE)                          AS c_privilege"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameDbPrivileges
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "environment_in IS NULL"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "ENVIRONMENT IS NULL"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "UPPER(ENVIRONMENT) = UPPER(environment_in)"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "OBJECTTYPE = 'DATABASE'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(v_enforcedOpType IS NULL OR UPPER(OPTYPE) = 'GRANT')"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "SEQUENCENO ASC"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_grantee = c_grantee;"
   genProcSectionHeader fileNo, "check for grantee to resolve in DBM config", 3
   Print #fileNo, addTab(3); "IF LEFT(v_grantee, 1) = '<' THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || REPLACE(REPLACE(v_grantee, '<',''), '>', '') || ' AS CHAR(100))) FROM TABLE(SYSFUN.GET_DBM_CONFIG()) AS DBMCFG WHERE DBMCONFIG_TYPE = 0';"
   Print #fileNo,
   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(4); "OPEN cfgParmCursor;"
   Print #fileNo,
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "cfgParmCursor"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_grantee"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "CLOSE cfgParmCursor WITH RELEASE;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF (COALESCE(v_grantee, '') <> '') AND c_granteeType <> 'PUBLIC' THEN"
   Print #fileNo, addTab(4); "CALL "; qualProcedureNameDb; "(c_opType, c_privilege, c_granteeType, v_grantee, mode_in, rowCount_out);"
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "IF schemaFilter_in IS NULL THEN"
   genProcSectionHeader fileNo, "process privileges on tablespaces", 2, True
   Print #fileNo, addTab(2); "FOR tsLoop AS tsCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "COALESCE(v_enforcedOpType, UPPER(OPTYPE)) AS c_opType,"
   Print #fileNo, addTab(4); "UPPER(OBJECTNAME)                         AS c_objectName,"
   Print #fileNo, addTab(4); "UPPER(FILTER)                             AS c_filter,"
   Print #fileNo, addTab(4); "UPPER(GRANTEETYPE)                        AS c_granteeType,"
   Print #fileNo, addTab(4); "UPPER(LTRIM(RTRIM(GRANTEE)))              AS c_grantee,"
   Print #fileNo, addTab(4); "UPPER(PRIVILEGE)                          AS c_privilege"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameDbPrivileges
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "environment_in IS NULL"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "ENVIRONMENT IS NULL"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "UPPER(ENVIRONMENT) = UPPER(environment_in)"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "OBJECTTYPE = 'TABLESPACE'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(v_enforcedOpType IS NULL OR UPPER(OPTYPE) = 'GRANT')"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "SEQUENCENO ASC"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_grantee = c_grantee;"
   genProcSectionHeader fileNo, "check for grantee to resolve in DBM config", 3
   Print #fileNo, addTab(3); "IF LEFT(v_grantee, 1) = '<' THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || REPLACE(REPLACE(v_grantee, '<',''), '>', '') || ' AS CHAR(100))) FROM TABLE(SYSFUN.GET_DBM_CONFIG()) AS DBMCFG WHERE DBMCONFIG_TYPE = 0';"
   Print #fileNo,
   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(4); "OPEN cfgParmCursor;"
   Print #fileNo,
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "cfgParmCursor"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_grantee"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "CLOSE cfgParmCursor WITH RELEASE;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF (COALESCE(v_grantee, '') <> '') AND c_granteeType <> 'PUBLIC' THEN"
   Print #fileNo, addTab(4); "CALL "; qualProcedureNameTSp; "(c_objectName, objFilter_in, c_filter, c_opType, c_privilege, c_granteeType, v_grantee, mode_in, rowCount_out);"
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF objFilter_in IS NULL THEN"
   genProcSectionHeader fileNo, "process privileges on schemas", 2, True
   Print #fileNo, addTab(2); "FOR schLoop AS schCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "COALESCE(v_enforcedOpType, UPPER(OPTYPE)) AS c_opType,"
   Print #fileNo, addTab(4); "UPPER(SCHEMANAME)                         AS c_schemaName,"
   Print #fileNo, addTab(4); "UPPER(FILTER)                             AS c_filter,"
   Print #fileNo, addTab(4); "UPPER(GRANTEETYPE)                        AS c_granteeType,"
   Print #fileNo, addTab(4); "UPPER(LTRIM(RTRIM(GRANTEE)))              AS c_grantee,"
   Print #fileNo, addTab(4); "UPPER(PRIVILEGE)                          AS c_privilege,"
   Print #fileNo, addTab(4); "WITHGRANT"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameDbPrivileges
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "environment_in IS NULL"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "ENVIRONMENT IS NULL"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "UPPER(ENVIRONMENT) = UPPER(environment_in)"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "OBJECTTYPE = 'SCHEMA'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(v_enforcedOpType IS NULL OR UPPER(OPTYPE) = 'GRANT')"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "SEQUENCENO ASC"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_grantee = c_grantee;"
   genProcSectionHeader fileNo, "check for grantee to resolve in DBM config", 3
   Print #fileNo, addTab(3); "IF LEFT(v_grantee, 1) = '<' THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || REPLACE(REPLACE(v_grantee, '<',''), '>', '') || ' AS CHAR(100))) FROM TABLE(SYSFUN.GET_DBM_CONFIG()) AS DBMCFG WHERE DBMCONFIG_TYPE = 0';"
   Print #fileNo,
   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(4); "OPEN cfgParmCursor;"
   Print #fileNo,
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "cfgParmCursor"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_grantee"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "CLOSE cfgParmCursor WITH RELEASE;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF (COALESCE(v_grantee, '') <> '') AND c_granteeType <> 'PUBLIC' THEN"
   Print #fileNo, addTab(4); "CALL "; qualProcedureNameSchema; "(c_schemaName, schemaFilter_in, c_filter, c_opType, c_privilege, c_granteeType, v_grantee, WITHGRANT, mode_in, rowCount_out);"
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "process privileges on tables"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "COALESCE(v_enforcedOpType, UPPER(OPTYPE)) AS c_opType,"
   Print #fileNo, addTab(3); "UPPER(OBJECTTYPE)                         AS c_objectType,"
   If ddlType = edtLdm Then
     Print #fileNo, addTab(3); "'%'                                       AS c_schemaName,"
   Else
     Print #fileNo, addTab(3); "UPPER(SCHEMANAME)                         AS c_schemaName,"
   End If
   Print #fileNo, addTab(3); "UPPER(OBJECTNAME)                         AS c_objectName,"
   Print #fileNo, addTab(3); "UPPER(FILTER)                             AS c_filter,"
   Print #fileNo, addTab(3); "UPPER(GRANTEETYPE)                        AS c_granteeType,"
   Print #fileNo, addTab(3); "UPPER(LTRIM(RTRIM(GRANTEE)))              AS c_grantee,"
   Print #fileNo, addTab(3); "UPPER(PRIVILEGE)                          AS c_privilege"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDbPrivileges
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "environment_in IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "ENVIRONMENT IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "UPPER(ENVIRONMENT) = UPPER(environment_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "OBJECTTYPE = 'TABLE'"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "OBJECTTYPE = 'VIEW'"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "OBJECTTYPE = 'ALIAS'"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(v_enforcedOpType IS NULL OR UPPER(OPTYPE) = 'GRANT')"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "SEQUENCENO ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_grantee = c_grantee;"
   genProcSectionHeader fileNo, "check for grantee to resolve in DBM config", 2
   Print #fileNo, addTab(2); "IF LEFT(v_grantee, 1) = '<' THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || REPLACE(REPLACE(v_grantee, '<',''), '>', '') || ' AS CHAR(100))) FROM TABLE(SYSFUN.GET_DBM_CONFIG()) AS DBMCFG WHERE DBMCONFIG_TYPE = 0';"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(3); "OPEN cfgParmCursor;"
   Print #fileNo,
   Print #fileNo, addTab(3); "FETCH"
   Print #fileNo, addTab(4); "cfgParmCursor"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_grantee"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "CLOSE cfgParmCursor WITH RELEASE;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF (COALESCE(v_grantee, '') <> '') AND c_granteeType <> 'PUBLIC' THEN"
   Print #fileNo, addTab(3); "SET v_typeSelector = LEFT(c_objectType,1);"
   Print #fileNo, addTab(3); "CALL "; qualProcedureNameTab; "(v_typeSelector, c_schemaName, schemaFilter_in, c_objectName, objFilter_in, c_filter, c_opType, c_privilege, c_granteeType, c_grantee, mode_in, rowCount_out);"
   Print #fileNo, addTab(3); "COMMIT;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "process privileges on sequences"
   Print #fileNo, addTab(1); "FOR seqLoop AS seqCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "COALESCE(v_enforcedOpType, UPPER(OPTYPE)) AS c_opType,"
   If ddlType = edtLdm Then
     Print #fileNo, addTab(3); "'%'                                       AS c_schemaName,"
   Else
     Print #fileNo, addTab(3); "UPPER(SCHEMANAME)                         AS c_schemaName,"
   End If
   Print #fileNo, addTab(3); "UPPER(OBJECTNAME)                         AS c_objectName,"
   Print #fileNo, addTab(3); "UPPER(FILTER)                             AS c_filter,"
   Print #fileNo, addTab(3); "UPPER(GRANTEETYPE)                        AS c_granteeType,"
   Print #fileNo, addTab(3); "UPPER(LTRIM(RTRIM(GRANTEE)))              AS c_grantee,"
   Print #fileNo, addTab(3); "UPPER(PRIVILEGE)                          AS c_privilege"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDbPrivileges
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "environment_in IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "ENVIRONMENT IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "UPPER(ENVIRONMENT) = UPPER(environment_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(3); "OBJECTTYPE = 'SEQUENCE'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(v_enforcedOpType IS NULL OR UPPER(OPTYPE) = 'GRANT')"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "SEQUENCENO ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_grantee = c_grantee;"
   genProcSectionHeader fileNo, "check for grantee to resolve in DBM config", 2
   Print #fileNo, addTab(2); "IF LEFT(v_grantee, 1) = '<' THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || REPLACE(REPLACE(v_grantee, '<',''), '>', '') || ' AS CHAR(100))) FROM TABLE(SYSFUN.GET_DBM_CONFIG()) AS DBMCFG WHERE DBMCONFIG_TYPE = 0';"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(3); "OPEN cfgParmCursor;"
   Print #fileNo,
   Print #fileNo, addTab(3); "FETCH"
   Print #fileNo, addTab(4); "cfgParmCursor"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_grantee"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "CLOSE cfgParmCursor WITH RELEASE;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF (COALESCE(v_grantee, '') <> '') AND c_granteeType <> 'PUBLIC' THEN"
   Print #fileNo, addTab(3); "CALL "; qualProcedureNameSeq; "(c_schemaName, schemaFilter_in, c_objectName, objFilter_in, c_filter, c_opType, c_privilege, c_granteeType, v_grantee, mode_in, rowCount_out);"
   Print #fileNo, addTab(3); "COMMIT;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "process privileges on procedures"
   Print #fileNo, addTab(1); "FOR proLoop AS proCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "COALESCE(v_enforcedOpType, UPPER(OPTYPE)) AS c_opType,"
   If ddlType = edtLdm Then
     Print #fileNo, addTab(3); "'%'                                       AS c_schemaName,"
   Else
     Print #fileNo, addTab(3); "UPPER(SCHEMANAME)                         AS c_schemaName,"
   End If
   Print #fileNo, addTab(3); "UPPER(OBJECTNAME)                         AS c_objectName,"
   Print #fileNo, addTab(3); "UPPER(FILTER)                             AS c_filter,"
   Print #fileNo, addTab(3); "UPPER(GRANTEETYPE)                        AS c_granteeType,"
   Print #fileNo, addTab(3); "UPPER(LTRIM(RTRIM(GRANTEE)))              AS c_grantee,"
   Print #fileNo, addTab(3); "UPPER(PRIVILEGE)                          AS c_privilege"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDbPrivileges
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "environment_in IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "ENVIRONMENT IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "UPPER(ENVIRONMENT) = UPPER(environment_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(3); "OBJECTTYPE = 'PROCEDURE'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(v_enforcedOpType IS NULL OR UPPER(OPTYPE) = 'GRANT')"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "SEQUENCENO ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_grantee = c_grantee;"
   genProcSectionHeader fileNo, "check for grantee to resolve in DBM config", 2
   Print #fileNo, addTab(2); "IF LEFT(v_grantee, 1) = '<' THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || REPLACE(REPLACE(v_grantee, '<',''), '>', '') || ' AS CHAR(100))) FROM TABLE(SYSFUN.GET_DBM_CONFIG()) AS DBMCFG WHERE DBMCONFIG_TYPE = 0';"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(3); "OPEN cfgParmCursor;"
   Print #fileNo,
   Print #fileNo, addTab(3); "FETCH"
   Print #fileNo, addTab(4); "cfgParmCursor"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_grantee"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "CLOSE cfgParmCursor WITH RELEASE;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF (COALESCE(v_grantee, '') <> '') AND c_granteeType <> 'PUBLIC' THEN"
   Print #fileNo, addTab(3); "CALL "; qualProcedureNamePro; "(c_schemaName, schemaFilter_in, c_objectName, objFilter_in, c_filter, c_opType, c_privilege, c_granteeType, v_grantee, mode_in, rowCount_out);"
   Print #fileNo, addTab(3); "COMMIT;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "process privileges on functions"
   Print #fileNo, addTab(1); "FOR udfLoop AS udfCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "COALESCE(v_enforcedOpType, UPPER(OPTYPE)) AS c_opType,"
   If ddlType = edtLdm Then
     Print #fileNo, addTab(3); "'%'                                       AS c_schemaName,"
   Else
     Print #fileNo, addTab(3); "UPPER(SCHEMANAME)                         AS c_schemaName,"
   End If
   Print #fileNo, addTab(3); "UPPER(OBJECTNAME)                         AS c_objectName,"
   Print #fileNo, addTab(3); "UPPER(FILTER)                             AS c_filter,"
   Print #fileNo, addTab(3); "UPPER(GRANTEETYPE)                        AS c_granteeType,"
   Print #fileNo, addTab(3); "UPPER(LTRIM(RTRIM(GRANTEE)))              AS c_grantee,"
   Print #fileNo, addTab(3); "UPPER(PRIVILEGE)                          AS c_privilege"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDbPrivileges
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "environment_in IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "ENVIRONMENT IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "UPPER(ENVIRONMENT) = UPPER(environment_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(3); "OBJECTTYPE = 'FUNCTION'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(v_enforcedOpType IS NULL OR UPPER(OPTYPE) = 'GRANT')"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "SEQUENCENO ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_grantee = c_grantee;"
   genProcSectionHeader fileNo, "check for grantee to resolve in DBM config", 2
   Print #fileNo, addTab(2); "IF LEFT(v_grantee, 1) = '<' THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || REPLACE(REPLACE(v_grantee, '<',''), '>', '') || ' AS CHAR(100))) FROM TABLE(SYSFUN.GET_DBM_CONFIG()) AS DBMCFG WHERE DBMCONFIG_TYPE = 0';"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(3); "OPEN cfgParmCursor;"
   Print #fileNo,
   Print #fileNo, addTab(3); "FETCH"
   Print #fileNo, addTab(4); "cfgParmCursor"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_grantee"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "CLOSE cfgParmCursor WITH RELEASE;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF (COALESCE(v_grantee, '') <> '') AND c_granteeType <> 'PUBLIC' THEN"
   Print #fileNo, addTab(3); "CALL "; qualProcedureNameUdf; "(c_schemaName, schemaFilter_in, c_objectName, objFilter_in, c_filter, c_opType, c_privilege, c_granteeType, v_grantee, mode_in, rowCount_out);"
   Print #fileNo, addTab(3); "COMMIT;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "process privileges on packages"
   Print #fileNo, addTab(1); "FOR pkgLoop AS pkgCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "COALESCE(v_enforcedOpType, UPPER(OPTYPE)) AS c_opType,"
   If ddlType = edtLdm Then
     Print #fileNo, addTab(3); "'%'                                       AS c_schemaName,"
   Else
     Print #fileNo, addTab(3); "UPPER(SCHEMANAME)                         AS c_schemaName,"
   End If
   Print #fileNo, addTab(3); "UPPER(OBJECTNAME)                         AS c_objectName,"
   Print #fileNo, addTab(3); "UPPER(FILTER)                             AS c_filter,"
   Print #fileNo, addTab(3); "UPPER(GRANTEETYPE)                        AS c_granteeType,"
   Print #fileNo, addTab(3); "UPPER(LTRIM(RTRIM(GRANTEE)))              AS c_grantee,"
   Print #fileNo, addTab(3); "UPPER(PRIVILEGE)                          AS c_privilege"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDbPrivileges
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "environment_in IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "ENVIRONMENT IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "UPPER(ENVIRONMENT) = UPPER(environment_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "OBJECTTYPE = 'PACKAGE'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(v_enforcedOpType IS NULL OR UPPER(OPTYPE) = 'GRANT')"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "SEQUENCENO ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_grantee = c_grantee;"
   genProcSectionHeader fileNo, "check for grantee to resolve in DBM config", 2
   Print #fileNo, addTab(2); "IF LEFT(v_grantee, 1) = '<' THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || REPLACE(REPLACE(v_grantee, '<',''), '>', '') || ' AS CHAR(100))) FROM TABLE(SYSFUN.GET_DBM_CONFIG()) AS DBMCFG WHERE DBMCONFIG_TYPE = 0';"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(3); "OPEN cfgParmCursor;"
   Print #fileNo,
   Print #fileNo, addTab(3); "FETCH"
   Print #fileNo, addTab(4); "cfgParmCursor"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_grantee"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "CLOSE cfgParmCursor WITH RELEASE;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF (COALESCE(v_grantee, '') <> '') AND c_granteeType <> 'PUBLIC' THEN"
   Print #fileNo, addTab(3); "CALL "; qualProcedureNamePkg; "(c_schemaName, schemaFilter_in, c_objectName, objFilter_in, c_filter, c_opType, c_privilege, c_granteeType, v_grantee, mode_in, rowCount_out);"
   Print #fileNo, addTab(3); "COMMIT;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementGrant
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameGrantByEnvFltr, ddlType, , "'environment_in", "mode_in", "'schemaFilter_in", "'objFilter_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for granting access rights (by environment)
   ' ####################################################################################################################

   Dim qualProcedureNameGrantByEnv As String
   qualProcedureNameGrantByEnv = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "ByEnv", eondmNone)

   printSectionHeader "SP for granting access rights (by environment)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameGrantByEnv
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "environment_in", "CHAR(1)", True, "refers to column 'environment' in table 'DbPrivileges'"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
   genSpLogProcEnter fileNo, qualProcedureNameGrantByEnv, ddlType, , "'environment_in", "mode_in", "rowCount_out"

   Print #fileNo, addTab(2); "CALL "; qualProcedureNameGrantByEnvFltr; "(environment_in, mode_in, NULL, NULL, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameGrantByEnv, ddlType, , "'environment_in", "mode_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for revoking access rights (by environment / filtered)
   ' ####################################################################################################################

   Dim qualProcedureNameRevokeByEnvFltr As String
   qualProcedureNameRevokeByEnvFltr = genQualProcName(g_sectionIndexDbAdmin, spnRevoke, ddlType, , , , "ByEnvFltr", eondmNone)

   printSectionHeader "SP for revoking access rights (by environment)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRevokeByEnvFltr
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "environment_in", "CHAR(1)", True, "refers to column 'environment' in table 'DbPrivileges'"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "schemaFilter_in", g_dbtDbSchemaName, True, "optional additional filter for schemas"
   genProcParm fileNo, "IN", "objFilter_in", "VARCHAR(50)", True, "optional additional filter for objects"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
   genSpLogProcEnter fileNo, qualProcedureNameRevokeByEnvFltr, ddlType, , "'environment_in", "mode_in", "'schemaFilter_in", "'objFilter_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET mode_in = mode_in + 10;"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameGrantByEnvFltr; "(environment_in, mode_in, schemaFilter_in, objFilter_in, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameRevokeByEnvFltr, ddlType, , "'environment_in", "mode_in", "'schemaFilter_in", "'objFilter_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for revoking access rights (by environment)
   ' ####################################################################################################################

   Dim qualProcedureNameRevokeByEnv As String
   qualProcedureNameRevokeByEnv = genQualProcName(g_sectionIndexDbAdmin, spnRevoke, ddlType, , , , "ByEnv", eondmNone)

   printSectionHeader "SP for revoking access rights (by environment)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRevokeByEnv
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "environment_in", "CHAR(1)", True, "refers to column 'environment' in table 'DbPrivileges'"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
   genSpLogProcEnter fileNo, qualProcedureNameRevokeByEnv, ddlType, , "'environment_in", "mode_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET mode_in = mode_in + 10;"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameGrantByEnvFltr; "(environment_in, mode_in, NULL, NULL, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameRevokeByEnv, ddlType, , "'environment_in", "mode_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for granting access rights (filtered)
   ' ####################################################################################################################

   Dim qualProcedureNameGrantFltr As String
   qualProcedureNameGrantFltr = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "Fltr", eondmNone)

   printSectionHeader "SP for granting access rights", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameGrantFltr
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "schemaFilter_in", g_dbtDbSchemaName, True, "optional additional filter for schemas"
   genProcParm fileNo, "IN", "objFilter_in", "VARCHAR(80)", True, "optional additional filter for objects"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, qualProcedureNameGrantFltr, ddlType, , "mode_in", "'schemaFilter_in", "'objFilter_in", "rowCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameGrantByEnvFltr; "(NULL, mode_in, schemaFilter_in, objFilter_in, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameGrantFltr, ddlType, , "mode_in", "'schemaFilter_in", "'objFilter_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for granting access rights
   ' ####################################################################################################################

   Dim qualProcedureNameGrant As String
   qualProcedureNameGrant = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType)

   printSectionHeader "SP for granting access rights", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameGrant
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of GRANT-/REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True
   genSpLogProcEnter fileNo, qualProcedureNameGrant, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameGrantByEnvFltr; "(NULL, mode_in, NULL, NULL, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameGrant, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for revoking access rights (filtered)
   ' ####################################################################################################################

   Dim qualProcedureNameRevokeFltr As String
   qualProcedureNameRevokeFltr = genQualProcName(g_sectionIndexDbAdmin, spnRevoke, ddlType, , , , "Fltr", eondmNone)

   printSectionHeader "SP for revoking access rights", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRevokeFltr
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "schemaFilter_in", g_dbtDbSchemaName, True, "optional additional filter for schemas"
   genProcParm fileNo, "IN", "objFilter_in", "VARCHAR(50)", True, "optional additional filter for objects"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
   genSpLogProcEnter fileNo, qualProcedureNameRevokeFltr, ddlType, , "mode_in", "'schemaFilter_in", "'objFilter_in", "rowCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET mode_in = mode_in + 10;"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameGrantFltr; "(mode_in, schemaFilter_in, objFilter_in, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameRevokeFltr, ddlType, , "mode_in", "'schemaFilter_in", "'objFilter_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### IF IVK ###
 Private Sub genDdlAutoDeploy( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   ' ####################################################################################################################
   ' #    Utility-SP determining the data fixes deployable based on content in tables DATAFIX, DATAFIXDEPENDENCY and DATAFIXIGNORED
   ' ####################################################################################################################

   Dim qualProcedureNameGetDataFixesToDeploy As String
   qualProcedureNameGetDataFixesToDeploy = genQualProcName(g_sectionIndexDbAdmin, spnGetDataFixesToDeploy, ddlType)

   printSectionHeader "Utility-SP determining the data fixes deployable based on content in tables DATAFIX, DATAFIXDEPENDENCY and DATAFIXIGNORED", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameGetDataFixesToDeploy
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "OUT", "fixCount_out", "INTEGER", False, "number of fixes satisfying preconditions for deployment"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_condValue", g_dbtBoolean, "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmntCond", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE c_cond CURSOR FOR v_stmntCond;"
 
   Dim qualTabNameDataFixesToDeploy As String
   qualTabNameDataFixesToDeploy = "SESSION.DataFixesToDeploy"
   Dim qualTabNameTempConditionValue As String
   qualTabNameTempConditionValue = "SESSION.ConditionValue"
 
   genProcSectionHeader fileNo, "temporary table for datafixes to deploy"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); qualTabNameDataFixesToDeploy
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "version   VARCHAR(20),"
   Print #fileNo, addTab(2); "revision  VARCHAR(20),"
   Print #fileNo, addTab(2); "ignoreMe  "; g_dbtBoolean
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True
 
   genProcSectionHeader fileNo, "temporary table for condition values"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); qualTabNameTempConditionValue
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "seqNo     INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),"
   Print #fileNo, addTab(2); "condValue "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "version   VARCHAR(20),"
   Print #fileNo, addTab(2); "revision  VARCHAR(20),"
   Print #fileNo, addTab(2); "CONDITION VARCHAR(2048)"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True
 
   genSpLogProcEnter fileNo, qualProcedureNameGetDataFixesToDeploy, ddlType, , "fixCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET fixCount_out = 0;"
 
   genProcSectionHeader fileNo, "initialize temp table with all available datafixes"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNameDataFixesToDeploy
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "version,"
   Print #fileNo, addTab(2); "revision,"
   Print #fileNo, addTab(2); "ignoreMe"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT DISTINCT"
   Print #fileNo, addTab(2); "F.VERSION,"
   Print #fileNo, addTab(2); "F.REVISION,"
   Print #fileNo, addTab(2); "0"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameDataFix; " F"
   Print #fileNo, addTab(1); "LEFT OUTER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameDataFixIgnored; " I"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "F.VERSION = I.VERSION"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE(F.REVISION, '') = COALESCE(I.REVISION, '')"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "I.VERSION IS NULL"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader fileNo, "loop over Data Fixes to identify fixes satisfying preconditions"
   Print #fileNo, addTab(1); "FOR fixLoop AS fixCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "version  AS c_version,"
   Print #fileNo, addTab(3); "revision AS c_revision"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameDataFixesToDeploy
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "VERSION,"
   Print #fileNo, addTab(3); "REVISION"
   Print #fileNo, addTab(2); "FOR UPDATE"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "condCheckLoop:"
   Print #fileNo, addTab(2); "FOR condLoop AS condCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "PRECONDITION AS c_precondition"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameDataFixPrecondition; " C"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "C.VERSION = c_version"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(C.REVISION, '') = COALESCE(c_revision, '')"
   Print #fileNo, addTab(2); "DO"
 
   genProcSectionHeader fileNo, "determine condition value of 'c_precondition'", 3, True
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'VALUES(CASE WHEN ' || c_precondition || ' THEN 1 ELSE 0 END)';"
   Print #fileNo, addTab(3); "PREPARE v_stmntCond FROM v_stmntTxt;"
   Print #fileNo, addTab(3); "OPEN c_cond;"
   Print #fileNo, addTab(3); "FETCH c_cond INTO v_condValue;"
   Print #fileNo, addTab(3); "CLOSE c_cond WITH RELEASE;"
 
   genProcSectionHeader fileNo, "keep track of condition evaluated", 3
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); qualTabNameTempConditionValue
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "condValue,"
   Print #fileNo, addTab(4); "version,"
   Print #fileNo, addTab(4); "revision,"
   Print #fileNo, addTab(4); "condition"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_condValue,"
   Print #fileNo, addTab(4); "c_version,"
   Print #fileNo, addTab(4); "c_revision,"
   Print #fileNo, addTab(4); "c_precondition"
   Print #fileNo, addTab(3); ");"
 
   genProcSectionHeader fileNo, "if this datafix does not satisfy condtion it must be ignored", 3
   Print #fileNo, addTab(3); "IF v_condValue = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(4); "UPDATE"
   Print #fileNo, addTab(5); qualTabNameDataFixesToDeploy
   Print #fileNo, addTab(4); "SET"
   Print #fileNo, addTab(5); "ignoreMe = "; gc_dbTrue
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "CURRENT OF fixCursor"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "LEAVE condCheckLoop;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "count number of fixes to deploy"
   Print #fileNo, addTab(1); "SET fixCount_out = (SELECT COUNT(*) FROM "; qualTabNameDataFixesToDeploy; " WHERE ignoreMe = 0);"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UPDATE / DELETE-Trigger prohibiting modification of 'IBM'-owned records defining 'FixIgnored'-records
   ' ####################################################################################################################

   Dim qualTriggerName As String
   Dim qualTabName As String

   Dim thisClassName As String
   Dim thisClassIndex As Integer
   Dim i As Integer
   For i = 1 To 2
     thisClassIndex = IIf(i = 1, g_classIndexDataFixIgnored, g_classIndexDdlFixIgnored)
     thisClassName = IIf(i = 1, clnDataFixIgnored, clnDdlFixIgnored)

     qualTriggerName = genQualTriggerNameByClassIndex(thisClassIndex, ddlType, , , , , , , , "UPD", eondmSuffix)
     qualTabName = genQualTabNameByClassIndex(thisClassIndex, ddlType)

     printSectionHeader "UPDATE-Trigger prohibiting modification of 'IBM'-owned records in table """ & qualTabName & """", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "NO CASCADE BEFORE UPDATE ON"
     Print #fileNo, addTab(1); qualTabName
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
     Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader fileNo, "verify that update does not modify an IBM-owned record", 1, True
     Print #fileNo, addTab(1); "IF "; gc_oldRecordName; ".REASON LIKE 'IBM:%' AND "; gc_newRecordName; ".REASON NOT LIKE 'ibm:%' THEN"
     genSignalDdl "updateNotAllowed", fileNo, 2, thisClassName
     Print #fileNo, addTab(1); "END IF;"
 
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
 
     qualTriggerName = genQualTriggerNameByClassIndex(thisClassIndex, ddlType, , , , , , , , "DEL", eondmSuffix)

     printSectionHeader "DELETE-Trigger prohibiting delete of 'IBM'-owned records in table """ & qualTabName & """", fileNo

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "NO CASCADE BEFORE DELETE ON"
     Print #fileNo, addTab(1); qualTabName
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader fileNo, "verify that delete does not involve an IBM-owned record", 1, True
     Print #fileNo, addTab(1); "IF "; gc_oldRecordName; ".REASON LIKE 'IBM:%' THEN"
     genSignalDdl "deleteNotAllowed", fileNo, 2, thisClassName
     Print #fileNo, addTab(1); "END IF;"

     Print #fileNo, "END"
     Print #fileNo, gc_sqlCmdDelim
   Next i
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### ENDIF IVK ###
 Private Sub genDdlRedirRestore( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

   Dim qualFuncNameLastStrElem As String
   qualFuncNameLastStrElem = genQualFuncName(g_sectionIndexMeta, udfnLastStrElem, ddlType)
 
   ' ####################################################################################################################
   ' #    View retrieving a 'redirected-restore script'
   ' ####################################################################################################################
 
   Dim qualViewNameRedirRestoreScript As String
   qualViewNameRedirRestoreScript = genQualViewName(g_sectionIndexDbAdmin, vnRedirectedRestoreScript, vnsRedirectedRestoreScript, ddlType)
 
   printSectionHeader "View retrieving a 'redirected-restore script'", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameRedirRestoreScript
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "stmnt"
   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "WITH"
   Print #fileNo, addTab(1); "V_TbSpaces"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "tbSpace,"
   Print #fileNo, addTab(1); "tbSpaceId,"
   Print #fileNo, addTab(1); "seqno"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "TBSPACE,"
   Print #fileNo, addTab(2); "TBSPACEID,"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY TBSPACEID ASC)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.TABLESPACES"
   Print #fileNo, addTab(0); "),"
   Print #fileNo, addTab(1); "V_TbSpaceContainer"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "tbSpaceId,"
   Print #fileNo, addTab(1); "tbSpace,"
   Print #fileNo, addTab(1); "containerType,"
   Print #fileNo, addTab(1); "containerPath,"
   Print #fileNo, addTab(1); "totalPages,"
   Print #fileNo, addTab(1); "containerName,"
   Print #fileNo, addTab(1); "seqno"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "TABLESPACE_ID,"
   Print #fileNo, addTab(2); "TABLESPACE_NAME,"
   Print #fileNo, addTab(2); "(CASE WHEN CONTAINER_TYPE = 0 THEN 'PATH' WHEN CONTAINER_TYPE IN (2,6) THEN 'FILE' ELSE 'DEVICE' END),"
   Print #fileNo, addTab(2); "CONTAINER_NAME,"
   Print #fileNo, addTab(2); "TOTAL_PAGES,"
   Print #fileNo, addTab(2); qualFuncNameLastStrElem; "(CONTAINER_NAME, CHAR((CASE WHEN POSSTR(CONTAINER_NAME, '/') > 0 THEN '/' ELSE '\' END), 1)),"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (PARTITION BY TABLESPACE_ID ORDER BY CONTAINER_ID ASC)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "TABLE (SYSPROC.SNAPSHOT_CONTAINER(CURRENT SERVER, -1)) X"
   Print #fileNo, addTab(0); "),"
   Print #fileNo, addTab(1); "V_TbSpaceContainerList"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "tbSpaceId,"
   Print #fileNo, addTab(1); "commentList,"
   Print #fileNo, addTab(1); "list,"
   Print #fileNo, addTab(1); "card"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "TC.tbSpaceId,"
   Print #fileNo, addTab(2); "'-- ' || TC.containerType || ' ''' || TC.containerPath || '''' || (CASE TC.containerType WHEN 'FILE' THEN ' ' || RTRIM(CHAR(TC.totalPages)) ELSE '' END) || CHR(10),"
   Print #fileNo, addTab(2); "'  ' || TC.containerType || ' ''<#tsRootDir#>' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END) ||"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE TC.tbSpaceId"
   Print #fileNo, addTab(3); "WHEN 0 THEN 'system1' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END)"
   Print #fileNo, addTab(3); "WHEN 1 THEN 'temp1sms' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END)"
   Print #fileNo, addTab(3); "ELSE ("
   Print #fileNo, addTab(7); "CASE"
   Print #fileNo, addTab(7); "WHEN POSSTR(TC.tbSpace, 'SYS' ) > 0 THEN 'system1' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END)"
   Print #fileNo, addTab(7); "WHEN POSSTR(TC.tbSpace, 'TEMP') > 0 THEN 'temp1sms' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END)"
   Print #fileNo, addTab(7); "ELSE ''"
   Print #fileNo, addTab(7); "END"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "END"
   Print #fileNo, addTab(2); ") || TC.containerName ||"
   Print #fileNo, addTab(2); "(CASE TC.containerType WHEN 'FILE' THEN ' ' || RTRIM(CHAR(TC.totalPages)) ELSE '' END),"
   Print #fileNo, addTab(2); "1"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_TbSpaceContainer TC"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "TC.seqno = 1"
   Print #fileNo, addTab(1); "UNION ALL"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "TC.tbSpaceId,"
   Print #fileNo, addTab(2); "TCL.commentList || CHR(10) ||"
   Print #fileNo, addTab(2); "'-- ' || TC.containerType || ' ''' || TC.containerPath || ''' ' || (CASE TC.containerType WHEN 'FILE' THEN ' ' || RTRIM(CHAR(TC.totalPages)) ELSE '' END) || CHR(10),"
   Print #fileNo, addTab(2); "TCL.list || ',' || CHR(10) ||"
   Print #fileNo, addTab(2); "'  ' || TC.containerType || ' ''<#tsRootDir#>' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END) ||"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE TC.tbSpaceId"
   Print #fileNo, addTab(3); "WHEN 0 THEN 'system1' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END)"
   Print #fileNo, addTab(3); "WHEN 1 THEN 'temp1sms' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END)"
   Print #fileNo, addTab(3); "ELSE ("
   Print #fileNo, addTab(7); "CASE"
   Print #fileNo, addTab(7); "WHEN POSSTR(TC.tbSpace, 'SYS' ) > 0 THEN 'system1' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END)"
   Print #fileNo, addTab(7); "WHEN POSSTR(TC.tbSpace, 'TEMP') > 0 THEN 'temp1sms' || (CASE WHEN POSSTR(TC.containerPath, '/') > 0 THEN '/' ELSE '\' END)"
   Print #fileNo, addTab(7); "ELSE ''"
   Print #fileNo, addTab(7); "END"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "END"
   Print #fileNo, addTab(2); ") || TC.containerName ||"
   Print #fileNo, addTab(2); "(CASE TC.containerType WHEN 'FILE' THEN ' ' || RTRIM(CHAR(TC.totalPages)) ELSE '' END),"
   Print #fileNo, addTab(2); "TCL.card + 1"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_TbSpaceContainerList TCL,"
   Print #fileNo, addTab(2); "V_TbSpaceContainer TC"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "TCL.tbSpaceId = TC.tbSpaceId"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TC.seqno = TCL.card + 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TCL.card < 1000"
   Print #fileNo, addTab(0); "),"
   Print #fileNo, addTab(1); " V_TbSpaceMap"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "map,"
   Print #fileNo, addTab(1); "seqNo"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'-- tablespace ''' || TS.tbSpace || ''' (ID ' || RTRIM(CHAR(TS.tbSpaceId)) || ')' || CHR(10) ||"
   Print #fileNo, addTab(2); "TCL.commentList ||"
   Print #fileNo, addTab(2); "'SET TABLESPACE CONTAINERS FOR ' || RTRIM(CHAR(TS.tbSpaceId)) || ' USING'    || CHR(10) ||"
   Print #fileNo, addTab(2); "'(' || CHR(10) ||"
   Print #fileNo, addTab(2); "TCL.list || CHR(10) ||"
   Print #fileNo, addTab(2); "')' || CHR(10) ||"
   Print #fileNo, addTab(2); "'"; gc_sqlCmdDelim; "' || CHR(10),"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY TS.tbSpaceId)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_TbSpaces TS,"
   Print #fileNo, addTab(2); "V_TbSpaceContainerList TCL"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "TS.tbSpaceId = TCL.tbSpaceId"
   Print #fileNo, addTab(0); "),"
   Print #fileNo, addTab(1); "V_Script"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "script,"
   Print #fileNo, addTab(1); "seqNo"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "CAST("
   Print #fileNo, addTab(3); "'----------------------------------------------------------------------'     || CHR(10) ||"
   Print #fileNo, addTab(3); "'-- Description: Perform redirected restore for database ' || CURRENT SERVER || CHR(10) ||"
   Print #fileNo, addTab(3); "'--              into new database .'                                        || CHR(10) ||"
   Print #fileNo, addTab(3); "'--'                                                                         || CHR(10) ||"
   Print #fileNo, addTab(3); "'-- Required authority: SYSADM or SYSCTRL'                                   || CHR(10) ||"
   Print #fileNo, addTab(3); "'--'                                                                         || CHR(10) ||"
   Print #fileNo, addTab(3); "'-- Usage: db2 -td"; gc_sqlCmdDelim; " -f <scripName>'                                          || CHR(10) ||"
   Print #fileNo, addTab(3); "'----------------------------------------------------------------------'     || CHR(10) ||"

   Print #fileNo, addTab(3); "CHR(10) ||"
   Print #fileNo, addTab(3); "'UPDATE COMMAND OPTIONS USING V ON"; gc_sqlCmdDelim; "'                                         || CHR(10) ||"
   Print #fileNo, addTab(3); "'UPDATE COMMAND OPTIONS USING S ON"; gc_sqlCmdDelim; "'                                         || CHR(10) ||"
   Print #fileNo, addTab(3); "'UPDATE COMMAND OPTIONS USING N ON"; gc_sqlCmdDelim; "'                                         || CHR(10) ||"
   Print #fileNo, addTab(3); "'UPDATE COMMAND OPTIONS USING X ON"; gc_sqlCmdDelim; "'                                         || CHR(10) ||"

   Print #fileNo, addTab(3); "CHR(10) ||"
   Print #fileNo, addTab(3); "'RESTORE DATABASE'                                                           || CHR(10) ||"
   Print #fileNo, addTab(3); "'  ' || CURRENT SERVER                                                       || CHR(10) ||"
   Print #fileNo, addTab(3); "'FROM'                                                                       || CHR(10) ||"
   Print #fileNo, addTab(3); "'  <#backupPath#>'                                                           || CHR(10) ||"
   Print #fileNo, addTab(3); "'--TAKEN AT'                                                                 || CHR(10) ||"
   Print #fileNo, addTab(3); "'  -- <backup timestamp (format: YYYYMMDDHHMMSS)>'                           || CHR(10) ||"
   Print #fileNo, addTab(3); "'INTO'                                                                       || CHR(10) ||"
   Print #fileNo, addTab(3); "'  <#newDbName#>'                                                            || CHR(10) ||"
   Print #fileNo, addTab(3); "'--NEWLOGPATH <#logPath#>'                                                   || CHR(10) ||"
   Print #fileNo, addTab(3); "'REDIRECT'                                                                   || CHR(10) ||"
   Print #fileNo, addTab(3); "'WITHOUT PROMPTING'                                                          || CHR(10) ||"
   Print #fileNo, addTab(3); "'"; gc_sqlCmdDelim; "'                                                                          || CHR(10) ||"

   Print #fileNo, addTab(3); "CHR(10)"
   Print #fileNo, addTab(2); "AS VARCHAR(32000)),"
   Print #fileNo, addTab(2); "0"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSIBM.SYSDUMMY1"
   Print #fileNo,
   Print #fileNo, addTab(1); "UNION ALL"
   Print #fileNo,
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "CAST("
   Print #fileNo, addTab(3); "TSC.script || CHR(10) ||"
   Print #fileNo, addTab(3); "TSM.map"
   Print #fileNo, addTab(2); "AS VARCHAR(32000)),"
   Print #fileNo, addTab(2); "TSC.seqNo +1"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_TbSpaceMap TSM,"
   Print #fileNo, addTab(2); "V_Script TSC"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "TSM.seqno = TSC.seqno + 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TSC.seqno < 1000"
   Print #fileNo, addTab(0); "),"
   Print #fileNo, addTab(1); " V_ScriptOrdered"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "script,"
   Print #fileNo, addTab(1); "seqno"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "script,"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY seqno DESC)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_Script"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "SELECT"
   Print #fileNo, addTab(1); "script  || CHR(10) ||"
   Print #fileNo, addTab(1); "'RESTORE DATABASE ' || CURRENT SERVER || ' CONTINUE' || CHR(10) ||"
   Print #fileNo, addTab(3); "'"; gc_sqlCmdDelim; "'"
   Print #fileNo, addTab(0); "FROM"
   Print #fileNo, addTab(1); "V_ScriptOrdered"
   Print #fileNo, addTab(0); "WHERE"
   Print #fileNo, addTab(1); "seqNo = 1"
 
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub

 
 Private Sub genDbAdminDdl2( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

   ' ####################################################################################################################
   ' #    SP for revoking access rights
   ' ####################################################################################################################

   Dim qualProcedureNameGrant As String
   qualProcedureNameGrant = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType)

   Dim qualProcedureNameRevoke As String
   qualProcedureNameRevoke = genQualProcName(g_sectionIndexDbAdmin, spnRevoke, ddlType)

   printSectionHeader "SP for revoking access rights", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRevoke
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of REVOKE-statements executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
   genSpLogProcEnter fileNo, qualProcedureNameRevoke, ddlType, , "mode_in", "rowCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET mode_in = mode_in + 10;"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameGrant; "(mode_in, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameRevoke, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   If ddlType <> edtPdm Then
     ' we do not support this for LDM
     Exit Sub
   End If

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDbAdminDdl3( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

   Dim qualViewNamePdmTable As String
   qualViewNamePdmTable = genQualViewName(g_sectionIndexDbMeta, vnPdmTable, vnsPdmTable, ddlType)

   ' ####################################################################################################################
   ' #    SP for setting integrity on tables
   ' ####################################################################################################################

   Dim qualProcName As String
   qualProcName = genQualProcName(g_sectionIndexDbAdmin, spnIntegrity, ddlType)

   printSectionHeader "SP for setting integrity on tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of tables affected"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "notCheckPending", "51027"
   genCondDecl fileNo, "cannotCheckDepTab", "428A8"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genVarDecl fileNo, "v_numTabsFound", "INTEGER", "0"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notCheckPending"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR cannotCheckDepTab"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   Dim qualTabNameTempStatementSetIntegrity As String
   qualTabNameTempStatementSetIntegrity = tempTabNameStatement & "SetIntegrity"

   genDdlForTempStatement fileNo, 1, True, , , True, True, , "SetIntegrity"

   genSpLogProcEnter fileNo, qualProcName, ddlType, , "mode_in", "tabCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"

   genProcSectionHeader fileNo, "loop as long as we find tables in 'check pending' state"
   Print #fileNo, addTab(1); "tabLoop:"
   Print #fileNo, addTab(1); "LOOP"
   Print #fileNo, addTab(2); "SET v_numTabsFound = 0;"

   genProcSectionHeader fileNo, "process each table in 'check pending state'", 2
   Print #fileNo, addTab(2); "FOR tabLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "T.TABSCHEMA AS c_schemaName,"
   Print #fileNo, addTab(4); "T.TABNAME AS c_tableName"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABLES T"
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "T.TABNAME = P."; g_anPdmTableName; ""
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "T.TABSCHEMA = P."; g_anPdmFkSchemaName
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "T.STATUS = 'C'"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "L."; g_anLdmFkSequenceNo
   Print #fileNo, addTab(3); "FOR READ ONLY"

   Print #fileNo, addTab(2); "DO"
 
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'SET INTEGRITY FOR ' || c_schemaName || '.' || c_tableName || ' IMMEDIATE CHECKED';"
 
   Print #fileNo, addTab(3); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(3); "SET v_numTabsFound = v_numTabsFound + 1;"

   genProcSectionHeader fileNo, "store statement in temporary table", 4
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); qualTabNameTempStatementSetIntegrity
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SEQNO,"
   Print #fileNo, addTab(4); "STATEMENT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "tabCount_out,"
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo, addTab(2); "END FOR;"

   Print #fileNo, addTab(2); "IF (v_numTabsFound = 0) OR (mode_in < 1) THEN"
   Print #fileNo, addTab(3); "LEAVE tabLoop;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END LOOP tabLoop;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTabNameTempStatementSetIntegrity
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcName, ddlType, , "mode_in", "tabCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for retrieving table status
   ' ####################################################################################################################

   Dim qualProcedureNameTabStatus As String
   qualProcedureNameTabStatus = genQualProcName(g_sectionIndexDbAdmin, spnGetTabStatus, ddlType)

   printSectionHeader "SP for retrieving table status", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTabStatus
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(300)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   Print #fileNo, addTab(1); "-- temporary table for table data"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.TabData"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "schema     "; g_dbtDbSchemaName; ","
   Print #fileNo, addTab(2); "name       VARCHAR(50),"
   Print #fileNo, addTab(2); "status     CHAR(1),"
   Print #fileNo, addTab(2); "numRows    INTEGER"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1

   genSpLogProcEnter fileNo, qualProcedureNameTabStatus, ddlType

   Print #fileNo,
   Print #fileNo, addTab(1); "DELETE FROM SESSION.TabData;"
   Print #fileNo,
   Print #fileNo, addTab(1); "-- loop over all tables in database"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "ST.TABSCHEMA AS c_schemaName,"
   Print #fileNo, addTab(3); "ST.TABNAME AS c_tableName,"
   Print #fileNo, addTab(3); "ST.STATUS AS c_tableStatus,"
   Print #fileNo, addTab(3); "PT.PDM_"; g_anOrganizationId; " AS c_organizationId,"
   Print #fileNo, addTab(3); "PT.PDM_"; g_anPoolTypeId; " AS c_poolTypeId"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES ST"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); qualViewNamePdmTable; " PT"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "ST.TABSCHEMA = PT."; g_anPdmFkSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "ST.TABNAME = PT."; g_anPdmTypedTableName
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "ST.TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "ST.TYPE = 'T'"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "ST.TABSCHEMA,"
   Print #fileNo, addTab(3); "ST.TABNAME"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO SESSION.TabData (schema,name,status,numRows) SELECT ' ||"
   Print #fileNo, addTab(3); "'''' || c_schemaName || ''',' ||"
   Print #fileNo, addTab(3); "'''' || c_tableName || ''',' ||"
   Print #fileNo, addTab(3); "'''' || c_tableStatus || ''',' ||"
   Print #fileNo, addTab(3); "'COUNT(*) FROM ' || c_schemaName || '.' || c_tableName ;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(1); "-- return result to application"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "*"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SESSION.TabData"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "schema ASC,"
   Print #fileNo, addTab(4); "name   ASC"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "-- leave cursor open for application"
   Print #fileNo, addTab(2); "OPEN stmntCursor;"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcExit fileNo, qualProcedureNameTabStatus, ddlType

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for re-setting OID sequence
   ' ####################################################################################################################

   qualProcName = genQualProcName(g_sectionIndexDbAdmin, spnResetOidSeq, ddlType)

   printSectionHeader "SP for re-setting OID-Sequence(s)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to reset the OID sequence number for"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables inspected"
   genProcParm fileNo, "OUT", "seqCount_out", "INTEGER", False, "number of sequences being reset"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
   genCondDecl fileNo, "oidNotExist", "42703"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR oidNotExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader fileNo, "temporary table for 'Max-OID' per table and organization"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.MaxOidPerTable"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "organizationId "; g_dbtEnumId; ","
   Print #fileNo, addTab(2); "maxOid         "; g_dbtOid
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1

   genSpLogProcEnter fileNo, qualProcName, ddlType, , "mode_in", "orgId_in", "tabCount_out", "seqCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET seqCount_out = 0;"
   Print #fileNo, addTab(1); "DELETE FROM SESSION.MaxOidPerTable;"
 
   genProcSectionHeader fileNo, "loop over all 'non-enumeration'-tables"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L,"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P,"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " <> 'E'"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_stmntTxt ="
   Print #fileNo, addTab(3); "'INSERT INTO ' ||"
   Print #fileNo, addTab(4); "'SESSION.MaxOidPerTable ' ||"
   Print #fileNo, addTab(4); "'(organizationId,maxOid) ' ||"
   Print #fileNo, addTab(3); "'SELECT ' ||"
   Print #fileNo, addTab(4); "'O.ID,' ||"
   Print #fileNo, addTab(4); "'COALESCE(MAX(T."; g_anOid; "),0)' ||"
   Print #fileNo, addTab(3); "'FROM ' ||"
   Print #fileNo, addTab(4); "c_schemaName || '.' || c_tableName || ' T,' ||"
   Print #fileNo, addTab(4); "'"; g_qualTabNamePdmOrganization; " O ' ||"
   Print #fileNo, addTab(3); "'WHERE ' ||"
   Print #fileNo, addTab(4); "'(T."; g_anOid; "/1"; gc_sequenceMinValue; ") = O.ID ' ||"
   Print #fileNo, addTab(3); "'GROUP BY ' ||"
   Print #fileNo, addTab(4); "'O.ID';"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "default values - if no records were found"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.MaxOidPerTable"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "organizationId,"
   Print #fileNo, addTab(2); "maxOid"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ID,"
   Print #fileNo, addTab(2); g_dbtOid; "(RTRIM(CAST(ID AS CHAR(3))) || '00000002000000000')"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNamePdmOrganization
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "COALESCE(orgId_in, ID) = ID"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader fileNo, "determine number of affected sequences"
   Print #fileNo, addTab(1); "SET seqCount_out = (SELECT COUNT(DISTINCT organizationId) FROM SESSION.MaxOidPerTable);"
 
   genProcSectionHeader fileNo, "reset OID sequence(s)"
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "FOR tabLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "organizationId AS c_organizationId,"
   Print #fileNo, addTab(4); "MAX(maxOid) AS c_maxOid"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SESSION.MaxOidPerTable"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "orgId_in IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "organizationId = orgId_in"
   Print #fileNo, addTab(3); "GROUP BY"
   Print #fileNo, addTab(4); "organizationId"
   Print #fileNo, addTab(3); "FOR READ ONLY"

   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'ALTER SEQUENCE "; g_schemaNameCtoMeta; "' || RIGHT('00' || RTRIM(CAST(c_organizationId AS CHAR(2))),2) || '."; UCase(gc_seqNameOid); " RESTART WITH ' || RTRIM(CAST((c_maxOid + "; CStr(gc_sequenceIncrementValue); ") AS CHAR(40)));"

   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CAST("
   Print #fileNo, addTab(6); "'ALTER SEQUENCE "; g_schemaNameCtoMeta; "' || RIGHT('00' || RTRIM(CAST(organizationId AS CHAR(2))),2) || '."; UCase(gc_seqNameOid); " RESTART WITH ' || RTRIM(CAST((MAX(maxOid) + "; CStr(gc_sequenceIncrementValue); ") AS CHAR(40)))"
   Print #fileNo, addTab(5); "AS VARCHAR(80)) AS STMNT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SESSION.MaxOidPerTable"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "orgId_in IS NULL"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "organizationId = orgId_in"
   Print #fileNo, addTab(4); "GROUP BY"
   Print #fileNo, addTab(5); "organizationId"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "organizationId ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcName, ddlType, , "mode_in", "orgId_in", "tabCount_out", "seqCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for deleting LRT-related records
   ' ####################################################################################################################

   qualProcName = genQualProcName(g_sectionIndexDbAdmin, spnClearLrt, ddlType)

   printSectionHeader "SP for 'clearing' LRT-related data", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "lrtOid_in", g_dbtOid, True, "(optional) OID of the LRT to clear"
   genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "(optional) userId to clear LRTs for"
   genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) Id of the organization to clear the LRTs for"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(500)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(1); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genProcSectionHeader fileNo, "temporary table for statements"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); tempTabNameStatement
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SEQNO     INTEGER,"
   Print #fileNo, addTab(2); "STATEMENT VARCHAR(1000)"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1

   genSpLogProcEnter fileNo, qualProcName, ddlType, , "mode_in", "lrtOid_in", "'cdUserId_in", "orgId_in", "tabCount_out", "rowCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET cdUserId_in = UPPER(cdUserId_in);"
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
   Print #fileNo, addTab(1); "DELETE FROM SESSION.Statements;"
 
   genProcSectionHeader fileNo, "process each LRT table"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
 ' ### IF IVK ###
   Print #fileNo, addTab(3); "A."; g_anAcmCondenseData; "    AS c_condenseData,"
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(3); "P1."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "P1."; g_anPdmTableName; " AS c_tableNamePub,"
   Print #fileNo, addTab(3); "P2."; g_anPdmTableName; " AS c_tableNamePriv,"
   Print #fileNo, addTab(3); "P3."; g_anPdmFkSchemaName; " AS c_schemaNameLrt,"
   Print #fileNo, addTab(3); "P3."; g_anPdmTableName; " AS c_tableNameLrt"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A,"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L1,"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L2,"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L3,"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P1,"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P2,"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P3"
   Print #fileNo, addTab(2); "WHERE"

   Print #fileNo, addTab(3); "L1."; g_anAcmEntitySection; " = A."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L1."; g_anAcmEntityName; " = A."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L1."; g_anAcmEntityType; " = A."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L1."; g_anAcmEntitySection; " = L2."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L1."; g_anAcmEntityName; " = L2."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L1."; g_anAcmEntityType; " = L2."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L1."; g_anLdmIsNl; " = L2."; g_anLdmIsNl
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L1."; g_anLdmIsGen; " = L2."; g_anLdmIsGen
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L1."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L2."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L2."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P1."; g_anPdmLdmFkSchemaName; " = L1."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P1."; g_anPdmLdmFkTableName; " = L1."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P2."; g_anPdmLdmFkSchemaName; " = L2."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P2."; g_anPdmLdmFkTableName; " = L2."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P1."; g_anOrganizationId; " = P2."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P1."; g_anPoolTypeId; " = P2."; g_anPoolTypeId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P1."; g_anOrganizationId; " = P3."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "((orgId_in IS NULL) OR (P1."; g_anOrganizationId; " = orgId_in))"

   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P3."; g_anPdmLdmFkSchemaName; " = L3."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P3."; g_anPdmLdmFkTableName; " = L3."; g_anLdmTableName

     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L3."; g_anAcmEntitySection; " = '"; UCase(g_classes.descriptors(g_classIndexLrt).sectionName); "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L3."; g_anAcmEntityName; " = '"; UCase(g_classes.descriptors(g_classIndexLrt).className); "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L3."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L3."; g_anLdmIsNl; " = "; gc_dbFalse

   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "P1."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(3); "P2."; g_anPdmTableName
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "process public table", 2, True

 ' ### IF IVK ###
   Const indent = 1
   Print #fileNo, addTab(2); "IF c_condenseData = "; gc_dbFalse; " THEN"
 ' ### ELSE IVK ###
 ' Const indent = 0
 ' ### ENDIF IVK ###
   Print #fileNo, addTab(indent + 2); "SET v_stmntTxt = 'UPDATE ' || c_schemaName || '.' || c_tableNamePub || ' SET "; g_anInLrt; " = NULL WHERE "; g_anInLrt; " IS NOT NULL';"
   Print #fileNo, addTab(indent + 2); "IF lrtOid_in IS NOT NULL THEN"
   Print #fileNo, addTab(indent + 3); "SET v_stmntTxt = v_stmntTxt || ' AND "; g_anInLrt; " = ' || RTRIM(CHAR(lrtOid_in));"
   Print #fileNo, addTab(indent + 2); "END IF;"
   Print #fileNo, addTab(indent + 2); "IF cdUserId_in IS NOT NULL THEN"
   Print #fileNo, addTab(indent + 3); "SET v_stmntTxt = v_stmntTxt || ' AND "; g_anInLrt; " IN (' ||"
   Print #fileNo, addTab(indent + 10); " 'SELECT A."; g_anOid; " FROM ' || c_schemaNameLrt ||"
   Print #fileNo, addTab(indent + 10); " '.LRT A INNER JOIN "; g_qualTabNameUser; " B ON UTROWN_OID = B."; g_anOid; " AND UPPER(B."; g_anUserId; ")=''' ||"
   Print #fileNo, addTab(indent + 10); " cdUserId_in || ''')';"
   Print #fileNo, addTab(indent + 2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(indent + 2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(indent + 2); "INSERT INTO SESSION.statements (SEQNO, STATEMENT ) VALUES (tabCount_out, v_stmntTxt);"
   Print #fileNo, addTab(indent + 2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(indent + 3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(indent + 2); "END IF;"

 ' ### IF IVK ###
   Print #fileNo, addTab(2); "END IF;"
 ' ### ENDIF IVK ###

   genProcSectionHeader fileNo, "process private table", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_schemaName || '.' || c_tableNamePriv || ' WHERE (1=1)';"
   Print #fileNo, addTab(2); "IF lrtOid_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND "; g_anInLrt; " = ' || RTRIM(CHAR(lrtOid_in));"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF cdUserId_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND "; g_anInLrt; " IN (' ||"
   Print #fileNo, addTab(10); " 'SELECT A."; g_anOid; " FROM ' || c_schemaNameLrt ||"
   Print #fileNo, addTab(10); " '.LRT A INNER JOIN "; g_qualTabNameUser; " B ON UTROWN_OID = B."; g_anOid; " AND UPPER(B."; g_anUserId; ")=''' ||"
   Print #fileNo, addTab(10); " cdUserId_in || ''')';"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "INSERT INTO SESSION.statements (SEQNO, STATEMENT ) VALUES (tabCount_out, v_stmntTxt);"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "process meta tables for each involved organization"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " AS c_schemaNameLrt,"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " AS c_tableNameLrt,"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " AS c_organizationId"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"

   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName

   Print #fileNo, addTab(3); "WHERE"

     Print #fileNo, addTab(4); "L."; g_anAcmEntitySection; " = '"; UCase(g_classes.descriptors(g_classIndexLrt).sectionName); "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anAcmEntityName; " = '"; UCase(g_classes.descriptors(g_classIndexLrt).className); "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anLdmIsNl; " = "; gc_dbFalse

   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "((orgId_in IS NULL) OR (P."; g_anOrganizationId; " = orgId_in))"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "process table '" & UCase(clnLrtAffectedEntity) & "'", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_schemaNameLrt || '.LRTAFFECTEDENTITY WHERE (1=1)';"
   Print #fileNo, addTab(2); "IF lrtOid_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND "; g_anLrtOid; " = ' || RTRIM(CHAR(lrtOid_in));"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF cdUserId_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND "; g_anLrtOid; " IN (SELECT OID FROM ' ||"
   Print #fileNo, addTab(10); " c_schemaNameLrt || '.' || c_tableNameLrt ||"
   Print #fileNo, addTab(10); " ' A WHERE UTROWN_OID = (SELECT B."; g_anOid; " FROM "; g_qualTabNameUser; " B WHERE UPPER(B."; g_anUserId; ")=''' ||"
   Print #fileNo, addTab(10); " cdUserId_in || '''))';"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "INSERT INTO SESSION.statements (SEQNO, STATEMENT ) VALUES (tabCount_out, v_stmntTxt);"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "process table '" & UCase(clnLrtExecStatus) & "'", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_schemaNameLrt || '."; UCase(clnLrtExecStatus); " WHERE (1=1)';"
   Print #fileNo, addTab(2); "IF lrtOid_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND "; g_anLrtOid; " = ' || RTRIM(CHAR(lrtOid_in));"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF cdUserId_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND "; g_anLrtOid; " IN (SELECT OID FROM ' ||"
   Print #fileNo, addTab(10); " c_schemaNameLrt || '.' || c_tableNameLrt ||"
   Print #fileNo, addTab(10); " ' A WHERE UTROWN_OID = (SELECT B."; g_anOid; " FROM "; g_qualTabNameUser; " B WHERE UPPER(B."; g_anUserId; ")=''' ||"
   Print #fileNo, addTab(10); " cdUserId_in || '''))';"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "INSERT INTO SESSION.statements (SEQNO, STATEMENT ) VALUES (tabCount_out, v_stmntTxt);"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "process NL-text table for '" & UCase(clnLrt) & "'", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_schemaNameLrt || '.' || c_tableNameLrt || '_NL_TEXT WHERE (1=1)';"
   Print #fileNo, addTab(2); "IF lrtOid_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND LRT_OID = ' || RTRIM(CHAR(lrtOid_in));"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF cdUserId_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND LRT_OID IN (SELECT OID FROM ' ||"
   Print #fileNo, addTab(10); " c_schemaNameLrt || '.' || c_tableNameLrt ||"
   Print #fileNo, addTab(10); " ' A WHERE UTROWN_OID = (SELECT B."; g_anOid; " FROM "; g_qualTabNameUser; " B WHERE UPPER(B."; g_anUserId; ")=''' ||"
   Print #fileNo, addTab(10); " cdUserId_in || '''))';"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "INSERT INTO SESSION.statements (SEQNO, STATEMENT ) VALUES (tabCount_out, v_stmntTxt);"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "process table '" & UCase(clnLrt) & "'", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_schemaNameLrt || '.' || c_tableNameLrt || ' WHERE (1=1)';"
   Print #fileNo, addTab(2); "IF lrtOid_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND OID = ' || RTRIM(CHAR(lrtOid_in));"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF cdUserId_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND  UTROWN_OID = (SELECT B."; g_anOid; " FROM "; g_qualTabNameUser; " B WHERE UPPER(B."; g_anUserId; ")=''' ||"
   Print #fileNo, addTab(10); " cdUserId_in || ''')';"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "INSERT INTO SESSION.statements (SEQNO, STATEMENT ) VALUES (tabCount_out, v_stmntTxt);"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"

 ' ### IF IVK ###
   genProcSectionHeader fileNo, "process table '" & UCase(clnRegistryDynamic) & "'", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM "; g_qualTabNameRegistryDynamic; " R WHERE R.SECTION = ''"; gc_regDynamicSectionAutoSetProd; "'' ' ||"
   Print #fileNo, addTab(10); "'AND R."; g_anKey; " = ''"; gc_regDynamicKeyAutoSetProd; "'' AND LENGTH(R."; g_anSubKey; ") > 3 AND LENGTH(R."; g_anSubKey; ") <= "; CStr(3 + Len(gc_dbMaxBigInt)); " AND RIGHT(LEFT(R."; g_anSubKey; ",3),1) = ''-'' ' ||"
   Print #fileNo, addTab(10); "'AND CAST(LEFT(R."; g_anSubKey; ",2) AS "; g_dbtEnumId; ") = ' || RTRIM(CAST(c_organizationId AS CHAR(10)));"

   Print #fileNo, addTab(2); "IF cdUserId_in IS NOT NULL THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' AND CAST(RIGHT(R."; g_anSubKey; ",LENGTH("; g_anSubKey; ")-3) AS "; g_dbtOid; ") IN (' ||"
   Print #fileNo, addTab(10); "'SELECT L.UTROWN_OID FROM ' || c_schemaNameLrt || '.' || c_tableNameLrt ||"
   Print #fileNo, addTab(10); "' L,"; g_qualTabNameUser; " U WHERE L.UTROWN_OID = U."; g_anOid; " AND UPPER(U."; g_anUserId; ")=''' ||"
   Print #fileNo, addTab(10); " UPPER(cdUserId_in) || ''')';"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(2); "SET tabCount_out = tabCount_out + 1;"
   Print #fileNo, addTab(2); "INSERT INTO SESSION.statements (SEQNO, STATEMENT ) VALUES (tabCount_out, v_stmntTxt);"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"

 ' ### ENDIF IVK ###
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application", 1
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcName, ddlType, , "mode_in", "lrtOid_in", "'cdUserId_in", "orgId_in", "tabCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for deleting LRT-related records
   ' ####################################################################################################################

   qualProcName = genQualProcName(g_sectionIndexDbAdmin, spnClearLrt, ddlType)

   printSectionHeader "SP for 'clearing' LRT-related data", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "(optional) userId to clear LRTs for"
   genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) Id of the organization to clear the LRTs for"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables affected"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_lrtOid", g_dbtOid, "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcName, ddlType, , "mode_in", "'cdUserId_in", "orgId_in", "tabCount_out", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcName; "(mode_in, v_lrtOid, cdUserId_in, orgId_in, tabCount_out, rowCount_out);"
   Print #fileNo,

   genSpLogProcExit fileNo, qualProcName, ddlType, , "mode_in", "'cdUserId_in", "orgId_in", "tabCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDbAdminDdl4( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     ' we do not support most of this for LDM
     GoTo LdmEntryPoint
   End If

   ' ####################################################################################################################
   ' #    SP for verifying database configuration
   ' ####################################################################################################################

   Dim qualProcName As String
   qualProcName = genQualProcName(g_sectionIndexDbAdmin, spnVerifyCfg, ddlType)

   printSectionHeader "SP for verification of database configuration", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genSigMsgVarDecl fileNo
   genSpLogDecl fileNo
   genSpLogProcEnter fileNo, qualProcName, ddlType
 
   genProcSectionHeader fileNo, "verify that database id running in UTC timezone"
   Print #fileNo, addTab(1); "IF (CURRENT TIMEZONE <> 0) THEN"
   genSpLogProcEscape fileNo, qualProcName, ddlType, 2
   genSignalDdlWithParms "noUtc", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(INTEGER(CURRENT TIMEZONE)))"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcName, ddlType

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP executing data cleanup jobs
   ' ####################################################################################################################
 
   Dim qualProcedureNameCleanup As String
   qualProcedureNameCleanup = genQualProcName(g_sectionIndexDbAdmin, spnCleanData, ddlType)

   printSectionHeader "SP executing data cleanup jobs", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCleanup
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup-statements, '1' list and execute statements, '2' execute statements only"
   genProcParm fileNo, "IN", "jobCategory_in", "VARCHAR(20)", True, "category of the clean-job to execute"
   genProcParm fileNo, "IN", "jobName_in", "VARCHAR(20)", True, "(optional) name of the clean-job to execute"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "IN", "parameter1_in", "VARCHAR(30)", True, "(optional) parameter 1 to use in condition term for job"
   genProcParm fileNo, "IN", "parameter2_in", "VARCHAR(30)", True, "(optional) parameter 2 to use in condition term for job"
   genProcParm fileNo, "IN", "parameter3_in", "VARCHAR(30)", True, "(optional) parameter 3 to use in condition term for job"
 
   genProcParm fileNo, "OUT", "stmntCount_out", "INTEGER", True, "number of statements for this job"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows deleted in any table"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_condition", "VARCHAR(500)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_rowCountThisTable", "INTEGER", "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_stmntTxtRaw", "VARCHAR(400)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   Dim qualTabNameTempStatement As String
   qualTabNameTempStatement = tempTabNameStatement & "CleanData"
   genDdlForTempStatement fileNo, 1, True, 600, False, True, True, False, "CleanData", True, , , , "numRowsDeleted", "INTEGER"

   genSpLogProcEnter fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "'parameter3_in", "stmntCount_out", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET stmntCount_out = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out   = 0;"
 
   genProcSectionHeader fileNo, "normalize input parameter"
   Print #fileNo, addTab(1); "SET jobCategory_in = UPPER(RTRIM(jobCategory_in));"
   Print #fileNo, addTab(1); "SET jobName_in     = UPPER(RTRIM(jobName_in));"
 
   genProcSectionHeader fileNo, "loop over job-records and execute job-statements"
   Print #fileNo, addTab(1); "FOR jobEntryLoop AS jobCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "UPPER(SCHEMANAME) AS c_schemaNamePattern,"
   Print #fileNo, addTab(3); "UPPER(TABLENAME) AS c_tableNamePattern,"
   Print #fileNo, addTab(3); "UPPER(TABLEREF) AS c_tableRef,"
   Print #fileNo, addTab(3); "CONDITION AS c_condition,"
   Print #fileNo, addTab(3); "COMMITCOUNT AS c_commitCount"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); genQualTabNameByClassIndex(g_classIndexCleanJobs, ddlType); " J"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "UPPER(J.JOBCATEGORY) = jobCategory_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(jobName_in IS NULL)"
   Print #fileNo, addTab(4); "  OR"
   Print #fileNo, addTab(4); "(COALESCE(UPPER(J.JOBNAME), jobName_in) = jobName_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(level_in IS NULL)"
   Print #fileNo, addTab(4); "  OR"
   Print #fileNo, addTab(4); "(J.LEVEL IS NULL)"
   Print #fileNo, addTab(4); "  OR"
   Print #fileNo, addTab(4); "(COALESCE(J.LEVEL, level_in) >= level_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "SEQUENCENO ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_condition = c_condition;"
   Print #fileNo, addTab(2); "IF (v_condition IS NOT NULL) THEN"
   Print #fileNo, addTab(3); "IF (parameter1_in IS NOT NULL) THEN"
   Print #fileNo, addTab(4); "SET v_condition = REPLACE(v_condition, '%1', parameter1_in);"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF (parameter2_in IS NOT NULL) THEN"
   Print #fileNo, addTab(4); "SET v_condition = REPLACE(v_condition, '%2', parameter2_in);"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF (parameter3_in IS NOT NULL) THEN"
   Print #fileNo, addTab(4); "SET v_condition = REPLACE(v_condition, '%3', parameter3_in);"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   genProcSectionHeader fileNo, "loop over matching tables", 2
   Print #fileNo, addTab(2); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "TABSCHEMA,"
   Print #fileNo, addTab(4); "TABNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABLES"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "TYPE = 'T'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "c_schemaNamePattern IS NULL"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "RTRIM(TABSCHEMA) LIKE c_schemaNamePattern ESCAPE '\'"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "RTRIM(TABNAME) LIKE c_tableNamePattern ESCAPE '\'"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DELETE FROM ' || RTRIM(TABSCHEMA) || '.' || RTRIM(TABNAME) || COALESCE(' ' || c_tableRef, '');"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF (v_condition IS NOT NULL) THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = v_stmntTxt || ' WHERE ' || v_condition;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_stmntTxtRaw = v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "SET v_rowCountThisTable = 0;"
   Print #fileNo,
   Print #fileNo, addTab(4); "IF COALESCE(c_commitCount, 0) = 0 THEN"

   Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(5); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(5); "SET v_rowCountThisTable    = v_rowCount;"

   Print #fileNo, addTab(4); "ELSE"
   Print #fileNo, addTab(5); "SET v_stmntTxt = REPLACE(v_stmntTxt, 'DELETE FROM', 'DELETE FROM (SELECT * FROM') || ' FETCH FIRST ' || RTRIM(CHAR(c_commitCount)) || ' ROWS ONLY)';"
   Print #fileNo, addTab(5); "SET v_rowCount = c_commitCount;"
   Print #fileNo,
   Print #fileNo, addTab(5); "WHILE v_rowCount = c_commitCount DO"

   Print #fileNo, addTab(6); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(6); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(6); "SET v_rowCountThisTable    = v_rowCountThisTable + v_rowCount;"
   Print #fileNo,
   Print #fileNo, addTab(6); "COMMIT;"
   Print #fileNo, addTab(5); "END WHILE;"
 
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(4); "SET rowCount_out = rowCount_out + v_rowCountThisTable;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "SET stmntCount_out = stmntCount_out +1;"
   Print #fileNo,
   Print #fileNo, addTab(3); "INSERT INTO "; qualTabNameTempStatement; "(seqNo, numRowsDeleted, statement) VALUES (stmntCount_out, v_rowCountThisTable, v_stmntTxtRaw);"

   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "numRowsDeleted,"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTabNameTempStatement
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "'parameter3_in", "stmntCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCleanup
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup-statements, '1' list and execute statements, '2' execute statements only"
   genProcParm fileNo, "IN", "jobCategory_in", "VARCHAR(20)", True, "category of the clean-job to execute"
   genProcParm fileNo, "IN", "jobName_in", "VARCHAR(20)", True, "name of the clean-job to execute"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "IN", "parameter1_in", "VARCHAR(30)", True, "(optional) parameter 1 to use in condition term for job"
   genProcParm fileNo, "IN", "parameter2_in", "VARCHAR(30)", True, "(optional) parameter 2 to use in condition term for job"
 
   genProcParm fileNo, "OUT", "stmntCount_out", "INTEGER", True, "number of statements for this job"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows deleted in any table"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_parameter3", "VARCHAR(30)", "NULL"
   genSpLogDecl fileNo
   genSpLogProcEnter fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "stmntCount_out", "rowCount_out"
 
   genProcSectionHeader fileNo, "call 'master' procedure"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameCleanup; "(mode_in, jobCategory_in, jobName_in, level_in, parameter1_in, parameter2_in, v_parameter3, stmntCount_out, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "stmntCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCleanup
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup-statements, '1' list and execute statements, '2' execute statements only"
   genProcParm fileNo, "IN", "jobCategory_in", "VARCHAR(20)", True, "category of the clean-job to execute"
   genProcParm fileNo, "IN", "jobName_in", "VARCHAR(20)", True, "name of the clean-job to execute"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "IN", "parameter1_in", "VARCHAR(30)", True, "(optional) parameter 1 to use in condition term for job"
 
   genProcParm fileNo, "OUT", "stmntCount_out", "INTEGER", True, "number of statements for this job"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows deleted in any table"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_parameter2", "VARCHAR(30)", "NULL"
   genVarDecl fileNo, "v_parameter3", "VARCHAR(30)", "NULL"
   genSpLogDecl fileNo
   genSpLogProcEnter fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "stmntCount_out", "rowCount_out"
 
   genProcSectionHeader fileNo, "call 'master' procedure"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameCleanup; "(mode_in, jobCategory_in, jobName_in, level_in, parameter1_in, v_parameter2, v_parameter3, stmntCount_out, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "stmntCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCleanup
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup-statements, '1' list and execute statements, '2' execute statements only"
   genProcParm fileNo, "IN", "jobCategory_in", "VARCHAR(20)", True, "category of the clean-job to execute"
   genProcParm fileNo, "IN", "jobName_in", "VARCHAR(20)", True, "name of the clean-job to execute"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
 
   genProcParm fileNo, "OUT", "stmntCount_out", "INTEGER", True, "number of statements for this job"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows deleted in any table"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_parameter1", "VARCHAR(30)", "NULL"
   genVarDecl fileNo, "v_parameter2", "VARCHAR(30)", "NULL"
   genVarDecl fileNo, "v_parameter3", "VARCHAR(30)", "NULL"
   genSpLogDecl fileNo
   genSpLogProcEnter fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "stmntCount_out", "rowCount_out"
 
   genProcSectionHeader fileNo, "call 'master' procedure"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameCleanup; "(mode_in, jobCategory_in, jobName_in, level_in, v_parameter1, v_parameter2, v_parameter3, stmntCount_out, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "stmntCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCleanup
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup-statements, '1' list and execute statements, '2' execute statements only"
   genProcParm fileNo, "IN", "jobCategory_in", "VARCHAR(20)", True, "category of the clean-job to execute"
   genProcParm fileNo, "IN", "jobName_in", "VARCHAR(20)", True, "name of the clean-job to execute"
 
   genProcParm fileNo, "OUT", "stmntCount_out", "INTEGER", True, "number of statements for this job"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows deleted in any table"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_level", "INTEGER", "NULL"
   genVarDecl fileNo, "v_parameter1", "VARCHAR(30)", "NULL"
   genVarDecl fileNo, "v_parameter2", "VARCHAR(30)", "NULL"
   genVarDecl fileNo, "v_parameter3", "VARCHAR(30)", "NULL"
   genSpLogDecl fileNo
   genSpLogProcEnter fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "stmntCount_out", "rowCount_out"
 
   genProcSectionHeader fileNo, "call 'master' procedure"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameCleanup; "(mode_in, jobCategory_in, jobName_in, v_level, v_parameter1, v_parameter2, v_parameter3, stmntCount_out, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameCleanup, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "stmntCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 LdmEntryPoint:
   ' ####################################################################################################################
   ' #    SP for HELP on Stored Procedures
   ' ####################################################################################################################

   Dim qualProcedureNameHelp As String
   qualProcedureNameHelp = genQualProcName(g_sectionIndexHelp, spnHelp, ddlType)

   printSectionHeader "SP for HELP on Stored Procedures (based on keyword and schema)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameHelp
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "schema name pattern of SQL-stored procedure(s) to provide help for"
   genProcParm fileNo, "IN", "objNamePattern_in", "VARCHAR(50)", True, "name pattern of procedure(s) / function(s) to provide help for"
   genProcParm fileNo, "OUT", "text_out", "CLOB(200000)", False, "returns API-information for referred stored procedure(s) / function(s)"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_delimLine", "VARCHAR(80)", "'--------------------------------------------------------------------------------'"
   genVarDecl fileNo, "v_foundEntry", g_dbtBoolean, gc_dbFalse
   genSpLogDecl fileNo
   genSpLogProcEnter fileNo, qualProcedureNameHelp, ddlType, , "'schemaNamePattern_in", "'objNamePattern_in", "'text_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET text_out = CHR(10) || CHR(10) || v_delimLine;"
 
   genProcSectionHeader fileNo, "loop over all matching procedures"
   Print #fileNo, addTab(1); "FOR procLoop AS procCursor CURSOR FOR"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "TEXT,"
   Print #fileNo, addTab(3); "PROCSCHEMA,"
   Print #fileNo, addTab(3); "PROCNAME,"
   Print #fileNo, addTab(3); "PARM_COUNT"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "REPLACE(TEXT, 'CREATE ' , ''),"
   Print #fileNo, addTab(4); "PROCSCHEMA,"
   Print #fileNo, addTab(4); "PROCNAME,"
   Print #fileNo, addTab(4); "PARM_COUNT"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.PROCEDURES"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "LANGUAGE = 'SQL'"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "LEFT(TEXT, COALESCE(POSSTR(TEXT, CHR(10) || ')') + 1, 500)) AS TEXTHEADER"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "( PROCSCHEMA LIKE '"; g_allSchemaNamePattern; "' )"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "( schemaNamePattern_in IS NULL )"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "( RTRIM(PROCSCHEMA) LIKE UPPER(schemaNamePattern_in) )"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "( objNamePattern_in IS NULL )"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "( PROCNAME LIKE UPPER(objNamePattern_in) )"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "PROCSCHEMA,"
   Print #fileNo, addTab(3); "PROCNAME,"
   Print #fileNo, addTab(3); "PARM_COUNT"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET text_out = text_out || CHR(10) || CHR(10) || TEXTHEADER || CHR(10);"
   Print #fileNo, addTab(2); "SET v_foundEntry = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "add delimiter line"
   Print #fileNo, addTab(1); "IF v_foundEntry = 1 THEN"
   Print #fileNo, addTab(2); "SET text_out = text_out || CHR(10) || v_delimLine || CHR(10);"
   Print #fileNo, addTab(2); "SET v_foundEntry = "; gc_dbFalse; ";"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "loop over all matching functions"
   Print #fileNo, addTab(1); "FOR funcLoop AS funcCursor CURSOR FOR"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "TEXT,"
   Print #fileNo, addTab(3); "FUNCSCHEMA,"
   Print #fileNo, addTab(3); "FUNCNAME,"
   Print #fileNo, addTab(3); "PARM_COUNT"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "REPLACE(BODY, 'CREATE ' , ''),"
   Print #fileNo, addTab(4); "FUNCSCHEMA,"
   Print #fileNo, addTab(4); "FUNCNAME,"
   Print #fileNo, addTab(4); "PARM_COUNT"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.FUNCTIONS"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "LANGUAGE = 'SQL'"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "LEFT(TEXT, COALESCE(POSSTR(TEXT, CHR(10) || ')') + 1,500)) AS TEXTHEADER"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "( FUNCSCHEMA LIKE '"; g_allSchemaNamePattern; "' )"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "( schemaNamePattern_in IS NULL )"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "( FUNCSCHEMA LIKE '%' || UPPER(schemaNamePattern_in) || '%' )"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "( objNamePattern_in IS NULL )"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "( FUNCNAME LIKE '%' || UPPER(objNamePattern_in) || '%' )"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "FUNCSCHEMA,"
   Print #fileNo, addTab(3); "FUNCNAME,"
   Print #fileNo, addTab(3); "PARM_COUNT"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET text_out = text_out || CHR(10) || CHR(10) || TEXTHEADER || CHR(10) ;"
   Print #fileNo, addTab(2); "SET v_foundEntry = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END FOR;"

   Print #fileNo, addTab(1); "IF v_foundEntry = 1 THEN"
   Print #fileNo, addTab(2); "SET text_out = text_out || CHR(10) || v_delimLine || CHR(10) ;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameHelp, ddlType, , "'schemaNamePattern_in", "'objNamePattern_in", "'text_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   printSectionHeader "SP for HELP on Stored Procedures (based on keyword)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameHelp
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "objNamePattern_in", "VARCHAR(50)", True, "name pattern of SQL-stored procedure(s) to provide help for"
   genProcParm fileNo, "OUT", "text_out", "CLOB(200000)", False, "returns API-information for referred stored procedures"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_schemaNamePattern", g_dbtDbSchemaName, "NULL"
   genSpLogDecl fileNo
   genSpLogProcEnter fileNo, qualProcedureNameHelp, ddlType, , "'objNamePattern_in", "'text_out"

   genProcSectionHeader fileNo, "call 'specific HELP procedure'"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameHelp; "(v_schemaNamePattern, objNamePattern_in, text_out);"

   genSpLogProcExit fileNo, qualProcedureNameHelp, ddlType, , "'objNamePattern_in", "'text_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for HELP on "Catching SQL-Codes" via "db2pd"
   ' ####################################################################################################################
 
   Dim qualProcedureNameHelpCatchSqlCode As String
   qualProcedureNameHelpCatchSqlCode = genQualProcName(g_sectionIndexHelp, spnHelpCatchSqlCode, ddlType)

   printSectionHeader "SP for HELP on ""Catching SQL-Codes"" via ""db2pd""", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameHelpCatchSqlCode
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "OUT", "text_out", "CLOB(10000)", False, "returns help on 'how to catch SQL-Codes via ""db2pd""'"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET text_out = CHR(10) ||"
   Print #fileNo, addTab(2); "'#!/bin/sh' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'# Place this script in file' || CHR(10) ||"
   Print #fileNo, addTab(2); "'#     $HOME/sqllob/db2cos' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# and activate it via' || CHR(10) ||"
   Print #fileNo, addTab(2); "'#     db2pd -catch -<sqlcCde>[,<errCode>]' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# e.g.:' || CHR(10) ||"
   Print #fileNo, addTab(2); "'#     db2pd -catch -911,2' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# To reset use' || CHR(10) ||"
   Print #fileNo, addTab(2); "'#     db2pd -catch reset' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'# Process the core arguments. Cycle through each argument until we find one that' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# does not have an ''='' sign' || CHR(10) ||"
   Print #fileNo, addTab(2); "'#' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# NOTE: the following core arguments are possible but should not be expected.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# Be sure to check whether they are set before using them.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'#' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# instance  : instance name' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# database  : database name' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# dbpart    : database partition number' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# pid       : process ID' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# tid       : thread ID' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# function  : function name of function that called the panic script' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# component : component of the function' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# probe     : probe point' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# timestamp : timestamp of when the script was called' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# appid     : application ID' || CHR(10) ||"
   Print #fileNo, addTab(2); "'# apphld    : application handle' || CHR(10) ||"
   Print #fileNo, addTab(2); "'' || CHR(10) ||"
   Print #fileNo, addTab(2); "'while [ ""$#"" -gt ""0"" ]' || CHR(10) ||"
   Print #fileNo, addTab(2); "'do' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   TAG=; VALUE=;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   TAG=`echo $1| sed ''s/=.*//''`; VALUE=`echo $1| sed ''s/''$TAG''=*//''`;' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'   if [ ! -n ""$VALUE"" ]' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   then' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      break' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   else' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      case $TAG in' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         INSTANCE)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            instance=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         DATABASE)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            database=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         DBPART)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            dbpart=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         PID)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            pid=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         TID)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            tid=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         FUNCTION)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            function=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         COMPONENT)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            component=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         PROBE)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            probe=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         TIMESTAMP)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            timestamp=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         APPID)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            appid=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         APPHLD)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            apphld=$VALUE' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'         *)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            #echo unknown TAG:$TAG' || CHR(10) ||"
   Print #fileNo, addTab(2); "'            ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      esac' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   fi' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   shift' || CHR(10) ||"
   Print #fileNo, addTab(2); "'done' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'export HOME=`lsuser -f -a home $instance | grep home= | awk -F= ''{ print $2 }''`' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'outFile=$HOME/sqllib/db2dump/db2cos.rpt.""$database"".`date ''+%Y.%m.%d-%H:%M:%S''`' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'# Process type ($1 is the type due to the ''shift'' in the above code)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'typeReason=$1' || CHR(10) ||"
   Print #fileNo, addTab(2); "'typeDesc=$2' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'shift;shift' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'case ""$typeReason"" in' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   DATA_COR)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        # data corruption area' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        # Order of input arguments' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        # 1- File/Lv name' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        # 2- Offset into file/lv in bytes.' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        # 3- Pagesize in bytes' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""**Corruption Details***""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""File/Lv name: $1""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Offset      : $2""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Page size   : $3""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Node Number : $dbpart""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      } >>  $outFile' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   TRAP)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      # trap area' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   DB2_TRC)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      # trace area - script was invoked from inside a trace point' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'      # Uncomment this if you''d like trace to be turned off' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      # db2trc dump /tmp/trc.dmp' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      # db2trc off' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   LOCKTIMEOUT)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Lock Timeout Caught""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        date' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Instance:        "" $instance' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Database:        "" $database' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Partition Number:"" $dbpart' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""PID:             "" $pid' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""TID:             "" $tid' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Function:        "" $function' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Component:       "" $component' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Probe:           "" $probe' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Timestamp:       "" $timestamp' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""AppID:           "" $appid' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""AppHdl:          "" $apphld' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'        db2 connect to $database' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        db2 ""CALL "; g_qualProcNameGetSnapshot; "(NULL, 2, 0, NULL, NULL, 1, ?, ?)"" ' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      } >>  $outFile' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   DEADLOCK)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Lock Deadlock Caught""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        date' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Instance:        "" $instance' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Database:        "" $database' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Partition Number:"" $dbpart' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""PID:             "" $pid' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""TID:             "" $tid' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Function:        "" $function' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Component:       "" $component' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Probe:           "" $probe' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Timestamp:       "" $timestamp' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""AppID:           "" $appid' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""AppHdl:          "" $apphld' || CHR(10) ||"
   Print #fileNo, addTab(2); "CHR(10) ||"
   Print #fileNo, addTab(2); "'        db2 connect to $database' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        db2 ""CALL "; g_qualProcNameGetSnapshot; "(NULL, 2, 0, NULL, NULL, 1, ?, ?)""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      } >>  $outFile' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   SQLCODE)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      {' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""SQLCODE caught""' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        date' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Instance:        "" $instance' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Database:        "" $database' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Partition Number:"" $dbpart' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""PID:             "" $pid' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""TID:             "" $tid' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Function:        "" $function' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Component:       "" $component' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Probe:           "" $probe' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""Timestamp:       "" $timestamp' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""AppID:           "" $appid' || CHR(10) ||"
   Print #fileNo, addTab(2); "'        echo ""AppHdl:          "" $apphld' || CHR(10) ||"
   Print #fileNo, addTab(2); "'      } >>  $outFile' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   *)' || CHR(10) ||"
   Print #fileNo, addTab(2); "'     exit # unknown type' || CHR(10) ||"
   Print #fileNo, addTab(2); "'   ;;' || CHR(10) ||"
   Print #fileNo, addTab(2); "'esac' || CHR(10)"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for Explaining MDS error codes
   ' ####################################################################################################################

   Dim qualProcedureNameErr As String
   qualProcedureNameErr = genQualProcName(g_sectionIndexHelp, spnError, ddlType)

   printSectionHeader "SP for Explaining MDS error codes", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameErr
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "sqlState_in", "INTEGER", True, "SQLSTATE of the MDS error"
   genProcParm fileNo, "OUT", "text_out", "VARCHAR(4000)", False, "description of the MDS error"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_delimLine", "VARCHAR(80)", "'--------------------------------------------------------------------------------'"
   genVarDecl fileNo, "v_offset", "VARCHAR(30)", "'                      '"
   genSpLogDecl fileNo
   genSpLogProcEnter fileNo, qualProcedureNameErr, ddlType, , "sqlState_in", "'text_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET text_out = '';"
 
   genProcSectionHeader fileNo, "identify error details"
   Print #fileNo, addTab(1); "FOR errorLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "E.TECHID,"
   Print #fileNo, addTab(3); "E.BUSID,"
   Print #fileNo, addTab(3); "E.MESSAGEPATTERN,"
   Print #fileNo, addTab(3); "E.CONTEXT"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameErrorMessage; " E"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "sqlState_in IS NULL"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "E.TECHID = sqlState_in"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "add headline", 2, True
   Print #fileNo, addTab(2); "IF text_out = '' THEN"
   Print #fileNo, addTab(3); "SET text_out = CHR(10) || CHR(10) || v_delimLine;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET text_out = text_out || CHR(10) || CHR(10) || '------------' || CHR(10);"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "add error details", 2
   Print #fileNo, addTab(2); "SET text_out = text_out ||"
   Print #fileNo, addTab(9); " CHR(10) || CHR(10) || 'SQLSTATE            : '   || RTRIM(CAST(techID AS CHAR(20))) ||"
   Print #fileNo, addTab(9); " CHR(10) || CHR(10) || 'Business Error Code : '   || COALESCE(RTRIM(CAST(BUSID AS CHAR(20))), '-') ||"
   Print #fileNo, addTab(9); " CHR(10) || CHR(10) || 'Message Pattern     : ''' || MESSAGEPATTERN || '''' ||"
   Print #fileNo, addTab(9); " CHR(10) || CHR(10) || 'Error Context       : '   ||"
   Print #fileNo, addTab(9); " ("
   Print #fileNo, addTab(10); " CASE WHEN"
   Print #fileNo, addTab(11); " CONTEXT IS NULL OR CONTEXT = ''"
   Print #fileNo, addTab(10); " THEN"
   Print #fileNo, addTab(11); " '-'"
   Print #fileNo, addTab(10); " ELSE"
   Print #fileNo, addTab(11); " REPLACE(CONTEXT, '\n', CHR(10) || v_offset)"
   Print #fileNo, addTab(10); " END"
   Print #fileNo, addTab(9); " ) ||"
   Print #fileNo, addTab(9); " CHR(10);"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF text_out = CAST('' AS VARCHAR(1)) THEN"
   genProcSectionHeader fileNo, "no error details available", 2, True
   Print #fileNo, addTab(2); "SET text_out = 'SQLSTATE ''' || RTRIM(CAST(sqlState_in AS CHAR(20))) || ''' unknown';"
   Print #fileNo, addTab(1); "ELSE"
   genProcSectionHeader fileNo, "add trailing line", 2, True
   Print #fileNo, addTab(2); "SET text_out = text_out || CHR(10) || CHR(10) || v_delimLine || CHR(10) || CHR(10);"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameErr, ddlType, , "sqlState_in", "'text_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for activating a configuration profile
   ' ####################################################################################################################

   Dim qualProcedureNameSetCfgProfile As String
   qualProcedureNameSetCfgProfile = genQualProcName(g_sectionIndexDbAdmin, spnSetCfg, ddlType)

   printSectionHeader "SP for activating a configuration profile", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetCfgProfile
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "profileName_in", "VARCHAR(20)", True, "(optional) name of the profile to apply"

   genProcParm fileNo, "IN", "objectType_in", "VARCHAR(10)", True, "(optional) name of the object type to configure"
   genProcParm fileNo, "IN", "objectSchema_in", g_dbtDbSchemaName, True, "(optional) name of the object schema to configure"
   genProcParm fileNo, "IN", "objectName_in", "VARCHAR(50)", True, "(optional) name of the object to configure"
   genProcParm fileNo, "IN", "parameter_in", "VARCHAR(20)", True, "(optional) name of the parameter to configure"

   genProcParm fileNo, "OUT", "rowCountExec_out", "INTEGER", True, "number of configuration statements executed"
   genProcParm fileNo, "OUT", "rowCountList_out", "INTEGER", False, "number of configuration statements not executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "notFound", "02000"
   genCondDecl fileNo, "colNotKnown", "42703"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_ignoreError", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_ignoreWarning", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_parameter", "VARCHAR(20)", "NULL"
   genVarDecl fileNo, "v_valueDiffers", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_colNotKnown", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_valueStr", "VARCHAR(60)", "NULL"
   genVarDecl fileNo, "v_oldValueStr", "VARCHAR(60)", "NULL"
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_osPlatform", "VARCHAR(5)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE parmValueCursor CURSOR WITH RETURN TO CALLER FOR v_stmnt;"
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "IF v_ignoreError = 1 THEN"
   Print #fileNo, addTab(3); "-- just ignore"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR colNotKnown"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_colNotKnown = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "IF v_ignoreError = 1 THEN"
   Print #fileNo, addTab(3); "-- just ignore"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "IF v_ignoreWarning = 1 THEN"
   Print #fileNo, addTab(3); "-- just ignore"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "IF v_ignoreError = 1 THEN"
   Print #fileNo, addTab(3); "-- just ignore"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
 
   genProcSectionHeader fileNo, "temporary table for statements"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); tempTabNameStatement
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "seqNo       INTEGER      GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),"
   Print #fileNo, addTab(2); "mode        CHAR(1),     -- 'E' - execute (internally)"
   Print #fileNo, addTab(2); "                         -- 'e' - execute (externally)"
   Print #fileNo, addTab(2); "                         -- 'A' - already"
   Print #fileNo, addTab(2); "                         -- 'I' - illegal (parameter not supported)"
   Print #fileNo, addTab(2); "useAdminCmd "; g_dbtBoolean; "     DEFAULT 0,"
   Print #fileNo, addTab(2); "objectType  VARCHAR(10),"
   Print #fileNo, addTab(2); "cmd         VARCHAR(10)  DEFAULT 'db2',"
   Print #fileNo, addTab(2); "statement   VARCHAR(120),"
   Print #fileNo, addTab(2); "oldsetting  VARCHAR(20)"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True
 
   genSpLogProcEnter fileNo, qualProcedureNameSetCfgProfile, ddlType, , "mode_in", "'profileName_in", "'objectType_in", _
                             "'objectSchema_in", "'objectName_in", "'parameter_in", "rowCountExec_out", "rowCountList_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET rowCountExec_out = 0;"
   Print #fileNo, addTab(1); "SET rowCountList_out = 0;"
 
   genProcSectionHeader fileNo, "determine OS-Platform"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE SERVER_PLATFORM"
   Print #fileNo, addTab(4); "WHEN  0 THEN 'UNK'   -- Unknown platform"
   Print #fileNo, addTab(4); "WHEN  1 THEN 'OS2'   -- OS/2"
   Print #fileNo, addTab(4); "WHEN  2 THEN 'DOS'   -- DOS"
   Print #fileNo, addTab(4); "WHEN  3 THEN 'WIN'   -- Windows"
   Print #fileNo, addTab(4); "WHEN  4 THEN 'AIX'   -- AIX"
   Print #fileNo, addTab(4); "WHEN  5 THEN 'NT'    -- NT"
   Print #fileNo, addTab(4); "WHEN  6 THEN 'HP'    -- HP"
   Print #fileNo, addTab(4); "WHEN  7 THEN 'SUN'   -- Sun"
   Print #fileNo, addTab(4); "WHEN  8 THEN 'MVS'   -- MVS (client via DRDA)"
   Print #fileNo, addTab(4); "WHEN  9 THEN '400'   -- AS400 (client via DRDA)"
   Print #fileNo, addTab(4); "WHEN 10 THEN 'VM'    -- VM (client via DRDA)"
   Print #fileNo, addTab(4); "WHEN 11 THEN 'VSE'   -- VSE (client via DRDA)"
   Print #fileNo, addTab(4); "WHEN 12 THEN 'UDRD'  -- Unknown DRDA Client"
   Print #fileNo, addTab(4); "WHEN 13 THEN 'SNI'   -- Siemens Nixdorf"
   Print #fileNo, addTab(4); "WHEN 14 THEN 'MacC'  -- Macintosh Client"
   Print #fileNo, addTab(4); "WHEN 15 THEN 'W95'   -- Windows 95"
   Print #fileNo, addTab(4); "WHEN 16 THEN 'SCO'   -- SCO"
   Print #fileNo, addTab(4); "WHEN 17 THEN 'SIGR'  -- Silicon Graphic"
   Print #fileNo, addTab(4); "WHEN 18 THEN 'LINUX' -- Linux"
   Print #fileNo, addTab(4); "WHEN 19 THEN 'DYNIX' -- DYNIX/ptx"
   Print #fileNo, addTab(4); "WHEN 20 THEN 'AIX64' -- AIX 64 bit"
   Print #fileNo, addTab(4); "WHEN 21 THEN 'SUN64' -- Sun 64 bit"
   Print #fileNo, addTab(4); "WHEN 22 THEN 'HP64'  -- HP 64 bit"
   Print #fileNo, addTab(4); "WHEN 23 THEN 'NT64'  -- NT 64 bit"
   Print #fileNo, addTab(4); "WHEN 24 THEN 'L390'  -- Linux for S/390"
   Print #fileNo, addTab(4); "WHEN 25 THEN 'L900'  -- Linux for z900"
   Print #fileNo, addTab(4); "WHEN 26 THEN 'LIA64' -- Linux for IA64"
   Print #fileNo, addTab(4); "WHEN 27 THEN 'LPPC'  -- Linux for PPC"
   Print #fileNo, addTab(4); "WHEN 28 THEN 'LPP64' -- Linux for PPC64"
   Print #fileNo, addTab(4); "WHEN 29 THEN 'OS390' -- OS/390 Tools (CC, DW)"
   Print #fileNo, addTab(4); "WHEN 30 THEN 'L8664' -- Linux for x86-64"
   Print #fileNo, addTab(4); "WHEN 31 THEN 'HPI32' -- HP-UX Itanium 32bit"
   Print #fileNo, addTab(4); "WHEN 32 THEN 'HPI64' -- HP-UX Itanium 64bit"
   Print #fileNo, addTab(4); "WHEN 33 THEN 'S8632' -- Sun x86 32bit"
   Print #fileNo, addTab(4); "WHEN 34 THEN 'S8664' -- Sun x86-64 64bit"
   Print #fileNo, addTab(4); "ELSE RTRIM(CAST(SERVER_PLATFORM AS CHAR(5)))"
   Print #fileNo, addTab(3); "END"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_osPlatform"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "TABLE(SYSPROC.SNAPSHOT_DATABASE(CURRENT SERVER, -1)) X"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); "SET v_osPlatform = COALESCE(v_osPlatform, 'AIX64');"

   genProcSectionHeader fileNo, "determine DB-release"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_ignoreError   = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "SET v_ignoreWarning = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "FOR paramLoop AS"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_Param"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "profileName,"
   Print #fileNo, addTab(3); "objectType,"
   Print #fileNo, addTab(3); "schemaName,"
   Print #fileNo, addTab(3); "objectName,"
   Print #fileNo, addTab(3); "parameter,"
   Print #fileNo, addTab(3); "value,"
   Print #fileNo, addTab(3); "sequenceNo,"
   Print #fileNo, addTab(3); "prio"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "P.PROFILENAME,"
   Print #fileNo, addTab(4); "P.OBJECTTYPE,"
   Print #fileNo, addTab(4); "P.SCHEMANAME,"
   Print #fileNo, addTab(4); "P.OBJECTNAME,"
   Print #fileNo, addTab(4); "P.PARAMETER,"
   Print #fileNo, addTab(4); "P."; g_anValue; ","
   Print #fileNo, addTab(4); "P.SEQUENCENO,"
   Print #fileNo, addTab(4); "ROWNUMBER() OVER ("
   Print #fileNo, addTab(5); "PARTITION BY P.OBJECTTYPE, P.SCHEMANAME, P.OBJECTNAME, P.PARAMETER"
   Print #fileNo, addTab(5); "ORDER BY COALESCE(P.MINDBRELEASE, 0) DESC, (CASE WHEN COALESCE(P.PROFILENAME, '') = '' THEN 1 ELSE 0 END) ASC"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameDbCfgProfile; " P"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "COALESCE(P.PROFILENAME, '') = ''"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "UPPER(P.PROFILENAME) = UPPER(profileName_in)"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(objectType_in, P.OBJECTTYPE) = P.OBJECTTYPE"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UPPER(COALESCE(objectSchema_in, P.SCHEMANAME, '')) = UPPER(COALESCE(P.SCHEMANAME, ''))"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UPPER(COALESCE(objectName_in, P.OBJECTNAME)) = UPPER(P.OBJECTNAME)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UPPER(COALESCE(parameter_in, P.PARAMETER)) = UPPER(P.PARAMETER)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(P.MINDBRELEASE, v_db2Release) <= v_db2Release"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(serverPlatform, v_osPlatform) = v_osPlatform"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "objectType AS c_objectType,"
   Print #fileNo, addTab(3); "schemaName AS c_schemaName,"
   Print #fileNo, addTab(3); "objectName AS c_objectName,"
   Print #fileNo, addTab(3); "parameter  AS c_parameter,"
   Print #fileNo, addTab(3); "value      AS c_value"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_Param"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "prio = 1"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE c_objectType"
   Print #fileNo, addTab(5); "WHEN 'DBM'        THEN 1"
   Print #fileNo, addTab(5); "WHEN 'DB'         THEN 2"
   Print #fileNo, addTab(5); "WHEN 'BUFFERPOOL' THEN 3"
   Print #fileNo, addTab(5); "WHEN 'SEQUENCE'   THEN 4"
   Print #fileNo, addTab(5); "ELSE 5"
   Print #fileNo, addTab(4); "END"
   Print #fileNo, addTab(3); ") ASC,"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE WHEN COALESCE(profileName, '') = '' THEN 0 ELSE 10 END"
   Print #fileNo, addTab(3); ") ASC,"
   Print #fileNo, addTab(3); "COALESCE(sequenceNo,0) ASC,"
   Print #fileNo, addTab(3); "parameter ASC,"
   Print #fileNo, addTab(3); "schemaName ASC,"
   Print #fileNo, addTab(3); "objectName ASC"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader fileNo, "process object types individually", 2, True
   Print #fileNo, addTab(2); "IF c_objectType = 'DBM' THEN"
 
   genProcSectionHeader fileNo, "determine whether config parameter is set correctly", 3, True
   Print #fileNo, addTab(3); "SET v_colNotKnown = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "SET v_oldValueStr = CAST(NULL AS VARCHAR(1));"

   Print #fileNo, addTab(3); "SET v_valueStr ="
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN c_value = 'ON'  THEN '1'"
   Print #fileNo, addTab(6); "WHEN c_value = 'YES' THEN '1'"
   Print #fileNo, addTab(6); "WHEN c_value = 'OFF' THEN '0'"
   Print #fileNo, addTab(6); "WHEN c_value = 'NO'  THEN '0'"
   Print #fileNo, addTab(6); "WHEN "; g_qualFuncNameIsNumeric; "(c_value) = 1 THEN c_value"
   Print #fileNo, addTab(6); "ELSE '''' || c_value || ''''"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || c_parameter || ' AS CHAR(20))), (CASE WHEN RTRIM(CHAR(' || c_parameter || ')) <> RTRIM(CHAR(' || v_valueStr || ')) THEN 1 ELSE 0 END) FROM TABLE(SYSFUN.GET_DBM_CONFIG()) AS DBMCFG WHERE DBMCONFIG_TYPE = 0';"
   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_colNotKnown = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(4); "OPEN parmValueCursor;"
   Print #fileNo,
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "parmValueCursor"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_oldValueStr,"
   Print #fileNo, addTab(5); "v_valueDiffers"
   Print #fileNo, addTab(4); ";"
   Print #fileNo,
   Print #fileNo, addTab(4); "CLOSE parmValueCursor WITH RELEASE;"
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "determine update statement", 3
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'UPDATE DBM CFG USING ' || c_parameter || ' ' || c_value;"

   genProcSectionHeader fileNo, "store statement in temporary table", 3
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatement
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "mode,"
   Print #fileNo, addTab(4); "objectType,"
   Print #fileNo, addTab(4); "statement,"
   Print #fileNo, addTab(4); "oldSetting"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE WHEN v_colNotKnown = 1 THEN 'I' WHEN v_valueDiffers = 1 THEN 'e' ELSE 'A' END,"
   Print #fileNo, addTab(4); "c_objectType,"
   Print #fileNo, addTab(4); "v_stmntTxt,"
   Print #fileNo, addTab(4); "CASE WHEN v_oldValueStr = '' THEN CAST(NULL AS VARCHAR(1)) ELSE v_oldValueStr END"
   Print #fileNo, addTab(3); ");"

   Print #fileNo, addTab(2); "ELSEIF c_objectType = 'DB' THEN"
 
   genProcSectionHeader fileNo, "determine whether config parameter is set correctly", 3, True
   Print #fileNo, addTab(3); "SET v_parameter = (CASE WHEN c_parameter = 'LOGFILSIZ' THEN 'LOGFILSZ' ELSE c_parameter END);"
   Print #fileNo, addTab(3); "SET v_colNotKnown = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "SET v_oldValueStr = CAST(NULL AS VARCHAR(1));"
   Print #fileNo, addTab(3); "SET v_valueStr ="
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN c_value = 'ON'  THEN '1'"
   Print #fileNo, addTab(6); "WHEN c_value = 'YES' THEN '1'"
   Print #fileNo, addTab(6); "WHEN c_value = 'OFF' THEN '0'"
   Print #fileNo, addTab(6); "WHEN c_value = 'NO'  THEN '0'"
   Print #fileNo, addTab(6); "WHEN "; g_qualFuncNameIsNumeric; "(c_value) = 1 THEN c_value"
   Print #fileNo, addTab(6); "ELSE '''' || c_value || ''''"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'SELECT RTRIM(CAST(' || v_parameter || ' AS CHAR(20))), (CASE WHEN RTRIM(CHAR(' || v_parameter || ')) <> RTRIM(CHAR(' || v_valueStr || ')) THEN 1 ELSE 0 END) FROM SESSION.DB_CONFIG WHERE DBCONFIG_TYPE = 0';"

   Print #fileNo,
   Print #fileNo, addTab(3); "CALL SYSPROC.GET_DB_CONFIG();"
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_colNotKnown = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(4); "OPEN parmValueCursor;"
   Print #fileNo,
   Print #fileNo, addTab(4); "FETCH"
   Print #fileNo, addTab(5); "parmValueCursor"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_oldValueStr,"
   Print #fileNo, addTab(5); "v_valueDiffers"
   Print #fileNo, addTab(4); ";"
   Print #fileNo,
   Print #fileNo, addTab(4); "CLOSE parmValueCursor WITH RELEASE;"
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "determine update statement", 3
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'UPDATE DB CFG FOR ' || CURRENT SERVER || ' USING ' || c_parameter || ' ' || c_value;"

   genProcSectionHeader fileNo, "store statement in temporary table", 4
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatement
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "mode,"
   Print #fileNo, addTab(4); "useAdminCmd,"
   Print #fileNo, addTab(4); "objectType,"
   Print #fileNo, addTab(4); "statement,"
   Print #fileNo, addTab(4); "oldSetting"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE WHEN v_colNotKnown = 1 THEN 'I' WHEN v_valueDiffers = 1 THEN 'E' ELSE 'A' END,"
   Print #fileNo, addTab(4); "1,"
   Print #fileNo, addTab(4); "c_objectType,"
   Print #fileNo, addTab(4); "v_stmntTxt,"
   Print #fileNo, addTab(4); "CASE WHEN v_oldValueStr = '' THEN CAST(NULL AS VARCHAR(1)) ELSE v_oldValueStr END"
   Print #fileNo, addTab(3); ");"

   Print #fileNo, addTab(2); "ELSEIF c_objectType = 'BUFFERPOOL' THEN"
   Print #fileNo, addTab(3); "IF c_parameter = 'NPAGES' THEN"
 
   Print #fileNo, addTab(4); "FOR bpSizeLoop AS"
   Print #fileNo, addTab(5); "SELECT"
   Print #fileNo, addTab(6); "B.BPNAME,"
   Print #fileNo, addTab(6); "B.NPAGES"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); "SYSCAT.BUFFERPOOLS B"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "B.BPNAME LIKE c_objectName"
   Print #fileNo, addTab(5); "FOR READ ONLY"
 
   Print #fileNo, addTab(4); "DO"
 
   genProcSectionHeader fileNo, "if NPAGES does not match target value, update it", 5, True
   Print #fileNo, addTab(5); "SET v_stmntTxt = 'ALTER BUFFERPOOL ' || c_objectName || ' SIZE ' || c_value;"

   genProcSectionHeader fileNo, "store statement in temporary table", 5
   Print #fileNo, addTab(5); "INSERT INTO"
   Print #fileNo, addTab(6); tempTabNameStatement
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "mode,"
   Print #fileNo, addTab(6); "objectType,"
   Print #fileNo, addTab(6); "statement,"
   Print #fileNo, addTab(6); "oldSetting"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "VALUES"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "CASE WHEN NPAGES = CAST(c_value AS INTEGER) THEN 'A' ELSE 'E' END,"
   Print #fileNo, addTab(6); "c_objectType,"
   Print #fileNo, addTab(6); "v_stmntTxt,"
   Print #fileNo, addTab(6); "RTRIM(CAST(NPAGES AS CHAR(20)))"
   Print #fileNo, addTab(5); ");"
   Print #fileNo, addTab(4); "END FOR;"
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo, addTab(2); "ELSEIF c_objectType = 'SEQUENCE' THEN"
   Print #fileNo, addTab(3); "IF c_parameter = 'CACHE SIZE' THEN"
 
   Print #fileNo, addTab(4); "FOR seqCacheLoop AS"
   Print #fileNo, addTab(5); "SELECT"
   Print #fileNo, addTab(6); "S.SEQSCHEMA,"
   Print #fileNo, addTab(6); "S.SEQNAME,"
   Print #fileNo, addTab(6); "S.CACHE"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); "SYSCAT.SEQUENCES S"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "S.SEQSCHEMA LIKE c_schemaName"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "S.SEQNAME LIKE c_objectName"
   Print #fileNo, addTab(5); "FOR READ ONLY"
 
   Print #fileNo, addTab(4); "DO"

   genProcSectionHeader fileNo, "if CACHE size does not match target value, update it", 5, True
   Print #fileNo, addTab(5); "IF CAST(c_value AS INTEGER) = 1 THEN"
   Print #fileNo, addTab(6); "SET v_stmntTxt = 'ALTER SEQUENCE ' || RTRIM(SEQSCHEMA) || '.' || SEQNAME || ' NO CACHE';"
   Print #fileNo, addTab(5); "ELSE"
   Print #fileNo, addTab(6); "SET v_stmntTxt = 'ALTER SEQUENCE ' || RTRIM(SEQSCHEMA) || '.' || SEQNAME || ' CACHE ' || c_value;"
   Print #fileNo, addTab(5); "END IF;"

   genProcSectionHeader fileNo, "store statement in temporary table", 5
   Print #fileNo, addTab(5); "INSERT INTO"
   Print #fileNo, addTab(6); tempTabNameStatement
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "mode,"
   Print #fileNo, addTab(6); "objectType,"
   Print #fileNo, addTab(6); "statement,"
   Print #fileNo, addTab(6); "oldSetting"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "VALUES"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "CASE WHEN CACHE = CAST(c_value AS INTEGER) THEN 'A' ELSE 'E' END,"
   Print #fileNo, addTab(6); "c_objectType,"
   Print #fileNo, addTab(6); "v_stmntTxt,"
   Print #fileNo, addTab(6); "RTRIM(CAST(CACHE AS CHAR(20)))"
   Print #fileNo, addTab(5); ");"
   Print #fileNo, addTab(4); "END FOR;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "ELSEIF c_objectType = 'DBPROFILE' THEN"
 
   genProcSectionHeader fileNo, "determine whether config parameter is set correctly", 3, True
   Print #fileNo, addTab(3); "SET v_colNotKnown = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "SET v_oldValueStr = CAST(NULL AS VARCHAR(1));"
   Print #fileNo, addTab(3); "SET v_valueStr = COALESCE(c_value, '');"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "LEFT(REG_VAR_VALUE, 100)"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_oldValueStr"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "TABLE(SYSPROC.REG_LIST_VARIABLES()) AS REGISTRYINFO"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "RTRIM(LTRIM(UPPER(REG_VAR_NAME))) = RTRIM(LTRIM(UPPER(c_parameter)))"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LEVEL = 'I'"
   Print #fileNo, addTab(3); "FETCH FIRST 1 ROW ONLY;"

   genProcSectionHeader fileNo, "determine update statement", 3
   Print #fileNo, addTab(3); "SET v_stmntTxt = RTRIM(LTRIM(UPPER(c_parameter))) || '=' || c_value;"

   genProcSectionHeader fileNo, "store statement in temporary table", 4
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatement
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "mode,"
   Print #fileNo, addTab(4); "useAdminCmd,"
   Print #fileNo, addTab(4); "objectType,"
   Print #fileNo, addTab(4); "cmd,"
   Print #fileNo, addTab(4); "statement,"
   Print #fileNo, addTab(4); "oldSetting"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "CASE WHEN (v_oldValueStr IS NULL) OR (v_oldValueStr <> v_valueStr) THEN 'e' ELSE 'A' END,"
   Print #fileNo, addTab(4); "0,"
   Print #fileNo, addTab(4); "c_objectType,"
   Print #fileNo, addTab(4); "'db2set',"
   Print #fileNo, addTab(4); "v_stmntTxt,"
   Print #fileNo, addTab(4); "v_oldValueStr"
   Print #fileNo, addTab(3); ");"

   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo, addTab(1); "SET v_ignoreError = "; gc_dbFalse; ";"
 
   genProcSectionHeader fileNo, "count number of statements"
   Print #fileNo, addTab(1); "SET rowCountExec_out = (SELECT COUNT(*) FROM SESSION.statements WHERE mode = 'E');"
   Print #fileNo, addTab(1); "SET rowCountList_out = (SELECT COUNT(*) FROM SESSION.statements WHERE mode NOT IN ('E','A'));"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "FOR stmntLoop AS"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "useAdminCmd,"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); tempTabNameStatement
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "MODE = 'E'"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "IF useAdminCmd = 1 THEN"
   Print #fileNo, addTab(4); "CALL SYSPROC.ADMIN_CMD(statement);"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE statement;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"
 
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(1); "SET v_ignoreWarning = "; gc_dbFalse; ";"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "mode,"
   Print #fileNo, addTab(5); "oldSetting,"
   Print #fileNo, addTab(5); "cmd,"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit fileNo, qualProcedureNameSetCfgProfile, ddlType, , "mode_in", "'profileName_in", "'objectType_in", _
                            "'objectSchema_in", "'objectName_in", "'parameter_in", "rowCountExec_out", "rowCountList_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader "SP for activating a configuration profile", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetCfgProfile
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "profileName_in", "VARCHAR(20)", True, "(optional) name of the profile to apply"
   genProcParm fileNo, "OUT", "rowCountExec_out", "INTEGER", True, "number of configuration statements executed"
   genProcParm fileNo, "OUT", "rowCountList_out", "INTEGER", False, "number of configuration statements not executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetCfgProfile; "(mode_in, profileName_in, NULL, NULL, NULL, NULL, rowCountExec_out, rowCountList_out);"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for activating a configuration profile", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetCfgProfile
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "profileName_in", "VARCHAR(20)", True, "(optional) name of the profile to apply"
   genProcParm fileNo, "IN", "objectType_in", "VARCHAR(10)", True, "(optional) name of the object type to configure"
   genProcParm fileNo, "OUT", "rowCountExec_out", "INTEGER", True, "number of configuration statements executed"
   genProcParm fileNo, "OUT", "rowCountList_out", "INTEGER", False, "number of configuration statements not executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetCfgProfile; "(mode_in, profileName_in, objectType_in, NULL, NULL, NULL, rowCountExec_out, rowCountList_out);"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for activating the 'default configuration profile'
   ' ####################################################################################################################

   printSectionHeader "SP for activating the default configuration profile", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetCfgProfile
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "rowCountExec_out", "INTEGER", True, "number of configuration statements executed"
   genProcParm fileNo, "OUT", "rowCountList_out", "INTEGER", False, "number of configuration statements not executed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_profileName", "VARCHAR(20)", "NULL"
   genVarDecl fileNo, "v_server", "VARCHAR(20)", "NULL"
   genSpLogDecl fileNo
 
   genSpLogProcEnter fileNo, qualProcedureNameSetCfgProfile, ddlType, , "mode_in", "rowCountExec_out", "rowCountList_out"
 
   genProcSectionHeader fileNo, "determine current server"
   Print #fileNo, addTab(1); "SET v_server = UPPER(CURRENT SERVER);"

   genProcSectionHeader fileNo, "guess default profile"
   Print #fileNo, addTab(1); "IF LEFT(v_server,"; CStr(Len(productKey)); ") = '"; UCase(productKey); "' THEN"
   Print #fileNo, addTab(2); "SET v_profileName = 'DAI';"
   Print #fileNo, addTab(1); "ELSEIF LEFT(v_server,3) = 'MDS' THEN"
   Print #fileNo, addTab(2); "SET v_profileName = 'IBM-TEST';"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "SET v_profileName = 'IBM-DEPLOYTEST';"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "set profile"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetCfgProfile; "(mode_in, v_profileName, rowCountExec_out, rowCountList_out);"

   genSpLogProcExit fileNo, qualProcedureNameSetCfgProfile, ddlType, , "mode_in", "rowCountExec_out", "rowCountList_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Private Sub genDdlSetTabCfg( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    SP for configuring tables
   ' ####################################################################################################################

   Dim qualProcedureNameSetTableCfg As String
   qualProcedureNameSetTableCfg = genQualProcName(g_sectionIndexDbAdmin, spnSetTableCfg, ddlType)

   printSectionHeader "SP for configuring tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetTableCfg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "IN", "cfgParmList_in", "VARCHAR(100)", True, "(optional) list of config parameter names to set"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntText", "VARCHAR(2000)", "NULL"
   genVarDecl fileNo, "v_dbSupportsCompression", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_cfgPctFree", "SMALLINT", "0"
   genVarDecl fileNo, "v_cfgIsVolatile", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_cfgUseCompression", g_dbtBoolean, gc_dbFalse
   genSpLogDecl fileNo
 
   Dim tempTabNameStatementTabCfg As String
   tempTabNameStatementTabCfg = tempTabNameStatement & "TabCfg"

   genDdlForTempTableCfg fileNo, 1, True, False, False
   genDdlForTempStatement fileNo, 1, True, , True, True, True, , "TabCfg"

   genSpLogProcEnter fileNo, qualProcedureNameSetTableCfg, ddlType, , "mode_in", "'tabSchema_in", "'tabName_in", "'cfgParmList_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "determine whether DB supports row level compression"
   Print #fileNo, addTab(1); "SET v_dbSupportsCompression = (CASE WHEN "; g_qualFuncNameDb2Release; "() > 8 THEN 1 ELSE 0 END);"

   genProcSectionHeader fileNo, "determine config parameters to deal with"
   Print #fileNo, addTab(1); "IF cfgParmList_in IS NULL THEN"

   Print #fileNo, addTab(2); "SET v_cfgPctFree        = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_cfgIsVolatile     = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_cfgUseCompression = v_dbSupportsCompression;"

   Print #fileNo, addTab(1); "ELSE"
 
   Print #fileNo, addTab(2); "FOR cfgParamLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "UPPER(RTRIM(LTRIM(ELEM))) AS c_elem"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(cfgParmList_in, CAST(',' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "ELEM IS NOT NULL"
   Print #fileNo, addTab(2); "DO"
 
   Print #fileNo, addTab(3); "IF POSSTR(c_elem, 'PCTFREE') > 0 THEN"
   Print #fileNo, addTab(4); "SET v_cfgPctFree = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "ELSEIF POSSTR(c_elem, 'VOLATILE') > 0 THEN"
   Print #fileNo, addTab(4); "SET v_cfgIsVolatile = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "ELSEIF POSSTR(c_elem, 'COMPRESSION') > 0 THEN"
   Print #fileNo, addTab(4); "SET v_cfgUseCompression = v_dbSupportsCompression;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "collect all table names"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); tempTabNameTableCfg
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "tabSchema,"
   Print #fileNo, addTab(2); "tabName"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "TABSCHEMA,"
   Print #fileNo, addTab(2); "TABNAME"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.TABLES"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "TYPE = 'T'"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "loop over table configurations"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"

   Print #fileNo, addTab(3); "'UPDATE ' ||"
   Print #fileNo, addTab(4); "'"; tempTabNameTableCfg; "' ||"
   Print #fileNo, addTab(3); "' SET ' ||"

   Print #fileNo, addTab(4); "'pctFree = '             || (CASE WHEN pctFree             IS NULL THEN 'pctFree'             ELSE RTRIM(CHAR(pctFree            )) END) || ',' ||"
   Print #fileNo, addTab(4); "'isVolatile = '          || (CASE WHEN isVolatile          IS NULL THEN 'isVolatile'          ELSE RTRIM(CHAR(isVolatile         )) END) || ',' ||"
   Print #fileNo, addTab(4); "'useCompression = '      || (CASE WHEN useCompression      IS NULL THEN 'useCompression'      ELSE RTRIM(CHAR(useCompression     )) END) || ',' ||"
   Print #fileNo, addTab(4); "'useIndexCompression = ' || (CASE WHEN useIndexCompression IS NULL THEN 'useIndexCompression' ELSE RTRIM(CHAR(useIndexCompression)) END) ||"

   Print #fileNo, addTab(3); "' WHERE ' ||"
   Print #fileNo, addTab(4); "'tabSchema LIKE ''' || COALESCE(SCHEMANAME, '%') || '''' ||"
   Print #fileNo, addTab(5); "' AND ' ||"
   Print #fileNo, addTab(4); "'tabName LIKE ''' || COALESCE(TABLENAME, '%') || '''' ||"
   Print #fileNo, addTab(5); "' AND ' ||"
   Print #fileNo, addTab(4); "'NOT (' ||"
   Print #fileNo, addTab(5); "'tabSchema LIKE ''' || COALESCE(SCHEMANAMEEXCLUDED, '-') || '''' ||"
   Print #fileNo, addTab(6); "' AND ' ||"
   Print #fileNo, addTab(5); "'tabName LIKE ''' || COALESCE(TABLENAMEEXCLUDED, '-') || '''' ||"
   Print #fileNo, addTab(4); "')' ||"
   Print #fileNo, addTab(5); "' AND ' ||"
   Print #fileNo, addTab(4); "'tabSchema LIKE ''' || COALESCE(tabSchema_in, '"; g_allSchemaNamePattern; "') || '''' ||"
   Print #fileNo, addTab(5); "' AND ' ||"
   Print #fileNo, addTab(4); "'tabName LIKE ''' || COALESCE(tabName_in, '%') || ''''"
   Print #fileNo, addTab(3); "AS c_stmntTxt"
 
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameTableCfg
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "SEQUENCENO"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE c_stmntTxt;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "loop over tables and update table configuration"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "'ALTER TABLE ' ||"
   Print #fileNo, addTab(4); "RTRIM(tabSchema) || '.' || RTRIM(tabName) ||"

   Print #fileNo, addTab(4); "(CASE WHEN pctFree IS NULL OR v_cfgPctFree = 0 THEN '' ELSE ' PCTFREE ' || RTRIM(CHAR(pctFree)) END) ||"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN isVolatile IS NULL OR v_cfgIsVolatile = "; gc_dbFalse
   Print #fileNo, addTab(6); "THEN ''"
   Print #fileNo, addTab(6); "ELSE (CASE isVolatile WHEN 1 THEN '' ELSE ' NOT' END) || ' VOLATILE'"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN useCompression IS NULL OR v_cfgUseCompression = "; gc_dbFalse
   Print #fileNo, addTab(6); "THEN ''"
   Print #fileNo, addTab(6); "ELSE ' COMPRESS ' || (CASE useCompression WHEN 1 THEN 'YES' ELSE 'NO' END)"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"
   Print #fileNo, addTab(3); "'' AS c_stmntTxt"
 
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); tempTabNameTableCfg
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "NOT ("
   Print #fileNo, addTab(4); "(pctFree IS NULL OR v_cfgPctFree = 0)"

   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(isVolatile IS NULL OR v_cfgIsVolatile = 0)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(useCompression IS NULL OR v_cfgUseCompression = 0)"

   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "tabSchema ASC,"
   Print #fileNo, addTab(3); "tabName   ASC"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader fileNo, "execute update of table configuration", 3, True
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE c_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store update statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementTabCfg
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "c_stmntTxt"
   Print #fileNo, addTab(3); ");"

   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "keep track of updated table", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "loop over indexes and update index configuration"
   Print #fileNo, addTab(1); "FOR indexLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "'ALTER INDEX ' ||"
   Print #fileNo, addTab(4); "RTRIM(I.INDSCHEMA) || '.' || RTRIM(I.INDNAME) ||"
 
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN C.useIndexCompression IS NULL OR v_cfgUseCompression = "; gc_dbFalse
   Print #fileNo, addTab(6); "THEN ''"
   Print #fileNo, addTab(6); "ELSE"
   Print #fileNo, addTab(7); "' COMPRESS ' ||(CASE C.useIndexCompression WHEN 1 THEN 'YES' ELSE 'NO' END)"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ") ||"
   Print #fileNo, addTab(3); "'' AS c_stmntTxt"
 
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); tempTabNameTableCfg; " C"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.INDEXES I"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "C.tabSchema = I.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "C.tabName = I.TABNAME"

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "NOT ("

   Print #fileNo, addTab(4); "(C.useIndexCompression IS NULL OR v_cfgUseCompression = 0)"
 
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "C.tabSchema ASC,"
   Print #fileNo, addTab(3); "C.tabName   ASC,"
   Print #fileNo, addTab(3); "I.TABSCHEMA ASC,"
   Print #fileNo, addTab(3); "I.TABNAME   ASC"
   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader fileNo, "execute update of index configuration", 3, True
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE c_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store update statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameStatementTabCfg
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "c_stmntTxt"
   Print #fileNo, addTab(3); ");"
 
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "keep track of updated index", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementTabCfg
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameSetTableCfg, ddlType, , "mode_in", "'tabSchema_in", "'tabName_in", "'cfgParmList_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for configuring tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetTableCfg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to configure"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to configure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, qualProcedureNameSetTableCfg, ddlType, , "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetTableCfg; "(mode_in, tabSchema_in, tabName_in, NULL, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameSetTableCfg, ddlType, , "mode_in", "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for configuring tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetTableCfg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of configuration statements"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, qualProcedureNameSetTableCfg, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetTableCfg; "(mode_in, NULL, NULL, NULL, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameSetTableCfg, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Private Sub genDbAdminDdlCompressionEstimation( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If Not supportCompresionEstimation Then
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    SP for estimating effectiveness of row compression
   ' ####################################################################################################################

   Dim qualProcedureNameCompressEstimate As String
   qualProcedureNameCompressEstimate = genQualProcName(g_sectionIndexDbAdmin, spnCompressEstimate, ddlType)
 
   printSectionHeader "SP for estimating effectiveness of row compression", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCompressEstimate
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "(optional) schema name pattern of the table(s) to estimate"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "(optional) name pattern of the table to estimate"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of tables estimated"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 2"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(600)", "NULL"
   genSigMsgVarDecl fileNo
   genSpLogDecl fileNo

   Dim qualTabNameTempStatementInspect As String
   qualTabNameTempStatementInspect = tempTabNameStatement & "Inspect"

   genDdlForTempStatement fileNo, 1, True, 200, True, True, True, , "Inspect"

   genProcSectionHeader fileNo, "temporary table for compression estimate information"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.CompEstimate"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "tabSchema         "; g_dbtDbSchemaName; ","
   Print #fileNo, addTab(2); "tabName           VARCHAR(50),"
   Print #fileNo, addTab(2); "dataPartitionId   INTEGER,"
   Print #fileNo, addTab(2); "compAttribute     CHAR(1),"
   Print #fileNo, addTab(2); "pagesSavedPercent SMALLINT,"
   Print #fileNo, addTab(2); "bytesSavedPercent SMALLINT"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True

   genSpLogProcEnter fileNo, qualProcedureNameCompressEstimate, ddlType, , "'tabSchema_in", "'tabName_in", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "Verify that this DB-Version supports Compression"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"
   Print #fileNo, addTab(1); "IF (v_db2Release < 9.05)THEN"
   genSpLogProcEscape fileNo, qualProcedureNameCompressEstimate, ddlType, -2, "'tabSchema_in", "'tabName_in", "rowCount_out"
   genSignalDdlWithParms "featureNotSupported", fileNo, 2, "ROW COMPRESSION ESTIMATE", "9.5"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "loop over tables and retrieve estimation data"
   Print #fileNo, addTab(1); "FOR tabLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "TABSCHEMA AS c_tabSchema,"
   Print #fileNo, addTab(3); "TABNAME   AS c_tabName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "TYPE = 'T'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(TABSCHEMA)) LIKE COALESCE(UCASE(tabSchema_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(TABNAME)) LIKE COALESCE(UCASE(tabName_in), '%')"
 
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "retrieve statement for estimating this table", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSPECT ROWCOMPESTIMATE TABLE NAME ' || RTRIM(c_tabName) || ' SCHEMA ' || RTRIM(c_tabSchema) || ' RESULTS rowComResult.out';"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); qualTabNameTempStatementInspect
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader fileNo, "execute estimation", 3, True

   genProcSectionHeader fileNo, "use dynamic SQL here to make this compile in all DB2-versions", 3, True
   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(3); "'INSERT INTO ' ||"
   Print #fileNo, addTab(4); "'SESSION.CompEstimate ' ||"
   Print #fileNo, addTab(3); "'(' ||"
   Print #fileNo, addTab(4); "'tabSchema,' ||"
   Print #fileNo, addTab(4); "'tabName,' ||"
   Print #fileNo, addTab(4); "'dataPartitionId,' ||"
   Print #fileNo, addTab(4); "'compAttribute,' ||"
   Print #fileNo, addTab(4); "'pagesSavedPercent,' ||"
   Print #fileNo, addTab(4); "'bytesSavedPercent' ||"
   Print #fileNo, addTab(3); "') ' ||"
   Print #fileNo, addTab(3); "'SELECT ' ||"
   Print #fileNo, addTab(4); "'RTRIM(LEFT(TABSCHEMA, 30)),' ||"
   Print #fileNo, addTab(4); "'RTRIM(LEFT(TABNAME, 50)),' ||"
   Print #fileNo, addTab(4); "'RTRIM(CHAR(DATA_PARTITION_ID)),' ||"
   Print #fileNo, addTab(4); "'COMPRESS_ATTR,' ||"
   Print #fileNo, addTab(4); "'PAGES_SAVED_PERCENT,' ||"
   Print #fileNo, addTab(4); "'BYTES_SAVED_PERCENT ' ||"
   Print #fileNo, addTab(3); "'FROM ' ||"
   Print #fileNo, addTab(4); "'TABLE (SYSPROC.ADMIN_GET_TAB_COMPRESS_INFO(''' || c_tabSchema || ''', ''' || c_tabName || ''', ''ESTIMATE'')) AS T'"
   Print #fileNo, addTab(3); ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count estimated table", 3, True
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "BEGIN"

   genProcSectionHeader fileNo, "declare cursor(s)", 2, True
   Print #fileNo, addTab(2); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameTempStatementInspect
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "seqNo ASC"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"

   Print #fileNo, addTab(2); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "tabSchema,"
   Print #fileNo, addTab(4); "tabName,"
   Print #fileNo, addTab(4); "dataPartitionId,"
   Print #fileNo, addTab(4); "compAttribute     AS CA,"
   Print #fileNo, addTab(4); "pagesSavedPercent AS ""PS[%]"","
   Print #fileNo, addTab(4); "bytesSavedPercent AS ""BS[%]"""
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SESSION.CompEstimate"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "tabSchema,"
   Print #fileNo, addTab(4); "tabName"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "leave cursor(s) open for application", 2
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END;"

   genSpLogProcExit fileNo, qualProcedureNameCompressEstimate, ddlType, , "'tabSchema_in", "'tabName_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for estimating effectiveness of row compression", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCompressEstimate
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of tables estimated"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True

   genSpLogProcEnter fileNo, qualProcedureNameCompressEstimate, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameCompressEstimate; "(mode_in, NULL, NULL, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameCompressEstimate, ddlType, , "mode_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub


 Private Sub genDbAdminDdl5( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   ' ####################################################################################################################
   ' #    View for 'invalid' database object
   ' ####################################################################################################################

   Dim qualViewNameInvalidDbObjects As String
   qualViewNameInvalidDbObjects = genQualViewName(g_sectionIndexDbAdmin, vnInvalidDbObjects, vnInvalidDbObjects, ddlType)

   printSectionHeader "View for 'invalid' database object", fileNo
   Print #fileNo,

   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameInvalidDbObjects
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "OBJTYPE,"
   Print #fileNo, addTab(1); "OBJSCHEMA,"
   Print #fileNo, addTab(1); "OBJNAME,"
   Print #fileNo, addTab(1); "STATUS,"
   Print #fileNo, addTab(1); "STATEMENT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
 
   genProcSectionHeader fileNo, "check for invalid ROUTINEs (FUNCTION, METHOD, PROCEDURE)", , True
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "(CASE ROUTINETYPE WHEN 'F' THEN 'FUNCTION' WHEN 'M' THEN 'METHOD' ELSE 'PROCEDURE' END),"
   Print #fileNo, addTab(2); "ROUTINESCHEMA,"
   Print #fileNo, addTab(2); "ROUTINENAME,"
   Print #fileNo, addTab(2); "(CASE VALID WHEN 'N' THEN 'invalid' ELSE 'inoperative' END),"
   Print #fileNo, addTab(2); "TEXT || CHR(10) || '"; gc_sqlCmdDelim; "' || CHR(10)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.ROUTINES"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "VALID <> 'Y'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ORIGIN = 'Q'"
 
   Print #fileNo, addTab(1); "UNION ALL"
 
   genProcSectionHeader fileNo, "check for invalid VIEWs"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'VIEW',"
   Print #fileNo, addTab(2); "VIEWSCHEMA,"
   Print #fileNo, addTab(2); "VIEWNAME,"
   Print #fileNo, addTab(2); "'invalid',"
   Print #fileNo, addTab(2); "TEXT || CHR(10) || '"; gc_sqlCmdDelim; "' || CHR(10)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.VIEWS"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "VALID <> 'Y'"
 
   Print #fileNo, addTab(1); "UNION ALL"
 
   genProcSectionHeader fileNo, "check for invalid TRIGGERs"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'TRIGGER',"
   Print #fileNo, addTab(2); "TRIGSCHEMA,"
   Print #fileNo, addTab(2); "TRIGNAME,"
   Print #fileNo, addTab(2); "'invalid',"
   Print #fileNo, addTab(2); "TEXT || CHR(10) || '"; gc_sqlCmdDelim; "' || CHR(10)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.TRIGGERS"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "VALID <> 'Y'"
 
   Print #fileNo, addTab(1); "UNION ALL"
 
   genProcSectionHeader fileNo, "check for invalid PACKAGEs"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'PACKAGE',"
   Print #fileNo, addTab(2); "PKGSCHEMA,"
   Print #fileNo, addTab(2); "PKGNAME,"
   Print #fileNo, addTab(2); "(CASE VALID WHEN 'N' THEN 'invalid' ELSE 'inoperative' END),"
   Print #fileNo, addTab(2); "'REBIND PACKAGE ' || RTRIM(PKGSCHEMA) || '.' || RTRIM(PKGNAME) || '"; gc_sqlCmdDelim; "' || CHR(10)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.PACKAGES"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "VALID <> 'Y'"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Procedure for checking validity of database objects
   ' ####################################################################################################################
 
   Dim qualProcNameCheckValidity As String
   qualProcNameCheckValidity = genQualViewName(g_sectionIndexDbAdmin, vnInvalidDbObjects, vnInvalidDbObjects, ddlType)
 
   qualProcNameCheckValidity = genQualProcName(g_sectionIndexDbAdmin, spnCheckValidity, ddlType)

   printSectionHeader "Procedure for checking validity of database objects", fileNo
   Print #fileNo,
 
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameCheckValidity
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of database objects found invalid"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader fileNo, "temporary table for status information"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.ObjStatus"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objType    VARCHAR(15),"
   Print #fileNo, addTab(2); "objSchema  VARCHAR(128),"
   Print #fileNo, addTab(2); "objName    VARCHAR(128),"
   Print #fileNo, addTab(2); "status     VARCHAR(40),"
   Print #fileNo, addTab(2); "statement  CLOB(32000)"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True
 
   genSpLogProcEnter fileNo, qualProcNameCheckValidity, ddlType, , "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter 'rowCount_out'"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "check for invalid VIEWs"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ObjStatus"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objType,"
   Print #fileNo, addTab(2); "objSchema,"
   Print #fileNo, addTab(2); "objName,"
   Print #fileNo, addTab(2); "status,"
   Print #fileNo, addTab(2); "statement"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'VIEW',"
   Print #fileNo, addTab(2); "VIEWSCHEMA,"
   Print #fileNo, addTab(2); "VIEWNAME,"
   Print #fileNo, addTab(2); "'invalid',"
   Print #fileNo, addTab(2); "LEFT(TEXT, 32000)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.VIEWS"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "VALID <> 'Y'"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   genProcSectionHeader fileNo, "check for invalid TRIGGERs"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ObjStatus"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objType,"
   Print #fileNo, addTab(2); "objSchema,"
   Print #fileNo, addTab(2); "objName,"
   Print #fileNo, addTab(2); "status,"
   Print #fileNo, addTab(2); "statement"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'TRIGGER',"
   Print #fileNo, addTab(2); "TRIGSCHEMA,"
   Print #fileNo, addTab(2); "TRIGNAME,"
   Print #fileNo, addTab(2); "'invalid',"
   Print #fileNo, addTab(2); "LEFT(TEXT, 32000)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.TRIGGERS"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "VALID <> 'Y'"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   genProcSectionHeader fileNo, "check for invalid PACKAGEs"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ObjStatus"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objType,"
   Print #fileNo, addTab(2); "objSchema,"
   Print #fileNo, addTab(2); "objName,"
   Print #fileNo, addTab(2); "status,"
   Print #fileNo, addTab(2); "statement"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'PACKAGE',"
   Print #fileNo, addTab(2); "PKGSCHEMA,"
   Print #fileNo, addTab(2); "PKGNAME,"
   Print #fileNo, addTab(2); "(CASE VALID WHEN 'N' THEN 'invalid' ELSE 'inoperative' END),"
   Print #fileNo, addTab(2); "'REBIND PACKAGE ' || RTRIM(PKGSCHEMA) || '.' || RTRIM(PKGNAME)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.PACKAGES"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "VALID <> 'Y'"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   genProcSectionHeader fileNo, "check for invalid ROUTINEs (FUNCTION, METHOD, PROCEDURE)"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ObjStatus"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objType,"
   Print #fileNo, addTab(2); "objSchema,"
   Print #fileNo, addTab(2); "objName,"
   Print #fileNo, addTab(2); "status,"
   Print #fileNo, addTab(2); "statement"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "(CASE ROUTINETYPE WHEN 'F' THEN 'FUNCTION' WHEN 'M' THEN 'METHOD' ELSE 'PROCEDURE' END),"
   Print #fileNo, addTab(2); "ROUTINESCHEMA,"
   Print #fileNo, addTab(2); "ROUTINENAME,"
   Print #fileNo, addTab(2); "(CASE VALID WHEN 'N' THEN 'invalid' ELSE 'inoperative' END),"
   Print #fileNo, addTab(2); "LEFT(TEXT, 32000)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.ROUTINES"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "VALID <> 'Y'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "ORIGIN = 'Q'"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   genProcSectionHeader fileNo, "check for invalid TABLEs, ALIASes, etc"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ObjStatus"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objType,"
   Print #fileNo, addTab(2); "objSchema,"
   Print #fileNo, addTab(2); "objName,"
   Print #fileNo, addTab(2); "status,"
   Print #fileNo, addTab(2); "statement"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "CASE TYPE"
   Print #fileNo, addTab(2); "WHEN 'A' THEN 'ALIAS'"
   Print #fileNo, addTab(2); "WHEN 'H' THEN 'HIERARCHY TABLE'"
   Print #fileNo, addTab(2); "WHEN 'N' THEN 'NICK NAME'"
   Print #fileNo, addTab(2); "WHEN 'S' THEN 'MATERIALIZED QUERY TABLE'"
   Print #fileNo, addTab(2); "WHEN 'T' THEN 'TABLE'"
   Print #fileNo, addTab(2); "WHEN 'U' THEN 'TYPED TABLE'"
   Print #fileNo, addTab(2); "WHEN 'V' THEN 'VIEW'"
   Print #fileNo, addTab(2); "WHEN 'W' THEN 'TYPED VIEW'"
   Print #fileNo, addTab(2); "ELSE '' END"
   Print #fileNo, addTab(1); "),"
   Print #fileNo, addTab(2); "TABSCHEMA,"
   Print #fileNo, addTab(2); "TABNAME,"
   Print #fileNo, addTab(2); "(CASE STATUS WHEN 'C' THEN 'check pending' ELSE 'inoperative' END),"
   Print #fileNo, addTab(2); "CAST(NULL AS VARCHAR(1))"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SYSCAT.TABLES"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "STATUS <> 'N'"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "objType,"
   Print #fileNo, addTab(4); "CAST(RTRIM(LEFT(objSchema, 30)) AS "; g_dbtDbSchemaName; ") AS objSchema,"
   Print #fileNo, addTab(4); "CAST(RTRIM(LEFT(objName,   50)) AS VARCHAR(50)) AS objName,"
   Print #fileNo, addTab(4); "status"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SESSION.ObjStatus"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "objType    ASC,"
   Print #fileNo, addTab(4); "objSchema  ASC,"
   Print #fileNo, addTab(4); "objName    ASC"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 2
   Print #fileNo, addTab(2); "OPEN stmntCursor;"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcExit fileNo, qualProcNameCheckValidity, ddlType, , "rowCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Procedure for revalidating views
   ' ####################################################################################################################

   Dim qualProcNameRevalidateViews As String
   qualProcNameRevalidateViews = genQualProcName(g_sectionIndexDbAdmin, spnRevalidate & "_VIEWS", ddlType)

   printSectionHeader "Procedure for revalidating database views", fileNo
   Print #fileNo,
 
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRevalidateViews
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas of views to revalidate"
   genProcParm fileNo, "IN", "viewNamePattern_in", g_dbtDbViewName, True, "determines the views to revalidate"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "viewCount_out", "INTEGER", False, "number of views revalidated"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "alreadyExist", "42710"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_viewDropStmt", "VARCHAR(300)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , 32000, True
   genDdlForTempGrants fileNo, 1, True

   genSpLogProcEnter fileNo, qualProcNameRevalidateViews, ddlType, , "'schemaNamePattern_in", "'viewNamePattern_in", "mode_in", "viewCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter 'viewCount_out'"
   Print #fileNo, addTab(1); "SET viewCount_out = 0;"

   genProcSectionHeader fileNo, "revalidate invalid VIEWs"
   Print #fileNo, addTab(1); "FOR viewLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "VIEWSCHEMA AS c_viewSchema,"
   Print #fileNo, addTab(3); "VIEWNAME   AS c_viewName,"
   Print #fileNo, addTab(3); "LEFT(TEXT, 128000) AS c_viewDefinition"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.VIEWS"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(VIEWSCHEMA LIKE '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "VALID <> 'Y'"

   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(schemaNamePattern_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(VIEWSCHEMA LIKE schemaNamePattern_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(viewNamePattern_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(VIEWNAME LIKE viewNamePattern_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "FOR READ ONLY"
 
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader fileNo, "determine DROP-Statement", 2, True
   Print #fileNo, addTab(2); "SET v_viewDropStmt = 'DROP VIEW ' || c_viewSchema || '.' || c_viewName;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO SESSION.statements(STATEMENT) VALUES(v_viewDropStmt);"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.statements(STATEMENT) VALUES(LEFT(c_viewDefinition,32000));"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader fileNo, "keep track of GRANTs on View", 3, True
 
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameGrant
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "schemaName,"
   Print #fileNo, addTab(4); "objectName,"
   Print #fileNo, addTab(4); "grantee,"
   Print #fileNo, addTab(4); "granteeType,"
   Print #fileNo, addTab(4); "privilege"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "A.TABSCHEMA,"
   Print #fileNo, addTab(4); "A.TABNAME,"
   Print #fileNo, addTab(4); "A.GRANTEE,"
   Print #fileNo, addTab(4); "A.GRANTEETYPE,"
   Print #fileNo, addTab(4); "'SELECT'"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABAUTH A"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "A.TABSCHEMA = c_viewSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.TABNAME = c_viewName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.SELECTAUTH <> 'N'"

   Print #fileNo, addTab(3); "UNION ALL"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "A.TABSCHEMA,"
   Print #fileNo, addTab(4); "A.TABNAME,"
   Print #fileNo, addTab(4); "A.GRANTEE,"
   Print #fileNo, addTab(4); "A.GRANTEETYPE,"
   Print #fileNo, addTab(4); "'INSERT'"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABAUTH A"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "A.TABSCHEMA = c_viewSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.TABNAME = c_viewName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.INSERTAUTH <> 'N'"

   Print #fileNo, addTab(3); "UNION ALL"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "A.TABSCHEMA,"
   Print #fileNo, addTab(4); "A.TABNAME,"
   Print #fileNo, addTab(4); "A.GRANTEE,"
   Print #fileNo, addTab(4); "A.GRANTEETYPE,"
   Print #fileNo, addTab(4); "'DELETE'"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABAUTH A"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "A.TABSCHEMA = c_viewSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.TABNAME = c_viewName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.DELETEAUTH <> 'N'"

   Print #fileNo, addTab(3); "UNION ALL"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "A.TABSCHEMA,"
   Print #fileNo, addTab(4); "A.TABNAME,"
   Print #fileNo, addTab(4); "A.GRANTEE,"
   Print #fileNo, addTab(4); "A.GRANTEETYPE,"
   Print #fileNo, addTab(4); "'UPDATE'"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABAUTH A"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "A.TABSCHEMA = c_viewSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.TABNAME = c_viewName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.UPDATEAUTH <> 'N'"

   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "re-create View", 3
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_viewDropStmt;"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE c_viewDefinition;"

   genProcSectionHeader fileNo, "set GRANTs on re-created View", 3
   Print #fileNo, addTab(3); "FOR privLoop AS"
   Print #fileNo, addTab(4); "SELECT"

   Print #fileNo, addTab(5); "'GRANT ' || G.privilege || ' ON ' || RTRIM(G.schemaName) || '.' || RTRIM(G.objectName) || ' TO ' ||"
   Print #fileNo, addTab(5); "(CASE G.granteeType WHEN 'P' THEN 'PUBLIC' WHEN 'G' THEN 'GROUP' ELSE 'USER' END) ||"
   Print #fileNo, addTab(5); "(CASE WHEN G.granteeType <> 'P' THEN ' ' || G.grantee ELSE '' END) AS grantStatement"

   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameGrant; " G"

   Print #fileNo, addTab(3); "DO"
   Print #fileNo, addTab(4); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 5, True
   Print #fileNo, addTab(5); "INSERT INTO SESSION.statements(STATEMENT) VALUES(grantStatement);"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE grantStatement;"

   Print #fileNo, addTab(3); "END FOR;"

   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "DELETE FROM "; tempTabNameGrant; ";"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET viewCount_out = viewCount_out + 1;"
 
   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit fileNo, qualProcNameRevalidateViews, ddlType, , "'schemaNamePattern_in", "'viewNamePattern_in", "mode_in", "viewCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Procedure for revalidating triggers
   ' ####################################################################################################################

   Dim qualProcNameRevalidateTriggers As String
   qualProcNameRevalidateTriggers = genQualProcName(g_sectionIndexDbAdmin, spnRevalidate, ddlType, , , , "TRIGGERS")

   printSectionHeader "Procedure for revalidating database triggers", fileNo
   Print #fileNo,
 
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRevalidateTriggers
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas of triggers to revalidate"
   genProcParm fileNo, "IN", "triggerNamePattern_in", "VARCHAR(50)", True, "determines the triggers to revalidate"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "triggerCount_out", "INTEGER", False, "number of triggers revalidated"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "alreadyExist", "42710"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_triggerDropStmt", "VARCHAR(300)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , 32000, True

   genSpLogProcEnter fileNo, qualProcNameRevalidateTriggers, ddlType, , "'schemaNamePattern_in", "'triggerNamePattern_in", "mode_in", "triggerCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter 'triggerCount_out'"
   Print #fileNo, addTab(1); "SET triggerCount_out = 0;"

   genProcSectionHeader fileNo, "revalidate invalid TRIGGERs"
   Print #fileNo, addTab(1); "FOR trigLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "TRIGSCHEMA         AS c_triggerSchema,"
   Print #fileNo, addTab(3); "TRIGNAME           AS c_triggerName,"
   Print #fileNo, addTab(3); "LEFT(TEXT, 128000) AS c_triggerDefinition"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TRIGGERS"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(TRIGSCHEMA LIKE '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "VALID <> 'Y'"

   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(schemaNamePattern_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(TRIGSCHEMA LIKE schemaNamePattern_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(triggerNamePattern_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(TRIGNAME LIKE triggerNamePattern_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "FOR READ ONLY"
 
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader fileNo, "determine DROP-Statement", 2, True
   Print #fileNo, addTab(2); "SET v_triggerDropStmt = 'DROP TRIGGER ' || c_triggerSchema || '.' || c_triggerName;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO SESSION.statements(STATEMENT) VALUES(v_triggerDropStmt);"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.statements(STATEMENT) VALUES(LEFT(c_triggerDefinition,32000));"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_triggerDropStmt;"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE c_triggerDefinition;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET triggerCount_out = triggerCount_out + 1;"
 
   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit fileNo, qualProcNameRevalidateTriggers, ddlType, , "'schemaNamePattern_in", "'triggerNamePattern_in", "mode_in", "triggerCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Procedure for revalidating packages
   ' ####################################################################################################################

   Dim qualProcNameReValidatePackages As String
   qualProcNameReValidatePackages = genQualProcName(g_sectionIndexDbAdmin, spnRevalidate, ddlType, , , , "PACKAGES")

   printSectionHeader "Procedure for revalidating database packages", fileNo
   Print #fileNo,
 
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameReValidatePackages
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas of procedured to revalidate packages for"
   genProcParm fileNo, "IN", "procNamePattern_in", g_dbtDbProcName, True, "determines the procedured to revalidate packages for"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "procCount_out", "INTEGER", False, "number of procedures revalidated"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "inUse", "55006"
   genCondDecl fileNo, "rebindFail", "38000"
   genCondDecl fileNo, "alreadyExist", "42710"
 
   genSpLogDecl fileNo, , True
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR inUse"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore - this is a package corresponding to this procedure"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR rebindFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore - this is a package corresponding to this procedure"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genDdlForTempStatement fileNo, 1, , 32000, True

   genSpLogProcEnter fileNo, qualProcNameReValidatePackages, ddlType, , "'schemaNamePattern_in", "'procNamePattern_in", "mode_in", "procCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter 'procCount_out'"
   Print #fileNo, addTab(1); "SET procCount_out = 0;"

   genProcSectionHeader fileNo, "revalidate invalid packages corresponding to procedures"
   Print #fileNo, addTab(1); "FOR procLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "RT.ROUTINESCHEMA AS PROCSCHEMA,"
   Print #fileNo, addTab(3); "RT.SPECIFICNAME,"
   Print #fileNo, addTab(3); "PA.PKGSCHEMA,"
   Print #fileNo, addTab(3); "PA.PKGNAME"
   Print #fileNo, addTab(2); "FROM"

   Print #fileNo, addTab(3); "SYSCAT.ROUTINES RT"

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.ROUTINEDEP RD"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "RD.ROUTINESCHEMA = RT.ROUTINESCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RD.ROUTINENAME = RT.SPECIFICNAME"

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.PACKAGES PA"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "RT.ROUTINESCHEMA = PA.PKGSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RD.BNAME = PA.PKGNAME"

   Print #fileNo, addTab(2); "WHERE"
 
   Print #fileNo, addTab(3); "RT.ROUTINETYPE = 'P'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "schemaNamePattern_in IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "RTRIM(RT.ROUTINESCHEMA) LIKE COALESCE(UCASE(schemaNamePattern_in),'"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(3); ")"

   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "procNamePattern_in IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "RTRIM(RT.ROUTINENAME) LIKE procNamePattern_in"
   Print #fileNo, addTab(3); ")"

   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PA.VALID <> 'Y'"
   Print #fileNo, addTab(2); "FOR READ ONLY"
 
   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO SESSION.statements(STATEMENT) VALUES('REBIND PACKAGE ' || RTRIM(PKGSCHEMA) || '.' || RTRIM(PKGNAME) || ' ANY');"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "CALL SYSPROC.REBIND_ROUTINE_PACKAGE('SP', RTRIM(PROCSCHEMA) || '.' || RTRIM(SPECIFICNAME), 'ANY');"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET procCount_out = procCount_out + 1;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit fileNo, qualProcNameReValidatePackages, ddlType, , "'schemaNamePattern_in", "'procNamePattern_in", "mode_in", "procCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Procedure for revalidating routines
   ' ####################################################################################################################

   Dim qualProcNameRevalidateRoutines As String
   qualProcNameRevalidateRoutines = genQualProcName(g_sectionIndexDbAdmin, spnRevalidate, ddlType, , , , "ROUTINES")

   printSectionHeader "Procedure for revalidating database routines", fileNo
   Print #fileNo,
 
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRevalidateRoutines
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas of routines to revalidate"
   genProcParm fileNo, "IN", "routineNamePattern_in", "VARCHAR(50)", True, "determines the routines to revalidate"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "routineCount_out", "INTEGER", False, "number of routines revalidated"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "alreadyExist", "42710"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_parmSignature", "VARCHAR(500)", "''"
   genVarDecl fileNo, "v_routineDropStmt", "VARCHAR(300)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, , 32000, True
   genDdlForTempGrants fileNo, 1, True, , , True

   genSpLogProcEnter fileNo, qualProcNameRevalidateRoutines, ddlType, , "'schemaNamePattern_in", "'routineNamePattern_in", "mode_in", "routineCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter 'routineCount_out'"
   Print #fileNo, addTab(1); "SET routineCount_out = 0;"

   genProcSectionHeader fileNo, "revalidate invalid ROUTINEs"
   Print #fileNo, addTab(1); "FOR routineLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "ROUTINESCHEMA      AS c_routineSchema,"
   Print #fileNo, addTab(3); "ROUTINENAME        AS c_routineName,"
   Print #fileNo, addTab(3); "SPECIFICNAME       AS c_specificName,"
   Print #fileNo, addTab(3); "ROUTINETYPE        AS c_routineType,"
   Print #fileNo, addTab(3); "LEFT(TEXT, 128000) AS c_routineDefinition"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.ROUTINES"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(ROUTINESCHEMA LIKE '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(VALID <> 'Y')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(ROUTINETYPE IN ('F', 'P'))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(LANGUAGE = 'SQL')"

   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(schemaNamePattern_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(ROUTINESCHEMA LIKE schemaNamePattern_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "(routineNamePattern_in IS NULL)"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "(ROUTINENAME LIKE routineNamePattern_in)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); "FOR READ ONLY"
 
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader fileNo, "determine DROP-Statement", 2, True
   Print #fileNo, addTab(2); "SET v_routineDropStmt = 'DROP SPECIFIC ' || (CASE c_routineType WHEN 'F' THEN 'FUNCTION ' ELSE 'PROCEDURE ' END) || c_routineSchema || '.' || c_specificName;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 3, True
   Print #fileNo, addTab(3); "INSERT INTO SESSION.statements(STATEMENT) VALUES(v_routineDropStmt);"
   Print #fileNo, addTab(3); "INSERT INTO SESSION.statements(STATEMENT) VALUES(LEFT(c_routineDefinition, 32000));"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   genProcSectionHeader fileNo, "determine parameter signature of routine", 3, True
   Print #fileNo, addTab(3); "SET v_parmSignature = '';"
   Print #fileNo, addTab(3); "FOR parmLoop AS"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "TYPENAME || (CASE WHEN TYPENAME IN ('CHARACTER', 'VARCHAR') THEN '(' || RTRIM(CHAR(LENGTH)) || ')' ELSE '' END) AS v_type"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SYSCAT.ROUTINEPARMS"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "ROUTINESCHEMA = c_routineSchema"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "SPECIFICNAME = c_specificName"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "ROWTYPE NOT IN ('R', 'C')"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "ORDINAL"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); "DO"

   Print #fileNo, addTab(4); "SET v_parmSignature = v_parmSignature || (CASE WHEN v_parmSignature = '' THEN '' ELSE ',' END) || v_type;"

   Print #fileNo, addTab(3); "END FOR;"

   genProcSectionHeader fileNo, "keep track of GRANTs on Procedure", 3
 
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); tempTabNameGrant
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "schemaName,"
   Print #fileNo, addTab(4); "objectName,"
   Print #fileNo, addTab(4); "parmSignature,"
   Print #fileNo, addTab(4); "grantee,"
   Print #fileNo, addTab(4); "granteeType,"
   Print #fileNo, addTab(4); "privilege"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "c_routineSchema,"
   Print #fileNo, addTab(4); "c_routineName,"
   Print #fileNo, addTab(4); "v_parmSignature,"
   Print #fileNo, addTab(4); "A.GRANTEE,"
   Print #fileNo, addTab(4); "A.GRANTEETYPE,"
   Print #fileNo, addTab(4); "'EXECUTE'"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.ROUTINEAUTH A"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "A.SCHEMA = c_routineSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.SPECIFICNAME = c_specificName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.EXECUTEAUTH <> 'N'"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "re-create Routine", 3
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_routineDropStmt;"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE c_routineDefinition;"

   genProcSectionHeader fileNo, "set GRANTs on re-created Routine", 3
   Print #fileNo, addTab(3); "FOR privLoop AS"
   Print #fileNo, addTab(4); "SELECT"

   Print #fileNo, addTab(5); "'GRANT ' || G.privilege || ' ON ' || (CASE c_routineType WHEN 'F' THEN 'FUNCTION' ELSE 'PROCEDURE' END) || ' ' ||"
   Print #fileNo, addTab(5); "RTRIM(G.schemaName) || '.' || RTRIM(G.objectName) || '(' || G.parmSignature || ') TO ' ||"
   Print #fileNo, addTab(5); "(CASE G.granteeType WHEN 'P' THEN 'PUBLIC' WHEN 'G' THEN 'GROUP' ELSE 'USER' END) ||"
   Print #fileNo, addTab(5); "(CASE WHEN G.granteeType <> 'P' THEN ' ' || G.grantee ELSE '' END) AS grantStatement"

   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameGrant; " G"

   Print #fileNo, addTab(3); "DO"
   Print #fileNo, addTab(4); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 5, True
   Print #fileNo, addTab(5); "INSERT INTO SESSION.statements(STATEMENT) VALUES(grantStatement);"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE grantStatement;"

   Print #fileNo, addTab(3); "END FOR;"

   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "DELETE FROM "; tempTabNameGrant; ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET routineCount_out = routineCount_out + 1;"
 
   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit fileNo, qualProcNameRevalidateRoutines, ddlType, , "'schemaNamePattern_in", "'routineNamePattern_in", "mode_in", "routineCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Procedure for revalidating database objects
   ' ####################################################################################################################

   Dim qualProcNameRevalidate As String
   qualProcNameRevalidate = genQualProcName(g_sectionIndexDbAdmin, spnRevalidate, ddlType)

   printSectionHeader "Procedure for revalidating database objects", fileNo
   Print #fileNo,
 
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRevalidate
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas of objects to revalidate"
   genProcParm fileNo, "IN", "objNamePattern_in", "VARCHAR(50)", True, "determines the object names to revalidate"
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"

   genProcParm fileNo, "OUT", "objCount_out", "INTEGER", False, "number of database objects revalidated"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "alreadyExist", "42710"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_objCount", "INTEGER", "0"
   genVarDecl fileNo, "v_failCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, True, 32000, True

   genSpLogProcEnter fileNo, qualProcNameRevalidate, ddlType, , "'schemaNamePattern_in", "'objNamePattern_in", "mode_in", "objCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter 'objCount_out'"
   Print #fileNo, addTab(1); "SET objCount_out = 0;"
 
   genProcSectionHeader fileNo, "revalidate VIEWs"
   Print #fileNo, addTab(1); "CALL "; qualProcNameRevalidateViews; "(schemaNamePattern_in, objNamePattern_in, mode_in, v_objCount);"
   Print #fileNo, addTab(1); "SET objCount_out = objCount_out + v_objCount;"
 
   genProcSectionHeader fileNo, "revalidate TRIGGERs"
   Print #fileNo, addTab(1); "CALL "; qualProcNameRevalidateTriggers; "(schemaNamePattern_in, objNamePattern_in, mode_in, v_objCount);"
   Print #fileNo, addTab(1); "SET objCount_out = objCount_out + v_objCount;"
 
   genProcSectionHeader fileNo, "revalidate ROUTINEs"
   Print #fileNo, addTab(1); "CALL "; qualProcNameRevalidateRoutines; "(schemaNamePattern_in, objNamePattern_in, mode_in, v_objCount);"
   Print #fileNo, addTab(1); "SET objCount_out = objCount_out + v_objCount;"

   genProcSectionHeader fileNo, "revalidate PACKAGEs"
   Print #fileNo, addTab(1); "CALL "; qualProcNameReValidatePackages; "(schemaNamePattern_in, objNamePattern_in, mode_in, v_objCount);"
   Print #fileNo, addTab(1); "SET objCount_out = objCount_out + v_objCount;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcNameRevalidate, ddlType, , "'schemaNamePattern_in", "'objNamePattern_in", "mode_in", "objCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "Procedure for revalidating database objects", fileNo
   Print #fileNo,
 
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRevalidate
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"

   genProcParm fileNo, "OUT", "objCount_out", "INTEGER", False, "number of database objects revalidated"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, qualProcNameRevalidate, ddlType, , "mode_in", "objCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "CALL "; qualProcNameRevalidate; "(NULL, NULL, mode_in, objCount_out);"

   genSpLogProcExit fileNo, qualProcNameRevalidate, ddlType, , "mode_in", "objCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for rebinding Procedures
   ' ####################################################################################################################

   Dim qualProcedureNameRebindProcs As String
   qualProcedureNameRebindProcs = genQualProcName(g_sectionIndexDbAdmin, spnRebind, ddlType)

   printSectionHeader "SP for rebinding Procedures", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRebindProcs
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas of procedures to rebind (default '" & g_allSchemaNamePattern & "')"
   genProcParm fileNo, "IN", "procNamePattern_in", g_dbtDbProcName, True, "determines the procedures to rebind (default '%')"
   genProcParm fileNo, "IN", "schemaNamePatternExcl_in", g_dbtDbSchemaName, True, "(optional) determines the schemas of procedures to exclude from rebind"
   genProcParm fileNo, "IN", "procNamePatternExcl_in", g_dbtDbProcName, True, "(optional) determines the procedures to exclude from rebind"
   genProcParm fileNo, "IN", "force_in", g_dbtBoolean, True, "either '0' (only invalid procedures) or '1' (all procedures)"
   genProcParm fileNo, "OUT", "procCount_out", "INTEGER", True, "number of procedures successfully processed"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of procedures failed due to lock-timeout"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "inUse", "55006"
   genCondDecl fileNo, "rebindFail", "38000"
   genCondDecl fileNo, "implicitBindFail", "56098"
   genCondDecl fileNo, "lockTimeout", "40001"
   genCondDecl fileNo, "cursorAtEnd", "24501"
   genCondDecl fileNo, "objectNotExists", "42704"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_lastPkgSchema", g_dbtDbSchemaName, "NULL"
   genVarDecl fileNo, "v_lastPkgName", "VARCHAR(50)", "NULL"
   genVarDecl fileNo, "v_numRetriesToDo", "INTEGER", "NULL"
   genVarDecl fileNo, "v_doRetry", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_diagnostics", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_atEnd", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_retryLoop", "SMALLINT", "1"
   genVarDecl fileNo, "v_lostTempTable", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR inUse"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore - this is a package corresponding to this procedure"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR rebindFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore - this is a package corresponding to this procedure"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR implicitBindFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR lockTimeout"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_doRetry = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "IF v_numRetriesToDo = "; CStr(numRetriesRunstatsRebindOnLockTimeout); " THEN"
   Print #fileNo, addTab(3); "SET failCount_out = failCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR cursorAtEnd"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "IF SQLCODE = -501 THEN"
   Print #fileNo, addTab(3); "SET v_retryLoop = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_atEnd = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR objectNotExists"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_lostTempTable = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING;"
   Print #fileNo, addTab(2); "IF SQLCODE = -2310 AND v_diagnostics = '-911' THEN"
   Print #fileNo, addTab(3); "SET v_doRetry = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "IF v_numRetriesToDo = "; CStr(numRetriesRunstatsRebindOnLockTimeout); " THEN"
   Print #fileNo, addTab(4); "SET failCount_out = failCount_out + 1;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "ELSEIF SQLCODE = -2310 AND v_diagnostics = '-1477' THEN"
   Print #fileNo, addTab(3); "SET v_numRetriesToDo = 0;"
   Print #fileNo, addTab(3); "SET failCount_out    = failCount_out + 1;"
   Print #fileNo, addTab(2); "ELSE"
   genSpLogProcEscape fileNo, qualProcedureNameRebindProcs, ddlType, 3, "mode_in", "'schemaNamePattern_in", _
                             "'procNamePattern_in", "'schemaNamePatternExcl_in", "'procNamePatternExcl_in", _
                             "force_in", "procCount_out", "failCount_out"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"

   Dim qualTempTabNameRebind As String
   qualTempTabNameRebind = tempTabNameStatement & "Rebind"
   genDdlForTempStatement fileNo, 1, True, 250, , , , , "Rebind", , , True

   genSpLogProcEnter fileNo, qualProcedureNameRebindProcs, ddlType, , "mode_in", "'schemaNamePattern_in", _
                             "'procNamePattern_in", "'schemaNamePatternExcl_in", "'procNamePatternExcl_in", _
                             "force_in", "procCount_out", "failCount_out"
 
   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET procCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"

   genProcSectionHeader fileNo, "since calling REBIND this way may close cursors we apply some very cumbersome implementation pattern involving nested loops", 1, True
   Print #fileNo, addTab(1); "WHILE v_retryLoop = 1 DO"
   Print #fileNo, addTab(2); "SET v_retryLoop = "; gc_dbFalse; ";"

   Print #fileNo,
   Print #fileNo, addTab(2); "procLoop: FOR procLoop AS csr CURSOR WITH HOLD FOR"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "RTRIM(RT.ROUTINESCHEMA) AS PKGSCHEMA,"
   Print #fileNo, addTab(4); "RTRIM(PA.PKGNAME)       AS PKGNAME,"
   Print #fileNo, addTab(4); "RTRIM(RT.ROUTINESCHEMA) AS PROCSCHEMA,"
   Print #fileNo, addTab(4); "RT.ROUTINENAME          AS PROCNAME,"
   Print #fileNo, addTab(4); "RT.SPECIFICNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.ROUTINES RT"

   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.ROUTINEDEP RD"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "RD.ROUTINESCHEMA = RT.ROUTINESCHEMA"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "RD.ROUTINENAME = RT.SPECIFICNAME"

   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.PACKAGES PA"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "RT.ROUTINESCHEMA = PA.PKGSCHEMA"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "RD.BNAME = PA.PKGNAME"

   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "RT.ROUTINETYPE = 'P'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "RT.ROUTINESCHEMA NOT LIKE COALESCE(schemaNamePatternExcl_in,'')"

   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "NOT (RT.ROUTINESCHEMA = '"; getSchemaName(qualProcedureNameRebindProcs); "' AND RT.ROUTINENAME LIKE '%"; getUnqualObjName(qualProcedureNameRebindProcs); "%')"

   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "RT.ROUTINENAME NOT LIKE COALESCE(procNamePatternExcl_in,'')"

   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "RT.ROUTINESCHEMA LIKE COALESCE(UCASE(schemaNamePattern_in),'"; g_allSchemaNamePattern; "')"

   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "RT.ROUTINENAME LIKE COALESCE(UCASE(procNamePattern_in),'%')"

   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "(v_lastPkgSchema is NULL)"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "(RT.ROUTINESCHEMA > v_lastPkgSchema)"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "(RT.ROUTINESCHEMA = v_lastPkgSchema AND RD.BNAME > v_lastPkgName)"
   Print #fileNo, addTab(4); ")"

   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "COALESCE(force_in,"; gc_dbTrue; ") = "; gc_dbTrue
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "PA.VALID <> 'Y'"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); "FOR READ ONLY"

   Print #fileNo, addTab(2); "DO"
 
   genProcSectionHeader fileNo, "leave or retry loop if requested", 3, True
   Print #fileNo, addTab(3); "IF v_atEnd = 1 OR v_retryLoop = 1 THEN"
   Print #fileNo, addTab(4); "LEAVE procLoop;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_lastPkgSchema = PKGSCHEMA;"
   Print #fileNo, addTab(3); "SET v_lastPkgName   = PKGNAME;"
   Print #fileNo, addTab(3); "SET procCount_out   = procCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "SET v_numRetriesToDo = "; CStr(numRetriesRunstatsRebindOnLockTimeout); ";"
   Print #fileNo, addTab(4); "SET v_doRetry = "; gc_dbFalse; ";"
   Print #fileNo, addTab(4); "WHILE v_numRetriesToDo > 0 DO"
   Print #fileNo, addTab(5); "CALL SYSPROC.REBIND_ROUTINE_PACKAGE('SP', RTRIM(PROCSCHEMA) || '.' || RTRIM(SPECIFICNAME), 'ANY');"
   Print #fileNo, addTab(5); "SET v_numRetriesToDo = (CASE v_doRetry WHEN 0 THEN 0 ELSE v_numRetriesToDo - 1 END);"
   Print #fileNo, addTab(4); "END WHILE;"
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "END IF;"
 
   genProcSectionHeader fileNo, "store statement in temporary table", 3
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); qualTempTabNameRebind
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "seqNo,"
   Print #fileNo, addTab(4); "flag,"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "procCount_out,"
   Print #fileNo, addTab(4); "(CASE v_doRetry WHEN 1 THEN '-' ELSE '+' END),"
   Print #fileNo, addTab(4); "'REBIND PACKAGE ' || PKGSCHEMA || '.' || PKGNAME"
   Print #fileNo, addTab(3); ");"
 
   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END WHILE;"

   genProcSectionHeader fileNo, "determine number of successful REBINDs"
   Print #fileNo, addTab(1); "SET procCount_out = procCount_out - failCount_out;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "IF v_lostTempTable = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "BEGIN"
   Print #fileNo, addTab(4); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(5); "SELECT"
   Print #fileNo, addTab(6); "flag AS f,"
   Print #fileNo, addTab(6); "statement"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); qualTempTabNameRebind
   Print #fileNo, addTab(5); "ORDER BY"
   Print #fileNo, addTab(6); "SEQNO ASC"
   Print #fileNo, addTab(5); "FOR READ ONLY"
   Print #fileNo, addTab(4); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 4
   Print #fileNo, addTab(4); "OPEN stmntCursor;"

   Print #fileNo, addTab(3); "END;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "BEGIN"
   Print #fileNo, addTab(4); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(5); "SELECT"
   Print #fileNo, addTab(6); "CAST('-' AS CHAR(1)) f,"
   Print #fileNo, addTab(6); "CAST('-- lost reference to table """; qualTempTabNameRebind; """' AS VARCHAR(150))"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); "SYSIBM.SYSDUMMY1"
   Print #fileNo, addTab(4); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 4
   Print #fileNo, addTab(4); "OPEN stmntCursor;"
   Print #fileNo, addTab(3); "END;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameRebindProcs, ddlType, , "mode_in", "'schemaNamePattern_in", _
                             "'procNamePattern_in", "'schemaNamePatternExcl_in", "'procNamePatternExcl_in", _
                             "force_in", "procCount_out", "failCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for rebinding Procedures", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRebindProcs
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas of procedures to rebind"
   genProcParm fileNo, "IN", "procNamePattern_in", g_dbtDbProcName, True, "determines the procedures to rebind"
   genProcParm fileNo, "IN", "force_in", g_dbtBoolean, True, "either '0' (only invalid procedures) or '1' (all procedures)"
   genProcParm fileNo, "OUT", "procCount_out", "INTEGER", False, "number of procedures successfully processed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameRebindProcs, ddlType, , "mode_in", "'schemaNamePattern_in", _
                             "'procNamePattern_in", "force_in", "procCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameRebindProcs; "(mode_in, schemaNamePattern_in, procNamePattern_in, NULL, NULL, force_in, procCount_out, v_failCount);"

   genSpLogProcExit fileNo, qualProcedureNameRebindProcs, ddlType, , "mode_in", "'schemaNamePattern_in", _
                             "'procNamePattern_in", "force_in", "procCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for rebinding Procedures", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRebindProcs
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "force_in", g_dbtBoolean, True, "either '0' (only invalid procedures) or '1' (all procedures)"
   genProcParm fileNo, "OUT", "procCount_out", "INTEGER", False, "number of procedures successfully processed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameRebindProcs, ddlType, , "mode_in", "force_in", "procCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameRebindProcs; "(mode_in, NULL, NULL, NULL, NULL, force_in, procCount_out, v_failCount);"

   genSpLogProcExit fileNo, qualProcedureNameRebindProcs, ddlType, , "mode_in", "force_in", "procCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Private Sub genDbAdminDdl6( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   ' ####################################################################################################################

   Dim qualProcedureNameReorg As String
   qualProcedureNameReorg = genQualProcName(g_sectionIndexDbAdmin, spnReorg, ddlType)

   printSectionHeader "SP for REORG of tables / indexes", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameReorg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "objType_in", "CHAR(1)", True, "(optional) determines the type of objects to REORG ('I' = index, 'T' = table)"
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schema of tables to REORG"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the name of tables to REORG"
   genProcParm fileNo, "IN", "tabSchemaPatternExcl_in", g_dbtDbSchemaName, True, "(optional) determines the schemas of tables to exclude from REORG"
   genProcParm fileNo, "IN", "tabNamePatternExcl_in", g_dbtDbTableName, True, "(optional) determines the tables to exclude from REORG"
   genProcParm fileNo, "IN", "doRunStats_in", g_dbtBoolean, True, "if set to '1': execute RUNSTATS on reorganized tables"
   genProcParm fileNo, "IN", "force_in", g_dbtBoolean, True, "if set to '1': REORG all objects, otherwise only those recommended"
   genProcParm fileNo, "IN", "reorgLobData_in", g_dbtBoolean, True, "if set to '1': REORG LOB data"

   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of REORGS on tables"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of procedures failed due to lock-timeout"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "lockTimeout", "40001"
   genCondDecl fileNo, "reorgError", "01H52"
   genCondDecl fileNo, "rsEmpty", "0F001"
 
   genProcSectionHeader fileNo, "declare variables"
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_execTime", "TIME", "NULL"
   genVarDecl fileNo, "v_numRetriesToDo", "INTEGER", CStr(numRetriesRunstatsRebindOnLockTimeout)
   genVarDecl fileNo, "v_doRetry", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_rsEmpty", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_doIgnore", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_diagnostics", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_tempTbSpaceName", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genVarDecl fileNo, "v_rc", "INTEGER", "0"
   genVarDecl fileNo, "v_TABLE_SCHEMA", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_TABLE_NAME", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_INDEX_SCHEMA", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_INDEX_NAME", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_CARD", "BIGINT", "NULL"
   genVarDecl fileNo, "v_OVERFLOW", "BIGINT", "NULL"
   genVarDecl fileNo, "v_NLEAF", "BIGINT", "NULL"
   genVarDecl fileNo, "v_NUM_EMPTY_LEAFS", "BIGINT", "NULL"
   genVarDecl fileNo, "v_NLEVELS ", "INTEGER", "NULL"
   genVarDecl fileNo, "v_ISIZE  ", "BIGINT", "NULL"
   genVarDecl fileNo, "v_NUMRIDS_DELETED  ", "BIGINT", "NULL"
   ' DB2 V9-specific
   genVarDecl fileNo, "v_DATAPARTITIONNAME", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_LEAF_RECSIZE ", "BIGINT", "NULL"
   genVarDecl fileNo, "v_NONLEAF_RECSIZE ", "BIGINT", "NULL"
   genVarDecl fileNo, "v_LEAF_PAGE_OVERHEAD ", "BIGINT", "NULL"
   genVarDecl fileNo, "v_NONLEAF_PAGE_OVERHEAD ", "BIGINT", "NULL"
   ' end DB2 V9-specific
   genVarDecl fileNo, "v_FULLKEYCARD  ", "BIGINT", "NULL"
   genVarDecl fileNo, "v_NPAGES", "BIGINT", "NULL"
   genVarDecl fileNo, "v_FPAGES", "BIGINT", "NULL"
   genVarDecl fileNo, "v_ACTIVE_BLOCKS", "BIGINT", "NULL"
   genVarDecl fileNo, "v_TSIZE", "BIGINT", "NULL"
   genVarDecl fileNo, "v_F1", "INTEGER", "NULL"
   genVarDecl fileNo, "v_F2", "INTEGER", "NULL"
   genVarDecl fileNo, "v_F3", "INTEGER", "NULL"
   genVarDecl fileNo, "v_F4", "INTEGER", "NULL"
   genVarDecl fileNo, "v_F5", "INTEGER", "NULL"
   genVarDecl fileNo, "v_F6", "INTEGER", "NULL"
   genVarDecl fileNo, "v_F7", "INTEGER", "NULL"
   genVarDecl fileNo, "v_F8", "INTEGER", "NULL"
   genVarDecl fileNo, "v_REORG_TAB", "CHAR(3)", "NULL"
   genVarDecl fileNo, "v_REORG_IND", "CHAR(5)", "NULL"
   genVarDecl fileNo, "v_stmtBegin", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_stmtEnd", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_db2Release", g_dbtDbRelease, "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare result set locator for reorgCheck-data"
   genVarDecl fileNo, "l_reorgChkResult", "RESULT_SET_LOCATOR VARYING"

   genProcSectionHeader fileNo, "declare continue handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR lockTimeout"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_doRetry = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR reorgError"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "IF SQLCODE = -1146 THEN"
   Print #fileNo, addTab(3); "SET v_doIgnore = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_doRetry = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR rsEmpty"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_rsEmpty = "; gc_dbTrue; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING;"
   Print #fileNo, addTab(2); "IF SQLCODE = -2310 AND v_diagnostics = '-911' THEN"
   Print #fileNo, addTab(3); "SET v_doRetry = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "ELSEIF SQLCODE = -2310 AND v_diagnostics = '-1477' THEN"
   Print #fileNo, addTab(3); "SET v_doRetry = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "ELSE"
   genSpLogProcEscape fileNo, qualProcedureNameReorg, ddlType, 3, "mode_in", "'objType_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
     "'tabSchemaPatternExcl_in", "'tabNamePatternExcl_in", "doRunStats_in", "force_in", "reorgLobData_in", "tabCount_out", "failCount_out"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"

   Dim qualTempTabNameReorg As String
   qualTempTabNameReorg = tempTabNameStatement & "Reorg"
   genDdlForTempStatement fileNo, , True, 400, False, True, True, False, "Reorg", True, True, False, , _
     "numRetries", "SMALLINT", "qualTabName", "VARCHAR(100)"

   genProcSectionHeader fileNo, "temporary table for objects to reorganize", 1
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.reorgChk"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objType         CHAR(1),"
   Print #fileNo, addTab(2); "tableSchemaName VARCHAR(128),"
   Print #fileNo, addTab(2); "tableName       VARCHAR(128),"
   Print #fileNo, addTab(2); "indexName       VARCHAR(128),"
   Print #fileNo, addTab(2); "tempTsName      VARCHAR(128)"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True
   Print #fileNo,
 
   genSpLogProcEnter fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "'objType_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
     "'tabSchemaPatternExcl_in", "'tabNamePatternExcl_in", "doRunStats_in", "force_in", "reorgLobData_in", "tabCount_out", "failCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET tabCount_out  = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"

   genProcSectionHeader fileNo, "determine database manager version"
   Print #fileNo, addTab(1); "SET v_db2Release = "; g_qualFuncNameDb2Release; "();"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF force_in = 1 THEN"
   genProcSectionHeader fileNo, "include all tables / indexes in REORG", 2, True

   Print #fileNo, addTab(2); "IF COALESCE(UPPER(objType_in), 'T') = 'T' THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); "SESSION.reorgChk"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "objType,"
   Print #fileNo, addTab(4); "tableSchemaName,"
   Print #fileNo, addTab(4); "tableName,"
   Print #fileNo, addTab(4); "tempTsName"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "WITH"
   Print #fileNo, addTab(4); "V"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "tableSchemaName,"
   Print #fileNo, addTab(4); "tableName,"
   Print #fileNo, addTab(4); "tempTsName,"
   Print #fileNo, addTab(4); "seqNo"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AS"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "T.TABSCHEMA,"
   Print #fileNo, addTab(5); "T.TABNAME,"
   Print #fileNo, addTab(5); "TEMPTS.TBSPACE,"
   Print #fileNo, addTab(5); "ROWNUMBER() OVER (PARTITION BY T.TABSCHEMA, T.TABNAME ORDER BY TEMPTS.CREATE_TIME DESC)"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SYSCAT.TABLES T"
   Print #fileNo, addTab(4); "LEFT OUTER JOIN"
   Print #fileNo, addTab(5); "SYSCAT.DATAPARTITIONS P"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "T.TABSCHEMA = P.TABSCHEMA"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "T.TABNAME = P.TABNAME"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P.DATAPARTITIONID = 0"
   Print #fileNo, addTab(4); "LEFT OUTER JOIN"
   Print #fileNo, addTab(5); "SYSCAT.TABLESPACES TS"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "P.TBSPACEID = TS.TBSPACEID"
   Print #fileNo, addTab(4); "LEFT OUTER JOIN"
   Print #fileNo, addTab(5); "SYSCAT.TABLESPACES TEMPTS"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "TEMPTS.PAGESIZE = TS.PAGESIZE"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "TEMPTS.TBSPACETYPE = 'S'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "TEMPTS.DATATYPE = 'T'"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "T.TYPE = 'T'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "UCASE(RTRIM(T.TABSCHEMA)) LIKE COALESCE(UCASE(tabSchemaPattern_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "UCASE(RTRIM(T.TABNAME)) LIKE COALESCE(UCASE(tabNamePattern_in), '%')"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "UCASE(RTRIM(T.TABSCHEMA)) NOT LIKE COALESCE(UCASE(tabSchemaPatternExcl_in), '')"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "UCASE(RTRIM(T.TABNAME)) NOT LIKE COALESCE(UCASE(tabNamePatternExcl_in), '')"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "NOT EXISTS ( SELECT 1 FROM SYSCAT.EVENTTABLES AS event WHERE T.TABSCHEMA = event.TABSCHEMA AND T.TABNAME = event.TABNAME )"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "'T',"
   Print #fileNo, addTab(4); "tableSchemaName,"
   Print #fileNo, addTab(4); "tableName,"
   Print #fileNo, addTab(4); "tempTsName"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "V"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "seqNo = 1"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF UPPER(objType_in) = 'I' THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); "SESSION.reorgChk"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "objType,"
   Print #fileNo, addTab(4); "tableSchemaName,"
   Print #fileNo, addTab(4); "tableName"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "'I',"
   Print #fileNo, addTab(4); "T.TABSCHEMA,"
   Print #fileNo, addTab(4); "T.TABNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABLES T"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "T.TYPE = 'T'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UCASE(RTRIM(T.TABSCHEMA)) LIKE COALESCE(UCASE(tabSchemaPattern_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UCASE(RTRIM(T.TABNAME)) LIKE COALESCE(UCASE(tabNamePattern_in), '%')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UCASE(RTRIM(T.TABSCHEMA)) NOT LIKE COALESCE(UCASE(tabSchemaPatternExcl_in), '')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UCASE(RTRIM(T.TABNAME)) NOT LIKE COALESCE(UCASE(tabNamePatternExcl_in), '')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "EXISTS("
   Print #fileNo, addTab(5); "SELECT"
   Print #fileNo, addTab(6); "1"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); "SYSCAT.INDEXES I"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "T.TABSCHEMA = I.TABSCHEMA"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "T.TABNAME = I.TABNAME"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "ELSE"

   genProcSectionHeader fileNo, "determine table reorgChk data", 2, True
   Print #fileNo, addTab(2); "IF COALESCE(UPPER(objType_in), 'T') = 'T' THEN"

   Print #fileNo, addTab(3); "CALL SYSPROC.REORGCHK_TB_STATS('T','ALL');"

   genProcSectionHeader fileNo, "associate result set locator", 3
   Print #fileNo, addTab(3); "ASSOCIATE RESULT SET LOCATORS(l_reorgChkResult) WITH PROCEDURE SYSPROC.REORGCHK_TB_STATS;"

   genProcSectionHeader fileNo, "allocate result set locator", 3
   Print #fileNo, addTab(3); "SET v_rsEmpty = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "ALLOCATE rsCursorTab CURSOR FOR RESULT SET l_reorgChkResult;"

   genProcSectionHeader fileNo, "retrieve result set records", 3
   Print #fileNo, addTab(3); "IF v_rsEmpty = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(4); "REPEAT"

   genProcSectionHeader fileNo, "fetch next record", 5, True
   Print #fileNo, addTab(5); "IF v_db2Release < 9.07 THEN"
   Print #fileNo, addTab(6); "FETCH"
   Print #fileNo, addTab(7); "rsCursorTab"
   Print #fileNo, addTab(6); "INTO"
   Print #fileNo, addTab(7); "v_TABLE_SCHEMA,"
   Print #fileNo, addTab(7); "v_TABLE_NAME,"
   Print #fileNo, addTab(7); "v_CARD,"
   Print #fileNo, addTab(7); "v_OVERFLOW,"
   Print #fileNo, addTab(7); "v_NPAGES,"
   Print #fileNo, addTab(7); "v_FPAGES,"
   Print #fileNo, addTab(7); "v_ACTIVE_BLOCKS,"
   Print #fileNo, addTab(7); "v_TSIZE,"
   Print #fileNo, addTab(7); "v_F1,"
   Print #fileNo, addTab(7); "v_F2,"
   Print #fileNo, addTab(7); "v_F3,"
   Print #fileNo, addTab(7); "v_REORG_TAB"
   Print #fileNo, addTab(6); ";"
   Print #fileNo,
   Print #fileNo, addTab(6); "SET v_rc = SQLCODE;"

   Print #fileNo, addTab(5); "ELSE"

   Print #fileNo, addTab(6); "FETCH"
   Print #fileNo, addTab(7); "rsCursorTab"
   Print #fileNo, addTab(6); "INTO"
   Print #fileNo, addTab(7); "v_TABLE_SCHEMA,"
   Print #fileNo, addTab(7); "v_TABLE_NAME,"
   Print #fileNo, addTab(7); "v_DATAPARTITIONNAME,"
   Print #fileNo, addTab(7); "v_CARD,"
   Print #fileNo, addTab(7); "v_OVERFLOW,"
   Print #fileNo, addTab(7); "v_NPAGES,"
   Print #fileNo, addTab(7); "v_FPAGES,"
   Print #fileNo, addTab(7); "v_ACTIVE_BLOCKS,"
   Print #fileNo, addTab(7); "v_TSIZE,"
   Print #fileNo, addTab(7); "v_F1,"
   Print #fileNo, addTab(7); "v_F2,"
   Print #fileNo, addTab(7); "v_F3,"
   Print #fileNo, addTab(7); "v_REORG_TAB"
   Print #fileNo, addTab(6); ";"
   Print #fileNo,
   Print #fileNo, addTab(6); "SET v_rc = SQLCODE;"
   Print #fileNo, addTab(5); "END IF;"

   genProcSectionHeader fileNo, "check whether record matches filter criteria and recommends REORG", 5
   Print #fileNo, addTab(5); "IF (UCASE(RTRIM(v_TABLE_SCHEMA))     LIKE COALESCE(UCASE(tabSchemaPattern_in), '"; g_allSchemaNamePattern; "')) AND"
   Print #fileNo, addTab(5); "   (UCASE(v_TABLE_NAME         )     LIKE COALESCE(UCASE(tabNamePattern_in),       '%')) AND"
   Print #fileNo, addTab(5); "   (UCASE(RTRIM(v_TABLE_SCHEMA)) NOT LIKE COALESCE(UCASE(tabSchemaPatternExcl_in), '' )) AND"
   Print #fileNo, addTab(5); "   (UCASE(v_TABLE_NAME         ) NOT LIKE COALESCE(UCASE(tabNamePatternExcl_in),   '' )) AND"
   Print #fileNo, addTab(5); "   ( (v_F1 >= 5) OR (v_F2 BETWEEN 0 AND 70) OR (v_F3 BETWEEN 0 AND 80) ) AND"
   Print #fileNo, addTab(5); "   ( v_NPAGES > 5 )"
   Print #fileNo, addTab(5); "THEN"
   genProcSectionHeader fileNo, "determine suitable temporary tablespace to use", 6, True

   Print #fileNo, addTab(6); "SET v_tempTbSpaceName = NULL;"
   Print #fileNo, addTab(6); "SELECT"
   Print #fileNo, addTab(7); "tempTsName"
   Print #fileNo, addTab(6); "INTO"
   Print #fileNo, addTab(7); "v_tempTbSpaceName"
   Print #fileNo, addTab(6); "FROM"
   Print #fileNo, addTab(7); "("
   Print #fileNo, addTab(8); "SELECT"
   Print #fileNo, addTab(9); "TEMPTS.TBSPACE AS tempTsName,"
   Print #fileNo, addTab(9); "ROWNUMBER() OVER (PARTITION BY TEMPTS.PAGESIZE ORDER BY TEMPTS.CREATE_TIME DESC) AS seqNo"
   Print #fileNo, addTab(8); "FROM"
   Print #fileNo, addTab(9); "SYSCAT.TABLES T"
   Print #fileNo, addTab(8); "LEFT OUTER JOIN"
   Print #fileNo, addTab(9); "SYSCAT.DATAPARTITIONS P"
   Print #fileNo, addTab(8); "ON"
   Print #fileNo, addTab(9); "T.TABSCHEMA = P.TABSCHEMA"
   Print #fileNo, addTab(10); "AND"
   Print #fileNo, addTab(9); "T.TABNAME = P.TABNAME"
   Print #fileNo, addTab(10); "AND"
   Print #fileNo, addTab(9); "P.DATAPARTITIONID = 0"
   Print #fileNo, addTab(8); "LEFT OUTER JOIN"
   Print #fileNo, addTab(9); "SYSCAT.TABLESPACES TS"
   Print #fileNo, addTab(8); "ON"
   Print #fileNo, addTab(9); "P.TBSPACEID = TS.TBSPACEID"
   Print #fileNo, addTab(8); "LEFT OUTER JOIN"
   Print #fileNo, addTab(9); "SYSCAT.TABLESPACES TEMPTS"
   Print #fileNo, addTab(8); "ON"
   Print #fileNo, addTab(9); "TEMPTS.PAGESIZE = TS.PAGESIZE"
   Print #fileNo, addTab(10); "AND"
   Print #fileNo, addTab(9); "TEMPTS.TBSPACETYPE = 'S'"
   Print #fileNo, addTab(10); "AND"
   Print #fileNo, addTab(9); "TEMPTS.DATATYPE = 'T'"
   Print #fileNo, addTab(8); "WHERE"
   Print #fileNo, addTab(9); "T.TYPE = 'T'"
   Print #fileNo, addTab(10); "AND"
   Print #fileNo, addTab(9); "T.TABSCHEMA = v_TABLE_SCHEMA"
   Print #fileNo, addTab(10); "AND"
   Print #fileNo, addTab(9); "T.TABNAME = v_TABLE_NAME"
   Print #fileNo, addTab(7); ") V"
   Print #fileNo, addTab(6); "WHERE"
   Print #fileNo, addTab(7); "seqNo = 1"
   Print #fileNo, addTab(6); ";"
   Print #fileNo,

   Print #fileNo, addTab(6); "INSERT INTO"
   Print #fileNo, addTab(7); "SESSION.reorgChk"
   Print #fileNo, addTab(6); "("
   Print #fileNo, addTab(7); "objType,"
   Print #fileNo, addTab(7); "tableSchemaName,"
   Print #fileNo, addTab(7); "tableName,"
   Print #fileNo, addTab(7); "tempTsName"
   Print #fileNo, addTab(6); ")"
   Print #fileNo, addTab(6); "VALUES"
   Print #fileNo, addTab(6); "("
   Print #fileNo, addTab(7); "'T',"
   Print #fileNo, addTab(7); "v_TABLE_SCHEMA,"
   Print #fileNo, addTab(7); "v_TABLE_NAME,"
   Print #fileNo, addTab(7); "v_tempTbSpaceName"
   Print #fileNo, addTab(6); ");"

   Print #fileNo, addTab(5); "END IF;"

   Print #fileNo, addTab(4); "UNTIL"
   Print #fileNo, addTab(5); "v_rc <> 0"
   Print #fileNo, addTab(4); "END REPEAT;"
   Print #fileNo,
   Print #fileNo, addTab(4); "CLOSE rsCursorTab;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "determine index reorgChk data", 2
   Print #fileNo, addTab(2); "IF UPPER(objType_in) = 'I' THEN"

   Print #fileNo, addTab(3); "CALL SYSPROC.REORGCHK_IX_STATS('T','ALL');"

   genProcSectionHeader fileNo, "associate result set locator", 3
   Print #fileNo, addTab(3); "ASSOCIATE RESULT SET LOCATORS(l_reorgChkResult) WITH PROCEDURE SYSPROC.REORGCHK_IX_STATS;"

   genProcSectionHeader fileNo, "allocate result set locator", 3
   Print #fileNo, addTab(3); "SET v_rsEmpty = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "ALLOCATE rsCursorInd CURSOR FOR RESULT SET l_reorgChkResult;"

   genProcSectionHeader fileNo, "retrieve result set records", 3
   Print #fileNo, addTab(3); "IF v_rsEmpty = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(4); "REPEAT"

   genProcSectionHeader fileNo, "fetch next record", 5, True
   Print #fileNo, addTab(5); "IF INTEGER(v_db2Release) = 8 THEN"

   Print #fileNo, addTab(6); "FETCH"
   Print #fileNo, addTab(7); "rsCursorInd"
   Print #fileNo, addTab(6); "INTO"
   Print #fileNo, addTab(7); "v_TABLE_SCHEMA,"
   Print #fileNo, addTab(7); "v_TABLE_NAME,"
   Print #fileNo, addTab(7); "v_INDEX_SCHEMA,"
   Print #fileNo, addTab(7); "v_INDEX_NAME,"
   Print #fileNo, addTab(7); "v_CARD,"
   Print #fileNo, addTab(7); "v_NLEAF,"
   Print #fileNo, addTab(7); "v_NUM_EMPTY_LEAFS,"
   Print #fileNo, addTab(7); "v_NLEVELS,"
   Print #fileNo, addTab(7); "v_ISIZE,"
   Print #fileNo, addTab(7); "v_NUMRIDS_DELETED,"
   Print #fileNo, addTab(7); "v_FULLKEYCARD,"
   Print #fileNo, addTab(7); "v_F4,"
   Print #fileNo, addTab(7); "v_F5,"
   Print #fileNo, addTab(7); "v_F6,"
   Print #fileNo, addTab(7); "v_F7,"
   Print #fileNo, addTab(7); "v_F8,"
   Print #fileNo, addTab(7); "v_REORG_IND"
   Print #fileNo, addTab(6); ";"
   Print #fileNo,
   Print #fileNo, addTab(6); "SET v_rc = SQLCODE;"

   Print #fileNo, addTab(5); "ELSEIF INTEGER(v_db2Release) = 9 THEN"
   Print #fileNo, addTab(6); "IF v_db2Release < 9.07 THEN"

   Print #fileNo, addTab(7); "FETCH"
   Print #fileNo, addTab(8); "rsCursorInd"
   Print #fileNo, addTab(7); "INTO"
   Print #fileNo, addTab(8); "v_TABLE_SCHEMA,"
   Print #fileNo, addTab(8); "v_TABLE_NAME,"
   Print #fileNo, addTab(8); "v_INDEX_SCHEMA,"
   Print #fileNo, addTab(8); "v_INDEX_NAME,"
   Print #fileNo, addTab(8); "v_CARD,"
   Print #fileNo, addTab(8); "v_NLEAF,"
   Print #fileNo, addTab(8); "v_NUM_EMPTY_LEAFS,"
   Print #fileNo, addTab(8); "v_NLEVELS,"
   Print #fileNo, addTab(8); "v_NUMRIDS_DELETED,"
   Print #fileNo, addTab(8); "v_FULLKEYCARD,"
   Print #fileNo, addTab(8); "v_LEAF_RECSIZE,"
   Print #fileNo, addTab(8); "v_NONLEAF_RECSIZE,"
   Print #fileNo, addTab(8); "v_LEAF_PAGE_OVERHEAD,"
   Print #fileNo, addTab(8); "v_NONLEAF_PAGE_OVERHEAD,"
   Print #fileNo, addTab(8); "v_F4,"
   Print #fileNo, addTab(8); "v_F5,"
   Print #fileNo, addTab(8); "v_F6,"
   Print #fileNo, addTab(8); "v_F7,"
   Print #fileNo, addTab(8); "v_F8,"
   Print #fileNo, addTab(8); "v_REORG_IND"
   Print #fileNo, addTab(7); ";"
   Print #fileNo,
   Print #fileNo, addTab(7); "SET v_rc = SQLCODE;"

   Print #fileNo, addTab(6); "ELSE"

   Print #fileNo, addTab(7); "FETCH"
   Print #fileNo, addTab(8); "rsCursorInd"
   Print #fileNo, addTab(7); "INTO"
   Print #fileNo, addTab(8); "v_TABLE_SCHEMA,"
   Print #fileNo, addTab(8); "v_TABLE_NAME,"
   Print #fileNo, addTab(7); "v_DATAPARTITIONNAME,"
   Print #fileNo, addTab(8); "v_INDEX_SCHEMA,"
   Print #fileNo, addTab(8); "v_INDEX_NAME,"
   Print #fileNo, addTab(8); "v_CARD,"
   Print #fileNo, addTab(8); "v_NLEAF,"
   Print #fileNo, addTab(8); "v_NUM_EMPTY_LEAFS,"
   Print #fileNo, addTab(8); "v_NLEVELS,"
   Print #fileNo, addTab(8); "v_NUMRIDS_DELETED,"
   Print #fileNo, addTab(8); "v_FULLKEYCARD,"
   Print #fileNo, addTab(8); "v_LEAF_RECSIZE,"
   Print #fileNo, addTab(8); "v_NONLEAF_RECSIZE,"
   Print #fileNo, addTab(8); "v_LEAF_PAGE_OVERHEAD,"
   Print #fileNo, addTab(8); "v_NONLEAF_PAGE_OVERHEAD,"
   Print #fileNo, addTab(8); "v_F4,"
   Print #fileNo, addTab(8); "v_F5,"
   Print #fileNo, addTab(8); "v_F6,"
   Print #fileNo, addTab(8); "v_F7,"
   Print #fileNo, addTab(8); "v_F8,"
   Print #fileNo, addTab(8); "v_REORG_IND"
   Print #fileNo, addTab(7); ";"
   Print #fileNo,
   Print #fileNo, addTab(7); "SET v_rc = SQLCODE;"

   Print #fileNo, addTab(6); "END IF;"

   Print #fileNo, addTab(5); "ELSE"
   genSpLogProcEscape fileNo, qualProcedureNameReorg, ddlType, 6, "mode_in", "'objType_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
     "'tabSchemaPatternExcl_in", "'tabNamePatternExcl_in", "doRunStats_in", "force_in", "reorgLobData_in", "tabCount_out", "failCount_out"
   genSignalDdlWithParms "dbVersNotSupported", fileNo, 6, , , , , , , , , , "RTRIM(CHAR(v_db2Release))"
   Print #fileNo, addTab(5); "END IF;"

   genProcSectionHeader fileNo, "check whether record matches filter criteria and recommends REORG", 6
   Print #fileNo, addTab(5); "IF (UCASE(RTRIM(v_TABLE_SCHEMA))     LIKE COALESCE(UCASE(tabSchemaPattern_in),     '"; g_allSchemaNamePattern; "')) AND"
   Print #fileNo, addTab(5); "   (UCASE(v_TABLE_NAME         )     LIKE COALESCE(UCASE(tabNamePattern_in),       '%')) AND"
   Print #fileNo, addTab(5); "   (UCASE(RTRIM(v_TABLE_SCHEMA)) NOT LIKE COALESCE(UCASE(tabSchemaPatternExcl_in), '' )) AND"
   Print #fileNo, addTab(5); "   (UCASE(v_TABLE_NAME         ) NOT LIKE COALESCE(UCASE(tabNamePatternExcl_in),   '' )) AND"
   Print #fileNo, addTab(5); "   ( (v_F5 BETWEEN 0 AND 50) OR (v_F7 >= 20) OR (v_F8 >= 20) )"
   Print #fileNo, addTab(5); "THEN"

   Print #fileNo, addTab(6); "INSERT INTO"
   Print #fileNo, addTab(7); "SESSION.reorgChk"
   Print #fileNo, addTab(6); "("
   Print #fileNo, addTab(7); "objType,"
   Print #fileNo, addTab(7); "tableSchemaName,"
   Print #fileNo, addTab(7); "tableName,"
   Print #fileNo, addTab(7); "indexName"
   Print #fileNo, addTab(6); ")"
   Print #fileNo, addTab(6); "VALUES"
   Print #fileNo, addTab(6); "("
   Print #fileNo, addTab(7); "'I',"
   Print #fileNo, addTab(7); "v_TABLE_SCHEMA,"
   Print #fileNo, addTab(7); "v_TABLE_NAME,"
   Print #fileNo, addTab(7); "v_INDEX_NAME"
   Print #fileNo, addTab(6); ");"

   Print #fileNo, addTab(5); "END IF;"

   Print #fileNo, addTab(4); "UNTIL"
   Print #fileNo, addTab(5); "v_rc <> 0"
   Print #fileNo, addTab(4); "END REPEAT;"

   Print #fileNo,
   Print #fileNo, addTab(4); "CLOSE rsCursorInd;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "ignore index-REORGs for tables not having indexes", 1
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); "SESSION.reorgChk RC"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "RC.objType = 'I'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NOT EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABLES T"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "T.TABSCHEMA = RC.tableSchemaName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "T.TABNAME = RC.tableName"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "determine REORG- and RUNSTATS-statements", 1
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTempTabNameReorg
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "seqNo,"
   Print #fileNo, addTab(2); "numRetries,"
   Print #fileNo, addTab(2); "qualTabName,"
   Print #fileNo, addTab(2); "statement"
   Print #fileNo, addTab(1); ")"

   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "V_ReorgChk"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objType,"
   Print #fileNo, addTab(2); "tableSchemaName,"
   Print #fileNo, addTab(2); "tableName,"
   Print #fileNo, addTab(2); "tempTsName"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "objType,"
   Print #fileNo, addTab(3); "tableSchemaName,"
   Print #fileNo, addTab(3); "tableName,"
   Print #fileNo, addTab(3); "tempTsName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SESSION.reorgChk"
   Print #fileNo, addTab(1); "),"

   Print #fileNo, addTab(2); "V_RunStats"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "onTable,"
   Print #fileNo, addTab(2); "onIndex,"
   Print #fileNo, addTab(2); "tableSchemaName,"
   Print #fileNo, addTab(2); "tableName"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); g_dbtBoolean; "(CASE WHEN T.tableName IS NULL THEN 0 ELSE 1 END),"
   Print #fileNo, addTab(3); g_dbtBoolean; "(CASE WHEN I.tableName IS NULL THEN 0 ELSE 1 END),"
   Print #fileNo, addTab(3); "A.tableSchemaName,"
   Print #fileNo, addTab(3); "A.tableName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SESSION.reorgChk A"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); "( SELECT DISTINCT tableSchemaName, tableName FROM SESSION.reorgChk WHERE objType = 'T' ) T"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "T.tableSchemaName = A.tableSchemaName"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.tableName = A.tableName"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); "( SELECT DISTINCT tableSchemaName, tableName FROM SESSION.reorgChk WHERE objType = 'I' ) I"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "I.tableSchemaName = A.tableSchemaName"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "I.tableName = A.tableName"
   Print #fileNo, addTab(1); "),"

   Print #fileNo, addTab(2); "V_Stmnt"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "type,"
   Print #fileNo, addTab(2); "tableSchemaName,"
   Print #fileNo, addTab(2); "tableName,"
   Print #fileNo, addTab(2); "stmt"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("

   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); g_dbtEnumId; "(1),"
   Print #fileNo, addTab(3); "tableSchemaName,"
   Print #fileNo, addTab(3); "tableName,"
   Print #fileNo, addTab(3); "CAST("
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "objType"
   Print #fileNo, addTab(5); "WHEN 'T' THEN 'REORG TABLE ' || RTRIM(tableSchemaName) || '.' || RTRIM(tableName) || (CASE WHEN reorgLobData_in = 1 THEN ' LONGLOBDATA' ELSE '' END)"
   Print #fileNo, addTab(5); "WHEN 'I' THEN 'REORG INDEXES ALL FOR TABLE ' || RTRIM(tableSchemaName) || '.' || RTRIM(tableName)"
   Print #fileNo, addTab(5); "ELSE CAST(NULL AS VARCHAR(1))"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); "AS VARCHAR(200)) AS STMNT"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_ReorgChk"
   Print #fileNo,
   Print #fileNo, addTab(2); "UNION ALL"
   Print #fileNo,
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); g_dbtEnumId; "(2),"
   Print #fileNo, addTab(3); "tableSchemaName,"
   Print #fileNo, addTab(3); "tableName,"
   Print #fileNo, addTab(3); "CAST("
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN onTable = 1 THEN 'RUNSTATS ON TABLE ' || RTRIM(tableSchemaName) || '.' || RTRIM(tableName) || ' WITH DISTRIBUTION AND DETAILED INDEXES ALL'"
   Print #fileNo, addTab(6); "WHEN onIndex = 1 THEN 'RUNSTATS ON TABLE ' || RTRIM(tableSchemaName) || '.' || RTRIM(tableName) || ' FOR DETAILED INDEXES ALL'"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); "AS VARCHAR(400)) AS STMNT"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_RunStats"
   Print #fileNo, addTab(1); ")"

   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ROWNUMBER() OVER (ORDER BY tableSchemaName ASC, tableName ASC, type ASC, stmt DESC),"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "LEFT(tableSchemaName || '.' || tableName, 100),"
   Print #fileNo, addTab(2); "stmt"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_Stmnt"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "COALESCE(doRunStats_in, "; gc_dbFalse; ") = "; gc_dbTrue
   Print #fileNo, addTab(3); "OR"
   Print #fileNo, addTab(2); "type = 1"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "execute REORGs (and RUNSTATs)"
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"

   Print #fileNo, addTab(2); "REPEAT"
   Print #fileNo, addTab(3); "SET v_stmntTxt = ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTempTabNameReorg
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "execTime IS NULL"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "numRetries < v_numRetriesToDo"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "numRetries <> -1"
   Print #fileNo, addTab(4); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(3); ");"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_stmntTxt IS NOT NULL THEN"
   Print #fileNo, addTab(4); "SET v_stmtBegin = CURRENT TIMESTAMP;"
   Print #fileNo, addTab(4); "SET v_doRetry   = "; gc_dbFalse; ";"
   Print #fileNo, addTab(4); "SET v_doIgnore  = "; gc_dbFalse; ";"
   Print #fileNo,
   Print #fileNo, addTab(4); "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);"
   Print #fileNo,
   Print #fileNo, addTab(4); "IF v_doIgnore = 1 THEN"
   Print #fileNo, addTab(5); "UPDATE"
   Print #fileNo, addTab(6); qualTempTabNameReorg
   Print #fileNo, addTab(5); "SET"
   Print #fileNo, addTab(6); "numRetries = -1"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "statement = v_stmntTxt"
   Print #fileNo, addTab(5); ";"
   Print #fileNo, addTab(4); "ELSEIF v_doRetry = 1 THEN"

   Print #fileNo, addTab(5); "UPDATE"
   Print #fileNo, addTab(6); qualTempTabNameReorg
   Print #fileNo, addTab(5); "SET"
   Print #fileNo, addTab(6); "numRetries = numRetries + 1"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "statement = v_stmntTxt"
   Print #fileNo, addTab(5); ";"

   Print #fileNo, addTab(4); "ELSE"

   Print #fileNo, addTab(5); "SET v_stmtEnd = CURRENT TIMESTAMP;"
   Print #fileNo,
   Print #fileNo, addTab(5); "UPDATE"
   Print #fileNo, addTab(6); qualTempTabNameReorg
   Print #fileNo, addTab(5); "SET"
   Print #fileNo, addTab(6); "execTime = (TIME('00:00:00') + TIMESTAMPDIFF(2, CHAR(v_stmtEnd - v_stmtBegin)) SECONDS)"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "statement = v_stmntTxt"
   Print #fileNo, addTab(5); ";"
   Print #fileNo,
   Print #fileNo, addTab(5); "COMMIT;"
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "UNTIL"
   Print #fileNo, addTab(3); "v_stmntTxt IS NULL"
   Print #fileNo, addTab(2); "END REPEAT;"

   genProcSectionHeader fileNo, "determine number of tables failed", 2
   Print #fileNo, addTab(2); "SET failCount_out = (SELECT COUNT(DISTINCT qualTabName) FROM "; qualTempTabNameReorg; " WHERE execTime IS NULL AND numRetries <> -1);"

   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "count number of tables & indexes"
   Print #fileNo, addTab(1); "SET tabCount_out  = (SELECT COUNT(DISTINCT qualTabName) FROM "; qualTempTabNameReorg; " WHERE numRetries <> -1) - failCount_out;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in < 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CAST(statement AS VARCHAR(150)) AS statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTempTabNameReorg
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "ELSEIF mode_in = 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CAST((CASE WHEN execTime IS NULL THEN '-' ELSE '+' END) AS CHAR(1)) AS F,"
   Print #fileNo, addTab(5); "CAST(statement AS VARCHAR(150)) AS statement,"
   Print #fileNo, addTab(5); "execTime"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTempTabNameReorg
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "numRetries <> -1"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "'objType_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
     "'tabSchemaPatternExcl_in", "'tabNamePatternExcl_in", "doRunStats_in", "force_in", "reorgLobData_in", "tabCount_out", "failCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameReorg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "objType_in", "CHAR(1)", True, "(optional) determines the type of objects to REORG ('I' = index, 'T' = table)"
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schema of tables to REORG"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the name of tables to REORG"
   genProcParm fileNo, "IN", "tabSchemaPatternExcl_in", g_dbtDbSchemaName, True, "(optional) determines the schemas of tables to exclude from REORG"
   genProcParm fileNo, "IN", "tabNamePatternExcl_in", g_dbtDbTableName, True, "(optional) determines the tables to exclude from REORG"
   genProcParm fileNo, "IN", "doRunStats_in", g_dbtBoolean, True, "if set to '1': execute RUNSTATS on reorganized tables"
   genProcParm fileNo, "IN", "force_in", g_dbtBoolean, True, "if set to '1': REORG all objects, otherwise only those recommended"

   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of REORGS on tables"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of procedures failed due to lock-timeout"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "'objType_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
     "'tabSchemaPatternExcl_in", "'tabNamePatternExcl_in", "doRunStats_in", "force_in", "tabCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameReorg; "(mode_in, objType_in, tabSchemaPattern_in, tabNamePattern_in, tabSchemaPatternExcl_in, tabSchemaPatternExcl_in, doRunStats_in, force_in, 0, tabCount_out, v_failCount);"

   genSpLogProcExit fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "'objType_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
     "'tabSchemaPatternExcl_in", "'tabNamePatternExcl_in", "doRunStats_in", "force_in", "tabCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for REORG of tables / indexes", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameReorg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "objType_in", "CHAR(1)", True, "(optional) determines the type of objects to REORG ('I' = index, 'T' = table)"
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schema of tables to REORG"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the name of tables to REORG"
   genProcParm fileNo, "IN", "doRunStats_in", g_dbtBoolean, True, "if set to '1': execute RUNSTATS on reorganized tables"
   genProcParm fileNo, "IN", "force_in", g_dbtBoolean, True, "if set to '1': REORG all objects, otherwise only those recommended"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of REORGS on tables"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "'objType_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
     "doRunStats_in", "force_in", "tabCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameReorg; "(mode_in, objType_in, tabSchemaPattern_in, tabNamePattern_in, NULL, NULL, doRunStats_in, force_in, 0, tabCount_out, v_failCount);"

   genSpLogProcExit fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "'objType_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
     "doRunStats_in", "force_in", "tabCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for REORG of tables / indexes", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameReorg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schema of tables to REORG"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the name of tables to REORG"
   genProcParm fileNo, "IN", "doRunStats_in", g_dbtBoolean, True, "if set to '1': execute RUNSTATS on reorganized tables"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of REORGS"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "'tabSchemaPattern_in", "'tabNamePattern_in", "doRunStats_in", "tabCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameReorg; "(mode_in, NULL, tabSchemaPattern_in, tabNamePattern_in, NULL, NULL, doRunStats_in, 0, tabCount_out, v_failCount);"

   genSpLogProcExit fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "'tabSchemaPattern_in", "'tabNamePattern_in", "doRunStats_in", "tabCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for REORG of tables / indexes", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameReorg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "doRunStats_in", g_dbtBoolean, True, "if set to '1': execute RUNSTATS on reorganized tables"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of REORGS"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "doRunStats_in", "tabCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameReorg; "(mode_in, NULL, NULL, NULL, NULL, NULL, doRunStats_in, 0, tabCount_out, v_failCount);"

   genSpLogProcExit fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "doRunStats_in", "tabCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for REORG of tables / indexes", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameReorg
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of REORGS"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "tabCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameReorg; "(mode_in, NULL, NULL, NULL, NULL, NULL, 1, 0, tabCount_out, v_failCount);"

   genSpLogProcExit fileNo, qualProcedureNameReorg, ddlType, , "mode_in", "tabCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for retrieving status of statistics
   ' ####################################################################################################################

   Dim qualProcedureNameGetStats As String
   qualProcedureNameGetStats = genQualProcName(g_sectionIndexDbAdmin, spnGetstats, ddlType)

   printSectionHeader "SP for retrieving status of statistics", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameGetStats
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "determines the schemas of tables to retrieve status for"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "determines the tables to retrieve status for"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of tables"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genProcSectionHeader fileNo, "declare cursor", 1, Not supportSpLogging Or Not generateSpLogMessages
   Print #fileNo, addTab(1); "DECLARE tabCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "CAST(RTRIM(LEFT(TABSCHEMA,30)) AS "; g_dbtDbSchemaName; ") TABSCHEMA,"
   Print #fileNo, addTab(3); "CAST(RTRIM(LEFT(TABNAME,  50)) AS VARCHAR(50)) TABNAME,"
   Print #fileNo, addTab(3); "STATS_TIME STATS_TIME,"
   Print #fileNo, addTab(3); "CARD       STATS_CARD"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "TYPE = 'T'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(TABSCHEMA)) LIKE COALESCE(UCASE(tabSchemaPattern_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(TABNAME)) LIKE COALESCE(UCASE(tabNamePattern_in), '%')"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); ";"
 
   genSpLogProcEnter fileNo, qualProcedureNameGetStats, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "tabCount_out"
 
   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET tabCount_out = ("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "COUNT(*)"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "TYPE = 'T'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(TABSCHEMA)) LIKE COALESCE(UCASE(tabSchemaPattern_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(TABNAME)) LIKE COALESCE(UCASE(tabNamePattern_in), '%')"
   Print #fileNo, addTab(1); ");"

   genProcSectionHeader fileNo, "leave cursor open for application"
   Print #fileNo, addTab(1); "OPEN tabCursor;"

   genSpLogProcExit fileNo, qualProcedureNameGetStats, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "tabCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for updating statistics
   ' ####################################################################################################################

   Dim qualProcedureNameRunstats As String
   qualProcedureNameRunstats = genQualProcName(g_sectionIndexDbAdmin, spnRunstats, ddlType)

   printSectionHeader "SP for updating statistics", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRunstats
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "determines the schemas of tables to 'runstats' (default '" & g_allSchemaNamePattern & "')"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "determines the tables to 'runstats' (default '%')"
   genProcParm fileNo, "IN", "tabSchemaPatternExcl_in", g_dbtDbSchemaName, True, "(optional) determines the schemas of tables to exclude from 'runstats'"
   genProcParm fileNo, "IN", "tabNamePatternExcl_in", g_dbtDbTableName, True, "(optional) determines the tables to exclude from 'runstats'"

   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables successfully processed"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of tables failed due to lock-timeout"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "cursorAtEnd", "24501"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_lastTabSchema", g_dbtDbSchemaName, "NULL"
   genVarDecl fileNo, "v_lastTabName", g_dbtDbTableName, "NULL"
   genVarDecl fileNo, "v_numRetriesToDo", "INTEGER", "NULL"
   genVarDecl fileNo, "v_doRetry", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_diagnostics", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_atEnd", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_retryLoop", "SMALLINT", "1"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR cursorAtEnd"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "IF SQLCODE = -501 THEN"
   Print #fileNo, addTab(3); "SET v_retryLoop = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_atEnd = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"

   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING;"
   Print #fileNo, addTab(2); "IF SQLCODE = -2310 AND v_diagnostics = '-911' THEN"
   Print #fileNo, addTab(3); "SET v_doRetry = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "IF v_numRetriesToDo = "; CStr(numRetriesRunstatsRebindOnLockTimeout); " THEN"
   Print #fileNo, addTab(4); "SET failCount_out = failCount_out + 1;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "ELSEIF SQLCODE = -2310 AND v_diagnostics = '-1477' THEN"
   Print #fileNo, addTab(3); "SET v_numRetriesToDo = 0;"
   Print #fileNo, addTab(3); "SET failCount_out    = failCount_out + 1;"
   Print #fileNo, addTab(2); "ELSE"
   genSpLogProcEscape fileNo, qualProcedureNameRunstats, ddlType, 3, "mode_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
                     "'tabSchemaPatternExcl_in", "'tabNamePatternExcl_in", "tabCount_out", "failCount_out"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"

   Dim qualTempTabNameRunstats As String
   qualTempTabNameRunstats = tempTabNameStatement & "Runstats"
   genDdlForTempStatement fileNo, 1, True, 250, , , , , "Runstats", , , True

   genSpLogProcEnter fileNo, qualProcedureNameRunstats, ddlType, , "mode_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
                     "'tabSchemaPatternExcl_in", "'tabNamePatternExcl_in", "tabCount_out", "failCount_out"
 
   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET tabCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"

   genProcSectionHeader fileNo, "since calling RUNSTATS via ADMIN_CMD may close cursors we apply some very cumbersome implementation pattern involving nested loops", 1, True
   Print #fileNo, addTab(1); "WHILE v_retryLoop = 1 DO"
   Print #fileNo, addTab(2); "SET v_retryLoop = "; gc_dbFalse; ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "tabLoop: FOR tabLoop AS csr CURSOR WITH HOLD FOR"
 
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "TABSCHEMA TS,"
   Print #fileNo, addTab(4); "TABNAME   TN"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABLES"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "TYPE = 'T'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UCASE(RTRIM(TABSCHEMA)) LIKE COALESCE(UCASE(tabSchemaPattern_in), '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UCASE(RTRIM(TABNAME)) LIKE COALESCE(UCASE(tabNamePattern_in), '%')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UCASE(RTRIM(TABSCHEMA)) NOT LIKE COALESCE(UCASE(tabSchemaPatternExcl_in), '')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "UCASE(RTRIM(TABNAME)) NOT LIKE COALESCE(UCASE(tabNamePatternExcl_in), '')"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "(v_lastTabSchema is NULL)"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "(TABSCHEMA > v_lastTabSchema)"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "(TABSCHEMA = v_lastTabSchema AND TABNAME > v_lastTabName)"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "TABSCHEMA,"
   Print #fileNo, addTab(4); "TABNAME"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); "DO"

   genProcSectionHeader fileNo, "leave or retry loop if requested", 3, True
   Print #fileNo, addTab(3); "IF v_atEnd = 1 OR v_retryLoop = 1 THEN"
   Print #fileNo, addTab(4); "LEAVE tabLoop;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_lastTabSchema = TS;"
   Print #fileNo, addTab(3); "SET v_lastTabName   = TN;"
   Print #fileNo, addTab(3); "SET v_stmntTxt      = 'RUNSTATS ON TABLE ' || RTRIM(TS) || '.' || RTRIM(TN) || ' WITH DISTRIBUTION ON ALL COLUMNS AND DETAILED INDEXES ALL';"
   Print #fileNo, addTab(3); "SET tabCount_out    = tabCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "SET v_numRetriesToDo = "; CStr(numRetriesRunstatsRebindOnLockTimeout); ";"
   Print #fileNo, addTab(4); "SET v_doRetry = "; gc_dbFalse; ";"
   Print #fileNo, addTab(4); "WHILE v_numRetriesToDo > 0 DO"
   Print #fileNo, addTab(5); "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);"
   Print #fileNo, addTab(5); "SET v_numRetriesToDo = (CASE v_doRetry WHEN 0 THEN 0 ELSE v_numRetriesToDo - 1 END);"
   Print #fileNo, addTab(4); "END WHILE;"
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "END IF;"
 
   genProcSectionHeader fileNo, "store statement in temporary table", 3
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'INSERT INTO "; qualTempTabNameRunstats; "(seqNo, flag, statement) VALUES (' || CHAR(tabCount_out) || ', ''' ||"
   Print #fileNo, addTab(11); " (CASE v_doRetry WHEN 1 THEN '-' ELSE '+' END) || ''', ''' || v_stmntTxt || ''')';"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
 
   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END WHILE;"

   genProcSectionHeader fileNo, "determine number of successful RUNSTATS"
   Print #fileNo, addTab(1); "SET tabCount_out = tabCount_out - failCount_out;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag AS f,"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTempTabNameRunstats
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(2); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameRunstats, ddlType, , "mode_in", "'tabSchemaPattern_in", "'tabNamePattern_in", _
                     "'tabSchemaPatternExcl_in", "'tabNamePatternExcl_in", "tabCount_out", "failCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for updating statistics", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRunstats
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "determines the schemas of tables to 'runstats'"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "determines the tables to 'runstats'"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of successfully tables processed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameRunstats, ddlType, , "mode_in", "'tabSchemaPattern_in", "'tabNamePattern_in", "tabCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameRunstats; "(mode_in, tabSchemaPattern_in, tabNamePattern_in, NULL, NULL, tabCount_out, v_failCount);"

   genSpLogProcExit fileNo, qualProcedureNameRunstats, ddlType, , "mode_in", "'tabSchemaPattern_in", "'tabNamePattern_in", "tabCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for updating statistics", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRunstats
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", False, "number of tables successfully processed"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameRunstats, ddlType, , "mode_in", "tabCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameRunstats; "(mode_in, NULL, NULL, NULL, NULL, tabCount_out, v_failCount);"

   genSpLogProcExit fileNo, qualProcedureNameRunstats, ddlType, , "mode_in", "tabCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   If ddlType <> edtPdm Then
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    SP temporarily disabling triggers
   ' ####################################################################################################################
 
   Dim qualProcedureNameTrigDisable As String
   qualProcedureNameTrigDisable = genQualProcName(g_sectionIndexDbAdmin, spnTriggerDisable, ddlType)

   printSectionHeader "SP temporarily disabling triggers", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTrigDisable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the table schemas that apply"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the table names that apply"
   genProcParm fileNo, "IN", "trigSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the trigger schemas that apply"
   genProcParm fileNo, "IN", "trigNamePattern_in", "VARCHAR(30)", True, "(optional) determines the trigger names that apply"
   genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) determines the organization owning the tables that apply"
   genProcParm fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "(optional) determines the data pool holding the tables that apply"

   genProcParm fileNo, "OUT", "trigCount_out", "INTEGER", True, "number of triggers disabled"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of triggers failed to disable"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "authFail", "42501"
   genCondDecl fileNo, "invocationFail", "42509"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_returnStatus", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR authFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42501;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR invocationFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42509;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcEnter fileNo, qualProcedureNameTrigDisable, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "'trigSchemaPattern_in", "'trigNamePattern_in", "orgId_in", "accessModeId_in", "trigCount_out", "failCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET trigCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "FOR trigLoop AS csr CURSOR FOR"

   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TRIGSCHEMA AS TS,"
   Print #fileNo, addTab(3); "T.TRIGNAME   AS TN"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TRIGGERS T"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "T.TABSCHEMA = P."; g_anPdmFkSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME = P."; g_anPdmTableName; ""
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "RTRIM(T.TABSCHEMA) LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(T.TRIGSCHEMA) LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(T.TABSCHEMA) LIKE COALESCE(UCASE(tabSchemaPattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME LIKE COALESCE(UCASE(tabNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(T.TRIGSCHEMA) LIKE COALESCE(UCASE(trigSchemaPattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TRIGNAME LIKE COALESCE(UCASE(trigNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(orgId_in IS NULL OR P."; g_anOrganizationId; " = orgId_in)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(accessModeId_in IS NULL OR P."; g_anPoolTypeId; " = accessModeId_in)"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(2); "WITH UR"

   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "INSERT INTO"

   Print #fileNo, addTab(3); g_qualTabNameDisabledTriggers
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "TRIGSCHEMA,"
   Print #fileNo, addTab(3); "TRIGNAME,"
   Print #fileNo, addTab(3); "TABSCHEMA,"
   Print #fileNo, addTab(3); "TABNAME,"
   Print #fileNo, addTab(3); "QUALIFIER,"
   Print #fileNo, addTab(3); "FUNCPATH,"
   Print #fileNo, addTab(3); "TEXT"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "TS,"
   Print #fileNo, addTab(3); "TN,"
   Print #fileNo, addTab(3); "TR.TABSCHEMA,"
   Print #fileNo, addTab(3); "TR.TABNAME,"
   Print #fileNo, addTab(3); "TR.QUALIFIER,"
   Print #fileNo, addTab(3); "TR.FUNC_PATH,"
   Print #fileNo, addTab(3); "CAST(TR.TEXT AS CLOB(2M))"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TRIGGERS TR"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "TR.TRIGSCHEMA = TS"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "TR.TRIGNAME = TN"
   Print #fileNo, addTab(2); ";"

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DROP TRIGGER ' || TS || '.' || TN;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET trigCount_out = trigCount_out + 1;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET failCount_out = failCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit fileNo, qualProcedureNameTrigDisable, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "'trigSchemaPattern_in", "'trigNamePattern_in", "orgId_in", "accessModeId_in", "trigCount_out", "failCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   If ddlType <> edtPdm Then
     ' the rest is not supported for LDM
     Exit Sub
   End If

   ' ####################################################################################################################

   printSectionHeader "SP temporarily disabling triggers", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTrigDisable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "trigSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schemas that apply"
   genProcParm fileNo, "IN", "trigNamePattern_in", "VARCHAR(30)", True, "(optional) determines the trigger names that apply"

   genProcParm fileNo, "OUT", "trigCount_out", "INTEGER", True, "number of triggers disabled"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of triggers failed to disable"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameTrigDisable; "(NULL, NULL, trigSchemaPattern_in, trigNamePattern_in, NULL, NULL, trigCount_out, failCount_out);"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP enabling disabled triggers
   ' ####################################################################################################################

   Dim qualProcedureNameTrigEnable As String
   qualProcedureNameTrigEnable = genQualProcName(g_sectionIndexDbAdmin, spnTriggerEnable, ddlType)

   printSectionHeader "SP enabling disabled triggers", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTrigEnable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the table schemas that apply"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the table names that apply"
   genProcParm fileNo, "IN", "trigSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the trigger schemas that apply"
   genProcParm fileNo, "IN", "trigNamePattern_in", "VARCHAR(30)", True, "(optional) determines the trigger names that apply"
   genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) determines the organization owning the tables that apply"
   genProcParm fileNo, "IN", "accessModeId_in", g_dbtEnumId, True, "(optional) determines the data pool holding the tables that apply"

   genProcParm fileNo, "OUT", "trigCount_out", "INTEGER", True, "number of triggers sucessfully enabled"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of triggers failed to enable"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "triggerAlreadyEnabled", "42710"
   genCondDecl fileNo, "authFail", "42501"
   genCondDecl fileNo, "invocationFail", "42509"

   genProcSectionHeader fileNo, "declare variables"
 '  genVarDecl fileNo, "v_qualifier", g_dbtDbSchemaName, "NULL"
 '  genVarDecl fileNo, "v_funcPath", "VARCHAR(254)", "NULL"
   genVarDecl fileNo, "v_curQualifier", g_dbtDbSchemaName, "NULL"
   genVarDecl fileNo, "v_curFuncPath", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_stmntTxt", "CLOB(2448)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_returnStatus", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR triggerAlreadyEnabled"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42710;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR authFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42501;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR invocationFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42509;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcEnter fileNo, qualProcedureNameTrigEnable, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "'trigSchemaPattern_in", "'trigNamePattern_in", "orgId_in", "accessModeId_in", "trigCount_out", "failCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET trigCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"
 
   genProcSectionHeader fileNo, "save current environment settings"
   Print #fileNo, addTab(1); "SET v_curQualifier = CURRENT SCHEMA;"
   Print #fileNo, addTab(1); "SET v_curFuncPath  = CURRENT FUNCTION PATH;"

   Print #fileNo,
   Print #fileNo, addTab(1); "FOR trigLoop AS csr CURSOR WITH HOLD FOR"

   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "T.TRIGSCHEMA AS TS,"
   Print #fileNo, addTab(3); "T.TRIGNAME   AS TN,"
   Print #fileNo, addTab(3); "COALESCE(T.QUALIFIER, CURRENT SCHEMA) AS QUALIFIER,"
   Print #fileNo, addTab(3); "COALESCE(T.FUNCPATH,  CURRENT FUNCTION PATH) AS FUNCPATH,"
   Print #fileNo, addTab(3); "T.TEXT"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDisabledTriggers; " T"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "T.TABSCHEMA = P."; g_anPdmFkSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME = P."; g_anPdmTableName; ""
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "UCASE(RTRIM(T.TABSCHEMA)) LIKE COALESCE(UCASE(tabSchemaPattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(T.TABNAME)) LIKE COALESCE(UCASE(tabNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(T.TRIGSCHEMA)) LIKE COALESCE(UCASE(trigSchemaPattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(T.TRIGNAME)) LIKE COALESCE(UCASE(trigNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(orgId_in IS NULL OR P."; g_anOrganizationId; " = orgId_in)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(accessModeId_in IS NULL OR P."; g_anPoolTypeId; " = accessModeId_in)"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(2); "WITH UR"

   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SET CURRENT SCHEMA = ' || QUALIFIER;"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SET CURRENT FUNCTION PATH = ' || FUNCPATH;"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE TEXT;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET trigCount_out = trigCount_out + 1;"
   Print #fileNo,

   Print #fileNo, addTab(3); "DELETE FROM"
   Print #fileNo, addTab(4); g_qualTabNameDisabledTriggers; " TR"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "TR.TRIGSCHEMA = TS"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "TR.TRIGNAME = TN"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET failCount_out = failCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "reset environment settings"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'SET CURRENT SCHEMA = ' || v_curQualifier;"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'SET CURRENT FUNCTION PATH = ' || v_curFuncPath;"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"

   genSpLogProcExit fileNo, qualProcedureNameTrigEnable, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "'trigSchemaPattern_in", "'trigNamePattern_in", "orgId_in", "accessModeId_in", "trigCount_out", "failCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_returnStatus;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP enabling disabled triggers", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTrigEnable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "trigSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schemas that apply"
   genProcParm fileNo, "IN", "trigNamePattern_in", "VARCHAR(30)", True, "(optional) determines the trigger names that apply"

   genProcParm fileNo, "OUT", "trigCount_out", "INTEGER", True, "number of triggers sucessfully enabled"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of triggers failed to enable"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameTrigEnable; "(NULL, NULL, trigSchemaPattern_in, trigNamePattern_in, NULL, NULL, trigCount_out, failCount_out);"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   If supportIndexMetrics Then
     ' ####################################################################################################################
     ' #    INSERT Trigger maintaining redundant columns in table for 'IndexMetrics'
     ' ####################################################################################################################

     Dim qualTriggerName As String
     qualTriggerName = genQualTriggerNameByClassIndex(g_classIndexIndexMetrics, ddlType, , , , , , , , "INS", eondmSuffix)

     printSectionHeader "Insert-Trigger for maintaining redundant columns in """ & g_qualTabNameIndexMetrics & """", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "AFTER INSERT ON"
     Print #fileNo, addTab(1); g_qualTabNameIndexMetrics
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     Print #fileNo, addTab(1); "IF"
     Print #fileNo, addTab(2); "(NEWRECORD.INDSCHEMA IS NULL)"
     Print #fileNo, addTab(3); "OR"
     Print #fileNo, addTab(2); "(NEWRECORD.INDNAME IS NULL)"
     Print #fileNo, addTab(3); "OR"
     Print #fileNo, addTab(2); "(NEWRECORD.TABSCHEMA IS NULL)"
     Print #fileNo, addTab(3); "OR"
     Print #fileNo, addTab(2); "(NEWRECORD.TABNAME IS NULL)"
     Print #fileNo, addTab(1); "THEN"

     Print #fileNo, addTab(2); "UPDATE"
     Print #fileNo, addTab(3); g_qualTabNameIndexMetrics; " IM"
     Print #fileNo, addTab(2); "SET"

     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "IM.INDSCHEMA,"
     Print #fileNo, addTab(4); "IM.INDNAME,"
     Print #fileNo, addTab(4); "IM.TABSCHEMA,"
     Print #fileNo, addTab(4); "IM.TABNAME"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(3); "="
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "IND.INDSCHEMA,"
     Print #fileNo, addTab(5); "IND.INDNAME,"
     Print #fileNo, addTab(5); "TAB.TABSCHEMA,"
     Print #fileNo, addTab(5); "TAB.TABNAME"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); "SYSCAT.INDEXES IND"
     Print #fileNo, addTab(4); "INNER JOIN"
     Print #fileNo, addTab(5); "SYSCAT.TABLES TAB"
     Print #fileNo, addTab(4); "ON"
     Print #fileNo, addTab(5); "IND.TABSCHEMA = TAB.TABSCHEMA"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "IND.TABNAME = TAB.TABNAME"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "IND.IID = NEWRECORD.INDEXID"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "TAB.TBSPACEID = NEWRECORD.TBSPACEID"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "TAB.TABLEID = NEWRECORD.TABLEID"
     Print #fileNo, addTab(4); "FETCH FIRST 1 ROWS ONLY"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "IM.TBSPACEID = NEWRECORD.TBSPACEID"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "IM.TABLEID = NEWRECORD.TABLEID"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "IM.INDEXID = NEWRECORD.INDEXID"
     Print #fileNo, addTab(2); ";"

     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If
 End Sub
 
 
 Private Sub genDbAdminDdl7( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     ' we do not support this for LDM
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    SP temporarily disabling indexes
   ' ####################################################################################################################
 
   Dim qualProcedureNameIndexDisable As String
   qualProcedureNameIndexDisable = genQualProcName(g_sectionIndexDbAdmin, spnIndexDisable, ddlType)

   printSectionHeader "SP temporarily disabling indexes", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameIndexDisable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "indexSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the (index resp. table) schemas that apply"
   genProcParm fileNo, "IN", "indexNamePattern_in", "VARCHAR(50)", True, "(optional) determines the index names resp. table names that apply"
   If supportIndexMetrics Then
     genProcParm fileNo, "IN", "onlyUnUsedIndexes_in", g_dbtBoolean, True, "if set to 1 disable only 'unused indexes' (wrt. table """ & g_qualTabNameIndexMetrics & """)"
   End If
   genProcParm fileNo, "OUT", "indexCount_out", "INTEGER", True, "number of indexes disabled"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of indexes failed to disable"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "authFail", "42501"
   genCondDecl fileNo, "invocationFail", "42509"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_returnStatus", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR authFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42501;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR invocationFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42509;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   If supportIndexMetrics Then
     genSpLogProcEnter fileNo, qualProcedureNameIndexDisable, ddlType, , "'indexSchemaPattern_in", "'indexNamePattern_in", "onlyUnUsedIndexes_in", "indexCount_out", "failCount_out"
   Else
     genSpLogProcEnter fileNo, qualProcedureNameIndexDisable, ddlType, , "'indexSchemaPattern_in", "'indexNamePattern_in", "indexCount_out", "failCount_out"
   End If

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET indexCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "FOR indexLoop AS indCsr CURSOR WITH HOLD FOR"

   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "IND.INDSCHEMA  AS IS,"
   Print #fileNo, addTab(3); "IND.INDNAME    AS IN,"
   Print #fileNo, addTab(3); "IND.TABSCHEMA  AS TS,"
   Print #fileNo, addTab(3); "IND.TABNAME    AS TN,"
   Print #fileNo, addTab(3); "IND.UNIQUERULE AS UR"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.INDEXES IND"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.TABLES TAB"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "TAB.TABSCHEMA = IND.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "TAB.TABNAME = IND.TABNAME"
   If supportIndexMetrics Then
     Print #fileNo, addTab(2); "LEFT OUTER JOIN"
     Print #fileNo, addTab(3); g_qualTabNameIndexMetrics; " IM"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "IM.TABSCHEMA IS NULL"
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "IM.TABNAME IS NULL"
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "IM.INDSCHEMA IS NULL"
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "IM.INDNAME IS NULL"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "TAB.TBSPACEID = IM.TBSPACEID"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "TAB.TABLEID = IM.TABLEID"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "IND.IID = IM.INDEXID"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(4); "OR"
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "TAB.TABSCHEMA = IM.TABSCHEMA"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TAB.TABNAME = IM.TABNAME"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "IND.INDSCHEMA = IM.INDSCHEMA"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "IND.INDNAME = IM.INDNAME"
     Print #fileNo, addTab(3); ")"
   End If

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "IND.UNIQUERULE NOT IN ('U', 'P')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "((RTRIM(IND.INDSCHEMA) LIKE '"; g_allSchemaNamePattern; "') OR (RTRIM(IND.TABSCHEMA) LIKE '"; g_allSchemaNamePattern; "'))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "((RTRIM(IND.INDSCHEMA) LIKE COALESCE(UCASE(indexSchemaPattern_in), '%')) OR (RTRIM(IND.TABSCHEMA) LIKE COALESCE(UCASE(indexSchemaPattern_in), '%')))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "((IND.INDNAME LIKE COALESCE(UCASE(indexNamePattern_in), '%')) OR (IND.TABNAME LIKE COALESCE(UCASE(indexNamePattern_in), '%')))"
   If supportIndexMetrics Then
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "(IM.NUMSCANS = 0 OR COALESCE(onlyUnUsedIndexes_in, 0) <> 1)"
   End If
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "IF UR = 'P' THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(TS) || '.' || RTRIM(TN) || ' ADD CONSTRAINT ' || RTRIM(IN) || ' PRIMARY KEY (';"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CREATE' || (CASE WHEN UR = 'U' THEN ' UNIQUE' ELSE '' END) || ' INDEX ' || RTRIM(IS) || '.' || RTRIM(IN) || ' ON ' || RTRIM(TS) || '.' || RTRIM(TN) || ' (';"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "FOR colLoop AS colCsr CURSOR WITH HOLD FOR"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "COL.COLNAME  AS CN,"
   Print #fileNo, addTab(4); "COL.COLSEQ   AS CS,"
   Print #fileNo, addTab(4); "COL.COLORDER AS CO"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.INDEXCOLUSE COL"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "COL.INDSCHEMA = IS"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COL.INDNAME = IN"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "COL.COLSEQ"
   Print #fileNo, addTab(3); "FOR READ ONLY"

   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || (CASE WHEN CS = 1 THEN '' ELSE ', ' END) || CN || (CASE WHEN CO = 'A' THEN ' ASC' ELSE ' DESC' END);"
   Print #fileNo, addTab(2); "END FOR;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt || ')';"

   Print #fileNo,
   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); g_qualTabNameDisabledIndexes; " DIN"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "DIN.INDSCHEMA = IS"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "DIN.INDNAME = IN"
   Print #fileNo, addTab(2); ";"

   Print #fileNo,
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); g_qualTabNameDisabledIndexes
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "INDSCHEMA,"
   Print #fileNo, addTab(3); "INDNAME,"
   Print #fileNo, addTab(3); "TABSCHEMA,"
   Print #fileNo, addTab(3); "TABNAME,"
   Print #fileNo, addTab(3); "TEXT"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "IS,"
   Print #fileNo, addTab(3); "IN,"
   Print #fileNo, addTab(3); "TS,"
   Print #fileNo, addTab(3); "TN,"
   Print #fileNo, addTab(3); "v_stmntTxt"
   Print #fileNo, addTab(2); ");"

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DROP INDEX ' || RTRIM(IS) || '.' || RTRIM(IN);"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET indexCount_out = indexCount_out + 1;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET failCount_out = failCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(3); "DELETE FROM"
   Print #fileNo, addTab(4); g_qualTabNameDisabledIndexes; " DIN"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "DIN.INDSCHEMA = IS"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DIN.INDNAME = IN"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "COMMIT;"

   Print #fileNo, addTab(1); "END FOR;"
 
   If supportIndexMetrics Then
     genSpLogProcExit fileNo, qualProcedureNameIndexDisable, ddlType, , "'indexSchemaPattern_in", "'indexNamePattern_in", "onlyUnUsedIndexes_in", "indexCount_out", "failCount_out"
   Else
     genSpLogProcExit fileNo, qualProcedureNameIndexDisable, ddlType, , "'indexSchemaPattern_in", "'indexNamePattern_in", "indexCount_out", "failCount_out"
   End If

   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_returnStatus;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP enabling disabled indexes
   ' ####################################################################################################################

   Dim qualProcedureNameIndexEnable As String
   qualProcedureNameIndexEnable = genQualProcName(g_sectionIndexDbAdmin, spnIndexEnable, ddlType)

   printSectionHeader "SP enabling disabled indexes", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameIndexEnable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "indexSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the (index resp. table) schemas that apply"
   genProcParm fileNo, "IN", "indexNamePattern_in", "VARCHAR(50)", True, "(optional) determines the index names resp. table names that apply"

   genProcParm fileNo, "OUT", "indexCount_out", "INTEGER", True, "number of indexes sucessfully enabled"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of indexes failed to enable"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "indexAlreadyEnabled", "01550"
   genCondDecl fileNo, "authFail", "42501"
   genCondDecl fileNo, "invocationFail", "42509"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "CLOB(70000)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_returnStatus", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR indexAlreadyEnabled"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(2); "SET v_returnStatus = 01550;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR authFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42501;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR invocationFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42509;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcEnter fileNo, qualProcedureNameIndexEnable, ddlType, , "'indexSchemaPattern_in", "'indexNamePattern_in", "indexCount_out", "failCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET indexCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "FOR indLoop AS indCsr CURSOR WITH HOLD FOR"

   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "INDSCHEMA AS IS,"
   Print #fileNo, addTab(3); "INDNAME   AS IN,"
   Print #fileNo, addTab(3); "TEXT"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDisabledIndexes
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "((UCASE(RTRIM(INDSCHEMA)) LIKE COALESCE(UCASE(indexSchemaPattern_in), '%')) OR (UCASE(RTRIM(TABSCHEMA)) LIKE COALESCE(UCASE(indexSchemaPattern_in), '%')))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "((UCASE(RTRIM(INDNAME)) LIKE COALESCE(UCASE(indexNamePattern_in), '%')) OR (UCASE(RTRIM(TABNAME)) LIKE COALESCE(UCASE(indexNamePattern_in), '%')))"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE TEXT;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET indexCount_out = indexCount_out + 1;"
   Print #fileNo,

   Print #fileNo, addTab(3); "DELETE FROM"
   Print #fileNo, addTab(4); g_qualTabNameDisabledIndexes; " DIN"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "DIN.INDSCHEMA = IS"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DIN.INDNAME = IN"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET failCount_out = failCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit fileNo, qualProcedureNameIndexEnable, ddlType, , "'indexSchemaPattern_in", "'indexNamePattern_in", "indexCount_out", "failCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_returnStatus;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP temporarily disabling foreign keys
   ' ####################################################################################################################
 
   Dim qualProcedureNameFkDisable As String
   qualProcedureNameFkDisable = genQualProcName(g_sectionIndexDbAdmin, spnFkDisable, ddlType)

   printSectionHeader "SP temporarily disabling foreign Keys", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameFkDisable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the table schemas that apply"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the table names that apply"
   genProcParm fileNo, "IN", "refTabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schemas of referred tables that apply"
   genProcParm fileNo, "IN", "refTabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the names of referred tables that apply"

   genProcParm fileNo, "OUT", "fkCount_out", "INTEGER", True, "number of foreign keys disabled"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of foreign keys failed to disable"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "authFail", "42501"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(2048)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_returnStatus", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR authFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42501;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter fileNo, qualProcedureNameFkDisable, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "'refTabSchemaPattern_in", "'refTabNamePattern_in", "fkCount_out", "failCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET fkCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"
 
  Print #fileNo,
   Print #fileNo, addTab(1); "FOR fkLoop AS fkCsr CURSOR WITH HOLD FOR"

   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "FK.CONSTNAME    AS CN,"
   Print #fileNo, addTab(3); "FK.TABSCHEMA    AS TS,"
   Print #fileNo, addTab(3); "FK.TABNAME      AS TN,"
   Print #fileNo, addTab(3); "FK.REFTABSCHEMA AS RS,"
   Print #fileNo, addTab(3); "FK.REFTABNAME   AS RN,"
   Print #fileNo, addTab(3); "FK.DELETERULE   AS DR,"
   Print #fileNo, addTab(3); "FK.UPDATERULE   AS UR,"
   Print #fileNo, addTab(3); "tc.enforced       AS l_enforced,"
   Print #fileNo, addTab(3); "tc.enablequeryopt AS l_enablequeryopt"
   Print #fileNo, addTab(2); ", LISTAGG( fk_col.colname, ', ') WITHIN GROUP( ORDER BY fk_col.colseq ) AS l_fk_colname"
   Print #fileNo, addTab(2); ", LISTAGG( pk_col.colname, ', ') WITHIN GROUP( ORDER BY pk_col.colseq ) AS l_pk_colname"
 
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.REFERENCES FK"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "syscat.tabconst AS tc"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "fk.constname = tc.constname"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "fk.tabschema = tc.tabschema"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "fk.tabname = tc.tabname"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "syscat.keycoluse as fk_col"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "fk.constname = fk_col.constname"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "fk.tabschema = fk_col.tabschema"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "fk.tabname   = fk_col.tabname"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "syscat.keycoluse as pk_col"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "fk.refkeyname   = pk_col.constname"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "fk.reftabschema = pk_col.tabschema"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "fk.reftabname   = pk_col.tabname"
   Print #fileNo, addTab(2); "AND"
   genProcSectionHeader fileNo, "There must be the same number of foreign key columns as there are in the parent key  See CREATE TABLE references-clause under referential-constraint", 0, True
   Print #fileNo, addTab(3); "fk_col.colseq   = pk_col.colseq"

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(RTRIM(FK.TABSCHEMA) LIKE '"; g_allSchemaNamePattern; "')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(RTRIM(FK.TABSCHEMA) LIKE COALESCE(UCASE(tabSchemaPattern_in), '%'))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(FK.TABNAME LIKE COALESCE(UCASE(tabNamePattern_in), '%'))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(RTRIM(FK.REFTABSCHEMA) LIKE COALESCE(UCASE(refTabSchemaPattern_in), '%'))"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(FK.REFTABNAME LIKE COALESCE(UCASE(refTabNamePattern_in), '%'))"
 
   Print #fileNo, addTab(2); "GROUP BY"
   Print #fileNo, addTab(3); "FK.CONSTNAME,"
   Print #fileNo, addTab(3); "FK.TABSCHEMA,"
   Print #fileNo, addTab(3); "FK.TABNAME,"
   Print #fileNo, addTab(3); "FK.REFTABSCHEMA,"
   Print #fileNo, addTab(3); "FK.REFTABNAME,"
   Print #fileNo, addTab(3); "FK.DELETERULE,"
   Print #fileNo, addTab(3); "FK.UPDATERULE,"
   Print #fileNo, addTab(3); "tc.enforced,"
   Print #fileNo, addTab(3); "tc.enablequeryopt"

   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo,
   Print #fileNo, addTab(2); "DO"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(TS) || '.' || RTRIM(TN) || ' ADD CONSTRAINT ' || RTRIM(CN) || ' ' ||"
   Print #fileNo, addTab(10); "'FOREIGN KEY (' || RTRIM( l_fk_colname ) || ') ' ||"
   Print #fileNo, addTab(10); "'REFERENCES ' || RTRIM(RS) || '.' || RTRIM(RN) || ' (' || RTRIM( l_pk_colname ) || ') ' ||"
   Print #fileNo, addTab(2); "                 'ON DELETE ' || (CASE DR WHEN 'R' THEN 'RESTRICT' WHEN 'C' THEN 'CASCADE' WHEN 'N' THEN 'SET NULL' ELSE 'NO ACTION' END) || ' ' ||"
   Print #fileNo, addTab(2); "                 'ON UPDATE ' || (CASE UR WHEN 'R' THEN 'RESTRICT' ELSE 'NO ACTION' END) || ' ' ||"
   Print #fileNo, addTab(2); "                 CASE l_enforced WHEN 'N' THEN 'NOT ENFORCED' ELSE '' END || ' ' ||"
   Print #fileNo, addTab(2); "                 CASE l_enablequeryopt WHEN 'N' THEN 'DISABLE QUERY OPTIMIZATION' ELSE '' END"
   Print #fileNo, addTab(2); "                 ;"

   Print #fileNo,
   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); g_qualTabNameDisabledFks; " DFK"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "DFK.TABSCHEMA = TS"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "DFK.TABNAME = TN"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "DFK.FKNAME = CN"
   Print #fileNo, addTab(2); ";"

   Print #fileNo,
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); g_qualTabNameDisabledFks
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "FKNAME,"
   Print #fileNo, addTab(3); "TABSCHEMA,"
   Print #fileNo, addTab(3); "TABNAME,"
   Print #fileNo, addTab(3); "REFTABSCHEMA,"
   Print #fileNo, addTab(3); "REFTABNAME,"
   Print #fileNo, addTab(3); "TEXT"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "VALUES"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CN,"
   Print #fileNo, addTab(3); "TS,"
   Print #fileNo, addTab(3); "TN,"
   Print #fileNo, addTab(3); "RS,"
   Print #fileNo, addTab(3); "RN,"
   Print #fileNo, addTab(3); "v_stmntTxt"
   Print #fileNo, addTab(2); ");"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(TS) || '.' || RTRIM(TN) || ' DROP CONSTRAINT ' || RTRIM(CN);"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET fkCount_out = fkCount_out + 1;"

   genProcSectionHeader fileNo, "SMM wohl nicht erforderlich", 0, True
 '  Print #fileNo,
   Print #fileNo, addTab(3); "--COMMIT;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET failCount_out = failCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(3); "DELETE FROM"
   Print #fileNo, addTab(4); g_qualTabNameDisabledFks; " DFK"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "DFK.TABSCHEMA = TS"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DFK.TABNAME = TN"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DFK.FKNAME = CN"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "SMM wohl nicht erforderlich", 0, True
 '  Print #fileNo,
   Print #fileNo, addTab(3); "--COMMIT;"

   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit fileNo, qualProcedureNameFkDisable, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "'refTabSchemaPattern_in", "'refTabNamePattern_in", "fkCount_out", "failCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_returnStatus;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP enabling disabled foreign keys
   ' ####################################################################################################################

   Dim qualProcedureNameFkEnable As String
   qualProcedureNameFkEnable = genQualProcName(g_sectionIndexDbAdmin, spnFkEnable, ddlType)

   printSectionHeader "SP enabling disabled foreign keys", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameFkEnable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the table schemas that apply"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the table names that apply"
   genProcParm fileNo, "IN", "refTabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schemas of referred tables that apply"
   genProcParm fileNo, "IN", "refTabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the names of referred tables that apply"

   genProcParm fileNo, "OUT", "fkCount_out", "INTEGER", True, "number of foreign feys sucessfully enabled"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of foreign keys failed to enable"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "authFail", "42501"
   genCondDecl fileNo, "fkAlreadyEnabled", "42710"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "CLOB(70000)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_returnStatus", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR fkAlreadyEnabled"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42710;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR authFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42501;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter fileNo, qualProcedureNameFkEnable, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "'refTabSchemaPattern_in", "'refTabNamePattern_in", "fkCount_out", "failCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET fkCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "FOR fkLoop AS indCsr CURSOR WITH HOLD FOR"

   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "FKNAME    AS FK,"
   Print #fileNo, addTab(3); "TABSCHEMA AS TS,"
   Print #fileNo, addTab(3); "TABNAME   AS TN,"
   Print #fileNo, addTab(3); "TEXT"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDisabledFks
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "UCASE(RTRIM(TABSCHEMA)) LIKE COALESCE(UCASE(tabSchemaPattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(TABNAME)) LIKE COALESCE(UCASE(tabNamePattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(REFTABSCHEMA)) LIKE COALESCE(UCASE(refTabSchemaPattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "UCASE(RTRIM(REFTABNAME)) LIKE COALESCE(UCASE(refTabNamePattern_in), '%')"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE TEXT;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET fkCount_out = fkCount_out + 1;"
   Print #fileNo,

   Print #fileNo, addTab(3); "DELETE FROM"
   Print #fileNo, addTab(4); g_qualTabNameDisabledFks; " DFK"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "DFK.FKNAME = FK"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DFK.TABSCHEMA = TS"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DFK.TABNAME = TN"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET failCount_out = failCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"

   genSpLogProcExit fileNo, qualProcedureNameFkEnable, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "'refTabSchemaPattern_in", "'refTabNamePattern_in", "fkCount_out", "failCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_returnStatus;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Private Sub genDbAdminDdl9( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    SP temporarily disabling routines
   ' ####################################################################################################################
 
   Dim qualProcedureNameRtDisable As String
   qualProcedureNameRtDisable = genQualProcName(g_sectionIndexDbAdmin, spnRtDisable, ddlType)

   printSectionHeader "SP temporarily disabling routines", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameRtDisable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "routineSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schema(s) of routines to disable"
   genProcParm fileNo, "IN", "routineNamePattern_in", "VARCHAR(50)", True, "(optional) determines the name(s) of routines to disable"
   genProcParm fileNo, "IN", "refObjSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schema(s) of objects for which to disable dependent routines"
   genProcParm fileNo, "IN", "refObjNamePattern_in", "VARCHAR(50)", True, "(optional) determines the name(s) of objects for which to disable dependent routines"

   genProcParm fileNo, "OUT", "rtCount_out", "INTEGER", True, "number of routines disabled"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of routines failed to disable"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "authFail", "42501"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(256)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_returnStatus", "INTEGER", "0"
   genVarDecl fileNo, "v_lastRoutineSchema", g_dbtDbSchemaName, "''"
   genVarDecl fileNo, "v_lastRoutineSpecificName", "VARCHAR(50)", "''"
   genVarDecl fileNo, "v_lastRoutineType", "CHAR(1)", "''"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR authFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42501;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader fileNo, "temporary table for data pool infos"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.ROUTINEDEP"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "DRT.ROUTINESCHEMA       AS routineSchema,"
   Print #fileNo, addTab(3); "DRT.ROUTINENAME         AS routineName,"
   Print #fileNo, addTab(3); "DRT.ROUTINESPECIFICNAME AS routineSpecificName,"
   Print #fileNo, addTab(3); "DRT.ROUTINETYPE         AS routineType,"
   Print #fileNo, addTab(3); "DRT.CREATETIMESTAMP     AS routineCreateTime,"
   Print #fileNo, addTab(3); "DRD.OBJSCHEMA           AS depObjSchema,"
   Print #fileNo, addTab(3); "DRT.ROUTINESPECIFICNAME AS depObjSpecificName,"
   Print #fileNo, addTab(3); "DRD.OBJNAME             AS depObjName,"
   Print #fileNo, addTab(3); "DRD.OBJTYPE             AS depObjType,"
   Print #fileNo, addTab(3); "INTEGER(1)              AS depLevel"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameDisabledRts; " DRT"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameDisabledRtDep; " DRD"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "DRT.ROUTINESCHEMA = DRD.ROUTINESCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "DRT.ROUTINESPECIFICNAME = DRD.ROUTINESPECIFICNAME"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "DRT.ROUTINETYPE = DRD.ROUTINETYPE"

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "DEFINITION ONLY"
   genDdlForTempTableDeclTrailer fileNo, 1, True

   genSpLogProcEnter fileNo, qualProcedureNameRtDisable, ddlType, , "'routineSchemaPattern_in", "'routineNamePattern_in", "'refObjSchemaPattern_in", "'refObjNamePattern_in", "rtCount_out", "failCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET rtCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"

   genProcSectionHeader fileNo, "determine routine dependencies"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ROUTINEDEP"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "routineSchema,"
   Print #fileNo, addTab(2); "routineName,"
   Print #fileNo, addTab(2); "routineSpecificName,"
   Print #fileNo, addTab(2); "routineType,"
   Print #fileNo, addTab(2); "routineCreateTime,"
   Print #fileNo, addTab(2); "depObjSchema,"
   Print #fileNo, addTab(2); "depObjSpecificName,"
   Print #fileNo, addTab(2); "depObjName,"
   Print #fileNo, addTab(2); "depObjType,"
   Print #fileNo, addTab(2); "depLevel"
   Print #fileNo, addTab(1); ")"

   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "V_RoutineDep"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "routineSchema,"
   Print #fileNo, addTab(2); "routineSpecificName,"
   Print #fileNo, addTab(2); "depObjSchema,"
   Print #fileNo, addTab(2); "depObjName,"
   Print #fileNo, addTab(2); "depObjType"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "ROUTINESCHEMA,"
   Print #fileNo, addTab(3); "ROUTINENAME,"
   Print #fileNo, addTab(3); "BSCHEMA,"
   Print #fileNo, addTab(3); "BNAME,"
   Print #fileNo, addTab(3); "BTYPE"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.ROUTINEDEP"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "BTYPE IN ('F','T')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "BSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(1); "),"
   Print #fileNo, addTab(2); "V_RoutineDepClosure"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "routineSchema,"
   Print #fileNo, addTab(2); "routineSpecificName,"
   Print #fileNo, addTab(2); "depObjSchema,"
   Print #fileNo, addTab(2); "depObjName,"
   Print #fileNo, addTab(2); "depObjType,"
   Print #fileNo, addTab(2); "depLevel"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "routineSchema,"
   Print #fileNo, addTab(4); "routineSpecificName,"
   Print #fileNo, addTab(4); "depObjSchema,"
   Print #fileNo, addTab(4); "depObjName,"
   Print #fileNo, addTab(4); "depObjType,"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "V_RoutineDep"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "UNION ALL"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "C.routineSchema,"
   Print #fileNo, addTab(4); "C.routineSpecificName,"
   Print #fileNo, addTab(4); "D.depObjSchema,"
   Print #fileNo, addTab(4); "D.depObjName,"
   Print #fileNo, addTab(4); "D.depObjType,"
   Print #fileNo, addTab(4); "C.depLevel + 1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "V_RoutineDep D,"
   Print #fileNo, addTab(4); "V_RoutineDepClosure C"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "D.routineSchema = C.depObjSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "D.routineSpecificName = C.depObjName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.depLevel < 500"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); "),"
   Print #fileNo, addTab(2); "V_RoutineDepClosureResolved"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "routineSchema,"
   Print #fileNo, addTab(2); "routineName,"
   Print #fileNo, addTab(2); "routineSpecificName,"
   Print #fileNo, addTab(2); "routineType,"
   Print #fileNo, addTab(2); "routineCreateTime,"
   Print #fileNo, addTab(2); "depObjSchema,"
   Print #fileNo, addTab(2); "depObjSpecificName,"
   Print #fileNo, addTab(2); "depObjName,"
   Print #fileNo, addTab(2); "depObjType,"
   Print #fileNo, addTab(2); "depLevel"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "V.routineSchema,"
   Print #fileNo, addTab(3); "R.ROUTINENAME,"
   Print #fileNo, addTab(3); "V.routineSpecificName,"
   Print #fileNo, addTab(3); "R.ROUTINETYPE,"
   Print #fileNo, addTab(3); "R.CREATE_TIME,"
   Print #fileNo, addTab(3); "V.depObjSchema,"
   Print #fileNo, addTab(3); "V.depObjName,"
   Print #fileNo, addTab(3); "(CASE V.depObjType WHEN 'F' THEN RD.ROUTINENAME ELSE V.depObjName END),"
   Print #fileNo, addTab(3); "V.depObjType,"
   Print #fileNo, addTab(3); "V.depLevel"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_RoutineDepClosure V"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.ROUTINES R"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "V.routineSchema = R.ROUTINESCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "V.routineSpecificName = R.SPECIFICNAME"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "R.ROUTINETYPE IN ('F','P')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "R.LANGUAGE = 'SQL'"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.ROUTINES RD"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "V.depObjSchema = RD.ROUTINESCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "V.depObjName = RD.SPECIFICNAME"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "V.depObjType = 'F'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RD.ROUTINETYPE IN ('F','P')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RD.LANGUAGE = 'SQL'"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "routineSchema,"
   Print #fileNo, addTab(2); "routineName,"
   Print #fileNo, addTab(2); "routineSpecificName,"
   Print #fileNo, addTab(2); "routineType,"
   Print #fileNo, addTab(2); "routineCreateTime,"
   Print #fileNo, addTab(2); "depObjSchema,"
   Print #fileNo, addTab(2); "depObjSpecificName,"
   Print #fileNo, addTab(2); "depObjName,"
   Print #fileNo, addTab(2); "depObjType,"
   Print #fileNo, addTab(2); "depLevel"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_RoutineDepClosureResolved"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "routineSchema LIKE COALESCE(routineSchemaPattern_in, '%')"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "routineName LIKE COALESCE(routineNamePattern_in, '%')"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "depObjSchema LIKE COALESCE(refObjSchemaPattern_in, '%')"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "depObjName LIKE COALESCE(refObjNamePattern_in, '%')"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "disable each routine individually"
   Print #fileNo, addTab(1); "FOR routineLoop AS routineCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "D.routineSchema       AS c_routineSchema,"
   Print #fileNo, addTab(3); "D.routineName         AS c_routineName,"
   Print #fileNo, addTab(3); "D.routineSpecificName AS c_routineSpecificName,"
   Print #fileNo, addTab(3); "D.routineType         AS c_routineType,"
   Print #fileNo, addTab(3); "R.TEXT                AS c_routineText,"
   Print #fileNo, addTab(3); "D.routineCreateTime   AS c_routineCreateTime,"
   Print #fileNo, addTab(3); "D.depObjSchema        AS c_depObjSchema,"
   Print #fileNo, addTab(3); "D.depObjSpecificName  AS c_depObjSpecificName,"
   Print #fileNo, addTab(3); "D.depObjName          AS c_depObjName,"
   Print #fileNo, addTab(3); "D.depObjType          AS c_depObjType,"
   Print #fileNo, addTab(3); "D.depLevel            AS c_depLevel"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SESSION.ROUTINEDEP D"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.ROUTINES R"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "R.ROUTINESCHEMA = D.routineSchema"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "R.SPECIFICNAME = D.routineSpecificName"
   Print #fileNo, addTab(2); "AND"
   Print #fileNo, addTab(3); "R.ROUTINETYPE = D.routineType"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "D.routineCreateTime DESC,"
   Print #fileNo, addTab(3); "D.depLevel DESC"

   Print #fileNo, addTab(1); "DO"
 
   Print #fileNo, addTab(2); "IF (v_lastRoutineSchema       <> c_routineSchema      ) OR"
   Print #fileNo, addTab(2); "   (v_lastRoutineSpecificName <> c_routineSpecificName) OR"
   Print #fileNo, addTab(2); "   (v_lastRoutineType         <> c_routineType        ) THEN"
 
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_lastRoutineSchema       = c_routineSchema;"
   Print #fileNo, addTab(3); "SET v_lastRoutineSpecificName = c_routineSpecificName;"
   Print #fileNo, addTab(3); "SET v_lastRoutineType         = c_routineType;"
 
   genProcSectionHeader fileNo, "keep track of deleted routine", 3
   Print #fileNo, addTab(3); "DELETE FROM"
   Print #fileNo, addTab(4); g_qualTabNameDisabledRtDep; " DRT"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "DRT.ROUTINESCHEMA = c_routineSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DRT.ROUTINESPECIFICNAME = c_routineSpecificName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DRT.ROUTINETYPE = c_routineType"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "DELETE FROM"
   Print #fileNo, addTab(4); g_qualTabNameDisabledRts; " DRT"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "DRT.ROUTINESCHEMA = c_routineSchema"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DRT.ROUTINESPECIFICNAME = c_routineSpecificName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "DRT.ROUTINETYPE = c_routineType"
   Print #fileNo, addTab(3); ";"
 
   Print #fileNo,
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); g_qualTabNameDisabledRts
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "ROUTINESCHEMA,"
   Print #fileNo, addTab(4); "ROUTINENAME,"
   Print #fileNo, addTab(4); "ROUTINESPECIFICNAME,"
   Print #fileNo, addTab(4); "ROUTINETYPE,"
   Print #fileNo, addTab(4); g_anCreateTimestamp; ","
   Print #fileNo, addTab(4); "TEXT"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "c_routineSchema,"
   Print #fileNo, addTab(4); "c_routineName,"
   Print #fileNo, addTab(4); "c_routineSpecificName,"
   Print #fileNo, addTab(4); "c_routineType,"
   Print #fileNo, addTab(4); "c_routineCreateTime,"
   Print #fileNo, addTab(4); "c_routineText"
   Print #fileNo, addTab(3); ");"
 
   genProcSectionHeader fileNo, "delete routine", 3
   Print #fileNo, addTab(3); "IF c_routineType = 'P' THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = 'DROP SPECIFIC PROCEDURE ' || RTRIM(c_routineSchema) || '.' || RTRIM(c_routineSpecificName);"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_stmntTxt = 'DROP SPECIFIC FUNCTION ' || RTRIM(c_routineSchema) || '.' || RTRIM(c_routineSpecificName);"
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_failed = "; gc_dbFalse; ";"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(4); "SET rtCount_out = rtCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET failCount_out = failCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(4); "DELETE FROM"
   Print #fileNo, addTab(5); g_qualTabNameDisabledRtDep; " DRT"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "DRT.ROUTINESCHEMA = c_routineSchema"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "DRT.ROUTINESPECIFICNAME = c_routineSpecificName"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "DRT.ROUTINETYPE = c_routineType"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(4); "DELETE FROM"
   Print #fileNo, addTab(5); g_qualTabNameDisabledRts; " DRT"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "DRT.ROUTINESCHEMA = c_routineSchema"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "DRT.ROUTINESPECIFICNAME = c_routineSpecificName"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "DRT.ROUTINETYPE = c_routineType"
   Print #fileNo, addTab(4); ";"

   Print #fileNo,
   Print #fileNo, addTab(4); "COMMIT;"

   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "keep track of deleted routine dependency", 2
   Print #fileNo, addTab(2); "IF v_failed = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); g_qualTabNameDisabledRtDep
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "ROUTINESCHEMA,"
   Print #fileNo, addTab(4); "ROUTINESPECIFICNAME,"
   Print #fileNo, addTab(4); "ROUTINETYPE,"
   Print #fileNo, addTab(4); "OBJSCHEMA,"
   Print #fileNo, addTab(4); "OBJNAME,"
   Print #fileNo, addTab(4); "OBJTYPE"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "c_routineSchema,"
   Print #fileNo, addTab(4); "c_routineSpecificName,"
   Print #fileNo, addTab(4); "c_routineType,"
   Print #fileNo, addTab(4); "c_depObjSchema,"
   Print #fileNo, addTab(4); "c_depObjName,"
   Print #fileNo, addTab(4); "c_depObjType"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit fileNo, qualProcedureNameRtDisable, ddlType, , "'routineSchemaPattern_in", "'routineNamePattern_in", "'refObjSchemaPattern_in", "'refObjNamePattern_in", "rtCount_out", "failCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP altering a table definition (not supported via 'ALTER TABLE')
   ' ####################################################################################################################

   Dim qualProcedureNameAlterTable As String
   qualProcedureNameAlterTable = genQualProcName(g_sectionIndexDbAdmin, spnAlterTable, ddlType)

   printSectionHeader "SP altering a table definition", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameAlterTable
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "tabSchema_in", g_dbtDbSchemaName, True, "schema name of the table alter"
   genProcParm fileNo, "IN", "tabName_in", g_dbtDbTableName, True, "name of the table to alter"
   genProcParm fileNo, "IN", "tabDefinition_in", "VARCHAR(4096)", True, "table definition (SQL-code following 'CREATE TABLE <tab> ')"

   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records maintained in the table"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "tabNotExist", "42704"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "CLOB(8092)", "NULL"
   genVarDecl fileNo, "v_tabNameTmp", g_dbtDbTableName, "NULL"
   genVarDecl fileNo, "v_colList", "VARCHAR(2048)", "''"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR tabNotExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter fileNo, qualProcedureNameAlterTable, ddlType, , "'tabSchema_in", "'tabName_in", "'tabDefinition_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "create temporary table"
   Print #fileNo, addTab(1); "SET v_tabNameTmp = UPPER(tabSchema_in) || '.TMP_' || UPPER(tabName_in);"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'DROP TABLE ' || v_tabNameTmp;"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CREATE TABLE ' || v_tabNameTmp || ' LIKE ' || UPPER(tabSchema_in) || '.' || UPPER(tabName_in);"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "copy data to temporary table"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO ' || v_tabNameTmp || ' SELECT * FROM ' || UPPER(tabSchema_in) || '.' || UPPER(tabName_in);"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "determine set of common columns"
   Print #fileNo, addTab(1); "FOR colLoop AS colCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "COL_OLD.COLNAME AS c_colName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.COLUMNS COL_OLD,"
   Print #fileNo, addTab(3); "SYSCAT.COLUMNS COL_NEW"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "COL_OLD.TABSCHEMA = tabSchema_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COL_OLD.TABNAME = tabName_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COL_NEW.TABSCHEMA = tabSchema_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COL_NEW.TABNAME = 'TMP_' || tabName_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COL_NEW.COLNAME = COL_OLD.COLNAME"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "COL_OLD.COLNO"
   Print #fileNo, addTab(2); "FOR READ ONLY"

   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_colList = (CASE WHEN v_colList = '' THEN '' ELSE v_colList || ',' END ) || c_colName;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "drop table"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'DROP TABLE ' || UPPER(tabSchema_in) || '.' || UPPER(tabName_in);"
   Print #fileNo, addTab(1); "-- EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "re-create table"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CREATE TABLE ' || UPPER(tabSchema_in) || '.' || UPPER(tabName_in) || ' ' || tabDefinition_in;"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "copy data back"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO ' || UPPER(tabSchema_in) || '.' || UPPER(tabName_in) || '('|| v_colList || ') SELECT '|| v_colList || ' FROM ' || v_tabNameTmp;"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"

   genSpLogProcExit fileNo, qualProcedureNameAlterTable, ddlType, , "'tabSchema_in", "'tabName_in", "'tabDefinition_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP setting 'APPEND'-mode of tables
   ' ####################################################################################################################

   Dim qualProcedureNameTabAppend As String
   qualProcedureNameTabAppend = genQualProcName(g_sectionIndexDbAdmin, spnSetTabAppend, ddlType)

   printSectionHeader "SP setting 'APPEND'-mode of tables", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameTabAppend
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "tabSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) determines the schemas that apply"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "(optional) determines the table names that apply"
   genProcParm fileNo, "IN", "appendMode_in", "INTEGER", True, "determines whether APPEND-mode is ON (1) or OFF (0)"
 
   genProcParm fileNo, "OUT", "tabCount_out", "INTEGER", True, "number of tables sucessfully configured"
   genProcParm fileNo, "OUT", "failCount_out", "INTEGER", False, "number of tables failed to configure"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "authFail", "42501"
   genCondDecl fileNo, "invocationFail", "42509"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "CLOB(70000)", "NULL"
   genVarDecl fileNo, "v_failed", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_returnStatus", "INTEGER", "0"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR authFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42501;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR invocationFail"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_failed = "; gc_dbTrue; ";"
   Print #fileNo, addTab(2); "SET v_returnStatus = 42509;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   genSpLogProcEnter fileNo, qualProcedureNameTabAppend, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "appendMode_in", "tabCount_out", "failCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET tabCount_out  = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"
 
   genProcSectionHeader fileNo, "process each table individually"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "TABSCHEMA TS,"
   Print #fileNo, addTab(3); "TABNAME   TN"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SYSCAT.TABLES"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "TYPE = 'T'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(TABSCHEMA) LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "RTRIM(TABSCHEMA) LIKE COALESCE(UCASE(tabSchemaPattern_in), '%')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "TABNAME LIKE COALESCE(UCASE(tabNamePattern_in), '%')"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(TS) || '.' || RTRIM(TN) || ' APPEND ' || (CASE appendMode_in WHEN 1 THEN 'ON' ELSE 'OFF' END);"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET tabCount_out  = tabCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_returnStatus;"

   genSpLogProcEnter fileNo, qualProcedureNameTabAppend, ddlType, , "'tabSchemaPattern_in", "'tabNamePattern_in", "appendMode_in", "tabCount_out", "failCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for dropping all database objects except tables
   ' ####################################################################################################################

   Dim qualProcedureNameStrip As String
   qualProcedureNameStrip = genQualProcName(g_sectionIndexDbAdmin, spnDbStrip, ddlType)

   Dim unqualProcedureNameStrip As String
   unqualProcedureNameStrip = getUnqualObjName(qualProcedureNameStrip)
 
   printSectionHeader "SP for dropping all database objects except tables", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameStrip
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "degree_in", "INTEGER", True, "'0' - do NOT drop indexes and primary keys, '1' drop indexes and PKs, too"
   genProcParm fileNo, "OUT", "objCount_out", "INTEGER", False, "number of objects dropped"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "udfDependency", "42893"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_finished", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR udfDependency"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_finished = "; gc_dbFalse; ";"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempStatement fileNo, 1, True, 200

   genSpLogProcEnter fileNo, qualProcedureNameStrip, ddlType, , "mode_in", "degree_in", "objCount_out"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "WHILE v_finished = 0 DO"
   Print #fileNo, addTab(2); "SET v_finished = "; gc_dbTrue; ";"

   ' ### drop Trigger

   genProcSectionHeader fileNo, "drop Trigger", 2
   Print #fileNo, addTab(2); "FOR trigLoop AS csr CURSOR FOR"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "TRIGSCHEMA,"
   Print #fileNo, addTab(4); "TRIGNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TRIGGERS"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "TRIGSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(3); "FOR READ ONLY"

   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP TRIGGER ' || RTRIM(TRIGSCHEMA ) || '.' || TRIGNAME;"
   Print #fileNo,

   Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 4, True
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "SEQNO,"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "objCount_out,"
   Print #fileNo, addTab(5); "v_stmntTxt"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END FOR;"
 
   ' ### drop Views

   genProcSectionHeader fileNo, "drop Views", 2
   Print #fileNo, addTab(2); "FOR viewLoop AS csr CURSOR FOR"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "VIEWSCHEMA,"
   Print #fileNo, addTab(4); "VIEWNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.VIEWS"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "VIEWSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(3); "FOR READ ONLY"

   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP VIEW ' || RTRIM(VIEWSCHEMA ) || '.' || VIEWNAME;"
   Print #fileNo,

   Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 4, True
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "SEQNO,"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "objCount_out,"
   Print #fileNo, addTab(5); "v_stmntTxt"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END FOR;"
 
    ' ### drop Procedures

   genProcSectionHeader fileNo, "drop Procedures", 2
   Print #fileNo, addTab(2); "FOR procLoop AS csr CURSOR FOR"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "PROCSCHEMA,"
   Print #fileNo, addTab(4); "SPECIFICNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.PROCEDURES"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "PROCSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LANGUAGE = 'SQL'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "NOT ("
   Print #fileNo, addTab(5); "PROCSCHEMA = '"; g_schemaNameCtoDbAdmin; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "PROCNAME = '"; unqualProcedureNameStrip; "'"
   Print #fileNo, addTab(4); ")"

   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "CREATE_TIME DESC"
   Print #fileNo, addTab(3); "FOR READ ONLY"

   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP SPECIFIC PROCEDURE ' || RTRIM(PROCSCHEMA ) || '.' || SPECIFICNAME;"
   Print #fileNo,

   Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 4, True
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "SEQNO,"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "objCount_out,"
   Print #fileNo, addTab(5); "v_stmntTxt"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END FOR;"
 
   ' ### drop User Defined Functions

   genProcSectionHeader fileNo, "drop User Defined Functions", 2
   Print #fileNo, addTab(2); "FOR udfLoop AS csr CURSOR FOR"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "FUNCSCHEMA,"
   Print #fileNo, addTab(4); "SPECIFICNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.FUNCTIONS"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "FUNCSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "CREATE_TIME DESC"
   Print #fileNo, addTab(3); "FOR READ ONLY"

   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP SPECIFIC FUNCTION ' || RTRIM(FUNCSCHEMA ) || '.' || SPECIFICNAME;"
   Print #fileNo,

   Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 4, True
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "SEQNO,"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "objCount_out,"
   Print #fileNo, addTab(5); "v_stmntTxt"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END FOR;"
 
  ' ### drop Aliases

   genProcSectionHeader fileNo, "drop Aliases", 2
   Print #fileNo, addTab(2); "FOR aliasLoop AS csr CURSOR FOR"

   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "TABSCHEMA,"
   Print #fileNo, addTab(4); "TABNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.TABLES"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "TYPE = 'A'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "TABSCHEMA ASC,"
   Print #fileNo, addTab(4); "TABNAME ASC"
   Print #fileNo, addTab(3); "FOR READ ONLY"

   Print #fileNo, addTab(2); "DO"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP ALIAS ' || RTRIM(TABSCHEMA ) || '.' || TABNAME;"
   Print #fileNo,

   Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 4, True
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "SEQNO,"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "objCount_out,"
   Print #fileNo, addTab(5); "v_stmntTxt"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END FOR;"
 
   ' ### drop Indexes

   Print #fileNo,
   Print #fileNo, addTab(2); "IF degree_in > 0 THEN"
   genProcSectionHeader fileNo, "drop Indexes", 3, True

   Print #fileNo, addTab(3); "FOR indLoop AS csr CURSOR FOR"

   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "INDSCHEMA,"
   Print #fileNo, addTab(5); "INDNAME"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SYSCAT.INDEXES"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "INDSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "UNIQUERULE <> 'P'"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "INDSCHEMA ASC,"
   Print #fileNo, addTab(5); "INDNAME ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"

   Print #fileNo, addTab(3); "DO"

   Print #fileNo, addTab(4); "SET v_stmntTxt = 'DROP INDEX ' || RTRIM(INDSCHEMA ) || '.' || INDNAME;"
   Print #fileNo,

   Print #fileNo, addTab(4); "SET objCount_out = objCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(4); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 5, True
   Print #fileNo, addTab(5); "INSERT INTO"
   Print #fileNo, addTab(6); tempTabNameStatement
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "SEQNO,"
   Print #fileNo, addTab(6); "STATEMENT"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "VALUES"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "objCount_out,"
   Print #fileNo, addTab(6); "v_stmntTxt"
   Print #fileNo, addTab(5); ");"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(4); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(4); "END IF;"
 
   Print #fileNo, addTab(3); "END FOR;"

   ' ### drop Foreign Keys

   genProcSectionHeader fileNo, "drop Foreign Keys", 3
   Print #fileNo, addTab(3); "FOR fkLoop AS csr CURSOR FOR"

   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CONSTNAME,"
   Print #fileNo, addTab(5); "TABSCHEMA,"
   Print #fileNo, addTab(5); "TABNAME"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SYSCAT.TABCONST"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "TYPE = 'F'"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "TABSCHEMA ASC,"
   Print #fileNo, addTab(5); "TABNAME ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"

   Print #fileNo, addTab(3); "DO"

   Print #fileNo, addTab(4); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(TABSCHEMA ) || '.' || TABNAME || ' DROP FOREIGN KEY ' || CONSTNAME;"
   Print #fileNo,

   Print #fileNo, addTab(4); "SET objCount_out = objCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(4); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 5, True
   Print #fileNo, addTab(5); "INSERT INTO"
   Print #fileNo, addTab(6); tempTabNameStatement
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "SEQNO,"
   Print #fileNo, addTab(6); "STATEMENT"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "VALUES"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "objCount_out,"
   Print #fileNo, addTab(6); "v_stmntTxt"
   Print #fileNo, addTab(5); ");"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(4); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo, addTab(3); "END FOR;"

   ' ### drop Primary Keys

   genProcSectionHeader fileNo, "drop Primary Keys", 3

   Print #fileNo, addTab(3); "FOR pkLoop AS csr CURSOR FOR"

   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CONSTNAME,"
   Print #fileNo, addTab(5); "TABSCHEMA,"
   Print #fileNo, addTab(5); "TABNAME"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SYSCAT.TABCONST"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "TYPE = 'P'"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "TABSCHEMA ASC,"
   Print #fileNo, addTab(5); "TABNAME ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"

   Print #fileNo, addTab(3); "DO"

   Print #fileNo, addTab(4); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(TABSCHEMA ) || '.' || TABNAME || ' DROP PRIMARY KEY';"
   Print #fileNo,

   Print #fileNo, addTab(4); "SET objCount_out = objCount_out + 1;"

   Print #fileNo,
   Print #fileNo, addTab(4); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 5, True
   Print #fileNo, addTab(5); "INSERT INTO"
   Print #fileNo, addTab(6); tempTabNameStatement
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "SEQNO,"
   Print #fileNo, addTab(6); "STATEMENT"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "VALUES"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "objCount_out,"
   Print #fileNo, addTab(6); "v_stmntTxt"
   Print #fileNo, addTab(5); ");"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(4); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(4); "END IF;"

   Print #fileNo, addTab(3); "END FOR;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END WHILE;"

   ' ### return result

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit fileNo, qualProcedureNameStrip, ddlType, , "mode_in", "degree_in", "objCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Private Sub genDbAdminDdl10( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If
 
   ' ####################################################################################################################
   ' #    SP for selectively dropping database objects
   ' ####################################################################################################################
 
   Dim qualProcedureNameDropObjects As String
   qualProcedureNameDropObjects = genQualProcName(g_sectionIndexDbAdmin, spnDropObjects, ddlType)

   Dim unqualProcedureNameDropObjects As String
   unqualProcedureNameDropObjects = getUnqualObjName(qualProcedureNameDropObjects)
   Const tabNameSuffix = "Drop"

   Dim useExcludeList As Boolean
   Dim i As Integer
   For i = 1 To 2
     useExcludeList = (i = 2)

     printSectionHeader "SP for selectively dropping database objects", fileNo
 
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcedureNameDropObjects
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
     genProcParm fileNo, "IN", "objType_in", "VARCHAR(30)", True, "distinguishes between 'FUNCTION', 'PROCEDURE', 'TABLE', ..."
     genProcParm fileNo, "IN", "objTypeFilter_in", "VARCHAR(50)", True, "(optional) filter specific for 'objType_in', e.g. LANGUAGE-filter for 'PROCEDURE'"
     genProcParm fileNo, "IN", "objSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) - determines the object schemas that apply"
     genProcParm fileNo, "IN", "objNamePattern_in", "VARCHAR(128)", True, "(optional) - determines the object names that apply"
     If useExcludeList Then
       genProcParm fileNo, "IN", "objSchemaExclPatList_in", "VARCHAR(1024)", True, "(optional) - determines object schemas that are excluded"
       genProcParm fileNo, "IN", "objNameExclPatList_in", "VARCHAR(1024)", True, "(optional) - determines object names that are excluded"
     Else
       genProcParm fileNo, "IN", "objSchemaExclPattern_in", g_dbtDbSchemaName, True, "(optional) - determines object schemas that are excluded"
       genProcParm fileNo, "IN", "objNameExclPattern_in", "VARCHAR(128)", True, "(optional) - determines object names that are excluded"
     End If
     genProcParm fileNo, "IN", "parentObjSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) - determines the 'parent' object schemas that apply (e.g table schema of a foreign / primary key)"
     genProcParm fileNo, "IN", "parentObjNamePattern_in", "VARCHAR(128)", True, "(optional) - determines the 'parent' object names that apply"
     genProcParm fileNo, "IN", "escape_in", "CHAR(1)", True, "(optional) - 'escape'-character used in patterns"
     If useExcludeList Then
       genProcParm fileNo, "IN", "delim_in", "CHAR(1)", True, "(optional) - 'delimiter'-character used in exclude-lists (default '|')"
     End If
     genProcParm fileNo, "OUT", "objCount_out", "INTEGER", True, "number of objects dropped"
     genProcParm fileNo, "OUT", "objFailCount_out", "INTEGER", False, "number of objects failed to drop"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 1"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader fileNo, "declare conditions", , True
     genCondDecl fileNo, "objDependent", "42893"
     genCondDecl fileNo, "objInUse", "55006"
     If supportSpLogging Then
       genCondDecl fileNo, "implicitRebindFailed", "56098"
       genCondDecl fileNo, "routineDoesNotExist", "42884"
     End If

     genProcSectionHeader fileNo, "declare variables"
     genSigMsgVarDecl fileNo
     genVarDecl fileNo, "v_finished", g_dbtBoolean, gc_dbFalse
     genVarDecl fileNo, "v_objType", "VARCHAR(30)", "NULL"
     genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
     genVarDecl fileNo, "v_includeCommentCol", g_dbtBoolean, gc_dbFalse
     genVarDecl fileNo, "v_escape", "CHAR(1)", "'\'"
     genVarDecl fileNo, "v_dropFailed", g_dbtBoolean, "NULL"
     genVarDecl fileNo, "v_returnCode", "INTEGER", "0"
     genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
     genSpLogDecl fileNo

     genProcSectionHeader fileNo, "declare condition handler"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR objDependent"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "SET v_dropFailed = "; gc_dbTrue; ";"
     Print #fileNo, addTab(2); "SET v_returnCode = SQLCODE;"
     Print #fileNo, addTab(2); "SET objFailCount_out = objFailCount_out + 1;"
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR objInUse"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "SET v_dropFailed = "; gc_dbTrue; ";"
     Print #fileNo, addTab(2); "SET v_returnCode = SQLCODE;"
     Print #fileNo, addTab(2); "SET objFailCount_out = objFailCount_out + 1;"
     Print #fileNo, addTab(1); "END;"
     If supportSpLogging Then
       Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR implicitRebindFailed"
       Print #fileNo, addTab(1); "BEGIN"
       Print #fileNo, addTab(2); "-- just ignore (SQLLOG does not exist);"
       Print #fileNo, addTab(1); "END;"
       Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR routineDoesNotExist"
       Print #fileNo, addTab(1); "BEGIN"
       Print #fileNo, addTab(2); "-- just ignore (SPLOG_EXIT does not exist);"
       Print #fileNo, addTab(1); "END;"
     End If

     genDdlForTempStatement fileNo, 1, True, 200, , , , True, tabNameSuffix, , , True
     If useExcludeList Then
       Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
       Print #fileNo, addTab(2); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "schemaName "; g_dbtDbSchemaName
       Print #fileNo, addTab(1); ")"
       genDdlForTempTableDeclTrailer fileNo, 1, True
       Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
       Print #fileNo, addTab(2); pc_tempTabNameExcludeName
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(2); "name VARCHAR(128)"
       Print #fileNo, addTab(1); ")"
       genDdlForTempTableDeclTrailer fileNo, 1, True
     End If

     If useExcludeList Then
       genSpLogProcEnter fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objSchemaPattern_in", "'objNamePattern_in", _
         "'objSchemaExclPatList_in", "'objNameExclPatList_in", "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "escape_in", "delim_in", "objCount_out"
     Else
       genSpLogProcEnter fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objSchemaPattern_in", "'objNamePattern_in", _
         "'objSchemaExclPattern_in", "'objNameExclPattern_in", "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "escape_in", "objCount_out", "objFailCount_out"
     End If

     genProcSectionHeader fileNo, "initialize parameters and variables"
     Print #fileNo, addTab(1); "SET objCount_out     = 0;"
     Print #fileNo, addTab(1); "SET objFailCount_out = 0;"
     Print #fileNo, addTab(1); "SET v_objType        = UPPER(REPLACE(objType_in, ' ', ''));"
     Print #fileNo, addTab(1); "SET v_escape         = COALESCE(escape_in, v_escape);"

     ' ### drop Sequence

     Print #fileNo, addTab(1); "IF v_objType = 'SEQUENCE' THEN"

     If useExcludeList Then
       genProcSectionHeader fileNo, "determine sequences to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "SEQSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.SEQUENCES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "SEQSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "SEQNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.SEQUENCES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "SEQNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop Sequences", 2, Not useExcludeList
     Print #fileNo, addTab(2); "FOR seqLoop AS csr CURSOR FOR"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "SEQSCHEMA,"
     Print #fileNo, addTab(4); "SEQNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.SEQUENCES"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "SEQSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(SEQSCHEMA LIKE COALESCE(objSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(SEQNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeSchema; " ES"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "ES.schemaName = SEQSCHEMA"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = SEQNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objSchemaExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(SEQSCHEMA NOT LIKE objSchemaExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(SEQNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(SEQSCHEMA LIKE COALESCE(parentObjSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(SEQNAME LIKE COALESCE(parentObjNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP SEQUENCE ' || RTRIM(SEQSCHEMA ) || '.' || SEQNAME;"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "SEQNO,"
     Print #fileNo, addTab(5); "STATEMENT,"
     Print #fileNo, addTab(5); "FLAG"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END)"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     ' ### drop Trigger

     Print #fileNo, addTab(1); "ELSEIF v_objType = 'TRIGGER' THEN"
     If useExcludeList Then
       genProcSectionHeader fileNo, "determine triggers to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "TRIGSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TRIGGERS"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "TRIGSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "TRIGNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TRIGGERS"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "TRIGNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop Trigger", 2, Not useExcludeList
     Print #fileNo, addTab(2); "FOR trigLoop AS csr CURSOR FOR"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "TRIGSCHEMA,"
     Print #fileNo, addTab(4); "TRIGNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.TRIGGERS"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "TRIGSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TRIGSCHEMA LIKE COALESCE(objSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TRIGNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeSchema; " ES"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "ES.schemaName = TRIGSCHEMA"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = TRIGNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objSchemaExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(TRIGSCHEMA NOT LIKE objSchemaExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(TRIGNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABSCHEMA LIKE COALESCE(parentObjSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABNAME LIKE COALESCE(parentObjNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "TRIGSCHEMA ASC,"
     Print #fileNo, addTab(4); "TRIGNAME   ASC"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP TRIGGER ' || RTRIM(TRIGSCHEMA ) || '.' || TRIGNAME;"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "SEQNO,"
     Print #fileNo, addTab(5); "STATEMENT,"
     Print #fileNo, addTab(5); "FLAG"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END)"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     ' ### drop Views

     Print #fileNo, addTab(1); "ELSEIF v_objType = 'VIEW' THEN"
     If useExcludeList Then
       genProcSectionHeader fileNo, "determine views to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "VIEWSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.VIEWS"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "VIEWSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "VIEWNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.VIEWS"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "VIEWNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop Views", 2, Not useExcludeList
     Print #fileNo, addTab(2); "FOR viewLoop AS csr CURSOR FOR"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "VIEWSCHEMA,"
     Print #fileNo, addTab(4); "VIEWNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.VIEWS"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "VIEWSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(VIEWSCHEMA LIKE COALESCE(objSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(VIEWNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeSchema; " ES"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "ES.schemaName = VIEWSCHEMA"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = VIEWNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objSchemaExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(VIEWSCHEMA NOT LIKE objSchemaExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(VIEWNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "VIEWSCHEMA ASC,"
     Print #fileNo, addTab(4); "VIEWNAME   ASC"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP VIEW ' || RTRIM(VIEWSCHEMA ) || '.' || VIEWNAME;"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "SEQNO,"
     Print #fileNo, addTab(5); "STATEMENT,"
     Print #fileNo, addTab(5); "FLAG"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END)"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     ' ### drop Tables

     Print #fileNo, addTab(1); "ELSEIF v_objType = 'TABLE' THEN"
     If useExcludeList Then
       genProcSectionHeader fileNo, "determine tables to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "TABSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TABLES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "TYPE = 'T'"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "TABSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "TABNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TABLES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "TYPE = 'T'"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "TABNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop Tables", 2, Not useExcludeList
     Print #fileNo, addTab(2); "FOR tabLoop AS csr CURSOR FOR"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "TABSCHEMA,"
     Print #fileNo, addTab(4); "TABNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.TABLES"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TYPE = 'T'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABSCHEMA LIKE COALESCE(objSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeSchema; " ES"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "ES.schemaName = TABSCHEMA"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = TABNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objSchemaExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(TABSCHEMA NOT LIKE objSchemaExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(TABNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "TABSCHEMA ASC,"
     Print #fileNo, addTab(4); "TABNAME   ASC"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP TABLE ' || RTRIM(TABSCHEMA ) || '.' || TABNAME;"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "SEQNO,"
     Print #fileNo, addTab(5); "STATEMENT,"
     Print #fileNo, addTab(5); "FLAG"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END)"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     ' ### drop Procedures

     Print #fileNo, addTab(1); "ELSEIF v_objType = 'PROCEDURE' THEN"
     If useExcludeList Then
       genProcSectionHeader fileNo, "determine procedures to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "PROCSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.PROCEDURES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "PROCSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "PROCNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.PROCEDURES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "PROCNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop Procedures", 2, Not useExcludeList
     Print #fileNo, addTab(2); "FOR procLoop AS csr CURSOR FOR"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "PROCSCHEMA,"
     Print #fileNo, addTab(4); "PROCNAME,"
     Print #fileNo, addTab(4); "SPECIFICNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.PROCEDURES"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "PROCSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(PROCSCHEMA LIKE COALESCE(objSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(PROCNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeSchema; " ES"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "ES.schemaName = PROCSCHEMA"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = PROCNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objSchemaExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(PROCSCHEMA NOT LIKE objSchemaExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(PROCNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "RTRIM(LANGUAGE) LIKE COALESCE(objTypeFilter_in, 'SQL')"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "NOT ("
     Print #fileNo, addTab(5); "PROCSCHEMA = '"; g_schemaNameCtoDbAdmin; "'"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "PROCNAME = '"; unqualProcedureNameDropObjects; "'"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "PROCSCHEMA ASC,"
     Print #fileNo, addTab(4); "PROCNAME   ASC"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP SPECIFIC PROCEDURE ' || RTRIM(PROCSCHEMA ) || '.' || SPECIFICNAME;"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "seqNo,"
     Print #fileNo, addTab(5); "statement,"
     Print #fileNo, addTab(5); "flag,"
     Print #fileNo, addTab(5); "comment"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END),"
     Print #fileNo, addTab(5); "LEFT(RTRIM(PROCSCHEMA) || '.' || PROCNAME, 100)"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"
     Print #fileNo,
     Print #fileNo, addTab(2); "SET v_includeCommentCol = "; gc_dbTrue; ";"

     ' ### drop User Defined Functions
     Print #fileNo, addTab(1); "ELSEIF v_objType = 'FUNCTION' THEN"
     If useExcludeList Then
       genProcSectionHeader fileNo, "determine functions to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "FUNCSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.FUNCTIONS"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "FUNCSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "FUNCNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.FUNCTIONS"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "FUNCNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop User Defined Functions", 2, Not useExcludeList
     Print #fileNo, addTab(2); "FOR udfLoop AS csr CURSOR FOR"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "FUNCSCHEMA,"
     Print #fileNo, addTab(4); "FUNCNAME,"
     Print #fileNo, addTab(4); "SPECIFICNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.FUNCTIONS"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "FUNCSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(FUNCSCHEMA LIKE COALESCE(objSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(FUNCNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeSchema; " ES"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "ES.schemaName = FUNCSCHEMA"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = FUNCNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objSchemaExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(FUNCSCHEMA NOT LIKE objSchemaExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(FUNCNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "RTRIM(LANGUAGE) LIKE COALESCE(objTypeFilter_in, 'SQL')"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "CREATE_TIME DESC"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP SPECIFIC FUNCTION ' || RTRIM(FUNCSCHEMA ) || '.' || SPECIFICNAME;"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "seqNo,"
     Print #fileNo, addTab(5); "statement,"
     Print #fileNo, addTab(5); "flag,"
     Print #fileNo, addTab(5); "comment"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END),"
     Print #fileNo, addTab(5); "LEFT(RTRIM(FUNCSCHEMA) || '.' || FUNCNAME, 100)"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     Print #fileNo,
     Print #fileNo, addTab(2); "SET v_includeCommentCol = "; gc_dbTrue; ";"

    ' ### drop Aliases

     Print #fileNo, addTab(1); "ELSEIF v_objType = 'ALIAS' THEN"
     If useExcludeList Then
       genProcSectionHeader fileNo, "determine aliases to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "TABSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TABLES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "TYPE = 'A'"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "TABSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "TABNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TABLES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "TYPE = 'A'"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "TABNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop Aliases", 2, Not useExcludeList
     Print #fileNo, addTab(2); "FOR aliasLoop AS csr CURSOR FOR"
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "TABSCHEMA,"
     Print #fileNo, addTab(4); "TABNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.TABLES"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "TYPE = 'A'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABSCHEMA LIKE COALESCE(objSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeSchema; " ES"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "ES.schemaName = TABSCHEMA"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = TABNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objSchemaExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(TABSCHEMA NOT LIKE objSchemaExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(TABNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(BASE_TABSCHEMA LIKE COALESCE(parentObjSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(BASE_TABNAME LIKE COALESCE(parentObjNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "TABSCHEMA ASC,"
     Print #fileNo, addTab(4); "TABNAME ASC"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP ALIAS ' || RTRIM(TABSCHEMA ) || '.' || TABNAME;"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "SEQNO,"
     Print #fileNo, addTab(5); "STATEMENT,"
     Print #fileNo, addTab(5); "FLAG"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END)"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     ' ### drop Indexes

     Print #fileNo, addTab(1); "ELSEIF (v_objType = 'INDEX' OR v_objType = 'UNIQUE INDEX') THEN"
     If useExcludeList Then
       genProcSectionHeader fileNo, "determine indexes to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "INDSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.INDEXES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "INDSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "INDNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.INDEXES"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "INDNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop Indexes", 2, Not useExcludeList

     Print #fileNo, addTab(2); "FOR indLoop AS csr CURSOR FOR"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "INDSCHEMA,"
     Print #fileNo, addTab(4); "INDNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.INDEXES"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(INDSCHEMA LIKE COALESCE(objSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(INDNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeSchema; " ES"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "ES.schemaName = INDSCHEMA"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = INDNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objSchemaExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(INDSCHEMA NOT LIKE objSchemaExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(INDNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABSCHEMA LIKE COALESCE(parentObjSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABNAME LIKE COALESCE(parentObjNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "(v_objType = 'INDEX' AND UNIQUERULE <> 'P')"
     Print #fileNo, addTab(6); "OR"
     Print #fileNo, addTab(5); "(v_objType = 'UNIQUE INDEX' AND UNIQUERULE = 'U')"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "INDSCHEMA ASC,"
     Print #fileNo, addTab(4); "INDNAME ASC"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'DROP INDEX ' || RTRIM(INDSCHEMA ) || '.' || INDNAME;"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "SEQNO,"
     Print #fileNo, addTab(5); "STATEMENT,"
     Print #fileNo, addTab(5); "FLAG"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END)"
     Print #fileNo, addTab(5); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     ' ### drop Foreign Keys

     Print #fileNo, addTab(1); "ELSEIF v_objType = 'FOREIGNKEY' THEN"
     If useExcludeList Then
       genProcSectionHeader fileNo, "determine foreign key to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "TABSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TABCONST"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "TABSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "TABNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TABCONST"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "TABNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop Foreign Keys", 2, Not useExcludeList
     Print #fileNo, addTab(2); "FOR fkLoop AS csr CURSOR FOR"

     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "CONSTNAME,"
     Print #fileNo, addTab(4); "TABSCHEMA,"
     Print #fileNo, addTab(4); "TABNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.TABCONST"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(CONSTNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABSCHEMA LIKE COALESCE(parentObjSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABNAME LIKE COALESCE(parentObjNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeSchema; " ES"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "ES.schemaName = TABSCHEMA"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = TABNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objSchemaExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(TABSCHEMA NOT LIKE objSchemaExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(TABNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TYPE = 'F'"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "TABSCHEMA ASC,"
     Print #fileNo, addTab(4); "TABNAME ASC"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(TABSCHEMA ) || '.' || TABNAME || ' DROP FOREIGN KEY ' || CONSTNAME;"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "SEQNO,"
     Print #fileNo, addTab(5); "STATEMENT,"
     Print #fileNo, addTab(5); "FLAG"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END)"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"

     ' ### drop Primary Keys

     Print #fileNo, addTab(1); "ELSEIF v_objType = 'PRIMARYKEY' THEN"
     If useExcludeList Then
       genProcSectionHeader fileNo, "determine foreign key to exclude", 2, True
       Print #fileNo, addTab(2); "FOR schemaPatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objSchemaExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeSchema
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "schemaName"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "TABSCHEMA"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TABCONST"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "TABSCHEMA LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"
       Print #fileNo, addTab(2); "END FOR;"
       Print #fileNo,

       Print #fileNo, addTab(2); "FOR namePatternLoop AS"
       Print #fileNo, addTab(3); "SELECT"
       Print #fileNo, addTab(4); "ELEM AS c_elem"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(objNameExclPatList_in, CAST(COALESCE(delim_in, '|') AS CHAR(1))) ) AS X"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "ELEM IS NOT NULL"
       Print #fileNo, addTab(2); "DO"
 
       Print #fileNo, addTab(3); "INSERT INTO"
       Print #fileNo, addTab(4); pc_tempTabNameExcludeName
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "name"
       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(3); "SELECT DISTINCT"
       Print #fileNo, addTab(4); "CONSTNAME"
       Print #fileNo, addTab(3); "FROM"
       Print #fileNo, addTab(4); "SYSCAT.TABCONST"
       Print #fileNo, addTab(3); "WHERE"
       Print #fileNo, addTab(4); "CONSTNAME LIKE c_elem ESCAPE escape_in"
       Print #fileNo, addTab(3); ";"

       Print #fileNo, addTab(2); "END FOR;"
     End If

     genProcSectionHeader fileNo, "drop Primary Keys", 2, Not useExcludeList
     Print #fileNo, addTab(2); "FOR pkLoop AS csr CURSOR FOR"
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "CONSTNAME,"
     Print #fileNo, addTab(4); "TABSCHEMA,"
     Print #fileNo, addTab(4); "TABNAME"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); "SYSCAT.TABCONST"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "TABSCHEMA LIKE '"; g_allSchemaNamePattern; "'"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(CONSTNAME LIKE COALESCE(objNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     If useExcludeList Then
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); pc_tempTabNameExcludeName; " EN"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "EN.name = CONSTNAME"
       Print #fileNo, addTab(4); ")"
     Else
       Print #fileNo, addTab(4); "("
       Print #fileNo, addTab(5); "(objNameExclPattern_in IS NULL)"
       Print #fileNo, addTab(6); "OR"
       Print #fileNo, addTab(5); "(CONSTNAME NOT LIKE objNameExclPattern_in ESCAPE v_escape)"
       Print #fileNo, addTab(4); ")"
     End If
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABSCHEMA LIKE COALESCE(parentObjSchemaPattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(TABNAME LIKE COALESCE(parentObjNamePattern_in, '%') ESCAPE v_escape)"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "TYPE = 'P'"
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "TABSCHEMA ASC,"
     Print #fileNo, addTab(4); "TABNAME ASC"
     Print #fileNo, addTab(3); "FOR READ ONLY"

     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_dropFailed = "; gc_dbFalse; ";"
     Print #fileNo, addTab(3); "SET v_stmntTxt = 'ALTER TABLE ' || RTRIM(TABSCHEMA ) || '.' || TABNAME || ' DROP PRIMARY KEY';"
     Print #fileNo,

     Print #fileNo, addTab(3); "SET objCount_out = objCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
     genProcSectionHeader fileNo, "store statement in temporary table", 4, True
     Print #fileNo, addTab(4); "INSERT INTO"
     Print #fileNo, addTab(5); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "SEQNO,"
     Print #fileNo, addTab(5); "STATEMENT,"
     Print #fileNo, addTab(5); "FLAG"
     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(4); "VALUES"
     Print #fileNo, addTab(4); "("
     Print #fileNo, addTab(5); "objCount_out,"
     Print #fileNo, addTab(5); "v_stmntTxt,"
     Print #fileNo, addTab(5); "(CASE v_dropFailed WHEN 0 THEN '+' ELSE '-' END)"
     Print #fileNo, addTab(4); ");"
     Print #fileNo, addTab(3); "END IF;"

     Print #fileNo, addTab(2); "END FOR;"
     Print #fileNo, addTab(1); "ELSE"

     If useExcludeList Then
       genSpLogProcEscape fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objSchemaPattern_in", "'objNamePattern_in", _
         "'objSchemaExclPatList_in", "'objNameExclPatList_in", "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "escape_in", "delim_in", "objCount_out"
     Else
       genSpLogProcEscape fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objSchemaPattern_in", "'objNamePattern_in", _
         "'objSchemaExclPattern_in", "'objNameExclPattern_in", "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "escape_in", "objCount_out", "objFailCount_out"
     End If
     genSignalDdlWithParms "objTypeNotSupported", fileNo, 2, , , , , , , , , , "objType_in"

     Print #fileNo, addTab(1); "END IF;"

     ' ### return result

     genProcSectionHeader fileNo, "return result to application"
     Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
     Print #fileNo, addTab(2); "IF v_includeCommentCol = "; gc_dbFalse; " THEN"
     Print #fileNo, addTab(3); "BEGIN"

     Print #fileNo, addTab(4); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
     Print #fileNo, addTab(5); "SELECT"
     Print #fileNo, addTab(6); "CAST(FLAG AS CHAR(1)) AS F,"
     Print #fileNo, addTab(6); "CAST(RTRIM(LEFT(STATEMENT, 120)) AS VARCHAR(80)) AS STATEMENT"
     Print #fileNo, addTab(5); "FROM"
     Print #fileNo, addTab(6); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(5); "ORDER BY"
     Print #fileNo, addTab(6); "SEQNO ASC"
     Print #fileNo, addTab(5); "FOR READ ONLY"
     Print #fileNo, addTab(4); ";"

     genProcSectionHeader fileNo, "leave cursor open for application", 4
     Print #fileNo, addTab(4); "OPEN stmntCursor;"

     Print #fileNo, addTab(3); "END;"
     Print #fileNo, addTab(2); "ELSE"
     Print #fileNo, addTab(3); "BEGIN"

     Print #fileNo, addTab(4); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
     Print #fileNo, addTab(5); "SELECT"
     Print #fileNo, addTab(6); "CAST(FLAG AS CHAR(1)) AS F,"
     Print #fileNo, addTab(6); "CAST(RTRIM(LEFT(STATEMENT, 120)) AS VARCHAR(80)) AS STATEMENT,"
     Print #fileNo, addTab(6); "CAST(RTRIM(LEFT(COMMENT,    60)) AS VARCHAR(60)) AS COMMENT"
     Print #fileNo, addTab(5); "FROM"
     Print #fileNo, addTab(6); tempTabNameStatement; tabNameSuffix
     Print #fileNo, addTab(5); "ORDER BY"
     Print #fileNo, addTab(6); "SEQNO ASC"
     Print #fileNo, addTab(5); "FOR READ ONLY"
     Print #fileNo, addTab(4); ";"

     genProcSectionHeader fileNo, "leave cursor open for application", 4
     Print #fileNo, addTab(4); "OPEN stmntCursor;"

     Print #fileNo, addTab(3); "END;"

     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo, addTab(1); "END IF;"

     If useExcludeList Then
       genSpLogProcExit fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objSchemaPattern_in", "'objNamePattern_in", _
         "'objSchemaExclPatList_in", "'objNameExclPatList_in", "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "escape_in", "delim_in", "objCount_out"
     Else
       genSpLogProcExit fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objSchemaPattern_in", "'objNamePattern_in", _
         "'objSchemaExclPattern_in", "'objNameExclPattern_in", "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "escape_in", "objCount_out", "objFailCount_out"
     End If
     Print #fileNo,
     Print #fileNo, addTab(1); "RETURN v_returnCode;"
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i
 
   ' ####################################################################################################################

   printSectionHeader "SP for selectively dropping database objects", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameDropObjects
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "objType_in", "VARCHAR(30)", True, "distinguishes between 'FUNCTION', 'PROCEDURE', 'TABLE', ..."
   genProcParm fileNo, "IN", "objSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) - determines the object schemas that apply"
   genProcParm fileNo, "IN", "objNamePattern_in", "VARCHAR(128)", True, "(optional) - determines the object names that apply"
   genProcParm fileNo, "IN", "parentObjSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) - determines the 'parent' object schemas that apply (e.g table schema of a foreign / primary key)"
   genProcParm fileNo, "IN", "parentObjNamePattern_in", "VARCHAR(128)", True, "(optional) - determines the 'parent' object names that apply"
   genProcParm fileNo, "OUT", "objCount_out", "INTEGER", True, "number of objects dropped"
   genProcParm fileNo, "OUT", "objFailCount_out", "INTEGER", False, "number of objects failed to drop"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
   genSpLogDecl fileNo, -1, True
 
   If supportSpLogging Then
     genProcSectionHeader fileNo, "declare conditions", , True
     genCondDecl fileNo, "implicitRebindFailed", "56098"
     genCondDecl fileNo, "routineDoesNotExist", "42884"
 
     genProcSectionHeader fileNo, "declare condition handler"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR implicitRebindFailed"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore (SQLLOG does not exist);"
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR routineDoesNotExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore (SPLOG_EXIT does not exist);"
     Print #fileNo, addTab(1); "END;"
   End If

   genSpLogProcEnter fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objSchemaPattern_in", "'objNamePattern_in", _
     "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "objCount_out", "objFailCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameDropObjects; "(mode_in, objType_in, NULL, objSchemaPattern_in, objNamePattern_in, ";
   Print #fileNo, addTab(0); "NULL, NULL, parentObjSchemaPattern_in, parentObjNamePattern_in, '\', objCount_out, objFailCount_out);"
 
   genSpLogProcExit fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objSchemaPattern_in", "'objNamePattern_in", _
     "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "objCount_out", "objFailCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for selectively dropping database objects", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameDropObjects
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "objType_in", "VARCHAR(30)", True, "distinguishes between 'FUNCTION', 'PROCEDURE', 'TABLE', ..."
   genProcParm fileNo, "IN", "objTypeFilter_in", "VARCHAR(50)", True, "(optional) filter specific for 'objType_in', e.g. LANGUAGE-filter for 'PROCEDURE'"
   genProcParm fileNo, "IN", "objSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) - determines the object schemas that apply"
   genProcParm fileNo, "IN", "objNamePattern_in", "VARCHAR(128)", True, "(optional) - determines the object names that apply"
   genProcParm fileNo, "IN", "parentObjSchemaPattern_in", g_dbtDbSchemaName, True, "(optional) - determines the 'parent' object schemas that apply (e.g table schema of a foreign / primary key)"
   genProcParm fileNo, "IN", "parentObjNamePattern_in", "VARCHAR(128)", True, "(optional) - determines the 'parent' object names that apply"
   genProcParm fileNo, "OUT", "objCount_out", "INTEGER", True, "number of objects dropped"
   genProcParm fileNo, "OUT", "objFailCount_out", "INTEGER", False, "number of objects failed to drop"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
   genSpLogDecl fileNo, -1, True
 
   If supportSpLogging Then
     genProcSectionHeader fileNo, "declare conditions", , True
     genCondDecl fileNo, "implicitRebindFailed", "56098"
     genCondDecl fileNo, "routineDoesNotExist", "42884"
 
     genProcSectionHeader fileNo, "declare condition handler"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR implicitRebindFailed"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore (SQLLOG does not exist);"
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR routineDoesNotExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore (SPLOG_EXIT does not exist);"
     Print #fileNo, addTab(1); "END;"
   End If

   genSpLogProcEnter fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objTypeFilter_in", "'objSchemaPattern_in", "'objNamePattern_in", _
     "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "objCount_out", "objFailCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameDropObjects; "(mode_in, objType_in, objTypeFilter_in, objSchemaPattern_in, objNamePattern_in, ";
   Print #fileNo, addTab(0); "NULL, NULL, parentObjSchemaPattern_in, parentObjNamePattern_in, '\', objCount_out, objFailCount_out);"
 
   genSpLogProcExit fileNo, qualProcedureNameDropObjects, ddlType, , "mode_in", "'objType_in", "'objTypeFilter_in", "'objSchemaPattern_in", "'objNamePattern_in", _
     "'parentObjSchemaPattern_in", "'parentObjNamePattern_in", "objCount_out", "objFailCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 ' ### IF IVK ###
 Private Sub genDbAdminDdl11( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   ' ####################################################################################################################
   ' #    SP for cleaning up data inconsistencies
   ' ####################################################################################################################

   Dim qualProcedureNameDataCleanup As String
   qualProcedureNameDataCleanup = genQualProcName(g_sectionIndexDbAdmin, spnDataInconsCleanup, ddlType)
 
   printSectionHeader "SP for cleaning up data inconsistencies", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameDataCleanup
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "schemaNamePattern_in", g_dbtDbSchemaName, True, "determines the schemas of tables to cleanup"
   genProcParm fileNo, "IN", "tabNamePattern_in", g_dbtDbTableName, True, "determines the tables to cleanup"
   genProcParm fileNo, "OUT", "stmntCount_out", "INTEGER", True, "number of cleanup-statements"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows cleaned up"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(400)", "NULL"
   genSpLogDecl fileNo
 
   genDdlForTempStatement fileNo, 1, True, 400, True
 
   genSpLogProcEnter fileNo, qualProcedureNameDataCleanup, ddlType, , "mode_in", "'schemaNamePattern_in", "'tabNamePattern_in", _
     "stmntCount_out", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize output variables"
   Print #fileNo, addTab(1); "SET stmntCount_out  = 0;"
   Print #fileNo, addTab(1); "SET rowCount_out    = 0;"
 
   genProcSectionHeader fileNo, "cleanup 'dangling LRT-private records'"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "PPR."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "PPR."; g_anPdmTableName; " AS c_privTabName,"
   Print #fileNo, addTab(3); "PPU."; g_anPdmTableName; " AS c_pubTabName,"
   Print #fileNo, addTab(3); "PLR."; g_anPdmFkSchemaName; " AS c_lrtSchemaName,"
   Print #fileNo, addTab(3); "PLR."; g_anPdmTableName; " AS c_lrtTableName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LPR"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LPU"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "LPR."; g_anAcmEntitySection; " = LPU."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPR."; g_anAcmEntityName; " = LPU."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPR."; g_anAcmEntityType; " = LPU."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPR."; g_anLdmIsNl; " = LPU."; g_anLdmIsNl
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPR."; g_anLdmIsGen; " = LPU."; g_anLdmIsGen
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPR."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPU."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LPR."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PPR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PPR."; g_anPdmLdmFkSchemaName; " = LPR."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPR."; g_anPdmLdmFkTableName; " = LPR."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PPU"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PPU."; g_anPdmLdmFkSchemaName; " = LPU."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPU."; g_anPdmLdmFkTableName; " = LPU."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPU."; g_anOrganizationId; " = PPR."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PPU."; g_anPoolTypeId; " = PPR."; g_anPoolTypeId
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PLR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PLR."; g_anOrganizationId; " = PPR."; g_anOrganizationId
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PLR."; g_anPoolTypeId; " = PPR."; g_anPoolTypeId

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LLR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PLR."; g_anPdmLdmFkSchemaName; " = LLR."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PLR."; g_anPdmLdmFkTableName; " = LLR."; g_anLdmTableName

   Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "LLR."; g_anAcmEntitySection; " = '"; UCase(g_classes.descriptors(g_classIndexLrt).sectionName); "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "LLR."; g_anAcmEntityName; " = '"; UCase(g_classes.descriptors(g_classIndexLrt).className); "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "LLR."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "LLR."; g_anLdmIsNl; " = "; gc_dbFalse

   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "PPU."; g_anOrganizationId; ","
   Print #fileNo, addTab(3); "PPU."; g_anPoolTypeId
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_schemaName || '.' || c_privTabName || ' PRIV WHERE EXISTS (SELECT 1 FROM ' || c_lrtSchemaName || '.' || c_lrtTableName || ' LRT' || ' WHERE PRIV."; g_anInLrt; " = LRT."; g_anOid; " AND LRT."; g_anEndTime; " IS NOT NULL)';"
   Print #fileNo, addTab(2); "SET stmntCount_out = stmntCount_out + 1;"
 
   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "INSERT INTO SESSION.statements ( STATEMENT ) VALUES ( v_stmntTxt );"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
 
   genProcSectionHeader fileNo, "count the number of rows cleaned up", 3
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo,
   Print #fileNo, addTab(3); "COMMIT;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'UPDATE ' || c_schemaName || '.' || c_pubTabName || ' PUB SET PUB."; g_anInLrt; " = NULL WHERE EXISTS (SELECT 1 FROM ' || c_lrtSchemaName || '.' || c_lrtTableName || ' LRT' || ' WHERE PUB."; g_anInLrt; " = LRT."; g_anOid; " AND LRT."; g_anEndTime; " IS NOT NULL)';"
   Print #fileNo, addTab(2); "SET stmntCount_out = stmntCount_out + 1;"
 
   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "INSERT INTO SESSION.statements ( STATEMENT ) VALUES ( v_stmntTxt );"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
 
   genProcSectionHeader fileNo, "count the number of rows cleaned up", 3
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo,
   Print #fileNo, addTab(3); "COMMIT;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatement
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmntCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameDataCleanup, ddlType, , "mode_in", "'schemaNamePattern_in", "'tabNamePattern_in", _
     "stmntCount_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 Private Sub genDbAdminDdlOrgInitDupCode( _
     fileNo As Integer, _
     Optional ddlType As DdlTypeId = edtPdm)

   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     Exit Sub
   End If
 
   Dim thisMpcId As Integer
   Dim thisPoolId As Integer
   thisMpcId = g_primaryOrgIndex
   thisPoolId = g_workDataPoolIndex
 
   Dim qualTabNameMdsUser As String
   qualTabNameMdsUser = g_qualTabNameUser
 
   Dim qualTabNamePdmOrganizationEnum As String
   qualTabNamePdmOrganizationEnum = g_qualTabNamePdmOrganization
 
   Dim qualTabNameGenericCode As String
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisMpcId, thisPoolId, False, False)

   Dim qualTabNameGenericCodePriv As String
   qualTabNameGenericCodePriv = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisMpcId, thisPoolId, False, True)
 
   Dim qualTabNamecodevalidfororganization As String
   qualTabNamecodevalidfororganization = genQualTabNameByRelIndex(g_relIndexCodeValidForOrganization, ddlType, thisMpcId, thisPoolId)
 
   Dim qualTabNamecodevalidfororganizationPriv As String
   qualTabNamecodevalidfororganizationPriv = genQualTabNameByRelIndex(g_relIndexCodeValidForOrganization, ddlType, thisMpcId, thisPoolId, True)
 
   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, thisMpcId, thisPoolId)
 
   Dim qualTabNameLrt As String
   Dim qualTabNameLrtNlText As String
   Dim qualTabNameLdmLrt As String
   Dim classIndexLrt As Integer
   classIndexLrt = getClassIndexByName(clxnLrt, clnLrt)
 
   qualTabNameLrt = genQualTabNameByClassIndex(classIndexLrt, ddlType, thisMpcId, thisPoolId)
   qualTabNameLrtNlText = genQualNlTabNameByClassIndex(classIndexLrt, ddlType, thisMpcId, thisPoolId)
   qualTabNameLdmLrt = genQualTabNameByClassIndex(classIndexLrt, edtLdm, thisMpcId, thisPoolId)
 
   Dim qualTabNameProductStructure As String
   qualTabNameProductStructure = g_qualTabNameProductStructure
 
   Dim oidSeqName As String
   oidSeqName = genQualOidSeqNameForOrg(g_primaryOrgIndex, ddlType)

   Dim qualProcNameLrtBegin As String
   qualProcNameLrtBegin = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, g_primaryOrgIndex, g_workDataPoolIndex)
 
   Dim qualProcNameLrtLock_Genericcode As String
   qualProcNameLrtLock_Genericcode = genQualProcName(g_sectionIndexCode, spnLrtLock_Genericcode, ddlType, g_primaryOrgIndex, g_workDataPoolIndex)
 
   Dim qualProcNameLrtCommit As String
   qualProcNameLrtCommit = genQualProcName(g_sectionIndexAliasLrt, spnLrtCommit, ddlType, g_primaryOrgIndex, g_workDataPoolIndex)
 
 
   ' ####################################################################################################################
   ' #    SP for initialization of organization's meta data DUP-Code
   ' ####################################################################################################################
 
   Dim qualProcedureNameOrgInitDupCode As String
   qualProcedureNameOrgInitDupCode = genQualProcName(g_sectionIndexDbAdmin, spnOrgInitDupCode, ddlType)
   'qualProcedureNameOrgInitDupCode = genQualObjName(snDbAdmin, ssnDbAdmin, spnOrgInitDupCode, spsnOrgInitDupCode, ddlType, , , schemaNameInfix)
 
   printSectionHeader "SP for initialization of organization's meta data DUP-Code", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameOrgInitDupCode
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "orgId_in", "SMALLINT", True, "ID of the organization to initialize"
   genProcParm fileNo, "IN", "cdUserId_in", "VARCHAR( 16 )", True, "CD User Id of the mdsUser"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of Codes affected"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "routineNotFound", "42884"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_lrtOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_ignoreError", "SMALLINT", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR( 2000 )", "NULL"
   genVarDecl fileNo, "v_gwspError", "VARCHAR(  256 )", "NULL"
   genVarDecl fileNo, "v_gwspInfo", "VARCHAR( 1024 )", "NULL"
   genVarDecl fileNo, "v_gwspWarning", "VARCHAR(  512 )", "NULL"
   genVarDecl fileNo, "v_msg", "VARCHAR(   70 )", "NULL"
 
   genProcSectionHeader fileNo, "declare statements"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare condition handler for routine not found"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR routineNotFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "IF ( v_ignoreError = 0 ) THEN"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
   genProcSectionHeader fileNo, "declare continue handler for SQL-Exceptions"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"
 
   Dim tempTabNameStatementOrgInitDupCode As String
   tempTabNameStatementOrgInitDupCode = tempTabNameStatement & "OrgInitDupCode"
 
   genDdlForTempStatement fileNo, 1, True, 2000, True, True, True, , "OrgInitDupCode"
 
   genProcSectionHeader fileNo, "temporary table for Code-OIDs"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "session.CodeOids"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "oid           BIGINT,"
   Print #fileNo, addTab(2); "cdidiv_oid    BIGINT"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
 
   genProcSectionHeader fileNo, "this procedure is supported starting with DB2 V9.7"
   genProcSectionHeader fileNo, "in order to support deployability in earlier DB2-Releases, use dynamic SQL here and ignore error"
   Print #fileNo, addTab(1); "SET v_ignoreError = 1;"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL SYSPROC.WLM_SET_CLIENT_INFO( '''', '''', '''', NULL, NULL )';"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "SET v_ignoreError = 0;"
 
   genProcSectionHeader fileNo, "make sure we are not initializing a 'built-in' organization"
   Print #fileNo, addTab(1); "IF ( SELECT 1 FROM "; qualTabNamePdmOrganizationEnum; " WHERE id = orgId_in ) IS NULL THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitDupCode, ddlType, 2, "mode_in", "orgId_in", "'orgBusName_in", _
     "defaultCountryId_in", "primaryLanguageId_in", "fallbackLanguageId_in", "recordCount_out"
   genSignalDdlWithParms "orgIdNotValid", fileNo, 2, , , , , , , , , , "RTRIM( CHAR( orgId_in ) )"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(1); "IF orgId_in < 3 THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitDupCode, ddlType, 2, "mode_in", "orgId_in", "'orgBusName_in", _
     "defaultCountryId_in", "primaryLanguageId_in", "fallbackLanguageId_in", "recordCount_out"
   genSignalDdlWithParms "orgIdBuiltIn", fileNo, 2, , , , , , , , , , "RTRIM( CHAR( orgId_in ) )"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "make sure that CD-User is valid"
   Print #fileNo, addTab(1); "IF ( SELECT 1 FROM "; qualTabNameMdsUser; " WHERE CDUSERID = cdUserId_in ) IS NULL THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitDupCode, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "recordCount_out"
   genSignalDdlWithParms "userUnknown", fileNo, 2, , , , , , , , , , "cdUserId_in"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "for batch-script: set command options"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( 'UPDATE COMMAND OPTIONS USING S ON' );"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( 'UPDATE COMMAND OPTIONS USING V ON' );"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "determine Code-OIDs affected"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "session.CodeOids"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "oid,"
   Print #fileNo, addTab(2); "cdidiv_oid"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT DISTINCT"
   Print #fileNo, addTab(2); "gc.oid,"
   Print #fileNo, addTab(2); "gc.cdidiv_oid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCode; " AS gc"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "gc.ctytyp_oid = 140"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "gc.isdeleted = 0"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NOT EXISTS"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "1"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTabNamecodevalidfororganization; " AS cvfo"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "cvfo.gco_oid = gc.oid"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "cvfo.org_oid = ( SELECT orgoid FROM "; qualTabNamePdmOrganizationEnum; " WHERE id = orgId_in )"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader fileNo, "determine number of affected Codes"
   Print #fileNo, addTab(1); "SET recordCount_out = ( SELECT COUNT( * ) FROM session.CodeOids );"
 
   genProcSectionHeader fileNo, "if no Code is effected, there is nothing to do"
   Print #fileNo, addTab(1); "IF recordCount_out = 0 THEN"
   Print #fileNo, addTab(2); "SET v_msg = RTRIM( LEFT( '[MDS]: No codes to relate to organization', 70 ) );"
   Print #fileNo, addTab(2); "SIGNAL SQLSTATE '79999' SET MESSAGE_TEXT = v_msg;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "Loop over Devision"
   Print #fileNo, addTab(1); "FOR tableLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT pdidiv_oid AS c_divOid, "
   Print #fileNo, addTab(3); "MIN( oid ) AS c_psOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameProductStructure
   Print #fileNo, addTab(2); "GROUP BY pdidiv_oid"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(2); "DO"
   genProcSectionHeader fileNo, "determine Statement", 2
   genProcSectionHeader fileNo, "begin a new LRT", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameLrtBegin; "( ?, ?, ?, ?, ? )';"
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "BEGIN"
   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(4); "EXECUTE"
   Print #fileNo, addTab(5); "v_stmnt"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_lrtOid"
   Print #fileNo, addTab(4); "USING"
   Print #fileNo, addTab(5); "cdUserId_in,"
   Print #fileNo, addTab(5); "1,"
   Print #fileNo, addTab(5); "c_psOid,"
   Print #fileNo, addTab(5); "0"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(3); "END;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "lock codes", 2
   Print #fileNo, addTab(2); "FOR codeOidLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "oid AS c_Oid"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "session.CodeOids"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "cdidiv_oid = c_divOid"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; qualProcNameLrtLock_Genericcode; "( ?, ?, ?, ? )';"
   Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(4); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "BEGIN"
   Print #fileNo, addTab(5); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(5); "EXECUTE"
   Print #fileNo, addTab(6); "v_stmnt"
   Print #fileNo, addTab(5); "INTO"
   Print #fileNo, addTab(6); "v_rowCount"
   Print #fileNo, addTab(5); "USING"
   Print #fileNo, addTab(6); "v_lrtOid,"
   Print #fileNo, addTab(6); "c_psOid,"
   Print #fileNo, addTab(6); "c_Oid"
   Print #fileNo, addTab(5); ";"
   Print #fileNo, addTab(4); "END;"
 
   genProcSectionHeader fileNo, "make sure that this Code could be locked", 4
   Print #fileNo, addTab(4); "IF v_rowCount = 0 THEN"
   Print #fileNo, addTab(5); "ROLLBACK;"
   Print #fileNo, addTab(5); "SET v_msg = RTRIM( LEFT( '[MDS]: failed to lock Code(' || RTRIM( CHAR( c_Oid ) ) || ')',  70 ) );"
   Print #fileNo, addTab(5); "SIGNAL SQLSTATE '79999' SET MESSAGE_TEXT = v_msg;"
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"
 
   genProcSectionHeader fileNo, "create Code-Organization - relationships", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO ' ||"
   Print #fileNo, addTab(3); "'      "; qualTabNamecodevalidfororganizationPriv; " ' ||"
   Print #fileNo, addTab(3); "'( ' ||"
   Print #fileNo, addTab(3); "'  oid, ' ||"
   Print #fileNo, addTab(3); "'  inlrt, ' ||"
   Print #fileNo, addTab(3); "'  status_id, ' ||"
   Print #fileNo, addTab(3); "'  ahclassid, ' ||"
   Print #fileNo, addTab(3); "'  ahoid, ' ||"
   Print #fileNo, addTab(3); "'  lrtstate, ' ||"
   Print #fileNo, addTab(3); "'  createuser, ' ||"
   Print #fileNo, addTab(3); "'  createtimestamp, ' ||"
   Print #fileNo, addTab(3); "'  updateuser, ' ||"
   Print #fileNo, addTab(3); "'  lastupdatetimestamp, ' ||"
   Print #fileNo, addTab(3); "'  org_oid, ' ||"
   Print #fileNo, addTab(3); "'  gco_oid ' ||"
   Print #fileNo, addTab(3); "') ' ||"
   Print #fileNo, addTab(3); "'SELECT ' ||"
   Print #fileNo, addTab(3); "'  NEXTVAL FOR "; oidSeqName; ", ' ||"
   Print #fileNo, addTab(3); "'  ' || v_lrtOid || ', ' ||"
   Print #fileNo, addTab(3); "'  1, ' ||"
   Print #fileNo, addTab(3); "'  gc.ahclassid, ' ||"
   Print #fileNo, addTab(3); "'  gc.ahoid, ' ||"
   Print #fileNo, addTab(3); "'  1, ' ||"
   Print #fileNo, addTab(3); "'  ''' || cdUserId_in || ''', ' ||"
   Print #fileNo, addTab(3); "'  CURRENT TIMESTAMP, ' ||"
   Print #fileNo, addTab(3); "'  ''' || cdUserId_in || ''', ' ||"
   Print #fileNo, addTab(3); "'  CURRENT TIMESTAMP, ' ||"
   Print #fileNo, addTab(3); "'  ( SELECT orgoid FROM "; qualTabNamePdmOrganizationEnum; " WHERE id = ' || orgId_in  || ') AS org_oid, ' ||"
   Print #fileNo, addTab(3); "'  gc.oid ' ||"
   Print #fileNo, addTab(3); "'FROM ' ||"
   Print #fileNo, addTab(3); "'  "; qualTabNameGenericCode; " AS gc ' ||"
   Print #fileNo, addTab(3); "'INNER JOIN ' ||"
   Print #fileNo, addTab(3); "'  session.CodeOids AS co ' ||"
   Print #fileNo, addTab(3); "'ON ' ||"
   Print #fileNo, addTab(3); "'  gc.OID = co.OID ' ||"
   Print #fileNo, addTab(3); "'WHERE ' ||"
   Print #fileNo, addTab(3); "'  gc.cdidiv_oid = ' || c_divOid || ' '"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "update Codes (there are only 'affeced' Codes in this LRT)", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'UPDATE ' ||"
   Print #fileNo, addTab(3); "'      "; qualTabNameGenericCodePriv; " ' ||"
   Print #fileNo, addTab(3); "'    SET ' ||"
   Print #fileNo, addTab(3); "'      updateuser          = ''' || cdUserId_in || ''', ' ||"
   Print #fileNo, addTab(3); "'      lastupdatetimestamp = CURRENT TIMESTAMP, ' ||"
   Print #fileNo, addTab(3); "'      lrtstate            = 2, ' ||"
   Print #fileNo, addTab(3); "'      versionid           = versionid + 1 ' ||"
   Print #fileNo, addTab(3); "'    WHERE ' ||"
   Print #fileNo, addTab(3); "'      inlrt = ' || v_lrtOid || ' ' ||"
   Print #fileNo, addTab(3); "'        AND ' ||"
   Print #fileNo, addTab(3); "'      cdidiv_oid = ' || c_divOid || ' '"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "register that ACM-Relationship CodeValidForOrganization is affected by this LRT", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO ' ||"
   Print #fileNo, addTab(3); "'      "; qualTabNameLrtAffectedEntity; " ' ||"
   Print #fileNo, addTab(3); "'    ( ' ||"
   Print #fileNo, addTab(3); "'      lrtoid, ' ||"
   Print #fileNo, addTab(3); "'      orparententityid, ' ||"
   Print #fileNo, addTab(3); "'      entitytype, ' ||"
   Print #fileNo, addTab(3); "'      isgen, ' ||"
   Print #fileNo, addTab(3); "'      isnl, ' ||"
   Print #fileNo, addTab(3); "'      opid ' ||"
   Print #fileNo, addTab(3); "'    ) ' ||"
   Print #fileNo, addTab(3); "'    VALUES ' ||"
   Print #fileNo, addTab(3); "'    ( ' ||"
   Print #fileNo, addTab(3); "'      ' || v_lrtOid || ', ' ||"
   Print #fileNo, addTab(3); "'      ''05005'', ' ||"
   Print #fileNo, addTab(3); "'      ''R'', ' ||"
   Print #fileNo, addTab(3); "'      0, ' ||"
   Print #fileNo, addTab(3); "'      0, ' ||"
   Print #fileNo, addTab(3); "'      1 ' ||"
   Print #fileNo, addTab(3); "'    ) '"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "register that ACM-Relationship CodeValidForOrganization is affected by this LRT", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO ' ||"
   Print #fileNo, addTab(3); "'      "; qualTabNameLrtAffectedEntity; " ' ||"
   Print #fileNo, addTab(3); "'    ( ' ||"
   Print #fileNo, addTab(3); "'      lrtoid, ' ||"
   Print #fileNo, addTab(3); "'      orparententityid, ' ||"
   Print #fileNo, addTab(3); "'      entitytype, ' ||"
   Print #fileNo, addTab(3); "'      isgen, ' ||"
   Print #fileNo, addTab(3); "'      isnl, ' ||"
   Print #fileNo, addTab(3); "'      opid ' ||"
   Print #fileNo, addTab(3); "'    ) ' ||"
   Print #fileNo, addTab(3); "'    VALUES ' ||"
   Print #fileNo, addTab(3); "'    ( ' ||"
   Print #fileNo, addTab(3); "'      ' || v_lrtOid || ', ' ||"
   Print #fileNo, addTab(3); "'      ''05006'', ' ||"
   Print #fileNo, addTab(3); "'      ''C'', ' ||"
   Print #fileNo, addTab(3); "'      0, ' ||"
   Print #fileNo, addTab(3); "'      0, ' ||"
   Print #fileNo, addTab(3); "'      2 ' ||"
   Print #fileNo, addTab(3); "'    ) '"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "commit LRT", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO ' ||"
   Print #fileNo, addTab(3); "'      "; qualTabNameLrtNlText; " ' ||"
   Print #fileNo, addTab(3); "'    ( ' ||"
   Print #fileNo, addTab(3); "'      oid, ' ||"
   Print #fileNo, addTab(3); "'      lrt_oid, ' ||"
   Print #fileNo, addTab(3); "'      language_id, ' ||"
   Print #fileNo, addTab(3); "'      transactioncomment, ' ||"
   Print #fileNo, addTab(3); "'      ps_oid ' ||"
   Print #fileNo, addTab(3); "'    ) ' ||"
   Print #fileNo, addTab(3); "'    SELECT ' ||"
   Print #fileNo, addTab(3); "'      NEXTVAL FOR "; oidSeqName; ", ' ||"
   Print #fileNo, addTab(3); "'      lrt.OID, ' ||"
   Print #fileNo, addTab(3); "'      1, ' ||"
   Print #fileNo, addTab(3); "'      ''DUP-Code für MPC = ' || orgId_in || ' gültig gemacht.'', ' ||"
   Print #fileNo, addTab(3); "'      ' || c_psOid || ' ' ||"
   Print #fileNo, addTab(3); "'    FROM ' ||"
   Print #fileNo, addTab(3); "'      "; qualTabNameLrt; " AS lrt ' ||"
   Print #fileNo, addTab(3); "'    WHERE ' ||"
   Print #fileNo, addTab(3); "'      lrt.OID = ' || v_lrtOid || ' ' ||"
   Print #fileNo, addTab(3); "'        AND ' ||"
   Print #fileNo, addTab(3); "'      lrt.endtime IS NULL '"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "commit LRT", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameLrtCommit; "( ?, ?, ?, ?, ?, ? )';"
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitDupCode; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "BEGIN"
   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(4); "EXECUTE"
   Print #fileNo, addTab(5); "v_stmnt"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "v_rowCount,"
   Print #fileNo, addTab(5); "v_gwspError,"
   Print #fileNo, addTab(5); "v_gwspInfo,"
   Print #fileNo, addTab(5); "v_gwspWarning"
   Print #fileNo, addTab(4); "USING"
   Print #fileNo, addTab(5); "v_lrtOid,"
   Print #fileNo, addTab(5); "0"
   Print #fileNo, addTab(4); ";"
   Print #fileNo, addTab(3); "END;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmtCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementOrgInitDupCode
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmtCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameOrgInitDupCode
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "orgId_in", "SMALLINT", True, "ID of the organization to initialize"
   genProcParm fileNo, "IN", "cdUserId_in", "VARCHAR( 16 )", True, "CD User Id of the mdsUser"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of Codes affected"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameOrgInitDupCode; "( 2, orgId_in, cdUserId_in, recordCount_out );"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 End Sub
 
 Private Sub genDbAdminDdlOrgInitMetaBus( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     Exit Sub
   End If

   Dim qualTabNamePricePreferencesFac As String
   qualTabNamePricePreferencesFac = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)

   Dim qualProcNameLrtBeginFactory As String
   qualProcNameLrtBeginFactory = genQualProcName(g_sectionIndexLrt, spnLrtBegin, ddlType, g_primaryOrgIndex, g_workDataPoolIndex)

   Dim qualProcNameRegStaticInit As String
   qualProcNameRegStaticInit = genQualProcName(g_sectionIndexMeta, spnRegStaticInit, ddlType)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(-1, ddlType)

   ' ####################################################################################################################
   ' #    SP for initialization of organization's meta data
   ' ####################################################################################################################

   Dim qualProcedureNameOrgInitMeta As String
   qualProcedureNameOrgInitMeta = genQualProcName(g_sectionIndexDbAdmin, spnOrgInitMeta, ddlType)

   printSectionHeader "SP for initialization of organization's meta data", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameOrgInitMeta
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "ID of the organization to initialize"
   genProcParm fileNo, "IN", "orgMetaName_in", "VARCHAR(20)", True, "'meta-name' of the organization to initialize"
   genProcParm fileNo, "OUT", "orgOid_out", g_dbtOid, True, "OID assigned to this organization"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records INSERTED"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL"
   genVarDecl fileNo, "v_qualTabName", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSigMsgVarDecl fileNo
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare continue handler for SQL-Exceptions"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"

   Dim tempTabNameStatementOrgInitMeta As String
   tempTabNameStatementOrgInitMeta = tempTabNameStatement & "OrgInitMeta"

   genDdlForTempStatement fileNo, 1, True, 2000, True, True, True, , "OrgInitMeta"

   genSpLogProcEnter fileNo, qualProcedureNameOrgInitMeta, ddlType, , "mode_in", "orgId_in", "'orgMetaName_in", _
     "orgOid_out", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "make sure we are not initializing a 'built-in' organization"
   Print #fileNo, addTab(1); "IF orgId_in IS NULL THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitMeta, ddlType, 2, "mode_in", "orgId_in", "'orgMetaName_in", _
     "orgOid_out", "rowCount_out"
   genSignalDdlWithParms "orgIdNotValid", fileNo, 2, , , , , , , , , , "'NULL'"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(1); "IF orgId_in < 4 THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitMeta, ddlType, 2, "mode_in", "orgId_in", "'orgMetaName_in", _
     "orgOid_out", "rowCount_out"
   genSignalDdlWithParms "orgIdBuiltIn", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(orgId_in))"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "for batch-script: set command options"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('UPDATE COMMAND OPTIONS USING S ON');"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('UPDATE COMMAND OPTIONS USING X ON');"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('UPDATE COMMAND OPTIONS USING V ON');"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "cleanup meta data - if data exists"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_Tabs"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "seqNo,"
   Print #fileNo, addTab(3); "qualTabName,"
   Print #fileNo, addTab(3); "filterColName"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "VALUES(1, '"; g_qualTabNamePdmTable; "', '"; g_anOrganizationId; "')"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(2, '"; g_qualTabNamePdmSchema; "', '"; g_anOrganizationId; "')"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(3, '"; g_qualTabNamePdmPrimarySchema; "', '"; g_anOrganizationId; "')"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(4, '"; g_qualTabNamePdmOrganizationNl; "', 'REF_ID')"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "qualTabName AS c_QualTabName,"
   Print #fileNo, addTab(3); "filterColName AS c_filterColName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_Tabs"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "seqNo"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_qualTabName || ' WHERE ' || c_filterColName || ' = ' || RTRIM(CHAR(orgId_in));"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "determine OID of new Organization"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "MAX(OID) + "; CStr(gc_sequenceIncrementValue)
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "orgOid_out"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameOrganization
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anOid; "< 1"; gc_sequenceMinValue
   Print #fileNo, addTab(1); "WITH UR;"
   Print #fileNo, addTab(1); "SET orgOid_out = COALESCE(orgOid_out, NEXTVAL FOR "; qualSeqNameOid; ");"

   ' ############################################################

   genProcSectionHeader fileNo, "setup organization-specific data in """ & g_qualTabNamePdmOrganization & """"

   Print #fileNo, addTab(1); "SET v_stmntTxt = 'DELETE FROM "; g_qualTabNamePdmOrganization; " WHERE ID = ' || RTRIM(CHAR(orgId_in));"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNamePdmOrganization; " (' ||"
   Print #fileNo, addTab(2); "'ID,' ||"

   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 0, , True
   genTransformedAttrListForEntityWithColReuse g_enumIndexPdmOrganization, eactEnum, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   Dim k As Integer
   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(1); "') VALUES (' ||"
   Print #fileNo, addTab(2); "RTRIM(CHAR(orgId_in)) || ',' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 3, , True
   setAttributeMapping transformation, 1, conOrgOid, "RTRIM(CHAR(orgOid_out)) || ','"
   setAttributeMapping transformation, 2, conPdmSequenceSchemaName, "'''" & g_schemaNameCtoMeta & "' || RIGHT(DIGITS(orgId_in), 2) || ''''"
   setAttributeMapping transformation, 3, conVersionId, "'1'"
   genTransformedAttrListForEntityWithColReuse g_enumIndexPdmOrganization, eactEnum, transformation, tabColumns, fileNo, ddlType, , , 3, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"
   Next k
   Print #fileNo, addTab(1); "')';"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
 
   ' ############################################################

   genProcSectionHeader fileNo, "setup organization-specific data in """ & g_qualTabNamePdmOrganizationNl & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNamePdmOrganizationNl; " (' ||"
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 1, , True
   setAttributeMapping transformation, 1, conVersionId, ""
   genNlsTransformedAttrListForEntityWithColReUse g_enumIndexPdmOrganization, eactEnum, transformation, tabColumns, fileNo, , , ddlType, , , 2, , , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(1); "') SELECT ' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 5, , True
   setAttributeMapping transformation, 1, conOid, "'NEXTVAL FOR " & qualSeqNameOid & "'"
   setAttributeMapping transformation, 2, conEnumRefId, "RTRIM(CHAR(orgId_in))"
   setAttributeMapping transformation, 3, conEnumLabelText, "'''' || orgMetaName_in || ''''"
   setAttributeMapping transformation, 4, conLanguageId, "'S.ID'"
   setAttributeMapping transformation, 5, conVersionId, ""
   genNlsTransformedAttrListForEntityWithColReUse g_enumIndexPdmOrganization, eactEnum, transformation, tabColumns, fileNo, , , ddlType, , , 2, , , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
   Next k
   Print #fileNo, addTab(1); "' FROM "; getQualTabNameLanguageEnum(, , ddlType); " S';"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
 
   ' ############################################################

   genProcSectionHeader fileNo, "setup organization-specific data in """ & g_qualTabNamePdmPrimarySchema & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNamePdmPrimarySchema; " (' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 2, , True
   setAttributeMapping transformation, 1, conVersionId, ""
   setAttributeMapping transformation, 2, conIsReportingSchema, ""

   genTransformedAttrListForEntityWithColReuse g_classIndexPdmPrimarySchema, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(1); "') SELECT ' ||"
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 11, , True, , "'PS.", , , , , , , "'"
   setAttributeMapping transformation, 1, conVersionId, ""
   setAttributeMapping transformation, 2, conIsReportingSchema, ""
   setAttributeMapping transformation, 3, conPdmPrimSchemaName, "'LEFT(PS.SCHEMANAME, LENGTH(PS.SCHEMANAME)-3) || ''' || RIGHT(DIGITS(orgId_in), 2) || ''' || RIGHT(PS.SCHEMANAME, 1)'"
   setAttributeMapping transformation, 4, conPsDpFilteredSchemaName, "'LEFT(PS.PSDPFILTEREDSCHEMANAME, LENGTH(PS.PSDPFILTEREDSCHEMANAME)-3) || ''' || RIGHT(DIGITS(orgId_in), 2) || ''' || RIGHT(PS.PSDPFILTEREDSCHEMANAME, 1)'"
   setAttributeMapping transformation, 5, conPsDpFilteredSchemaNameSparte, "'LEFT(PS.PSDPFILTEREDSCHEMANAMESPARTE, LENGTH(PS.PSDPFILTEREDSCHEMANAMESPARTE)-3) || ''' || RIGHT(DIGITS(orgId_in), 2) || ''' || RIGHT(PS.PSDPFILTEREDSCHEMANAMESPARTE, 1)'"
   setAttributeMapping transformation, 6, conPdmDeletedObjectSchemaName, "'LEFT(PS.DELETEDOBJECTSCHEMANAME, LENGTH(PS.DELETEDOBJECTSCHEMANAME)-3) || ''' || RIGHT(DIGITS(orgId_in), 2) || ''' || RIGHT(PS.DELETEDOBJECTSCHEMANAME, 1)'"
   setAttributeMapping transformation, 7, conPdmNativeSchemaName, "'LEFT(PS." & g_anPdmNativeSchemaName & ", LENGTH(PS." & g_anPdmNativeSchemaName & ")-3) || ''' || RIGHT(DIGITS(orgId_in), 2) || ''' || RIGHT(PS." & g_anPdmNativeSchemaName & ", 1)'"
   setAttributeMapping transformation, 8, conPdmPrivateSchemaName, "'LEFT(PS.PRIVATESCHEMANAME, LENGTH(PS.PRIVATESCHEMANAME)-3) || ''' || RIGHT(DIGITS(orgId_in), 2) || ''' || RIGHT(PS.PRIVATESCHEMANAME, 1)'"
   setAttributeMapping transformation, 9, conPdmPublicSchemaName, "'LEFT(PS.PUBLICSCHEMANAME, LENGTH(PS.PUBLICSCHEMANAME)-3) || ''' || RIGHT(DIGITS(orgId_in), 2) || ''' || RIGHT(PS.PUBLICSCHEMANAME, 1)'"
   setAttributeMapping transformation, 10, conOrganizationId, "RIGHT(DIGITS(orgId_in), 2)"
   setAttributeMapping transformation, 11, conPoolTypeId, "'PS." & g_anPoolTypeId & "'"

   genTransformedAttrListForEntityWithColReuse g_classIndexPdmPrimarySchema, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
   Next k
   Print #fileNo, addTab(1); "' FROM ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNamePdmPrimarySchema; " PS' ||"
   Print #fileNo, addTab(1); "' INNER JOIN ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNamePdmOrganization; " PO' ||"
   Print #fileNo, addTab(1); "' ON ' ||"
   Print #fileNo, addTab(2); "'PS."; g_anOrganizationId; " = PO.ID' ||"
   Print #fileNo, addTab(1); "' WHERE ' ||"
   Print #fileNo, addTab(2); "'PO.ID = (SELECT MIN(ID) FROM "; g_qualTabNamePdmOrganization; " WHERE ID <> "; CStr(g_primaryOrgId); ")'"
   Print #fileNo, addTab(1); ";"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
 
   ' ############################################################

   genProcSectionHeader fileNo, "setup organization-specific data in """ & g_qualTabNamePdmSchema & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNamePdmSchema; " (' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 1, , True
   setAttributeMapping transformation, 1, conVersionId, ""

   genTransformedAttrListForEntityWithColReuse g_classIndexPdmSchema, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(1); "') SELECT ' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 3, , True, , "'PS.", , , , , , , "'"
   setAttributeMapping transformation, 1, conVersionId, ""
   setAttributeMapping transformation, 2, conPdmPrimSchemaName, "'(CASE WHEN " & g_qualFuncNameIsNumeric & "(RIGHT(PS.SCHEMANAME,3)) = 1 " & _
                                                               "THEN LEFT(PS.SCHEMANAME, LENGTH(PS.SCHEMANAME)-3) || RIGHT(DIGITS(' || RTRIM(CHAR(orgId_in)) || '), 2) || RIGHT(PS.SCHEMANAME, 1) " & _
                                                               "WHEN " & g_qualFuncNameIsNumeric & "(RIGHT(PS.SCHEMANAME,2)) = 1 " & _
                                                               "THEN LEFT(PS.SCHEMANAME, LENGTH(PS.SCHEMANAME)-2) || RIGHT(DIGITS(' || RTRIM(CHAR(orgId_in)) || '), 2) " & _
                                                         "END)'"
   setAttributeMapping transformation, 3, conOrganizationId, "RTRIM(CHAR(orgId_in))"

   genTransformedAttrListForEntityWithColReuse g_classIndexPdmSchema, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
   Next k
   Print #fileNo, addTab(1); "' FROM ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNamePdmSchema; " PS' ||"
   Print #fileNo, addTab(1); "' INNER JOIN ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNamePdmOrganization; " PO' ||"
   Print #fileNo, addTab(1); "' ON ' ||"
   Print #fileNo, addTab(2); "'PS."; g_anOrganizationId; " = PO.ID' ||"
   Print #fileNo, addTab(1); "' WHERE ' ||"
   Print #fileNo, addTab(2); "'PO.ID = (SELECT MIN(ID) FROM "; g_qualTabNamePdmOrganization; " WHERE ID <> "; CStr(g_primaryOrgId); ")'"
   Print #fileNo, addTab(1); ";"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
 
   ' ############################################################

   genProcSectionHeader fileNo, "setup organization-specific data in """ & g_qualTabNamePdmTable & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNamePdmTable; " (' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 1, , True
   setAttributeMapping transformation, 1, conVersionId, ""

   genTransformedAttrListForEntityWithColReuse g_classIndexPdmTable, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(1); "') SELECT ' ||"
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 3, , True, , "'PT.", , , , , , , "'"
   setAttributeMapping transformation, 1, conVersionId, ""
   setAttributeMapping transformation, 2, conPdmFkSchemaName, "'(CASE WHEN " & g_qualFuncNameIsNumeric & "(RIGHT(PT." & g_anPdmFkSchemaName & ",3)) = 1 " & _
                                                               "THEN LEFT(PT." & g_anPdmFkSchemaName & ", LENGTH(PT." & g_anPdmFkSchemaName & ")-3) || RIGHT(DIGITS(' || RTRIM(CHAR(orgId_in)) || '), 2) || RIGHT(PT." & g_anPdmFkSchemaName & ", 1) " & _
                                                               "WHEN " & g_qualFuncNameIsNumeric & "(RIGHT(PT." & g_anPdmFkSchemaName & ",2)) = 1 " & _
                                                               "THEN LEFT(PT." & g_anPdmFkSchemaName & ", LENGTH(PT." & g_anPdmFkSchemaName & ")-2) || RIGHT(DIGITS(' || RTRIM(CHAR(orgId_in)) || '), 2) " & _
                                                            "END)'"
   setAttributeMapping transformation, 3, conOrganizationId, "RTRIM(CHAR(orgId_in))"

   genTransformedAttrListForEntityWithColReuse g_classIndexPdmTable, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
   Next k
   Print #fileNo, addTab(1); "' FROM ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNamePdmTable; " PT' ||"
   Print #fileNo, addTab(1); "' INNER JOIN ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNamePdmOrganization; " PO' ||"
   Print #fileNo, addTab(1); "' ON ' ||"
   Print #fileNo, addTab(2); "'PT."; g_anOrganizationId; " = PO.ID' ||"
   Print #fileNo, addTab(1); "' WHERE ' ||"
   Print #fileNo, addTab(2); "'PO.ID = (SELECT MIN(ID) FROM "; g_qualTabNamePdmOrganization; " WHERE ID <> "; CStr(g_primaryOrgId); ")'"
   Print #fileNo, addTab(1); ";"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "verify DDL-deployment for this organization"
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " || '.' || P."; g_anPdmTableName
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_qualTabName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); "SYSCAT.TABLES T"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " = T.TABSCHEMA"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " = T.TABNAME"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = orgId_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "T.TABNAME IS NULL"
   Print #fileNo, addTab(2); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(2); "WITH UR;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_qualTabName IS NOT NULL THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitMeta, ddlType, 3, "mode_in", "orgId_in", "'orgMetaName_in", _
     "orgOid_out", "rowCount_out"
   genSignalDdlWithParms "pdmTabNotExist", fileNo, 3, , , , , , , , , , "v_qualTabName"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"
 
   ' ############################################################

   genProcSectionHeader fileNo, "create LRT-related ALIASes for organization"

   Dim qualProcNameCreateLrtAliases As String
   qualProcNameCreateLrtAliases = genQualProcName(g_sectionIndexDbAdmin, spnCreateLrtAliases, ddlType)

   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameCreateLrtAliases; "(2, ' || RTRIM(CHAR(orgId_in)) || ', NULL, ?, ?)';"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   genProcSectionHeader fileNo, "declare variables", 3, True
   genVarDecl fileNo, "v_aliasCount", "INTEGER", "NULL", 3
   genVarDecl fileNo, "v_viewCount", "INTEGER", "NULL", 3

   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_aliasCount,"
   Print #fileNo, addTab(4); "v_viewCount"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   ' ############################################################
   genProcSectionHeader fileNo, "set GRANTs on new DB-objects"
   Dim qualProcNameSetGrants As String
   qualProcNameSetGrants = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "Fltr", eondmNone)
 
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameSetGrants; "(2, ''"; g_allSchemaNamePattern; "' || RIGHT(DIGITS(orgId_in),2) || '%'', NULL, ?)';"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   genProcSectionHeader fileNo, "declare statement", 3, True
   genVarDecl fileNo, "v_stmnt", "STATEMENT", , 3

   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   ' ############################################################

   If supportRangePartitioningByPsOid Then
     Dim qualProcedureNameAddTablePartitionByPs As String
     qualProcedureNameAddTablePartitionByPs = genQualProcName(g_sectionIndexDbAdmin, spnAddTablePartitionByPs, ddlType)

     genProcSectionHeader fileNo, "add range partitions for PS-tagged tables in new organization"

     Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameAddTablePartitionByPs; "(2, ?)';"

     Print #fileNo,
     Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
     If genTimeStampsDuringOrgInit Then
       Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
     End If
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo,
     Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(2); "BEGIN"
     genProcSectionHeader fileNo, "declare variables", 3, True
     genVarDecl fileNo, "v_partitionCount", "INTEGER", "NULL", 3

     Print #fileNo,
     Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(3); "EXECUTE"
     Print #fileNo, addTab(4); "v_stmnt"
     Print #fileNo, addTab(3); "INTO"
     Print #fileNo, addTab(4); "v_partitionCount"
     Print #fileNo, addTab(3); ";"

     Print #fileNo, addTab(2); "END;"
     Print #fileNo, addTab(1); "END IF;"
   End If

   ' ############################################################

   If supportRangePartitioningByDivOid Then
     Dim qualProcedureNameAddTablePartitionByDiv As String
     qualProcedureNameAddTablePartitionByDiv = genQualProcName(g_sectionIndexDbAdmin, spnAddTablePartitionByDiv, ddlType)

     genProcSectionHeader fileNo, "add range partitions for Division-tagged tables in new organization"

     Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameAddTablePartitionByDiv; "(2, ?)';"

     Print #fileNo,
     Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES (v_stmntTxt);"
     If genTimeStampsDuringOrgInit Then
       Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitMeta; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
     End If
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo,
     Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
     Print #fileNo, addTab(2); "BEGIN"
     genProcSectionHeader fileNo, "declare variables", 3, True
     genVarDecl fileNo, "v_partitionCount", "INTEGER", "NULL", 3

     Print #fileNo,
     Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(3); "EXECUTE"
     Print #fileNo, addTab(4); "v_stmnt"
     Print #fileNo, addTab(3); "INTO"
     Print #fileNo, addTab(4); "v_partitionCount"
     Print #fileNo, addTab(3); ";"

     Print #fileNo, addTab(2); "END;"
     Print #fileNo, addTab(1); "END IF;"
   End If

   ' ############################################################

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmtCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementOrgInitMeta
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmtCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameOrgInitMeta, ddlType, , "mode_in", "orgId_in", "'orgMetaName_in", _
     "orgOid_out", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for initialization of organization's business data
   ' ####################################################################################################################

   Dim qualProcedureNameOrgInitBus As String
   qualProcedureNameOrgInitBus = genQualProcName(g_sectionIndexDbAdmin, spnOrgInitBus, ddlType)

   printSectionHeader "SP for initialization of organization's business data", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameOrgInitBus
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "ID of the organization to initialize"
   genProcParm fileNo, "IN", "orgBusName_in", "VARCHAR(255)", True, "business Name of the organization to initialize"
   genProcParm fileNo, "IN", "defaultCountryId_in", g_dbtEnumId, True, "ID of the organization's default country"
   genProcParm fileNo, "IN", "primaryLanguageId_in", g_dbtEnumId, True, "ID of the organization's primary language"
   genProcParm fileNo, "IN", "fallbackLanguageId_in", g_dbtEnumId, True, "ID of the organization's fallback language"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records INSERTED / UPDATED"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL"
   genVarDecl fileNo, "v_defaultCountryOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_orgOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSigMsgVarDecl fileNo
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare continue handler for SQL-Exceptions"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcEnter fileNo, qualProcedureNameOrgInitBus, ddlType, , "mode_in", "orgId_in", "'orgBusName_in", _
     "defaultCountryId_in", "primaryLanguageId_in", "fallbackLanguageId_in", "rowCount_out"
 
   Dim tempTabNameStatementOrgInitBus As String
   tempTabNameStatementOrgInitBus = tempTabNameStatement & "OrgInitBus"

   genDdlForTempStatement fileNo, 1, True, 2000, True, True, True, , "OrgInitBus"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "make sure we are not initializing a 'built-in' organization"
   Print #fileNo, addTab(1); "IF orgId_in IS NULL THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitBus, ddlType, 2, "mode_in", "orgId_in", "'orgBusName_in", _
     "defaultCountryId_in", "primaryLanguageId_in", "fallbackLanguageId_in", "rowCount_out"
   genSignalDdlWithParms "orgIdNotValid", fileNo, 2, , , , , , , , , , "'NULL'"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(1); "IF orgId_in < 4 THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitBus, ddlType, 2, "mode_in", "orgId_in", "'orgBusName_in", _
     "defaultCountryId_in", "primaryLanguageId_in", "fallbackLanguageId_in", "rowCount_out"
   genSignalDdlWithParms "orgIdBuiltIn", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(orgId_in))"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "make sure that business name of the organization is new"
   Print #fileNo, addTab(1); "IF ( SELECT 1 FROM "; g_qualTabNameOrganization; " WHERE name = orgBusName_in ) IS NOT NULL THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitBus, ddlType, 2, "mode_in", "orgId_in", "'orgBusName_in", _
     "defaultCountryId_in", "primaryLanguageId_in", "fallbackLanguageId_in", "rowCount_out"
   genSignalDdlWithParms "orgBusNameIn", fileNo, 2, , , , , , , , , , "RTRIM( orgBusName_in )"
   Print #fileNo, addTab(1); "END IF;"
 
   Dim qualProcNameAssertRebateDefault As String
   qualProcNameAssertRebateDefault = genQualProcName(g_sectionIndexMeta, spnAssertRebateDefault, ddlType)

   genProcSectionHeader fileNo, "make sure that each ProductStructure has a default rebate (for type)"
   Print #fileNo, addTab(1); "FOR psLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); g_anOid; " AS c_psOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameProductStructure
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anIsUnderConstruction; " = "; gc_dbFalse
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "CALL "; qualProcNameAssertRebateDefault; "(c_psOid, 0);"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "determine OID of new Organization"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ORGOID"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_orgOid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNamePdmOrganization
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "ID = orgId_in"
   Print #fileNo, addTab(1); "WITH UR;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_orgOid IS NULL THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitBus, ddlType, 2, "mode_in", "orgId_in", "'orgBusName_in", _
     "defaultCountryId_in", "primaryLanguageId_in", "fallbackLanguageId_in", "rowCount_out"
   genSignalDdlWithParms "orgIdNotValid", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(orgId_in))"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "determine OID of the default country"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "C."; g_anOid
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_defaultCountryOid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); genQualTabNameByClassIndex(g_classIndexCountrySpec, ddlType); " C"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "C.ID = defaultCountryId_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "C."; g_anCid; " = '"; getClassIdStrByIndex(g_classIndexCountry); "'"
   Print #fileNo, addTab(1); "WITH UR;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_defaultCountryOid IS NULL THEN"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInitBus, ddlType, 2, "mode_in", "orgId_in", "'orgBusName_in", _
     "defaultCountryId_in", "primaryLanguageId_in", "fallbackLanguageId_in", "rowCount_out"
   genSignalDdlWithParms "countryIdNotValid", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(defaultCountryId_in))"
   Print #fileNo, addTab(1); "END IF;"

   ' ############################################################

   genProcSectionHeader fileNo, "for batch-script: set command options"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('UPDATE COMMAND OPTIONS USING S ON');"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('UPDATE COMMAND OPTIONS USING X ON');"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('UPDATE COMMAND OPTIONS USING V ON');"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "cleanup meta data - if data exists"
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_Tabs"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "seqNo,"
   Print #fileNo, addTab(3); "qualTabName,"
   Print #fileNo, addTab(3); "filterColName,"
   Print #fileNo, addTab(3); "filterSet"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "VALUES(1, '"; g_qualTabNameLanguageSequenceElement; "', 'LSESEQ_OID', 'SELECT OID FROM "; g_qualTabNameLanguageSequence; " WHERE LASFOR_OID = ' || RTRIM(CHAR(v_orgOid)))"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(2, '"; g_qualTabNameLanguageSequence; "', 'LASFOR_OID', RTRIM(CHAR(v_orgOid)))"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(3, '"; g_qualTabNameDataPool; "', 'DPOORG_OID', RTRIM(CHAR(v_orgOid)))"
   Print #fileNo, addTab(4); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(4, '"; g_qualTabNameOrganization; "', 'OID', RTRIM(CHAR(v_orgOid)))"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "qualTabName AS c_qualTabName,"
   Print #fileNo, addTab(3); "filterColName AS c_filterColName,"
   Print #fileNo, addTab(3); "filterSet AS c_filterSet"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_Tabs"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "seqNo"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_qualTabName || ' WHERE ' || c_filterColName || ' IN (' || c_filterSet || ')';"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   ' ############################################################

   genProcSectionHeader fileNo, "setup organization-specific data in """ & g_qualTabNameOrganization & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNameOrganization; " (' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 4, , True
   setAttributeMapping transformation, 1, conCreateTimestamp, ""
   setAttributeMapping transformation, 2, conUpdateUser, ""
   setAttributeMapping transformation, 3, conLastUpdateTimestamp, ""
   setAttributeMapping transformation, 4, conVersionId, ""

   genTransformedAttrListForEntityWithColReuse g_classIndexOrganization, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(1); "') VALUES (' ||"
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 13, , True
   setAttributeMapping transformation, 1, conCreateTimestamp, ""
   setAttributeMapping transformation, 2, conUpdateUser, ""
   setAttributeMapping transformation, 3, conLastUpdateTimestamp, ""
   setAttributeMapping transformation, 4, conVersionId, ""
   setAttributeMapping transformation, 5, conOid, "RTRIM(CHAR(v_orgOid))"
   setAttributeMapping transformation, 6, conName, "'''' || orgBusName_in || ''''"
   setAttributeMapping transformation, 7, conIsMpc, "'1'"
   setAttributeMapping transformation, 8, conIsDcVd, "'0'"
   setAttributeMapping transformation, 9, conPrimaryLanguage, "RTRIM(CHAR(primaryLanguageId_in))"
   setAttributeMapping transformation, 10, conFallBackLanguage, "RTRIM(CHAR(fallbackLanguageId_in))"
   setAttributeMapping transformation, 11, "LASTARCHIVEDATE", "'NULL'"
   setAttributeMapping transformation, 12, "DFCHDC_OID", "RTRIM(CHAR(v_defaultCountryOid))"
   setAttributeMapping transformation, 13, conCreateUser, "'''' || RTRIM(CURRENT USER) || ''''"

   genTransformedAttrListForEntityWithColReuse g_classIndexOrganization, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
   Next k
   Print #fileNo, addTab(1); "')';"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"

   ' ############################################################

   genProcSectionHeader fileNo, "setup organization-specific data in """ & g_qualTabNameDataPool & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNameDataPool; " (' ||"
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 5, , True
   setAttributeMapping transformation, 1, conCreateTimestamp, ""
   setAttributeMapping transformation, 2, conUpdateUser, ""
   setAttributeMapping transformation, 3, conLastUpdateTimestamp, ""
   setAttributeMapping transformation, 4, conVersionId, ""
   setAttributeMapping transformation, 5, conPaiEntitlementGroupId, ""

   genTransformedAttrListForEntityWithColReuse g_classIndexDataPool, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k
 
   Print #fileNo, addTab(1); "') SELECT ' ||"
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 8, , True, , "'DP.", , , , , , , "'"
   setAttributeMapping transformation, 1, conCreateTimestamp, ""
   setAttributeMapping transformation, 2, conUpdateUser, ""
   setAttributeMapping transformation, 3, conLastUpdateTimestamp, ""
   setAttributeMapping transformation, 4, conVersionId, ""
   setAttributeMapping transformation, 5, conOid, "'NEXTVAL FOR " & qualSeqNameOid & "'"
   setAttributeMapping transformation, 6, conPaiEntitlementGroupId, ""
   setAttributeMapping transformation, 7, "DPOORG_OID", "RTRIM(CHAR(v_orgOid))"
   setAttributeMapping transformation, 8, conCreateUser, "'''' || RTRIM(CURRENT USER) || ''''"

   genTransformedAttrListForEntityWithColReuse g_classIndexDataPool, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
   Next k
   Print #fileNo, addTab(1); "' FROM ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNameDataPool; " DP' ||"
   Print #fileNo, addTab(1); "' INNER JOIN ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNamePdmOrganization; " PO' ||"
   Print #fileNo, addTab(1); "' ON ' ||"
   Print #fileNo, addTab(2); "'DP.DPOORG_OID = PO.ORGOID' ||"
   Print #fileNo, addTab(1); "' WHERE ' ||"
   Print #fileNo, addTab(2); "'PO.ID = "; CStr(g_primaryOrgId); "' "

   Print #fileNo, addTab(1); ";"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,


   ' ############################################################

   genProcSectionHeader fileNo, "initialize PS-related data in table """ & g_qualTabNameRegistryStatic & """"
   Print #fileNo, addTab(1); "CALL "; qualProcNameRegStaticInit; "(v_orgOid, NULL, NULL, v_rowCount);"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 

   ' ############################################################

   genProcSectionHeader fileNo, "setup organization-specific data in """ & g_qualTabNameLanguageSequence & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNameLanguageSequence; " (' ||"
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 1, , True
   setAttributeMapping transformation, 1, conVersionId, ""

   genTransformedAttrListForEntityWithColReuse g_classIndexLanguageSequence, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(1); "') SELECT ' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 3, , True, , "'LS.", , , , , , , "'"
   setAttributeMapping transformation, 1, conVersionId, ""
   setAttributeMapping transformation, 2, conOid, "'NEXTVAL FOR " & qualSeqNameOid & "'"
   setAttributeMapping transformation, 3, "LASFOR_OID", "RTRIM(CHAR(v_orgOid))"

   genTransformedAttrListForEntityWithColReuse g_classIndexLanguageSequence, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
   Next k
   Print #fileNo, addTab(1); "' FROM ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNameLanguageSequence; " LS' ||"
   Print #fileNo, addTab(1); "' INNER JOIN ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNamePdmOrganization; " PO' ||"
   Print #fileNo, addTab(1); "' ON ' ||"
   Print #fileNo, addTab(2); "'LS.LASFOR_OID = PO.ORGOID' ||"
   Print #fileNo, addTab(1); "' WHERE ' ||"
   Print #fileNo, addTab(2); "'PO.ID = (SELECT MIN(ID) FROM "; g_qualTabNamePdmOrganization; ")'"
   Print #fileNo, addTab(1); ";"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"

   ' ############################################################

   genProcSectionHeader fileNo, "setup organization-specific data in """ & g_qualTabNameLanguageSequenceElement & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNameLanguageSequenceElement; " (' ||"
 
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 1, , True
   setAttributeMapping transformation, 1, conVersionId, ""

   genTransformedAttrListForEntityWithColReuse g_classIndexLanguageSequenceElement, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(1); "') SELECT ' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 3, , True, , "'LSE.", , , , , , , "'"
   setAttributeMapping transformation, 1, conVersionId, ""
   setAttributeMapping transformation, 2, conOid, "'NEXTVAL FOR " & qualSeqNameOid & "'"
   setAttributeMapping transformation, 3, "LSESEQ_OID", "'LS_T." & g_anOid & "'"

   genTransformedAttrListForEntityWithColReuse g_classIndexLanguageSequenceElement, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
   Next k
   Print #fileNo, addTab(1); "' FROM ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNameLanguageSequenceElement; " LSE' ||"
   Print #fileNo, addTab(1); "' INNER JOIN ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNameLanguageSequence; " LS_S' ||"
   Print #fileNo, addTab(1); "' ON ' ||"
   Print #fileNo, addTab(2); "'LSE.LSESEQ_OID = LS_S."; g_anOid; "' ||"
   Print #fileNo, addTab(1); "' INNER JOIN ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNameLanguageSequence; " LS_T' ||"
   Print #fileNo, addTab(1); "' ON ' ||"
   Print #fileNo, addTab(2); "'LS_S.TYPE_ID = LS_T.TYPE_ID' ||"
   Print #fileNo, addTab(1); "' INNER JOIN ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNamePdmOrganization; " PO' ||"
   Print #fileNo, addTab(1); "' ON ' ||"
   Print #fileNo, addTab(2); "'LS_S.LASFOR_OID = PO.ORGOID' ||"
   Print #fileNo, addTab(1); "' WHERE ' ||"
   Print #fileNo, addTab(2); "'PO.ID = (SELECT MIN(ID) FROM "; g_qualTabNamePdmOrganization; ")' ||"
   Print #fileNo, addTab(3); "' AND ' ||"
   Print #fileNo, addTab(2); "'LS_T.LASFOR_OID = ' || RTRIM(CHAR(v_orgOid))"
   Print #fileNo, addTab(1); ";"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
 
   ' ############################################################
   genProcSectionHeader fileNo, "setup organization-specific data in """ & getUnqualObjName(g_qualTabNamePricePreferencesCto) & """"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO "; getSchemaName(g_qualTabNamePricePreferencesCto); _
                               "' || RIGHT(DIGITS(orgId_in),2) || '."; getUnqualObjName(g_qualTabNamePricePreferencesCto); _
                               "(' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 1, , True
   setAttributeMapping transformation, 1, conVersionId, ""
 
   genTransformedAttrListForEntityWithColReuse g_classIndexPricePreferences, eactClass, transformation, tabColumns, fileNo, ddlType, g_primaryOrgIndex, , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(1); "') SELECT ' ||"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformation, 5, , True, , "'PPR.", , , , , , , "'"
   setAttributeMapping transformation, 1, conVersionId, ""
   setAttributeMapping transformation, 2, conOid, "'NEXTVAL FOR " & qualSeqNameOid & "'"
   setAttributeMapping transformation, 3, conPrimaryPriceTypeForTestId, "'" & CStr(gc_dfltPrimaryPriceTypeOrg) & "'"
   setAttributeMapping transformation, 4, conPriceSelectionForOverlapId, "'" & CStr(gc_dfltPriceSelectionForOverlapOrg) & "'"
   setAttributeMapping transformation, 5, conRebateValueType, "'COALESCE((SELECT D.VALUETYPE FROM " & g_qualTabNameRebateDefault & " D WHERE D." & g_anPsOid & " = PS." & g_anOid & "), PPR.REBATEVALUETYPE)'"

   genTransformedAttrListForEntityWithColReuse g_classIndexPricePreferences, eactClass, transformation, tabColumns, fileNo, ddlType, g_primaryOrgIndex, , 2, , , edomNone

   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(2); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
   Next k
   Print #fileNo, addTab(1); "' FROM ' ||"
   Print #fileNo, addTab(2); "'"; qualTabNamePricePreferencesFac; " PPR' ||"
   Print #fileNo, addTab(1); "' INNER JOIN ' ||"
   Print #fileNo, addTab(2); "'"; g_qualTabNameProductStructure; " PS' ||"
   Print #fileNo, addTab(1); "' ON ' ||"
   Print #fileNo, addTab(2); "'PPR."; g_anPsOid; " = PS."; g_anOid; "' ||"
   Print #fileNo, addTab(1); "' WHERE ' ||"
   Print #fileNo, addTab(2); "'PS."; g_anIsUnderConstruction; " = 0'"
   Print #fileNo, addTab(1); ";"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInitBus; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"

   ' ############################################################

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmtCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementOrgInitBus
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmtCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameOrgInitBus, ddlType, , "mode_in", "orgId_in", "'orgBusName_in", _
     "defaultCountryId_in", "primaryLanguageId_in", "fallbackLanguageId_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Private Sub genDbAdminDdlOrgInitEnp( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     Exit Sub
   End If

   Dim qualTabNameGenericAspectFactoryProd As String
   qualTabNameGenericAspectFactoryProd = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
   Dim qualTabNamePropertyFactoryProd As String
   qualTabNamePropertyFactoryProd = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
   Dim qualTabNamePropertyTemplateFactoryProd As String
   qualTabNamePropertyTemplateFactoryProd = genQualTabNameByClassIndex(g_classIndexPropertyTemplate, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
 
   Dim qualProcedureNameOrgInitEnp As String
   qualProcedureNameOrgInitEnp = genQualProcName(g_sectionIndexDbAdmin, spnOrgInitEnp, ddlType)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(-1, ddlType)

   ' ####################################################################################################################
   ' #    SP for initialization of organization's ENPs in Work Datapool
   ' ####################################################################################################################

   printSectionHeader "SP for initialization of organization's ENPs in Work Datapool", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameOrgInitEnp
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "ID of the organization to initialize"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records INSERTED / UPDATED"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_rebateValue", "INTEGER", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(32000)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genSigMsgVarDecl fileNo
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "alreadyExist", "42710"
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE c CURSOR FOR v_stmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempEnpOids fileNo, , True, True, True
   genDdlForTempChangeLogSummary fileNo, , True, True, True, True

   genSpLogProcEnter fileNo, qualProcedureNameOrgInitEnp, ddlType, , "orgId_in", "rowCount_out"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "loop over ProductStructures"
   Print #fileNo, addTab(1); "FOR psLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "PS."; g_anOid; " AS V_PSOID"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameProductStructure; " PS"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "PS."; g_anIsUnderConstruction; " = "; gc_dbFalse
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "determine rebate value", 2
   Print #fileNo, addTab(2); "SET v_rebateValue = 0;"

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SELECT REBATEVALUETYPE FROM "; getSchemaName(g_qualTabNamePricePreferencesCto); _
                             "' || RIGHT(DIGITS(orgId_in),2) || '."; _
                             getUnqualObjName(g_qualTabNamePricePreferencesCto); " WHERE "; _
                             g_anPsOid; " = ' || RTRIM(CHAR(V_PSOID));"

   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "OPEN c;"
   Print #fileNo, addTab(2); "FETCH"
   Print #fileNo, addTab(3); "c"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rebateValue"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "CLOSE c;"

   Print #fileNo, addTab(2); "SET v_rebateValue = COALESCE(v_rebateValue, 25);"

   genProcSectionHeader fileNo, "initialize " & gc_tempTabNameChangeLogOrgSummary & " with EBP-Prices", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt ="
   Print #fileNo, addTab(3); "'INSERT INTO ' ||"
   Print #fileNo, addTab(4); "'"; gc_tempTabNameChangeLogOrgSummary; "' ||"
   Print #fileNo, addTab(3); "'(' ||"
   Print #fileNo, addTab(4); "'objectId,' ||"
   Print #fileNo, addTab(4); "'entityId,' ||"
   Print #fileNo, addTab(4); "'entityType,' ||"
   Print #fileNo, addTab(4); "'ahClassId,' ||"
   Print #fileNo, addTab(4); "'ahObjectId,' ||"
   Print #fileNo, addTab(4); "'aggregateType,' ||"
   Print #fileNo, addTab(4); "'ahIsCreated,' ||"
   Print #fileNo, addTab(4); "'ahIsUpdated,' ||"
   Print #fileNo, addTab(4); "'ahIsDeleted,' ||"
   Print #fileNo, addTab(4); "'isCreated,' ||"
   Print #fileNo, addTab(4); "'isUpdated,' ||"
   Print #fileNo, addTab(4); "'isDeleted' ||"
   Print #fileNo, addTab(3); "') ' ||"

   Print #fileNo, addTab(3); "'SELECT ' ||"

   Print #fileNo, addTab(4); "'GAS."; g_anOid; ",' ||"
   Print #fileNo, addTab(4); "'GAS."; g_anCid; ",' ||"
   Print #fileNo, addTab(4); "'''"; gc_acmEntityTypeKeyClass; "'',' ||"
   Print #fileNo, addTab(4); "'GAS."; g_anCid; ",' ||"
   Print #fileNo, addTab(4); "'GAS."; g_anOid; ",' ||"
   Print #fileNo, addTab(4); "'''"; g_classes.descriptors(g_classIndexGenericAspect).aggHeadClassIdStr; "'',' ||"
   Print #fileNo, addTab(4); "'1,' ||"
   Print #fileNo, addTab(4); "'0,' ||"
   Print #fileNo, addTab(4); "'0,' ||"
   Print #fileNo, addTab(4); "'1,' ||"
   Print #fileNo, addTab(4); "'0,' ||"
   Print #fileNo, addTab(4); "'0' ||"
 
   Print #fileNo, addTab(3); "' FROM ' ||"
   Print #fileNo, addTab(4); "'"; qualTabNameGenericAspectFactoryProd; " GAS' ||"
   Print #fileNo, addTab(3); "' INNER JOIN ' ||"
   Print #fileNo, addTab(4); "'"; qualTabNamePropertyFactoryProd; " PRP' ||"
   Print #fileNo, addTab(3); "' ON ' ||"
   Print #fileNo, addTab(4); "'GAS.PRPAPR_OID = PRP."; g_anOid; "' ||"
   Print #fileNo, addTab(3); "' INNER JOIN ' ||"
   Print #fileNo, addTab(4); "'"; qualTabNamePropertyTemplateFactoryProd; " PRT' ||"
   Print #fileNo, addTab(3); "' ON ' ||"
   Print #fileNo, addTab(4); "'PRP.PTMHTP_OID = PRT."; g_anOid; "' ||"
   Print #fileNo, addTab(3); "' WHERE ' ||"
   Print #fileNo, addTab(4); "'GAS."; g_anCid; " = ''"; g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr; "''' ||"
   Print #fileNo, addTab(3); "' AND ' ||"
   Print #fileNo, addTab(4); "'PRT.ID = "; CStr(propertyTemplateIdEbp); "'"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "determine ENP/EBP-Mapping", 2
   Dim qualProcedureNameGetEnpEbpMapping As String
   qualProcedureNameGetEnpEbpMapping = genQualProcName(g_sectionIndexFactoryTakeover, spnFtoGetEnpEbpMap, ddlType)

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; getSchemaName(qualProcedureNameGetEnpEbpMapping); "' || RIGHT(DIGITS(orgId_in),2) || '"; CStr(g_workDataPoolId); "."; _
                             getUnqualObjName(qualProcedureNameGetEnpEbpMapping); "(' || RTRIM(CHAR(V_PSOID)) || ', ?)';"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); ";"
 
   genProcSectionHeader fileNo, "accumulate number of affected rows", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   genProcSectionHeader fileNo, "calculate EBP", 2
 
   Dim qualProcedureNameSetEnp As String
   qualProcedureNameSetEnp = genQualProcName(g_sectionIndexFactoryTakeover, spnFtoSetEnp, ddlType, , , , "NoLrt", eondmNone)
 
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; getSchemaName(qualProcedureNameSetEnp); "' || RIGHT(DIGITS(orgId_in),2) || '"; CStr(g_workDataPoolId); "."; _
                             getUnqualObjName(qualProcedureNameSetEnp); "(' || RTRIM(CHAR(v_rebateValue)) || ', ?)';"

   genProcSectionHeader fileNo, "accumulate number of affected rows", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit fileNo, qualProcedureNameOrgInitEnp, ddlType, , "orgId_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 Private Sub genDbAdminDdlOrgInit( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     Exit Sub
   End If

   Dim qualProcedureNameOrgInitEnp As String
   qualProcedureNameOrgInitEnp = genQualProcName(g_sectionIndexDbAdmin, spnOrgInitEnp, ddlType)

   Dim qualTabNameGenericAspectFactoryProd As String
   qualTabNameGenericAspectFactoryProd = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
   Dim qualTabNamePropertyFactoryProd As String
   qualTabNamePropertyFactoryProd = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
   Dim qualTabNamePropertyTemplateFactoryProd As String
   qualTabNamePropertyTemplateFactoryProd = genQualTabNameByClassIndex(g_classIndexPropertyTemplate, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
 
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(-1, ddlType)

   ' ####################################################################################################################
   ' #    SP for initialization of organization's data
   ' ####################################################################################################################

   Dim qualProcedureNameOrgInit As String
   qualProcedureNameOrgInit = genQualProcName(g_sectionIndexDbAdmin, spnOrgInit, ddlType)

   printSectionHeader "SP for initialization of organization's data (copy from factory)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameOrgInit
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "orgId_in", g_dbtEnumId, True, "ID of the organization to initialize"
   genProcParm fileNo, "IN", "psOidList_in", "VARCHAR(400)", True, "(optional) ','-delimited list of OIDs of ProductStructures"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being copied (sum over all tables)"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 2"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "SQLCODE", "INTEGER", "0"
   genVarDecl fileNo, "v_orgOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_psOidList", "VARCHAR(400)", "''"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(32000)", "NULL"
   genVarDecl fileNo, "v_stmntTxtUtil", "VARCHAR(32000)", "NULL"
   genVarDecl fileNo, "v_numDataPools", "INTEGER", "NULL"
   genVarDecl fileNo, "v_colList", "VARCHAR(8000)", "NULL"
   genVarDecl fileNo, "v_colListForSelect", "VARCHAR(8000)", "NULL"
   genVarDecl fileNo, "v_IsBlockedPriceExpression", "VARCHAR(1000)", "NULL"
   genVarDecl fileNo, "v_fltrTxt", "VARCHAR(1600)", "NULL"
   genVarDecl fileNo, "v_deleteFltrTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_rowCount2", "INTEGER", "0"
   genVarDecl fileNo, "v_qualTabNameView", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_qualTabNameGeneralSettings", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_qualTabNameEndSlot", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_qualTabNameNsrValidForOrganization", "VARCHAR(128)", "NULL"
   genVarDecl fileNo, "v_qualTabNameDisplaySlot", "VARCHAR(128)", "NULL"
   genSigMsgVarDecl fileNo
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare continue handler for SQL-Exceptions"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"

   Dim tempTabNameStatementOrgInit As String
   tempTabNameStatementOrgInit = tempTabNameStatement & "OrgInit"

   genDdlForTempStatement fileNo, 1, True, 32000, True, True, True, , "OrgInit"
 
   genProcSectionHeader fileNo, "temporary table for table statistics"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.TableData"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "seqNo            INTEGER,"
   Print #fileNo, addTab(2); "poolTypeId       INTEGER,"
   Print #fileNo, addTab(2); "numRowsDeleted   INTEGER,"
   Print #fileNo, addTab(2); "numRowsInserted  INTEGER,"
   Print #fileNo, addTab(2); "srcTabSchemaName "; g_dbtDbSchemaName; ","
   Print #fileNo, addTab(2); "tgtTabSchemaName "; g_dbtDbSchemaName; ", "
   Print #fileNo, addTab(2); "tabName          VARCHAR(50)"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True

   genProcSectionHeader fileNo, "temporary table for CountryID-Lists"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.myCountryIdLists"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "countryIdListOid "; g_dbtOid
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True

   genProcSectionHeader fileNo, "temporary table for productstructure oids"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.PsOids"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "oid "; g_dbtOid
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True, True, True
 
   genSpLogProcEnter fileNo, qualProcedureNameOrgInit, ddlType, 1, "mode_in", "orgId_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "make sure that a entry for 'countries managed by the new MPC' exists"
   Print #fileNo, addTab(1); "IF NOT EXISTS ( SELECT 1 FROM "; g_qualTabNameOrgManagesCountry; " WHERE org_oid = ( SELECT ORGOID FROM "; g_qualTabNamePdmOrganization; " WHERE ID = orgId_in ) ) THEN"
   genSignalDdlWithParms "OrgManagesCountryNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(orgId_in))"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "determine Organization's OID"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ORGOID"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_orgOid"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNamePdmOrganization
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "ID = orgId_in"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader fileNo, "determine 'my' countryIdLists"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.myCountryIdLists"
   Print #fileNo, addTab(1); "SELECT DISTINCT"
   Print #fileNo, addTab(2); "X.CIL_OID"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameOrgManagesCountry; " M"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNamePdmOrganization; " O"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "M.ORG_OID = O.ORGOID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameCountryIdXRef; " X"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "X.CSP_OID = M.CNT_OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "O.ID = orgId_in"
   Print #fileNo, addTab(1); ";"


   genProcSectionHeader fileNo, "store relevant ps_oids in temp table "
   Print #fileNo, addTab(1); "IF psOidList_in IS NULL THEN"
   Print #fileNo, addTab(2); "INSERT INTO SESSION.PsOids( oid ) SELECT P.OID FROM VL6CPST.PRODUCTSTRUCTURE P;"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "INSERT INTO SESSION.PsOids( oid )"
   Print #fileNo, addTab(3); "SELECT BIGINT(X.elem) FROM TABLE ( VL6CMET.F_STRELEMS(psOidList_in, CAST(',' AS CHAR(1))) ) AS X;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "check for valid ps oids"
   Print #fileNo, addTab(1); "FOR psLoop AS psCursor CURSOR FOR"
   Print #fileNo, addTab(2); "SELECT oid AS c_ps_oid FROM SESSION.PsOids DO"
   Print #fileNo, addTab(3); "IF NOT (c_ps_oid IN (SELECT oid FROM VL6CPST.PRODUCTSTRUCTURE)) THEN"
   genSignalDdlWithParms "psNotExist", fileNo, 4, , , , , , , , , , "RTRIM(CHAR(c_ps_oid))"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(3); "SET v_psOidList = v_psOidList || c_ps_oid || ',';"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo, addTab(1); "SET v_psOidList = strip(v_psOidList, trailing, ',');"

 
   genProcSectionHeader fileNo, "for batch-script: set command options"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES ('UPDATE COMMAND OPTIONS USING S ON');"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES ('UPDATE COMMAND OPTIONS USING X ON');"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES ('UPDATE COMMAND OPTIONS USING V ON');"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Dim qualProcedureNameTrigDisable As String
   qualProcedureNameTrigDisable = genQualProcName(g_sectionIndexDbAdmin, spnTriggerDisable, ddlType)

   genProcSectionHeader fileNo, "disable triggers for organization"

   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameTrigDisable; "(NULL, NULL, NULL, NULL, ' || RTRIM(CHAR(orgId_in)) || ', NULL, ?, ?)';"

   Print #fileNo,
   addStatementIntoTempTable fileNo, 1, tempTabNameStatementOrgInit, False
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   genProcSectionHeader fileNo, "declare variables", 3, True
   genVarDecl fileNo, "v_trigCount", "INTEGER", "NULL", 3
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL", 3

   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_trigCount,"
   Print #fileNo, addTab(4); "v_failCount"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "delete all data in all organization's data pools - in case there is already some data"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_tabschemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " AS c_poolTypeId,"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " AS c_fkSequenceNo"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "WHERE"

   Dim firstException As Boolean
   Dim exceptionComment As String
   Dim thisExceptionComment As String
   firstException = True
   exceptionComment = ""
   Dim i As Integer
   For i = 1 To g_classes.numDescriptors
     thisExceptionComment = ""
       If g_classes.descriptors(i).classIndex = g_classIndexPricePreferences Then
         thisExceptionComment = "exclude """ & UCase(g_classes.descriptors(i).sectionName) & "." & UCase(g_classes.descriptors(i).className) & """ (already initialized)"
       End If

       If thisExceptionComment <> "" Then
         If firstException Then
           Print #fileNo, addTab(3); "NOT ("
           Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "A."; g_anAcmEntityId; " IN ("
         Else
           Print #fileNo, ","; IIf(exceptionComment <> "", " -- " & exceptionComment, "")
         End If
         exceptionComment = thisExceptionComment
         Print #fileNo, addTab(5); "'"; UCase(g_classes.descriptors(i).classIdStr); "'";
         firstException = False
       End If
   Next i
 
   If exceptionComment <> "" Then
     genProcSectionHeader fileNo, exceptionComment, 1, True

     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); ")"

     Print #fileNo, addTab(4); "AND"
   End If

   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = orgId_in"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " DESC,"
   Print #fileNo, addTab(3); "COALESCE(P."; g_anPoolTypeId; ", 999) ASC,"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " DESC"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || RTRIM(c_tabschemaName) || '.' || RTRIM(c_tabName);"
   Print #fileNo,
   addStatementIntoTempTable fileNo, 2, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 3
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "commit to toggle on logging again", 2
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "COMMIT;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "UPDATE SESSION.TableData T SET T.numRowsDeleted = v_rowCount WHERE T.TgtTabSchemaName = c_tabschemaName AND T.tabName = c_tabName;"
   Print #fileNo, addTab(2); "IF SQLCODE <> 0 THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); "SESSION.TableData"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "seqNo,"
   Print #fileNo, addTab(4); "poolTypeId,"
   Print #fileNo, addTab(4); "numRowsDeleted,"
   Print #fileNo, addTab(4); "tgtTabSchemaName,"
   Print #fileNo, addTab(4); "tabName"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "c_fkSequenceNo,"
   Print #fileNo, addTab(4); "c_poolTypeId,"
   Print #fileNo, addTab(4); "v_rowCount,"
   Print #fileNo, addTab(4); "c_tabschemaName,"
   Print #fileNo, addTab(4); "c_tabName"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "REORG for Table GENERICASPECT because of DELETE-run time-problems for subsequent commands", 2
   Print #fileNo, addTab(2); "IF c_tabName = 'GENERICASPECT' THEN"
 
   Print #fileNo, addTab(3); "SET v_stmntTxtUtil = 'CALL VL6CDBA.REORG( ''1'', ''T'', ''' || RTRIM( c_tabschemaName ) || ''', ''' || RTRIM( c_tabName ) || ''', NULL, NULL, ''0'', ''1'', ?, ? )' ;"
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES (v_stmntTxtUtil);"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxtUtil;"
 
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "lock all factory productive data pools", 1
   Dim qualProcNameSetLock As String
   qualProcNameSetLock = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , "SHAREDREAD")
 
   Print #fileNo, addTab(1); "FOR dpLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "OR.ORGOID AS c_orgOid,"
   Print #fileNo, addTab(3); "SC."; g_anPoolTypeId; "  AS c_accessModeId,"
   Print #fileNo, addTab(3); "PS."; g_anOid; " AS c_psOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SESSION.PsOids PS,"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " OR,"
   Print #fileNo, addTab(3); g_qualTabNamePdmPrimarySchema; " SC"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(OR.ID = SC."; g_anOrganizationId; ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(SC."; g_anPoolTypeId; " = "; CStr(g_productiveDataPoolId); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(SC."; g_anOrganizationId; " = "; CStr(g_primaryOrgId); ")"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "lock data pool", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameSetLock; "(''' ||"
   Print #fileNo, addTab(12); "RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',' ||"
   Print #fileNo, addTab(12); "'''<admin>'',' ||"
   Print #fileNo, addTab(12); "'''' || CAST(CASE COALESCE(CURRENT USER, '') WHEN '' THEN '<unknown>' ELSE CURRENT USER END AS "; g_dbtUserId; ") || ''',' ||"
   Print #fileNo, addTab(12); "'''ORGINIT'',' ||"
   Print #fileNo, addTab(12); "'?)';"
   Print #fileNo,
   addStatementIntoTempTable fileNo, 2, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_numDataPools"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "if lock on data pool could not be aquired, rollback and exit", 3
   Print #fileNo, addTab(3); "IF v_numDataPools = 0 THEN"
   Print #fileNo, addTab(4); "ROLLBACK;"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInit, ddlType, 4, "mode_in", "orgId_in", "rowCount_out"
   genSignalDdlWithParms "setRel2ProdLockFail", fileNo, 4, "SHAREDREAD", , , , , , , , , "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(c_psOid))", "RTRIM(CHAR(c_accessModeId))"

   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "lock all organization's data pools", 1
 '  Dim qualProcNameSetLock As String
   qualProcNameSetLock = genQualProcName(g_sectionIndexDbMeta, spnSetRel2ProdLock, ddlType, , , , "EXCLUSIVEWRITE")
 
   Print #fileNo, addTab(1); "FOR dpLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "OR.ORGOID AS c_orgOid,"
   Print #fileNo, addTab(3); "SC."; g_anPoolTypeId; "  AS c_accessModeId,"
   Print #fileNo, addTab(3); "PS."; g_anOid; " AS c_psOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SESSION.PsOids PS,"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " OR,"
   Print #fileNo, addTab(3); g_qualTabNamePdmPrimarySchema; " SC"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(OR.ID = SC."; g_anOrganizationId; ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(SC."; g_anPoolTypeId; " IN ( "; CStr(g_workDataPoolId); ","; CStr(g_productiveDataPoolId); " ) )"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(SC."; g_anOrganizationId; " = orgId_in)"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "lock data pool", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameSetLock; "(''' ||"
   Print #fileNo, addTab(12); "RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',' ||"
   Print #fileNo, addTab(12); "'''<admin>'',' ||"
   Print #fileNo, addTab(12); "'''' || CAST(CASE COALESCE(CURRENT USER, '') WHEN '' THEN '<unknown>' ELSE CURRENT USER END AS "; g_dbtUserId; ") || ''',' ||"
   Print #fileNo, addTab(12); "'''ORGINIT'',' ||"
   Print #fileNo, addTab(12); "'?)';"
   Print #fileNo,
   addStatementIntoTempTable fileNo, 2, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_numDataPools"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "if lock on data pool could not be aquired, rollback and exit", 3
   Print #fileNo, addTab(3); "IF v_numDataPools = 0 THEN"
   Print #fileNo, addTab(4); "ROLLBACK;"
   genSpLogProcEscape fileNo, qualProcedureNameOrgInit, ddlType, 4, "mode_in", "orgId_in", "rowCount_out"
   genSignalDdlWithParms "setRel2ProdLockFail", fileNo, 4, "EXCLUSIVEWRITE", , , , , , , , , "RTRIM(CHAR(c_orgOid))", "RTRIM(CHAR(c_psOid))", "RTRIM(CHAR(c_accessModeId))"

   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "copy data from factory productive data pool to organization's work data pool"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " AS c_entitySection,"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " AS c_entityName,"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityShortName; " AS c_entityShortName,"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " AS c_entityType,"
   Print #fileNo, addTab(3); "A."; g_anAcmIsPs; " AS c_isPs,"
   Print #fileNo, addTab(3); "A1."; conAcmEntityShortName; " AS c_divPrefix,"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityName; " AS c_ahClassName,"
   Print #fileNo, addTab(3); "PF."; g_anPdmFkSchemaName; " AS c_srcTabSchemaName,"
   Print #fileNo, addTab(3); "PO."; g_anPdmFkSchemaName; " AS c_tgtTabSchemaName,"
   Print #fileNo, addTab(3); "PO."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "PO."; g_anPoolTypeId; " AS c_poolTypeId,"
   Print #fileNo, addTab(3); "PH."; g_anPdmFkSchemaName; " AS c_ahTabSchemaName,"
   Print #fileNo, addTab(3); "PH."; g_anPdmTableName; " AS c_ahTabName,"
   Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " AS c_isGen,"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " AS c_isNl,"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " AS c_fkSequenceNo,"
   Print #fileNo, addTab(3); "(CASE WHEN D.SRC_SCHEMANAME IS NULL THEN 0 ELSE 1 END) AS c_hasSelfReference"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
 
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PO"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PO."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PO."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
 
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PF"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PF."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PF."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " AH"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityId; " = A."; g_anAhCid; ""
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LH"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntitySection; " = LH."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityName; " = LH."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityType; " = LH."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LH."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LH."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LH."; g_anLdmIsLrt; " = "; gc_dbFalse

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PH"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PH."; g_anPdmLdmFkSchemaName; " = LH."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PH."; g_anPdmLdmFkTableName; " = LH."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PH."; g_anOrganizationId; " = orgId_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COALESCE(PH."; g_anPoolTypeId; ","; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameFkDependency; " D"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "D.SRC_SCHEMANAME = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "D.SRC_TABLENAME = L."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "D.DST_SCHEMANAME = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "D.DST_TABLENAME = L."; g_anLdmTableName

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A1"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = A1."; conAcmLeftEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A1."; conAcmRightEntityName; " = 'DIVISION'"
 
   Print #fileNo, addTab(2); "WHERE"

   firstException = True
   exceptionComment = ""
   For i = 1 To g_relationships.numDescriptors
     thisExceptionComment = ""
       If Not g_relationships.descriptors(i).isUserTransactional And ((g_relationships.descriptors(i).maxLeftCardinality = -1 And g_relationships.descriptors(i).maxRightCardinality = -1) Or g_relationships.descriptors(i).isNl) And _
          (UCase(g_relationships.descriptors(i).sectionName) = UCase(snOrder) Or UCase(g_relationships.descriptors(i).sectionName) = UCase(snReport) Or UCase(g_relationships.descriptors(i).sectionName) = UCase(snPricing)) Then
         thisExceptionComment = "exclude """ & UCase(g_relationships.descriptors(i).sectionName) & "." & UCase(g_relationships.descriptors(i).relName) & """ (section """ & UCase(g_relationships.descriptors(i).sectionName) & """)"
       End If

       If thisExceptionComment <> "" Then
         If firstException Then
           Print #fileNo, addTab(3); "NOT ("
           Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "A."; g_anAcmEntityId; " IN ("
         Else
           Print #fileNo, ","; IIf(exceptionComment <> "", " -- " & exceptionComment, "")
         End If
         exceptionComment = thisExceptionComment
         Print #fileNo, addTab(5); "'"; UCase(g_relationships.descriptors(i).relIdStr); "'";
         firstException = False
       End If
   Next i

   If exceptionComment <> "" Then
     genProcSectionHeader fileNo, exceptionComment, 1, True

     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); ")"

     Print #fileNo, addTab(4); "AND"
   End If

   firstException = True
   exceptionComment = ""
   For i = 1 To g_classes.numDescriptors
     thisExceptionComment = ""
       If g_classes.descriptors(i).classIndex = g_classIndexPricePreferences Then
         thisExceptionComment = "exclude """ & UCase(g_classes.descriptors(i).sectionName) & "." & UCase(g_classes.descriptors(i).className) & """ (already initialized)"
       End If

       If (g_classes.descriptors(i).superClassIndex <= 0 And Not g_classes.descriptors(i).isUserTransactional) Or g_classes.descriptors(i).classIndex = g_classIndexTaxParameter Or g_classes.descriptors(i).classIndex = g_classIndexTaxType Then
         If UCase(g_classes.descriptors(i).sectionName) = UCase(snOrder) Or UCase(g_classes.descriptors(i).sectionName) = UCase(snReport) Or UCase(g_classes.descriptors(i).sectionName) = UCase(snPricing) Then
           thisExceptionComment = "exclude """ & UCase(g_classes.descriptors(i).sectionName) & "." & UCase(g_classes.descriptors(i).className) & """ (section """ & UCase(g_classes.descriptors(i).sectionName) & """)"
         End If
       End If

       If thisExceptionComment <> "" Then
         If firstException Then
           Print #fileNo, addTab(3); "NOT ("
           Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "A."; g_anAcmEntityId; " IN ("
         Else
           Print #fileNo, ","; IIf(exceptionComment <> "", " -- " & exceptionComment, "")
         End If
         exceptionComment = thisExceptionComment
         Print #fileNo, addTab(5); "'"; UCase(g_classes.descriptors(i).classIdStr); "'";
         firstException = False
       End If
   Next i

   If exceptionComment <> "" Then
     genProcSectionHeader fileNo, exceptionComment, 1, True

     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); ")"

     Print #fileNo, addTab(4); "AND"
   End If

   Print #fileNo, addTab(3); "(A."; g_anAcmIsLrt; " = 1 OR PO."; g_anPoolTypeId; " IS NULL)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PO."; g_anOrganizationId; " = orgId_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COALESCE(PO."; g_anPoolTypeId; ","; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PF."; g_anOrganizationId; " = "; CStr(g_primaryOrgId)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COALESCE(PF."; g_anPoolTypeId; ","; CStr(g_productiveDataPoolId); ") = "; CStr(g_productiveDataPoolId)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "(CASE WHEN A."; g_anAhCid; " IS NULL THEN 0 ELSE 1 END) DESC,"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " ASC"
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader fileNo, "determine common columns in source and target table", 2, True
   Print #fileNo, addTab(2); "SET v_colList = '';"
   Print #fileNo, addTab(2); "SET v_colListForSelect = '';"
   Print #fileNo, addTab(2); "FOR colLoop AS colCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "S.COLNAME AS V_COLNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS S"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS T"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "S.TABNAME = T.TABNAME"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "S.COLNAME = T.COLNAME"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "S.TABSCHEMA = c_tgtTabSchemaName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "T.TABSCHEMA = c_srcTabSchemaName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "T.TABNAME = c_tabName"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "S.COLNO"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "SET v_colList = v_colList || (CASE v_colList WHEN '' THEN '' ELSE ',' END) || V_COLNAME;"
   Print #fileNo, addTab(3); "IF c_tabName = 'GENERICASPECT' AND V_COLNAME = 'ISBLOCKEDPRICE' THEN"
   Print #fileNo, addTab(4); "SET v_IsBlockedPriceExpression = 'CASE WHEN S.CLASSID = ''09006'' AND ((SELECT P.ISDPB FROM VL6CMET' || RIGHT(DIGITS(orgId_in),2) || '.PRICEPREFERENCES P WHERE P.PS_OID = S.PS_OID) = 0) THEN 1 ELSE S.ISBLOCKEDPRICE END';"
   Print #fileNo, addTab(4); "SET v_colListForSelect = v_colListForSelect || (CASE v_colListForSelect WHEN '' THEN '' ELSE ',' END) || v_IsBlockedPriceExpression;"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_colListForSelect = v_colListForSelect || (CASE v_colListForSelect WHEN '' THEN '' ELSE ',' END) || V_COLNAME;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO ' || RTRIM(c_tgtTabSchemaName) || '.' || RTRIM(c_tabName) || '(' || v_colList || ')' || ' SELECT ' || v_colListForSelect || ' FROM ' || RTRIM(c_srcTabSchemaName) || '.' || RTRIM(c_tabName) || ' S WHERE (1=1)';"

   genProcSectionHeader fileNo, "for PS-tagged tables: exclude records corresponding to PRODUCTSTRUCTURE under construction or to not relevant product structures/divisions", 2
   Print #fileNo, addTab(2); "IF (c_isPs = 1) AND (c_isNl = 0) THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM "; g_qualTabNameProductStructure; " PS WHERE S."; g_anPsOid; " = PS."; g_anOid; " AND PS."; g_anOid; " IN (' || v_psOidList || ') AND PS."; g_anIsUnderConstruction; " = 0)';"
   Print #fileNo, addTab(2); "ELSEIF (c_isNl = 0) AND (c_isGen = 0) AND (c_divPrefix IS NOT NULL) THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM "; g_qualTabNameProductStructure; " PS WHERE S.' || c_divPrefix ||  'DIV_OID = PS.PDIDIV_OID AND PS."; g_anOid; " IN (' || v_psOidList || ') AND PS."; g_anIsUnderConstruction; " = 0)';"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "exclude records referring to aggregate heads not relevant for this organization", 2
   Print #fileNo, addTab(2); "IF (c_ahTabSchemaName IS NOT NULL AND c_ahTabName IS NOT NULL) AND (c_ahTabSchemaName <> c_tgtTabSchemaName OR c_ahTabName <> c_tabName) AND (c_ahClassName <> '"; UCase(clnExpression); "') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM ' || RTRIM(c_ahTabSchemaName) || '.' || RTRIM(c_ahTabName) || ' AH WHERE S."; g_anAhOid; " = AH."; g_anOid; ")';"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "for GEN- and NL_TEXT-tables: exclude records referring to 'parent records' not relevant for this organization'", 2
   Print #fileNo, addTab(2); "IF (c_isNl = 1) THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM ' || RTRIM(c_tgtTabSchemaName) || '.' || RTRIM(REPLACE(c_tabName, '_NL_TEXT', '')) || ' PAR WHERE S.' || RTRIM(c_entityShortName) || '_OID = PAR."; g_anOid; ")';"
   Print #fileNo, addTab(2); "ELSEIF (c_isGen = 1) THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM ' || RTRIM(c_tgtTabSchemaName) || '.' || RTRIM(REPLACE(c_tabName, '_GEN', '')) || ' PAR WHERE S.' || RTRIM(c_entityShortName) || '_OID = PAR."; g_anOid; ")';"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "for relationship-tables: filter by foreign keys referring to records not relevant for this organization", 2
   Print #fileNo, addTab(2); "IF c_entityType = '"; gc_acmEntityTypeKeyRel; "' AND c_isNl = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET v_fltrTxt = NULL;"
   Print #fileNo,
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "(CASE WHEN PL."; g_anOrganizationId; " IS NULL THEN '' ELSE ' AND EXISTS (SELECT 1 FROM ' || RTRIM(PL."; g_anPdmFkSchemaName; ") || '.' || RTRIM(PL."; g_anPdmTableName; ") || ' L WHERE S.' || AL."; g_anAcmEntityShortName; " || '_OID = L."; g_anOid; ")' END) ||"
   Print #fileNo, addTab(4); "(CASE WHEN PR."; g_anOrganizationId; " IS NULL THEN '' ELSE ' AND EXISTS (SELECT 1 FROM ' || RTRIM(PR."; g_anPdmFkSchemaName; ") || '.' || RTRIM(PR."; g_anPdmTableName; ") || ' R WHERE S.' || AR."; g_anAcmEntityShortName; " || '_OID = R."; g_anOid; ")' END)"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_fltrTxt"
   Print #fileNo, addTab(3); "FROM"

   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " AL"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "A."; g_anAcmLeftEntitySection; " = AL."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmLeftEntityName; " = AL."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.LEFT_"; g_anAcmEntityType; " = AL."; g_anAcmEntityType
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " ALPar"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "ALPar."; g_anAcmEntitySection; " = COALESCE(AL."; g_anAcmOrParEntitySection; ", AL."; g_anAcmEntitySection; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "ALPar."; g_anAcmEntityName; " = COALESCE(AL."; g_anAcmOrParEntityName; ", AL."; g_anAcmEntityName; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "ALPar."; g_anAcmEntityType; " = AL."; g_anAcmOrParEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " AR"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "A."; g_anAcmRightEntitySection; " = AR."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmRightEntityName; " = AR."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmRightEntityType; " = AR."; g_anAcmEntityType
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " ARPar"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "ARPar."; g_anAcmEntitySection; " = COALESCE(AR."; g_anAcmOrParEntitySection; ", AR."; g_anAcmEntitySection; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "ARPar."; g_anAcmEntityName; " = COALESCE(AR."; g_anAcmOrParEntityName; ", AR."; g_anAcmEntityName; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "ARPar."; g_anAcmEntityType; " = AR."; g_anAcmOrParEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " LL"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "LL."; g_anAcmEntitySection; " = ALPar."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anAcmEntityName; " = ALPar."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anAcmEntityType; " = ALPar."; g_anAcmEntityType
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " LR"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "LR."; g_anAcmEntitySection; " = ARPar."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anAcmEntityName; " = ARPar."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anAcmEntityType; " = ARPar."; g_anAcmEntityType
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " PL"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "PL."; g_anPdmLdmFkSchemaName; " = LL."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "PL."; g_anPdmLdmFkTableName; " = LL."; g_anLdmTableName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(PL."; g_anOrganizationId; ",orgId_in) = orgId_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(PL."; g_anPoolTypeId; ","; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " PR"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "PR."; g_anPdmLdmFkSchemaName; " = LR."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "PR."; g_anPdmLdmFkTableName; " = LR."; g_anLdmTableName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(PR."; g_anOrganizationId; ",orgId_in) = orgId_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(PR."; g_anPoolTypeId; ","; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(3); "WHERE"

   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = c_entityType"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = c_entityName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = c_entitySection"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(A."; g_anAcmMaxLeftCardinality; " IS NULL AND A."; g_anAcmMaxRightCardinality; " IS NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(PL."; g_anOrganizationId; " IS NOT NULL OR PR."; g_anOrganizationId; " IS NOT NULL)"
   Print #fileNo, addTab(3); "WITH UR;"
 
   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_fltrTxt IS NOT NULL THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = v_stmntTxt || v_fltrTxt;"
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "apply some table-specific filter", 2
   Print #fileNo, addTab(2); "FOR filterLoop AS"
   Print #fileNo, addTab(3); "WITH"
   Print #fileNo, addTab(4); "V_EntityFilter"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "entitySection,"
   Print #fileNo, addTab(4); "entityName,"
   Print #fileNo, addTab(4); "entityType,"
   Print #fileNo, addTab(4); "forGen,"
   Print #fileNo, addTab(4); "forNl,"
   Print #fileNo, addTab(4); "filter"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AS"
   Print #fileNo, addTab(3); "("

   genProcSectionHeader fileNo, "dummy-entry - first record", 4, True
   Print #fileNo, addTab(4); "VALUES('-none-', '-none-', 'X', 0, 0, '0=1')"

   Dim qualFuncNameHasAlCountry As String
   For i = 1 To g_classes.numDescriptors
       If UCase(g_classes.descriptors(i).className) = UCase(clnGenericCode) Then
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude CODEs by type", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; "', 0, 0, 'S.CTYTYP_OID <> 128')"
       ElseIf UCase(g_classes.descriptors(i).className) = UCase(clnGenericAspect) Then
         qualFuncNameHasAlCountry = genQualFuncName(g_classes.descriptors(i).sectionIndex, "HASALCNTRY", ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude " & g_classes.descriptors(i).className & "s not valid for this organization", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; _
               "', 0, 0, 'S.ACLACL_OID IS NULL OR ("; qualFuncNameHasAlCountry; "(S."; g_anOid; ",S."; g_anCid; ",' || RTRIM(CHAR(v_orgOid)) || ')=1)')"
       ElseIf UCase(g_classes.descriptors(i).className) = UCase(clnDecisionTable) Then
         qualFuncNameHasAlCountry = genQualFuncName(g_classes.descriptors(i).sectionIndex, "HASALCNTRY", ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude " & g_classes.descriptors(i).className & "s not valid for this organization", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; _
               "', 0, 0, '"; qualFuncNameHasAlCountry; "(S."; g_anOid; ",' || RTRIM(CHAR(v_orgOid)) || ')=1')"
       End If
       If UCase(g_classes.descriptors(i).className) = UCase(clnView) Then
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude non SR1/SR0-VIEWS and deletable VIEWS", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; "', 0, 0, 'UPPER(S."; g_anName; ") IN (''SR0'',''SR1'') AND S."; g_anIsDeletable; " = 0')"
       End If
       If g_classes.descriptors(i).navPathToOrg.relRefIndex > 0 Then
         Print #fileNo, addTab(4); "UNION ALL"

         Dim qualRelTabOrg As String, relOrgEntityIdStr As String
         qualRelTabOrg = genQualTabNameByRelIndex(g_classes.descriptors(i).navPathToOrg.relRefIndex, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)

         Dim fkAttrToOrg As String
         Dim fkAttrToAh As String
         If g_classes.descriptors(i).navPathToOrg.navDirection = etLeft Then
             fkAttrToOrg = g_relationships.descriptors(g_classes.descriptors(i).navPathToOrg.relRefIndex).leftFkColName(ddlType)
             fkAttrToAh = g_relationships.descriptors(g_classes.descriptors(i).navPathToOrg.relRefIndex).rightFkColName(ddlType)
         Else
             fkAttrToOrg = g_relationships.descriptors(g_classes.descriptors(i).navPathToOrg.relRefIndex).rightFkColName(ddlType)
             fkAttrToAh = g_relationships.descriptors(g_classes.descriptors(i).navPathToOrg.relRefIndex).leftFkColName(ddlType)
         End If

         genProcSectionHeader fileNo, "exclude '" & UCase(g_classes.descriptors(i).className) & "s not relevant for this organization'", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; "', 0, 0, 'NOT EXISTS (SELECT 1 FROM "; qualRelTabOrg; " V WHERE V."; fkAttrToAh; " = S."; g_anOid; ") OR EXISTS (SELECT 1 FROM "; qualRelTabOrg; " V WHERE V."; fkAttrToAh; " = S."; g_anOid; " AND V."; fkAttrToOrg; " = ' || RTRIM(CHAR(v_orgOid)) || ')')"
       End If
       If g_classes.descriptors(i).containsIsNotPublishedInclSubClasses And g_classes.descriptors(i).superClassIndex <= 0 Then
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude 'not published " & UCase(g_classes.descriptors(i).className) & "s'", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; "', 0, 0, 'S."; g_anIsNotPublished; " = 0')"
       End If
   Next i

   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "V.filter AS c_filter"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "V_EntityFilter V"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "V.entitySection = c_entitySection"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V.entityName = c_entityName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V.entityType = c_entityType"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V.forGen = c_isGen"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V.forNl = c_isNl"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "IF c_filter = '0=1' THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = NULL;"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_stmntTxt = v_stmntTxt  || ' AND (' || c_filter || ')';"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"

   genProcSectionHeader fileNo, "apply Foreign-Key-based filter", 2
   Print #fileNo, addTab(2); "SET v_deleteFltrTxt = '';"
   Print #fileNo, addTab(2); "FOR fkLoop AS"
   Print #fileNo, addTab(3); "WITH"
   Print #fileNo, addTab(4); "V_FkCandidates"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "srcOrParentEntitySection,"
   Print #fileNo, addTab(4); "srcOrParentEntityName,"
   Print #fileNo, addTab(4); "srcEntityType,"
   Print #fileNo, addTab(4); "tgtOrParentEntitySection,"
   Print #fileNo, addTab(4); "tgtOrParentEntityName,"
   Print #fileNo, addTab(4); "tgtEntityType,"
   Print #fileNo, addTab(4); "fkColName,"
   Print #fileNo, addTab(4); "isEnforced"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AS"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT DISTINCT"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(L."; g_anAcmOrParEntitySection; ", L."; g_anAcmEntitySection; ") ELSE COALESCE(R."; g_anAcmOrParEntitySection; ", R."; g_anAcmEntitySection; ") END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(L."; g_anAcmOrParEntityName; ",    L."; g_anAcmEntityName; ")    ELSE COALESCE(R."; g_anAcmOrParEntityName; ",    R."; g_anAcmEntityName; ")    END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN L."; g_anAcmEntityType; " ELSE R."; g_anAcmEntityType; " END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(R."; g_anAcmOrParEntitySection; ", R."; g_anAcmEntitySection; ") ELSE COALESCE(L."; g_anAcmOrParEntitySection; ", L."; g_anAcmEntitySection; ") END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(R."; g_anAcmOrParEntityName; ",    R."; g_anAcmEntityName; ")    ELSE COALESCE(L."; g_anAcmOrParEntityName; ",    L."; g_anAcmEntityName; ")    END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN R."; g_anAcmEntityType; " ELSE L."; g_anAcmEntityType; " END ),"
   Print #fileNo, addTab(5); "COALESCE(E."; g_anAcmAliasShortName; ", E."; g_anAcmEntityShortName; ") ||"
   Print #fileNo, addTab(6); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN E."; g_anAcmLrShortName; " ELSE E."; g_anAcmRlShortName; " END ) || '_OID',"
   Print #fileNo, addTab(6); "E."; g_anAcmIsEnforced
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity; " E"
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity; " L"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "E."; g_anAcmLeftEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E."; g_anAcmLeftEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E."; g_anAcmLeftEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity; " R"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "E."; g_anAcmRightEntitySection; " = R."; g_anAcmEntitySection
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E."; g_anAcmRightEntityName; " = R."; g_anAcmEntityName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E."; g_anAcmRightEntityType; " = R."; g_anAcmEntityType
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "E."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(L."; g_anAcmOrParEntitySection; ", L."; g_anAcmEntitySection; ") ELSE COALESCE(R."; g_anAcmOrParEntitySection; ", R."; g_anAcmEntitySection; ") END) = c_entitySection"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(L."; g_anAcmOrParEntityName; ", L."; g_anAcmEntityName; ") ELSE COALESCE(R."; g_anAcmOrParEntityName; ", R."; g_anAcmEntityName; ") END) = c_entityName"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN L."; g_anAcmEntityType; " ELSE R."; g_anAcmEntityType; " END) = c_entityType"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "COALESCE(E."; g_anAcmMaxLeftCardinality; ",0) = 1"
   Print #fileNo, addTab(7); "OR"
   Print #fileNo, addTab(6); "COALESCE(E."; g_anAcmMaxRightCardinality; ",0) = 1"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(3); "),"
   Print #fileNo, addTab(4); "V_FksByTab"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "srcOrParentEntitySection,"
   Print #fileNo, addTab(4); "srcOrParentEntityName,"
   Print #fileNo, addTab(4); "srcEntityType,"
   Print #fileNo, addTab(4); "tgtOrParentEntitySection,"
   Print #fileNo, addTab(4); "tgtOrParentEntityName,"
   Print #fileNo, addTab(4); "tgtEntityType,"
   Print #fileNo, addTab(4); "fkColName,"
   Print #fileNo, addTab(4); "isEnforced,"
   Print #fileNo, addTab(4); "srcTabName,"
   Print #fileNo, addTab(4); "srcTabSchema,"
   Print #fileNo, addTab(4); "tgtTabName,"
   Print #fileNo, addTab(4); "tgtTabSchema,"
   Print #fileNo, addTab(4); "isSelfReference"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AS"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "E.srcOrParentEntitySection,"
   Print #fileNo, addTab(5); "E.srcOrParentEntityName,"
   Print #fileNo, addTab(5); "E.srcEntityType,"
   Print #fileNo, addTab(5); "E.tgtOrParentEntitySection,"
   Print #fileNo, addTab(5); "E.tgtOrParentEntityName,"
   Print #fileNo, addTab(5); "E.tgtEntityType,"
   Print #fileNo, addTab(5); "E.fkColName,"
   Print #fileNo, addTab(5); "E.isEnforced,"
   Print #fileNo, addTab(5); "P_SRC."; g_anPdmTableName; ","
   Print #fileNo, addTab(5); "P_SRC."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(5); "P_TGT."; g_anPdmTableName; ","
   Print #fileNo, addTab(5); "P_TGT."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(5); "CAST((CASE WHEN P_TGT."; g_anPdmFkSchemaName; " = P_SRC."; g_anPdmFkSchemaName; " AND P_TGT."; g_anPdmTableName; " = P_SRC."; g_anPdmTableName; " THEN 1 ELSE 0 END) AS "; g_dbtBoolean; ")"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "V_FkCandidates E"
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNameLdmTable; " L_SRC"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "E.srcEntityType = L_SRC."; g_anAcmEntityType
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.srcOrParentEntityName = L_SRC."; g_anAcmEntityName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.srcOrParentEntitySection = L_SRC."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNamePdmTable; " P_SRC"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "L_SRC."; g_anLdmTableName; " = P_SRC."; g_anPdmLdmFkTableName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_SRC."; g_anLdmSchemaName; " = P_SRC."; g_anPdmLdmFkSchemaName
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNameLdmTable; " L_TGT"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "E.tgtEntityType = L_TGT."; g_anAcmEntityType
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.tgtOrParentEntityName = L_TGT."; g_anAcmEntityName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.tgtOrParentEntitySection = L_TGT."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNamePdmTable; " P_TGT"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmTableName; " = P_TGT."; g_anPdmLdmFkTableName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmSchemaName; " = P_TGT."; g_anPdmLdmFkSchemaName
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "L_SRC."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "COALESCE(P_SRC."; g_anPoolTypeId; ", "; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "COALESCE(P_TGT."; g_anPoolTypeId; ", "; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P_SRC."; g_anOrganizationId; " = orgId_in"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P_TGT."; g_anOrganizationId; " = orgId_in"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "CAST(C.COLNAME AS VARCHAR(10)) AS c_fkColName,"
   Print #fileNo, addTab(4); "V.tgtTabSchema                 AS c_tgtTabSchema,"
   Print #fileNo, addTab(4); "V.tgtTabName                   AS c_tgtTabName,"
   Print #fileNo, addTab(4); "V.isSelfReference              AS c_hasSelfReference"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS C"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "V_FksByTab V"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "C.COLNAME = V.fkColName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.TABNAME = V.srcTabName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.TABSCHEMA = V.srcTabSchema"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "c_isNl = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "c_isGen = "; gc_dbFalse
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "C.COLNAME"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "IF c_hasSelfReference = 1 THEN"

   Print #fileNo, addTab(4); "SET v_deleteFltrTxt = v_deleteFltrTxt || (CASE WHEN v_deleteFltrTxt = '' THEN '' ELSE ' AND ' END) ||' (T.' || c_fkColName || ' IS NOT NULL AND NOT EXISTS(SELECT 1 FROM ' || RTRIM(c_tgtTabSchema) || '.' || RTRIM(c_tgtTabName) || ' T2 WHERE T.' || c_fkColName || ' = T2.oid))';"

   Print #fileNo, addTab(3); "ELSE"

   Print #fileNo, addTab(4); "SET v_stmntTxt = v_stmntTxt || ' AND (S.' || c_fkColName || ' IS NULL OR EXISTS(SELECT 1 FROM ' || RTRIM(c_tgtTabSchema) || '.' || RTRIM(c_tgtTabName) || ' T WHERE S.' || c_fkColName || ' = T.oid))';"

   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_stmntTxt IS NOT NULL THEN"
   addStatementIntoTempTable fileNo, 3, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
   genProcSectionHeader fileNo, "count the number of affected rows", 4
   Print #fileNo, addTab(4); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(4); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "if table has some 'self-reference' we need to do some specific cleanup", 3
   Print #fileNo, addTab(3); "IF c_hasSelfReference = 1 THEN"
   genProcSectionHeader fileNo, "ignore records if they correspond to the same aggregate as other ignored records", 4, True
   Print #fileNo, addTab(4); "IF (c_entityType <> '"; gc_acmEntityTypeKeyClass; "' OR c_entityName <> c_ahClassName OR c_isGen = 1 OR c_isNl = 1) THEN"
   Print #fileNo, addTab(5); "SET v_stmntTxt = 'DELETE FROM ' || RTRIM(c_tgtTabSchemaName) || '.' || RTRIM(c_tabName) || ' WHERE "; g_anAhOid; " IN (' ||"
   Print #fileNo, addTab(7); "'SELECT DISTINCT S."; g_anAhOid; " FROM ' || RTRIM(c_srcTabSchemaName) || '.' || RTRIM(c_tabName) || ' S LEFT OUTER JOIN ' ||"
   Print #fileNo, addTab(7); "RTRIM(c_tgtTabSchemaName) || '.' || RTRIM(c_tabName) || ' T ON S."; g_anOid; " = T."; g_anOid; " WHERE T."; g_anOid; " IS NULL)';"

   Print #fileNo,
   addStatementIntoTempTable fileNo, 5, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(5); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(6); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 6
   Print #fileNo, addTab(6); "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;"
   Print #fileNo, addTab(6); "SET v_rowCount = v_rowCount - v_rowCount2;"
   Print #fileNo, addTab(5); "END IF;"
   Print #fileNo, addTab(4); "END IF;"
 
   genProcSectionHeader fileNo, "ignore records referring to other ignored records", 4
   Print #fileNo, addTab(4); "IF c_entityType = '"; gc_acmEntityTypeKeyClass; "' AND c_entityName = c_ahClassName AND c_isGen = 0 AND c_isNl = 0 AND v_deleteFltrTxt <> '' THEN"
   Print #fileNo, addTab(5); "SET v_stmntTxt = 'DELETE FROM ' || RTRIM(c_tgtTabSchemaName) || '.' || RTRIM(c_tabName) || ' T WHERE ' || v_deleteFltrTxt;"

   Print #fileNo,
   addStatementIntoTempTable fileNo, 5, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(5); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(6); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 6
   Print #fileNo, addTab(6); "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;"
   Print #fileNo, addTab(6); "SET v_rowCount = v_rowCount - v_rowCount2;"
   Print #fileNo, addTab(5); "END IF;"
   Print #fileNo, addTab(4); "END IF;"
 
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "update table statistics", 3
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "UPDATE SESSION.TableData T SET T.numRowsInserted = v_rowCount, T.srcTabSchemaName = c_srcTabSchemaName WHERE T.TgtTabSchemaName = c_tgtTabSchemaName AND T.tabName = c_tabName;"
   Print #fileNo, addTab(4); "IF SQLCODE <> 0 THEN"
   Print #fileNo, addTab(5); "INSERT INTO"
   Print #fileNo, addTab(6); "SESSION.TableData"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "seqNo,"
   Print #fileNo, addTab(6); "poolTypeId,"
   Print #fileNo, addTab(6); "numRowsInserted,"
   Print #fileNo, addTab(6); "srcTabSchemaName,"
   Print #fileNo, addTab(6); "TgtTabSchemaName,"
   Print #fileNo, addTab(6); "tabName"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "VALUES"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "c_fkSequenceNo,"
   Print #fileNo, addTab(6); "c_poolTypeId,"
   Print #fileNo, addTab(6); "v_rowCount,"
   Print #fileNo, addTab(6); "c_srcTabSchemaName,"
   Print #fileNo, addTab(6); "c_tgtTabSchemaName,"
   Print #fileNo, addTab(6); "c_tabName"
   Print #fileNo, addTab(5); ");"
   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "commit to toggle on logging again", 3
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "COMMIT;"
   Print #fileNo, addTab(3); "END IF;"

   genProcSectionHeader fileNo, "RUNSTATS because of run time-problems for subsequent commands", 3
   Print #fileNo, addTab(3); "SET v_stmntTxtUtil = 'CALL VL6CDBA.RUNSTATS( ''1'', ''' || RTRIM( c_tgtTabSchemaName ) || ''', ''' || RTRIM( c_tabName ) || ''', NULL, NULL, ?, ? )' ;"
 
   Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(4); "INSERT INTO "; tempTabNameStatementOrgInit; "( statement ) VALUES( v_stmntTxtUtil );"
 
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxtUtil;"
 
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "determine some table names in organization's work data pool", 1
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " AS c_entityName,"
   Print #fileNo, addTab(3); "RTRIM(PTGT."; g_anPdmFkSchemaName; ") || '.' || RTRIM(PTGT."; g_anPdmTableName; ") AS c_qualTabName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PTGT"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PTGT."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PTGT."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "(A."; g_anAcmEntityType; " = '"; CStr(gc_acmEntityTypeKeyClass); "' AND A."; g_anAcmEntitySection; " = '"; UCase(g_classes.descriptors(g_classIndexView).sectionName); "' AND A."; g_anAcmEntityName; " = '"; UCase(g_classes.descriptors(g_classIndexView).className); "')"
   Print #fileNo, addTab(5); "OR"
     Print #fileNo, addTab(4); "(A."; g_anAcmEntityType; " = '"; CStr(gc_acmEntityTypeKeyClass); "' AND A."; g_anAcmEntitySection; " = '"; UCase(g_classes.descriptors(g_classIndexEndSlot).sectionName); "' AND A."; g_anAcmEntityName; " = '"; UCase(g_classes.descriptors(g_classIndexEndSlot).className); "')"
   Print #fileNo, addTab(5); "OR"
     Print #fileNo, addTab(4); "(A."; g_anAcmEntityType; " = '"; CStr(gc_acmEntityTypeKeyClass); "' AND A."; g_anAcmEntitySection; " = '"; UCase(g_classes.descriptors(g_classIndexGeneralSettings).sectionName); "' AND A."; g_anAcmEntityName; " = '"; UCase(g_classes.descriptors(g_classIndexGeneralSettings).className); "')"
   Print #fileNo, addTab(5); "OR"
     Print #fileNo, addTab(4); "(A."; g_anAcmEntityType; " = '"; CStr(gc_acmEntityTypeKeyRel); "' AND A."; g_anAcmEntitySection; " = '"; UCase(g_relationships.descriptors(g_relIndexNsr1ValidForOrganization).sectionName); "' AND A."; g_anAcmEntityName; " = '"; UCase(g_relationships.descriptors(g_relIndexNsr1ValidForOrganization).relName); "')"
   Print #fileNo, addTab(5); "OR"
     Print #fileNo, addTab(4); "(A."; g_anAcmEntityType; " = '"; CStr(gc_acmEntityTypeKeyRel); "' AND A."; g_anAcmEntitySection; " = '"; UCase(g_relationships.descriptors(g_relIndexDisplaySlot).sectionName); "' AND A."; g_anAcmEntityName; " = '"; UCase(g_relationships.descriptors(g_relIndexDisplaySlot).relName); "')"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PTGT."; g_anOrganizationId; " = orgId_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COALESCE(PTGT."; g_anPoolTypeId; ","; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "IF c_entityName = '"; UCase(clnView); "' THEN"
   Print #fileNo, addTab(3); "SET v_qualTabNameView = c_qualTabName;"
   Print #fileNo, addTab(2); "ELSEIF c_entityName = '"; UCase(clnEndSlot); "' THEN"
   Print #fileNo, addTab(3); "SET v_qualTabNameEndSlot = c_qualTabName;"
   Print #fileNo, addTab(2); "ELSEIF c_entityName = '"; UCase(clnGeneralSettings); "' THEN"
   Print #fileNo, addTab(3); "SET v_qualTabNameGeneralSettings = c_qualTabName;"
   Print #fileNo, addTab(2); "ELSEIF c_entityName = '"; UCase(rnDisplaySlot); "' THEN"
   Print #fileNo, addTab(3); "SET v_qualTabNameDisplaySlot = c_qualTabName;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_qualTabNameNsrValidForOrganization = c_qualTabName;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,

   genProcSectionHeader fileNo, "create NSR1-Views", 1
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO ' || v_qualTabNameView || '(' ||"

   Dim transformation As AttributeListTransformation
   initAttributeTransformation transformation, 4, , True, , "'", , , , , , , ",' ||"
   transformation.suppressAllComma = True
   setAttributeMapping transformation, 1, conIsDeletable, ""
   setAttributeMapping transformation, 2, "OVWOWB_OID", ""
   setAttributeMapping transformation, 3, conPsOid, "'" & g_anPsOid & "' ||"
   setAttributeMapping transformation, 4, conVersionId, ""
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse g_classIndexView, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomList

   Print #fileNo, addTab(1); "') SELECT ' ||"

   initAttributeTransformation transformation, 7, , True, , "'", , , , , , , "' ||"
   transformation.suppressAllComma = True
   setAttributeMapping transformation, 1, conIsDeletable, ""
   setAttributeMapping transformation, 2, "OVWOWB_OID", ""
   setAttributeMapping transformation, 3, conIsStandard, "'1,' ||"
   setAttributeMapping transformation, 4, conName, "'''NSR1'',' ||"
   setAttributeMapping transformation, 5, conOid, "'NEXTVAL FOR " & qualSeqNameOid & ",' ||"
   setAttributeMapping transformation, 6, conPsOid, "'PS." & g_anOid & " ' ||"
   setAttributeMapping transformation, 7, conVersionId, ""
   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse g_classIndexView, eactClass, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomList
   Print #fileNo, addTab(1); "'FROM ' ||"
 '  Print #fileNo, addTab(2); "'"; "SESSION.PsOids"; " PS '"
   Print #fileNo, addTab(2); "'"; g_qualTabNameProductStructure; " PS WHERE PS."; g_anOid; " IN (' || v_psOidList || ') AND PS."; g_anIsUnderConstruction; " = 0 ' "
   Print #fileNo, addTab(1); ";"

   Print #fileNo,
   addStatementIntoTempTable fileNo, 1, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET v_rowCount = v_rowCount + v_rowCount2;"
   Print #fileNo, addTab(1); "END IF;"

   Dim relIndexDisplaySlot As Integer
   relIndexDisplaySlot = getRelIndexByName(rxnDisplaySlot, rnDisplaySlot)

   genProcSectionHeader fileNo, "Create DisplaySlots for NSR1-Slots"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO ' || v_qualTabNameDisplaySlot || '(' ||"

   initAttributeTransformation transformation, 4, , True, , "'", , , , , , , ",' ||"
   setAttributeMapping transformation, 1, conCreateTimestamp, ""
   setAttributeMapping transformation, 2, conLastUpdateTimestamp, ""
   setAttributeMapping transformation, 3, conPsOid, "'" & g_anPsOid & "' ||"
   setAttributeMapping transformation, 4, conVersionId, ""
   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse relIndexDisplaySlot, eactRelationship, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomList

   Print #fileNo, addTab(1); "') SELECT ' ||"

   initAttributeTransformation transformation, 12, , True, , "'", , , , , , , "' ||"
   transformation.suppressAllComma = True
   setAttributeMapping transformation, 1, conCreateTimestamp, ""
   setAttributeMapping transformation, 2, conLastUpdateTimestamp, ""
   setAttributeMapping transformation, 3, conIsDeletable, ""
   setAttributeMapping transformation, 4, "OVWOWB_OID", ""
   setAttributeMapping transformation, 5, conOid, "'NEXTVAL FOR " & qualSeqNameOid & ",' ||"
   setAttributeMapping transformation, 6, conSequenceNumber, "'E.NSR1ORDER,' ||"
   setAttributeMapping transformation, 7, "VIW_OID", "'V." & g_anOid & ",' ||"
   setAttributeMapping transformation, 8, "ESL_OID", "'E." & g_anOid & ",' ||"
   setAttributeMapping transformation, 9, conCreateUser, "'N." & g_anCreateUser & ",' ||"
   setAttributeMapping transformation, 10, conUpdateUser, "'N." & g_anUpdateUser & ",' ||"
   setAttributeMapping transformation, 11, conPsOid, "'V." & g_anPsOid & " ' ||"
   setAttributeMapping transformation, 12, conVersionId, ""

   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse relIndexDisplaySlot, eactRelationship, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomList

   Print #fileNo, addTab(1); "'FROM ' || v_qualTabNameView || ' V ' ||"
   Print #fileNo, addTab(1); "'INNER JOIN ' || v_qualTabNameNsrValidForOrganization || ' N ' ||"
   Print #fileNo, addTab(1); "'ON V."; g_anPsOid; " = N."; g_anPsOid; " ' ||"
   Print #fileNo, addTab(1); "'AND N.ORG_OID = ' || v_orgOid || ' ' ||"
   Print #fileNo, addTab(1); "'INNER JOIN ' || v_qualTabNameEndSlot || ' E ' ||"
   Print #fileNo, addTab(1); "'ON N.ESL_OID = E."; g_anOid; " ' ||"
   Print #fileNo, addTab(1); "'WHERE V."; g_anName; " = ''NSR1''';"

   Print #fileNo,
   addStatementIntoTempTable fileNo, 1, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET v_rowCount = v_rowCount + v_rowCount2;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "Create DisplaySlots for DUP-Slots"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO ' || v_qualTabNameDisplaySlot || '(' ||"

   initAttributeTransformation transformation, 4, , True, , "'", , , , , , , ",' ||"
   transformation.suppressAllComma = True
   setAttributeMapping transformation, 1, conCreateTimestamp, ""
   setAttributeMapping transformation, 2, conLastUpdateTimestamp, ""
   setAttributeMapping transformation, 3, conPsOid, "'" & g_anPsOid & "' ||"
   setAttributeMapping transformation, 4, conVersionId, ""
   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse relIndexDisplaySlot, eactRelationship, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomList

   Print #fileNo, addTab(1); "') SELECT ' ||"

   initAttributeTransformation transformation, 12, , True, , "'", , , , , , , "' ||"
   transformation.suppressAllComma = True
   setAttributeMapping transformation, 1, conCreateTimestamp, ""
   setAttributeMapping transformation, 2, conLastUpdateTimestamp, ""
   setAttributeMapping transformation, 3, conIsDeletable, ""
   setAttributeMapping transformation, 4, "OVWOWB_OID", ""
   setAttributeMapping transformation, 5, conOid, "'NEXTVAL FOR " & qualSeqNameOid & ",' ||"
   setAttributeMapping transformation, 6, conSequenceNumber, "'(SELECT MAX(" & g_anSequenceNumber & ") +1 FROM ' || v_qualTabNameDisplaySlot || ' S WHERE S." & g_anPsOid & " = E." & g_anPsOid & "),' ||"
   setAttributeMapping transformation, 7, "VIW_OID", "'V." & g_anOid & ",' ||"
   setAttributeMapping transformation, 8, "ESL_OID", "'E." & g_anOid & ",' ||"
   setAttributeMapping transformation, 9, conCreateUser, "'E." & g_anCreateUser & ",' ||"
   setAttributeMapping transformation, 10, conUpdateUser, "'E." & g_anUpdateUser & ",' ||"
   setAttributeMapping transformation, 11, conPsOid, "'E." & g_anPsOid & " ' ||"
   setAttributeMapping transformation, 12, conVersionId, ""

   tabColumns = nullEntityColumnDescriptors
   genTransformedAttrListForEntityWithColReuse relIndexDisplaySlot, eactRelationship, transformation, tabColumns, fileNo, ddlType, , , 2, , , edomList

   Print #fileNo, addTab(1); "'FROM ' || v_qualTabNameView || ' V ' ||"
   Print #fileNo, addTab(1); "'INNER JOIN ' || v_qualTabNameEndSlot || ' E ' ||"
   Print #fileNo, addTab(1); "'ON V."; g_anPsOid; " = E."; g_anPsOid; " ' ||"
   Print #fileNo, addTab(1); "'WHERE E."; g_anIsDuplicating; " = 1 AND V."; g_anName; " = ''NSR1''';"

   Print #fileNo,
   addStatementIntoTempTable fileNo, 1, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET v_rowCount = v_rowCount + v_rowCount2;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "map ENP <-> EBP in organization's work datapool", 1
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameOrgInitEnp; "(' || RTRIM(CHAR(orgId_in)) || ', ?)';"
   Print #fileNo,
   addStatementIntoTempTable fileNo, 1, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"

   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"

   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount2"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "accumulate the number of affected rows", 2
   Print #fileNo, addTab(2); "SET v_rowCount = v_rowCount + v_rowCount2;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "report timestamp of last FTO in GENERALSETTINGS", 1
   Print #fileNo, addTab(1); "SET v_stmntTxt ="
   Print #fileNo, addTab(2); "'UPDATE ' ||"
   Print #fileNo, addTab(2); "v_qualTabNameGeneralSettings ||"
   Print #fileNo, addTab(1); "' SET ' ||"
   Print #fileNo, addTab(2); "'LASTCENTRALDATATRANSFERBEGIN = CURRENT TIMESTAMP,' ||"
   Print #fileNo, addTab(2); "'LASTCENTRALDATATRANSFERCOMMIT = CURRENT TIMESTAMP'"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   addStatementIntoTempTable fileNo, 1, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
 
   genProcSectionHeader fileNo, "accumulate the number of affected rows", 2
   Print #fileNo, addTab(2); "SET v_rowCount = v_rowCount + v_rowCount2;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "unlock all factory productive data pools", 1
   Dim qualProcNameResetLock As String
   qualProcNameResetLock = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , "SHAREDREAD")
 
   Print #fileNo, addTab(1); "FOR dpLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "OR.ORGOID AS c_orgOid,"
   Print #fileNo, addTab(3); "SC."; g_anPoolTypeId; " AS c_accessModeId,"
   Print #fileNo, addTab(3); "PS."; g_anOid; " AS c_psOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SESSION.PsOids"; " PS,"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " OR,"
   Print #fileNo, addTab(3); g_qualTabNamePdmPrimarySchema; " SC"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(OR.ID = SC."; g_anOrganizationId; ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(SC."; g_anPoolTypeId; " = "; CStr(g_productiveDataPoolId); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(SC."; g_anOrganizationId; " = "; CStr(g_primaryOrgId); ")"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "unlock data pool", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameResetLock; "(''' ||"
   Print #fileNo, addTab(12); "RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',' ||"
   Print #fileNo, addTab(12); "'''<admin>'',' ||"
   Print #fileNo, addTab(12); "'''' || CAST(CASE COALESCE(CURRENT USER, '') WHEN '' THEN '<unknown>' ELSE CURRENT USER END AS "; g_dbtUserId; ") || ''',' ||"
   Print #fileNo, addTab(12); "'''ORGINIT'',' ||"
   Print #fileNo, addTab(12); "'?)';"
   Print #fileNo,
   addStatementIntoTempTable fileNo, 2, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_numDataPools"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "ignore if lock on data pool could not be released - should not happen", 3
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "unlock all organization's data pools", 1
 '  Dim qualProcNameResetLock As String
   qualProcNameResetLock = genQualProcName(g_sectionIndexDbMeta, spnResetRel2ProdLock, ddlType, , , , "EXCLUSIVEWRITE")
 
   Print #fileNo, addTab(1); "FOR dpLoop AS csr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "OR.ORGOID AS c_orgOid,"
   Print #fileNo, addTab(3); "SC."; g_anPoolTypeId; " AS c_accessModeId,"
   Print #fileNo, addTab(3); "PS."; g_anOid; " AS c_psOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "SESSION.PsOids"; " PS,"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization; " OR,"
   Print #fileNo, addTab(3); g_qualTabNamePdmPrimarySchema; " SC"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "(OR.ID = SC."; g_anOrganizationId; ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(SC."; g_anPoolTypeId; " IN ( "; CStr(g_workDataPoolId); ","; CStr(g_productiveDataPoolId); " ) )"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "(SC."; g_anOrganizationId; " = orgId_in)"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "unlock data pool", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcNameResetLock; "(''' ||"
   Print #fileNo, addTab(12); "RTRIM(CHAR(c_orgOid)) || ',' || RTRIM(CHAR(c_psOid)) || ',' || RTRIM(CHAR(c_accessModeId)) || ''',' ||"
   Print #fileNo, addTab(12); "'''<admin>'',' ||"
   Print #fileNo, addTab(12); "'''' || CAST(CASE COALESCE(CURRENT USER, '') WHEN '' THEN '<unknown>' ELSE CURRENT USER END AS "; g_dbtUserId; ") || ''',' ||"
   Print #fileNo, addTab(12); "'''ORGINIT'',' ||"
   Print #fileNo, addTab(12); "'?)';"
   Print #fileNo,
   addStatementIntoTempTable fileNo, 2, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_numDataPools"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "ignore if lock on data pool could not be released - should not happen", 3
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "copy data to organization's non-work data pools"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "PSRC."; g_anPdmFkSchemaName; " AS c_srcTabSchemaName,"
   Print #fileNo, addTab(3); "PTGT."; g_anPdmFkSchemaName; " AS c_tgtTabSchemaName,"
   Print #fileNo, addTab(3); "PTGT."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "PTGT."; g_anPoolTypeId; " AS c_poolTypeId,"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " AS c_fkSequenceNo"

   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PTGT"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PTGT."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PTGT."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PSRC"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PSRC."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PSRC."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PTGT."; g_anOrganizationId; " = orgId_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PTGT."; g_anPoolTypeId; " IN ("; CStr(g_productiveDataPoolId); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PSRC."; g_anOrganizationId; " = orgId_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PSRC."; g_anPoolTypeId; " = "; genPoolId(g_workDataPoolIndex, ddlType)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "(CASE WHEN A."; g_anAhCid; " IS NULL THEN 0 ELSE 1 END) DESC,"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " ASC,"
   Print #fileNo, addTab(3); "PTGT."; g_anPoolTypeId; " ASC"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO ' || RTRIM(c_tgtTabSchemaName) || '.' || RTRIM(c_tabName) || ' SELECT S.* FROM ' || RTRIM(c_srcTabSchemaName) || '.' || RTRIM(c_tabName) || ' S';"
   Print #fileNo,
   addStatementIntoTempTable fileNo, 2, tempTabNameStatementOrgInit, genTimeStampsDuringOrgInit
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"

   genProcSectionHeader fileNo, "count the number of affected rows", 3
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "update table statistics", 2
   Print #fileNo, addTab(2); "UPDATE SESSION.TableData T SET T.numRowsInserted = v_rowCount, T.srcTabSchemaName = c_srcTabSchemaName WHERE T.TgtTabSchemaName = c_tgtTabSchemaName AND T.tabName = c_tabName;"
   Print #fileNo, addTab(2); "IF SQLCODE <> 0 THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); "SESSION.TableData"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "seqNo,"
   Print #fileNo, addTab(4); "poolTypeId,"
   Print #fileNo, addTab(4); "numRowsInserted,"
   Print #fileNo, addTab(4); "srcTabSchemaName,"
   Print #fileNo, addTab(4); "tgtTabSchemaName,"
   Print #fileNo, addTab(4); "tabName"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "c_fkSequenceNo,"
   Print #fileNo, addTab(4); "c_poolTypeId,"
   Print #fileNo, addTab(4); "v_rowCount,"
   Print #fileNo, addTab(4); "c_srcTabSchemaName,"
   Print #fileNo, addTab(4); "c_tgtTabSchemaName,"
   Print #fileNo, addTab(4); "c_tabName"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "RUNSTATS because of run time-problems for subsequent commands", 2
   Print #fileNo, addTab(2); "SET v_stmntTxtUtil = 'CALL VL6CDBA.RUNSTATS( ''1'', ''' || RTRIM( c_tgtTabSchemaName ) || ''', ''' || RTRIM( c_tabName ) || ''', NULL, NULL, ?, ? )' ;"
 
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(4); "INSERT INTO "; tempTabNameStatementOrgInit; "( statement ) VALUES( v_stmntTxtUtil );"

   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxtUtil;"
 
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "commit to toggle on logging again"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "COMMIT;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"

   ' ############################################################

   genProcSectionHeader fileNo, "synchronize MQTs", fileNo

   Dim qualProcNameMqtSync As String
   qualProcNameMqtSync = genQualProcName(g_sectionIndexDbAdmin, spnLrtMqtSync, ddlType)

   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameMqtSync; "(' || RTRIM(CHAR(orgId_in)) || ', ?, ?, ?)';"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   genProcSectionHeader fileNo, "declare variables", 3, True
   genVarDecl fileNo, "v_orgCountMqt", "INTEGER", "NULL", 3
   genVarDecl fileNo, "v_tabCountMqt", "INTEGER", "NULL", 3
   genVarDecl fileNo, "v_rowCountMqt", "BIGINT", "NULL", 3

   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_orgCountMqt,"
   Print #fileNo, addTab(4); "v_tabCountMqt,"
   Print #fileNo, addTab(4); "v_rowCountMqt"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCountMqt;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   ' ############################################################

   genProcSectionHeader fileNo, "re-enable disabled triggers for organization"

   Dim qualProcedureNameTrigEnable As String
   qualProcedureNameTrigEnable = genQualProcName(g_sectionIndexDbAdmin, spnTriggerEnable, ddlType)

   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameTrigEnable; "(NULL, NULL, NULL, NULL, ' || RTRIM(CHAR(orgId_in)) || ', NULL, ?, ?)';"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   genProcSectionHeader fileNo, "declare variables", 3, True
   genVarDecl fileNo, "v_trigCount", "INTEGER", "NULL", 3
   genVarDecl fileNo, "v_failCount", "INTEGER", "NULL", 3

   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_trigCount,"
   Print #fileNo, addTab(4); "v_failCount"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"


 End Sub
 
 
 
 
 Private Sub genDbAdminDdlOrgInit2( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     Exit Sub
   End If


   Dim qualProcedureNameOrgInit As String
   qualProcedureNameOrgInit = genQualProcName(g_sectionIndexDbAdmin, spnOrgInit, ddlType)
 
   Dim tempTabNameStatementOrgInit As String
   tempTabNameStatementOrgInit = tempTabNameStatement & "OrgInit"

   ' ############################################################

   genProcSectionHeader fileNo, "revalidate any object which may be invalidated"
 
   Dim qualProcedureNameRevalidate As String
   qualProcedureNameRevalidate = genQualProcName(g_sectionIndexDbAdmin, spnRevalidate, ddlType)

   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcedureNameRevalidate; "(2, ?)';"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "END IF;"
   ' ############################################################

   genProcSectionHeader fileNo, "set GRANTs on new DB-objects"

   Dim qualProcNameSetGrants As String
   qualProcNameSetGrants = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "Fltr", eondmNone)
 
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameSetGrants; "(2, ''"; g_allSchemaNamePattern; "' || RIGHT(DIGITS(orgId_in),2) || '%'', NULL, ?)';"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "v_stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); ";"

   Print #fileNo, addTab(1); "END IF;"

   ' ############################################################
 
   genProcSectionHeader fileNo, "call ""GENWORKSPACE""", fileNo

   Dim qualProcNameGenWs As String
   qualProcNameGenWs = genQualProcName(g_sectionIndexMeta, spnGenWorkspaceWrapper, ddlType, , , , "_WITHERROR", False)

   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameGenWs; "(1, ' || RTRIM(CHAR(orgId_in)) || ', NULL, NULL, 0, 0, ?)';"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES (v_stmntTxt);"
   If genTimeStampsDuringOrgInit Then
     Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementOrgInit; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   genProcSectionHeader fileNo, "declare variables", 3, True
   genVarDecl fileNo, "v_callCount", "INTEGER", "NULL", 3

   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_callCount"
   Print #fileNo, addTab(3); ";"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   ' ############################################################

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmtCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementOrgInit
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "DECLARE tabCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "poolTypeId,"
   Print #fileNo, addTab(5); "numRowsDeleted,"
   Print #fileNo, addTab(5); "numRowsInserted,"
   Print #fileNo, addTab(5); "srcTabSchemaName,"
   Print #fileNo, addTab(5); "tgtTabSchemaName,"
   Print #fileNo, addTab(5); "tabName"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "SESSION.TableData"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "srcTabSchemaName IS NOT NULL"
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "poolTypeId ASC,"
   Print #fileNo, addTab(5); "tgtTabSchemaName ASC,"
   Print #fileNo, addTab(5); "tabName ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor(s) open for application", 3
   Print #fileNo, addTab(3); "OPEN stmtCursor;"
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(4); "OPEN tabCursor;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameOrgInit, ddlType, 1, "mode_in", "orgId_in", "rowCount_out"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 End Sub
 
 
 Private Sub genDbAdminDdlMessageUpdate( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     Exit Sub
   End If

   Dim relIndexMessageSeverity As Integer
   relIndexMessageSeverity = getRelIndexByName(rxnMessageSeverity, rnMessageSeverity)

   Dim qualProcedureNameSetMessageSeverity As String
   qualProcedureNameSetMessageSeverity = genQualProcName(g_sectionIndexDbAdmin, spnSetMessageSeverity, ddlType)

   ' ####################################################################################################################
   ' #    SP for setting message severity in ALL MPCs
   ' ####################################################################################################################

   printSectionHeader "SP for setting message severity in ALL MPCs", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetMessageSeverity
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup-statements, '1' list and execute statements, '2' execute statements only"
   genProcParm fileNo, "IN", "orgOid_in", g_dbtOid, True, "(optional) OID of the Organization to set message severity for (default: ALL)"
   genProcParm fileNo, "IN", "oid_in", g_dbtOid, True, "OID to use for new record"
   genProcParm fileNo, "IN", "msgOid_in", g_dbtOid, True, "OID to Message to set severity for"
   genProcParm fileNo, "IN", "divOid_in", g_dbtOid, True, "OID to Division to set severity for"
   genProcParm fileNo, "IN", "severityId_in", "BIGINT", True, "ID of the Severity to set"
   genProcParm fileNo, "IN", "cdUserId_in", g_dbtUserId, True, "UserId of the user used for " & g_anCreateUser & " / " & g_anUpdateUser
   genProcParm fileNo, "IN", "timestamp_in", "TIMESTAMP", True, "(optional) timestamp to use for " & g_anCreateTimestamp & " / " & g_anLastUpdateTimestamp & " (default: CURRENT TIMESTAMP)"
   genProcParm fileNo, "IN", "overwrite_in", g_dbtBoolean, True, "if set to '1' overwrite severity if record exists for given MSG_OID and DIV_OID"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records INSERTED / UPDATED"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(32000)", "NULL"
   genVarDecl fileNo, "v_recordCount", "INTEGER", "0"
   genSigMsgVarDecl fileNo
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE c CURSOR FOR v_stmnt;"

   Dim tempTabNameStatementSetMsgSev As String
   tempTabNameStatementSetMsgSev = tempTabNameStatement & "SetMsgSev"

   genDdlForTempStatement fileNo, 1, True, 400, True, True, True, , "SetMsgSev"

   genSpLogProcEnter fileNo, qualProcedureNameSetMessageSeverity, ddlType, , "mode_in", "orgOid_in", "oid_in", "msgOid_in", "divOid_in", "severityId_in", "'cdUserId_in", "timestamp_in", "overwrite_in", "rowCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(2); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "verify timestamp"
   Print #fileNo, addTab(2); "SET timestamp_in = COALESCE(timestamp_in, CURRENT TIMESTAMP);"

   genProcSectionHeader fileNo, "loop over Organizations"
   Print #fileNo, addTab(1); "FOR orgLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(4); "P."; g_anPdmTableName; " AS c_tableName"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmOrganization; " O"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = O.ID"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "COALESCE(orgOid_in, O.ORGOID) = O.ORGOID"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "P."; g_anPoolTypeId; " IS NULL"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityId; " = '"; getRelIdStrByIndex(relIndexMessageSeverity); "'"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "O.ORGOID"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "check whether record exists for this organization and given MSG_OID and DIV_OID", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'SELECT COUNT(*) FROM ' || RTRIM(c_schemaName) || '.' || c_tableName || ' WHERE ' ||"
   Print #fileNo, addTab(3); "'MSG_OID = ' || RTRIM(CHAR(msgOid_in)) || ' AND DIV_OID = ' || RTRIM(CHAR(divOid_in));"
   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(2); "OPEN c;"
   Print #fileNo, addTab(2); "FETCH"
   Print #fileNo, addTab(3); "c"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_recordCount"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "CLOSE c;"

   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_stmntTxt = NULL;"
   Print #fileNo, addTab(2); "IF v_recordCount > 0 THEN"
   genProcSectionHeader fileNo, "if record exists overwrite if requested", 3, True
   Print #fileNo, addTab(3); "IF overwrite_in = 1 THEN"

   Print #fileNo, addTab(4); "SET v_stmntTxt = 'UPDATE ' || RTRIM(c_schemaName) || '.' || c_tableName || ' SET ' ||"
   Print #fileNo, addTab(5); "'SEVERITY_ID = ' || RTRIM(CHAR(severityId_in)) || ', "; g_anUpdateUser; " = ''' || cdUserId_in || ''', ' ||"
   Print #fileNo, addTab(5); "'"; g_anLastUpdateTimestamp; " = ''' || CHAR(timestamp_in) || ''', "; g_anVersionId; " = "; g_anVersionId; "+1 ' ||"
   Print #fileNo, addTab(5); "'WHERE MSG_OID = ' || RTRIM(CHAR(msgOid_in)) || ' AND DIV_OID = ' || RTRIM(CHAR(divOid_in));"

   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "ELSE"
   genProcSectionHeader fileNo, "if record does not exist insert it", 3, True

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   Dim transformation As AttributeListTransformation
   initAttributeTransformation transformation, 0, , True
   transformation.trimRight = False
   genTransformedAttrListForEntityWithColReuse relIndexMessageSeverity, eactRelationship, transformation, tabColumns, fileNo, ddlType, , , 3, , , edomNone

   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'INSERT INTO ' || RTRIM(c_schemaName) || '.' || c_tableName || '(' ||"

   Dim k As Integer
   For k = 1 To tabColumns.numDescriptors
       Print #fileNo, addTab(5); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
   Next k

   Print #fileNo, addTab(4); "') VALUES (' ||"

   For k = 1 To tabColumns.numDescriptors
       If tabColumns.descriptors(k).columnName = g_anOid Then
         Print #fileNo, addTab(5); "RTRIM(CHAR(oid_in))";
       ElseIf tabColumns.descriptors(k).columnName = "SEVERITY_ID" Then
         Print #fileNo, addTab(5); "RTRIM(CHAR(severityId_in))";
       ElseIf (tabColumns.descriptors(k).columnName = g_anCreateUser Or tabColumns.descriptors(k).columnName = g_anUpdateUser) Then
         Print #fileNo, addTab(5); "'''' || cdUserId_in || ''''";
       ElseIf (tabColumns.descriptors(k).columnName = g_anCreateTimestamp Or tabColumns.descriptors(k).columnName = g_anLastUpdateTimestamp) Then
         Print #fileNo, addTab(5); "'TIMESTAMP(''' || CHAR(timestamp_in) || ''')'";
       ElseIf tabColumns.descriptors(k).columnName = "MSG_OID" Then
         Print #fileNo, addTab(5); "RTRIM(CHAR(msgOid_in))";
       ElseIf tabColumns.descriptors(k).columnName = "DIV_OID" Then
         Print #fileNo, addTab(5); "RTRIM(CHAR(divOid_in))";
       ElseIf tabColumns.descriptors(k).columnName = g_anVersionId Then
         Print #fileNo, addTab(5); "'1'";
       Else
         Print #fileNo, addTab(5); "'"; tabColumns.descriptors(k).columnName; "'"
       End If

       Print #fileNo, " ||"; IIf(k = tabColumns.numDescriptors, "", " ',' ||")
   Next k
   Print #fileNo, addTab(4); "')'"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_stmntTxt IS NOT NULL THEN"
   Print #fileNo, addTab(3); "IF mode_in <= 1 THEN"
   genProcSectionHeader fileNo, "store statement in temporary table", 4, True
   Print #fileNo, addTab(4); "INSERT INTO"
   Print #fileNo, addTab(5); tempTabNameStatementSetMsgSev
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "STATEMENT"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(4); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "v_stmntTxt"
   Print #fileNo, addTab(4); ");"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "IF mode_in >= 1 THEN"
   genProcSectionHeader fileNo, "update record", 4, True
   Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "DECLARE resCursor CURSOR WITH RETURN FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); tempTabNameStatementSetMsgSev
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "seqNo"
   Print #fileNo, addTab(3); "FOR READ ONLY"
   Print #fileNo, addTab(2); ";"
   genProcSectionHeader fileNo, "leave cursor open for application", 2
   Print #fileNo, addTab(2); "OPEN resCursor;"
   Print #fileNo, addTab(1); "END;"

   genSpLogProcExit fileNo, qualProcedureNameSetMessageSeverity, ddlType, , "mode_in", "orgOid_in", "oid_in", "msgOid_in", "divOid_in", "severityId_in", "'cdUserId_in", "timestamp_in", "overwrite_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 End Sub
 
 
 ' ### ENDIF IVK ###
 Sub genDbAdminDdlByPool( _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   If thisOrgIndex < 1 Or thisPoolIndex < 1 Then
     Exit Sub
   End If

   If Not g_pools.descriptors(thisPoolIndex).supportAcm Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbAdmin, processingStepAdmin, ddlType, thisOrgIndex, thisPoolIndex, , phaseDbSupport)
 
   Dim qualProcedureNameCleanupGlobal As String
   qualProcedureNameCleanupGlobal = genQualProcName(g_sectionIndexDbAdmin, spnCleanData, ddlType)

   Dim qualProcedureNameCleanupLocal As String
   qualProcedureNameCleanupLocal = genQualProcName(g_sectionIndexAliasLrt, spnCleanData, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader "SP for executing data cleanup jobs (wrapper with unique name / no overloadding)", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCleanupLocal
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' list cleanup-statements, '1' list and execute statements, '2' execute statements only"
   genProcParm fileNo, "IN", "jobCategory_in", "VARCHAR(20)", True, "category of the clean-job to execute"
   genProcParm fileNo, "IN", "jobName_in", "VARCHAR(20)", True, "name of the clean-job to execute"
   genProcParm fileNo, "IN", "level_in", "INTEGER", True, "(optional) level to use for column filtering"
   genProcParm fileNo, "IN", "parameter1_in", "VARCHAR(30)", True, "(optional) parameter 1 to use in condition term for job"
   genProcParm fileNo, "IN", "parameter2_in", "VARCHAR(30)", True, "(optional) parameter 2 to use in condition term for job"
   genProcParm fileNo, "IN", "parameter3_in", "VARCHAR(30)", True, "(optional) parameter 3 to use in condition term for job"
 
   genProcParm fileNo, "OUT", "stmntCount_out", "INTEGER", True, "number of statements for this job"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows deleted in any table"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genSpLogProcEnter fileNo, qualProcedureNameCleanupLocal, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "'parameter3_in", "stmntCount_out", "rowCount_out"
 
   Print #fileNo, addTab(1); "CALL "; qualProcedureNameCleanupGlobal; "(mode_in, jobCategory_in, jobName_in, level_in, parameter1_in, parameter2_in, parameter3_in, stmntCount_out, rowCount_out);"
 
   genSpLogProcExit fileNo, qualProcedureNameCleanupLocal, ddlType, , "mode_in", "'jobCategory_in", "'jobName_in", "level_in", "'parameter1_in", "'parameter2_in", "'parameter3_in", "stmntCount_out", "rowCount_out"
 
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
 
 
 ' ### IF IVK ###
 Private Sub genDdlAddTestUser( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If Not supportAddTestUser Then
     Exit Sub
   End If
 
   On Error GoTo ErrorExit

   Dim qualTabNameMdsUserNlText As String
   qualTabNameMdsUserNlText = genQualNlTabNameByClassIndex(g_classIndexUser, ddlType)

   ' ####################################################################################################################
   ' #    SP creating Test-UserIDs
   ' ####################################################################################################################

   Dim qualProcedureNameCreateTestUser As String
   qualProcedureNameCreateTestUser = genQualProcName(g_sectionIndexDbAdmin, spnAddTestUser, ddlType)

   printSectionHeader "SP creating Test-UserIDs", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCreateTestUser
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "userCount_out", "INTEGER", True, "number of userIDs created"
   genProcParm fileNo, "OUT", "privCount_out", "INTEGER", True, "number of privileges created"
   genProcParm fileNo, "OUT", "grantCount_out", "INTEGER", False, "number of grants set"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "dupKey", "23505"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_userIdNum", "INTEGER", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(2000)", "NULL"
   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR dupKey"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
 
   Dim qualTempTabNameAddUser As String
   qualTempTabNameAddUser = tempTabNameStatement & "AddTestUser"
   genDdlForTempStatement fileNo, 1, True, 400, True, True, True, , "AddTestUser", True
 
   genSpLogProcEnter fileNo, qualProcedureNameCreateTestUser, ddlType, , "mode_in", "userCount_out", "privCount_out", "grantCount_out"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET userCount_out  = 0;"
   Print #fileNo, addTab(1); "SET privCount_out  = 0;"
   Print #fileNo, addTab(1); "SET grantCount_out = 0;"

   genProcSectionHeader fileNo, "create 251 P-user + 51 D-User"
   Print #fileNo, addTab(1); "WHILE v_userIdNum <= 250 DO"
   Print #fileNo, addTab(2); "IF v_userIdNum <= 50 THEN"

   Print #fileNo, addTab(3); "IF NOT EXISTS (SELECT 1 FROM "; g_qualTabNameUser; " WHERE "; g_anUserId; " = CAST('D' || RIGHT(DIGITS(v_userIdNum), 6) AS "; g_dbtUserId; ") OR OID = 140 + v_userIdNum) THEN"

   Print #fileNo, addTab(4); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNameUser; " (OID, "; g_anUserId; ", "; g_anUserName; ", "; g_anIsActive; ", "; g_anCreateUser; ", "; g_anUpdateUser; ") ' ||"
   Print #fileNo, addTab(12); " 'VALUES (' || RTRIM(CHAR(140 + v_userIdNum)) || ', ''D' || RIGHT(DIGITS(v_userIdNum), 6) || ''', ''NAME_D' || RIGHT(DIGITS(v_userIdNum), 6) || ''', 1, ''1'', ''MIG'')';"

   Print #fileNo, addTab(4); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(4); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(4); "SET v_stmntTxt = 'INSERT INTO "; qualTabNameMdsUserNlText; " (OID, USR_OID, "; g_anLanguageId; ", COMMENT, RESPONSIBILITY) ' ||"
   Print #fileNo, addTab(12); " 'VALUES (' || RTRIM(CHAR(1100 + v_userIdNum)) || ', ' || RTRIM(CHAR(140 + v_userIdNum)) || ', 1, ''Devtestuser' || RIGHT(DIGITS(v_userIdNum), 2) || ''', '''')';"

   Print #fileNo, addTab(4); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(4); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(4); "SET v_stmntTxt = 'INSERT INTO "; qualTabNameMdsUserNlText; " (OID, USR_OID, "; g_anLanguageId; ", COMMENT, RESPONSIBILITY) ' ||"
   Print #fileNo, addTab(12); " 'VALUES (' || RTRIM(CHAR(1200 + v_userIdNum)) || ', ' || RTRIM(CHAR(140 + v_userIdNum)) || ', 1, ''Devtestuser' || RIGHT(DIGITS(v_userIdNum), 2) || ' engl.'', '''')';"

   Print #fileNo, addTab(4); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(4); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(4); "SET userCount_out = userCount_out + 1;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   Print #fileNo, addTab(2); "IF NOT EXISTS (SELECT 1 FROM "; g_qualTabNameUser; "  WHERE "; g_anUserId; " = CAST('P' || RIGHT(DIGITS(v_userIdNum), 6) AS "; g_dbtUserId; ") OR OID = 240 + v_userIdNum) THEN"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNameUser; " (OID, "; g_anUserId; ", "; g_anUserName; ", "; g_anIsActive; ", "; g_anCreateUser; ", "; g_anUpdateUser; ") ' ||"
   Print #fileNo, addTab(11); " 'VALUES (' || RTRIM(CHAR(240 + v_userIdNum)) || ', ''P' || RIGHT(DIGITS(v_userIdNum), 6) || ''', ''NAME_P' || RIGHT(DIGITS(v_userIdNum), 6) || ''', 1, ''1'', ''MIG'')';"

   Print #fileNo, addTab(3); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(3); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'INSERT INTO "; qualTabNameMdsUserNlText; " (OID, USR_OID, "; g_anLanguageId; ", COMMENT, RESPONSIBILITY) ' ||"
   Print #fileNo, addTab(11); " 'VALUES (' || RTRIM(CHAR(1300 + v_userIdNum)) || ', ' || RTRIM(CHAR(240 + v_userIdNum)) || ', 1, ''Testuser' || RIGHT(DIGITS(v_userIdNum), 3) || ''', '''')';"

   Print #fileNo, addTab(3); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(3); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'INSERT INTO "; qualTabNameMdsUserNlText; " (OID, USR_OID, "; g_anLanguageId; ", COMMENT, RESPONSIBILITY) ' ||"
   Print #fileNo, addTab(11); " 'VALUES (' || RTRIM(CHAR(1600 + v_userIdNum)) || ', ' || RTRIM(CHAR(240 + v_userIdNum)) || ', 1, ''Testuser' || RIGHT(DIGITS(v_userIdNum), 3) || ' engl.'', '''')';"

   Print #fileNo, addTab(3); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(3); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"
 
   Print #fileNo,
   Print #fileNo, addTab(3); "SET userCount_out = userCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET v_userIdNum = v_userIdNum + 1;"
   Print #fileNo, addTab(1); "END WHILE;"

   genProcSectionHeader fileNo, "create some extra user"
   Print #fileNo, addTab(1); "FOR usrLoop AS"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_User"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(2); "numId,"
   Print #fileNo, addTab(2); "cdUserId"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "VALUES(1, 'PWEISSB')"
   Print #fileNo, addTab(3); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(2, 'PESCHAI')"
   Print #fileNo, addTab(3); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(3, 'STESTOC')"
   Print #fileNo, addTab(3); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(4, 'CHGALST')"
   Print #fileNo, addTab(3); "UNION ALL"
   Print #fileNo, addTab(3); "VALUES(5, 'RBENDRI')"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "numId    AS c_numId,"
   Print #fileNo, addTab(3); "cdUserId AS c_cdUserId"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_User"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "numId"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "IF NOT EXISTS (SELECT 1 FROM "; g_qualTabNameUser; "  WHERE "; g_anUserId; " = c_cdUserId OR OID = 490 + c_numId) THEN"

   Print #fileNo, addTab(3); "SET v_stmntTxt = 'INSERT INTO "; g_qualTabNameUser; " (OID, "; g_anUserId; ", "; g_anUserName; ", "; g_anIsActive; ", "; g_anCreateUser; ", "; g_anUpdateUser; ") VALUES (' || RTRIM(CHAR(490 + c_numId)) || ', ''' || c_cdUserId || ''', ''NAME_' || c_cdUserId || ''', 1, ''1'', ''MIG'')';"

   Print #fileNo, addTab(3); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(3); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'INSERT INTO "; qualTabNameMdsUserNlText; " (OID, USR_OID, "; g_anLanguageId; ", COMMENT, RESPONSIBILITY, "; g_anVersionId; ") values (' || RTRIM(CHAR(1550 + c_numId)) || ', ' || RTRIM(CHAR(490 + c_numId)) || ', 1, ''' || c_cdUserId || ''', '''', 1)';"

   Print #fileNo, addTab(3); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(3); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'INSERT INTO "; qualTabNameMdsUserNlText; " (OID, USR_OID, "; g_anLanguageId; ", COMMENT, RESPONSIBILITY, "; g_anVersionId; ") values (' || RTRIM(CHAR(1850 + c_numId)) || ', ' || RTRIM(CHAR(490 + c_numId)) || ', 2, ''' || c_cdUserId || ' engl.'', '''', 1)';"

   Print #fileNo, addTab(3); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(3); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"

   Print #fileNo,
   Print #fileNo, addTab(3); "SET userCount_out = userCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "add DB-Privileges"
   Print #fileNo, addTab(1); "FOR usrLoop AS"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_Priv"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "sequenceNo,"
   Print #fileNo, addTab(3); "environment,"
   Print #fileNo, addTab(3); "opType,"
   Print #fileNo, addTab(3); "objectType,"
   Print #fileNo, addTab(3); "schemaName,"
   Print #fileNo, addTab(3); "objectName,"
   Print #fileNo, addTab(3); "filter,"
   Print #fileNo, addTab(3); "granteeType,"
   Print #fileNo, addTab(3); "grantee,"
   Print #fileNo, addTab(3); "privilege,"
   Print #fileNo, addTab(3); "withGrant"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("

   Dim i As Integer
   Dim firstPriv As Boolean
   firstPriv = True
   For i = 1 To g_privileges.numDescriptors
       If g_privileges.descriptors(i).environment = "T" Then
         If Not firstPriv Then
           Print #fileNo, addTab(4); "UNION ALL"
         Else
           firstPriv = False
         End If
         Print #fileNo, addTab(3); "VALUES("; _
                                   IIf(g_privileges.descriptors(i).sequenceNumber < 0, "CAST(NULL AS SMALLINT)", "SMALLINT(" & CStr(g_privileges.descriptors(i).sequenceNumber) & ")"); ", "; _
                                   "'"; g_privileges.descriptors(i).environment; "', "; _
                                   "'"; g_privileges.descriptors(i).operation; "', "; _
                                   "'"; g_privileges.descriptors(i).objectType; "', "; _
                                   IIf(g_privileges.descriptors(i).schemaName = "", "CAST(NULL AS VARCHAR(1))", "'" & g_privileges.descriptors(i).schemaName & "'"); ", "; _
                                   IIf(g_privileges.descriptors(i).objectName = "", "CAST(NULL AS VARCHAR(1))", "'" & g_privileges.descriptors(i).objectName & "'"); ", "; _
                                   IIf(g_privileges.descriptors(i).filter = "", "CAST(NULL AS VARCHAR(1))", "'" & g_privileges.descriptors(i).filter & "'"); ", "; _
                                   "'"; g_privileges.descriptors(i).granteeType; "', "; _
                                   IIf(g_privileges.descriptors(i).grantee = "", "CAST(NULL AS VARCHAR(1))", "'" & g_privileges.descriptors(i).grantee & "'"); ", "; _
                                   "'"; g_privileges.descriptors(i).privilege; "', "; _
                                   IIf(g_privileges.descriptors(i).withGrantOption, gc_dbTrue, gc_dbFalse); _
                                   ")"
       End If
   Next i
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "sequenceNo  AS c_sequenceNo,"
   Print #fileNo, addTab(3); "environment AS c_environment,"
   Print #fileNo, addTab(3); "opType      AS c_opType,"
   Print #fileNo, addTab(3); "objectType  AS c_objectType,"
   Print #fileNo, addTab(3); "schemaName  AS c_schemaName,"
   Print #fileNo, addTab(3); "objectName  AS c_objectName,"
   Print #fileNo, addTab(3); "filter      AS c_filter,"
   Print #fileNo, addTab(3); "granteeType AS c_granteeType,"
   Print #fileNo, addTab(3); "grantee     AS c_grantee,"
   Print #fileNo, addTab(3); "privilege   AS c_privilege,"
   Print #fileNo, addTab(3); "withGrant   AS c_withGrant"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_Priv"
   Print #fileNo, addTab(1); "DO"

   Print #fileNo, addTab(2); "IF"
   Print #fileNo, addTab(3); "NOT EXISTS ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "1"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameDbPrivileges; " P"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "COALESCE(RTRIM(CHAR(P.SEQUENCENO)), '') = COALESCE(RTRIM(CHAR(c_sequenceNo)), '')"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P.ENVIRONMENT = c_environment"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P.OPTYPE = c_opType"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P.OBJECTTYPE = c_objectType"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "COALESCE(P.SCHEMANAME, '') = COALESCE(c_schemaName, '')"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "COALESCE(P.OBJECTNAME, '') = COALESCE(c_objectName, '')"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "COALESCE(P.FILTER, '') = COALESCE(c_filter, '')"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P.GRANTEETYPE = c_granteeType"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "COALESCE(P.GRANTEE, '') = c_grantee"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P.PRIVILEGE = c_privilege"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P.WITHGRANT = c_withGrant"
   Print #fileNo, addTab(3); ") THEN"

   Print #fileNo, addTab(3); "SET v_stmntTxt ="
   Print #fileNo, addTab(4); "'INSERT INTO ' ||"
 
   Print #fileNo, addTab(5); "'"; g_qualTabNameDbPrivileges; " ' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "'SEQUENCENO, ' ||"
   Print #fileNo, addTab(5); "'ENVIRONMENT, ' ||"
   Print #fileNo, addTab(5); "'OPTYPE, ' ||"
   Print #fileNo, addTab(5); "'OBJECTTYPE, ' ||"
   Print #fileNo, addTab(5); "'SCHEMANAME, ' ||"
   Print #fileNo, addTab(5); "'OBJECTNAME, ' ||"
   Print #fileNo, addTab(5); "'FILTER, ' ||"
   Print #fileNo, addTab(5); "'GRANTEETYPE, ' ||"
   Print #fileNo, addTab(5); "'GRANTEE, ' ||"
   Print #fileNo, addTab(5); "'PRIVILEGE, ' ||"
   Print #fileNo, addTab(5); "'WITHGRANT' || "
   Print #fileNo, addTab(4); "') ' ||"
   Print #fileNo, addTab(4); "'VALUES ' ||"
   Print #fileNo, addTab(4); "'(' ||"
   Print #fileNo, addTab(5); "COALESCE('''' || RTRIM(CHAR(c_sequenceNo)) || '''', 'CAST(NULL AS SMALLINT)') || ', ' ||"
   Print #fileNo, addTab(5); "'''' || c_environment || ''', ' ||"
   Print #fileNo, addTab(5); "'''' || c_opType || ''', ' ||"
   Print #fileNo, addTab(5); "'''' || c_objectType || ''', ' ||"
   Print #fileNo, addTab(5); "COALESCE('''' || c_schemaName || '''', 'CAST(NULL AS VARCHAR(1))') || ', ' ||"
   Print #fileNo, addTab(5); "COALESCE('''' || c_objectName || '''', 'CAST(NULL AS VARCHAR(1))') || ', ' ||"
   Print #fileNo, addTab(5); "COALESCE('''' || c_filter || '''', 'CAST(NULL AS VARCHAR(1))') || ', ' ||"
   Print #fileNo, addTab(5); "'''' || c_granteeType || ''', ' ||"
   Print #fileNo, addTab(5); "COALESCE('''' || c_grantee || '''', 'CAST(NULL AS VARCHAR(1))') || ', ' ||"
   Print #fileNo, addTab(5); "'''' || c_privilege || ''', ' ||"
   Print #fileNo, addTab(5); "'''' || RTRIM(CHAR(c_withGrant)) || '''' ||"
   Print #fileNo, addTab(4); "')';"
 
   Print #fileNo, addTab(3); "IF mode_in < 2 THEN INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt); END IF;"
   Print #fileNo, addTab(3); "IF mode_in > 0 THEN EXECUTE IMMEDIATE v_stmntTxt; END IF;"
 
   Print #fileNo, addTab(3); "SET privCount_out = privCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"

   Dim qualProcedureNameGrantByEnv As String
   qualProcedureNameGrantByEnv = genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "ByEnv", eondmNone)

   genProcSectionHeader fileNo, "if new privileges were defined, apply them", 1
   Print #fileNo, addTab(1); "IF privCount_out > 0 THEN"

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcedureNameGrantByEnv; " (''T'', 2, ?)';"

   Print #fileNo, addTab(2); "IF mode_in < 2 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; qualTempTabNameAddUser; "(statement) VALUES (v_stmntTxt);"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF mode_in > 0 THEN"
   Print #fileNo, addTab(3); "BEGIN"
   genProcSectionHeader fileNo, "declare statement", 4, True
   genVarDecl fileNo, "v_stmnt", "STATEMENT", , 4
 
   Print #fileNo,
   Print #fileNo, addTab(4); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(4); "EXECUTE"
   Print #fileNo, addTab(5); "v_stmnt"
   Print #fileNo, addTab(4); "INTO"
   Print #fileNo, addTab(5); "grantCount_out"
   Print #fileNo, addTab(4); ";"

   Print #fileNo, addTab(3); "END;"

   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "return result to application", 1
   Print #fileNo, addTab(1); "IF mode_in < 2 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTempTabNameAddUser
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"

   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"

   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameCreateTestUser, ddlType, , "mode_in", "userCount_out", "privCount_out", "grantCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP creating Test-UserIDs", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameCreateTestUser
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "OUT", "userCount_out", "INTEGER", False, "number of userIDs created"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_privCount", "INTEGER", "0"
   genVarDecl fileNo, "v_grantCount", "INTEGER", "0"
   genSpLogDecl fileNo

   genSpLogProcEnter fileNo, qualProcedureNameCreateTestUser, ddlType, , "mode_in", "userCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameCreateTestUser; "(mode_in, userCount_out, v_privCount, v_grantCount);"

   genSpLogProcExit fileNo, qualProcedureNameCreateTestUser, ddlType, , "mode_in", "userCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ### ENDIF IVK ###
 Private Sub genDdlDbCompact( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If Not supportDbCompact Then
     Exit Sub
   End If
 
   On Error GoTo ErrorExit

   ' ####################################################################################################################
   ' #    SP compacting database
   ' ####################################################################################################################

   Dim qualProcedureNameDbCompact As String
   qualProcedureNameDbCompact = genQualProcName(g_sectionIndexDbAdmin, spnDbCompact, ddlType)

   printSectionHeader "SP compacting database", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameDbCompact
   Print #fileNo, addTab(0); "("
 
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "tsNamePattern_in", "VARCHAR(30)", True, "(optional) name (pattern) of tablespace(s) to compact"
   genProcParm fileNo, "IN", "contingencyPercent_in", "INTEGER", True, "(optional) minimum amount of 'free space' left in tablespace (default 10)"
   genProcParm fileNo, "IN", "minPages_in", "INTEGER", True, "(optional) minimum number of 'free space' left in tablespace (default 0)"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of statements executed / created"
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genSpLogDecl fileNo
 
   Dim qualTempTabNameDbCompact As String
   qualTempTabNameDbCompact = tempTabNameStatement & "DbCompact"
   genDdlForTempStatement fileNo, 1, True, 200, True, True, True, , "DbCompact", True, , True, , "msg", "VARCHAR(30)"

   genSpLogProcEnter fileNo, qualProcedureNameDbCompact, ddlType, , "mode_in", "'tsNamePattern_in", "contingencyPercent_in", "rowCount_out"
 
   genProcSectionHeader fileNo, "verify input parameter"
   Print #fileNo, addTab(1); "SET contingencyPercent_in = COALESCE(contingencyPercent_in, 10);"
   Print #fileNo, addTab(1); "SET tsNamePattern_in      = COALESCE(UPPER(tsNamePattern_in), '%');"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader fileNo, "loop over tablespaces"
   Print #fileNo, addTab(1); "FOR tsLoop AS tsCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_TabSpaces"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "tsName,"
   Print #fileNo, addTab(3); "tsAutoStorage,"
   Print #fileNo, addTab(3); "usablePages,"
   Print #fileNo, addTab(3); "targetPages,"
   Print #fileNo, addTab(3); "maxDeltaTech,"
   Print #fileNo, addTab(3); "maxDeltaTarget"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "TSS.TBSP_NAME,"
   Print #fileNo, addTab(4); "TSS.TBSP_USING_AUTO_STORAGE,"
   Print #fileNo, addTab(4); "TSP.TBSP_USABLE_PAGES,"
   Print #fileNo, addTab(4); "INTEGER(FLOAT(TSP.TBSP_USED_PAGES) * (1+(FLOAT(contingencyPercent_in)/100))) + 1,"
   Print #fileNo, addTab(4); "TSP.TBSP_USABLE_PAGES - TSP.TBSP_PAGE_TOP,"
   Print #fileNo, addTab(4); "TSP.TBSP_USABLE_PAGES - (INTEGER(FLOAT(TSP.TBSP_USED_PAGES) * (1+(FLOAT(contingencyPercent_in)/100))) + 1)"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "TABLE(SYSPROC.SNAP_GET_TBSP_V91(CURRENT SERVER,-1)) AS TSS"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "TABLE(SYSPROC.SNAP_GET_TBSP_PART_V97(CURRENT SERVER,-1)) AS TSP"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "TSS.TBSP_NAME = TSP.TBSP_NAME"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "UPPER(TSS.TBSP_NAME) LIKE tsNamePattern_in"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(3); "V_TabSpacesDelta"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "tsName,"
   Print #fileNo, addTab(3); "tsAutoStorage,"
   Print #fileNo, addTab(3); "maxDelta"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "tsName,"
   Print #fileNo, addTab(4); "tsAutoStorage,"
   Print #fileNo, addTab(4); "(CASE WHEN maxDeltaTech > maxDeltaTarget THEN maxDeltaTarget ELSE maxDeltaTech END)"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "V_TabSpaces"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "targetPages < usablePages"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "minPages_in IS NULL"
   Print #fileNo, addTab(6); "OR"
   Print #fileNo, addTab(5); "targetPages > minPages_in"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "tsName        AS c_tsName,"
   Print #fileNo, addTab(3); "tsAutoStorage AS c_tsAutoStorage,"
   Print #fileNo, addTab(3); "maxDelta      AS c_maxDelta"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_TabSpacesDelta"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "maxDelta > 0"
   Print #fileNo, addTab(1); "DO"
 
   genProcSectionHeader fileNo, "assemble ALTER TABLESPACE statement", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'ALTER TABLESPACE ' || RTRIM(c_tsName) || ' REDUCE';"
   Print #fileNo, addTab(2); "IF (c_tsAutoStorage = '0') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt || ' (ALL CONTAINERS ' || RTRIM(CHAR(c_maxDelta)) || ')';"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "store statement in temporary table", 2
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); qualTempTabNameDbCompact
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "statement"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "v_stmntTxt"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "execute configuration", 2
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "END IF;"
 
   genProcSectionHeader fileNo, "count statement", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + 1;"
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE resCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTempTabNameDbCompact
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqNo ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN resCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
 
   genSpLogProcExit fileNo, qualProcedureNameDbCompact, ddlType, , "mode_in", "'tsNamePattern_in", "contingencyPercent_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genDbAdminDdlDivCreate( _
     fileNo As Integer, _
     Optional ddlType As DdlTypeId = edtPdm)

   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType <> edtPdm Then
     Exit Sub
   End If
 
   Dim qualTabNameMdsUser As String
   qualTabNameMdsUser = g_qualTabNameUser
 
   Dim qualTabNameDivision As String
   qualTabNameDivision = g_qualTabNameDivision
 
   Dim qualTabNameDivisionNlText As String
   qualTabNameDivisionNlText = genQualTabNameByClassIndex(g_classIndexDivision, ddlType, g_primaryOrgIndex, g_workDataPoolIndex, , , , True)
 
   Dim qualTabNamePdmSchema As String
   qualTabNamePdmSchema = g_qualTabNamePdmSchema

   Dim qualTabNameMessage As String
   qualTabNameMessage = g_qualTabNameMessage
 
   Dim oidSeqName As String
   oidSeqName = genQualOidSeqNameForOrg(-1, ddlType)
 
 
   ' ####################################################################################################################
   ' #    SP for Create a new division
   ' ####################################################################################################################
 
   Dim qualProcedureNameDivCreate As String
   qualProcedureNameDivCreate = genQualProcName(g_sectionIndexDbAdmin, spnDivCreate, ddlType)
 
   printSectionHeader "SP for initialization of organization's meta data DUP-Code", fileNo
 
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE OR REPLACE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameDivCreate
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "mode_in", "INTEGER", True, "'0' - only list statements, '1' list and execute, '2' execute only"
   genProcParm fileNo, "IN", "cdUserId_in", "VARCHAR(  16 )", True, "CD User Id of the mdsUser"
   genProcParm fileNo, "IN", "label1_in", "VARCHAR( 255 )", True, "label of Division            (corresponding to languageId1_in)"
   genProcParm fileNo, "IN", "description1_in", "VARCHAR( 382 )", True, "description text of Division (corresponding to languageId1_in)"
   genProcParm fileNo, "IN", "label2_in", "VARCHAR( 255 )", True, "label of Division            (corresponding to languageId2_in)"
   genProcParm fileNo, "IN", "description2_in", "VARCHAR( 382 )", True, "description text of Division (corresponding to languageId2_in)"
   genProcParm fileNo, "OUT", "divOid_out", "BIGINT", True, "new division Oid"
   genProcParm fileNo, "OUT", "recordCount_out", "INTEGER", False, "number of Rows inserted"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "routineNotFound", "42884"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_rowCount", "INTEGER        ", "0"
   genVarDecl fileNo, "v_ignoreError", "SMALLINT       ", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR( 2000 )", "NULL"
   genVarDecl fileNo, "v_msg", "VARCHAR(   70 )", "NULL"
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "declare condition handler for routine not found"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR routineNotFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "IF ( v_ignoreError = 0 ) THEN"
   Print #fileNo, addTab(3); "RESIGNAL;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
   genProcSectionHeader fileNo, "declare continue handler for SQL-Exceptions"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "ROLLBACK;"
   Print #fileNo, addTab(2); "RESIGNAL;"
   Print #fileNo, addTab(1); "END;"
 
   Dim tempTabNameStatementDivCreate As String
   tempTabNameStatementDivCreate = tempTabNameStatement & "DivCreate"
 
   genDdlForTempStatement fileNo, 1, True, 2000, True, True, True, , "DivCreate"
 
   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET recordCount_out = 0;"
 
   genProcSectionHeader fileNo, "this procedure is supported starting with DB2 V9.7"
   genProcSectionHeader fileNo, "in order to support deployability in earlier DB2-Releases, use dynamic SQL here and ignore error"
   Print #fileNo, addTab(1); "SET v_ignoreError = 1;"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL SYSPROC.WLM_SET_CLIENT_INFO( '''', '''', '''', NULL, NULL )';"
   Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "SET v_ignoreError = 0;"
 
   genProcSectionHeader fileNo, "make sure that CD-User is valid"
   Print #fileNo, addTab(1); "IF ( SELECT 1 FROM "; qualTabNameMdsUser; " WHERE cduserid = cdUserId_in ) IS NULL THEN"
   genSpLogProcEscape fileNo, qualProcedureNameDivCreate, ddlType, 2, "'codeNumberList_in", "'cdUserId_in", "divOid_in", "recordCount_out"
   genSignalDdlWithParms "userUnknown", fileNo, 2, , , , , , , , , , "cdUserId_in"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "for batch-script: set command options"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementDivCreate; "( statement ) VALUES ( 'UPDATE COMMAND OPTIONS USING S ON' );"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementDivCreate; "( statement ) VALUES ( 'UPDATE COMMAND OPTIONS USING V ON' );"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "determine new Division OID"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "MAX( oid ) + 4"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "divOid_out"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameDivision
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "oid < 10000"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader fileNo, "create a new division"
   genProcSectionHeader fileNo, "insert new division Oid into table division"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO ' ||"
   Print #fileNo, addTab(2); "'      "; qualTabNameDivision; " ' ||"
   Print #fileNo, addTab(2); "'( ' ||"
   Print #fileNo, addTab(2); "'  oid, ' ||"
   Print #fileNo, addTab(2); "'  createuser, ' ||"
   Print #fileNo, addTab(2); "'  createtimestamp, ' ||"
   Print #fileNo, addTab(2); "'  updateuser, ' ||"
   Print #fileNo, addTab(2); "'  lastupdatetimestamp, ' ||"
   Print #fileNo, addTab(2); "'  versionid ' ||"
   Print #fileNo, addTab(2); "') ' ||"
   Print #fileNo, addTab(2); "'VALUES ' ||"
   Print #fileNo, addTab(2); "'( ' ||"
   Print #fileNo, addTab(2); "'  ' || divOid_out || ', ' ||"
   Print #fileNo, addTab(2); "'  ''' || cdUserId_in || ''', ' ||"
   Print #fileNo, addTab(2); "'  CURRENT TIMESTAMP, ' ||"
   Print #fileNo, addTab(2); "'  ''' || cdUserId_in || ''', ' ||"
   Print #fileNo, addTab(2); "'  CURRENT TIMESTAMP, ' ||"
   Print #fileNo, addTab(2); "'  1 ' ||"
   Print #fileNo, addTab(2); "') '"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementDivCreate; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET recordCount_out = recordCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "insert division description into table division_nl_text"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO ' ||"
   Print #fileNo, addTab(2); "'      "; qualTabNameDivisionNlText; " ' ||"
   Print #fileNo, addTab(2); "'( ' ||"
   Print #fileNo, addTab(2); "'  oid, ' ||"
   Print #fileNo, addTab(2); "'  div_oid, ' ||"
   Print #fileNo, addTab(2); "'  language_id, ' ||"
   Print #fileNo, addTab(2); "'  label, ' ||"
   Print #fileNo, addTab(2); "'  description, ' ||"
   Print #fileNo, addTab(2); "'  versionid ' ||"
   Print #fileNo, addTab(2); "') ' ||"
   Print #fileNo, addTab(2); "'VALUES ' ||"
   Print #fileNo, addTab(2); "'( ' ||"
   Print #fileNo, addTab(2); "'  NEXTVAL FOR "; oidSeqName; ", ' ||"
   Print #fileNo, addTab(2); "'  ' || divOid_out || ', ' ||"
   Print #fileNo, addTab(2); "'  1, ' ||"
   Print #fileNo, addTab(2); "'  ''' || label1_in || ''', ' ||"
   Print #fileNo, addTab(2); "'  ''' || description1_in || ''', ' ||"
   Print #fileNo, addTab(2); "'  1 ' ||"
   Print #fileNo, addTab(2); "') '"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementDivCreate; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET recordCount_out = recordCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(1); "SET v_stmntTxt = 'INSERT INTO ' ||"
   Print #fileNo, addTab(2); "'      "; qualTabNameDivisionNlText; " ' ||"
   Print #fileNo, addTab(2); "'( ' ||"
   Print #fileNo, addTab(2); "'  oid, ' ||"
   Print #fileNo, addTab(2); "'  div_oid, ' ||"
   Print #fileNo, addTab(2); "'  language_id, ' ||"
   Print #fileNo, addTab(2); "'  label, ' ||"
   Print #fileNo, addTab(2); "'  description, ' ||"
   Print #fileNo, addTab(2); "'  versionid ' ||"
   Print #fileNo, addTab(2); "') ' ||"
   Print #fileNo, addTab(2); "'VALUES ' ||"
   Print #fileNo, addTab(2); "'( ' ||"
   Print #fileNo, addTab(2); "'  NEXTVAL FOR "; oidSeqName; ", ' ||"
   Print #fileNo, addTab(2); "'  ' || divOid_out || ', ' ||"
   Print #fileNo, addTab(2); "'  2, ' ||"
   Print #fileNo, addTab(2); "'  ''' || label2_in || ''', ' ||"
   Print #fileNo, addTab(2); "'  ''' || description2_in || ''', ' ||"
   Print #fileNo, addTab(2); "'  1 ' ||"
   Print #fileNo, addTab(2); "') '"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "INSERT INTO "; tempTabNameStatementDivCreate; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(1); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET recordCount_out = recordCount_out + v_rowCount;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader fileNo, "Loop over MPCs"
   Print #fileNo, addTab(1); "FOR tableLoop AS tblCrsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "schemaname AS l_schemaname,"
   Print #fileNo, addTab(3); "SUBSTR( schemaname, 8, 2 ) AS l_schemaname_orgid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNamePdmSchema
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "schemaname LIKE 'VL6CERR%'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "pooltype_id IS NULL"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "organization_id IS NOT NULL"
   Print #fileNo, addTab(2); "GROUP BY schemaname"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "insert rows into table messageseverity", 2
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO ' ||"
   Print #fileNo, addTab(3); "'  ' || l_schemaname || '.messageseverity' || ' ' ||"
   Print #fileNo, addTab(3); "'( ' ||"
   Print #fileNo, addTab(3); "'  oid, ' ||"
   Print #fileNo, addTab(3); "'  severity_id, ' ||"
   Print #fileNo, addTab(3); "'  createuser, ' ||"
   Print #fileNo, addTab(3); "'  createtimestamp, ' ||"
   Print #fileNo, addTab(3); "'  updateuser, ' ||"
   Print #fileNo, addTab(3); "'  lastupdatetimestamp, ' ||"
   Print #fileNo, addTab(3); "'  msg_oid, ' ||"
   Print #fileNo, addTab(3); "'  div_oid, ' ||"
   Print #fileNo, addTab(3); "'  versionid ' ||"
   Print #fileNo, addTab(3); "') ' ||"
   Print #fileNo, addTab(3); "'SELECT ' ||"
   Print #fileNo, addTab(3); "'  NEXTVAL FOR vl6cmet' || l_schemaname_orgid || '.oidsequence, ' ||"
   Print #fileNo, addTab(3); "'  defaultseverity_id, ' ||"
   Print #fileNo, addTab(3); "'  ''' || cdUserId_in || ''', ' ||"
   Print #fileNo, addTab(3); "'  CURRENT TIMESTAMP, ' ||"
   Print #fileNo, addTab(3); "'  ''' || cdUserId_in || ''', ' ||"
   Print #fileNo, addTab(3); "'  CURRENT TIMESTAMP, ' ||"
   Print #fileNo, addTab(3); "'  oid, ' ||"
   Print #fileNo, addTab(3); "'  ' || divOid_out || ', ' ||"
   Print #fileNo, addTab(3); "'  1 ' ||"
   Print #fileNo, addTab(3); "'FROM ' ||"
   Print #fileNo, addTab(3); "'  "; qualTabNameMessage; " ' ||"
   Print #fileNo, addTab(3); "'WHERE ' ||"
   Print #fileNo, addTab(3); "'  isseverityeditable = 1 '"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementDivCreate; "( statement ) VALUES ( v_stmntTxt );"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(3); "SET recordCount_out = recordCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "END IF;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genProcSectionHeader fileNo, "return result to application"
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmtCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "statement"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementDivCreate
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "SEQNO ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader fileNo, "leave cursor open for application", 3
   Print #fileNo, addTab(3); "OPEN stmtCursor;"
   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 End Sub
 
 Private Sub addStatementIntoTempTable( _
     fileNo As Integer, _
     tabIndex As Integer, _
     tableName As String, _
     genTimestamps As Boolean _
     )

   Print #fileNo, addTab(tabIndex); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(tabIndex + 1); "INSERT INTO "; tableName; "(statement) VALUES (v_stmntTxt);"
   If genTimestamps Then
     Print #fileNo, addTab(tabIndex + 1); "INSERT INTO "; tableName; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
   End If
   Print #fileNo, addTab(tabIndex); "END IF;"
 End Sub
