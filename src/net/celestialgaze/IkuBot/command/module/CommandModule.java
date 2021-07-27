package net.celestialgaze.IkuBot.command.module;

import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.IkuBot.command.Command;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class CommandModule {
	private Map<String, Command> commands = new HashMap<String, Command>();
	String name;
	boolean enabledByDefault = false;
	
	public CommandModule(String name, Command... commands) {
		for (Command command : commands) {
			this.name = name;
			this.commands.put(command.getName(), command);
		}
	}
	
	public ModuleSettings getSettings(Guild guild) {
		return new ModuleSettings(guild, this);
	}
	
	public boolean isEnabled(Guild guild) {
		return getSettings(guild).getSetting("enabled", enabledByDefault);
	}
	
	public void setEnabled(Guild guild, boolean enabled) {
		getSettings(guild).setSetting("enabled", enabled);
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
