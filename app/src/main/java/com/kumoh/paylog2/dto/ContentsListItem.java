package com.kumoh.paylog2.dto;

public interface ContentsListItem {
    public int getViewType();
    public String getDate();
    void setSpending(int spending);
    void setIncome(int income);
    int getSepnding();
    int getIncome();
}
