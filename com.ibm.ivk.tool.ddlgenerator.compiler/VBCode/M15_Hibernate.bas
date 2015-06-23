Attribute VB_Name = "M15_Hibernate"
' ### IF IVK ###
Option Explicit


Sub genHCfgForClass( _
  ByRef classIndex As Integer, _
  fileNoHCfg As Integer, _
  Optional ddlType As DdlTypeId = edtLdm, _
  Optional forGen As Boolean = False _
)
  With g_classes.descriptors(classIndex)
    Print #fileNoHCfg, "<?xml version=""1.0""?>"
    Print #fileNoHCfg, "<!DOCTYPE hibernate-mapping PUBLIC ""-//Hibernate/Hibernate Mapping DTD 3.0//EN"""
    Print #fileNoHCfg, """http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd"">"
    Print #fileNoHCfg, "<!-- Generated "; Now; " ; by ""Ludger's Magic Tools"" 1.3.7 -->"
    Print #fileNoHCfg, " < Hibernate - mapping > "
    
    Print #fileNoHCfg, addTab(1); "<class name=""com.dcx.ivkmds.Common.bo.persistent."; UCase(.sectionName & "." & .className); """";
    Print #fileNoHCfg, " table="""; genQualTabNameByClassIndex(.orMappingSuperClassIndex, ddlType); """";
    Print #fileNoHCfg, " schema="""; UCase(.sectionName); """";
    With g_classes.descriptors(.orMappingSuperClassIndex)
      Print #fileNoHCfg, IIf(.hasSubClass, " discriminator - value = """ & .classIdStr & """", "");
    End With
    Print #fileNoHCfg, ">"
    Print #fileNoHCfg,
    
    genAttrListForClassRecursive classIndex, fileNoHCfg, ddlType, , 2, , , edomMapHibernate, erdUp
    
    Print #fileNoHCfg,
  
    Print #fileNoHCfg, addTab(1); "</class>"
    Print #fileNoHCfg, "</hibernate-mapping>"
    Print #fileNoHCfg,
  End With
End Sub

' ### ENDIF IVK ###
