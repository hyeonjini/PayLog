package com.kumoh.paylog2.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = 1 , entities = {PurchaseGroup.class})
public abstract class LocalDatabase extends RoomDatabase {
    public abstract PurchaseGroupDao purchaseGroupDao();

    private static LocalDatabase instance;

    public static LocalDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, "local-db")
                    .build();
        }
        return instance;
    }
}
