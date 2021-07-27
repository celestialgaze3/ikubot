package net.celestialgaze.IkuBot.command.module;

import org.bson.Document;

import net.celestialgaze.IkuBot.database.Server;
import net.dv8tion.jda.api.entities.Guild;

public class ModuleSettings {
	
	Guild guild;
	CommandModule module;
	Server server;
	public ModuleSettings(Guild guild, CommandModule module) {
		this.guild = guild;
		this.module = module;
		this.server = Server.get(guild.getIdLong());
	}
	
	/**
	 * Gets a setting for a module
	 * @param <T> type
	 * @param settingName Name of the setting
	 * @param def Default value for the setting
	 * @return The setting's value, or default if none was found.
	 */
	public <T> T getSetting(String settingName, T def) {
		return server.getModuleSettings(module.getInternalName()).get(settingName, def);
	}
	
	/**
	 * Sets a setting for a module
	 * @param settingName Name of the setting
	 * @param value Value to set for the setting
	 */
	public void setSetting(String settingName, Object value) {
		Document settings = server.getModuleSettings(module.getInternalName());
		settings.put(settingName, value);
		server.setModuleSettings(module.getInternalName(), settings);
	}
	
}
