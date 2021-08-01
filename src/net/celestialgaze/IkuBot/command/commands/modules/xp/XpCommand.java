package net.celestialgaze.IkuBot.command.commands.modules.xp;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.command.MasterCommand;
import net.celestialgaze.IkuBot.command.commands.modules.xp.roles.XpRoles;
import net.celestialgaze.IkuBot.command.module.XpModule;
import net.celestialgaze.IkuBot.database.UserProfile;
import net.dv8tion.jda.api.entities.Message;

public class XpCommand extends MasterCommand {

	public XpCommand() {
		super("xp",
			  "Gives you info on your current xp, and contains many xp-related subcommands.\nSee `${prefix}xp help`",
			  "[subcommand]");
		this.setUsableDMs(false);
	}
	
	@Override
	public void init() {
		super.init();
		addSubcommand(new XpLbCommand());
		addSubcommand(new XpRoles());
	}

	@Override
	public void run(String[] args, Message message) {
		int xp = UserProfile.get(message.getGuild().getIdLong(), message.getAuthor().getIdLong()).getExperience();
		int level = XpModule.getLevel(xp);
		int xpToLevel = XpModule.getXp(level + 1) - xp;
		Iku.send(message, "You have `" + xp + "` xp (Level " + level + ", " + xpToLevel + "xp to level up)");
	}

}
