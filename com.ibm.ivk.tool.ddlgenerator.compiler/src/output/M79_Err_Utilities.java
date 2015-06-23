package output;

import org.apache.poi.ss.usermodel.*;
import java.nio.file.*;
import java.text.*;
import java.util.Date;

public class M79_Err_Utilities {




class ErrDescriptor {
public String id;
public boolean isTechnical;
public int sqlStateOffset;
public String busErrorMessageNo;
public String messagePattern;
public String messageExplanation;
public String conEnumLabelText;

public ErrDescriptor(String id, boolean isTechnical, int sqlStateOffset, String busErrorMessageNo, String messagePattern, String messageExplanation, String conEnumLabelText) {
this.id = id;
this.isTechnical = isTechnical;
this.sqlStateOffset = sqlStateOffset;
this.busErrorMessageNo = busErrorMessageNo;
this.messagePattern = messagePattern;
this.messageExplanation = messageExplanation;
this.conEnumLabelText = conEnumLabelText;
}
}

class ErrDescriptors {
public M79_Err_Utilities.ErrDescriptor[] descriptors;
public int numDescriptors;

public ErrDescriptors(int numDescriptors, M79_Err_Utilities.ErrDescriptor[] descriptors) {
this.numDescriptors = numDescriptors;
this.descriptors = descriptors;
}
}


public static void initErrDescriptors(M79_Err_Utilities.ErrDescriptors errs) {
errs.numDescriptors = 0;
}


public static Integer allocErrDescriptorIndex(M79_Err_Utilities.ErrDescriptors errs) {
Integer returnValue;
returnValue = -1;

if (errs.numDescriptors == 0) {
errs.descriptors =  new M79_Err_Utilities.ErrDescriptor[M01_Common.gc_allocBlockSize];
} else if (errs.numDescriptors >= M00_Helper.uBound(errs.descriptors)) {
M79_Err_Utilities.ErrDescriptor[] descriptorsBackup = errs.descriptors;
errs.descriptors =  new M79_Err_Utilities.ErrDescriptor[errs.numDescriptors + M01_Common.gc_allocBlockSize];
//alte Daten in das neue Array übernehmen
int indexCounter = 0;
for (M79_Err_Utilities.ErrDescriptor value : descriptorsBackup) {
errs.descriptors[indexCounter] = value;
indexCounter++;
}
}
errs.numDescriptors = errs.numDescriptors + 1;
returnValue = errs.numDescriptors;
return returnValue;
}



}