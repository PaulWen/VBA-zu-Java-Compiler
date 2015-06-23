package com.ibm.ivk.tool.ddlgenerator.compiler;

import java.util.LinkedList;

import com.ibm.ivk.tool.ddlgenerator.compiler.util.VisitorDataObject;
import com.ibm.ivk.tool.ddlgenerator.sablecc.analysis.DepthFirstAdapter;
import com.ibm.ivk.tool.ddlgenerator.sablecc.node.*;

/**
 * Die Klasse implementiert auf der Basis vom {@link DepthFirstAdapter} ein Analysewerkzeug,
 * um in einem Syntaxbaum dargestellten VBA-Code in Java-Code zu übersetzen.
 * 
 * @author Paul Wenzel, wenzel.paul@de.ibm.com
 *
 */
public class Visitor extends DepthFirstAdapter {

	/** die Variable ist TRUE, wenn eine Variable deklariert wird */
	private static boolean varDec = false;
	/** die Variable ist TRUE, wenn ein Enum definiert wird */
	private static boolean enumDec = false;
	/** die Variable ist TRUE, wenn ein Objekt-Datentyp definiert wird */
	private static boolean typeDec = false;
	/** die Variable ist TRUE, wenn eine Methode definiert wird */
	private static boolean methodDec = false;
	
	/** 
	 * Die Werte eines Enums bekommen numerische Werte zugewiesen.
	 * Damit diese numerischen Werte nicht doppelt innerhalb einer Enum-Definition vorkommen wird über diese Variable mitgezählt.
	 */
	private static int nextEnumValue = 0;
	
	
	/** 
	 * Das Objekt bündelt alle Informationen, die bei einem Vba zu Java Kompiliervorgang benötigt werden,
	 * um im Java-Code die statischen Bezüge richtige zu setzen.
	 */
	private static VisitorDataObject visitorDataObject = new VisitorDataObject();
	
	/** der Name des Moduls, welches aktuell übersetzt wird (ist in der Übersetzung (in Java) der Klassenname) */
	private String className;
	
	/** beinhaltet die Übersetzung */
	private StringBuffer result;

	
	
	public Visitor(String className) {
		this.className = className;
		
		result = new StringBuffer();
	}
	
	
	public StringBuffer getResult() {
		return result;
	}

	public VisitorDataObject getVisitorDataObject() {
		return visitorDataObject;
	}
	
////////////////////Listen////////////////////
	

////////////////////File Statements////////////////////

	@Override
	public void caseAVarDecFileStmt(AVarDecFileStmt node) {
		varDec = true;

		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getVarDec().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
		
		// VisitorDataObjekt updaten
		if (!result.contains("private")) {
			// VariablesCall
			visitorDataObject.addGlobalVariablesCall(((AId)((AModifierId)((AVarDec)node.getVarDec()).getModifierId()).getId()).getIdToken().getText(), className + "." + ((AId)((AModifierId)((AVarDec)node.getVarDec()).getModifierId()).getId()).getIdToken().getText());
		}
		// VisitorDataObjekt updaten ENDE
	}
	
	@Override
	public void caseAVarDecInFileStmt(AVarDecInFileStmt node) {
		varDec = true;
		
		String result = "";

		Visitor visitor = new Visitor(className);
		node.getVarDecIn().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);

		// VisitorDataObjekt updaten
		Visitor idVisitor = new Visitor(className);
		((AModifierId)((AVarDecIn)node.getVarDecIn()).getModifierId()).getId().apply(idVisitor);
		if (!result.contains("private")) {
			// VariablesCall
			visitorDataObject.addGlobalVariablesCall(idVisitor.getResult().toString(), className + "." + idVisitor.getResult().toString());
		}
		// VisitorDataObjekt updaten ENDE
	}
	
	@Override
	public void caseAArrayDecFileStmt(AArrayDecFileStmt node) {
		varDec = true;
		
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getArrayDec().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
		
		
		// VisitorDataObjekt updaten
		Visitor dataTypeVisitor = new Visitor(className);
		((AArrayDec)node.getArrayDec()).getDataType().apply(dataTypeVisitor);
		
		Visitor idVisitor = new Visitor(className);
		((AModifierId)((AArrayDec)node.getArrayDec()).getModifierId()).getId().apply(idVisitor);
		
		// VaraiablesDataType
		if (!result.contains("private")) {
			// VariablesCall
			visitorDataObject.addGlobalVariablesCall(idVisitor.getResult().toString(), className + "." + idVisitor.getResult().toString());
		}
		// VisitorDataObjekt updaten ENDE
	}
	
	@Override
	public void caseAEnumDecInFileStmt(AEnumDecInFileStmt node) {
		enumDec = true;
		
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getEnumDecIn().apply(visitor);
		result += visitor.getResult();

		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);

		// VisitorDataObjekt updaten
		Visitor idVisitor = new Visitor(className);
		((AEnumDecIn)node.getEnumDecIn()).getId().apply(idVisitor);
		
			// EnumName
		visitorDataObject.addEnumNames(idVisitor.getResult().toString());
		// VisitorDataObjekt updaten ENDE

		enumDec = false;
	}
	
	@Override
	public void caseATypeDecFileStmt(ATypeDecFileStmt node) {
		typeDec = true;
		
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getTypeDec().apply(visitor);
		result += visitor.getResult();
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);

		// VisitorDataObjekt updaten
		if (!result.contains("private")) {
			Visitor idVisitor = new Visitor(className);
			((ATypeDec)node.getTypeDec()).getId().apply(idVisitor);
			
			// DataTypeCall
			visitorDataObject.addObjectTypeCall(idVisitor.getResult().toString(), className + "." + idVisitor.getResult().toString());
		}
		// VisitorDataObjekt updaten ENDE
		
		typeDec = false;
	}
	
////////////////////Function Statements////////////////////

	@Override
	public void caseAVarDecFunctionStmt(AVarDecFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getVarDec().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAVarDecInWithDataTypeFunctionStmt(AVarDecInWithDataTypeFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getVarDecInWithDataType().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAVarDecInFunctionStmt(AVarDecInFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getVarDecIn().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAVarDecListFunctionStmt(AVarDecListFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getVarDecList().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAVarSetFunctionStmt(AVarSetFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getVarSet().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAMethodCallFunctionStmt(AMethodCallFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getMethodCall().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAMethodCallWithoutParensFunctionStmt(AMethodCallWithoutParensFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getMethodCallWithoutParens().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAWaitSubCallFunctionStmt(AWaitSubCallFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getWaitSubCall().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAArrayDecFunctionStmt(AArrayDecFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getArrayDec().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
		
		// VisitorDataObjekt updaten
		Visitor idVisitor = new Visitor(className);
		if (node.getArrayDec() instanceof AArrayDec) {
			((AModifierId)((AArrayDec)node.getArrayDec()).getModifierId()).getId().apply(idVisitor);
		} else if (node.getArrayDec() instanceof ARangeArrayDec) {
			((AModifierId)((ARangeArrayDec)node.getArrayDec()).getModifierId()).getId().apply(idVisitor);
		}
		// VisitorDataObjekt updaten ENDE
	}
	
	@Override
	public void caseAArrayResizeFunctionStmt(AArrayResizeFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getArrayResize().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}

	@Override
	public void caseAArrayResizePreserveDataFunctionStmt(AArrayResizePreserveDataFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getArrayResizePreserveData().apply(visitor);
		result += visitor.getResult();

		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAArraySetElementFunctionStmt(AArraySetElementFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getArraySetElement().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAExitSubFunctionStmt(AExitSubFunctionStmt node) {
		String result = "";
		
		result += "return";

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAExitFunctionFunctionStmt(AExitFunctionFunctionStmt node) {
		String result = "";
		
		result += "return returnValue";

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAExitForFunctionStmt(AExitForFunctionStmt node) {
		String result = "";
		
		result += "break";

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAOpenFileFunctionFunctionStmt(AOpenFileFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getOpenFileFunction().apply(visitor);
		result += visitor.getResult();

		result += ";";

		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseACloseFileFunctionFunctionStmt(ACloseFileFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getCloseFileFunction().apply(visitor);
		result += visitor.getResult();
		
		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAPrintFunctionFunctionStmt(APrintFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getPrintFunction().apply(visitor);
		result += visitor.getResult();
		
		result += ";";

		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseADebugPrintFunctionFunctionStmt(ADebugPrintFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getDebugPrintFunction().apply(visitor);
		result += visitor.getResult();
		
		result += ";";

		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAKillFunctionFunctionStmt(AKillFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getKillFunction().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseARenameFileFunctionFunctionStmt(ARenameFileFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getRenameFileFunction().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAMsgBoxFunctionWithoutParensFunctionStmt(AMsgBoxFunctionWithoutParensFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getMsgBoxFunctionWithoutParens().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAExcelSheetFunctionStmt(AExcelSheetFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getExcelSheet().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseASelectFunctionFunctionStmt(ASelectFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getSelectFunction().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAMoveFunctionFunctionStmt(AMoveFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getMoveFunction().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAAutoFitFunctionFunctionStmt(AAutoFitFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getAutoFitFunction().apply(visitor);
		result += visitor.getResult();
		
		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseASetNameExcelFunctionFunctionStmt(ASetNameExcelFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getSetNameExcelFunction().apply(visitor);
		result += visitor.getResult();
		
		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}

	@Override
	public void caseACopyFunctionFunctionStmt(ACopyFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getCopyFunction().apply(visitor);
		result += visitor.getResult();
		
		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseADeleteFunctionFunctionStmt(ADeleteFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getDeleteFunction().apply(visitor);
		result += visitor.getResult();
		
		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAActivateFunctionFunctionStmt(AActivateFunctionFunctionStmt node) {
		String result = "";
		
		result += "M00_Excel.activateSheet(";
		
		Visitor sheetVisitor = new Visitor(className);
		node.getActivateFunction().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();
		
		result += ");\n";
		
		this.result.append(result);
	}
	
	@Override
	public void caseASetCellFunctionFunctionStmt(ASetCellFunctionFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getSetCellFunction().apply(visitor);
		result += visitor.getResult();
		
		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseATryFunctionStmt(ATryFunctionStmt node) {
		String result = "";
		
		// Befehl auskommentieren
		result += "//" + node.getTry().toString();
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAGotoCallFunctionStmt(AGotoCallFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getGotoCall().apply(visitor);
		result += visitor.getResult();
		
		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);

	}
	
	@Override
	public void caseAGotoCodeStartFunctionStmt(AGotoCodeStartFunctionStmt node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getGotoCodeStart().apply(visitor);
		result += visitor.getResult();
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
////////////////////General Statements////////////////////
	
	@Override
	/**
	 * Zeilenumbruch
	 */
	public void caseAEndOfLineGeneralStmt(AEndOfLineGeneralStmt node) {
		result.append("\n");
	}
	
	@Override
	/**
	 * einzeiliger Kommentar
	 * 
	 * VBA:'Ich bin ein Kommentar
	 */
	public void caseACommentGeneralStmt(ACommentGeneralStmt node) {
		String comment = ((AComment)node.getComment()).getCommentToken().getText();
		comment = comment.replaceFirst("'", "//");
		result.append(comment + "\n");
	}
	
////////////////////Werte////////////////////
	
	@Override
	public void caseANullValue(ANullValue node) {
		String result = "";

		result += "null";

		
		this.result.append(result);
	}
	
	@Override
	public void caseAStringValueWithParens(AStringValueWithParens node) {
		String result = "";

		result += "(";

		Visitor stringVisitor = new Visitor(className);
		node.getStringValueWithoutParens().apply(stringVisitor);
		result += stringVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAId(AId node) {
		String result = "";
		
		String id = node.getIdToken().getText();
		// falls ID "default" heißt diese Id zuerst umbennenen, da "default" in Java ein Schlüsselwort ist
		if (id.equals("default")) {
			id = "defaultValue";
		}
		
		if (!typeDec && !enumDec && !varDec && visitorDataObject.getGlobalVaraiblesCall(id) != null) {
			// wenn es sich nicht um eine Enum oder Variablen Definition handlet (also um den Namen der Methode bzw. vom Enum) UND
			// es sich nicht um ein Datenfeld innerhalb einer Klassendefinition handelt
			result = visitorDataObject.getGlobalVaraiblesCall(id);
		} else if (!methodDec && visitorDataObject.getMethodsCall(id) != null) {
			// wenn es sich nicht um einen Methoden definition, also um einen Methodennamen handelt
			result = visitorDataObject.getMethodsCall(id);
		} else if (!typeDec && visitorDataObject.getObjectTypeCall(id) != null) {
			// wenn es sich nicht um eine Klassen definition, also um einen Klassennamen handelt
			result = visitorDataObject.getObjectTypeCall(id);
		} else if (!enumDec && visitorDataObject.isEnum(id)) {
			// wenn es sich um den Datentyp eines optionalen Parameter handelt, aber nicht um den Namen einer Enum-Klasse
			result = "Integer";
		} else {
			// wenn die ID in keiner Liste besonders behandelt wird
			result = id;
			varDec = false;
			methodDec = false;
		}
		
		this.result.append(result);
	}
	@Override
	public void caseANotId(ANotId node) {
		String result = "";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += "!" + idVisitor.getResult().toString();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAIdInParensValue(AIdInParensValue node) {
		String result = "";

		result += "(";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseABooleanValueTrue(ABooleanValueTrue node) {
		String result = "true";
		this.result.append(result);
	}
	@Override
	public void caseABooleanValueFalse(ABooleanValueFalse node) {
		String result = "false";
		this.result.append(result);
	}
	
	@Override
	public void caseAStringStringPartValue(AStringStringPartValue node) {
		String result = "";
		
		result += node.getStringToken().getText();
		
		if (result.length() > 2) {
			// falls das Zeichen '\' innerhalb des Strings vorkommt, muss dieses maskiert werden
			result = '"' + result.substring(1, result.length() - 1).replace("\\", "\\\\") + '"';
			// falls das Zeichen '"' innerhalb des Strings vorkommt, muss dieses maskiert werden
			result = '"' + result.substring(1, result.length() - 1).replace("\"\"", "\\\"") + '"';
		}
		
		this.result.append(result);
	}
	@Override
	public void caseAArithmeticExpressionInParensStringPartValue(AArithmeticExpressionInParensStringPartValue node) {
		String result = "";

		result += "(";
		
		Visitor arithmeticExpressionVisitor = new Visitor(className);
		node.getArithmeticExpression().apply(arithmeticExpressionVisitor);
		result += arithmeticExpressionVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAOnlyValueCharacterString(AOnlyValueCharacterString node) {
		String result = "";
		
		result += node.getStringToken().getText();
		
		if (result.length() > 2) {
			// falls das Zeichen '\' innerhalb des Strings vorkommt, muss dieses maskiert werden
			result = '"' + result.substring(1, result.length() - 1).replace("\\", "\\\\") + '"';
			// falls das Zeichen '"' innerhalb des Strings vorkommt, muss dieses maskiert werden
			result = '"' + result.substring(1, result.length() - 1).replace("\"\"", "\\\"") + '"';
		}
		
		this.result.append(result);
	}
	
	@Override
	public void caseACharacterString(ACharacterString node) {
		String result = "";
		
		Visitor firstStringPartValueVisitor = new Visitor(className);
		node.getFirst().apply(firstStringPartValueVisitor);
		result += firstStringPartValueVisitor.getResult().toString()  + " + ";
		
		for (PStringPart stringPart : node.getStringPart()) {
			Visitor stringPartvalueVisitor = new Visitor(className);
			((AStringPart) stringPart).getStringPartValue().apply(stringPartvalueVisitor);
			result += stringPartvalueVisitor.getResult().toString()  + " + ";
		}
		Visitor stringPartValueVisitor = new Visitor(className);
		node.getStringPartValue().apply(stringPartValueVisitor);
		result += stringPartValueVisitor.getResult().toString();
		
		this.result.append(result);
	}
	
	
	@Override
	public void caseAOnlyValueArithmeticExpression(AOnlyValueArithmeticExpression node) {
		String result = "";
		
		result += node.getNumber().getText();
		
		this.result.append(result);
	}
	
	
	@Override
	public void caseANegateIdArithmeticExpression(ANegateIdArithmeticExpression node) {
		String result = "";
		
		result += "- ";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		this.result.append(result);
	}
	
	@Override
	public void caseANegatedArithmeticExpressionInParensArithmeticExpression(ANegatedArithmeticExpressionInParensArithmeticExpression node) {
		String result = "";
		
		result += "-(";
		
		Visitor arithmeticExpressionVisitor = new Visitor(className);
		node.getArithmeticExpression().apply(arithmeticExpressionVisitor);
		result += arithmeticExpressionVisitor.getResult().toString();

		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAArithmeticExpression(AArithmeticExpression node) {
		String result = "";
		
		LinkedList<PArithmeticExpressionPart> arithmeticExpresssionPartList = new LinkedList<PArithmeticExpressionPart>();
		arithmeticExpresssionPartList.add(node.getFirst());
		arithmeticExpresssionPartList.addAll(node.getArithmeticExpressionPart());
		
		for (PArithmeticExpressionPart arithmeticExpressionPart : arithmeticExpresssionPartList) {
			Visitor arithmeticExpressionValueVisitor = new Visitor(className);
			((AArithmeticExpressionPart)arithmeticExpressionPart).getArithmeticExpressionPartValue().apply(arithmeticExpressionValueVisitor);
			result += arithmeticExpressionValueVisitor.getResult().toString()  + " ";
			
			Visitor arithmeticOperatorVisitor = new Visitor(className);
			((AArithmeticExpressionPart)arithmeticExpressionPart).getArithmeticOperator().apply(arithmeticOperatorVisitor);
			result += arithmeticOperatorVisitor.getResult().toString() + " ";
		}
		
		Visitor arithmeticExpressionValueVisitor = new Visitor(className);
		node.getArithmeticExpressionPartValue().apply(arithmeticExpressionValueVisitor);
		result += arithmeticExpressionValueVisitor.getResult().toString();
		
		this.result.append(result);
	}
	@Override
	public void caseAArithmeticExpressionInParensValue(AArithmeticExpressionInParensValue node) {
		String result = "";

		result += "(";
		
		Visitor arithmeticExpressionVisitor = new Visitor(className);
		node.getArithmeticExpression().apply(arithmeticExpressionVisitor);
		result += arithmeticExpressionVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseANumberArithmeticExpressionPartValueNotNegated(ANumberArithmeticExpressionPartValueNotNegated node) {
		String result = "";
		result += node.getNumber().getText();
		this.result.append(result);
	}
	
	@Override
	public void caseAArithmeticExpressionPartValueNegated(AArithmeticExpressionPartValueNegated node) {
		String result = "";

		result += "(- ";
		
		Visitor arithmeticExpressionValueVisitor = new Visitor(className);
		node.getArithmeticExpressionPartValueNotNegated().apply(arithmeticExpressionValueVisitor);
		result += arithmeticExpressionValueVisitor.getResult().toString();
		
		result += ")";

		this.result.append(result);
	}
	
	@Override
	public void caseAMethodChaining(AMethodChaining node) {
		String result = "";
		
		LinkedList<PMethodChainingValue> methodChainingPartList = new LinkedList<PMethodChainingValue>();
		
		// alle MethodChaining Parts in einer Liste sammeln
		methodChainingPartList.add(((AMethodChainingPart)node.getFirst()).getMethodChainingValue());
		for (PMethodChainingPart methodChainingPart : node.getMethodChainingPart()) {
			if (methodChainingPart instanceof AMethodChainingPart) {
				methodChainingPartList.add(((AMethodChainingPart) methodChainingPart).getMethodChainingValue());
			}
		}
		methodChainingPartList.add(node.getMethodChainingValue());
		
		// JAVA
		for (PMethodChainingValue methodChainingValue : methodChainingPartList) {
			Visitor methodChainingValueVisitor = new Visitor(className);
			methodChainingValue.apply(methodChainingValueVisitor);
			result += methodChainingValueVisitor.getResult().toString()  + ".";
		}
		
		// das letzte "." wieder entfernen
		result = result.substring(0, result.length() - 1);

		
		// Ausnahmen behandlen
		String[] split = result.split("\\.");
		if (split[split.length - 2].startsWith("M")) {
			result = result.replace("." + split[split.length - 2], "");
		}

		this.result.append(result);
	}
	
////////////////////Datentypen////////////////////
	
	@Override
	public void caseAIntegerDataType(AIntegerDataType node) {
		String result = "int";
		this.result.append(result);
	}
	
	@Override
	public void caseALongDataType(ALongDataType node) {
		String result = "long";
		this.result.append(result);
	}
	
	@Override
	public void caseASingleDataType(ASingleDataType node) {
		String result = "double";
		this.result.append(result);
	}
	
	@Override
	public void caseAStringDataType(AStringDataType node) {
		String result = "String";
		this.result.append(result);
	}
	
	@Override
	public void caseABooleanDataType(ABooleanDataType node) {
		String result = "boolean";
		this.result.append(result);
	}

	@Override
	public void caseAIntegerArrayDataType(AIntegerArrayDataType node) {
		String result = "int[]";
		this.result.append(result);
	}
	
	@Override
	public void caseAIdDataType(AIdDataType node) {
		String result = "";
		
		if (visitorDataObject.getObjectTypeCall(((AId)node.getId()).getIdToken().getText()) != null) {
			result = visitorDataObject.getObjectTypeCall(((AId)node.getId()).getIdToken().getText());
		} else if (visitorDataObject.isEnum(((AId)node.getId()).getIdToken().getText())) {
			result = "Integer";
		} else {
			result = ((AId)node.getId()).getIdToken().getText();
		}
		
		this.result.append(result);
	}
	
	@Override
	public void caseAWorkbookDataType(AWorkbookDataType node) {
		//TODO chek
		String result = "";
		
		result += "Workbook";
		
		this.result.append(result);
	}
	@Override
	public void caseAWorksheetDataType(AWorksheetDataType node) {
		String result = "";
		
		result += "Sheet";
		
		this.result.append(result);
	}
	
////////////////////Modifier////////////////////
	
	@Override
	public void caseAByValModifier(AByValModifier node) {
	}
	
	@Override
	public void caseAStaticModifier(AStaticModifier node) {
		// wird bereits bei den anderen Modifier mitgegeben!
	}
	
	@Override
	public void caseAPrivateModifier(APrivateModifier node) {
		String result = "private static";
		this.result.append(result);
	}
	
	@Override
	public void caseAPublicModifier(APublicModifier node) {
		String result = "public static";
		this.result.append(result);
	}
	
	@Override
	public void caseAGlobalModifier(AGlobalModifier node) {
		String result = "public static";
		this.result.append(result);
	}
	
	@Override
	public void caseAConstModifier(AConstModifier node) {
		String result = "final";
		this.result.append(result);
	}
	
////////////////////logische Operatoren////////////////////

	@Override
	public void caseANotLogicalOperator(ANotLogicalOperator node) {
		String result = "!";
		this.result.append(result);
	}
	
	@Override
	public void caseAEqualLogicalOperator(AEqualLogicalOperator node) {
		String result = "==";
		this.result.append(result);
	}
	
	@Override
	public void caseANotEqualLogicalOperator(ANotEqualLogicalOperator node) {
		String result = "!=";
		this.result.append(result);
	}
	
	@Override
	public void caseALessThanLogicalOperator(ALessThanLogicalOperator node) {
		String result = "<";
		this.result.append(result);
	}
	
	@Override
	public void caseAGreaterThanLogicalOperator(AGreaterThanLogicalOperator node) {
		String result = ">";
		this.result.append(result);
	}
	
	@Override
	public void caseALessThanOrEqualLogicalOperator(ALessThanOrEqualLogicalOperator node) {
		String result = "<=";
		this.result.append(result);
	}
	
	@Override
	public void caseAGreaterThanOrEqualLogicalOperator(AGreaterThanOrEqualLogicalOperator node) {
		String result = ">=";
		this.result.append(result);
	}
	
////////////////////arithmetische Operatoren////////////////////
	@Override
	public void caseAAdditionArithmeticOperator(AAdditionArithmeticOperator node) {
		String result = "+";
		this.result.append(result);
	}
	
	@Override
	public void caseASubtractionArithmeticOperator(ASubtractionArithmeticOperator node) {
		String result = "-";
		this.result.append(result);
	}
	
	@Override
	public void caseAMultiplicationArithmeticOperator(AMultiplicationArithmeticOperator node) {
		String result = "*";
		this.result.append(result);
	}
	
	@Override
	public void caseADivisionArithmeticOperator(ADivisionArithmeticOperator node) {
		String result = "/";
		this.result.append(result);
	}
	
	@Override
	public void caseAModuloArithmeticOperator(AModuloArithmeticOperator node) {
		String result = "%";
		this.result.append(result);
	}
	
////////////////////Variablen Deklaration und Initialisierung////////////////////
	
	@Override
	public void caseAAttributeDecIn(AAttributeDecIn node) {
		String result = "";
		this.result.append(result);
	}
	
	@Override
	public void caseAVarDecIn(AVarDecIn node) {
		String result = "";
		
		for (PModifier modifier : ((AModifierId)node.getModifierId()).getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		result += getDataTypeOfValue(node.getValue()) + " ";
		
		Visitor idVisitor = new Visitor(className);
		((AModifierId)node.getModifierId()).getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		result += " = ";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult();
		
		this.result.append(result);
		
		// VisitorDataObjekt updaten
			// VariablesDatatType
		visitorDataObject.addVariablesDataType(idVisitor.getResult().toString(), getDataTypeOfValue(node.getValue()));
		// VisitorDataObjekt updaten ENDE
	}
	@Override
	public void caseAVarDecInWithDataType(AVarDecInWithDataType node) {
		String result = "";
		
		for (PModifier modifier : ((AModifierId)node.getModifierId()).getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		Visitor dataTypeVisitor = new Visitor(className);
		node.getDataType().apply(dataTypeVisitor);
		result += dataTypeVisitor.getResult() + " ";
		
		Visitor idVisitor = new Visitor(className);
		((AModifierId)node.getModifierId()).getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		result += " = ";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult();
		
		this.result.append(result);
		
		// VisitorDataObjekt updaten
			// VariablesDatatType
		visitorDataObject.addVariablesDataType(idVisitor.getResult().toString(), dataTypeVisitor.getResult().toString());
		// VisitorDataObjekt updaten ENDE
	}
	
	@Override
	public void caseAEnumDecIn(AEnumDecIn node) {
		nextEnumValue = 0;

		String result = "";
		
		
		if (node.getModifier().isEmpty()) {
			result += "public class ";
		} else {
			for (PModifier modifier : node.getModifier()) {
				Visitor modifierVisitor = new Visitor(className);
				modifier.apply(modifierVisitor);
				result += modifierVisitor.getResult() + " ";
			}
			
			result += "class ";
		}
		
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		result += " {\n";
		
		for (PEnumValue enumValue : node.getEnumValue()) {
			Visitor enumValueVisitor = new Visitor(className);
			enumValue.apply(enumValueVisitor);
			result += enumValueVisitor.getResult();
			
			// VisitorDataObjekt updaten
			if (enumValue instanceof AVarSetEnumValue) {
				Visitor idEnumValueVisitor = new Visitor(className);
				((AVarSet)((AVarSetEnumValue)enumValue).getVarSet()).getMethodChaining().apply(idEnumValueVisitor);
				
				// VariablesDataType
				visitorDataObject.addVariablesDataType(idEnumValueVisitor.getResult().toString(), "int");
				
				// VariablesCall
				visitorDataObject.addGlobalVariablesCall(idEnumValueVisitor.getResult().toString(), className + "." + idVisitor.getResult().toString() + "." + idEnumValueVisitor.getResult().toString());
			} else if (enumValue instanceof AWithoutValueEnumValue) {
				Visitor idEnumValueVisitor = new Visitor(className);
				((AWithoutValueEnumValue) enumValue).getId().apply(idEnumValueVisitor);
				
				// VariablesDataType
				visitorDataObject.addVariablesDataType(idEnumValueVisitor.getResult().toString(), idVisitor.getResult().toString());
				
				// VariablesCall
				visitorDataObject.addGlobalVariablesCall(idEnumValueVisitor.getResult().toString(), "int");
			}
			// VisitorDataObjekt updaten ENDE
		}
		
		// Enum schließen
		result += "}";
		
		this.result.append(result);
	}
	@Override
	public void caseAVarSetEnumValue(AVarSetEnumValue node) {
		String result = "";
		
		result += "public static final int ";

		Visitor idVisitor = new Visitor(className);
		((AVarSet) node.getVarSet()).getMethodChaining().apply(idVisitor);
		result += idVisitor.getResult().toString() + " = ";
		
		Visitor valueVisitor = new Visitor(className);
		((AVarSet)node.getVarSet()).getValue().apply(valueVisitor);
		
		// überflüssige Klammern entfernen
		String valueVisitorResult = valueVisitor.getResult().toString().replaceFirst("\\(", "");
		valueVisitorResult = valueVisitorResult.replaceAll("\\)$", "");
		result += valueVisitorResult;
		
		result += ";";
		
		Visitor genStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(genStmtVisitor);
		result += genStmtVisitor.getResult();
		
		this.result.append(result);
	}
	@Override
	public void caseAWithoutValueEnumValue(AWithoutValueEnumValue node) {
		String result = "";
		
		result += "public static final int ";

		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString() + " = ";
		result += Math.pow(2, nextEnumValue++);
		
		result += ";";
		
		Visitor genStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(genStmtVisitor);
		result += genStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseATypeDec(ATypeDec node) {
		String result = "";
		
		// Klassen-Kopf
		for (PModifier modifier : node.getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		result += "class "; 
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString() + " {\n";
		
		
		// Datenfelder deklarieren
		LinkedList<AVarDec> varDecList = new LinkedList<AVarDec>();
		LinkedList<PArrayDec> arrayDecList = new LinkedList<PArrayDec>();
		for (PTypeDecValue typeDecValue : node.getTypeDecValue()) {
			if (typeDecValue instanceof AVarDecTypeDecValue) {
				varDecList.add((AVarDec)((AVarDecTypeDecValue)typeDecValue).getVarDec());
			
				// alles außer Array
				result += "public ";

				Visitor varDecVisitor = new Visitor(className);
				((AVarDecTypeDecValue) typeDecValue).getVarDec().apply(varDecVisitor);
				result += varDecVisitor.getResult();
				
				result += ";";
				
				Visitor generalStmtVisitor = new Visitor(className);
				((AVarDecTypeDecValue) typeDecValue).getGeneralStmt().apply(generalStmtVisitor);
				result += generalStmtVisitor.getResult();

			} else if (typeDecValue instanceof AArrayDecTypeDecValue) {
				arrayDecList.add(((AArrayDecTypeDecValue)typeDecValue).getArrayDec());
				
				// Array
				result += "public ";
				
				Visitor varDecVisitor = new Visitor(className);
				((AArrayDecTypeDecValue)typeDecValue).getArrayDec().apply(varDecVisitor);
				result += varDecVisitor.getResult();
				
				result += ";";
				
				Visitor generalStmtVisitor = new Visitor(className);
				((AArrayDecTypeDecValue) typeDecValue).getGeneralStmt().apply(generalStmtVisitor);
				result += generalStmtVisitor.getResult();
			} else if (typeDecValue instanceof AGeneralStmtTypeDecValue) {
				Visitor generalStmtVisitor = new Visitor(className);
				((AGeneralStmtTypeDecValue) typeDecValue).getGeneralStmt().apply(generalStmtVisitor);
				result += generalStmtVisitor.getResult();
			}
		}
		
		result += "\n";
		
		// Konstruktor definieren
		result += "public ";
		
		result += idVisitor.getResult().toString();
		
		result += "(";
		for (AVarDec varDec : varDecList) {
			Visitor dataTypeVisitor = new Visitor(className);
			varDec.getDataType().apply(dataTypeVisitor);
			
			result += dataTypeVisitor.getResult() + " " + ((AId)((AModifierId)varDec.getModifierId()).getId()).getIdToken().getText() + ", ";
		}
		for (PArrayDec arrayDec : arrayDecList) {
			if (arrayDec instanceof AArrayDec) {
				Visitor dataTypeVisitor = new Visitor(className);
				((AArrayDec) arrayDec).getDataType().apply(dataTypeVisitor);
				
				result += dataTypeVisitor.getResult() + "[] " + ((AId)((AModifierId)((AArrayDec)arrayDec).getModifierId()).getId()).getIdToken().getText() + ", ";
			} else if (arrayDec instanceof ARangeArrayDec) {
				Visitor dataTypeVisitor = new Visitor(className);
				((ARangeArrayDec) arrayDec).getDataType().apply(dataTypeVisitor);
				
				result += dataTypeVisitor.getResult() + "[] " + ((AId)((AModifierId)((ARangeArrayDec) arrayDec).getModifierId()).getId()).getIdToken().getText() + ", ";
			}
		}
		
		// das letzte ", " wieder entfernen
		result = result.substring(0, result.length() - 2);
		
		result += ") {\n";
		
		for (AVarDec varDec : varDecList) {
			result += "this.";
			result += ((AId)((AModifierId)varDec.getModifierId()).getId()).getIdToken().getText() + " = " + ((AId)((AModifierId)varDec.getModifierId()).getId()).getIdToken().getText() + ";\n";
		}
		for (PArrayDec arrayDec : arrayDecList) {
			result += "this.";
			if (arrayDec instanceof AArrayDec) {
				result += ((AId)((AModifierId)((AArrayDec) arrayDec).getModifierId()).getId()).getIdToken().getText() + " = " + ((AId)((AModifierId)((AArrayDec) arrayDec).getModifierId()).getId()).getIdToken().getText() + ";\n";
			} else if (arrayDec instanceof ARangeArrayDec) {
				result += ((AId)((AModifierId)((ARangeArrayDec) arrayDec).getModifierId()).getId()).getIdToken().getText() + " = " + ((AId)((AModifierId)((ARangeArrayDec) arrayDec).getModifierId()).getId()).getIdToken().getText() + ";\n";
			}
		}

		result += "}\n"; // Konstruktor schließen

		result += "}"; // Klasse schließen
		
		this.result.append(result);
	}
	@Override
	public void caseAVarDecTypeDecValue(AVarDecTypeDecValue node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getVarDec().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	@Override
	public void caseAArrayDecTypeDecValue(AArrayDecTypeDecValue node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getArrayDec().apply(visitor);
		result += visitor.getResult();

		result += ";";
		
		Visitor generalStmtVisitor = new Visitor(className);
		node.getGeneralStmt().apply(generalStmtVisitor);
		result += generalStmtVisitor.getResult();
		
		this.result.append(result);
	}
	
////////////////////Variablen Deklaration////////////////////
	
	@Override
	public void caseAVarDec(AVarDec node) {
		String result = "";
		
		for (PModifier modifier : ((AModifierId)node.getModifierId()).getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		Visitor dataTypeVisitor = new Visitor(className);
		node.getDataType().apply(dataTypeVisitor);
		result += dataTypeVisitor.getResult() + " ";
		
		
		Visitor idVisitor = new Visitor(className);
		((AModifierId)node.getModifierId()).getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		this.result.append(result);
		
		// VisitorDataObjekt updaten
			// VariablesDatatType
		visitorDataObject.addVariablesDataType(idVisitor.getResult().toString(), dataTypeVisitor.getResult().toString());
		// VisitorDataObjekt updaten ENDE
	}
	
	@Override
	public void caseAVarDecList(AVarDecList node) {
		String result = "";

		LinkedList<PVarDec> varDecList = new LinkedList<PVarDec>();
		
		varDecList.add(((AVarDecListValue)node.getFirst()).getVarDec());
		for (PVarDecListValue varDecListValue : node.getVarDecListValue()) {
			varDecList.add(((AVarDecListValue)varDecListValue).getVarDec());
		}
		varDecList.add(node.getVarDec());

		for (PVarDec varDec : varDecList) {
			Visitor dataTypeVisitor = new Visitor(className);
			varDec.apply(dataTypeVisitor);
			result += dataTypeVisitor.getResult() + ";\n";
			
			// VisitorDataObjekt updaten
				// VariablesDatatType
			visitorDataObject.addVariablesDataType(((AId)((AModifierId)((AVarDec)varDec).getModifierId()).getId()).getIdToken().getText(), dataTypeVisitor.getResult().toString());
			// VisitorDataObjekt updaten ENDE			
		}
		
		// das letzte ";\n" wieder entfernen
		result = result.substring(0, result.length() - 2);
		
		this.result.append(result);
	}
	
	@Override
	public void caseAArrayDec(AArrayDec node) {
		String result = "";

		for (PModifier modifier : ((AModifierId)node.getModifierId()).getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		Visitor dataTypeVisitor = new Visitor(className);
		node.getDataType().apply(dataTypeVisitor);
		result += dataTypeVisitor.getResult() + "[] ";

		Visitor idVisitor = new Visitor(className);
		((AModifierId)node.getModifierId()).getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		if (node.getParamCallList() instanceof AParamCallList) {
			result += " = new ";
			
			result += dataTypeVisitor.getResult() + "[";
			
			Visitor sizeVisitor = new Visitor(className);
			((AParamCallList)node.getParamCallList()).getParamCallValue().apply(sizeVisitor);
			result += sizeVisitor.getResult().toString();
			
			result += "]";
		}
		
		this.result.append(result);

		// VisitorDataObjekt updaten
			// VariablesDatatType
		visitorDataObject.addVariablesDataType(idVisitor.getResult().toString(), dataTypeVisitor.getResult().toString());
		
			// ArrayName
		visitorDataObject.addArrayName(idVisitor.getResult().toString());
		// VisitorDataObjekt updaten ENDE
	}
	@Override
	public void caseARangeArrayDec(ARangeArrayDec node) {
		String result = "";

		for (PModifier modifier : ((AModifierId)node.getModifierId()).getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		Visitor dataTypeVisitor = new Visitor(className);
		node.getDataType().apply(dataTypeVisitor);
		result += dataTypeVisitor.getResult() + "[] ";
		
		Visitor idVisitor = new Visitor(className);
		((AModifierId)node.getModifierId()).getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		result += " = new ";
		
		result += dataTypeVisitor.getResult() + "[";

		Visitor valueVisitor = new Visitor(className);
		node.getLargest().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += "]";
		
		this.result.append(result);

		// VaraiablesDataType
			// ArrayName
		visitorDataObject.addArrayName(idVisitor.getResult().toString());
		// VisitorDataObjekt updaten ENDE
	}
	

////////////////////den Wert der Variable neu setzen////////////////////
	
	@Override
	public void caseAVarSet(AVarSet node) {
		String result = "";
		
		Visitor methodChainingVisitor = new Visitor(className);
		node.getMethodChaining().apply(methodChainingVisitor);
		
		// falls es sich bei der ID um einen Methoden-Name handelt, dann handelt es sich um das Setzen des Rückgabewertes
		if (visitorDataObject.getMethodsDataType(methodChainingVisitor.getResult().toString()) != null || visitorDataObject.getPrivateMethodsDataType(methodChainingVisitor.getResult().toString()) != null) {
			result += "returnValue = ";
		} else { // ansonsten ist es eine normale Wertzuweisung
			result += methodChainingVisitor.getResult().toString() + " = ";
		}
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult();
		
		
		if (visitorDataObject.getMethodsDataType(methodChainingVisitor.getResult().toString()) != null || visitorDataObject.getPrivateMethodsDataType(methodChainingVisitor.getResult().toString()) != null) {
			if (visitorDataObject.getVariablesDataType("returnValue") != null && visitorDataObject.getVariablesDataType("returnValue").toLowerCase().equals("boolean")  && node.getValue() instanceof AMethodValue && ((AMethodValue)node.getValue()).getMethodCall() instanceof AInStrMethodCall) {
				result += " != 0";
			}
		} else { // ansonsten ist es eine normale Wertzuweisung
			if (visitorDataObject.getVariablesDataType(methodChainingVisitor.getResult().toString()) != null && visitorDataObject.getVariablesDataType(methodChainingVisitor.getResult().toString()).toLowerCase().equals("boolean")  && node.getValue() instanceof AMethodValue && ((AMethodValue)node.getValue()).getMethodCall() instanceof AInStrMethodCall) {
				result += " != 0";
			}
		}
		
		this.result.append(result);
	}
	@Override
	public void caseATrimVarSet(ATrimVarSet node) {
		String result = "";
		
		Visitor trimFunctionVisitor = new Visitor(className);
		node.getTrimFunction().apply(trimFunctionVisitor);
		result += trimFunctionVisitor.getResult().toString() + " = ";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult();
		
		this.result.append(result);
	}
	
////////////////////mit einem Array interagieren////////////////////

	@Override
	public void caseAArrayResize(AArrayResize node) {
		String result = "";
		
		Visitor idVisitor = new Visitor(className);
		node.getMethodChaining().apply(idVisitor);
		result += idVisitor.getResult().toString() + " = ";
		
		result += " new " + visitorDataObject.getVariablesDataType(idVisitor.getResult().toString()) + "[";
		
		Visitor valueVisitor = new Visitor(className);
		node.getLargest().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += "]";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAArrayResizePreserveData(AArrayResizePreserveData node) {
		String result = "";
		
		Visitor idVisitor = new Visitor(className);
		node.getMethodChaining().apply(idVisitor);

		// die Daten behalten
		result += visitorDataObject.getVariablesDataType(idVisitor.getResult().toString()) + "[] ";
		result += idVisitor.getResult().toString().substring((idVisitor.getResult().toString().lastIndexOf('.') == -1 ? 0 : idVisitor.getResult().toString().lastIndexOf('.') + 1), idVisitor.getResult().toString().length()) +
				  "Backup = " + idVisitor.getResult().toString() + ";\n";
		
		// Array mit neuer Größe deklarieren
		result += idVisitor.getResult().toString() + " = ";
		
		result += " new " + visitorDataObject.getVariablesDataType(idVisitor.getResult().toString()) + "[";
		
		Visitor valueVisitor = new Visitor(className);
		node.getLargest().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += "];\n";
		
		// alte Daten in das neue Array übernehmen
		result += "//alte Daten in das neue Array übernehmen\n";
		result += "int indexCounter = 0;\n";
		result += "for (" + visitorDataObject.getVariablesDataType(idVisitor.getResult().toString()) + " value : " +
				  idVisitor.getResult().toString().substring((idVisitor.getResult().toString().lastIndexOf('.') == -1 ? 0 : idVisitor.getResult().toString().lastIndexOf('.') + 1), idVisitor.getResult().toString().length()) +
				  "Backup" + ") {\n";
		result += idVisitor.getResult().toString() + "[indexCounter] = value;\n";
		result += "indexCounter++;\n";
		result += "}";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAArraySetElement(AArraySetElement node) {
		String result = "";
		
		
		Visitor idVisitor = new Visitor(className);
		node.getMethodChaining().apply(idVisitor);
		result += idVisitor.getResult().toString() + "[";
		
		Visitor paramCallVisitor = new Visitor(className);
		node.getParamCallList().apply(paramCallVisitor);
		result += paramCallVisitor.getResult();
		// "(" und ")" entfernen
		result = result.replace("\\(", "");
		result = result.replace("\\)", "");
		
		
		result += "] = ";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult();
		
		this.result.append(result);
	}
	
////////////////////Methoden////////////////////
	
	private String methodHelper(LinkedList<PModifier> modifierList, String methodName, PParamList paramList, String returnType, LinkedList<PFunctionStmt> stmts) {
		String result = "";
		
		String modifierString = "";
		
		if (modifierList.isEmpty()) {
			result += "public static ";
		} else {
			for (PModifier modifier : modifierList) {
				Visitor modifierVisitor = new Visitor(className);
				modifier.apply(modifierVisitor);
				result += modifierVisitor.getResult() + " ";
				modifierString += modifierVisitor.getResult() + " ";
			}
		}
		
		result += returnType + " " + methodName;
		
		Visitor paramListVisitor = new Visitor(className);
		paramList.apply(paramListVisitor);
		result += paramListVisitor.getResult();
		
		result += " {\n";
		
		// wenn es Parameter gibt ggf. optionale parameter einrichten
		if (paramList instanceof AParamList) {
			// Optionale Parameter einrichten
			LinkedList<POptionalParamValue> optionalParamList = new LinkedList<POptionalParamValue>();
			for (PParamPart paramPart : ((AParamList)paramList).getParamPart()) {
				if (((AParamPart)paramPart).getParamValue() instanceof AOptionalParamValueParamValue) {
					optionalParamList.add(((AOptionalParamValueParamValue)((AParamPart)paramPart).getParamValue()).getOptionalParamValue());
				}
			}
			if (((AParamList)paramList).getParamValue() instanceof AOptionalParamValueParamValue) {
				optionalParamList.add(((AOptionalParamValueParamValue)((AParamList)paramList).getParamValue()).getOptionalParamValue());
			}
			
			for (POptionalParamValue optionalParam : optionalParamList) {
				// Standartwerte der Parameter setzen
				if (optionalParam instanceof AOptionalParamOptionalParamValue) {
					Visitor idVisitor = new Visitor(className);
					((AOptionalParamOptionalParamValue)optionalParam).getId().apply(idVisitor);
					
					Visitor dataTypeVisitor = new Visitor(className);
					((AOptionalParamOptionalParamValue)optionalParam).getDataType().apply(dataTypeVisitor);
					
					
					result += dataTypeVisitor.getResult().toString() + " " + idVisitor.getResult().toString() + "; \n";
					result += "if (" + idVisitor.getResult().toString() + "W == null) {\n";
					result += idVisitor.getResult().toString() + " = ";
					
					
					Visitor valueVisitor = new Visitor(className);
					((AOptionalParamOptionalParamValue)optionalParam).getValue().apply(valueVisitor);
					result += valueVisitor.getResult();
					
					
					result += ";\n";
					result += "} else {\n";
					result += idVisitor.getResult().toString() + " = " + idVisitor.getResult().toString() +"W;";
					result += "\n}\n\n";
				} else if (optionalParam instanceof AOptionalParamAsBooleanOptionalParamValue) {
					Visitor idVisitor = new Visitor(className);
					((AOptionalParamAsBooleanOptionalParamValue)optionalParam).getId().apply(idVisitor);
					
					result += "boolean " + idVisitor.getResult().toString() + "; \n";
					result += "if (" + idVisitor.getResult().toString() + "W == null) {\n";
					result += idVisitor.getResult().toString() + " = ";
					
					result += "false"; //Standart Boolean-Wert
					
					result += ";\n";
					result += "} else {\n";
					result += idVisitor.getResult().toString() + " = " + idVisitor.getResult().toString() +"W;";
					result += "\n}\n\n";
				} else if (optionalParam instanceof AOptionalParamAsIntegerOptionalParamValue) {
					Visitor idVisitor = new Visitor(className);
					((AOptionalParamAsIntegerOptionalParamValue)optionalParam).getId().apply(idVisitor);
					
					result += "int " + idVisitor.getResult().toString() + "; \n";
					result += "if (" + idVisitor.getResult().toString() + "W == null) {\n";
					result += idVisitor.getResult().toString() + " = ";
					
					result += "0"; //Standart Integer-Wert
					
					result += ";\n";
					result += "} else {\n";
					result += idVisitor.getResult().toString() + " = " + idVisitor.getResult().toString() +"W;";
					result += "\n}\n\n";
				} else if (optionalParam instanceof AOptionalParamAsStringOptionalParamValue) {
					Visitor idVisitor = new Visitor(className);
					((AOptionalParamAsStringOptionalParamValue)optionalParam).getId().apply(idVisitor);
					
					result += "String " + idVisitor.getResult().toString() + "; \n";
					result += "if (" + idVisitor.getResult().toString() + "W == null) {\n";
					result += idVisitor.getResult().toString() + " = ";
					
					result += "null"; //Standart Integer-Wert
					
					result += ";\n";
					result += "} else {\n";
					result += idVisitor.getResult().toString() + " = " + idVisitor.getResult().toString() +"W;";
					result += "\n}\n\n";
				} else if (optionalParam instanceof AOptionalParamAsTypeOptionalParamValue) {
					Visitor idVisitor = new Visitor(className);
					((AOptionalParamAsTypeOptionalParamValue)optionalParam).getId().apply(idVisitor);
					
					Visitor dataTypeVisitor = new Visitor(className);
					((AOptionalParamAsTypeOptionalParamValue)optionalParam).getDataType().apply(dataTypeVisitor);
					
					
					result += dataTypeVisitor.getResult().toString() + " " + idVisitor.getResult().toString() + "; \n";
					result += "if (" + idVisitor.getResult().toString() + "W == null) {\n";
					result += idVisitor.getResult().toString() + " = ";
					
					result += "null"; //Standart Opjekttyp-Wert
					
					result += ";\n";
					result += "} else {\n";
					result += idVisitor.getResult().toString() + " = " + idVisitor.getResult().toString() +"W;";
					result += "\n}\n\n";
				} else if (optionalParam instanceof AOptionalParamNoDataTypeOptionalParamValue) {
					Visitor idVisitor = new Visitor(className);
					((AOptionalParamNoDataTypeOptionalParamValue)optionalParam).getId().apply(idVisitor);
					
					result += getDataTypeOfValue(((AOptionalParamNoDataTypeOptionalParamValue)optionalParam).getValue()) + " " + idVisitor.getResult().toString() + "; \n";
					result += "if (" + idVisitor.getResult().toString() + "W == null) {\n";
					result += idVisitor.getResult().toString() + " = ";
					
					
					Visitor valueVisitor = new Visitor(className);
					((AOptionalParamNoDataTypeOptionalParamValue)optionalParam).getValue().apply(valueVisitor);
					result += valueVisitor.getResult();
					
					
					result += ";\n";
					result += "} else {\n";
					result += idVisitor.getResult().toString() + " = " + idVisitor.getResult().toString() +"W;";
					result += "\n}\n\n";
				}
				
			}
		}
		
		// Wenn es sich um eine Methode mit Rückgabewert handelt, dann eine Variable die den Rückgabewert immer speichert anlegen.
		if (returnType != null && !returnType.equals("void")) {
			result += returnType + " returnValue;\n";
			
			// VisitorDataObjekt updaten
				// VariablesDatatType
			visitorDataObject.addVariablesDataType("returnValue", returnType);
			// VisitorDataObjekt updaten ENDE
		}
		
		// Methoden-Befehle ausführen
		for (PFunctionStmt functionStmt : stmts) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult();
		}
		
		
		// Wenn es sich um eine Methode mit Rückgabewert handelt, dann den Rückgabewert noch zurückgeben.
		if (returnType != null && !returnType.equals("void")) {
			result += "return returnValue;\n";
		}
		
		result += "}";
		
		
		// VisitorDataObjekt updaten
		if (!modifierString.contains("private")) {
			// MethodsDataType
			visitorDataObject.addMethodsDataType(methodName, returnType);
			// MethodParameters
			if (paramList instanceof ANoParamParamList) {
				visitorDataObject.addMethodsParameters(methodName, 0);
			} else if (paramList instanceof AParamList) {
				visitorDataObject.addMethodsParameters(methodName, ((AParamList)paramList).getParamPart().size() + 1);
			}
			// MethodCall
			visitorDataObject.addMethodsCall(methodName, className + "." + methodName);
		} else {
			// MethodParameters
			if (paramList instanceof ANoParamParamList) {
				visitorDataObject.addPrivateMethodsParameters(methodName, 0);
			} else if (paramList instanceof AParamList) {
				visitorDataObject.addPrivateMethodsParameters(methodName, ((AParamList)paramList).getParamPart().size() + 1);
			}
			// MethodsDataType
			visitorDataObject.addPrivateMethodsDataType(methodName, returnType);
		}
		// VisitorDataObjekt updaten ENDE
		
		return result;
	}
	
	// Methoden ohne Rückgabewert
	@Override
	public void caseASub(ASub node) {
		methodDec = true;
		
		String result = "";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		
		result += methodHelper(node.getModifier(), idVisitor.getResult().toString(), node.getParamList(), "void", node.getFunctionStmt());
		
		this.result.append(result);
	}

	
	//Methoden mit Rückgabewert
	@Override
	public void caseAFunction(AFunction node) {
		methodDec = true;
		
		String result = "";

		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);

		result += methodHelper(node.getModifier(), idVisitor.getResult().toString(), node.getParamList(), getWrapperDataTypeOfDataType(node.getDataType()), node.getFunctionStmt());
		
		this.result.append(result);
	}
	
	@Override
	public void caseAFunctionWithoutReturnType(AFunctionWithoutReturnType node) {
		
		methodDec = true;
		
		String result = "";

		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);

		result += methodHelper(node.getModifier(), idVisitor.getResult().toString(), node.getParamList(), "String", node.getFunctionStmt());
		
		this.result.append(result);
	}
	
	
	//Parameter
	@Override
	public void caseAParamList(AParamList node) {
		String result = "";

		result += "("; 
		
		for (PParamPart param : node.getParamPart()) {
			
			Visitor paramVisitor = new Visitor(className);
			param.apply(paramVisitor);
			result += paramVisitor.getResult() + ", ";
		}
		Visitor paramVisitor = new Visitor(className);
		node.getParamValue().apply(paramVisitor);
		result += paramVisitor.getResult().toString();
		
		result += ")"; 

		this.result.append(result);
	}
	
	@Override
	public void caseANoParamParamList(ANoParamParamList node) {
		String result = "()";
		this.result.append(result);
	}
	
	
	@Override
	public void caseAOptionalParamOptionalParamValue(AOptionalParamOptionalParamValue node) {
		String result = "";
		
		for (PModifier modifier : node.getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		result += getWrapperDataTypeOfDataType(node.getDataType()) + " ";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString() + "W";
		
		this.result.append(result);
	}
	@Override
	public void caseAOptionalParamAsBooleanOptionalParamValue(AOptionalParamAsBooleanOptionalParamValue node) {
		String result = "";
		
		for (PModifier modifier : node.getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		result += "Boolean ";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString() + "W";
		
		this.result.append(result);
	}
	@Override
	public void caseAOptionalParamAsIntegerOptionalParamValue(AOptionalParamAsIntegerOptionalParamValue node) {
		String result = "";
		
		for (PModifier modifier : node.getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		result += "Integer ";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString() + "W";
		
		this.result.append(result);
	}
	@Override
	public void caseAOptionalParamAsStringOptionalParamValue(AOptionalParamAsStringOptionalParamValue node) {
		String result = "";
		
		for (PModifier modifier : node.getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		result += "String ";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString() + "W";
		
		this.result.append(result);
	}
	@Override
	public void caseAParamWithoutDataTypeParamValue(AParamWithoutDataTypeParamValue node) {
		String result = "";
		
		for (PModifier modifier : node.getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		result += "nichtAngegebenInVba ";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		this.result.append(result);	
	}
	@Override
	public void caseAOptionalParamAsTypeOptionalParamValue(AOptionalParamAsTypeOptionalParamValue node) {
		String result = "";
		
		for (PModifier modifier : node.getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		Visitor dataTypeVisitor = new Visitor(className);
		node.getDataType().apply(dataTypeVisitor);
		result += dataTypeVisitor.getResult().toString() + " ";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString() + "W";
		
		this.result.append(result);	
	}
	@Override
	public void caseAOptionalParamNoDataTypeOptionalParamValue(AOptionalParamNoDataTypeOptionalParamValue node) {
		String result = "";
		
		for (PModifier modifier : node.getModifier()) {
			Visitor modifierVisitor = new Visitor(className);
			modifier.apply(modifierVisitor);
			result += modifierVisitor.getResult() + " ";
		}
		
		result += getDataTypeOfValue(node.getValue()) + " ";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		result += " = ";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAMethodChainingInParensBooleanValue(AMethodChainingInParensBooleanValue node) {
		String result = "";
		
		result = "(";
		
		Visitor methodChainingVisitor = new Visitor(className);
		node.getMethodChaining().apply(methodChainingVisitor);
		result += methodChainingVisitor.getResult().toString() + " ";
		
		result = ")";
		
		this.result.append(result);	
	}
	
////////////////////Methoden aufrufen////////////////////
	
	@Override
	public void caseAMethodCall(AMethodCall node) {
		String result = "";
		
		Visitor idVisitor = new Visitor(className);
		node.getId().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		Visitor paramCallListVisitor = new Visitor(className);
		node.getParamCallList().apply(paramCallListVisitor);
		result += paramCallListVisitor.getResult().toString();
		
		// falls nicht genug Parameter angegeben mit "null" ergänzen
		int numberOfParams;
		if (paramCallListVisitor.getResult().toString().equals("()")) {
			numberOfParams = 0;
		} else {
			numberOfParams = ((AParamCallList)node.getParamCallList()).getParamCallPart().size() + 1;
		}
		
		if (visitorDataObject.getMethodsParameters(idVisitor.getResult().toString()) != -1) {
			if (numberOfParams < visitorDataObject.getMethodsParameters(idVisitor.getResult().toString()) && numberOfParams == 0) {
				result = result.substring(0, result.length() - 1);
				result += "null";
				numberOfParams++;
				for (int i = numberOfParams; i < visitorDataObject.getMethodsParameters(idVisitor.getResult().toString()); i++) {
					result += ", null";
				}
				result += ")";
			} else if (numberOfParams < visitorDataObject.getMethodsParameters(idVisitor.getResult().toString())) {
				result = result.substring(0, result.length() - 1);
				for (int i = numberOfParams; i < visitorDataObject.getMethodsParameters(idVisitor.getResult().toString()); i++) {
					result += ", null";
				}
				result += ")";
			}	
		} else if (visitorDataObject.getPrivateMethodsParameters(idVisitor.getResult().toString()) != -1) {
			if (numberOfParams < visitorDataObject.getPrivateMethodsParameters(idVisitor.getResult().toString()) && numberOfParams == 0) {
				result = result.substring(0, result.length() - 1);
				result += "null";
				numberOfParams++;
				for (int i = numberOfParams; i < visitorDataObject.getPrivateMethodsParameters(idVisitor.getResult().toString()); i++) {
					result += ", null";
				}
				result += ")";
			} else if (numberOfParams < visitorDataObject.getPrivateMethodsParameters(idVisitor.getResult().toString())) {
				result = result.substring(0, result.length() - 1);
				for (int i = numberOfParams; i < visitorDataObject.getPrivateMethodsParameters(idVisitor.getResult().toString()); i++) {
					result += ", null";
				}
				result += ")";
			}
		}
		
		// wenn es sich gar nicht um einen Methodenaufruf, sondern um das Auslesen eines Arrays handelt, die Klammern ändern
		if (visitorDataObject.isArray(idVisitor.getResult().toString())) {
			result = result.replaceFirst("\\(", "[");
			result = result.substring(0, result.length() - 1);
			result += "]";
		}
		
		this.result.append(result);
	}
	@Override
	public void caseAReplaceMethodCall(AReplaceMethodCall node) {
		String result = "";
		
		result += "M00_Helper.replace";
		
		Visitor paramCallListVisitor = new Visitor(className);
		node.getParamCallList().apply(paramCallListVisitor);
		result += paramCallListVisitor.getResult().toString();
		
		this.result.append(result);
	}
	@Override
	public void caseAInStrMethodCall(AInStrMethodCall node) {
		String result = "";
		
		result += "M00_Helper.inStr";
		
		Visitor paramCallListVisitor = new Visitor(className);
		node.getParamCallList().apply(paramCallListVisitor);
		result += paramCallListVisitor.getResult().toString();
		
		this.result.append(result);
	}
	@Override
	public void caseAMethodCallInParens(AMethodCallInParens node) {
		String result = "";

		result += "(";
		
		Visitor methodCallVisitor = new Visitor(className);
		node.getMethodCall().apply(methodCallVisitor);
		result += methodCallVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	@Override
	public void caseAMethodCallWithoutParens(AMethodCallWithoutParens node) {
		String result = "";
		
		Visitor idVisitor = new Visitor(className);
		node.getMethodChaining().apply(idVisitor);
		result += idVisitor.getResult().toString();

		Visitor paramCallListVisitor = new Visitor(className);
		node.getParamCallListWithoutParens().apply(paramCallListVisitor);
		result += paramCallListVisitor.getResult().toString();
		
		this.result.append(result);
	}
	
	@Override
	public void caseANoParamMethodCallWithoutParens(ANoParamMethodCallWithoutParens node) {
		String result = "";
		
		Visitor idVisitor = new Visitor(className);
		node.getMethodChaining().apply(idVisitor);
		result += idVisitor.getResult().toString();
		
		this.result.append(result);
	}
	
	// Parameter mit Klammern
	@Override
	public void caseAParamCallList(AParamCallList node) {
		String result = "";

		result += "(";
		
		for (PParamCallPart param : node.getParamCallPart()) {
			Visitor paramCallVisitor = new Visitor(className);
			param.apply(paramCallVisitor);
			result += paramCallVisitor.getResult().toString() + ", ";
		}
		
		Visitor paramCallVisitor = new Visitor(className);
		node.getParamCallValue().apply(paramCallVisitor);
		result += paramCallVisitor.getResult().toString();
		
		result += ")";

		this.result.append(result);
	}
	@Override
	public void caseANoParamParamCallList(ANoParamParamCallList node) {
		String result = "()";
		this.result.append(result);
	}
	
	@Override
	public void caseACommaParamCallPart(ACommaParamCallPart node) {
		String result = "null";
		this.result.append(result);
	}
	
	// Parameter ohne Klammern
	@Override
	public void caseAParamCallListWithoutParens(AParamCallListWithoutParens node) {
		String result = "";

		result += "(";
		
		for (PParamCallListWithoutParensPart param : node.getParamCallListWithoutParensPart()) {
			Visitor paramCallVisitor = new Visitor(className);
			param.apply(paramCallVisitor);
			result += paramCallVisitor.getResult().toString() + ", ";
		}
		
		Visitor paramCallVisitor = new Visitor(className);
		node.getParamCallListWithoutParensValue().apply(paramCallVisitor);
		result += paramCallVisitor.getResult().toString();
		
		result += ")";

		this.result.append(result);
	}
	@Override
	public void caseACommaParamCallListWithoutParensPart(ACommaParamCallListWithoutParensPart node) {
		String result = "null";
		this.result.append(result);
	}
	
	@Override
	public void caseAParamWithParamName(AParamWithParamName node) {
		String result = "";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		this.result.append(result);
	}
	
////////////////////direkt/explizit behandelte Methoden////////////////////
	
	@Override
	public void caseADllFunction(ADllFunction node) {
	}
	@Override
	public void caseADllFunctionCall(ADllFunctionCall node) {
	}
	
	@Override
	public void caseAWaitSub(AWaitSub node) {
	}
	
	@Override
	public void caseAWaitSubCall(AWaitSubCall node) {
		String result = "";
		
		result = "Thread.sleep(";
		
		Visitor numberVisitor = new Visitor(className);
		node.getDuration().apply(numberVisitor);
		result += numberVisitor.getResult().toString();

		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseACStrFunction(ACStrFunction node) {
		String result = "";

		result += "String.valueOf(";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseACIntFunction(ACIntFunction node) {
		String result = "";

		result += "new Double(";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ").intValue()";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAUCaseFunction(AUCaseFunction node) {
		String result = "";

		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ".toUpperCase()";
		
		this.result.append(result);
	}
	
	@Override
	public void caseALenFunction(ALenFunction node) {
		String result = "";

		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ".length()";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAMidFunction(AMidFunction node) {
		String result = "";

		Visitor stringVisitor = new Visitor(className);
		node.getString().apply(stringVisitor);
		
		Visitor beginIndexVisitor = new Visitor(className);
		node.getBeginIndex().apply(beginIndexVisitor);
		
		Visitor lengthVisitor = new Visitor(className);
		node.getLength().apply(lengthVisitor);
		
		result += stringVisitor.getResult().toString() + ".substring(" + beginIndexVisitor.getResult().toString() +
				  " - 1, " + beginIndexVisitor.getResult().toString() + " + " + lengthVisitor.getResult().toString() + " - 1" + ")";
		
		this.result.append(result);
	}
	@Override
	public void caseAMidFunctionTwoParam(AMidFunctionTwoParam node) {
		String result = "";

		Visitor stringVisitor = new Visitor(className);
		node.getString().apply(stringVisitor);
		
		Visitor beginIndexVisitor = new Visitor(className);
		node.getBeginIndex().apply(beginIndexVisitor);
		
		result += stringVisitor.getResult().toString() + ".substring(" + beginIndexVisitor.getResult().toString() +
				  " - 1, " + beginIndexVisitor.getResult().toString() + " + " + stringVisitor.getResult().toString() + ".length() - 1" +
				  ")";
		
		this.result.append(result);
	}
	
	
	@Override
	public void caseALeftFunction(ALeftFunction node) {
		String result = "";

		Visitor stringVisitor = new Visitor(className);
		node.getString().apply(stringVisitor);
		
		Visitor lengthVisitor = new Visitor(className);
		node.getLength().apply(lengthVisitor);
		
		result += stringVisitor.getResult().toString() + ".substring(" + "0, " + lengthVisitor.getResult().toString() + ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseARightFunction(ARightFunction node) {
		String result = "";

		Visitor stringVisitor = new Visitor(className);
		node.getString().apply(stringVisitor);
		String stringVisitorValue = stringVisitor.getResult().toString();
		if (stringVisitorValue.contains("+")) {
			stringVisitorValue = "new String (" + stringVisitorValue + ")";
		}
		
		Visitor lengthVisitor = new Visitor(className);
		node.getLength().apply(lengthVisitor);
		
		result += stringVisitorValue + ".substring(" + stringVisitorValue + ".length() - 1 - " + lengthVisitor.getResult().toString() + ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseATrimFunction(ATrimFunction node) {
		String result = "";

		Visitor stringVisitor = new Visitor(className);
		node.getString().apply(stringVisitor);
		
		result += stringVisitor.getResult().toString() + ".trim()";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAUBoundFunction(AUBoundFunction node) {
		String result = "";

		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		
		result += "M00_Helper.uBound(" + valueVisitor.getResult().toString() + ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseALBoundFunction(ALBoundFunction node) {
		String result = "";

		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		
		result += "M00_Helper.lBound(" + valueVisitor.getResult().toString() + ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseACLngFunction(ACLngFunction node) {
		String result = "";

		result += "new Double(";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ").longValue()";
		
		this.result.append(result);
	}
	
	@Override
	public void caseACDblFunction(ACDblFunction node) {
		String result = "";

		result += "new Double(";
		
		Visitor valueVisitor = new Visitor(className);
		node.getStringValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseACSngFunction(ACSngFunction node) {
		String result = "";

		result += "new Double(";
		
		Visitor valueVisitor = new Visitor(className);
		node.getStringValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseARTrimFunction(ARTrimFunction node) {
		String result = "";

		Visitor valueVisitor = new Visitor(className);
		node.getStringValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ".replaceAll(\" +$\",\"\")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseALTrimFunction(ALTrimFunction node) {
		String result = "";

		Visitor valueVisitor = new Visitor(className);
		node.getStringValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ".replaceAll(\"^ +\",\"\")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseALCaseFunction(ALCaseFunction node) {
		String result = "";

		Visitor valueVisitor = new Visitor(className);
		node.getStringValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ".toLowerCase()";
		
		this.result.append(result);
	}
	
	@Override
	public void caseASplitFunction(ASplitFunction node) {
		String result = "";

		Visitor stringVisitor = new Visitor(className);
		node.getString().apply(stringVisitor);
		result += stringVisitor.getResult().toString();
		
		result += ".split(";

		Visitor delimiterVisitor = new Visitor(className);
		node.getDelimiter().apply(delimiterVisitor);
		result += delimiterVisitor.getResult().toString();

		result += ")";
		
		this.result.append(result);
	}
	@Override
	public void caseASplitFunctionWithLimit(ASplitFunctionWithLimit node) {
		String result = "";

		Visitor stringVisitor = new Visitor(className);
		node.getString().apply(stringVisitor);
		result += stringVisitor.getResult().toString();
		
		result += ".split(";

		Visitor delimiterVisitor = new Visitor(className);
		node.getDelimiter().apply(delimiterVisitor);
		result += delimiterVisitor.getResult().toString() + ", ";
		
		Visitor limitVisitor = new Visitor(className);
		node.getLimit().apply(limitVisitor);
		result += limitVisitor.getResult().toString();

		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAFormatFunction(AFormatFunction node) {
		String result = "";

		Visitor formatValueVisitor = new Visitor(className);
		node.getFormatValue().apply(formatValueVisitor);
		
		result += "new SimpleDateFormat(" + formatValueVisitor.getResult().toString() + ").format(new Date())";
		
		this.result.append(result);
	}
	
	@Override
	public void caseANowFunction(ANowFunction node) {
		String result = "";

		result += "new SimpleDateFormat(\"dd/MM/yy HH:mm:ss\").format(new Date())";
		
		this.result.append(result);
	}
	
	@Override
	public void caseASpaceFunction(ASpaceFunction node) {
		String result = "";

		result += "M00_Helper.space(" + node.getNumber().getText() + ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAChrFunction(AChrFunction node) {
		String result = "";

		result += "(char)" + node.getNumber().getText();
		
		this.result.append(result);
	}
	
////////////////////Verzweigungen////////////////////
	
	@Override
	public void caseAIfStmt(AIfStmt node) {
		String result = "";
		
		result += "if ("; 
	
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		if (result.startsWith("if (M00_Helper.inStr(")) {
			result += " != 0";
		} else if (result.startsWith("if (!(M00_Helper.inStr(")) {
			result = result.substring(0, result.length() - 1);
			result += " != 0)";
		}
		
		result += ") {\n";

		// Befehle ausführen
		for (PFunctionStmt functionStmt : node.getFunctionStmt()) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString();
		}
		
		result += "}";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAIfStmtOneLine(AIfStmtOneLine node) {
		String result = "";
		
		result += "if ("; 
	
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();

		result += ") {\n";

		// ersten Befehl ausführen
		Visitor functionStmtVisitor = new Visitor(className);
		node.getFirst().apply(functionStmtVisitor);
		result += functionStmtVisitor.getResult().toString() + ";\n";
		// Befehle ausführen
		for (PIfStmtOneLineValue functionStmt : node.getIfStmtOneLineValue()) {
			Visitor functionStmt2Visitor = new Visitor(className);
			functionStmt.apply(functionStmt2Visitor);
			result += functionStmt2Visitor.getResult().toString() + ";\n";
		}
		
		result += "}\n";
		
		result = result.replace("{;", "{");
		
		this.result.append(result);
	}
	
	@Override
	public void caseAElseIfStm(AElseIfStm node) {
		String result = "";
		
		result += "} else if ("; 
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ") {";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAElseStmt(AElseStmt node) {
		String result = "";
		
		result += "} else {"; 
		
		this.result.append(result);
	}
	
	@Override
	public void caseAShortIfStmt(AShortIfStmt node) {
		String result = "";

		result += "(";
		
		// Bedingung
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += " ? ";
		
		Visitor valueTrueVisitor = new Visitor(className);
		node.getTrue().apply(valueTrueVisitor);
		result += valueTrueVisitor.getResult().toString();
		
		result += " : ";
		
		Visitor valueFalseVisitor = new Visitor(className);
		node.getFalse().apply(valueFalseVisitor);
		result += valueFalseVisitor.getResult().toString();
				
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseASelectStmt(ASelectStmt node) {
		String result = "";
		
		result += "switch ("; 
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
	
		result += ") {\n";

		
		// Befehle ausführen
		for (PSelectCaseStmt selectCaseStmt : node.getSelectCaseStmt()) {
			Visitor selectCaseStmtVisitor = new Visitor(className);
			selectCaseStmt.apply(selectCaseStmtVisitor);
			result += selectCaseStmtVisitor.getResult().toString();
		}
		
		result += "}";
		
		this.result.append(result);
	}
	
	@Override
	public void caseADefaultSelectCaseStmt(ADefaultSelectCaseStmt node) {
		String result = "";
		
		result += "default: {"; 
	
		// Befehle ausführen
		for (PFunctionStmt functionStmt : node.getFunctionStmt()) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString();
		}

		result += "}";
		
		this.result.append(result);
	}
	@Override
	public void caseAValuesSelectCaseStmt(AValuesSelectCaseStmt node) {
		String result = "";
		
		for (PParamCallListWithoutParensPart paramPart : ((AParamCallListWithoutParens)node.getParamCallListWithoutParens()).getParamCallListWithoutParensPart()) {
			if (paramPart instanceof AParamCallListWithoutParensPart) {
				Visitor valueVisitor = new Visitor(className);
				((AParamCallListWithoutParensPart) paramPart).getParamCallListWithoutParensValue().apply(valueVisitor);
				result += "case " + valueVisitor.getResult().toString() + ": {";
			}
		}
		
		Visitor valueVisitor = new Visitor(className);
		((AParamCallListWithoutParens)node.getParamCallListWithoutParens()).getParamCallListWithoutParensValue().apply(valueVisitor);
		result += "case " + valueVisitor.getResult().toString() + ": {";

		// Befehle ausführen
		for (PFunctionStmt functionStmt : node.getFunctionStmt()) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString();
		}
		
		if (!result.contains("return")) {
			result += "break;\n";
		}
		
		result += "}";

		this.result.append(result);
	}
	
////////////////////Bedingungen/logishce Verknüpfungen////////////////////
	
	@Override
	public void caseACondition(ACondition node) {
		String result = "";
		
		Visitor firstLogicalPartVisitor = new Visitor(className);
		node.getFirst().apply(firstLogicalPartVisitor);
		result += firstLogicalPartVisitor.getResult().toString() + " ";
		
		for (PLogicalPart logicalPart : node.getLogicalPart()) {
			Visitor logicalPartVisitor = new Visitor(className);
			logicalPart.apply(logicalPartVisitor);
			result += logicalPartVisitor.getResult().toString();
		}
		
		Visitor logicalValueVisitor = new Visitor(className);
		node.getLogicalValue().apply(logicalValueVisitor);
		result += logicalValueVisitor.getResult().toString();

		this.result.append(result);
	}
	@Override
	public void caseANotCondition(ANotCondition node) {
		String result = "";
		
		result += "!(";
		
		Visitor firstLogicalPartVisitor = new Visitor(className);
		node.getFirst().apply(firstLogicalPartVisitor);
		result += firstLogicalPartVisitor.getResult().toString() + " ";
		
		for (PLogicalPart logicalPart : node.getLogicalPart()) {
			Visitor logicalPartVisitor = new Visitor(className);
			logicalPart.apply(logicalPartVisitor);
			result += logicalPartVisitor.getResult().toString();
		}
		
		Visitor logicalValueVisitor = new Visitor(className);
		node.getLogicalValue().apply(logicalValueVisitor);
		result += logicalValueVisitor.getResult().toString();
		
		result += ")";

		this.result.append(result);
	}
	@Override
	public void caseANotValueCondition(ANotValueCondition node) {
		String result = "";
		
		result += "!(";
		
		Visitor valueVisitor = new Visitor(className);
		node.getLogicalValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		result += ")";

		this.result.append(result);
	}
	
	@Override
	public void caseAAndLogicalPart(AAndLogicalPart node) {
		String result = "";
		
		Visitor logicalValueVisitor = new Visitor(className);
		node.getLogicalValue().apply(logicalValueVisitor);
		result += logicalValueVisitor.getResult().toString();
		
		result += " & ";
		
		this.result.append(result);
	}
	@Override
	public void caseAAndNotLogicalPart(AAndNotLogicalPart node) {
		String result = "";
		
		Visitor logicalValueVisitor = new Visitor(className);
		node.getLogicalValue().apply(logicalValueVisitor);
		result += logicalValueVisitor.getResult().toString();
		
		result += " & !";
		
		this.result.append(result);
	}
	@Override
	public void caseAOrLogicalPart(AOrLogicalPart node) {
		String result = "";
		
		Visitor logicalValueVisitor = new Visitor(className);
		node.getLogicalValue().apply(logicalValueVisitor);
		result += logicalValueVisitor.getResult().toString();
		
		result += " | ";
		
		this.result.append(result);
	}
	@Override
	public void caseAOrNotLogicalPart(AOrNotLogicalPart node) {
		String result = "";
		
		Visitor logicalValueVisitor = new Visitor(className);
		node.getLogicalValue().apply(logicalValueVisitor);
		result += logicalValueVisitor.getResult().toString();
		
		result += " | !";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAComparison(AComparison node) {
		String result = "";
		
		// Wenn beide Werte Strings sein können, handelt es sich vermutlich um einen String Vergleich,
		// wozu andere Operatoren verwendet werden müssen.
		Visitor valueVisitor = new Visitor(className);
		node.getA().apply(valueVisitor);
		String leftValue = valueVisitor.getResult().toString();
		
		Visitor valueVisitor2 = new Visitor(className);
		node.getB().apply(valueVisitor2);
		String rightValue = valueVisitor2.getResult().toString();

		if (!isPrimitiveDataType(leftValue) && !isPrimitiveDataType(rightValue)) {
			
			if (node.getA() instanceof ACharacterStringComparisonValue && node.getB() instanceof ACharacterStringComparisonValue) { // Bsp: "abs" = "xyz"
				result += leftValue + " ";
				
				Visitor operatorVisitor = new Visitor(className);
				node.getLogicalOperator().apply(operatorVisitor);
				result += operatorVisitor.getResult().toString();
				
				result += " " + rightValue;
			} else if (node.getB() instanceof ACharacterStringComparisonValue) { // id = "abc" --> id.equals("abc");
				if (node.getLogicalOperator() instanceof AEqualLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") == 0";
				} else if (node.getLogicalOperator() instanceof ANotEqualLogicalOperator) {
					result += "!(" + leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") == 0)";
				} else if (node.getLogicalOperator() instanceof ALessThanLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") < 0";
				} else if (node.getLogicalOperator() instanceof AGreaterThanLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") > 0";
				} else if (node.getLogicalOperator() instanceof ALessThanOrEqualLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") <= 0";
				} else if (node.getLogicalOperator() instanceof AGreaterThanOrEqualLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") >= 0";
				}
			} else if (node.getA() instanceof ACharacterStringComparisonValue) { // "abc" = id --> id.equals("abc");
				if (node.getLogicalOperator() instanceof AEqualLogicalOperator) {
					result += rightValue + ".compareTo(";
					
					result += leftValue;
					
					result += ") == 0";
				} else if (node.getLogicalOperator() instanceof ANotEqualLogicalOperator) {
					result += "!(" + rightValue + ".compareTo(";
					
					result += leftValue;
					
					result += ") == 0)";
				} else if (node.getLogicalOperator() instanceof ALessThanLogicalOperator) {
					result += rightValue + ".compareTo(";
					
					result += leftValue;
					
					result += ") < 0";
				} else if (node.getLogicalOperator() instanceof AGreaterThanLogicalOperator) {
					result += rightValue + ".compareTo(";
					
					result += leftValue;
					
					result += ") > 0";
				} else if (node.getLogicalOperator() instanceof ALessThanOrEqualLogicalOperator) {
					result += rightValue + ".compareTo(";
					
					result += leftValue;
					
					result += ") <= 0";
				} else if (node.getLogicalOperator() instanceof AGreaterThanOrEqualLogicalOperator) {
					result += rightValue + ".compareTo(";
					
					result += leftValue;
					
					result += ") >= 0";
				} 
			} else { // Bsp: abc = xyz
				if (node.getLogicalOperator() instanceof AEqualLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") == 0";
				} else if (node.getLogicalOperator() instanceof ANotEqualLogicalOperator) {
					result += "!(" + leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") == 0)";
				} else if (node.getLogicalOperator() instanceof ALessThanLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") < 0";
				} else if (node.getLogicalOperator() instanceof AGreaterThanLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") > 0";
				} else if (node.getLogicalOperator() instanceof ALessThanOrEqualLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") <= 0";
				} else if (node.getLogicalOperator() instanceof AGreaterThanOrEqualLogicalOperator) {
					result += leftValue + ".compareTo(";
					
					result += rightValue;
					
					result += ") >= 0";
				}
			}
		} else {
			result += leftValue + " ";
			
			Visitor operatorVisitor = new Visitor(className);
			node.getLogicalOperator().apply(operatorVisitor);
			result += operatorVisitor.getResult().toString();
			
			result += " " + rightValue;
		}
		
		this.result.append(result);
	}
	@Override
	public void caseACharacterStringInParanesComparisonValue(ACharacterStringInParanesComparisonValue node) {
		String result = "";

		result += "(";
		
		Visitor characterStringVisitor = new Visitor(className);
		node.getCharacterString().apply(characterStringVisitor);
		result += characterStringVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	@Override
	public void caseAConditionInParens(AConditionInParens node) {
		String result = "";

		result += "(";
		
		Visitor conditionVisitor = new Visitor(className);
		node.getCondition().apply(conditionVisitor);
		result += conditionVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	@Override
	public void caseAArithmeticExpressionInParensComparisonValue(AArithmeticExpressionInParensComparisonValue node) {
		String result = "";

		result += "(";
		
		Visitor conditionVisitor = new Visitor(className);
		node.getArithmeticExpression().apply(conditionVisitor);
		result += conditionVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	// SimpelCondition	
	@Override
	public void caseASimpelCondition(ASimpelCondition node) {
		String result = "";
		
		for (PSimpelConditionPart logicalPart : node.getSimpelConditionPart()) {
			Visitor logicalPartVisitor = new Visitor(className);
			logicalPart.apply(logicalPartVisitor);
			result += logicalPartVisitor.getResult().toString();
		}
		
		Visitor logicalValueVisitor = new Visitor(className);
		node.getSimpelConditionValue().apply(logicalValueVisitor);
		result += logicalValueVisitor.getResult().toString();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAOrSimpelConditionPart(AOrSimpelConditionPart node) {
		String result = "";
		
		Visitor logicalValueVisitor = new Visitor(className);
		node.getSimpelConditionValue().apply(logicalValueVisitor);
		result += logicalValueVisitor.getResult().toString();
		
		result += " | ";
		
		this.result.append(result);
	}
	
///////////////Schleifen///////////////
	
	@Override
	public void caseAWhileStmt(AWhileStmt node) {
		String result = "";
		
		result += "while ("; 
			
		Visitor conditionVisitor = new Visitor(className);
		node.getCondition().apply(conditionVisitor);
		result += conditionVisitor.getResult().toString();

		result += ") {\n";

		// Befehle ausführen
		for (PFunctionStmt functionStmt : node.getFunctionStmt()) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString();
		}
		
		result += "}";
		
		this.result.append(result);
	}
	
	@Override
	public void caseADoWhile(ADoWhile node) {
		String result = "";
		
		result += "do {"; 

		// Befehle ausführen
		for (PFunctionStmt functionStmt : node.getFunctionStmt()) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString();
		}
		
		result += "} while (";
		Visitor conditionVisitor = new Visitor(className);
		node.getCondition().apply(conditionVisitor);
		result += conditionVisitor.getResult().toString();
		
		result += ");";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAForEachStmt(AForEachStmt node) {
		String result = "";
		
		result += "for ("; 
			
		Visitor listVisitor = new Visitor(className);
		node.getList().apply(listVisitor);

		result += visitorDataObject.getVariablesDataType(listVisitor.getResult().toString()) + " ";
		
		Visitor variableIdVisitor = new Visitor(className);
		node.getVariable().apply(variableIdVisitor);
		result += variableIdVisitor.getResult().toString() + " : ";
		
		
		result += listVisitor.getResult().toString() + " : ";

		result += ") {\n";

		// Befehle ausführen
		for (PFunctionStmt functionStmt : node.getFunctionStmt()) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString();
		}
		
		result += "}";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAForEachStmtOnlyNext(AForEachStmtOnlyNext node) {
		String result = "";
		
		result += "for ("; 
			
		Visitor listVisitor = new Visitor(className);
		node.getList().apply(listVisitor);

		result += visitorDataObject.getVariablesDataType(listVisitor.getResult().toString()) + " ";
		
		Visitor variableIdVisitor = new Visitor(className);
		node.getVariable().apply(variableIdVisitor);
		result += variableIdVisitor.getResult().toString() + " : ";
		
		
		result += listVisitor.getResult().toString() + " : ";

		result += ") {\n";

		// Befehle ausführen
		for (PFunctionStmt functionStmt : node.getFunctionStmt()) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString();
		}
		
		result += "}";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAForStmt(AForStmt node) {
		String result = "";
		
		result += "for (int "; 
			
		Visitor varSetVisitor = new Visitor(className);
		node.getVarSet().apply(varSetVisitor);
		result += varSetVisitor.getResult().toString() + "; ";
		
		Visitor idCounterVisitor = new Visitor(className);
		node.getCounter().apply(idCounterVisitor);
		
		Visitor idLimitVisitor = new Visitor(className);
		node.getLimit().apply(idLimitVisitor);

		result += idCounterVisitor.getResult().toString() + " <= " + idLimitVisitor.getResult().toString() + "; " +
				  idCounterVisitor.getResult().toString() + "++";
		
		result += ") {\n";

		// Befehle ausführen
		for (PFunctionStmt functionStmt : node.getFunctionStmt()) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString();
		}
		
		result += "}";
		
		this.result.append(result);
	}

	@Override
	public void caseAForStmtStep(AForStmtStep node) {
		String result = "";
		
		result += "for ("; 
			
		Visitor varSetVisitor = new Visitor(className);
		node.getVarSet().apply(varSetVisitor);
		result += varSetVisitor.getResult().toString() + "; ";
		
		Visitor idCounterVisitor = new Visitor(className);
		node.getCounter().apply(idCounterVisitor);
		
		Visitor idLimitVisitor = new Visitor(className);
		node.getLimit().apply(idLimitVisitor);

		Visitor stepVisitor = new Visitor(className);
		node.getArithmeticExpression().apply(stepVisitor);

		result += idCounterVisitor.getResult().toString() + " <= " + stepVisitor.getResult().toString() + "; " +
				  idCounterVisitor.getResult().toString() + " += (" + stepVisitor.getResult().toString() + ")";
		
		result += ") {\n";

		// Befehle ausführen
		for (PFunctionStmt functionStmt : node.getFunctionStmt()) {
			Visitor functionStmtVisitor = new Visitor(className);
			functionStmt.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString();
		}
		
		result += "}";
		
		this.result.append(result);
	}
	
///////////////Print///////////////

	@Override
	public void caseALofFunction(ALofFunction node) {
		String result = "";

		result += "M00_FileWriter.openFileForOutput(";
		
		Visitor fileNumberVisitor = new Visitor(className);
		node.getFileNumber().apply(fileNumberVisitor);
		result += fileNumberVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAFreeFileFunction(AFreeFileFunction node) {
		String result = "";
			
		result += "M00_FileWriter.freeFileNumber()";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAOpenFileFunction(AOpenFileFunction node) {
		String result = "";
		
		if (node.getOpenFileMode() instanceof AOpenFileAppendModeOpenFileMode) {
		
			result += "M00_FileWriter.openFileForOutput(";
			
			Visitor fileNumberVisitor = new Visitor(className);
			node.getFileNumber().apply(fileNumberVisitor);
			result += fileNumberVisitor.getResult().toString() + ", ";

			Visitor fileNameVisitor = new Visitor(className);
			node.getFileName().apply(fileNameVisitor);
			result += fileNameVisitor.getResult().toString();
			
			result += ", true)";
		} else if (node.getOpenFileMode() instanceof AOpenFileOutputModeOpenFileMode) {

			result += "M00_FileWriter.openFileForOutput(";
			
			Visitor fileNumberVisitor = new Visitor(className);
			node.getFileNumber().apply(fileNumberVisitor);
			result += fileNumberVisitor.getResult().toString() + ", ";
			
			Visitor fileNameVisitor = new Visitor(className);
			node.getFileName().apply(fileNameVisitor);
			result += fileNameVisitor.getResult().toString();
			
			result += ", false)";
		} else if (node.getOpenFileMode() instanceof AOpenFileInputModeOpenFileMode) {
			
			result += "M00_FileWriter.openFileForInput(";

			Visitor fileNumberVisitor = new Visitor(className);
			node.getFileNumber().apply(fileNumberVisitor);
			result += fileNumberVisitor.getResult().toString() + ", ";
			
			
			Visitor fileNameVisitor = new Visitor(className);
			node.getFileName().apply(fileNameVisitor);
			result += fileNameVisitor.getResult().toString();
			
			result += "))";
		}
		
		this.result.append(result);
	}
	
	@Override
	public void caseACloseFileFunction(ACloseFileFunction node) {
		String result = "";

		result += "M00_FileWriter.closeFile(";
		
		Visitor fileNumberVisitor = new Visitor(className);
		node.getFileNumber().apply(fileNumberVisitor);
		result += fileNumberVisitor.getResult().toString();
		
		result += ")";

		this.result.append(result);
	}
	
	@Override
	public void caseAAPrintFunction(AAPrintFunction node) {
		String result = "";

		result += "M00_FileWriter.printToFile(";

		Visitor fileNumberVisitor = new Visitor(className);
		node.getFileNumber().apply(fileNumberVisitor);
		result += fileNumberVisitor.getResult().toString() + ", ";

		// Print-Teile zusammenfügen
		for (PPrintFunctionPart printFunctionPart : node.getPrintFunctionPart()) {
			Visitor printPartVisitor = new Visitor(className);
			printFunctionPart.apply(printPartVisitor);
			result += printPartVisitor.getResult().toString() + " + ";
		}
		
		Visitor lastValueVisitor = new Visitor(className);
		node.getValue().apply(lastValueVisitor);
		result += lastValueVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	@Override
	public void caseABPrintFunction(ABPrintFunction node) {
		String result = "";

		result += "M00_FileWriter.printToFile(";

		Visitor fileNumberVisitor = new Visitor(className);
		node.getFileNumber().apply(fileNumberVisitor);
		result += fileNumberVisitor.getResult().toString() + ", ";

		
		if (!node.getPrintFunctionPart().isEmpty()) {
			// Print-Teile zusammenfügen
			for (PPrintFunctionPart printFunctionPart : node.getPrintFunctionPart()) {
				Visitor printPartVisitor = new Visitor(className);
				printFunctionPart.apply(printPartVisitor);
				result += printPartVisitor.getResult().toString() + " + ";
			}
			
			// das letzte " + " wieder entfernen
			result = result.substring(0, result.length() - 3);
		} else {
			result += "\"\"";
		}
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAInputFunction(AInputFunction node) {
		String result = "";

		result += "M00_FileWriter.textOfFile(";
		
		Visitor fileNumberVisitor = new Visitor(className);
		node.getFileNumber().apply(fileNumberVisitor);
		result += fileNumberVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseADebugPrintFunction(ADebugPrintFunction node) {
		String result = "";

		result += "System.out.println(";

		// Print-Teile zusammenfügen
		for (PPrintFunctionPart printFunctionPart : node.getPrintFunctionPart()) {
			Visitor functionStmtVisitor = new Visitor(className);
			printFunctionPart.apply(functionStmtVisitor);
			result += functionStmtVisitor.getResult().toString() + " + ";
		}
		
		Visitor lastValueVisitor = new Visitor(className);
		node.getValue().apply(lastValueVisitor);
		result += lastValueVisitor.getResult().toString();
		
		result += ");\n";
		
		this.result.append(result);
	}

	
	@Override
	public void caseAValueCommaPrintFunctionPart(AValueCommaPrintFunctionPart node) {
		String result = "";

		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString() + " + \"\t\"";
		
		this.result.append(result);
	}
	@Override
	public void caseAValueSemicolonPrintFunctionPart(AValueSemicolonPrintFunctionPart node) {
		String result = "";
		
		Visitor valueVisitor = new Visitor(className);
		node.getValue().apply(valueVisitor);
		result += valueVisitor.getResult().toString();
		
		this.result.append(result);
	}
	@Override
	public void caseASemicolonPrintFunctionPart(ASemicolonPrintFunctionPart node) {
	}	
	
	
	
	@Override
	public void caseAKillFunction(AKillFunction node) {
		String result = "";

		result += "Files.delete(";

		Visitor stringValueVisitor = new Visitor(className);
		node.getStringValue().apply(stringValueVisitor);
		result += stringValueVisitor.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseARenameFileFunction(ARenameFileFunction node) {
		// TODO Auto-generated method stub
	}
	
///////////////MsgBox///////////////

	@Override
	public void caseAMsgBoxFunction(AMsgBoxFunction node) {
		// TODO Auto-generated method stub
		String result = "";

		result += "System.out.println(";

		result += "MsgBox: " + node.getParamCallList().toString();
		
		result += ");\n";
		
		this.result.append(result);
	}
	@Override
	public void caseAMsgBoxFunctionWithoutParens(AMsgBoxFunctionWithoutParens node) {
		// TODO Auto-generated method stub
	}
	
///////////////Excel///////////////
	
////// Objekte
	@Override
	public void caseAExcelActiveWindow(AExcelActiveWindow node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getActiveWindow().apply(visitor);
		result += visitor.getResult().toString();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAActiveWorbookExcelWorkbook(AActiveWorbookExcelWorkbook node) {
		String result = "";
		
		result += "M00_Excel.activeWorkbook";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAIdExcelWorkbook(AIdExcelWorkbook node) {
		String result = "";
		
		result += ((AId)node.getId()).getIdToken().getText();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAActiveWindowExcelSheet(AActiveWindowExcelSheet node) {
		String result = "";
		
		Visitor windowVisitor = new Visitor(className);
		node.getExcelActiveWindow().apply(windowVisitor);
		result += windowVisitor.getResult().toString();
		
		result += ".";
		
		Visitor sheetVisitor = new Visitor(className);
		node.getExcelSheetValue().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();		
		
		this.result.append(result);
	}
	
	@Override
	public void caseAWorkbookExcelSheet(AWorkbookExcelSheet node) {
		String result = "";
		
		Visitor workBookVisitor = new Visitor(className);
		node.getExcelWorkbook().apply(workBookVisitor);
		result += workBookVisitor.getResult().toString();
		
		result += ".";

		Visitor sheetVisitor = new Visitor(className);
		node.getExcelSheetValue().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();		
		
		this.result.append(result);
	}
	
	@Override
	public void caseAIdExcelSheet(AIdExcelSheet node) {
		String result = "";

		result += ((AId)node.getId()).getIdToken().getText();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAWorksheetsExcelSheet(AWorksheetsExcelSheet node) {
		String result = "";
		
		result += "M00_Excel.activeWorkbook.";
		
		Visitor visitor = new Visitor(className);
		node.getExcelSheetValue().apply(visitor);
		result += visitor.getResult().toString();
			
		this.result.append(result);
	}
	
	@Override
	public void caseAMethodChainingExcelSheet(AMethodChainingExcelSheet node) {
		String result = "";
		
		Visitor methodVisitor = new Visitor(className);
		node.getMethodChaining().apply(methodVisitor);
		result += methodVisitor.getResult().toString();
		
		result += ".";
		
		Visitor sheetVisitor = new Visitor(className);
		node.getExcelSheetValue().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();		
		
		this.result.append(result);
	}
	
	@Override
	public void caseAWorksheetsExcelSheetValue(AWorksheetsExcelSheetValue node) {
		String result = "";
		
		result += "M00_Excel.activeWorkbook.getSheet";
		
		Visitor visitor = new Visitor(className);
		node.getWorksheets().apply(visitor);
		result += visitor.getResult().toString();
			
		this.result.append(result);
	}
	
	@Override
	public void caseAActiveSheetExcelSheetValue(AActiveSheetExcelSheetValue node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getActiveSheet().apply(visitor);
		result += visitor.getResult().toString();
			
		this.result.append(result);
	}
	
	@Override
	public void caseAWorksheetsWithParamsExcelSheetValue(AWorksheetsWithParamsExcelSheetValue node) {
		String result = "";
		
		// check if string or integer
		String param = ((AParamCallList)node.getParamCallList()).getParamCallValue().toString().trim();
		boolean isString = false;
		try {
			new Integer(param);
		} catch (NumberFormatException e) {
			isString = true;
		}
		if (isString) {
			result += "getSheet";
		} else {		
			result += "getSheetAt";
		}
		
		Visitor workSheetsVisitor = new Visitor(className);
		node.getWorksheets().apply(workSheetsVisitor);
		result += workSheetsVisitor.getResult().toString();
			
		Visitor visitor = new Visitor(className);
		node.getParamCallList().apply(visitor);
		result += visitor.getResult().toString();

		this.result.append(result);
	}
	
	@Override
	public void caseASheetsWithParamsExcelSheetValue(ASheetsWithParamsExcelSheetValue node) {
		String result = "";
		
		Visitor sheetsVisitor = new Visitor(className);
		node.getSheets().apply(sheetsVisitor);
		result += sheetsVisitor.getResult().toString();
		
		result += "getSheet";
			
		Visitor visitor = new Visitor(className);
		node.getParamCallList().apply(visitor);
		result += visitor.getResult().toString();

		this.result.append(result);
	}
	
	@Override
	public void caseASelectedSheetsExcelSheetValue(ASelectedSheetsExcelSheetValue node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getSelectedSheets().apply(visitor);
		result += visitor.getResult().toString();
			
		this.result.append(result);
	}
	
	@Override
	public void caseAExcelRow(AExcelRow node) {
		String result = "";
		
		Visitor methodVisitor = new Visitor(className);
		node.getExcelSheet().apply(methodVisitor);
		result += methodVisitor.getResult().toString();
		
		result += ".";
		
		Visitor sheetVisitor = new Visitor(className);
		node.getRows().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();		
		
		this.result.append(result);
	}
	
	@Override
	public void caseASheetExcelColumn(ASheetExcelColumn node) {
		String result = "";
		
		Visitor methodVisitor = new Visitor(className);
		node.getExcelSheet().apply(methodVisitor);
		result += methodVisitor.getResult().toString();
		
		result += ".";
		
		Visitor sheetVisitor = new Visitor(className);
		node.getExcelColumneValue().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();		
		
		this.result.append(result);
	}
	
	@Override
	public void caseAExcelColumneValue(AExcelColumneValue node) {
		// TODO Typo + check
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getColumns().apply(visitor);
		result += visitor.getResult().toString();
			
		this.result.append(result);
	}
	
	@Override
	public void caseAExcelCell(AExcelCell node) {
		String result = "";
		
		result += "M00_Excel.getCell(";
		
		Visitor visitor = new Visitor(className);
		node.getExcelSheet().apply(visitor);
		result += visitor.getResult().toString();
		
		result += ", ";
		
		Visitor excelCellVisitor = new Visitor(className);
		node.getExcelCellValue().apply(excelCellVisitor);
		String cellYX = excelCellVisitor.getResult().toString();
		cellYX.substring(1);		
		result += cellYX.substring(1);
		
		result += ".getStringCellValue()";
		
		this.result.append(result);
	}
	
	@Override
	public void caseAWithParamsExcelCellValue(AWithParamsExcelCellValue node) {
		String result = "";
		
		Visitor visitor = new Visitor(className);
		node.getParamCallList().apply(visitor);
		result += visitor.getResult().toString();

		this.result.append(result);
	}
	
////// Funktionen

	@Override
	public void caseACopyFunction(ACopyFunction node) {
		String result = "";
		
		result += "M00_Excel.copySheet(";
		
		Visitor sheetVisitor = new Visitor(className);
		node.getExcelSheet().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();
		
		result += ")";
				
//		Visitor paramVisitor = new Visitor(className);
//		node.getParamCallListWithoutParens().apply(paramVisitor);
//		result += paramVisitor.getResult().toString();

		this.result.append(result);
	}
	
	@Override
	public void caseAActiveWorbookFullNameFunction(AActiveWorbookFullNameFunction node) {
		String result = "";
		
		result += "M00_Excel.fileName";
		
		this.result.append(result);
	}
	
	@Override
	public void caseACellSelectFunction(ACellSelectFunction node) {
		String result = "";
		
		Visitor cellVisitor = new Visitor(className);
		node.getExcelCell().apply(cellVisitor);
		result += cellVisitor.getResult().toString();
		
		result += ".setAsActiveCell()";
		
		this.result.append(result);
	}
	
	@Override
	public void caseASheetSelectFunction(ASheetSelectFunction node) {
		String result = "";
		
		Visitor sheetVisitor = new Visitor(className);
		node.getExcelSheet().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();
		
		result += ".";
		
		Visitor selectVisitor = new Visitor(className);
		node.getSelect().apply(selectVisitor);
		result += selectVisitor.getResult().toString();		
		
		this.result.append(result);
	}
	
	@Override
	public void caseACountFunction(ACountFunction node) {
		String result = "";
		
		Visitor sheetVisitor = new Visitor(className);
		node.getExcelSheet().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();
		
		result += ".";
		
		Visitor countVisitor = new Visitor(className);
		node.getCount().apply(countVisitor);
		result += countVisitor.getResult().toString();		
		
		this.result.append(result);
	}
	
	@Override
	public void caseAMoveFunction(AMoveFunction node) {
		String result = "";
		
		Visitor sheetVisitor = new Visitor(className);
		node.getExcelSheet().apply(sheetVisitor);
		result += sheetVisitor.getResult().toString();
		
		result += ".";
		
		Visitor moveVisitor = new Visitor(className);
		node.getMove().apply(moveVisitor);
		result += moveVisitor.getResult().toString();		
		
		Visitor paramVisitor = new Visitor(className);
		node.getParamCallListWithoutParens().apply(paramVisitor);
		result += paramVisitor.getResult().toString();

		this.result.append(result);
	}
	
	@Override
	public void caseARowAutoFitFunction(ARowAutoFitFunction node) {
		String result = "";		
		this.result.append(result);
	}
	
	@Override
	public void caseAColumnAutoFitFunction(AColumnAutoFitFunction node) {
		String result = "";		
		this.result.append(result);
	}
	
	@Override
	public void caseASheetGetNameExcelFunction(ASheetGetNameExcelFunction node) {
		String result = "";
		
		Visitor visitor1 = new Visitor(className);
		node.getExcelSheet().apply(visitor1);
		result += visitor1.getResult().toString();
		
		result += ".";
		
		Visitor visitor2 = new Visitor(className);
		node.getName().apply(visitor2);
		result += visitor2.getResult().toString();		
		
		this.result.append(result);
	}
	
	@Override
	public void caseAWorkbookGetNameExcelFunction(AWorkbookGetNameExcelFunction node) {
		// TODO Auto-generated method stub
		super.caseAWorkbookGetNameExcelFunction(node);
	}
	
	@Override
	public void caseASetNameExcelFunction(ASetNameExcelFunction node) {
		String result = "";
		
		result += "M00_Excel.renameSheet(";
		
		Visitor visitor1 = new Visitor(className);
		node.getExcelSheet().apply(visitor1);
		result += visitor1.getResult().toString();
		
		result += ", ";
		
		Visitor visitor4 = new Visitor(className);
		node.getId().apply(visitor4);
		result += visitor4.getResult().toString();		

		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseASheetAddFunction(ASheetAddFunction node) {
		String result = "";
		
		result += "M00_Excel.activeWorkbook.createSheet()";
		
//		Visitor visitor2 = new Visitor(className);
//		node.getAdd().apply(visitor2);
//		result += visitor2.getResult().toString();		
//		
//		Visitor visitor3 = new Visitor(className);
//		node.getParamCallList().apply(visitor3);
//		result += visitor3.getResult().toString();		
				
		this.result.append(result);
	}
	
	@Override
	public void caseASheetDeleteFunction(ASheetDeleteFunction node) {
		String result = "";
		
		result += "M00_Excel.deleteSheet(";

		Visitor visitor1 = new Visitor(className);
		node.getExcelSheet().apply(visitor1);
		result += visitor1.getResult().toString();
		
		result += ")";
		
		this.result.append(result);
	}
	
	@Override
	public void caseASheetActivateFunction(ASheetActivateFunction node) {
		// TODO Auto-generated method stub
		super.caseASheetActivateFunction(node);
	}
	
	@Override
	public void caseASetCellFunction(ASetCellFunction node) {
		String result = "";
		
		Visitor visitor1 = new Visitor(className);
		node.getExcelCell().apply(visitor1);
		result += visitor1.getResult().toString();
		
		result += ".setCellValue(";
		
		Visitor visitor3 = new Visitor(className);
		node.getValue().apply(visitor3);
		result += visitor3.getResult().toString();	
		
		result += ")";
		
		this.result.append(result);
	}
	
    @Override
    public void caseAAddFunctionVarSet(AAddFunctionVarSet node) {
		String result = "";
		
		Visitor visitor1 = new Visitor(className);
		node.getMethodChaining().apply(visitor1);
		result += visitor1.getResult().toString();
		
		result += " = ";
		
		Visitor visitor3 = new Visitor(className);
		node.getAddFunction().apply(visitor3);
		result += visitor3.getResult().toString();
		
		result += ";";		
		
		this.result.append(result);
    }

	
///////////////GoTo///////////////
	
	@Override
	public void caseAGotoCall(AGotoCall node) {
		String result = "";
		
		result += "goto "; 
			
		result += node.getIdToken().getText();
		
		this.result.append(result);
	}
	
	@Override
	public void caseAGotoCodeStart(AGotoCodeStart node) {
		String result = "";
		
		result += node.getGotoId().getText();
		
		this.result.append(result);
	}
	
///////////////Private Helfer Methoden///////////////
	
	/**
	 * Die Methode gibt für einen gegebenen Wert den Java Datentyp aus.
	 * 
	 * @param value der Wert von dem der Datentyp ermittelt werden soll
	 * @return den Datentyp als String oder null, falls der Datentyp nicht herausgefunden werden konnte
	 */
	private String getDataTypeOfValue(PValue value) {
		if (value instanceof AMethodChainingValue) {
			if (((AMethodChainingValue)value).getMethodChaining() instanceof AOnlyValueMethodChaining) {
				return visitorDataObject.getVariablesDataType(((AId)((AOnlyValueMethodChaining)((AMethodChainingValue)value).getMethodChaining()).getId()).getIdToken().getText());
			}
		} else if (value instanceof ACharacterStringValue) {
			return "String";
		} else if (value instanceof AArithmeticExpressionValue) {
			return "int";
		} else if (value instanceof AConditionValue) {
			return "boolean";
		} else if (value instanceof ATrueValue) {
			return "boolean";
		} else if (value instanceof AFalseValue) {
			return "boolean";
		}

		return null;
	}
	/**
	 * Die Methode gibt für einen gegebenen Datentyp den Java Objekt-Datentyp aus.
	 * 
	 * @param value der Datentyp von dem der Objekt-Datentyp ermittelt werden soll
	 * @return den Objekt-Datentyp als String oder null, falls der Objekt-Datentyp nicht herausgefunden werden konnte
	 */
	private String getWrapperDataTypeOfDataType(PDataType dataType) {
		if (dataType instanceof AIntegerDataType) {
			return "Integer";
		} else if (dataType instanceof ALongDataType) {
			return "Long";
		} else if (dataType instanceof ASingleDataType) {
			return "Double";
		} else if (dataType instanceof AStringDataType) {
			return "String";
		} else if (dataType instanceof ABooleanDataType) {
			return "Boolean";
		} else if (dataType instanceof AIntegerArrayDataType) {
			return "int[]";
		} else if (dataType instanceof AIdDataType) {
			Visitor idVisitor = new Visitor(className);
			((AIdDataType)dataType).apply(idVisitor);
			return idVisitor.getResult().toString();
		}
		
		return null;
	}
	
	/**
	 * Die Methode gibt aus, ob der eingegebene ComparissonValue-Wert einen String repräsentieren köönnte.
	 * 
	 * @param value der Wert, bei dem überprüft werden soll, ob er einen String repräsentiert
	 * @return true: wenn der Wert einen String repräsentieren könnt; false: wenn nicht
	 */
	private boolean isPrimitiveDataType(String value) {
		if (visitorDataObject.getVariablesDataType(value) != null) {
			String dataType = visitorDataObject.getVariablesDataType(value);
			if (dataType.equals("int")) {
				return true;
			} else if (dataType.equals("long")) {
				return true;
			} else if (dataType.equals("double")) {
				return true;
			} else if (dataType.equals("boolean")) {
				return true;
			}
		} else if (!value.contains("\"")) {
			return true;
		}
		
		return false;
	}
}
