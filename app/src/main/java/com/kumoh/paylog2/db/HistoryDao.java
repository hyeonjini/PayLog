package com.kumoh.paylog2.db;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.kumoh.paylog2.dto.ContentsMonthItem;
import com.kumoh.paylog2.dto.ContentsStatisticsCategoryItem;

import java.util.List;

@Dao
public abstract class HistoryDao {
    @Query("SELECT * FROM History WHERE historyId = :id")
    public abstract History getHistoryById(int id);
    @Insert
    public abstract void insertHistory(History history);
    //@Query("SELECT categoryId, sum(amount) as amount FROM History WHERE kind = -1 GROUP BY categoryId LIMIT 6") //Spend by category Top 6
    //LiveData<List<SpendByCategory>> getSpendingOfCategory();
    @Query("SELECT * FROM History WHERE accountId = :accountId order by date desc")
    public abstract LiveData<List<History>> getAllByAccountId(int accountId);

    //월별 수익, 지출
    @Query("select t1.m as date , t1.spending, t2.income from(select substr(date,0,8)as m , sum(amount) as spending from history where accountId = :accountId and kind = 1 group by m) as t1 join (Select substr(date,0,8)as m , sum(amount) as income from history where accountId= :accountId and kind = -1 group by m) as t2 on t1.m = t2.m")
    public abstract LiveData<List<ContentsMonthItem>> getAllByMonthAndAccountId(int accountId);

    // 해당 accountId의 fromDate 부터 toDate 까지의 history
    @Query("SELECT * FROM History WHERE accountId = :accountId and date >= :fromDate and date <= :toDate order by date desc")
    public abstract List<History> getAllFromToByAccountId(int accountId, String fromDate, String toDate);

    // 해당 accountId의 fromDate 부터 toDate 까지의 history (LiveData)
    @Query("SELECT * FROM History WHERE accountId = :accountId and date >= :fromDate and date <= :toDate order by date asc")
    public abstract LiveData<List<History>> getAllByAccountIdFromTo(int accountId, String fromDate, String toDate);

    // 해당 accountId의 카테고리 별 amount 내림차순 정렬
    @Query("SELECT SUM(amount) as amount, categoryId, kind From History WHERE accountId = :accountId GROUP BY categoryId ORDER BY amount asc")
    public abstract LiveData<List<ContentsStatisticsCategoryItem>> getGroupedListByCategory(int accountId);

    @Update
    public abstract void updateHistory(History history);

    @Query("DELETE FROM History WHERE historyId = :historyId")
    public abstract void deleteHistory(int historyId);

    //전체 History 불러오기(데이터 백업용)
    @Query("SELECT * FROM History")
    public abstract List<History> getAllHistories();

    @Query("SELECT last_insert_rowid()")
    public abstract int getLastId();

    @Transaction
    public int insertHistoryAndGetId(History history) {
        insertHistory(history);
        return getLastId();
    }
}
