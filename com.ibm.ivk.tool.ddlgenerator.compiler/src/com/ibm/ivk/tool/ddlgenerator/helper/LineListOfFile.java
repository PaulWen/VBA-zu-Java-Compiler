package com.ibm.ivk.tool.ddlgenerator.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LineListOfFile {
	
	public static void main(String[] args) {
		for (String s : lineList("C:/Users/IBM_ADMIN/Desktop/test.txt")) {
			System.out.println(s);
		}
	}
	
	/**
	 * Die Methode durchsucht eine Datei un erstellt eine Liste mit allen Zeilen die Vorkommen.
	 * Doppelte Zeilen werden dabei nur einmal aufgelistet!
	 * 
	 * @param inputFilePath der Dateipfad auf die auzulesende Datei
	 */
	private static ArrayList<String> lineList(String inputFilePath) {
		ArrayList<String> lineList = new ArrayList<String>();
		
		// Datei auslesen
		try {
			FileInputStream fstream = new FileInputStream(inputFilePath);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				if (!lineList.contains(strLine)) {
					lineList.add(strLine);
				}
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lineList;
	}
}
