package net.celestialgaze.IkuBot.listeners;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.command.PagedMessage;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MiscListener extends ListenerAdapter {

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		// Paged messages
		PagedMessage pagedMsg = PagedMessage.list.get(event.getMessageIdLong());
		if (pagedMsg != null) {
			try {
				pagedMsg.onReacted(event);
				
				// Remove user's reaction
				if (event.getUserIdLong() != Iku.getIdLong()) 
					Iku.bot.retrieveUserById(event.getUserId()).queue(user -> {
						event.getReaction().removeReaction(user).queue();
					});
			} catch (Exception e) {
				Iku.error("Error in paged message " + event.getMessageIdLong());
				e.printStackTrace();
			}
		}
	}
}
