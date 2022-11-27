package com.shpp.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shpp.db.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    public Connection getConnection() {
        Config config = new Config();

        try {
            logger.info("load driver");
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("load driver error",e);
            throw new RuntimeException(e);
        }

        Connection connection;
        try {
            connection = DriverManager.getConnection(config.getURL(), config.getNAME(), config.getPASS());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
