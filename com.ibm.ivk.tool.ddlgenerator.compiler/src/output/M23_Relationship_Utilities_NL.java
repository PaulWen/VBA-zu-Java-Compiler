package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M23_Relationship_Utilities_NL {




class RelationshipNlDescriptor {
public String i18nId;

public String[] nl;

// derived attributes
public String relationshipIndex;

public RelationshipNlDescriptor(String i18nId, String relationshipIndex, String[] nl) {
this.i18nId = i18nId;
this.relationshipIndex = relationshipIndex;
this.nl = nl;
}
}

class RelationshipNlDescriptors {
public M23_Relationship_Utilities_NL.RelationshipNlDescriptor[] descriptors;
public int numDescriptors;

public RelationshipNlDescriptors(int numDescriptors, M23_Relationship_Utilities_NL.RelationshipNlDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}

public static Integer allocRelationshipNlDescriptorIndex(M23_Relationship_Utilities_NL.RelationshipNlDescriptors relationshipNls) {
Integer returnValue;
returnValue = -1;

if (M23_Relationship_NL.numLangsForRelationshipsNl > 0) {
if (relationshipNls.numDescriptors == 0) {
relationshipNls.descriptors =  new M23_Relationship_Utilities_NL.RelationshipNlDescriptor[M01_Common.gc_allocBlockSize];
} else if (relationshipNls.numDescriptors >= M00_Helper.uBound(relationshipNls.descriptors)) {
M23_Relationship_Utilities_NL.RelationshipNlDescriptor[] descriptorsBackup = relationshipNls.descriptors;
relationshipNls.descriptors =  new M23_Relationship_Utilities_NL.RelationshipNlDescriptor[relationshipNls.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M23_Relationship_Utilities_NL.RelationshipNlDescriptor value : descriptorsBackup) {
relationshipNls.descriptors[indexCounter] = value;
indexCounter++;
}
}
relationshipNls.numDescriptors = relationshipNls.numDescriptors + 1;
relationshipNls.descriptors[relationshipNls.numDescriptors].nl =  new String[M23_Relationship_NL.numLangsForRelationshipsNl];
returnValue = relationshipNls.numDescriptors;
}
return returnValue;
}


public static String getPrimaryRelationshipLabelByIndex(int relationshipIndex) {
String returnValue;
returnValue = "<unknown relationship>";
int i;
int langId;
int minLangId;
minLangId = 9999;

if (relationshipIndex > 0) {
if (M23_Relationship.g_relationships.descriptors[relationshipIndex].relNlIndex > 0) {
if (!(M04_Utilities.strArrayIsNull(M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[relationshipIndex].relNlIndex].nl))) {
for (int langId = M00_Helper.lBound(M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[relationshipIndex].relNlIndex].nl); langId <= M00_Helper.uBound(M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[relationshipIndex].relNlIndex].nl); langId++) {
if (M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[relationshipIndex].relNlIndex].nl[langId] != "") {
if (langId == M01_Globals_IVK.gc_langIdEnglish) {
returnValue = M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[relationshipIndex].relNlIndex].nl[langId];
return returnValue;
}
if (langId.compareTo(minLangId) < 0) {
returnValue = M23_Relationship_NL.g_relationshipsNl.descriptors[M23_Relationship.g_relationships.descriptors[relationshipIndex].relNlIndex].nl[langId];
minLangId = langId;
}
}
}
}
}
}
return returnValue;
}


}