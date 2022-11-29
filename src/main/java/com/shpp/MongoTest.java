package com.shpp;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoTest {

    public static void main(String[] args) {

        MongoCredential mongoCredential = MongoCredential.createCredential("VolodymyrMongo","shop","Overlord12".toCharArray());
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://VolodymyrMongo:Overlord12@cluster0.dacztnk.mongodb.net/?retryWrites=true&w=majority"));
        MongoDatabase mongoDatabase = mongoClient.getDatabase("shop");
        MongoCollection<Document> collection = mongoDatabase.getCollection("test");
        collection.insertOne(new Document("name","pidor"));
        System.out.println();
    }
}
