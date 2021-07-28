package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class ModuleListCommand extends Command {

	public ModuleListCommand() {
		super("list",
			  "Get a list of all modules",
			  "");
	}

	@Override
	public void run(String[] args, Message message) {
		EmbedBuilder embed = new EmbedBuilder();
		for (CommandModule module : CommandModules.list.values()) {
			embed.appendDescription(module.getName() + ": " + (module.isEnabled(IkuUtil.getGuild(message)) ? "Enabled" : "Disabled") + "\n");
		}
		message.getChannel().sendMessageEmbeds(embed.build()).queue();
	}

}
