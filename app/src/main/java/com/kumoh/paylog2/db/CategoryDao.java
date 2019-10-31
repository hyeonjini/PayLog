package com.kumoh.paylog2.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category WHERE categoryId = :id")
    Category getCategoryById(int id);

    @Query("SELECT * FROM Category WHERE kind = 0 or 2")
    Category getIncomeCategory();

    @Query("SELECT * FROM Category WHERE kind = 1 or 3")
    Category getSpendingCategory();

    @Insert
    void insertAll(Category... categories);

    @Insert
    void insertCategory(Category category);
}
