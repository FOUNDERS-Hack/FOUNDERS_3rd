package com.samsung.open_crypto_wallet_app.view;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.samsung.open_crypto_wallet_app.R;
import com.samsung.open_crypto_wallet_app.Util;
import com.samsung.open_crypto_wallet_app.databinding.FragmentSendEtherBinding;
import com.samsung.open_crypto_wallet_app.view_model.AccountViewModel;
import com.samsung.open_crypto_wallet_app.view_model.TransactionViewModel;

import org.web3j.utils.Convert;

import java.math.BigInteger;

public class SendEtherFragment extends Fragment {

    private EditText mEtherAmountToSendEditText;
    private TextView mCurrentAmount;
    private RadioGroup mTransactionSpeedRadioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSendEtherBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_ether, container, false);
        binding.setAccountInfo(AccountViewModel.getDefaultAccount());
        binding.setTransactionModel(TransactionViewModel.getCurrentTransaction());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button signTransactionButton = view.findViewById(R.id.signTransactionButton);
        Button sendTransactionButton = view.findViewById(R.id.sendTransactionButton);
        mCurrentAmount = view.findViewById(R.id.currentBalanceTextView);
        mEtherAmountToSendEditText = view.findViewById(R.id.etherAmountEditText);
        mTransactionSpeedRadioGroup = view.findViewById(R.id.transactionSpeedRadioGroup);
        setSignTransactionButtonOnClickListener(signTransactionButton);
        setSendTransactionButtonOnClickListener(sendTransactionButton);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setSignTransactionButtonOnClickListener(Button signTransactionButton) {
        signTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 스마트 컨트랙트 주소 설정
                String toAddress = "0x73d4F1ce2064870e0f4E642734f8156F3434fD28"; // 스마트 컨트랙트 주소
                String amount = mEtherAmountToSendEditText.getText().toString();
                Integer selectedRadioButtonId = mTransactionSpeedRadioGroup.getCheckedRadioButtonId();

                if (toAddress.isEmpty()) {
                    Log.e(Util.LOG_TAG, "Send to Address is empty");
                    AlertUtil.showReceiverAddressEmptyDialog();
                    return;
                }

                if (amount.isEmpty()) {
                    Log.e(Util.LOG_TAG, "Sending Amount is empty");
                    AlertUtil.showEtherAmountEmptyDialog();
                    return;
                }

                BigInteger amountValue = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
                BigInteger currentValue = Convert.toWei(mCurrentAmount.getText().toString(), Convert.Unit.ETHER).toBigInteger();

                if (currentValue.compareTo(amountValue) < 0) {
                    Log.e(Util.LOG_TAG, "Account Balance is not enough");
                    AlertUtil.showEtherAmountNotEnoughDialog();
                    return;
                }

                if (selectedRadioButtonId == -1) {
                    Log.e(Util.LOG_TAG, "Sending Speed not selected");
                    AlertUtil.showTransactionSpeedNotSelectedDialog();
                    return;
                }

                TransactionViewModel.createAndSignTransaction(getActivity(), toAddress, amount, getTransactionSpeed(selectedRadioButtonId));

            }
        });
    }

    private void setSendTransactionButtonOnClickListener(Button sendTransactionButton) {
        sendTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TransactionViewModel.getCurrentTransaction().isSigned()) {
                    TransactionViewModel.sendTransaction(getActivity());
                } else {
                    AlertUtil.showTransactionNotSignedDialog();
                }
            }
        });
    }

    private String getTransactionSpeed(int selectedRadioButtonId) {
        if (selectedRadioButtonId == R.id.transactionSpeedSlow) {
            return "slow";
        }
        if (selectedRadioButtonId == R.id.transactionSpeedAverage) {
            return "average";
        }
        if (selectedRadioButtonId == R.id.transactionSpeedFast) {
            return "fast";
        }
        return "average";
    }

    private void startQRScanner() {
        FragmentAndQRIntentIntegrator sendEtherFragmentAndQRCodeScannerIntegrator = new FragmentAndQRIntentIntegrator(this);
        //InitiateScan function launches the QR Code Activity. Result of the activity is processed using onActivityResult
        sendEtherFragmentAndQRCodeScannerIntegrator.initiateScan();
    }

    //When QRCode Activity closes, it returns the result here
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d(Util.LOG_TAG, "Cancelled");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

class FragmentAndQRIntentIntegrator extends IntentIntegrator {

    private final Fragment fragment;

    public FragmentAndQRIntentIntegrator(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
    }

    @Override
    protected void startActivityForResult(Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
    }
}