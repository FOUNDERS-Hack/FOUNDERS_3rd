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
import com.samsung.android.sdk.blockchain.account.Account;

import java.util.List;

public class AccountAdapter extends ArrayAdapter {

    public AccountAdapter(Context context, List<Account> accountArrayList) {
        super(context, 0, accountArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_spinner_list_item, parent, false);
        }

        TextView accountHDPath = convertView.findViewById(R.id.spinner_item_hdpath);
        TextView accountPublicAddress = convertView.findViewById(R.id.spinner_item_public_address);

        //Always load top item as selected item is always at top
        Account currentAccount = (Account) getItem(0);

        //Set Account Details on UI
        accountHDPath.setText("");
        accountPublicAddress.setText(currentAccount.getAddress());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_spinner_list_item_primary, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_spinner_list_item_secondary, parent, false);
        }

        TextView accountHDPath = convertView.findViewById(R.id.spinner_item_hdpath);
        TextView accountPublicAddress = convertView.findViewById(R.id.spinner_item_public_address);

        Account currentAccount = (Account) getItem(position);

        //Set Account Details on UI
        accountHDPath.setText("");
        accountPublicAddress.setText(currentAccount.getAddress());
        return convertView;
    }
}
