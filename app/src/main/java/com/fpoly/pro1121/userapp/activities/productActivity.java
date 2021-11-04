package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fpoly.pro1121.userapp.R;

public class productActivity extends AppCompatActivity {

    Button btnAddCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        btnAddCart = (Button) findViewById(R.id.btnAddCart);
    }
}