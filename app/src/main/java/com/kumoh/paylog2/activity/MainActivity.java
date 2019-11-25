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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.MainFragmentAdapter;
import com.kumoh.paylog2.db.Account;
import com.kumoh.paylog2.db.AccountDao;
import com.kumoh.paylog2.db.LocalDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseUser user;
    private LocalDatabase db;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View headerView;
    private TextView idView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user =  FirebaseAuth.getInstance().getCurrentUser();

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
        headerView = navigationView.getHeaderView(0);
        idView = headerView.findViewById(R.id.header_nav_user_id);
        idView.setText(user.getEmail());

        navigationView.setNavigationItemSelectedListener(this);

        //DB 초기화
        db = LocalDatabase.getInstance(this);

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
            case R.id.nav_data_up:
                Intent intent1 = new Intent(getApplicationContext(), DataActivity.class);
                intent1.putExtra("type", 0);
                startActivity(intent1);
                break;
            case R.id.nav_data_down:
                Intent intent2 = new Intent(getApplicationContext(), DataActivity.class);
                intent2.putExtra("type", 1);
                startActivity(intent2);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
