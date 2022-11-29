package com.shpp.dto;

import jakarta.validation.constraints.NotNull;

public class Market {
    @NotNull // Можна причепили щоб була "вул тратата"
    private String address;
    @NotNull
    private String name;

    public Market(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public String getName() {
        return name;
    }
}
