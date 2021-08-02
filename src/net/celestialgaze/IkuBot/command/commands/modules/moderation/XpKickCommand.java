package net.celestialgaze.IkuBot.command.commands.modules.moderation;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class XpKickCommand extends Command {

	public XpKickCommand() {
		super("kick",
			  "Kick a user from the server",
			  "<member>");
		this.setUsableDMs(false);
		this.setPermissions(Permission.KICK_MEMBERS);
		this.setPermissionsRequired(Permission.KICK_MEMBERS);
	}

	@Override
	public void run(String[] args, Message message) {
		if (!this.meetsArgCount(message, args, 1)) return;
		String remainingArgs = IkuUtil.arrayToString(args, " ");
		Member member = IkuUtil.getMember(message.getGuild(), remainingArgs);
		member.kick("");
	}

}
