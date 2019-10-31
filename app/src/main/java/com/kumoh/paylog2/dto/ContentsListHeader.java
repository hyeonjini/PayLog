package com.kumoh.paylog2.dto;

public class ContentsListHeader implements ContentsListItem{
    final static int HEADER_VIEW = 0;
    int spendingAmount = 0;
    int incomeAmount = 0;
    String date;

    public ContentsListHeader(String date){
        this.date = date;
    }
    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public void setSpending(int spending) {
        this.spendingAmount = spending;
    }

    @Override
    public void setIncome(int income) {
        this.incomeAmount = income;
    }

    @Override
    public int getSepnding() {
        return this.spendingAmount;
    }

    @Override
    public int getIncome() {
        return this.incomeAmount;
    }

    public int getSpendingAmount() {
        return spendingAmount;
    }

    public void setSpendigAmount(int spendingAmount) {
        this.spendingAmount = spendingAmount;
    }

    public int getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(int incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public void setDate(String date) {
        this.date = date;
    }
}