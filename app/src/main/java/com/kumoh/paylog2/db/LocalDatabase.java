package com.kumoh.paylog2.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kumoh.paylog2.dto.ListItemDto;

@Database(version = 1 , entities = {Account.class, Spending.class, Income.class})
public abstract class LocalDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();
    public abstract SpendingDao spendingDao();
    public abstract IncomeDao incomeDao();

    private static LocalDatabase instance;

    public static LocalDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, "paylog-db")
                    .build();
        }
        return instance;
    }
}
