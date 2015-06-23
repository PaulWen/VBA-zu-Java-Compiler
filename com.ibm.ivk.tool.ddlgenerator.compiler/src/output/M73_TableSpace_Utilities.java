package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M73_TableSpace_Utilities {




public class TabSpaceCategory {
public static final int tscSms = 0;
public static final int tscDms = 1;
}

class TableSpaceDescriptor {
public String tableSpaceName;
public String shortName;
public boolean isCommonToOrgs;
public int specificToOrgId;
public boolean isCommonToPools;
public int specificToPool;
public boolean isPdmSpecific;
public boolean isMonitor;
public String type;
public Integer category;
public String pageSize;
public boolean autoResize;
public int increasePercent;
public String increaseAbsolute;
public String maxSize;
public String extentSize;
public String prefetchSize;
public String bufferPoolName;
public String overhead;
public String transferrate;
public boolean useFileSystemCaching;
public boolean supportDroppedTableRecovery;

// derived attributes
public int tableSpaceIndex;
public M74_Container_Utilities.ContainerDescriptorRefs containerRefs;
public int bufferPoolIndex;

public TableSpaceDescriptor(String tableSpaceName, String shortName, boolean isCommonToOrgs, int specificToOrgId, boolean isCommonToPools, int specificToPool, boolean isPdmSpecific, boolean isMonitor, String type, Integer category, String pageSize, boolean autoResize, int increasePercent, String increaseAbsolute, String maxSize, String extentSize, String prefetchSize, String bufferPoolName, String overhead, String transferrate, boolean useFileSystemCaching, boolean supportDroppedTableRecovery, int tableSpaceIndex, M74_Container_Utilities.ContainerDescriptorRefs containerRefs, int bufferPoolIndex) {
this.tableSpaceName = tableSpaceName;
this.shortName = shortName;
this.isCommonToOrgs = isCommonToOrgs;
this.specificToOrgId = specificToOrgId;
this.isCommonToPools = isCommonToPools;
this.specificToPool = specificToPool;
this.isPdmSpecific = isPdmSpecific;
this.isMonitor = isMonitor;
this.type = type;
this.category = category;
this.pageSize = pageSize;
this.autoResize = autoResize;
this.increasePercent = increasePercent;
this.increaseAbsolute = increaseAbsolute;
this.maxSize = maxSize;
this.extentSize = extentSize;
this.prefetchSize = prefetchSize;
this.bufferPoolName = bufferPoolName;
this.overhead = overhead;
this.transferrate = transferrate;
this.useFileSystemCaching = useFileSystemCaching;
this.supportDroppedTableRecovery = supportDroppedTableRecovery;
this.tableSpaceIndex = tableSpaceIndex;
this.containerRefs = containerRefs;
this.bufferPoolIndex = bufferPoolIndex;
}
}

class TableSpaceDescriptors {
public M73_TableSpace_Utilities.TableSpaceDescriptor[] descriptors;
public int numDescriptors;

public TableSpaceDescriptors(int numDescriptors, M73_TableSpace_Utilities.TableSpaceDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initTableSpaceDescriptors(M73_TableSpace_Utilities.TableSpaceDescriptors tablespace) {
tablespace.numDescriptors = 0;
}


public static Integer allocTableSpaceDescriptorIndex(M73_TableSpace_Utilities.TableSpaceDescriptors tablespace) {
Integer returnValue;
returnValue = -1;

if (tablespace.numDescriptors == 0) {
tablespace.descriptors =  new M73_TableSpace_Utilities.TableSpaceDescriptor[M01_Common.gc_allocBlockSize];
} else if (tablespace.numDescriptors >= M00_Helper.uBound(tablespace.descriptors)) {
M73_TableSpace_Utilities.TableSpaceDescriptor[] descriptorsBackup = tablespace.descriptors;
tablespace.descriptors =  new M73_TableSpace_Utilities.TableSpaceDescriptor[tablespace.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M73_TableSpace_Utilities.TableSpaceDescriptor value : descriptorsBackup) {
tablespace.descriptors[indexCounter] = value;
indexCounter++;
}
}
tablespace.numDescriptors = tablespace.numDescriptors + 1;
tablespace.descriptors[tablespace.numDescriptors].containerRefs.numDescriptors = 0;
returnValue = tablespace.numDescriptors;
return returnValue;
}


public static Integer getTabSpaceCategory(String str) {
Integer returnValue;
str = str + "".trim().substring(0, 1).toUpperCase();
returnValue = ((str.compareTo("D") == 0) ? M73_TableSpace_Utilities.TabSpaceCategory.tscDms : M73_TableSpace_Utilities.TabSpaceCategory.tscSms);
return returnValue;
}

}