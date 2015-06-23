 Attribute VB_Name = "M03_Config"
 Option Explicit
 
 Enum ConfigMode
   ecfgTest = 1
   ecfgProductionEw = 2
   ecfgDelivery = 3
 End Enum
 
 '''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
 ' Configurable settings
 Global g_cfgSqlStateStart As Long
 Global g_cfgSqlMsgPrefix As String
 
 Global g_cfgGenLogChangeForLrtTabs As Boolean
 Global g_cfgGenLogChangeForNlTabs  As Boolean
 Global g_cfgGenLogChangeForGenTabs As Boolean
 
 Global g_cfgLrtGenDB2View As Boolean
 Global g_cfgLrtGenDB2Trigger As Boolean
 
 Global productKey As String
 Global versionString As String
 Global targetPlatform As String
 Global environmentIds() As String
 Global entityFilterKeys As String
 Global hiddenWorksheetSuffixes As String
 Global snapshotApiVersion As String
 Global supportSpLogging As Boolean
 Global generateSpLogMessages As Boolean
 Global spLogMode As DbSpLogMode
 Global spLogAutonomousTransaction As Boolean
 Global supportIndexMetrics As Boolean
 Global supportCompresionEstimation As Boolean
 Global workSheetSuffix As String
 Global generateFwkTest As Boolean
 Global supportVirtualColumns As Boolean
 Global virtualColumnSyncCommitCount As Long
 Global supportGroupIdColumns As Boolean
 Global maintainGroupIdColumnsInLrtTrigger As Boolean
 Global maintainGroupIdColumnsInLrtCommit As Boolean
 Global maintainGroupIdColumnsInSetProductive As Boolean
 Global generateDdlHeader As Boolean
 ' ### IF IVK ###
 Global generatePsTaggingView As Boolean
 Global supportFilteringByPsDpMapping As Boolean
 Global usePsFltrByDpMappingForRegularViews As Boolean
 Global generatePsTaggingTrigger As Boolean
 Global generatePsCopySupport As Boolean
 Global generatePsCopyExtendedSupport As Boolean
 Global generatePsCreateSupport As Boolean
 Global generateExpCopySupport As Boolean
 ' ### ENDIF IVK ###
 Global generateLogChangeView As Boolean
 Global reuseRelationships As Boolean
 Global supportArchivePool As Boolean
 Global generateArchiveView As Boolean
 Global generateLdm As Boolean
 Global formatLdmForWord As Boolean
 Global generatePdm As Boolean
 Global pdmSchemaNamePattern As String
 Global generateLrt As Boolean
 Global generateNonLrt As Boolean
 Global generateDeployPackage As Boolean
 Global generateUpdatePackage As Boolean
 Global includeExplainDdlInDeliveryPackage As Boolean
 Global bindJdbcPackagesWithReoptAlways As Boolean
 Global setDefaultCfgDuringDeployment As Boolean
 
 Global generateDdlCreateTable As Boolean
 Global generateDdlCreateIndex As Boolean
 Global generateDdlCreatePK As Boolean
 Global generateDdlCreateFK As Boolean
 Global generateDdlCreateSeq As Boolean
 Global exportVBCode As Boolean
 Global exportXlsSheets As Boolean
 Global includeUtilityScrptsinPackage As Boolean
 
 Global generateUpdatableCheckInUpdateTrigger As Boolean
 Global generateDb2RegistryCheckInSps As Boolean
 Global generateCommentOnTables As Boolean
 Global generateCommentOnColumns As Boolean
 Global generateCommentOnAliases As Boolean
 Global generateLrtSps As Boolean
 ' ### IF IVK ###
 Global lrtLogRetrieveSr0CodesFromSr0Context As Boolean
 Global genSupportForHibernate As Boolean
 ' ### ENDIF IVK ###
 Global generateAhIdsNotNull As Boolean
 Global disableLoggingDuringSync As Boolean
 Global supportUnicode As Boolean
 Global unicodeExpansionFactor As Single
 Global generateEntityIdList As Boolean
 ' ### IF IVK ###
 Global generateXmlExportSupport As Boolean
 Global generateXmlXsdFuncs As Boolean
 Global generateXmlExportFuncs As Boolean
 Global generateXmlExportViews As Boolean
 Global generateXsdInCtoSchema As Boolean
 Global xmlExportVirtualColumns As Boolean
 Global xmlExportColumnInLrt As Boolean
 Global xmlExportColumnClassId As Boolean
 Global xmlExportColumnVersionId As Boolean
 Global generateXmlPsOidColForPsTaggedEntities As Boolean
 ' ### ENDIF IVK ###
 Global dbCompressSystemDefaults As Boolean
 Global dbCompressValues As Boolean
 Global dbCompressValuesInNlsTabs As Boolean
 Global dbCompressValuesInEnumTabs As Boolean
 ' ### IF IVK ###
 Global maxXmlExportStringLength As String
 ' ### ENDIF IVK ###
 Global generateIndexOnFk As Boolean
 Global generateIndexOnLrtTabs As Boolean
 Global generateIndexOnFkForNLang As Boolean
 ' ### IF IVK ###
 Global generateIndexOnFkForPsTag As Boolean
 ' ### ENDIF IVK ###
 Global generateIndexOnFkForEnums As Boolean
 Global generateIndexOnFkForLrtId As Boolean
 Global generateIndexOnClassId As Boolean
 Global generateIndexOnValidFromUntil As Boolean
 Global generateIndexOnValidFrom As Boolean
 Global generateIndexOnValidUntil As Boolean
 Global generateIndexOnAhClassIdOid As Boolean
 Global generateIndexOnAhClassIdOidStatus As Boolean
 Global generateIndexOnAhOid As Boolean
 ' ### IF IVK ###
 Global generateIndexOnExpressionFks As Boolean
 Global generateIndexForSetProductive As Boolean
 Global generateStatusCheckDdl As Boolean
 ' ### ENDIF IVK ###
 Global useSurrogateKeysForNMRelationships As Boolean
 Global reuseColumnsInTabsForOrMapping As Boolean
 ' ### IF IVK ###
 Global generateSupportForUc304 As Boolean
 Global hasBeenSetProductiveInPrivLrt As Boolean
 ' ### ENDIF IVK ###
 Global useMqtToImplementLrt As Boolean
 Global activateLrtMqtViews As Boolean
 Global implementLrtNonMqtViewsForEntitiesSupportingMqts As Boolean
 Global includeTermStringsInMqt As Boolean
 Global numRetriesRunstatsRebindOnLockTimeout As Integer
 ' fixme: this is disabled because of a bug - this feature is not supported yet
 Global lrtDistinguishGenAndNlTextTabsInAffectedEntities As Boolean
 Global maintainVirtAttrInTriggerOnRelTabs As Boolean
 Global maintainVirtAttrInTriggerPubOnRelTabs As Boolean
 Global maintainVirtAttrInTriggerOnEntityTabs As Boolean
 Global maintainVirtAttrInTriggerPubOnEntityTabs As Boolean
 Global maintainVirtAttrInTriggerPrivOnEntityTabs As Boolean
 
 Global lrtTablesVolatile As Boolean
 
 Global navToAggHeadForClAttrs As Boolean
 Global cr132 As Boolean
 
 Global genDataCheckCl As Boolean
 Global supportSimulationSps As Boolean
 Global genTemplateDdl As Boolean
 ' ### IF IVK ###
 Global supportSstCheck As Boolean
 Global supportSectionDataFix As Boolean
 Global resolveCountryIdListInChangeLog As Boolean
 Global lrtCommitDeleteDeletedNonProductiveRecords As Boolean
 Global ftoLockSingleObjectProcessing As Boolean
 ' ### ENDIF IVK ###
 Global genFksForLrtOnRelationships As Boolean
 ' ### IF IVK ###
 Global genTimeStampsDuringOrgInit As Boolean
 Global listRangePartitionTablesByPsOid As String
 Global supportRangePartitioningByPsOid As Boolean
 Global listRangePartitionTablesByDivOid As String
 Global supportRangePartitioningByDivOid As Boolean
 Global supportRangePartitioningByClassId As Boolean
 Global supportRangePartitioningByClassIdFirstPsOid As Boolean
 Global usePsTagInNlTextTables As Boolean
 Global partitionLrtPrivateWhenMqt As Boolean
 Global partitionLrtPublicWhenMqt As Boolean
 Global partitionLrtPrivateWhenNoMqt As Boolean
 Global partitionLrtPublicWhenNoMqt As Boolean
 Global noPartitioningInDataPools As String
 Global supportCtsConfigByTemplate As Boolean
 Global supportAddTestUser As Boolean
 ' ### ENDIF IVK ###
 Global supportDbCompact As Boolean
 Global supportColumnIsInstantiatedInAcmAttribute As Boolean
 
 ' Global settings
 Global Const ignoreUnknownSections = True
 Global Const genIndexesForAcmClasses = True
 Global Const includeFksInPks = True
 ' ### IF IVK ###
 Global Const reusePsTagForRelationships = False
 Global Const nationalFlagPartOfPK = False
 ' ### ENDIF IVK ###
 Global Const supportNlForRelationships = True
 ' ### IF IVK ###
 Global Const supportAliasDelForNonLrtPools = False
 ' ### ENDIF IVK ###
 Global Const referToAggHeadInChangeLog = True
 
 
 Private Const colCategory = 2
 Private Const colSubCategory = colCategory + 1
 Private Const colKey = colSubCategory + 1
 Private Const colParameter = colKey + 1
 Private Const colSetting = colParameter + 1
 Private Const colEffectiveSetting = colSetting + 1
 
 Private Const colIrregularSettingTest = 10
 Private Const colIrregularSettingProductionEw = colIrregularSettingTest + 5
 Private Const colIrregularSettingDelivery = colIrregularSettingProductionEw + 5
 
 Private Const colEffectiveSettingFwkTest = colEffectiveSetting
 Private Const colEffectiveSettingProductionEw = colEffectiveSettingFwkTest + 5
 Private Const colEffectiveSettingDelivery = colEffectiveSettingProductionEw + 5
 
 Private Const firstRow = 4
 
 Private Const sheetName = "Config"

 Global Const configSheetName = sheetName
 
 Private Function getColSettingIrreg( _
   Optional cfgMode As ConfigMode = ecfgTest _
 ) As Integer
   Select Case cfgMode
   Case ecfgTest
     getColSettingIrreg = colIrregularSettingTest
   Case ecfgProductionEw
     getColSettingIrreg = colIrregularSettingProductionEw
   Case ecfgDelivery
     getColSettingIrreg = colIrregularSettingDelivery
   Case Else
         getColSettingIrreg = Null
   End Select
 End Function
 
 
 Private Function getColEffectiveSetting( _
   Optional cfgMode As ConfigMode = ecfgTest _
 ) As Integer
   Dim offset As Integer

   Select Case cfgMode
   Case ecfgTest
     getColEffectiveSetting = colEffectiveSetting
   Case ecfgProductionEw
     getColEffectiveSetting = colEffectiveSettingProductionEw
   Case ecfgDelivery
     getColEffectiveSetting = colEffectiveSettingDelivery
   Case Else
         getColEffectiveSetting = Null
   End Select
 End Function
 
 
 Function irregularSetting( _
   Optional cfgMode As ConfigMode = ecfgTest _
 ) As Boolean
         Dim rowOffset As Integer
         rowOffset = IIf(ActiveWorkbook.Worksheets(sheetName).Cells(1, 1) = "", 0, 1)
     irregularSetting = CStr(ActiveWorkbook.Worksheets(sheetName).Cells(firstRow - 1 + rowOffset, getColSettingIrreg(cfgMode))) <> "0"
 End Function
 
 Sub readConfig( _
   Optional cfgMode As ConfigMode = ecfgTest, _
   Optional silent As Boolean = False _
 )
   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Worksheets(sheetName)
   Dim thisRow As Integer
   thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)

   Dim colSetting As Integer
   colSetting = getColEffectiveSetting(cfgMode)

   Dim environmentIdsStr As String

   Dim key As String, setting As String, parameter As String
   While thisSheet.Cells(thisRow, colKey) & "" <> "" Or thisSheet.Cells(thisRow + 1, colKey) & "" <> ""
     key = Trim(thisSheet.Cells(thisRow, colKey))
     setting = thisSheet.Cells(thisRow, colSetting)
     parameter = IIf(silent, "", thisSheet.Cells(thisRow, colParameter))

     If key = "GPKY" Then
       productKey = setting
     ElseIf key = "GVER" Then
       versionString = setting
     ElseIf key = "GWSS" Then
       workSheetSuffix = setting
     ElseIf key = "GSQS" Then
       g_cfgSqlStateStart = getLong(setting, 79000)
     ElseIf key = "GTGP" Then
       targetPlatform = setting
     ElseIf key = "GENV" Then
       environmentIdsStr = Replace(setting, " ", "")
       genAttrList(environmentIds, environmentIdsStr)
     ElseIf key = "GEFK" Then
       entityFilterKeys = Replace(setting, " ", "")
     ElseIf key = "GEPR" Then
       g_cfgSqlMsgPrefix = Trim(setting) & " "
     ElseIf key = "GDBV" Then
       snapshotApiVersion = Replace(setting, " ", "")
     ElseIf key = "GHWS" Then
       hiddenWorksheetSuffixes = Trim(setting)
     ElseIf key = "GSPL" Then
       supportSpLogging = getBoolean(setting)
     ElseIf key = "GGSL" Then
       generateSpLogMessages = getBoolean(setting)
     ElseIf key = "SPLA" Then
       spLogAutonomousTransaction = getBoolean(setting)
     ElseIf key = "SPLM" Then
       spLogMode = getDbSpLogMode(setting)
     ElseIf key = "GSIM" Then
       supportIndexMetrics = getBoolean(setting)
     ElseIf key = "GSCE" Then
       supportCompresionEstimation = getBoolean(setting)
     ElseIf key = "GFWK" Then
       generateFwkTest = getBoolean(setting)
     ElseIf key = "GSVC" Then
       supportVirtualColumns = getBoolean(setting)
     ElseIf key = "GCCC" Then
       virtualColumnSyncCommitCount = getLong(setting, -1)
     ElseIf key = "GGID" Then
       supportGroupIdColumns = getBoolean(setting)
     ElseIf key = "MGID" Then
       maintainGroupIdColumnsInLrtTrigger = getBoolean(setting)
     ElseIf key = "MGIC" Then
       maintainGroupIdColumnsInLrtCommit = getBoolean(setting)
     ElseIf key = "MGIS" Then
       maintainGroupIdColumnsInSetProductive = getBoolean(setting)
     ElseIf key = "GSKR" Then
       useSurrogateKeysForNMRelationships = getBoolean(setting)
     ElseIf key = "GRCO" Then
       reuseColumnsInTabsForOrMapping = getBoolean(setting)
 ' ### IF IVK ###
     ElseIf key = "GHPL" Then
       hasBeenSetProductiveInPrivLrt = getBoolean(setting)
 ' ### ENDIF IVK ###
     ElseIf key = "GDHD" Then
       generateDdlHeader = getBoolean(setting)
     ElseIf key = "GCTD" Then
       generateCommentOnTables = getBoolean(setting)
     ElseIf key = "GCCD" Then
       generateCommentOnColumns = getBoolean(setting)
     ElseIf key = "GCAD" Then
       generateCommentOnAliases = getBoolean(setting)
     ElseIf key = "GCIL" Then
       generateEntityIdList = getBoolean(setting)
     ElseIf key = "GRER" Then
       reuseRelationships = getBoolean(setting)
     ElseIf key = "GRCK" Then
       generateDb2RegistryCheckInSps = getBoolean(setting)
     ElseIf key = "GUCH" Then
       generateUpdatableCheckInUpdateTrigger = getBoolean(setting)
     ElseIf key = "GCLN" Then
       g_cfgGenLogChangeForNlTabs = getBoolean(setting)
     ElseIf key = "GCLG" Then
       g_cfgGenLogChangeForGenTabs = getBoolean(setting)
     ElseIf key = "DCSD" Then
       dbCompressSystemDefaults = getBoolean(setting)
     ElseIf key = "DCVA" Then
       dbCompressValues = getBoolean(setting)
     ElseIf key = "DCVN" Then
       dbCompressValuesInNlsTabs = getBoolean(setting)
     ElseIf key = "DCVE" Then
       dbCompressValuesInEnumTabs = getBoolean(setting)
     ElseIf key = "AHNN" Then
       generateAhIdsNotNull = getBoolean(setting)
     ElseIf key = "GDSM" Then
       disableLoggingDuringSync = getBoolean(setting)
     ElseIf key = "GDLP" Then
       generateDeployPackage = getBoolean(setting)
     ElseIf key = "GUPP" Then
       generateUpdatePackage = getBoolean(setting)
     ElseIf key = "DEXP" Then
       includeExplainDdlInDeliveryPackage = getBoolean(setting)
     ElseIf key = "DBJO" Then
       bindJdbcPackagesWithReoptAlways = getBoolean(setting)
     ElseIf key = "DCFD" Then
       setDefaultCfgDuringDeployment = getBoolean(setting)
     ElseIf key = "DDLT" Then
       generateDdlCreateTable = getBoolean(setting)
     ElseIf key = "DDLI" Then
       generateDdlCreateIndex = getBoolean(setting)
     ElseIf key = "DDLP" Then
       generateDdlCreatePK = getBoolean(setting)
     ElseIf key = "DDLF" Then
       generateDdlCreateFK = getBoolean(setting)
     ElseIf key = "DDLS" Then
       generateDdlCreateSeq = getBoolean(setting)
     ElseIf key = "IUSC" Then
       includeUtilityScrptsinPackage = getBoolean(setting)
     ElseIf key = "EVBC" Then
       exportVBCode = getBoolean(setting)
     ElseIf key = "ESHE" Then
       exportXlsSheets = getBoolean(setting)
     ElseIf key = "LINL" Then
       generateNonLrt = getBoolean(setting)
     ElseIf key = "LILR" Then
       generateLrt = getBoolean(setting)
     ElseIf key = "LMQT" Then
       useMqtToImplementLrt = getBoolean(setting)
     ElseIf key = "LMQV" Then
       activateLrtMqtViews = getBoolean(setting)
     ElseIf key = "LMNM" Then
       implementLrtNonMqtViewsForEntitiesSupportingMqts = getBoolean(setting)
     ElseIf key = "LMTS" Then
       includeTermStringsInMqt = getBoolean(setting)
     ElseIf key = "LVOL" Then
       lrtTablesVolatile = getBoolean(setting)
 ' ### IF IVK ###
     ElseIf key = "LRSC" Then
       lrtLogRetrieveSr0CodesFromSr0Context = getBoolean(setting)
 ' ### ENDIF IVK ###
     ElseIf key = "LILA" Then
       g_cfgGenLogChangeForLrtTabs = getBoolean(setting)
     ElseIf key = "LIDV" Then
       g_cfgLrtGenDB2View = getBoolean(setting)
     ElseIf key = "LIDT" Then
       g_cfgLrtGenDB2Trigger = getBoolean(setting)
     ElseIf key = "LISP" Then
       generateLrtSps = getBoolean(setting)
 ' ### IF IVK ###
     ElseIf key = "LGSC" Then
       generateStatusCheckDdl = getBoolean(setting)
 ' ### ENDIF IVK ###
     ElseIf key = "LDTT" Then
       lrtDistinguishGenAndNlTextTabsInAffectedEntities = getBoolean(setting)
     ElseIf key = "MVTR" Then
       maintainVirtAttrInTriggerOnRelTabs = getBoolean(setting)
     ElseIf key = "MVPU" Then
       maintainVirtAttrInTriggerPubOnRelTabs = getBoolean(setting)
     ElseIf key = "MVEN" Then
       maintainVirtAttrInTriggerOnEntityTabs = getBoolean(setting)
     ElseIf key = "MVEU" Then
       maintainVirtAttrInTriggerPubOnEntityTabs = getBoolean(setting)
     ElseIf key = "MVER" Then
       maintainVirtAttrInTriggerPrivOnEntityTabs = getBoolean(setting)
 ' ### IF IVK ###
     ElseIf key = "HGCF" Then
       genSupportForHibernate = getBoolean(setting)
 ' ### ENDIF IVK ###
     ElseIf key = "LILD" Then
       generateLdm = getBoolean(setting)
     ElseIf key = "LFWR" Then
       formatLdmForWord = getBoolean(setting)
     ElseIf key = "PIPD" Then
       generatePdm = getBoolean(setting)
     ElseIf key = "PSNP" Then
       pdmSchemaNamePattern = Trim(setting)
     ElseIf key = "PILT" Then
       generateIndexOnLrtTabs = getBoolean(setting)
     ElseIf key = "PIFK" Then
       generateIndexOnFk = getBoolean(setting)
     ElseIf key = "PIFN" Then
       generateIndexOnFkForNLang = getBoolean(setting)
 ' ### IF IVK ###
     ElseIf key = "PIFP" Then
       generateIndexOnFkForPsTag = getBoolean(setting)
 ' ### ENDIF IVK ###
     ElseIf key = "PIFE" Then
       generateIndexOnFkForEnums = getBoolean(setting)
     ElseIf key = "PIFL" Then
       generateIndexOnFkForLrtId = getBoolean(setting)
     ElseIf key = "PICI" Then
       generateIndexOnClassId = getBoolean(setting)
     ElseIf key = "PIVA" Then
       generateIndexOnValidFromUntil = getBoolean(setting)
     ElseIf key = "PIVF" Then
       generateIndexOnValidFrom = getBoolean(setting)
     ElseIf key = "PIVU" Then
       generateIndexOnValidUntil = getBoolean(setting)
     ElseIf key = "PNRR" Then
       numRetriesRunstatsRebindOnLockTimeout = getInteger(setting)
     ElseIf key = "PIAH" Then
       generateIndexOnAhClassIdOid = getBoolean(setting)
     ElseIf key = "PIAS" Then
       generateIndexOnAhClassIdOidStatus = getBoolean(setting)
     ElseIf key = "PIAO" Then
       generateIndexOnAhOid = getBoolean(setting)
 ' ### IF IVK ###
     ElseIf key = "PIEX" Then
       generateIndexOnExpressionFks = getBoolean(setting)
     ElseIf key = "PISP" Then
       generateIndexForSetProductive = getBoolean(setting)
     ElseIf key = "PSGV" Then
       generatePsTaggingView = getBoolean(setting)
     ElseIf key = "PSFD" Then
       supportFilteringByPsDpMapping = getBoolean(setting)
     ElseIf key = "PSFU" Then
       usePsFltrByDpMappingForRegularViews = getBoolean(setting)
 ' ### ENDIF IVK ###
     ElseIf key = "LCGV" Then
       generateLogChangeView = getBoolean(setting)
 ' ### IF IVK ###
     ElseIf key = "U304" Then
       generateSupportForUc304 = getBoolean(setting)
     ElseIf key = "ARCP" Then
       supportArchivePool = getBoolean(setting)
     ElseIf key = "ARCV" Then
       generateArchiveView = getBoolean(setting)
     ElseIf key = "PSGT" Then
       generatePsTaggingTrigger = getBoolean(setting)
     ElseIf key = "PSCP" Then
       generatePsCopySupport = getBoolean(setting)
     ElseIf key = "PSCX" Then
       generatePsCopyExtendedSupport = getBoolean(setting)
     ElseIf key = "EXCP" Then
       generateExpCopySupport = getBoolean(setting)
     ElseIf key = "PSCR" Then
       generatePsCreateSupport = getBoolean(setting)
 ' ### ENDIF IVK ###
     ElseIf key = "UNCD" Then
       supportUnicode = getBoolean(setting)
     ElseIf key = "UCEF" Then
       unicodeExpansionFactor = getSingle(setting)
 ' ### IF IVK ###
     ElseIf key = "XEXP" Then
       generateXmlExportSupport = getBoolean(setting)
     ElseIf key = "XEXX" Then
       generateXmlXsdFuncs = getBoolean(setting)
     ElseIf key = "XEXV" Then
       generateXmlExportViews = getBoolean(setting)
     ElseIf key = "XEXF" Then
       generateXmlExportFuncs = getBoolean(setting)
     ElseIf key = "XEVC" Then
       xmlExportVirtualColumns = getBoolean(setting)
     ElseIf key = "XCIL" Then
       xmlExportColumnInLrt = getBoolean(setting)
     ElseIf key = "XCCI" Then
       xmlExportColumnClassId = getBoolean(setting)
     ElseIf key = "XCVI" Then
       xmlExportColumnVersionId = getBoolean(setting)
     ElseIf key = "XVCS" Then
       generateXsdInCtoSchema = getBoolean(setting)
     ElseIf key = "XEXV" Then
       generateXmlExportViews = getBoolean(setting)
     ElseIf key = "XVPT" Then
       generateXmlPsOidColForPsTaggedEntities = getBoolean(setting)
     ElseIf key = "XMSL" Then
       maxXmlExportStringLength = setting
     ElseIf key = "CR132" Then
       cr132 = getBoolean(setting)
 ' ### ENDIF IVK ###
     ElseIf key = "GDCC" Then
       genDataCheckCl = getBoolean(setting)
     ElseIf key = "SSSP" Then
       supportSimulationSps = getBoolean(setting)
     ElseIf key = "GTDD" Then
       genTemplateDdl = getBoolean(setting, parameter)
 ' ### IF IVK ###
     ElseIf key = "SSCH" Then
       supportSstCheck = getBoolean(setting, parameter)
     ElseIf key = "SSDF" Then
       supportSectionDataFix = getBoolean(setting, parameter)
     ElseIf key = "RCLC" Then
       resolveCountryIdListInChangeLog = getBoolean(setting, parameter)
     ElseIf key = "CDDD" Then
       lrtCommitDeleteDeletedNonProductiveRecords = getBoolean(setting, parameter)
     ElseIf key = "FSOP" Then
       ftoLockSingleObjectProcessing = getBoolean(setting, parameter)
 ' ### ENDIF IVK ###
     ElseIf key = "GLFR" Then
       genFksForLrtOnRelationships = getBoolean(setting, parameter)
 ' ### IF IVK ###
     ElseIf key = "SRPP" Then
       listRangePartitionTablesByPsOid = Replace(Replace(setting, ".", ","), " ", "")
       supportRangePartitioningByPsOid = listRangePartitionTablesByPsOid <> ""
     ElseIf key = "OIGT" Then
       genTimeStampsDuringOrgInit = getBoolean(setting, parameter)
     ElseIf key = "SRPD" Then
       listRangePartitionTablesByDivOid = Replace(Replace(setting, ".", ","), " ", "")
       supportRangePartitioningByDivOid = listRangePartitionTablesByDivOid <> ""
     ElseIf key = "SRPC" Then
       supportRangePartitioningByClassId = getBoolean(setting, parameter)
     ElseIf key = "SR1P" Then
       supportRangePartitioningByClassIdFirstPsOid = getBoolean(setting, parameter)
     ElseIf key = "PTNL" Then
       usePsTagInNlTextTables = getBoolean(setting, parameter)
     ElseIf key = "PPUM" Then
       partitionLrtPublicWhenMqt = getBoolean(setting, parameter)
     ElseIf key = "PPRM" Then
       partitionLrtPrivateWhenMqt = getBoolean(setting, parameter)
     ElseIf key = "PPUV" Then
       partitionLrtPublicWhenNoMqt = getBoolean(setting, parameter)
     ElseIf key = "PPRV" Then
       partitionLrtPrivateWhenNoMqt = getBoolean(setting, parameter)
     ElseIf key = "NPDP" Then
       noPartitioningInDataPools = Replace(setting, " ", "")
     ElseIf key = "SCCT" Then
       supportCtsConfigByTemplate = getBoolean(setting, parameter)
     ElseIf key = "SATU" Then
       supportAddTestUser = getBoolean(setting, parameter)
 ' ### ENDIF IVK ###
     ElseIf key = "SDBC" Then
       supportDbCompact = getBoolean(setting, parameter)
     ElseIf key = "AARE" Then
       supportColumnIsInstantiatedInAcmAttribute = getBoolean(setting, parameter)
     End If
     thisRow = thisRow + 1
   Wend
 
   If spLogMode <> esplTable Then
     entityFilterKeys = entityFilterKeys & ",L"
   End If
   If Not supportIndexMetrics Then
     entityFilterKeys = entityFilterKeys & ",IM"
   End If
 ' ### IF IVK ###
   If supportSectionDataFix Then
     entityFilterKeys = entityFilterKeys & ",d"
   Else
     entityFilterKeys = entityFilterKeys & ",D"
   End If
 ' ### ENDIF IVK ###
   If snapshotApiVersion = "8" Then
     entityFilterKeys = entityFilterKeys & ",S9"
   ElseIf Replace(Left(snapshotApiVersion, 3), ",", ".") = "9.7" Then
     entityFilterKeys = entityFilterKeys & ",S8"
   End If
 ' ### IF IVK ###
   If supportSstCheck Then
     entityFilterKeys = entityFilterKeys & ",x"
   Else
     entityFilterKeys = entityFilterKeys & ",X"
   End If
 ' ### ENDIF IVK ###
   If supportColumnIsInstantiatedInAcmAttribute Then
     entityFilterKeys = entityFilterKeys & ",r"
   Else
     entityFilterKeys = entityFilterKeys & ",R"
   End If
   If Not InStr(1, "," & environmentIdsStr & ",", ",T,") Then
     entityFilterKeys = entityFilterKeys & ",TE"
   End If
 End Sub
 
