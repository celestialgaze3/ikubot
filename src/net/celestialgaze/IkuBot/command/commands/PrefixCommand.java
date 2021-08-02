package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.database.Server;
import net.celestialgaze.IkuBot.util.Iku;
import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.api.entities.Message;

public class PrefixCommand extends Command {

	public PrefixCommand() {
		super("prefix",
			  "Get or set the prefix of your server",
			  "[prefix]");
		this.setUsableDMs(false);
	}

	@Override
	public void run(String[] args, Message message) {
		String prefix = getPrefix(message);
		if (args.length >= 1) {
			String requestedPrefix = IkuUtil.arrayToString(args, " ").strip();
			
			final int prefixCharLimit = 10;
			if (requestedPrefix.length() > prefixCharLimit) {
				Iku.sendError(message, "Sorry, the limit for prefix length is " + prefixCharLimit);
				return;
			}
			
			Server server = Server.get(message);
			server.setPrefix(requestedPrefix);
			Iku.sendSuccess(message, "This server's prefix is now `" + requestedPrefix + "`");
		} else {
			Iku.send(message, "This server's prefix is `" + prefix + "`");
		}
	}

}
