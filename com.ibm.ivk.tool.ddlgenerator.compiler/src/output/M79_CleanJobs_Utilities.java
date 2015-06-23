package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_CleanJobs_Utilities {




class CleanJobDescriptor {
public String jobCategory;
public String jobName;
public String level;
public String sequenceNo;
public String tableSchema;
public String tableName;
public String tableRef;
public String condition;
public long commitCount;

public CleanJobDescriptor(String jobCategory, String jobName, String level, String sequenceNo, String tableSchema, String tableName, String tableRef, String condition, long commitCount) {
this.jobCategory = jobCategory;
this.jobName = jobName;
this.level = level;
this.sequenceNo = sequenceNo;
this.tableSchema = tableSchema;
this.tableName = tableName;
this.tableRef = tableRef;
this.condition = condition;
this.commitCount = commitCount;
}
}

class CleanJobDescriptors {
public M79_CleanJobs_Utilities.CleanJobDescriptor[] descriptors;
public int numDescriptors;

public CleanJobDescriptors(int numDescriptors, M79_CleanJobs_Utilities.CleanJobDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initCleanJobDescriptors(M79_CleanJobs_Utilities.CleanJobDescriptors jobs) {
jobs.numDescriptors = 0;
}


public static Integer allocCleanJobDescriptorIndex(M79_CleanJobs_Utilities.CleanJobDescriptors jobs) {
Integer returnValue;
returnValue = -1;

if (jobs.numDescriptors == 0) {
jobs.descriptors =  new M79_CleanJobs_Utilities.CleanJobDescriptor[M01_Common.gc_allocBlockSize];
} else if (jobs.numDescriptors >= M00_Helper.uBound(jobs.descriptors)) {
M79_CleanJobs_Utilities.CleanJobDescriptor[] descriptorsBackup = jobs.descriptors;
jobs.descriptors =  new M79_CleanJobs_Utilities.CleanJobDescriptor[jobs.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M79_CleanJobs_Utilities.CleanJobDescriptor value : descriptorsBackup) {
jobs.descriptors[indexCounter] = value;
indexCounter++;
}
}
jobs.numDescriptors = jobs.numDescriptors + 1;
returnValue = jobs.numDescriptors;
return returnValue;
}


}