package net.celestialgaze.IkuBot.command.module;

import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.IkuBot.command.commands.modules.xp.XpCommand;
import net.celestialgaze.IkuBot.command.commands.modules.xp.XpLbCommand;

public class CommandModules {
	public static Map<String, CommandModule> list = new HashMap<String, CommandModule>();
	
	
	public static void init() {
		list.put(Module.XP.getInternalName(), new XpModule(
			new XpCommand(),
			new XpLbCommand()
		));
	}
	
	public static enum Module {
		XP;
		
		public String getInternalName() {
			return this.getName().toLowerCase();
		}
		
		public String getName() {
			return this.toString();
		}
	}
}
