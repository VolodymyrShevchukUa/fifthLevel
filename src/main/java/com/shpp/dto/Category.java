package com.shpp.dto;

import jakarta.validation.constraints.NotNull;

public class Category {
    @NotNull
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
