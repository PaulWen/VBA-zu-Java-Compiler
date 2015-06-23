 Attribute VB_Name = "M04_Utilities"
 Option Explicit
 
 Dim nextOid As Integer
 
 Private ddlEmptyFileSize As Integer
 
 Private targetDir As String
 
 Public Enum ObjNameDelimMode
   eondmNone = 0
   eondmPrefix = 1
   eondmSuffix = 2
   eondmInfix = 4

   eondmAll = (eondmPrefix Or eondmSuffix Or eondmInfix)
   eondmFrame = (eondmPrefix Or eondmSuffix)
 End Enum

 Enum CodeScope
   ecsBase = 0
   ecsDecl = 1
   ecsBody = 2
 End Enum
 
 
 ' ### IF IVK ###
 Private Type ModuleDescriptor
   moduleName As String
   moduleType As vbext_ComponentType
 End Type

 Private Type ModuleDescriptors
   descriptors() As ModuleDescriptor
   numDescriptors As Integer
 End Type

 Private Type ProcDescriptor
   procName As String
   startsAtLine As Long
   hasErrorHandler As Boolean
   hasErrorExit As Boolean
 End Type

 Private Type ProcDescriptors
   descriptors() As ProcDescriptor
   numDescriptors As Integer
 End Type
 
 
 Function dirName( _
   fileName As String _
 ) As String
   Dim intI As Long
 
   If fileName = "" Then
     dirName = ""
     Exit Function
   End If
 
   For intI = Len(fileName) To 1 Step -1
     If Mid(fileName, intI, 1) = "/" Or _
         Mid(fileName, intI, 1) = "\" Then Exit For
   Next intI
 
   dirName = Left(fileName, IIf(intI > 0, intI - 1, intI))
 End Function
 
 
 Function baseName( _
   ByVal fileName As String, _
   Optional ByRef suffixList As String = "", _
   Optional ByRef delimiter As String = "", _
   Optional ByRef suffixDelimiter As String = "", _
   Optional suffixListDelimiter As String = "," _
 ) As String
   Dim intI As Long
   Dim lastI As Long
   Dim base As String
   Dim fileNameLen As Integer
   Dim suffixElems() As String

   If fileName = "" Then
     baseName = ""
     Exit Function
   End If

   suffixElems = split(suffixList, suffixListDelimiter)

   lastI = 0
   If delimiter = "" Then
     If Right(fileName, 1) = "\" Or Right(fileName, 1) = "/" Then
       fileName = Left(fileName, Len(fileName) - 1)
     End If

     fileNameLen = Len(fileName)
     For intI = 1 To fileNameLen Step 1
       If Mid(fileName, intI, 1) = "/" Or _
          Mid(fileName, intI, 1) = "\" Then
         lastI = intI
       End If
     Next intI
   Else
     fileNameLen = Len(fileName)
     For intI = 1 To fileNameLen Step 1
       If Mid(fileName, intI, 1) = delimiter Then
         lastI = intI
       End If
     Next intI
   End If

   base = Right(fileName, Len(fileName) - lastI)
   For intI = LBound(suffixElems) To UBound(suffixElems)
     If (suffixElems(intI) <> "") And (Right(base, Len(suffixElems(intI))) = (suffixElems(intI))) Then
       base = Left(base, Len(base) - Len(suffixElems(intI)))
       intI = UBound(suffixElems)
     End If
   Next intI

   If suffixDelimiter <> "" Then
     intI = InStr(base, suffixDelimiter)
     If intI > 0 Then
       base = Left(base, intI - 1)
     End If
   End If

   baseName = base
 End Function
 
 
 Sub addStrListElem( _
   ByRef strList As String, _
   ByRef elem As String _
 )
   Dim list() As String

   If Trim(strList) = "" Then
     strList = elem
     Exit Sub
   End If
   If Trim(elem) = "" Then
     Exit Sub
   End If

   list = split(strList, ",")
   Dim i As Integer
   For i = LBound(list) To UBound(list)
     If UCase(list(i)) = UCase(elem) Then
       Exit Sub
     End If
   Next i
 
   strList = strList & "," & elem
 End Sub
 
 Function setSheetName( _
   ByRef book As Workbook, _
   ByRef sheet As Worksheet, _
   ByRef name As String, _
   allowMerge As Boolean, _
   Optional force As Boolean = True _
 ) As Integer
   Dim oldSheet As Worksheet
   ' vbNo - add data to existing worksheet
   ' vbYes - delete old worksheet and replace by new sheet
   ' vbCancel - add data to new 'anonymous' worksheet

   setSheetName = vbNo

   On Error GoTo setName
   Set oldSheet = book.Worksheets(name)
   On Error Resume Next

   Dim rsp As Integer
   Dim options As Integer
   If allowMerge Then
     options = vbYesNoCancel
   Else
     options = vbOKCancel
   End If

   If force Then
     rsp = vbOK
   Else
     rsp = MsgBox("Do you want to replace sheet '" & name & "'?", vbCritical Or options, "Confirm delete of Worksheet")
   End If

   If rsp = vbOK Then
     rsp = vbYes
   End If

   If rsp = vbNo Then
     sheet.Delete
     oldSheet.Activate
     Exit Function
   ElseIf rsp = vbCancel Then
     setSheetName = vbCancel
     Exit Function
   End If

   oldSheet.Delete
 setName:
   sheet.Name = name
   sheet.Activate
   setSheetName = vbYes
 End Function
 
 
 Function verifyWorksheet( _
   ByRef name As String, _
   Optional ByRef tmplName As String = "" _
 ) As Boolean
   Dim xlWs As Worksheet
   verifyWorksheet = True

   On Error GoTo ErrorExit
   Set xlWs = Worksheets(name)
 
 NormalExit:
   Exit Function
 
 ErrorExit:
   If Trim(tmplName & "") <> "" Then
     Worksheets(tmplName).Copy AFTER:=Worksheets(1)
     Worksheets(2).Name = name
   Else
     Set xlWs = Worksheets.Add(, , 1, xlWorksheet)
     xlWs.Name = name
     xlWs.Rows.AutoFit
     xlWs.Columns.AutoFit
   End If

   verifyWorksheet = False
 End Function
 
 
 Function paddRight( _
   ByRef str As String, _
   Optional width As Integer = 25, _
   Optional cutOff As Boolean = False _
 ) As String
   If Not cutOff And Len(str) >= width Then
     paddRight = str
   Else
     paddRight = Left(str & "                                                                                                    ", width)
   End If
 End Function
 
 
 Function addTab( _
   Optional tabCount As Integer = 1 _
 ) As String
   Dim result As String
   result = ""

   Dim i As Integer
   For i = 1 To tabCount Step 1
     result = result & "  "
   Next i

   addTab = result
 End Function
 
 
 Function getBoolean( _
   ByVal str As String, _
   Optional ByRef query As String = "" _
 ) As Boolean
   str = UCase(Left(Trim(str & ""), 1))
   If str = "?" And query <> "" Then
     getBoolean = (MsgBox(query & "?", vbYesNo Or vbQuestion Or vbDefaultButton2) = vbYes)
     Exit Function
   End If

   getBoolean = (str = "X") Or (str = "1") Or (str = "J") Or (str = "Y")
 End Function
 
 
 Function getTvBoolean( _
   ByVal str As String, _
   Optional ByRef query As String = "" _
 ) As TvBoolean
   str = UCase(Left(Trim(str & ""), 1))
   Dim rsp As Integer
   If str = "?" And query <> "" Then
     rsp = (MsgBox(query & "?", vbYesNoCancel Or vbQuestion Or vbDefaultButton2) = vbYes)
     If rsp = vbYes Then
       getTvBoolean = tvTrue
     ElseIf rsp = vbNo Then
       getTvBoolean = tvFalse
     Else
       getTvBoolean = tvNull
     End If
     Exit Function
   End If

   If (str = "x") Or (str = "+") Or (str = "J") Or (str = "Y") Then
     getTvBoolean = tvTrue
   ElseIf (str = "-") Or (str = "N") Then
     getTvBoolean = tvFalse
   Else
     getTvBoolean = tvNull
   End If
 End Function
 
 
 Function getInteger( _
   ByRef str As String, _
   Optional default As Integer = -1 _
 ) As Integer
   On Error GoTo ErrorExit
   getInteger = CInt(str)
 
 NormalExit:
   Exit Function
 
 ErrorExit:
   getInteger = default
 End Function
 
 
 Function getLong( _
   ByRef str As String, _
   Optional default As Long = -1 _
 ) As Long
   On Error GoTo ErrorExit
   getLong = CLng(str)
 
 NormalExit:
   Exit Function
 
 ErrorExit:
   getLong = default
 End Function
 
 Function getSingle( _
   ByRef str As String, _
   Optional default As Single = -1 _
 ) As Single
   On Error GoTo ErrorExit
   getSingle = CSng(str)
 
 NormalExit:
   Exit Function
 
 ErrorExit:
   getSingle = default
 End Function
 
 
 Function getDbSpLogMode( _
   ByVal str As String _
 ) As DbSpLogMode
   str = UCase(str)
   getDbSpLogMode = IIf(str = "FILE", esplFile, IIf(str = "TABLE", esplTable, esplNone))
 End Function
 
 
 Function getIsEntityFiltered( _
   ByVal str As String _
 ) As Boolean
   If str = "" Then
     getIsEntityFiltered = False
   Else
     getIsEntityFiltered = InStr(1, "," & entityFilterKeys & ",", "," & Trim(str) & ",")
   End If
 End Function
 
 
 ' ### IF IVK ###
 Function getDbUpdateMode( _
   ByRef str As String _
 ) As DbUpdateMode
   str = UCase(str)

   If str = "" Then
     getDbUpdateMode = eupmAll
     Exit Function
   End If

   Dim list() As String
   list = split("", ".")
   list = split(str, ".")
   Dim result As DbUpdateMode
   result = eupmNone

   Dim i As Integer
   For i = LBound(list) To UBound(list)
     If list(i) = "X" Then
       result = eupmNone
     ElseIf list(i) = "I" Then
       result = result Or eupmInsert
     ElseIf list(i) = "U" Then
       result = result Or eupmUpdate
     ElseIf list(i) = "D" Then
       result = result Or eupmDelete
     End If
   Next i
   getDbUpdateMode = result
 End Function
 
 
 ' ### ENDIF IVK ###
 Function getFkMaintenanceMode( _
   ByVal str As String _
 ) As FkMaintenanceMode
   str = UCase(Left(str, 1))

   If str = "C" Then
     getFkMaintenanceMode = efkmCascade
   ElseIf str = "" Then
     getFkMaintenanceMode = efkmRestrict
   End If
 End Function
 
 
 Function genTemplateParamWrapper( _
   ByRef str As String, _
   Optional forOid As Boolean _
 ) As String
   If forOid Then
     genTemplateParamWrapper = "<<$" & IIf(forOid, "OID$", "") & str & "$>>"
   Else
 ' ### IF IVK ###
     genTemplateParamWrapper = "<<mpcId>>"
 ' ### ELSE IVK ###
 '   genTemplateParamWrapper = "<<orgId>>"
 ' ### ENDIF IVK ###
   End If
 End Function
 
 
 Function genOrgId( _
   ByVal thisOrgIndex As Integer, _
   ddlType As DdlTypeId, _
   Optional stripped As Boolean = False _
 ) As String
   If ddlType = edtLdm Then
     genOrgId = "0"
   ElseIf thisOrgIndex < 0 Then
     genOrgId = ""
   Else
       If g_orgs.descriptors(thisOrgIndex).isTemplate Then
         If stripped Then
           genOrgId = genTemplateParamWrapper(CStr(g_orgs.descriptors(thisOrgIndex).id))
         Else
           genOrgId = genTemplateParamWrapper(Right("00" & g_orgs.descriptors(thisOrgIndex).id, 2))
         End If
       Else
         If stripped Then
           genOrgId = CStr(g_orgs.descriptors(thisOrgIndex).id)
         Else
           genOrgId = Right("00" & g_orgs.descriptors(thisOrgIndex).id, 2)
         End If
       End If
   End If
 End Function
 
 
 Function genOrgIdByIndex( _
   ByVal thisOrgIndex As Integer, _
   ddlType As DdlTypeId, _
   Optional stripped As Boolean = False _
 ) As String
   If ddlType = edtLdm Then
     genOrgIdByIndex = "0"
   ElseIf thisOrgIndex < 0 Then
     genOrgIdByIndex = ""
   Else
       If g_orgs.descriptors(thisOrgIndex).isTemplate Then
         If stripped Then
           genOrgIdByIndex = genTemplateParamWrapper(CStr(g_orgs.descriptors(thisOrgIndex).id))
         Else
           genOrgIdByIndex = genTemplateParamWrapper(Right("00" & g_orgs.descriptors(thisOrgIndex).id, 2))
         End If
       Else
         If stripped Then
           genOrgIdByIndex = CStr(g_orgs.descriptors(thisOrgIndex).id)
         Else
           genOrgIdByIndex = Right("00" & g_orgs.descriptors(thisOrgIndex).id, 2)
         End If
       End If
   End If
 End Function
 
 
 Function genPoolId( _
   ByVal thisPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 ) As String
   If ddlType = edtLdm Then
     genPoolId = "0"
   Else
     If thisPoolIndex < 1 Then
       genPoolId = ""
     Else
       genPoolId = Right("0" & g_pools.descriptors(thisPoolIndex).id, 1)
     End If
   End If
 End Function
 
 
 Function genPoolIdByIndex( _
   ByVal thisPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 ) As String
   If ddlType = edtLdm Then
     genPoolIdByIndex = "0"
   Else
     If thisPoolIndex < 0 Then
       genPoolIdByIndex = ""
     Else
        genPoolIdByIndex = Right("0" & g_pools.descriptors(thisPoolIndex).id, 1)
     End If
   End If
 End Function
 
 
 Function genSchemaName( _
   ByRef sectName As String, _
   ByRef sectNameShort As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 ) As String
   genSchemaName = "X"

   If ddlType = edtLdm Then
     genSchemaName = UCase(sectName)
   ElseIf ddlType = edtPdm Then
     Dim thisOrgIdStr As String
     Dim thisPoolIdStr As String
     If thisOrgIndex > 0 Then thisOrgIdStr = genOrgIdByIndex(thisOrgIndex, ddlType) Else thisOrgIdStr = ""
     If thisPoolIndex > 0 Then thisPoolIdStr = genPoolIdByIndex(thisPoolIndex, ddlType) Else thisPoolIdStr = ""

     genSchemaName = _
       Replace( _
         Replace( _
           Replace( _
             Replace( _
               pdmSchemaNamePattern, _
               "<pk>", _
               productKey _
             ), _
             "<s>", _
             UCase(sectNameShort) _
           ), _
           "<o>", _
           thisOrgIdStr _
         ), _
         "<p>", _
         thisPoolIdStr _
       )
   End If
 End Function
 
 
 Function getObjBaseName( _
   ByRef qualObjName As String, _
   Optional ByRef delimiter As String = "." _
 ) As String
   Dim intI As Long, pos As Long
 
   If qualObjName = "" Then
     getObjBaseName = ""
     Exit Function
   End If
 
   pos = 0
   For intI = 1 To Len(qualObjName) Step 1
     If Mid(qualObjName, intI, 1) = delimiter Then
       pos = intI
       Exit For
     End If
   Next intI
 
   getObjBaseName = Right(qualObjName, Len(qualObjName) - pos)
 End Function
 
 
 Function genEnumObjName( _
   ByRef entityName As String, _
   Optional forNl As Boolean = False _
 ) As String
   Dim objName As String

   objName = entityName & "_ENUM"

   If forNl Then
     objName = genNlObjName(objName)
   End If

   genEnumObjName = objName
 End Function
 
 
 Function genNlObjName( _
   ByRef objName As String, _
   Optional ByRef attributeName As String = "", _
   Optional ByRef forGen As Boolean = False, _
   Optional abbreviate As Boolean = False _
 ) As String
   If abbreviate Then
     genNlObjName = objName & IIf(forGen, "G", "") & tabPrefixNl & "T"
   Else
     genNlObjName = objName & "_" & IIf(forGen, "GEN_", "") & tabPrefixNl & "_TEXT"
   End If
 End Function
 
 
 Function genNlObjShortName( _
   ByRef objName As String, _
   Optional ByRef attributeName As String = "", _
   Optional ByRef forGen As Boolean = False, _
   Optional abbreviate As Boolean = False _
 ) As String
   genNlObjShortName = objName & IIf(forGen, IIf(abbreviate, "G", "GEN"), "") & IIf(abbreviate, "NL", tabPrefixNl & "TXT")
 End Function
 
 
 Function genQualObjName( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   Optional ByRef objNameShort As String = "", _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False _
 ) As String
   Const delim = "_"
   genQualObjName = "X.X"

     If forNl Then
       genQualObjName = _
         genSchemaName(g_sections.descriptors(sectionIndex).sectionName, g_sections.descriptors(sectionIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex) & "." & _
           UCase(prefix) & IIf((prefix <> "") And (delimMode And eondmPrefix) <> eondmNone, delim, "") & _
           UCase(IIf(forNl, genNlObjName(objName, , forGen, abbreviate), objName)) & _
           IIf(forGen And Not forNl, IIf(abbreviate, gc_dbObjSuffixShortGen, IIf(delimMode And eondmInfix, delim, "") & gc_dbObjSuffixGen), "") & _
           IIf(forLrt, IIf(abbreviate, gc_dbObjSuffixShortLrt, IIf(delimMode And eondmInfix, delim, "") & gc_dbObjSuffixLrt), "") & _
           IIf(forMqt, IIf(abbreviate, gc_dbObjSuffixShortMqt, IIf(delimMode And eondmInfix, delim, "") & gc_dbObjSuffixMqt), "") & _
           IIf(suffix <> "" And (delimMode And eondmSuffix) <> eondmNone, delim, "") & UCase(suffix)
     Else
       genQualObjName = _
         genSchemaName(g_sections.descriptors(sectionIndex).sectionName, g_sections.descriptors(sectionIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex) & "." & _
           UCase(prefix) & IIf((prefix <> "") And (delimMode And eondmPrefix) <> eondmNone, delim, "") & _
           UCase(objName) & _
           IIf(forGen And Not forNl, IIf(abbreviate, gc_dbObjSuffixShortGen, IIf(delimMode And eondmInfix, delim, "") & gc_dbObjSuffixGen), "") & _
           IIf(forLrt, IIf(abbreviate, gc_dbObjSuffixShortLrt, IIf(delimMode And eondmInfix, delim, "") & gc_dbObjSuffixLrt), "") & _
           IIf(forMqt, IIf(abbreviate, gc_dbObjSuffixShortMqt, IIf(delimMode And eondmInfix, delim, "") & gc_dbObjSuffixMqt), "") & _
           IIf(suffix <> "" And (delimMode And eondmSuffix) <> eondmNone, delim, "") & UCase(suffix)
     End If
 End Function
 
 
 Function genQualIndexName( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   ByRef objNameShort As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional ByRef suffix As String = "" _
 ) As String
   Dim infix As String
   infix = IIf(forMqt, "M", "") & IIf(forLrt, "L", "") & IIf(forGen, "G", "") & IIf(forNl, "N", "")

     If Left(UCase(objNameShort), 4) = "IDX_" Then
       genQualIndexName = _
         genQualObjName(g_sections.descriptors(sectionIndex).sectionIndex, _
           objNameShort & infix, objNameShort & infix, ddlType, thisOrgIndex, thisPoolIndex, , , , , , suffix)
     Else
       genQualIndexName = _
         genQualObjName(g_sections.descriptors(sectionIndex).sectionIndex, _
           "IDX_" & objNameShort & infix, "IDX_" & objNameShort & infix, _
           ddlType, thisOrgIndex, thisPoolIndex, , , , , , suffix)
     End If
 End Function
 
 
 Function genQualTabName( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   ByRef objNameShort As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "" _
 ) As String
   genQualTabName = genQualObjName(sectionIndex, objName, objNameShort, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix)
 End Function
 
 
 Function genQualObjNameByClassIndex( _
   ByRef classIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional useOrParent As Boolean = False, _
   Optional inLrtAliasSchema As Boolean = False, _
   Optional forcePoolParams As Boolean = False, _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False, _
   Optional ByRef extraInfix As String = "" _
 ) As String
   On Error GoTo ErrorExit

   Dim effectiveClassIndex As Integer
   effectiveClassIndex = classIndex
   If useOrParent Then
     effectiveClassIndex = g_classes.descriptors(classIndex).orMappingSuperClassIndex
   End If

     Dim effectiveSectionIndex As Integer
     effectiveSectionIndex = IIf(inLrtAliasSchema, g_sectionIndexAliasLrt, g_classes.descriptors(effectiveClassIndex).sectionIndex)

     Dim thisPoolId As Integer
     Dim thisOrgId As Integer

     Dim commonItemsLocal As Boolean
     If thisPoolIndex <= 0 Then
       thisPoolId = -1
       commonItemsLocal = False
     Else
       commonItemsLocal = g_pools.descriptors(thisPoolIndex).commonItemsLocal
     End If

     If thisOrgId <= 0 Then
       thisOrgId = -1
       thisPoolId = -1
     End If

     Dim effectiveOrgIndex As Integer
     Dim effectivePoolIndex As Integer
     effectiveOrgIndex = getEffectiveOrgId(thisOrgIndex, g_classes.descriptors(effectiveClassIndex).isCommonToOrgs And Not forcePoolParams And Not commonItemsLocal)
     effectivePoolIndex = getEffectivePoolId(thisPoolIndex, (effectiveOrgIndex = -1 Or g_classes.descriptors(effectiveClassIndex).isCommonToPools) And Not forcePoolParams And Not commonItemsLocal)

     genQualObjNameByClassIndex = _
       genQualObjName(effectiveSectionIndex, g_classes.descriptors(effectiveClassIndex).className & extraInfix, g_classes.descriptors(effectiveClassIndex).shortName & extraInfix, ddlType, _
         effectiveOrgIndex, effectivePoolIndex, forGen, forLrt Or (forMqt And g_classes.descriptors(effectiveClassIndex).useMqtToImplementLrt), _
         forMqt And g_classes.descriptors(effectiveClassIndex).useMqtToImplementLrt, forNl, prefix, suffix, delimMode, abbreviate _
       )
 
 NormalExit:
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
 End Function
 
 
 Function genQualTabNameByClassIndex( _
   ByRef classIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional useOrParent As Boolean = False, _
   Optional inLrtAliasSchema As Boolean = False, _
   Optional forcePoolParams As Boolean = False _
 ) As String
   genQualTabNameByClassIndex = _
     genQualObjNameByClassIndex( _
       classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, , , useOrParent, inLrtAliasSchema, forcePoolParams _
     )
 End Function
 
 
 Function genQualNlTabNameByClassIndex( _
   ByRef classIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional useOrParent As Boolean = False, _
   Optional inLrtAliasSchema As Boolean = False, _
   Optional forcePoolParams As Boolean = False _
 ) As String
   genQualNlTabNameByClassIndex = _
     genQualObjNameByClassIndex( _
       classIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, True, , , useOrParent, inLrtAliasSchema, forcePoolParams _
     )
 End Function
 
 
 Function genQualObjNameByRelIndex( _
   ByRef relIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional inLrtAliasSchema As Boolean = False, _
   Optional forcePoolParams As Boolean = False, _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False, _
   Optional ByRef extraInfix As String = "" _
 ) As String
   On Error GoTo ErrorExit

     Dim effectiveSectionIndex  As Integer
     effectiveSectionIndex = IIf(inLrtAliasSchema, g_sectionIndexAliasLrt, g_relationships.descriptors(relIndex).sectionIndex)

     Dim commonItemsLocal As Boolean
     If thisPoolIndex > 0 Then
       commonItemsLocal = g_pools.descriptors(thisPoolIndex).commonItemsLocal
     Else
       commonItemsLocal = False
     End If

     If thisOrgIndex <= 0 Then
       thisPoolIndex = -1
     End If

     Dim effectiveOrgIndex As Integer
     Dim effectivePoolIndex As Integer
     effectiveOrgIndex = getEffectiveOrgIndex(thisOrgIndex, g_relationships.descriptors(relIndex).isCommonToOrgs And Not forcePoolParams And Not commonItemsLocal)
     effectivePoolIndex = getEffectivePoolIndex(thisPoolIndex, (effectiveOrgIndex = -1 Or g_relationships.descriptors(relIndex).isCommonToPools) And Not forcePoolParams And Not commonItemsLocal)

     genQualObjNameByRelIndex = _
       genQualObjName( _
         effectiveSectionIndex, g_relationships.descriptors(relIndex).relName & extraInfix, g_relationships.descriptors(relIndex).shortName & extraInfix, ddlType, _
         effectiveOrgIndex, effectivePoolIndex, False, forLrt, forMqt, forNl, _
         prefix, suffix, delimMode, abbreviate _
       )

 NormalExit:
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
 End Function
 
 
 Function genQualTabNameByRelIndex( _
   ByRef relIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional inLrtAliasSchema As Boolean = False, _
   Optional forcePoolParams As Boolean = False, _
   Optional ByRef prefix As String = "" _
 ) As String
   genQualTabNameByRelIndex = _
     genQualObjNameByRelIndex( _
       relIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, prefix, , inLrtAliasSchema, forcePoolParams, eondmInfix Or eondmSuffix _
     )
 End Function
 
 Function genQualObjNameByEnumIndex( _
   ByRef enumIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional inLrtAliasSchema As Boolean = False, _
   Optional forcePoolParams As Boolean = False, _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False, _
   Optional ByRef extraInfix As String = "" _
 ) As String
   On Error GoTo ErrorExit

     Dim effectiveSectionIndex  As Integer
     effectiveSectionIndex = IIf(inLrtAliasSchema, g_sectionIndexAliasLrt, g_enums.descriptors(enumIndex).sectionIndex)

     Dim commonItemsLocal As Boolean
     If thisPoolIndex > 0 Then
       commonItemsLocal = g_pools.descriptors(thisPoolIndex).commonItemsLocal
     Else
       commonItemsLocal = False
     End If

     If thisOrgIndex <= 0 Then
       thisPoolIndex = -1
     End If

     Dim effectiveOrgIndex As Integer
     Dim effectivePoolIndex As Integer
     effectiveOrgIndex = getEffectiveOrgId(thisOrgIndex, g_enums.descriptors(enumIndex).isCommonToOrgs And Not forcePoolParams And Not commonItemsLocal)
     effectivePoolIndex = getEffectivePoolId(thisPoolIndex, (effectiveOrgIndex = -1 Or g_enums.descriptors(enumIndex).isCommonToPools) And Not forcePoolParams And Not commonItemsLocal)

     genQualObjNameByEnumIndex = _
       genQualObjName( _
         effectiveSectionIndex, genEnumObjName(g_enums.descriptors(enumIndex).enumName) & extraInfix, genEnumObjName(g_enums.descriptors(enumIndex).shortName) & extraInfix, ddlType, _
         effectiveOrgIndex, effectivePoolIndex, False, False, False, forNl, prefix, suffix, delimMode, abbreviate _
       )
 
 NormalExit:
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
 End Function
 
 
 Function genQualTabNameByEnumIndex( _
   ByRef enumIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forNl As Boolean = False, _
   Optional inLrtAliasSchema As Boolean = False, _
   Optional forcePoolParams As Boolean = False _
 ) As String
   genQualTabNameByEnumIndex = _
     genQualObjNameByEnumIndex(enumIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, , , inLrtAliasSchema, forcePoolParams)
 End Function
 
 
 Function genQualObjNameByEntityIndex( _
   ByVal acmEntityIndex As Integer, _
   ByVal acmEntityType As AcmAttrContainerType, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional inLrtAliasSchema As Boolean = False, _
   Optional forcePoolParams As Boolean = False, _
   Optional useOrParent As Boolean = False, _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False, _
   Optional ByRef extraInfix As String = "" _
 ) As String
   If acmEntityType = eactClass Then
     genQualObjNameByEntityIndex = genQualObjNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix, useOrParent, inLrtAliasSchema, forcePoolParams, delimMode, abbreviate, extraInfix)
   ElseIf acmEntityType = eactRelationship Then
     genQualObjNameByEntityIndex = genQualObjNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, prefix, suffix, inLrtAliasSchema, forcePoolParams, delimMode, abbreviate, extraInfix)
   ElseIf acmEntityType = eactEnum Then
     genQualObjNameByEntityIndex = genQualObjNameByEnumIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, prefix, suffix, inLrtAliasSchema, forcePoolParams, delimMode, abbreviate, extraInfix)
   End If
 End Function
 

 Function genQualTabNameByEntityIndex( _
   ByVal acmEntityIndex As Integer, _
   ByVal acmEntityType As AcmAttrContainerType, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional inLrtAliasSchema As Boolean = False, _
   Optional forcePoolParams As Boolean = False, _
   Optional useOrParent As Boolean = False _
 ) As String
   If acmEntityType = eactClass Then
     genQualTabNameByEntityIndex = genQualTabNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, useOrParent, inLrtAliasSchema, forcePoolParams)
   ElseIf acmEntityType = eactRelationship Then
     genQualTabNameByEntityIndex = genQualTabNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, inLrtAliasSchema, forcePoolParams)
   ElseIf acmEntityType = eactEnum Then
     genQualTabNameByEntityIndex = genQualTabNameByEnumIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, inLrtAliasSchema, forcePoolParams)
   End If
 End Function


 Function genQualViewName( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   ByRef objNameShort As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False _
 ) As String
   genQualViewName = _
     genQualObjName( _
       sectionIndex, objName, objNameShort, ddlType, thisOrgIndex, thisPoolIndex, _
       forGen, forLrt, forMqt, forNl, IIf(prefix = "", "V", "V_") & prefix, suffix, delimMode, abbreviate _
     )
 End Function
 
 
 Function genQualViewNameByClassIndex( _
   ByRef classIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False _
 ) As String
     genQualViewNameByClassIndex = _
       genQualViewName( _
         g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, _
         forGen, forLrt And g_classes.descriptors(classIndex).isUserTransactional, forMqt And g_classes.descriptors(classIndex).useMqtToImplementLrt, forNl, _
         prefix, suffix, delimMode, abbreviate _
       )
 End Function
 
 
 Function genQualViewNameByRelIndex( _
   ByRef relIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False _
 ) As String
     genQualViewNameByRelIndex = _
       genQualViewName( _
         g_relationships.descriptors(relIndex).sectionIndex, g_relationships.descriptors(relIndex).relName, g_relationships.descriptors(relIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, _
         False, forLrt, forMqt And g_relationships.descriptors(relIndex).useMqtToImplementLrt, forNl, prefix, suffix, delimMode, abbreviate _
       )
 End Function
 
 
 Function genQualViewNameByEnumIndex( _
   ByRef enumIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False _
 ) As String
     genQualViewNameByEnumIndex = _
       genQualViewName( _
         g_enums.descriptors(enumIndex).sectionIndex, genEnumObjName(g_enums.descriptors(enumIndex).enumName), genEnumObjName(g_enums.descriptors(enumIndex).shortName), ddlType, thisOrgIndex, thisPoolIndex, _
         False, False, False, forNl, prefix, suffix, delimMode, abbreviate _
       )
 End Function
 
 
 Function genQualViewNameByEntityIndex( _
   ByVal acmEntityIndex As Integer, _
   ByVal acmEntityType As AcmAttrContainerType, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False _
 ) As String
   If acmEntityType = eactClass Then
     genQualViewNameByEntityIndex = genQualViewNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix, delimMode, abbreviate)
   ElseIf acmEntityType = eactRelationship Then
     genQualViewNameByEntityIndex = genQualViewNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, prefix, suffix, delimMode, abbreviate)
   ElseIf acmEntityType = eactEnum Then
     genQualViewNameByEntityIndex = genQualViewNameByEnumIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, prefix, suffix, delimMode, abbreviate)
   End If
 End Function

 
 Function genQualProcName( _
   ByRef sectionIndex As Integer, _
   ByRef procName As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False _
 ) As String
   genQualProcName = genQualObjName(sectionIndex, procName, , ddlType, thisOrgIndex, thisPoolIndex, , , , , prefix, suffix, delimMode)
 End Function
 
 
 Function genQualProcNameByEntityIndex( _
   acmEntityIndex As Integer, _
   acmEntityType As AcmAttrContainerType, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False, _
   Optional ByRef extraInfix As String = "" _
 ) As String
   genQualProcNameByEntityIndex = genQualObjNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix, , , , delimMode, abbreviate, extraInfix)
 End Function
 
 
 Function genQualFuncName( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional ByRef suffix As String = "", _
   Optional ByRef noPrefix As Boolean = False _
 ) As String
   If noPrefix Then
     genQualFuncName = _
       genQualObjName(sectionIndex, objName, objName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, , , , suffix)
   Else
     genQualFuncName = _
       genQualObjName(sectionIndex, "F_" & objName, "F_" & objName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, , , , suffix)
   End If
 End Function
 
 
 Function genQualTriggerName( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   ByRef objNameShort As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmSuffix, _
   Optional abbreviate As Boolean = True _
 ) As String
   genQualTriggerName = _
     genQualObjName( _
       sectionIndex, objNameShort, objNameShort, ddlType, thisOrgIndex, thisPoolIndex, _
       forGen, forLrt, forMqt, forNl, "TR_" & prefix, suffix, delimMode, abbreviate _
     )
 End Function
 
 
 ' ### IF IVK ###
 Function genQualTriggerNameByClassIndex( _
   ByRef classIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional ByVal delimMode As ObjNameDelimMode = eondmNone, _
   Optional abbreviate As Boolean = True _
 ) As String
 ' ### ELSE IVK ###
 'Function genQualTriggerNameByClassIndex( _
 ' ByRef classIndex As Integer, _
 ' Optional ByRef ddlType As DdlTypeId = edtLdm, _
 ' Optional ByVal thisOrgIndex As Integer = -1, _
 ' Optional ByVal thisPoolIndex As Integer = -1, _
 ' Optional forGen As Boolean = False, _
 ' Optional forLrt As Boolean = False, _
 ' Optional forMqt As Boolean = False, _
 ' Optional forNl As Boolean = False, _
 ' Optional ByRef prefix As String = "", _
 ' Optional ByRef suffix As String = "", _
 ' Optional delimMode As ObjNameDelimMode = eondmSuffix, _
 ' Optional abbreviate As Boolean = True _
 ') As String
 ' ### ENDIF IVK ###
     genQualTriggerNameByClassIndex = _
       genQualTriggerName( _
         g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, g_classes.descriptors(classIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, _
         forGen, forLrt And g_classes.descriptors(classIndex).isUserTransactional, forMqt And g_classes.descriptors(classIndex).useMqtToImplementLrt, forNl, _
         prefix, suffix, delimMode, abbreviate _
       )
 End Function
 
 
 ' ### IF IVK ###
 Function genQualTriggerNameByRelIndex( _
   ByRef relIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional ByVal delimMode As ObjNameDelimMode = eondmNone, _
   Optional abbreviate As Boolean = True _
 ) As String
 ' ### ELSE IVK ###
 'Function genQualTriggerNameByRelIndex( _
 ' ByRef relIndex As Integer, _
 ' Optional ByRef ddlType As DdlTypeId = edtLdm, _
 ' Optional ByVal thisOrgIndex As Integer = -1, _
 ' Optional ByVal thisPoolIndex As Integer = -1, _
 ' Optional forLrt As Boolean = False, _
 ' Optional forMqt As Boolean = False, _
 ' Optional forNl As Boolean = False, _
 ' Optional ByRef prefix As String = "", _
 ' Optional ByRef suffix As String = "", _
 ' Optional delimMode As ObjNameDelimMode = eondmSuffix, _
 ' Optional abbreviate As Boolean = True _
 ') As String
 ' ### ENDIF IVK ###
     genQualTriggerNameByRelIndex = _
       genQualTriggerName( _
         g_relationships.descriptors(relIndex).sectionIndex, g_relationships.descriptors(relIndex).relName, g_relationships.descriptors(relIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, _
         False, forLrt And g_relationships.descriptors(relIndex).isUserTransactional, forMqt And g_relationships.descriptors(relIndex).useMqtToImplementLrt, forNl, _
         prefix, suffix, delimMode, abbreviate _
       )
 End Function
 
 
 ' ### IF IVK ###
 Function genQualTriggerNameByEnumIndex( _
   ByRef enumIndex As Integer, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional ByVal delimMode As ObjNameDelimMode = eondmNone, _
   Optional abbreviate As Boolean = True _
 ) As String
 ' ### ELSE IVK ###
 'Function genQualTriggerNameByEnumIndex( _
 ' ByRef enumIndex As Integer, _
 ' Optional ByRef ddlType As DdlTypeId = edtLdm, _
 ' Optional ByVal thisOrgIndex As Integer = -1, _
 ' Optional ByVal thisPoolIndex As Integer = -1, _
 ' Optional forNl As Boolean = False, _
 ' Optional ByRef prefix As String = "", _
 ' Optional ByRef suffix As String = "", _
 ' Optional delimMode As ObjNameDelimMode = eondmSuffix, _
 ' Optional abbreviate As Boolean = True _
 ') As String
 ' ### ENDIF IVK ###
     genQualTriggerNameByEnumIndex = _
       genQualTriggerName( _
         g_enums.descriptors(enumIndex).sectionIndex, g_enums.descriptors(enumIndex).enumName, g_enums.descriptors(enumIndex).shortName, ddlType, thisOrgIndex, thisPoolIndex, _
         False, False, False, forNl, prefix, suffix, delimMode, abbreviate _
       )
 End Function
 
 
 ' ### IF IVK ###
 Function genQualTriggerNameByEntityIndex( _
   ByVal acmEntityIndex As Integer, _
   ByVal acmEntityType As AcmAttrContainerType, _
   Optional ByRef ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False, _
   Optional forNl As Boolean = False, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional ByVal delimMode As ObjNameDelimMode = eondmNone, _
   Optional abbreviate As Boolean = True _
 ) As String
 ' ### ELSE IVK ###
 'Function genQualTriggerNameByEntityIndex( _
 ' ByVal acmEntityIndex As Integer, _
 ' ByVal acmEntityType As AcmAttrContainerType, _
 ' Optional ByRef ddlType As DdlTypeId = edtLdm, _
 ' Optional ByVal thisOrgIndex As Integer = -1, _
 ' Optional ByVal thisPoolIndex As Integer = -1, _
 ' Optional forGen As Boolean = False, _
 ' Optional forLrt As Boolean = False, _
 ' Optional forMqt As Boolean = False, _
 ' Optional forNl As Boolean = False, _
 ' Optional ByRef prefix As String = "", _
 ' Optional ByRef suffix As String = "", _
 ' Optional ByVal delimMode As ObjNameDelimMode = eondmSuffix, _
 ' Optional abbreviate As Boolean = True _
 ') As String
 ' ### ENDIF IVK ###
   If acmEntityType = eactClass Then
     genQualTriggerNameByEntityIndex = genQualTriggerNameByClassIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forMqt, forNl, prefix, suffix, delimMode, abbreviate)
   ElseIf acmEntityType = eactRelationship Then
     genQualTriggerNameByEntityIndex = genQualTriggerNameByRelIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forLrt, forMqt, forNl, prefix, suffix, delimMode, abbreviate)
   ElseIf acmEntityType = eactEnum Then
     genQualTriggerNameByEntityIndex = genQualTriggerNameByEnumIndex(acmEntityIndex, ddlType, thisOrgIndex, thisPoolIndex, forNl, prefix, suffix, delimMode, abbreviate)
   End If
 End Function
 
 
 Function genQualSeqName( _
   ByRef sectionIndex As Integer, _
   ByRef seqName As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef prefix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional delimMode As ObjNameDelimMode = eondmAll, _
   Optional abbreviate As Boolean = False _
 ) As String
   genQualSeqName = genQualObjName(sectionIndex, seqName, seqName, ddlType, thisOrgIndex, thisPoolIndex, , , , , prefix, suffix, delimMode, abbreviate)
 End Function
 
 
 ' ### IF IVK ###
 Function genQualAliasName( _
   ByRef objName As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional aliasType As DbAliasEntityType, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional ByRef suffix As String = "", _
   Optional forLrtSchema As Boolean = False, _
   Optional forDeletedObjects As Boolean = False, _
   Optional forPsDpFilter As Boolean = False, _
   Optional forPsDpFilterExtended As Boolean = False, _
   Optional suppressGenSuffix As Boolean = False _
 ) As String
 ' ### ELSE IVK ###
 'Function genQualAliasName( _
 ' ByRef objName As String, _
 ' ByRef objNameShort As String, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional ByVal thisOrgIndex As Integer = -1, _
 ' Optional ByVal thisPoolIndex As Integer = -1, _
 ' Optional aliasType As DbAliasEntityType, _
 ' Optional forGen As Boolean = False, _
 ' Optional forLrt As Boolean = False, _
 ' Optional byref suffix As String = "", _
 ' Optional forLrtSchema As Boolean = False, _
 ' Optional suppressGenSuffix As Boolean = False _
 ') As String
 ' ### ENDIF IVK ###
   genQualAliasName = ""

   Dim result As String
   Dim sectionIndex As Integer
 ' ### IF IVK ###
   If forPsDpFilter Then
     sectionIndex = g_sectionIndexAliasPsDpFiltered
   ElseIf forPsDpFilterExtended Then
     sectionIndex = g_sectionIndexAliasPsDpFilteredExtended
   ElseIf forDeletedObjects Then
     sectionIndex = g_sectionindexAliasDelObj
   ElseIf forLrtSchema Then
 ' ### ELSE IVK ###
 ' If forLrtSchema Then
 ' ### ENDIF IVK ###
     sectionIndex = g_sectionIndexAliasLrt
   Else
     sectionIndex = g_sectionIndexAlias
   End If

   If ddlType = edtPdm Then
     If aliasType = edatTable Then
       result = _
         genQualTabName(sectionIndex, objName, objName, ddlType, thisOrgIndex, thisPoolIndex, _
           forGen And Not suppressGenSuffix, forLrt And Not forLrtSchema, , , , suffix)
     ElseIf aliasType = edatView Then
       result = _
         genQualViewName(sectionIndex, objName, objName, ddlType, thisOrgIndex, thisPoolIndex, _
           forGen And Not suppressGenSuffix, forLrt And Not forLrtSchema, , , , suffix)
     End If
   End If

   If InStr(1, result, "NL_TEXT_GEN") Then
     ' FixMe: Hack!! cleanup concepts for generating table names!
     genQualAliasName = Replace(result, "_NL_TEXT_GEN", "_GEN_NL_TEXT", 1)
   Else
     genQualAliasName = result
   End If
 End Function
 
 
 Function genBufferPoolNameByIndex( _
   thisBufPoolIndex As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtPdm _
 ) As String
     genBufferPoolNameByIndex = UCase(Left(g_bufPools.descriptors(thisBufPoolIndex).bufPoolName, gc_dbMaxBufferPoolNameLength - 1))

     If g_bufPools.descriptors(thisBufPoolIndex).isCommonToOrgs Then
       Exit Function
     End If

     Dim thisOrgIdString As String
     thisOrgIdString = genOrgId(thisOrgIndex, ddlType)
     If (g_bufPools.descriptors(thisBufPoolIndex).isCommonToPools Or thisPoolIndex <= 0) And thisOrgIndex > 0 Then
       genBufferPoolNameByIndex = UCase(Left(g_bufPools.descriptors(thisBufPoolIndex).bufPoolName, gc_dbMaxBufferPoolNameLength - 1 - Len(thisOrgIdString)) & thisOrgIdString)
       Exit Function
     End If

     Dim thisPoolIdString As String
     thisPoolIdString = genPoolId(thisPoolIndex, ddlType)
     If thisOrgIndex > 0 And thisPoolIndex > 0 Then
       genBufferPoolNameByIndex = UCase(Left(g_bufPools.descriptors(thisBufPoolIndex).bufPoolName, gc_dbMaxBufferPoolNameLength - 1 - Len(thisOrgIdString) - Len(thisPoolIdString)) & thisOrgIdString & thisPoolIdString)
     End If
 End Function
 
 
 Function genTablespaceNameByIndex( _
   ByRef thisTabSpaceIndex As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtPdm _
 ) As String
     genTablespaceNameByIndex = UCase(Left(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName, gc_dbMaxTablespaceNameLength - 1))

     If g_tableSpaces.descriptors(thisTabSpaceIndex).isCommonToOrgs Then
       Exit Function
     End If

     Dim thisOrgIdString As String
     thisOrgIdString = genOrgId(thisOrgIndex, ddlType)
     If (g_tableSpaces.descriptors(thisTabSpaceIndex).isCommonToPools Or thisPoolIndex <= 0) And thisOrgIndex > 0 Then
       genTablespaceNameByIndex = UCase(Left(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName, gc_dbMaxTablespaceNameLength - 1 - Len(thisOrgIdString)) & thisOrgIdString)
       Exit Function
     End If

     Dim thisPoolIdString As String
     thisPoolIdString = genPoolId(thisPoolIndex, ddlType)
     If thisOrgIndex > 0 And thisPoolIndex > 0 Then
       genTablespaceNameByIndex = UCase(Left(g_tableSpaces.descriptors(thisTabSpaceIndex).tableSpaceName, gc_dbMaxTablespaceNameLength - 1 - Len(thisOrgIdString) - Len(thisPoolIdString)) & thisOrgIdString & thisPoolIdString)
     End If
 End Function
 
 
 Function genContainerNameByIndex( _
   thisContainerIndex As Integer, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ddlType As DdlTypeId = edtPdm _
 ) As String
     genContainerNameByIndex = g_containers.descriptors(thisContainerIndex).containerName

     If g_containers.descriptors(thisContainerIndex).isCommonToOrgs Then
       Exit Function
     End If

     Dim thisOrgIdString As String
     thisOrgIdString = genOrgId(thisOrgIndex, ddlType)
     If (g_containers.descriptors(thisContainerIndex).isCommonToPools Or thisPoolIndex <= 0) And thisOrgIndex > 0 Then
       genContainerNameByIndex = g_containers.descriptors(thisContainerIndex).containerName & thisOrgIdString
       Exit Function
     End If

     Dim thisPoolIdString As String
     thisPoolIdString = genPoolId(thisPoolIndex, ddlType)
     If thisOrgIndex > 0 And thisPoolIndex > 0 Then
       genContainerNameByIndex = g_containers.descriptors(thisContainerIndex).containerName & thisOrgIdString & thisPoolIdString
     End If
 End Function
 
 
 Function genGetUserNameByIdDdl( _
   ByRef cdUserId As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 ) As String
   If InStr(UCase(cdUserId), "CREATE") Then
     genGetUserNameByIdDdl = _
       "COALESCE((SELECT U." & g_anUserName & " FROM " & g_qualTabNameUser & _
       " U WHERE U." & g_anUserId & " = " & cdUserId & "), '[' || " & cdUserId & " || ']')"
   Else
     genGetUserNameByIdDdl = _
       "COALESCE((SELECT U." & g_anUserName & " FROM " & g_qualTabNameUser & _
       " U WHERE U." & g_anUserId & " = " & cdUserId & "), (CASE WHEN " & _
       cdUserId & " = '' THEN '' ELSE '[' || " & cdUserId & " || ']' END))"
   End If
 End Function
 
 
 ' ### IF IVK ###
 Function mapExpression( _
   ByVal expression As String, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   ddlType As DdlTypeId, _
   Optional ByRef tabQualifier1 As String = "", _
   Optional ByRef tabQualifier2 As String = "", _
   Optional ByRef lrtOidRef As String = "" _
 ) As String
   On Error GoTo ErrorExit

   If Left(expression, 1) = "#" Then
     expression = Right(expression, Len(expression) - 1)
   End If

   expression = Replace(expression, ";", ",")
   mapExpression = expression

   ' map section parameters
   Dim sPosStart As Integer
   sPosStart = InStr(1, expression, "<%S")
   While sPosStart > 0
     Dim sPosEnd As Integer
     Dim sectionExpression As String
     Dim schemaName As String
     sPosEnd = InStr(sPosStart + 2, expression, ">")

     sectionExpression = Mid(expression, sPosStart + 1, CLng(sPosEnd - sPosStart - 1))

     If Left(sectionExpression, 3) <> "%S(" Or Right(sectionExpression, 1) <> ")" Then
       GoTo SyntaxError
     End If

     Dim isCtoSchema As Boolean
     Dim isCtpSchema As Boolean

     isCtoSchema = False
     isCtpSchema = False
     schemaName = Mid(sectionExpression, 4, Len(sectionExpression) - 4)
     If Left(schemaName, 5) = "[cto]" Then
       isCtoSchema = True
       isCtpSchema = True
       schemaName = Right(schemaName, Len(schemaName) - 5)
     End If
     If Left(schemaName, 5) = "[ctp]" Then
       isCtpSchema = True
       schemaName = Right(schemaName, Len(schemaName) - 5)
     End If
     schemaName = genSchemaName(schemaName, getSectionShortNameByName(schemaName), ddlType, IIf(isCtoSchema, -1, thisOrgIndex), IIf(isCtpSchema, -1, thisPoolIndex))
     expression = Left(expression, sPosStart - 1) & schemaName & "." & Mid(expression, sPosEnd + 1)

     sPosStart = InStr(1, expression, "<%S")
   Wend

   ' map tab qualifiers
   Dim tPos As Integer
   tPos = InStr(1, expression, "<%T>")
   While tPos > 0
     expression = Left(expression, tPos - 1) & tabQualifier1 & IIf(tabQualifier1 = "" Or Right(tabQualifier1, 1) = ".", "", ".") & Mid(expression, tPos + 4)
     tPos = InStr(1, expression, "<%T>")
   Wend
   If tabQualifier2 <> "" Then
     tPos = InStr(1, expression, "<%T2>")
     While tPos > 0
       expression = Left(expression, tPos - 1) & tabQualifier1 & IIf(Right(tabQualifier2, 1) = ".", "", ".") & Mid(expression, tPos + 5)
       tPos = InStr(1, expression, "<%T2>")
     Wend
   End If

   ' map lrtOid reference
   Dim lPos As Integer
   lPos = InStr(1, expression, "<,%L>")
   While lPos > 0
     expression = Left(expression, lPos - 1) & IIf(lrtOidRef = "", "", "," & lrtOidRef) & Mid(expression, lPos + 5)
     lPos = InStr(1, expression, "<,%L>")
   Wend
   lPos = InStr(1, expression, "<, %L>")
   While lPos > 0
     expression = Left(expression, lPos - 1) & IIf(lrtOidRef = "", "", ", " & lrtOidRef) & Mid(expression, lPos + 6)
     lPos = InStr(1, expression, "<, %L>")
   Wend

   mapExpression = expression
 
 NormalExit:
   Exit Function
 
 SyntaxError:
   logMsg("Syntax Error in attribute mapping expression """ & expression & """", ellError, edtNone)
   GoTo NormalExit
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 
 
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
 Function transformAttrName( _
   ByRef db2AttrName As String, _
   ByVal valueType As AttrValueType, _
   ByRef valueTypeIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional useDomain As Boolean = True, _
   Optional ByRef infix As String = "", _
   Optional transformToConstant As Boolean = False, _
   Optional isVirtual As Boolean = False, _
   Optional attrIndex As Integer = -1, _
   Optional ByVal outputMode As DdlOutputMode = edomList, _
   Optional isNullable As Boolean = True, _
   Optional ByVal persisted As Boolean = True, _
   Optional forRead As Boolean = False, _
   Optional attrCat As AttrCategory = eacRegular _
 ) As String
 ' ### ELSE IVK ###
 'Function transformAttrName( _
 ' ByRef db2AttrName As String, _
 ' ByVal valueType as AttrValueType, _
 ' ByRef valueTypeIndex As Integer, _
 ' ByRef transformation As AttributeListTransformation, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional useDomain As Boolean = True, _
 ' Optional byref infix As String = "", _
 ' Optional transformToConstant As Boolean = False, _
 ' Optional attrIndex As Integer = -1, _
 ' Optional ByVal outputMode As DdlOutputMode = edomList, _
 ' Optional isNullable As Boolean = True, _
 ' Optional forRead As Boolean = False, _
 ' Optional attrCat As AttrCategory = eacRegular _
 ') As String
 ' ### ENDIF IVK ###

   Dim i As Integer
   Dim name As String
   transformToConstant = False
   transformAttrName = ""

   Dim effectiveDomainIndex As Integer
   If valueTypeIndex > 0 Then
     If valueType = eavtDomain Then
       effectiveDomainIndex = valueTypeIndex
     ElseIf valueType = eavtEnum Then
       effectiveDomainIndex = g_enums.descriptors(valueTypeIndex).domainIndexId
       db2AttrName = Left(db2AttrName & gc_enumAttrNameSuffix, gc_dbMaxAttributeNameLength)
     ElseIf valueType = eavtDomainEnumId Then
       effectiveDomainIndex = g_enums.descriptors(valueTypeIndex).domainIndexId
     ElseIf valueType = eavtDomainEnumValue Then
       effectiveDomainIndex = g_enums.descriptors(valueTypeIndex).domainIndexValue
     End If
   End If

 ' ### IF IVK ###
     If isVirtual And Not persisted And (outputMode And edomVirtualPersisted) Then
       Exit Function
     End If
 
     If ((outputMode And edomListVirtual <> 0) And (outputMode And edomValueVirtualNonPersisted <> 0)) Then
       ' two alternative options specified - check if this attribute is persisted
       If Not persisted Then
         outputMode = outputMode And (Not edomListVirtual)
         outputMode = outputMode Or edomValueVirtual
       End If
     End If
 
     If (isVirtual And transformation.doCollectVirtualDomainDescriptors) Or (Not isVirtual And transformation.doCollectDomainDescriptors) Then
       addDomainDescriptorRef(transformation.domainRefs, effectiveDomainIndex, isNullable, transformation.distinguishNullabilityForDomainRefs)
     End If
     If isVirtual And Not persisted And ((outputMode And edomListVirtual) <> 0) And ((outputMode And edomValueVirtual) <> 0) Then
       outputMode = outputMode And (Not edomListVirtual)
     End If

 ' ### ENDIF IVK ###
     For i = 1 To transformation.numMappings Step 1
         If IIf(transformation.mappings(i).attributeName <> "", UCase(transformation.mappings(i).attributeName) = UCase(db2AttrName), transformation.mappings(i).domainSection <> "") And _
            IIf(useDomain, _
                 (transformation.mappings(i).domainSection = "" Or UCase(transformation.mappings(i).domainSection) = UCase(g_domains.descriptors(effectiveDomainIndex).sectionName)) And _
                 (transformation.mappings(i).domainName = "" Or UCase(transformation.mappings(i).domainName) = UCase(g_domains.descriptors(effectiveDomainIndex).domainName)), _
                 True) Then
           name = transformation.mappings(i).value
           transformToConstant = transformation.mappings(i).isConstant
           If Not transformation.postProcessAfterMapping Then
             transformAttrName = name
             Exit Function
           End If
         End If
     Next i

 ' ### IF IVK ###
     If (outputMode And edomValueVirtual) And name = "" And isVirtual And attrIndex > 0 Then
       If g_attributes.descriptors(attrIndex).virtuallyMapsTo.isRelBasedMapping Then
         ' FixMe: implement this ...
       Else
         If (outputMode And edomVirtualPersisted) And g_attributes.descriptors(attrIndex).isPersistent Then
           ' handle as regular attribute
         Else
           If forRead And g_attributes.descriptors(attrIndex).virtuallyMapsToForRead.description <> "" Then
             transformAttrName = _
               mapExpression( _
                 g_attributes.descriptors(attrIndex).virtuallyMapsToForRead.mapTo, _
                 transformation.conEnumLabelText.orgIndex, _
                 transformation.conEnumLabelText.poolIndex, _
                 ddlType, _
                 transformation.conEnumLabelText.tabQualifier, , _
                 transformation.conEnumLabelText.lrtOidRef)
           Else
             transformAttrName = _
               mapExpression( _
                 g_attributes.descriptors(attrIndex).virtuallyMapsTo.mapTo, _
                 transformation.conEnumLabelText.orgIndex, _
                 transformation.conEnumLabelText.poolIndex, _
                 ddlType, _
                 transformation.conEnumLabelText.tabQualifier, , _
                 transformation.conEnumLabelText.lrtOidRef)
           End If
           transformToConstant = True
           Exit Function
         End If
       End If
     End If
 
     If ((outputMode And edomXml) <> 0) And ((outputMode And edomExpressionRef) = 0) And (attrCat And eacFkOidExpression) Then
       Exit Function
     End If

     If (outputMode And (edomValueExpression Or edomXml)) And attrIndex > 0 Then
         If g_attributes.descriptors(attrIndex).isExpression Then
           If (outputMode And edomExpression) = 0 Then
             If (outputMode And (Not edomDecl) And edomExpressionDummy) = 0 Then
               ' we accept edomExpressionDummy only in 'List' or 'Value'-mode
               Exit Function
             End If
           End If

           If (outputMode And edomList) And ((outputMode And edomValue) = 0) Then
               transformAttrName = transformation.attributePrefix & db2AttrName & transformation.attributePostfix
             Exit Function
           End If
           Dim qualTabNameExpression As String
           Dim qualTabNameExpressionLrt As String
           If attrCat And eacNationalBool Then
             Dim attrNameBase As String
             attrNameBase = genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP") & "_ISNATACTIVE"
               transformAttrName = _
                 transformation.attributePrefix & IIf(outputMode And edomXml, db2AttrName, attrNameBase) & _
                 IIf(transformation.attributeRepeatDelimiter <> "", transformation.attributeRepeatDelimiter & attrNameBase, "") & _
                 transformation.attributePostfix
           Else
             If outputMode And edomExpressionDummy Then
               name = "CAST(NULL AS VARCHAR(1))"
             ElseIf outputMode And edomExpressionRef Then
               name = genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP")
             Else
               Dim maxStrLength As Integer
               Dim castToDataType As String
               maxStrLength = 0
               If effectiveDomainIndex > 0 Then
                   If g_domains.descriptors(effectiveDomainIndex).dataType = etChar Or g_domains.descriptors(effectiveDomainIndex).dataType = etVarchar Then
                     maxStrLength = g_domains.descriptors(effectiveDomainIndex).maxLength
                     castToDataType = getDataType(g_domains.descriptors(effectiveDomainIndex).dataType, g_domains.descriptors(effectiveDomainIndex).maxLength, , g_domains.descriptors(effectiveDomainIndex).supportUnicode)
                   End If
               End If

                 qualTabNameExpression = genQualTabNameByClassIndex(g_classIndexExpression, ddlType, transformation.conEnumLabelText.orgIndex, transformation.conEnumLabelText.poolIndex)
                 If transformation.conEnumLabelText.forLrt Then
                   qualTabNameExpressionLrt = genQualTabNameByClassIndex(g_classIndexExpression, ddlType, transformation.conEnumLabelText.orgIndex, transformation.conEnumLabelText.poolIndex, , True, True)
                 End If

               If transformation.conEnumLabelText.forLrt Then
                 If g_classes.descriptors(g_classIndexExpression).useMqtToImplementLrt Then
                   If maxStrLength > 0 Then
                     name = _
                       "(SELECT CAST(RTRIM(LEFT(X.TERMSTRING," & maxStrLength & ")) AS " & castToDataType & ") FROM " & qualTabNameExpressionLrt & " X WHERE X." & g_anOid & " = " & _
                       IIf(transformation.conEnumLabelText.tabQualifier = "", "", transformation.conEnumLabelText.tabQualifier & ".") & _
                       genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP", , , , (attrCat And eacNational) <> 0) & " FETCH FIRST 1 ROW ONLY)" & _
                       IIf(outputMode And edomColumnName, " AS " & db2AttrName, "")
                   Else
                     name = _
                       "(SELECT X.TERMSTRING FROM " & qualTabNameExpressionLrt & " X WHERE X." & g_anOid & " = " & _
                       IIf(transformation.conEnumLabelText.tabQualifier = "", "", transformation.conEnumLabelText.tabQualifier & ".") & _
                       genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP", , , , (attrCat And eacNational) <> 0) & " FETCH FIRST 1 ROW ONLY)" & _
                       IIf(outputMode And edomColumnName, " AS " & db2AttrName, "")
                   End If
                 Else
                   If maxStrLength > 0 Then
                     name = _
                       "CAST(RTRIM(LEFT(COALESCE(" & _
                         "(SELECT X.TERMSTRING FROM " & qualTabNameExpressionLrt & " X WHERE X." & g_anOid & " = " & _
                         IIf(transformation.conEnumLabelText.tabQualifier = "", "", transformation.conEnumLabelText.tabQualifier & ".") & _
                         genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP", , , , (attrCat And eacNational) <> 0) & ")" & _
                       "," & _
                         "(SELECT X.TERMSTRING FROM " & qualTabNameExpression & " X WHERE X." & g_anOid & " = " & _
                         IIf(transformation.conEnumLabelText.tabQualifier = "", "", transformation.conEnumLabelText.tabQualifier & ".") & _
                         genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP", , , , (attrCat And eacNational) <> 0) & ")" & _
                       ")," & maxStrLength & ")) AS VARCHAR(" & maxStrLength & "))" & _
                       IIf(outputMode And edomColumnName, " AS " & db2AttrName, "")
                   Else
                     name = _
                       "COALESCE(" & _
                         "(SELECT X.TERMSTRING FROM " & qualTabNameExpressionLrt & " X WHERE X." & g_anOid & " = " & _
                         IIf(transformation.conEnumLabelText.tabQualifier = "", "", transformation.conEnumLabelText.tabQualifier & ".") & _
                         genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP", , , , (attrCat And eacNational) <> 0) & ")" & _
                       "," & _
                         "(SELECT X.TERMSTRING FROM " & qualTabNameExpression & " X WHERE X." & g_anOid & " = " & _
                         IIf(transformation.conEnumLabelText.tabQualifier = "", "", transformation.conEnumLabelText.tabQualifier & ".") & _
                         genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP", , , , (attrCat And eacNational) <> 0) & ")" & _
                       ")" & _
                       IIf(outputMode And edomColumnName, " AS " & db2AttrName, "")
                   End If
                 End If
               Else
                 If maxStrLength > 0 Then
                   name = _
                     "(SELECT CAST(RTRIM(LEFT(X.TERMSTRING," & maxStrLength & ")) AS " & castToDataType & ") FROM " & qualTabNameExpression & " X WHERE X." & g_anOid & " = " & _
                     IIf(transformation.conEnumLabelText.tabQualifier = "", "", transformation.conEnumLabelText.tabQualifier & ".") & _
                     genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP", , , , (attrCat And eacNational) <> 0) & ")" & _
                     IIf(outputMode And edomColumnName, " AS " & db2AttrName, "")
                 Else
                   name = _
                     "(SELECT X.TERMSTRING FROM " & qualTabNameExpression & " X WHERE X." & g_anOid & " = " & _
                     IIf(transformation.conEnumLabelText.tabQualifier = "", "", transformation.conEnumLabelText.tabQualifier & ".") & _
                     genSurrogateKeyName(ddlType, g_attributes.descriptors(attrIndex).shortName & "EXP", , , , (attrCat And eacNational) <> 0) & ")" & _
                     IIf(outputMode And edomColumnName, " AS " & db2AttrName, "")
                 End If
               End If

               If (outputMode And edomValue) <> 0 And (outputMode And edomXml) = 0 Then
                 transformAttrName = name
                 Exit Function
               End If
             End If

               If outputMode And edomList Then
                 transformAttrName = transformation.attributePrefix & name & transformation.attributePostfix
               ElseIf outputMode And edomXsd Then
                 transformAttrName = name
               ElseIf outputMode And edomXml Then
                 transformAttrName = "XMLELEMENT (NAME """ & db2AttrName & """, " & name & ")"
               Else
                 transformAttrName = name
               End If
           End If
           Exit Function
         End If
     ElseIf ((outputMode And edomValueVirtual) <> 0) And ((outputMode And edomExpressionDummy) <> 0) And attrIndex > 0 Then
         If g_attributes.descriptors(attrIndex).isExpression Then
           If attrCat And eacNationalBool Then
             transformAttrName = "CAST(NULL AS " & g_dbtBoolean & ")"
             Exit Function
           Else
             transformAttrName = "CAST(NULL AS VARCHAR(1))"
             Exit Function
           End If
         End If
     End If

 ' ### ENDIF IVK ###
     If name = "" Then
       name = db2AttrName
     End If

     transformAttrName = transformation.attributePrefix & name & IIf(transformation.attributeRepeatDelimiter <> "", transformation.attributeRepeatDelimiter & name, "") & transformation.attributePostfix
 End Function
 
 
 ' ### IF IVK ###
 Private Function genTransformedAttrDeclWithColReUse( _
   ByRef attrName As String, ByRef attrNameShort As String, _
   ByVal valueType As AttrValueType, _
   ByVal valueTypeIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional acmEntityType As AcmAttrContainerType = eactClass, _
   Optional acmEntityIndex As Integer = -1, _
   Optional ByRef specifics As String = "", _
   Optional ByVal addComma As Boolean = True, _
   Optional ByVal ddlType As DdlTypeId = edtLdm, _
   Optional ByRef infix As String = "", _
   Optional ByVal outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional ByVal attrCat As AttrCategory = eacRegular, _
   Optional ByRef fkRelIndex As Integer = -1, _
   Optional ByVal indent As Integer = 1, _
   Optional ByRef attrIsReUsed As Boolean = False, _
   Optional ByRef comment As String = "", _
   Optional ByRef default As String = "", _
   Optional ByVal useAlternativeDefaults As Boolean = False, _
   Optional ByVal isVirtual As Boolean = False, _
   Optional ByVal isOptional As Boolean = False, _
   Optional attrIndex As Integer = -1, _
   Optional ByVal persisted As Boolean = True _
 ) As String
 ' ### ELSE IVK ###
 'Private Function genTransformedAttrDeclWithColReUse( _
 ' ByRef attrName As String, ByRef attrNameShort As String, _
 ' valueType As AttrValueType, _
 ' valueTypeIndex As Integer, _
 ' ByRef transformation As AttributeListTransformation, _
 ' ByRef tabColumns As EntityColumnDescriptors, _
 ' Optional acmEntityType As AcmAttrContainerType = eactClass, _
 ' Optional acmEntityIndex As Integer = -1, _
 ' Optional ByRef specifics As String = "", _
 ' Optional ByVal addComma As Boolean = True, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional byref infix As String = "", _
 ' Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
 ' Optional attrCat As AttrCategory = eacRegular, _
 ' Optional ByRef fkRelIndex As Integer = -1, _
 ' Optional indent As Integer = 1, _
 ' Optional ByRef attrIsReUsed As Boolean = False, _
 ' Optional ByRef comment As String = "", _
 ' Optional byref default As String = "", _
 ' Optional useAlternativeDefaults As Boolean = False, _
 ' Optional ByVal isOptional As Boolean = False, _
 ' Optional attrIndex As Integer = -1 _
 ') As String
 ' ### ENDIF IVK ###
   On Error GoTo ErrorExit

   Dim entityName As String
   Dim entityNameShort As String
   Dim effectiveDomainIndex As Integer
 ' ### IF IVK ###
   Dim attrSupportXmlExport As Boolean
   attrSupportXmlExport = True

   Dim isExpression As Boolean
   isExpression = False
 
 ' ### ENDIF IVK ###
   Dim i As Integer

   If valueType = eavtDomain Then
     effectiveDomainIndex = valueTypeIndex
   ElseIf valueType = eavtEnum Then
     effectiveDomainIndex = g_enums.descriptors(valueTypeIndex).domainIndexId
   ElseIf valueType = eavtDomainEnumId Then
     effectiveDomainIndex = g_enums.descriptors(valueTypeIndex).domainIndexId
   ElseIf valueType = eavtDomainEnumValue Then
     effectiveDomainIndex = g_enums.descriptors(valueTypeIndex).domainIndexValue
   End If

   If acmEntityIndex > 0 Then
     If acmEntityType = eactClass Then
 ' ### IF IVK ###
         If g_classes.descriptors(acmEntityIndex).noXmlExport Then
           attrSupportXmlExport = False
         End If
 ' ### ENDIF IVK ###
         entityName = g_classes.descriptors(acmEntityIndex).className
         entityNameShort = g_classes.descriptors(acmEntityIndex).shortName
     ElseIf acmEntityType = eactRelationship Then
 ' ### IF IVK ###
         If g_relationships.descriptors(acmEntityIndex).noXmlExport Then
           attrSupportXmlExport = False
         End If
 ' ### ENDIF IVK ###
         entityName = g_relationships.descriptors(acmEntityIndex).relName
         entityNameShort = g_relationships.descriptors(acmEntityIndex).shortName
     ElseIf acmEntityType = eactEnum Then
 ' ### IF IVK ###
         If g_enums.descriptors(acmEntityIndex).noXmlExport Then
           attrSupportXmlExport = False
         End If
 ' ### ENDIF IVK ###
         entityName = g_enums.descriptors(acmEntityIndex).enumName
         entityNameShort = g_enums.descriptors(acmEntityIndex).shortName
     End If
   End If
 
   genTransformedAttrDeclWithColReUse = ""

 ' ### IF IVK ###
   ' FIXME: work-around as long as we do not fully support virtual attributes
   ' any virtual attribute implicitly is nullable
   isOptional = isOptional Or (isVirtual And g_domains.descriptors(effectiveDomainIndex).dataType <> etBoolean)

   If Not supportVirtualColumns Then
     isVirtual = False
   End If

   If isVirtual And Not xmlExportVirtualColumns Then
     Exit Function
   End If

   If (outputMode And (edomXsd Or edomXml)) Then
     If Not attrSupportXmlExport Then
       Exit Function
     End If

     If UCase(attrName) = UCase(conInLrt) Then
       If Not xmlExportColumnInLrt Then
         Exit Function
       End If
     End If
     If UCase(attrName) = UCase(conClassId) Then
       If Not xmlExportColumnClassId Then
         Exit Function
       End If
     End If
     If UCase(attrName) = UCase(conVersionId) Then
       If Not xmlExportColumnVersionId Then
         Exit Function
       End If
     End If
   End If
 
 ' ### ENDIF IVK ###
   addComma = addComma And Not transformation.suppressAllComma

     Dim effectiveMaxLength As String
     If g_domains.descriptors(effectiveDomainIndex).maxLength = "" Then
       effectiveMaxLength = ""
     Else
       If supportUnicode And g_domains.descriptors(effectiveDomainIndex).supportUnicode Then
         effectiveMaxLength = CInt(g_domains.descriptors(effectiveDomainIndex).unicodeExpansionFactor * CInt(g_domains.descriptors(effectiveDomainIndex).maxLength)) & ""
       Else
         effectiveMaxLength = g_domains.descriptors(effectiveDomainIndex).maxLength
       End If
     End If

     Dim db2AttrName As String
     If reuseColumnsInTabsForOrMapping Then
       db2AttrName = genAttrName(attrName, ddlType, , infix)
     Else
       db2AttrName = genAttrName(attrName, ddlType, entityNameShort, infix)
     End If

 ' ### IF IVK ###
     Dim isVirtuallyReferredTo As Boolean
     isVirtuallyReferredTo = False
     Dim isVirtualInstantiated As Boolean
     isVirtualInstantiated = True
     If attrIndex > 0 Then
         isVirtual = g_attributes.descriptors(attrIndex).isVirtual And Not g_attributes.descriptors(attrIndex).virtuallyMapsTo.isRelBasedMapping
         isExpression = g_attributes.descriptors(attrIndex).isExpression

         If isVirtual And Not g_attributes.descriptors(attrIndex).virtuallyMapsTo.isInstantiated And ((outputMode And (edomMqtLrt Or edomXref)) = 0) Then
           genTransformedAttrDeclWithColReUse = printComment("virtual column / not instantiated", -1, outputMode)
           Exit Function
         End If

         If g_attributes.descriptors(attrIndex).noXmlExport And (outputMode And (edomXml Or edomXsd)) <> 0 Then
           Exit Function
         End If
         isVirtualInstantiated = g_attributes.descriptors(attrIndex).virtuallyMapsTo.isInstantiated
       isVirtuallyReferredTo = UBound(g_attributes.descriptors(attrIndex).virtuallyReferredToBy) > 0
     End If
 
 ' ### ENDIF IVK ###
     Dim transformedAttrName As String
     Dim transformToConstant As Boolean
 ' ### IF IVK ###
     transformedAttrName = _
       transformAttrName(db2AttrName, valueType, valueTypeIndex, transformation, ddlType, , infix, transformToConstant, isVirtual, _
         attrIndex, outputMode, isOptional, persisted, (outputMode And edomValue) <> 0, attrCat)
 ' ### ELSE IVK ###
 '   transformedAttrName = _
 '     transformAttrName(db2AttrName, valueType, valueTypeIndex, transformation, ddlType, , infix, transformToConstant, _
 '       attrIndex, outputMode, isOptional, (outputMode And edomValue) <> 0, attrCat)
 ' ### ENDIF IVK ###
 
     If transformedAttrName = "" Then
       Exit Function
     End If

     If transformation.doCollectOidColDescriptors And (attrCat And transformation.oidColFilter) Then
       addOidColDescriptor(transformation.oidDescriptors, db2AttrName, attrCat)
     End If

     Dim colIndex As Integer
 ' ### IF IVK ###
     If isVirtual And Not (outputMode And edomList) And Not transformation.doCollectVirtualAttrDescriptors Then
       colIndex = _
         findColumnToUse(tabColumns, db2AttrName, entityName, acmEntityType, attrName, valueType, valueTypeIndex, attrIsReUsed, _
           attrCat, fkRelIndex, True, attrIndex, isOptional, isVirtualInstantiated)
     Else
       colIndex = _
         findColumnToUse(tabColumns, db2AttrName, entityName, acmEntityType, attrName, valueType, valueTypeIndex, attrIsReUsed, _
           attrCat, fkRelIndex, , attrIndex, isOptional, isVirtualInstantiated)
     End If
 ' ### ELSE IVK ###
 '   colIndex = findColumnToUse(tabColumns, db2AttrName, entityName, acmEntityType, attrName, effectiveDomainIndex, attrIsReUsed, attrCat, fkRelIndex, , attrIndex, isOptional)
 ' ### ENDIF IVK ###

     If attrIsReUsed And colIndex > 0 Then
         tabColumns.descriptors(colIndex).columnCategory = tabColumns.descriptors(colIndex).columnCategory Or attrCat
     End If

 ' ### IF IVK ###
     If colIndex > 0 Then
         If isVirtual Then
           tabColumns.descriptors(colIndex).columnCategory = tabColumns.descriptors(colIndex).columnCategory Or eacVirtual
         End If
         If isExpression Then
           tabColumns.descriptors(colIndex).columnCategory = tabColumns.descriptors(colIndex).columnCategory Or eacExpression
         End If

         If attrIndex > 0 Then
           If g_attributes.descriptors(attrIndex).groupIdBasedOn <> "" Then
             tabColumns.descriptors(colIndex).columnCategory = tabColumns.descriptors(colIndex).columnCategory Or eacGroupId
           End If
         End If
     End If
 
 ' ### ENDIF IVK ###
 ' ### IF IVK ###
     If (outputMode And (Not edomXref)) = edomNone Then
 ' ### ELSE IVK ###
 '   If outputMode = edomNone Then
 ' ### ENDIF IVK ###
       Exit Function
     End If

 ' ### IF IVK ###
     If outputMode And edomMapHibernate Then
       Dim javaMaxTypeLength As String
       Dim javaDataType As String
       javaMaxTypeLength = getJavaMaxTypeLength(g_domains.descriptors(effectiveDomainIndex).dataType, g_domains.descriptors(effectiveDomainIndex).maxLength)
       javaDataType = getJavaDataType(g_domains.descriptors(effectiveDomainIndex).dataType)

       Dim attrSpecs As String
       attrSpecs = ""

       If attrCat And eacAnyOid Then
         genTransformedAttrDeclWithColReUse = _
           addTab(indent) & "<id name=""" & attrName & """ type=""" & javaDataType & """>" & vbCrLf & _
           addTab(indent + 1) & "<column name=""" & transformedAttrName & """/>" & vbCrLf & _
           addTab(indent + 1) & "<generator class=""sequence"" >" & vbCrLf & _
           addTab(indent + 2) & "<param name=""sequence"">" & UCase(snMeta) & "." & UCase(gc_seqNameOid) & "</param>" & vbCrLf & _
           addTab(indent + 1) & "</generator>" & vbCrLf & _
           addTab(indent) & "</id>" & vbCrLf
       ElseIf attrCat And eacCid Then
         genTransformedAttrDeclWithColReUse = _
           addTab(indent) & "<discriminator  column=""" & transformedAttrName & """ type=""" & javaDataType & """" & _
           IIf(javaMaxTypeLength <> "", """ length=""" & javaMaxTypeLength & """", "") & "/>" & vbCrLf
       ElseIf attrCat And eacVid Then
         genTransformedAttrDeclWithColReUse = _
           addTab(indent) & "<version name=""" & conVersionId & """ type=""" & javaDataType & """>" & vbCrLf & _
           addTab(indent + 1) & "<column name=""" & transformedAttrName & """/>" & vbCrLf & _
           addTab(indent) & "</version>" & vbCrLf
       Else
         genTransformedAttrDeclWithColReUse = _
           addTab(indent) & "<property name=""" & attrName & """ type=""" & javaDataType & """>" & vbCrLf & _
           addTab(indent + 1) & "<column name=""" & transformedAttrName & """" & _
           IIf(javaMaxTypeLength <> "", """ length=""" & javaMaxTypeLength & """", "") & "/>" & vbCrLf & _
           addTab(indent) & "</property>" & vbCrLf
       End If
       Exit Function
     ElseIf outputMode And edomXsd Then
       If (attrCat And eacFkOidExpression) Then
         Exit Function
       End If
       If ((outputMode And edomVirtual) Or Not isVirtual) And (Not reuseColumnsInTabsForOrMapping Or Not attrIsReUsed) Then
         If transformedAttrName <> "" Then
           genTransformedAttrDeclWithColReUse = _
             addTab(2) & "'" & addTab(indent + 3) & "<element name=""" & transformedAttrName & """ type=""standardxml:" & _
             g_domains.descriptors(effectiveDomainIndex).sectionName & "_" & g_domains.descriptors(effectiveDomainIndex).domainName & IIf(isOptional, "_N", "") & _
             """/>' || cr ||"
         End If
       End If
       Exit Function
     End If

 ' ### ENDIF IVK ###
     If ((outputMode And edomList) <> 0) And ((outputMode And edomComment) = 0) Then
 ' ### IF IVK ###
       If ((outputMode And edomVirtual) Or Not isVirtual) And (Not reuseColumnsInTabsForOrMapping Or Not attrIsReUsed) Then
 ' ### ENDIF IVK ###
         If (outputMode And edomDefaultValue) And default <> "" Then
           If default <> transformedAttrName And Not transformToConstant Then
             transformedAttrName = "COALESCE(" & transformedAttrName & ", " & default & ")"
           End If
         End If
         genTransformedAttrDeclWithColReUse = IIf(transformedAttrName = "", "", addTab(indent) & transformedAttrName & IIf(addComma, ",", ""))
 ' ### IF IVK ###
       End If
 ' ### ENDIF IVK ###
     ElseIf ((outputMode And edomValue) <> 0) And ((outputMode And edomComment) = 0) Then
 ' ### IF IVK ###
       If Not ((outputMode And edomVirtual) Or Not isVirtual) Or Not attrIsReUsed Then
 ' ### ELSE IVK ###
 '     If Not attrIsReUsed Then
 ' ### ENDIF IVK ###
         Dim transformedAttrValue As String

         transformedAttrValue = transformedAttrName
 ' ### IF IVK ###
         If isVirtuallyReferredTo Then
           Dim thisClassIndex As Integer
           Dim referringClassIndex As Integer
           Dim fkAttrName As String
             thisClassIndex = g_attributes.descriptors(attrIndex).acmEntityIndex
             For i = 1 To UBound(g_attributes.descriptors(attrIndex).virtuallyReferredToBy)
                 referringClassIndex = g_attributes.descriptors(g_attributes.descriptors(attrIndex).virtuallyReferredToBy(i)).acmEntityIndex
                 fkAttrName = _
                   genSurrogateKeyName( _
                     ddlType, , g_relationships.descriptors(g_attributes.descriptors(g_attributes.descriptors(attrIndex).virtuallyReferredToBy(i)).virtuallyMapsTo.relIndex).shortName & _
                     IIf(g_attributes.descriptors(g_attributes.descriptors(attrIndex).virtuallyReferredToBy(i)).virtuallyMapsTo.navDirection = etLeft, _
                     g_relationships.descriptors(g_attributes.descriptors(g_attributes.descriptors(attrIndex).virtuallyReferredToBy(i)).virtuallyMapsTo.relIndex).rlShortRelName, _
                     g_relationships.descriptors(g_attributes.descriptors(g_attributes.descriptors(attrIndex).virtuallyReferredToBy(i)).virtuallyMapsTo.relIndex).lrShortRelName))
 '                .virtuallyMapsTo.relIndex
 '                classIndex = g_attributes.descriptors(.virtuallyReferredToBy(i)).acmEntityIndex
             Next i
         ElseIf transformedAttrValue = (transformation.attributePrefix & db2AttrName & transformation.attributePostfix) Then
 ' ### ELSE IVK ###
 '       If transformedAttrValue = (transformation.attributePrefix & db2AttrName & transformation.attributePostfix) Then
 ' ### ENDIF IVK ###
           ' todo: use a more transparent way to figure out if attribute value effectively was transformed
           If (outputMode And edomDefaultValue) And default <> "" Then
             transformedAttrValue = default
           Else
             transformedAttrValue = "CAST(NULL AS " & getDataType(g_domains.descriptors(effectiveDomainIndex).dataType, effectiveMaxLength, g_domains.descriptors(effectiveDomainIndex).scale) & ")"
           End If
         End If
         genTransformedAttrDeclWithColReUse = addTab(indent) & transformedAttrValue & IIf(addComma, ",", "")
       End If
     ElseIf outputMode And edomComment Then
       If comment <> "" Then
         genTransformedAttrDeclWithColReUse = _
           addTab(indent) & paddRight(db2AttrName, gc_dbMaxAttributeNameLength) & " IS '" & Replace(comment, "'", "''", vbTextCompare) & "'" & IIf(addComma, ",", "")
       Else
 Debug.Print "empty comment / "; db2AttrName
       End If
     Else
 ' ### IF IVK ###
       If ((outputMode And edomVirtual) Or Not isVirtual) And (outputMode And edomNoSpecifics) = edomNoSpecifics Then
 ' ### ELSE IVK ###
 '     If (outputMode And edomNoSpecifics) = edomNoSpecifics Then
 ' ### ENDIF IVK ###
         If Not reuseColumnsInTabsForOrMapping Or Not attrIsReUsed Then
           If addComma Then
             genTransformedAttrDeclWithColReUse = _
               addTab(indent) & paddRight(db2AttrName, gc_dbMaxAttributeNameLength) & " " & _
               paddRight(getDataType(g_domains.descriptors(effectiveDomainIndex).dataType, effectiveMaxLength, g_domains.descriptors(effectiveDomainIndex).scale)) & " " & IIf(addComma, ",", "")
           Else
             genTransformedAttrDeclWithColReUse = _
               addTab(indent) & paddRight(db2AttrName, gc_dbMaxAttributeNameLength) & " " & getDataType(g_domains.descriptors(effectiveDomainIndex).dataType, effectiveMaxLength, g_domains.descriptors(effectiveDomainIndex).scale)
           End If
         End If
       Else
 ' ### IF IVK ###
         If isVirtual And attrIndex > 0 And Not ((outputMode And edomDeclVirtual) = edomDeclVirtual) Then
             If g_attributes.descriptors(attrIndex).virtuallyMapsTo.isRelBasedMapping Then
               genTransformedAttrDeclWithColReUse = _
                 printComment("virtually map to """ & g_attributes.descriptors(attrIndex).virtuallyMapsTo.mapTo & g_classes.descriptors(g_attributes.descriptors(attrIndex).virtuallyMapsTo.targetClassIndex).className & """", -1, outputMode)
             Else
               genTransformedAttrDeclWithColReUse = _
                 printComment("virtually map to """ & mapExpression(g_attributes.descriptors(attrIndex).virtuallyMapsTo.mapTo, transformation.conEnumLabelText.orgIndex, transformation.conEnumLabelText.poolIndex, ddlType, _
                 transformation.conEnumLabelText.tabQualifier, , transformation.conEnumLabelText.lrtOidRef) & _
                 """", -1, outputMode) & _
                 IIf((reuseColumnsInTabsForOrMapping And attrIsReUsed) Or Not g_attributes.descriptors(attrIndex).isPersistent, "", _
                 vbCrLf & addTab(indent) & paddRight(db2AttrName, gc_dbMaxAttributeNameLength) & " " & _
                 paddRight(getDataTypeByDomainIndex(effectiveDomainIndex)) & " " & IIf(addComma, ",", ""))
             End If
         ElseIf reuseColumnsInTabsForOrMapping And attrIsReUsed Then
 ' ### ELSE IVK ###
 '       If reuseColumnsInTabsForOrMapping And attrIsReUsed Then
 ' ### ENDIF IVK ###
             genTransformedAttrDeclWithColReUse = _
               printComment("reuse attribute """ & tabColumns.descriptors(colIndex).acmAttributeName & IIf(tabColumns.descriptors(colIndex).acmEntityName <> "", "@" & tabColumns.descriptors(colIndex).acmEntityName, "") & """", -1, outputMode)
         Else
           Dim constraint As String
           Dim constraintName As String
           Dim numConditions As Integer
           numConditions = 0
           If acmEntityIndex > 0 Then
             constraintName = "CHK_" & IIf(attrNameShort = "", UCase(Left(attrName, 14)), UCase(attrNameShort))
           End If
           constraint = ""

           If g_domains.descriptors(effectiveDomainIndex).minLength <> "" Then
             constraint = constraint & "(LENGTH(" & db2AttrName & ") >= " & g_domains.descriptors(effectiveDomainIndex).minLength & ")"
             numConditions = numConditions + 1
           End If

           If g_domains.descriptors(effectiveDomainIndex).minValue <> "" Then
             constraint = constraint & IIf(constraint = "", "", " AND ") & "(" & db2AttrName & " >= " & g_domains.descriptors(effectiveDomainIndex).minValue & ")"
             numConditions = numConditions + 1
           End If

           If g_domains.descriptors(effectiveDomainIndex).maxValue <> "" Then
             constraint = constraint & IIf(constraint = "", "", " AND ") & "(" & db2AttrName & " <= " & g_domains.descriptors(effectiveDomainIndex).maxValue & ")"
             numConditions = numConditions + 1
           End If

           If g_domains.descriptors(effectiveDomainIndex).constraint <> "" Then
             constraint = constraint & IIf(constraint = "", "", " AND ") & "(" & Replace(g_domains.descriptors(effectiveDomainIndex).constraint, "<value>", db2AttrName) & ")"
             numConditions = numConditions + 1
           End If

           If g_domains.descriptors(effectiveDomainIndex).valueList <> "" Then
             constraint = constraint & IIf(constraint = "", "", " AND ") & "(" & db2AttrName & " IN (" & g_domains.descriptors(effectiveDomainIndex).valueList & "))"
             numConditions = numConditions + 1
           End If

           If constraint <> "" Then
             constraint = _
               IIf(specifics = "", "", " ") & IIf(constraintName <> "", "CONSTRAINT " & constraintName & " ", "") & _
               "CHECK" & IIf(numConditions > 1, "(", "") & constraint & IIf(numConditions > 1, ")", "")
           End If

           If g_domains.descriptors(effectiveDomainIndex).notLogged Then
             constraint = constraint & IIf(Right(" " & constraint, 1) = " ", "", " ") & "NOT LOGGED"
           End If

           If g_domains.descriptors(effectiveDomainIndex).notCompact Then
             constraint = constraint & IIf(Right(" " & constraint, 1) = " ", "", " ") & "NOT COMPACT"
           End If

           addAttrToDdlSummary(db2AttrName, getDataType(g_domains.descriptors(effectiveDomainIndex).dataType), effectiveMaxLength, specifics, ddlType)

           If transformation.trimRight Then
             genTransformedAttrDeclWithColReUse = _
               addTab(indent) & RTrim(paddRight(db2AttrName, gc_dbMaxAttributeNameLength) & " " & _
               paddRight(getDataType(g_domains.descriptors(effectiveDomainIndex).dataType, effectiveMaxLength, g_domains.descriptors(effectiveDomainIndex).scale)) & " " & _
               specifics & IIf(transformation.ignoreConstraint, "", constraint) & IIf(addComma, ",", ""))
           Else
             genTransformedAttrDeclWithColReUse = _
               addTab(indent) & paddRight(db2AttrName, gc_dbMaxAttributeNameLength) & " " & _
               paddRight(getDataType(g_domains.descriptors(effectiveDomainIndex).dataType, effectiveMaxLength, g_domains.descriptors(effectiveDomainIndex).scale)) & " " & _
               specifics & IIf(transformation.ignoreConstraint, "", constraint) & IIf(addComma, ",", "")
           End If
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
 
 
 ' ### IF IVK ###
 Private Function genTransformedAttrDecl( _
   ByRef attrName As String, _
   ByRef attrNameShort As String, _
   valueType As AttrValueType, _
   valueTypeIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   Optional acmEntityType As AcmAttrContainerType = eactClass, _
   Optional acmEntityIndex As Integer = -1, _
   Optional ByRef specifics As String = "", _
   Optional addComma As Boolean = True, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef infix As String = "", _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional attrCat As AttrCategory = eacRegular, _
   Optional ByRef fkRelIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional ByVal isVirtual As Boolean = False, _
   Optional ByVal isOptional As Boolean = False, _
   Optional attrIndex As Integer = -1, _
   Optional ByRef comment As String = "" _
 ) As String
   On Error GoTo ErrorExit

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   genTransformedAttrDecl = _
     genTransformedAttrDeclWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, _
       specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, , comment, , , isVirtual, isOptional, attrIndex)

 NormalExit:
   On Error Resume Next
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 ' ### ELSE IVK ###
 'Private Function genTransformedAttrDecl( _
 ' ByRef attrName As String, _
 ' ByRef attrNameShort As String, _
 ' valueType As AttrValueType, _
 ' valueTypeIndex As Integer, _
 ' ByRef transformation As AttributeListTransformation, _
 ' Optional acmEntityType As AcmAttrContainerType = eactClass, _
 ' Optional acmEntityIndex As Integer = -1, _
 ' Optional ByRef specifics As String = "", _
 ' Optional addComma As Boolean = True, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional byref infix As String = "", _
 ' Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
 ' Optional attrCat As AttrCategory = eacRegular, _
 ' Optional ByRef fkRelIndex As Integer = -1, _
 ' Optional indent As Integer = 1, _
 ' Optional ByVal isOptional As Boolean = False, _
 ' Optional attrIndex As Integer = -1, _
 ' Optional ByRef comment As String = "" _
 ') As String
 ' On Error Goto ErrorExit
 '
 ' Dim tabColumns As EntityColumnDescriptors
 ' tabColumns = nullEntityColumnDescriptors
 '
 ' genTransformedAttrDecl = _
 '   genTransformedAttrDeclWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, transformation, tabColumns, acmEntityType, acmEntityIndex, _
 '      specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, , comment, , , isOptional, attrIndex)
 '
 'NormalExit:
 ' On Error Resume Next
 ' Exit Function
 '
 'ErrorExit:
 ' errMsgBox Err.description
 ' Resume NormalExit
 'End Function
 ' ### ENDIF IVK ###
 
 
 ' ### IF IVK ###
 Function genAttrDecl( _
   ByRef attrName As String, _
   ByRef attrNameShort As String, _
   valueType As AttrValueType, _
   valueTypeIndex As Integer, _
   Optional acmEntityType As AcmAttrContainerType = eactClass, _
   Optional acmEntityIndex As Integer = -1, _
   Optional ByRef specifics As String = "", _
   Optional addComma As Boolean = True, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef infix As String = "", _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional attrCat As AttrCategory = eacRegular, _
   Optional ByRef fkRelIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional ByVal isVirtual As Boolean = False, _
   Optional ByVal isOptional As Boolean = False, _
   Optional attrIndex As Integer = -1, _
   Optional ByRef comment As String = "" _
 ) As String
   On Error GoTo ErrorExit

   genAttrDecl = _
     genTransformedAttrDecl(attrName, attrNameShort, valueType, valueTypeIndex, nullAttributeTransformation, acmEntityType, acmEntityIndex, _
       specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, isVirtual, isOptional, attrIndex, comment)
 
 NormalExit:
   On Error Resume Next
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 ' ### ELSE IVK ###
 'Function genAttrDecl( _
 ' ByRef attrName As String, _
 ' ByRef attrNameShort As String, _
 ' valueType As AttrValueType, _
 ' valueTypeIndex As Integer, _
 ' Optional acmEntityType As AcmAttrContainerType = eactClass, _
 ' Optional acmEntityIndex As Integer = -1, _
 ' Optional ByRef specifics As String = "", _
 ' Optional addComma As Boolean = True, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional byref infix As String = "", _
 ' Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
 ' Optional attrCat As AttrCategory = eacRegular, _
 ' Optional ByRef fkRelIndex As Integer = -1, _
 ' Optional indent As Integer = 1, _
 ' Optional ByVal isOptional As Boolean = False, _
 ' Optional attrIndex As Integer = -1, _
 ' Optional ByRef comment As String = "" _
 ') As String
 ' On Error Goto ErrorExit
 '
 ' genAttrDecl = _
 '   genTransformedAttrDecl(attrName, attrNameShort, valueType, valueTypeIndex, nullAttributeTransformation, acmEntityType, acmEntityIndex, _
 '     specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, indent, isOptional, attrIndex, comment)
 '
 'NormalExit:
 ' On Error Resume Next
 ' Exit Function
 '
 'ErrorExit:
 ' errMsgBox Err.description
 ' Resume NormalExit
 'End Function
 ' ### ENDIF IVK ###
 
 
 ' ### IF IVK ###
 Function genSurrogateKeyName( _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef classNameShort As String = "", _
   Optional ByRef infix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional ByVal valueType As AttrValueType, _
   Optional ByVal isNational As Boolean = False _
 ) As String
   genSurrogateKeyName = genAttrName(cosnOid, ddlType, classNameShort, infix, suffix, valueType, isNational)
 End Function
 ' ### ELSE IVK ###
 'Function genSurrogateKeyName( _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional ByRef classNameShort As String = "", _
 ' Optional ByRef infix As String = "", _
 ' Optional ByRef suffix As String = "", _
 ' Optional ByVal valueType As AttrValueType _
 ') As String
 ' genSurrogateKeyName = genAttrName(cosnOid, ddlType, classNameShort, infix, suffix, valueType)
 'End Function
 ' ### ENDIF IVK ###
 
 
 Function genSurrogateKeyShortName( _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef classNameShort As String = "", _
   Optional ByRef infix As String = "" _
 ) As String
   genSurrogateKeyShortName = genAttrName(cosnOid, ddlType, classNameShort, infix)
 End Function
 
 
 ' ### IF IVK ###
 Function genTransformedAttrDeclByDomainWithColReUse( _
   ByRef attrName As String, _
   ByRef attrNameShort As String, _
   valueType As AttrValueType, _
   valueTypeIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   ByRef tabColumns As EntityColumnDescriptors, _
   Optional acmEntityType As AcmAttrContainerType = eactClass, _
   Optional acmEntityIndex As Integer = -1, _
   Optional ByRef specifics As String = "", _
   Optional addComma As Boolean = True, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef infix As String = "", _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional attrCat As AttrCategory = eacRegular, _
   Optional ByRef fkRelIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional ByRef attrIsReUsed As Boolean = False, _
   Optional ByRef comment As String = "", _
   Optional ByRef default As String = "", _
   Optional ByVal isVirtual As Boolean = False, _
   Optional ByVal isOptional As Boolean = False, _
   Optional attrIndex As Integer = -1, _
   Optional ByVal persisted As Boolean = True _
 ) As String
 ' ### ELSE IVK ###
 'Function genTransformedAttrDeclByDomainWithColReUse( _
 ' ByRef attrName As String, _
 ' ByRef attrNameShort As String, _
 ' valueType As AttrValueType, _
 ' valueTypeIndex As Integer, _
 ' ByRef transformation As AttributeListTransformation, _
 ' ByRef tabColumns As EntityColumnDescriptors, _
 ' Optional acmEntityType As AcmAttrContainerType = eactClass, _
 ' Optional acmEntityIndex As Integer = -1, _
 ' Optional ByRef specifics As String = "", _
 ' Optional addComma As Boolean = True, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional ByRef infix As String = "", _
 ' Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
 ' Optional attrCat As AttrCategory = eacRegular, _
 ' Optional ByRef fkRelIndex As Integer = -1, _
 ' Optional indent As Integer = 1, _
 ' Optional ByRef attrIsReUsed As Boolean = False, _
 ' Optional ByRef comment As String = "", _
 ' Optional ByRef default As String = "", _
 ' Optional ByVal isOptional As Boolean = False, _
 ' Optional attrIndex As Integer = -1 _
 ') As String
 ' ### ENDIF IVK ###
   On Error GoTo ErrorExit

   genTransformedAttrDeclByDomainWithColReUse = ""
 
 ' FIXME: This Proc-Level is redundant ?

 ' ### IF IVK ###
   genTransformedAttrDeclByDomainWithColReUse = _
     genTransformedAttrDeclWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, transformation, tabColumns, _
       acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, _
       indent, attrIsReUsed, comment, default, , isVirtual, isOptional, attrIndex, persisted)
 ' ### ELSE IVK ###
 '   genTransformedAttrDeclByDomainWithColReUse = _
 '     genTransformedAttrDeclWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, transformation, tabColumns, _
 '       acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, outputMode, attrCat, fkRelIndex, _
 '       indent, attrIsReUsed, comment, default, , isOptional, attrIndex)
 ' ### ENDIF IVK ###
 
 NormalExit:
   On Error Resume Next
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 
 
 Function genTransformedAttrDeclByDomain( _
   ByRef attrName As String, _
   ByRef attrNameShort As String, _
   valueType As AttrValueType, _
   valueTypeIndex As Integer, _
   ByRef transformation As AttributeListTransformation, _
   Optional acmEntityType As AcmAttrContainerType = eactClass, _
   Optional acmEntityIndex As Integer = -1, _
   Optional ByRef specifics As String = "", _
   Optional addComma As Boolean = True, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef infix As String = "", _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional attrCat As AttrCategory = eacRegular, _
   Optional ByRef fkRelIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional ByRef comment As String = "", _
   Optional ByRef default As String = "", _
   Optional ByVal isVirtual As Boolean = False, _
   Optional ByVal isOptional As Boolean = False, _
   Optional attrIndex As Integer = -1, _
   Optional ByVal persisted As Boolean = True _
 ) As String
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

 ' ### IF IVK ###
   genTransformedAttrDeclByDomain = _
     genTransformedAttrDeclByDomainWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, _
       transformation, tabColumns, acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, _
       outputMode, attrCat, fkRelIndex, indent, , comment, default, isVirtual, isOptional, attrIndex, persisted)
 ' ### ELSE IVK ###
 ' genTransformedAttrDeclByDomain = _
 '   genTransformedAttrDeclByDomainWithColReUse(attrName, attrNameShort, valueType, valueTypeIndex, _
 '     transformation, tabColumns, acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, _
 '     outputMode, attrCat, fkRelIndex, indent, , comment, default, isOptional, attrIndex)
 ' ### ENDIF IVK ###
 End Function
 
 
 Function genAttrDeclByDomain( _
   ByRef attrName As String, _
   ByRef attrNameShort As String, _
   valueType As AttrValueType, _
   valueTypeIndex As Integer, _
   Optional acmEntityType As AcmAttrContainerType = eactClass, _
   Optional acmEntityIndex As Integer = -1, _
   Optional ByRef specifics As String = "", _
   Optional addComma As Boolean = True, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef infix As String = "", _
   Optional outputMode As DdlOutputMode = edomDeclNonLrt, _
   Optional attrCat As AttrCategory = eacRegular, _
   Optional ByRef fkRelIndex As Integer = -1, _
   Optional indent As Integer = 1, _
   Optional isOptional As Boolean = False, _
   Optional ByRef comment As String = "" _
 ) As String
   genAttrDeclByDomain = _
     genTransformedAttrDeclByDomain(attrName, attrNameShort, valueType, valueTypeIndex, nullAttributeTransformation, _
       acmEntityType, acmEntityIndex, specifics, addComma, ddlType, infix, outputMode, attrCat, _
       fkRelIndex, indent, comment, , , isOptional)
 End Function
 
 
 ' ### IF IVK ###
 Function genAttrName( _
   ByRef attrName As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef entityNameShort As String = "", _
   Optional ByRef infix As String = "", _
   Optional ByRef suffix As String = "", _
   Optional ByVal valueType As AttrValueType = eavtDomain, _
   Optional ByVal isNational As Boolean = False, _
   Optional ByVal forDb As Boolean = True _
 ) As String
 ' ### ELSE IVK ###
 'Function genAttrName( _
 ' ByRef attrName As String, _
 ' Optional ddlType As DdlTypeId = edtLdm, _
 ' Optional ByRef entityNameShort As String = "", _
 ' Optional ByRef infix As String = "", _
 ' Optional ByRef suffix As String = "", _
 ' Optional ByVal valueType As AttrValueType = eavtDomain, _
 '  Optional ByVal forDb As Boolean = True _
 ') As String
 ' ### ENDIF IVK ###
   Dim result As String
   genAttrName = "X?"

 ' ### IF IVK ###
   If isNational Then
     If entityNameShort <> "" And Len(entityNameShort) = 3 Then
       infix = infix & Replace(gc_anSuffixNat, "_", "", 1, 1)
     Else
       suffix = suffix & gc_anSuffixNat
     End If
   End If
 ' ### ENDIF IVK ###

   If valueType = eavtEnum Then
     suffix = suffix & gc_enumAttrNameSuffix
   End If

   If forDb Then
     genAttrName = _
       Left( _
         UCase( _
           entityNameShort & IIf(entityNameShort = "", "", "_") & _
           infix & _
           IIf(infix = "", "", "_") & _
           attrName & _
           suffix _
         ), _
         gc_dbMaxAttributeNameLength _
       )
   Else
     genAttrName = _
       entityNameShort & IIf(entityNameShort = "", "", "_") & _
       infix & _
       IIf(infix = "", "", "_") & _
       attrName & _
       suffix
   End If
 End Function
 
 
 Function genAttrNameByIndex( _
   ByRef attrIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm _
 ) As String
     genAttrNameByIndex = genAttrName(g_attributes.descriptors(attrIndex).attributeName, ddlType, , , , g_attributes.descriptors(attrIndex).valueType)
 End Function
 
 
 Function genPkName( _
   ByRef objName As String, _
   ByRef objNameShort As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False _
 ) As String
   genPkName = ""

   If ddlType = edtLdm Then
     genPkName = "PK_" & UCase(objNameShort) & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, "")
   ElseIf ddlType = edtPdm Then
     genPkName = "PK_" & UCase(objNameShort) & genOrgId(thisOrgIndex, ddlType) & genPoolId(thisPoolIndex, ddlType) & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, "")
   End If
 End Function
 
 
 Function genUkName( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   ByRef objNameShort As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False _
 ) As String
   Dim prefix As String
   prefix = IIf(forLrt, "IDX", "UK") & IIf(forMqt, gc_dbObjSuffixShortMqt, "") & "_"

   genUkName = _
     genQualObjName(sectionIndex, _
       prefix & UCase(objNameShort) & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, ""), _
       prefix & UCase(objNameShort) & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, ""), _
       ddlType, thisOrgIndex, thisPoolIndex)
 End Function
 
 
 Function genQualPkName( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   ByRef objNameShort As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False _
 ) As String
   Dim prefix As String
   prefix = "PK_"

   genQualPkName = _
     genQualObjName(sectionIndex, _
       prefix & UCase(objNameShort) & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, ""), _
       prefix & UCase(objNameShort) & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, ""), _
       ddlType, thisOrgIndex, thisPoolIndex)
 End Function
 
 Function genQualUkName( _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   ByRef objNameShort As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False, _
   Optional forMqt As Boolean = False _
 ) As String
   Dim prefix As String
   prefix = ""

   genQualUkName = _
     Replace(genQualObjName(sectionIndex, _
       prefix & objNameShort & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, ""), _
       prefix & objNameShort & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, ""), _
       ddlType, thisOrgIndex, thisPoolIndex), "<<MPCID>>", "<<mpcId>>")
 End Function
 
 Function genFkName( _
   ByRef objName As String, _
   ByRef objNameShort As String, _
   ByRef refObjDescr As String, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional forGen As Boolean = False, _
   Optional forLrt As Boolean = False _
 ) As String
   If ddlType = edtLdm Then
     genFkName = "FK_" & UCase(objNameShort & IIf(refObjDescr = "", "", refObjDescr) & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, ""))
   ElseIf ddlType = edtPdm Then
     Dim numInfix As String
     numInfix = genOrgId(thisOrgIndex, ddlType) & genPoolId(thisPoolIndex, ddlType)
     genFkName = "FK_" & numInfix & UCase(objNameShort & IIf(refObjDescr = "", "", refObjDescr) & IIf(forLrt, "_" & gc_dbObjSuffixLrt, "") & IIf(forGen, "_" & gc_dbObjSuffixGen, ""))
   End If
 End Function
 
 
 ' ### IF IVK ###
 Function genPartitionName( _
   oid As Long, _
   Optional byPsOid As Boolean = True, _
   Optional ByRef cid As String = "" _
 ) As String
   genPartitionName = IIf(byPsOid, "P", "D") & cid & Right("000000000000000000000000000000000000000000000000" & CStr(oid), gc_maxDb2PartitionNameSuffixLen)
 End Function
 
 
 ' ### ENDIF IVK ###
 Function genMetaFileName( _
   ByRef dir As String, _
   ByRef fileBase As String, _
   Optional ByRef suffix As String = ".lst" _
 ) As String
   genMetaFileName = dir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & "\meta\" & fileBase & suffix
 End Function
 
 
 Function genCsvFileName( _
   ByRef dir As String, _
   ByRef sectionIndex As Integer, _
   ByRef objName As String, _
   step As Integer, _
   Optional ByRef subDir As String = "", _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional isCommonToOrg As Boolean = True, _
   Optional isCommonToPools As Boolean = True, _
   Optional forOrgIndex As Integer = -1 _
 ) As String
   Dim seqNo As Integer
   seqNo = getSectionSeqNoByIndex(sectionIndex)
   Dim fileBase As String

   thisOrgIndex = getEffectiveOrgIndex(thisOrgIndex, isCommonToOrg)
   thisPoolIndex = getEffectivePoolIndex(thisPoolIndex, isCommonToPools)

   If forOrgIndex = -1 Then
     forOrgIndex = thisOrgIndex
   End If

   fileBase = _
     IIf(subDir = "", "", subDir & "\") & Right("00000" & seqNo, seqNoDigits) & _
     "-" & Right("00000" & step, stepDigits) & "-" & _
     genQualObjName(sectionIndex, objName, "", ddlType, thisOrgIndex, thisPoolIndex) & _
     "." & gc_fileNameSuffixCsv
 
   genCsvFileName = ""
 
   Dim dirInfix As String
   dirInfix = IIf(g_genLrtSupport, "-LRT", "")

   If ddlType = edtLdm Then
     dirInfix = "\LDM" & dirInfix

     genCsvFileName = dir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & dirInfix & "\CSV\" & fileBase
   Else
     Dim orgIsTemplate As Boolean
     orgIsTemplate = False
     If forOrgIndex > 0 Then
       orgIsTemplate = g_orgs.descriptors(forOrgIndex).isTemplate
     End If

     dirInfix = "\PDM" & dirInfix

     Dim orgNameInfix As String, poolName As String
     If orgIsTemplate Then
       dirInfix = dirInfix & "\template\"
       orgNameInfix = Replace(getOrgNameByIndex(forOrgIndex), " ", "_", , , vbTextCompare)
     Else
       orgNameInfix = gc_dirPrefixOrg & genOrgIdByIndex(thisOrgIndex, ddlType) & "-" & Replace(getOrgNameByIndex(forOrgIndex), " ", "_", , , vbTextCompare)
     End If

     If thisPoolIndex > 0 Then
       poolName = Replace(g_pools.descriptors(thisPoolIndex).name, " ", "_", , , vbTextCompare)
     End If

     genCsvFileName = _
       dir & _
       IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & _
       dirInfix & IIf((thisOrgIndex < 0) And Not orgIsTemplate, "", orgNameInfix) & _
       IIf(thisPoolIndex < 0, "", "\DPool-" & genPoolIdByIndex(thisPoolIndex, ddlType) & "-" & poolName) & "\CSV\" & fileBase
   End If
 End Function
 
 
 Private Function genXFileName( _
   ByRef dir As String, _
   ByVal sectionIndex As Integer, _
   ByRef step As Integer, _
   ByRef ddlType As DdlTypeId, _
   ByRef suffix As String, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef subDir As String = "", _
   Optional ByRef increment As Integer = 0, _
   Optional ldmIteration As Integer = -1 _
 ) As String
   Dim seqNo As Integer
   seqNo = getSectionSeqNoByIndex(sectionIndex)
   Dim fileBase As String
 
   If ddlType = edtLdm Then
     fileBase = _
       IIf(subDir = "", "", subDir & "\") & IIf(ldmIteration >= 0, ldmIteration & "-", "") & Right("00000" & seqNo + increment, seqNoDigits) & _
       "-" & Right("00000" & step, stepDigits) & "-" & UCase(g_sections.descriptors(sectionIndex).sectionName) & "." & suffix
   Else
     fileBase = _
       IIf(subDir = "", "", subDir & "\") & Right("00000" & seqNo + increment, seqNoDigits) & _
       "-" & Right("00000" & step, stepDigits) & "-" & UCase(g_sections.descriptors(sectionIndex).sectionName) & "." & suffix
   End If
   Dim dirInfix As String
   dirInfix = IIf(g_genLrtSupport, "-LRT", "")

   genXFileName = ""

   If ddlType = edtLdm Then
     dirInfix = "\LDM" & dirInfix

     genXFileName = dir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & dirInfix & "\" & fileBase
   ElseIf ddlType = edtPdm Then
     Dim orgIsTemplate As Boolean
     If thisOrgIndex > 0 Then
       orgIsTemplate = g_orgs.descriptors(thisOrgIndex).isTemplate
     Else
       orgIsTemplate = False
     End If

     dirInfix = "\PDM" & dirInfix & IIf(orgIsTemplate, "\template", "")

     Dim orgNameInfix As String, poolName As String
     orgNameInfix = IIf(orgIsTemplate, "", gc_dirPrefixOrg & genOrgId(thisOrgIndex, ddlType) & "-") & Replace(getOrgNameByIndex(thisOrgIndex), " ", "_", , , vbTextCompare)

     poolName = Replace(getDataPoolNameByIndex(thisPoolIndex), " ", "_", , , vbTextCompare)

     genXFileName = _
       dir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & _
       dirInfix & IIf(thisOrgIndex <= 0, "", "\" & orgNameInfix) & _
       IIf(thisPoolIndex <= 0, "", "\DPool-" & genPoolId(thisPoolIndex, ddlType) & "-" & poolName) & "\" & fileBase
   End If
 End Function
 
 
 Function genDdlFileName( _
   ByRef dir As String, _
   ByVal sectionIndex As Integer, _
   ByRef step As Integer, _
   ByRef ddlType As DdlTypeId, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef subDir As String = "", _
   Optional increment As Integer = 0, _
   Optional ldmIteration As Integer = ldmIterationGlobal _
 ) As String
   genDdlFileName = genXFileName(dir, sectionIndex, step, ddlType, gc_fileNameSuffixDdl, thisOrgIndex, thisPoolIndex, subDir, increment, ldmIteration)
 End Function
 
 
 Private Sub addHeaderLine( _
   ByRef fileNo As Integer, _
   Optional ByRef line As String = "" _
 )
   Const lineLength = 88
   Const blanks = "                                                                                                         "

   Print #fileNo, "-- # "; Left(line & blanks, lineLength - 4); " #"
 End Sub
 
 
 Private Sub addDxlFileHeader( _
   ByRef fileNo As Integer, _
   ByRef fileName As String _
 )
   Const maxFileNameLen = 80

   If generateDdlHeader Then
     If LOF(fileNo) = 0 Then
       fileName = Replace(fileName, "\", "/")

       If workSheetSuffix & "" <> "" Then
         fileName = Replace(fileName, workSheetSuffix & "/", "")
       End If

       Const extraBlanks = "                                                                                                              "
       Dim fileNameLength As Integer
       fileNameLength = Len(fileName)
       If fileNameLength > maxFileNameLen Then
         fileNameLength = maxFileNameLen
       End If

       Print #fileNo,
       Print #fileNo, "-- "; gc_sqlDelimLine1
       addHeaderLine(fileNo)
       addHeaderLine(fileNo, "File: " & fileName)
       addHeaderLine(fileNo)
       Print #fileNo, "-- "; gc_sqlDelimLine1
       Print #fileNo,
       Print #fileNo, "ECHO processing file '"; Left(fileName, maxFileNameLen); "'"; gc_sqlCmdDelim

       ' add extra blanks to make sure that we can exactly recognize 'empty' files by their size

       Print #fileNo, IIf(fileNameLength >= maxFileNameLen, "", Left(extraBlanks, maxFileNameLen - fileNameLength))

       If ddlEmptyFileSize = 0 Then ddlEmptyFileSize = LOF(fileNo)
     End If
   Else
     ddlEmptyFileSize = 0
   End If
 End Sub
 
 
 Function openDdlFile( _
   ByRef dir As String, _
   ByRef sectionIndex As Integer, _
   ByRef step As Integer, _
   ByRef ddlType As DdlTypeId, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef subDir As String = "", _
   Optional increment As Integer = 0, _
   Optional ldmIteration As Integer = ldmIterationGlobal _
 ) As Integer
   Dim fileName As String
   Dim fileNo As Integer
 
   openDdlFile = -1

   fileName = genDdlFileName(dir, sectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, subDir, increment, ldmIteration)
   assertDir(fileName)
   fileNo = FreeFile()
 
   Open fileName For Append As #fileNo
   addDxlFileHeader(fileNo, Right(fileName, Len(fileName) - Len(dir) - 1))
   openDdlFile = fileNo

   Exit Function
 End Function
 
 
 Function openDdlFileBySectionIndex( _
   ByRef dir As String, _
   ByRef thisSectionIndex As Integer, _
   ByRef step As Integer, _
   ByRef ddlType As DdlTypeId, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef subDir As String = "", _
   Optional incrementIndex As Integer = 1, _
   Optional ldmIteration As Integer = ldmIterationGlobal _
 ) As Integer
   openDdlFileBySectionIndex = -1

   On Error GoTo ErrorExit

   If ddlType <> edtPdm Then
     thisOrgIndex = -1
     thisPoolIndex = -1
   End If

     If g_sections.descriptors(thisSectionIndex).fileNoDdl(thisOrgIndex, thisPoolIndex, step, incrementIndex) <= 0 Then
       Dim fileName As String
       Dim fileNo As Integer
       fileName = _
         genDdlFileName( _
           dir, thisSectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, subDir, _
           g_fileNameIncrements(incrementIndex), ldmIteration _
         )
       assertDir(fileName)

       fileNo = FreeFile()
       Open fileName For Append As #fileNo
       addDxlFileHeader(fileNo, Right(fileName, Len(fileName) - Len(dir) - 1))

       g_sections.descriptors(thisSectionIndex).fileNoDdl(thisOrgIndex, thisPoolIndex, step, incrementIndex) = fileNo
     End If

     openDdlFileBySectionIndex = g_sections.descriptors(thisSectionIndex).fileNoDdl(thisOrgIndex, thisPoolIndex, step, incrementIndex)
 
 NormalExit:
   Exit Function
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Function
 
 
 Sub closeAllCsvFiles( _
   Optional ddlType = edtLdm _
 )
   closeCsvFilesLPdmTable()
 End Sub
 
 
 Sub closeAllDdlFiles( _
   Optional ByVal orgIndex As Integer = -1, _
   Optional ByVal poolIndex As Integer = -1, _
   Optional ByVal sectionIndex As Integer = -1, _
   Optional ByVal processingStep As Integer = -1, _
   Optional ByVal incrementIndex As Integer = -1, _
   Optional ByVal ddlType As DdlTypeId = edtPdm _
 )
   If ddlType <> edtPdm Then
     orgIndex = -1
     poolIndex = -1
   End If

   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer
   Dim thisSectionIndex As Integer
   Dim thisProcessingStep As Integer
   Dim thisIncrementIndex As Integer

   For thisOrgIndex = orgIndex To IIf(ddlType = edtPdm, g_orgs.numDescriptors, orgIndex)
     For thisPoolIndex = poolIndex To IIf(ddlType = edtPdm, g_pools.numDescriptors, poolIndex)
       For thisSectionIndex = IIf(sectionIndex > 0, sectionIndex, 1) To IIf(sectionIndex > 0, sectionIndex, g_sections.numDescriptors)
           For thisProcessingStep = IIf(processingStep > 0, processingStep, 1) To IIf(processingStep > 0, processingStep, gc_maxProcessingStep)
             For thisIncrementIndex = IIf(incrementIndex > 0, incrementIndex, LBound(g_fileNameIncrements)) To IIf(incrementIndex > 0, incrementIndex, UBound(g_fileNameIncrements))
               If g_sections.descriptors(thisSectionIndex).fileNoDdl(thisOrgIndex, thisPoolIndex, thisProcessingStep, thisIncrementIndex) > 0 Then
                 Close #g_sections.descriptors(thisSectionIndex).fileNoDdl(thisOrgIndex, thisPoolIndex, thisProcessingStep, thisIncrementIndex)
                 g_sections.descriptors(thisSectionIndex).fileNoDdl(thisOrgIndex, thisPoolIndex, thisProcessingStep, thisIncrementIndex) = -1
               End If
             Next thisIncrementIndex
           Next thisProcessingStep
       Next thisSectionIndex
     Next thisPoolIndex
   Next thisOrgIndex
 End Sub
 
 Function genDmlFileName( _
   ByRef dir As String, _
   ByRef sectionIndex As Integer, _
   ByRef step As Integer, _
   ByRef ddlType As DdlTypeId, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef subDir As String = "", _
   Optional increment As Integer = 0 _
 ) As String
   genDmlFileName = genXFileName(dir, sectionIndex, step, ddlType, gc_fileNameSuffixDml, thisOrgIndex, thisPoolIndex, subDir, increment)
 End Function
 
 
 Function openDmlFile( _
   ByRef dir As String, _
   ByRef sectionIndex As Integer, _
   ByRef step As Integer, _
   ByRef ddlType As DdlTypeId, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional ByRef subDir As String = "", _
   Optional increment As Integer = 0 _
 ) As Integer
   Dim fileName As String
   Dim fileNo As Integer

   fileName = genDmlFileName(dir, sectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, subDir, increment)
   assertDir(fileName)
   fileNo = FreeFile()

   Open fileName For Append As #fileNo
 '  addDxlFileHeader fileNo, baseName(fileName)
   addDxlFileHeader(fileNo, Right(fileName, Len(fileName) - Len(dir) - 1))
   openDmlFile = fileNo
 End Function
 
 
 ' ### IF IVK ###
 Function genHCfgFileName( _
   ByRef dir As String, _
   ByRef classIndex As Integer, _
   ddlType As DdlTypeId _
 ) As String
     genHCfgFileName = dir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & "\hcfg" & IIf(g_genLrtSupport, "-LRT", "") & "\" & LCase(g_classes.descriptors(classIndex).sectionName) & "\" & g_classes.descriptors(classIndex).className & ".hbm.xml"
 End Function
 
 
 Function genXmlExportFileName( _
   ByRef dir As String, _
   ByRef classIndex As Integer, _
   ddlType As DdlTypeId, _
   Optional forGen As Boolean = False, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 ) As String
   Dim usePsSubdirs As Boolean
   usePsSubdirs = False

     Dim unQualTabNameNoGen As String
     unQualTabNameNoGen = getUnqualObjName(genQualTabNameByClassIndex(classIndex, ddlType, thisOrgIndex, thisPoolIndex))

     genXmlExportFileName = _
       dir & IIf(workSheetSuffix <> "", "\" & workSheetSuffix, "") & "\" & _
       IIf(ddlType = edtLdm, "LDM-", "PDM-") & "xmlExport" & "\" & _
       LCase(g_classes.descriptors(classIndex).sectionName) & "\" & _
       IIf(usePsSubdirs And g_classes.descriptors(classIndex).isPsTagged, "[PS]\", "") & _
       unQualTabNameNoGen & "\" & _
       g_classes.descriptors(classIndex).className & IIf(forGen, "-GEN", "") & ".xmlExp.sql"
 End Function
 
 
 ' ### ENDIF IVK ###
 Function genLogFileName() As String
   If targetDir = "" Then
     targetDir = IIf(g_targetDir = "", dirName(ActiveWorkbook.FullName), g_targetDir)
   End If

   genLogFileName = targetDir & "\" & baseName(ActiveWorkbook.Name, gc_workBookSuffixes) & ".log"
 End Function
 
 
 Sub killFile( _
   ByRef filePath As String, _
   Optional onlyIfEmpty As Boolean = False _
 )
   Dim fileSize As Long
   On Error Resume Next
   fileSize = -1
   fileSize = FileLen(filePath)
 
   If fileSize < 0 Then
     Exit Sub
   End If
 
   If Not onlyIfEmpty Or fileSize = 0 Or (generateDdlHeader And (fileSize <= ddlEmptyFileSize)) Then
     Kill filePath
   End If
 End Sub
 
 
 Sub killCsvFileWhereEver( _
   ByRef sectionIndex As Integer, _
   ByRef entityName As String, _
   ByRef directory As String, _
   Optional step As Integer = 0, _
   Optional onlyIfEmpty As Boolean = False, _
   Optional ByRef subDir As String = "" _
 )
   On Error Resume Next

   On Error Resume Next
   If generateLdm Then
     killFile(genCsvFileName(directory, sectionIndex, entityName, step, subDir, edtLdm), onlyIfEmpty)
   End If

   If generatePdm Then
     killFile(genCsvFileName(directory, sectionIndex, entityName, step, subDir, edtPdm), onlyIfEmpty)
     Dim thisOrgIndex As Integer
     For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
       killFile(genCsvFileName(directory, sectionIndex, entityName, step, subDir, edtPdm, thisOrgIndex, , , , thisOrgIndex), onlyIfEmpty)

       Dim thisPoolIndex As Integer
       For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
         killFile(genCsvFileName(directory, sectionIndex, entityName, step, subDir, edtPdm, thisOrgIndex, thisPoolIndex, , , thisOrgIndex), onlyIfEmpty)
       Next thisPoolIndex
     Next thisOrgIndex
   End If
 NormalExit:
 End Sub
 
 
 Sub dropDdlByProcessingStepSectionAndDllType( _
   ByRef sectionIndex As Integer, _
   step As Integer, _
   ddlType As DdlTypeId, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1, _
   Optional onlyIfEmpty As Boolean = False _
 )
   If sectionIndex < 1 Then
     GoTo NormalExit
   End If
 
   Dim suffixes(1 To 2) As String
   Dim numSuffixes As Integer

   suffixes(1) = ""
   suffixes(2) = "Deploy"

   numSuffixes = IIf(thisOrgIndex <= 0, 2, 1)

   Dim sIndex As Integer, iIndex As Integer, i As Integer
   For iIndex = LBound(g_fileNameIncrements) To UBound(g_fileNameIncrements) Step 1
     For sIndex = LBound(suffixes) To (LBound(suffixes) + numSuffixes - 1) Step 1
       If suffixes(sIndex) <> "-" Then
         For i = ldmIterationGlobal To IIf(ddlType = edtLdm, ldmIterationPostProc, ldmIterationGlobal)
           killFile(genDdlFileName(g_targetDir, sectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, suffixes(sIndex), g_fileNameIncrements(iIndex), i), onlyIfEmpty)
         Next i
       End If
     Next sIndex
   Next iIndex
 
   Dim mSuffixes(1 To 1) As String
   mSuffixes(1) = "Deploy"
   Dim mIncrements(1 To 1) As Integer
   mIncrements(1) = phaseAliases
   For iIndex = LBound(mIncrements) To UBound(mIncrements) Step 1
     For sIndex = LBound(mSuffixes) To UBound(mSuffixes) Step 1
       killFile(genDmlFileName(g_targetDir, sectionIndex, step, ddlType, thisOrgIndex, thisPoolIndex, mSuffixes(sIndex), mIncrements(iIndex)), onlyIfEmpty)
     Next sIndex
   Next iIndex
 
 NormalExit:
 End Sub
 
 
 Sub dropDdlByProcessingStep( _
   step As Integer, _
   Optional onlyIfEmpty As Boolean = False _
 )
   Dim sectIndex As Integer
   For sectIndex = 1 To g_sections.numDescriptors Step 1
       If Not g_sections.descriptors(sectIndex).isTechnical Then
         dropDdlByProcessingStepAndSection(step, g_sections.descriptors(sectIndex).sectionIndex, onlyIfEmpty)
       End If
   Next sectIndex
 End Sub
 
 
 Sub dropDdlByProcessingStepAndSection( _
   step As Integer, _
   ByRef sectionIndex As Integer, _
   Optional onlyIfEmpty As Boolean = False, _
   Optional allLevels As Boolean = True _
 )
   If generateLdm Then
     dropDdlByProcessingStepSectionAndDllType(sectionIndex, step, edtLdm, , , onlyIfEmpty)
   End If

   If generatePdm Then
     dropDdlByProcessingStepSectionAndDllType(sectionIndex, step, edtPdm, , , onlyIfEmpty)
     If allLevels Then
     Dim orgIndex As Integer
       For orgIndex = 1 To g_orgs.numDescriptors Step 1
         dropDdlByProcessingStepSectionAndDllType(sectionIndex, step, edtPdm, orgIndex, , onlyIfEmpty)
         Dim poolIndex As Integer
         For poolIndex = 1 To g_pools.numDescriptors Step 1
           dropDdlByProcessingStepSectionAndDllType(sectionIndex, step, edtPdm, orgIndex, poolIndex, onlyIfEmpty)
         Next poolIndex
       Next orgIndex
     End If
   End If
 End Sub
 
 
 Sub dropDdl( _
   Optional onlyIfEmpty As Boolean = False _
 )
   Dim i As Integer
   For i = 0 To maxProcessingStep Step 1
     dropDdlByProcessingStep(i, onlyIfEmpty)
   Next i
 
   dropOrgsDdl(onlyIfEmpty)
   ddlEmptyFileSize = 0
 End Sub
 
 
 Sub dropCsv( _
   Optional onlyIfEmpty As Boolean = False _
 )
   dropRelationshipsCsv(onlyIfEmpty)
   dropRelationshipsNlCsv(onlyIfEmpty)
   dropClassesCsv(onlyIfEmpty)
   dropClassesNlCsv(onlyIfEmpty)
   dropEnumsCsv(onlyIfEmpty)
   dropEnumsNlCsv(onlyIfEmpty)
   dropAttributeCsv(onlyIfEmpty)
   dropAttributeNlCsv(onlyIfEmpty)
   dropDomainCsv(onlyIfEmpty)
   dropSectionsCsv(onlyIfEmpty)
   dropPrivilegesCsv(onlyIfEmpty)
   dropCleanJobsCsv(onlyIfEmpty)
 ' ### IF IVK ###
   dropDCompCsv(onlyIfEmpty)
 ' ### ENDIF IVK ###
   dropDbCfgProfilesCsv(onlyIfEmpty)
   dropTabCfgsCsv(onlyIfEmpty)
   dropSnapshotTypesCsv(onlyIfEmpty)
   dropSnapshotColsCsv(onlyIfEmpty)
   dropSnapshotFilterCsv(onlyIfEmpty)
   dropErrorCsv(onlyIfEmpty)
 End Sub
 
 
 Sub evalObjects()
   evalContainers()
 ' ### IF IVK ###
   evalOrgs()
 ' ### ENDIF IVK ###
   evalSections()
   evalDomains()
   ' we need to do this before relationships since new relationships may be added here
   evalAttributes()
   evalAttributesNl()
   evalClasses()
   evalClassesNl()
   evalRelationships()
   evalRelationshipsNl()
   evalEnums()
   evalEnumsNl()
   evalIndexes()
 ' ### IF IVK ###
   evalClasses2()
   evalTypes()
 ' ### ENDIF IVK ###
   evalTablespaces()
   evalBufferPools()
   evalIndexAttrs()
   evalPrivileges()
 ' ### IF IVK ###
   evalDComps()
 ' ### ENDIF IVK ###
   evalSnapshotTypes()

   ' link attributes to relationships and classes
   evalAttributes2()
 End Sub
 
 
 Sub genAcmMetaCsv( _
   ByVal ddlType As DdlTypeId _
 )
   genSectionAcmMetaCsv(ddlType)
   genEnumAcmMetaCsv(ddlType)
   genEnumNlAcmMetaCsv(ddlType)
   genClassAcmMetaCsv(ddlType)
   genClassNlAcmMetaCsv(ddlType)
   genRelationshipAcmMetaCsv(ddlType)
   genRelationshipNlAcmMetaCsv(ddlType)
   genAttributeAcmMetaCsv(ddlType)
   genAttributeNlAcmMetaCsv(ddlType)
   genDomainAcmMetaCsv(ddlType)
   genErrorCsv(ddlType)
 End Sub
 
 
 Sub genLdmMetaCsv( _
   ByVal ddlType As DdlTypeId _
 )
   genSectionLdmMetaCsv(ddlType)
 End Sub
 
 
 Sub genPdmMetaCsv( _
   ByVal ddlType As DdlTypeId _
 )
   If ddlType <> edtPdm Then
     Exit Sub
   End If

   genSectionPdmMetaCsv()
 End Sub
 
 
 Sub assertDir( _
   path As String _
 )
   Dim dirPath As String
   dirPath = dirName(path)
   If dirPath = "" Then
     Exit Sub
   End If

   On Error Resume Next
   Err.Number = 0
   MkDir dirPath
   If Err.Number = 76 Then
     assertDir (dirPath)
     MkDir (dirPath)
   End If
   If Err.Number <> 75 And Err.Number <> 0 Then
     logMsg(Err.Number & "/" & Err.description, ellError, edtNone)
   End If
 End Sub
 
 
 Function getUnqualObjName( _
   qualObjName As String _
 ) As String
   getUnqualObjName = qualObjName
   If qualObjName = "" Then
     Exit Function
   End If

   Dim dotPos As Integer
   For dotPos = 1 To Len(qualObjName)
     If Mid(qualObjName, dotPos, 1) = "." Then
       getUnqualObjName = Right(qualObjName, Len(qualObjName) - dotPos)
       Exit Function
     End If
   Next dotPos
 End Function
 
 
 Function getSchemaName( _
   qualTabName As String _
 ) As String
   getSchemaName = qualTabName
   If qualTabName = "" Then
     Exit Function
   End If

   Dim dotPos As Integer
   For dotPos = 1 To Len(qualTabName)
     If Mid(qualTabName, dotPos, 1) = "." Then
       getSchemaName = Left(qualTabName, dotPos - 1)
       Exit Function
     End If
   Next dotPos
 End Function
 
 
 Function getAcmEntityTypeKey( _
   acmEntityType As AcmAttrContainerType _
 ) As String
   If acmEntityType = eactClass Then
     getAcmEntityTypeKey = gc_acmEntityTypeKeyClass
   ElseIf acmEntityType = eactEnum Then
     getAcmEntityTypeKey = gc_acmEntityTypeKeyEnum
   ElseIf acmEntityType = eactRelationship Then
     getAcmEntityTypeKey = gc_acmEntityTypeKeyRel
 ' ### IF IVK ###
   ElseIf acmEntityType = eactType Then
     getAcmEntityTypeKey = gc_acmEntityTypeKeyType
 ' ### ENDIF IVK ###
   ElseIf acmEntityType = eactView Then
     getAcmEntityTypeKey = gc_acmEntityTypeKeyView
   End If
 End Function
 
 
 
 Function logLevelId( _
   logLvl As LogLevel _
 ) As String
   logLevelId = ""

   If logLvl = ellFatal Then
     logLevelId = "F"
   ElseIf logLvl = ellError Then
     logLevelId = "E"
   ElseIf logLvl = ellWarning Then
     logLevelId = "W"
   ElseIf logLvl = ellFixableWarning Then
     logLevelId = "w"
   ElseIf logLvl = ellInfo Then
     logLevelId = "I"
   End If
 End Function
 
 
 Sub logMsg( _
   ByRef msg As String, _
   logLvl As LogLevel, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 )
   If logLvl And g_logLevelsReport Then
     Dim fileNo As Integer
     Dim fileName As String
     fileName = genLogFileName()
     fileNo = FreeFile()
     Open fileName For Append As #fileNo

     Dim thisOrgId As Integer
     Dim thisPoolId As Integer
     If thisOrgIndex > 0 Then thisOrgId = g_orgs.descriptors(thisOrgIndex).id Else thisOrgId = -1
     If thisPoolIndex > 0 Then thisPoolId = g_pools.descriptors(thisPoolIndex).id Else thisPoolId = -1

     Dim msgPrefix As String
     msgPrefix = Now & " [" & logLevelId(logLvl) & "]: " & getDdlTypeDescr(ddlType) & IIf(g_genLrtSupport, "[LRT]", "")

     On Error Resume Next
     If ddlType = edtLdm Or ddlType = edtNone Then
       Print #fileNo, msgPrefix; ": "; msg
     Else
       Print #fileNo, msgPrefix; "["; CStr(g_orgs.descriptors(thisOrgIndex).id); "|"; CStr(thisPoolId); "] : "; msg
     End If
     Close #fileNo
   End If

   If logLvl And g_logLevelsMsgBox Then
     Dim button As VbMsgBoxStyle
     If logLvl = ellFatal Then
       button = vbCritical Or vbExclamation
     ElseIf logLvl = ellError Then
       button = vbCritical
     ElseIf logLvl = ellWarning Then
       button = vbExclamation
     End If

     MsgBox msg, button
     'wait 2
   End If
 End Sub
 
 
 Function genSheetFileName( _
   ByRef dir As String, _
   ByRef sheetName As String, _
   Optional ByRef subDirName As String = "Sheets" _
 ) As String
   genSheetFileName = dir & "\" & IIf(subDirName <> "", subDirName & "\", "") & sheetName & ".csv"
 End Function
 
 
 Sub exportSheetsByWorkbook( _
   ByRef thisWorkbook As Workbook _
 )
   Dim targetDir As String
   Dim fileName As String
   Dim fileNo As Integer
 
   targetDir = dirName(thisWorkbook.FullName) & "\" & baseName(thisWorkbook.Name, gc_workBookSuffixes)
   fileName = genSheetFileName(targetDir, "XXX")
   assertDir(fileName)

   logMsg("exporting Excel-Sheets to """ & dirName(fileName) & """", ellInfo, edtNone)

   On Error GoTo ErrorExit
   Dim sheetNum As Integer
   Dim rowNum As Integer
   Dim colNum As Integer
   Dim numEmpty As Integer
   Dim i As Integer
     For sheetNum = 1 To thisWorkbook.Sheets.Count
         fileName = genSheetFileName(targetDir, thisWorkbook.Sheets(sheetNum).Name)
         fileNo = FreeFile()
         Open fileName For Output As #fileNo
         rowNum = 1
         While thisWorkbook.Sheets(sheetNum).Cells(rowNum, 1) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum, 1) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum, 3) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum, 4) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum, 5) <> "" Or _
               thisWorkbook.Sheets(sheetNum).Cells(rowNum + 1, 1) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum + 1, 1) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum + 1, 3) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum + 1, 4) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum + 1, 5) <> "" Or _
               thisWorkbook.Sheets(sheetNum).Cells(rowNum + 2, 1) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum + 2, 1) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum + 2, 3) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum + 2, 4) <> "" Or thisWorkbook.Sheets(sheetNum).Cells(rowNum + 2, 5) <> ""
           numEmpty = 0
           For colNum = 1 To 100
             If CStr(thisWorkbook.Sheets(sheetNum).Cells(rowNum, colNum)) = "" Then
               numEmpty = numEmpty + 1
             Else
               For i = 1 To numEmpty
                 Print #fileNo, ";";
               Next i
               numEmpty = 0
               Print #fileNo, """"; Replace(CStr(thisWorkbook.Sheets(sheetNum).Cells(rowNum, colNum)), """", """"""); """;";
             End If
           Next colNum
           Print #fileNo,

           rowNum = rowNum + 1
         Wend
         Close #fileNo
     Next sheetNum
 
 NormalExit:
   On Error Resume Next
   Close #fileNo

   Exit Sub
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub exportSheets()
 Attribute exportSheets.VB_ProcData.VB_Invoke_Func = "E\n14"
   exportSheetsByWorkbook(ActiveWorkbook)
 End Sub
 
 Function getCsvTrailer( _
   numCommas As Integer _
 ) As String
   Dim str As String
   str = ""
   Dim i As Integer
   For i = 1 To numCommas
     str = str & ","
   Next i
   getCsvTrailer = str & "1"
 End Function
 
 
 Function pullOid() As Integer
   Const firstOid = 8000
   Const lastOid = 14999

   If nextOid = 0 Then
     nextOid = firstOid
   End If

   pullOid = nextOid
   nextOid = nextOid + 1
 End Function
 
 
 Sub resetOid()
   nextOid = 0
 End Sub
 
 
 Function arrayIsNull( _
   ByRef arr() As AttributeMappingForCl _
 ) As Boolean
   arrayIsNull = False
   On Error GoTo ErrorExit
   Dim i As Integer
   i = LBound(arr)

 NormalExit:
   Exit Function
 
 ErrorExit:
   arrayIsNull = True
 End Function
 
 
 Function strArrayIsNull( _
   ByRef arr() As String _
 ) As Boolean
   strArrayIsNull = False
   On Error GoTo ErrorExit
   Dim i As Integer
   i = LBound(arr)

 NormalExit:
   Exit Function
 
 ErrorExit:
   strArrayIsNull = True
 End Function
 
 
 Function getWorkSheetName( _
   ByRef workSheetBaseName As String, _
   ByRef suffix As String _
 ) As String
   getWorkSheetName = workSheetBaseName
   If Trim(suffix & "") = "" Then
     Exit Function
   End If

   Dim ws As Worksheet
 
   On Error GoTo ErrorExit
   Set ws = ActiveWorkbook.Worksheets(workSheetBaseName & "." & suffix)
 
   getWorkSheetName = workSheetBaseName & "." & suffix
 
 ErrorExit:
   Exit Function
 End Function
 
 
 Function genSrxType2Str( _
   srxType As SrxTypeId _
 ) As String
   If srxType = estSr0 Then
     genSrxType2Str = "SR0"
   ElseIf srxType = estSr1 Then
     genSrxType2Str = "SR1"
   ElseIf srxType = estNsr1 Then
     genSrxType2Str = "NSR1"
   Else
     genSrxType2Str = "- unknown -"
   End If
 End Function
 
 
 Function getPrimaryEntityLabelByIndex( _
   acmEntityType As AcmAttrContainerType, _
   acmEntityIndex As Integer _
 ) As String
   If acmEntityType = eactClass Then
     getPrimaryEntityLabelByIndex = getPrimaryClassLabelByIndex(acmEntityIndex)
   ElseIf acmEntityType = eactRelationship Then
     getPrimaryEntityLabelByIndex = getPrimaryRelationshipLabelByIndex(acmEntityIndex)
   Else
     getPrimaryEntityLabelByIndex = "<unknown entity type>"
   End If
 End Function
 
 
 Function listHasPostiveElement( _
   ByVal list As String _
 ) As Boolean
   listHasPostiveElement = False
   Dim elems() As String
   elems = split(list, ",")
 
   Dim i As Integer
   For i = LBound(elems) To UBound(elems)
     If elems(i) > 0 Then
       listHasPostiveElement = True
       Exit Function
     End If
   Next i
 End Function
 
 
 Function includedInList( _
   ByVal list As String, _
   ByRef element As Integer _
 ) As Boolean
   list = Replace(list, " ", "")
   list = Replace(list, ".", ",")

   If (element < 0) Or (list = "") Then
     includedInList = True
   Else
     Dim elems() As String
     elems = split(list, ",")
 
     Dim i As Integer
     For i = LBound(elems) To UBound(elems)
       If elems(i) = ("-" & CStr(element)) Then
         includedInList = False
         Exit Function
       ElseIf elems(i) = CStr(element) Then
         includedInList = True
         Exit Function
       End If
     Next i
     includedInList = Left(list, 1) = "-"
   End If
 End Function
 
 
 Sub printConditional( _
   fileNo As Integer, _
   ByRef line As String, _
   Optional condition As Boolean = True, _
   Optional indent As Integer = 0 _
 )
   If condition And (line <> "") Then
     Print #fileNo, addTab(indent); line
   End If
 End Sub
 
 
