package com.samsung.uni_block_app.view;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.samsung.uni_block_app.R;
import com.samsung.uni_block_app.services.SBPManager;
import com.samsung.uni_block_app.services.TokenService;
import com.samsung.uni_block_app.util.Util;
import com.samsung.uni_block_app.viewmodel.AccountInformationViewModel;
import com.samsung.uni_block_app.viewmodel.PaymentViewModel;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.ui.CucumberWebView;
import com.samsung.android.sdk.blockchain.ui.OnSendTransactionListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebDAppMarketPlaceFragment extends Fragment {

    private AccountInformationViewModel mAccountInformationViewModel;
    private PaymentViewModel mPaymentViewModel;
    private ProgressBar mProgressBar;
    private CucumberWebView mDAppWebView;
    private String url;


    private EthereumAccount getDefaultAccountInfo() {
        // getting default account for so that, account change while spinner account is changed.
        Log.i(Util.LOG_TAG, "Getting default account.");
        MutableLiveData<Account> accounts = mAccountInformationViewModel.getSelectedAccount();
        return (EthereumAccount) accounts.getValue();
    }

    @Override
    public void onCreate(@androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaymentViewModel = ViewModelProviders.of(getActivity()).get(PaymentViewModel.class);
        mAccountInformationViewModel = ViewModelProviders.of(getActivity()).get(AccountInformationViewModel.class);
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dapp_web_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(Util.LOG_TAG, "Creating CucumberWebView object.");
        mDAppWebView = view.findViewById(R.id.dapp_web_view_container);

        mProgressBar = view.findViewById(R.id.web_page_loading_bar);

        EditText urlEditText = view.findViewById(R.id.url_input_edit_text);
        ImageButton loadUrlImageButton = view.findViewById(R.id.load_url_image_button);

        //default url.
        url = "https://web3js-test-app.herokuapp.com/index.html";
        urlEditText.setText(url);

        // go to another url.
        loadUrlImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(Util.LOG_TAG, "Load new url.");
                url = urlEditText.getText().toString();
                //check https before url.
                url = addHttps(url);
                urlEditText.setText(url);
                // clear url edit text focus.
                urlEditText.clearFocus();
                // hide soft keyboard when go button pressed.
                hideKeyBoard(view);
                webPageLoad(urlEditText, url);
            }
        });

        webPageLoad(urlEditText, url);
        // Navigate back in web page.
        webPageNavigator();

    }

    private String addHttps(String url) {
        if (checkValidUrl(url)) {
            if (!URLUtil.isHttpsUrl(url)) {
                if (URLUtil.isHttpUrl(url)) {
                    //Drop HTTP
                    url = url.replace("http://", "");
                }
                //Add HTTPS
                Log.d(Util.LOG_TAG, "Adding Https in the beginning of Url");
                return "https://" + url;
            } else {
                return url;
            }
        } else {
            Log.d(Util.LOG_TAG, "Invalid URL");
            AlertUtil.showAlertDialog(getActivity(), "Invalid URL!");
            return url = "";
        }
    }

    //checking if url is valid
    private boolean checkValidUrl(String url) {
        Pattern urlPattern = Patterns.WEB_URL;
        Matcher matchUrl = urlPattern.matcher(url.toLowerCase());
        return matchUrl.matches();
    }

    private void hideKeyBoard(View view) {
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void webPageLoad(EditText urlEditText, String url) {
        //call init binding with spinner(Account change action).
        mAccountInformationViewModel.getSelectedAccount().observe(this, account -> initWebView(account));

        // Enabling java script.
        WebSettings webSettings = mDAppWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Show website alerts as android dialog
        showWebAlert();

        // showing progressbar while webpage start to load.
        showProgressBar();

        // Loading a website.
        try {
            mDAppWebView.loadUrl(url);
        } catch (IllegalArgumentException d) {
            Log.d(Util.LOG_TAG, "Invalid Url");
        }
    }

    private void initWebView(Account account) {
        Log.i(Util.LOG_TAG, "Injecting coin and account info into CucumberWebView.");
        mDAppWebView.init(SBPManager.getInstance().getCoinService(), account, new OnSendTransactionListener() {
            @Override
            public void onSendTransaction(@NotNull String requestID, @NotNull EthereumAccount fromAccount, @NotNull String toAddress,
                                          @Nullable BigInteger productPrice, @Nullable String data, @Nullable BigInteger nonce) {

                //checking network connection.
                Log.d(Util.LOG_TAG, "Checking Network connection");
//                if (!Util.isInternetConnectionAvailable()) {
//                    Log.e(Util.LOG_TAG, "Network Error!");
//                    AlertUtil.showAlertDialog(getActivity(), Util.CONNECTION_ERROR);
//                } else {
                    Log.i(Util.LOG_TAG, "Calling payment sheet intent for Web Dapp.");
                    Intent dAppBuyIntent = mPaymentViewModel.getDAppPaymentIntent(fromAccount, toAddress, productPrice, data, nonce);
                    startActivityForResult(dAppBuyIntent, 0);

               // }
            }
        });

    }

    private void showWebAlert() {
        mDAppWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                DialogInterface.OnClickListener positiveActionListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(Util.LOG_TAG, "Wallet has integrated with website.");
                        result.confirm();
                    }
                };
                DialogInterface.OnClickListener negativeActionListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(Util.LOG_TAG, "Wallet has not integrated with website.");
                        result.cancel();
                    }
                };
                AlertUtil.showConfirmationAlertDialog(getActivity(), message, R.string.yes, positiveActionListener, R.string.no, negativeActionListener);
                return true;
            }
        });
    }

    private void showProgressBar() {
        mDAppWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                mProgressBar.setVisibility(View.VISIBLE);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    Log.d(Util.LOG_TAG, "Page loaded.");
                }
            }
        });
    }

    // Implemented Back button event
    private void webPageNavigator() {
        mDAppWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    CucumberWebView webView = (CucumberWebView) view;
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                Log.d(Util.LOG_TAG, "Go back to previous page.");
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    String transactionID = data.getStringExtra("txid");
                    Log.d(Util.LOG_TAG, "TransactionId : " + transactionID);
                    postDappTransactionSuccessFunctions(transactionID);
                    break;
                case Activity.RESULT_CANCELED:
                    Log.d(Util.LOG_TAG, "Transaction canceled by user.");
                    AlertUtil.showAlertDialog(getActivity(), "Transaction canceled.");
                    break;
            }
        }
    }

    private void postDappTransactionSuccessFunctions(String transactionID) {
        DialogInterface.OnClickListener positiveActionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        };
        DialogInterface.OnClickListener negativeActionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Transaction Hash", transactionID);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Transaction Hash Copied", Toast.LENGTH_SHORT).show();
            }
        };
        AlertUtil.showConfirmationAlertDialog(getActivity(), "Transaction Successful. Transaction Hash: " + transactionID, R.string.close, positiveActionListener, R.string.copy_to_clipboard, negativeActionListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDAppWebView != null) {
            mDAppWebView.destroy();
        }
    }
}
