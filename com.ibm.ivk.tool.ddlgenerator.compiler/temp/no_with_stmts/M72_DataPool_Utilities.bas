 Attribute VB_Name = "M72_DataPool_Utilities"
 Option Explicit
 
 Type DataPoolDescriptor
   id As Integer
   name As String
   shortName As String
   specificToOrgId As Integer
   supportLrt As Boolean
 ' ### IF IVK ###
   supportViewsForPsTag As Boolean
   supportTriggerForPsTag As Boolean
   supportXmlExport As Boolean
 ' ### ENDIF IVK ###
   supportUpdates As Boolean
   suppressRefIntegrity As Boolean
   suppressUniqueConstraints As Boolean
 ' ### IF IVK ###
   instantiateExpressions As Boolean
 ' ### ENDIF IVK ###
   commonItemsLocal As Boolean
   supportAcm As Boolean
   isActive As Boolean
 ' ### IF IVK ###
   isProductive As Boolean
   isArchive As Boolean
   supportNationalization As Boolean
 ' ### ENDIF IVK ###
   sequenceCacheSize As Integer
 End Type
 
 Type DataPoolDescriptors
   descriptors() As DataPoolDescriptor
   numDescriptors As Integer
 End Type
 
 
 Sub initDataPoolDescriptors( _
   ByRef pools As DataPoolDescriptors _
 )
   pools.numDescriptors = 0
 End Sub
 
 Function allocDataPoolIndex( _
   ByRef pools As DataPoolDescriptors _
 ) As Integer
   allocDataPoolIndex = -1

     If pools.numDescriptors = 0 Then
       ReDim pools.descriptors(1 To gc_allocBlockSize)
     ElseIf pools.numDescriptors >= UBound(pools.descriptors) Then
       ReDim Preserve pools.descriptors(1 To pools.numDescriptors + gc_allocBlockSize)
     End If
     pools.numDescriptors = pools.numDescriptors + 1
     allocDataPoolIndex = pools.numDescriptors
 End Function
 
 
 Function getEffectivePoolId( _
   thisPoolId As Integer, _
   isCommon As Boolean _
 ) As Integer
   getEffectivePoolId = IIf(isCommon, -1, thisPoolId)
 End Function
 
 
 Function getEffectivePoolIndex( _
   ByVal thisPoolIndex As Integer, _
   isCommon As Boolean _
 ) As Integer
   getEffectivePoolIndex = IIf(isCommon, -1, thisPoolIndex)
 End Function
 
 
 ' ### IF IVK ###
 Function getMigDataPoolIndex() As Integer
   getMigDataPoolIndex = -1

   Dim i As Integer
   For i = 1 To g_pools.numDescriptors
         ' FIXME
     If g_pools.descriptors(i).id = 0 Then
       getMigDataPoolIndex = i
       Exit Function
     End If
   Next i
 End Function
 
 
 Function getMigDataPoolId() As Integer
   getMigDataPoolId = 0 ' FIXME
 End Function
 
 
 ' ### ENDIF IVK ###
 Function getWorkDataPoolIndex() As Integer
   getWorkDataPoolIndex = -1

   Dim i As Integer
   For i = 1 To g_pools.numDescriptors
     If g_pools.descriptors(i).supportLrt Then
       getWorkDataPoolIndex = i
       Exit Function
     End If
   Next i
 End Function
 
 
 Function getWorkDataPoolId() As Integer
   getWorkDataPoolId = -1

   Dim i As Integer
   For i = 1 To g_pools.numDescriptors
       If g_pools.descriptors(i).supportLrt Then
         getWorkDataPoolId = g_pools.descriptors(i).id
         Exit Function
       End If
   Next i
 End Function
 
 
 ' ### IF IVK ###
 Function getProductiveDataPoolIndex() As Integer
   getProductiveDataPoolIndex = -1
   getDataPools

   Dim i As Integer
   For i = 1 To g_pools.numDescriptors
     If g_pools.descriptors(i).isProductive Then
       getProductiveDataPoolIndex = i
       Exit Function
     End If
   Next i
 End Function
 
 
 Function getProductiveDataPoolId() As Integer
   getProductiveDataPoolId = -1
   getDataPools

   Dim i As Integer
   For i = 1 To g_pools.numDescriptors
       If g_pools.descriptors(i).isProductive Then
         getProductiveDataPoolId = g_pools.descriptors(i).id
         Exit Function
       End If
   Next i
 End Function
 
 
 Function getArchiveDataPoolIndex() As Integer
   getArchiveDataPoolIndex = -1
   getDataPools

   Dim i As Integer
   For i = 1 To g_pools.numDescriptors
     If g_pools.descriptors(i).isArchive Then
       getArchiveDataPoolIndex = i
       Exit Function
     End If
   Next i
 End Function
 
 
 Function getArchiveDataPoolId() As Integer
   getArchiveDataPoolId = -1
   getDataPools

   Dim i As Integer
   For i = 1 To g_pools.numDescriptors
       If g_pools.descriptors(i).isArchive Then
         getArchiveDataPoolId = g_pools.descriptors(i).id
         Exit Function
       End If
   Next i
 End Function
 ' ### ENDIF IVK ###
 
