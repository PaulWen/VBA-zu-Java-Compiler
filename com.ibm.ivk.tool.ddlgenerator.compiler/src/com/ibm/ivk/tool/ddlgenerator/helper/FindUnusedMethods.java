package com.ibm.ivk.tool.ddlgenerator.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FindUnusedMethods {

	public static void main(String[] args) {
		
		ArrayList<String> ignore = new ArrayList<String>();
		ignore.add("doPackForDelivery");
		ignore.add("doPackProductive");
		ignore.add("doPackTest");
		ignore.add("doRunForDelivery");
		ignore.add("doRunProductiveEw");
		ignore.add("doRunTest");
		ignore.add("exportCode");
		ignore.add("exportGeneratorGeneric");
		ignore.add("exportGeneratorTrim");
		ignore.add("exportSheets");
		
		for (String s : findUnusedMethods("VBCode", ignore)) {
			System.out.println(s);
		}
	}
	
	/**
	 * Die Methode durchsucht einen Ordner mit VBA-Dateien und sucht alle Subs/Functions raus,
	 * welche nicht verwendet werden.
	 * 
	 * @param inputFolder
	 * @return Liste mit den Namen aller Subs/Functions, welche nicht aufgerufen werden
	 */
	private static ArrayList<String> findUnusedMethods(String inputFolder, ArrayList<String> idstoIgnore) {
		HashMap<String, Integer> methodList = new HashMap<String, Integer>();
		
		for (String methodId : findAllMethods(inputFolder)) {
			if (!idstoIgnore.contains(methodId)) {
				methodList.put(methodId, 0);
			}
		}
		
		for (File file : new File(inputFolder).listFiles()) {
			System.out.println(file.getName());
			
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
							for (String key : methodList.keySet()) {
								if (line[i].startsWith(key) || line[i].startsWith(key + "(") || line[i].startsWith(key + "()") || line[i].contains("(" + key)) {
									// gucken, ob es sich lediglich um ein Return handelt
									if (!((line.length > i + 1) && line[i + 1].equals("=")) || line[i].contains("(" + key)) {
										// wenn die Methoden-ID gefunden wurde dies in der Map vermerken
										methodList.put(key, methodList.get(key) + 1);
									}
								}
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
		
		
		//Ausgabe
		ArrayList<String> unusedMethods = new ArrayList<String>();
		
		for (String key : methodList.keySet()) {
			// wenn die Methoden-ID nur einmal vorkam so handelt es sich um die Methoden Definition --> 
			if (methodList.get(key) == 1) {
				unusedMethods.add(key);
			}
		}
		
		return unusedMethods;
	}
	
	/**
	 * Die Methode durchsucht einen Ordner mit VBA-Dateien und schreibt alle
	 * IDs der Subs und Functions raus
	 * 
	 * @param inputFolder
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
								if (!methoodIdList.contains(line[i + 1].replace("()", ""))) {
									String id = line[i + 1];
									id = id.replace("(", "");
									id = id.replace(")", "");
									methoodIdList.add(id);
									System.out.println("NEUE METHODE: " + id);
								}
								break;
							} else if (line[i].equals("Function")) { // Function
								if (!methoodIdList.contains(line[i + 1].replace("(", ""))) {
									String id = line[i + 1];
									id = id.replace("(", "");
									id = id.replace(")", "");
									methoodIdList.add(id);
									System.out.println("NEUE METHODE: " + id);
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
