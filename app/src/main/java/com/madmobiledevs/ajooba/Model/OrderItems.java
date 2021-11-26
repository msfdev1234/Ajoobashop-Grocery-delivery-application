package com.madmobiledevs.ajooba.Model;

import android.widget.TextView;

public class OrderItems {

    public String categoryName, multiple, pid, name, quantity, rate, units, images, subCategoryName;

    public OrderItems(){
    }

    public OrderItems(String categoryName, String multiple, String pid, String name, String quantity, String rate, String units, String images, String subCategoryName) {
        this.categoryName = categoryName;
        this.multiple = multiple;
        this.pid = pid;
        this.name = name;
        this.quantity = quantity;
        this.rate = rate;
        this.units = units;
        this.images = images;
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}
