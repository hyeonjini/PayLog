package com.kumoh.paylog2.dto;

import java.sql.Date;

public class ListItemDto {
    int amount;
    String subscribe;
    String type;

    public ListItemDto(int amount, String subscribe, String type) {
        this.amount = amount;
        this.subscribe = subscribe;
        this.type = type;
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
