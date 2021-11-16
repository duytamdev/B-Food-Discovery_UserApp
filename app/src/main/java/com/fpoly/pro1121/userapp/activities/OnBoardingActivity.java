package com.fpoly.pro1121.userapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fpoly.pro1121.userapp.R;
import com.ramotion.paperonboarding.PaperOnboardingEngine;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onboarding_main_layout); // liên kết với layout thư viện
        PaperOnboardingEngine engine = new PaperOnboardingEngine(findViewById(R.id.onboardingRootView),
                getPaperBoardingPageData(), this);

        engine.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                Intent intent = new Intent(OnBoardingActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });

    }

    private ArrayList<PaperOnboardingPage> getPaperBoardingPageData() {
        PaperOnboardingPage scr1 = new PaperOnboardingPage("Welcome to Bhook",
                "food delivery app that helps you to get the best dishes quickly and in time from your nearest restaurant.",
                Color.parseColor("#FFFFFF"), R.drawable.boarding1, R.drawable.ic_more);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Enjoy fast delivery",
                "We offer 45 minutes delivery gurantee or the food will be delivered for free.",
                Color.parseColor("#FFFFFF"), R.drawable.boarding2, R.drawable.ic_more);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Order best dishes",
                "Your order will be immediately collected and sent by our courier",
                Color.parseColor("#FFFFFF"), R.drawable.boarding3, R.drawable.ic_more);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);
        return elements;
    }
}