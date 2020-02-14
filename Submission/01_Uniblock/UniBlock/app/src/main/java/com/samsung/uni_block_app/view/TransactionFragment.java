package com.samsung.uni_block_app.view;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.databinding.FragmentTransactionBinding;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.AccountInformationViewModel;
import com.samsung.uni_block_app.viewmodel.TransactionViewModel;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;

import java.math.BigDecimal;

public class TransactionFragment extends Fragment {

    private AccountInformationViewModel mAccountInformationViewModel;
    private TransactionViewModel mTransactionViewModel;

    private Button transferFundsButton;
    private ProgressBar balanceProgressBar;
    private ProgressBar transferFundsProgressBar;
    private ImageButton refreshBalanceButton;

    private String fundReceiversAddress;
    private BigDecimal transferAmount;
    private int transactionSpeed;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mAccountInformationViewModel = ViewModelProviders.of(getActivity()).get(AccountInformationViewModel.class);
        mTransactionViewModel = ViewModelProviders.of(getActivity()).get(TransactionViewModel.class);
        FragmentTransactionBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false);
        binding.setAccountInformationViewModel(mAccountInformationViewModel);
        binding.setTransactionViewModel(mTransactionViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText fundReceiversAddressEditText = view.findViewById(R.id.fund_receiver_address_edit_text);
        EditText transferAmountEditText = view.findViewById(R.id.fund_amount_edit_text);
        ImageButton scanReceiversAddress = view.findViewById(R.id.scan_fund_receiver_address_button);
        TextView balanceTextView = getView().findViewById(R.id.balance_text_view_tf);
        transferFundsButton = view.findViewById(R.id.transfer_funds_button);

        RadioGroup transactionSpeedGroup = view.findViewById(R.id.transaction_speed_radio_group);
        //'tf' denotes Transaction Fragment
        balanceProgressBar = view.findViewById(R.id.balance_progressbar_tf);
        transferFundsProgressBar = view.findViewById(R.id.transfer_funds_progressBar);
        refreshBalanceButton = view.findViewById(R.id.balance_refresh_button_tf);

        transferFundsProgressBar.setVisibility(View.GONE);

        fundReceiversAddressEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                boolean isValueSubmitted = (keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER);
                if (isValueSubmitted) {
                    String fundReceiversAddress = fundReceiversAddressEditText.getText().toString();
                    if (fundReceiversAddress.isEmpty()) {
                        AlertUtil.showAlertDialog(getActivity(), "Receiver\'s Address cannot be empty.");
                    } else if (!(mTransactionViewModel.isAddressValid(fundReceiversAddress))) {
                        AlertUtil.showAlertDialog(getActivity(), "Receiver\'s Address is not valid.");
                    }
                    return true;
                }
                return false;
            }
        });

        scanReceiversAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRCodeScan(Util.REQUEST_CODE_RECIPIENT_ADDRESS);
            }
        });

        transferAmountEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                boolean isValueSubmitted = (keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER);
                if (isValueSubmitted) {
                    String transferAmount = transferAmountEditText.getText().toString();
                    if (transferAmount.isEmpty()) {
                        AlertUtil.showAlertDialog(getActivity(), "Please fill \"Amount\" field.");
                    }
                    return true;
                }
                return false;
            }
        });

        transferFundsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int transactionSpeedID = transactionSpeedGroup.getCheckedRadioButtonId();
                String fundReceiversAddress = fundReceiversAddressEditText.getText().toString();
                String fundAmountString = transferAmountEditText.getText().toString();

                // Check fields not being empty at "Transfer Funds" buttonClick
                if (transactionSpeedID == -1) {
                    // No RadioButton Checked
                    AlertUtil.showAlertDialog(getActivity(), "Please Select a Transaction Speed.");
                    return;
                } else if (fundReceiversAddress.isEmpty()) {
                    AlertUtil.showAlertDialog(getActivity(), "Receiver\'s Address cannot be empty.");
                    return;
                } else if (fundAmountString.isEmpty()) {
                    AlertUtil.showAlertDialog(getActivity(), "Amount cannot be empty.");
                    return;
                }

                transactionSpeed = -1;
                switch (transactionSpeedID) {
                    case R.id.transaction_speed_slow:
                        transactionSpeed = Util.TRANSACTION_SPEED_SLOW;
                        break;
                    case R.id.transaction_speed_normal:
                        transactionSpeed = Util.TRANSACTION_SPEED_NORMAL;
                        break;
                    case R.id.transaction_speed_fast:
                        transactionSpeed = Util.TRANSACTION_SPEED_FAST;
                        break;
                    default:
                        Log.e(Util.LOG_TAG, "Transaction speed radio button error.");
                        break;
                }

                BigDecimal transferAmount = new BigDecimal(transferAmountEditText.getText().toString());
                setupFundTransfer(fundReceiversAddress, transferAmount);
            }
        });

        mTransactionViewModel.getBalanceFetchFlag().observe(this, responseValue -> {
            if (responseValue == Util.POSITIVE_RESPONSE) {
                UIUtil.displayBalance(mTransactionViewModel.getBalance(), balanceTextView, refreshBalanceButton, balanceProgressBar);
            } else if (responseValue == Util.NEGATIVE_RESPONSE) {
                UIUtil.displayBalance("", balanceTextView, refreshBalanceButton, balanceProgressBar);
                AlertUtil.showAlertDialog(getActivity(), "Could not refresh balance. \n" + Util.CONNECTION_ERROR);
            }
            Util.neutralizeConsumedFlag(mTransactionViewModel.getBalanceFetchFlag());
        });


        mAccountInformationViewModel.getSelectedAccount().observe(this, account -> {
            Log.i(Util.LOG_TAG, "Default Account Changed. Default Account HD Path: " + account.getHdPath());
            //Account HD Path and Address displayed using databinding
            fetchBalanceRequest(account, refreshBalanceButton);
        });

        refreshBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchBalanceRequest(mAccountInformationViewModel.getSelectedAccount().getValue(), refreshBalanceButton);
            }
        });

        mTransactionViewModel.getFeeInfoFetchFlag().observe(this, responseValue -> {
            if (responseValue == Util.POSITIVE_RESPONSE) {
                // FeeInformation fetched Successfully

                CoinType coinType = mAccountInformationViewModel.getCoinNetworkInfo().getCoinType();

                switch (coinType) {
                    case ETH:
                        EthereumAccount selectedAccount = (EthereumAccount) mAccountInformationViewModel.getSelectedAccount().getValue();
                        mTransactionViewModel.estimateEthereumGasLimit(selectedAccount, fundReceiversAddress, transferAmount);
                        break;
                    default:
                        String msg = "AeroWallet do not support this CoinType";
                        Log.e(Util.LOG_TAG, msg);
                        AlertUtil.showAlertDialog(getActivity(), msg);
                        break;
                }

            }

            Util.neutralizeConsumedFlag(mTransactionViewModel.getFeeInfoFetchFlag());
        });


        mTransactionViewModel.getGasLimitEstimationFlag().observe(this, responseValue -> {
            if (responseValue == Util.POSITIVE_RESPONSE) {
                mTransactionViewModel.sendEthereumTransaction(mAccountInformationViewModel.getSelectedAccount().getValue(),
                        transactionSpeed, fundReceiversAddress, transferAmount, mTransactionViewModel.getBalance());
            }

            Util.neutralizeConsumedFlag(mTransactionViewModel.getGasLimitEstimationFlag());
        });

        mTransactionViewModel.getTransactionStatusFlag().observe(this, responseValue -> {
            transferFundsProgressBar.setVisibility(View.GONE);
            transferFundsButton.setVisibility(View.VISIBLE);

            if (responseValue == Util.POSITIVE_RESPONSE) {
                postTransactionSuccessFunctions();

                // Clear UI fields
                fundReceiversAddressEditText.getText().clear();
                transferAmountEditText.getText().clear();
                transactionSpeedGroup.clearCheck();
            }

            if (responseValue == Util.NEGATIVE_RESPONSE) {
                AlertUtil.showAlertDialog(getActivity(), "Transaction Failed");
            }

            if (responseValue == Util.TRANSACTION_INSUFFICIENT_BALANCE) {
                AlertUtil.showAlertDialog(getActivity(), "Insufficient funds for gas, Transaction Not possible.");
            }

            Util.neutralizeConsumedFlag(mTransactionViewModel.getTransactionStatusFlag());
        });
    }

    private void postTransactionSuccessFunctions() {
        DialogInterface.OnClickListener positiveActionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
        DialogInterface.OnClickListener negativeActionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Transaction Hash", mTransactionViewModel.getTransactionHash());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Transaction Hash Copied", Toast.LENGTH_SHORT).show();
            }
        };
        AlertUtil.showConfirmationAlertDialog(getActivity(), "Transaction Successful. Transaction Hash: " + mTransactionViewModel.getTransactionHash(), R.string.close, positiveActionListener, R.string.copy_to_clipboard, negativeActionListener);
    }

    private void startQRCodeScan(int requestCode) {
        FragmentAndQRIntentIntegrator intentIntegrator = new FragmentAndQRIntentIntegrator(this);
        intentIntegrator.setRequestCode(requestCode);
        intentIntegrator.initiateScan();
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
            if (requestCode == Util.REQUEST_CODE_RECIPIENT_ADDRESS) {
                if (mTransactionViewModel.isAddressValid(resultAddress)) {
                    setReceiverAddressOnUI(resultAddress);
                } else {
                    AlertUtil.showAlertDialog(getActivity(), "Recipient Address is not valid.");
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(Util.LOG_TAG, "QR Scan cancelled.");
        }
    }

    private void setReceiverAddressOnUI(String receiversAddress) {
        EditText receiversAddressEditText = getView().findViewById(R.id.fund_receiver_address_edit_text);
        receiversAddressEditText.setText(receiversAddress);
    }

    private void fetchBalanceRequest(Account account, ImageButton refreshBalanceButton) {
        if (account == null) {
            Log.e(Util.LOG_TAG, "fetchBalanceRequest with null account.");
            return;
        }

        //Refresh Balance
        Log.i(Util.LOG_TAG, "Request Balance Update.");
        UIUtil.toggleProgressBar(View.VISIBLE, balanceProgressBar, refreshBalanceButton);
        mTransactionViewModel.fetchBalance(account);
    }

    private void setupFundTransfer(String fundReceiversAddressString, BigDecimal transferAmountDecimal) {
        if (mAccountInformationViewModel.getSelectedAccount().getValue() == null) {
            Log.e(Util.LOG_TAG, "Selected Account value is null");
            return;
        }
        if (!(mTransactionViewModel.isAddressValid(fundReceiversAddressString))) {
            AlertUtil.showAlertDialog(getActivity(), "Receiver\'s Address is not valid.");
            return;
        }

        // Check if balance is sufficient
        String balanceString = mTransactionViewModel.getBalance();
        BigDecimal balanceDecimal = new BigDecimal(balanceString);
        if (balanceDecimal.compareTo(transferAmountDecimal) < 0) {
            // balance < transferAmount
            AlertUtil.showAlertDialog(getActivity(), "Insufficient Balance.");
            Log.d(Util.LOG_TAG, "Insufficient Balance.");
            return;
        }

        fundReceiversAddress = fundReceiversAddressString;
        transferAmount = transferAmountDecimal;

        transferFundsButton.setVisibility(View.INVISIBLE);
        transferFundsProgressBar.setVisibility(View.VISIBLE);

        mTransactionViewModel.getFeeInformationFromSDK();
        //AutoTrigger based on flag: getFeeInformation > gasLimitEstimation >  TransferFunds
    }
}
