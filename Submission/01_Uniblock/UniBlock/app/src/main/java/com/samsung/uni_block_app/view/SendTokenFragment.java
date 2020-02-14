package com.samsung.uni_block_app.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.adapter.TokenAdapter;
import com.samsung.uni_block_app.databinding.FragmentTokenSendBinding;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.AccountInformationViewModel;
import com.samsung.uni_block_app.viewmodel.TokenViewModel;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumTokenInfo;

import java.math.BigDecimal;
import java.util.List;

public class SendTokenFragment extends Fragment {

    private AccountInformationViewModel mAccountInformationViewModel;
    private TokenViewModel mTokenViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAccountInformationViewModel = ViewModelProviders.of(getActivity()).get(AccountInformationViewModel.class);
        mTokenViewModel = ViewModelProviders.of(getActivity()).get(TokenViewModel.class);
        FragmentTokenSendBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_token_send, container, false);
        binding.setAccountInformationViewModel(mAccountInformationViewModel);
        binding.setTokenViewModel(mTokenViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(Util.LOG_TAG, "Sent Token Fragment Launched.");

        EditText receiverAddressEditText = view.findViewById(R.id.receiver_address_edit_text);
        EditText tokenAmountEditText = view.findViewById(R.id.token_amount_edit_text);
        Button sendTokenButton = view.findViewById(R.id.send_token_button);
        ImageButton scanReceiverAddressQRCodeButton = view.findViewById(R.id.scan_receiver_address_qr_code_button);
        ImageButton addTokenImageButton = view.findViewById(R.id.add_token_image_button);
        Spinner tokenAddressSpinner = getView().findViewById(R.id.token_list_spinner);

        addEditTextEnterListener(receiverAddressEditText, true);
        addEditTextEnterListener(tokenAmountEditText, false);

        addSendTokenButtonListener(sendTokenButton);
        addAddTokenButtonListener(addTokenImageButton);
        scanReceiverAddressQRCodeButton.setOnClickListener(buttonView -> startQRCodeScan(Util.REQUEST_CODE_RECIPIENT_ADDRESS));

        addTokenAddressSpinnerItemSelectionListener(tokenAddressSpinner);

        //Observe Selected Account, If selected account changes so does associated list of tokens
        mAccountInformationViewModel.getSelectedAccount().observe(this, account -> {
            clearTokenInfoFromUI();
            reloadSpinner(tokenAddressSpinner, account.getAddress());
        });

        //Observe Token Info Fetch Flag, If Info Fetched successfully then token info appears on UI via Databinding
        //If Fetching Token Info fails display error message
        mTokenViewModel.getTokenInfoFetchedSuccessfullyFlag().observe(this, responseValue -> {
            if (responseValue == Util.NEGATIVE_RESPONSE) {
                AlertUtil.showAlertDialog(getActivity(), Util.removeSDKSignatureFromErrorMessage(mTokenViewModel.getErrorMessage()));
            } else if (responseValue == Util.POSITIVE_RESPONSE) {
                //Fetch Token Balance
                EthereumAccount tokenAccount = mAccountInformationViewModel.getTokenAccount(
                        mAccountInformationViewModel.getSelectedAccount().getValue().getAddress(),
                        getTokenAddress());
                mTokenViewModel.getTokenBalance(tokenAccount);
            }
            Util.neutralizeConsumedFlag(mTokenViewModel.getTokenInfoFetchedSuccessfullyFlag());
        });

        //If Token Sent successfully show Transaction Hash
        //If not show error alert
        mTokenViewModel.getTokenSentSuccessfullyFlag().observe(this, responseValue -> {
            if (responseValue == Util.NEGATIVE_RESPONSE) {
                AlertUtil.showAlertDialog(getActivity(), Util.removeSDKSignatureFromErrorMessage(mTokenViewModel.getErrorMessage()));
            } else if (responseValue == Util.POSITIVE_RESPONSE) {
                UIUtil.displayTransactionHash(mTokenViewModel.getTransactionHash(), getActivity());
            }
            Util.neutralizeConsumedFlag(mTokenViewModel.getTokenSentSuccessfullyFlag());
            UIUtil.setButtonEnabled(sendTokenButton, true);
        });

    }

    private void reloadSpinner(Spinner tokenAddressSpinner, String accountAddress) {
        List<String> tokenAddressList = mTokenViewModel.createTokenAddressList(accountAddress, mAccountInformationViewModel.getAllAccounts());
        TokenAdapter tokenAdapter = new TokenAdapter(getContext(), 0, tokenAddressList);
        tokenAddressSpinner.setAdapter(tokenAdapter);
        Log.i(Util.LOG_TAG, "Token Addresses loaded for account: " + accountAddress);
    }

    private void addTokenAddressSpinnerItemSelectionListener(Spinner tokenAddressSpinner) {
        tokenAddressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTokenAddress = (String) parent.getItemAtPosition(position);
                Log.i(Util.LOG_TAG, "Token Address Spinner: " + selectedTokenAddress + " selected.");
                requestTokenInformation(selectedTokenAddress);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i(Util.LOG_TAG, "Token Address Spinner: Nothing selected.");
            }
        });
    }

    private void addAddTokenButtonListener(ImageButton addTokenButton) {
        addTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddTokenFragment()).commit();
            }
        });
    }

    private void addSendTokenButtonListener(Button sendTokenButton) {
        sendTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contractAddress = getTokenAddress();
                String toAddress = getReceiverAddress();
                String amount = getAmount();
                if (!mTokenViewModel.isAddressValid(contractAddress)) {
                    AlertUtil.showAlertDialog(getActivity(), "Contract Address is not valid");
                } else if (!mTokenViewModel.isAddressValid(toAddress)) {
                    AlertUtil.showAlertDialog(getActivity(), "Receiver Address is not valid");
                } else if (amount.isEmpty()) {
                    AlertUtil.showAlertDialog(getActivity(), "Amount cannot be empty");
                } else if (!isTokenBalanceSufficient(amount)) {
                    AlertUtil.showAlertDialog(getActivity(), "Insufficient token balance");
                } else {
                    UIUtil.setButtonEnabled(sendTokenButton, false);
                    mTokenViewModel.sendEthereumToken((EthereumAccount) mAccountInformationViewModel.getSelectedAccount().getValue(),
                            toAddress, mTokenViewModel.getTokenInfo().getValue().getAddress(),
                            new BigDecimal(amount));
                }
            }
        });
    }

    private boolean isTokenBalanceSufficient(String amount) {
        BigDecimal amountToSend = new BigDecimal(amount);
        BigDecimal tokenBalance = new BigDecimal(mTokenViewModel.getTokenBalance().getValue());

        if (tokenBalance.compareTo(amountToSend) == -1) {
            //tokenBalance < amountToSend
            return false;
        } else {
            return true;
        }
    }

    private void addEditTextEnterListener(EditText editText, boolean isEthereumAddress) {
        editText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                boolean isValueSubmitted = (keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER);
                if (isValueSubmitted) {
                    String inputString = editText.getText().toString();
                    if (isEthereumAddress) {
                        boolean isAddressValid = mTokenViewModel.isAddressValid(inputString);
                        if (!isAddressValid) {
                            AlertUtil.showAlertDialog(getActivity(), "Receiver Address is not valid!");
                        }
                    } else {
                        boolean isAmountEntered = !inputString.isEmpty();
                        if (!isAmountEntered) {
                            AlertUtil.showAlertDialog(getActivity(), "Amount cannot be empty!");
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void startQRCodeScan(int requestCode) {
        FragmentAndQRIntentIntegrator intentIntegrator = new FragmentAndQRIntentIntegrator(this);
        intentIntegrator.setRequestCode(requestCode);
        intentIntegrator.initiateScan();
    }

    private void requestTokenInformation(String tokenAddress) {
        showProgressBar(true);
        clearTokenInfoFromUI();
        //Fetch real Token Info
        mTokenViewModel.fetchTokenInformation(tokenAddress);
    }

    private String getTokenAddress() {
        Spinner tokenAddressSpinner = getView().findViewById(R.id.token_list_spinner);
        return (String) tokenAddressSpinner.getSelectedItem();
    }

    private String getReceiverAddress() {
        EditText receiverAddressEditText = getView().findViewById(R.id.receiver_address_edit_text);
        return receiverAddressEditText.getText().toString();
    }

    private String getAmount() {
        EditText tokenAmountEditText = getView().findViewById(R.id.token_amount_edit_text);
        return tokenAmountEditText.getText().toString();
    }

    private void setReceiverAddressOnUI(String receiverAddress) {
        EditText tokenAddressEditText = getView().findViewById(R.id.receiver_address_edit_text);
        tokenAddressEditText.setText(receiverAddress);
    }

    private void clearTokenInfoFromUI() {
        //Set Blank Token Info as Placeholder
        mTokenViewModel.getTokenInfo().postValue(new EthereumTokenInfo());
        mTokenViewModel.getTokenBalance().postValue("");
    }

    private void showProgressBar(Boolean isVisible) {
        mTokenViewModel.getTokenInformationIsLoading().postValue(isVisible);
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
                setReceiverAddressOnUI(resultAddress);
            } else {
                AlertUtil.showAlertDialog(getActivity(), "Recipient Address is not valid.");
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(Util.LOG_TAG, "QR Scan cancelled.");
        }
    }

}
