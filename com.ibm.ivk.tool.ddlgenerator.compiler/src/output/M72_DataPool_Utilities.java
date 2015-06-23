package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M72_DataPool_Utilities {




class DataPoolDescriptor {
public int id;
public String name;
public String shortName;
public int specificToOrgId;
public boolean supportLrt;
// ### IF IVK ###
public boolean supportViewsForPsTag;
public boolean supportTriggerForPsTag;
public boolean supportXmlExport;
// ### ENDIF IVK ###
public boolean supportUpdates;
public boolean suppressRefIntegrity;
public boolean suppressUniqueConstraints;
// ### IF IVK ###
public boolean instantiateExpressions;
// ### ENDIF IVK ###
public boolean commonItemsLocal;
public boolean supportAcm;
public boolean isActive;
// ### IF IVK ###
public boolean isProductive;
public boolean isArchive;
public boolean supportNationalization;
// ### ENDIF IVK ###
public int sequenceCacheSize;

public DataPoolDescriptor(int id, String name, String shortName, int specificToOrgId, boolean supportLrt, boolean supportViewsForPsTag, boolean supportTriggerForPsTag, boolean supportXmlExport, boolean supportUpdates, boolean suppressRefIntegrity, boolean suppressUniqueConstraints, boolean instantiateExpressions, boolean commonItemsLocal, boolean supportAcm, boolean isActive, boolean isProductive, boolean isArchive, boolean supportNationalization, int sequenceCacheSize) {
this.id = id;
this.name = name;
this.shortName = shortName;
this.specificToOrgId = specificToOrgId;
this.supportLrt = supportLrt;
this.supportViewsForPsTag = supportViewsForPsTag;
this.supportTriggerForPsTag = supportTriggerForPsTag;
this.supportXmlExport = supportXmlExport;
this.supportUpdates = supportUpdates;
this.suppressRefIntegrity = suppressRefIntegrity;
this.suppressUniqueConstraints = suppressUniqueConstraints;
this.instantiateExpressions = instantiateExpressions;
this.commonItemsLocal = commonItemsLocal;
this.supportAcm = supportAcm;
this.isActive = isActive;
this.isProductive = isProductive;
this.isArchive = isArchive;
this.supportNationalization = supportNationalization;
this.sequenceCacheSize = sequenceCacheSize;
}
}

class DataPoolDescriptors {
public M72_DataPool_Utilities.DataPoolDescriptor[] descriptors;
public int numDescriptors;

public DataPoolDescriptors(int numDescriptors, M72_DataPool_Utilities.DataPoolDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initDataPoolDescriptors(M72_DataPool_Utilities.DataPoolDescriptors pools) {
pools.numDescriptors = 0;
}

public static Integer allocDataPoolIndex(M72_DataPool_Utilities.DataPoolDescriptors pools) {
Integer returnValue;
returnValue = -1;

if (pools.numDescriptors == 0) {
pools.descriptors =  new M72_DataPool_Utilities.DataPoolDescriptor[M01_Common.gc_allocBlockSize];
} else if (pools.numDescriptors >= M00_Helper.uBound(pools.descriptors)) {
M72_DataPool_Utilities.DataPoolDescriptor[] descriptorsBackup = pools.descriptors;
pools.descriptors =  new M72_DataPool_Utilities.DataPoolDescriptor[pools.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M72_DataPool_Utilities.DataPoolDescriptor value : descriptorsBackup) {
pools.descriptors[indexCounter] = value;
indexCounter++;
}
}
pools.numDescriptors = pools.numDescriptors + 1;
returnValue = pools.numDescriptors;
return returnValue;
}


public static Integer getEffectivePoolId(int thisPoolId, boolean isCommon) {
Integer returnValue;
returnValue = (isCommon ? -1 : thisPoolId);
return returnValue;
}


public static Integer getEffectivePoolIndex( int thisPoolIndex, boolean isCommon) {
Integer returnValue;
returnValue = (isCommon ? -1 : thisPoolIndex);
return returnValue;
}


// ### IF IVK ###
public static Integer getMigDataPoolIndex() {
Integer returnValue;
returnValue = -1;

int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
// FIXME
if (M72_DataPool.g_pools.descriptors[i].id == 0) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static Integer getMigDataPoolId() {
Integer returnValue;
returnValue = 0;// FIXME
return returnValue;
}


// ### ENDIF IVK ###
public static Integer getWorkDataPoolIndex() {
Integer returnValue;
returnValue = -1;

int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M72_DataPool.g_pools.descriptors[i].supportLrt) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static Integer getWorkDataPoolId() {
Integer returnValue;
returnValue = -1;

int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M72_DataPool.g_pools.descriptors[i].supportLrt) {
returnValue = M72_DataPool.g_pools.descriptors[i].id;
return returnValue;
}
}
return returnValue;
}


// ### IF IVK ###
public static Integer getProductiveDataPoolIndex() {
Integer returnValue;
returnValue = -1;
M72_DataPool.getDataPools();

int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M72_DataPool.g_pools.descriptors[i].isProductive) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static Integer getProductiveDataPoolId() {
Integer returnValue;
returnValue = -1;
M72_DataPool.getDataPools();

int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M72_DataPool.g_pools.descriptors[i].isProductive) {
returnValue = M72_DataPool.g_pools.descriptors[i].id;
return returnValue;
}
}
return returnValue;
}


public static Integer getArchiveDataPoolIndex() {
Integer returnValue;
returnValue = -1;
M72_DataPool.getDataPools();

int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M72_DataPool.g_pools.descriptors[i].isArchive) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static Integer getArchiveDataPoolId() {
Integer returnValue;
returnValue = -1;
M72_DataPool.getDataPools();

int i;
for (int i = 1; i <= M72_DataPool.g_pools.numDescriptors; i++) {
if (M72_DataPool.g_pools.descriptors[i].isArchive) {
returnValue = M72_DataPool.g_pools.descriptors[i].id;
return returnValue;
}
}
return returnValue;
}
// ### ENDIF IVK ###


}