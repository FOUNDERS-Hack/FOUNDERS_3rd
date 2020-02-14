package com.samsung.uni_block_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.samsung.uni_block_app.R;
import com.samsung.android.sdk.blockchain.CoinType;

import java.util.List;

public class CoinTypeAdapter extends ArrayAdapter {

    public CoinTypeAdapter(Context context, List<CoinType> coinTypes) {
        super(context, 0, coinTypes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initialiseView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initialiseView(position, convertView, parent);
    }

    private View initialiseView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.coin_spinner_list_item, parent, false);
        }

        TextView coinNameTextView = convertView.findViewById(R.id.list_item_coin_name);

        CoinType currentCoinType = (CoinType) getItem(position);

        //Set Account Details on UI
        coinNameTextView.setText(currentCoinType.name());
        return convertView;
    }

}
