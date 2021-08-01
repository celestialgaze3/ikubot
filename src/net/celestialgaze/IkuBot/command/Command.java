package net.celestialgaze.IkuBot.command;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.IkuUtil;
import net.celestialgaze.IkuBot.command.module.CommandModule;
import net.celestialgaze.IkuBot.database.Server;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public abstract class Command {
	public static Map<String, Command> baseCommands = new HashMap<String, Command>();
	
	protected String name;
	protected String description;
	protected String usage;
	
	List<Permission> permissions = new ArrayList<Permission>();
	List<Permission> permissionsToRun = new ArrayList<Permission>();
	protected boolean inheritsModulePermissions = false;
	protected boolean usableDMs = true;
	
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
		
		return null;
	}
	
	public abstract void run(String args[], Message message);
	public void init() {}
	public void moduleInit() {}
}
