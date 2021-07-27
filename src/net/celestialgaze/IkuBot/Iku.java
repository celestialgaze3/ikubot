package net.celestialgaze.IkuBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.SelfUser;

public class Iku {
	
	public static JDA bot;
	public static final String DEFAULT_PREFIX = "i!";
	
	/**
	 * @return The ID of the bot's user
	 */
	public static long getIdLong() {
		return getUser().getIdLong();
	}
	
	/**
	 * @return The name of the bot's user
	 */
	public static String getName() {
		return getUser().getName();
	}
	
	/**
	 * @return The name of the bot's user including the tag
	 */
	public static String getFullUser() {
		return getUser().getAsTag();
	}
	
	/**
	 * @return The bot's SelfUser instance
	 */
	public static SelfUser getUser() {
		return bot.getSelfUser();
	}
	
	/**
	 * Logs to console
	 * @param str String to log
	 */
	public static void log(String str) {
		System.out.println("[" + IkuUtil.getTimestamp() + "] [INFO] " + str);
	}
	
	/**
	 * Errors to console
	 * @param str Error message
	 */
	public static void error(String str) {
		final String ANSI_RED = "\u001B[31m";
		System.out.println(ANSI_RED + "[" + IkuUtil.getTimestamp() + "] [ERROR] " + str);
	}
	
}
