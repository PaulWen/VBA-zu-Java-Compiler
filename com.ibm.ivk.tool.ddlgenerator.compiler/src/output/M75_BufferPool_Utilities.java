package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M75_BufferPool_Utilities {




class BufferPoolDescriptor {
public String bufPoolName;
public String shortName;
public boolean isCommonToOrgs;
public int specificToOrgId;
public boolean isCommonToPools;
public int specificToPool;
public boolean isPdmSpecific;
public long numBlockPages;
public String pageSize;
public long numPages;

// derived attributes
public int bufPoolIndex;

public BufferPoolDescriptor(String bufPoolName, String shortName, boolean isCommonToOrgs, int specificToOrgId, boolean isCommonToPools, int specificToPool, boolean isPdmSpecific, long numBlockPages, String pageSize, long numPages, int bufPoolIndex) {
this.bufPoolName = bufPoolName;
this.shortName = shortName;
this.isCommonToOrgs = isCommonToOrgs;
this.specificToOrgId = specificToOrgId;
this.isCommonToPools = isCommonToPools;
this.specificToPool = specificToPool;
this.isPdmSpecific = isPdmSpecific;
this.numBlockPages = numBlockPages;
this.pageSize = pageSize;
this.numPages = numPages;
this.bufPoolIndex = bufPoolIndex;
}
}

class BufferPoolDescriptors {
public M75_BufferPool_Utilities.BufferPoolDescriptor[] descriptors;
public int numDescriptors;

public BufferPoolDescriptors(int numDescriptors, M75_BufferPool_Utilities.BufferPoolDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initBufferPoolDescriptors(M75_BufferPool_Utilities.BufferPoolDescriptors bufPools) {
bufPools.numDescriptors = 0;
}


public static Integer allocBufferPoolDescriptorIndex(M75_BufferPool_Utilities.BufferPoolDescriptors bufPools) {
Integer returnValue;
returnValue = -1;

if (bufPools.numDescriptors == 0) {
bufPools.descriptors =  new M75_BufferPool_Utilities.BufferPoolDescriptor[M01_Common.gc_allocBlockSize];
} else if (bufPools.numDescriptors >= M00_Helper.uBound(bufPools.descriptors)) {
M75_BufferPool_Utilities.BufferPoolDescriptor[] descriptorsBackup = bufPools.descriptors;
bufPools.descriptors =  new M75_BufferPool_Utilities.BufferPoolDescriptor[bufPools.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M75_BufferPool_Utilities.BufferPoolDescriptor value : descriptorsBackup) {
bufPools.descriptors[indexCounter] = value;
indexCounter++;
}
}
bufPools.numDescriptors = bufPools.numDescriptors + 1;
returnValue = bufPools.numDescriptors;
return returnValue;
}


public static void evalBufferPools() {
int thisBufPoolIndex;
for (thisBufPoolIndex = 1; thisBufPoolIndex <= 1; thisBufPoolIndex += (1)) {
M75_BufferPool.g_bufPools.descriptors[thisBufPoolIndex].bufPoolIndex = thisBufPoolIndex;
}
}



}