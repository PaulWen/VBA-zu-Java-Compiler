package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M24_Attribute_Utilities_NL {




class AttributeNlDescriptor {
public String i18nId;

public String[] nl;

// derived attributes
public String attributeIndex;

public AttributeNlDescriptor(String i18nId, String attributeIndex, String[] nl) {
this.i18nId = i18nId;
this.attributeIndex = attributeIndex;
this.nl = nl;
}
}

class AttributeNlDescriptors {
public M24_Attribute_Utilities_NL.AttributeNlDescriptor[] descriptors;
public int numDescriptors;

public AttributeNlDescriptors(int numDescriptors, M24_Attribute_Utilities_NL.AttributeNlDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}




public static Integer allocAttributeNlDescriptorIndex(M24_Attribute_Utilities_NL.AttributeNlDescriptors attributeNls) {
Integer returnValue;
returnValue = -1;

if (M24_Attribute_NL.numLangsForAttributesNl > 0) {
if (attributeNls.numDescriptors == 0) {
attributeNls.descriptors =  new M24_Attribute_Utilities_NL.AttributeNlDescriptor[M01_Common.gc_allocBlockSize];
} else if (attributeNls.numDescriptors >= M00_Helper.uBound(attributeNls.descriptors)) {
M24_Attribute_Utilities_NL.AttributeNlDescriptor[] descriptorsBackup = attributeNls.descriptors;
attributeNls.descriptors =  new M24_Attribute_Utilities_NL.AttributeNlDescriptor[attributeNls.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M24_Attribute_Utilities_NL.AttributeNlDescriptor value : descriptorsBackup) {
attributeNls.descriptors[indexCounter] = value;
indexCounter++;
}
}
attributeNls.numDescriptors = attributeNls.numDescriptors + 1;
attributeNls.descriptors[attributeNls.numDescriptors].nl =  new String[M24_Attribute_NL.numLangsForAttributesNl];
returnValue = attributeNls.numDescriptors;
}
return returnValue;
}



}