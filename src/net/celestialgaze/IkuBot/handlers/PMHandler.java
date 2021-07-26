package net.celestialgaze.IkuBot.handlers;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PMHandler {

	public static void handle(MessageReceivedEvent event) {
		event.getChannel().sendMessage("Sorry, this bot currently doesn't support DMs!").queue();
	}

}
