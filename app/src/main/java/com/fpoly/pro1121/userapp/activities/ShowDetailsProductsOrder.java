package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.adapter.ProductOrderAdapter;
import com.fpoly.pro1121.userapp.model.ProductOrder;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailsProductsOrder extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView rvProductsInOrder;
    ProductOrderAdapter productOrderAdapter;
    List<ProductOrder> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details_products_order);
        initUI();
        initToolbar();
        getDataIntent();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_show_products_in_order);
        toolbar.setTitle("Your products order");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view-> onBackPressed());
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        list = new ArrayList<>();
        list = intent.getParcelableArrayListExtra("list_product_order_history");
        productOrderAdapter.setData(list);
    }

    private void initUI() {
        rvProductsInOrder = findViewById(R.id.rv_product_in_order_history);
        productOrderAdapter = new ProductOrderAdapter(new ProductOrderAdapter.IClickProductListener() {
            @Override
            public void clickUpdateQuantity(boolean isAdd, ProductOrder productOrder) {

            }

            @Override
            public void clickDelete(int idProductOrder) {

            }
        },true);
        productOrderAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvProductsInOrder.setLayoutManager(linearLayoutManager);
        rvProductsInOrder.setAdapter(productOrderAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}