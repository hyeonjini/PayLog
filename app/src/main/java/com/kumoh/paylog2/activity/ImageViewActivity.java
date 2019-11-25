package com.kumoh.paylog2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.ImageDao;
import com.kumoh.paylog2.db.LocalDatabase;

import java.io.File;

public class ImageViewActivity extends AppCompatActivity {
    private TextView pathTest;
    private String filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        pathTest = (TextView) findViewById(R.id.path_test);
        LocalDatabase db = LocalDatabase.getInstance(this);

        Intent intent = getIntent();
       String sAccountId = intent.getExtras().getString("account");
       String sHistoryId = intent.getExtras().getString("history");

       Integer[] imageInfo = new Integer[2];

       imageInfo[0] = Integer.parseInt(sAccountId);
       imageInfo[1] = Integer.parseInt(sHistoryId);

       try {
           filePath = new GetImagePath(db.imageDao()).execute(imageInfo).get();
       }catch (Exception e){
           e.printStackTrace();
       }

       File imgFile = new File(filePath);
       if(imgFile.exists()){
           Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

           ImageView imageView = (ImageView) findViewById(R.id.image_view);
           imageView.setImageBitmap(bitmap);
       }else{
           pathTest.setVisibility(View.VISIBLE);
       }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private static class GetImagePath extends AsyncTask<Integer,Void, String>{
        private ImageDao dao;
        public GetImagePath(ImageDao dao){
            this.dao = dao;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String filePath = dao.getFilePathById(integers[0], integers[1]);
            if(filePath == null)
                return "";
            return filePath;
        }
    }
}
