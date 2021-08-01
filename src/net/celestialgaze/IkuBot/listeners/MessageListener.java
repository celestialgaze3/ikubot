package net.celestialgaze.IkuBot.listeners;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.database.BotStats;
import net.celestialgaze.IkuBot.handlers.PMHandler;
import net.celestialgaze.IkuBot.handlers.ServerMessageHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		// Ignore if message is from the bot itself
		if (event.getAuthor().getIdLong() == Iku.getIdLong()) return;
		
		try {
			switch (event.getChannelType()) {
				case PRIVATE:
					PMHandler.handle(event);
					break;
				case TEXT:
					ServerMessageHandler.handle(event);
					break;
				default:
					Iku.log("Recieved message from unrecognized channel type " + event.getChannelType().toString() + ", ignoring");
					break;
			}
		} catch (Exception e) {
			Iku.sendError(event.getMessage(), "Looks like there was an error... `"
					+ e.getMessage() + "`");
			Iku.error("Error when received " + event.getMessage().getContentRaw() + ".");
			BotStats.instance.setErrors(BotStats.instance.getErrors() + 1);
			e.printStackTrace();
		}	
    }
}
