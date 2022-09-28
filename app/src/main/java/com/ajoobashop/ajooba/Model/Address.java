package com.ajoobashop.ajooba.Model;

public class Address {

    String address, apartment, flatNo, hno, lat, longt, type, isDefault;

    public Address() {

    }

    public Address(String address, String apartment, String flatNo, String hno, String lat, String longt, String type, String isDefault) {
        this.address = address;
        this.apartment = apartment;
        this.flatNo = flatNo;
        this.hno = hno;
        this.lat = lat;
        this.longt = longt;
        this.type = type;
        this.isDefault = isDefault;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getHno() {
        return hno;
    }

    public void setHno(String hno) {
        this.hno = hno;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
