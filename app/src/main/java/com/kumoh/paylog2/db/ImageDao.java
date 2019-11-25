package com.kumoh.paylog2.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ImageDao {
    @Insert
    void insertImage(Image image);
    @Query("SELECT filePath FROM Image WHERE accountId=:accountId and historyId = :historyId")
    String getFilePathById(int accountId, int historyId);
}
