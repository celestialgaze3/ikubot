package net.celestialgaze.IkuBot.command;

import org.bson.Document;

import net.celestialgaze.IkuBot.Iku;
import net.celestialgaze.IkuBot.database.IDocSavable;
import net.dv8tion.jda.api.entities.Message;

public class TextCommand extends Command implements IDocSavable {
	String text;
	public TextCommand(String name, String description, String text) {
		super(name, description, "");
		this.text = text;
	}
	
	public TextCommand() {
		super("default", "default", "");
	}

	@Override
	public void run(String[] args, Message message) {
		Iku.send(message, text);
	}

	@Override
	public Document asDocument() {
		return new Document()
				.append("name", name)
				.append("description", description)
				.append("text", text);
	}

	@Override
	public void fromDocument(Document doc) {
		this.name = doc.getString("name");
		this.description = doc.getString("description");
		this.text = doc.getString("text");
	}

	
}
