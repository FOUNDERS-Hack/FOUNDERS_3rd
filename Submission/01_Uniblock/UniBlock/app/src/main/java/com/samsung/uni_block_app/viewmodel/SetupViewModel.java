package com.samsung.uni_block_app.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.model.CoinTypeModel;
import com.samsung.uni_block_app.model.HardwareWalletTypeModel;
import com.samsung.uni_block_app.model.NetworkTypeModel;
import com.samsung.uni_block_app.services.SetupService;
import com.samsung.uni_block_app.util.Util;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.network.NetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SetupViewModel extends AndroidViewModel {

    private SetupService mSetupService = new SetupService();
    private MutableLiveData<Integer> connectedToHardwareWalletFlag = new MutableLiveData<>();
    private MutableLiveData<Boolean> startButtonClicked = new MutableLiveData<>();

    //Trial code should be removed
    private String errorMessage;

    public SetupViewModel(@NonNull Application application) {
        super(application);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    //This flag checks whether Connection to HW Wallet has been established successfully
    public MutableLiveData<Integer> isConnectedToHardwareWallet() {
        return connectedToHardwareWalletFlag;
    }

    public MutableLiveData<Boolean> getStartButtonClicked() {
        return startButtonClicked;
    }

    public void setStartButtonClicked(Boolean bool) {
        startButtonClicked.postValue(bool);
    }

    public void connectHardwareWallet(HardwareWalletType selectedHardwareWalletType) {

        ListenableFutureTask<HardwareWallet> connectionTask = mSetupService.connectHardwareWallet(selectedHardwareWalletType);
        connectionTask.setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
            @Override
            public void onSuccess(HardwareWallet hardwareWallet) {
                Log.i(Util.LOG_TAG, "Connected to Hardware wallet.");
                mSetupService.setHardwareWallet(hardwareWallet);
                connectedToHardwareWalletFlag.postValue(Util.POSITIVE_RESPONSE);
            }

            @Override
            public void onFailure(@NotNull ExecutionException e) {
                Log.e(Util.LOG_TAG, "Could not connect to Hardware wallet");
                Util.printErrorMessage(e);
                setErrorMessage(e.getMessage());
                connectedToHardwareWalletFlag.postValue(Util.NEGATIVE_RESPONSE);
            }

            @Override
            public void onCancelled(@NotNull InterruptedException e) {
                Log.d(Util.LOG_TAG, "Hardware wallet connection operation Canceled");
                Util.printErrorMessage(e);
                setErrorMessage(e.getMessage());
                connectedToHardwareWalletFlag.postValue(Util.NEGATIVE_RESPONSE);
            }
        });

    }

    public HardwareWallet getHardwareWallet() {
        return mSetupService.getHardwareWallet();
    }

    public void disconnectHardwareWallet() {
        mSetupService.disconnectHardwareWallet();
    }

    public ArrayList<HardwareWalletTypeModel> getWalletModelList() {
        ArrayList<HardwareWalletTypeModel> hardwareWalletTypeModelList = new ArrayList<>();
        //Get supported HW list
        Log.i(Util.LOG_TAG, "Calling service for wallet list.");
        ArrayList<HardwareWalletType> supportedHardwareWalletTypeList = mSetupService.getWalletTypeList();
        //Process list
        hardwareWalletTypeModelList.add(new HardwareWalletTypeModel(1, "Samsung KeyStore",
                R.drawable.ic_icon_samsung_keystore, supportedHardwareWalletTypeList.get(0)));
        hardwareWalletTypeModelList.add(new HardwareWalletTypeModel(2, "Ledger Nano USB",
                R.drawable.ic_icon_ledger, supportedHardwareWalletTypeList.get(1)));
        hardwareWalletTypeModelList.add(new HardwareWalletTypeModel(3, "Ledger Nano BLE",
                R.drawable.ic_icon_ledger, supportedHardwareWalletTypeList.get(2)));
        //return list
        return hardwareWalletTypeModelList;
    }

    public ArrayList<CoinTypeModel> getCoinTypeModelList() {
        ArrayList<CoinTypeModel> coinTypeModelArrayList = new ArrayList<CoinTypeModel>();
        //Get Coin Type list from service
        Log.i(Util.LOG_TAG, "Calling service for coin type list.");
        List<CoinType> coinTypeList = mSetupService.getCoinTypeList();
        //Process coin type list into Coin type model list so that it is presentable in the UI

        int coinIndex = 1;
        for (CoinType coinType : coinTypeList) {
            coinTypeModelArrayList.add(new CoinTypeModel(coinIndex++, "Ethereum", R.drawable.ic_icon_ethereum, coinType));
        }//Return list
        return coinTypeModelArrayList;
    }

    public ArrayList<NetworkTypeModel> getNetworkTypeModelList() {
        ArrayList<NetworkTypeModel> networkTypeModelArrayList = new ArrayList<NetworkTypeModel>();
        //Get Network Type list from service
        Log.i(Util.LOG_TAG, "Calling service for coin type list.");
        NetworkType[] networkTypeList = mSetupService.getNetworkTypeList();
        //Process Network type list into Coin type model list so that it is presentable in the UI
        networkTypeModelArrayList.add(new NetworkTypeModel(1, "Main Ethereum Network",
                R.drawable.ic_icon_network, networkTypeList[0]));
        networkTypeModelArrayList.add(new NetworkTypeModel(2, "Ropsten Test Network",
                R.drawable.ic_icon_network, networkTypeList[1]));
        networkTypeModelArrayList.add(new NetworkTypeModel(3, "Kovan Test Network",
                R.drawable.ic_icon_network, networkTypeList[2]));
        networkTypeModelArrayList.add(new NetworkTypeModel(4, "Rinkeby Test Network",
                R.drawable.ic_icon_network, networkTypeList[3]));
        return networkTypeModelArrayList;
    }

    public List<CoinType> getSupportedCoinTypes() {
        return mSetupService.getSupportedCoins();
    }

    public void setCoinNetworkInfo(CoinType selectedCoinType, NetworkType selectedNetworkType, String rpcString) {
        mSetupService.setCoinNetworkInfo(selectedCoinType, selectedNetworkType, rpcString);
    }

    public CoinNetworkInfo getCoinNetworkInfo() {
        return mSetupService.getCoinNetworkInfo();
    }
}
