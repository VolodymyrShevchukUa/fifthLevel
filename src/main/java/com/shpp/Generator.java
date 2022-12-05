package com.shpp;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneModel;
import com.shpp.dto.Balance;
import com.shpp.dto.Goods;
import com.shpp.dto.Market;
import com.shpp.dto.Storage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.activemq.util.StopWatch;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Generator {
    private static final int MAX_SIZE = 500000;
    public static final int BATCH_SIZE = 2000;
    public static final int checked_counter = 10000;

    AtomicInteger count;
    private final MongoCollection<Document> mongoCollection;
    private List<Goods> goods;
    private List<Market> markets;
    static StopWatch stopWatch = new StopWatch();

    Logger logger = LoggerFactory.getLogger(Generator.class);

    LinkedList<Document> list = new LinkedList<>();

    LinkedList<InsertOneModel<Document>> batch = new LinkedList<>();

    Document market;

    Document storageDoc;


    public Generator(MongoCollection<Document> mongoCollection, AtomicInteger count) {
        this.mongoCollection = mongoCollection;
        this.count = count;
    }

    public void generate() {
        DocReader docReader = new DocReader();

        goods = docReader.getGoods();
        markets = docReader.getMarkets();
        Storage storage = new Storage();

        Random random = new Random();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        logger.info("Start Generation");
        stopWatch.restart();
        Stream.generate(Balance::new)
                .map(g -> g.setGoods(goods.get(random.nextInt(goods.size())))
                        .setMarket(markets.get(random.nextInt(markets.size())))).filter(g -> isValid(g, validator)).limit(MAX_SIZE)
                .forEach(t -> insertIntoDB(t, storage, mongoCollection));
//        if (!list.isEmpty()) {
//            mongoCollection.insertMany(list);
//        }
        factory.close();
        logger.info("Total saved goods {} rps = {}", count, (count.get() / (stopWatch.taken() / 1000d)));
        logger.info("Finish Generation, Time of generation =:".concat(stopWatch.stop() + "").concat("ms"));

    }


    public void insertIntoDB(Balance balance, Storage storage, MongoCollection<Document> mongoCollection) {
        storage.setGoodsCategory(balance.getGoods().getCategory().getName()).setGoodsName(balance.getGoods().getName())
                .setGoodsPrice(balance.getGoods().getPrice()).setMarket(balance.getMarket());
        market = new Document("address", storage.getMarket().getAddress()).append("name", storage.getMarket().getName());

        storageDoc = new Document("market", market)
                .append("goodsCategory", storage.getGoodsCategory())
                .append("goodsName", storage.getGoodsName())
                .append("goodsPrice", storage.getGoodsPrice());


//        batch.add(new InsertOneModel<>(storageDoc));
//        if((count.incrementAndGet()% BATCH_SIZE) == 0){
//            mongoCollection.bulkWrite(batch);
//            batch.clear();
//        }

//
        list.add(storageDoc);
        if ((count.incrementAndGet() % 10000) == 0) {
            mongoCollection.insertMany(list);
            list.clear();
        }

        if(count.get() % checked_counter == 0){
            logger.info("{} products has generated", count);
        }




    }

    public boolean isValid(Balance balance, Validator validator) {
        Set<ConstraintViolation<Market>> marketValidator = validator.validate(balance.getMarket());
        Set<ConstraintViolation<Goods>> goodsValidator = validator.validate(balance.getGoods());
        return marketValidator.isEmpty() && goodsValidator.isEmpty();
    }
}


//        String json;
//        try {
//            json = objectMapper.writeValueAsString(storage);
//            mongoCollection.insertOne(Document.parse(json));
//            mongoCollection.insertOne(new Document().append("market",
//                    new Document("address",storage.getMarket().getAddress()).append("name",storage.getMarket().getName())));
//            list.add(Document.parse(json));
//            if(list.size() == 10000){
//                mongoCollection.bulkWrite(list);
//               mongoCollection.insertMany(list);
//                list.clear();
//            }


//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }



