package com.ibm.ivk.tool.ddlgenerator.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * ACHTUNG: Die Klasse funktioniert vermutlich noch nicht richtig! Da die Entwicklung nicht zielführend war wurde sie abgebrochen!
 * 
 * 
 * @author wenzel
 *
 */
public class DeleteMethods {
	
//	public static void main(String[] args) {
//		deleteMethods("VBCode", "Temp", FindMissingLinesBetweenTwoFiles.findMissingLinesBetweenTwoFiles("C:/Users/IBM_ADMIN/Desktop/all Methods.txt", "C:/Users/IBM_ADMIN/Desktop/P - Used Methods.txt"));
//	}
	
	/**
	 * Die Methode durchsucht einen Ordner mit VBA-Dateien und schreibt am anfang
	 * jeder Methode ein Print-Stmt um den Namen der methode in eine Datei zuschreiben.
	 * 
	 * @param inputFolder
	 * @param outputFolder
	 * @param methodsToDelete
	 */
	private static void deleteMethods(String inputFolder, String outputFolder, ArrayList<String> methodsToDelete) {
		
		for (File file : new File(inputFolder).listFiles()) {
			StringBuffer result = new StringBuffer();
			
			// VBA-Datei auslesen
			try {
				FileInputStream fstream = new FileInputStream(inputFolder + "/" + file.getName());
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				
				boolean deleteLine = false;
				
				while ((strLine = br.readLine()) != null) {
					String[] line = strLine.split(" ");
					
					
					if (!isLineComment(strLine)) {
						for (int i = 0; i < line.length; i++) {
							if (line[i].equals("Sub")) { // Sub
								String id = line[i + 1];
								id = id.replace("(", "");
								id = id.replace(")", "");
								
								if (methodsToDelete.contains(id)) {
									deleteLine = true;
									System.out.println("METHODE LÖSCHEN: " + id);
								}
								break;
							} else if (line[i].equals("Function")) { // Function
								String id = line[i + 1];
								id = id.replace("(", "");
								id = id.replace(")", "");
								
								if (methodsToDelete.contains(id)) {
									deleteLine = true;
									System.out.println("METHODE LÖSCHEN: " + id);
								}
								break;
							} else if ((line[i].equals("End") && line[i + 1].equals("Sub")) || (line[i].equals("End") && line[i + 1].equals("Function"))) { // zu ignorierende Zeilen
								deleteLine = false;
								break;
							} else if (line[i].equals("Exit")) {
								break;
							}
						}
					}
					
					if (!deleteLine) {
						result.append(strLine + "\n");
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
