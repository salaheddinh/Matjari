package com.salaheddin.store.models;

public class DictionaryModel {
    private String tilte;
    private String type;

    public DictionaryModel(String tilte, String type) {
        this.tilte = tilte;
        this.type = type;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTilte() {
        return tilte;
    }

    public String getType() {
        return type;
    }
}
