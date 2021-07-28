package net.celestialgaze.IkuBot.command;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.dv8tion.jda.api.entities.Message;

public class CommandInterpreter {
	/**
	 * Gets the command from a message and runs it.
	 * @param message The message to process
	 * @param prefix The prefix of the string (if any)
	 * @return Whether it was successfully able to run a command or not
	 */
	public static boolean runCommandFromMsg(Message message, String prefix) {
		/* A command would look like !command subcommand subcommand ... When the next argument in a message isn't a subcommand
		 * is when the arguments for the command begin. 
		 * 
		 * So, we get the command from the first argument, then check if the next argument is the name of a subcommand of the first command,
		 * then repeat this process for each subcommand until the next argument is no longer a subcommand of the current command. Then, the
		 * command to run will be the current command, and the arguments to pass in will be everything not already looped through.
		 */
		
		// Make all commands work by mentioning the bot
		String mention = "<@!" + Iku.getUser().getId() + "> ";
		if (!prefix.contentEquals(mention) && runCommandFromMsg(message, mention)) return true;
		
		// If the message does not start with the prefix, we already know it's not a command
		if (!message.getContentRaw().toLowerCase().startsWith(prefix.toLowerCase())) return false;
		
		// Remove prefix
		String str = message.getContentRaw().substring(prefix.length());
		
		String[] args = str.split(" ");
		
		// Get the first command
		Command currentCmd = Commands.getBaseCommands(IkuUtil.getGuild(message)).get(args[0].toLowerCase());
		if (currentCmd == null) return false; // Was not a valid command.
		
		int argsBeginIndex = 1;
		// Loop through until the end of the subcommands is reached
		for (int i = 1; i < args.length; i++) {
			String arg = args[i];
			if (currentCmd.hasSubcommand(arg)) {
				currentCmd = currentCmd.getSubcommand(arg);
				argsBeginIndex = i + 1;
			} else {
				break;
			}
		}
		
		String[] cutArgs = IkuUtil.cutArray(args, argsBeginIndex, args.length - 1);
		if (currentCmd.canRun(message))
			currentCmd.run(cutArgs, message);
		else message.getChannel().sendMessage(currentCmd.getReason(message)).queue();
		
		return true;
	}
	
	/**
	 * Get the command arguments from a message on the fly
	 * @param message The message to get arguments from
	 * @return The arguments of a command that would run off of this message
	 */
	public static String[] getArgs(Message message, String prefix) {
		// Make all commands work by mentioning the bot
		String mention = "<@!" + Iku.getUser().getId() + "> ";
		if (message.getContentRaw().contentEquals(mention.strip())) return new String[0]; // Mentioning the bot gets the help command by default
		if (!prefix.contentEquals(mention) && getArgs(message, mention) != null) return getArgs(message, mention);
		
		// If the message does not start with the prefix, we already know it's not a command
		if (!message.getContentRaw().toLowerCase().startsWith(prefix.toLowerCase())) return null;
				
		// Remove prefix
		String str = message.getContentRaw().substring(prefix.length());
		
		String[] args = str.split(" ");
		
		// Get the first command
		Command currentCmd = Commands.getBaseCommands(IkuUtil.getGuild(message)).get(args[0].toLowerCase());
		if (currentCmd == null) return null; // Was not a valid command.
		
		int argsBeginIndex = 1;
		// Loop through until the end of the subcommands is reached
		for (int i = 1; i < args.length; i++) {
			String arg = args[i];
			if (currentCmd.hasSubcommand(arg)) {
				currentCmd = currentCmd.getSubcommand(arg);
				argsBeginIndex = i + 1;
			} else {
				break;
			}
		}
		
		String[] cutArgs = IkuUtil.cutArray(args, argsBeginIndex, args.length - 1);
		return cutArgs;
	}
}
