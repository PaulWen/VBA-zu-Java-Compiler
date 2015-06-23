Attribute VB_Name = "M21_Enum_Utilities"
Option Explicit

Global Const maxAttrsPerEnum = 15

Type EnumVal
  id As Integer
  oid As Integer
  languageId As Integer
  valueString As String
  
  isOrgSpecific As Boolean
  
  attrStrings(1 To maxAttrsPerEnum) As String
End Type

Type EnumVals
  vals() As EnumVal
  numVals As Integer
End Type

Type EnumDescriptor
  sectionName As String
  enumName As String
  i18nId As String
  shortName As String
  isEnumLang As Boolean
  idDomainSection As String
  idDomainName As String
  maxLength As Integer
  isCommonToOrgs As Boolean
  isCommonToPools As Boolean
  enumId As Integer
  notAcmRelated As Boolean
  noAlias As Boolean
' ### IF IVK ###
  noXmlExport As Boolean
  useXmlExport As Boolean
' ### ENDIF IVK ###
  isLrtSpecific As Boolean
  isPdmSpecific As Boolean
  refersToPdm As Boolean
  
  tabSpaceData As String
  tabSpaceLong As String
  tabSpaceNl As String
  tabSpaceIndex As String
  
  values As EnumVals

  ' derived attributes
  enumIdStr As String
  enumIndex As Integer
  enumNameDb As String
  idDataType As typeId
  domainIndexId As Integer
  domainIndexValue As Integer
  sectionIndex As Integer
  sectionShortName As String
  attrRefs As AttrDescriptorRefs
  tabSpaceIndexData As Integer
  tabSpaceIndexIndex As Integer
  tabSpaceIndexLong As Integer
  tabSpaceIndexNl As Integer
' ### IF IVK ###
  supportXmlExport As Boolean
' ### ENDIF IVK ###

  ' temporary variables supporting processing
  isLdmCsvExported As Boolean
' ### IF IVK ###
  isXsdExported As Boolean
' ### ENDIF IVK ###
  isCtoAliasCreated As Boolean
End Type

Type EnumDescriptors
  descriptors() As EnumDescriptor
  numDescriptors As Integer
End Type


Function getEnumLangIndex() As Integer
  Dim i As Integer
  getEnumLangIndex = -1
  For i = 1 To g_enums.numDescriptors Step 1
    If g_enums.descriptors(i).isEnumLang Then
      getEnumLangIndex = i
      Exit Function
    End If
  Next i
End Function


Sub initEnumVals( _
  ByRef vals As EnumVals _
)
  vals.numVals = 0
End Sub


Function allocEnumValIndex( _
  ByRef values As EnumVals _
) As Integer
  allocEnumValIndex = -1
  
  With values
    If .numVals = 0 Then
      ReDim .vals(1 To gc_allocBlockSize)
    ElseIf .numVals >= UBound(.vals) Then
      ReDim Preserve .vals(1 To .numVals + gc_allocBlockSize)
    End If
    .numVals = .numVals + 1
    allocEnumValIndex = .numVals
  End With
End Function


Sub initEnumDescriptors( _
  ByRef enums As EnumDescriptors _
)
  enums.numDescriptors = 0
End Sub

Function allocEnumDescriptorIndex( _
  ByRef enums As EnumDescriptors _
) As Integer
  allocEnumDescriptorIndex = -1
  
  With enums
    If .numDescriptors = 0 Then
      ReDim .descriptors(1 To gc_allocBlockSize)
    ElseIf .numDescriptors >= UBound(.descriptors) Then
      ReDim Preserve .descriptors(1 To .numDescriptors + gc_allocBlockSize)
    End If
    .numDescriptors = .numDescriptors + 1
    allocEnumDescriptorIndex = .numDescriptors
  End With
End Function

Function getEnumIdByIndex( _
  thisEnumIndex As Integer _
) As String
  getEnumIdByIndex = ""

  If thisEnumIndex > 0 Then
    With g_enums.descriptors(thisEnumIndex)
      If .enumId > 0 Then
        getEnumIdByIndex = Right("00" & getSectionSeqNoByIndex(.sectionIndex), 2) & Right("000" & .enumId, 3)
      End If
    End With
  End If
End Function

