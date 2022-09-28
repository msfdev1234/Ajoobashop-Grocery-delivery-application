package com.ajoobashop.ajooba.Model;

public class orders {

    String orderId, day, address, deliveryCharge, grandTotal, latitude, longitude, name, paymentStatus,
            timeSlot, totalAmount, fullDay, itemCount, status_Order, user_status_Order, date_Single, DeliveryPartner,
            type, hno, apartment, flatNo ;

    public orders() {
    }

    public orders(String orderId, String day, String address, String deliveryCharge, String grandTotal, String latitude, String longitude, String name, String paymentStatus, String timeSlot, String totalAmount, String fullDay, String itemCount, String status_Order, String user_status_Order, String date_Single, String DeliveryPartner) {
        this.orderId = orderId;
        this.day = day;
        this.address = address;
        this.deliveryCharge = deliveryCharge;
        this.grandTotal = grandTotal;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.paymentStatus = paymentStatus;
        this.timeSlot = timeSlot;
        this.totalAmount = totalAmount;
        this.fullDay = fullDay;
        this.itemCount = itemCount;
        this.status_Order = status_Order;
        this.user_status_Order = user_status_Order;
        this.date_Single = date_Single;
        this.DeliveryPartner = DeliveryPartner;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFullDay() {
        return fullDay;
    }

    public void setFullDay(String fullDay) {
        this.fullDay = fullDay;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }

    public String getStatus_Order() {
        return status_Order;
    }

    public void setStatus_Order(String status_Order) {
        this.status_Order = status_Order;
    }

    public String getUser_status_Order() {
        return user_status_Order;
    }

    public void setUser_status_Order(String user_status_Order) {
        this.user_status_Order = user_status_Order;
    }

    public String getDate_Single() {
        return date_Single;
    }

    public void setDate_Single(String date_Single) {
        this.date_Single = date_Single;
    }

    public String getDeliveryPartner() {
        return DeliveryPartner;
    }

    public void setDeliveryPartner(String deliveryPartner) {
        DeliveryPartner = deliveryPartner;
    }
}
