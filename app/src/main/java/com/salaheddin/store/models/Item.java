package com.salaheddin.store.models;

import java.io.Serializable;
import java.util.ArrayList;


public class Item implements Serializable {
    private String id;
    private Color color;
    private ArrayList<Image> image;
    private int quantity;
    private String price;
    private String newPrice;
    private Double discount;
    private boolean isWished;
    private boolean isSelected;
    private ArrayList<KeyValue> sizes;
    private String currency;

    public Item() {
    }

    public Item(String id, Color color, ArrayList<Image> image, int quantity, String price, String newPrice,
                Double discount, boolean isWished, ArrayList<KeyValue> sizes,String currency) {
        this.id = id;
        this.color = color;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.newPrice = newPrice;
        this.discount = discount;
        this.isWished = isWished;
        this.sizes = sizes;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public ArrayList<KeyValue> getSizes() {
        return sizes;
    }

    public void setWished(boolean wished) {
        isWished = wished;
    }

    public boolean isWished() {
        return isWished;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setImage(ArrayList<Image> image) {
        this.image = image;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public ArrayList<Image> getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public Double getDiscount() {
        return discount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
