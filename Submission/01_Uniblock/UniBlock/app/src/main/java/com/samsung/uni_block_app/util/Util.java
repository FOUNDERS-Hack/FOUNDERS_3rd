package com.samsung.uni_block_app.util;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumUtils;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.network.NetworkType;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Util {

    public static final String LOG_TAG = "AeroWallet";
    public static final String RPC_URL_ROPSTEN = "https://ropsten.infura.io/v3/71cf9f88c5b54962a394d80890e41d5b";
    public static final String RPC_URL_KOVAN = "https://kovan.infura.io/v3/71cf9f88c5b54962a394d80890e41d5b";
    public static final String RPC_URL_RINKEBY = "https://rinkeby.infura.io/v3/71cf9f88c5b54962a394d80890e41d5b";
    public static final String RPC_URL_MAINNET = "https://mainnet.infura.io/v3/71cf9f88c5b54962a394d80890e41d5b";

    public static final int NEGATIVE_RESPONSE = -1;
    public static final int NEUTRAL_RESPONSE = 0;
    public static final int POSITIVE_RESPONSE = 1;

    public static final int REQUEST_CODE_TOKEN_ADDRESS = 2;
    public static final int REQUEST_CODE_RECIPIENT_ADDRESS = 3;

    public static final int TRANSACTION_SPEED_SLOW = 4;
    public static final int TRANSACTION_SPEED_NORMAL = 5;
    public static final int TRANSACTION_SPEED_FAST = 6;

    public static final int TRANSACTION_INSUFFICIENT_BALANCE = 7;
    public static final int FILE_PERMISSION_REQUEST_CODE = 8;

    public static final String COIN_TYPE = "COIN_TYPE";
    public static final String NETWORK_TYPE = "NETWORK_TYPE";

    public static final String CONNECTION_ERROR = "Please check internet connection";
    public static final String WALLET_NOT_SET = "Please configure your hardware-wallet first";

    private Util() {
    }

    public static Bitmap generateQRCode(String text) {

        int qrCodeWidth = 10;
        int qrCodeHeight = 10;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap = Bitmap.createBitmap(qrCodeWidth, qrCodeHeight, Bitmap.Config.RGB_565);
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            Log.i(Util.LOG_TAG, "Could not create QR Code bitmap!");
            Util.printErrorMessage(e);
        }
        return bitmap;
    }

    public static void printErrorMessage(Exception exception) {
        Log.e(Util.LOG_TAG, "Error message: " + exception.getMessage());
    }

    public static String getRPCString(NetworkType selectedNetworkType) {
        String rpcString;
        if (selectedNetworkType.equals(EthereumNetworkType.ROPSTEN)) {
            rpcString = RPC_URL_ROPSTEN;
        } else if (selectedNetworkType.equals(EthereumNetworkType.KOVAN)) {
            rpcString = RPC_URL_KOVAN;
        } else if (selectedNetworkType.equals(EthereumNetworkType.RINKEBY)) {
            rpcString = RPC_URL_RINKEBY;
        } else if (selectedNetworkType.equals(EthereumNetworkType.MAINNET)) {
            rpcString = RPC_URL_MAINNET;
        } else {
            rpcString = "";
        }
        return rpcString;
    }

    public static String getHdPath(CoinType selectedCoinType) {
        String hdPathString = "";
        switch (selectedCoinType){
            case ETH:
                hdPathString = "m/44'/60'/0'/0";
                break;
        }
        return hdPathString;
    }

    public static String formatScannedAddress(String unformattedString) {
        String formattedString = unformattedString.replace("ethereum:", "");
        return formattedString;
    }

    public static BigDecimal convertToETH(BigInteger input) {
        BigDecimal balanceInEther = EthereumUtils.convertWeiToEth(input);
        return balanceInEther;
    }

    public static BigInteger convertToWEI(BigDecimal input) {
        BigInteger balanceInWEI = EthereumUtils.convertEthToWei(input);
        return balanceInWEI;
    }

    public static boolean isInternetConnectionAvailable() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");  // ping Google Public DNS
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            Log.i(Util.LOG_TAG, "Internet is not available!");
            Util.printErrorMessage(e);
        }
        return false;
    }

    public static String trimBalanceString(String balance) {
        if (balance.length() > 8) {
            return balance.substring(0, 7);
        } else return balance;
    }

    public static String removeSDKSignatureFromErrorMessage(String errorMessage) {
        int indexOfColon = errorMessage.indexOf(':');
        String cleanErrorMessage = errorMessage.substring(indexOfColon + 1);
        return cleanErrorMessage;
    }

    public static void neutralizeConsumedFlag(MutableLiveData<Integer> flag) {
        if (flag.getValue() != NEUTRAL_RESPONSE) {
            flag.postValue(NEUTRAL_RESPONSE);
        }
    }

}