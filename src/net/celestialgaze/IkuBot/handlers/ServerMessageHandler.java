package net.celestialgaze.IkuBot.handlers;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.command.CommandInterpreter;
import net.celestialgaze.IkuBot.command.Commands;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.celestialgaze.IkuBot.database.BotStats;
import net.celestialgaze.IkuBot.database.Server;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ServerMessageHandler {
	
	public static void handle(MessageReceivedEvent event) {
		try {
			Message message = event.getMessage();
			Guild guild = event.getGuild();
			
			// Run any commands
			if (CommandInterpreter.runCommandFromMsg(message, Server.get(message).getPrefix())) { // Command successfully ran
				BotStats.instance.setCommandsRan(BotStats.instance.getCommandsRan() + 1);
			}
			
			// Run any enabled modules onMessage method
			for (CommandModule module : CommandModules.list.values()) {
				if (module.isEnabled(guild)) module.onMessage(event);
			}
			
			// If the message is just mentioning the bot, run the help command
			String mention = "<@!" + Iku.getUser().getId() + ">";
			if (message.getContentRaw().contentEquals(mention)) {
				Commands.getBaseCommands(guild).get("help").run(new String[0], message);
			}
		} catch (Exception e) {
			event.getMessage().getChannel().sendMessage("Looks like there was an error...").queue();
			Iku.error("Error when received " + event.getMessage().getContentRaw() + ".");
			BotStats.instance.setErrors(BotStats.instance.getErrors() + 1);
			e.printStackTrace();
		}
	}
	
}
