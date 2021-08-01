package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.dv8tion.jda.api.entities.Message;

public class ModuleDisableCommand extends Command {

	public ModuleDisableCommand() {
		super("disable",
			  "Disable a module",
			  "<module>");
		this.setUsableDMs(false);
	}

	@Override
	public void run(String[] args, Message message) {
		if (args.length >= 1) {
			String moduleName = IkuUtil.arrayToString(args, " ");
			CommandModule module = CommandModules.list.get(moduleName.toLowerCase());
			if (module != null) {
				module.setEnabled(IkuUtil.getGuild(message), false);
				message.getChannel().sendMessage("Disabled module " + module.getName() + " successfully").queue();
			} else {
				Iku.sendError(message, "A module by the name of " + moduleName + " was not found");
			}
		}
	}

}
