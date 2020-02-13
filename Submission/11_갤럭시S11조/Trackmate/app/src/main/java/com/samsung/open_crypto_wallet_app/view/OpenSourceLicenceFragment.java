package com.samsung.open_crypto_wallet_app.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.samsung.open_crypto_wallet_app.R;

public class OpenSourceLicenceFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.opensourcelicence_detail, container, false);
        WebView mWebView = mView.findViewById(R.id.opensource_licence_detail_webtview);

        mWebView.loadUrl("file:///android_asset/open_source_licenses.txt");

        WebSettings settings = mWebView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setTextZoom(150);
        return mView;
    }

}
