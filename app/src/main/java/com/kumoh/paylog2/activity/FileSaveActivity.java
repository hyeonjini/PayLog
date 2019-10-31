package com.kumoh.paylog2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kumoh.paylog2.FileConversion;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.Account;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.LocalDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSaveActivity extends AppCompatActivity {

    private LocalDatabase db;

    private Spinner spinner;
    private DatePicker fromDatePicker;
    private EditText fileNameEdit;
    private Button saveFileBtn;

    private ArrayAdapter<String> arrayAdapter;



    private ArrayList<History> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_save);

        saveFileBtn = findViewById(R.id.save_file_btn);
        fileNameEdit = findViewById(R.id.file_name_edit);

        db = LocalDatabase.getInstance(this);

        // spinner 에 account 이름 설정
        db.accountDao().getAll().observe(this, accountList -> {
            List<String> accountNameList = new ArrayList<>();

            for(int i = 0; i < accountList.size(); i++) {
                accountNameList.add(accountList.get(i).getName());
            }

            // 스피너 설정
           setSpinner(accountNameList);
        });


        mItems.add(new History(12, 1, "2011", 12, "하이", 2000));

        //
        saveFileBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = fileNameEdit.getText().toString() + ".xls";

                FileConversion fConv = new FileConversion();

                File savedFile = fConv.saveExcelFile(getApplicationContext(), mItems, fileName);

                Intent resultIntent = new Intent();

                resultIntent.putExtra("file",savedFile);

                setResult(RESULT_OK,resultIntent);

                finish();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
        }
    };


    private void setSpinner(List<String> aList) {
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                aList);

        spinner = findViewById(R.id.group_name_spinner);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*Toast.makeText(getApplicationContext(),arrayList.get(i)+"가 선택되었습니다.",
                        Toast.LENGTH_SHORT).show();*/
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void displayDialog(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, listener, 2019, 9, 04);
        dialog.show();
    }
}
