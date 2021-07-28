package net.celestialgaze.IkuBot.command.module;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import net.celestialgaze.IkuBot.command.Command;
import net.celestialgaze.IkuBot.command.TextCommand;
import net.dv8tion.jda.api.entities.Guild;

public class CustomCmdsModule extends CommandModule {

	public CustomCmdsModule(Command... commands) {
		super(CommandModules.Module.CUSTOM.getName(), commands);
	}

	public List<Command> getCustomCommands(Guild guild) {
		List<Command> output = new ArrayList<Command>();
		ModuleSettings settings = this.getSettings(guild);
		Document customCmds = settings.getSetting("commands", new Document());
		customCmds.forEach((key, value) -> {
			TextCommand cmd = new TextCommand();
			cmd.fromDocument((Document) value);
			output.add(cmd);
		});
		return output;
	}
	
	public void addCustomCommand(Guild guild, String name, String description, String text) {
		TextCommand textCommand = new TextCommand(name, description, text);
		ModuleSettings settings = this.getSettings(guild);
		
		Document customCmds = settings.getSetting("commands", new Document());
		customCmds.append(name, textCommand.asDocument());
		settings.setSetting("commands", customCmds);
	}
	
	public boolean isCustomCommand(Guild guild, String name) {
		ModuleSettings settings = this.getSettings(guild);
		
		Document customCmds = settings.getSetting("commands", new Document());
		return customCmds.containsKey(name);
	}
	
	public void removeCustomCommand(Guild guild, String name) {
		ModuleSettings settings = this.getSettings(guild);
		
		Document customCmds = settings.getSetting("commands", new Document());
		customCmds.remove(name);
		settings.setSetting("commands", customCmds);
	}
	
	
}
