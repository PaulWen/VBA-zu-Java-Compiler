 Attribute VB_Name = "M96_DdlSummary"
 Private Const colRowNum = 2
 Private Const colSchemaName = 3
 
 Private Const colTabName = 4
 Private Const colTabNotAcmRelated = colTabName + 1
 Private Const colAttrName = colTabNotAcmRelated + 1
 Private Const colAttrNameReserved = colAttrName + 1
 Private Const colDBType = colAttrNameReserved + 1
 Private Const colLength = colDBType + 1
 Private Const colSpecifics = colLength + 1
 
 Private Const colFirst = colTabName
 Private Const colLast = colSpecifics
 Private Const numCols = colSpecifics
 
 Private Const firstRow = 1
 
 Private activeRow As Integer
 Private didPrintTabName As Boolean
 
 Private thisTabName As String
 Private thisSchemaName As String
 Private thisNotAcmRelated As Boolean
 
 
 Private Sub initVars()
   If activeRow > 0 Then
     Exit Sub
   End If

   activeRow = firstRow
   didPrintTabName = False
   thisTabName = ""
   thisSchemaName = ""
   thisNotAcmRelated = True
 End Sub
 
 
 Sub resetDdl()
   activeRow = 0
 End Sub
 
 
 Sub addAttrToDdlSummary( _
   ByRef attrName As String, _
   ByRef dataType As String, _
   ByRef length As String, _
   ByRef specifics As String, _
   ddlType As DdlTypeId _
 )
   If ddlType <> edtLdm Then
     Exit Sub
   End If

   initVars

   Dim legalErroNo As Integer
   legalErroNo = 9
   On Error GoTo ErrorExit
   Dim thisSheet As Worksheet
   Set thisSheet = ActiveWorkbook.Sheets(g_sheetNameDdlSummary)
   legalErroNo = -999
   If Not didPrintTabName Then
     thisSheet.Cells(activeRow, colTabName) = thisTabName
     thisSheet.Cells(activeRow, colTabNotAcmRelated) = IIf(thisNotAcmRelated, "1", "")
     didPrintTabName = True
   End If

   thisSheet.Cells(activeRow, colAttrName) = attrName
   thisSheet.Cells(activeRow, colDBType) = dataType
   If length <> "" Then
     thisSheet.Cells(activeRow, colLength) = length
   End If
   thisSheet.Cells(activeRow, colSpecifics) = specifics
   thisSheet.Cells(activeRow, colRowNum) = activeRow
   thisSheet.Cells(activeRow, 1) = getSectionSeqNoByName(thisSchemaName)
   thisSheet.Cells(activeRow, colSchemaName) = thisSchemaName
   thisSheet.Cells(activeRow, colTabName).Select
   activeRow = activeRow + 1
 
 NormalExit:
   Exit Sub
 
 ErrorExit:
   If Err.Number <> legalErroNo Then
     Err.Raise Err.Number, Err.Source, Err.description, Err.HelpFile, Err.HelpContext
   End If
 End Sub
 
 
 Sub addTabToDdlSummary( _
   ByRef tabName As String, _
   ddlType As DdlTypeId, _
   notAcmRelated As Boolean _
 )
   If ddlType <> edtLdm Then
     Exit Sub
   End If

   initVars

   didPrintTabName = False
   thisTabName = getUnqualObjName(tabName)
   thisSchemaName = getSchemaName(tabName)
   thisNotAcmRelated = notAcmRelated
 End Sub
