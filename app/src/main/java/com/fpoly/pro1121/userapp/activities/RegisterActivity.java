package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fpoly.pro1121.userapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    EditText edtEmail,edtPassword,edtFullName;
    Button btnRegister;
    TextView tvHaveAnAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();

    }

    private void initUI() {
        edtEmail = findViewById(R.id.edt_email_register);
        edtPassword =  findViewById(R.id.edt_password_register);
        edtFullName =  findViewById(R.id.edt_name_register);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        tvHaveAnAccount = (TextView) findViewById(R.id.tvHaveAnAccount);
    }
}