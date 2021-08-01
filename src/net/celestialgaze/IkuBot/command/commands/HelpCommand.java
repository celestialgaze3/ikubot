package net.celestialgaze.IkuBot.command.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.CommandInterpreter;
import net.celestialgaze.IkuBot.command.Commands;
import net.celestialgaze.IkuBot.command.PagedCommand;
import net.celestialgaze.IkuBot.command.PagedMessage;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.celestialgaze.IkuBot.command.module.CustomCmdsModule;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class HelpCommand extends PagedCommand {

	public HelpCommand() {
		super("help",
			  "Get a list of all commands",
			  "");
	}
	
	
	@Override
	public MessageEmbed getUpdatedEmbed(Message message, PagedMessage pagedMsg) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setColor(getColor(message));
		String prefix = getPrefix(message);
		
		String[] args = CommandInterpreter.getArgs(message, prefix);
		
		// i!help without any arguments
		if (args.length == 0) {
			pagedMsg.setPageLimit(1);
			embed = getMainHelpMenu(message);
		} else if (args.length >= 1) { // i!help COMMAND or i!help MODULE
			String arg = args[0];
			pagedMsg.setPageSize(7);
			
			if (CommandModules.list.containsKey(arg.toLowerCase())) { // Check if it's a valid module
				embed = getModuleHelpMenu(message, pagedMsg, arg);
			} else if (Commands.getBaseCommands(IkuUtil.getGuild(message)).containsKey(arg)) { // Check if it's a valid command
				embed = getCommandHelpMenu(message, pagedMsg, arg);
			} else if (arg.equalsIgnoreCase("core")) { // "Core" module (commands not part of a module)
				List<Command> cmdList = new ArrayList<Command>(Command.baseCommands.values());
				addCommands(message, pagedMsg, cmdList, embed);
			} else {
				return embed.setDescription("Sorry, I couldn't find the module or command you want help with. Did you misspell something?").build();
			}
			
		}
		
		return embed.build();
	}

	private EmbedBuilder getMainHelpMenu(Message message) {
		EmbedBuilder embed = new EmbedBuilder();
		String prefix = getPrefix(message);
		
		embed.setAuthor(Iku.getName() + " Help Menu", null, Iku.getUser().getAvatarUrl());
		embed.appendDescription("**Prefix: ** `" + prefix + "` All commands also work by mentioning the bot.\n");
		embed.appendDescription("Get help with a specific command with `" + prefix + "help <command_name>`\n");
		embed.appendDescription("See core commands that are not part of a module with `" + prefix + "help core`\n");
		embed.appendDescription("[Invite](https://discord.com/oauth2/authorize?client_id=869032530643398716&scope=bot&permissions=8589934591)");

		boolean hasAModuleEnabled = false;
		for (CommandModule module : CommandModules.list.values()) {
			if (module.isEnabled(IkuUtil.getGuild(message))) {
				hasAModuleEnabled = true;
				embed.addField("**" + module.getName() + "**", "`" + prefix + "help " + module.getName()+"`", true);
			}
		}
		
		if (!hasAModuleEnabled) embed.appendDescription("\nYou currently do not have any modules enabled. Use `" + prefix + "module` to manage them.");
		return embed;
	}
	
	private static EmbedBuilder getCommandHelpMenu(Message message, PagedMessage paged, String commandName) {
		EmbedBuilder embed = new EmbedBuilder();
		String prefix = getPrefix(message);
		
		Command c = Commands.getBaseCommands(IkuUtil.getGuild(message)).get(commandName);
		embed.appendDescription(c.view(prefix));
		return embed;
	}
	
	private static EmbedBuilder getModuleHelpMenu(Message message, PagedMessage paged, String moduleName) {
		EmbedBuilder embed = new EmbedBuilder();
		CommandModule module = CommandModules.list.get(moduleName.toLowerCase());
		String prefix = getPrefix(message);
		
		embed.setTitle(module.getName() + " Module Help Menu");
		embed.setColor(getColor(message));
		
		if (!module.isEnabled(IkuUtil.getGuild(message))) {
			embed.setDescription("That module isn't enabled! Do `" + prefix + "module enable " + module.getName() + "` to enable it.");
			return embed;
		}
		
		// List permissions the bot should have if they don't already have all of the required permissions
		if (message.isFromGuild() && !module.hasRequiredPerms(message.getGuild()) && paged.getPage() == 1) {
			Member member = Iku.getMember(message.getGuild());
			embed.appendDescription("**Permissions:**\n");
			for (Entry<Permission, String> entry : module.getRequiredPerms().entrySet()) {
				embed.appendDescription(member.hasPermission(entry.getKey()) ? "✅" : "❌" + " " + entry.getValue() + " (" + entry.getKey().getName() + ")\n");
			}
			embed.appendDescription("\n");
		}
		
		List<Command> cmdList = IkuUtil.removeDuplicates(new ArrayList<Command>(module.getCommands().values()));
		
		// Add any custom commands to the custom cmds module
		if (module instanceof CustomCmdsModule) {
			CustomCmdsModule customCmds = (CustomCmdsModule) module;
			for (Command c : customCmds.getCustomCommands(IkuUtil.getGuild(message))) {
				cmdList.add(c);
			}
		}
		
		addCommands(message, paged, cmdList, embed);
		return embed;
	}
	
	public static EmbedBuilder addCommands(Message message, PagedMessage paged, List<Command> cmdList, EmbedBuilder embed) {
		String prefix = getPrefix(message);
		
		// Remove duplicate commands
		cmdList = IkuUtil.removeDuplicates(cmdList);
		
		// Remove commands that can't be ran
		List<Command> toRemove = new ArrayList<Command>();
		for (Command c : cmdList) {
			if (!c.canRun(message)) toRemove.add(c);
		}
		for (Command c : toRemove) {
			cmdList.remove(c);
		}
					
		// Paginating
		paged.updatePageLimit(cmdList.size());
		for (int i = paged.getStartIndex(); i < cmdList.size() && i <= paged.getEndIndex(); i++) {
			Command c = cmdList.get(i);
			embed.appendDescription(c.view(prefix));
		}
		
		embed.setFooter("Page " + paged.getPage() + " • <> required, [] optional", Iku.getUser().getAvatarUrl());
		
		return embed;
	}
}
