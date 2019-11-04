package com.kumoh.paylog2.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    private int categoryId;
    private String name;
    private int kind;   // [Base Category 수입:0 지출:1][User Category 수입:2 지출:3]

    public Category(String name, int kind) {
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
                // 수입 기본 카테고리
                new Category("급여",0),
                new Category("용돈",0),
                new Category("금융수입",0),
                new Category("기타수입",0),
                // 지출 기본 카테고리
                new Category("식비",1),
                new Category("카페/간식",1),
                new Category("술/유흥",1),
                new Category("생활",1),
                new Category("문화/여가",1),
                new Category("패션/쇼핑",1),
                new Category("교통",1),
                new Category("자동차",1),
                new Category("주거",1),
                new Category("통신비",1),
                new Category("의료/건강",1),
                new Category("금융",1),
                new Category("여행",1),
                new Category("교육",1),
                new Category("자녀",1),
                new Category("경조사/선물",1)
        };
    }
}
