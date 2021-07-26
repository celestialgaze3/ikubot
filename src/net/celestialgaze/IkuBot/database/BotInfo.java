package net.celestialgaze.IkuBot.database;

import org.bson.Document;

public class BotInfo extends DatabaseElement {

	public static BotInfo instance;
	
	String token;
	public void BotInfo() {
		load();
	}
	
	@Override
	protected void load() {
		if (loaded) return;
		Document doc = findDocument(Database.bot);
		token = doc.getString("token");
	}

	public String getToken() {
		load();
		return token;
	}

	@Override
	protected Object getId() {
		return "info";
	}

	@Override
	protected Document getDefaultDocument() {
		return new Document()
				.append("id", getId())
				.append("token", "default");
	}

}
