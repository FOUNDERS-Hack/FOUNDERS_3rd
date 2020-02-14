package com.samsung.uni_block_app.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.databinding.FragmentTokenAddBinding;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.AccountInformationViewModel;
import com.samsung.uni_block_app.viewmodel.TokenViewModel;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumTokenInfo;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTokenFragment extends Fragment {

    private TokenViewModel mTokenViewModel;
    private AccountInformationViewModel mAccountInformationViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAccountInformationViewModel = ViewModelProviders.of(getActivity()).get(AccountInformationViewModel.class);
        mTokenViewModel = ViewModelProviders.of(getActivity()).get(TokenViewModel.class);
        FragmentTokenAddBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_token_add, container, false);
        binding.setTokenViewModel(mTokenViewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(Util.LOG_TAG, "Add Token Fragment Launched.");

        EditText tokenAddressEditText = view.findViewById(R.id.token_address_edit_text);
        addTokenAddressSubmissionListener(tokenAddressEditText);

        ImageButton scanTokenAddressButton = view.findViewById(R.id.scan_token_address_qr_code_button);
        scanTokenAddressButton.setOnClickListener(scanTokenAddressButtonView -> startQRCodeScan(Util.REQUEST_CODE_TOKEN_ADDRESS));

        Button addTokenButton = view.findViewById(R.id.add_token_button);
        addTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tokenAddress = getTokenAddress();
                if (mTokenViewModel.isAddressValid(tokenAddress)) {
                    mTokenViewModel.addTokenToEthereumAccount((EthereumAccount) mAccountInformationViewModel.getSelectedAccount().getValue(), tokenAddress);
                } else {
                    AlertUtil.showAlertDialog(getActivity(), "Token Address not valid.");
                }
                UIUtil.setButtonEnabled(addTokenButton, false);
            }
        });
        UIUtil.setButtonEnabled(addTokenButton, false);

        //Make Token Info Displayed on UI Blank
        mTokenViewModel.getTokenInfo().postValue(new EthereumTokenInfo());

        mTokenViewModel.getTokenInfoFetchedSuccessfullyFlag().observe(this, responseValue -> {
            if (responseValue == Util.NEGATIVE_RESPONSE) {
                AlertUtil.showAlertDialog(getActivity(), Util.removeSDKSignatureFromErrorMessage(mTokenViewModel.getErrorMessage()));
            } else if (responseValue == Util.POSITIVE_RESPONSE) {
                UIUtil.setButtonEnabled(addTokenButton, true);
            }
            Util.neutralizeConsumedFlag(mTokenViewModel.getTokenInfoFetchedSuccessfullyFlag());
        });

        mTokenViewModel.getTokenAddedSuccessfullyFlag().observe(this, responseValue -> {
            if (responseValue == Util.POSITIVE_RESPONSE) {
                mTokenViewModel.getTokenInfo().postValue(new EthereumTokenInfo());
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new SendTokenFragment()).commit();
            } else if (responseValue == Util.NEGATIVE_RESPONSE) {
                AlertUtil.showAlertDialog(getActivity(), Util.removeSDKSignatureFromErrorMessage(mTokenViewModel.getErrorMessage()));
            }
            Util.neutralizeConsumedFlag(mTokenViewModel.getTokenAddedSuccessfullyFlag());
        });

    }

    private void addTokenAddressSubmissionListener(EditText tokenAddressEditText) {
        tokenAddressEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                boolean isValueSubmitted = (keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER);
                if (isValueSubmitted) {
                    String inputString = tokenAddressEditText.getText().toString();
                    validateTokenAddress(inputString);
                    return true;
                }
                return false;
            }
        });
    }

    private void validateTokenAddress(String inputString) {
        boolean isAddressValid = mTokenViewModel.isAddressValid(inputString);
        if (!isAddressValid) {
            AlertUtil.showAlertDialog(getActivity(), "Token Address is not valid");
        } else {
            requestTokenInformation(inputString);
        }
    }

    private void startQRCodeScan(int requestCode) {
        FragmentAndQRIntentIntegrator intentIntegrator = new FragmentAndQRIntentIntegrator(this);
        intentIntegrator.setRequestCode(requestCode);
        intentIntegrator.initiateScan();
    }

    private void setTokenAddressOnUI(String tokenContractAddress) {
        EditText tokenAddressEditText = getView().findViewById(R.id.token_address_edit_text);
        tokenAddressEditText.setText(tokenContractAddress);
    }

    private void requestTokenInformation(String tokenAddress) {
        showProgressBar(true);
        //Set Blank Token Info as Placeholder
        mTokenViewModel.getTokenInfo().postValue(new EthereumTokenInfo());
        //Fetch real Token Info
        mTokenViewModel.fetchTokenInformation(tokenAddress);
    }

    private void showProgressBar(Boolean isVisible) {
        mTokenViewModel.getTokenInformationIsLoading().postValue(isVisible);
    }

    private String getTokenAddress() {
        EditText tokenAddressEditText = getView().findViewById(R.id.token_address_edit_text);
        return tokenAddressEditText.getText().toString();
    }

    //When QRCode Activity closes, it returns the result here
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(Util.LOG_TAG, "On QR Code Scan Activity Result reached.");
        Log.i(Util.LOG_TAG, "Request Code: " + requestCode);
        Log.i(Util.LOG_TAG, "Result Code: " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            Log.i(Util.LOG_TAG, "QR Scan result returned.");
            String resultAddress = Util.formatScannedAddress(data.getStringExtra("SCAN_RESULT"));
            Log.i(Util.LOG_TAG, "Result Address: " + resultAddress);
            if (mTokenViewModel.isAddressValid(resultAddress)) {
                setTokenAddressOnUI(resultAddress);
                requestTokenInformation(resultAddress);
            } else {
                AlertUtil.showAlertDialog(getActivity(), "Token Address is not valid.");
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(Util.LOG_TAG, "QR Scan cancelled.");
        }
    }

}
