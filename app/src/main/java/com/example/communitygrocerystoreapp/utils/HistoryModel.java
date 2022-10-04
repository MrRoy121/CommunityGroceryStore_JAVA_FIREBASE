package com.example.communitygrocerystoreapp.utils;

public class HistoryModel {
    String amount, items, uid;

    public HistoryModel(String amount, String items, String uid) {
        this.amount = amount;
        this.items = items;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }


}
