package com.kumoh.paylog2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.contents.ContentsFragmentAdapter;
import com.kumoh.paylog2.db.LocalDatabase;

public class ContentsActivity extends AppCompatActivity implements View.OnClickListener
,ViewPager.OnPageChangeListener{
    private LocalDatabase db;

    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);
        //toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_contents);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_icon_arrow_back_24dp);

        //데이터 수신
        Intent intent = getIntent();
        String selectedGroupId = intent.getStringExtra("selectedGroupId");

        //Viewpager + tabLayout
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager_contents);
        ContentsFragmentAdapter adapater = new ContentsFragmentAdapter(getSupportFragmentManager(), 0);
        viewPager.setAdapter(adapater);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_contents);
        tabLayout.addTab(tabLayout.newTab().setText("항목"));
        tabLayout.addTab(tabLayout.newTab().setText("월별"));
        tabLayout.addTab(tabLayout.newTab().setText("달력"));
        tabLayout.addTab(tabLayout.newTab().setText("통계"));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //FloatingActionButton
        fab = (FloatingActionButton) findViewById(R.id.add_contents_fab);
        fab.setOnClickListener(this);

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
                Toast.makeText(this, "Receipt Activity 실행", Toast.LENGTH_SHORT).show();
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
}
