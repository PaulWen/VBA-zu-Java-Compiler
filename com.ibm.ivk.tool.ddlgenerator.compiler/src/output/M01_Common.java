package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M01_Common {




public class TvBoolean {
public static final int tvFalse = 0;
public static final int tvTrue = 1;
public static final int tvNull = -1;
}

public class typeId {
public static final int etNone = 0;
public static final int etSmallint = 1;
public static final int etInteger = 2;
public static final int etBigInt = 3;
public static final int etChar = 4;
public static final int etBinChar = 5;
public static final int etVarchar = 6;
public static final int etLongVarchar = 7;
public static final int etBinVarchar = 8;
public static final int etDecimal = 9;
public static final int etFloat = 10;
public static final int etDouble = 11;
public static final int etDate = 12;
public static final int etTime = 13;
public static final int etTimestamp = 14;
public static final int etBlob = 15;
public static final int etClob = 16;
public static final int etBoolean = 17;
}

public class DdlTypeId {
public static final int edtNone = 0;
public static final int edtPdm = 1;
public static final int edtLdm = 2;
}

public class AttrCategory {
public static final int eacRegular = 1;// regular column
public static final int eacCid = 2;// classId column
public static final int eacOid = 4;// primary key column in table
// ### IF IVK ###
public static final int eacPsOid = 8;// PS-tag
public static final int eacPsFormingOid = 16;// PS-forming OID
// ### ENDIF IVK ###
public static final int eacFkOid = 32;// foreign key
// ### IF IVK ###
public static final int eacFkExtPsCopyOid = 64;// PS-forming OID
// ### ENDIF IVK ###
public static final int eacVid = 128;
public static final int eacLrtMeta = 256;// meta attributes for LRT implementation
public static final int eacLangId = 512;// languageId
// ### IF IVK ###
public static final int eacSetProdMeta = 1024;// meta attributes for setting data productive
// ### ENDIF IVK ###
public static final int eacMqtLrtMeta = 2048;// meta attributes for LRT-MQT-tables
// ### IF IVK ###
public static final int eacVirtual = 4096;
public static final int eacGroupId = 8192;
// ### ENDIF IVK ###
public static final int eacChlgMeta = 16384;
// ### IF IVK ###
public static final int eacExpression = 32768;

public static final int eacNational = 65536;
public static final int eacNationalBool = 131072;

public static final int eacNationalEntityMeta = 262144;

// ### ENDIF IVK ###
public static final int eacFkOidParent = 524288;
// ### IF IVK ###
public static final int eacFkOidExpression = 1048576;
public static final int eacFkCountryIdList = 2097152;

public static final int eacFkOidExpElement = 4194304;
// ### ENDIF IVK ###

public static final int eacAhOid = 8388608;

public static final int eacDivOid = 16777216;

// ### IF IVK ###
public static final int eacAnyOid = eacOid |  eacPsOid | eacPsFormingOid | eacFkOid | eacFkExtPsCopyOid | eacFkOidExpElement | eacFkCountryIdList | eacDivOid;
// ### ELSE IVK ###
// eacAnyOid = (eacOid Or eacFkOid)
// ### ENDIF IVK ###
public static final int eacMeta = eacLrtMeta |  eacMqtLrtMeta | eacChlgMeta;
}


public class DdlOutputMode {
public static final int edomNone = 0;
public static final int edomDeclNonLrt = 1;
public static final int edomDeclLrt = 2;

public static final int edomListNonLrt = 4;
public static final int edomListLrt = 8;
public static final int edomListNoLrt = 16;

public static final int edomXsd = 32;

public static final int edomValueNonLrt = 64;
public static final int edomValueLrt = 128;

public static final int edomNoSpecifics = 256;
// ### IF IVK ###
public static final int edomMapHibernate = 512;
public static final int edomMapNoHibernate = 511;
// ### ENDIF IVK ###
public static final int edomCid = 1028;
public static final int edomComment = 2048;
public static final int edomNoDdlComment = 4096;

public static final int edomMqtLrt = 8192;

public static final int edomDefaultValue = 16384;
// ### IF IVK ###
public static final int edomListVirtual = 32768;
public static final int edomValueVirtual = 65536;
public static final int edomValueVirtualNonPersisted = 2097152;
public static final int edomDeclVirtual = 131072;
public static final int edomXmlVirtual = 262144;

public static final int edomVirtualPersisted = 524288;

public static final int edomXml = 1048576;
public static final int edomXref = 4194304;

public static final int edomListExpression = 8388608;
public static final int edomValueExpression = 16777216;
public static final int edomDeclExpression = 33554432;

public static final int edomExpressionRef = 67108864;
public static final int edomExpressionDummy = 134217728;
// ### ENDIF IVK ###
public static final int edomColumnName = 268435456;

public static final int edomLrtPriv = 536870912;

// ### IF IVK ###
public static final int edomDecl = edomDeclLrt |  edomDeclNonLrt | edomDeclExpression;
public static final int edomList = edomListNoLrt |  edomListLrt | edomListNonLrt | edomListExpression;
public static final int edomValue = edomValueLrt |  edomValueNonLrt | edomValueVirtual | edomValueExpression;
// ### ELSE IVK ###
// edomDecl = (edomDeclLrt Or edomDeclNonLrt)
// edomList = (edomListNoLrt Or edomListLrt Or edomListNonLrt)
// edomValue = (edomValueLrt Or edomValueNonLrt)
// ### ENDIF IVK ###

public static final int edomLrt = edomValueLrt |  edomListLrt | edomDeclLrt;
public static final int edomNonLrt = edomValueNonLrt |  edomListNoLrt | edomListNonLrt | edomDeclNonLrt;

// ### IF IVK ###
public static final int edomVirtual = edomListVirtual |  edomValueVirtual | edomDeclVirtual | edomXmlVirtual;
public static final int edomExpression = edomListExpression |  edomValueExpression | edomDeclExpression;

public static final int edomAll = edomDecl |  edomList | edomValue | edomVirtual | edomExpression;
// ### ELSE IVK ###
// edomAll = (edomDecl Or edomList Or edomValue)
// ### ENDIF IVK ###
}

public class DbAliasEntityType {
public static final int edatNone = 0;
public static final int edatTable = 1;
public static final int edatView = 2;
}

public class DbAliasEntityCharacteristics {
public static final int edaecNone = 0;
public static final int edaecLrt = 1;
// ### IF IVK ###
public static final int edaecDeletedObjects = 2;
// ### ENDIF IVK ###
public static final int edaecGen = 4;
public static final int edaecNl = 8;
}

public class RecursionDirection {
public static final int erdDown = 0;
public static final int erdUp = 1;
}


public class SrxTypeId {
public static final int estSr0 = 1;
public static final int estSr1 = 2;
public static final int estNsr1 = 3;
}

public class RelNavigationDirection {
public static final int etLeft = 1;
public static final int etRight = 2;
}

public class RelNavigationMode {
public static final int ernmNone = 0;
public static final int ernmLeft = 1;
public static final int ernmRight = 2;
}

public class DbSpLogMode {
public static final int esplNone = 0;
public static final int esplTable = 1;
public static final int esplFile = 2;
}

// ### IF IVK ###
public class DbUpdateMode {
public static final int eupmNone = 0;
public static final int eupmInsert = 1;
public static final int eupmUpdate = 2;
public static final int eupmDelete = 4;
public static final int eupmAll = eupmInsert |  eupmUpdate | eupmDelete;
}

// ### ENDIF IVK ###

public class LogLevel {
public static final int ellFatal = 1;
public static final int ellError = 2;
public static final int ellWarning = 4;
public static final int ellFixableWarning = 8;
public static final int ellInfo = 16;
}

public static final int gc_allocBlockSize = 50;

// ############################################

public static final String langDfltSuffix = "DFLT";

// ################################################

public static final int phaseRegularTables = 0;// entity tables, keys & indexes
public static final int phaseFksRelTabs = 200;// relationship tables, FKs, NL
public static final int phaseCoreSupport = 600;// SP Logging etc.
public static final int phaseModuleMeta = 700;// Module-specific support for meta functions
public static final int phaseLrt = 1000;// LRT-Views ???
public static final int phaseLrtViews = 1500;// LRT-Views
public static final int phaseLrtSupport = 3000;// LRT-Trigger, -SPs
public static final int phaseChangeLogViews = 2000;// ChangeLog-Views
public static final int phaseLrtMqt = 3000;// LRT-MQT Support
public static final int phaseLogChange = 5000;// <logChange>-Views
public static final int phaseDbSupport = 7000;// Misc DB Support
public static final int phaseDbSupport2 = 8000;// Misc DB Support
public static final int phaseAliases = 9000;// Aliases

// ### IF IVK ###
public static final int phaseGaSyncSupport = 1800;// Misceleaneous support for GroupId-Syncronization
public static final int phaseVirtAttr = 3000;// Virtual Attribute Support
public static final int phaseGroupId = 3000;// Group-ID Attribute Support
public static final int phaseArchive = 4000;// Archive
public static final int phasePsTagging = 5000;// PS-Tagging (Views)
public static final int phaseXmlExport = 6000;// XML-Export-Views
public static final int phaseDataCompare = 7000;// Support for Data Comparison
public static final int phaseUseCases = 8000;// PS-Copy, FactoryTakeOver, Set Productive
// ### ENDIF IVK ###

public static final int ldmIterationGlobal = 0;
public static final int ldmIterationPoolSpecific = 1;
public static final int ldmIterationPostProc = 2;

public static final int seqNoDigits = 4;
public static final int stepDigits = 2;
public static final int maxProcessingStep = 6;

public static boolean isInitialized;


}