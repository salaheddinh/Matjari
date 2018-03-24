package com.salaheddin.store.models;

import java.util.ArrayList;

public class FilterTypes {
    private String id;
    private String title;
    private int type;
    private ArrayList<KeyValue> values;
    private ArrayList<KeyValue> colors;
    private String min;
    private String max;

    public FilterTypes() {
    }

    public FilterTypes(String id, String title, int type, ArrayList<KeyValue> values, ArrayList<KeyValue> colors, String min, String max) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.values = values;
        this.colors = colors;
        this.min = min;
        this.max = max;
    }

    public ArrayList<KeyValue> getColors() {
        return colors;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setValues(ArrayList<KeyValue> values) {
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public ArrayList<KeyValue> getValues() {
        return values;
    }
}
