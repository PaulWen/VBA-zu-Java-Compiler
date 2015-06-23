package com.ibm.ivk.tool.ddlgenerator.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Die Klasse vergleicht zwei Text-Dateien und gibt aus welche Zeilen in FileA vorkommen die nicht in FileB vorkommen.
 * 
 * @author wenzel
 *
 */
public class FindMissingLinesBetweenTwoFiles {

//	public static void main(String[] args) {
//		for (String s : findMissingLinesBetweenTwoFiles("C:/Users/IBM_ADMIN/Desktop/D - Used Methods.txt", "C:/Users/IBM_ADMIN/Desktop/P - Used Methods.txt")) {
//			System.out.println(s);
//		}
//	}
	
	/**
	 * Die Methode vergleicht zwei Text-Dateien und gibt aus welche Zeilen in FileA vorkommen die nicht in FileB vorkommen.
	 * 
	 * @param fileA der Dateipfad auf die DateiA
	 * @param fileB der Dateipfad auf die DateiB
	 * 
	 * @return alle Zeilen die in FileA aber NICHT in FileB vorkommen
	 */
	public static ArrayList<String> findMissingLinesBetweenTwoFiles(String fileA, String fileB) {
		ArrayList<String> lineListFileB = new ArrayList<String>();
		ArrayList<String> extraLinesInA = new ArrayList<String>();
		
		// DateiB auslesen
		try {
			FileInputStream fstream = new FileInputStream(fileB);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				if (!lineListFileB.contains(strLine)) {
					lineListFileB.add(strLine);
				}
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// mit DateiA abgleichen
		try {
			FileInputStream fstream = new FileInputStream(fileA);
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			
			while ((strLine = br.readLine()) != null) {
				if (!lineListFileB.contains(strLine)) {
					extraLinesInA.add(strLine);
				}
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return extraLinesInA;
	}
	
}
