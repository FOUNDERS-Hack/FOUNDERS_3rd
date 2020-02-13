package com.android.samsung.codelab.guestbookdapp;

import android.app.Application;

import com.android.samsung.codelab.guestbookdapp.util.PrefsHelper;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PrefsHelper.initialize(getApplicationContext());

    }
}
