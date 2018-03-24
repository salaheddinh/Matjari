package com.salaheddin.store.models;

import java.io.Serializable;

public class SliderItem implements Serializable {
    private String id;
    private String photo;
    private String categoryId;
    private int localImage;

    public SliderItem() {
    }

    public SliderItem(String id, String photo,String categoryId) {
        this.id = id;
        this.photo = photo;
        this.categoryId = categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setLocalImage(int localImage) {
        this.localImage = localImage;
    }

    public int getLocalImage(){
        return localImage;
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
