package com.kumoh.paylog2.dto;

public class ContentsListBody implements ContentsListItem{
    final static int SPENDING_VIEW = -1;
    final static int INCOME_VIEW = 1;

    private int kind; //지출 수입 구분
    private String date;
    private int categoryId;
    private String description;
    private int amount;
    private int historyId;

    public ContentsListBody(int kind, String date, int categoryId, String description, int amount, int historyId) {
        this.kind = kind;
        this.date = date;
        this.categoryId = categoryId;
        this.description = description;
        this.amount = amount;
        this.historyId = historyId;
    }

    @Override
    public int getViewType() {
        if (kind == SPENDING_VIEW)
            return SPENDING_VIEW;
        else
        return 1;
    }

    @Override
    public String getDate() {
        return this.date;
    }

    @Override
    public void setSpending(int spending) {

    }

    @Override
    public void setIncome(int income) {

    }

    @Override
    public int getSepnding() {
        return this.amount;
    }

    @Override
    public int getIncome() {
        return this.amount;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
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

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }
}
