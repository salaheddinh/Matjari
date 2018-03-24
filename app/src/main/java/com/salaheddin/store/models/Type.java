package com.salaheddin.store.models;

import android.util.Log;

import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.network.WebUrls;

import java.io.Serializable;

public class Type implements Serializable{
    private String id;
    private String title;
    private String photo;

    public Type() {
    }

    public Type(String id, String title, String photo) {
        this.id = id;
        this.title = title;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public String getPhoto(String size) {
        //Log.d("imagess", WebUrls.IMAGES_SERVER_URL + AppConstants.categories + "/" + size + "/" + photo);
        return photo;
    }
}
