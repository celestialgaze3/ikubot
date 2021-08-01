package net.celestialgaze.IkuBot.handlers;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.command.CommandInterpreter;
import net.celestialgaze.IkuBot.command.Commands;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.celestialgaze.IkuBot.database.BotStats;
import net.celestialgaze.IkuBot.database.Server;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ServerMessageHandler {
	
	public static void handle(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Guild guild = event.getGuild();

		if (!Iku.getMember(guild).hasPermission(Permission.MESSAGE_WRITE)) return;
			
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
		if (message.getContentRaw().equalsIgnoreCase(mention)) {
			Commands.getBaseCommands(guild).get("help").run(new String[0], message);
		}		
	}
	
}
