package com.kumoh.paylog2.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int categoryId;
    private String name;
    private int kind;   // [Base Category 수입:0 지출:1][User Category 수입:2 지출:3]

    public Category(int categoryId, String name, int kind) {
        this.categoryId = categoryId;
        this.name = name;
        this.kind = kind;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public static Category[] populateData(){
        return new Category[]{
                //new Category(0,2)
        };
    }
}
