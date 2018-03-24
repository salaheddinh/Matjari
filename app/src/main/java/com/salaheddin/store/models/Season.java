package com.salaheddin.store.models;

import java.io.Serializable;


public class Season implements Serializable {
    private String id;
    private String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Season(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Season() {
    }
}
