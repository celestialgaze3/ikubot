package net.celestialgaze.IkuBot.command.module;

import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.module.ModuleSettings.Type;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CommandModule {
	private Map<String, Command> commands = new HashMap<String, Command>();
	
	String name;
	String description;
	Map<Permission, String> permissionsNeeded = new HashMap<Permission, String>();
	
	Map<String, Type> settings = new HashMap<String, Type>();
	Map<String, Type> lowerCaseSettings = new HashMap<String, Type>();
	Map<String, Type> editableSettings = new HashMap<String, Type>();
	
	boolean enabledByDefault = false;
	
	public CommandModule(String name, Command... commands) {
		for (Command command : commands) {
			this.name = name;
			command.setModule(this);
			command.moduleInit();
			this.commands.put(command.getName(), command);
			for (String alias : command.getAliases()) {
				this.commands.put(alias, command);
			}
		}
		this.addSetting("enabled", Type.BOOLEAN);
	}

	public void addSetting(String settingName, Type type) {
		addSetting(settingName, type, false);
	}
	
	public void addSetting(String settingName, Type type, boolean deezNuts) {
		settings.put(settingName, type);
		lowerCaseSettings.put(settingName.toLowerCase(), type);
		if (deezNuts) editableSettings.put(settingName, type);
	}

	public boolean isSetting(String settingName, Type type) {
		return isSetting(settingName, type, true);
	}
	
	public boolean isSetting(String settingName, Type type, boolean log) {
		boolean setting = lowerCaseSettings.containsKey(settingName.toLowerCase()) && lowerCaseSettings.get(settingName.toLowerCase()).equals(type);
		if (!setting && log) {
			Iku.error("The setting " + settingName + " for type " + type.toString() + " does not exist in module " + getName());
		}
		return setting;
	}
	
	public void addPermission(Permission permission, String reason) {
		permissionsNeeded.put(permission, reason);
	}
	
	public boolean hasRequiredPerms(Guild guild) {
		if (guild == null) return false;
		return Iku.getMember(guild).hasPermission(permissionsNeeded.keySet());
	}
	
	public Map<Permission, String> getRequiredPerms() {
		return permissionsNeeded;
	}
	
	public ModuleSettings getSettings(Guild guild) {
		return new ModuleSettings(guild, this);
	}
	
	public boolean enabledByDefault() {
		return enabledByDefault;
	}
	
	public boolean isEnabled(Guild guild) {
		if (guild == null) return enabledByDefault();
		return getSettings(guild).getBoolean("enabled", enabledByDefault);
	}
	
	public void setEnabled(Guild guild, boolean enabled) {
		getSettings(guild).setBoolean("enabled", enabled);
	}
	
	/**
	 * @return The user-friendly name of the module
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The name that will be used in programming
	 */
	public String getInternalName() {
		return name.toLowerCase();
	}
	
	public Map<String, Command> getCommands() {
		return commands;
	}
	
	public void onMessage(MessageReceivedEvent event) {}
}
