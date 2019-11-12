package com.kumoh.paylog2.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kumoh.paylog2.db.LocalDatabase;

public class SplashActivity extends AppCompatActivity {
    // 파이어베이스 인증 객체 생성
    private FirebaseAuth firebaseAuth;
    private LocalDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        db = LocalDatabase.getInstance(this); // 초기 데이터

        //account가 null이 아닐 경우, 이 사용자는 이미 구글 로그인 상태이다.
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) { //이미 구글 로그인이 된(이 앱에 로그인 된) 상태
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            finish();
        }
        else { //로그인이 되지 않았으니, 로그인을 받아야 한다.
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            finish();
        }
    }
}
