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

     If values.numVals = 0 Then
       ReDim values.vals(1 To gc_allocBlockSize)
     ElseIf values.numVals >= UBound(values.vals) Then
       ReDim Preserve values.vals(1 To values.numVals + gc_allocBlockSize)
     End If
     values.numVals = values.numVals + 1
     allocEnumValIndex = values.numVals
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

     If enums.numDescriptors = 0 Then
       ReDim enums.descriptors(1 To gc_allocBlockSize)
     ElseIf enums.numDescriptors >= UBound(enums.descriptors) Then
       ReDim Preserve enums.descriptors(1 To enums.numDescriptors + gc_allocBlockSize)
     End If
     enums.numDescriptors = enums.numDescriptors + 1
     allocEnumDescriptorIndex = enums.numDescriptors
 End Function
 
 Function getEnumIdByIndex( _
   thisEnumIndex As Integer _
 ) As String
   getEnumIdByIndex = ""
 
   If thisEnumIndex > 0 Then
       If g_enums.descriptors(thisEnumIndex).enumId > 0 Then
         getEnumIdByIndex = Right("00" & getSectionSeqNoByIndex(g_enums.descriptors(thisEnumIndex).sectionIndex), 2) & Right("000" & g_enums.descriptors(thisEnumIndex).enumId, 3)
       End If
   End If
 End Function
 
