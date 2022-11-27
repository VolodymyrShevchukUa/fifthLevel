package com.shpp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class Goods {
    @NotNull
    private final Category category;
    @NotNull
    private final String name;
    @Min(50)
    private final double price;

    public Goods(Category category, String name, double price) {
        this.category = category;
        this.name = name;
        this.price = price;
    }


}
