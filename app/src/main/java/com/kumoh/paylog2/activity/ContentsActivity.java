package com.kumoh.paylog2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.dto.HistoryVO;
import com.kumoh.paylog2.fragment.contents.ContentsListFragment;
import com.kumoh.paylog2.fragment.contents.ContentsMonthFragment;
import com.kumoh.paylog2.util.RequestHttpURLConnection;
import com.kumoh.paylog2.adapter.contents.ContentsFragmentAdapter;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.HistoryDao;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dialog.AddIncomeHistoryDialog;
import com.kumoh.paylog2.dialog.AddSpendingHistoryDialog;
import com.kumoh.paylog2.dto.ContentsCategoryItem;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContentsActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener,
        ContentsListFragment.RecyclerScrollStateChangedListener, ContentsMonthFragment.RecyclerScrollStateChangedListener {
    private LocalDatabase db;
    private int selectedAccountId;
    private ViewPager viewPager;

    // FAB
    private FloatingActionButton addFab, spendingFab, incomeFab, imageFab;
    private Animation fab_open, fab_close, all_fab_open, all_fab_close;
    private Boolean isAddFabVisible = false;
    private Boolean isAllFabVisible = true;

    // 기본 카테고리 리스트
    List<ContentsCategoryItem> spendingLists;
    List<ContentsCategoryItem> incomeLists;
    // 사진이 저장될 경로
    private String mCurrentPhotoPath;
    // 원본 사진을 앱 폴더에 저장해 두다가 crop 을 마치면 삭제함
    private File originalFile;

    final static int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents);

        db = LocalDatabase.getInstance(this);

        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_contents);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_icon_arrow_back_24dp);

        // 카테고리 초기화
        initCategories();

        //데이터 수신
        Intent intent = getIntent();
        selectedAccountId = Integer.parseInt(intent.getStringExtra("selectedGroupId"));

        //Viewpager + tabLayout
        viewPager = (ViewPager) findViewById(R.id.view_pager_contents);
        ContentsFragmentAdapter adapter = new ContentsFragmentAdapter(getSupportFragmentManager(), 0, selectedAccountId);

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_contents);
        tabLayout.addTab(tabLayout.newTab().setText("항목"));
        tabLayout.addTab(tabLayout.newTab().setText("월별"));
        tabLayout.addTab(tabLayout.newTab().setText("달력"));
        tabLayout.addTab(tabLayout.newTab().setText("통계"));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(tabSelectedListener);

        //FloatingActionButton
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        all_fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.all_fab_open);
        all_fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.all_fab_close);

        spendingFab = (FloatingActionButton) findViewById(R.id.add_contents_fab_spending);
        incomeFab = (FloatingActionButton) findViewById(R.id.add_contents_fab_income);
        addFab = (FloatingActionButton) findViewById(R.id.add_contents_fab);
        imageFab = (FloatingActionButton) findViewById(R.id.capture_image_fab);

        spendingFab.setOnClickListener(this);
        incomeFab.setOnClickListener(this);
        addFab.setOnClickListener(this);
        imageFab.setOnClickListener(this);
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
                addFabAnim();
                break;
            case R.id.add_contents_fab_spending:
                addFabAnim();
                AddSpendingHistoryDialog addSpendingHistoryDialog = new AddSpendingHistoryDialog(this, spendingLists);

                addSpendingHistoryDialog.setAddSpendingHistoryDialogListener(new AddSpendingHistoryDialog.AddSpendingHistoryDialogListener() {
                    @Override
                    public void onAddButtonClicked(int kind, String date, int category, String description, int amount) {
                        History spending = new History(selectedAccountId,kind,date,category,description,amount);
                        new InsertHistory(db.historyDao()).execute(spending);
                    }
                });
                addSpendingHistoryDialog.show();
                break;
            case R.id.add_contents_fab_income:
                addFabAnim();
                AddIncomeHistoryDialog addIncomeHistoryDialog = new AddIncomeHistoryDialog(this, incomeLists);

                addIncomeHistoryDialog.setAddIncomeHistoryDialogListener(new AddIncomeHistoryDialog.AddIncomeHistoryDialogListener() {
                    @Override
                    public void onAddButtonClicked(int kind, String date, int category, String description, int amount) {
                        History income = new History(selectedAccountId,kind,date,category,description,amount);
                        new InsertHistory(db.historyDao()).execute(income);
                    }
                });
                addIncomeHistoryDialog.show();
                break;
            case R.id.capture_image_fab:
                // 6.0 마쉬멜로우 이상일 경우에는 권한 체크 후 권한 요청
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
                dispatchTakePictureIntent();
                break;
        }
    }

    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();

            viewPager.setCurrentItem(position);
            if(position < 2) {
                if(isAllFabVisible != true) { // 보여야 할 곳에 안보이면 보여주고
                    allFabAnim();
                }
            } else {
                if(isAllFabVisible == true) { // 안보여야 할 곳에 보이면 숨기고
                    allFabAnim();
                }
                if(isAddFabVisible == true) { // add Fab 이 펼쳐져 있으면 숨기고
                    addFabAnim();
                }
            }
        }
        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}
        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };

    // Fragment 에서 Fab anim 을 위한 interface 구현
    @Override
    public void onContentsListScrollChanged(int newState) {
        recyclerAnim(newState);
    }

    @Override
    public void onContentsMonthScrollChanged(int newState) {
        recyclerAnim(newState);
    }

    private void recyclerAnim(int newState) {
        switch(newState) {
            case RecyclerView.SCROLL_STATE_DRAGGING: {
                if (isAddFabVisible) {
                    addFabAnim();
                }
                if (isAllFabVisible) {
                    allFabAnim();
                }
                break;
            }
            case RecyclerView.SCROLL_STATE_IDLE: {
                if (isAllFabVisible == false) {
                    allFabAnim();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            switch(requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        // 저장된 경로의 이미지 파일
                        File originalFile = new File(mCurrentPhotoPath);

                        // start cropping activity for pre-acquired image saved on the device
                        CropImage.activity(Uri.fromFile(originalFile))
                                .start(this);

                    }
                    break;
                }
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if(result != null) {
                        if (resultCode == RESULT_OK) {
                            Uri resultUri = result.getUri();
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);

                            new SendImageAsyncTask(db.historyDao()).execute(bitmap);
                             /*----------------------------------------
                            //
                            // 이 부분에서 서버로 보내면 될듯
                            //
                             -----------------------------------------*/

                        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            //Exception error = resultUri.getError();
                        }
                        originalFile = new File(mCurrentPhotoPath);
                        originalFile.delete();
                    }
                    break;
                }
            }
        } catch(Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            addFab.show();
        } else {
            addFab.hide();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    // 카테고리 초기화
    private void initCategories(){
        List<ContentsCategoryItem> spendingCategories = new ArrayList<>();
        List<ContentsCategoryItem> incomeCategories = new ArrayList<>();

        db.categoryDao().getSpendingCategories().observe(this, list->{
            for(int pos = 0; pos < list.size(); pos++){
                spendingCategories.add(new ContentsCategoryItem(list.get(pos).getCategoryId(), list.get(pos).getName(), list.get(pos).getKind()));
            }
        });

        db.categoryDao().getIncomeCategories().observe(this, list->{
            for(int pos = 0; pos < list.size(); pos++){
                incomeCategories.add(new ContentsCategoryItem(list.get(pos).getCategoryId(), list.get(pos).getName(), list.get(pos).getKind()));
            }
        });

        spendingLists =  spendingCategories;
        incomeLists = incomeCategories;
    }

    private static class InsertHistory extends AsyncTask<History, Void, Void>{
        HistoryDao dao;
        public InsertHistory(HistoryDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(History... histories) {
            //날짜 보정
            String date = histories[0].getDate();
            String split[] = date.split("-");
            String year = split[0];
            String month = split[1];
            String day = split[2];
            if(month.length() == 1){
                month = "0"+month;
            }
            if(day.length() == 1){
                day = "0"+day;
            }
            date = year+"-"+month+"-"+day;
            histories[0].setDate(date);

            dao.insertHistory(histories[0]);
            return null;
        }
    }

    // 이미지를 저장시킬 파일 형식(파일이름, 확장자, 저장경로)을 만든다.
    private File createImageFile() throws IOException {
        // 이미지 파일 이름 만드는 과정
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // 이미지 저장경로
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // 이미지 저장 형식 지정
        File image = File.createTempFile(
                imageFileName,       /* prefix */
                ".jpg",       /* suffix */
                storageDir           /* directory */
        );

        // 사진을 저장한 경로를 저장해둔다. (나중에 저장된 사진을 불러들여 crop 과정을 거치기 위함)
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    // 카메라 촬영
    private void dispatchTakePictureIntent() {
        // 카메라 촬영 하는 Intent 생성
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // 촬영한 사진을 저장할 파일 형식(파일이름, 확장자, 저장경로)
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.kumoh.paylog2.fileprovider",
                        photoFile);
                // 사진을 저장할 양식 등록
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // 카메라 촬영 시작
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void anim() {
        Toast.makeText(this, "하이", Toast.LENGTH_SHORT).show();
    }

    private void allFabAnim() {
        if(isAllFabVisible) {
            addFab.startAnimation(all_fab_close);
            imageFab.startAnimation(all_fab_close);
            addFab.setClickable(false);
            imageFab.setClickable(false);

            isAllFabVisible = false;
        } else {
            addFab.startAnimation(all_fab_open);
            imageFab.startAnimation(all_fab_open);
            addFab.setClickable(true);
            imageFab.setClickable(true);

            isAllFabVisible = true;
        }
    }

    private void addFabAnim() {
        if (isAddFabVisible) {
            addFab.setImageDrawable(getResources().getDrawable(R.drawable.white_icon_edit_24dp));

            spendingFab.startAnimation(fab_close);
            incomeFab.startAnimation(fab_close);
            spendingFab.setClickable(false);
            incomeFab.setClickable(false);

            isAddFabVisible = false;
        } else {
            addFab.setImageDrawable(getResources().getDrawable(R.drawable.white_icon_close_24dp));
            spendingFab.startAnimation(fab_open);
            incomeFab.startAnimation(fab_open);
            spendingFab.setClickable(true);
            incomeFab.setClickable(true);

            isAddFabVisible = true;
        }
    }

    private static class SendImageAsyncTask extends AsyncTask<Bitmap, Void, Void> {
        RequestHttpURLConnection conn;
        HistoryDao dao;

        public SendImageAsyncTask(HistoryDao dao) {
            this.dao = dao;
        }

        @Override
        protected void onPreExecute() {
            conn = new RequestHttpURLConnection();
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            JSONObject jsonObject = conn.requestImageProcessing("http://192.168.1.72:3000/image", bitmaps[0]);

            // Gson 이용해서 객체에 담기
            Gson gson = new Gson();
            HistoryVO history = gson.fromJson(jsonObject.toString(), HistoryVO.class);

            // 담은 객체 roomDB에 저장
            //dao.insertHistory(history);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
}
