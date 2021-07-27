package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.dv8tion.jda.api.entities.Message;

public class ModuleEnableCommand extends Command {

	public ModuleEnableCommand() {
		super("enable",
			  "Enable a module",
			  "<module>");
	}

	@Override
	public void run(String[] args, Message message) {
		if (args.length >= 1) {
			String moduleName = IkuUtil.arrayToString(args, " ");
			CommandModule module = CommandModules.list.get(moduleName.toLowerCase());
			if (module != null) {
				module.setEnabled(message.getGuild(), true);
				message.getChannel().sendMessage("Enabled module " + module.getName() + " successfully").queue();
			} else {
				message.getChannel().sendMessage("A module by the name of " + moduleName + " was not found").queue();
			}
		}
	}

}
