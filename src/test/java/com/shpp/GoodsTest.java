package com.shpp;



import com.shpp.dto.Category;
import com.shpp.dto.Goods;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoodsTest {


    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    @Test

    void ValidationTest(){
        Goods goods = new Goods("Name", null,51.2);
        Set<ConstraintViolation<Goods>> goodsValidator = validator.validate(goods);
        Assertions.assertEquals(1, goodsValidator.size());
        assertEquals("must not be null",goodsValidator.iterator().next().getMessage());
    }
    @Test
    void ValidationPrice(){
        Goods goods = new Goods("Name", new Category("ss"),49.9);
        Set<ConstraintViolation<Goods>> goodsValidator = validator.validate(goods);
        Assertions.assertEquals(1, goodsValidator.size());
        assertEquals("must be greater than or equal to 50",goodsValidator.iterator().next().getMessage());
    }
}