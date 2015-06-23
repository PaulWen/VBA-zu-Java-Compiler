package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M74_Container_Utilities {




class ContainerDescriptorRefs {
public int[] descriptors;
public int numDescriptors;

public ContainerDescriptorRefs(int numDescriptors, int[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}

public class containerType {
public static final int cntFile = 0;
public static final int cntDevice = 1;
}

class ContainerDescriptor {
public String tableSpaceName;
public String containerName;
public Integer type;
public boolean isCommonToOrgs;
public int specificToOrgId;
public boolean isCommonToPools;
public int specificToPool;
public boolean isPdmSpecific;
public long size;

// derived attributes
public int containerIndex;

public ContainerDescriptor(String tableSpaceName, String containerName, Integer type, boolean isCommonToOrgs, int specificToOrgId, boolean isCommonToPools, int specificToPool, boolean isPdmSpecific, long size, int containerIndex) {
this.tableSpaceName = tableSpaceName;
this.containerName = containerName;
this.type = type;
this.isCommonToOrgs = isCommonToOrgs;
this.specificToOrgId = specificToOrgId;
this.isCommonToPools = isCommonToPools;
this.specificToPool = specificToPool;
this.isPdmSpecific = isPdmSpecific;
this.size = size;
this.containerIndex = containerIndex;
}
}

class ContainerDescriptors {
public M74_Container_Utilities.ContainerDescriptor[] descriptors;
public int numDescriptors;

public ContainerDescriptors(int numDescriptors, M74_Container_Utilities.ContainerDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initContainerDescriptors(M74_Container_Utilities.ContainerDescriptors container) {
container.numDescriptors = 0;
}


public static Integer allocContainerDescriptorIndex(M74_Container_Utilities.ContainerDescriptors container) {
Integer returnValue;
returnValue = -1;

if (container.numDescriptors == 0) {
container.descriptors =  new M74_Container_Utilities.ContainerDescriptor[M01_Common.gc_allocBlockSize];
} else if (container.numDescriptors >= M00_Helper.uBound(container.descriptors)) {
M74_Container_Utilities.ContainerDescriptor[] descriptorsBackup = container.descriptors;
container.descriptors =  new M74_Container_Utilities.ContainerDescriptor[container.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M74_Container_Utilities.ContainerDescriptor value : descriptorsBackup) {
container.descriptors[indexCounter] = value;
indexCounter++;
}
}
container.numDescriptors = container.numDescriptors + 1;
returnValue = container.numDescriptors;
return returnValue;
}


public static Integer getContainerType(String str) {
Integer returnValue;
str = str + "".trim().substring(0, 1).toUpperCase();
returnValue = ((str.compareTo("D") == 0) ? M74_Container_Utilities.containerType.cntDevice : M74_Container_Utilities.containerType.cntFile);
return returnValue;
}



public static Integer allocContainerDescriptorRefIndex(M74_Container_Utilities.ContainerDescriptorRefs containerRefs) {
Integer returnValue;
returnValue = -1;

if (containerRefs.numDescriptors == 0) {
containerRefs.descriptors =  new M74_Container_Utilities.ContainerDescriptor[M01_Common.gc_allocBlockSize];
} else if (containerRefs.numDescriptors >= M00_Helper.uBound(containerRefs.descriptors)) {
M74_Container_Utilities.ContainerDescriptor[] descriptorsBackup = containerRefs.descriptors;
containerRefs.descriptors =  new M74_Container_Utilities.ContainerDescriptor[containerRefs.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M74_Container_Utilities.ContainerDescriptor value : descriptorsBackup) {
containerRefs.descriptors[indexCounter] = value;
indexCounter++;
}
}
containerRefs.numDescriptors = containerRefs.numDescriptors + 1;
returnValue = containerRefs.numDescriptors;
return returnValue;
}


}