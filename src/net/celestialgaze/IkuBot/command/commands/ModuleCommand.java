package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.command.MasterCommand;

public class ModuleCommand extends MasterCommand {

	public ModuleCommand() {
		super("module",
			  "Commands to manage modules",
			  "<subcommand>");
	}

	public void init() {
		addSubcommand(new ModuleListCommand());
		addSubcommand(new ModuleEnableCommand());
		addSubcommand(new ModuleDisableCommand());
	}

}
