package com.fpoly.pro1121.userapp;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharePreference {

    private static final String NAME_SHARE = "share";
    private static MySharePreference instance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private MySharePreference(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME_SHARE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized MySharePreference getInstance(Context context) {
        if (instance == null) {
            instance = new MySharePreference(context);
        }
        return instance;
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putString(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }
}
