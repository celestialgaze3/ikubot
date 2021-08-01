package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.PagedCommand;
import net.celestialgaze.IkuBot.command.PagedMessage;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ModuleListCommand extends PagedCommand {

	public ModuleListCommand() {
		super("list",
			  "Get a list of all modules",
			  "");
	}

	@Override
	public MessageEmbed getUpdatedEmbed(Message message, PagedMessage pagedMsg) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(getColor(message));
		for (CommandModule module : CommandModules.list.values()) {
			embed.appendDescription(module.getName() + ": " + (module.isEnabled(IkuUtil.getGuild(message)) ? "Enabled" : "Disabled") + "\n");
		}
		return embed.build();
	}

}
