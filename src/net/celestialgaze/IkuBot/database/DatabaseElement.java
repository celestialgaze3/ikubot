package net.celestialgaze.IkuBot.database;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public abstract class DatabaseElement {
	boolean loaded = false;
	
	/**
	 * Load values from the database into this instance
	 */
	protected abstract void load();
	
	/**
	 * Save a property of this instance to the database
	 * @param propertyName The name of the property
	 * @param value The value of the property
	 */
	protected void save(MongoCollection<Document> collection, String propertyName, Object value) {
		Bson updateOperation = Updates.set(propertyName, value);
		collection.updateOne(Filters.eq("id", getId()), updateOperation);
	}

	/**
	 * Get the identifier that uniquely identifies what document in the database relates to this instance
	 * @return The unique ID
	 */
	protected abstract Object getId();
	
	/**
	 * Gets a document with the default values for this instance.
	 * @return The default document
	 */
	protected abstract Document getDefaultDocument();
	
	/**
	 * Finds the document of this instance in the database
	 * @param collection The collection to search through
	 * @return The document in the database, or the default document if none is found.
	 */
	protected Document findDocument(MongoCollection<Document> collection) {
		Document doc = collection.find(Filters.eq("id", getId())).first();
		if (doc == null) {
			collection.insertOne(getDefaultDocument());
			return getDefaultDocument();
		}
		return doc;
	}
}
