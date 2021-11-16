package com.fpoly.pro1121.userapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat formatMonth = new SimpleDateFormat("dd\nMMM");


    public static String DateToStringMonth(Date date) {
        return formatMonth.format(date);
    }

    public static String getFormatNumber(int number) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
        return (currencyVN.format(number));
    }
    public static void addTextChangedListenerPass(EditText e, final TextInputLayout t){
        e.setOnFocusChangeListener((view, b) -> {
            if (b && e.getText().toString().isEmpty()) {
                t.setEnabled(true);
                t.setError("Không được để trống");
            }
        });
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() <= 0) {
                    t.setEnabled(true);
                    t.setError("Không được để trống");
                }
                if(charSequence.length()<6){
                    t.setEnabled(true);
                    t.setError("Mật khẩu không thể dưới 6 kí tự");
                } else {
                    t.setError(null);
                    t.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public static void addTextChangedListener(EditText e, final TextInputLayout t, boolean isEmail) {
        e.setOnFocusChangeListener((view, b) -> {
            if (b && e.getText().toString().isEmpty()) {
                t.setEnabled(true);
                t.setError("Không được để trống");
            }
        });
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    t.setEnabled(true);
                    t.setError("Không được để trống");
                } else if (isEmail && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    t.setEnabled(true);
                    t.setError("Email không hợp lệ");
                } else {
                    t.setError(null);
                    t.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
