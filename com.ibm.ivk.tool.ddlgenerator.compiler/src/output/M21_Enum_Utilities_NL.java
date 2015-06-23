package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M21_Enum_Utilities_NL {




class EnumNlDescriptor {
public String i18nId;

public String[] nl;

// derived attributes
public String enumIndex;

public EnumNlDescriptor(String i18nId, String enumIndex, String[] nl) {
this.i18nId = i18nId;
this.enumIndex = enumIndex;
this.nl = nl;
}
}

class EnumNlDescriptors {
public M21_Enum_Utilities_NL.EnumNlDescriptor[] descriptors;
public int numDescriptors;

public EnumNlDescriptors(int numDescriptors, M21_Enum_Utilities_NL.EnumNlDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}

public static Integer allocEnumNlDescriptorIndex(M21_Enum_Utilities_NL.EnumNlDescriptors enumNls) {
Integer returnValue;
returnValue = -1;

if (M21_Enum_NL.numLangsForEnumsNl > 0) {
if (enumNls.numDescriptors == 0) {
enumNls.descriptors =  new M21_Enum_Utilities_NL.EnumNlDescriptor[M01_Common.gc_allocBlockSize];
} else if (enumNls.numDescriptors >= M00_Helper.uBound(enumNls.descriptors)) {
M21_Enum_Utilities_NL.EnumNlDescriptor[] descriptorsBackup = enumNls.descriptors;
enumNls.descriptors =  new M21_Enum_Utilities_NL.EnumNlDescriptor[enumNls.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M21_Enum_Utilities_NL.EnumNlDescriptor value : descriptorsBackup) {
enumNls.descriptors[indexCounter] = value;
indexCounter++;
}
}
enumNls.numDescriptors = enumNls.numDescriptors + 1;
enumNls.descriptors[enumNls.numDescriptors].nl =  new String[M21_Enum_NL.numLangsForEnumsNl];
returnValue = enumNls.numDescriptors;
}
return returnValue;
}


}