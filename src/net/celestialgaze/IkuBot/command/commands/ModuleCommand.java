package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class ModuleCommand extends Command {

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
	
	@Override
	public void run(String[] args, Message message) {
		EmbedBuilder embed = new EmbedBuilder();
		for (Command c : this.subcommands.values()) {
			embed.appendDescription(this.getName() + " " + c.getName() + "\n");
		}
		message.getChannel().sendMessageEmbeds(embed.build()).queue();
	}

}
