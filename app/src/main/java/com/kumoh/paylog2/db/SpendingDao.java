package com.kumoh.paylog2.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
interface SpendingDao {
    @Query("SELECT * FROM Spending WHERE accountId = :accountId")
    LiveData<List<Spending>> getAllByAccountId(int accountId);
    @Insert
    void insertSpending(Spending spending);
    @Query("DELETE FROM Spending WHERE spendingId = :spendingId")
    void deleteBySpendingId(int spendingId);
    @Update
    void updateSpending(Spending spending);
    @Query("SELECT SUM(accountId) FROM Spending WHERE accountId = :accountId")
    LiveData<Integer> getAmountByAccountId(int accountId); //해당 그룹의 총 지출액

    @Query("SELECT count(spendingId) FROM Spending WHERE accountId = :accountId")
    int getSpendingIdCountByAccountId(int accountId);// 그룹 아이디로 spending count 구하기
}
