package com.example.easycoin;

public class CoinModel {
    private String name;
    private String symbol;
    private double price;
    private double change24h;
    private boolean favourite;

    public CoinModel(String name, String symbol, double price, double change24h, boolean favourite) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.change24h = change24h;
        this.favourite = favourite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getChange24h() {
        return change24h;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean getFavourite()  { return favourite;};
}
