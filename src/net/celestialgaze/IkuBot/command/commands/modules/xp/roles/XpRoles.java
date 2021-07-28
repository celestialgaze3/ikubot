package net.celestialgaze.IkuBot.command.commands.modules.xp.roles;

import net.celestialgaze.IkuBot.command.MasterCommand;

public class XpRoles extends MasterCommand {

	public XpRoles() {
		super("roles", "Manage roles given by leveling up", "<subcommand>");
	}

	@Override
	public void init() {
		addSubcommand(new XpRolesAdd());
	}
}
