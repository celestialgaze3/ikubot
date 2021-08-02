package net.celestialgaze.IkuBot.command.commands.modules.xp.roles;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import net.celestialgaze.IkuBot.command.MasterCommand;
import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class XpRoles extends MasterCommand {

	public static XpRoles instance;
	
	public XpRoles() {
		super("roles", 
			  "Manage roles given by leveling up", 
			  "[subcommand]");
		this.setPermissionsRequired(Permission.MANAGE_ROLES);
		instance = this;
	}

	@Override
	public void init() {
		super.init();
		addSubcommand(new XpRolesAdd());
		addSubcommand(new XpRolesDelete());
		addSubcommand(new XpRolesList());
	}
	
	public void setXpRoles(Guild guild, Map<Integer, Role> xpRoles) {
		Document doc = new Document();
		xpRoles.forEach((level, role) -> {
			doc.append(Integer.toString(level), role.getIdLong());
		});
		module.getSettings(guild).setDocument("xpRoles", doc);
	}
	
	public HashMap<Integer, Role> getXpRoles(Guild guild) {
		Document doc = module.getSettings(guild).getDocument("xpRoles");
		HashMap<Integer, Role> xpRoles = new HashMap<Integer, Role>();
		doc.forEach((level, id) -> {
			if (IkuUtil.isRole(guild, "Level " + level + " XP role", (long) id)) {
				xpRoles.put(Integer.parseInt(level), IkuUtil.getRole(guild, Long.toString((long) id)));
			}
		});
		return xpRoles;
	}
}
