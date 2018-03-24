package com.salaheddin.store.models;

import java.io.Serializable;

public class Brand implements Serializable {
    private String id;
    private String icon;
    private String name;

    public Brand() {
    }

    public Brand(String id, String icon, String name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
