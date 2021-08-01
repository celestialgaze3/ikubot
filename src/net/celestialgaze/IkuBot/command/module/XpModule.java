package net.celestialgaze.IkuBot.command.module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.commands.modules.xp.roles.XpRoles;
import net.celestialgaze.IkuBot.command.module.ModuleSettings.Type;
import net.celestialgaze.IkuBot.database.UserProfile;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class XpModule extends CommandModule {
	static Map<Integer, Integer> levelXpMinimum = new HashMap<Integer, Integer>();
	Map<Long, HashMap<Long, Long>> cooldownEndTimes = new HashMap<Long, HashMap<Long, Long>>();
	
	public XpModule(Command... commands) {
		super(CommandModules.Module.XP.getName(), commands);
		
		this.addPermission(Permission.MANAGE_ROLES, "Assigning xp roles");
		
		this.addSetting("xpRoles", Type.DOCUMENT);
		this.addSetting("levelUpMessageLocation", Type.STRING);
	}

	final int expBaseIncrease = 25;
	final int expChanceBonus = 18;
	final long cooldownDurationMs = 20 * 1000;
	
	@Override
	public void onMessage(MessageReceivedEvent event) {
		Message message = event.getMessage();
		User user = event.getAuthor();
		Guild guild = event.getGuild();
		if (!isCooldown(guild, user)) {
			UserProfile profile = UserProfile.get(guild.getIdLong(), user.getIdLong());
			final int xpToAdd = expBaseIncrease + new Random().nextInt(expChanceBonus);
			profile.addExperience(xpToAdd);
			setCooldown(guild, user, cooldownDurationMs);
			
			// If user has leveled up
			int level = getLevel(profile.getExperience());
			if (level > getLevel(profile.getExperience() - xpToAdd)) {
				XpRoles xpRoles = XpRoles.instance;
				
				// Send levelup message
				getLevelUpMsgChannel(event.getMessage()).sendMessage(user.getAsMention() + ", you're now Level " + level).queue();
				
				// Give all roles for their level
				List<Entry<Integer, Role>> roles = new ArrayList<Entry<Integer, Role>>(xpRoles.getXpRoles(guild).entrySet());
				for (int i = 0; i < roles.size(); i++) {
					Entry<Integer, Role> entry = roles.get(i);
					if (Iku.canManageRole(guild, entry.getValue())) {
						if (entry.getKey() <= level) { // Should have role
							guild.addRoleToMember(message.getMember(), entry.getValue()).queue();
						} else {
							guild.removeRoleFromMember(message.getMember(), entry.getValue()).queue();
						}
					} else {
						message.getChannel().sendMessage("I can't manage the role " + entry.getValue().getName()).queue();
					}
				}
			}
		}
		
		
	}
	
	public MessageChannel getLevelUpMsgChannel(Message message) {
		String levelUpMsgSetting = this.getSettings(message.getGuild()).getString("levelUpMessageLocation", "DM");
		
		// Is ID
		if (IkuUtil.isLong(levelUpMsgSetting)) {
			long id = IkuUtil.getLong(levelUpMsgSetting);
			TextChannel channel = message.getGuild().getTextChannelById(id);
			if (channel != null) {
				return channel;
			}
		} else {
			if (levelUpMsgSetting.equalsIgnoreCase("DM")) {
				return message.getMember().getUser().openPrivateChannel().complete();
			}
		}
		return message.getChannel();
	}
	
	public static int getXp(int level) {
		if (level > 100) {
			return levelXpMinimum.get(100);
		}
		return levelXpMinimum.get(level);
	}
	
	public static int getLevel(int xp) {
		
		// Calculate levels if not done already
		if (levelXpMinimum.size() == 0) {
			// Equation for lvls 1-10
			for (int lvl = 1; lvl <= 10; lvl++) {
				levelXpMinimum.put(lvl, IkuUtil.roundDownExact(57.9512 * Math.pow(lvl, 2) - 145.0552 * lvl + 85.5847));
			}
			
			// Equation for 11-30
			for (int lvl = 11; lvl <= 30; lvl++) {
				levelXpMinimum.put(lvl, IkuUtil.roundDownExact(5.6591 * Math.pow(lvl, 3) - 164.1407 * Math.pow(lvl, 2) + 2194.9452 * lvl - 6767.7358));
			}
			
			// Equation for lvls 31 - 70
			for (int lvl = 31; lvl <= 70; lvl++) {
				levelXpMinimum.put(lvl, IkuUtil.roundDownExact(-0.0744 * Math.pow(lvl, 4) + 12.8810 * Math.pow(lvl, 3) - 706.3125 * Math.pow(lvl, 2) + 20406.541 * lvl -199988.0952));
			}
			
			// Equation for lvls 71 - 100
			for (int lvl = 71; lvl <= 100; lvl++) {
				levelXpMinimum.put(lvl, IkuUtil.roundDownExact(350 * Math.pow(lvl, 2) - 39700 * lvl + 1470000));
			}
		}
		for (int lvl : levelXpMinimum.keySet()) {
			if (xp < levelXpMinimum.get(lvl)) return lvl - 1;
		}
		return 1;
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
