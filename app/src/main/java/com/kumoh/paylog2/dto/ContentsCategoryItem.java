package com.kumoh.paylog2.dto;

// 카테고리 선택 화면을 위한 DTO
public class ContentsCategoryItem {
    int id;
    String category;
    int kind;

    public ContentsCategoryItem(int id, String category, int kind) {
        this.id = id;
        this.category = category;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}
