package net.celestialgaze.IkuBot.command;

import java.util.ArrayList;
import java.util.List;

import net.celestialgaze.IkuBot.command.commands.HelpCommand;
import net.celestialgaze.IkuBot.database.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MasterCommand extends PagedCommand {

	boolean nestedHelp = true;
	public MasterCommand(String name, String description, String usage, String... aliases) {
		super(name, description, usage, aliases);
		this.nestedHelp = true;
		init();
	}
	
	public MasterCommand(String name, String description, String usage, boolean nestedHelp, String... aliases) {
		super(name, description, usage, aliases);
		this.nestedHelp = nestedHelp;
		init();
	}
	public void init() {
		if (nestedHelp) {
			MasterCommand self = this;
			MasterCommand c = new MasterCommand("help", "List this command's subcommands", "", false) {
				
				@Override
				public MessageEmbed getUpdatedEmbed(Message message, PagedMessage pagedMsg) {
					return self.getUpdatedEmbed(message, pagedMsg);
				}
			};
			this.addSubcommand(c);
		}
	}
	
	@Override
	public MessageEmbed getUpdatedEmbed(Message message, PagedMessage pagedMsg) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(this.getFullName() + " subcommands");
		embed.setColor(Server.get(message).getColor());
		pagedMsg.setPageSize(7);
		
		List<Command> cmdList = new ArrayList<Command>(subcommands.values());
		HelpCommand.addCommands(message, pagedMsg, cmdList, embed);
		
		return embed.build();
	}

}
