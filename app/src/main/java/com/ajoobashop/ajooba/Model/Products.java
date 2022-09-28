package com.ajoobashop.ajooba.Model;

public class Products {

    String categoryName, subCategoryName,date, image, mrp1, mrp2, pid, pname, quantity1, quantity2, rate1, rate2, time, units1, units2, status;

    public Products() {
    }

    public Products(String categoryName, String subCategoryName, String date, String image, String mrp1, String mrp2, String pid, String pname, String quantity1, String quantity2, String rate1, String rate2, String time, String units1, String units2, String status) {
        this.categoryName = categoryName;
        this.subCategoryName = subCategoryName;
        this.date = date;
        this.image = image;
        this.mrp1 = mrp1;
        this.mrp2 = mrp2;
        this.pid = pid;
        this.pname = pname;
        this.quantity1 = quantity1;
        this.quantity2 = quantity2;
        this.rate1 = rate1;
        this.rate2 = rate2;
        this.time = time;
        this.units1 = units1;
        this.units2 = units2;
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
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

    public String getMrp1() {
        return mrp1;
    }

    public void setMrp1(String mrp1) {
        this.mrp1 = mrp1;
    }

    public String getMrp2() {
        return mrp2;
    }

    public void setMrp2(String mrp2) {
        this.mrp2 = mrp2;
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

    public String getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(String quantity1) {
        this.quantity1 = quantity1;
    }

    public String getQuantity2() {
        return quantity2;
    }

    public void setQuantity2(String quantity2) {
        this.quantity2 = quantity2;
    }

    public String getRate1() {
        return rate1;
    }

    public void setRate1(String rate1) {
        this.rate1 = rate1;
    }

    public String getRate2() {
        return rate2;
    }

    public void setRate2(String rate2) {
        this.rate2 = rate2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUnits1() {
        return units1;
    }

    public void setUnits1(String units1) {
        this.units1 = units1;
    }

    public String getUnits2() {
        return units2;
    }

    public void setUnits2(String units2) {
        this.units2 = units2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
