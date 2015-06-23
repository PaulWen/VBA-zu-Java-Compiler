package com.ibm.ivk.tool.ddlgenerator.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PushbackReader;

import com.ibm.ivk.tool.ddlgenerator.compiler.util.MethodCallWithoutParensSolver;
import com.ibm.ivk.tool.ddlgenerator.compiler.util.VbaMethodNameSolver;
import com.ibm.ivk.tool.ddlgenerator.compiler.util.WithStmtSolver;
import com.ibm.ivk.tool.ddlgenerator.sablecc.lexer.Lexer;
import com.ibm.ivk.tool.ddlgenerator.sablecc.node.EOF;
import com.ibm.ivk.tool.ddlgenerator.sablecc.node.Token;
import com.ibm.ivk.tool.ddlgenerator.sablecc.parser.Parser;

/**
 * Die Klasse definiert zwei verschiedene Main-Methoden.
 * 
 * Die erste Main-Methode führt den kompletten Compiler aus.
 * 
 * Die zweite Main-Methode beschränkt sich lediglich auf das Lexing. Sie dient dazu genau zu gucken, in welche Tokens
 * der Lexer die Eingabe zerlegt. 
 * 
 * @author Paul Wenzel, wenzel.paul@de.ibm.com
 *
 */
public class Main {

	/**
	 * Die Methode führt einen kompletten Kompiliervorgang durch.
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("START");
		
		// With-Stmts auflösen
		System.out.println("With-Stmts im VBA-Code auflösen...");
		WithStmtSolver.removeWithStmts("VBCode", "temp/no_with_stmts");
		
		// alle Parameterlisten von Methodenaufrufen, im VBA-Code, in Klammern setzen
		System.out.println("Alle Parameterlisten von Methodenaufrufen, im VBA-Code, in Klammern setzen...");
		MethodCallWithoutParensSolver.findMethodCallsWithoutParens("temp/no_with_stmts", "temp/no_with_stmts & no_method_calls_without_parens");
		
		// alle VBA-Methoden, die mit einem klein Buchstaben beginnen umschreiben,
		// sodass alle mit einem Großbuchstaben beginnen
		System.out.println("Alle VBA-Methoden, die mit einem klein Buchstaben beginnen umschreiben...");
		VbaMethodNameSolver.vbaMethodsUpperCase("temp/no_with_stmts & no_method_calls_without_parens", "temp/no_with_stmts & no_method_calls_without_parens & no_lowercase_vba_method");
		
		// Der erste und zweite Durchlauf ist dafür da, dass die Listen vom statischen VisitorDataObject mit allen Variablen-, Methoden- Enum-Namen etc. gefüllt werden.
		System.out.println("VisitorDataObject mit allen Variablen-, Methoden- Enum-Namen etc. füllen...");
		for (int i = 0; i < 2; i++) {
				for (File file : new File("temp/no_with_stmts & no_method_calls_without_parens & no_lowercase_vba_method").listFiles()) {
				System.out.print(".");
				Lexer lexer = new Lexer(new PushbackReader(new BufferedReader(new FileReader("temp/no_with_stmts & no_method_calls_without_parens & no_lowercase_vba_method/" + file.getName()))));
				Parser parser = new Parser(lexer);
				Visitor visitor = new Visitor(file.getName().replaceAll(".bas", ""));
				parser.parse().apply(visitor);
			}
			System.out.println();
		}
		
		// Wo nun die Liste mit allen Variablen-, Methoden- Enum-Namen etc. erstellt wurde, das dritte mal den Kompiliervorgang durchlaufen lassen, um den VBA-Code in Java-Code zu übersetzen.
		// Da nun die Listen vom VisitorDataObject mit allen Variablen-, Methoden- Enum-Namen etc. gefüllt wurden, können jetzt alle Referenzen und Datentypen richtig gesetzt werden!
		System.out.println("VBA-Code in Java-Code übersetzen...");
		for (File file : new File("temp/no_with_stmts & no_method_calls_without_parens & no_lowercase_vba_method").listFiles()) {
			System.out.println("FILE: " + file.getName());
			Lexer lexer = new Lexer(new PushbackReader(new BufferedReader(new FileReader("temp/no_with_stmts & no_method_calls_without_parens & no_lowercase_vba_method/" + file.getName()))));
			Parser parser = new Parser(lexer);
			Visitor visitor = new Visitor(file.getName().replaceAll(".bas", ""));
			parser.parse().apply(visitor);
			
			// Java-Datei erstellen
			try {
				FileWriter writer = new FileWriter("src/output/" + file.getName().replaceAll(".bas", "") + ".java", false);
				
				writer.write("package output;\n");
				writer.write(System.getProperty("line.separator"));
				writer.write("import org.apache.poi.ss.usermodel.*;\n");
				writer.write("import java.nio.file.*;\n");
				writer.write("import java.text.*;\n");
				writer.write("import java.util.Date;\n");
				writer.write(System.getProperty("line.separator"));
				writer.write("public class " + file.getName().replaceAll(".bas", "") + " {");
				writer.write(System.getProperty("line.separator"));
				writer.write(System.getProperty("line.separator"));
				writer.write(visitor.getResult().toString());
				writer.write(System.getProperty("line.separator"));
				writer.write("}");
				
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("FERTIG!!");
	}

	/**
	 * Die Methode führt nur den Scanner aus und gibt in der Konsole aus, in welche lexikalischen Einheiten (Tokens) die Eingabe zerteilt wird.
	 */
	public static void mainLexer(String[] args) throws Exception {
		for (File file : new File("temp/no_with_stmts & no_method_calls_without_parens & no_lowercase_vba_method").listFiles()) {
			System.out.println("FILE: " + file.getName());
			Lexer lexer = new Lexer(new PushbackReader(new BufferedReader(new FileReader("temp/no_with_stmts & no_method_calls_without_parens & no_lowercase_vba_method/" + file.getName()))));
			while (!(lexer.peek() instanceof EOF)) {
				Token token = lexer.next();
				String[] parts = token.getClass().toString().split("\\.");
				System.out.println(token.getText() + " --> "
						+ parts[parts.length - 1] + "  (" + file.getName() + ")");
			}
		}
	}
}