package com.ibm.ivk.tool.ddlgenerator.compiler.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Die Klasse bündelt alle Informationen, die bei einem Vba zu Java Kompiliervorgang benötigt werden,
 * um im Java-Code die statischen Bezüge richtige zu setzen.
 * 
 * @author Paul Wenzel, wenzel.paul@de.ibm.com
 *
 */
public class VisitorDataObject {

	/** Liste, in welcher die Namen aller Variablen (Key) und deren Datentyp (Value) gespeichert sind */
	private HashMap<String, String> variablesDataType;
	/** Liste, in welcher die Namen aller Variablen (Key) und wie man sie aufrufen kann (Value) gespeichert sind */
	private HashMap<String, String> globalVariablesCall;
	/** Liste, in welcher die Namen aller Methoden (Key) und die Datentypen der Rückgabewerte (Value) gespeichert sind */
	private HashMap<String, String> methodsDataType;
	/** Liste, in welcher die Namen aller Methoden (Key) und die Datentypen der Rückgabewerte (Value) gespeichert sind */
	private HashMap<String, String> privateMethodsDataType;
	/** Liste, in welcher alle Methoden Namen (Key) und die Anzahl der Parameter die sie haben (Value) gespeichert sind */
	private HashMap<String, Integer> methodsParameters;
	/** Liste, in welcher alle private Methoden Namen (Key) und die Anzahl der Parameter die sie haben (Value) gespeichert sind */
	private HashMap<String, Integer> privateMethodsParameters;
	/** Liste, in welcher alle Methoden Namen (Key) und wie man sie aufrufen kann (Value) gespeichert sind */
	private HashMap<String, String> methodsCall;
	/** Liste, in welcher alle Namen der selbst definierten Klassen (Key) und wie man sie aufrufen kann (Value) gespeichert sind */
	private HashMap<String, String> objectTypeCall;
	/** Liste, in welcher alle Namen der selbst definierten Enums gespeichert sind */
	private ArrayList<String> enumNames;
	/** Liste, in welcher alle Namen von Arrays gespeichert sind */
	private ArrayList<String> arrayNames;
	
	
	public VisitorDataObject() {
		variablesDataType = new  HashMap<String, String>();
		globalVariablesCall = new  HashMap<String, String>();
		methodsDataType = new HashMap<String, String>(); 
		privateMethodsDataType = new HashMap<String, String>(); 
		methodsParameters = new HashMap<String, Integer>();
		privateMethodsParameters = new HashMap<String, Integer>();
		methodsCall = new HashMap<String, String>(); 
		objectTypeCall = new HashMap<String, String>(); 
		enumNames = new ArrayList<String>(); 
		arrayNames = new ArrayList<String>();
	}
	
	public void addVariablesDataType(String id, String dataType) {
		variablesDataType.put(id, dataType);
	}
	public String getVariablesDataType(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		return variablesDataType.get(id); 
	}
	
	public void addGlobalVariablesCall(String id, String call) {
			globalVariablesCall.put(id, call);
	}
	public String getGlobalVaraiblesCall(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		return globalVariablesCall.get(id); 
	}

	public void addMethodsDataType(String id, String dataType) {
			methodsDataType.put(id, dataType);
	}
	public String getMethodsDataType(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		return methodsDataType.get(id); 
	}
	
	public void addPrivateMethodsDataType(String id, String dataType) {
			privateMethodsDataType.put(id, dataType);
	}
	public String getPrivateMethodsDataType(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		return privateMethodsDataType.get(id); 
	}
	
	public void addMethodsParameters(String id, int parameters) {
			methodsParameters.put(id, parameters);
	}
	public int getMethodsParameters(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		if (methodsParameters.get(id) != null) {
			return methodsParameters.get(id);
		} else {
			return -1;
		}
	}
	
	public void addPrivateMethodsParameters(String id, int parameters) {
		privateMethodsParameters.put(id, parameters);
	}
	public int getPrivateMethodsParameters(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		if (privateMethodsParameters.get(id) != null) {
			return privateMethodsParameters.get(id);
		} else {
			return -1;
		}
	}
	
	public void addMethodsCall(String id, String call) {
			methodsCall.put(id, call);
	}
	public String getMethodsCall(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		return methodsCall.get(id); 
	}

	public void addObjectTypeCall(String id, String call) {
			objectTypeCall.put(id, call);
	}
	public String getObjectTypeCall(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		return objectTypeCall.get(id); 
	}
	
	public void addEnumNames(String id) {
			enumNames.add(id);
	}
	public boolean isEnum(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		if (enumNames.contains(id)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void addArrayName(String id) {
			arrayNames.add(id);
	}
	public boolean isArray(String id) {
		id = id.substring((id.lastIndexOf('.') == -1 ? 0 : id.lastIndexOf('.') + 1), id.length());
		if (arrayNames.contains(id)) {
			return true;
		} else {
			return false;
		}
	}
}