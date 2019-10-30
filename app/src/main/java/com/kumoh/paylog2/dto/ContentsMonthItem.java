package com.kumoh.paylog2.dto;

public class ContentsMonthItem {
    String date;
    int income = 0;
    int spending = 0;

    public ContentsMonthItem(String date, int income, int spending) {
        this.date = date;
        this.income = income;
        this.spending = spending;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getSpending() {
        return spending;
    }

    public void setSpending(int spending) {
        this.spending = spending;
    }
}
