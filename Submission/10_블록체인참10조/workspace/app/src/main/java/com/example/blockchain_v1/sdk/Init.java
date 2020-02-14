package com.example.blockchain_v1.sdk;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.AccountManager;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.TransactionResult;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumUtils;
import com.samsung.android.sdk.blockchain.exception.AccountException;
import com.samsung.android.sdk.blockchain.exception.AvailabilityException;
import com.samsung.android.sdk.blockchain.exception.RemoteClientException;
import com.samsung.android.sdk.blockchain.exception.RootSeedChangedException;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletManager;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;
import org.web3j.crypto.RawTransaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Init {

    private SBlockchain mSBlockchain;
    private AccountManager accountManager;
    private HardwareWallet wallet;
    private HardwareWalletManager walletManager;
    private Account generatedAccount;
    private EthereumService etherService;
    private CoinNetworkInfo mCoinNetworkInfo;
    Context context;
    private Account myAccount;
    private BigInteger balance;
    public static final String CONTRACT_ADDRESS = "0x2a4884fd0505d96563e163cb41b00c2d7ce2df6a";
    public static final String endpoint_url = "ropsten.infura.io/v3/5a8c9d08293f4c4f9ad86af45a8a92ce";
    public Init(Context context){
        this.context = context;
        try {
            mSBlockchain = new SBlockchain();
            mSBlockchain.initialize(context);
        } catch (SsdkUnsupportedException e) {
            if (e.getErrorType() == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED) {
                Log.e("error", "Platform SDK is not supported");
            }
        }

        setNetwork(context);
        try {

            Thread.sleep(1000); //1초 대기

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        connect(mSBlockchain);
        try {

            Thread.sleep(1000); //1초 대기

        } catch (InterruptedException e) {

            e.printStackTrace();

        }


        getAccount();
        try {

            Thread.sleep(1000); //1초 대기

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

        // writeFirst();
        // writeAndDistribute();
        //getParentHash();
    }

    private void setNetwork(Context context) {
        mCoinNetworkInfo =
                new CoinNetworkInfo(
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN,
                        endpoint_url
                );

        etherService =
                (EthereumService) CoinServiceFactory
                        .getCoinService(
                                context.getApplicationContext(),
                                mCoinNetworkInfo
                        );
    }

    private void connect(SBlockchain mSBlockchain) {

        walletManager = mSBlockchain.getHardwareWalletManager();
        walletManager.connect(HardwareWalletType.SAMSUNG, true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                    @Override
                    public void onSuccess(HardwareWallet hardwareWallet) {
                        wallet = hardwareWallet;
                        Log.d("walletID", wallet.getWalletId());
                    }

                    @Override
                    public void onCancelled(InterruptedException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onFailure(ExecutionException e) {
                        e.printStackTrace();
                    }

                });
    }

    public void disConnect() {
        walletManager.disconnect(wallet);
    }

    public void getAccount() {
        HardwareWallet wallet = walletManager.getConnectedHardwareWallet();

        try {
            accountManager = mSBlockchain.getAccountManager();
        } catch (IllegalStateException e) {
            // handling exception
        }



        List<Account> accounts =
                mSBlockchain.getAccountManager()
                        .getAccounts(wallet.getWalletId(), CoinType.ETH, EthereumNetworkType.ROPSTEN);

        for (Account account : accounts) {
            Log.i(TAG, "Account: " + account.toString());
        }

        myAccount = accounts.get(0);
        etherService.getBalance(accounts.get(0)).setCallback(
                new ListenableFutureTask.Callback<BigInteger>() {
                    @Override
                    public void onSuccess(BigInteger result) {
                        //success
//                        balance = result;
//                        Log.d("balance", balance.toString());
                    }
                    @Override
                    public void onFailure(ExecutionException exception) {
                        //failure
                    }
                    @Override
                    public void onCancelled(@NotNull InterruptedException exception) {
                        //cancelled
                    }
                });


    }


    public void writeFirst() {

        BigInteger value= BigInteger.ZERO;

        try {
            etherService.sendSmartContractTransaction(
                    wallet, // wallet
                    (EthereumAccount)myAccount,  // from
                    CONTRACT_ADDRESS,   //to
                    new BigInteger("500000"),   // gas limit
                    BigInteger.valueOf(Long.parseLong("210000")), //gasprice
                    "0x469cf30a", // encoded function
                    value,   // value
                    BigInteger.ONE)   // nonce
                    .setCallback(
                            new ListenableFutureTask.Callback<TransactionResult>() {
                                @Override
                                public void onSuccess(TransactionResult result) {
                                    //success
                                    Log.d("result", "Hash : " + result.getHash());
                                }
                                @Override
                                public void onFailure(ExecutionException exception) {
                                    //failure
                                    exception.printStackTrace();

                                }
                                @Override
                                public void onCancelled(InterruptedException exception) {
                                    //cancelled
                                    Log.d("result", "canceclled");
                                }
                            });
        } catch (AvailabilityException e) {
            //handle exception
        }
    }

    public void writeAndDistribute() {

        BigInteger value= EthereumUtils.convertEthToWei(BigDecimal.valueOf(0.01));

        try {
            etherService.sendSmartContractTransaction(
                    wallet, // wallet
                    (EthereumAccount)myAccount,  // from
                    CONTRACT_ADDRESS,   //to
                    new BigInteger("500000"),
                    BigInteger.valueOf(Long.parseLong("210000")), //gasprice
                    "0x53575df6", // encoded function
                    value,   // value
                    BigInteger.ONE)   // nonce
                    .setCallback(
                            new ListenableFutureTask.Callback<TransactionResult>() {
                                @Override
                                public void onSuccess(TransactionResult result) {
                                    //success
                                    Log.d("result", "Hash : " + result.getHash());
                                }
                                @Override
                                public void onFailure(ExecutionException exception) {
                                    //failure
                                    exception.printStackTrace();

                                }
                                @Override
                                public void onCancelled(InterruptedException exception) {
                                    //cancelled
                                    Log.d("result", "canceclled");
                                }
                            });
        } catch (AvailabilityException e) {
            //handle exception
        }
    }

    public void getParentHash() {
        etherService
                .callSmartContractFunction(
                        (EthereumAccount)myAccount,
                        CONTRACT_ADDRESS,
                        "0x5355c533"
                )
                .setCallback(new ListenableFutureTask.Callback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        //success
                        Log.d("result", "Hash : " + result.toString());
                    }
                    @Override
                    public void onFailure(ExecutionException exception) {
                        //failure
                        exception.printStackTrace();
                        Log.d("result", "failed");
                    }
                    @Override
                    public void onCancelled(InterruptedException exception) {
                        //cancelled
                    }
                });
    }

/*
    public void sendTransaction() {

        BigInteger nonce = new BigInteger("1");
        BigInteger value= EthereumUtils.convertEthToWei(BigDecimal.valueOf(0.01));
        BigInteger gasPrice = EthereumUtils.convertGweiToWei(new BigDecimal("10"));
        BigInteger gasLimit = new BigInteger("21000");
        RawTransaction rawTx = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, (EthereumAccount)CONTRACT_ADDRESS, value, );



    }

 */
}