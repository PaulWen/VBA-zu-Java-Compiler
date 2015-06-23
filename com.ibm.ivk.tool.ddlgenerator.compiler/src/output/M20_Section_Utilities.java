package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M20_Section_Utilities {




class SectionDescriptor {
public String sectionName;
public String shortName;
public int seqNo;
public String specificToOrgs;
public String specificToPools;

public boolean isTechnical;

// derived attributes
public int sectionIndex;
public int maxRelId;

// file handles
public int[] fileNoDdl;

public SectionDescriptor(String sectionName, String shortName, int seqNo, String specificToOrgs, String specificToPools, boolean isTechnical, int sectionIndex, int maxRelId, int[] fileNoDdl) {
this.sectionName = sectionName;
this.shortName = shortName;
this.seqNo = seqNo;
this.specificToOrgs = specificToOrgs;
this.specificToPools = specificToPools;
this.isTechnical = isTechnical;
this.sectionIndex = sectionIndex;
this.maxRelId = maxRelId;
this.fileNoDdl = fileNoDdl;
}
}

class SectionDescriptors {
public M20_Section_Utilities.SectionDescriptor[] descriptors;
public int numDescriptors;
public int maxSeqNo;

public SectionDescriptors(int numDescriptors, int maxSeqNo, M20_Section_Utilities.SectionDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.maxSeqNo = maxSeqNo;
this.descriptors = descriptors;
}
}


public static void initSectionDescriptors(M20_Section_Utilities.SectionDescriptors sects) {
sects.numDescriptors = 0;
sects.maxSeqNo = 0;
}


public static Integer allocSectionDescriptorIndex(M20_Section_Utilities.SectionDescriptors sects) {
Integer returnValue;
returnValue = -1;

if (sects.numDescriptors == 0) {
sects.descriptors =  new M20_Section_Utilities.SectionDescriptor[M01_Common.gc_allocBlockSize];
} else if (sects.numDescriptors >= M00_Helper.uBound(sects.descriptors)) {
M20_Section_Utilities.SectionDescriptor[] descriptorsBackup = sects.descriptors;
sects.descriptors =  new M20_Section_Utilities.SectionDescriptor[sects.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M20_Section_Utilities.SectionDescriptor value : descriptorsBackup) {
sects.descriptors[indexCounter] = value;
indexCounter++;
}
}
sects.numDescriptors = sects.numDescriptors + 1;
returnValue = sects.numDescriptors;
return returnValue;
}


public static Boolean sectionValidForPoolAndOrg(int sectionIndex,  Integer thisOrgIndexW,  Integer thisPoolIndexW) {
int thisOrgIndex; 
if (thisOrgIndexW == null) {
thisOrgIndex = -1;
} else {
thisOrgIndex = thisOrgIndexW;
}

int thisPoolIndex; 
if (thisPoolIndexW == null) {
thisPoolIndex = -1;
} else {
thisPoolIndex = thisPoolIndexW;
}

Boolean returnValue;
returnValue = false;

if (!(M20_Section.g_sections.descriptors[sectionIndex].specificToOrgs.compareTo("") == 0)) {
if (thisOrgIndex < 1) {
returnValue = !(M04_Utilities.listHasPostiveElement(M20_Section.g_sections.descriptors[sectionIndex].specificToOrgs));
return returnValue;
} else if (!(M04_Utilities.includedInList(M20_Section.g_sections.descriptors[sectionIndex].specificToOrgs, M71_Org.g_orgs.descriptors[thisOrgIndex].id))) {
return returnValue;
}
}
if (!(M20_Section.g_sections.descriptors[sectionIndex].specificToPools.compareTo("") == 0)) {
if (thisPoolIndex < 1) {
returnValue = !(M04_Utilities.listHasPostiveElement(M20_Section.g_sections.descriptors[sectionIndex].specificToPools));
return returnValue;
} else if (!(M04_Utilities.includedInList(M20_Section.g_sections.descriptors[sectionIndex].specificToPools, M72_DataPool.g_pools.descriptors[thisPoolIndex].id))) {
return returnValue;
}
}

returnValue = true;
return returnValue;
}


}