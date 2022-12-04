package com.shpp.manager;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.shpp.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManager {

    private final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

// Можливо діч, в плані що кожен раз вертає новий клієнт, но ето нє точно.
    public MongoClient getClient() {
        Config config = new Config();
        return new MongoClient(new MongoClientURI(config.getURL()));
    }
}

