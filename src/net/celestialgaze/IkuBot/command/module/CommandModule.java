package net.celestialgaze.IkuBot.command.module;

import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.IkuBot.command.Command;
import net.dv8tion.jda.api.entities.Guild;

public class CommandModule {
	private Map<String, Command> commands = new HashMap<String, Command>();
	private boolean enabled = true;
	String name;
	
	public CommandModule(String name, Command... commands) {
		for (Command command : commands) {
			this.name = name;
			this.commands.put(command.getName(), command);
		}
	}
	
	public boolean isEnabled(Guild guild) {
		return enabled;
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, Command> getCommands() {
		return commands;
	}
}
