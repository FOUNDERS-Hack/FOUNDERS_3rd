package com.samsung.open_crypto_wallet_app;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.samsung.open_crypto_wallet_app.model.AccountModel;

import java8.util.concurrent.CompletableFuture;

@Database(entities = {AccountModel.class},version = 1)
public abstract class AccountDB extends RoomDatabase {

    private static AccountDB accountDBInstance;
    public abstract IAccountDAO iAccountDAO();

    public static CompletableFuture<AccountDB> getInstance(Context context) {
        CompletableFuture<AccountDB> DBSupplier = new CompletableFuture().supplyAsync(() -> {
            if (accountDBInstance == null || !accountDBInstance.isOpen()) {
                accountDBInstance = Room.databaseBuilder(context, AccountDB.class, Util.DB_NAME).build();
                Log.i(Util.LOG_TAG,"New Room DB Created");
                return accountDBInstance;
            }
            return accountDBInstance;
        });
        return DBSupplier;
    }

}
