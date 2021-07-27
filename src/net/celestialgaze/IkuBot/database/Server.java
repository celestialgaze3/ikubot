package net.celestialgaze.IkuBot.database;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import net.celestialgaze.IkuBot.Iku;

public class Server extends DatabaseElement {
	private static Map<Long, Server> cache = new HashMap<Long, Server>();
	
	long id;
	String prefix = "!";
	Document settings = new Document();
	private Server(long id) {
		this.id = id;
		load();
	}
	
	public static Server get(long id) {
		if (cache.containsKey(id)) {
			return cache.get(id);
		} else {
			if (Iku.bot.getGuildById(id) == null) {
				Iku.error("Unable to find guild with id " + id + " when getting server");
				return null; 
			}
			Server server = new Server(id);
			Iku.log("Loaded server " + id);
			cache.put(id, server);
			return server;
		}
	}
	
	public String getPrefix() {
		return (prefix != null ? prefix : Iku.DEFAULT_PREFIX);
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		save(Database.server, "prefix", prefix);
	}
	
	public Document getSettings() {
		return settings;
	}
	
	public Document getModuleSettings(String moduleName) {
		return settings.get(moduleName, new Document());
	}
	
	public void setModuleSettings(String moduleName, Document newSettings) {
		settings.put(moduleName, newSettings);
		updateSettings();
	}
	
	private void updateSettings() {
		save(Database.server, "settings", settings);
	}
	
	@Override
	public void load() {
		if (loaded) return;
		Document doc = findDocument(Database.server);
		prefix = doc.getString("prefix");
		settings = doc.get("settings", new Document());
	}

	@Override
	public Document getDefaultDocument() {
		return new Document()
				.append("id", id)
				.append("prefix", Iku.DEFAULT_PREFIX)
				.append("settings", settings);
	}

	@Override
	public Object getId() {
		return id;
	}

}
