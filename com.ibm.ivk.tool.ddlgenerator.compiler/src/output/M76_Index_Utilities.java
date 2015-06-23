package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M76_Index_Utilities {




class IndexDescriptorRefs {
public int[] refs;
public int numRefs;

public IndexDescriptorRefs(int numRefs, int[] refs) {
this.numRefs = numRefs;
this.refs = refs;
}
}

class IndexDescriptor {
public String sectionName;
public String className;
public Integer cType;
public String indexName;
public String shortName;
public boolean isUnique;
public boolean forGen;
public boolean specificToQueryTables;
public String specificToPools;

// derived attributes
public int sectionIndex;
public M77_IndexAttr_Utilities.IndexAttrDescriptorRefs attrRefs;

public IndexDescriptor(String sectionName, String className, Integer cType, String indexName, String shortName, boolean isUnique, boolean forGen, boolean specificToQueryTables, String specificToPools, int sectionIndex, M77_IndexAttr_Utilities.IndexAttrDescriptorRefs attrRefs) {
this.sectionName = sectionName;
this.className = className;
this.cType = cType;
this.indexName = indexName;
this.shortName = shortName;
this.isUnique = isUnique;
this.forGen = forGen;
this.specificToQueryTables = specificToQueryTables;
this.specificToPools = specificToPools;
this.sectionIndex = sectionIndex;
this.attrRefs = attrRefs;
}
}

class IndexDescriptors {
public M76_Index_Utilities.IndexDescriptor[] descriptors;
public int numDescriptors;

public IndexDescriptors(int numDescriptors, M76_Index_Utilities.IndexDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initIndexDescriptors(M76_Index_Utilities.IndexDescriptors indexes) {
indexes.numDescriptors = 0;
}


public static Integer allocIndexDescriptorIndex(M76_Index_Utilities.IndexDescriptors indexes) {
Integer returnValue;
returnValue = -1;

if (indexes.numDescriptors == 0) {
indexes.descriptors =  new M76_Index_Utilities.IndexDescriptor[M01_Common.gc_allocBlockSize];
} else if (indexes.numDescriptors >= M00_Helper.uBound(indexes.descriptors)) {
M76_Index_Utilities.IndexDescriptor[] descriptorsBackup = indexes.descriptors;
indexes.descriptors =  new M76_Index_Utilities.IndexDescriptor[indexes.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M76_Index_Utilities.IndexDescriptor value : descriptorsBackup) {
indexes.descriptors[indexCounter] = value;
indexCounter++;
}
}
indexes.numDescriptors = indexes.numDescriptors + 1;
indexes.descriptors[indexes.numDescriptors].attrRefs.numRefs = 0;
returnValue = indexes.numDescriptors;
return returnValue;
}


public static Integer allocIndexDescriptorRefIndex(M76_Index_Utilities.IndexDescriptorRefs indexRefs) {
Integer returnValue;
returnValue = -1;

if (indexRefs.numRefs == 0) {
indexRefs.refs =  new int[M01_Common.gc_allocBlockSize];
} else if (indexRefs.numRefs >= M00_Helper.uBound(indexRefs.refs)) {
int[] refsBackup = indexRefs.refs;
indexRefs.refs =  new int[indexRefs.numRefs + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (int value : refsBackup) {
indexRefs.refs[indexCounter] = value;
indexCounter++;
}
}
indexRefs.numRefs = indexRefs.numRefs + 1;
returnValue = indexRefs.numRefs;
return returnValue;
}


}