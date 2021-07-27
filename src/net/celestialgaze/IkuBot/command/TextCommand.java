package net.celestialgaze.IkuBot.command;

import net.dv8tion.jda.api.entities.Message;

public class TextCommand extends Command {
	String text;
	public TextCommand(String name, String description, String text) {
		super(name, description, "");
		this.text = text;
	}

	@Override
	public void run(String[] args, Message message) {
		message.getChannel().sendMessage(text).queue();
	}

}
