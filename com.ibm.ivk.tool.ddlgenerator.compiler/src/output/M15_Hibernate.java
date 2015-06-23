package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M15_Hibernate {


// ### IF IVK ###



public static void genHCfgForClass(int classIndex, int fileNoHCfg, Integer ddlTypeW, Boolean forGenW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = M01_Common.DdlTypeId.edtLdm;
} else {
ddlType = ddlTypeW;
}

boolean forGen; 
if (forGenW == null) {
forGen = false;
} else {
forGen = forGenW;
}

M00_FileWriter.printToFile(fileNoHCfg, "<?xml version=\"1.0\"?>");
M00_FileWriter.printToFile(fileNoHCfg, "<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\"");
M00_FileWriter.printToFile(fileNoHCfg, "\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">");
M00_FileWriter.printToFile(fileNoHCfg, "<!-- Generated " + new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()) + " ; by \"Ludger's Magic Tools\" 1.3.7 -->");
M00_FileWriter.printToFile(fileNoHCfg, " < Hibernate - mapping > ");

M00_FileWriter.printToFile(fileNoHCfg, M04_Utilities.addTab(1) + "<class name=\"com.dcx.ivkmds.Common.bo.persistent." + M22_Class.g_classes.descriptors[classIndex].sectionName + "." + M22_Class.g_classes.descriptors[classIndex].className.toUpperCase() + "\"");
M00_FileWriter.printToFile(fileNoHCfg, " table=\"" + M04_Utilities.genQualTabNameByClassIndex(M22_Class.g_classes.descriptors[classIndex].orMappingSuperClassIndex, ddlType, null, null, null, null, null, null, null, null, null) + "\"");
M00_FileWriter.printToFile(fileNoHCfg, " schema=\"" + M22_Class.g_classes.descriptors[classIndex].sectionName.toUpperCase() + "\"");
M00_FileWriter.printToFile(fileNoHCfg, (M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].orMappingSuperClassIndex].hasSubClass ? " discriminator - value = \"" + M22_Class.g_classes.descriptors[M22_Class.g_classes.descriptors[classIndex].orMappingSuperClassIndex].classIdStr + "\"" : ""));
M00_FileWriter.printToFile(fileNoHCfg, ">");
M00_FileWriter.printToFile(fileNoHCfg, "");

M24_Attribute.genAttrListForClassRecursive(classIndex, fileNoHCfg, ddlType, null, 2, null, null, M01_Common.DdlOutputMode.edomMapHibernate, M01_Common.RecursionDirection.erdUp, null);

M00_FileWriter.printToFile(fileNoHCfg, "");

M00_FileWriter.printToFile(fileNoHCfg, M04_Utilities.addTab(1) + "</class>");
M00_FileWriter.printToFile(fileNoHCfg, "</hibernate-mapping>");
M00_FileWriter.printToFile(fileNoHCfg, "");
}

// ### ENDIF IVK ###

}