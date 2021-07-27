package net.celestialgaze.IkuBot;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IkuUtil {
	
	/**
	 * @return A formatted timestamp in the format yyyy-MM-dd@HH:mm:ss.ms for the current time in the America/Chicago timezone
	 */
	public static String getTimestamp() {
		return getTimestamp("America/Chicago"); // My timezone
	}
	
	/**
	 * @param timezone The <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/ZoneId.html">timezone</a> to get the time for	
	 * @return A formatted timestamp in the format yyyy-MM-dd@HH:mm:ss.ms for the current time in the selected timezome
	 */
	public static String getTimestamp(String timezone) {
		return getTimestamp(timezone, Instant.now());
	}
	
	/**
	 * @param timezone The <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/ZoneId.html">timezone</a> to get the time for	
	 * @param instant The instant of time
	 * @return A formatted timestamp in the format yyyy-MM-dd@HH:mm:ss.ms for the current time in the selected timezome
	 */
	public static String getTimestamp(String timezone, Instant instant) {
		ZoneId zoneId = ZoneId.of(timezone);
		ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd@HH:mm:ss.ms");
		String output = zdt.format(formatter);
		return output;
	}
	
	/**
	 * Converts an array to a string
	 * @param array The array to convert
	 * @param delimiter What to put between each element in the final string
	 * @return The array in string form
	 */
	public static String arrayToString(String[] array, String delimiter) {
		return arrayToString(array, delimiter, 0, array.length - 1);
	}
	
	/**
	 * Converts an array to a string
	 * @param array The array to convert
	 * @param delimiter What to put between each element in the final string
	 * @param beginIndex What element to start at
	 * @param endIndex What element to end at
	 * @return The array in string form
	 */
	public static String arrayToString(String[] array, String delimiter, int beginIndex, int endIndex) {
		String[] cutArray = cutArray(array, beginIndex, endIndex);
		String str = "";
		for (int i = 0; i < cutArray.length; i++) {
			str += cutArray[i];
			if (i != cutArray.length - 1) {
				str += delimiter;
			}
		}
		return str;
	}
	
	/**
	 * Cuts an array from beginIndex to endIndex
	 * @param array The array to cut
	 * @param beginIndex The begin index
	 * @param endIndex The endIndex
	 * @return The cut array
	 */
	public static String[] cutArray(String[] array, int beginIndex, int endIndex) {
		String[] newArray = new String[endIndex - beginIndex + 1];
		int j = 0;
		for (int i = beginIndex; i <= endIndex && i < array.length; i++) {
			newArray[j] = array[i];
			j++;
		}
		return newArray;
	}
	
	/**
	 * @param toParse String to parse
	 * @return Whether or not it is an integer
	 */
	public static boolean isInteger(String toParse) {
		try {
			Integer.parseInt(toParse);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * @param toParse String to parse
	 * @return The integer value, or 0 if it is not an integer.
	 */
	public static int getInteger(String toParse) {
		try {
			return Integer.parseInt(toParse);
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Returns a list with no duplicate items
	 * @param <T> type
	 * @param list List to remove duplicates from
	 * @return A new list containing no duplicate items
	 */
	public static <T> List<T> removeDuplicates(List<T> list) {
		List<T> newList = new ArrayList<T>();
		for (T element : list) {
			if (!newList.contains(element)) newList.add(element);
		}
		return newList;
	}
	
	/**
	 * Rounds a double up
	 * @param value Value to round up
	 * @return Rounded up value (ex. 4.01 -> 5)
	 */
	public static int roundUpExact(double value) {
		return Math.toIntExact(Math.round(Math.ceil(value)));
	}
}
