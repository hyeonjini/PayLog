package com.kumoh.paylog2.db;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface ImageDao {
    @Insert
    void insertImage(Image image);
}
