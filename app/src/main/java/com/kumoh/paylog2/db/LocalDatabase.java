package com.kumoh.paylog2.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;


@Database(version = 1, entities = {Account.class, History.class, Category.class})
public abstract class LocalDatabase extends RoomDatabase {

    public abstract AccountDao accountDao();
    public abstract HistoryDao historyDao();
    public abstract CategoryDao categoryDao();

    private static LocalDatabase instance = null;

    public static LocalDatabase getInstance(Context context){
        if(instance == null){
            instance = buildDatabase(context);
        }
        return instance;
    }

    private static LocalDatabase buildDatabase(final Context context){
        return Room.databaseBuilder(context.getApplicationContext(),
                LocalDatabase.class,
                "paylog-db")
                .addCallback(new Callback(){
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db){
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Add Default Category","now");
                                getInstance(context).categoryDao().insertAll(Category.populateData());
                                Log.i("Add Default Group","now");
                                getInstance(context).accountDao().insertAccount(Account.populateDefaultGroup());
                            }
                        });
                    }
                })
                .build();
    }
}
