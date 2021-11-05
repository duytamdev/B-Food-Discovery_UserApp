package com.fpoly.pro1121.userapp.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private String userID;
    private List<ProductOrder> lisstProductOrder;
    private int unitPrice;

    public Order(String id, String userID, List<ProductOrder> lisstProductOrder, int unitPrice) {
        this.id = id;
        this.userID = userID;
        this.lisstProductOrder = lisstProductOrder;
        this.unitPrice = unitPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<ProductOrder> getLisstProductOrder() {
        return lisstProductOrder;
    }

    public void setLisstProductOrder(List<ProductOrder> lisstProductOrder) {
        this.lisstProductOrder = lisstProductOrder;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }
}
