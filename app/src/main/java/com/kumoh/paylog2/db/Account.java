package com.kumoh.paylog2.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Account {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int accountId;
    private int budget;
    private String name;
    private String subscribe;
    private int isMain;

    public Account(int budget, String name, String subscribe, int isMain) {
        this.budget = budget;
        this.name = name;
        this.subscribe = subscribe;
        this.isMain = isMain;
    }
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public int getIsMain() {
        return isMain;
    }

    public void setIsMain(int isMain) {
        this.isMain = isMain;
    }
}
