package net.celestialgaze.IkuBot.command.commands.modules;

import net.celestialgaze.IkuBot.command.Command;
import net.dv8tion.jda.api.entities.Message;

public class ModuleTestCommand extends Command {

	public ModuleTestCommand() {
		super("moduletest");
	}

	@Override
	public void run(String[] args, Message message) {
		message.getChannel().sendMessage("test module command").queue();
	}

}
