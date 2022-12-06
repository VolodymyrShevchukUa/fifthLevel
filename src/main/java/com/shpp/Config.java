package com.shpp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private final String URL;
    private final String DB_NAME;
    private final String COLLECTION;


    public Config() {
        Properties properties = new Properties();
        try(FileInputStream file = new FileInputStream("config.properties")) {
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        URL = properties.getProperty("data_base_url");
        DB_NAME = properties.getProperty("data_base_name");
        COLLECTION = properties.getProperty("collection");
    }

    public String getURL() {
        return URL;
    }

    public String getDB_NAME() {
        return DB_NAME;
    }

    public String getCollection() {
        return COLLECTION;
    }
}
