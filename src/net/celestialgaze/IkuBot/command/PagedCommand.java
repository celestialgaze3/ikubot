package net.celestialgaze.IkuBot.command;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public abstract class PagedCommand extends Command {
	public PagedCommand(String name, String description, String usage, String... aliases) {
		super(name, description, usage, aliases);
	}

	@Override
	public void run(String[] args, Message message) {
		int page = 1;
		if (args.length >= 1) {
			String toParse = args[0];
			if (IkuUtil.isInteger(toParse)) {
				page = IkuUtil.getInteger(toParse);
			}
		}
		PagedMessage pagedMsg = new PagedMessage() {

			@Override
			public MessageEmbed getUpdated() {
				MessageEmbed embed = getUpdatedEmbed(getCmdMsg(), this);
				if (embed.isEmpty() || !embed.isSendable()) {
					Iku.error("Failed to update embed!");
					return null;
				}
				return embed;
			}
			
		};
		pagedMsg.setCmdMsg(message);
		pagedMsg.setPage(page);
		MessageEmbed embed = getUpdatedEmbed(message, pagedMsg);
		
		if (!embed.isEmpty() && embed.isSendable()) {
			message.getChannel().sendMessageEmbeds(getUpdatedEmbed(message, pagedMsg)).queue(response -> {
				pagedMsg.setMessage(response);
				pagedMsg.updateReactions();
			});
		}
	}
	
	public abstract MessageEmbed getUpdatedEmbed(Message message, PagedMessage pagedMsg); 
}
