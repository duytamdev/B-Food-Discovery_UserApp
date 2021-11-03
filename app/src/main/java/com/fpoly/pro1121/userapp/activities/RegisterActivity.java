package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.fpoly.pro1121.userapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    TextInputLayout edtUsernameRegister, edtPasswordRegister, edtName;
    Button btnRegister;
    TextView tvHaveAnAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();

    }

    private void initUI() {
        edtUsernameRegister = (TextInputLayout) findViewById(R.id.edtUsernameRegister);
        edtPasswordRegister = (TextInputLayout) findViewById(R.id.edtPasswordRegister);
        edtName = (TextInputLayout) findViewById(R.id.edtName);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvHaveAnAccount = (TextView) findViewById(R.id.tvHaveAnAccount);
    }
}