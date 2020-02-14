package com.samsung.uni_block_app.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.samsung.uni_block_app.util.Util;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;

import java.math.BigInteger;

public class PaymentService {
    private EthereumService ethereumService = (EthereumService) SBPManager.getInstance().getCoinService();
    private HardwareWallet hardwareWallet = SBPManager.getInstance().getHardwareWallet();

    public Intent buildDappProductPaymentSheet(Context context, EthereumAccount account, String toAddress, BigInteger productPrice
            , String data, BigInteger nonce) {
        Log.i(Util.LOG_TAG, "Starting service for payment sheet intent.");
        Intent intent = ethereumService.createEthereumPaymentSheetActivityIntent(context
                , hardwareWallet
                , account
                , toAddress
                , productPrice
                , data
                , nonce
        );
        return intent;
    }
}
