package net.celestialgaze.IkuBot.command.commands.modules.custom;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.module.CustomCmdsModule;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

public class CustomCmdRemoveCommand extends Command {

	public CustomCmdRemoveCommand() {
		super("remove", "Removes a custom command from your server", "<name>", "delete");
	}

	@Override
	public void run(String[] args, Message message) {
		CustomCmdsModule module = (CustomCmdsModule) this.module;
		Guild guild = IkuUtil.getGuild(message);
		
		String name = getFullStringArg(args);
		
		if (name == null) {
			Iku.sendError(message, "Not enough arguments");
		} else {
			if (!module.isCustomCommand(guild, name.toLowerCase())) {
				Iku.sendError(message, "You cannot remove the command `" + name.toLowerCase() + "` as it does not exist");
				return;
			}
			module.removeCustomCommand(guild, name.toLowerCase());
			Iku.sendSuccess(message, "Removed `" + name.toLowerCase() + "` command");
		}
	}

}
