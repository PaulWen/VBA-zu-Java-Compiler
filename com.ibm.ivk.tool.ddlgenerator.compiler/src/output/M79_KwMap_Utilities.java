package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_KwMap_Utilities {




class KwMapDescriptor {
public String keyword;
public String value;

public KwMapDescriptor(String keyword, String value) {
this.keyword = keyword;
this.value = value;
}
}

class KwMapDescriptors {
public M79_KwMap_Utilities.KwMapDescriptor[] descriptors;
public int numDescriptors;

public KwMapDescriptors(int numDescriptors, M79_KwMap_Utilities.KwMapDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initKwMapDescriptors(M79_KwMap_Utilities.KwMapDescriptors kwMap) {
kwMap.numDescriptors = 0;
}


public static Integer allocKwMapDescriptorIndex(M79_KwMap_Utilities.KwMapDescriptors kwMap) {
Integer returnValue;
returnValue = -1;

if (kwMap.numDescriptors == 0) {
kwMap.descriptors =  new M79_KwMap_Utilities.KwMapDescriptor[M01_Common.gc_allocBlockSize];
} else if (kwMap.numDescriptors >= M00_Helper.uBound(kwMap.descriptors)) {
M79_KwMap_Utilities.KwMapDescriptor[] descriptorsBackup = kwMap.descriptors;
kwMap.descriptors =  new M79_KwMap_Utilities.KwMapDescriptor[kwMap.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M79_KwMap_Utilities.KwMapDescriptor value : descriptorsBackup) {
kwMap.descriptors[indexCounter] = value;
indexCounter++;
}
}
kwMap.numDescriptors = kwMap.numDescriptors + 1;
returnValue = kwMap.numDescriptors;
return returnValue;
}


}