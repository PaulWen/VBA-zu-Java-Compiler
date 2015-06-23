package output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Die Klasse simuliert ansatzweise VBA-I/O-Zugriffe, um die Portierung zu Java zu vereinfachen.
 * 
 * @author Paul Wenzel, wenzel.paul@de.ibm.com
 *
 */
public class M00_FileWriter {

	// die Arrays "fileNumbers" und "fileNames" werden immer parallel geupdatet,
	// sodass der gleichen Index jeweils den gleichen I/O-Zugriff beschreibt
	/** binhaltet alle FileWriter/FileReader */
	private static Object[] fileNumbers = new FileWriter[255]; 
	/** beinhlatet den Namen der Dtaei, auf die zugegriffen wird */
	private static String[] fileNames = new String[255]; 
	
	/**
	 * Die Methode gibt die nächste freie FileNumber im M00_FileWriter.fileNumbers - Array aus.
	 * 
	 * @return die nächste freie FileNumber oder -1, wenn es keine freie mehr gibt.
	 */
	public static int freeFileNumber() {
		int i = 0;
		for (Object writer : fileNumbers) {
			if (writer == null) {
				return i;
			}
			i++;
		}
		
		return -1;
	}
	
	public static void openFileForOutput(int fileNumber, String fileName, boolean append) {
		if (fileNumbers[fileNumber] != null) {
			try {
				if (fileNumbers[fileNumber] instanceof FileWriter) {
					((FileWriter)fileNumbers[fileNumber]).close();
				}
				fileNumbers[fileNumber] = null;
				fileNames[fileNumber] = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		try {
			fileNumbers[fileNumber] = new FileWriter(fileName, append);
			fileNames[fileNumber] = fileName;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void closeFile(int fileNumber) {
		if (fileNumbers[fileNumber] != null) {
			try {
				if (fileNumbers[fileNumber] instanceof FileWriter) {
					((FileWriter)fileNumbers[fileNumber]).close();
				}
				fileNumbers[fileNumber] = null;
				fileNames[fileNumber] = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void printToFile(int fileNumber, String text) {
		if (fileNumbers[fileNumber] != null) {
			try {
				if (fileNumbers[fileNumber] instanceof FileWriter) {
					((FileWriter)fileNumbers[fileNumber]).write(text);
					((FileWriter)fileNumbers[fileNumber]).write(System.getProperty("line.separator"));
					((FileWriter)fileNumbers[fileNumber]).flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void openFileForInput(int fileNumber, String fileName) {
		if (fileNumbers[fileNumber] != null) {
			try {
				if (fileNumbers[fileNumber] instanceof FileWriter) {
					((FileWriter)fileNumbers[fileNumber]).close();
				}
				fileNumbers[fileNumber] = null;
				fileNames[fileNumber] = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			fileNumbers[fileNumber] = Files.readAllBytes(Paths.get(fileName));
			fileNames[fileNumber] = fileName;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static String textOfFile(int fileNumber) {
		if (fileNumbers[fileNumber] != null) {
			if (fileNumbers[fileNumber] instanceof byte[]) {
				return new String((byte[])fileNumbers[fileNumber]);
			}
		}
		return null;
	}
	
	public static long byteSizeOfFile(int fileNumber) {
		if (fileNumbers[fileNumber] != null) {
			return new File(fileNames[fileNumber]).length();
		}
		return -1;
	}
}
