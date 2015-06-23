package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M78_DbCfg_Utilities {




class DbCfgParamDescriptor {
public String parameter;
public String value;
public boolean isDbmCfgParam;
public boolean isDbProfileParam;
public String serverPlatform;
public int sequenceNo;
public String minDbRelease;

public DbCfgParamDescriptor(String parameter, String value, boolean isDbmCfgParam, boolean isDbProfileParam, String serverPlatform, int sequenceNo, String minDbRelease) {
this.parameter = parameter;
this.value = value;
this.isDbmCfgParam = isDbmCfgParam;
this.isDbProfileParam = isDbProfileParam;
this.serverPlatform = serverPlatform;
this.sequenceNo = sequenceNo;
this.minDbRelease = minDbRelease;
}
}

class DbCfgParamDescriptors {
public M78_DbCfg_Utilities.DbCfgParamDescriptor[] descriptors;
public int numDescriptors;

public DbCfgParamDescriptors(int numDescriptors, M78_DbCfg_Utilities.DbCfgParamDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initDbCfgParamDescriptors(M78_DbCfg_Utilities.DbCfgParamDescriptors dbCfgs) {
dbCfgs.numDescriptors = 0;
}


public static Integer allocDbCfgParamDescriptorIndex(M78_DbCfg_Utilities.DbCfgParamDescriptors dbCfgs) {
Integer returnValue;
returnValue = -1;

if (dbCfgs.numDescriptors == 0) {
dbCfgs.descriptors =  new M78_DbCfg_Utilities.DbCfgParamDescriptor[M01_Common.gc_allocBlockSize];
} else if (dbCfgs.numDescriptors >= M00_Helper.uBound(dbCfgs.descriptors)) {
M78_DbCfg_Utilities.DbCfgParamDescriptor[] descriptorsBackup = dbCfgs.descriptors;
dbCfgs.descriptors =  new M78_DbCfg_Utilities.DbCfgParamDescriptor[dbCfgs.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M78_DbCfg_Utilities.DbCfgParamDescriptor value : descriptorsBackup) {
dbCfgs.descriptors[indexCounter] = value;
indexCounter++;
}
}
dbCfgs.numDescriptors = dbCfgs.numDescriptors + 1;
returnValue = dbCfgs.numDescriptors;
return returnValue;
}


}