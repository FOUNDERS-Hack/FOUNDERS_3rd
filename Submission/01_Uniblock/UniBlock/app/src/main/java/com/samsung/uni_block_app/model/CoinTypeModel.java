package com.samsung.uni_block_app.model;

import com.samsung.android.sdk.blockchain.CoinType;

public class CoinTypeModel extends SetupModel {
    private CoinType coinType;

    public CoinTypeModel(int coinId, String coinName, int coinImageId, CoinType coinType) {
        super(coinId, coinName, coinImageId);
        this.coinType = coinType;
    }

    public CoinType getCoinType() {
        return coinType;
    }

    public void setCoinType(CoinType coinType) {
        this.coinType = coinType;
    }
}
