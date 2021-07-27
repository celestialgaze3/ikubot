package net.celestialgaze.IkuBot.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import net.celestialgaze.IkuBot.Iku;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.requests.RestAction;

public class UserProfile extends DatabaseElement {
	private static Map<Long, HashMap<Long, UserProfile>> cache = new HashMap<Long, HashMap<Long, UserProfile>>();
	private static List<Long> cachedServers = new ArrayList<Long>();
	long serverId;
	long userId;
	
	int experience;
	private UserProfile(long serverId, long userId, boolean load) {
		this.serverId = serverId;
		this.userId = userId;
		if (load) load();
	}
	private UserProfile(long serverId, long userId) {
		this.serverId = serverId;
		this.userId = userId;
		load();
	}
	
	public int getExperience() {
		return experience;
	}
	
	public void setExperience(int experience) {
		this.experience = experience;
		save(Database.profile, "experience", experience);
	}
	
	public void addExperience(int experience) {
		try {
			setExperience(Math.addExact(getExperience(), experience));
		} catch (ArithmeticException e) {
			setExperience(Integer.MAX_VALUE);
		}
	}
	
	public static UserProfile get(long serverId, long userId) {
		if (cache.containsKey(serverId) && cache.get(serverId).containsKey(userId)) {
			return cache.get(serverId).get(userId);
		} else {
			if (Iku.bot.getGuildById(serverId) == null) {
				Iku.error("Unable to find guild with id " + serverId + " when getting user profile");
				return null; 
			}
			UserProfile profile = new UserProfile(serverId, userId);
			Iku.log("Loaded user profile for user " + userId + " for server " + serverId);
			if (!cache.containsKey(serverId)) cache.put(serverId, new HashMap<Long, UserProfile>());
			cache.get(serverId).put(userId, profile);
			return profile;
		}
	}
	
	/**
	 * Get all of the UserProfiles for a server
	 * @param serverId The ID of the server to get UserProfiles for
	 * @return All of the UserProfiles in the server
	 */
	public static List<UserProfile> getServer(long serverId) {
		List<UserProfile> list = new ArrayList<UserProfile>();
		if (!cache.containsKey(serverId)) cache.put(serverId, new HashMap<Long, UserProfile>());
		if (cachedServers.contains(serverId)) {
			return new ArrayList<UserProfile>(cache.get(serverId).values());
		} else {
			FindIterable<Document> userIterator = Database.profile.find(Filters.eq("serverId", serverId));
			userIterator.forEach(doc -> {
				long userId = doc.getLong("userId");
				UserProfile profile = new UserProfile(serverId, userId, false);
				profile.loadFromDoc(doc);
				cache.get(serverId).put(userId, profile);
				list.add(profile);
			});
			cachedServers.add(serverId);
			Iku.log("Loaded all user profiles for server " + serverId);
		}
		return list;
	}
	
	public RestAction<Member> retrieveMember() {
		return Iku.bot.getGuildById(serverId).retrieveMemberById(userId);
	}

	public long getUserIdLong() {
		return userId;
	}

	@Override
	protected void load() {
		if (loaded) return;
		Document doc = findDocument(Database.profile);
		loadFromDoc(doc);
	}
	
	private void loadFromDoc(Document doc) {
		experience = doc.getInteger("experience", 0);
	}

	@Override
	protected Object getId() {
		return serverId + "-" + userId;
	}

	@Override
	protected Document findDocument(MongoCollection<Document> collection) {
		Document doc = collection.find(Filters.eq("serverId", serverId)).filter(Filters.eq("userId", userId)).first();
		if (doc == null) {
			collection.insertOne(getDefaultDocument());
			return getDefaultDocument();
		}
		return doc;
	}
	
	@Override
	protected void save(MongoCollection<Document> collection, String propertyName, Object value) {
		Bson updateOperation = Updates.set(propertyName, value);
		collection.updateOne(Filters.and(Filters.eq("serverId", serverId), Filters.eq("userId", userId)), updateOperation);
	}
	
	@Override
	protected Document getDefaultDocument() {
		return new Document()
				.append("serverId", serverId)
				.append("userId", userId)
				.append("experience", 0);
	}

}
