package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M26_Type_Utilities {


// ### IF IVK ###


class TypeDescriptor {
public String sectionName;
public String typeName;
public String shortName;
public String comment;

// derived attributes
public int typeIndex;
public int sectionIndex;
public M24_Attribute_Utilities.AttrDescriptorRefs attrRefs;

public TypeDescriptor(String sectionName, String typeName, String shortName, String comment, int typeIndex, int sectionIndex, M24_Attribute_Utilities.AttrDescriptorRefs attrRefs) {
this.sectionName = sectionName;
this.typeName = typeName;
this.shortName = shortName;
this.comment = comment;
this.typeIndex = typeIndex;
this.sectionIndex = sectionIndex;
this.attrRefs = attrRefs;
}
}

class TypeDescriptors {
public M26_Type_Utilities.TypeDescriptor[] descriptors;
public int numDescriptors;

public TypeDescriptors(int numDescriptors, M26_Type_Utilities.TypeDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initTypeDescriptors(M26_Type_Utilities.TypeDescriptors types) {
types.numDescriptors = 0;
}


public static Integer allocTypeDescriptorIndex(M26_Type_Utilities.TypeDescriptors types) {
Integer returnValue;
returnValue = -1;

if (types.numDescriptors == 0) {
types.descriptors =  new M26_Type_Utilities.TypeDescriptor[M01_Common.gc_allocBlockSize];
} else if (types.numDescriptors >= M00_Helper.uBound(types.descriptors)) {
M26_Type_Utilities.TypeDescriptor[] descriptorsBackup = types.descriptors;
types.descriptors =  new M26_Type_Utilities.TypeDescriptor[types.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M26_Type_Utilities.TypeDescriptor value : descriptorsBackup) {
types.descriptors[indexCounter] = value;
indexCounter++;
}
}
types.numDescriptors = types.numDescriptors + 1;
returnValue = types.numDescriptors;
return returnValue;
}
// ### ENDIF IVK ###


}