package com.shpp;

import com.mongodb.client.MongoCollection;
import com.shpp.dto.Balance;
import com.shpp.dto.Category;
import com.shpp.dto.Goods;
import com.shpp.dto.Market;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.bson.Document;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.atomic.AtomicInteger;



class GeneratorTest {

    MongoCollection<Document> mongoCollection = Mockito.mock(MongoCollection.class);

    AtomicInteger counter = new AtomicInteger(0);

    Generator generator = new Generator(mongoCollection,counter);

    Balance balance = new Balance().setGoods(new Goods("Blance", new Category("Пиво"),23.80))
            .setMarket(new Market("ss","Вул. Симоненка"));

    @Test
    void filterTest(){
       try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory();) {
           Validator validator = factory.getValidator();
           Assert.assertEquals(false,generator.isValid(balance,validator));
       }
    }
}