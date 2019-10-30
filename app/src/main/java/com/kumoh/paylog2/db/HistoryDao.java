package com.kumoh.paylog2.db;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.kumoh.paylog2.dto.ContentsMonthItem;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM History WHERE historyId = :id")
    History getHistoryById(int id);
    @Insert
    void insertHistory(History history);
    //@Query("SELECT categoryId, sum(amount) as amount FROM History WHERE kind = -1 GROUP BY categoryId LIMIT 6") //Spend by category Top 6
    //LiveData<List<SpendByCategory>> getSpendingOfCategory();
    @Query("SELECT * FROM History WHERE accountId = :accountId order by date desc")
    LiveData<List<History>> getAllByAccountId(int accountId);

    //월별 수익, 지출
    @Query("select t1.m as date , t1.spending, t2.income from(select substr(date,0,8)as m , sum(amount) as spending from history where accountId = :accountId and kind = 1 group by m) as t1 join (Select substr(date,0,8)as m , sum(amount) as income from history where accountId= :accountId and kind = -1 group by m) as t2 on t1.m = t2.m")
    LiveData<List<ContentsMonthItem>> getAllByMonthAndAccountId(int accountId);

}
