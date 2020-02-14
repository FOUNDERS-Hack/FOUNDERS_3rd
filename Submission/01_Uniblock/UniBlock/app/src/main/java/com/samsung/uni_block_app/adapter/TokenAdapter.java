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

import java.util.List;

public class TokenAdapter extends ArrayAdapter {
    public TokenAdapter(Context context, int resource, List<String> tokenAddressList) {
        super(context, resource, tokenAddressList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.token_list_item, parent, false);
        TextView tokenAddress = convertView.findViewById(R.id.token_item_address);
        tokenAddress.setText((String) getItem(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.token_list_item, parent, false);
        TextView tokenAddress = convertView.findViewById(R.id.token_item_address);
        tokenAddress.setText((String) getItem(position));
        return convertView;
    }
}
