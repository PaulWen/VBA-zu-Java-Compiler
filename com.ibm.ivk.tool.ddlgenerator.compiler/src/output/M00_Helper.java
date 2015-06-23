package output;

/**
 * Die Klasse definiert standard VBA-Methoden, die nicht direkt mit standard Java-Methoden umgesetzt werden können.
 * 
 * @author Paul Wenzel, wenzel.paul@de.ibm.com
 *
 */
public class M00_Helper {
	
	/**
	 * Die Methode sucht den größten Index von einem int[] heraus.
	 * 
	 * @return gibt den größten Index aus oder -1, wenn das Array null ist
	 */
	public static int uBound(int[] array) {
		if (array == null) {
			return -1;
		} else {
			return array.length -1;
		}
	}
	/**
	 * Die Methode sucht den größten Index von einem Object[] heraus.
	 * 
	 * @return gibt den größten Index aus oder -1, wenn das Array null ist
	 */
	public static int uBound(Object[] array) {
		if (array == null) {
			return -1;
		} else {
			return array.length -1;
		}
	}
	
	/**
	 * Die Methode gibt den ersten Index vom int[] aus, bei dem der Wert nicht 0 ist.
	 * 
	 * @return gibt den ersten Index aus, bei dem der Wert nicht 0 ist oder -1, wenn das Array null ist, oder alle Stellen im Array 0 sind
	 */
	public static int lBound(int[] array) {
		if (array == null) {
			return -1;
		} else {
			for (int i = 0; i < array.length; i++) {
				if (array[i] != 0) {
					return i;
				}
			}
		}
		
		return -1;
	}
	/**
	 * Die Methode gibt den ersten Index vom Object[] aus, bei dem der Wert nicht Null ist.
	 * 
	 * @return gibt den ersten Index aus, bei dem der Wert nicht null ist oder -1, wenn das Array null ist, oder alle Stellen im Array null sind
	 */
	public static int lBound(Object[] array) {
		if (array == null) {
			return -1;
		} else {
			for (int i = 0; i < array.length; i++) {
				if (array[i] != null) {
					return i;
				}
			}
		}
		
		return -1;
	}
	
	/**
	 * Die Methode gibt einen String mit der gewünschten Anzahl an Leerzeichen aus.
	 * 
	 * @param gewünschte Anzahl an Leerzeichen
	 * @return den String, welche aus der gewünschte Anzahl an Leerzeichen besteht
	 */
	public static String space(int number) {
		StringBuffer result = new StringBuffer();
		
		for (int i = 0; i < number; i++) {
			result.append(" ");
		}
		return result.toString();
		
	}
	
	/**
	 * Die Methode gibt den Start-Index der ersten Position eines Substrings innerhalb eines Strings aus.
	 * 
	 * @param string der zu durchsuchende String
	 * @param find der Substring, der gefunden werden soll
	 * @return den Start-Index oder -1, wenn der Substring nicht gefunden werden konnte
	 */
	public static int inStr(String string, String find) {
		return string.indexOf(find) + 1;
	}
	
	public static String replace(String expression, String find, String replacement) {
		return replace(expression, find, replacement, 1, -1);
	}
	public static String replace(String expression, String find, String replacement, int start) {
		return replace(expression, find, replacement, start, -1);
	}
	public static String replace(String expression, String find, String replacement, int start, int count) {
		String subExpression = expression.substring(start - 1, expression.length());
		String[] splitExpression = subExpression.split(find);
		String result = "";
		
		int i = 0;
		for (String s : splitExpression) {
			if (i < count || count == -1) {
				result += s + replacement;
			} else {
				result += s + find;
			}
			i++;
		}
		
		return expression.substring(0, start) + result;
	}
	
	/**
	 * Die Methode gibt den Start-Index der ersten Position eines Substrings innerhalb eines Strings aus.
	 * Ab wo die Suche nach dem ersten Vorkommen des Substrings im String begonnen werden soll, kann ebenfalls definiert werden.
	 * 
	 * @param startIndex wo die Suche nach dem ersten Vorkommen des Substrings begonnen werden soll (0 --> ganz vorne)
	 * @param string der zu durchsuchende String
	 * @param find der Substring, der gefunden werden soll
	 * @return den Start-Index oder -1, wenn der Substring nicht gefunden werden konnte
	 */
	public static int inStr(int startIndex, String string, String find) {
		return string.indexOf(find, startIndex - 1) + 1;
	}
	
	/**
	 * Die Methode wandelt einen double-Wert in einen int-Wert um.
	 * 
	 * @param number der double-Wert der in einen int-Wert umgewandelt werden soll
	 * @return den zum int gecasteten double-Wert
	 */
	public static int cInt(double number) {
		return (int)number;
	}
}
