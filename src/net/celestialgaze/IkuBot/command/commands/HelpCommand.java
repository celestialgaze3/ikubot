package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class HelpCommand extends Command {

	public HelpCommand() {
		super("help");
	}

	@Override
	public void run(String[] args, Message message) {
		EmbedBuilder embed = new EmbedBuilder();
		
		// Add module commands in individual fields
		for (CommandModule module : CommandModules.list) {
			String fieldDesc = "";
			for (Command c : module.getCommands().values()) {
				fieldDesc += c.getName() + "\n";
			}
			embed.addField(module.getName(), fieldDesc, false);
		}
		
		// Base commands should show up at the top
		for (Command c : Command.baseCommands.values()) {
			embed.appendDescription(c.getName() + "\n");
		}
		
		message.getChannel().sendMessageEmbeds(embed.build()).queue();
	}

}
