package com.kumoh.paylog2.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kumoh.paylog2.dto.AccountInfo;

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
    //Account 정보 획득
    @Query("SELECT t1.accountId, t1.name, t1.isMain, t1.budget, t1.spending , t2.income  FROM (SELECT a.accountId, a.name, a.isMain, a.budget, t.spending FROM account as a left outer JOIN (SELECT accountId , sum(amount) as spending FROM history where kind = 1 or kind = 3 group by accountId)as t ON a.accountId = t.accountId)as t1 left outer JOIN (SELECT accountId, sum(amount) as income FROM history where kind = 0 or kind = 2 group by accountId) as t2 on t1.accountId = t2.accountId")
    LiveData<List<AccountInfo>> getAccountInfoList();

    //MainAccount 찾기
    @Query("SELECT t1.accountId, t1.name, t1.isMain, t1.budget, t1.spending, t2.income FROM (SELECT a.accountId, a.name, a.isMain, a.budget, t.spending FROM account as a left outer JOIN (SELECT accountId , sum(amount) as spending FROM history where kind = 1 or kind = 3 group by accountId)as t ON a.accountId = t.accountId)as t1 left outer JOIN (SELECT accountId, sum(amount) as income FROM history where kind = 0 or kind = 2 group by accountId) as t2 on t1.accountId = t2.accountId WHERE t1.isMain =1")
    LiveData<AccountInfo> getMainAccountInfo();

}
