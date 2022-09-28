package com.ajoobashop.ajooba.Model;

public class Category {
    private String categoryName, cid, date, image, time;

    public Category() {
    }

    public Category(String categoryName, String cid, String date, String image, String time) {
        this.categoryName = categoryName;
        this.cid = cid;
        this.date = date;
        this.image = image;
        this.time = time;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
