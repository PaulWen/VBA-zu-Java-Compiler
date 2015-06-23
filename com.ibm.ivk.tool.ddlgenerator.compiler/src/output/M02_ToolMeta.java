package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M02_ToolMeta {




public static Integer getDataTypeId(String typeStr) {
Integer returnValue;
if (typeStr.toUpperCase() == "BOOLEAN") {
returnValue = M01_Common.typeId.etBoolean;
} else if (typeStr.toUpperCase() == "SMALLINT") {
returnValue = M01_Common.typeId.etSmallint;
} else if (typeStr.toUpperCase() == "VARCHAR") {
returnValue = M01_Common.typeId.etVarchar;
} else if (typeStr.toUpperCase() == "LONG VARCHAR") {
returnValue = M01_Common.typeId.etLongVarchar;
} else if (typeStr.toUpperCase() == "VARCHAR FOR BIT DATA") {
returnValue = M01_Common.typeId.etBinVarchar;
} else if (typeStr.toUpperCase() == "CHAR") {
returnValue = M01_Common.typeId.etChar;
} else if (typeStr.toUpperCase() == "CHAR FOR BIT DATA") {
returnValue = M01_Common.typeId.etBinChar;
} else if (typeStr.toUpperCase() == "BLOB") {
returnValue = M01_Common.typeId.etBlob;
} else if (typeStr.toUpperCase() == "CLOB") {
returnValue = M01_Common.typeId.etClob;
} else if (typeStr.toUpperCase() == "INTEGER") {
returnValue = M01_Common.typeId.etInteger;
} else if (typeStr.toUpperCase() == "BIGINT") {
returnValue = M01_Common.typeId.etBigInt;
} else if (typeStr.toUpperCase() == "FLOAT") {
returnValue = M01_Common.typeId.etFloat;
} else if (typeStr.toUpperCase() == "DECIMAL") {
returnValue = M01_Common.typeId.etDecimal;
} else if (typeStr.toUpperCase() == "DATE") {
returnValue = M01_Common.typeId.etDate;
} else if (typeStr.toUpperCase() == "TIME") {
returnValue = M01_Common.typeId.etTime;
} else if (typeStr.toUpperCase() == "TIMESTAMP") {
returnValue = M01_Common.typeId.etTimestamp;
} else if (typeStr.toUpperCase() == "DOUBLE") {
returnValue = M01_Common.typeId.etDouble;
} else {
returnValue = null;
}
return returnValue;
}


public static String getDataType(Integer Integer, String maxLengthW, Integer precW, Boolean useUnicodeW, Double expansionFactorW) {
String maxLength; 
if (maxLengthW == null) {
maxLength = "";
} else {
maxLength = maxLengthW;
}

int prec; 
if (precW == null) {
prec = -1;
} else {
prec = precW;
}

boolean useUnicode; 
if (useUnicodeW == null) {
useUnicode = false;
} else {
useUnicode = useUnicodeW;
}

double expansionFactor; 
if (expansionFactorW == null) {
expansionFactor = -1;
} else {
expansionFactor = expansionFactorW;
}

String returnValue;
String effectiveMaxLength;
effectiveMaxLength = maxLength;

if (!(maxLength.compareTo("") == 0)) {
if (M03_Config.supportUnicode &  useUnicode) {
effectiveMaxLength = new Double((expansionFactor >= 1 ? expansionFactor : M03_Config.unicodeExpansionFactor) * new Double(maxLength).intValue()).intValue() + "";
} else {
effectiveMaxLength = maxLength;
}
}

String specific;
specific = (!(maxLength.compareTo("") == 0) ? "(" + effectiveMaxLength + (prec > 0 ? "," + prec : "") + ")" : "");

if (Integer == M01_Common.typeId.etBoolean) {
returnValue = "SMALLINT";
} else if (Integer == M01_Common.typeId.etSmallint) {
returnValue = "SMALLINT";
} else if (Integer == M01_Common.typeId.etVarchar) {
returnValue = "VARCHAR" + specific;
} else if (Integer == M01_Common.typeId.etLongVarchar) {
returnValue = "LONG VARCHAR" + specific;
} else if (Integer == M01_Common.typeId.etBinVarchar) {
returnValue = "VARCHAR" + specific + " FOR BIT DATA";
} else if (Integer == M01_Common.typeId.etChar) {
returnValue = "CHAR" + specific;
} else if (Integer == M01_Common.typeId.etBinChar) {
returnValue = "CHAR" + specific + " FOR BIT DATA";
} else if (Integer == M01_Common.typeId.etBlob) {
returnValue = "BLOB" + specific;
} else if (Integer == M01_Common.typeId.etClob) {
returnValue = "CLOB" + specific;
} else if (Integer == M01_Common.typeId.etInteger) {
returnValue = "INTEGER";
} else if (Integer == M01_Common.typeId.etBigInt) {
returnValue = "BIGINT";
} else if (Integer == M01_Common.typeId.etDecimal) {
returnValue = "DECIMAL" + specific;
} else if (Integer == M01_Common.typeId.etFloat) {
returnValue = "FLOAT";
} else if (Integer == M01_Common.typeId.etDate) {
returnValue = "DATE";
} else if (Integer == M01_Common.typeId.etTime) {
returnValue = "TIME";
} else if (Integer == M01_Common.typeId.etTimestamp) {
returnValue = "TIMESTAMP";
} else if (Integer == M01_Common.typeId.etDouble) {
returnValue = "DOUBLE";
} else {
returnValue = null;
}
return returnValue;
}


public static String getDataTypeByDomainIndex(int domainIndex, Boolean useUnicodeW) {
boolean useUnicode; 
if (useUnicodeW == null) {
useUnicode = false;
} else {
useUnicode = useUnicodeW;
}

String returnValue;
returnValue = M02_ToolMeta.getDataType(M25_Domain.g_domains.descriptors[domainIndex].dataType, M25_Domain.g_domains.descriptors[domainIndex].maxLength, M25_Domain.g_domains.descriptors[domainIndex].scale, useUnicode, M25_Domain.g_domains.descriptors[domainIndex].unicodeExpansionFactor);
return returnValue;
}


public static String getJavaDataType(Integer Integer) {
String returnValue;
if (Integer == M01_Common.typeId.etSmallint) {
returnValue = "XSMALLINT";
} else if (Integer == M01_Common.typeId.etVarchar) {
returnValue = "string";
} else if (Integer == M01_Common.typeId.etChar) {
returnValue = "string";
} else if (Integer == M01_Common.typeId.etBinChar) {
returnValue = "XCLOB";
} else if (Integer == M01_Common.typeId.etBlob) {
returnValue = "XBLOB";
} else if (Integer == M01_Common.typeId.etClob) {
returnValue = "XCLOB";
} else if (Integer == M01_Common.typeId.etInteger) {
returnValue = "java.lang.Integer";
} else if (Integer == M01_Common.typeId.etBigInt) {
returnValue = "java.lang.Long";
} else if (Integer == M01_Common.typeId.etDecimal) {
returnValue = "XDECIMAL";
} else if (Integer == M01_Common.typeId.etDate) {
returnValue = "date";
} else if (Integer == M01_Common.typeId.etTime) {
returnValue = "XTIME";
} else if (Integer == M01_Common.typeId.etTimestamp) {
returnValue = "timestamp";
} else {
returnValue = null;
}
return returnValue;
}


public static String getJavaMaxTypeLength(Integer Integer, String maxLength) {
String returnValue;
if (Integer == M01_Common.typeId.etSmallint) {
returnValue = "";
} else if (Integer == M01_Common.typeId.etVarchar) {
returnValue = "";
} else if (Integer == M01_Common.typeId.etChar) {
returnValue = maxLength;
} else if (Integer == M01_Common.typeId.etBinChar) {
returnValue = maxLength;
} else if (Integer == M01_Common.typeId.etBlob) {
returnValue = maxLength;
} else if (Integer == M01_Common.typeId.etClob) {
returnValue = maxLength;
} else if (Integer == M01_Common.typeId.etSmallint) {
returnValue = "";
} else if (Integer == M01_Common.typeId.etInteger) {
returnValue = "";
} else if (Integer == M01_Common.typeId.etBigInt) {
returnValue = "";
} else if (Integer == M01_Common.typeId.etDecimal) {
returnValue = "";
} else if (Integer == M01_Common.typeId.etDate) {
returnValue = "";
} else if (Integer == M01_Common.typeId.etTime) {
returnValue = maxLength;
} else if (Integer == M01_Common.typeId.etTimestamp) {
returnValue = "";
} else {
returnValue = null;
}
return returnValue;
}


public static String getDdlTypeDescr(Integer ddlType) {
String returnValue;
if (ddlType == M01_Common.DdlTypeId.edtLdm) {
returnValue = "LDM";
} else if (ddlType == M01_Common.DdlTypeId.edtPdm) {
returnValue = "PDM";
} else {
returnValue = "---";
}
return returnValue;
}



// ### ENDIF IVK ###
public static String getActiveLrtOidStrDdl(Integer ddlType,  int thisOrgIndex) {
String returnValue;
returnValue = "COALESCE(RTRIM(" + M01_LDM.gc_db2RegVarLrtOid + "), '')";
return returnValue;
}


}