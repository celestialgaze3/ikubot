package net.celestialgaze.IkuBot.database;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import net.celestialgaze.IkuBot.Iku;

public class Server extends DatabaseElement {
	private static Map<Long, Server> cache = new HashMap<Long, Server>();
	
	long id;
	String prefix = "!";
	private Server(long id) {
		this.id = id;
		load();
	}
	
	public static Server get(long id) {
		if (cache.containsKey(id)) {
			return cache.get(id);
		} else {
			if (Iku.bot.getGuildById(id) == null) {
				Iku.error("Unable to find guild with id " + id);
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
	
	@Override
	public void load() {
		if (loaded) return;
		Document doc = findDocument(Database.server);
		prefix = doc.getString("prefix");
	}

	@Override
	public Document getDefaultDocument() {
		return new Document()
				.append("id", id)
				.append("prefix", Iku.DEFAULT_PREFIX);
	}

	@Override
	public Object getId() {
		return id;
	}

}
