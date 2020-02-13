package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.samsung.android.sdk.blockchain.*;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.samsung.android.sdk.blockchain.CoinType;
import com.samsung.android.sdk.blockchain.ListenableFutureTask;
import com.samsung.android.sdk.blockchain.SBlockchain;
import com.samsung.android.sdk.blockchain.account.Account;
import com.samsung.android.sdk.blockchain.account.ethereum.EthereumAccount;
import com.samsung.android.sdk.blockchain.coinservice.CoinNetworkInfo;
import com.samsung.android.sdk.blockchain.coinservice.CoinServiceFactory;
import com.samsung.android.sdk.blockchain.coinservice.ethereum.EthereumService;
import com.samsung.android.sdk.blockchain.exception.SsdkUnsupportedException;
import com.samsung.android.sdk.blockchain.network.EthereumNetworkType;
import com.samsung.android.sdk.blockchain.ui.CucumberWebView;
import com.samsung.android.sdk.blockchain.ui.OnSendTransactionListener;
import com.samsung.android.sdk.blockchain.wallet.HardwareWallet;
import com.samsung.android.sdk.blockchain.wallet.HardwareWalletType;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements OnSendTransactionListener {

    Button connectBtn;
    Button generateAccountBtn;
    Button getAccountBtn;
    Button paymentSheetBtn;
    Button sendSmartContractBtn;
    private SBlockchain sBlockchain;
    private HardwareWallet wallet;
    private Account generateAccount;
    private CucumberWebView webView;
    private Button webViewInitBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sBlockchain = new SBlockchain();
        try {
            sBlockchain = new SBlockchain();
            sBlockchain.initialize(getApplicationContext());
        } catch (
                SsdkUnsupportedException e) {
            if (e.getErrorType() == SsdkUnsupportedException.VENDOR_NOT_SUPPORTED) {
                Log.e("error", "Platform SDK is not support this device");
            }
        }


        connectBtn = findViewById(R.id.connect);
        generateAccountBtn = findViewById(R.id.generateaccounts);
        getAccountBtn = findViewById(R.id.getaccounts);
        paymentSheetBtn = findViewById(R.id.paymentsheet);
        sendSmartContractBtn = findViewById(R.id.sendsmartcontract);
        webView = findViewById(R.id.cucumberWebView);
        webViewInitBtn  = findViewById(R.id.webviewinit);



        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

        generateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
generate();
            }
        });


        getAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getAccounts();
            }
        });

        paymentSheetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
paymentSheet();
            }

        });

        sendSmartContractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendsmartcontract();
            }


        });
        webViewInitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewInit();
            }
        });
    }

    private void paymentSheet(){
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/33b24639f822476f95547cfad081949c"
        );

        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(
                        wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                );
        EthereumService ethereumService = (EthereumService) CoinServiceFactory.getCoinService(
                this,
                coinNetworkInfo
        );

        Intent intent = ethereumService
                .createEthereumPaymentSheetActivityIntent(
                        this,
                        wallet,
                        (EthereumAccount) accounts.get(0),
                        "0x1892C16cAdB3685Ed993282B1E3cbf6C3312509D",
                        new BigInteger("10000000000000"),
                        null,
                        null

                );
        startActivityForResult(intent, 0);

    }

    private void getAccounts() {
        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(wallet.getWalletId(), CoinType.ETH, EthereumNetworkType.ROPSTEN);
        Log.d("MyApp", Arrays.toString(new List[]{accounts}));
        Log.d("MyApp", accounts.get(0).getAddress());
    }

        private void connect() {
        sBlockchain.getHardwareWalletManager()
                .connect(HardwareWalletType.SAMSUNG, true)
                .setCallback(new ListenableFutureTask.Callback<HardwareWallet>() {
                    @Override
                    public void onSuccess(HardwareWallet hardwareWallet) {
                        wallet = hardwareWallet;
                    }

                    @Override
                    public void onCancelled(@NotNull InterruptedException e) {

                    }

                    @Override
                    public void onFailure(@NotNull ExecutionException e) {

                    }
                });
    }
    private  void webViewInit () {
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/33b24639f822476f95547cfad081949c"
        );

        List<Account> accounts = sBlockchain.getAccountManager()
                .getAccounts(
                        wallet.getWalletId(),
                        CoinType.ETH,
                        EthereumNetworkType.ROPSTEN
                );
        EthereumService ethereumService = (EthereumService) CoinServiceFactory.getCoinService(
                this,
                coinNetworkInfo
        );

        webView.init(ethereumService, accounts.get(0),this);
        webView.loadUrl("file:///android_asset/www/index.html");
    }
    private void generate() {
        CoinNetworkInfo coinNetworkInfo = new CoinNetworkInfo(
                CoinType.ETH,
                EthereumNetworkType.ROPSTEN,
                "https://ropsten.infura.io/v3/33b24639f822476f95547cfad081949c"

        );
        sBlockchain.getAccountManager()
                .generateNewAccount(wallet, coinNetworkInfo)
                .setCallback(new ListenableFutureTask.Callback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        generateAccount = account;
                        Log.d("MyApp", account.toString());
                    }

                    @Override
                    public void onFailure(@NotNull ExecutionException e) {
                        Log.d("MyApp", e.toString());
                    }

                    @Override
                    public void onCancelled(@NotNull InterruptedException e) {

                    }
                });
    }

    public void sendsmartcontract() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSendTransaction(
            @NotNull String requestId,
            @NotNull EthereumAccount fromAccount,
            @NotNull String toAddress,
            @org.jetbrains.annotations.Nullable BigInteger value,
            @org.jetbrains.annotations.Nullable String data,
            @org.jetbrains.annotations.Nullable BigInteger nonce
    ) {
        Intent intent =
                webView.createEthereumPaymentSheetActivityIntent(
                        this,
                        requestId,
                        wallet,
                        toAddress,
                        value,
                        data,
                        nonce
                );

        startActivityForResult(intent, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != 0) {
            return;
        }

        webView.onActivityResult(requestCode, resultCode, data);
    }


}
