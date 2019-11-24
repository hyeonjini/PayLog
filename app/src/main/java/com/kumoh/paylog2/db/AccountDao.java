package com.kumoh.paylog2.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.kumoh.paylog2.dto.AccountInfo;

import java.util.List;

@Dao
public abstract class AccountDao {
    @Query("SELECT * FROM Account WHERE accountId = :accountId")
    public abstract Account getAccountById(int accountId);
    @Insert
    public abstract void insertAccount(Account account);
    @Query("DELETE FROM Account WHERE accountId = :accountId")
    public abstract void deleteAccountById(int accountId);
    @Query("UPDATE Account SET name = :accountName, subscribe = :subscribe, budget = :budget WHERE accountId = :accountId")
    public abstract void updateAccount(int accountId, String accountName, String subscribe, int budget);
    @Query("SELECT * FROM Account")
    public  abstract LiveData<List<Account>> getAll();
    @Query("SELECT COUNT(accountId) FROM Account")
    public  abstract int getRowCount();

    //Account 정보 획득
    @Query("SELECT t1.accountId, t1.name, t1.isMain, t1.budget, t1.subscribe, t1.spending, t2.income FROM (SELECT a.accountId, a.name, a.isMain, a.budget, a.subscribe, t.spending FROM account as a left outer JOIN (SELECT accountId , sum(amount) as spending FROM history where kind = 1 or kind = 3 group by accountId)as t ON a.accountId = t.accountId)as t1 left outer JOIN (SELECT accountId, sum(amount) as income FROM history where kind = 0 or kind = 2 group by accountId) as t2 on t1.accountId = t2.accountId")
    public abstract LiveData<List<AccountInfo>> getAccountInfoList();
    //MainAccount 찾기
    @Query("SELECT t1.accountId, t1.name, t1.isMain, t1.budget, t1.spending, t2.income FROM (SELECT a.accountId, a.name, a.isMain, a.budget, t.spending FROM account as a left outer JOIN (SELECT accountId , sum(amount) as spending FROM history where kind = 1 or kind = 3 group by accountId)as t ON a.accountId = t.accountId)as t1 left outer JOIN (SELECT accountId, sum(amount) as income FROM history where kind = 0 or kind = 2 group by accountId) as t2 on t1.accountId = t2.accountId WHERE t1.isMain =1")
    public abstract LiveData<AccountInfo> getMainAccountInfo();

    @Transaction
    public void wipeOutAndAppointMainAcocuntById(int accountId) {
        wipeOutMainAccount();
        appointMainAccountById(accountId);
    }

    @Query("UPDATE Account SET isMain = 0 WHERE isMain = 1")
    public abstract void wipeOutMainAccount();
    @Query("UPDATE Account SET isMain = 1 WHERE accountId = :accountId")
    public abstract void appointMainAccountById(int accountId);

    //전체 Account 불러오기(데이터 백업용)
    @Query("SELECT * FROM Account")
    public abstract List<Account> getAllAccounts();
}
