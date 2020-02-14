package com.samsung.uni_block_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.adapter.SetupAdapter;
import com.samsung.uni_block_app.model.CoinTypeModel;
import com.samsung.uni_block_app.services.SharedPreferenceManager;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.SetupViewModel;
import com.samsung.android.sdk.blockchain.CoinType;

import java.util.ArrayList;

public class CoinSelectionActivity extends AppCompatActivity {

    private SetupViewModel mCoinSelectionViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_init_list);
        mCoinSelectionViewModel = ViewModelProviders.of(this).get(SetupViewModel.class);
        populateAdapter();
    }

    private void populateAdapter() {

        RecyclerView coinRecyclerView;
        RecyclerView.Adapter coinRecyclerAdapter;
        TextView titleTextView;
        TextView totalSupportedItem;

        // CoinList: View/Activity > ViewModel > Model/Service > Samsung Blockchain Platform (SBP) SDK
        ArrayList<CoinTypeModel> coinList = mCoinSelectionViewModel.getCoinTypeModelList();

        coinRecyclerView = findViewById(R.id.wallet_list_recycler_view);
        // Setting title of the page.
        titleTextView = findViewById(R.id.setup_option_title);
        totalSupportedItem = findViewById(R.id.total_supported_item);
        titleTextView.setText(getString(R.string.coin_selection_title));
        totalSupportedItem.setText(coinList.size() + " Coin available");

        // Create a LinearLayoutManager.
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        coinRecyclerView.setLayoutManager(mLayoutManager);

        SetupAdapter.OnItemClickListener coinClickListener = new SetupAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                // Storing CoinType to SharedPreference
                SharedPreferenceManager.setCoinTypeString(coinList.get(position).getCoinType().toString());
                launchNetworkSelectionActivity(coinList.get(position).getCoinType());
            }
        };

        coinRecyclerAdapter = new SetupAdapter(coinList, coinClickListener);

        // Setting Adapter
        coinRecyclerView.setAdapter(coinRecyclerAdapter);
    }

    private void launchNetworkSelectionActivity(CoinType coinType) {
        Intent networkActivityIntent = new Intent(this, NetworkSelectionActivity.class);
        //networkActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle dataBundle = new Bundle();
        dataBundle.putSerializable(Util.COIN_TYPE, coinType);
        networkActivityIntent.putExtras(dataBundle);

        startActivity(networkActivityIntent);
    }
}
