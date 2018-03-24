package com.salaheddin.store.models;

import java.io.Serializable;


public class Order implements Serializable {
    private String id;
    private String price;
    private String orderStatus;
    private String orderDate;
    private String isOrdered;
    private String cancellationNote;
    private String currency;

    public Order(){

    }

    public Order(String id, String price, String orderStatus, String orderDate,
                 String isOrdered, String cancellationNote,String currency) {
        this.id = id;
        this.price = price;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.isOrdered = isOrdered;
        this.cancellationNote = cancellationNote;
        this.currency = currency;
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

    public String getCurrency() {
        return currency;
    }
}
