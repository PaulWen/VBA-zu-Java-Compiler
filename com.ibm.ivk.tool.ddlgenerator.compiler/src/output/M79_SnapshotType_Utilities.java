package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_SnapshotType_Utilities {




class SnapshotTypeDescriptor {
public String procName;
public String className;
public String viewName;
public int sequenceNo;
public int sequenceNoCollect;
public String category;
public int level;
public boolean isApplSpecific;
public boolean supportAnalysis;

// derived attributes
public int classIndex;

public SnapshotTypeDescriptor(String procName, String className, String viewName, int sequenceNo, int sequenceNoCollect, String category, int level, boolean isApplSpecific, boolean supportAnalysis, int classIndex) {
this.procName = procName;
this.className = className;
this.viewName = viewName;
this.sequenceNo = sequenceNo;
this.sequenceNoCollect = sequenceNoCollect;
this.category = category;
this.level = level;
this.isApplSpecific = isApplSpecific;
this.supportAnalysis = supportAnalysis;
this.classIndex = classIndex;
}
}

class SnapshotTypeDescriptors {
public M79_SnapshotType_Utilities.SnapshotTypeDescriptor[] descriptors;
public int numDescriptors;

public SnapshotTypeDescriptors(int numDescriptors, M79_SnapshotType_Utilities.SnapshotTypeDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initSnapshotTypeDescriptors(M79_SnapshotType_Utilities.SnapshotTypeDescriptors types) {
types.numDescriptors = 0;
}


public static Integer allocSnapshotTypeDescriptorIndex(M79_SnapshotType_Utilities.SnapshotTypeDescriptors types) {
Integer returnValue;
returnValue = -1;

if (types.numDescriptors == 0) {
types.descriptors =  new M79_SnapshotType_Utilities.SnapshotTypeDescriptor[M01_Common.gc_allocBlockSize];
} else if (types.numDescriptors >= M00_Helper.uBound(types.descriptors)) {
M79_SnapshotType_Utilities.SnapshotTypeDescriptor[] descriptorsBackup = types.descriptors;
types.descriptors =  new M79_SnapshotType_Utilities.SnapshotTypeDescriptor[types.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M79_SnapshotType_Utilities.SnapshotTypeDescriptor value : descriptorsBackup) {
types.descriptors[indexCounter] = value;
indexCounter++;
}
}
types.numDescriptors = types.numDescriptors + 1;
returnValue = types.numDescriptors;
return returnValue;
}

}