package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.Utils;
import com.fpoly.pro1121.userapp.database.ProductOrderDAO;
import com.fpoly.pro1121.userapp.model.Product;
import com.fpoly.pro1121.userapp.model.ProductOrder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ProductDetailsActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String idUserCurrent = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    Button btnAddCart;
    ImageView imgProduct,ivAddQuantity,ivMinusQuantity;
    TextView tvName,tvPrice,tvDescription,tvQuantity;
    Product productCurrent;
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        initUI();
        getDataToView();
        actionQuantity();
        actionAddToCart();
    }

    private void actionAddToCart() {
        btnAddCart.setOnClickListener(view->{
            String idProduct = productCurrent.getId();
            int quantityOrder = quantity;
            ProductOrder productOrder = new ProductOrder(idUserCurrent,idProduct,productCurrent.getPrice(),quantityOrder);
            boolean result = ProductOrderDAO.getInstance(this).insertProductOrder(productOrder);
            if(result) {
                Toast.makeText(ProductDetailsActivity.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actionQuantity() {
        ivAddQuantity.setOnClickListener(view->{
            quantity++;
            tvQuantity.setText(quantity+"");
        });
        ivMinusQuantity.setOnClickListener(view->{
            quantity--;
            if(quantity<=1){
                quantity = 1;
            }
            tvQuantity.setText(quantity+"");
        });
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
        ivAddQuantity = findViewById(R.id.iv_add_quantity_product);
        ivMinusQuantity =  findViewById(R.id.iv_minus_quantity_product);
        tvQuantity = findViewById(R.id.tv_quantity);
        tvQuantity.setText(quantity+"");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}