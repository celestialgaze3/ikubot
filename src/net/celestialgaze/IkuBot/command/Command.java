package net.celestialgaze.IkuBot.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.api.entities.Message;

public abstract class Command {
	public static Map<String, Command> baseCommands = new HashMap<String, Command>();
	protected String name;
	protected String description;
	protected String usage;
	
	protected List<String> aliases = new ArrayList<String>();
	
	protected Map<String, Command> subcommands = new HashMap<String, Command>();
	
	public static void addBaseCommand(Command command) {
		baseCommands.put(command.getName(), command);
		for (String alias : command.getAliases()) {
			baseCommands.put(alias, command);
		}
	}
	
	public Command(String name, String description, String usage, String... aliases) {
		this.name = name;
		this.description = description;
		this.usage = usage;
		for (String s : aliases) {
			this.aliases.add(s);
		}
		
		init();
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public String getDescription() {
		return description;
	}

	public String getUsage() {
		return usage;
	}
	
	public Command getSubcommand(String commandName) {
		return subcommands.get(commandName);
	}

	public boolean hasSubcommand(String commandName) {
		return subcommands.containsKey(commandName);
	}

	public boolean hasSubcommands() {
		return subcommands.size() > 0;
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
