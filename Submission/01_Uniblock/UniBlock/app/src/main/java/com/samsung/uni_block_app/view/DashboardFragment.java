package com.samsung.uni_block_app.view;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.model.HardwareWalletTypeModel;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.AccountInformationViewModel;
import com.samsung.uni_block_app.viewmodel.SetupViewModel;
import com.samsung.uni_block_app.viewmodel.TransactionViewModel;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private AccountInformationViewModel mAccountInformationViewModel;
    private TransactionViewModel mTransactionViewModel;
    private SetupViewModel mSetupViewModel;

    private ProgressBar balanceProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Always pass activity instead of fragment since parent activity stays alive even if fragment is switched.
        mAccountInformationViewModel = ViewModelProviders.of(getActivity()).get(AccountInformationViewModel.class);
        mTransactionViewModel = ViewModelProviders.of(getActivity()).get(TransactionViewModel.class);
        mSetupViewModel = ViewModelProviders.of(getActivity()).get(SetupViewModel.class);
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView balanceTextView = view.findViewById(R.id.balance_textview);
        TextView balanceUnitTextView = view.findViewById(R.id.balance_unit);
        TextView hardwareWalletTextView = view.findViewById(R.id.hardware_wallet_textview);
//        TextView selectedNetworkTestView = view.findViewById(R.id.network_textview);
//
//        selectedNetworkTestView.setText(mSetupViewModel.getCoinNetworkInfo().getNetworkType().toString());

        ArrayList<HardwareWalletTypeModel> hardWalletTypeModel = mSetupViewModel.getWalletModelList();
        HardwareWalletType hardWalletType = mSetupViewModel.getHardwareWallet().getWalletType();

        if (hardWalletType == hardWalletTypeModel.get(0).getWalletType()) {
            hardwareWalletTextView.setText(hardWalletTypeModel.get(0).getName());
        } else if (hardWalletType == hardWalletTypeModel.get(1).getWalletType()) {
            hardwareWalletTextView.setText(hardWalletTypeModel.get(1).getName());
        } else {
            hardwareWalletTextView.setText(hardWalletTypeModel.get(2).getName());
        }


        balanceUnitTextView.setText(mAccountInformationViewModel.getCoinNetworkInfo().getCoinType().toString());
        balanceProgressBar = view.findViewById(R.id.balance_progressbar);

        Button receiveButton = view.findViewById(R.id.receive_button);
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFragment(new ReceiveFragment());
            }
        });

        Button sendButton = view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFragment(new TransactionFragment());
                //hard coded changed bottom navigation selected fragment.
                BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
                navView.getMenu().getItem(3).setChecked(true);
            }
        });

        ImageButton refreshBalanceButton = view.findViewById(R.id.balance_refresh_button);
        refreshBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchBalanceRequest(mAccountInformationViewModel.getSelectedAccount().getValue(), refreshBalanceButton);
            }
        });

        //mAccountInformationViewModel.getSelectedAccount() returns MutableLiveData<Account>
        mAccountInformationViewModel.getSelectedAccount().observe(this, account -> {
            Log.i(Util.LOG_TAG, "Default Account Changed. Default Account HD Path: " + account.getHdPath());
            //Account HD Path and Address displayed using databinding
            fetchBalanceRequest(account, refreshBalanceButton);
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
    }

    private void launchFragment(Fragment fragmentToBeLaunched) {
        Log.i(Util.LOG_TAG, "Launching " + fragmentToBeLaunched.getClass().getSimpleName());
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentToBeLaunched).commit();
    }

    private void fetchBalanceRequest(Account account, ImageButton refreshBalanceButton) {
        if (account == null) {
            Log.e(Util.LOG_TAG, "fetchBalanceRequest with null account.");
            return;
        }

        //Refresh Balance
        Log.i(Util.LOG_TAG, "Request Balance Update.");
        UIUtil.toggleProgressBar(View.VISIBLE, balanceProgressBar, refreshBalanceButton);
        //isBalanceProgressBarVisible(true, refreshBalanceButton);
        mTransactionViewModel.fetchBalance(account);
    }
}
