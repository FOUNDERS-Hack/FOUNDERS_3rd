package com.samsung.open_crypto_wallet_app;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.samsung.open_crypto_wallet_app.model.AccountModel;

@Dao
public interface IAccountDAO {
    @Query("SELECT * FROM AccountModel")
    AccountModel[] getAllAccounts();

    @Query("SELECT * FROM AccountModel WHERE accountId =:accountId")
    AccountModel getDefaultAccountModel(int accountId);

    @Query("SELECT MAX(accountId) FROM AccountModel")
    int getMaxAccoundId();

    @Query("SELECT COUNT(accountId) FROM AccountModel")
    int getAccountCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAccountModel(AccountModel... accountModels);

    @Query("DELETE FROM AccountModel")
    void deleteAccounts();

    @Query("UPDATE AccountModel SET accountName =:accountName WHERE accountId=:accountId")
    void editAccountName(int accountId, String accountName);

    //deleteAccounts();
    //INSERT INTO AccountModel
    //VALUES (0,"0xab","m/44'/60'/0'/0/0",false);
}
