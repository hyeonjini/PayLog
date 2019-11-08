package com.kumoh.paylog2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kumoh.paylog2.util.FileConversion;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.Account;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.HistoryDao;
import com.kumoh.paylog2.db.LocalDatabase;

import java.util.ArrayList;
import java.util.List;

public class FileSaveActivity extends AppCompatActivity implements View.OnClickListener {

    private LocalDatabase db;

    // account
    private Spinner accountNameSpinner;
    private ArrayAdapter<AccountSpinnerData> arrayAdapter;

    // fromDate
    private TextView fromDateTextView;
    private Button fromDateBtn;

    // toDate
    private TextView toDateTextView;
    private Button toDateBtn;

    //  fileName
    private EditText fileNameEdit;

    // save button
    private Button saveFileBtn;

    // spinner 를 위한 객체
    private class AccountSpinnerData {
        private int id;
        private String name;

        public AccountSpinnerData(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_save);

        fromDateBtn = findViewById(R.id.save_file_select_fromDate_button);
        fromDateTextView = findViewById(R.id.save_file_fromDate_text_view);
        toDateBtn = findViewById(R.id.save_file_select_toDate_button);
        toDateTextView = findViewById(R.id.save_file_toDate_text_view);
        saveFileBtn = findViewById(R.id.save_file_btn);
        fileNameEdit = findViewById(R.id.file_name_edit);

        db = LocalDatabase.getInstance(this);

        // spinner 에 account 이름 설정
        db.accountDao().getAll().observe(this, accountList -> {
            List<AccountSpinnerData> accountNameList = new ArrayList<>();

            for(int i = 0; i < accountList.size(); i++) {
                Account account = accountList.get(i);
                AccountSpinnerData aSpinnerData = new AccountSpinnerData(account.getAccountId(), account.getName());

                accountNameList.add(aSpinnerData);
            }

            // 스피너 설정
            setSpinner(accountNameList);
        });

        // onClickListener 등록
        saveFileBtn.setOnClickListener(this);
        fromDateBtn.setOnClickListener(this);
        toDateBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.save_file_btn: {
                // 사용자가 설정한 값 가져오기
                AccountSpinnerData accountInfo = (AccountSpinnerData) accountNameSpinner.getSelectedItem();
                String fromDate = fromDateTextView.getText().toString();
                String toDate = toDateTextView.getText().toString();

                // 저장할 파일의 이름
                String fileName = fileNameEdit.getText().toString() + ".xls";

                new SaveFileAsyncTask(this, db.historyDao(), fileName, accountInfo.getId(), fromDate, toDate).execute();

                Intent resultIntent = new Intent();

                setResult(RESULT_OK, resultIntent);

                finish();

                break;
            }
            case R.id.save_file_select_fromDate_button:
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(this, fromPickerCallBack, 2019, 9, 04);
                fromDatePickerDialog.show();
                break;

            case R.id.save_file_select_toDate_button:
                DatePickerDialog toDatePickerDialog = new DatePickerDialog(this, toPickerCallBack, 2019, 9, 04);
                toDatePickerDialog.show();
                break;
        }
    }


    private DatePickerDialog.OnDateSetListener fromPickerCallBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            fromDateTextView.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
        }
    };

    private DatePickerDialog.OnDateSetListener toPickerCallBack = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            toDateTextView.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
        }
    };

    private void setSpinner(List<AccountSpinnerData> aList) {
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                aList);

        accountNameSpinner = findViewById(R.id.account_name_spinner);
        accountNameSpinner.setAdapter(arrayAdapter);
        accountNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private static class SaveFileAsyncTask extends AsyncTask<List<History>, Void, Void> {

        private Context context;

        private HistoryDao hDao;

        private String fileName;
        private int accountId;
        private String fromDate;
        private String toDate;
        private List<History> hList;

        private FileConversion fConv;

        public SaveFileAsyncTask(Context context, HistoryDao hDao, String fileName, int accountId, String fromData, String toDate) {
            this.context = context;
            this.hDao = hDao;
            this.fileName = fileName;
            this.accountId = accountId;
            this.fromDate = fromData;
            this.toDate = toDate;
        }

        @Override
        protected void onPreExecute() {
            fConv = new FileConversion();
            hList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(List<History>... lists) {
            fromDate = reviseDate(fromDate);
            toDate = reviseDate(toDate);

            hList = hDao.getAllFromToByAccountId(accountId, fromDate, toDate);

            fConv.saveExcelFile(context, hList, fileName);

            return null;
        }

        private String reviseDate(String date) {
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

            return date;
        }
    }
}
