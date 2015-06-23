 Attribute VB_Name = "M92_DBUtilities"
 Option Explicit
 
 Private Const processingStepUtilities = 4
 
 Global Const tempTabNameOids = "SESSION.Oids"
 Global Const tempTabNameInvExpOids = "SESSION.InvExpOids"
 
 
 Sub genDdlForTempInvExpOids( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary tables for OIDs", indent)
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempTabNameInvExpOids
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 
 Sub genDdlForTempOids( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader(fileNo, "temporary tables for OIDs", indent)
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempTabNameOids
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid "; g_dbtOid
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer(fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve)
 End Sub
 
 
 Sub genDbUtilitiesDdl( _
   ddlType As DdlTypeId _
 )
   If ddlType = edtLdm Then
     genDbUtilitiesDdlByDdl(edtLdm)
   ElseIf ddlType = edtPdm Then
     genDbUtilitiesDdlByDdl(edtPdm)
 ' ### IF IVK ###

     genDbUtilitiesDdlByPool(edtPdm)

     Dim thisOrgIndex As Integer
     For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
       Dim thisPoolIndex As Integer
       For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And g_pools.descriptors(thisPoolIndex).commonItemsLocal Then
             genDbUtilitiesDdlByPool(edtPdm, thisOrgIndex, thisPoolIndex)
           End If
       Next thisPoolIndex
     Next thisOrgIndex
 ' ### ENDIF IVK ###
   End If
 End Sub
 
 
 Sub genDbUtilitiesDdlByDdl( _
   ddlType As DdlTypeId _
 )
   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbMeta, processingStepUtilities, ddlType, , , , phaseDbSupport)

   On Error GoTo ErrorExit

   Dim qualFuncNameStrTrim As String
   qualFuncNameStrTrim = genQualFuncName(g_sectionIndexMeta, udfnStrTrim, ddlType)

   Dim qualFuncNameStrElemIndexes As String
   Dim qualFuncNameLastStrElem As String
 
   Dim qualFuncNamePosStr As String
   qualFuncNamePosStr = genQualFuncName(g_sectionIndexMeta, udfnPosStr, ddlType, , , , , , True)

   Dim qualFuncNameOccurs As String
   qualFuncNameOccurs = genQualFuncName(g_sectionIndexMeta, udfnOccurs, ddlType, , , , , , True)

   Dim qualFuncNameOccursShort As String
   qualFuncNameOccursShort = genQualFuncName(g_sectionIndexMeta, udfnOccursShort, ddlType, , , , , , True)

   Dim maxTrimParamLength As Integer
   maxTrimParamLength = 1024
   ' ####################################################################################################################
   ' #    Function trimming limited length strings
   ' ####################################################################################################################

   printSectionHeader("Function trimming limited length strings", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameStrTrim
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "str_in", "VARCHAR(" & CStr(maxTrimParamLength) & ")", True, "string-encode list delimited by 'delimiter_in'")
   genProcParm(fileNo, "", "maxLength_in", "INTEGER", True, "maximum length of string returned")
   genProcParm(fileNo, "", "trailer_in", "VARCHAR(5)", False, "trailer added at string end if string is cut off at the end")
   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR("; CStr(maxTrimParamLength); ")"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "CONTAINS SQL"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_strLength", "INTEGER", "0")
   genVarDecl(fileNo, "v_maxLength", "INTEGER", "0")

   genProcSectionHeader(fileNo, "special handling if input parameters are NULL")
   Print #fileNo, addTab(1); "IF (str_in IS NULL) OR (maxLength_in IS NULL) THEN"
   Print #fileNo, addTab(2); "RETURN str_in;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "SET v_strLength = LENGTH(str_in);"
   Print #fileNo, addTab(1); "SET v_maxLength = maxLength_in - COALESCE(LENGTH(trailer_in), 0);"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_strLength > maxLength_in THEN"
   Print #fileNo, addTab(2); "RETURN LEFT(str_in, v_maxLength) || COALESCE(trailer_in, '');"
   Print #fileNo, addTab(1); "ELSE"
   Print #fileNo, addTab(2); "RETURN str_in;"
   Print #fileNo, addTab(1); "END IF;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   Dim strParamDbDataType As String
   Dim maxStrElemNumber As Long
   Dim contextParamDbDataType As String
   Dim maxElemLength As Integer
   Dim useContext As Boolean
   Dim fNameSuffix As String
   Dim i As Integer
   For i = 1 To 3
     useContext = (i = 3)
     contextParamDbDataType = ""
     maxElemLength = 50
     useContext = False
     fNameSuffix = ""

     If i = 1 Then
       strParamDbDataType = "VARCHAR(4000)"
       maxStrElemNumber = 1000
     ElseIf i = 2 Then
       strParamDbDataType = "CLOB(1M)"
       maxStrElemNumber = 250000
     ElseIf i = 4 Then
       strParamDbDataType = "CLOB(100M)"
       maxStrElemNumber = 2000000
       fNameSuffix = "_X"
     Else
       strParamDbDataType = "VARCHAR(32672)"
       maxStrElemNumber = 1000
       contextParamDbDataType = "VARCHAR(5)"
       useContext = True
       maxElemLength = 200
     End If

     qualFuncNameStrElemIndexes = genQualFuncName(g_sectionIndexMeta, udfnStrElemIndexes & fNameSuffix, ddlType)
     qualFuncNameLastStrElem = genQualFuncName(g_sectionIndexMeta, udfnLastStrElem & fNameSuffix, ddlType)

     ' ####################################################################################################################
     ' #    Function for retrieving the delimiter index-positions in string-encoded lists
     ' ####################################################################################################################

     printSectionHeader("Function for retrieving the delimiter index-positions in string-encoded lists", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); qualFuncNameStrElemIndexes
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "", "list_in", strParamDbDataType, True, "string-encode list delimited by 'delimiter_in'")
     genProcParm(fileNo, "", "delimiter_in", "CHAR(1)", useContext, "delimiter for string parsing")
     If useContext Then
       genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, True, "prefix supposed to precede the delimiter")
       genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, False, "postfix supposed to follow the delimiter")
     End If
     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); "RETURNS TABLE"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "ordinal  INTEGER,"
     Print #fileNo, addTab(2); "posIndex INTEGER"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "CONTAINS SQL"
     Print #fileNo, addTab(0); "RETURN"

     Print #fileNo, addTab(1); "WITH"
     Print #fileNo, addTab(2); "V"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "ordinal,"
     Print #fileNo, addTab(2); "posIndex"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "AS"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "VALUES ( 0, 0 )"
     Print #fileNo, addTab(2); "UNION ALL"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "ordinal+1,"
     If useContext Then
       Print #fileNo, addTab(3); "COALESCE(LOCATE(contextPrefix_in || delimiter_in || contextPostfix_in, list_in, posIndex + 1) + LENGTH(contextPrefix_in), LENGTH(list_in) + 1)"
     Else
       Print #fileNo, addTab(3); "COALESCE(NULLIF(LOCATE(delimiter_in, list_in, posIndex+1), 0), LENGTH(list_in)+1)"
     End If
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "V"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "ordinal < "; CStr(maxStrElemNumber)
     Print #fileNo, addTab(4); "AND"
     If useContext Then
       Print #fileNo, addTab(3); "LOCATE(contextPrefix_in || delimiter_in || contextPostfix_in, list_in, posIndex + LENGTH(delimiter_in)) <> 0"
     Else
       Print #fileNo, addTab(3); "LOCATE(delimiter_in, list_in, posIndex+1) <> 0"
     End If
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "ordinal,"
     Print #fileNo, addTab(2); "posIndex"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "V"
     Print #fileNo, addTab(1); "UNION ALL"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "MAX(ordinal)+1,"
     Print #fileNo, addTab(2); "LENGTH(list_in)+1"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "V"

     Print #fileNo, addTab(0); gc_sqlCmdDelim

     printSectionHeader("Function for retrieving the delimiter index-positions in string-encoded lists", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); qualFuncNameStrElemIndexes
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "", "list_in", strParamDbDataType, useContext, "string-encode list delimited by ','")
     If useContext Then
       genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, True, "prefix supposed to precede the delimiter")
       genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, False, "postfix supposed to follow the delimiter")
     End If
     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); "RETURNS TABLE"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "ordinal  INTEGER,"
     Print #fileNo, addTab(2); "posIndex INTEGER"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "CONTAINS SQL"
     Print #fileNo, addTab(0); "RETURN"

     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "ordinal,"
     Print #fileNo, addTab(2); "posIndex"
     Print #fileNo, addTab(1); "FROM"
     If useContext Then
       Print #fileNo, addTab(2); "TABLE ("; qualFuncNameStrElemIndexes; "(list_in, CAST(',' AS CHAR(1)), contextPrefix_in, contextPostfix_in)) AS X"
     Else
       Print #fileNo, addTab(2); "TABLE ("; qualFuncNameStrElemIndexes; "(list_in, CAST(',' AS CHAR(1)))) AS X"
     End If

     Print #fileNo, addTab(0); gc_sqlCmdDelim

     ' ####################################################################################################################
     ' #    Function for retrieving the elements of string-encoded lists
     ' ####################################################################################################################

     printSectionHeader("Function for retrieving the elements of string-encoded lists", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); g_qualFuncNameStrElems
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "", "list_in", strParamDbDataType, True, "string-encode list delimited by 'delimiter_in'")
     genProcParm(fileNo, "", "delimiter_in", "CHAR(1)", useContext, "delimiter for string parsing")
     If useContext Then
       genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, True, "prefix supposed to precede the delimiter")
       genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, False, "postfix supposed to follow the delimiter")
     End If
     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); "RETURNS TABLE"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "elem     VARCHAR("; CStr(maxElemLength); "),"
     Print #fileNo, addTab(2); "posIndex INTEGER"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "CONTAINS SQL"
     Print #fileNo, addTab(0); "RETURN"

     Print #fileNo, addTab(1); "WITH"
     Print #fileNo, addTab(2); "V"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "ordinal,"
     Print #fileNo, addTab(2); "posIndex"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "AS"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "ordinal,"
     Print #fileNo, addTab(3); "posIndex"
     Print #fileNo, addTab(2); "FROM"
     If useContext Then
       Print #fileNo, addTab(3); "TABLE ("; qualFuncNameStrElemIndexes; "(list_in, delimiter_in, contextPrefix_in, contextPostfix_in)) AS X"
     Else
       Print #fileNo, addTab(3); "TABLE ("; qualFuncNameStrElemIndexes; "(list_in, delimiter_in)) AS X"
     End If
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "RTRIM(LTRIM(SUBSTR(list_in, t1.posIndex+1, t2.posIndex - t1.posIndex - 1))),"
     Print #fileNo, addTab(2); "t1.ordinal"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "V AS t1"
     Print #fileNo, addTab(1); "JOIN"
     Print #fileNo, addTab(2); "V AS t2"
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); "t2.ordinal = t1.ordinal+1"

     Print #fileNo, addTab(0); gc_sqlCmdDelim

     ' ####################################################################################################################

     printSectionHeader("Function for retrieving the elements of string-encoded lists", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); g_qualFuncNameStrElems
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "", "list_in", strParamDbDataType, useContext, "string-encode list delimited by ','")
     If useContext Then
       genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, True, "prefix supposed to precede the delimiter")
       genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, False, "postfix supposed to follow the delimiter")
     End If
     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); "RETURNS TABLE"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "elem     VARCHAR("; CStr(maxElemLength); "),"
     Print #fileNo, addTab(2); "posIndex INTEGER"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "CONTAINS SQL"
     Print #fileNo, addTab(0); "RETURN"

     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "elem,"
     Print #fileNo, addTab(2); "posIndex"
     Print #fileNo, addTab(1); "FROM"
     If useContext Then
       Print #fileNo, addTab(2); "TABLE ("; g_qualFuncNameStrElems; "(list_in, CAST(',' AS CHAR(1)), contextPrefix_in, contextPostfix_in)) AS X"
     Else
       Print #fileNo, addTab(2); "TABLE ("; g_qualFuncNameStrElems; "(list_in, CAST(',' AS CHAR(1)))) AS X"
     End If

     Print #fileNo, addTab(0); gc_sqlCmdDelim

     ' ####################################################################################################################
     ' #    Function for retrieving the last element of a string-encoded list
     ' ####################################################################################################################

     printSectionHeader("Function for retrieving the last element of a string-encoded list", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); qualFuncNameLastStrElem
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "", "list_in", strParamDbDataType, True, "string-encode list delimited by 'delimiter_in'")
     genProcParm(fileNo, "", "delimiter_in", "CHAR(1)", useContext, "delimiter for string parsing")
     If useContext Then
       genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, True, "prefix supposed to precede the delimiter")
       genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, False, "postfix supposed to follow the delimiter")
     End If
     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); "RETURNS"
     Print #fileNo, addTab(1); "VARCHAR("; CStr(maxElemLength); ")"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "CONTAINS SQL"
     Print #fileNo, addTab(0); "RETURN"

     Print #fileNo, addTab(1); "WITH"
     Print #fileNo, addTab(2); "V"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "elem,"
     Print #fileNo, addTab(2); "seqNo"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "AS"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "elem,"
     Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY posIndex DESC)"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "TABLE ("; g_qualFuncNameStrElems; "(list_in, delimiter_in"; IIf(useContext, " , contextPrefix_in, contextPostfix_in", ""); ")) AS X"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "COALESCE(elem, '') <> ''"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "elem"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "V"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "seqNo =1"

     Print #fileNo, addTab(0); gc_sqlCmdDelim

     ' ####################################################################################################################

     printSectionHeader("Function for retrieving the last element of a string-encoded list", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); qualFuncNameLastStrElem
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "", "list_in", strParamDbDataType, useContext, "string-encode list delimited by 'delimiter_in'")
     If useContext Then
       genProcParm(fileNo, "", "contextPrefix_in", contextParamDbDataType, True, "prefix supposed to precede the delimiter")
       genProcParm(fileNo, "", "contextPostfix_in", contextParamDbDataType, False, "postfix supposed to follow the delimiter")
     End If
     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); "RETURNS"
     Print #fileNo, addTab(1); "VARCHAR("; CStr(maxElemLength); ")"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "CONTAINS SQL"
     Print #fileNo, addTab(0); "RETURN"

     Print #fileNo, addTab(1); "WITH"
     Print #fileNo, addTab(2); "V"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "elem,"
     Print #fileNo, addTab(2); "seqNo"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "AS"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "elem,"
     Print #fileNo, addTab(3); "ROWNUMBER() OVER (ORDER BY posIndex DESC)"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "TABLE ("; g_qualFuncNameStrElems; "(list_in, CAST(',' AS CHAR(1))"; IIf(useContext, " , contextPrefix_in, contextPostfix_in", ""); ")) AS X"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "COALESCE(elem, '') <> ''"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "elem"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); "V"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "seqNo =1"

     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i
 
   ' ####################################################################################################################
   ' #    Function for retrieving the delimiter index-positions in string-encoded lists
   ' ####################################################################################################################

   printSectionHeader("Function for retrieving the delimiter index-positions in string-encoded lists", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); g_qualFuncNameStrListMap
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "list_in", "VARCHAR(4000)", True, "string-encode list delimited by ','")
   genProcParm(fileNo, "", "elemVar1_in", "VARCHAR(20)", True, "optional prefix which is placed in front of each string element (1st nstance)")
   genProcParm(fileNo, "", "elemVar2_in", "VARCHAR(20)", True, "optional prefix which is placed in front of each string element (2nd instance)")
   genProcParm(fileNo, "", "elemOp_in", "VARCHAR(20)", True, "optional infix placed between the two instances of each string element")
   genProcParm(fileNo, "", "conjunction_in", "VARCHAR(10)", True, "optional infix placed between each expression element")
   genProcParm(fileNo, "", "useBrackets_in", "INTEGER", False, "specifies whether brackets need to be placed around expressions per string element")
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(2); "VARCHAR(8000)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "CONTAINS SQL"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader(fileNo, "declare variables", 1, True)
   genVarDecl(fileNo, "v_result", "VARCHAR(8000)", "''")
 
   genProcSectionHeader(fileNo, "loop over list elements")
 
   Print #fileNo, addTab(1); "FOR tabLoop AS"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "elem,"
   Print #fileNo, addTab(3); "posIndex"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "TABLE ("; g_qualFuncNameStrElems; "(list_in)) AS X"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "posIndex ASC"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_result ="
   Print #fileNo, addTab(4); "v_result ||"
   Print #fileNo, addTab(4); "(Case v_result WHEN '' THEN '' ELSE conjunction_in END) ||"
   Print #fileNo, addTab(4); "(CASE useBrackets_in WHEN 1 THEN '(' ELSE '' END) ||"
   Print #fileNo, addTab(4); "COALESCE(elemVar1_in,'') ||"
   Print #fileNo, addTab(4); "elem ||"
   Print #fileNo, addTab(4); "COALESCE(elemOp_in, '') ||"
   Print #fileNo, addTab(4); "(CASE COALESCE(elemVar2_in,'') WHEN '' THEN '' ELSE elemVar2_in || elem END) ||"
   Print #fileNo, addTab(4); "(CASE useBrackets_in WHEN 1 THEN ')' ELSE '' END);"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_result;"
   Print #fileNo, addTab(0); "END"
 
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function for decomposing classId - OID Lists
   ' ####################################################################################################################

   Dim qualFuncNameParseClassIdOidList As String
   qualFuncNameParseClassIdOidList = genQualFuncName(g_sectionIndexMeta, udfnParseClassIdOidList, ddlType, , , , , , True)
 
   printSectionHeader("Function for decomposing classId - OID Lists", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameParseClassIdOidList
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "classOidList_in", "CLOB(1M)", False, "'|'-separated List of pairs 'classId,Oid'")
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS TABLE"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId "; g_dbtEntityId; ","
   Print #fileNo, addTab(2); "oid "; g_dbtOid
   Print #fileNo, addTab(1); ")"
 
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "V_Pair"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "pair"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "RTRIM(LTRIM(REPLACE(REPLACE(elem, '<', ''), '>', '')))"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "TABLE ( "; g_qualFuncNameStrElems; "(classOidList_in, CAST('|' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(1); "),"
   Print #fileNo, addTab(2); "V_PairResolved"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "oid"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "LEFT(pair, POSSTR(pair, ',')-1),"
   Print #fileNo, addTab(3); "RIGHT(pair, LENGTH(pair)-POSSTR(pair, ','))"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_Pair"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "CAST(RIGHT('00000' || RTRIM(LTRIM(REPLACE(classId, '''', ''))), 5) AS "; g_dbtEntityId; "),"
   Print #fileNo, addTab(2); g_dbtOid; "(oid)"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V_PairResolved"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function determining whether a character string represents a (BIG-) INTEGER
   ' ####################################################################################################################

   printSectionHeader("Function determining whether a character string represents a (BIG-) INTEGER", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); g_qualFuncNameIsNumeric
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "str_in", "VARCHAR(25)", False, "string to analyze")
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtBoolean
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "CONTAINS SQL"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_str", "VARCHAR(25)", "NULL")
 
   genProcSectionHeader(fileNo, "if string is NULL return")
   Print #fileNo, addTab(1); "IF str_in IS NULL THEN"
   Print #fileNo, addTab(2); "RETURN NULL;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "strip off trailing and leading blank")
   Print #fileNo, addTab(1); "SET v_str = LTRIM(RTRIM(str_in));"
 
   genProcSectionHeader(fileNo, "if string is empty it does not represent an INTEGER")
   Print #fileNo, addTab(1); "IF (v_str = '') OR (POSSTR(v_str, '#') > 0) THEN"
   Print #fileNo, addTab(2); "RETURN 0;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "replace any numeric character")
   Print #fileNo, addTab(1); "SET v_str = REPLACE(TRANSLATE(v_str, '##########', '0123456789'), '#', '');"
 
   genProcSectionHeader(fileNo, "check whether result string is empty")
   Print #fileNo, addTab(1); "RETURN (CASE WHEN LENGTH(v_str) =0 THEN 1 ELSE 0 END);"
 
   Print #fileNo, addTab(1); "END"
 
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function determining the position of a search-string in a string
   ' ####################################################################################################################

   printSectionHeader("Function determining the position of a search-string in a string", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNamePosStr
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "str_in", "VARCHAR(1024)", True, "string to search in")
   genProcParm(fileNo, "", "searchStr_in", "VARCHAR(1024)", False, "string to search for")
 
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "INTEGER"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_i", "INTEGER", "1")
   genVarDecl(fileNo, "v_length", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_uBound", "INTEGER", "NULL")
 
   genProcSectionHeader(fileNo, "initialize variables")
   Print #fileNo, addTab(1); "SET v_length = LENGTH(searchStr_in);"
   Print #fileNo, addTab(1); "SET v_uBound = COALESCE(LENGTH(str_in) - v_length + 1, 0);"

   genProcSectionHeader(fileNo, "search substring")
   Print #fileNo, addTab(1); "WHILE v_i < v_uBound DO"
   Print #fileNo, addTab(2); "IF SUBSTR(str_in, v_i, v_length) = searchStr_in THEN"
   Print #fileNo, addTab(3); "RETURN v_i;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "SET v_i = v_i + 1;"
   Print #fileNo, addTab(1); "END WHILE;"

   genProcSectionHeader(fileNo, "string not found - return NULL")
   Print #fileNo, addTab(1); "RETURN NULL;"
 
   Print #fileNo, addTab(0); "END"
 
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function determining the number of occurrences of a search-string in a string
   ' ####################################################################################################################

   printSectionHeader("Function determining the number of occurrences of a search-string in a string", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameOccursShort
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "str_in", "VARCHAR(32000)", True, "string to search in")
   genProcParm(fileNo, "", "searchStr_in", "VARCHAR(1024)", False, "string to search for")
 
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "INTEGER"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_occurs", "INTEGER", "0")
 
   genProcSectionHeader(fileNo, "count matches")
   Print #fileNo, addTab(1); "IF LENGTH(searchStr_in) > 0 THEN"
   Print #fileNo, addTab(2); "SET v_occurs = (LENGTH(str_in) - LENGTH(REPLACE(str_in, searchStr_in, '')) ) / LENGTH(searchStr_in);"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "return result")
   Print #fileNo, addTab(1); "RETURN v_occurs;"
 
   Print #fileNo, addTab(0); "END"
 
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################

   printSectionHeader("Function determining the number of occurrences of a search-string in a string", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameOccurs
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "str_in", "CLOB(1M)", True, "string to search in")
   genProcParm(fileNo, "", "searchStr_in", "VARCHAR(1024)", False, "string to search for")
 
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "INTEGER"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_occurs", "INTEGER", "0")
   genVarDecl(fileNo, "v_posStart", "INTEGER", "NULL")
   genVarDecl(fileNo, "v_posSearched", "INTEGER", "NULL")
 
   genProcSectionHeader(fileNo, "count matches")
   Print #fileNo, addTab(1); "IF LENGTH(searchStr_in) > 0 THEN"
   Print #fileNo, addTab(2); "SET v_posSearched = LENGTH(str_in);"
   Print #fileNo, addTab(2); "SET v_posStart    = LOCATE(searchStr_in, str_in);"
 
   Print #fileNo,
   Print #fileNo, addTab(2); "WHILE v_posStart > 0 DO"
   Print #fileNo, addTab(3); "SET v_occurs   = v_occurs  + 1;"
   Print #fileNo, addTab(3); "SET v_posStart = LOCATE(searchStr_in, str_in, v_posStart + 1);"
 
   Print #fileNo, addTab(3); "IF v_posStart >= v_posSearched THEN"
   Print #fileNo, addTab(4); "SET v_occurs   = v_occurs  + 1;"
   Print #fileNo, addTab(4); "SET v_posStart = 0;"
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo, addTab(2); "END WHILE;"
 
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader(fileNo, "return result")
   Print #fileNo, addTab(1); "RETURN v_occurs;"
 
   Print #fileNo, addTab(0); "END"
 
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function retrieving a substring from a string based on delimiter strings
   ' ####################################################################################################################

   printSectionHeader("Function retrieving a substring from a string based on delimiter strings", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); g_qualFuncNameGetStrElem
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "str_in", "VARCHAR(1024)", True, "string to search in")
   genProcParm(fileNo, "", "beginDelim_in", "VARCHAR(20)", True, "delimiter string indicating the beginning of the string to retrieve")
   genProcParm(fileNo, "", "endDelim_in", "VARCHAR(20)", False, "delimiter string indicating the ending of the string to retrieve")
 
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); "VARCHAR(1024)"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader(fileNo, "declare variables")
   genVarDecl(fileNo, "v_str", "VARCHAR(1024)", "NULL")
   genVarDecl(fileNo, "v_pos", "INTEGER", "0")
 
   genProcSectionHeader(fileNo, "search begin delimiter")
   Print #fileNo, addTab(1); "SET v_pos = COALESCE("; qualFuncNamePosStr; "(str_in, beginDelim_in), 0);"
   Print #fileNo, addTab(1); "IF v_pos = 0 THEN"
   Print #fileNo, addTab(2); "RETURN NULL;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "ignore anything before begin delimiter including delimiter")
   Print #fileNo, addTab(1); "SET v_str = SUBSTR(str_in, v_pos + LENGTH(beginDelim_in));"
 
   genProcSectionHeader(fileNo, "search end delimiter")
   Print #fileNo, addTab(1); "SET v_pos = COALESCE("; qualFuncNamePosStr; "(v_str, endDelim_in), 0);"
   Print #fileNo, addTab(1); "IF v_pos = 0 THEN"
   Print #fileNo, addTab(2); "RETURN v_str;"
   Print #fileNo, addTab(1); "END IF;"
 
   genProcSectionHeader(fileNo, "ignore anything after end delimiter including delimiter")
   Print #fileNo, addTab(1); "RETURN SUBSTR(v_str, 1,  v_pos-1);"
   Print #fileNo, addTab(0); "END"
 
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   Dim qualFuncNameIsSubset As String
   qualFuncNameIsSubset = genQualFuncName(g_sectionIndexMeta, udfnIsSubset, ddlType, , , , , , True)
 
   printSectionHeader("Function determining whether set represented as delimiter-separated list is a subset of a second set", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameIsSubset
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "list1_in", "VARCHAR(500)", True, "delimiter-separated string-list-representation of set 1")
   genProcParm(fileNo, "", "list2_in", "VARCHAR(500)", True, "delimiter-separated string-list-representation of set 2")
   genProcParm(fileNo, "", "delimiter_in", "CHAR(1)", False, "delimiter separating elements of lists")
   Print #fileNo, addTab(0); ")"

   Print #fileNo, addTab(0); "RETURNS"
   Print #fileNo, addTab(1); g_dbtBoolean
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "READS SQL DATA"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"

   genProcSectionHeader(fileNo, "declare variables", 1, True)
   genVarDecl(fileNo, "v_isSubSet", g_dbtBoolean, gc_dbTrue)

   genProcSectionHeader(fileNo, "for each element of set 1: check if it contained in set 2")
   Print #fileNo, addTab(1); "IF EXISTS ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "SET1.elem"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "TABLE ( "; g_qualFuncNameStrElems; "(list1_in, delimiter_in) ) AS SET1"
   Print #fileNo, addTab(4); "LEFT OUTER JOIN"
   Print #fileNo, addTab(5); "TABLE ( "; g_qualFuncNameStrElems; "(list2_in, delimiter_in) ) AS SET2"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "SET1. elem = SET2.elem"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "SET1.elem <> ''"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "SET2.elem IS NULL"
   Print #fileNo, addTab(1); ") THEN"
   Print #fileNo, addTab(2); "SET v_isSubSet = "; gc_dbFalse; ";"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN v_isSubSet;"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   If generateFwkTest Then
     GoTo NormalExit
   End If

 ' ### IF IVK ###
   ' ####################################################################################################################
   ' #    SP for Setting up data pool specific data in REGISTRYSTATIC
   ' ####################################################################################################################

   Dim qualProcNameRegStaticInit As String
   qualProcNameRegStaticInit = genQualProcName(g_sectionIndexMeta, spnRegStaticInit, ddlType)

   printSectionHeader("SP for Setting up Data Pool specific data in " & g_qualTabNameRegistryStatic, fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRegStaticInit
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) organization ID")
   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "(optional) OID of the product structure")
   genProcParm(fileNo, "IN", "poolId_in", g_dbtEnumId, True, "(optional) ID of the data pool")

   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records inserted")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_rowCount", "INTEGER", "0")
   genVarDecl(fileNo, "v_orgOid", g_dbtOid, "NULL")
   genSpLogDecl(fileNo)

   genSpLogProcEnter(fileNo, qualProcNameRegStaticInit, ddlType, , "orgId_in", "psOid_in", "poolId_in", "rowCount_out")

   genProcSectionHeader(fileNo, "initialize output variables")
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genDdlForRegStaticSstUpdate(fileNo, ddlType, 1, True, "orgId_in", , "psOid_in", "poolId_in")

   genSpLogProcExit(fileNo, qualProcNameRegStaticInit, ddlType, , "orgId_in", "psOid_in", "poolId_in", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP for initializing Default Rebate
   ' ####################################################################################################################
 
   Dim qualProcNameRebateInitDefault As String
   qualProcNameRebateInitDefault = genQualProcName(g_sectionIndexMeta, spnRebateInitDefault, ddlType)

   printSectionHeader("SP for Initializing Default Rebate value", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameRebateInitDefault
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "(optional) OID of the product structure to initialize rebate value for")

   genProcParm(fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records inserted")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogDecl(fileNo, -1, True)
 
   genSpLogProcEnter(fileNo, qualProcNameRebateInitDefault, ddlType, , "psOid_in", "rowCount_out")

   genProcSectionHeader(fileNo, "initialize output variables")
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
 
   genProcSectionHeader(fileNo, "insert default values")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); g_qualTabNameRebateDefault
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "VALUETYPE,"
   Print #fileNo, addTab(2); g_anPsOid
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "(CASE WHEN PS.PDIDIV_OID = 16 THEN 20 ELSE 25 END),"
   Print #fileNo, addTab(2); "PS."; g_anOid
 
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure; " PS"
   Print #fileNo, addTab(1); "LEFT OUTER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameRebateDefault; " R"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PS."; g_anOid; " = R."; g_anPsOid
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "R."; g_anPsOid; " IS NULL"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "COALESCE(psOid_in, PS."; g_anOid; ") = PS."; g_anOid
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader(fileNo, "count the number of affected rows", 1)
   Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"
 
   genSpLogProcExit(fileNo, qualProcNameRebateInitDefault, ddlType, , "psOid_in", "rowCount_out")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    SP verifying that a default rebate value is configured for a given ProductStructure
   ' ####################################################################################################################

   Dim qualProcNameAssertRebateDefault As String
   qualProcNameAssertRebateDefault = genQualProcName(g_sectionIndexMeta, spnAssertRebateDefault, ddlType)

   printSectionHeader("SP verifying that a default rebate value is configured for a given ProductStructure", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcNameAssertRebateDefault
   Print #fileNo, addTab(0); "("

   genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the product structure verify")
   genProcParm(fileNo, "IN", "busErrMsg_in", g_dbtBoolean, False, "(optional) if '1': use business error message")

   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader(fileNo, "declare variables")
   genSigMsgVarDecl(fileNo)
   genSpLogDecl(fileNo)
 
   genSpLogProcEnter(fileNo, qualProcNameAssertRebateDefault, ddlType, , "psOid_in", "busErrMsg_in")

   genProcSectionHeader(fileNo, "verify default value")
   Print #fileNo, addTab(1); "IF NOT EXISTS (SELECT 1 FROM "; g_qualTabNameRebateDefault; " WHERE "; g_anPsOid; " = psOid_in) THEN"
   genSpLogProcEscape(fileNo, qualProcNameAssertRebateDefault, ddlType, 2, "psOid_in", "busErrMsg_in")
   Print #fileNo, addTab(2); "IF busErrMsg_in = 1 THEN"
   genSignalDdlWithParms("rebateDefNotDefBus", fileNo, 3, , , , , , , , , , "RTRIM(CHAR(psOid_in))")
   Print #fileNo, addTab(2); "ELSE"
   genSignalDdlWithParms("rebateDefNotDef", fileNo, 3, , , , , , , , , , "RTRIM(CHAR(psOid_in))")
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit(fileNo, qualProcNameAssertRebateDefault, ddlType, , "psOid_in", "busErrMsg_in")

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 ' ### ENDIF IVK ###
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 ' ### IF IVK ###
 
 Sub genDelDistNlTextProc( _
   fileNo As Integer, _
   Optional procName As String, _
   Optional schemaPrefix As String, _
   Optional tableName As String, _
   Optional fkName As String _
 )
   Print #fileNo, addTab(0)
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   'toDo remove hardcoded reference
   Print #fileNo, addTab(1); "VL6CMET."; procName
   Print #fileNo, addTab(0); "("
   Print #fileNo, addTab(1); "IN oid_in      BIGINT"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
   Print #fileNo, addTab(1); "DECLARE v_stmntTxt        VARCHAR(500)     DEFAULT NULL;"
   Print #fileNo, addTab(1); "DECLARE v_rowCount        INTEGER          DEFAULT 0;"
   Print #fileNo, addTab(1); ""
   genProcSectionHeader(fileNo, "loop over all organizations", 1)
   Print #fileNo, addTab(1); "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "'"; schemaPrefix; "' || CAST(RIGHT('00' || RTRIM(CAST(O.ID AS CHAR(2))),2) AS CHAR(2)) as c_schema"
   Print #fileNo, addTab(2); "FROM"
   'toDo remove hardcoded reference
   Print #fileNo, addTab(3); "VL6CDBM.PDMORGANIZATION_ENUM O"
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "O.ID"
   Print #fileNo, addTab(2); "FOR READ ONLY"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "SET v_stmntTxt = 'DELETE FROM ' || c_schema || '.V_"; tableName; "_LC NL WHERE NL."; fkName; " = ' || oid_in ;"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 
 End Sub
 
 Sub genDbUtilitiesDdlByPool( _
   ddlType As DdlTypeId, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 )
   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexDbMeta, processingStepUtilities, ddlType, thisOrgIndex, thisPoolIndex, , phaseDbSupport)

   On Error GoTo ErrorExit

   Dim forPool As Boolean
   forPool = (thisOrgIndex > 0) And (thisPoolIndex > 0)

   Dim countryIdListLength  As Integer
     countryIdListLength = g_domains.descriptors(g_domainIndexCountryIdList).maxLength

   Dim qualTabNameCountryIdXRef As String
   qualTabNameCountryIdXRef = genQualTabNameByRelIndex(g_relIndexCountryIdXRef, ddlType, thisOrgIndex, thisPoolIndex, , , , , True)
   Dim qualTabNameCountrySpec As String
   qualTabNameCountrySpec = genQualTabNameByClassIndex(g_classIndexCountrySpec, ddlType, thisOrgIndex, thisPoolIndex, , , , , , , True)

   Dim qualViewNamePdmTable As String
   qualViewNamePdmTable = genQualViewName(g_sectionIndexDbMeta, vnPdmTable, vnsPdmTable, ddlType)

   Dim transformation As AttributeListTransformation

   ' ####################################################################################################################
   ' #    user defined function normalizing lists of CountryIDs
   ' ####################################################################################################################

   Dim qualFuncNameNormCidList As String
   qualFuncNameNormCidList = genQualFuncName(g_sectionIndexCountry, udfnNormalizeCountryIdList, ddlType)

   If Not forPool Then
     printSectionHeader("UDF normalizing lists of CountryIDs", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE FUNCTION"
     Print #fileNo, addTab(1); qualFuncNameNormCidList
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "", "countryIdList_in", "VARCHAR(" & CStr(countryIdListLength) & ")", False, "list of CountryIDs")
     Print #fileNo, addTab(0); ")"

     Print #fileNo, addTab(0); "RETURNS"
     Print #fileNo, addTab(1); "VARCHAR("; CStr(countryIdListLength); ")"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "DETERMINISTIC"
     Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
     Print #fileNo, addTab(0); "CONTAINS SQL"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader(fileNo, "declare variables", , True)
     genVarDecl(fileNo, "v_result", "VARCHAR(" & CStr(countryIdListLength) & ")", "NULL")

     genProcSectionHeader(fileNo, "loop over list elements")
     Print #fileNo, addTab(1); "FOR countryLoop AS"
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); g_dbtEnumId; "(elem) AS countryId"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); "TABLE ("; g_qualFuncNameStrElems; "(countryIdList_in)) AS X"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "COALESCE(elem, '') <> ''"
     Print #fileNo, addTab(2); "ORDER BY"
     Print #fileNo, addTab(3); g_dbtEnumId; "(elem) ASC"
     Print #fileNo, addTab(1); "DO"
     Print #fileNo, addTab(2); "SET v_result = COALESCE(v_result || ',', '') || RIGHT(DIGITS(countryId), 3);"
     Print #fileNo, addTab(1); "END FOR;"
     Print #fileNo,
     Print #fileNo, addTab(1); "RETURN v_result;"
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If

   ' ####################################################################################################################
   ' #    user defined function maintaining table of lists of CountryIDs
   ' ####################################################################################################################

   Dim qualTabNameCountryIdList  As String
   qualTabNameCountryIdList = genQualTabNameByClassIndex(g_classIndexCountryIdList, ddlType, thisOrgIndex, thisPoolIndex, , , , , , , True)
   Dim qualFuncNameAssertCidList As String
   qualFuncNameAssertCidList = genQualFuncName(g_sectionIndexCountry, udfnAssertCountryIdList, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(-1, ddlType)
 
   printSectionHeader("UDF maintaining table of lists of CountryIDs", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncNameAssertCidList
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "countryIdList_in", "VARCHAR(" & CStr(countryIdListLength) & ")", False, "list of CountryIDs")
   Print #fileNo, addTab(0); ")"
 
   Print #fileNo, addTab(0); "RETURNS TABLE"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(1); "oid "; g_dbtOid; ","
   Print #fileNo, addTab(1); "IDLIST VARCHAR("; CStr(countryIdListLength); ")"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "MODIFIES SQL DATA"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_countryIdListOid", g_dbtOid, "NULL")
   genVarDecl(fileNo, "v_countryIdList", "VARCHAR(" & CStr(countryIdListLength) & ")", "NULL")
 
   genProcSectionHeader(fileNo, "verify that input Country-ID list is not empty")
   Print #fileNo, addTab(1); "IF countryIdList_in IS NOT NULL AND RTRIM(countryIdList_in) <> '' THEN"
   genProcSectionHeader(fileNo, "determine normalized Country-ID list", 2, True)
   Print #fileNo, addTab(2); "SET v_countryIdList = "; qualFuncNameNormCidList; "(countryIdList_in);"
 
   genProcSectionHeader(fileNo, "determine OID of Country-ID list", 2)
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "v_countryIdListOid = ("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); g_anOid
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualTabNameCountryIdList
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "IDLIST = v_countryIdList"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ";"
 
   genProcSectionHeader(fileNo, "if Country-ID list is not found create a new one", 2)
   Print #fileNo, addTab(2); "IF v_countryIdListOid IS NULL THEN"
   Print #fileNo, addTab(3); "SET v_countryIdListOid = (NEXTVAL FOR "; qualSeqNameOid; ");"
   Print #fileNo,
   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); qualTabNameCountryIdList
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); g_anOid; ","
   Print #fileNo, addTab(4); "IDLIST,"
   Print #fileNo, addTab(4); g_anVersionId
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(4); "v_countryIdListOid,"
   Print #fileNo, addTab(4); "v_countryIdList,"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "RETURN SELECT OID, IDLIST FROM "; qualTabNameCountryIdList; " WHERE OID = v_countryIdListOid;"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   Dim qualTriggerName As String

   If Not forPool Then
     ' ####################################################################################################################
     ' #    SP for Propagating EXPRESSIONs and TERMs between Data Pools
     ' ####################################################################################################################

     Dim qualProcNamePropExpr As String
     qualProcNamePropExpr = genQualProcName(g_sectionIndexMeta, spnPropExpr, ddlType)

     printSectionHeader("SP for Propagating EXPRESSIONs and TERMs between Data Pools", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNamePropExpr
     Print #fileNo, addTab(0); "("

     genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the product structure to propagate expressions and terms for")
     genProcParm(fileNo, "IN", "srcOrgId_in", g_dbtEnumId, True, "organization ID of the 'source data pool'")
     genProcParm(fileNo, "IN", "srcPoolId_in", g_dbtEnumId, True, "ID of the 'source data pool'")
     genProcParm(fileNo, "IN", "targetOrgId_in", g_dbtEnumId, True, "organization ID of the 'target data pool'")
     genProcParm(fileNo, "IN", "targetPoolId_in", g_dbtEnumId, True, "ID of the 'target data pool'")

     genProcParm(fileNo, "OUT", "numExprSuccess_out", "INTEGER", True, "number of Expressions propagated")
     genProcParm(fileNo, "OUT", "numExprFailed_out", "INTEGER", False, "number of Expressions failed to propagate (referred objects not available in target data pool)")

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare variables", , True)
     genVarDecl(fileNo, "v_srcQualTabNameExpr", "VARCHAR(100)", "NULL")
     genVarDecl(fileNo, "v_targetQualTabNameExpr", "VARCHAR(100)", "NULL")
     genVarDecl(fileNo, "v_srcQualTabNameTerm", "VARCHAR(100)", "NULL")
     genVarDecl(fileNo, "v_targetQualTabNameTerm", "VARCHAR(100)", "NULL")
     genVarDecl(fileNo, "v_recordCount", "INTEGER", "NULL")
     genVarDecl(fileNo, "v_propFailed", g_dbtBoolean, "NULL")
     genVarDecl(fileNo, "v_stmntExpr", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_stmntProp", "VARCHAR(200)", "NULL")
     genVarDecl(fileNo, "v_exprOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "SQLCODE", "INTEGER", "NULL")
     genSpLogDecl(fileNo)
 
     genProcSectionHeader(fileNo, "declare conditions")
     genCondDecl(fileNo, "foreignKeyNotFound", "23503")
     genCondDecl(fileNo, "alreadyExist", "42710")

     genProcSectionHeader(fileNo, "declare statements")
     genVarDecl(fileNo, "v_stmntExpr", "STATEMENT")

     genProcSectionHeader(fileNo, "declare cursor")
     Print #fileNo, addTab(1); "DECLARE exprCursor CURSOR FOR v_stmntExpr;"
 
     genProcSectionHeader(fileNo, "declare condition handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR foreignKeyNotFound"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "SET v_propFailed = "; gc_dbTrue; ";"
     Print #fileNo, addTab(1); "END;"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"

     genDdlForTempOids(fileNo, , True, , True)

     genSpLogProcEnter(fileNo, _
       qualProcNamePropExpr, ddlType, , "srcOrgId_in", "srcPoolId_in", "targetOrgId_in", "targetPoolId_in", _
       "numExprSuccess_out", "targetPoolId_in")

     genProcSectionHeader(fileNo, "initialize output variables")
     Print #fileNo, addTab(1); "SET numExprSuccess_out = 0;"
     Print #fileNo, addTab(1); "SET numExprFailed_out = 0;"
 
     genProcSectionHeader(fileNo, "determine qualified table names")
     Dim prefix As String, classIndex As Integer, suffix As String
     Dim i As Integer
     For i = 1 To 2
       If i = 1 Then
         suffix = "Expr"
         classIndex = g_classIndexExpression
       Else
         suffix = "Term"
         classIndex = g_classIndexTerm
       End If

       Dim j As Integer
       For j = 1 To 2
         prefix = IIf(j = 1, "src", "target")
         If (i <> 1) Or (j <> 1) Then
           Print #fileNo,
         End If
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); g_anPdmFkSchemaName; " || '.' || "; g_anPdmTypedTableName
         Print #fileNo, addTab(1); "INTO"
         Print #fileNo, addTab(2); "v_"; prefix; "QualTabName"; suffix
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualViewNamePdmTable
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "ENTITY_TYPE = '"; gc_acmEntityTypeKeyClass; "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "ENTITY_ID = '"; g_classes.descriptors(classIndex).classIdStr; "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "PDM_"; g_anOrganizationId; " = "; prefix; "OrgId_in"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "PDM_POOLTYPE_ID = "; prefix; "PoolId_in"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "LDM_ISLRT = "; gc_dbFalse
         Print #fileNo, addTab(1); "WITH UR;"
       Next j
     Next i

     genProcSectionHeader(fileNo, "statement selecting Expressions in source data pool which do not exist in target data pool")
     Print #fileNo, addTab(1); "SET v_stmntExpr ="
     Print #fileNo, addTab(3); "'SELECT ' ||"
     Print #fileNo, addTab(4); "'S."; g_anOid; " ' ||"
     Print #fileNo, addTab(3); "'FROM ' ||"
     Print #fileNo, addTab(4); "v_srcQualTabNameExpr || ' S ' ||"
     Print #fileNo, addTab(3); "'WHERE ' ||"
     Print #fileNo, addTab(4); "'"; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||"
     Print #fileNo, addTab(5); "'AND ' ||"
     Print #fileNo, addTab(4); "'ISINVALID = 0 ' ||"
     Print #fileNo, addTab(5); "'AND ' ||"
     Print #fileNo, addTab(4); "'NOT EXISTS (' ||"
     Print #fileNo, addTab(5); "'SELECT ' ||"
     Print #fileNo, addTab(6); "'1 ' ||"
     Print #fileNo, addTab(5); "'FROM ' ||"
     Print #fileNo, addTab(6); "v_targetQualTabNameExpr || ' T ' ||"
     Print #fileNo, addTab(5); "'WHERE ' ||"
     Print #fileNo, addTab(6); "'S."; g_anOid; " = T."; g_anOid; "' ||"
     Print #fileNo, addTab(4); "')'"
     Print #fileNo, addTab(1); ";"
     Print #fileNo,
     Print #fileNo, addTab(1); "PREPARE v_stmntExpr FROM v_stmntExpr;"

     genProcSectionHeader(fileNo, "loop over Expressions in source data pool not in target data pool")
     Print #fileNo, addTab(1); "OPEN exprCursor;"
     Print #fileNo, addTab(1); "FETCH exprCursor INTO v_exprOid;"
     Print #fileNo, addTab(1); "WHILE (SQLCODE = 0) DO"

     Print #fileNo, addTab(2); "SET v_propFailed = "; gc_dbFalse; ";"

     genProcSectionHeader(fileNo, "define a savepoint - in case we need to rollback", 2)
     Print #fileNo, addTab(2); "SAVEPOINT exprPropFail ON ROLLBACK RETAIN CURSORS;"

     genProcSectionHeader(fileNo, "propagate Terms corresponding to this Expression", 2)
     Print #fileNo, addTab(2); "SET v_stmntProp ="
     Print #fileNo, addTab(3); "'INSERT INTO ' ||"
     Print #fileNo, addTab(4); "v_targetQualTabNameTerm || ' ' ||"
     Print #fileNo, addTab(3); "'SELECT ' ||"
     Print #fileNo, addTab(4); "'* ' ||"
     Print #fileNo, addTab(3); "'FROM ' ||"
     Print #fileNo, addTab(4); "v_srcQualTabNameTerm || ' ' ||"
     Print #fileNo, addTab(3); "'WHERE ' ||"
     Print #fileNo, addTab(4); "'"; g_anAhOid; " = ' || CHAR(v_exprOid)"
     Print #fileNo, addTab(2); ";"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntProp;"

     genProcSectionHeader(fileNo, "propagate Expression", 2)
     Print #fileNo, addTab(2); "SET v_stmntProp ="
     Print #fileNo, addTab(3); "'INSERT INTO ' ||"
     Print #fileNo, addTab(4); "v_targetQualTabNameExpr || ' ' ||"
     Print #fileNo, addTab(3); "'SELECT ' ||"
     Print #fileNo, addTab(4); "'* ' ||"
     Print #fileNo, addTab(3); "'FROM ' ||"
     Print #fileNo, addTab(4); "v_srcQualTabNameExpr || ' ' ||"
     Print #fileNo, addTab(3); "'WHERE ' ||"
     Print #fileNo, addTab(4); "'OID = ' || CHAR(v_exprOid)"
     Print #fileNo, addTab(2); ";"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntProp;"

     genProcSectionHeader(fileNo, "in case of failure: rollback to savepoint", 2)
     Print #fileNo, addTab(2); "IF v_propFailed = 1 THEN"
     Print #fileNo, addTab(3); "ROLLBACK TO SAVEPOINT exprPropFail;"
     Print #fileNo, addTab(3); "SET numExprFailed_out = numExprFailed_out + 1;"
     Print #fileNo, addTab(2); "ELSE"
     Print #fileNo, addTab(3); "SET numExprSuccess_out = numExprSuccess_out + 1;"

     genProcSectionHeader(fileNo, "keep track of this OID - need to create ChangeLog record for it", 3)
     Print #fileNo, addTab(3); "INSERT INTO "; tempTabNameOids; " (OID) VALUES( v_exprOid );"
     Print #fileNo, addTab(2); "END IF;"
     Print #fileNo, addTab(2); "RELEASE SAVEPOINT exprPropFail;"

     genProcSectionHeader(fileNo, "retrieve next Expression", 2)
     Print #fileNo, addTab(2); "FETCH exprCursor INTO v_exprOid;"

     Print #fileNo, addTab(1); "END WHILE;"
     Print #fileNo, addTab(1); "CLOSE exprCursor WITH RELEASE;"

     genSpLogProcExit(fileNo, _
       qualProcNamePropExpr, ddlType, , "srcOrgId_in", "srcPoolId_in", "targetOrgId_in", "targetPoolId_in", _
       "numExprSuccess_out", "targetPoolId_in")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     ' ####################################################################################################################
     ' #    SP for Propagating Invalid EXPRESSIONs and TERMs between Data Pools
     ' ####################################################################################################################

     Dim qualProcNamePropInvExpr As String
     qualProcNamePropInvExpr = genQualProcName(g_sectionIndexMeta, spnPropInvExpr, ddlType)

     printSectionHeader("SP for Propagating Invalid EXPRESSIONs and TERMs between Data Pools", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNamePropInvExpr
     Print #fileNo, addTab(0); "("

     genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the product structure to propagate expressions and terms for")
     genProcParm(fileNo, "IN", "srcOrgId_in", g_dbtEnumId, True, "organization ID of the 'source data pool'")
     genProcParm(fileNo, "IN", "srcPoolId_in", g_dbtEnumId, True, "ID of the 'source data pool'")
     genProcParm(fileNo, "IN", "targetOrgId_in", g_dbtEnumId, True, "organization ID of the 'target data pool'")
     genProcParm(fileNo, "IN", "targetPoolId_in", g_dbtEnumId, True, "ID of the 'target data pool'")
     genProcParm(fileNo, "IN", "setProductiveTs_in", "TIMESTAMP", True, "marks the timestamp of setting data productive")
     genProcParm(fileNo, "OUT", "numExprSuccess_out", "INTEGER", True, "number of Expressions propagated")
     genProcParm(fileNo, "OUT", "numExprFailed_out", "INTEGER", False, "number of Expressions failed to propagate (referred objects not available in target data pool)")

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare variables", , True)
     genVarDecl(fileNo, "v_srcQualTabNameExpr", "VARCHAR(100)", "NULL")
     genVarDecl(fileNo, "v_targetQualTabNameExpr", "VARCHAR(100)", "NULL")
     genVarDecl(fileNo, "v_srcQualTabNameTerm", "VARCHAR(100)", "NULL")
     genVarDecl(fileNo, "v_targetQualTabNameTerm", "VARCHAR(100)", "NULL")
     genVarDecl(fileNo, "v_recordCount", "INTEGER", "NULL")
     genVarDecl(fileNo, "v_propFailed", g_dbtBoolean, "NULL")
     genVarDecl(fileNo, "v_stmntExpr", "VARCHAR(2000)", "NULL")
     genVarDecl(fileNo, "v_stmntProp", "VARCHAR(2000)", "NULL")
     genVarDecl(fileNo, "v_exprOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "SQLCODE", "INTEGER", "NULL")
     genSpLogDecl(fileNo)
 
     genProcSectionHeader(fileNo, "declare conditions")
     genCondDecl(fileNo, "alreadyExist", "42710")

     genProcSectionHeader(fileNo, "declare statements")
     genVarDecl(fileNo, "v_stmntExpr", "STATEMENT")

     genProcSectionHeader(fileNo, "declare cursor")
     Print #fileNo, addTab(1); "DECLARE exprCursor CURSOR FOR v_stmntExpr;"
 
     genProcSectionHeader(fileNo, "declare condition handler")
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"

     genDdlForTempInvExpOids(fileNo, , True, , True)

     genSpLogProcEnter(fileNo, _
       qualProcNamePropExpr, ddlType, , "srcOrgId_in", "srcPoolId_in", "targetOrgId_in", "targetPoolId_in", _
       "numExprSuccess_out", "targetPoolId_in")

     genProcSectionHeader(fileNo, "initialize output variables")
     Print #fileNo, addTab(1); "SET numExprSuccess_out = 0;"
     Print #fileNo, addTab(1); "SET numExprFailed_out = 0;"
 
     genProcSectionHeader(fileNo, "determine qualified table names")
     For i = 1 To 2
       If i = 1 Then
         suffix = "Expr"
         classIndex = g_classIndexExpression
       Else
         suffix = "Term"
         classIndex = g_classIndexTerm
       End If

       For j = 1 To 2
         prefix = IIf(j = 1, "src", "target")
         If (i <> 1) Or (j <> 1) Then
           Print #fileNo,
         End If
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); g_anPdmFkSchemaName; " || '.' || "; g_anPdmTypedTableName
         Print #fileNo, addTab(1); "INTO"
         Print #fileNo, addTab(2); "v_"; prefix; "QualTabName"; suffix
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualViewNamePdmTable
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "ENTITY_TYPE = '"; gc_acmEntityTypeKeyClass; "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "ENTITY_ID = '"; g_classes.descriptors(classIndex).classIdStr; "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "PDM_"; g_anOrganizationId; " = "; prefix; "OrgId_in"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "PDM_POOLTYPE_ID = "; prefix; "PoolId_in"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "LDM_ISLRT = "; gc_dbFalse
         Print #fileNo, addTab(1); "WITH UR;"
       Next j
     Next i

     'rs6
     genProcSectionHeader(fileNo, "statement inserting Expressions in target data pool which are invalid in source data pool")
     Print #fileNo, addTab(1); "SET v_stmntExpr ="
     Print #fileNo, addTab(3); "'INSERT INTO ' ||"
     Print #fileNo, addTab(4); "'SESSION.InvExpOids ' ||"
     Print #fileNo, addTab(3); "'SELECT ' ||"
     Print #fileNo, addTab(4); "'T."; g_anOid; " ' ||"
     Print #fileNo, addTab(3); "'FROM ' ||"
     Print #fileNo, addTab(4); "v_targetQualTabNameExpr || ' T ' ||"
     Print #fileNo, addTab(3); "'WHERE ' ||"
     Print #fileNo, addTab(4); "'T."; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||"
     Print #fileNo, addTab(5); "'AND ' ||"
     Print #fileNo, addTab(4); "'T.ISINVALID = 0 ' ||"
     Print #fileNo, addTab(5); "'AND ' ||"
     Print #fileNo, addTab(4); "'T."; g_anOid; " IN (' ||"
     Print #fileNo, addTab(5); "'SELECT ' ||"
     Print #fileNo, addTab(6); "'S."; g_anOid; " ' ||"
     Print #fileNo, addTab(5); "'FROM ' ||"
     Print #fileNo, addTab(6); "v_srcQualTabNameExpr || ' S ' ||"
     Print #fileNo, addTab(5); "'WHERE ' ||"
     Print #fileNo, addTab(6); "'S."; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||"
     Print #fileNo, addTab(5); "'AND ' ||"
     Print #fileNo, addTab(6); "'S.ISINVALID = 1 ' ||"
     Print #fileNo, addTab(4); "')'"
     Print #fileNo, addTab(1); ";"
     Print #fileNo,
     Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntExpr;"
     Print #fileNo,
     Print #fileNo, addTab(1); "SET numExprSuccess_out = (SELECT count(oid) FROM SESSION.InvExpOids);"

     genProcSectionHeader(fileNo, "update Expressions in target data pool")
     Print #fileNo, addTab(1); "SET v_stmntProp ="
     Print #fileNo, addTab(2); "'UPDATE ' ||"
     Print #fileNo, addTab(3); "v_targetQualTabNameExpr || ' T ' ||"
     Print #fileNo, addTab(2); "'SET ' ||"
     Print #fileNo, addTab(3); "'(ISINVALID, EXTTRM_OID, UPDATEUSER, LASTUPDATETIMESTAMP, VERSIONID) ' ||"
     Print #fileNo, addTab(4); "'= ' ||"
     Print #fileNo, addTab(3); "'( SELECT ' ||"
     Print #fileNo, addTab(5); "'S.ISINVALID, ' ||"
     Print #fileNo, addTab(5); "'NULL AS EXTTRM_OID, ' ||"
     Print #fileNo, addTab(5); "'S.UPDATEUSER, ' ||"
     Print #fileNo, addTab(5); "'''' || setProductiveTs_in || ''', ' ||"
     Print #fileNo, addTab(5); "'S.VERSIONID ' ||"
     Print #fileNo, addTab(4); "'FROM ' ||"
     Print #fileNo, addTab(5); "v_srcQualTabNameExpr || ' S ' ||"
     Print #fileNo, addTab(4); "'WHERE ' ||"
     Print #fileNo, addTab(5); "'S."; g_anOid; " = T."; g_anOid; " ' ||"
     Print #fileNo, addTab(6); "'AND ' ||"
     Print #fileNo, addTab(5); "'S."; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||"
     Print #fileNo, addTab(3); "')' ||"
     Print #fileNo, addTab(2); "'WHERE ' ||"
     Print #fileNo, addTab(3); "'T."; g_anOid; " IN (SELECT oid FROM SESSION.InvExpOids) ' ||"
     Print #fileNo, addTab(4); "'AND ' ||"
     Print #fileNo, addTab(3); "'T."; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) || ' '"
     Print #fileNo, addTab(1); ";"
     Print #fileNo,
     Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntProp;"

     genProcSectionHeader(fileNo, "delete Terms corresponding to this Expression")
     Print #fileNo, addTab(1); "SET v_stmntProp ="
     Print #fileNo, addTab(2); "'DELETE ' ||"
     Print #fileNo, addTab(3); "v_targetQualTabNameTerm || ' ' ||"
     Print #fileNo, addTab(2); "'WHERE ' ||"
     Print #fileNo, addTab(3); "'"; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||"
     Print #fileNo, addTab(4); "'AND ' ||"
     Print #fileNo, addTab(3); "'"; g_anAhOid; " IN (SELECT oid FROM SESSION.InvExpOids) '"
     Print #fileNo, addTab(1); ";"
     Print #fileNo,
     Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntProp;"

     genProcSectionHeader(fileNo, "update Expressions in source data pool")
     Print #fileNo, addTab(1); "SET v_stmntProp ="
     Print #fileNo, addTab(2); "'UPDATE ' ||"
     Print #fileNo, addTab(3); "v_srcQualTabNameExpr || ' T ' ||"
     Print #fileNo, addTab(2); "'SET ' ||"
     Print #fileNo, addTab(3); "'EXTTRM_OID = NULL ' ||"
     Print #fileNo, addTab(2); "'WHERE ' ||"
     Print #fileNo, addTab(3); "'"; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||"
     Print #fileNo, addTab(4); "'AND ' ||"
     Print #fileNo, addTab(3); "'"; g_anOid; " IN (SELECT oid FROM SESSION.InvExpOids) '"
     Print #fileNo, addTab(1); ";"
     Print #fileNo,
     Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntProp;"

     genProcSectionHeader(fileNo, "delete Terms in source data pool corresponding to this Expression")
     Print #fileNo, addTab(1); "SET v_stmntProp ="
     Print #fileNo, addTab(2); "'DELETE ' ||"
     Print #fileNo, addTab(3); "v_srcQualTabNameTerm || ' ' ||"
     Print #fileNo, addTab(2); "'WHERE ' ||"
     Print #fileNo, addTab(3); "'"; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) || ' ' ||"
     Print #fileNo, addTab(4); "'AND ' ||"
     Print #fileNo, addTab(3); "'"; g_anAhOid; " IN (SELECT oid FROM SESSION.InvExpOids) '"
     Print #fileNo, addTab(1); ";"
     Print #fileNo,
     Print #fileNo, addTab(1); "EXECUTE IMMEDIATE v_stmntProp;"

     genSpLogProcExit(fileNo, _
       qualProcNamePropExpr, ddlType, , "srcOrgId_in", "srcPoolId_in", "targetOrgId_in", "targetPoolId_in", _
       "numExprSuccess_out", "targetPoolId_in")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     ' ####################################################################################################################
     ' #    SP for Broadcasting Changelog-Records
     ' ####################################################################################################################

     Dim qualProcNameClBroadCast As String
     qualProcNameClBroadCast = genQualProcName(g_sectionIndexChangeLog, spnClBroadcast, ddlType)

     printSectionHeader("SP for Broadcasting Changelog-Records (only for public 'insert' of non-NL & non-GEN records)", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameClBroadCast
     Print #fileNo, addTab(0); "("

     genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to broadcast to")
     genProcParm(fileNo, "IN", "poolId_in", g_dbtEnumId, True, "(optional) ID of the pool to broadcast to")

     genProcSectionHeader(fileNo, "the following input parameters refer to columns of the changelog record to be broadcasted", , True)

     genProcParm(fileNo, "IN", "entityId_in", g_dbtEntityId, True)
     genProcParm(fileNo, "IN", "entityType_in", g_dbtEntityType, True)
     genProcParm(fileNo, "IN", "ahClassId_in", g_dbtEntityId, True)
     genProcParm(fileNo, "IN", "ahObjectId_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "nl_in", g_dbtBoolean, True)
     genProcParm(fileNo, "IN", "dbTableName_in", g_dbtDbTableName, True)
     genProcParm(fileNo, "IN", "dbColumnName_in", g_dbtDbColumnName, True)
     genProcParm(fileNo, "IN", "objectId_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "valueTypeId_in", g_dbtInteger, True)
     genProcParm(fileNo, "IN", "oldValueBigInt_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "newValueBigInt_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "oldValueString_in", g_dbtChangeLogString, True)
     genProcParm(fileNo, "IN", "newValueString_in", g_dbtChangeLogString, True)
     genProcParm(fileNo, "IN", "oldValueInteger_in", g_dbtInteger, True)
     genProcParm(fileNo, "IN", "newValueInteger_in", g_dbtInteger, True)
     genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True)
     genProcParm(fileNo, "IN", "divisionOid_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "operationId_in", g_dbtEnumId, True)
     genProcParm(fileNo, "IN", "opTimestamp_in", "TIMESTAMP", True)

     genProcParm(fileNo, "OUT", "changeLogCount_out", "INTEGER", False, "number of changelog tables the message was broadcasted to")

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "declare variables", , True)
     genVarDecl(fileNo, "v_clRecordOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_stmntTxt", "VARCHAR(10000)", "NULL")
     genVarDecl(fileNo, "v_opTimestamp", "TIMESTAMP", "NULL")
     genVarDecl(fileNo, "v_rowCount", "INTEGER", "NULL")
     genSpLogDecl(fileNo)

     genSpLogProcEnter(fileNo, _
       qualProcNameClBroadCast, ddlType, , "orgId_in", "poolId_in", "'entityId_in", "'entityType_in", "'ahClassId_in", _
       "ahObjectId_in", "'dbTableName_in", "objectId_in", "'cdUserId_in", "divisionOid_in", "psOid_in")

     genProcSectionHeader(fileNo, "initialize output variables")
     Print #fileNo, addTab(1); "SET changeLogCount_out = 0;"

     genProcSectionHeader(fileNo, "initialize variables")
     Print #fileNo, addTab(1); "SET v_opTimestamp = COALESCE(opTimestamp_in, CURRENT TIMESTAMP);"

     genProcSectionHeader(fileNo, "determine OID of Changelog Record")
     Print #fileNo, addTab(1); "SET v_clRecordOid = NEXTVAL FOR "; qualSeqNameOid; ";"
 
     genProcSectionHeader(fileNo, "loop over ChangeLog-Tables in data pools to create Changelog-Record")
     Print #fileNo, addTab(1); "FOR tabLoop AS"

     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
     Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_clTableName,"
     Print #fileNo, addTab(3); "P2."; g_anPdmTableName; " AS c_clsTableName"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
     Print #fileNo, addTab(2); "LEFT OUTER JOIN"
     Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A2"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "A2."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A2."; g_anAcmEntityId; " IN ('"; getClassIdStrByIndex(g_classIndexChangeLogStatus); "')"
     Print #fileNo, addTab(2); "LEFT OUTER JOIN"
     Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L2"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "A2."; g_anAcmEntityType; " = L2."; g_anAcmEntityType
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A2."; g_anAcmEntityName; " = L2."; g_anAcmEntityName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A2."; g_anAcmEntitySection; " = L2."; g_anAcmEntitySection
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L2."; g_anLdmIsNl; " = "; gc_dbFalse
     Print #fileNo, addTab(2); "LEFT OUTER JOIN"
     Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P2"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "P2."; g_anPdmLdmFkSchemaName; " = L2."; g_anLdmSchemaName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P2."; g_anPdmLdmFkTableName; " = L2."; g_anLdmTableName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P2."; g_anPoolTypeId; " = P."; g_anPoolTypeId
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P2."; g_anOrganizationId; " = P."; g_anOrganizationId
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityId; " IN ('"; getClassIdStrByIndex(g_classIndexChangeLog); "')"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " NOT IN ("; CStr(g_migDataPoolId); ", "; CStr(g_archiveDataPoolId); ")"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "((poolId_in IS NULL) OR (P."; g_anPoolTypeId; " = poolId_in) OR ((COALESCE(poolId_in,0) < 0) AND (P."; g_anPoolTypeId; " <> -poolId_in)))"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "((orgId_in IS NULL) OR (P."; g_anOrganizationId; " = orgId_in) OR ((COALESCE(orgId_in,0) < 0) AND (P."; g_anOrganizationId; " <> -orgId_in)))"

     Print #fileNo, addTab(1); "DO"

     Print #fileNo, addTab(2); "SET v_stmntTxt ="
     Print #fileNo, addTab(4); "'INSERT INTO ' ||"
     Print #fileNo, addTab(5); "c_schemaName || '.' || c_clTableName ||"
     Print #fileNo, addTab(4); "'(' ||"

     Print #fileNo, addTab(5); "'OID,' ||"
     Print #fileNo, addTab(5); "'"; g_anAcmEntityId; ",' ||"
     Print #fileNo, addTab(5); "'"; g_anAcmEntityType; ",' ||"
     Print #fileNo, addTab(5); "'"; g_anAhCid; ",' ||"
     Print #fileNo, addTab(5); "'AHOBJECTID,' ||"
     Print #fileNo, addTab(5); "'NL,' ||"
     Print #fileNo, addTab(5); "'DBTABLENAME,' ||"
     Print #fileNo, addTab(5); "'DBCOLUMNNAME,' ||"
     Print #fileNo, addTab(5); "'DIVISIONOID,' ||"
     Print #fileNo, addTab(5); "'OBJECTID,' ||"
     Print #fileNo, addTab(5); "'VALUETYPE_ID,' ||"
     Print #fileNo, addTab(5); "'OLDVALUEBIGINT,' ||"
     Print #fileNo, addTab(5); "'NEWVALUEBIGINT,' ||"
     Print #fileNo, addTab(5); "'OLDVALUESTRING,' ||"
     Print #fileNo, addTab(5); "'NEWVALUESTRING,' ||"
     Print #fileNo, addTab(5); "'OLDVALUEINTEGER,' ||"
     Print #fileNo, addTab(5); "'NEWVALUEINTEGER,' ||"
     Print #fileNo, addTab(5); "'OPERATION_ID,' ||"
     Print #fileNo, addTab(5); "'OPTIMESTAMP,' ||"
     Print #fileNo, addTab(5); "'"; g_anUserId; ",' ||"
     Print #fileNo, addTab(5); "'"; g_anPsOid; "' ||"

     Print #fileNo, addTab(4); "')' ||"
     Print #fileNo, addTab(4); "'VALUES' ||"
     Print #fileNo, addTab(4); "'(' ||"

     Print #fileNo, addTab(5); "RTRIM(CHAR(v_clRecordOid)) || ',' ||"
     Print #fileNo, addTab(5); "'''' || entityId_in || ''',' ||"
     Print #fileNo, addTab(5); "'''' || entityType_in || ''',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN ahClassId_in IS NULL THEN 'NULL' ELSE '''' || ahClassId_in || '''' END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN ahObjectId_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(ahObjectId_in)) END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN nl_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(nl_in)) END) || ',' ||"
     Print #fileNo, addTab(5); "'''' || dbTableName_in || ''',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN dbColumnName_in IS NULL THEN 'NULL' ELSE '''' || dbColumnName_in || '''' END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN divisionOid_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(divisionOid_in)) END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN objectId_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(objectId_in)) END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN valueTypeId_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(valueTypeId_in)) END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN oldValueBigInt_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(oldValueBigInt_in)) END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN newValueBigInt_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(newValueBigInt_in)) END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN oldValueString_in IS NULL THEN 'NULL' ELSE '''' || oldValueString_in || '''' END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN newValueString_in IS NULL THEN 'NULL' ELSE '''' || newValueString_in || '''' END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN oldValueInteger_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(oldValueInteger_in)) END) || ',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN newValueInteger_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(newValueInteger_in)) END) || ',' ||"
     Print #fileNo, addTab(5); "RTRIM(CHAR(operationId_in)) || ',' ||"
     Print #fileNo, addTab(5); "'TIMESTAMP(''' || RTRIM(CHAR(v_opTimestamp)) || '''),' ||"
     Print #fileNo, addTab(5); "'''' || COALESCE(cdUserId_in, 'NN') || ''',' ||"
     Print #fileNo, addTab(5); "(CASE WHEN psOid_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(psOid_in)) END) ||"
 
     Print #fileNo, addTab(4); "')';"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE IMMEDIATE v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(2); "SET changeLogCount_out = changeLogCount_out + 1;"

     Print #fileNo,
     Print #fileNo, addTab(2); "IF c_clsTableName IS NOT NULL THEN"

     Print #fileNo, addTab(3); "SET v_stmntTxt ="
     Print #fileNo, addTab(5); "'UPDATE ' ||"
     Print #fileNo, addTab(6); "c_schemaName || '.' || c_clsTableName || ' ' ||"
     Print #fileNo, addTab(5); "'SET ' ||"
     Print #fileNo, addTab(6); "'LASTCOMMITTIME = TIMESTAMP(''' || RTRIM(CHAR(v_opTimestamp)) || ''') ' ||"
     Print #fileNo, addTab(5); "'WHERE ' ||"
     Print #fileNo, addTab(6); "(CASE WHEN psOid_in IS NULL THEN '"; g_anPsOid; " IS NULL' ELSE '"; g_anPsOid; " = ' || RTRIM(CHAR(psOid_in)) END) ||"
     Print #fileNo, addTab(7); "' AND ' ||"
     Print #fileNo, addTab(6); "(CASE WHEN divisionOid_in IS NULL THEN 'DIVISIONOID IS NULL' ELSE 'DIVISIONOID = ' || RTRIM(CHAR(divisionOid_in)) END) ||"
     Print #fileNo, addTab(7); "' AND ' ||"
     Print #fileNo, addTab(6); "'WITHLRTCONTEXT = 0'"
     Print #fileNo, addTab(3); ";"
     Print #fileNo,
     Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
 
     Print #fileNo,
     Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo,
     Print #fileNo, addTab(3); "IF v_rowCount = 0 THEN"
 
     Print #fileNo, addTab(4); "SET v_stmntTxt ="
     Print #fileNo, addTab(6); "'INSERT INTO ' ||"
     Print #fileNo, addTab(7); "c_schemaName || '.' || c_clsTableName ||"
     Print #fileNo, addTab(6); "'(' ||"
     Print #fileNo, addTab(7); "'"; g_anPsOid; ",' ||"
     Print #fileNo, addTab(7); "'DIVISIONOID,' ||"
     Print #fileNo, addTab(7); "'LASTCOMMITTIME,' ||"
     Print #fileNo, addTab(7); "'WITHLRTCONTEXT' ||"
     Print #fileNo, addTab(6); "') VALUES (' ||"
     Print #fileNo, addTab(7); "(CASE WHEN psOid_in       IS NULL THEN 'NULL' ELSE RTRIM(CHAR(psOid_in      )) END) || ',' ||"
     Print #fileNo, addTab(7); "(CASE WHEN divisionOid_in IS NULL THEN 'NULL' ELSE RTRIM(CHAR(divisionOid_in)) END) || ',' ||"
     Print #fileNo, addTab(7); "'TIMESTAMP(''' || RTRIM(CHAR(v_opTimestamp)) || '''),' ||"
     Print #fileNo, addTab(7); "'0' ||"
     Print #fileNo, addTab(6); "')'"
     Print #fileNo, addTab(4); ";"
     Print #fileNo,
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntTxt;"
 
     Print #fileNo, addTab(3); "END IF;"
     Print #fileNo, addTab(2); "END IF;"

     Print #fileNo, addTab(1); "END FOR;"

     genSpLogProcExit(fileNo, _
       qualProcNameClBroadCast, ddlType, , "orgId_in", "poolId_in", "'entityId_in", "'entityType_in", "'ahClassId_in", _
       "ahObjectId_in", "'dbTableName_in", "objectId_in", "'cdUserId_in", "divisionOid_in", "psOid_in")

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim


     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameClBroadCast
     Print #fileNo, addTab(0); "("

     genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to broadcast to")
     genProcParm(fileNo, "IN", "poolId_in", g_dbtEnumId, True, "(optional) ID of the pool to broadcast to")

     genProcSectionHeader(fileNo, "the following input parameters refer to columns of the changelog record to be broadcasted", , True)

     genProcParm(fileNo, "IN", "entityId_in", g_dbtEntityId, True)
     genProcParm(fileNo, "IN", "entityType_in", g_dbtEntityType, True)
     genProcParm(fileNo, "IN", "ahClassId_in", g_dbtEntityId, True)
     genProcParm(fileNo, "IN", "ahObjectId_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "nl_in", g_dbtBoolean, True)
     genProcParm(fileNo, "IN", "dbTableName_in", g_dbtDbTableName, True)
     genProcParm(fileNo, "IN", "dbColumnName_in", g_dbtDbColumnName, True)
     genProcParm(fileNo, "IN", "objectId_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "valueTypeId_in", g_dbtInteger, True)
     genProcParm(fileNo, "IN", "oldValueBigInt_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "newValueBigInt_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True)
     genProcParm(fileNo, "IN", "divisionOid_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "operationId_in", g_dbtEnumId, True)
     genProcParm(fileNo, "IN", "opTimestamp_in", "TIMESTAMP", True)

     genProcParm(fileNo, "OUT", "changeLogCount_out", "INTEGER", False, "number of changelog tables the message was broadcasted to")

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
     Print #fileNo,

     Print #fileNo, addTab(0); "CALL "; qualProcNameClBroadCast; "("
     Print #fileNo, addTab(1); "orgId_in,"
     Print #fileNo, addTab(1); "poolId_in,"
     Print #fileNo, addTab(1); "entityId_in,"
     Print #fileNo, addTab(1); "entityType_in,"
     Print #fileNo, addTab(1); "ahClassId_in,"
     Print #fileNo, addTab(1); "ahObjectId_in,"
     Print #fileNo, addTab(1); "nl_in,"
     Print #fileNo, addTab(1); "dbTableName_in,"
     Print #fileNo, addTab(1); "dbColumnName_in,"
     Print #fileNo, addTab(1); "objectId_in,"
     Print #fileNo, addTab(1); "valueTypeId_in,"
     Print #fileNo, addTab(1); "oldValueBigInt_in,"
     Print #fileNo, addTab(1); "newValueBigInt_in,"
     Print #fileNo, addTab(1); "null,"
     Print #fileNo, addTab(1); "null,"
     Print #fileNo, addTab(1); "null,"
     Print #fileNo, addTab(1); "null,"
     Print #fileNo, addTab(1); "cdUserId_in,"
     Print #fileNo, addTab(1); "divisionOid_in,"
     Print #fileNo, addTab(1); "psOid_in,"
     Print #fileNo, addTab(1); "operationId_in,"
     Print #fileNo, addTab(1); "opTimestamp_in,"
     Print #fileNo, addTab(1); "changeLogCount_out);"
     Print #fileNo,

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameClBroadCast
     Print #fileNo, addTab(0); "("

     genProcParm(fileNo, "IN", "orgId_in", g_dbtEnumId, True, "(optional) ID of the organization to broadcast to")
     genProcParm(fileNo, "IN", "poolId_in", g_dbtEnumId, True, "(optional) ID of the pool to broadcast to")

     genProcSectionHeader(fileNo, "the following input parameters refer to columns of the changelog record to be broadcasted", , True)

     genProcParm(fileNo, "IN", "entityId_in", g_dbtEntityId, True)
     genProcParm(fileNo, "IN", "entityType_in", g_dbtEntityType, True)
     genProcParm(fileNo, "IN", "ahClassId_in", g_dbtEntityId, True)
     genProcParm(fileNo, "IN", "ahObjectId_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "dbTableName_in", g_dbtDbTableName, True)
     genProcParm(fileNo, "IN", "objectId_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "cdUserId_in", g_dbtUserId, True)
     genProcParm(fileNo, "IN", "divisionOid_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "psOid_in", g_dbtOid, True)
     genProcParm(fileNo, "IN", "operationId_in", g_dbtEnumId, True)
     genProcParm(fileNo, "IN", "opTimestamp_in", "TIMESTAMP", True)

     genProcParm(fileNo, "OUT", "changeLogCount_out", "INTEGER", False, "number of changelog tables the message was broadcasted to")

     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
     Print #fileNo,

     Print #fileNo, addTab(0); "CALL "; qualProcNameClBroadCast; "("
     Print #fileNo, addTab(1); "orgId_in,"
     Print #fileNo, addTab(1); "poolId_in,"
     Print #fileNo, addTab(1); "entityId_in,"
     Print #fileNo, addTab(1); "entityType_in,"
     Print #fileNo, addTab(1); "ahClassId_in,"
     Print #fileNo, addTab(1); "ahObjectId_in,"
     Print #fileNo, addTab(1); "0,"
     Print #fileNo, addTab(1); "dbTableName_in,"
     Print #fileNo, addTab(1); "null,"
     Print #fileNo, addTab(1); "objectId_in,"
     Print #fileNo, addTab(1); "null,"
     Print #fileNo, addTab(1); "null,"
     Print #fileNo, addTab(1); "null,"
     Print #fileNo, addTab(1); "cdUserId_in,"
     Print #fileNo, addTab(1); "divisionOid_in,"
     Print #fileNo, addTab(1); "psOid_in,"
     Print #fileNo, addTab(1); "operationId_in,"
     Print #fileNo, addTab(1); "opTimestamp_in,"
     Print #fileNo, addTab(1); "changeLogCount_out);"
     Print #fileNo,

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     'toDo remove hardcoded reference
     genDelDistNlTextProc(fileNo, "DELAGGNODEDISTNLTEXT", "VL6CMET", "AGGREGATIONNODE_DIST_NL_TEXT", "ANLANO_OID")
     genDelDistNlTextProc(fileNo, "DELENDNODEDISTNLTEXT", "VL6CMET", "ENDNODE_DIST_NL_TEXT", "ENLENO_OID")
     genDelDistNlTextProc(fileNo, "DELGROUPDISTNLTEXT", "VL6CMET", "GROUP_DIST_NL_TEXT", "GNLGRP_OID")

     Dim qualProcNameGetGroupElementsGlobal As String
     qualProcNameGetGroupElementsGlobal = genQualProcName(g_sectionIndexMeta, spnGetGroupElements, ddlType)

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameGetGroupElementsGlobal
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "IN languageId_in           INTEGER,"
     Print #fileNo, addTab(1); "IN fallbackLanguageId_in   INTEGER,"
     Print #fileNo, addTab(1); "IN classId_in              VARCHAR(5),"
     Print #fileNo, addTab(1); "IN groupElementOid_in      BIGINT"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 1"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
     Print #fileNo, addTab(1); "DECLARE v_stmntTxt        VARCHAR(500)     DEFAULT NULL;"
     Print #fileNo, addTab(1); "DECLARE v_stmntMerge      VARCHAR(2000)    DEFAULT NULL;"
     Print #fileNo, addTab(1); "DECLARE v_restmntTxt      VARCHAR(200)     DEFAULT NULL;"
     Print #fileNo, addTab(1); "DECLARE v_rowCount        INTEGER          DEFAULT 0;"
     Print #fileNo, addTab(1); "DECLARE v_level           INTEGER          DEFAULT 0;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); "DECLARE c_groupId          VARCHAR(5)     DEFAULT '11022';"
     Print #fileNo, addTab(1); "DECLARE c_aggNodeId        VARCHAR(5)     DEFAULT '11023';"
     Print #fileNo, addTab(1); "DECLARE c_endNodeId        VARCHAR(5)     DEFAULT '11024';"
     Print #fileNo, addTab(1); "DECLARE c_enHasGcId        VARCHAR(5)     DEFAULT '05011';"
     Print #fileNo, addTab(1); "DECLARE c_enHasSrId        VARCHAR(5)     DEFAULT '09147';"
     Print #fileNo, addTab(1); "DECLARE c_enHasPtId        VARCHAR(5)     DEFAULT '04035';"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); "-- declare statement"
     Print #fileNo, addTab(1); "DECLARE v_stmnt                   STATEMENT;"
     Print #fileNo, addTab(1); "DECLARE v_restmnt                 STATEMENT;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); "-- declare cursor"
     Print #fileNo, addTab(1); "DECLARE c_return CURSOR WITH RETURN FOR v_restmnt;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); "-- temporary table for GroupElements"
     Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(2); "SESSION.GroupElements"
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "oid         BIGINT,"
     Print #fileNo, addTab(2); "classid     VARCHAR(5),"
     Print #fileNo, addTab(2); "divOid      BIGINT,"
     Print #fileNo, addTab(2); "psOid       BIGINT,"
     Print #fileNo, addTab(2); "orgOid      BIGINT,"
     Print #fileNo, addTab(2); "accModeId   INTEGER,"
     Print #fileNo, addTab(2); "entity      VARCHAR(250)"
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "NOT LOGGED"
     Print #fileNo, addTab(1); "WITH REPLACE;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); ""
     Print #fileNo, addTab(1); "IF classId_in = c_groupId THEN"
     Print #fileNo, addTab(2); "MERGE INTO"
     Print #fileNo, addTab(3); "SESSION.GroupElements T"
     Print #fileNo, addTab(2); "USING (SELECT AN.OID, c_aggNodeId AS CLASSID, AVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID"
     Print #fileNo, addTab(5); " FROM VL6CMET.AGGREGATIONNODE AN"
     Print #fileNo, addTab(5); " WHERE ANGGRP_OID = groupElementOid_in "
     Print #fileNo, addTab(5); ") S"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "T.OID = S.OID"
     Print #fileNo, addTab(2); "WHEN NOT MATCHED THEN"
     Print #fileNo, addTab(3); "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)"
     Print #fileNo, addTab(3); "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)"
     Print #fileNo, addTab(2); "ELSE IGNORE;"
     Print #fileNo, addTab(2); "SELECT MAXNUMBEROFLEVELS - 2 INTO v_level"
     Print #fileNo, addTab(2); "FROM VL6CMET.GROUP"
     Print #fileNo, addTab(2); "WHERE OID = groupElementOid_in;"
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo, addTab(1); ""
     Print #fileNo, addTab(1); "IF classId_in = c_aggNodeId THEN"
     Print #fileNo, addTab(2); "SELECT MAXNUMBEROFLEVELS - 2 INTO v_level"
     Print #fileNo, addTab(2); "FROM VL6CMET.GROUP G"
     Print #fileNo, addTab(2); "JOIN VL6CMET.AGGREGATIONNODE AN ON G.OID = AN.ANGGRP_OID"
     Print #fileNo, addTab(2); "WHERE AN.OID = groupElementOid_in;"
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); "IF classId_in <= c_aggNodeId THEN"
     Print #fileNo, addTab(2); "MERGE INTO"
     Print #fileNo, addTab(3); "SESSION.GroupElements T"
     Print #fileNo, addTab(2); "USING (SELECT AN.OID, c_aggNodeId AS CLASSID, AVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID"
     Print #fileNo, addTab(5); " FROM VL6CMET.AGGREGATIONNODE AN"
     Print #fileNo, addTab(5); " WHERE ANPANO_OID = groupElementOid_in "
     Print #fileNo, addTab(5); ") S"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "T.OID = S.OID"
     Print #fileNo, addTab(2); "WHEN NOT MATCHED THEN"
     Print #fileNo, addTab(3); "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)"
     Print #fileNo, addTab(3); "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)"
     Print #fileNo, addTab(2); "ELSE IGNORE;"
     Print #fileNo, addTab(2); ""
     Print #fileNo, addTab(2); "WHILE v_level > 0 DO"
     Print #fileNo, addTab(3); "MERGE INTO"
     Print #fileNo, addTab(4); "SESSION.GroupElements T"
     Print #fileNo, addTab(3); "USING (SELECT AN.OID, c_aggNodeId AS CLASSID, AVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID"
     Print #fileNo, addTab(6); " FROM SESSION.GroupElements GE"
     Print #fileNo, addTab(6); " JOIN VL6CMET.AGGREGATIONNODE AN ON AN.ANPANO_OID = GE.OID"
     Print #fileNo, addTab(6); " WHERE GE.CLASSID = c_aggNodeId"
     Print #fileNo, addTab(6); ") S"
     Print #fileNo, addTab(3); "ON"
     Print #fileNo, addTab(4); "T.OID = S.OID"
     Print #fileNo, addTab(3); "WHEN NOT MATCHED THEN"
     Print #fileNo, addTab(4); "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)"
     Print #fileNo, addTab(4); "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)"
     Print #fileNo, addTab(3); "ELSE IGNORE;"
     Print #fileNo, addTab(3); "SET v_level = v_level - 1;"
     Print #fileNo, addTab(2); "END WHILE;"
     Print #fileNo, addTab(2); ""
     Print #fileNo, addTab(2); "MERGE INTO"
     Print #fileNo, addTab(3); "SESSION.GroupElements T"
     Print #fileNo, addTab(2); "USING (SELECT EN.OID, c_endNodeId AS CLASSID, EVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID"
     Print #fileNo, addTab(5); " FROM VL6CMET.ENDNODE EN "
     Print #fileNo, addTab(5); " WHERE EN.ENPANO_OID = groupElementOid_in"
     Print #fileNo, addTab(5); ") S"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "T.OID = S.OID"
     Print #fileNo, addTab(2); "WHEN NOT MATCHED THEN"
     Print #fileNo, addTab(3); "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)"
     Print #fileNo, addTab(3); "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)"
     Print #fileNo, addTab(2); "ELSE IGNORE;"
     Print #fileNo, addTab(2); ""
     Print #fileNo, addTab(2); "MERGE INTO"
     Print #fileNo, addTab(3); "SESSION.GroupElements T"
     Print #fileNo, addTab(2); "USING (SELECT EN.OID, c_endNodeId AS CLASSID, EVDDIV_OID AS DIV_OID, NULL AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID"
     Print #fileNo, addTab(5); " FROM SESSION.GroupElements GE JOIN VL6CMET.ENDNODE EN "
     Print #fileNo, addTab(5); " ON EN.ENPANO_OID = GE.OID AND GE.CLASSID = c_aggNodeId"
     Print #fileNo, addTab(5); ") S"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "T.OID = S.OID"
     Print #fileNo, addTab(2); "WHEN NOT MATCHED THEN"
     Print #fileNo, addTab(3); "INSERT (oid, classid, divOid, psOid, orgOid, accModeId)"
     Print #fileNo, addTab(3); "VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID)"
     Print #fileNo, addTab(2); "ELSE IGNORE;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(2); "-- loop over all organizations"
     Print #fileNo, addTab(3); "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR"
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "'VL6CAL' || CAST(RIGHT('00' || RTRIM(CAST(O.ID AS CHAR(2))),2) AS CHAR(2)) || D.ID as c_schema,"
     Print #fileNo, addTab(5); "O.ORGOID AS c_org_id,"
     Print #fileNo, addTab(5); "D.ID AS c_acc_id"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); "VL6CDBM.PDMORGANIZATION_ENUM O JOIN VL6CDBM.PDMDATAPOOLTYPE_ENUM D ON D.ID > 0"
     Print #fileNo, addTab(4); "ORDER BY"
     Print #fileNo, addTab(5); "O.ID, D.ID"
     Print #fileNo, addTab(4); "FOR READ ONLY"
     Print #fileNo, addTab(3); "DO"
     Print #fileNo, addTab(4); "IF c_acc_id <= 3 THEN"
     Print #fileNo, addTab(5); "SET v_stmntMerge = 'MERGE INTO ';"
     Print #fileNo, addTab(6); "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasGcId || ''' AS CLASSID, GC.CDIDIV_OID AS DIV_OID, NULL AS PS_OID, ' || c_org_id || ' AS ORG_OID, ' || c_acc_id || ' AS ACC_ID ';"
     Print #fileNo, addTab(8); " SET v_stmntMerge = v_stmntMerge || 'FROM SESSION.GroupElements GE ';"
     Print #fileNo, addTab(8); " SET v_stmntMerge = v_stmntMerge || 'JOIN ' || c_schema || '.ENDNODEHASGENERICCODE EN ON EN.ENO_OID = GE.OID ';"
     Print #fileNo, addTab(8); " SET v_stmntMerge = v_stmntMerge || 'JOIN ' || c_schema || '.GENERICCODE GC ON GC.OID = EN.GCO_OID ';"
     Print #fileNo, addTab(8); " SET v_stmntMerge = v_stmntMerge || 'WHERE GE.CLASSID = ''' || c_endNodeId || '''';"
     Print #fileNo, addTab(8); "SET v_stmntMerge = v_stmntMerge || ') S ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'ON ';"
     Print #fileNo, addTab(6); "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID AND T.orgOid = S.ORG_OID AND T.accModeId = S.ACC_ID ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';"
     Print #fileNo, addTab(6); "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';"
     Print #fileNo, addTab(6); "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';"
     Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntMerge;"
     Print #fileNo, addTab(4); "END IF;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(4); "SET v_stmntMerge = 'MERGE INTO ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasSrId || ''' AS CLASSID, NULL AS DIV_OID, EN.PS_OID AS PS_OID, ' || c_org_id || ' AS ORG_OID, ' || c_acc_id || ' AS ACC_ID ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'FROM SESSION.GroupElements GE ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'JOIN ' || c_schema || '.ENDNODEHASNSR1VALIDITY EN ON EN.ENO_OID = GE.OID ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'WHERE GE.CLASSID = ''' || c_endNodeId || '''';"
     Print #fileNo, addTab(7); "SET v_stmntMerge = v_stmntMerge || ') S ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'ON ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID AND T.orgOid = S.ORG_OID AND T.accModeId = S.ACC_ID ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntMerge;"
     Print #fileNo, addTab(3); "END FOR;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(4); "SET v_stmntMerge = 'MERGE INTO ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasPtId || ''' AS CLASSID, NULL AS DIV_OID, EN.EPSPST_OID AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'FROM SESSION.GroupElements GE ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'JOIN VL6CPST.ENDNODEHASPROPERTYTEMPLATE EN ON EN.ENO_OID = GE.OID ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'WHERE GE.CLASSID = ''' || c_endNodeId || '''';"
     Print #fileNo, addTab(7); "SET v_stmntMerge = v_stmntMerge || ') S ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'ON ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntMerge;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); "IF classId_in = c_endNodeId THEN"
     Print #fileNo, addTab(2); "-- loop over all organizations"
     Print #fileNo, addTab(3); "FOR orgLoop AS orgCursor CURSOR WITH HOLD FOR"
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "'VL6CAL' || CAST(RIGHT('00' || RTRIM(CAST(O.ID AS CHAR(2))),2) AS CHAR(2)) || D.ID as c_schema,"
     Print #fileNo, addTab(5); "O.ORGOID AS c_org_id,"
     Print #fileNo, addTab(5); "D.ID AS c_acc_id"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); "VL6CDBM.PDMORGANIZATION_ENUM O JOIN VL6CDBM.PDMDATAPOOLTYPE_ENUM D ON D.ID > 0"
     Print #fileNo, addTab(4); "ORDER BY"
     Print #fileNo, addTab(5); "O.ID, D.ID"
     Print #fileNo, addTab(4); "FOR READ ONLY"
     Print #fileNo, addTab(3); "DO"
     Print #fileNo, addTab(4); "IF c_acc_id <= 3 THEN"
     Print #fileNo, addTab(5); "SET v_stmntMerge = 'MERGE INTO ';"
     Print #fileNo, addTab(6); "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasGcId || ''' AS CLASSID, GC.CDIDIV_OID AS DIV_OID, NULL AS PS_OID, ' || c_org_id || ' AS ORG_OID, ' || c_acc_id || ' AS ACC_ID ';"
     Print #fileNo, addTab(8); " SET v_stmntMerge = v_stmntMerge || 'FROM ' || c_schema || '.ENDNODEHASGENERICCODE EN ';"
     Print #fileNo, addTab(8); " SET v_stmntMerge = v_stmntMerge || 'JOIN ' || c_schema || '.GENERICCODE GC ON GC.OID = EN.GCO_OID ';"
     Print #fileNo, addTab(8); " SET v_stmntMerge = v_stmntMerge || 'WHERE EN.ENO_OID = ' || groupElementOid_in ;"
     Print #fileNo, addTab(8); "SET v_stmntMerge = v_stmntMerge || ') S ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'ON ';"
     Print #fileNo, addTab(6); "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID AND T.orgOid = S.ORG_OID AND T.accModeId = S.ACC_ID ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';"
     Print #fileNo, addTab(6); "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';"
     Print #fileNo, addTab(6); "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';"
     Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntMerge;"
     Print #fileNo, addTab(4); "END IF;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(4); "SET v_stmntMerge = 'MERGE INTO ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasSrId || ''' AS CLASSID, NULL AS DIV_OID, EN.PS_OID AS PS_OID, ' || c_org_id || ' AS ORG_OID, ' || c_acc_id || ' AS ACC_ID ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'FROM ' || c_schema || '.ENDNODEHASNSR1VALIDITY EN ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'WHERE EN.ENO_OID = ' || groupElementOid_in ;"
     Print #fileNo, addTab(7); "SET v_stmntMerge = v_stmntMerge || ') S ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'ON ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID AND T.orgOid = S.ORG_OID AND T.accModeId = S.ACC_ID ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntMerge;"
     Print #fileNo, addTab(3); "END FOR;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(4); "SET v_stmntMerge = 'MERGE INTO ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'SESSION.GroupElements T ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'USING (SELECT EN.OID, ''' || c_enHasPtId || ''' AS CLASSID, NULL AS DIV_OID, EN.EPSPST_OID AS PS_OID, NULL AS ORG_OID, NULL AS ACC_ID ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'FROM VL6CPST.ENDNODEHASPROPERTYTEMPLATE EN ';"
     Print #fileNo, addTab(7); " SET v_stmntMerge = v_stmntMerge || 'WHERE EN.ENO_OID = ' || groupElementOid_in ;"
     Print #fileNo, addTab(7); "SET v_stmntMerge = v_stmntMerge || ') S ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'ON ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'T.oid = S.OID ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'WHEN NOT MATCHED THEN ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'INSERT (oid, classid, divOid, psOid, orgOid, accModeId) ';"
     Print #fileNo, addTab(5); "SET v_stmntMerge = v_stmntMerge || 'VALUES(S.OID, S.CLASSID, S.DIV_OID, S.PS_OID, S.ORG_OID, S.ACC_ID) ';"
     Print #fileNo, addTab(4); "SET v_stmntMerge = v_stmntMerge || 'ELSE IGNORE';"
     Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntMerge;"
     Print #fileNo, addTab(1); "END IF;"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); "MERGE INTO"
     Print #fileNo, addTab(2); "SESSION.GroupElements T"
     Print #fileNo, addTab(1); "USING (SELECT DISTINCT GE.classid, COALESCE(NL1.ENTITYLABEL, NL2.ENTITYLABEL, NL3.ENTITYLABEL, '') AS ENTITY"
     Print #fileNo, addTab(4); " FROM SESSION.GroupElements GE"
     Print #fileNo, addTab(4); " JOIN VL6CDBM.ACMENTITY E ON E.ENTITYID = GE.classid AND ((E.ENTITYTYPE = 'C' AND E.ENTITYID IN ('11022', '11023', '11024')) OR (E.ENTITYTYPE = 'R' AND E.ENTITYID IN ('05011', '09147', '04035')))"
     Print #fileNo, addTab(4); " LEFT JOIN VL6CDBM.ACMENTITY_NL_TEXT NL1 ON E.ENTITYSECTION = NL1.ENTITYSECTION AND E.ENTITYNAME = NL1.ENTITYNAME AND E.ENTITYTYPE = NL1.ENTITYTYPE AND NL1.LANGUAGE_ID = languageId_in"
     Print #fileNo, addTab(4); " LEFT JOIN VL6CDBM.ACMENTITY_NL_TEXT NL2 ON E.ENTITYSECTION = NL2.ENTITYSECTION AND E.ENTITYNAME = NL2.ENTITYNAME AND E.ENTITYTYPE = NL2.ENTITYTYPE AND NL2.LANGUAGE_ID = fallbackLanguageId_in"
     Print #fileNo, addTab(4); " LEFT JOIN VL6CDBM.ACMENTITY_NL_TEXT NL3 ON E.ENTITYSECTION = NL3.ENTITYSECTION AND E.ENTITYNAME = NL3.ENTITYNAME AND E.ENTITYTYPE = NL3.ENTITYTYPE AND NL3.LANGUAGE_ID = 1"
     Print #fileNo, addTab(4); ") S"
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); "T.classid = S.CLASSID"
     Print #fileNo, addTab(1); "WHEN MATCHED THEN"
     Print #fileNo, addTab(2); "UPDATE SET T.entity = S.ENTITY"
     Print #fileNo, addTab(1); "ELSE IGNORE"
     Print #fileNo, addTab(1); ";"
     Print #fileNo, addTab(0); ""
     Print #fileNo, addTab(1); "SET v_restmntTxt = 'SELECT DISTINCT oid, classid, divOid, psOid, orgOid, accModeId, entity FROM SESSION.GroupElements ORDER BY divOid, psOid, orgOid, accModeId';"
     Print #fileNo, addTab(1); "PREPARE v_restmnt FROM v_restmntTxt;"
     Print #fileNo, addTab(1); "OPEN c_return;"
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
 

   End If

 
   ' ####################################################################################################################
   ' #    INSERT Trigger handling new CountryID list
   ' ####################################################################################################################

   qualTriggerName = genQualTriggerNameByClassIndex(g_classIndexCountryIdList, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "_INS")

   printSectionHeader("Insert-Trigger handling new CountryID list in table """ & qualTabNameCountryIdList & """)", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE TRIGGER"
   Print #fileNo, addTab(1); qualTriggerName
   Print #fileNo, addTab(0); "AFTER INSERT ON"
   Print #fileNo, addTab(1); qualTabNameCountryIdList
   Print #fileNo, addTab(0); "REFERENCING"
   Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
   Print #fileNo, addTab(0); "FOR EACH ROW"
   Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
   genProcSectionHeader(fileNo, "declare variables", , True)
   genVarDecl(fileNo, "v_idList", "VARCHAR(" & CStr(countryIdListLength) & ")", "NULL")
 
   If Not forPool Then
     genVarDecl(fileNo, "v_cdUserId", g_dbtUserId, "NULL")
     genVarDecl(fileNo, "v_psOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_divisionOid", g_dbtOid, "NULL")
     genVarDecl(fileNo, "v_numClRecords", "INTEGER", "NULL")
     genVarDecl(fileNo, "v_clRecordOid", g_dbtOid, "NULL")
   End If

   genProcSectionHeader(fileNo, "normalize CountryId list", 1)
   Print #fileNo, addTab(1); "SET v_idList = "; qualFuncNameNormCidList; "("; gc_newRecordName; ".IDLIST);"
 
   genProcSectionHeader(fileNo, "persist normalized CountryId list", 1)
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); qualTabNameCountryIdList
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); "IDLIST = v_idList"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anOid; " = "; gc_newRecordName; "."; g_anOid
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "maintain table """ & qualTabNameCountryIdXRef & """")
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNameCountryIdXRef
   Print #fileNo, addTab(1); "("
 
   genAttrListForEntity(g_relIndexCountryIdXRef, eactRelationship, fileNo, ddlType, , , 2, False, False, edomListNonLrt)

   Print #fileNo, addTab(1); ")"

   Print #fileNo, addTab(1); "SELECT"

   initAttributeTransformation(transformation, 2)

   setAttributeMapping(transformation, 1, "CIL_OID", gc_newRecordName & "." & g_anOid)
   setAttributeMapping(transformation, 2, "CSP_OID", "CSP." & g_anOid)

   genTransformedAttrListForEntity(g_relIndexCountryIdXRef, eactRelationship, transformation, fileNo, ddlType, , , 2, , , , edomListNonLrt)

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "TABLE ("; g_qualFuncNameStrElems; "(v_idList)) CID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameCountrySpec; " CSP"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "CSP.ID = "; g_dbtEnumId; "(CID.elem)"
   Print #fileNo, addTab(1); ";"
 
   If Not forPool Then
     genProcSectionHeader(fileNo, "CountryID lists are 'common to Productstructure and Organization'", 1)
     Print #fileNo, addTab(1); "SET v_psOid       = NULL;"
     Print #fileNo, addTab(1); "SET v_divisionOid = NULL;"
 
     genProcSectionHeader(fileNo, "determine User id")
     Print #fileNo, addTab(1); "SET v_cdUserId = CAST(CASE COALESCE(CURRENT CLIENT_USERID, '') WHEN '' THEN 'NN' ELSE CURRENT CLIENT_USERID END AS "; g_dbtUserId; ");"
 
     genProcSectionHeader(fileNo, "create Changelog Records", 1)
     Print #fileNo, addTab(1); "CALL"
     Print #fileNo, addTab(2); qualProcNameClBroadCast
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "'"; g_classes.descriptors(g_classIndexCountryIdList).classIdStr; "',"
     Print #fileNo, addTab(2); "'"; gc_acmEntityTypeKeyClass; "',"
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "NULL,"
     Print #fileNo, addTab(2); "'"; getUnqualObjName(g_qualTabNameCountryIdList); "',"
     Print #fileNo, addTab(2); gc_newRecordName; "."; g_anOid; ","
     Print #fileNo, addTab(2); "v_cdUserId,"
     Print #fileNo, addTab(2); "v_divisionOid,"
     Print #fileNo, addTab(2); "v_psOid,"
     Print #fileNo, addTab(2); ""; CStr(lrtStatusNonLrtCreated); ","
     Print #fileNo, addTab(2); "CURRENT TIMESTAMP,"
     Print #fileNo, addTab(2); "v_numClRecords"
     Print #fileNo, addTab(1); ");"
   End If

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   If generateUpdatableCheckInUpdateTrigger And Not forPool Then
     ' ####################################################################################################################
     ' #    UPDATE Trigger prohibiting updates on CountryID list
     ' ####################################################################################################################

     qualTriggerName = genQualTriggerNameByClassIndex(g_classIndexCountryIdList, ddlType, , , , , , , , "_UPD")

     printSectionHeader("Update-Trigger prohibiting updates on CountryID list in table """ & g_qualTabNameCountryIdList & """)", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "AFTER UPDATE ON"
     Print #fileNo, addTab(1); g_qualTabNameCountryIdList
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
     Print #fileNo, addTab(1); "OLD AS "; gc_oldRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"

     genProcSectionHeader(fileNo, "verify that update maintains equivalence of ID lists", 1, True)
     Print #fileNo, addTab(1); "IF "; qualFuncNameNormCidList; "("; gc_newRecordName; ".IDLIST) <> "; qualFuncNameNormCidList; "("; gc_oldRecordName; ".IDLIST) THEN"
     genSignalDdl("updateNotAllowed", fileNo, 2, clnCountryIdList)
     Print #fileNo, addTab(1); "END IF;"

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If
 
   If generateUpdatableCheckInUpdateTrigger And Not forPool And Not generateFwkTest Then
     Dim qualTabNameCtsConfigHistory As String

     qualTriggerName = genQualTriggerNameByClassIndex(g_classIndexCtsConfig, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "_INS")
     qualTabNameCtsConfigHistory = genQualTabNameByClassIndex(g_classIndexCtsConfigHistory, ddlType, thisOrgIndex, thisPoolIndex)

     ' ####################################################################################################################
     ' #    INSERT Trigger propagating record from CTSCONFIG to CTSCONFIGHISTORY
     ' ####################################################################################################################

     printSectionHeader("Insert-Trigger propagating records from """ & g_qualTabNameCtsConfig & """ to """ & qualTabNameCtsConfigHistory & """", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "AFTER INSERT ON"
     Print #fileNo, addTab(1); g_qualTabNameCtsConfig
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
     genProcSectionHeader(fileNo, "propagate record", , True)
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameCtsConfigHistory
     Print #fileNo, addTab(1); "("
 
     genAttrListForEntity(g_classIndexCtsConfig, eactClass, fileNo, ddlType, , , 2, , , edomList)
 
     Print #fileNo, addTab(1); ")"
 
     Print #fileNo, addTab(1); "VALUES"
 
     Print #fileNo, addTab(1); "("
 
     initAttributeTransformation(transformation, 1, , , , gc_newRecordName & ".")
     setAttributeMapping(transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid)
 
     genTransformedAttrListForEntity(g_classIndexCtsConfig, eactClass, transformation, fileNo, ddlType, , , 2, , , , edomList)
 
     Print #fileNo, addTab(1); ");"

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim

     ' ####################################################################################################################
     ' #    UPDATE Trigger propagating record from CTSCONFIG to CTSCONFIGHISTORY
     ' ####################################################################################################################

     qualTriggerName = genQualTriggerNameByClassIndex(g_classIndexCtsConfig, ddlType, thisOrgIndex, thisPoolIndex, , , , , , "_UPD")

     printSectionHeader("Update-Trigger propagating records from """ & g_qualTabNameCtsConfig & """ to """ & qualTabNameCtsConfigHistory & """", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE TRIGGER"
     Print #fileNo, addTab(1); qualTriggerName
     Print #fileNo, addTab(0); "AFTER UPDATE ON"
     Print #fileNo, addTab(1); g_qualTabNameCtsConfig
     Print #fileNo, addTab(0); "REFERENCING"
     Print #fileNo, addTab(1); "NEW AS "; gc_newRecordName
     Print #fileNo, addTab(0); "FOR EACH ROW"
     Print #fileNo, addTab(0); "BEGIN ATOMIC"
 
     genProcSectionHeader(fileNo, "declare variables", , True)
     genVarDecl(fileNo, "v_ignorePropagate", g_dbtBoolean, gc_dbFalse)
 
     genProcSectionHeader(fileNo, "determine whether this update needs to be reported in history")
     Print #fileNo, addTab(1); "FOR recordLoop AS"
     Print #fileNo, addTab(2); "SELECT"
 
     Dim tabColumns As EntityColumnDescriptors

     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 10, , True)
     setAttributeMapping(transformation, 1, conRuleScopeId, "")
     setAttributeMapping(transformation, 2, conServiceType, "")
     setAttributeMapping(transformation, 3, "CORORG_OID", "")
     setAttributeMapping(transformation, 4, conPsOid, "")
     setAttributeMapping(transformation, 5, conCreateTimestamp, "")
     setAttributeMapping(transformation, 6, conUpdateUser, "")
     setAttributeMapping(transformation, 7, conLastUpdateTimestamp, "")
     setAttributeMapping(transformation, 8, conCreateUser, "")
     setAttributeMapping(transformation, 9, conVersionId, "")
     setAttributeMapping(transformation, 10, conOid, "")
 
     genTransformedAttrListForEntityWithColReuse(g_classIndexCtsConfig, eactClass, transformation, tabColumns, fileNo, ddlType, , , 3, , , edomNone)
     Dim k As Integer
     For k = 1 To tabColumns.numDescriptors
         Print #fileNo, addTab(3); tabColumns.descriptors(k).columnName; " AS c_"; tabColumns.descriptors(k).acmAttributeName; IIf(k = tabColumns.numDescriptors, "", ",")
     Next k
 
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameCtsConfigHistory
     Print #fileNo, addTab(2); "WHERE "
     Print #fileNo, addTab(3); g_anRuleScope; " = "; gc_newRecordName; "."; g_anRuleScope
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "SERVICETYPE = "; gc_newRecordName; ".SERVICETYPE"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "CORORG_OID = "; gc_newRecordName; ".CORORG_OID"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anPsOid; " = "; gc_newRecordName; "."; g_anPsOid
     Print #fileNo, addTab(2); "ORDER BY"
     Print #fileNo, addTab(3); "TIMESTAMP DESC"
     Print #fileNo, addTab(2); "FETCH FIRST 1 ROW ONLY"
     Print #fileNo, addTab(1); "DO"
 
     Print #fileNo, addTab(2); "SET v_ignorePropagate ="
     Print #fileNo, addTab(3); "("
     Print #fileNo, addTab(4); "CASE WHEN"

     For k = 1 To tabColumns.numDescriptors
         If k > 1 Then
           Print #fileNo, addTab(6); "AND"
         End If
         If tabColumns.descriptors(k).columnName = "SIZEFACTOR" Then
           Print #fileNo, addTab(5); gc_newRecordName; "."; tabColumns.descriptors(k).columnName; " > (0.9 * c_"; tabColumns.descriptors(k).acmAttributeName; ")"
           Print #fileNo, addTab(6); "AND"
           Print #fileNo, addTab(5); gc_newRecordName; "."; tabColumns.descriptors(k).columnName; " < (1.1 * c_"; tabColumns.descriptors(k).acmAttributeName; ")"
         Else
           Print #fileNo, addTab(5); gc_newRecordName; "."; tabColumns.descriptors(k).columnName; " = c_"; tabColumns.descriptors(k).acmAttributeName
         End If
     Next k
     Print #fileNo, addTab(4); "THEN 1 ELSE 0 END"
     Print #fileNo, addTab(3); ");"
 
     Print #fileNo, addTab(1); "END FOR;"
 
     genProcSectionHeader(fileNo, "propagate record")
     Print #fileNo, addTab(1); "IF v_ignorePropagate = "; gc_dbFalse; " THEN"
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); qualTabNameCtsConfigHistory
     Print #fileNo, addTab(2); "("

     genAttrListForEntity(g_classIndexCtsConfig, eactClass, fileNo, ddlType, , , 3, , , edomList)

     Print #fileNo, addTab(2); ")"

     Print #fileNo, addTab(2); "VALUES"

     Print #fileNo, addTab(2); "("

     initAttributeTransformation(transformation, 1, , , , gc_newRecordName & ".")
     setAttributeMapping(transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid)

     genTransformedAttrListForEntity(g_classIndexCtsConfig, eactClass, transformation, fileNo, ddlType, , , 3, , , , edomList)

     Print #fileNo, addTab(2); ");"
     Print #fileNo, addTab(1); "END IF;"

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
 
 
 Sub genDdlForRegStaticSstUpdate( _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtPdm, _
   Optional indent As Integer = 1, _
   Optional useStaticSql As Boolean = True, _
   Optional ByRef orgIdFilterStr As String = "", _
   Optional ByRef orgOidFilterStr As String = "", _
   Optional ByRef psOidFilterStr As String = "", _
   Optional ByRef accessModeIdFilterStr As String = "", _
   Optional ByRef stmntTxtVarName As String = "v_stmntTxt", _
   Optional ByRef tempTableName As String = tempTabNameStatement, _
   Optional ByRef stmntColName As String = "statement", _
   Optional ByRef modeVarName As String = "mode_in", _
   Optional ByRef rowCountVarName As String = "v_rowCount", _
   Optional ByRef rowCountSumVarName As String = "rowCount_out", _
   Optional genTimestampDdl As Boolean = False _
 )
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(-1, ddlType)

   Dim transformation As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors

   genProcSectionHeader(fileNo, "setup organization-specific data in """ & g_qualTabNameRegistryStatic & """", indent)
   If useStaticSql Then
     Print #fileNo, addTab(indent + 0); "INSERT INTO"
     Print #fileNo, addTab(indent + 1); g_qualTabNameRegistryStatic
     Print #fileNo, addTab(indent + 0); "("
 
     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 4, , True)
     setAttributeMapping(transformation, 1, conCreateTimestamp, "")
     setAttributeMapping(transformation, 2, conUpdateUser, "")
     setAttributeMapping(transformation, 3, conLastUpdateTimestamp, "")
     setAttributeMapping(transformation, 4, conVersionId, "")
 
     genTransformedAttrListForEntityWithColReuse(g_classIndexRegistryStatic, eactClass, transformation, tabColumns, fileNo, ddlType, , , indent + 1, , , edomNone)
     Dim k As Integer
     For k = 1 To tabColumns.numDescriptors
         Print #fileNo, addTab(indent + 1); tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ",")
     Next k
 
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "WITH"
     Print #fileNo, addTab(indent + 1); "V_Section"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "section"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "AS"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "VALUES('STANDARDXML') UNION ALL"
     Print #fileNo, addTab(indent + 1); "VALUES('VDFXML')"
     Print #fileNo, addTab(indent + 0); "),"
     Print #fileNo, addTab(indent + 1); "V_RefPs"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "oid"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "AS"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "SELECT MIN(OID) FROM "; g_qualTabNameProductStructure
     Print #fileNo, addTab(indent + 0); "),"
     Print #fileNo, addTab(indent + 1); "V_Src"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "accessModeId,"
     Print #fileNo, addTab(indent + 1); "section,"
     Print #fileNo, addTab(indent + 1); "value"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "AS"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "SELECT"
     Print #fileNo, addTab(indent + 2); "AM.ID,"
     Print #fileNo, addTab(indent + 2); "V.section,"
     Print #fileNo, addTab(indent + 2); "CAST(R."; g_anValue; " AS VARCHAR(30))"
     Print #fileNo, addTab(indent + 1); "FROM"
     Print #fileNo, addTab(indent + 2); g_qualTabNameDataPoolAccessMode; " AM"
     Print #fileNo, addTab(indent + 1); "INNER JOIN"
     Print #fileNo, addTab(indent + 2); "V_RefPs"
     Print #fileNo, addTab(indent + 1); "ON"
     Print #fileNo, addTab(indent + 2); "AM.ID IN (2,3)"
     Print #fileNo, addTab(indent + 1); "INNER JOIN"
     Print #fileNo, addTab(indent + 2); g_qualTabNamePdmOrganization; " O"
     Print #fileNo, addTab(indent + 1); "ON"
     Print #fileNo, addTab(indent + 2); "O.ID = 2"
     Print #fileNo, addTab(indent + 1); "INNER JOIN"
     Print #fileNo, addTab(indent + 2); g_qualTabNameRegistryStatic; " R"
     Print #fileNo, addTab(indent + 1); "ON"
     Print #fileNo, addTab(indent + 2); "R."; g_anSubKey; " = RTRIM(CHAR(O.ORGOID)) || ',' || RTRIM(CHAR(V_RefPs.oid)) || ',' || RTRIM(CHAR(AM.ID))"
     Print #fileNo, addTab(indent + 3); "AND"
     Print #fileNo, addTab(indent + 2); g_anKey; " = 'DESTINATION'"
     Print #fileNo, addTab(indent + 1); "INNER JOIN"
     Print #fileNo, addTab(indent + 2); "V_Section V"
     Print #fileNo, addTab(indent + 1); "ON"
     Print #fileNo, addTab(indent + 2); "R.SECTION = V.section"
     Print #fileNo, addTab(indent + 0); "),"
     Print #fileNo, addTab(indent + 1); "V_Default"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "accessModeId,"
     Print #fileNo, addTab(indent + 1); "section,"
     Print #fileNo, addTab(indent + 1); "value"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "AS"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "SELECT"
     Print #fileNo, addTab(indent + 2); "AM.ID,"
     Print #fileNo, addTab(indent + 2); "V.section,"
     Print #fileNo, addTab(indent + 2); "'temp'"
     Print #fileNo, addTab(indent + 1); "FROM"
     Print #fileNo, addTab(indent + 2); "V_Section V,"
     Print #fileNo, addTab(indent + 2); g_qualTabNameDataPoolAccessMode; " AM"
     Print #fileNo, addTab(indent + 1); "WHERE"
     Print #fileNo, addTab(indent + 2); "AM.ID IN (2,3)"
     Print #fileNo, addTab(indent + 0); "),"
     Print #fileNo, addTab(indent + 1); "V_Values"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "prio,"
     Print #fileNo, addTab(indent + 1); "accessModeId,"
     Print #fileNo, addTab(indent + 1); "section,"
     Print #fileNo, addTab(indent + 1); "value"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "AS"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "SELECT 1, accessModeId, section, value FROM V_Src"
     Print #fileNo, addTab(indent + 2); "UNION ALL"
     Print #fileNo, addTab(indent + 1); "SELECT 2, accessModeId, section, value FROM V_Default"
     Print #fileNo, addTab(indent + 0); "),"
     Print #fileNo, addTab(indent + 1); "V_ValuesBestMatch"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "prio,"
     Print #fileNo, addTab(indent + 1); "accessModeId,"
     Print #fileNo, addTab(indent + 1); "section,"
     Print #fileNo, addTab(indent + 1); "value"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "AS"
     Print #fileNo, addTab(indent + 0); "("
     Print #fileNo, addTab(indent + 1); "SELECT"
     Print #fileNo, addTab(indent + 2); "prio,"
     Print #fileNo, addTab(indent + 2); "accessModeId,"
     Print #fileNo, addTab(indent + 2); "section,"
     Print #fileNo, addTab(indent + 2); "value"
     Print #fileNo, addTab(indent + 1); "FROM"
     Print #fileNo, addTab(indent + 2); "V_Values V"
     Print #fileNo, addTab(indent + 1); "WHERE"
     Print #fileNo, addTab(indent + 2); "NOT EXISTS ("
     Print #fileNo, addTab(indent + 3); "SELECT"
     Print #fileNo, addTab(indent + 4); "1"
     Print #fileNo, addTab(indent + 3); "FROM"
     Print #fileNo, addTab(indent + 4); "V_Values V2"
     Print #fileNo, addTab(indent + 3); "WHERE"
     Print #fileNo, addTab(indent + 4); "V.accessModeId = V2.accessModeId"
     Print #fileNo, addTab(indent + 5); "AND"
     Print #fileNo, addTab(indent + 4); "V.section = V2.section"
     Print #fileNo, addTab(indent + 5); "AND"
     Print #fileNo, addTab(indent + 4); "V.prio > V2.prio"
     Print #fileNo, addTab(indent + 2); ")"
     Print #fileNo, addTab(indent + 0); ")"
     Print #fileNo, addTab(indent + 0); "SELECT"

     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 10, , True)
     setAttributeMapping(transformation, 1, conCreateTimestamp, "")
     setAttributeMapping(transformation, 2, conUpdateUser, "")
     setAttributeMapping(transformation, 3, conLastUpdateTimestamp, "")
     setAttributeMapping(transformation, 4, conCreateUser, "RTRIM(CURRENT USER)")
     setAttributeMapping(transformation, 5, conVersionId, "")
     setAttributeMapping(transformation, 6, conOid, "NEXTVAL FOR " & qualSeqNameOid)
     setAttributeMapping(transformation, 7, conSection, "V.section")
     setAttributeMapping(transformation, 8, conKey, "'DESTINATION'")
     setAttributeMapping(transformation, 9, conSubKey, "RTRIM(CHAR(DP.DPOORG_OID)) || ',' || RTRIM(CHAR(DP.DPSPST_OID)) || ',' || RTRIM(CHAR(DP." & g_anAccessModeId & "))")
     setAttributeMapping(transformation, 10, conValue, "V.value")

     genTransformedAttrListForEntityWithColReuse(g_classIndexRegistryStatic, eactClass, transformation, tabColumns, fileNo, ddlType, , , indent + 1, , , edomNone)
 
     For k = 1 To tabColumns.numDescriptors
         Print #fileNo, addTab(indent + 1); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); IIf(k < tabColumns.numDescriptors, ",", "")
     Next k

     Print #fileNo, addTab(indent + 0); "FROM"
     Print #fileNo, addTab(indent + 1); g_qualTabNameDataPool; " DP"
     Print #fileNo, addTab(indent + 0); "INNER JOIN"
     Print #fileNo, addTab(indent + 1); "V_ValuesBestMatch V"
     Print #fileNo, addTab(indent + 0); "ON"
     Print #fileNo, addTab(indent + 1); "DP."; g_anAccessModeId; " = V.accessModeId"
     Print #fileNo, addTab(indent + 0); "INNER JOIN"
     Print #fileNo, addTab(indent + 1); g_qualTabNamePdmOrganization; " O"
     Print #fileNo, addTab(indent + 0); "ON"
     Print #fileNo, addTab(indent + 1); "DP.DPOORG_OID = O.ORGOID"
     Print #fileNo, addTab(indent + 2); "AND"
     Print #fileNo, addTab(indent + 1); "O.ID = COALESCE(orgId_in, O.ID)"
     Print #fileNo, addTab(indent + 0); "WHERE"
     Print #fileNo, addTab(indent + 1); "DP.DPSPST_OID = COALESCE(psOid_in, DP.DPSPST_OID)"
     Print #fileNo, addTab(indent + 2); "AND"
     Print #fileNo, addTab(indent + 1); "DP."; g_anAccessModeId; " = COALESCE(poolId_in, DP."; g_anAccessModeId; ")"
     Print #fileNo, addTab(indent + 2); "AND"
     Print #fileNo, addTab(indent + 1); "NOT EXISTS ("
     Print #fileNo, addTab(indent + 2); "SELECT"
     Print #fileNo, addTab(indent + 3); "1"
     Print #fileNo, addTab(indent + 2); "FROM"
     Print #fileNo, addTab(indent + 3); g_qualTabNameRegistryStatic; " R"
     Print #fileNo, addTab(indent + 2); "WHERE"
     Print #fileNo, addTab(indent + 3); "R.SECTION = V.section"
     Print #fileNo, addTab(indent + 4); "AND"
     Print #fileNo, addTab(indent + 3); "R."; g_anKey; " = 'DESTINATION'"
     Print #fileNo, addTab(indent + 4); "AND"
     Print #fileNo, addTab(indent + 3); "R."; g_anSubKey; " = RTRIM(CHAR(DP.DPOORG_OID)) || ',' || RTRIM(CHAR(DP.DPSPST_OID)) || ',' || RTRIM(CHAR(DP."; g_anAccessModeId; "))"
     Print #fileNo, addTab(indent + 1); ")"
     Print #fileNo, addTab(indent + 0); ";"

     If rowCountVarName <> "" Then
       genProcSectionHeader(fileNo, "count the number of affected rows", indent)
       Print #fileNo, addTab(indent); "GET DIAGNOSTICS "; rowCountVarName; " = ROW_COUNT;"
       If rowCountSumVarName <> "" Then
         Print #fileNo, addTab(indent); "SET "; rowCountSumVarName; " = "; rowCountSumVarName; " + "; rowCountVarName; ";"
       End If
     End If
   Else
     Print #fileNo, addTab(indent + 0); "SET "; stmntTxtVarName; " = 'INSERT INTO "; g_qualTabNameRegistryStatic; " (' ||"

     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 4, , True)
     setAttributeMapping(transformation, 1, conCreateTimestamp, "")
     setAttributeMapping(transformation, 2, conUpdateUser, "")
     setAttributeMapping(transformation, 3, conLastUpdateTimestamp, "")
     setAttributeMapping(transformation, 4, conVersionId, "")

     genTransformedAttrListForEntityWithColReuse(g_classIndexRegistryStatic, eactClass, transformation, tabColumns, fileNo, ddlType, , , indent + 1, , , edomNone)

     For k = 1 To tabColumns.numDescriptors
         Print #fileNo, addTab(indent + 1); "'"; tabColumns.descriptors(k).columnName; IIf(k = tabColumns.numDescriptors, "", ","); "' ||"
     Next k

     Print #fileNo, addTab(indent + 0); "') ' ||"
 
     Print #fileNo, addTab(indent + 0); "'WITH ' ||"
     Print #fileNo, addTab(indent + 1); "'V_Section ' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'section' ||"
     Print #fileNo, addTab(indent + 0); "') ' ||"
     Print #fileNo, addTab(indent + 0); "'AS ' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'VALUES(''STANDARDXML'') UNION ALL ' ||"
     Print #fileNo, addTab(indent + 1); "'VALUES(''VDFXML'')' ||"
     Print #fileNo, addTab(indent + 0); "'),' ||"
     Print #fileNo, addTab(indent + 1); "'V_RefPs' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'oid' ||"
     Print #fileNo, addTab(indent + 0); "')' ||"
     Print #fileNo, addTab(indent + 0); "'AS' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'SELECT MIN(OID) FROM "; g_qualTabNameProductStructure; "' ||"
     Print #fileNo, addTab(indent + 0); "'), ' ||"
     Print #fileNo, addTab(indent + 1); "'V_Src' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'accessModeId,' ||"
     Print #fileNo, addTab(indent + 1); "'section,' ||"
     Print #fileNo, addTab(indent + 1); "'value' ||"
     Print #fileNo, addTab(indent + 0); "') ' ||"
     Print #fileNo, addTab(indent + 0); "'AS ' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'SELECT ' ||"
     Print #fileNo, addTab(indent + 2); "'AM.ID,' ||"
     Print #fileNo, addTab(indent + 2); "'V.section,' ||"
     Print #fileNo, addTab(indent + 2); "'CAST(R."; g_anValue; " AS VARCHAR(30)) ' ||"
     Print #fileNo, addTab(indent + 1); "'FROM ' ||"
     Print #fileNo, addTab(indent + 2); "'"; g_qualTabNameDataPoolAccessMode; " AM ' ||"
     Print #fileNo, addTab(indent + 1); "'INNER JOIN ' ||"
     Print #fileNo, addTab(indent + 2); "'V_RefPs ' ||"
     Print #fileNo, addTab(indent + 1); "'ON ' ||"
     Print #fileNo, addTab(indent + 2); "'AM.ID IN (2,3) ' ||"
     Print #fileNo, addTab(indent + 1); "'INNER JOIN ' ||"
     Print #fileNo, addTab(indent + 2); "'"; g_qualTabNamePdmOrganization; " O ' ||"
     Print #fileNo, addTab(indent + 1); "'ON ' ||"
     Print #fileNo, addTab(indent + 2); "'O.ID = 2 ' ||"
     Print #fileNo, addTab(indent + 1); "'INNER JOIN ' ||"
     Print #fileNo, addTab(indent + 2); "'"; g_qualTabNameRegistryStatic; " R ' ||"
     Print #fileNo, addTab(indent + 1); "'ON ' ||"
     Print #fileNo, addTab(indent + 2); "'R."; g_anSubKey; " = RTRIM(CHAR(O.ORGOID)) || '','' || RTRIM(CHAR(V_RefPs.oid)) || '','' || RTRIM(CHAR(AM.ID)) ' ||"
     Print #fileNo, addTab(indent + 3); "'AND ' ||"
     Print #fileNo, addTab(indent + 2); "'"; g_anKey; " = ''DESTINATION'' ' ||"
     Print #fileNo, addTab(indent + 1); "'INNER JOIN ' ||"
     Print #fileNo, addTab(indent + 2); "'V_Section V ' ||"
     Print #fileNo, addTab(indent + 1); "'ON ' ||"
     Print #fileNo, addTab(indent + 2); "'R.SECTION = V.section ' ||"
     Print #fileNo, addTab(indent + 0); "'), ' ||"
     Print #fileNo, addTab(indent + 1); "'V_Default ' ||"
     Print #fileNo, addTab(indent + 0); "'( ' ||"
     Print #fileNo, addTab(indent + 1); "'accessModeId,' ||"
     Print #fileNo, addTab(indent + 1); "'section,' ||"
     Print #fileNo, addTab(indent + 1); "'value' ||"
     Print #fileNo, addTab(indent + 0); "') ' ||"
     Print #fileNo, addTab(indent + 0); "'AS ' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'SELECT ' ||"
     Print #fileNo, addTab(indent + 2); "'AM.ID,' ||"
     Print #fileNo, addTab(indent + 2); "'V.section,' ||"
     Print #fileNo, addTab(indent + 2); "'''temp'' ' ||"
     Print #fileNo, addTab(indent + 1); "'FROM ' ||"
     Print #fileNo, addTab(indent + 2); "'V_Section V,' ||"
     Print #fileNo, addTab(indent + 2); "'"; g_qualTabNameDataPoolAccessMode; " AM ' ||"
     Print #fileNo, addTab(indent + 1); "'WHERE ' ||"
     Print #fileNo, addTab(indent + 2); "'AM.ID IN (2,3)' ||"
     Print #fileNo, addTab(indent + 0); "'), ' ||"
     Print #fileNo, addTab(indent + 1); "'V_Values ' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'prio,' ||"
     Print #fileNo, addTab(indent + 1); "'accessModeId,' ||"
     Print #fileNo, addTab(indent + 1); "'section,' ||"
     Print #fileNo, addTab(indent + 1); "'value ' ||"
     Print #fileNo, addTab(indent + 0); "') ' ||"
     Print #fileNo, addTab(indent + 0); "'AS ' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'SELECT 1, accessModeId, section, value FROM V_Src ' ||"
     Print #fileNo, addTab(indent + 2); "'UNION ALL ' ||"
     Print #fileNo, addTab(indent + 1); "'SELECT 2, accessModeId, section, value FROM V_Default ' ||"
     Print #fileNo, addTab(indent + 0); "'), ' ||"
     Print #fileNo, addTab(indent + 1); "'V_ValuesBestMatch ' ||"
     Print #fileNo, addTab(indent + 0); "'(' ||"
     Print #fileNo, addTab(indent + 1); "'prio,' ||"
     Print #fileNo, addTab(indent + 1); "'accessModeId,' ||"
     Print #fileNo, addTab(indent + 1); "'section,' ||"
     Print #fileNo, addTab(indent + 1); "'value' ||"
     Print #fileNo, addTab(indent + 0); "') ' ||"
     Print #fileNo, addTab(indent + 0); "'AS ' ||"
     Print #fileNo, addTab(indent + 0); "'( ' ||"
     Print #fileNo, addTab(indent + 1); "'SELECT ' ||"
     Print #fileNo, addTab(indent + 2); "'prio,' ||"
     Print #fileNo, addTab(indent + 2); "'accessModeId,' ||"
     Print #fileNo, addTab(indent + 2); "'section,' ||"
     Print #fileNo, addTab(indent + 2); "'value ' ||"
     Print #fileNo, addTab(indent + 1); "'FROM ' ||"
     Print #fileNo, addTab(indent + 2); "'V_Values V ' ||"
     Print #fileNo, addTab(indent + 1); "'WHERE ' ||"
     Print #fileNo, addTab(indent + 2); "'NOT EXISTS (' ||"
     Print #fileNo, addTab(indent + 3); "'SELECT ' ||"
     Print #fileNo, addTab(indent + 4); "'1 ' ||"
     Print #fileNo, addTab(indent + 3); "'FROM ' ||"
     Print #fileNo, addTab(indent + 4); "'V_Values V2 ' ||"
     Print #fileNo, addTab(indent + 3); "'WHERE ' ||"
     Print #fileNo, addTab(indent + 4); "'V.accessModeId = V2.accessModeId ' ||"
     Print #fileNo, addTab(indent + 5); "'AND ' ||"
     Print #fileNo, addTab(indent + 4); "'V.section = V2.section ' ||"
     Print #fileNo, addTab(indent + 5); "'AND ' ||"
     Print #fileNo, addTab(indent + 4); "'V.prio > V2.prio' ||"
     Print #fileNo, addTab(indent + 2); "')' ||"
     Print #fileNo, addTab(indent + 0); "') ' ||"
     Print #fileNo, addTab(indent + 0); "'SELECT ' ||"

     tabColumns = nullEntityColumnDescriptors
     initAttributeTransformation(transformation, 10, , True)
     setAttributeMapping(transformation, 1, conCreateTimestamp, "")
     setAttributeMapping(transformation, 2, conUpdateUser, "")
     setAttributeMapping(transformation, 3, conLastUpdateTimestamp, "")
     setAttributeMapping(transformation, 4, conCreateUser, "'''' || RTRIM(CURRENT USER) || ''''")
     setAttributeMapping(transformation, 5, conVersionId, "")
     setAttributeMapping(transformation, 6, conOid, "'NEXTVAL FOR " & qualSeqNameOid & "'")
     setAttributeMapping(transformation, 7, conSection, "'V.section'")
     setAttributeMapping(transformation, 8, conKey, "'''DESTINATION'''")
     setAttributeMapping(transformation, 9, conSubKey, "'RTRIM(CHAR(DP.DPOORG_OID)) || '','' || RTRIM(CHAR(DP.DPSPST_OID)) || '','' || RTRIM(CHAR(DP." & g_anAccessModeId & "))'")
     setAttributeMapping(transformation, 10, conValue, "'V.value'")

     genTransformedAttrListForEntityWithColReuse(g_classIndexRegistryStatic, eactClass, transformation, tabColumns, fileNo, ddlType, , , indent + 1, , , edomNone)
 
     For k = 1 To tabColumns.numDescriptors
         Print #fileNo, addTab(indent + 1); transformAttrName(tabColumns.descriptors(k).columnName, eavtDomain, tabColumns.descriptors(k).dbDomainIndex, transformation, ddlType, , , , True, tabColumns.descriptors(k).acmAttributeIndex, edomList); " ||"; IIf(k < tabColumns.numDescriptors, " ',' ||", "")
     Next k

     Print #fileNo, addTab(indent + 0); "'FROM ' ||"
     Print #fileNo, addTab(indent + 1); "'"; g_qualTabNameDataPool; " DP ' ||"
     Print #fileNo, addTab(indent + 0); "'INNER JOIN ' ||"
     Print #fileNo, addTab(indent + 1); "'V_ValuesBestMatch V ' ||"
     Print #fileNo, addTab(indent + 0); "'ON ' ||"
     Print #fileNo, addTab(indent + 1); "'DP."; g_anAccessModeId; " = V.accessModeId ' ||"
     Print #fileNo, addTab(indent + 0); "'INNER JOIN ' ||"
     Print #fileNo, addTab(indent + 1); "'"; g_qualTabNamePdmOrganization; " O ' ||"
     Print #fileNo, addTab(indent + 0); "'ON ' ||"
     Print #fileNo, addTab(indent + 1); "'DP.DPOORG_OID = O.ORGOID ' ||"
     Print #fileNo, addTab(indent + 2); "'AND ' ||"
     Print #fileNo, addTab(indent + 1); "'O.ID = COALESCE(orgId_in, O.ID) ' ||"
     Print #fileNo, addTab(indent + 0); "'WHERE ' ||"
     Print #fileNo, addTab(indent + 1); "'DP.DPSPST_OID = COALESCE(psOid_in, DP.DPSPST_OID) ' ||"
     Print #fileNo, addTab(indent + 2); "'AND ' ||"
     Print #fileNo, addTab(indent + 1); "'DP."; g_anAccessModeId; " = COALESCE(poolId_in, DP."; g_anAccessModeId; ") ' ||"
     Print #fileNo, addTab(indent + 2); "'AND ' ||"
     Print #fileNo, addTab(indent + 1); "'NOT EXISTS (' ||"
     Print #fileNo, addTab(indent + 2); "'SELECT ' ||"
     Print #fileNo, addTab(indent + 3); "'1 ' ||"
     Print #fileNo, addTab(indent + 2); "'FROM ' ||"
     Print #fileNo, addTab(indent + 3); "'"; g_qualTabNameRegistryStatic; " R ' ||"
     Print #fileNo, addTab(indent + 2); "'WHERE ' ||"
     Print #fileNo, addTab(indent + 3); "'R.SECTION = V.section ' ||"
     Print #fileNo, addTab(indent + 4); "'AND ' ||"
     Print #fileNo, addTab(indent + 3); "'R."; g_anKey; " = ''DESTINATION'' ' ||"
     Print #fileNo, addTab(indent + 4); "'AND ' ||"
     Print #fileNo, addTab(indent + 3); "'R."; g_anSubKey; " = RTRIM(CHAR(DP.DPOORG_OID)) || '','' || RTRIM(CHAR(DP.DPSPST_OID)) || '','' || RTRIM(CHAR(DP."; g_anAccessModeId; "))' ||"
     Print #fileNo, addTab(indent + 1); "')'"
     Print #fileNo, addTab(indent + 0); ";"

     Dim offset As Integer
     offset = IIf(modeVarName = "", 0, 1)
     If tempTableName <> "" Then
       Print #fileNo,
       If modeVarName <> "" Then
         Print #fileNo, addTab(indent + 0); "IF "; modeVarName; " <= 1 THEN"
       End If
       Print #fileNo, addTab(indent + offset); "INSERT INTO "; tempTableName; "("; stmntColName; ") VALUES ("; stmntTxtVarName; ");"

       If genTimestampDdl Then
         Print #fileNo, addTab(indent + offset); "INSERT INTO "; tempTableName; "(statement) VALUES ('VALUES CURRENT TIMESTAMP');"
       End If

       If modeVarName <> "" Then
         Print #fileNo, addTab(indent + 0); "END IF;"
       End If
     End If

     Print #fileNo,
     If modeVarName <> "" Then
       Print #fileNo, addTab(indent + 0); "IF "; modeVarName; " >= 1 THEN"
     End If
     Print #fileNo, addTab(indent + offset); "EXECUTE IMMEDIATE "; stmntTxtVarName; ";"
     If rowCountVarName <> "" Then
       genProcSectionHeader(fileNo, "count the number of affected rows", indent + offset)
       Print #fileNo, addTab(indent + offset); "GET DIAGNOSTICS "; rowCountVarName; " = ROW_COUNT;"
       If rowCountSumVarName <> "" Then
         Print #fileNo, addTab(indent + offset); "SET "; rowCountSumVarName; " = "; rowCountSumVarName; " + "; rowCountVarName; ";"
       End If
     End If
     If modeVarName <> "" Then
       Print #fileNo, addTab(indent + 0); "END IF;"
     End If

   End If
 End Sub
 ' ### ENDIF IVK ###
 
 
 
 
 
