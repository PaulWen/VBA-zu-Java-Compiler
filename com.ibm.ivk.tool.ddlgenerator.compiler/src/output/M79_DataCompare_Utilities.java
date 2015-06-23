package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_DataCompare_Utilities {


// ### IF IVK ###


public class DataCompareMode {
public static final int dcmKey = 1;
public static final int dcmCompare = 2;
public static final int dcmNone = 0;
}

class DCompDescriptorRefs {
public int[] refs;
public int numRefs;

public DCompDescriptorRefs(int numRefs, int[] refs) {
this.numRefs = numRefs;
this.refs = refs;
}
}

class DCompDescriptor {
public String checkName;
public String sectionName;
public String entityName;
public Integer cType;
public int dataPoolId;
public int refDataPoolId;

public String attrName;
public String compareMode;
public int sequenceNo;

// derived attributes
public int attrRef;

public DCompDescriptor(String checkName, String sectionName, String entityName, Integer cType, int dataPoolId, int refDataPoolId, String attrName, String compareMode, int sequenceNo, int attrRef) {
this.checkName = checkName;
this.sectionName = sectionName;
this.entityName = entityName;
this.cType = cType;
this.dataPoolId = dataPoolId;
this.refDataPoolId = refDataPoolId;
this.attrName = attrName;
this.compareMode = compareMode;
this.sequenceNo = sequenceNo;
this.attrRef = attrRef;
}
}

class DCompDescriptors {
public M79_DataCompare_Utilities.DCompDescriptor[] descriptors;
public int numDescriptors;

public DCompDescriptors(int numDescriptors, M79_DataCompare_Utilities.DCompDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initDCompDescriptors(M79_DataCompare_Utilities.DCompDescriptors dComps) {
dComps.numDescriptors = 0;
}


public static Integer allocDCompDescriptorIndex(M79_DataCompare_Utilities.DCompDescriptors dComps) {
Integer returnValue;
returnValue = -1;

if (dComps.numDescriptors == 0) {
dComps.descriptors =  new M79_DataCompare_Utilities.DCompDescriptor[M01_Common.gc_allocBlockSize];
} else if (dComps.numDescriptors >= M00_Helper.uBound(dComps.descriptors)) {
M79_DataCompare_Utilities.DCompDescriptor[] descriptorsBackup = dComps.descriptors;
dComps.descriptors =  new M79_DataCompare_Utilities.DCompDescriptor[dComps.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M79_DataCompare_Utilities.DCompDescriptor value : descriptorsBackup) {
dComps.descriptors[indexCounter] = value;
indexCounter++;
}
}
dComps.numDescriptors = dComps.numDescriptors + 1;
returnValue = dComps.numDescriptors;
return returnValue;
}


public static String getDataCompareMode(String str) {
String returnValue;
str = str + "".trim().substring(0, 1).toUpperCase();
returnValue = ((str.compareTo("K") == 0) ? M79_DataCompare_Utilities.DataCompareMode.dcmKey : ((str.compareTo("C") == 0) ? M79_DataCompare_Utilities.DataCompareMode.dcmCompare : M79_DataCompare_Utilities.DataCompareMode.dcmNone));
return returnValue;
}
// ### ENDIF IVK ###


}