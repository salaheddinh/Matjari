package com.salaheddin.store.models;

import java.io.Serializable;


public class SubCategory implements Serializable{
    private String id;
    private String name;
    private String image;
    private String description;

    public SubCategory() {
    }

    public SubCategory(String id, String name, String image, String description) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
