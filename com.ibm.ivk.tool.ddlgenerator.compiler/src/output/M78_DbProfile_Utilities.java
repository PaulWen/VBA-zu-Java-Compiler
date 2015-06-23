package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M78_DbProfile_Utilities {




class DbCfgProfileDescriptor {
public String profileName;
public String objectType;
public String schemaName;
public String objectName;
public int sequenceNo;
public String configParameter;
public String configValue;
public String serverPlatform;
public String minDbRelease;

public DbCfgProfileDescriptor(String profileName, String objectType, String schemaName, String objectName, int sequenceNo, String configParameter, String configValue, String serverPlatform, String minDbRelease) {
this.profileName = profileName;
this.objectType = objectType;
this.schemaName = schemaName;
this.objectName = objectName;
this.sequenceNo = sequenceNo;
this.configParameter = configParameter;
this.configValue = configValue;
this.serverPlatform = serverPlatform;
this.minDbRelease = minDbRelease;
}
}

class DbCfgProfileDescriptors {
public M78_DbProfile_Utilities.DbCfgProfileDescriptor[] descriptors;
public int numDescriptors;

public DbCfgProfileDescriptors(int numDescriptors, M78_DbProfile_Utilities.DbCfgProfileDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initDbCfgProfileDescriptors(M78_DbProfile_Utilities.DbCfgProfileDescriptors indexes) {
indexes.numDescriptors = 0;
}


public static Integer allocDbCfgProfileDescriptorIndex(M78_DbProfile_Utilities.DbCfgProfileDescriptors indexes) {
Integer returnValue;
returnValue = -1;

if (indexes.numDescriptors == 0) {
indexes.descriptors =  new M78_DbProfile_Utilities.DbCfgProfileDescriptor[M01_Common.gc_allocBlockSize];
} else if (indexes.numDescriptors >= M00_Helper.uBound(indexes.descriptors)) {
M78_DbProfile_Utilities.DbCfgProfileDescriptor[] descriptorsBackup = indexes.descriptors;
indexes.descriptors =  new M78_DbProfile_Utilities.DbCfgProfileDescriptor[indexes.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M78_DbProfile_Utilities.DbCfgProfileDescriptor value : descriptorsBackup) {
indexes.descriptors[indexCounter] = value;
indexCounter++;
}
}
indexes.numDescriptors = indexes.numDescriptors + 1;
returnValue = indexes.numDescriptors;
return returnValue;
}


}