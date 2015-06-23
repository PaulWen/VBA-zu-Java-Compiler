package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_SnapshotFilter_Utilities {




class SnapshotFilterDescriptor {
public String tabName;
public int level;
public String collectFilter;
public String selectFilter;

public SnapshotFilterDescriptor(String tabName, int level, String collectFilter, String selectFilter) {
this.tabName = tabName;
this.level = level;
this.collectFilter = collectFilter;
this.selectFilter = selectFilter;
}
}

class SnapshotFilterDescriptors {
public M79_SnapshotFilter_Utilities.SnapshotFilterDescriptor[] descriptors;
public int numDescriptors;

public SnapshotFilterDescriptors(int numDescriptors, M79_SnapshotFilter_Utilities.SnapshotFilterDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initSnapshotFilterDescriptors(M79_SnapshotFilter_Utilities.SnapshotFilterDescriptors cols) {
cols.numDescriptors = 0;
}


public static Integer allocSnapshotFilterDescriptorIndex(M79_SnapshotFilter_Utilities.SnapshotFilterDescriptors cols) {
Integer returnValue;
returnValue = -1;

if (cols.numDescriptors == 0) {
cols.descriptors =  new M79_SnapshotFilter_Utilities.SnapshotFilterDescriptor[M01_Common.gc_allocBlockSize];
} else if (cols.numDescriptors >= M00_Helper.uBound(cols.descriptors)) {
M79_SnapshotFilter_Utilities.SnapshotFilterDescriptor[] descriptorsBackup = cols.descriptors;
cols.descriptors =  new M79_SnapshotFilter_Utilities.SnapshotFilterDescriptor[cols.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M79_SnapshotFilter_Utilities.SnapshotFilterDescriptor value : descriptorsBackup) {
cols.descriptors[indexCounter] = value;
indexCounter++;
}
}
cols.numDescriptors = cols.numDescriptors + 1;
returnValue = cols.numDescriptors;
return returnValue;
}




}