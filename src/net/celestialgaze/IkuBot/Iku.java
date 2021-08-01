package net.celestialgaze.IkuBot;

import java.awt.Color;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.SelfUser;

public class Iku {
	
	public static JDA bot;
	public static final String DEFAULT_PREFIX = "i!";
	public static final Color EMBED_COLOR = Color.decode("#fff6cc");
	public static final String EMBED_COLOR_HEX = IkuUtil.colorToHex(EMBED_COLOR);
	
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
	 * Gets the member instance for the bot for the specified guild
	 * @param guild The guild to get the member for
	 * @return The bot's member instance
	 */
	public static Member getMember(Guild guild) {
		return guild.getMemberById(getUser().getIdLong());
	}
	
	/**
	 * @param guild The guild
	 * @param role The role
	 * @return Whether or not the bot can manage a role
	 */
	public static boolean canManageRole(Guild guild, Role role) {
		boolean canManage = false;
		for (Role r : Iku.getMember(guild).getRoles()) {
			if (r.canInteract(role)) canManage = true;
		}
		return canManage;
	}
	
	/**
	 * Logs to console
	 * @param str String to log
	 */
	public static void log(String str) {
		System.out.println("[" + IkuUtil.getTimestamp() + "] [INFO] " + str);
		if (str.contains("727")) System.out.println("WYSI");
	}
	
	/**
	 * Errors to console
	 * @param str Error message
	 */
	public static void error(String str) {
		System.out.println("[" + IkuUtil.getTimestamp() + "] [ERROR] " + str);
	}
	
	/**
	 * Replies to a message with an error
	 */
	public static void sendError(Message message, String str) {
		message.getChannel().sendMessage(str.substring(0, str.length() <= 2000 ? str.length() : 2000)).queue(success -> {}, failure -> {});
	}
	
	/**
	 * Send method to avoid errors
	 */
	public static void send(Message message, String str) {
		message.getChannel().sendMessage(str.substring(0, str.length() <= 2000 ? str.length() : 2000)).queue(success -> {}, failure -> {});
	}
	
	/**
	 * Replies to a message indicating success
	 */
	public static void sendSuccess(Message message, String str) {
		message.getChannel().sendMessage(str.substring(0, str.length() <= 2000 ? str.length() : 2000)).queue(success -> {}, failure -> {});
	}
	
	/**
	 * Notifies the owner/moderators of a server in some way
	 * @param guild Guild to notify
	 * @param message Text
	 */
	public static void notify(Guild guild, String message) {
		
	}
}
