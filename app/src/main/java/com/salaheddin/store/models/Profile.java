package com.salaheddin.store.models;

import java.util.ArrayList;
public class Profile {
    private String id;
    private String firetName;
    private String lastName;
    private String email;
    private boolean isStoreAdmin;
    private String storeId;
    private String gender;
    private UserAddress userAddress;

    public Profile() {
    }

    public Profile(String id, String firetName, String lastName, String email, boolean isStoreAdmin, String storeId, String gender, UserAddress userAddress) {
        this.id = id;
        this.firetName = firetName;
        this.lastName = lastName;
        this.email = email;
        this.isStoreAdmin = isStoreAdmin;
        this.storeId = storeId;
        this.gender = gender;
        this.userAddress = userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFiretName(String firetName) {
        this.firetName = firetName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStoreAdmin(boolean storeAdmin) {
        isStoreAdmin = storeAdmin;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getFiretName() {
        return firetName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isStoreAdmin() {
        return isStoreAdmin;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getGender() {
        return gender;
    }
}
