package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.database.BotStats;
import net.dv8tion.jda.api.entities.Message;

public class PingCommand extends Command {

	public PingCommand() {
		super("ping",
			  "Get the bot's latency",
			  "", "pang", "pung");
	}

	@Override
	public void run(String[] args, Message message) {
		long startMs = System.currentTimeMillis();
		message.getChannel().sendMessage("Ping?").queue(response -> {
			long endMs = System.currentTimeMillis();
			BotStats.instance.setPings(BotStats.instance.getPings() + 1);
			response.editMessage("Pong! Latency was " + (endMs - startMs) + "ms. The bot has been pinged " + BotStats.instance.getPings() + " times.").queue();
		});
	}

}
