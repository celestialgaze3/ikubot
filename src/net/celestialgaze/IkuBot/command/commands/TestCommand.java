package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.dv8tion.jda.api.entities.Message;

public class TestCommand extends Command {

	public TestCommand() {
		super("test",
			  "you won't believe how testy this test command is! (it tests)",
			  "");
	}

	public void init() {
		this.addSubcommand(new TestTestCommand());
	}
	
	@Override
	public void run(String[] args, Message message) {
		message.getChannel().sendMessage("test !!!!!!!!!! :D\non another note, the args are " + IkuUtil.arrayToString(args, " ")).queue();
	}

}
