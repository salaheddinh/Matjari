package com.salaheddin.store.models;

import java.io.Serializable;


public class Store implements Serializable {
    private String id;
    private String name;
    private String logo;
    private String phone;
    private String countryId;
    private String mainStoreId;
    private String longitude;
    private String latitude;
    private String locationDescription;

    public Store() {
    }

    public Store(String id, String name, String logo, String phone, String countryId, String mainStoreId, String longitude, String latitude, String locationDescription) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.phone = phone;
        this.countryId = countryId;
        this.mainStoreId = mainStoreId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locationDescription = locationDescription;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public void setMainStoreId(String mainStoreId) {
        this.mainStoreId = mainStoreId;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getMainStoreId() {
        return mainStoreId;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLocationDescription() {
        return locationDescription;
    }
}

