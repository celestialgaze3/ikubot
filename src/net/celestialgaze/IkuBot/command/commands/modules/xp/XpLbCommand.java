package net.celestialgaze.IkuBot.command.commands.modules.xp;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.database.UserProfile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class XpLbCommand extends Command {

	public XpLbCommand() {
		super("leaderboard");
	}

	@Override
	public void run(String[] args, Message message) {
		List<UserProfile> profiles = UserProfile.getServer(message.getGuild().getIdLong());
		Collections.sort(profiles, Comparator.comparingInt(UserProfile::getExperience).reversed());
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(message.getGuild().getName(), null, message.getGuild().getIconUrl());
		embed.setTitle("XP Leaderboard");
		for (int i = 0; i < profiles.size(); i++) {
			UserProfile profile = profiles.get(i);
			Member member = profile.retrieveMember().complete();
			embed.appendDescription((i == 0 ? "**" : "") + (i + 1) + ". " + member.getUser().getAsMention() + " - " + profile.getExperience() + " xp" + (i == 0 ? "**" : "") + "\n");
		}
		message.getChannel().sendMessageEmbeds(embed.build()).queue();
	}

}
