package net.celestialgaze.IkuBot;

import net.celestialgaze.IkuBot.command.Commands;
import net.celestialgaze.IkuBot.database.BotInfo;
import net.celestialgaze.IkuBot.database.Database;
import net.celestialgaze.IkuBot.listeners.MessageListener;
import net.celestialgaze.IkuBot.listeners.MiscListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Main {
	public static JDA bot;
	public static void main(String[] args) {
		Commands.init();
		Database.init();
		try {
			startBot();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void startBot() throws Exception {
		// Start our JDA instance and block until ready
		bot = JDABuilder.createDefault(BotInfo.instance.getToken()).build();
		bot.awaitReady();
		Iku.bot = bot;
		
		Iku.log("Logged in as " + Iku.getFullUser());
		
		bot.addEventListener(new MessageListener());
		bot.addEventListener(new MiscListener());
		
		
		bot.getPresence().setActivity(Activity.watching("for i!help || @Iku help"));
	}
	
	

}
