package com.salaheddin.store.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private String id;
    private String name;
    private String desc;
    private int canBeShipped;
    private String shippingFee;
    private ArrayList<Item> items;
    private Type type;
    private Brand brand;
    private Store store;
    private ArrayList<SubCategory> categories;
    private Season season;
    private String itemsCount;
    private ArrayList<Product> relatedProducts;

    public Product() {

    }

    public Product(String id, String name, String desc, int canBeShipped, String shippingFee, ArrayList<Item> items, Type type,
                   Brand brand, Store store, ArrayList<SubCategory> categories, Season season,String itemsCount,ArrayList<Product> relatedProducts) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.canBeShipped = canBeShipped;
        this.shippingFee = shippingFee;
        this.items = items;
        this.type = type;
        this.brand = brand;
        this.store = store;
        this.categories = categories;
        this.season = season;
        this.itemsCount = itemsCount;
        this.relatedProducts = relatedProducts;
    }

    public ArrayList<Product> getRelatedProducts() {
        return relatedProducts;
    }

    public void setItemsCount(String itemsCount) {
        this.itemsCount = itemsCount;
    }

    public String getItemsCount() {
        return itemsCount;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setCategories(ArrayList<SubCategory> categories) {
        this.categories = categories;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public int getCanBeShipped() {
        return canBeShipped;
    }

    public Type getType() {
        return type;
    }

    public Brand getBrand() {
        return brand;
    }

    public Store getStore() {
        return store;
    }

    public ArrayList<SubCategory> getCategories() {
        return categories;
    }

    public Season getSeason() {
        return season;
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

    public void setCanBeShipped(int canBeShipped) {
        this.canBeShipped = canBeShipped;
    }

    public void setShippingFee(String shippingFee) {
        this.shippingFee = shippingFee;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
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

    public int isCanBeShipped() {
        return canBeShipped;
    }

    public String getShippingFee() {
        return shippingFee;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
