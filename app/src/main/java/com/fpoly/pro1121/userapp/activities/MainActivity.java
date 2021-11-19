package com.fpoly.pro1121.userapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.adapter.PagerMainAdapter;
import com.fpoly.pro1121.userapp.database.ProductOrderDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public ChipNavigationBar bottomNavigationBar;
    ViewPager2 viewPagerMain;
    PagerMainAdapter pageMainAdapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initViewPager();
    }

    private void initUI() {
        bottomNavigationBar = findViewById(R.id.bottom_nav_chip);
        viewPagerMain = findViewById(R.id.viewpager2_main);
    }

    @SuppressLint("NonConstantResourceId")
    private void initViewPager() {
        pageMainAdapter = new PagerMainAdapter(this);
        viewPagerMain.setAdapter(pageMainAdapter);
        viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // connect với bottom nav
                switch (position) {
                    case 1:
                        bottomNavigationBar.setItemSelected(R.id.action_cart, true);
                        break;
                    case 2:
                        bottomNavigationBar.setItemSelected(R.id.action_account, true);
                        break;
                    case 0:
                    default:
                        bottomNavigationBar.setItemSelected(R.id.action_home, true);
                        break;
                }
            }
        });
        viewPagerMain.setUserInputEnabled(false); // ko cho người dùng trượt để chuyển fragment
        bottomNavigationBar.setOnItemSelectedListener(i -> {
            // connect với viewpager2
            switch (i) {
                case R.id.action_cart:
                    viewPagerMain.setCurrentItem(1);
                    break;
                case R.id.action_account:
                    viewPagerMain.setCurrentItem(2);
                    break;
                case R.id.action_home:
                default:
                    viewPagerMain.setCurrentItem(0);
                    break;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        String idUser = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        bottomNavigationBar.showBadge(R.id.action_cart, ProductOrderDAO.getInstance(MainActivity.this).getQuantityProductsOrder(idUser));
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        // nếu quá 2 giây ko thao tác thì chuyen trang thai false
        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}