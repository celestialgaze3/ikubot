package net.celestialgaze.IkuBot.command.commands;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.util.Iku;
import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class AvatarCommand extends Command {

	public AvatarCommand() {
		super("avatar", "Get the avatar of a user", "<user>");
	}

	@Override
	public void run(String[] args, Message message) {
		if (!this.meetsArgCount(message, args, 1)) return;
		String input = IkuUtil.arrayToString(args, " ");

		Member member = null;
		if (message.getChannelType().equals(ChannelType.PRIVATE)) {
			// Search all mutual guilds for the user
			for (Guild g : message.getAuthor().getMutualGuilds()) {
				Member search = IkuUtil.getMember(g, input);
				if (search != null) {
					member = search;
					break;
				}
			}
		} else {
			member = IkuUtil.getMember(message.getGuild(), input);
		}
		if (member == null) {
			Iku.sendError(message, "Could not find user " + input);
			return;
		}
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(getColor(message));
		embed.setTitle(member.getUser().getAsTag() + "'s Avatar");
		embed.setImage(member.getUser().getEffectiveAvatarUrl() + "?size=2048");
		
		message.getChannel().sendMessageEmbeds(embed.build()).queue();
	}
	
}
