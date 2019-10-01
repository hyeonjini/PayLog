package com.kumoh.paylog2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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

        //tablayout 설정

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.white_icon_home_24px));
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.white_icon_list_24px));


        //pager 리스너
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //tab layout 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#FF000000"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
