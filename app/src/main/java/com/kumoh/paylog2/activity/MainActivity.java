package com.kumoh.paylog2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.viewpager.widget.ViewPager;

import android.os.AsyncTask;
import android.os.Bundle;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.MainFragmentAdapter;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.db.PurchaseGroup;
import com.kumoh.paylog2.db.PurchaseGroupDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LocalDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DB 초기화

        db = LocalDatabase.getInstance(this);
        //만약 데이터가 아무것도 없으면 default  group 추가
        new CreateDefaultGroupAsyncTask(db.purchaseGroupDao()).execute();

        ViewPager pager = findViewById(R.id.main_view_pager);
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager(), 0);
        pager.setAdapter(adapter);


    }
    private static class InsertGroupAsyncTask extends AsyncTask<PurchaseGroup, Void, Void>{

        private PurchaseGroupDao dao;
        public InsertGroupAsyncTask(PurchaseGroupDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(PurchaseGroup... purchaseGroups) {
            dao.insertPurchaseGroup(purchaseGroups[0]);

            return null;
        }
    }
    private static class CreateDefaultGroupAsyncTask extends AsyncTask<Void, Void, Void>{
        PurchaseGroupDao dao ;
        public CreateDefaultGroupAsyncTask(PurchaseGroupDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            if(dao.getRowCount() == 0){
                System.out.println("그룹 0개");
                dao.insertPurchaseGroup(new PurchaseGroup("디폴트 그룹", "디폴트 그룹"));
            }
            return null;
        }
    }
}
