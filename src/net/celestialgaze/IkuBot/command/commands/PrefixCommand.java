package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.database.Server;
import net.dv8tion.jda.api.entities.Message;

public class PrefixCommand extends Command {

	public PrefixCommand() {
		super("prefix",
			  "Get or set the prefix of your server",
			  "[prefix]");
	}

	@Override
	public void run(String[] args, Message message) {
		if (args.length >= 1) {
			String requestedPrefix = IkuUtil.arrayToString(args, " ");
			
			final int prefixCharLimit = 100;
			if (requestedPrefix.length() > prefixCharLimit) {
				message.getChannel().sendMessage("Sorry, the limit for prefix length is " + prefixCharLimit).queue();
				return;
			}
			
			Server server = Server.get(message.getGuild().getIdLong());
			server.setPrefix(requestedPrefix);
			message.getChannel().sendMessage("This server's prefix is now `" + Server.get(message.getGuild().getIdLong()).getPrefix() + "`").queue();
		} else {
			message.getChannel().sendMessage("This server's prefix is `" + Server.get(message.getGuild().getIdLong()).getPrefix() + "`").queue();
		}
	}

}
