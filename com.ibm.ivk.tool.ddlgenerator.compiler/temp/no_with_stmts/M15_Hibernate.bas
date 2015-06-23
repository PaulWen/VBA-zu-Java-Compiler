 Attribute VB_Name = "M15_Hibernate"
 ' ### IF IVK ###
 Option Explicit
 
 
 Sub genHCfgForClass( _
   ByRef classIndex As Integer, _
   fileNoHCfg As Integer, _
   Optional ddlType As DdlTypeId = edtLdm, _
   Optional forGen As Boolean = False _
 )
     Print #fileNoHCfg, "<?xml version=""1.0""?>"
     Print #fileNoHCfg, "<!DOCTYPE hibernate-mapping PUBLIC ""-//Hibernate/Hibernate Mapping DTD 3.0//EN"""
     Print #fileNoHCfg, """http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd"">"
     Print #fileNoHCfg, "<!-- Generated "; Now; " ; by ""Ludger's Magic Tools"" 1.3.7 -->"
     Print #fileNoHCfg, " < Hibernate - mapping > "

     Print #fileNoHCfg, addTab(1); "<class name=""com.dcx.ivkmds.Common.bo.persistent."; UCase(g_classes.descriptors(classIndex).sectionName & "." & g_classes.descriptors(classIndex).className); """";
     Print #fileNoHCfg, " table="""; genQualTabNameByClassIndex(g_classes.descriptors(classIndex).orMappingSuperClassIndex, ddlType); """";
     Print #fileNoHCfg, " schema="""; UCase(g_classes.descriptors(classIndex).sectionName); """";
       Print #fileNoHCfg, IIf(g_classes.descriptors(g_classes.descriptors(classIndex).orMappingSuperClassIndex).hasSubClass, " discriminator - value = """ & g_classes.descriptors(g_classes.descriptors(classIndex).orMappingSuperClassIndex).classIdStr & """", "");
     Print #fileNoHCfg, ">"
     Print #fileNoHCfg,

     genAttrListForClassRecursive classIndex, fileNoHCfg, ddlType, , 2, , , edomMapHibernate, erdUp

     Print #fileNoHCfg,

     Print #fileNoHCfg, addTab(1); "</class>"
     Print #fileNoHCfg, "</hibernate-mapping>"
     Print #fileNoHCfg,
 End Sub
 
 ' ### ENDIF IVK ###
