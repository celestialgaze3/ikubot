package net.celestialgaze.IkuBot.handlers;

import net.celestialgaze.IkuBot.command.CommandInterpreter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ServerMessageHandler {
	
	public static void handle(MessageReceivedEvent event) {
		CommandInterpreter.runCommandFromMsg(event.getMessage(), "!");
	}
	
}
