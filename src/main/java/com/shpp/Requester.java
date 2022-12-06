package com.shpp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.shpp.dto.Market;
import org.bson.Document;

import java.util.Arrays;

public class Requester {

    public static final String COUNT = "count";

    public Market getResult(MongoCollection<Document> collection, String category){
        Market result;
       result = collection.aggregate(
                Arrays.asList(
                        Aggregates.match(Filters.eq("goodsCategory",category)),
                        Aggregates.project(
                                Projections.fields(
                                Projections.include("market"),
                                Projections.include("goodsCategory")
                                ,Projections.computed(COUNT,new Document("$add",1)))),
                        Aggregates.group("$market", Accumulators.sum(COUNT,1)),
                        Aggregates.sort(new Document(COUNT,-1)), // Вроді робочий варік
                        Aggregates.limit(1)
                )
        ).map(this::mapMarket).first();
        return result;
    }

    private Market mapMarket(Document document){
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        Market result;
        try {
            json = objectMapper.writeValueAsString(document.get("_id"));
            result = objectMapper.readValue(json,Market.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
