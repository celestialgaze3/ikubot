package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.api.entities.Message;

public class TestTestCommand extends Command {

	public TestTestCommand() {
		super("test", 
		      "an incredible subcommand of the test command. that also tests. it's awesomeazing", 
			  "");
	}

	@Override
	public void run(String[] args, Message message) {
		message.getChannel().sendMessage("test test!!! wooo!!!!!!!!!!!\non another note, the args are " + IkuUtil.arrayToString(args, " ")).queue();
	}

}
