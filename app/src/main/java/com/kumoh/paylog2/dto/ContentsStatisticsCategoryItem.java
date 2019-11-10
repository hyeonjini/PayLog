package com.kumoh.paylog2.dto;

// 통계 화면의 데이터 추출을 위한 DTO
public class ContentsStatisticsCategoryItem {
    int amount;
    int categoryId;
    int kind;

    public ContentsStatisticsCategoryItem(int amount, int categoryId, int kind) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.kind = kind;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
