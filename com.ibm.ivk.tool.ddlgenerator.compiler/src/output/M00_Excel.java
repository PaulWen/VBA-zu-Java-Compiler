package output;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


/**
 * Klasse für Methoden die auf das zugrundeliegende Excelsheet zugreifen.
 * 
 * @author thomas.loehnert@de.ibm.com
 */
public class M00_Excel {
	
	public static String fileName = "C:\\temp\\MDS-Object+Data-Model.xlsm"; 
	
	public static HSSFWorkbook activeWorkbook; 
	
	public static Cell getCell(Sheet sheet, int rowIdx, int columnIdx) {		
		Row row = sheet.getRow(rowIdx);
		Cell cell = row.getCell(columnIdx);
		return cell;		
	}
	
	public static void copySheet(Sheet sheet) {		
		activeWorkbook.cloneSheet(activeWorkbook.getSheetIndex(sheet));
	}
	
	public static void activateSheet(Sheet sheet) {		
		activeWorkbook.setActiveSheet(activeWorkbook.getSheetIndex(sheet));
	}
	
	public static void deleteSheet(Sheet sheet) {		
		activeWorkbook.removeSheetAt(activeWorkbook.getSheetIndex(sheet));
	}
	
	public static void renameSheet(Sheet sheet, String name) {		
		activeWorkbook.setSheetName(activeWorkbook.getSheetIndex(sheet), name);
	}
	
	static {
		try {
			InputStream input = new FileInputStream(fileName);
			activeWorkbook = new HSSFWorkbook(new POIFSFileSystem(input));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
