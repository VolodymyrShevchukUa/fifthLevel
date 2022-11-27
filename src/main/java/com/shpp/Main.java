package com.shpp;


import com.shpp.manager.ConnectionManager;
import com.shpp.manager.Selector;
import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class Main {
    ConnectionManager connectionManager;
    static Logger logger = LoggerFactory.getLogger(Main.class);

    static StopWatch stopWatch = new StopWatch();

    private AtomicInteger counter = new AtomicInteger(0);


    public static void main(String[] args) {
        logger.info("Start of the program");
        Main main = new Main();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> main.startGeneration(args));
        executorService.submit(() -> main.startGeneration(args));
        executorService.shutdown();

    }

    private void startGeneration(String[] args) {
       connectionManager = new ConnectionManager();
        try (Connection connection = connectionManager.getConnection()) {
            Generator generator = new Generator(connection, counter);
            generator.generate();
            logger.info("Починаємо робити вибірку з БД");
            stopWatch.restart();
            logger.info("Магазин з найбільшою кількістю товарів категорії  ".concat(args[0]).concat(" Є магазин з назвою ")
                    .concat(new Selector(connection).getAddress(args[0])));
            logger.info("Тривалість запиту становить =:".concat(stopWatch.stop() + "").concat("ms"));
        } catch (SQLException e) {
            logger.error("Connection problem ", e);
            throw new RuntimeException(e);
        }
    }
}
