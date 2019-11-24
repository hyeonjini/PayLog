package com.kumoh.paylog2.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    LiveData<List<Category>> getEveryCategories();

    @Query("SELECT * FROM Category WHERE kind = 0 OR kind = 2")
    LiveData<List<Category>> getIncomeCategories();

    @Query("SELECT * FROM Category WHERE kind = 1 OR kind = 3")
    LiveData<List<Category>> getSpendingCategories();

    @Query("SELECT * FROM Category WHERE categoryId = :id")
    Category getCategoryById(int id);
    @Insert
    void insertAll(Category... categories);

    @Insert
    void insertCategory(Category category);

    //전체 History 불러오기(데이터 백업용)
    @Query("SELECT * FROM Category")
    List<Category> getAllCategories();
}
