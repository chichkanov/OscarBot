package com.chichkanov.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chichkanov on 21/10/2017.
 * telegram - @chichkanov777
 */
public class Product {

    @SerializedName("product")
    private String productName;

    @SerializedName("price")
    private double price;

    @SerializedName("actualPrice")
    private String actualPrice;

    @SerializedName("category")
    private String category;

    public Product(String productName, double price, String actualPrice, String category) {
        this.productName = productName;
        this.price = price;
        this.actualPrice = actualPrice;
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
