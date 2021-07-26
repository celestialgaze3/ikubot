package net.celestialgaze.IkuBot;

import net.celestialgaze.IkuBot.command.Commands;
import net.celestialgaze.IkuBot.listeners.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
	public static JDA bot;
	public static void main(String[] args) {
		Commands.init();
		try {
			startBot();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void startBot() throws Exception {
		// Start our JDA instance and block until ready
		bot = JDABuilder.createDefault("ODY5MDMyNTMwNjQzMzk4NzE2.YP4TnQ.beyZLxrZN_aRgH3-glG9yyz7v4Y").build();
		bot.awaitReady();
		Iku.log("Logged in as " + Iku.getFullUser());
		bot.addEventListener(new MessageListener());
	}
	
	

}
