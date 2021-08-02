package net.celestialgaze.IkuBot.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.celestialgaze.IkuBot.command.input.IntegerProcessor;
import net.celestialgaze.IkuBot.command.input.StringProcessor;
import net.celestialgaze.IkuBot.command.input.TypeProcessor;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.database.Server;
import net.celestialgaze.IkuBot.util.Iku;
import net.celestialgaze.IkuBot.util.IkuUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.internal.utils.tuple.MutableTriple;
import net.dv8tion.jda.internal.utils.tuple.Pair;

public abstract class Command {
	public static Map<String, Command> baseCommands = new HashMap<String, Command>();
	
	protected String name;
	protected String description;
	protected String usage;
	
	List<Permission> permissions = new ArrayList<Permission>();
	List<Permission> permissionsToRun = new ArrayList<Permission>();
	protected boolean inheritsModulePermissions = false;
	protected boolean usableDMs = true;
	protected boolean admin = false;
	
	protected CommandModule module;
	
	protected List<String> aliases = new ArrayList<String>();
	
	protected Map<String, Command> subcommands = new HashMap<String, Command>();
	protected Command parent;

	public static void addBaseCommand(Command command) {
		baseCommands.put(command.getName(), command);
		for (String alias : command.getAliases()) {
			baseCommands.put(alias, command);
		}
	}
	
	public Command(String name, String description, String usage, String... aliases) {
		this.name = name;
		this.description = description;
		this.usage = usage;
		for (String s : aliases) {
			this.aliases.add(s);
		}
		
		init();
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public String getDescription() {
		return description;
	}

	public String getUsage() {
		return usage;
	}
	
	public Command getSubcommand(String commandName) {
		return subcommands.get(commandName);
	}

	public boolean hasSubcommand(String commandName) {
		return subcommands.containsKey(commandName);
	}

	public boolean hasSubcommands() {
		return subcommands.size() > 0;
	}
	
	public void addSubcommand(Command command) {
		command.setParent(this);
		command.setPermissions(permissions.toArray(new Permission[permissions.size()]));
		subcommands.put(command.getName(), command);
		for (String alias : command.getAliases()) {
			subcommands.put(alias, command);
		}
	}

	public Command getParent() {
		return parent;
	}

	public void setParent(Command parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}
	
	public void setModule(CommandModule module) {
		this.module = module;
		for (Command c : subcommands.values()) {
			c.setModule(module);
			c.moduleInit();
		}
		moduleInit();
	}
	
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public boolean isUsableDMs() {
		return usableDMs;
	}

	public void setUsableDMs(boolean usableDMs) {
		this.usableDMs = usableDMs;
	}
	
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Permission... permissions) {
		for (Permission p : permissions) {
			this.permissions.add(p);
		}
		for (Command c : subcommands.values()) {
			c.setPermissions(permissions);
		}
	}
	
	public List<Permission> getPermissionsRequired() {
		return permissionsToRun;
	}

	public void setPermissionsRequired(Permission... permissions) {
		for (Permission p : permissions) {
			this.permissionsToRun.add(p);
		}
		for (Command c : subcommands.values()) {
			c.setPermissionsRequired(permissions);
		}
	}
	
	public static String getPrefix(Message message) {
		return message.isFromGuild() ? Server.get(message).getPrefix() : Iku.DEFAULT_PREFIX;
	}
	
	public static Color getColor(Message message) {
		return message.isFromGuild() ? Server.get(message).getColor() : Iku.EMBED_COLOR;
	}
	
	public static Map<String, Pair<String, ArgumentType>> arguments = new LinkedHashMap<String, Pair<String, ArgumentType>>();
	
	public static void addArgument(String shorthand, String fullName, ArgumentType type) {
		arguments.put(fullName, Pair.of(shorthand, type));
	}
	
	public static Pair<Object, String[]> getArgument(String[] args, String fullName, ArgumentType type) {
		List<String> argsList = new ArrayList<String>(Arrays.asList(args));
		MutableTriple<Object, Integer, Integer> returnValue = ArgumentType.process(args, fullName, type);
		int alreadyRemoved = 0;
		for (int i = returnValue.getMiddle(); i <= returnValue.getRight() && i >= 0; i++) {
			argsList.remove(i - alreadyRemoved);
			alreadyRemoved++;
		}
		
		return Pair.of(returnValue.getLeft(), argsList.toArray(new String[argsList.size()]));
	}
	
	public static Object processArgument(Message message, String[] args, String fullName, ArgumentType type) {
		Pair<Object, String[]> value = getArgument(args, fullName, type);
		if (value.getLeft() == null) {
			Iku.sendError(message, "You must provide a value for " + fullName);
			return null;
		}
		
		args = value.getRight();
		return value.getLeft();
	}
	
	public Map<String, Object> processArguments(Message message, String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<String> keys = new ArrayList<String>(arguments.keySet());
		for (int i = 0; i < arguments.size(); i++) {
			String name = keys.get(i);
			Pair<String, ArgumentType> info = arguments.get(name);
			Object processed = processArgument(message, args, name, info.getRight());
			if (processed == null) return null;
			map.put(name, processed);
		}
		return map;
	}
	
	
	public enum ArgumentType {
		INTEGER, STRING;
		public static MutableTriple<Object, Integer, Integer> process(String args[], String fullName, ArgumentType type) {
			MutableTriple<String, Integer, Integer> parsable = findParsable(args, fullName, type);
			TypeProcessor processor = type.getProcessor();
			
			if (processor.matches(parsable.getLeft())) {
				return MutableTriple.of(processor.parse(parsable.getLeft()), parsable.getMiddle(),  parsable.getRight());
			} else {
				return MutableTriple.of(null, -1, -1);
			}
		}
		
		public TypeProcessor getProcessor() {
			switch(this) {
			case INTEGER:
				return new IntegerProcessor();
			case STRING:
				return new StringProcessor();
			default:
				return null;
			}
		}
		
		public static MutableTriple<String, Integer, Integer> findParsable(String[] args, String fullName, ArgumentType type) {
			String shorthand = arguments.get(fullName).getLeft();
			
			String toParse = "a";
			int indexStart = -1;
			int indexEnd = -1;
			
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				
				// ex. "n:10" and "num:10"
				boolean found = arg.startsWith(shorthand + ":") || arg.startsWith(fullName + ":");
				if (arg.startsWith(shorthand + ":")) {
					toParse = arg.substring(shorthand.length() + 1);
				} else if (arg.startsWith(fullName + ":")) {
					toParse = arg.substring(fullName.length() + 1);
				}
				
				if (found) {
					// Compile a list of strings to stop at (to make sure we don't bleed over into other arguments)
					List<String> forbidden = new ArrayList<String>();
					for (String name : arguments.keySet()) {
						Pair<String, ArgumentType> info = arguments.get(name);
						forbidden.add(name);
						forbidden.add(info.getLeft());
					}
					
					if (toParse.isBlank() && i + 1 < args.length) {
						i++;
						toParse = args[i];
					}
					
					while (i < args.length) {
						Iku.log(fullName + ": " + toParse);
						if (i + 1 < args.length) i++;
						boolean leave = false;
						for (String s : forbidden) {
							if (args[i].startsWith(s + ":")) {
								leave = true;
								break;
							}
						}
						if (leave) break;
						
						if (type.getProcessor().matches(toParse)
								&& (i + 1 < args.length && !type.getProcessor().matches(toParse + " " + args[i]))) break;
						
						toParse += " " + args[i];
						Iku.log("2" + fullName + ": " + toParse);
						
					}
					indexEnd = i;
				}
				
			}
			Iku.log("Returned " + toParse.strip());
			return MutableTriple.of(toParse.strip(), indexStart, indexEnd);
		}
	}
	
	public static String getQuoteArg(String[] args, int i) {
		List<Integer> quoteIndexes = new ArrayList<Integer>();
		if (args.length >= 1) {
			int index = 0;
			for (String arg : args) {
				for (int j = 0; j < arg.length(); j++) {
					char c = arg.charAt(j);
					if (c == '"') {
						quoteIndexes.add(index + j);
					}
				}
				index += arg.length() + 1;
			}
			
			if (quoteIndexes.size() <= i * 2 + 1) return null;
			String isolated = IkuUtil.arrayToString(args, " ").substring(quoteIndexes.get(i * 2), quoteIndexes.get(i * 2 + 1));
			isolated = isolated.substring(1, isolated.length());
			return isolated.strip();
		} else {
			return null;
		}
	}
	
	public static String getFullStringArg(String[] args) {
		if (args.length >= 1) {
			return IkuUtil.arrayToString(args, " ");
		} else {
			return null;
		}
	}
	
	/**
	 * Gets a command as it would appear in a help menu
	 * @param prefix The prefix to use
	 * @return How a command appears in a help menu
	 */
	public String view(String prefix) {
		StringBuilder str = new StringBuilder();
		str.append("`");
		str.append(prefix + getFullName(true));
		if (!getUsage().isBlank()) str.append(" " + getUsage());
		str.append("`");
		str.append("\n");
		str.append(getDescription().replace("${prefix}", prefix));
		str.append("\n\n");
		return str.toString();
	}
	
	/**
	 * Gets the command's full name including parents
	 * @return The command's full name without aliases (ex. command subcommand subcommand)
	 */
	public String getFullName() {
		return getFullName(false);
	}
	
	/**
	 * Gets the command's full name including parents
	 * @param aliases Whether or not to display aliases
	 * @return The command's full name (ex. command|alias|alias subcommand|alias subcommand)
	 */
	public String getFullName(boolean aliases) {
		if (parent != null) {
			StringBuilder str = new StringBuilder();
			str.append(parent.getFullName());
			if (aliases) 
				for (String alias : parent.getAliases()) {
					str.append("|" + alias);
				}
			str.append(" " + this.getName());
			if (aliases) 
				for (String alias : getAliases()) {
					str.append("|" + alias);
				}
			return str.toString();
		} else {
			StringBuilder str = new StringBuilder();
			str.append(this.getName());
			if (aliases) {
				for (String alias : getAliases()) {
					str.append("|" + alias);
				}
			}
			
			return str.toString();
		}
	}
	
	public boolean meetsArgCount(Message message, String[] args, int count) {
		if (args.length < count) {
			Iku.sendError(message, "Not enough arguments");
			return false;
		}
		return true;
	}
	
	public boolean canRun(Message message) {
		return getReason(message) == null;
	}
	
	public String getReason(Message message) {
		if (message.isFromGuild()) {

			// User does not have enough permissions
			if (permissions != null) {
				for (Permission p : permissions) {
					if (!message.getMember().hasPermission(p)) {
						return "Not enough permissions; need the " + p.toString() + " permission.";
					}
				}
			}

			// Bot does not have enough permissions
			if (permissionsToRun != null) {
				Member self = Iku.getMember(message.getGuild());
				for (Permission p : permissionsToRun) {
					if (!self.hasPermission(p)) return "Not enough permissions; need the " + p.toString() + " permission.";
				}
			}
			
		} else {
			if (!usableDMs) return "This command is not available in DMs";
		}
		
		if (admin && !message.getAuthor().getId().equalsIgnoreCase("218525899535024129")) return "You are not a bot admin!";
		
		return null;
	}
	
	public abstract void run(String args[], Message message);
	public void init() {}
	public void moduleInit() {}
}
