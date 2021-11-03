package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.fpoly.pro1121.userapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout tvUsernameLogin, tvPasswordLogin;
    Button btnLogin;
    TextView tvSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        events();
    }



    private void initUI() {
        tvUsernameLogin = (TextInputLayout) findViewById(R.id.tvUsernameLogin);
        tvPasswordLogin = (TextInputLayout) findViewById(R.id.tvPasswordLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvSignup = (TextView) findViewById(R.id.tvSignup);
    }
    private void events() {
        tvSignup.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        btnLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}