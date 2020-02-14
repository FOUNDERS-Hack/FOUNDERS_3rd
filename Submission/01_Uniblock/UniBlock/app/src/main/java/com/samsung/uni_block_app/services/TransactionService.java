package com.samsung.uni_block_app.services;

import android.content.Context;

import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinService;
import com.samsung.android.sdk.blockchain.coinservice.FeeInfo;
import com.samsung.android.sdk.blockchain.coinservice.TransactionResult;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.exception.AvailabilityException;

import java.math.BigInteger;

public class TransactionService {
    private SBPManager mSBPManager = SBPManager.getInstance();

    public ListenableFutureTask<BigInteger> getBalance(Account account) {
        return mSBPManager.getCoinService().getBalance(account);
    }

    public CoinNetworkInfo getCoinNetworkInfo() {
        return mSBPManager.getCoinNetworkInfo();
    }

    public CoinService getCoinService() {
        return mSBPManager.getCoinService();
    }

    public void setCoinService(Context applicationContext) {
        mSBPManager.setCoinService(applicationContext);
    }

    public EthereumService getEthereumService() {
        return (EthereumService) mSBPManager.getCoinService();
    }

    public boolean isAddressValid(String receiversAddress) {
        return mSBPManager.getCoinService().isValidAddress(receiversAddress);
    }

    public ListenableFutureTask<FeeInfo> getFeeInformation() {
        return mSBPManager.getCoinService().getFeeInfo();
    }

    public ListenableFutureTask<BigInteger> estimateEthereumGasLimit(EthereumAccount account, String toAddress, BigInteger value) {
        EthereumService ethereumService = (EthereumService) mSBPManager.getCoinService();
        return ethereumService.estimateGasLimit(account, toAddress, value, null);
    }

    public ListenableFutureTask<TransactionResult> sendEthereumTransaction(Account fromAccount, BigInteger gasPrice, BigInteger gasLimit, String toAddress, BigInteger amount) throws AvailabilityException {
        EthereumService ethereumService = (EthereumService) mSBPManager.getCoinService();
        return ethereumService.sendTransaction(mSBPManager.getHardwareWallet(), (EthereumAccount) fromAccount,
                gasPrice, gasLimit, toAddress, amount, null);
    }

}
