package net.celestialgaze.IkuBot.database;

import org.bson.Document;

public class BotStats extends DatabaseElement {
	int starts = 0;
	int commandsRan = 0;
	int pings = 0;
	public static BotStats instance;
	
	public BotStats() {
		load();
	}
	
	public int getStarts() {
		load();
		return starts;
	}
	
	public int getCommandsRan() {
		load();
		return commandsRan;
	}
	
	public int getPings() {
		load();
		return pings;
	}

	public void setStarts(int starts) {
		this.starts = starts;
		save(Database.bot, "starts", starts);
	}

	public void setCommandsRan(int commandsRan) {
		this.commandsRan = commandsRan;
		save(Database.bot, "commandsRan", commandsRan);
	}

	public void setPings(int pings) {
		this.pings = pings;
		save(Database.bot, "pings", pings);
	}

	@Override
	public void load() {
		if (loaded) return;
		Document doc = findDocument(Database.bot);
		starts = doc.getInteger("starts", 0);
		commandsRan = doc.getInteger("commandsRan", 0);
		pings = doc.getInteger("pings", 0);
		loaded = true;
	}

	@Override
	public Document getDefaultDocument() {
		return new Document()
				.append("id", getId())
				.append("starts", starts)
				.append("commandsRan", commandsRan)
				.append("pings", pings);
	}

	@Override
	public Object getId() {
		return "stats";
	}
	
	
	
}
