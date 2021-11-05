package com.fpoly.pro1121.userapp.model;

public class ProductOder {
    private String idUser;
    private String idProduct;
    private String name;
    private int price;
    private String urlImage;
    private int quantity;
    private int sumMoney;


    public ProductOder(String idUser, String idProduct, String name, int price, String urlImage, int quantity) {
        this.idUser = idUser;
        this.idProduct = idProduct;
        this.name = name;
        this.price = price;
        this.urlImage = urlImage;
        this.quantity = quantity;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getSumMoney() {
        return this.quantity * price;
    }

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
