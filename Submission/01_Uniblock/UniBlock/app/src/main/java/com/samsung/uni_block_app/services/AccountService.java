package com.samsung.uni_block_app.services;

import android.util.Log;

import com.samsung.uni_block_app.util.Util;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;

import java.util.List;

public class AccountService {

    private SBPManager mSBPManager = SBPManager.getInstance();

    public List<Account> getAccounts() {
        List<Account> accountsList = mSBPManager.getAccountManager().getAccounts(mSBPManager.getHardwareWallet().getWalletId(),
                mSBPManager.getCoinNetworkInfo().getCoinType(),
                mSBPManager.getCoinNetworkInfo().getNetworkType());

        for (Account account : accountsList) {
            Log.i(Util.LOG_TAG, "Account: " + account.toString());
        }

        return accountsList;
    }

    public ListenableFutureTask<Account> generateNewAccount() {
        return mSBPManager.getAccountManager().generateNewAccount(mSBPManager.getHardwareWallet(), mSBPManager.getCoinNetworkInfo());
    }

    public CoinNetworkInfo getCoinNetworkInfo() {
        return mSBPManager.getCoinNetworkInfo();
    }

    public ListenableFutureTask<Boolean> restoreAccounts(){
        return mSBPManager.getAccountManager().restoreAccounts(mSBPManager.getHardwareWallet(),true,mSBPManager.getCoinNetworkInfo(),
                                                                            Util.getHdPath(mSBPManager.getCoinNetworkInfo().getCoinType()));
    }

}
