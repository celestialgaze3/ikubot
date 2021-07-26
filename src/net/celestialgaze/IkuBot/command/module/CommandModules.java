package net.celestialgaze.IkuBot.command.module;

import java.util.ArrayList;
import java.util.List;

import net.celestialgaze.IkuBot.command.commands.modules.ModuleTestCommand;

public class CommandModules {
	public static List<CommandModule> list = new ArrayList<CommandModule>();
	
	
	public static void init() {
		CommandModule test = new CommandModule("Test", new ModuleTestCommand());
		list.add(test);
	}
}
