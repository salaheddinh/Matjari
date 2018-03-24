package com.salaheddin.store.models;

import java.util.ArrayList;


public class UserAddress {
    private String id;
    private String name;
    private String note;
    private String longitude;
    private String latitude;
    private ArrayList<TelephoneNumber> telephoneNumbers;

    public UserAddress(){

    }

    public UserAddress(String id, String name, String note, String longitude, String latitude, ArrayList<TelephoneNumber> telephoneNumbers) {
        this.id = id;
        this.name = name;
        this.note = note;
        this.longitude = longitude;
        this.latitude = latitude;
        this.telephoneNumbers = telephoneNumbers;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setTelephoneNumbers(ArrayList<TelephoneNumber> telephoneNumbers) {
        this.telephoneNumbers = telephoneNumbers;
    }

    public String getNote() {
        return note;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public ArrayList<TelephoneNumber> getTelephoneNumbers() {
        return telephoneNumbers;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.name = number;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
