package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M01_LDM_IVK {


// ### IF IVK ###


public static final String gc_valDateOrigin = "'1900-01-01'";
public static final String gc_valTimestampOrigin = "'1900-01-01-00.00.00.000000'";
public static final String gc_valDateEarliest = "'1980-01-01'";
public static final String gc_valDateInfinite = "'2155-12-31'";
public static final String gc_valTimestampInfinite = "'2155-12-31-23.59.59.999999'";


public static final int gc_dfltPrimaryPriceTypeFactory = 1;
public static final String gc_dfltPrimaryPriceTypeOrg = "CAST(NULL AS SMALLINT)";

public static final int gc_dfltPriceSelectionForOverlapFactory = 1;
public static final int gc_dfltPriceSelectionForOverlapOrg = 1;

public static final String gc_xmlObjNameSuffix = "Xml";
public static final String gc_xsdObjNameSuffix = "Xsd";

// ### ENDIF IVK ###


}