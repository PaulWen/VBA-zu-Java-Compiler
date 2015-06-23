package com.ibm.ivk.tool.ddlgenerator.compiler.util;

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
 * Die Klasse dient dem Umschreiben von allen  Subs-/Functions-Aufrufe,
 * bei denen die Parameterliste nicht in Klammern steht, sodass alle Parameterlisten in Klammern stehen.
 * 
 * @author Paul Wenzel, wenzel.paul@de.ibm.com
 *
 */
public class MethodCallWithoutParensSolver {

	/**
	 * Die Methode durchsucht einen Ordner mit VBA-Dateien und sucht alle Subs-/Functions-Aufrufe raus,
	 * bei denen die Parameterliste nicht in Klammern steht. All diese Subs-/Functions-Aufrufe werden dann
	 * umgeschrieben, sodass die Parameterliste in Klammern steht.
	 * 
	 * @param inputFolder
	 * @param outputFolder
	 */
	public static void findMethodCallsWithoutParens(String inputFolder, String outputFolder) {
		ArrayList<String> methodList = findAllMethods(inputFolder);
		
		for (File file : new File(inputFolder).listFiles()) {
			StringBuffer result = new StringBuffer();
			
			// VBA-Datei auslesen
			try {
				FileInputStream fstream = new FileInputStream(inputFolder + "/" + file.getName());

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				
				boolean continueLine = false;
				
				while ((strLine = br.readLine()) != null) {
					String leadingSpaces = strLine.replace(strLine.trim(), "");
					strLine = strLine.trim();
					String[] line = strLine.split(" ");
					if (!isLineComment(strLine) || continueLine) {
						if (continueLine) {
							if (!strLine.endsWith(" _")) {
								strLine = strLine + ")";
								continueLine = false;
							}
						} else {
							for (String method : methodList) {
								if (line.length > 0 && line[0].equals(method)) {
									if (!(line.length > 1 && line[1].startsWith("("))) {
										// Methodenaufruf gefunden, bei denen die Klammern um die Parameter FEHLEN
										// gucken, ob es sich lediglich um ein Return handelt
										if (!((line.length > 1) && line[1].equals("="))) {
											if (strLine.length() == line[0].length()) {
												strLine = line[0] + "()";
												break;
											} else if (!strLine.endsWith(" _")) {
												strLine = line[0] + "(" + strLine.substring(line[0].length() + 1, strLine.length()) + ")";
												break;
											} else {
												strLine = line[0] + "(" + strLine.substring(line[0].length() + 1, strLine.length());
												continueLine = true;
												break;
											}
										}
									}
								}
							}
						}
					}
					result.append(leadingSpaces + strLine + "\n");
				}
				
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
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
	
	/**
	 * Die Methode durchsucht einen Ordner mit VBA-Dateien und schreibt alle
	 * IDs der Subs und Functions raus.
	 * 
	 * @param inputFolder
	 * @return List mit den Namen aller Subs und Functions
	 */
	private static ArrayList<String> findAllMethods(String inputFolder) {
		ArrayList<String> methoodIdList = new ArrayList<String>();
		
		for (File file : new File(inputFolder).listFiles()) {
			// VBA-Datei auslesen
			try {
				FileInputStream fstream = new FileInputStream(inputFolder + "/" + file.getName());

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				
				while ((strLine = br.readLine()) != null) {
					String[] line = strLine.split(" ");
					
					if (!isLineComment(strLine)) {
						for (int i = 0; i < line.length; i++) {
							if (line[i].equals("Sub")) { // Sub
								if (!methoodIdList.contains(line[i + 1].replace("()", "")) || !methoodIdList.contains(line[i + 1].replace("(", ""))) {
									String id = line[i + 1];
									id = id.replace("(", "");
									id = id.replace(")", "");
									
									methoodIdList.add(id);
								}
								break;
							} else if (line[i].equals("Function")) { // Function
								if (!methoodIdList.contains(line[i + 1].replace("()", "")) || !methoodIdList.contains(line[i + 1].replace("(", ""))) {
									String id = line[i + 1];
									id = id.replace("(", "");
									id = id.replace(")", "");
									methoodIdList.add(id);
								}
								break;
							} else if (line[i].equals("End") || line[i].equals("Exit")) { // zu ignorierende Zeilen
								break;
							}
						}
						
					}
				}
				
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return methoodIdList;
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
