package com.ibm.ivk.tool.ddlgenerator.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditVbaMethods {

//	public static void main(String[] args) {
//		
//		editAllMethods("VBCode", "Temp");
//	}
	
	/**
	 * Die Methode durchsucht einen Ordner mit VBA-Dateien und schreibt am anfang
	 * jeder Methode ein Print-Stmt um den Namen der methode in eine Datei zuschreiben.
	 * 
	 * @param inputFolder
	 */
	private static void editAllMethods(String inputFolder, String outputFolder) {
		
		for (File file : new File(inputFolder).listFiles()) {
			StringBuffer result = new StringBuffer();
			
			// VBA-Datei auslesen
			try {
				FileInputStream fstream = new FileInputStream(inputFolder + "/" + file.getName());
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				
				boolean printStmt = false;
				String methodId = null;
				
				while ((strLine = br.readLine()) != null) {
					String[] line = strLine.split(" ");
					
					
					if (!isLineComment(strLine)) {
						for (int i = 0; i < line.length; i++) {
							if (line[i].equals("Sub")) { // Sub
								String id = line[i + 1];
								id = id.replace("(", "");
								id = id.replace(")", "");
								methodId = id;
								System.out.println("NEUE METHODE: " + id);
								break;
							} else if (line[i].equals("Function")) { // Function
								String id = line[i + 1];
								id = id.replace("(", "");
								id = id.replace(")", "");
								methodId = id;
								System.out.println("NEUE METHODE: " + id);
								break;
							} else if (line[i].equals("End") || line[i].equals("Exit")) { // zu ignorierende Zeilen
								break;
							}
							
							if (strLine.endsWith(")") || strLine.startsWith(") As")) {
								printStmt = true;
							}
						}
					}
					
					if (methodId != null && printStmt) {
						result.append(strLine + "\n");
						result.append("  Print #debugFile, \"" + methodId + "\"" + "\n");
						methodId = null;
						printStmt = false;
					} else {
						result.append(strLine + "\n");
					}
					
					if (printStmt) {
						printStmt = false;
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
