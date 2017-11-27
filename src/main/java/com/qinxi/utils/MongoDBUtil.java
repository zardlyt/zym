package com.qinxi.utils;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {
	private static MongoClient mongoClient = null;
	private MongoDBUtil() {}

	static {
		synchronized (MongoDBUtil.class) {
			initDBPrompties();
		}
	}

	public static MongoCollection<Document> getCollection(String dbName,String collName) {
		MongoDatabase db = mongoClient.getDatabase(dbName);
		return db.getCollection(collName);
	}
	
	public static MongoCollection<Document> getDBCollection(String dbName,String collName) {
		return mongoClient.getDatabase(dbName).getCollection(collName);
	}
	
	 /** 
	 * 初始化连接池 
	 */  
	private static void initDBPrompties() {  
		try {  
//			mongoClient = new MongoClient(SpiderConfig.mongoHost, SpiderConfig.mongoPort);
			mongoClient = new MongoClient("10.9.201.190", 27017);
		} catch (MongoException e) {  
			e.printStackTrace();
		}  
    }

	public static void main(String[] args) {
		MongoCursor<Document> iter = MongoDBUtil.getCollection("douban20161220", "douban_media_basic_info").find().iterator();
		while(iter.hasNext()){
			System.out.println(iter.next().getString("name"));
		}
	}
}