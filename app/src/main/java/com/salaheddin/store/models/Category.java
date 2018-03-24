package com.salaheddin.store.models;

import android.util.Log;

import com.salaheddin.store.helpers.AppConstants;
import com.salaheddin.store.network.WebUrls;

import java.util.ArrayList;

public class Category {
    private String id;
    private String name;
    private String image;
    private String description;
    private ArrayList<SubCategory> subCategories;

    public Category() {
    }

    public Category(String id, String name, String image, String description,ArrayList<SubCategory> subCategories) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.subCategories = subCategories;
    }

    public void setSubCategories(ArrayList<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public ArrayList<SubCategory> getSubCategories() {
        return subCategories;
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

    public String getImage(String size) {
        Log.d("imagess",WebUrls.IMAGES_SERVER_URL + AppConstants.categories + "/" + size + "/" +image);
        return WebUrls.IMAGES_SERVER_URL + AppConstants.categories + "/" + size + "/" +image;
    }
    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
