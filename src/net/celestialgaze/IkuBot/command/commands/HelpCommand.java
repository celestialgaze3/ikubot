package net.celestialgaze.IkuBot.command.commands;

import java.util.ArrayList;
import java.util.List;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.CommandInterpreter;
import net.celestialgaze.IkuBot.command.Commands;
import net.celestialgaze.IkuBot.command.PagedCommand;
import net.celestialgaze.IkuBot.command.PagedMessage;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.command.module.CommandModules;
import net.celestialgaze.IkuBot.database.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class HelpCommand extends PagedCommand {

	public HelpCommand() {
		super("help",
			  "Get a list of all commands",
			  "");
	}
	
	private String commandView(Command c, Message message) {
		StringBuilder str = new StringBuilder();
		String prefix = Server.get(message.getGuild().getIdLong()).getPrefix();
		str.append("`");
		str.append(prefix + c.getName());
		for (String alias : c.getAliases()) {
			str.append("|" + alias);
		}
		if (!c.getUsage().isBlank()) str.append(" " + c.getUsage());
		str.append("`");
		str.append("\n");
		str.append(c.getDescription());
		str.append("\n\n");
		return str.toString();
	}

	@Override
	public MessageEmbed getUpdatedEmbed(Message message, PagedMessage pagedMsg) {
		EmbedBuilder embed = new EmbedBuilder();
		Server server = Server.get(message.getGuild().getIdLong());
		String prefix = server.getPrefix();
		
		String[] args = CommandInterpreter.getArgs(message, prefix);
		
		// i!help without any arguments
		if (args.length == 0) {
			pagedMsg.setPageLimit(1);
			embed.setAuthor(Iku.getName() + " Help Menu", null, Iku.getUser().getAvatarUrl());
			embed.appendDescription("**Prefix: ** `" + prefix + "` All commands also work by mentioning the bot.\n");
			embed.appendDescription("Get help with a specific command with `" + prefix + "help <command_name>`\n");
			embed.appendDescription("See core commands that are not part of a module with `" + prefix + "help core`\n");
			embed.appendDescription("[Invite](https://discord.com/oauth2/authorize?client_id=869032530643398716&scope=bot&permissions=8589934591)");

			boolean hasAModuleEnabled = false;
			for (CommandModule module : CommandModules.list.values()) {
				if (module.isEnabled(message.getGuild())) {
					hasAModuleEnabled = true;
					embed.addField("**" + module.getName() + "**", "`" + prefix + "help " + module.getName()+"`", true);
				}
			}
			
			if (!hasAModuleEnabled) embed.appendDescription("\nYou currently do not have any modules enabled. Use `" + prefix + "module` to manage them.");
		} else if (args.length >= 1) { // i!help COMMAND or i!help MODULE
			String arg = args[0];
			List<Command> cmdList = new ArrayList<Command>();
			pagedMsg.setPageSize(7);
			
			if (CommandModules.list.containsKey(arg.toLowerCase())) { // Check if it's a valid module
				CommandModule module = CommandModules.list.get(arg.toLowerCase());
				embed.setAuthor(module.getName() + " Module Help Menu", null, Iku.getUser().getAvatarUrl());
				
				if (!module.isEnabled(message.getGuild())) {
					embed.setDescription("That module isn't enabled! Do `" + prefix + "module enable " + module.getName() + "` to enable it.");
					return embed.build();
				}
				cmdList = IkuUtil.removeDuplicates(new ArrayList<Command>(module.getCommands().values()));
				
				
			} else if (Commands.getBaseCommands(message.getGuild()).containsKey(arg)) { // Check if it's a valid command
				Command c = Commands.getBaseCommands(message.getGuild()).get(arg);
				embed.appendDescription(commandView(c, message));
			} else if (arg.equalsIgnoreCase("core")) { // "Core" module (commands not part of a module)
				cmdList = IkuUtil.removeDuplicates(new ArrayList<Command>(Command.baseCommands.values()));
			} else {
				return embed.setDescription("Sorry, I couldn't find the module or command you want help with. Did you misspell something?").build();
			}
			
			// Paginating
			pagedMsg.updatePageLimit(cmdList.size());
			for (int i = pagedMsg.getStartIndex(); i < cmdList.size() && i <= pagedMsg.getEndIndex(); i++) {
				Command c = cmdList.get(i);
				embed.appendDescription(commandView(c, message));
			}
			
			embed.setFooter("Page " + pagedMsg.getPage() + " • <> required, [] optional", Iku.getUser().getAvatarUrl());
		}
		
		return embed.build();
	}

}
