package com.kumoh.paylog2.db;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(foreignKeys = @ForeignKey(entity = Account.class, parentColumns = "accountId", childColumns = "accountId",onDelete = ForeignKey.CASCADE),
        primaryKeys = {"accountId","incomeId"})
public class Income {
    private int accountId;
    private int incomeId;
    private int amount;
    private String date;
    private String category;

    public Income(int accountId, int incomeId, int amount, String date, String category) {
        this.accountId = accountId;
        this.incomeId = incomeId;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(int incomeId) {
        this.incomeId = incomeId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
