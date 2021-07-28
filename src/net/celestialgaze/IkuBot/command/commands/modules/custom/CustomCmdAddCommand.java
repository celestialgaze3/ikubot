package net.celestialgaze.IkuBot.command.commands.modules.custom;

import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.Commands;
import net.celestialgaze.IkuBot.command.module.CustomCmdsModule;
import net.dv8tion.jda.api.entities.Message;

public class CustomCmdAddCommand extends Command {

	public CustomCmdAddCommand() {
		super("add", "Adds a custom command to your server", "\"name\" \"description\" \"text\"", "create");
	}

	@Override
	public void run(String[] args, Message message) {
		CustomCmdsModule module = (CustomCmdsModule) this.module;
		
		String name = getQuoteArg(args, 0);
		String description = getQuoteArg(args, 1);
		String text = getQuoteArg(args, 2);
		
		if (name == null || description == null || text == null) {
			message.getChannel().sendMessage("Not enough arguments").queue();
		} else {
			// Ensure no command overlapping happenes
			if (Commands.getBaseCommands(IkuUtil.getGuild(message)).containsKey(name.toLowerCase())) {
				message.getChannel().sendMessage("You cannot create a command with the name `" + name.toLowerCase() + "` as it already exists").queue();
				return;
			}
			module.addCustomCommand(IkuUtil.getGuild(message), name.toLowerCase(), description, text);
			message.getChannel().sendMessage("Added `" + name.toLowerCase() + "` command").queue();
		}
	}

}
