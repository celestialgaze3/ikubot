package net.celestialgaze.IkuBot.database;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.dv8tion.jda.api.entities.Message;

public class Server extends DatabaseElement {
	private static Map<Long, Server> cache = new HashMap<Long, Server>();
	
	long id;
	String prefix = Iku.DEFAULT_PREFIX;
	Color color = Iku.EMBED_COLOR;
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
	
	public static Server get(Message message) {
		if (!message.isFromGuild()) return null;
		long id = message.getGuild().getIdLong();
		return Server.get(id);
	}
	
	public String getPrefix() {
		return (prefix != null ? prefix : Iku.DEFAULT_PREFIX);
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
		save(Database.server, "prefix", prefix);
	}
	
	public Color getColor() {
		return color != null ? color : Iku.EMBED_COLOR;
	}
	
	public void setColor(Color color) {
		this.color = color;
		save(Database.server, "color", IkuUtil.colorToHex(color));
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
		Document def = getDefaultDocument();
		prefix = (String) doc.get("string", def.getString("prefix"));
		color = Color.decode(doc.get("color", def.getString("color")));
		settings = doc.get("settings", new Document());
	}

	@Override
	public Document getDefaultDocument() {
		return new Document()
				.append("id", id)
				.append("prefix", Iku.DEFAULT_PREFIX)
				.append("color", IkuUtil.colorToHex(Iku.EMBED_COLOR))
				.append("settings", settings);
	}

	@Override
	public Object getId() {
		return id;
	}

}
