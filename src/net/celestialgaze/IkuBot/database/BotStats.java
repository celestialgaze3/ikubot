package net.celestialgaze.IkuBot.database;

import org.bson.Document;

public class BotStats extends DatabaseElement {
	int starts = 0;
	int commandsRan = 0;
	int pings = 0;
	int errors = 0;
	public static BotStats instance;
	
	public BotStats() {
		load();
	}
	
	public int getStarts() {
		return starts;
	}
	
	public int getCommandsRan() {
		return commandsRan;
	}
	
	public int getPings() {
		return pings;
	}
	
	public int getErrors() {
		return errors;
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
	
	public void setErrors(int errors) {
		this.errors = errors;
		save(Database.bot, "errors", errors);
	}

	@Override
	public void load() {
		if (loaded) return;
		Document doc = findDocument(Database.bot);
		starts = doc.getInteger("starts", 0);
		commandsRan = doc.getInteger("commandsRan", 0);
		pings = doc.getInteger("pings", 0);
		errors = doc.getInteger("errors", 0);
		loaded = true;
	}

	@Override
	public Document getDefaultDocument() {
		return new Document()
				.append("id", getId())
				.append("starts", starts)
				.append("commandsRan", commandsRan)
				.append("pings", pings)
				.append("errors", errors);
	}

	@Override
	public Object getId() {
		return "stats";
	}
	
	
	
}
