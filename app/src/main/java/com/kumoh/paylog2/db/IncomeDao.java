package com.kumoh.paylog2.db;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
interface IncomeDao {
    @Query("SELECT * FROM Income WHERE accountId = :accountId")
    List<Income> getAllByAccountId(int accountId);

    @Query("SELECT SUM(amount) FROM Income WHERE accountId = :accountId")
    int getIncomeAmountByAccountId(int accountId);
}
