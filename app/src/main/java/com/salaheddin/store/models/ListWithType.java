package com.salaheddin.store.models;

import java.util.ArrayList;

public class ListWithType {
    private ArrayList<Object> objects;
    private int type;

    public ListWithType(ArrayList<Object> objects, int type) {
        this.objects = objects;
        this.type = type;
    }

    public void setObjects(ArrayList<Object> objects) {
        this.objects = objects;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public int getType() {
        return type;
    }
}
