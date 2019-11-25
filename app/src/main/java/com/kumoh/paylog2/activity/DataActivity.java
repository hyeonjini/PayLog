package com.kumoh.paylog2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.PrimaryKey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kumoh.paylog2.R;
import com.kumoh.paylog2.db.Account;
import com.kumoh.paylog2.db.Category;
import com.kumoh.paylog2.db.History;
import com.kumoh.paylog2.db.LocalDatabase;
import com.kumoh.paylog2.dto.HistoryVO;
import com.kumoh.paylog2.util.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DataActivity extends AppCompatActivity implements View.OnClickListener {
    private Button sendBtn;
    private TextView title;
    private TextView explain1;
    private TextView explain2;
    private String userId;
    private int activityType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Intent intent = getIntent();
        activityType = intent.getExtras().getInt("type");

        // Toolbar 설정
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_icon_arrow_back_24dp);

        sendBtn = findViewById(R.id.btn_send);
        title = findViewById(R.id.data_title);
        explain1 = findViewById(R.id.data_exp_1);
        explain2 = findViewById(R.id.data_exp_2);

        if(activityType == 1){
            sendBtn.setText(R.string.data_in_btn);
            title.setText(R.string.data_in_title);
            explain1.setText(R.string.data_in_explain_1);
            explain2.setText(R.string.data_in_explain_2);
        }

        sendBtn.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        System.out.println("Token : " + userId );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_send:
                new sendUserJson().execute(userId);
                break;
        }
    }

    // Toolbar home icon action 설정 (뒤로가기)
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

    // 전송을 위한 Json 생성
    private JSONObject createUserJson(String userId, List<Account> accounts,
                                      List<History> histories, List<Category> categories){
        JSONObject userJson = new JSONObject();

        JSONArray accountJson = new JSONArray();
        JSONArray historyJson = new JSONArray();
        JSONArray categoryJson = new JSONArray();

        try{
            userJson.put("UID", userId);

            for(Account account : accounts){
                JSONObject accountTmp = new JSONObject();
                accountTmp.put("accountId", account.getAccountId());
                accountTmp.put("budget", account.getBudget());
                accountTmp.put("name", account.getName());
                accountTmp.put("subscribe", account.getSubscribe());
                accountTmp.put("isMain", account.getIsMain());
                accountJson.put(accountTmp);
            }
            userJson.put("account", accountJson);

            for(History history : histories){
                JSONObject historyTmp = new JSONObject();
                historyTmp.put("accountId", history.getAccountId());
                historyTmp.put("historyId", history.getHistoryId());
                historyTmp.put("kind", history.getKind());
                historyTmp.put("date", history.getDate());
                historyTmp.put("categoryId", history.getCategoryId());
                historyTmp.put("description", history.getDescription());
                historyTmp.put("amount", history.getAmount());
                historyJson.put(historyTmp);
            }
            userJson.put("history", historyJson);

            for(Category category : categories){
                JSONObject categoryTmp = new JSONObject();
                categoryTmp.put("categoryId", category.getCategoryId());
                categoryTmp.put("name", category.getName());
                categoryTmp.put("kind", category.getKind());
                categoryJson.put(categoryTmp);
            }
            userJson.put("category", categoryJson);

        }catch (JSONException e){
            e.printStackTrace();
        }

        Log.i("생성한 Json : ", userJson.toString());

        return userJson;
    }

    private class sendUserJson extends AsyncTask<String, Void, Boolean>{
        private RequestHttpURLConnection conn;
        private LocalDatabase db;
        private String userId;
        private List<Account> accounts;
        private List<History> histories;
        private List<Category> categories;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            conn = new RequestHttpURLConnection();
            //imageProcessingProgressBar.setVisibility(View.VISIBLE);
            progressDialog = new ProgressDialog(DataActivity.this);
            progressDialog.setMessage(getResources().getString(R.string.data_wait));
            if(activityType == 0){
                progressDialog.setTitle(R.string.data_out_dialog);
            }
            else{
                progressDialog.setTitle(R.string.data_in_dialog);
            }
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            db = LocalDatabase.getInstance(getApplicationContext());
            userId = strings[0];
            accounts = db.accountDao().getAllAccounts();
            Log.i("계정 수: ", "" + accounts.size());
            histories = db.historyDao().getAllHistories();
            Log.i("내역 수: ", "" + histories.size());
            categories = db.categoryDao().getAllCategories();
            Log.i("카테고리 수: ", "" + categories.size());
            createUserJson(userId, accounts, histories, categories);
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_LONG).show();
        }
    }
}
