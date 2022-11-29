package com.shpp;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shpp.dto.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.activemq.util.StopWatch;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Generator {
    private static final int MAX_SIZE = 1500;

    AtomicInteger count;
    private MongoDatabase database;
    private List<Goods> goods;
    private List<Market> markets;
    static StopWatch stopWatch = new StopWatch();

    Logger logger = LoggerFactory.getLogger(Generator.class);


    public Generator(MongoDatabase connection, AtomicInteger count) {
        this.database = connection;
        this.count = count;
    }

    public void generate() {

        DocReader docReader = new DocReader();
        List<Goods> goods = docReader.getGoods();
        List<Market> markets = docReader.getMarkets();
        Storage storage = new Storage();

        Random random = new Random();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        logger.info("Start Generation");
        stopWatch.restart();
        Stream.generate(Balance::new)
                .map((g) -> g.setGoods(goods.get(random.nextInt(goods.size())))
                        .setMarket(markets.get(random.nextInt(markets.size())))).limit(MAX_SIZE)
                .forEach(t -> insertIntoDB(t,storage));
        factory.close();
//            logger.info("Total saved goods {} rps = {}", count, count.get() / (stopWatch.taken() / 1000));
//            logger.info("Finish Generation, Time of generation =:".concat(stopWatch.stop() + "").concat("ms"));
    }

    public void insertIntoDB(Balance balance,Storage storage) {
        storage.setGoodsCategory(balance.getGoods().getCategory().getName()).setGoodsName(balance.getGoods().getName())
                .setGoodsPrice(balance.getGoods().getPrice()).setMarketAddress(balance.getMarket().getAddress())
                .setMarketName(balance.getMarket().getName());
        MongoCollection<Document> mongoCollection = database.getCollection("storage");
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(storage);
            mongoCollection.insertOne(Document.parse(json));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid(Balance balance, Validator validator) {
        Set<ConstraintViolation<Market>> marketValidator = validator.validate(balance.getMarket());
        Set<ConstraintViolation<Goods>> goodsValidator = validator.validate(balance.getGoods());
        return marketValidator.isEmpty() && goodsValidator.isEmpty();
    }
}




