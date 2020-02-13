package com.samsung.open_crypto_wallet_app.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.samsung.open_crypto_wallet_app.R;

import java.util.ArrayList;

public class AccountModelAdapter extends ArrayAdapter {

    public AccountModelAdapter(Context context, ArrayList<AccountModel> accountArrayList) {
        super(context, 0, accountArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initialiseView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initialiseView(position, convertView, parent);
    }

    private View initialiseView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView accountNameTextView = convertView.findViewById(R.id.accountNameTextView);
        TextView accountAddressTextView = convertView.findViewById(R.id.accountAddressTextView);

        AccountModel currentAccount = (AccountModel) getItem(position);

        //Set Account Details on UI
        accountNameTextView.setText(currentAccount.getAccountName());
        accountAddressTextView.setText(currentAccount.getPublicAddress());
        return convertView;
    }

}
