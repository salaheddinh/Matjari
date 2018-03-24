package com.salaheddin.store.models;

import java.io.Serializable;

public class MainType implements Serializable{
    private String id;
    private String title;
    private int photo;

    public MainType() {
    }

    public MainType(String id, String title, int photo) {
        this.id = id;
        this.title = title;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public int getPhoto() {
        return photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }
}
