package com.samsung.open_crypto_wallet_app.view;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.samsung.open_crypto_wallet_app.R;
import com.samsung.open_crypto_wallet_app.databinding.FragmentDashboardBinding;
import com.samsung.open_crypto_wallet_app.view_model.AccountViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDashboardBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        binding.setAccountInfo(AccountViewModel.getDefaultAccount());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageButton editAccountNameButton = view.findViewById(R.id.editAccountNameButton);
        setEditAccountNameButtonOnClickListener(editAccountNameButton);
        ImageButton refreshBalanceButton = view.findViewById(R.id.refreshBalanceButton);
        setRefreshBalanceButtonOnClickListener(refreshBalanceButton);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setSharePublicAddressButtonOnClickListener(Button sharePublicAddressButton) {
        sharePublicAddressButton.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, AccountViewModel.getDefaultAccountAddress());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });
    }

    private void setCopyPublicAddressButtonOnClickListener(Button copyPublicAddressButton) {
        copyPublicAddressButton.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) this.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("Public Address", AccountViewModel.getDefaultAccountAddress());
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this.getContext(), "Address Copied", Toast.LENGTH_SHORT).show();
        });
    }

    private void setRefreshBalanceButtonOnClickListener(ImageButton refreshBalanceButton) {
        refreshBalanceButton.setOnClickListener(v -> {
            AccountViewModel.fetchBalance();
        });
    }

    private void setEditAccountNameButtonOnClickListener(ImageButton editAccountNameButton) {
        editAccountNameButton.setOnClickListener(v -> {
            AlertUtil.editAccountDialog((Activity) v.getContext());
        });
    }
}
