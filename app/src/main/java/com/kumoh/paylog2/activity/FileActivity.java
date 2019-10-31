package com.kumoh.paylog2.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kumoh.paylog2.R;
import com.kumoh.paylog2.adapter.FileAdapter;

import java.io.File;
import java.util.ArrayList;

public class FileActivity extends AppCompatActivity {

    private FileAdapter adapter;

    private RecyclerView recyclerView;
    private Button convert_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        convert_btn = findViewById(R.id.file_convert_btn);

        ArrayList<File> mList = new ArrayList<>();

        // 리사이클러뷰에 LayoutManager 객체 지정.
        recyclerView = findViewById(R.id.file_recyclerview) ;
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        // 리사이클러뷰에 Adapter 객체 지정.
        adapter = new FileAdapter(mList);
        recyclerView.setAdapter(adapter);

        adapter.setData(getFileList());

//        getFileList().observe(this, list -> {
//            adapter.setData(list);
//        });

        convert_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event
                Intent intent = new Intent(getApplicationContext(), FileSaveActivity.class);
                startActivityForResult(intent, 3000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                // MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case 3000:
                    adapter.setData(getFileList());
                    adapter.notifyDataSetChanged();
                    //showFileList();
                    break;
            }
        }

        super.onActivityResult(0,0,null);
    }



    private ArrayList<File> getFileList() {
        String file_location = getApplicationContext().getExternalFilesDir(null).toString();

        File f = new File(file_location);
        File[] files = f.listFiles();
        /*File[] files = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase(Locale.US).endsWith(".xlsx"); //확장자
            }
        });*/
        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<File> list = new ArrayList();

        for (int i=0; i<files.length; i++) {
            list.add(files[i]) ;
        }

//        ListLiveData<File> lList = new ListLiveData<>();
//        lList.setValue(list);

        return list;
    }
}
