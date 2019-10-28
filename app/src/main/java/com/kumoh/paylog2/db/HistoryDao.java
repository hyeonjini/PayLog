package com.kumoh.paylog2.db;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM History WHERE historyId = :id")
    History getHistoryById(int id);
    @Insert
    void insertHistory(History history);
    //@Query("SELECT categoryId, sum(amount) as amount FROM History WHERE kind = -1 GROUP BY categoryId LIMIT 6") //Spend by category Top 6
    //LiveData<List<SpendByCategory>> getSpendingOfCategory();
}
