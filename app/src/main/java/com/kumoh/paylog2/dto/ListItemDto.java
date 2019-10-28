package com.kumoh.paylog2.dto;

public class ListItemDto {
    String date;
    int amount;
    String subscribe;
    String type;

    public ListItemDto(String date, int amount, String subscribe, String type) {
        this.date = date;
        this.amount = amount;
        this.subscribe = subscribe;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
