package com.samsung.uni_block_app.services;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.samsung.uni_block_app.util.Util;

import org.jetbrains.annotations.NotNull;

public class SharedPreferenceManager {
    private static final String HARDWARE_WALLET_TYPE_KEY = "HARDWARE_WALLET_TYPE_KEY";
    private static final String COIN_TYPE_KEY = "COIN_TYPE_KEY";
    private static final String NETWORK_TYPE_KEY = "NETWORK_TYPE_KEY";
    private static final String IS_VALID_KEY = "IS_VALID_KEY";
    private static final String FILE_NAME = "uni_block_shared_pref_file";
    private static SharedPreferences mSharedPreferences;

    private SharedPreferenceManager() {
    }

    public static void setSharedPreferenceStorage(@NotNull Application application) {
        //get SharedPreference
        SharedPreferenceManager.mSharedPreferences = application.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static String getHardwareWalletTypeString() {
        return mSharedPreferences.getString(HARDWARE_WALLET_TYPE_KEY, "SAMSUNG");
    }

    public static void setHardwareWalletTypeString(String hardwareWalletTypeString) {
        storeString(HARDWARE_WALLET_TYPE_KEY, hardwareWalletTypeString);
        Log.i(Util.LOG_TAG, "Storing on SharedPreference->wallet:" + hardwareWalletTypeString);
    }

    public static String getCoinTypeString() {
        //second parameter is default value to return if the key doesn't exist
        return mSharedPreferences.getString(COIN_TYPE_KEY, "ETH");
    }

    public static void setCoinTypeString(String coinTypeString) {
        storeString(COIN_TYPE_KEY, coinTypeString);
        Log.i(Util.LOG_TAG, "Storing on SharedPreference->coin:" + coinTypeString);
    }

    public static String getNetworkTypeString() {
        return mSharedPreferences.getString(NETWORK_TYPE_KEY, "ROPSTEN");
    }

    public static void setNetworkTypeString(String networkTypeString) {
        storeString(NETWORK_TYPE_KEY, networkTypeString);
        Log.i(Util.LOG_TAG, "Storing on SharedPreference->network:" + networkTypeString);
    }

    public static Boolean isValid() {
        //second parameter is default value to return if the key doesn't exist
        return mSharedPreferences.getBoolean(IS_VALID_KEY, false);
    }

    public static void setValid(Boolean isValidBool) {
        storeBoolean(IS_VALID_KEY, isValidBool);
        Log.i(Util.LOG_TAG, "Flag set: SharedPreference->isValid:" + isValidBool);
    }

    private static void storeString(String key, String data) {
        // Store seed hash in Shared Preference
        if (mSharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
        } else {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(key, data);
            editor.commit();
        }
    }

    private static void storeBoolean(String key, Boolean data) {
        // Store seed hash in Shared Preference
        if (mSharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
        } else {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(key, data);
            editor.commit();
        }
    }
}
