package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M77_IndexAttr_Utilities {




class IndexAttrDescriptorRefs {
public int[] refs;
public int numRefs;

public IndexAttrDescriptorRefs(int numRefs, int[] refs) {
this.numRefs = numRefs;
this.refs = refs;
}
}

class IndexAttrDescriptor {
public String sectionName;
public String className;
public Integer cType;
public String indexName;
public String attrName;
public boolean attrIsIncluded;
public String relSectionName;
public String relName;
public boolean isAsc;

// derived attributes
public int attrRef;
public int relRef;
public Integer relRefDirection;

public IndexAttrDescriptor(String sectionName, String className, Integer cType, String indexName, String attrName, boolean attrIsIncluded, String relSectionName, String relName, boolean isAsc, int attrRef, int relRef, Integer relRefDirection) {
this.sectionName = sectionName;
this.className = className;
this.cType = cType;
this.indexName = indexName;
this.attrName = attrName;
this.attrIsIncluded = attrIsIncluded;
this.relSectionName = relSectionName;
this.relName = relName;
this.isAsc = isAsc;
this.attrRef = attrRef;
this.relRef = relRef;
this.relRefDirection = relRefDirection;
}
}

class IndexAttrDescriptors {
public M77_IndexAttr_Utilities.IndexAttrDescriptor[] descriptors;
public int numDescriptors;

public IndexAttrDescriptors(int numDescriptors, M77_IndexAttr_Utilities.IndexAttrDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initIndexAttrDescriptors(M77_IndexAttr_Utilities.IndexAttrDescriptors indexes) {
indexes.numDescriptors = 0;
}


public static Integer allocIndexAttrDescriptorIndex(M77_IndexAttr_Utilities.IndexAttrDescriptors indexes) {
Integer returnValue;
returnValue = -1;

if (indexes.numDescriptors == 0) {
indexes.descriptors =  new M77_IndexAttr_Utilities.IndexAttrDescriptor[M01_Common.gc_allocBlockSize];
} else if (indexes.numDescriptors >= M00_Helper.uBound(indexes.descriptors)) {
M77_IndexAttr_Utilities.IndexAttrDescriptor[] descriptorsBackup = indexes.descriptors;
indexes.descriptors =  new M77_IndexAttr_Utilities.IndexAttrDescriptor[indexes.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M77_IndexAttr_Utilities.IndexAttrDescriptor value : descriptorsBackup) {
indexes.descriptors[indexCounter] = value;
indexCounter++;
}
}
indexes.numDescriptors = indexes.numDescriptors + 1;
returnValue = indexes.numDescriptors;
return returnValue;
}


public static Integer allocIndexAttrDescriptorRefIndex(M77_IndexAttr_Utilities.IndexAttrDescriptorRefs attrRefs) {
Integer returnValue;
returnValue = -1;

if (attrRefs.numRefs == 0) {
attrRefs.refs =  new int[M01_Common.gc_allocBlockSize];
} else if (attrRefs.numRefs >= M00_Helper.uBound(attrRefs.refs)) {
int[] refsBackup = attrRefs.refs;
attrRefs.refs =  new int[attrRefs.numRefs + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : refsBackup) {
attrRefs.refs[indexCounter] = value;
indexCounter++;
}
}
attrRefs.numRefs = attrRefs.numRefs + 1;
returnValue = attrRefs.numRefs;
return returnValue;
}


}