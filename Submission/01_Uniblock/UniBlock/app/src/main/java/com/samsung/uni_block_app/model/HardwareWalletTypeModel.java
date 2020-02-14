package com.samsung.uni_block_app.model;

import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

public class HardwareWalletTypeModel extends SetupModel {

    private HardwareWalletType walletType;

    public HardwareWalletTypeModel(int walletId, String walletName, int walletImageId, HardwareWalletType walletType) {
        super(walletId, walletName, walletImageId);
        this.walletType = walletType;
    }

    public HardwareWalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(HardwareWalletType walletType) {
        this.walletType = walletType;
    }
}
