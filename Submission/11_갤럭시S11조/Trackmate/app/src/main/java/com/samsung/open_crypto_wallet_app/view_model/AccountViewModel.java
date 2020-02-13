package com.samsung.open_crypto_wallet_app.view_model;

import android.util.Log;
import android.widget.Toast;

import com.samsung.open_crypto_wallet_app.DBManager;
import com.samsung.open_crypto_wallet_app.KeyStoreManager;
import com.samsung.open_crypto_wallet_app.NodeConnector;
import com.samsung.open_crypto_wallet_app.SharedPreferenceManager;
import com.samsung.open_crypto_wallet_app.Util;
import com.samsung.open_crypto_wallet_app.model.AccountModel;
import com.samsung.open_crypto_wallet_app.view.NavActivity;

import java.util.ArrayList;

public class AccountViewModel {

    private static AccountModel mDefaultAccount;
    private static NavActivity mNavActivityInstance;

    public static AccountModel getDefaultAccount() {
        if (mDefaultAccount == null) {
            mDefaultAccount = new AccountModel();
        }
        return mDefaultAccount;
    }

    public static void setNavActivityInstance(NavActivity navActivity) {
        AccountViewModel.mNavActivityInstance = navActivity;
    }

    public static void setAllAccounts(ArrayList<AccountModel> allAccounts) {
        //Running Code related to adapter on UI thread
        mNavActivityInstance.refreshUIList(allAccounts);
        setDefaultAccount(allAccounts.get(SharedPreferenceManager.getDefaultAccountID(mNavActivityInstance) - 1));
    }

    public static void setDefaultAccount(AccountModel defaultAccount) {

        defaultAccount.setQrCode(Util.generateQRCode(defaultAccount.getPublicAddress()));

        //Ensuring Data Binding refresh
        mDefaultAccount.setPublicAddress(defaultAccount.getPublicAddress());
        mDefaultAccount.setQrCode(defaultAccount.getQrCode());
        mDefaultAccount.setLoading(false);
        mDefaultAccount.setAccountName(defaultAccount.getAccountName());
        mDefaultAccount.setAccountId(defaultAccount.getAccountId());
        mDefaultAccount.setHdPath(defaultAccount.getHdPath());
        fetchBalance();
        Log.i(Util.LOG_TAG, "Default Account Set Properly");
    }

    public static String getDefaultHDPath() {
        //Database functions to be added later
        return mDefaultAccount.getHdPath();
    }

    public static String getDefaultAccountAddress() {
        return mDefaultAccount.getPublicAddress();
    }

    public static String getDefaultAccountName() {
        return mDefaultAccount.getAccountName();
    }

    public static void setBalance(String balance) {
        mDefaultAccount.setAccountBalance(balance);
        mNavActivityInstance.runOnUiThread(() -> {
            Toast.makeText(mNavActivityInstance, "Ether Balance is: " + balance, Toast.LENGTH_SHORT).show();
        });
    }

    public static void fetchBalance() {
        NodeConnector.getInstance(mNavActivityInstance).getBalance(getDefaultAccountAddress());
    }


    public static void populateAllAccounts() {
        //Check SeedHash is changed or not to detect SBK Wallet switch
        //  if Changed, Set new seed hash, drop DB, recreate DB, set default account id as 0

        if (!SharedPreferenceManager.getGenerateAccountWithOtherKeyManager(mNavActivityInstance) && isSeedHashChanged()) {
            DBManager.storeAndFetchAccountDB(1, true);    // reset: true when SBK Wallet Changes-> Drop DB, Recreate DB
        } else {
            DBManager.fetchAllAccountsFromDB();
        }

    }

    public static boolean isSeedHashChanged() {
        String hashFromSharedPreference = SharedPreferenceManager.getSeedHash(mNavActivityInstance);
        String hashFromSBK = KeyStoreManager.getInstance(mNavActivityInstance).getSeedHashFromSBK();
        if (hashFromSharedPreference.isEmpty()) {
            //Seed hash is empty. App first run.
            Log.i(Util.LOG_TAG, "Current Seed hash is empty.");
            return true;
        } else {
            if (!hashFromSharedPreference.equals(hashFromSBK)) {
                //Seed hash changed need to store new seed hash and drop existing db
                Log.i(Util.LOG_TAG, "Seed hash has been changed.");
                return true;
            } else {
                //Seed hash unchanged
                Log.i(Util.LOG_TAG, "Seed hash Unchanged.");
                return false;
            }
        }
    }

    public static void createNewAccount() {
        DBManager.getDBInstance().thenApply(accountDB -> {
            DBManager.getMaxID(accountDB).thenApply(maxID -> {
                DBManager.storeAndFetchAccountDB(maxID + 1, false);
                return maxID;
            });
            return accountDB;
        });
    }

    public static void editAccountName(String newAccountName) {
        DBManager.editAccountName(mDefaultAccount.getAccountId(), newAccountName);
    }

}
