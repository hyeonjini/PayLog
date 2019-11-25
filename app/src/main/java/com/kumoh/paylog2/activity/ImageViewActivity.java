package com.kumoh.paylog2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kumoh.paylog2.R;

public class ImageViewActivity extends AppCompatActivity {
    private TextView pathTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        pathTest = (TextView) findViewById(R.id.path_test);

        Intent intent = getIntent();
        pathTest.setText(intent.getExtras().getString("imagePath"));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
