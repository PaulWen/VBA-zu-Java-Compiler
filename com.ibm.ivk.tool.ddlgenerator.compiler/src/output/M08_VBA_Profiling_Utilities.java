package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M08_VBA_Profiling_Utilities {




class ProfLevelDescriptor {
public String moduleName;
public String procName;
public int level;

public ProfLevelDescriptor(String moduleName, String procName, int level) {
this.moduleName = moduleName;
this.procName = procName;
this.level = level;
}
}

class ProfLevelDescriptors {
public M08_VBA_Profiling_Utilities.ProfLevelDescriptor[] descriptors;
public int numDescriptors;

public ProfLevelDescriptors(int numDescriptors, M08_VBA_Profiling_Utilities.ProfLevelDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static Integer allocProfLevelDescriptorIndex(M08_VBA_Profiling_Utilities.ProfLevelDescriptors plds) {
Integer returnValue;
returnValue = -1;

if (plds.numDescriptors == 0) {
plds.descriptors =  new M08_VBA_Profiling_Utilities.ProfLevelDescriptor[M01_Common.gc_allocBlockSize];
} else if (plds.numDescriptors >= M00_Helper.uBound(plds.descriptors)) {
M08_VBA_Profiling_Utilities.ProfLevelDescriptor[] descriptorsBackup = plds.descriptors;
plds.descriptors =  new M08_VBA_Profiling_Utilities.ProfLevelDescriptor[plds.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M08_VBA_Profiling_Utilities.ProfLevelDescriptor value : descriptorsBackup) {
plds.descriptors[indexCounter] = value;
indexCounter++;
}
}
plds.numDescriptors = plds.numDescriptors + 1;
returnValue = plds.numDescriptors;
return returnValue;
}





}