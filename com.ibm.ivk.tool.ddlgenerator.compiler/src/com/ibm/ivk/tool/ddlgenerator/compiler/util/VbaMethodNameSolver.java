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
 * Die Klasse sorgt dafür, dass alle Datenfelderaufrufe, die in Java durch Methodenaufrufe erfolgen müssen
 * umgeschrieben werden, sodass sie mit einem Großbuchstaben beginnen.
 * 
 * @author Paul Wenzel, wenzel.paul@de.ibm.com
 *
 */
public class VbaMethodNameSolver {

	/**
	 * Die Methode durchsucht einen Ordner mit VBA-Dateien und schreibt alle
	 * Datenfelderaufrufe, die in Java durch Methodenaufrufe erfolgen müssen
	 * um, sodass sie mit einem Großbuchstaben beginnen.
	 * 
	 * @param inputFolder
	 * @param outputFolder
	 */
	public static void vbaMethodsUpperCase(String inputFolder, String outputFolder) {
		for (File file : new File(inputFolder).listFiles()) {
			StringBuffer result = new StringBuffer();
			
			// VBA-Datei auslesen
			try {
				FileInputStream fstream = new FileInputStream(inputFolder + "/" + file.getName());
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				
				while ((strLine = br.readLine()) != null) {
					
					// alle Name (".name") Abfragen auf Excel-Objekte in ".Name" umwandeln
					ArrayList<String> nameExceptions = new ArrayList<String>();
					nameExceptions.add("g_pools.descriptors(thisPoolIndex).name");
					nameExceptions.add("comp.CodeModule.Parent.name");
					nameExceptions.add("g_orgs.descriptors(thisOrgIndex).name");
					nameExceptions.add("genSequence(\"Sequence for Generating Object IDs for FwkTest / MPC \"\"\" & .name & \"\"\"\", g");
					nameExceptions.add("g_orgs.descriptors(i).name");
					nameExceptions.add("g_pools.descriptors(i).name");
					nameExceptions.add("g_classes.descriptors(classIndex).subclassIndexesRecursive(i)).subClassIdStrSeparatePartition.maps(j).name");
					nameExceptions.add("mapping.maps(i).name");
					nameExceptions.add(".numMaps.name = name");
					nameExceptions.add(".name = Trim(thisSheet.Cells(thisRow, colName))");
					nameExceptions.add("g_orgs.descriptors(orgIndex).name");
					nameExceptions.add("genTemplateParamWrapper(.name), .name)");
					nameExceptions.add("g_pools.descriptors(poolIndex).name");
					nameExceptions.add("mapping.maps(mapping.numMaps).name = name");
					boolean changeName = true;
					for (String s : nameExceptions) {
						if (strLine.contains(s)) {
							changeName = false;
						}
					}
					if (changeName) {
						strLine = strLine.replace(".name", ".Name");
					}
					
					result.append(strLine + "\n"); 
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
}
