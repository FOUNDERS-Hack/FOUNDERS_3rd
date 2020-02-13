package com.samsung.open_crypto_wallet_app.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.samsung.open_crypto_wallet_app.R;
import com.samsung.open_crypto_wallet_app.SharedPreferenceManager;
import com.samsung.open_crypto_wallet_app.view_model.AccountViewModel;

public class TransactionHistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        WebView transactionHistoryWebView = view.findViewById(R.id.etherscan_webview);
        setUpEtherScanTransactionHistoryWebView(transactionHistoryWebView);
        super.onViewCreated(view, savedInstanceState);
    }

    private void setUpEtherScanTransactionHistoryWebView(WebView transactionHistoryWebView) {
        transactionHistoryWebView.setWebChromeClient(new WebChromeClient());
        transactionHistoryWebView.setWebViewClient(new WebViewClient());
        transactionHistoryWebView.loadUrl("https://" + getNetworkName() + "etherscan.io/address/" + AccountViewModel.getDefaultAccountAddress());
    }

    //Code for preparing etherscan URL to hit
    private String getNetworkName() {
        String networkName = SharedPreferenceManager.getDefaultNetwork(getActivity());
        if (networkName.equals("mainnet")) {
            networkName = "";
        } else {
            networkName = networkName.concat(".");
        }
        return networkName;
    }
}
