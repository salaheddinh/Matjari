package com.salaheddin.store.models;

import java.io.Serializable;

public class Image implements Serializable{
    private String id;
    private String photo;

    public Image() {
    }

    public Image(String id, String photo) {
        this.id = id;
        this.photo = photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
