package com.salaheddin.store.models;

public class DictionaryItem {
    private String id;
    private String nameEn;
    private String nameAr;
    private String icon;
    private String name;
    private int type;
    private boolean selected;

    public DictionaryItem(){

    }

    public DictionaryItem(String id, String nameEn, String nameAr, String icon, String name, int type,boolean selected) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.icon = icon;
        this.name = name;
        this.type = type;
        this.selected = selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }
}
