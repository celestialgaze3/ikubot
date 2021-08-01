package net.celestialgaze.IkuBot.command.commands.modules.xp.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.celestialgaze.IkuBot.command.PagedCommand;
import net.celestialgaze.IkuBot.command.PagedMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;

public class XpRolesList extends PagedCommand {

	public XpRolesList() {
		super("list", 
			  "List of roles to give", 
			  "");
	}

	@Override
	public MessageEmbed getUpdatedEmbed(Message message, PagedMessage pagedMsg) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(getColor(message));
		
		XpRoles roles = (XpRoles) parent;
		Map<Integer, Role> xpRoles = roles.getXpRoles(message.getGuild());
		List<Entry<Integer, Role>> entries = new ArrayList<Entry<Integer, Role>>(xpRoles.entrySet());
		for (int i = pagedMsg.getStartIndex(); i < pagedMsg.getEndIndex() && i < entries.size(); i++) {
			Entry<Integer, Role> entry = entries.get(i);
			embed.appendDescription("**Level " + entry.getKey() + ":** " + entry.getValue().getAsMention() + "\n");
		}
		if (entries.size() == 0) {
			embed.appendDescription("No roles");
		}
		return embed.build();
	}

}
