package com.example.mjd_final;

import android.app.Application;

import com.example.mjd_final.util.PrefsHelper;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PrefsHelper.initialize(getApplicationContext());
    }
}
