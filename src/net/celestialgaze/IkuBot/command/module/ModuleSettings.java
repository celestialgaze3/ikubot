package net.celestialgaze.IkuBot.command.module;

import org.bson.Document;

import net.celestialgaze.IkuBot.database.Server;
import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class ModuleSettings {
	
	Guild guild;
	CommandModule module;
	Server server;
	public ModuleSettings(Guild guild, CommandModule module) {
		this.guild = guild;
		this.module = module;
		this.server = Server.get(guild.getIdLong());
	}
	
	public Document getDocument(String settingName) {
		if (!module.isSetting(settingName, Type.DOCUMENT)) return null;
		return getSetting(settingName, new Document());
	}
	
	public void setDocument(String settingName, Document document) {
		if (!module.isSetting(settingName, Type.DOCUMENT)) return;
		if (document.isEmpty()) {
			removeSetting("xpRoles");
		} else {
			setSetting(settingName, document);
		}
	}
	
	public boolean getBoolean(String settingName, boolean defaultValue) {
		if (!module.isSetting(settingName, Type.BOOLEAN)) return false;
		return getSetting(settingName, defaultValue);
	}
	
	public void setBoolean(String settingName, boolean value) {
		if (!module.isSetting(settingName, Type.BOOLEAN)) return;
		setSetting(settingName, value);
	}
	
	public String getString(String settingName, String defaultValue) {
		if (!module.isSetting(settingName, Type.STRING)) return null;
		return getSetting(settingName, defaultValue);
	}
	
	public void setString(String settingName, String value) {
		if (!module.isSetting(settingName, Type.STRING)) return;
		setSetting(settingName, value);
	}
	
	public Role getRole(String settingName) {
		if (!module.isSetting(settingName, Type.ROLE)) return null;
		long id = getSetting(settingName, (long) 0);
		if (!IkuUtil.isRole(guild, settingName, id)) removeSetting(settingName);
		return guild.getRoleById(id);
	}
	
	public void setRole(String settingName, Role role) {
		if (!module.isSetting(settingName, Type.ROLE)) return;
		setSetting(settingName, role.getIdLong());
	}
	
	/**
	 * Gets a setting for a module
	 * @param <T> type
	 * @param settingName Name of the setting
	 * @param def Default value for the setting
	 * @return The setting's value, or default if none was found.
	 */
	private <T> T getSetting(String settingName, T def) {
		return server.getModuleSettings(module.getInternalName()).get(settingName, def);
	}
	
	/**
	 * Sets a setting for a module
	 * @param settingName Name of the setting
	 * @param value Value to set for the setting
	 */
	private void setSetting(String settingName, Object value) {
		Document settings = server.getModuleSettings(module.getInternalName());
		settings.put(settingName, value);
		server.setModuleSettings(module.getInternalName(), settings);
	}
	/**
	 * Removes a setting for a module
	 * @param settingName Name of the setting
	 */
	public void removeSetting(String settingName) {
		Document settings = server.getModuleSettings(module.getInternalName());
		settings.remove(settingName);
		server.setModuleSettings(module.getInternalName(), settings);
	}
	
	public enum Type {
		BOOLEAN, STRING, CHANNEL, ROLE, DOCUMENT;
	}
	
}
