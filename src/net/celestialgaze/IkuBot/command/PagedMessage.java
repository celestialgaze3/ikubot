package net.celestialgaze.IkuBot.command;

import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.IkuBot.util.Iku;
import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public abstract class PagedMessage {
	
	public static Map<Long, PagedMessage> list = new HashMap<Long, PagedMessage>();
	public static final String NEXT_PAGE = "▶️";
	public static final String PREV_PAGE = "◀️";
	
	Message message; // Message to update
	Message cmdMsg; // Message that created this paged message
	
	int page = 1;
	int pageLimit = -1;
	int pageSize = 10;
	
	public PagedMessage(Message message) {
		this.message = message;
		list.put(message.getIdLong(), this);
	}
	
	public PagedMessage() {}

	public void update() {
		if (message != null) {
			message.editMessageEmbeds(getUpdated()).queue();
			updateReactions();
		}
	}
	
	public void updateReactions() {
		if (shouldHavePrev() && !hasPrev()) {
			if (hasNext()) {
				// Remove next page reaction before adding prev page, then add next page reaction back if necessary
				message.removeReaction(NEXT_PAGE).queue(nextPageReaction -> {
				message.addReaction(PREV_PAGE).queue(prevPageReaction -> { 
						if (shouldHaveNext()) message.addReaction(NEXT_PAGE).queue(); 
					}); 
				});
			} else {
				message.addReaction(PREV_PAGE).queue();
			}
		} else if (!shouldHavePrev() && hasPrev()) {
			message.removeReaction(PREV_PAGE).queue();
		}
					
		if (shouldHaveNext() && !hasNext()) {
			message.addReaction(NEXT_PAGE).queue();
		} else if (!shouldHaveNext() && hasNext()) {
			message.removeReaction(NEXT_PAGE).queue();
		}
	}
	
	public void onReacted(MessageReactionAddEvent event) {
		event.getChannel().retrieveMessageById(event.getMessageId()).queue(message -> {
			this.setMessage(message);
			updateReactions();
			if (event.getUserIdLong() == Iku.getIdLong()) return; // Ignore reactions from the bot
			String emoji = event.getReactionEmote().getEmoji();
			if (emoji.contentEquals(NEXT_PAGE)) {
				nextPage();
				update();
			} else if (emoji.contentEquals(PREV_PAGE)) {
				prevPage();
				update();
			}
		});
	}
	
	public boolean hasPrev() {
		return message.getReactionByUnicode(PREV_PAGE) != null;
	}
	
	public boolean hasNext() {
		return message.getReactionByUnicode(NEXT_PAGE) != null;
	}
	
	public boolean shouldHavePrev() {
		return page > 1;
	}
	
	public boolean shouldHaveNext() {
		return page < pageLimit;
	}
	
	public abstract MessageEmbed getUpdated();
	
	public Message getMessage() {
		return message;
	}
	
	public Message getCmdMsg() {
		return cmdMsg;
	}
	
	public void setCmdMsg(Message cmdMsg) {
		this.cmdMsg = cmdMsg;
	}
	
	public void setMessage(Message message) {
		this.message = message;
		list.put(message.getIdLong(), this);
	}
	
	public int getPage() {
		return page;
	}
	
	public void nextPage() {
		setPage(getPage() + 1);
	}
	
	public void prevPage() {
		setPage(getPage() - 1);
	}
	
	public void setPage(int page) {
		this.page = page;
		if (pageLimit != -1 && page > pageLimit) this.page = pageLimit;
		if (page < 1) this.page = 1;
	}
	
	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}
	
	public int getPageLimit() {
		return pageLimit;
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartIndex() { 
		return page * pageSize - pageSize;
	}
	
	public int getEndIndex() { 
		return getStartIndex() + pageSize - 1;
	}
	
	public void updatePageLimit(int elementAmount) {
		setPageLimit(IkuUtil.roundUpExact((elementAmount + 0d) / (getPageSize() + 0d)));
	}
}