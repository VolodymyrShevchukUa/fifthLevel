package com.shpp;


import com.shpp.dto.Balance;
import com.shpp.dto.Goods;
import com.shpp.dto.Market;
import com.shpp.manager.Selector;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Generator {
    private static final int MAX_SIZE = 15000;

    private static final int BATCH_SIZE = 1000;


    AtomicInteger count;
    private Connection connection;
    private List<Goods> goods;
    private List<Market> markets;
    static StopWatch stopWatch = new StopWatch();

    Logger logger = LoggerFactory.getLogger(Generator.class);


    public Generator(Connection connection, AtomicInteger count) {
        this.connection = connection;
        this.count = count;
    }

    public void generate() {
        Random random = new Random();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO goods_list VALUES (?,?)");
             ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Selector selector = new Selector(connection);

            markets = selector.getMarkets();
            goods = selector.getGoods();

            Validator validator = factory.getValidator();
            logger.info("Start Generation");
            stopWatch.restart();
            connection.setAutoCommit(false);
            Stream.generate(Balance::new)
                    .map((g) -> g.setGoods(goods.get(random.nextInt(goods.size())))
                            .setMarket(markets.get(random.nextInt(markets.size())))).filter(g -> isValid(g, validator)).limit(MAX_SIZE)
                    .forEach(t -> putOnBatch(t, preparedStatement, markets, goods));
            preparedStatement.executeBatch();
            connection.commit();
            logger.info("Total saved goods {} rps = {}", count, count.get() / (stopWatch.taken() / 1000));
            logger.info("Finish Generation, Time of generation =:".concat(stopWatch.stop() + "").concat("ms"));
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }


    public void putOnBatch(Balance balance, PreparedStatement preparedStatement, List<Market> markets, List<Goods> goods) {
        try {
            preparedStatement.setInt(1, markets.indexOf(balance.getMarket()) + 1);
            preparedStatement.setInt(2, goods.indexOf(balance.getGoods()) + 1);
            preparedStatement.addBatch();


            if (count.get() % BATCH_SIZE == 0) {
                preparedStatement.executeBatch();
                logger.info("Уже записано {}", count);
            }
            count.getAndIncrement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid(Balance balance, Validator validator) {
        Set<ConstraintViolation<Market>> marketValidator = validator.validate(balance.getMarket());
        Set<ConstraintViolation<Goods>> goodsValidator = validator.validate(balance.getGoods());
        return marketValidator.isEmpty() && goodsValidator.isEmpty();
    }
}




