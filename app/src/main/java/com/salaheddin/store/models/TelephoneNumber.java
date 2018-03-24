package com.salaheddin.store.models;

public class TelephoneNumber {
    private String id;
    private String number;

    public TelephoneNumber(){

    }

    public TelephoneNumber(String id, String number) {
        this.id = id;
        this.number = number;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}
