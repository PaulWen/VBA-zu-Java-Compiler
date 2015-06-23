 Attribute VB_Name = "M06_Packaging"
 Option Explicit
 
 Private Const maxSubDirs = 30
 
 Private Const dirInstall = "install"
 Private Const dirScripts = "scripts"
 Private Const dirTemplate = "template"
 Private Const dirData = "data"
 Private Const dirEtc = "etc"
 
 Private Const fileReadMe = "readme.txt"
 Private Const fileFilesystemSetup = "00_please_edit_filesystem-setup.sh"
 Private Const fileCreateDb = "01_please_edit_create_db.sql"
 Private Const fileCreateTs = "02_please_edit_create_tablespaces.sql"
 Private Const fileUpdDbCfg = "03_please_edit_update_db_cfg.sql"
 Private Const fileUpdDbProfile = "03_update_db_profile.sh"
 Private Const fileCreateExtSps = "04_please_edit_create_ext_storedprocs.sql"
 Private Const fileCreateExtSpsSh = "04_please_edit_create_ext_storedprocs.sh"
 Private Const fileCreateObjects = "05_create_db_objects.sql"
 Private Const fileCreateObjectsTemplate = "05_create_db_objects-template.sql"
 Private Const fileImportData = "06_import_data.sql"
 Private Const fileInitMeta = "07_init_meta_data.sql"
 Private Const fileGrant = "08_grant.sql"
 Private Const fileRebindCli = "09_rebind_cli.sql"
 
 Private Const fnSuffixTmp = ".tmp"
 Private Const fnSuffixJar = ".jar"
 
 Enum DeployPackageType
   edptFullDeployment
   edptUpdate
 End Enum
 
 
 Sub genPackages()
   If generateLdm Then
     If generateNonLrt Then
       genPackageByDdlType(edtLdm, False)
     End If
     If generateLrt Then
       genPackageByDdlType(edtLdm, True)
     End If
   End If

   If generatePdm Then
     If generateNonLrt Then
       genPackageByDdlType(edtPdm, False)
     End If
     If generateLrt Then
       genPackageByDdlType(edtPdm, True)
     End If
   End If
 End Sub
 
 
 ' ####################################################################################################################
 ' #    utilities
 ' ####################################################################################################################
 
 Private Sub genScriptHeader( _
   fileNo As Integer, _
   ByRef fileName As String, _
   ByRef description As String, _
   Optional forShell As Boolean = False, _
   Optional ignoreTs As Boolean = False _
 )
   Dim linePrefix As String
   If forShell Then
     linePrefix = ""
     Print #fileNo, "#!/bin/ksh"; vbLf;
     Print #fileNo, ; vbLf;
   Else
     linePrefix = "-- "
   End If

   Print #fileNo, linePrefix; gc_sqlDelimLine1; vbLf;
   Print #fileNo, linePrefix; "#"; vbLf;
   Print #fileNo, linePrefix; "#  Script      : "; baseName(fileName); vbLf;
   Print #fileNo, linePrefix; "#  Version     : "; versionString; vbLf;
   Print #fileNo, linePrefix; "#  Contact     : "; kwTranslate("<contactCompany>"); ", "; kwTranslate("<contactPerson>"); vbLf;
   Print #fileNo, linePrefix; "#  Description : "; description; vbLf;

   If forShell Then
     Print #fileNo, linePrefix; "#  Usage       : "; baseName(fileName); vbLf;
   Else
     Print #fileNo, linePrefix; "#  Usage       : db2 -td@ -f "; baseName(fileName); vbLf;
   End If

   Print #fileNo, linePrefix; "#  History     :"; vbLf;
   Print #fileNo, linePrefix; "#"; vbLf;
   Print #fileNo, linePrefix; gc_sqlDelimLine1; vbLf;
   Print #fileNo, vbLf;

   If Not forShell And Not ignoreTs Then
     Print #fileNo, "UPDATE COMMAND OPTIONS USING V OFF"; gc_sqlCmdDelim; vbLf;
 '    Print #fileNo, "UPDATE COMMAND OPTIONS USING S ON"; gc_sqlCmdDelim; vbLf;
     Print #fileNo, "UPDATE COMMAND OPTIONS USING N ON"; gc_sqlCmdDelim; vbLf;
     Print #fileNo, "UPDATE COMMAND OPTIONS USING X ON"; gc_sqlCmdDelim; vbLf;
 
     Print #fileNo, vbLf;
     Print #fileNo, "VALUES"; vbLf;
     Print #fileNo, "'*******************************************************************************************' || CHR(10) ||"; vbLf;
     Print #fileNo, "'*' || CHR(10) ||"; vbLf;
     Print #fileNo, "'* Begin of script execution ("; baseName(fileName); "): ' || CURRENT TIMESTAMP || CHR(10) ||"; vbLf;
     Print #fileNo, "'*' || CHR(10) ||"; vbLf;
     Print #fileNo, "'*******************************************************************************************' || CHR(10)"; vbLf;
     Print #fileNo, gc_sqlCmdDelim; vbLf;
     Print #fileNo, vbLf;
   End If
 End Sub
 
 
 Private Sub genScriptTrailer( _
   fileNo As Integer, _
   ByRef fileName As String, _
   Optional forShell As Boolean = False, _
   Optional forTemplate As Boolean = False _
 )
   Dim linePrefix As String
   If forShell Then
     linePrefix = ""
   Else
     linePrefix = "-- "
   End If

   If forTemplate Then
     printSectionHeader("update deployment history of template", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "INSERT INTO"
     Print #fileNo, addTab(1); g_qualTabNameApplVersion
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "VERSION,"
     Print #fileNo, addTab(1); "DEPLOYDATE,"
     Print #fileNo, addTab(1); "DESCRIPTION"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "VALUES"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "'"; versionString; " ["; genTemplateParamWrapper("1"); "]',"
     Print #fileNo, addTab(1); "CURRENT DATE,"
     Print #fileNo, addTab(1); "'MPC "; genTemplateParamWrapper("1"); " created'"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, gc_sqlCmdDelim
   End If

   If Not forShell Then
     Print #fileNo, vbLf;
     Print #fileNo, "VALUES"; vbLf;
     Print #fileNo, "'*******************************************************************************************' || CHR(10) ||"; vbLf;
     Print #fileNo, "'*' || CHR(10) ||"; vbLf;
     Print #fileNo, "'* End of script execution ("; baseName(fileName); "): ' || CURRENT TIMESTAMP || CHR(10) ||"; vbLf;
     Print #fileNo, "'*' || CHR(10) ||"; vbLf;
     Print #fileNo, "'*******************************************************************************************' || CHR(10)"; vbLf;
     Print #fileNo, gc_sqlCmdDelim; vbLf;
     Print #fileNo, vbLf;
   End If
 End Sub
 
 
 Private Sub mapConditionalDdlSections( _
   ByRef text As String, _
   condition As Boolean, _
   ByRef keyword As String _
 )
   Dim startStr As String
   Dim endStr As String
   Dim startPos As Integer
   Dim endPos As Integer
 
   ' first remove sections which do not apply
   If condition Then
     startStr = "-- $$IF NOT " & UCase(keyword) & "$$"
     endStr = "-- $$END IF NOT " & UCase(keyword) & "$$"
   Else
     startStr = "-- $$IF " & UCase(keyword) & "$$"
     endStr = "-- $$END IF " & UCase(keyword) & "$$"
   End If
 
   startPos = InStr(1, text, startStr, vbTextCompare)
   endPos = InStr(1, text, endStr, vbTextCompare)
 
   While startPos > 0 And endPos > 0
     text = Left(text, startPos - 1) & Right(text, Len(text) - endPos - Len(endStr) - 1)

     startPos = InStr(1, text, startStr, vbTextCompare)
     endPos = InStr(1, text, endStr, vbTextCompare)
   Wend

   ' second remove escape sequences for sections which do apply
   If condition Then
     startStr = "-- $$IF " & UCase(keyword) & "$$"
     endStr = "-- $$END IF " & UCase(keyword) & "$$"
   Else
     startStr = "-- $$IF NOT " & UCase(keyword) & "$$"
     endStr = "-- $$END IF NOT " & UCase(keyword) & "$$"
   End If
 
   startPos = InStr(1, text, startStr, vbTextCompare)
   While startPos > 0
     text = Left(text, startPos - 1) & Right(text, Len(text) - startPos - Len(startStr) - 1)
     startPos = InStr(1, text, startStr, vbTextCompare)
   Wend
 
   endPos = InStr(1, text, endStr, vbTextCompare)
   While endPos > 0
     text = Left(text, endPos - 1) & Right(text, Len(text) - endPos - Len(endStr) - 1)
     endPos = InStr(1, text, endStr, vbTextCompare)
   Wend
 End Sub
 
 Private Sub catFile( _
   ByRef fileNameIn As String, _
   fileNoOut As Integer, _
   Optional mapKeyWords As Boolean = False, _
   Optional mapConditionalSections As Boolean = False _
 )
   Dim fileNo As Integer

   On Error GoTo ErrorExit
 
   fileNo = FreeFile()

   Open fileNameIn For Input As #fileNo

   Dim text As String
   text = Input(LOF(fileNo), fileNo)
 
   If mapConditionalSections Then
     mapConditionalDdlSections(text, supportSpLogging, "SPLOGGING")
   End If

   If mapKeyWords Then
     Print #fileNoOut, Replace(kwTranslate(text), vbCr, "");
   Else
     Print #fileNoOut, Replace(text, vbCr, "");
   End If

   If (Right(text, 1) = vbLf) Or (Right(text, 1) = " " And Left(Right(text, 2), 1) = vbLf) Then
     '
   Else
     Print #fileNoOut, vbLf;
   End If
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub catDdlsInDir( _
   fileNoOutCrTs As Integer, _
   fileNoOutCrObj As Integer, _
   ByRef dirPath As String, _
   ddlType As DdlTypeId, _
   Optional mapKeyWords As Boolean = False, _
   Optional attributes As Integer = vbNormal, _
   Optional mapConditionalSections As Boolean = False _
 )
   On Error GoTo ErrorExit

   Dim match As String
   Dim tsDdlFileName As String
   tsDdlFileName = getTableSpaceDdlBaseFileName(ddlType)
   Dim bpDdlFileName As String
   bpDdlFileName = getBufferPoolDdlBaseFileName(ddlType)

   match = dir(dirPath & "\*DDL", attributes)

   Do While match <> ""
     If LCase(Left(match, 3)) = "ivk" Then
       ' ignore
     ElseIf InStr(1, LCase(match), "splog") And Not (supportSpLogging And targetPlatform = "AIX" And spLogMode = esplFile) Then
       ' ignore
     Else
       If (fileNoOutCrTs > 0) And (match = tsDdlFileName Or match = bpDdlFileName) Then
         catFile(dirPath & "\" & match, fileNoOutCrTs, mapKeyWords)
       Else
         catFile(dirPath & "\" & match, fileNoOutCrObj, mapKeyWords, mapConditionalSections)
       End If
     End If
     match = dir ' next entry.
   Loop
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   On Error Resume Next
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub catDdlsInDirRecursive( _
   fileNoOutCrTs As Integer, _
   fileNoOutCrObj As Integer, _
   ByRef dirPath As String, _
   ByRef ddlType As DdlTypeId, _
   Optional mapKeyWords As Boolean = False, _
   Optional attributes As Integer = vbNormal, _
   Optional ByVal mapConditionalSections As Boolean = False _
 )

   Dim match As String
   Dim baseDirName As String
   Dim subDirs(1 To maxSubDirs) As String
   Dim numSubDirs As Integer
   numSubDirs = 0
   baseDirName = baseName(dirPath)

   If (UCase(baseDirName) = "EXPLAIN") And Not includeExplainDdlInDeliveryPackage Then
     Exit Sub
   End If

 ' ### IF IVK ###
   If (UCase(baseDirName) = "SST-TEST") And Not supportSstCheck Then
     Exit Sub
   End If

   If (UCase(baseDirName) = "MIGRATION") Or (UCase(baseDirName) = "VDOKF") Or (UCase(baseDirName) = "EXPLAIN") Then
     mapConditionalSections = True
   End If

 ' ### ENDIF IVK ###
   match = dir(dirPath & "\*", vbDirectory)
   Do While match <> ""
     If numSubDirs < maxSubDirs And match <> "." And match <> ".." And UCase(match) <> ".SVN" And UCase(match) <> "DROP" And UCase(match) <> "TEMPLATE" And UCase(match) <> "DEPLOY" Then
       If (GetAttr(dirPath & "\" & match) And vbDirectory) = vbDirectory Then
         numSubDirs = numSubDirs + 1
         subDirs(numSubDirs) = match
       End If
     End If
     match = dir ' next entry.
   Loop
 
   catDdlsInDir(fileNoOutCrTs, fileNoOutCrObj, dirPath, ddlType, mapKeyWords, attributes, mapConditionalSections)
 
   Dim i As Integer
   For i = 1 To numSubDirs
     catDdlsInDirRecursive(fileNoOutCrTs, fileNoOutCrObj, dirPath & "\" & subDirs(i), ddlType, mapKeyWords, attributes, mapConditionalSections)
   Next i
 End Sub
 
 
 Private Sub catDmlsInDir( _
   fileNoOut As Integer, _
   ByRef dirPath As String, _
   ddlType As DdlTypeId, _
   Optional attributes As Integer = vbNormal _
 )
 
   Dim match As String
   Dim fileName As String

   match = dir(dirPath & "\*DML", attributes)
   Do While match <> ""
     catFile(dirPath & "\" & match, fileNoOut)
     match = dir ' next entry.
   Loop
 End Sub
 
 
 Private Sub stripCrInFile( _
   ByRef fileNameIn As String _
 )
   Dim fileNameOut As String
   Dim fileNoIn As Integer
   Dim fileNoOut As Integer
 
   fileNameOut = fileNameIn & fnSuffixTmp

   On Error GoTo ErrorExit
 
   fileNoIn = FreeFile()
   Open fileNameIn For Input As #fileNoIn

   fileNoOut = FreeFile()
   Open fileNameOut For Output As #fileNoOut

   Print #fileNoOut, Replace(Input(LOF(fileNoIn), fileNoIn), vbCr, "");
 
   Close #fileNoIn
   Close #fileNoOut
 
   Kill fileNameIn
   Name fileNameOut As fileNameIn
 
 NormalExit:
   On Error Resume Next
   Kill fileNameOut
   Close #fileNoIn
   Close #fileNoOut
   Exit Sub
 
 ErrorExit:
   If UCase(Left(baseName(fileNameIn), 6)) <> "EDIT." Then
     errMsgBox Err.description & "/" & Err.Number & "/" & fileNameIn
   End If
   Resume NormalExit
 End Sub
 
 
 Private Sub stripCrInDir( _
   ByRef dirPath As String _
 )
   Dim match As String
   Dim fileName As String

   match = dir(dirPath & "\*")
   Do While match <> ""
     fileName = dirPath & "\" & match
     If (Right(fileName, Len(fnSuffixTmp)) <> fnSuffixTmp) And _
        (Right(fileName, Len(fnSuffixTmp)) <> fnSuffixJar) Then
       If (GetAttr(fileName) And vbDirectory) = 0 Then
         stripCrInFile(fileName)
       End If
     End If

     match = dir ' next entry.
   Loop
 End Sub
 
 
 Private Sub stripCrInDirRecursive( _
   ByRef dirPath As String _
 )
   Dim match As String
   Dim subDirs(1 To maxSubDirs) As String
   Dim numSubDirs As Integer
   numSubDirs = 0

   match = dir(dirPath & "\*", vbDirectory)
   Do While match <> ""
     If numSubDirs < maxSubDirs And match <> "." And match <> ".." And UCase(match) <> ".SVN" Then
       If (GetAttr(dirPath & "\" & match) And vbDirectory) = vbDirectory And match <> "obj" Then
         numSubDirs = numSubDirs + 1
         subDirs(numSubDirs) = match
       End If
     End If
     match = dir ' next entry.
   Loop
 
   stripCrInDir(dirPath)
 
   Dim i As Integer
   For i = 1 To numSubDirs
     stripCrInDirRecursive(dirPath & "\" & subDirs(i))
   Next i
 End Sub
 
 
 Private Sub cpEtcFilesFromDir( _
   ByRef sourceDirPath As String, _
   ByRef targetDirPath As String, _
   ByRef ddlType As DdlTypeId, _
   Optional attributes As Integer = vbNormal _
 )
   If Not includeUtilityScrptsinPackage Then
     Exit Sub
   End If

   Dim match As String
   On Error GoTo ErrorExit
   Dim qualTabName As String

   match = dir(sourceDirPath & "\*", attributes)
   If match = "" Then
     Exit Sub
   End If

   assertDir(targetDirPath & "\dummy")

   Dim list() As String
   Do While match <> ""
     FileCopy sourceDirPath & "\" & match, targetDirPath & "\" & match
     stripCrInFile(targetDirPath & "\" & match)
     match = dir ' next entry.
   Loop
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub cpCsvsFromDir( _
   fileNoOutImportData As Integer, _
   ByRef sourceDirPath As String, _
   ByRef targetDirPath As String, _
   ByRef ddlType As DdlTypeId, _
   Optional attributes As Integer = vbNormal _
 )
   Dim match As String
   On Error GoTo ErrorExit
   Dim qualTabName As String

   match = dir(sourceDirPath & "\*CSV", attributes)
   If match = "" Then
     Exit Sub
   End If

   Dim list() As String
   Do While match <> ""
     qualTabName = baseName(match, ".csv")
     list = split(qualTabName, "-")
     If UBound(list) >= 2 Then
       FileCopy sourceDirPath & "\" & match, targetDirPath & "\" & match
       stripCrInFile(targetDirPath & "\" & match)
       qualTabName = list(2)
       Print #fileNoOutImportData, "IMPORT FROM ../data/"; match; " OF DEL MODIFIED BY COLDEL, COMMITCOUNT 10000 INSERT INTO "; qualTabName; " "; gc_sqlCmdDelim; vbLf;
     End If
     match = dir ' next entry.
   Loop
 
 NormalExit:
   On Error Resume Next
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub cpCsvFromDirRecursive( _
   fileNoOutImportData As Integer, _
   ByRef sourceDirPath As String, _
   ByRef targetDirPath As String, _
   ByRef ddlType As DdlTypeId, _
   Optional attributes As Integer = vbNormal _
 )

   Dim match As String
   Dim subDirs(1 To maxSubDirs) As String
   Dim numSubDirs As Integer
   numSubDirs = 0

   match = dir(sourceDirPath & "\*", vbDirectory)
   Do While match <> ""
     If numSubDirs < maxSubDirs And match <> "." And match <> ".." And UCase(match) <> ".SVN" Then
       If (GetAttr(sourceDirPath & "\" & match) And vbDirectory) = vbDirectory Then
         numSubDirs = numSubDirs + 1
         subDirs(numSubDirs) = match
       End If
     End If
     match = dir ' next entry.
   Loop
 
   assertDir(targetDirPath & "\dummy")
   cpCsvsFromDir(fileNoOutImportData, sourceDirPath, targetDirPath, ddlType)
 
   Dim i As Integer
   For i = 1 To numSubDirs
     cpCsvFromDirRecursive(fileNoOutImportData, sourceDirPath & "\" & subDirs(i), targetDirPath, ddlType, attributes)
   Next i
 End Sub
 
 
 Private Sub cpImplModulesFromDir( _
   ByRef sourceDirPath As String, _
   ByRef targetDirPath As String, _
   Optional ByRef fileNameSuffix As String = "", _
   Optional attributes As Integer = vbNormal, _
   Optional ByRef exception As String = "" _
 )
   Dim match As String
   On Error GoTo ErrorExit

   match = dir(sourceDirPath & "\*" & fileNameSuffix, attributes)

   Do While match <> ""
     If (Len(exception) > 0) And (Left(match, Len(exception)) = exception) Then
       On Error Resume Next
       Kill targetDirPath & "\" & match
       On Error GoTo ErrorExit
     Else
       assertDir(targetDirPath & "\" & match)
       FileCopy sourceDirPath & "\" & match, targetDirPath & "\" & match
     End If
     match = dir ' next entry.
   Loop
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    README
 ' ####################################################################################################################
 
 Private Sub genReadMe( _
   ByRef targetDir As String _
 )
   Dim fileName As String
   Dim fileNo As Integer

   On Error GoTo ErrorExit

   fileName = targetDir & "\" & fileReadMe
   assertDir(fileName)
   fileNo = FreeFile()

   Open fileName For Output As #fileNo

   Print #fileNo, kwTranslate("Please edit each script as follows:"); vbLf;
   Print #fileNo, vbLf;
   Print #fileNo, kwTranslate("- to provide correct connection information replace '<myDb>'"); vbLf;
   Print #fileNo, kwTranslate("  with the actual db name according to environment; "); vbLf;
   Print #fileNo, kwTranslate("- in script '" & fileCreateDb & "' replace '<myDbDir>' with the path where '<myDb>' is supposed to reside"); vbLf;

   If kwTranslate("<tsRootDir>") <> "" Then
     Print #fileNo, kwTranslate("- in scripts '" & fileCreateDb & "' and '" & fileCreateTs & "' replace '<tsRootDir>'"); vbLf;
     Print #fileNo, kwTranslate("  with the path where tablespace containers are supposed to reside;"); vbLf;
     Print #fileNo, kwTranslate("  (verify that the replacemant results in container paths which conform to the environment)"); vbLf;
   End If

   If kwTranslate("<dbInstance>") <> "" Then
     If supportSpLogging And targetPlatform = "AIX" And spLogMode = esplFile Then
       Print #fileNo, kwTranslate("- in script '" & fileCreateExtSpsSh & "' replace '<dbInstance>'"); vbLf;
       Print #fileNo, kwTranslate("  with the name of the database instance;"); vbLf;
     End If
   End If

   If kwTranslate("<logPath>") <> "" Then
     Print #fileNo, kwTranslate("- in script '" & fileUpdDbCfg & "' replace '<logPath>'"); vbLf;
   End If

   If kwTranslate("<jarPath>") <> "" Then
     Print #fileNo, kwTranslate("- in script '" & fileCreateExtSps & "' replace '<jarPath>'"); vbLf;
     Print #fileNo, kwTranslate("  with the absolute directory path where 'jar'-files reside which implement the"); vbLf;
     Print #fileNo, kwTranslate("  JAVA-Stored Procedures;"); vbLf;
     Print #fileNo, kwTranslate("  (the relative path in the deployment package is '../install/jar')"); vbLf;
   End If
 
   Print #fileNo, vbLf;
   Print #fileNo, kwTranslate("Please execute scripts using ""db2 -td@ -f <scriptname>"""); vbLf;
   Print #fileNo, kwTranslate("in the following order (make sure to check output for successful execution):"); vbLf;
   Print #fileNo, vbLf;
   Print #fileNo, kwTranslate("db2 -td@ -f " & fileCreateDb); vbLf;
   Print #fileNo, vbLf;
   Print #fileNo, kwTranslate("db2 connect to <myDb>"); vbLf;
   Print #fileNo, kwTranslate("db2 -td@ -f " & fileCreateTs); vbLf;
   Print #fileNo, kwTranslate("db2 -td@ -f " & fileUpdDbCfg); vbLf;
   Print #fileNo, kwTranslate("./" & fileUpdDbProfile); vbLf;
   Print #fileNo, kwTranslate("db2 terminate"); vbLf;
   Print #fileNo, vbLf;
   Print #fileNo, kwTranslate("< take backup in order to take database out of 'backup pending'-state >"); vbLf;
   Print #fileNo, vbLf;
   Print #fileNo, kwTranslate("db2 connect to <myDb>"); vbLf;
   Print #fileNo, kwTranslate("db2 -td@ -f " & fileCreateExtSps); vbLf;
   Print #fileNo, kwTranslate("db2 -td@ -f " & fileCreateObjects); vbLf;
   Print #fileNo, kwTranslate("db2 -td@ -f " & fileImportData); vbLf;
   Print #fileNo, kwTranslate("db2 -td@ -f " & fileInitMeta); vbLf;
   Print #fileNo, kwTranslate("db2 -td@ -f " & fileGrant); vbLf;
   If bindJdbcPackagesWithReoptAlways Then
     Print #fileNo, kwTranslate("db2 -td@ -f " & fileRebindCli); vbLf;
   End If

   Print #fileNo, kwTranslate("db2 terminate"); vbLf;
   Print #fileNo, vbLf;
   Print #fileNo, kwTranslate("Note: To simplify script adaptation the 'CONNECT'-command is no longer"); vbLf;
   Print #fileNo, kwTranslate("      part of the individual SQL-scripts. Thus, 'CONNECT'- and 'TERMINATE'-commands"); vbLf;
   Print #fileNo, kwTranslate("      must be executed separately as indicated above."); vbLf;
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 ' ####################################################################################################################
 ' #    create the directory structures
 ' ####################################################################################################################
 
 Private Sub genScriptFilesystemSetup( _
   ByRef targetDir As String _
 )
   Dim fileName As String
   Dim fileNo As Integer

   On Error GoTo ErrorExit

   fileName = targetDir & "\" & dirScripts & "\" & fileFilesystemSetup
   assertDir(fileName)
   fileNo = FreeFile()

   Open fileName For Output As #fileNo

   genScriptHeader(fileNo, fileName, "used to create the directory structures", True)

 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 ' ####################################################################################################################
 ' #    create DB
 ' ####################################################################################################################
 
 Private Sub genScriptCreateDb( _
   ByRef targetDir As String _
 )
   Dim fileName As String
   Dim fileNo As Integer

   On Error GoTo ErrorExit

   fileName = targetDir & "\" & dirScripts & "\" & fileCreateDb
   assertDir(fileName)
   fileNo = FreeFile()

   Open fileName For Output As #fileNo

   genScriptHeader(fileNo, fileName, "Creates the MDS database", , True)
 
   Print #fileNo, kwTranslate("CREATE DB <dbName> ON <dbPath> USING CODESET UTF-8 TERRITORY DE"); vbLf;
   If kwTranslate("<tsRootDir>") <> "" Then
     Print #fileNo, addTab(1); kwTranslate("CATALOG   TABLESPACE MANAGED BY SYSTEM USING ('<tsRootDir>/system1/SYSCATSPACE')"); vbLf;
     Print #fileNo, addTab(1); kwTranslate("USER      TABLESPACE MANAGED BY SYSTEM USING ('<tsRootDir>/data1/USERSPACE1')"); vbLf;
     Print #fileNo, addTab(1); kwTranslate("TEMPORARY TABLESPACE MANAGED BY SYSTEM USING ('<tsRootDir>/temp1sms/TEMPSPACE1')"); vbLf;
   End If
   Print #fileNo, addTab(0); gc_sqlCmdDelim; vbLf;
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    update DB cfg
 ' ####################################################################################################################
 
 
 
 Private Sub genScriptUpdDbCfg( _
   ByRef targetDir As String _
 )
   Dim fileName As String
   Dim fileNo As Integer
   Dim ddlType As DdlTypeId

   ddlType = edtPdm

   On Error GoTo ErrorExit

   fileName = targetDir & "\" & dirScripts & "\" & fileUpdDbCfg
   assertDir(fileName)
   fileNo = FreeFile()

   Open fileName For Output As #fileNo
 
   genScriptHeader(fileNo, fileName, "Sets database manager and database parameters for MDS")

   Print #fileNo, "UPDATE COMMAND OPTIONS USING V ON"; gc_sqlCmdDelim; vbLf;

   Dim i As Integer
   For i = 1 To g_dbCfgParams.numDescriptors
       If g_dbCfgParams.descriptors(i).isDbmCfgParam Then
         Print #fileNo, kwTranslate("UPDATE DBM CFG USING " & Left(g_dbCfgParams.descriptors(i).parameter & "                     ", 20) & g_dbCfgParams.descriptors(i).value); " "; gc_sqlCmdDelim; vbLf;
       End If
   Next i

   Print #fileNo,
   Print #fileNo, "UPDATE COMMAND OPTIONS USING V OFF"; gc_sqlCmdDelim; vbLf;
   Print #fileNo,
   Print #fileNo,

   ' ####################################################################################################################
   ' #    (temporary) SP for configuring database parameter
   ' ####################################################################################################################

   Dim qualProcedureNameSetDbCfg As String
   qualProcedureNameSetDbCfg = genQualProcName(g_sectionIndexDbAdmin, spnSetDbCfg, ddlType)

   printSectionHeader("(temporary) SP for configuring database parameter", fileNo)

   Print #fileNo, vbLf;
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameSetDbCfg
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "mode_in", "SMALLINT", True, "'0' - only list statements, '1' list and execute, '2' execute only")
   genProcParm(fileNo, "OUT", "osPlatform_out", "VARCHAR(5)", True, "OS-platform of the database server")
   genProcParm(fileNo, "OUT", "dbRelease_out", g_dbtDbRelease, True, "DB-release of the database server")
   genProcParm(fileNo, "OUT", "parmCount_out", "INTEGER", True, "number of parameters sucessfully set")
   genProcParm(fileNo, "OUT", "failCount_out", "INTEGER", False, "number of parameter failed to set")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(80)", "NULL")
   genVarDecl(fileNo, "v_flag", "CHAR(1)", "NULL")
   genVarDecl(fileNo, "v_diagnostics", "VARCHAR(100)", "NULL")
   genVarDecl(fileNo, "v_messageText", "VARCHAR(100)", "NULL")
   genVarDecl(fileNo, "v_catchException", g_dbtBoolean, gc_dbFalse)
   genVarDecl(fileNo, "SQLCODE", "INTEGER", "NULL")
 
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "SET v_flag = '?';"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLEXCEPTION"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "GET DIAGNOSTICS EXCEPTION 1 v_diagnostics = DB2_TOKEN_STRING, v_messageText = MESSAGE_TEXT;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF SQLCODE > 0 THEN"
   Print #fileNo, addTab(3); "SET v_flag = '?';"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "IF v_catchException = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(4); "RESIGNAL;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(3); "SET v_flag = '-';"
   Print #fileNo, addTab(3); "SET failCount_out = failCount_out + 1;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END;"
 
   Dim tempTabNameStatementDbCfg As String
   tempTabNameStatementDbCfg = tempTabNameStatement & "DbCfg"

   genDdlForTempStatement(fileNo, 1, True, 80, True, True, True, , "DbCfg", , , True, , "msg", "VARCHAR(100)")
 
   genProcSectionHeader(fileNo, "initialize output parameter")
   Print #fileNo, addTab(1); "SET parmCount_out = 0;"
   Print #fileNo, addTab(1); "SET failCount_out = 0;"

   genProcSectionHeader(fileNo, "determine OS-Platform")
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
   Print #fileNo, addTab(2); "osPlatform_out"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "TABLE(SYSPROC.SNAPSHOT_DATABASE(CURRENT SERVER, -1)) X"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); "SET osPlatform_out = COALESCE(osPlatform_out, 'AIX64');"

   genProcSectionHeader(fileNo, "determine DB-Release")
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "CAST((v_int + ((v_dec - v_int) / 10)) AS "; g_dbtDbRelease; ")"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "dbRelease_out"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "CAST(str AS "; g_dbtDbRelease; ") AS v_dec,"
   Print #fileNo, addTab(4); "INTEGER(CAST(str AS "; g_dbtDbRelease; ")) AS v_int,"
   Print #fileNo, addTab(4); "str AS v_str"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "SUBSTR(str, 1, POSSTR(str, '.') + POSSTR(RIGHT(str, LENGTH(str) - POSSTR(str, '.')), '.') -1) AS str"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "SELECT"
   Print #fileNo, addTab(7); "REPLACE("
   Print #fileNo, addTab(8); "RIGHT(SERVICE_LEVEL, LENGTH(SERVICE_LEVEL) - POSSTR(SERVICE_LEVEL, ' ')),"
   Print #fileNo, addTab(8); "'v', ''"
   Print #fileNo, addTab(7); ") AS str"
   Print #fileNo, addTab(6); "FROM"
   Print #fileNo, addTab(7); "TABLE(SYSPROC.ENV_GET_INST_INFO()) AS INSTANCEINFO"
   Print #fileNo, addTab(5); ") V_VERS"
   Print #fileNo, addTab(3); ") V_VERS_T"
   Print #fileNo, addTab(2); ") V_VERS_S"
   Print #fileNo, addTab(1); ";"
   Print #fileNo, addTab(1); "SET dbRelease_out = COALESCE(dbRelease_out, 8);"

   genProcSectionHeader(fileNo, "loop over configuration parameters and find 'best match' based on DB2 Release")
   Print #fileNo, addTab(1); "FOR parmLoop AS parmCsr CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_ParmList"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "name,"
   Print #fileNo, addTab(3); "value,"
   Print #fileNo, addTab(3); "seqNoDeploy,"
   Print #fileNo, addTab(3); "serverPlatform,"
   Print #fileNo, addTab(3); "minDbRelease"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
 
   Dim firstParam As Boolean
   firstParam = True
   For i = 1 To g_dbCfgParams.numDescriptors
       If Not g_dbCfgParams.descriptors(i).isDbmCfgParam And Not g_dbCfgParams.descriptors(i).isDbProfileParam Then
         If Not firstParam Then
           Print #fileNo, addTab(3); "UNION ALL"
         End If
         firstParam = False
         Print #fileNo, _
           addTab(3); _
           "VALUES('"; Left(g_dbCfgParams.descriptors(i).parameter & "',                     ", 20); _
           " '"; Left(Trim(g_dbCfgParams.descriptors(i).value) & "',                     ", 50); _
           " "; Left("SMALLINT(" & IIf(g_dbCfgParams.descriptors(i).sequenceNo <= 0, "9999", CStr(g_dbCfgParams.descriptors(i).sequenceNo)) & "),    ", 16); _
           " "; Left("CAST(" & IIf(g_dbCfgParams.descriptors(i).serverPlatform = "", "NULL", "'" & g_dbCfgParams.descriptors(i).serverPlatform & "'") & " AS VARCHAR(5)),           ", 31); _
           " CAST("; IIf(g_dbCfgParams.descriptors(i).minDbRelease = "", "NULL", Replace(UCase(g_dbCfgParams.descriptors(i).minDbRelease), ",", ".")); " AS "; g_dbtDbRelease; ")"; ")"
       End If
   Next i
 
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(3); "V_ParmListOrdered"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "name,"
   Print #fileNo, addTab(3); "value,"
   Print #fileNo, addTab(3); "seqNoDeploy,"
   Print #fileNo, addTab(3); "seqNoReleaseMatch"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "name,"
   Print #fileNo, addTab(4); "value,"
   Print #fileNo, addTab(4); "seqNoDeploy,"
   Print #fileNo, addTab(4); "ROWNUMBER() OVER (PARTITION BY name ORDER BY COALESCE(minDbRelease,0) DESC)"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "V_ParmList"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "COALESCE(minDbRelease, dbRelease_out) <= dbRelease_out"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(serverPlatform, osPlatform_out) = osPlatform_out"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "name AS c_name,"
   Print #fileNo, addTab(3); "value AS c_value"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_ParmListOrdered"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "seqNoReleaseMatch = 1"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "seqNoDeploy,"
   Print #fileNo, addTab(3); "name"
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader(fileNo, "assemble UPDATE statement", 2, True)
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'UPDATE DB CFG FOR ' || RTRIM(CURRENT SERVER) || ' USING ' || c_name || ' ' || c_value;"
   Print #fileNo, addTab(2); "SET v_flag = '+';"
   Print #fileNo, addTab(2); "SET v_messageText = NULL;"
   Print #fileNo, addTab(2); "SET parmCount_out = parmCount_out + 1;"
 
   genProcSectionHeader(fileNo, "execute config-update-statement", 2)
   Print #fileNo, addTab(2); "IF mode_in >= 1 THEN"
   Print #fileNo, addTab(3); "SET v_catchException = "; gc_dbTrue; ";"
   Print #fileNo, addTab(3); "CALL SYSPROC.ADMIN_CMD(v_stmntTxt);"
   Print #fileNo, addTab(3); "SET v_catchException = "; gc_dbFalse; ";"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader(fileNo, "store statement in temporary table", 2)
   Print #fileNo, addTab(2); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameStatementDbCfg; " (flag, statement, msg) VALUES (v_flag, v_stmntTxt, v_messageText);"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(2); "SET parmCount_out = parmCount_out - failCount_out;"

   genProcSectionHeader(fileNo, "return result to application")
   Print #fileNo, addTab(1); "IF mode_in <= 1 THEN"
   Print #fileNo, addTab(2); "BEGIN"
   Print #fileNo, addTab(3); "DECLARE stmntCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "flag AS F,"
   Print #fileNo, addTab(5); "statement,"
   Print #fileNo, addTab(5); "msg"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); tempTabNameStatementDbCfg
   Print #fileNo, addTab(4); "ORDER BY"
   Print #fileNo, addTab(5); "seqno ASC"
   Print #fileNo, addTab(4); "FOR READ ONLY"
   Print #fileNo, addTab(3); ";"
 
   genProcSectionHeader(fileNo, "leave cursor open for application", 3)
   Print #fileNo, addTab(3); "OPEN stmntCursor;"

   Print #fileNo, addTab(2); "END;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF failCount_out = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(2); "RETURN 0;"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "RETURN 1;"
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   Print #fileNo, vbLf;
   Print #fileNo, vbLf;
   Print #fileNo, addTab(0); "CALL "; qualProcedureNameSetDbCfg; "(1, ?, ?, ?, ?)"; gc_sqlCmdDelim
   Print #fileNo, vbLf;
   Print #fileNo, addTab(0); "DROP PROCEDURE "; qualProcedureNameSetDbCfg; "(SMALLINT, VARCHAR(5), "; g_dbtDbRelease; ", INTEGER, INTEGER)"; gc_sqlCmdDelim
   Print #fileNo, vbLf;
 
   genScriptTrailer(fileNo, fileName)
 
 NormalExit:
   On Error Resume Next
   Close #fileNo

   stripCrInFile(fileName)

   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genScriptUpdDbProfile( _
   ByRef targetDir As String _
 )
   Dim fileName As String
   Dim fileNo As Integer

   On Error GoTo ErrorExit

   fileName = targetDir & "\" & dirScripts & "\" & fileUpdDbProfile
   assertDir(fileName)
   fileNo = FreeFile()

   Open fileName For Output As #fileNo
 
   genScriptHeader(fileNo, fileName, "Sets database profile parameters for MDS", True)
 
   Dim i As Integer
   For i = 1 To g_dbCfgParams.numDescriptors
       If g_dbCfgParams.descriptors(i).isDbProfileParam Then
         Print #fileNo, kwTranslate("db2set " & Trim(g_dbCfgParams.descriptors(i).parameter) & "=" & Trim(g_dbCfgParams.descriptors(i).value)); vbLf;
       End If
   Next i
 
   genScriptTrailer(fileNo, fileName, True)
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    create bufferpools, tablespaces and DB objects
 ' ####################################################################################################################
 
 Private Sub genScriptCreateTsAndObjects( _
   ByRef sourceDir As String, _
   ByRef targetDir As String, _
   ddlType As DdlTypeId _
 )
   Dim fileNameCrTs As String
   Dim fileNoCrTs As Integer
   Dim fileNameCrObj As String
   Dim fileNoCrObj As Integer

   On Error GoTo ErrorExit

   fileNameCrTs = targetDir & "\" & dirScripts & "\" & fileCreateTs
   fileNameCrObj = targetDir & "\" & dirScripts & "\" & fileCreateObjects

   assertDir(fileNameCrTs)
   assertDir(fileNameCrObj)

   fileNoCrTs = FreeFile()
   Open fileNameCrTs For Output As #fileNoCrTs
   fileNoCrObj = FreeFile()
   Open fileNameCrObj For Output As #fileNoCrObj

   genScriptHeader(fileNoCrTs, fileNameCrTs, "Creates Bufferpools and Tablespaces for the MDS database")
   genScriptHeader(fileNoCrObj, fileNameCrObj, "Creates all objects (tables, views, procedures etc) of the MDS database")

   catDdlsInDirRecursive(fileNoCrTs, fileNoCrObj, sourceDir, ddlType)

   genScriptTrailer(fileNoCrTs, fileNameCrTs)
   genScriptTrailer(fileNoCrObj, fileNameCrObj)
 
 NormalExit:
   On Error Resume Next
   Close #fileNoCrTs
   Close #fileNoCrObj
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    create DB objects for template DDL
 ' ####################################################################################################################
 
 Private Sub genScriptCreateObjectsTemplate( _
   ByRef sourceDir As String, _
   ByRef targetDir As String, _
   ddlType As DdlTypeId _
 )
   Dim fileNameCrObj As String
   Dim fileNoCrObj As Integer

   On Error GoTo ErrorExit

   Dim match As String
   Dim subDirs(1 To maxSubDirs) As String
   Dim numSubDirs As Integer
   numSubDirs = 0

   match = dir(sourceDir & "\*", vbDirectory)
   Do While match <> ""
     If numSubDirs < maxSubDirs And match <> "." And match <> ".." And UCase(match) <> ".SVN" And UCase(match) <> "DROP" Then
       If (GetAttr(sourceDir & "\" & match) And vbDirectory) = vbDirectory Then
         numSubDirs = numSubDirs + 1
         subDirs(numSubDirs) = match
       End If
     End If
     match = dir ' next entry.
   Loop
 
   Dim i As Integer
   For i = 1 To numSubDirs
     fileNameCrObj = targetDir & "\" & dirTemplate & "\" & subDirs(i) & "\" & fileCreateObjectsTemplate
     assertDir(fileNameCrObj)

     fileNoCrObj = FreeFile()
     Open fileNameCrObj For Output As #fileNoCrObj

     genScriptHeader(fileNoCrObj, fileNameCrObj, "Creates all objects (tables, views, procedures etc) of the MDS database")

     catDdlsInDirRecursive(-1, fileNoCrObj, sourceDir & "\" & subDirs(i), ddlType)

     genScriptTrailer(fileNoCrObj, fileNameCrObj, False, True)

     Close #fileNoCrObj
   Next i

 NormalExit:
   On Error Resume Next
   Close #fileNoCrObj
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    create external Stored Procedures
 ' ####################################################################################################################
 
 Private Sub genScriptCreateExternalProcedures( _
   ByRef sourceDir As String, _
   ByRef sourceJavaImplDir As String, _
   ByRef sourceNativeImplDir As String, _
   ByRef targetDir As String, _
   ByRef targetJavaImplDir As String, _
   ByRef targetNativeImplDir As String, _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer
   Dim fileNoSh As Integer

   On Error GoTo ErrorExit

   fileName = targetDir & "\" & dirScripts & "\" & fileCreateExtSps

   assertDir(fileName)

   fileNo = FreeFile()
   Open fileName For Output As #fileNo

   genScriptHeader(fileNo, fileName, "Creates external Stored Procedures for the MDS database")

   catDdlsInDirRecursive(-1, fileNo, sourceDir, ddlType, True, , True)

   genScriptTrailer(fileNo, fileName)

   cpImplModulesFromDir(sourceJavaImplDir, targetJavaImplDir, fnSuffixJar)
 
   cpImplModulesFromDir(sourceNativeImplDir, targetNativeImplDir, , , IIf(supportSpLogging And targetPlatform = "AIX" And spLogMode = esplFile, "", "splogger"))

   If supportSpLogging And targetPlatform = "AIX" And spLogMode = esplFile Then
     fileName = targetDir & "\" & dirScripts & "\" & fileCreateExtSpsSh

     assertDir(fileName)

     fileNoSh = FreeFile()
     Open fileName For Output As #fileNoSh

     genScriptHeader(fileNoSh, fileName, "Deploy Implementation Modules for external Stored Procedures for the MDS database", True)
 
     Print #fileNoSh, kwTranslate("spRootDir=`echo ""echo <dbInstance>/sqllib/function"" | /bin/ksh`"); vbLf;
     Print #fileNoSh, kwTranslate("spSubDir=""<spPathPrefix>/"""); vbLf;

     Print #fileNoSh, "dirMode=a+r+x "; vbLf;
     Print #fileNoSh, vbLf;
     Print #fileNoSh, "spSubDir=`echo $spSubDir | sed 's#/[\/]*#/#g'`"; vbLf;
     Print #fileNoSh, vbLf;
     Print #fileNoSh, "[ ""$spSubDir"" = '/' ] && spSubDir=''"; vbLf;
     Print #fileNoSh, vbLf;
     Print #fileNoSh, "spDir=""$spRootDir"""; vbLf;
     Print #fileNoSh, vbLf;
     Print #fileNoSh, "if [ ""$spSubDir"" ]; then"; vbLf;
     Print #fileNoSh, "  spDir=""$spRootDir/$spSubDir"""; vbLf;
     Print #fileNoSh, vbLf;
     Print #fileNoSh, "  echo ""Creating directory $spDir"""; vbLf;
     Print #fileNoSh, "  mkdir -p ""$spDir"" "; vbLf;
     Print #fileNoSh, vbLf;
     Print #fileNoSh, "  # fenced user needs to have access to deployment directory"; vbLf;
     Print #fileNoSh, "  thisDir=""$spSubDir"" "; vbLf;
     Print #fileNoSh, "  while [ ""$thisDir"" != '' ] ; do "; vbLf;
     Print #fileNoSh, "    echo ""Setting permissions '$dirMode' on directory $spRootDir/$thisDir"""; vbLf;
     Print #fileNoSh, "    chmod $dirMode ""$spRootDir/$thisDir"""; vbLf;
     Print #fileNoSh, "    thisDir=""`dirname ""$thisDir""`"""; vbLf;
     Print #fileNoSh, "    [ ""$thisDir"" = '.' -o ""$thisDir"" = '/' ] && thisDir=''"; vbLf;
     Print #fileNoSh, "  done"; vbLf;
     Print #fileNoSh, "fi"; vbLf;
     Print #fileNoSh, vbLf;
     Print #fileNoSh, "echo ""Copying Stored Procedure Modules to directory $spDir"" "; vbLf;
     Print #fileNoSh, "for module in ../install/obj/*; do"; vbLf;
     Print #fileNoSh, "  rm -f ""$spDir""/`basename $module`"; vbLf;
     Print #fileNoSh, "  cp $module ""$spDir"""; vbLf;
     Print #fileNoSh, "  chmod 644 ""$spDir""/`basename $module`"; vbLf;
     Print #fileNoSh, "done"; vbLf;

     genScriptTrailer(fileNoSh, fileName)
   End If
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Close #fileNoSh
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    import data
 ' ####################################################################################################################
 
 Private Sub genScriptImportData( _
   ByRef sourceDir As String, _
   ByRef targetDir As String, _
   ddlType As DdlTypeId _
 )
   Dim fileNameImpData As String
   Dim fileNoImpData As Integer

   On Error GoTo ErrorExit

   fileNameImpData = targetDir & "\" & dirScripts & "\" & fileImportData

   assertDir(fileNameImpData)

   fileNoImpData = FreeFile()
   Open fileNameImpData For Output As #fileNoImpData

   genScriptHeader(fileNoImpData, fileNameImpData, "Imports base data into the MDS database")

   cpCsvFromDirRecursive(fileNoImpData, sourceDir, targetDir & "\" & dirData, ddlType)

   genScriptTrailer(fileNoImpData, fileNameImpData)
 
 NormalExit:
   On Error Resume Next
   Close fileNoImpData
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    initialize meta data
 ' ####################################################################################################################
 
 Private Sub genScriptMetaData( _
   ByRef sourceDir As String, _
   ByRef targetDir As String, _
   ddlType As DdlTypeId _
 )
   Dim fileNameInMet As String
   Dim fileNoInMet As Integer

   On Error GoTo ErrorExit

   fileNameInMet = targetDir & "\" & dirScripts & "\" & fileInitMeta

   assertDir(fileNameInMet)

   fileNoInMet = FreeFile()
   Open fileNameInMet For Output As #fileNoInMet

   genScriptHeader(fileNoInMet, fileNameInMet, "initializes meta data in the MDS database")

   catDmlsInDir(fileNoInMet, sourceDir, ddlType)
 
   genScriptTrailer(fileNoInMet, fileNameInMet)
 
 NormalExit:
   On Error Resume Next
   Close #fileNoInMet
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    Bind CLI
 ' ####################################################################################################################
 
 Private Sub genScriptBindCli( _
   ByRef sourceDir As String, _
   ByRef targetDir As String, _
   ddlType As DdlTypeId _
 )
   Dim fileNameBindCli As String
   Dim fileNoBindCli As Integer
 
   On Error GoTo ErrorExit

   If Not bindJdbcPackagesWithReoptAlways Then
     Exit Sub
   End If

   fileNameBindCli = targetDir & "\" & dirScripts & "\" & fileRebindCli

   assertDir(fileNameBindCli)

   fileNoBindCli = FreeFile()
   Open fileNameBindCli For Output As #fileNoBindCli

   Dim schemaNamePackageReopt As String
   schemaNamePackageReopt = genSchemaName(snPackageReopt, ssnPackageReopt, edtPdm)

   genScriptHeader(fileNoBindCli, fileNameBindCli, "bind CLI in dedicated schema '" & schemaNamePackageReopt & "'")

   Print #fileNoBindCli, kwTranslate("BIND ""<dbBindDir>@db2cli.lst"" BLOCKING ALL CLIPKG 3 COLLECTION " & schemaNamePackageReopt & " GRANT PUBLIC REOPT ALWAYS"); vbLf;
   Print #fileNoBindCli, gc_sqlCmdDelim; vbLf;
 
   genScriptTrailer(fileNoBindCli, fileNameBindCli)
 
 NormalExit:
   On Error Resume Next
   Close #fileNoBindCli
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    grant permssions
 ' ####################################################################################################################
 
 Private Sub genScriptGrant( _
   ByRef targetDir As String, _
   ddlType As DdlTypeId _
 )
   Dim fileName As String
   Dim fileNo As Integer

   On Error GoTo ErrorExit

   fileName = targetDir & "\" & dirScripts & "\" & fileGrant

   assertDir(fileName)

   fileNo = FreeFile()
   Open fileName For Output As #fileNo

   genScriptHeader(fileNo, fileName, "grants privileges for the MDS database and objects in the database")

   If strArrayIsNull(environmentIds) Then
     Print #fileNo, "CALL "; genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType); "(1,?)"; gc_sqlCmdDelim; vbLf;
   Else
     Dim i As Integer
     For i = LBound(environmentIds) To UBound(environmentIds)
       Print #fileNo, "CALL "; genQualProcName(g_sectionIndexDbAdmin, spnGrant, ddlType, , , , "ByEnv", eondmNone); "('"; environmentIds(i); "',1,?)"; gc_sqlCmdDelim; vbLf;
     Next i
   End If
 
   genScriptTrailer(fileNo, fileName)
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 ' ####################################################################################################################
 ' #    generate package
 ' ####################################################################################################################
 
 Sub genPackageByDdlType( _
   ddlType As DdlTypeId, _
   forLrt As Boolean, _
   Optional packageType As DeployPackageType = edptFullDeployment _
 )
   Dim sourceDir As String

   sourceDir = _
     g_targetDir & _
     IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & _
     IIf(ddlType = edtLdm, "\LDM", "\PDM") & IIf(forLrt, "-LRT", "") & "\"

   Dim targetDir As String
   targetDir = _
     g_targetDir & _
     "\deploy" & _
     IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & _
     IIf(ddlType = edtLdm, "\LDM", "\PDM") & IIf(forLrt, "-LRT", "") & "\"

   logMsg("packaging deployment package to """ & targetDir & """", ellInfo, ddlType)

   On Error Resume Next
   Kill targetDir & "\" & dirScripts & "\" & "*.sql"
   Kill targetDir & "\" & dirData & "\" & "*.csv"

   On Error GoTo ErrorExit

   genReadMe(targetDir)

   genScriptFilesystemSetup(targetDir)
   genScriptCreateDb(targetDir)
   genScriptUpdDbCfg(targetDir)
   genScriptUpdDbProfile(targetDir)
   genScriptCreateTsAndObjects(sourceDir, targetDir, ddlType)
   genScriptCreateObjectsTemplate(sourceDir & "\template", targetDir, ddlType)
   genScriptCreateExternalProcedures(_
     sourceDir & "\Deploy", _
     sourceDir & "\Deploy\jar", _
     sourceDir & "\Deploy\obj\" & targetPlatform, _
     targetDir, _
     targetDir & "\" & dirInstall & "\jar", _
     targetDir & "\" & dirInstall & "\obj", _
     ddlType)

   genScriptGrant(targetDir, ddlType)
   genScriptImportData(sourceDir & "\CSV", targetDir, ddlType)
   genScriptMetaData(sourceDir & "\Deploy", targetDir, ddlType)
   genScriptBindCli(sourceDir & "\Deploy", targetDir, ddlType)
   cpEtcFilesFromDir(sourceDir & "\Deploy\" & dirEtc, targetDir & "\" & dirEtc, ddlType)
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
