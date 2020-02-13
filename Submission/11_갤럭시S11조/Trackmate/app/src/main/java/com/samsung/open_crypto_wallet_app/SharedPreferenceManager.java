package com.samsung.open_crypto_wallet_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceManager {

    private static final String KEY = "SharedPreference";
    private static final String seedHashKey = "seed_hash";
    private static final String defaultSeedHashValue = "";
    private static final String defaultAccountIDKey = "default_account_ID";
    private static final int defaultAccountIDValue = 1;
    private static final String networkKey = "network";
    private static final String defaultNetworkValue = "ropsten";
    private static final String USE_OTHER_KEYMANAGER = "useOtherKeyManager";
    private static final String GENERAGTE_ADDRESS_USE_OTHER_KEYMANAGER = "generate_address_useOtherKeyManager";


    private static SharedPreferences getSharedPreference(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return sharedPref;
    }

    public static String getSeedHash(Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
            return defaultSeedHashValue;
        } else {
            return sharedPreferences.getString(seedHashKey, defaultSeedHashValue);
        }
    }

    public static void setSeedHash(Context context, String seedHash) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(seedHashKey, seedHash);
            editor.commit();
        }
    }

    public static Integer getDefaultAccountID(Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
            return defaultAccountIDValue;
        } else {
            return sharedPreferences.getInt(defaultAccountIDKey, defaultAccountIDValue);
        }
    }

    public static void setDefaultAccountID(Context context, int defaultAccountID) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(defaultAccountIDKey, defaultAccountID);
            editor.commit();
        }
    }

    public static String getDefaultNetwork(Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
            return defaultNetworkValue;
        } else {
            return sharedPreferences.getString(networkKey, defaultNetworkValue);
        }
    }

    public static void setDefaultNetwork(Context context, String defaultNetwork) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(networkKey, defaultNetwork);
            editor.commit();
        }
    }

    public static void setCredentialFilePath(Context context, String key, String path) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, path);
            editor.commit();
        }
    }

    public static String getCredentialFilePath(Context context, String key) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
            return null;
        } else {
            return sharedPreferences.getString(key, null);
        }
    }

    public static void setUseOtherKeyManager(Context context, boolean flag) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(USE_OTHER_KEYMANAGER, flag);
            editor.commit();
        }
    }

    public static boolean getUseOtherKeyManager(Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
            return false;
        } else {
            return sharedPreferences.getBoolean(USE_OTHER_KEYMANAGER, false);
        }
    }

    public static void setGenerateAccountWithOtherKeyManager(Context context, boolean flag) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(GENERAGTE_ADDRESS_USE_OTHER_KEYMANAGER, flag);
            editor.commit();
        }
    }

    public static boolean getGenerateAccountWithOtherKeyManager(Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        if (sharedPreferences == null) {
            Log.e(Util.LOG_TAG, "Shared Preference not set.");
            return false;
        } else {
            return sharedPreferences.getBoolean(GENERAGTE_ADDRESS_USE_OTHER_KEYMANAGER, false);
        }
    }

}
