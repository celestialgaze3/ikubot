package net.celestialgaze.IkuBot.command.commands.modules.xp;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.database.UserProfile;
import net.dv8tion.jda.api.entities.Message;

public class XpCommand extends Command {

	public XpCommand() {
		super("xp",
			  "Gets you info on your current xp",
			  "");
	}

	@Override
	public void run(String[] args, Message message) {
		message.getChannel().sendMessage("You have `" + UserProfile.get(message.getGuild().getIdLong(), message.getAuthor().getIdLong()).getExperience() + "` xp").queue();
	}

}
