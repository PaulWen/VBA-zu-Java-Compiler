package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M25_Domain_Utilities {




class DomainDescriptor {
public String sectionName;
public String domainName;
public Integer dataType;
public String minLength;
public String maxLength;
public int scale;
public String minValue;
public String maxValue;
public String valueList;
public String constraint;
public boolean notLogged;
public boolean notCompact;
public boolean supportUnicode;
public double unicodeExpansionFactor;
public boolean isGenerated;

// derived attributes
public int domainIndex;// my index position in 'g_domains'

public DomainDescriptor(String sectionName, String domainName, Integer dataType, String minLength, String maxLength, int scale, String minValue, String maxValue, String valueList, String constraint, boolean notLogged, boolean notCompact, boolean supportUnicode, double unicodeExpansionFactor, boolean isGenerated, int domainIndex) {
this.sectionName = sectionName;
this.domainName = domainName;
this.dataType = dataType;
this.minLength = minLength;
this.maxLength = maxLength;
this.scale = scale;
this.minValue = minValue;
this.maxValue = maxValue;
this.valueList = valueList;
this.constraint = constraint;
this.notLogged = notLogged;
this.notCompact = notCompact;
this.supportUnicode = supportUnicode;
this.unicodeExpansionFactor = unicodeExpansionFactor;
this.isGenerated = isGenerated;
this.domainIndex = domainIndex;
}
}

class DomainDescriptors {
public M25_Domain_Utilities.DomainDescriptor[] descriptors;
public int numDescriptors;

public DomainDescriptors(int numDescriptors, M25_Domain_Utilities.DomainDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}

class DomainDescriptorRefHandle {
public int ref;
public boolean isNullable;

public DomainDescriptorRefHandle(int ref, boolean isNullable) {
this.ref = ref;
this.isNullable = isNullable;
}
}

class DomainDescriptorRefs {
public M25_Domain_Utilities.DomainDescriptorRefHandle[] refs;
public int numRefs;

public DomainDescriptorRefs(int numRefs, M25_Domain_Utilities.DomainDescriptorRefHandle[] refs) {
this.numRefs = numRefs;
this.refs = refs;
}
}


public static void initDomainDescriptors(M25_Domain_Utilities.DomainDescriptors domains) {
domains.numDescriptors = 0;
}


public static Integer allocDomainDescriptorIndex(M25_Domain_Utilities.DomainDescriptors domains) {
Integer returnValue;
returnValue = -1;

if (domains.numDescriptors == 0) {
domains.descriptors =  new M25_Domain_Utilities.DomainDescriptor[M01_Common.gc_allocBlockSize];
} else if (domains.numDescriptors >= M00_Helper.uBound(domains.descriptors)) {
M25_Domain_Utilities.DomainDescriptor[] descriptorsBackup = domains.descriptors;
domains.descriptors =  new M25_Domain_Utilities.DomainDescriptor[domains.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M25_Domain_Utilities.DomainDescriptor value : descriptorsBackup) {
domains.descriptors[indexCounter] = value;
indexCounter++;
}
}
domains.numDescriptors = domains.numDescriptors + 1;
returnValue = domains.numDescriptors;
return returnValue;
}


public static void initDomainDescriptorRefs(M25_Domain_Utilities.DomainDescriptorRefs refs) {
refs.numRefs = 0;
}

public static void addDomainDescriptorRef(M25_Domain_Utilities.DomainDescriptorRefs refs, int ref, Boolean isNullableW, Boolean distinguishNullabilityW) {
boolean isNullable; 
if (isNullableW == null) {
isNullable = false;
} else {
isNullable = isNullableW;
}

boolean distinguishNullability; 
if (distinguishNullabilityW == null) {
distinguishNullability = false;
} else {
distinguishNullability = distinguishNullabilityW;
}

int i;

// check if this domain is already listed
for (i = 1; i <= 1; i += (1)) {
if (refs.refs[i].ref == ref &  (!(distinguishNullability |  refs.refs[i].isNullable == isNullable))) {
return;
}
}

// domain is not listed -> add it
if (refs.numRefs == 0) {
refs.refs =  new M25_Domain_Utilities.DomainDescriptorRefs[M01_Common.gc_allocBlockSize];
} else if (refs.numRefs >= M00_Helper.uBound(refs.refs)) {
M25_Domain_Utilities.DomainDescriptorRefs[] refsBackup = refs.refs;
refs.refs =  new M25_Domain_Utilities.DomainDescriptorRefs[refs.numRefs + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M25_Domain_Utilities.DomainDescriptorRefs value : refsBackup) {
refs.refs[indexCounter] = value;
indexCounter++;
}
}
refs.numRefs = refs.numRefs + 1;
refs.refs[refs.numRefs].ref = ref;
refs.refs[refs.numRefs].isNullable = isNullable;
}

}