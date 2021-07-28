package net.celestialgaze.IkuBot.database;

import org.bson.Document;

public interface IDocSavable {
	public Document asDocument();
	public void fromDocument(Document doc);
}
