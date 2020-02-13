package com.android.samsung.codelab.guestbookdapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsHelper {

    private static final String KEY_SEED_HASH = "seed_hash";
    private static final String KEY_ADDRESS = "address";
    private static PrefsHelper instance;
    private final SharedPreferences prefs;

    private PrefsHelper(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PrefsHelper initialize(Context context) {
        if (instance == null) {
            instance = new PrefsHelper(context);
        }
        return instance;
    }

    public static PrefsHelper getInstance() {
        return instance;
    }

    public String getCachedSeedHash() {
        return prefs.getString(KEY_SEED_HASH, "");
    }

    public String getCachedAddress() {
        return prefs.getString(KEY_ADDRESS, "");
    }

    public void updateSeedHash(String seedHash) {
        putString(KEY_SEED_HASH, seedHash);
    }

    public void updateAddress(String address) {
        putString(KEY_ADDRESS, address);
    }

    private void putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }


}
