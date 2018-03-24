package com.salaheddin.store.models;


public class User {
    private String id;
    private String session;
    private String firstName;
    private String lastName;
    private String email;
    private String hashKey;
    private String relatedStoreId;
    private boolean isAdmin;
    private int cartItems;

    public User(String id, String session, String firstName, String lastName, String email, String hashKey, String relatedStoreId, boolean isAdmin,int cartItems) {
        this.id = id;
        this.session = session;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hashKey = hashKey;
        this.relatedStoreId = relatedStoreId;
        this.isAdmin = isAdmin;
        this.cartItems = cartItems;
    }

    public void setCartItems(int cartItems) {
        this.cartItems = cartItems;
    }

    public int getCartItems() {
        return cartItems;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public void setRelatedStoreId(String relatedStoreId) {
        this.relatedStoreId = relatedStoreId;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getId() {
        return id;
    }

    public String getSession() {
        return session;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getHashKey() {
        return hashKey;
    }

    public String getRelatedStoreId() {
        return relatedStoreId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
