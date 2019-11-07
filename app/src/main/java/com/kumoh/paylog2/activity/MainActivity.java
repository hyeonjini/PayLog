package com.kumoh.paylog2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.MainFragmentAdapter;
import com.kumoh.paylog2.db.Account;
import com.kumoh.paylog2.db.AccountDao;
import com.kumoh.paylog2.db.LocalDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private LocalDatabase db;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //화면 최대 크기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.white_icon_menu_24dp);

        //Navigation 설정
        drawerLayout = findViewById(R.id.main_drawer);

        navigationView = findViewById(R.id.main_nav);
        navigationView.setNavigationItemSelectedListener(this);

        //DB 초기화
        db = LocalDatabase.getInstance(this);
        //만약 데이터가 아무것도 없으면 default  group 추가
        new CreateDefaultAccountAsyncTask(db.accountDao()).execute();

        ViewPager pager = findViewById(R.id.main_view_pager);
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager(), 0);
        pager.setAdapter(adapter);

        //tablayout 설정

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.white_icon_home_24px));
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(Color.parseColor("#2b90d9"), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.white_icon_list_24px));

        //pager 리스너
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //tab layout 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(Color.parseColor("#2b90d9"), PorterDuff.Mode.SRC_IN);
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
    private static class InsertAccountAsyncTask extends AsyncTask<Account, Void, Void>{

        private AccountDao dao;
        public InsertAccountAsyncTask(AccountDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Account... accounts) {
            dao.insertAccount(accounts[0]);

            return null;
        }
    }
    private static class CreateDefaultAccountAsyncTask extends AsyncTask<Void, Void, Void>{
        AccountDao dao ;
        public CreateDefaultAccountAsyncTask(AccountDao dao){
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            if(dao.getRowCount() == 0){
                dao.insertAccount(new Account(0,"디폴트그룹","기본 생성",1));
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.nav_document:
                Intent intent = new Intent(getApplicationContext(), FileActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
