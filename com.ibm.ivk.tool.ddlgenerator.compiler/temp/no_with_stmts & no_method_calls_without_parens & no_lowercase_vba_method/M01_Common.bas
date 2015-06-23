 Attribute VB_Name = "M01_Common"
 Option Explicit
 
 Enum TvBoolean
   tvFalse = 0
   tvTrue = 1
   tvNull = -1
 End Enum

 Enum typeId
   etNone = 0
   etSmallint = 1
   etInteger = 2
   etBigInt = 3
   etChar = 4
   etBinChar = 5
   etVarchar = 6
   etLongVarchar = 7
   etBinVarchar = 8
   etDecimal = 9
   etFloat = 10
   etDouble = 11
   etDate = 12
   etTime = 13
   etTimestamp = 14
   etBlob = 15
   etClob = 16
   etBoolean = 17
 End Enum
 
 Enum DdlTypeId
   edtNone = 0
   edtPdm = 1
   edtLdm = 2
 End Enum
 
 Enum AttrCategory
   eacRegular = 1         ' regular column
   eacCid = 2             ' classId column
   eacOid = 4             ' primary key column in table
 ' ### IF IVK ###
   eacPsOid = 8           ' PS-tag
   eacPsFormingOid = 16   ' PS-forming OID
 ' ### ENDIF IVK ###
   eacFkOid = 32          ' foreign key
 ' ### IF IVK ###
   eacFkExtPsCopyOid = 64 ' PS-forming OID
 ' ### ENDIF IVK ###
   eacVid = 128
   eacLrtMeta = 256       ' meta attributes for LRT implementation
   eacLangId = 512        ' languageId
 ' ### IF IVK ###
   eacSetProdMeta = 1024  ' meta attributes for setting data productive
 ' ### ENDIF IVK ###
   eacMqtLrtMeta = 2048   ' meta attributes for LRT-MQT-tables
 ' ### IF IVK ###
   eacVirtual = 4096
   eacGroupId = 8192
 ' ### ENDIF IVK ###
   eacChlgMeta = 16384
 ' ### IF IVK ###
   eacExpression = 32768

   eacNational = 65536
   eacNationalBool = 131072
 
   eacNationalEntityMeta = 262144

 ' ### ENDIF IVK ###
   eacFkOidParent = 524288
 ' ### IF IVK ###
   eacFkOidExpression = 1048576
   eacFkCountryIdList = 2097152

   eacFkOidExpElement = 4194304
 ' ### ENDIF IVK ###

   eacAhOid = 8388608

   eacDivOid = 16777216
 
 ' ### IF IVK ###
   eacAnyOid = (eacOid Or eacPsOid Or eacPsFormingOid Or eacFkOid Or eacFkExtPsCopyOid Or eacFkOidExpElement Or eacFkCountryIdList Or eacDivOid)
 ' ### ELSE IVK ###
 ' eacAnyOid = (eacOid Or eacFkOid)
 ' ### ENDIF IVK ###
   eacMeta = (eacLrtMeta Or eacMqtLrtMeta Or eacChlgMeta)
 End Enum
 
 
 Enum DdlOutputMode
   edomNone = 0
   edomDeclNonLrt = 1
   edomDeclLrt = 2

   edomListNonLrt = 4
   edomListLrt = 8
   edomListNoLrt = 16

   edomXsd = 32

   edomValueNonLrt = 64
   edomValueLrt = 128

   edomNoSpecifics = 256
 ' ### IF IVK ###
   edomMapHibernate = 512
   edomMapNoHibernate = 511
 ' ### ENDIF IVK ###
   edomCid = 1028
   edomComment = 2048
   edomNoDdlComment = 4096

   edomMqtLrt = 8192

   edomDefaultValue = 16384
 ' ### IF IVK ###
   edomListVirtual = 32768
   edomValueVirtual = 65536
   edomValueVirtualNonPersisted = 2097152
   edomDeclVirtual = 131072
   edomXmlVirtual = 262144

   edomVirtualPersisted = 524288

   edomXml = 1048576
   edomXref = 4194304

   edomListExpression = 8388608
   edomValueExpression = 16777216
   edomDeclExpression = 33554432

   edomExpressionRef = 67108864
   edomExpressionDummy = 134217728
 ' ### ENDIF IVK ###
   edomColumnName = 268435456
 
   edomLrtPriv = 536870912
 
 ' ### IF IVK ###
   edomDecl = (edomDeclLrt Or edomDeclNonLrt Or edomDeclExpression)
   edomList = (edomListNoLrt Or edomListLrt Or edomListNonLrt Or edomListExpression)
   edomValue = (edomValueLrt Or edomValueNonLrt Or edomValueVirtual Or edomValueExpression)
 ' ### ELSE IVK ###
 ' edomDecl = (edomDeclLrt Or edomDeclNonLrt)
 ' edomList = (edomListNoLrt Or edomListLrt Or edomListNonLrt)
 ' edomValue = (edomValueLrt Or edomValueNonLrt)
 ' ### ENDIF IVK ###

   edomLrt = (edomValueLrt Or edomListLrt Or edomDeclLrt)
   edomNonLrt = (edomValueNonLrt Or edomListNoLrt Or edomListNonLrt Or edomDeclNonLrt)

 ' ### IF IVK ###
   edomVirtual = (edomListVirtual Or edomValueVirtual Or edomDeclVirtual Or edomXmlVirtual)
   edomExpression = (edomListExpression Or edomValueExpression Or edomDeclExpression)

   edomAll = (edomDecl Or edomList Or edomValue Or edomVirtual Or edomExpression)
 ' ### ELSE IVK ###
 ' edomAll = (edomDecl Or edomList Or edomValue)
 ' ### ENDIF IVK ###
 End Enum
 
 Enum DbAliasEntityType
   edatNone = 0
   edatTable = 1
   edatView = 2
 End Enum
 
 Enum DbAliasEntityCharacteristics
   edaecNone = 0
   edaecLrt = 1
 ' ### IF IVK ###
   edaecDeletedObjects = 2
 ' ### ENDIF IVK ###
   edaecGen = 4
   edaecNl = 8
 End Enum
 
 Enum RecursionDirection
   erdDown = 0
   erdUp = 1
 End Enum
 
 
 Enum SrxTypeId
   estSr0 = 1
   estSr1 = 2
   estNsr1 = 3
 End Enum
 
 Enum RelNavigationDirection
   etLeft = 1
   etRight = 2
 End Enum
 
 Enum RelNavigationMode
   ernmNone = 0
   ernmLeft = 1
   ernmRight = 2
 End Enum
 
 Enum DbSpLogMode
   esplNone = 0
   esplTable = 1
   esplFile = 2
 End Enum
 
 ' ### IF IVK ###
 Enum DbUpdateMode
   eupmNone = 0
   eupmInsert = 1
   eupmUpdate = 2
   eupmDelete = 4
   eupmAll = (eupmInsert Or eupmUpdate Or eupmDelete)
 End Enum
 
 ' ### ENDIF IVK ###
 
 Enum LogLevel
   ellFatal = 1
   ellError = 2
   ellWarning = 4
   ellFixableWarning = 8
   ellInfo = 16
 End Enum
 
 Global Const gc_allocBlockSize = 50
 
 ' ############################################
 
 Global Const langDfltSuffix = "DFLT"
 
 ' ################################################
 
 Global Const phaseRegularTables = 0      ' entity tables, keys & indexes
 Global Const phaseFksRelTabs = 200 ' relationship tables, FKs, NL
 Global Const phaseCoreSupport = 600   ' SP Logging etc.
 Global Const phaseModuleMeta = 700 ' Module-specific support for meta functions
 Global Const phaseLrt = 1000 ' LRT-Views ???
 Global Const phaseLrtViews = 1500 ' LRT-Views
 Global Const phaseLrtSupport = 3000 ' LRT-Trigger, -SPs
 Global Const phaseChangeLogViews = 2000 ' ChangeLog-Views
 Global Const phaseLrtMqt = 3000 ' LRT-MQT Support
 Global Const phaseLogChange = 5000 ' <logChange>-Views
 Global Const phaseDbSupport = 7000 ' Misc DB Support
 Global Const phaseDbSupport2 = 8000 ' Misc DB Support
 Global Const phaseAliases = 9000 ' Aliases
 
 ' ### IF IVK ###
 Global Const phaseGaSyncSupport = 1800 ' Misceleaneous support for GroupId-Syncronization
 Global Const phaseVirtAttr = 3000 ' Virtual Attribute Support
 Global Const phaseGroupId = 3000 ' Group-ID Attribute Support
 Global Const phaseArchive = 4000 ' Archive
 Global Const phasePsTagging = 5000 ' PS-Tagging (Views)
 Global Const phaseXmlExport = 6000 ' XML-Export-Views
 Global Const phaseDataCompare = 7000 ' Support for Data Comparison
 Global Const phaseUseCases = 8000 ' PS-Copy, FactoryTakeOver, Set Productive
 ' ### ENDIF IVK ###
 
 Global Const ldmIterationGlobal = 0
 Global Const ldmIterationPoolSpecific = 1
 Global Const ldmIterationPostProc = 2
 
 Global Const seqNoDigits = 4
 Global Const stepDigits = 2
 Global Const maxProcessingStep = 6
 
 Global isInitialized As Boolean
 
