package com.fpoly.pro1121.userapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.fpoly.pro1121.userapp.R;

public class SearchProductActivity extends AppCompatActivity {

    ImageView  imgPrev;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        initUI();
        events();
    }


    private void initUI() {
        imgPrev = findViewById(R.id.iv_prev_search);
        searchView = findViewById(R.id.search_product);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search");
    }

    private void events() {
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchProductActivity.this,MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
    }
}