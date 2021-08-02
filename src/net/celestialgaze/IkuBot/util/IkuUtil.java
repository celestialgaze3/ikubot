package net.celestialgaze.IkuBot.util;

import java.awt.Color;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd@HH:mm:ss.SSS");
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
	 * @param toParse String to parse
	 * @return Whether or not it is an integer
	 */
	public static boolean isLong(String toParse) {
		try {
			Long.parseLong(toParse);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * @param toParse String to parse
	 * @return The integer value, or 0 if it is not an integer.
	 */
	public static long getLong(String toParse) {
		try {
			return Long.parseLong(toParse);
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
	
	/**
	 * Rounds a double down
	 * @param value Value to round down
	 * @return Rounded down value (ex. 4.99 -> 4)
	 */
	public static int roundDownExact(double value) {
		return Math.toIntExact(Math.round(Math.floor(value)));
	}
	
	
	/**
	 * @param message Message to get guild from
	 * @return The guild, or null if the message was not sent in a guild
	 */
	public static Guild getGuild(Message message) {
		return (message.isFromGuild() ? message.getGuild() : null);
	}
	
	/**
	 * Tries to get role by name, mention, or ID
	 * @param guild Guild to get role from
	 * @param toParse String to get role from
	 * @return The role, or null if none was found
	 */
	public static Role getRole(Guild guild, String toParse) {
		// Try to get role by name
		List<Role> roles = guild.getRolesByName(toParse, true);
		if (roles.size() > 0) {
			return roles.get(0);
		}
		
		// Try to get role by ID (or mention)
		if (toParse.startsWith("<@&")) toParse = toParse.substring(3, toParse.length() - 1); // Remove mention stuff
		
		if (isLong(toParse)) {
			Role role = guild.getRoleById(getLong(toParse));
			if (role != null) return role;
		}
		
		return null;
	}
	
	/**
	 * Returns if a given ID maps to a role in a guild, and notifies if it does not
	 * @param guild Guild to check
	 * @param settingName The name of the setting to include in the notification
	 * @return Whether or not the ID is a valid role
	 */
	public static boolean isRole(Guild guild, String settingName, long id) {
		Role role = IkuUtil.getRole(guild, Long.toString(id));
		if (role == null && id != 0) {
			Iku.notify(guild, "I couldn't find the role with role ID " + role + ", so I've reset your " + settingName + " setting.");
			return false;
		}
		return true;
	}
	
	/**
	 * Tries to get a member by name, mention, or ID
	 * @param guild Guild to get member from
	 * @param toParse String to get member from
	 * @return The member, or null if none was found
	 */
	public static Member getMember(Guild guild, String toParse) {
		Member member = null;
		
		if (toParse.startsWith("<@!")) toParse = toParse.substring(3, toParse.length() - 1); // Remove mention stuff
		// Try to get member by ID
		if (isLong(toParse)) {
			member = guild.getMemberById(toParse);
			if (member != null) return member;
		}
		
		// Try to get member by name
		List<Member> members = guild.getMembersByName(toParse, true);
		if (members.size() > 0) {
			return members.get(0);
		}

		// Try to get member by name#0000
		try {
			member = guild.getMemberByTag(toParse);
			if (member != null) return member;
		} catch (Exception e) {
			// cry about it
		}
		
		// Try to get member by nickname
		members = guild.getMembersByNickname(toParse, true);
		if (members.size() > 0) {
			return members.get(0);
		}
		return null;
	}
	
	/**
	 * Converts a color's RGB to hex in the format #000000
	 * @param color The color to convert
	 * @return The hex string
	 */
	public static String colorToHex(Color color) {
		return "#" + Integer.toHexString(color.getRGB()).substring(2);
	}
}
