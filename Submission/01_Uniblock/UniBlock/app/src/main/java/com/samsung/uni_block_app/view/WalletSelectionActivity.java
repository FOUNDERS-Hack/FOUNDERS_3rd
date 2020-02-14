package com.samsung.uni_block_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.adapter.SetupAdapter;
import com.samsung.uni_block_app.model.HardwareWalletTypeModel;
import com.samsung.uni_block_app.services.SBPManager;
import com.samsung.uni_block_app.services.SharedPreferenceManager;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.SetupViewModel;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import java.util.ArrayList;

public class WalletSelectionActivity extends AppCompatActivity {

    private SetupViewModel mWalletViewModel;
    private String selectedWalletName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_init_list);
        mWalletViewModel = ViewModelProviders.of(this).get(SetupViewModel.class);

        mWalletViewModel.isConnectedToHardwareWallet().observe(this, responseValue -> {
            if (responseValue == Util.POSITIVE_RESPONSE) {
                hideSelectionWalletLoadingProgressBar();
                UIUtil.showToast(getApplicationContext(), "Connected to " + selectedWalletName);
                // Storing HardwareWalletType to SharedPreference
                SharedPreferenceManager.setHardwareWalletTypeString(selectedWalletName);
                mWalletViewModel.isConnectedToHardwareWallet().postValue(Util.NEUTRAL_RESPONSE);
                launchCoinSelectionActivity();
            } else if (responseValue == Util.NEGATIVE_RESPONSE) {
                hideSelectionWalletLoadingProgressBar();
                AlertUtil.showAlertDialog(WalletSelectionActivity.this, mWalletViewModel.getErrorMessage());
            }
            Util.neutralizeConsumedFlag(mWalletViewModel.isConnectedToHardwareWallet());
        });

        populateAdapter();
    }

    private void populateAdapter() {

        RecyclerView walletRecyclerView;
        RecyclerView.Adapter walletRecyclerAdapter;
        TextView titleTextView;
        TextView totalSupportedItem;

        ArrayList<HardwareWalletTypeModel> walletList = mWalletViewModel.getWalletModelList();

        Log.i(Util.LOG_TAG, "Getting wallet list from viewmodel.");
        walletRecyclerView = findViewById(R.id.wallet_list_recycler_view);
        titleTextView = findViewById(R.id.setup_option_title);
        totalSupportedItem = findViewById(R.id.total_supported_item);
        titleTextView.setText(getString(R.string.wallet_selection_title));
        totalSupportedItem.setText(walletList.size() + " Hardware Wallets");

        // Create a LinearLayoutManager.
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        walletRecyclerView.setLayoutManager(mLayoutManager);

        SetupAdapter.OnItemClickListener walletClickListener = new SetupAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {

                Log.i(Util.LOG_TAG, "On Click --> Wallet Type: " + walletList.get(position).getWalletType());

                //Make progressbar visible
                showSelectionWalletLoadingProgressBar();

                //Connect to hardware wallet and then move to settings
                try {
                    SBPManager.getInstance().initializeSBlockChain(getApplicationContext());

                    HardwareWalletType selectedWallet = walletList.get(position).getWalletType();
                    selectedWalletName = selectedWallet.toString();
                    //Connect to HW : View/Activity > ViewModel > Model/Service > Samsung Blockchain Platform (SBP) SDK
                    mWalletViewModel.connectHardwareWallet(selectedWallet);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        walletRecyclerAdapter = new SetupAdapter(walletList, walletClickListener);

        // Setting Adapter
        walletRecyclerView.setAdapter(walletRecyclerAdapter);
    }

    private void launchCoinSelectionActivity() {
        Intent coinSelectionActivityIntent = new Intent(this, CoinSelectionActivity.class);
        coinSelectionActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(coinSelectionActivityIntent);
    }

    private void hideSelectionWalletLoadingProgressBar() {
        findViewById(R.id.hardware_wallet_connection_progressbar).setVisibility(View.GONE);
        findViewById(R.id.hardware_wallet_connection_progressbar_parent).setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void showSelectionWalletLoadingProgressBar() {
        findViewById(R.id.hardware_wallet_connection_progressbar).setVisibility(View.VISIBLE);
        findViewById(R.id.hardware_wallet_connection_progressbar_parent).setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
