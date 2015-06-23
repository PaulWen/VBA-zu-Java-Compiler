package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M71_Org_Utilities {




class OrgDescriptor {
public int id;
public String name;
public boolean isPrimary;
public int oid;
public int sequenceCacheSize;
public boolean isTemplate;

public int oidSequenceCount;

// derived attributes
public int setProductiveTargetPoolId;
public int setProductiveTargetPoolIndex;

public OrgDescriptor(int id, String name, boolean isPrimary, int oid, int sequenceCacheSize, boolean isTemplate, int oidSequenceCount, int setProductiveTargetPoolId, int setProductiveTargetPoolIndex) {
this.id = id;
this.name = name;
this.isPrimary = isPrimary;
this.oid = oid;
this.sequenceCacheSize = sequenceCacheSize;
this.isTemplate = isTemplate;
this.oidSequenceCount = oidSequenceCount;
this.setProductiveTargetPoolId = setProductiveTargetPoolId;
this.setProductiveTargetPoolIndex = setProductiveTargetPoolIndex;
}
}

class OrgDescriptors {
public M71_Org_Utilities.OrgDescriptor[] descriptors;
public int numDescriptors;

public OrgDescriptors(int numDescriptors, M71_Org_Utilities.OrgDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initOrgDescriptors(M71_Org_Utilities.OrgDescriptors orgs) {
orgs.numDescriptors = 0;
}


public static Integer allocOrgIndex(M71_Org_Utilities.OrgDescriptors orgs) {
Integer returnValue;
returnValue = -1;

if (orgs.numDescriptors == 0) {
orgs.descriptors =  new M71_Org_Utilities.OrgDescriptor[M01_Common.gc_allocBlockSize];
} else if (orgs.numDescriptors >= M00_Helper.uBound(orgs.descriptors)) {
M71_Org_Utilities.OrgDescriptor[] descriptorsBackup = orgs.descriptors;
orgs.descriptors =  new M71_Org_Utilities.OrgDescriptor[orgs.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M71_Org_Utilities.OrgDescriptor value : descriptorsBackup) {
orgs.descriptors[indexCounter] = value;
indexCounter++;
}
}
orgs.numDescriptors = orgs.numDescriptors + 1;

orgs.descriptors[orgs.numDescriptors].oidSequenceCount = 1;
returnValue = orgs.numDescriptors;
return returnValue;
}


public static Integer getEffectiveOrgId(int thisOrgId, boolean isCommon) {
Integer returnValue;
returnValue = (isCommon ? -1 : thisOrgId);
return returnValue;
}


public static Integer getEffectiveOrgIndex( int thisOrgIndex, boolean isCommon) {
Integer returnValue;
returnValue = (isCommon ? -1 : thisOrgIndex);
return returnValue;
}

public static Integer getPrimaryOrgId() {
Integer returnValue;
returnValue = -1;

int i;
for (int i = 1; i <= M71_Org.g_orgs.numDescriptors; i++) {
if (M71_Org.g_orgs.descriptors[i].isPrimary) {
returnValue = M71_Org.g_orgs.descriptors[i].id;
return returnValue;
}
}
return returnValue;
}


public static Integer getPrimaryOrgIndex() {
Integer returnValue;
returnValue = -1;

int i;
for (int i = 1; i <= M71_Org.g_orgs.numDescriptors; i++) {
if (M71_Org.g_orgs.descriptors[i].isPrimary) {
returnValue = i;
return returnValue;
}
}
return returnValue;
}


public static Integer getMinOrgId() {
Integer returnValue;
int result;

result = -1;
returnValue = -1;

int i;
for (int i = 1; i <= M71_Org.g_orgs.numDescriptors; i++) {
if (!(M71_Org.g_orgs.descriptors[i].isTemplate &  (result < 0 |  result > M71_Org.g_orgs.descriptors[i].id))) {
result = M71_Org.g_orgs.descriptors[i].id;
returnValue = M71_Org.g_orgs.descriptors[i].id;
}
}
return returnValue;
}


public static Integer getMaxOrgId() {
Integer returnValue;
int result;

result = -1;
returnValue = -1;

int i;
for (int i = 1; i <= M71_Org.g_orgs.numDescriptors; i++) {
if (!(M71_Org.g_orgs.descriptors[i].isTemplate &  (result < 0 |  result < M71_Org.g_orgs.descriptors[i].id))) {
result = M71_Org.g_orgs.descriptors[i].id;
returnValue = M71_Org.g_orgs.descriptors[i].id;
}
}
return returnValue;
}


public static Integer pullOrgOidByIndex( int thisOrgIndex) {
Integer returnValue;
returnValue = -1;

if (thisOrgIndex >= 1 &  thisOrgIndex <= M71_Org.g_orgs.numDescriptors) {
returnValue = M71_Org.g_orgs.descriptors[thisOrgIndex].oidSequenceCount;
M71_Org.g_orgs.descriptors[thisOrgIndex].oidSequenceCount = M71_Org.g_orgs.descriptors[thisOrgIndex].oidSequenceCount + 1;
}
return returnValue;
}


}