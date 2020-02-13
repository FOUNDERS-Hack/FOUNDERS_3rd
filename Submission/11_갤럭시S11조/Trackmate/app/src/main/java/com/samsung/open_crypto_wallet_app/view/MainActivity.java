package com.samsung.open_crypto_wallet_app.view;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.samsung.open_crypto_wallet_app.DBManager;
import com.samsung.open_crypto_wallet_app.KeyStoreManager;
import com.samsung.open_crypto_wallet_app.NodeConnector;
import com.samsung.open_crypto_wallet_app.R;
import com.samsung.open_crypto_wallet_app.SharedPreferenceManager;
import com.samsung.open_crypto_wallet_app.Util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;

public class MainActivity extends AppCompatActivity {

    ObservableBoolean mIsMandatoryUpdateRequired;
    ObservableBoolean mIsCheckUpdateTaskCompleted;

    Activity mActivity;

    boolean mUseOtherKeyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActivity = this;
        mUseOtherKeyManager = false;

        Log.i(Util.LOG_TAG, "Initial Page: Main Activity Load Successful.");

        setupBouncyCastle();
    }

    @Override
    protected void onResume() {
        findViewById(R.id.get_started_button).setVisibility(View.VISIBLE);
        findViewById(R.id.app_opening_progressbar).setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        NodeConnector.getInstance(this).shutDown();
        DBManager.closeDB();
        super.onDestroy();
    }

    public void onClickGetStarted(View view) {
        Log.i(Util.LOG_TAG, "Initial Page: onClickGetStarted()");

        // Internet Connectivity Check with Google's Public DNS
        if (!Util.isInternetConnectionAvailable()) {
            AlertUtil.showInternetConnectionNotAvailableDialog(this);
            Log.d(Util.LOG_TAG, "No Internet Connection at Init");
            return;
        }

        // Check if SBK is supported on the device or not
        if (!KeyStoreManager.getInstance(mActivity).isSBKSupported()) {
            Log.d(Util.LOG_TAG, "SBK is not supported on Device");

            setUseOtherKeyManager(true);
            SharedPreferenceManager.setUseOtherKeyManager(this, true);

            Intent intent = new Intent(this, NavActivity.class);
            startActivity(intent);
            Log.i(Util.LOG_TAG, "Launching NavActivity");

            return;

        }

        //Hide Get Started Button Show Progressbar
        findViewById(R.id.get_started_button).setVisibility(View.GONE);
        findViewById(R.id.app_opening_progressbar).setVisibility(View.VISIBLE);

        setUseOtherKeyManager(false);
        SharedPreferenceManager.setUseOtherKeyManager(this, false);

        // Check if required API level is not matched
        if (!Util.isAPILevelMatched(mActivity)) {
            AlertUtil.showUpdateRequireDialog(this);
            Log.d(Util.LOG_TAG, "SBK update required.");
            return;
        }

        // Check if SBK Wallet is set
        if (!KeyStoreManager.getInstance(mActivity).isSBKWalletSet()) {
            AlertUtil.SBKWalletNotSetDialog(this);
            Log.d(Util.LOG_TAG, "SBK wallet not set. Need to jump to SBK to create a wallet");
            return;
        }

        // Check if Mandatory Update is required
        checkMandatoryUpdateAndLaunch();

        return;


    }

    public void setUseOtherKeyManager(boolean flag) {
        mUseOtherKeyManager = flag;
    }

    private void checkMandatoryUpdateAndLaunch() {
        mIsCheckUpdateTaskCompleted = new ObservableBoolean(false);     // To Monitor CheckMandatoryUpdateTask being completed or not
        mIsMandatoryUpdateRequired = new ObservableBoolean(true);       // Assuming Update is 'required' at Init

        Observable.OnPropertyChangedCallback checkUpdateTaskCompletedCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.i(Util.LOG_TAG, "Check Mandatory Update Task Completed");
                launchNavActivity();
            }
        };

        mIsCheckUpdateTaskCompleted.addOnPropertyChangedCallback(checkUpdateTaskCompletedCallback);
        //Async checkMandatoryUpdateRequired method
        KeyStoreManager.getInstance(mActivity).checkMandatoryUpdateRequired(mIsMandatoryUpdateRequired, mIsCheckUpdateTaskCompleted);
    }

    public void launchNavActivity() {

        if (!mUseOtherKeyManager && mIsMandatoryUpdateRequired.get()) {
            AlertUtil.showUpdateRequireDialog(this);
            Log.d(Util.LOG_TAG, "SBK Mandatory update required.");
        }

        // Launching NavActivity if mandatoryUpdate is not required
        else {
            Intent intent = new Intent(this, NavActivity.class);
            startActivity(intent);
            Log.i(Util.LOG_TAG, "Launching NavActivity");
        }
    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }
}
