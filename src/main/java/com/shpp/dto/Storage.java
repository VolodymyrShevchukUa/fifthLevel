package com.shpp.dto;

public class Storage {


    private Market market;

    private String goodsName;

    private String goodsCategory;

    private double goodsPrice;


    public Storage(){
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

    public Market getMarket(){return market;}

    public Storage setMarket(Market market) {
        this.market = market;
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
