 Attribute VB_Name = "M89_TechData"
 ' ### IF IVK ###
 Option Explicit
 
 Private Const processingStep = 5
 
 
 Sub genTechDataSupDdl( _
   ddlType As DdlTypeId _
 )
   Dim thisOrgIndex As Integer
   Dim thisPoolIndex As Integer

   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtLdm Then
     gengenTechDataSupByPool(edtLdm)
   ElseIf ddlType = edtPdm Then
     For thisOrgIndex = 1 To g_orgs.numDescriptors Step 1
       For thisPoolIndex = 1 To g_pools.numDescriptors Step 1
           If poolIsValidForOrg(thisPoolIndex, thisOrgIndex) And g_pools.descriptors(thisPoolIndex).supportAcm And Not g_pools.descriptors(thisPoolIndex).isArchive Then
             gengenTechDataSupByPool(edtPdm, thisOrgIndex, thisPoolIndex)
           End If
       Next thisPoolIndex
     Next thisOrgIndex
   End If
 End Sub
 
 
 Private Sub gengenTechDataSupByPool( _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByVal thisOrgIndex As Integer = -1, _
   Optional ByVal thisPoolIndex As Integer = -1 _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtPdm And (thisOrgIndex < 1 Or thisPoolIndex < 1) Then
     ' only supported at 'pool-level'
     Exit Sub
   End If

   If thisPoolIndex > 0 And thisPoolIndex <> g_workDataPoolIndex Then
     Exit Sub
   End If

   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexStaging, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)

   Dim qualFuncName As String
   Dim qualProcName As String
 
   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameTechDataDeltaImport As String
   qualTabNameTechDataDeltaImport = genQualTabNameByClassIndex(g_classIndexTechDataDeltaImport, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameGenericCode As String
   qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameEndSlotGen As String
   qualTabNameEndSlotGen = genQualTabNameByClassIndex(g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, True)

   Dim qualTabNameEndSlotGenNl As String
   qualTabNameEndSlotGenNl = genQualNlTabNameByClassIndex(g_classIndexEndSlot, ddlType, thisOrgIndex, thisPoolIndex, True)

   Dim qualTabNameAggregationSlotGen As String
   qualTabNameAggregationSlotGen = genQualTabNameByClassIndex(g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, True)

   Dim qualTabNameAggregationSlotGenNl As String
   qualTabNameAggregationSlotGenNl = genQualNlTabNameByClassIndex(g_classIndexAggregationSlot, ddlType, thisOrgIndex, thisPoolIndex, True)

   Dim qualTabNamePropertyGen As String
   qualTabNamePropertyGen = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, True)

   Dim qualTabNamePropertyGenNl As String
   qualTabNamePropertyGenNl = genQualNlTabNameByClassIndex(g_classIndexProperty, ddlType, thisOrgIndex, thisPoolIndex, True)

   Dim sr0ValidityClassIdStr As String
   sr0ValidityClassIdStr = getClassIdStrByIndex(g_classIndexSr0Validity)

   Dim qualFuncNameSparte2DivOid As String
   qualFuncNameSparte2DivOid = genQualFuncName(g_sectionIndexMeta, udfnSparte2DivOid, ddlType, , , , , , True)

   ' ####################################################################################################################
   ' #    Procedure retrieving the BM attribute with undefined baumuster for TECHDATADELTAIMPORT
   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexAliasLrt, "UDBM4TDDI", ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("Procedure retrieving the BM attribute with undefined baumuster for TECHDATADELTAIMPORT", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "v_timestamp", "TIMESTAMP", False)
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.DeletableTDDI"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "sparte CHAR(1),"
   Print #fileNo, addTab(2); "baumuster VARCHAR(8)"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer(fileNo, 1, True)

   Print #fileNo,
   Print #fileNo, addTab(1); "FOR tdLoop AS"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "TD.SPARTE AS v_sparte,"
   Print #fileNo, addTab(3); "TD.BAUMUSTER AS v_baumuster"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameTechDataDeltaImport; " TD"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "TD.FILETIMESTAMP = v_timestamp"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(1); "DO"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); "SESSION.DeletableTDDI"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "sparte,"
   Print #fileNo, addTab(3); "baumuster"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "v_sparte,"
   Print #fileNo, addTab(3); "v_baumuster"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameGenericAspect; " GA"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "GA."; g_anCid; " = '09003'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "EXISTS (SELECT 1 FROM "; g_qualTabNamePsDpMapping; " M WHERE M.DPSPARTE = v_sparte AND M.PSOID = GA."; g_anPsOid; ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "GA.BAUMUSTER LIKE v_baumuster"
   Print #fileNo, addTab(2); "HAVING COUNT(*) = 0"
   Print #fileNo, addTab(2); "WITH UR;"
   Print #fileNo, addTab(1); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(1); "-- return result to application"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "DECLARE logCursor CURSOR WITH RETURN FOR"
   Print #fileNo, addTab(3); "SELECT DISTINCT"
   Print #fileNo, addTab(4); "sparte,"
   Print #fileNo, addTab(4); "baumuster"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SESSION.DeletableTDDI"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "sparte,"
   Print #fileNo, addTab(4); "baumuster"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "-- leave cursor open for application"
   Print #fileNo, addTab(2); "OPEN logCursor;"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    UDF retrieving the BEI attribute with undefined code for TECHDATADELTAIMPORT
   ' ####################################################################################################################
   qualFuncName = genQualFuncName(g_sectionIndexAliasLrt, "UDBEI4TDDI", ddlType, thisOrgIndex, thisPoolIndex, , , , True)
   printSectionHeader("Function retrieving the BEI attribute with undefined code for TECHDATADELTAIMPORT", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", False)
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS TABLE"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SPARTE  CHAR(1),"
   Print #fileNo, addTab(2); "BEI     VARCHAR(752),"
   Print #fileNo, addTab(2); "CODE    VARCHAR(22)"
   Print #fileNo, addTab(1); ")"
 
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "BEI_ELEMENTS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "LEVEL,"
   Print #fileNo, addTab(2); "SPARTE,"
   Print #fileNo, addTab(2); "CODE,"
   Print #fileNo, addTab(2); "REST,"
   Print #fileNo, addTab(2); "BEI_TEXT"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "CAST(1 AS INTEGER),"
   Print #fileNo, addTab(4); "TD.SPARTE,"
   Print #fileNo, addTab(4); "'#',"
   Print #fileNo, addTab(4); "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(RTRIM(TD.BEI), '+-', '|'), '+', '|'), '/', '|'), ',', '|'), '!', '|'),"
   Print #fileNo, addTab(4); "TD.BEI"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameTechDataDeltaImport; " TD"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "TD.BEI IS NOT NULL"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "TD.FILETIMESTAMP = v_timestamp"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(3); "UNION ALL"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "LEVEL + 1,"
   Print #fileNo, addTab(4); "SPARTE,"
   Print #fileNo, addTab(4); "CASE LOCATE('|', REST)"
   Print #fileNo, addTab(5); "WHEN 0 THEN REST"
   Print #fileNo, addTab(5); "WHEN 1 THEN"
   Print #fileNo, addTab(5); "         CASE LOCATE('|', REST, 2)"
   Print #fileNo, addTab(5); "           WHEN 0 THEN LTRIM(RTRIM(SUBSTR(REST, 2)))"
   Print #fileNo, addTab(5); "                  ELSE LTRIM(RTRIM(SUBSTR(REST, 2, LOCATE('|', REST, 2)-2)))"
   Print #fileNo, addTab(5); "         END"
   Print #fileNo, addTab(5); "       ELSE LTRIM(RTRIM(SUBSTR(REST, 1, LOCATE('|', REST)-1)))"
   Print #fileNo, addTab(4); "END,"
   Print #fileNo, addTab(4); "CASE LOCATE('|', REST)"
   Print #fileNo, addTab(5); "WHEN 0 THEN ''"
   Print #fileNo, addTab(5); "WHEN 1 THEN"
   Print #fileNo, addTab(5); "         CASE LOCATE('|', REST, 2)"
   Print #fileNo, addTab(5); "           WHEN 0 THEN LTRIM(RTRIM(SUBSTR(REST, 2)))"
   Print #fileNo, addTab(5); "                  ELSE LTRIM(RTRIM(SUBSTR(REST, LOCATE('|', REST, 2)+1, LENGTH(REST))))"
   Print #fileNo, addTab(5); "         END"
   Print #fileNo, addTab(5); "       ELSE LTRIM(RTRIM(SUBSTR(REST, LOCATE('|', REST)+1, LENGTH(REST))))"
   Print #fileNo, addTab(4); "END,"
   Print #fileNo, addTab(4); "BEI_TEXT"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "BEI_ELEMENTS E"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "LENGTH(REST) > 0"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LEVEL < 100000"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); "),"
 
   Print #fileNo, addTab(2); "BEI_ELEMENTS_DISTINCT"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SPARTE,"
   Print #fileNo, addTab(2); "BEI,"
   Print #fileNo, addTab(2); "CODE"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "SPARTE,"
   Print #fileNo, addTab(3); "BEI_TEXT,"
   Print #fileNo, addTab(3); "CASE WHEN LOCATE('-', CODE) > 0 THEN SUBSTR(CODE, LOCATE('-', CODE)+1) ELSE CODE END"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "BEI_ELEMENTS"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "CODE <> '#'"
   Print #fileNo, addTab(1); ")"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "*"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "BEI_ELEMENTS_DISTINCT B"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NOT EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameGenericCode; " C"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "C.CDIDIV_OID = "; qualFuncNameSparte2DivOid; "(B.SPARTE)"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "C."; g_anCodeNumber; " = B.CODE"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Function retrieving the PROPERTYNAME attribute with undefined property for TECHDATADELTAIMPORT
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexAliasLrt, "UDP4TDDI", ddlType, thisOrgIndex, thisPoolIndex, , , , True)
   printSectionHeader("Function retrieving the PROPERTYNAME attribute with undefined property for TECHDATADELTAIMPORT", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", False)
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS TABLE"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SPARTE       CHAR(1),"
   Print #fileNo, addTab(2); "PROPERTYNAME VARCHAR(256)"
   Print #fileNo, addTab(1); ")"
 
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "PROPERTY_ELEMENTS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SPARTE,"
   Print #fileNo, addTab(2); "PROPERTY"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "(SELECT M.DPSPARTE FROM "; g_qualTabNamePsDpMapping; " M WHERE M.PSOID = PR."; g_anPsOid; "),"
   Print #fileNo, addTab(3); "PRNL.LABEL"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNamePropertyGenNl; " PRNL"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); qualTabNamePropertyGen; " PR"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PR."; g_anOid; " = PRNL.PRP_OID"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "PRNL."; g_anLanguageId; " = 1"
   Print #fileNo, addTab(1); ")"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "TD.SPARTE,"
   Print #fileNo, addTab(2); "TD.PROPERTYNAME"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameTechDataDeltaImport; " TD"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NOT EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "PROPERTY_ELEMENTS PE"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "TD.SPARTE = PE.SPARTE"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "TD.PROPERTYNAME = PE.PROPERTY"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    UDF retrieving the CODE attribute with undefined code for TECHDATADELTAIMPORT
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexAliasLrt, "UDC4TDDI", ddlType, thisOrgIndex, thisPoolIndex, , , , True)
   printSectionHeader("Function retrieving the CODE attribute with undefined code for TECHDATADELTAIMPORT", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", False)
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS TABLE"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SPARTE CHAR(1),"
   Print #fileNo, addTab(2); "CODE   "; g_dbtCodeNumber
   Print #fileNo, addTab(1); ")"
 
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "TD.SPARTE,"
   Print #fileNo, addTab(2); "TD.CODE"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(3); qualTabNameTechDataDeltaImport; " TD"
 
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NOT EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameGenericCode; " C"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "C.CDIDIV_OID = "; qualFuncNameSparte2DivOid; "(TD.SPARTE)"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(4); "C."; g_anCodeNumber; " = TD.CODE"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.CODE IS NOT NULL"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.FILETIMESTAMP = v_timestamp"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    UDF retrieving the SLOTNAME attribute with undefined endslots for TECHDATADELTAIMPORT
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexAliasLrt, "UDES4TDDI", ddlType, thisOrgIndex, thisPoolIndex, , , , True)
   printSectionHeader("Function retrieving the SLOTNAME attribute with undefined endslots for TECHDATADELTAIMPORT", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", False)
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS TABLE"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SPARTE   CHAR(1),"
   Print #fileNo, addTab(2); "SLOTNAME VARCHAR(256)"
   Print #fileNo, addTab(1); ")"
 
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "SLOT_ELEMENTS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SPARTE,"
   Print #fileNo, addTab(2); "SLOT"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "(SELECT M.DPSPARTE FROM "; g_qualTabNamePsDpMapping; " M WHERE M.PSOID = ESL."; g_anPsOid; "),"
   Print #fileNo, addTab(3); "ESLNL.LABEL"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameEndSlotGenNl; " ESLNL"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); qualTabNameEndSlotGen; " ESL"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "ESL."; g_anOid; " = ESLNL.ESL_OID"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "ESLNL."; g_anLanguageId; " = 1"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "TD.SPARTE,"
   Print #fileNo, addTab(2); "TD.SLOTNAME"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameTechDataDeltaImport; " TD"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NOT EXISTS (SELECT 1 FROM SLOT_ELEMENTS SE WHERE TD.SPARTE = SE.SPARTE AND TD.SLOTNAME = SE.SLOT)"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.CODE IS NOT NULL"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.FILETIMESTAMP = v_timestamp"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    UDF retrieving the SLOTNAME attribute with undefined aggregationslots for TECHDATADELTAIMPORT
   ' ####################################################################################################################
 
   qualFuncName = genQualFuncName(g_sectionIndexAliasLrt, "UDAS4TDDI", ddlType, thisOrgIndex, thisPoolIndex, , , , True)
   printSectionHeader("Function retrieving the SLOTNAME attribute with undefined aggregationslots for TECHDATADELTAIMPORT", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE FUNCTION"
   Print #fileNo, addTab(1); qualFuncName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "", "v_timestamp", "TIMESTAMP", False)
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RETURNS TABLE"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SPARTE   CHAR(1),"
   Print #fileNo, addTab(2); "SLOTNAME VARCHAR(256)"
   Print #fileNo, addTab(1); ")"
 
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "DETERMINISTIC"
   Print #fileNo, addTab(0); "NO EXTERNAL ACTION"
   Print #fileNo, addTab(0); "RETURN"
 
   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "SLOT_ELEMENTS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SPARTE,"
   Print #fileNo, addTab(2); "SLOT"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "(SELECT M.DPSPARTE FROM "; g_qualTabNamePsDpMapping; " M WHERE M.PSOID = ASL."; g_anPsOid; "),"
   Print #fileNo, addTab(3); "ASLNL.LABEL"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameAggregationSlotGenNl; " ASLNL"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); qualTabNameAggregationSlotGen; " ASL"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "ASL."; g_anOid; " = ASLNL.ASL_OID"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "ASLNL."; g_anLanguageId; " = 1"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "TD.SPARTE,"
   Print #fileNo, addTab(2); "TD.SLOTNAME"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(3); qualTabNameTechDataDeltaImport; " TD"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NOT EXISTS (SELECT 1 FROM SLOT_ELEMENTS SE WHERE TD.SPARTE = SE.SPARTE AND TD.SLOTNAME = SE.SLOT)"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.CODE IS NULL"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.FILETIMESTAMP = v_timestamp"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Procedure for 'rolling back' a TechDataDelta-Import
   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexAliasLrt, "TDDIROLLBACK", ddlType, thisOrgIndex, thisPoolIndex)
 
   printSectionHeader("SP for 'rolling back' a TechDataDelta-Import", fileNo)
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm(fileNo, "IN", "timestamp_in", "TIMESTAMP", True)
   genProcParm(fileNo, "IN", "sparte_in", "VARCHAR(1)", False)
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader(fileNo, "delete records related to the specified timestamp and sparte")
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); qualTabNameTechDataDeltaImport; " TD"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "TD.FILETIMESTAMP = timestamp_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.SPARTE = sparte_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.PROPERTYVALUEOLD IS NULL"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.VALUEGATHERINGOLD IS NULL"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader(fileNo, "rollback changes of PROPERTYVALUE and VALUEGATHERIN related to the specified timestamp and sparte")
   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); qualTabNameTechDataDeltaImport; " TD"
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); "TD.PROPERTYVALUE = TD.PROPERTYVALUEOLD,"
   Print #fileNo, addTab(2); "TD.VALUEGATHERING = TD.VALUEGATHERINGOLD,"
   Print #fileNo, addTab(2); "TD."; g_anLastUpdateTimestamp; " = CURRENT TIMESTAMP"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "TD.FILETIMESTAMP = timestamp_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "TD.SPARTE = sparte_in"
   Print #fileNo, addTab(1); ";"
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Procedure for 'rolling back' individual rows for TechDataDelta-Import
   ' ####################################################################################################################
   qualProcName = genQualProcName(g_sectionIndexAliasLrt, "TDDIDELETEROW", ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim genCountryParams As Boolean
   Dim i As Integer
   For i = 1 To 2
     genCountryParams = (i = 2)
     printSectionHeader("SP for 'rolling back' individual rows for TechDataDelta-Import", fileNo)
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcName
     Print #fileNo, addTab(0); "("
     genProcParm(fileNo, "IN", "timestamp_in", "TIMESTAMP", True)
     genProcParm(fileNo, "IN", "sparte_in", "VARCHAR(1)", True)
     genProcParm(fileNo, "IN", "baumuster_in", "VARCHAR(8)", True)
     genProcParm(fileNo, "IN", "bei_in", "VARCHAR(752)", True)
     If genCountryParams Then
       genProcParm(fileNo, "IN", "land_in", "VARCHAR(3)", True)
       genProcParm(fileNo, "IN", "ausserland_in", "VARCHAR(600)", True)
     End If
     genProcParm(fileNo, "IN", "gueltig_ab_in", "DATE", True)
     genProcParm(fileNo, "IN", "gueltig_bis_in", "DATE", True)
     genProcParm(fileNo, "IN", "property_in", "VARCHAR(256)", True)
     genProcParm(fileNo, "IN", "slot_in", "VARCHAR(256)", True)
     genProcParm(fileNo, "IN", "code_in", g_dbtCodeNumber, False)
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader(fileNo, "delete records related to the specified parameters", 1, True)
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); qualTabNameTechDataDeltaImport; " TD"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "TD.FILETIMESTAMP = timestamp_in"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "TD.SPARTE = sparte_in"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "((TD.BAUMUSTER IS NULL AND baumuster_in IS NULL) OR (TD.BAUMUSTER = baumuster_in))"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "((TD.BEI IS NULL AND bei_in IS NULL) OR (TD.BEI = bei_in))"
     If genCountryParams Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "((TD.LAND IS NULL AND land_in IS NULL) OR (TD.LAND = land_in))"
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "((TD.AUSSERLAND IS NULL AND ausserland_in IS NULL) OR (TD.AUSSERLAND = ausserland_in))"
     End If
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "((TD.GUELTIGAB IS NULL AND gueltig_ab_in IS NULL) OR (TD.GUELTIGAB = gueltig_ab_in))"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "((TD.GUELTIGBIS IS NULL AND gueltig_bis_in IS NULL) OR (TD.GUELTIGBIS = gueltig_bis_in))"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "((TD.PROPERTYNAME IS NULL AND property_in IS NULL) OR (TD.PROPERTYNAME = property_in))"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "((TD.SLOTNAME IS NULL AND slot_in IS NULL) OR (TD.SLOTNAME = slot_in))"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "COALESCE(TD.CODE,'') = COALESCE(code_in, '')"
     Print #fileNo, addTab(1); ";"
 
     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next i

 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 ' ### ENDIF IVK ###
