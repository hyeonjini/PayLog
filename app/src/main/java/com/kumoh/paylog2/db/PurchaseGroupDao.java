package com.kumoh.paylog2.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PurchaseGroupDao {
    @Insert
    void insertPurchaseGroup(PurchaseGroup p);

    @Delete
    void deletePurchaseGroup(PurchaseGroup p);

    @Query("SELECT * FROM purchasegroup WHERE id = :id")
    PurchaseGroup getPurchaseGroupById(int id);

    @Query("SELECT * FROM purchasegroup")
    LiveData<List<PurchaseGroup>> getAll();

    @Query("SELECT COUNT(id) FROM purchasegroup")
    int getRowCount();
}
