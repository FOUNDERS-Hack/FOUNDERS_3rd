package com.example.customerapplication.presenter;


import android.text.TextUtils;
import android.util.Log;

import com.example.customerapplication.OpenDoorLock;
import com.samsung.android.sdk.coldwallet.ScwCoinType;
import com.samsung.android.sdk.coldwallet.ScwService;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

public class WriteFeedPresenter {
    public static String address;

    public static final String CONTRACT_ADDRESS = "0x5dFcDc09966250F5ec678d1D28695211CEd672bb";

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
