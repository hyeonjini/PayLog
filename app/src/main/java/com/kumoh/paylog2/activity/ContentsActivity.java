package com.kumoh.paylog2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.ContentsFragmentAdapter;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.HistoryDao;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dialog.AddSpendingHistoryDialog;

public class ContentsActivity extends AppCompatActivity implements View.OnClickListener
,ViewPager.OnPageChangeListener{
    private Animation fab_open, fab_close;
    private LocalDatabase db;
    private int selectedAccountId;
    private FloatingActionButton fab, spendingFab, incomeFab;
    private Boolean isFabOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        //toolbar
        db = LocalDatabase.getInstance(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_contents);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_icon_arrow_back_24dp);

        //데이터 수신
        Intent intent = getIntent();
        selectedAccountId = Integer.parseInt(intent.getStringExtra("selectedGroupId"));

        //Viewpager + tabLayout
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_contents);
        ContentsFragmentAdapter adapter = new ContentsFragmentAdapter(getSupportFragmentManager(), 0, selectedAccountId);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_contents);
        tabLayout.addTab(tabLayout.newTab().setText("항목"));
        tabLayout.addTab(tabLayout.newTab().setText("월별"));
        tabLayout.addTab(tabLayout.newTab().setText("달력"));
        tabLayout.addTab(tabLayout.newTab().setText("통계"));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //FloatingActionButton
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        spendingFab = (FloatingActionButton) findViewById(R.id.add_contents_fab_spending);
        incomeFab = (FloatingActionButton) findViewById(R.id.add_contents_fab_income);
        fab = (FloatingActionButton) findViewById(R.id.add_contents_fab);
        fab.setOnClickListener(this);
        spendingFab.setOnClickListener(this);
        incomeFab.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_contents_fab:
                anim();

                Toast.makeText(this, "Receipt Activity 실행", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add_contents_fab_spending:
                anim();
                AddSpendingHistoryDialog dialog = new AddSpendingHistoryDialog(this);

                dialog.setAddSpendingHistoryDialogListener(new AddSpendingHistoryDialog.AddSpendingHistoryDialogListener() {
                    @Override
                    public void onAddButtonClicked(int kind, String date, int category, String description, int amount) {
                        History spending = new History(selectedAccountId,-1,date,category,description,amount);
                        new InsertHistory(db.historyDao()).execute(spending);
                    }
                });
                dialog.show();
                break;
            case R.id.add_contents_fab_income:
                anim();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            fab.show();
        } else {
            fab.hide();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private static class InsertHistory extends AsyncTask<History, Void, Void>{
        HistoryDao dao;
        public InsertHistory(HistoryDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(History... histories) {
            dao.insertHistory(histories[0]);
            return null;
        }
    }
    public void anim() {
        if (isFabOpen) {
            spendingFab.startAnimation(fab_close);
            incomeFab.startAnimation(fab_close);
            spendingFab.setClickable(false);
            incomeFab.setClickable(false);
            isFabOpen = false;
        } else {
            spendingFab.startAnimation(fab_open);
            incomeFab.startAnimation(fab_open);
            spendingFab.setClickable(true);
            incomeFab.setClickable(true);
            isFabOpen = true;
        }
    }
}
