package net.celestialgaze.IkuBot.database;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import net.celestialgaze.IkuBot.util.Iku;

public class Database {
	
	public static MongoClient mongoClient;
	public static MongoDatabase iku;
	public static MongoCollection<Document> bot; // Bot collection stores things related to the bot only
	public static MongoCollection<Document> profile; // Profiles are a user profile for each server they are in.
	public static MongoCollection<Document> server; // Storing data for each server
	public static MongoCollection<Document> user; // Storing data for each user
	
	public static final String IP = "localhost";
	
	public static void init() {
		mongoClient = MongoClients.create("mongodb://" + IP + ":27017");
		iku = mongoClient.getDatabase("iku");
		bot = iku.getCollection("bot");
		profile = iku.getCollection("profile");
		server = iku.getCollection("server");
		user = iku.getCollection("user");
		Iku.log("Successfully connected to the database at " + IP + ":27017");
		
		BotStats.instance = new BotStats();
		BotInfo.instance = new BotInfo();
	}
	
}
