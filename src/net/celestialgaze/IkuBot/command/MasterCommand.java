package net.celestialgaze.IkuBot.command;

import java.util.ArrayList;
import java.util.List;

import net.celestialgaze.IkuBot.IkuUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MasterCommand extends PagedCommand {

	public MasterCommand(String name, String description, String usage, String... aliases) {
		super(name, description, usage, aliases);
	}

	@Override
	public MessageEmbed getUpdatedEmbed(Message message, PagedMessage pagedMsg) {
		EmbedBuilder embed = new EmbedBuilder();
		String prefix = getPrefix(message);
		pagedMsg.setPageSize(7);
		
		List<Command> cmdList = IkuUtil.removeDuplicates(new ArrayList<Command>(subcommands.values()));
		
		// Remove commands that can't be ran
		List<Command> toRemove = new ArrayList<Command>();
		for (Command c : cmdList) {
			if (!c.canRun(message)) toRemove.add(c);
		}
		for (Command c : toRemove) {
			cmdList.remove(c);
		}
		
		pagedMsg.updatePageLimit(cmdList.size());
		for (int i = pagedMsg.getStartIndex(); i < cmdList.size() && i <= pagedMsg.getEndIndex(); i++) {
			Command c = cmdList.get(i);
			embed.appendDescription(c.view(prefix));
		}
		return embed.build();
	}

}
