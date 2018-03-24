package com.salaheddin.store.models;

import java.io.Serializable;
import java.util.ArrayList;


public class Cart implements Serializable {
    private String id;
    private String price;
    private String orderStatus;
    private String orderDate;
    private String isOrdered;
    private String cancellationNote;
    private String itemsCount;
    private ArrayList<Product> products;
    private String currency;

    public Cart() {
    }

    public Cart(String id, String price, String orderStatus, String orderDate, String isOrdered,
                String cancellationNote,String itemsCount, ArrayList<Product> products,String currency) {
        this.id = id;
        this.price = price;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.isOrdered = isOrdered;
        this.cancellationNote = cancellationNote;
        this.products = products;
        this.itemsCount = itemsCount;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setItemsCount(String itemsCount) {
        this.itemsCount = itemsCount;
    }

    public String getItemsCount() {
        return itemsCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public void setIsOrdered(String isOrdered) {
        this.isOrdered = isOrdered;
    }

    public void setCancellationNote(String cancellationNote) {
        this.cancellationNote = cancellationNote;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getIsOrdered() {
        return isOrdered;
    }

    public String getCancellationNote() {
        return cancellationNote;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
