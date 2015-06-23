package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M78_TabCfg_Utilities {




class TabCfgParamDescriptor {
public int sequenceNumber;
public String schemaPattern;
public String namePattern;
public String schemaPatternExcluded;
public String namePatternExcluded;
public int pctFree;
public Integer isVolatile;
public Integer useRowCompression;
public Integer useIndexCompression;

public TabCfgParamDescriptor(int sequenceNumber, String schemaPattern, String namePattern, String schemaPatternExcluded, String namePatternExcluded, int pctFree, Integer isVolatile, Integer useRowCompression, Integer useIndexCompression) {
this.sequenceNumber = sequenceNumber;
this.schemaPattern = schemaPattern;
this.namePattern = namePattern;
this.schemaPatternExcluded = schemaPatternExcluded;
this.namePatternExcluded = namePatternExcluded;
this.pctFree = pctFree;
this.isVolatile = isVolatile;
this.useRowCompression = useRowCompression;
this.useIndexCompression = useIndexCompression;
}
}

class TabCfgParamDescriptors {
public M78_TabCfg_Utilities.TabCfgParamDescriptor[] descriptors;
public int numDescriptors;

public TabCfgParamDescriptors(int numDescriptors, M78_TabCfg_Utilities.TabCfgParamDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initTabCfgParamDescriptors(M78_TabCfg_Utilities.TabCfgParamDescriptors tabCfgs) {
tabCfgs.numDescriptors = 0;
}


public static Integer allocTabCfgParamDescriptorIndex(M78_TabCfg_Utilities.TabCfgParamDescriptors tabCfgs) {
Integer returnValue;
returnValue = -1;

if (tabCfgs.numDescriptors == 0) {
tabCfgs.descriptors =  new M78_TabCfg_Utilities.TabCfgParamDescriptor[M01_Common.gc_allocBlockSize];
} else if (tabCfgs.numDescriptors >= M00_Helper.uBound(tabCfgs.descriptors)) {
M78_TabCfg_Utilities.TabCfgParamDescriptor[] descriptorsBackup = tabCfgs.descriptors;
tabCfgs.descriptors =  new M78_TabCfg_Utilities.TabCfgParamDescriptor[tabCfgs.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M78_TabCfg_Utilities.TabCfgParamDescriptor value : descriptorsBackup) {
tabCfgs.descriptors[indexCounter] = value;
indexCounter++;
}
}
tabCfgs.numDescriptors = tabCfgs.numDescriptors + 1;
returnValue = tabCfgs.numDescriptors;
return returnValue;
}


}