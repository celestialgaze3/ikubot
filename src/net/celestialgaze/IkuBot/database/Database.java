package net.celestialgaze.IkuBot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class Database {
	
	public static MongoClient mongoClient;
	public static final String IP = "192.168.0.17";
	
	public static void init() {
		mongoClient = MongoClients.create("mongodb://" + IP + ":27017");
	}
}
