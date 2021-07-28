package net.celestialgaze.IkuBot.handlers;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.command.CommandInterpreter;
import net.celestialgaze.IkuBot.database.BotStats;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PMHandler {

	public static void handle(MessageReceivedEvent event) {
		Message message = event.getMessage();
		
		// Run any commands
		if (CommandInterpreter.runCommandFromMsg(message, Iku.DEFAULT_PREFIX)) { // Command successfully ran
			BotStats.instance.setCommandsRan(BotStats.instance.getCommandsRan() + 1);
		}

	}

}
