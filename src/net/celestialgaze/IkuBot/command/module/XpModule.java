package net.celestialgaze.IkuBot.command.module;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.database.UserProfile;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class XpModule extends CommandModule {
	Map<Long, HashMap<Long, Long>> cooldownEndTimes = new HashMap<Long, HashMap<Long, Long>>();
	
	public XpModule(Command... commands) {
		super(CommandModules.Module.XP.getName(), commands);
	}
	
	@Override
	public void onMessage(MessageReceivedEvent event) {
		final int expBaseIncrease = 10;
		final int expChanceBonus = 5;
		final long cooldownDurationMs = 15 * 1000;

		User user = event.getAuthor();
		Guild guild = event.getGuild();
		if (!isCooldown(guild, user)) {
			UserProfile profile = UserProfile.get(guild.getIdLong(), user.getIdLong());
			final int xpToAdd = expBaseIncrease + new Random().nextInt(expChanceBonus);
			profile.addExperience(xpToAdd);
			setCooldown(guild, user, cooldownDurationMs);
		}
	}
	
	private void setCooldown(Guild guild, User user, long ms) {
		if (!cooldownEndTimes.containsKey(guild.getIdLong())) {
			cooldownEndTimes.put(guild.getIdLong(), new HashMap<Long, Long>());
		}
		cooldownEndTimes.get(guild.getIdLong()).put(user.getIdLong(), System.currentTimeMillis() + ms);
	}
	
	private boolean isCooldown(Guild guild, User user) {
		return cooldownEndTimes.containsKey(guild.getIdLong()) && 
				cooldownEndTimes.get(guild.getIdLong()).containsKey(user.getIdLong()) &&
				System.currentTimeMillis() < cooldownEndTimes.get(guild.getIdLong()).get(user.getIdLong());
	}

}
