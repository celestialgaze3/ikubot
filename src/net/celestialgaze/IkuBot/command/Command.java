package net.celestialgaze.IkuBot.command;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.api.entities.Message;

public abstract class Command {
	public static Map<String, Command> baseCommands = new HashMap<String, Command>();
	protected String name;
	protected Map<String, Command> subcommands = new HashMap<String, Command>();
	
	public static void addBaseCommand(Command command) {
		baseCommands.put(command.getName(), command);
	}
	
	public Command(String name) {
		this.name = name;
		init();
	}
	
	public boolean hasSubcommands() {
		return subcommands.size() > 0;
	}
	
	public boolean hasSubcommand(String commandName) {
		return subcommands.containsKey(commandName);
	}
	
	public Command getSubcommand(String commandName) {
		return subcommands.get(commandName);
	}
	
	public void addSubcommand(Command command) {
		subcommands.put(command.getName(), command);
	}
	
	public String getName() {
		return name;
	}
	
	public abstract void run(String args[], Message message);
	public void init() {}
}
