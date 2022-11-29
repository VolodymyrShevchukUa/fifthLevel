package com.shpp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.shpp.dto.Balance;
import org.bson.Document;
import org.slf4j.LoggerFactory;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);

    static LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    static Logger rootLogger = (Logger) loggerContext.getLogger("org.mongodb.driver");
    static StopWatch stopWatch = new StopWatch();
    private AtomicInteger counter = new AtomicInteger(0);
    private Config config = new Config();


    public static void main(String[] args) {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.WARN);
        logger.info("Start of the program");
        Main main = new Main();
        main.startGeneration(args);
        //        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //        executorService.submit(() -> main.startGeneration(args));
        //       executorService.submit(() -> main.startGeneration(args));
        //        executorService.shutdown();
    }

    private void startGeneration(String[] args) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI(config.getURL()));
        MongoDatabase mongoDatabase = mongoClient.getDatabase(config.getDB_NAME());
        //   MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("storage"); Поки не потрібне
        MongoCollection<Document> collection = mongoDatabase.getCollection("storage");
        FindIterable<Document> document = collection.find();
//        for (Document document1:document){
//            String json = document1.toJson();// Воно парсить ще й айдішку...
//            try {
//                Balance balance = new ObjectMapper().readValue(json,Balance.class);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }
        Generator generator = new Generator(mongoDatabase, counter);
        generator.generate();
        mongoClient.close();

    }
}
