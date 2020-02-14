package com.samsung.uni_block_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.adapter.SetupAdapter;
import com.samsung.uni_block_app.model.NetworkTypeModel;
import com.samsung.uni_block_app.services.SharedPreferenceManager;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.SetupViewModel;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import java.util.ArrayList;

public class NetworkSelectionActivity extends AppCompatActivity {

    private SetupViewModel mSetupViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_init_list);
        mSetupViewModel = ViewModelProviders.of(this).get(SetupViewModel.class);

        Bundle coinNetworkInfoBundle = getIntent().getExtras();

        populateAdapter(coinNetworkInfoBundle);
    }

    private void populateAdapter(Bundle coinNetworkInfo) {

        RecyclerView networkRecyclerView;
        RecyclerView.Adapter networkRecyclerAdapter;
        TextView titleTextView;
        TextView totalSupportedItem;

        // Supported Network List: View/Activity > ViewModel > Model/Service > Samsung Blockchain Platform (SBP) SDK
        ArrayList<NetworkTypeModel> networkList = mSetupViewModel.getNetworkTypeModelList();

        networkRecyclerView = findViewById(R.id.wallet_list_recycler_view);
        // Setting title of the page.
        titleTextView = findViewById(R.id.setup_option_title);
        totalSupportedItem = findViewById(R.id.total_supported_item);
        titleTextView.setText(getString(R.string.network_selection_title));
        totalSupportedItem.setText(networkList.size() + " Networks available");

        // Create a LinearLayoutManager.
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        networkRecyclerView.setLayoutManager(mLayoutManager);

        SetupAdapter.OnItemClickListener networkClickListener = new SetupAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                // coinNetworkInfo -> Sending Coin Info received from CoinSelectionActivity,
                // Network Type -> Sending selected network type

                // Storing HardwareWalletType to SharedPreference
                SharedPreferenceManager.setNetworkTypeString(networkList.get(position).getNetworkType().toString());
                // Flagging SharedPreference valid, Only if Wallet is Samsung Blockchain KeyStore
                if (mSetupViewModel.getHardwareWallet().getWalletType() == HardwareWalletType.SAMSUNG) {
                    SharedPreferenceManager.setValid(true);
                } else {
                    SharedPreferenceManager.setValid(false);
                    Log.d(Util.LOG_TAG, "Connected HardwareWallet is not Samsung Blockchain KeyStore. SharedPreference is set inValid.");
                }
                launchNavigationActivity(coinNetworkInfo, networkList.get(position));
            }
        };

        networkRecyclerAdapter = new SetupAdapter(networkList, networkClickListener);

        // Setting Adapter
        networkRecyclerView.setAdapter(networkRecyclerAdapter);
    }

    private void launchNavigationActivity(Bundle coinNetworkInfo, NetworkTypeModel networkTypeModel) {
        Intent navigationActivityIntent = new Intent(this, NavigationActivity.class);
        navigationActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        CoinType coinType = (CoinType) coinNetworkInfo.get(Util.COIN_TYPE);
        switch (coinType) {
            case ETH:
                coinNetworkInfo.putSerializable(Util.NETWORK_TYPE, (EthereumNetworkType) networkTypeModel.getNetworkType());
                break;
            default:
                String msg = "UniBlock do not support this CoinType";
                Log.e(Util.LOG_TAG, msg);
                AlertUtil.showAlertDialog(this, msg);
                break;
        }
        navigationActivityIntent.putExtras(coinNetworkInfo);
        startActivity(navigationActivityIntent);
    }

}
