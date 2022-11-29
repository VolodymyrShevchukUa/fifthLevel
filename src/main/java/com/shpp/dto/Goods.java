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

    public Goods(String name, Category category, double price) {
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }


}
