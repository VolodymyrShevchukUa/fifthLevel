package com.shpp.dto;

import jakarta.validation.constraints.NotNull;

public class Balance {
    @NotNull
    private Market market;
    @NotNull
    private Goods goods;


    public Market getMarket() {
        return market;
    }

    public Balance setMarket(Market market) {
        this.market = market;
        return this;
    }

    public Goods getGoods() {
        return goods;
    }

    public Balance setGoods(Goods goods) {
        this.goods = goods;
        return this;
    }
}
