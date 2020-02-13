package com.samsung.open_crypto_wallet_app.view;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.android.sdk.coldwallet.ScwDeepLink;
import com.samsung.open_crypto_wallet_app.KeyStoreManager;
import com.samsung.open_crypto_wallet_app.NodeConnector;
import com.samsung.open_crypto_wallet_app.R;
import com.samsung.open_crypto_wallet_app.SharedPreferenceManager;
import com.samsung.open_crypto_wallet_app.Util;
import com.samsung.open_crypto_wallet_app.view_model.AccountViewModel;


public class SettingsFragment extends Fragment {

    private static NavActivity mNavActivityInstance;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ConstraintLayout keystoreSettings = view.findViewById(R.id.keystoreSettingsLayout);

        if (SharedPreferenceManager.getUseOtherKeyManager(mNavActivityInstance)) {
            keystoreSettings.setVisibility(View.GONE);
        } else {
            keystoreSettings.setVisibility(View.VISIBLE);

            TextView apiLevelNumberTextView = view.findViewById(R.id.apiLevelNumberTextView);
            apiLevelNumberTextView.setText(KeyStoreManager.getInstance(mNavActivityInstance).getKeystoreApiLevel().toString());

            TextView changePin = view.findViewById(R.id.textViewChangePin);
            setChangePinButtonOnClickListener(changePin);

            TextView launchSBK = view.findViewById(R.id.textViewLaunchSBK);
            setLaunchSBKOnClickListener(launchSBK);

            TextView revealSeed = view.findViewById(R.id.textViewRevealSeedWords);
            setRevealSeedOnClickListener(revealSeed);

            TextView resetWallet = view.findViewById(R.id.textViewResetWallet);
            setResetWalletOnClickListener(resetWallet);
        }


        setDefaultNetwork(SharedPreferenceManager.getDefaultNetwork(mNavActivityInstance), view);

        RadioGroup changeNetworkRadioGroup = view.findViewById(R.id.radioGroupNetwork);
        Button changeNetworkButton = view.findViewById(R.id.changeNetworkButton);
        setChangeNetworkButtonOnClickListener(changeNetworkButton, changeNetworkRadioGroup);

        Button openSourceLicenseButton = view.findViewById(R.id.openSourceButton);
        setOpenSourceLicenseOnClickListener(openSourceLicenseButton);

        super.onViewCreated(view, savedInstanceState);
    }

    private void setChangeNetworkButtonOnClickListener(Button changeNetworkButton, RadioGroup changeNetworkRadioGroup) {
        changeNetworkButton.setOnClickListener(v -> {
            Integer selectedNetworkRadioButtonID = changeNetworkRadioGroup.getCheckedRadioButtonId();
            if (selectedNetworkRadioButtonID == -1) {
                Log.e(Util.LOG_TAG, "No Network is selected");
                Toast.makeText(this.getContext(), "No network is selected", Toast.LENGTH_SHORT).show();
            } else {
                if (selectedNetworkRadioButtonID == R.id.radioButtonRopsten) {
                    SharedPreferenceManager.setDefaultNetwork(mNavActivityInstance, NodeConnector.ROPSTEN);
                } else if (selectedNetworkRadioButtonID == R.id.radioButtonKovan) {
                    SharedPreferenceManager.setDefaultNetwork(mNavActivityInstance, NodeConnector.KOVAN);
                } else if (selectedNetworkRadioButtonID == R.id.radioButtonMainNet) {
                    SharedPreferenceManager.setDefaultNetwork(mNavActivityInstance, NodeConnector.MAINNET);
                }
                Log.i(Util.LOG_TAG, "Network Changed");
                NodeConnector.reCreateNodeConnector(mNavActivityInstance);
                AccountViewModel.fetchBalance();
                Toast.makeText(this.getContext(), "Network has been changed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setDefaultNetwork(String defaultNetwork, View view) {
        //Ropsten is default network if no network is selected
        RadioButton radioButtonToBeSelected = view.findViewById(R.id.radioButtonRopsten);
        if (defaultNetwork.equals(NodeConnector.KOVAN)) {
            radioButtonToBeSelected = view.findViewById(R.id.radioButtonKovan);
        } else if (defaultNetwork.equals(NodeConnector.MAINNET)) {
            radioButtonToBeSelected = view.findViewById(R.id.radioButtonMainNet);
        }
        radioButtonToBeSelected.setChecked(true);
    }

    //Activity required to launch DeepLinks
    public static void setNavActivityInstance(NavActivity navActivity) {
        SettingsFragment.mNavActivityInstance = navActivity;
    }

    private void setChangePinButtonOnClickListener(TextView changePin) {
        changePin.setOnClickListener(view -> {
            Util.launchDeepLink(mNavActivityInstance, ScwDeepLink.CHANGE_PIN);
            Log.i(Util.LOG_TAG, "SBK Launched to Change PIN.");
        });
    }

    private void setLaunchSBKOnClickListener(TextView launchSBK) {
        launchSBK.setOnClickListener(view -> {
            Util.launchDeepLink(mNavActivityInstance, ScwDeepLink.MAIN);
            Log.i(Util.LOG_TAG, "SBK Launched.");
            mNavActivityInstance.finish();      //Redirects back to main, as SBK Wallet might change
        });
    }

    private void setRevealSeedOnClickListener(TextView revealSeed) {
        revealSeed.setOnClickListener(view -> {
            Util.launchDeepLink(mNavActivityInstance, ScwDeepLink.DISPLAY_WALLET);
            Log.i(Util.LOG_TAG, "SBK Launched to reveal Seed Words.");
        });
    }

    private void setResetWalletOnClickListener(TextView resetWallet) {
        resetWallet.setOnClickListener(view -> {
            Util.launchDeepLink(mNavActivityInstance, ScwDeepLink.RESET);
            Log.i(Util.LOG_TAG, "SBK Launched to Reset Wallet.");
            mNavActivityInstance.finish();       //Redirects back to main, as SBK Wallet might change
        });
    }


    private void setOpenSourceLicenseOnClickListener(Button openSourceLicenseButton) {
        openSourceLicenseButton.setOnClickListener(view -> {
            Fragment openSourceFragment = new OpenSourceLicenceFragment();
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, openSourceFragment).commit();
            }
        });

    }
}
