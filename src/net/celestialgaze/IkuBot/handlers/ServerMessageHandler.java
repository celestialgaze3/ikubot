package net.celestialgaze.IkuBot.handlers;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.command.CommandInterpreter;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.celestialgaze.IkuBot.database.BotStats;
import net.celestialgaze.IkuBot.database.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ServerMessageHandler {
	
	public static void handle(MessageReceivedEvent event) {
		try {
			// Run any commands
			CommandInterpreter.runCommandFromMsg(event.getMessage(), Server.get(event.getGuild().getIdLong()).getPrefix());
			
			// Run any enabled modules onMessage method
			for (CommandModule module : CommandModules.list.values()) {
				if (module.isEnabled(event.getGuild())) module.onMessage(event);
			}
		} catch (Exception e) {
			event.getMessage().getChannel().sendMessage("Looks like there was an error...").queue();
			Iku.error("Error when received " + event.getMessage().getContentRaw() + ".");
			BotStats.instance.setErrors(BotStats.instance.getErrors() + 1);
			e.printStackTrace();
		}
	}
	
}
