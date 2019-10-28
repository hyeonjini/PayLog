package com.kumoh.paylog2.db;


import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class History {
    @ForeignKey(entity = Account.class, parentColumns = "accountId", childColumns = "accountId")
    private int accountId;
    @PrimaryKey(autoGenerate = true)
    private int historyId;
    private int kind;
    private String date;
    private int categoryId;
    private String description;
    private int amount;

    public History(int accountId, int kind, String date, int categoryId, String description, int amount) {
        this.accountId = accountId;
        this.kind = kind;
        this.date = date;
        this.categoryId = categoryId;
        this.description = description;
        this.amount = amount;

    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
