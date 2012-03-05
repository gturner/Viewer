/**
 * 
 */
package au.edu.anu.scanu.utils;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Rahul Khanna
 * 
 */
public final class Utils
{
	/**
	 * Returns a String from an InputStream object.
	 * 
	 * @param inStream
	 *            An InputStream Object.
	 * @return A String containing the characters from InputStream Object.
	 */
	public static String InputStreamToString(InputStream inStream)
	{
		return new Scanner(inStream).useDelimiter("\\A").next();
	}

	/**
	 * Capitalises the first character of a string.
	 * 
	 * @param inString
	 *            String whose first character needs to be changed to uppercase all other characters will be changed to lowercase.
	 * @return
	 */
	public static String titleCase(String inString)
	{
		char firstChar = Character.toUpperCase(inString.charAt(0));
		return Character.toString(firstChar) + inString.substring(1).toLowerCase();
	}
}
