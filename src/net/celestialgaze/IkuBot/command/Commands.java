package net.celestialgaze.IkuBot.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.celestialgaze.IkuBot.command.commands.AvatarCommand;
import net.celestialgaze.IkuBot.command.commands.HelpCommand;
import net.celestialgaze.IkuBot.command.commands.ModuleCommand;
import net.celestialgaze.IkuBot.command.commands.PingCommand;
import net.celestialgaze.IkuBot.command.commands.PrefixCommand;
import net.celestialgaze.IkuBot.command.commands.SayCommand;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.celestialgaze.IkuBot.command.module.CustomCmdsModule;
import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.api.entities.Guild;

public class Commands {
	public static void init() {
		Command.addBaseCommand(new RollCommand());
		Command.addBaseCommand(new AvatarCommand());
		Command.addBaseCommand(new PingCommand());
		Command.addBaseCommand(new PrefixCommand());
		Command.addBaseCommand(new HelpCommand());
		Command.addBaseCommand(new ModuleCommand());
		Command.addBaseCommand(new SayCommand());
		Command.addBaseCommand(new TextCommand("source", "Get a link to the bot's source on GitHub", "https://github.com/celestialgaze3/ikubot"));
		CommandModules.init();
	}
	
	/**
	 * Gives the list of top-level commands (the ones that should be detected for the first argument in a message)
	 * @param guild The guild to get the base commands for
	 * @return The list of top-level commands
	 */
	public static Map<String, Command> getBaseCommands(Guild guild) {
		Map<String, Command> commands = new HashMap<String, Command>();
		
		if (guild != null) {
			// Base commands
			for (Entry<String, Command> entry : Command.baseCommands.entrySet()) {
				commands.put(entry.getKey(), entry.getValue());
			}
			
			// Module commands
			for (CommandModule module : CommandModules.list.values()) {
				if (module.isEnabled(guild)) { // Add enabled modules only
					for (Entry<String, Command> entry : module.getCommands().entrySet()) {
						commands.put(entry.getKey(), entry.getValue());
					}
					if (module instanceof CustomCmdsModule) {
						CustomCmdsModule customCmds = (CustomCmdsModule) module;
						for (Command c : customCmds.getCustomCommands(guild)) {
							commands.put(c.getName(), c);
						}
					}
				}
			}
		} else {
			// Base commands
			for (Entry<String, Command> entry : Command.baseCommands.entrySet()) {
				commands.put(entry.getKey(), entry.getValue());
			}
						
			// Module commands
			for (CommandModule module : CommandModules.list.values()) {
				if (module.enabledByDefault()) {
					for (Command c : IkuUtil.removeDuplicates(new ArrayList<Command>(module.getCommands().values()))) {
						commands.put(c.getName(), c);
					}
				}
			}
		}
		
		return commands;
	}
}
