package net.celestialgaze.IkuBot.command.commands.modules.xp.roles;

import java.util.Map;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.module.XpModule;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class XpRolesAdd extends Command {

	public XpRolesAdd() {
		super("add", 
			  "Adds a role to the list of roles to give", 
			  "<level> <role>");
	}

	@Override
	public void run(String[] args, Message message) {
		if (!this.meetsArgCount(message, args, 2)) return;
		if (!IkuUtil.isInteger(args[0])) {
			Iku.sendError(message, "Not a valid level");
			return;
		}
		Guild guild = message.getGuild();
		int level = IkuUtil.getInteger(args[0]);
		if (level < 1) level = 1;
		if (level > XpModule.MAX_LEVEL) level = XpModule.MAX_LEVEL;
		
		Role role = IkuUtil.getRole(guild, IkuUtil.arrayToString(args, " ", 1, args.length - 1));
		if (role != null) {
			XpRoles roles = (XpRoles) parent;
			Map<Integer, Role> xpRoles = roles.getXpRoles(guild);
			
			if (xpRoles.containsValue(role)) {
				Iku.sendError(message, "This role is already assigned to a different level.");
				return;
			}
			
			if (!Iku.canManageRole(guild, role)) {
				Iku.sendError(message, "I don't have permissions to manage that role. " + 
						"Please ensure that my role is above this role in your server's roles list.");
				return;
			}
			xpRoles.put(level, role);
			roles.setXpRoles(guild, xpRoles);
			Iku.sendSuccess(message, "Users will now gain the **" + role.getName() + "** role when they reach level " + level);
		} else {
			Iku.sendError(message, "Not a valid role");
		}
	}

}
