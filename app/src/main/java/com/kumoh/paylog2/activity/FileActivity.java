package com.kumoh.paylog2.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.FileAdapter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity
        implements FileAdapter.FileListRecyclerOnClickListener, FileAdapter.FileListRecyclerLongClickListener {

    private RecyclerView recyclerView;
    private FileAdapter adapter;
    List<File> fileList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        // Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_file);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_icon_arrow_back_24dp);

        // 리사이클러뷰에 LayoutManager 객체 지정.
        recyclerView = findViewById(R.id.file_recyclerview) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 어댑터에 리스트 객체 등록
        adapter = new FileAdapter(fileList);
        adapter.setData(getFileList());

        // 리사이클러뷰에 어댑터 객체 지정.
        recyclerView.setAdapter(adapter);

        // 리스너 등록
        adapter.setOnClickListener(this);
        adapter.setLongClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case 3000:
                    adapter.setData(getFileList());
                    break;
            }
        }

        super.onActivityResult(0,0,null);
    }

    private ArrayList<File> getFileList() {
        String file_location = getApplicationContext().getExternalFilesDir(null).toString();

        File f = new File(file_location);
        File[] files = f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File path, String fileName) {
                return fileName.endsWith("xls");
            }
        });

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<File> list = new ArrayList();

        for (int i=0; i<files.length; i++) {
            list.add(files[i]) ;
        }

        return list;
    }

    // 엑셀 파일 열기
    private void fileOpen(File item) {
        Uri uri;
        // API 24 이상 일경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", item);
        }

        // API 24 미만 일경우
        else {
            uri = Uri.fromFile(item);
        }

        Intent shareIntent = new Intent(Intent.ACTION_VIEW);
        shareIntent.setDataAndType(uri, "application/vnd.ms-excel");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(shareIntent);


        //Uri path = Uri.fromFile(item);



    }

    // 삭제 Dialog
    private void fileDeleteDialog(final File file){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("해당 파일을 삭제하시겠습니까?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        file.delete();
                        adapter.removeItem(file);
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
    });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("파일 삭제");

        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_file_app_bar_menu, menu);

        return true;
    }

    // Recycler View item click event
    @Override
    public void onItemClicked(int position) {
        if (position != RecyclerView.NO_POSITION) {
            File item = adapter.getItem(position);

            fileOpen(item);
        }
    }

    // Recycler View item longClick event
    @Override
    public void onItemLongClicked(View view, int position) {
        // 오랫동안 눌렀을 때 이벤트가 발생됨


        PopupMenu popup=new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.item_file_popup_menu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                File file = adapter.getItem(position);

                switch(item.getItemId()){
                    // 파일 공유
                    case R.id.file_share_popup_item:
                        // 파일 공유하기
                        Uri uri = FileProvider.getUriForFile(getApplicationContext(), "com.kumoh.paylog2.fileprovider", file);

                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("application/excel");
                        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);

                        Intent intent = Intent.createChooser(shareIntent, "엑셀 내보내기");

                        startActivity(intent);
                        //출처: https://liveonthekeyboard.tistory.com/entry/안드로이드-엑셀-파일-생성-내보내기 [키위남]
                        break;

                    // 파일 삭제
                    case R.id.file_delete_popup_item:
                        fileDeleteDialog(file);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    // Toolbar home icon action 설정 (뒤로가기)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.excel_convert_item:
                Intent intent = new Intent(getApplicationContext(), FileSaveActivity.class);
                startActivityForResult(intent, 3000);
                break;
            case R.id.pdf_convert_item:
                Toast.makeText(this, "기능 구현중", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
