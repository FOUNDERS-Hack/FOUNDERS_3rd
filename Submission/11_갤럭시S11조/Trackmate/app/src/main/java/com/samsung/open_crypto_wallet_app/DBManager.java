package com.samsung.open_crypto_wallet_app;

import android.app.Activity;
import android.util.Log;

import com.samsung.android.sdk.coldwallet.ScwCoinType;
import com.samsung.android.sdk.coldwallet.ScwService;
import com.samsung.open_crypto_wallet_app.model.AccountModel;
import com.samsung.open_crypto_wallet_app.view_model.AccountViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import java8.util.concurrent.CompletableFuture;

public class DBManager {
    private static Activity mNavActivityInstance;
    private static AccountModel mAccountModel;

    public static void setNavActivityInstance(Activity mNavActivityInstance) {
        DBManager.mNavActivityInstance = mNavActivityInstance;
    }

    public static CompletableFuture<AccountDB> getDBInstance() {
        return AccountDB.getInstance(mNavActivityInstance);
    }

    public static CompletableFuture<AccountDB> deleteAccounts(AccountDB accountDB) {
        return new CompletableFuture<AccountDB>().supplyAsync(() -> {
            accountDB.iAccountDAO().deleteAccounts();
            return accountDB;
        });
    }

    public static CompletableFuture insertAccount(AccountDB accountDB) {
        return new CompletableFuture().supplyAsync(() -> {
            Log.i(Util.LOG_TAG, "Going to Insert below accountModel on DB");
            Log.i(Util.LOG_TAG, "id: " + mAccountModel.getAccountId() + "\n" +
                    "name: " + mAccountModel.getAccountName() + "\n" +
                    "hd path: " + mAccountModel.getHdPath() + "\n" +
                    "public address: " + mAccountModel.getPublicAddress() + "\n"
            );
            accountDB.iAccountDAO().insertAccountModel(mAccountModel);
            return accountDB;           //dummy return
        });
    }

    public static void storeAndFetchAccountDB(int accountId, boolean reset) {

        Log.i(Util.LOG_TAG, "Initiated storeAccountToDB");

        if (!reset) {
            populateAccountModel(accountId);
        } else {
            // Using CompletableFuture as DB Operations are Async, Can't run on UI thread
            CompletableFuture<AccountDB> DBSupplyTask = getDBInstance();
            DBSupplyTask.thenApply(accountDB -> {
                CompletableFuture<AccountDB> DeletionTask = deleteAccounts(accountDB);
                DeletionTask.thenApply(returnedAccountDB -> {
                    Log.i(Util.LOG_TAG, "Accounts deleted Successfully from DB");
                    Log.i(Util.LOG_TAG, "Accounts count in DB: " + returnedAccountDB.iAccountDAO().getAccountCount());
                    populateAccountModel(accountId);
                    return returnedAccountDB;       // returned accountDB as dummy, to avoid returning 'null'
                });
                return accountDB;                   // returned accountDB as dummy, to avoid returning 'null'
            });
        }
    }

    public static CompletableFuture<Integer> getMaxID(AccountDB accountDB) {
        return new CompletableFuture<Integer>().supplyAsync(() -> accountDB.iAccountDAO().getMaxAccoundId());
    }

    public static CompletableFuture editAccountNameDB(AccountDB accountDB, int accountId, String accountName) {
        return new CompletableFuture().supplyAsync(() -> {
            accountDB.iAccountDAO().editAccountName(accountId, accountName);
            return accountDB;       //dummy return
        });
    }

    public static void editAccountName(int accountId, String accountName) {
        CompletableFuture<AccountDB> DBSupplyTask = getDBInstance();
        DBSupplyTask.thenApply((accountDB) -> {
            CompletableFuture EditTask = editAccountNameDB(accountDB, accountId, accountName);
            EditTask.thenApply(dummy -> {
                Log.i(Util.LOG_TAG, "Edited Successfully.");
                fetchAllAccountsFromDB();
                return accountDB;       //dummy return
            });
            return accountDB;           //dummy return
        });
    }

    public static CompletableFuture<ArrayList<AccountModel>> fetchAllAccounts(AccountDB accountDB) {
        return new CompletableFuture<ArrayList<AccountModel>>().supplyAsync(() -> {
            return new ArrayList<AccountModel>(Arrays.asList(accountDB.iAccountDAO().getAllAccounts()));
        });
    }

    public static void fetchAllAccountsFromDB() {
        CompletableFuture<AccountDB> DBSupplyTask = getDBInstance();
        DBSupplyTask.thenApply(accountDB -> {
            CompletableFuture<ArrayList<AccountModel>> fetchTask = fetchAllAccounts(accountDB);
            fetchTask.thenApply(accountModels -> {
                Log.i(Util.LOG_TAG, "Fetching all accounts successful.");
                Log.i(Util.LOG_TAG, "Number of accounts fetched: " + accountModels.size());
                //Set account model list of data binding class
                AccountViewModel.setAllAccounts(accountModels);
                return accountModels;
            });
            return accountDB;
        });
    }


    // Populates AccountModel Object from accountID
    private static void populateAccountModel(int accountId) {
        mAccountModel = new AccountModel();
        mAccountModel.setAccountId(accountId);
        mAccountModel.setAccountName(Util.ACCOUNT_NAME_PREFIX + accountId);

        if(!SharedPreferenceManager.getUseOtherKeyManager(mNavActivityInstance)) {
            // TODO : Get HDPath from Keystore using getHdPath API and ScwCoinType class
            //mAccountModel.setHdPath(hdPath);
            String hdPath = ScwService.getHdPath(ScwCoinType.ETH, 0);
            Log.d("DBManager","HDpath : "+ hdPath);
            mAccountModel.setHdPath(hdPath);
            // Get Public Address from SBK for specific HDpath
            KeyStoreManager.getInstance(mNavActivityInstance).getPublicAddress(mAccountModel.getHdPath());
            // Async task, "onGetAddressSuccess" method will be triggered once SBK returns Address
        } else {
            KeyStoreManager.getInstance(mNavActivityInstance).getPublicAddress();
        }

    }

    public static void onGetAddressSuccess(String publicAddress) {

        Log.i(Util.LOG_TAG, "Address on DBManager is : " + publicAddress);

        mAccountModel.setPublicAddress(publicAddress);

        CompletableFuture<AccountDB> DBSupplyTask;

        DBSupplyTask = getDBInstance();

        DBSupplyTask.thenApply(accountDB -> {

            CompletableFuture InsertionTask = insertAccount(accountDB);

            InsertionTask.thenApply(dummy -> {
                Log.i(Util.LOG_TAG, "Account Insertion on DB Successful");
                SharedPreferenceManager.setDefaultAccountID(mNavActivityInstance, mAccountModel.getAccountId());
                Log.i(Util.LOG_TAG, "Accounts count: " + accountDB.iAccountDAO().getAccountCount());
                fetchAllAccountsFromDB();
                //Save seed hash on shared preference if SBK account is reset

                if(!SharedPreferenceManager.getUseOtherKeyManager(mNavActivityInstance)) {
                    SharedPreferenceManager.setSeedHash(mNavActivityInstance, KeyStoreManager.getInstance(mNavActivityInstance).getSeedHashFromSBK());
                } else {
                    SharedPreferenceManager.setGenerateAccountWithOtherKeyManager(mNavActivityInstance, true);
                }
                return dummy;
            });

            return accountDB;           //dummy return
        });

    }

    public static void closeDB() {
        CompletableFuture<AccountDB> DBSupplyTask = getDBInstance();
        DBSupplyTask.thenApply(accountDB -> {
            if (accountDB.isOpen()) {
                Log.i(Util.LOG_TAG, "Shutting Down DB Connection");
                accountDB.close();
            }
            return accountDB;
        });
    }

}