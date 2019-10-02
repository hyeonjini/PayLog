package com.kumoh.paylog2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kumoh.paylog2.R;

public class ContentsActivity extends AppCompatActivity {
    TextView groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        //데이터 수신
        Intent intent = getIntent();
        String selectedGroupId = intent.getStringExtra("selectedGroupId");

        //뷰 바인딩
        groupName = (TextView) findViewById(R.id.content_group_name);
        groupName.setText("선택된 그룹 id : "+ selectedGroupId);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
