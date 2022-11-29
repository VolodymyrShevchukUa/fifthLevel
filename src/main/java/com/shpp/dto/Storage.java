package com.shpp.dto;

public class Storage {

    private String marketName;

    private String marketAddress;

    private String goodsName;

    private String goodsCategory;

    private double goodsPrice;


    public Storage(){
    }

    public String getMarketName() {
        return marketName;
    }

    public String getMarketAddress() {
        return marketAddress;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public Storage setMarketName(String marketName) {
        this.marketName = marketName;
        return this;
    }

    public Storage setMarketAddress(String marketAddress) {
        this.marketAddress = marketAddress;
        return this;
    }

    public Storage setGoodsName(String goodsName) {
        this.goodsName = goodsName;
        return this;
    }

    public Storage setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
        return this;
    }

    public Storage setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
        return this;
    }
}
