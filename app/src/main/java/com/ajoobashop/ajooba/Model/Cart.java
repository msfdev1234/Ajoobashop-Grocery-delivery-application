package com.ajoobashop.ajooba.Model;

public class Cart {

    String mrp, multiple, pid, pname, quantity, rate, units, categoryName, image, subCategoryName, userPhoneKey;

    public Cart() {
    }

    public Cart(String mrp, String multiple, String pid, String pname, String quantity, String rate, String units, String categoryName, String image, String subCategoryName, String userPhoneKey) {
        this.mrp = mrp;
        this.multiple = multiple;
        this.pid = pid;
        this.pname = pname;
        this.quantity = quantity;
        this.rate = rate;
        this.units = units;
        this.categoryName = categoryName;
        this.image = image;
        this.subCategoryName = subCategoryName;
        this.userPhoneKey = userPhoneKey;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
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

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getUserPhoneKey() {
        return userPhoneKey;
    }

    public void setUserPhoneKey(String userPhoneKey) {
        this.userPhoneKey = userPhoneKey;
    }
}
