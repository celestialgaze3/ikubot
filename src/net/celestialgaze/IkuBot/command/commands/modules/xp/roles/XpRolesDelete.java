package net.celestialgaze.IkuBot.command.commands.modules.xp.roles;

import net.celestialgaze.IkuBot.command.Command;
import net.dv8tion.jda.api.entities.Message;

public class XpRolesDelete extends Command {

	public XpRolesDelete() {
		super("delete", "Remove a role from the list of roles to give", "<role>", "remove");
	}

	@Override
	public void run(String[] args, Message message) {
		
	}

}
