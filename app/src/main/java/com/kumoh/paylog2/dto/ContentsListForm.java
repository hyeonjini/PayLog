package com.kumoh.paylog2.dto;

public class ContentsListForm {
    private String contents;
    private String date;
    private int amount;
    private int VIEW_TYPE;  // 0, -1, 1
    private int total = 0;

    public ContentsListForm(String contents, String date, int amount, int VIEW_TYPE) {
        this.contents = contents;
        this.date = date;
        this.amount = amount;
        this.VIEW_TYPE = VIEW_TYPE;
    }

    public ContentsListForm(String date, int VIEW_TYPE, int total) {
        this.date = date;
        this.VIEW_TYPE = VIEW_TYPE;
        this.total = total;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
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

    public int getVIEW_TYPE() {
        return VIEW_TYPE;
    }

    public void setVIEW_TYPE(int VIEW_TYPE) {
        this.VIEW_TYPE = VIEW_TYPE;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
