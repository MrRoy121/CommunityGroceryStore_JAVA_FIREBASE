package com.example.communitygrocerystoreapp.utils;

public class ProductModel {
    String uid, name, img, cate,price;

    public ProductModel(String uid, String name, String img, String cate, String price) {
        this.uid = uid;
        this.name = name;
        this.img = img;
        this.cate = cate;
        this.price = price;
    }

    public ProductModel(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
