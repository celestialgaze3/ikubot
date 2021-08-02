package net.celestialgaze.IkuBot.command;

import java.util.Map;

import net.celestialgaze.IkuBot.util.Iku;
import net.dv8tion.jda.api.entities.Message;

public class RollCommand extends Command {

	public RollCommand() {
		super("roll",
			  "Rolls a random number up to the range",
			  "<min:int> <max:int>");
		addArgument("mi", "min", ArgumentType.INTEGER);
		addArgument("ma", "max", ArgumentType.INTEGER);
		addArgument("s", "string", ArgumentType.STRING);
	}

	@Override
	public void run(String[] args, Message message) {
		Map<String, Object> inputs = this.processArguments(message, args);
		if (inputs == null) return;
		
		Iku.sendSuccess(message, "" + "min: " + (int) inputs.get("min") + " max: " + (int) inputs.get("max"));
		Iku.sendSuccess(message, "" + "str: " + inputs.get("string"));
	}

}
