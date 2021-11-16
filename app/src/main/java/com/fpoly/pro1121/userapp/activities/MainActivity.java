package com.fpoly.pro1121.userapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.adapter.PagerMainAdapter;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar bottomNavigationBar;
    ViewPager2 viewPagerMain;
    PagerMainAdapter pageMainAdapter;

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

    private void initViewPager() {
        pageMainAdapter = new PagerMainAdapter(this);
        viewPagerMain.setAdapter(pageMainAdapter);
        viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // connect với bottom nav
                switch (position) {
                    case 0:
                        bottomNavigationBar.setItemSelected(R.id.action_home, true);
                        break;
                    case 1:
                        bottomNavigationBar.setItemSelected(R.id.action_cart, true);
                        break;
                    case 2:
                        bottomNavigationBar.setItemSelected(R.id.action_account, true);
                        break;
                    default:
                        bottomNavigationBar.setItemSelected(R.id.action_home, true);
                        break;
                }
            }
        });
        viewPagerMain.setUserInputEnabled(false); // ko cho người dùng trượt để chuyển fragment
        bottomNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onItemSelected(int i) {
                // connect với viewpager2
                switch (i) {
                    case R.id.action_home:
                        viewPagerMain.setCurrentItem(0);
                        break;
                    case R.id.action_cart:
                        viewPagerMain.setCurrentItem(1);
                        break;
                    case R.id.action_account:
                        viewPagerMain.setCurrentItem(2);
                        break;
                    default:
                        viewPagerMain.setCurrentItem(0);
                        break;
                }
            }
        });
    }
}