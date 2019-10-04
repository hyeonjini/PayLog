package com.kumoh.paylog2.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM Account WHERE accountId = :accountId")
    Account getAccountById(int accountId);
    @Insert
    void insertAccount(Account account);
    @Delete
    void deleteAccount(Account account);
    @Update
    void updateAccount(Account account);
    @Query("SELECT * FROM Account")
    LiveData<List<Account>> getAll();
    @Query("SELECT COUNT(accountId) FROM Account")
    int getRowCount();
}
