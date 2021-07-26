package net.celestialgaze.IkuBot.command;

import net.celestialgaze.IkuBot.command.commands.TestCommand;

public class Commands {
	public static void init() {
		Command.addBaseCommand(new TestCommand());
	}
}
