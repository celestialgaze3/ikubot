package net.celestialgaze.IkuBot.command.commands.modules.xp;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.celestialgaze.IkuBot.command.PagedCommand;
import net.celestialgaze.IkuBot.command.PagedMessage;
import net.celestialgaze.IkuBot.command.module.XpModule;
import net.celestialgaze.IkuBot.database.UserProfile;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class XpLbCommand extends PagedCommand {
	public XpLbCommand() {
		super("leaderboard",
			  "See who has the most XP",
			  "",
			  "lb");
		this.setUsableDMs(false);
	}

	@Override
	public MessageEmbed getUpdatedEmbed(Message message, PagedMessage pagedMsg) {
		List<UserProfile> profiles = UserProfile.getServer(message.getGuild().getIdLong());
		Collections.sort(profiles, Comparator.comparingInt(UserProfile::getExperience).reversed());
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setAuthor(message.getGuild().getName(), null, message.getGuild().getIconUrl());
		embed.setTitle("XP Leaderboard");
		embed.setColor(getColor(message));
		
		pagedMsg.setPageSize(10);
		pagedMsg.updatePageLimit(profiles.size());
		
		for (int i = pagedMsg.getStartIndex(); i < profiles.size() && i <= pagedMsg.getEndIndex(); i++) {
			UserProfile profile = profiles.get(i);
			if (i == 0) embed.setThumbnail(message.getGuild().getMemberById(profile.getUserIdLong()).getUser().getAvatarUrl());
			embed.appendDescription((i == 0 ? "**" : "") + (i + 1) + ". <@!" + profile.getUserIdLong() + "> - " + profile.getExperience() + " xp (Level " + XpModule.getLevel(profile.getExperience()) + ")" + (i == 0 ? "**" : "") + "\n");
		}
		
		return embed.build();
	}

}
