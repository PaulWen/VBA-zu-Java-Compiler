package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_Privileges_Utilities {




class PrivilegeDescriptor {
public int sequenceNumber;
public String environment;
public String operation;
public String objectType;
public String schemaName;
public String objectName;
public String filter;
public String granteeType;
public String grantee;
public String privilege;
public boolean withGrantOption;

public PrivilegeDescriptor(int sequenceNumber, String environment, String operation, String objectType, String schemaName, String objectName, String filter, String granteeType, String grantee, String privilege, boolean withGrantOption) {
this.sequenceNumber = sequenceNumber;
this.environment = environment;
this.operation = operation;
this.objectType = objectType;
this.schemaName = schemaName;
this.objectName = objectName;
this.filter = filter;
this.granteeType = granteeType;
this.grantee = grantee;
this.privilege = privilege;
this.withGrantOption = withGrantOption;
}
}

class PrivilegeDescriptors {
public M79_Privileges_Utilities.PrivilegeDescriptor[] descriptors;
public int numDescriptors;

public PrivilegeDescriptors(int numDescriptors, M79_Privileges_Utilities.PrivilegeDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initPrivilegeDescriptors(M79_Privileges_Utilities.PrivilegeDescriptors perms) {
perms.numDescriptors = 0;
}


public static Integer allocPrivilegeDescriptorIndex(M79_Privileges_Utilities.PrivilegeDescriptors perms) {
Integer returnValue;
returnValue = -1;

if (perms.numDescriptors == 0) {
perms.descriptors =  new M79_Privileges_Utilities.PrivilegeDescriptor[M01_Common.gc_allocBlockSize];
} else if (perms.numDescriptors >= M00_Helper.uBound(perms.descriptors)) {
M79_Privileges_Utilities.PrivilegeDescriptor[] descriptorsBackup = perms.descriptors;
perms.descriptors =  new M79_Privileges_Utilities.PrivilegeDescriptor[perms.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M79_Privileges_Utilities.PrivilegeDescriptor value : descriptorsBackup) {
perms.descriptors[indexCounter] = value;
indexCounter++;
}
}
perms.numDescriptors = perms.numDescriptors + 1;
returnValue = perms.numDescriptors;
return returnValue;
}


public static void evalPrivileges() {
int i;
int j;
for (i = 1; i <= 1; i += (1)) {
if (M79_Privileges.g_privileges.descriptors[i].withGrantOption &  M79_Privileges.g_privileges.descriptors[i].objectType.toUpperCase() != "SCHEMA") {
M04_Utilities.logMsg("privileges on object \"" + M79_Privileges.g_privileges.descriptors[i].objectName + "\" (" + M79_Privileges.g_privileges.descriptors[i].objectType + ") WITH GRANT OPTION not supported - fixed", M01_Common.LogLevel.ellFixableWarning, null, null, null);
M79_Privileges.g_privileges.descriptors[i].withGrantOption = false;
}
}
}

}