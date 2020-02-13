package com.example.customerapplication.presenter;


import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.customerapplication.OpenDoorLock;
import com.samsung.android.sdk.coldwallet.ScwCoinType;
import com.samsung.android.sdk.coldwallet.ScwService;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

public class WriteFeedPresenter {
    public static String address;
    public static String signedTxHex;
    public static final String CONTRACT_ADDRESS = "0x69f08eBAC35013b51F6a572B7852fC0dB57AE6E7";

    public static Function createGetPostCountSmartContractCall() {
        return new Function("openDoor"
                , Collections.emptyList()
                , singletonList(new TypeReference<Bool>() {
        }));
    }
    public static String makeTrx(String address) {


        Function func = createGetPostCountSmartContractCall();
        String data = FunctionEncoder.encode(func);
        Log.i("aaaa",data);
        return data;
    }

    public static void sign() {
        try {

            String hdPath = ScwService.getHdPath(ScwCoinType.ETH, 0);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());


            RawTransaction unsignedTransaction = RawTransaction.createTransaction(new BigInteger("10"), Convert.toWei("10", Convert.Unit.GWEI).toBigInteger()
                    , new BigInteger("41000"), CONTRACT_ADDRESS, Long.toString(timestamp.getTime()));


            byte[] byteArr = TransactionEncoder.encode(unsignedTransaction);

            ScwService.getInstance().signEthTransaction(new ScwService.ScwSignEthTransactionCallback() {
                @Override
                public void onSuccess(byte[] bytes) {
                    signedTxHex = Numeric.toHexString(bytes);
                    Log.i("rrr",signedTxHex);

                }
                @Override
                public void onFailure(int i, @Nullable String s) {
                    Log.e("signEth", s);
                }
            }, byteArr,hdPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ScwService.ScwGetAddressListCallback callback =
                new ScwService.ScwGetAddressListCallback() {
                    @Override
                    public void onSuccess(List<String> addressList) {
                        Log.i("hello", addressList.get(0)); //public address
                        address = addressList.get(0);
                        Log.i("qqq",address);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMessage) {
                        //handle errors

                    }
                };

        String hdPath = ScwService.getHdPath(ScwCoinType.ETH, 0);
        ArrayList<String> hdPathList = new ArrayList<>();
        hdPathList.add(hdPath);
        ScwService.getInstance().getAddressList(callback, hdPathList);
    }
    public static void getEthereumAddress() {
        ScwService.ScwGetAddressListCallback callback =
                new ScwService.ScwGetAddressListCallback() {
                    @Override
                    public void onSuccess(List<String> addressList) {
                        Log.i("hello", addressList.get(0)); //public address
                        address = addressList.get(0);
                        Log.i("qqq",address);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMessage) {
                        //handle errors

                    }
                };

        String hdPath = ScwService.getHdPath(ScwCoinType.ETH, 0);
        ArrayList<String> hdPathList = new ArrayList<>();
        hdPathList.add(hdPath);
        ScwService.getInstance().getAddressList(callback, hdPathList);
    }



}
