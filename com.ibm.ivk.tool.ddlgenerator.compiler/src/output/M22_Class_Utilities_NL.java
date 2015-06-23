package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M22_Class_Utilities_NL {




class ClassNlDescriptor {
public String i18nId;

public String[] nl;

// derived attributes
public String classIndex;

public ClassNlDescriptor(String i18nId, String classIndex, String[] nl) {
this.i18nId = i18nId;
this.classIndex = classIndex;
this.nl = nl;
}
}

class ClassNlDescriptors {
public M22_Class_Utilities_NL.ClassNlDescriptor[] descriptors;
public int numDescriptors;

public ClassNlDescriptors(int numDescriptors, M22_Class_Utilities_NL.ClassNlDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}




public static Integer allocClassNlDescriptorIndex(M22_Class_Utilities_NL.ClassNlDescriptors classNls) {
Integer returnValue;
returnValue = -1;

if (M22_Class_NL.numLangsForClassesNl > 0) {
if (classNls.numDescriptors == 0) {
classNls.descriptors =  new M22_Class_Utilities_NL.ClassNlDescriptor[M01_Common.gc_allocBlockSize];
} else if (classNls.numDescriptors >= M00_Helper.uBound(classNls.descriptors)) {
M22_Class_Utilities_NL.ClassNlDescriptor[] descriptorsBackup = classNls.descriptors;
classNls.descriptors =  new M22_Class_Utilities_NL.ClassNlDescriptor[classNls.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M22_Class_Utilities_NL.ClassNlDescriptor value : descriptorsBackup) {
classNls.descriptors[indexCounter] = value;
indexCounter++;
}
}
classNls.numDescriptors = classNls.numDescriptors + 1;
classNls.descriptors[classNls.numDescriptors].nl =  new String[M22_Class_NL.numLangsForClassesNl];
returnValue = classNls.numDescriptors;
}
return returnValue;
}


public static String getPrimaryClassLabelByIndex(int classIndex) {
String returnValue;
returnValue = "<unknown class>";
int i;
int langId;
int minLangId;
minLangId = 9999;

if (classIndex > 0) {
if (M22_Class.g_classes.descriptors[classIndex].classNlIndex > 0) {
if (!(M04_Utilities.strArrayIsNull(M22_Class_NL.g_classesNl.descriptors[M22_Class.g_classes.descriptors[classIndex].classNlIndex].nl))) {
for (int langId = M00_Helper.lBound(M22_Class_NL.g_classesNl.descriptors[M22_Class.g_classes.descriptors[classIndex].classNlIndex].nl); langId <= M00_Helper.uBound(M22_Class_NL.g_classesNl.descriptors[M22_Class.g_classes.descriptors[classIndex].classNlIndex].nl); langId++) {
if (M22_Class_NL.g_classesNl.descriptors[M22_Class.g_classes.descriptors[classIndex].classNlIndex].nl[langId] != "") {
if (langId == M01_Globals_IVK.gc_langIdEnglish) {
returnValue = M22_Class_NL.g_classesNl.descriptors[M22_Class.g_classes.descriptors[classIndex].classNlIndex].nl[langId];
return returnValue;
}
if (langId.compareTo(minLangId) < 0) {
returnValue = M22_Class_NL.g_classesNl.descriptors[M22_Class.g_classes.descriptors[classIndex].classNlIndex].nl[langId];
minLangId = langId;
}
}
}
}
}
}
return returnValue;
}


}