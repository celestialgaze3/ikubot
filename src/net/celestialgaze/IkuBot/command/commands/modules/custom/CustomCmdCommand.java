package net.celestialgaze.IkuBot.command.commands.modules.custom;

import net.celestialgaze.IkuBot.command.MasterCommand;
import net.dv8tion.jda.api.Permission;

public class CustomCmdCommand extends MasterCommand {

	public CustomCmdCommand() {
		super("customcmd", 
			  "Manage custom commands within your server", 
			  "<subcommand>", 
			  "cc");
		this.setPermission(Permission.MANAGE_SERVER);
	}

	@Override
	public void init() {
		addSubcommand(new CustomCmdAddCommand());
		addSubcommand(new CustomCmdRemoveCommand());
	}

}
