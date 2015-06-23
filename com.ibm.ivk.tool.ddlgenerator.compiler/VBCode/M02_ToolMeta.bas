Attribute VB_Name = "M02_ToolMeta"
Option Explicit

Function getDataTypeId( _
  typeStr As String _
) As typeId
  If UCase(typeStr) = "BOOLEAN" Then
    getDataTypeId = etBoolean
  ElseIf UCase(typeStr) = "SMALLINT" Then
    getDataTypeId = etSmallint
  ElseIf UCase(typeStr) = "VARCHAR" Then
    getDataTypeId = etVarchar
  ElseIf UCase(typeStr) = "LONG VARCHAR" Then
    getDataTypeId = etLongVarchar
  ElseIf UCase(typeStr) = "VARCHAR FOR BIT DATA" Then
    getDataTypeId = etBinVarchar
  ElseIf UCase(typeStr) = "CHAR" Then
    getDataTypeId = etChar
  ElseIf UCase(typeStr) = "CHAR FOR BIT DATA" Then
    getDataTypeId = etBinChar
  ElseIf UCase(typeStr) = "BLOB" Then
    getDataTypeId = etBlob
  ElseIf UCase(typeStr) = "CLOB" Then
    getDataTypeId = etClob
  ElseIf UCase(typeStr) = "INTEGER" Then
    getDataTypeId = etInteger
  ElseIf UCase(typeStr) = "BIGINT" Then
    getDataTypeId = etBigInt
  ElseIf UCase(typeStr) = "FLOAT" Then
    getDataTypeId = etFloat
  ElseIf UCase(typeStr) = "DECIMAL" Then
    getDataTypeId = etDecimal
  ElseIf UCase(typeStr) = "DATE" Then
    getDataTypeId = etDate
  ElseIf UCase(typeStr) = "TIME" Then
    getDataTypeId = etTime
  ElseIf UCase(typeStr) = "TIMESTAMP" Then
    getDataTypeId = etTimestamp
  ElseIf UCase(typeStr) = "DOUBLE" Then
    getDataTypeId = etDouble
  Else
    getDataTypeId = Null
  End If
End Function


Function getDataType( _
  typeId As typeId, _
  Optional ByRef maxLength As String = "", _
  Optional prec As Integer = -1, _
  Optional useUnicode As Boolean = False, _
  Optional expansionFactor As Single = -1 _
) As String
  Dim effectiveMaxLength As String
  effectiveMaxLength = maxLength
  
  If maxLength <> "" Then
    If supportUnicode And useUnicode Then
      effectiveMaxLength = CInt(IIf(expansionFactor >= 1, expansionFactor, unicodeExpansionFactor) * CInt(maxLength)) & ""
    Else
      effectiveMaxLength = maxLength
    End If
  End If
  
  Dim specific As String
  specific = IIf(maxLength <> "", "(" & effectiveMaxLength & IIf(prec > 0, "," & prec, "") & ")", "")
    
  If typeId = etBoolean Then
    getDataType = "SMALLINT"
  ElseIf typeId = etSmallint Then
    getDataType = "SMALLINT"
  ElseIf typeId = etVarchar Then
    getDataType = "VARCHAR" & specific
  ElseIf typeId = etLongVarchar Then
    getDataType = "LONG VARCHAR" & specific
  ElseIf typeId = etBinVarchar Then
    getDataType = "VARCHAR" & specific & " FOR BIT DATA"
  ElseIf typeId = etChar Then
    getDataType = "CHAR" & specific
  ElseIf typeId = etBinChar Then
    getDataType = "CHAR" & specific & " FOR BIT DATA"
  ElseIf typeId = etBlob Then
    getDataType = "BLOB" & specific
  ElseIf typeId = etClob Then
    getDataType = "CLOB" & specific
  ElseIf typeId = etInteger Then
    getDataType = "INTEGER"
  ElseIf typeId = etBigInt Then
    getDataType = "BIGINT"
  ElseIf typeId = etDecimal Then
    getDataType = "DECIMAL" & specific
  ElseIf typeId = etFloat Then
    getDataType = "FLOAT"
  ElseIf typeId = etDate Then
    getDataType = "DATE"
  ElseIf typeId = etTime Then
    getDataType = "TIME"
  ElseIf typeId = etTimestamp Then
    getDataType = "TIMESTAMP"
  ElseIf typeId = etDouble Then
    getDataType = "DOUBLE"
  Else
    getDataType = Null
  End If
End Function


Function getDataTypeByDomainIndex( _
  domainIndex As Integer, _
  Optional useUnicode As Boolean = False _
) As String
  With g_domains.descriptors(domainIndex)
    getDataTypeByDomainIndex = getDataType(.dataType, .maxLength, .scale, useUnicode, .unicodeExpansionFactor)
  End With
End Function


Function getJavaDataType( _
  typeId As typeId _
) As String
  If typeId = etSmallint Then
    getJavaDataType = "XSMALLINT"
  ElseIf typeId = etVarchar Then
    getJavaDataType = "string"
  ElseIf typeId = etChar Then
    getJavaDataType = "string"
  ElseIf typeId = etBinChar Then
    getJavaDataType = "XCLOB"
  ElseIf typeId = etBlob Then
    getJavaDataType = "XBLOB"
  ElseIf typeId = etClob Then
    getJavaDataType = "XCLOB"
  ElseIf typeId = etInteger Then
    getJavaDataType = "java.lang.Integer"
  ElseIf typeId = etBigInt Then
    getJavaDataType = "java.lang.Long"
  ElseIf typeId = etDecimal Then
    getJavaDataType = "XDECIMAL"
  ElseIf typeId = etDate Then
    getJavaDataType = "date"
  ElseIf typeId = etTime Then
    getJavaDataType = "XTIME"
  ElseIf typeId = etTimestamp Then
    getJavaDataType = "timestamp"
  Else
        getJavaDataType = Null
  End If
End Function


Function getJavaMaxTypeLength( _
  typeId As typeId, maxLength As String _
) As String
  If typeId = etSmallint Then
    getJavaMaxTypeLength = ""
  ElseIf typeId = etVarchar Then
    getJavaMaxTypeLength = ""
  ElseIf typeId = etChar Then
    getJavaMaxTypeLength = maxLength
  ElseIf typeId = etBinChar Then
    getJavaMaxTypeLength = maxLength
  ElseIf typeId = etBlob Then
    getJavaMaxTypeLength = maxLength
  ElseIf typeId = etClob Then
    getJavaMaxTypeLength = maxLength
  ElseIf typeId = etSmallint Then
    getJavaMaxTypeLength = ""
  ElseIf typeId = etInteger Then
    getJavaMaxTypeLength = ""
  ElseIf typeId = etBigInt Then
    getJavaMaxTypeLength = ""
  ElseIf typeId = etDecimal Then
    getJavaMaxTypeLength = ""
  ElseIf typeId = etDate Then
    getJavaMaxTypeLength = ""
  ElseIf typeId = etTime Then
    getJavaMaxTypeLength = maxLength
  ElseIf typeId = etTimestamp Then
    getJavaMaxTypeLength = ""
  Else
        getJavaMaxTypeLength = Null
  End If
End Function


Function getDdlTypeDescr( _
  ddlType As DdlTypeId _
) As String
  If ddlType = edtLdm Then
    getDdlTypeDescr = "LDM"
  ElseIf ddlType = edtPdm Then
    getDdlTypeDescr = "PDM"
  Else
    getDdlTypeDescr = "---"
  End If
End Function



' ### ENDIF IVK ###
Function getActiveLrtOidStrDdl( _
  ddlType As DdlTypeId, _
  ByVal thisOrgIndex As Integer _
) As String
  getActiveLrtOidStrDdl = "COALESCE(RTRIM(" & gc_db2RegVarLrtOid & "), '')"
End Function

