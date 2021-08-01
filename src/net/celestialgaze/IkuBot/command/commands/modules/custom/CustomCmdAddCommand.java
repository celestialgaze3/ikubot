package net.celestialgaze.IkuBot.command.commands.modules.custom;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.Commands;
import net.celestialgaze.IkuBot.command.module.CustomCmdsModule;
import net.dv8tion.jda.api.entities.Message;

public class CustomCmdAddCommand extends Command {

	public CustomCmdAddCommand() {
		super("add", "Adds a custom command to your server", "<name> \"description\" \"text\"", "create");
	}

	@Override
	public void run(String[] args, Message message) {
		if (!meetsArgCount(message, args, 2)) return;
		CustomCmdsModule module = (CustomCmdsModule) this.module;
		
		String name = args[0];
		String description = getQuoteArg(args, 0);
		String text = getQuoteArg(args, 1);
		
		if (name == null || description == null || text == null) {
			Iku.sendError(message, "Not enough arguments");
		} else {
			// Ensure no command overlapping happenes
			if (Commands.getBaseCommands(IkuUtil.getGuild(message)).containsKey(name.toLowerCase())) {
				Iku.sendError(message, "You cannot create a command with the name `" + name.toLowerCase() + "` as it already exists");
				return;
			}
			module.addCustomCommand(IkuUtil.getGuild(message), name.toLowerCase(), description, text);
			Iku.sendSuccess(message, "Added `" + name.toLowerCase() + "` command");
		}
	}

}
