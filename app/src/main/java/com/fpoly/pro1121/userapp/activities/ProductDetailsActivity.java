package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.fpoly.pro1121.userapp.R;

public class ProductDetailsActivity extends AppCompatActivity {

    Button btnAddCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        btnAddCart = (Button) findViewById(R.id.btnAddCart);
    }
}