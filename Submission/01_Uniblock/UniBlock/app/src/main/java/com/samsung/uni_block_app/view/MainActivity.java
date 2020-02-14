package com.samsung.uni_block_app.view;

import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.databinding.ActivityMainBinding;
import com.samsung.uni_block_app.services.SBPManager;
import com.samsung.uni_block_app.services.SharedPreferenceManager;
import com.samsung.uni_block_app.services.TokenService;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.SetupViewModel;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.network.NetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

public class MainActivity extends AppCompatActivity {

    private SetupViewModel mSetupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mSetupViewModel = ViewModelProviders.of(this).get(SetupViewModel.class);
        binding.setSetupViewModel(mSetupViewModel);
        //Setting lifecycle owner is necessary while using liveData
        binding.setLifecycleOwner(this);

        SharedPreferenceManager.setSharedPreferenceStorage(getApplication());
    }

    public void onClickGetStarted(View view) {
        Log.i(Util.LOG_TAG, "Into universe clicked.");

        // Check Internet Connectivity, If unavailable won't allow proceeding
        if (!Util.isInternetConnectionAvailable()) {
            AlertUtil.showActivityCloseAlertDialog(this, Util.CONNECTION_ERROR, false);
            return;
        }

        // Hide Get Started Button
        mSetupViewModel.setStartButtonClicked(true);

        if (dataIsPresentInSharedPreference()) {
            try {
                SBPManager.getInstance().initializeSBlockChain(getApplicationContext());

                String storedHardwareWalletType = SharedPreferenceManager.getHardwareWalletTypeString();
                HardwareWalletType hardwareWalletType = HardwareWalletType.valueOf(storedHardwareWalletType);

                mSetupViewModel.isConnectedToHardwareWallet().observe(this, responseValue -> {
                    if (responseValue == Util.POSITIVE_RESPONSE) {
                        Log.i(Util.LOG_TAG, "Connected to Hardware Wallet!");
                        launchNavigationActivity();
                    } else if (responseValue == Util.NEGATIVE_RESPONSE) {
                        AlertUtil.showAlertDialog(MainActivity.this, mSetupViewModel.getErrorMessage());
                    }
                    Util.neutralizeConsumedFlag(mSetupViewModel.isConnectedToHardwareWallet());
                });

                mSetupViewModel.connectHardwareWallet(hardwareWalletType);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            launchWalletSelectionActivity();
        }
    }

    //Dummy function to be filled later
    private boolean dataIsPresentInSharedPreference() {
        return SharedPreferenceManager.isValid();
    }

    private void launchNavigationActivity() {
        Intent navigationActivityIntent = new Intent(this, NavigationActivity.class);

        Bundle dataBundle = new Bundle();

        String storedCoinType = SharedPreferenceManager.getCoinTypeString();
        CoinType coinType = CoinType.valueOf(storedCoinType);
        dataBundle.putSerializable(Util.COIN_TYPE, coinType);

        String storedNetworkType = SharedPreferenceManager.getNetworkTypeString();
        NetworkType networkType;
        switch (coinType) {
            case ETH:
                networkType = EthereumNetworkType.valueOf(storedNetworkType);
                dataBundle.putSerializable(Util.NETWORK_TYPE, (EthereumNetworkType) networkType);
                break;
            default:
                String msg = "Uniblock do not support this CoinType";
                Log.e(Util.LOG_TAG, msg);
                AlertUtil.showAlertDialog(this, msg);
                break;
        }

        navigationActivityIntent.putExtras(dataBundle);
        //Make newly launched activity root activity
        navigationActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(navigationActivityIntent);
    }

    private void launchWalletSelectionActivity() {
        Intent navigationActivityIntent = new Intent(this, WalletSelectionActivity.class);
        //Make newly launched activity root activity
        navigationActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(navigationActivityIntent);
    }
    
}
