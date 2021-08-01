package net.celestialgaze.IkuBot.command.module;

import java.util.HashMap;
import java.util.Map;

import net.celestialgaze.IkuBot.command.commands.modules.custom.CustomCmdCommand;
import net.celestialgaze.IkuBot.command.commands.modules.xp.XpCommand;

public class CommandModules {
	public static Map<String, CommandModule> list = new HashMap<String, CommandModule>();
	
	public static CustomCmdsModule customCmds;
	
	public static void init() {
		list.put(Module.XP.getInternalName(), new XpModule(
			new XpCommand()
		));
		
		list.put(Module.CUSTOM.getInternalName(), new CustomCmdsModule(
			new CustomCmdCommand()
		));
		customCmds = (CustomCmdsModule) list.get(Module.CUSTOM.getInternalName());
	}
	
	public static enum Module {
		XP("XP"), CUSTOM("Custom");
		
		String name;
		Module(String name) {
			this.name = name;
		}
		
		public String getInternalName() {
			return name.toLowerCase();
		}
		
		public String getName() {
			return name;
		}
	}
}
