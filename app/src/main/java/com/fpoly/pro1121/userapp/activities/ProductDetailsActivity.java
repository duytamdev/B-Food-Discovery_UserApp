package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.Utils;
import com.fpoly.pro1121.userapp.model.Product;

public class ProductDetailsActivity extends AppCompatActivity {

    Button btnAddCart;
    ImageView imgProduct;
    TextView tvName,tvPrice,tvDescription;
    Product productCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initUI();
        getDataToView();
    }

    private void getDataToView() {
        Intent intent = getIntent();
        if(intent !=null){
            productCurrent = intent.getParcelableExtra("product");
            tvName.setText(productCurrent.getName());
            tvPrice.setText(Utils.getFormatNumber(productCurrent.getPrice()));
            tvDescription.setText(productCurrent.getDescription());
            Glide.with(this)
                    .load(productCurrent.getUrlImage())
                    .centerCrop()
                    .into(imgProduct);
        }
    }

    private void initUI() {
        btnAddCart =findViewById(R.id.btnAddCart);
        imgProduct = findViewById(R.id.imgProduct);
        tvName = findViewById(R.id.tvProduct);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);

    }
}