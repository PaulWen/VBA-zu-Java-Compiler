Attribute VB_Name = "M76_Index"
Option Explicit

Private Const colEntryFilter = 1
Private Const colSection = 2
Private Const colClassName = colSection + 1
Private Const colEntityType = colClassName + 1
Private Const colIndexName = colEntityType + 1
Private Const colShortName = colIndexName + 1
Private Const colIsUnique = colShortName + 1
Private Const colForGen = colIsUnique + 1
Private Const colSpecificToQueryTables = colForGen + 1
Private Const colSpecificToPool = colSpecificToQueryTables + 1

Private Const firstRow = 3

Private Const sheetName = "Idx"

Global g_indexes As IndexDescriptors


Private Sub readSheet()
  initIndexDescriptors g_indexes
  
  Dim thisSheet As Worksheet
  Set thisSheet = ActiveWorkbook.Worksheets(getWorkSheetName(sheetName, workSheetSuffix))
  Dim thisRow As Integer
  thisRow = firstRow + IIf(thisSheet.Cells(1, 1) = "", 0, 1)
  
  While thisSheet.Cells(thisRow, colSection) & "" <> ""
    If getIsEntityFiltered(thisSheet.Cells(thisRow, colEntryFilter)) Then
      GoTo NextRow
    End If
    
    With g_indexes.descriptors(allocIndexDescriptorIndex(g_indexes))
      .sectionName = Trim(thisSheet.Cells(thisRow, colSection))
      .className = Trim(thisSheet.Cells(thisRow, colClassName))
      .cType = getAttrContainerType(Trim(thisSheet.Cells(thisRow, colEntityType)))
      .indexName = Trim(thisSheet.Cells(thisRow, colIndexName))
      .shortName = Trim(thisSheet.Cells(thisRow, colShortName))
      .isUnique = getBoolean(thisSheet.Cells(thisRow, colIsUnique))
      .forGen = getBoolean(thisSheet.Cells(thisRow, colForGen))
      .specificToQueryTables = getBoolean(thisSheet.Cells(thisRow, colSpecificToQueryTables))
      .specificToPools = Trim(thisSheet.Cells(thisRow, colSpecificToPool))
    End With

NextRow:
    thisRow = thisRow + 1
  Wend
End Sub


Sub getIndexes()
  If (g_indexes.numDescriptors = 0) Then
    readSheet
  End If
End Sub


Sub resetIndexes()
  g_indexes.numDescriptors = 0
End Sub


' ### IF IVK ###
Sub genIndexesForEntity( _
  ByRef qualTabName As String, _
  ByRef acmEntityIndex As Integer, _
  ByRef acmEntityType As AcmAttrContainerType, _
  ByVal thisOrgIndex As Integer, _
  ByVal thisPoolIndex As Integer, _
  fileNo As Integer, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional forGen As Boolean = False, _
  Optional forLrt As Boolean = False, _
  Optional forMqt As Boolean = False, _
  Optional forNl As Boolean = False, _
  Optional noConstraints As Boolean = False, _
  Optional tabPartitionType As PartitionType = ptNone _
)
' ### ELSE IVK ###
'Sub genIndexesForEntity( _
' ByRef qualTabName As String, _
' ByRef acmEntityIndex As Integer, _
' ByRef acmEntityType As AcmAttrContainerType, _
' thisOrgIndex As Integer, _
' thisPoolIndex As Integer, _
' fileNo As Integer, _
' Optional ddlType As DdlTypeId = edtLdm, _
' Optional forGen As Boolean = False, _
' Optional forLrt As Boolean = False, _
' Optional forMqt As Boolean = False, _
' Optional forNl As Boolean = False, _
' Optional noConstraints As Boolean = False _
')
' ### ENDIF IVK ###
  On Error GoTo ErrorExit
  
  If Not genIndexesForAcmClasses Or (forLrt And Not generateIndexOnLrtTabs) Or Not generateDdlCreateIndex Then
    Exit Sub
  End If
  
  If acmEntityType = eactClass Then
    'Defect 19643 wf
    'Hier ein Aufruf für Erstelung Indexe VL6CPST011.PROPERTY_GEN_LRT_MQT
    genIndexesForClassIndex qualTabName, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forNl, forMqt, , noConstraints
      
    If Not forNl Then
      With g_classes.descriptors(acmEntityIndex)
        Dim i As Integer
        For i = 1 To UBound(.subclassIndexesRecursive)
          'Defect 19643 wf
          'Aufruf erfolgt 5 Mal fuer VL6CPST011.PROPERTY_GEN_LRT_MQT
          genIndexesForClassIndex qualTabName, .subclassIndexesRecursive(i), thisOrgIndex, thisPoolIndex, fileNo, ddlType, forGen, forLrt, forNl, forMqt, True, noConstraints
        Next i
      End With
    End If
  ElseIf acmEntityType = eactRelationship Then
    If Not forNl Then
' ### IF IVK ###
      genIndexesForRelationshipIndex qualTabName, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt, forNl, forMqt, noConstraints, tabPartitionType
' ### ELSE IVK ###
'     genIndexesForRelationshipIndex qualTabName, acmEntityIndex, thisOrgIndex, thisPoolIndex, fileNo, ddlType, forLrt, forNl, forMqt, noConstraints
' ### ENDIF IVK ###
    End If
  End If

NormalExit:
  On Error Resume Next
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub

' ### IF IVK ###
Sub genIndexesForRelationshipIndex( _
  ByRef qualTabName As String, _
  ByRef thisRelIndex As Integer, _
  ByVal thisOrgIndex As Integer, _
  ByVal thisPoolIndex As Integer, _
  fileNo As Integer, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional forLrt As Boolean = False, _
  Optional forNl As Boolean = False, _
  Optional forMqt As Boolean = False, _
  Optional noConstraints As Boolean = False, _
  Optional tabPartitionType As PartitionType = ptNone _
)
' ### ELSE IVK ###
'Sub genIndexesForRelationshipIndex( _
' ByRef qualTabName As String, _
' ByRef thisRelIndex As Integer, _
' thisOrgIndex As Integer, _
' thisPoolIndex As Integer, _
' fileNo As Integer, _
' Optional ddlType As DdlTypeId = edtLdm, _
' Optional forLrt As Boolean = False, _
' Optional forNl As Boolean = False, _
' Optional forMqt As Boolean = False, _
' Optional noConstraints As Boolean = False _
')
' ### ENDIF IVK ###
  If Not genIndexesForAcmClasses Or (forLrt And Not generateIndexOnLrtTabs) Or Not generateDdlCreateIndex Then
    Exit Sub
  End If
  
  On Error GoTo ErrorExit
  
  Dim poolSuppressUniqueConstraints As Boolean
  Dim poolSupportLrt As Boolean
  Dim poolCommonItemsLocal As Boolean
  If thisPoolIndex > 0 Then
    With g_pools.descriptors(thisPoolIndex)
      poolSuppressUniqueConstraints = .suppressUniqueConstraints
      poolSupportLrt = .supportLrt
      poolCommonItemsLocal = .commonItemsLocal
    End With
  End If
  
  Dim qualIndexName As String
  Dim colList As String
  colList = ""
  Dim colListIncluded As String
  colListIncluded = ""
  Dim ukAttrDecls As String
  Dim pkAttrList As String
  Dim leftFkAttrs As String
  Dim rightFkAttrs As String
  Dim relShortName As String
  Dim ukName As String
  
  With g_relationships.descriptors(thisRelIndex)
    Dim leftClass As ClassDescriptor, rightclass As ClassDescriptor
    leftClass = g_classes.descriptors(.leftEntityIndex)
    rightclass = g_classes.descriptors(.rightEntityIndex)
    
    Dim leftOrClass As ClassDescriptor, rightOrClass As ClassDescriptor
    leftOrClass = getOrMappingSuperClass(leftClass.sectionName, leftClass.className)
    rightOrClass = getOrMappingSuperClass(rightclass.sectionName, rightclass.className)

    relShortName = .shortName
    
    Dim numAttrs As Integer
    numAttrs = .attrRefs.numDescriptors
      
    Dim tabColumns As EntityColumnDescriptors
    tabColumns = nullEntityColumnDescriptors
  
    Dim transformation As AttributeListTransformation
    transformation = nullAttributeTransformation
    genTransformedAttrDeclsForRelationshipWithColReUse_Int thisRelIndex, transformation, tabColumns, ukAttrDecls, pkAttrList, leftFkAttrs, rightFkAttrs, fileNo, _
      ddlType, thisOrgIndex, thisPoolIndex, 1, , False, forLrt, edomNone, poolCommonItemsLocal
          
    If useSurrogateKeysForNMRelationships And (numAttrs > 0 Or .logLastChange Or .isUserTransactional) And .useSurrogateKey And Not forLrt Then
      If generateDdlCreatePK Then
        printSectionHeader "Primary Key for """ & qualTabName & """", fileNo
        Print #fileNo,
        Print #fileNo, addTab(0); "ALTER TABLE"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "ADD CONSTRAINT"
        Print #fileNo, addTab(1); genPkName(.relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex)
        Print #fileNo, addTab(0); "PRIMARY KEY ("; g_anOid; ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
        If (thisPoolIndex = 2 Or thisPoolIndex = 3) And numAttrs > 1 Then
'        If (g_pools.descriptors(thisPoolIndex).id = 1 Or g_pools.descriptors(thisPoolIndex).id = 3) And numAttrs > 1 Then
            
          Dim additionalUK As Boolean
          If .maxRightCardinality = -1 And g_classes.descriptors(.leftEntityIndex).isPsTagged = True Then
            additionalUK = True
          End If
          If .maxLeftCardinality = -1 And g_classes.descriptors(.rightEntityIndex).isPsTagged = True Then
            additionalUK = True
          End If
          If .minLeftCardinality = 1 And _
             .maxLeftCardinality = 1 And _
             .minRightCardinality = 1 And _
             .maxRightCardinality = 1 And _
             g_classes.descriptors(.leftEntityIndex).isPsTagged = True Then
            additionalUK = True
          End If
          If .minLeftCardinality = 1 And _
             .maxLeftCardinality = 1 And _
             .minRightCardinality = 1 And _
             .maxRightCardinality = 1 And _
             g_classes.descriptors(.rightEntityIndex).isPsTagged = True Then
            additionalUK = True
          End If
            
          If additionalUK Then
            ukName = "UK_" & Mid(genPkName(.relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex), 4)
            
            printSectionHeader "Unique Constraint for """ & qualTabName & """", fileNo

            Print #fileNo,
            Print #fileNo, addTab(0); "CREATE UNIQUE INDEX"
            Print #fileNo, addTab(1); genQualUkName(.sectionIndex, "", ukName, ddlType, thisOrgIndex, thisPoolIndex)
            Print #fileNo, addTab(0); "ON"
            Print #fileNo, addTab(1); qualTabName; "("; g_anOid; ", PS_OID"; ")"
            Print #fileNo, gc_sqlCmdDelim

            Print #fileNo, addTab(0); "ALTER TABLE"
            Print #fileNo, addTab(1); qualTabName
            Print #fileNo, addTab(0); "ADD CONSTRAINT "
            Print #fileNo, addTab(1); ukName
            Print #fileNo, addTab(1); "UNIQUE ("; g_anOid; ", PS_OID"; ")"
            Print #fileNo, gc_sqlCmdDelim
          End If

        End If

      End If
      
      qualIndexName = genUkName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, False, False)
          
      pkAttrList = getPkAttrListByRel(thisRelIndex, ddlType)
          
      pkAttrList = pkAttrList & IIf(pkAttrList = "", "", ", ") & UCase(leftFkAttrs) & ", " & UCase(rightFkAttrs)
        
' ### IF IVK ###
      If g_genLrtSupport And .isUserTransactional And Not forLrt And pkAttrList <> "" Then
        pkAttrList = pkAttrList & ", " & g_anIsDeleted
      End If
         
' ### ENDIF IVK ###
      printSectionHeader "Unique Index on Foreign Key Attributes", fileNo
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE "; IIf(poolSuppressUniqueConstraints, "", "UNIQUE "); "INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName; " ("; pkAttrList; ")"
        Print #fileNo, gc_sqlCmdDelim
      End If ' indexExcp
    ElseIf useSurrogateKeysForNMRelationships And (numAttrs > 0 Or .logLastChange Or Not .isUserTransactional Or forLrt) And .useSurrogateKey Then
        
      pkAttrList = UCase(leftFkAttrs) & ", " & UCase(rightFkAttrs)
          
      If forLrt Then
        pkAttrList = pkAttrList & ", " & _
                     g_anInLrt & ", " & _
                     g_anLrtState
        
        qualIndexName = genUkName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, forMqt)
          
          ' FIXME: Uniqueness of this index is correct from business point of view, but
          ' Hibernate may propagate INSERTs / DELETEs in a wrong sequence
          printSectionHeader "Index on Foreign Key Attributes", fileNo
          
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName; " ("; pkAttrList; ")"
          Print #fileNo, gc_sqlCmdDelim
        End If ' indexExcp
      Else
        If generateDdlCreatePK Then
          printSectionHeader "Primary Key for """ & qualTabName & """", fileNo
          
          Print #fileNo,
          Print #fileNo, addTab(0); "ALTER TABLE"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "ADD CONSTRAINT"
          Print #fileNo, addTab(1); genPkName(.relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, , forLrt)
          Print #fileNo, addTab(0); "PRIMARY KEY ("; pkAttrList; ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If
      End If
    End If
    
    If useSurrogateKeysForNMRelationships And forLrt And Not forMqt And Not .isPsTagged And _
      (rightOrClass.isCommonToOrgs <> leftOrClass.isCommonToOrgs) Then _

        qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, forNl, forMqt, cosnInLrt & "CFK")
      
        printSectionHeader "Index on """ & g_anInLrt & """, " & _
                           IIf(Not leftOrClass.isCommonToOrgs, """" & .leftFkColName(ddlType) & """, ", "") & _
                           IIf(Not rightOrClass.isCommonToOrgs, """" & .rightFkColName(ddlType) & """, ", "") & _
                           """" & g_anLrtState & """ and """ & g_anOid & """ in table """ & qualTabName & """", fileNo
        
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
          
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          
          If Not leftOrClass.isCommonToOrgs Then
            Print #fileNo, addTab(1); .leftFkColName(ddlType); " ASC,"
          End If
          
          If Not rightOrClass.isCommonToOrgs Then
            Print #fileNo, addTab(1); .rightFkColName(ddlType); " ASC,"
          End If
  
          Print #fileNo, addTab(1); g_anLrtState; " ASC,"
          Print #fileNo, addTab(1); g_anOid; " ASC"
          
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
    
    If useSurrogateKeysForNMRelationships Then
      If forMqt Then
        qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, forNl, forMqt, cosnOid & cosnIsLrtPrivate)
      
        printSectionHeader "Index on """ & g_anOid & """ and """ & g_anIsLrtPrivate & """ in table """ & qualTabName & """", fileNo
        
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
          
          Print #fileNo, addTab(1); g_anOid; " ASC"; IIf(forLrt Or forMqt, ",", "")
          If forMqt Then
            Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
          End If
          If forLrt Then
            Print #fileNo, addTab(1); g_anInLrt; " ASC,"
            Print #fileNo, addTab(1); g_anLrtState; " ASC"
          End If
          
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
      ElseIf forLrt Then
        qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, forNl, forMqt, cosnOid)
      
        printSectionHeader "Index on """ & g_anOid & """ in table """ & qualTabName & """", fileNo
        
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
          Print #fileNo, addTab(1); g_anOid; " ASC"
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
        
        qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, forNl, forMqt, "IS" & IIf(.logLastChange, "CU", ""))
      
        printSectionHeader "Index on """ & g_anInLrt & ", " & g_anLrtState & ", " & IIf(.logLastChange, g_anCreateTimestamp & ", " & g_anLastUpdateTimestamp, "") & """ in table """ & qualTabName & """", fileNo
        
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"; IIf(.logLastChange, ",", "")
          If .logLastChange Then
            Print #fileNo, addTab(1); g_anCreateTimestamp; " ASC,"
            Print #fileNo, addTab(1); g_anLastUpdateTimestamp; " ASC"
          End If
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
      End If
    End If
    
' ### IF IVK ###
    If (rightOrClass.isPsTagged Or leftOrClass.isPsTagged) Then
      If (ddlType = edtPdm) And generateIndexOnFkForPsTag Then
        qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, forNl, forMqt, "PSO")
        
        colList = g_anPsOid & " ASC"
        
        colList = colList & ", " & leftFkAttrs & " ASC, " & rightFkAttrs & " ASC"
        
        If g_genLrtSupport And .isUserTransactional And (Not forLrt Or forMqt) Then
          colList = colList & ", " & g_anIsDeleted & " ASC"
        End If
                
        If forMqt Then
          colList = colList & ", " & g_anIsLrtPrivate & " ASC"
        End If
        
        If forLrt Then
          colList = colList & ", " & g_anInLrt & " ASC" & ", " & g_anLrtState & " ASC"
        End If
        
        printSectionHeader "Index on Foreign Key to ""Product Structure"" Table", fileNo
        
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
          Print #fileNo, addTab(1); colList
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
      End If
    ElseIf (rightOrClass.aggHeadClassIndex = g_classIndexGenericCode Or leftOrClass.aggHeadClassIndex = g_classIndexGenericCode) Then
        'generate index for DIV_OID
      If (ddlType = edtPdm) Then
        qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, forNl, forMqt, "DVO")
        
        colList = g_anDivOid & " ASC"
        
        colList = colList & ", " & leftFkAttrs & " ASC, " & rightFkAttrs & " ASC"
        
        If g_genLrtSupport And .isUserTransactional And (Not forLrt Or forMqt) Then
          colList = colList & ", " & g_anIsDeleted & " ASC"
        End If
                
        If forMqt Then
          colList = colList & ", " & g_anIsLrtPrivate & " ASC"
        End If
        
        If forLrt Then
          colList = colList & ", " & g_anInLrt & " ASC" & ", " & g_anLrtState & " ASC"
        End If
        
        printSectionHeader "Index on Foreign Key to ""Division"" Table", fileNo
        
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
          Print #fileNo, addTab(1); colList
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
      End If
    
    
    End If

' ### ENDIF IVK ###
    If supportNlForRelationships And .isNl Then
      If (ddlType = edtPdm) And generateIndexOnFkForNLang Then
        qualIndexName = genQualIndexName(.sectionIndex, .relName & "LAN", .shortName & "LAN", ddlType, thisOrgIndex, thisPoolIndex, , forLrt, forNl, forMqt)
        
        printSectionHeader "Index on Foreign Key to ""Language Table""", fileNo
        
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
          Print #fileNo, addTab(1); g_anLanguageId; " ASC"
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
      End If
    End If

    If (ddlType = edtPdm) And generateIndexOnFk Then
      qualIndexName = genQualIndexName(.sectionIndex, rightclass.className & relShortName, rightclass.shortName & relShortName, ddlType, thisOrgIndex, thisPoolIndex, , forLrt, forNl, forMqt)
            
      printSectionHeader "Index on Foreign Key corresponding to Class """ & rightclass.sectionName & "." & rightclass.className & """", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        
        Print #fileNo, addTab(1); getFkSrcAttrSeq(rightclass.classIndex, "", ddlType); " ASC"; IIf(forLrt Or forMqt, ",", "")
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
          
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
      
      qualIndexName = genQualIndexName(.sectionIndex, leftClass.className & relShortName, leftClass.shortName & relShortName, ddlType, thisOrgIndex, thisPoolIndex, , forLrt, forNl, forMqt)
                
      printSectionHeader "Index on Foreign Key corresponding to Class """ & leftClass.sectionName & "." & leftClass.className & """", fileNo
          
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
         
        Print #fileNo, addTab(1); getFkSrcAttrSeq(leftClass.classIndex, "", ddlType); " ASC"; IIf(forLrt Or forMqt, ",", "")
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
          
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
  
    If .isUserTransactional And generateIndexOnAhClassIdOid And (Not forLrt Or forMqt) Then
      qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, , forLrt, forNl, forMqt, cosnAggHeadClassId & cosnAggHeadOId)
      
      printSectionHeader "Index on """ & g_anAhCid & """ and """ & g_anAhOid & """ in table """ & qualTabName & """", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        Print #fileNo, addTab(1); g_anAhCid; " ASC,"
        
        Print #fileNo, addTab(1); g_anAhOid; " ASC"; IIf(forLrt Or forMqt, ",", "")
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
    
' ### IF IVK ###
    If (.aggHeadClassIndex > 0) And g_genLrtSupport And .isUserTransactional And generateIndexOnAhClassIdOidStatus And (Not forLrt Or forMqt) Then
      qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, , forLrt, forNl, forMqt, _
                                       "X" & Left(cosnAggHeadClassId, 1) & Left(cosnAggHeadOId, 1) & Left(esnStatus, 1))
      
      printSectionHeader "Index on """ & g_anAhCid & """, """ & g_anAhOid & """ and """ & g_anStatus & """ in table """ & qualTabName & """", fileNo
      
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        Print #fileNo, addTab(1); g_anAhCid; " ASC,"
        
        Print #fileNo, addTab(1); g_anAhOid; " ASC,"
        Print #fileNo, addTab(1); g_anStatus; " ASC"; IIf(forLrt Or forMqt, ",", "")
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
    
' ### ENDIF IVK ###
    If (.aggHeadClassIndex > 0) And (Not forLrt Or forMqt) And generateIndexOnAhOid Then
      qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, , forLrt, forNl, forMqt, cosnAggHeadOId)
      
      printSectionHeader "Index on """ & g_anAhOid & """ in table """ & qualTabName & """", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        
        Print #fileNo, addTab(1); g_anAhOid; " ASC"; IIf(forLrt Or forMqt, ",", "")
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If

' ### IF IVK ###
    If .isPsTagged And .isUserTransactional And Not forLrt And Not forMqt And generateIndexForSetProductive Then
      qualIndexName = genQualIndexName(.sectionIndex, .relName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, , forLrt, forNl, forMqt, "STP")
      
      printSectionHeader "Index on """ & g_anPsOid & """,""" & g_anStatus & """,""" & g_anIsDeleted & """,""" & g_anHasBeenSetProductive & """,""" & g_anOid & """ in table """ & qualTabName & """ (for SETPRODUCTIVE)", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        
        Print #fileNo, addTab(1); g_anPsOid; " ASC,"
        Print #fileNo, addTab(1); g_anStatus; " ASC,"
        Print #fileNo, addTab(1); g_anIsDeleted; " ASC,"
        Print #fileNo, addTab(1); g_anHasBeenSetProductive; " ASC,"
        Print #fileNo, addTab(1); g_anOid; " ASC"
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
  
' ### ENDIF IVK ###
    Dim i As Integer
    For i = 1 To .indexRefs.numRefs
      colList = ""
      colListIncluded = ""
      With g_indexes.descriptors(.indexRefs.refs(i))
        If .specificToQueryTables Then
          If poolSupportLrt Then
            If g_relationships.descriptors(thisRelIndex).useMqtToImplementLrt Then
              If Not forMqt Then
                GoTo NextI
              End If
            End If
          End If
        End If
        
        If (.attrRefs.numRefs > 0) And (.specificToPools = "" Or includedInList(.specificToPools, g_pools.descriptors(thisPoolIndex).id)) Then
          Dim j As Integer
          For j = 1 To .attrRefs.numRefs
            Dim thisColName As String
            Dim extraColName As String
            thisColName = ""
            extraColName = ""
            If g_indexAttrs.descriptors(.attrRefs.refs(j)).attrRef > 0 Then
              With g_attributes.descriptors(g_indexAttrs.descriptors(.attrRefs.refs(j)).attrRef)
                thisColName = .dbColName(ddlType)
              End With
            ElseIf g_indexAttrs.descriptors(.attrRefs.refs(j)).attrRef < 0 Then
              ' meta attribute
              thisColName = g_indexAttrs.descriptors(.attrRefs.refs(j)).attrName
            ElseIf g_indexAttrs.descriptors(.attrRefs.refs(j)).relRef > 0 Then
              If g_indexAttrs.descriptors(.attrRefs.refs(j)).relRefDirection = etLeft Then
                With g_relationships.descriptors(g_indexAttrs.descriptors(.attrRefs.refs(j)).relRef)
                  If g_classes.descriptors(.rightEntityIndex).useSurrogateKey Then
                    thisColName = genSurrogateKeyName(ddlType, g_classes.descriptors(.rightEntityIndex).shortName)
                  Else
                    thisColName = getPkAttrListByClass(.rightEntityIndex, ddlType)
                  End If
                End With
              Else
                With g_relationships.descriptors(g_indexAttrs.descriptors(.attrRefs.refs(j)).relRef)
                  If g_classes.descriptors(.leftEntityIndex).useSurrogateKey Then
                    thisColName = genSurrogateKeyName(ddlType, g_classes.descriptors(.leftEntityIndex).shortName)
                  Else
                    thisColName = getPkAttrListByClass(.leftEntityIndex, ddlType)
                  End If
                End With
              End If
            End If
            
            If thisColName <> "" Then
              With g_indexAttrs.descriptors(g_indexes.descriptors(g_relationships.descriptors(thisRelIndex).indexRefs.refs(i)).attrRefs.refs(j))
                If .attrIsIncluded Then
                  colListIncluded = colListIncluded & IIf(colListIncluded = "", "", "," & vbCrLf) & addTab(1) & thisColName & IIf(.isAsc, " ASC", " DESC")
                Else
                  colList = colList & IIf(colList = "", "", "," & vbCrLf) & addTab(1) & thisColName & IIf(.isAsc, " ASC", " DESC")
                  If extraColName <> "" Then
                    colList = colList & IIf(colList = "", "", "," & vbCrLf) & addTab(1) & extraColName & " ASC"
                  End If
                End If
              End With
            End If
          Next j

          printSectionHeader "Index """ & .indexName & """ for " & IIf(g_relationships.descriptors(thisRelIndex).notAcmRelated, "table", "ACM relationship") & " """ & .sectionName & "." & .className & """", fileNo
            
          qualIndexName = genQualIndexName(.sectionIndex, .indexName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, False, forLrt, forNl, forMqt)
          If indexExcp(qualIndexName, thisOrgIndex) = False Then
            Print #fileNo,
            Print #fileNo, addTab(0); "CREATE " & IIf(.isUnique And Not noConstraints And Not forMqt, "UNIQUE ", "") & "INDEX"
            Print #fileNo, addTab(1); qualIndexName
            Print #fileNo, addTab(0); "ON"
            Print #fileNo, addTab(1); qualTabName
            Print #fileNo, addTab(0); "("
            Print #fileNo, addTab(0); colList; IIf(forLrt Or forMqt, ",", "")
                
            If forMqt Then
              Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
            End If
            If forLrt Then
              Print #fileNo, addTab(1); g_anInLrt; " ASC,"
              Print #fileNo, addTab(1); g_anLrtState; " ASC"
            End If
              
            Print #fileNo, addTab(0); ")"
              
            If colListIncluded <> "" Then
              Print #fileNo, addTab(0); "INCLUDE"
              Print #fileNo, addTab(0); "("
              Print #fileNo, addTab(0); colListIncluded
              Print #fileNo, addTab(0); ")"
            End If
              
            Print #fileNo, addTab(0); gc_sqlCmdDelim
          End If ' indexExcp
        End If
      End With
NextI:
    Next i
  End With

NormalExit:
  On Error Resume Next
  Exit Sub

ErrorExit:
  errMsgBox Err.description
  Resume NormalExit
End Sub


Sub genIndexesForClassIndex( _
  ByRef qualTabName As String, _
  ByRef classIndex As Integer, _
  ByVal thisOrgIndex As Integer, _
  ByVal thisPoolIndex As Integer, _
  fileNo As Integer, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional forGen As Boolean = False, _
  Optional forLrt As Boolean = False, _
  Optional forNl As Boolean = False, _
  Optional forMqt As Boolean = False, _
  Optional forSubClass As Boolean = False, _
  Optional noConstraints As Boolean = False _
)
  If Not genIndexesForAcmClasses Or (forLrt And Not generateIndexOnLrtTabs) Or Not generateDdlCreateIndex Then
    Exit Sub
  End If
  
  Dim poolSupportLrt As Boolean
  Dim poolCommonItemsLocal As Boolean
  If thisPoolIndex > 0 Then
    With g_pools.descriptors(thisPoolIndex)
      poolSupportLrt = .supportLrt
      poolCommonItemsLocal = .commonItemsLocal
    End With
  End If
  
  Dim qualIndexName As String
  Dim i As Integer, j As Integer
  Dim colList As String
  Dim colListIncluded As String

  colList = ""
  colListIncluded = ""
  
' ### IF IVK ###
  Dim fkAttrToDiv As String
  fkAttrToDiv = ""
  Dim useFkToDiv As Boolean
  Dim tabPartitionType As PartitionType
' ### ENDIF IVK ###
  
  With g_classes.descriptors(classIndex)
' ### IF IVK ###
    Dim isDivTagged As Boolean
    isDivTagged = False
    
    If .navPathToDiv.relRefIndex > 0 And Not .isPsTagged And Not forNl Then
      With g_relationships.descriptors(.navPathToDiv.relRefIndex)
        fkAttrToDiv = IIf(g_classes.descriptors(classIndex).navPathToDiv.navDirection = etLeft, .leftFkColName(ddlType), .rightFkColName(ddlType))
        isDivTagged = True
      End With
    End If
    
    If .isPsTagged And supportRangePartitioningByPsOid Then
      tabPartitionType = IIf(.noRangePartitioning, ptNone, ptPsOid)
    ElseIf isDivTagged And supportRangePartitioningByDivOid Then
      tabPartitionType = IIf(.noRangePartitioning, ptNone, ptDivOid)
    End If
    
    If tabPartitionType <> ptNone Then
      If .isUserTransactional Then
        If .useMqtToImplementLrt Then
          If forLrt Then
            If Not (forMqt Or partitionLrtPrivateWhenMqt) Then tabPartitionType = ptNone
          Else
            If Not (forMqt Or partitionLrtPublicWhenMqt) Then tabPartitionType = ptNone
          End If
        Else
          If forLrt Then
            If Not partitionLrtPrivateWhenNoMqt Then tabPartitionType = ptNone
          Else
            If Not partitionLrtPublicWhenNoMqt Then tabPartitionType = ptNone
          End If
        End If
      End If
      If (tabPartitionType <> ptNone) And noPartitioningInDataPools <> "" And thisPoolIndex > 0 Then
        If includedInList(noPartitioningInDataPools, g_pools.descriptors(thisPoolIndex).id) Then tabPartitionType = ptNone
      End If
    End If
        
    If .hasGroupIdAttrInNonGenInclSubClasses And Not forNl And Not forSubClass And (thisPoolIndex <> g_archiveDataPoolIndex) Then
      If (.isUserTransactional And (poolSupportLrt And (.useMqtToImplementLrt = forMqt))) Or Not poolSupportLrt Then
        qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "GRP")
        
        printSectionHeader "Index on ""GroupID-columns"" in table """ & qualTabName & """", fileNo
          
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
  
          If Not forNl And Not .hasOwnTable And Not forSubClass And Not .notAcmRelated Then
            Print #fileNo, addTab(1); g_anCid; " ASC,"
          End If
          
          ' add groupid attributes only once
          Dim k As Integer
          Dim groupIdAttrNames(5) As String ' so far only 2
          For k = LBound(.groupIdAttrIndexesInclSubclasses) To UBound(.groupIdAttrIndexesInclSubclasses)
            With g_attributes.descriptors(.groupIdAttrIndexesInclSubclasses(k))
              Dim found As Boolean
              found = False
              Dim attrName As String
              attrName = genAttrName(.attributeName, ddlType)
              groupIdAttrNames(k) = attrName
              For i = 1 To k - 1
                 If (groupIdAttrNames(i) = attrName) Then
                   found = True
                 End If
              Next i
              If Not found Then
                Print #fileNo, addTab(1); attrName; " ASC,"
              End If
            End With
          Next k
  
          If .isPsTagged Then
            Print #fileNo, addTab(1); g_anPsOid; " ASC"; IIf(forMqt Or .isUserTransactional, ",", "")
          End If
          If .isUserTransactional And forLrt And poolSupportLrt Then
            Print #fileNo, addTab(1); g_anLrtState; " ASC,"
          End If
          If .isUserTransactional And poolSupportLrt Then
            Print #fileNo, addTab(1); g_anIsDeleted; " ASC,"
            Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          End If
          If forMqt Then
            Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC,"
          End If
          Print #fileNo, addTab(1); g_anOid; " ASC"
          
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
      End If
    End If
    
' ### ENDIF IVK ###
    If forMqt And Not forSubClass Then
' ### IF IVK ###
      For i = 1 To IIf(fkAttrToDiv = "", 1, 2)
        useFkToDiv = (i = 2)
        qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnOid & cosnIsLrtPrivate & IIf(useFkToDiv, "D", ""))
' ### ELSE IVK ###
' ### INDENT IVK ### -2
'       qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnOid & cosnIsLrtPrivate)
' ### ENDIF IVK ###
        
' ### IF IVK ###
        printSectionHeader "Index on " & IIf(useFkToDiv, """" & fkAttrToDiv & """, ", "") & """" & g_anOid & """, """ & g_anIsLrtPrivate & """, ... in table """ & qualTabName & """", fileNo
' ### ELSE IVK ###
'       printSectionHeader "Index on " & """" & g_anOid & """, """ & g_anIsLrtPrivate & """, ... in table """ & qualTabName & """", fileNo
' ### ENDIF IVK ###
          
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
' ### IF IVK ###
          
          If useFkToDiv Then
            Print #fileNo, addTab(1); fkAttrToDiv; " ASC,"
          End If
' ### ENDIF IVK ###
          
          Print #fileNo, addTab(1); g_anOid; " ASC,"
' ### IF IVK ###
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt Or Not .isPsTagged, ",", "")
' ### ELSE IVK ###
'       Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
' ### ENDIF IVK ###
          
          If forLrt Then
            Print #fileNo, addTab(1); g_anInLrt; " ASC,"
' ### IF IVK ###
            Print #fileNo, addTab(1); g_anLrtState; " ASC"; IIf(Not .isPsTagged, ",", "")
' ### ELSE IVK ###
'         Print #fileNo, addTab(1); g_anLrtState; " ASC"
' ### ENDIF IVK ###
          End If
' ### IF IVK ###
          
          If Not .isPsTagged Then
            Print #fileNo, addTab(1); g_anIsDeleted; " ASC,"
            Print #fileNo, addTab(1); g_anAhOid; " ASC"
          End If
' ### ELSE IVK ###
'       Print #fileNo, addTab(1); g_anAhOid; " ASC"
' ### ENDIF IVK ###
          
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
' ### IF IVK ###
      Next i
' ### ELSE IVK ###
' ### INDENT IVK ### 0
' ### ENDIF IVK ###
    End If
' ### IF IVK ###
    
    If fkAttrToDiv <> "" And Not .isCommonToPools And Not poolCommonItemsLocal And (forMqt Or Not (.useMqtToImplementLrt And forLrt)) Then
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnOid & cosnIsLrtPrivate & "DD")
          
      printSectionHeader "Index on """ & fkAttrToDiv & """, """ & g_anOid & """, ... in table """ & qualTabName & """", fileNo
            
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
            
        Print #fileNo, addTab(1); g_anOid; " ASC,"
        
        If Not .isPsTagged And Not forLrt Then
          Print #fileNo, addTab(1); g_anIsDeleted; " ASC,"
        End If
            
        Print #fileNo, addTab(1); fkAttrToDiv; " ASC"; IIf((Not .isPsTagged And forLrt) Or forLrt Or forMqt, ",", "")
            
        If Not .isPsTagged And forLrt Then
          Print #fileNo, addTab(1); g_anIsDeleted; " ASC"; IIf(forLrt Or forMqt, ",", "")
        End If
            
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
            
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
            
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
' ### ENDIF IVK ###
    
    If forLrt And Not forMqt And Not forSubClass Then
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "IS" & IIf(.logLastChange, "CU", ""))
      
      printSectionHeader "Index on """ & g_anInLrt & ", " & g_anLrtState & ", " & IIf(.logLastChange, g_anCreateTimestamp & ", " & g_anLastUpdateTimestamp, "") & """ in table """ & qualTabName & """", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        Print #fileNo, addTab(1); g_anInLrt; " ASC,"
        Print #fileNo, addTab(1); g_anLrtState; " ASC"; IIf(.logLastChange, ",", "")
        If .logLastChange Then
          Print #fileNo, addTab(1); g_anCreateTimestamp; " ASC,"
          Print #fileNo, addTab(1); g_anLastUpdateTimestamp; " ASC"
        End If
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
' ### IF IVK ###
    
    If .isUserTransactional And fkAttrToDiv <> "" And Not forNl And Not forGen And Not forNl And Not forLrt And poolSupportLrt Then
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, g_classes.descriptors(g_classIndexDivision).shortName)
        
      printSectionHeader "Index on """ & fkAttrToDiv & """ and """ & g_anInLrt & """ in table """ & qualTabName & """", fileNo
    
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
          
        Print #fileNo, addTab(1); fkAttrToDiv; " ASC,"
        Print #fileNo, addTab(1); g_anInLrt; " ASC"
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
' ### ENDIF IVK ###
    
    If Not forSubClass And Not forNl And (forMqt Or (Not .notAcmRelated And Not .useMqtToImplementLrt)) And Not .hasOwnTable And Not poolCommonItemsLocal Then
' ### IF IVK ###
      For i = 1 To IIf(fkAttrToDiv = "", 1, 2)
        useFkToDiv = (i = 2)
        qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnOid & cosnClassId & IIf(useFkToDiv, "D", ""))
        
        printSectionHeader "Index on " & IIf(useFkToDiv, """" & fkAttrToDiv & """, ", "") & """" & g_anOid & """" & IIf(forMqt, ",", " and") & " """ & g_anCid & """" & IIf(forMqt, " and """ & g_anIsLrtPrivate & """", "") & " in table """ & qualTabName & """", fileNo
' ### ELSE IVK ###
' ### INDENT IVK ### -2
'       qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnOid & cosnClassId)
'
'       printSectionHeader "Index on " & """" & g_anOid & """" & IIf(forMqt, ",", " and") & " """ & g_anCid & """" & IIf(forMqt, " and """ & g_anIsLrtPrivate & """", "") & " in table """ & qualTabName & """", fileNo
' ### ENDIF IVK ###
          
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
' ### IF IVK ###
          
          If useFkToDiv Then
            Print #fileNo, addTab(1); fkAttrToDiv; " ASC,"
          End If
          
' ### ENDIF IVK ###
          Print #fileNo, addTab(1); g_anOid; " ASC,"
' ### IF IVK ###
          Print #fileNo, addTab(1); g_anCid; " ASC"; IIf(.isPsTagged Or forMqt Or .isUserTransactional, ",", "")
          If .isPsTagged Then
            Print #fileNo, addTab(1); g_anPsOid; " ASC"; IIf(forMqt Or .isUserTransactional, ",", "")
          End If
' ### ELSE IVK ###
'       Print #fileNo, addTab(1); g_anCid; " ASC"; IIf(forMqt Or .isUserTransactional, ",", "")
' ### ENDIF IVK ###
          If forMqt Then
            Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(.isUserTransactional, ",", "")
          End If
          If .isUserTransactional Then
            Print #fileNo, addTab(1); g_anInLrt; " ASC"; IIf(forLrt, ",", "")
            If forLrt Then
              Print #fileNo, addTab(1); g_anLrtState; " ASC"
            End If
          End If
          
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
' ### IF IVK ###
      Next i
' ### ELSE IVK ###
' ### INDENT IVK ### 0
' ### ENDIF IVK ###
    End If
' ### IF IVK ###
    
    If (Not forLrt Or forMqt) And Not forNl And Not forSubClass And ((forGen And .hasExpressionInGen) Or (Not forGen And .hasExpressionInNonGen)) Then
      If generateIndexOnFk Then
        Dim classHasNoIdentity As Boolean
        classHasNoIdentity = .hasNoIdentity
        
        Dim transformation As AttributeListTransformation
        Dim tabColumns As EntityColumnDescriptors
        tabColumns = nullEntityColumnDescriptors
        initAttributeTransformation transformation, 0, , True
        genTransformedAttrListForEntityWithColReuse .classIndex, eactClass, transformation, tabColumns, fileNo, ddlType, thisOrgIndex, thisPoolIndex, , forLrt, forGen, edomNone
  
        For k = 1 To tabColumns.numDescriptors
          With tabColumns.descriptors(k)
            If (.columnCategory And eacFkOidExpression) <> 0 And (.columnCategory And eacNationalBool) = 0 Then
              qualIndexName = genQualIndexName(g_classes.descriptors(classIndex).sectionIndex, g_classes.descriptors(classIndex).className, _
                g_classes.descriptors(classIndex).shortName & g_relationships.descriptors(.acmFkRelIndex).shortName & IIf(.columnCategory And eacNational, "N", ""), _
                ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "EXP")
      
              printSectionHeader "Index on Expression-Foreign-Key """ & .columnName & """ in table """ & qualTabName & """", fileNo
        
              If indexExcp(qualIndexName, thisOrgIndex) = False Then
                Print #fileNo,
                Print #fileNo, addTab(0); "CREATE INDEX"
                Print #fileNo, addTab(1); qualIndexName
                Print #fileNo, addTab(0); "ON"
                Print #fileNo, addTab(1); qualTabName
                Print #fileNo, addTab(0); "("
          
                Print #fileNo, addTab(1); .columnName; " ASC"
          
                Print #fileNo, addTab(0); ")"
                Print #fileNo, gc_sqlCmdDelim
              End If ' indexExcp
            End If
          End With
        Next k
      End If
    End If
' ### ENDIF IVK ###
    
    If forNl Then
      If generateIndexOnFk Then
        Dim attrNameParFk As String
        attrNameParFk = genSurrogateKeyName(ddlType, .shortName)
        
        qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "PAR")
            
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
          
          Print #fileNo, addTab(1); attrNameParFk; " ASC"; IIf(forLrt Or forMqt, ",", "")
          If forMqt Then
            Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
          End If
          If forLrt Then
            Print #fileNo, addTab(1); g_anInLrt; " ASC,"
            Print #fileNo, addTab(1); g_anLrtState; " ASC"
          End If
          
          Print #fileNo, addTab(0); ")"
          Print #fileNo, gc_sqlCmdDelim
        End If ' indexExcp
      End If
    End If
    
    If Not forNl And Not .hasOwnTable And Not forSubClass And generateIndexOnClassId And Not .notAcmRelated Then
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnClassId)
      
      printSectionHeader "Index on """ & g_anCid & """ in table """ & qualTabName & """", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        
  ' ### IF IVK ###
        Print #fileNo, addTab(1); g_anCid; " ASC"; IIf(forLrt Or forMqt Or .isPsTagged, ",", "")
        If .isPsTagged Then
          Print #fileNo, addTab(1); g_anPsOid; " ASC"; IIf(forMqt Or forLrt, ",", "")
        End If
  ' ### ELSE IVK ###
  '     Print #fileNo, addTab(1); g_anCid; " ASC"; IIf(forLrt Or forMqt, ",", "")
  ' ### ENDIF IVK ###
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
' ### IF IVK ###
    
    If Not forNl And .isPsTagged And forLrt And Not forSubClass And generateIndexOnFkForPsTag And Not .notAcmRelated Then
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "PLS")
      
      printSectionHeader "Index on """ & g_anPsOid & """, """ & IIf(forMqt, """" & g_anIsLrtPrivate & """, ", "") & g_anInLrt & """ and """ & g_anLrtState & """ in table """ & qualTabName & """", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        
        Print #fileNo, addTab(1); g_anPsOid; " ASC"; IIf(forLrt Or forMqt, ",", "")
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        Print #fileNo, addTab(1); g_anInLrt; " ASC,"
        Print #fileNo, addTab(1); g_anLrtState; " ASC"
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
' ### ENDIF IVK ###
    
' ### IF IVK ###
    If Not forNl And Not forSubClass And .isGenForming And (forGen Or .hasNoIdentity) Then
' ### ELSE IVK ###
'   If Not forNl And Not forSubClass And .isGenForming And forGen Then
' ### ENDIF IVK ###
      If generateIndexOnValidFrom Then
        qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnValidFrom)
      
        printSectionHeader "Index on """ & g_anValidFrom & """ in table """ & qualTabName & """", fileNo
      
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
        
          Print #fileNo, addTab(1); g_anValidFrom; " ASC"; IIf(forLrt Or forMqt, ",", "")
          If forMqt Then
            Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
          End If
          If forLrt Then
            Print #fileNo, addTab(1); g_anInLrt; " ASC,"
            Print #fileNo, addTab(1); g_anLrtState; " ASC"
          End If
        
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
      End If
      
      If generateIndexOnValidUntil Then
        qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnValidTo)
      
        printSectionHeader "Index on """ & g_anValidTo & """ in table """ & qualTabName & """", fileNo
      
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
        
          Print #fileNo, addTab(1); g_anValidTo; " ASC"; IIf(forLrt Or forMqt, ",", "")
          If forMqt Then
            Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
          End If
          If forLrt Then
            Print #fileNo, addTab(1); g_anInLrt; " ASC,"
            Print #fileNo, addTab(1); g_anLrtState; " ASC"
          End If
        
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
      End If
  
      If generateIndexOnValidFromUntil Then
        qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnValidFrom & cosnValidTo)
      
        printSectionHeader "Index on """ & g_anValidFrom & """ and """ & g_anValidTo & """ in table """ & qualTabName & """", fileNo
      
        If indexExcp(qualIndexName, thisOrgIndex) = False Then
          Print #fileNo,
          Print #fileNo, addTab(0); "CREATE INDEX"
          Print #fileNo, addTab(1); qualIndexName
          Print #fileNo, addTab(0); "ON"
          Print #fileNo, addTab(1); qualTabName
          Print #fileNo, addTab(0); "("
          Print #fileNo, addTab(1); g_anValidFrom; " ASC,"
        
          Print #fileNo, addTab(1); g_anValidTo; " ASC"; IIf(forLrt Or forMqt, ",", "")
          If forMqt Then
            Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
          End If
          If forLrt Then
            Print #fileNo, addTab(1); g_anInLrt; " ASC,"
            Print #fileNo, addTab(1); g_anLrtState; " ASC"
          End If
        
          Print #fileNo, addTab(0); ")"
          Print #fileNo, addTab(0); gc_sqlCmdDelim
        End If ' indexExcp
      End If
    End If
    
' ### IF IVK ###
    If .isUserTransactional And Not forSubClass And Not .condenseData And generateIndexOnAhClassIdOid And (Not forLrt Or forMqt) Then
' ### ELSE IVK ###
'   If .isUserTransactional And Not forSubClass And generateIndexOnAhClassIdOid And (Not forLrt Or forMqt) Then
' ### ENDIF IVK ###
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnAggHeadClassId & cosnAggHeadOId)
      
      printSectionHeader "Index on """ & g_anAhCid & """ and """ & g_anAhOid & """ in table """ & qualTabName & """", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        Print #fileNo, addTab(1); g_anAhCid; " ASC,"
        
        Print #fileNo, addTab(1); g_anAhOid; " ASC"; IIf(forLrt Or forMqt, ",", "")
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
    
' ### IF IVK ###
    If (.aggHeadClassIndex > 0) And g_genLrtSupport And .isUserTransactional And Not forSubClass And Not .condenseData And generateIndexOnAhClassIdOidStatus And (Not forLrt Or forMqt) Then
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, _
                                       "X" & Left(cosnAggHeadClassId, 1) & Left(cosnAggHeadOId, 1) & Left(esnStatus, 1))
      
      printSectionHeader "Index on """ & g_anAhCid & """, """ & g_anAhOid & """ and """ & g_anStatus & """ in table """ & qualTabName & """", fileNo
      
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        Print #fileNo, addTab(1); g_anAhCid; " ASC,"
        
        Print #fileNo, addTab(1); g_anAhOid; " ASC,"
        Print #fileNo, addTab(1); g_anStatus; " ASC"; IIf(forLrt Or forMqt, ",", "")
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
    
' ### ENDIF IVK ###
    If (.aggHeadClassIndex > 0) And (Not forLrt Or forMqt) And generateIndexOnAhOid And Not forSubClass Then
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, cosnAggHeadOId)
      
      printSectionHeader "Index on """ & g_anAhOid & """ in table """ & qualTabName & """", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        
        Print #fileNo, addTab(1); g_anAhOid; " ASC"; IIf(forLrt Or forMqt, ",", "")
        If forMqt Then
          Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
        End If
        If forLrt Then
          Print #fileNo, addTab(1); g_anInLrt; " ASC,"
          Print #fileNo, addTab(1); g_anLrtState; " ASC"
        End If
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
' ### IF IVK ###
    
    If .isPsTagged And .isUserTransactional And .isAggHead And Not .condenseData And Not forGen And Not forLrt And Not forMqt And Not forSubClass And poolSupportLrt And (thisOrgIndex <> g_primaryOrgIndex) Then
      ' Index for FTOLOCK
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "PIO")
      
      printSectionHeader "Index on """ & g_anPsOid & """,""" & g_anInLrt & """, """ & g_anOid & """ in table """ & qualTabName & """", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        
        Print #fileNo, addTab(1); g_anPsOid; " ASC,"
        Print #fileNo, addTab(1); g_anInLrt; " ASC,"
        Print #fileNo, addTab(1); g_anOid; " ASC"
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
    
    If .isPsTagged And .isUserTransactional And Not forLrt And Not forMqt And Not forSubClass And generateIndexForSetProductive Then
      qualIndexName = genQualIndexName(.sectionIndex, .className, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt, "STP")
      
      printSectionHeader "Index on """ & g_anPsOid & """,""" & g_anStatus & """,""" & _
                         g_anIsDeleted & """,""" & g_anHasBeenSetProductive & """,""" & g_anOid & """ in table """ & qualTabName & """ (for SETPRODUCTIVE)", fileNo
        
      If indexExcp(qualIndexName, thisOrgIndex) = False Then
        Print #fileNo,
        Print #fileNo, addTab(0); "CREATE INDEX"
        Print #fileNo, addTab(1); qualIndexName
        Print #fileNo, addTab(0); "ON"
        Print #fileNo, addTab(1); qualTabName
        Print #fileNo, addTab(0); "("
        
        Print #fileNo, addTab(1); g_anPsOid; " ASC,"
        If Not .condenseData Then
          Print #fileNo, addTab(1); g_anStatus; " ASC,"
          Print #fileNo, addTab(1); g_anIsDeleted; " ASC,"
        End If
        Print #fileNo, addTab(1); g_anHasBeenSetProductive; " ASC,"
        If forGen Then
          Print #fileNo, addTab(1); g_anOid; " ASC,"
          Print #fileNo, addTab(1); genSurrogateKeyName(ddlType, .shortName); " ASC"
        Else
          Print #fileNo, addTab(1); g_anOid; " ASC"
        End If
        
        Print #fileNo, addTab(0); ")"
        Print #fileNo, addTab(0); gc_sqlCmdDelim
      End If ' indexExcp
    End If
' ### ENDIF IVK ###
    
    If Not forNl Then
      For i = 1 To .indexRefs.numRefs
        colList = ""
        colListIncluded = ""
        With g_indexes.descriptors(.indexRefs.refs(i))
          If (.specificToPools <> "") Then
            If (thisPoolIndex < 1) Then
              GoTo NextI
            ElseIf Not includedInList(.specificToPools, g_pools.descriptors(thisPoolIndex).id) Then
              GoTo NextI
            End If
          End If
          
          If (.forGen <> forGen) Then
            GoTo NextI
          End If
          
          If .specificToQueryTables Then
            If poolSupportLrt Then
              If g_classes.descriptors(classIndex).useMqtToImplementLrt Then
                If Not forMqt Then
                  GoTo NextI
                End If
              End If
            End If
          End If
          
          If .attrRefs.numRefs > 0 Then
            For j = 1 To .attrRefs.numRefs
              Dim thisColName As String
              Dim extraColName As String
              thisColName = ""
              extraColName = ""
              If g_indexAttrs.descriptors(.attrRefs.refs(j)).attrRef > 0 Then
' ### IF IVK ###
                With g_attributes.descriptors(g_indexAttrs.descriptors(.attrRefs.refs(j)).attrRef)
                  Dim isGenAttr As Boolean
                  isGenAttr = .isTimeVarying
                  If .cType = eactClass And .acmEntityIndex > 0 Then
                    With g_classes.descriptors(.acmEntityIndex)
                      If .hasNoIdentity Then
                        isGenAttr = False
                      End If
                    End With
                  End If
                  If forGen = isGenAttr Then
                    thisColName = .dbColName(ddlType)
                    If .groupIdBasedOn <> "" And Not g_classes.descriptors(classIndex).hasOwnTable Then
                      extraColName = g_anCid
                    End If
                  End If
                End With
' ### ENDIF IVK ###
              ElseIf g_indexAttrs.descriptors(.attrRefs.refs(j)).attrRef < 0 Then
                ' meta attribute such as 'CLASSID'
                thisColName = g_indexAttrs.descriptors(.attrRefs.refs(j)).attrName
              ElseIf g_indexAttrs.descriptors(.attrRefs.refs(j)).relRef > 0 And Not forGen Then
                If g_indexAttrs.descriptors(.attrRefs.refs(j)).relRefDirection = etLeft Then
                  With g_relationships.descriptors(g_indexAttrs.descriptors(.attrRefs.refs(j)).relRef)
                    If g_classes.descriptors(.rightEntityIndex).useSurrogateKey Then
                      thisColName = .rightFkColName(ddlType)
                    Else
                      thisColName = getPkAttrListByClass(.rightEntityIndex, ddlType)
                    End If
                  End With
                Else
                  With g_relationships.descriptors(g_indexAttrs.descriptors(.attrRefs.refs(j)).relRef)
                    If g_classes.descriptors(.leftEntityIndex).useSurrogateKey Then
                      thisColName = .leftFkColName(ddlType)
                    Else
                      thisColName = getPkAttrListByClass(.leftEntityIndex, ddlType)
                    End If
                  End With
                End If
              End If
              
' ### IF IVK ###
              If thisColName <> "" And (thisColName <> g_anIsDeleted Or forMqt) Then
' ### ELSE IVK ###
'             If thisColName <> "" And forMqt Then
' ### ENDIF IVK ###
                With g_indexAttrs.descriptors(g_indexes.descriptors(g_classes.descriptors(classIndex).indexRefs.refs(i)).attrRefs.refs(j))
                  If .attrIsIncluded Then
                    colListIncluded = colListIncluded & IIf(colListIncluded = "", "", "," & vbCrLf) & addTab(1) & thisColName & IIf(.isAsc, " ASC", " DESC")
                  Else
                    colList = colList & IIf(colList = "", "", "," & vbCrLf) & addTab(1) & thisColName & IIf(.isAsc, " ASC", " DESC")
                    If extraColName <> "" Then
                      colList = colList & IIf(colList = "", "", "," & vbCrLf) & addTab(1) & extraColName & " ASC"
                    End If
                  End If
                End With
              End If
            Next j
            
            printSectionHeader "Index """ & .indexName & """ for " & IIf(g_classes.descriptors(classIndex).notAcmRelated, "table", "ACM class") & " """ & .sectionName & "." & .className & """", fileNo
            
            qualIndexName = genQualIndexName(.sectionIndex, .indexName, .shortName, ddlType, thisOrgIndex, thisPoolIndex, forGen, forLrt, forNl, forMqt)
            If indexExcp(qualIndexName, thisOrgIndex) = False Then
              Print #fileNo,
              Print #fileNo, addTab(0); "CREATE " & IIf(.isUnique And Not noConstraints And Not forMqt, "UNIQUE ", "") & "INDEX"
              Print #fileNo, addTab(1); qualIndexName
              Print #fileNo, addTab(0); "ON"
              Print #fileNo, addTab(1); qualTabName
              Print #fileNo, addTab(0); "("
              Print #fileNo, addTab(0); colList; IIf(forLrt Or forMqt, ",", "")
                
              If forMqt Then
                Print #fileNo, addTab(1); g_anIsLrtPrivate; " ASC"; IIf(forLrt, ",", "")
              End If
              If forLrt Then
                Print #fileNo, addTab(1); g_anInLrt; " ASC,"
                Print #fileNo, addTab(1); g_anLrtState; " ASC"
              End If
                
              Print #fileNo, addTab(0); ")"
              
              If colListIncluded <> "" Then
                Print #fileNo, addTab(0); "INCLUDE"
                Print #fileNo, addTab(0); "("
                Print #fileNo, addTab(0); colListIncluded
                Print #fileNo, addTab(0); ")"
              End If
              
              Print #fileNo, addTab(0); gc_sqlCmdDelim
            End If ' indexExcp
          End If
        End With
NextI:
      Next i
    End If
  End With
End Sub


Sub evalIndexes()
  Dim i As Integer, j As Integer
  With g_indexes
    Dim enumDescr As EnumDescriptor
    
    For i = 1 To .numDescriptors Step 1
      With .descriptors(i)
        ' determine references to index attributes
        g_indexes.descriptors(i).attrRefs.numRefs = 0
        .sectionIndex = getSectionIndexByName(.sectionName)
        For j = 1 To g_indexAttrs.numDescriptors Step 1
          With g_indexAttrs.descriptors(j)
            If UCase(g_indexes.descriptors(i).sectionName) = UCase(.sectionName) And _
               g_indexes.descriptors(i).cType = .cType And _
               UCase(g_indexes.descriptors(i).indexName) = UCase(.indexName) Then
              ' verify that .className corresponds to some sub-class of the indexes .classname
              Dim foundMatch As Boolean
              If .cType = eactClass Then
                If UCase(g_indexes.descriptors(i).className) = UCase(.className) Then
                  foundMatch = True
                Else
                  foundMatch = False
                  Dim thisClassIndex As Integer
                  thisClassIndex = getClassIndexByName(.sectionName, g_indexes.descriptors(i).className)
                  With g_classes.descriptors(thisClassIndex)
                    Dim k As Integer
                    For k = 1 To UBound(.subclassIndexesRecursive)
                      If g_classes.descriptors(.subclassIndexesRecursive(k)).className = g_indexAttrs.descriptors(j).className Then
                        foundMatch = True
                      End If
                    Next k
                  End With
                End If
              Else
                foundMatch = True
              End If
              If foundMatch Then
                allocIndexAttrDescriptorRefIndex g_indexes.descriptors(i).attrRefs
                g_indexes.descriptors(i).attrRefs.refs(g_indexes.descriptors(i).attrRefs.numRefs) = j
              End If
            End If
          End With
        Next j
      End With
    Next i
  End With
End Sub



