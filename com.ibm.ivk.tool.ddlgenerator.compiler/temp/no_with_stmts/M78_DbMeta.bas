 Attribute VB_Name = "M78_DbMeta"
 Option Explicit
 
 Type LdmFk
   srcSchema As String
   srcTable As String
   dstSchema As String
   dstTable As String
   isEnforced As Boolean
 End Type
 
 Type CheckFk
   srcQualTableName As String
   dstQualTableName As String
   srcAttrSeq As String
   isEnforced As Boolean
 End Type
 
 Type LdmFks
   numFks As Integer
   fks() As LdmFk
 End Type
 
 Type CheckFks
   numFks As Integer
   fks() As CheckFk
 End Type
 
 Global g_ldmFks As LdmFks
 
 Global g_checkFks As CheckFks
 
 Private Const pdmCsvProcessingStep = 3
 
 Private fileNoCsvLdmTable As Integer
 Private fileNoCsvPdmTable As Integer
 
 
 Sub closeCsvFilesLPdmTable()
   On Error Resume Next
   Close fileNoCsvLdmTable
   Close fileNoCsvPdmTable
 
   fileNoCsvLdmTable = -1
   fileNoCsvPdmTable = -1
 End Sub
 
 
 Sub initGLdmFks()
   initLdmFks g_ldmFks
   initCheckFks g_checkFks
 End Sub
 
 
 Private Sub initLdmFks( _
   ByRef fks As LdmFks _
 )
   fks.numFks = 0
 End Sub
 
 Private Sub initCheckFks( _
   ByRef fks As CheckFks _
 )
   fks.numFks = 0
 End Sub
 
 
 Function allocLdmFkIndex( _
   ByRef fks As LdmFks _
 ) As Integer
   allocLdmFkIndex = -1

     If fks.numFks = 0 Then
       ReDim fks.fks(1 To gc_allocBlockSize)
     ElseIf fks.numFks >= UBound(fks.fks) Then
       ReDim Preserve fks.fks(1 To fks.numFks + gc_allocBlockSize)
     End If
     fks.numFks = fks.numFks + 1
     allocLdmFkIndex = fks.numFks
 End Function
 
 Function allocCheckFkIndex( _
   ByRef fks As CheckFks _
 ) As Integer
   allocCheckFkIndex = -1

     If fks.numFks = 0 Then
       ReDim fks.fks(1 To gc_allocBlockSize)
     ElseIf fks.numFks >= UBound(fks.fks) Then
       ReDim Preserve fks.fks(1 To fks.numFks + gc_allocBlockSize)
     End If
     fks.numFks = fks.numFks + 1
     allocCheckFkIndex = fks.numFks
 End Function
 
 
 Private Sub addLdmFk( _
   ByRef srcSchema As String, _
   ByRef srcTable As String, _
   ByRef dstSchema As String, _
   ByRef dstTable As String, _
   Optional ByRef isEnforced As Boolean = True _
 )
   Dim i As Integer
     For i = 1 To g_ldmFks.numFks
         If g_ldmFks.fks(i).srcSchema = srcSchema And g_ldmFks.fks(i).srcTable = srcTable And g_ldmFks.fks(i).dstSchema = dstSchema And g_ldmFks.fks(i).dstTable = dstTable Then
           Exit Sub
         End If
     Next i
       g_ldmFks.fks(allocLdmFkIndex(g_ldmFks)).srcSchema = srcSchema
       g_ldmFks.fks(allocLdmFkIndex(g_ldmFks)).srcTable = srcTable
       g_ldmFks.fks(allocLdmFkIndex(g_ldmFks)).dstSchema = dstSchema
       g_ldmFks.fks(allocLdmFkIndex(g_ldmFks)).dstTable = dstTable
       g_ldmFks.fks(allocLdmFkIndex(g_ldmFks)).isEnforced = isEnforced
 End Sub
 
 Private Sub addCheckFk( _
   ByRef srcQualTableName As String, _
   ByRef dstQualTableName As String, _
   ByRef srcAttrSeq As String, _
   Optional ByRef isEnforced As Boolean = True _
 )
   Dim i As Integer
     For i = 1 To g_checkFks.numFks
         If g_checkFks.fks(i).srcQualTableName = srcQualTableName And g_checkFks.fks(i).dstQualTableName = dstQualTableName And g_checkFks.fks(i).srcAttrSeq = srcAttrSeq Then
           Exit Sub
         End If
     Next i
       g_checkFks.fks(allocCheckFkIndex(g_checkFks)).srcQualTableName = srcQualTableName
       g_checkFks.fks(allocCheckFkIndex(g_checkFks)).dstQualTableName = dstQualTableName
       g_checkFks.fks(allocCheckFkIndex(g_checkFks)).srcAttrSeq = srcAttrSeq
       g_checkFks.fks(allocCheckFkIndex(g_checkFks)).isEnforced = isEnforced
 End Sub
 Sub registerCheckFk( _
   ByRef srcQualTableName As String, _
   ByRef dstQualTableName As String, _
   ByRef srcAttrSeq As String, _
   Optional ByRef isEnforced As Boolean = True _
 )
   addCheckFk srcQualTableName, dstQualTableName, srcAttrSeq, isEnforced
 End Sub
 
 Private Sub registerLdmFk( _
   ByRef srcSchemaName As String, _
   ByRef srcTabName As String, _
   ByRef dstSchemaName As String, _
   ByRef dstTabName As String, _
   ByRef srcAcmEntityIndex As Integer, _
   ByRef srcAcmEntityType As AcmAttrContainerType, _
   Optional dstNotAcmRelated As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional ByRef isEnforced As Boolean = True _
 )
   addLdmFk srcSchemaName, srcTabName, dstSchemaName, dstTabName, isEnforced
 End Sub
 
 
 Private Sub genLdmFksCsv( _
   Optional ddlType As DdlTypeId = edtLdm _
 )
   Dim fileNo As Integer
   Dim fileName As String
   fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnFkDependency, ldmCsvFkProcessingStep, "LDM", ddlType)
   assertDir fileName
   fileNo = FreeFile()

   On Error GoTo ErrorExit
   Open fileName For Append As #fileNo

   Dim i As Integer
   For i = 1 To g_ldmFks.numFks
       Print #fileNo, IIf(g_ldmFks.fks(i).isEnforced, gc_dbTrue, gc_dbFalse); ",";
       Print #fileNo, """"; UCase(Trim(g_ldmFks.fks(i).srcTable)); """,";
       Print #fileNo, """"; UCase(Trim(g_ldmFks.fks(i).srcSchema)); """,";
       Print #fileNo, """"; UCase(Trim(g_ldmFks.fks(i).dstTable)); """,";
       Print #fileNo, """"; UCase(Trim(g_ldmFks.fks(i).dstSchema)); """,";
       Print #fileNo, getCsvTrailer(0)
   Next i

 NormalExit:
   On Error Resume Next
   Close #fileNo
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub genLdmFksCsvs()
   If generateLdm Then
     genLdmFksCsv edtLdm
   End If
 
   If generatePdm Then
     genLdmFksCsv edtPdm
   End If
 End Sub
 
 
 Sub registerQualLdmFk( _
   ByRef qualLdmSrcTableName As String, _
   ByRef qualLdmDstTableName As String, _
   ByRef srcAcmEntityIndex As Integer, _
   ByRef srcAcmEntityType As AcmAttrContainerType, _
   Optional dstNotAcmRelated As Boolean = False, _
   Optional forGen As Boolean = False, _
   Optional isEnforced As Boolean = True _
 )
   If dstNotAcmRelated Then
     Exit Sub
   End If

   If srcAcmEntityType = eactClass Then
       If g_classes.descriptors(srcAcmEntityIndex).notAcmRelated Then
         Exit Sub
       End If
   ElseIf srcAcmEntityType = eactRelationship Then
       If g_relationships.descriptors(srcAcmEntityIndex).notAcmRelated Or g_relationships.descriptors(srcAcmEntityIndex).isNotEnforced Then
         Exit Sub
       End If
   ElseIf srcAcmEntityType = eactEnum Then
       If g_enums.descriptors(srcAcmEntityIndex).notAcmRelated Then
         Exit Sub
       End If
   End If

   Dim srcSchemaName As String
   Dim srcTabName As String
   Dim dstSchemaName As String
   Dim dstTabName As String
 
   splitQualifiedName qualLdmSrcTableName, srcSchemaName, srcTabName
   splitQualifiedName qualLdmDstTableName, dstSchemaName, dstTabName
   registerLdmFk srcSchemaName, srcTabName, dstSchemaName, dstTabName, srcAcmEntityIndex, srcAcmEntityType, dstNotAcmRelated, forGen, isEnforced
 End Sub
 
 
 Private Sub registerLdmTable( _
   ByRef clnLdmSchema As String, _
   ByRef ldmTabName As String, _
   ByRef rootAcmEntityIndex As Integer, _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional isGen As Boolean = False, _
   Optional isLrt As Boolean = False, _
   Optional isNl As Boolean = False, _
   Optional isMqt As Boolean = False _
 )
   Dim acmSectionName As String
   Dim acmEntityName As String

   If acmEntityType = eactClass Then
       If (Not isLrt And g_classes.descriptors(rootAcmEntityIndex).isLdmCsvExported) Or (isLrt And g_classes.descriptors(rootAcmEntityIndex).isLdmLrtCsvExported) Or g_classes.descriptors(rootAcmEntityIndex).notAcmRelated Then
         Exit Sub
       End If
       acmSectionName = g_classes.descriptors(acmEntityIndex).sectionName
       acmEntityName = g_classes.descriptors(acmEntityIndex).className
   ElseIf acmEntityType = eactEnum Then
       If g_enums.descriptors(rootAcmEntityIndex).isLdmCsvExported Or g_enums.descriptors(rootAcmEntityIndex).notAcmRelated Then
         Exit Sub
       End If
       acmSectionName = g_enums.descriptors(acmEntityIndex).sectionName
       acmEntityName = g_enums.descriptors(acmEntityIndex).enumName
   ElseIf acmEntityType = eactRelationship Then
       If (Not isLrt And g_relationships.descriptors(rootAcmEntityIndex).isLdmCsvExported) Or (isLrt And g_relationships.descriptors(rootAcmEntityIndex).isLdmLrtCsvExported) Or g_relationships.descriptors(rootAcmEntityIndex).notAcmRelated Then
         Exit Sub
       End If
       acmSectionName = g_relationships.descriptors(acmEntityIndex).sectionName
       acmEntityName = g_relationships.descriptors(acmEntityIndex).relName
   End If
 
   On Error GoTo ErrorExit

   If fileNoCsvLdmTable < 1 Then
     Dim fileName As String
     fileName = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnLdmTable, ldmCsvTableProcessingStep, "LDM", ddlType)
     assertDir fileName
     fileNoCsvLdmTable = FreeFile()

     Open fileName For Append As #fileNoCsvLdmTable
   End If

   Print #fileNoCsvLdmTable, """"; UCase(Trim(ldmTabName)); """,";
   Print #fileNoCsvLdmTable, ",";
   Print #fileNoCsvLdmTable, IIf(isNl, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNoCsvLdmTable, IIf(isGen, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNoCsvLdmTable, IIf(isLrt, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNoCsvLdmTable, IIf(isMqt, gc_dbTrue, gc_dbFalse); ",";
   Print #fileNoCsvLdmTable, """"; UCase(Trim(acmSectionName)); """,";
   Print #fileNoCsvLdmTable, """"; UCase(Trim(acmEntityName)); """,";
   Print #fileNoCsvLdmTable, """"; getAcmEntityTypeKey(acmEntityType); """,";
   Print #fileNoCsvLdmTable, """"; UCase(Trim(clnLdmSchema)); """,";
   Print #fileNoCsvLdmTable, getCsvTrailer(0)

 NormalExit:
   On Error Resume Next
   ' leave file open
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub splitQualifiedName( _
   ByRef qualifiedName As String, _
   ByRef qualifier As String, _
   ByRef unqualifiedName As String _
 )
   Dim elems() As String
   elems = split(qualifiedName, ".", 2)

   qualifier = ""
   unqualifiedName = ""
   If UBound(elems) = 1 Then
     qualifier = elems(0)
     unqualifiedName = elems(1)
   End If
 End Sub
 
 
 Private Sub registerPdmTable( _
   ByRef qualRefObjNamePdm As String, _
   ByRef qualRefObjNameLdm As String, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer _
 )
   Dim qualifierLdm As String
   Dim nameLdm As String
   Dim qualifierPdm As String
   Dim namePdm As String

   splitQualifiedName qualRefObjNameLdm, qualifierLdm, nameLdm
   splitQualifiedName qualRefObjNamePdm, qualifierPdm, namePdm

   If getOrgIsTemplate(thisOrgIndex) Then
     ' we do not create CSV for template Orgs
     Exit Sub
   End If

   On Error GoTo ErrorExit

   If fileNoCsvPdmTable < 1 Then
     Dim fileNameCsv As String
     fileNameCsv = genCsvFileName(g_targetDir, g_sectionIndexDbMeta, clnPdmTable, pdmCsvProcessingStep, "PDM", edtPdm, , , , , thisOrgIndex)
     assertDir fileNameCsv

     fileNoCsvPdmTable = FreeFile()
     Open fileNameCsv For Append As #fileNoCsvPdmTable
   End If

   Dim orgIdStr As String
   If thisOrgIndex <= 0 Then
     orgIdStr = ""
   Else
       If g_orgs.descriptors(thisOrgIndex).isTemplate Then
         orgIdStr = genTemplateParamWrapper(CStr(g_orgs.descriptors(thisOrgIndex).id))
       Else
         orgIdStr = CStr(g_orgs.descriptors(thisOrgIndex).id)
       End If
   End If

   Print #fileNoCsvPdmTable, """"; namePdm; """,";
   Print #fileNoCsvPdmTable, orgIdStr; ",";
   If thisPoolIndex > 0 Then
     Print #fileNoCsvPdmTable, CStr(g_pools.descriptors(thisPoolIndex).id); ",";
   Else
     Print #fileNoCsvPdmTable, ",";
   End If
   Print #fileNoCsvPdmTable, """"; nameLdm; """,";
   Print #fileNoCsvPdmTable, """"; qualifierLdm; """,";
   Print #fileNoCsvPdmTable, """"; qualifierPdm; """,";
   Print #fileNoCsvPdmTable, getCsvTrailer(0)
 
 NormalExit:
   On Error Resume Next
   ' leave file open
   Exit Sub
 
 ErrorExit:
   errMsgBox Err.description
   Resume NormalExit
 End Sub
 
 
 Sub registerQualTable( _
   ByRef qualLdmTableName As String, _
   ByRef qualPdmTableName As String, _
   ByRef rootAcmEntityIndex As Integer, _
   ByRef acmEntityIndex As Integer, _
   ByRef acmEntityType As AcmAttrContainerType, _
   ByVal thisOrgIndex As Integer, _
   ByVal thisPoolIndex As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional ByRef notAcmRelated As Boolean, _
   Optional isGen As Boolean = False, _
   Optional isLrt As Boolean = False, _
   Optional isNl As Boolean = False, _
   Optional isMqt As Boolean = False _
 )
   Dim ldmSchemaName As String
   Dim ldmTableName As String
 
   splitQualifiedName qualLdmTableName, ldmSchemaName, ldmTableName
   registerLdmTable ldmSchemaName, ldmTableName, rootAcmEntityIndex, acmEntityIndex, acmEntityType, ddlType, isGen, isLrt, isNl, isMqt

   If ddlType = edtPdm And Not notAcmRelated Then
     registerPdmTable qualPdmTableName, qualLdmTableName, thisOrgIndex, thisPoolIndex
   End If
 End Sub
 
 
 
