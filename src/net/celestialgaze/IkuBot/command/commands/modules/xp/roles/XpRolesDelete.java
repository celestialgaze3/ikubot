package net.celestialgaze.IkuBot.command.commands.modules.xp.roles;

import java.util.Map;

import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class XpRolesDelete extends Command {

	public XpRolesDelete() {
		super("delete", 
			  "Remove a role from the list of roles to give", 
			  "<role>", 
			  "remove");
	}
	
	@Override
	public void run(String[] args, Message message) {
		if (!this.meetsArgCount(message, args, 1)) return;
		if (!IkuUtil.isInteger(args[0])) {
			message.getChannel().sendMessage("Not a valid level").queue();
			return;
		}
		Guild guild = message.getGuild();
		int level = IkuUtil.getInteger(args[0]);

		XpRoles roles = (XpRoles) parent;
		Map<Integer, Role> xpRoles = roles.getXpRoles(guild);
		if (xpRoles.containsKey(level)) {
			xpRoles.remove(level);
			roles.setXpRoles(guild, xpRoles);
			
			message.getChannel().sendMessage("Successfully removed role for level " + level).queue();
		} else {
			message.getChannel().sendMessage("Unable to remove role for level " + level + " as it does not exist").queue();
		}
	}

}
