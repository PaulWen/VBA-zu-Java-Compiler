package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M99_IndexException_Utilities {




class IndexExcpDescriptor {
public String sectionName;
public String sectionShortName;
public String indexName;
public String noIndexInPool;

public IndexExcpDescriptor(String sectionName, String sectionShortName, String indexName, String noIndexInPool) {
this.sectionName = sectionName;
this.sectionShortName = sectionShortName;
this.indexName = indexName;
this.noIndexInPool = noIndexInPool;
}
}

class IndexExcpDescriptors {
public M99_IndexException_Utilities.IndexExcpDescriptor[] descriptors;
public int numDescriptors;

public IndexExcpDescriptors(int numDescriptors, M99_IndexException_Utilities.IndexExcpDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initIndexExcpDescriptors(M99_IndexException_Utilities.IndexExcpDescriptors indexes) {
indexes.numDescriptors = 0;
}



public static Integer allocIndexExcpDescriptorIndex(M99_IndexException_Utilities.IndexExcpDescriptors indexes) {
Integer returnValue;
returnValue = -1;

if (indexes.numDescriptors == 0) {
indexes.descriptors =  new M99_IndexException_Utilities.IndexExcpDescriptor[M01_Common.gc_allocBlockSize];
} else if (indexes.numDescriptors >= M00_Helper.uBound(indexes.descriptors)) {
M99_IndexException_Utilities.IndexExcpDescriptor[] descriptorsBackup = indexes.descriptors;
indexes.descriptors =  new M99_IndexException_Utilities.IndexExcpDescriptor[indexes.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M99_IndexException_Utilities.IndexExcpDescriptor value : descriptorsBackup) {
indexes.descriptors[indexCounter] = value;
indexCounter++;
}
}
indexes.numDescriptors = indexes.numDescriptors + 1;
returnValue = indexes.numDescriptors;
return returnValue;
}



public static Boolean indexExcp( String qualIndexName,  int thisOrgIndex, Integer ddlTypeW) {
Integer ddlType; 
if (ddlTypeW == null) {
ddlType = null;
} else {
ddlType = ddlTypeW;
}

Boolean returnValue;
returnValue = false;

int i;
//  Dim test As String
for (i = 1; i <= 1; i += (1)) {
//    test = "VL6C" & .sectionShortName & genOrgIdByIndex(thisOrgIndex, ddlType) & .noIndexInPool & "." & .indexName
if (("VL6C" + M99_IndexException.g_indexExcp.descriptors[i].sectionShortName + M04_Utilities.genOrgIdByIndex(thisOrgIndex, ddlType, null) + M99_IndexException.g_indexExcp.descriptors[i].noIndexInPool + "." + M99_IndexException.g_indexExcp.descriptors[i].indexName).compareTo(qualIndexName) == 0) {
returnValue = true;
return returnValue;
}
}
return returnValue;
}




}