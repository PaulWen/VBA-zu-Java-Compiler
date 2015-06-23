 Attribute VB_Name = "M87_FactoryTakeOver"
 ' ### IF IVK ###
 Option Explicit
 
 Global Const tempEnpOidTabName = "SESSION.EnpOids"
 Global Const tempEbpTypePriceTabName = "SESSION.EbpTypePrice"
 Global Const tempEnpTypePriceTabName = "SESSION.EnpTypePrice"
 Global Const tempTabNameSr0ContextFac = "SESSION.Sr0ContextFac"
 Global Const tempTabNameSr0ContextOrg = "SESSION.Sr0ContextMpc"
 
 Global Const propertyTemplateIdEbp = 105
 Global Const propertyTemplateIdEnp = 107
 Global Const propertyTemplateIdListCalcPrice = "60,61,101,102"
 
 Private Const processingStep = 5
 Sub genInsertSessionConflictMultiGa( _
     fileNo As Integer _
 )
 
 genProcSectionHeader fileNo, "Split conflicts to seperate rows and insert to common session table"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.Conflict"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrName,"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "seqOid,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "plrOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'exclusionFormulaFactory',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "seqOid,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "plrOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGa"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrExclusionFormulaFactory= 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'inclusionFormulaFactory',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "seqOid,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "plrOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGa"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrInclusionFormulaFactory= 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'conclusionFactory',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "seqOid,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "plrOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGa"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrConclusionFactory= 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'numValue',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "seqOid,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "plrOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGa"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrNumValue= 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'valueGathering',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "seqOid,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "plrOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGa"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrValueGathering= 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'boolValue',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "seqOid,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "plrOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGa"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrBoolValue= 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'expression',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "seqOid,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "plrOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGa"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrExpression= 1"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
 
 End Sub
 
 Sub genFactoryTakeOverDdl( _
   ddlType As DdlTypeId _
 )
   If generateFwkTest Or Not g_genLrtSupport Then
     Exit Sub
   End If

   If ddlType = edtPdm Then
     Dim srcOrgIndex As Integer
     Dim srcPoolIndex As Integer
     Dim dstPoolIndex As Integer

     srcOrgIndex = g_primaryOrgIndex
     srcPoolIndex = g_productiveDataPoolIndex
     dstPoolIndex = g_workDataPoolIndex

     genFactoryTakeOverDdlCommon edtPdm

     Dim dstOrgIndex As Integer
     For dstOrgIndex = 1 To g_orgs.numDescriptors Step 1
       If Not g_orgs.descriptors(dstOrgIndex).isPrimary Then
         genFactoryTakeOverDdlByOrg srcOrgIndex, dstOrgIndex, srcPoolIndex, dstPoolIndex, edtPdm
         genFactoryTakeOverDdlByOrg2 srcOrgIndex, dstOrgIndex, srcPoolIndex, dstPoolIndex, edtPdm
         genFactoryTakeOverDdlByOrg3 srcOrgIndex, dstOrgIndex, srcPoolIndex, dstPoolIndex, edtPdm
         genFactoryTakeOverDdlByOrg4 srcOrgIndex, dstOrgIndex, srcPoolIndex, dstPoolIndex, edtPdm
         genFactoryTakeOverPriceConflictHandling dstOrgIndex, dstPoolIndex, edtPdm
         genFactoryTakeOverExtendedConflictHandling dstOrgIndex, dstPoolIndex, edtPdm
       End If
     Next dstOrgIndex
   End If
 End Sub
 
 
 Sub genDdlForTempEnpOids( _
   fileNo As Integer, _
   Optional indent As Integer = 1, _
   Optional withReplace As Boolean = False, _
   Optional onCommitPreserve As Boolean = False, _
   Optional onRollbackPreserve As Boolean = False _
 )
   genProcSectionHeader fileNo, "temporary table for ENP-OIDs", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempEnpOidTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "EBP_OID         "; g_dbtOid; " NOT NULL,"
   Print #fileNo, addTab(indent + 1); "ENP_OID         "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "ENPNEW_OID      "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "isDeleted       "; g_dbtBoolean; " DEFAULT 0"
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer fileNo, indent, withReplace, onCommitPreserve, onRollbackPreserve
 End Sub
 
 
 Private Sub genDdlForTempTypePriceAspects( _
   fileNo As Integer, _
   Optional indent As Integer = 1 _
 )
   genProcSectionHeader fileNo, "temporary table for EBP Type Prices", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempEbpTypePriceTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid                        "; g_dbtOid; " NOT NULL,"
   Print #fileNo, addTab(indent + 1); "validFrom                  DATE,"
   Print #fileNo, addTab(indent + 1); "validTo                    DATE,"
   Print #fileNo, addTab(indent + 1); "allowedCountryIdListOid    "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "disallowedCountryIdListOid "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "sr0CtxtOidList             VARCHAR(220),"
   Print #fileNo, addTab(indent + 1); "sr1CtxtOidList             VARCHAR(220)"
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer fileNo, indent, True
 
   genProcSectionHeader fileNo, "temporary table for ENP Type Prices", indent
   Print #fileNo, addTab(indent + 0); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(indent + 1); tempEnpTypePriceTabName
   Print #fileNo, addTab(indent + 0); "("
   Print #fileNo, addTab(indent + 1); "oid                        "; g_dbtOid; " NOT NULL,"
   Print #fileNo, addTab(indent + 1); "validFrom                  DATE,"
   Print #fileNo, addTab(indent + 1); "validTo                    DATE,"
   Print #fileNo, addTab(indent + 1); "allowedCountryIdListOid    "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "disallowedCountryIdListOid "; g_dbtOid; ","
   Print #fileNo, addTab(indent + 1); "sr0CtxtOidList             VARCHAR(220),"
   Print #fileNo, addTab(indent + 1); "sr1CtxtOidList             VARCHAR(220)"
   Print #fileNo, addTab(indent + 0); ")"
   genDdlForTempTableDeclTrailer fileNo, indent, True
 End Sub
 
 
 Private Sub genFactoryTakeOverDdlCommon( _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, , , , phaseUseCases, ldmIterationPoolSpecific)
 
   ' ####################################################################################################################
   ' #    View determining the tables corresponding to aggregate heads
   ' ####################################################################################################################

   Dim qualViewNameAggHead As String
   qualViewNameAggHead = genQualViewName(g_sectionIndexDbMeta, vnAggHeadTab, vsnAggHeadTab, ddlType)

   printSectionHeader "View determining tables corresponding to aggregate heads", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE VIEW"
   Print #fileNo, addTab(1); qualViewNameAggHead
   Print #fileNo, addTab(0); "("

   Print #fileNo, addTab(1); "TABSCHEMA,"
   Print #fileNo, addTab(1); "TABNAME,"
   Print #fileNo, addTab(1); g_anAcmCondenseData; ","
   Print #fileNo, addTab(1); g_anAcmIsNt2m; ","
   Print #fileNo, addTab(1); g_anOrganizationId; ","
   Print #fileNo, addTab(1); g_anPoolTypeId; ","
   Print #fileNo, addTab(1); g_anLdmFkSequenceNo
 
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "AS"
   Print #fileNo, addTab(1); "SELECT"
 
   Print #fileNo, addTab(2); "P."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(2); "P."; g_anPdmTableName; ","
   Print #fileNo, addTab(1); "A."; g_anAcmCondenseData; ","
   Print #fileNo, addTab(1); "A."; g_anAcmIsNt2m; ","
   Print #fileNo, addTab(2); "P."; g_anOrganizationId; ","
   Print #fileNo, addTab(2); "P."; g_anPoolTypeId; ","
   Print #fileNo, addTab(2); "L."; g_anLdmFkSequenceNo
   Print #fileNo, addTab(1); "FROM"

   Print #fileNo, addTab(2); g_qualTabNameLdmTable; " L,"
   Print #fileNo, addTab(2); g_qualTabNamePdmTable; " P,"
   Print #fileNo, addTab(2); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmEntityId; " = A."; g_anAhCid
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "L."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Function getNonAbstractClassIdSeq( _
   classIndex As Integer _
 ) As String
   Dim classIdSeq As String
   classIdSeq = ""

     classIdSeq = IIf(g_classes.descriptors(classIndex).isAbstract, "", "'" & g_classes.descriptors(classIndex).classIdStr & "'")

     Dim j As Integer
     For j = 1 To UBound(g_classes.descriptors(classIndex).subclassIndexesRecursive)
         If Not g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexesRecursive(j)).isAbstract Then
           classIdSeq = classIdSeq & IIf(classIdSeq = "", "", ",") & "'" & g_classes.descriptors(g_classes.descriptors(classIndex).subclassIndexesRecursive(j)).classIdStr & "'"
         End If
     Next j

   getNonAbstractClassIdSeq = classIdSeq
 End Function
 
 
 Private Sub genFactoryTakeOverDdlByOrg( _
   srcOrgIndex As Integer, _
   dstOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType = edtPdm And (srcOrgIndex < 1 Or dstOrgIndex < 1 Or srcPoolIndex < 0 Or dstPoolIndex < 1) Then
     ' Factory-Take-Over is only supported at 'pool-level'
     Exit Sub
   End If
 
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, dstOrgIndex, dstPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(dstOrgIndex, ddlType)

   Dim qualViewTabName As String
   qualViewTabName = genQualTabNameByClassIndex(g_classIndexView, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualViewNameAggHead As String
   qualViewNameAggHead = genQualViewName(g_sectionIndexDbMeta, vnAggHeadTab, vsnAggHeadTab, ddlType)

   Dim qualProcedureNameGetEnpEbpMapping As String
   Dim qualProcedureNameSetEnp As String

   ' ####################################################################################################################
   ' #    Retrieve OID-magging of EBP-/ENP-objects
   ' ####################################################################################################################

   qualProcedureNameGetEnpEbpMapping = _
     genQualProcName(g_sectionIndexFactoryTakeover, spnFtoGetEnpEbpMap, ddlType, dstOrgIndex, dstPoolIndex)

   printSectionHeader "SP for Retrieving OID-magging of EBP-/ENP-objects", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameGetEnpEbpMapping
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "psOid_in", "INTEGER", True, "OID of the ProductStructure"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of EBPs found for 'Factory Takeover'"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genSpLogDecl fileNo, -1, True
 
   genProcSectionHeader fileNo, "declare conditions", , Not supportSpLogging Or Not generateSpLogMessages
   genCondDecl fileNo, "alreadyExist", "42710"
 
   genProcSectionHeader fileNo, "declare variables"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempEnpOids fileNo
   genDdlForTempTypePriceAspects fileNo
   genDdlForTempImplicitChangeLogSummary fileNo, 1, True

   genDdlForTempChangeLogSummary fileNo, , True

   genSpLogProcEnter fileNo, qualProcedureNameGetEnpEbpMapping, ddlType, , "psOid_in", "rowCount_out"
   genProcSectionHeader fileNo, "determine EBP-Typeprices"
 
   Dim qualTabNameSrcGenericAspect As String
   Dim qualTabNameSrcProperty As String
   Dim qualTabNameSrcPropertyTemplate As String
   qualTabNameSrcGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, srcOrgIndex, srcPoolIndex)
   qualTabNameSrcProperty = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, srcOrgIndex, srcPoolIndex)
   qualTabNameSrcPropertyTemplate = genQualTabNameByClassIndex(g_classIndexPropertyTemplate, ddlType, srcOrgIndex, srcPoolIndex)

   Dim qualTabNameDstGenericAspect As String
   Dim qualTabNameDstProperty As String
   qualTabNameDstGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, dstOrgIndex, dstPoolIndex)
   qualTabNameDstProperty = genQualTabNameByClassIndex(g_classIndexProperty, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualViewNameDstGenericAspect As String
   Dim qualViewNameDstProperty As String
   Dim qualTabNameDstPropertyTemplate As String
   qualViewNameDstGenericAspect = genQualViewNameByClassIndex(g_classIndexGenericAspect, ddlType, dstOrgIndex, dstPoolIndex, , True, useMqtToImplementLrt)
   qualViewNameDstProperty = genQualViewNameByClassIndex(g_classIndexProperty, ddlType, dstOrgIndex, dstPoolIndex, , True, useMqtToImplementLrt)
   qualTabNameDstPropertyTemplate = genQualTabNameByClassIndex(g_classIndexPropertyTemplate, ddlType, dstOrgIndex, dstPoolIndex)
 
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); tempEnpOidTabName
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); tempEnpOidTabName
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "EBP_OID,"
   Print #fileNo, addTab(2); "isDeleted"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT DISTINCT"
   Print #fileNo, addTab(2); "MCLS.ahObjectId,"
   Print #fileNo, addTab(2); "MCLS.ahIsDeleted"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); gc_tempTabNameChangeLogOrgSummary; " MCLS"

   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameSrcGenericAspect; " GA"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "MCLS.ahObjectId = GA."; g_anOid
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "MCLS.ahClassId = '"; g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr; "'"

   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); gc_tempTabNameChangeLogImplicitChanges; " MICS"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "MICS.ahObjectId = GA."; g_anOid
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "MICS.isToBeDeleted = 0"

   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameSrcProperty; " PRP"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "GA.PRPAPR_OID = PRP."; g_anOid

   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameSrcPropertyTemplate; " PRT"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PRP.PTMHTP_OID = PRT."; g_anOid
 
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "MCLS.ahIsDeleted = "; gc_dbFalse
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PRT.ID = "; CStr(propertyTemplateIdEbp)
   Print #fileNo, addTab(1); "WITH UR;"

   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

   Print #fileNo,
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); tempEnpOidTabName
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "EBP_OID,"
   Print #fileNo, addTab(2); "isDeleted"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT DISTINCT"
   Print #fileNo, addTab(2); "MCLS.ahObjectId,"
   Print #fileNo, addTab(2); "MCLS.ahIsDeleted"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); gc_tempTabNameChangeLogOrgSummary; " MCLS"

   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameDstGenericAspect; " GA"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "MCLS.ahObjectId = GA."; g_anOid
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "MCLS.ahClassId = '"; g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr; "'"

   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameDstProperty; " PRP"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "GA.PRPAPR_OID = PRP."; g_anOid

   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameDstPropertyTemplate; " PRT"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "PRP.PTMHTP_OID = PRT."; g_anOid
 
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "MCLS.ahIsDeleted = "; gc_dbTrue
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "PRT.ID = "; CStr(propertyTemplateIdEbp)
   Print #fileNo, addTab(1); "WITH UR;"

   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   Dim qualFuncNameSr0Src As String
   Dim qualFuncNameSr1Src As String
     qualFuncNameSr0Src = genQualFuncName(g_classes.descriptors(g_classIndexGenericAspect).sectionIndex, "SR0Ctxt_OID", ddlType, dstOrgIndex, dstPoolIndex)
     qualFuncNameSr1Src = genQualFuncName(g_classes.descriptors(g_classIndexGenericAspect).sectionIndex, "SR1Ctxt_OID", ddlType, dstOrgIndex, dstPoolIndex)

   genProcSectionHeader fileNo, "identify ENP-Typeprices"
   Print #fileNo, addTab(1); "IF rowCount_out > 0 THEN"

   genProcSectionHeader fileNo, "retrieve details of related EBP-Typeprices in MPC's data pool", 2, True
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); tempEbpTypePriceTabName
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "oid,"
   Print #fileNo, addTab(3); "validFrom,"
   Print #fileNo, addTab(3); "validTo,"
   Print #fileNo, addTab(3); "allowedCountryIdListOid,"
   Print #fileNo, addTab(3); "disallowedCountryIdListOid,"
   Print #fileNo, addTab(3); "sr0CtxtOidList,"
   Print #fileNo, addTab(3); "sr1CtxtOidList"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "GAS."; g_anOid; ","
   Print #fileNo, addTab(3); "GAS."; g_anValidFrom; ","
   Print #fileNo, addTab(3); "GAS."; g_anValidTo; ","
   Print #fileNo, addTab(3); "GAS.ACLACL_OID,"
   Print #fileNo, addTab(3); "GAS.DCLDCL_OID,"
   Print #fileNo, addTab(3); qualFuncNameSr0Src; "(GAS."; g_anOid; "),"
   Print #fileNo, addTab(3); qualFuncNameSr1Src; "(GAS."; g_anOid; ")"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); tempEnpOidTabName; " B"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); qualTabNameDstGenericAspect; " GAS"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "B.EBP_OID = GAS."; g_anOid
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "GAS."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(2); "WITH UR;"
 
   genProcSectionHeader fileNo, "retrieve details of all ENP-Typeprices in MPC's data pool", 2
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); tempEnpTypePriceTabName
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "oid,"
   Print #fileNo, addTab(3); "validFrom,"
   Print #fileNo, addTab(3); "validTo,"
   Print #fileNo, addTab(3); "allowedCountryIdListOid,"
   Print #fileNo, addTab(3); "disallowedCountryIdListOid,"
   Print #fileNo, addTab(3); "sr0CtxtOidList,"
   Print #fileNo, addTab(3); "sr1CtxtOidList"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "GAS."; g_anOid; ","
   Print #fileNo, addTab(3); "GAS."; g_anValidFrom; ","
   Print #fileNo, addTab(3); "GAS."; g_anValidTo; ","
   Print #fileNo, addTab(3); "GAS.ACLACL_OID,"
   Print #fileNo, addTab(3); "GAS.DCLDCL_OID,"
   Print #fileNo, addTab(3); qualFuncNameSr0Src; "(GAS."; g_anOid; "),"
   Print #fileNo, addTab(3); qualFuncNameSr1Src; "(GAS."; g_anOid; ")"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameDstGenericAspect; " GAS"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); qualTabNameDstProperty; " PRP"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "GAS.PRPAPR_OID = PRP."; g_anOid
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); qualTabNameDstPropertyTemplate; " PRT"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PRP.PTMHTP_OID = PRT."; g_anOid
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "GAS."; g_anPsOid; " = psOid_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "GAS."; g_anCid; " = '"; g_classes.descriptors(g_classIndexTypePriceAssignment).classIdStr; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PRT.ID = "; CStr(propertyTemplateIdEnp)
   Print #fileNo, addTab(2); "WITH UR;"
 
   genProcSectionHeader fileNo, "map ENPs to EBPs", 2
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); tempEnpOidTabName; " EBP"
   Print #fileNo, addTab(2); "SET"
   Print #fileNo, addTab(3); "EBP.ENP_OID ="
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "SELECT"
   Print #fileNo, addTab(6); "GAM.oid"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); tempEbpTypePriceTabName; " GAF"
   Print #fileNo, addTab(5); "INNER JOIN"
   Print #fileNo, addTab(6); tempEnpTypePriceTabName; " GAM"
   Print #fileNo, addTab(5); "ON"
   Print #fileNo, addTab(6); "GAF.validFrom = GAM.validFrom"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "GAF.validTo = GAM.validTo"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "GAF.allowedCountryIdListOid = GAM.allowedCountryIdListOid"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "COALESCE(GAF.disallowedCountryIdListOid,0) = COALESCE(GAM.disallowedCountryIdListOid,0)"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "GAF.sr0CtxtOidList = GAM.sr0CtxtOidList"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "GAF.sr1CtxtOidList = GAM.sr1CtxtOidList"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "GAF.oid = EBP.EBP_OID"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(2); "WITH UR;"
 
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameGetEnpEbpMapping, ddlType, , "psOid_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
   ' ####################################################################################################################
   ' #    Set ENP- Prices
   ' ####################################################################################################################

   Dim mapEnpInLrt As Boolean
   Dim qualTargetNameGenericAspect As String
   Dim qualTargetNameProperty As String

   Dim i As Integer
   For i = 1 To 2
     mapEnpInLrt = (i = 1)
     qualTargetNameGenericAspect = IIf(mapEnpInLrt, qualViewNameDstGenericAspect, qualTabNameDstGenericAspect)
     qualTargetNameProperty = IIf(mapEnpInLrt, qualViewNameDstProperty, qualTabNameDstProperty)

     qualProcedureNameSetEnp = _
       genQualProcName( _
         g_sectionIndexFactoryTakeover, spnFtoSetEnp, ddlType, dstOrgIndex, dstPoolIndex, , IIf(mapEnpInLrt, "", "NoLrt"), False _
       )

     printSectionHeader "SP for Setting ENP-prices", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcedureNameSetEnp
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "IN", "rebateValue_in", "INTEGER", True, "rebate to apply (%)"
     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of ENPs being set"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader fileNo, "declare variables", , True
     genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
     genVarDecl fileNo, "v_someEnpDeleted", g_dbtBoolean, gc_dbFalse
     genVarDecl fileNo, "v_someEnpCreated", g_dbtBoolean, gc_dbFalse
     genVarDecl fileNo, "v_propertyOidEnp", g_dbtOid, "0"
     genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
     genSigMsgVarDecl fileNo
     genSpLogDecl fileNo

     genProcSectionHeader fileNo, "declare conditions"
     genCondDecl fileNo, "alreadyExist", "42710"

     genProcSectionHeader fileNo, "declare condition handler"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"

     genDdlForTempEnpOids fileNo
 
     genSpLogProcEnter fileNo, qualProcedureNameSetEnp, ddlType, , "rebateValue_in", "rowCount_out"

     genProcSectionHeader fileNo, "verify that the rebate value entered is within the limits"
     Print #fileNo, addTab(1); "IF (COALESCE(rebateValue_in, -1) < 0) OR (rebateValue_in >= 100) THEN"

     genSpLogProcEscape fileNo, qualProcedureNameSetEnp, ddlType, -2, "rebateValue_in", "rowCount_out"

     genSignalDdlWithParms "illegalRebateValue", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(rebateValue_in))"
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "determine whether some ENP is created"
     Print #fileNo, addTab(1); "IF EXISTS (SELECT 1 FROM "; tempEnpOidTabName; " WHERE isDeleted = 0 AND ENP_OID IS NULL ) THEN"
     Print #fileNo, addTab(2); "SET v_someEnpCreated = "; gc_dbTrue; ";"
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "determine whether some ENP is deleted"
     Print #fileNo, addTab(1); "IF EXISTS (SELECT 1 FROM "; tempEnpOidTabName; " WHERE isDeleted = 1 AND ENP_OID IS NOT NULL ) THEN"
     Print #fileNo, addTab(2); "SET v_someEnpDeleted = "; gc_dbTrue; ";"
     Print #fileNo, addTab(1); "END IF;"

     Print #fileNo,
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"

     genProcSectionHeader fileNo, "create new ENPs"
     Print #fileNo, addTab(1); "IF v_someEnpCreated > 0 THEN"

     genProcSectionHeader fileNo, "determine PROPERTY OID for ENP", 2, True
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "PRP."; g_anOid
     Print #fileNo, addTab(2); "INTO"
     Print #fileNo, addTab(3); "v_propertyOidEnp"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTargetNameProperty; " PRP"
     Print #fileNo, addTab(2); "INNER JOIN"
     Print #fileNo, addTab(3); qualTabNameDstPropertyTemplate; " PRT"
     Print #fileNo, addTab(2); "ON"
     Print #fileNo, addTab(3); "PRP.PTMHTP_OID = PRT."; g_anOid
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "PRT.ID = "; CStr(propertyTemplateIdEnp)
     Print #fileNo, addTab(2); "FETCH FIRST 1 ROW ONLY;"

     genProcSectionHeader fileNo, "create OIDs for new ENPs as needed", 2
     Print #fileNo, addTab(2); "UPDATE"
     Print #fileNo, addTab(3); tempEnpOidTabName; " MAP"
     Print #fileNo, addTab(2); "SET"
     Print #fileNo, addTab(3); "MAP.ENPNEW_OID = NEXTVAL FOR "; qualSeqNameOid
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "MAP.ENP_OID IS NULL"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "MAP.isDeleted = "; gc_dbFalse
     Print #fileNo, addTab(2); ";"
     Print #fileNo,

     Dim transformation As AttributeListTransformation
     Dim tabColumns As EntityColumnDescriptors

     genProcSectionHeader fileNo, "handle INSERT in GENERICASPECT", 2, True
     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); qualTargetNameGenericAspect
     Print #fileNo, addTab(2); "("

     genAttrListForEntity g_classIndexGenericAspect, eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, , , edomListNonLrt

     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "SELECT"

     initAttributeTransformation transformation, IIf(mapEnpInLrt, 5, 4), , , , "EBP."
     setAttributeMapping transformation, 1, conOid, "MAP.ENPNEW_OID"
     setAttributeMapping transformation, 2, conValue, "EBP." & g_anValue & " * (DECIMAL(100-rebateValue_in)/100)"
     setAttributeMapping transformation, 3, "PRPAPR_OID", "v_propertyOidEnp"
     setAttributeMapping transformation, 4, conAhOId, "MAP.ENPNEW_OID"
     If mapEnpInLrt Then
       setAttributeMapping transformation, 5, conStatusId, CStr(statusWorkInProgress)
     End If

     tabColumns = nullEntityColumnDescriptors
     genTransformedAttrListForEntityWithColReuse g_classIndexGenericAspect, eactClass, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, , , edomListNonLrt

     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameSrcGenericAspect; " EBP,"
     Print #fileNo, addTab(3); tempEnpOidTabName; " MAP"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "MAP.EBP_OID = EBP."; g_anOid
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "MAP.ENP_OID IS NULL"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "MAP.isDeleted = "; gc_dbFalse
     Print #fileNo, addTab(2); ";"

     genProcSectionHeader fileNo, "count the number of affected rows", 2
     Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "update existing ENPs"
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); qualTargetNameGenericAspect; " ENP"
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "ENP."; g_anValidFrom; ","
     Print #fileNo, addTab(3); "ENP."; g_anValidTo; ","
     If mapEnpInLrt Then
       Print #fileNo, addTab(3); "ENP."; g_anStatus; ","
     End If

     Print #fileNo, addTab(3); "ENP.ACLACL_OID,"
     Print #fileNo, addTab(3); "ENP.DCLDCL_OID,"
     Print #fileNo, addTab(3); "ENP."; g_anValue; ","
     Print #fileNo, addTab(3); "ENP.NSR1CONTEXT,"
     Print #fileNo, addTab(3); "ENP."; g_anVersionId
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "="
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "EBP."; g_anValidFrom; ","
     Print #fileNo, addTab(4); "EBP."; g_anValidTo; ","
     If mapEnpInLrt Then
       Print #fileNo, addTab(4); CStr(statusWorkInProgress); ","
     End If
     Print #fileNo, addTab(4); "EBP.ACLACL_OID,"
     Print #fileNo, addTab(4); "EBP.DCLDCL_OID,"
     Print #fileNo, addTab(4); "EBP."; g_anValue; " * (DECIMAL(100-rebateValue_in)/100),"
     Print #fileNo, addTab(4); "EBP.NSR1CONTEXT,"
     Print #fileNo, addTab(4); "ENP."; g_anVersionId; " + 1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameSrcGenericAspect; " EBP,"
     Print #fileNo, addTab(4); tempEnpOidTabName; " MAP"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "MAP.EBP_OID = EBP."; g_anOid
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "MAP.ENP_OID = ENP."; g_anOid
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "MAP.isDeleted = "; gc_dbFalse
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "ENP."; g_anOid; " IN ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "ENP_OID"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); tempEnpOidTabName; " MAP"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "MAP.isDeleted = "; gc_dbFalse
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "MAP.ENP_OID IS NOT NULL"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     genProcSectionHeader fileNo, "remove deleted ENPs"
     Print #fileNo, addTab(1); "IF v_someEnpDeleted > 0 THEN"

     genProcSectionHeader fileNo, "handle DELETE of EBPs", 2, True
     Print #fileNo, addTab(2); "DELETE FROM"
     Print #fileNo, addTab(3); qualTargetNameGenericAspect; " ENP"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "ENP."; g_anOid; " IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "MAP.ENP_OID"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); tempEnpOidTabName; " MAP"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "MAP.isDeleted = "; gc_dbTrue
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MAP.ENP_OID IS NOT NULL"
     Print #fileNo, addTab(3); ")"
     Print #fileNo, addTab(2); ";"

     genProcSectionHeader fileNo, "count the number of affected rows", 2
     Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

     Print #fileNo, addTab(1); "END IF;"

     genSpLogProcExit fileNo, qualProcedureNameSetEnp, ddlType, , "rebateValue_in", "rowCount_out"

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
 
 
 Private Sub genFactoryTakeOverDdlByOrg2( _
   srcOrgIndex As Integer, _
   dstOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType = edtPdm And (srcOrgIndex < 1 Or dstOrgIndex < 1 Or srcPoolIndex < 1 Or dstPoolIndex < 1) Then
     ' Factory-Take-Over is only supported at 'pool-level'
     Exit Sub
   End If

   If ddlType = edtLdm Then
     Exit Sub
   End If
 
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, dstOrgIndex, dstPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim clClassIndex As Integer
   clClassIndex = g_classIndexChangeLog
   Dim qualSrcClTabName As String
   qualSrcClTabName = genQualTabNameByClassIndex(clClassIndex, ddlType, srcOrgIndex, srcPoolIndex)
 
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(dstOrgIndex, ddlType)

   Dim qualViewTabName As String
   qualViewTabName = genQualTabNameByClassIndex(g_classIndexView, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNameGeneralSettings As String
   qualTabNameGeneralSettings = genQualTabNameByClassIndex(g_classIndexGeneralSettings, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNamePricePreferences As String
   qualTabNamePricePreferences = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualViewNameAggHead As String
   qualViewNameAggHead = genQualViewName(g_sectionIndexDbMeta, vnAggHeadTab, vsnAggHeadTab, ddlType)

   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors

   Dim qualProcedureNameFtoGetChangelog As String
   Dim qualProcedureNameFtoGetImplicitChanges As String
   Dim qualProcName As String

   ' ####################################################################################################################
   ' #    SP for Retrieving ChangeLog for Factory Data Take-Over
   ' ####################################################################################################################

   Dim qualTabNameChangeLog As String
   qualTabNameChangeLog = gc_tempTabNameChangeLog

   qualProcedureNameFtoGetChangelog = _
     genQualProcName( _
       g_sectionIndexAliasLrt, spnFtoGetChangeLog, ddlType, dstOrgIndex, dstPoolIndex _
     )

   printSectionHeader "SP for Retrieving ChangeLog for 'Factory Data Take-Over' (limit numer of records)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameFtoGetChangelog
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "classIdList_in", "VARCHAR(200)", True, "(optional) ','-separated list of classIDs used as filter-critereon"
   genProcParm fileNo, "IN", "maxRowCount_in", "INTEGER", True, "(optional) maximum number of rows to retrieve (= -1 when called from FACTORYTAKEOVER)"
   genProcParm fileNo, "IN", "languageId_in", g_dbtEnumId, True, "(optional) retrieve NL-strings only for this language"
   genProcParm fileNo, "IN", "filterBySr0Context_in", g_dbtBoolean, True, "if set to '1' records are filtered by SR0Context (applies only to GenericAspect)"
   genProcParm fileNo, "INOUT", "endTimestamp_inout", "TIMESTAMP", True, "marks the 'current' timestamp: only records before this timestamp are retrieved"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows in the ChangeLog"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_maxClOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_psOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_divisionOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_orgOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_startTimestamp", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_endTimestamp", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "alreadyExist", "42710"

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "stmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempTablesChangeLog fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, False
   genDdlForTempChangeLogSummary fileNo, 1, False
   genDdlForTempChangeLogSummary fileNo, 1, True
   genDdlForTempImplicitChangeLogSummary fileNo, 1, True

   genSpLogProcEnter fileNo, qualProcedureNameFtoGetChangelog, ddlType, , "'classIdList_in", "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out"

   genDb2RegVarCheckDdl fileNo, ddlType, dstOrgIndex, dstPoolIndex, tvNull, 1

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "determine ProductStructure"
   Print #fileNo, addTab(1); "SET v_psOid = "; g_activePsOidDdl; ";"

   genProcSectionHeader fileNo, "make sure that ProductStructure exists and Division can be determined"
   Print #fileNo, addTab(1); "SET v_divisionOid ="
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "PDIDIV_OID"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameProductStructure
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anOid; " = v_psOid"
   Print #fileNo, addTab(1); ");"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF (v_divisionOid IS NULL) THEN"

   genSpLogProcEscape fileNo, qualProcedureNameFtoGetChangelog, ddlType, 2, "'classIdList_in", "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out"

   genSignalDdlWithParms "psNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_psOid))"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "determine OID of 'my Organization'"
   Print #fileNo, addTab(1); "SET v_orgOid ="
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "ORGOID"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNamePdmOrganization
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "ID = "; genOrgId(dstOrgIndex, ddlType, True)
   Print #fileNo, addTab(1); ");"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF (v_orgOid IS NULL) THEN"

   genSpLogProcEscape fileNo, qualProcedureNameFtoGetChangelog, ddlType, -2, "'classIdList_in", "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out"

   genSignalDdl "noOrg", fileNo, 2

   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "determine TimeStamp of last Factory Data Take-Over"
   Print #fileNo, addTab(1); "SET v_startTimestamp ="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "MAX(LASTCENTRALDATATRANSFERCOMMIT)"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameGeneralSettings
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,

   Print #fileNo, addTab(1); "SET v_startTimestamp = COALESCE(v_startTimestamp, TIMESTAMP("; gc_valTimestampOrigin; "));"

   genProcSectionHeader fileNo, "retrieve ChangeLog-Summary data"
   Print #fileNo, addTab(1); "SET endTimestamp_inout = COALESCE(endTimestamp_inout, CURRENT TIMESTAMP);"
   Print #fileNo, addTab(1); "SET v_endTimestamp     = endTimestamp_inout;"

   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); gc_tempTabNameChangeLogSummary
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "entityId,"
   Print #fileNo, addTab(2); "entityType,"
   Print #fileNo, addTab(2); "ahClassId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "aggregateType,"
   Print #fileNo, addTab(2); "isCreated,"
   Print #fileNo, addTab(2); "isUpdated,"
   Print #fileNo, addTab(2); "isDeleted"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "WITH"
   Print #fileNo, addTab(2); "V"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "entityId,"
   Print #fileNo, addTab(2); "entityType,"
   Print #fileNo, addTab(2); "ahClassId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "aggregateType,"
   Print #fileNo, addTab(2); "isCreated,"
   Print #fileNo, addTab(2); "isUpdated,"
   Print #fileNo, addTab(2); "isDeleted"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "AS"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "CL.OBJECTID,"
   Print #fileNo, addTab(3); "CL."; g_anAcmEntityId; ","
   Print #fileNo, addTab(3); "CL."; g_anAcmEntityType; ","
   Print #fileNo, addTab(3); "CL."; g_anAhCid; ","
   Print #fileNo, addTab(3); "CL.AHOBJECTID,"
   Print #fileNo, addTab(3); "AE."; g_anAhCid; ","
   Print #fileNo, addTab(3); "MAX(CASE CL.OPERATION_ID WHEN "; CStr(lrtStatusCreated); " THEN 1 ELSE 0 END),"
   Print #fileNo, addTab(3); "MAX(CASE CL.OPERATION_ID WHEN "; CStr(lrtStatusUpdated); " THEN 1 ELSE 0 END),"
   Print #fileNo, addTab(3); "MAX(CASE CL.OPERATION_ID WHEN "; CStr(lrtStatusDeleted); " THEN 1 ELSE 0 END)"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualSrcClTabName; " CL,"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " AE"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "((CL."; g_anPsOid; " IS NULL AND CL.DIVISIONOID = v_divisionOid) OR CL."; g_anPsOid; " = v_psOid )"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "CL.OPTIMESTAMP > v_startTimestamp"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "CL.OPTIMESTAMP <= v_endTimestamp"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AE."; g_anAcmEntityId; " = CL."; g_anAhCid
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AE."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AE."; g_anAcmIsNt2m; " = "; gc_dbFalse
   Print #fileNo, addTab(2); "GROUP BY"
   Print #fileNo, addTab(3); "CL.OBJECTID,"
   Print #fileNo, addTab(3); "CL."; g_anAcmEntityId; ","
   Print #fileNo, addTab(3); "CL."; g_anAcmEntityType; ","
   Print #fileNo, addTab(3); "CL."; g_anAhCid; ","
   Print #fileNo, addTab(3); "CL.AHOBJECTID,"
   Print #fileNo, addTab(3); "AE."; g_anAhCid
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "entityId,"
   Print #fileNo, addTab(2); "entityType,"
   Print #fileNo, addTab(2); "ahClassId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "aggregateType,"
   Print #fileNo, addTab(2); "isCreated,"
   Print #fileNo, addTab(2); "isUpdated,"
   Print #fileNo, addTab(2); "isDeleted"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "V"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NOT (ISCREATED = 1 AND "; g_anIsDeleted; " = 1)"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader fileNo, "loop over aggregate heads"

   Print #fileNo, addTab(1); "FOR tabLoop AS"

   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_FltrClassIds"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "classId"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT DISTINCT"
   Print #fileNo, addTab(4); "elem"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "TABLE ( "; g_qualFuncNameStrElems; "(classIdList_in, CAST(',' AS CHAR(1))) ) AS X"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "elem IS NOT NULL"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(3); "V_FltrAhClassIds"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "ahClassId"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT DISTINCT"
   Print #fileNo, addTab(4); "AH."; g_anAhCid
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " AH"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "V_FltrClassIds F"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "F.classId = AH."; g_anAcmEntityId
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AH."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "classIdList_in IS NULL"
   Print #fileNo, addTab(5); "OR"
   Print #fileNo, addTab(4); "A."; g_anAhCid; " IN (SELECT ahClassId FROM V_FltrAhClassIds)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityId; " = A."; g_anAhCid
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmIgnoreForChangelog; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmIsNt2m; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(dstOrgIndex, ddlType, True)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = "; genPoolId(dstPoolIndex, ddlType)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " ASC"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(2); "FOR READ ONLY"
 
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader fileNo, "process each aggregate head individually", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '."; UCase(spnFtoGetChangeLog); "_' || c_tableName || '(?,?,?,?,?,?,?)';"

   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "v_psOid,"
   Print #fileNo, addTab(3); "v_divisionOid,"
   Print #fileNo, addTab(3); "v_orgOid,"
   Print #fileNo, addTab(3); "filterBySr0Context_in,"
   Print #fileNo, addTab(3); "v_startTimestamp,"
   Print #fileNo, addTab(3); "v_endTimestamp"
   Print #fileNo, addTab(2); ";"
 
   genProcSectionHeader fileNo, "add to number of affected rows", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   Print #fileNo,
   Print #fileNo, addTab(1); "IF maxRowCount_in < 0 THEN"
   genProcSectionHeader fileNo, "retrieve MPC-related ChangeLog-Summary", 2, True
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); gc_tempTabNameChangeLogOrgSummary
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "objectId,"
   Print #fileNo, addTab(3); "entityId,"
   Print #fileNo, addTab(3); "entityType,"
   Print #fileNo, addTab(3); "ahClassId,"
   Print #fileNo, addTab(3); "ahObjectId,"
   Print #fileNo, addTab(3); "aggregateType,"
   Print #fileNo, addTab(3); "ahIsCreated,"
   Print #fileNo, addTab(3); "ahIsUpdated,"
   Print #fileNo, addTab(3); "ahIsDeleted,"
   Print #fileNo, addTab(3); "isCreated,"
   Print #fileNo, addTab(3); "isUpdated,"
   Print #fileNo, addTab(3); "isDeleted"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "objectId,"
   Print #fileNo, addTab(3); "entityId,"
   Print #fileNo, addTab(3); "entityType,"
   Print #fileNo, addTab(3); "ahClassId,"
   Print #fileNo, addTab(3); "ahObjectId,"
   Print #fileNo, addTab(3); "aggregateType,"
   Print #fileNo, addTab(3); "isCreated,"
   Print #fileNo, addTab(3); "isUpdated,"
   Print #fileNo, addTab(3); "isDeleted"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "CL.OBJECTID,"
   Print #fileNo, addTab(4); "CL."; g_anAcmEntityId; ","
   Print #fileNo, addTab(4); "CL."; g_anAcmEntityType; ","
   Print #fileNo, addTab(4); "CL."; g_anAhCid; ","
   Print #fileNo, addTab(4); "CL.AHOBJECTID,"
   Print #fileNo, addTab(4); "AE."; g_anAhCid; ","
   Print #fileNo, addTab(4); "MAX(CASE CL.OPERATION_ID WHEN "; CStr(lrtStatusCreated); " THEN 1 ELSE 0 END),"
   Print #fileNo, addTab(4); "MAX(CASE CL.OPERATION_ID WHEN "; CStr(lrtStatusUpdated); " THEN 1 ELSE 0 END),"
   Print #fileNo, addTab(4); "MAX(CASE CL.OPERATION_ID WHEN "; CStr(lrtStatusDeleted); " THEN 1 ELSE 0 END)"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); gc_tempTabNameChangeLog; " CL,"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " AE"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "AE."; g_anAcmEntityId; " = CL."; g_anAhCid
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "AE."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(3); "GROUP BY"
   Print #fileNo, addTab(4); "CL.OBJECTID,"
   Print #fileNo, addTab(4); "CL."; g_anAcmEntityId; ","
   Print #fileNo, addTab(4); "CL."; g_anAcmEntityType; ","
   Print #fileNo, addTab(4); "CL."; g_anAhCid; ","
   Print #fileNo, addTab(4); "CL.AHOBJECTID,"
   Print #fileNo, addTab(4); "AE."; g_anAhCid
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "OBJ.objectId,"
   Print #fileNo, addTab(3); "OBJ.entityId,"
   Print #fileNo, addTab(3); "OBJ.entityType,"
   Print #fileNo, addTab(3); "OBJ.ahClassId,"
   Print #fileNo, addTab(3); "OBJ.ahObjectId,"
   Print #fileNo, addTab(3); "OBJ.aggregateType,"
   Print #fileNo, addTab(3); "COALESCE(AHD.isCreated,"; gc_dbFalse; "),"
   Print #fileNo, addTab(3); "COALESCE(AHD.isUpdated,"; gc_dbFalse; "),"
   Print #fileNo, addTab(3); "COALESCE(AHD.isDeleted,"; gc_dbFalse; "),"
   Print #fileNo, addTab(3); "OBJ.isCreated,"
   Print #fileNo, addTab(3); "OBJ.isUpdated,"
   Print #fileNo, addTab(3); "OBJ.isDeleted"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V OBJ"
   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); "V AHD"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "OBJ.ahObjectId = AHD.objectId"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "determine summary of implicit changes", 2

   Print #fileNo, addTab(2); "FOR tabLoop AS"

   Print #fileNo, addTab(3); "SELECT"

   Print #fileNo, addTab(4); "TABSCHEMA AS c_schemaName,"
   Print #fileNo, addTab(4); "TABNAME AS c_tableName"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualViewNameAggHead
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); g_anOrganizationId; " = "; genOrgId(dstOrgIndex, ddlType, True)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); g_anPoolTypeId; " = "; genPoolId(dstPoolIndex, ddlType)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); g_anAcmCondenseData; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); g_anAcmIsNt2m; " = "; gc_dbFalse
   ' do not call an empty generated SP
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "TABNAME"; " <> "; "'EXPRESSION'"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); g_anLdmFkSequenceNo; " ASC"
   Print #fileNo, addTab(3); "WITH UR"
   Print #fileNo, addTab(3); "FOR READ ONLY"
 
   Print #fileNo, addTab(2); "DO"
   genProcSectionHeader fileNo, "process each aggregate head individually", 3, True
   Print #fileNo, addTab(3); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '."; UCase(spnFtoGetImplicitChanges); "_' || c_tableName || '(?,?,?,?)';"

   Print #fileNo,
   Print #fileNo, addTab(3); "PREPARE stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "v_psOid,"
   Print #fileNo, addTab(4); "v_divisionOid,"
   Print #fileNo, addTab(4); "v_orgOid"
   Print #fileNo, addTab(3); ";"
 
   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "if classId-filter is given ignore records not matching filter critereon"
   Print #fileNo, addTab(1); "IF classIdList_in IS NOT NULL THEN"

   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); gc_tempTabNameChangeLog
   Print #fileNo, addTab(2); "WHERE"

   Print #fileNo, addTab(3); g_anAhCid; " NOT IN ("
 
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(3); "classId"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(3); "TABLE ( "; g_qualFuncNameGetSubClassIdsByList; "(classIdList_in) ) X"

   Print #fileNo, addTab(3); ")"

   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "determine max OID of ChangeLog - if number of output records is limited"

   Print #fileNo, addTab(1); "IF maxRowCount_in > 0 THEN"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); g_anOid
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_maxClOid"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); g_anOid; ","
   Print #fileNo, addTab(5); "ROWNUMBER() OVER (ORDER BY OID ASC) AS seqNo"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); gc_tempTabNameChangeLog; " CL"
   Print #fileNo, addTab(3); ") V_OidSeq"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "V_OidSeq.seqNo = maxRowCount_in"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(1); "END IF;"

   Dim changeLogClassIndex As Integer
   changeLogClassIndex = g_classIndexChangeLog
   Dim qualTabNameChangeLogNl As String
   qualTabNameChangeLogNl = genQualTabNameByClassIndex(changeLogClassIndex, ddlType, srcOrgIndex, srcPoolIndex, , , , True)

   genProcSectionHeader fileNo, "return result to application"

   Print #fileNo, addTab(1); "IF COALESCE(maxRowCount_in,1) > 0 THEN"
   Print #fileNo, addTab(2); "IF languageId_in > 0 THEN"
   Print #fileNo, addTab(3); "BEGIN"
   Print #fileNo, addTab(4); "DECLARE logCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(5); "SELECT"

   Dim transformation As AttributeListTransformation
   initAttributeTransformation transformation, 0, , True
   tabColumns = nullEntityColumnDescriptors
   genNlsTransformedAttrListForEntityWithColReUse changeLogClassIndex, eactClass, transformation, tabColumns, fileNo, , , ddlType, srcOrgIndex, srcPoolIndex, , , , , edomNone Or edomXref

   Dim i As Integer
   For i = 1 To tabColumns.numDescriptors
       If tabColumns.descriptors(i).columnCategory = eacLangId Or (tabColumns.descriptors(i).columnCategory And eacRegular) Then
         Print #fileNo, addTab(6); "CLNL."; tabColumns.descriptors(i).columnName; ","
       End If
   Next i

   initAttributeTransformation transformation, 0, , True, , "CL."
   setAttributeTransformationContext transformation, srcOrgIndex, srcPoolIndex, "CL"
   tabColumns = nullEntityColumnDescriptors

   genTransformedAttrListForEntityWithColReuse changeLogClassIndex, eactClass, transformation, tabColumns, fileNo, ddlType, , , 6, , , edomListNonLrt Or edomExpression Or edomNoDdlComment Or edomColumnName

   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); gc_tempTabNameChangeLog; " CL"
   Print #fileNo, addTab(5); "LEFT OUTER JOIN"
 
   Print #fileNo, addTab(6); qualTabNameChangeLogNl; " CLNL"
 
   Print #fileNo, addTab(5); "ON"
 
   Print #fileNo, addTab(6); "CLNL.CLG_OID = CL."; g_anOid
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "CLNL."; g_anLanguageId; " = languageId_in"

   Print #fileNo, addTab(5); "WHERE"

   Print #fileNo, addTab(6); "v_maxClOid IS NULL"
   Print #fileNo, addTab(7); "OR"
   Print #fileNo, addTab(6); "CL."; g_anOid; " <= v_maxClOid"
   Print #fileNo, addTab(5); "ORDER BY"
   Print #fileNo, addTab(6); "CL."; g_anOid
   Print #fileNo, addTab(5); "FOR READ ONLY"
   Print #fileNo, addTab(4); ";"
 
   Print #fileNo,
   Print #fileNo, addTab(4); "OPEN logCursor;"
   Print #fileNo, addTab(3); "END;"

   Print #fileNo, addTab(2); "ELSE"

   Print #fileNo, addTab(3); "BEGIN"
   Print #fileNo, addTab(4); "DECLARE logCursor CURSOR WITH RETURN TO CLIENT FOR"
   Print #fileNo, addTab(5); "SELECT"
   Print #fileNo, addTab(6); "CL.*"
   Print #fileNo, addTab(5); "FROM"
   Print #fileNo, addTab(6); gc_tempTabNameChangeLog; " CL"
   Print #fileNo, addTab(5); "WHERE"
   Print #fileNo, addTab(6); "(v_maxClOid IS NULL)"
   Print #fileNo, addTab(7); "OR"
   Print #fileNo, addTab(6); "(CL."; g_anOid; " <= v_maxClOid)"
   Print #fileNo, addTab(5); "ORDER BY"
   Print #fileNo, addTab(6); "CL."; g_anOid
   Print #fileNo, addTab(5); "FOR READ ONLY"
   Print #fileNo, addTab(4); ";"

   Print #fileNo,
   Print #fileNo, addTab(4); "OPEN logCursor;"
   Print #fileNo, addTab(3); "END;"

   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcedureNameFtoGetChangelog, ddlType, , "'classIdList_in", "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   printSectionHeader "SP for Retrieving ChangeLog for 'Factory Data Take-Over' (limit numer of records)", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameFtoGetChangelog
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "maxRowCount_in", "INTEGER", True, "(optional) maximum number of rows to retrieve (= -1 when called from FACTORYTAKEOVER)"
   genProcParm fileNo, "IN", "languageId_in", g_dbtEnumId, True, "(optional) retrieve NL-strings only for this language"
   genProcParm fileNo, "IN", "filterBySr0Context_in", g_dbtBoolean, True, "if set to '1' records are filtered by SR0Context (applies only to GenericAspect)"
   genProcParm fileNo, "INOUT", "endTimestamp_inout", "TIMESTAMP", True, "marks the 'current' timestamp: only records before this timestamp are retrieved"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows in the ChangeLog"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 1"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genSpLogProcEnter fileNo, qualProcedureNameFtoGetChangelog, ddlType, , "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out"

   Print #fileNo, addTab(1); "CALL "; qualProcedureNameFtoGetChangelog; "(CAST(NULL AS VARCHAR(1)), maxRowCount_in, languageId_in, filterBySr0Context_in, endTimestamp_inout, rowCount_out);"

   genSpLogProcExit fileNo, qualProcedureNameFtoGetChangelog, ddlType, , "maxRowCount_in", "languageId_in", "filterBySr0Context_in", "#endTimestamp_inout", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    SP for retrieving cardinality of Changelog for Factory Data Take-Over
   ' ####################################################################################################################

   Dim qualProcedureNameFtoGetChangelogCard As String
   qualProcedureNameFtoGetChangelogCard = _
     genQualProcName( _
       g_sectionIndexAliasLrt, spnFtoGetChangeLogCard, ddlType, dstOrgIndex, dstPoolIndex _
     )

   printSectionHeader "SP for Retrieving cardinality of ChangeLog for 'Factory Data Take-Over'", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameFtoGetChangelogCard
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows in the log"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"
 
   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "alreadyExist", "42710"

   genSpLogDecl fileNo, , True

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist BEGIN END;"
 
   genDdlForTempTablesChangeLog fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, False

   genSpLogProcEnter fileNo, qualProcedureNameFtoGetChangelogCard, ddlType, , "rowCount_out"

   genProcSectionHeader fileNo, "count rows in LRT-Log"
   Print #fileNo, addTab(1); "SET rowCount_out = (SELECT COUNT(*) FROM "; gc_tempTabNameChangeLog; ");"

   genSpLogProcExit fileNo, qualProcedureNameFtoGetChangelogCard, ddlType, , "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################

   Dim isGenericAspect As Boolean
   Dim qualFuncNameHasAlCountry As String

   Dim qualSrcTabName As String
   Dim qualDstTabName As String
   Dim qualDstTabNameLrt As String

   For i = 1 To g_classes.numDescriptors
       If g_classes.descriptors(i).isAggHead And g_classes.descriptors(i).isUserTransactional And Not g_classes.descriptors(i).noFto Then
         qualSrcTabName = genQualTabNameByClassIndex(i, ddlType, srcOrgIndex, srcPoolIndex)
         qualDstTabName = genQualTabNameByClassIndex(i, ddlType, dstOrgIndex, dstPoolIndex)

         ' GenericAspects always need special treatment ;-)
         isGenericAspect = (UCase(g_classes.descriptors(i).className) = "GENERICASPECT")
         If UCase(g_classes.descriptors(i).className) <> "EXPRESSION" Then

         ' ####################################################################################################################
         ' #    Get Summary of Implicit Changes for Factory Data Take-Over
         ' ####################################################################################################################

         qualProcedureNameFtoGetImplicitChanges = _
           genQualProcNameByEntityIndex(g_classes.descriptors(i).classIndex, eactClass, ddlType, dstOrgIndex, dstPoolIndex, , , , , spnFtoGetImplicitChanges)

         printSectionHeader "SP for Retrieving Summary of Implicit Changes for 'Factory Data Take-Over' on Aggregate Head '" & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & "'", fileNo
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE PROCEDURE"
         Print #fileNo, addTab(1); qualProcedureNameFtoGetImplicitChanges
         Print #fileNo, addTab(0); "("
         genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure to retrieve the ChangeLog for"
         genProcParm fileNo, "IN", "divisionOid_in", g_dbtOid, True, "OID of the Division to retrieve the ChangeLog for"
         genProcParm fileNo, "IN", "orgOid_in", g_dbtOid, True, "OID of 'my Organization'"
         genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows in the ChangeLog"
         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); "RESULT SETS 0"
         Print #fileNo, addTab(0); "LANGUAGE SQL"
         Print #fileNo, addTab(0); "BEGIN"

         Dim aggHeadContainsIsNotPublished As Boolean
         Dim aggHeadSubClassIdStrList As String

         initAttributeTransformation transformation, 0
         tabColumns = nullEntityColumnDescriptors
         aggHeadContainsIsNotPublished = False
         aggHeadSubClassIdStrList = ""

           aggHeadSubClassIdStrList = IIf(g_classes.descriptors(g_classes.descriptors(i).classIndex).isAbstract, "", "'" & g_classes.descriptors(g_classes.descriptors(i).classIndex).classIdStr & "'")

           Dim j As Integer
           For j = 1 To UBound(g_classes.descriptors(g_classes.descriptors(i).classIndex).subclassIndexesRecursive)
               If Not g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(i).classIndex).subclassIndexesRecursive(j)).isAbstract Then
                 aggHeadSubClassIdStrList = aggHeadSubClassIdStrList & IIf(aggHeadSubClassIdStrList = "", "", ",") & "'" & g_classes.descriptors(g_classes.descriptors(g_classes.descriptors(i).classIndex).subclassIndexesRecursive(j)).classIdStr & "'"
               End If
           Next j

         ' todo: should this be derived during initialization and stored in the class itself?
         genTransformedAttrListForEntityWithColReuse g_classes.descriptors(i).classIndex, eactClass, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, True, , edomNone
         For j = 1 To tabColumns.numDescriptors
           If tabColumns.descriptors(j).columnName = g_anIsNotPublished Then
             aggHeadContainsIsNotPublished = True
             Exit For
           End If
         Next j

         If g_classes.descriptors(i).navPathToOrg.relRefIndex > 0 Or aggHeadContainsIsNotPublished Or isGenericAspect Then
           genProcSectionHeader fileNo, "declare conditions", , True
           genCondDecl fileNo, "alreadyExist", "42710"

           genSpLogDecl fileNo, , True

           genProcSectionHeader fileNo, "declare condition handler"
           Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
           Print #fileNo, addTab(1); "BEGIN"
           Print #fileNo, addTab(2); "-- just ignore"
           Print #fileNo, addTab(1); "END;"

           If aggHeadContainsIsNotPublished Then
             genDdlForTempTablesChangeLog fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, False
           End If
           genDdlForTempChangeLogSummary fileNo, 1, True
           genDdlForTempImplicitChangeLogSummary fileNo, 1, True

           genSpLogProcEnter fileNo, qualProcedureNameFtoGetImplicitChanges, ddlType, , "psOid_in", "divisionOid_in", "orgOid_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out"
 
           If isGenericAspect Then
             genDdlForTempFtoClgGenericAspect fileNo, 1, True, , , False, False
           End If
 
           If isGenericAspect Then
             Dim qualTabNameCountryGroupElem As String
             qualTabNameCountryGroupElem = genQualTabNameByRelIndex(g_relIndexCountryGroupElement, ddlType, dstOrgIndex, dstPoolIndex)
 '            Dim qualTabNameGenericAspectOrg As String
 '            qualTabNameGenericAspectOrg = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, dstOrgIndex, dstPoolIndex)

             genProcSectionHeader fileNo, "determine Countries managed by 'this Organization'"
             Print #fileNo, addTab(1); "INSERT INTO"
             Print #fileNo, addTab(2); gc_tempTabNameManagedCountry
             Print #fileNo, addTab(1); "("
             Print #fileNo, addTab(2); "countryOid"
             Print #fileNo, addTab(1); ")"
             Print #fileNo, addTab(1); "WITH"
             Print #fileNo, addTab(2); "V_CountriesManaged"
             Print #fileNo, addTab(1); "("
             Print #fileNo, addTab(2); "countryOid,"
             Print #fileNo, addTab(2); "level"
             Print #fileNo, addTab(1); ")"
             Print #fileNo, addTab(1); "AS"
             Print #fileNo, addTab(1); "("
             Print #fileNo, addTab(2); "SELECT"
             Print #fileNo, addTab(3); "C.CNT_OID,"
             Print #fileNo, addTab(3); "1"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); g_qualTabNameOrgManagesCountry; " C"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "C.ORG_OID = orgOid_in"
             Print #fileNo,
             Print #fileNo, addTab(2); "UNION ALL"
             Print #fileNo,
             Print #fileNo, addTab(2); "SELECT"
             Print #fileNo, addTab(3); "E.CSP_OID,"
             Print #fileNo, addTab(3); "M.level + 1"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); "V_CountriesManaged M,"
             Print #fileNo, addTab(3); qualTabNameCountryGroupElem; " E"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "M.countryOid = E.CNG_OID"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "M.level < 1000"
             Print #fileNo, addTab(1); ")"
             Print #fileNo, addTab(1); "SELECT DISTINCT"
             Print #fileNo, addTab(2); "M.countryOid"
             Print #fileNo, addTab(1); "FROM"
             Print #fileNo, addTab(2); "V_CountriesManaged M"
             Print #fileNo, addTab(1); ";"

             genProcSectionHeader fileNo, "determine Countries relevant for 'this Organization'"
             Print #fileNo, addTab(1); "INSERT INTO"
             Print #fileNo, addTab(2); gc_tempTabNameRelevantCountry
             Print #fileNo, addTab(1); "("
             Print #fileNo, addTab(2); "countryOid"
             Print #fileNo, addTab(1); ")"
             Print #fileNo, addTab(1); "WITH"
             Print #fileNo, addTab(2); "V_CountriesRelevant"
             Print #fileNo, addTab(1); "("
             Print #fileNo, addTab(2); "countryOid,"
             Print #fileNo, addTab(2); "level"
             Print #fileNo, addTab(1); ")"
             Print #fileNo, addTab(1); "AS"
             Print #fileNo, addTab(1); "("
             Print #fileNo, addTab(2); "SELECT"
             Print #fileNo, addTab(3); "countryOid,"
             Print #fileNo, addTab(3); "1"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); gc_tempTabNameManagedCountry

             Print #fileNo,
             Print #fileNo, addTab(2); "UNION ALL"
             Print #fileNo,

             Print #fileNo, addTab(2); "SELECT"
             Print #fileNo, addTab(3); "E.CNG_OID,"
             Print #fileNo, addTab(3); "R.level + 1"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); "V_CountriesRelevant R,"
             Print #fileNo, addTab(3); qualTabNameCountryGroupElem; " E"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "R.countryOid = E.CSP_OID"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "R.level < 1000"
             Print #fileNo, addTab(1); ")"

             Print #fileNo, addTab(1); "SELECT DISTINCT"
             Print #fileNo, addTab(2); "R.countryOid"
             Print #fileNo, addTab(1); "FROM"
             Print #fileNo, addTab(2); "V_CountriesRelevant R"
             Print #fileNo, addTab(1); ";"

             genProcSectionHeader fileNo, "determine CountryId Lists involving Countries relevant for 'this Organization'"
             Print #fileNo, addTab(1); "INSERT INTO"
             Print #fileNo, addTab(2); gc_tempTabNameRelevantCountryIdList
             Print #fileNo, addTab(1); "("
             Print #fileNo, addTab(2); "idListOid"
             Print #fileNo, addTab(1); ")"
             Print #fileNo, addTab(1); "SELECT DISTINCT"
             Print #fileNo, addTab(2); "X.CIL_OID"
             Print #fileNo, addTab(1); "FROM"
             Print #fileNo, addTab(2); gc_tempTabNameRelevantCountry; " R"
             Print #fileNo, addTab(1); "INNER JOIN"
             Print #fileNo, addTab(2); g_qualTabNameCountryIdXRef; " X"
             Print #fileNo, addTab(1); "ON"
             Print #fileNo, addTab(2); "X.CSP_OID = R.countryOid"
             Print #fileNo, addTab(1); ";"

           End If
 
           genProcSectionHeader fileNo, "determine implicit 'create' related to this aggregate"
           Print #fileNo, addTab(1); "INSERT INTO"
           Print #fileNo, addTab(2); gc_tempTabNameChangeLogImplicitChanges
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "aggregateType,"
           Print #fileNo, addTab(2); "ahClassId,"
           Print #fileNo, addTab(2); "ahObjectId,"
           Print #fileNo, addTab(2); "isToBeCreated,"
           Print #fileNo, addTab(2); "isToBeDeleted"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "WITH"
           Print #fileNo, addTab(2); "V"
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "aggregateType,"
           Print #fileNo, addTab(2); "ahClassId,"
           Print #fileNo, addTab(2); "ahObjectId,"
           Print #fileNo, addTab(2); "isToBeCreated,"
           Print #fileNo, addTab(2); "isToBeDeleted"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "AS"
           Print #fileNo, addTab(1); "("
 
           Print #fileNo, addTab(2); "SELECT"

           Dim useUnion As Boolean
           useUnion = False
           If g_classes.descriptors(i).navPathToOrg.relRefIndex > 0 Then
             Dim qualRelTabOrg As String, relOrgEntityIdStr As String

             qualRelTabOrg = genQualTabNameByRelIndex(g_classes.descriptors(i).navPathToOrg.relRefIndex, ddlType, srcOrgIndex, srcPoolIndex)
             relOrgEntityIdStr = g_relationships.descriptors(g_classes.descriptors(i).navPathToOrg.relRefIndex).relIdStr

             genProcSectionHeader fileNo, "insert records related to a newly created organization-relationship", 3, True
             Print #fileNo, addTab(3); "MCLS.aggregateType,"
             Print #fileNo, addTab(3); "MCLS.ahClassId,"
             Print #fileNo, addTab(3); "MCLS.ahObjectId,"
             Print #fileNo, addTab(3); "1,"
             Print #fileNo, addTab(3); "0"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); gc_tempTabNameChangeLogOrgSummary; " MCLS,"
             Print #fileNo, addTab(3); qualRelTabOrg; " VFO"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "MCLS.entityId = '"; relOrgEntityIdStr; "'"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.entityType = '"; gc_acmEntityTypeKeyRel; "'"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.isCreated = "; gc_dbTrue
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.ahIsCreated = "; gc_dbFalse
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.objectId = VFO."; g_anOid
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "VFO.ORG_OID = orgOid_in"
             useUnion = True
           End If

           If aggHeadContainsIsNotPublished Then
             If useUnion Then
               Print #fileNo, addTab(2); "UNION ALL"
               Print #fileNo, addTab(2); "SELECT"
             End If
             genProcSectionHeader fileNo, "insert records related to a change of '" & g_anIsNotPublished & "'", 3, True
             Print #fileNo, addTab(3); "MCLS.aggregateType,"
             Print #fileNo, addTab(3); "MCLS.ahClassId,"
             Print #fileNo, addTab(3); "MCLS.ahObjectId,"
             Print #fileNo, addTab(3); "1,"
             Print #fileNo, addTab(3); "0"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); gc_tempTabNameChangeLog; " CL,"
             Print #fileNo, addTab(3); gc_tempTabNameChangeLogOrgSummary; " MCLS"
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "CL.entityType = '"; gc_acmEntityTypeKeyClass; "'"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.entityId IN ("; aggHeadSubClassIdStrList; ")"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.operation_ID = "; CStr(lrtStatusUpdated)
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.dbColumnName = '"; g_anIsNotPublished; "'"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.objectId = CL.objectId"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.isCreated = "; gc_dbFalse
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.isUpdated = "; gc_dbTrue
             useUnion = True
           End If

           If isGenericAspect Then
             If useUnion Then
               Print #fileNo, addTab(2); "UNION ALL"
               Print #fileNo, addTab(2); "SELECT"
             End If

             genProcSectionHeader fileNo, "insert records related to a change of '" & g_anIsNotPublished & "' of a code", 3, True
 
             Print #fileNo, addTab(3); "'"; getClassIdStrByIndex(g_classIndexGenericAspect); "', -- aggregateType 'GenericAspect'"
             Print #fileNo, addTab(3); "AF."; g_anAhCid; ","
             Print #fileNo, addTab(3); "AF."; g_anAhOid; ","
             Print #fileNo, addTab(3); "1,"
             Print #fileNo, addTab(3); "0"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); qualSrcTabName; " AF"
 
             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); gc_tempTabNameRelevantCountryIdList; " ACL"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "AF.ACLACL_OID = ACL.idListOid"
 
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "AF."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "AF."; g_anIsNotPublished; " = "; gc_dbFalse
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "AF.BCDBCD_OID IN ("
             Print #fileNo, addTab(4); "SELECT"
             Print #fileNo, addTab(5); "CL.objectId"
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); gc_tempTabNameChangeLog; " CL,"
             Print #fileNo, addTab(5); gc_tempTabNameChangeLogOrgSummary; " MCLS"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "CL.entityType = '"; gc_acmEntityTypeKeyClass; "'"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "CL.entityId IN ("; getNonAbstractClassIdSeq(g_classIndexGenericCode); ")"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "CL.operation_ID = "; CStr(lrtStatusUpdated)
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "CL.dbColumnName = '"; g_anIsNotPublished; "'"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "MCLS.objectId = CL.objectId"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "MCLS.isCreated = "; gc_dbFalse
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "MCLS.isUpdated = "; gc_dbTrue
             Print #fileNo, addTab(3); ")"
 
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "("
             Print #fileNo, addTab(4); "AF.DCLDCL_OID IS NULL"
             Print #fileNo, addTab(5); "OR"
             Print #fileNo, addTab(4); "AF.DCLDCL_OID NOT IN ("
             Print #fileNo, addTab(5); "SELECT"
             Print #fileNo, addTab(6); "DCL.idListOid"
             Print #fileNo, addTab(5); "FROM"
             Print #fileNo, addTab(6); gc_tempTabNameRelevantCountryIdList; " DCL"
             Print #fileNo, addTab(4); ")"
             Print #fileNo, addTab(3); ")"
 
             Print #fileNo, addTab(2); "UNION ALL"
             Print #fileNo, addTab(2); "SELECT"
             genProcSectionHeader fileNo, "insert records related to a change of 'ACLACL_OID' or 'DCLDCL_OID'", 3, True
             Print #fileNo, addTab(3); "MCLS.aggregateType,"
             Print #fileNo, addTab(3); "MCLS.ahClassId,"
             Print #fileNo, addTab(3); "MCLS.ahObjectId,"
             Print #fileNo, addTab(3); "1,"
             Print #fileNo, addTab(3); "0"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); gc_tempTabNameChangeLog; " CL"
             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); gc_tempTabNameChangeLogOrgSummary; " MCLS"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "MCLS.objectId = CL.objectId"
             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); qualSrcTabName; " AF"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "MCLS.ahObjectId = AF."; g_anOid
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "AF."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.entityType = '"; gc_acmEntityTypeKeyClass; "'"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.entityId IN ("; aggHeadSubClassIdStrList; ")"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.operation_ID = "; CStr(lrtStatusUpdated)
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.dbColumnName IN ('ACLACL_OID', 'DCLDCL_OID')"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.isCreated = "; gc_dbFalse
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.isUpdated = "; gc_dbTrue
 
             Print #fileNo, addTab(4); "AND"
 
             qualFuncNameHasAlCountry = genQualFuncName(g_classes.descriptors(i).sectionIndex, "HASALCNTRY", ddlType, srcOrgIndex, srcPoolIndex)
             Print #fileNo, addTab(3); qualFuncNameHasAlCountry; "(AF."; g_anOid; ", AF."; g_anCid; ", orgOid_in) = "; gc_dbTrue
 
             Print #fileNo, addTab(4); "AND"
 
             Print #fileNo, addTab(3); "NOT EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             genProcSectionHeader fileNo, "only consider this record if it does not yet exist for given MPC", 5, True
             Print #fileNo, addTab(5); g_anOid
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); qualDstTabName; " REF"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "REF."; g_anOid; " = MCLS.ahobjectId"
             Print #fileNo, addTab(3); ")"
           End If

           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "SELECT DISTINCT"
           Print #fileNo, addTab(2); "aggregateType,"
           Print #fileNo, addTab(2); "ahClassId,"
           Print #fileNo, addTab(2); "ahObjectId,"
           Print #fileNo, addTab(2); "isToBeCreated,"
           Print #fileNo, addTab(2); "isToBeDeleted"
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); "V"
           Print #fileNo, addTab(1); ";"

           If isGenericAspect Then
             genProcSectionHeader fileNo, "determine implicit 'delete' related to this aggregate"
             Print #fileNo, addTab(1); "INSERT INTO"
             Print #fileNo, addTab(2); gc_tempTabNameChangeLogImplicitChanges
             Print #fileNo, addTab(1); "("
             Print #fileNo, addTab(2); "aggregateType,"
             Print #fileNo, addTab(2); "ahClassId,"
             Print #fileNo, addTab(2); "ahObjectId,"
             Print #fileNo, addTab(2); "isToBeCreated,"
             Print #fileNo, addTab(2); "isToBeDeleted"
             Print #fileNo, addTab(1); ")"
             Print #fileNo, addTab(1); "WITH"
             Print #fileNo, addTab(2); "V"
             Print #fileNo, addTab(1); "("
             Print #fileNo, addTab(2); "aggregateType,"
             Print #fileNo, addTab(2); "ahClassId,"
             Print #fileNo, addTab(2); "ahObjectId,"
             Print #fileNo, addTab(2); "isToBeCreated,"
             Print #fileNo, addTab(2); "isToBeDeleted"
             Print #fileNo, addTab(1); ")"
             Print #fileNo, addTab(1); "AS"
             Print #fileNo, addTab(1); "("

             Print #fileNo, addTab(2); "SELECT"
             genProcSectionHeader fileNo, "delete records related to a change of 'ACLACL_OID' or 'DCLDCL_OID'", 3, True
             Print #fileNo, addTab(3); "MCLS.aggregateType,"
             Print #fileNo, addTab(3); "MCLS.ahClassId,"
             Print #fileNo, addTab(3); "MCLS.ahObjectId,"
             Print #fileNo, addTab(3); "0,"
             Print #fileNo, addTab(3); "1"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); gc_tempTabNameChangeLog; " CL"
             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); gc_tempTabNameChangeLogOrgSummary; " MCLS"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "MCLS.objectId = CL.objectId"
             Print #fileNo, addTab(2); "INNER JOIN"
             Print #fileNo, addTab(3); qualSrcTabName; " AF"
             Print #fileNo, addTab(2); "ON"
             Print #fileNo, addTab(3); "MCLS.ahObjectId = AF."; g_anOid
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); "CL.entityType = '"; gc_acmEntityTypeKeyClass; "'"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.entityId IN ("; aggHeadSubClassIdStrList; ")"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.operation_ID = "; CStr(lrtStatusUpdated)
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "CL.dbColumnName IN ('ACLACL_OID', 'DCLDCL_OID')"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.isCreated = "; gc_dbFalse
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "MCLS.isUpdated = "; gc_dbTrue
 
             Print #fileNo, addTab(4); "AND"
 
             qualFuncNameHasAlCountry = genQualFuncName(g_classes.descriptors(i).sectionIndex, "HASALCNTRY", ddlType, srcOrgIndex, srcPoolIndex)
             Print #fileNo, addTab(3); qualFuncNameHasAlCountry; "(AF."; g_anOid; ", AF."; g_anCid; ", orgOid_in) = "; gc_dbFalse
 
             Print #fileNo, addTab(4); "AND"
 
             Print #fileNo, addTab(3); "EXISTS ("
             Print #fileNo, addTab(4); "SELECT"
             genProcSectionHeader fileNo, "only consider this record if it exists for given MPC", 5, True
             Print #fileNo, addTab(5); g_anOid
             Print #fileNo, addTab(4); "FROM"
             Print #fileNo, addTab(5); qualDstTabName; " REF"
             Print #fileNo, addTab(4); "WHERE"
             Print #fileNo, addTab(5); "REF."; g_anOid; " = MCLS.ahobjectId"
             Print #fileNo, addTab(3); ")"
             Print #fileNo, addTab(1); ")"
             Print #fileNo, addTab(1); "SELECT DISTINCT"
             Print #fileNo, addTab(2); "aggregateType,"
             Print #fileNo, addTab(2); "ahClassId,"
             Print #fileNo, addTab(2); "ahObjectId,"
             Print #fileNo, addTab(2); "isToBeCreated,"
             Print #fileNo, addTab(2); "isToBeDeleted"
             Print #fileNo, addTab(1); "FROM"
             Print #fileNo, addTab(2); "V"
             Print #fileNo, addTab(1); ";"
           End If

           genSpLogProcExit fileNo, qualProcedureNameFtoGetImplicitChanges, ddlType, , "psOid_in", "divisionOid_in", "orgOid_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out"
         Else
           genSpLogDecl fileNo, , True
           genSpLogProcEnter fileNo, qualProcedureNameFtoGetImplicitChanges, ddlType, , "psOid_in", "divisionOid_in", "orgOid_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out"

           genProcSectionHeader fileNo, "initialize output parameter"
           Print #fileNo, addTab(1); "SET rowCount_out  = 0;"

           genSpLogProcExit fileNo, qualProcedureNameFtoGetImplicitChanges, ddlType, , "psOid_in", "divisionOid_in", "orgOid_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out"
         End If

         Print #fileNo, addTab(0); "END"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
         End If '<> EXPRESSION
         ' ####################################################################################################################
         ' #    ChangeLog for Factory Data Take-Over per aggregate
         ' ####################################################################################################################

         Dim qualFuncNameSr0 As String
           qualFuncNameSr0 = genQualFuncName(g_classes.descriptors(g_classIndexGenericAspect).sectionIndex, "Sr0IsAvail", ddlType, dstOrgIndex, dstPoolIndex)

         qualProcName = genQualProcNameByEntityIndex(g_classes.descriptors(i).classIndex, eactClass, ddlType, dstOrgIndex, dstPoolIndex, , , , , spnFtoGetChangeLog)

         printSectionHeader "SP for Retrieving ChangeLog for 'Factory Data Take-Over' on Aggregate Head '" & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & "'", fileNo
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE PROCEDURE"
         Print #fileNo, addTab(1); qualProcName
         Print #fileNo, addTab(0); "("
         genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure to retrieve the ChangeLog for"
         genProcParm fileNo, "IN", "divisionOid_in", g_dbtOid, True, "OID of the Division to retrieve the ChangeLog for"
         genProcParm fileNo, "IN", "orgOid_in", g_dbtOid, True, "OID of 'my Organization'"
         genProcParm fileNo, "IN", "filterBySr0Context_in", g_dbtBoolean, True, "if set to '1' records are filtered by SR0Context (applies only to GenericAspect)"
         genProcParm fileNo, "IN", "startTimestamp_in", "TIMESTAMP", True, "only ChangeLog records past this timestamp are retrieved"
         genProcParm fileNo, "IN", "endTimestamp_in", "TIMESTAMP", True, "only ChangeLog records up to this timestamp are retrieved"
         genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows returned in the ChangeLog"
         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); "RESULT SETS 0"
         Print #fileNo, addTab(0); "LANGUAGE SQL"
         Print #fileNo, addTab(0); "BEGIN"

         Dim containsSr0Context As Boolean
         containsSr0Context = False

         Dim k As Integer
         For k = 1 To tabColumns.numDescriptors
             If tabColumns.descriptors(k).columnName = g_anSr0Context Then
               containsSr0Context = True
               Exit For
             End If
         Next k

         If containsSr0Context Then
           genProcSectionHeader fileNo, "declare variables", , True
           genVarDecl fileNo, "v_sr0Context", "VARCHAR(50)", "NULL"
         End If
         If isGenericAspect Then
             genVarDecl fileNo, "v_takeoverCBVFlag", "SMALLINT", "0"
         End If

         genProcSectionHeader fileNo, "declare conditions", , Not containsSr0Context
         genCondDecl fileNo, "alreadyExist", "42710"

         genSpLogDecl fileNo, , True
 
         genProcSectionHeader fileNo, "declare condition handler"
         Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
         Print #fileNo, addTab(1); "BEGIN"
         Print #fileNo, addTab(2); "-- just ignore"
         Print #fileNo, addTab(1); "END;"

         genDdlForTempTablesChangeLog fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1
         genDdlForTempChangeLogSummary fileNo, 1

         If containsSr0Context Then
           genProcSectionHeader fileNo, "temporary table for Factory SR0CONTEXTs", 1
           Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
           Print #fileNo, addTab(2); tempTabNameSr0ContextFac
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "sr0Context      VARCHAR(50),"
           Print #fileNo, addTab(2); "isMpcRelevant   "; g_dbtBoolean; " DEFAULT 0"
           Print #fileNo, addTab(1); ")"
           genDdlForTempTableDeclTrailer fileNo, 1
 
           genProcSectionHeader fileNo, "temporary table for MPC SR0CONTEXTs", 1
           Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
           Print #fileNo, addTab(2); tempTabNameSr0ContextOrg
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "sr0Context      VARCHAR(50)"
           Print #fileNo, addTab(1); ")"
           genDdlForTempTableDeclTrailer fileNo, 1
         End If

         If isGenericAspect Then
           genDdlForTempFtoClgGenericAspect fileNo, 1, True, , , False, False
         End If

         genSpLogProcEnter fileNo, qualProcName, ddlType, , "psOid_in", "divisionOid_in", "orgOid_in", "filterBySr0Context_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out"

         If isGenericAspect Then
 '          Dim qualTabNameCountryGroupElem As String
 '          qualTabNameCountryGroupElem = genQualTabNameByRelIndex(g_relIndexCountryGroupElement, ddlType, dstOrgIndex, dstPoolIndex)
           Dim qualTabNameGenericAspectOrg As String
           qualTabNameGenericAspectOrg = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, dstOrgIndex, dstPoolIndex)

           genProcSectionHeader fileNo, "determine Countries managed by 'this Organization'"
           Print #fileNo, addTab(1); "INSERT INTO"
           Print #fileNo, addTab(2); gc_tempTabNameManagedCountry
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "countryOid"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "WITH"
           Print #fileNo, addTab(2); "V_CountriesManaged"
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "countryOid,"
           Print #fileNo, addTab(2); "level"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "AS"
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "SELECT"
           Print #fileNo, addTab(3); "C.CNT_OID,"
           Print #fileNo, addTab(3); "1"
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); g_qualTabNameOrgManagesCountry; " C"
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "C.ORG_OID = orgOid_in"
           Print #fileNo,
           Print #fileNo, addTab(2); "UNION ALL"
           Print #fileNo,
           Print #fileNo, addTab(2); "SELECT"
           Print #fileNo, addTab(3); "E.CSP_OID,"
           Print #fileNo, addTab(3); "M.level + 1"
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); "V_CountriesManaged M,"
           Print #fileNo, addTab(3); qualTabNameCountryGroupElem; " E"
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "M.countryOid = E.CNG_OID"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "M.level < 1000"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "SELECT DISTINCT"
           Print #fileNo, addTab(2); "M.countryOid"
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); "V_CountriesManaged M"
           Print #fileNo, addTab(1); ";"

           genProcSectionHeader fileNo, "determine Countries relevant for 'this Organization'"
           Print #fileNo, addTab(1); "INSERT INTO"
           Print #fileNo, addTab(2); gc_tempTabNameRelevantCountry
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "countryOid"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "WITH"
           Print #fileNo, addTab(2); "V_CountriesRelevant"
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "countryOid,"
           Print #fileNo, addTab(2); "level"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "AS"
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "SELECT"
           Print #fileNo, addTab(3); "countryOid,"
           Print #fileNo, addTab(3); "1"
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); gc_tempTabNameManagedCountry

           Print #fileNo,
           Print #fileNo, addTab(2); "UNION ALL"
           Print #fileNo,

           Print #fileNo, addTab(2); "SELECT"
           Print #fileNo, addTab(3); "E.CNG_OID,"
           Print #fileNo, addTab(3); "R.level + 1"
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); "V_CountriesRelevant R,"
           Print #fileNo, addTab(3); qualTabNameCountryGroupElem; " E"
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "R.countryOid = E.CSP_OID"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "R.level < 1000"
           Print #fileNo, addTab(1); ")"

           Print #fileNo, addTab(1); "SELECT DISTINCT"
           Print #fileNo, addTab(2); "R.countryOid"
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); "V_CountriesRelevant R"
           Print #fileNo, addTab(1); ";"

           genProcSectionHeader fileNo, "determine CountryId Lists involving Countries relevant for 'this Organization'"
           Print #fileNo, addTab(1); "INSERT INTO"
           Print #fileNo, addTab(2); gc_tempTabNameRelevantCountryIdList
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "idListOid"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "SELECT DISTINCT"
           Print #fileNo, addTab(2); "X.CIL_OID"
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); gc_tempTabNameRelevantCountry; " R"
           Print #fileNo, addTab(1); "INNER JOIN"
           Print #fileNo, addTab(2); g_qualTabNameCountryIdXRef; " X"
           Print #fileNo, addTab(1); "ON"
           Print #fileNo, addTab(2); "X.CSP_OID = R.countryOid"
           Print #fileNo, addTab(1); ";"

           genProcSectionHeader fileNo, "determine cross-references for CountryId Lists involving Countries managed by 'this Organization'"
           Print #fileNo, addTab(1); "INSERT INTO"
           Print #fileNo, addTab(2); gc_tempTabNameRelevantCountryIdXRef
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "idListOid,"
           Print #fileNo, addTab(2); "countryOid"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "WITH"
           Print #fileNo, addTab(2); "V_CountryIdList"
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "idListOid"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "AS"
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "SELECT"
           Print #fileNo, addTab(3); "idListOid"
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); gc_tempTabNameRelevantCountryIdList
           Print #fileNo, addTab(1); "),"
           Print #fileNo, addTab(1); "V_CountryIdXRef"
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "idListOid,"
           Print #fileNo, addTab(2); "countryOid,"
           Print #fileNo, addTab(2); "level"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "AS"
           Print #fileNo, addTab(1); "("
           Print #fileNo, addTab(2); "SELECT"
           Print #fileNo, addTab(3); "X.CIL_OID,"
           Print #fileNo, addTab(3); "X.CSP_OID,"
           Print #fileNo, addTab(3); "1"
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); "V_CountryIdList V,"
           Print #fileNo, addTab(3); g_qualTabNameCountryIdXRef; " X"
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "V.idListOid = X.CIL_OID"
           Print #fileNo,
           Print #fileNo, addTab(2); "UNION ALL"
           Print #fileNo,
           Print #fileNo, addTab(2); "SELECT"
           Print #fileNo, addTab(3); "V.idListOid,"
           Print #fileNo, addTab(3); "E.CSP_OID,"
           Print #fileNo, addTab(3); "V.level + 1"
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); "V_CountryIdXRef V,"
           Print #fileNo, addTab(3); qualTabNameCountryGroupElem; " E"
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "V.countryOid = E.CNG_OID"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "V.level < 1000"
           Print #fileNo, addTab(1); ")"
           Print #fileNo, addTab(1); "SELECT DISTINCT"
           Print #fileNo, addTab(2); "X.idListOid,"
           Print #fileNo, addTab(2); "X.countryOid"
           Print #fileNo, addTab(1); "FROM"
           Print #fileNo, addTab(2); "V_CountryIdXRef X"
           Print #fileNo, addTab(1); "INNER JOIN"
           Print #fileNo, addTab(2); gc_tempTabNameManagedCountry; " MC"
           Print #fileNo, addTab(1); "ON"
           Print #fileNo, addTab(2); "X.countryOid = MC.countryOid"
           Print #fileNo, addTab(1); "INNER JOIN"
           Print #fileNo, addTab(2); g_qualTabNameCountrySpec; " C"
           Print #fileNo, addTab(1); "ON"
           Print #fileNo, addTab(2); "C."; g_anOid; " = MC.countryOid"
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "C."; g_anCid; " = '"; getClassIdStrByIndex(g_classIndexCountry); "'"
           Print #fileNo, addTab(1); ";"

           ' special handling for CodeBaumusterValidities depends on PricePreferences
           Print #fileNo,
           Print #fileNo, addTab(1); "SELECT "
           Print #fileNo, addTab(2); "P.ISDPB * P.TAKEOVERBLOCKEDPRICEFLAG"
           Print #fileNo, addTab(1); "INTO "
           Print #fileNo, addTab(2); "v_takeoverCBVFlag"
           Print #fileNo, addTab(1); "FROM "
           Print #fileNo, addTab(2); qualTabNamePricePreferences; " P "
           Print #fileNo, addTab(1); "WHERE "
           Print #fileNo, addTab(2); "P.PS_OID = psOid_in";
           Print #fileNo,
           Print #fileNo, addTab(1); ";"
           Print #fileNo,

         End If

         genProcSectionHeader fileNo, "retrieve ChangeLog records related to this aggregate"

         Dim offset As Integer
         offset = 0

         If containsSr0Context Then
           Dim qualFuncNameIsSubset As String
           qualFuncNameIsSubset = genQualFuncName(g_sectionIndexMeta, udfnIsSubset, ddlType, , , , , , True)

           offset = 1
           Print #fileNo, addTab(1); "IF filterBySr0Context_in = 1 THEN"
           genProcSectionHeader fileNo, "determine SR0Contexts in Factory data", 2, True
           Print #fileNo, addTab(2); "INSERT INTO"
           Print #fileNo, addTab(3); tempTabNameSr0ContextFac
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "sr0Context"
           Print #fileNo, addTab(2); ")"
 
           Print #fileNo, addTab(2); "SELECT DISTINCT"
           Print #fileNo, addTab(3); "AHD."; g_anSr0Context
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); gc_tempTabNameChangeLogSummary; " CLS"
           Print #fileNo, addTab(2); "INNER JOIN"
           Print #fileNo, addTab(3); qualSrcTabName; " AHD"
           Print #fileNo, addTab(2); "ON"
           Print #fileNo, addTab(3); "AHD."; g_anOid; " = CLS.ahObjectId"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "CLS.aggregateType = '"; g_classes.descriptors(i).classIdStr; "'"
 
           ' Fixme: the following handling of navigation paths make implicit assumtions about cardinality of relationships!
           '      : remove this!
           If g_classes.descriptors(i).isPsTagged Then
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "AHD."; g_anPsOid; " = psOid_in"
           ElseIf g_classes.descriptors(i).navPathToDiv.relRefIndex > 0 Then
             Dim fkAttrToDiv As String
             Dim navPathToDiv As NavPathFromClassToClass
             navPathToDiv = g_classes.descriptors(i).navPathToDiv
               If navPathToDiv.navDirection = etLeft Then
                 fkAttrToDiv = g_relationships.descriptors(navPathToDiv.relRefIndex).leftFkColName(ddlType)
               Else
                 fkAttrToDiv = g_relationships.descriptors(navPathToDiv.relRefIndex).rightFkColName(ddlType)
               End If

             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "AHD."; fkAttrToDiv; " = divisionOid_in"
           End If
 
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "AHD."; g_anSr0Context; " IS NOT NULL"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "RTRIM(AHD."; g_anSr0Context; ") <> ''"
           Print #fileNo, addTab(2); ";"
 
           genProcSectionHeader fileNo, "determine SR0Contexts supported by 'this Organization'", 2
           Print #fileNo, addTab(2); "INSERT INTO"
           Print #fileNo, addTab(3); tempTabNameSr0ContextOrg
           Print #fileNo, addTab(2); "("
           Print #fileNo, addTab(3); "sr0Context"
           Print #fileNo, addTab(2); ")"
           Print #fileNo, addTab(2); "SELECT DISTINCT"
           Print #fileNo, addTab(3); "SR0."; g_anSr0Context
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); qualTabNameGenericAspectOrg; " NSR1"
           Print #fileNo, addTab(2); "INNER JOIN"
           Print #fileNo, addTab(3); qualTabNameGenericAspectOrg; " SR1"
           Print #fileNo, addTab(2); "ON"
           Print #fileNo, addTab(3); "NSR1.E1VEX1_OID = SR1."; g_anOid
           Print #fileNo, addTab(2); "INNER JOIN"
           Print #fileNo, addTab(3); qualTabNameGenericAspectOrg; " SR0"
           Print #fileNo, addTab(2); "ON"
           Print #fileNo, addTab(3); "SR1.E0VEX0_OID = SR0."; g_anOid
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "NSR1."; g_anCid; " = '"; getClassIdStrByIndex(g_classIndexNSr1Validity); "'"
           Print #fileNo, addTab(2); "AND"
           Print #fileNo, addTab(3); "NSR1."; g_anPsOid; " = psOid_in"
           Print #fileNo, addTab(2); ";"
 
           genProcSectionHeader fileNo, "first check: compare by 'syntactic containment'", 2
           Print #fileNo, addTab(2); "FOR sr0Loop AS sr0Cursor CURSOR FOR"
           Print #fileNo, addTab(3); "SELECT"
           Print #fileNo, addTab(4); "sr0Context AS fSr0Context"
           Print #fileNo, addTab(3); "FROM"
           Print #fileNo, addTab(4); tempTabNameSr0ContextFac; " F"
           Print #fileNo, addTab(3); "FOR UPDATE OF"
           Print #fileNo, addTab(4); "isMpcRelevant"
           Print #fileNo, addTab(2); "DO"
           Print #fileNo, addTab(3); "SET v_sr0Context = '%' || REPLACE(fSr0Context, '+', '%') || '%';"
           Print #fileNo,
           Print #fileNo, addTab(3); "IF EXISTS(SELECT 1 FROM "; tempTabNameSr0ContextOrg; " M WHERE M.sr0Context LIKE v_sr0Context) THEN"
           Print #fileNo, addTab(4); "UPDATE"
           Print #fileNo, addTab(5); tempTabNameSr0ContextFac
           Print #fileNo, addTab(4); "SET"
           Print #fileNo, addTab(5); "isMpcRelevant = "; gc_dbTrue
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "CURRENT OF sr0Cursor"
           Print #fileNo, addTab(4); ";"
           Print #fileNo, addTab(3); "END IF;"
           Print #fileNo, addTab(2); "END FOR;"
 
           genProcSectionHeader fileNo, "second check (for remaining sr0Contexts): compare by 'set containment'", 2
           Print #fileNo, addTab(2); "FOR sr0Loop AS sr0Cursor CURSOR FOR"
           Print #fileNo, addTab(3); "SELECT"
           Print #fileNo, addTab(4); "sr0Context AS fSr0Context"
           Print #fileNo, addTab(3); "FROM"
           Print #fileNo, addTab(4); tempTabNameSr0ContextFac; " F"
           Print #fileNo, addTab(3); "WHERE"
           Print #fileNo, addTab(4); "F.isMpcRelevant = "; gc_dbFalse
           Print #fileNo, addTab(3); "FOR UPDATE OF"
           Print #fileNo, addTab(4); "isMpcRelevant"
           Print #fileNo, addTab(2); "DO"
           Print #fileNo, addTab(3); "IF EXISTS(SELECT 1 FROM "; tempTabNameSr0ContextOrg; " M WHERE "; qualFuncNameIsSubset; "(fSr0Context, M.sr0Context, CHAR('+')) = 1) THEN"
           Print #fileNo, addTab(4); "UPDATE"
           Print #fileNo, addTab(5); tempTabNameSr0ContextFac
           Print #fileNo, addTab(4); "SET"
           Print #fileNo, addTab(5); "isMpcRelevant = "; gc_dbTrue
           Print #fileNo, addTab(4); "WHERE"
           Print #fileNo, addTab(5); "CURRENT OF sr0Cursor"
           Print #fileNo, addTab(4); ";"
           Print #fileNo, addTab(3); "END IF;"
           Print #fileNo, addTab(2); "END FOR;"
           Print #fileNo,
         End If

         Dim filterBySr0Context As Boolean
         For k = IIf(containsSr0Context, 1, 2) To 2
           filterBySr0Context = (k = 1)
           If (containsSr0Context And Not filterBySr0Context) Then
             Print #fileNo, addTab(1); "ELSE"
           End If
 
           For j = 1 To IIf(g_classes.descriptors(i).isDeletable, 2, 1)
             If (j = 1) Then
               Print #fileNo, addTab(offset + 1); "INSERT INTO"
               Print #fileNo, addTab(offset + 2); gc_tempTabNameChangeLog
               Print #fileNo, addTab(offset + 2); "("
               genAttrListForEntity changeLogClassIndex, eactClass, fileNo, ddlType, srcOrgIndex, srcPoolIndex, offset + 2, , , edomListLrt Or edomListVirtual Or edomVirtualPersisted
               Print #fileNo, addTab(offset + 2); ")"
             Else
               Print #fileNo,
               Print #fileNo, addTab(offset + 1); "UNION"
               Print #fileNo,
             End If
             Print #fileNo, addTab(offset + 1); "SELECT"

             initAttributeTransformation transformation, 0, , True, , "CL."
             tabColumns = nullEntityColumnDescriptors
             genTransformedAttrListForEntityWithColReuse changeLogClassIndex, eactClass, transformation, tabColumns, fileNo, ddlType, , , offset + 2, , , edomListNonLrt Or edomNoDdlComment Or edomColumnName
 
             Print #fileNo, addTab(offset + 1); "FROM"
             Print #fileNo, addTab(offset + 2); qualSrcClTabName; " CL,"
             Print #fileNo, addTab(offset + 2); "("

             Print #fileNo, addTab(offset + 3); "SELECT"
             Print #fileNo, addTab(offset + 4); "CLS.objectId,"
             Print #fileNo, addTab(offset + 4); "CLS.entityId,"
             Print #fileNo, addTab(offset + 4); "CLS.entityType"
             Print #fileNo, addTab(offset + 3); "FROM"
             Print #fileNo, addTab(offset + 4); gc_tempTabNameChangeLogSummary; " CLS"
             Print #fileNo, addTab(offset + 3); "INNER JOIN"
             Print #fileNo, addTab(offset + 4); IIf(j = 2, qualDstTabName, qualSrcTabName); " AHD"
             Print #fileNo, addTab(offset + 3); "ON"
             Print #fileNo, addTab(offset + 4); "AHD."; g_anOid; " = CLS.ahObjectId"
             Print #fileNo, addTab(offset + 5); "AND"
             Print #fileNo, addTab(offset + 4); "CLS.aggregateType = '"; g_classes.descriptors(i).classIdStr; "'"

             ' Fixme: the following handling of navigation paths make implicit assumtions about cardinality of relationships!
             '      : remove this!
             If g_classes.descriptors(i).isPsTagged Then
               Print #fileNo, addTab(offset + 5); "AND"
               Print #fileNo, addTab(offset + 4); "AHD."; g_anPsOid; " = psOid_in"
             ElseIf g_classes.descriptors(i).navPathToDiv.relRefIndex > 0 Then
               navPathToDiv = g_classes.descriptors(i).navPathToDiv
                 If navPathToDiv.navDirection = etLeft Then
                   fkAttrToDiv = g_relationships.descriptors(navPathToDiv.relRefIndex).leftFkColName(ddlType)
                 Else
                   fkAttrToDiv = g_relationships.descriptors(navPathToDiv.relRefIndex).rightFkColName(ddlType)
                 End If

               Print #fileNo, addTab(offset + 5); "AND"
               Print #fileNo, addTab(offset + 4); "AHD."; fkAttrToDiv; " = divisionOid_in"
             End If

             If filterBySr0Context Then
               Print #fileNo, addTab(offset + 3); "INNER JOIN"
               Print #fileNo, addTab(offset + 4); tempTabNameSr0ContextFac; " S0F"
               Print #fileNo, addTab(offset + 3); "ON"
               Print #fileNo, addTab(offset + 4); "S0F.sr0Context = AHD."; g_anSr0Context
               Print #fileNo, addTab(offset + 5); "AND"
               Print #fileNo, addTab(offset + 4); "S0F.isMpcRelevant = "; gc_dbTrue
             End If

             ' FIXME: get rid of this hard-coding!
             If isGenericAspect And (j = 1) Then
               Print #fileNo, addTab(offset + 3); "LEFT OUTER JOIN"
               Print #fileNo, addTab(offset + 4); gc_tempTabNameRelevantCountryIdList; " ACL"
               Print #fileNo, addTab(offset + 3); "ON"
               Print #fileNo, addTab(offset + 4); "AHD.ACLACL_OID = ACL.idListOid"
               Print #fileNo, addTab(offset + 3); "LEFT OUTER JOIN"
               Print #fileNo, addTab(offset + 4); gc_tempTabNameRelevantCountryIdList; " DCL"
               Print #fileNo, addTab(offset + 3); "ON"
               Print #fileNo, addTab(offset + 4); "AHD.DCLDCL_OID = DCL.idListOid"

               Dim qualTabNameGenericCode As String
               qualTabNameGenericCode = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, srcOrgIndex, srcPoolIndex)

               Print #fileNo, addTab(offset + 3); "LEFT OUTER JOIN"
               Print #fileNo, addTab(offset + 4); "("
               Print #fileNo, addTab(offset + 6); qualTabNameGenericCode; " CD"
               Print #fileNo, addTab(offset + 5); "INNER JOIN"
               Print #fileNo, addTab(offset + 6); g_qualTabNameCodeType; " CT"
               Print #fileNo, addTab(offset + 5); "ON"
               Print #fileNo, addTab(offset + 6); "CD.CTYTYP_OID = CT."; g_anOid
               Print #fileNo, addTab(offset + 4); ")"
               Print #fileNo, addTab(offset + 3); "ON"
               Print #fileNo, addTab(offset + 4); "AHD.BCDBCD_OID = CD."; g_anOid
             End If

             Dim fkAttrToAh As String
             Dim qualRelTabName As String
             Dim qualCodeTypeTabName As String
             If g_classes.descriptors(i).navPathToCodeType.relRefIndex > 0 Then
               Dim navPathToCodeType As NavPathFromClassToClass
               Dim fkAttrToCodeType As String
               qualCodeTypeTabName = genQualTabNameByClassIndex(g_classIndexCodeType, ddlType, srcOrgIndex, srcPoolIndex)
               navPathToCodeType = g_classes.descriptors(i).navPathToCodeType

                 If navPathToCodeType.navDirection = etLeft Then
                   fkAttrToCodeType = g_relationships.descriptors(navPathToCodeType.relRefIndex).leftFkColName(ddlType)
                 Else
                   fkAttrToCodeType = g_relationships.descriptors(navPathToCodeType.relRefIndex).rightFkColName(ddlType)
                 End If

               Print #fileNo, addTab(offset + 3); "INNER JOIN"
               Print #fileNo, addTab(offset + 4); qualCodeTypeTabName; " CTY"
               Print #fileNo, addTab(offset + 3); "ON"
               Print #fileNo, addTab(offset + 4); "AHD."; fkAttrToCodeType; " = CTY."; g_anOid
               Print #fileNo, addTab(offset + 5); "AND"
               Print #fileNo, addTab(offset + 4); "CTY.CODETYPENUMBER <> 'H'"
             End If

             ' check which columns we find in this table
             tabColumns = nullEntityColumnDescriptors
             initAttributeTransformation transformation, 0
             genTransformedAttrListForEntityWithColReuse i, eactClass, transformation, tabColumns, fileNo, ddlType, , , , , , edomNone

             Dim fkAttrToOrg As String
             fkAttrToOrg = ""
             If g_classes.descriptors(i).navPathToOrg.relRefIndex > 0 Then
               Dim navPathToOrg As NavPathFromClassToClass
               qualRelTabName = genQualTabNameByRelIndex(g_classes.descriptors(i).navPathToOrg.relRefIndex, ddlType, srcOrgIndex, srcPoolIndex)
               navPathToOrg = g_classes.descriptors(i).navPathToOrg
                 If navPathToOrg.navDirection = etLeft Then
                   fkAttrToOrg = g_relationships.descriptors(navPathToOrg.relRefIndex).leftFkColName(ddlType)
                   fkAttrToAh = g_relationships.descriptors(navPathToOrg.relRefIndex).rightFkColName(ddlType)
                 Else
                   fkAttrToOrg = g_relationships.descriptors(navPathToOrg.relRefIndex).rightFkColName(ddlType)
                   fkAttrToAh = g_relationships.descriptors(navPathToOrg.relRefIndex).leftFkColName(ddlType)
                 End If
               If (j = 1) Then
                 Print #fileNo, addTab(offset + 3); "LEFT OUTER JOIN"
                 Print #fileNo, addTab(offset + 4); qualRelTabName; " VFO"

                 Print #fileNo, addTab(offset + 3); "ON"
                 Print #fileNo, addTab(offset + 4); "AHD."; g_anOid; " = VFO."; fkAttrToAh
               End If
             End If

             Dim firstCondition As Boolean
             Dim printedWhere As Boolean
             firstCondition = True
             printedWhere = False

             If fkAttrToOrg <> "" And (j = 1) Then
               Print #fileNo, addTab(offset + 3); "WHERE"
               printedWhere = True

               Print #fileNo, addTab(offset + 4); "COALESCE(VFO."; fkAttrToOrg; ", orgOid_in) = orgOid_in"
               firstCondition = False
             End If

             Dim m As Integer
             For m = 1 To tabColumns.numDescriptors
                 If tabColumns.descriptors(m).columnName = g_anIsNotPublished Then
                   If Not printedWhere Then
                     Print #fileNo, addTab(offset + 3); "WHERE"
                     printedWhere = True
                   End If
                   If Not firstCondition Then
                     Print #fileNo, addTab(offset + 5); "AND"
                   End If
                   firstCondition = False
                   Print #fileNo, addTab(offset + 4); "AHD."; g_anIsNotPublished; " = "; gc_dbFalse
                 ElseIf tabColumns.descriptors(m).columnName = g_anSr0Context Then
                   containsSr0Context = True
                 End If
             Next m

             If isGenericAspect And (j = 1) Then
               If Not printedWhere Then
                 Print #fileNo, addTab(offset + 3); "WHERE"
                 printedWhere = True
               End If
               If Not firstCondition Then
                 Print #fileNo, addTab(offset + 5); "AND"
               End If
               firstCondition = False
               Print #fileNo, addTab(offset + 4); "(CD."; g_anOid; " IS NULL OR (CD."; g_anIsNotPublished; " = 0 AND CT.CODETYPENUMBER <> 'H'))"
               Print #fileNo, addTab(offset + 5); "AND"
               Print #fileNo, addTab(offset + 4); "("
               Print #fileNo, addTab(offset + 5); "AHD."; g_anCid; " = '"; g_classes.descriptors(g_classIndexSr0Validity).classIdStr; "'"
               Print #fileNo, addTab(offset + 6); "OR"
               Print #fileNo, addTab(offset + 5); "("

               Print #fileNo, addTab(offset + 6); "-- at least one country exists in the 'allowed countries list' which is managed by 'this Organization' and not disallowed in the 'disallowed countries list'"
               Print #fileNo, addTab(offset + 6); "EXISTS ("
               Print #fileNo, addTab(offset + 7); "SELECT"
               Print #fileNo, addTab(offset + 8); "1"
               Print #fileNo, addTab(offset + 7); "FROM"
               Print #fileNo, addTab(offset + 8); gc_tempTabNameRelevantCountryIdXRef; " AX"
               Print #fileNo, addTab(offset + 7); "WHERE"
               Print #fileNo, addTab(offset + 8); "AX.idListOid = ACL.idListOid"
               Print #fileNo, addTab(offset + 9); "AND"
               Print #fileNo, addTab(offset + 8); "NOT EXISTS ("
               Print #fileNo, addTab(offset + 9); "SELECT"
               Print #fileNo, addTab(offset + 10); "1"
               Print #fileNo, addTab(offset + 9); "FROM"
               Print #fileNo, addTab(offset + 10); gc_tempTabNameRelevantCountryIdXRef; " DX"
               Print #fileNo, addTab(offset + 9); "WHERE"
               Print #fileNo, addTab(offset + 10); "DX.countryOid = AX.countryOid"
               Print #fileNo, addTab(offset + 11); "AND"
               Print #fileNo, addTab(offset + 10); "DX.idListOid = DCL.idListOid"
               Print #fileNo, addTab(offset + 8); ")"
               Print #fileNo, addTab(offset + 6); ")"

               Print #fileNo, addTab(offset + 5); ")"
               Print #fileNo, addTab(offset + 4); ")"
             End If

             If g_classes.descriptors(i).isDeletable Then
               If Not isGenericAspect Then
                 If Not printedWhere Then
                   Print #fileNo, addTab(offset + 3); "WHERE"
                   printedWhere = True
                 End If
                 If Not firstCondition Then
                   Print #fileNo, addTab(offset + 5); "AND"
                 End If
                 Print #fileNo, addTab(offset + 4); "CLS.isDeleted = "; IIf(j = 1, gc_dbFalse, gc_dbTrue)
               End If
             End If

             Print #fileNo, addTab(offset + 2); ") FLTR"
             Print #fileNo, addTab(offset + 1); "WHERE"
             Print #fileNo, addTab(offset + 2); "CL.objectId = FLTR.objectId"
             Print #fileNo, addTab(offset + 3); "AND"
             Print #fileNo, addTab(offset + 2); "CL."; g_anAcmEntityId; " = FLTR."; g_anAcmEntityId
             Print #fileNo, addTab(offset + 3); "AND"
             Print #fileNo, addTab(offset + 2); "CL."; g_anAcmEntityType; " = FLTR."; g_anAcmEntityType
             Print #fileNo, addTab(offset + 3); "AND"
             Print #fileNo, addTab(offset + 2); "CL.OPTIMESTAMP > startTimestamp_in"
             Print #fileNo, addTab(offset + 3); "AND"
             Print #fileNo, addTab(offset + 2); "CL.OPTIMESTAMP <= endTimestamp_in"
             If isGenericAspect Then
             'special handling CodeBaumusterValidity: no changes in MPC in this case
                 Print #fileNo, addTab(offset + 3); "AND NOT ( CL.OPERATION_ID = 2 AND CL.AHCLASSID = '09006' AND CL.DBCOLUMNNAME = 'ISBLOCKEDPRICE' AND v_takeoverCBVFlag = 0)"
             End If

             If (j = IIf(g_classes.descriptors(i).isDeletable, 2, 1)) Then
               Print #fileNo, addTab(offset + 1); ";"
             End If
           Next j
         Next k
 
         If containsSr0Context Then
           Print #fileNo, addTab(1); "END IF;"
         End If

         genProcSectionHeader fileNo, "count the number of affected rows"
         Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

         genSpLogProcExit fileNo, qualProcName, ddlType, , "psOid_in", "divisionOid_in", "orgOid_in", "filterBySr0Context_in", "#startTimestamp_in", "#endTimestamp_in", "rowCount_out"

         Print #fileNo, addTab(0); "END"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       End If
   Next i
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 
 Private Sub genFactoryTakeOverDdlByOrg4( _
   srcOrgIndex As Integer, _
   dstOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If generateFwkTest Then
     Exit Sub
   End If

   If ddlType = edtPdm And (srcOrgIndex < 1 Or dstOrgIndex < 1 Or srcPoolIndex < 1 Or dstPoolIndex < 1) Then
     ' Factory-Take-Over is only supported at 'pool-level'
     Exit Sub
   End If
 
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, dstOrgIndex, dstPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(-1, ddlType)

   ' ####################################################################################################################
   ' #    SP for initial factory takeover
   ' ####################################################################################################################

   Dim qualProcedureNameInitialFTO As String
   qualProcedureNameInitialFTO = genQualProcName(g_sectionIndexAliasLrt, spnFtoInitial, ddlType, dstOrgIndex, dstPoolIndex)

   printSectionHeader "SP for initial factory takeover", fileNo

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameInitialFTO
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "orgOid_in", g_dbtOid, True, " the organization oid"
   genProcParm fileNo, "IN", "psOid", g_dbtOid, True, "the productstructure oid"
   genProcParm fileNo, "IN", "divisionOid", g_dbtOid, True, "the oid of the productstructure's division"
   genProcParm fileNo, "IN", "lrtOid", g_dbtOid, True, "the lrt oid"
   genProcParm fileNo, "IN", "cdUserId", "VARCHAR(16)", True, "the lrt's user id"
   genProcParm fileNo, "OUT", "endTimestamp_out", "TIMESTAMP", True, "marks the 'end timestamp' for data being taken over"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being taken over"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare conditions", , True
   genCondDecl fileNo, "notFound", "02000"
 
   genProcSectionHeader fileNo, "declare variables"
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(32000)", "NULL"
   genVarDecl fileNo, "v_stmntTxtTerm", "VARCHAR(32000)", "NULL"
   genVarDecl fileNo, "v_colList", "VARCHAR(8000)", "NULL"
   genVarDecl fileNo, "v_colListForSelect", "VARCHAR(8000)", "NULL"
   genVarDecl fileNo, "v_IsBlockedPriceExpression", "VARCHAR(1000)", "NULL"
   genVarDecl fileNo, "v_fltrTxt", "VARCHAR(1600)", "NULL"
   genVarDecl fileNo, "v_deleteFltrTxt", "VARCHAR(800)", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_rowCount2", "INTEGER", "0"
   genVarDecl fileNo, "v_lrtCount", "INTEGER", "0"
   genVarDecl fileNo, "v_aliasSchemaName", "VARCHAR(100)", "NULL"
   genVarDecl fileNo, "v_orgId", "SMALLINT", "0"
   genVarDecl fileNo, "v_idx", "INTEGER", "0"
   genVarDecl fileNo, "v_len", "INTEGER", "0"
   genVarDecl fileNo, "v_stmtSinceFrom", "VARCHAR(32000)", "NULL"
   genVarDecl fileNo, "v_insert", "VARCHAR(32000)", "NULL"

   genSpLogDecl fileNo
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
   genProcSectionHeader fileNo, "declare cursor"
   Print #fileNo, addTab(1); "DECLARE v_stmt_cursor CURSOR FOR v_stmnt;"

   genProcSectionHeader fileNo, "declare condition handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genProcSectionHeader fileNo, "temporary table for Termoids"
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); "SESSION.Termoids"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "termOid "; g_dbtOid; ","
   Print #fileNo, addTab(2); "termAhOid "; g_dbtOid
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1

   genSpLogProcEnter fileNo, qualProcedureNameInitialFTO, ddlType, 1, "orgOid_in", "psOid", "divisionOid", "lrtOid", "cdUserId", "endTimestamp_out", "rowCount_out"

   genDb2RegVarCheckDdl fileNo, ddlType, dstOrgIndex, dstPoolIndex, tvNull, 1

   Dim aliasSchemaName As String
   ' contains <<mpcId>>
   If Len(qualProcedureNameInitialFTO) > 35 Then
     aliasSchemaName = Mid(qualProcedureNameInitialFTO, 1, 17)
   Else
     aliasSchemaName = Mid(qualProcedureNameInitialFTO, 1, 10)
   End If
   Print #fileNo, addTab(1); "SET v_aliasSchemaName = '"; aliasSchemaName; "';"

   genProcSectionHeader fileNo, "determine Organization's ID"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "ID"
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_orgId"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); g_qualTabNamePdmOrganization
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "ORGOID = orgOid_in"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"
   Print #fileNo, addTab(1); "SET endTimestamp_out = CURRENT TIMESTAMP;"

   genProcSectionHeader fileNo, "copy data from factory productive data pool to organization's work data pool (into LRT tables)"
   Print #fileNo, addTab(1); "FOR tabLoop AS tabCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " AS c_entitySection,"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " AS c_entityName,"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityShortName; " AS c_entityShortName,"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " AS c_entityType,"
   Print #fileNo, addTab(3); "A."; g_anAcmIsPs; " AS c_isPs,"
   Print #fileNo, addTab(3); "A.ISLRT AS c_isLrt,"
   Print #fileNo, addTab(3); "A.USELRTMQT AS c_useLrtMqt,"
   Print #fileNo, addTab(3); "A1."; conAcmEntityShortName; " AS c_divPrefix,"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityName; " AS c_ahClassName,"
   Print #fileNo, addTab(3); "PF."; g_anPdmFkSchemaName; " AS c_srcTabSchemaName,"
   Print #fileNo, addTab(3); "PO."; g_anPdmFkSchemaName; " AS c_tgtTabSchemaName,"
   Print #fileNo, addTab(3); "PO."; g_anPdmTableName; " AS c_tabName,"
   Print #fileNo, addTab(3); "PO."; g_anPoolTypeId; " AS c_poolTypeId,"
   Print #fileNo, addTab(3); "PH."; g_anPdmFkSchemaName; " AS c_ahTabSchemaName,"
   Print #fileNo, addTab(3); "PH."; g_anPdmTableName; " AS c_ahTabName,"
   Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " AS c_isGen,"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " AS c_isNl,"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " AS c_fkSequenceNo,"
   Print #fileNo, addTab(3); "(CASE WHEN D.SRC_SCHEMANAME IS NULL THEN 0 ELSE 1 END) AS c_hasSelfReference"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
 
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PO"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PO."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PO."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
 
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PF"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PF."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PF."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " AH"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityId; " = A."; g_anAhCid; ""
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " LH"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntitySection; " = LH."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityName; " = LH."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "AH."; g_anAcmEntityType; " = LH."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LH."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LH."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LH."; g_anLdmIsLrt; " = "; gc_dbFalse

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " PH"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "PH."; g_anPdmLdmFkSchemaName; " = LH."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PH."; g_anPdmLdmFkTableName; " = LH."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PH."; g_anOrganizationId; " = v_orgId"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COALESCE(PH."; g_anPoolTypeId; ","; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameFkDependency; " D"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "D.SRC_SCHEMANAME = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "D.SRC_TABLENAME = L."; g_anLdmTableName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "D.DST_SCHEMANAME = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "D.DST_TABLENAME = L."; g_anLdmTableName

   Print #fileNo, addTab(2); "LEFT OUTER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A1"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = A1."; conAcmLeftEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A1."; conAcmRightEntityName; " = 'DIVISION'"
 
   Print #fileNo, addTab(2); "WHERE"

   Dim firstException As Boolean
   Dim exceptionComment As String
   Dim thisExceptionComment As String
   firstException = True
   exceptionComment = ""
   Dim i As Integer
   For i = 1 To g_relationships.numDescriptors
     thisExceptionComment = ""
       If Not g_relationships.descriptors(i).isUserTransactional And ((g_relationships.descriptors(i).maxLeftCardinality = -1 And g_relationships.descriptors(i).maxRightCardinality = -1) Or g_relationships.descriptors(i).isNl) And _
          (UCase(g_relationships.descriptors(i).sectionName) = UCase(snOrder) Or UCase(g_relationships.descriptors(i).sectionName) = UCase(snReport) Or UCase(g_relationships.descriptors(i).sectionName) = UCase(snPricing)) Then
         thisExceptionComment = "exclude """ & UCase(g_relationships.descriptors(i).sectionName) & "." & UCase(g_relationships.descriptors(i).relName) & """ (section """ & UCase(g_relationships.descriptors(i).sectionName) & """)"
       End If

       If thisExceptionComment <> "" Then
         If firstException Then
           Print #fileNo, addTab(3); "NOT ("
           Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "A."; g_anAcmEntityId; " IN ("
         Else
           Print #fileNo, ","; IIf(exceptionComment <> "", " -- " & exceptionComment, "")
         End If
         exceptionComment = thisExceptionComment
         Print #fileNo, addTab(5); "'"; UCase(g_relationships.descriptors(i).relIdStr); "'";
         firstException = False
       End If
   Next i

   If exceptionComment <> "" Then
     genProcSectionHeader fileNo, exceptionComment, 1, True

     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); ")"

     Print #fileNo, addTab(4); "AND"
   End If

   firstException = True
   exceptionComment = ""
   For i = 1 To g_classes.numDescriptors
     thisExceptionComment = ""
       If g_classes.descriptors(i).classIndex = g_classIndexPricePreferences Then
         thisExceptionComment = "exclude """ & UCase(g_classes.descriptors(i).sectionName) & "." & UCase(g_classes.descriptors(i).className) & """ (already initialized)"
       End If

       If (g_classes.descriptors(i).superClassIndex <= 0 And Not g_classes.descriptors(i).isUserTransactional) Or g_classes.descriptors(i).classIndex = g_classIndexTaxParameter Or g_classes.descriptors(i).classIndex = g_classIndexTaxType Then
         If UCase(g_classes.descriptors(i).sectionName) = UCase(snOrder) Or UCase(g_classes.descriptors(i).sectionName) = UCase(snReport) Or UCase(g_classes.descriptors(i).sectionName) = UCase(snPricing) Then
           thisExceptionComment = "exclude """ & UCase(g_classes.descriptors(i).sectionName) & "." & UCase(g_classes.descriptors(i).className) & """ (section """ & UCase(g_classes.descriptors(i).sectionName) & """)"
         End If
       End If

       If thisExceptionComment <> "" Then
         If firstException Then
           Print #fileNo, addTab(3); "NOT ("
           Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "A."; g_anAcmEntityId; " IN ("
         Else
           Print #fileNo, ","; IIf(exceptionComment <> "", " -- " & exceptionComment, "")
         End If
         exceptionComment = thisExceptionComment
         Print #fileNo, addTab(5); "'"; UCase(g_classes.descriptors(i).classIdStr); "'";
         firstException = False
       End If
   Next i

   If exceptionComment <> "" Then
     genProcSectionHeader fileNo, exceptionComment, 1, True

     Print #fileNo, addTab(4); ")"
     Print #fileNo, addTab(3); ")"

     Print #fileNo, addTab(4); "AND"
   End If

   Print #fileNo, addTab(3); "(A."; g_anAcmIsLrt; " = 1 OR PO."; g_anPoolTypeId; " IS NULL)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A.ISCTO = 0"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A.ISCTP = 0"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PO."; g_anOrganizationId; " = v_orgId"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COALESCE(PO."; g_anPoolTypeId; ","; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "PF."; g_anOrganizationId; " = "; CStr(g_primaryOrgId)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "COALESCE(PF."; g_anPoolTypeId; ","; CStr(g_productiveDataPoolId); ") = "; CStr(g_productiveDataPoolId)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "(CASE WHEN A."; g_anAhCid; " IS NULL THEN 0 ELSE 1 END) DESC,"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " ASC"
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader fileNo, "determine common columns in source and target table", 2, True
   Print #fileNo, addTab(2); "SET v_colList = '';"
   Print #fileNo, addTab(2); "SET v_colListForSelect = '';"
   Print #fileNo, addTab(2); "FOR colLoop AS colCursor CURSOR WITH HOLD FOR"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "S.COLNAME AS V_COLNAME"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS S"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS T"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "S.TABNAME = T.TABNAME"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "S.COLNAME = T.COLNAME"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "S.TABSCHEMA = c_tgtTabSchemaName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "T.TABSCHEMA = c_srcTabSchemaName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "T.TABNAME = c_tabName"
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "S.COLNO"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "SET v_colList = v_colList || (CASE v_colList WHEN '' THEN '' ELSE ',' END) || V_COLNAME;"
   Print #fileNo, addTab(3); "IF c_tabName = 'GENERICASPECT' AND V_COLNAME = 'ISBLOCKEDPRICE' THEN"
   Print #fileNo, addTab(4); "SET v_IsBlockedPriceExpression = 'CASE WHEN S.CLASSID = ''09006'' AND ((SELECT P.ISDPB FROM VL6CMET' || RIGHT(DIGITS(v_orgId),2) || '.PRICEPREFERENCES P WHERE P.PS_OID = S.PS_OID) = 0) THEN 1 ELSE S.ISBLOCKEDPRICE END';"
   Print #fileNo, addTab(4); "SET v_colListForSelect = v_colListForSelect || (CASE v_colListForSelect WHEN '' THEN '' ELSE ',' END) || v_IsBlockedPriceExpression;"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_colListForSelect = v_colListForSelect || (CASE v_colListForSelect WHEN '' THEN '' ELSE ',' END) || V_COLNAME;"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"

   Print #fileNo, addTab(2); "SET v_stmntTxt = 'INSERT INTO ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || '(' || v_colList || ')' || ' SELECT ' || v_colListForSelect || ' FROM ' || RTRIM(c_srcTabSchemaName) || '.' || RTRIM(c_tabName) || ' S WHERE (1=1)';"

   genProcSectionHeader fileNo, "for PS-tagged tables: exclude records corresponding to PRODUCTSTRUCTURE under construction or to not relevant product structures/divisions", 2
   Print #fileNo, addTab(2); "IF (c_isPs = 1) AND (c_isNl = 0) THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM "; g_qualTabNameProductStructure; " PS WHERE S."; g_anPsOid; " = PS."; g_anOid; " AND PS."; g_anOid; " = ' || psOid || ' AND PS."; g_anIsUnderConstruction; " = 0)';"
   Print #fileNo, addTab(2); "ELSEIF (c_isNl = 0) AND (c_isGen = 0) AND (c_divPrefix IS NOT NULL) THEN"
   genProcSectionHeader fileNo, "filter out already existing GenericCodes", 2
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND NOT EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' TP WHERE TP.OID = S.OID';"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND S.' || c_divPrefix ||  'DIV_OID = ' || divisionOid || ')';"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND S.' || c_divPrefix ||  'DIV_OID = ' || divisionOid;"
   Print #fileNo, addTab(2); "ELSEIF (c_isNl = 0) AND (c_isGen = 0) AND (c_divPrefix IS NULL) THEN"
   genProcSectionHeader fileNo, "filter out already existing entries like EndNodeHasGenericCode, CODEVALIDFORORGANIZATION", 2
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND NOT EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' TP WHERE TP.OID = S.OID)';"
   Print #fileNo, addTab(2); "ELSEIF (c_isNl = 1) AND (c_isPs = 0) THEN"
   genProcSectionHeader fileNo, "filter out already existing GenericCode-NlText", 2
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND NOT EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' TP WHERE TP.OID = S.OID)';"
   Print #fileNo, addTab(2); "END IF;"

   Dim viewName As String
   genProcSectionHeader fileNo, "exclude records referring to aggregate heads not relevant for this organization", 2
   Print #fileNo, addTab(2); "IF (c_ahTabSchemaName IS NOT NULL AND c_ahTabName IS NOT NULL) AND (c_ahTabSchemaName <> c_tgtTabSchemaName OR c_ahTabName <> c_tabName) AND (c_ahClassName <> '"; UCase(clnExpression); "') THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_ahTabName) || ' AH WHERE S."; g_anAhOid; " = AH."; g_anOid; ")';"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "for GEN- and NL_TEXT-tables: exclude records referring to 'parent records' not relevant for this organization'", 2
   Print #fileNo, addTab(2); "IF (c_isNl = 1) THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(REPLACE(c_tabName, '_NL_TEXT', '')) || ' PAR WHERE S.' || RTRIM(c_entityShortName) || '_OID = PAR."; g_anOid; ")';"
   Print #fileNo, addTab(2); "ELSEIF (c_isGen = 1) THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt  || ' AND EXISTS (SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(REPLACE(c_tabName, '_GEN', '')) || ' PAR WHERE S.' || RTRIM(c_entityShortName) || '_OID = PAR."; g_anOid; ")';"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "for relationship-tables: filter by foreign keys referring to records not relevant for this organization", 2
   Print #fileNo, addTab(2); "IF c_entityType = '"; gc_acmEntityTypeKeyRel; "' AND c_isNl = "; gc_dbFalse; " THEN"
   Print #fileNo, addTab(3); "SET v_fltrTxt = NULL;"
   Print #fileNo,
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "(CASE WHEN PL."; g_anOrganizationId; " IS NULL THEN '' ELSE ' AND EXISTS (SELECT 1 FROM "; aliasSchemaName; ".' || RTRIM(PL."; g_anPdmTableName; ") || ' L WHERE S.' || AL."; g_anAcmEntityShortName; " || '_OID = L."; g_anOid; ")' END) ||"
   Print #fileNo, addTab(4); "(CASE WHEN PR."; g_anOrganizationId; " IS NULL THEN '' ELSE ' AND EXISTS (SELECT 1 FROM "; aliasSchemaName; ".' || RTRIM(PR."; g_anPdmTableName; ") || ' R WHERE S.' || AR."; g_anAcmEntityShortName; " || '_OID = R."; g_anOid; ")' END)"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_fltrTxt"
   Print #fileNo, addTab(3); "FROM"

   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " AL"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "A."; g_anAcmLeftEntitySection; " = AL."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmLeftEntityName; " = AL."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A.LEFT_"; g_anAcmEntityType; " = AL."; g_anAcmEntityType
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " ALPar"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "ALPar."; g_anAcmEntitySection; " = COALESCE(AL."; g_anAcmOrParEntitySection; ", AL."; g_anAcmEntitySection; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "ALPar."; g_anAcmEntityName; " = COALESCE(AL."; g_anAcmOrParEntityName; ", AL."; g_anAcmEntityName; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "ALPar."; g_anAcmEntityType; " = AL."; g_anAcmOrParEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " AR"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "A."; g_anAcmRightEntitySection; " = AR."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmRightEntityName; " = AR."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmRightEntityType; " = AR."; g_anAcmEntityType
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " ARPar"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "ARPar."; g_anAcmEntitySection; " = COALESCE(AR."; g_anAcmOrParEntitySection; ", AR."; g_anAcmEntitySection; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "ARPar."; g_anAcmEntityName; " = COALESCE(AR."; g_anAcmOrParEntityName; ", AR."; g_anAcmEntityName; ")"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "ARPar."; g_anAcmEntityType; " = AR."; g_anAcmOrParEntityType
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " LL"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "LL."; g_anAcmEntitySection; " = ALPar."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anAcmEntityName; " = ALPar."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anAcmEntityType; " = ALPar."; g_anAcmEntityType
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LL."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameLdmTable; " LR"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "LR."; g_anAcmEntitySection; " = ARPar."; g_anAcmEntitySection
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anAcmEntityName; " = ARPar."; g_anAcmEntityName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anAcmEntityType; " = ARPar."; g_anAcmEntityType
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "LR."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " PL"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "PL."; g_anPdmLdmFkSchemaName; " = LL."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "PL."; g_anPdmLdmFkTableName; " = LL."; g_anLdmTableName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(PL."; g_anOrganizationId; ",v_orgId) = v_orgId"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(PL."; g_anPoolTypeId; ","; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(3); "LEFT OUTER JOIN"
   Print #fileNo, addTab(4); g_qualTabNamePdmTable; " PR"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "PR."; g_anPdmLdmFkSchemaName; " = LR."; g_anLdmSchemaName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "PR."; g_anPdmLdmFkTableName; " = LR."; g_anLdmTableName
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(PR."; g_anOrganizationId; ",v_orgId) = v_orgId"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "COALESCE(PR."; g_anPoolTypeId; ","; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(3); "WHERE"

   Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " = c_entityType"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntityName; " = c_entityName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "A."; g_anAcmEntitySection; " = c_entitySection"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(A."; g_anAcmMaxLeftCardinality; " IS NULL AND A."; g_anAcmMaxRightCardinality; " IS NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(PL."; g_anOrganizationId; " IS NOT NULL OR PR."; g_anOrganizationId; " IS NOT NULL)"
   Print #fileNo, addTab(3); "WITH UR;"
 
   Print #fileNo,
   Print #fileNo, addTab(3); "IF v_fltrTxt IS NOT NULL THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = v_stmntTxt || v_fltrTxt;"
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "apply some table-specific filter", 2
   Print #fileNo, addTab(2); "FOR filterLoop AS"
   Print #fileNo, addTab(3); "WITH"
   Print #fileNo, addTab(4); "V_EntityFilter"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "entitySection,"
   Print #fileNo, addTab(4); "entityName,"
   Print #fileNo, addTab(4); "entityType,"
   Print #fileNo, addTab(4); "forGen,"
   Print #fileNo, addTab(4); "forNl,"
   Print #fileNo, addTab(4); "filter"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AS"
   Print #fileNo, addTab(3); "("

   genProcSectionHeader fileNo, "dummy-entry - first record", 4, True
   Print #fileNo, addTab(4); "VALUES('-none-', '-none-', 'X', 0, 0, '0=1')"

   Dim qualFuncNameHasAlCountry As String
   For i = 1 To g_classes.numDescriptors
       If UCase(g_classes.descriptors(i).className) = UCase(clnGenericCode) Then
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude CODEs by type", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; "', 0, 0, 'S.CTYTYP_OID <> 128')"
       ElseIf UCase(g_classes.descriptors(i).className) = UCase(clnGenericAspect) Then
         qualFuncNameHasAlCountry = genQualFuncName(g_classes.descriptors(i).sectionIndex, "HASALCNTRY", ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude " & g_classes.descriptors(i).className & "s not valid for this organization", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; _
               "', 0, 0, 'S.ACLACL_OID IS NULL OR ("; qualFuncNameHasAlCountry; "(S."; g_anOid; ",S."; g_anCid; ",' || RTRIM(CHAR(orgOid_in)) || ')=1)')"
       ElseIf UCase(g_classes.descriptors(i).className) = UCase(clnDecisionTable) Then
         qualFuncNameHasAlCountry = genQualFuncName(g_classes.descriptors(i).sectionIndex, "HASALCNTRY", ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude " & g_classes.descriptors(i).className & "s not valid for this organization", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; _
               "', 0, 0, '"; qualFuncNameHasAlCountry; "(S."; g_anOid; ",' || RTRIM(CHAR(orgOid_in)) || ')=1')"
       End If
       If UCase(g_classes.descriptors(i).className) = UCase(clnView) Then
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude non SR1/SR0-VIEWS and deletable VIEWS", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; "', 0, 0, 'UPPER(S."; g_anName; ") IN (''SR0'',''SR1'') AND S."; g_anIsDeletable; " = 0')"
       End If
       If g_classes.descriptors(i).navPathToOrg.relRefIndex > 0 Then
         Print #fileNo, addTab(4); "UNION ALL"

         Dim qualRelTabOrg As String, relOrgEntityIdStr As String
         qualRelTabOrg = genQualTabNameByRelIndex(g_classes.descriptors(i).navPathToOrg.relRefIndex, ddlType, g_primaryOrgIndex, g_productiveDataPoolIndex)

         Dim fkAttrToOrg As String
         Dim fkAttrToAh As String
         If g_classes.descriptors(i).navPathToOrg.navDirection = etLeft Then
             fkAttrToOrg = g_relationships.descriptors(g_classes.descriptors(i).navPathToOrg.relRefIndex).leftFkColName(ddlType)
             fkAttrToAh = g_relationships.descriptors(g_classes.descriptors(i).navPathToOrg.relRefIndex).rightFkColName(ddlType)
         Else
             fkAttrToOrg = g_relationships.descriptors(g_classes.descriptors(i).navPathToOrg.relRefIndex).rightFkColName(ddlType)
             fkAttrToAh = g_relationships.descriptors(g_classes.descriptors(i).navPathToOrg.relRefIndex).leftFkColName(ddlType)
         End If

         genProcSectionHeader fileNo, "exclude '" & UCase(g_classes.descriptors(i).className) & "s not relevant for this organization'", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; "', 0, 0, 'NOT EXISTS (SELECT 1 FROM "; qualRelTabOrg; " V WHERE V."; fkAttrToAh; " = S."; g_anOid; ") OR EXISTS (SELECT 1 FROM "; qualRelTabOrg; " V WHERE V."; fkAttrToAh; " = S."; g_anOid; " AND V."; fkAttrToOrg; " = ' || RTRIM(CHAR(orgOid_in)) || ')')"
       End If
       If g_classes.descriptors(i).containsIsNotPublishedInclSubClasses And g_classes.descriptors(i).superClassIndex <= 0 Then
         Print #fileNo, addTab(4); "UNION ALL"
         genProcSectionHeader fileNo, "exclude 'not published " & UCase(g_classes.descriptors(i).className) & "s'", 4, True
         Print #fileNo, addTab(4); "VALUES('"; UCase(g_classes.descriptors(i).sectionName); "', '"; UCase(g_classes.descriptors(i).className); "', '"; gc_acmEntityTypeKeyClass; "', 0, 0, 'S."; g_anIsNotPublished; " = 0')"
       End If
   Next i

   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "V.filter AS c_filter"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "V_EntityFilter V"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "V.entitySection = c_entitySection"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V.entityName = c_entityName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V.entityType = c_entityType"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V.forGen = c_isGen"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V.forNl = c_isNl"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "IF c_filter = '0=1' THEN"
   Print #fileNo, addTab(4); "SET v_stmntTxt = NULL;"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_stmntTxt = v_stmntTxt  || ' AND (' || c_filter || ')';"
   Print #fileNo, addTab(3); "END IF;"
   Print #fileNo, addTab(2); "END FOR;"

   genProcSectionHeader fileNo, "apply Foreign-Key-based filter", 2
   Print #fileNo, addTab(2); "SET v_deleteFltrTxt = '';"
   Print #fileNo, addTab(2); "FOR fkLoop AS"
   Print #fileNo, addTab(3); "WITH"
   Print #fileNo, addTab(4); "V_FkCandidates"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "srcOrParentEntitySection,"
   Print #fileNo, addTab(4); "srcOrParentEntityName,"
   Print #fileNo, addTab(4); "srcEntityType,"
   Print #fileNo, addTab(4); "tgtOrParentEntitySection,"
   Print #fileNo, addTab(4); "tgtOrParentEntityName,"
   Print #fileNo, addTab(4); "tgtEntityType,"
   Print #fileNo, addTab(4); "fkColName,"
   Print #fileNo, addTab(4); "isEnforced"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AS"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT DISTINCT"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(L."; g_anAcmOrParEntitySection; ", L."; g_anAcmEntitySection; ") ELSE COALESCE(R."; g_anAcmOrParEntitySection; ", R."; g_anAcmEntitySection; ") END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(L."; g_anAcmOrParEntityName; ",    L."; g_anAcmEntityName; ")    ELSE COALESCE(R."; g_anAcmOrParEntityName; ",    R."; g_anAcmEntityName; ")    END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN L."; g_anAcmEntityType; " ELSE R."; g_anAcmEntityType; " END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(R."; g_anAcmOrParEntitySection; ", R."; g_anAcmEntitySection; ") ELSE COALESCE(L."; g_anAcmOrParEntitySection; ", L."; g_anAcmEntitySection; ") END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(R."; g_anAcmOrParEntityName; ",    R."; g_anAcmEntityName; ")    ELSE COALESCE(L."; g_anAcmOrParEntityName; ",    L."; g_anAcmEntityName; ")    END ),"
   Print #fileNo, addTab(5); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN R."; g_anAcmEntityType; " ELSE L."; g_anAcmEntityType; " END ),"
   Print #fileNo, addTab(5); "COALESCE(E."; g_anAcmAliasShortName; ", E."; g_anAcmEntityShortName; ") ||"
   Print #fileNo, addTab(6); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN E."; g_anAcmLrShortName; " ELSE E."; g_anAcmRlShortName; " END ) || '_OID',"
   Print #fileNo, addTab(6); "E."; g_anAcmIsEnforced
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity; " E"
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity; " L"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "E."; g_anAcmLeftEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E."; g_anAcmLeftEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E."; g_anAcmLeftEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNameAcmEntity; " R"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "E."; g_anAcmRightEntitySection; " = R."; g_anAcmEntitySection
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E."; g_anAcmRightEntityName; " = R."; g_anAcmEntityName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E."; g_anAcmRightEntityType; " = R."; g_anAcmEntityType
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "E."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "'"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(L."; g_anAcmOrParEntitySection; ", L."; g_anAcmEntitySection; ") ELSE COALESCE(R."; g_anAcmOrParEntitySection; ", R."; g_anAcmEntitySection; ") END) = c_entitySection"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN COALESCE(L."; g_anAcmOrParEntityName; ", L."; g_anAcmEntityName; ") ELSE COALESCE(R."; g_anAcmOrParEntityName; ", R."; g_anAcmEntityName; ") END) = c_entityName"
   Print #fileNo, addTab(7); "AND"
   Print #fileNo, addTab(6); "(CASE WHEN E."; g_anAcmMaxRightCardinality; " = 1 THEN L."; g_anAcmEntityType; " ELSE R."; g_anAcmEntityType; " END) = c_entityType"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "COALESCE(E."; g_anAcmMaxLeftCardinality; ",0) = 1"
   Print #fileNo, addTab(7); "OR"
   Print #fileNo, addTab(6); "COALESCE(E."; g_anAcmMaxRightCardinality; ",0) = 1"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(3); "),"
   Print #fileNo, addTab(4); "V_FksByTab"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "srcOrParentEntitySection,"
   Print #fileNo, addTab(4); "srcOrParentEntityName,"
   Print #fileNo, addTab(4); "srcEntityType,"
   Print #fileNo, addTab(4); "tgtOrParentEntitySection,"
   Print #fileNo, addTab(4); "tgtOrParentEntityName,"
   Print #fileNo, addTab(4); "tgtEntityType,"
   Print #fileNo, addTab(4); "fkColName,"
   Print #fileNo, addTab(4); "isEnforced,"
   Print #fileNo, addTab(4); "srcTabName,"
   Print #fileNo, addTab(4); "srcTabSchema,"
   Print #fileNo, addTab(4); "tgtTabName,"
   Print #fileNo, addTab(4); "tgtTabSchema,"
   Print #fileNo, addTab(4); "isSelfReference"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "AS"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "E.srcOrParentEntitySection,"
   Print #fileNo, addTab(5); "E.srcOrParentEntityName,"
   Print #fileNo, addTab(5); "E.srcEntityType,"
   Print #fileNo, addTab(5); "E.tgtOrParentEntitySection,"
   Print #fileNo, addTab(5); "E.tgtOrParentEntityName,"
   Print #fileNo, addTab(5); "E.tgtEntityType,"
   Print #fileNo, addTab(5); "E.fkColName,"
   Print #fileNo, addTab(5); "E.isEnforced,"
   Print #fileNo, addTab(5); "P_SRC."; g_anPdmTableName; ","
   Print #fileNo, addTab(5); "P_SRC."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(5); "P_TGT."; g_anPdmTableName; ","
   Print #fileNo, addTab(5); "P_TGT."; g_anPdmFkSchemaName; ","
   Print #fileNo, addTab(5); "CAST((CASE WHEN P_TGT."; g_anPdmFkSchemaName; " = P_SRC."; g_anPdmFkSchemaName; " AND P_TGT."; g_anPdmTableName; " = P_SRC."; g_anPdmTableName; " THEN 1 ELSE 0 END) AS "; g_dbtBoolean; ")"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); "V_FkCandidates E"
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNameLdmTable; " L_SRC"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "E.srcEntityType = L_SRC."; g_anAcmEntityType
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.srcOrParentEntityName = L_SRC."; g_anAcmEntityName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.srcOrParentEntitySection = L_SRC."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNamePdmTable; " P_SRC"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "L_SRC."; g_anLdmTableName; " = P_SRC."; g_anPdmLdmFkTableName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_SRC."; g_anLdmSchemaName; " = P_SRC."; g_anPdmLdmFkSchemaName
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNameLdmTable; " L_TGT"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "E.tgtEntityType = L_TGT."; g_anAcmEntityType
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.tgtOrParentEntityName = L_TGT."; g_anAcmEntityName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "E.tgtOrParentEntitySection = L_TGT."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "INNER JOIN"
   Print #fileNo, addTab(5); g_qualTabNamePdmTable; " P_TGT"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmTableName; " = P_TGT."; g_anPdmLdmFkTableName
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmSchemaName; " = P_TGT."; g_anPdmLdmFkSchemaName
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "L_SRC."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "L_TGT."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "COALESCE(P_SRC."; g_anPoolTypeId; ", "; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "COALESCE(P_TGT."; g_anPoolTypeId; ", "; CStr(g_workDataPoolId); ") = "; CStr(g_workDataPoolId)
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P_SRC."; g_anOrganizationId; " = v_orgId"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "P_TGT."; g_anOrganizationId; " = v_orgId"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "CAST(C.COLNAME AS VARCHAR(10)) AS c_fkColName,"
   Print #fileNo, addTab(4); "V.tgtTabSchema                 AS c_tgtTabSchema,"
   Print #fileNo, addTab(4); "V.tgtTabName                   AS c_tgtTabName,"
   Print #fileNo, addTab(4); "V.isSelfReference              AS c_hasSelfReference"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); "SYSCAT.COLUMNS C"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "V_FksByTab V"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "C.COLNAME = V.fkColName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.TABNAME = V.srcTabName"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.TABSCHEMA = V.srcTabSchema"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "c_isNl = "; gc_dbFalse
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "c_isGen = "; gc_dbFalse
   Print #fileNo, addTab(3); "ORDER BY"
   Print #fileNo, addTab(4); "C.COLNAME"
   Print #fileNo, addTab(2); "DO"
   Print #fileNo, addTab(3); "IF c_hasSelfReference = 1 THEN"

   Print #fileNo, addTab(4); "SET v_deleteFltrTxt = v_deleteFltrTxt || (CASE WHEN v_deleteFltrTxt = '' THEN '' ELSE ' AND ' END) ||' (T.' || c_fkColName || ' IS NOT NULL AND NOT EXISTS(SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tgtTabName) || ' T2 WHERE T.' || c_fkColName || ' = T2.oid))';"
   Print #fileNo, addTab(3); "ELSE"
   Print #fileNo, addTab(4); "SET v_stmntTxt = v_stmntTxt || ' AND (S.' || c_fkColName || ' IS NULL OR EXISTS(SELECT 1 FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tgtTabName) || ' T WHERE S.' || c_fkColName || ' = T.oid))';"
   Print #fileNo, addTab(3); "END IF;"

   Print #fileNo, addTab(2); "END FOR;"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_stmntTxt IS NOT NULL THEN"
   Print #fileNo, addTab(2); "IF (c_entityName <> 'TERM') THEN"
   'Print #fileNo, addTab(3); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_stmntTxt );"

   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
 
   'Print #fileNo, addTab(3); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount );"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
   Print #fileNo, addTab(2); "ELSE"
   'special term handling
   Print #fileNo, addTab(3); "SET v_stmntTxtTerm = v_stmntTxt;"
   Print #fileNo, addTab(3); "SET v_idx = locate_in_string(v_stmntTxt, 'FROM');"
   Print #fileNo, addTab(3); "SET v_len = length(v_stmntTxt);"
   Print #fileNo, addTab(3); "SET v_stmtSinceFrom = substr(v_stmntTxt, v_idx, v_len - v_idx +1);"
   Print #fileNo, addTab(3); "SET v_insert = 'INSERT INTO SESSION.Termoids(TERMOID, TERMAHOID) SELECT OID, AHOID ' || v_stmtSinceFrom;"
   'Print #fileNo, addTab(3); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_insert );"
   Print #fileNo, addTab(3); "EXECUTE IMMEDIATE v_insert;"
   Print #fileNo, addTab(5); "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;"
   'Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount2 );"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "if table has some 'self-reference' we need to do some specific cleanup", 3
   Print #fileNo, addTab(3); "IF c_hasSelfReference = 1 THEN"
   genProcSectionHeader fileNo, "ignore records if they correspond to the same aggregate as other ignored records", 4, True
   Print #fileNo, addTab(4); "IF (c_entityType <> '"; gc_acmEntityTypeKeyClass; "' OR c_entityName <> c_ahClassName OR c_isGen = 1 OR c_isNl = 1) THEN"
   Print #fileNo, addTab(5); "IF (c_entityName = 'TERM') THEN"
   'special term handling
   Print #fileNo, addTab(6); "SET v_stmntTxt = 'DELETE FROM Session.Termoids WHERE termAhOid IN (' ||"
   Print #fileNo, addTab(6); "'SELECT DISTINCT S."; g_anAhOid; " FROM ' || RTRIM(c_srcTabSchemaName) || '.' || RTRIM(c_tabName) || ' S LEFT OUTER JOIN ' ||"
   Print #fileNo, addTab(6); "'Session.Termoids T ON S."; g_anOid; " = T.termOid  WHERE T.termOid IS NULL)';"

   Print #fileNo, addTab(5); "ELSE"
   Print #fileNo, addTab(6); "SET v_stmntTxt = 'DELETE FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' WHERE "; g_anAhOid; " IN (' ||"
   Print #fileNo, addTab(6); "'SELECT DISTINCT S."; g_anAhOid; " FROM ' || RTRIM(c_srcTabSchemaName) || '.' || RTRIM(c_tabName) || ' S LEFT OUTER JOIN ' ||"
   Print #fileNo, addTab(6); "v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' T ON S."; g_anOid; " = T."; g_anOid; " WHERE T."; g_anOid; " IS NULL)';"
   Print #fileNo, addTab(5); "END IF;"

   'Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_stmntTxt );"
   Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(5); "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;"
   Print #fileNo, addTab(5); "SET v_rowCount = v_rowCount - v_rowCount2;"
   'Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount2 );"
   ' special term handling: now the insert
   Print #fileNo, addTab(5); "IF (c_entityName = 'TERM') THEN"
   Print #fileNo, addTab(6); "SET v_stmntTxt = v_stmntTxtTerm || ' AND OID IN (SELECT TERMOID FROM Session.Termoids)';"
   'Print #fileNo, addTab(6); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_stmntTxt );"
   Print #fileNo, addTab(6); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(6); "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;"
   Print #fileNo, addTab(6); "SET v_rowCount = v_rowCount + v_rowCount2;"
   'Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount2 );"
   Print #fileNo, addTab(5); "END IF;"
   Print #fileNo, addTab(4); "END IF;"
 
   genProcSectionHeader fileNo, "ignore records referring to other ignored records", 4
   Print #fileNo, addTab(4); "IF c_entityType = '"; gc_acmEntityTypeKeyClass; "' AND c_entityName = c_ahClassName AND c_isGen = 0 AND c_isNl = 0 AND v_deleteFltrTxt <> '' THEN"
   Print #fileNo, addTab(5); "SET v_stmntTxt = 'DELETE FROM ' || v_aliasSchemaName || '.' || RTRIM(c_tabName) || ' T WHERE ' || v_deleteFltrTxt;"

   Print #fileNo,
   'Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' ' || v_stmntTxt );"
   Print #fileNo, addTab(5); "EXECUTE IMMEDIATE v_stmntTxt;"
   Print #fileNo, addTab(5); "GET DIAGNOSTICS v_rowCount2 = ROW_COUNT;"
   'Print #fileNo, addTab(5); "CALL DBMS_OUTPUT.PUT_LINE( 'TF ' || current timestamp || ' Stmt executed: no. of rows: ' || v_rowCount2 );"
   Print #fileNo, addTab(5); "SET v_rowCount = v_rowCount - v_rowCount2;"

   Print #fileNo, addTab(4); "END IF;"
   Print #fileNo, addTab(3); "END IF;"
 
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(1); "END FOR;"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit

 End Sub
 
 
 Private Sub genFactoryTakeOverDdlByOrg3( _
   srcOrgIndex As Integer, _
   dstOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType = edtPdm And (srcOrgIndex < 1 Or dstOrgIndex < 1 Or srcPoolIndex < 1 Or dstPoolIndex < 1) Then
     ' Factory-Take-Over is only supported at 'pool-level'
     Exit Sub
   End If
 
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, dstOrgIndex, dstPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim transformation As AttributeListTransformation
 
   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(dstOrgIndex, ddlType)

   Dim qualTabNameViewDst As String
   qualTabNameViewDst = genQualTabNameByClassIndex(g_classIndexView, ddlType, dstOrgIndex, dstPoolIndex)
 
   Dim relIndexDisplaySlot As Integer
   relIndexDisplaySlot = getRelIndexByName(rxnDisplaySlot, rnDisplaySlot)
   Dim qualTabNameDisplaySlotSrc As String
   qualTabNameDisplaySlotSrc = genQualTabNameByRelIndex(relIndexDisplaySlot, ddlType, srcOrgIndex, srcPoolIndex)
   Dim qualTabNameDisplaySlotDst As String
   qualTabNameDisplaySlotDst = genQualTabNameByRelIndex(relIndexDisplaySlot, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNameGeneralSettings As String
   qualTabNameGeneralSettings = genQualTabNameByClassIndex(g_classIndexGeneralSettings, ddlType, dstOrgIndex, dstPoolIndex)
 
   Dim qualTabNamePricePreferences As String
   qualTabNamePricePreferences = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNameNsr1ValidForOrganizationDst As String
   qualTabNameNsr1ValidForOrganizationDst = genQualTabNameByRelIndex(g_relIndexNsr1ValidForOrganization, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNameEndSlotDst As String
   qualTabNameEndSlotDst = genQualTabNameByClassIndex(g_classIndexEndSlot, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNameLrtAffectedEntity As String
   qualTabNameLrtAffectedEntity = genQualTabNameByClassIndex(g_classIndexLrtAffectedEntity, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNameCategory As String
   qualTabNameCategory = genQualTabNameByClassIndex(g_classIndexCategory, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualTabNameCodeCategory As String
   qualTabNameCodeCategory = genQualTabNameByRelIndex(g_relIndexCodeCategory, ddlType, dstOrgIndex, dstPoolIndex)
   Dim qualTabNameCodeCategoryLrt As String
   qualTabNameCodeCategoryLrt = genQualTabNameByRelIndex(g_relIndexCodeCategory, ddlType, dstOrgIndex, dstPoolIndex, True)

   Dim qualProcNameAssignCodeCat As String
   qualProcNameAssignCodeCat = genQualProcName(g_sectionIndexAliasLrt, spnAssignCodeCat, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualProcedureNameFtoGetChangelog As String
   qualProcedureNameFtoGetChangelog = genQualProcName(g_sectionIndexAliasLrt, spnFtoGetChangeLog, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualProcedureNameFtoInitial As String
   qualProcedureNameFtoInitial = genQualProcName(g_sectionIndexAliasLrt, spnFtoInitial, ddlType, dstOrgIndex, dstPoolIndex)
 
   Dim qualProcedureNameSetEnp As String
   qualProcedureNameSetEnp = genQualProcName(g_sectionIndexFactoryTakeover, spnFtoSetEnp, ddlType, dstOrgIndex, dstPoolIndex)

   Dim qualProcedureNameGetEnpEbpMapping As String
   qualProcedureNameGetEnpEbpMapping = genQualProcName(g_sectionIndexFactoryTakeover, spnFtoGetEnpEbpMap, ddlType, dstOrgIndex, dstPoolIndex)
 
   Dim qualProcedureNameFtoLock As String
   qualProcedureNameFtoLock = genQualProcName(g_sectionIndexLrt, spnFtoLock, ddlType, dstOrgIndex, dstPoolIndex)
 
   Dim qualProcNameAssertRebateDefault As String
   qualProcNameAssertRebateDefault = genQualProcName(g_sectionIndexMeta, spnAssertRebateDefault, ddlType)

   ' ####################################################################################################################
   ' #    Lock Aggregate Heads for Factory Data Take-Over
   ' ####################################################################################################################

   printSectionHeader "SP for Lock Aggregate Heads for 'Factory Data Take-Over'", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcedureNameFtoLock
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "lrtOid_in", g_dbtOid, True, "OID of the LRT to use for locking"
   genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure corresponding to the LRT"
   genProcParm fileNo, "IN", "divisionOid_in", g_dbtOid, True, "OID of the Division corresponding to the LRT"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being locked"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "alreadyExist", "42710"
 
   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "stmnt", "STATEMENT"

   genProcSectionHeader fileNo, "declare continue handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   genDdlForTempChangeLogSummary fileNo, 1, True
   genDdlForTempImplicitChangeLogSummary fileNo, 1, True

   genSpLogProcEnter fileNo, qualProcedureNameFtoLock, ddlType, , "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out"

   Print #fileNo,
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "loop over affected aggregate heads"

   Print #fileNo, addTab(1); "FOR tabLoop AS"

   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_AffectedAggregateType"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "aggregateType"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT aggregateType FROM "; gc_tempTabNameChangeLogOrgSummary
   Print #fileNo, addTab(4); "UNION"
   Print #fileNo, addTab(3); "SELECT aggregateType FROM "; gc_tempTabNameChangeLogImplicitChanges
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); "V_AffectedAggregateType V"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "V.aggregateType = A."; g_anAcmEntityId
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmEntityId; " = A."; g_anAhCid
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "A."; g_anAcmCondenseData; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(dstOrgIndex, ddlType, True)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = "; genPoolId(dstPoolIndex, ddlType)
   Print #fileNo, addTab(2); "ORDER BY"
   Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " ASC"
   Print #fileNo, addTab(2); "WITH UR"
   Print #fileNo, addTab(2); "FOR READ ONLY"
 
   Print #fileNo, addTab(1); "DO"
   genProcSectionHeader fileNo, "process each aggregate head individually", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '."; UCase(spnFtoLock); "_' || c_tableName || '(?,?,?,?)' ;"

   Print #fileNo,
   Print #fileNo, addTab(2); "PREPARE stmnt FROM v_stmntTxt;"
   Print #fileNo,
   Print #fileNo, addTab(2); "EXECUTE"
   Print #fileNo, addTab(3); "stmnt"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_rowCount"
   Print #fileNo, addTab(2); "USING"
   Print #fileNo, addTab(3); "lrtOid_in,"
   Print #fileNo, addTab(3); "psOid_in,"
   Print #fileNo, addTab(3); "divisionOid_in"
   Print #fileNo, addTab(2); ";"
 
   genProcSectionHeader fileNo, "add to number of affected rows", 2
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   Print #fileNo, addTab(1); "END FOR;"
 
   genSpLogProcExit fileNo, qualProcedureNameFtoLock, ddlType, , "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   ' ####################################################################################################################
   ' #    Lock Aggregate Heads for Factory Data Take-Over
   ' ####################################################################################################################

   Dim busKeyAttrListNoFks As String
   Dim busKeyAttrArrayNoFks() As String
 
   Dim qualProcName As String
   Dim qualDstTabName As String
   Dim qualDstTabNameLrt As String

   Dim i As Integer
   For i = 1 To g_classes.numDescriptors
       If g_classes.descriptors(i).isAggHead And g_classes.descriptors(i).isUserTransactional And Not g_classes.descriptors(i).condenseData Then
         Dim fkAttrToDiv As String
         fkAttrToDiv = ""

         If g_classes.descriptors(i).navPathToDiv.relRefIndex > 0 Then
           If g_classes.descriptors(i).navPathToDiv.navDirection = etLeft Then
               fkAttrToDiv = g_relationships.descriptors(g_classes.descriptors(i).navPathToDiv.relRefIndex).leftFkColName(ddlType)
           Else
               fkAttrToDiv = g_relationships.descriptors(g_classes.descriptors(i).navPathToDiv.relRefIndex).rightFkColName(ddlType)
           End If
         End If

         busKeyAttrListNoFks = ""
 
         If g_classes.descriptors(i).hasBusinessKey Then
           busKeyAttrListNoFks = getPkAttrListByClassIndex(g_classes.descriptors(i).classIndex, ddlType, , , , True)

           genAttrList busKeyAttrArrayNoFks, busKeyAttrListNoFks
         End If

         qualProcName = genQualProcNameByEntityIndex(g_classes.descriptors(i).classIndex, eactClass, ddlType, dstOrgIndex, dstPoolIndex, , , , , spnFtoLock)
         qualDstTabName = genQualTabNameByClassIndex(i, ddlType, dstOrgIndex, dstPoolIndex)
         qualDstTabNameLrt = genQualTabNameByClassIndex(i, ddlType, dstOrgIndex, dstPoolIndex, , True)
 
         printSectionHeader "SP for Locking Records corresponding to Aggregate Head '" & g_classes.descriptors(i).sectionName & "." & g_classes.descriptors(i).className & "'", fileNo
         Print #fileNo,
         Print #fileNo, addTab(0); "CREATE PROCEDURE"
         Print #fileNo, addTab(1); qualProcName
         Print #fileNo, addTab(0); "("
         genProcParm fileNo, "IN", "lrtOid_in", g_dbtOid, True, "OID of the LRT to use for locking"
         genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure corresponding to the LRT"
         genProcParm fileNo, "IN", "divisionOid_in", g_dbtOid, True, "OID of the Division corresponding to the LRT"
         genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being locked"
         Print #fileNo, addTab(0); ")"
         Print #fileNo, addTab(0); "RESULT SETS 0"
         Print #fileNo, addTab(0); "LANGUAGE SQL"
         Print #fileNo, addTab(0); "BEGIN"

         genProcSectionHeader fileNo, "declare variables", , True
         genSigMsgVarDecl fileNo
         genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
         genVarDecl fileNo, "v_lrtEntityIdCount", "INTEGER", "0"
         genVarDecl fileNo, "v_oid", g_dbtOid, "NULL"
         genVarDecl fileNo, "v_oidStr", "VARCHAR(30)", "NULL"
         If Not g_classes.descriptors(i).hasOwnTable Then
           genVarDecl fileNo, "v_entityId", g_dbtEntityId, "'" & g_classes.descriptors(i).classIdStr & "'"
         End If
         genVarDecl fileNo, "v_entityLabel", "VARCHAR(90)", "'" & getPrimaryEntityLabelByIndex(eactClass, g_classes.descriptors(i).classIndex) & "'"
         If busKeyAttrListNoFks <> "" Then
           genVarDecl fileNo, "v_busKeyValues", "VARCHAR(200)", "NULL"
           Dim j As Integer
           For j = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
             genVarDecl fileNo, "v_" & busKeyAttrArrayNoFks(j), "VARCHAR(40)", "NULL"
           Next j
         End If
         genSpLogDecl fileNo
 
         genProcSectionHeader fileNo, "declare conditions"
         genCondDecl fileNo, "alreadyExist", "42710"

         genProcSectionHeader fileNo, "declare condition handler"
         Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR SQLWARNING"
         Print #fileNo, addTab(1); "BEGIN"
         Print #fileNo, addTab(2); "-- just ignore"
         Print #fileNo, addTab(1); "END;"
         Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
         Print #fileNo, addTab(1); "BEGIN"
         Print #fileNo, addTab(2); "-- just ignore"
         Print #fileNo, addTab(1); "END;"

         genDdlForTempChangeLogSummary fileNo, 1, True

         genSpLogProcEnter fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out"

         genProcSectionHeader fileNo, "lock records in 'public table' of MPC work data pool"
         If ftoLockSingleObjectProcessing Then

           Print #fileNo, addTab(1); "FOR oidLoop AS"
           Print #fileNo, addTab(2); "SELECT DISTINCT"
           Print #fileNo, addTab(3); "MCLS.ahObjectId AS c_oid"
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); gc_tempTabNameChangeLogOrgSummary; " MCLS"
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "MCLS.aggregateType = '"; g_classes.descriptors(i).classIdStr; "'"
           Print #fileNo, addTab(1); "DO"
 
           Print #fileNo, addTab(2); "UPDATE"
           Print #fileNo, addTab(3); qualDstTabName; " AHD"
           Print #fileNo, addTab(2); "SET"
           Print #fileNo, addTab(3); "AHD."; g_anInLrt; " = lrtOid_in"
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); "AHD."; g_anOid; " = c_oid"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "AHD."; g_anInLrt; " IS NULL"
           Print #fileNo, addTab(2); ";"
           Print #fileNo, addTab(1); "END FOR;"

         Else

           Print #fileNo, addTab(1); "UPDATE"
           Print #fileNo, addTab(2); qualDstTabName; " AHD"
           Print #fileNo, addTab(1); "SET"
           Print #fileNo, addTab(2); "AHD."; g_anInLrt; " = lrtOid_in"
           Print #fileNo, addTab(1); "WHERE"
           Print #fileNo, addTab(2); "AHD."; g_anInLrt; " IS NULL"
           If fkAttrToDiv <> "" Then
             Print #fileNo, addTab(3); "AND"
             Print #fileNo, addTab(2); "AHD."; UCase(fkAttrToDiv); " = divisionOid_in"
           ElseIf g_classes.descriptors(i).isPsTagged Then
             Print #fileNo, addTab(3); "AND"
             Print #fileNo, addTab(2); "AHD."; g_anPsOid; " = psOid_in"
           End If

           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "EXISTS ("
           Print #fileNo, addTab(3); "SELECT"
           Print #fileNo, addTab(4); "1"
           Print #fileNo, addTab(3); "FROM"
           Print #fileNo, addTab(4); gc_tempTabNameChangeLogOrgSummary; " MCLS"
           Print #fileNo, addTab(3); "WHERE"
           Print #fileNo, addTab(4); "MCLS.aggregateType = '"; g_classes.descriptors(i).classIdStr; "'"
           Print #fileNo, addTab(5); "AND"
           Print #fileNo, addTab(4); "MCLS.ahObjectId = AHD."; g_anOid
           Print #fileNo, addTab(2); ")"
           Print #fileNo, addTab(1); ";"
         End If
 
         genProcSectionHeader fileNo, "count the number of affected rows"
         Print #fileNo, addTab(1); "GET DIAGNOSTICS rowCount_out = ROW_COUNT;"

         genProcSectionHeader fileNo, "verify that now all records are locked by this LRT"
         Print #fileNo, addTab(1); "SELECT"
         If g_classes.descriptors(i).hasOwnTable Then
           Print #fileNo, addTab(2); "AHD."; g_anOid
         Else
           Print #fileNo, addTab(2); "AHD."; g_anOid; ","
           Print #fileNo, addTab(2); "MCLS.ahClassId"
         End If
         Print #fileNo, addTab(1); "INTO"
         If g_classes.descriptors(i).hasOwnTable Then
           Print #fileNo, addTab(2); "v_oid"
         Else
           Print #fileNo, addTab(2); "v_oid,"
           Print #fileNo, addTab(2); "v_entityId"
         End If
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualDstTabName; " AHD,"
         Print #fileNo, addTab(2); gc_tempTabNameChangeLogOrgSummary; " MCLS"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "AHD."; g_anOid; " = MCLS.ahObjectId"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "MCLS.aggregateType = '"; g_classes.descriptors(i).classIdStr; "'"
         Print #fileNo, addTab(3); "AND"
         Print #fileNo, addTab(2); "AHD."; g_anInLrt; " <> lrtOid_in"
         If fkAttrToDiv <> "" Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "AHD."; UCase(fkAttrToDiv); " = divisionOid_in"
         ElseIf g_classes.descriptors(i).isPsTagged Then
           Print #fileNo, addTab(3); "AND"
           Print #fileNo, addTab(2); "AHD."; g_anPsOid; " = psOid_in"
         End If
         Print #fileNo, addTab(1); "FETCH FIRST 1 ROW ONLY;"

         genProcSectionHeader fileNo, "if there is any row that is locked in some other transaction we need to quit"
         Print #fileNo, addTab(1); "IF (v_oid IS NOT NULL) THEN"
 
         genProcSectionHeader fileNo, "determine entityLabel", 2, True
         Print #fileNo, addTab(2); "SELECT"
         Print #fileNo, addTab(3); g_anAcmEntityLabel
         Print #fileNo, addTab(2); "INTO"
         Print #fileNo, addTab(3); "v_entityLabel"
         Print #fileNo, addTab(2); "FROM"
         Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " E"
         Print #fileNo, addTab(2); "INNER JOIN"
         Print #fileNo, addTab(3); g_qualTabNameAcmEntityNl; " ENL"
         Print #fileNo, addTab(2); "ON"
         Print #fileNo, addTab(3); "E."; g_anAcmEntitySection; " = ENL."; g_anAcmEntitySection
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "E."; g_anAcmEntityName; " = ENL."; g_anAcmEntityName
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "E."; g_anAcmEntityType; " = ENL."; g_anAcmEntityType
         Print #fileNo, addTab(2); "WHERE"
         Print #fileNo, addTab(3); "E."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "E."; g_anAcmEntityId; " = "; IIf(g_classes.descriptors(i).hasOwnTable, "'" & g_classes.descriptors(i).classIdStr & "'", "v_entityId")
         Print #fileNo, addTab(2); "ORDER BY"
         Print #fileNo, addTab(3); "(CASE ENL."; g_anLanguageId; " WHEN "; CStr(gc_langIdEnglish); " THEN 0 ELSE ENL."; g_anLanguageId; " END) ASC"
         Print #fileNo, addTab(2); "FETCH FIRST 1 ROW ONLY"
         Print #fileNo, addTab(2); "WITH UR;"

         Print #fileNo,
         Print #fileNo, addTab(2); "SET v_entityLabel = RTRIM(LEFT(COALESCE(v_entityLabel, "; _
                                    "'"; getPrimaryEntityLabelByIndex(eactClass, g_classes.descriptors(i).classIndex); "'), "; _
                                   CStr(33 - IIf(busKeyAttrListNoFks = "", 3, Len(busKeyAttrListNoFks))); "));"

         If busKeyAttrListNoFks <> "" Then
           genProcSectionHeader fileNo, "determine non-FK values violating business key", 2
           Print #fileNo, addTab(2); "SELECT"
           For j = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
             Print #fileNo, addTab(3); "CAST(RTRIM(CAST("; UCase(busKeyAttrArrayNoFks(j)); " AS CHAR(40))) AS VARCHAR(40))"; IIf(j < UBound(busKeyAttrArrayNoFks), ",", "")
           Next j
           Print #fileNo, addTab(2); "INTO"
           For j = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
             Print #fileNo, addTab(3); "v_"; busKeyAttrArrayNoFks(j); IIf(j < UBound(busKeyAttrArrayNoFks), ",", "")
           Next j
           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); qualDstTabName
           Print #fileNo, addTab(2); "WHERE"
           Print #fileNo, addTab(3); g_anOid; " = v_oid"
           Print #fileNo, addTab(2); ";"

           genProcSectionHeader fileNo, "concatenate business key values for error message", 2
           Print #fileNo, addTab(2); "SET v_busKeyValues ="
           For j = LBound(busKeyAttrArrayNoFks) To UBound(busKeyAttrArrayNoFks)
             Print #fileNo, addTab(4); "COALESCE(v_"; busKeyAttrArrayNoFks(j); ", '"; busKeyAttrArrayNoFks(j); "=?')"; IIf(j < UBound(busKeyAttrArrayNoFks), " || ',' ||", "")
           Next j
           Print #fileNo, addTab(2); ";"
 
           genProcSectionHeader fileNo, "signal eror message", 2
           genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out"

           genSignalDdlWithParms "ftoLockDetail", fileNo, 2, busKeyAttrListNoFks, , , , , , , , , "v_entityLabel", "v_busKeyValues"
         Else
           genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out"
           genSignalDdlWithParms "ftoLockDetail", fileNo, 2, g_anOid, , , , , , , , , "v_entityLabel", "RTRIM(CHAR(v_oid))"
         End If

         Print #fileNo, addTab(1); "END IF;"
 
         genProcSectionHeader fileNo, "copy the 'public records' into 'private table'"
         Print #fileNo, addTab(1); "INSERT INTO"
         Print #fileNo, addTab(2); qualDstTabNameLrt
         Print #fileNo, addTab(1); "("

         genAttrListForEntity g_classes.descriptors(i).classIndex, eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, True, , edomListLrt

         Print #fileNo, addTab(1); ")"
         Print #fileNo, addTab(1); "SELECT"

         initAttributeTransformation transformation, 2
         setAttributeMapping transformation, 1, conLrtState, "" & lrtStatusLocked
         setAttributeMapping transformation, 2, conInLrt, "lrtOid_in"

         genTransformedAttrListForEntity g_classes.descriptors(i).classIndex, eactClass, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, , True, , edomListLrt

         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); qualDstTabName
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); g_anOid; " IN"
         Print #fileNo, addTab(3); "("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "MCLS.ahObjectId"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); gc_tempTabNameChangeLogOrgSummary; " MCLS"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "MCLS.aggregateType = '"; g_classes.descriptors(i).classIdStr; "'"
         Print #fileNo, addTab(2); ")"
         Print #fileNo, addTab(1); "WITH UR;"

         genDdlForUpdateAffectedEntities fileNo, "ACM-class", eactClass, gc_acmEntityTypeKeyClass, False, False, qualTabNameLrtAffectedEntity, _
           g_classes.descriptors(i).classIdStr, g_classes.descriptors(i).classIdStr, "lrtOid_in", 1, CStr(lrtStatusLocked), False
 
         genSpLogProcExit fileNo, qualProcName, ddlType, , "lrtOid_in", "psOid_in", "divisionOid_in", "rowCount_out"

         Print #fileNo, addTab(0); "END"
         Print #fileNo, addTab(0); gc_sqlCmdDelim
       End If
   Next i

   ' ####################################################################################################################
   ' #    Factory Data Take-Over
   ' ####################################################################################################################

   Dim qualViewName As String
   qualViewName = genQualViewName(g_sectionIndexDbMeta, vnSetProdAffectedPdmTab, vsnSetProdAffectedPdmTab, ddlType)
 
   Dim simulate As Boolean
   Dim procNameSuffix As String
   For j = 1 To IIf(supportSimulationSps, 2, 1)
     simulate = (j = 2)
     procNameSuffix = IIf(simulate, "sim", "")

     qualProcName = _
       genQualProcName( _
         g_sectionIndexAliasLrt, spnFactoryTakeOver, ddlType, dstOrgIndex, dstPoolIndex, , procNameSuffix _
       )

     printSectionHeader "SP for 'Factory Data Take-Over'", fileNo
     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcName
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "OUT", "endTimestamp_out", "TIMESTAMP", True, "marks the 'end timestamp' for data being taken over"
     If simulate Then
       genProcParm fileNo, "OUT", "refId_out", "INTEGER", True, "ID used to identify persisted records related to this procedure call"
     End If
     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows being taken over"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"

     genProcSectionHeader fileNo, "declare conditions", , True
     genCondDecl fileNo, "notFound", "02000"

     genProcSectionHeader fileNo, "declare variables"
     genSigMsgVarDecl fileNo
     genVarDecl fileNo, "v_stmntTxt", "VARCHAR(200)", "NULL"
     genVarDecl fileNo, "v_lrtOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_psOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_divisionOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_orgOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_lrtCount", "INTEGER", "NULL"
     genVarDecl fileNo, "v_cdUserId", g_dbtUserId, "NULL"
     genVarDecl fileNo, "v_filterBySr0Context", g_dbtBoolean, gc_dbFalse
     genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
     genVarDecl fileNo, "v_rebateValueType", "INTEGER", "NULL"
     genVarDecl fileNo, "v_nsr1ViewOid", g_dbtOid, "NULL"
     genVarDecl fileNo, "v_MaxSequenceNumber", "SMALLINT", "NULL"
     genVarDecl fileNo, "v_opType", g_dbtEnumId, "NULL"
     If Not simulate Then
         genVarDecl fileNo, "v_initialFTO", "TIMESTAMP", "NULL"
     End If
 
     genSpLogDecl fileNo

     genProcSectionHeader fileNo, "declare statement"
     genVarDecl fileNo, "v_stmnt", "STATEMENT"

     genProcSectionHeader fileNo, "declare condition handler"
     Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
     Print #fileNo, addTab(1); "BEGIN"
     Print #fileNo, addTab(2); "-- just ignore"
     Print #fileNo, addTab(1); "END;"

     genDdlForTempTablesChangeLog fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1, , , , True
     genDdlForTempChangeLogSummary fileNo, 1, True, True

     If simulate Then
       genSpLogProcEnter fileNo, qualProcName, ddlType, , "endTimestamp_out", "refId_out", "rowCount_out"
     Else
       genSpLogProcEnter fileNo, qualProcName, ddlType, , "endTimestamp_out", "rowCount_out"
     End If

     genDb2RegVarCheckDdl fileNo, ddlType, dstOrgIndex, dstPoolIndex, tvNull, 1

     genProcSectionHeader fileNo, "initialize output parameter"
     Print #fileNo, addTab(1); "SET rowCount_out = 0;"
     If simulate Then
       Print #fileNo, addTab(1); "SET refId_out = 0;"
     End If

     ' ########################################################################
     genProcSectionHeader fileNo, "determine OID of 'my Organization'"
     Print #fileNo, addTab(1); "SET v_orgOid ="
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "ORGOID"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); g_qualTabNamePdmOrganization
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "ID = "; genOrgId(dstOrgIndex, ddlType, True)
     Print #fileNo, addTab(1); ");"
     Print #fileNo,
     Print #fileNo, addTab(1); "IF (v_orgOid IS NULL) THEN"

     If simulate Then
       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "endTimestamp_out", "refId_out", "rowCount_out"
     Else
       genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "endTimestamp_out", "rowCount_out"
     End If
     genSignalDdl "noOrg", fileNo, 2

     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "determine ProductStructure"
     Print #fileNo, addTab(1); "SET v_psOid = "; g_activePsOidDdl; ";"

     genProcSectionHeader fileNo, "verify lrtOid"
     Print #fileNo, addTab(1); "SET v_lrtOid = "; g_activeLrtOidDdl; ";"

     Print #fileNo, addTab(1); "SET v_lrtCount ="
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "COUNT(*)"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameLrt; " L"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "L."; g_anOid; " = v_lrtOid"
     Print #fileNo, addTab(1); ");"

     genProcSectionHeader fileNo, "if this transaction does not exist, we need to quit"
     Print #fileNo, addTab(1); "IF (v_lrtCount = 0) THEN"
     If simulate Then
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out"
     Else
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out"
     End If
     genSignalDdlWithParms "lrtNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(v_lrtOid))"
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "verify that no other factory takeover is running for current ProductStructure"
     Print #fileNo, addTab(1); "IF"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameLrt; " L"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "L."; g_anOid; " <> v_lrtOid"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anEndTime; " IS NULL"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anIsCentralDataTransfer; " = "; gc_dbTrue
 ' FIXME: USE UNCOMMITTED READ here
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); "THEN"
     If simulate Then
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out"
     Else
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out"
     End If
     genSignalDdl "ftoAlreadyOnPs", fileNo, 2
     Print #fileNo, addTab(1); "END IF;"
 
     genProcSectionHeader fileNo, "determine division OID", 1
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "PDIDIV_OID"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_divisionOid"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); g_qualTabNameProductStructure
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anOid; " = v_psOid"
     Print #fileNo, addTab(1); "WITH UR;"

     genProcSectionHeader fileNo, "verify that no other factory takeover is running for current Division"
     Print #fileNo, addTab(1); "IF"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameLrt; " L,"
     Print #fileNo, addTab(4); g_qualTabNameProductStructure; " P"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "P.PDIDIV_OID = v_divisionOid"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "P."; g_anOid; " = L."; g_anPsOid
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anOid; " <> v_lrtOid"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anEndTime; " IS NULL"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anIsCentralDataTransfer; " = "; gc_dbTrue
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); "THEN"
     If simulate Then
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out"
     Else
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out"
     End If
     genSignalDdl "ftoAlreadyInDiv", fileNo, 2
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "verify that active transaction is empty"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "COUNT(*)"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_lrtCount"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameLrtAffectedEntity; " E"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "E."; g_anLrtOid; " = v_lrtOid"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "E.OPID IN ("; CStr(lrtStatusCreated); ","; CStr(lrtStatusUpdated); ","; CStr(lrtStatusDeleted); ")"
     Print #fileNo, addTab(1); ";"
     Print #fileNo, addTab(1); "IF v_lrtCount > 0 THEN"
     If simulate Then
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out"
     Else
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out"
     End If
     genSignalDdl "ftoLrtNotEmpty", fileNo, 2
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "verify that there are no uncommitted changes related to active transaction"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "COUNT(*)"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_lrtCount"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameLrtAffectedEntity; " E"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "E."; g_anLrtOid; " = v_lrtOid"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "E.OPID IN ("; CStr(lrtStatusCreated); ","; CStr(lrtStatusUpdated); ","; CStr(lrtStatusDeleted); ")"
     Print #fileNo, addTab(1); "WITH UR;"
     Print #fileNo, addTab(1); "IF v_lrtCount > 0 THEN"
     If simulate Then
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "refId_out", "rowCount_out"
     Else
       genSpLogProcEscape fileNo, qualProcName, ddlType, 2, "endTimestamp_out", "rowCount_out"
     End If
     genSignalDdl "ftoLrtInUse", fileNo, 2
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "determine Id of User executing this Take-Over"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "U."; g_anUserId
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_cdUserId"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); g_qualTabNameUser; " U,"
     Print #fileNo, addTab(2); qualTabNameLrt; " L"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "U."; g_anOid; " = L.UTROWN_OID"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "L."; g_anOid; " = v_lrtOid"
     Print #fileNo, addTab(1); "WITH UR;"

     ' ########################################################################
     genProcSectionHeader fileNo, "initialize output parameter"
     Print #fileNo, addTab(1); "SET endTimestamp_out  = CURRENT TIMESTAMP;"

     ' ########################################################################

     genProcSectionHeader fileNo, "Step 1: Verify GeneralSettings and PricePreferences"
     genProcSectionHeader fileNo, "make sure that this ProductStructure has a default rebate (for type)", , True
     Print #fileNo, addTab(1); "CALL "; qualProcNameAssertRebateDefault; "(v_psOid, 1);"

     genProcSectionHeader fileNo, "GeneralSettings"
     Print #fileNo, addTab(1); "IF NOT EXISTS (SELECT "; g_anOid; " FROM "; qualTabNameGeneralSettings; " WHERE "; g_anPsOid; " = v_psOid) THEN"

     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); qualTabNameGeneralSettings
     Print #fileNo, addTab(2); "("

     genAttrListForEntity g_classIndexGeneralSettings, eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, False, False, edomListNonLrt

     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "VALUES"
     Print #fileNo, addTab(2); "("

     initAttributeTransformation transformation, 5
     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conPsOid, "v_psOid"
     setAttributeMapping transformation, 3, conCreateUser, "v_cdUserId"
     setAttributeMapping transformation, 4, conUpdateUser, "v_cdUserId"
     setAttributeMapping transformation, 5, conLastCentralDataTransferBegin, "endTimestamp_out"

     genTransformedAttrListForEntity g_classIndexGeneralSettings, eactClass, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, , , , edomValueNonLrt Or edomDefaultValue

     Print #fileNo, addTab(2); ");"
     Print #fileNo, addTab(1); "END IF;"

     genProcSectionHeader fileNo, "PricePreferences"
     Print #fileNo, addTab(1); "IF NOT EXISTS (SELECT "; g_anOid; " FROM "; qualTabNamePricePreferences; " WHERE "; g_anPsOid; " = v_psOid) THEN"

     Print #fileNo, addTab(2); "INSERT INTO"
     Print #fileNo, addTab(3); qualTabNamePricePreferences
     Print #fileNo, addTab(2); "("

     genAttrListForEntity g_classIndexPricePreferences, eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, False, False, edomListNonLrt

     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "VALUES"
     Print #fileNo, addTab(2); "("

     initAttributeTransformation transformation, 11
     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conVehicleTotalPriceCalculationId, "1"
     setAttributeMapping transformation, 3, conRebateValueCode, "25"
     setAttributeMapping transformation, 4, conRebateValueType, "COALESCE((SELECT VALUETYPE FROM " & g_qualTabNameRebateDefault & " WHERE " & g_anPsOid & " = v_psOid), 25)"
     setAttributeMapping transformation, 5, conCurrency, "'EUR'"
     setAttributeMapping transformation, 6, conCurrencyFactor, "1"
     setAttributeMapping transformation, 7, conPsOid, "v_psOid"
     setAttributeMapping transformation, 8, conCreateUser, "v_cdUserId"
     setAttributeMapping transformation, 9, conUpdateUser, "v_cdUserId"
     setAttributeMapping transformation, 10, conPrimaryPriceTypeForTestId, CStr(gc_dfltPrimaryPriceTypeOrg)
     setAttributeMapping transformation, 11, conPriceSelectionForOverlapId, CStr(gc_dfltPriceSelectionForOverlapOrg)

     genTransformedAttrListForEntity g_classIndexPricePreferences, eactClass, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, , , , edomValueNonLrt Or edomDefaultValue

     Print #fileNo, addTab(2); ");"

     Print #fileNo, addTab(1); "END IF;"
 
     genProcSectionHeader fileNo, "determine RebateValueType"
     Print #fileNo, addTab(1); "SET v_rebateValueType ="
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); g_anRebateValueType
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNamePricePreferences
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(2); "FETCH FIRST 1 ROWS ONLY"
     Print #fileNo, addTab(1); ");"
     Print #fileNo, addTab(1); "SET v_rebateValueType = COALESCE(v_rebateValueType, 25);"

     ' ########################################################################
     If Not simulate Then
       genProcSectionHeader fileNo, "check if initial factory takeover", 1
       Print #fileNo, addTab(1); "SET v_initialFTO ="
       Print #fileNo, addTab(1); "("
       Print #fileNo, addTab(1); "SELECT"
       Print #fileNo, addTab(2); "MAX(LASTCENTRALDATATRANSFERCOMMIT) "
       Print #fileNo, addTab(1); "FROM"
       Print #fileNo, addTab(2); qualTabNameGeneralSettings
       Print #fileNo, addTab(1); "WHERE"
       Print #fileNo, addTab(2); conPsOid; " = v_psOid"
       Print #fileNo, addTab(1); "WITH UR"
       Print #fileNo, addTab(1); ");"

       genProcSectionHeader fileNo, "Special handling for initial factory takeovers"
       Print #fileNo, addTab(1); "IF v_initialFTO IS NULL THEN"

       Print #fileNo, addTab(2); "SET v_stmntTxt = 'CALL "; qualProcedureNameFtoInitial; "(?,?,?,?,?,?,?)';"

       Print #fileNo,
       Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
       Print #fileNo,
       Print #fileNo, addTab(2); "EXECUTE"
       Print #fileNo, addTab(3); "v_stmnt"
       Print #fileNo, addTab(2); "INTO"
       Print #fileNo, addTab(3); "endTimestamp_out,"
       Print #fileNo, addTab(3); "v_rowCount"
       Print #fileNo, addTab(2); "USING"
       Print #fileNo, addTab(3); "v_orgOid,"
       Print #fileNo, addTab(3); "v_psOid,"
       Print #fileNo, addTab(3); "v_divisionOid,"
       Print #fileNo, addTab(3); "v_lrtOid,"
       Print #fileNo, addTab(3); "v_cdUserId"
       Print #fileNo, addTab(2); ";"

       Print #fileNo, addTab(2); "SET rowCount_out  = rowCount_out + v_rowCount;"

     '   If Not simulate Then
     'Print #fileNo, addTab(1); "CALL DBMS_OUTPUT.PUT_LINE( 'TF' || current timestamp || ' ' || v_stmntTxt || ' called with result: ' ||  v_rowCount);"
     'End If
       Print #fileNo, addTab(1); "ELSE"
     End If

     ' ########################################################################
     genProcSectionHeader fileNo, "Retrieve MPC-related ChangeLog Entries"
     Print #fileNo, addTab(1); "CALL "; qualProcedureNameFtoGetChangelog; "(-1, -1, v_filterBySr0Context, endTimestamp_out, rowCount_out);"

     ' ########################################################################
     genProcSectionHeader fileNo, "Lock Aggregate Heads"
     Print #fileNo, addTab(1); "CALL "; qualProcedureNameFtoLock; "(v_lrtOid, v_psOid, v_divisionOid, v_rowCount);"

     ' ########################################################################
     genProcSectionHeader fileNo, "Calculate ENP-OID Mapping"
     Print #fileNo, addTab(1); "CALL "; qualProcedureNameGetEnpEbpMapping; "(v_psOid, v_rowCount);"

     ' ########################################################################
     genProcSectionHeader fileNo, "verify again that there are no uncommitted changes related to active transaction"
     Print #fileNo, addTab(1); "SELECT"
     Print #fileNo, addTab(2); "COUNT(*)"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_lrtCount"
     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameLrtAffectedEntity; " E"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "E."; g_anLrtOid; " = v_lrtOid"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "E.OPID IN ("; CStr(lrtStatusCreated); ","; CStr(lrtStatusUpdated); ","; CStr(lrtStatusDeleted); ")"
     Print #fileNo, addTab(1); "WITH UR;"
     Print #fileNo, addTab(1); "IF v_lrtCount > 0 THEN"
     genSpLogProcEscape fileNo, qualProcName, ddlType, -2, "endTimestamp_out", "rowCount_out"
     genSignalDdl "ftoLrtInUse", fileNo, 2
     Print #fileNo, addTab(1); "END IF;"

     ' ########################################################################
     genProcSectionHeader fileNo, "Data Take-Over - process each affected table"
     Print #fileNo, addTab(1); "FOR tabLoop AS"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_tableName,"
     Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_schemaName"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " A,"
     Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L,"
     Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "L."; g_anAcmEntitySection; " = A."; g_anAcmEntitySection
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anAcmEntityName; " = A."; g_anAcmEntityName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anAcmEntityType; " = A."; g_anAcmEntityType
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmSchemaName; " = P."; g_anPdmLdmFkSchemaName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmTableName; " = P."; g_anPdmLdmFkTableName
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmIsNt2m; " = "; gc_dbFalse
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmIsCto; " = "; gc_dbFalse
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmIsCtp; " = "; gc_dbFalse
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "A."; g_anAcmEntityType; " IN ('"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"

     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(dstOrgIndex, ddlType, True)
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = "; genPoolId(g_workDataPoolIndex, ddlType)
     Print #fileNo, addTab(2); "ORDER BY"
     Print #fileNo, addTab(3); "L."; g_anLdmFkSequenceNo; " DESC"
     Print #fileNo, addTab(2); "WITH UR"
     Print #fileNo, addTab(2); "FOR READ ONLY"
     Print #fileNo, addTab(1); "DO"

     Print #fileNo, addTab(2); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '."; UCase(spnFactoryTakeOver); "_' || c_tableName || '(?,?,?,?)' ;"

     Print #fileNo,
     Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE"
     Print #fileNo, addTab(3); "v_stmnt"
     Print #fileNo, addTab(2); "INTO"
     Print #fileNo, addTab(3); "v_rowCount"
     Print #fileNo, addTab(2); "USING"
     Print #fileNo, addTab(3); "v_divisionOid,"
     Print #fileNo, addTab(3); "v_psOid,"
     Print #fileNo, addTab(3); "v_orgOid"
     Print #fileNo, addTab(2); ";"

     Print #fileNo, addTab(1); "END FOR;"

     genProcSectionHeader fileNo, "delete records implicitly deleted for target organization"
     genProcSectionHeader fileNo, "and insert records implicitly created for target organization", , True
     Print #fileNo, addTab(1); "SET v_opType = 3;"
     Print #fileNo, addTab(1); "WHILE v_opType IS NOT NULL DO"
     Print #fileNo, addTab(2); "FOR tabLoop AS"
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "P."; g_anPdmTableName; " AS c_tableName,"
     Print #fileNo, addTab(4); "P."; g_anPdmFkSchemaName; " AS c_schemaName"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " A,"
     Print #fileNo, addTab(4); g_qualTabNameLdmTable; " L,"
     Print #fileNo, addTab(4); g_qualTabNamePdmTable; " P"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "L."; g_anAcmEntitySection; " = A."; g_anAcmEntitySection
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anAcmEntityName; " = A."; g_anAcmEntityName
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anAcmEntityType; " = A."; g_anAcmEntityType
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anLdmSchemaName; " = P."; g_anPdmLdmFkSchemaName
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anLdmTableName; " = P."; g_anPdmLdmFkTableName
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "A."; g_anAcmIsNt2m; " = "; gc_dbFalse
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "A."; g_anAcmIsCto; " = "; gc_dbFalse
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "A."; g_anAcmIsCtp; " = "; gc_dbFalse
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "A."; g_anAcmUseFtoPostProcess; " = "; gc_dbTrue
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "A."; g_anAcmEntityType; " IN ('"; gc_acmEntityTypeKeyClass; "', '"; gc_acmEntityTypeKeyRel; "')"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "(v_opType = 3 OR A."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyRel; "')"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anLdmIsLrt; " = "; gc_dbFalse
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anLdmIsNl; " = "; gc_dbFalse
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "L."; g_anLdmIsGen; " = "; gc_dbFalse
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "P."; g_anOrganizationId; " = "; genOrgId(dstOrgIndex, ddlType, True)
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "P."; g_anPoolTypeId; " = "; genPoolId(g_workDataPoolIndex, ddlType)
     Print #fileNo, addTab(3); "ORDER BY"
     Print #fileNo, addTab(4); "L."; g_anLdmFkSequenceNo; " ASC"
     Print #fileNo, addTab(3); "WITH UR"
     Print #fileNo, addTab(3); "FOR READ ONLY"
     Print #fileNo, addTab(2); "DO"

     Print #fileNo, addTab(3); "SET v_stmntTxt  = 'CALL ' || c_schemaName || '."; UCase(spnFtoPostProc); "_' || c_tableName || '(?,?,?,?,?)' ;"

     Print #fileNo,
     Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(3); "EXECUTE"
     Print #fileNo, addTab(4); "v_stmnt"
     Print #fileNo, addTab(3); "INTO"
     Print #fileNo, addTab(4); "v_rowCount"
     Print #fileNo, addTab(3); "USING"
     Print #fileNo, addTab(4); "v_lrtOid,"
     Print #fileNo, addTab(4); "v_psOid,"
     Print #fileNo, addTab(4); "v_divisionOid,"
     Print #fileNo, addTab(4); "v_opType"
     Print #fileNo, addTab(3); ";"

     Print #fileNo, addTab(2); "END FOR;"
     Print #fileNo,
     Print #fileNo, addTab(2); "SET v_opType = (CASE v_opType WHEN 3 THEN 1 ELSE NULL END);"
     Print #fileNo, addTab(1); "END WHILE;"

     ' ########################################################################
     genProcSectionHeader fileNo, "Calculate ENPs"

     Print #fileNo, addTab(1); "CALL "; qualProcedureNameSetEnp; "(v_rebateValueType, v_rowCount);"

     ' ########################################################################

     genProcSectionHeader fileNo, "AssignCodeCat"

     Print #fileNo, addTab(1); "FOR tabLoop AS"
     Print #fileNo, addTab(2); "SELECT DISTINCT"
     Print #fileNo, addTab(3); "CCL.GCO_OID AS v_code,"
     Print #fileNo, addTab(3); "CCL.CAT_OID AS v_category"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameCodeCategoryLrt; " CCL"
     Print #fileNo, addTab(2); "JOIN"
     Print #fileNo, addTab(3); qualTabNameCodeCategory; " CC"
     Print #fileNo, addTab(4); "ON"
     Print #fileNo, addTab(3); "CCL.OID = CC.OID"
     Print #fileNo, addTab(2); "JOIN"
     Print #fileNo, addTab(3); qualTabNameCategory, " C"
     Print #fileNo, addTab(4); "ON"
     Print #fileNo, addTab(3); "C.ISDEFAULT <> 1"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "C.PS_OID = CC.PS_OID"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "CC.CAT_OID = C.OID"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "CCL.INLRT = v_lrtOid"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "CCL.PS_OID = v_psOid"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "CCL.LRTSTATE = 2"
     Print #fileNo, addTab(1); "DO"
     Print #fileNo, addTab(2); "SET v_stmntTxt  = 'CALL "; qualProcNameAssignCodeCat; " (?,?,?)';"
     Print #fileNo,
     Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(2); "EXECUTE"
     Print #fileNo, addTab(3); "v_stmnt"
     Print #fileNo, addTab(2); "INTO"
     Print #fileNo, addTab(3); "v_rowCount"
     Print #fileNo, addTab(2); "USING"
     Print #fileNo, addTab(3); "v_code,"
     Print #fileNo, addTab(3); "v_category"
     Print #fileNo, addTab(2); ";"
     Print #fileNo, addTab(1); "END FOR;"

     If Not simulate Then
         Print #fileNo, addTab(1); "END IF; -- v_initialFTO"
     End If

     genProcSectionHeader fileNo, "Verify existence of 'StandardViews' for SR0, SR1 and NSR1"
     Dim viewName As String
     For i = 1 To 3
       viewName = IIf(i = 1, "SR0", IIf(i = 2, "SR1", "NSR1"))
       Print #fileNo, addTab(1); "IF NOT EXISTS (SELECT "; g_anOid; " FROM "; _
                                  qualTabNameViewDst; " WHERE "; "RTRIM(UPPER("; g_anName; ")) = '"; viewName; "' AND "; g_anPsOid; " = v_psOid) THEN"

       Print #fileNo, addTab(2); "INSERT INTO"
       Print #fileNo, addTab(3); qualTabNameViewDst
       Print #fileNo, addTab(2); "("

       genAttrListForEntity g_classIndexView, eactClass, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, False, False, edomListNonLrt

       Print #fileNo, addTab(2); ")"
       If i < 3 Then
         Print #fileNo, addTab(2); "SELECT"

         genAttrListForEntity g_classIndexView, eactClass, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, False, False, edomListNonLrt

         Print #fileNo, addTab(2); "FROM"
         Print #fileNo, addTab(3); genQualTabNameByClassIndex(g_classIndexView, ddlType, srcOrgIndex, srcPoolIndex)
         Print #fileNo, addTab(2); "WHERE"
         Print #fileNo, addTab(3); "UPPER("; g_anName; ") = '"; viewName; "'"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
         Print #fileNo, addTab(2); ";"
       Else
         Print #fileNo, addTab(2); "VALUES"
         Print #fileNo, addTab(2); "("

         initAttributeTransformation transformation, 4
         setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
         setAttributeMapping transformation, 2, conName, "'" & viewName & "'"
         setAttributeMapping transformation, 3, conIsStandard, gc_dbTrue
         setAttributeMapping transformation, 4, conPsOid, "v_psOid"

         genTransformedAttrListForEntity g_classIndexView, eactClass, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 3, , , , edomValueNonLrt Or edomDefaultValue

         Print #fileNo, addTab(2); ");"
       End If

       Print #fileNo, addTab(1); "END IF;"

       If i < 3 Then
         Print #fileNo,
       End If
     Next i

     ' ########################################################################
     genProcSectionHeader fileNo, "Maintain DisplaySlots for Standard Views"
     genProcSectionHeader fileNo, "Delete existing Standard Slots", , True
 
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); qualTabNameDisplaySlotDst; " D"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameViewDst; " V"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "V."; g_anOid; " = D.VIW_OID"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "UPPER(V."; g_anName; ") IN ('SR0', 'SR1', 'NSR1')"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "V."; g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); ";"
 
     genProcSectionHeader fileNo, "Copy DisplaySlots for SR0- and SR1-View from factory"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameDisplaySlotDst
     Print #fileNo, addTab(1); "("

     genAttrListForEntity relIndexDisplaySlot, eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, False, False, edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

     genAttrListForEntity relIndexDisplaySlot, eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, False, False, edomListNonLrt

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameDisplaySlotSrc; " D"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "1"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTabNameViewDst; " V"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "V."; g_anOid; " = D.VIW_OID"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "UPPER(V."; g_anName; ") IN ('SR0', 'SR1')"
     Print #fileNo, addTab(5); "AND"
     Print #fileNo, addTab(4); "V."; g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "Create DisplaySlots for NSR1-View"
     genProcSectionHeader fileNo, "Determine OID of NSR1-View", , True
 
     Print #fileNo, addTab(1); "SET v_nsr1ViewOid = ("
 
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); g_anOid
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameViewDst
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "UPPER("; g_anName; ") = 'NSR1'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(1); ");"
 
     genProcSectionHeader fileNo, "Copy DisplaySlots of SR1-View for NSR1-View (create new OIDs, point FK to NSR1-View)"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameDisplaySlotDst
     Print #fileNo, addTab(1); "("

     genAttrListForEntity relIndexDisplaySlot, eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, False, False, edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

     initAttributeTransformation transformation, 7, , , , "D."
     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conPsOid, "v_psOid"
     setAttributeMapping transformation, 3, conCreateTimestamp, "CURRENT TIMESTAMP"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "CURRENT TIMESTAMP"
     setAttributeMapping transformation, 5, conCreateUserName, "v_cdUserId"
     setAttributeMapping transformation, 6, conUpdateUserName, "v_cdUserId"
     setAttributeMapping transformation, 7, "VIW_OID", "v_nsr1ViewOid"
 
     genTransformedAttrListForEntity relIndexDisplaySlot, eactRelationship, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, , , , edomListNonLrt

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameDisplaySlotDst; " D,"
     Print #fileNo, addTab(2); qualTabNameViewDst; " V"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "D.VIW_OID = V."; g_anOid
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "UPPER(V."; g_anName; ") = 'SR1'"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "V."; g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(1); ";"
 
     genProcSectionHeader fileNo, "Determine MaxSequenceNumber of NSR1-DisplaySlots"
     Print #fileNo, addTab(1); "SET v_MaxSequenceNumber = COALESCE(("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "MAX("; g_anSequenceNumber; ")"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameDisplaySlotDst
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "VIW_OID = v_nsr1ViewOid"
     Print #fileNo, addTab(1); "), 0);"
 
     genProcSectionHeader fileNo, "Create DisplaySlots for NSR1-Slots"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameDisplaySlotDst
     Print #fileNo, addTab(1); "("

     genAttrListForEntity relIndexDisplaySlot, eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, False, False, edomListNonLrt

     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(1); "SELECT"

     initAttributeTransformation transformation, 10, , , , "N."
     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conPsOid, "v_psOid"
     setAttributeMapping transformation, 3, conCreateTimestamp, "CURRENT TIMESTAMP"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "CURRENT TIMESTAMP"
     setAttributeMapping transformation, 5, conCreateUserName, "v_cdUserId"
     setAttributeMapping transformation, 6, conUpdateUserName, "v_cdUserId"
     setAttributeMapping transformation, 7, "VIW_OID", "v_nsr1ViewOid"
     setAttributeMapping transformation, 8, conSequenceNumber, "v_MaxSequenceNumber + E.NSR1ORDER"
     setAttributeMapping transformation, 9, "ESL_OID", "N.ESL_OID"
     setAttributeMapping transformation, 10, conVersionId, "1"
 
     genTransformedAttrListForEntity relIndexDisplaySlot, eactRelationship, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, , , , edomListNonLrt

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameNsr1ValidForOrganizationDst; " N"
     Print #fileNo, addTab(1); "INNER JOIN"
     Print #fileNo, addTab(2); qualTabNameEndSlotDst; " E"
     Print #fileNo, addTab(1); "ON"
     Print #fileNo, addTab(2); "N.ESL_OID = E."; g_anOid

     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "N."; g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "E.NSR1ORDER IS NOT NULL"
     Print #fileNo, addTab(1); ";"
 
     genProcSectionHeader fileNo, "Determine MaxSequenceNumber of NSR1-DisplaySlots"
     Print #fileNo, addTab(1); "SET v_MaxSequenceNumber = COALESCE(("
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "MAX("; g_anSequenceNumber; ")"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualTabNameDisplaySlotDst
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "VIW_OID = v_nsr1ViewOid"
     Print #fileNo, addTab(1); "), 0);"
 
     genProcSectionHeader fileNo, "Create DisplaySlot for DUP-Slot"
     Print #fileNo, addTab(1); "INSERT INTO"
     Print #fileNo, addTab(2); qualTabNameDisplaySlotDst
     Print #fileNo, addTab(1); "("

     genAttrListForEntity relIndexDisplaySlot, eactRelationship, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, False, False, edomListNonLrt

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "SELECT"

     initAttributeTransformation transformation, 10, , , , "D."
     setAttributeMapping transformation, 1, conOid, "NEXTVAL FOR " & qualSeqNameOid
     setAttributeMapping transformation, 2, conPsOid, "v_psOid"
     setAttributeMapping transformation, 3, conCreateTimestamp, "CURRENT TIMESTAMP"
     setAttributeMapping transformation, 4, conLastUpdateTimestamp, "CURRENT TIMESTAMP"
     setAttributeMapping transformation, 5, conCreateUserName, "v_cdUserId"
     setAttributeMapping transformation, 6, conUpdateUserName, "v_cdUserId"
     setAttributeMapping transformation, 7, "VIW_OID", "v_nsr1ViewOid"
     setAttributeMapping transformation, 8, conSequenceNumber, "v_MaxSequenceNumber + 1"
     setAttributeMapping transformation, 9, "ESL_OID", "E." & g_anOid
     setAttributeMapping transformation, 10, conVersionId, "1"
 
     genTransformedAttrListForEntity relIndexDisplaySlot, eactRelationship, transformation, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, , , , edomValueNonLrt

     Print #fileNo, addTab(1); "FROM"
     Print #fileNo, addTab(2); qualTabNameEndSlotDst; " E"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "E."; g_anIsDuplicating; " = "; gc_dbTrue
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "E."; g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(1); ";"

     ' ########################################################################
     genProcSectionHeader fileNo, "Update GeneralSettings"
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); qualTabNameGeneralSettings
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); "LASTCENTRALDATATRANSFERBEGIN = endTimestamp_out,"
     Print #fileNo, addTab(2); g_anUpdateUser; " = v_cdUserId,"
     Print #fileNo, addTab(2); g_anLastUpdateTimestamp; " = CURRENT TIMESTAMP"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anPsOid; " = v_psOid"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "mark active transaction as 'factory takeover' (should already be done by application)"
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); qualTabNameLrt; " L"
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); g_anIsCentralDataTransfer; " = "; gc_dbTrue
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); g_anOid; " = v_lrtoid"
     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "determine current set of FTO-CONFLICTs", 1
     Dim qualProcNameGetConflicts As String
     qualProcNameGetConflicts = _
       genQualProcName( _
         g_sectionIndexAliasLrt, spnFtoGetConflicts, ddlType, dstOrgIndex, dstPoolIndex _
       )
 
     Print #fileNo, addTab(1); "SET v_stmntTxt = 'CALL "; qualProcNameGetConflicts; "(?,?)';"

     Print #fileNo,
     Print #fileNo, addTab(1); "PREPARE v_stmnt FROM v_stmntTxt;"
     Print #fileNo,
     Print #fileNo, addTab(1); "EXECUTE"
     Print #fileNo, addTab(2); "v_stmnt"
     Print #fileNo, addTab(1); "INTO"
     Print #fileNo, addTab(2); "v_rowCount"
     Print #fileNo, addTab(1); "USING"
     Print #fileNo, addTab(2); "v_lrtoid"
     Print #fileNo, addTab(1); ";"

     If simulate Then
       genSpLogProcExit fileNo, qualProcName, ddlType, 1, "endTimestamp_out", "refId_out", "rowCount_out"
     Else
       genSpLogProcExit fileNo, qualProcName, ddlType, 1, "endTimestamp_out", "rowCount_out"
     End If

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   Next j
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Private Sub genFtoConflictSpecLine( _
   fileNo As Integer, _
   ByVal objClassIndex As Integer, _
   ByRef attrName As String, _
   ByVal messageId As Long, _
   ByVal conflictTypeId As Integer, _
   ByVal includeColon As Boolean, _
   Optional ByVal indent As Integer = 6 _
 )
   Print #fileNo, addTab(indent); "('"; g_classes.descriptors(objClassIndex).classIdStr; "', '"; attrName; "', "; CStr(messageId); ", "; CStr(conflictTypeId); ")"; IIf(includeColon, ",", "")
 End Sub
 
 
 Private Sub genFactoryTakeOverPriceConflictHandling( _
   thisOrgIndex As Integer, _
   thisPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType = edtPdm And (thisOrgIndex < 0 Or thisPoolIndex < 0) Then
     ' Factory-Take-Over is only supported at 'pool-level'
     Exit Sub
   End If
 
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameConflict As String
   qualTabNameConflict = genQualTabNameByClassIndex(g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   ' ####################################################################################################################
   ' #    Factory Data Take-Over: determine prices 'in conflict'
   ' ####################################################################################################################
   Dim qualPriceConflictProcName As String
   qualPriceConflictProcName = genQualProcName(g_sectionIndexAliasLrt, spnFtoGetPriceConflicts, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualViewNamePropertyLrtMqt As String
   qualViewNamePropertyLrtMqt = _
       genQualViewNameByEntityIndex( _
         g_classIndexProperty, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False, , "" _
       )
 
   Dim qualViewNameGenericCodeLrtMqt As String
   qualViewNameGenericCodeLrtMqt = _
       genQualViewNameByEntityIndex( _
         g_classIndexGenericCode, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False, , "" _
       )
 
   Dim qualViewNameEndSlotGen As String
   qualViewNameEndSlotGen = _
       genQualViewNameByEntityIndex( _
         g_classIndexEndSlot, eactClass, ddlType, thisOrgIndex, thisPoolIndex, True, True, True, False, , "" _
       )

   Dim qualViewNameEndSlotGenNl As String
   qualViewNameEndSlotGenNl = _
       genQualViewNameByEntityIndex( _
         g_classIndexEndSlot, eactClass, ddlType, thisOrgIndex, thisPoolIndex, True, True, True, True, , "" _
       )

   Dim qualTabNamePropertyTemplate As String
   qualTabNamePropertyTemplate = genQualTabNameByClassIndex(g_classIndexPropertyTemplate, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNamePricePreferences As String
   qualTabNamePricePreferences = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType, thisOrgIndex)
 
   Dim qualTabNameGenericAspectLrt As String
   qualTabNameGenericAspectLrt = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, False, True)

   Dim qualViewNameGenericAspectLrtMqt As String
   qualViewNameGenericAspectLrtMqt = _
       genQualViewNameByEntityIndex( _
         g_classIndexGenericAspect, eactClass, ddlType, thisOrgIndex, thisPoolIndex, False, True, True, False, , "" _
       )
 
   Dim qualTabNameUser As String
   qualTabNameUser = genQualTabNameByClassIndex(g_classIndexUser, ddlType, thisOrgIndex)
 
   Dim qualTabNameOrg As String
   qualTabNameOrg = genQualTabNameByClassIndex(g_classIndexOrganization, ddlType, thisOrgIndex)
 
   printSectionHeader "SP for determining 'records in price conflict during Factory Data Take-Over'", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualPriceConflictProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "lrtOid_in", getDbDatatypeByDomainIndex(g_domainIndexOid), True, "OID of the LRT holding the FTO-data"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records identified as 'being in conflict'"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables"
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_psOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_divOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_lrtCdUserId", g_dbtUserId, "NULL"
   genVarDecl fileNo, "v_isCentralDatatransfer", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_endtime", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   genVarDecl fileNo, "v_elpId", "INTEGER", "106"
   genVarDecl fileNo, "v_enpId", "INTEGER", "107"
   genVarDecl fileNo, "v_tireOmissionElpId", "INTEGER", "144"
   genVarDecl fileNo, "v_tireOmissionEnpId", "INTEGER", "145"
   genVarDecl fileNo, "v_priceTemplateId", "INTEGER", "NULL"
   genVarDecl fileNo, "v_tireOmissionPriceTemplate", "INTEGER", "NULL"
   genVarDecl fileNo, "v_langIdUser", "INTEGER", "NULL"
   genVarDecl fileNo, "v_primLangIdOrg", "INTEGER", "NULL"
   genVarDecl fileNo, "v_secLangIdOrg", "INTEGER", "NULL"
   genVarDecl fileNo, "v_oidOfinvalidPrice", "BIGINT", "NULL"

   genProcSectionHeader fileNo, "temporary table for PriceConflicts", 1
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); gc_tempTabNameConflictPrice
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "PRICE_OID                 "; g_dbtOid; ","
   Print #fileNo, addTab(2); "PRICE_COUNT               "; g_dbtInteger; ","
   Print #fileNo, addTab(2); "PRICE_LRTSTATE            "; g_dbtInteger; ","
   Print #fileNo, addTab(2); "OMISSIONPRICE_OID         "; g_dbtOid; ","
   Print #fileNo, addTab(2); "OMISSIONPRICE_COUNT       "; g_dbtInteger; ","
   Print #fileNo, addTab(2); "OMISSIONPRICE_LRTSTATE    "; g_dbtInteger; ","
   Print #fileNo, addTab(2); "NATIONALPRICE_OID         "; g_dbtOid; ","
   Print #fileNo, addTab(2); "NATIONALOMISSIONPRICE_OID "; g_dbtOid; ","
   Print #fileNo, addTab(2); "LEADINGCODE_OID           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "LEADINGCODE_NUMBER        VARCHAR(320),"
   Print #fileNo, addTab(2); "LEADINGSLOT_OID           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "LEADINGSLOT_STRING        VARCHAR(320)"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True

   genProcSectionHeader fileNo, "temporary table for Slot OID and label mapping", 1
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); gc_tempTabNameConflictSlotNames
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "SLOT_OID    "; g_dbtOid; ","
   Print #fileNo, addTab(2); "SLOT_STRING VARCHAR(320)"
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True
 
   Print #fileNo, addTab(1); "CREATE INDEX "; gc_tempTabNameConflictPrice; "_OID_INDEX ON "; gc_tempTabNameConflictPrice; " (LEADINGSLOT_OID) COLLECT STATISTICS;"
 
   genSpLogProcEnter fileNo, qualPriceConflictProcName, ddlType, 1, "lrtOid_in", "rowCount_out"
 
   genDb2RegVarCheckDdl fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1
 
   genProcSectionHeader fileNo, "verify that LRT corresponds to FTO and is consistent to 'current ProductStructure'"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "L."; g_anPsOid; ","
   Print #fileNo, addTab(2); "P.PDIDIV_OID,"
   Print #fileNo, addTab(2); "U."; g_anUserId; ","
   Print #fileNo, addTab(2); "L."; g_anIsCentralDataTransfer; ","
   Print #fileNo, addTab(2); "L."; g_anEndTime
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_psOid,"
   Print #fileNo, addTab(2); "v_divOid,"
   Print #fileNo, addTab(2); "v_lrtCdUserId,"
   Print #fileNo, addTab(2); "v_isCentralDatatransfer,"
   Print #fileNo, addTab(2); "v_endtime"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameLrt; " L"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure; " P"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "L."; g_anPsOid; " = P."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameUser; " U"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "L.UTROWN_OID = U."; g_anOid
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "L."; g_anOid; " = lrtOid_in"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_psOid IS NULL THEN"
   genProcSectionHeader fileNo, "LRT does not exist", 2, True
   genSignalDdlWithParms "lrtNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(lrtOid_in))"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_psOid <> "; g_activePsOidDdl; " THEN"
   genProcSectionHeader fileNo, "LRT does not match current PS", 2, True
   genSignalDdl "incorrPsTag", fileNo, 2
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_endTime IS NOT NULL THEN"
   genProcSectionHeader fileNo, "LRT is already closed", 2, True
   genSignalDdl "lrtClosed", fileNo, 2
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_isCentralDatatransfer <> 1 THEN"
   genProcSectionHeader fileNo, "LRT does not refer to FTO", 2, True
   genSignalDdlWithParms "lrtNotFto", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(lrtOid_in))"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   genProcSectionHeader fileNo, "determine update-records in '" + qualTabNameGenericAspectLrt + "' causing conflict"
   genProcSectionHeader fileNo, "If price conflict determination ist to be done according to price preferences of the current organization then ...", , True
   Print #fileNo, addTab(1); "IF 1 = ("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "ISCONFLICTDETERMFORPRICES"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNamePricePreferences
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(1); ") THEN"
   Print #fileNo,
   genProcSectionHeader fileNo, "Group codePriceAssignments and paintZonePriceAssignments, that are inserted, updated or deleted in the current LRT, and which are of ELP or TireOmissionELP, resp. ENP or TireOmissionENP, depending", 2
   genProcSectionHeader fileNo, "on flag isEnpBasedForNP in price preferences of the current organization", 2, True
   genProcSectionHeader fileNo, "Read flag isEnpBasedForNP in price preferences", 2
   Print #fileNo, addTab(2); "IF 0 = ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "ISENPBASEDFORNP"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNamePricePreferences
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(2); ") THEN"
   Print #fileNo, addTab(3); "SET v_priceTemplateId  = v_elpId;"
   Print #fileNo, addTab(3); "SET v_tireOmissionPriceTemplate = v_tireOmissionElpId;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_priceTemplateId  = v_enpId;"
   Print #fileNo, addTab(3); "SET v_tireOmissionPriceTemplate = v_tireOmissionEnpId;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo,

   genProcSectionHeader fileNo, "Write into Conflict SessionTable", 2
   genProcSectionHeader fileNo, "Group by SR0-Kontext (String-Representation), leading code, leading slot, with, with not,", 2, True
   genProcSectionHeader fileNo, " allowed countries, disallowed countries, validTo, validFrom", 2, True
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); gc_tempTabNameConflictPrice
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "PRICE_OID,"
   Print #fileNo, addTab(3); "PRICE_COUNT,"
   Print #fileNo, addTab(3); "PRICE_LRTSTATE,"
   Print #fileNo, addTab(3); "OMISSIONPRICE_OID,"
   Print #fileNo, addTab(3); "OMISSIONPRICE_COUNT,"
   Print #fileNo, addTab(3); "OMISSIONPRICE_LRTSTATE,"
   Print #fileNo, addTab(3); "LEADINGCODE_OID,"
   Print #fileNo, addTab(3); "LEADINGSLOT_OID     "
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "MAX(CASE"
   Print #fileNo, addTab(6); "WHEN"
   Print #fileNo, addTab(7); "PT.ID = v_priceTemplateId"
   Print #fileNo, addTab(6); "THEN"
   Print #fileNo, addTab(7); "S.OID"
   Print #fileNo, addTab(6); "ELSE"
   Print #fileNo, addTab(7); "0"
   Print #fileNo, addTab(5); "END) AS PRICE_OID,"
   Print #fileNo, addTab(3); "SUM(CASE"
   Print #fileNo, addTab(6); "WHEN"
   Print #fileNo, addTab(7); "PT.ID = v_priceTemplateId"
   Print #fileNo, addTab(6); "THEN"
   Print #fileNo, addTab(7); "1"
   Print #fileNo, addTab(6); "ELSE"
   Print #fileNo, addTab(7); "0"
   Print #fileNo, addTab(5); "END) AS PRICE_COUNT,"
   Print #fileNo, addTab(3); "MAX(CASE"
   Print #fileNo, addTab(6); "WHEN"
   Print #fileNo, addTab(7); "PT.ID = v_priceTemplateId"
   Print #fileNo, addTab(6); "THEN"
   Print #fileNo, addTab(7); "S.LRTSTATE"
   Print #fileNo, addTab(6); "ELSE"
   Print #fileNo, addTab(7); "0"
   Print #fileNo, addTab(5); "END) AS PRICE_LRTSTATE,"
   Print #fileNo, addTab(3); "MAX(CASE"
   Print #fileNo, addTab(6); "WHEN"
   Print #fileNo, addTab(7); "PT.ID = v_tireOmissionPriceTemplate"
   Print #fileNo, addTab(6); "THEN"
   Print #fileNo, addTab(7); "S.OID"
   Print #fileNo, addTab(6); "ELSE"
   Print #fileNo, addTab(7); "0"
   Print #fileNo, addTab(5); "END) AS OMISSIONPRICE_OID,"
   Print #fileNo, addTab(3); "SUM(CASE"
   Print #fileNo, addTab(6); "WHEN"
   Print #fileNo, addTab(7); "PT.ID = v_tireOmissionPriceTemplate"
   Print #fileNo, addTab(6); "THEN"
   Print #fileNo, addTab(7); "1"
   Print #fileNo, addTab(6); "ELSE"
   Print #fileNo, addTab(7); "0"
   Print #fileNo, addTab(5); "END) AS OMISSIONPRICE_COUNT,"
   Print #fileNo, addTab(3); "MAX(CASE"
   Print #fileNo, addTab(6); "WHEN"
   Print #fileNo, addTab(7); "PT.ID = v_tireOmissionPriceTemplate"
   Print #fileNo, addTab(6); "THEN"
   Print #fileNo, addTab(7); "S.LRTSTATE"
   Print #fileNo, addTab(6); "ELSE"
   Print #fileNo, addTab(7); "0"
   Print #fileNo, addTab(5); "END) AS OMISSIONPRIC_LRTSTATE,"
   Print #fileNo, addTab(3); "BCDBCD_OID,"
   Print #fileNo, addTab(3); "BESESL_OID"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameGenericAspectLrt; " S"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); qualViewNamePropertyLrtMqt; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "S.PRPAPR_OID = P.OID"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); qualTabNamePropertyTemplate; " PT"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "P.PTMHTP_OID = PT.OID"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "S.INLRT = lrtOid_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "S."; g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "S.CLASSID IN ('09031', '09033')"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "( PT.ID = v_priceTemplateId OR PT.ID = v_tireOmissionPriceTemplate )"
   Print #fileNo, addTab(2); "GROUP BY"
   Print #fileNo, addTab(3); "S.SR0CONTEXT,"
   Print #fileNo, addTab(3); "S.BCDBCD_OID,"
   Print #fileNo, addTab(3); "S.BESESL_OID,"
   Print #fileNo, addTab(3); "S.WITEXP_OID,"
   Print #fileNo, addTab(3); "S.WINEXP_OID,"
   Print #fileNo, addTab(3); "S.ACLACL_OID,"
   Print #fileNo, addTab(3); "S.DCLDCL_OID,"
   Print #fileNo, addTab(3); "S.VALIDFROM,"
   Print #fileNo, addTab(3); "S.VALIDTO,"
   Print #fileNo, addTab(3); "S.LRTSTATE"
   Print #fileNo, addTab(2); ";"
 
 
   genProcSectionHeader fileNo, "take over slot oids to slot mapping table", 2
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); gc_tempTabNameConflictSlotNames
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SLOT_OID "
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "DISTINCT LEADINGSLOT_OID"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); gc_tempTabNameConflictPrice
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "LEADINGSLOT_OID IS NOT NULL;"


   genProcSectionHeader fileNo, "Check Consistency: Only max 1 ELN(ENP) and only max 1 OmitELN(ENP)", 2
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN PRICE_COUNT > 1 THEN PRICE_OID"
   Print #fileNo, addTab(4); "ELSE OMISSIONPRICE_OID"
   Print #fileNo, addTab(3); "END"
   Print #fileNo, addTab(2); "INTO"
   Print #fileNo, addTab(3); "v_oidOfinvalidPrice"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); gc_tempTabNameConflictPrice
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "PRICE_COUNT > 1"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "OMISSIONPRICE_COUNT > 1"
   Print #fileNo, addTab(2); ";"

   Print #fileNo, addTab(2); "IF v_oidOfinvalidPrice IS NOT NULL THEN"
   genSignalDdlWithParms "priceGrpNotValid", fileNo, 3, , , , , , , , , , "RTRIM(CHAR(v_oidOfinvalidPrice))"
   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo,
   genProcSectionHeader fileNo, "For each central price and central tire omission price check for a national price reference and national tire omission price reference", 2
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); gc_tempTabNameConflictPrice; " C"
   Print #fileNo, addTab(2); "SET ("
   Print #fileNo, addTab(3); "NATIONALPRICE_OID"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "NP.OID"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualViewNameGenericAspectLrtMqt; " NP"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "NP.CCPCCP_OID = C.PRICE_OID"
   Print #fileNo, addTab(3); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "C.PRICE_LRTSTATE IN (2, 3)"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); gc_tempTabNameConflictPrice; " C"
   Print #fileNo, addTab(2); "SET ("
   Print #fileNo, addTab(3); "NATIONALOMISSIONPRICE_OID"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "NO.OID"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualViewNameGenericAspectLrtMqt; " NO"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "NO.CCPCCP_OID = C.OMISSIONPRICE_OID"
   Print #fileNo, addTab(3); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "C.OMISSIONPRICE_LRTSTATE IN (2, 3)"
   Print #fileNo, addTab(2); ";"
 
 
   genProcSectionHeader fileNo, "Pricechanges / Deletions without national Prices are irrelevant", 2
   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); gc_tempTabNameConflictPrice
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "PRICE_LRTSTATE IN (2, 3)"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "NATIONALPRICE_OID IS NULL"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "NATIONALOMISSIONPRICE_OID IS NULL"
   Print #fileNo, addTab(2); ";"
   Print #fileNo,
   genProcSectionHeader fileNo, "Enrich session table with CodeNumbers", 2
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); gc_tempTabNameConflictPrice; " C"
   Print #fileNo, addTab(2); "SET ("
   Print #fileNo, addTab(3); "LEADINGCODE_NUMBER"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "GC.CODENUMBER"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualViewNameGenericCodeLrtMqt; " GC"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "GC.OID = C.LEADINGCODE_OID"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "LEADINGCODE_OID IS NOT NULL"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LEADINGCODE_NUMBER IS NULL"
   Print #fileNo, addTab(2); ";"
 
   Print #fileNo,
   genProcSectionHeader fileNo, "Enrich slot oid to string mapping table with NlStrings", 2
   genProcSectionHeader fileNo, "Step 1 - For dataLanguage@User", 2, True
   Print #fileNo, addTab(2); "SET v_langIdUser = ("
   Print #fileNo, addTab(3); "SELECT "
   Print #fileNo, addTab(4); "DATALANGUAGE_ID "
   Print #fileNo, addTab(3); "FROM "
   Print #fileNo, addTab(4); qualTabNameUser
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "CDUSERID = v_lrtCdUserId"
   Print #fileNo, addTab(3); ");"
   Print #fileNo,
   Print #fileNo, addTab(2); "IF v_langIdUser IS NOT NULL THEN"
   Print #fileNo,
   Print #fileNo, addTab(3); "UPDATE"
   Print #fileNo, addTab(4); gc_tempTabNameConflictSlotNames; " C"
   Print #fileNo, addTab(3); "SET ("
   Print #fileNo, addTab(4); "SLOT_STRING"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "="
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN ESGNL.LABEL_ISNATACTIVE=1 THEN ESGNL.LABEL_NATIONAL"
   Print #fileNo, addTab(6); "ELSE ESGNL.LABEL"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualViewNameEndSlotGen; " ESG"
   Print #fileNo, addTab(4); "JOIN"
   Print #fileNo, addTab(5); qualViewNameEndSlotGenNl; " ESGNL"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "ESG.OID = ESGNL.ESL_OID"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "ESG.ESL_OID = C.SLOT_OID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "ESGNL.LANGUAGE_ID = v_langIdUser"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "CURRENT TIMESTAMP BETWEEN ESG.VALIDFROM AND ESG.VALIDTO"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "SLOT_STRING IS NULL"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "Enrich slot oid to string mapping table with NlStrings", 2
   genProcSectionHeader fileNo, "Step 2 - For primaryLanguage@Org", 2, True
   Print #fileNo, addTab(2); "SET v_primLangIdOrg = ("
   Print #fileNo, addTab(3); "SELECT "
   Print #fileNo, addTab(4); "PRIMARYLANGUAGE "
   Print #fileNo, addTab(3); "FROM "
   Print #fileNo, addTab(4); qualTabNameOrg
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "OID = ( SELECT"
   Print #fileNo, addTab(9); "ORGOID"
   Print #fileNo, addTab(8); "FROM"
   Print #fileNo, addTab(9); g_qualTabNamePdmOrganization
   Print #fileNo, addTab(8); "WHERE"
   Print #fileNo, addTab(9); "ID="; genOrgIdByIndex(thisOrgIndex, ddlType)
   Print #fileNo, addTab(7); ")"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); ""
   Print #fileNo, addTab(2); "IF v_primLangIdOrg IS NOT NULL THEN "
   Print #fileNo, addTab(3); "UPDATE"
   Print #fileNo, addTab(4); gc_tempTabNameConflictSlotNames; " C"
   Print #fileNo, addTab(3); "SET ("
   Print #fileNo, addTab(4); "SLOT_STRING"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "="
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN ESGNL.LABEL_ISNATACTIVE=1 THEN ESGNL.LABEL_NATIONAL"
   Print #fileNo, addTab(6); "ELSE ESGNL.LABEL"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualViewNameEndSlotGen; " ESG"
   Print #fileNo, addTab(4); "JOIN"
   Print #fileNo, addTab(5); qualViewNameEndSlotGenNl; " ESGNL"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "ESG.OID = ESGNL.ESL_OID"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "ESG.ESL_OID = C.SLOT_OID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "ESGNL.LANGUAGE_ID = v_primLangIdOrg"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "CURRENT TIMESTAMP BETWEEN ESG.VALIDFROM AND ESG.VALIDTO"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "SLOT_STRING IS NULL"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"

   genProcSectionHeader fileNo, "Enrich slot oid to string mapping table with NlStrings", 2
   genProcSectionHeader fileNo, "Step 3 - For secondaryLanguage@Org", 2, True
   Print #fileNo, addTab(2); "SET v_secLangIdOrg = ("
   Print #fileNo, addTab(3); "SELECT "
   Print #fileNo, addTab(4); "FALLBACKLANGUAGE"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameOrg
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "OID = ( SELECT"
   Print #fileNo, addTab(9); "ORGOID"
   Print #fileNo, addTab(8); "FROM"
   Print #fileNo, addTab(9); g_qualTabNamePdmOrganization
   Print #fileNo, addTab(8); "WHERE"
   Print #fileNo, addTab(9); "ID="; genOrgIdByIndex(thisOrgIndex, ddlType)
   Print #fileNo, addTab(7); ")"
   Print #fileNo, addTab(3); ");"
   Print #fileNo, addTab(2); ""
   Print #fileNo, addTab(2); "IF v_secLangIdOrg IS NOT NULL THEN"
   Print #fileNo, addTab(3); "UPDATE"
   Print #fileNo, addTab(4); gc_tempTabNameConflictSlotNames; " C"
   Print #fileNo, addTab(3); "SET ("
   Print #fileNo, addTab(4); "SLOT_STRING"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "="
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "SELECT"
   Print #fileNo, addTab(5); "CASE"
   Print #fileNo, addTab(6); "WHEN ESGNL.LABEL_ISNATACTIVE=1 THEN ESGNL.LABEL_NATIONAL"
   Print #fileNo, addTab(6); "ELSE ESGNL.LABEL"
   Print #fileNo, addTab(5); "END"
   Print #fileNo, addTab(4); "FROM"
   Print #fileNo, addTab(5); qualViewNameEndSlotGen; " ESG"
   Print #fileNo, addTab(4); "JOIN"
   Print #fileNo, addTab(5); qualViewNameEndSlotGenNl; " ESGNL"
   Print #fileNo, addTab(4); "ON"
   Print #fileNo, addTab(5); "ESG.OID = ESGNL.ESL_OID"
   Print #fileNo, addTab(4); "WHERE"
   Print #fileNo, addTab(5); "ESG.ESL_OID = C.SLOT_OID"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "ESGNL.LANGUAGE_ID = v_secLangIdOrg"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "CURRENT TIMESTAMP BETWEEN ESG.VALIDFROM AND ESG.VALIDTO"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "SLOT_STRING IS NULL"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); ""
   Print #fileNo, addTab(2); "UPDATE"
   Print #fileNo, addTab(3); gc_tempTabNameConflictPrice; " C"
   Print #fileNo, addTab(2); "SET ("
   Print #fileNo, addTab(3); "LEADINGSLOT_STRING"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "SLOT_STRING"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); gc_tempTabNameConflictSlotNames; " SN"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "SN.SLOT_OID = C.LEADINGSLOT_OID"
   Print #fileNo, addTab(2); " )"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "LEADINGSLOT_OID IS NOT NULL"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "LEADINGSLOT_STRING IS NULL"
   Print #fileNo, addTab(2); ";"

   Print #fileNo,
   genProcSectionHeader fileNo, "Insert sessionTable entries into Conflict table", 2
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(3); qualTabNameConflict
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "OID,"
   Print #fileNo, addTab(3); "CLASSID,"
   Print #fileNo, addTab(3); "CONFLICTTYPE_ID,"
   Print #fileNo, addTab(3); "CONFLICTSTATE_ID,"
   Print #fileNo, addTab(3); "MESSAGEID,"
   Print #fileNo, addTab(3); "MESSAGEARGUMENT,"
   Print #fileNo, addTab(3); "LEADINGCODE,"
   Print #fileNo, addTab(3); "LEADINGSLOT,"
   Print #fileNo, addTab(3); "FAPPRA_OID,"
   Print #fileNo, addTab(3); "FTPPRA_OID,"
   Print #fileNo, addTab(3); "NAPPRA_OID,"
   Print #fileNo, addTab(3); "NTPPRA_OID,"
   Print #fileNo, addTab(3); "CLRLRT_OID,"
   Print #fileNo, addTab(3); "PS_OID,"
   Print #fileNo, addTab(3); "CREATEUSER,"
   Print #fileNo, addTab(3); "CREATETIMESTAMP,"
   Print #fileNo, addTab(3); "UPDATEUSER,"
   Print #fileNo, addTab(3); "LASTUPDATETIMESTAMP,"
   Print #fileNo, addTab(3); "VERSIONID"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "NEXTVAL FOR "; qualSeqNameOid; ","
   Print #fileNo, addTab(3); "'27010',"
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN SC.PRICE_LRTSTATE=1 OR SC.OMISSIONPRICE_LRTSTATE=1 THEN 9"
   Print #fileNo, addTab(4); "WHEN SC.PRICE_LRTSTATE=2 OR SC.OMISSIONPRICE_LRTSTATE=2 THEN 10"
   Print #fileNo, addTab(4); "WHEN SC.PRICE_LRTSTATE=3 OR SC.OMISSIONPRICE_LRTSTATE=3 THEN 11"
   Print #fileNo, addTab(4); "ELSE NULL"
   Print #fileNo, addTab(3); "END,"
   Print #fileNo, addTab(3); "1,"
   Print #fileNo, addTab(3); "1300029,"
   Print #fileNo, addTab(3); "null,"
   Print #fileNo, addTab(3); "TRIM(SUBSTR(SC.LEADINGCODE_NUMBER,1,15)),"
   Print #fileNo, addTab(3); "TRIM(SUBSTR(SC.LEADINGSLOT_STRING,1,300)),"
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN SC.PRICE_OID = 0 THEN NULL"
   Print #fileNo, addTab(4); "ELSE SC.PRICE_OID"
   Print #fileNo, addTab(3); "END IF,"
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN SC.OMISSIONPRICE_OID = 0 THEN NULL"
   Print #fileNo, addTab(4); "ELSE SC.OMISSIONPRICE_OID"
   Print #fileNo, addTab(3); "END IF,"
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN SC.NATIONALPRICE_OID = 0 THEN NULL"
   Print #fileNo, addTab(4); "ELSE SC.NATIONALPRICE_OID"
   Print #fileNo, addTab(3); "END IF,"
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN SC.NATIONALOMISSIONPRICE_OID = 0 THEN NULL"
   Print #fileNo, addTab(4); "ELSE SC.NATIONALOMISSIONPRICE_OID"
   Print #fileNo, addTab(3); "END IF,"
   Print #fileNo, addTab(3); "lrtOid_in,"
   Print #fileNo, addTab(3); "v_psOid,"
   Print #fileNo, addTab(3); "v_lrtCdUserId,"
   Print #fileNo, addTab(3); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(3); "v_lrtCdUserId,"
   Print #fileNo, addTab(3); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(3); "1"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); gc_tempTabNameConflictPrice; " SC"
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); " not exists (SELECT"
   Print #fileNo, addTab(10); "1"
   Print #fileNo, addTab(9); "FROM"
   Print #fileNo, addTab(10); qualTabNameConflict
   Print #fileNo, addTab(9); "WHERE"
   Print #fileNo, addTab(10); "CLRLRT_OID = lrtOid_in"
   Print #fileNo, addTab(11); "AND"
   Print #fileNo, addTab(10); "(FAPPRA_OID = SC.PRICE_OID"
   Print #fileNo, addTab(12); "OR"
   Print #fileNo, addTab(11); "FTPPRA_OID = SC.OMISSIONPRICE_OID)"
   Print #fileNo, addTab(9); ")"
   Print #fileNo, addTab(2); ";"

   genProcSectionHeader fileNo, "count the number of affected rows", 2
   Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNo, addTab(1); "END IF;  "
 
   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 Private Sub genFactoryTakeOverExtendedConflictHandling( _
   thisOrgIndex As Integer, _
   thisPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtPdm _
 )
   If ddlType = edtPdm And (thisOrgIndex < 0 Or thisPoolIndex < 0) Then
     ' Factory-Take-Over is only supported at 'pool-level'
     Exit Sub
   End If
 
   On Error GoTo ErrorExit

   Dim fileNo As Integer
   fileNo = openDdlFile(g_targetDir, g_sectionIndexProductStructure, processingStep, ddlType, thisOrgIndex, thisPoolIndex, , phaseUseCases, ldmIterationPoolSpecific)
 
   Dim qualTabNameLrt As String
   qualTabNameLrt = genQualTabNameByClassIndex(g_classIndexLrt, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualTabNameConflict As String
   qualTabNameConflict = genQualTabNameByClassIndex(g_classIndexConflict, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualSeqNameOid As String
   qualSeqNameOid = genQualOidSeqNameForOrg(thisOrgIndex, ddlType)

   ' ####################################################################################################################
   ' #    Factory Data Take-Over: determine records 'in conflict'
   ' ####################################################################################################################

   Dim qualProcName As String
   qualProcName = genQualProcName(g_sectionIndexAliasLrt, spnFtoGetConflicts, ddlType, thisOrgIndex, thisPoolIndex)

   Dim qualPriceConflictProcName As String
   qualPriceConflictProcName = genQualProcName(g_sectionIndexAliasLrt, spnFtoGetPriceConflicts, ddlType, thisOrgIndex, thisPoolIndex)

   printSectionHeader "SP for determining 'records in conflict during Factory Data Take-Over'", fileNo
   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "lrtOid_in", getDbDatatypeByDomainIndex(g_domainIndexOid), True, "OID of the LRT holding the FTO-data"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of records identified as 'being in conflict'"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables"
   genSigMsgVarDecl fileNo
   genVarDecl fileNo, "v_psOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_divOid", g_dbtOid, "NULL"
   genVarDecl fileNo, "v_lrtCdUserId", g_dbtUserId, "NULL"
   genVarDecl fileNo, "v_isCentralDatatransfer", g_dbtBoolean, "NULL"
   genVarDecl fileNo, "v_endtime", "TIMESTAMP", "NULL"
   genVarDecl fileNo, "v_rowCount", "INTEGER", "NULL"
   genVarDecl fileNo, "v_generalPriceConflict", g_dbtBoolean, gc_dbFalse
   genVarDecl fileNo, "v_stmntTxt", "VARCHAR(500)", "NULL"

   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare statement"
   genVarDecl fileNo, "v_stmnt", "STATEMENT"
 
   genProcSectionHeader fileNo, "temporary table for Conflicts", 1
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); gc_tempTabNameConflict
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "conflictClassId  "; g_dbtEntityId; ","
   Print #fileNo, addTab(2); "classId          "; g_dbtEntityId; ","
   Print #fileNo, addTab(2); "objectId         "; g_dbtOid; ","
   Print #fileNo, addTab(2); "ahObjectId       "; g_dbtOid; ","
   Print #fileNo, addTab(2); "attrName         CHAR(60),"
   Print #fileNo, addTab(2); "isGen            "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "isNl             "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "conflictType_Id  "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "messageId        BIGINT,"
   Print #fileNo, addTab(2); "messageArgument  VARCHAR(1000),"
   Print #fileNo, addTab(2); "nsr1Oid          "; g_dbtOid; ","
   Print #fileNo, addTab(2); "sr1Oid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "seqOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "canOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "nanOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "gcoOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "cnlOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "gcgOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "cgnOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "prpOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "plrOid           "; g_dbtOid
 '  SM Änderung PriceConflict: hier müssen noch die neuen Felder eingetragen werden - wird von RS realisiert
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True
 
   genProcSectionHeader fileNo, "temporary table for potentially multiple GenericAspect Conflicts", 1
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); gc_tempTabNameConflictMultiGa
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId          "; g_dbtEntityId; ","
   Print #fileNo, addTab(2); "objectId         "; g_dbtOid; ","
   Print #fileNo, addTab(2); "ahObjectId       "; g_dbtOid; ","
   Print #fileNo, addTab(2); "attrExclusionFormulaFactory "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrInclusionFormulaFactory "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrConclusionFactory       "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrNumValue                "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrValueGathering          "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrBoolValue               "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrExpression              "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "isGen            "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "isNl             "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "nsr1Oid          "; g_dbtOid; ","
   Print #fileNo, addTab(2); "sr1Oid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "seqOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "prpOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "plrOid           "; g_dbtOid
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True

   genProcSectionHeader fileNo, "temporary table for potentially multiple SRValidity Conflicts", 1
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); gc_tempTabNameConflictMultiSr
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId          "; g_dbtEntityId; ","
   Print #fileNo, addTab(2); "objectId         "; g_dbtOid; ","
   Print #fileNo, addTab(2); "ahObjectId       "; g_dbtOid; ","
   Print #fileNo, addTab(2); "attrModelType1   "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrModelType2   "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrExtTypeDesc  "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrModelDrive   "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrModelWheelBase "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrSr1Context   "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "isGen            "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "isNl             "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "nsr1Oid          "; g_dbtOid; ","
   Print #fileNo, addTab(2); "sr1Oid           "; g_dbtOid
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True

   genProcSectionHeader fileNo, "temporary table for potentially multiple GenericAspectNlText Conflicts", 1
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); gc_tempTabNameConflictMultiGaNl
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId          "; g_dbtEntityId; ","
   Print #fileNo, addTab(2); "objectId         "; g_dbtOid; ","
   Print #fileNo, addTab(2); "ahObjectId       "; g_dbtOid; ","
   Print #fileNo, addTab(2); "attrDescription  "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrTextValue    "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "isGen            "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "isNl             "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "prpOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "nanOid           "; g_dbtOid
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True
 
   genProcSectionHeader fileNo, "temporary table for potentially multiple GenericCodeNlText Conflicts", 1
   Print #fileNo, addTab(1); "DECLARE GLOBAL TEMPORARY TABLE"
   Print #fileNo, addTab(2); gc_tempTabNameConflictMultiCdNl
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "conflictClassId  "; g_dbtEntityId; ","
   Print #fileNo, addTab(2); "classId          "; g_dbtEntityId; ","
   Print #fileNo, addTab(2); "objectId         "; g_dbtOid; ","
   Print #fileNo, addTab(2); "ahObjectId       "; g_dbtOid; ","
   Print #fileNo, addTab(2); "attrLabel        "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrSortingCriterion      "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "attrICodeShortDescription "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "isGen            "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "isNl             "; g_dbtBoolean; ","
   Print #fileNo, addTab(2); "gcoOid           "; g_dbtOid; ","
   Print #fileNo, addTab(2); "cnlOid           "; g_dbtOid
   Print #fileNo, addTab(1); ")"
   genDdlForTempTableDeclTrailer fileNo, 1, True
 
   genSpLogProcEnter fileNo, qualProcName, ddlType, 1, "lrtOid_in", "rowCount_out"
 
   genDb2RegVarCheckDdl fileNo, ddlType, thisOrgIndex, thisPoolIndex, tvNull, 1
 
   genProcSectionHeader fileNo, "verify that LRT corresponds to FTO and is consistent to 'current ProductStructure'"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "L."; g_anPsOid; ","
   Print #fileNo, addTab(2); "P.PDIDIV_OID,"
   Print #fileNo, addTab(2); "U."; g_anUserId; ","
   Print #fileNo, addTab(2); "L."; g_anIsCentralDataTransfer; ","
   Print #fileNo, addTab(2); "L."; g_anEndTime
   Print #fileNo, addTab(1); "INTO"
   Print #fileNo, addTab(2); "v_psOid,"
   Print #fileNo, addTab(2); "v_divOid,"
   Print #fileNo, addTab(2); "v_lrtCdUserId,"
   Print #fileNo, addTab(2); "v_isCentralDatatransfer,"
   Print #fileNo, addTab(2); "v_endtime"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameLrt; " L"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameProductStructure; " P"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "L."; g_anPsOid; " = P."; g_anOid
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); g_qualTabNameUser; " U"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "L.UTROWN_OID = U."; g_anOid
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "L."; g_anOid; " = lrtOid_in"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_psOid IS NULL THEN"
   genProcSectionHeader fileNo, "LRT does not exist", 2, True
   genSignalDdlWithParms "lrtNotExist", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(lrtOid_in))"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_psOid <> "; g_activePsOidDdl; " THEN"
   genProcSectionHeader fileNo, "LRT does not match current PS", 2, True
   genSignalDdl "incorrPsTag", fileNo, 2
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_endTime IS NOT NULL THEN"
   genProcSectionHeader fileNo, "LRT is already closed", 2, True
   genSignalDdl "lrtClosed", fileNo, 2
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   Print #fileNo, addTab(1); "IF v_isCentralDatatransfer <> 1 THEN"
   genProcSectionHeader fileNo, "LRT does not refer to FTO", 2, True
   genSignalDdlWithParms "lrtNotFto", fileNo, 2, , , , , , , , , , "RTRIM(CHAR(lrtOid_in))"
   Print #fileNo, addTab(1); "END IF;"

   genProcSectionHeader fileNo, "initialize output parameter"
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   Dim qualTabNameGenericAspect As String
   qualTabNameGenericAspect = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNameGenericAspectLrt As String
   qualTabNameGenericAspectLrt = genQualTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, False, True)
 
   Dim qualTabNameGenericAspectNlText As String
   qualTabNameGenericAspectNlText = genQualNlTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNameGenericAspectNlTextLrt As String
   qualTabNameGenericAspectNlTextLrt = genQualNlTabNameByClassIndex(g_classIndexGenericAspect, ddlType, thisOrgIndex, thisPoolIndex, False, True)
 
   Dim qualTabNameExpression As String
   qualTabNameExpression = genQualTabNameByClassIndex(g_classIndexExpression, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNameGenericCodeLrt As String
   qualTabNameGenericCodeLrt = genQualTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, False, True)
 
   Dim qualTabNameGenericCodeNlText As String
   qualTabNameGenericCodeNlText = genQualNlTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex)
 
   Dim qualTabNameGenericCodeNlTextLrt As String
   qualTabNameGenericCodeNlTextLrt = genQualNlTabNameByClassIndex(g_classIndexGenericCode, ddlType, thisOrgIndex, thisPoolIndex, False, True)

   genProcSectionHeader fileNo, "determine update-records in '" & qualTabNameGenericAspectLrt & "' causing conflict"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGa"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrExclusionFormulaFactory,"
   Print #fileNo, addTab(2); "attrInclusionFormulaFactory,"
   Print #fileNo, addTab(2); "attrConclusionFactory,"
   Print #fileNo, addTab(2); "attrNumValue,"
   Print #fileNo, addTab(2); "attrValueGathering,"
   Print #fileNo, addTab(2); "attrBoolValue,"
   Print #fileNo, addTab(2); "attrExpression,"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "seqOid,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "plrOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "S.CLASSID,"
   Print #fileNo, addTab(2); "S.OID,"
   Print #fileNo, addTab(2); "S.AHOID,"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.EFNEXP_OID IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = T.EFFEXP_OID), 'not available')"
   Print #fileNo, addTab(7); "<>"
   Print #fileNo, addTab(6); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = S.EFFEXP_OID), 'not available')"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.IFNEXP_OID IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = T.IFFEXP_OID), 'not available')"
   Print #fileNo, addTab(7); "<>"
   Print #fileNo, addTab(6); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = S.IFFEXP_OID), 'not available')"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.CONEXP_OID IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,1000)) AS VARCHAR(1000)) FROM "; qualTabNameExpression; " X WHERE X.OID = T.COFEXP_OID), 'not available')"
   Print #fileNo, addTab(7); "<>"
   Print #fileNo, addTab(6); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,1000)) AS VARCHAR(1000)) FROM "; qualTabNameExpression; " X WHERE X.OID = S.COFEXP_OID), 'not available')"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.NUMVALUE_NATIONAL IS NOT NULL) AND (COALESCE(CHAR(S.NUMVALUE), '#') <> COALESCE(CHAR(T.NUMVALUE), '#'))"
   Print #fileNo, addTab(4); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.VALUEGATHERING_NATIONAL_ID IS NOT NULL) AND (COALESCE(CHAR(S.VALUEGATHERING_ID), '#') <> COALESCE(CHAR(T.VALUEGATHERING_ID), '#'))"
   Print #fileNo, addTab(4); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.BOOLVALUE_ISNATACTIVE = 1) AND (S.BOOLVALUE <> T.BOOLVALUE)"
   Print #fileNo, addTab(4); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.VALEXP_OID_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "("
   Print #fileNo, addTab(6); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = T.VALEXP_OID), 'not available')"
   Print #fileNo, addTab(7); "<>"
   Print #fileNo, addTab(6); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = S.VALEXP_OID), 'not available')"
   Print #fileNo, addTab(5); ")"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "(CASE WHEN S.CLASSID = '09005' THEN S.E1VEX1_OID ELSE CAST(NULL AS BIGINT) END),"
   Print #fileNo, addTab(2); "(CASE WHEN S.CLASSID = '09004' THEN S.OID ELSE CAST(NULL AS BIGINT) END),"
   Print #fileNo, addTab(2); "(CASE WHEN S.CLASSID = '09025' THEN S.OID ELSE CAST(NULL AS BIGINT) END),"
   Print #fileNo, addTab(2); "(CASE WHEN S.CLASSID IN ('09016','09017','09018','09019','09021','09022','09023','09024') THEN S.OID ELSE CAST(NULL AS BIGINT) END),"
   Print #fileNo, addTab(2); "(CASE WHEN S.CLASSID IN ( '09013' , '09014' ) THEN S.OID ELSE CAST(NULL AS BIGINT) END),"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "0"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericAspectLrt; " S"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericAspect; " T"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "T.OID = S.OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "S.INLRT = lrtOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "T.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.LRTSTATE = 2"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'exclusionFormulaFactory@CodePlausibilityRule'", 4
   Print #fileNo, addTab(4); "(T.EFNEXP_OID IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = T.EFFEXP_OID), 'not available')"
   Print #fileNo, addTab(6); "<>"
   Print #fileNo, addTab(5); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = S.EFFEXP_OID), 'not available')"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'inclusionFormulaFactory@CodePlausibilityRule'", 4
   Print #fileNo, addTab(4); "(T.IFNEXP_OID IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = T.IFFEXP_OID), 'not available')"
   Print #fileNo, addTab(6); "<>"
   Print #fileNo, addTab(5); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = S.IFFEXP_OID), 'not available')"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'conclusionFactory@SlotPlausibilityRule'", 4
   Print #fileNo, addTab(4); "(T.CONEXP_OID IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,1000)) AS VARCHAR(1000)) FROM "; qualTabNameExpression; " X WHERE X.OID = T.COFEXP_OID), 'not available')"
   Print #fileNo, addTab(6); "<>"
   Print #fileNo, addTab(5); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,1000)) AS VARCHAR(1000)) FROM "; qualTabNameExpression; " X WHERE X.OID = S.COFEXP_OID), 'not available')"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'numValue@SlotNumericPropertyAssignment'", 4
   Print #fileNo, addTab(4); "(T.NUMVALUE_NATIONAL IS NOT NULL) AND (COALESCE(CHAR(S.NUMVALUE), '#') <> COALESCE(CHAR(T.NUMVALUE), '#'))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'valueGathering@SlotNumericPropertyAssignment'", 4
   Print #fileNo, addTab(4); "(T.VALUEGATHERING_NATIONAL_ID IS NOT NULL) AND (COALESCE(CHAR(S.VALUEGATHERING_ID), '#') <> COALESCE(CHAR(T.VALUEGATHERING_ID), '#'))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'boolValue@SlotBooleanPropertyAssignment'", 4
   Print #fileNo, addTab(4); "(T.BOOLVALUE_ISNATACTIVE = 1) AND (S.BOOLVALUE <> T.BOOLVALUE)"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'expression@SlotTextPropertyAssignment'", 4
   Print #fileNo, addTab(4); "(T.VALEXP_OID_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = T.VALEXP_OID), 'not available')"
   Print #fileNo, addTab(6); "<>"
   Print #fileNo, addTab(5); "COALESCE((SELECT CAST(RTRIM(LEFT(X.TERMSTRING,2000)) AS VARCHAR(2000)) FROM "; qualTabNameExpression; " X WHERE X.OID = S.VALEXP_OID), 'not available')"
   Print #fileNo, addTab(4); ")"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,

   genInsertSessionConflictMultiGa fileNo

   Print #fileNo,
   genProcSectionHeader fileNo, "determin whether 'general price conflict record' applies"
   Print #fileNo, addTab(1); "IF EXISTS ("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "1"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameGenericAspectLrt
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "PS_OID = v_psOid"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "INLRT = lrtOid_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "CLASSID IN ('09031','09032','09033')"
   Print #fileNo, addTab(1); ") THEN"
   Print #fileNo, addTab(2); "SET v_generalPriceConflict = 1;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   genProcSectionHeader fileNo, "determine whether update of some nationalized SR1Validity-attribute causes conflict for NSR1"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiSr"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrModelType1,"
   Print #fileNo, addTab(2); "attrModelType2,"
   Print #fileNo, addTab(2); "attrExtTypeDesc,"
   Print #fileNo, addTab(2); "attrModelDrive,"
   Print #fileNo, addTab(2); "attrModelWheelBase,"
   Print #fileNo, addTab(2); "attrSr1Context,"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'09004',"
   Print #fileNo, addTab(2); "SR1_F.OID,"
   Print #fileNo, addTab(2); "SR1_F.AHOID,"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(COALESCE(SR1_F.MODELTYPE1, '') <> COALESCE(SR1_M.MODELTYPE1, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(COALESCE(SR1_F.MODELTYPE2, '') <> COALESCE(SR1_M.MODELTYPE2, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(COALESCE(SR1_F.EXTTYPEDESC, '') <> COALESCE(SR1_M.EXTTYPEDESC, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(COALESCE(SR1_F.MODELDRIVE, '') <> COALESCE(SR1_M.MODELDRIVE, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(COALESCE(SR1_F.MODELWHEELBASE, '') <> COALESCE(SR1_M.MODELWHEELBASE, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(COALESCE(SR1_F.SR1CONTEXT, '') <> COALESCE(SR1_M.SR1CONTEXT, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "NSR1.OID,"
   Print #fileNo, addTab(2); "SR1_F.OID,"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "0"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericAspectLrt; " SR1_F"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericAspect; " SR1_M"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "SR1_F.OID = SR1_M.OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericAspect; " NSR1"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "NSR1.E1VEX1_OID = SR1_M.OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NSR1.CLASSID = '09005'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NSR1.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SR1_F.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SR1_M.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SR1_F.CLASSID = '09004'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SR1_F.INLRT = lrtOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SR1_F.LRTSTATE = 2"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "(COALESCE(SR1_F.MODELTYPE1, '') <> COALESCE(SR1_M.MODELTYPE1, ''))"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(COALESCE(SR1_F.MODELTYPE2, '') <> COALESCE(SR1_M.MODELTYPE2, ''))"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(COALESCE(SR1_F.EXTTYPEDESC, '') <> COALESCE(SR1_M.EXTTYPEDESC, ''))"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(COALESCE(SR1_F.MODELDRIVE, '') <> COALESCE(SR1_M.MODELDRIVE, ''))"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(COALESCE(SR1_F.MODELWHEELBASE, '') <> COALESCE(SR1_M.MODELWHEELBASE, ''))"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "(COALESCE(SR1_F.SR1CONTEXT, '') <> COALESCE(SR1_M.SR1CONTEXT, ''))"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   genProcSectionHeader fileNo, "Split conflicts to seperate rows and insert to common session table"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.Conflict"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrName,"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'modelType1',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiSr"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrModelType1 = 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'modelType2',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiSr"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrModelType2 = 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'extTypeDesc',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiSr"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrExtTypeDesc = 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'modelDrive',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiSr"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrModelDrive = 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'modelWheelBase',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiSr"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrModelWheelBase = 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'sr1Context',"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiSr"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrSr1Context = 1"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   Print #fileNo,
   genProcSectionHeader fileNo, "determine update-records in '" & qualTabNameGenericAspectNlTextLrt & "' causing conflict"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGaNl"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrDescription,"
   Print #fileNo, addTab(2); "attrTextValue,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "nanOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "SAH.CLASSID,"
   Print #fileNo, addTab(2); "S.OID,"
   Print #fileNo, addTab(2); "S.AHOID,"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.DESCRIPTION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.DESCRIPTION, '') <> COALESCE(T.DESCRIPTION, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.TEXTVALUE_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.TEXTVALUE, '') <> COALESCE(T.TEXTVALUE, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "SAH.OID,"
   Print #fileNo, addTab(2); "S.OID,"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "1"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericAspectNlTextLrt; " S"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericAspectNlText; " T"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "T.OID = S.OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericAspectLrt; " SAH"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "S.GAS_OID = SAH.OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "S.INLRT = lrtOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "T.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.LRTSTATE = 2"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'description@PropertyAssignment'", 4
   Print #fileNo, addTab(4); "(T.DESCRIPTION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.DESCRIPTION, '') <> COALESCE(T.DESCRIPTION, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'textValue@SlotTextPropertyAssignment'", 4
   Print #fileNo, addTab(4); "(T.TEXTVALUE_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.TEXTVALUE, '') <> COALESCE(T.TEXTVALUE, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   'TF: CQDAT00027123: the record with lrtstate = 1 contains the merged entries
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGaNl"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrDescription,"
   Print #fileNo, addTab(2); "attrTextValue,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "nanOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "SAH.CLASSID,"
   Print #fileNo, addTab(2); "S.OID,"
   Print #fileNo, addTab(2); "S.AHOID,"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(S.DESCRIPTION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.DESCRIPTION_NATIONAL, '') <> COALESCE(S.DESCRIPTION, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(S.TEXTVALUE_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.TEXTVALUE_NATIONAL, '') <> COALESCE(S.TEXTVALUE, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "SAH.OID,"
   Print #fileNo, addTab(2); "S.OID,"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "1"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericAspectNlTextLrt; " S"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericAspectLrt; " SAH"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "S.GAS_OID = SAH.OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "S.INLRT = lrtOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.LRTSTATE = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'description@PropertyAssignment'", 4
   Print #fileNo, addTab(4); "(S.DESCRIPTION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.DESCRIPTION_NATIONAL, '') <> COALESCE(S.DESCRIPTION, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'textValue@SlotTextPropertyAssignment'", 4
   Print #fileNo, addTab(4); "(S.TEXTVALUE_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.TEXTVALUE_NATIONAL, '') <> COALESCE(S.TEXTVALUE, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,

   genProcSectionHeader fileNo, "Split conflicts to seperate rows and insert to common session table"
   Print #fileNo, addTab(2); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.Conflict"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrName,"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "nanOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'description',"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "nanOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGaNl"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrDescription = 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'textValue',"
   Print #fileNo, addTab(2); "prpOid,"
   Print #fileNo, addTab(2); "nanOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiGaNl"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrTextValue = 1"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   genProcSectionHeader fileNo, "determin whether 'general price conflict record' applies"
   Print #fileNo, addTab(1); "IF EXISTS ("
   Print #fileNo, addTab(2); "SELECT"
   Print #fileNo, addTab(3); "1"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); qualTabNameGenericAspectLrt
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "PS_OID = v_psOid"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "INLRT = lrtOid_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "CLASSID IN ('09031','09032','09033')"
   Print #fileNo, addTab(1); ") THEN"
   Print #fileNo, addTab(2); "SET v_generalPriceConflict = 1;"
   Print #fileNo, addTab(1); "END IF;"
   Print #fileNo,
   genProcSectionHeader fileNo, "determine whether update of some nationalized SR1Validity-attribute causes conflict for NSR1"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.Conflict"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrName,"
   Print #fileNo, addTab(2); "nsr1Oid,"
   Print #fileNo, addTab(2); "sr1Oid,"
   Print #fileNo, addTab(2); "nanOid,"
   Print #fileNo, addTab(2); "canOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'09004',"
   Print #fileNo, addTab(2); "SR1_FNL.GAS_OID,"
   Print #fileNo, addTab(2); "SR1_FNL.AHOID,"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(COALESCE(SR1_FNL.MODELNAME, '') <> COALESCE(SR1_MNL.MODELNAME, ''))"
   Print #fileNo, addTab(4); "THEN 'modelName'"
   genProcSectionHeader fileNo, "this is never reached", 4
   Print #fileNo, addTab(4); "ELSE 'unknown'"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "NSR1.OID,"
   Print #fileNo, addTab(2); "SR1_FNL.GAS_OID,"
   Print #fileNo, addTab(2); "NSR1_MNL.OID,"
   Print #fileNo, addTab(2); "SR1_FNL.OID,"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "1"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericAspectNlTextLrt; " SR1_FNL"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericAspectNlText; " SR1_MNL"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "SR1_FNL.OID = SR1_MNL.OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericAspect; " NSR1"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "NSR1.E1VEX1_OID = SR1_MNL.GAS_OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericAspectNlText; " NSR1_MNL"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "NSR1.OID = NSR1_MNL.GAS_OID"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NSR1_MNL.LANGUAGE_ID = SR1_MNL.LANGUAGE_ID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "NSR1.CLASSID = '09005'"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "NSR1.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SR1_FNL.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SR1_MNL.PS_OID = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SR1_FNL.INLRT = lrtOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "SR1_FNL.LRTSTATE = 2"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "(COALESCE(SR1_FNL.MODELNAME, '') <> COALESCE(SR1_MNL.MODELNAME, ''))"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
 
   genProcSectionHeader fileNo, "determine update-records in '" & qualTabNameGenericCodeNlTextLrt & "' causing conflict"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiCdNl"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrLabel,"
   Print #fileNo, addTab(2); "attrSortingCriterion,"
   Print #fileNo, addTab(2); "attrICodeShortDescription,"
   Print #fileNo, addTab(2); "gcoOid,"
   Print #fileNo, addTab(2); "cnlOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'05006',"
   Print #fileNo, addTab(2); "S.OID,"
   Print #fileNo, addTab(2); "S.AHOID,"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.LABEL_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.LABEL, '') <> COALESCE(T.LABEL, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.SORTINGCRITERION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.SORTINGCRITERION, '') <> COALESCE(T.SORTINGCRITERION, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(T.ICODESHORTDESCRIPTION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.ICODESHORTDESCRIPTION, '') <> COALESCE(T.ICODESHORTDESCRIPTION, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "SAH.OID,"
   Print #fileNo, addTab(2); "S.OID,"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "1"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCodeNlTextLrt; " S"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericCodeNlText; " T"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "T.OID = S.OID"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericCodeLrt; " SAH"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "S.GCO_OID = SAH.OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "S.INLRT = lrtOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.LRTSTATE = 2"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'label@GenericCode'", 4
   Print #fileNo, addTab(4); "(T.LABEL_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.LABEL, '') <> COALESCE(T.LABEL, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'sortingCriterion@GenericCode'", 4
   Print #fileNo, addTab(4); "(T.SORTINGCRITERION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.SORTINGCRITERION, '') <> COALESCE(T.SORTINGCRITERION, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'iCodeShortDescription@GenericCode'", 4
   Print #fileNo, addTab(4); "(T.ICODESHORTDESCRIPTION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.ICODESHORTDESCRIPTION, '') <> COALESCE(T.ICODESHORTDESCRIPTION, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,
   'TF: CQDAT00027123: the record with lrtstate = 1 contains the merged entries
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiCdNl"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrLabel,"
   Print #fileNo, addTab(2); "attrSortingCriterion,"
   Print #fileNo, addTab(2); "attrICodeShortDescription,"
   Print #fileNo, addTab(2); "gcoOid,"
   Print #fileNo, addTab(2); "cnlOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "'05006',"
   Print #fileNo, addTab(2); "S.OID,"
   Print #fileNo, addTab(2); "S.AHOID,"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(S.LABEL_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.LABEL_NATIONAL, '') <> COALESCE(S.LABEL, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(S.SORTINGCRITERION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.SORTINGCRITERION_NATIONAL, '') <> COALESCE(S.SORTINGCRITERION, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "CASE"
   Print #fileNo, addTab(4); "WHEN"
   Print #fileNo, addTab(5); "(S.ICODESHORTDESCRIPTION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(6); "AND"
   Print #fileNo, addTab(5); "(COALESCE(S.ICODESHORTDESCRIPTION_NATIONAL, '') <> COALESCE(S.ICODESHORTDESCRIPTION, ''))"
   Print #fileNo, addTab(5); "THEN 1"
   Print #fileNo, addTab(4); "ELSE 0"
   Print #fileNo, addTab(2); "END"
   Print #fileNo, addTab(2); "),"
   Print #fileNo, addTab(2); "SAH.OID,"
   Print #fileNo, addTab(2); "S.OID,"
   Print #fileNo, addTab(2); "0,"
   Print #fileNo, addTab(2); "1"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualTabNameGenericCodeNlTextLrt; " S"
   Print #fileNo, addTab(1); "INNER JOIN"
   Print #fileNo, addTab(2); qualTabNameGenericCodeLrt; " SAH"
   Print #fileNo, addTab(1); "ON"
   Print #fileNo, addTab(2); "S.GCO_OID = SAH.OID"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "S.INLRT = lrtOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "S.LRTSTATE = 1"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'label@GenericCode'", 4
   Print #fileNo, addTab(4); "(S.LABEL_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.LABEL_NATIONAL, '') <> COALESCE(S.LABEL, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'sortingCriterion@GenericCode'", 4
   Print #fileNo, addTab(4); "(S.SORTINGCRITERION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.SORTINGCRITERION_NATIONAL, '') <> COALESCE(S.SORTINGCRITERION, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(4); "OR"
   Print #fileNo, addTab(3); "("
   genProcSectionHeader fileNo, "check attribute 'iCodeShortDescription@GenericCode'", 4
   Print #fileNo, addTab(4); "(S.ICODESHORTDESCRIPTION_NATIONAL IS NOT NULL)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "(COALESCE(S.ICODESHORTDESCRIPTION_NATIONAL, '') <> COALESCE(S.ICODESHORTDESCRIPTION, ''))"
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
   Print #fileNo,

   genProcSectionHeader fileNo, "Split conflicts to seperate rows and insert to common session table"
   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); "SESSION.Conflict"
   Print #fileNo, addTab(1); "("
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "attrName,"
   Print #fileNo, addTab(2); "gcoOid,"
   Print #fileNo, addTab(2); "cnlOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'label',"
   Print #fileNo, addTab(2); "gcoOid,"
   Print #fileNo, addTab(2); "cnlOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiCdNl"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrLabel = 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'sortingCriterion',"
   Print #fileNo, addTab(2); "gcoOid,"
   Print #fileNo, addTab(2); "cnlOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiCdNl"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrSortingCriterion = 1"
   Print #fileNo, addTab(1); "UNION"
   Print #fileNo, addTab(1); "SELECT"
   Print #fileNo, addTab(2); "classId,"
   Print #fileNo, addTab(2); "objectId,"
   Print #fileNo, addTab(2); "ahObjectId,"
   Print #fileNo, addTab(2); "'iCodeShortDescription',"
   Print #fileNo, addTab(2); "gcoOid,"
   Print #fileNo, addTab(2); "cnlOid,"
   Print #fileNo, addTab(2); "isGen,"
   Print #fileNo, addTab(2); "isNl"
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); "SESSION.ConflictMultiCdNl"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "attrICodeShortDescription = 1"
   Print #fileNo, addTab(1); ";"
 
   ' newRS end

   genProcSectionHeader fileNo, "determine message IDs and types of conflicts"

   Print #fileNo, addTab(1); "UPDATE"
   Print #fileNo, addTab(2); gc_tempTabNameConflict; " TC"
   Print #fileNo, addTab(1); "SET"
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "messageId,"
   Print #fileNo, addTab(3); "conflictClassId,"
   Print #fileNo, addTab(3); "conflictType_Id"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(2); "="
   Print #fileNo, addTab(2); "("
   Print #fileNo, addTab(3); "SELECT"

   Print #fileNo, addTab(4); "V_ConflictDetails.messageId,"
   Print #fileNo, addTab(4); "V_ConflictType.classId,"
   Print #fileNo, addTab(4); "V_ConflictType.typeId"

   Print #fileNo, addTab(3); "FROM"

   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "VALUES"
   Print #fileNo, addTab(6); "(", CStr(gc_ftoConflictTypeNSr1); ", '"; g_classes.descriptors(g_classIndexTypeConflict).classIdStr; "'),"
   Print #fileNo, addTab(6); "(", CStr(gc_ftoConflictTypeGeneralPrice); ", '"; g_classes.descriptors(g_classIndexGeneralPriceConflict).classIdStr; "'),"
   Print #fileNo, addTab(6); "(", CStr(gc_ftoConflictTypeCodeLabel); ", '"; g_classes.descriptors(g_classIndexCodeLabelConflict).classIdStr; "'),"
   Print #fileNo, addTab(6); "(", CStr(gc_ftoConflictTypeTypeLabel); ", '"; g_classes.descriptors(g_classIndexTypeLabelConflict).classIdStr; "'),"
   Print #fileNo, addTab(6); "(", CStr(gc_ftoConflictTypePlausibilityRule); ", '"; g_classes.descriptors(g_classIndexPlausibilityRuleConflict).classIdStr; "'),"
   Print #fileNo, addTab(6); "(", CStr(gc_ftoConflictTypeCodePropertyAssignment); ", '"; g_classes.descriptors(g_classIndexCodePropertyAssignmentConflict).classIdStr; "'),"
   Print #fileNo, addTab(6); "(", CStr(gc_ftoConflictTypeSlotPropertyAssignment); ", '"; g_classes.descriptors(g_classIndexSlotPropertyAssignmentConflict).classIdStr; "')"
   Print #fileNo, addTab(4); ") V_ConflictType ( typeId, classId )"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); "("
   Print #fileNo, addTab(5); "VALUES"
   genFtoConflictSpecLine fileNo, g_classIndexCodePlausibilityRule, "inclusionFormulaFactory", 1300041, gc_ftoConflictTypePlausibilityRule, True
   genFtoConflictSpecLine fileNo, g_classIndexCodePlausibilityRule, "exclusionFormulaFactory", 1300042, gc_ftoConflictTypePlausibilityRule, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotPlausibilityRule, "conclusionFactory", 1300043, gc_ftoConflictTypePlausibilityRule, True
   genFtoConflictSpecLine fileNo, g_classIndexGenericCode, "label ", 1300030, gc_ftoConflictTypeCodeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexGenericCode, "sortingCriterion", 1300031, gc_ftoConflictTypeCodeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexGenericCode, "iCodeShortDescription", 1300032, gc_ftoConflictTypeCodeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexSr1Validity, "modelName", 1300035, gc_ftoConflictTypeTypeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexSr1Validity, "sr1Context", 1300036, gc_ftoConflictTypeTypeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexSr1Validity, "modelType1", 1300037, gc_ftoConflictTypeTypeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexSr1Validity, "modelType2", 1300037, gc_ftoConflictTypeTypeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexSr1Validity, "extTypeDesc", 1300038, gc_ftoConflictTypeTypeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexSr1Validity, "modeDrive", 1300039, gc_ftoConflictTypeTypeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexSr1Validity, "modelWheelBase", 1300040, gc_ftoConflictTypeTypeLabel, True
   genFtoConflictSpecLine fileNo, g_classIndexCodeBinaryPropertyAssignment, "description", 1300011, gc_ftoConflictTypeCodePropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexCodeBooleanPropertyAssignment, "description", 1300012, gc_ftoConflictTypeCodePropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexCodeNumericPropertyAssignment, "description", 1300013, gc_ftoConflictTypeCodePropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexCodeTextPropertyAssignment, "description", 1300014, gc_ftoConflictTypeCodePropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexCodeBooleanPropertyAssignment, "boolValue", 1300015, gc_ftoConflictTypeCodePropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexCodeNumericPropertyAssignment, "numValue", 1300016, gc_ftoConflictTypeCodePropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexCodeNumericPropertyAssignment, "valueGathering", 1300017, gc_ftoConflictTypeCodePropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexCodeTextPropertyAssignment, "textValue", 1300018, gc_ftoConflictTypeCodePropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexCodeTextPropertyAssignment, "expression", 1300019, gc_ftoConflictTypeCodePropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotBinaryPropertyAssignment, "description", 1300020, gc_ftoConflictTypeSlotPropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotBooleanPropertyAssignment, "description", 1300021, gc_ftoConflictTypeSlotPropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotNumericPropertyAssignment, "description", 1300022, gc_ftoConflictTypeSlotPropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotNumericPropertyAssignment, "description", 1300022, gc_ftoConflictTypeSlotPropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotTextPropertyAssignment, "description", 1300023, gc_ftoConflictTypeSlotPropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotBooleanPropertyAssignment, "boolValue", 1300024, gc_ftoConflictTypeSlotPropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotNumericPropertyAssignment, "numValue", 1300025, gc_ftoConflictTypeSlotPropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotNumericPropertyAssignment, "valueGathering", 1300026, gc_ftoConflictTypeSlotPropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotTextPropertyAssignment, "textValue", 1300027, gc_ftoConflictTypeSlotPropertyAssignment, True
   genFtoConflictSpecLine fileNo, g_classIndexSlotTextPropertyAssignment, "expression", 1300028, gc_ftoConflictTypeSlotPropertyAssignment, False

   Print #fileNo, addTab(4); ") V_ConflictDetails ( classId, attributeName, messageId, conflictTypeId )"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "V_ConflictType.typeId = V_ConflictDetails.conflictTypeId"

   Print #fileNo, addTab(3); "WHERE"
 
   Print #fileNo, addTab(4); "V_ConflictDetails.classId = TC.classId"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "V_ConflictDetails.attributeName = TC.attrName"
   Print #fileNo, addTab(3); "FETCH FIRST 1 ROW ONLY"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader fileNo, "delete 'previous set of open conflict records'"
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); qualTabNameConflict
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); "CLRLRT_OID = lrtOid_in"
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); conConflictStateId; " = "; CStr(gc_ftoConflictStateOpen)
   Print #fileNo, addTab(3); "AND"
   Print #fileNo, addTab(2); g_anConflictTypeId; " IN ("; _
                             CStr(gc_ftoConflictTypeNSr1); ", "; _
                             CStr(gc_ftoConflictTypeCodeLabel); ", "; _
                             CStr(gc_ftoConflictTypeGeneralPrice); ", "; _
                             CStr(gc_ftoConflictTypeCodePropertyAssignment); ", "; _
                             CStr(gc_ftoConflictTypeSlotPropertyAssignment); ", "; _
                             CStr(gc_ftoConflictTypePlausibilityRule); ", "; _
                             CStr(gc_ftoConflictTypeTypeLabel); ")"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "ignore current conflict records which are marked as 'resolved'"
   Print #fileNo, addTab(1); "DELETE FROM"
   Print #fileNo, addTab(2); gc_tempTabNameConflict; " TC"
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "EXISTS ("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameConflict; " C"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "C."; conConflictStateId; " = "; CStr(gc_ftoConflictStateResolved)
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C."; g_anConflictTypeId; " = TC."; g_anConflictTypeId
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C."; g_anMessageId; " = TC.messageId"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "TC.objectId = COALESCE(C.SR1SR1_OID, C.SEQSEQ_OID, C.PRPPRP_OID, C.PLRPLR_OID, C.NANCNL_OID, C.CNLCNL_OID)"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "C.CLRLRT_OID = lrtOid_in"
   Print #fileNo, addTab(2); ")"
   Print #fileNo, addTab(1); ";"
 
   genProcSectionHeader fileNo, "set '" & g_anHasConflict & "-flag' for remaining records (loop over involved tables)"
 
   Print #fileNo, addTab(1); "FOR ahTabLoop AS"
   Print #fileNo, addTab(2); "WITH"
   Print #fileNo, addTab(3); "V_ClassIds (classId)"
   Print #fileNo, addTab(2); "AS"
   Print #fileNo, addTab(3); "( SELECT DISTINCT classId FROM "; gc_tempTabNameConflict; ")"
   Print #fileNo, addTab(2); "SELECT DISTINCT"
   Print #fileNo, addTab(3); "E_AH."; g_anAcmEntityId; " AS c_ahClassId,"
   Print #fileNo, addTab(3); "P."; g_anPdmFkSchemaName; " AS c_pdmSchemaName,"
   Print #fileNo, addTab(3); "P."; g_anPdmTableName; " AS c_pdmTableName,"
   Print #fileNo, addTab(3); "E_AH."; g_anAcmIsPs; " AS c_IsPs"
   Print #fileNo, addTab(2); "FROM"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " E"
   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); "V_ClassIds C"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "E."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "E."; g_anAcmEntityId; " = C.classId"

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameAcmEntity; " E_AH"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "E_AH."; g_anAcmEntityId; " = E."; g_anAhCid
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "E_AH."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNameLdmTable; " L"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "L."; g_anAcmEntityType; " = E_AH."; g_anAcmEntityType
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anAcmEntitySection; " = E_AH."; g_anAcmEntitySection
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anAcmEntityName; " = E_AH."; g_anAcmEntityName

   Print #fileNo, addTab(2); "INNER JOIN"
   Print #fileNo, addTab(3); g_qualTabNamePdmTable; " P"
   Print #fileNo, addTab(2); "ON"
   Print #fileNo, addTab(3); "L."; g_anLdmSchemaName; " = P."; g_anPdmLdmFkSchemaName
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmTableName; " = P."; g_anPdmLdmFkTableName

   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); "L."; g_anLdmIsGen; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "L."; g_anLdmIsNl; " = "; gc_dbFalse
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anOrganizationId; " = "; genOrgId(thisOrgIndex, ddlType, True)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "P."; g_anPoolTypeId; " = "; genPoolId(thisPoolIndex, ddlType)
   Print #fileNo, addTab(1); "DO"

   genProcSectionHeader fileNo, "reset '" & g_anHasConflict & "' for all records having no DB-related conflict (i.e. only SOLVER-related conflics)", 2, True
   Print #fileNo, addTab(2); "SET v_stmntTxt ="
   Print #fileNo, addTab(3); "'UPDATE ' ||"
   Print #fileNo, addTab(4); "c_pdmSchemaName || '.' || c_pdmTableName || ' T ' ||"
   Print #fileNo, addTab(3); "'SET ' ||"
   Print #fileNo, addTab(4); "'T."; g_anHasConflict; " = 0 ' ||"
   Print #fileNo, addTab(3); "'WHERE ' ||"
   Print #fileNo, addTab(4); "'T."; g_anHasConflict; " = 1 ' ||"
   Print #fileNo, addTab(5); "'AND ' ||"
   Print #fileNo, addTab(4); "'T."; g_anInLrt; " = ' || lrtOid_in || ' ' ||"
   Print #fileNo, addTab(5); "'AND '"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "IF c_IsPs = 1 THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||"
   Print #fileNo, addTab(4); "'T."; g_anPsOid; " = ' || v_psOid || ' '"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt = v_stmntTxt ||"
   Print #fileNo, addTab(4); "'T.CDIDIV_OID = ' || v_divOid || ' '"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "SET v_stmntTxt = v_stmntTxt ||"
   Print #fileNo, addTab(5); "'AND ' ||"
   Print #fileNo, addTab(4); "'NOT EXISTS(' ||"
   Print #fileNo, addTab(5); "'SELECT ' ||"
   Print #fileNo, addTab(6); "'1 ' ||"
   Print #fileNo, addTab(5); "'FROM ' ||"
   Print #fileNo, addTab(6); "'"; qualTabNameConflict; " C ' ||"
   Print #fileNo, addTab(5); "'WHERE  ' ||"
   Print #fileNo, addTab(6); "'T."; g_anOid; " = COALESCE(C.SR1SR1_OID, C.SEQSEQ_OID, C.PRPPRP_OID, C.PLRPLR_OID, C.NANCNL_OID, C.CNLCNL_OID) ' ||"
   Print #fileNo, addTab(7); "'AND ' ||"
   Print #fileNo, addTab(6); "'LEFT(CHAR(C."; g_anMessageId; "), 2) = ''13''' ||"
   Print #fileNo, addTab(7); "'AND ' ||"
   Print #fileNo, addTab(6); "'C.CLRLRT_OID = ' || lrtOid_in ||"
   Print #fileNo, addTab(4); "')'"
   Print #fileNo, addTab(2); ";"
   Print #fileNo, addTab(2); "EXECUTE IMMEDIATE(v_stmntTxt);"

   genProcSectionHeader fileNo, "prepare update-statement for this table", 2, True
   Print #fileNo, addTab(2); "IF c_IsPs = 1 THEN"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'UPDATE ' || c_pdmSchemaName || '.' || c_pdmTableName || ' SET "; g_anHasConflict; " = 1 WHERE "; g_anOid; " = ? AND "; g_anInLrt; " = ' || lrtOid_in || ' AND "; g_anPsOid; " = ' || v_psOid ;"
   Print #fileNo, addTab(2); "ELSE"
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'UPDATE ' || c_pdmSchemaName || '.' || c_pdmTableName || ' SET "; g_anHasConflict; " = 1 WHERE "; g_anOid; " = ? AND "; g_anInLrt; " = ' || lrtOid_in || ' AND CDIDIV_OID = ' || v_divOid ;"
   Print #fileNo, addTab(2); "END IF;"
   Print #fileNo, addTab(2); "PREPARE v_stmnt FROM v_stmntTxt;"

   genProcSectionHeader fileNo, "loop over involved objects in this table", 2
   Print #fileNo, addTab(2); "FOR oidLoop AS"
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "TC.objectId AS c_objectId"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); gc_tempTabNameConflict; " TC"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " E"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "TC.classId = E."; g_anAcmEntityId
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "E."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(3); "INNER JOIN"
   Print #fileNo, addTab(4); g_qualTabNameAcmEntity; " E_AH"
   Print #fileNo, addTab(3); "ON"
   Print #fileNo, addTab(4); "E_AH."; g_anAcmEntityId; " = E."; g_anAhCid
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "E_AH."; g_anAcmEntityType; " = '"; gc_acmEntityTypeKeyClass; "'"
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); "E_AH."; g_anAcmEntityId; " = c_ahClassId"

   Print #fileNo, addTab(2); "DO"

   genProcSectionHeader fileNo, "update this record", 3, True
   Print #fileNo, addTab(3); "EXECUTE v_stmnt USING c_objectId;"

   Print #fileNo, addTab(2); "END FOR;"

   Print #fileNo, addTab(1); "END FOR;"

   genProcSectionHeader fileNo, "persist current set of conflict records"

   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTabNameConflict
   Print #fileNo, addTab(1); "("

   Dim transformationConflict As AttributeListTransformation
   Dim tabColumns As EntityColumnDescriptors
   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformationConflict, 9
   setAttributeMapping transformationConflict, 1, conUpdateUser, ""
   setAttributeMapping transformationConflict, 2, conLastUpdateTimestamp, , ""
   setAttributeMapping transformationConflict, 3, "CBVCBM_OID", ""
   setAttributeMapping transformationConflict, 4, "LEADINGCODE", ""
   setAttributeMapping transformationConflict, 5, "LEADINGSLOT", ""
   setAttributeMapping transformationConflict, 6, "FAPPRA_OID", ""
   setAttributeMapping transformationConflict, 7, "FTPPRA_OID", ""
   setAttributeMapping transformationConflict, 8, "NAPPRA_OID", ""
   setAttributeMapping transformationConflict, 9, "NTPPRA_OID", ""

   genTransformedAttrListForEntityWithColReuse g_classIndexConflict, eactClass, transformationConflict, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , edomList

   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"

   tabColumns = nullEntityColumnDescriptors
   initAttributeTransformation transformationConflict, 30
   setAttributeMapping transformationConflict, 1, conUpdateUser, ""
   setAttributeMapping transformationConflict, 2, conLastUpdateTimestamp, , ""
   setAttributeMapping transformationConflict, 3, "CBVCBM_OID", ""
   setAttributeMapping transformationConflict, 4, "LEADINGCODE", ""
   setAttributeMapping transformationConflict, 5, "LEADINGSLOT", ""
   setAttributeMapping transformationConflict, 6, "FAPPRA_OID", ""
   setAttributeMapping transformationConflict, 7, "FTPPRA_OID", ""
   setAttributeMapping transformationConflict, 8, "NAPPRA_OID", ""
   setAttributeMapping transformationConflict, 9, "NTPPRA_OID", ""
   setAttributeMapping transformationConflict, 10, conOid, "NEXTVAL FOR " & qualSeqNameOid
   setAttributeMapping transformationConflict, 11, conClassId, "conflictClassId  "
   setAttributeMapping transformationConflict, 12, conConflictTypeId, "conflictType_ID"
   setAttributeMapping transformationConflict, 13, conConflictStateId, CStr(gc_ftoConflictStateOpen)
   setAttributeMapping transformationConflict, 14, conMessageId, "messageId"
   setAttributeMapping transformationConflict, 15, "CLRLRT_OID", "lrtOid_in"
   setAttributeMapping transformationConflict, 16, "NR1NS1_OID", "nsr1Oid"
   setAttributeMapping transformationConflict, 17, "SR1SR1_OID", "sr1Oid"
   setAttributeMapping transformationConflict, 18, "SEQSEQ_OID", "seqOid"
   setAttributeMapping transformationConflict, 19, "CANCNL_OID", "canOid"
   setAttributeMapping transformationConflict, 20, "NANCNL_OID", "nanOid"
   setAttributeMapping transformationConflict, 21, "CCOCOD_OID", "gcoOid"
   setAttributeMapping transformationConflict, 22, "CNLCNL_OID", "cnlOid"
   setAttributeMapping transformationConflict, 23, "PRPPRP_OID", "prpOid"
   setAttributeMapping transformationConflict, 24, "PLRPLR_OID", "plrOid"
   setAttributeMapping transformationConflict, 25, conPsOid, "v_psOid"
   setAttributeMapping transformationConflict, 26, conCreateUser, "v_lrtCdUserId"
   setAttributeMapping transformationConflict, 27, conCreateTimestamp, "CURRENT TIMESTAMP"
   setAttributeMapping transformationConflict, 28, conUpdateUser, "v_lrtCdUserId"
   setAttributeMapping transformationConflict, 29, conLastUpdateTimestamp, "CURRENT TIMESTAMP"
   setAttributeMapping transformationConflict, 30, conVersionId, "1"

   genTransformedAttrListForEntityWithColReuse g_classIndexConflict, eactClass, transformationConflict, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, 2, , , edomList

   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); gc_tempTabNameConflict
   Print #fileNo, addTab(1); "WHERE"
   Print #fileNo, addTab(2); "conflictClassId IS NOT NULL"
   Print #fileNo, addTab(1); ";"

   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

   genProcSectionHeader fileNo, "create 'general price conflict' if required"
   genProcSectionHeader fileNo, "and call subroutine for detailed price conflicts"
   Print #fileNo, addTab(1); "IF v_generalPriceConflict = 1 THEN"

   Print #fileNo, addTab(2); "IF NOT EXISTS("
   Print #fileNo, addTab(3); "SELECT"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); "FROM"
   Print #fileNo, addTab(4); qualTabNameConflict
   Print #fileNo, addTab(3); "WHERE"
   Print #fileNo, addTab(4); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); "CLRLRT_OID = lrtOid_in"
   Print #fileNo, addTab(5); "AND"
   Print #fileNo, addTab(4); g_anConflictTypeId; " = "; CStr(gc_ftoConflictTypeGeneralPrice)
   Print #fileNo, addTab(2); ") THEN"

   Print #fileNo, addTab(3); "INSERT INTO"
   Print #fileNo, addTab(4); qualTabNameConflict
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); g_anOid; ","
   Print #fileNo, addTab(4); g_anCid; ","
   Print #fileNo, addTab(4); g_anConflictTypeId; ","
   Print #fileNo, addTab(4); conConflictStateId; ","
   Print #fileNo, addTab(4); g_anMessageId; ","
   Print #fileNo, addTab(4); "CLRLRT_OID,"
   Print #fileNo, addTab(4); g_anPsOid; ","
   Print #fileNo, addTab(4); g_anCreateUser; ","
   Print #fileNo, addTab(4); g_anCreateTimestamp; ","
   Print #fileNo, addTab(4); g_anVersionId
   Print #fileNo, addTab(3); ")"
   Print #fileNo, addTab(3); "VALUES"
   Print #fileNo, addTab(3); "("
   Print #fileNo, addTab(4); "NEXTVAL FOR "; qualSeqNameOid; ","
   Print #fileNo, addTab(4); "'"; g_classes.descriptors(g_classIndexGeneralPriceConflict).classIdStr; "',"
   Print #fileNo, addTab(4); CStr(gc_ftoConflictTypeGeneralPrice); ","
   Print #fileNo, addTab(4); CStr(gc_ftoConflictStateOpen); ","
   Print #fileNo, addTab(4); "1300029,"
   Print #fileNo, addTab(4); "lrtOid_in,"
   Print #fileNo, addTab(4); "v_psOid,"
   Print #fileNo, addTab(4); "v_lrtCdUserId,"
   Print #fileNo, addTab(4); "CURRENT TIMESTAMP,"
   Print #fileNo, addTab(4); "1"
   Print #fileNo, addTab(3); ");"

   Print #fileNo,
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + 1;"
   genProcSectionHeader fileNo, "call subroutine for detailed price conflicts", 3
   Print #fileNo, addTab(3); "SET v_stmntTxt = 'CALL "; qualPriceConflictProcName; "(?,?)';"
   Print #fileNo, addTab(3); "PREPARE v_stmnt FROM v_stmntTxt;"
   Print #fileNo, addTab(3); "EXECUTE"
   Print #fileNo, addTab(4); "v_stmnt"
   Print #fileNo, addTab(3); "INTO"
   Print #fileNo, addTab(4); "v_rowCount"
   Print #fileNo, addTab(3); "USING"
   Print #fileNo, addTab(4); "lrtOid_in"
   Print #fileNo, addTab(3); ";"
   Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"

   Print #fileNo, addTab(2); "END IF;"

   Print #fileNo, addTab(1); "ELSE"

   Print #fileNo, addTab(2); "DELETE FROM"
   Print #fileNo, addTab(3); qualTabNameConflict
   Print #fileNo, addTab(2); "WHERE"
   Print #fileNo, addTab(3); g_anConflictTypeId; " = "; CStr(gc_ftoConflictTypeGeneralPrice)
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); "CLRLRT_OID = lrtOid_in"
   Print #fileNo, addTab(4); "AND"
   Print #fileNo, addTab(3); g_anPsOid; " = v_psOid"
   Print #fileNo, addTab(2); ";"

   Print #fileNo, addTab(1); "END IF;"

   genSpLogProcExit fileNo, qualProcName, ddlType, 1, "lrtOid_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 Sub genFtoSupportSpsForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   srcOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstOrgIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False _
 )
   Dim sectionName As String
   Dim acmEntityName As String
   Dim acmEntityShortName As String
   Dim entityTypeDescr As String
   Dim isUserTransactional As Boolean
   Dim isPsTagged As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim aggHeadIdStr As String
   Dim aggHeadClassIndex As Integer
   Dim dbAcmEntityType As String
   Dim attrRefs As AttrDescriptorRefs
   Dim attrRefsInclSubClasses As AttrDescriptorRefs
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
   Dim isDeletable As Boolean
   Dim hasNoIdentity As Boolean
   Dim ignoreForChangelog As Boolean
   Dim hasNlAttributes As Boolean
   Dim hasNlAttributesInGen As Boolean
   Dim useMqtToImplementLrtForEntity As Boolean
   Dim aggHeadNavPathToOrg As NavPathFromClassToClass
   Dim subClassIdStrList As String
   Dim aggHeadSubClassIdStrList As String
   Dim hasOrganizationSpecificReference As Boolean
   Dim relRefsToOrganizationSpecificClasses As RelationshipDescriptorRefs
   Dim condenseData As Boolean
   Dim isAggHead As Boolean
   Dim isDisAllowedCountriesAspect As Boolean
   Dim isTerm As Boolean
   Dim fkAttrToDiv As String
   Dim hasNationalColumn As Boolean

   On Error GoTo ErrorExit

   fkAttrToDiv = ""
   subClassIdStrList = ""
   hasNationalColumn = False

   If acmEntityType = eactClass Then
       aggHeadNavPathToOrg = g_classes.descriptors(acmEntityIndex).navPathToOrg

       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       acmEntityName = g_classes.descriptors(acmEntityIndex).className
       acmEntityShortName = g_classes.descriptors(acmEntityIndex).shortName
       hasNlAttributes = IIf(forGen, g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses)
       hasNlAttributesInGen = g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity
       If forNl Then
         entityTypeDescr = "ACM-Class (NL-Text)"
       Else
         entityTypeDescr = "ACM-Class"
         If g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex > 0 And Not g_classes.descriptors(acmEntityIndex).isPsTagged And Not forNl Then
             fkAttrToDiv = IIf(g_classes.descriptors(acmEntityIndex).navPathToDiv.navDirection = etLeft, g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex).leftFkColName(ddlType), g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex).rightFkColName(ddlType))
         End If
       End If
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       aggHeadIdStr = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
       attrRefsInclSubClasses = g_classes.descriptors(acmEntityIndex).attrRefsInclSubClasses
       relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
       isDeletable = g_classes.descriptors(acmEntityIndex).isDeletable
       isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       ignoreForChangelog = g_classes.descriptors(acmEntityIndex).ignoreForChangelog
       isDisAllowedCountriesAspect = False
       isTerm = (UCase(g_classes.descriptors(acmEntityIndex).className) = "TERM")
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
       hasOrganizationSpecificReference = g_classes.descriptors(acmEntityIndex).hasOrganizationSpecificReference
       relRefsToOrganizationSpecificClasses = g_classes.descriptors(acmEntityIndex).relRefsToOrganizationSpecificClasses
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex) And Not forGen And Not forNl

       subClassIdStrList = IIf(g_classes.descriptors(acmEntityIndex).isAbstract, "", "'" & g_classes.descriptors(acmEntityIndex).classIdStr & "'")
       Dim i As Integer
       For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive)
           If Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isAbstract Then
             subClassIdStrList = subClassIdStrList & IIf(subClassIdStrList = "", "", ",") & "'" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).classIdStr & "'"
           End If
       Next i
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       acmEntityName = g_relationships.descriptors(acmEntityIndex).relName
       acmEntityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       If forNl Then
         entityTypeDescr = "ACM-Relationship (NL-Text)"
       Else
         entityTypeDescr = "ACM-Relationship"
       End If

       hasNlAttributes = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0
       hasNlAttributesInGen = False
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       aggHeadIdStr = g_relationships.descriptors(acmEntityIndex).aggHeadClassIdStr
       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       dbAcmEntityType = "R"
       attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
       attrRefsInclSubClasses = g_relationships.descriptors(acmEntityIndex).attrRefs
       relRefs.numRefs = 0
       isGenForming = False
       hasNoIdentity = False
       ignoreForChangelog = g_relationships.descriptors(acmEntityIndex).ignoreForChangelog
       subClassIdStrList = "'" & g_relationships.descriptors(acmEntityIndex).relIdStr & "'"
       isDisAllowedCountriesAspect = (UCase(g_relationships.descriptors(acmEntityIndex).relName) = "DISALLOWEDCOUNTRIESASPECT")
       isTerm = False
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
       hasOrganizationSpecificReference = g_relationships.descriptors(acmEntityIndex).hasOrganizationSpecificReference
       condenseData = False
       isAggHead = False
   Else
     Exit Sub
   End If

   Dim qualSourceTabName As String
   Dim qualSourceParTabName As String
   Dim qualTargetRefTabName As String
   Dim qualTargetViewName As String

   qualSourceTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen, , , forNl)
   qualSourceParTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen)
   qualTargetRefTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, , , forNl)
   qualTargetViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, True, useMqtToImplementLrtForEntity, forNl)

   If Not generateLrt Or Not isUserTransactional Then
     Exit Sub
   End If
   If ddlType = edtPdm And (srcOrgIndex < 1 Or srcPoolIndex < 1) Then
     ' LRT is only supported at 'pool-level'
     Exit Sub
   End If
 
   Dim transformation As AttributeListTransformation
   Dim qualRelTabOrg As String, relOrgEntityIdStr As String
 
   ' ####################################################################################################################
   ' #    SP for Factory Data Takeover
   ' ####################################################################################################################
 
   Dim qualProcName As String
   qualProcName = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, , , forNl, spnFactoryTakeOver)

   printSectionHeader "SP for Factory Data Takeover for """ & qualSourceTabName & """ (" & entityTypeDescr & " """ & _
     sectionName & "." & acmEntityName & """" & IIf(forGen, "(GEN)", "") & ")", fileNo

   Dim readUnCommitedInWorkDataPool As Boolean
   readUnCommitedInWorkDataPool = isPsTagged
   readUnCommitedInWorkDataPool = True ' all records wich are subject to FTO are locked by FTOLOCK

   Dim tabColumns As EntityColumnDescriptors
   Dim aggHeadContainsIsNotPublished As Boolean
   initAttributeTransformation transformation, 0
   tabColumns = nullEntityColumnDescriptors
   aggHeadContainsIsNotPublished = False

   If aggHeadClassIndex > 0 Then
     genTransformedAttrListForEntityWithColReuse aggHeadClassIndex, eactClass, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, False, forGen, edomNone
     For i = 1 To tabColumns.numDescriptors
       If tabColumns.descriptors(i).columnName = g_anIsNotPublished Then
         aggHeadContainsIsNotPublished = True
       End If
     Next i
   End If
 
   initAttributeTransformation transformation, 0
   tabColumns = nullEntityColumnDescriptors
   If forNl Then
     genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, , , ddlType, dstOrgIndex, dstPoolIndex, 2, forGen, False, , edomNone
   Else
     genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, False, forGen, edomNone
   End If
 
   Dim numAttrsToSkip As Integer
   numAttrsToSkip = 0
   For i = 1 To tabColumns.numDescriptors
       If (UCase(Right(tabColumns.descriptors(i).acmAttributeName, 8)) = "NATIONAL") Or _
          (Right(tabColumns.descriptors(i).columnName, 8) = "NATIONAL") Or _
          Right(tabColumns.descriptors(i).columnName, 12) = "_NATIONAL_ID" Or _
          Right(tabColumns.descriptors(i).columnName, 12) = "_ISNATACTIVE" Or _
          Right(tabColumns.descriptors(i).columnName, 9) = "_ISNATACT" Then
         numAttrsToSkip = numAttrsToSkip + 1
         hasNationalColumn = True
         If (tabColumns.descriptors(i).columnCategory And eacExpression) <> 0 And tabColumns.descriptors(i).acmAttributeIndex > 0 Then
             If g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).isNationalizable Or g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).ftoConflictWithSrcAttrIndex > 0 Then
               numAttrsToSkip = numAttrsToSkip + 1
             End If
         End If
       End If
   Next i

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "divisionOid_in", g_dbtOid, True, "OID of the Division owning the Product Structure"
   genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to process"
   genProcParm fileNo, "IN", "orgOid_in", g_dbtOid, True, "OID of the Organization"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by this call"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   If forNl And hasNationalColumn Then
     genVarDecl fileNo, "v_lrtId", g_dbtOid, "0"
   End If
   If UCase(acmEntityName) = "GENERICASPECT" And Not forNl Then
     genVarDecl fileNo, "v_isDpb", "SMALLINT", "0"
     genVarDecl fileNo, "v_isTakeoverBlockedPriceFlag", "SMALLINT", "0"
   End If
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "notFound", "02000"
   genCondDecl fileNo, "alreadyExist", "42710"
 
   genProcSectionHeader fileNo, "declare continue handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   If aggHeadContainsIsNotPublished Then
     genDdlForTempTablesChangeLog fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1
   End If
   genDdlForTempChangeLogSummary fileNo, 1, True
   If Not condenseData Then
     genDdlForTempImplicitChangeLogSummary fileNo, 1, True
   End If

   If forNl And hasNationalColumn Then
     genProcSectionHeader fileNo, "temporary table for specially handled nl records"
     Print #fileNo, addTab(0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(1); "SESSION.NL_TEXT_OIDS"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "FACTORY_NL_OID      "; g_dbtOid; ","
     Print #fileNo, addTab(1); "MPC_NL_OID   "; g_dbtOid
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "NOT LOGGED;"
   End If

   If UCase(acmEntityName) = "GENERICASPECT" And Not forNl Then
   ' special handling for CodeBaumusterValidities depends on PricePreferences
     Dim qualTabNamePricePreferences As String
     qualTabNamePricePreferences = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType, dstOrgIndex)
 
     Print #fileNo,
       Print #fileNo, addTab(1); "SELECT "
     Print #fileNo, addTab(2); "P.ISDPB, P.TAKEOVERBLOCKEDPRICEFLAG "
     Print #fileNo, addTab(1); "INTO "
     Print #fileNo, addTab(2); "v_isDpb, v_isTakeoverBlockedPriceFlag "
     Print #fileNo, addTab(1); "FROM "
     Print #fileNo, addTab(2); qualTabNamePricePreferences; " P "
     Print #fileNo, addTab(1); "WHERE "
     Print #fileNo, addTab(2); "P.PS_OID = psOid_in"
     Print #fileNo, addTab(1); ";"
     Print #fileNo,
   End If

   genSpLogProcEnter fileNo, qualProcName, ddlType, , "psOid_in", "orgOid_in", "rowCount_out"

   genDb2RegVarCheckDdl fileNo, ddlType, dstOrgIndex, dstPoolIndex, tvNull, 1

   Print #fileNo,
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   ' handle INSERTs
   genProcSectionHeader fileNo, "handle INSERTs (ignore INSERTs for already existing records)"

   'rs40
   If Not condenseData Then
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); gc_tempTabNameChangeLogOrgSummary; " MCLS"
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); "MCLS.isCreated = "; gc_dbFalse
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "MCLS.isCreated = "; gc_dbTrue
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "MCLS.entityId IN ("; subClassIdStrList; ")"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); g_anOid
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTargetRefTabName; " REF"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "REF."; g_anOid; " = MCLS.objectId"
     Print #fileNo, addTab(2); ");"
     Print #fileNo,
   End If

   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTargetViewName
   Print #fileNo, addTab(1); "("
 
   initAttributeTransformation transformation, 3
   setAttributeMapping transformation, 1, conCreateUserName, ""
   setAttributeMapping transformation, 2, conUpdateUserName, ""
   setAttributeMapping transformation, 3, conInLrt, ""
 
   If forNl Then
     genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, False, , edomListNonLrt Or edomListVirtual
   Else
     genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, , False, forGen, edomListNonLrt Or edomListVirtual
   End If
 
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"

   If UCase(acmEntityName) = "GENERICASPECT" And Not forNl Then
     initAttributeTransformation transformation, 7, , , , "E."
     setAttributeMapping transformation, 6, conComment, "CAST(NULL AS VARCHAR(1))"
     setAttributeMapping transformation, 7, conIsBlockedPrice, "CASE" & vbCrLf & vbTab & vbTab & "WHEN E.CLASSID = '09006' AND v_isDpb = 0 THEN 1" & vbCrLf & vbTab & vbTab & "ELSE E.ISBLOCKEDPRICE" & vbCrLf & vbTab & "END"
   Else
     initAttributeTransformation transformation, 5, , , , "E."
   End If
   setAttributeMapping transformation, 1, conHasBeenSetProductive, gc_dbFalse
   setAttributeMapping transformation, 2, conCreateUserName, ""
   setAttributeMapping transformation, 3, conUpdateUserName, ""
   setAttributeMapping transformation, 4, conInLrt, ""
   setAttributeMapping transformation, 5, conStatusId, CStr(statusWorkInProgress)
 
   If forNl Then
     genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, False, , edomListNonLrt Or edomListVirtual
   Else
     genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, , False, forGen, edomListNonLrt Or edomListVirtual
   End If
 
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualSourceTabName; " E"
   Print #fileNo, addTab(1); "WHERE"

   Print #fileNo, addTab(2); "("

   If condenseData Then
     genProcSectionHeader fileNo, "propagate all records not found in target data pool", 3, True
     Print #fileNo, addTab(3); "NOT EXISTS ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "MPC_E."; g_anOid
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); qualTargetRefTabName; " MPC_E"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "MPC_E."; g_anOid; " = E."; g_anOid
     Print #fileNo, addTab(3); ")"
   Else
     genProcSectionHeader fileNo, "propagate inserts of records to this entity", 3, True
     Print #fileNo, addTab(3); "E."; g_anOid; " IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "MCLS.objectId"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); gc_tempTabNameChangeLogOrgSummary; " MCLS"

     If isDisAllowedCountriesAspect Then
       Print #fileNo, addTab(4); "LEFT OUTER JOIN"
       Print #fileNo, addTab(5); gc_tempTabNameChangeLogImplicitChanges; " MIC"
       Print #fileNo, addTab(4); "ON"
       Print #fileNo, addTab(5); "MCLS.ahObjectId = MIC.ahObjectId"
     End If

     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "MCLS.isCreated = "; gc_dbTrue
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MCLS.entityId IN ("; subClassIdStrList; ")"

     If isDisAllowedCountriesAspect Then
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "COALESCE(MIC.isToBeDeleted,"; gc_dbFalse; ") = "; gc_dbFalse
     End If
     Print #fileNo, addTab(3); ")"

     Print #fileNo, addTab(4); "OR"
     If isAggHead Then
       Print #fileNo, addTab(5); "E."; g_anOid; " IN ("
     Else
       Print #fileNo, addTab(5); "E."; g_anAhOid; " IN ("
     End If
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "MIC.ahObjectId"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); gc_tempTabNameChangeLogImplicitChanges; " MIC"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "MIC.isToBeCreated = "; gc_dbTrue
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MIC.aggregateType = '"; aggHeadIdStr; "'"
     'rs40
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "NOT EXISTS ("
     Print #fileNo, addTab(6); "SELECT"
     Print #fileNo, addTab(7); g_anOid
     Print #fileNo, addTab(6); "FROM"
     Print #fileNo, addTab(7); qualTargetRefTabName; " REF"
     Print #fileNo, addTab(6); "WHERE"
     Print #fileNo, addTab(7); "E."; g_anOid; " = REF."; g_anOid
     Print #fileNo, addTab(6); ")"
     Print #fileNo, addTab(3); ")"
   End If

   Print #fileNo, addTab(2); ")"

   If fkAttrToDiv <> "" Then
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "E."; fkAttrToDiv; " = divisionOid_in"
   End If

   If acmEntityIndex = g_classIndexExpression Then
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "E.ISINVALID = 0"
   End If

   If isPsTagged Then
     If Not forGen And Not forNl Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "E."; g_anPsOid; " = psOid_in"
     End If
   End If
   Print #fileNo, addTab(1); IIf(readUnCommitedInWorkDataPool, "WITH UR", ""); ";"

   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   If condenseData Then
     genProcSectionHeader fileNo, "no need to deal with UPDATEs since a """ & acmEntityName & """ is only inserted"
   ElseIf isTerm Then
     genProcSectionHeader fileNo, "no need to deal with UPDATEs since a """ & acmEntityName & """ is only inserted or deleted"
   Else
     ' handle UPDATEs
     genProcSectionHeader fileNo, "handle UPDATEs"
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); qualTargetViewName; " T"
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(1); "("

     If UCase(acmEntityName) = "GENERICASPECT" And Not forNl Then
         initAttributeTransformation transformation, numAttrsToSkip + 12 + 2
     ElseIf UCase(acmEntityName) = "EXPRESSION" Then
         initAttributeTransformation transformation, numAttrsToSkip + 12 + 2
     Else
         initAttributeTransformation transformation, numAttrsToSkip + 12 + 1
     End If
     setAttributeMapping transformation, numAttrsToSkip + 1, conOid, ""
     setAttributeMapping transformation, numAttrsToSkip + 2, conInLrt, ""
     setAttributeMapping transformation, numAttrsToSkip + 3, conCreateTimestamp, ""
     setAttributeMapping transformation, numAttrsToSkip + 4, conLastUpdateTimestamp, ""
     setAttributeMapping transformation, numAttrsToSkip + 5, conVersionId, g_anVersionId
     setAttributeMapping transformation, numAttrsToSkip + 6, conCreateUserName, ""
     setAttributeMapping transformation, numAttrsToSkip + 7, conUpdateUserName, ""
     setAttributeMapping transformation, numAttrsToSkip + 8, conIsNational, ""
     setAttributeMapping transformation, numAttrsToSkip + 9, conHasBeenSetProductive, ""
     setAttributeMapping transformation, numAttrsToSkip + 10, conHasConflict, ""
     setAttributeMapping transformation, numAttrsToSkip + 11, conIsDeleted, ""
     setAttributeMapping transformation, numAttrsToSkip + 12, conNationalDisabled, ""
     setAttributeMapping transformation, numAttrsToSkip + 13, conStatusId, g_anStatus


     Dim thisColNo As Integer
     thisColNo = 1
     For i = 1 To tabColumns.numDescriptors
         If (UCase(Right(tabColumns.descriptors(i).acmAttributeName, 8)) = "NATIONAL") Or _
            (Right(tabColumns.descriptors(i).columnName, 8) = "NATIONAL") Or _
            Right(tabColumns.descriptors(i).columnName, 12) = "_NATIONAL_ID" Or _
            Right(tabColumns.descriptors(i).columnName, 12) = "_ISNATACTIVE" Or _
            Right(tabColumns.descriptors(i).columnName, 9) = "_ISNATACT" Then
           setAttributeMapping transformation, thisColNo, tabColumns.descriptors(i).columnName, ""
           thisColNo = thisColNo + 1
           If (tabColumns.descriptors(i).columnCategory And eacExpression) <> 0 And tabColumns.descriptors(i).acmAttributeIndex > 0 Then
               If g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).isNationalizable Then
                 setAttributeMapping transformation, thisColNo, genSurrogateKeyName(ddlType, g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).shortName & "EXP", , , , True), ""
                 thisColNo = thisColNo + 1
               ElseIf g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).ftoConflictWithSrcAttrIndex > 0 Then
                 setAttributeMapping transformation, thisColNo, genSurrogateKeyName(ddlType, g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).shortName & "EXP"), ""
                 thisColNo = thisColNo + 1
               End If
           End If
         End If
         'special handling of Expression to Term reference due to codeCategoryAssignment processing - update to null value are not transferred (will be handled in setProd)
         'If .acmEntityName = "Term" And .columnName = "EXTTRM_OID" Then
         '  setAttributeMapping transformation, thisColNo, "EXTTRM_OID", "COALESCE(S.EXTTRM_OID,T.EXTTRM_OID)"
         'End If
     Next i

     If forNl Then
       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, False, , edomListNonLrt Or edomListVirtual
     Else
       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, , False, forGen, edomListNonLrt Or edomListVirtual
     End If

     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "="
     Print #fileNo, addTab(1); "("
     Print #fileNo, addTab(2); "SELECT"

     transformation.attributePrefix = "S."
     setAttributeMapping transformation, numAttrsToSkip + 5, conVersionId, "T." & g_anVersionId & " + 1"
     setAttributeMapping transformation, numAttrsToSkip + 13, conStatusId, CStr(statusWorkInProgress)
 
     'special handling of Expression to Term reference due to codeCategoryAssignment processing - update to null value are not transferred (will be handled in setProd)
     If UCase(acmEntityName) = "EXPRESSION" Then
         setAttributeMapping transformation, numAttrsToSkip + 14, "EXTTRM_OID", "COALESCE(S.EXTTRM_OID,T.EXTTRM_OID)"
     End If
 
     If UCase(acmEntityName) = "GENERICASPECT" And Not forNl Then
         setAttributeMapping transformation, numAttrsToSkip + 14, conIsBlockedPrice, "CASE" & vbCrLf & vbTab & vbTab & "WHEN S.CLASSID = '09006' AND v_isDpb = 1 AND v_isTakeoverBlockedPriceFlag = 1 THEN S.ISBLOCKEDPRICE" & vbCrLf & vbTab & vbTab & "ELSE T.ISBLOCKEDPRICE" & vbCrLf & vbTab & vbTab & "END"
     End If
 
 
     If forNl Then
       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, srcOrgIndex, srcPoolIndex, 3, forGen, False, , edomListNonLrt Or edomListVirtual
     Else
       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, , False, forGen, edomListNonLrt Or edomListVirtual
     End If

     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualSourceTabName; " S"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "T."; g_anOid; " = S."; g_anOid
     Print #fileNo, addTab(1); ")"
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "T."; g_anOid; " IN ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); "MCLS.objectId"
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); gc_tempTabNameChangeLogOrgSummary; " MCLS"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "MCLS.isUpdated = "; gc_dbTrue
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(4); "MCLS.isCreated = "; gc_dbFalse
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(4); "MCLS.isDeleted = "; gc_dbFalse
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(4); "MCLS.aggregateType = '"; aggHeadIdStr; "'"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(4); "MCLS.entityType = '"; dbAcmEntityType; "'"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(4); "MCLS.entityId IN ("; subClassIdStrList; ")"
     Print #fileNo, addTab(2); ")"

     If fkAttrToDiv <> "" Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "T."; fkAttrToDiv; " = divisionOid_in"
     End If

     Print #fileNo, addTab(1); ";"

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"

     If forNl And hasNationalColumn Then
         genProcSectionHeader fileNo, "merge an entry created in MPC with the entry created in factory"
         Dim targetLrtTableName As String
         targetLrtTableName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, True, , forNl)
         Print #fileNo, addTab(1); "SET v_lrtId = (SELECT CURRENT CLIENT_WRKSTNNAME FROM sysibm.sysdummy1);"
         Print #fileNo,
         Print #fileNo, addTab(1); "INSERT INTO"
         Print #fileNo, addTab(2); "SESSION.NL_TEXT_OIDS(FACTORY_NL_OID, MPC_NL_OID)"
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "T.oid , S.oid"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); targetLrtTableName; " T, "; qualTargetRefTabName; " S"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "T.AHOID = S.AHOID"
         Print #fileNo, addTab(1); "AND"
         Print #fileNo, addTab(2); "T.LANGUAGE_ID = S.LANGUAGE_ID"
         Print #fileNo, addTab(1); "AND"
         Print #fileNo, addTab(2); "T.INLRT = v_lrtId"
         Print #fileNo, addTab(1); "AND"
         Print #fileNo, addTab(2); "S.OID <> T.oid"

         Dim nlColumn As String
         Dim nlColumnWithoutNational As String

         Print #fileNo, addTab(1); ";"
         genProcSectionHeader fileNo, "update central records with national values"
         Print #fileNo,
         Print #fileNo, addTab(1); "MERGE INTO"
         Print #fileNo, addTab(2); qualTargetViewName; " T"
         Print #fileNo, addTab(1); "USING"
         Print #fileNo, addTab(2); "(SELECT ST.FACTORY_NL_OID, NL.* FROM SESSION.NL_TEXT_OIDS ST, "; qualTargetRefTabName; " NL WHERE NL.OID = ST.MPC_NL_OID ) AS S"
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); "T.oid = S.FACTORY_NL_OID"
         Print #fileNo, addTab(1); "WHEN MATCHED"
         Print #fileNo, addTab(2); "THEN UPDATE"
         Print #fileNo, addTab(3); "SET "

         For i = 1 To tabColumns.numDescriptors
                 nlColumn = tabColumns.descriptors(i).columnName
                 If (Right(nlColumn, 8) = "NATIONAL") Then
                     nlColumnWithoutNational = Left(nlColumn, Len(nlColumn) - 9)
                     Print #fileNo, addTab(4); "T."; nlColumn; " = S."; nlColumn; ", "
                     Print #fileNo, addTab(4); "T."; nlColumnWithoutNational; "_ISNATACTIVE = 1, "
                 End If
         Next i
         Print #fileNo, addTab(4); "T.VERSIONID = T.VERSIONID + 1"
         Print #fileNo, addTab(1); "ELSE IGNORE;"

         genProcSectionHeader fileNo, "delete national records"
         Print #fileNo,
         Print #fileNo, addTab(1); "DELETE FROM"
         Print #fileNo, addTab(2); qualTargetViewName; " T"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "T.oid IN"
         Print #fileNo, addTab(3); "(SELECT MPC_NL_OID FROM SESSION.NL_TEXT_OIDS);"
     End If


   End If

   ' handle DELETEs
   If Not condenseData Then
     genProcSectionHeader fileNo, "handle DELETEs"
     Print #fileNo, addTab(1); "DELETE FROM"
     Print #fileNo, addTab(2); qualTargetViewName; " E"
     Print #fileNo, addTab(1); "WHERE"
     genProcSectionHeader fileNo, "propagate deletes of records to this entity", 2, True

     Print #fileNo, addTab(2); "("

     If isTerm Then
       Print #fileNo, addTab(3); "("
       Print #fileNo, addTab(4); "EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "1"
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); gc_tempTabNameChangeLogOrgSummary; " MCLS"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "MCLS.aggregateType = '"; aggHeadIdStr; "'"
       Print #fileNo, addTab(7); "AND"
       Print #fileNo, addTab(6); "MCLS.ahObjectId = E."; g_anAhOid
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(5); "AND"
       Print #fileNo, addTab(4); "NOT EXISTS ("
       Print #fileNo, addTab(5); "SELECT"
       Print #fileNo, addTab(6); "F_E."; g_anOid
       Print #fileNo, addTab(5); "FROM"
       Print #fileNo, addTab(6); qualSourceTabName; " F_E"
       Print #fileNo, addTab(5); "WHERE"
       Print #fileNo, addTab(6); "F_E."; g_anOid; " = E."; g_anOid
       Print #fileNo, addTab(4); ")"
       Print #fileNo, addTab(3); ")"
     Else
       Print #fileNo, addTab(3); "E."; g_anOid; " IN ("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "MCLS.objectId"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); gc_tempTabNameChangeLogOrgSummary; " MCLS"

       If isDisAllowedCountriesAspect Then
         Print #fileNo, addTab(5); "LEFT OUTER JOIN"
         Print #fileNo, addTab(6); gc_tempTabNameChangeLogImplicitChanges; " MIC"
         Print #fileNo, addTab(5); "ON"
         Print #fileNo, addTab(6); "MCLS.ahObjectId = MIC.ahObjectId"
       End If

       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "MCLS.isDeleted = "; gc_dbTrue
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "MCLS.entityType = '"; dbAcmEntityType; "'"
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "MCLS.entityId IN ("; subClassIdStrList; ")"

       If isDisAllowedCountriesAspect Then
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "COALESCE(MIC.isToBeCreated,"; gc_dbFalse; ") = "; gc_dbFalse
       End If

       Print #fileNo, addTab(3); ")"
     End If

     Print #fileNo, addTab(4); "OR"
     If isAggHead Then
       Print #fileNo, addTab(5); "E."; g_anOid; " IN ("
     Else
       Print #fileNo, addTab(5); "E."; g_anAhOid; " IN ("
     End If
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "MIC.ahObjectId"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); gc_tempTabNameChangeLogImplicitChanges; " MIC"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "MIC.isToBeDeleted = "; gc_dbTrue
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MIC.aggregateType = '"; aggHeadIdStr; "'"
     Print #fileNo, addTab(3); ")"

     If isDeletable And forGen And Not forNl Then
       Print #fileNo, addTab(4); "OR"
       Print #fileNo, addTab(3); "E."; g_anAhOid; " IN ("
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "MCLS.objectId"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); gc_tempTabNameChangeLogOrgSummary; " MCLS"
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "MCLS.isDeleted = "; gc_dbTrue
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "MCLS.entityType = '"; dbAcmEntityType; "'"
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "MCLS.entityId IN ("; subClassIdStrList; ")"
       Print #fileNo, addTab(3); ")"
     End If

     Print #fileNo, addTab(2); ")"

     If fkAttrToDiv <> "" Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "E."; fkAttrToDiv; " = divisionOid_in"
     End If

     If readUnCommitedInWorkDataPool Then
       Print #fileNo, addTab(1); "WITH UR;"
     Else
       Print #fileNo, addTab(1); ";"
     End If

     genProcSectionHeader fileNo, "count the number of affected rows"
     Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
   End If
 
   genSpLogProcExit fileNo, qualProcName, ddlType, , "psOid_in", "orgOid_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim

   If (Not forGen And Not forNl And hasOrganizationSpecificReference) Then
   End If
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
 End Sub
 
 
 Sub genFtoSupportSpsForEntitySingleObject( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   srcOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstOrgIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False _
 )
   Dim sectionName As String
   Dim acmEntityName As String
   Dim acmEntityShortName As String
   Dim entityTypeDescr As String
   Dim isUserTransactional As Boolean
   Dim isPsTagged As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim aggHeadIdStr As String
   Dim aggHeadClassIndex As Integer
   Dim dbAcmEntityType As String
   Dim attrRefs As AttrDescriptorRefs
   Dim attrRefsInclSubClasses As AttrDescriptorRefs
   Dim relRefs As RelationshipDescriptorRefs
   Dim isGenForming As Boolean
   Dim hasNoIdentity As Boolean
   Dim ignoreForChangelog As Boolean
   Dim hasNlAttributes As Boolean
   Dim hasNlAttributesInGen As Boolean
   Dim useMqtToImplementLrtForEntity As Boolean
   Dim aggHeadNavPathToOrg As NavPathFromClassToClass
   Dim subClassIdStrList As String
   Dim aggHeadSubClassIdStrList As String
   Dim hasOrganizationSpecificReference As Boolean
   Dim relRefsToOrganizationSpecificClasses As RelationshipDescriptorRefs
   Dim condenseData As Boolean
   Dim isAggHead As Boolean
   Dim isDisAllowedCountriesAspect As Boolean
   Dim isTerm As Boolean
   Dim fkAttrToDiv As String

   On Error GoTo ErrorExit

   fkAttrToDiv = ""
   subClassIdStrList = ""
 
   If acmEntityType = eactClass Then
       aggHeadNavPathToOrg = g_classes.descriptors(acmEntityIndex).navPathToOrg

       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       acmEntityName = g_classes.descriptors(acmEntityIndex).className
       acmEntityShortName = g_classes.descriptors(acmEntityIndex).shortName
       hasNlAttributes = IIf(forGen, g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses)
       hasNlAttributesInGen = g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity
       If forNl Then
         entityTypeDescr = "ACM-Class (NL-Text)"
       Else
         entityTypeDescr = "ACM-Class"
         If g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex > 0 And Not g_classes.descriptors(acmEntityIndex).isPsTagged And Not forNl Then
             fkAttrToDiv = IIf(g_classes.descriptors(acmEntityIndex).navPathToDiv.navDirection = etLeft, g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex).leftFkColName(ddlType), g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex).rightFkColName(ddlType))
         End If
       End If
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       aggHeadIdStr = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       attrRefs = g_classes.descriptors(acmEntityIndex).attrRefs
       attrRefsInclSubClasses = g_classes.descriptors(acmEntityIndex).attrRefsInclSubClasses
       relRefs = g_classes.descriptors(acmEntityIndex).relRefsRecursive
       isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       ignoreForChangelog = g_classes.descriptors(acmEntityIndex).ignoreForChangelog
       isDisAllowedCountriesAspect = False
       isTerm = (UCase(g_classes.descriptors(acmEntityIndex).className) = "TERM")
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
       hasOrganizationSpecificReference = g_classes.descriptors(acmEntityIndex).hasOrganizationSpecificReference
       relRefsToOrganizationSpecificClasses = g_classes.descriptors(acmEntityIndex).relRefsToOrganizationSpecificClasses
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex) And Not forGen And Not forNl

       subClassIdStrList = IIf(g_classes.descriptors(acmEntityIndex).isAbstract, "", "'" & g_classes.descriptors(acmEntityIndex).classIdStr & "'")
       Dim i As Integer
       For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive)
           If Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isAbstract Then
             subClassIdStrList = subClassIdStrList & IIf(subClassIdStrList = "", "", ",") & "'" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).classIdStr & "'"
           End If
       Next i
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       acmEntityName = g_relationships.descriptors(acmEntityIndex).relName
       acmEntityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       If forNl Then
         entityTypeDescr = "ACM-Relationship (NL-Text)"
       Else
         entityTypeDescr = "ACM-Relationship"
       End If

       hasNlAttributes = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0
       hasNlAttributesInGen = False
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       aggHeadIdStr = g_relationships.descriptors(acmEntityIndex).aggHeadClassIdStr
       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       dbAcmEntityType = "R"
       attrRefs = g_relationships.descriptors(acmEntityIndex).attrRefs
       attrRefsInclSubClasses = g_relationships.descriptors(acmEntityIndex).attrRefs
       relRefs.numRefs = 0
       isGenForming = False
       hasNoIdentity = False
       ignoreForChangelog = g_relationships.descriptors(acmEntityIndex).ignoreForChangelog
       subClassIdStrList = "'" & g_relationships.descriptors(acmEntityIndex).relIdStr & "'"
       isDisAllowedCountriesAspect = (UCase(g_relationships.descriptors(acmEntityIndex).relName) = "DISALLOWEDCOUNTRIESASPECT")
       isTerm = False
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
       hasOrganizationSpecificReference = g_relationships.descriptors(acmEntityIndex).hasOrganizationSpecificReference
       condenseData = False
       isAggHead = False
   Else
     Exit Sub
   End If

   If Not generateLrt Or Not isUserTransactional Then
     Exit Sub
   End If
   If ddlType = edtPdm And (srcOrgIndex < 1 Or srcPoolIndex < 1) Then
     ' LRT is only supported at 'pool-level'
     Exit Sub
   End If
 
   Dim qualSourceTabName As String
   Dim qualSourceParTabName As String
   Dim qualTargetViewName As String
   Dim qualTargetRefTabName As String

   qualSourceTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen, , , forNl)
   qualSourceParTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen)
   qualTargetRefTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, , , forNl)
   qualTargetViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, True, useMqtToImplementLrtForEntity, forNl)

   Dim qualTargetTabNamePub As String
   qualTargetTabNamePub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, False, , forNl)

   Dim qualRelTabOrg As String, relOrgEntityIdStr As String
   Dim transformation As AttributeListTransformation

   ' ####################################################################################################################
   ' #    SP for Factory Data Takeover
   ' ####################################################################################################################
 
   Dim qualProcName As String
   qualProcName = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, , , forNl, spnFactoryTakeOver)

   printSectionHeader "SP for Factory Data Takeover for """ & qualSourceTabName & """ (" & entityTypeDescr & " """ & _
     sectionName & "." & acmEntityName & """" & IIf(forGen, "(GEN)", "") & ")", fileNo

   Dim readUnCommitedInFactory As Boolean
   readUnCommitedInFactory = isPsTagged

   Dim tabColumns As EntityColumnDescriptors
   Dim aggHeadContainsIsNotPublished As Boolean
   initAttributeTransformation transformation, 0
   tabColumns = nullEntityColumnDescriptors
   aggHeadContainsIsNotPublished = False

   If aggHeadClassIndex > 0 Then
     genTransformedAttrListForEntityWithColReuse aggHeadClassIndex, eactClass, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, False, forGen, edomNone
     For i = 1 To tabColumns.numDescriptors
       If tabColumns.descriptors(i).columnName = g_anIsNotPublished Then
         aggHeadContainsIsNotPublished = True
       End If
     Next i
   End If
 
   initAttributeTransformation transformation, 0
   tabColumns = nullEntityColumnDescriptors
   If forNl Then
     genNlsTransformedAttrListForEntityWithColReUse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, , , ddlType, dstOrgIndex, dstPoolIndex, 2, forGen, False, , edomNone
   Else
     genTransformedAttrListForEntityWithColReuse acmEntityIndex, acmEntityType, transformation, tabColumns, fileNo, ddlType, dstOrgIndex, dstPoolIndex, 2, False, forGen, edomNone
   End If
 
   Dim numAttrsToSkip As Integer
   numAttrsToSkip = 0
   For i = 1 To tabColumns.numDescriptors
       If (UCase(Right(tabColumns.descriptors(i).acmAttributeName, 8)) = "NATIONAL") Or _
          (Right(tabColumns.descriptors(i).columnName, 8) = "NATIONAL") Or _
          Right(tabColumns.descriptors(i).columnName, 12) = "_NATIONAL_ID" Or _
          Right(tabColumns.descriptors(i).columnName, 12) = "_ISNATACTIVE" Or _
          Right(tabColumns.descriptors(i).columnName, 9) = "_ISNATACT" Then
         numAttrsToSkip = numAttrsToSkip + 1
         If (tabColumns.descriptors(i).columnCategory And eacExpression) <> 0 And tabColumns.descriptors(i).acmAttributeIndex > 0 Then
             If g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).isNationalizable Or g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).ftoConflictWithSrcAttrIndex > 0 Then
               numAttrsToSkip = numAttrsToSkip + 1
             End If
         End If
       End If
   Next i

   Print #fileNo,
   Print #fileNo, addTab(0); "CREATE PROCEDURE"
   Print #fileNo, addTab(1); qualProcName
   Print #fileNo, addTab(0); "("
   genProcParm fileNo, "IN", "divisionOid_in", g_dbtOid, True, "OID of the Division owning the Product Structure"
   genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the Product Structure to process"
   genProcParm fileNo, "IN", "orgOid_in", g_dbtOid, True, "OID of the Organization"
   genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by this call"
   Print #fileNo, addTab(0); ")"
   Print #fileNo, addTab(0); "RESULT SETS 0"
   Print #fileNo, addTab(0); "LANGUAGE SQL"
   Print #fileNo, addTab(0); "BEGIN"

   genProcSectionHeader fileNo, "declare variables", , True
   genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
   If forNl Then
     genVarDecl fileNo, "v_lrtId", g_dbtOid, "0"
   End If
   genSpLogDecl fileNo

   genProcSectionHeader fileNo, "declare conditions"
   genCondDecl fileNo, "notFound", "02000"
   genCondDecl fileNo, "alreadyExist", "42710"
 
   genProcSectionHeader fileNo, "declare continue handler"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR notFound"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"
   Print #fileNo, addTab(1); "DECLARE CONTINUE HANDLER FOR alreadyExist"
   Print #fileNo, addTab(1); "BEGIN"
   Print #fileNo, addTab(2); "-- just ignore"
   Print #fileNo, addTab(1); "END;"

   If aggHeadContainsIsNotPublished Then
     genDdlForTempTablesChangeLog fileNo, dstOrgIndex, dstPoolIndex, ddlType, 1
   End If
   genDdlForTempChangeLogSummary fileNo, 1, True
   If Not condenseData Then
     genDdlForTempImplicitChangeLogSummary fileNo, 1, True
   End If

   If forNl Then
   genProcSectionHeader fileNo, "temporary table for specially handled nl records"
     Print #fileNo, addTab(0); "DECLARE GLOBAL TEMPORARY TABLE"
     Print #fileNo, addTab(1); "SESSION.NL_TEXT_OIDS"
     Print #fileNo, addTab(0); "("
     Print #fileNo, addTab(1); "FACTORY_NL_OID      "; g_dbtOid; ","
     Print #fileNo, addTab(1); "MPC_NL_OID   "; g_dbtOid
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "NOT LOGGED;"
   End If

   genSpLogProcEnter fileNo, qualProcName, ddlType, , "psOid_in", "orgOid_in", "rowCount_out"

   genDb2RegVarCheckDdl fileNo, ddlType, dstOrgIndex, dstPoolIndex, tvNull, 1

   Print #fileNo,
   Print #fileNo, addTab(1); "SET rowCount_out = 0;"

   ' handle INSERTs
   genProcSectionHeader fileNo, "handle INSERTs (ignore INSERTs for already existing records)"

   'rs40
   If Not condenseData Then
     Print #fileNo, addTab(1); "UPDATE"
     Print #fileNo, addTab(2); gc_tempTabNameChangeLogOrgSummary; " MCLS"
     Print #fileNo, addTab(1); "SET"
     Print #fileNo, addTab(2); "MCLS.isCreated = "; gc_dbFalse
     Print #fileNo, addTab(1); "WHERE"
     Print #fileNo, addTab(2); "MCLS.isCreated = "; gc_dbTrue
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "MCLS.entityId IN ("; subClassIdStrList; ")"
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "EXISTS ("
     Print #fileNo, addTab(3); "SELECT"
     Print #fileNo, addTab(4); g_anOid
     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualTargetRefTabName; " REF"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "REF."; g_anOid; " = MCLS.objectId"
     Print #fileNo, addTab(2); ");"
     Print #fileNo,
   End If

   Print #fileNo, addTab(1); "INSERT INTO"
   Print #fileNo, addTab(2); qualTargetViewName
   Print #fileNo, addTab(1); "("
 
   initAttributeTransformation transformation, 3
   setAttributeMapping transformation, 1, conCreateUserName, ""
   setAttributeMapping transformation, 2, conUpdateUserName, ""
   setAttributeMapping transformation, 3, conInLrt, ""

   If forNl Then
     genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, False, , edomListNonLrt Or edomListVirtual
   Else
     genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, , False, forGen, edomListNonLrt Or edomListVirtual
   End If
 
   Print #fileNo, addTab(1); ")"
   Print #fileNo, addTab(1); "SELECT"
 
   If UCase(acmEntityName) = "GENERICASPECT" Then
     initAttributeTransformation transformation, 6, , , , "E."
     setAttributeMapping transformation, 6, conComment, "CAST(NULL AS VARCHAR(1))"
   Else
     initAttributeTransformation transformation, 5, , , , "E."
   End If
   setAttributeMapping transformation, 1, conHasBeenSetProductive, gc_dbFalse
   setAttributeMapping transformation, 2, conCreateUserName, ""
   setAttributeMapping transformation, 3, conUpdateUserName, ""
   setAttributeMapping transformation, 4, conInLrt, ""
   setAttributeMapping transformation, 5, conStatusId, CStr(statusWorkInProgress)

   If forNl Then
     genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, srcOrgIndex, srcPoolIndex, 2, forGen, False, , edomListNonLrt Or edomListVirtual
   Else
     genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 2, , False, forGen, edomListNonLrt Or edomListVirtual
   End If
 
   Print #fileNo, addTab(1); "FROM"
   Print #fileNo, addTab(2); qualSourceTabName; " E"
   Print #fileNo, addTab(1); "WHERE"

   Print #fileNo, addTab(2); "("

   If condenseData Then
     genProcSectionHeader fileNo, "propagate all records not found in target data pool", 3, True
     Print #fileNo, addTab(3); "NOT EXISTS ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "MPC_E."; g_anOid
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); qualTargetRefTabName; " MPC_E"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "MPC_E."; g_anOid; " = E."; g_anOid
     Print #fileNo, addTab(3); ")"
   Else
     genProcSectionHeader fileNo, "propagate inserts of records to this entity", 3, True

     Print #fileNo, addTab(3); "E."; g_anOid; " IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "MCLS.objectId"

     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); gc_tempTabNameChangeLogOrgSummary; " MCLS"

     If isDisAllowedCountriesAspect Then
       Print #fileNo, addTab(4); "LEFT OUTER JOIN"
       Print #fileNo, addTab(5); gc_tempTabNameChangeLogImplicitChanges; " MIC"
       Print #fileNo, addTab(4); "ON"
       Print #fileNo, addTab(5); "MCLS.ahObjectId = MIC.ahObjectId"
     End If

     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "MCLS.isCreated = "; gc_dbTrue
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MCLS.entityId IN ("; subClassIdStrList; ")"
     If isDisAllowedCountriesAspect Then
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "COALESCE(MIC.isToBeDeleted,"; gc_dbFalse; ") = "; gc_dbFalse
     End If
     Print #fileNo, addTab(3); ")"

     Print #fileNo, addTab(4); "OR"
     If isAggHead Then
       Print #fileNo, addTab(5); "E."; g_anOid; " IN ("
     Else
       Print #fileNo, addTab(5); "E."; g_anAhOid; " IN ("
     End If
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "MIC.ahObjectId"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); gc_tempTabNameChangeLogImplicitChanges; " MIC"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "MIC.isToBeCreated = "; gc_dbTrue
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MIC.aggregateType = '"; aggHeadIdStr; "'"
     'rs40
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "NOT EXISTS ("
     Print #fileNo, addTab(6); "SELECT"
     Print #fileNo, addTab(7); g_anOid
     Print #fileNo, addTab(6); "FROM"
     Print #fileNo, addTab(7); qualTargetRefTabName; " REF"
     Print #fileNo, addTab(6); "WHERE"
     Print #fileNo, addTab(7); "E."; g_anOid; " = REF."; g_anOid
     Print #fileNo, addTab(6); ")"
     Print #fileNo, addTab(3); ")"
   End If

   Print #fileNo, addTab(2); ")"

   If fkAttrToDiv <> "" Then
     Print #fileNo, addTab(3); "AND"
     Print #fileNo, addTab(2); "E."; fkAttrToDiv; " = divisionOid_in"
   End If

   If isPsTagged Then
     If Not forGen And Not forNl Then
       Print #fileNo, addTab(3); "AND"
       Print #fileNo, addTab(2); "E."; g_anPsOid; " = psOid_in"
     End If
   End If
   Print #fileNo, addTab(1); IIf(readUnCommitedInFactory, "WITH UR", ""); ";"
 
   genProcSectionHeader fileNo, "count the number of affected rows"
   Print #fileNo, addTab(1); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
   Print #fileNo, addTab(1); "SET rowCount_out = rowCount_out + v_rowCount;"
 
   If condenseData Then
     genProcSectionHeader fileNo, "no need to deal with UPDATEs since a """ & acmEntityName & """ is only inserted"
   ElseIf isTerm Then
     genProcSectionHeader fileNo, "no need to deal with UPDATEs since a """ & acmEntityName & """ is only inserted or deleted"
   Else
     ' handle UPDATEs
     genProcSectionHeader fileNo, "handle UPDATEs"
     Print #fileNo, addTab(1); "FOR oidLoop AS"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); g_anOid; " AS oidToUpdate"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); qualSourceTabName; " E"
     Print #fileNo, addTab(2); "WHERE"

     Print #fileNo, addTab(3); "E."; g_anOid; " IN ("
     Print #fileNo, addTab(4); "SELECT"
     Print #fileNo, addTab(5); "MCLS.objectId"
     Print #fileNo, addTab(4); "FROM"
     Print #fileNo, addTab(5); gc_tempTabNameChangeLogOrgSummary; " MCLS"
     Print #fileNo, addTab(4); "WHERE"
     Print #fileNo, addTab(5); "MCLS.isUpdated = "; gc_dbTrue
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MCLS.isCreated = "; gc_dbFalse
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MCLS.isDeleted = "; gc_dbFalse
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MCLS.aggregateType = '"; aggHeadIdStr; "'"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MCLS.entityType = '"; dbAcmEntityType; "'"
     Print #fileNo, addTab(6); "AND"
     Print #fileNo, addTab(5); "MCLS.entityId IN ("; subClassIdStrList; ")"
     Print #fileNo, addTab(3); ")"

     If fkAttrToDiv <> "" Then
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "E."; fkAttrToDiv; " = divisionOid_in"
     End If
     If readUnCommitedInFactory Then
       Print #fileNo, addTab(2); "WITH UR"
     End If
     Print #fileNo, addTab(1); "DO"

     Print #fileNo, addTab(2); "UPDATE"
     Print #fileNo, addTab(3); qualTargetViewName; " T"
     Print #fileNo, addTab(2); "SET"
     Print #fileNo, addTab(3); "("

     initAttributeTransformation transformation, numAttrsToSkip + 12 + 1
     setAttributeMapping transformation, numAttrsToSkip + 1, conOid, ""
     setAttributeMapping transformation, numAttrsToSkip + 2, conInLrt, ""
     setAttributeMapping transformation, numAttrsToSkip + 3, conCreateTimestamp, ""
     setAttributeMapping transformation, numAttrsToSkip + 4, conLastUpdateTimestamp, ""
     setAttributeMapping transformation, numAttrsToSkip + 5, conVersionId, g_anVersionId
     setAttributeMapping transformation, numAttrsToSkip + 6, conCreateUserName, ""
     setAttributeMapping transformation, numAttrsToSkip + 7, conUpdateUserName, ""
     setAttributeMapping transformation, numAttrsToSkip + 8, conIsNational, ""
     setAttributeMapping transformation, numAttrsToSkip + 9, conHasBeenSetProductive, ""
     setAttributeMapping transformation, numAttrsToSkip + 10, conHasConflict, ""
     setAttributeMapping transformation, numAttrsToSkip + 11, conIsDeleted, ""
     setAttributeMapping transformation, numAttrsToSkip + 12, conNationalDisabled, ""
     setAttributeMapping transformation, numAttrsToSkip + 13, conStatusId, g_anStatus

     Dim thisColNo As Integer
     thisColNo = 1
     For i = 1 To tabColumns.numDescriptors
         If (UCase(Right(tabColumns.descriptors(i).acmAttributeName, 8)) = "NATIONAL") Or _
            (Right(tabColumns.descriptors(i).columnName, 8) = "NATIONAL") Or _
            Right(tabColumns.descriptors(i).columnName, 12) = "_NATIONAL_ID" Or _
            Right(tabColumns.descriptors(i).columnName, 12) = "_ISNATACTIVE" Or _
            Right(tabColumns.descriptors(i).columnName, 9) = "_ISNATACT" Then
           setAttributeMapping transformation, thisColNo, tabColumns.descriptors(i).columnName, ""
           thisColNo = thisColNo + 1
           If (tabColumns.descriptors(i).columnCategory And eacExpression) <> 0 And tabColumns.descriptors(i).acmAttributeIndex > 0 Then
               If g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).isNationalizable Then
                 setAttributeMapping transformation, thisColNo, genSurrogateKeyName(ddlType, g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).shortName & "EXP", , , , True), ""
                 thisColNo = thisColNo + 1
               ElseIf g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).ftoConflictWithSrcAttrIndex > 0 Then
                 setAttributeMapping transformation, thisColNo, genSurrogateKeyName(ddlType, g_attributes.descriptors(tabColumns.descriptors(i).acmAttributeIndex).shortName & "EXP"), ""
                 thisColNo = thisColNo + 1
               End If
           End If
         End If
     Next i

     If forNl Then
       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, srcOrgIndex, srcPoolIndex, 3, forGen, False, , edomListNonLrt Or edomListVirtual
     Else
       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, , False, forGen, edomListNonLrt Or edomListVirtual
     End If

     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "="
     Print #fileNo, addTab(2); "("
     Print #fileNo, addTab(3); "SELECT"

     transformation.attributePrefix = "S."
     setAttributeMapping transformation, numAttrsToSkip + 5, conVersionId, "T." & g_anVersionId & " + 1"
     setAttributeMapping transformation, numAttrsToSkip + 13, conStatusId, CStr(statusWorkInProgress)
 
     If forNl Then
       genNlsTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, , , ddlType, srcOrgIndex, srcPoolIndex, 4, forGen, False, , edomListNonLrt Or edomListVirtual
     Else
       genTransformedAttrListForEntity acmEntityIndex, acmEntityType, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 4, , False, forGen, edomListNonLrt Or edomListVirtual
     End If

     Print #fileNo, addTab(3); "FROM"
     Print #fileNo, addTab(4); qualSourceTabName; " S"
     Print #fileNo, addTab(3); "WHERE"
     Print #fileNo, addTab(4); "T."; g_anOid; " = S."; g_anOid
     Print #fileNo, addTab(2); ")"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "T."; g_anOid; " = oidToUpdate"

     Print #fileNo, addTab(2); ";"

     genProcSectionHeader fileNo, "count the number of affected rows", 2
     Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
     Print #fileNo, addTab(1); "END FOR;"


     If forNl Then
         genProcSectionHeader fileNo, "merge an entry created in MPC with the entry created in factory"
         Print #fileNo,
         Dim targetLrtTableName As String
         targetLrtTableName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, True, , forNl)
         Print #fileNo, addTab(1); "SET v_lrtId = (SELECT CURRENT CLIENT_WRKSTNNAME FROM sysibm.sysdummy1);"

         Print #fileNo, addTab(1); "INSERT INTO"
         Print #fileNo, addTab(2); "SESSION.NL_TEXT_OIDS(FACTORY_NL_OID, MPC_NL_OID)"
         Print #fileNo, addTab(1); "SELECT"
         Print #fileNo, addTab(2); "T.oid , S.oid"
         Print #fileNo, addTab(1); "FROM"
         Print #fileNo, addTab(2); targetLrtTableName; " T, "; qualTargetRefTabName; " S"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "T.AHOID = S.AHOID"
         Print #fileNo, addTab(1); "AND"
         Print #fileNo, addTab(2); "T.LANGUAGE_ID = S.LANGUAGE_ID"
         Print #fileNo, addTab(1); "AND"
         Print #fileNo, addTab(2); "T.INLRT = v_lrtId"

         Dim nlColumn As String
         Dim nlColumnWithoutNational As String

         For i = 1 To tabColumns.numDescriptors
                 nlColumn = tabColumns.descriptors(i).columnName
                 If (Right(nlColumn, 8) = "NATIONAL") Then
                     nlColumnWithoutNational = Left(nlColumn, Len(nlColumn) - 9)
                     Print #fileNo, addTab(1); "AND "
                     Print #fileNo, addTab(2); "S."; nlColumnWithoutNational; " IS NULL "
                 End If
         Next i

         Print #fileNo, addTab(1); ";"
         genProcSectionHeader fileNo, "update central records with national values"
         Print #fileNo,
         Print #fileNo, addTab(1); "MERGE INTO"
         Print #fileNo, addTab(2); qualTargetViewName; " T"
         Print #fileNo, addTab(1); "USING"
         Print #fileNo, addTab(2); "(SELECT ST.FACTORY_NL_OID, NL.* FROM SESSION.NL_TEXT_OIDS ST, "; qualTargetRefTabName; " NL WHERE NL.OID = ST.MPC_NL_OID ) AS S"
         Print #fileNo, addTab(1); "ON"
         Print #fileNo, addTab(2); "T.oid = S.FACTORY_NL_OID"
         Print #fileNo, addTab(1); "WHEN MATCHED"
         Print #fileNo, addTab(2); "THEN UPDATE"
         Print #fileNo, addTab(3); "SET "

         For i = 1 To tabColumns.numDescriptors
                 nlColumn = tabColumns.descriptors(i).columnName
                 If (Right(nlColumn, 8) = "NATIONAL") Then
                     nlColumnWithoutNational = Left(nlColumn, Len(nlColumn) - 9)
                     Dim suffix As String
                     If (nlColumnWithoutNational = "ICODESHORTDESCRIPTION") Then
                         suffix = "_ISNATACT"
                     Else
                         suffix = "_ISNATACTIVE"
                     End If
                     Print #fileNo, addTab(4); "T."; nlColumn; " = S."; nlColumn; ", "
                     Print #fileNo, addTab(4); "T."; nlColumnWithoutNational; suffix; " = 1, "
                 End If
         Next i
         Print #fileNo, addTab(4); "T.VERSIONID = T.VERSIONID + 1"
         Print #fileNo, addTab(1); "ELSE IGNORE;"

         genProcSectionHeader fileNo, "delete national records"
         Print #fileNo,
         Print #fileNo, addTab(1); "DELETE FROM"
         Print #fileNo, addTab(2); qualTargetViewName; " T"
         Print #fileNo, addTab(1); "WHERE"
         Print #fileNo, addTab(2); "T.oid IN"
         Print #fileNo, addTab(3); "(SELECT MPC_NL_OID FROM SESSION.NL_TEXT_OIDS);"
     End If


   End If

   ' handle DELETEs
   If Not condenseData Then
     genProcSectionHeader fileNo, "handle DELETEs based on explicitly deleted objects"
     Print #fileNo, addTab(1); "FOR oidLoop AS"
     Print #fileNo, addTab(2); "SELECT"
     Print #fileNo, addTab(3); "MCLS.objectId AS oidToDelete"
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); gc_tempTabNameChangeLogOrgSummary; " MCLS"

     If isDisAllowedCountriesAspect Then
       Print #fileNo, addTab(2); "LEFT OUTER JOIN"
       Print #fileNo, addTab(3); gc_tempTabNameChangeLogImplicitChanges; " MIC"
       Print #fileNo, addTab(2); "ON"
       Print #fileNo, addTab(3); "MCLS.ahObjectId = MIC.ahObjectId"
     End If

     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "MCLS.isDeleted = "; gc_dbTrue
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "MCLS.entityType = '"; dbAcmEntityType; "'"
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "MCLS.entityId IN ("; subClassIdStrList; ")"

     If isDisAllowedCountriesAspect Then
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "COALESCE(MIC.isToBeCreated,"; gc_dbFalse; ") = "; gc_dbFalse
     End If

     Print #fileNo, addTab(1); "DO"

     Print #fileNo, addTab(2); "DELETE FROM"
     Print #fileNo, addTab(3); qualTargetViewName; " T"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "T."; g_anOid; " = oidToDelete"
     Print #fileNo, addTab(2); ";"

     genProcSectionHeader fileNo, "count the number of affected rows", 2
     Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
     Print #fileNo, addTab(1); "END FOR;"

     genProcSectionHeader fileNo, "handle DELETEs based on implicitly deleted aggregate heads"
     Print #fileNo, addTab(1); "FOR oidLoop AS"
     Print #fileNo, addTab(2); "SELECT"

     If isAggHead Then
       Print #fileNo, addTab(3); "MIC.ahObjectId AS oidToDelete"
     Else
       Print #fileNo, addTab(3); "MIC.ahObjectId AS ahOidToDelete"
     End If
     Print #fileNo, addTab(2); "FROM"
     Print #fileNo, addTab(3); gc_tempTabNameChangeLogImplicitChanges; " MIC"
     Print #fileNo, addTab(2); "WHERE"
     Print #fileNo, addTab(3); "MIC.isToBeDeleted = "; gc_dbTrue
     Print #fileNo, addTab(4); "AND"
     Print #fileNo, addTab(3); "MIC.aggregateType = '"; aggHeadIdStr; "'"

     Print #fileNo, addTab(1); "DO"

     Print #fileNo, addTab(2); "DELETE FROM"
     Print #fileNo, addTab(3); qualTargetViewName; " T"
     Print #fileNo, addTab(2); "WHERE"
     If isAggHead Then
       Print #fileNo, addTab(3); "T."; g_anOid; " = oidToDelete"
     Else
       Print #fileNo, addTab(3); "T."; g_anAhOid; " = ahOidToDelete"
     End If
     Print #fileNo, addTab(2); ";"

     genProcSectionHeader fileNo, "count the number of affected rows", 2
     Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
     Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
     Print #fileNo, addTab(1); "END FOR;"
   End If
 
   genSpLogProcExit fileNo, qualProcName, ddlType, , "psOid_in", "orgOid_in", "rowCount_out"

   Print #fileNo, addTab(0); "END"
   Print #fileNo, addTab(0); gc_sqlCmdDelim
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
 End Sub
 
 
 Sub genFtoPostProcSupportSpsForEntity( _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   srcOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstOrgIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False, _
   Optional forNl As Boolean = False _
 )
   Dim sectionName As String
   Dim acmEntityName As String
   Dim acmEntityShortName As String
   Dim dbObjShortName As String
   Dim entityTypeDescr As String
   Dim isUserTransactional As Boolean
   Dim isPsTagged As Boolean
   Dim hasOwnTable As Boolean
   Dim isCommonToOrgs As Boolean
   Dim isCommonToPools As Boolean
   Dim isAbstract As Boolean
   Dim entityIdStr As String
   Dim aggHeadIdStr As String
   Dim aggHeadClassIndex As Integer
   Dim dbAcmEntityType As String
   Dim isGenForming As Boolean
   Dim hasNoIdentity As Boolean
   Dim hasNlAttributes As Boolean
   Dim hasNlAttributesInGen As Boolean
   Dim useMqtToImplementLrtForEntity As Boolean
   Dim aggHeadNavPathToOrg As NavPathFromClassToClass
   Dim subClassIdStrList As String
   Dim aggHeadSubClassIdStrList As String
   Dim hasOrganizationSpecificReference As Boolean
   Dim relRefsToOrganizationSpecificClasses As RelationshipDescriptorRefs
   Dim condenseData As Boolean
   Dim isAggHead As Boolean
   Dim isTerm As Boolean
   Dim fkAttrToDiv As String

   On Error GoTo ErrorExit

   fkAttrToDiv = ""
   subClassIdStrList = ""
 
   If acmEntityType = eactClass Then
       aggHeadNavPathToOrg = g_classes.descriptors(acmEntityIndex).navPathToOrg
       sectionName = g_classes.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_classes.descriptors(acmEntityIndex).isUserTransactional
       acmEntityName = g_classes.descriptors(acmEntityIndex).className
       acmEntityShortName = g_classes.descriptors(acmEntityIndex).shortName
       hasNlAttributes = IIf(forGen, g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(acmEntityIndex).hasNlAttrsInNonGenInclSubClasses)
       hasNlAttributesInGen = g_classes.descriptors(acmEntityIndex).hasNlAttrsInGenInclSubClasses And Not g_classes.descriptors(acmEntityIndex).hasNoIdentity
       If forNl Then
         dbObjShortName = genNlObjShortName(g_classes.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Class (NL-Text)"
       Else
         dbObjShortName = g_classes.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Class"
         If g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex > 0 And Not g_classes.descriptors(acmEntityIndex).isPsTagged And Not forNl Then
             fkAttrToDiv = IIf(g_classes.descriptors(acmEntityIndex).navPathToDiv.navDirection = etLeft, g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex).leftFkColName(ddlType), g_relationships.descriptors(g_classes.descriptors(acmEntityIndex).navPathToDiv.relRefIndex).rightFkColName(ddlType))
         End If
       End If
       isPsTagged = g_classes.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = g_classes.descriptors(acmEntityIndex).hasOwnTable
       isCommonToOrgs = g_classes.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_classes.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = g_classes.descriptors(acmEntityIndex).isAbstract
       entityIdStr = g_classes.descriptors(acmEntityIndex).classIdStr
       aggHeadIdStr = g_classes.descriptors(acmEntityIndex).aggHeadClassIdStr
       aggHeadClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex
       dbAcmEntityType = gc_acmEntityTypeKeyClass
       isGenForming = g_classes.descriptors(acmEntityIndex).isGenForming
       hasNoIdentity = g_classes.descriptors(acmEntityIndex).hasNoIdentity
       isTerm = (UCase(g_classes.descriptors(acmEntityIndex).className) = "TERM")
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_classes.descriptors(acmEntityIndex).useMqtToImplementLrt
       hasOrganizationSpecificReference = g_classes.descriptors(acmEntityIndex).hasOrganizationSpecificReference
       relRefsToOrganizationSpecificClasses = g_classes.descriptors(acmEntityIndex).relRefsToOrganizationSpecificClasses
       condenseData = g_classes.descriptors(acmEntityIndex).condenseData
       isAggHead = (g_classes.descriptors(acmEntityIndex).aggHeadClassIndex > 0) And (g_classes.descriptors(acmEntityIndex).orMappingSuperClassIndex = g_classes.descriptors(acmEntityIndex).aggHeadClassIndex) And Not forGen And Not forNl

       subClassIdStrList = IIf(g_classes.descriptors(acmEntityIndex).isAbstract, "", "'" & g_classes.descriptors(acmEntityIndex).classIdStr & "'")
       Dim i As Integer
       For i = 1 To UBound(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive)
           If Not g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).isAbstract Then
             subClassIdStrList = subClassIdStrList & IIf(subClassIdStrList = "", "", ",") & "'" & g_classes.descriptors(g_classes.descriptors(acmEntityIndex).subclassIndexesRecursive(i)).classIdStr & "'"
           End If
       Next i
   ElseIf acmEntityType = eactRelationship Then
       sectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       isUserTransactional = g_relationships.descriptors(acmEntityIndex).isUserTransactional
       acmEntityName = g_relationships.descriptors(acmEntityIndex).relName
       acmEntityShortName = g_relationships.descriptors(acmEntityIndex).shortName
       If forNl Then
         dbObjShortName = genNlObjShortName(g_relationships.descriptors(acmEntityIndex).shortName, , forGen, True)
         entityTypeDescr = "ACM-Relationship (NL-Text)"
       Else
         dbObjShortName = g_relationships.descriptors(acmEntityIndex).shortName
         entityTypeDescr = "ACM-Relationship"
       End If

       hasNlAttributes = g_relationships.descriptors(acmEntityIndex).nlAttrRefs.numDescriptors > 0
       hasNlAttributesInGen = False
       isPsTagged = g_relationships.descriptors(acmEntityIndex).isPsTagged
       hasOwnTable = True
       isCommonToOrgs = g_relationships.descriptors(acmEntityIndex).isCommonToOrgs
       isCommonToPools = g_relationships.descriptors(acmEntityIndex).isCommonToPools
       isAbstract = False
       entityIdStr = g_relationships.descriptors(acmEntityIndex).relIdStr
       aggHeadIdStr = g_relationships.descriptors(acmEntityIndex).aggHeadClassIdStr
       aggHeadClassIndex = g_relationships.descriptors(acmEntityIndex).aggHeadClassIndex
       dbAcmEntityType = "R"
       isGenForming = False
       hasNoIdentity = False
       subClassIdStrList = "'" & g_relationships.descriptors(acmEntityIndex).relIdStr & "'"
       isTerm = False
       useMqtToImplementLrtForEntity = useMqtToImplementLrt And g_relationships.descriptors(acmEntityIndex).useMqtToImplementLrt
       hasOrganizationSpecificReference = g_relationships.descriptors(acmEntityIndex).hasOrganizationSpecificReference
       condenseData = False
       isAggHead = False
   Else
     Exit Sub
   End If

   Dim qualSourceTabName As String
   Dim qualSourceParTabName As String
   Dim qualTargetRefTabName As String
   Dim qualTargetViewName As String

   qualSourceTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen, , , forNl)
   qualSourceParTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, srcOrgIndex, srcPoolIndex, forGen)
   qualTargetRefTabName = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, , , forNl)
   qualTargetViewName = genQualViewNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, True, useMqtToImplementLrtForEntity, forNl)

   Dim qualTabNamePricePreferences As String
   qualTabNamePricePreferences = genQualTabNameByClassIndex(g_classIndexPricePreferences, ddlType, dstOrgIndex, dstPoolIndex)

   If Not generateLrt Or Not isUserTransactional Then
     Exit Sub
   End If
   If ddlType = edtPdm And (srcOrgIndex < 1 Or srcPoolIndex < 1) Then
     ' LRT is only supported at 'pool-level'
     Exit Sub
   End If

   Dim transformation As AttributeListTransformation
   Dim qualRelTabOrg As String, relOrgEntityIdStr As String
 
   If (Not forGen And Not forNl And hasOrganizationSpecificReference) Then
     ' ####################################################################################################################
     ' #    SP for Factory Data Takeover Post-Processing
     ' ####################################################################################################################
     Dim qualProcNameFtoPostProc As String
     qualProcNameFtoPostProc = genQualProcNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, forGen, , , forNl, spnFtoPostProc)

     printSectionHeader "SP for Factory Data Takeover Post-Processing for """ & qualSourceTabName & """ (" & entityTypeDescr & " """ & _
       sectionName & "." & acmEntityName & """" & IIf(forGen, "(GEN)", "") & ")", fileNo

     Print #fileNo,
     Print #fileNo, addTab(0); "CREATE PROCEDURE"
     Print #fileNo, addTab(1); qualProcNameFtoPostProc
     Print #fileNo, addTab(0); "("
     genProcParm fileNo, "IN", "lrtOid_in", g_dbtOid, True, "OID of Factory-Takeover LRT"
     genProcParm fileNo, "IN", "psOid_in", g_dbtOid, True, "OID of the ProductStructure corresponding to the LRT"
     genProcParm fileNo, "IN", "divisionOid_in", g_dbtOid, True, "OID of the Division corresponding to the LRT"
     genProcParm fileNo, "IN", "opType_in", g_dbtEnumId, True, "if '1' post-process INSERT, if set to '3' post-process DELETE"
     genProcParm fileNo, "OUT", "rowCount_out", "INTEGER", False, "number of rows affected by this call"
     Print #fileNo, addTab(0); ")"
     Print #fileNo, addTab(0); "RESULT SETS 0"
     Print #fileNo, addTab(0); "LANGUAGE SQL"
     Print #fileNo, addTab(0); "BEGIN"
 
     genProcSectionHeader fileNo, "declare variables", , True
     genVarDecl fileNo, "v_rowCount", "INTEGER", "0"
     If UCase(acmEntityName) = UCase(rnCodeCategory) Then
         genSigMsgVarDecl fileNo
         genVarDecl fileNo, "v_catOid", "BIGINT", "NULL"
     End If
     If isAggHead Or isTerm Then
       genVarDecl fileNo, "v_stmntText", "VARCHAR(200)", "NULL"
     End If
     genSpLogDecl fileNo
 
     genSpLogProcEnter fileNo, qualProcNameFtoPostProc, ddlType, , "lrtOid_in", "rowCount_out"
 
     Dim qualTargetTabNamePriv As String
     qualTargetTabNamePriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, , True)
 
     Dim qualTargetTabNamePub As String
     qualTargetTabNamePub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, , False)

     If isAggHead Or isTerm Then
       genDdlForTempOids fileNo, 1, True
     End If

     genProcSectionHeader fileNo, "initialize output parameter"
     Print #fileNo, addTab(1); "SET rowCount_out  = 0;"
 
     Dim qualTargetRefTabNamePriv As String
     Dim qualTargetRefTabNamePub As String
     Dim fkAttrName As String
     Dim tabClassIndex As Integer
     Dim isFirstRel As Boolean
     Dim isFirstLoop As Boolean
     Dim start As Integer
     Dim ende As Integer

     Print #fileNo,
     Print #fileNo, addTab(1); "IF ( opType_in = "; CStr(lrtStatusDeleted); " ) THEN"
 
     If isAggHead Or isTerm Then
       genProcSectionHeader fileNo, "determine records with references to organization-specific records in other tables", 2, True
       Print #fileNo, addTab(2); "INSERT INTO"
       Print #fileNo, addTab(3); tempTabNameOids
       Print #fileNo, addTab(2); "("
       Print #fileNo, addTab(3); "oid"
       Print #fileNo, addTab(2); ")"
       Print #fileNo, addTab(2); "SELECT"
       If isAggHead Then
         Print #fileNo, addTab(3); UCase(dbObjShortName); "."; g_anOid
       Else
         Print #fileNo, addTab(3); UCase(dbObjShortName); "."; g_anAhOid
       End If
       Print #fileNo, addTab(2); "FROM"
       Print #fileNo, addTab(3); qualTargetTabNamePriv; " "; UCase(dbObjShortName)
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); UCase(dbObjShortName); "."; g_anInLrt; " = lrtOid_in"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); UCase(dbObjShortName); "."; g_anLrtState; " IN ("; CStr(lrtStatusCreated); ","; CStr(lrtStatusUpdated); ")"
       Print #fileNo, addTab(4); "AND"

       If fkAttrToDiv <> "" Then
         Print #fileNo, addTab(3); fkAttrToDiv; " = divisionOid_in"
         Print #fileNo, addTab(4); "AND"
       End If

       If isPsTagged Then
         Print #fileNo, addTab(3); g_anPsOid; " = psOid_in"
         Print #fileNo, addTab(4); "AND"
       End If

       Print #fileNo, addTab(3); "("

       If acmEntityType = eactClass Then
         isFirstRel = True
         For i = 1 To relRefsToOrganizationSpecificClasses.numRefs
               If g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).reusedRelIndex > 0 Then
                 GoTo nextRelRef
               End If

               If relRefsToOrganizationSpecificClasses.refs(i).refType = etLeft Then
                   tabClassIndex = IIf(g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).rightEntityIndex).hasOwnTable, g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).rightEntityIndex).classIndex, g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).rightEntityIndex).orMappingSuperClassIndex)
                 fkAttrName = g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).rightFkColName(ddlType)
               Else
                   tabClassIndex = IIf(g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).leftEntityIndex).hasOwnTable, g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).leftEntityIndex).classIndex, g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).leftEntityIndex).orMappingSuperClassIndex)
                 fkAttrName = g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).leftFkColName(ddlType)
               End If
             qualTargetRefTabNamePriv = genQualTabNameByClassIndex(tabClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , True)
             qualTargetRefTabNamePub = genQualTabNameByClassIndex(tabClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , False)

             If Not isFirstRel Then
               Print #fileNo, addTab(5); "OR"
             End If
             isFirstRel = False

             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); UCase(dbObjShortName); "."; fkAttrName; " IS NOT NULL"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePriv; " PRIV"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PRIV."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anLrtState; " = "; CStr(lrtStatusCreated)
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePub; " PUB"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PUB."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             'rs32
             If Not g_classes.descriptors(tabClassIndex).condenseData And Not condenseData Then
               Print #fileNo, addTab(8); "AND"
               Print #fileNo, addTab(7); "PUB."; g_anIsDeleted; " = "; gc_dbFalse
             End If
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(4); ")"
 nextRelRef:
         Next i
       ElseIf acmEntityType = eactRelationship And g_relationships.descriptors(acmEntityIndex).reusedRelIndex <= 0 Then
 
           isFirstLoop = True
 
           start = IIf(g_relationships.descriptors(acmEntityIndex).leftClassIsOrganizationSpecific And g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).isUserTransactional, 1, 2)
           ende = IIf(g_relationships.descriptors(acmEntityIndex).rightClassIsOrganizationSpecific And g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).isUserTransactional, 2, 1)
           For i = start To ende
             ' left class is organization specific
             If i = 1 Then
                 qualTargetRefTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , True)
                 qualTargetRefTabNamePub = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , False)
                 fkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).shortName)
             Else
                 qualTargetRefTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , True)
                 qualTargetRefTabNamePub = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , False)
                 fkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).shortName)
             End If

             If Not isFirstLoop Then
               Print #fileNo, addTab(5); "OR"
             End If
             isFirstLoop = False

             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePriv; " PRIV"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PRIV."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anLrtState; " = "; CStr(lrtStatusCreated)
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePub; " PUB"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PUB."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PUB."; g_anIsDeleted; " = "; gc_dbFalse
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(4); ")"
           Next i
       End If

       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(2); ";"

       genProcSectionHeader fileNo, "count the number of affected rows", 2
       Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
       Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
     End If

     If isAggHead Or isTerm Then
       genProcSectionHeader fileNo, "if some record is to be deleted, delete it and all records related to this aggregate", 2
       Print #fileNo, addTab(2); "IF v_rowCount > 0 THEN"

       Print #fileNo, addTab(3); "FOR tabLoop AS"
       Print #fileNo, addTab(4); "SELECT"
       Print #fileNo, addTab(5); "P."; g_anPdmFkSchemaName; " AS TABSCHEMA,"
       Print #fileNo, addTab(5); "P."; g_anPdmTableName; " AS TABNAME"
       Print #fileNo, addTab(4); "FROM"
       Print #fileNo, addTab(5); g_qualTabNameAcmEntity; " A"
       Print #fileNo, addTab(4); "INNER JOIN"
       Print #fileNo, addTab(5); g_qualTabNameLdmTable; " L"
       Print #fileNo, addTab(4); "ON"
       Print #fileNo, addTab(5); "A."; g_anAcmEntitySection; " = L."; g_anAcmEntitySection
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "A."; g_anAcmEntityName; " = L."; g_anAcmEntityName
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "A."; g_anAcmEntityType; " = L."; g_anAcmEntityType
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "A."; g_anAcmIsLrt; " = "; gc_dbTrue
       Print #fileNo, addTab(4); "INNER JOIN"
       Print #fileNo, addTab(5); g_qualTabNamePdmTable; " P"
       Print #fileNo, addTab(4); "ON"
       Print #fileNo, addTab(5); "P."; g_anPdmLdmFkSchemaName; " = L."; g_anLdmSchemaName
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "P."; g_anPdmLdmFkTableName; " = L."; g_anLdmTableName
       Print #fileNo, addTab(4); "WHERE"
       Print #fileNo, addTab(5); "L."; g_anLdmIsLrt; " = "; gc_dbTrue
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "L."; g_anLdmIsMqt; " = "; gc_dbFalse
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "P."; g_anOrganizationId; " = "; genOrgId(dstOrgIndex, ddlType, True)
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "P."; g_anPoolTypeId; " = "; genPoolId(dstPoolIndex, ddlType)
       Print #fileNo, addTab(6); "AND"
       Print #fileNo, addTab(5); "A."; g_anAhCid; " = '"; aggHeadIdStr; "'"
       Print #fileNo, addTab(4); "WITH UR"
       Print #fileNo, addTab(3); "DO"

       genProcSectionHeader fileNo, "delete dependent aggregate elements", 4, True

       Print #fileNo, addTab(4); "SET v_stmntText = 'DELETE FROM ' || TABSCHEMA || '.' || TABNAME || ' AE WHERE EXISTS (SELECT 1 FROM ' ||"
       Print #fileNo, addTab(13); "'"; tempTabNameOids; " O WHERE AE."; g_anAhOid; " = O."; g_anOid; ") AND AE."; g_anInLrt; " = ' || COALESCE(RTRIM(CHAR(lrtOid_in)), '-1') || ' WITH UR';"
       Print #fileNo, addTab(4); "EXECUTE IMMEDIATE v_stmntText;"

       genProcSectionHeader fileNo, "count the number of affected rows", 4
       Print #fileNo, addTab(4); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
       Print #fileNo, addTab(4); "SET rowCount_out = rowCount_out + v_rowCount;"

       Print #fileNo, addTab(3); "END FOR;"
       Print #fileNo, addTab(2); "END IF;"

       If UCase(acmEntityName) = UCase(clnGenericAspect) Then
         genProcSectionHeader fileNo, "CCPCCP_OID reference set NULL, if the central record has been deleted", 4

         Print #fileNo, addTab(2); "IF EXISTS ( SELECT "; g_anOid; " FROM "; qualTabNamePricePreferences; " WHERE "; g_anPsOid; " = psOid_in AND isconflictdetermforprices = 0 ) THEN"
         Print #fileNo, addTab(0); ""
         Print #fileNo, addTab(3); "UPDATE"
         Print #fileNo, addTab(4); qualTargetViewName; " AS gas"
         Print #fileNo, addTab(3); "SET"
         Print #fileNo, addTab(4); "gas.ccpccp_oid = NULL"
         Print #fileNo, addTab(3); "WHERE"
         Print #fileNo, addTab(3); "gas.ccpccp_oid IS NOT NULL"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "gas."; g_anIsNational; " = "; gc_dbTrue
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "gas.classid IN ( '09031', '09033')"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "gas.ps_oid = psOid_in"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "EXISTS"
         Print #fileNo, addTab(4); "("
         Print #fileNo, addTab(5); "SELECT"
         Print #fileNo, addTab(6); "'1'"
         Print #fileNo, addTab(5); "FROM"
         Print #fileNo, addTab(6); qualTargetTabNamePriv; " AS gas_l"
         Print #fileNo, addTab(5); "WHERE"
         Print #fileNo, addTab(6); "gas.ccpccp_oid = gas_l.oid"
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "gas_l."; g_anIsNational; " = "; gc_dbFalse
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "gas_l.classid IN ( '09031', '09033')"
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "gas_l.ps_oid = psOid_in"
         Print #fileNo, addTab(7); "AND"
         Print #fileNo, addTab(6); "gas_l.lrtstate = " & CStr(lrtStatusDeleted)
         Print #fileNo, addTab(4); ")"
         Print #fileNo, addTab(3); ";"
 
         genProcSectionHeader fileNo, "count the number of affected rows", 3
         Print #fileNo, addTab(3); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
         Print #fileNo, addTab(3); "SET rowCount_out = rowCount_out + v_rowCount;"
         Print #fileNo,

         Print #fileNo, addTab(2); "END IF;"
         Print #fileNo,

       End If
 
     Else

         If UCase(acmEntityName) = UCase(rnCodeCategory) Then
              genProcSectionHeader fileNo, "don't update records with references to organization-specific records in other tables", 2, Not (isAggHead Or isTerm)
                     qualTargetRefTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , True)
                     qualTargetRefTabNamePub = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , False)
                     fkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).shortName)

             Print #fileNo, addTab(2); "SELECT "; fkAttrName
             Print #fileNo, addTab(2); "INTO v_CatOid"
             Print #fileNo, addTab(2); "FROM"
             Print #fileNo, addTab(3); qualTargetTabNamePriv; " "; UCase(dbObjShortName)
             Print #fileNo, addTab(2); "WHERE"
             Print #fileNo, addTab(3); UCase(dbObjShortName); "."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); UCase(dbObjShortName); "."; g_anLrtState; " IN ("; CStr(lrtStatusUpdated); ")"
             Print #fileNo, addTab(4); "AND"

             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePriv; " PRIV"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PRIV."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anLrtState; " = "; CStr(lrtStatusCreated)
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePub; " PUB"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PUB."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PUB."; g_anIsDeleted; " = "; gc_dbFalse
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(4); ")"

             Print #fileNo, addTab(2); "FETCH FIRST 1 ROW ONLY;"

             Print #fileNo,
             Print #fileNo, addTab(2); "IF v_CatOid IS NOT NULL THEN"
             Print #fileNo, addTab(3); "SET v_msg = RTRIM(LEFT('[MDS]:  CodeCategory with invalid category ''' || RTRIM(CHAR(v_CatOid)) || ''' for this MPC',70));"
             Print #fileNo, addTab(3); "SIGNAL SQLSTATE '79030' SET MESSAGE_TEXT = v_msg;"
             Print #fileNo, addTab(2); "END IF;"
             Print #fileNo,
           End If


       genProcSectionHeader fileNo, "ignore records with references to organization-specific records in other tables", 2, Not (isAggHead Or isTerm)

       Print #fileNo, addTab(2); "DELETE FROM"
       Print #fileNo, addTab(3); qualTargetTabNamePriv; " "; UCase(dbObjShortName)
       Print #fileNo, addTab(2); "WHERE"
       Print #fileNo, addTab(3); UCase(dbObjShortName); "."; g_anInLrt; " = lrtOid_in"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); UCase(dbObjShortName); "."; g_anLrtState; " IN ("; CStr(lrtStatusCreated); ","; CStr(lrtStatusUpdated); ")"
       Print #fileNo, addTab(4); "AND"
       Print #fileNo, addTab(3); "("

       If acmEntityType = eactClass Then
         isFirstRel = True
         For i = 1 To relRefsToOrganizationSpecificClasses.numRefs
               If g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).reusedRelIndex > 0 Then
                 GoTo nextRelRef2
               End If

               If relRefsToOrganizationSpecificClasses.refs(i).refType = etLeft Then
                   tabClassIndex = IIf(g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).rightEntityIndex).hasOwnTable, g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).rightEntityIndex).classIndex, g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).rightEntityIndex).orMappingSuperClassIndex)
                 fkAttrName = g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).rightFkColName(ddlType)
               Else
                   tabClassIndex = IIf(g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).leftEntityIndex).hasOwnTable, g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).leftEntityIndex).classIndex, g_classes.descriptors(g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).leftEntityIndex).orMappingSuperClassIndex)
                 fkAttrName = g_relationships.descriptors(relRefsToOrganizationSpecificClasses.refs(i).refIndex).leftFkColName(ddlType)
               End If
             qualTargetRefTabNamePriv = genQualTabNameByClassIndex(tabClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , True)
             qualTargetRefTabNamePub = genQualTabNameByClassIndex(tabClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , False)

             If Not isFirstRel Then
               Print #fileNo, addTab(5); "OR"
             End If
             isFirstRel = False

             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); UCase(dbObjShortName); "."; fkAttrName; " IS NOT NULL"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePriv; " PRIV"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PRIV."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anLrtState; " = "; CStr(lrtStatusCreated)
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePub; " PUB"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PUB."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             If Not condenseData Then
               Print #fileNo, addTab(8); "AND"
               Print #fileNo, addTab(7); "PUB."; g_anIsDeleted; " = "; gc_dbFalse
             End If
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(4); ")"
 nextRelRef2:
         Next i
       ElseIf acmEntityType = eactRelationship And g_relationships.descriptors(acmEntityIndex).reusedRelIndex <= 0 Then
 
           isFirstLoop = True
 
           start = IIf(g_relationships.descriptors(acmEntityIndex).leftClassIsOrganizationSpecific And g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).isUserTransactional, 1, 2)
           ende = IIf(g_relationships.descriptors(acmEntityIndex).rightClassIsOrganizationSpecific And g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).isUserTransactional, 2, 1)
           For i = start To ende
             ' left class is organization specific
             If i = 1 Then
                 qualTargetRefTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , True)
                 qualTargetRefTabNamePub = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , False)
                 fkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).shortName)
             Else
                 qualTargetRefTabNamePriv = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , True)
                 qualTargetRefTabNamePub = genQualTabNameByClassIndex(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex, ddlType, dstOrgIndex, dstPoolIndex, , False)
                 fkAttrName = genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).shortName)
             End If

             If Not isFirstLoop Then
               Print #fileNo, addTab(5); "OR"
             End If
             isFirstLoop = False

             Print #fileNo, addTab(4); "("
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePriv; " PRIV"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PRIV."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anLrtState; " = "; CStr(lrtStatusCreated)
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PRIV."; g_anInLrt; " = lrtOid_in"
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(6); "AND"
             Print #fileNo, addTab(5); "NOT EXISTS ("
             Print #fileNo, addTab(6); "SELECT"
             Print #fileNo, addTab(7); "1"
             Print #fileNo, addTab(6); "FROM"
             Print #fileNo, addTab(7); qualTargetRefTabNamePub; " PUB"
             Print #fileNo, addTab(6); "WHERE"
             Print #fileNo, addTab(7); "PUB."; g_anOid; " = "; UCase(dbObjShortName); "."; fkAttrName
             Print #fileNo, addTab(8); "AND"
             Print #fileNo, addTab(7); "PUB."; g_anIsDeleted; " = "; gc_dbFalse
             Print #fileNo, addTab(5); ")"
             Print #fileNo, addTab(4); ")"
           Next i
       End If

       Print #fileNo, addTab(3); ")"
       Print #fileNo, addTab(2); ";"

       genProcSectionHeader fileNo, "count the number of affected rows", 2
       Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
       Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
 
       If isGenForming And Not hasNoIdentity Then
 '        Dim qualTargetTabNamePub As String
         qualTargetTabNamePub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, False, False)
         Dim qualTargetTabNameGenPriv As String
         qualTargetTabNameGenPriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, True, True)

         fkAttrName = genSurrogateKeyName(ddlType, acmEntityShortName)

         genProcSectionHeader fileNo, "ignore records in GEN-table with references to organization-specific records in parent table", 2
         Print #fileNo, addTab(2); "DELETE FROM"
         Print #fileNo, addTab(3); qualTargetTabNameGenPriv; " PRIVGEN"
         Print #fileNo, addTab(2); "WHERE"
         Print #fileNo, addTab(3); "PRIVGEN."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "PRIVGEN."; g_anLrtState; " IN ("; CStr(lrtStatusCreated); ","; CStr(lrtStatusUpdated); ")"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "NOT EXISTS ("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "1"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); qualTargetTabNamePriv; " PRIVPAR"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PRIVPAR."; g_anOid; " = PRIVGEN."; fkAttrName
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "PRIVPAR."; g_anLrtState; " = "; CStr(lrtStatusCreated)
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "PRIVPAR."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(3); ")"

         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "NOT EXISTS ("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "1"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); qualTargetTabNamePub; " PUBPAR"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PUBPAR."; g_anOid; " = PRIVGEN."; fkAttrName
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "PUBPAR."; g_anIsDeleted; " = "; gc_dbFalse
         Print #fileNo, addTab(3); ")"

         Print #fileNo, addTab(2); ";"

         genProcSectionHeader fileNo, "count the number of affected rows", 1
         Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
         Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
       End If

       If (isGenForming And Not hasNoIdentity And hasNlAttributesInGen) Or (Not isGenForming And hasNlAttributes) Then
         Dim qualTargetTabNameNlPriv As String
         Dim qualTargetTabNameParPub As String
         Dim qualTargetTabNameParPriv As String

         If isGenForming And Not hasNoIdentity And hasNlAttributesInGen Then
           qualTargetTabNameNlPriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, True, True, , True)
           qualTargetTabNameParPub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, True, False)
           qualTargetTabNameParPriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, True, True)
         Else
           qualTargetTabNameNlPriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, False, True, , True)
           qualTargetTabNameParPub = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, False, False)
           qualTargetTabNameParPriv = genQualTabNameByEntityIndex(acmEntityIndex, acmEntityType, ddlType, dstOrgIndex, dstPoolIndex, False, True)
         End If

         fkAttrName = genSurrogateKeyName(ddlType, acmEntityShortName)

         genProcSectionHeader fileNo, "ignore records in NL-table with references to organization-specific records in parent table", 2
         Print #fileNo, addTab(2); "DELETE FROM"
         Print #fileNo, addTab(3); qualTargetTabNameNlPriv; " PRIVNL"
         Print #fileNo, addTab(2); "WHERE"
         Print #fileNo, addTab(3); "PRIVNL."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "PRIVNL."; g_anLrtState; " IN ("; CStr(lrtStatusCreated); ","; CStr(lrtStatusUpdated); ")"
         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "NOT EXISTS ("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "1"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); qualTargetTabNameParPriv; " PRIVPAR"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PRIVPAR."; g_anOid; " = PRIVNL."; fkAttrName
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "PRIVPAR."; g_anLrtState; " = "; CStr(lrtStatusCreated)
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "PRIVPAR."; g_anInLrt; " = lrtOid_in"
         Print #fileNo, addTab(3); ")"

         Print #fileNo, addTab(4); "AND"
         Print #fileNo, addTab(3); "NOT EXISTS ("
         Print #fileNo, addTab(4); "SELECT"
         Print #fileNo, addTab(5); "1"
         Print #fileNo, addTab(4); "FROM"
         Print #fileNo, addTab(5); qualTargetTabNameParPub; " PUBPAR"
         Print #fileNo, addTab(4); "WHERE"
         Print #fileNo, addTab(5); "PUBPAR."; g_anOid; " = PRIVNL."; fkAttrName
         Print #fileNo, addTab(6); "AND"
         Print #fileNo, addTab(5); "PUBPAR."; g_anIsDeleted; " = "; gc_dbFalse
         Print #fileNo, addTab(3); ")"

         Print #fileNo, addTab(2); ";"

         genProcSectionHeader fileNo, "count the number of affected rows", 1
         Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
         Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"
       End If
     End If

     Print #fileNo, addTab(1); "END IF;"

     If acmEntityType = eactRelationship Then
         If g_relationships.descriptors(acmEntityIndex).reusedRelIndex <= 0 And g_relationships.descriptors(acmEntityIndex).leftEntityType = eactClass And g_relationships.descriptors(acmEntityIndex).rightEntityType = eactClass Then
           Dim qualTargetTabNameLeft As String
           Dim qualTargetTabNameRight As String
           Dim fkAttrNameLeft As String
           Dim fkAttrNameRight As String
           Dim fkAttrToDivLeft As String
           Dim fkAttrToDivRight As String
           Dim leftEntityIndexPar As Integer
           Dim leftEntityIsCommonToOrgs As Boolean
 
             leftEntityIsCommonToOrgs = g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex).isCommonToOrgs
             If leftEntityIsCommonToOrgs Then
               qualTargetTabNameLeft = genQualTabNameByClassIndex(g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex).classIndex, ddlType, dstOrgIndex, dstPoolIndex, forGen, False)
             Else
               qualTargetTabNameLeft = genQualViewNameByClassIndex(g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex).classIndex, ddlType, dstOrgIndex, dstPoolIndex, False, True, True)
             End If

             If g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex).navPathToDiv.relRefIndex > 0 And Not g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex).isPsTagged Then
               If g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex).navPathToDiv.navDirection = etLeft Then
                   fkAttrToDivLeft = g_relationships.descriptors(g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex).navPathToDiv.relRefIndex).leftFkColName(ddlType)
               Else
                   fkAttrToDivLeft = g_relationships.descriptors(g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).orMappingSuperClassIndex).navPathToDiv.relRefIndex).rightFkColName(ddlType)
               End If
             End If
 
             fkAttrNameLeft = genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).leftEntityIndex).shortName)

 
 '           qualTargetTabNameRight = genQualTabNameByClassIndex(.classIndex, ddlType, dstOrgIndex, dstPoolIndex, forGen, False, , , , True)
             qualTargetTabNameRight = genQualViewNameByClassIndex(g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex).classIndex, ddlType, dstOrgIndex, dstPoolIndex, False, True, True)

             If g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex).navPathToDiv.relRefIndex > 0 And Not g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex).isPsTagged Then
               If g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex).navPathToDiv.navDirection = etLeft Then
                   fkAttrToDivRight = g_relationships.descriptors(g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex).navPathToDiv.relRefIndex).leftFkColName(ddlType)
               Else
                   fkAttrToDivRight = g_relationships.descriptors(g_classes.descriptors(g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).orMappingSuperClassIndex).navPathToDiv.relRefIndex).rightFkColName(ddlType)
               End If
             End If

             fkAttrNameRight = genSurrogateKeyName(ddlType, g_classes.descriptors(g_relationships.descriptors(acmEntityIndex).rightEntityIndex).shortName)

           Print #fileNo,
           Print #fileNo, addTab(1); "IF ( opType_in = "; CStr(lrtStatusCreated); " ) THEN"
           genProcSectionHeader fileNo, "include records with references to organization-specific records in other tables", 2, True

           Print #fileNo, addTab(2); "INSERT INTO"
           Print #fileNo, addTab(3); qualTargetViewName
           Print #fileNo, addTab(2); "("

           initAttributeTransformation transformation, 3
           setAttributeMapping transformation, 1, conCreateUserName, ""
           setAttributeMapping transformation, 2, conUpdateUserName, ""
           setAttributeMapping transformation, 3, conInLrt, ""
 
           genTransformedAttrListForEntity acmEntityIndex, eactRelationship, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, , False, False, edomListNonLrt Or edomListVirtual
 
           Print #fileNo, addTab(2); ")"
           Print #fileNo, addTab(2); "SELECT"

           initAttributeTransformation transformation, 5, , , , "SRC."
 
           setAttributeMapping transformation, 1, conHasBeenSetProductive, gc_dbFalse
           setAttributeMapping transformation, 2, conCreateUserName, ""
           setAttributeMapping transformation, 3, conUpdateUserName, ""
           setAttributeMapping transformation, 4, conInLrt, ""
           setAttributeMapping transformation, 5, conStatusId, CStr(statusWorkInProgress)
 
           genTransformedAttrListForEntity acmEntityIndex, eactRelationship, transformation, fileNo, ddlType, srcOrgIndex, srcPoolIndex, 3, , False, False, edomListNonLrt Or edomListVirtual

           Print #fileNo, addTab(2); "FROM"
           Print #fileNo, addTab(3); qualSourceTabName; " SRC"
           Print #fileNo, addTab(2); "INNER JOIN"
           Print #fileNo, addTab(3); qualTargetTabNameLeft; " LFT"
           Print #fileNo, addTab(2); "ON"
           Print #fileNo, addTab(3); "LFT."; g_anOid; " = SRC."; fkAttrNameLeft
           Print #fileNo, addTab(2); "INNER JOIN"
           Print #fileNo, addTab(3); qualTargetTabNameRight; " RGHT"
           Print #fileNo, addTab(2); "ON"
           Print #fileNo, addTab(3); "RGHT."; g_anOid; " = SRC."; fkAttrNameRight
           Print #fileNo, addTab(2); "LEFT OUTER JOIN"
           Print #fileNo, addTab(3); qualTargetTabNamePriv; " TGTPRI"
           Print #fileNo, addTab(2); "ON"
           Print #fileNo, addTab(3); "SRC."; g_anOid; " = TGTPRI."; g_anOid
           Print #fileNo, addTab(2); "LEFT OUTER JOIN"
           Print #fileNo, addTab(3); qualTargetTabNamePub; " TGTPUB"
           Print #fileNo, addTab(2); "ON"
           Print #fileNo, addTab(3); "SRC."; g_anOid; " = TGTPUB."; g_anOid

           Print #fileNo, addTab(2); "WHERE"

           If isPsTagged Then
             Print #fileNo, addTab(3); "SRC."; g_anPsOid; " = psOid_in"
             Print #fileNo, addTab(4); "AND"
           End If

           Print #fileNo, addTab(3); "TGTPRI."; g_anOid; " IS NULL"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "TGTPUB."; g_anOid; " IS NULL"
           Print #fileNo, addTab(4); "AND"
           Print #fileNo, addTab(3); "SRC."; g_anIsDeleted; " = "; gc_dbFalse

           If fkAttrToDivLeft <> "" Then
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "LFT."; fkAttrToDivLeft; " = divisionOid_in"
           End If

           If fkAttrToDivRight <> "" Then
             Print #fileNo, addTab(4); "AND"
             Print #fileNo, addTab(3); "RGHT."; fkAttrToDivRight; " = divisionOid_in"
           End If

           Print #fileNo, addTab(2); ";"

           genProcSectionHeader fileNo, "count the number of affected rows", 2
           Print #fileNo, addTab(2); "GET DIAGNOSTICS v_rowCount = ROW_COUNT;"
           Print #fileNo, addTab(2); "SET rowCount_out = rowCount_out + v_rowCount;"

           Print #fileNo, addTab(1); "END IF;"
         End If
     End If

     genSpLogProcExit fileNo, qualProcNameFtoPostProc, ddlType, , "lrtOid_in", "rowCount_out"

     Print #fileNo, addTab(0); "END"
     Print #fileNo, addTab(0); gc_sqlCmdDelim
   End If
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
 End Sub
 
 
 Sub genFtoSupportDdlForClass( _
   ByRef classIndex As Integer, _
   srcOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstOrgIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
     If g_classes.descriptors(classIndex).ftoSingleObjProcessing Then
       genFtoSupportSpsForEntitySingleObject g_classes.descriptors(classIndex).classIndex, eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen
     Else
       genFtoSupportSpsForEntity g_classes.descriptors(classIndex).classIndex, eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen
     End If

     genFtoPostProcSupportSpsForEntity g_classes.descriptors(classIndex).classIndex, eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen

     If IIf(forGen, g_classes.descriptors(classIndex).hasNlAttrsInGenInclSubClasses, g_classes.descriptors(classIndex).hasNlAttrsInNonGenInclSubClasses) Then
       If g_classes.descriptors(classIndex).ftoSingleObjProcessing Then
         genFtoSupportSpsForEntitySingleObject g_classes.descriptors(classIndex).classIndex, eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, True
       Else
         genFtoSupportSpsForEntity g_classes.descriptors(classIndex).classIndex, eactClass, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, True
       End If
     End If
 End Sub
 
 
 Sub genFtoSupportDdlForRelationship( _
   thisRelIndex As Integer, _
   srcOrgIndex As Integer, _
   srcPoolIndex As Integer, _
   dstOrgIndex As Integer, _
   dstPoolIndex As Integer, _
   fileNo As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
     If g_relationships.descriptors(thisRelIndex).ftoSingleObjProcessing Then
       genFtoSupportSpsForEntitySingleObject g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen
     Else
       genFtoSupportSpsForEntity g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen
     End If
     genFtoPostProcSupportSpsForEntity g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen

     If g_relationships.descriptors(thisRelIndex).nlAttrRefs.numDescriptors > 0 Then
       If g_relationships.descriptors(thisRelIndex).ftoSingleObjProcessing Then
         genFtoSupportSpsForEntitySingleObject g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, True
       Else
         genFtoSupportSpsForEntity g_relationships.descriptors(thisRelIndex).relIndex, eactRelationship, srcOrgIndex, srcPoolIndex, dstOrgIndex, dstPoolIndex, fileNo, ddlType, forGen, True
       End If
     End If
 End Sub
 ' ### ENDIF IVK ###
 
 
 
 
 
 
 
 
 
 
 
 
