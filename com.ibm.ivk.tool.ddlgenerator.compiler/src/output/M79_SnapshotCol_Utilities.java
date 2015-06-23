package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_SnapshotCol_Utilities {




class SnapshotColDescriptor {
public String tabName;
public String colName;
public String colAlias;
public String displayFunction;
public String columnExpression;
public int sequenceNo;
public String category;
public int level;

public SnapshotColDescriptor(String tabName, String colName, String colAlias, String displayFunction, String columnExpression, int sequenceNo, String category, int level) {
this.tabName = tabName;
this.colName = colName;
this.colAlias = colAlias;
this.displayFunction = displayFunction;
this.columnExpression = columnExpression;
this.sequenceNo = sequenceNo;
this.category = category;
this.level = level;
}
}

class SnapshotColDescriptors {
public M79_SnapshotCol_Utilities.SnapshotColDescriptor[] descriptors;
public int numDescriptors;

public SnapshotColDescriptors(int numDescriptors, M79_SnapshotCol_Utilities.SnapshotColDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initSnapshotColDescriptors(M79_SnapshotCol_Utilities.SnapshotColDescriptors cols) {
cols.numDescriptors = 0;
}


public static Integer allocSnapshotColDescriptorIndex(M79_SnapshotCol_Utilities.SnapshotColDescriptors cols) {
Integer returnValue;
returnValue = -1;

if (cols.numDescriptors == 0) {
cols.descriptors =  new M79_SnapshotCol_Utilities.SnapshotColDescriptor[M01_Common.gc_allocBlockSize];
} else if (cols.numDescriptors >= M00_Helper.uBound(cols.descriptors)) {
M79_SnapshotCol_Utilities.SnapshotColDescriptor[] descriptorsBackup = cols.descriptors;
cols.descriptors =  new M79_SnapshotCol_Utilities.SnapshotColDescriptor[cols.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M79_SnapshotCol_Utilities.SnapshotColDescriptor value : descriptorsBackup) {
cols.descriptors[indexCounter] = value;
indexCounter++;
}
}
cols.numDescriptors = cols.numDescriptors + 1;
returnValue = cols.numDescriptors;
return returnValue;
}



}