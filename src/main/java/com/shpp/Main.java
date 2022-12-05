package com.shpp;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Config config = new Config();

    private MongoClient mongoClient;

    MongoCollection<Document> mongoCollection;





    public static void main(String[] args) {
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.WARN);

        Main main = new Main();
        logger.info("Start of the program");
        stopWatch.restart();
        main.initCollection();

        try {
            int nThreads = Integer.parseInt(args[1]);
            ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
            for(int i = nThreads;i>0;i--){
                executorService.submit(main::startGeneration);
            }
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.MINUTES);
            main.printResult(args[0]);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        main.mongoClient.close();
//        main.startGeneration();
//        main.printResult(args[0]);
    }

    private void initCollection(){
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(config.getURL()))
                .build();
        WriteConcern wc = new WriteConcern(0).withJournal(false);
        mongoClient = MongoClients.create(settings);
        MongoDatabase mongoDatabase = mongoClient.getDatabase(config.getDB_NAME());
        mongoCollection = mongoDatabase.getCollection(config.getCollection()).withWriteConcern(wc);
    }

    private void startGeneration() {
        Generator generator = new Generator(mongoCollection, counter);
        generator.generate();
    }

    private void printResult(String category) {
        stopWatch.restart();
        Requester requester = new Requester();

        Market market = requester.getResult(mongoCollection, category);
        logger.info("Найбільше товарів категорії ".concat(category).concat(" В магазині ").concat(market.getName())
                .concat(" За адресою ").concat(market.getAddress()));
        logger.info("Швидкість запиту становить = : {} ms" ,stopWatch.stop());
        mongoClient.close();
    }
}
