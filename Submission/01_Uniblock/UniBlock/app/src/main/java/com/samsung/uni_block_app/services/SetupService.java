package com.samsung.uni_block_app.services;

import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.network.NetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import java.util.ArrayList;
import java.util.List;

public class SetupService {

    private SBPManager mSBPManager = SBPManager.getInstance();

    public ListenableFutureTask<HardwareWallet> connectHardwareWallet(HardwareWalletType selectedHardwareWalletType) {
        ListenableFutureTask<HardwareWallet> connectionTask = mSBPManager.connectHardwareWallet(selectedHardwareWalletType);
        return connectionTask;
    }

    public void disconnectHardwareWallet() {
        mSBPManager.disconnectHardwareWallet();
    }

    public HardwareWallet getHardwareWallet() {
        return mSBPManager.getHardwareWallet();
    }

    public void setHardwareWallet(HardwareWallet connectedHardwareWallet) {
        mSBPManager.setHardwareWallet(connectedHardwareWallet);
    }

    public ArrayList<HardwareWalletType> getWalletTypeList() {
        ArrayList<HardwareWalletType> supportedColdWallets = mSBPManager.getSBlockchain().getSupportedHardwareWallet();
        return supportedColdWallets;
    }

    public List<CoinType> getCoinTypeList() {
        List<CoinType> supportedCoins = mSBPManager.getSBlockchain()
                .getHardwareWalletManager()
                .getConnectedHardwareWallet()
                .getSupportedCoins();

        return supportedCoins;
    }

    public List<CoinType> getSupportedCoins() {
        List<CoinType> supportedCoinTypes = mSBPManager.getHardwareWallet().getSupportedCoins();
        return supportedCoinTypes;
    }

    public EthereumNetworkType[] getNetworkTypeList() {
        return EthereumNetworkType.values();
    }

    public void setCoinNetworkInfo(CoinType selectedCoinType, NetworkType selectedNetworkType, String rpcString) {
        mSBPManager.setCoinNetworkInfo(new CoinNetworkInfo(selectedCoinType, selectedNetworkType, rpcString));
    }

    public CoinNetworkInfo getCoinNetworkInfo() {
        return mSBPManager.getCoinNetworkInfo();
    }
}
