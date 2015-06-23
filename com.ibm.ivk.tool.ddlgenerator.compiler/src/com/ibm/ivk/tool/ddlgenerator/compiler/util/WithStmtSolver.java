package com.ibm.ivk.tool.ddlgenerator.compiler.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

/**
 * Die Klasse dient dem Auflösen von With-Stmts in VBA-Code.
 * 
 * @author Paul Wenzel, wenzel.paul@de.ibm.com
 *
 */
public class WithStmtSolver {

	/**
	 * Die Methode durchsucht einen Ordner mit VBA-Dateien und löst alle
	 * With-Stmts auf.
	 * 
	 * @param inputFolder
	 * @param outputFolder
	 */
	public static void removeWithStmts(String inputFolder, String outputFolder) {
		for (File file : new File(inputFolder).listFiles()) {
			StringBuffer result = new StringBuffer();
			
			// VBA-Datei auslesen
			try {
				FileInputStream fstream = new FileInputStream(inputFolder + "/" + file.getName());
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				
				Stack<String> with = new Stack<String>();
				
				while ((strLine = br.readLine()) != null) {
					boolean deleteLine = false;
					
					String[] line = strLine.split(" ");

					if (!isLineComment(strLine)) {
						// Datei umschreiben
						for (int i = 0; i < line.length; i++) {
							
							if (line[i].equals("With")) { // With Start
								
								// Falls im With-Stmt ein Leerzeichen vorkommt
								String withValue = "";
								for (int j = i + 1; j < line.length; j++) {
									withValue += line[j] + " ";
								}
								// das letzte " " wieder entfernen
								withValue = withValue.substring(0, withValue.length() - 1);
								
								// Falls das With-Stmt ein anderes With-Stmt braucht
								if (withValue.startsWith(".") || withValue.contains(" .")) { // With Verwendung
									String[] withValueSplit = withValue.split(" ");
									for (int j = 0; j < withValueSplit.length; j++) {
										
										if (withValueSplit[j].startsWith(".")) {
											withValueSplit[j] = with.peek() + withValueSplit[j];
										}
									}
									withValue = "";
									for (String s : withValueSplit) {
										withValue += s + " ";
									}
									// das letzte " " wieder entfernen
									withValue = withValue.substring(0, withValue.length() - 1);
								} 
								
								// Sonderfälle
								if (withValue.contains("(.")) {
									withValue = withValue.split("\\(\\.")[0]  + "(" + with.peek() + "." + withValue.split("\\(\\.")[1];
								}
								if (line[i].contains("#.")) {
									withValue = withValue.split("#\\.")[0]  + "#" + with.peek() + "." + withValue.split("#\\.")[1];
								}
								
								with.push(withValue);

								deleteLine = true;
								break;
							} else if (line[i].equals("End") && line[++i].equals("With")) { // With Ende
								with.pop();
								deleteLine = true;
								break;
							} 
							
							if (line[i].startsWith(".") && !(line[i].startsWith("..") || line[i].startsWith("./"))) { // With Verwendung
								if (!with.isEmpty()) {
									line[i] = with.peek() + line[i];
								}
							} 
							// Sonderfälle
							if (line[i].contains("(.")) {
								line[i] = line[i].split("\\(\\.")[0]  + "(" + with.peek() + "." + line[i].split("\\(\\.")[1];
							}
							if (line[i].contains("#.")) {
								line[i] = line[i].split("#\\.")[0]  + "#" + with.peek() + "." + line[i].split("#\\.")[1];
							}
						}
						
					}
					
					if (!deleteLine) {
						// in den Result String schreiben
						for (String s : line) {
							result.append(" " + s); 
						}
						result.append("\n");
					}
				}
				in.close();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}


			// VBA-Datei neu erstellen
			File newFile = new File(outputFolder + "/" + file.getName());
			try {
				FileWriter writer = new FileWriter(newFile, false);

				writer.write(result.toString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static boolean isLineComment(String s) {
		for (char c : s.toCharArray()) {
			if (c != ' ' && c == '\'') {
				return true;
			} else if (c != ' ') {
				break;
			}
		}
		return false;
	}
}
