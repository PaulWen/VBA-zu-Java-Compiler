package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M01_PDM_IVK {


// ### IF IVK ###


// ############################################
// # dynamic registry keys
// ############################################

public static final String gc_regDynamicSectionMdsDb = "MDSDB";

public static final String gc_regDynamicSectionAutoSetProd = M01_PDM_IVK.gc_regDynamicSectionMdsDb;
public static final String gc_regDynamicKeyAutoSetProd = "PASP";

public static final String gc_regDynamicSectionCodeWithoutDependencies = M01_PDM_IVK.gc_regDynamicSectionMdsDb;
public static final String gc_regDynamicKeyCodeWithoutDependencies = "CWDP";

// ### ENDIF IVK ###


}