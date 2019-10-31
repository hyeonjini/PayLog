package com.kumoh.paylog2.dto;

public class ContentsCategoryItem {
    float percent;
    String category;
    int value;

    public ContentsCategoryItem(float percent, String category, int value){
        this.percent = percent;
        this.category = category;
        this.value = value;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
