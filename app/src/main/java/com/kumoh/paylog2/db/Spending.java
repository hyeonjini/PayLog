package com.kumoh.paylog2.db;

import android.graphics.drawable.Drawable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

@Entity(foreignKeys = @ForeignKey(entity = Account.class, parentColumns = "accountId", childColumns = "accountId",onDelete = ForeignKey.CASCADE),
        primaryKeys = {"accountId","spendingId"})
public class Spending {
    private int accountId;
    private int spendingId;

    private String place;
    private String date;
    private int amount;
    private String category;
    private String method; //결제수단

    public Spending(int accountId,int spendingId, String place, String date, int amount, String category, String method) {
        this.accountId = accountId;
        this.spendingId = spendingId;
        this.place = place;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.method = method;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getSpendingId() {
        return spendingId;
    }

    public void setSpendingId(int spendingId) {
        this.spendingId = spendingId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
