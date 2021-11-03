package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fpoly.pro1121.userapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail,edtPassword;
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
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword =  findViewById(R.id.edt_password_login);
        btnLogin =  findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);
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