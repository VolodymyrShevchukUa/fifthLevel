package com.shpp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shpp.dto.Market;
import org.apache.activemq.util.StopWatch;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class Main {
    static Logger logger = LoggerFactory.getLogger(Main.class);

    static StopWatch stopWatch = new StopWatch();
    private AtomicInteger counter = new AtomicInteger(0);
    private Config config = new Config();


    public static void main(String[] args) {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.WARN);

        Main main = new Main();

        MongoClient mongoClient = new MongoClient(new MongoClientURI(main.config.getURL()));
        MongoDatabase mongoDatabase = mongoClient.getDatabase(main.config.getDB_NAME());
        MongoCollection<Document> collection = mongoDatabase.getCollection("storage");
        logger.info("Start of the program");
        stopWatch.restart();
        try (mongoClient) {
            Integer nThreads = Integer.parseInt(args[1]);
            ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
            for(int i = nThreads;i>0;i--){
                executorService.submit(() -> main.startGeneration(mongoDatabase));
            }
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.MINUTES);
            main.printResult(collection,args[0]);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void startGeneration(MongoDatabase mongoDatabase) {
        Generator generator = new Generator(mongoDatabase, counter);
        generator.generate();
    }

    private void printResult(MongoCollection collection, String category) {
        stopWatch.restart();
        Requester requester = new Requester();
        Market market = requester.getResult(collection, category);
        logger.info("Найбільше товарів категорії ".concat(category).concat(" В магазині ").concat(market.getName())
                .concat(" За адресою ").concat(market.getAddress()));
        logger.info("Швидкість запиту становить = : {} ms" ,stopWatch.stop());
    }
}
