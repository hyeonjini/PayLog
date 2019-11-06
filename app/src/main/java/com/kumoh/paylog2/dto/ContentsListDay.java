package com.kumoh.paylog2.dto;

public class ContentsListDay {
    private int year;
    private int month;
    private int day;
    private int incomeAmt;
    private int spendingAmt;

    public ContentsListDay(int year, int month, int day, int incomeAmt, int spendingAmt) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.incomeAmt = incomeAmt;
        this.spendingAmt = spendingAmt;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getIncomeAmt() {
        return incomeAmt;
    }

    public void setIncomeAmt(int incomeAmt) {
        this.incomeAmt = incomeAmt;
    }

    public int getSpendingAmt() {
        return spendingAmt;
    }

    public void setSpendingAmt(int spendingAmt) {
        this.spendingAmt = spendingAmt;
    }
}
