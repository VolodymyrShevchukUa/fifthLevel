package com.shpp;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

    AtomicInteger count;
    private final MongoDatabase mongoDatabase;
    private List<Goods> goods;
    private List<Market> markets;
    static StopWatch stopWatch = new StopWatch();

    Logger logger = LoggerFactory.getLogger(Generator.class);

    LinkedList<Document> list = new LinkedList<>();

//    LinkedList<InsertOneModel> linkedList = new LinkedList<>();


    public Generator(MongoDatabase mongoDatabase, AtomicInteger count) {
        this.mongoDatabase = mongoDatabase;
        this.count = count;
    }

    public void generate() {
        DocReader docReader = new DocReader();
        MongoCollection<Document> collection = mongoDatabase.getCollection("storage");
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
                        .setMarket(markets.get(random.nextInt(markets.size())))).filter(g -> isValid(g,validator)).limit(MAX_SIZE)
                .forEach(t -> insertIntoDB(t, storage, collection));
        if (!list.isEmpty()) {
            collection.insertMany(list);
        }
        factory.close();
        logger.info("Total saved goods {} rps = {}", count, (count.get() / (stopWatch.taken() / 1000d)));
        logger.info("Finish Generation, Time of generation =:".concat(stopWatch.stop() + "").concat("ms"));

    }


    public void insertIntoDB(Balance balance, Storage storage, MongoCollection mongoCollection) {
        storage.setGoodsCategory(balance.getGoods().getCategory().getName()).setGoodsName(balance.getGoods().getName())
                .setGoodsPrice(balance.getGoods().getPrice()).setMarket(balance.getMarket());
        Document market = new Document("address", storage.getMarket().getAddress()).append("name", storage.getMarket().getName());
        Document storageDoc = new Document("market", market)
                .append("goodsCategory", storage.getGoodsCategory())
                .append("goodsName", storage.getGoodsName())
                .append("goodsPrice", storage.getGoodsPrice());
//        mongoCollection.insertOne(storageDoc);

//        linkedList.add(new InsertOneModel<>(storageDoc));
//        if(linkedList.size() > 99999){
//            mongoCollection.bulkWrite(linkedList);
//            linkedList.clear();
//        }
        list.add(storageDoc);

        if (list.size() > 50000) {
            mongoCollection.insertMany(list);
            logger.info("{} products has PUTTED", count);
            list.clear();
        }

        if (count.addAndGet(1) % 1000 == 0) {
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



