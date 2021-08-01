package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;

public class SayCommand extends Command {

	public SayCommand() {
		super("say", "Make the bot say something", "<message>");
		this.setAdmin(true);
	}

	@Override
	public void run(String[] args, Message message) {
		if (!this.meetsArgCount(message, args, 1)) return;
		Iku.send(message, IkuUtil.arrayToString(args, " "));
		if (!message.getChannelType().equals(ChannelType.PRIVATE) &&
				Iku.getMember(message.getGuild()).hasPermission(Permission.MESSAGE_MANAGE)) 
			message.delete().queue(success -> {}, failure -> {});
	}

}
