package com.kumoh.paylog2.dto;

// 통계 화면의 데이터 추출을 위한 DTO
public class ContentsStatisticsCategoryItem {
    int value;
    String category;

    public ContentsStatisticsCategoryItem(int value, String category){
        this.value = value;
        this.category = category;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
