package com.salaheddin.store.models;

import java.io.Serializable;
import java.util.ArrayList;


public class Offer implements Serializable{
    private String id;
    private String name;
    private String desc;
    private String offerItemsPrice;
    private String offerPrice;
    private String image;
    private int status;
    private int offersItemCount;
    private double remainingTime;
    private ArrayList<Product> offerProducts;

    public Offer() {
    }

    public Offer(String id, String name, String desc, String offerItemsPrice, String offerPrice, String image,
                 int status, int offersItemCount, double remainingTime, ArrayList<Product> offerProducts) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.offerItemsPrice = offerItemsPrice;
        this.offerPrice = offerPrice;
        this.image = image;
        this.status = status;
        this.offersItemCount = offersItemCount;
        this.remainingTime = remainingTime;
        this.offerProducts = offerProducts;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setOfferItemsPrice(String offerItemsPrice) {
        this.offerItemsPrice = offerItemsPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setOffersItemCount(int offersItemCount) {
        this.offersItemCount = offersItemCount;
    }

    public void setRemainingTime(double remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setOfferProducts(ArrayList<Product> offerProducts) {
        this.offerProducts = offerProducts;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getOfferItemsPrice() {
        return offerItemsPrice;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public String getImage() {
        return image;
    }

    public int getStatus() {
        return status;
    }

    public int getOffersItemCount() {
        return offersItemCount;
    }

    public double getRemainingTime() {
        return remainingTime;
    }

    public ArrayList<Product> getOfferProducts() {
        return offerProducts;
    }
}
