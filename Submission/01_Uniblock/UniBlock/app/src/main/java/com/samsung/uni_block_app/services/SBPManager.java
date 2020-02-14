package com.samsung.uni_block_app.services;

import android.content.Context;
import android.util.Log;

import com.samsung.uni_block_app.util.Util;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.AccountManager;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinService;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumUtils;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletManager;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SBPManager {

    private static SBPManager INSTANCE;
    private SBlockchain SBlockchain;
    private HardwareWalletManager hardwareWalletManager;
    private AccountManager accountManager;


    private HardwareWallet hardwareWallet;
    private CoinNetworkInfo coinNetworkInfo;
    private CoinService coinService;

    private BigInteger ethereumGasPriceSlow = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(4)).toBigInteger();
    private BigInteger ethereumGasPriceNormal = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(10)).toBigInteger();
    private BigInteger ethereumGasPriceFast = EthereumUtils.convertEthToGwei(BigDecimal.valueOf(20)).toBigInteger();

    // private Constructor for Singleton design
    private SBPManager() {
    }

    public static SBPManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SBPManager();
            INSTANCE.setSBlockchain(new SBlockchain());
        }
        return INSTANCE;
    }

    public void initializeSBlockChain(Context context) {
        try {
            SBlockchain.initialize(context);
            hardwareWalletManager = SBlockchain.getHardwareWalletManager();
            accountManager = SBlockchain.getAccountManager();

        } catch (SsdkUnsupportedException e) {
            Log.e(Util.LOG_TAG, "Could not initialize SBK.");
            Log.e(Util.LOG_TAG, "Error message: " + e.getMessage());
        }
    }

    public ListenableFutureTask<HardwareWallet> connectHardwareWallet(HardwareWalletType hardwareWalletType) {
        if (SBlockchain == null) {
            Log.e(Util.LOG_TAG, "SBlockchain not initialized.");
        }

        ListenableFutureTask<HardwareWallet> connectionTask = hardwareWalletManager.connect(hardwareWalletType, true);
        return connectionTask;
    }

    public void disconnectHardwareWallet() {
        if (hardwareWallet != null) {
            hardwareWalletManager.disconnect(hardwareWallet);
        }
    }

    public SBlockchain getSBlockchain() {
        return SBlockchain;
    }

    public void setSBlockchain(SBlockchain mSBlockchain) {
        this.SBlockchain = mSBlockchain;
    }

    public CoinNetworkInfo getCoinNetworkInfo() {
        return coinNetworkInfo;
    }

    public void setCoinNetworkInfo(CoinNetworkInfo coinNetworkInfo) {
        this.coinNetworkInfo = coinNetworkInfo;
    }

    public HardwareWallet getHardwareWallet() {
        return hardwareWallet;
    }

    public void setHardwareWallet(HardwareWallet hardwareWallet) {
        this.hardwareWallet = hardwareWallet;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public CoinService getCoinService() {
        return coinService;
    }

    public void setCoinService(Context applicationContext) {
        this.coinService = CoinServiceFactory.getCoinService(applicationContext, coinNetworkInfo);
    }

    // Setting GasPrice & GasLimit as per Metamask
    public BigInteger getEthereumGasPriceSlow() {
        return ethereumGasPriceSlow;
    }

    public BigInteger getEthereumGasPriceNormal() {
        return ethereumGasPriceNormal;
    }

    public BigInteger getEthereumGasPriceFast() {
        return ethereumGasPriceFast;
    }

}
