package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.fpoly.pro1121.userapp.MySharePreference;
import com.fpoly.pro1121.userapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isInstalled = MySharePreference.getInstance(SplashActivity.this).getBoolean("isInstalled");

                    // đã đăng nhập , đã dùng app
                if(mAuth.getCurrentUser()!=null&& isInstalled){
                    startMyActivity(MainActivity.class);
                    // đã dùng app, chưa đăng nhập
                }else if(mAuth.getCurrentUser()==null&& isInstalled){
                    startMyActivity(LoginActivity.class);
                    // chưa dùng app
                }else{
                    startMyActivity(OnboardActivity.class);
                    MySharePreference.getInstance(SplashActivity.this).putBoolean("isInstalled",true);
                }
            }
        },2500);
    }
    public void startMyActivity(Class<?> cls){
        Intent intent = new Intent(SplashActivity.this, cls);
        startActivity(intent);
        finish();
    }
}